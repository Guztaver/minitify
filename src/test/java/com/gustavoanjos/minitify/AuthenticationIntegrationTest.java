package com.gustavoanjos.minitify;

import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "security.jwt.shared-secret=test-secret-key-that-is-at-least-256-bits-long-for-hs256-testing",
        "security.jwt.issuer=test",
        "security.jwt.expiration-ms=3600000",
        // use an in-memory H2 database for tests
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        // Disable Flyway migrations in tests (project migrations are MySQL-specific)
        "spring.flyway.enabled=false"
})
public class AuthenticationIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    public void registerLoginAndAccessProtectedEndpoint() throws Exception {
        String unique = UUID.randomUUID().toString().substring(0, 8);
        String email = "testuser+" + unique + "@example.com";
        String password = "password123";

        // 1) Register user
        Map<String, String> user = Map.of(
                "name", "Test User",
                "email", email,
                "password", password
        );

        ResponseEntity<Void> registerResponse = restTemplate.postForEntity(
                new URI(baseUrl("/auth/register")), user, Void.class
        );
        Assertions.assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode(), "Register should return 201 CREATED");

        // 2) Login
        Map<String, String> login = Map.of(
                "email", email,
                "password", password
        );

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                new URI(baseUrl("/auth/login")), login, String.class
        );
        Assertions.assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Login should return 200 OK");
        String token = loginResponse.getBody();
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isBlank());

        // 3) Inspect token: should contain roles claim with USER
        SignedJWT jwt = SignedJWT.parse(token);
        Object rolesClaim = jwt.getJWTClaimsSet().getClaim("roles");
        Assertions.assertNotNull(rolesClaim, "Token should contain 'roles' claim");
        // rolesClaim could be a List of strings
        if (rolesClaim instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> roles = (List<Object>) rolesClaim;
            Assertions.assertTrue(roles.stream().anyMatch(r -> String.valueOf(r).equalsIgnoreCase("USER")), "roles should contain USER");
        } else {
            Assertions.fail("Unexpected roles claim type: " + rolesClaim.getClass());
        }

        // 4) Access protected endpoint without token -> expect 401
        ResponseEntity<String> unauthedResponse = restTemplate.getForEntity(new URI(baseUrl("/artist/all")), String.class);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, unauthedResponse.getStatusCode(), 
            "Request without token should return 401 UNAUTHORIZED");

        // 5) Access protected endpoint with token -> expect 200
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> protectedRes = restTemplate.exchange(
                new URI(baseUrl("/artist/all")), HttpMethod.GET, entity, String.class
        );
        Assertions.assertEquals(HttpStatus.OK, protectedRes.getStatusCode(), "Protected endpoint should return 200 when called with valid token");
    }
}
