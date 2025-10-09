package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.playlist.Playlist;
import com.gustavoanjos.minitify.domain.product.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

    List<Playlist> findByOwner(User owner);

    List<Playlist> findByOwnerId(UUID ownerId);

    List<Playlist> findByIsPublicTrue();

    Optional<Playlist> findByIdAndOwner(UUID id, User owner);

    @SuppressWarnings("unused")
    Optional<Playlist> findByIdAndOwnerId(UUID id, UUID ownerId);

    @Query("SELECT p FROM Playlist p WHERE p.isPublic = true OR p.owner.id = :userId")
    List<Playlist> findAllAccessibleByUser(@Param("userId") UUID userId);

    @SuppressWarnings("unused")
    @Query("SELECT p FROM Playlist p JOIN p.musics m WHERE m.id = :musicId AND (p.isPublic = true OR p.owner.id = :userId)")
    List<Playlist> findByMusicIdAndAccessibleByUser(@Param("musicId") UUID musicId, @Param("userId") UUID userId);

    long countByOwnerId(UUID ownerId);
}
