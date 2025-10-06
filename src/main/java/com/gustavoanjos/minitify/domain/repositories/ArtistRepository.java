package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    public Collection<Artist> findByName(String name);
    public Collection<Artist> findByGenre(String genre);
    public Collection<Artist> findByDescription(String description);
}
