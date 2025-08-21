package apim.dto;

public class ResponseDto {
    private String msg;
    private String value;

    public ResponseDto() {}

    public ResponseDto(String value, String msg) {
        this.msg = msg;
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public String getValue() {
        return value;
    }
}
