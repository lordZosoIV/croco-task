CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    email        VARCHAR   NOT NULL UNIQUE,
    name         VARCHAR   NOT NULL UNIQUE,
    password     VARCHAR   NOT NULL,
    active       BOOLEAN   NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

INSERT INTO roles (name)
VALUES ('ADMINISTRATOR'),
       ('USER');