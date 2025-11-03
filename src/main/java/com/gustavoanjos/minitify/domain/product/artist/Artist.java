package com.gustavoanjos.minitify.domain.product.artist;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "artists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Artist {
    @Id
    public String id;

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
