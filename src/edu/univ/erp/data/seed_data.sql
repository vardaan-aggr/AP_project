-- NECESSARY
use erp_db;
insert into settings (settings_key, settings_value) values ('maintain_mode', 'false');
INSERT INTO settings (settings_key, settings_value) VALUES ('drop_deadline', '2025-12-01');
use auth_db;
INSERT INTO auth_table  (roll_no, username, role, hash_password) VALUES
    ('1', 'adm1', 'admin', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S'),
    ('2', 'ins1', 'instructor', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S'),
    ('3', 'std1', 'student', '$2a$10$p.uYMyUofLa94cKp0Lqfsuwm.RaYPRKBYAUtGpycR/aPXSUgo7u/S');
-- ///////////////

use erp_db;
Insert into courses(course_code, title, section, credits) VALUES 
    ('ECE250', 'Signals and Systems', 'N', '4'),
    ('MTH203', 'Calculas 3', 'N', '4'),
    ('MTH203', 'Maths3', 'B', '4');
Insert  into instructors (roll_no, department) VALUES
    ('2','ECE');

Insert into enrollments (roll_no, course_code ,section, status) values 
    ('3', 'ECE250', 'N', 'enrolled'),
    ('3', 'MTH203', 'B', 'enrolled');
Insert into grades (roll_no, course_code, section, grade) values
    ('3', 'ECE250', 'N', 'A'),
    ('3', 'MTH203', 'B', 'B');
Insert into sections (course_code, section, roll_no, day_time, room, capacity, semester, year) values
    ('ECE250', 'N', '2', 'Wed 10:30-12:00, Fri 8:30-11:00', 'C102', '600', 'Monsoon', '2025'),
    ('CTD', 'N', '2', 'Wed 10:30-12:00, Fri 8:30-11:00', 'C202', '610', 'Monsoon', '2025'),
    ('MTH203', 'B', '2', 'Tue 4:30-6:00, Thu 4:30-6:00', 'B-003', '200', 'Monsoon', '2025');
Insert into students(roll_no, program ,year ) values
    ('11', 'B.Tech KSAI', '4');
use auth_db;
delete from auth_table where username = 'asdas';
select * from grades;
update instructors set department = 'ECE' where roll_no = 2;

select * from enrollments;
drop table sections;
use erp_db;
delete from grades where section = 'B';
select * from grades;
Insert into courses(course_code, title, section, credits) VALUES 
    ('IP12', 'Intro to Plumbing', 'N', '6');
use erp_db;
delete from grades where roll_no = 4;