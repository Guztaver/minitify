package com.gustavoanjos.minitify;

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
public class EndpointAccessTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testRegisterEndpoint() {
        Map<String, String> user = Map.of(
                "name", "Test User",
                "email", "test@example.com",
                "password", "password123"
        );

        ResponseEntity<Void> response = restTemplate.postForEntity("/auth/register", user, Void.class);
        System.out.println("Register response: " + response.getStatusCode());
        
        // This should return 201 CREATED
        assert response.getStatusCode() == HttpStatus.CREATED;
    }

    @Test
    public void testLoginEndpointWithoutUser() {
        Map<String, String> login = Map.of(
                "email", "nonexistent@example.com",
                "password", "password123"
        );

        ResponseEntity<String> response = restTemplate.postForEntity("/auth/login", login, String.class);
        System.out.println("Login response (no user): " + response.getStatusCode());
        
        // This should return 500 or 400 due to invalid credentials, not 401
        assert response.getStatusCode() != HttpStatus.UNAUTHORIZED;
    }
}