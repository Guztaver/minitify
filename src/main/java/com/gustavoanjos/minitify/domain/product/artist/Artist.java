package com.gustavoanjos.minitify.domain.product.artist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @Setter
    public String name;
    @Setter
    public String description;
    @Setter
    public String genre;

    public Artist(String name, String description, String genre) {}
}
