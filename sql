

create database cabinbooking;
use cabinbooking;

-----------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'customer'
);
select*from users;
------------------------------------------------------------------------------------------------------------------------------------------------------
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
ALTER TABLE cabins
ADD COLUMN latitude DECIMAL(10, 8),
ADD COLUMN longitude DECIMAL(11, 8);
----------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE cabins CHANGE price_per_night hourly_rate DECIMAL(10,2) NOT NULL;
ALTER TABLE cabins CHANGE max_guests capacity INT NOT NULL;
ALTER TABLE cabins
DROP COLUMN bedrooms,
DROP COLUMN bathrooms,
DROP COLUMN latitude,
DROP COLUMN longitude,
DROP COLUMN is_featured;
select * from cabins;
show create table cabins;
select * from cabins;
-----------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE `cabins`
  CHANGE `hourly_rate` `price_per_night` DECIMAL(10,2) NOT NULL,
  CHANGE `capacity` `max_guests` INT NOT NULL,
  ADD `bedrooms` INT DEFAULT 0,
  ADD `bathrooms` INT DEFAULT 0,
  ADD `is_featured` TINYINT(1) DEFAULT 0,
  ADD `image_urls` TEXT,
  ADD `latitude` DOUBLE,
  ADD `longitude` DOUBLE;
-----------------------------------------------------------------------------------------------------------------------------------------------------
ALTER TABLE cabins
  CHANGE price_per_night hourly_rate DECIMAL(10,2) NOT NULL,
  CHANGE max_guests capacity INT NOT NULL,
  DROP COLUMN bedrooms,
  DROP COLUMN bathrooms,
  DROP COLUMN is_featured,
  DROP COLUMN image_urls,
  DROP COLUMN latitude,
  DROP COLUMN longitude;
  Describe cabins;
  DROP TABLE IF EXISTS cabin_images;
 ----------------------------------------------------------------------------------------------------------------------------------------------------
UPDATE cabins SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
ALTER TABLE cabins MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

UPDATE cabins
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL AND id > 0;
ALTER TABLE cabins MODIFY created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE cabins
MODIFY created_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP;
-----------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE system_settings (
    setting_name VARCHAR(50) PRIMARY KEY,
    setting_value VARCHAR(255) NOT NULL
);

-- Insert some default settings
INSERT INTO system_settings (setting_name, setting_value) VALUES
('min_booking_days', '1'),
('max_booking_days', '30'),
('checkin_time', '14:00'),
('checkout_time', '11:00'),
('cancellation_policy', '48'),
('deposit_percentage', '20');
select * from system_settings;
-----------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS bookings;
-----------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    cabin_id INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    guests INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    payment_status VARCHAR(20) DEFAULT 'unpaid',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraints
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_cabin FOREIGN KEY (cabin_id) REFERENCES cabins(id) ON DELETE CASCADE
);
---------------------------------------------------------------------------------------------------
