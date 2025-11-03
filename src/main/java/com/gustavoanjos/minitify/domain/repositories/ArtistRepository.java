package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ArtistRepository extends MongoRepository<Artist, String> {
    Collection<Artist> findByGenre(String genre);
}
