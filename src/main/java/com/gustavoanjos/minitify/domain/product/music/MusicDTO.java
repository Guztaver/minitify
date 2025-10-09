package com.gustavoanjos.minitify.domain.product.music;

import com.gustavoanjos.minitify.domain.product.DTO;
import com.gustavoanjos.minitify.domain.product.album.Album;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

public record MusicDTO(String title, @Validated Album album, int duration) implements DTO<Music> {
    @Override
    public Music toEntity() {
        return new Music(
                this.title,
                this.album,
                this.duration
        );
    }

    public record WithAlbumId(String title, UUID albumId, int duration) {
    }
}
