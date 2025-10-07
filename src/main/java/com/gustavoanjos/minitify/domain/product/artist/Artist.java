package com.gustavoanjos.minitify.domain.product.artist;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gustavoanjos.minitify.domain.product.album.Album;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "artists")
public class Artist {
    @Id
    @GeneratedValue
    public UUID id;

    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private String genre;
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    @JsonManagedReference
    private Set<Album> albums = new HashSet<>();

    public Artist(String name, String description, String genre) {
        this.name = name;
        this.description = description;
        this.genre = genre;
    }
}
