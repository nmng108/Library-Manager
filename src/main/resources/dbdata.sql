use `library-manager`;
-- Role
/*
-- Query: 
-- Date: 2023-09-07 04:35
*/
INSERT INTO `roles` (`id`,`name`) VALUES (1,'ROOT_ADMIN');
INSERT INTO `roles` (`id`,`name`) VALUES (2,'ADMIN');
INSERT INTO `roles` (`id`,`name`) VALUES (3,'LIBRARIAN');
INSERT INTO `roles` (`id`,`name`) VALUES (4,'PATRON');

-- User
/*
-- Query: 
-- Date: 2023-09-07 14:32
*/
INSERT INTO `users` (`id`,`identity_number`,`username`,`password`,`full_name`,`phone`,`email`,`address`) VALUES (1,'101029','admin','123','nguyen nam','010283vkei','dnj@g.vn','fij jid');
INSERT INTO `users` (`id`,`identity_number`,`username`,`password`,`full_name`,`phone`,`email`,`address`) VALUES (2,'425266','user01','098765','kjsdjl','wfiojsa','',NULL);
INSERT INTO `users` (`id`,`identity_number`,`username`,`password`,`full_name`,`phone`,`email`,`address`) VALUES (3,'t4g3br','nmng','nhn','sadfjkl;','sajf;','',NULL);

-- User's role
/*
-- Query: 
-- Date: 2023-09-08 11:12
*/
INSERT INTO `user_roles` (`id`,`user_id`,`role_id`) VALUES (1,1,1);
INSERT INTO `user_roles` (`id`,`user_id`,`role_id`) VALUES (2,2,3);
INSERT INTO `user_roles` (`id`,`user_id`,`role_id`) VALUES (3,3,4);

-- Category
/*
-- Query: 
-- Date: 2023-09-07 04:36
*/
INSERT INTO `categories` (`id`,`name`) VALUES (1,'Science');
INSERT INTO `categories` (`id`,`name`) VALUES (2,'Comic');
INSERT INTO `categories` (`id`,`name`) VALUES (3,'Mathematics');
INSERT INTO `categories` (`id`,`name`) VALUES (4,'Physic');
INSERT INTO `categories` (`id`,`name`) VALUES (5,'Novel');
INSERT INTO `categories` (`id`,`name`) VALUES (6,'IT');
INSERT INTO `categories` (`id`,`name`) VALUES (7,'Art');

-- Book
