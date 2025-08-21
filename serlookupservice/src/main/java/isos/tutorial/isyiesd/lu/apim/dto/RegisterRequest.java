package isos.tutorial.isyiesd.lu.apim.dto;

public class RegisterRequest {
    private final String type;
    private final String hostname;

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