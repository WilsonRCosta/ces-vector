package service.vector.apim.dto;

public class RegisterRequest {
    private String type;
    private String hostname;

    public RegisterRequest() {}

    public RegisterRequest(final String type, final String hostname) {
        this.type = type;
        this.hostname = hostname;
    }

    public String getType() {
        return type;
    }

    public String getHostname() {
        return hostname;
    }
}