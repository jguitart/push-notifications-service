package org.jguitart.notification.push.resouce.applicationconfig;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import org.jguitart.notification.push.dto.ApplicationConfigAndroidDto;
import org.jguitart.notification.push.dto.ApplicationConfigDto;
import org.jguitart.notification.push.dto.ApplicationConfigIosDto;
import org.jguitart.notification.push.model.ApplicationConfig;
import org.jguitart.notification.push.model.Errors;
import org.jguitart.notification.push.service.ApplicationConfigService;
import org.jguitart.notification.push.util.TestUtils;
import org.junit.jupiter.api.Test;


import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class GetApplicationConfigResourceTest {

    private static final String GET_URL_FORMAT = "/app-config/%d";

    @Inject
    private ApplicationConfigService applicationConfigService;

    private ApplicationConfigDto createApplicationConfigTestInstance() throws IOException {
        String certirficate = TestUtils.loadP12CertificateBase64();
        ApplicationConfigDto instance = ApplicationConfigDto.builder()
                .androidConfig(ApplicationConfigAndroidDto.builder()
                        .firebaseKey("firebaseKey")
                        .firebaseSenderId("firebaseSenderId")
                        .packageName("package.name")
                        .build())
                .iosConfig(ApplicationConfigIosDto.builder()
                        .bundleId("bundle.id")
                        .apnsCertificate(certirficate)
                        .apnsCertificatePassword("changeit")
                        .build())
                .build();

        ApplicationConfigDto result = this.applicationConfigService.create(instance);
        return result;
    }

    @Test
    public void testGetApplicationResourceNotExists() {
        String url = String.format(GET_URL_FORMAT, Integer.MAX_VALUE);
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(url)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_NOT_EXISTS));
    }

    @Test
    public void testGetApplicationResourceOk() throws IOException {
        ApplicationConfigDto expected = this.createApplicationConfigTestInstance();
        String url = String.format(GET_URL_FORMAT, expected.getId());

        ApplicationConfigDto actual = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(url)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ApplicationConfigDto.class);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

    }
}
