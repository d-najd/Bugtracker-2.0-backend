INSERT INTO user_authority_types VALUES ("manage_users_auth");

INSERT INTO user_authority_types VALUES ("assign_roles_auth");

INSERT INTO user_authority_types VALUES ("view_auth");

INSERT INTO user_authority_types VALUES ("insert_auth");

INSERT INTO user_authority_types VALUES ("update_auth");

INSERT INTO user_authority_types VALUES ("delete_auth");

INSERT INTO user_authority_types VALUES ("comment_auth");

INSERT INTO user VALUES ("admin", "password", '2018-06-01 15:13:45', true);

INSERT INTO user VALUES ("user1", "password", '2018-06-01 15:13:45', true);

INSERT INTO user VALUES ("user2", "password", '2018-06-01 15:13:45', true);

INSERT INTO project VALUES (1, "user1", "Title", "example project", '2018-06-01 15:13:45');

INSERT INTO user_authority VALUES ("user2", 1, "view_auth");

INSERT INTO user_authority VALUES ("user2", 1, "update_auth");

INSERT INTO project_table VALUES (1, 1, "Table1", 0);

INSERT INTO project_table VALUES (2, 1, "Table2", 1);

INSERT INTO project_table VALUES (3, 1, "Table3", 2);