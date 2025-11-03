package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.album.Album;
import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import com.gustavoanjos.minitify.domain.services.AlbumService;
import com.gustavoanjos.minitify.domain.services.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/artist")
@Tag(name = "Artists", description = "Artist management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ArtistController {

    private final ArtistService service;
    private final ArtistRepository repository;

    public ArtistController(ArtistService service) {
        this.service = service;
        this.repository = service.getRepository();
    }

    @Operation(summary = "Create artist", description = "Create a new artist (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artist created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    @PostMapping
    public ResponseEntity<ArtistDTO> createArtist(@RequestBody @Validated ArtistDTO artistDTO) {
        var artist = service.createArtist(artistDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ArtistDTO.fromArtist(artist));
    }

    @Operation(summary = "Get all artists", description = "Retrieve all artists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artists retrieved successfully")
    })
    @GetMapping("/all")
    public ResponseEntity<Collection<ArtistDTO>> findAll(@RequestParam(required = false) String genre) {
        var artists = Optional.ofNullable(genre)
                .filter(g -> !g.isEmpty())
                .map(repository::findByGenre)
                .orElseGet(repository::findAll)
                .stream()
                .map(ArtistDTO::fromArtist)
                .collect(Collectors.toList());
        return ResponseEntity.ok(artists);
    }

    @Operation(summary = "Delete artist", description = "Delete an artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArtist(@PathVariable @Validated String id) {
        log.info("Deleting artist with ID: {}", id);
        service.deleteArtist(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update artist", description = "Update an existing artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> updateArtist(
            @RequestBody @Validated ArtistDTO artistDTO,
            @PathVariable @Validated String id
    ) {
        log.info("Updating artist with ID: {}", id);
        var updatedArtist = service.updateArtist(id, artistDTO);
        return ResponseEntity.ok(ArtistDTO.fromArtist(updatedArtist));
    }

    @Operation(summary = "Get artist by ID", description = "Retrieve a specific artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist found"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> findById(@PathVariable @Validated String id) {
        log.info("Finding artist with ID: {}", id);
        var artist = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Artist not found with ID: " + id)
        );
        return ResponseEntity.ok(ArtistDTO.fromArtist(artist));
    }

    @Operation(summary = "Get artist albums", description = "Retrieve albums of a specific artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Albums retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @GetMapping("/{id}/albums")
    public ResponseEntity<Set<Album>> getArtistAlbums(@PathVariable @Validated String id, AlbumService albumService) {
        var artist = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Artist not found with ID: " + id)
        );
        var albums = albumService.getRepository().findByArtist(artist);
        return ResponseEntity.ok(albums);
    }
}