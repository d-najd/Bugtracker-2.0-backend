DROP PROCEDURE IF EXISTS project_table_swap_positions;

CREATE PROCEDURE project_table_swap_positions(IN fId bigint, IN sId bigint)
BEGIN
    SELECT @fPos := position
    FROM project_table
    WHERE id = fId
        FOR UPDATE;

    SELECT @sPos := position
    FROM project_table
    WHERE id = sId
        FOR UPDATE;

    UPDATE project_table SET position = -1 WHERE id = fId;
    UPDATE project_table SET position = @fPos WHERE id = sId;
    UPDATE project_table SET position = @sPos WHERE id = fId;
END;

CREATE PROCEDURE project_table_issue_swap_positions(IN fId bigint, IN sId bigint)
BEGIN
    SELECT @fPos := position
    FROM project_table_issue
    WHERE id = fId
        FOR UPDATE;

    SELECT @sPos := position
    FROM project_table_issue
    WHERE id = sId
        FOR UPDATE;

    UPDATE project_table_issue SET position = -1 WHERE id = fId;
    UPDATE project_table_issue SET position = @fPos WHERE id = sId;
    UPDATE project_table_issue SET position = @sPos WHERE id = fId;
END;