CREATE DATABASE auth_db ;

USE auth_db;

CREATE TABLE users_auth (
    user_id INT PRIMARY KEY AUTO_INCREMENT,  -- A unique ID for each user
    username VARCHAR(50) UNIQUE NOT NULL,    -- The username, must be unique
    role VARCHAR(20) NOT NULL,               -- 'student', 'instructor', or 'admin'
    password_hash VARCHAR(255) NOT NULL,     -- The secure hashed password
    status VARCHAR(20) DEFAULT 'active',     -- e.g., 'active', 'locked'
    last_login TIMESTAMP NULL                -- The last time they logged in
);