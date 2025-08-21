package service.vector.apim.client;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/tm/ax")
public interface IAx {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/register/{tid}")
    @Blocking
    Uni<Response> axRegister(
            @PathParam("tid") Long tid,
            @QueryParam("endpoint") String endpoint
    );

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unregister/{tid}")
    @Blocking
    Uni<Response> axUnregister(@PathParam("tid") Long tid, @QueryParam("endpoint") String endpoint);

}
