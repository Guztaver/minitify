package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.music.Music;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicRepository extends MongoRepository<Music, String> {
}
