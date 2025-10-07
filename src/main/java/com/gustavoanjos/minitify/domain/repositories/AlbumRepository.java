package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.album.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
}
