DROP INDEX `id_position_unique` ON `project_table`;

ALTER TABLE `project_table` ADD CONSTRAINT project_id_position_unique UNIQUE (project_id, position);

ALTER TABLE `project_table_issue` ADD CONSTRAINT table_id_position_unique UNIQUE (table_id, position);