package com.gustavoanjos.minitify.domain.product.album;


import java.util.Date;
import java.util.UUID;

public record AlbumCreationDTO(String title, UUID artistId, String genre, Date releaseYear) {
}

