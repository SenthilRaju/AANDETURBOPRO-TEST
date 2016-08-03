/** add column for Save other address in 'PO Genereal' **/

ALTER TABLE `rxAddress` ADD COLUMN `otherBillTo` INT(10) NULL DEFAULT '0'  AFTER `coTaxTerritoryID` , ADD COLUMN `otherShipTo` INT(10) NULL DEFAULT '0'  AFTER `otherBillTo` ;

SET SQL_SAFE_UPDATES = 0;

UPDATE rxAddress SET otherBillTo = 0;

UPDATE rxAddress SET otherShipTo = 0;

/** Add column for email time stamp **/

ALTER TABLE `vePO` ADD COLUMN `emailTimeStamp` DATETIME NULL DEFAULT NULL  AFTER `shipToIndex` ;