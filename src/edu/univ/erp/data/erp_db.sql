Create database IF NOT EXISTS erp_db;
use erp_db;

Create table IF NOT EXISTS students (
    roll_no int,
    program varchar(50),
    year int
);
Create table IF NOT EXISTS instructors (
    roll_no int,
    department varchar(50)
);
Create table IF NOT EXISTS courses (
    course_code varchar(50),
    title varchar (50),
    section varchar(50),
    credits int
);
Create table IF NOT EXISTS enrollments (
    roll_no int,
    section_id varchar(50),
    status varchar(50)
);
Create table IF NOT EXISTS sections (
    course_code varchar(50),
    roll_no varchar(50),
    day_time varchar (50),
    room varchar (50),
    capacity int,
    semester varchar(50),
    year int
);
Create table IF NOT EXISTS grades (
    roll_no int,
    course_code varchar(50),
    -- section varchar(50),
    grade varchar(50)
);
-- Create table IF NOT EXISTS settings (
--     students varchar(50),
--     instructor role (50),
--     courses varchar(50)
-- );
