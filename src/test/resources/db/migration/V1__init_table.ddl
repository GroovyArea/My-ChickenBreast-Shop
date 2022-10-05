drop table if exists `user`;
drop table if exists `product`;
drop table if exists `amount`;
drop table if exists CARDINFO;
drop table if exists `order`;
drop table if exists category;
drop table if exists ordered_product;
DROP TABLE IF EXISTS payment;

CREATE TABLE user (
                                         `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                         `login_id` VARCHAR(45) NOT NULL,
                                         `password` VARCHAR(200) NOT NULL,
                                         `salt` VARCHAR(200) NOT NULL,
                                         `name` VARCHAR(30) NOT NULL,
                                         `email` VARCHAR(45) NOT NULL,
                                         `phone` VARCHAR(45) NOT NULL,
                                         `address` VARCHAR(50) NOT NULL,
                                         `grade` CHAR(10) NOT NULL,
                                         `created_at` DATETIME NOT NULL,
                                         `updated_at` DATETIME NULL,
                                         `deleted_at` DATETIME NULL,
                                         PRIMARY KEY (`id`),
                                         INDEX `nix-user-login_id` (`login_id` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '유저 테이블';

CREATE TABLE product (
                                            `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                            `name` VARCHAR(50) NOT NULL,
                                            `price` INT UNSIGNED NOT NULL,
                                            `quantity` INT UNSIGNED NOT NULL,
                                            `content` VARCHAR(4000) NULL,
                                            `image` VARCHAR(100) NULL,
                                            `created_at` DATETIME NOT NULL,
                                            `updated_at` DATETIME NULL,
                                            `deleted_at` DATETIME NULL,
                                            `status` CHAR(10) NOT NULL,
                                            PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '상품 테이블';

CREATE TABLE category (
                                             `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                             `name` VARCHAR(50) NOT NULL,
                                             `created_at` DATETIME NOT NULL,
                                             `updated_at` DATETIME NULL,
                                             `deleted_at` DATETIME NULL,
                                             PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '상품 카테고리 테이블';

CREATE TABLE `order` (
                                          `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                          `count` INT UNSIGNED NOT NULL,
                                          `order_price` BIGINT NOT NULL,
                                          `status` CHAR(10) NOT NULL,
                                          `created_at` DATETIME NOT NULL,
                                          `updated_at` DATETIME NULL,
                                          `deleted_at` DATETIME NULL,
                                          PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '주문 테이블';

CREATE TABLE ordered_product (
                                                    `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                                    `count` INT UNSIGNED NOT NULL,
                                                    `order_price` BIGINT UNSIGNED NOT NULL,
                                                    `product_id` BIGINT UNSIGNED NOT NULL,
                                                    `product_name` VARCHAR(50) NOT NULL,
                                                    `product_image` VARCHAR(100) NULL,
                                                    `product_content` VARCHAR(4000) NULL,
                                                    `created_at` DATETIME NOT NULL,
                                                    `updated_at` DATETIME NULL,
                                                    `deleted_at` DATETIME NULL,
                                                    PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '주문상품 테이블';

CREATE TABLE payment (
                                            `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                            `count` INT UNSIGNED NOT NULL,
                                            `total_price` BIGINT UNSIGNED NOT NULL,
                                            `key` VARCHAR(100) NOT NULL,
                                            `status` CHAR(10) NOT NULL,
                                            `created_at` DATETIME NOT NULL,
                                            `updated_at` DATETIME NULL,
                                            `deleted_at` DATETIME NULL,
                                            PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '결제 정보 테이블';

CREATE TABLE cardinfo (
                                             `id` BIGINT UNSIGNED NOT NULL auto_increment,
                                             `bin` VARCHAR(50) NOT NULL,
                                             `card_type` VARCHAR(20) NOT NULL,
                                             `install_month` VARCHAR(20) NOT NULL,
                                             `interest_free_install` VARCHAR(5) NOT NULL,
                                             `created_at` DATETIME NOT NULL,
                                             `updated_at` DATETIME NULL,
                                             `deleted_at` DATETIME NULL,
                                             PRIMARY KEY (`id`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_unicode_ci
    COMMENT = '카드 정보 테이블';
