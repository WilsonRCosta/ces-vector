package service.tplm.ope.resource;

import io.smallrye.mutiny.Uni;
import service.tplm.apim.dto.ResponseDto;
import service.tplm.apim.dto.ResponseErrorDto;

import javax.ws.rs.core.Response;

public abstract class BaseResource {

    protected <T> Uni<Response> ok(Uni<T> response, String msg) {
        return response.map(dto -> Response.ok(new ResponseDto<>(dto, msg)).build());
    }

    protected Uni<Response> badRequest(String msg, String field, String value) {
        return Uni.createFrom().item(
                Response.status(400).entity(new ResponseErrorDto(msg, field, value)).build()
        );
    }

}