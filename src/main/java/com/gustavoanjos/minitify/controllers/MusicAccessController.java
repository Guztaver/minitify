package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.services.MusicAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
@Tag(name = "Trendings", description = "Endpoints for music access statistics and trending")
@SecurityRequirement(name = "bearerAuth")
public class MusicAccessController {

    private final MusicAccessService musicAccessService;

    public MusicAccessController(MusicAccessService musicAccessService) {
        this.musicAccessService = musicAccessService;
    }

    @GetMapping("/musics/{id}/access/count")
    @Operation(summary = "Get unique access count for a music")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count returned"),
            @ApiResponse(responseCode = "404", description = "Music not found")
    })
    public ResponseEntity<Map<String, Object>> countForMusic(@PathVariable UUID id) {
        long count = musicAccessService.countForMusic(id);
        return ResponseEntity.ok(Map.of("musicId", id, "count", count));
    }

    @GetMapping("/musics/trending")
    @Operation(summary = "Trending musics by unique user accesses")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trending returned")
    })
    public ResponseEntity<List<Map<String, Object>>> trending(@RequestParam(defaultValue = "10") int limit) {
        var list = musicAccessService.trending(limit);
        return ResponseEntity.ok(list);
    }
}

