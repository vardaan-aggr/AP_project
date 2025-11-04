Create database IF NOT EXIST auth_db;
use auth_db;
Create table  IF NOT EXIST auth {
    user_id varchar (255),
    username varchar(255),
    role varchar(255),
    hash_password varchar(255),
    varchar(255) status
};

-- user id, username, role, password hash, status, last login)