INSERT INTO users (email, name, password, active, created_at, updated_at)
VALUES ('admin@test.com', 'ADMIN', '$2a$10$/qhgXKLWEU8QkCykJCsa4.d9X5w/CdALuFPvcLFjBSXBp91eDrltm', true, NOW(), NOW()),
       ('bla@test.com', 'user123', '$2a$10$/qhgXKLWEU8QkCykJCsa4.d9X5w/CdALuFPvcLFjBSXBp91eDrltm', true, NOW(), NOW()),
       ('blabla@test.com', 'user23454', '$2a$10$/qhgXKLWEU8QkCykJCsa4.d9X5w/CdALuFPvcLFjBSXBp91eDrltm', true, NOW(),
        NOW());

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON
        (u.email = 'admin@test.com' AND r.name = 'ADMINISTRATOR') OR
        (u.email = 'bla@test.com' AND r.name = 'USER') OR
        (u.email = 'blabla@test.com' AND r.name = 'USER');
