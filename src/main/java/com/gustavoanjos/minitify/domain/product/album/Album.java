package com.gustavoanjos.minitify.domain.product.album;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
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
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Artist artist;
    @Setter
    private String genre;
    @Setter
    private Date releaseYear;

    public Album(String title, Artist artist, String genre, Date releaseYear) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    public String getArtistName() {
        return artist.getName();
    }
}
