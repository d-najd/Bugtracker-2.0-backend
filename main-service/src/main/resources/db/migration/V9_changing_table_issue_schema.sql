ALTER TABLE project_table_issue DROP FOREIGN KEY project_table_issue_ibfk_3;

ALTER TABLE project_table_issue DROP COLUMN parent_issue_id;

CREATE TABLE project_table_issue_parent_issue(
    parent_id bigint NOT NULL,
    child_id bigint NOT NULL,
    PRIMARY KEY (parent_id, child_id),
    FOREIGN KEY (parent_id) REFERENCES project_table_issue(id),
    FOREIGN KEY (child_id) REFERENCES project_table_issue(id)
);
