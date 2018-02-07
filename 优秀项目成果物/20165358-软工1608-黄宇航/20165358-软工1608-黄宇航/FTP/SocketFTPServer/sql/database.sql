CREATE DATABASE socketsql;
USE socketsql;

CREATE TABLE user (
  id           BIGINT      NOT NULL AUTO_INCREMENT
  COMMENT '动态的id',
  username        VARCHAR(16) NOT NULL
  COMMENT '用户名',
  password     VARCHAR(16) NOT NULL
  COMMENT '密码',
  email         VARCHAR(30) NOT NULL
  COMMENT '邮箱',
  PRIMARY KEY (id),
  INDEX (username),
  INDEX (password)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE file (
  id         BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT '动态的id',
  filename   VARCHAR(30)  NOT NULL
  COMMENT '文件名',
  path   VARCHAR(200)  NOT NULL
  COMMENT '路径',
  PRIMARY KEY (id),
  INDEX (filename),
  UNIQUE (path)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
