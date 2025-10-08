package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    Collection<Artist> findByGenre(String genre);
}
