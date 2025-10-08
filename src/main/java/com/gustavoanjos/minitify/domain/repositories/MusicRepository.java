package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.music.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<Music, UUID> {
}
