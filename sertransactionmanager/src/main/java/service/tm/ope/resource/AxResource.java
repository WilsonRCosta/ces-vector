package service.tm.ope.resource;

import io.smallrye.mutiny.Uni;
import service.tm.apim.resource.IAxResource;
import service.tm.apim.service.IAxService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/tm/ax")
public class AxResource extends BaseResource implements IAxResource {

    @Inject
    IAxService axService;

    @Override
    public Uni<Response> axRegister(Long tid, String endpoint) {
        return ok(axService.axRegister(tid, endpoint), "Transaction registered");
    }

    @Override
    public Uni<Response> axUnregister(Long tid, String endpoint) {
        return ok(axService.axUnregister(tid, endpoint), "Transaction unregistered");
    }
}
