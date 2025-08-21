package service.vector.ope.resource;

import io.smallrye.mutiny.Uni;
import service.vector.apim.resource.IVectorResource;
import service.vector.ope.service.VectorService;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/vector/op")
public class VectorResource extends BaseResource implements IVectorResource {

    @Inject
    VectorService vectorService;

    @Override
    public Uni<Response> read(long tid, int index) {
        try {
            return ok(vectorService.read(tid, index), "Value from array retrieved.");
        } catch (ArrayIndexOutOfBoundsException e) {
            return badRequest("Array out of bounds", "index", String.valueOf(index));
        }
    }

    @Override
    public Uni<Response> write(long tid, int index, int value) {
        try {
            return ok(vectorService.write(tid, index, value), "Transaction " + tid + ": " + value + " written in position " + index);
        } catch (IndexOutOfBoundsException e) {
            return badRequest("Array out of bounds", "index", String.valueOf(index));
        }
    }
}
