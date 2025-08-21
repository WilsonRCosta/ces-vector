package service.tm.apim.resource;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ITxResource {

    @GET
    @Path("/begin")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> txBegin();

    @POST
    @Path("/close/{tid}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> txClose(@PathParam("tid") Long tid);

    @POST
    @Path("/commit/{tid}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> txCommit(@PathParam("tid") Long tid);

    @POST
    @Path("/rollback/{tid}")
    @Produces(MediaType.APPLICATION_JSON)
    Uni<Response> txRollback(@PathParam("tid") Long tid);
}
