package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Service
public class TokenService {

    @Value("${security.jwt.shared-secret:}")
    private String sharedSecret;

    @Value("${security.jwt.issuer:minitify}")
    private String issuer;

    @Value("${security.jwt.expiration-ms:3600000}")
    private long expirationMs;

    public String generateToken(String username, Set<Roles> roles) {
        if (sharedSecret == null || sharedSecret.isBlank()) {
            throw new IllegalStateException("Nenhuma chave compartilhada configurada em security.jwt.shared-secret");
        }
        try {
            JWSSigner signer = new MACSigner(sharedSecret.getBytes(StandardCharsets.UTF_8));

            Date now = new Date();
            Date exp = new Date(now.getTime() + expirationMs);

            JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                    .subject(Objects.requireNonNull(username))
                    .issuer(issuer)
                    .issueTime(now)
                    .expirationTime(exp);

            if (roles != null && !roles.isEmpty()) {
                claims.claim("roles", roles);
            }

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims.build());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Erro ao gerar JWT", e);
        }
    }
}