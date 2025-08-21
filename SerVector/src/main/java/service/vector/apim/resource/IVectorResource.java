package service.vector.apim.resource;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IVectorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/read/{tid}")
    Uni<Response> read(@PathParam("tid") long tid, @QueryParam("index") int index);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/write/{tid}")
    Uni<Response> write(@PathParam("tid") long tid, @QueryParam("index") int index, @QueryParam("value") int value);
}
