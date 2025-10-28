USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'student1', 'student', '$2a$10$VXmhYmoGz6txvP684BvLNOmg6aqwFtN/l9HXNgHg5m9jamFtWWXMi');
-- mdo1

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (2, 'instructor1', 'instructor', '$2a$10$67hQA2kMSxRZB/0hPjMSMearlKJfcZ80eCGweqMWPbfWxHtkHLbje');
-- mdo2