ALTER TABLE `prMaster` ADD COLUMN `NewPrice` DECIMAL(19,4) NULL DEFAULT NULL AFTER `Submitted` , ADD COLUMN `OldPrice` DECIMAL(19,4) NULL DEFAULT NULL AFTER `NewPrice` , ADD COLUMN `LastCost` DECIMAL(19,4) NULL DEFAULT NULL AFTER `OldPrice`;

ALTER TABLE `prMaster` ADD COLUMN `NewPrice` DECIMAL(19,4) NULL DEFAULT NULL  AFTER `Submitted` , ADD COLUMN `OldPrice` DECIMAL(19,4) NULL DEFAULT NULL  AFTER `NewPrice` , ADD COLUMN `LastCost` DECIMAL(19,4) NULL DEFAULT NULL  AFTER `OldPrice` ;

