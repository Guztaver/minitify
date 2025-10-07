package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.album.AlbumCreationDTO;
import com.gustavoanjos.minitify.domain.repositories.AlbumRepository;
import com.gustavoanjos.minitify.domain.services.AlbumService;
import com.gustavoanjos.minitify.dto.AlbumResponseDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/albums")
@Tag(description = "Endpoints for managing albums", name = "Album Controller")
public class AlbumController {

    private final AlbumService albumService;
    private final AlbumRepository repository;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
        this.repository = albumService.getRepository();
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "Album created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid album data provided")
    })
    public ResponseEntity<HttpStatus> createAlbum(@Validated @RequestBody AlbumCreationDTO data) {
        albumService.createAlbum(data);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "Albums retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No albums found")
    })
    public ResponseEntity<List<AlbumResponseDTO>> getAllAlbums() {
        var albums = repository.findAll()
                .stream()
                .map(AlbumResponseDTO::fromAlbum)
                .collect(Collectors.toList());

        return ResponseEntity.ok(albums);
    }
}
