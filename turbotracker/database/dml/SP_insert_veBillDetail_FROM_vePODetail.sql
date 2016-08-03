DELIMITER $$
DROP PROCEDURE IF EXISTS veBilldetail_Insert;  
$$

CREATE PROCEDURE `veBilldetail_Insert` (IN invePOID INT, IN inveBillID INT, OUT success VARCHAR(20))  
BEGIN 

INSERT veBillDetail(veBillID,vePODetailID,prMasterID,Description,Note,QuantityBilled,UnitCost,PriceMultiplier,FreightCost)
	SELECT inveBillID,vePODetailID,prMasterID,Description,Note,QuantityBilled,UnitCost,PriceMultiplier,0.00
FROM vePODetail WHERE vePOID = invePOID;

SET success = 'Success' ;
END$$
DELIMITER ;