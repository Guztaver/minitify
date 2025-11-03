package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.music.MusicDTO;
import com.gustavoanjos.minitify.domain.repositories.MusicRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

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
        Album album = albumRepository.findById(data.albumId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));
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
        Album album = albumRepository.findById(data.albumId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        existingMusic.setTitle(data.title());
        existingMusic.setAlbum(album);
        existingMusic.setDuration(data.duration());

        repository.save(existingMusic);
    }

    public void deleteMusic(String id) {
        repository.findById(id).ifPresent(repository::delete);
    }
}
