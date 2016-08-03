 ALTER TABLE `joQuoteDetail` ADD COLUMN `Percentage` DECIMAL(19,4) NULL DEFAULT 0  AFTER `position`;
 
 UPDATE joQuoteDetail set Percentage =  (1-(Cost/Price))*100;
 
 SET SQL_SAFE_UPDATES=0;