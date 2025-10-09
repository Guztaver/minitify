package com.gustavoanjos.minitify.domain.aspects;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.services.MusicAccessService;
import com.gustavoanjos.minitify.domain.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class MusicAccessAspect {
    private final MusicAccessService musicAccessService;
    private final UserService userService;

    public MusicAccessAspect(MusicAccessService musicAccessService, UserService userService) {
        this.musicAccessService = musicAccessService;
        this.userService = userService;
    }

    /**
     * Intercepts methods annotated with @TrackMusicAccess and records the access for the authenticated user.
     * Supports methods that return a Music, ResponseEntity<Music>, Optional<Music> or that receive a UUID music id as an arg.
     */
    @AfterReturning(pointcut = "@annotation(com.gustavoanjos.minitify.domain.aspects.TrackMusicAccess)", returning = "retVal")
    public void afterTrackedMethod(JoinPoint jp, Object retVal) {
        try {
            Optional<Music> musicOpt = resolveMusicFromReturn(retVal)
                    .or(() -> resolveMusicFromArgs(jp.getArgs()));

            musicOpt.ifPresentOrElse(
                    this::recordAccessForAuthenticatedUser,
                    () -> log.debug("MusicAccessAspect: no music resolved from method {}", jp.getSignature())
            );
        } catch (Exception e) {
            log.error("Error in MusicAccessAspect while recording access", e);
        }
    }

    private void recordAccessForAuthenticatedUser(Music music) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getName)
                .flatMap(username -> userService.getRepository().findByEmail(username))
                .ifPresentOrElse(
                        user -> musicAccessService.recordAccess(user, music),
                        () -> log.debug("MusicAccessAspect: authenticated principal not found as User")
                );
    }

    private Optional<Music> resolveMusicFromReturn(Object retVal) {
        return Optional.ofNullable(retVal)
                .flatMap(val -> switch (val) {
                    case Music music -> Optional.of(music);
                    case ResponseEntity<?> response -> Optional.ofNullable(response.getBody())
                            .filter(body -> body instanceof Music)
                            .map(body -> (Music) body);
                    case Optional<?> opt -> opt.filter(o -> o instanceof Music)
                            .map(o -> (Music) o);
                    default -> Optional.empty();
                });
    }

    private Optional<Music> resolveMusicFromArgs(Object[] args) {
        return Optional.ofNullable(args)
                .stream()
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .flatMap(arg -> {
                    if (arg instanceof Music music) {
                        return Optional.of(music).stream();
                    }
                    if (arg instanceof UUID id) {
                        return musicAccessService.getMusicService().getRepository().findById(id).stream();
                    }
                    if (arg instanceof String str) {
                        try {
                            UUID id = UUID.fromString(str);
                            return musicAccessService.getMusicService().getRepository().findById(id).stream();
                        } catch (IllegalArgumentException ignored) {
                            return Optional.<Music>empty().stream();
                        }
                    }
                    return Optional.<Music>empty().stream();
                })
                .findFirst();
    }
}
