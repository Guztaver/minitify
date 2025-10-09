package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.aspects.TrackMusicAccess;
import com.gustavoanjos.minitify.domain.product.music.Music;
import com.gustavoanjos.minitify.domain.product.music.MusicDTO;
import com.gustavoanjos.minitify.domain.repositories.MusicRepository;
import com.gustavoanjos.minitify.domain.services.MusicService;
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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/musics")
@Tag(name = "Music", description = "Endpoints for managing music tracks")
@SecurityRequirement(name = "bearerAuth")
public class MusicController {

    private final MusicService musicService;
    private final MusicRepository repository;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
        this.repository = musicService.getRepository();
    }

    @PostMapping
    @Operation(summary = "Create a new music track", description = "Creates a new music track associated with an album")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Music created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpStatus> createMusic(@Validated @RequestBody MusicDTO.WithAlbumId data) {
        log.info("Creating music: {}", data);
        musicService.createMusic(data);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get music by ID", description = "Retrieves a music track by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Music retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Music not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @TrackMusicAccess
    public ResponseEntity<Music> getMusicById(@PathVariable UUID id) {
        log.info("Fetching music with ID: {}", id);
        var music = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        return ResponseEntity.ok(music);
    }

    @GetMapping
    @Operation(summary = "Get all music tracks", description = "Retrieves all music tracks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Music tracks retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Iterable<Music>> getAllMusics() {
        log.info("Fetching all music tracks");
        var musics = repository.findAll();
        return ResponseEntity.ok(musics);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a music track", description = "Updates an existing music track by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Music updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Music not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpStatus> updateMusic(@PathVariable UUID id, @Validated @RequestBody MusicDTO.WithAlbumId data) {
        log.info("Updating music with ID: {}", id);
        var existingMusic = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        musicService.updateMusic(existingMusic, data);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a music track", description = "Deletes a music track by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Music deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Music not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<HttpStatus> deleteMusic(@PathVariable UUID id) {
        log.info("Deleting music with ID: {}", id);
        musicService.deleteMusic(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/album")
    @Operation(summary = "Get album of a music track", description = "Retrieves the album associated with a music track by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Album retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Music or Album not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAlbumByMusicId(@PathVariable UUID id) {
        log.info("Fetching album for music with ID: {}", id);
        var music = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
        var album = music.getAlbum();
        return ResponseEntity.ok(album);
    }
}
