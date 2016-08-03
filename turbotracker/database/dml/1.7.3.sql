		/**  Save  billto Address  to 'tsSetting' **/
		
		ALTER TABLE `tsUserSetting` ADD COLUMN `IsBillTo` TINYINT(4) NOT NULL  AFTER `termsPurchaseOrders` , ADD COLUMN `billToAddress1` VARCHAR(45) NULL  AFTER `IsBillTo` , ADD COLUMN `billToAddress2` VARCHAR(45) NULL  AFTER `billToAddress1` , ADD COLUMN `billToCity` VARCHAR(45) NULL  AFTER `billToAddress2` , ADD COLUMN `billToState` VARCHAR(45) NULL  AFTER `billToCity` , ADD COLUMN `billToZip` VARCHAR(45) NULL  AFTER `billToState` , ADD COLUMN `billToDescription` VARCHAR(45) NULL  AFTER `billToZip` ;
		
			
		/** Set initial BillTo Address values **/
		
		UPDATE `tsUserSetting` SET `billToAddress1`='P.O. Box 921369', `billToAddress2`='', `billToCity`='Norcross', `billToState`='GA', `billToZip`='30010', `billToDescription`='Bache Sales Agency' WHERE `CompanyID`='1';