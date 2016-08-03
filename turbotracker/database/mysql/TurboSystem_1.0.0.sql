-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.11
--
-- Create schema turbosystem
--

CREATE DATABASE IF NOT EXISTS turbosystem;
USE turbosystem;

--
-- Definition of table `accessmodule`
--

DROP TABLE IF EXISTS `accessmodule`;
CREATE TABLE `accessmodule` (
  `AccessModuleID` int(11) NOT NULL DEFAULT '0',
  `ModuleName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`AccessModuleID`),
  KEY `AccessModuleID` (`AccessModuleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `accessmodule`
--


INSERT INTO `accessmodule` (`AccessModuleID`,`ModuleName`) VALUES 
 (1000,'Company'),
 (1500,'Financial Reports'),
 (1600,'Tax Reports'),
 (1700,'Other Reports'),
 (2000,'Banking'),
 (2900,'Banking Reports'),
 (3000,'Product'),
 (3900,'Product Reports'),
 (4000,'Customer'),
 (4900,'Customer Reports'),
 (5000,'Employee'),
 (5800,'Payroll Reports'),
 (5900,'Salesrep Reports'),
 (6000,'Vendor'),
 (6900,'Vendor Reports'),
 (7000,'Job'),
 (7900,'Job Reports'),
 (8000,'Tools'),
 (8200,'Administrative'),
 (9550,'Debug');



--
-- Definition of table `accessprocedure`
--

DROP TABLE IF EXISTS `accessprocedure`;
CREATE TABLE `accessprocedure` (
  `AccessProcedureID` int(11) NOT NULL DEFAULT '0',
  `AccessModuleID` int(11) DEFAULT '0',
  `ProcedureName` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`AccessProcedureID`),
  KEY `AccessProcedureID` (`AccessProcedureID`),
  KEY `AccessModuleID` (`AccessModuleID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `accessprocedure`
--

INSERT INTO `accessprocedure` (`AccessProcedureID`,`AccessModuleID`,`ProcedureName`) VALUES 
 (101200,1000,'Rolodex'),
 (101201,1000,'* Allow adding or changing Rolodex *'),
 (110000,1000,'Chart of Accounts'),
 (110100,1000,'Divisions'),
 (112000,1000,'Tax Territories'),
 (120000,1000,'Accounting Cycles'),
 (130000,1000,'Journal Entries'),
 (140000,1000,'General Ledger'),
 (150100,1500,'Trial Balance'),
 (150200,1500,'Income Statement'),
 (150300,1500,'Balance Sheet'),
 (150400,1500,'Cash Flow Statement'),
 (150500,1500,'Budgeting'),
 (150600,1500,'Chart of Accounts'),
 (170100,1700,'Rolodex Listing'),
 (211000,2000,'Bank Account List'),
 (221000,2000,'Transaction Register'),
 (222000,2000,'Write Checks'),
 (223000,2000,'Reconcile Account'),
 (224000,2000,'Prepare Deposit'),
 (290100,2900,'Account Summary'),
 (290200,2900,'Transaction Register '),
 (290300,2900,'Reconciliation'),
 (311000,3000,'Product/Service List'),
 (311001,3000,'* Allow adding or changing products *'),
 (312000,3000,'Departments'),
 (312500,3000,'Categories'),
 (313000,3000,'Warehouses'),
 (314000,3000,'Order Points'),
 (321000,3000,'Receive Inventory'),
 (322000,3000,'Transfer Inventory'),
 (323000,3000,'Count Inventory'),
 (390100,3900,'Price Sheet'),
 (390200,3900,'Sales by Item'),
 (390300,3900,'Inventory Turns'),
 (390400,3900,'Suggested Reorder'),
 (390500,3900,'Count Sheet'),
 (390600,3900,'Backorders'),
 (390700,3900,'Department Summary'),
 (390800,3900,'Inventory Transactions'),
 (390900,3900,'Inventory Value'),
 (391000,3900,'Non Inventory Items'),
 (391100,3900,'Inventory Items'),
 (411000,4000,'Customer List'),
 (411001,4000,'* Allow adding or changing customers *'),
 (412000,4000,'Payment Terms'),
 (413000,4000,'Customer Types'),
 (421000,4000,'Sales Orders'),
 (422000,4000,'Invoices'),
 (422001,4000,'* Allow changing existing invoice *'),
 (422002,4000,'* Override Customer Holds *'),
 (422100,4000,'Print Multiple Invoices'),
 (423000,4000,'Payments'),
 (431000,4000,'Statements'),
 (432000,4000,'Finance Charges'),
 (490100,4900,'A/R Aging (Current)'),
 (490200,4900,'Customer Transactions'),
 (490300,4900,'Customers on Credit Hold'),
 (490400,4900,'Customer Profit Summary'),
 (490500,1600,'Sales Tax'),
 (490600,4900,'Payments Received'),
 (490700,4900,'Quoted Jobs'),
 (490800,4900,'Customer Listing'),
 (490900,4900,'Unapplied Payments'),
 (491000,4900,'Billing Profit Detail'),
 (491100,4900,'Customer Profit Margin'),
 (491200,4900,'Open Sales Orders'),
 (491300,4900,'Bill Only Releases not Billed'),
 (491400,4900,'A/R Aging (As Of)'),
 (491500,4900,'Customer Invoices'),
 (491600,4900,'Average Days to Pay'),
 (491700,4900,'Field Service'),
 (511000,5000,'Employee List'),
 (512000,5000,'Commissions'),
 (513000,5000,'Assign CSRs'),
 (514000,5000,'Prepare Payroll'),
 (515000,5000,'Manual Records'),
 (580100,5800,'Posting Detail'),
 (580200,5800,'Validation'),
 (581000,1600,'941 Quarterly Federal Tax Return'),
 (581100,1600,'State Quarterly Wage Worksheet'),
 (581200,1600,'W2 Wage and Tax Statement'),
 (581300,1600,'W3 Transmittal of Wage and Tax Statements'),
 (581400,1600,'1099 MISC Tax Statement'),
 (581500,1600,'940 Federal Unemployment Report'),
 (590100,5900,'Potential Sales'),
 (590200,5900,'Booking Performance'),
 (590300,5900,'Billing Profit'),
 (590400,5900,'Customer Profit'),
 (590500,5900,'Booking Gross / Profit Summary'),
 (590600,5900,'Booking Gross / Profit Detail'),
 (590700,5900,'Monthly Gross Bookings'),
 (590800,5900,'Monthly Profit Bookings'),
 (590900,5900,'Open Jobs'),
 (591000,5900,'Billing Profit Detail'),
 (591100,5900,'Employee Profit Margin'),
 (611000,6000,'Vendor List'),
 (611001,6000,'* Allow adding or changing vendors *'),
 (619000,6000,'Ship Via List'),
 (619100,6000,'Freight Charge List'),
 (621000,6000,'Purchase Orders'),
 (622000,6000,'Invoices  Bills'),
 (622001,6000,'* Allow changing existing invoice *'),
 (623000,6000,'Pay Bills'),
 (624000,6000,'Shipment Confirmation'),
 (690100,6900,'Purchasing Summary'),
 (690200,6900,'Direct Sales Detail'),
 (690300,6900,'Open POs'),
 (690400,6900,'Expected Receiving'),
 (690500,6900,'A/P Aging (Current)'),
 (690600,6900,'Vendor Transactions'),
 (690700,6900,'G/L Detail'),
 (690800,6900,'Vendor / Customer Invoice Billing Comparision'),
 (690900,6900,'A/P Aging (As Of)'),
 (691000,6900,'Vendor Invoices'),
 (691100,6900,'Vendor Listing'),
 (691200,6900,'Purchasing Detail'),
 (691300,6900,'Open Factory Commissions'),
 (691400,6900,'Vendor Products Bought'),
 (711000,7000,'Job List'),
 (711001,7000,'* Allow any change after booked *'),
 (711002,7000,'* Allow Booking Jobs *'),
 (711003,7000,'* Allow Closing Jobs *'),
 (711004,7000,'* Allow Changing Credit Status *'),
 (711100,7000,'Submittals'),
 (721000,7000,'Bid Status Codes'),
 (722000,7000,'Submittal Schedule Templates'),
 (790100,7900,'Bid Dates'),
 (790200,7900,'Unassigned Job Bid List'),
 (790300,7900,'Open Jobs'),
 (790400,7900,'GC\'s  Subs'),
 (790500,7900,'Lien Rights Alert'),
 (790600,7900,'Quoted Job Status'),
 (790700,7900,'Suggested Closeouts'),
 (790800,7900,'Closed Jobs'),
 (790900,7900,'Booked / Not Released'),
 (791000,7900,'Joint Check'),
 (791100,7900,'Submitted / Not Released'),
 (791200,7900,'Quoted Jobs, Low GC No Sub'),
 (791300,7900,'Engineer\'s  Subs'),
 (791400,7900,'Bid Jobs / Unknown GC'),
 (791500,7900,'Claims Filed'),
 (801000,8000,'Select Company'),
 (801100,8000,'Company'),
 (801200,8000,'Rebuild Database'),
 (801300,8000,'My'),
 (801400,8000,'Quick A/R  A/P Entry'),
 (801500,8000,'Payroll'),
 (801600,8000,'General'),
 (801700,8000,'Forms'),
 (821000,8200,'User List'),
 (822000,8200,'Group List'),
 (823000,8200,'Current Users'),
 (824000,9550,'Merge'),
 (899800,9550,'Show Grid Info'),
 (899900,9550,'Create FormScratch'),
 (995100,9550,'Reset Company'),
 (995200,9550,'Temp Stuff');



--
-- Definition of table `bugarea`
--

DROP TABLE IF EXISTS `bugarea`;
CREATE TABLE `bugarea` (
  `BugAreaID` int(11) NOT NULL AUTO_INCREMENT,
  `AreaDescription` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`BugAreaID`)
) ENGINE=MyISAM AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bugarea`
--


INSERT INTO `bugarea` (`BugAreaID`,`AreaDescription`) VALUES 
 (1,'Accounting Cycles'),
 (2,'Bank Account List'),
 (3,'Chart of Accounts'),
 (4,'Count Inventory'),
 (5,'Customer Invoices'),
 (6,'Customer List'),
 (7,'Customer Types'),
 (8,'Departments'),
 (9,'Finance Charges'),
 (10,'Freight Charge List'),
 (11,'General Ledger'),
 (12,'Job List'),
 (13,'Journal Entries'),
 (14,'Options'),
 (15,'Pay Bills'),
 (16,'Payment Terms'),
 (17,'Payments'),
 (18,'Prepare Deposit'),
 (19,'Product/Service List'),
 (20,'Purchase Orders'),
 (21,'Rebuild Database'),
 (22,'Receive Inventory'),
 (23,'Reconcile Account'),
 (24,'Reports'),
 (25,'Rolodex'),
 (26,'Sales Orders'),
 (27,'Ship Via List'),
 (28,'Statements'),
 (29,'Tax Territories'),
 (30,'Transaction Register'),
 (31,'Transfer Inventory'),
 (32,'Vendor Invoices & Bills'),
 (33,'Vendor List'),
 (34,'Warehouses'),
 (35,'Write Checks');



--
-- Definition of table `bugcategory`
--

DROP TABLE IF EXISTS `bugcategory`;
CREATE TABLE `bugcategory` (
  `BugCategoryID` tinyint(4) NOT NULL DEFAULT '0',
  `CategoryDescription` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`BugCategoryID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bugcategory`
--


INSERT INTO `bugcategory` (`BugCategoryID`,`CategoryDescription`) VALUES 
 (0,'(Select a Category)'),
 (1,'Error Message'),
 (2,'Data Error'),
 (3,'Other Error'),
 (4,'New Feature Idea'),
 (5,'Change Request');



--
-- Definition of table `bugreport`
--

DROP TABLE IF EXISTS `bugreport`;
CREATE TABLE `bugreport` (
  `BugReportID` int(11) NOT NULL AUTO_INCREMENT,
  `BugCategoryID` tinyint(4) DEFAULT '0',
  `BugAreaID` int(11) DEFAULT '0',
  `UserLoginID` int(11) DEFAULT '0',
  `EntyDate` datetime DEFAULT NULL,
  `Summary` varchar(50) DEFAULT NULL,
  `DetailedDescription` longtext,
  PRIMARY KEY (`BugReportID`),
  KEY `BugAreaID` (`BugAreaID`),
  KEY `BugCategoryID` (`BugCategoryID`),
  KEY `BugReportID` (`BugReportID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bugreport`
--

--
-- Definition of table `company`
--

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `CompanyID` int(11) NOT NULL AUTO_INCREMENT,
  `CompanyName` varchar(40) DEFAULT NULL,
  `DatasetName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`CompanyID`),
  KEY `CompanyID` (`CompanyID`)
) ENGINE=MyISAM AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `company`
--


INSERT INTO `company` (`CompanyID`,`CompanyName`,`DatasetName`) VALUES 
 (32,'LOCAL TEST COMPANY','xxx'),
 (33,'Testco','xxx');



--
-- Definition of table `fax`
--

DROP TABLE IF EXISTS `fax`;
CREATE TABLE `fax` (
  `FaxID` int(11) NOT NULL AUTO_INCREMENT,
  `OwnerID` int(11) DEFAULT NULL,
  `CompanyID` int(11) DEFAULT NULL,
  `DateCreated` datetime DEFAULT NULL,
  `FaxNo` varchar(10) DEFAULT NULL,
  `ServerRequest` tinyint(4) DEFAULT '0',
  `TransactionType` int(11) DEFAULT '0',
  `TransactionID` int(11) DEFAULT '0',
  `ToCompany` varchar(50) DEFAULT NULL,
  `Subject` varchar(80) DEFAULT NULL,
  `TransmitDateTimeString` varchar(20) DEFAULT NULL,
  `TransmitCSID` varchar(50) DEFAULT NULL,
  `JobName` varchar(50) DEFAULT NULL,
  `ProgressCheck` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`FaxID`),
  KEY `CompanyID` (`CompanyID`),
  KEY `FaxID` (`FaxID`),
  KEY `OwnerID` (`OwnerID`),
  KEY `ServerRequest` (`ServerRequest`,`FaxID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `fax`
--


INSERT INTO `fax` (`FaxID`,`OwnerID`,`CompanyID`,`DateCreated`,`FaxNo`,`ServerRequest`,`TransactionType`,`TransactionID`,`ToCompany`,`Subject`,`TransmitDateTimeString`,`TransmitCSID`,`JobName`,`ProgressCheck`) VALUES 
 (1,1,32,'2004-08-05 10:07:40','234',3,13,10,'aaa','Quote for project: test',NULL,NULL,'',0),
 (2,1,32,'2004-08-05 12:43:12','234',3,13,10,'aaa','Quote for project: test',NULL,NULL,'',0),
 (3,1,32,'2004-08-05 12:48:57','234',3,13,10,'aaa','Quote for project: test',NULL,NULL,'',0),
 (4,1,32,'2005-04-27 11:57:23','3433434343',3,13,5,'A A Air Company','Quote for project: test',NULL,NULL,'',0),
 (5,1,32,'2005-04-27 11:59:17','3433434343',3,13,5,'A A Air Company','Quote for project: test',NULL,NULL,'',0);



--
-- Definition of table `info`
--

DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `InfoID` int(11) NOT NULL DEFAULT '0',
  `DBRequiresMajor` int(11) DEFAULT '0',
  `DBRequiresMinor` int(11) DEFAULT '0',
  `DBRequiresRevision` int(11) DEFAULT '0',
  `LatestMajor` int(11) DEFAULT '0',
  `LatestMinor` int(11) DEFAULT '0',
  `LatestRevision` int(11) DEFAULT '0',
  `Locked` tinyint(1) DEFAULT '0',
  `LockedReason` varchar(200) DEFAULT NULL,
  `ServerTicks` int(11) DEFAULT '0',
  `ServerOutBoxCount` int(11) DEFAULT '0',
  PRIMARY KEY (`InfoID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `info`
--



--
-- Definition of table `license`
--

DROP TABLE IF EXISTS `license`;
CREATE TABLE `license` (
  `LicenseID` int(11) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) DEFAULT NULL,
  `Company` varchar(50) DEFAULT NULL,
  `SerialNo` varchar(50) DEFAULT NULL,
  `Users` int(11) DEFAULT '0',
  PRIMARY KEY (`LicenseID`),
  KEY `LicenseID` (`LicenseID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `license`
--

--
-- Definition of table `message`
--

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `MessageID` int(11) NOT NULL AUTO_INCREMENT,
  `OwnerID` int(11) DEFAULT NULL,
  `CompanyID` int(11) DEFAULT NULL,
  `UserToID` int(11) DEFAULT NULL,
  `GroupToID` int(11) DEFAULT NULL,
  `FromID` int(11) DEFAULT NULL,
  `FromWorkstationID` int(11) DEFAULT NULL,
  `ReferenceID` int(11) DEFAULT NULL,
  `MessageFolder` tinyint(4) DEFAULT NULL,
  `ForID` int(11) DEFAULT '0',
  `Hidden` tinyint(1) DEFAULT '0',
  `SenderCopy` tinyint(1) DEFAULT '0',
  `DraftCopy` tinyint(1) DEFAULT '0',
  `MessageType` tinyint(4) DEFAULT NULL,
  `ToName` varchar(50) DEFAULT NULL,
  `Attention` varchar(50) DEFAULT NULL,
  `FaxToID` int(11) DEFAULT NULL,
  `AttentionID` int(11) DEFAULT NULL,
  `FromName` varchar(50) DEFAULT NULL,
  `Subject` varchar(80) DEFAULT NULL,
  `FaxNumber` varchar(30) DEFAULT NULL,
  `SendDate` datetime DEFAULT NULL,
  `MsgUrgencyID` int(11) DEFAULT NULL,
  `RespondBy` datetime DEFAULT NULL,
  `Urgency` varchar(30) DEFAULT NULL,
  `BodyText` longtext,
  PRIMARY KEY (`MessageID`),
  KEY `AttentionID` (`AttentionID`),
  KEY `CompanyID` (`CompanyID`),
  KEY `FaxToID` (`FaxToID`),
  KEY `ForID` (`ForID`),
  KEY `FromID` (`FromID`),
  KEY `FromWorkstationID` (`FromWorkstationID`),
  KEY `GroupToID` (`GroupToID`),
  KEY `MessageID` (`MessageID`),
  KEY `UserToID` (`UserToID`),
  KEY `MsgUrgencyID` (`MsgUrgencyID`),
  KEY `OwnerID` (`OwnerID`),
  KEY `ReferenceID` (`ReferenceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `message`
--

--
-- Definition of table `msgurgency`
--

DROP TABLE IF EXISTS `msgurgency`;
CREATE TABLE `msgurgency` (
  `MsgUrgencyID` int(11) NOT NULL AUTO_INCREMENT,
  `Urgency` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`MsgUrgencyID`),
  KEY `MsgUrgencyID` (`MsgUrgencyID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `msgurgency`
--

INSERT INTO `msgurgency` (`MsgUrgencyID`,`Urgency`) VALUES 
 (1,'Urgent Message'),
 (2,'Respond By . . .'),
 (3,'FYI'),
 (4,'Immediate Reponse Requested');



--
-- Definition of table `spidlist`
--

DROP TABLE IF EXISTS `spidlist`;
CREATE TABLE `spidlist` (
  `SPID` int(11) NOT NULL,
  `UserLoginID` int(11) DEFAULT NULL,
  PRIMARY KEY (`SPID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `spidlist`
--

INSERT INTO `spidlist` (`SPID`,`UserLoginID`) VALUES 
 (1,5);



--
-- Definition of table `ticket`
--

DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `TicketID` int(11) NOT NULL AUTO_INCREMENT,
  `SubmittedByID` int(11) DEFAULT '0',
  `SubmittedOn` datetime DEFAULT NULL,
  `prWarehouseID` int(11) DEFAULT '0',
  `cuSOID` int(11) DEFAULT '0',
  `Status` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`TicketID`),
  KEY `prWarehouseID` (`prWarehouseID`),
  KEY `TicketID` (`TicketID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ticket`
--

--
-- Definition of table `usergroup`
--

DROP TABLE IF EXISTS `usergroup`;
CREATE TABLE `usergroup` (
  `UserGroupID` int(11) NOT NULL AUTO_INCREMENT,
  `GroupName` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`UserGroupID`),
  KEY `UserGroupID` (`UserGroupID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usergroup`
--

INSERT INTO `usergroup` (`UserGroupID`,`GroupName`) VALUES 
 (1,'Salesreps'),
 (2,'CSRs'),
 (3,'Employees'),
 (4,'Accounting');


--
-- Definition of table `usergrouplink`
--

DROP TABLE IF EXISTS `usergrouplink`;
CREATE TABLE `usergrouplink` (
  `UserGroupLinkID` int(11) NOT NULL AUTO_INCREMENT,
  `UserLoginID` int(11) DEFAULT '0',
  `UserGroupID` int(11) DEFAULT '0',
  PRIMARY KEY (`UserGroupLinkID`),
  KEY `UserGroupID` (`UserGroupID`),
  KEY `UserGroupLinkID` (`UserGroupLinkID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usergrouplink`
--

--
-- Definition of table `userlogin`
--

DROP TABLE IF EXISTS `userlogin`;
CREATE TABLE `userlogin` (
  `UserLoginID` int(11) NOT NULL AUTO_INCREMENT,
  `LoginName` varchar(15) DEFAULT NULL,
  `LoginPassword` varchar(15) DEFAULT NULL,
  `FullName` varchar(50) DEFAULT NULL,
  `Initials` varchar(6) DEFAULT NULL,
  `SystemAdministrator` tinyint(1) DEFAULT '0',
  `SalesRep` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`UserLoginID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userlogin`
--
--
-- Definition of table `workstation`
--

DROP TABLE IF EXISTS `workstation`;
CREATE TABLE `workstation` (
  `WorkstationID` int(11) NOT NULL AUTO_INCREMENT,
  `ComputerName` varchar(20) DEFAULT NULL,
  `MacAddress` varchar(100) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `DateRegistered` datetime DEFAULT NULL,
  `DateLastAccessed` datetime DEFAULT NULL,
  `AccessCounter` int(11) DEFAULT '0',
  `LoginName` varchar(15) DEFAULT '',
  `LoginPassword` varchar(50) DEFAULT '',
  `LatestRunMajor` int(11) DEFAULT '0',
  `LatestRunMinor` int(11) DEFAULT '0',
  `LatestRunRevision` int(11) DEFAULT '0',
  `AutoLogin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`WorkstationID`),
  KEY `ComputerName` (`ComputerName`,`MacAddress`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `workstation`
--

INSERT INTO `workstation` (`WorkstationID`,`ComputerName`,`MacAddress`,`Description`,`DateRegistered`,`DateLastAccessed`,`AccessCounter`,`LoginName`,`LoginPassword`,`LatestRunMajor`,`LatestRunMinor`,`LatestRunRevision`,`AutoLogin`) VALUES 
 (1,'SSMITH','00023F7C7058','SSMITH','2004-05-25 06:55:48','2005-09-23 08:45:55',3384,'Admin','',0,0,0,1),
 (2,'SSMITH','00022D70AE14','SSMITH','2004-05-25 19:58:49','2005-03-27 14:09:28',2,'','',0,0,0,0),
 (3,'SSMITH','','SSMITH','2004-07-20 10:36:41','2005-03-11 09:28:45',19,'Admin','',0,0,0,0),
 (4,'SSMITH','00023F7C7058-00022D70AE14','SSMITH','2005-09-08 11:07:33','2005-09-08 11:07:33',1,'','',0,0,0,0);

