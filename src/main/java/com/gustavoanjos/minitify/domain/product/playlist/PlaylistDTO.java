package com.gustavoanjos.minitify.domain.product.playlist;

import com.gustavoanjos.minitify.domain.product.DTO;
import com.gustavoanjos.minitify.domain.product.music.Music;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record PlaylistDTO(
        @NotBlank String name,
        String description,
        boolean isPublic
) implements DTO<Playlist> {

    @SuppressWarnings("unused")
    public static PlaylistDTO fromPlaylist(Playlist playlist) {
        return new PlaylistDTO(
                playlist.getName(),
                playlist.getDescription(),
                playlist.isPublic()
        );
    }

    @Override
    public Playlist toEntity() {
        return new Playlist(this.name, this.description, null, this.isPublic);
    }

    public record Response(
            String id,
            String name,
            String description,
            String ownerId,
            String ownerName,
            boolean isPublic,
            int musicCount,
            int totalDuration,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Response fromPlaylist(Playlist playlist) {
            return new Response(
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getDescription(),
                    playlist.getOwner().getId(),
                    playlist.getOwner().getName(),
                    playlist.isPublic(),
                    playlist.getMusicCount(),
                    playlist.getTotalDuration(),
                    playlist.getCreatedAt(),
                    playlist.getUpdatedAt()
            );
        }
    }

    public record WithMusics(
            String id,
            String name,
            String description,
            String ownerId,
            String ownerName,
            boolean isPublic,
            List<MusicInfo> musics,
            int totalDuration,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static WithMusics fromPlaylist(Playlist playlist) {
            List<MusicInfo> musicInfos = playlist.getMusics().stream()
                    .map(MusicInfo::fromMusic)
                    .collect(Collectors.toList());

            return new WithMusics(
                    playlist.getId(),
                    playlist.getName(),
                    playlist.getDescription(),
                    playlist.getOwner().getId(),
                    playlist.getOwner().getName(),
                    playlist.isPublic(),
                    musicInfos,
                    playlist.getTotalDuration(),
                    playlist.getCreatedAt(),
                    playlist.getUpdatedAt()
            );
        }
    }

    public record MusicInfo(
            String id,
            String title,
            int duration,
            String albumId,
            String albumTitle
    ) {
        public static MusicInfo fromMusic(Music music) {
            return new MusicInfo(
                    music.getId(),
                    music.getTitle(),
                    music.getDuration(),
                    music.getAlbum().getId(),
                    music.getAlbum().getTitle()
            );
        }
    }

    public record CreateRequest(
            @NotBlank String name,
            String description,
            boolean isPublic
    ) {
    }

    public record UpdateRequest(
            String name,
            String description,
            Boolean isPublic
    ) {
    }

    public record AddMusicRequest(
            @NotNull String musicId
    ) {
    }
}
