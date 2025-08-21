package service.tplm.apim.client;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "service-lu")
@Path("/lu")
public interface ILu {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    Uni<Response> register(final String registerRequest);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lookup")
    Uni<Response> lookup(@QueryParam("type") final String type);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unregister")
    Uni<Response> unRegister(final String registerRequest);
}