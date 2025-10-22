USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'student1', 'student', 'your_generated_bcrypt_hash_here');
-- mdo1

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (2, 'instructor1', 'instructor', 'your_generated_bcrypt_hash_here');
-- mdo2