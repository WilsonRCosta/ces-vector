package apim.client;

import apim.dto.ResponseDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class RestRequest {

    public static final boolean inProd = true;

    public static String ISOS_HOST = "http://isos.iesd.pt";
    public static String DEV_ISOS_HOST = "http://127.0.0.1";

    public static String DEV_TM_PORT = ":9001";
    public static String DEV_TPLM_PORT = ":9002";
    public static String DEV_LU_PORT = ":9003";
    public static String DEV_VECTOR_PORT = ":9100";

    private static final String LU_HOST = ISOS_HOST + "/lu";
    private static final String DEV_LU_HOST = DEV_ISOS_HOST + DEV_LU_PORT + "/lu";

    private final WebTarget target;
    private final Client lookupClient;

    public String tmHost;
    public String vectorHost;
    public String tplmHost;

    public RestRequest() {
        String LU_HOST_V2 = "http://lookup-service:9003";
        lookupClient = ClientBuilder.newClient();
        target = lookupClient.target(inProd ? LU_HOST_V2 : DEV_LU_HOST);

        tmHost = lookup("TM");
        vectorHost = lookup("VECTOR");
        tplmHost = lookup("TPLM");
    }

    public void close() {
        lookupClient.close();
    }

    ResponseDto postRequestEmptyBody(final WebTarget target, final String pathValue) {
        return target.path(pathValue)
                .request(MediaType.APPLICATION_JSON)
                .post(null, ResponseDto.class);
    }

    public String lookup(final String type) {
        return target
                .path("lookup")
                .queryParam("type", type)
                .request(MediaType.APPLICATION_JSON)
                .get(ResponseDto.class)
                .getValue();
    }
}
