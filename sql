create database contactdb;
use contactdb;
create table users(
id int  primary key auto_increment,
name varchar(100) not null,
email varchar(100) not null,
password varchar(100) not null,
phone varchar(20),
profile_pic varchar(255),
about text,
created_at timestamp default current_timestamp);
Select * from users;
create table contacts(
id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
Select*from contacts;
ALTER TABLE contacts ADD COLUMN category VARCHAR(50) DEFAULT 'Other';
ALTER TABLE contacts ADD COLUMN photo_path VARCHAR(255);
ALTER TABLE contacts ADD COLUMN notes TEXT;
ALTER TABLE contacts
ADD birthday DATE,
ADD anniversary DATE;
ALTER TABLE contacts ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE contacts ADD COLUMN is_favorite BOOLEAN DEFAULT FALSE;

CREATE TABLE activities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    contact_id INT,
    action_type ENUM('ADD', 'UPDATE', 'DELETE', 'REMINDER', 'FAVORITE', 'EXPORT') NOT NULL,
    description VARCHAR(255) NOT NULL,
    activity_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (contact_id) REFERENCES contacts(id)
);
CREATE TABLE reminders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    contact_id INT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    reminder_date DATETIME NOT NULL,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') NOT NULL DEFAULT 'MEDIUM',
    type ENUM('BIRTHDAY', 'ANNIVERSARY', 'MEETING', 'TASK', 'OTHER') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (contact_id) REFERENCES contacts(id)
);
UPDATE contacts SET created_at = NOW() WHERE created_at IS NULL;

USE contactdb;
SHOW TABLES;
DESCRIBE contacts;
select * from contacts;
SELECT * FROM contacts WHERE user_id = 3;
SHOW CREATE TABLE contacts;

create database cabinbooking;
use cabinbooking;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'customer'
);
select*from users;

CREATE TABLE cabins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    location VARCHAR(255) NOT NULL,
    price_per_night DECIMAL(10,2) NOT NULL,
    max_guests INT NOT NULL,
    bedrooms INT NOT NULL,
    bathrooms INT NOT NULL,
    amenities TEXT,
    image_url VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
ALTER TABLE cabins
ADD COLUMN is_featured BOOLEAN DEFAULT FALSE;
select * from cabins;
DESC cabins;
SELECT id, name, created_at FROM cabins ORDER BY created_at DESC;
