DROP PROCEDURE project_table_swap_positions;

CREATE PROCEDURE project_table_swap_positions(IN fId int, IN sId int)
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

    SELECT * FROM project_table WHERE id IN (fId, sId);
END;

DROP PROCEDURE IF EXISTS project_table_issue_swap_positions;

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

    SELECT * FROM project_table_issue WHERE id IN (fId, sId);
END;

DROP PROCEDURE IF EXISTS project_table_issue_move_issue;

CREATE PROCEDURE project_table_issue_move_issue(IN fId bigint, IN sId bigint)
BEGIN
    DECLARE table_id_shared bigint;
    DECLARE f_pos int;
    DECLARE s_pos int;

    SELECT table_id, position FROM project_table_issue WHERE id = fId FOR UPDATE INTO table_id_shared, f_pos;

    SELECT position FROM project_table_issue WHERE id = sId FOR UPDATE INTO s_pos;

    UPDATE project_table_issue SET position = -10 WHERE id = fId;

    IF s_pos > f_pos THEN
        UPDATE project_table_issue SET position = position - 1
        WHERE table_id = table_id_shared AND position
            BETWEEN f_pos AND s_pos
        ORDER BY position;
    ELSE
        UPDATE project_table_issue SET position = position + 1
        WHERE table_id = table_id_shared AND position
            BETWEEN s_pos AND f_pos
        ORDER BY position DESC;
    END IF;

    UPDATE project_table_issue SET position = s_pos
    WHERE id = fId;

    SELECT * FROM project_table_issue WHERE table_id = table_id_shared;
END;