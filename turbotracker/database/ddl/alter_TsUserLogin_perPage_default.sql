ALTER TABLE `BacheCompany`.`tsUserLogin` 
CHANGE COLUMN `userPerPage` `userPerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `customerPerPage` `customerPerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `vendorPerPage` `vendorPerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `employeePerPage` `employeePerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `rolodexPerPage` `rolodexPerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `activeUserList` `activeUserList` INT(10) DEFAULT 50 ,
CHANGE COLUMN `activeEmployeeList` `activeEmployeeList` INT(10) DEFAULT 50 ,
CHANGE COLUMN `chartAccountPerPage` `chartAccountPerPage` INT(10) NOT NULL DEFAULT 50 ,
CHANGE COLUMN `bankingPerPage` `bankingPerPage` INT(10) NOT NULL DEFAULT 50 ;