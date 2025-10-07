package com.gustavoanjos.minitify.dto;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.artist.Artist;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ArtistResponseDTO(UUID id, String name, String description, String genre, Set<UUID> albumIds) {
    public static ArtistResponseDTO fromArtist(Artist artist) {
        return new ArtistResponseDTO(
                artist.getId(),
                artist.getName(),
                artist.getDescription(),
                artist.getGenre(),
                artist.getAlbums().stream().map(Album::getId).collect(Collectors.toSet())
        );
    }
}

