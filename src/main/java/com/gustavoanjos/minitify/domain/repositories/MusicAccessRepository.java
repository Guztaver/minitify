package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.musicAccess.MusicAccess;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicAccessRepository extends MongoRepository<MusicAccess, String> {
    Optional<MusicAccess> findByUserIdAndMusicId(String userId, String musicId);

    long countByMusic_Id(String musicId);

    @Aggregation(pipeline = {
        "{ '$group': { '_id': '$music.$id', 'count': { '$sum': 1 } } }",
        "{ '$sort': { 'count': -1 } }",
        "{ '$limit': ?0 }"
    })
    List<MusicAccessCount> findTrending(int limit);

    interface MusicAccessCount {
        String getId();
        Long getCount();
    }
}
