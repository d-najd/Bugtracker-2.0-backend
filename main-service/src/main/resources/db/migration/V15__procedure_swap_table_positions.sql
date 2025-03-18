CREATE PROCEDURE project_table_swap_positions(IN fId int, IN sId int)
BEGIN
    SELECT @temp0 := position
    FROM project_table
    WHERE id = fId
        FOR UPDATE;
    SELECT @temp1 := position
    FROM project_table
    WHERE id = sId
        FOR UPDATE;

    UPDATE project_table SET position = -@temp0 WHERE id = sId;
    UPDATE project_table SET position = @temp1 WHERE id = fId;
    UPDATE project_table SET position = @temp0 WHERE id = sId;
END
