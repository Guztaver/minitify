package com.gustavoanjos.minitify.domain.product.artist;

import jakarta.persistence.*;
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
public class Artists {
    @Id
    @GeneratedValue
    public UUID id;

    @Setter
    public String name;
    @Setter
    public String description;
    @Setter
    public String genre;

    public Artists(String name, String description, String genre) {
        this.name = name;
        this.description = description;
        this.genre = genre;
    }
}
