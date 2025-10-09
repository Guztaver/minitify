package com.gustavoanjos.minitify.domain.services;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.musicAccess.MusicAccess;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.repositories.MusicAccessRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * Record a unique access from a user to a music. If an entry already exists for the user/music pair, do nothing.
     */
    public void recordAccess(User user, Music music) {
        if (user == null || music == null) return;
        UUID userId = user.getId();
        UUID musicId = music.getId();
        repository.findByUser_IdAndMusic_Id(userId, musicId)
                .ifPresentOrElse(existing -> log.debug("Access already recorded for user {} music {}", userId, musicId),
                        () -> {
                            MusicAccess ma = new MusicAccess(user, music);
                            repository.save(ma);
                            log.info("Recorded music access: user={} music={}", userId, musicId);
                        });
    }

    public long countForMusic(UUID musicId) {
        return repository.countByMusic_Id(musicId);
    }

    /**
     * Return a list of pairs [musicId, count] ordered by count desc limited by `limit`.
     */
    public List<Map<String, Object>> trending(int limit) {
        var page = PageRequest.of(0, Math.max(1, limit));
        List<Object[]> raw = repository.findTrending(page);
        List<Map<String, Object>> out = new ArrayList<>();
        for (Object[] row : raw) {
            UUID musicId = (UUID) row[0];
            Long cnt = ((Number) row[1]).longValue();
            Optional<Music> m = musicService.getRepository().findById(musicId);
            Map<String, Object> map = new HashMap<>();
            map.put("musicId", musicId);
            map.put("count", cnt);
            m.ifPresent(music -> {
                map.put("title", music.getTitle());
                map.put("album", Optional.ofNullable(music.getAlbum()).map(a -> a.getId()).orElse(null));
            });
            out.add(map);
        }
        return out;
    }
}
