package service.tm.apim.dto;

public class ResponseDto<T> {
    private final String msg;
    private final T value;

    public ResponseDto(T value, String msg) {
        this.msg = msg;
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public T getValue() {
        return value;
    }
}
