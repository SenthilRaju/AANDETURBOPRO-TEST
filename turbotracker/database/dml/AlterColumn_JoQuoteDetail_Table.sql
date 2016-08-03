-- Add Column in JoQuoteDetail --
ALTER TABLE `joQuoteDetail` CHANGE COLUMN `position` `position` DOUBLE NULL DEFAULT '0.00'  ;

ALTER TABLE `joQuoteDetail` ADD COLUMN `position` DOUBLE  NOT NULL DEFAULT 0.00 AFTER `PricingNote`;

UPDATE joQuoteDetail SET position = joQuoteDetailID;
