package com.gustavoanjos.minitify.domain.product.user;

import com.gustavoanjos.minitify.domain.product.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // BCrypt hash prefixes for different versions
    private static final String BCRYPT_PREFIX_2A = "$2a$";
    private static final String BCRYPT_PREFIX_2B = "$2b$";
    private static final String BCRYPT_PREFIX_2Y = "$2y$";

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @NotNull
    private String name;
    @Setter
    @NotNull
    private String email;

    @Column(nullable = false)
    @NotNull
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Enumerated(EnumType.STRING)
    private final Set<Roles> roles = new HashSet<>();

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = Optional.ofNullable(password)
                .map(pwd -> isAlreadyBCryptHashed(pwd) ? pwd : PASSWORD_ENCODER.encode(pwd))
                .orElse(null);
    }

    /**
     * Checks if the given password string is already a BCrypt hash.
     * BCrypt hashes start with $2a$, $2b$, or $2y$ followed by the cost factor.
     *
     * @param password the password string to check
     * @return true if the password is already BCrypt hashed, false otherwise
     */
    private boolean isAlreadyBCryptHashed(String password) {
        return password.startsWith(BCRYPT_PREFIX_2A) ||
                password.startsWith(BCRYPT_PREFIX_2B) ||
                password.startsWith(BCRYPT_PREFIX_2Y);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}