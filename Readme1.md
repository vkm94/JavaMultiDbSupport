DELIMITER //

CREATE TRIGGER after_user_insert
AFTER INSERT ON tblmusers
FOR EACH ROW
BEGIN
    -- This part "calls" the copy action automatically
    INSERT INTO replica_db.tblmusers (
        user_id, email, first_name, last_name, mobile, password, username
    ) 
    VALUES (
        NEW.user_id, NEW.email, NEW.first_name, NEW.last_name, NEW.mobile, NEW.password, NEW.username
    );
END; //

DELIMITER ;
//db SYNC
