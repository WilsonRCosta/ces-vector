package service.tplm.apim.resource;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import service.tplm.apim.model.LockElementsDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface ITplmResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/acquire")
    @Blocking
    Uni<Response> acquireLocks(final LockElementsDto locks);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/release")
    @Blocking
    Uni<Response> releaseLocks(final LockElementsDto locks);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/locks")
    Uni<Response> listTransactionLocks(@QueryParam("tid") final Long tid);
}
