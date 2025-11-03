package com.gustavoanjos.minitify.domain.product.musicAccess;

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
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDateTime;

@Document(collection = "music_access")
@CompoundIndex(name = "user_music_idx", def = "{'user.$id': 1, 'music.$id': 1}", unique = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MusicAccess {
    @Id
    private String id;

    @DBRef
    @NotNull
    @Setter
    private User user;

    @DBRef
    @NotNull
    @Setter
    private Music music;

    private LocalDateTime accessedAt = LocalDateTime.now();

    public MusicAccess(User user, Music music) {
        this.user = user;
        this.music = music;
        this.accessedAt = LocalDateTime.now();
    }
}
