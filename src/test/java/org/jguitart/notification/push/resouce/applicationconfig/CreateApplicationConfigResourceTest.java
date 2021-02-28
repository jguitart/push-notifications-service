package org.jguitart.notification.push.resouce.applicationconfig;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import org.hamcrest.MatcherAssert;
import org.jguitart.notification.push.dto.ApplicationConfigAndroidDto;
import org.jguitart.notification.push.dto.ApplicationConfigDto;
import org.jguitart.notification.push.dto.ApplicationConfigIosDto;
import org.jguitart.notification.push.model.ApplicationConfig;
import org.jguitart.notification.push.model.Errors;
import org.jguitart.notification.push.util.TestUtils;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.assertj.core.api.Assertions.*;

@QuarkusTest
public class CreateApplicationConfigResourceTest {

    private static final String POST_URL = "/app-config";

    @Test
    public void testNameNotValid() {
        // given
        ApplicationConfigDto payload = ApplicationConfigDto.builder()
                .build();

        given()
                .body(payload)
                .contentType(MediaType.APPLICATION_JSON)
            .when()
                .post(POST_URL)
            .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_NAME_MANDATORY));
    }

    @Test
    public void testNoConfigsIsOk() {
        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_no_config_is_ok_app")
                .build();

        ApplicationConfigDto actual = given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ApplicationConfigDto.class);

        assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());

        ApplicationConfig instance = ApplicationConfig.findById(actual.getId());
        assertEquals(instance.getId(), actual.getId());
        assertEquals(instance.getName(), actual.getName());
    }

    @Test
    public void testFullConfigsOk() throws IOException {

        ApplicationConfigAndroidDto androidConfig = ApplicationConfigAndroidDto.builder()
                .firebaseSenderId("senderId")
                .packageName("test.full.config.is.ok.android.config")
                .firebaseKey("firebaseKey")
                .build();

        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .bundleId("test.full.config.is.ok.ios.config")
                .apnsCertificatePassword("changeit")
                .apnsCertificate(TestUtils.loadP12CertificateBase64())
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_full_config_is_ok")
                .iosConfig(iosConfig)
                .androidConfig(androidConfig)
                .build();

        ApplicationConfigDto actual = given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ApplicationConfigDto.class);

        assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getAndroidConfig().getFirebaseKey(), actual.getAndroidConfig().getFirebaseKey());
        assertEquals(expected.getAndroidConfig().getFirebaseSenderId(), actual.getAndroidConfig().getFirebaseSenderId());
        assertEquals(expected.getAndroidConfig().getPackageName(), actual.getAndroidConfig().getPackageName());

        assertEquals(expected.getIosConfig().getBundleId(), actual.getIosConfig().getBundleId());
        assertEquals(expected.getIosConfig().getApnsCertificate(), actual.getIosConfig().getApnsCertificate());
        assertEquals(expected.getIosConfig().getApnsCertificatePassword(), actual.getIosConfig().getApnsCertificatePassword());

        ApplicationConfig instance = ApplicationConfig.findById(actual.getId());
        assertEquals(instance.getId(), actual.getId());
        assertEquals(instance.getName(), actual.getName());
        assertEquals(instance.getAndroidConfig().getFirebaseKey(), actual.getAndroidConfig().getFirebaseKey());
        assertEquals(instance.getAndroidConfig().getFirebaseSenderId(), actual.getAndroidConfig().getFirebaseSenderId());
        assertEquals(instance.getAndroidConfig().getPackageName(), actual.getAndroidConfig().getPackageName());

        assertEquals(instance.getIosConfig().getBundleId(), actual.getIosConfig().getBundleId());
        assertEquals(Base64.getEncoder().encodeToString(instance.getIosConfig().getApnsCertificate()), actual.getIosConfig().getApnsCertificate());
        assertEquals(instance.getIosConfig().getApnsCertificatePassword(), actual.getIosConfig().getApnsCertificatePassword());

    }

    @Test
    public void testConfigIosNoBundleId() throws IOException {
        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .apnsCertificatePassword("password")
                .apnsCertificate(TestUtils.loadP12CertificateBase64())
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_ios_no_bundle_id")
                .iosConfig(iosConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_IOS_BUNDLE_ID_MANDATORY));
    }

    @Test
    public void testConfigIosNoP12() {
        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .bundleId("test_config_ios_no_p12_ios_bundle")
                .apnsCertificatePassword("password")
                .apnsCertificate(null)
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_ios_no_bundle_id")
                .iosConfig(iosConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_IOS_CERTIFICATE_MANDATORY));
    }

    @Test
    public void testConfigIosInvalidP12() {
        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .bundleId("test_config_ios_no_p12_ios_bundle")
                .apnsCertificatePassword("password")
                .apnsCertificate("invalidp12")
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_ios_no_bundle_id")
                .iosConfig(iosConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_IOS_CERTIFICATE_INVALID));
    }

    @Test
    public void testConfigIosNoPassword() throws IOException {
        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .bundleId("test_config_ios_no_password")
                .apnsCertificatePassword(null)
                .apnsCertificate(TestUtils.loadP12CertificateBase64())
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_ios_no_bundle_id")
                .iosConfig(iosConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_IOS_PASSWORD_INVALID));
    }

    @Test
    public void testConfigIosInvalidPassword() throws IOException {
        ApplicationConfigIosDto iosConfig = ApplicationConfigIosDto.builder()
                .bundleId("test_config_ios_invalid_password")
                .apnsCertificatePassword("password")
                .apnsCertificate(TestUtils.loadP12CertificateBase64())
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_ios_no_bundle_id")
                .iosConfig(iosConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_IOS_PASSWORD_INVALID));
    }

    @Test
    public void testConfigAndroidNoPackage() {
        ApplicationConfigAndroidDto androidConfig = ApplicationConfigAndroidDto.builder()
                .firebaseSenderId("senderId")
                .firebaseKey("senderKey")
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_android_no_package")
                .androidConfig(androidConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_ANDROID_PACKAGE_MANDATORY));
    }

    @Test
    public void testConfigAndroidNoFirebaseSenderId() {
        ApplicationConfigAndroidDto androidConfig = ApplicationConfigAndroidDto.builder()
                .packageName("test.config.android.no.package")
                .firebaseKey("senderKey")
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_android_no_firebase_sender_id")
                .androidConfig(androidConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_ANDROID_SENDER_MANDATORY));
    }

    @Test
    public void testConfigAndroidNoFirebaseKey() {
        ApplicationConfigAndroidDto androidConfig = ApplicationConfigAndroidDto.builder()
                .packageName("test.config.android.no.package")
                .firebaseSenderId("senderId")
                .build();

        ApplicationConfigDto expected = ApplicationConfigDto.builder()
                .name("test_config_android_no_firebase_key")
                .androidConfig(androidConfig)
                .build();

        given()
                .body(expected)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(POST_URL)
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body("error", is(Errors.ERROR_APP_CONFIG_ANDROID_KEY_MANDATORY));
    }

}
