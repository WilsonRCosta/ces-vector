package apim.client;

import apim.dto.ResponseDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class TmTxRest extends RestRequest {
    private static final String TM_TX_HOST = ISOS_HOST + "/tm/tx";
    private static final String DEV_TM_TX_HOST = DEV_ISOS_HOST + DEV_TM_PORT + "/tm/tx";

    private final WebTarget target;
    private final Client client;

    public TmTxRest() {
        String TM_TX_V2 = tmHost + "/tm/tx";
        client = ClientBuilder.newClient();
        target = client.target(inProd ? TM_TX_V2 : DEV_TM_TX_HOST);
    }

    public void closeClient() {
        client.close();
    }

    public long txBegin() {
        ResponseDto resp = target.path("begin")
                .request(MediaType.APPLICATION_JSON)
                .get(ResponseDto.class);
        return Long.parseLong(resp.getValue());
    }

    public void txCommit(final long tid) {
        ResponseDto resp = postRequestEmptyBody(target, "commit/" + tid);
        System.out.println(resp.getMsg());
    }

    public void txClose(final long tid) {
        ResponseDto resp = postRequestEmptyBody(target, "close/" + tid);
        System.out.println(resp.getMsg());
    }

    public void txRollback(final long tid) {
        ResponseDto resp = postRequestEmptyBody(target, "rollback/" + tid);
        System.out.println(resp.getMsg());
    }

}
