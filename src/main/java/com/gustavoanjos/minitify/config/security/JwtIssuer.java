package com.gustavoanjos.minitify.config.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

public class JwtIssuer {

    private static final long EXP_MS = 3600_000; // 1 hora

    // Gera token HS256 a partir de um segredo compartilhado
    public static String generateHmacToken(String secret, String subject, String issuer) throws JOSEException {
        JWSSigner signer = new MACSigner(secret.getBytes(StandardCharsets.UTF_8));

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject(subject)
            .issuer(issuer)
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + EXP_MS))
            .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    // Gera token RS256 a partir da chave privada RSA
    public static String generateRsaToken(RSAPrivateKey privateKey, String subject, String issuer) throws JOSEException {
        JWSSigner signer = new RSASSASigner(privateKey);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
            .subject(subject)
            .issuer(issuer)
            .issueTime(new Date())
            .expirationTime(new Date(System.currentTimeMillis() + EXP_MS))
            .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}