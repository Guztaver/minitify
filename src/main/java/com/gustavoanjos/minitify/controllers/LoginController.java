package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import com.gustavoanjos.minitify.domain.product.user.LoginDTO;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.product.user.UserDTO;
import com.gustavoanjos.minitify.domain.repositories.UserRepository;
import com.gustavoanjos.minitify.domain.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import java.lang.IllegalArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for user login and registration")
public class LoginController {
    private final UserRepository repository;
    private final TokenService service;
    private final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository repository, TokenService service, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "404", description = "Email not found"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials or request data")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginDTO data) {
        log.debug("Attempting login for email: {}", data.email());
        
        if (data.email().isEmpty()) return ResponseEntity.badRequest().body("Email is required");
        if (data.password().isEmpty()) return ResponseEntity.badRequest().body("Password is required");

        var user = repository.findByEmail(data.email()).orElseThrow(
                () -> new IllegalIdentifierException("Email not found")
        );

        if (!passwordEncoder.matches(data.password(), user.getPassword())) return ResponseEntity.badRequest().body("Invalid email or password");

        var token = service.generateToken(user.getUsername(), user.getRoles());
        log.debug("Generated token: {}...", String.valueOf(token).substring(0, 10));
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "User registration", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated UserDTO data) {
        log.debug("Attempting register for email: {}", data.email());
        
        if (data.name().isEmpty()) return ResponseEntity.badRequest().body("Name is required");
        if (data.email().isEmpty()) return ResponseEntity.badRequest().body("Email is required");
        if (data.password().isEmpty()) return ResponseEntity.badRequest().body("Password is required");

        if (repository.findByEmail(data.email()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");

        var user = new User(data.name(), data.email(), passwordEncoder.encode(data.password()));
        user.getRoles().add(Roles.USER);
        repository.save(user);
        log.debug("User {} registered successfully", data.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
