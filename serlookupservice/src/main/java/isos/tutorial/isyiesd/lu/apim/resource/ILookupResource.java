package isos.tutorial.isyiesd.lu.apim.resource;

import io.smallrye.mutiny.Uni;
import isos.tutorial.isyiesd.lu.apim.dto.RegisterRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ILookupResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register")
    Uni<Response> register(final RegisterRequest registerRequest);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/lookup")
    Uni<Response> lookup(@QueryParam("type") final String type);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unregister")
    Uni<Response> unRegister(final RegisterRequest registerRequest);

}
