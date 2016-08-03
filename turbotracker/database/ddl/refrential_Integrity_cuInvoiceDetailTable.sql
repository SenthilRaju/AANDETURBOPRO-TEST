ALTER TABLE `UnderwoodCompany`.`cuInvoiceDetail` 
DROP FOREIGN KEY `cuInvoiceDetail_FK00`;
ALTER TABLE `UnderwoodCompany`.`cuInvoiceDetail` 
ADD CONSTRAINT `cuInvoiceDetail_FK00`
  FOREIGN KEY (`cuInvoiceID`)
  REFERENCES `UnderwoodCompany`.`cuInvoice` (`cuInvoiceID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;



ALTER TABLE `UnderwoodCompany`.`cuSODetail` 
DROP FOREIGN KEY `cuSODetail_FK00`;
ALTER TABLE `UnderwoodCompany`.`cuSODetail` 
ADD CONSTRAINT `cuSODetail_FK00`
  FOREIGN KEY (`cuSOID`)
  REFERENCES `UnderwoodCompany`.`cuSO` (`cuSOID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


ALTER TABLE `UnderwoodCompany`.`vePODetail` 
DROP FOREIGN KEY `vePODetail_FK01`;
ALTER TABLE `UnderwoodCompany`.`vePODetail` 
ADD CONSTRAINT `vePODetail_FK01`
  FOREIGN KEY (`vePOID`)
  REFERENCES `UnderwoodCompany`.`vePO` (`vePOID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


ALTER TABLE `UnderwoodCompany`.`veBillDetail` 
DROP FOREIGN KEY `veBillDetail_FK01`;
ALTER TABLE `UnderwoodCompany`.`veBillDetail` 
ADD CONSTRAINT `veBillDetail_FK01`
  FOREIGN KEY (`veBillID`)
  REFERENCES `UnderwoodCompany`.`veBill` (`veBillID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTIOasdN;
  
  
  --------------For BacheCompany------------------
  
  ALTER TABLE `BacheCompany`.`cuSODetail` 
ADD CONSTRAINT `fk_cuSODetail_1`
  FOREIGN KEY (`cuSOID`)
  REFERENCES `BacheCompany`.`cuSO` (`cuSOID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
  ALTER TABLE `BacheCompany`.`cuSODetail` 
DROP FOREIGN KEY `fk_cuSODetail_1`;
ALTER TABLE `BacheCompany`.`cuSODetail` 
ADD CONSTRAINT `fk_cuSODetail_1`
  FOREIGN KEY (`cuSOID`)
  REFERENCES `BacheCompany`.`cuSO` (`cuSOID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


ALTER TABLE `BacheCompany`.`vePODetail` 
ADD CONSTRAINT `fk_vePODetail_1`
  FOREIGN KEY (`vePOID`)
  REFERENCES `BacheCompany`.`vePO` (`vePOID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `BacheCompany`.`vePODetail` 
DROP FOREIGN KEY `fk_vePODetail_1`;
ALTER TABLE `BacheCompany`.`vePODetail` 
ADD CONSTRAINT `fk_vePODetail_1`
  FOREIGN KEY (`vePOID`)
  REFERENCES `BacheCompany`.`vePO` (`vePOID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

  
  DELETE FROM veBillDetail WHERE veBillID = 64908;
  
ALTER TABLE `BacheCompany`.`veBillDetail` 
ADD CONSTRAINT `fk_veBillDetail_1`
  FOREIGN KEY (`veBillID`)
  REFERENCES `BacheCompany`.`veBill` (`veBillID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

  DELETE from cuInvoiceDetail where cuInvoiceID = 135293277; 

ALTER TABLE `BacheCompany`.`cuInvoiceDetail` 
ADD CONSTRAINT `fk_cuInvoiceDetail_1`
  FOREIGN KEY (`cuInvoiceID`)
  REFERENCES `BacheCompany`.`cuInvoice` (`cuInvoiceID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  
  ALTER TABLE `BacheCompany`.`cuInvoiceDetail` 
DROP FOREIGN KEY `fk_cuInvoiceDetail_1`;
ALTER TABLE `BacheCompany`.`cuInvoiceDetail` 
ADD CONSTRAINT `fk_cuInvoiceDetail_1`
  FOREIGN KEY (`cuInvoiceID`)
  REFERENCES `BacheCompany`.`cuInvoice` (`cuInvoiceID`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;

