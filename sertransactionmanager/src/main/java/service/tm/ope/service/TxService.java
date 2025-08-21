package service.tm.ope.service;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import service.tm.apim.client.IXa;
import service.tm.apim.dto.ResponseDto;
import service.tm.apim.service.ITxService;
import service.tm.apim.xid.GlobalXid;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
public class TxService implements ITxService {

    private static final Integer SUM_CHECK = 1500;
    private static final Logger logger = Logger.getLogger(TxService.class.getName());
    private final List<GlobalXid> txTransactions = new ArrayList<>();

    @ConfigProperty(name = "hostname")
    String hostname;

    @Override
    public Uni<Long> txBegin() {
        final GlobalXid xid = new GlobalXid(hostname);
        txTransactions.add(xid);
        logger.info("New transaction: " + xid.getTransactionId());
        return Uni.createFrom().item(xid.getTransactionId());
    }

    @Override
    public Uni<Void> txCommit(Long xid) {
        Optional<GlobalXid> tx = checkTransaction(xid);

        if (tx.isEmpty() || !tx.get().getBranchesMap().containsKey(xid)) {
            logger.severe("Error. Transaction " + xid + " not registered.");
            return Uni.createFrom().voidItem();
        }
        final var values = Uni.combine()
                .all()
                .unis(tx.get()
                        .getBranchesMap()
                        .get(xid)
                        .stream()
                        .map(branch -> {
                            logger.info("Preparing commit for transaction [" + xid + "] on branch: " + branch.getBranchId());
                            return RestClientBuilder.newBuilder().baseUri(URI.create(branch.getIdentifier()))
                                    .build(IXa.class).xaPrepare(xid).onItem().transform(resp -> {
                                        ResponseDto<Integer> dto = resp.readEntity(new GenericType<>() {});
                                        return dto.getValue();
                                    });
                        })
                        .collect(Collectors.toList()))
                .collectFailures()
                .combinedWith(item -> item).await().atMost(Duration.ofSeconds(5));

        int invariantCheckSum = values.stream().map(Integer.class::cast).mapToInt(Integer::valueOf).sum();

        // Sum of values needs to correspond to: SUM_CHECK * number of VectorServices
        logger.info("Number of current branches: " + values.size());
        if (invariantCheckSum == SUM_CHECK * values.size()) {
            logger.info("Invariant check result as been verified. Committing transaction " + xid);

            tx.get().getBranchesMap().get(xid).forEach(branch ->
                    RestClientBuilder.newBuilder().baseUri(URI.create(branch.getIdentifier()))
                            .build(IXa.class)
                            .xaCommit(xid).subscribe().with(resp -> {
                                ResponseDto<Void> dto = resp.readEntity(new GenericType<>() {});
                                logger.info(dto.getMsg());
                            }));
            logger.info("Transaction " + xid + " committed for branches.");

            return Uni.createFrom().voidItem();
        } else {
            logger.severe("Error. Invariant check did not match (" + invariantCheckSum + " - " + SUM_CHECK + "). Transaction " + xid + " rolling back.");
            return txRollback(xid);
        }
    }

    @Override
    public Uni<Void> txRollback(Long xid) {
        Optional<GlobalXid> tx = checkTransaction(xid);
        if (tx.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        tx.get().getBranchesMap().get(xid).forEach(branch -> {
                RestClientBuilder.newBuilder().baseUri(URI.create(branch.getIdentifier()))
                        .build(IXa.class)
                        .xaRollback(xid).subscribe().with(tid -> logger.severe("Rolled back " + xid));
        });
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> txClose(Long xid) {
        Optional<GlobalXid> tx = checkTransaction(xid);
        if (tx.isEmpty()) {
            return Uni.createFrom().voidItem();
        }
        tx.get().getBranchesMap().get(xid).forEach(branch -> {
            RestClientBuilder.newBuilder().baseUri(URI.create(branch.getIdentifier()))
                    .build(IXa.class)
                    .xaClose(xid).subscribe().with(tid -> logger.severe("Rolled back " + xid));
        });
        return Uni.createFrom().voidItem();
    }

    protected Optional<GlobalXid> checkTransaction(Long tid) {
        Optional<GlobalXid> searchTransaction = txTransactions.stream().filter(tx -> tx.getTransactionId() == tid).findFirst();
        if (searchTransaction.isEmpty()) {
            logger.info("Transaction " + tid + " does not exist.");
            return Optional.empty();
        }
        return searchTransaction;
    }

    protected void removeTxTransaction(final GlobalXid transaction) {
        if(txTransactions.removeIf(t -> t.equals(transaction))) {
            logger.info("Transaction " + transaction.getTransactionId() + " closed.");
        } else {
            logger.severe("Transaction " + transaction.getTransactionId() + " does not exist.");
        }
    }
}
