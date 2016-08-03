-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `sp_job_upd_searchindex`$$

CREATE PROCEDURE `sp_job_upd_searchindex`(IN joMasterID_in INT, IN JobNumber_in VARCHAR(20), IN description_in VARCHAR(75), IN cuAssignmentID0_in INT, IN bidDate_in DATETIME)
BEGIN
    DECLARE var_search varchar(200);
    DECLARE var_search_text varchar(200);
    SET var_search = CONCAT(JobNumber_in,'|',description_in);
     SET var_search_text = CONCAT(JobNumber_in,'|',description_in,'|', (SELECT tsUserLogin.FullName FROM tsUserLogin
              WHERE UserLoginID = (select cuAssignmentID0 from joMaster Where joMasterID = joMasterID_in)));
    IF ( cuAssignmentID0_in IS NULL)
    THEN
        UPDATE search_index SET searchText = var_search, bidDate = bidDate_in WHERE pk_fields = joMasterID_in AND resultedTableName = 'joMaster' ;
    ELSE
        UPDATE search_index SET searchText = var_search_text, bidDate = bidDate_in WHERE pk_fields = joMasterID_in AND resultedTableName = 'joMaster' ;
    END IF;
   END $$
DELIMITER ;