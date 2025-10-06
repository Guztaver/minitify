package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public User findByEmail(String email);
}
