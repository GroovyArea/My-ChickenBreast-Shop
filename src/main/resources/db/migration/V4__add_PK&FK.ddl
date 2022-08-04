ALTER TABLE amount
    CHANGE COLUMN `tid` `amount_no` INT(20) NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`amount_no`);

ALTER TABLE cardinfo
    DROP COLUMN `tid`,
    ADD COLUMN `cardinfo_no` INT(20) NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (`cardinfo_no`),
    DROP INDEX `idx_tid`;

ALTER TABLE user
    CHANGE COLUMN `user_pw` `user_pw` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_salt` `user_salt` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_name` `user_name` VARCHAR(30) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_phone` `user_phone` VARCHAR(30) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_email` `user_email` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_main_address` `user_main_address` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_detail_address` `user_detail_address` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_zipcode` `user_zipcode` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `user_grade` `user_grade` VARCHAR(20) NOT NULL ,
    ADD INDEX `phone_index` (`user_phone` ASC) VISIBLE;

ALTER TABLE product
    CHANGE COLUMN `product_name` `product_name` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
    CHANGE COLUMN `product_category` `product_category` INT(1) NOT NULL ,
    CHANGE COLUMN `product_price` `product_price` INT(5) NOT NULL ,
    CHANGE COLUMN `product_stock` `product_stock` INT(5) NOT NULL ,
    CHANGE COLUMN `product_detail` `product_detail` VARCHAR(4000) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL ,
    CHANGE COLUMN `product_image` `product_image` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL ,
    CHANGE COLUMN `product_status` `product_status` VARCHAR(10) NOT NULL;