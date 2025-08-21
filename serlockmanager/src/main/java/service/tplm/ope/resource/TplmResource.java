package service.tplm.ope.resource;

import io.smallrye.mutiny.Uni;
import service.tplm.apim.model.LockElementsDto;
import service.tplm.apim.resource.ITplmResource;
import service.tplm.apim.service.ITplmService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/tplm")
public class TplmResource extends BaseResource implements ITplmResource {

    @Inject
    ITplmService tplmService;

    @Override
    public Uni<Response> acquireLocks(final LockElementsDto locks) {
        return ok(tplmService.verifyLocks(locks.getLockElements()), "Process finished.");
    }

    @Override
    public Uni<Response> releaseLocks(final LockElementsDto locks) {
        return ok(tplmService.releaseLocks(locks.getLockElements()), "Process finished.");
    }

    @Override
    public Uni<Response> listTransactionLocks(Long tid) {
        return ok(tplmService.listTransactionLocks(tid), "Listed transaction " + tid + " content.");
    }
}