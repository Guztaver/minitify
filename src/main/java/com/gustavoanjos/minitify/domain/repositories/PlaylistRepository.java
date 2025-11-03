package com.gustavoanjos.minitify.domain.repositories;

import com.gustavoanjos.minitify.domain.product.playlist.Playlist;
import com.gustavoanjos.minitify.domain.product.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends MongoRepository<Playlist, String> {

    List<Playlist> findByOwner(User owner);

    List<Playlist> findByOwnerId(String ownerId);

    List<Playlist> findByIsPublicTrue();

    Optional<Playlist> findByIdAndOwner(String id, User owner);

    @SuppressWarnings("unused")
    Optional<Playlist> findByIdAndOwnerId(String id, String ownerId);

    @Query("{ '$or': [ { 'isPublic': true }, { 'owner.$id': ?0 } ] }")
    List<Playlist> findAllAccessibleByUser(String userId);

    @SuppressWarnings("unused")
    @Query("{ 'musics.$id': ?0, '$or': [ { 'isPublic': true }, { 'owner.$id': ?1 } ] }")
    List<Playlist> findByMusicIdAndAccessibleByUser(String musicId, String userId);

    long countByOwnerId(String ownerId);
}
