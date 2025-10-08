package com.gustavoanjos.minitify.domain.product.album;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue
    private UUID id;

    @Setter
    @NotNull
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JsonBackReference
    private Artist artist;
    @Setter
    @NotNull
    private String genre;
    @Setter
    @NotNull
    private Date releaseYear;

    public Album(String title, Artist artist, String genre, Date releaseYear) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }
}
