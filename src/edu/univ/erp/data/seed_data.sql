USE auth_db;

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (1, 'student1', 'student', '$2a$10$VXmhYmoGz6txvP684BvLNOmg6aqwFtN/l9HXNgHg5m9jamFtWWXMi');
-- mdo1

INSERT INTO users_auth (user_id, username, role, password_hash) 
VALUES (2, 'instructor1', 'instructor', '$2a$10$67hQA2kMSxRZB/0hPjMSMearlKJfcZ80eCGweqMWPbfWxHtkHLbje');
-- mdo2

USE erp_db;

INSERT INTO instructors (user_id, department) 
VALUES (2, 'Computer Science');

INSERT INTO students (user_id, roll_no, program, year)
VALUES (1, '2024001', 'Advance Programming', 2025); 

INSERT INTO courses (code, title, credits) 
VALUES 
("CSE201", "Advance Programming", 4),
("MTH203", 'Maths III', 4),
("ECE250", 'Signals and Systems', 4);

INSERT INTO sections (course_id, instructor_id, day_time, room, capacity, semester, year) 
VALUES 
(1, 2, 'Tue/Thu 15:00', 'C102', 250, 'Monsoon', 2025);
(2, 2, 'Tue/Thu 16:30', 'MTH203', 25, 'Monsoon', 2025); 