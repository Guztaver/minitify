package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.album.AlbumDTO;
import com.gustavoanjos.minitify.domain.repositories.AlbumRepository;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    @Getter
    private final AlbumRepository repository;
    private final ArtistRepository artistRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.repository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public void createAlbum(AlbumDTO data) {
        var artist = artistRepository.findById(data.artist().getId())
                .orElseThrow(() -> new EntityNotFoundException("Artist not found"));

        var album = new Album(
                data.title(),
                artist,
                data.genre(),
                data.releaseYear()
        );
        repository.save(album);
    }
}
