CREATE TABLE `user` (
  `username` varchar(255) UNIQUE PRIMARY KEY NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL,
  `enabled` boolean NOT NULL DEFAULT true
);

CREATE TABLE `user_authority` (
  `user` varchar(255) NOT NULL,
  `project_id` bigint NOT NULL,
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`user`, `project_id`, `authority`)
);

CREATE TABLE `user_authority_types` (
  `authority` varchar(255) PRIMARY KEY NOT NULL
);

CREATE TABLE `project` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `owner` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `description` text,
  `created_at` timestamp NOT NULL
);

CREATE TABLE `project_table` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `title` varchar(255) NOT NULL,
  `position` int NOT NULL COMMENT 'ascending positions where 0 is the one most to the left'
);

CREATE TABLE `project_table_issue` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `table_id` bigint NOT NULL,
  `reporter` varchar(255) NOT NULL,
  `parent_issue_id` bigint,
  `severity` int NOT NULL,
  `title` varchar(255) NOT NULL,
  `position` int NOT NULL COMMENT 'ascending positions where 0 is the one on the top',
  `description` text,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL
);

CREATE TABLE `project_label` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `label` varchar(255) NOT NULL COMMENT 'Mutliple labels like API-ISSUE, UI, PERFORMANCE, etc'
);

CREATE TABLE `project_table_issue_label` (
  `item_id` bigint NOT NULL,
  `label_id` bigint NOT NULL,
  PRIMARY KEY (`item_id`, `label_id`)
);

CREATE TABLE `project_table_issue_comment` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `user` varchar(255) NOT NULL,
  `issue_id` bigint NOT NULL,
  `message` text NOT NULL,
  `created_at` timestamp NOT NULL,
  `edited_at` timestamp
);

CREATE UNIQUE INDEX `project_table_index_0` ON `project_table` (`project_id`, `position`);

CREATE UNIQUE INDEX `project_table_index_1` ON `project_table` (`project_id`, `title`);

CREATE UNIQUE INDEX `project_table_issue_index_2` ON `project_table_issue` (`table_id`, `position`);

CREATE UNIQUE INDEX `project_label_index_3` ON `project_label` (`project_id`, `label`);

ALTER TABLE `user_authority` ADD FOREIGN KEY (`user`) REFERENCES `user` (`username`);

ALTER TABLE `user_authority` ADD FOREIGN KEY (`project_id`) REFERENCES `project` (`id`);

ALTER TABLE `user_authority` ADD FOREIGN KEY (`authority`) REFERENCES `user_authority_types` (`authority`);

ALTER TABLE `project_table_issue_label` ADD FOREIGN KEY (`item_id`) REFERENCES `project_table_issue` (`id`);

ALTER TABLE `project_table_issue_label` ADD FOREIGN KEY (`label_id`) REFERENCES `project_label` (`id`);

ALTER TABLE `project` ADD FOREIGN KEY (`owner`) REFERENCES `user` (`username`);

ALTER TABLE `project_table` ADD FOREIGN KEY (`project_id`) REFERENCES `project` (`id`);

ALTER TABLE `project_table_issue` ADD FOREIGN KEY (`table_id`) REFERENCES `project_table` (`id`);

ALTER TABLE `project_table_issue` ADD FOREIGN KEY (`reporter`) REFERENCES `user` (`username`);

ALTER TABLE `project_table_issue` ADD FOREIGN KEY (`parent_issue_id`) REFERENCES `project_table_issue` (`id`);

ALTER TABLE `project_table_issue_comment` ADD FOREIGN KEY (`user`) REFERENCES `user` (`username`);

ALTER TABLE `project_table_issue_comment` ADD FOREIGN KEY (`issue_id`) REFERENCES `project_table_issue` (`id`);