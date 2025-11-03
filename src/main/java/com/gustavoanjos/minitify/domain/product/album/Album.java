package com.gustavoanjos.minitify.domain.product.album;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

@Document(collection = "albums")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Album {
    @Id
    private String id;

    @Setter
    @NotNull
    private String title;

    @DBRef
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
