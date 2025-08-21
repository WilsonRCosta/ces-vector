package apim.client;

import apim.dto.ResponseDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class VectorRest extends RestRequest {
    private static final String VECTOR_HOST = ISOS_HOST + "/vector/op";
    private static final String DEV_VECTOR_TX_HOST = DEV_ISOS_HOST + DEV_VECTOR_PORT + "/vector/op";

    private final WebTarget target;
    private final Client client;

    public VectorRest() {
        String VECTOR_V2 = vectorHost + "/vector/op";
        client = ClientBuilder.newClient();
        target = client.target(inProd ? VECTOR_V2 : DEV_VECTOR_TX_HOST);
    }

    public void closeClient() {
        client.close();
    }

    public int read(final long tid, final int index) {
        ResponseDto resp = target.path("read").path(String.valueOf(tid))
                .queryParam("index", index)
                .request(MediaType.APPLICATION_JSON)
                .get(ResponseDto.class);
        return Integer.parseInt(resp.getValue());
    }

    public void write(final long tid, final int index, final int value) {
        ResponseDto resp = target.path("write").path(String.valueOf(tid))
                .queryParam("index", index)
                .queryParam("value", value)
                .request(MediaType.APPLICATION_JSON)
                .post(null, ResponseDto.class);
        System.out.println(resp.getMsg());
    }
}
