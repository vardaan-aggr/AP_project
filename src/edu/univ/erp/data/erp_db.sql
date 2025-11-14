Create database IF NOT EXISTS erp_db;
use erp_db;

Create table IF NOT EXISTS students (
    roll_no INT PRIMARY KEY,
    program varchar(50),
    year INT 
);
Create table IF NOT EXISTS instructors (
    roll_no INT PRIMARY KEY,
    department varchar(50)
);
Create table IF NOT EXISTS courses (
    course_code varchar(50),
    title varchar (50),
    section varchar(50),
    credits int,
    PRIMARY KEY (course_code, section)
);
Create table IF NOT EXISTS enrollments (
    roll_no int,
    course_code varchar(50),
    section varchar(50),
    status varchar(50),
    PRIMARY KEY(roll_no, course_code, section)
);
Create table IF NOT EXISTS sections (
    course_code varchar(50),
    section varchar(50),
    roll_no varchar(50),
    day_time varchar (50),
    room varchar (50),
    capacity INT  ,
    semester varchar(50),
    year INT 
    -- PRIMARY KEY(course_code, section)
);
Create table IF NOT EXISTS grades (
    roll_no INT ,
    course_code varchar(50),
    section varchar(50),
    grade varchar(50),
    -- PRIMARY KEY (course_code, section)
);
