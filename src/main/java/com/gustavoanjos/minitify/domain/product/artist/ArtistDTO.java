package com.gustavoanjos.minitify.domain.product.artist;

import com.gustavoanjos.minitify.domain.product.DTO;

public record ArtistDTO(String name, String description, String genre) implements DTO<Artist> {
    @Override
    public Artist toEntity() {
        return new Artist(
                this.name,
                this.description,
                this.genre
        );
    }
}