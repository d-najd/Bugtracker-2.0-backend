ALTER TABLE `project_table` ADD CONSTRAINT id_position_unique UNIQUE (id, position);

ALTER TABLE `project_table` MODIFY COLUMN `position` INT UNSIGNED NOT NULL COMMENT 'ascending positions where 0 is the one most to the left';