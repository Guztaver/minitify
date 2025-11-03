package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.playlist.Playlist;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.repositories.PlaylistRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class PlaylistService {

    @Getter
    private final PlaylistRepository repository;
    private final MusicService musicService;
    @SuppressWarnings("FieldCanBeLocal")
    private final UserService userService;

    public PlaylistService(PlaylistRepository repository, MusicService musicService, UserService userService) {
        this.repository = repository;
        this.musicService = musicService;
        this.userService = userService;
    }

    @Transactional
    public Playlist createPlaylist(String name, String description, boolean isPublic, User owner) {
        Playlist playlist = new Playlist(name, description, owner, isPublic);
        Playlist saved = repository.save(playlist);
        log.info("Created playlist: {} for user: {}", saved.getId(), owner.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Playlist> getPlaylistsByUser(User user) {
        return repository.findByOwner(user);
    }

    @Transactional(readOnly = true)
    public List<Playlist> getPlaylistsByUserId(String userId) {
        return repository.findByOwnerId(userId);
    }

    @Transactional(readOnly = true)
    public List<Playlist> getPublicPlaylists() {
        return repository.findByIsPublicTrue();
    }

    @Transactional(readOnly = true)
    public List<Playlist> getAllAccessibleByUser(String userId) {
        return repository.findAllAccessibleByUser(userId);
    }

    @Transactional(readOnly = true)
    public Playlist getPlaylistById(String playlistId) {
        return repository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found with ID: " + playlistId));
    }

    @Transactional(readOnly = true)
    public Playlist getPlaylistByIdAndOwner(String playlistId, User owner) {
        return repository.findByIdAndOwner(playlistId, owner)
                .orElseThrow(() -> new RuntimeException("Playlist not found or you don't have access to it"));
    }

    @Transactional
    public Playlist updatePlaylist(String playlistId, User owner, String name, String description, Boolean isPublic) {
        Playlist playlist = getPlaylistByIdAndOwner(playlistId, owner);

        if (name != null && !name.isBlank()) {
            playlist.setName(name);
        }
        if (description != null) {
            playlist.setDescription(description);
        }
        if (isPublic != null) {
            playlist.setPublic(isPublic);
        }

        Playlist updated = repository.save(playlist);
        log.info("Updated playlist: {}", playlistId);
        return updated;
    }

    @Transactional
    public void deletePlaylist(String playlistId, User owner) {
        Playlist playlist = getPlaylistByIdAndOwner(playlistId, owner);
        repository.delete(playlist);
        log.info("Deleted playlist: {} by user: {}", playlistId, owner.getId());
    }

    @Transactional
    public Playlist addMusicToPlaylist(String playlistId, String musicId, User owner) {
        Playlist playlist = getPlaylistByIdAndOwner(playlistId, owner);
        Music music = musicService.getRepository().findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found with ID: " + musicId));

        playlist.addMusic(music);
        Playlist updated = repository.save(playlist);
        log.info("Added music {} to playlist {}", musicId, playlistId);
        return updated;
    }

    @Transactional
    public Playlist removeMusicFromPlaylist(String playlistId, String musicId, User owner) {
        Playlist playlist = getPlaylistByIdAndOwner(playlistId, owner);
        Music music = musicService.getRepository().findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found with ID: " + musicId));

        playlist.removeMusic(music);
        Playlist updated = repository.save(playlist);
        log.info("Removed music {} from playlist {}", musicId, playlistId);
        return updated;
    }

    @Transactional(readOnly = true)
    public boolean canUserAccessPlaylist(String playlistId, User user) {
        return repository.findById(playlistId)
                .map(playlist -> playlist.isPublic() || playlist.getOwner().getId().equals(user.getId()))
                .orElse(false);
    }

    @SuppressWarnings("unused")
    @Transactional(readOnly = true)
    public long countPlaylistsByUser(String userId) {
        return repository.countByOwnerId(userId);
    }
}
