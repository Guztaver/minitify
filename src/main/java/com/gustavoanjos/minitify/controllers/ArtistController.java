package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.artist.Artist;
import com.gustavoanjos.minitify.domain.product.artist.ArtistDTO;
import com.gustavoanjos.minitify.domain.repositories.ArtistRepository;
import com.gustavoanjos.minitify.domain.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistService service;
    private final ArtistRepository repository;

    public ArtistController(ArtistService service) {
        this.service = service;
        this.repository = service.getRepository();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createArtist(@RequestBody @Validated ArtistDTO artistDTO) {
        repository.save(new Artist(artistDTO.name(), artistDTO.description(), artistDTO.genre()));

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public Collection<Artist> findAll() {
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArtist(@PathVariable @Validated UUID id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateArtist(
            @RequestBody @Validated ArtistDTO artistDTO,
            @PathVariable @Validated UUID id
    ) {
        var artist = repository.findById(id);
        if (artist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        service.updateArtist(id, artistDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable @Validated UUID id) {
        var artist = repository.findById(id);
        return artist.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}