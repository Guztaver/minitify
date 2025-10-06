package com.gustavoanjos.minitify.config.security;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import com.gustavoanjos.minitify.domain.services.AuthenticationService;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AuthenticationService service;

    public SecurityConfig(AuthenticationService service) {
        this.service = service;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/auth/**")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/artists").hasRole(String.valueOf(Roles.ADMIN))
                .anyRequest().authenticated()
            );

        var jwtDecoder = service.jwtDecoder();
        if (jwtDecoder != null) {
            http.oauth2ResourceServer(oauth -> oauth
                    .jwt(jwt -> jwt
                            .jwtAuthenticationConverter(service.jwtAuthenticationConverter())
                            .decoder(jwtDecoder)
                    )
            );
        }

        return http.build();
    }
}