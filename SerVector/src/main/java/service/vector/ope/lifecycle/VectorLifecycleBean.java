package service.vector.ope.lifecycle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import service.vector.apim.client.ILu;
import service.vector.apim.dto.RegisterRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class VectorLifecycleBean {
    private static final Logger logger = Logger.getLogger(VectorLifecycleBean.class.getName());
    private static final Gson gson = new GsonBuilder().create();

    @Inject
    @RestClient
    ILu lookupService;

    @ConfigProperty(name = "hostname")
    String hostname;

    void onStart(@Observes StartupEvent ev) {
        logger.info("The application is starting...");
        lookupService
                .register(gson.toJson(new RegisterRequest("VECTOR", hostname)))
                .subscribe()
                .with(response -> logger.info(hostname + " registered in Lookup Service"));
    }

    void onStop(@Observes ShutdownEvent ev) {
        logger.info("The application is stopping...");
        lookupService
                .unRegister(gson.toJson(new RegisterRequest("VECTOR", hostname)))
                .subscribe()
                .with(response -> logger.info(hostname + " unregister in Lookup Service"));
    }

}