ALTER TABLE `project` ADD `iconUrl` VARCHAR(255) NOT NULL DEFAULT 'Nan' AFTER `description`;

ALTER TABLE `project` ALTER `iconUrl` DROP DEFAULT;