CREATE TABLE albums
(
    id           UUID NOT NULL,
    title        VARCHAR(255),
    artist_id    UUID,
    genre        VARCHAR(255),
    release_year TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_albums PRIMARY KEY (id)
);

ALTER TABLE albums
    ADD CONSTRAINT FK_ALBUMS_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artists (id);