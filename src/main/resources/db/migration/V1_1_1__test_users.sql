-- Inserting data into the 'users' table
INSERT INTO users (email, name, password, active, created_at, updated_at)
VALUES ('admin@test.com', 'ADMIN', '$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2', true, NOW(), NOW()),
       ('bla@test.com', 'user123', '$2a$10$mNsTkkPYAu5p1/KtZQmOz.NDA6h3cvKAA8.T79NGUi.tFUnwCpU3m', true, NOW(), NOW());

-- Inserting data into the 'users_roles' table and establishing a relationship
INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON
        (u.email = 'admin@test.com' AND r.name = 'ADMINISTRATOR') OR
        (u.email = 'bla@test.com' AND r.name = 'USER');
