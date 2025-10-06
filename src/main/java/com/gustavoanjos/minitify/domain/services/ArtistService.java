package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.artist.Artists;
import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ArtistService {
    @Getter
    public final ArtistRepository repository;

    public ArtistService(ArtistRepository repository) {
        this.repository = repository;
    }

    public void deleteArtist(UUID id) {
        try {
            repository.findById(id).ifPresent(repository::delete);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to delete artist with ID: " + id, e);
        }
    }

    public void updateArtist(UUID id, ArtistDTO artistDTO) throws IllegalArgumentException {
        if (id == null || artistDTO == null) {
            throw new IllegalArgumentException("ID and ArtistDTO must not be null");
        }
        try {
            repository.findById(id)
                    .map(artist -> {
                        artist.setName(artistDTO.name());
                        artist.setDescription(artistDTO.description());
                        artist.setGenre(artistDTO.genre());
                        repository.save(artist);
                        return new ArtistDTO(artist.getName(), artist.getDescription(), artist.getGenre());
                    });
        } catch (Exception exception) {
            throw new RuntimeException("Failed to update artist with ID: " + id, exception);
        }
    }

    public void createArtist(ArtistDTO artistDTO) {
        if (artistDTO == null) throw new IllegalArgumentException("ArtistDTO must not be null");

        try {
            var artist = new Artists(artistDTO.name(), artistDTO.description(), artistDTO.genre());
            repository.save(artist);
            log.info("Created artist with ID: {}", artist.getId());
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create artist", exception);
        }
    }
}
