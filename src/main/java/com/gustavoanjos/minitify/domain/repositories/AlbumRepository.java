package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
    Set<Album> findByArtist(Artist artist);
}
