-- -----------------------------------------------------
-- Data for table `library-manager`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`users` (`id`, `identity_number`, `username`, `password`, `full_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (1, '101029', 'admin00', '123', 'Tran Van A', '0987654321', 'adm@gmail.com.vn', 'Hà Nội', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`users` (`id`, `identity_number`, `username`, `password`, `full_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (2, '425266', 'lib001', '098765', 'Le Thi C', '0865732194', '', 'NB', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`users` (`id`, `identity_number`, `username`, `password`, `full_name`, `phone`, `email`, `address`, `create_time`, `update_time`) VALUES (3, 't4g3br', 'namng', 'nhn', 'Nguyen Nam', 'sajf;', '', 'Vĩnh Phúc', DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`categories`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (1, 'Science', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (2, 'Comic', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (3, 'Mathematics', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (4, 'Physics', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (5, 'Literature', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (6, 'IT', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`categories` (`id`, `name`, `create_time`, `update_time`) VALUES (7, 'Art', DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`roles` (`id`, `name`, `create_time`, `update_time`) VALUES (1, 'RootAdmin', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`roles` (`id`, `name`, `create_time`, `update_time`) VALUES (2, 'Admin', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`roles` (`id`, `name`, `create_time`, `update_time`) VALUES (3, 'Librarian', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`roles` (`id`, `name`, `create_time`, `update_time`) VALUES (4, 'Patron', DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`user_roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`user_roles` (`id`, `user_id`, `role_id`, `create_time`, `update_time`) VALUES (1, 1, 1, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`user_roles` (`id`, `user_id`, `role_id`, `create_time`, `update_time`) VALUES (2, 1, 3, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`user_roles` (`id`, `user_id`, `role_id`, `create_time`, `update_time`) VALUES (3, 2, 3, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`user_roles` (`id`, `user_id`, `role_id`, `create_time`, `update_time`) VALUES (4, 3, 4, DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`books`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`books` (`id`, `name`, `book_number`, `authors`, `edition`, `publisher`, `category_id`, `quantity`, `create_time`, `update_time`) VALUES (1, 'CS', NULL, 'Martin', 7, 'NXB GD', 6, 509, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`books` (`id`, `name`, `book_number`, `authors`, `edition`, `publisher`, `category_id`, `quantity`, `create_time`, `update_time`) VALUES (2, 'Design patterns', NULL, 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides, Grady Booch', 5, '', 6, 281, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`books` (`id`, `name`, `book_number`, `authors`, `edition`, `publisher`, `category_id`, `quantity`, `create_time`, `update_time`) VALUES (3, '100 Truyện cổ tích Việt Nam', NULL, 'Trần Trường Minh', 2, 'NXB Văn Học', 5, 230, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`books` (`id`, `name`, `book_number`, `authors`, `edition`, `publisher`, `category_id`, `quantity`, `create_time`, `update_time`) VALUES (4, 'Harry Potter Và Tên Tù Nhân Ngục Azkaban', 3, 'J. K.Rowling', 4, 'NXB Trẻ', 5, 116, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`books` (`id`, `name`, `book_number`, `authors`, `edition`, `publisher`, `category_id`, `quantity`, `create_time`, `update_time`) VALUES (5, 'Sách Vật lí đại cương', 2, 'Lương Duyên Bình, Dư Trí Công, Nguyễn Hữu Hồ', 1, 'NXB GD', 4, 360, DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`request_status`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`request_status` (`id`, `name`) VALUES (1, 'BORROWING');
INSERT INTO `library-manager`.`request_status` (`id`, `name`) VALUES (2, 'RETURNED');
INSERT INTO `library-manager`.`request_status` (`id`, `name`) VALUES (3, 'CANCELLED');
INSERT INTO `library-manager`.`request_status` (`id`, `name`) VALUES (4, 'EXPIRED');

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`requests`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`requests` (`id`, `user_id`, `status_id`, `fine`, `due_date`, `create_time`, `update_time`) VALUES (1, 3, 1, 0, '2023-09-12', '2023-08-16', '2023-08-16');
INSERT INTO `library-manager`.`requests` (`id`, `user_id`, `status_id`, `fine`, `due_date`, `create_time`, `update_time`) VALUES (2, 3, 2, 0, '2023-09-05', '2023-08-22', '2023-09-05');
INSERT INTO `library-manager`.`requests` (`id`, `user_id`, `status_id`, `fine`, `due_date`, `create_time`, `update_time`) VALUES (3, 3, 4, 0, '2023-09-06', '2023-08-30', '2023-09-09');

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`policies`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`policies` (`id`, `name`, `value`, `create_time`, `update_time`) VALUES (1, 'expired_request_fine', '1000', DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`policies` (`id`, `name`, `value`, `create_time`, `update_time`) VALUES (2, 'fine_count_interval', '15 SECOND', DEFAULT, DEFAULT);

COMMIT;


-- -----------------------------------------------------
-- Data for table `library-manager`.`request_details`
-- -----------------------------------------------------
START TRANSACTION;
USE `library-manager`;
INSERT INTO `library-manager`.`request_details` (`id`, `request_id`, `book_id`, `quantity`, `create_time`, `update_time`) VALUES (1, 2, 1, 3, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`request_details` (`id`, `request_id`, `book_id`, `quantity`, `create_time`, `update_time`) VALUES (2, 3, 4, 2, DEFAULT, DEFAULT);
INSERT INTO `library-manager`.`request_details` (`id`, `request_id`, `book_id`, `quantity`, `create_time`, `update_time`) VALUES (3, 1, 2, 1, DEFAULT, DEFAULT);

COMMIT;

