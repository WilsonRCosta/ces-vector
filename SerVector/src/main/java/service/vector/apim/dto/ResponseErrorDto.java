package service.vector.apim.dto;

import java.util.Map;

public class ResponseErrorDto {
    private final String msg;
    private final Map<String, String> errorMap;

    public ResponseErrorDto(String msg, String field, String value) {
        this.msg = msg;
        this.errorMap = Map.of(field, value);
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }
}
