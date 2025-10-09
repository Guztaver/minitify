-- Create playlists table
CREATE TABLE playlists
(
    id          UUID                        NOT NULL,
    name        VARCHAR(255)                NOT NULL,
    description VARCHAR(500),
    user_id     UUID                        NOT NULL,
    is_public   BOOLEAN                     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_playlists PRIMARY KEY (id)
);

-- Create playlist_musics join table (many-to-many relationship)
CREATE TABLE playlist_musics
(
    playlist_id UUID NOT NULL,
    music_id    UUID NOT NULL,
    CONSTRAINT pk_playlist_musics PRIMARY KEY (playlist_id, music_id)
);

-- Add foreign keys
ALTER TABLE playlists
    ADD CONSTRAINT fk_playlists_on_user FOREIGN KEY (user_id) REFERENCES app_users (id) ON DELETE CASCADE;

ALTER TABLE playlist_musics
    ADD CONSTRAINT fk_playlist_musics_on_playlist FOREIGN KEY (playlist_id) REFERENCES playlists (id) ON DELETE CASCADE;

ALTER TABLE playlist_musics
    ADD CONSTRAINT fk_playlist_musics_on_music FOREIGN KEY (music_id) REFERENCES musics (id) ON DELETE CASCADE;

-- Create indexes for better performance
CREATE INDEX idx_playlists_user_id ON playlists (user_id);
CREATE INDEX idx_playlists_is_public ON playlists (is_public);
CREATE INDEX idx_playlist_musics_music_id ON playlist_musics (music_id);

