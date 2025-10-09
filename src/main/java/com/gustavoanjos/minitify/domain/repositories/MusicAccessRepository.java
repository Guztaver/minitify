package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.musicAccess.MusicAccess;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MusicAccessRepository extends JpaRepository<MusicAccess, UUID> {
    Optional<MusicAccess> findByUser_IdAndMusic_Id(UUID userId, UUID musicId);

    long countByMusic_Id(UUID musicId);

    @Query("SELECT ma.music.id, COUNT(ma) as cnt FROM MusicAccess ma GROUP BY ma.music.id ORDER BY COUNT(ma) DESC")
    List<Object[]> findTrending(Pageable pageable);
}
