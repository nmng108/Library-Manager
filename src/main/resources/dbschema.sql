-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema library-manager
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `library-manager` ;

-- -----------------------------------------------------
-- Schema library-manager
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library-manager` DEFAULT CHARACTER SET utf8mb4 ;
USE `library-manager` ;

-- -----------------------------------------------------
-- Table `library-manager`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library-manager`.`users` ;

CREATE TABLE IF NOT EXISTS `library-manager`.`users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `identity_number` VARCHAR(20) NOT NULL,
  `username` VARCHAR(16) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `full_name` VARCHAR(70) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `email` VARCHAR(90) NULL,
  `address` VARCHAR(120) NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `identity_number` (`identity_number` ASC) INVISIBLE,
  INDEX `username` USING BTREE (`username`) VISIBLE,
  UNIQUE INDEX `identity_number_UNIQUE` (`identity_number` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library-manager`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library-manager`.`categories` ;

CREATE TABLE IF NOT EXISTS `library-manager`.`categories` (
  `id` SMALLINT(3) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library-manager`.`roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library-manager`.`roles` ;

CREATE TABLE IF NOT EXISTS `library-manager`.`roles` (
  `id` TINYINT(1) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10) NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library-manager`.`user_roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library-manager`.`user_roles` ;

CREATE TABLE IF NOT EXISTS `library-manager`.`user_roles` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `role_id` TINYINT(1) UNSIGNED NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `fk_roles_idx` (`role_id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_roles_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `library-manager`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_roles`
    FOREIGN KEY (`role_id`)
    REFERENCES `library-manager`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library-manager`.`books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `library-manager`.`books` ;

CREATE TABLE IF NOT EXISTS `library-manager`.`books` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `book_number` TINYINT(3) UNSIGNED NULL,
  `authors` VARCHAR(100) NOT NULL,
  `edition` TINYINT(3) UNSIGNED NOT NULL DEFAULT 1,
  `publisher` VARCHAR(60) NOT NULL,
  `category_id` SMALLINT(3) UNSIGNED NOT NULL,
  `quantity` MEDIUMINT(6) UNSIGNED NOT NULL,
  `borrow_fee` DOUBLE UNSIGNED NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_books_category1_idx` (`category_id` ASC) VISIBLE,
  CONSTRAINT `fk_books_category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `library-manager`.`categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
