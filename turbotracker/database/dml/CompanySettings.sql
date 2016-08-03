/** Create Quoery for usersettring */
CREATE TABLE  `tsUserSetting` (
  `CompanyID` int(10) NOT NULL AUTO_INCREMENT,
  `LoginID` int(10) NOT NULL,
  `Quote` tinyint(4) NOT NULL,
  `QuickQuote` tinyint(4) NOT NULL,
  `Invoices` tinyint(4) NOT NULL,
  `PurchaseOrders` tinyint(4) NOT NULL,
  `HeaderText` longtext,
  `Terms` longtext,
  `headerQuote` tinyint(4) NOT NULL,
  `headerQuickQuote` tinyint(4) NOT NULL,
  `headerInvoices` tinyint(4) NOT NULL,
  `headerPurchaseOrders` tinyint(4) NOT NULL,
  `termsQuote` tinyint(4) NOT NULL,
  `termsQuickQuote` tinyint(4) NOT NULL,
  `termsInvoices` tinyint(4) NOT NULL,
  `termsPurchaseOrders` tinyint(4) NOT NULL,
  PRIMARY KEY (`CompanyID`) USING BTREE
) AUTO_INCREMENT=0 DEFAULT CHARSET=latin1;

/** Alternate Quoery for usersettring */

ALTER TABLE `tsUserSetting` ADD COLUMN `companyLogo` BLOB  NOT NULL AFTER `termsPurchaseOrders`;

/** Insert Query **/

INSERT INTO `BartosCompany`.`tsUserSetting` VALUES  (1,4,0,0,0,0,
										'<b><font size=\"4\">THE UNDERWOOD COMPANIES</font></b><div align=\"justify\"><div style=\"text-align: justify;\"></div></div>','<div style=\"text-align: left;\"><div>
										<div>NET 30 DAYS OR FACTORY STANDARD, SUBJECT TO CREDIT APPROVAL.TAXES:NONE INCLUDED.SHIPMENT:F.O.B.FACTORY OR WAREHOUSE.FREIGHT:PREPAID 
										`and`amp; ALLOWED WITH $25 NOTIFICATION CHARGE ADDED AS REQUESTED.</div>
										</div></div>',0,0,0,0,0,0,0,0);