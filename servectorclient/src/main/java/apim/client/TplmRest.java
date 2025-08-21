package apim.client;

import apim.dto.ResponseDto;
import apim.tplm.LockElementsDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class TplmRest extends RestRequest {
    private static final String TPLM_HOST = ISOS_HOST + "/tplm";
    private static final String DEV_TPLM_TX_HOST = DEV_ISOS_HOST + DEV_TPLM_PORT + "/tplm";

    private final WebTarget target;
    private final Client client;

    public TplmRest() {
        String TPLM_V2 = tplmHost + "/tplm";
        client = ClientBuilder.newClient();
        target = client.target(inProd ? TPLM_V2 : DEV_TPLM_TX_HOST);
    }

    public void closeClient() {
        client.close();
    }

    public boolean lockAction(final LockElementsDto lockElements, String action) {
        ResponseDto resp = target.path(action)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(lockElements))
                .readEntity(ResponseDto.class);
        return Boolean.parseBoolean(resp.getValue());
    }
}
