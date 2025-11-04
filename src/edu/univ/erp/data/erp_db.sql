Create database IF NOT EXIST erp_db;
use erp_db;
Create table IF NOT EXIST students {
    roll_no int,
    program varchar(50),
    year int
};
Create table IF NOT EXIST instructors {
    roll_no int,
    department varchar(50)
};
Create table IF NOT EXIST courses {
    code varchar(50),
    title varchar (50),
    section varchar(50),
    credits int,
};
Create table IF NOT EXIST enrollments {
    roll_no int,
    section_id varchar(50),
    status varchar(50)
};
Create table IF NOT EXIST sections {
    course_id varchar(50),
    instructor_id varchar(50),
    day_time varchar (50),
    room varchar (50),
    capacity int,
    semester varchar(50),
    year int
};
-- Create table IF NOT EXIST grades {
--     score int,
--     final_grade int
-- };
-- Create table IF NOT EXIST settings {
--     students varchar(50),
--     instructor role (50),
--     courses varchar(50)
-- };