package service.tm.apim.resource;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IAxResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/{tid}")
    Uni<Response> axRegister(@PathParam("tid") Long tid, @QueryParam("endpoint") String endpoint);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unregister/{tid}")
    Uni<Response> axUnregister(@PathParam("tid") Long tid, @QueryParam("endpoint") String endpoint);

}
