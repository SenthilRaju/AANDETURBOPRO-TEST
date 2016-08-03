-- trigger for drop and update a record in rxMaster table, affected in search_text table. --
DELIMITER $$ 
DROP TRIGGER IF EXISTS `tr_upd_rolodexSearch`$$ 

CREATE TRIGGER `tr_upd_rolodexSearch` AFTER UPDATE on `rxMaster` 
	FOR EACH ROW 
		BEGIN 
			CALL sp_rolodex_upd_searchindex(NEW.rxMasterID, NEW.Name, NEW.SearchName, NEW.Phone1); 
		END$$ 
DELIMITER ; 