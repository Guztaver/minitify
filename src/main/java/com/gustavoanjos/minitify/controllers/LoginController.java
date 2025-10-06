package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import com.gustavoanjos.minitify.domain.product.user.LoginDTO;
import com.gustavoanjos.minitify.domain.product.user.Users;
import com.gustavoanjos.minitify.domain.product.user.UserDTO;
import com.gustavoanjos.minitify.domain.repositories.UserRepository;
import com.gustavoanjos.minitify.domain.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for user login and registration")
public class LoginController {
    public UserRepository repository;
    public final TokenService service;
    public final PasswordEncoder passwordEncoder;

    public LoginController(UserRepository repository, TokenService service, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated LoginDTO data) {
        log.debug("Attempting login for email: {}", data.email());
        
        // Validate input
        if (data.email() == null || data.email().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (data.password() == null || data.password().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        
        var user = repository.findByEmail(data.email());
        if (user == null) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
        if (!passwordEncoder.matches(data.password(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

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
        
        // Validate input
        if (data.name() == null || data.name().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name is required");
        }
        if (data.email() == null || data.email().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }
        if (data.password() == null || data.password().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        
        // Check if user already exists
        if (repository.findByEmail(data.email()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this email already exists");
        }
        
        var user = new Users(data.name(), data.email(), passwordEncoder.encode(data.password()));
        user.roles.add(Roles.USER);
        repository.save(user);
        log.debug("User {} registered successfully", data.email());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
