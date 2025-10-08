CREATE TABLE albums
(
    id           UUID         NOT NULL,
    title        VARCHAR(255) NOT NULL,
    artist_id    UUID         NOT NULL,
    genre        VARCHAR(255) NOT NULL,
    release_year TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_albums PRIMARY KEY (id)
);

CREATE TABLE app_users
(
    id       UUID         NOT NULL,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_app_users PRIMARY KEY (id)
);

CREATE TABLE artists
(
    id          UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    genre       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_artists PRIMARY KEY (id)
);

CREATE TABLE musics
(
    id       UUID         NOT NULL,
    title    VARCHAR(255) NOT NULL,
    duration INTEGER      NOT NULL,
    album_id UUID         NOT NULL,
    CONSTRAINT pk_musics PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

ALTER TABLE albums
    ADD CONSTRAINT FK_ALBUMS_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artists (id);

ALTER TABLE musics
    ADD CONSTRAINT FK_MUSICS_ON_ALBUM FOREIGN KEY (album_id) REFERENCES albums (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES app_users (id);