DROP TABLE project_table_issue_label;

DROP TABLE project_label;

DROP TABLE project_label_type;

CREATE TABLE project_issue_label_type(
    project_id bigint NOT NULL COMMENT 'Unique per project since when we select a label from existing we will need to fetch all labels unique to project, and this seems easier to scale en mass if needed',
    label varchar(255) NOT NULL UNIQUE COMMENT 'Multiple labels like API-ISSUE, UI, PERFORMANCE, etc',
    PRIMARY KEY (project_id, label),
    FOREIGN KEY (project_id) REFERENCES project_table(id)
);

CREATE TABLE project_issue_label(
    issue_id bigint,
    label varchar(255) NOT NULL COMMENT 'Multiple labels like API-ISSUE, UI, PERFORMANCE, etc',
    PRIMARY KEY (issue_id, label),
    FOREIGN KEY (label) REFERENCES project_issue_label_type(label),
    FOREIGN KEY (issue_id) REFERENCES project_table_issue(id)
);
