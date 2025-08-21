package service.tm.apim.client;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Consumes(MediaType.APPLICATION_JSON)
@Path("/vector/xa")
public interface IXa {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/prepare/{tid}")
    Uni<Response> xaPrepare(@PathParam("tid") Long tid);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/commit/{tid}")
    Uni<Response> xaCommit(@PathParam("tid") Long tid);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rollback/{tid}")
    Uni<Response> xaRollback(@PathParam("tid") Long tid);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/close/{tid}")
    Uni<Response> xaClose(@PathParam("tid") Long tid);
}
