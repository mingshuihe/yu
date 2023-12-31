CREATE DATABASE yudb;

drop table if exists halo_blog;
CREATE TABLE halo_blog(
  id BIGINT(11) NOT NULL AUTO_INCREMENT,
  title VARCHAR(100) DEFAULT NULL,
  slug VARCHAR(100) DEFAULT NULL,
  cover VARCHAR(500) DEFAULT NULL,
  NAME VARCHAR(100) DEFAULT NULL,
  content TEXT DEFAULT NULL,
  excerpt VARCHAR(300) DEFAULT NULL,
  tag VARCHAR(100) DEFAULT NULL,
  page_url VARCHAR(500) DEFAULT NULL,
  check_sum VARCHAR(100) DEFAULT NULL,
  create_status VARCHAR(10) DEFAULT NULL,
  publish_status VARCHAR(10) DEFAULT NULL,
  has_error VARCHAR(10) DEFAULT NULL,
  error_msg TEXT DEFAULT NULL,
  create_date DATETIME DEFAULT NOW(),
  PRIMARY KEY (id)
)
