/* created a isBillto, isShipto column  for setting the address*/ 

	ALTER TABLE `vePO` DROP COLUMN `isShipTo` , DROP COLUMN `isBillTo` ;

	ALTER TABLE `vePO` ADD COLUMN `billToIndex` INT(10) NULL DEFAULT '0'  AFTER `joStockReleaseID` , ADD COLUMN `shipToIndex` INT(10) NULL DEFAULT '0'  AFTER `billToIndex` ;

 
	/** Updaet initial value **/
	
	SET SQL_SAFE_UPDATES = 0 ;

	Update vePO SET billToIndex = 0;
	Update vePO SET shipToIndex = 2;