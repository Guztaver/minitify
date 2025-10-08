package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.album.AlbumDTO;
import com.gustavoanjos.minitify.domain.repositories.AlbumRepository;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AlbumService {
    @Getter
    private final AlbumRepository repository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.repository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public void createAlbum(AlbumDTO.WithArtistId data) {
        var artist = artistRepository.findById(data.artistId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found"));

        var album = new Album(
                data.title(),
                artist,
                data.genre(),
                data.releaseYear()
        );
        repository.save(album);
    }

    public void delete(UUID id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    public void update(UUID existingAlbumId, AlbumDTO.WithArtistId data) {
        repository.save(repository.findById(existingAlbumId).map(
                a -> {
                    a.setTitle(data.title());
                    a.setGenre(data.genre());
                    a.setReleaseYear(data.releaseYear());
                    return repository.save(a);
                }
        ).orElseThrow(() -> new EntityNotFoundException("Album not found")));
    }
}
