INSERT INTO auth_table (roll_no, username, role, hash_password) VALUES
    ('1', 'admin1', 'admin', '$2a$10$3b1FoH6INdCd/kR46yuG5OGJxTdTsd9Mdfk3c5mKbMTqgFT0Oa9qu'),
    ('2', 'instructor1', 'instructor', '$2a$10$VIBCacj3YM50mKIhgugjq.LOUO3LO1tIkjhoKolBggMiLFKTN7Yfe'),
    ('3', 'student1', 'student', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S')
Insert into courses (course_code, title, section, credits) VALUES 
    ('ECE250', 'Signals and Systems', 'N', '4')
use erp_db;
Insert into instructors (roll_no, department) VALUES
    ('2','ECE250');
Insert into enrollments (roll_no, section_id, status) values 
    ('3', 'N', 'enrolled');
Insert into sections (course_code, instructor_id, day_time, room, capacity, semester, year) values
    ('ECE250', '2', 'Fri_9:00-11:00', 'C102', '600', 'Monsoon', '2025');
Insert into grades (roll_no, course_code, grade) values
    ('1', 'ECE250', 'A-');
use erp_db;
Insert into sections (course_code, roll_no, day_time, room, capacity, semester, year) values
    ('ECE250', '2', 'Wed-(10:30-12:00), Fri-(8:30-11:00)', 'C102', '600', 'Monsoon', '2025')