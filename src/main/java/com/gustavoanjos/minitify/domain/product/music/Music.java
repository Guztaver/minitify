package com.gustavoanjos.minitify.domain.product.music;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gustavoanjos.minitify.domain.product.album.Album;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "musics")
@Getter
public class Music {
    @Id
    @GeneratedValue
    private UUID id;
    @Setter
    @NotNull
    private String title;
    @Setter
    @NotNull
    private int duration;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @NotNull
    private Album album;

    public Music(String title, Album album, int duration) {
        this.title = title;
        this.album = album;
        this.duration = duration;
    }
}
