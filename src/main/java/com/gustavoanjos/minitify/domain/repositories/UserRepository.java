package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);
}
