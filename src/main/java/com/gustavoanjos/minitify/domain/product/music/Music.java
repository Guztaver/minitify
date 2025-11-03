package com.gustavoanjos.minitify.domain.product.music;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gustavoanjos.minitify.domain.product.album.Album;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document(collection = "musics")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Music {
    @Id
    private String id;

    @Setter
    @NotNull
    private String title;

    @Setter
    @NotNull
    private int duration;

    @Setter
    @DBRef
    @JsonBackReference
    @NotNull
    private Album album;

    public Music(String title, Album album, int duration) {
        this.title = title;
        this.album = album;
        this.duration = duration;
    }
}
