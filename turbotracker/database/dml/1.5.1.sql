		--  Alter table for  Charts Accounts Per Page  --
		
ALTER TABLE tsUserLogin ADD COLUMN chartAccountPerPage INT(10) NULL DEFAULT NULL  AFTER activeEmployeeList;

UPDATE `tsUserLogin` SET chartAccountPerPage = 50;