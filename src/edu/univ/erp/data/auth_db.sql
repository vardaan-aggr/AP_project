CREATE DATABASE IF NOT EXISTS auth_db;
USE auth_db;

CREATE TABLE IF NOT EXISTS auth (
    user_id integer (7),
    username VARCHAR(255),
    role VARCHAR(255),
    hash_password VARCHAR(255),
    status boolean,
    last_login DATETIME
);

-- user id, username, role, password hash, status, last login