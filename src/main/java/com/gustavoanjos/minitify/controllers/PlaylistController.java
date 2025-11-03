package com.gustavoanjos.minitify.controllers;

import com.gustavoanjos.minitify.domain.product.playlist.Playlist;
import com.gustavoanjos.minitify.domain.product.playlist.PlaylistDTO;
import com.gustavoanjos.minitify.domain.product.user.User;
import com.gustavoanjos.minitify.domain.services.PlaylistService;
import com.gustavoanjos.minitify.domain.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/playlists")
@Tag(name = "Playlists", description = "Playlist management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserService userService;

    public PlaylistController(PlaylistService playlistService, UserService userService) {
        this.playlistService = playlistService;
        this.userService = userService;
    }

    @Operation(summary = "Create a new playlist", description = "Create a new playlist for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Playlist created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<PlaylistDTO.Response> createPlaylist(@Valid @RequestBody PlaylistDTO.CreateRequest request) {
        User currentUser = getCurrentUser();

        Playlist playlist = playlistService.createPlaylist(
                request.name(),
                request.description(),
                request.isPublic(),
                currentUser
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PlaylistDTO.Response.fromPlaylist(playlist));
    }

    @Operation(summary = "Get all playlists", description = "Get all playlists accessible by the current user (owned + public)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<PlaylistDTO.Response>> getAllPlaylists() {
        User currentUser = getCurrentUser();

        List<PlaylistDTO.Response> playlists = playlistService.getAllAccessibleByUser(currentUser.getId())
                .stream()
                .map(PlaylistDTO.Response::fromPlaylist)
                .collect(Collectors.toList());

        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Get my playlists", description = "Get all playlists owned by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists retrieved successfully")
    })
    @GetMapping("/my")
    public ResponseEntity<List<PlaylistDTO.Response>> getMyPlaylists() {
        User currentUser = getCurrentUser();

        List<PlaylistDTO.Response> playlists = playlistService.getPlaylistsByUser(currentUser)
                .stream()
                .map(PlaylistDTO.Response::fromPlaylist)
                .collect(Collectors.toList());

        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Get public playlists", description = "Get all public playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists retrieved successfully")
    })
    @GetMapping("/public")
    public ResponseEntity<List<PlaylistDTO.Response>> getPublicPlaylists() {
        List<PlaylistDTO.Response> playlists = playlistService.getPublicPlaylists()
                .stream()
                .map(PlaylistDTO.Response::fromPlaylist)
                .collect(Collectors.toList());

        return ResponseEntity.ok(playlists);
    }

    @Operation(summary = "Get playlist by ID", description = "Get a specific playlist with all its musics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist found"),
            @ApiResponse(responseCode = "404", description = "Playlist not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO.WithMusics> getPlaylistById(@PathVariable String id) {
        User currentUser = getCurrentUser();
        Playlist playlist = playlistService.getPlaylistById(id);

        // Check access
        if (!playlistService.canUserAccessPlaylist(id, currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(PlaylistDTO.WithMusics.fromPlaylist(playlist));
    }

    @Operation(summary = "Update playlist", description = "Update playlist details (only owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist updated successfully"),
            @ApiResponse(responseCode = "404", description = "Playlist not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO.Response> updatePlaylist(
            @PathVariable String id,
            @Valid @RequestBody PlaylistDTO.UpdateRequest request) {
        User currentUser = getCurrentUser();

        Playlist updated = playlistService.updatePlaylist(
                id,
                currentUser,
                request.name(),
                request.description(),
                request.isPublic()
        );

        return ResponseEntity.ok(PlaylistDTO.Response.fromPlaylist(updated));
    }

    @Operation(summary = "Delete playlist", description = "Delete a playlist (only owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Playlist deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Playlist not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String id) {
        User currentUser = getCurrentUser();
        playlistService.deletePlaylist(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add music to playlist", description = "Add a music to a playlist (only owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Music added successfully"),
            @ApiResponse(responseCode = "404", description = "Playlist or music not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/{id}/musics")
    public ResponseEntity<PlaylistDTO.WithMusics> addMusicToPlaylist(
            @PathVariable String id,
            @Valid @RequestBody PlaylistDTO.AddMusicRequest request) {
        User currentUser = getCurrentUser();

        Playlist updated = playlistService.addMusicToPlaylist(id, request.musicId(), currentUser);

        return ResponseEntity.ok(PlaylistDTO.WithMusics.fromPlaylist(updated));
    }

    @Operation(summary = "Remove music from playlist", description = "Remove a music from a playlist (only owner)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Music removed successfully"),
            @ApiResponse(responseCode = "404", description = "Playlist or music not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}/musics/{musicId}")
    public ResponseEntity<PlaylistDTO.WithMusics> removeMusicFromPlaylist(
            @PathVariable String id,
            @PathVariable String musicId) {
        User currentUser = getCurrentUser();

        Playlist updated = playlistService.removeMusicFromPlaylist(id, musicId, currentUser);

        return ResponseEntity.ok(PlaylistDTO.WithMusics.fromPlaylist(updated));
    }

    @Operation(summary = "Get user's playlists by user ID", description = "Get all public playlists for a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlists retrieved successfully")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistDTO.Response>> getPlaylistsByUserId(@PathVariable String userId) {
        List<PlaylistDTO.Response> playlists = playlistService.getPlaylistsByUserId(userId)
                .stream()
                .filter(Playlist::isPublic)
                .map(PlaylistDTO.Response::fromPlaylist)
                .collect(Collectors.toList());

        return ResponseEntity.ok(playlists);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.getRepository().findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
