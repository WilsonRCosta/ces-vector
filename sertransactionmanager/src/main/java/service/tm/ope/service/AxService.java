package service.tm.ope.service;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.tm.apim.service.IAxService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AxService implements IAxService {
    private static final Logger logger = LoggerFactory.getLogger(AxService.class.getName());

    @Inject
    TxService tx;

    @Override
    public Uni<Void> axRegister(final Long tid, final String endpoint) {
        logger.info("Registering " + endpoint + " in transaction " + tid);
        tx.checkTransaction(tid).ifPresent((t) -> t.addResourceToBranches(endpoint));
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Void> axUnregister(final Long tid, final String endpoint) {
        logger.info("Unregistering " + endpoint + " in transaction " + tid);
        tx.checkTransaction(tid).ifPresent((t) -> {
            t.removeResourceFromBranches(endpoint);
            tx.removeTxTransaction(t);
        });

        return Uni.createFrom().voidItem();
    }
}
