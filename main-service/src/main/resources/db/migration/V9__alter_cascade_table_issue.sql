ALTER TABLE project_table_issue
    DROP FOREIGN KEY project_table_issue_ibfk_3;

ALTER TABLE project_table_issue
    DROP FOREIGN KEY project_table_issue_ibfk_1;

ALTER TABLE project_table_issue
    ADD FOREIGN KEY (parent_issue_id) REFERENCES project_table_issue(id);

ALTER TABLE project_table_issue
    ADD FOREIGN KEY (table_id) REFERENCES project_table(id);