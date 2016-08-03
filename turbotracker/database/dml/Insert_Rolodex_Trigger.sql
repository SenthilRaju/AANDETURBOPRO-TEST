-- trigger for drop and insert a record in rxMaster table, affected in search_text table. --
DELIMITER $$ 
DROP TRIGGER IF EXISTS `tr_rolodexSearch`$$ 

CREATE TRIGGER `tr_rolodexSearch` AFTER INSERT on `rxMaster` 
	FOR EACH ROW 
		BEGIN 
			CALL sp_rolodex_ins_searchindex(NEW.rxMasterID, NEW.Name, NEW.SearchName, NEW.Phone1); 
		END$$ 
DELIMITER ; 