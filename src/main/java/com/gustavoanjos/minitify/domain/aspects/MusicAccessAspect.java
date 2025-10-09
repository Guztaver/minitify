package com.gustavoanjos.minitify.domain.aspects;

import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.user.User;
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
            // resolve music from return value first
            Music music = resolveMusicFromReturn(retVal);

            // if not found in return, try to find UUID in method args
            if (music == null) {
                music = resolveMusicFromArgs(jp.getArgs());
            }

            if (music == null) {
                log.debug("MusicAccessAspect: no music resolved from method {}", jp.getSignature());
                return;
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                log.debug("MusicAccessAspect: no authenticated user");
                return;
            }

            String username = auth.getName();
            Optional<User> userOpt = userService.getRepository().findByEmail(username);
            if (userOpt.isEmpty()) {
                log.debug("MusicAccessAspect: authenticated principal {} not found as User", username);
                return;
            }

            musicAccessService.recordAccess(userOpt.get(), music);

        } catch (Exception e) {
            log.error("Error in MusicAccessAspect while recording access", e);
        }
    }

    private Music resolveMusicFromReturn(Object retVal) {
        if (retVal == null) return null;
        if (retVal instanceof Music) return (Music) retVal;
        if (retVal instanceof ResponseEntity) {
            Object body = ((ResponseEntity<?>) retVal).getBody();
            if (body instanceof Music) return (Music) body;
        }
        if (retVal instanceof Optional<?> o) {
            if (o.isPresent() && o.get() instanceof Music) return (Music) o.get();
        }
        return null;
    }

    private Music resolveMusicFromArgs(Object[] args) {
        if (args == null) return null;
        // try to find UUID or Music in args
        for (Object a : args) {
            if (a == null) continue;
            if (a instanceof UUID id) {
                return musicAccessService.getMusicService().getRepository().findById(id).orElse(null);
            }
            if (a instanceof String) {
                // maybe UUID as string
                try {
                    UUID id = UUID.fromString((String) a);
                    return musicAccessService.getMusicService().getRepository().findById(id).orElse(null);
                } catch (IllegalArgumentException ignored) {
                }
            }
            if (a instanceof Music) return (Music) a;
        }
        return null;
    }
}
