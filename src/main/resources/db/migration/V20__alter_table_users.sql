ALTER TABLE user ADD gmail varchar(255) NOT NULL DEFAULT 'temp@gmail.com';

UPDATE user SET gmail = 'user1@gmail.com' WHERE username = 'user1';

UPDATE user SET gmail = 'user2@gmail.com' WHERE username = 'user2';

# Dropping default
ALTER TABLE user MODIFY COLUMN gmail varchar(255) NOT NULL;

ALTER TABLE user ADD CONSTRAINT project_table_gmail_unique UNIQUE (gmail);
