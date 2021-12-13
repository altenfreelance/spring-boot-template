package com.alten.template;

import lombok.Getter;
import lombok.Setter;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import java.util.UUID;

public class MockJWSBuilder {

    @Getter
    @Setter
    private RsaJsonWebKey rsaJsonWebKey;

    @Getter
    @Setter
    private String claimsIssuer;

    @Getter
    @Setter
    private String claimsSubject;

    public JsonWebSignature build(){
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(getClaims().toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(rsaJsonWebKey.getAlgorithm());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setHeader("typ", "JWT");

        return jws;
    }

    private JwtClaims getClaims() {
        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setJwtId(UUID.randomUUID().toString());
        jwtClaims.setIssuer(claimsIssuer);
        jwtClaims.setAudience("https://onnet.ohionational.com");

        // 10 minutes
        float minutes = 10F;
        jwtClaims.setExpirationTimeMinutesInTheFuture(minutes);
        jwtClaims.setIssuedAtToNow();
        jwtClaims.setClaim("scope", "openid profile offline_access");

        String userID = "fakeUserID";
        jwtClaims.setClaim("azp", userID);
        jwtClaims.setClaim("sub", userID);

        return jwtClaims;
    }

}


