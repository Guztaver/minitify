package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.artist.Artist;
import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ArtistService {
    @Getter
    private final ArtistRepository repository;

    public ArtistService(ArtistRepository repository) {
        this.repository = repository;
    }

    public void deleteArtist(UUID id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public Artist updateArtist(UUID id, ArtistDTO artistDTO) throws IllegalArgumentException {
        return repository.findById(id)
                .map(artist -> {
                    artist.setName(artistDTO.name());
                    artist.setDescription(artistDTO.description());
                    artist.setGenre(artistDTO.genre());
                    artist.setAlbums(new HashSet<>());
                    return repository.save(artist);
                }).orElseThrow(
                        () -> new IllegalArgumentException("Artist with ID " + id + " not found")
                );
    }

    public Artist createArtist(@Validated @NonNull ArtistDTO data) {
        var artist = repository.save(data.toEntity());
        log.info("Artist with ID {} has been created", artist.id);
        return artist;
    }
}
