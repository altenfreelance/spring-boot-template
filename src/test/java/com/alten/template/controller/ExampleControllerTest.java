package com.alten.template.controller;

import com.alten.template.MockJWSBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.alten.template.model.ExamplePOJO;
import com.alten.template.model.GenericErrorResponse;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test-suite")
public class ExampleControllerTest {

    private static RsaJsonWebKey rsaJsonWebKey;

    static {
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            e.printStackTrace();
        }
    }

    private static final MockJWSBuilder mockJwsBuilder = new MockJWSBuilder();

    @Value("${wiremock.server.baseUrl}")
    private static String wireMockServerBaseUrl;

    @BeforeAll
    public static void setup() {
        rsaJsonWebKey.setKeyId("k1");
        rsaJsonWebKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        rsaJsonWebKey.setUse("sig");
        mockJwsBuilder.setRsaJsonWebKey(rsaJsonWebKey);
    }

    @BeforeEach
    public void init() {
        mockJwsBuilder.setClaimsIssuer(wireMockServerBaseUrl);
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/.well-known/jwks.json"))
                .willReturn(
                        WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(new JsonWebKeySet(rsaJsonWebKey).toJson())
                )
        );
    }


    @Test
    void privateEndpointShouldGive401WithNoToken(@Autowired TestRestTemplate restTemplate) {
        ResponseEntity<ExamplePOJO> response = restTemplate.getForEntity("/test", ExamplePOJO.class);
        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
    }

    @Test
    void privateEndpointShouldGive200WithToken(@Autowired TestRestTemplate restTemplate) throws JoseException, JsonProcessingException {

        String token = mockJwsBuilder.build().getCompactSerialization();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/test", HttpMethod.GET, requestEntity, String.class);

        ExamplePOJO examplePOJO = new ObjectMapper().readValue(response.getBody(), ExamplePOJO.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(examplePOJO.getUserID());
    }

    @Test
    void controllerAdviceIsWorking(@Autowired TestRestTemplate restTemplate) throws JoseException, JsonProcessingException {

        String token = mockJwsBuilder.build().getCompactSerialization();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/test/exception", HttpMethod.GET, requestEntity, String.class);

        GenericErrorResponse genericErrorResponse = new ObjectMapper().readValue(response.getBody(), GenericErrorResponse.class);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals("This is an example exception dummy", genericErrorResponse.getError());
    }

    @Test
    void publicEndpointShouldGive200WithNoToken(@Autowired TestRestTemplate restTemplate) {
        ResponseEntity<ExamplePOJO> response = restTemplate.getForEntity("/public/test", ExamplePOJO.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(isSameDay(new Date(), Objects.requireNonNull(response.getBody()).getTestDate()));
    }

    private boolean isSameDay(Date date1, Date date2) {
        LocalDate localDate1 = date1.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate localDate2 = date2.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return localDate1.isEqual(localDate2);
    }
}