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
END;