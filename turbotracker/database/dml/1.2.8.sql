-- Active/InActive Employee List -- 

ALTER TABLE `tsUserLogin` ADD COLUMN `activeEmployeeList` INT(10) NULL DEFAULT NULL  AFTER `activeUserList` ;

UPDATE `tsUserLogin` SET `activeEmployeeList` = 0