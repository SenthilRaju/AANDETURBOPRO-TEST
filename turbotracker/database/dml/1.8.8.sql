CREATE TABLE `BacheCompany`.`joQuoteProperties` (
  `joQuotePropertiesID` INT NOT NULL AUTO_INCREMENT,
  `DisplayQuantity` VARCHAR(45) NOT NULL DEFAULT 0,
  `DisplayParagraph` TINYINT(2) NOT NULL DEFAULT 0,
  `DisplayManufacturer` TINYINT(2) NOT NULL DEFAULT 0,
  `DisplayCost` TINYINT(2) NOT NULL DEFAULT 0,
  `DisplayPrice` TINYINT(2) NOT NULL DEFAULT 0,
  `DisplayMult` TINYINT(2) NOT NULL DEFAULT 0,
  `DisplaySpec` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintQuantity` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintParagraph` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintManufacturer` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintCost` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintPrice` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintMult` TINYINT(2) NOT NULL DEFAULT 0,
  `PrintSpec` TINYINT(2) NOT NULL DEFAULT 0,
  `UserLoginID` INT NOT NULL,
  PRIMARY KEY (`joQuotePropertiesID`),
  UNIQUE INDEX `joQuotePropertiesID_UNIQUE` (`joQuotePropertiesID` ASC));

  
  ALTER TABLE `BacheCompany`.`joQuoteProperties` 
CHANGE COLUMN `DisplayQuantity` `DisplayQuantity` TINYINT(2) NOT NULL DEFAULT '0' ;

INSERT INTO `BacheCompany`.`joQuoteProperties` (`joQuotePropertiesID`, `DisplayQuantity`, `DisplayParagraph`, `DisplayManufacturer`, `DisplayCost`, `DisplayPrice`, `DisplayMult`, `DisplaySpec`, `PrintQuantity`, `PrintParagraph`, `PrintManufacturer`, `PrintCost`, `PrintPrice`, `PrintMult`, `PrintSpec`) VALUES ('0', '1', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
