-- trigger for drop and update a record in joMaster table, affected in search_text table. --

DELIMITER $$ 
DROP TRIGGER IF EXISTS `tr_job_upd_jobSearch`$$

CREATE TRIGGER `tr_job_upd_jobSearch` AFTER UPDATE on `joMaster` 
	FOR EACH ROW 
	BEGIN 
		CALL sp_job_upd_searchindex(NEW.joMasterID, NEW.JobNumber, NEW.Description, NEW.cuAssignmentID0, NEW.BidDate); 
	END$$ 
DELIMITER ; 