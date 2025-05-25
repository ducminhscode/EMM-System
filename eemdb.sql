
CREATE DATABASE IF NOT EXISTS eemdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE eemdb;

CREATE TABLE IF NOT EXISTS device_type (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS fatal_level (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS maintenance_type (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS repair_type (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user` (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  last_name VARCHAR(45) NOT NULL,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(100) NOT NULL,
  email VARCHAR(50) DEFAULT NULL,
  phone VARCHAR(10) DEFAULT NULL,
  user_role enum('ROLE_ADMIN','ROLE_TECHNICIAN','ROLE_EMPLOYEE') NOT NULL,
  avatar VARCHAR(255) DEFAULT 'https://res.cloudinary.com/dp9b0dkkt/image/upload/v1745512749/de995be2-6311-4125-9ac2-19e11fcaf801_jo8gcs.png',
  active TINYINT(1) DEFAULT 1,
  created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY (username),
  UNIQUE KEY (email),
  UNIQUE KEY (phone),
  CONSTRAINT user_chk_1 CHECK (email LIKE '%@%.%'),
  CONSTRAINT user_chk_2 CHECK (REGEXP_LIKE(phone, '^[0-9]{10}$'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS facility (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  address VARCHAR(255) NOT NULL,
  created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  user_id INT,
  PRIMARY KEY (id),
  UNIQUE KEY (name),
  UNIQUE KEY (address),
  KEY (user_id),
  CONSTRAINT facility_ibfk_1 FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS technician (
  id INT NOT NULL,
  specialization VARCHAR(50),
  facility_id INT NOT NULL,
  PRIMARY KEY (id),
  KEY fk_technician_facility1_idx (facility_id),
  CONSTRAINT fk_technician_user1 FOREIGN KEY (id) REFERENCES `user` (id) ON DELETE CASCADE,
  CONSTRAINT fk_technician_facility1 FOREIGN KEY (facility_id) REFERENCES facility (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS device (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  type_id INT NOT NULL,
  manufacturer VARCHAR(255) NOT NULL,
  purchase_date DATETIME NOT NULL,
  device_status enum('Hoạt động','Bảo trì','Sửa chữa') NOT NULL DEFAULT 'Hoạt động',
  facility_id INT NOT NULL,
  created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  user_id INT,
  PRIMARY KEY (id),
  KEY (type_id),
  KEY fk_device_facility1_idx (facility_id),
  KEY (user_id),
  CONSTRAINT device_ibfk_1 FOREIGN KEY (type_id) REFERENCES device_type (id) ON DELETE RESTRICT,
  CONSTRAINT device_ibfk_3 FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE SET NULL,
  CONSTRAINT fk_device_facility1 FOREIGN KEY (facility_id) REFERENCES facility (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS problem (
  id INT NOT NULL AUTO_INCREMENT,
  happened_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  description VARCHAR(255) DEFAULT NULL,
  fatal_level_id INT NOT NULL DEFAULT 5,
  problem_status enum('Chưa xác nhận','Xác nhận','Đang sửa chữa','Đã sửa chữa') NOT NULL DEFAULT 'Chưa xác nhận',
  user_id INT,
  device_id INT NOT NULL,
  created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY fk_problem_user1_idx (user_id),
  KEY fk_problem_device1_idx (device_id),
  KEY (fatal_level_id),
  CONSTRAINT fk_problem_device1 FOREIGN KEY (device_id) REFERENCES device (id) ON DELETE CASCADE,
  CONSTRAINT fk_problem_user1 FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE SET NULL,
  CONSTRAINT problem_ibfk_1 FOREIGN KEY (fatal_level_id) REFERENCES fatal_level (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS repair_history (
  id INT NOT NULL AUTO_INCREMENT,
  problem_id INT NOT NULL,
  technician_id INT,
  type_id INT,
  expense DECIMAL(10,2),
  description VARCHAR(255),
  start_date DATETIME,
  end_date DATETIME,
  PRIMARY KEY (id),
  KEY idx_start_date (start_date),
  KEY idx_end_date (end_date),
  KEY (problem_id),
  KEY (technician_id),
  KEY (type_id),
  CONSTRAINT repair_ibfk_1 FOREIGN KEY (problem_id) REFERENCES problem (id) ON DELETE CASCADE,
  CONSTRAINT repair_ibfk_2 FOREIGN KEY (technician_id) REFERENCES technician (id) ON DELETE SET NULL,
  CONSTRAINT repair_history_ibfk_1 FOREIGN KEY (type_id) REFERENCES repair_type (id) ON DELETE RESTRICT,
  CONSTRAINT chk_positive_expense CHECK (expense >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS maintenance_schedule (
  id INT NOT NULL AUTO_INCREMENT,
  type_id INT NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  title VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  frequency VARCHAR(50) NOT NULL,
  expense_first DECIMAL(10,2),
  expense_last DECIMAL(10,2),
  maintenance_status enum('Chưa bảo trì','Đã bảo trì','Quá hạn bảo trì','Đang bảo trì','Ngừng bảo trì') NOT NULL,
  maintenance_date DATETIME,
  device_id INT NOT NULL,
  user_id INT,
  PRIMARY KEY (id),
  KEY idx_dates (start_date, end_date),
  KEY fk_maintenance_schedule_user1_idx (user_id),
  KEY (device_id),
  KEY (type_id),
  CONSTRAINT fk_maintenance_schedule_device1 FOREIGN KEY (device_id) REFERENCES `device`(id) ON DELETE CASCADE,
  CONSTRAINT fk_maintenance_schedule_user1 FOREIGN KEY (user_id) REFERENCES `user` (id) ON DELETE SET NULL,
  CONSTRAINT maintenance_schedule_ibfk_1 FOREIGN KEY (type_id) REFERENCES maintenance_type (id) ON DELETE RESTRICT,
  CONSTRAINT chk_valid_dates CHECK (start_date <= end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS maintenance_assignment (
  id INT NOT NULL AUTO_INCREMENT,
  technician_id INT,
  maintenance_schedule_id INT NOT NULL,
  is_cap TINYINT(1) DEFAULT 0,
  is_notify TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id),
  KEY (technician_id),
  KEY (maintenance_schedule_id),
  CONSTRAINT maintenance_assignment_ibfk_1 FOREIGN KEY (technician_id) REFERENCES technician (id) ON DELETE SET NULL,
  CONSTRAINT maintenance_assignment_ibfk_2 FOREIGN KEY (maintenance_schedule_id) REFERENCES maintenance_schedule (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO device_type (name) VALUES 
('Điện tử'),
('Cơ khí'),
('Gia dụng'),
('Viễn thông');

INSERT INTO fatal_level (name) VALUES 
('Thấp'),
('Trung bình'),
('Cao');

INSERT INTO maintenance_type (name) VALUES 
('Định kỳ'),
('Cải tiến'),
('Khẩn cấp'),
('Phục hồi');

INSERT INTO repair_type (name) VALUES 
('Khẩn cấp'),
('Thay thế'),
('Tình trạng');
