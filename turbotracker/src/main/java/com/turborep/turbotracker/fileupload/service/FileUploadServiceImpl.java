package com.turborep.turbotracker.fileupload.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.fileupload.exception.FileUploadException;
import com.turborep.turbotracker.job.dao.Jobidder;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.vendor.dao.Vepodetail;

@Service("fileUploadService")
@Transactional
public class FileUploadServiceImpl implements FileUploadService {
	
	Logger itsLogger = Logger.getLogger(FileUploadServiceImpl.class);

	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Override
	public String insertExcelData(InputStream theInputStream, int vePOId) throws FileUploadException {
		XSSFWorkbook workbook = null;
		Session aVePOSession = null;
		try {
			workbook = new XSSFWorkbook(theInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			Vepodetail aVepodetail = null;
			
			List<Vepodetail> aPODetailList = new ArrayList<Vepodetail>();
			//{0=Product No, 1=Description, 2=Qty, 3=Cost Each., 4=Mult., 5=Tax}
			int count = 0;
			while(rowIterator.hasNext()) {
				aVepodetail = new Vepodetail();
				Row row = rowIterator.next();
				//Cell prodNo = row.getCell(0);
				Cell prodDesc = row.getCell(1);
				Cell qty = row.getCell(2);
				Cell costEach = row.getCell(3);
				Cell aMult = row.getCell(4);
				Cell tax = row.getCell(5);
				Cell itemCode = row.getCell(0);
				aVepodetail.setVePoid(vePOId);
				if(count > 0) {
					aVepodetail.setDescription(prodDesc.toString());
					aVepodetail.setQuantityOrdered(BigDecimal.valueOf(Double.valueOf(qty.toString())));
					aVepodetail.setUnitCost(BigDecimal.valueOf(Double.valueOf(costEach.toString())));
					aVepodetail.setPriceMultiplier(BigDecimal.valueOf(Double.valueOf(aMult.toString())));
					aVepodetail.setTaxable(tax.getBooleanCellValue());
					String productWhereClause = "WHERE ItemCode = '"+ itemCode + "' AND Description = '" + prodDesc + "'";
					int prmasterID = productService.getProducts(productWhereClause).getPrMasterId();
					aVepodetail.setPrMasterId(prmasterID);
					aPODetailList.add(aVepodetail);					
				}
				count++;
		       /*  
		        //For each row, iterate through each columns
		        Iterator<Cell> cellIterator = row.cellIterator();
		        while(cellIterator.hasNext()) {
		             
		            Cell cell = cellIterator.next();
		             
		            switch(cell.getCellType()) {
		                case Cell.CELL_TYPE_BOOLEAN:
		                    System.out.print(cell.getBooleanCellValue() + "\t\t");
		                    break;
		                case Cell.CELL_TYPE_NUMERIC:
		                    System.out.print(cell.getNumericCellValue() + "\t\t");
		                    break;
		                case Cell.CELL_TYPE_STRING:
		                    System.out.print(cell.getStringCellValue() + "\t\t");
		                    break;
		            }
		        }*/
		        System.out.println("");
		    }
			aVePOSession = itsSessionFactory.openSession();
			Transaction aTransaction;
			Integer aVePOdetailID = 0;
			Iterator<Vepodetail> itr = aPODetailList.iterator();
			aTransaction = aVePOSession.beginTransaction();
			aTransaction.begin();
			while(itr.hasNext()) {
				Vepodetail aVPOdetail = (Vepodetail) itr.next();
				aVePOdetailID = (Integer) aVePOSession.save(aVPOdetail);
				aVPOdetail.setPosistion(aVePOdetailID);
				aVePOSession.update(aVepodetail);
				aTransaction.commit();
				aVePOSession.flush();
				aVePOSession.clear();
			}  
			//aTransaction.commit();
		} catch (IOException e) {
			itsLogger.error(e.getMessage(), e);
			throw new FileUploadException(e.getMessage(), e);
		} catch (ProductException e) {
			itsLogger.error(e.getMessage(), e);
			throw new FileUploadException(e.getMessage(), e);
		} finally {
			aVePOSession.flush();
			aVePOSession.close();
		}
		return "Success";
	}
	
	@Override
	public String uploadCompanyLogo(Blob blob) throws FileUploadException {
		Session aSession = null;
		Transaction aTransaction = null;
		try {
			aSession = itsSessionFactory.openSession();
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			TsUserSetting aSetting = (TsUserSetting) aSession.get(TsUserSetting.class, 1);
			aSetting.setCompanyLogo(blob);
			aSession.update(aSetting);
			aTransaction.commit();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new FileUploadException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return "Success";
	}
}
