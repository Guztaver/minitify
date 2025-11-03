package com.gustavoanjos.minitify.domain.product.playlist;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.user.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "playlists")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {

    @Id
    private String id;

    @Setter
    @NotNull
    private String name;

    @Setter
    private String description;

    @Setter
    @DBRef
    @NotNull
    private User owner;

    @Setter
    @DBRef
    private List<Music> musics = new ArrayList<>();

    @Setter
    private boolean isPublic = true;

    private LocalDateTime createdAt;

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