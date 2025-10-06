CREATE TABLE app_users
(
    id       UUID NOT NULL,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE artists
(
    id          UUID NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(255),
    genre       VARCHAR(255),
    CONSTRAINT pk_artist PRIMARY KEY (id)
);

CREATE TABLE user_roles
(
    user_id UUID NOT NULL,
    roles   VARCHAR(255)
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_on_user FOREIGN KEY (user_id) REFERENCES app_users (id);