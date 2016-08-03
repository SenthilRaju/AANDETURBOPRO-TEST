-- trigger for drop and insert a record in joMaster table, affected in search_text table. --

DELIMITER $$ 
DROP TRIGGER IF EXISTS `tr_job_ins_jobSearch`$$ 

CREATE TRIGGER `tr_job_ins_jobSearch` AFTER INSERT on `joMaster` 
	FOR EACH ROW 
	BEGIN 
		CALL sp_job_ins_searchindex(NEW.joMasterID, NEW.JobNumber, NEW.Description, NEW.cuAssignmentID0, NEW.BidDate); 
	END$$ 
DELIMITER ; 
