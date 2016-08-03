-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$
DROP PROCEDURE IF EXISTS `sp_rolodex_upd_searchindex`$$

CREATE PROCEDURE `sp_rolodex_upd_searchindex`(IN rxMasterID_in INT, IN Name_in VARCHAR(40), IN SearchName_in VARCHAR(10), IN Phone1_in VARCHAR(30))
BEGIN
    DECLARE var_search_text , var_search_text_ph VARCHAR(100);
    DECLARE var_isCustomer, var_isEmployee, var_isVendor, var_isArchitect, var_isEngineer, var_isGc INT;
    DECLARE var_pk_fields INT;
    DECLARE var_entity_cust, var_entity_Emp, var_entity_Vend, var_entity_Arch, var_entity_Eng, var_entity_Gc, var_entity_Eng_Arc VARCHAR(50);
    DECLARE Search_id_Cust,Search_id_Emp,Search_id_Ven,Search_id_Arc,Search_id_Eng,Search_id_Gc,Search_id_EA INT;
    SET var_isCustomer = (select IsCustomer FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isEmployee = (select IsEmployee FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isVendor = (select IsVendor FROM rxMaster WHERE rxMasterID = rxMasterID_in);
    SET var_isArchitect = (select IsCategory1 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    SET var_isEngineer = (select IsCategory2 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    SET var_isGc = (select IsCategory3 FROM rxMaster WHERE rxMAsterID = rxMasterID_in);
    SET var_entity_cust=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='customers');
    SET var_entity_Emp=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='employeelist');
    SET var_entity_Vend=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='vendors');
    SET var_entity_Arch=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='architect');
    SET var_entity_Eng=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='engineer');
    SET var_entity_Gc=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='generalcontractors');
    SET var_entity_Eng_Arc=(SELECT entity FROM search_index  WHERE pk_fields = rxMasterID_in AND entity='architect/engineer');
    SET var_pk_fields=rxMasterID_in;
    SET var_search_text = CONCAT(Name_in,'|',UPPER(Name_in)) ;
    SET var_search_text_ph = CONCAT(Name_in,'|',UPPER(Name_in),'|',Phone1_in);

   IF(Phone1_in IS NULL )THEN
      UPDATE search_index SET searchText = var_search_text WHERE pk_fields = rxMasterID_in AND resultedTableName ='rxMaster';
    ELSEIF (Phone1_in IS NOT NULL ) THEN
      UPDATE search_index SET searchText = var_search_text_ph WHERE pk_fields = rxMasterID_in AND resultedTableName ='rxMaster';
    END IF;

   IF ( var_isCustomer = 0 && var_entity_cust='customers' && var_pk_fields= rxMasterID_in) THEN
      SET Search_id_Cust =  (SELECT searchId FROM search_index WHERE entity='customers' AND pk_fields= rxMasterID_in);
      DELETE FROM  search_index WHERE searchId = Search_id_Cust;
   ELSEIF ( var_isCustomer = 1 && var_entity_cust IS NULL  && Phone1_in IS NULL ) THEN
       INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
       SELECT 'customers' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName, rxMasterID as pk_fields
       FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isCustomer = 1 && var_entity_cust IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'customers' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isEmployee  = 0 && var_entity_Emp='employeelist' && var_pk_fields= rxMasterID_in) THEN
         SET Search_id_Emp = (SELECT searchId FROM search_index WHERE entity='employeelist' AND pk_fields= rxMasterID_in);
         DELETE FROM  search_index WHERE searchId = Search_id_Emp;
   ELSEIF ( var_isEmployee = 1 && var_entity_Emp IS NULL && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'employeelist' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,  rxMasterID_in as pk_fields
        FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isEmployee = 1 && var_entity_Emp IS NULL) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'employeelist' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isVendor = 0 && var_entity_Vend='vendors' && var_pk_fields= rxMasterID_in) THEN
         SET Search_id_Ven = (SELECT searchId FROM search_index WHERE entity='vendors' AND pk_fields= rxMasterID_in);
         DELETE FROM  search_index WHERE searchId = Search_id_Ven;
   ELSEIF ( var_isVendor = 1 && var_entity_Vend IS NULL && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'vendors' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isVendor = 1 && var_entity_Vend IS NULL) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'vendors' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isArchitect = 0  && var_entity_Arch='architect' && var_pk_fields= rxMasterID_in) THEN
        SET Search_id_Arc =  (SELECT searchId FROM search_index WHERE entity='architect' AND pk_fields= rxMasterID_in);
        DELETE FROM  search_index WHERE searchId= Search_id_Arc;
   ELSEIF ( var_isArchitect = 1 && var_isEngineer = 0 && var_entity_Arch IS NULL && var_isEngineer = 0 && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isArchitect = 1 && var_isEngineer = 0 && var_entity_Arch IS NULL) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isEngineer = 0 &&var_entity_Eng='engineer' && var_pk_fields= rxMasterID_in) THEN
        SET Search_id_Eng =(SELECT searchId FROM search_index WHERE entity='engineer' AND pk_fields= rxMasterID_in);
        DELETE FROM  search_index WHERE searchId = Search_id_Eng;
   ELSEIF ( var_isEngineer = 1 && var_isArchitect = 0 && var_entity_Eng IS NULL && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'engineer' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isEngineer = 1  && var_isArchitect = 0 && var_entity_Eng IS NULL) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'engineer' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isGc = 0 && var_entity_Gc='generalcontractors' && var_pk_fields= rxMasterID_in) THEN
        SET Search_id_Gc =(SELECT searchId FROM search_index WHERE entity='generalcontractors' AND pk_fields= rxMasterID_in);
        DELETE FROM  search_index WHERE searchId = Search_id_Gc;
   ELSEIF ( var_isGc = 1 && var_entity_Gc IS NULL && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'generalcontractors' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isGc = 1 && var_entity_Gc IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'generalcontractors' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;

   IF ( var_isEngineer = 0 && var_isArchitect = 0 && var_entity_Eng_Arc='architect/engineer' && var_pk_fields= rxMasterID_in) THEN
        SET Search_id_EA = (SELECT searchId FROM search_index WHERE entity='architect/engineer' AND pk_fields= rxMasterID_in);
        DELETE FROM  search_index WHERE searchId = Search_id_EA;
   ELSEIF ( var_isEngineer = 1 && var_isArchitect = 1 && var_entity_Eng_Arc IS NULL && Phone1_in IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect/engineer' as entity, CONCAT(Name_in,'|',SearchName_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   ELSEIF ( var_isEngineer = 1 && var_isArchitect = 1 && var_entity_Eng_Arc IS NULL ) THEN
        INSERT INTO search_index ( `entity`, `searchText`, `resultedTableName`, `pk_fields` )
        SELECT 'architect/engineer' as entity, CONCAT(Name_in,'|',SearchName_in,'|',Phone1_in) AS searchText, 'rxMaster' as resultedTableName,
            rxMasterID_in as pk_fields FROM rxMaster WHERE rxMasterID = rxMasterID_in;
   END IF;   
   END