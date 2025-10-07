package com.gustavoanjos.minitify.domain.product.user;

import com.gustavoanjos.minitify.domain.product.DTO;

public record UserDTO(String name, String email, String password) implements DTO<User> {
    @Override
    public User toEntity() {
        return new User(this.name, this.email, this.password);
    }
}
