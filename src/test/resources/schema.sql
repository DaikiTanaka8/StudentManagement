CREATE TABLE IF NOT EXISTS students (
  student_id VARCHAR(36) NOT NULL,
  name VARCHAR(100) NOT NULL,
  furigana VARCHAR(100) NOT NULL,
  nickname VARCHAR(100) DEFAULT NULL,
  email VARCHAR(100) NOT NULL,
  city VARCHAR(100) DEFAULT NULL,
  age INT DEFAULT NULL,
  gender VARCHAR(100) DEFAULT NULL,
  remark VARCHAR(255) DEFAULT NULL,
  is_deleted tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (student_id)
);

CREATE TABLE IF NOT EXISTS students_courses (
  course_id VARCHAR(36) NOT NULL,
  student_id VARCHAR(36) NOT NULL,
  course_name VARCHAR(100) NOT NULL,
  start_date DATE DEFAULT NULL,
  end_date DATE DEFAULT NULL,
  PRIMARY KEY (course_id)
);