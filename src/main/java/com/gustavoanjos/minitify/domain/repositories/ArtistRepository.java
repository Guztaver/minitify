package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.artist.Artists;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface ArtistRepository extends JpaRepository<Artists, UUID> {
    Collection<Artists> findByName(String name);
    Collection<Artists> findByGenre(String genre);
    Collection<Artists> findByDescription(String description);
}
