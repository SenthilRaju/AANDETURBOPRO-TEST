/** Add Banking information in tsAccessProcedure table **/

INSERT INTO `tsAccessProcedure` (`AccessProcedureID`, `AccessModuleID`, `ProcedureName`) VALUES (1601000, 16000, 'Banking');

/** Add per page column in User Table  **/

ALTER TABLE `tsUserLogin` ADD COLUMN `bankingPerPage` INT(10) NULL DEFAULT NULL  AFTER `chartAccountPerPage`;

UPDATE `tsUserLogin` SET `bankingPerPage` = 25;