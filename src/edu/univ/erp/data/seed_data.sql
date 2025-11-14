INSERT INTO auth_table (roll_no, username, role, hash_password) VALUES
    ('1', 'admin1', 'admin', '$2a$10$3b1FoH6INdCd/kR46yuG5OGJxTdTsd9Mdfk3c5mKbMTqgFT0Oa9qu'),
    ('2', 'instructor1', 'instructor', '$2a$10$VIBCacj3YM50mKIhgugjq.LOUO3LO1tIkjhoKolBggMiLFKTN7Yfe'),
    ('3', 'student1', 'student', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S')
Insert into courses (course_code, title, section, credits) VALUES 
    ('ECE250', 'Signals and Systems', 'N', '4');
Insert into instructors (roll_no, department) VALUES
    ('2','ECE250');
Insert into enrollments (roll_no, course_code ,section, status) values 
    ('3', 'ECE250', 'N', 'enrolled');
Insert into grades (roll_no, course_code, section, grade) values
    ('3', 'ECE250', 'N', 'A');
use erp_db;
Insert into sections (course_code, section, roll_no, day_time, room, capacity, semester, year) values
    ('ECE250', 'N', '2', 'Wed 10:30-12:00, Fri 8:30-11:00', 'C102', '600', 'Monsoon', '2025');
use erp_db;
select * from grades;
Insert into courses (course_code, title, section, credits) VALUES 
    ('MTH203', 'Maths3', 'B', '4');
Insert into grades (roll_no, course_code, section, grade) values
    ('3', 'MTH203', 'B', 'B');
select * from enrollments;
Insert into enrollments (roll_no, course_code ,section, status) values 
    ('3', 'MTH203', 'B', 'enrolled');
use erp_db;
Insert into sections (course_code, section, roll_no, day_time, room, capacity, semester, year) values
    ('MTH203', 'B', '2', 'Tue 4:30-6:00, Thu 4:30-6:00', 'B-003', '200', 'Monsoon', '2025');
