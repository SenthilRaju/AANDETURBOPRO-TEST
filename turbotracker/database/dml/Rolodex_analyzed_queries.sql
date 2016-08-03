--/** factory list **/

SELECT * FROM VeFactory WHERE rxMasterId = 6870

--Remove * and mention the columns that you need-------------------------------

--/** Co-Tax Territory **/

SELECT county,state From coTaxTerritory where coTaxTerritoryId= 11

--/** rxContact list **/

SELECT * FROM Rxcontact WHERE rxMasterID = '6870'

--Remove * and mention the columns that you need-------------------------------



--/** Copy Quotes **/

SELECT * FROM joQuoteDetail where joQuoteHeaderID= '23912' ORDER BY Position

--change the query-----------------------------------------------------
SELECT joQuoteDetailID,
joQuoteHeaderID,
Product,
ProductNote,
ItemQuantity,
Paragraph,
rxManufacturerID,
Price,
InlineNote,
Cost,
veFactoryID,
Percentage
FROM joQuoteDetail where joQuoteHeaderID= '23912' ORDER BY Position;
--------------------------------------------------------------------------

SELECT Description FROM veFactory WHERE veFactoryID ='1035'

--/** Cancel Quote **/

SELECT * FROM joQuoteDetail where joQuoteHeaderID = '35018'

--change the query-----------------------------------------------------

SELECT joQuoteHeaderID FROM joQuoteDetail where joQuoteHeaderID = '35018'

--------------------------------------------------------------------------

--/** Sales Page Quories **/

SELECT  joMaster.BookedDate as date,  
        joMaster.JobNumber as number,  
		joMaster.Description as name,  
		joMaster.ContractAmount as amount,  
		rxMaster.Name as fname,  
		rxMaster.Phone1 as phone,  
		tsUserLogin.FullName as name1,  
		rxMaster.FirstName as name2 
FROM joMaster  
LEFT JOIN rxMaster ON rxMaster.rxMasterId = joMaster.rxCategory2 = joMaster.cuAssignmentID2  
INNER JOIN tsUserLogin ON tsUserLogin.UserLoginID = joMaster.cuAssignmentID0  
WHERE joMaster.SubmittalSent ='0'  AND joMaster.JobStatus = '3' AND joMaster.cuAssignmentID0 = '4'  
ORDER BY joMaster.BookedDate asc

--please check this query it is having two comparison on single JOIN-----------------------------------------------------