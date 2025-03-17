ALTER TABLE project_table DROP CONSTRAINT project_id;

# ALTER TABLE project_table ADD CONSTRAINT project_table_unique_position UNIQUE (project_id, position) DEFERRABLE INITIALLY DEFERRED