USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'Chandrapal', 'admin', 'your_generated_bcrypt_hash_here');

select * from users_auth;

UPDATE users_auth
SET password_hash = '$2a$10$GNPhgEPY6HE4xIGePzDD3OiRGVy7gBcu3fkoiUGfyibbzyCnUER7a'
WHERE user_id = 1;