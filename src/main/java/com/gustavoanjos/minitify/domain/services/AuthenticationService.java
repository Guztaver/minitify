package com.gustavoanjos.minitify.domain.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService {
    
    @Value("${security.jwt.jwk-set-uri:}")
    private String jwkSetUri;

    @Value("${security.jwt.shared-secret:}")
    private String sharedSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        return Optional.ofNullable(jwkSetUri)
                .filter(uri -> !uri.isBlank())
                .map(uri -> (JwtDecoder) NimbusJwtDecoder.withJwkSetUri(uri).build())
                .orElseGet(() -> Optional.ofNullable(sharedSecret)
                        .filter(secret -> !secret.isBlank())
                        .map(secret -> {
                            SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
                            return (JwtDecoder) NimbusJwtDecoder.withSecretKey(key).build();
                        })
                        .orElseGet(() -> {
                            log.debug("JWT decoder is null - jwkSetUri: {}, sharedSecret: {}",
                                    jwkSetUri,
                                    Optional.ofNullable(sharedSecret).map(s -> "present").orElse("null"));
                            return null;
                        }));
    }


    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtAuthConverter;
    }
}
