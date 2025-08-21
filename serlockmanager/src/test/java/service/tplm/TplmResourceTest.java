package service.tplm;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.internal.mapping.JaxbMapper;
import org.junit.jupiter.api.Test;
import service.tplm.apim.model.LockElement;
import service.tplm.apim.model.LockElementsDto;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TplmResourceTest {

    @Test
    public void testGetLocksEndpoint() {
        LockElementsDto elementsMock = new LockElementsDto();
        elementsMock.setLockElements(List.of(new LockElement(0, "Vector1", "READ")));


//        given()
//                .when()
//                .queryParam("tid", 1234)
//                .body(elementsMock)
//                .get("/tplm/locks")
//                .then()
//                .statusCode(200)
//                .body(is(true));
    }

}