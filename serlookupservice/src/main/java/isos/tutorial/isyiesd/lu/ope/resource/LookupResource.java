package isos.tutorial.isyiesd.lu.ope.resource;

import io.smallrye.mutiny.Uni;
import isos.tutorial.isyiesd.lu.apim.dto.RegisterRequest;
import isos.tutorial.isyiesd.lu.apim.resource.ILookupResource;
import isos.tutorial.isyiesd.lu.apim.service.ILookupService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/lu")
public class LookupResource extends BaseResource implements ILookupResource {

    @Inject
    ILookupService lookupService;

    @Override
    public Uni<Response> register(final RegisterRequest req) {
        return ok(lookupService.register(req.getType(), req.getHostname()), req.getHostname() + " registered.");
    }

    @Override
    public Uni<Response> lookup(final String type) {
        return ok(lookupService.lookup(type), "Returned a " + type + " resource.");
    }

    @Override
    public Uni<Response> unRegister(final RegisterRequest req) {
        return ok(lookupService.unRegister(req.getType(), req.getHostname()), req.getHostname() + " unregistered.");
    }
}