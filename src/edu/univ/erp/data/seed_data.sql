USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'student1', 'student', '$2a$10$xhRzK.a4sM7mZLAKmx04ROVY/mESpBexzFBMdX6bure4UwuPV/Fym');
-- mdo1

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (2, 'instructor1', 'instructor', '$2a$10$VDLz9lUzl31OqhWZbFa3/.RruoQaislKBGz1SA.nKaTNK12M7bt4O');
-- mdo2