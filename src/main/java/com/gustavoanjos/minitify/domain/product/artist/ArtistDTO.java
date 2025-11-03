package com.gustavoanjos.minitify.domain.product.artist;

import com.gustavoanjos.minitify.domain.product.DTO;

public record ArtistDTO(String id, String name, String description, String genre) implements DTO<Artist> {
    @Override
    public Artist toEntity() {
        Artist artist = new Artist(
                this.name,
                this.description,
                this.genre
        );
        if (this.id != null) {
            artist.id = this.id;
        }
        return artist;
    }

    public static ArtistDTO fromArtist(Artist artist) {
        return new ArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getGenre()
        );
    }
}
