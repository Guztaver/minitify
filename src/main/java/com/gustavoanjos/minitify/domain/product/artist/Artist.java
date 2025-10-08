package com.gustavoanjos.minitify.domain.product.artist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotNull
    private String name;
    @Setter
    @NotNull
    private String description;
    @Setter
    @NotNull
    private String genre;

    public Artist(String name, String description, String genre) {
        this.name = name;
        this.description = description;
        this.genre = genre;
    }
}
