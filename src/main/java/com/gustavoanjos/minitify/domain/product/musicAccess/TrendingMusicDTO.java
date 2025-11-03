package com.gustavoanjos.minitify.domain.product.musicAccess;

/**
 * DTO for trending music data
 */
public record TrendingMusicDTO(String musicId, Long count, String title, String albumId) {
}
