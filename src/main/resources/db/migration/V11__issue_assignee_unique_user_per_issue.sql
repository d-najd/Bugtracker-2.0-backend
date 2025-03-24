ALTER TABLE project_table_issue_assignee
    ADD UNIQUE (issue_id, assigned_username);