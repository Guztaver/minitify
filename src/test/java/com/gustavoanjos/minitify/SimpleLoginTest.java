package com.gustavoanjos.minitify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "security.jwt.shared-secret=test-secret-key-that-is-at-least-256-bits-long-for-hs256-testing",
        "security.jwt.issuer=test",
        "security.jwt.expiration-ms=3600000",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=false"
})
public class SimpleLoginTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleLoginTest.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testLoginEndpoint() {
        // Test register first
        Map<String, String> user = Map.of(
                "name", "Test User",
                "email", "test@example.com",
                "password", "password123"
        );

        ResponseEntity<Void> registerResponse = restTemplate.postForEntity("/auth/register", user, Void.class);
        log.debug("Register response: {}", registerResponse.getStatusCode());

        // Test login
        Map<String, String> login = Map.of(
                "email", "test@example.com",
                "password", "password123"
        );

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("/auth/login", login, String.class);
        log.debug("Login response: {}", loginResponse.getStatusCode());
        log.debug("Login body: {}", loginResponse.getBody());
        log.debug("Login headers: {}", loginResponse.getHeaders());
        
        // This should pass
        if (loginResponse.getStatusCode() != HttpStatus.OK) {
            log.error("Login failed with status: {}", loginResponse.getStatusCode());
            log.error("Response body: {}", loginResponse.getBody());
        }
        assert loginResponse.getStatusCode() == HttpStatus.OK;
    }
}