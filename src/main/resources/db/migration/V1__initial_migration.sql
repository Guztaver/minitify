CREATE TABLE albums
(
    id           UUID NOT NULL,
    title        VARCHAR(255),
    artist_id    UUID,
    genre        VARCHAR(255),
    release_year TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_albums PRIMARY KEY (id)
);

CREATE TABLE app_users
(
    id       UUID         NOT NULL,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    CONSTRAINT pk_app_users PRIMARY KEY (id)
);

CREATE TABLE artists
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    genre       VARCHAR(255),
    CONSTRAINT pk_artists PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

ALTER TABLE albums
    ADD CONSTRAINT FK_ALBUMS_ON_ARTIST FOREIGN KEY (artist_id) REFERENCES artists (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES app_users (id);