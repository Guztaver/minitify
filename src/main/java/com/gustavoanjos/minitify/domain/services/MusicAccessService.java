package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.musicAccess.MusicAccess;
import com.gustavoanjos.minitify.domain.product.musicAccess.TrendingMusicDTO;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.repositories.MusicAccessRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MusicAccessService {
    @Getter
    private final MusicAccessRepository repository;
    @Getter
    private final MusicService musicService;
    @Getter
    private final UserService userService;

    public MusicAccessService(MusicAccessRepository repository, MusicService musicService, UserService userService) {
        this.repository = repository;
        this.musicService = musicService;
        this.userService = userService;
    }

    /**
     * Record unique access from a user to a music. If an entry already exists for the user/music pair, do nothing.
     */
    public void recordAccess(User user, Music music) {
        Optional.ofNullable(user)
                .flatMap(u -> Optional.ofNullable(music)
                        .map(m -> new Object[]{u, m}))
                .ifPresent(pair -> {
                    User u = (User) pair[0];
                    Music m = (Music) pair[1];
                    UUID userId = u.getId();
                    UUID musicId = m.getId();
                    repository.findByUser_IdAndMusic_Id(userId, musicId)
                            .ifPresentOrElse(
                                    existing -> log.debug("Access already recorded for user {} music {}", userId, musicId),
                                    () -> {
                                        MusicAccess ma = new MusicAccess(u, m);
                                        repository.save(ma);
                                        log.info("Recorded music access: user={} music={}", userId, musicId);
                                    });
                });
    }

    public long countForMusic(UUID musicId) {
        return repository.countByMusic_Id(musicId);
    }

    /**
     * Return a list of trending music with metadata, ordered by access count desc.
     */
    public List<TrendingMusicDTO> trending(int limit) {
        var page = PageRequest.of(0, Math.max(1, limit));
        return repository.findTrending(page).stream()
                .map(row -> {
                    UUID musicId = (UUID) row[0];
                    Long count = ((Number) row[1]).longValue();
                    return musicService.getRepository().findById(musicId)
                            .map(music -> new TrendingMusicDTO(
                                    musicId,
                                    count,
                                    music.getTitle(),
                                    Optional.ofNullable(music.getAlbum()).map(Album::getId).orElse(null)
                            ))
                            .orElseGet(() -> new TrendingMusicDTO(musicId, count, null, null));
                })
                .collect(Collectors.toList());
    }


}
