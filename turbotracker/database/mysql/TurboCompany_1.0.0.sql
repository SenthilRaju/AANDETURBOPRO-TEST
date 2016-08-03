-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.11


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema turbocompany
--

CREATE DATABASE IF NOT EXISTS turbocompany;
USE turbocompany;

--
-- Definition of table `coaccount`
--

DROP TABLE IF EXISTS `coaccount`;
CREATE TABLE `coaccount` (
  `coAccountID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) NOT NULL DEFAULT '0',
  `Number` varchar(12) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `BalanceSheetColumn` tinyint(4) NOT NULL DEFAULT '0',
  `IncludeWhenZero` tinyint(1) NOT NULL DEFAULT '0',
  `DebitBalance` tinyint(1) NOT NULL DEFAULT '0',
  `ContraAccount` tinyint(1) NOT NULL DEFAULT '0',
  `LineAboveAmount` tinyint(4) NOT NULL DEFAULT '0',
  `LineBelowAmount` tinyint(4) NOT NULL DEFAULT '0',
  `TotalingLevel` tinyint(4) NOT NULL DEFAULT '0',
  `VerticalSpacing` tinyint(4) NOT NULL DEFAULT '0',
  `HorizontalSpacing` tinyint(4) NOT NULL DEFAULT '0',
  `FontLarge` tinyint(1) NOT NULL DEFAULT '0',
  `FontBold` tinyint(1) NOT NULL DEFAULT '0',
  `FontItalic` tinyint(1) NOT NULL DEFAULT '0',
  `FontUnderline` tinyint(1) NOT NULL DEFAULT '0',
  `Tax1099` tinyint(1) NOT NULL DEFAULT '0',
  `SubAccount` varchar(10) DEFAULT NULL,
  `IsSubAccount` tinyint(1) NOT NULL DEFAULT '0',
  `IsMasterAccount` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`coAccountID`),
  KEY `coAccountID` (`coAccountID`),
  KEY `Number` (`Number`)
) ENGINE=MyISAM AUTO_INCREMENT=125 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coaccount`
--

/*!40000 ALTER TABLE `coaccount` DISABLE KEYS */;
INSERT INTO `coaccount` (`coAccountID`,`InActive`,`Number`,`Description`,`BalanceSheetColumn`,`IncludeWhenZero`,`DebitBalance`,`ContraAccount`,`LineAboveAmount`,`LineBelowAmount`,`TotalingLevel`,`VerticalSpacing`,`HorizontalSpacing`,`FontLarge`,`FontBold`,`FontItalic`,`FontUnderline`,`Tax1099`,`SubAccount`,`IsSubAccount`,`IsMasterAccount`) VALUES 
 (124,0,'0310','Retained Earnings',0,0,0,0,0,0,0,0,0,0,0,0,0,0,NULL,0,0);
/*!40000 ALTER TABLE `coaccount` ENABLE KEYS */;


--
-- Definition of table `cobalance`
--

DROP TABLE IF EXISTS `cobalance`;
CREATE TABLE `cobalance` (
  `coBalanceID` int(11) NOT NULL AUTO_INCREMENT,
  `coAccountID` int(11) DEFAULT '0',
  `coFiscalPeriodID` int(11) DEFAULT '0',
  `YearOpening` decimal(19,4) DEFAULT '0.0000',
  `YearDebits` decimal(19,4) DEFAULT '0.0000',
  `YearCredits` decimal(19,4) DEFAULT '0.0000',
  `QuarterOpening` decimal(19,4) DEFAULT '0.0000',
  `QuarterDebits` decimal(19,4) DEFAULT '0.0000',
  `QuarterCredits` decimal(19,4) DEFAULT '0.0000',
  `PeriodOpening` decimal(19,4) DEFAULT '0.0000',
  `PeriodDebits` decimal(19,4) DEFAULT '0.0000',
  `PeriodCredits` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`coBalanceID`),
  KEY `coAccountID` (`coAccountID`),
  KEY `coFiscalPeriodID` (`coFiscalPeriodID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cobalance`
--

/*!40000 ALTER TABLE `cobalance` DISABLE KEYS */;
/*!40000 ALTER TABLE `cobalance` ENABLE KEYS */;


--
-- Definition of table `codivision`
--

DROP TABLE IF EXISTS `codivision`;
CREATE TABLE `codivision` (
  `coDivisionID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) DEFAULT NULL,
  `Code` varchar(6) DEFAULT NULL,
  `InActive` tinyint(1) NOT NULL DEFAULT '0',
  `UseInvoiceSeqNo` tinyint(1) NOT NULL DEFAULT '0',
  `InvoiceSeqNo` int(11) DEFAULT NULL,
  `AddressQuote` tinyint(1) NOT NULL DEFAULT '0',
  `Address1` varchar(40) DEFAULT NULL,
  `Address2` varchar(40) DEFAULT NULL,
  `Address3` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`coDivisionID`),
  KEY `coDivisionID` (`coDivisionID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `codivision`
--

/*!40000 ALTER TABLE `codivision` DISABLE KEYS */;
INSERT INTO `codivision` (`coDivisionID`,`Description`,`Code`,`InActive`,`UseInvoiceSeqNo`,`InvoiceSeqNo`,`AddressQuote`,`Address1`,`Address2`,`Address3`) VALUES 
 (1,'Pitney Flats','PF',0,0,NULL,1,'5577 Highway 11E, Suite B','Piney Flats, Tennessee 37686','Phone (423)391-4330 - FAX (423)391-4332'),
 (2,'Knoxville','KV',0,0,NULL,1,'6134 Industrial Heights Drive','Knoxville, Tennessee 37909','Phone (865)584-3267  - FAX (865)584-9641'),
 (3,'Chattanooga','CH',0,0,NULL,1,'3908 Tennessee Ave., Suite J','Chattanooga, Tennessee 37409','Phone (423)822-9309  - Fax (423)822-3400');
/*!40000 ALTER TABLE `codivision` ENABLE KEYS */;


--
-- Definition of table `codivisionposting`
--

DROP TABLE IF EXISTS `codivisionposting`;
CREATE TABLE `codivisionposting` (
  `coDivisionPostingID` int(11) NOT NULL AUTO_INCREMENT,
  `coDivisionID` int(11) DEFAULT '0',
  `coAccountPostID` int(11) DEFAULT '0',
  `coAccountAlternateID` int(11) DEFAULT '0',
  PRIMARY KEY (`coDivisionPostingID`),
  KEY `coAccountAlternateID` (`coAccountAlternateID`),
  KEY `coAccountPostID` (`coAccountPostID`),
  KEY `coDivisionID` (`coDivisionID`),
  KEY `coDivisionPostingID` (`coDivisionPostingID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `codivisionposting`
--

/*!40000 ALTER TABLE `codivisionposting` DISABLE KEYS */;
/*!40000 ALTER TABLE `codivisionposting` ENABLE KEYS */;


--
-- Definition of table `cofiscalperiod`
--

DROP TABLE IF EXISTS `cofiscalperiod`;
CREATE TABLE `cofiscalperiod` (
  `coFiscalPeriodID` int(11) NOT NULL AUTO_INCREMENT,
  `coFiscalYearID` int(11) DEFAULT '0',
  `StartDate` datetime DEFAULT NULL,
  `EndDate` datetime DEFAULT NULL,
  `StartNewQuarter` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`coFiscalPeriodID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cofiscalperiod`
--

/*!40000 ALTER TABLE `cofiscalperiod` DISABLE KEYS */;
INSERT INTO `cofiscalperiod` (`coFiscalPeriodID`,`coFiscalYearID`,`StartDate`,`EndDate`,`StartNewQuarter`) VALUES 
 (3,2,'2005-01-01 00:00:00','2005-01-31 00:00:00',0);
/*!40000 ALTER TABLE `cofiscalperiod` ENABLE KEYS */;


--
-- Definition of table `cofiscalyear`
--

DROP TABLE IF EXISTS `cofiscalyear`;
CREATE TABLE `cofiscalyear` (
  `coFiscalYearID` int(11) NOT NULL AUTO_INCREMENT,
  `StartDate` datetime DEFAULT NULL,
  `EndDate` datetime DEFAULT NULL,
  PRIMARY KEY (`coFiscalYearID`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cofiscalyear`
--

/*!40000 ALTER TABLE `cofiscalyear` DISABLE KEYS */;
INSERT INTO `cofiscalyear` (`coFiscalYearID`,`StartDate`,`EndDate`) VALUES 
 (2,'2005-01-01 00:00:00','2005-12-31 00:00:00');
/*!40000 ALTER TABLE `cofiscalyear` ENABLE KEYS */;


--
-- Definition of table `coledgerdetail`
--

DROP TABLE IF EXISTS `coledgerdetail`;
CREATE TABLE `coledgerdetail` (
  `coLedgerDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `coLedgerHeaderID` int(11) DEFAULT '0',
  `coAccountID` int(11) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`coLedgerDetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coledgerdetail`
--

/*!40000 ALTER TABLE `coledgerdetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `coledgerdetail` ENABLE KEYS */;


--
-- Definition of table `coledgerdetailworkfile`
--

DROP TABLE IF EXISTS `coledgerdetailworkfile`;
CREATE TABLE `coledgerdetailworkfile` (
  `coLedgerDetailWorkFile` int(11) NOT NULL AUTO_INCREMENT,
  `Number` varchar(12) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Debit` decimal(19,4) DEFAULT '0.0000',
  `Credit` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`coLedgerDetailWorkFile`),
  UNIQUE KEY `Number` (`Number`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coledgerdetailworkfile`
--

/*!40000 ALTER TABLE `coledgerdetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `coledgerdetailworkfile` ENABLE KEYS */;


--
-- Definition of table `coledgerheader`
--

DROP TABLE IF EXISTS `coledgerheader`;
CREATE TABLE `coledgerheader` (
  `coLedgerHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `coFiscalPeriodID` int(11) DEFAULT '0',
  `coLedgerSourceID` int(11) DEFAULT NULL,
  `SourceNo` tinyint(4) DEFAULT '0',
  `TransactionReferenceID` int(11) DEFAULT NULL,
  `PostDate` datetime DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Reference` varchar(20) DEFAULT NULL,
  `Posted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`coLedgerHeaderID`),
  KEY `coFiscalPeriodID` (`coFiscalPeriodID`),
  KEY `coLedgerSourceID` (`coLedgerSourceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coledgerheader`
--

/*!40000 ALTER TABLE `coledgerheader` DISABLE KEYS */;
/*!40000 ALTER TABLE `coledgerheader` ENABLE KEYS */;


--
-- Definition of table `coledgerpostingerrors`
--

DROP TABLE IF EXISTS `coledgerpostingerrors`;
CREATE TABLE `coledgerpostingerrors` (
  `coLedgerPostingErrorsID` int(11) NOT NULL AUTO_INCREMENT,
  `coLedgerSourceID` int(11) DEFAULT NULL,
  `SourceNo` tinyint(4) DEFAULT '0',
  `TransactionReferenceID` int(11) DEFAULT NULL,
  `PostDate` datetime DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Reference` varchar(15) DEFAULT NULL,
  `ErrorDescription` varchar(50) DEFAULT NULL,
  `CriticalError` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`coLedgerPostingErrorsID`),
  KEY `coLedgerSourceID` (`coLedgerSourceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coledgerpostingerrors`
--

/*!40000 ALTER TABLE `coledgerpostingerrors` DISABLE KEYS */;
/*!40000 ALTER TABLE `coledgerpostingerrors` ENABLE KEYS */;


--
-- Definition of table `coledgersource`
--

DROP TABLE IF EXISTS `coledgersource`;
CREATE TABLE `coledgersource` (
  `coLedgerSourceID` int(11) NOT NULL DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`coLedgerSourceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `coledgersource`
--

/*!40000 ALTER TABLE `coledgersource` DISABLE KEYS */;
INSERT INTO `coledgersource` (`coLedgerSourceID`,`Description`) VALUES 
 (1300,'Journal Entry'),
 (2210,'Bank Transaction'),
 (4220,'Customer Invoices'),
 (4230,'Customer Payments'),
 (6220,'Vendor Bills');
/*!40000 ALTER TABLE `coledgersource` ENABLE KEYS */;


--
-- Definition of table `convert_coa`
--

DROP TABLE IF EXISTS `convert_coa`;
CREATE TABLE `convert_coa` (
  `Account` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `convert_coa`
--

/*!40000 ALTER TABLE `convert_coa` DISABLE KEYS */;
INSERT INTO `convert_coa` (`Account`,`Description`) VALUES 
 ('xxx','xxx'),
 ('101500','Cash on Hand'),
 ('102000','Home Federal Bank'),
 ('110000','ACCOUNTS RECEIVABLE'),
 ('117000','INVENTORY'),
 ('123000','EQUIPMENT & MACHINARY'),
 ('123200','EQUIP. & MACHINERY CHATTANOOGA'),
 ('123100','EQUIPMENT & MACHINERY - Tri-City'),
 ('122000','COMPUTER/COPIER/FAX'),
 ('122200','COMPUTER/COPIER/FAX  CHATT'),
 ('122100','COMPUTER/COPIER/FAX-Tri-City'),
 ('121000','FURNITURE/FIXTURES'),
 ('121002','FURNITURE/FIXTURES-CHATT'),
 ('121001','FURNITURE/FIXTURES - Tri-City'),
 ('125000','AUTOS/TRUCKS'),
 ('125100','AUTO & TRUCK - Piney Flats'),
 ('125000','BUILDINGS'),
 ('125010','BUILDING IMPROVEMENTS'),
 ('120000','LAND'),
 ('130000','ACCUMULATED DEPRECIATION'),
 ('111000','EMPLOYEE LOAN'),
 ('111100','Note Receivable Off - RG, Jr.'),
 ('171000','Rent Deposit - Tri-City'),
 ('114500','ADVANCE ON COMMISSIONS - Linda G.'),
 ('119000','CORPORATE TAX DEPOSITS'),
 ('170000','LIFE INSURANCE CASH VALUE'),
 ('200000','ACCOUNTS PAYABLE'),
 ('204000','Sales Tax'),
 ('203000','Payroll Tax Payable'),
 ('202000','UNEMPLOYMENT TAXES PAYABLE'),
 ('205000','CORPORATE TAX PAYABLE'),
 ('211200','Note Payable - Stock RG, Sr.'),
 ('211300','Note Payable - Suburban Jeff'),
 ('211400','Note Payable - Yukon Bob'),
 ('206500','Note Payable - Current'),
 ('206400','Note Payable - Home Federal LOC'),
 ('213000','LESS: CURRENT PORTION'),
 ('301500','CAPTIAL STOCK ISSUED'),
 ('306500','TREASURY STOCK'),
 ('302500','RETAINED EARNINGS'),
 ('400000','SALES'),
 ('401000','SALES-VENDOR COMMISSION REC\'D'),
 ('402000','FINANCE CHARGES'),
 ('400400','FREIGHT'),
 ('403000','DISCOUNTS RECEIVABLES'),
 ('500000','BEGINNING INVENTORY'),
 ('501000','PURCHASES'),
 ('502000','FREIGHT'),
 ('590000','ENDING INVENTORY'),
 ('605000','ADVERTISING - Knoxville'),
 ('605100','Advertising - Chattanooga'),
 ('605200','ADVERTISING - Tri-City'),
 ('607500','BANK CHARGES'),
 ('606000','Auto'),
 ('606100','Auto - RLG, JR'),
 ('606200','Auto - JRG'),
 ('610000','COMMISSIONS'),
 ('610800','COMPUTER Expense'),
 ('609000','Casual Labor'),
 ('613000','DEPRECIATION'),
 ('611000','Contributions - Knoxville'),
 ('611500','Contributions - Tri-City'),
 ('611200','Contributions - Chattanooga'),
 ('615000','DUES & SUBSCRIPTIONS - Knoxville'),
 ('615100','DUES & SUBSCRIPTIONS - Chattanooga'),
 ('615200','DUES & SUBSCRIPTIONS - Piney Flats'),
 ('617000','Freight'),
 ('625000','Meals & Entertainment - Knoxville'),
 ('625100','Meals & Entertainment - Chattanooga'),
 ('625200','Meals & Entertainment - Tri-City'),
 ('800600','Admin Exp Reimb - Jerry'),
 ('800700','Admin Exp Reimb - Jack'),
 ('800800','Admin Exp Reimb - Linda'),
 ('619000','INSURANCE'),
 ('619200','Insurance - Life'),
 ('620000','LEASE (USBANCORP-COPIER)'),
 ('633000','Repairs & MAINTENANCE - Knoxville'),
 ('633100','Repairs & MAINTENANCE - Chattanooga'),
 ('633200','Repairs & MAINTENANCE - Tri-City'),
 ('627000','OFFICE EXPENSE - Knoxville'),
 ('627100','OFFICE EXPENSE - Chattanooga'),
 ('627200','OFFICE EXPENSE - Piney Flats'),
 ('629500','Penalties & Fines'),
 ('630000','POSTAGE - Knoxville'),
 ('630100','POSTAGE - Piney Flats'),
 ('630500','PROFESSIONAL FEES'),
 ('623000','Legal & Accounting Fees'),
 ('631000','RENT - Chattanooga'),
 ('631100','RENT - Piney Flats'),
 ('616000','Equipment Rental - Knoxville'),
 ('616100','Equipment Rental - Chattanooga'),
 ('616200','Equipment Rental - Tri-City'),
 ('629000','Payroll Taxes'),
 ('629050','Payroll Taxes Unemployment'),
 ('638000','Seminars & Shows'),
 ('643000','TELEPHONE - Knoxville'),
 ('643200','TELEPHONE - CHATTANOOGA'),
 ('643100','TELEPHONE - Tri-City'),
 ('645000','TRAVEL - Knoxville'),
 ('645050','TRAVEL - Chattanooga'),
 ('645100','TRAVEL - Tri-City'),
 ('649000','UTILITIES - Knoxville'),
 ('649200','UTILITIES - CHATTANOOGA'),
 ('649100','UTILITIES - Piney Flats'),
 ('635000','Salaries - Officers'),
 ('637000','Salaries - Others'),
 ('637400','Security Expense'),
 ('641000','Taxes & Licenses'),
 ('613000','DEPRECIATION'),
 ('621000','INTEREST EXPENSE'),
 ('621100','INTEREST EXP SALES PERSONS'),
 ('801000','INTEREST INCOME'),
 ('801100','DISCOUNTS A/P'),
 ('801500','Vendors Comp. - Sales Tax'),
 ('803000','GAIN(LOSS) ON SALE OF ASSET'),
 ('990000','PROVISION FOR TAXES'),
 ('990100','Suspense');
/*!40000 ALTER TABLE `convert_coa` ENABLE KEYS */;


--
-- Definition of table `convert_customers`
--

DROP TABLE IF EXISTS `convert_customers`;
CREATE TABLE `convert_customers` (
  `Name` varchar(255) DEFAULT NULL,
  `Address1` varchar(255) DEFAULT NULL,
  `Address2` varchar(255) DEFAULT NULL,
  `CityStateZip` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `State` varchar(255) DEFAULT NULL,
  `Zip` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `Fax` varchar(255) DEFAULT NULL,
  `Attention` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `convert_customers`
--

/*!40000 ALTER TABLE `convert_customers` DISABLE KEYS */;
INSERT INTO `convert_customers` (`Name`,`Address1`,`Address2`,`CityStateZip`,`City`,`State`,`Zip`,`Phone`,`Fax`,`Attention`) VALUES 
 ('Associated Mechanical','3315 Curtis St',NULL,NULL,'Chattanooga','TN','37406','4236222598',NULL,'Craig'),
 ('Eagle Mechanical','1500 E 40th St',NULL,NULL,'Chattanooga','TN','37407',NULL,NULL,NULL),
 ('King Industries','3945 Cromwell Road',NULL,NULL,'Chattanooga','TN','37421',NULL,NULL,NULL),
 ('Davis Heating','2420 Gutherie Ave.',NULL,NULL,'Cleveland','TN','37312',NULL,NULL,NULL),
 ('Callahan Mechanical','2811 8th Ave.',NULL,NULL,'Chattanooga','TN','37407',NULL,NULL,NULL),
 ('Jake Marshall Service','611 West Manning St.',NULL,NULL,'Chattanooga','TN','37405',NULL,NULL,NULL),
 ('Tapp Heating & A/C','774 Cross St.',NULL,NULL,'Rossville','GA','30741',NULL,NULL,NULL),
 ('Mechanical Systems','460 Market Street',NULL,NULL,'Charleston','TN','37310',NULL,NULL,NULL),
 ('Cleveland Metalworks','625 Mimosa Drive NW',NULL,NULL,'Cleveland','TN','37312',NULL,NULL,NULL),
 ('Goins Heat & Air','132 Pikeville Ave.',NULL,NULL,'Graysville','TN','37338',NULL,NULL,NULL),
 ('Jerry\'s Electric Motor Service','433 Broad Street SW',NULL,NULL,'Cleveland','TN','37320',NULL,NULL,NULL),
 ('Hobbs & Associates','774 Finn Drive',NULL,NULL,'Chattanooga','TN','37412',NULL,NULL,NULL),
 ('DWK Enterprises','6200 F Highway 58',NULL,NULL,'Harrison','TN','37343',NULL,NULL,NULL),
 ('BP Mechanical','1516 East Main Street',NULL,NULL,'Chattanooga','TN','37404','4236291496',NULL,'Charlie Addison'),
 ('A-Tech','1401  Dodds Avenue',NULL,NULL,'Chattanooga','TN','37404','4238708322',NULL,'Cleo'),
 ('Rheaco','174 Cemetary Road',NULL,NULL,'Dayton','TN','37321','4237756513',NULL,NULL),
 ('Carter Heating & A/C','3903 Volunteer Dr., Bldg B',NULL,NULL,'Chattanooga','TN','37416','4233449031',NULL,NULL),
 ('Conerstone','4300 N. Access Rd,m Suite A',NULL,NULL,'Chattanooga','TN','37415',NULL,NULL,NULL),
 ('Jake Marshall Sheet Metal','2906 S. Hickory Street',NULL,NULL,'Chattanooga','TN','37407','4236986312',NULL,NULL),
 ('Tech Mechanical','4563 Pinnacle Lane',NULL,NULL,'Chattanooga','TN','37415',NULL,NULL,NULL),
 ('ED\'s Supply','3011 South Hickory St',NULL,NULL,'Chattanooga','TN','37407',NULL,NULL,NULL),
 ('Johnstone Supply','2100 South Holley',NULL,NULL,'Chattanooga','TN','37404','4236974908',NULL,NULL),
 ('C. C Dickson','434 S. Broad Street',NULL,NULL,'Cleveland','TN','37311','4234794521',NULL,NULL),
 ('Air Alliance','1800 S. Greenwood',NULL,NULL,'Chattanooga','TN','37404',NULL,NULL,NULL),
 ('Chase Plumbing','6112 Airways Blvd.',NULL,NULL,'Chattanooga','TN','37421',NULL,NULL,NULL),
 ('Chattanooga Clarion Hotel','407 Chestnut Street',NULL,NULL,'Chattanooga','TN','37402',NULL,NULL,'Jamie Dockery'),
 ('Byrd\'s Electric Motor Service','2193 Waterlevel Highway',NULL,NULL,'Cleveland','TN','37311',NULL,NULL,NULL),
 ('Anchor Glass Container','4108 Valley Industrial Blvd.',NULL,NULL,'Shakopee','MN','55379',NULL,NULL,NULL),
 ('Duncan Electric','1801 E. 23rd St.',NULL,NULL,'Chattanooga','TN','37404',NULL,NULL,NULL),
 ('Woody\'s Service Co.','1384 D Gunbarrel Rd.',NULL,NULL,'Chattanooga','TN','37421',NULL,NULL,NULL),
 ('General Heating & Air Conditioning','1412 Gunbarrel Road',NULL,NULL,'Chattanooga','TN','37421','4238998861',NULL,'Wendell'),
 ('Construction Consultants','1416 Fort Street',NULL,NULL,'Chattanooga','TN','37402','4232654131',NULL,NULL),
 ('Southeast Company','4000 7th AVE.',NULL,NULL,'Chattanooga','TN','37407',NULL,NULL,NULL),
 ('S & K Heating & A/C','1148 State Highway 68 West',NULL,NULL,'Decatur','TN','37322','4233341425',NULL,NULL),
 ('Helton Construction','4109 Mountain View Road',NULL,NULL,'Chattanooga','TN','37415','4238758850',NULL,NULL),
 ('St.John\'s United Methodist Church','3921 Murray Hills Drive',NULL,NULL,'Chattanooga','TN','37416',NULL,NULL,'Jim Humburger'),
 ('JDC Industrial Mechanical','3610 Steeplechase Lane',NULL,NULL,'Cleveland','TN','37323',NULL,NULL,NULL),
 ('United Enertech','3101 S. Orchard Knob Ave.',NULL,NULL,'Chattanooga','TN','37407',NULL,NULL,NULL),
 ('Chattanooga Mechanical Contractors','134 Morrison Lane',NULL,NULL,'Chickamauga','GA','30707','4235939282',NULL,NULL),
 ('Triad Corporation','1007 East Main Street',NULL,NULL,'Chattanooga','TN','37408','4232672288',NULL,'Vicki'),
 ('Home Depot Supply','4162 B South Creek Road',NULL,NULL,'Chattanooga','TN','37406','4236244500',NULL,NULL),
 ('C. C. Dickson','1600 E. 20th Street',NULL,NULL,'Chattanooga','TN','37404','4236986948',NULL,NULL),
 ('Action Air & Electric','1406 Boyscout Road',NULL,NULL,'Hixson','TN','37343','4238433090',NULL,NULL),
 ('Chickamauga Schools','402 Cove Road',NULL,NULL,'Chickamauga','GA','30707',NULL,NULL,NULL),
 ('Chattanooga Free Press','400 East 11th Street',NULL,NULL,'Chattanooga','TN','37403',NULL,NULL,NULL),
 ('O\'Neal Heating & A/C',NULL,NULL,NULL,'Ft. Oglethorpe','GA','30742','4234139017',NULL,'James O\'Neal'),
 ('Reliable Heating & Air','404 Spears Avenue',NULL,NULL,'Chattanooga','TN','37405','423266-242',NULL,NULL),
 ('First Seventh Day Adventist','1500 East 40th Street',NULL,NULL,'Chattanooga','TN','37407','4235937448',NULL,'Suzanne Hayes'),
 ('Malone Heating & Air Conditioning','2021 Watauga St.',NULL,NULL,'Chattanooga','TN','37404',NULL,NULL,NULL),
 ('Southern Adventist University','P. O. Box 370',NULL,NULL,'Collegedale','TN','37315',NULL,NULL,NULL),
 ('Bud Goins','Sale Creek Mountain',NULL,NULL,'Sales Creek','TN','37338',NULL,NULL,NULL),
 ('Central Baptist Church','5208 Hixson Pike',NULL,NULL,'Hixson','TN','37343','4238776462',NULL,'Susan J. Marshall'),
 ('Ringgold Custom HVAC','Battlefield Parkway',NULL,NULL,'Ringgold','GA','30743','7069657755',NULL,NULL),
 ('Pannell Mechanical Inc.','4639 Dayton Blvd',NULL,NULL,'Chattanooga','TN','37415',NULL,NULL,NULL),
 ('Thurman Bryant Electric Supply','309 W 21st Street',NULL,NULL,'Chattanooga','TN','37408',NULL,NULL,NULL),
 ('GSI','4738 Wesleyan RD  SW',NULL,NULL,'Cleveland','TN','37311',NULL,NULL,NULL),
 ('Alstom Power','1119 Riverfront Parkway',NULL,NULL,'Chattanooga','TN','37402',NULL,NULL,NULL),
 ('Commercial Air Solutions','Greenwood Avenue',NULL,NULL,'Chattanooga','TN','37407','4235952025',NULL,NULL),
 ('Dade County Board of Education','62 Traditions Lane',NULL,NULL,'Trenton','GA','30752',NULL,NULL,NULL),
 ('Glasscock International','3908 Tennessee Ave, Suite A',NULL,NULL,'Chattanooga','TN','37409',NULL,NULL,NULL),
 ('Accurate Mechanical',NULL,NULL,NULL,'Dalton','GA','30721','7062793200',NULL,NULL),
 ('Pikeville Church of God','1324 Main Street',NULL,NULL,'Pikeville','TN','37367','4234476563',NULL,NULL),
 ('McMahan Mechanical','6549 Creekhead Road',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,''),
 ('Shoffner Mechanical & Industrial','3600 Papermill Rd',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,'Don Davis'),
 ('Johnson Controls','6101 Industrial Heights Dr.',NULL,NULL,'Knoxville','TN','37909-    ','4235881197',NULL,''),
 ('Four Seasons Comfort','2109 Dutch Valley Rd.',NULL,NULL,'Knoxville','TN','37918-    ','4232199886',NULL,'David Burnet'),
 ('Colvin & Son Heating','149 Aluminum Ave.',NULL,NULL,'Alcoa','TN','37701-    ','',NULL,'Mike Henderson'),
 ('John H. Coleman, Co.','814 State St.',NULL,NULL,'Knoxville','TN','37902-    ','4235255111',NULL,''),
 ('Chancey & Reynolds, Inc.','614 Van St.',NULL,NULL,'Knoxville','TN','37921-    ','4235255076',NULL,'Sam Talbott'),
 ('Maintenance Tech','3500 Seasonal Way',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,''),
 ('Air Comfort Supply','2317 Johnston Street',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('Air Quest America','9521 Maynardville Hwy',NULL,NULL,'Maynardville','TN','37807-    ','',NULL,''),
 ('Alcoa Mechanical','3333 Regal Dr',NULL,NULL,'Alcoa','TN','37701-    ','',NULL,'Tim Hurst'),
 ('Applied Engineering Solutions','706 Walnut Street',NULL,NULL,'Knoxville','TN','37902-    ','',NULL,''),
 ('Archer Air Conditioning Co.','2710 Bond Street',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Armstrong Heating & air','4121 Topeka St.',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Associated Refrigeration Services','3412 Johnson Road',NULL,NULL,'Knoxville','TN','37931-    ','',NULL,''),
 ('Blaine Construction Co','6510 Deane Hill Drive',NULL,NULL,'Knoxville','TN','37919-    ','8656938900',NULL,'Jon Searle'),
 ('Carroll Blalock Heating & A/C','872 Proffits Springs Road',NULL,NULL,'Maryville','TN','37801-    ','',NULL,''),
 ('Carroll Heating & Air','2903 W. Beaver Creek Dr.',NULL,NULL,'Powell','TN','37849-    ','',NULL,''),
 ('City Heating & A/C','5502 Middlebrook Pike',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('Community Tectonics Inc.','105 N. Concord St.  Ste 200',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,''),
 ('Consolidated Electronics, Inc.','746 N. 5th Avenue',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Cooks Htg & A/C','420 Robertsville Rd.',NULL,NULL,'Oak Ridge','TN','37830-    ','',NULL,''),
 ('Dallas Heating & A/C','1120 Triple Crown Blvd.',NULL,NULL,'Knoxville','TN','37922-    ','',NULL,''),
 ('Del-Air Service Co., Inc.','135 Chickamauga Avenue',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,'Terry Golden'),
 ('East Tn. Gas Product','718 East Deport Ave.',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Efficient Electric','P.O. Box 258',NULL,NULL,'Knoxville','TN','37901-    ','',NULL,''),
 ('Engert Plumbing & Heating Co.','1715 Riverside Drive',NULL,NULL,'Knoxville','TN','37915-    ','',NULL,''),
 ('Engineering Services Group','900 E. Hill Avenue,  Suite 350',NULL,NULL,'Knoxville','TN','37915-    ','8655220393',NULL,''),
 ('Ferguson Enterprises, Inc.','6422A Deane Hill Dr.',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,''),
 ('Ferguson  Equipment Co','1301 Hannah Avenue',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('J.A. Fielden Co.','P.O. Box 3278',NULL,NULL,'Knoxville','TN','37917-3278','',NULL,''),
 ('Flatt Plumbing Co.','5507 Ball Camp Pike',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('Four Seasons Heating & A/C','756 Baugh Lane',NULL,NULL,'Abingdon','VA','24210-    ','',NULL,''),
 ('Foust Metal Works, Inc.','S. Economy Rd.  Box 521',NULL,NULL,'Morristown','TN','37814-    ','',NULL,''),
 ('Fritts Refrigeration','217 Beechwood Land',NULL,NULL,'Clinton','TN','37716-    ','',NULL,''),
 ('Henshaw\'s Heating','1512 Wright Street',NULL,NULL,'Knoxville','TN','37927-7465','',NULL,''),
 ('Hicks & Ingle Corp.','8909 Joe Daniels Road',NULL,NULL,'Knoxville','TN','37931-    ','',NULL,'Jay McMahan'),
 ('Higginbotham Htg. & A/C','1700 Branner',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Hi-Tech Heat & Air','106 S. Second St.',NULL,NULL,'Lake City','TN','37769-    ','',NULL,''),
 ('Interstate Mechanical Contractors','3200 Henson Road',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,'Dan Carlson'),
 ('J & F Heating','Route 4 Box 332',NULL,NULL,'Rutledge','TN','37861-    ','',NULL,'Eddie'),
 ('Johnson & Galyon, Inc.','1130 Atlantic Ave',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,'Project Estimator'),
 ('Trane Company','5220 South Middlebrook Pike',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,'Wayne Doane'),
 ('KBM','3511 Overlook Circle',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,''),
 ('Kalthoff, Inc.','1010 Wilder Place',NULL,NULL,'Knoxville','TN','37915-    ','8655223106',NULL,'Shaun Turner'),
 ('M & A Supply','1500 Galway Street',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,''),
 ('Mechanical Services','1010 Wilder Place',NULL,NULL,'Knoxville','TN','37915-    ','',NULL,''),
 ('Modern Supply Co','P.O. Box 22997',NULL,NULL,'Knoxville','TN','37922-    ','',NULL,''),
 ('Newcomb Machine & Welding Co.','2172 Tennessee Ave.',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('Roger L. Newman Co.','1100 Montvale Road',NULL,NULL,'Maryville','TN','37801-    ','',NULL,''),
 ('Payne Heating & Cooling','P.O. Box 736',NULL,NULL,'Wartburg','TN','37887-    ','',NULL,''),
 ('Rankin Electrical Const. Co., Inc.','542 S. Commerce St.',NULL,NULL,'Marion','VA','24354-    ','',NULL,'Brent Harrell'),
 ('George W. Reagan Co., Inc.','1700 Grainger Ave.',NULL,NULL,'Knoxville','TN','37927-    ','',NULL,''),
 ('Rochat\'s Htg & A/C Service','383 Nash Road, N.E.',NULL,NULL,'Knoxville','TN','37914-    ','',NULL,''),
 ('Russell & Abbott','116 Nicole Court',NULL,NULL,'Maryville','TN','37801-    ','',NULL,'Cecil Hopkins'),
 ('Temperature Control, Inc.','4335 Anderson Road',NULL,NULL,'Knoxville','TN','37928-    ','',NULL,''),
 ('Tobler Air Conditioning Service, Inc.','10932 Murdock Rd.',NULL,NULL,'Knoxville','TN','37922-    ','',NULL,''),
 ('Union Controls, Inc.','3505 John Sevier Highway',NULL,NULL,'Knoxville','TN','37920-    ','',NULL,''),
 ('Hobbs & Associates, Inc.','11064 Terrapin Station Ln',NULL,NULL,'Knoxville','TN','37932-    ','8657770817',NULL,'Bill Jacoby'),
 ('Volunteer  Mechanical','527 Callahan Drive',NULL,NULL,'Knoxville','TN','37912-    ','',NULL,'Heath'),
 ('Air Alliance','4028 Papermill Road  Ste 10',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,''),
 ('Weather Control','1405 East Broadway',NULL,NULL,'Maryville','TN','37801-    ','',NULL,''),
 ('Webb Plumbing & Heating Electric','P.O. Box 847',NULL,NULL,'Athens','TN','37303-    ','',NULL,''),
 ('West, Welch, Reed','5417 Ball Camp Pike',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,''),
 ('Winnie\'s Hi-Tech Htg. & A/C','111 Garfield Lane',NULL,NULL,'Rockwood','TN','37854-    ','',NULL,'Ray'),
 ('Carrier Corp','284 Carrier Dr',NULL,NULL,'Morrison','TN','37357-    ','',NULL,''),
 ('Denso Manufacturing TN Inc','1720 Robert C Jackson Dr',NULL,NULL,'Maryville','TN','37801-    ','',NULL,''),
 ('Kimberly-Clark Corp','5899 Sugerlimb Road',NULL,NULL,'Loudon','TN','37774-    ','',NULL,'PO#'),
 ('Pilot Oil Corp.','P.O. Box  10146',NULL,NULL,'Knoxville','TN','37939-0146','4235887487',NULL,'Karen Simmons'),
 ('U. T. Hospital','1924  Alcoa Hwy.',NULL,NULL,'Knoxville','TN','37920-    ','4235448848',NULL,'Harry Whetsell'),
 ('Shoffner Service Co.','3400 Division St.',NULL,NULL,'Knoxville','TN','37919-    ','4235216948',NULL,'David Riordan'),
 ('DBS','',NULL,NULL,'Chattanooga','TN',NULL,'',NULL,''),
 ('Technical Air Supply & Electric','4610 Old Broadway',NULL,NULL,'Knoxville','TN','37918-    ','689-5490',NULL,'Don Reynolds'),
 ('Guignard Mechanical','2190 Dutch Valley Rd.',NULL,NULL,'Knoxville','TN','37918-    ','4232199886',NULL,'Eddie Harmon'),
 ('Rome, Eddleman & Assoc.',NULL,NULL,NULL,'Knoxville','TN','37932-    ','',NULL,'Myron Carter'),
 ('U. T. Physical Plant','2233 Volunteer Blvd',NULL,NULL,'Knoxville','TN','37996-    ','',NULL,''),
 ('Oak Ridge National Laboratory','Environmental Sciences Division',NULL,NULL,'Oak Ridge','TN','37831-    ','',NULL,''),
 ('American Refrigeration Sales & Service','317 King St.',NULL,NULL,'Knoxville','TN','37917-    ','4235467869',NULL,''),
 ('Kelso-Regen','6709 Kingston Pike',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,''),
 ('Richard A. Nix Htg. & A/C','6939 Pennell Lane',NULL,NULL,'Knoxville','TN','37931-    ','',NULL,'Allen Nix'),
 ('Harris & Swicegood','10323 Starkey Lane',NULL,NULL,'Knoxville','TN','37932-    ','',NULL,''),
 ('Control Equip Inc','2044 E Magnolia Ave',NULL,NULL,'Knoxville','TN','37917-    ','',NULL,'Walt'),
 ('Gatlinburg Electric & Construction Co.','316 Amolee Ln.',NULL,NULL,'Sevierville','TN','37876-    ','8654530358',NULL,'Dallas Atchley'),
 ('Cherokee Millwright','1034 Ross Drive',NULL,NULL,'Maryville','TN','37802-    ','4233791500',NULL,'Heath Boggs'),
 ('Morristown Mechanical,LLC','5063-2 West Andrew Johnson Hwy.',NULL,NULL,'Morristown','TN','37814-    ','4233810000',NULL,'Denise'),
 ('Air Technologies','1140 Rocktown Rd',NULL,NULL,'Talbot','TN','37877-    ','8654710031',NULL,'Robert Allen'),
 ('Fort Sanders Park West ','9352 Park West Blvd.',NULL,NULL,'Knoxville','TN','37923-    ','',NULL,'Barbara Parkerson'),
 ('Premier Sales , Inc.','8239 West Darryl Drive',NULL,NULL,'Baton Rouge','LA','70815-8031','5049276140',NULL,'Jeff A. Hudson'),
 ('Boiler Supply',NULL,NULL,NULL,'Knoxville','TN','37900-    ','',NULL,'John'),
 ('Moye Electreic Co.','',NULL,NULL,'Dublin','GA','31021-    ','9122759054',NULL,'Mike'),
 ('Four Seasons Mech. Contractors','718 R Willoford Rd.',NULL,NULL,'Dandridge','TN','37725-    ','',NULL,'Mike Loveday'),
 ('City Of Oak Ridge','100 Wood bury Lane',NULL,NULL,'Oak Ridge','TN','37830-    ','4232201849',NULL,'Ray Crawhorn'),
 ('Air Tech Service Co.','2332 West Beaver Creek Rd',NULL,NULL,'Powell','TN','37849-    ','4239387826',NULL,'Randy Standberry'),
 ('Industrial Belting & Supply','203 West Depot Ave',NULL,NULL,'Knoxville','TN','37917-    ','4235258133',NULL,'Terry'),
 ('Boiler Equipment Co., Inc.','',NULL,NULL,'Knoxville','TN','37917-    ','4235250771',NULL,''),
 ('Pierce Construction Co','620 Wall Street',NULL,NULL,'Sevierville','TN','37864-    ','',NULL,'Jerry Pierce'),
 ('UT Vet School','',NULL,NULL,'Knoxville','TN','37900-    ','',NULL,'Scott Wyrick'),
 ('Crossley Construction','PO Box 23155',NULL,NULL,'Knoxville','TN','37933-    ','',NULL,'Hogue Crossley'),
 ('Denark Construction','1635 Western Ave #105',NULL,NULL,'Knoxville','TN','37921-    ','',NULL,'Project Estimator'),
 ('Hardaway Construction','6701 Baum Drive #218',NULL,NULL,'Knoxville','TN','37919-7362','',NULL,'Project Estimator'),
 ('CMC Construction','170 Raliegh Rd',NULL,NULL,'Oak Ridge','TN','37830-5040','',NULL,'Project Estimator'),
 ('Webb & Sons Construction','PO Box 388',NULL,NULL,'Athens','TN','37371-    ','',NULL,'Project Estimator'),
 ('Rentenbach Constructors Inc','2400 Sutherland Ave',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,'Project Estimator'),
 ('Ray Bell Construction','PO Box 363',NULL,NULL,'Brentwood','TN','37024-    ','',NULL,'Project Estimator'),
 ('Brownlee Construction Inc','PO Box 59001',NULL,NULL,'Knoxville','TN','37950-    ','',NULL,'Project Estimator'),
 ('Construction Design Management','104 North A St',NULL,NULL,'Lenoir City','TN','37771-    ','',NULL,'Project Estimator'),
 ('Lambert Construction Co','650 Poplar Springs Rd',NULL,NULL,'Loudon','TN','37774-    ','',NULL,'Project Estimator'),
 ('Harbin Construction','6408 Clinton Hwy',NULL,NULL,'Knoxville','TN','37912-    ','',NULL,'Project Estimator'),
 ('Cooks Comfort Systems','2538 Oliver Springs Rd.',NULL,NULL,'Oliver Springs','TN','37840-    ','',NULL,''),
 ('A A Air Company','1001 Katherine Ave.',NULL,NULL,'Knoxville','TN','37921-    ','689-5290',NULL,'Don Pirkle'),
 ('R & R Properties','105 Mitchel Rd, Suite 201',NULL,NULL,'Oak Ridge','TN','37830-    ','4234823602',NULL,'Steve Maughan'),
 ('McGaha Electric Co., Inc.','',NULL,NULL,'Knoxville','TN','37917-    ','5238373',NULL,''),
 ('Rogers & Morgan, Inc.','3516 Lyle Drive',NULL,NULL,'Knoxville','TN','37919-    ','8655241100',NULL,'Bill McConnell'),
 ('Construction Plus, Inc.','601 Reliability Circle',NULL,NULL,'Knoxville','TN','37932-3370','8656713711',NULL,'Project Estimator'),
 ('Southern Mechanical Services','1549 E Union Valley RD',NULL,NULL,'Powell','TN','37865-    ','',NULL,'John Peters'),
 ('Stokes Electric','1701 McCalla Ave',NULL,NULL,'Knoxville','TN','37915-    ','525-0351',NULL,'Dave'),
 ('Powell Valley Heating & Air',NULL,NULL,NULL,NULL,'TN',NULL,'4235621967',NULL,'Beverly'),
 ('C. C. Dickson - Sevierville',NULL,NULL,NULL,'Sevierville','TN',NULL,'8654531721',NULL,'Scott'),
 ('Cook Comfort Systems','103A Henley Road',NULL,NULL,'Oak Ridge','TN','37830-    ','4234813344',NULL,''),
 ('Penco Sales Co.','',NULL,NULL,'Knoxville','TN','37921-    ','8655842087',NULL,'Jon Pence'),
 ('Pioneer Heating & Air, Inc.','4604 Mill Branch Lane',NULL,NULL,'Knoxville','TN','37938-    ','4239222817',NULL,'Gordy Noe'),
 ('Howell Doka','',NULL,NULL,'Knoxville','TN','37909-    ','8655887599',NULL,'Howell'),
 ('Headrick Heating & Air','955 Blair Loop',NULL,NULL,'Walland','TN','37886-    ','8656811095',NULL,'Sam Headrick'),
 ('IT Corp','312 Directors Dr',NULL,NULL,'Knoxville','TN','37923-    ','8656903211',NULL,'Lee Latimer'),
 ('Carrier/Bryant-Tennessee','5900 Westbrook Lane',NULL,NULL,'Knoxville','TN','37950-0190','8565586336',NULL,'Scott Henry'),
 ('HBE Construction Co.','100 Rankin Dr.',NULL,NULL,'Marion','NC','28752-    ','',NULL,'Chancey & Reynolds'),
 ('Taylor Heating & Air','310 Melbourne Dr.',NULL,NULL,'Maryville','TN','37804-    ','4236819021',NULL,'Gary Taylor'),
 ('Comfort-Tech Heating & A.C.','P.O. Box 14549',NULL,NULL,'Knoxville','TN','37914-    ','8656736748',NULL,'David McCurry'),
 ('Comfort Tech','301 Iroquois Rd.',NULL,NULL,'Knoxville,','TN','37914-    ','8656736748',NULL,'David McCurry'),
 ('Treadway Brothers','2501 Maloney Rd',NULL,NULL,'Knoxville','TN','37920-    ','8655779000',NULL,'George Treadway'),
 ('Kesterson Construction','818 Belfast St',NULL,NULL,'Maryville','TN','37801-    ','8659849589',NULL,'Mark Frye'),
 ('Vasey Heating & Air Conditioning','',NULL,NULL,'Knoxville','TN','37922-    ','8659665155',NULL,'Brad'),
 ('Building Automation & Supply Co., LLC','',NULL,NULL,'Morristown','TN','37813-    ','4235811990',NULL,'John Knisley'),
 ('Butler Electric','806 Tusculum Blvd',NULL,NULL,'Greeneville','TN','37745-    ','4236390404',NULL,'Brian Root'),
 ('JMS Construction',NULL,NULL,NULL,'Knoxville','TN',NULL,'8653892242',NULL,'John M. Kiernan, Jr.'),
 ('Haynes Heating & Air','',NULL,NULL,'Greenback','TN','37742-    ','',NULL,''),
 ('Cherokee Electric Inc.','',NULL,NULL,'Knoxville','TN','37924-    ','8655239581',NULL,'Ralph Sheriod'),
 ('Valleywide Construction Inc','Suite 4',NULL,NULL,'Athens','TN','37303-    ','4237441311',NULL,'Richard Lehner'),
 ('All Weather Heating and Air',NULL,NULL,NULL,'Tazeweel','TN',NULL,'4236262837',NULL,'Howard Walker'),
 ('Fenco Supply',NULL,NULL,NULL,NULL,'TN',NULL,'4232821472',NULL,'Bob Wilson'),
 ('B & S Builders','',NULL,NULL,'Pigeon Forge','TN','37868-    ','8653883890',NULL,'Howard Sexton'),
 ('Wade & Associates, Inc.','4300 Papermill Dr',NULL,NULL,'Knoxville','TN','37909-    ','8655885577',NULL,'Harry Wade'),
 ('Southern Mechanical Service Corp','',NULL,NULL,'Knoxville','TN','37920-    ','8655796030',NULL,'Randy Atchley'),
 ('Miller Heating & A.C.','',NULL,NULL,'Crossville','TN','38555-    ','9314568474',NULL,'Johnathon Miller'),
 ('Morgan Construction','690 Manufacturers Dr',NULL,NULL,'Chattanooga','TN','37405-    ','4232666218',NULL,'Bill Lanham'),
 ('Carrier of Knoxville',NULL,NULL,NULL,'Knoxville','TN','37900-    ','8656338963',NULL,'Terry'),
 ('Maryville Refrigeration','',NULL,NULL,'Maryville','TN','37701-    ','',NULL,'David Nicholson'),
 ('Cumberland Htg. & Air','',NULL,NULL,'Harrogate','TN','37752-    ','4238698554',NULL,'Toni Seal'),
 ('Perfection Services of Tennessee','',NULL,NULL,'Knoxville','TN','37912-    ','8659384822',NULL,'Randy'),
 ('Pro Air','3815 West AJ Highway',NULL,NULL,'Morristown','TN','37814-    ','4235850044',NULL,'Mike Nies'),
 ('Brown & Brown General Contractors','3709 Martin Mill Pike',NULL,NULL,'Knoxville','TN','37920-    ','8655778477',NULL,'Travis'),
 ('Tennessee Armature & Electric','1301 Galway St',NULL,NULL,'Knoxville','TN','37917-    ','8655243681',NULL,'Jimmy'),
 ('Bud Lewis',NULL,NULL,NULL,'Knoxville','TN','37900-    ','8653890330',NULL,'Bud Lewis'),
 ('Jack Southard Company','278 Joe Byrd Lane',NULL,NULL,'Clinton','TN','37716-    ','8654570012',NULL,'Jack Southard'),
 ('Electric Motors Company','',NULL,NULL,'LaFollette','TN','37766-    ','4235629366',NULL,'Devery Dagley'),
 ('Noland Company','',NULL,NULL,'Knoxville','TN','37915-    ','525-7171',NULL,'Scott Ryan'),
 ('Roane State Community College','',NULL,NULL,'Oak Ridge','TN','37830-    ','',NULL,'Raink Hembree'),
 ('Valley Mechanical & Iron','',NULL,NULL,'White Pine','TN','37764-    ','',NULL,'Tony Watkins'),
 ('Ferguson Heating & Air Conditioning','',NULL,NULL,'Knoxville','TN','37909-    ','',NULL,''),
 ('Ferguson Mech.','',NULL,NULL,'Knoxville','TN','37918-    ','8656891100',NULL,''),
 ('Tennessee Fire & Safety','',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,'Richard Price'),
 ('Black & Snyder Heating A/C','4630 Ball Camp Pike',NULL,NULL,'Knoxville','TN','37921-    ','8655251084',NULL,'George McDoogle'),
 ('J. B. Stennitt Htg. & A.C.','',NULL,NULL,'Newport','TN','37821-    ','4236238696',NULL,'J.B. Stennitt'),
 ('Johnstone Supply','3801 Western Ave',NULL,NULL,'Knoxville','TN','37921-    ','8655256296',NULL,'Jason'),
 ('BWXT Y-12 L.L.C.','Bldg. 9734S, MS 8130',NULL,NULL,'Oak Ridge','TN','37830-    ','8655742854',NULL,'Andy McLaughlin'),
 ('Cureton - Hodges','',NULL,NULL,'Knoxville','TN','37919-    ','',NULL,'Henry Welch'),
 ('Del-Air Mechanical','135 Chicamauga Ave.',NULL,NULL,'Knoxville','TN','37917-    ','8655254119',NULL,'Jim McCampbell'),
 ('Leon Williams G.C.','',NULL,NULL,'Maryville','TN','37801-    ','8659825015',NULL,'Leon Williams'),
 ('Powell Bros.Htg. & Cooling','2624 Clinton Hwy',NULL,NULL,'Clinton','TN','37717-    ','8659455250',NULL,'Keith Powell'),
 ('Alstom Power - Enviro Systems Div','1409 Centerpoint Blvd',NULL,NULL,'Knoxville','TN','37932-    ','8656945281',NULL,'Linda Wert'),
 ('Sewell & Co. Construction','8124 Nutmeg Circle',NULL,NULL,'Knoxville','TN','37938-    ','8659225242',NULL,'Bill Sewell'),
 ('State of Tennessee Military Dept','Division of Facilities Maintenance',NULL,NULL,'Nashville','TN','37204-    ','6153130705',NULL,'Randy Wentz'),
 ('Webb Plumbing & Heating  ','1418 South White Street',NULL,NULL,'Athens','TN','37371-0847','4237453590',NULL,'Tom Vicars'),
 ('Blount Memorial Hospital','907 E. Lamar Alexander Pkwy',NULL,NULL,'Maryville','TN','37804-    ','',NULL,''),
 ('Action Door & Glass','1290 Rocky Hill Road',NULL,NULL,'Knoxville','TN','37919-    ','8656937252',NULL,'Bill Sullivan'),
 ('Ultra-Tech A/C & Heating','5618 Wallwood Dr',NULL,NULL,'Knoxville','TN','37912-    ','8656892323',NULL,'T.A. Clough'),
 ('Copeland Brothers, Inc.','1671 Lake City Hwy',NULL,NULL,'Clinton','TN','37716-    ','8654574290',NULL,'John Copeland'),
 ('U.S. Foods','',NULL,NULL,'Alcoa','TN','37701-    ','',NULL,'Stacey Henry'),
 ('Rule Construction','1140 Topside Rd',NULL,NULL,'Louisville','TN','37777-    ','8659703038',NULL,'Hugh Rule'),
 ('AlcuLight & Supply','4511 Central Ave. Pike',NULL,NULL,'Knoxville','TN','37912-    ','8656888636',NULL,'Craig Rochat'),
 ('Modern Heating','1807 Elmendorf St',NULL,NULL,'Chattanooga','TN','37406-    ',NULL,NULL,'Calvin'),
 ('Advanced Catalyst Systems','304 Partnership Pkwy.',NULL,NULL,'Maryville','TN','37801-    ','',NULL,''),
 ('Cherokee Group','1866 Country Meadows Dr',NULL,NULL,'Sevierville','TN','37864-    ','',NULL,'Brett Nutter'),
 ('Rebco, Inc.','431 Park Village Rd',NULL,NULL,'Knoxville','TN','37923-    ','8656930384',NULL,''),
 ('Montgomery Electric','313 Draper Lane',NULL,NULL,'Gainesboro','TN','38562-    ','9312680261',NULL,'Frank Montgomery'),
 ('Control Services Inc.','536 S. Hill Street',NULL,NULL,'Morristown','TN','37813-    ','4235867411',NULL,'Travis Wise'),
 ('Vineyard Htg & Air',NULL,NULL,NULL,'Knoxville','TN',NULL,'8659330632',NULL,'Wes Vineyard'),
 ('Southern Constructors','1150 Maryville Pike',NULL,NULL,'Knoxville','TN','37920-    ','4235795351',NULL,'Division 10200 Estimator'),
 ('Valley Electric & Mechanical','2308 Sycamore Dr.',NULL,NULL,'Knoxville','TN','37921-    ','8656375787',NULL,'Bill McKee'),
 ('Hale Construction Inc','1990 S. Economy Rd',NULL,NULL,'Morristown','TN','37816-    ','4235872612',NULL,''),
 ('Stooksbury Appliance Service, Inc.','7411 Texas Valley Road',NULL,NULL,'Knoxville','TN','37938-    ','922-2279',NULL,''),
 ('Haren Construction Co Inc','1715 Hwy 411 N',NULL,NULL,'Etowah','TN','37331-    ','4232635561',NULL,'Division 10200 Estimator'),
 ('Creative Structures','4707 Old Broadway',NULL,NULL,'Knoxville','TN','37918-    ','8656881335',NULL,'Division 10200 Estimator'),
 ('Vacuum Technology','',NULL,NULL,'Oak Ridge','TN',NULL,'8654813342',NULL,'JC Stutts'),
 ('C. Laney & Sons Construction, Inc.','Suite 3',NULL,NULL,'Sevierville','TN','37862-    ','8657744607',NULL,''),
 ('Anthony Gordon Construction',NULL,NULL,NULL,NULL,'TN',NULL,'4233655100',NULL,'Steve Dover'),
 ('Walters State Community College','500 S. Davy Crockett Pkwy.',NULL,NULL,'Morristown','TN','37813-6899','',NULL,''),
 ('Wells & West, Inc.','1268 Andrews Road',NULL,NULL,'Murphy','NC','28906-    ','8288372437',NULL,''),
 ('Vifan','5706 Superior Drive',NULL,NULL,'Morristown','TN','37814-    ','4235816990',NULL,''),
 ('C. C. Dickson - Knoxville','1338 East Weisgarber Rd',NULL,NULL,'Knoxville','TN','37909-    ','8655830401',NULL,'Tom Shoults'),
 ('Craze Brothers Heating & Air','Midway Circle, Rt 3',NULL,NULL,'Oliver Springs','TN','37840-    ','8654357404',NULL,'Terry'),
 ('Coastal Supply','1031 Lee St',NULL,NULL,'Knoxville','TN','37917-    ','8656379262',NULL,'Earl Sharp'),
 ('Taylors Heating & Cooling','1700 Taylor Place Road',NULL,NULL,'Jamestown','TN','38556-    ','9318799554',NULL,'Barry Farmer'),
 ('State Mechanical','',NULL,NULL,'Knoxville','TN','37900-    ','',NULL,''),
 ('Denso Manufacturing Athens TN','2400 Denso Drive',NULL,NULL,'Athens','TN','37303-    ','4237461153',NULL,'Angela Gasche'),
 ('Igloo  Heating & A.C.','',NULL,NULL,'Knoxville','TN','37918-    ','8656891121',NULL,'Bryce Laurie'),
 ('Glen Phillips Heating & Air','1398 Providence Rd',NULL,NULL,'Sevierville','TN','37876-    ','8659080820',NULL,'Glen Phillips'),
 ('NuAire Systems, Inc.','',NULL,NULL,'Knoxville','TN','37912-    ','8656379144',NULL,''),
 ('Metro Services Inc','4563 Pinnacle Ln',NULL,NULL,'Chattanooga','TN','37415-    ','4238705558',NULL,'Don Baker'),
 ('Clay County Board of Education','154 Yellow Jacket Dr',NULL,NULL,'Haysville','NC','28904-    ','8283898513',NULL,'Jerry Bradley'),
 ('Industrial Applications Company','PO Box 9510',NULL,NULL,'Knoxville','TN','37940-0510','8656098001',NULL,'Wayne Brickey'),
 ('Beverly Steel Corp.','4322 Anderson Rd.',NULL,NULL,'Knoxville','TN','37918-    ','',NULL,'Joe Funderburk'),
 ('Rohm and Haas Company','730 Dale Ave',NULL,NULL,'Knoxville','TN','37921-    ','8655218200',NULL,'Mark Johnson'),
 ('Hickory Construction, Inc.','124 Kent Place',NULL,NULL,'Alcoa','TN','37701-    ','8659837856',NULL,''),
 ('Jones Heating & Cooling',NULL,NULL,NULL,'Cookeville','TN',NULL,'9315376376',NULL,'Ray Jones'),
 ('American Eagle Mechanical','914  H Callahan Dr.',NULL,NULL,'Knoxville','TN','37912-    ','8656897206',NULL,'Glen Holt'),
 ('Fairfax Development','7503 Asheville Hwy',NULL,NULL,'Knoxville','TN','37924-    ','8656078350',NULL,'Abe'),
 ('Zarb Properties','118 Huxley Rd',NULL,NULL,'Knoxville','TN','37922-    ','8656711744',NULL,'Tina'),
 ('Mills Wilson George, Inc.','1847 Vanderhorn Drive',NULL,NULL,'Memphis','TN','38134-    ','9013735100',NULL,'Steve Wilson'),
 ('Hannah Construction','1725 Scenic Valley Ln',NULL,NULL,'Knoxville','TN','37922-    ','8653100014',NULL,'Richard Hash, Jr'),
 ('Melton Heating & Air, Inc.','1203 N Charles Seivers Blvd.',NULL,NULL,'Clinton','TN','37716-    ','8654576898',NULL,''),
 ('Foster-Wheeler','111 Union Valley Road',NULL,NULL,'Oak Ridge','TN','37830-    ','8654818635',NULL,'Greg Wagner'),
 ('Fort Sanders Regional Med Center','1901 Clinch Ave',NULL,NULL,'Knoxville','TN','37916-    ','541-1244',NULL,'Sam Calloway'),
 ('Wood Brothers Construction','219 W Young High Pike',NULL,NULL,'Knoxville','TN','37920-    ','',NULL,'Neil Woodland'),
 ('Atech','900 East Hille Ave',NULL,NULL,'Knoxville','TN','37915-    ','8655220777',NULL,''),
 ('McSpadden, Inc.','613 E. Meeting Street',NULL,NULL,'Dandridge','TN','37725-0948','8653973822',NULL,'Kevin Bryant'),
 ('UT-Batelle',NULL,NULL,NULL,'Oak Ridge','TN','37803-    ','',NULL,'Pete Kulesza'),
 ('West & Wells Inc.','7755 Hwy # 294',NULL,NULL,'Murphy','NC','28906-    ','',NULL,'Charles'),
 ('SBS Mechanical',NULL,NULL,NULL,NULL,'TN','37000-    ','8652980195',NULL,'Tony Pierceall'),
 ('Titan Mechanical',NULL,NULL,NULL,'Knoxville','TN','37900-    ','',NULL,'Marcus'),
 ('Blevins Electric Inc',NULL,NULL,NULL,'Kingsport','TN','37663-    ','4232391088',NULL,'Jay Davis'),
 ('Heat & Air Technology, Inc.','1629 Woodpointe Dr.',NULL,NULL,'Knoxville','TN','37931-    ','599-6095',NULL,'Heath Boggs'),
 ('Premier Services','333 Troy Circle, Suite P',NULL,NULL,'Knoxville','TN','37919-    ','8655585711',NULL,'Chuck Langford'),
 ('Bovis Lend & Lease','572 Rivers Rd.',NULL,NULL,'Boone','NC','28607-    ','',NULL,'Adision ( Logan Htg. & Air )'),
 ('C & C Mechanical','4401 Deerfield  Rd',NULL,NULL,'Knoxville','TN','37921-    ','8659099353',NULL,'Jon'),
 ('Southeast Building Solutions','104 Meco Lane',NULL,NULL,'Oak Ridge','TN','37830-    ','8652980194',NULL,'Tony Pierceall'),
 ('Weather Doctor','8922 Bluegrass Rd',NULL,NULL,'Knoxville','TN','37922-    ','8650000000',NULL,'Jeff Maples'),
 ('Baptist Hospital West','10820 Parkside Dr',NULL,NULL,'Knoxville','TN','37922-    ','8656079903',NULL,'Dennis'),
 ('Lawson Quality Heat & Air','7110 Weaver Rd',NULL,NULL,'Powell','TN','37849-    ','8654541189',NULL,'Steve lawson'),
 ('Pipe Doctor','75 E Norris Rd',NULL,NULL,'Norris','TN',NULL,'8654940691',NULL,'Dennis'),
 ('Air Movers, Inc.','1008 Bluff City Hwy.',NULL,NULL,'Bristol','TN','37620',NULL,NULL,'John Crewey'),
 ('Central Htg & Air','1619 1/2 W. Market Street',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('S.B. White Co.','226-228 E. Market St.',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Air Balance Technology','Route 1 Bancroft Chapel Rd',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Allen Dryden Architects','1201 N. Eastman Rd',NULL,NULL,'Kingsport','TN','37664',NULL,NULL,NULL),
 ('Allied Metals Co.','201 Prince St.',NULL,NULL,'Johnson City','TN','37605',NULL,NULL,NULL),
 ('American Mechanical Cont\'s, Inc.','5200 Sullivan Gardens Pkwy.',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Armstrong Construction','151 Shelby Street',NULL,NULL,'Kingsport','TN','37662',NULL,NULL,NULL),
 ('Farnsworth Htg. & Plbg. Co., Inc.','2310 W. State Street',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('Fugate Heating','3400 Timberlake Rd',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('J. E. Green Co.','303 E. Market Street',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Griffith Engineering & Consulting','7261 Sweetbriar Drive',NULL,NULL,'Talbott','TN','37877',NULL,NULL,NULL),
 ('H.E.B. Heating & Cooling Inc.','108 L.P. Auer Rd.',NULL,NULL,'Johnson City','TN','37603',NULL,NULL,NULL),
 ('Hill Wright Enterprises','P.O. Box 1207',NULL,NULL,'Seymour','TN','37865',NULL,NULL,NULL),
 ('Johnson City Medical Center','400 State of Franklin Road',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('Johnson-Hillard, Inc.','1100 East Industry Drive',NULL,NULL,'Kingsport','TN','37662',NULL,NULL,NULL),
 ('Jack Kite Air Conditioning','261 Bristol Metals Rd.',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('Moody Sprinkler Co., Inc.','5434 Fort Henry Drive',NULL,NULL,'Kingsport','TN','37663',NULL,NULL,NULL),
 ('Nor-Well Company, Inc.','136 Elk Avenue',NULL,NULL,'Elizabethton','TN','37643',NULL,NULL,NULL),
 ('Northeastern Sales','605 E. Watauga St.',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('R. L. Slusher & Associates, Inc.','4617 Chambliss Avenue',NULL,NULL,'Knoxville','TN','37919',NULL,NULL,NULL),
 ('Trane Co.(Tri-Cities)','660 Eastern Star Road',NULL,NULL,'Kingsport','TN','37663',NULL,NULL,NULL),
 ('V. A. Medical Center','119 E. King St',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Wellmont Health System','1 Medical Park Blvd.',NULL,NULL,'Kingsport','TN','37620',NULL,NULL,NULL),
 ('Cox Mechanical','314 W. Main St.',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('East Tennessee State University','Central Receiving Dept.',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('C. C. Dickson(JC)','1406 East Millard',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Refrigeration Services,Inc.','744 Bluff City Hwy.',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('J.A. Street & Assoc.,Inc.','245 Birch Street',NULL,NULL,'Blountville','TN','37617',NULL,NULL,NULL),
 ('York International','1004 Eastman Rd.',NULL,NULL,'Kingsport','TN','37663',NULL,NULL,NULL),
 ('Burleson Const. Co.,Inc.','725 W. Walnut Strret',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('Summers Hardware & Supply','Buffalo & Ashe Street',NULL,NULL,'Johnson City','TN','37605',NULL,NULL,NULL),
 ('Newman Htg. & Air , Inc.','802 W. Jackson Blvd.',NULL,NULL,'Jonesborough','TN','37659',NULL,NULL,NULL),
 ('Kingsport Htg. & A/C','1905 Brookside Drive',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Landmark Corp','935 N. State Of Franklin Rd.',NULL,NULL,'Johnson City','TN','37604',NULL,NULL,NULL),
 ('CED Electrical Distributors','1623 Industrial Rd.',NULL,NULL,'Greeneville','TN','37745',NULL,NULL,NULL),
 ('H.S. Williams Co. , Inc.','1320 Hwy. 16',NULL,NULL,'Marion','VA','24354',NULL,NULL,NULL),
 ('Cooks A/C & Heating','1565 Hwy. 75',NULL,NULL,'Blountville','TN','37617',NULL,NULL,NULL),
 ('Kingsport Armature & Electric Co.,Inc.','323 E. Market St.',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Beeson Lusk & Street','101Fountain Square-5719',NULL,NULL,'Johnson City','TN','37604-',NULL,NULL,NULL),
 ('Burwil Construction Co.,Inc.','620 Locust Street',NULL,NULL,'Bristol','TN','37621',NULL,NULL,NULL),
 ('Ken Ross Architects','2700 S. Roan Street',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('N.E. State Community College','PO Box 246',NULL,NULL,'Blountville','TN','37617',NULL,NULL,NULL),
 ('Barry Htg. & A/C','931 State Of Franklin Rd.',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('C  &  C Millwright Co.','311 Old Knoxville Hwy',NULL,NULL,'Greeneville','TN','37743',NULL,NULL,NULL),
 ('Quesenberry\'s , Inc.','104 E. 19th Street',NULL,NULL,'Big Stone Gap','VA','24219',NULL,NULL,NULL),
 ('Goins Rash Cain,Inc.','130 Regional Park Drive',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Lauren Constructors & Engineers','1205 Banner Hill Rd.',NULL,NULL,'Erwin','TN','37650',NULL,NULL,NULL),
 ('C.C. Dickson Co. Kingsport,TN.','920 S. Wilcox Drive - Bldg. 17',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Collings-Parker Associates,Inc.','908 E. Center Street',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Duncan Mechanical,Inc.','507 Jonesborough Hwy.',NULL,NULL,'Erwin','TN','37650',NULL,NULL,NULL),
 ('Fenco Supply Co., Inc.','3017 Oakland Ave.',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Advanced Heating & A/C,Inc.','25280 Lee Hwy.',NULL,NULL,'Abingdon','VA','24211',NULL,NULL,NULL),
 ('Erwin Htg. & A/C,L.L.C.','529B Keever Street',NULL,NULL,'Erwin','TN','37650',NULL,NULL,NULL),
 ('C & T Construction Co. , Inc.','144 Alf Taylor Road',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Partons Heating & A/C','105 N. College St.',NULL,NULL,'Greeneville','TN','37743',NULL,NULL,NULL),
 ('Glade Construction, Inc.','960 West Main Street',NULL,NULL,'Abingdon','VA','24210',NULL,NULL,NULL),
 ('Steadman Construction Co.','1521 Riverport Road',NULL,NULL,'Kingsport','TN','37662',NULL,NULL,NULL),
 ('Cumberland Htg. & Air','630 Patterson Road',NULL,NULL,'Harrogate','TN','37752',NULL,NULL,NULL),
 ('Kingsport Plbg. , Cooling & Heating','635 West Sullivan Street',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Advanced Ht. Pump Systems,Inc.','2909 Rocky Top Rd.',NULL,NULL,'Johnson City','TN','37601',NULL,NULL,NULL),
 ('Frizzell Construction Company','1501 Bluff City Hwy.',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('Hoilman Construction','PO Box 371',NULL,NULL,'Johnson City','TN','37605',NULL,NULL,NULL),
 ('Cooks A/C & Heating(Knox)','130 A Perimeter Park Rd.',NULL,NULL,'Knoxville','TN','37922',NULL,NULL,NULL),
 ('K.D.Moore Construction Co.','109 Meadowview Rd.',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('Day & Zimmerman',NULL,NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Comfort Systems USA','294 Blevins Blvd.',NULL,NULL,'Bristol','VA','24202',NULL,NULL,NULL),
 ('GILCO Construction, Inc.','3080 Hwy. 75 Suite 3',NULL,NULL,'Blountville','TN','37617',NULL,NULL,NULL),
 ('Kingsport City Schools-Facility Mngt.','1000 Poplar Street',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('Nuclear Fuels Service, Inc.','1205 Banner Hill Road',NULL,NULL,'Erwin','TN','37650',NULL,NULL,NULL),
 ('Wellmont Holston Valley Med. Center','301 West Main Street',NULL,NULL,'Kingsport','TN','37660',NULL,NULL,NULL),
 ('GlaxoSmithKline','201 Industrial Drive',NULL,NULL,'Bristol','TN','37620',NULL,NULL,NULL),
 ('Blevins Electric, Inc.(HVAC Dept.)','5063 Fort Henry Drive',NULL,NULL,'Kingsport','TN','37663',NULL,NULL,NULL);
/*!40000 ALTER TABLE `convert_customers` ENABLE KEYS */;


--
-- Definition of table `convert_inventory`
--

DROP TABLE IF EXISTS `convert_inventory`;
CREATE TABLE `convert_inventory` (
  `ItemCode` varchar(255) DEFAULT NULL,
  `Vendor` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `AverageCost` double DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `convert_inventory`
--

/*!40000 ALTER TABLE `convert_inventory` DISABLE KEYS */;
INSERT INTO `convert_inventory` (`ItemCode`,`Vendor`,`Description`,`AverageCost`) VALUES 
 ('AD10012','Price','B12 White Paint - 13 oz. Aerosol Can',NULL),
 ('AD10015','Price','B15 Aluminum Paint - 13 oz. Aerosol Can',NULL),
 ('AD10017','Price','B17 Black Paint - 13 oz. Aerosol Can',NULL),
 ('AD10112','Price','SPF 12x12 Steel Plaster Frame',NULL),
 ('AD10112A','Price','AMF 12x12 Aluminum Plaster Frame',NULL),
 ('AD10115A','Price','AMF 15x15 Aluminum Plaster Frame',NULL),
 ('AD10118A','Price','AMF 18x18 Aluminum Plaster Frame',NULL),
 ('AD10120A','Price','AMF 20x20 Aluminum Plaster Frame',NULL),
 ('AD10121A','Price','AMF 21X21 Aluminum Plaster Frame',NULL),
 ('AD10124','Price','SPF 24x24 Steel Plaster Frame',NULL),
 ('AD1012412A','Price','AMF 24x12 Aluminum Plaster Frame',NULL),
 ('AD10124A','Price','AMF 24x24 Aluminum Plaster Frame',NULL),
 ('AD1020606','Price','VCS3 6x6 Opposed Blade Damper',NULL),
 ('AD1020808','Price','VCS3 8x8 Opposed Blade Damper',NULL),
 ('AD1020909','Price','VCS3 9x9 Opposed Blade Damper',NULL),
 ('AD1021010','Price','VCS3 10x10 Opposed Blade Damper',NULL),
 ('AD1021212','Price','VCS3 12x12 Opposed Blade Damper',NULL),
 ('AD1021414','Price','VCS3 14x14 Opposed Blade Damper',NULL),
 ('AD1021515','Price','VCS3 15x15 Opposed Blade Damper',NULL),
 ('AD1021616','Price','VCS3 16x16 Opposed Blade Damper',NULL),
 ('AD1021818','Price','VCS3 18x18 Opposed Blade Damper',NULL),
 ('AD1022222','Price','VCS3 22x22 Opposed Blade Damper',NULL),
 ('AD1022424','Price','VCS3 24x24 Opposed Blade Damper',NULL),
 ('AD10506','Price','VCR7 6\" Radial Opposed Blade Damper',NULL),
 ('AD10508','Price','VCR7 8\" Radial Opposed Blade Damper',NULL),
 ('AD10510','Price','VCR7 10\" Radial Opposed Blade Damper',NULL),
 ('AD10512','Price','VCR7 12\" Radial Opposed Blade Damper',NULL),
 ('AD10606','Price','VCR8E 6\" Butterfly Damper',NULL),
 ('AD10608','Price','VCR8E 8\" Butterfly Damper',NULL),
 ('AD10610','Price','VCR8E 10\" Butterfly Damper',NULL),
 ('AD10612','Price','VCR8E 12\" Butterfly Damper',NULL),
 ('AD10614','Price','VCR8E 14\" Butterfly Damper',NULL),
 ('AD10706','Price','VCR9 6\" Radial Damper',NULL),
 ('AD10708','Price','VCR9 8\" Radial Damper',NULL),
 ('AD10710','Price','VCR9 10\" Radial Damper',NULL),
 ('AD10712','Price','VCR9 12\" Radial Damper',NULL),
 ('AD10714','Price','VCR9 14\" Radial Damper',NULL),
 ('AD20106','Price','SCD 6\" Diffuser - 12x12 Face',NULL),
 ('AD20108','Price','SCD 8\" Diffuser - 12x12 Face',NULL),
 ('AD20206','Price','SCD 6\" Diffuser - 20x20 Face',NULL),
 ('AD20208','Price','SCD 8\" Diffuser - 20x20 Face',NULL),
 ('AD20210','Price','SCD 10\" Diffuser - 20x20 Face',NULL),
 ('AD20306','Price','SCD/3P 6\" Diffuser - 12x12 Face/24x24 Panel',NULL),
 ('AD20308','Price','SCD/3P 8\" Diffuser - 12x12 Face/24x24 Panel',NULL),
 ('AD20406','Price','SCD 6\" Diffuser ',NULL),
 ('AD20408','Price','SCD 8\" Diffuser ',NULL),
 ('AD20410','Price','SCD 10\" Diffuser ',NULL),
 ('AD20412','Price','SCD 12\" Diffuser ',NULL),
 ('AD20414','Price','SCD 14\" Diffuser ',NULL),
 ('AD20415','Price','SCD 15\" Diffuser ',NULL),
 ('AD20506','Price','SCD/I 6\" Diffuser - Insulated Backpan',NULL),
 ('AD20508','Price','SCD/I 8\" Diffuser - Insulated Backpan',NULL),
 ('AD20510','Price','SCD/I 10\" Diffuser - Insulated Backpan',NULL),
 ('AD20512','Price','SCD/I 12\" Diffuser - Insulated Backpan',NULL),
 ('AD20514','Price','SCD/I 14\" Diffuser - Insulated Backpan',NULL),
 ('AD20515','Price','SCD/I 15\" Diffuser - Insulated Backpan',NULL),
 ('AD20706','Price','SCDA 6\" Diffuser - Adjustable',NULL),
 ('AD20708','Price','SCDA 8\" Diffuser - Adjustable',NULL),
 ('AD20710','Price','SCDA 10\" Diffuser - Adjustable',NULL),
 ('AD20712','Price','SCDA 12\" Diffuser - Adjustable',NULL),
 ('AD20714','Price','SCDA 14\" Diffuser - Adjustable',NULL),
 ('AD20906','Price','SMD-6 6x6 Diffuser - Beveled Frame',NULL),
 ('AD20909','Price','SMD-6 9x9 Diffuser - Beveled Frame',NULL),
 ('AD20912','Price','SMD-6 12x12 Diffuser - Beveled Frame',NULL),
 ('AD20915','Price','SMD-6 15x15 Diffuser - Beveled Frame',NULL),
 ('AD21006','Price','SMD-1 6x6 Diffuser - Flush Frame',NULL),
 ('AD21009','Price','SMD-1 9x9 Diffuser - Flush Frame',NULL),
 ('AD21012','Price','SMD-1 12x12 Diffuser - Flush Frame',NULL),
 ('AD21015','Price','SMD-1 15x15 Diffuser - Flush Frame',NULL),
 ('AD21106','Price','SMD-3P 6x6 Diffuser - 24x24 Panel',NULL),
 ('AD21109','Price','SMD-3P 9x9 Diffuser - 24x24 Panel',NULL),
 ('AD21112','Price','SMD-3P 12x12 Diffuser - 24x24 Panel',NULL),
 ('AD21115','Price','SMD-3P 15x15 Diffuser - 24x24 Panel',NULL),
 ('AD21118','Price','SMD-36 18x18 Diffuser - 24x24 Face',NULL),
 ('AD23006','Price','RCDE 6\" Round Diffuser',NULL),
 ('AD23008','Price','RCDE 8\" Round Diffuser',NULL),
 ('AD23010','Price','RCDE 10\" Round Diffuser',NULL),
 ('AD23012','Price','RCDE 12\" Round Diffuser',NULL),
 ('AD23014','Price','RCDE 14\" Round Diffuser',NULL),
 ('AD30324','Price','PFRF 24x24 Non-Ducted Perforated Return',NULL),
 ('AD30822','Price','PDDR 22x22 Perforated Return',NULL),
 ('AD31306','Price','PDDR 6\" Perforated Return',NULL),
 ('AD31308','Price','PDDR 8\" Perforated Return',NULL),
 ('AD31310','Price','PDDR 10\" Perforated Return',NULL),
 ('AD31312','Price','PDDR 12\" Perforated Return',NULL),
 ('AD31314','Price','PDDR 14\" Perforated Return',NULL),
 ('AD31316','Price','PDDR 16\" Perforated Return',NULL),
 ('AD4030606','Price','80F 6x6 Eggcrate Return',NULL),
 ('AD4030808','Price','80F 8x8 Eggcrate Return',NULL),
 ('AD4031010','Price','80F 10x10 Eggcrate Return',NULL),
 ('AD4031212','Price','80F 12x12 Eggcrate Return',NULL),
 ('AD4031414','Price','80F 14x14 Eggcrate Return',NULL),
 ('AD4031616','Price','80F 16x16 Eggcrate Return',NULL),
 ('AD4031818','Price','80F 18x18 Eggcrate Return',NULL),
 ('AD4032020','Price','80F 20x20 Eggcrate Return',NULL),
 ('AD4032222','Price','80F 22x22 Eggcrate Return',NULL),
 ('AD4032424','Price','80F 24x24 Eggcrate Return',NULL),
 ('AD4040606','Price','80/LI 6x6 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4040808','Price','80/LI 8x8 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4041010','Price','80/LI 10x10 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4041212','Price','80/LI 12x12 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4041414','Price','80/LI 14x14 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4041616','Price','80/LI 16x16 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4041818','Price','80/LI 18x18 Eggcrate Return in 24x24 Panel',NULL),
 ('AD4052210','Price','80F 22x10 NSH Eggcrate Return',NULL),
 ('AD4052222','Price','80F 22x22 NSH Eggcrate Return',NULL),
 ('AD4054622','Price','80F 46x22 NSH Eggcrate Return',NULL),
 ('AD4072020','Price','80FF 24x24 Lay-in  Eggcrate Filter Grille',NULL),
 ('AD4110606','Price','530 6x6 45 Degree Louvered Return',NULL),
 ('AD4110808','Price','530 8x8 45 Degree Louvered Return',NULL),
 ('AD4111010','Price','530 10x10 45 Degree Louvered Return',NULL),
 ('AD4111212','Price','530 12x12 45 Degree Louvered Return',NULL),
 ('AD4111414','Price','530 14x14 45 Degree Louvered Return',NULL),
 ('AD4111616','Price','530 16x16 45 Degree Louvered Return',NULL),
 ('AD4111818','Price','530 18x18 45 Degree Louvered Return',NULL),
 ('AD4112020','Price','530 20x20 45 Degree Louvered Return',NULL),
 ('AD4112024','Price','530/S 20x24 45 Degree Louvered Return',NULL),
 ('AD4112424','Price','530 24x24 45 Degree Louvered Return',NULL),
 ('AD4114824','Price','530/L 48x24 45 Degree Louvered Return',NULL),
 ('AD4122222','Price','530 22x22 NSH 45 Degree Louvered Return',NULL),
 ('AD4132020','Price','530FF 24x24 Lay-in 45 Degree Louvered Filter Grille',NULL),
 ('AD5100804','Price','520D/S 8x4 Double Deflection with Damper',NULL),
 ('AD5100806','Price','520D/S 8x6 Double Deflection with Damper',NULL),
 ('AD5101004','Price','520D/S 10x4 Double Deflection with Damper',NULL),
 ('AD5101006','Price','520D/S 10x6 Double Deflection with Damper',NULL),
 ('AD5101008','Price','520D/S 10x8 Double Deflection with Damper',NULL),
 ('AD5101204','Price','520D/S 12x4 Double Deflection with Damper',NULL),
 ('AD5101206','Price','520D/S 12x6 Double Deflection with Damper',NULL),
 ('AD5101208','Price','520D/S 12x8 Double Deflection with Damper',NULL),
 ('AD5101210','Price','520D/S 12x10 Double Deflection with Damper',NULL),
 ('AD5101212','Price','520D/S 12x12 Double Deflection with Damper',NULL),
 ('AD5101406','Price','520D/S 14x6 Double Deflection with Damper',NULL),
 ('AD5101408','Price','520D/S 14x8 Double Deflection with Damper',NULL),
 ('AD5101410','Price','520D/S 14x10 Double Deflection with Damper',NULL),
 ('AD5101412','Price','520D/S 14x12 Double Deflection with Damper',NULL),
 ('AD5101606','Price','520D/S 16x6 Double Deflection with Damper',NULL),
 ('AD5101608','Price','520D/S 16x8 Double Deflection with Damper',NULL),
 ('AD5101610','Price','520D/S 16x10 Double Deflection with Damper',NULL),
 ('AD5101612','Price','520D/S 16x12 Double Deflection with Damper',NULL),
 ('AD5101806','Price','520D/S 18x6 Double Deflection with Damper',NULL),
 ('AD5101808','Price','520D/S 18x8 Double Deflection with Damper',NULL),
 ('AD5101810','Price','520D/S 18x10 Double Deflection with Damper',NULL),
 ('AD5101812','Price','520D/S 18x12 Double Deflection with Damper',NULL),
 ('AD5102006','Price','520D/S 20x6 Double Deflection with Damper',NULL),
 ('AD5102008','Price','520D/S 20x8 Double Deflection with Damper',NULL),
 ('AD5102010','Price','520D/S 20x10 Double Deflection with Damper',NULL),
 ('AD5102210','Price','520D/S 22x10 Double Deflection with Damper',NULL),
 ('AD5102408','Price','520D/S 24x8 Double Deflection with Damper',NULL),
 ('AD5102410','Price','520D/S 24x10 Double Deflection with Damper',NULL),
 ('AD5102412','Price','520D/S 24x12 Double Deflection with Damper',NULL),
 ('AD9001212','Price','STG1/BF 12x12 Door Grille',NULL),
 ('AD9001812','Price','STG1/BF 18x12 Door Grille',NULL),
 ('AD9001818','Price','STG1/BF 18x18 Door Grille',NULL),
 ('ADBF1006','Ward','Access Door, 10x6 Flat',NULL),
 ('ADBF1612','Ward','Access Door, 16x12 Flat',NULL),
 ('ADBR10606','Ward','Access Door, 10x 6 on  6\" Round Duct',NULL),
 ('ADBR10608','Ward','Access Door, 10x 6 on  7-8\" Round Duct',NULL),
 ('ADBR10610','Ward','Access Door, 10x 6 on 9-10\" Round Duct',NULL),
 ('ADBR10612','Ward','Access Door, 10x 6 on 12\" Round Duct',NULL),
 ('ADBR10614','Ward','Access Door, 10x 6 on 14\" Round Duct',NULL),
 ('ADBR10616','Ward','Access Door, 10x 6 on 16\" Round Duct',NULL),
 ('ADBR10618','Ward','Access Door, 10x 6 on 18\" Round Duct',NULL),
 ('ADBR10620','Ward','Access Door, 10x 6 on 20\" Round Duct',NULL),
 ('ADBR10624','Ward','Access Door, 10x 6 on 24\" Round Duct',NULL),
 ('ADBR10626','Ward','Access Door 10x 6 on 26\" Round Duct',NULL),
 ('ADBR10634','Ward','Access Door 10x 6 on 36\" Round Duct',NULL),
 ('ADD0606','Kees','ADH-D 6x6 Access Door - Duct',NULL),
 ('ADD0808','Kees','ADH-D 8x8 Access Door - Duct',NULL),
 ('ADD1010','Kees','ADH-D 10x10 Access Door - Duct',NULL),
 ('ADD1212','Kees','ADH-D 12x12 Access Door - Duct',NULL),
 ('ADD1414','Kees','ADH-D 14x14 Access Door - Duct',NULL),
 ('ADD1616','Kees','ADH-D 16x16 Access Door - Duct',NULL),
 ('ADD1818','Kees','ADH-D 18x18 Access Door - Duct',NULL),
 ('ADD2020','Kees','ADH-D 20x20 Access Door - Duct',NULL),
 ('ADD2424','Kees','ADH-D 24x24 Access Door - Duct',NULL),
 ('ADW0808','Acudor','UF-5000 8x8 Access Door - Wall',NULL),
 ('ADW1010','Acudor','UF-5000 10x10 Access Door - Wall',NULL),
 ('ADW1212','Acudor','UF-5000 12x12 Access Door - Wall',NULL),
 ('ADW1414','Acudor','UF-5000 14x14 Access Door - Wall',NULL),
 ('ADW1616','Acudor','UF-5000 16x16 Access Door - Wall',NULL),
 ('ADW1818','Acudor','UF-5000 18x18 Access Door - Wall',NULL),
 ('ADW2424','Acudor','UF-5000 24x24 Access Door - Wall',NULL),
 ('ADWUL1212','Acudor','FW-5050 12x12 Fire Rated Access Door',NULL),
 ('ARLV1212','Arrow','EA-415-D 12x12 Louver - Channel Frame',NULL),
 ('ARLV1212F','Arrow','EA-415-D 12x12 Louver - Flanged Frame',NULL),
 ('ARLV1818','Arrow','EA-415-D 18x18 Louver - Channel Frame',NULL),
 ('ARLV1818F','Arrow','EA-415-D 18x18 Louver - Flanged Frame',NULL),
 ('ARLV2424','Arrow','EA-415-D 24x24 Louver - Channel Frame',NULL),
 ('ARLV2424F','Arrow','EA-415-D 24x24 Louver - Flanged Frame',NULL),
 ('BR57W','Broan ','57W 3A Speed Contrl',NULL),
 ('BR611','Broan','611CM Roof Cap, 8\" Throat, Curb Mount',NULL),
 ('BR612','Broan','612CM Roof Cap, 12\" Throat, Curb Mount',NULL),
 ('BR634','Broan','634 8\" Sloped Roof Cap, Steel, Black',NULL),
 ('BR636','Broan','636 4\" Sloped Roof Cap, Steel, Black',NULL),
 ('BR639','Broan','639 3x10 Wall Cap, Steel, Black',NULL),
 ('BR640','Broan','640 3\" Wall Cap, Steel, Black',NULL),
 ('BR641','Broan','641 6\" Wall Cap, Natural Aluminum',NULL),
 ('BR642','Broan','642 4\" Wall Cap, Natural Aluminum',NULL),
 ('BR643','Broan','643 8\" Wall Cap, Natural Aluminum',NULL),
 ('BR649','Broan','649 3x10 Wall Cap, Natural Aluminum',NULL),
 ('BR655','Broan','655 Htr/Fan/Light - 70 CFM',NULL),
 ('BR657','Broan','657 Fan/Light - 70 CFM',NULL),
 ('BR670','Broan','670 - 50 CFM Fan',NULL),
 ('BR671','Broan','671 - 70 CFM Fan',NULL),
 ('BR676','Broan','676 - 110 CFM Fan',NULL),
 ('BR684','Broan','684 - 80 CFM Fan',NULL),
 ('BR68W','Broan','68W 2-Function Control',NULL),
 ('BRHD80L','Broan','HD80L Fan/Light - 80 CFM',NULL),
 ('BV088','Sunvent','EX808 Brick Vent 8x8',NULL),
 ('BV1212','Sunvent','EX1212 Brick Vent 12x12',NULL),
 ('BV164','Sunvent','EX164 Brick Vent 16x5',NULL),
 ('BV168','Sunvent','EX168 Brick Vent 16x8',NULL),
 ('CRDR06','Leader Industries','CFD-R  6\" Ceiling Radiation Fire Damper',NULL),
 ('CRDR08','Leader Industries','CFD-R  8\" Ceiling Radiation Fire Damper',NULL),
 ('CRDR10','Leader Industries','CFD-R 10\" Ceiling Radiation Fire Damper',NULL),
 ('CRDR12','Leader Industries','CFD-R 12\" Ceiling Radiation Fire Damper',NULL),
 ('CRDR14','Leader Industries','CFD-R 14\" Ceiling Radiation Fire Damper',NULL),
 ('CRDR16','Leader Industries','CFD-R 16\" Ceiling Radiation Fire Damper',NULL),
 ('CRDS0606','Leader Industries','CFD-1 6x6 Ceiling Radiation Fire Dmper',NULL),
 ('CRDS0808','Leader Industries','CFD-1 8x8 Ceiling Radiation Fire Damper',NULL),
 ('CRDS0909','Leader Industries','CFD-1 9x9 Ceiling Radiation Fire  Damper',NULL),
 ('CRDS1010','Leader Industries','CFD-1 10x10 Ceiling Radiation Fire Damper',NULL),
 ('CRDS1212','Leader Industries','CFD-1 12x12 Ceiling Radiation Fire Damper',NULL),
 ('CRDS1414','Leader Industries','CFD-1 14x14 Ceiling Radiation Fire Damper',NULL),
 ('CRDS1515','Leader Industries','CFD-1 15x15 Ceiling Radiation Fire Damper',NULL),
 ('CRDS1818','Leader Industries','CFD-1 18x18 Ceiling Radiation Fire Damper',NULL),
 ('CRDS2210','Leader Industries','CFD-1 22x10 Ceiling Radiation Fire Damper',NULL),
 ('CRDS2222','Leader Industries','CFD-1 22x22 Ceiling Radiation Fire Damper',NULL),
 ('CRDS2424','Leader Industries','CFD-1 24x24 Ceiling Radiation Fire Damper',NULL),
 ('CRDTB','Leader Industries','TIB 24x24 Thermal Insulating Blanket',NULL),
 ('DMPR06','Leader Industries','WM-CD  6\" Manual Damper',NULL),
 ('DMPR08','Leader Industries','WM-CD  8\" Manual Damper',NULL),
 ('DMPR10','Leader Industries','WM-CD 10\" Manual Damper',NULL),
 ('DMPR12','Leader Industries','WM-CD 12\" Manual Damper',NULL),
 ('DMPR14','Leader Industries','WM-CD 14\" Manual Damper',NULL),
 ('DMPR16','Leader Industries','WM-CD 16\" Manual Damper',NULL),
 ('DMPR18','Leader Industries','WM-CD 18\" Manual Damper',NULL),
 ('DMPRE','Leader Industries','Shaft Extension - 1/2\" Round',NULL),
 ('DMPRQ12R','Leader Industries','1/2\" Round Locking Quadrant',NULL),
 ('DMPRQ38S','Leader Industries','3/8\" Square Locking Quadrant',NULL),
 ('FDA0606','Leader Industries','U215-AX 6x6 Fire Damper with 12\" Sleeve',NULL),
 ('FDA0808','Leader Industries','U215-AX 8x8 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1010','Leader Industries','U215-AX 10x10 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1212','Leader Industries','U215-AX 12x12 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1414','Leader Industries','U215-AX 14x14 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1515','Leader Industries','U215-AX 15x15 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1612','Leader Industries','U215-AX 16x12 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1616','Leader Industries','U215-AX 16x16 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1812','Leader Industries','U215-AX 18x12 Fire Damper with 12\" Sleeve',NULL),
 ('FDA1818','Leader Industries','U215-AX 18x18 Fire Damper with 12\" Sleeve',NULL),
 ('FDA2012','Leader Industries','U215-AX 20x12 Fire Damper with 12\" Sleeve',NULL),
 ('FDA2412','Leader Industries','U215-AX 24x12 Fire Damper with 12\" Sleeve',NULL),
 ('FDA2416','Leader Industries','U215-AX 24x16 Fire Damper with 12\" Sleeve',NULL),
 ('FDA2418','Leader Industries','U215-AX 24x18 Fire Damper with 12\" Sleeve',NULL),
 ('FDA2424','Leader Industries','U215-AX 24x24 Fire Damper with 12\" Sleeve',NULL),
 ('FDL165L','Leader Industries','165 Degree Fusible Link -Large',NULL),
 ('FDL165S','Leader Industries','165 Degree Fusible Link -Small',NULL),
 ('FDL212L','Leader Industries','212 Degree Fusible Link -Large',NULL),
 ('FDL212S','Leader Industries','212 Degree Fusible Link -Small',NULL),
 ('FDSK','Leader Industries','Fire Damper Horizontal Spring Kit',NULL),
 ('HETD06','Jer-Air','AT-501 6\" High Efficiency Takeoff w/Damper',NULL),
 ('HETD08','Jer-Air','AT-501 8\" High Efficiency Takeoff w/Damper',NULL),
 ('HETD10','Jer-Air','AT-501 10\" High Efficiency Takeoff w/Damper',NULL),
 ('HETD12','Jer-Air','AT-501 12\" High Efficiency Takeoff w/Damper',NULL),
 ('HETD14','Jer-Air','AT-501 14\" High Efficiency Takeoff w/Damper',NULL),
 ('JJ4205','J & J','4205FF 24x24 Lay-in  Eggcrate Filter Grille',NULL),
 ('JJ4260','J & J','4260FF 24x24 Lay-in Stamped Filter Grille',NULL),
 ('JJ4290','J & J','4290FF 24x24 Lay-in 45 Degree Louvered Filter Grille',NULL),
 ('JJ60FF','J & J','60GHFF 20x20 Wall Stamped Filter Grille',NULL),
 ('LCPR08','Cook','PR8 Roof Cap w/Screen',NULL),
 ('LCPR12','Cook','PR12 Roof Cap w/Screen',NULL),
 ('LCPR16','Cook','PR16 Roof Cap w/Screen',NULL),
 ('LCPR20','Cook','PR20 Roof Cap w/Screen',NULL),
 ('LCRDA6','Cook','RDA-6 GC Discharge Adaptor',NULL),
 ('LCRDA8','Cook','RDA-8 GC Discharge Adaptor',NULL),
 ('LCWCR6','Cook','WCR-6 Wall Cap, Aluminum',NULL),
 ('LCWCR8','Cook','WCR-8 Wall Cap, Aluminum',NULL),
 ('RC1212','Creative Metals','CSSF-12H-12x12 Roof Curb',NULL),
 ('RC125125','Creative Metals','CSSF-12H-12.5x12.5 Roof Curb',NULL),
 ('RC1414','Creative Metals','CSSF-12H-14x14 Roof Curb',NULL),
 ('RC1717','Creative Metals','CSSF-12H-17x17 Roof Curb',NULL),
 ('RC1919','Creative Metals','CSSF-12H-19x19 Roof Curb',NULL),
 ('RC2020','Creative Metals','CSSF-12H-20x20 Roof Curb',NULL),
 ('RC2323','Creative Metals','CSSF-12H-23x23 Roof Curb',NULL),
 ('RC2525','Creative Metals','CSSF-12H-25x25 Roof Curb',NULL),
 ('RC2727','Creative Metals','CSSF-12H-27x27 Roof Curb',NULL),
 ('RC2929','Creative Metals','CSSF-12H-29x29 Roof Curb',NULL),
 ('RPF1','Creative Metals','Pipe Flashing #1, .25\"-2\"',NULL),
 ('RPF3','Creative Metals','Pipe Flashing #3, 2\" - 4\"',NULL),
 ('RPF4','Creative Metals','Pipe Flashing #4, 3\" - 6\"',NULL),
 ('RPF5','Creative Metals','Pipe Flashing #5, 4\" - 7\"',NULL),
 ('RPF7','Creative Metals','Pipe Flashing #7, 6\"-11\"',NULL),
 ('RPF8','Creative Metals','Pipe Flashing #8, 7\"-13\"',NULL),
 ('RPP4','Creative Metals','PP-4 Pipe Penetration',NULL),
 ('SE0100','Hardcast','Sealer, AFG-1402-3, 4 Rolls/3\" x 100\'',NULL),
 ('SE0105','Hardcast','Sealer, AP-1602-3   3\" x 50\'',NULL),
 ('SE0110','Hardcast','Sealer, AFT-701-3\" Roll',NULL),
 ('SE0116','Hardcast','Sealer, AFT-701-6\" Roll',NULL),
 ('SE0130','Hardcast','Sealer, BRT-801-6\" Roll',NULL),
 ('SE0140','Hardcast','Sealer, DT-5300-3\" Roll',NULL),
 ('SE0150','Hardcast','Sealer, FG-550  Gallon',NULL),
 ('SE0160','Hardcast','Sealer, FG-550 11oz Tube',NULL),
 ('SE0180','Hardcast','Sealer, IG-601 Gallon',NULL),
 ('SE0190','Hardcast','Sealer, IG-601 11oz Tube',NULL),
 ('SE0200','Hardcast','Sealer, P-301-3\"',NULL),
 ('SE0210','Hardcast','Sealer, RTA-50 Gallon',NULL),
 ('SE0300','Hardcast','Sealer, PT-302',NULL),
 ('SE0321','Hardcast','Sealer, DS-321 Gallon',NULL),
 ('SO06','Jer-Air','SO-S1S 6\" Stick-On',NULL),
 ('SO08','Jer-Air','SO-S1S 8\" Stick-On',NULL),
 ('SO10','Jer-Air','SO-S1S 10\" Stick-On',NULL),
 ('SO12','Jer-Air','SO-S1S 12\" Stick-On',NULL),
 ('SO14','Jer-Air','SO-S1S 14\" Stick-On',NULL),
 ('SO16','Jer-Air','SO-S1S 16\" Stick-On',NULL),
 ('SO18','Jer-Air','SO-S1S 18\" Stick-On',NULL),
 ('SOSD04','Jer-Air','SO-S4S 4\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD05','Jer-Air','SO-S4S 5\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD06','Jer-Air','SO-S4S 6\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD07','Jer-Air','SO-S4S 7\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD08','Jer-Air','SO-S4S 8\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD09','Jer-Air','SO-S4S 9\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD10','Jer-Air','SO-S4S 10\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD12','Jer-Air','SO-S4S 12\" Stick-On w/Scoop & Damper',NULL),
 ('SOSD14','Jer-Air','SO-S4S 14\" Stick-On w/Scoop & Damper',NULL),
 ('SR0606','Jer-Air','Square-to-Round 6x6 / 6',NULL),
 ('SR0806','Jer-Air','Square-to-Round 8x8 / 6',NULL),
 ('SR0808','Jer-Air','Square-to-Round 8x8 / 8',NULL),
 ('SR0908','Jer-Air','Square-to-Round 9x9 / 8',NULL),
 ('SR0909','Jer-Air','Square-to-Round 9x9 / 9',NULL),
 ('SR1008','Jer-Air','Square-to-Round 10x10 / 8',NULL),
 ('SR1010','Jer-Air','Square-to-Round 10x10 / 10',NULL),
 ('SR1208','Jer-Air','Square-to-Round 12x12 / 8',NULL),
 ('SR1210','Jer-Air','Square-to-Round 12x12 / 10',NULL),
 ('SR1212','Jer-Air','Square-to-Round 12x12 / 12',NULL),
 ('SR1412','Jer-Air','Square-to-Round 14x14 / 12',NULL),
 ('SR1414','Jer-Air','Square-to-Round 14x14 / 14',NULL),
 ('SR1512','Jer-Air','Square-to-Round 15x15 / 12',NULL),
 ('SR1514','Jer-Air','Square-to-Round 15x15 / 14',NULL),
 ('SR1515','Jer-Air','Square-to-Round 15x15 / 15',NULL),
 ('SR1616','Jer-Air','Square-to-Round 16x16 / 16',NULL),
 ('SR1816','Jer-Air','Square-to-Round 18x18 / 16',NULL),
 ('SR1818','Jer-Air','Square-to-Round 18x18 / 18',NULL),
 ('SR2020','Jer-Air','Square Pan 20x20',NULL),
 ('SR2222','Jer-Air','Square Pan 22x22',NULL),
 ('VE025A','Vibration Eliminator','T44-5A RIS Mount 150# Blue',NULL),
 ('VE29C1','Vibration Eliminator','3C-1 RIS Hanger 150# White',NULL),
 ('VE33C20','Vibration Eliminator','SNC1-C20 Spring Hanger 30# Red',NULL),
 ('VE33C21','Vibration Eliminator','SNC1-C21 Spring Hanger 60# Green',NULL),
 ('VE33C22','Vibration Eliminator','SNC1-C22 Spring Hanger 100# Blue',NULL),
 ('VE51F21','Vibration Eliminator','OST2R-F21 Spring Mount 40# Yellow',NULL),
 ('VE51F22','Vibration Eliminator','OST2R-F22 Spring Mount 100# Red',NULL),
 ('VE51F23','Vibration Eliminator','OST2R-F23 Spring Mount 150# White',NULL),
 ('VE51F24','Vibration Eliminator','OST2R-F24 Spring Mount 210# Green',NULL),
 ('VE51F25','Vibration Eliminator','OST2R-F25 Spring Mount 300# Blue',NULL);
/*!40000 ALTER TABLE `convert_inventory` ENABLE KEYS */;


--
-- Definition of table `convert_vendors`
--

DROP TABLE IF EXISTS `convert_vendors`;
CREATE TABLE `convert_vendors` (
  `Name` varchar(255) DEFAULT NULL,
  `Address1` varchar(255) DEFAULT NULL,
  `Address2` varchar(255) DEFAULT NULL,
  `CityStateZip` varchar(255) DEFAULT NULL,
  `City` varchar(255) DEFAULT NULL,
  `State` varchar(255) DEFAULT NULL,
  `Zip` varchar(255) DEFAULT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `Fax` varchar(255) DEFAULT NULL,
  `Attention` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `convert_vendors`
--

/*!40000 ALTER TABLE `convert_vendors` DISABLE KEYS */;
INSERT INTO `convert_vendors` (`Name`,`Address1`,`Address2`,`CityStateZip`,`City`,`State`,`Zip`,`Phone`,`Fax`,`Attention`) VALUES 
 ('MUSCULAR DYSTROPHY ASSOC.','C/O FIRST TENNESSEE BANK','6801 MAYNARDVILLE HWY.','KNOXVILLE        TN 37918',NULL,NULL,NULL,'','',NULL),
 ('ASHRAE RESEARCH','','','',NULL,NULL,NULL,'','',NULL),
 ('ATECH, INC.','900 E. HILL AVE.','SUITE 135','KNOXVILLE,       TN 37915',NULL,NULL,NULL,'522-0777','',NULL),
 ('INTERNAL REVENUE SERVICE','','','OGDEN,           UT 84201-0038',NULL,NULL,NULL,'','',NULL),
 ('EDUCATIONAL MEDIA GROUP','530 3RD AVE. SOUTH, SUITE #1','','NASHVILLE,       TN 37210',NULL,NULL,NULL,'800-575-8088','800-611-4016',NULL),
 ('SHRINE CIRCUS','DONATION','','',NULL,NULL,NULL,'573-0446','',NULL),
 ('METLIFE','','','',NULL,NULL,NULL,'','',NULL),
 ('NATIONAL NOTARY ASSOC.','P.O. BOX 2402','','CHATSWORTH,      CA 91313-9965',NULL,NULL,NULL,'800-876-6827','',NULL),
 ('A-J MANUFACTURING CO.','P.O. BOX 270320','','KANSAS CITY      MO 64127-0320',NULL,NULL,NULL,'816-231-5522','',NULL),
 ('CARRIER CORPORATION','P.O. BOX 905707','','CHARLOTTE,       NC 28290',NULL,NULL,NULL,'866-338-3518','315-433-8771',NULL),
 ('AES INDUSTRIES, INC.','P.O. BOX 781147','','TALLASSEE        AL 36078',NULL,NULL,NULL,'800-786-0402','334-283-5447',NULL),
 ('ABSOLUTE AIRE','5496 NORTH RIVERVIEW DR.','','KALAMAZOO        MI 49004',NULL,NULL,NULL,'','',NULL),
 ('ACUDOR PRODUCTS, INC.','80 LITTLE FALLS RD.','','FAIRFIELD        NJ 07004',NULL,NULL,NULL,'973-575-5120','',NULL),
 ('ACME ENGINEERING &','MANUFACTURING CORP.','DEPT. 2022','TULSA            OK 74182',NULL,NULL,NULL,'918-684-0530','918-684-0541',NULL),
 ('ADVANCED CONTROLS, INC.','DEPT. #77-3498','','CHICAGO,         IL 60678-3498',NULL,NULL,NULL,'941-746-3221','941-746-3466',NULL),
 ('PRECISION PRODUCTS, LLC','4415 POPLAR LEVEL ROAD','','LOUISVILLE,      KY 40213',NULL,NULL,NULL,'800-769-7999','',NULL),
 ('AIR CONTROL PRODUCTS, INC','3800 TOWPATH','','CLEVELAND        OH 44147-3296',NULL,NULL,NULL,'440-526-3020','440-526-0503',NULL),
 ('AIR-DUCT MFG., INC.','700 WEST BELDEN AVENUE','','ADDISON,         IL 60101',NULL,NULL,NULL,'630-628-9790','630-628-9585',NULL),
 ('AIRGUIDE MFG., LLC','795 W. 20TH STREET','','HIALEAH          FL 33010-2466',NULL,NULL,NULL,'','',NULL),
 ('AIR QUALITY ENGINEERING','7140 NORTHLAND DRIVE NORTH','','BROOKLYN PARK,   MN 55428',NULL,NULL,NULL,'763-531-9823','763-531-9900',NULL),
 ('AIR SEAL FILTER HOUSING','P.O. BOX 26340','','SAN DIEGO        CA 92196-0340',NULL,NULL,NULL,'','',NULL),
 ('AIRCON','441 GREEN ST.','','PHILADELPHIA     PA 19123',NULL,NULL,NULL,'615-292-5555','615-292-3324',NULL),
 ('AITKEN PRODUCTS INC.','566 NORTH EAGLE STREET','P.O. BOX 151','GENEVA, OH       IO 44041',NULL,NULL,NULL,'','',NULL),
 ('AMERICAN CANCER SOCIETY','871 WEISGARBER RD.','','KNOXVILLE,       TN 37909',NULL,NULL,NULL,'800-227-2345','',NULL),
 ('AMERICAN MECHANICAL','CONTRACTORS, INC.','5200 SULLIVAN GARDENS PARKWAY','KINGSPORT,       TN 37660',NULL,NULL,NULL,'423-349-7011','',NULL),
 ('APEX SUPPLY CO., INC.','TENNESSEE DIVISION','P.O. BOX 101802','ATLANTA          GA 30392-1802',NULL,NULL,NULL,'','',NULL),
 ('APPLIED THERMAL SYS., INC','3903 VOLUNTEER DRIVE, SUITE 4','P.O. BOX 23055','CHATTANOOGA      TN 37422-3055',NULL,NULL,NULL,'423-899-9664','',NULL),
 ('ARROW UNITED IND., INC.','','P.O. BOX 5-0571','WOBURN,          MA 01815-0571',NULL,NULL,NULL,'413-564-5974','BROADBRIDGE',NULL),
 ('BABCOCK-DAVIS, INC.','NW7845','P.O. BOX 1450','MINNEAPOLIS      MN 55485-7845',NULL,NULL,NULL,'888-412-3726','612-781-1363',NULL),
 ('BEASLEY COMPANY','AIR MOVING PRODUCTS','P.O. BOX 101481','NASHVILLE        TN 37224-1481',NULL,NULL,NULL,'615-441-6510','',NULL),
 ('BERNER INTERNATIONAL CORP','LOCKBOX 360415 M','','PITTSBURGH       PA 15251',NULL,NULL,NULL,'724-658-3551','724-652-0682',NULL),
 ('AWV','P.O. BOX 5-0571','','WOBURN,          MA 01815-0571',NULL,NULL,NULL,'','',NULL),
 ('BROCK/GLASCOCK','3908 TENNESSEE AVE.','SUITE A','CHATTANOOGA,     TN 37409',NULL,NULL,NULL,'423-825-0049','',NULL),
 ('BLOUNT STEEL SUPPLY INC.','409 HOME AVENUE','','MARYVILLE        TN 37801',NULL,NULL,NULL,'','',NULL),
 ('BRASCH MANUFACTURING CO.','11880 DORSETT ROAD','','MARYLAND HEIGHT  MO 63043',NULL,NULL,NULL,'314-291-0440','(VERY NICE)',NULL),
 ('BREIDERT AIR PRODUCTS','LOCKBOX','P.O. BOX 863004','ORLANDO,         FL 32886-3004',NULL,NULL,NULL,'904-731-4711','904-737-8322',NULL),
 ('LINDA J. BROOKS','1813 ROY DRIVE','','KNOXVILLE        TN 37923',NULL,NULL,NULL,'693-1523','',NULL),
 ('BUCKLEY AIR PRODUCTS, INC','P.O. BOX 1410','','HANOVER,         MA 02339',NULL,NULL,NULL,'800-878-5000','',NULL),
 ('CALLAHAN MECHANICAL CONT.','','2811 8TH AVE.','CHATTANOOGA      TN 37407',NULL,NULL,NULL,'423-622-280','',NULL),
 ('CAPTIVE-AIRE SYSTEMS, INC','117 FRANKLIN PARK AVE.','','YOUNGSVILLE      NC 27596',NULL,NULL,NULL,'800-334-9256','919-554-4605',NULL),
 ('CAMFIL FARR, INC.','P.O. BOX 92438','','CHICAGO,         IL 606752438',NULL,NULL,NULL,'','',NULL),
 ('CAR-MON','1225 DAVIS ROAD','','ELGIN            IL 60123',NULL,NULL,NULL,'847-695-9000','847-695-9078',NULL),
 ('COASTAL SUPPLY CO., INC.','1031 LEE STREET','','KNOXVILLE        TN 37917',NULL,NULL,NULL,'423-637-9262','',NULL),
 ('CHATTANOOGA CONTRACTORS\'','ASSOCIATION','P.O. BOX 3124','CHATTANOOGA      TN 37404-0124',NULL,NULL,NULL,'423-622-1114','',NULL),
 ('CONTROL-EQUIP, INC. OF TN','2044 MAGNOLIA AVE.','','KNOXVILLE        TN 37917',NULL,NULL,NULL,'','',NULL),
 ('CROWN PRODUCTS CO., INC.','6390 PHILLIPS HIGHWAY','','JACKSONVILLE     FL 32216',NULL,NULL,NULL,'904-737-7144','904-737-3533',NULL),
 ('SHOFFNER MECHANICAL','INDUSTRIAL & SERVICE CO.,INC.','P.O. BOX 10048','KNOXVILLE        TN 37939',NULL,NULL,NULL,'','',NULL),
 ('DAVIS HEATING & A/C, INC.','P.O. BOX 3182','','CLEVELAND        TN 37320-3182',NULL,NULL,NULL,'423-472-4692','',NULL),
 ('BEST ONE TIRE','OF KNOXVILLE','5407 MIDDLEBROOK PIKE','KNOXVILLE,       TN 37921',NULL,NULL,NULL,'588-5165','',NULL),
 ('DONCO AIR PRODUCTS','LAFAYETTE INDUSTRIAL PARK','P.O. BOX 250','ALBION           IA 50005',NULL,NULL,NULL,'641-488-2211','641-488-2247',NULL),
 ('DIXIE BOX COMPANY, INC.','P.O. BOX 50244','','KNOXVILLE,       TN 37950',NULL,NULL,NULL,'588-8333','584-3301',NULL),
 ('DUO-AIRE, INC.','MILWOOD OFFICE CENTER','4000 PORTAGE RD., SUITE A','KALAMAZOO        MI 49001',NULL,NULL,NULL,'','',NULL),
 ('DURA-VENT','1177 MARKLEY DRIVE','','PLYMOTH          IA 46563-8227',NULL,NULL,NULL,'','',NULL),
 ('LOREN COOK COMPANY','P.O. BOX 4047','','SPRINGFIELD      MO 65808',NULL,NULL,NULL,'417-869-6474','417-862-3820',NULL),
 ('DWYER INSTRUMENTS INC.','P.O. BOX 338','','MICHIGAN CITY    IN 46361',NULL,NULL,NULL,'219-879-8000','',NULL),
 ('EAST TENNESSEE ASHRAE','c/o DAVID KAMINSKY','P.O. BOX 50365','KNOXVILLE        TN 37950-0365',NULL,NULL,NULL,'974-2231','',NULL),
 ('ED\'S SUPPLY CO., INC.','P.O. BOX 3551','','KNOXVILLE        TN 37927-3551',NULL,NULL,NULL,'','',NULL),
 ('ENGERT PLUMBING & HTG INC','P.O. BOX 55','','KNOXVILLE        TN 37901',NULL,NULL,NULL,'','',NULL),
 ('EWC INC. CONTROLS DIV.','385 HIGHWAY 33','','ENGLISHTOWN      NJ 07726',NULL,NULL,NULL,'','',NULL),
 ('DUCTSOX CORPORATION','CUST# 227614','BOX 78196','MILWAUKEE        WI 53278-0196',NULL,NULL,NULL,'414-362-3722','414-355-9248',NULL),
 ('FANTECH, INC.','P.O. BOX 8500-53093','','PHILADELPHIA,    PA 19178-3093',NULL,NULL,NULL,'800-747-1762','800-487-9915',NULL),
 ('ETTC','P.O. BOX 651690','','CHARLOTTE,       NC 28265-1690',NULL,NULL,NULL,'558-6310','558-0524',NULL),
 ('FERGUSON ENTERPRISES, INC','FEI #13','P.O. BOX 100286','ATLANTA,         GA 30384-0286',NULL,NULL,NULL,'531-8550','531-6443',NULL),
 ('CLEVELAND METALWORKS, INC','625 MIMOSA DRIVE, N.W.','','CLEVELAND,       TN 37312',NULL,NULL,NULL,'','',NULL),
 ('FRAZIER WELDING & STEEL','917 COOPER STREET','','KNOXVILLE        TN 37917',NULL,NULL,NULL,'','',NULL),
 ('FERGUSON EQUIPMENT CO.','P.O. BOX 2645','','KNOXVILLE        TN 37901-2645',NULL,NULL,NULL,'524-1491','',NULL),
 ('G.D. THOMPSON CO., INC.','P.O. BOX 1565','','HUNTSVILLE       AL 35807',NULL,NULL,NULL,'','',NULL),
 ('GAYLORD INDUSTRIES, INC.','P.O. BOX 2109','','CAROL STREAM,    IL 60132',NULL,NULL,NULL,'','',NULL),
 ('GLASFLOSS INDUSTRIES INC','P.O. BOX 150469','','DALLAS           TX 75315',NULL,NULL,NULL,'214-741-7056','',NULL),
 ('GUIGNARD MECHANICAL SYS.','2109 DUTCH VALLEY RD.','','KNOXVILLE        TN 37918',NULL,NULL,NULL,'423-219-9912','',NULL),
 ('EASTERN SALES & MARKETING','GREGORY SUPPLY, DIV. OF','P.O. BOX 4052','CLEVELAND,       TN 37320-4052',NULL,NULL,NULL,'800-813-8760','423-479-9006',NULL),
 ('GRAINGER','P.O. BOX 419267','DEPT 416 - 80454511','KANSAS CITY      MO 64141-6267',NULL,NULL,NULL,'423-588-2956','',NULL),
 ('HERO FABRIDUCT, INC.','SUITE 700','820 SCENIC HWY.','LOOKOUT MTN.     TN 37350',NULL,NULL,NULL,'423-822-7777','423-822-0307',NULL),
 ('CARLISLE COATINGS','LOCKBOX 22723','22723 NETWORK PLACE','CHICAGO          IL 60673-1227',NULL,NULL,NULL,'800-338-8701','918-227-0603',NULL),
 ('INDUSTRIAL ENGINEERING &','EQUIPMENT CO. - INDEECO','P.O. BOX 790379','ST. LOUIS        MO 63179',NULL,NULL,NULL,'314-644-4300','314-644-5332',NULL),
 ('JER-AIR MFG., INC.','P.O. BOX 656','','MCINTOSH         FL 32664',NULL,NULL,NULL,'352-591-2674','',NULL),
 ('JOHNSON CONTROLS, INC.','P.O. BOX 905240','','CHARLOTTE,       NC 28290-5240',NULL,NULL,NULL,'','',NULL),
 ('KELSO-REGEN ASSOCIATES','6709 KINGSTON PIKE','','KNOXVILLE,       TN 37919',NULL,NULL,NULL,'588-5348','',NULL),
 ('KNOX BRADLEY MECH.CONTRS.','3511 OVERLOOK CIRCLE','','KNOXVILLE,       TN 37909',NULL,NULL,NULL,'584-3077','',NULL),
 ('KENTUCKIANA CURB CO.,INC.','2716 GRASSLAND DR.','','LOUISVILLE       KY 40299',NULL,NULL,NULL,'502-491-9880','',NULL),
 ('HEAT RECOVERY SYSTEMS','505 INDUSTRIAL DRIVE','','CARMEL,          IN 46032',NULL,NULL,NULL,'317-848-2745','BILL-RETIRED',NULL),
 ('KEES, INC.','P.O. BOX L','','ELKHART LAKE     WS 53020',NULL,NULL,NULL,'920-876-3391','920-876-3065',NULL),
 ('KEI','P.O. BOX 922','','DEKALB,          IL 60115',NULL,NULL,NULL,'800-233-6530','815-825-2687',NULL),
 ('GOINS HEATING & A/C','P.O. BOX 52','','GRAYSVILLE,      TN 37338',NULL,NULL,NULL,'423-775-0773','423-775-3874',NULL),
 ('KNOX FIRE EXTINGUISHER CO','DIV. OF DESCO CARBONIC','1201 UNIVERSITY AVE.','KNOXVILLE        TN 37921',NULL,NULL,NULL,'423-523-7710','',NULL),
 ('KNOXVILLE RUBBER & GASKET','5800 MIDDLEBROOK PIKE','','KNOXVILLE        TN 37921',NULL,NULL,NULL,'','',NULL),
 ('AIR SYSTEM COMPONENTS','','P.O. BOX 102249','ATLANTA          GA 30368-0249',NULL,NULL,NULL,'214-918-8875','',NULL),
 ('RICKARD AIR DIFFUSION,INC','7711 WELBORN STREET','SUITE 110','RALEIGH,         NC 27615',NULL,NULL,NULL,'919-862-0002','919-862-0212',NULL),
 ('M & A SUPPLY CO., INC.','1540 AMHERST ROAD BLDG. A','','KNOXVILLE,       TN 37909',NULL,NULL,NULL,'800-264-0820','615-889-1797',NULL),
 ('MARKEL PRODUCTS CO.','P.O. BOX 94150','','CHICAGO          IL 60690',NULL,NULL,NULL,'423-477-4131','423-477-0064',NULL),
 ('LELLYETT & ROGERS COMPANY','1717 LEBANON ROAD','','NASHVILLE,       TN 37210-3206',NULL,NULL,NULL,'615-316-0780','615-316-9517',NULL),
 ('MARLEY ENGINEERED PRODS','P.O. BOX 99124','','CHICAGO          IL 60693',NULL,NULL,NULL,'800-452-4179','843-479-8448',NULL),
 ('LAKE POINTE ADVERTISING','SPECIALTIES, LLC','1996 HIGHWAY 75','BLOUNTVILLE,     TN 37617',NULL,NULL,NULL,'','',NULL),
 ('MARS SALES CO., INC.','14716 SOUTH BROADWAY','','GARDENA          CA 90248',NULL,NULL,NULL,'213-770-1555','',NULL),
 ('MODERN SUPPLY COMPANY','P.O. BOX 22997','','KNOXVILLE        TN 37933-0997',NULL,NULL,NULL,'966-4567','966-2019',NULL),
 ('MODINE MFG. COMPANY','P.O. BOX 75234','','CHARLOTTE        NC 28275-0234',NULL,NULL,NULL,'262-636-1839','262-636-1818',NULL),
 ('METAL-FAB, INC.','P.O. BOX 3610','','WICHITA,         KS 67201',NULL,NULL,NULL,'','',NULL),
 ('NCA MANUFACTURING, INC.','1036 S JUPITER, SUITE 100','','GARLAND          TX 75042',NULL,NULL,NULL,'972-276-5002','',NULL),
 ('NCA MFG. - CANADA','1735 MATTAWA AVE.','MISSISSAUGA','ONTARIO  L       4X 1K5',NULL,NULL,NULL,'214-276-5002','',NULL),
 ('PRISTINE SPRINGS','2029 BROOKSIDE LANE','','KINGSPORT,       TN 37660',NULL,NULL,NULL,'423-230-2440','866-889-7873',NULL),
 ('NORTEC INDUSTRIES, INC.','P.O. BOX 70726','','CHICAGO          IL 60673',NULL,NULL,NULL,'315-425-1255','',NULL),
 ('NORTHEASTERN SALES, INC.','605 E. WATAUGA AVE.','','JOHNSON CITY     TN 37601',NULL,NULL,NULL,'423-926-1441','423-926-0892',NULL),
 ('NORTHEASTERN SHEET METAL','P.O. BOX 246','','GOFFSTOWN        NH 03045',NULL,NULL,NULL,'603-497-4166','603-497-8518',NULL),
 ('JAMES M. PLEASANTS CO.INC','P.O. BOX 16706','','GREENSBORO       NC 27416',NULL,NULL,NULL,'800-365-9010','336-378-2588',NULL),
 ('PEACOCK SALES CO., INC.','3683 N. PEACHTREE RD.','','ATLANTA          GA 30341',NULL,NULL,NULL,'770-451-7905','770-458-3405',NULL),
 ('SCHWANK LTD.','5285 BRADCO BLVD.','MISSISSAUGA','ONTARIO             L4W 2A6',NULL,NULL,NULL,'905-712-4766','905-712-8336',NULL),
 ('NUAIRE SYSTEMS, INC.','P.O. BOX 12867','','KNOXVILLE,       TN 37912',NULL,NULL,NULL,'','',NULL),
 ('PERRY FIBERGLASS PRODUCTS','P.O. BOX 179','','AVON LAKE,       OH 44012',NULL,NULL,NULL,'440-930-7701','440-930-7717',NULL),
 ('R&I ENTERPRISE, INC.','3650 TURTLECREEK RD.','','LEBANON          OH 45036',NULL,NULL,NULL,'800-525-7172','888-992-9696',NULL),
 ('PRE TECH, INC.','1121','AVENUE G','ROSENBURG,       TX 77471',NULL,NULL,NULL,'281-344-8711','281-344-8677',NULL),
 ('PREFCO PRODUCTS, INC.','P.O. BOX 425','','BUCKINGHAM       PA 18912',NULL,NULL,NULL,'800-437-6653','',NULL),
 ('RAPID FIRE EQUIPMENT, INC','P.O. BOX 4307','','SEVIERVILLE,     TN 37864-4307',NULL,NULL,NULL,'453-0189','',NULL),
 ('R.S. ELY COMPANY','2269 OLD FRANKFORT PIKE','','LEXINGTON        KY 40510',NULL,NULL,NULL,'606-231-1599','',NULL),
 ('RELIABLE PRODUCTS','P.O. BOX 102335','','ATLANTA,         GA 30368-2335',NULL,NULL,NULL,'334-239-4621','334-684-3680',NULL),
 ('QUALITY LUMBER COMPANY','P.O. BOX 51428','5620 INDUSTRY LANE','KNOXVILLE,       TN 37950',NULL,NULL,NULL,'588-7431','588-7437',NULL),
 ('ROGERS & MORGAN, INC.','P.O. BOX 10846','','KNOXVILLE        TN 37939',NULL,NULL,NULL,'524-1100','',NULL),
 ('SPIRAL FITTINGS, INC.','P.O. BOX 467','655 PUNCHEON CREEK DRIVE','ANDREWS          SC 29510',NULL,NULL,NULL,'843-264-9038','843-264-5438',NULL),
 ('SYLRO SALES CORPORATION','SUNVENT INDUSTRIES','1 INDUSTRIAL DRIVE #26','PELHAM           NH 03076',NULL,NULL,NULL,'800-325-4115','603-595-4778',NULL),
 ('STOKES ELECTRIC CO., INC.','P.O. BOX 2503','','KNOXVILLE        TN 37901',NULL,NULL,NULL,'423-525-0351','',NULL),
 ('FASTENAL COMPANY, THE','P.O. BOX 978','','WINONA           MN 55987-0978',NULL,NULL,NULL,'','',NULL),
 ('AIR ALLIANCE  LLC','P.O. BOX 52465','','KNOXVILLE        TN 37950-2465',NULL,NULL,NULL,'450-9770','450-9353',NULL),
 ('SULLIVAN COUNTY TRUSTEE','P.O. BOX 550','','BLOUNTVILLE,     TN 37617-0550',NULL,NULL,NULL,'','',NULL),
 ('THERMAL ENGINEERING CO.','P.O BOX 12046','','ATLANTA          GA 30355',NULL,NULL,NULL,'','',NULL),
 ('THERMAL PRODUCTS CORP.','412 NORTH 6th AVENUE','','TUSCON           AZ 85705',NULL,NULL,NULL,'800-420-1153','',NULL),
 ('SPIRAL FAB','606B SOUTH SPRING ST.','','FULTON,          MS 38843',NULL,NULL,NULL,'662-862-7999','662-862-7699',NULL),
 ('TJERNLUND PRODUCTS, INC.','1601 NINTH STREET','','WHITE BEAR LAKE  MN 55110-6794',NULL,NULL,NULL,'800-255-4208','651-426-9547',NULL),
 ('THE ELY COMPANY','2269 FRANKFORT COURT','','LEXINGTON        KY 40510',NULL,NULL,NULL,'859-231-1599','859-231-1591',NULL),
 ('TRIANGLE METAL AND MFG CO','P.O. DRAWER 38271','','HOUSTON          TX 77238',NULL,NULL,NULL,'800-232-6374','',NULL),
 ('THE HOME DEPOT','','','',NULL,NULL,NULL,'','',NULL),
 ('TUTCO INC.','P.O. BOX 8500-7025','','PHILADELPHIA     PA 19178-7025',NULL,NULL,NULL,'931-432-7253','931-432-4140',NULL),
 ('TWIN CITY FAN CO., LTD.','SDS 12-1256','P.O BOX 86','MINNEAPOLIS      MN 55486-1256',NULL,NULL,NULL,'763-551-7573','763-551-7601',NULL),
 ('R & J MFG OF GAINESVILLE','P.O. BOX 1216','','GAINESVILLE,     FL 32602-1216',NULL,NULL,NULL,'352-375-3130','',NULL),
 ('ULTRATECH, INC.','P.O. BOX 465','','GARNER           NC 27529',NULL,NULL,NULL,'919-779-2004','',NULL),
 ('LEADER INDUSTRIES','UNITED AIR DAMPERS','P.O. BOX 40913','NASHVILLE        TN 37204',NULL,NULL,NULL,'615-256-3500','',NULL),
 ('CREATIVE METALS','DIV. OF LEADER INDUSTRIES','P.O. BOX 40913','NASHVILLE        TN 37204',NULL,NULL,NULL,'615-256-3500','',NULL),
 ('VENTFABRICS, INC.','5520 NORTH LYNCH AVE','','CHICAGO          IL 60630',NULL,NULL,NULL,'','',NULL),
 ('VIBRATION ELIMINATOR CO.','15 DIXON AVE.','','COPIAGUE         NY 11726',NULL,NULL,NULL,'631-841-4000','',NULL),
 ('VULCAIN ALARM, INC.','4005 MATTE BLVD.','UNIT G','BROSSARD,QUEBEC     J4Y 2P4',NULL,NULL,NULL,'800-563-2967','450-619-2525',NULL),
 ('VENTILATION MARKETING','SERVICES, INC.','141 E. CENTRAL AVE., SUITE 340','WINTER HAVEN     FL 33880',NULL,NULL,NULL,'','',NULL),
 ('EMMET M. WALSH ASSOC.,INC','2578-A OLD ROCKBRIDGE ROAD','','NORCROSS,        GA 30071',NULL,NULL,NULL,'770-449-4802','770-449-3636',NULL),
 ('WADE & ASSOCIATES','P.O. BOX 50754','','KNOXVILLE        TN 37950-0754',NULL,NULL,NULL,'','',NULL),
 ('W.F. (BILL) KNOWLES','COUNTY CLERK','ROOM 201, COURTHOUSE','CHATTANOOGA,     TN 37402',NULL,NULL,NULL,'423-209-6500','',NULL),
 ('WARD INDUSTRIES','P. O. BOX 102335','','ATLANTA,         GA 30368-2335',NULL,NULL,NULL,'800-466-9374','724-684-8697',NULL),
 ('WINKLER-BLASTGATES LLC','S84 W18887 ENTERPRISE DRIVE','P.O. BOX 690','MUSKEGO,         WI 53150-0690',NULL,NULL,NULL,'888-223-3782','262-679-6710',NULL),
 ('WITT BUILDING MATERIAL CO','P.O. BOX 51346','','KNOXVILLE        TN 37950-1346',NULL,NULL,NULL,'423-588-5331','',NULL),
 ('YOUNG REGULATOR','20910 MILES PARKWAY','','CLEVELAND        OH 44128',NULL,NULL,NULL,'216-663-5646','216-631-1830',NULL),
 ('WYATT INSURANCE SERV.,INC','P.O. BOX 31996','','KNOXVILLE        TN 37930-1996',NULL,NULL,NULL,'470-9654','ALSOBROOKS',NULL),
 ('CL WARD & FAMILY INC.','BOX 951486','','CLEVELAND,       OH 44193',NULL,NULL,NULL,'888-973-7600','724-292-9091',NULL),
 ('WORLDWIDE EXPRESS','P.O. BOX 30183','','KNOXVILLE        TN 37930-0183',NULL,NULL,NULL,'692-9905','IPC FEE - -',NULL),
 ('LINCOLN ASSOCIATES','540 POWDER SPRINGS ST.','SUITE 29E','MARIETTA,        GA 30064',NULL,NULL,NULL,'770-425-1500','770-425-1503',NULL),
 ('LINDA GOINS','','','',NULL,NULL,NULL,'','',NULL),
 ('McMAHAN MECHANICAL, INC.','6549 CREEKHEAD RD.','','KNOXVILLE,       TN 37909',NULL,NULL,NULL,'691-3857','',NULL),
 ('QWEXPRESS','13158 COLLECTIONS CENTER DRIVE','','CHICAGO,         IL 60693',NULL,NULL,NULL,'877-536-5526','',NULL),
 ('SEMCO DUCT & ACCOUSTICAL','PRODUCTS, INC.','P.O. BOX 404966','ATLANTA,         GA 30384-4966',NULL,NULL,NULL,'573-443-1481','',NULL),
 ('HART & COOLEY, INC.','P.O. BOX 102335','','ATLANTA          GA 30368-2335',NULL,NULL,NULL,'800-748-0392','616-392-4829',NULL),
 ('KNOXVILLE CONVENTION CTR','','','',NULL,NULL,NULL,'','',NULL),
 ('R J YOUNG OF CHATTANOOGA','P.O. BOX 41668','','NASHVILLE,       TN 37204-166',NULL,NULL,NULL,'423-892-3672','423-296-4356',NULL),
 ('JOHN H. COLEMAN CO., LLC','P.O. BOX 386','','KNOXVILLE        TN 37901',NULL,NULL,NULL,'525-5111','',NULL),
 ('A & E SPECIALTIES, INC.','P.O. BOX 921369','','NORCROSS,        GA 30010',NULL,NULL,NULL,'770-449-0680','770-263-7602',NULL),
 ('AEROLATOR SYSTEMS,INC.','2716 CHAMBER DRIVE','','MONROE           NC 28110',NULL,NULL,NULL,'704-289-9585','704-226-9242',NULL),
 ('AGC','249 NEAL DRIVE','','BLOUNTVILLE      TN 37617',NULL,NULL,NULL,'423-323-7121','',NULL),
 ('ALL YEAR LAWN CARE','9601 HIGHLANDER WAY','','KNOXVILLE,       TN 37922',NULL,NULL,NULL,'470-3160','',NULL),
 ('BLUECROSS BLUESHIELD TN','RECEIPTS DEPARTMENT','P.O. BOX 180172','CHATTANOOGA      TN 37401-7172',NULL,NULL,NULL,'','',NULL),
 ('CHANCEY & REYNOLDS','614  VAN STREET','','KNOXVILLE        TN 37921',NULL,NULL,NULL,'423-525-5076','',NULL),
 ('THE CASEY C. JONES INS CO','P.O. BOX 52608','316 NANCY LYNN DR., SUITE 1','KNOXVILLE        TN 37950',NULL,NULL,NULL,'588-9744','',NULL),
 ('COMCAST','P.O. BOX 827554','','PHILADELPHIA     PA 19182-7554',NULL,NULL,NULL,'800-316-1619','',NULL),
 ('COMBUSTION RESEARCH CORP.','2516 LEACH ROAD','','ROCHESTER HILLS  MI 48309',NULL,NULL,NULL,'248-852-3611','SCULTHORPE',NULL),
 ('CROUCH FLORIST & GIFTS','7200 KINGSTON PIKE','','KNOXVILLE        TN 37919-5601',NULL,NULL,NULL,'558-0011','',NULL),
 ('CHARLES SCHWAB & COMPANY','1950 SUMMIT PARK PLACE','SUITE 400','ORLANDO,         FL 32810-5938',NULL,NULL,NULL,'281-7077','',NULL),
 ('CURBS PLUS, INC.','1200 CARLINE RD.','','ROSSVILLE        GA 30741',NULL,NULL,NULL,'706-858-1188','706-866-2339',NULL),
 ('DANNY DAVIS ELECTRICAL','CONTRACTORS','P.O. BOX 4866','MARYVILLE        TN 37802-4866',NULL,NULL,NULL,'984-4885','984-1998',NULL),
 ('KAREN DURKEE','P.O. BOX 624','','PINEY FLATS,     TN 37686',NULL,NULL,NULL,'423-538-0161','423-538-0661',NULL),
 ('ELIZABETHTON/CARTER CO.','CHAMBER OF COMMERCE','','',NULL,NULL,NULL,'','',NULL),
 ('ERIE INSURANCE GROUP','100 ERIE INSURANCE PLACE','','ERIE             PA 16530-1105',NULL,NULL,NULL,'470-9654','470-9431',NULL),
 ('ROBERT L. GREELEY, SR.','','','',NULL,NULL,NULL,'986-2833','',NULL),
 ('LESLIE G. MARRA','','','',NULL,NULL,NULL,'','',NULL),
 ('ROBERT L. GREELEY, JR.','1223 WALNUT BRANCH LANE','','KNOXVILLE        TN 37922',NULL,NULL,NULL,'675-3848','',NULL),
 ('JEFF R. GREELEY','P.O. BOX 277','','PINEY FLATS      TN 37686',NULL,NULL,NULL,'423-282-4116','423-391-4332',NULL),
 ('ROBERT L. GREELEY, III','1223 WALNUT BRANCH LANE','','KNOXVILLE,       TN 37922',NULL,NULL,NULL,'','',NULL),
 ('ENVIRO-SYSTEMS, INC.','236 ADMIRAL RD.','','KNOXVILLE        TN 37922',NULL,NULL,NULL,'966-2033','966-2038',NULL),
 ('ESTES FREIGHT LINES','P.O. BOX 25612','','RICHMOND         VA 23260-5612',NULL,NULL,NULL,'423-909-0683','',NULL),
 ('ABF FREIGHT SYSTEM, INC.','2818 TEXAS AVE.','','KNOXVILLE        TN 37921',NULL,NULL,NULL,'','',NULL),
 ('AAA COOPER TRANSPORTATION','P.O. BOX 6827','','DOTHAN           AL 36023',NULL,NULL,NULL,'','',NULL),
 ('ADVANCED TRANSPORTATION','P.O. BOX 719','','MILWAUK          EE 53201-0719',NULL,NULL,NULL,'','',NULL),
 ('AIRBORNE EXPRESS','P.O. BOX 91001','','SEATTLE          WA 98111',NULL,NULL,NULL,'800-722-0081','',NULL),
 ('AMERICAN FREIGHTWAYS','P.O. BOX 910150','','DALLAS           TX 75391-0150',NULL,NULL,NULL,'','',NULL),
 ('AVERITT EXPRESS, INC.','P.O. BOX 3145','','COOKEVILLE       TN 38502-3145',NULL,NULL,NULL,'','',NULL),
 ('CENTRAL TRANSPORT','P.O. BOX 80','','WARREN           MI 48090',NULL,NULL,NULL,'','',NULL),
 ('CAROLINA FREIGHT CORP.','P.O. BOX 905121','','CHARLOTTE        NC 28290-5121',NULL,NULL,NULL,'','',NULL),
 ('CF CONSOLIDATED FREIGHTW','P.O. BOX 730415','','DALLAS,          TX 75373-0415',NULL,NULL,NULL,'','',NULL),
 ('CONWAY CENTRAL EXPRESS','CCX / CNWY','P.O. BOX 360054','PITTSBURGH       PA 15250-6054',NULL,NULL,NULL,'','',NULL),
 ('THE CUSTOM COMPANIES','P.O. BOX 94338','','CHICAGO          IL 60678-4338',NULL,NULL,NULL,'','',NULL),
 ('DONCO TRANSPORTATION','P.O. BOX 8312','','DES MOINES       IA 50301-8312',NULL,NULL,NULL,'','',NULL),
 ('USF DUGAN, INC.','21141 NETWORK PLACE','','CHICAGO,         IL 60673-1211',NULL,NULL,NULL,'316-941-3000','',NULL),
 ('FORT DEARBORN LIFE INS CO','36788 EAGLE WAY','','CHICAGO,         IL 60678-1367',NULL,NULL,NULL,'800-348-4512','312-240-0143',NULL),
 ('FEDEX','P.O. BOX 1140','','MEMPHIS          TN 38101-1140',NULL,NULL,NULL,'800-238-5355','',NULL),
 ('FREDRICKSON MOTOR EXPRESS','P.O. BOX 5369','','CHARLOTTE        NC 28225-53',NULL,NULL,NULL,'','',NULL),
 ('GOLDEN STRIP TRANSFER INC','P.O. BOX 458','','SIMPSONVIL       LE SC29681',NULL,NULL,NULL,'','',NULL),
 ('HUMBOLDT EXPRESS INC.','P.O. DRAWER T-4022','','NASHVILLE        TN 37244',NULL,NULL,NULL,'','',NULL),
 ('FAULKNER/HAYNES ASSOC.INC','P.O. BOX 900002','','RALEIGH,         NC 27675-900',NULL,NULL,NULL,'919-863-1386','',NULL),
 ('L & D TRANSPORTATION, INC','P.O. BOX 22774','','KNOXVILLE        TN 37933',NULL,NULL,NULL,'539-1285','',NULL),
 ('FLANDERS PRECISION AIRE','P.O. BOX 70042','','SANTA ANA,       CA 92725-0042',NULL,NULL,NULL,'727-822-4411','888-594-2565',NULL),
 ('MILAN EXPRESS CO., INC.','P.O. DRAWER T577','','NASHVILLE        TN 37244',NULL,NULL,NULL,'','',NULL),
 ('OLD DOMINION FREIGHT LINE','P.O. BOX 60908','','CHARLOTTE,       NC 28260-0908',NULL,NULL,NULL,'','',NULL),
 ('OVERNITE TRANSPORTATION','P.O. BOX 79755','','BALTIMORE,       MD 21279-0755',NULL,NULL,NULL,'','',NULL),
 ('FOUR SEASONS COMFORT','2109 DUTCH VALLEY RD.','','KNOXVILLE        TN 37918',NULL,NULL,NULL,'423-546-1990','',NULL),
 ('PRESTON TRUCKING CO., INC','P.O. BOX 277084','','ATLANTA,         GA 30384-7084',NULL,NULL,NULL,'','',NULL),
 ('LIBERTY TRANSPORT','1147 ELLIS STREET','','BENSENVILLE,     IL 60106',NULL,NULL,NULL,'708-344-6200','',NULL),
 ('RANDOLPH TRUCKING INC.','1818 HOLSTON RIVER ROAD','','KNOXVILLE        TN 37914',NULL,NULL,NULL,'','',NULL),
 ('ROADRUNNER FREIGHT SYSTEM','P.O. BOX 8903','','CUDUHY           WS 53110-8903',NULL,NULL,NULL,'','',NULL),
 ('ROADWAY EXPRESS','P.O. BOX 905587','','CHARLOTTE,       NC 28290-5587',NULL,NULL,NULL,'','',NULL),
 ('ROYAL CHOICE CARRIERS','P.O. BOX 100541','','ATLANTA          GA 30384',NULL,NULL,NULL,'','',NULL),
 ('R & L CARRIERS','P.O. BOX 271','','WILMINGTON       OH 451770271',NULL,NULL,NULL,'','',NULL),
 ('SOUTHEASTERN FRT LINE','P.O. BOX 1691','','COLUMBIA         SC 29202',NULL,NULL,NULL,'','',NULL),
 ('SKYLINE TRANSPORT, INC.','10732 DUTCHTOWN ROAD','','KNOXVILLE        TN 37932',NULL,NULL,NULL,'','',NULL),
 ('SPARTAN','P.O. BOX 1050','                        .','GREER            SC 29652-1050',NULL,NULL,NULL,'','',NULL),
 ('SERVICEONE TRANSPORTATION','P.O. BOX 1437','','SHEBOYGAN,       WI 53082-1437',NULL,NULL,NULL,'920-208-7576','920-208-7580',NULL),
 ('SPI INTERNATIONAL','TRANSPORTATION','P.O. BOX 691','POINT ROBERTS,   WA 98281',NULL,NULL,NULL,'604-946-0227','604-946-0210',NULL),
 ('SERVICE TRANSPORT, INC.','135 S LASALLE, DEPT 2673','','CHICAGO,         IL 60674-2673',NULL,NULL,NULL,'','',NULL),
 ('STEELMAN TRANSPORTATION','P.O. BOX 21820 DEPT. 1301','','TULSA,           OK 74121-1820',NULL,NULL,NULL,'417-831-6300','',NULL),
 ('FENCO SUPPLY CO., INC.','3017 OAKLAND AVE.','','JOHNSON CITY     TN 37601',NULL,NULL,NULL,'423-282-1472','423-282-9636',NULL),
 ('T.W. OWENS & SONS','P.O. BOX 100101','','ATLANTA          GA 30384',NULL,NULL,NULL,'','',NULL),
 ('TNT HOLLAND MOTOR EXPRESS','750 E. 40th STREET','','HOLLAND          MI 49423',NULL,NULL,NULL,'','',NULL),
 ('TRANSUS FREIGHT','P.O. BOX 6944','','ATLANTA          GA 30315',NULL,NULL,NULL,'','',NULL),
 ('TRI-STAR TRANSPORTATION','4909 BALL ROAD','','KNOXVILLE        TN 37931',NULL,NULL,NULL,'','',NULL),
 ('UNISHIPPERS','6350 LBJ FREEWAY','SUITE 162','DALLAS,          TX 75240-6505',NULL,NULL,NULL,'888-360-7447','',NULL),
 ('VOLUNTEER EXPRESS, INC.','P.O. BOX 100886','','NASHVILLE        TN 37224-0886',NULL,NULL,NULL,'','',NULL),
 ('WATKINS (WWAT)','P.O. BOX 95001','','LAKELAND         FL 33804-5001',NULL,NULL,NULL,'','',NULL),
 ('WILSON TRUCKING CORP.','P.O. BOX 200','','FISHERVILLE      VA 22939',NULL,NULL,NULL,'','',NULL),
 ('WEST BEND TRANSIT','& SERVICE COMPANY','P.O. BOX 477','WEST BEND        WI 53095-0477',NULL,NULL,NULL,'414-334-2386','',NULL),
 ('YELLOW FRT. SYSTEM, INC.','P.O. BOX 195175','','CHARLOTTE        NC 28290',NULL,NULL,NULL,'','',NULL),
 ('GMAC PMT PROCESSING CTR','P.O. BOX 9001951','','LOUISVILLE,      KY 40290-1951',NULL,NULL,NULL,'800-200-4622','',NULL),
 ('HOME FEDERAL BANK L/C','','','',NULL,NULL,NULL,'541-6008','5416968',NULL),
 ('HOME FEDERAL/PETTY CASH','PETTY CASH','','',NULL,NULL,NULL,'','',NULL),
 ('HOME FEDERAL/VISA','VISA','','',NULL,NULL,NULL,'813-882-9450','',NULL),
 ('HOME FEDERAL BANK','','','',NULL,NULL,NULL,'450-3406','',NULL),
 ('HOBBS & ASSOCIATES, INC.','P.O. BOX 10250','','VIRGINIA BEACH   VA 23450',NULL,NULL,NULL,'698-4016','',NULL),
 ('THE HONEYBAKED HAM CO.','','','',NULL,NULL,NULL,'584-8886','',NULL),
 ('JOHNSON CITY POWER BOARD','P.O. BOX 2058','','JOHNSON CITY     TN 37605-2058',NULL,NULL,NULL,'','',NULL),
 ('KALTHOFF FABRICATORS,INC.','P.O. BOX 20349','','KNOXVILLE        TN 37940',NULL,NULL,NULL,'423-522-3106','',NULL),
 ('KNOX AREA RESCUE MINISTRI','','P0 BOX 3352','KNOXVILLE        TN 37927-3352',NULL,NULL,NULL,'','',NULL),
 ('KNOXVILLE OFFICE SUPPLY','925 CENTRAL AVE.','','KNOXVILLE        TN 37917',NULL,NULL,NULL,'423-525-7345','',NULL),
 ('KROGER','','','',NULL,NULL,NULL,'','',NULL),
 ('LINDA G. MACKZUM','3529 MAPLE VALLEY LANE','','KNOXVILLE        TN 37931',NULL,NULL,NULL,'567-4201','',NULL),
 ('AMOCO','CUSTOMER SERVICE CENTER','','DES MOINES       IA 50364-0064',NULL,NULL,NULL,'','',NULL),
 ('BP OIL COMPANY','P.O. BOX 9076','','DES MOINES,      IA 50368-9076',NULL,NULL,NULL,'','',NULL),
 ('BELLSOUTH','P.O. BOX 105262','','ATLANTA          GA 30348-5262',NULL,NULL,NULL,'','',NULL),
 ('BELLSOUTH','P.O. BOX 105262','','ATLANTA          GA 30348-5262',NULL,NULL,NULL,'','',NULL),
 ('ExxonMobil','P.O. BOX 4597','','CAROL STREAM     IL 60197-4597',NULL,NULL,NULL,'','',NULL),
 ('KNOXVILLE UTILITIES','P.O. BOX 59017','','KNOXVILLE        TN 37950-9017',NULL,NULL,NULL,'','',NULL),
 ('KNOXVILLE BLUE PRINT','P.O. BOX 3293','','KNOXVILLE        TN 37927-3293',NULL,NULL,NULL,'525-0463','',NULL),
 ('CONTINENTAL ALARM SYS.INC','P.O. BOX 830','','BLUFF CITY,      TN 37618',NULL,NULL,NULL,'800-747-5638','',NULL),
 ('DUNN, MACDONALD, COLEMAN','AND REYNOLDS','6204 BAUM DRIVE','KNOXVILLE,       TN 37919',NULL,NULL,NULL,'525-0505','',NULL),
 ('OFFICE SUPPLY & EQUIP.','P.O. BOX 548','','KNOXVILLE        TN 37901-0548',NULL,NULL,NULL,'','',NULL),
 ('OFFICE DEPOT CREDIT PLAN','P.O. BOX 9020','','DES MOINES,      IA 50368-9020',NULL,NULL,NULL,'800-729-7744','',NULL),
 ('SMITH & SONS PRINTERS','6461 RUTLEDGE PIKE','','KNOXVILLE        TN 37924',NULL,NULL,NULL,'523-1419','521-7495',NULL),
 ('TEXACO / SHELL','P.O. BOX 9010','','DES MOINES       IA 50368-9010',NULL,NULL,NULL,'','',NULL),
 ('UPS','P.O. BOX 7247-0244','','PHILADELPHIA,    PA 19170-0001',NULL,NULL,NULL,'800-742-5877','',NULL),
 ('COLVIN & SON','P.O. BOX 117','','ALCOA,           TN 37701',NULL,NULL,NULL,'983-4501','',NULL),
 ('BRICE          GERALD','4622 TENNESSEE AVE.','','CHATTANOOGA      TN 37409',NULL,NULL,NULL,'825-5420','423-825-6932',NULL),
 ('MICHAEL HENDERSON','3681 TUCKALEECHEE PK','','MARYVILLE        TN 37801',NULL,NULL,NULL,'983-4501','',NULL),
 ('JACK L. PAYNE','3115 DOROTHY DR.','','MARYVILLE        TN 37804',NULL,NULL,NULL,'','',NULL),
 ('SOUTHEASTERN TELECOM, INC','P.O. BOX 292307','','NASHVILLE        TN 37229-2307',NULL,NULL,NULL,'','',NULL),
 ('FLEENOR SECURITY SYSTEMS','10446 COGDILL ROAD','','KNOXVILLE        TN 37932',NULL,NULL,NULL,'423-777-1199','',NULL),
 ('KNOXVILLE BUILDERS EXCH.','P.O. BOX 226','','KNOXVILLE        TN 37901-0226',NULL,NULL,NULL,'423-525-0443','',NULL),
 ('KNOXVILLE PHCC','P.O. BOX 52742','','KNOXVILLE        TN 37950-2742',NULL,NULL,NULL,'','',NULL),
 ('KNOX COUNTY CLERK','P.O. BOX 1566','','KNOXVILLE        TN 37901',NULL,NULL,NULL,'','',NULL),
 ('TAPHCC','408 CEDAR BLUFF ROAD','SUITE 260','KNOXVILLE        TN 37923',NULL,NULL,NULL,'531-7422','',NULL),
 ('JENKINS & JENKINS','ATTORNEYS, PLLC','2121 FIRST TN PLAZA','KNOXVILLE        TN 37929-2121',NULL,NULL,NULL,'423-524-1873','',NULL),
 ('SWISHER INTERNATIONAL','P.O. BOX 473526','','CHARLOTTE        NC 27247-3526',NULL,NULL,NULL,'423-558-0500','',NULL),
 ('ORKIN PEST CONTROL','ORKIN EXTERMINATING','P.O. BOX 10007','KNOXVILLE        TN 37939-0007',NULL,NULL,NULL,'588-1303','',NULL),
 ('SAM TALBOTT','138 LAKEVIEW LANE','','MAYNARDVILLE     TN 37807',NULL,NULL,NULL,'','',NULL),
 ('NACM','NAT\'L ASSOC. CREDIT MNGT.','P.O. BOX 6809','KNOXVILLE        TN 37914',NULL,NULL,NULL,'546-0452','525-6908',NULL),
 ('NEXTEL PARTNERS','P.O. BOX 4192','','CAROL STREAM,    IL 60197-4192',NULL,NULL,NULL,'423-6603','423-6650',NULL),
 ('KINKO\'S, INC.','P.O. BOX 672085','','DALLAS,          TX 75267-2085',NULL,NULL,NULL,'800-488-3705','',NULL),
 ('NEXTEL COMMUNICATIONS','P.O. BOX 4191','','CAROL STREAM     IL 60197-4191',NULL,NULL,NULL,'800-639-6111','',NULL),
 ('COMMERCIAL LIGHTING','1485 AMHERST ROAD','','KNOXVILLE,       TN 37909',NULL,NULL,NULL,'558-0080','558-6167',NULL),
 ('ITC DELTACOM','P.O. BOX 740597','','ATLANTA          GA 30374-0597',NULL,NULL,NULL,'246-2500','246-2520',NULL),
 ('ITC DELTACOM','P.O. BOX 740597','','ATLANTA          GA 30374-0597',NULL,NULL,NULL,'246-2500','246-2520',NULL),
 ('COMPUTER SYSTEMS PLUS','605 SEVIER AVE.','','KNOXVILLE,       TN 37920',NULL,NULL,NULL,'573-5303','',NULL),
 ('A-1 LOCK & SAFE, INC.','P.O. BOX 10508','','KNOXVILLE        TN 37939',NULL,NULL,NULL,'588-2314','',NULL),
 ('BORING & GOINS, P.C.','2927 ESSARY DRIVE','','KNOXVILLE,       TN 37918',NULL,NULL,NULL,'525-6233','251-1492',NULL),
 ('SUNSHINE ART STUDIOS,INC.','BUSINESS CLASS','51 DENSLOW ROAD','EAST LONGMEADOW  MA 01028',NULL,NULL,NULL,'800-873-7681','800-232-3633',NULL),
 ('CHATTANOOGA CITY TREASURE','101 E 11TH ST., ROOM 102','','CHATTANOOGA,     TN 37402-4284',NULL,NULL,NULL,'423-757-5195','',NULL),
 ('DONALD L. WENNER, JR.','1817 ROY DRIVE','','KNOXVILLE        TN 37923',NULL,NULL,NULL,'567-224','',NULL),
 ('GOLDEN GLOVES CHARITIES','','','',NULL,NULL,NULL,'','',NULL),
 ('HAMILTON COUNTY CLERK','ROOM 201','HAMILTON COUNTY COURTHOUSE','CHATTANOOGA,     TN 37402',NULL,NULL,NULL,'423-209-6500','',NULL),
 ('PC MAGAZINE','P.O.B. 54064','','BOULDER          CO 80322-4064',NULL,NULL,NULL,'','',NULL),
 ('JOHNSON CITY AMERICAN','LEAGUE','','',NULL,NULL,NULL,'','',NULL),
 ('KNOXVILLE TRACK CLUB','','','',NULL,NULL,NULL,'','',NULL),
 ('PLANTAG','3111 B-18 FORTUNE WAY','','WELLINGTON,      FL 33414',NULL,NULL,NULL,'800-289-8236','',NULL),
 ('SPRINT','P.O. BOX 96064','','CHARLOTTE,       NC 28296-0064',NULL,NULL,NULL,'','',NULL),
 ('CINTAS DOCUMENT MGMT','P.O. BOX 26110','','KNOXVILLE,       TN 37912',NULL,NULL,NULL,'688-7979','688-7983',NULL),
 ('STRUCTURED GLASS, INC.','P.O. BOX 12631','','KNOXVILLE,       TN 37912-0631',NULL,NULL,NULL,'637-0631','523-6877',NULL),
 ('TRI CORD VOICE & DATA','P.O. BOX 185','','DAYTON,          TN 37321-0185',NULL,NULL,NULL,'423-421-8029','',NULL),
 ('OFFICE EQUIPMENT','FINANCE SERVICES','P.O. BOX 790448','ST. LOUIS,       MO 63179-0448',NULL,NULL,NULL,'507-532-7754','',NULL),
 ('MILLS-WILSON-GEORGE, INC.','P.O. BOX 627','','MEMPHIS          TN 38101',NULL,NULL,NULL,'901-373-5100','',NULL),
 ('PITNEY BOWES PURCHASE PWR','P.O. BOX 856042','','LOUISVILLE,      KY 40285-6042',NULL,NULL,NULL,'800-243-7800','',NULL),
 ('RESERVE ACCOUNT','P.O. BOX 856056','','LOUISVILLE,      KY 40285-6056',NULL,NULL,NULL,'80048510412','',NULL),
 ('PITNEY BOWES CREDIT CORP','P.O. BOX 856460','','LOUISVILLE       KY 40285-6460',NULL,NULL,NULL,'800-451-0412','',NULL),
 ('PUGH & COMPANY BENEFIT','CONSULTANTS, LLC','315 N.CEDAR BLUFF RD.SUITE 200','KNOXVILLE,       TN 37923',NULL,NULL,NULL,'769-9395','',NULL),
 ('POSTMASTER','P.O. BOX FEE PAYMENT','','PINEY FLATS,     TN 37686-9998',NULL,NULL,NULL,'','',NULL),
 ('POSTMASTER','WINDOW SERVICE','1237 E WEISGARBER RD','KNOXVILLE,       TN 37950-9998',NULL,NULL,NULL,'','',NULL),
 ('PRICE INDUSTRIES, INC.','P.O. BOX 609','2975 SHAWNEE RIDGE COURT','SUWANEE          GA 30024',NULL,NULL,NULL,'770-623-6085','770-623-6927',NULL),
 ('PROFESSIONAL TECHNOLOGIES','P.O. BOX 20098','','KNOXVILLE        TN 37940',NULL,NULL,NULL,'546-3475','',NULL),
 ('PTS OFFICE AUTOMATION,INC','P.O. BOX 11043','','KNOXVILLE,       TN 37939-1043',NULL,NULL,NULL,'588-9823','588-5070',NULL),
 ('ROBERT ORR SYSCO','','','',NULL,NULL,NULL,'588-5726','',NULL),
 ('ROOFTOP SYSTEMS, INC.','P.O. BOX 670762','','DALLAS           TX 75367-0762',NULL,NULL,NULL,'972-247-7447','',NULL),
 ('SOUTHERN PNEUMATICS','JOE HILL CO., INC.','P.O. BOX 6333','KNOXVILLE        TN 37914',NULL,NULL,NULL,'','',NULL),
 ('JEANIE F. GAMMON','SULLIVAN COUNTY CLERK','P.O. BOX 530','BLOUNTVILLE,     TN 37617',NULL,NULL,NULL,'','',NULL),
 ('SHEET METAL COMPANY','6120 AIRWAYS BLVD','','CHATTANOOGA      TN 37421',NULL,NULL,NULL,'855-5955','',NULL),
 ('SHERWIN WILLIAMS','4720 N BROADWAY','','KNOXVILLE        TN 37918',NULL,NULL,NULL,'687-5650','687-0217',NULL),
 ('TAX TRUST ACCT.','SALES TAX DIVISION','P.O. BOX 830725','BIRMINGHAM,      AL 35283-0725',NULL,NULL,NULL,'','',NULL),
 ('CITY OF BIRMINGHAM','REVENUE DIVISION','P.O. BOX 10566','BIRMINGHAM,      AL 35296-0001',NULL,NULL,NULL,'205-254-2198','',NULL),
 ('FLORIDA DEPT. OF REVENUE','5050 W. TENNESSEE ST.','','TALLAHASSEE      FL 32399-0125',NULL,NULL,NULL,'','',NULL),
 ('FRANKLIN COUNTY','SALES TAX DIVISION','P.O. BOX 3989','MUSCLE SHOALS,   AL 35662',NULL,NULL,NULL,'','',NULL),
 ('GEORGIA DEPT. OF REVENUE','SALES & USE TAX DIVISION','P.O. BOX 105296','ATLANTA          GA 30348-5296',NULL,NULL,NULL,'','',NULL),
 ('HUNTSVILLE CITY','SALES & USE TAX DIVISION','P.O. BOX 308','HUNTSVILLE,      AL 35804',NULL,NULL,NULL,'256-427-5068','',NULL),
 ('MADISON COUNTY','SALES & USE TAX DIVISION','100 NORTHSIDE SQUARE','HUNTSVILLE       AL 35801-4820',NULL,NULL,NULL,'256-532-3498','',NULL),
 ('JEFFERSON COUNTY','DEPARTMENT OF REVENUE','','BIRMINGHAM,      AL 35203',NULL,NULL,NULL,'205-325-5180','205-325-5695',NULL),
 ('KENTUCKY STATE TREASURER','REVENUE CABINET','','FRANKFORT,       KY 40619',NULL,NULL,NULL,'','',NULL),
 ('COMPTROLLER OF THE TREAS','COMPLIANCE DIVISION','301 W. PRESTON ST., ROOM 203','BALTIMORE,       MD 21201-2383',NULL,NULL,NULL,'410-767-1300','',NULL),
 ('NC DEPT. OF REVENUE','P.O. BOX 25000','','RALEIGH          NC 27640',NULL,NULL,NULL,'','',NULL),
 ('NEW YORK STATE SALES TAX','','','',NULL,NULL,NULL,'800-225-5829','',NULL),
 ('OHIO DEPT OF TAXATION','SALES & USE TAX DIVISION','P.O. BOX 530','COLUMBUS,        OH 43216-0530',NULL,NULL,NULL,'888-405-4039','',NULL),
 ('OKLAHOMA TAX COMMISSION','P.O. BOX 26850','','OKLAHOMA CITY,   OK 73126-0850',NULL,NULL,NULL,'405-521-3279','',NULL),
 ('PA DEPT.OF REV.BUREAU OF','BUS.TRUST FUND TAX DISC.DIV.','DEPT. 280901','HARRISBURG,      PA 17128-0901',NULL,NULL,NULL,'717-772-9249','LIGHTNER',NULL),
 ('SC DEPT. OF REVENUE','SALES TAX RETURN','','COLUMBIA         SC 29214-0102',NULL,NULL,NULL,'','',NULL),
 ('TENN. DEPT. OF REVENUE','ANDREW JACKSON STATE OFC BLDG','500 DEADERICK STREET','NASHVILLE        TN 37242-0700',NULL,NULL,NULL,'','',NULL),
 ('VIRGINIA DEPT.OF TAXATION','','P.O. BOX 26626','RICHMOND         VA 23261-6626',NULL,NULL,NULL,'','',NULL),
 ('WEST VIRGINIA STATE TAX','DEPT. INTERNAL AUDITING DIV.','P.O. BOX 425','CHARLESTON,      WV 25322-0425',NULL,NULL,NULL,'800-982-8297','',NULL),
 ('MONTGOMERY CITY','ATTN: SALES TAX MONTGOMERY','P.O. BOX 1111','MONTGOMERY,      AL 36101-1111',NULL,NULL,NULL,'334-241-2037','',NULL),
 ('ALABAMA DEPARTMENT','OF REVENUE','P.O. BOX 831199','BIRMINGHAM,      AL 35283-1199',NULL,NULL,NULL,'334-241-2037','',NULL),
 ('STAMPED FITTINGS','P.O. BOX 2146','','ELMIRA HEIGHTS   NY 14903',NULL,NULL,NULL,'607-733-9988','888-343-3676',NULL),
 ('STEPHEN FOSSLER COMPANY','439 SOUTH DARTMOOR DRIVE','','CRYSTAL LAKE     IL 60014-9978',NULL,NULL,NULL,'800-762-0030','',NULL),
 ('TN STATE BUSINESS TAX','','','',NULL,NULL,NULL,'','',NULL),
 ('MIKE LOWE','KNOX COUNTY TRUSTEE','P.O. BOX 70','KNOXVILLE        TN 37901-0070',NULL,NULL,NULL,'423-546-3306','',NULL),
 ('THE CITY OF KNOXVILLE','BUSINESS TAX DIVISION','P.O. BOX 1028','KNOXVILLE        TN 37901-1028',NULL,NULL,NULL,'215-2083','',NULL),
 ('TAPP A/C & HEATING CO.INC','774 CROSS STREET','','ROSSVILLE        GA 30741',NULL,NULL,NULL,'706-866-2607','',NULL),
 ('TN DEPT OF LABOR AND','WORKFORCE DEV.EMP.ACCT.OPER.','P.O. BOX 101','NASHVILLE        TN 37202-0101',NULL,NULL,NULL,'','',NULL),
 ('TENN SECRETARY OF STATE','SUITE 1800','JAMES K. POLK BUILDING','NASHVILLE        TN 37243-0306',NULL,NULL,NULL,'','',NULL),
 ('CARL E. LEVI','HAMILTON COUNTY TRUSTEE','RM 210 HAMILTON CO.COURTHOUSE','CHATTANOOGA,     TN 37402',NULL,NULL,NULL,'423-209-7270','',NULL),
 ('BILL BENNETT','ASSESSOR OF PROP.,HAMILTON CO.','6135 HERITAGE PARK DRIVE','CHATTANOOGA,     TN 37416',NULL,NULL,NULL,'423-209-7323','',NULL),
 ('BOB ICENHOUR','SULLIVAN COUNTY ASSESSOR','3411 HWY. 126, SUITE 103','BLOUNTVILLE,     TN 37617',NULL,NULL,NULL,'','',NULL),
 ('INTERNAL REVENUE SERVICE','P.O. BOX 660264','','DALLAS,          TX 75266-0264',NULL,NULL,NULL,'','',NULL),
 ('SULLIVAN COUNTY CLERK','JEANIE F. GAMMON','3258 HWY. 126 RM. 101','BLOUNTVILLE,     TN 37617',NULL,NULL,NULL,'423-323-6435','',NULL),
 ('F.M. GEORGE SAFE & LOCK','P.O. BOX 3398','717 EAST MAGNOLIA AVE','KNOXVILLE        TN 37917',NULL,NULL,NULL,'522-0841','',NULL),
 ('THERMOCOPY OF TENNESSEE','P. O. BOX 10665','','KNOXVILLE        TN 37939',NULL,NULL,NULL,'423-524-1124','',NULL),
 ('TPI CORP.','P.O. BOX 94150','','CHICAGO          IL 60696',NULL,NULL,NULL,'423-477-4131','423-477-4125',NULL),
 ('CITY OF KNOXVILLE','P.O. BOX 59031','','KNOXVILLE        TN 37950-9031',NULL,NULL,NULL,'','',NULL),
 ('VIRGINIA DEPT OF TAXATION','P.O. BOX 1777','','RICHMOND         VA 23218-1777',NULL,NULL,NULL,'','',NULL),
 ('UNITED ENERTECH','P.O. BOX 71659','','CHATTANOOGA      TN 37407',NULL,NULL,NULL,'423-698-7715','423-698-6629',NULL),
 ('UNITED WAY','OF GREATER KNOXVILLE','P.O. BOX 326','KNOXVILLE,       TN 37901',NULL,NULL,NULL,'523-9131','522-7312',NULL),
 ('USF HOLLAND INC.','P.O. BOX 9021','','HOLLAND          MI 49422-9021',NULL,NULL,NULL,'','',NULL),
 ('WASTE MANAGEMENT','KNOXVILLE','P.O. BOX 9001054','LOUISVILLE,      KY 40290-1054',NULL,NULL,NULL,'423-525-0529','',NULL);
/*!40000 ALTER TABLE `convert_vendors` ENABLE KEYS */;


--
-- Definition of table `costate`
--

DROP TABLE IF EXISTS `costate`;
CREATE TABLE `costate` (
  `coStateID` int(11) NOT NULL DEFAULT '0',
  `State` varchar(2) DEFAULT NULL,
  `Description` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`coStateID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `costate`
--

/*!40000 ALTER TABLE `costate` DISABLE KEYS */;
INSERT INTO `costate` (`coStateID`,`State`,`Description`) VALUES 
 (1,'AK','Alaska'),
 (2,'AL','Alabama'),
 (3,'AR','Arkansas'),
 (4,'AZ','Arizona '),
 (5,'CA','California '),
 (6,'CO','Colorado '),
 (7,'CT','Connecticut'),
 (8,'DC','District Of Columbia'),
 (9,'DE','Delaware'),
 (10,'FL','Florida'),
 (11,'GA','Georgia'),
 (12,'HI','Hawaii'),
 (13,'IA','Iowa'),
 (14,'ID','Idaho'),
 (15,'IL','Illinois'),
 (16,'IN','Indiana'),
 (17,'KS','Kansas'),
 (18,'KY','Kentucky'),
 (19,'LA','Louisiana'),
 (20,'MA','Massachusetts'),
 (21,'MD','Maryland'),
 (22,'ME','Maine'),
 (23,'MI','Michigan'),
 (24,'MN','Minnesota'),
 (25,'MO','Missouri'),
 (26,'MS','Mississippi'),
 (27,'MT','Montana'),
 (28,'NC','North Carolina'),
 (29,'ND','North Dakota'),
 (30,'NE','Nebraska'),
 (31,'NH','New Hampshire'),
 (32,'NJ','New Jersey'),
 (33,'NM','New Mexico'),
 (34,'NV','Nevada'),
 (35,'NY','New York'),
 (36,'OH','Ohio'),
 (37,'OK','Oklahoma'),
 (38,'OR','Oregon'),
 (39,'PA','Pennsylvania'),
 (40,'RI','Rhode Island'),
 (41,'SC','South Carolina'),
 (42,'SD','South Dakota'),
 (43,'TN','Tennessee'),
 (44,'TX','Texas'),
 (45,'UT','Utah'),
 (46,'VA','Virginia '),
 (47,'VT','Vermont'),
 (48,'WA','Washington'),
 (49,'WI','Wisconsin'),
 (50,'WV','West Virginia'),
 (51,'WY','Wyoming');
/*!40000 ALTER TABLE `costate` ENABLE KEYS */;


--
-- Definition of table `cotaxterritory`
--

DROP TABLE IF EXISTS `cotaxterritory`;
CREATE TABLE `cotaxterritory` (
  `coTaxTerritoryID` int(11) NOT NULL AUTO_INCREMENT,
  `County` varchar(50) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `CountyCode` varchar(6) DEFAULT NULL,
  `TaxRate` decimal(19,4) DEFAULT '0.0000',
  `Distribution1` decimal(19,4) DEFAULT '0.0000',
  `Distribution2` decimal(19,4) DEFAULT '0.0000',
  `Distribution3` decimal(19,4) DEFAULT '0.0000',
  `Distribution4` decimal(19,4) DEFAULT '0.0000',
  `Distribution5` decimal(19,4) DEFAULT '0.0000',
  `Distribution6` decimal(19,4) DEFAULT '0.0000',
  `Distribution7` decimal(19,4) DEFAULT '0.0000',
  `Distribution8` decimal(19,4) DEFAULT '0.0000',
  `HasSurtax` tinyint(1) DEFAULT '0',
  `SurtaxCap` decimal(19,4) DEFAULT '0.0000',
  `SurtaxRate` decimal(19,4) DEFAULT '0.0000',
  `MultiRate` tinyint(1) DEFAULT '0',
  `Rate2` decimal(19,4) DEFAULT NULL,
  `From2` decimal(19,4) DEFAULT NULL,
  `Rate3` decimal(19,4) DEFAULT NULL,
  `From3` decimal(19,4) DEFAULT NULL,
  `Rate4` decimal(19,4) DEFAULT NULL,
  `From4` decimal(19,4) DEFAULT NULL,
  `Rate5` decimal(19,4) DEFAULT NULL,
  `From5` decimal(19,4) DEFAULT NULL,
  `Rate6` decimal(19,4) DEFAULT NULL,
  `From6` decimal(19,4) DEFAULT NULL,
  `TaxShipping` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`coTaxTerritoryID`),
  KEY `CountyCode` (`CountyCode`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cotaxterritory`
--

/*!40000 ALTER TABLE `cotaxterritory` DISABLE KEYS */;
/*!40000 ALTER TABLE `cotaxterritory` ENABLE KEYS */;


--
-- Definition of table `cuinvoice`
--

DROP TABLE IF EXISTS `cuinvoice`;
CREATE TABLE `cuinvoice` (
  `cuInvoiceID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `TransactionStatus` int(11) DEFAULT '1',
  `Applied` tinyint(1) DEFAULT '0',
  `joReleaseDetailID` int(11) DEFAULT NULL,
  `cuSOID` int(11) DEFAULT NULL,
  `rxCustomerID` int(11) DEFAULT NULL,
  `rxBillToID` int(11) DEFAULT NULL,
  `rxBillToAddressID` int(11) DEFAULT NULL,
  `rxShipToID` int(11) DEFAULT NULL,
  `rxShipToAddressID` int(11) DEFAULT NULL,
  `veShipViaID` int(11) DEFAULT NULL,
  `prFromWarehouseID` int(11) DEFAULT NULL,
  `prToWarehouseID` int(11) DEFAULT NULL,
  `cuTermsID` int(11) DEFAULT NULL,
  `ShipToMode` tinyint(4) NOT NULL DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `QuickJobName` varchar(40) DEFAULT NULL,
  `InvoiceDate` datetime DEFAULT NULL,
  `ShipDate` datetime DEFAULT NULL,
  `DueDate` datetime DEFAULT NULL,
  `PrintDate` datetime DEFAULT NULL,
  `InvoiceAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `AppliedAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `TaxAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `Freight` decimal(19,4) DEFAULT '0.0000',
  `CostTotal` decimal(19,4) DEFAULT '0.0000',
  `Subtotal` decimal(19,4) DEFAULT '0.0000',
  `TaxTotal` decimal(19,4) DEFAULT '0.0000',
  `TaxRate` decimal(19,4) DEFAULT '0.0000',
  `cuAssignmentID0` int(11) DEFAULT NULL,
  `cuAssignmentID1` int(11) DEFAULT NULL,
  `cuAssignmentID2` int(11) DEFAULT NULL,
  `cuAssignmentID3` int(11) DEFAULT NULL,
  `cuAssignmentID4` int(11) DEFAULT NULL,
  `TrackingNumber` varchar(30) DEFAULT NULL,
  `SurtaxOverrideCap` tinyint(1) DEFAULT '0',
  `SurtaxTotal` decimal(19,4) DEFAULT '0.0000',
  `SurtaxRate` decimal(19,4) DEFAULT '0.0000',
  `SurtaxAmount` decimal(19,4) DEFAULT '0.0000',
  `SingleItemTaxAmount` decimal(19,4) DEFAULT NULL,
  `coDivisionID` int(11) DEFAULT NULL,
  `FreightCost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`cuInvoiceID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `cuAssignmentID0` (`cuAssignmentID0`),
  KEY `cuAssignmentID1` (`cuAssignmentID1`),
  KEY `cuAssignmentID2` (`cuAssignmentID2`),
  KEY `cuAssignmentID3` (`cuAssignmentID3`),
  KEY `cuAssignmentID4` (`cuAssignmentID4`),
  KEY `cuSOID` (`cuSOID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `joReleaseDetailID` (`joReleaseDetailID`),
  KEY `prFromWarehouseID` (`prFromWarehouseID`),
  KEY `prToWarehouseID` (`prToWarehouseID`),
  KEY `rxBillToAddressID` (`rxBillToAddressID`),
  KEY `rxBillToID` (`rxBillToID`),
  KEY `rxCustomerID` (`rxCustomerID`),
  KEY `rxShipToAddressID` (`rxShipToAddressID`),
  KEY `rxShipToID` (`rxShipToID`),
  KEY `InvoiceNumber` (`InvoiceNumber`),
  KEY `veShipViaID` (`veShipViaID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuinvoice`
--

/*!40000 ALTER TABLE `cuinvoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuinvoice` ENABLE KEYS */;


--
-- Definition of table `cuinvoicedetail`
--

DROP TABLE IF EXISTS `cuinvoicedetail`;
CREATE TABLE `cuinvoicedetail` (
  `cuInvoiceDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceID` int(11) DEFAULT '0',
  `cuSODetailID` int(11) DEFAULT NULL,
  `prMasterID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityBilled` decimal(19,4) DEFAULT NULL,
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `UnitPrice` decimal(19,4) DEFAULT NULL,
  `PriceMultiplier` decimal(19,4) DEFAULT NULL,
  `Taxable` tinyint(1) DEFAULT '0',
  `HasSingleItemTaxAmount` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cuInvoiceDetailID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `cuSODetailID` (`cuSODetailID`),
  KEY `cuInvoiceDetailID` (`cuInvoiceDetailID`),
  KEY `prMasterID` (`prMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuinvoicedetail`
--

/*!40000 ALTER TABLE `cuinvoicedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuinvoicedetail` ENABLE KEYS */;


--
-- Definition of table `cuinvoicedetailworkfile`
--

DROP TABLE IF EXISTS `cuinvoicedetailworkfile`;
CREATE TABLE `cuinvoicedetailworkfile` (
  `cuInvoiceDetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceDetailID` int(11) DEFAULT '0',
  `cuInvoiceID` int(11) DEFAULT '0',
  `cuSODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `UnitPrice` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Taxable` tinyint(1) DEFAULT '0',
  `HasSingleItemTaxAmount` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cuInvoiceDetailWorkFileID`),
  UNIQUE KEY `cuInvoiceDetailWorkFileID` (`cuInvoiceDetailWorkFileID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `cuSODetailID` (`cuSODetailID`),
  KEY `cuInvoiceDetailID` (`cuInvoiceDetailID`),
  KEY `prMasterID` (`prMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuinvoicedetailworkfile`
--

/*!40000 ALTER TABLE `cuinvoicedetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuinvoicedetailworkfile` ENABLE KEYS */;


--
-- Definition of table `cuinvoicepayworkfile`
--

DROP TABLE IF EXISTS `cuinvoicepayworkfile`;
CREATE TABLE `cuinvoicepayworkfile` (
  `cuInvoicePayWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceID` int(11) DEFAULT '0',
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `InvoiceDate` datetime DEFAULT NULL,
  `InvoiceBalance` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `DiscountUsed` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `PaymentApplied` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `Remaining` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `InvoiceAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `PrevDiscountUsed` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `PrevPaymentApplied` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`cuInvoicePayWorkFileID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `cuInvoicePayWorkFileID` (`cuInvoicePayWorkFileID`),
  KEY `InvoiceNumber` (`InvoiceNumber`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuinvoicepayworkfile`
--

/*!40000 ALTER TABLE `cuinvoicepayworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuinvoicepayworkfile` ENABLE KEYS */;


--
-- Definition of table `culinkagedetail`
--

DROP TABLE IF EXISTS `culinkagedetail`;
CREATE TABLE `culinkagedetail` (
  `cuLinkageDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceID` int(11) DEFAULT '0',
  `cuReceiptID` int(11) DEFAULT '0',
  `rxCustomerID` int(11) DEFAULT NULL,
  `PaymentApplied` decimal(19,4) DEFAULT '0.0000',
  `DiscountUsed` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`cuLinkageDetailID`),
  KEY `cuLinkageDetailID` (`cuLinkageDetailID`),
  KEY `cuReceiptID` (`cuReceiptID`),
  KEY `rxCustomerID` (`rxCustomerID`),
  KEY `cuInvoiceID` (`cuInvoiceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `culinkagedetail`
--

/*!40000 ALTER TABLE `culinkagedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `culinkagedetail` ENABLE KEYS */;


--
-- Definition of table `cumaster`
--

DROP TABLE IF EXISTS `cumaster`;
CREATE TABLE `cumaster` (
  `cuMasterID` int(11) NOT NULL DEFAULT '0',
  `cuTermsID` int(11) DEFAULT NULL,
  `cuAssignmentID0` int(11) DEFAULT NULL,
  `cuAssignmentID1` int(11) DEFAULT NULL,
  `cuAssignmentID2` int(11) DEFAULT NULL,
  `cuAssignmentID3` int(11) DEFAULT NULL,
  `cuAssignmentID4` int(11) DEFAULT NULL,
  `PORequired` tinyint(1) DEFAULT '0',
  `cuMasterTypeID` int(11) DEFAULT NULL,
  `PriceLevel` tinyint(4) DEFAULT '0',
  `CreditHold` tinyint(1) DEFAULT '0',
  `CreditLimit` decimal(19,4) DEFAULT '0.0000',
  `CreditHoldOverride` datetime DEFAULT NULL,
  `TaxExemptNumber` varchar(30) DEFAULT NULL,
  `AccountNumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`cuMasterID`),
  KEY `cuAssignmentID0` (`cuAssignmentID0`),
  KEY `cuAssignmentID1` (`cuAssignmentID1`),
  KEY `cuAssignmentID2` (`cuAssignmentID2`),
  KEY `cuAssignmentID3` (`cuAssignmentID3`),
  KEY `cuAssignmentID4` (`cuAssignmentID4`),
  KEY `cuMasterTypeID` (`cuMasterTypeID`),
  KEY `cuTermsID` (`cuTermsID`),
  KEY `cuMasterID` (`cuMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cumaster`
--

/*!40000 ALTER TABLE `cumaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `cumaster` ENABLE KEYS */;


--
-- Definition of table `cumastertype`
--

DROP TABLE IF EXISTS `cumastertype`;
CREATE TABLE `cumastertype` (
  `cuMasterTypeID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(6) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`cuMasterTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cumastertype`
--

/*!40000 ALTER TABLE `cumastertype` DISABLE KEYS */;
/*!40000 ALTER TABLE `cumastertype` ENABLE KEYS */;


--
-- Definition of table `cureceipt`
--

DROP TABLE IF EXISTS `cureceipt`;
CREATE TABLE `cureceipt` (
  `cuReceiptID` int(11) NOT NULL AUTO_INCREMENT,
  `rxCustomerID` int(11) DEFAULT NULL,
  `cuReceiptTypeID` tinyint(4) DEFAULT NULL,
  `ReceiptDate` datetime NOT NULL,
  `Reference` varchar(12) DEFAULT NULL,
  `Memo` varchar(50) DEFAULT NULL,
  `Amount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`cuReceiptID`),
  KEY `cuReceiptTypeID` (`cuReceiptTypeID`),
  KEY `cuReceiptID` (`cuReceiptID`),
  KEY `rxCustomerID` (`rxCustomerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cureceipt`
--

/*!40000 ALTER TABLE `cureceipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `cureceipt` ENABLE KEYS */;


--
-- Definition of table `cureceipttype`
--

DROP TABLE IF EXISTS `cureceipttype`;
CREATE TABLE `cureceipttype` (
  `cuReceiptTypeID` tinyint(4) NOT NULL DEFAULT '0',
  `Description` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`cuReceiptTypeID`),
  KEY `cuReceiptTypeID` (`cuReceiptTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cureceipttype`
--

/*!40000 ALTER TABLE `cureceipttype` DISABLE KEYS */;
INSERT INTO `cureceipttype` (`cuReceiptTypeID`,`Description`) VALUES 
 (1,'Cash'),
 (2,'Check'),
 (3,'Credit Card'),
 (4,'Other');
/*!40000 ALTER TABLE `cureceipttype` ENABLE KEYS */;


--
-- Definition of table `cuship`
--

DROP TABLE IF EXISTS `cuship`;
CREATE TABLE `cuship` (
  `cuShipID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `cuSOID` int(11) DEFAULT '0',
  `ShipDate` datetime DEFAULT NULL,
  PRIMARY KEY (`cuShipID`),
  KEY `cuSOID` (`cuSOID`),
  KEY `cuShipID` (`cuShipID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuship`
--

/*!40000 ALTER TABLE `cuship` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuship` ENABLE KEYS */;


--
-- Definition of table `cushipdetail`
--

DROP TABLE IF EXISTS `cushipdetail`;
CREATE TABLE `cushipdetail` (
  `cuShipDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `cuShipID` int(11) DEFAULT '0',
  `cuSODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityShipped` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`cuShipDetailID`),
  KEY `cuShipDetailID` (`cuShipDetailID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `cuSODetailID` (`cuSODetailID`),
  KEY `cuShipID` (`cuShipID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cushipdetail`
--

/*!40000 ALTER TABLE `cushipdetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `cushipdetail` ENABLE KEYS */;


--
-- Definition of table `cuso`
--

DROP TABLE IF EXISTS `cuso`;
CREATE TABLE `cuso` (
  `cuSOID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `TransactionStatus` int(11) DEFAULT '1',
  `joReleaseID` int(11) DEFAULT NULL,
  `rxCustomerID` int(11) DEFAULT '0',
  `rxBillToID` int(11) DEFAULT NULL,
  `rxBillToAddressID` int(11) DEFAULT NULL,
  `rxShipToID` int(11) DEFAULT NULL,
  `rxShipToAddressID` int(11) DEFAULT NULL,
  `veShipViaID` int(11) DEFAULT NULL,
  `prFromWarehouseID` int(11) DEFAULT NULL,
  `prToWarehouseID` int(11) DEFAULT NULL,
  `cuTermsID` int(11) DEFAULT NULL,
  `ShipToMode` tinyint(4) NOT NULL DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  `SONumber` varchar(20) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `DatePromised` varchar(12) DEFAULT NULL,
  `QuickJobName` varchar(40) DEFAULT NULL,
  `OrderDate` datetime DEFAULT NULL,
  `ShipDate` datetime DEFAULT NULL,
  `Freight` decimal(19,4) DEFAULT '0.0000',
  `CostTotal` decimal(19,4) DEFAULT '0.0000',
  `SubTotal` decimal(19,4) DEFAULT '0.0000',
  `TaxTotal` decimal(19,4) DEFAULT '0.0000',
  `TaxRate` decimal(19,4) DEFAULT '0.0000',
  `cuAssignmentID0` int(11) DEFAULT NULL,
  `cuAssignmentID1` int(11) DEFAULT NULL,
  `cuAssignmentID2` int(11) DEFAULT NULL,
  `cuAssignmentID3` int(11) DEFAULT NULL,
  `cuAssignmentID4` int(11) DEFAULT NULL,
  `TrackingNumber` varchar(30) DEFAULT NULL,
  `SurtaxOverrideCap` tinyint(1) DEFAULT '0',
  `SurtaxTotal` decimal(19,4) DEFAULT '0.0000',
  `SurtaxRate` decimal(19,4) DEFAULT '0.0000',
  `SurtaxAmount` decimal(19,4) DEFAULT '0.0000',
  `SingleItemTaxAmount` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`cuSOID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `cuAssignmentID0` (`cuAssignmentID0`),
  KEY `cuAssignmentID1` (`cuAssignmentID1`),
  KEY `cuAssignmentID2` (`cuAssignmentID2`),
  KEY `cuAssignmentID3` (`cuAssignmentID3`),
  KEY `cuAssignmentID4` (`cuAssignmentID4`),
  KEY `cuSOID` (`cuSOID`),
  KEY `joReleaseID` (`joReleaseID`),
  KEY `SONumber` (`SONumber`),
  KEY `prFromWarehouseID` (`prFromWarehouseID`),
  KEY `prToWarehouseID` (`prToWarehouseID`),
  KEY `rxBillToAddressID` (`rxBillToAddressID`),
  KEY `rxBillToID` (`rxBillToID`),
  KEY `rxCustomerID` (`rxCustomerID`),
  KEY `rxShipToAddressID` (`rxShipToAddressID`),
  KEY `rxShipToID` (`rxShipToID`),
  KEY `cuTermsID` (`cuTermsID`),
  KEY `veShipViaID` (`veShipViaID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuso`
--

/*!40000 ALTER TABLE `cuso` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuso` ENABLE KEYS */;


--
-- Definition of table `cusodetail`
--

DROP TABLE IF EXISTS `cusodetail`;
CREATE TABLE `cusodetail` (
  `cuSODetailID` int(11) NOT NULL AUTO_INCREMENT,
  `cuSOID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityOrdered` decimal(19,4) DEFAULT '0.0000',
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `UnitPrice` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Taxable` tinyint(1) DEFAULT '0',
  `HasSingleItemTaxAmount` tinyint(1) NOT NULL DEFAULT '0',
  `joSchedDetailID` int(11) DEFAULT NULL,
  PRIMARY KEY (`cuSODetailID`),
  KEY `cuSOID` (`cuSOID`),
  KEY `cuSODetailID` (`cuSODetailID`),
  KEY `prMasterID` (`prMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cusodetail`
--

/*!40000 ALTER TABLE `cusodetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `cusodetail` ENABLE KEYS */;


--
-- Definition of table `cusodetailworkfile`
--

DROP TABLE IF EXISTS `cusodetailworkfile`;
CREATE TABLE `cusodetailworkfile` (
  `cuSODetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `cuSODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityOrdered` decimal(19,4) DEFAULT '0.0000',
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `UnitPrice` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Taxable` tinyint(1) DEFAULT '0',
  `HasSingleItemTaxAmount` tinyint(1) NOT NULL DEFAULT '0',
  `joSchedDetailID` int(11) DEFAULT NULL,
  PRIMARY KEY (`cuSODetailWorkFileID`),
  UNIQUE KEY `cuSODetailWorkFileID` (`cuSODetailWorkFileID`),
  KEY `cuSODetailID` (`cuSODetailID`),
  KEY `prMasterID` (`prMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cusodetailworkfile`
--

/*!40000 ALTER TABLE `cusodetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `cusodetailworkfile` ENABLE KEYS */;


--
-- Definition of table `cuterms`
--

DROP TABLE IF EXISTS `cuterms`;
CREATE TABLE `cuterms` (
  `cuTermsID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(35) DEFAULT NULL,
  `InActive` tinyint(1) NOT NULL DEFAULT '0',
  `DueDays` int(11) NOT NULL DEFAULT '0',
  `DiscountDays` int(11) NOT NULL DEFAULT '0',
  `DiscountPercent` decimal(19,4) DEFAULT '0.0000',
  `DueOnDay` tinyint(1) NOT NULL DEFAULT '0',
  `DiscOnDay` tinyint(1) NOT NULL DEFAULT '0',
  `OrderNote` longtext,
  `PickTicketNote` longtext,
  `PickTicketNote1` varchar(31) DEFAULT NULL,
  `PickTicketNote2` varchar(31) DEFAULT NULL,
  `PickTicketNote3` varchar(31) DEFAULT NULL,
  `PickTicketNote4` varchar(31) DEFAULT NULL,
  `PickTicketNote5` varchar(31) DEFAULT NULL,
  PRIMARY KEY (`cuTermsID`),
  KEY `cuTermsID` (`cuTermsID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `cuterms`
--

/*!40000 ALTER TABLE `cuterms` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuterms` ENABLE KEYS */;


--
-- Definition of table `datablelist`
--

DROP TABLE IF EXISTS `datablelist`;
CREATE TABLE `datablelist` (
  `Name` varchar(255) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `datablelist`
--

/*!40000 ALTER TABLE `datablelist` DISABLE KEYS */;
INSERT INTO `datablelist` (`Name`) VALUES 
 ('ARCUSTYP'),
 ('coAccount'),
 ('coBalance'),
 ('coFiscalPeriod'),
 ('coFiscalYear'),
 ('coLedgerDetail'),
 ('coLedgerDetailWorkFile'),
 ('coLedgerHeader'),
 ('coLedgerPostingErrors'),
 ('coLedgerSource'),
 ('coState'),
 ('coTaxTerritory'),
 ('cuInvoice'),
 ('cuInvoiceDetail'),
 ('cuInvoiceDetailWorkFile'),
 ('cuInvoicePayWorkFile'),
 ('cuLinkageDetail'),
 ('cuMaster'),
 ('cuMasterType'),
 ('cuReceipt'),
 ('cuReceiptType'),
 ('cuShip'),
 ('cuShipDetail'),
 ('cuSO'),
 ('cuSODetail'),
 ('cuSODetailWorkFile'),
 ('cuTerms'),
 ('daTableList'),
 ('ecInvoiceHeader'),
 ('ecInvoices'),
 ('ecInvoiceSplit'),
 ('ecJobs'),
 ('ecPeriod'),
 ('ecSalesMgrOverride'),
 ('ecSplitInvoice'),
 ('ecSplitJob'),
 ('ecSplitRelease'),
 ('ecStatement'),
 ('emMaster'),
 ('ep1099WorkFile'),
 ('epCoDeduction'),
 ('epCoDepartment'),
 ('epCoEarning'),
 ('epCoLiability'),
 ('epDepDeduction'),
 ('epDepEarning'),
 ('epDepLiability'),
 ('epEmpDeduction'),
 ('epEmpEarning'),
 ('epEmpLiability'),
 ('epTranDeduction'),
 ('epTranEarning'),
 ('epTranLiability'),
 ('epTransaction'),
 ('epW2WorkFile'),
 ('epW3WorkFile'),
 ('JobChanges'),
 ('joBidder'),
 ('joChange'),
 ('joCustPO'),
 ('joCustPOWorkFile'),
 ('joDodgeExcludeType'),
 ('joDodgeWork'),
 ('joJournal'),
 ('joMaster'),
 ('joQuoteDetail'),
 ('joQuoteDetailWorkFile'),
 ('joQuoteHeader'),
 ('joQuoteTemplateDetail'),
 ('joQuoteTemplateHeader'),
 ('joRelease'),
 ('joReleaseDetail'),
 ('joStatus'),
 ('joSubmittal'),
 ('moAccount'),
 ('moLinkage'),
 ('moLinkageDetail'),
 ('moPayrollCheckTemp'),
 ('moTransaction'),
 ('moTransactionType'),
 ('moVendorCheckTemp'),
 ('prAdjustment'),
 ('prDepartment'),
 ('prInventoryCount'),
 ('prMaster'),
 ('prOrderPoint'),
 ('prTransfer'),
 ('prTransferDetail'),
 ('prTransferDetailWorkFile'),
 ('prWarehouse'),
 ('prWarehouseInventory'),
 ('rxAddress'),
 ('rxAddressWorkFile'),
 ('rxContact'),
 ('rxContactWorkFile'),
 ('rxJournal'),
 ('rxJournalType'),
 ('rxJournalWorkFile'),
 ('rxMaster'),
 ('sysAccountLinkage'),
 ('sysAssignment'),
 ('sysConfigArea'),
 ('sysConfigModule'),
 ('sysConfigVariables'),
 ('sysInfo'),
 ('sysLock'),
 ('sysPrivilege'),
 ('sysSequence'),
 ('sysTableLock'),
 ('sysVariable'),
 ('tlVerticalSpacing'),
 ('UserLoginClone'),
 ('veBill'),
 ('veBillDetail'),
 ('veBillDetailWorkFile'),
 ('veBillDistribution'),
 ('veBillDistributionWorkFile'),
 ('veBillPay'),
 ('veFreightCharges'),
 ('veMaster'),
 ('vePO'),
 ('vePODetail'),
 ('vePODetailWorkFile'),
 ('veReceive'),
 ('veReceiveDetail'),
 ('veReceiveWorkFile'),
 ('veShipVia'),
 ('WebAccount'),
 ('WorkFile');
/*!40000 ALTER TABLE `datablelist` ENABLE KEYS */;


--
-- Definition of table `ecinvoiceperiod`
--

DROP TABLE IF EXISTS `ecinvoiceperiod`;
CREATE TABLE `ecinvoiceperiod` (
  `ecInvoicePeriodID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceID` int(11) DEFAULT NULL,
  `ecPeriodID` int(11) DEFAULT NULL,
  `Modified` tinyint(1) DEFAULT '0',
  `rxCustomerID` int(11) DEFAULT NULL,
  `ReleaseType` tinyint(4) DEFAULT '0',
  `Reference` varchar(30) DEFAULT NULL,
  `DatePaid` datetime DEFAULT NULL,
  `Sales` decimal(19,4) DEFAULT '0.0000',
  `Profit` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecInvoicePeriodID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `DatePaid` (`DatePaid`),
  KEY `ecPeriodID` (`ecPeriodID`),
  KEY `rxCustomerID` (`rxCustomerID`)
) ENGINE=MyISAM AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecinvoiceperiod`
--

/*!40000 ALTER TABLE `ecinvoiceperiod` DISABLE KEYS */;
INSERT INTO `ecinvoiceperiod` (`ecInvoicePeriodID`,`cuInvoiceID`,`ecPeriodID`,`Modified`,`rxCustomerID`,`ReleaseType`,`Reference`,`DatePaid`,`Sales`,`Profit`) VALUES 
 (63,5,6,0,1,NULL,'100003','2004-05-22 00:00:00','12321.0000','12321.0000'),
 (132,1,1,0,1,NULL,'dddd','2004-08-16 00:00:00','0.0000','0.0000'),
 (133,2,1,0,1,NULL,'asdasd','2004-08-16 00:00:00','0.0000','0.0000'),
 (134,3,1,0,1,2,'458','2004-10-26 00:00:00','0.0000','0.0000'),
 (135,4,1,0,1,2,'A1234A1','2004-11-02 00:00:00','1000.0000','900.0000');
/*!40000 ALTER TABLE `ecinvoiceperiod` ENABLE KEYS */;


--
-- Definition of table `ecinvoicerepsplit`
--

DROP TABLE IF EXISTS `ecinvoicerepsplit`;
CREATE TABLE `ecinvoicerepsplit` (
  `ecInvoiceRepSplitID` int(11) NOT NULL AUTO_INCREMENT,
  `ecInvoicePeriodID` int(11) DEFAULT NULL,
  `ecStatementID` int(11) DEFAULT NULL,
  `Profit` decimal(19,4) DEFAULT '0.0000',
  `CommissionRate` decimal(19,4) DEFAULT '0.0000',
  `AmountDue` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecInvoiceRepSplitID`),
  KEY `ecInvoicePeriodID` (`ecInvoicePeriodID`),
  KEY `ecStatementID` (`ecStatementID`)
) ENGINE=MyISAM AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecinvoicerepsplit`
--

/*!40000 ALTER TABLE `ecinvoicerepsplit` DISABLE KEYS */;
INSERT INTO `ecinvoicerepsplit` (`ecInvoiceRepSplitID`,`ecInvoicePeriodID`,`ecStatementID`,`Profit`,`CommissionRate`,`AmountDue`) VALUES 
 (74,134,1,'0.0000','30.0000','0.0000'),
 (75,135,1,'900.0000','30.0000','135.0000'),
 (76,135,2,'900.0000','30.0000','135.0000');
/*!40000 ALTER TABLE `ecinvoicerepsplit` ENABLE KEYS */;


--
-- Definition of table `ecinvoices`
--

DROP TABLE IF EXISTS `ecinvoices`;
CREATE TABLE `ecinvoices` (
  `ecInvoicesID` int(11) NOT NULL AUTO_INCREMENT,
  `cuInvoiceID` int(11) DEFAULT NULL,
  `ecStatementID` int(11) DEFAULT NULL,
  `Profit` decimal(19,4) DEFAULT '0.0000',
  `CommissionRate` decimal(19,4) DEFAULT '0.0000',
  `AmountDue` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecInvoicesID`),
  KEY `cuInvoiceID` (`cuInvoiceID`),
  KEY `ecInvoicesID` (`ecInvoicesID`),
  KEY `ecStatementID` (`ecStatementID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecinvoices`
--

/*!40000 ALTER TABLE `ecinvoices` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecinvoices` ENABLE KEYS */;


--
-- Definition of table `ecjobs`
--

DROP TABLE IF EXISTS `ecjobs`;
CREATE TABLE `ecjobs` (
  `ecJobsID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT '0',
  `ecStatementID` int(11) DEFAULT '0',
  `ProfitToDate` decimal(19,4) DEFAULT '0.0000',
  `ProfitPaidOnToDate` decimal(19,4) DEFAULT '0.0000',
  `CommissionRate` decimal(19,4) DEFAULT '0.0000',
  `CommissionToDate` decimal(19,4) DEFAULT '0.0000',
  `PaidToDate` decimal(19,4) DEFAULT '0.0000',
  `AmountDue` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecJobsID`),
  KEY `ecStatementID` (`ecStatementID`),
  KEY `joMasterID` (`joMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecjobs`
--

/*!40000 ALTER TABLE `ecjobs` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecjobs` ENABLE KEYS */;


--
-- Definition of table `ecperiod`
--

DROP TABLE IF EXISTS `ecperiod`;
CREATE TABLE `ecperiod` (
  `ecPeriodID` int(11) NOT NULL AUTO_INCREMENT,
  `PeriodEndingDate` datetime DEFAULT NULL,
  PRIMARY KEY (`ecPeriodID`),
  KEY `ecPeriodID` (`ecPeriodID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecperiod`
--

/*!40000 ALTER TABLE `ecperiod` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecperiod` ENABLE KEYS */;


--
-- Definition of table `ecsalesmgroverride`
--

DROP TABLE IF EXISTS `ecsalesmgroverride`;
CREATE TABLE `ecsalesmgroverride` (
  `ecSalesMgrOverrideID` int(11) NOT NULL AUTO_INCREMENT,
  `RepLoginID` int(11) DEFAULT NULL,
  `ecStatementID` int(11) DEFAULT '0',
  `Profit` decimal(19,4) DEFAULT '0.0000',
  `CommissionRate` decimal(19,4) DEFAULT '0.0000',
  `AmountDue` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecSalesMgrOverrideID`),
  KEY `ecSalesMgrOverrideID` (`ecSalesMgrOverrideID`),
  KEY `ecStatementID` (`ecStatementID`),
  KEY `RepLoginID` (`RepLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecsalesmgroverride`
--

/*!40000 ALTER TABLE `ecsalesmgroverride` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecsalesmgroverride` ENABLE KEYS */;


--
-- Definition of table `ecsplitinvoice`
--

DROP TABLE IF EXISTS `ecsplitinvoice`;
CREATE TABLE `ecsplitinvoice` (
  `ecSplitJobID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  `Allocated` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecSplitJobID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `ecSplitJobID` (`ecSplitJobID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecsplitinvoice`
--

/*!40000 ALTER TABLE `ecsplitinvoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecsplitinvoice` ENABLE KEYS */;


--
-- Definition of table `ecsplitjob`
--

DROP TABLE IF EXISTS `ecsplitjob`;
CREATE TABLE `ecsplitjob` (
  `ecSplitJobID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `rxMasterID` int(11) DEFAULT NULL,
  `Allocated` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`ecSplitJobID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `ecSplitJobID` (`ecSplitJobID`)
) ENGINE=MyISAM AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecsplitjob`
--

/*!40000 ALTER TABLE `ecsplitjob` DISABLE KEYS */;
INSERT INTO `ecsplitjob` (`ecSplitJobID`,`joMasterID`,`rxMasterID`,`Allocated`) VALUES 
 (1,3099,NULL,'0.0000'),
 (2,3099,NULL,'0.0000'),
 (3,3099,NULL,'0.0000'),
 (4,6,10,'50.0000'),
 (5,6,15,'52.0000'),
 (6,10,NULL,'123.3333');
/*!40000 ALTER TABLE `ecsplitjob` ENABLE KEYS */;


--
-- Definition of table `ecsplitrelease`
--

DROP TABLE IF EXISTS `ecsplitrelease`;
CREATE TABLE `ecsplitrelease` (
  `ecSplitJobID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  `joReleaseID` int(11) DEFAULT '0',
  `Allocated` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`ecSplitJobID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joReleaseID` (`joReleaseID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `ecSplitJobID` (`ecSplitJobID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecsplitrelease`
--

/*!40000 ALTER TABLE `ecsplitrelease` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecsplitrelease` ENABLE KEYS */;


--
-- Definition of table `ecstatement`
--

DROP TABLE IF EXISTS `ecstatement`;
CREATE TABLE `ecstatement` (
  `ecStatementID` int(11) NOT NULL AUTO_INCREMENT,
  `ecPeriodID` int(11) DEFAULT NULL,
  `RepLoginID` int(11) DEFAULT NULL,
  `OpeningBalance` decimal(19,4) DEFAULT '0.0000',
  `JobCommissions` decimal(19,4) DEFAULT '0.0000',
  `OtherCommissions` decimal(19,4) DEFAULT '0.0000',
  `Overrides` decimal(19,4) DEFAULT '0.0000',
  `Adjustments` decimal(19,4) DEFAULT '0.0000',
  `Payment` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct1` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct2` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct3` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct4` decimal(19,4) DEFAULT '0.0000',
  `Comment` longtext,
  `Custom1` decimal(19,4) DEFAULT '0.0000',
  `Custom2` decimal(19,4) DEFAULT '0.0000',
  `Custom3` decimal(19,4) DEFAULT '0.0000',
  `Custom4` decimal(19,4) DEFAULT '0.0000',
  `Custom5` decimal(19,4) DEFAULT '0.0000',
  `Custom6` decimal(19,4) DEFAULT '0.0000',
  `Custom7` decimal(19,4) DEFAULT '0.0000',
  `Custom8` decimal(19,4) DEFAULT '0.0000',
  `Custom9` decimal(19,4) DEFAULT '0.0000',
  `Custom10` decimal(19,4) DEFAULT '0.0000',
  `Flags` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ecStatementID`),
  KEY `ecPeriodID` (`ecPeriodID`),
  KEY `ecStatementID` (`ecStatementID`),
  KEY `RepLoginID` (`RepLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ecstatement`
--

/*!40000 ALTER TABLE `ecstatement` DISABLE KEYS */;
/*!40000 ALTER TABLE `ecstatement` ENABLE KEYS */;


--
-- Definition of table `emmaster`
--

DROP TABLE IF EXISTS `emmaster`;
CREATE TABLE `emmaster` (
  `emMasterID` int(11) NOT NULL DEFAULT '0',
  `CommissionJobProfit` decimal(19,4) DEFAULT '0.0000',
  `CommissionInvoiceProfit` decimal(19,4) DEFAULT '0.0000',
  `UserLoginID` int(11) DEFAULT NULL,
  `GetsCommission` tinyint(1) DEFAULT '0',
  `Quota` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct1` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct2` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct3` decimal(19,4) DEFAULT '0.0000',
  `RepDeduct4` decimal(19,4) DEFAULT '0.0000',
  `ShowDirectory` tinyint(1) DEFAULT '0',
  `Location` varchar(20) DEFAULT NULL,
  `Position` varchar(20) DEFAULT NULL,
  `PhotoName` varchar(20) DEFAULT NULL,
  `Phone` varchar(20) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Comment` varchar(50) DEFAULT NULL,
  `WebName` varchar(35) DEFAULT NULL,
  `EmploymentStatus` tinyint(4) DEFAULT '0',
  `EmploymentType` tinyint(4) DEFAULT '0',
  `MaritalStatus` tinyint(4) DEFAULT '0',
  `Sex` varchar(1) DEFAULT NULL,
  `SSN` varchar(11) DEFAULT NULL,
  `BirthDate` datetime DEFAULT NULL,
  `HireDate` datetime DEFAULT NULL,
  `NextReviewDate` datetime DEFAULT NULL,
  `LastRaiseDate` datetime DEFAULT NULL,
  `TerminationDate` datetime DEFAULT NULL,
  `VacaType` tinyint(4) DEFAULT '0',
  `VacaStartDate` datetime DEFAULT NULL,
  `VacaHoursPerYear` decimal(19,4) DEFAULT '0.0000',
  `VacaHoursMaximum` decimal(19,4) DEFAULT '0.0000',
  `VacaHoursAvailable` decimal(19,4) DEFAULT '0.0000',
  `VacaCarryOverYear` tinyint(1) DEFAULT '0',
  `SickType` tinyint(4) DEFAULT '0',
  `SickStartDate` datetime DEFAULT NULL,
  `SickHoursPerYear` decimal(19,4) DEFAULT '0.0000',
  `SickHoursMaximum` decimal(19,4) DEFAULT '0.0000',
  `SickHoursAvailable` decimal(19,4) DEFAULT '0.0000',
  `SickCarryOverYear` tinyint(1) DEFAULT '0',
  `PayFrequency` tinyint(4) DEFAULT '0',
  `RetirementPlan` tinyint(1) DEFAULT '0',
  `StatutoryEmployee` tinyint(1) DEFAULT '0',
  `coStateID` int(11) DEFAULT '0',
  `epCoDepartmentID` int(11) DEFAULT '0',
  `FT_MaritalStatus` tinyint(4) DEFAULT '0',
  `FT_Exemptions` tinyint(4) DEFAULT '0',
  `FT_SupWH` decimal(19,4) DEFAULT '0.0000',
  `FT_SpecialCode` varchar(5) DEFAULT NULL,
  `ST_MaritalStatus` tinyint(4) DEFAULT '0',
  `ST_Exemptions` tinyint(4) DEFAULT '0',
  `ST_SupWH` decimal(19,4) DEFAULT '0.0000',
  `ST_SpecialCode` varchar(5) DEFAULT NULL,
  `LT_MaritalStatus` tinyint(4) DEFAULT '0',
  `LT_Exemptions` tinyint(4) DEFAULT '0',
  `LT_SupWH` decimal(19,4) DEFAULT '0.0000',
  `LT_SpecialCode` varchar(5) DEFAULT NULL,
  `JobNumberPrefix` varchar(3) DEFAULT NULL,
  `JobNumberSequence` int(11) DEFAULT NULL,
  `JobNumberGenerate` tinyint(1) DEFAULT '0',
  `DirectDeposit` tinyint(1) NOT NULL DEFAULT '0',
  `coDivisionID` int(11) DEFAULT NULL,
  PRIMARY KEY (`emMasterID`),
  UNIQUE KEY `emMasterID` (`emMasterID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `emmaster`
--

/*!40000 ALTER TABLE `emmaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `emmaster` ENABLE KEYS */;


--
-- Definition of table `ep1099workfile`
--

DROP TABLE IF EXISTS `ep1099workfile`;
CREATE TABLE `ep1099workfile` (
  `ep1099WorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Payer1` varchar(50) DEFAULT NULL,
  `Payer2` varchar(50) DEFAULT NULL,
  `Payer3` varchar(50) DEFAULT NULL,
  `Payer4` varchar(50) DEFAULT NULL,
  `PayerFIN` varchar(50) DEFAULT NULL,
  `RecipFIN` varchar(50) DEFAULT NULL,
  `RecipName` varchar(50) DEFAULT NULL,
  `RecipAddress1` varchar(50) DEFAULT NULL,
  `RecipAddress2` varchar(50) DEFAULT NULL,
  `RecipCityStateZip` varchar(50) DEFAULT NULL,
  `ControlNo` varchar(50) DEFAULT NULL,
  `Box7` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ep1099WorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ep1099workfile`
--

/*!40000 ALTER TABLE `ep1099workfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `ep1099workfile` ENABLE KEYS */;


--
-- Definition of table `epcodeduction`
--

DROP TABLE IF EXISTS `epcodeduction`;
CREATE TABLE `epcodeduction` (
  `epCoDeductionID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) DEFAULT '0',
  `Description` varchar(20) DEFAULT NULL,
  `W2Code` varchar(1) DEFAULT NULL,
  `BuiltIn` tinyint(1) NOT NULL DEFAULT '0',
  `CalculationType` tinyint(4) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  `Maximum` decimal(19,4) DEFAULT '0.0000',
  `FederalIncomeTaxExempt` tinyint(1) DEFAULT '0',
  `SocialSecurityExempt` tinyint(1) DEFAULT '0',
  `MedicareExempt` tinyint(1) DEFAULT '0',
  `StateIncomeTaxExempt` tinyint(1) DEFAULT '0',
  `LocalIncomeTaxExempt` tinyint(1) DEFAULT '0',
  `OtherExempt` tinyint(1) DEFAULT '0',
  `SortOrder` int(11) DEFAULT '0',
  PRIMARY KEY (`epCoDeductionID`),
  KEY `W2Code` (`W2Code`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epcodeduction`
--

/*!40000 ALTER TABLE `epcodeduction` DISABLE KEYS */;
INSERT INTO `epcodeduction` (`epCoDeductionID`,`InActive`,`Description`,`W2Code`,`BuiltIn`,`CalculationType`,`Amount`,`Maximum`,`FederalIncomeTaxExempt`,`SocialSecurityExempt`,`MedicareExempt`,`StateIncomeTaxExempt`,`LocalIncomeTaxExempt`,`OtherExempt`,`SortOrder`) VALUES 
 (1,0,'Federal Income Tax',NULL,1,NULL,'0.0000','0.0000',0,0,0,0,0,0,NULL),
 (2,0,'Social Security Tax',NULL,1,2,'6.2000','5449.8000',0,0,0,0,0,0,NULL),
 (3,0,'Medicare Tax',NULL,1,2,'1.4500','0.0000',0,0,0,0,0,0,NULL),
 (4,0,'State Income Tax',NULL,1,0,'0.0000','0.0000',0,0,0,0,0,0,NULL),
 (5,0,'Local Income Tax',NULL,1,NULL,'0.0000','0.0000',0,0,0,0,0,0,NULL),
 (6,0,'Insurance',NULL,0,0,'0.0000','0.0000',0,0,0,0,0,0,NULL),
 (7,0,'401K','D',0,3,'10.0000','1300.0000',1,0,0,1,1,0,NULL);
/*!40000 ALTER TABLE `epcodeduction` ENABLE KEYS */;


--
-- Definition of table `epcodepartment`
--

DROP TABLE IF EXISTS `epcodepartment`;
CREATE TABLE `epcodepartment` (
  `epCoDepartmentID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) DEFAULT '0',
  `Description` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`epCoDepartmentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epcodepartment`
--

/*!40000 ALTER TABLE `epcodepartment` DISABLE KEYS */;
/*!40000 ALTER TABLE `epcodepartment` ENABLE KEYS */;


--
-- Definition of table `epcoearning`
--

DROP TABLE IF EXISTS `epcoearning`;
CREATE TABLE `epcoearning` (
  `epCoEarningID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) DEFAULT '0',
  `Description` varchar(20) DEFAULT NULL,
  `PayType` tinyint(4) DEFAULT '0',
  `SortOrder` int(11) DEFAULT '0',
  PRIMARY KEY (`epCoEarningID`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epcoearning`
--

/*!40000 ALTER TABLE `epcoearning` DISABLE KEYS */;
INSERT INTO `epcoearning` (`epCoEarningID`,`InActive`,`Description`,`PayType`,`SortOrder`) VALUES 
 (1,0,'Salary',1,999),
 (2,0,'Hourly',2,999),
 (3,0,'Piece Work',3,999),
 (4,0,'Commission',4,999),
 (5,0,'Other',5,999),
 (6,0,'Overtime',2,999),
 (7,0,'Sick Leave',2,999),
 (8,0,'Vacation',2,999),
 (9,0,'Holiday',2,999);
/*!40000 ALTER TABLE `epcoearning` ENABLE KEYS */;


--
-- Definition of table `epcoliability`
--

DROP TABLE IF EXISTS `epcoliability`;
CREATE TABLE `epcoliability` (
  `epCoLiabilityID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) DEFAULT '0',
  `Description` varchar(20) DEFAULT NULL,
  `BuiltIn` tinyint(1) NOT NULL DEFAULT '0',
  `CalculationType` tinyint(4) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  `Maximum` decimal(19,4) DEFAULT '0.0000',
  `SortOrder` int(11) DEFAULT '0',
  PRIMARY KEY (`epCoLiabilityID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epcoliability`
--

/*!40000 ALTER TABLE `epcoliability` DISABLE KEYS */;
INSERT INTO `epcoliability` (`epCoLiabilityID`,`InActive`,`Description`,`BuiltIn`,`CalculationType`,`Amount`,`Maximum`,`SortOrder`) VALUES 
 (1,0,'Social Security',1,2,'6.2000','5449.8000',NULL),
 (2,0,'Medicare',1,2,'1.4500','87900.0000',NULL),
 (3,0,'FUTA',1,2,'0.0000','0.0000',NULL),
 (4,0,'SUTA',1,2,'0.0000','0.0000',NULL),
 (5,0,'SDI',1,2,'0.0000','0.0000',NULL);
/*!40000 ALTER TABLE `epcoliability` ENABLE KEYS */;


--
-- Definition of table `epdepdeduction`
--

DROP TABLE IF EXISTS `epdepdeduction`;
CREATE TABLE `epdepdeduction` (
  `epDepDeductionID` int(11) NOT NULL AUTO_INCREMENT,
  `epCoDepartmentID` int(11) DEFAULT '0',
  `epCoDeductionID` int(11) DEFAULT '0',
  `coAccountID` int(11) DEFAULT '0',
  PRIMARY KEY (`epDepDeductionID`),
  KEY `epCoDepartmentID` (`epCoDepartmentID`),
  KEY `epDepDeductionID` (`epDepDeductionID`),
  KEY `epCoDeductionID` (`epCoDeductionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epdepdeduction`
--

/*!40000 ALTER TABLE `epdepdeduction` DISABLE KEYS */;
/*!40000 ALTER TABLE `epdepdeduction` ENABLE KEYS */;


--
-- Definition of table `epdepearning`
--

DROP TABLE IF EXISTS `epdepearning`;
CREATE TABLE `epdepearning` (
  `epDepEarningID` int(11) NOT NULL AUTO_INCREMENT,
  `epCoDepartmentID` int(11) DEFAULT '0',
  `epCoEarningID` int(11) DEFAULT '0',
  `coAccountID` int(11) DEFAULT '0',
  PRIMARY KEY (`epDepEarningID`),
  KEY `epCoDepartmentID` (`epCoDepartmentID`),
  KEY `epDepEarningID` (`epDepEarningID`),
  KEY `epCoEarningID` (`epCoEarningID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epdepearning`
--

/*!40000 ALTER TABLE `epdepearning` DISABLE KEYS */;
/*!40000 ALTER TABLE `epdepearning` ENABLE KEYS */;


--
-- Definition of table `epdepliability`
--

DROP TABLE IF EXISTS `epdepliability`;
CREATE TABLE `epdepliability` (
  `epDepLiabilityID` int(11) NOT NULL AUTO_INCREMENT,
  `epCoDepartmentID` int(11) DEFAULT '0',
  `epCoLiabilityID` int(11) DEFAULT '0',
  `coExpenseAccountID` int(11) DEFAULT '0',
  `coLiabilityAccountID` int(11) DEFAULT '0',
  PRIMARY KEY (`epDepLiabilityID`),
  KEY `epCoDepartmentID` (`epCoDepartmentID`),
  KEY `epDepLiabilityID` (`epDepLiabilityID`),
  KEY `epCoLiabilityID` (`epCoLiabilityID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epdepliability`
--

/*!40000 ALTER TABLE `epdepliability` DISABLE KEYS */;
/*!40000 ALTER TABLE `epdepliability` ENABLE KEYS */;


--
-- Definition of table `epempdeduction`
--

DROP TABLE IF EXISTS `epempdeduction`;
CREATE TABLE `epempdeduction` (
  `epEmpDeductionID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `epCoDeductionID` int(11) DEFAULT '0',
  `Active` tinyint(1) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  `Maximum` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`epEmpDeductionID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `epEmpDeductionID` (`epEmpDeductionID`),
  KEY `epCoDeductionID` (`epCoDeductionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epempdeduction`
--

/*!40000 ALTER TABLE `epempdeduction` DISABLE KEYS */;
/*!40000 ALTER TABLE `epempdeduction` ENABLE KEYS */;


--
-- Definition of table `epempearning`
--

DROP TABLE IF EXISTS `epempearning`;
CREATE TABLE `epempearning` (
  `epEmpEarningID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `epCoEarningID` int(11) DEFAULT '0',
  `Active` tinyint(1) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`epEmpEarningID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `epEmpEarningID` (`epEmpEarningID`),
  KEY `epCoEarningID` (`epCoEarningID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epempearning`
--

/*!40000 ALTER TABLE `epempearning` DISABLE KEYS */;
/*!40000 ALTER TABLE `epempearning` ENABLE KEYS */;


--
-- Definition of table `epempliability`
--

DROP TABLE IF EXISTS `epempliability`;
CREATE TABLE `epempliability` (
  `epEmpLiabilityID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `epCoLiabilityID` int(11) DEFAULT '0',
  `Active` tinyint(1) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  `Maximum` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`epEmpLiabilityID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `epEmpLiabilityID` (`epEmpLiabilityID`),
  KEY `epCoLiabilityID` (`epCoLiabilityID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epempliability`
--

/*!40000 ALTER TABLE `epempliability` DISABLE KEYS */;
/*!40000 ALTER TABLE `epempliability` ENABLE KEYS */;


--
-- Definition of table `eptrandeduction`
--

DROP TABLE IF EXISTS `eptrandeduction`;
CREATE TABLE `eptrandeduction` (
  `epTranDeductionID` int(11) NOT NULL AUTO_INCREMENT,
  `epTransactionID` int(11) DEFAULT '0',
  `epCoDeductionID` int(11) DEFAULT '0',
  `DeductionAmount` decimal(19,4) DEFAULT '0.0000',
  `Override` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`epTranDeductionID`),
  KEY `epCoDeductionID` (`epCoDeductionID`),
  KEY `epTranDeductionID` (`epTranDeductionID`),
  KEY `epTransactionID` (`epTransactionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `eptrandeduction`
--

/*!40000 ALTER TABLE `eptrandeduction` DISABLE KEYS */;
/*!40000 ALTER TABLE `eptrandeduction` ENABLE KEYS */;


--
-- Definition of table `eptranearning`
--

DROP TABLE IF EXISTS `eptranearning`;
CREATE TABLE `eptranearning` (
  `epTranEarningID` int(11) NOT NULL AUTO_INCREMENT,
  `epTransactionID` int(11) DEFAULT '0',
  `epCoEarningID` int(11) DEFAULT '0',
  `Quantity` decimal(19,4) DEFAULT '0.0000',
  `Rate` decimal(19,4) DEFAULT '0.0000',
  `EarningAmount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`epTranEarningID`),
  KEY `epCoEarningID` (`epCoEarningID`),
  KEY `epTranEarningID` (`epTranEarningID`),
  KEY `epTransactionID` (`epTransactionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `eptranearning`
--

/*!40000 ALTER TABLE `eptranearning` DISABLE KEYS */;
/*!40000 ALTER TABLE `eptranearning` ENABLE KEYS */;


--
-- Definition of table `eptranliability`
--

DROP TABLE IF EXISTS `eptranliability`;
CREATE TABLE `eptranliability` (
  `epTranLiabilityID` int(11) NOT NULL AUTO_INCREMENT,
  `epTransactionID` int(11) DEFAULT '0',
  `epCoLiabilityID` int(11) DEFAULT '0',
  `LiabilityAmount` decimal(19,4) DEFAULT '0.0000',
  `Override` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`epTranLiabilityID`),
  KEY `epCoLiabilityID` (`epCoLiabilityID`),
  KEY `epTranLiabilityID` (`epTranLiabilityID`),
  KEY `epTransactionID` (`epTransactionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `eptranliability`
--

/*!40000 ALTER TABLE `eptranliability` DISABLE KEYS */;
/*!40000 ALTER TABLE `eptranliability` ENABLE KEYS */;


--
-- Definition of table `eptransaction`
--

DROP TABLE IF EXISTS `eptransaction`;
CREATE TABLE `eptransaction` (
  `epTransactionID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `moTransactionID` int(11) DEFAULT NULL,
  `coStateID` int(11) DEFAULT '0',
  `PostDate` datetime DEFAULT NULL,
  `PayPeriodEnding` datetime DEFAULT NULL,
  `PeriodFrequency` tinyint(4) DEFAULT '0',
  `GrossWages` decimal(19,4) DEFAULT '0.0000',
  `FITWages` decimal(19,4) DEFAULT '0.0000',
  `SocialSecurityWages` decimal(19,4) DEFAULT '0.0000',
  `MedicareWages` decimal(19,4) DEFAULT '0.0000',
  `OtherWages` decimal(19,4) DEFAULT '0.0000',
  `NetWages` decimal(19,4) DEFAULT '0.0000',
  `OpenBalance` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`epTransactionID`),
  KEY `coStateID` (`coStateID`),
  KEY `epTransactionID` (`epTransactionID`),
  KEY `moTransactionID` (`moTransactionID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `eptransaction`
--

/*!40000 ALTER TABLE `eptransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `eptransaction` ENABLE KEYS */;


--
-- Definition of table `epw2workfile`
--

DROP TABLE IF EXISTS `epw2workfile`;
CREATE TABLE `epw2workfile` (
  `epW2WorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `la` varchar(50) DEFAULT NULL,
  `lb` varchar(50) DEFAULT NULL,
  `lc1` varchar(50) DEFAULT NULL,
  `lc2` varchar(50) DEFAULT NULL,
  `lc3` varchar(50) DEFAULT NULL,
  `lc4` varchar(50) DEFAULT NULL,
  `ld` varchar(50) DEFAULT NULL,
  `le1` varchar(50) DEFAULT NULL,
  `le2` varchar(50) DEFAULT NULL,
  `le3` varchar(50) DEFAULT NULL,
  `le4` varchar(50) DEFAULT NULL,
  `le5` varchar(50) DEFAULT NULL,
  `le6` varchar(50) DEFAULT NULL,
  `l1` varchar(50) DEFAULT NULL,
  `l2` varchar(50) DEFAULT NULL,
  `l3` varchar(50) DEFAULT NULL,
  `l4` varchar(50) DEFAULT NULL,
  `l5` varchar(50) DEFAULT NULL,
  `l6` varchar(50) DEFAULT NULL,
  `l7` varchar(50) DEFAULT NULL,
  `l8` varchar(50) DEFAULT NULL,
  `l9` varchar(50) DEFAULT NULL,
  `l10` varchar(50) DEFAULT NULL,
  `l11` varchar(50) DEFAULT NULL,
  `l12a1` varchar(1) DEFAULT NULL,
  `l12a2` varchar(50) DEFAULT NULL,
  `l12b1` varchar(1) DEFAULT NULL,
  `l12b2` varchar(50) DEFAULT NULL,
  `l12c1` varchar(1) DEFAULT NULL,
  `l12c2` varchar(50) DEFAULT NULL,
  `l12d1` varchar(1) DEFAULT NULL,
  `l12d2` varchar(50) DEFAULT NULL,
  `l13a` varchar(1) DEFAULT NULL,
  `l13b` varchar(1) DEFAULT NULL,
  `l13c` varchar(1) DEFAULT NULL,
  `l14a1` varchar(50) DEFAULT NULL,
  `l14a2` varchar(50) DEFAULT NULL,
  `l14b1` varchar(50) DEFAULT NULL,
  `l14b2` varchar(50) DEFAULT NULL,
  `l14c1` varchar(50) DEFAULT NULL,
  `l14c2` varchar(50) DEFAULT NULL,
  `l15a` varchar(50) DEFAULT NULL,
  `l15b` varchar(50) DEFAULT NULL,
  `l16` varchar(50) DEFAULT NULL,
  `l17` varchar(50) DEFAULT NULL,
  `l18` varchar(50) DEFAULT NULL,
  `l19` varchar(50) DEFAULT NULL,
  `l20` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`epW2WorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epw2workfile`
--

/*!40000 ALTER TABLE `epw2workfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `epw2workfile` ENABLE KEYS */;


--
-- Definition of table `epw3workfile`
--

DROP TABLE IF EXISTS `epw3workfile`;
CREATE TABLE `epw3workfile` (
  `epW3WorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `la` varchar(50) DEFAULT NULL,
  `lc` varchar(50) DEFAULT NULL,
  `ld` varchar(50) DEFAULT NULL,
  `le` varchar(50) DEFAULT NULL,
  `lf1` varchar(50) DEFAULT NULL,
  `lf2` varchar(50) DEFAULT NULL,
  `lf3` varchar(50) DEFAULT NULL,
  `lf4` varchar(50) DEFAULT NULL,
  `l15a` varchar(50) DEFAULT NULL,
  `l15b` varchar(50) DEFAULT NULL,
  `Contact` varchar(50) DEFAULT NULL,
  `Email` varchar(50) DEFAULT NULL,
  `Phone1AC` varchar(3) DEFAULT NULL,
  `Phone1` varchar(12) DEFAULT NULL,
  `Phone2AC` varchar(3) DEFAULT NULL,
  `Phone2` varchar(50) DEFAULT NULL,
  `l1` varchar(50) DEFAULT NULL,
  `l2` varchar(50) DEFAULT NULL,
  `l3` varchar(50) DEFAULT NULL,
  `l4` varchar(50) DEFAULT NULL,
  `l5` varchar(50) DEFAULT NULL,
  `l6` varchar(50) DEFAULT NULL,
  `l7` varchar(50) DEFAULT NULL,
  `l8` varchar(50) DEFAULT NULL,
  `l9` varchar(50) DEFAULT NULL,
  `l10` varchar(50) DEFAULT NULL,
  `l11` varchar(50) DEFAULT NULL,
  `l12` varchar(50) DEFAULT NULL,
  `l16` varchar(50) DEFAULT NULL,
  `l17` varchar(50) DEFAULT NULL,
  `l18` varchar(50) DEFAULT NULL,
  `l19` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`epW3WorkFileID`),
  KEY `epW3WorkFileID` (`epW3WorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `epw3workfile`
--

/*!40000 ALTER TABLE `epw3workfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `epw3workfile` ENABLE KEYS */;


--
-- Definition of table `formcontrol`
--

DROP TABLE IF EXISTS `formcontrol`;
CREATE TABLE `formcontrol` (
  `FormControlID` int(11) NOT NULL AUTO_INCREMENT,
  `FormNo` int(11) DEFAULT '0',
  `FormDescription` varchar(50) DEFAULT NULL,
  `UseDefaultLabels` tinyint(1) DEFAULT '0',
  `UseCustomLabels` tinyint(1) DEFAULT '0',
  `UseDefaultLogo` tinyint(1) DEFAULT '0',
  `UseCustomLogo` tinyint(1) DEFAULT '0',
  `TopOffset` int(11) DEFAULT '0',
  `SlideHeader` int(11) DEFAULT '0',
  PRIMARY KEY (`FormControlID`),
  KEY `FormControlID` (`FormControlID`)
) ENGINE=MyISAM AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formcontrol`
--

/*!40000 ALTER TABLE `formcontrol` DISABLE KEYS */;
INSERT INTO `formcontrol` (`FormControlID`,`FormNo`,`FormDescription`,`UseDefaultLabels`,`UseCustomLabels`,`UseDefaultLogo`,`UseCustomLogo`,`TopOffset`,`SlideHeader`) VALUES 
 (8,7111,'Quote',1,1,0,0,540,0),
 (9,6221,'Purchase Order',0,0,0,0,0,0),
 (10,4310,'Customer Statement',0,0,0,0,0,0),
 (11,4221,'Customer Invoice',0,0,0,0,0,0),
 (12,4222,'Pick Ticket',0,0,0,0,0,0),
 (13,4211,'Sales Order',0,0,0,0,0,0),
 (14,9900,'Message',0,0,0,0,540,0);
/*!40000 ALTER TABLE `formcontrol` ENABLE KEYS */;


--
-- Definition of table `formlabel`
--

DROP TABLE IF EXISTS `formlabel`;
CREATE TABLE `formlabel` (
  `FormHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `TempSort` decimal(19,4) DEFAULT '0.0000',
  `FormNo` int(11) DEFAULT '0',
  `Section` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Caption` longtext,
  `Hide` tinyint(1) DEFAULT '0',
  `Show` tinyint(1) DEFAULT '0',
  `UseTopOffset` tinyint(1) DEFAULT '0',
  `locTop` int(11) DEFAULT '0',
  `locLeft` int(11) DEFAULT '0',
  `sizeHeight` int(11) DEFAULT '0',
  `sizeWidth` int(11) DEFAULT '0',
  `BorderStyle` int(11) DEFAULT '0',
  `Style` varchar(255) DEFAULT NULL,
  `Tag` int(11) DEFAULT NULL,
  PRIMARY KEY (`FormHeaderID`),
  KEY `FormHeaderID` (`FormHeaderID`)
) ENGINE=MyISAM AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formlabel`
--

/*!40000 ALTER TABLE `formlabel` DISABLE KEYS */;
INSERT INTO `formlabel` (`FormHeaderID`,`TempSort`,`FormNo`,`Section`,`Name`,`Caption`,`Hide`,`Show`,`UseTopOffset`,`locTop`,`locLeft`,`sizeHeight`,`sizeWidth`,`BorderStyle`,`Style`,`Tag`) VALUES 
 (13,NULL,0,'ReportHeader',NULL,'GREELEY & ASSOCIATES, INC.',0,0,1,0,2250,450,6300,NULL,'font-weight: bold; font-size: 20pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; ',NULL),
 (14,NULL,0,'ReportHeader',NULL,'6134 Industrial Heights Drive',0,0,1,450,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; ',1),
 (15,NULL,0,'ReportHeader',NULL,'Knoxville, Tennessee 37909',0,0,1,720,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; ',2),
 (16,NULL,0,'ReportHeader',NULL,'Phone (865)584-3267 - FAX (865)584-9641',0,0,1,990,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; ',3),
 (17,NULL,7111,'ReportHeader','lblTerms1','We are please to quote you on equi[ment for the above project as follows:',0,0,0,-1,-1,-1,-1,NULL,'text-align: left; font-family: \'Times New Roman\'; ddo-char-set: 0; vertical-align: middle; font-weight: bold; font-size: 10pt;',NULL),
 (19,NULL,7111,'ReportFooter','lblDisclaimer','This quotation subject to change without notice and voide after 60 days. After 60 days, 1% per month escalation. To the prices and terms quoted, add any manufacturers\' or sales tax payable on the transaction under and effective Federal or State statute.',0,0,0,-1,1260,360,9000,NULL,'text-align: center; font-family: \'Times New Roman\'; ddo-char-set: 1; font-size: 8pt; font-weight: bold;',NULL),
 (20,NULL,7111,'ReportFooter','lblBottom','MATERIAL: - F.O.B. Factory Full Freight Allowed     TERMS: Net 30 Days, Upon Receipt of Satisfactory Credit Information',0,0,0,540,-1,-1,-1,NULL,'text-align: center; font-family: \'Times New Roman\'; ddo-char-set: 1; font-weight: bold;',NULL);
/*!40000 ALTER TABLE `formlabel` ENABLE KEYS */;


--
-- Definition of table `formlabelcopy`
--

DROP TABLE IF EXISTS `formlabelcopy`;
CREATE TABLE `formlabelcopy` (
  `FormHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `FormNo` int(11) DEFAULT '0',
  `Section` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Caption` varchar(255) DEFAULT NULL,
  `Hide` tinyint(1) DEFAULT '0',
  `UseTopOffset` tinyint(1) DEFAULT '0',
  `locTop` int(11) DEFAULT '0',
  `locLeft` int(11) DEFAULT '0',
  `sizeHeight` int(11) DEFAULT '0',
  `sizeWidth` int(11) DEFAULT '0',
  `Style` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FormHeaderID`),
  KEY `FormHeaderID` (`FormHeaderID`)
) ENGINE=MyISAM AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formlabelcopy`
--

/*!40000 ALTER TABLE `formlabelcopy` DISABLE KEYS */;
INSERT INTO `formlabelcopy` (`FormHeaderID`,`FormNo`,`Section`,`Name`,`Caption`,`Hide`,`UseTopOffset`,`locTop`,`locLeft`,`sizeHeight`,`sizeWidth`,`Style`) VALUES 
 (1,0,NULL,NULL,'HVAC Associates, Inc.',0,1,0,3780,360,4770,'font-weight: bold; font-size: 18pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (2,0,NULL,NULL,'P.O. Box 1355',0,1,360,3780,270,4770,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (3,0,NULL,NULL,'Pompano Beach, FL 33061-1355',0,1,630,3780,270,4770,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (5,0,NULL,NULL,'Office',0,1,900,3780,270,900,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (6,0,NULL,NULL,'954-783-4892',0,1,900,4680,270,3600,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (7,0,NULL,NULL,'Fax',0,1,1170,3780,270,900,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (8,0,NULL,NULL,'954-783-4893',0,1,1170,4680,270,3600,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (13,4221,'ReportFooter','lblComment1','AS PER HVAC ASSOCIATES, INC., CREDIT APPLICATION TERMS AND CONDITIONS OF SALE.',0,0,-1,-1,-1,-1,NULL),
 (14,4221,'ReportFooter','lblComment2','THANK YOU FOR YOUR BUSINESS!',0,0,-1,-1,-1,-1,'font-size: 12pt; font-weight: (null); font-style: italic; text-align: center; font-family: Times New Roman; ddo-char-set: 1;'),
 (15,7111,NULL,NULL,'HVAC Associates, Inc.',0,0,0,1890,360,6660,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1; '),
 (16,7111,NULL,NULL,'CORPORATE HEADQUARTERS • SE FLORIDA',0,0,900,630,270,4950,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (17,7111,NULL,NULL,'P.O. Box 1355 • Pompano Beach, Florida 33061-1355',0,0,1170,630,270,4950,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (18,7111,NULL,NULL,'Broward (954)783-4892 • Date (305)949-5126',0,0,1440,630,270,4950,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (19,7111,NULL,NULL,'Palm Beach (561) 734-8602 • Fax (954)783-4893',0,0,1710,630,270,4950,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (20,7111,NULL,NULL,'Manufacturers Agent',0,0,360,1890,360,6660,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: bottom; font-family: Arial; ddo-char-set: 1; '),
 (21,7111,NULL,NULL,'CENTRAL FLORIDA',0,0,900,5760,270,4500,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (22,7111,NULL,NULL,'254 C.R. 427, • Suite #220 • Longwood, FL 32750',0,0,1170,5760,270,4500,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (23,7111,NULL,NULL,'Office (407) 339-2930 • Fax (407) 339-5970',0,0,1440,5760,270,4500,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (24,7111,NULL,NULL,'Florida Only Toll Free • (866) 339-2930',0,0,1710,5760,270,4500,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (26,7111,NULL,'lblQuoteRevision',NULL,0,0,0,-1,-1,-1,NULL),
 (27,7111,NULL,'lblTitle',NULL,0,0,2070,1890,540,6660,NULL),
 (28,7111,NULL,'lblTerms1','□ The quantities and pricing are based upon information provided to us, please verify with your take-off quantities.',0,0,-1,-1,390,-1,'text-align: left; font-family: Times New Roman; ddo-char-set: 1; vertical-align: middle; font-weight: bold;'),
 (29,7111,NULL,'lblTerms2',NULL,1,0,-1,-1,-1,-1,NULL);
/*!40000 ALTER TABLE `formlabelcopy` ENABLE KEYS */;


--
-- Definition of table `formlabelcopy2`
--

DROP TABLE IF EXISTS `formlabelcopy2`;
CREATE TABLE `formlabelcopy2` (
  `FormHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `TempSort` decimal(19,4) DEFAULT '0.0000',
  `FormNo` int(11) DEFAULT '0',
  `Section` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Caption` longtext,
  `Hide` tinyint(1) DEFAULT '0',
  `Show` tinyint(1) DEFAULT '0',
  `UseTopOffset` tinyint(1) DEFAULT '0',
  `locTop` int(11) DEFAULT '0',
  `locLeft` int(11) DEFAULT '0',
  `sizeHeight` int(11) DEFAULT '0',
  `sizeWidth` int(11) DEFAULT '0',
  `BorderStyle` int(11) DEFAULT '0',
  `Style` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`FormHeaderID`),
  KEY `FormHeaderID` (`FormHeaderID`)
) ENGINE=MyISAM AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formlabelcopy2`
--

/*!40000 ALTER TABLE `formlabelcopy2` DISABLE KEYS */;
INSERT INTO `formlabelcopy2` (`FormHeaderID`,`TempSort`,`FormNo`,`Section`,`Name`,`Caption`,`Hide`,`Show`,`UseTopOffset`,`locTop`,`locLeft`,`sizeHeight`,`sizeWidth`,`BorderStyle`,`Style`) VALUES 
 (1,'1.0000',0,NULL,NULL,'HVAC Associates, Inc.',0,0,1,0,1890,360,4770,NULL,'font-weight: bold; font-size: 18pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (2,'2.0000',0,NULL,NULL,'P.O. Box 1355',0,0,1,360,1890,270,4770,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (3,'3.0000',0,NULL,NULL,'Pompano Beach, FL 33061-1355',0,0,1,630,1890,270,4770,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1;'),
 (5,'5.0000',0,NULL,NULL,'Office',0,0,1,900,1890,270,900,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (6,'6.0000',0,NULL,NULL,'954-783-4892',0,0,1,900,2790,270,3600,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (7,'7.0000',0,NULL,NULL,'Fax',0,0,1,1170,1890,270,900,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (8,'8.0000',0,NULL,NULL,'954-783-4893',0,0,1,1170,2790,270,3600,NULL,'font-weight: bold; font-size: 12pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1;'),
 (15,'15.0000',7111,NULL,NULL,'HVAC Associates, Inc.',0,0,0,0,1890,360,6660,NULL,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1; '),
 (16,'16.0000',7111,NULL,NULL,'CORPORATE HEADQUARTERS • SE FLORIDA',0,0,0,900,630,270,4950,NULL,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (17,'17.0000',7111,NULL,NULL,'P.O. Box 1355 • Pompano Beach, Florida 33061-1355',0,0,0,1170,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (18,'18.0000',7111,NULL,NULL,'Broward (954)783-4892 • Dade (305)949-5126',0,0,0,1440,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (19,'19.0000',7111,NULL,NULL,'Palm Beach (561) 734-8602 • Fax (954)783-4893',0,0,0,1710,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (20,'20.0000',7111,NULL,NULL,'Manufacturers Agent',0,0,0,360,1890,360,6660,NULL,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: bottom; font-family: Arial; ddo-char-set: 1; '),
 (21,'21.0000',7111,NULL,NULL,'CENTRAL FLORIDA',0,0,0,900,6210,270,4500,NULL,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (22,'22.0000',7111,NULL,NULL,'254 C.R. 427, • Suite #220 • Longwood, FL 32750',0,0,0,1170,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (23,'23.0000',7111,NULL,NULL,'Office (407) 339-2930 • Fax (407) 339-5970',0,0,0,1440,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (24,'24.0000',7111,NULL,NULL,'Florida Only Toll Free • (866) 339-2930',0,0,0,1710,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (26,'26.0000',7111,NULL,'lblQuoteRevision',NULL,0,0,0,0,-1,-1,-1,NULL,NULL),
 (27,'27.0000',7111,NULL,'lblTitle',NULL,0,0,0,2070,1890,540,6660,NULL,NULL),
 (28,'28.0000',7111,NULL,'lblTerms1','The quantities and pricing are based upon information provided to us, please verify with your take-off quantities.',0,0,0,-1,-1,390,-1,NULL,'text-align: left; font-family: Times New Roman; ddo-char-set: 1; vertical-align: middle; font-weight: bold;'),
 (29,'29.0000',7111,NULL,'lblTerms2',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (30,'30.0000',7111,'ReportFooter','lblDisclaimer','ALL ORDERS SUBJECT TO ACCEPTANCE AT THE FACTORY.  ALL QUOTES ARE FOR ACCEPTANCE WITHIN 30 DAYS.  PRICES QUOTED NOT TO INCLUDE FEDERAL, STATE OR LOCAL TAXES WHICH WE ARE REQUIRED TO COLLECT FROM THE BUYER AND ARE F.O.B. FACTORY UNLESS OTHERWISE STATED.  TERMS OF SALE ARE AS  PER HVAC ASSOCIATES, INC., CREDIT APPLICATION OR C.O.D. CHANGING ECONOMIC CONDITIONS MAKE IT NECESSARY FOR US TO CHARGE A 1.5% SERVICE CHARGE (18% PER ANNUM) ON THE PAST DUE ACCOUNTS.  TITLE TO THE GOODS SOLD SHALL REMAIN THE SELLERS AS SECURITY FOR THE PAYMENT OF PURCHASE PRICE UNTIL SAME IS PAID IN FULL PAYMENT FOR ALL GOODS SHALL BE PAID IN U.S. $ CURRENCY.  IN CONSIDERATION OF THE EXTENSION OF CREDIT FROM HVAC ASSOCIATES, INC., AND/OR ANY OF ITS SUBSIDARIES, BY ACCEPTANCE OF THIS INSTRUMENT UNCONDITIONALLY GUARANTEES PAYMENT OF ALL SUCH CREDIT INDEBTEDNESS ACCORDING TO THE TERMS OF HVAC ASSOCIATES, INC. CREDIT APPLICATION.  THIS QUOTATION IS FOR QUANTITIES ONLY AS INDICATED ABOVE.  AS PER HVAC ASSOCIATES, INC. CREDIT APPLICATION TERMS AND CONDITIONS OF SALE.',0,0,0,-1,-1,-1,-1,NULL,'text-align: left; font-family: Times New Roman; ddo-char-set: 1; font-size: 5pt;'),
 (31,'31.0000',7111,'ReportFooter','lblBottom',NULL,1,0,0,0,0,0,0,NULL,NULL),
 (32,'32.0000',7111,'ghjoBidderID','lblTitle1','Description',0,0,0,-1,4050,-1,-1,NULL,NULL),
 (33,'33.0000',7111,'ghjoBidderID','lblTitle2','Qty',0,0,0,-1,0,-1,-1,NULL,NULL),
 (34,'34.0000',7111,'ghjoBidderID','lblTitle3','Section',0,0,0,-1,2430,-1,-1,NULL,NULL),
 (35,'35.0000',7111,'ghjoBidderID','lblTitle4','Mfr',0,0,0,-1,810,-1,-1,NULL,NULL),
 (36,'36.0000',7111,'ghjoBidderID','lblTitle5','Total$',0,0,0,-1,10350,-1,-1,NULL,NULL),
 (37,'37.0000',7111,'Detail','txtQT_ITEM',NULL,0,0,0,-1,4050,-1,6210,NULL,NULL),
 (38,'38.0000',7111,'Detail','txtQT_QTY',NULL,0,0,0,-1,0,-1,720,NULL,NULL),
 (39,'39.0000',7111,'Detail','txtQT_PAR',NULL,0,0,0,-1,2430,-1,1530,NULL,NULL),
 (40,'40.0000',7111,'Detail','txtQT_MAN',NULL,0,0,0,-1,810,-1,1530,NULL,NULL),
 (41,'41.0000',7111,'Detail','txtQT_PRICE',NULL,0,0,0,-1,10350,-1,-1,NULL,NULL),
 (42,'42.0000',7111,'gfjoBidderID','lblSummation','Total:',0,0,0,0,0,0,0,NULL,NULL),
 (43,'43.0000',7111,NULL,'lblLocation1',NULL,0,1,0,900,360,270,270,1,'text-align: center; vertical-align: middle; font-weight: bold;'),
 (44,'44.0000',7111,NULL,'lblLocation2',NULL,0,1,0,900,5940,270,270,1,'text-align: center; vertical-align: middle; font-weight: bold;'),
 (45,'45.0000',7111,'ReportHeader','lblPlanDate',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (46,'46.0000',7111,'ReportHeader','txtPlanDate',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (47,'47.0000',7111,'ReportHeader','lblBinNumber',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (48,'48.0000',7111,'ReportHeader','txtBinNumber',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (49,'49.0000',4221,'ReportFooter','lblComment1','AS PER HVAC ASSOCIATES, INC., QUOTATION AND CREDIT APPLICATION TERMS AND CONDITIONS OF SALE.',0,0,0,360,540,450,4860,NULL,'font-size: 8pt; vertical-align: middle; font-style: italic; '),
 (50,'50.0000',4221,'ReportFooter','lblComment2','THANK YOU FOR YOUR BUSINESS!',0,0,0,-1,-1,-1,-1,NULL,'font-size: 12pt; font-weight: (null); font-style: italic; text-align: center; font-family: Times New Roman; ddo-char-set: 1;'),
 (51,'51.0000',4221,'ReportFooter','lblTerms',NULL,0,0,0,90,360,270,4590,NULL,NULL),
 (52,'52.0000',4221,'ReportFooter',NULL,'Remit To:',0,0,0,90,5580,270,2610,NULL,'font-size: 8pt; vertical-align: middle; font-weight: bold; '),
 (53,'53.0000',4221,'ReportFooter',NULL,'P.O. Box 1355',0,0,0,360,5670,270,2520,NULL,'font-size: 8pt; vertical-align: top; '),
 (54,'54.0000',4221,'ReportFooter',NULL,'Pompano Beach, FL 33061-1355',0,0,0,540,5670,270,2520,NULL,'font-size: 8pt; vertical-align: top; '),
 (55,'55.0000',4221,'PageHeader','lblQuantityBilled',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (56,'56.0000',4221,'PageHeader','lblDescription',NULL,0,0,0,-1,2880,-1,7200,NULL,NULL),
 (57,'57.0000',4221,'PageHeader','lblItemCode',NULL,0,1,0,-1,900,-1,-1,NULL,NULL),
 (58,'58.0000',4221,'PageHeader','lblUnitPrice',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (59,'59.0000',4221,'PageHeader','lblExtendedList',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (60,'60.0000',4221,'PageHeader','lblPriceMultiplier',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (61,'61.0000',4221,'PageHeader','lblExtendedTotal',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (64,'64.0000',4221,'Detail','txtQuantityBilled',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (65,'65.0000',4221,'Detail','txtDescription',NULL,0,0,0,-1,2880,-1,7200,NULL,NULL),
 (66,'66.0000',4221,'Detail','txtItemCode',NULL,0,0,0,-1,900,-1,-1,NULL,NULL),
 (67,'67.0000',4221,'Detail','txtUnitPrice',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (68,'68.0000',4221,'Detail','txtExtendedList',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (69,'69.0000',4221,'Detail','txtPriceMultiplier',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (70,'70.0000',4221,'Detail','txtExtendedTotal',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (71,'71.0000',4221,'ReportHeader','Shape8',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (72,'72.0000',4221,'ReportHeader','Label67',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (73,'73.0000',4221,'ReportHeader','lblShipDate',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (75,'75.0000',4221,'ReportHeader','Shape9',NULL,0,0,0,-1,9540,-1,1980,NULL,NULL),
 (76,'76.0000',4221,'ReportHeader','Label69','Invoice Date',0,0,0,-1,9630,-1,1800,NULL,'font-weight: bold; font-size: 8pt; text-align: center; vertical-align: middle; text-decoration: underline;'),
 (77,'77.0000',4221,'ReportHeader','lblInvoiceDate',NULL,0,0,0,-1,9630,-1,1800,NULL,'font-size: 10pt; text-align: center; vertical-align: middle;'),
 (78,'78.0000',6221,NULL,NULL,'HVAC Associates, Inc.',0,0,0,0,1890,360,6660,NULL,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: middle; font-family: Arial Black; ddo-char-set: 1; '),
 (79,'79.0000',6221,NULL,NULL,'CORPORATE HEADQUARTERS • SE FLORIDA',0,0,0,900,630,270,4950,NULL,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (80,'80.0000',6221,NULL,NULL,'P.O. Box 1355 • Pompano Beach, Florida 33061-1355',0,0,0,1170,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (81,'81.0000',6221,NULL,NULL,'Broward (954)783-4892 • Dade (305)949-5126',0,0,0,1440,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (82,'82.0000',6221,NULL,NULL,'Palm Beach (561) 734-8602 • Fax (954)783-4893',0,0,0,1710,630,270,4950,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (83,'83.0000',6221,NULL,NULL,'Manufacturers Agent',0,0,0,360,1890,360,6660,NULL,'font-weight: bold; font-size: 18pt; text-align: center; vertical-align: bottom; font-family: Arial; ddo-char-set: 1; '),
 (84,'84.0000',6221,NULL,NULL,'CENTRAL FLORIDA',0,0,0,900,6210,270,4500,NULL,'font-weight: bold; font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: underline; '),
 (85,'85.0000',6221,NULL,NULL,'254 C.R. 427, • Suite #220 • Longwood, FL 32750',0,0,0,1170,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (86,'86.0000',6221,NULL,NULL,'Office (407) 339-2930 • Fax (407) 339-5970',0,0,0,1440,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (87,'87.0000',6221,NULL,NULL,'Florida Only Toll Free • (866) 339-2930',0,0,0,1710,6210,270,4500,NULL,'font-weight: (null); font-size: 10pt; text-align: left; vertical-align: middle; font-family: Arial; ddo-char-set: 1; text-decoration: (null); '),
 (88,'88.0000',6221,NULL,'lblLocation1',NULL,0,1,0,900,360,270,270,1,'text-align: center; vertical-align: middle; font-weight: bold;'),
 (89,'89.0000',6221,NULL,'lblLocation2',NULL,0,1,0,900,5940,270,270,1,'text-align: center; vertical-align: middle; font-weight: bold;'),
 (90,'90.0000',6221,NULL,'Shape3',NULL,0,0,0,0,-1,-1,-1,NULL,NULL),
 (91,'91.0000',6221,NULL,'Label7',NULL,0,0,0,90,-1,-1,-1,NULL,NULL),
 (92,'92.0000',6221,NULL,'lblPONo',NULL,0,0,0,540,-1,-1,-1,NULL,NULL),
 (93,'93.0000',6221,'GroupFooter1','Label28','Terms: Open     Tax #: 16-8012156809-3     PLEASE NOTIFY US IMMEDIATLEY IF YOU ARE UNABLE TO SHIP COMPLETE ORDER BY DATE SPECIFIED',0,0,0,1350,450,270,11070,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; '),
 (99,'99.0000',6221,'GroupFooter1',NULL,'TERMS AND CONDITIONS',0,0,0,1710,0,270,11070,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; font-weight: bold; '),
 (100,'100.0000',6221,'GroupFooter1',NULL,'1. BILLED PRICE: must not be greater then that shown on the face of this order without first notifying this office and obtaining written consent.  The Buyer reserves the right to return good shipped at a higher price at the Seller\'s expense.',0,0,0,1980,0,450,11520,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; font-size: 8pt;'),
 (101,'101.0000',6221,'GroupFooter1',NULL,'2. CANCELLATIONS: We reserve the right to cancel this order in its entirety or in part on account of defects in materials or equipment, workmanship or quality, or if materials or equipment are not shipped as specified herein, or are not in accordance with drawings and prints.  Approved samples or specifications or instructions issued in connection herewith or if performance on our part is prevented by caused beyond our control, such as fires, strikes, court orders, or acts or demands of any person or agency exercising governmental authority, or if you fail to comply with other terms and conditions of this order.',0,0,0,2430,0,810,11520,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; font-size: 8pt;'),
 (102,'102.0000',6221,'GroupFooter1',NULL,'3. CONFIDENTIAL: Seller shall not, without first obtaining Buyer’s written consent, disseminate the fact that Seller has furnished or has contracted to furnish Buyer the items covered hereby nor, except as necessary for performance of this order, shall Seller disclose any of the details connected with this order to third parties.',0,0,0,3240,0,450,11520,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; font-size: 8pt;'),
 (103,'103.0000',6221,'GroupFooter1',NULL,'4. Vendor acknowledges and agrees that One hundred dollars ($100.00) of the Purchase Order represents the specific consideration paid by Buyer for indemnification from the Vendor and holds the Buyer harmless of any loss, damage, cost and expense that may be suffered or sustained or be threatened with liability arising from the Purchase Order.',0,0,0,3690,0,450,11520,NULL,'font-family: \'Times New Roman\'; ddo-char-set: 1; text-align: left; font-size: 8pt;'),
 (104,'104.0000',6221,'PageHeader','lblQuantityOrdered',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (105,'105.0000',6221,'PageHeader','lblDescription',NULL,0,0,0,-1,2880,-1,7200,NULL,NULL),
 (106,'106.0000',6221,'PageHeader','lblItemCode',NULL,0,1,0,-1,900,-1,-1,NULL,NULL),
 (107,'107.0000',6221,'PageHeader','lblUnitCost',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (108,'108.0000',6221,'PageHeader','lblExtendedList',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (109,'109.0000',6221,'PageHeader','lblPriceMultiplier',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (110,'110.0000',6221,'PageHeader','lblExtendedTotal',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (111,'111.0000',6221,'Detail','txtQuantityOrdered',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (112,'112.0000',6221,'Detail','txtDescription',NULL,0,0,0,-1,2880,-1,7200,NULL,NULL),
 (113,'113.0000',6221,'Detail','txtItemCode',NULL,0,0,0,-1,900,-1,-1,NULL,NULL),
 (114,'114.0000',6221,'Detail','txtUnitCost',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (115,'115.0000',6221,'Detail','txtExtendedList',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (116,'116.0000',6221,'Detail','txtPriceMultiplier',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (117,'117.0000',6221,'Detail','txtExtendedTotal',NULL,0,0,0,-1,-1,-1,-1,NULL,NULL),
 (118,'36.5000',7111,'Detail','txtLineNo',NULL,1,0,0,0,0,0,0,0,NULL),
 (119,'41.0000',7111,'gfjoBidderID','txtLumpSum',NULL,0,0,0,-1,10350,-1,-1,NULL,NULL),
 (120,'47.0000',7111,'ReportHeader','lblPlanNos',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (121,'48.0000',7111,'ReportHeader','txtPlanNos',NULL,0,1,0,-1,-1,-1,-1,NULL,NULL),
 (122,'48.0000',7111,'ReportHeader','Shape1',NULL,1,0,0,-1,-1,-1,-1,NULL,NULL),
 (123,'48.0000',7111,'ReportHeader','lblQuoteYesNo1',NULL,0,1,0,-1,-1,180,180,NULL,'text-align: center; vertical-align: middle; font-weight: bold; font-size: 8pt; ');
/*!40000 ALTER TABLE `formlabelcopy2` ENABLE KEYS */;


--
-- Definition of table `formlogo`
--

DROP TABLE IF EXISTS `formlogo`;
CREATE TABLE `formlogo` (
  `FormLogoID` int(11) NOT NULL AUTO_INCREMENT,
  `FormNo` int(11) DEFAULT '0',
  `PictureLocation` varchar(255) DEFAULT NULL,
  `locTop` int(11) DEFAULT '0',
  `locLeft` int(11) DEFAULT '0',
  `sizeHeight` int(11) DEFAULT '0',
  `sizeWidth` int(11) DEFAULT '0',
  `PictureAlignment` int(11) DEFAULT '0',
  `SizeMode` int(11) DEFAULT '0',
  PRIMARY KEY (`FormLogoID`),
  KEY `FormLogoID` (`FormLogoID`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formlogo`
--

/*!40000 ALTER TABLE `formlogo` DISABLE KEYS */;
INSERT INTO `formlogo` (`FormLogoID`,`FormNo`,`PictureLocation`,`locTop`,`locLeft`,`sizeHeight`,`sizeWidth`,`PictureAlignment`,`SizeMode`) VALUES 
 (1,0,'C:\\DOCS\\TurboRep\\Clients\\Greeley\\GreeleyLogo.bmp',0,0,1440,1440,2,1);
/*!40000 ALTER TABLE `formlogo` ENABLE KEYS */;


--
-- Definition of table `formscratch`
--

DROP TABLE IF EXISTS `formscratch`;
CREATE TABLE `formscratch` (
  `FormHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `TempSort` decimal(19,4) DEFAULT NULL,
  `FormNo` int(11) DEFAULT NULL,
  `Section` varchar(50) DEFAULT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Caption` longtext,
  `Hide` tinyint(1) DEFAULT '0',
  `Show` tinyint(1) DEFAULT '0',
  `UseTopOffset` tinyint(1) DEFAULT '0',
  `locTop` int(11) DEFAULT NULL,
  `locLeft` int(11) DEFAULT NULL,
  `sizeHeight` int(11) DEFAULT NULL,
  `sizeWidth` int(11) DEFAULT NULL,
  `BorderStyle` int(11) DEFAULT NULL,
  `Style` varchar(255) DEFAULT NULL,
  KEY `FormHeaderID` (`FormHeaderID`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `formscratch`
--

/*!40000 ALTER TABLE `formscratch` DISABLE KEYS */;
INSERT INTO `formscratch` (`FormHeaderID`,`TempSort`,`FormNo`,`Section`,`Name`,`Caption`,`Hide`,`Show`,`UseTopOffset`,`locTop`,`locLeft`,`sizeHeight`,`sizeWidth`,`BorderStyle`,`Style`) VALUES 
 (1,NULL,NULL,'ReportHeader',NULL,'GREELEY & ASSOCIATES, INC.',0,0,0,0,2250,450,6300,NULL,'font-weight: bold; font-size: 20pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; '),
 (2,NULL,NULL,'ReportHeader',NULL,'6134 Industrial Heights Drive',0,0,0,450,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; '),
 (3,NULL,NULL,'ReportHeader',NULL,'Knoxville, Tennessee 37909',0,0,0,720,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; '),
 (4,NULL,NULL,'ReportHeader',NULL,'Phone (865)584-3267 - FAX (865)584-9641',0,0,0,990,2250,270,6300,NULL,'font-size: 10pt; text-align: center; vertical-align: middle; font-family: \'Arial\'; ddo-char-set: 1; font-weight: bold; ');
/*!40000 ALTER TABLE `formscratch` ENABLE KEYS */;


--
-- Definition of table `jobchanges`
--

DROP TABLE IF EXISTS `jobchanges`;
CREATE TABLE `jobchanges` (
  `joMasterID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `JobNumber` varchar(10) DEFAULT NULL,
  `rxCustomerID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `LocationName` varchar(40) DEFAULT NULL,
  `LocationAddress1` varchar(40) DEFAULT NULL,
  `LocationAddress2` varchar(40) DEFAULT NULL,
  `LocationCity` varchar(30) DEFAULT NULL,
  `LocationState` varchar(2) DEFAULT NULL,
  `LocationZip` varchar(10) DEFAULT NULL,
  `rxCategory1` int(11) DEFAULT NULL,
  `rxCategory2` int(11) DEFAULT NULL,
  `rxCategory3` int(11) DEFAULT NULL,
  `rxCategory4` int(11) DEFAULT NULL,
  `rxCategory5` int(11) DEFAULT NULL,
  `cuAssignmentID0` int(11) DEFAULT NULL,
  `cuAssignmentID1` int(11) DEFAULT NULL,
  `cuAssignmentID2` int(11) DEFAULT NULL,
  `cuAssignmentID3` int(11) DEFAULT NULL,
  `cuAssignmentID4` int(11) DEFAULT NULL,
  `PlanAndSpecJob` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalRequired` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalDone` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalDoneDate` datetime DEFAULT NULL,
  `PriorApprovalGranted` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalGrantedDate` datetime DEFAULT NULL,
  `Source1` tinyint(1) NOT NULL DEFAULT '0',
  `Source2` tinyint(1) NOT NULL DEFAULT '0',
  `Source3` tinyint(1) NOT NULL DEFAULT '0',
  `Source4` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalSent` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalApproved` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalResent` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalSentDate` datetime DEFAULT NULL,
  `SubmittalApprovedDate` datetime DEFAULT NULL,
  `SubmittalResentDate` datetime DEFAULT NULL,
  `AddendumReceived` varchar(1) DEFAULT NULL,
  `AddendumQuotedThru` varchar(1) DEFAULT NULL,
  `BidDate` datetime DEFAULT NULL,
  `BidTime` varchar(8) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `BookedDate` datetime DEFAULT NULL,
  `ClosedDate` datetime DEFAULT NULL,
  `EntireJobContractAmount` decimal(19,4) DEFAULT '0.0000',
  `ContractAmount` decimal(19,4) DEFAULT '0.0000',
  `EstimatedCost` decimal(19,4) DEFAULT '0.0000',
  `EstimatedProfit` decimal(19,4) DEFAULT '0.0000',
  `CloseOutActualProfit` decimal(19,4) DEFAULT NULL,
  `JobStatus` int(11) DEFAULT '0',
  `CreditStatus` tinyint(4) DEFAULT '0',
  `CreditType` tinyint(4) DEFAULT '0',
  `JobBonded` tinyint(1) NOT NULL DEFAULT '0',
  `RequestedNOC` tinyint(1) NOT NULL DEFAULT '0',
  `RequestedNOCDate` datetime DEFAULT NULL,
  `ReceivedNOC` tinyint(1) NOT NULL DEFAULT '0',
  `ReceivedNOCDate` datetime DEFAULT NULL,
  `SentNTC` tinyint(1) NOT NULL DEFAULT '0',
  `SentNTCDate` datetime DEFAULT NULL,
  `LienWaverSigned` tinyint(1) NOT NULL DEFAULT '0',
  `LienWaverSignedDate` datetime DEFAULT NULL,
  `LienWaverThrough` tinyint(1) NOT NULL DEFAULT '0',
  `LienWaverThroughDate` datetime DEFAULT NULL,
  `LienWaverThroughAmount` decimal(19,4) DEFAULT '0.0000',
  `LienWaverThroughFinal` tinyint(1) NOT NULL DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  `Notice24` tinyint(1) DEFAULT '0',
  `Notice48` tinyint(1) DEFAULT '0',
  `NoticeCall` varchar(35) DEFAULT NULL,
  `who0` int(11) DEFAULT '0',
  `who1` int(11) DEFAULT '0',
  `who2` int(11) DEFAULT '0',
  `who3` int(11) DEFAULT '0',
  `who4` int(11) DEFAULT '0',
  `who5` int(11) DEFAULT '0',
  `who6` int(11) DEFAULT '0',
  `who7` int(11) DEFAULT '0',
  `CreditStatusDate` datetime DEFAULT NULL,
  `CreditTypeDate` datetime DEFAULT NULL,
  `ClaimFiledDate` datetime DEFAULT NULL,
  `CreditContact0` int(11) DEFAULT '0',
  `CreditContact1` int(11) DEFAULT '0',
  `CreditContact2` int(11) DEFAULT '0',
  `ClaimFiled` tinyint(4) DEFAULT '0',
  `CreditNotes` longtext,
  PRIMARY KEY (`joMasterID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `cuAssignmentID0` (`cuAssignmentID0`),
  KEY `cuAssignmentID1` (`cuAssignmentID1`),
  KEY `cuAssignmentID2` (`cuAssignmentID2`),
  KEY `cuAssignmentID3` (`cuAssignmentID3`),
  KEY `cuAssignmentID4` (`cuAssignmentID4`),
  KEY `joMasterID` (`joMasterID`),
  KEY `JobNumber` (`JobNumber`),
  KEY `rxCategory4` (`rxCategory4`),
  KEY `rxCategory5` (`rxCategory5`),
  KEY `rxCustomerID` (`rxCustomerID`),
  KEY `rxCategory1` (`rxCategory1`),
  KEY `rxCategory2` (`rxCategory2`),
  KEY `rxCategory3` (`rxCategory3`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jobchanges`
--

/*!40000 ALTER TABLE `jobchanges` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobchanges` ENABLE KEYS */;


--
-- Definition of table `jobidder`
--

DROP TABLE IF EXISTS `jobidder`;
CREATE TABLE `jobidder` (
  `joBidderID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT NULL,
  `rxContactID` int(11) DEFAULT NULL,
  `cuMasterTypeID` int(11) DEFAULT NULL,
  `LowBid` tinyint(1) NOT NULL DEFAULT '0',
  `NoChance` tinyint(1) NOT NULL DEFAULT '0',
  `QuoteRev` varchar(3) DEFAULT NULL,
  `QuoteDate` datetime DEFAULT NULL,
  `QuoteStatus` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joBidderID`),
  KEY `cuMasterTypeID` (`cuMasterTypeID`),
  KEY `joBidderID` (`joBidderID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `rxContactID` (`rxContactID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jobidder`
--

/*!40000 ALTER TABLE `jobidder` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobidder` ENABLE KEYS */;


--
-- Definition of table `jobidstatus`
--

DROP TABLE IF EXISTS `jobidstatus`;
CREATE TABLE `jobidstatus` (
  `joBidStatusID` int(11) NOT NULL AUTO_INCREMENT,
  `Code` varchar(6) NOT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`joBidStatusID`),
  KEY `joBidStatusID` (`joBidStatusID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jobidstatus`
--

/*!40000 ALTER TABLE `jobidstatus` DISABLE KEYS */;
/*!40000 ALTER TABLE `jobidstatus` ENABLE KEYS */;


--
-- Definition of table `jochange`
--

DROP TABLE IF EXISTS `jochange`;
CREATE TABLE `jochange` (
  `joChangeID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `ChangeDate` datetime DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `ChangeByID` int(11) DEFAULT NULL,
  `ChangeByName` varchar(15) DEFAULT NULL,
  `ChangeReason` longtext,
  `ChangeAmount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`joChangeID`),
  KEY `ChangeByID` (`ChangeByID`),
  KEY `joChangeID` (`joChangeID`),
  KEY `joMasterID` (`joMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jochange`
--

/*!40000 ALTER TABLE `jochange` DISABLE KEYS */;
/*!40000 ALTER TABLE `jochange` ENABLE KEYS */;


--
-- Definition of table `jocustpo`
--

DROP TABLE IF EXISTS `jocustpo`;
CREATE TABLE `jocustpo` (
  `joCustPOID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `PODesc0` varchar(64) DEFAULT NULL,
  `PODesc1` varchar(64) DEFAULT NULL,
  `PODesc2` varchar(64) DEFAULT NULL,
  `PODesc3` varchar(64) DEFAULT NULL,
  `PODesc4` varchar(64) DEFAULT NULL,
  `PODesc5` varchar(64) DEFAULT NULL,
  `CustomerPONumber1` varchar(20) DEFAULT NULL,
  `CustomerPONumber2` varchar(20) DEFAULT NULL,
  `CustomerPONumber3` varchar(20) DEFAULT NULL,
  `CustomerPONumber4` varchar(20) DEFAULT NULL,
  `CustomerPONumber5` varchar(20) DEFAULT NULL,
  `POAmount0` decimal(19,4) DEFAULT '0.0000',
  `POAmount1` decimal(19,4) DEFAULT '0.0000',
  `POAmount2` decimal(19,4) DEFAULT '0.0000',
  `POAmount3` decimal(19,4) DEFAULT '0.0000',
  `POAmount4` decimal(19,4) DEFAULT '0.0000',
  `POAmount5` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`joCustPOID`),
  UNIQUE KEY `joCustPOID` (`joCustPOID`),
  KEY `joMasterID` (`joMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jocustpo`
--

/*!40000 ALTER TABLE `jocustpo` DISABLE KEYS */;
/*!40000 ALTER TABLE `jocustpo` ENABLE KEYS */;


--
-- Definition of table `jocustpoworkfile`
--

DROP TABLE IF EXISTS `jocustpoworkfile`;
CREATE TABLE `jocustpoworkfile` (
  `joCustPOWorkFileID` int(11) DEFAULT '0',
  `PONumber` varchar(20) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jocustpoworkfile`
--

/*!40000 ALTER TABLE `jocustpoworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `jocustpoworkfile` ENABLE KEYS */;


--
-- Definition of table `jododgeexcludetype`
--

DROP TABLE IF EXISTS `jododgeexcludetype`;
CREATE TABLE `jododgeexcludetype` (
  `ExcludeType` varchar(50) NOT NULL,
  PRIMARY KEY (`ExcludeType`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jododgeexcludetype`
--

/*!40000 ALTER TABLE `jododgeexcludetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `jododgeexcludetype` ENABLE KEYS */;


--
-- Definition of table `jododgework`
--

DROP TABLE IF EXISTS `jododgework`;
CREATE TABLE `jododgework` (
  `joDodgeWorkID` int(11) NOT NULL AUTO_INCREMENT,
  `Import` tinyint(1) DEFAULT '0',
  `fldPlans` varchar(50) DEFAULT NULL,
  `fldProjectName` varchar(80) DEFAULT NULL,
  `fldReportNo` varchar(10) DEFAULT NULL,
  `fldVerd` varchar(50) DEFAULT NULL,
  `fldBidDate` varchar(50) DEFAULT NULL,
  `fldStage` varchar(50) DEFAULT NULL,
  `fldProjectType` varchar(50) DEFAULT NULL,
  `fldValuation` varchar(50) DEFAULT NULL,
  `fldAddress` varchar(50) DEFAULT NULL,
  `fldCity` varchar(50) DEFAULT NULL,
  `fldCounty` varchar(50) DEFAULT NULL,
  `fldState` varchar(50) DEFAULT NULL,
  `fldZipCode` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`joDodgeWorkID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jododgework`
--

/*!40000 ALTER TABLE `jododgework` DISABLE KEYS */;
/*!40000 ALTER TABLE `jododgework` ENABLE KEYS */;


--
-- Definition of table `jojournal`
--

DROP TABLE IF EXISTS `jojournal`;
CREATE TABLE `jojournal` (
  `joJournalID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `JournalDate` datetime DEFAULT NULL,
  `JournalByID` int(11) DEFAULT NULL,
  `JournalByName` varchar(15) DEFAULT NULL,
  `JournalNote` longtext,
  PRIMARY KEY (`joJournalID`),
  KEY `JournalByID` (`JournalByID`),
  KEY `joJournalID` (`joJournalID`),
  KEY `joMasterID` (`joMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jojournal`
--

/*!40000 ALTER TABLE `jojournal` DISABLE KEYS */;
/*!40000 ALTER TABLE `jojournal` ENABLE KEYS */;


--
-- Definition of table `jomaster`
--

DROP TABLE IF EXISTS `jomaster`;
CREATE TABLE `jomaster` (
  `joMasterID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `JobNumber` varchar(10) DEFAULT NULL,
  `rxCustomerID` int(11) DEFAULT NULL,
  `coDivisionID` int(11) DEFAULT NULL,
  `joBidStatusID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `LocationName` varchar(40) DEFAULT NULL,
  `LocationAddress1` varchar(40) DEFAULT NULL,
  `LocationAddress2` varchar(40) DEFAULT NULL,
  `LocationCity` varchar(30) DEFAULT NULL,
  `LocationState` varchar(2) DEFAULT NULL,
  `LocationZip` varchar(10) DEFAULT NULL,
  `rxCategory1` int(11) DEFAULT NULL,
  `rxCategory2` int(11) DEFAULT NULL,
  `rxCategory3` int(11) DEFAULT NULL,
  `rxCategory4` int(11) DEFAULT NULL,
  `rxCategory5` int(11) DEFAULT NULL,
  `rxCategory6` int(11) DEFAULT NULL,
  `cuAssignmentID0` int(11) DEFAULT NULL,
  `cuAssignmentID1` int(11) DEFAULT NULL,
  `cuAssignmentID2` int(11) DEFAULT NULL,
  `cuAssignmentID3` int(11) DEFAULT NULL,
  `cuAssignmentID4` int(11) DEFAULT NULL,
  `cuAssignmentID5` int(11) DEFAULT NULL,
  `cuAssignmentID6` int(11) DEFAULT NULL,
  `PlanAndSpecJob` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalRequired` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalDone` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalDoneDate` datetime DEFAULT NULL,
  `PriorApprovalGranted` tinyint(1) NOT NULL DEFAULT '0',
  `PriorApprovalGrantedDate` datetime DEFAULT NULL,
  `Source1` tinyint(1) NOT NULL DEFAULT '0',
  `Source2` tinyint(1) NOT NULL DEFAULT '0',
  `Source3` tinyint(1) NOT NULL DEFAULT '0',
  `Source4` tinyint(1) NOT NULL DEFAULT '0',
  `SourceReport1` varchar(20) DEFAULT NULL,
  `SubmittalSent` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalApproved` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalResent` tinyint(1) NOT NULL DEFAULT '0',
  `SubmittalSentDate` datetime DEFAULT NULL,
  `SubmittalApprovedDate` datetime DEFAULT NULL,
  `SubmittalResentDate` datetime DEFAULT NULL,
  `AddendumReceived` varchar(1) DEFAULT NULL,
  `AddendumQuotedThru` varchar(1) DEFAULT NULL,
  `BinNumber` varchar(2) DEFAULT NULL,
  `PlanDate` datetime DEFAULT NULL,
  `BidDate` datetime DEFAULT NULL,
  `BidTime` varchar(8) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `BookedDate` datetime DEFAULT NULL,
  `ClosedDate` datetime DEFAULT NULL,
  `EntireJobContractAmount` decimal(19,4) DEFAULT '0.0000',
  `ContractAmount` decimal(19,4) DEFAULT '0.0000',
  `EstimatedCost` decimal(19,4) DEFAULT '0.0000',
  `EstimatedProfit` decimal(19,4) DEFAULT '0.0000',
  `CloseOutActualProfit` decimal(19,4) DEFAULT NULL,
  `JobStatus` int(11) DEFAULT '0',
  `CreditStatus` tinyint(4) DEFAULT '0',
  `CreditType` tinyint(4) DEFAULT '0',
  `LostComment` varchar(35) DEFAULT NULL,
  `JobBonded` tinyint(1) NOT NULL DEFAULT '0',
  `RequestedNOC` tinyint(1) NOT NULL DEFAULT '0',
  `RequestedNOCDate` datetime DEFAULT NULL,
  `ReceivedNOC` tinyint(1) NOT NULL DEFAULT '0',
  `ReceivedNOCDate` datetime DEFAULT NULL,
  `SentNTC` tinyint(1) NOT NULL DEFAULT '0',
  `SentNTCDate` datetime DEFAULT NULL,
  `LienWaverSigned` tinyint(1) NOT NULL DEFAULT '0',
  `LienWaverSignedDate` datetime DEFAULT NULL,
  `LienWaverThrough` tinyint(1) NOT NULL DEFAULT '0',
  `LienWaverThroughDate` datetime DEFAULT NULL,
  `LienWaverThroughAmount` decimal(19,4) DEFAULT '0.0000',
  `LienWaverThroughFinal` tinyint(1) NOT NULL DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  `Notice24` tinyint(1) DEFAULT '0',
  `Notice48` tinyint(1) DEFAULT '0',
  `NoticeCall` varchar(35) DEFAULT NULL,
  `who0` int(11) DEFAULT '0',
  `who1` int(11) DEFAULT '0',
  `who2` int(11) DEFAULT '0',
  `who3` int(11) DEFAULT '0',
  `who4` int(11) DEFAULT '0',
  `who5` int(11) DEFAULT '0',
  `who6` int(11) DEFAULT '0',
  `who7` int(11) DEFAULT '0',
  `CreditStatusDate` datetime DEFAULT NULL,
  `CreditTypeDate` datetime DEFAULT NULL,
  `ClaimFiledDate` datetime DEFAULT NULL,
  `CreditContact0` int(11) DEFAULT '0',
  `CreditContact1` int(11) DEFAULT '0',
  `CreditContact2` int(11) DEFAULT '0',
  `ClaimFiled` tinyint(4) DEFAULT '0',
  `CreditNotes` longtext,
  `ReleaseNotes` longtext,
  `CreditReferenceNumber` varchar(12) DEFAULT NULL,
  `QuoteYesNo1` tinyint(1) DEFAULT '0',
  `PlanNumbers` longtext,
  `PriorCost` decimal(19,4) DEFAULT NULL,
  `PriorRevenue` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`joMasterID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `cuAssignmentID0` (`cuAssignmentID0`),
  KEY `cuAssignmentID1` (`cuAssignmentID1`),
  KEY `cuAssignmentID2` (`cuAssignmentID2`),
  KEY `cuAssignmentID3` (`cuAssignmentID3`),
  KEY `cuAssignmentID4` (`cuAssignmentID4`),
  KEY `cuAssignmentID5` (`cuAssignmentID5`),
  KEY `cuAssignmentID6` (`cuAssignmentID6`),
  KEY `joMasterID` (`joMasterID`),
  KEY `JobNumber` (`JobNumber`),
  KEY `rxCategory4` (`rxCategory4`),
  KEY `rxCategory5` (`rxCategory5`),
  KEY `rxCategory6` (`rxCategory6`),
  KEY `rxCustomerID` (`rxCustomerID`),
  KEY `rxCategory1` (`rxCategory1`),
  KEY `rxCategory2` (`rxCategory2`),
  KEY `rxCategory3` (`rxCategory3`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jomaster`
--

/*!40000 ALTER TABLE `jomaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `jomaster` ENABLE KEYS */;


--
-- Definition of table `joquotedetail`
--

DROP TABLE IF EXISTS `joquotedetail`;
CREATE TABLE `joquotedetail` (
  `joQuoteDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `joQuoteHeaderID` int(11) DEFAULT '0',
  `Product` longtext,
  `ProductNote` longtext,
  `ItemQuantity` varchar(8) DEFAULT NULL,
  `Paragraph` varchar(16) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT '0',
  `Price` decimal(19,4) DEFAULT '0.0000',
  `InlineNote` longtext,
  `Cost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`joQuoteDetailID`),
  UNIQUE KEY `joQuoteDetailID` (`joQuoteDetailID`),
  KEY `joQuoteHeaderID` (`joQuoteHeaderID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquotedetail`
--

/*!40000 ALTER TABLE `joquotedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `joquotedetail` ENABLE KEYS */;


--
-- Definition of table `joquotedetailworkfile`
--

DROP TABLE IF EXISTS `joquotedetailworkfile`;
CREATE TABLE `joquotedetailworkfile` (
  `joQuoteDetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `Product` longtext,
  `ProductNote` longtext,
  `ItemQuantity` varchar(8) DEFAULT NULL,
  `Paragraph` varchar(16) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT '0',
  `Price` decimal(19,4) DEFAULT '0.0000',
  `InlineNote` longtext,
  `Cost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`joQuoteDetailWorkFileID`),
  UNIQUE KEY `joQuoteDetailWorkFileID` (`joQuoteDetailWorkFileID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquotedetailworkfile`
--

/*!40000 ALTER TABLE `joquotedetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `joquotedetailworkfile` ENABLE KEYS */;


--
-- Definition of table `joquoteheader`
--

DROP TABLE IF EXISTS `joquoteheader`;
CREATE TABLE `joquoteheader` (
  `joQuoteHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `cuMasterTypeID` int(11) DEFAULT NULL,
  `QuoteRev` varchar(3) DEFAULT NULL,
  `QuoteAmount` decimal(19,4) DEFAULT '0.0000',
  `DiscountedPrice` decimal(19,4) DEFAULT '0.0000',
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedByName` varchar(15) DEFAULT NULL,
  `Remarks` longtext,
  `QuoteYesNo1` tinyint(1) DEFAULT '0',
  `Locked` tinyint(1) DEFAULT '0',
  `DateCreated` datetime DEFAULT NULL,
  `DisplayQuantity` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayParagraph` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayManufacturer` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayCost` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayPrice` tinyint(1) NOT NULL DEFAULT '0',
  `PrintQuantity` tinyint(1) NOT NULL DEFAULT '0',
  `PrintParagraph` tinyint(1) NOT NULL DEFAULT '0',
  `PrintManufacturer` tinyint(1) NOT NULL DEFAULT '0',
  `PrintCost` tinyint(1) NOT NULL DEFAULT '0',
  `PrintPrice` tinyint(1) NOT NULL DEFAULT '0',
  `NotesFullWidth` tinyint(1) NOT NULL DEFAULT '0',
  `LineNumbers` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joQuoteHeaderID`),
  KEY `CreatedByID` (`CreatedByID`),
  KEY `cuMasterTypeID` (`cuMasterTypeID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joQuoteHeaderID` (`joQuoteHeaderID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquoteheader`
--

/*!40000 ALTER TABLE `joquoteheader` DISABLE KEYS */;
/*!40000 ALTER TABLE `joquoteheader` ENABLE KEYS */;


--
-- Definition of table `joquotehistory`
--

DROP TABLE IF EXISTS `joquotehistory`;
CREATE TABLE `joquotehistory` (
  `joQuoteHistoryID` int(11) NOT NULL AUTO_INCREMENT,
  `joQuoteHeaderID` int(11) DEFAULT NULL,
  `joMasterID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT NULL,
  `rxContactID` int(11) DEFAULT NULL,
  `QuoteRev` varchar(3) DEFAULT NULL,
  `QuoteDate` datetime DEFAULT NULL,
  `QuoteStatus` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joQuoteHistoryID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joQuoteHeaderID` (`joQuoteHeaderID`),
  KEY `joQuoteHistoryID` (`joQuoteHistoryID`),
  KEY `rxContactID` (`rxContactID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquotehistory`
--

/*!40000 ALTER TABLE `joquotehistory` DISABLE KEYS */;
INSERT INTO `joquotehistory` (`joQuoteHistoryID`,`joQuoteHeaderID`,`joMasterID`,`rxMasterID`,`rxContactID`,`QuoteRev`,`QuoteDate`,`QuoteStatus`) VALUES 
 (1,3,1,2717,NULL,'','2005-04-27 11:57:23',5),
 (2,3,1,2892,NULL,'','2005-04-27 11:57:47',1),
 (3,3,1,2604,1,'','2005-04-27 11:58:33',5),
 (4,5,1,2717,NULL,'3','2005-04-27 11:59:17',3),
 (5,5,1,2604,1,'3','2005-04-27 11:59:34',1),
 (6,5,1,2604,1,'3','2005-04-27 12:00:05',1),
 (7,5,1,2604,1,'3','2005-04-27 13:34:16',3),
 (8,5,1,2604,1,'3','2005-04-27 13:34:42',3),
 (9,5,1,2604,1,'3','2005-04-27 13:35:03',3),
 (10,5,1,2604,1,'3','2005-04-27 13:36:07',3),
 (11,5,1,2604,1,'3','2005-04-27 13:40:34',2);
/*!40000 ALTER TABLE `joquotehistory` ENABLE KEYS */;


--
-- Definition of table `joquotetemplatedetail`
--

DROP TABLE IF EXISTS `joquotetemplatedetail`;
CREATE TABLE `joquotetemplatedetail` (
  `joQuoteTemplateDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `joQuoteTemplateHeaderID` int(11) DEFAULT '0',
  `Product` longtext,
  `ProductNote` longtext,
  `ItemQuantity` varchar(8) DEFAULT NULL,
  `Paragraph` varchar(16) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT '0',
  `Price` decimal(19,4) DEFAULT '0.0000',
  `InlineNote` longtext,
  `Cost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`joQuoteTemplateDetailID`),
  UNIQUE KEY `joQuoteTemplateDetailID` (`joQuoteTemplateDetailID`),
  KEY `joQuoteTemplateHeaderID` (`joQuoteTemplateHeaderID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquotetemplatedetail`
--

/*!40000 ALTER TABLE `joquotetemplatedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `joquotetemplatedetail` ENABLE KEYS */;


--
-- Definition of table `joquotetemplateheader`
--

DROP TABLE IF EXISTS `joquotetemplateheader`;
CREATE TABLE `joquotetemplateheader` (
  `joQuoteTemplateHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `OwnerID` int(11) DEFAULT NULL,
  `TemplateName` varchar(50) DEFAULT NULL,
  `cuMasterTypeID` int(11) DEFAULT NULL,
  `QuoteRev` varchar(3) DEFAULT NULL,
  `Remarks` longtext,
  `QuoteAmount` decimal(19,4) DEFAULT '0.0000',
  `QuoteYesNo1` tinyint(1) DEFAULT '0',
  `DisplayQuantity` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayParagraph` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayManufacturer` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayCost` tinyint(1) NOT NULL DEFAULT '0',
  `DisplayPrice` tinyint(1) NOT NULL DEFAULT '0',
  `PrintQuantity` tinyint(1) NOT NULL DEFAULT '0',
  `PrintParagraph` tinyint(1) NOT NULL DEFAULT '0',
  `PrintManufacturer` tinyint(1) NOT NULL DEFAULT '0',
  `PrintCost` tinyint(1) NOT NULL DEFAULT '0',
  `PrintPrice` tinyint(1) NOT NULL DEFAULT '0',
  `NotesFullWidth` tinyint(1) NOT NULL DEFAULT '0',
  `LineNumbers` tinyint(1) NOT NULL DEFAULT '0',
  `GlobalTemplate` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joQuoteTemplateHeaderID`),
  KEY `OwnerID` (`OwnerID`),
  KEY `cuMasterTypeID` (`cuMasterTypeID`),
  KEY `joQuoteTemplateHeaderID` (`joQuoteTemplateHeaderID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joquotetemplateheader`
--

/*!40000 ALTER TABLE `joquotetemplateheader` DISABLE KEYS */;
/*!40000 ALTER TABLE `joquotetemplateheader` ENABLE KEYS */;


--
-- Definition of table `jorelease`
--

DROP TABLE IF EXISTS `jorelease`;
CREATE TABLE `jorelease` (
  `joReleaseID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT '0',
  `ReleaseType` tinyint(4) DEFAULT '0',
  `ReleaseDate` datetime DEFAULT NULL,
  `EstimatedBilling` decimal(19,4) DEFAULT NULL,
  `ReleaseNote` varchar(50) DEFAULT NULL,
  `CommissionReceived` tinyint(1) DEFAULT '0',
  `CommissionDate` datetime DEFAULT NULL,
  `CommissionAmount` decimal(19,4) DEFAULT NULL,
  `Cancelled` tinyint(1) DEFAULT '0',
  `POID` int(11) DEFAULT NULL,
  `BillNote` longtext,
  PRIMARY KEY (`joReleaseID`),
  KEY `joReleaseID` (`joReleaseID`),
  KEY `joMasterID` (`joMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jorelease`
--

/*!40000 ALTER TABLE `jorelease` DISABLE KEYS */;
/*!40000 ALTER TABLE `jorelease` ENABLE KEYS */;


--
-- Definition of table `joreleasedetail`
--

DROP TABLE IF EXISTS `joreleasedetail`;
CREATE TABLE `joreleasedetail` (
  `joReleaseDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `joReleaseID` int(11) DEFAULT NULL,
  `joMasterID` int(11) DEFAULT '0',
  `ShipDate` datetime DEFAULT NULL,
  `VendorInvoiceDate` datetime DEFAULT NULL,
  `VendorInvoiceAmount` decimal(19,4) DEFAULT NULL,
  `CustomerInvoiceDate` datetime DEFAULT NULL,
  `CustomerInvoiceAmount` decimal(19,4) DEFAULT NULL,
  `CustomerInvoiceCost` decimal(19,4) DEFAULT NULL,
  `BalanceDue` decimal(19,4) DEFAULT NULL,
  `PaymentDate` datetime DEFAULT NULL,
  PRIMARY KEY (`joReleaseDetailID`),
  KEY `joReleaseDetailID` (`joReleaseDetailID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joReleaseID` (`joReleaseID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joreleasedetail`
--

/*!40000 ALTER TABLE `joreleasedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `joreleasedetail` ENABLE KEYS */;


--
-- Definition of table `joschedaccessory`
--

DROP TABLE IF EXISTS `joschedaccessory`;
CREATE TABLE `joschedaccessory` (
  `joSchedAccessoryID` int(11) NOT NULL AUTO_INCREMENT,
  `joSchedTempColumnID` int(11) DEFAULT '0',
  `Code` varchar(6) DEFAULT NULL,
  `Description` longtext,
  PRIMARY KEY (`joSchedAccessoryID`),
  KEY `joSchedAccessoryID` (`joSchedAccessoryID`),
  KEY `joSchedTempColumnID` (`joSchedTempColumnID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joschedaccessory`
--

/*!40000 ALTER TABLE `joschedaccessory` DISABLE KEYS */;
/*!40000 ALTER TABLE `joschedaccessory` ENABLE KEYS */;


--
-- Definition of table `joschedtempcolumn`
--

DROP TABLE IF EXISTS `joschedtempcolumn`;
CREATE TABLE `joschedtempcolumn` (
  `joSchedTempColumnID` int(11) NOT NULL AUTO_INCREMENT,
  `joSchedTempHeaderID` int(11) DEFAULT '0',
  `ColumnNumber` int(11) DEFAULT '0',
  `joSchedTempColumnTypeID` int(11) DEFAULT '0',
  `DisplayText` varchar(50) DEFAULT NULL,
  `DisplayWidth` int(11) DEFAULT '0',
  `PrintText` varchar(50) DEFAULT NULL,
  `PrintWidth` int(11) DEFAULT '0',
  `Inactive` tinyint(1) DEFAULT '0',
  `CopyDefaults` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`joSchedTempColumnID`),
  KEY `joSchedTempColumnID` (`joSchedTempColumnID`),
  KEY `joSchedTempHeaderID` (`joSchedTempHeaderID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joschedtempcolumn`
--

/*!40000 ALTER TABLE `joschedtempcolumn` DISABLE KEYS */;
/*!40000 ALTER TABLE `joschedtempcolumn` ENABLE KEYS */;


--
-- Definition of table `joschedtempcolumntype`
--

DROP TABLE IF EXISTS `joschedtempcolumntype`;
CREATE TABLE `joschedtempcolumntype` (
  `joSchedTempColumnTypeID` int(11) NOT NULL DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`joSchedTempColumnTypeID`),
  KEY `joSchedTempColumnTypeID` (`joSchedTempColumnTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joschedtempcolumntype`
--

/*!40000 ALTER TABLE `joschedtempcolumntype` DISABLE KEYS */;
/*!40000 ALTER TABLE `joschedtempcolumntype` ENABLE KEYS */;


--
-- Definition of table `joschedtempheader`
--

DROP TABLE IF EXISTS `joschedtempheader`;
CREATE TABLE `joschedtempheader` (
  `joSchedTempHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(12) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT '0',
  `Product` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`joSchedTempHeaderID`),
  KEY `joSchedTempHeaderID` (`joSchedTempHeaderID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joschedtempheader`
--

/*!40000 ALTER TABLE `joschedtempheader` DISABLE KEYS */;
/*!40000 ALTER TABLE `joschedtempheader` ENABLE KEYS */;


--
-- Definition of table `joscheduledetail`
--

DROP TABLE IF EXISTS `joscheduledetail`;
CREATE TABLE `joscheduledetail` (
  `joScheduleDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `joSubmittalDetailID` int(11) DEFAULT '0',
  `joLineNumber` int(11) DEFAULT NULL,
  `joScheduleModelID` int(11) DEFAULT NULL,
  `Quantity` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Col01` varchar(255) DEFAULT NULL,
  `Col02` varchar(255) DEFAULT NULL,
  `Col03` varchar(255) DEFAULT NULL,
  `Col04` varchar(255) DEFAULT NULL,
  `Col05` varchar(255) DEFAULT NULL,
  `Col06` varchar(255) DEFAULT NULL,
  `Col07` varchar(255) DEFAULT NULL,
  `Col08` varchar(255) DEFAULT NULL,
  `Col09` varchar(255) DEFAULT NULL,
  `Col10` varchar(255) DEFAULT NULL,
  `Col11` varchar(255) DEFAULT NULL,
  `Col12` varchar(255) DEFAULT NULL,
  `Col13` varchar(255) DEFAULT NULL,
  `Col14` varchar(255) DEFAULT NULL,
  `Col15` varchar(255) DEFAULT NULL,
  `Col16` varchar(255) DEFAULT NULL,
  `Col17` varchar(255) DEFAULT NULL,
  `Col18` varchar(255) DEFAULT NULL,
  `Col19` varchar(255) DEFAULT NULL,
  `Col20` varchar(255) DEFAULT NULL,
  `FN_Factory` longtext,
  `FN_Engineer` longtext,
  `FN_Advise` longtext,
  `QtyOrdered` decimal(19,4) DEFAULT NULL,
  `Status` int(11) DEFAULT NULL,
  `TAG` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`joScheduleDetailID`),
  KEY `joSubmittalDetailID` (`joSubmittalDetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joscheduledetail`
--

/*!40000 ALTER TABLE `joscheduledetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `joscheduledetail` ENABLE KEYS */;


--
-- Definition of table `joschedulemodel`
--

DROP TABLE IF EXISTS `joschedulemodel`;
CREATE TABLE `joschedulemodel` (
  `joScheduleModelID` int(11) NOT NULL AUTO_INCREMENT,
  `joSchedTempHeaderID` int(11) DEFAULT NULL,
  `ModelNo` varchar(50) DEFAULT NULL,
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Col01` varchar(255) DEFAULT NULL,
  `Col02` varchar(255) DEFAULT NULL,
  `Col03` varchar(255) DEFAULT NULL,
  `Col04` varchar(255) DEFAULT NULL,
  `Col05` varchar(255) DEFAULT NULL,
  `Col06` varchar(255) DEFAULT NULL,
  `Col07` varchar(255) DEFAULT NULL,
  `Col08` varchar(255) DEFAULT NULL,
  `Col09` varchar(255) DEFAULT NULL,
  `Col10` varchar(255) DEFAULT NULL,
  `Col11` varchar(255) DEFAULT NULL,
  `Col12` varchar(255) DEFAULT NULL,
  `Col13` varchar(255) DEFAULT NULL,
  `Col14` varchar(255) DEFAULT NULL,
  `Col15` varchar(255) DEFAULT NULL,
  `Col16` varchar(255) DEFAULT NULL,
  `Col17` varchar(255) DEFAULT NULL,
  `Col18` varchar(255) DEFAULT NULL,
  `Col19` varchar(255) DEFAULT NULL,
  `Col20` varchar(255) DEFAULT NULL,
  `prMasterID` int(11) DEFAULT NULL,
  `WebAddress` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`joScheduleModelID`),
  KEY `joScheduleModelID` (`joScheduleModelID`),
  KEY `ModelNo` (`ModelNo`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `joschedulemodel`
--

/*!40000 ALTER TABLE `joschedulemodel` DISABLE KEYS */;
/*!40000 ALTER TABLE `joschedulemodel` ENABLE KEYS */;


--
-- Definition of table `jostatus`
--

DROP TABLE IF EXISTS `jostatus`;
CREATE TABLE `jostatus` (
  `joStatusID` int(11) NOT NULL,
  `JobStatus` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`joStatusID`),
  KEY `joStatusID` (`joStatusID`),
  KEY `JobStatus` (`JobStatus`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `jostatus`
--

/*!40000 ALTER TABLE `jostatus` DISABLE KEYS */;
INSERT INTO `jostatus` (`joStatusID`,`JobStatus`) VALUES 
 (-4,'Planning'),
 (-3,'Over Budget'),
 (-2,'Rejected'),
 (-1,'Abandoned'),
 (0,'Bid'),
 (1,'Quote'),
 (2,'Lost'),
 (3,'Booked'),
 (4,'Closed');
/*!40000 ALTER TABLE `jostatus` ENABLE KEYS */;


--
-- Definition of table `josubmittal`
--

DROP TABLE IF EXISTS `josubmittal`;
CREATE TABLE `josubmittal` (
  `joSubmittalID` int(11) NOT NULL AUTO_INCREMENT,
  `joMasterID` int(11) DEFAULT NULL,
  `Product` varchar(30) DEFAULT NULL,
  `ProductNote` longtext,
  `ItemQuantity` varchar(8) DEFAULT NULL,
  `Paragraph` varchar(12) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT NULL,
  `Status` tinyint(4) DEFAULT '0',
  `RawCost` decimal(19,4) DEFAULT '0.0000',
  `FFA` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joSubmittalID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joSubmittalID` (`joSubmittalID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `josubmittal`
--

/*!40000 ALTER TABLE `josubmittal` DISABLE KEYS */;
/*!40000 ALTER TABLE `josubmittal` ENABLE KEYS */;


--
-- Definition of table `josubmittaldetail`
--

DROP TABLE IF EXISTS `josubmittaldetail`;
CREATE TABLE `josubmittaldetail` (
  `joSubmittalDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `joSubmittalHeaderID` int(11) DEFAULT NULL,
  `joLineNumber` int(11) DEFAULT NULL,
  `joSchedTempHeaderID` int(11) DEFAULT NULL,
  `Product` varchar(30) DEFAULT NULL,
  `Quantity` varchar(12) DEFAULT NULL,
  `Paragraph` varchar(12) DEFAULT NULL,
  `rxManufacturerID` int(11) DEFAULT NULL,
  `FN_Engineer` longtext,
  `ManufacturerOverride` varchar(30) DEFAULT NULL,
  `Status` int(11) DEFAULT NULL,
  `Cost` decimal(19,4) DEFAULT NULL,
  `EstimatedCost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`joSubmittalDetailID`),
  KEY `joSubmittalHeaderID` (`joSubmittalHeaderID`),
  KEY `joSubmittalDetailID` (`joSubmittalDetailID`),
  KEY `rxManufacturerID` (`rxManufacturerID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `josubmittaldetail`
--

/*!40000 ALTER TABLE `josubmittaldetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `josubmittaldetail` ENABLE KEYS */;


--
-- Definition of table `josubmittalheader`
--

DROP TABLE IF EXISTS `josubmittalheader`;
CREATE TABLE `josubmittalheader` (
  `joSubmittalHeaderID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `joMasterID` int(11) DEFAULT NULL,
  `Revision` tinyint(4) DEFAULT NULL,
  `SubmittalByID` int(11) DEFAULT '0',
  `SubmittalDate` datetime DEFAULT NULL,
  `Copies` varchar(6) DEFAULT NULL,
  `RevisionNote` longtext,
  `RemarkNote` longtext,
  `PlanDate` datetime DEFAULT NULL,
  `Addendum` varchar(1) DEFAULT NULL,
  `SignedByID` int(11) DEFAULT NULL,
  `ForRecordOnly` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`joSubmittalHeaderID`),
  KEY `joMasterID` (`joMasterID`),
  KEY `joSubmittalHeaderID` (`joSubmittalHeaderID`),
  KEY `SubmittalByID` (`SubmittalByID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `josubmittalheader`
--

/*!40000 ALTER TABLE `josubmittalheader` DISABLE KEYS */;
/*!40000 ALTER TABLE `josubmittalheader` ENABLE KEYS */;


--
-- Definition of table `moaccount`
--

DROP TABLE IF EXISTS `moaccount`;
CREATE TABLE `moaccount` (
  `moAccountID` int(11) NOT NULL AUTO_INCREMENT,
  `InActive` tinyint(1) NOT NULL DEFAULT '0',
  `AccountType` tinyint(4) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `coAccountIDAsset` int(11) DEFAULT NULL,
  `coAccountIDDeposits` int(11) DEFAULT NULL,
  `coAccountIDInterest` int(11) DEFAULT NULL,
  `coAccountIDFees` int(11) DEFAULT NULL,
  `OpenBalance` decimal(19,4) DEFAULT '0.0000',
  `Additions` decimal(19,4) DEFAULT '0.0000',
  `Subtractions` decimal(19,4) DEFAULT '0.0000',
  `UndepositedReceipts` decimal(19,4) DEFAULT '0.0000',
  `UnprintedPayables` decimal(19,4) DEFAULT '0.0000',
  `UnprintedPayroll` decimal(19,4) DEFAULT '0.0000',
  `NextCheckNumber` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`moAccountID`),
  KEY `moAccountID` (`moAccountID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `moaccount`
--

/*!40000 ALTER TABLE `moaccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `moaccount` ENABLE KEYS */;


--
-- Definition of table `molinkage`
--

DROP TABLE IF EXISTS `molinkage`;
CREATE TABLE `molinkage` (
  `moLinkageID` int(11) NOT NULL AUTO_INCREMENT,
  `DummyVal` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`moLinkageID`),
  KEY `moLinkageID` (`moLinkageID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `molinkage`
--

/*!40000 ALTER TABLE `molinkage` DISABLE KEYS */;
/*!40000 ALTER TABLE `molinkage` ENABLE KEYS */;


--
-- Definition of table `molinkagedetail`
--

DROP TABLE IF EXISTS `molinkagedetail`;
CREATE TABLE `molinkagedetail` (
  `moLinkageDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `veBillID` int(11) DEFAULT '0',
  `moTransactionID` int(11) DEFAULT '0',
  `moLinkageID` int(11) DEFAULT '0',
  `Amount` decimal(19,4) DEFAULT '0.0000',
  `Discount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`moLinkageDetailID`),
  KEY `moLinkageDetailID` (`moLinkageDetailID`),
  KEY `moLinkageID` (`moLinkageID`),
  KEY `moTransactionID` (`moTransactionID`),
  KEY `veBillID` (`veBillID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `molinkagedetail`
--

/*!40000 ALTER TABLE `molinkagedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `molinkagedetail` ENABLE KEYS */;


--
-- Definition of table `mopayrollchecktemp`
--

DROP TABLE IF EXISTS `mopayrollchecktemp`;
CREATE TABLE `mopayrollchecktemp` (
  `moPayrollCheckTempID` int(11) NOT NULL AUTO_INCREMENT,
  `moTransactionID` int(11) DEFAULT '0',
  PRIMARY KEY (`moPayrollCheckTempID`),
  UNIQUE KEY `moPayrollCheckTempID` (`moPayrollCheckTempID`),
  KEY `moTransactionID` (`moTransactionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `mopayrollchecktemp`
--

/*!40000 ALTER TABLE `mopayrollchecktemp` DISABLE KEYS */;
/*!40000 ALTER TABLE `mopayrollchecktemp` ENABLE KEYS */;


--
-- Definition of table `motransaction`
--

DROP TABLE IF EXISTS `motransaction`;
CREATE TABLE `motransaction` (
  `moTransactionID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `rxAddressID` int(11) DEFAULT NULL,
  `coAccountID` int(11) DEFAULT NULL,
  `moAccountID` int(11) DEFAULT NULL,
  `TransactionDate` datetime NOT NULL,
  `moTransactionTypeID` tinyint(4) DEFAULT '0',
  `CheckType` tinyint(4) DEFAULT '0',
  `Reference` varchar(12) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Void` tinyint(1) NOT NULL DEFAULT '0',
  `Reconciled` tinyint(1) NOT NULL DEFAULT '0',
  `TempRec` tinyint(1) NOT NULL DEFAULT '0',
  `Printed` tinyint(1) NOT NULL DEFAULT '0',
  `Amount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`moTransactionID`),
  KEY `coAccountID` (`coAccountID`),
  KEY `moTransactionID` (`moTransactionID`),
  KEY `moAccountID` (`moAccountID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `rxAddressID` (`rxAddressID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `motransaction`
--

/*!40000 ALTER TABLE `motransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `motransaction` ENABLE KEYS */;


--
-- Definition of table `motransactiontype`
--

DROP TABLE IF EXISTS `motransactiontype`;
CREATE TABLE `motransactiontype` (
  `moTransactionTypeID` tinyint(4) NOT NULL DEFAULT '0',
  `Description` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`moTransactionTypeID`),
  KEY `moTransactionTypeID` (`moTransactionTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `motransactiontype`
--

/*!40000 ALTER TABLE `motransactiontype` DISABLE KEYS */;
INSERT INTO `motransactiontype` (`moTransactionTypeID`,`Description`) VALUES 
 (0,'Deposit'),
 (1,'Withdrawal'),
 (2,'Check'),
 (3,'Fee'),
 (4,'Interest');
/*!40000 ALTER TABLE `motransactiontype` ENABLE KEYS */;


--
-- Definition of table `movendorchecktemp`
--

DROP TABLE IF EXISTS `movendorchecktemp`;
CREATE TABLE `movendorchecktemp` (
  `moVendorCheckTempID` int(11) NOT NULL AUTO_INCREMENT,
  `moTransactionID` int(11) DEFAULT '0',
  PRIMARY KEY (`moVendorCheckTempID`),
  UNIQUE KEY `moVendorCheckTempID` (`moVendorCheckTempID`),
  KEY `moTransactionID` (`moTransactionID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `movendorchecktemp`
--

/*!40000 ALTER TABLE `movendorchecktemp` DISABLE KEYS */;
/*!40000 ALTER TABLE `movendorchecktemp` ENABLE KEYS */;


--
-- Definition of table `pradjustment`
--

DROP TABLE IF EXISTS `pradjustment`;
CREATE TABLE `pradjustment` (
  `prAdjustmentID` int(11) NOT NULL AUTO_INCREMENT,
  `prMasterID` int(11) DEFAULT '0',
  PRIMARY KEY (`prAdjustmentID`),
  KEY `prAdjustmentID` (`prAdjustmentID`),
  KEY `prMasterID` (`prMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `pradjustment`
--

/*!40000 ALTER TABLE `pradjustment` DISABLE KEYS */;
/*!40000 ALTER TABLE `pradjustment` ENABLE KEYS */;


--
-- Definition of table `prcategory`
--

DROP TABLE IF EXISTS `prcategory`;
CREATE TABLE `prcategory` (
  `prCategoryID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `MarkupCost` tinyint(1) NOT NULL DEFAULT '0',
  `MarkupAmount` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`prCategoryID`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prcategory`
--

/*!40000 ALTER TABLE `prcategory` DISABLE KEYS */;
INSERT INTO `prcategory` (`prCategoryID`,`Description`,`InActive`,`MarkupCost`,`MarkupAmount`) VALUES 
 (1,'Twenty',0,1,'1.2000'),
 (2,'Thirty',0,1,'1.3000'),
 (3,'nada',0,0,'1.7000');
/*!40000 ALTER TABLE `prcategory` ENABLE KEYS */;


--
-- Definition of table `prdepartment`
--

DROP TABLE IF EXISTS `prdepartment`;
CREATE TABLE `prdepartment` (
  `prDepartmentID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `coAccountIDSales` int(11) DEFAULT NULL,
  `coAccountIDCOGS` int(11) DEFAULT NULL,
  PRIMARY KEY (`prDepartmentID`),
  KEY `prDepartmentID` (`prDepartmentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prdepartment`
--

/*!40000 ALTER TABLE `prdepartment` DISABLE KEYS */;
/*!40000 ALTER TABLE `prdepartment` ENABLE KEYS */;


--
-- Definition of table `prinventorycount`
--

DROP TABLE IF EXISTS `prinventorycount`;
CREATE TABLE `prinventorycount` (
  `prMasterID` int(11) NOT NULL DEFAULT '0',
  `prDepartmentID` int(11) DEFAULT NULL,
  `ItemCode` varchar(20) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `SystemOnHand` decimal(19,4) DEFAULT '0.0000',
  `CountedOnHand` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`prMasterID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `prDepartmentID` (`prDepartmentID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prinventorycount`
--

/*!40000 ALTER TABLE `prinventorycount` DISABLE KEYS */;
/*!40000 ALTER TABLE `prinventorycount` ENABLE KEYS */;


--
-- Definition of table `prmaster`
--

DROP TABLE IF EXISTS `prmaster`;
CREATE TABLE `prmaster` (
  `prMasterID` int(11) NOT NULL AUTO_INCREMENT,
  `prDepartmentID` int(11) DEFAULT NULL,
  `prCategoryID` int(11) DEFAULT NULL,
  `ItemCode` varchar(20) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `IsInventory` tinyint(1) DEFAULT '0',
  `IsTaxable` tinyint(1) DEFAULT '0',
  `PrintOnPOs` tinyint(1) DEFAULT '0',
  `PrintOnSOs` tinyint(1) DEFAULT '0',
  `PurchasingUnitMultiplier` decimal(19,4) DEFAULT '0.0000',
  `PurchasingUnitOfMeasure` varchar(6) DEFAULT NULL,
  `SalesUnitMultiplier` decimal(19,4) DEFAULT '0.0000',
  `SalesUnitOfMeasure` varchar(6) DEFAULT NULL,
  `rxMasterIDPrimaryVendor` int(11) DEFAULT NULL,
  `VendorItemNumber` varchar(20) DEFAULT NULL,
  `SalesPrice00` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice01` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice02` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice03` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice04` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice05` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice10` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice11` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice12` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice13` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice14` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice15` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice20` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice21` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice22` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice23` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice24` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice25` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice30` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice31` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice32` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice33` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice34` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice35` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice40` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice41` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice42` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice43` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice44` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice45` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice50` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice51` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice52` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice53` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice54` decimal(19,4) DEFAULT '0.0000',
  `SalesPrice55` decimal(19,4) DEFAULT '0.0000',
  `InitialCost` decimal(19,4) DEFAULT '0.0000',
  `AverageCost` decimal(19,4) DEFAULT '0.0000',
  `InventoryOnHand` decimal(19,4) DEFAULT '0.0000',
  `InventoryAllocated` decimal(19,4) DEFAULT '0.0000',
  `InventoryOnOrder` decimal(19,4) DEFAULT '0.0000',
  `InventoryOrderPoint` decimal(19,4) DEFAULT '0.0000',
  `InventoryOrderQuantity` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak0` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak1` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak2` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak3` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak4` decimal(19,4) DEFAULT '0.0000',
  `QuantityBreak5` decimal(19,4) DEFAULT '0.0000',
  `SingleItemTax` tinyint(1) NOT NULL DEFAULT '0',
  `HasInitialCost` tinyint(1) NOT NULL DEFAULT '0',
  `CostAsOf` datetime DEFAULT NULL,
  `InitialOnHand` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`prMasterID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `ItemCode` (`ItemCode`),
  KEY `prCategoryID` (`prCategoryID`),
  KEY `prDepartmentID` (`prDepartmentID`),
  KEY `rxMasterIDPrimaryVendor` (`rxMasterIDPrimaryVendor`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prmaster`
--

/*!40000 ALTER TABLE `prmaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `prmaster` ENABLE KEYS */;


--
-- Definition of table `prorderpoint`
--

DROP TABLE IF EXISTS `prorderpoint`;
CREATE TABLE `prorderpoint` (
  `prOrderPointID` int(11) NOT NULL AUTO_INCREMENT,
  `prMasterID` int(11) DEFAULT '0',
  `prWarehouseID` int(11) DEFAULT '0',
  `InventoryOrderPoint` decimal(19,4) DEFAULT '0.0000',
  `InventoryOrderQuantity` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`prOrderPointID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `prWarehouseID` (`prWarehouseID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prorderpoint`
--

/*!40000 ALTER TABLE `prorderpoint` DISABLE KEYS */;
/*!40000 ALTER TABLE `prorderpoint` ENABLE KEYS */;


--
-- Definition of table `prtransfer`
--

DROP TABLE IF EXISTS `prtransfer`;
CREATE TABLE `prtransfer` (
  `prTransferID` int(11) NOT NULL AUTO_INCREMENT,
  `TransferDate` datetime DEFAULT NULL,
  `prFromWarehouseID` int(11) DEFAULT NULL,
  `prToWarehouseID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`prTransferID`),
  KEY `prTransferID` (`prTransferID`),
  KEY `prFromWarehouseID` (`prFromWarehouseID`),
  KEY `prToWarehouseID` (`prToWarehouseID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prtransfer`
--

/*!40000 ALTER TABLE `prtransfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `prtransfer` ENABLE KEYS */;


--
-- Definition of table `prtransferdetail`
--

DROP TABLE IF EXISTS `prtransferdetail`;
CREATE TABLE `prtransferdetail` (
  `prTransferDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `prTransferID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `QuantityTransfered` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`prTransferDetailID`),
  UNIQUE KEY `prTransferDetailID` (`prTransferDetailID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `prTransferID` (`prTransferID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prtransferdetail`
--

/*!40000 ALTER TABLE `prtransferdetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `prtransferdetail` ENABLE KEYS */;


--
-- Definition of table `prtransferdetailworkfile`
--

DROP TABLE IF EXISTS `prtransferdetailworkfile`;
CREATE TABLE `prtransferdetailworkfile` (
  `prTransferDetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `prTransferID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `QuantityTransfered` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`prTransferDetailWorkFileID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `prTransferDetailWorkFileID` (`prTransferDetailWorkFileID`),
  KEY `prTransferID` (`prTransferID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prtransferdetailworkfile`
--

/*!40000 ALTER TABLE `prtransferdetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `prtransferdetailworkfile` ENABLE KEYS */;


--
-- Definition of table `prwarehouse`
--

DROP TABLE IF EXISTS `prwarehouse`;
CREATE TABLE `prwarehouse` (
  `prWarehouseID` int(11) NOT NULL AUTO_INCREMENT,
  `SearchName` varchar(50) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `Address1` varchar(40) DEFAULT NULL,
  `Address2` varchar(40) DEFAULT NULL,
  `City` varchar(30) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `Zip` varchar(10) DEFAULT NULL,
  `coAccountIDAsset` int(11) DEFAULT NULL,
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  `PrinterName` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`prWarehouseID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `prWarehouseID` (`prWarehouseID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prwarehouse`
--

/*!40000 ALTER TABLE `prwarehouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `prwarehouse` ENABLE KEYS */;


--
-- Definition of table `prwarehouseinventory`
--

DROP TABLE IF EXISTS `prwarehouseinventory`;
CREATE TABLE `prwarehouseinventory` (
  `prWarehouseInventoryID` int(11) NOT NULL AUTO_INCREMENT,
  `prMasterID` int(11) DEFAULT '-1',
  `prWarehouseID` int(11) DEFAULT '-1',
  `InventoryOnHand` decimal(19,4) DEFAULT '0.0000',
  `InventoryAllocated` decimal(19,4) DEFAULT '0.0000',
  `InventoryOnOrder` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`prWarehouseInventoryID`),
  UNIQUE KEY `prMasterID` (`prMasterID`,`prWarehouseID`),
  KEY `prMasterID_2` (`prMasterID`),
  KEY `prWarehouseID` (`prWarehouseID`),
  KEY `prWarehouseInventoryID` (`prWarehouseInventoryID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `prwarehouseinventory`
--

/*!40000 ALTER TABLE `prwarehouseinventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `prwarehouseinventory` ENABLE KEYS */;


--
-- Definition of table `quickap`
--

DROP TABLE IF EXISTS `quickap`;
CREATE TABLE `quickap` (
  `QuickAPID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `InvoiceDate` datetime DEFAULT NULL,
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `InvoiceAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`QuickAPID`),
  KEY `QuickAPID` (`QuickAPID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `quickap`
--

/*!40000 ALTER TABLE `quickap` DISABLE KEYS */;
/*!40000 ALTER TABLE `quickap` ENABLE KEYS */;


--
-- Definition of table `quickar`
--

DROP TABLE IF EXISTS `quickar`;
CREATE TABLE `quickar` (
  `QuickARID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `InvoiceDate` datetime DEFAULT NULL,
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `InvoiceAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`QuickARID`),
  KEY `QuickARID` (`QuickARID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `quickar`
--

/*!40000 ALTER TABLE `quickar` DISABLE KEYS */;
/*!40000 ALTER TABLE `quickar` ENABLE KEYS */;


--
-- Definition of table `quickjob`
--

DROP TABLE IF EXISTS `quickjob`;
CREATE TABLE `quickjob` (
  `QuickJobID` int(11) NOT NULL AUTO_INCREMENT,
  `JobNumber` varchar(10) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `rxMasterID` int(11) DEFAULT NULL,
  `BookDate` datetime DEFAULT NULL,
  `PriorCost` decimal(19,4) DEFAULT '0.0000',
  `PriorRevenue` decimal(19,4) DEFAULT '0.0000',
  `ContractAmount` decimal(19,4) DEFAULT '0.0000',
  `EstimatedProfit` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`QuickJobID`),
  KEY `QuickJobID` (`QuickJobID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `quickjob`
--

/*!40000 ALTER TABLE `quickjob` DISABLE KEYS */;
/*!40000 ALTER TABLE `quickjob` ENABLE KEYS */;


--
-- Definition of table `rxaddress`
--

DROP TABLE IF EXISTS `rxaddress`;
CREATE TABLE `rxaddress` (
  `rxAddressID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `Name` varchar(40) DEFAULT NULL,
  `Address1` varchar(40) DEFAULT NULL,
  `Address2` varchar(40) DEFAULT NULL,
  `City` varchar(30) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `Zip` varchar(10) DEFAULT NULL,
  `IsStreet` tinyint(1) DEFAULT '0',
  `IsMailing` tinyint(1) DEFAULT '0',
  `IsBillTo` tinyint(1) DEFAULT '0',
  `IsShipTo` tinyint(1) DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  PRIMARY KEY (`rxAddressID`),
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `rxAddressID` (`rxAddressID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxaddress`
--

/*!40000 ALTER TABLE `rxaddress` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxaddress` ENABLE KEYS */;


--
-- Definition of table `rxaddressworkfile`
--

DROP TABLE IF EXISTS `rxaddressworkfile`;
CREATE TABLE `rxaddressworkfile` (
  `rxAddressWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `rxAddressID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  `AlreadyExists` tinyint(1) DEFAULT '0',
  `IsDeleted` tinyint(1) DEFAULT '0',
  `InActive` tinyint(1) DEFAULT '0',
  `Address1` varchar(40) DEFAULT NULL,
  `Address2` varchar(40) DEFAULT NULL,
  `City` varchar(30) DEFAULT NULL,
  `State` varchar(2) DEFAULT NULL,
  `Zip` varchar(10) DEFAULT NULL,
  `IsStreet` tinyint(1) DEFAULT '0',
  `IsMailing` tinyint(1) DEFAULT '0',
  `IsBillTo` tinyint(1) DEFAULT '0',
  `IsShipTo` tinyint(1) DEFAULT '0',
  `coTaxTerritoryID` int(11) DEFAULT NULL,
  KEY `coTaxTerritoryID` (`coTaxTerritoryID`),
  KEY `rxAddressWorkFileID` (`rxAddressWorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxaddressworkfile`
--

/*!40000 ALTER TABLE `rxaddressworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxaddressworkfile` ENABLE KEYS */;


--
-- Definition of table `rxcontact`
--

DROP TABLE IF EXISTS `rxcontact`;
CREATE TABLE `rxcontact` (
  `rxContactID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `InActive` tinyint(1) DEFAULT '0',
  `Title` varchar(10) DEFAULT NULL,
  `FirstName` varchar(20) DEFAULT NULL,
  `MiddleInitial` varchar(1) DEFAULT NULL,
  `LastName` varchar(25) DEFAULT NULL,
  `JobPosition` varchar(25) DEFAULT NULL,
  `Phone` varchar(30) DEFAULT NULL,
  `Cell` varchar(30) DEFAULT NULL,
  `Pager` varchar(30) DEFAULT NULL,
  `Fax` varchar(30) DEFAULT NULL,
  `EMail` varchar(64) DEFAULT NULL,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  PRIMARY KEY (`rxContactID`),
  KEY `rxContactID` (`rxContactID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxcontact`
--

/*!40000 ALTER TABLE `rxcontact` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxcontact` ENABLE KEYS */;


--
-- Definition of table `rxcontactworkfile`
--

DROP TABLE IF EXISTS `rxcontactworkfile`;
CREATE TABLE `rxcontactworkfile` (
  `rxContactWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `rxContactID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  `AlreadyExists` tinyint(1) DEFAULT '0',
  `InActive` tinyint(1) DEFAULT '0',
  `Title` varchar(10) DEFAULT NULL,
  `FirstName` varchar(20) DEFAULT NULL,
  `MiddleInitial` varchar(1) DEFAULT NULL,
  `LastName` varchar(25) DEFAULT NULL,
  `JobPosition` varchar(25) DEFAULT NULL,
  `Phone` varchar(30) DEFAULT NULL,
  `Cell` varchar(30) DEFAULT NULL,
  `Pager` varchar(30) DEFAULT NULL,
  `Fax` varchar(30) DEFAULT NULL,
  `EMail` varchar(64) DEFAULT NULL,
  `ViewDeleted` tinyint(4) DEFAULT '0',
  `ViewName` varchar(50) DEFAULT NULL,
  `ViewPhone` varchar(50) DEFAULT NULL,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  PRIMARY KEY (`rxContactWorkFileID`),
  KEY `rxContactWorkFileID` (`rxContactWorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxcontactworkfile`
--

/*!40000 ALTER TABLE `rxcontactworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxcontactworkfile` ENABLE KEYS */;


--
-- Definition of table `rxjournal`
--

DROP TABLE IF EXISTS `rxjournal`;
CREATE TABLE `rxjournal` (
  `rxJournalID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT '0',
  `rxJournalTypeID` int(11) DEFAULT '0',
  `UserLoginID` int(11) DEFAULT '0',
  `EntryDate` datetime DEFAULT NULL,
  `EntryMemo` longtext,
  PRIMARY KEY (`rxJournalID`),
  UNIQUE KEY `rxJournalID` (`rxJournalID`),
  KEY `rxJournalTypeID` (`rxJournalTypeID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxjournal`
--

/*!40000 ALTER TABLE `rxjournal` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxjournal` ENABLE KEYS */;


--
-- Definition of table `rxjournaltype`
--

DROP TABLE IF EXISTS `rxjournaltype`;
CREATE TABLE `rxjournaltype` (
  `rxJournalTypeID` int(11) NOT NULL DEFAULT '0',
  `Description` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`rxJournalTypeID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxjournaltype`
--

/*!40000 ALTER TABLE `rxjournaltype` DISABLE KEYS */;
INSERT INTO `rxjournaltype` (`rxJournalTypeID`,`Description`) VALUES 
 (0,'General');
/*!40000 ALTER TABLE `rxjournaltype` ENABLE KEYS */;


--
-- Definition of table `rxjournalworkfile`
--

DROP TABLE IF EXISTS `rxjournalworkfile`;
CREATE TABLE `rxjournalworkfile` (
  `rxJournalWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `rxJournalID` int(11) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  `AlreadyExists` tinyint(1) DEFAULT '0',
  `rxJournalTypeID` int(11) DEFAULT '0',
  `IsDeleted` tinyint(4) DEFAULT '0',
  `UserLoginID` int(11) DEFAULT '0',
  `EntryDate` datetime DEFAULT NULL,
  `EntryMemo` longtext,
  PRIMARY KEY (`rxJournalWorkFileID`),
  KEY `rxJournalWorkFileID` (`rxJournalWorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxjournalworkfile`
--

/*!40000 ALTER TABLE `rxjournalworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxjournalworkfile` ENABLE KEYS */;


--
-- Definition of table `rxmaster`
--

DROP TABLE IF EXISTS `rxmaster`;
CREATE TABLE `rxmaster` (
  `rxMasterID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `Name` varchar(40) DEFAULT NULL,
  `SearchName` varchar(10) NOT NULL DEFAULT '',
  `FirstName` varchar(25) NOT NULL DEFAULT '',
  `MiddleInitial` varchar(1) DEFAULT NULL,
  `Phone1` varchar(30) DEFAULT NULL,
  `Phone2` varchar(30) DEFAULT NULL,
  `Phone3` varchar(30) DEFAULT NULL,
  `Fax` varchar(30) DEFAULT NULL,
  `IsCustomer` tinyint(1) NOT NULL DEFAULT '0',
  `IsVendor` tinyint(1) NOT NULL DEFAULT '0',
  `IsEmployee` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory1` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory2` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory3` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory4` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory5` tinyint(1) NOT NULL DEFAULT '0',
  `IsCategory6` tinyint(1) NOT NULL DEFAULT '0',
  `Note` longtext,
  `IsProspect` tinyint(1) NOT NULL DEFAULT '0',
  `Websight` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`rxMasterID`),
  KEY `rxMasterID` (`rxMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `rxmaster`
--

/*!40000 ALTER TABLE `rxmaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `rxmaster` ENABLE KEYS */;


--
-- Definition of table `sysaccountlinkage`
--

DROP TABLE IF EXISTS `sysaccountlinkage`;
CREATE TABLE `sysaccountlinkage` (
  `sysAccountLinkageID` int(11) NOT NULL DEFAULT '0',
  `coAccountIDNetSales` int(11) DEFAULT NULL,
  `coAccountIDPL` int(11) DEFAULT NULL,
  `coAccountIDCurEarnings` int(11) DEFAULT NULL,
  `coAccountIDRetainedEarnings` int(11) DEFAULT NULL,
  `coAccountIDAR` int(11) DEFAULT NULL,
  `coAccountIDDiscounts` int(11) DEFAULT NULL,
  `coAccountIDSalesTaxInv` int(11) DEFAULT NULL,
  `coAccountIDShipping` int(11) DEFAULT NULL,
  `coAccountIDOtherCharges` int(11) DEFAULT NULL,
  `coAccountIDPayments` int(11) DEFAULT NULL,
  `coAccountIDAP` int(11) DEFAULT NULL,
  `coAccountIDFreight` int(11) DEFAULT NULL,
  `coAccountIDSalesTaxPaid` int(11) DEFAULT NULL,
  `coAccountIDDiscountsTaken` int(11) DEFAULT NULL,
  `coAccountIDMisc` int(11) DEFAULT '0',
  `coRangeAsset1` varchar(12) DEFAULT NULL,
  `coRangeAsset2` varchar(12) DEFAULT NULL,
  `coRangeLiability1` varchar(12) DEFAULT NULL,
  `coRangeLiability2` varchar(12) DEFAULT NULL,
  `coRangeEquity1` varchar(12) DEFAULT NULL,
  `coRangeEquity2` varchar(12) DEFAULT NULL,
  `coRangeIncome1` varchar(12) DEFAULT NULL,
  `coRangeIncome2` varchar(12) DEFAULT NULL,
  `coRangeExpense1` varchar(12) DEFAULT NULL,
  `coRangeExpense2` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`sysAccountLinkageID`),
  KEY `sysAccountLinkageID` (`sysAccountLinkageID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sysaccountlinkage`
--

/*!40000 ALTER TABLE `sysaccountlinkage` DISABLE KEYS */;
INSERT INTO `sysaccountlinkage` (`sysAccountLinkageID`,`coAccountIDNetSales`,`coAccountIDPL`,`coAccountIDCurEarnings`,`coAccountIDRetainedEarnings`,`coAccountIDAR`,`coAccountIDDiscounts`,`coAccountIDSalesTaxInv`,`coAccountIDShipping`,`coAccountIDOtherCharges`,`coAccountIDPayments`,`coAccountIDAP`,`coAccountIDFreight`,`coAccountIDSalesTaxPaid`,`coAccountIDDiscountsTaken`,`coAccountIDMisc`,`coRangeAsset1`,`coRangeAsset2`,`coRangeLiability1`,`coRangeLiability2`,`coRangeEquity1`,`coRangeEquity2`,`coRangeIncome1`,`coRangeIncome2`,`coRangeExpense1`,`coRangeExpense2`) VALUES 
 (1,124,124,124,124,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0100','0199','0200','0299','0300','0399','0400','0499','0500','9999');
/*!40000 ALTER TABLE `sysaccountlinkage` ENABLE KEYS */;


--
-- Definition of table `sysassignment`
--

DROP TABLE IF EXISTS `sysassignment`;
CREATE TABLE `sysassignment` (
  `sysAssignmentID` tinyint(4) NOT NULL DEFAULT '0',
  `Title` varchar(12) DEFAULT NULL,
  `DefaultID` int(11) DEFAULT NULL,
  `AllowCustomerChange` tinyint(1) DEFAULT '0',
  `AllowTransactionChange` tinyint(1) DEFAULT '0',
  `TransDefaultLogin` tinyint(1) DEFAULT '0',
  `AllocatePctJob` decimal(19,4) DEFAULT '0.0000',
  `AllocatePctSales` decimal(19,4) DEFAULT '0.0000',
  `AllocatePctProfit` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`sysAssignmentID`),
  KEY `DefaultID` (`DefaultID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sysassignment`
--

/*!40000 ALTER TABLE `sysassignment` DISABLE KEYS */;
INSERT INTO `sysassignment` (`sysAssignmentID`,`Title`,`DefaultID`,`AllowCustomerChange`,`AllowTransactionChange`,`TransDefaultLogin`,`AllocatePctJob`,`AllocatePctSales`,`AllocatePctProfit`) VALUES 
 (0,'Salesrep',NULL,1,1,0,'0.0000','0.0000','0.0000'),
 (1,'CSR',NULL,1,1,0,'0.0000','0.0000','0.0000'),
 (2,'Salesmgr.',NULL,0,0,0,'0.0000','0.0000','0.0000'),
 (3,'Engineer',4,0,0,0,'0.0000','0.0000','0.0000'),
 (4,'',NULL,0,0,0,'0.0000','0.0000','0.0000');
/*!40000 ALTER TABLE `sysassignment` ENABLE KEYS */;


--
-- Definition of table `sysinfo`
--

DROP TABLE IF EXISTS `sysinfo`;
CREATE TABLE `sysinfo` (
  `sysInfoID` int(11) NOT NULL DEFAULT '0',
  `DataMark` int(11) NOT NULL DEFAULT '0',
  `DBRequiresMajor` int(11) DEFAULT '0',
  `DBRequiresMinor` int(11) DEFAULT '0',
  `DBRequiresRevision` int(11) DEFAULT '0',
  `LatestMajor` int(11) DEFAULT '0',
  `LatestMinor` int(11) DEFAULT '0',
  `LatestRevision` int(11) DEFAULT '0',
  `Locked` tinyint(1) DEFAULT '0',
  `LockedReason` varchar(200) DEFAULT NULL,
  `CashBasisAccounting` tinyint(1) DEFAULT '0',
  `CurrentFiscalYearID` int(11) DEFAULT NULL,
  `CurrentPeriodID` int(11) DEFAULT NULL,
  `CashBasis` tinyint(1) DEFAULT '0',
  `rxMasterCategory1Desc` varchar(12) DEFAULT NULL,
  `rxMasterCategory2Desc` varchar(12) DEFAULT NULL,
  `rxMasterCategory3Desc` varchar(12) DEFAULT NULL,
  `rxMasterCategory4Desc` varchar(12) DEFAULT NULL,
  `rxMasterCategory5Desc` varchar(12) DEFAULT NULL,
  `rxMasterCategory6Desc` varchar(12) DEFAULT NULL,
  `coCompanyNameAddress0` varchar(50) DEFAULT NULL,
  `coCompanyNameAddress1` varchar(50) DEFAULT NULL,
  `coCompanyNameAddress2` varchar(50) DEFAULT NULL,
  `coCompanyNameAddress3` varchar(50) DEFAULT NULL,
  `coCompanyNameAddress4` varchar(50) DEFAULT NULL,
  `prPriceLevel0` varchar(12) DEFAULT NULL,
  `prPriceLevel1` varchar(12) DEFAULT NULL,
  `prPriceLevel2` varchar(12) DEFAULT NULL,
  `prPriceLevel3` varchar(12) DEFAULT NULL,
  `prPriceLevel4` varchar(12) DEFAULT NULL,
  `prPriceLevel5` varchar(12) DEFAULT NULL,
  `prPriceLevel6` varchar(12) DEFAULT NULL,
  `BillPayAccountID` int(11) DEFAULT '0',
  `POBillTo_Name` varchar(40) DEFAULT NULL,
  `POBillTo_Address1` varchar(40) DEFAULT NULL,
  `POBillTo_Address2` varchar(40) DEFAULT NULL,
  `POBillTo_City` varchar(30) DEFAULT NULL,
  `POBillTo_State` varchar(2) DEFAULT NULL,
  `POBillTo_Zip` varchar(10) DEFAULT NULL,
  `POShipTo_Name` varchar(40) DEFAULT NULL,
  `POShipTo_Address1` varchar(40) DEFAULT NULL,
  `POShipTo_Address2` varchar(40) DEFAULT NULL,
  `POShipTo_City` varchar(30) DEFAULT NULL,
  `POShipTo_State` varchar(2) DEFAULT NULL,
  `POShipTo_Zip` varchar(10) DEFAULT NULL,
  `joSource1Desc` varchar(8) DEFAULT NULL,
  `joSource2Desc` varchar(8) DEFAULT NULL,
  `joSource3Desc` varchar(8) DEFAULT NULL,
  `joSource4Desc` varchar(8) DEFAULT NULL,
  `QuotedPriceID` int(11) DEFAULT NULL,
  `RepDeduct1Desc` varchar(15) DEFAULT NULL,
  `RepDeduct2Desc` varchar(15) DEFAULT NULL,
  `RepDeduct3Desc` varchar(15) DEFAULT NULL,
  `RepDeduct4Desc` varchar(15) DEFAULT NULL,
  `SalesMgrID` int(11) DEFAULT NULL,
  `CommissionOverride` decimal(19,4) DEFAULT '0.0000',
  `SalesTaxDist1` varchar(15) DEFAULT NULL,
  `SalesTaxDist2` varchar(15) DEFAULT NULL,
  `SalesTaxDist3` varchar(15) DEFAULT NULL,
  `SalesTaxDist4` varchar(15) DEFAULT NULL,
  `SalesTaxDist5` varchar(15) DEFAULT NULL,
  `SalesTaxDist6` varchar(15) DEFAULT NULL,
  `SalesTaxDist7` varchar(15) DEFAULT NULL,
  `SalesTaxDist8` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`sysInfoID`),
  KEY `SalesMgrID` (`SalesMgrID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sysinfo`
--

/*!40000 ALTER TABLE `sysinfo` DISABLE KEYS */;
INSERT INTO `sysinfo` (`sysInfoID`,`DataMark`,`DBRequiresMajor`,`DBRequiresMinor`,`DBRequiresRevision`,`LatestMajor`,`LatestMinor`,`LatestRevision`,`Locked`,`LockedReason`,`CashBasisAccounting`,`CurrentFiscalYearID`,`CurrentPeriodID`,`CashBasis`,`rxMasterCategory1Desc`,`rxMasterCategory2Desc`,`rxMasterCategory3Desc`,`rxMasterCategory4Desc`,`rxMasterCategory5Desc`,`rxMasterCategory6Desc`,`coCompanyNameAddress0`,`coCompanyNameAddress1`,`coCompanyNameAddress2`,`coCompanyNameAddress3`,`coCompanyNameAddress4`,`prPriceLevel0`,`prPriceLevel1`,`prPriceLevel2`,`prPriceLevel3`,`prPriceLevel4`,`prPriceLevel5`,`prPriceLevel6`,`BillPayAccountID`,`POBillTo_Name`,`POBillTo_Address1`,`POBillTo_Address2`,`POBillTo_City`,`POBillTo_State`,`POBillTo_Zip`,`POShipTo_Name`,`POShipTo_Address1`,`POShipTo_Address2`,`POShipTo_City`,`POShipTo_State`,`POShipTo_Zip`,`joSource1Desc`,`joSource2Desc`,`joSource3Desc`,`joSource4Desc`,`QuotedPriceID`,`RepDeduct1Desc`,`RepDeduct2Desc`,`RepDeduct3Desc`,`RepDeduct4Desc`,`SalesMgrID`,`CommissionOverride`,`SalesTaxDist1`,`SalesTaxDist2`,`SalesTaxDist3`,`SalesTaxDist4`,`SalesTaxDist5`,`SalesTaxDist6`,`SalesTaxDist7`,`SalesTaxDist8`) VALUES 
 (1,68,0,0,0,0,0,0,0,NULL,0,2,3,0,'Architect','Engineer','GC','Owner','Bond Agent','Const. Mgr.','Your Company Name','','','','','Retail','','','','','',NULL,1,'HVAC Associates, Inc.','P.O. Box 1355','CORPORATE HQ - SE FLORIDA','Pompano Beach','FL','33061','Your Company Name',NULL,NULL,NULL,NULL,NULL,'aaaaaaaa','bbbbbbbb','cccccccc','dddddddd',8,NULL,NULL,NULL,NULL,0,'0.0000','','','','','','','','');
/*!40000 ALTER TABLE `sysinfo` ENABLE KEYS */;


--
-- Definition of table `syslock`
--

DROP TABLE IF EXISTS `syslock`;
CREATE TABLE `syslock` (
  `sysLockID` int(11) NOT NULL AUTO_INCREMENT,
  `LockTime` datetime DEFAULT NULL,
  `LockType` int(11) DEFAULT '0',
  `UserLoginID` int(11) DEFAULT '0',
  PRIMARY KEY (`sysLockID`),
  KEY `sysLockID` (`sysLockID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `syslock`
--

/*!40000 ALTER TABLE `syslock` DISABLE KEYS */;
/*!40000 ALTER TABLE `syslock` ENABLE KEYS */;


--
-- Definition of table `sysprivilege`
--

DROP TABLE IF EXISTS `sysprivilege`;
CREATE TABLE `sysprivilege` (
  `sysPrivilegeID` int(11) NOT NULL AUTO_INCREMENT,
  `AccessProcedureID` int(11) DEFAULT NULL,
  `UserLoginID` int(11) DEFAULT NULL,
  `UserGroupID` int(11) DEFAULT NULL,
  `PrivilegeValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`sysPrivilegeID`),
  KEY `AccessProcedureID` (`AccessProcedureID`),
  KEY `sysPrivilegeID` (`sysPrivilegeID`),
  KEY `UserGroupID` (`UserGroupID`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM AUTO_INCREMENT=107 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sysprivilege`
--

/*!40000 ALTER TABLE `sysprivilege` DISABLE KEYS */;
INSERT INTO `sysprivilege` (`sysPrivilegeID`,`AccessProcedureID`,`UserLoginID`,`UserGroupID`,`PrivilegeValue`) VALUES 
 (1,821000,1,NULL,1),
 (2,822000,1,NULL,1),
 (3,823000,1,NULL,1),
 (8,711000,1,NULL,1),
 (9,711004,1,NULL,1),
 (10,711000,NULL,3,1),
 (11,821000,NULL,3,1),
 (12,822000,NULL,3,1),
 (17,801500,1,NULL,1),
 (18,801300,1,NULL,1),
 (19,801200,1,NULL,1),
 (20,801100,1,NULL,1),
 (21,801000,1,NULL,1),
 (22,623000,1,NULL,1),
 (23,622000,1,NULL,1),
 (24,621000,1,NULL,1),
 (25,619100,1,NULL,1),
 (26,619000,1,NULL,1),
 (28,690100,1,NULL,1),
 (29,690300,1,NULL,1),
 (30,690500,1,NULL,1),
 (31,690600,1,NULL,1),
 (32,690700,1,NULL,1),
 (33,160600,1,NULL,1),
 (34,160500,1,NULL,1),
 (35,160400,1,NULL,1),
 (36,160300,1,NULL,1),
 (37,160200,1,NULL,1),
 (38,160100,1,NULL,1),
 (39,591100,1,NULL,1),
 (40,591000,1,NULL,1),
 (41,590900,1,NULL,1),
 (42,590800,1,NULL,1),
 (43,590700,1,NULL,1),
 (44,590600,1,NULL,1),
 (45,590500,1,NULL,1),
 (46,590400,1,NULL,1),
 (47,590300,1,NULL,1),
 (48,590200,1,NULL,1),
 (49,590100,1,NULL,1),
 (50,390900,1,NULL,1),
 (51,390800,1,NULL,1),
 (52,390500,1,NULL,1),
 (53,390400,1,NULL,1),
 (54,390300,1,NULL,1),
 (55,390200,1,NULL,1),
 (56,390100,1,NULL,1),
 (57,211000,1,NULL,1),
 (58,221000,1,NULL,1),
 (59,222000,1,NULL,1),
 (60,223000,1,NULL,1),
 (61,224000,1,NULL,1),
 (62,290100,1,NULL,1),
 (63,290200,1,NULL,1),
 (64,290300,1,NULL,1),
 (65,101200,1,NULL,1),
 (66,110000,1,NULL,1),
 (67,112000,1,NULL,1),
 (68,120000,1,NULL,1),
 (69,130000,1,NULL,1),
 (70,140000,1,NULL,1),
 (72,412000,1,NULL,1),
 (73,422000,1,NULL,1),
 (74,421000,1,NULL,1),
 (75,413000,1,NULL,1),
 (76,422100,1,NULL,1),
 (77,423000,1,NULL,1),
 (78,431000,1,NULL,1),
 (79,432000,1,NULL,1),
 (80,995100,1,NULL,1),
 (81,311000,1,NULL,1),
 (82,110100,1,NULL,1),
 (84,422001,1,NULL,1),
 (87,711003,1,NULL,1),
 (88,711002,1,NULL,1),
 (89,711001,1,NULL,1),
 (90,721000,1,NULL,1),
 (91,722000,1,NULL,1),
 (92,711100,1,NULL,1),
 (93,422002,1,NULL,1),
 (96,801400,1,NULL,1),
 (97,801600,1,NULL,1),
 (98,801700,1,NULL,1),
 (102,101201,1,NULL,1),
 (103,411000,2,NULL,1),
 (104,101200,2,NULL,1),
 (105,421000,2,NULL,1),
 (106,422000,2,NULL,1);
/*!40000 ALTER TABLE `sysprivilege` ENABLE KEYS */;


--
-- Definition of table `syssequence`
--

DROP TABLE IF EXISTS `syssequence`;
CREATE TABLE `syssequence` (
  `sysSequenceID` int(11) NOT NULL AUTO_INCREMENT,
  `TableName` varchar(50) NOT NULL,
  `Sequence` int(11) NOT NULL,
  `WorkstationID` int(11) DEFAULT NULL,
  PRIMARY KEY (`sysSequenceID`),
  UNIQUE KEY `TableName` (`TableName`),
  KEY `sysSequenceID` (`sysSequenceID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `syssequence`
--

/*!40000 ALTER TABLE `syssequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `syssequence` ENABLE KEYS */;


--
-- Definition of table `systablelock`
--

DROP TABLE IF EXISTS `systablelock`;
CREATE TABLE `systablelock` (
  `sysTableLockKey` varchar(50) NOT NULL,
  `LockTime` datetime DEFAULT NULL,
  `UserLoginID` int(11) DEFAULT '0',
  `WorkstationID` int(11) DEFAULT '0',
  PRIMARY KEY (`sysTableLockKey`),
  KEY `sysTableLockKey` (`sysTableLockKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `systablelock`
--

/*!40000 ALTER TABLE `systablelock` DISABLE KEYS */;
/*!40000 ALTER TABLE `systablelock` ENABLE KEYS */;


--
-- Definition of table `sysvariable`
--

DROP TABLE IF EXISTS `sysvariable`;
CREATE TABLE `sysvariable` (
  `sysVariableID` int(11) NOT NULL DEFAULT '0',
  `ValueLong` int(11) DEFAULT '0',
  `ValueCurrency` decimal(19,4) DEFAULT '0.0000',
  `ValueString` varchar(255) DEFAULT NULL,
  `ValueDate` datetime DEFAULT NULL,
  `ValueMemo` longtext,
  PRIMARY KEY (`sysVariableID`),
  KEY `sysVariableID` (`sysVariableID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sysvariable`
--

/*!40000 ALTER TABLE `sysvariable` DISABLE KEYS */;
INSERT INTO `sysvariable` (`sysVariableID`,`ValueLong`,`ValueCurrency`,`ValueString`,`ValueDate`,`ValueMemo`) VALUES 
 (1,1,'0.0000',NULL,NULL,NULL),
 (2,0,'0.0000',NULL,NULL,NULL),
 (3,0,'0.0000',NULL,NULL,NULL),
 (4,-1,'0.0000',NULL,NULL,NULL),
 (5,-1,'0.0000',NULL,NULL,NULL),
 (6,-1,'0.0000',NULL,NULL,NULL),
 (25,1,'0.0000',NULL,NULL,NULL),
 (26,0,'0.0000',NULL,NULL,NULL),
 (27,1,'0.0000',NULL,NULL,NULL),
 (28,0,'0.0000',NULL,'2005-02-24 00:00:00',NULL),
 (29,0,'0.0000',NULL,NULL,'This is text that will appear for joint check invoices only.  This is text that will appear for joint check invoices only.  This is text that will appear for joint check invoices only.  This is text that will appear for joint check invoices only.  This is text that will appear for joint check invoices only.'),
 (8014,1,'0.0000',NULL,NULL,NULL),
 (1000000,1,'0.0000',NULL,NULL,NULL),
 (1010000,1,'0.0000',NULL,NULL,NULL),
 (1010001,1,'0.0000',NULL,NULL,NULL),
 (1010004,1,'0.0000',NULL,NULL,NULL),
 (1010005,1,'0.0000',NULL,NULL,NULL),
 (1020000,0,'1.1000',NULL,NULL,NULL),
 (1030000,1,'0.0000',NULL,NULL,NULL),
 (1040001,1,'0.0000',NULL,NULL,NULL),
 (1040002,0,'0.0000',NULL,NULL,NULL),
 (1040003,0,'0.0000','/QB',NULL,NULL),
 (1040004,0,'0.0000','',NULL,NULL),
 (1040005,0,'0.0000','',NULL,NULL),
 (1040006,0,'0.0000','',NULL,NULL),
 (1040007,0,'0.0000','',NULL,NULL),
 (1040008,0,'0.0000','Job Commissions',NULL,NULL),
 (1040009,0,'0.0000','Warehouse Commissions',NULL,NULL),
 (1040010,0,'0.0000','Override Commissions',NULL,NULL),
 (1050000,0,'0.0000',NULL,NULL,NULL),
 (1060001,0,'0.0000','',NULL,NULL),
 (1060002,0,'0.0000','',NULL,NULL),
 (1060003,0,'0.0000','Requested NOC?',NULL,NULL),
 (1060004,1,'0.0000',NULL,NULL,NULL),
 (1060005,0,'0.0000','Received NOC?',NULL,NULL),
 (1060006,1,'0.0000',NULL,NULL,NULL),
 (1060007,0,'0.0000','NTC Sent GC?',NULL,NULL),
 (1060008,1,'0.0000',NULL,NULL,NULL),
 (1060009,0,'0.0000',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sysvariable` ENABLE KEYS */;


--
-- Definition of table `userloginclone`
--

DROP TABLE IF EXISTS `userloginclone`;
CREATE TABLE `userloginclone` (
  `UserLoginID` int(11) NOT NULL DEFAULT '0',
  `LoginName` varchar(15) DEFAULT NULL,
  `LoginPassword` varchar(50) DEFAULT NULL,
  `FullName` varchar(50) DEFAULT NULL,
  `Initials` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`UserLoginID`),
  KEY `Initials` (`Initials`),
  KEY `UserLoginID` (`UserLoginID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `userloginclone`
--

/*!40000 ALTER TABLE `userloginclone` DISABLE KEYS */;
/*!40000 ALTER TABLE `userloginclone` ENABLE KEYS */;


--
-- Definition of table `vebill`
--

DROP TABLE IF EXISTS `vebill`;
CREATE TABLE `vebill` (
  `veBillID` int(11) NOT NULL AUTO_INCREMENT,
  `TransactionStatus` int(11) DEFAULT '1',
  `rxMasterID` int(11) DEFAULT NULL,
  `vePOID` int(11) DEFAULT NULL,
  `joReleaseDetailID` int(11) DEFAULT NULL,
  `cuInvoiceID_ApplyCost` int(11) DEFAULT NULL,
  `BillDate` datetime NOT NULL,
  `UsePostDate` tinyint(1) NOT NULL DEFAULT '0',
  `ReceiveDate` datetime DEFAULT NULL,
  `PostDate` datetime NOT NULL,
  `DueDate` datetime DEFAULT NULL,
  `ShipDate` datetime DEFAULT NULL,
  `BillAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `AppliedAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `TaxAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `FreightAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `APAccountID` int(11) DEFAULT NULL,
  `Void` tinyint(1) NOT NULL DEFAULT '0',
  `Applied` tinyint(1) NOT NULL DEFAULT '0',
  `veShipViaID` int(11) DEFAULT NULL,
  `TrackingNumber` varchar(30) DEFAULT NULL,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `AdditionalFreight` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`veBillID`),
  KEY `cuInvoiceID_ApplyCost` (`cuInvoiceID_ApplyCost`),
  KEY `veBillID` (`veBillID`),
  KEY `joReleaseDetailID` (`joReleaseDetailID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `APAccountID` (`APAccountID`),
  KEY `vePOID` (`vePOID`),
  KEY `veShipViaID` (`veShipViaID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebill`
--

/*!40000 ALTER TABLE `vebill` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebill` ENABLE KEYS */;


--
-- Definition of table `vebilldetail`
--

DROP TABLE IF EXISTS `vebilldetail`;
CREATE TABLE `vebilldetail` (
  `veBillDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `veBillID` int(11) DEFAULT '0',
  `vePODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `FreightCost` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`veBillDetailID`),
  KEY `veBillDetailID` (`veBillDetailID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `veBillID` (`veBillID`),
  KEY `vePODetailID` (`vePODetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebilldetail`
--

/*!40000 ALTER TABLE `vebilldetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebilldetail` ENABLE KEYS */;


--
-- Definition of table `vebilldetailworkfile`
--

DROP TABLE IF EXISTS `vebilldetailworkfile`;
CREATE TABLE `vebilldetailworkfile` (
  `veBillDetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `vePODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`veBillDetailWorkFileID`),
  UNIQUE KEY `veBillDetailWorkFileID` (`veBillDetailWorkFileID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `vePODetailID` (`vePODetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebilldetailworkfile`
--

/*!40000 ALTER TABLE `vebilldetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebilldetailworkfile` ENABLE KEYS */;


--
-- Definition of table `vebilldistribution`
--

DROP TABLE IF EXISTS `vebilldistribution`;
CREATE TABLE `vebilldistribution` (
  `veBillDistributionID` int(11) NOT NULL AUTO_INCREMENT,
  `veBillID` int(11) DEFAULT '0',
  `coExpenseAccountID` int(11) DEFAULT NULL,
  `ExpenseAmount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`veBillDistributionID`),
  KEY `coExpenseAccountID` (`coExpenseAccountID`),
  KEY `veBillDistributionID` (`veBillDistributionID`),
  KEY `veBillID` (`veBillID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebilldistribution`
--

/*!40000 ALTER TABLE `vebilldistribution` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebilldistribution` ENABLE KEYS */;


--
-- Definition of table `vebilldistributionworkfile`
--

DROP TABLE IF EXISTS `vebilldistributionworkfile`;
CREATE TABLE `vebilldistributionworkfile` (
  `veBillDistributionWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `coExpenseAccountID` int(11) DEFAULT NULL,
  `ExpenseAmount` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`veBillDistributionWorkFileID`),
  KEY `coExpenseAccountID` (`coExpenseAccountID`),
  KEY `veBillDistributionWorkFileID` (`veBillDistributionWorkFileID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebilldistributionworkfile`
--

/*!40000 ALTER TABLE `vebilldistributionworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebilldistributionworkfile` ENABLE KEYS */;


--
-- Definition of table `vebillpay`
--

DROP TABLE IF EXISTS `vebillpay`;
CREATE TABLE `vebillpay` (
  `veBillPayID` int(11) NOT NULL AUTO_INCREMENT,
  `veBillID` int(11) NOT NULL,
  `ApplyingAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `DiscountAmount` decimal(19,4) NOT NULL DEFAULT '0.0000',
  PRIMARY KEY (`veBillPayID`),
  KEY `veBillID` (`veBillID`),
  KEY `veBillPayID` (`veBillPayID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vebillpay`
--

/*!40000 ALTER TABLE `vebillpay` DISABLE KEYS */;
/*!40000 ALTER TABLE `vebillpay` ENABLE KEYS */;


--
-- Definition of table `vecommdetail`
--

DROP TABLE IF EXISTS `vecommdetail`;
CREATE TABLE `vecommdetail` (
  `veCommDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `joReleaseDetailID` int(11) DEFAULT NULL,
  `ShipDate` datetime DEFAULT NULL,
  `veShipViaID` int(11) DEFAULT NULL,
  `TrackingNumber` varchar(30) DEFAULT NULL,
  `InvoiceNumber` varchar(20) DEFAULT NULL,
  `BillAmount` decimal(19,4) DEFAULT NULL,
  `CommDate` datetime DEFAULT NULL,
  `CommAmount` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`veCommDetailID`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vecommdetail`
--

/*!40000 ALTER TABLE `vecommdetail` DISABLE KEYS */;
INSERT INTO `vecommdetail` (`veCommDetailID`,`rxMasterID`,`joReleaseDetailID`,`ShipDate`,`veShipViaID`,`TrackingNumber`,`InvoiceNumber`,`BillAmount`,`CommDate`,`CommAmount`) VALUES 
 (1,NULL,NULL,NULL,1,'trac','ddd','1.0000',NULL,'2.0000'),
 (2,NULL,NULL,'2005-06-12 00:00:00',1,'123','456','7.0000','2005-06-13 00:00:00','8.0000'),
 (3,NULL,14,'2005-06-15 00:00:00',1,'123123123','more','123.0000',NULL,'456.0000'),
 (4,NULL,15,'2005-06-15 00:00:00',NULL,'sss',NULL,NULL,NULL,'111.0000'),
 (5,NULL,16,'2005-06-16 00:00:00',NULL,'ddd','zzzzz',NULL,'2005-06-16 00:00:00',NULL);
/*!40000 ALTER TABLE `vecommdetail` ENABLE KEYS */;


--
-- Definition of table `vefreightcharges`
--

DROP TABLE IF EXISTS `vefreightcharges`;
CREATE TABLE `vefreightcharges` (
  `veFreightChargesID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(20) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`veFreightChargesID`),
  KEY `veFreightChargesID` (`veFreightChargesID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vefreightcharges`
--

/*!40000 ALTER TABLE `vefreightcharges` DISABLE KEYS */;
/*!40000 ALTER TABLE `vefreightcharges` ENABLE KEYS */;


--
-- Definition of table `vemaster`
--

DROP TABLE IF EXISTS `vemaster`;
CREATE TABLE `vemaster` (
  `veMasterID` int(11) NOT NULL DEFAULT '0',
  `DueDays` int(11) NOT NULL DEFAULT '0',
  `DiscountDays` int(11) NOT NULL DEFAULT '0',
  `DiscountPercent` int(11) NOT NULL DEFAULT '0',
  `DueOnDay` tinyint(1) NOT NULL DEFAULT '0',
  `DiscOnDay` tinyint(1) NOT NULL DEFAULT '0',
  `DiscountIncludesFreight` tinyint(1) NOT NULL DEFAULT '0',
  `Manufacturer` varchar(20) DEFAULT NULL,
  `coExpenseAccountID` int(11) DEFAULT NULL,
  `Tax1099` tinyint(1) NOT NULL DEFAULT '0',
  `SSN` varchar(11) DEFAULT NULL,
  `ImportType` int(11) DEFAULT NULL,
  `AccountNumber` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`veMasterID`),
  KEY `coExpenseAccountID` (`coExpenseAccountID`),
  KEY `Manufacturer` (`Manufacturer`),
  KEY `veMasterID` (`veMasterID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vemaster`
--

/*!40000 ALTER TABLE `vemaster` DISABLE KEYS */;
/*!40000 ALTER TABLE `vemaster` ENABLE KEYS */;


--
-- Definition of table `vepo`
--

DROP TABLE IF EXISTS `vepo`;
CREATE TABLE `vepo` (
  `vePOID` int(11) NOT NULL AUTO_INCREMENT,
  `CreatedByID` int(11) DEFAULT NULL,
  `CreatedOn` datetime DEFAULT NULL,
  `ChangedByID` int(11) DEFAULT NULL,
  `ChangedOn` datetime DEFAULT NULL,
  `TransactionStatus` int(11) DEFAULT '1',
  `joReleaseID` int(11) DEFAULT NULL,
  `rxVendorID` int(11) DEFAULT NULL,
  `rxBillToID` int(11) DEFAULT NULL,
  `rxBillToAddressID` int(11) DEFAULT NULL,
  `rxShipToID` int(11) DEFAULT NULL,
  `rxShipToAddressID` int(11) DEFAULT NULL,
  `veShipViaID` int(11) DEFAULT NULL,
  `prWarehouseID` int(11) DEFAULT NULL,
  `veFreightChargesID` int(11) DEFAULT NULL,
  `OrderedByID` int(11) DEFAULT NULL,
  `BillToMode` tinyint(4) NOT NULL DEFAULT '0',
  `ShipToMode` tinyint(4) NOT NULL DEFAULT '0',
  `Acknowledged` tinyint(1) NOT NULL DEFAULT '0',
  `PONumber` varchar(20) DEFAULT NULL,
  `VendorOrderNumber` varchar(20) DEFAULT NULL,
  `CustomerPONumber` varchar(20) DEFAULT NULL,
  `OrderDate` datetime DEFAULT NULL,
  `AcknowledgementDate` datetime DEFAULT NULL,
  `EstimatedShipDate` datetime DEFAULT NULL,
  `DateWanted` varchar(12) DEFAULT NULL,
  `Tag` longtext,
  `SpecialInstructions` longtext,
  `Subtotal` decimal(19,4) DEFAULT '0.0000',
  `TaxTotal` decimal(19,4) DEFAULT '0.0000',
  `Freight` decimal(19,4) DEFAULT '0.0000',
  `TaxRate` decimal(19,4) DEFAULT '0.0000',
  `rxVendorAddressID` int(11) DEFAULT NULL,
  `rxVendorContactID` int(11) DEFAULT NULL,
  PRIMARY KEY (`vePOID`),
  KEY `rxBillToID` (`rxBillToID`),
  KEY `vePOID` (`vePOID`),
  KEY `joReleaseID` (`joReleaseID`),
  KEY `PONumber` (`PONumber`),
  KEY `OrderedByID` (`OrderedByID`),
  KEY `prWarehouseID` (`prWarehouseID`),
  KEY `rxBillToAddressID` (`rxBillToAddressID`),
  KEY `rxShipToAddressID` (`rxShipToAddressID`),
  KEY `rxVendorID` (`rxVendorID`),
  KEY `rxShipToID` (`rxShipToID`),
  KEY `veFreightChargesID` (`veFreightChargesID`),
  KEY `veShipViaID` (`veShipViaID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vepo`
--

/*!40000 ALTER TABLE `vepo` DISABLE KEYS */;
/*!40000 ALTER TABLE `vepo` ENABLE KEYS */;


--
-- Definition of table `vepodetail`
--

DROP TABLE IF EXISTS `vepodetail`;
CREATE TABLE `vepodetail` (
  `vePODetailID` int(11) NOT NULL AUTO_INCREMENT,
  `vePOID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityOrdered` decimal(19,4) DEFAULT '0.0000',
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Taxable` tinyint(1) DEFAULT '0',
  `EstimatedShipDate` datetime DEFAULT NULL,
  `joSchedDetailID` int(11) DEFAULT NULL,
  PRIMARY KEY (`vePODetailID`),
  KEY `vePODetailID` (`vePODetailID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `vePOID` (`vePOID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vepodetail`
--

/*!40000 ALTER TABLE `vepodetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `vepodetail` ENABLE KEYS */;


--
-- Definition of table `vepodetailworkfile`
--

DROP TABLE IF EXISTS `vepodetailworkfile`;
CREATE TABLE `vepodetailworkfile` (
  `vePODetailWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `vePODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityOrdered` decimal(19,4) DEFAULT '0.0000',
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  `QuantityBilled` decimal(19,4) DEFAULT '0.0000',
  `UnitCost` decimal(19,4) DEFAULT '0.0000',
  `PriceMultiplier` decimal(19,4) DEFAULT '0.0000',
  `Taxable` tinyint(1) DEFAULT '0',
  `EstimatedShipDate` datetime DEFAULT NULL,
  `joSchedDetailID` int(11) DEFAULT NULL,
  PRIMARY KEY (`vePODetailWorkFileID`),
  KEY `vePODetailWorkFileID` (`vePODetailWorkFileID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `vePODetailID` (`vePODetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vepodetailworkfile`
--

/*!40000 ALTER TABLE `vepodetailworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `vepodetailworkfile` ENABLE KEYS */;


--
-- Definition of table `vereceive`
--

DROP TABLE IF EXISTS `vereceive`;
CREATE TABLE `vereceive` (
  `veReceiveID` int(11) NOT NULL AUTO_INCREMENT,
  `rxMasterID` int(11) DEFAULT NULL,
  `vePOID` int(11) DEFAULT '0',
  `ReceiveDate` datetime DEFAULT NULL,
  PRIMARY KEY (`veReceiveID`),
  KEY `veReceiveID` (`veReceiveID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `vePOID` (`vePOID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vereceive`
--

/*!40000 ALTER TABLE `vereceive` DISABLE KEYS */;
/*!40000 ALTER TABLE `vereceive` ENABLE KEYS */;


--
-- Definition of table `vereceivedetail`
--

DROP TABLE IF EXISTS `vereceivedetail`;
CREATE TABLE `vereceivedetail` (
  `veReceiveDetailID` int(11) NOT NULL AUTO_INCREMENT,
  `veReceiveID` int(11) DEFAULT '0',
  `vePODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT NULL,
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`veReceiveDetailID`),
  KEY `veReceiveDetailID` (`veReceiveDetailID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `vePODetailID` (`vePODetailID`),
  KEY `veReceiveID` (`veReceiveID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vereceivedetail`
--

/*!40000 ALTER TABLE `vereceivedetail` DISABLE KEYS */;
/*!40000 ALTER TABLE `vereceivedetail` ENABLE KEYS */;


--
-- Definition of table `vereceiveworkfile`
--

DROP TABLE IF EXISTS `vereceiveworkfile`;
CREATE TABLE `vereceiveworkfile` (
  `veReceiveWorkFileID` int(11) NOT NULL AUTO_INCREMENT,
  `vePODetailID` int(11) DEFAULT '0',
  `prMasterID` int(11) DEFAULT '0',
  `Description` varchar(50) DEFAULT NULL,
  `Note` longtext,
  `QuantityReceived` decimal(19,4) DEFAULT '0.0000',
  PRIMARY KEY (`veReceiveWorkFileID`),
  KEY `veReceiveWorkFileID` (`veReceiveWorkFileID`),
  KEY `prMasterID` (`prMasterID`),
  KEY `vePODetailID` (`vePODetailID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `vereceiveworkfile`
--

/*!40000 ALTER TABLE `vereceiveworkfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `vereceiveworkfile` ENABLE KEYS */;


--
-- Definition of table `veshipvia`
--

DROP TABLE IF EXISTS `veshipvia`;
CREATE TABLE `veshipvia` (
  `veShipViaID` int(11) NOT NULL AUTO_INCREMENT,
  `Description` varchar(20) DEFAULT NULL,
  `InActive` tinyint(1) DEFAULT '0',
  `TrackURL` varchar(120) DEFAULT NULL,
  `TrackPrefix` varchar(120) DEFAULT NULL,
  `TrackSuffix` varchar(120) DEFAULT NULL,
  PRIMARY KEY (`veShipViaID`),
  KEY `veShipViaID` (`veShipViaID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `veshipvia`
--

/*!40000 ALTER TABLE `veshipvia` DISABLE KEYS */;
/*!40000 ALTER TABLE `veshipvia` ENABLE KEYS */;


--
-- Definition of table `webaccount`
--

DROP TABLE IF EXISTS `webaccount`;
CREATE TABLE `webaccount` (
  `WebAccountID` int(11) NOT NULL AUTO_INCREMENT,
  `LoginName` varchar(15) DEFAULT NULL,
  `LoginPassword` varchar(50) DEFAULT NULL,
  `CompanyName` varchar(40) DEFAULT NULL,
  `Approved` tinyint(1) DEFAULT '0',
  `rxMasterID` int(11) DEFAULT '0',
  PRIMARY KEY (`WebAccountID`),
  KEY `rxMasterID` (`rxMasterID`),
  KEY `WebAccountID` (`WebAccountID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `webaccount`
--

/*!40000 ALTER TABLE `webaccount` DISABLE KEYS */;
/*!40000 ALTER TABLE `webaccount` ENABLE KEYS */;


--
-- Definition of table `workfile`
--

DROP TABLE IF EXISTS `workfile`;
CREATE TABLE `workfile` (
  `fldKey` int(11) NOT NULL AUTO_INCREMENT,
  `fldText` varchar(50) DEFAULT NULL,
  `fldBool` tinyint(1) NOT NULL DEFAULT '0',
  `fldMoney` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `fldInteger` int(11) NOT NULL DEFAULT '0',
  `fldDate` datetime NOT NULL,
  PRIMARY KEY (`fldKey`),
  KEY `fldKey` (`fldKey`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `workfile`
--

/*!40000 ALTER TABLE `workfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `workfile` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
