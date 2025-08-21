package service.tm.ope.resource;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import service.tm.apim.resource.ITxResource;
import service.tm.apim.service.ITxService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/tm/tx")
public class TxResource extends BaseResource implements ITxResource {

    @Inject
    ITxService txService;

    @Override
    public Uni<Response> txBegin() {
        return ok(txService.txBegin(), "Transaction id retrieved");
    }

    @Override
    public Uni<Response> txClose(Long tid) {
        return ok(txService.txClose(tid), "Transaction closed");
    }

    @Override
    @Blocking
    public Uni<Response> txCommit(Long tid) {
        return ok(txService.txCommit(tid), "Transaction committed");
    }

    @Override
    public Uni<Response> txRollback(Long tid) {
        return ok(txService.txRollback(tid), "Transaction rolled back");
    }
}