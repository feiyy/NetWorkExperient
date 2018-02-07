CREATE DATABASE socketftpsql;
USE socketftpsql;

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
  userid     BIGINT       NOT NULL
  COMMENT '上传者id',
  filename   VARCHAR(30)  NOT NULL
  COMMENT '文件名',
  size       INT          NOT NULL
  COMMENT '文件大小(单位kb)',
  type    VARCHAR(15) NOT NULL
  COMMENT '类型',
  summary    VARCHAR(150) NOT NULL
  COMMENT '简介',
  uploadtime    TIMESTAMP            DEFAULT current_timestamp
  COMMENT '上传时间',
  PRIMARY KEY (id),
  INDEX (userid)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;