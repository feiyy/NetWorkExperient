CREATE DATABASE rmisql;
USE rmisql;

CREATE TABLE user (
  id       BIGINT      NOT NULL AUTO_INCREMENT
  COMMENT '动态的id',
  username VARCHAR(16) NOT NULL
  COMMENT '用户名',
  password VARCHAR(16) NOT NULL
  COMMENT '密码',
  PRIMARY KEY (id),
  INDEX (username),
  INDEX (password)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE meeting (
  id        BIGINT      NOT NULL AUTO_INCREMENT
  COMMENT '动态的id',
  username  VARCHAR(16) NOT NULL
  COMMENT '用户名',
  othername VARCHAR(16) NOT NULL
  COMMENT '另一位用户',
  title     VARCHAR(16) NOT NULL
  COMMENT '标题',
  label     VARCHAR(16) NOT NULL
  COMMENT '标签',
  starttime LONG        NOT NULL
  COMMENT '开始时间',
  endtime LONG        NOT NULL
  COMMENT '结束时间',
  PRIMARY KEY (id),
  INDEX (username),
  INDEX (othername)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO user (username, password) VALUES ("2", "2");