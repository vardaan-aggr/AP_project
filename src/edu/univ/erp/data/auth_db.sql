CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

CREATE TABLE IF NOT EXISTS auth_table (
    user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    role VARCHAR(50),
    hash_password VARCHAR(255),
    status boolean,
    last_login DATETIME
);
