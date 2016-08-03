-- Add Column in vePODetail --
ALTER TABLE `vePODetail` ADD COLUMN `posistion` DOUBLE  NOT NULL DEFAULT 0.00 AFTER `cuSODetailID`;

ALTER TABLE `vePODetail` CHANGE COLUMN `posistion` `posistion` DOUBLE NULL DEFAULT '0.00';

UPDATE vePODetail SET posistion = vePODetailID;