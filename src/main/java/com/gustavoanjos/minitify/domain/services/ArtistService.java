package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ArtistService {
    @Getter
    public final ArtistRepository repository;

    public ArtistService(ArtistRepository repository) {
        this.repository = repository;
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
}
