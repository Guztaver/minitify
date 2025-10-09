package com.gustavoanjos.minitify.domain.product.playlist;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "playlists")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @NotNull
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(length = 500)
    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User owner;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_musics",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "music_id")
    )
    private List<Music> musics = new ArrayList<>();

    @Setter
    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public Playlist(String name, String description, User owner, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.isPublic = isPublic;
        this.musics = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addMusic(Music music) {
        if (!this.musics.contains(music)) {
            this.musics.add(music);
            this.updatedAt = LocalDateTime.now();
        }
    }

    public void removeMusic(Music music) {
        this.musics.remove(music);
        this.updatedAt = LocalDateTime.now();
    }

    public int getMusicCount() {
        return this.musics.size();
    }

    public int getTotalDuration() {
        return this.musics.stream()
                .mapToInt(Music::getDuration)
                .sum();
    }
}