create table users(username varchar_ignorecase(50) not null primary key,password varchar_ignorecase(500) not null,enabled boolean not null);
create table authorities (username varchar_ignorecase(50) not null,authority varchar_ignorecase(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

INSERT IGNORE INTO `users` VALUES ('user', '{noop}eazybankuser@123456', '1');
INSERT IGNORE INTO `authorities` VALUES ('user', 'read');

INSERT IGNORE INTO `users` VALUES ('admin','{bcrypt}$2a$12$1UN2OGBUcp4Opsfn.RJN5.IRgB5uqPC9tDoG.F/j3itwEqXu2uiPS','1');
INSERT IGNORE INTO `authorities` VALUES ('admin','admin');

CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT ,
  `email` varchar(45) NOT NULL ,
  `pwd` varchar(200) NOT NULL ,
  `role` varchar(45) NOT NULL ,
  PRIMARY KEY (`id`)
);

INSERT  INTO `customer` (`email`,`pwd`,`role`) VALUES ('mohammad@gmail.com', '{noop}eazybankuser@123456', 'read');
INSERT  INTO `customer` (`email`,`pwd`,`role`) VALUES ('admin@gmail.com', '{bcrypt}$2a$12$1UN2OGBUcp4Opsfn.RJN5.IRgB5uqPC9tDoG.F/j3itwEqXu2uiPS', 'admin');