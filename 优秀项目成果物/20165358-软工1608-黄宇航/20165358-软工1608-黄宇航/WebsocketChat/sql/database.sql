CREATE DATABASE socketsql;
USE socketsql;

CREATE TABLE user (
  id        BIGINT      NOT NULL AUTO_INCREMENT
  COMMENT '动态的id',
  username  VARCHAR(16) NOT NULL
  COMMENT '用户名',
  password  VARCHAR(16) NOT NULL
  COMMENT '密码',
  email     VARCHAR(30) NOT NULL
  COMMENT '邮箱',
  authority TINYINT              DEFAULT 0
  COMMENT '权限',
  number    INT                  DEFAULT 0
  COMMENT '工号',
  PRIMARY KEY (id),
  INDEX (username),
  INDEX (password)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO user (username, password, email) VALUE ("3", "3", "1");
INSERT INTO user (username, password, email) VALUE ("1", "1", "1");
INSERT INTO user (username, password, email) VALUE ("2", "2", "1");
INSERT INTO user (username, password, email,authority,number) VALUE ("k1", "1", "1",1,1);
INSERT INTO user (username, password, email,authority,number) VALUE ("k2", "1", "1",1,2);