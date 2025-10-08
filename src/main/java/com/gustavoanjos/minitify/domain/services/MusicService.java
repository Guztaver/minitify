package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.music.MusicDTO;
import com.gustavoanjos.minitify.domain.repositories.MusicRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MusicService {
    private final AlbumService albumService;
    @Getter
    private final MusicRepository repository;

    public MusicService(AlbumService albumService, MusicRepository repository) {
        this.repository = repository;
        this.albumService = albumService;
    }

    public void createMusic(MusicDTO.WithAlbumId data) {
        var albumRepository = albumService.getRepository();
        Album album = albumRepository.getReferenceById(data.albumId());
        var music = new Music(
                data.title(),
                album,
                data.duration()
        );
        music.setAlbum(album);
        repository.save(music);
    }

    public void updateMusic(Music existingMusic, MusicDTO.WithAlbumId data) {
        var albumRepository = albumService.getRepository();
        Album album = albumRepository.getReferenceById(data.albumId());

        existingMusic.setTitle(data.title());
        existingMusic.setAlbum(album);
        existingMusic.setDuration(data.duration());

        repository.save(existingMusic);
    }

    public void deleteMusic(UUID id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
