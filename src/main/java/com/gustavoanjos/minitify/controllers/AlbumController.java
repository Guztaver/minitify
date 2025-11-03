package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.album.AlbumDTO;
import com.gustavoanjos.minitify.domain.repositories.AlbumRepository;
import com.gustavoanjos.minitify.domain.services.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
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
@SecurityRequirement(name = "bearerAuth")
public class AlbumController {

    private final AlbumService service;
    private final AlbumRepository repository;

    public AlbumController(AlbumService service) {
        this.service = service;
        this.repository = service.getRepository();
    }

    @Operation(summary = "Create a new album", description = "Create a new album with the provided data")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description =  "Album created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid album data provided")
    })
    public ResponseEntity<HttpStatus> createAlbum(@Validated @RequestBody AlbumDTO.WithArtistId data) {
        service.createAlbum(data);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get all albums", description = "Retrieve a list of all albums")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description =  "Albums retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No albums found")
    })
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
        var albums = repository.findAll()
                .stream()
                .map(AlbumDTO::fromAlbum)
                .collect(Collectors.toList());

        if (albums.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(albums);
    }

    @Operation(summary = "Get album by ID", description = "Retrieve a specific album by its ID")
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Album retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable String id) {
        var album = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Album with id " + id + " not found")
        );

        return ResponseEntity.ok(AlbumDTO.fromAlbum(album));
    }

    @Operation(summary = "Delete album by ID", description = "Delete a specific album by its ID")
    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    public ResponseEntity<HttpStatus> deleteAlbumById(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update album", description = "Update an existing album")
    @PutMapping("/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Album updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid album data provided"),
            @ApiResponse(responseCode = "404", description = "Album not found")
    })
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable String id, @Validated @RequestBody AlbumDTO.WithArtistId data) {
        var album = repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Album with id " + id + " not found")
        );

        service.update(album.getId(), data);

        return ResponseEntity.ok(AlbumDTO.fromAlbum(album));
    }
}
