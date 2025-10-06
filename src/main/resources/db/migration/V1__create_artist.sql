CREATE TABLE artist
(
    id            BINARY(16)   NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    genre         VARCHAR(255) NULL,
    CONSTRAINT pk_artist PRIMARY KEY (id)
);