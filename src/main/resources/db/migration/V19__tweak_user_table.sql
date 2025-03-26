ALTER TABLE user DROP COLUMN password;

ALTER TABLE user DROP COLUMN enabled;

RENAME TABLE user_authority TO project_user_authority;

ALTER TABLE project_user_authority RENAME COLUMN user TO username;