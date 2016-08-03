/** Insert the Admin User Details **/

INSERT INTO `tsUserLogin` (`LoginName`, `LoginPassword`, `FullName`, `Initials`, `SystemAdministrator`, `SalesRep`, `Employee0`, `Employee1`, `Employee2`, `Employee3`, `Employee4`, `Inactive`, `EmailName`, `EmailAddr`, 
																							`LogOnName`, `LogOnPswd`, `UseAuthentication`, `CCAddr1`, `SMTPSvr`, `SMTPEmailActive`, `SMTPPort`) 
														VALUES ('admin', 'brockadmin', 'Administrator', 'ADM', 1, 1, 1, 1, 1, 1, 1, 0, 'admin', 'noreply@aandespecialties.com', 'noreply@aandespecialties.com', 'A&Enoreply', '', '', 'smtpout.secureserver.net', '', 465);
														
/** Alter Table **/
														
ALTER TABLE `tsUserLogin` ADD COLUMN `userZipCode` BIGINT NULL  AFTER `SMTPPort` ;