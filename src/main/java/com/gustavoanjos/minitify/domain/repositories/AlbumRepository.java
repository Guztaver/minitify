package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {
    Set<Album> findByArtist(Artist artist);
}
