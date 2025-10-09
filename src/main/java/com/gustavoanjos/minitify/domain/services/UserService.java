package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.repositories.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Getter
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
