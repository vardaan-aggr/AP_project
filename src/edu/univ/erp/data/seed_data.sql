USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'Chandrapal', 'admin', 'your_generated_bcrypt_hash_here');

select * from users_auth;