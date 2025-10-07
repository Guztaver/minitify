package com.gustavoanjos.minitify.dto;

import com.gustavoanjos.minitify.domain.product.album.Album;

import java.util.Date;
import java.util.UUID;

public record AlbumResponseDTO(UUID id, String title, String genre, Date releaseYear, UUID artistId) {
    public static AlbumResponseDTO fromAlbum(Album album) {
        return new AlbumResponseDTO(
                album.getId(),
                album.getTitle(),
                album.getGenre(),
                album.getReleaseYear(),
                album.getArtist().getId()
        );
    }
}
