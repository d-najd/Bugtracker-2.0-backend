ALTER TABLE project_issue_label_type DROP FOREIGN KEY project_issue_label_type_ibfk_1;

ALTER TABLE project_issue_label_type ADD FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE;

ALTER TABLE project_issue_label DROP FOREIGN KEY project_issue_label_ibfk_1;

ALTER TABLE project_issue_label DROP FOREIGN KEY project_issue_label_ibfk_2;

ALTER TABLE project_issue_label ADD FOREIGN KEY (label) REFERENCES project_issue_label_type(label) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE project_issue_label ADD FOREIGN KEY (issue_id) REFERENCES project_table_issue(id) ON DELETE CASCADE;

ALTER TABLE project_table_issue
    DROP FOREIGN KEY project_table_issue_ibfk_4;

ALTER TABLE project_table_issue
    DROP FOREIGN KEY project_table_issue_ibfk_3;

ALTER TABLE project_table_issue
    DROP FOREIGN KEY project_table_issue_ibfk_2;

ALTER TABLE project_table_issue
    ADD FOREIGN KEY (parent_issue_id) REFERENCES project_table_issue(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE project_table_issue
    ADD FOREIGN KEY (table_id) REFERENCES project_table(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE project_table_issue
    ADD FOREIGN KEY (reporter) REFERENCES user(username) ON DELETE CASCADE;