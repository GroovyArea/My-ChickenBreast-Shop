ALTER TABLE amount
    DROP INDEX `idx_tid`;

ALTER TABLE `chickenbreast`.`order`
    DROP COLUMN `user_id`,
    DROP INDEX `idx_userid` ;