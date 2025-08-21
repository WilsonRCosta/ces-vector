package service.tm.ope.lifecycle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import service.tm.apim.client.ILu;
import service.tm.apim.dto.RegisterRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class TransactionManagerLifecycleBean {
    private static final Logger logger = Logger.getLogger(TransactionManagerLifecycleBean.class.getName());
    private static final Gson gson = new GsonBuilder().create();

    @Inject
    @RestClient
    ILu lookupService;

    @ConfigProperty(name = "hostname")
    String hostname;

    void onStart(@Observes StartupEvent ev) {
        logger.info("The application is starting...");
        lookupService
                .register(gson.toJson(new RegisterRequest("TM", hostname)))
                .subscribe()
                .with(response -> logger.info(hostname + " registered in Lookup Service"));
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
        lookupService
                .unRegister(gson.toJson(new RegisterRequest("TM", hostname)))
                .subscribe()
                .with(response -> logger.info(hostname + " unregister in Lookup Service"));
    }

}