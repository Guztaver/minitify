package com.gustavoanjos.minitify.domain.product.album;

import com.gustavoanjos.minitify.domain.product.DTO;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

public record AlbumDTO(String title, @Validated Artist artist, String genre, @Validated Date releaseYear) implements DTO<Album> {

    @Override
    public Album toEntity() {

        return new Album(
                this.title,
                this.artist,
                this.genre,
                this.releaseYear
        );
    }
}
