-- 社团管理系统建表 SQL
-- 数据库：club_manager（utf8mb4）

CREATE DATABASE IF NOT EXISTS club_manager DEFAULT CHARACTER SET utf8mb4;
USE club_manager;

-- 学生表（社团负责人）
CREATE TABLE IF NOT EXISTS student (
  id INT PRIMARY KEY AUTO_INCREMENT,
  stu_name VARCHAR(50) NOT NULL,
  grade VARCHAR(20),
  major VARCHAR(50),
  phone VARCHAR(20)
);

-- 社团表
CREATE TABLE IF NOT EXISTS club (
  id INT PRIMARY KEY AUTO_INCREMENT,
  club_name VARCHAR(100) NOT NULL,
  club_type VARCHAR(20),
  leader_id INT,
  create_time DATETIME
);

-- 社团成员表
CREATE TABLE IF NOT EXISTS club_member (
  id INT PRIMARY KEY AUTO_INCREMENT,
  club_id INT,
  stu_id INT,
  role VARCHAR(20)
);

-- 管理员表（登录用）
CREATE TABLE IF NOT EXISTS admin (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(50) NOT NULL
);

-- 默认账号：admin / 123456
INSERT INTO admin (username, password) VALUES ('admin', '123456');
