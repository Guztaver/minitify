package com.gustavoanjos.minitify.domain.product.musicAccess;

import java.util.UUID;

/**
 * DTO for trending music data
 */
public record TrendingMusicDTO(UUID musicId, Long count, String title, UUID albumId) {
}
