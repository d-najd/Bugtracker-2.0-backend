INSERT IGNORE INTO `user` VALUES ('user1', 'password', now(), true);
INSERT IGNORE INTO `user` VALUES ('user2', 'password', now(), true);

ALTER TABLE `project` ADD `owner` VARCHAR(255) NOT NULL DEFAULT 'user1' AFTER `title`,
    ADD CONSTRAINT `owner_username` FOREIGN KEY (`owner`) REFERENCES `user` (`username`)
    ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `project` ALTER `owner` DROP DEFAULT;