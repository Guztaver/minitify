package com.gustavoanjos.minitify.domain.product.musicAccess;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "music_access", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "music_id"}))
public class MusicAccess {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @Setter
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    @NotNull
    @Setter
    private Music music;

    @Column(name = "accessed_at", nullable = false)
    private LocalDateTime accessedAt = LocalDateTime.now();

    public MusicAccess(User user, Music music) {
        this.user = user;
        this.music = music;
        this.accessedAt = LocalDateTime.now();
    }

}
