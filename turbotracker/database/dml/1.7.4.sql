ALTER TABLE `tsUserSetting` ADD COLUMN `companyLogo` BLOB  NOT NULL AFTER `billToDescription`;

ALTER TABLE `tsUserSetting` MODIFY COLUMN `companyLogo` LONGBLOB  NOT NULL;