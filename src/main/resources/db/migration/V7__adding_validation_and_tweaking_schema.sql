ALTER TABLE `project` ADD CONSTRAINT `title_not_empty_project` CHECK(TRIM(title) <> '');

ALTER TABLE `project` ADD CONSTRAINT `description_not_blank_project` CHECK(description IS NULL OR TRIM(description) <> '');

ALTER TABLE `project_table` ADD CONSTRAINT `title_not_empty_project_table` CHECK(TRIM(title) <> '');

ALTER TABLE `project_table_issue` MODIFY COLUMN `severity` TINYINT UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE `project_table_issue` ADD CONSTRAINT `severity_limit_project_table_issue` CHECK(severity <= 5);

ALTER TABLE `project_table_issue` ADD CONSTRAINT `title_not_empty_project_table_issue` CHECK(TRIM(title) <> '');

ALTER TABLE `project_table_issue` ADD CONSTRAINT `description_not_blank_project_table_issue` CHECK(description IS NULL OR TRIM(description) <> '');

ALTER TABLE `project_table_issue_comment` ADD CONSTRAINT `message_not_empty_project_table_issue_comment` CHECK(TRIM(message) <> '');

CREATE TABLE `project_label_types` (
    label varchar(255) UNIQUE PRIMARY KEY NOT NULL
);

ALTER TABLE `project_label_types` ADD CONSTRAINT `label_not_empty_project_label_types` CHECK(TRIM(label) <> '');

ALTER TABLE `project_label` ADD CONSTRAINT `label_label_types_fk`
    FOREIGN KEY (`label`) REFERENCES `project_label_types` (`label`)
    ON UPDATE RESTRICT ON DELETE CASCADE;

ALTER TABLE `user_authority_types` ADD CONSTRAINT `authority_not_empty_user_authority_types` CHECK(TRIM(authority) <> '');