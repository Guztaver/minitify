package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import com.gustavoanjos.minitify.domain.product.user.LoginDTO;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.product.user.UserDTO;
import com.gustavoanjos.minitify.domain.repositories.UserRepository;
import com.gustavoanjos.minitify.domain.services.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {
    public UserRepository repository;
    public final TokenService service;

    public LoginController(UserRepository repository, TokenService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Validated LoginDTO data) {
        var user = repository.findByEmail(data.email());
        if (user == null || !user.isPasswordValid(data.password())) throw new RuntimeException("Invalid credentials");

        return ResponseEntity.ok(service.generateToken(user.getUsername(), user.getRoles()));
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Validated UserDTO data) {
        var user = new User(data.name(), data.email(), data.password());
        user.roles.add(Roles.USER);
        repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
