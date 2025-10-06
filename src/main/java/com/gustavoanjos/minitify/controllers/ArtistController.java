package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.artist.Artists;
import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
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
import java.util.UUID;

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
            @ApiResponse(responseCode = "200", description = "Artist created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
    })
    @PostMapping
    public ResponseEntity<HttpStatus> createArtist(@RequestBody @Validated ArtistDTO artistDTO) {
        service.createArtist(artistDTO);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "Get all artists", description = "Retrieve all artists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artists retrieved successfully")
    })
    @GetMapping("/all")
    public Collection<Artists> findAll() {
        log.info("Finding all artists");
        return repository.findAll();
    }

    @Operation(summary = "Delete artist", description = "Delete an artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArtist(@PathVariable @Validated UUID id) {
        log.info("Deleting artist with ID: {}", id);
        repository.findById(id).orElseThrow();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update artist", description = "Update an existing artist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist updated successfully"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateArtist(
            @RequestBody @Validated ArtistDTO artistDTO,
            @PathVariable @Validated UUID id
    ) {
        log.info("Updating artist with ID: {}", id);
        var artist = repository.findById(id);
        if (artist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.updateArtist(id, artistDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "Get artist by ID", description = "Retrieve a specific artist by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artist found"),
            @ApiResponse(responseCode = "404", description = "Artist not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Artists> findById(@PathVariable @Validated UUID id) {
        log.info("Finding artist with ID: {}", id);
        var artist = repository.findById(id);
        return artist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}