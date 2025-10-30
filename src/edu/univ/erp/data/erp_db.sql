CREATE DATABASE IF NOT EXISTS erp_db;

USE erp_db;

CREATE TABLE IF NOT EXISTS students (
    user_id INT PRIMARY KEY,                 -- This MUST match the user_id from auth_db
    roll_no VARCHAR(20) UNIQUE NOT NULL,
    program VARCHAR(100),
    year INT,
    FOREIGN KEY (user_id) REFERENCES auth_db.users_auth(user_id) -- Link to the other DB
);

-- Create the instructors table [cite: 49]
CREATE TABLE IF NOT EXISTS instructors (
    user_id INT PRIMARY KEY,                 -- This also matches the user_id from auth_db
    department VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES auth_db.users_auth(user_id) -- Link to the other DB
);

-- Create the courses table [cite: 50]
CREATE TABLE IF NOT EXISTS courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(100),
    credits INT
);

-- Create the sections table [cite: 51]
CREATE TABLE IF NOT EXISTS sections (
    section_id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT,
    instructor_id INT,
    day_time VARCHAR(50),
    room VARCHAR(20),
    capacity INT,
    semester VARCHAR(20),
    year INT,
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (instructor_id) REFERENCES instructors(user_id)
);

-- Create the enrollments table [cite: 52]
CREATE TABLE IF NOT EXISTS enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    section_id INT,
    status VARCHAR(20),
    UNIQUE(student_id, section_id), -- Prevents a student from enrolling in the same section twice [cite: 52]
    FOREIGN KEY (student_id) REFERENCES students(user_id),
    FOREIGN KEY (section_id) REFERENCES sections(section_id)
);

-- Create the grades table [cite: 53]
CREATE TABLE IF NOT EXISTS grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    enrollment_id INT,
    component VARCHAR(50), -- e.g., 'quiz', 'midterm'
    score DECIMAL(5, 2),
    final_grade VARCHAR(2),
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id)
);

-- Create the settings table for things like Maintenance Mode [cite: 54]
CREATE TABLE IF NOT EXISTS settings (
    setting_key VARCHAR(50) PRIMARY KEY,
    setting_value VARCHAR(50)
);