package service.vector.apim.resource;

import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface IXaResource {

    /** Inform RM to prepare commitment of performed work
     * @param tid
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/prepare/{tid}")
    Uni<Response> xaPrepare(@PathParam("tid") Long tid);

    /** Commit the work associated with the Xid
     * @param tid
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/commit/{tid}")
    Uni<Response> xaCommit(@PathParam("tid") Long tid);

    /** Inform RM to rollback work performed
     * @param tid
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/rollback/{tid}")
    Uni<Response> xaRollback(@PathParam("tid") Long tid);

    /** Close specific resource manager
     * @param tid
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/close/{tid}")
    Uni<Response> xaClose(@PathParam("tid") Long tid);
}
