/** Alter Query for Rolodex Per Page **/

ALTER TABLE `tsUserLogin` ADD COLUMN `customerPerPage` INT(10) NULL DEFAULT NULL  AFTER `userPerPage` , ADD COLUMN `vendorPerPage` INT(10) NULL DEFAULT NULL  AFTER `customerPerPage` , ADD COLUMN `employeePerPage` INT(10) NULL DEFAULT NULL  AFTER `vendorPerPage` , ADD COLUMN `rolodexPerPage` INT(10) NULL DEFAULT NULL  AFTER `employeePerPage` ;

/** Set default values **/

UPDATE `tsUserLogin` SET `userPerPage`=25, `customerPerPage`=50, `vendorPerPage`=100, `employeePerPage`=50, `rolodexPerPage`=50;