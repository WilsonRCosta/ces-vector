package service.vector.ope.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import service.vector.apim.client.IAx;
import service.vector.apim.client.ILu;
import service.vector.apim.dto.RegisterRequest;
import service.vector.apim.dto.ResponseDto;
import service.vector.apim.service.IXaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import static service.vector.ope.service.VectorService.tmHostUrl;

@ApplicationScoped
public class XaService implements IXaService {
    private static final Logger logger = Logger.getLogger(XaService.class.getName());
    private static final Gson gson = new GsonBuilder().create();

    @ConfigProperty(name = "hostname")
    String hostname;

    @Inject
    @RestClient
    ILu lookupService;

    @Inject
    VectorService vector;

    /** Calls invariantCheck to verify if the vector of the specified transaction has the mandatory sum of values
     * @param tid transaction id
     * @return boolean indicating if the sum is correct
     */
    @Override
    public Uni<Integer> xaPrepare(final long tid) {
        logger.info("Preparing transaction: " + tid);
        if (!vector.tidIsValid(tid)) {
            logger.severe("Invalid transaction id inserted.");
            return Uni.createFrom().item(null);
        }
        int vectorSum = vector.getTempTxVector().stream().mapToInt(Integer::intValue).sum();
        logger.info("Sum of vector values for " + tid + ": " + vectorSum);
        return Uni.createFrom().item(vectorSum);
    }

    /** Replaces the global vector with the new vector for the specified transaction id
     * @param tid transaction id
     */
    @Override
    public Uni<Void> xaCommit(final long tid) {
        logger.info("Committing transaction: " + tid);
        if (!vector.tidIsValid(tid)) {
            logger.severe("Invalid transaction id inserted.");
            return Uni.createFrom().voidItem();
        }
        List<Integer> newVector = vector.getTempTxVector();
        logger.info("New global vector from " + tid + ": " + newVector);
        vector.setGlobalVector(newVector);
        return Uni.createFrom().voidItem();
    }

    /** Removes the temporary vector of the specified transaction
     * @param tid transaction id
     */
    @Override
    public Uni<Void> xaRollback(final long tid) {
        logger.info("Rolling back transaction: " + tid);
        if (!vector.tidIsValid(tid)) {
            logger.severe("Invalid transaction id inserted.");
            return Uni.createFrom().voidItem();
        }
        logger.info("Rolling back transaction from: " + tid);
        vector.resetTempTxVector();
        return Uni.createFrom().voidItem();
    }

    /** Removes the temporary vector for the specified transaction and calls unregister of transaction manager
     * @param tid transaction id
     */
    @Override
    public Uni<Void> xaClose(final long tid) {
        logger.info("Closing transaction: " + tid);
        if (!vector.tidIsValid(tid)) {
            logger.severe("Invalid transaction id inserted.");
            return Uni.createFrom().voidItem();
        }
        vector.resetTempTxVector();
        vector.currentTxs.remove(tid);

        lookupService.unRegister(gson.toJson(new RegisterRequest("VECTOR", hostname))).subscribe().with(resp -> {
            Response innerResp = RestClientBuilder.newBuilder().baseUri(URI.create("http://" + tmHostUrl))
                    .build(IAx.class)
                    .axUnregister(tid, hostname).await().atMost(Duration.ofSeconds(5));
            ResponseDto<Void> dto = innerResp.readEntity(new GenericType<>() { });
            logger.info(dto.getMsg());
            logger.info("Closed transaction: " + tid);
        });

        return Uni.createFrom().voidItem();
    }
}
