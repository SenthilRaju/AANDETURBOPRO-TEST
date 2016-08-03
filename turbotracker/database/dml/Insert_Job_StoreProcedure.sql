-- Add 'BidDate Column in 'search_index' table.

ALTER TABLE `search_index` ADD COLUMN `bidDate` DATETIME  DEFAULT NULL AFTER `pk_fields`;

-- storeprocedure with 'BidDate' for while drop and create a record in joMaster, avaliable in search_text table  --
DELIMITER $$
DROP PROCEDURE IF EXISTS `sp_job_ins_searchindex`$$

CREATE PROCEDURE `sp_job_ins_searchindex`(IN joMasterID_in INT, IN JobNumber_in VARCHAR(20), IN description_in VARCHAR(75), IN cuAssignmentID0_in INT, IN bidDate_in DATETIME)
     BEGIN  
        IF ( cuAssignmentID0_in IS NULL)
        THEN 
            INSERT INTO search_index ( `pk_fields`,  `searchText`,  `entity`, `resultedTableName`, `bidDate` )  
            SELECT joMasterID_in, CONCAT(JobNumber_in,'|',description_in) AS searchText, 'Job' as entity ,'joMaster' as resultedTableName, bidDate_in AS bidDate;
        ELSE 
            INSERT INTO search_index ( `pk_fields`,  `searchText`,  `entity`, `resultedTableName`, `bidDate` )  
            SELECT joMasterID_in, CONCAT(JobNumber_in,'|',description_in,'|',tsUserLogin.FullName) AS searchText,
            'Job' as entity ,'joMaster' as resultedTableName, bidDate_in AS bidDate FROM tsUserLogin 
              WHERE UserLoginID = (select cuAssignmentID0 from joMaster Where joMasterID = joMasterID_in);
        END IF;
     END $$
DELIMITER ;