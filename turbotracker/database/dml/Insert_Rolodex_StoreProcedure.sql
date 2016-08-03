-- storeprocedure for while drop and create a record in rxMaster, avaliable in search_text table  --
DELIMITER $$
DROP PROCEDURE IF EXISTS `sp_rolodex_ins_searchindex`$$

CREATE PROCEDURE `sp_rolodex_ins_searchindex`(IN rxMasterID_in INT, IN Name_in VARCHAR(40), IN SearchName_in VARCHAR(10), IN Phone1_in VARCHAR(30))
BEGIN 
    DECLARE var_isCustomer, var_isEmployee, var_isVendor, var_isArchitect, var_isEngineer, var_isGc int;
    SET var_isCustomer = (select IsCustomer FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isEmployee = (select IsEmployee FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isVendor = (select IsVendor FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isArchitect = (select IsCategory1 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    SET var_isEngineer = (select IsCategory2 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    SET var_isGc = (select IsCategory3 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    
    IF ( var_isCustomer = 1 && Phone1_in IS NULL ) THEN
       INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
       SELECT 'customers' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, rxMasterID_in as pk_fields 
       FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isCustomer = 1 ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'customers' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     END IF; 
      
     IF ( var_isEmployee = 1 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'employeelist' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,  rxMasterID_in as pk_fields 
        FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isEmployee = 1 ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'employeelist' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     END IF;
     
     IF ( var_isVendor = 1 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'vendors' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isVendor = 1 ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'vendors' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
      END IF;   
      
      IF ( var_isArchitect = 1 && var_isEngineer = 0 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isArchitect = 1 && var_isEngineer = 0) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
      END IF; 

      IF ( var_isEngineer = 1 && var_isArchitect = 0 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'engineer' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isEngineer = 1 && var_isArchitect = 0) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'engineer' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
      END IF;

      IF ( var_isGc = 1 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'generalcontractors' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isGc = 1 ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'generalcontractors' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
      END IF;

	 IF ( var_isEngineer = 1 && var_isArchitect = 1 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect/engineer' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
     ELSEIF ( var_isEngineer = 1 && var_isArchitect = 1 ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect/engineer' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName, 
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
      END IF;
      
   END$$

DELIMITER ;