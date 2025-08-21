package service.vector.ope.resource;

import io.smallrye.mutiny.Uni;
import service.vector.apim.resource.IXaResource;
import service.vector.ope.service.XaService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/vector/xa")
public class XaResource extends BaseResource implements IXaResource {

    @Inject
    XaService xaService;

    @Override
    public Uni<Response> xaPrepare(Long tid) {
        return ok(xaService.xaPrepare(tid), "Value for invariant check retrieved.");
    }

    @Override
    public Uni<Response> xaCommit(Long tid) {
        return ok(xaService.xaCommit(tid), "Transaction committed.");
    }

    @Override
    public Uni<Response> xaRollback(Long tid) {
        return ok(xaService.xaRollback(tid), "Transaction rolled back.");
    }

    @Override
    public Uni<Response> xaClose(Long tid) {
        return ok(xaService.xaClose(tid), "Transaction closed.");
    }
}
