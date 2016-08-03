/* Create Quote template property Table*/


CREATE TABLE `joQuoteTemplateProperties` (
  `joQuotePropertiesID` int(11) NOT NULL AUTO_INCREMENT,
  `DisplayQuantity` tinyint(2) NOT NULL DEFAULT '0',
  `DisplayParagraph` tinyint(2) NOT NULL DEFAULT '0',
  `DisplayManufacturer` tinyint(2) NOT NULL DEFAULT '0',
  `DisplayCost` tinyint(2) NOT NULL DEFAULT '0',
  `DisplayPrice` tinyint(2) NOT NULL DEFAULT '0',
  `DisplayMult` tinyint(2) NOT NULL DEFAULT '0',
  `DisplaySpec` tinyint(2) NOT NULL DEFAULT '0',
  `PrintQuantity` tinyint(2) NOT NULL DEFAULT '0',
  `PrintParagraph` tinyint(2) NOT NULL DEFAULT '0',
  `PrintManufacturer` tinyint(2) NOT NULL DEFAULT '0',
  `PrintCost` tinyint(2) NOT NULL DEFAULT '0',
  `PrintPrice` tinyint(2) NOT NULL DEFAULT '0',
  `PrintMult` tinyint(2) NOT NULL DEFAULT '0',
  `PrintSpec` tinyint(2) NOT NULL DEFAULT '0',
  `UserLoginID` int(11) NOT NULL,
  `NotesFullWidth` tinyint(4) DEFAULT '0',
  `LineNumbers` tinyint(4) DEFAULT '0',
  `PrintTotal` tinyint(4) DEFAULT '0',
  `HidePrice` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`joQuotePropertiesID`),
  UNIQUE KEY `joQuotePropertiesID_UNIQUE` (`joQuotePropertiesID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
