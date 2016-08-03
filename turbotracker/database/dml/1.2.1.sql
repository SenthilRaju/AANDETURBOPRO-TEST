/* Created a 'userPerPage column for saving a value */

ALTER TABLE `tsUserLogin` ADD COLUMN `userPerPage` INT(10) NULL  AFTER `userZipCode` ;

/** Alter column in 'ActiveUserList' in UserLogin Table **/

ALTER TABLE `tsUserLogin` ADD COLUMN `activeUserList` INT(10) NULL DEFAULT NULL  AFTER `rolodexPerPage` ;

UPDATE `tsUserLogin` SET `activeUserList` = 0;

