package service.vector.ope.service;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import service.vector.apim.client.IAx;
import service.vector.apim.client.ILu;
import service.vector.apim.dto.ResponseDto;
import service.vector.apim.service.IVectorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@ApplicationScoped
public class VectorService implements IVectorService {

    private static final Logger logger = Logger.getLogger(VectorService.class.getName());
    private List<Integer> globalVector = Arrays.asList(300, 234, 177, 789);
    private List<Integer> tempTxVector = new ArrayList<>(globalVector);

    protected final ConcurrentLinkedQueue<Long> currentTxs = new ConcurrentLinkedQueue<>();

    @ConfigProperty(name = "hostname")
    String hostname;

    @Inject
    @RestClient
    ILu lookupService;

    public static String tmHostUrl; // "http://transactionmanager-service:9001"; //"http://127.0.0.1:9001";

    public List<Integer> getTempTxVector() {
        return tempTxVector;
    }

    public void resetTempTxVector() {
        this.tempTxVector = new ArrayList<>(globalVector);
    }

    public void setGlobalVector(List<Integer> globalVector) {
        this.globalVector = globalVector;
    }

    protected boolean tidIsValid(long tid) {
        return currentTxs.stream().anyMatch(tx -> tx == tid);
    }

    @Override
    public Uni<Integer> read(final long tid, final int index) throws ArrayIndexOutOfBoundsException {
        logger.info("Reading position [" + index + "] from vector " + globalVector);
        checkRegistration(tid);
        return Uni.createFrom().item(globalVector.get(index));
    }

    @Override
    public Uni<Void> write(final long tid, final int index, final int value) throws ArrayIndexOutOfBoundsException {
        checkRegistration(tid);
        tempTxVector.set(index, value);
        logger.info("ID [" + tid + "]: Writing value [" + value + "] in position [" + index + "]");
        return Uni.createFrom().voidItem();
    }

    private void checkRegistration(final long xid) {
        if (currentTxs.parallelStream().noneMatch(tx -> tx == xid)) {
            logger.info("Registering transaction " + xid);
            currentTxs.add(xid);
            logger.info("Transaction Manager hostname: " + tmHostUrl);
            lookupService.lookup("TM").subscribe().with(resp -> {
                ResponseDto<String> dto = resp.readEntity(new GenericType<>() { });
                tmHostUrl = dto.getValue();
                Response innerResp = RestClientBuilder.newBuilder().baseUri(URI.create("http://" + tmHostUrl))
                        .build(IAx.class)
                        .axRegister(xid, hostname).await().atMost(Duration.ofSeconds(5));
                ResponseDto<Void> innerDto = innerResp.readEntity(new GenericType<>() { });
                logger.info(innerDto.getMsg());
            });
        }
    }
}
