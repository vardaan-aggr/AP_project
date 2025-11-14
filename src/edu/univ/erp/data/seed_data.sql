use auth_db;
TRUNCATE TABLE auth_db.auth_table;
use erp_db;
TRUNCATE TABLE courses;
TRUNCATE TABLE instructors;
TRUNCATE TABLE enrollments;
TRUNCATE TABLE grades;
TRUNCATE TABLE sections;
TRUNCATE TABLE students;

use auth_db;
INSERT INTO auth_table  (roll_no, username, role, hash_password) VALUES
    ('1', 'admin1', 'admin', '$2a$10$3b1FoH6INdCd/kR46yuG5OGJxTdTsd9Mdfk3c5mKbMTqgFT0Oa9qu'),
    ('2', 'instructor1', 'instructor', '$2a$10$VIBCacj3YM50mKIhgugjq.LOUO3LO1tIkjhoKolBggMiLFKTN7Yfe'),
    ('3', 'student1', 'student', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S');

use erp_db;
Insert into courses(course_code, title, section, credits) VALUES 
    ('ECE250', 'Signals and Systems', 'N', '4'),
    ('MTH203', 'Calculas 3', 'N', '4');
    ('MTH203', 'Maths3', 'B', '4');
Insert  into instructors (roll_no, department) VALUES
    ('2','ECE');

Insert into enrollments (roll_no, course_code ,section, status) values 
    ('3', 'ECE250', 'N', 'enrolled');
    ('3', 'MTH203', 'B', 'enrolled');
Insert into grades (roll_no, course_code, section, grade) values
    ('3', 'ECE250', 'N', 'A');
    ('3', 'MTH203', 'B', 'B');
Insert into sections (course_code, section, roll_no, day_time, room, capacity, semester, year) values
    ('ECE250', 'N', '2', 'Wed 10:30-12:00, Fri 8:30-11:00', 'C102', '600', 'Monsoon', '2025');
    ('CTD', 'N', '2', 'Wed 10:30-12:00, Fri 8:30-11:00', 'C202', '610', 'Monsoon', '2025');
    ('MTH203', 'B', '2', 'Tue 4:30-6:00, Thu 4:30-6:00', 'B-003', '200', 'Monsoon', '2025');
Insert into students(roll_no ,program ,year ) values
    ('3', 'B.Tech ECE', '2');


select * from grades;