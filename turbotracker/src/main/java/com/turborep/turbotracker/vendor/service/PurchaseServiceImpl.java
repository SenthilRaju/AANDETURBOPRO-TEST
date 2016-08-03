package com.turborep.turbotracker.vendor.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.job.dao.JoInvoiceCost;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JobPurchaseOrderBean;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.WarehouseTransferBean;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.exception.VendorException;

@Service("purchaseService")
@Transactional
public class PurchaseServiceImpl implements PurchaseService {

	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	@Resource(name = "customerService")
	private CustomerService itscustomerService;
	
	Logger itsLogger = Logger.getLogger(PurchaseServiceImpl.class);
	
	@Override
	public List<PurchaseOrdersBean> getAllPOsList(int from, int to,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException {
		itsLogger.debug("Retrieving all POs");
		
		StringBuilder aPOSelectQry = new StringBuilder("SELECT vp.vePOID, vp.CreatedOn, vp.joReleaseID, vp.rxVendorID, vp.PONumber, rm.Name, vp.subtotal, vp.Tag,mas.Description ")
												.append("FROM vePO vp "
														+ "LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID "
														+ "LEFT JOIN joRelease jo ON jo.joReleaseID =  vp.joReleaseID "
														+ "LEFT JOIN joMaster AS mas ON jo.joMasterID = mas.joMasterID ");
		

		
		//aPOSelectQry.append("WHERE vp.vePOID LIKE '%'");
		
		if(searchData!=null && !searchData.equals("")){
			aPOSelectQry.append(" WHERE (vp.PONumber LIKE '%"+searchData+"%' OR vp.joReleaseID LIKE '%"+searchData+"%' " +
					" OR rm.Name LIKE '%"+searchData+"%' " +
					" OR vp.subtotal LIKE '%"+searchData+"%' )");
		}
				
		if(!startDate.equals("")&& !endDate.equals("")){
			if(aPOSelectQry.indexOf("WHERE")>0)
				aPOSelectQry.append(" and vp.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
			else
				aPOSelectQry.append(" WHERE vp.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}else if(!startDate.equals("") && endDate.equals("")){
			if(aPOSelectQry.indexOf("WHERE")>0)
				aPOSelectQry.append(" and vp.CreatedOn >='"+startDate+" 00:00:00'");
			else
				aPOSelectQry.append(" WHERE vp.CreatedOn >='"+startDate+" 00:00:00'");
		}else if(!endDate.equals("") && startDate.equals("")){
			if(aPOSelectQry.indexOf("WHERE")>0)
				aPOSelectQry.append(" and vp.CreatedOn<='"+endDate+" 23:59:59'");
			else
				aPOSelectQry.append(" WHERE vp.CreatedOn<='"+endDate+" 23:59:59'");
		}
		
		String orderByIndex="";
		String orderBy="DESC";
		
		if(sortIndex.equals("vePOID")){
			orderByIndex="vp.vePOID";
		}else if(sortIndex.equals("createdOn")){
			orderByIndex="vp.CreatedOn ";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="vp.PONumber";
		}else if(sortIndex.equals("vendorName")){
			orderByIndex="rm.Name";
		}else if(sortIndex.equals("subtotal")){
			orderByIndex="vp.subtotal";
		}else{
			orderByIndex="vp.vePOID";
		}
		
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		aPOSelectQry.append(" ORDER BY "+orderByIndex+" "+orderBy+"  LIMIT " + from + ", " + to);
		
		Session aSession = null;
		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}else
					aPurchaseOrdersBean.setCreatedOn((Date)aObj[1] ); //(Date) aObj[1]			
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);	
				
				if(aPurchaseOrdersBean.getJoReleaseID() == null || aPurchaseOrdersBean.getJoReleaseID() == 1){
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName((String) aObj[7]);
				}else{
				//	String jobName = "SELECT mas.Description FROM joMaster AS mas LEFT JOIN joRelease jo ON jo.joMasterID = mas.joMasterID WHERE jo.joReleaseID ="+ aPurchaseOrdersBean.getJoReleaseID();
					//jobSession = itsSessionFactory.openSession();
				//	System.out.println("  PurchaseServiceImpl    List<PurchaseOrdersBean> getAllPOsList() query string ->> jobName :: "+jobName);
				//	Query aQuery1 = aSession.createSQLQuery(jobName.toString());
//					String sjobname = "";
//					List li=aQuery1.list();
//					if(li.size() > 0)
//						sjobname = (String)li.get(0);
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName((String) aObj[8]);
				}
					//SELECT description FROM jomaster AS mas LEFT JOIN jorelease jo ON jo.jomasterID = mas.jomasterID WHERE jo.joreleaseID = 66186
					aPurchaseOrdersBean.setRxVendorID((Integer)aObj[3]);		
					aPurchaseOrdersBean.setPONumber((String) aObj[4]);			
					aPurchaseOrdersBean.setVendorName((String) aObj[5]);
					aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[6]);
				
					aQueryList.add(aPurchaseOrdersBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
			
		} finally {
			aSession.flush();
			aSession.close();
			aPOSelectQry = null;
			orderByIndex=null;
			orderBy=null;
			aPurchaseOrdersBean = null;
		}
		return  aQueryList;
	}

	public Integer getRecordsCount() throws VendorException{
		String aJobCountStr = "SELECT COUNT(vePOID) AS count FROM vePO";
	//	String aJobCountStr = "SELECT COUNT(vp.vePOID) AS COUNT FROM vePO vp LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID  WHERE receiveddate <>''";
		
		//System.out.println("PurchaseServiceImpl Integer getRecordsCount() query string :: "+aJobCountStr);
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	public Integer getRecordsCountforReceiveInvenory() throws VendorException{
		//String aJobCountStr = "SELECT COUNT(vePOID) AS count FROM vePO";
		
		String aJobCountStr = "SELECT COUNT(vp.vePOID) AS COUNT FROM vePO vp WHERE receiveddate <>''";
		
		//System.out.println("PurchaseServiceImpl Integer getRecordsCount() query string :: "+aJobCountStr);
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	
	@Override
	public String getManufactureName(Integer theRxMasterId) throws VendorException {
		Session aSession = null;
		String aManufactureName = null;
		Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(theRxMasterId != null)
			{
				aQuery = aSession.createQuery("SELECT r.name FROM Rxmaster r WHERE r.rxMasterId= :theRxMasterId").setParameter("theRxMasterId", theRxMasterId);
				System.out.println("PurchaseServiceImpl String getManufactureName() query strng :: SELECT r.name FROM Rxmaster r WHERE r.rxMasterId= "+theRxMasterId);
				if(aQuery.list().size() > 0)
					aManufactureName	=	(String)aQuery.uniqueResult();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return aManufactureName;
	}	
	
	
	@Override
	public JobPurchaseOrderBean getPurchaseDetails(Integer theVepoID) throws VendorException {
		String aPurcheaseDeatils = "SELECT ve.vePOID," +
											" ve.CreatedByID," +
											" ve.joReleaseID," +
											" ve.veShipViaID," +
											" ve.veFreightChargesID," +
											" ve.OrderedByID," +
											" ve.PONumber," +
											" ve.CustomerPONumber," +
											" ve.OrderDate," +
											" ve.DateWanted," +
											" ve.Tag," +
											" ve.SpecialInstructions," +
											" ve.SubTotal," +
											" ve.TaxTotal," +
											" ve.Freight," +
											" ve.TaxRate," +
											" ve.rxVendorContactID," +
											" ve.veFactoryID, " +
											" ve.billToIndex," +
											" ve.shipToIndex," +
											" ve.emailTimeStamp," +
											" ve.wantedOnOrBefore," +
											" ve.ShipToMode," +
											 " ve.qbPO," +
											 " ve.rxBillToID," +
											 " ve.rxShipToID," +
											 " ve.rxBillToAddressId," +
											 " ve.rxShipToAddressId," +
											 " ve.rxVendorId," +
											 " ve.TransactionStatus," +
											 " ve.prWarehouseId" +
											" from vePO ve where vePOID ="+theVepoID + ";";
		Session aSession=null;
		JobPurchaseOrderBean aJobPurchaseOrderBean = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPurcheaseDeatils);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext()) {
				aJobPurchaseOrderBean = new JobPurchaseOrderBean();
				Object[] aObj = (Object[])aIterator.next();
				aJobPurchaseOrderBean.setVePoid((Integer)aObj[0]);	
				if((Integer)aObj[1] != null){
					aJobPurchaseOrderBean.setCreatedById((Integer)aObj[1]);
				}
				aJobPurchaseOrderBean.setJoReleaseId((Integer)aObj[2]);
				aJobPurchaseOrderBean.setVeShipViaId((Integer)aObj[3]);
				aJobPurchaseOrderBean.setVeFreightChargesId((Integer)aObj[4]);
				if(aObj[5] != null && (Integer)aObj[5] != null){
					aJobPurchaseOrderBean.setOrderedById((Integer)aObj[5]);
				}
				
				aJobPurchaseOrderBean.setPonumber((String)aObj[6]);
				aJobPurchaseOrderBean.setCustomerPonumber((String)aObj[7]);
				aJobPurchaseOrderBean.setOrderDate((Timestamp)aObj[8]);
				aJobPurchaseOrderBean.setDateWanted((String)aObj[9]);
				aJobPurchaseOrderBean.setTag((String)aObj[10]);
				aJobPurchaseOrderBean.setSpecialInstructions((String)aObj[11]);
				aJobPurchaseOrderBean.setSubtotal((BigDecimal)aObj[12]);
				aJobPurchaseOrderBean.setTaxTotal((BigDecimal)aObj[13]);
				aJobPurchaseOrderBean.setFreight((BigDecimal)aObj[14]);
				aJobPurchaseOrderBean.setTaxRate((BigDecimal)aObj[15]);
				aJobPurchaseOrderBean.setRxVendorContactId((Integer)aObj[16]);
				aJobPurchaseOrderBean.setVeFactoryId((Short)aObj[17]);
				aJobPurchaseOrderBean.setBillTo((Integer) aObj[18]);
				aJobPurchaseOrderBean.setShipTo((Integer) aObj[19]);
				aJobPurchaseOrderBean.setQbPO((String) aObj[23]);
				aJobPurchaseOrderBean.setRxBillToID((Integer) aObj[24]);
				aJobPurchaseOrderBean.setRxShipToId((Integer) aObj[25]);
				aJobPurchaseOrderBean.setRxBillToAddressId((Integer) aObj[26]);
				aJobPurchaseOrderBean.setRxShipToAddressId((Integer) aObj[27]);
				aJobPurchaseOrderBean.setRxVendorId((Integer) aObj[28]);
				aJobPurchaseOrderBean.setPrWarehouseId((Integer) aObj[30]);
				short transStatus =  aObj[29]!=null?(Short)aObj[29]:1;
				aJobPurchaseOrderBean.setTransactionStatus(Integer.valueOf(transStatus));
				SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
				if(aObj[20] != null){
					String aDateFormat = fromUser.format(aObj[20]);
					aJobPurchaseOrderBean.setEmailTimeStamp(aDateFormat);
				}
				aJobPurchaseOrderBean.setWantedOnOrBefore((Integer) aObj[21]);
				aJobPurchaseOrderBean.setShipToMode((Short) aObj[22]);
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aPurcheaseDeatils = null;
		}
		return aJobPurchaseOrderBean;
	}
	
	@Override
	public Integer getRxCustomerID(Integer joReleaseID) throws VendorException {
		Session aSession = null;
		Integer rxCustomerID = null;
		String sQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			sQuery =  "SELECT joMasterID FROM joMaster WHERE joMasterID = (SELECT joMasterID FROM joRelease WHERE joReleaseID="+joReleaseID+")";
			Query aQuery1 = aSession.createSQLQuery(sQuery.toString());
			
			@SuppressWarnings("unchecked")
			List<Integer> intList = aQuery1.list();
			if(intList.size() > 0)
				rxCustomerID = intList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			sQuery = null;
		}
		return rxCustomerID;
	}
	
	@Override
	public Integer getRxMasterID(Integer aVePOId) throws VendorException {
		String aQryStr = "SELECT c.rxVendorId FROM Vepo c WHERE  c.vePoid= :aVePOId";
		Session aSession = null;
		Integer aRxVendorId = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQryStr).setParameter("aVePOId", aVePOId);
			if(aQuery.list().size() > 0)
			aRxVendorId	=	(Integer)aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQryStr = null;
		}
		return aRxVendorId;
	}
	
	@Override
	public Integer getVendorRxMasterID(Integer aVePOId) throws VendorException {
		String aQryStr = "SELECT c.rxShipToId FROM Vepo c WHERE  c.vePoid= :aVePOId";
		Session aSession = null;
		Integer aRxVendorId = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQryStr).setParameter("aVePOId", aVePOId);
			if(aQuery.list().size() > 0)
			aRxVendorId	=	(Integer)aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQryStr = null;
		}
		return aRxVendorId;
	}
	
	@Override
	public String getJobNumberFromVePO(Integer aVePOId) throws VendorException {
		String aQryStr = "SELECT jom.JobNumber FROM joMaster jom WHERE "
				+ "jom.joMasterID =(SELECT jor.joMasterID FROM joRelease jor WHERE "
				+ "jor.joReleaseID =(SELECT c.joReleaseID FROM vePO c WHERE  c.vePoid= "+aVePOId+"))";

		Session aSession = null;
		String jobNumber = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQryStr);
			List<?> aList = aQuery.list();
			if(aList.size() > 0)
				jobNumber	=	(String)aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQryStr = null;
		}
		return jobNumber;
	}
	
	@Override
	public List<Rxcontact> getContactList(Integer theRxMasterId) throws VendorException {
		String aQry = "FROM Rxcontact WHERE rxMasterId = :theRxMasterId";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("theRxMasterId", theRxMasterId);
			return aQuery.list();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
	}
	
	@Override
	public List<Rxcontact> getContactList() throws VendorException {
		String aQry = "FROM Rxcontact";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry);
			return aQuery.list();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
	}

	@Override
	public Rxaddress getRxaddress(Integer theRxMasterId) throws VendorException {
		String aQry = "FROM Rxaddress WHERE rxAddressId = :theRxMasterId";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("theRxMasterId", theRxMasterId);
			System.out.println("PurchaseServiceImpl Rxaddress getRxaddress() query string :: "+aQry);
//			if(aQuery.list().size() > 0)
//			return (Rxaddress) aQuery.list().get(0);
			List<?> list = aQuery.list();
			if(list.size() > 0)
				return (Rxaddress)list.get(0);
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
	}
	
	/* Added by CGS */
	@Override
	public List<VeFactory> getManufactureNames(Integer rxMasterID) throws VendorException {

		String aQry = "FROM VeFactory WHERE rxMasterId = :rxMasterID";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("rxMasterID", rxMasterID);
//			if(aQuery.list().size() > 0)
//			return aQuery.list();
			List<?> list = aQuery.list();
			if(list.size() > 0)
				return (List<VeFactory>)list;
			else
				return new ArrayList<VeFactory>();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	/* Added by CGS */
	@Override
	public List<Rxcontact> getATTNName(Integer rxMasterID) throws VendorException {

		String aQry = "FROM Rxcontact WHERE rxMasterId = :rxMasterID";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("rxMasterID", rxMasterID);
//			if(aQuery.list().size() > 0)
//			return aQuery.list();
			List<?> list = aQuery.list();
			if(list.size() > 0)
				return (List<Rxcontact>) list;
			else
				return new ArrayList<Rxcontact>();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		
	}
	
	/* Added by CGS */
	@Override
	public List<Rxmaster> getOrderedByName() throws VendorException {

		String aQry = "FROM Rxmaster r WHERE r.isEmployee= :employee AND r.inActive=:active";
		Session aSession=null;
		boolean employee = true;
		boolean active = false;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("employee", employee).setParameter("active", active);
			List<?> list = aQuery.list();
//			if(aQuery.list().size() > 0)
//			return aQuery.list();
			if(list.size() > 0)
				return (List<Rxmaster>)list;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	/* Added by CGS */
	@Override
	public TsUserSetting getBilltoAddress() throws VendorException {

		String aQry = "SELECT POBillTo_Name,POBillTo_Address1,POBillTo_Address2,POBillTo_City,POBillTo_State,POBillTo_Zip FROM sysInfo WHERE sysInfoID=1";
		Session aSession=null;
		TsUserSetting sBilltoAddress = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			//Iterator<?> aIterator = aQuery.list().iterator();
			List<?> list = aQuery.list();
			Iterator<?> aIterator = list.iterator();
			while(aIterator.hasNext())
			{
				sBilltoAddress = new TsUserSetting();
				Object[] aObj = (Object[])aIterator.next();
				sBilltoAddress.setBillToDescription((String) aObj[0]);
				sBilltoAddress.setBillToAddress1((String) aObj[1]);
				sBilltoAddress.setBillToAddress2((String) aObj[2]);
				sBilltoAddress.setBillToCity((String) aObj[3]);
				sBilltoAddress.setBillToState((String) aObj[4]);
				sBilltoAddress.setBillToZip((String) aObj[5]);
			}
//			if(aQuery.list().size() > 0)
//			return sBilltoAddress;
			if(list.size() > 0)
				return sBilltoAddress;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	/* Added by CGS */
	/*@Override
	public List<TsUserSetting> getBilltoShiptoCustomerAddress() throws VendorException {

		Rxmaster obj = new Rxmaster();		
		String aQry = "select * from prwarehouse";
		Session aSession=null;
		boolean employee = true;
		TsUserSetting sBilltoAddress = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				sBilltoAddress = new TsUserSetting();
				Object[] aObj = (Object[])aIterator.next();
				sBilltoAddress.setBillToDescription((String) aObj[0]);
				sBilltoAddress.setBillToAddress1((String) aObj[1]);
				sBilltoAddress.setBillToAddress2((String) aObj[2]);
				sBilltoAddress.setBillToCity((String) aObj[3]);
				sBilltoAddress.setBillToState((String) aObj[4]);
				sBilltoAddress.setBillToZip((String) aObj[5]);
			}
			if(aQuery.list().size() > 0)
			return sBilltoAddress;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}	
		
	}*/
	/* Added by CGS */
	@Override
	public List<Rxaddress> getCustomerAddress(Integer rxMasterID) throws VendorException {
		
		//String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,cotax.county,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxmaster ON rxmaster.rxMasterID = address.rxMasterID JOIN CoTaxTerritory AS cotax ON cotax.coTaxTerritoryId = address.coTaxTerritoryId WHERE address.rxMasterID = "+rxMasterID;
		
		String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,address.city,address.State,address.Zip,"
				+ "address.rxAddressId,cotax.coTaxTerritoryID, address.IsBillTo,address.IsShipTo,address.IsMailing FROM rxMaster rxmaster "
				+ "LEFT JOIN rxAddress address ON address.rxMasterID = rxmaster.rxMasterID "
				+ "LEFT JOIN cuMaster cumaster ON cumaster.cuMasterID = rxmaster.rxMasterID "
				+ "LEFT JOIN coTaxTerritory cotax ON address.coTaxTerritoryID = cotax.coTaxTerritoryID "
				+ "WHERE cumaster.cuMasterID = address.rxMasterID AND rxmaster.Name IS NOT NULL AND address.rxMasterID ="+rxMasterID;
				/*+ "SELECT rxmaster.Name,address.Address1,address.Address2,address.city,address.State,address.Zip,"
				+ "address.rxAddressId,cotax.coTaxTerritoryID, address.IsShipTo,address.IsBillTo FROM cuMaster cumaster, rxMaster rxmaster, "
				+ "rxAddress address, coTaxTerritory cotax WHERE cumaster.cuMasterID = rxmaster.rxMasterID AND"+
		" cumaster.cuMasterID = address.rxMasterID AND rxmaster.rxMasterID = address.rxMasterID AND address.coTaxTerritoryID = cotax.coTaxTerritoryID AND"+
		" rxmaster.Name IS NOT NULL AND address.rxMasterID ="+rxMasterID;*/
		Session aSession=null;
		Rxaddress objRxaddress = null;
		List<Rxaddress> alRxaddress = new ArrayList<Rxaddress>();
		Query aQuery = null;
		List<?> list = null;
		Iterator<?> aIterator = null;
		itsLogger.info("RxDetails Query:"+aQry);
		try{
			aSession=itsSessionFactory.openSession();
			aQuery = aSession.createSQLQuery(aQry);
			//Iterator<?> aIterator = aQuery.list().iterator();
			list = aQuery.list();
			aIterator = list.iterator();
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setName((String) aObj[0]);
				objRxaddress.setAddress1((String) aObj[1]);
				objRxaddress.setAddress2((String) aObj[2]);
				objRxaddress.setCity((String) aObj[3]);
				objRxaddress.setState((String) aObj[4]);
				objRxaddress.setZip((String) aObj[5]);
				objRxaddress.setRxAddressId((Integer) aObj[6]);
				
				if(null != aObj[7]){
					objRxaddress.setCoTaxTerritoryId((Integer) aObj[7]);
				}
				Byte billTo= ((Byte) aObj[8]);
				Byte shipTo= ((Byte) aObj[9]);
				Byte ismail= ((Byte) aObj[10]);
				if(billTo == 1){
					objRxaddress.setIsBillTo(true);
				}else{
					objRxaddress.setIsBillTo(false);
				}
				if(shipTo == 1){
					objRxaddress.setIsShipTo(true);
				}else{
					objRxaddress.setIsShipTo(false);
				}
				if(ismail == 1){
					objRxaddress.setIsMailing(true);
				}else{
					objRxaddress.setIsMailing(false);
				}
				
				alRxaddress.add(objRxaddress);
			}
//			if(aQuery.list().size() > 0)
//			return alRxaddress;
			if(list.size() > 0)
				return alRxaddress;
			else{
				aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,address.city,address.State,address.Zip,address.rxAddressId,address.coTaxTerritoryID,address.IsMailing FROM rxAddress AS address JOIN rxMaster AS rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+rxMasterID+ " and IsDefault = 1";
				aQuery = aSession.createSQLQuery(aQry);
				aIterator = list.iterator();
				while(aIterator.hasNext())
				{
					objRxaddress = new Rxaddress();
					Object[] aObj = (Object[])aIterator.next();
					objRxaddress.setName((String) aObj[0]);
					objRxaddress.setAddress1((String) aObj[1]);
					objRxaddress.setAddress2((String) aObj[2]);
					objRxaddress.setCity((String) aObj[3]);
					objRxaddress.setState((String) aObj[4]);
					objRxaddress.setZip((String) aObj[5]);
					objRxaddress.setRxAddressId((Integer) aObj[6]);
					if(null != aObj[7]){
						objRxaddress.setCoTaxTerritoryId((Integer) aObj[7]);
					}
					objRxaddress.setIsMailing((Boolean)aObj[8]);
					alRxaddress.add(objRxaddress);
				}
				return alRxaddress;
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
			objRxaddress = null;
			aQuery = null;
			list = null;
			aIterator = null;
		}	
		
	}
	
	@Override
	public Rxaddress getCustomerAddressauto(Integer rxMasterID) throws VendorException {
		
		//String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,cotax.county,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxmaster ON rxmaster.rxMasterID = address.rxMasterID JOIN CoTaxTerritory AS cotax ON cotax.coTaxTerritoryId = address.coTaxTerritoryId WHERE address.rxMasterID = "+rxMasterID;
		String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,address.City,address.State,address.Zip,address.rxAddressId,cotax.coTaxTerritoryID FROM cuMaster cumaster, rxMaster rxmaster, rxAddress address, coTaxTerritory cotax WHERE cumaster.cuMasterID = rxmaster.rxMasterID AND"+
				" cumaster.cuMasterID = address.rxMasterID AND rxmaster.rxMasterID = address.rxMasterID AND address.coTaxTerritoryID = cotax.coTaxTerritoryID AND"+
				" rxmaster.Name IS NOT NULL AND address.rxMasterID ="+rxMasterID;
		Session aSession=null;
		Rxaddress objRxaddress = null;
	itsLogger.info("aQry"+aQry);
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			System.out.println("PurchaseServiceImpl  Rxaddress getCustomerAddressauto() query string :: "+aQry);
			//Iterator<?> aIterator = aQuery.list().iterator();
			List<?> list = aQuery.list();
			Iterator<?> aIterator = list.iterator();
			
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setName((String) aObj[0]);
				objRxaddress.setAddress1((String) aObj[1]);
				objRxaddress.setAddress2((String) aObj[2]);
				objRxaddress.setCity((String) aObj[3]);
				objRxaddress.setState((String) aObj[4]);
				objRxaddress.setZip((String) aObj[5]);
				objRxaddress.setRxAddressId((Integer) aObj[6]);
				if(null != aObj[7])
					objRxaddress.setCoTaxTerritoryId((Integer) aObj[7]);
			}
//			if(aQuery.list().size() > 0)
//			return objRxaddress;
			if(list.size() > 0)
				return objRxaddress;
			else{
				aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,address.City,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxMaster AS rxmaster ON rxmaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+rxMasterID;
				aQuery = aSession.createSQLQuery(aQry);
				System.out.println("2->>>>>>> PurchaseServiceImpl  Rxaddress getCustomerAddressauto() query string :: "+aQry);
				aIterator = aQuery.list().iterator();
				while(aIterator.hasNext())
				{
					objRxaddress = new Rxaddress();
					Object[] aObj = (Object[])aIterator.next();
					objRxaddress.setName((String) aObj[0]);
					objRxaddress.setAddress1((String) aObj[1]);
					objRxaddress.setAddress2((String) aObj[2]);
					objRxaddress.setCity((String) aObj[3]);
					objRxaddress.setState((String) aObj[4]);
					objRxaddress.setZip((String) aObj[5]);
					objRxaddress.setRxAddressId((Integer) aObj[6]);
				}
				return objRxaddress;
			}
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	/* Added by CGS */
	@Override
	public List<Rxaddress> getVendorName(Integer rxMasterID) throws VendorException {
		
		String aQry = "SELECT * FROM rxMaster WHERE rxMasterID = "+rxMasterID;
		Session aSession=null;
		Rxaddress objRxaddress = null;
		List<Rxaddress> alRxaddress = new ArrayList<Rxaddress>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
//			Iterator<?> aIterator = aQuery.list().iterator();
			List<?> list  = aQuery.list();
			Iterator<?> aIterator = list.iterator();
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setRxMasterId((Integer)aObj[0]);
				objRxaddress.setName((String) aObj[10]);				
				alRxaddress.add(objRxaddress);
			}
//			if(aQuery.list().size() > 0)
//			return alRxaddress;
			if(list.size() > 0)
				return alRxaddress;
			else
				return new ArrayList<Rxaddress>();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	/* Newly added */
	@Override
	public List<Prmaster> getLineItems(Integer prMasterID) throws VendorException {
		/*Prmaster*/
		//SELECT prMasterID,ItemCode,Description,IsTaxable,PurchasingUnitMultiplier,NewPrice FROM prMaster
		
		String aQry = "SELECT prMasterId,itemCode,Description,isTaxable,POMult,lastCost,SalesPrice00 FROM prMaster WHERE prMasterId = "+prMasterID;
		Session aSession=null;
		List<Prmaster> alPrmaster = new ArrayList<Prmaster>();
		Prmaster objPrmaster = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				objPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				objPrmaster.setPrMasterId(Integer.valueOf(((Integer)aObj[0])));	
				objPrmaster.setItemCode((String) aObj[1]);	
				objPrmaster.setDescription((String) aObj[2]);	
				objPrmaster.setIsTaxable( new Byte((aObj[3]).toString()));	
				/*
				 * Edited By Simon on 22nd June
				 * Reason for changing : ID#567
				 */
				//objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				if(null == aObj[4])
				{
					/*String sPOMulti = (String)aObj[4];
					if(null == sPOMulti || "null".equalsIgnoreCase(sPOMulti) || "".equalsIgnoreCase(sPOMulti))
					{*/
						objPrmaster.setPOMult(new BigDecimal(0));
					/*}
					else
						objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));*/
				}
				else
				{
					objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				}
				if(null == aObj[5])
				{
					/*String sLastPrice = (String)aObj[5];
					if(null == sLastPrice || "null".equalsIgnoreCase(sLastPrice) || "".equalsIgnoreCase(sLastPrice))
					{*/
						objPrmaster.setLastCost(new BigDecimal(0));
					/*}
					else
						objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));*/
				}
				else
				{
					objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
				
				if(null == aObj[6])
				{
					/*String salesPrice00 = (String)aObj[6];
					if(null == salesPrice00 || "null".equalsIgnoreCase(salesPrice00) || "".equalsIgnoreCase(salesPrice00))
					{*/
						objPrmaster.setSalesPrice00(new BigDecimal(0));
					/*}
					else
						objPrmaster.setSalesPrice00(new BigDecimal(aObj[6].toString()));*/
				}
				else
				{
					objPrmaster.setSalesPrice00(new BigDecimal(aObj[6].toString()));
				}
					
				alPrmaster.add(objPrmaster);
			}
			//if(aQuery.list().size() > 0)
			return alPrmaster;
			/*else
				return null;*/
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}
		
	}
	
	@Override
	public List<Rxmaster> getRxMasterName(Integer prMasterID) throws VendorException {
		/*Rxmaster*/
		//SELECT prMasterID,ItemCode,Description,IsTaxable,PurchasingUnitMultiplier,NewPrice FROM prMaster
		
		String aQry = "SELECT rxMasterId,Name FROM rxMaster WHERE rxMasterId = "+prMasterID;
		Session aSession=null;
		List<Rxmaster> alPrmaster = new ArrayList<Rxmaster>();
		Rxmaster objPrmaster = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				objPrmaster = new Rxmaster();
				Object[] aObj = (Object[])aIterator.next();
				objPrmaster.setRxMasterId(Integer.valueOf(((Integer)aObj[0])));	
				objPrmaster.setName((String) aObj[1]);	
				
					
				alPrmaster.add(objPrmaster);
			}
			//if(aQuery.list().size() > 0)
			return alPrmaster;
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	@Override
	public List<Prmaster> getLineItemsSO(Integer prMasterID) throws VendorException {
		/*Prmaster*/
		//SELECT prMasterID,ItemCode,Description,IsTaxable,PurchasingUnitMultiplier,NewPrice FROM prMaster
		
		String aQry = "SELECT prMasterId,itemCode,Description,isTaxable,POMult,SalesPrice00,SOPopup FROM prMaster WHERE prMasterId = "+prMasterID;
		Session aSession=null;
		List<Prmaster> alPrmaster = new ArrayList<Prmaster>();
		Prmaster objPrmaster = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				objPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				objPrmaster.setPrMasterId(Integer.valueOf(((Integer)aObj[0])));	
				objPrmaster.setItemCode((String) aObj[1]);	
				objPrmaster.setDescription((String) aObj[2]);	
				objPrmaster.setIsTaxable( new Byte((aObj[3]).toString()));
				if(aObj[4]==null){
					aObj[4]=0;
				}
				objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				if(aObj[6]!=null){
					objPrmaster.setSOPopup((String) aObj[6]);	
				}else{
					objPrmaster.setSOPopup("");
				}
				
				if(null == aObj[5])
				{
//					String sLastPrice = (String)aObj[5];
//					if(null == sLastPrice || "null".equalsIgnoreCase(sLastPrice) || "".equalsIgnoreCase(sLastPrice))
//					{
						objPrmaster.setLastCost(new BigDecimal(0));
//					}
//					else
//						objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
				else
				{
					objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
					
				alPrmaster.add(objPrmaster);
			}
			//if(aQuery.list().size() > 0)
			return alPrmaster;
			/*else
				return null;*/
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry=null;
		}	
		
	}
	
	@Override
	public String getVendor(Integer theRxMasterId) throws VendorException {
		Session aSession = null;
		String aVendorName = null;
		Query aQuery = null;
		try {
			aSession = itsSessionFactory.openSession();
			if(theRxMasterId != null)
			{
				aQuery = aSession.createQuery("SELECT r.name FROM Rxmaster r WHERE r.rxMasterId= :theRxMasterId").setParameter("theRxMasterId", theRxMasterId);
				System.out.println("PurchaseServiceImpl  String getVendor() query string :: "+aQuery.getQueryString());
				List<?> list = aQuery.list();
//				if(aQuery.list().size() > 0)
//					aVendorName	=	(String)aQuery.uniqueResult();
				if(list.size() > 0)
					aVendorName	=	(String)aQuery.uniqueResult();
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
		}
		return aVendorName;
	}
	
	@Override
	public String getManufacture(Integer theRxMasterId) throws VendorException {
		
		String aQry = "SELECT description FROM VeFactory WHERE rxMasterId = :theRxMasterId and InActive <> 1";
		Session aSession=null;
		String aManufactureName = null;
		List liQuery = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry).setParameter("theRxMasterId", theRxMasterId);
			liQuery = aQuery.list();
			
			if(liQuery.size() > 0)
				aManufactureName=(String)liQuery.get(0);
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}	
		return aManufactureName;
	}	
	
	@Override
	public CoTaxTerritory getSingleTaxTerritory(String country)
			throws CompanyException {
		String query = "SELECT coTaxTerritoryId,taxRate FROM CoTaxTerritory where county LIKE '" + country + "%';";
		Session aSession =null;
		CoTaxTerritory aCoTaxTerritory = new CoTaxTerritory();
		try{ 
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(query);
			List<?> list = aQuery.list();
			if(list.size() > 0)
			{
				Iterator<?> aIterator = list.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					aCoTaxTerritory.setCoTaxTerritoryId((Integer)aObj[0]);
					
					aCoTaxTerritory.setTaxRate((BigDecimal)aObj[1]);
					aCoTaxTerritory.setCounty(country);					
				}
			}		
			
			else
			{
				aCoTaxTerritory.setCounty(country);
				aCoTaxTerritory.setTaxRate(new BigDecimal(0));
			}
				
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CompanyException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCoTaxTerritory;
	}
	
	@Override
	public CoTaxTerritory getSingleTaxTerritory(Integer TaxTerritoryID)
			throws CompanyException {
		String query = "FROM CoTaxTerritory where coTaxTerritoryId="+TaxTerritoryID;
		Session aSession =null;
		CoTaxTerritory aCoTaxTerritory = new CoTaxTerritory();
		//System.out.println("PurchaseServiceImpl CoTaxTerritory getSingleTaxTerritory() query string :: "+query);
		try{ 
			aSession = itsSessionFactory.openSession();
			aCoTaxTerritory = (CoTaxTerritory) aSession.createQuery(query).list().get(0);
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			throw new CompanyException(excep.getMessage(), excep);
		} finally {
			aSession.flush();
			aSession.close();
			query = null;
		}
		return aCoTaxTerritory;
	}
	
	/*public List<PurchaseOrdersBean> getAllSOsList(int from, int to) throws VendorException {
		itsLogger.debug("Retrieving all POs");
		StringBuilder aPOSelectQry = new StringBuilder("SELECT vp.vePOID, vp.CreatedOn, vp.joReleaseID, vp.rxVendorID, vp.PONumber, rm.Name, vp.subtotal ")
												.append("FROM vePO vp LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID ")
												.append("LIMIT " + from + ", " + to);
		Session aSession = null;
		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);  			
				aPurchaseOrdersBean.setCreatedOn((Date) aObj[1]);			
				aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);		
				aPurchaseOrdersBean.setRxVendorID((Integer)aObj[3]);		
				aPurchaseOrdersBean.setPONumber((String) aObj[4]);			
				aPurchaseOrdersBean.setVendorName((String) aObj[5]);
				aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[6]);
				aQueryList.add(aPurchaseOrdersBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}*/
	@Override
	public List<PurchaseOrdersBean> getAllSOsList(int from, int to,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException {
		itsLogger.debug("Retrieving all SOs"+searchData);
		String searchCondition ="";
		StringBuilder aPOSelectQry = null;
		if(!searchData.equals("")){
			itsLogger.info("Data Available");
			searchCondition = "WHERE (so.CreatedOn LIKE '%"+searchData+"%' OR so.joReleaseID LIKE '%"+searchData+"%' " +
					" OR so.rxCustomerID LIKE '%"+searchData+"%' " +
					" OR so.SONumber LIKE '%"+searchData+"%' " +
					" OR rm.Name LIKE '%"+searchData+"%' " +
					" OR j.Description LIKE '%"+searchData+"%'" +
					" OR (so.costtotal-so.SubTotal) LIKE '%"+searchData+"%'"+
					" OR subtotal LIKE '%"+searchData+"%'"+
					" OR costtotal LIKE '%"+searchData+"%')";
			}
		if(!startDate.equals("")&& !endDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND so.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
			}else{
				searchCondition+= " WHERE so.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
			}
		}
		else if(!startDate.equals("") && endDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND so.CreatedOn >='"+startDate+" 00:00:00'";
			}else{
				searchCondition+= " WHERE so.CreatedOn >='"+startDate+" 00:00:00'";
			}
		}
		else if(!endDate.equals("") && startDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND so.CreatedOn<='"+endDate+" 23:59:59'";
			}else{
				searchCondition+= " WHERE so.CreatedOn<='"+endDate+" 23:59:59'";
			}
		}
		      

		String orderByIndex="";
			String orderBy="DESC";
			
			if(sortIndex.equals("createdOn")){
				orderByIndex="createdOn DESC ,SONumber";
			}else if(sortIndex.equals("ponumber")){
				orderByIndex="SONumber";
			}else if(sortIndex.equals("vendorName")){
				orderByIndex="vendorName";
			}else if(sortIndex.equals("jobName")){
				orderByIndex="jobName";
			}else if(sortIndex.equals("subtotal")){
				orderByIndex="subtotal";
			}else if(sortIndex.equals("costtotal")){
				orderByIndex="costtotal";
			}
			else if(sortIndex.equals("difference")){
				orderByIndex="difference";
			}
			
			if(!sortOrder.equals("")){
				orderBy=sortOrder.toUpperCase();
			}
		
		
		aPOSelectQry = new StringBuilder("SELECT so.cuSOID, so.CreatedOn as createdOn, so.joReleaseID, so.rxCustomerID, so.SONumber as SONumber, rm.Name as vendorName, IFNULL(so.CostTotal,0.0000) as costtotal, so.Tag, IFNULL(so.SubTotal,0.0000) as subtotal ,j.Description as  jobName,(IFNULL(so.costtotal,0.0000)-IFNULL(so.SubTotal,0.0000)) AS difference ")
		.append("FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID  LEFT JOIN joRelease jo ON so.joReleaseID = jo.joReleaseID LEFT JOIN joMaster j ON jo.joMasterID = j.joMasterID ")
		
		
		/*StringBuilder aPOSelectQry = new StringBuilder("SELECT so.cuSOID, so.CreatedOn, so.joReleaseID, so.rxCustomerID, so.SONumber, rm.Name, so.costtotal, so.Tag, so.SubTotal  ")
		.append("FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID ")*/
		
		/*  INNER JOIN joRelease jo ON (jo.joReleaseID = so.joReleaseID) INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) ")
		 * Commented By Jenith on 2014-10-23 15:09
		 * StringBuilder aPOSelectQry = new StringBuilder("SELECT so.cuSOID, so.CreatedOn, so.joReleaseID, so.rxCustomerID, so.SONumber, rm.Name, so.costtotal, so.Tag, so.SubTotal  ")
		.append("FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID  INNER JOIN joRelease jo ON (jo.joReleaseID = so.joReleaseID)	INNER JOIN joMaster j ON (j.joMasterID = jo.joMasterID) ")
		
		*/
		
		.append(searchCondition)		
		.append(" ORDER BY "+orderByIndex+"  "+orderBy+" LIMIT " + from + ", " + to);
		//.append(" ORDER BY so.cuSOID DESC LIMIT " + from + ", " + to);
		
		Session aSession = null;
		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		Query aQuery = null;
		Iterator<?> aIterator = null;
		List<?> aList = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			itsLogger.info("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);  
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}
				else
					aPurchaseOrdersBean.setCreatedOn((Date)aObj[1] ); //(Date) aObj[1]	
				
				aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);	
				itsLogger.info("Job Id = "+aPurchaseOrdersBean.getJoReleaseID());
				if(aPurchaseOrdersBean.getJoReleaseID() == null || aPurchaseOrdersBean.getJoReleaseID() == 1)
				{
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName((String) aObj[7]);
				}
				else
				{
					String jobName = "SELECT mas.Description FROM joMaster AS mas LEFT JOIN joRelease jo ON jo.joMasterID = mas.joMasterID WHERE jo.joReleaseID ="+ aPurchaseOrdersBean.getJoReleaseID();
					//jobSession = itsSessionFactory.openSession();
					Query aQuery1 = aSession.createSQLQuery(jobName.toString());
					String sjobname = "";
					aList = aQuery1.list();
					if(aList.size() > 0)
						sjobname = (String)aList.get(0);						
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName(sjobname);
					
				}
				aPurchaseOrdersBean.setRxVendorID((Integer)aObj[3]);		
				aPurchaseOrdersBean.setPONumber((String) aObj[4]);			
				aPurchaseOrdersBean.setVendorName((String) aObj[5]);
				aPurchaseOrdersBean.setCosttotal((BigDecimal) aObj[6]);
				aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[8]);
				/*
				 * BigDecimal mapvalue=getTemplatePriceDetails(aPurchaseOrdersBean.getVePOID());
				aPurchaseOrdersBean.setWhCost(mapvalue);*/
				BigDecimal difference = new BigDecimal(0);
				if(aObj[8]!=null){
					//difference = (aPurchaseOrdersBean.getCosttotal()).subtract((BigDecimal) aObj[8]);	 // CommentedBy:Naveed - BID #1132
					difference = ((BigDecimal) aObj[8]).subtract(aPurchaseOrdersBean.getCosttotal());	
				}
				aPurchaseOrdersBean.setDifference(difference);
				aQueryList.add(aPurchaseOrdersBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			searchCondition = null;
			aPOSelectQry = null;
			aQuery = null;
			aIterator = null;
			aList = null;
		}
		return  aQueryList;
	}
	public BigDecimal getTemplatePriceDetails(int CusoID){
		Session aSession = null;
		BigDecimal margin = new BigDecimal(0);
		
		BigDecimal cost = new BigDecimal(0);
		Map<String,BigDecimal> aPrice = new HashMap<String, BigDecimal>();
		String Query = null;
		try {
			Query = "SELECT prMaster.prMasterID,template.QuantityOrdered FROM cuSODetail AS template,prMaster WHERE template.cuSOID= :cuSoid AND prMaster.prMasterID = template.prMasterID";
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(Query);
			aQuery.setParameter("cuSoid", CusoID);
			List aList = aQuery.list();
			if (!aList.isEmpty()) {
				Iterator<?> aIterator = aList.iterator();
				while (aIterator.hasNext()) {
					Object[] aObj = (Object[])aIterator.next();
					BigDecimal qty = (BigDecimal)aObj[1]== null ? new BigDecimal(0.00) : (BigDecimal)aObj[1];
					cost = cost.add(itsInventoryService.getWarehouseCost((Integer)aObj[0]).multiply(qty));
					
				}				
			}
				
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			
		} finally {
			aSession.flush();
			aSession.close();
			Query = null;
		}
		return cost;
	}
	
	@Override
	public List<Rxaddress> getCustomerAddresses(Integer rxMasterID) throws VendorException {
		
		String aQry = "SELECT rxMaster.Name,address.Address1,address.Address2,address.city,address.State,address.Zip,address.rxAddressId,address.IsDefault FROM rxAddress AS address JOIN rxMaster ON rxMaster.rxMasterID = address.rxMasterID WHERE address.rxMasterID = "+rxMasterID;
		itsLogger.info(aQry);
		Session aSession=null;
		Rxaddress objRxaddress = null;
		List<Rxaddress> alRxaddress = new ArrayList<Rxaddress>();
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
//			Iterator<?> aIterator = aQuery.list().iterator();
			List<?> list = aQuery.list();
			Iterator<?> aIterator = list.iterator();
			while(aIterator.hasNext())
			{
				objRxaddress = new Rxaddress();
				Object[] aObj = (Object[])aIterator.next();
				objRxaddress.setName((String) aObj[0]);
				objRxaddress.setAddress1((String) aObj[1]);
				objRxaddress.setAddress2((String) aObj[2]);
				objRxaddress.setCity((String) aObj[3]);
				objRxaddress.setState((String) aObj[4]);
				objRxaddress.setZip((String) aObj[5]);
				objRxaddress.setRxAddressId((Integer) aObj[6]);
				Byte isdefault= ((Byte) aObj[7]);
				if(isdefault == 1){
					objRxaddress.setIsDefault(true);
				}else{
					objRxaddress.setIsDefault(false);
				}
				alRxaddress.add(objRxaddress);
			}
//			if(aQuery.list().size() > 0)
//			return alRxaddress;
			if(list.size() > 0)
				return alRxaddress;
			else
				return new ArrayList<Rxaddress>();
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	public Integer getSORecordsCount()throws VendorException{
		String aJobCountStr = "SELECT COUNT(cuSoid) AS count FROM cuSO";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	/*public Integer getSORecordsCount()throws VendorException{
		Integer itsReleaseType = 2;
		String aJobCountStr = "SELECT COUNT(itsJoReleaseID) AS COUNT FROM joRelease WHERE itsReleaseType=:itsReleaseType";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr).setParameter("itsReleaseType", itsReleaseType);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aTotalCount.intValue();
	}*/
	@Override
	public Integer getVepoIDFRomPoNumber(String PoNumber) throws VendorException {
		String aQryStr = "SELECT c.vePoid FROM Vepo c WHERE  c.ponumber= :poNumber";
		Session aSession = null;
		Integer vePOID = null;
		try {
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQryStr).setParameter("poNumber", PoNumber);
			if(aQuery.list().size() > 0)
				vePOID	=	(Integer)aQuery.uniqueResult();
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw  new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQryStr = null;
		}
		return vePOID;
	}	
	@Override
	public Vepo getVePo(Integer theVePOID) throws VendorException {
		Vepo aPO = new Vepo();
		Session aSession = null;
		try {
			aSession = itsSessionFactory.openSession();
			aPO = (Vepo) aSession.get(Vepo.class, theVePOID);
			if(aPO != null){
				if(aPO.getTag() == null){
					aPO.setTag("");
				}if(aPO.getDateWanted() == null){
					aPO.setDateWanted("");
				}if(aPO.getCustomerPonumber() == null){
					aPO.setCustomerPonumber("");
				}if(aPO.getSpecialInstructions() == null){
					aPO.setSpecialInstructions("");
				}
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			VendorException aVendorException = new VendorException(e.getCause().getMessage(), e);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		return aPO;
	}
	
	

	public Integer getCuInvoiceRecordsCount(){
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedfrom);
		
		cal.add(Calendar.MONTH, -6);
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedto);
	
		//String acuInvoiceCountStr = "SELECT COUNT(cuInvoiceID) AS count FROM cuInvoice WHERE CreditMemo = 0 OR CreditMemo IS NULL and CreatedOn BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59'";
		String acuInvoiceCountStr="SELECT COUNT(dummy.countvalue) FROM (SELECT COUNT(cu.cuInvoiceID) AS countvalue  FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID  LEFT JOIN joReleaseDetail jod ON(jod.joReleaseDetailID=cu.joReleaseDetailID) LEFT JOIN joMaster jo ON(jo.joMasterID=jod.joMasterID)  LEFT JOIN cuPaymentGlpost cgl ON (cgl.cuInvoiceID = cu.cuInvoiceID AND cgl.postStatus<>0) LEFT JOIN cuLinkageDetail cuL ON (cuL.cuInvoiceID = cu.cuInvoiceID AND cuL.datePaid = (SELECT MAX(datePaid) FROM cuLinkageDetail WHERE cuInvoiceID = cu.cuInvoiceID ))  LEFT JOIN cuReceipt cur ON cur.cuReceiptID = cuL.cuReceiptID  WHERE cu.CreatedOn BETWEEN '"+formattedto+" 00:00:00' AND '"+formattedfrom+" 23:59:59' AND cu.CreditMemo = 0 OR cu.CreditMemo IS NULL GROUP BY cu.cuInvoiceID ) AS dummy";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(acuInvoiceCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			acuInvoiceCountStr = null;
		}
		return aTotalCount.intValue();
	}
	
	public Integer getCuInvoiceRecordsCountWithDate(String startDate,String endDate,String searchData){
		
/*		String acuInvoiceCountStr =null;
		if(!fromdate.equals("")&& !todate.equals("")){
			acuInvoiceCountStr = "SELECT COUNT(cuInvoiceID) AS count FROM cuInvoice WHERE CreatedOn BETWEEN '"+fromdate +" 00:00:00' AND '"+todate+" 23:59:59'";
		}
		else if(!fromdate.equals("") && todate.equals("")){
			acuInvoiceCountStr = "SELECT COUNT(cuInvoiceID) AS count FROM cuInvoice WHERE CreatedOn  >= '"+fromdate +" 00:00:00' ";
		}
		else if(fromdate.equals("") && !todate.equals("")){
			acuInvoiceCountStr = "SELECT COUNT(cuInvoiceID) AS count FROM cuInvoice WHERE CreatedOn  <= '"+todate +" 23:59:59' "
			
		}*/
		String searchCondition ="";
		if(!searchData.equals("")){
			searchCondition = "WHERE (rm.Name LIKE '%"+searchData+"%' OR cu.InvoiceNumber LIKE '%"+searchData+"%' OR cu.CustomerPONumber LIKE '%"+searchData+"%' " +
					"OR jo.Description LIKE '%"+searchData+"%' OR cu.rxCustomerID LIKE '%"+searchData+"%' OR cu.Subtotal LIKE '%"+searchData+"%') ";
			}
			if(!startDate.equals("")&& !endDate.equals("")){
				if(searchCondition.contains("WHERE")){
					searchCondition+= " AND cu.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59' ";
				}else{
					searchCondition+= " WHERE cu.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
				}
			}
			else if(!startDate.equals("") && endDate.equals("")){
				if(searchCondition.contains("WHERE")){
					searchCondition+= " AND cu.CreatedOn >='"+startDate+" 00:00:00' ";
				}else{
					searchCondition+= " WHERE cu.CreatedOn >='"+startDate+" 00:00:00' ";
				}
			}
			else if(!endDate.equals("") && startDate.equals("")){
				if(searchCondition.contains("WHERE")){
					searchCondition+= " AND cu.CreatedOn <='"+endDate+" 23:59:59' ";
				}else{
					searchCondition+= " WHERE cu.CreatedOn <='"+endDate+" 23:59:59'";
				}
			}
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formattedfrom = format1.format(cal.getTime());
			System.out.println("formattedfrom "+formattedfrom);
			
			cal.add(Calendar.MONTH, -6);
			String formattedto = format1.format(cal.getTime());
			System.out.println("formattedto "+formattedto);
			
			if(searchCondition=="")
			{
				searchCondition+= " WHERE cu.CreatedOn BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59' and cu.CreditMemo = 0 OR cu.CreditMemo IS NULL";
			}else{
				searchCondition+="and cu.CreditMemo = 0 OR cu.CreditMemo IS NULL";
			}
			
			
			StringBuilder aPOSelectQry = new StringBuilder("SELECT COUNT(cu.cuInvoiceID)  ")
			.append(" FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID ")
			.append(" LEFT JOIN joReleaseDetail jod ON(jod.joReleaseDetailID=cu.joReleaseDetailID) LEFT JOIN joMaster jo ON(jo.joMasterID=jod.joMasterID) ")
			.append(" LEFT JOIN cuPaymentGlpost cgl ON (cgl.cuInvoiceID = cu.cuInvoiceID AND cgl.postStatus<>0) LEFT JOIN cuLinkageDetail cuL ON (cuL.cuLinkageDetailID = cgl.cuLinkageDetailID) LEFT JOIN cuReceipt cur ON cur.cuReceiptID = cuL.cuReceiptID ")
			.append(searchCondition);
			

		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aPOSelectQry = null;
		}
		return aTotalCount.intValue();
	}

	@Override
	public List<Cuinvoice> getAllCuInvoicesList(int from, int to,String sortIndex, String sortOrder,String searchData,String startDate,String endDate) throws VendorException {
		itsLogger.debug("Retrieving all POs");
		String orderByIndex="";
		String orderBy="DESC";
		String searchCondition ="";
		if(sortIndex.equals("cuInvoiceId")){
			orderByIndex="cu.cuInvoiceID";
		}else if(sortIndex.equals("createdOn")){
			orderByIndex="cu.CreatedOn";
		}else if(sortIndex.equals("customerPonumber")){
			orderByIndex="CustomerPONumber";
		}else if(sortIndex.equals("invoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("jobname")){
			orderByIndex="Description";
		}else if(sortIndex.equals("joReleaseDetailId")){
			orderByIndex="joReleaseDetailID";
		}else if(sortIndex.equals("quickJobName")){
			orderByIndex="Name";
		}else if(sortIndex.equals("rxCustomerId")){
			orderByIndex="rxCustomerID";
		}else if(sortIndex.equals("cuSoid")){
			orderByIndex="cuSOID";
		}else if(sortIndex.equals("subtotal")){
			orderByIndex="Subtotal";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="CAST(reference AS SIGNED)";
		}else if(sortIndex.equals("datePaid")){
			orderByIndex="cur.ReceiptDate";
		}else if(sortIndex.equals("invoiceAmount")){
			orderByIndex="InvoiceAmount";
		}
		
		if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}
		if(!searchData.equals("")){
		searchCondition = "WHERE (rm.Name LIKE '%"+searchData+"%' OR cu.InvoiceNumber LIKE '%"+searchData+"%' OR cu.CustomerPONumber LIKE '%"+searchData+"%' " +
				"OR jo.Description LIKE '%"+searchData+"%' OR cu.rxCustomerID LIKE '%"+searchData+"%' OR cu.Subtotal LIKE '%"+searchData+"%') ";
		}
		if(!startDate.equals("")&& !endDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND cu.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59' ";
			}else{
				searchCondition+= " WHERE cu.CreatedOn BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
			}
		}
		else if(!startDate.equals("") && endDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND cu.CreatedOn >='"+startDate+" 00:00:00' ";
			}else{
				searchCondition+= " WHERE cu.CreatedOn >='"+startDate+" 00:00:00' ";
			}
		}
		else if(!endDate.equals("") && startDate.equals("")){
			if(searchCondition.contains("WHERE")){
				searchCondition+= " AND cu.CreatedOn <='"+endDate+" 23:59:59' ";
			}else{
				searchCondition+= " WHERE cu.CreatedOn <='"+endDate+" 23:59:59'";
			}
		}
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedfrom);
		
		cal.add(Calendar.MONTH, -6);
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedto);
		
		if(searchCondition=="")
		{
			searchCondition+= " WHERE cu.CreatedOn BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59' and cu.CreditMemo = 0 OR cu.CreditMemo IS NULL";
		}else{
			searchCondition+="and cu.CreditMemo = 0 OR cu.CreditMemo IS NULL";
		}
		
		
		StringBuilder aPOSelectQry = new StringBuilder("SELECT cu.cuInvoiceID, cu.CreatedOn, cu.joReleaseDetailID, cu.cuSOID,cu.rxCustomerID, cu.InvoiceNumber,cu.CustomerPONumber, rm.Name, cu.Subtotal,cu.InvoiceAmount,(CASE WHEN jo.Description IS NULL THEN cu.jobnodescription ELSE jo.Description END) as Description,cu.cIopenStatus,cur.reference,cuL.datePaid,jo.joMasterID,cu.EmailDate,cu.emailStatus ")
		.append(" FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID ")
		.append(" LEFT JOIN joReleaseDetail jod ON(jod.joReleaseDetailID=cu.joReleaseDetailID) LEFT JOIN joMaster jo ON(jo.joMasterID=jod.joMasterID) ")
		.append(" LEFT JOIN cuPaymentGlpost cgl ON (cgl.cuInvoiceID = cu.cuInvoiceID AND cgl.postStatus<>0) LEFT JOIN cuLinkageDetail cuL ON (cuL.cuInvoiceID = cu.cuInvoiceID AND cuL.datePaid = (SELECT MAX(datePaid) FROM cuLinkageDetail WHERE cuInvoiceID = cu.cuInvoiceID ))  LEFT JOIN cuReceipt cur ON cur.cuReceiptID = cuL.cuReceiptID ")
		.append(searchCondition)
		.append(" GROUP BY cu.cuInvoiceID ORDER BY "+orderByIndex+" "+ orderBy +",cu.InvoiceNumber Desc,rm.Name Asc LIMIT " + from + ", " + to);
		
		System.out.println("PurchaseServiceImpl List<Cuinvoice> getAllCuInvoicesList() query string aPOSelectQry :: "+aPOSelectQry);
		Session aSession = null;
		List<Cuinvoice> aQueryList = new ArrayList<Cuinvoice>();
		Cuinvoice aCuinvoice = null;
		try {
			System.out.println("aPOSelectQry"+aPOSelectQry);
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aCuinvoice = new Cuinvoice();
				Object[] aObj = (Object[])aIterator.next();
				aCuinvoice.setCuInvoiceId((Integer) aObj[0]);  
				if(aObj[1] != null)
					{
						Timestamp stamp = (Timestamp)aObj[1];
						Date date = new Date(stamp.getTime());
						aCuinvoice.setCreatedOn(date); //(Date) aObj[1]
					}
				else
				aCuinvoice.setCreatedOn((Date)aObj[1] ); //(Date) aObj[1]	
				aCuinvoice.setJoReleaseDetailId((Integer) aObj[2]);	
				aCuinvoice.setCuSoid((Integer)aObj[3]);	
				aCuinvoice.setRxCustomerId((Integer)aObj[4]);		
				aCuinvoice.setInvoiceNumber((String) aObj[5]);	
				aCuinvoice.setCustomerPonumber((String) aObj[6]);
				aCuinvoice.setQuickJobName((String) aObj[7]);
				aCuinvoice.setSubtotal((BigDecimal) aObj[8]);
				aCuinvoice.setInvoiceAmount((BigDecimal) aObj[9]);
				aCuinvoice.setJobname((String) aObj[10]);
				aCuinvoice.setcIopenStatus((Boolean) aObj[11]);
				aCuinvoice.setChkNo((String) aObj[12]);
				if(aObj[13] != null)
				{
					Timestamp stamp = (Timestamp)aObj[13];
					Date date = new Date(stamp.getTime());
					aCuinvoice.setDatePaid(date); //(Date) aObj[1]
				}
				Integer joMasterID=0;
				if(aObj[14]!=null){
					joMasterID=(Integer)aObj[14];
				}
				aCuinvoice.setJoMasterID(joMasterID);
				if(aObj[15] != null)
				{
					Timestamp stamp = (Timestamp)aObj[15];
					Date date = new Date(stamp.getTime());
					aCuinvoice.setSentEmailDate((String) DateFormatUtils
							.format(date, "MM/dd/yy hh:mm a"));
				}
				aCuinvoice.setEmailStatus((Integer) aObj[16]);
				
				aQueryList.add(aCuinvoice);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return  aQueryList;
	}
	
	@Override
	public List<Rxcontact> getEmailList(Integer rxMasterID) throws VendorException {
		
		//String sQuery = "SELECT rxContactID,EMail FROM rxContact where rxMasterID = :rxMasterID";
		String sQuery = "FROM Rxcontact where rxMasterId = :rxMasterID";
		Session aSession = null;
		Query aQuery = null;
		List<?> list = null;
		try
		{
			aSession = itsSessionFactory.openSession();
			aQuery = aSession.createQuery(sQuery).setParameter("rxMasterID", rxMasterID);
//			if(aQuery.list().size() > 0)
//				return aQuery.list();
			list = aQuery.list();
			if(list.size() > 0)
				return (List<Rxcontact>)list;
			else
				return null;
		}
		catch(Exception e)
		{
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQuery = null;
			list = null;
		}
		
	}
	@Override
	public List<PurchaseOrdersBean> filterPOsList(int from, int to,String searchData,String startDate,String endDate,String sortIndex,String sortOrder) throws VendorException, ParseException {
		itsLogger.debug("Retrieving all POs");
		/*StringBuilder aPOSelectQry = new StringBuilder("SELECT vp.vePOID, vp.CreatedOn, vp.joReleaseID, vp.rxVendorID, vp.PONumber, rm.Name, vp.subtotal, vp.Tag,vp.receiveddate,(SUM(ve.QuantityOrdered) - SUM(IF(ve.QuantityReceived IS NULL OR ve.QuantityReceived = '', 0, ve.QuantityReceived))) AS Recievedifference  ")
												.append(" FROM vePODetail ve,vePO vp LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID ");*/
		
		StringBuilder aPOSelectQry = new StringBuilder("SELECT vp.vePOID, vp.CreatedOn, vp.joReleaseID, vp.rxVendorID, vp.PONumber, rm.Name, vp.subtotal, vp.Tag,vp.receiveddate,")
		.append("(SUM(ve.QuantityOrdered) - SUM(IF(ve.QuantityReceived IS NULL OR ve.QuantityReceived = '', 0, ve.QuantityReceived))) AS Recievedifference, Max(vr.veReceiveID) ")
		.append("FROM vePODetail ve LEFT JOIN vePO vp ON vp.vePOID = ve.vePOID LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID "
				+ " LEFT JOIN veReceive vr ON vr.vePOID = ve.vePOID LEFT JOIN prMaster pr ON pr.prMasterID = ve.prMasterID ");
		
		if(searchData !=null && !searchData.trim().equals("") && (startDate!=null && !startDate.trim().equals("")) && 
				(endDate!=null && !endDate.trim().equals(""))){
			aPOSelectQry.append(" WHERE (vp.PONumber like '%"+searchData+"%' or rm.Name like '%"+searchData+"%')");	
		}else if(searchData !=null && !searchData.trim().equals("")){
			aPOSelectQry.append(" WHERE (vp.PONumber like '%"+searchData+"%' or rm.Name like '%"+searchData+"%') AND receiveddate <>'' ");	
		}else {
			aPOSelectQry.append(" WHERE receiveddate <>''");	
		}
	
		if(!startDate.equals("")&& !endDate.equals("")){
			aPOSelectQry.append(" AND vp.receiveddate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'");
		}
		else if(!startDate.equals("") && endDate.equals("")){
			aPOSelectQry.append(" AND vp.receiveddate >='"+startDate+" 00:00:00'");
		}else if(!endDate.equals("") && startDate.equals("")){
			aPOSelectQry.append(" AND vp.receiveddate<='"+endDate+" 23:59:59'");
		}
		
		aPOSelectQry.append(" AND ve.vePOID = vp.vePOID AND pr.IsInventory<>0  GROUP BY vp.vePOID");
		/*if(vendorId!=0){
			if(range.equalsIgnoreCase("to")){
				
				aPOSelectQry.append(" WHERE rxVendorID="+vendorId+" AND receiveddate <>'' AND ve.vePOID = vp.vePOID GROUP BY vp.vePOID");	
			}else{
				String[] daterange = range.split("to");
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
				DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date startDate = df.parse(daterange[0]);
				java.util.Date endDate = df.parse(daterange[1]);
				
				System.out.println("inside non empty"+df1.format(startDate)+" :to: "+df1.format(endDate));
				
				aPOSelectQry.append(" WHERE rxVendorID="+vendorId+" AND receiveddate BETWEEN '"+df1.format(startDate)+"' AND '"+df1.format(endDate)+"' AND ve.vePOID = vp.vePOID GROUP BY vp.vePOID");
			}
			
		}else{
			if(range.equalsIgnoreCase("to")){
				System.out.println("inside empty");
				aPOSelectQry.append(" WHERE receiveddate <>'' AND ve.vePOID = vp.vePOID GROUP BY vp.vePOID");	
			}else{
				System.out.println("inside non empty");
				
				String[] daterange = range.split("to");
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
				DateFormat df1 = new SimpleDateFormat("yyyy/MM/dd");
				java.util.Date startDate = df.parse(daterange[0]);
				java.util.Date endDate = df.parse(daterange[1]);
				
				System.out.println("inside non empty"+df1.format(startDate)+" :to: "+df1.format(endDate));
				aPOSelectQry.append(" WHERE receiveddate BETWEEN '"+df1.format(startDate)+"' AND '"+df1.format(endDate)+"' AND ve.vePOID = vp.vePOID GROUP BY vp.vePOID");
			}
			//aPOSelectQry.append(" WHERE receiveddate <>''");
		}
		if(orderby.equals("from")){
			aPOSelectQry.append("ORDER BY rm.Name ASC");	
		}
		else if(orderby.equals("date")){
			aPOSelectQry.append("ORDER BY vp.receiveddate DESC");	
		}*/
		aPOSelectQry.append(" LIMIT " + from + ", " + to);
		Session aSession = null;

		List<PurchaseOrdersBean> aQueryList = new ArrayList<PurchaseOrdersBean>();
		PurchaseOrdersBean aPurchaseOrdersBean = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			//System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aPurchaseOrdersBean = new PurchaseOrdersBean();
				Object[] aObj = (Object[])aIterator.next();
				aPurchaseOrdersBean.setVePOID((Integer) aObj[0]);			
				if(aObj[8] != null)
				{
					Timestamp stamp = (Timestamp)aObj[8];
					Date date = new Date(stamp.getTime());
					//System.out.println("Date in PO is----->"+date);
					aPurchaseOrdersBean.setCreatedOn(date); //(Date) aObj[1]
				}else
					aPurchaseOrdersBean.setCreatedOn((Date)aObj[8] ); //(Date) aObj[1]			
				aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);	
				//System.out.println("Job Id = "+aPurchaseOrdersBean.getJoReleaseID());
				if(aPurchaseOrdersBean.getJoReleaseID() == null || aPurchaseOrdersBean.getJoReleaseID() == 1)
				{
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName((String) aObj[7]);
				}else{
					String jobName = "SELECT mas.Description FROM joMaster AS mas LEFT JOIN joRelease jo ON jo.joMasterID = mas.joMasterID WHERE jo.joReleaseID ="+ aPurchaseOrdersBean.getJoReleaseID();
					//jobSession = itsSessionFactory.openSession();
					Query aQuery1 = aSession.createSQLQuery(jobName.toString());
					List<?> aList = aQuery1.list();
					String sjobname = "";
					if(aList.size() > 0)
						sjobname = (String)aList.get(0);
					aPurchaseOrdersBean.setJoReleaseID((Integer) aObj[2]);
					aPurchaseOrdersBean.setJobName(sjobname);
				}
				//SELECT description FROM jomaster AS mas LEFT JOIN jorelease jo ON jo.jomasterID = mas.jomasterID WHERE jo.joreleaseID = 66186
				aPurchaseOrdersBean.setRxVendorID((Integer)aObj[3]);		
				aPurchaseOrdersBean.setPONumber((String) aObj[4]);			
				aPurchaseOrdersBean.setVendorName((String) aObj[5]);
				aPurchaseOrdersBean.setSubtotal((BigDecimal) aObj[6]);
				aPurchaseOrdersBean.setDifference((BigDecimal)aObj[9]);
				aPurchaseOrdersBean.setVeReceiveID((Integer)aObj[10]);
				
				//System.out.println("Job Name = "+aPurchaseOrdersBean.getJobName());
				aQueryList.add(aPurchaseOrdersBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aPOSelectQry = null;
		}
		return  aQueryList;
	}
	
	@Override
	public List<WarehouseTransferBean> getAllWarehouseTransfer(int from, int to) throws VendorException {
		itsLogger.debug("Retrieving all POs");
		StringBuilder aPOSelectQry = new StringBuilder("SELECT wh.prTransferID,wh.TransferDate,wh.prFromWareHouseID,wh.prToWareHouseID,wh.TransNumber,wh.Description,wh.ReceivedDate,wh.EstreceiveDate")
												.append(" FROM prWHtransfer wh WHERE wh.prToWareHouseID is NOT NULL")
												.append(" ORDER BY wh.TransferDate DESC LIMIT " + from + ", " + to);
		
		
		
	/*	SELECT wh.prTransferID,wh.TransferDate,,wh.ReceivedDate,wh.prFromWareHouseID,wh.prToWareHouseID,wh.TransNumber,wh.Description
		FROM prWHtransfer wh WHERE wh.prToWareHouseID IS NOT NULL
			ORDER BY wh.TransferDate DESC*/
		
		Session aSession = null;
		List<WarehouseTransferBean> aQueryList = new ArrayList<WarehouseTransferBean>();
		WarehouseTransferBean aWarehouseTransferBean = null;
		String sQuery = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			System.out.println("Session statics: " + itsSessionFactory.getStatistics().getConnectCount());
			// Create a Hibernate aQuery (HQL)
			Query aQuery = aSession.createSQLQuery(aPOSelectQry.toString());
			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aWarehouseTransferBean = new WarehouseTransferBean();
				Object[] aObj = (Object[])aIterator.next();
				aWarehouseTransferBean.setPrTransferId((Integer) aObj[0]);			
				if(aObj[1] != null)
				{
					Timestamp stamp = (Timestamp)aObj[1];
					Date date = new Date(stamp.getTime());
					System.out.println("Date in Warehouse Transfer is----->"+date);
					aWarehouseTransferBean.setTransferDate(date); //(Date) aObj[1]
				}
				else
					aWarehouseTransferBean.setTransferDate((Date)aObj[1] ); //(Date) aObj[1]
				if(null != aObj[2] )
				{
					Integer prFromWarehouseId = (Integer) aObj[2];
					sQuery = "SELECT city FROM prWarehouse WHERE prWarehouseID = "+ prFromWarehouseId;
					//jobSession = itsSessionFactory.openSession();
					Query aQuery1 = aSession.createSQLQuery(sQuery.toString());
					List<?> bList = aQuery1.list();
					String sCity = "";
					if(bList.size() > 0)
						sCity = (String)bList.get(0);
					aWarehouseTransferBean.setFrom(sCity);
				}
				if(null != aObj[3] )
				{
					Integer prToWarehouseId = (Integer) aObj[3];
					sQuery = "SELECT city FROM prWarehouse WHERE prWarehouseID = "+ prToWarehouseId;
					//jobSession = itsSessionFactory.openSession();
					Query aQuery1 = aSession.createSQLQuery(sQuery.toString());
					List<?> aList = aQuery1.list();
					String sCity = "";
					if(aList.size() > 0)
						sCity = (String)aList.get(0);
					aWarehouseTransferBean.setTo(sCity);
				}
				//(String) aObj[2]
				//aWarehouseTransferBean.setTo("");//(String) aObj[3]
				aWarehouseTransferBean.setTransactionNumber((Integer) aObj[4]);
				aWarehouseTransferBean.setDesc((String) aObj[5]);
				if(aObj[6] != null)
				{
					Timestamp stamp = (Timestamp)aObj[6];
					Date date = new Date(stamp.getTime());
					System.out.println("Date in Warehouse Transfer is----->"+date);
					aWarehouseTransferBean.setReceivedDate(date); //(Date) aObj[1]
				}
				else
					aWarehouseTransferBean.setReceivedDate((Date)aObj[6] ); 
				//(Date) aObj[1]
				if(aObj[7] != null)
				
				aQueryList.add(aWarehouseTransferBean);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			sQuery = null;
		}
		return  aQueryList;
	}
	
	public Integer getRecordsCountForWhTransfer()throws VendorException{
		String aJobCountStr = "SELECT COUNT(prTransferID) AS count FROM prWHtransfer WHERE prToWareHouseID is NOT NULL";
		Session aSession = null;
		BigInteger aTotalCount = null;
		try {
			// Retrieve aSession from Hibernate
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aJobCountStr);
			List<?> aList = aQuery.list();
			aTotalCount = (BigInteger) aList.get(0);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aJobCountStr= null;
		}
		return aTotalCount.intValue();
	}
	@Override
	public List<Prmaster> getLineItemsForSOTemplate(Integer prMasterID) throws VendorException {
		/*Prmaster*/
		//SELECT prMasterID,ItemCode,Description,IsTaxable,PurchasingUnitMultiplier,NewPrice FROM prMaster
		
		String aQry = "SELECT prMasterId,itemCode,Description,isTaxable,POMult,SalesPrice00 FROM prMaster WHERE prMasterId = "+prMasterID;
		Session aSession=null;
		List<Prmaster> alPrmaster = new ArrayList<Prmaster>();
		Prmaster objPrmaster = null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createSQLQuery(aQry);
			Iterator<?> aIterator = aQuery.list().iterator();
			while(aIterator.hasNext())
			{
				objPrmaster = new Prmaster();
				Object[] aObj = (Object[])aIterator.next();
				objPrmaster.setPrMasterId(Integer.valueOf(((Integer)aObj[0])));	
				objPrmaster.setItemCode((String) aObj[1]);	
				objPrmaster.setDescription((String) aObj[2]);	
				objPrmaster.setIsTaxable( new Byte((aObj[3]).toString()));	
				//objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				if(null == aObj[4])
				{
					/*String sPOMulti = (String)aObj[4];
					if(null == sPOMulti || "null".equalsIgnoreCase(sPOMulti) || "".equalsIgnoreCase(sPOMulti))
					{*/
						objPrmaster.setPOMult(new BigDecimal(0));
//					}
//					else
//						objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				}
				else
				{
					objPrmaster.setPOMult(new BigDecimal(aObj[4].toString()));
				}
				if(null == aObj[5])
				{
				/*	String sLastPrice = (String)aObj[5];
					if(null == sLastPrice || "null".equalsIgnoreCase(sLastPrice) || "".equalsIgnoreCase(sLastPrice))
					{*/
						objPrmaster.setLastCost(new BigDecimal(0));
//					}
//					else
//						objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
				else
				{
					objPrmaster.setLastCost(new BigDecimal(aObj[5].toString()));
				}
					
				alPrmaster.add(objPrmaster);
			}
			//if(aQuery.list().size() > 0)
			return alPrmaster;
			/*else
				return null;*/
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	@Override
	public Rxaddress getCustomerAddressdetails(Integer rxMasterID) throws VendorException {
		
		//String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,cotax.county,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxmaster ON rxmaster.rxMasterID = address.rxMasterID JOIN CoTaxTerritory AS cotax ON cotax.coTaxTerritoryId = address.coTaxTerritoryId WHERE address.rxMasterID = "+rxMasterID;
		Session aSession=null;
		Rxaddress objRxaddress = null;
		String aQry = null;
		try{
			aSession=itsSessionFactory.openSession();
			
			
				aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,address.City,address.State,address.Zip,address.rxAddressId FROM rxMaster rxmaster, rxAddress address WHERE  rxmaster.rxMasterID = address.rxMasterID AND rxmaster.rxMasterID ="+rxMasterID;
				itsLogger.info("aQry"+aQry);
				Query aQuery = aSession.createSQLQuery(aQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				if(aIterator.hasNext())
				{
					objRxaddress = new Rxaddress();
					Object[] aObj = (Object[])aIterator.next();
					objRxaddress.setName((String) aObj[0]);
					objRxaddress.setAddress1((String) aObj[1]);
					objRxaddress.setAddress2((String) aObj[2]);
					objRxaddress.setCity((String) aObj[3]);
					objRxaddress.setState((String) aObj[4]);
					objRxaddress.setZip((String) aObj[5]);
					objRxaddress.setRxAddressId((Integer) aObj[6]);
				}
				return objRxaddress;
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
		
	}
	
	@Override
	public List<Cumaster> getJobCUInvoiceEmailList(Integer rxMasterID) throws VendorException {
		
		//String sQuery = "SELECT rxContactID,EMail FROM rxContact where rxMasterID = :rxMasterID";
		String sQuery = "FROM Cumaster where cuMasterId = :cuMasterId";
		Session aSession = null;
		try
		{
			aSession = itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(sQuery).setParameter("cuMasterId", rxMasterID);
//			if(aQuery.list().size() > 0)
//				return aQuery.list();
			List<?> list = aQuery.list();
			if(list.size() > 0)
				return (List<Cumaster>)list;
			else
				return null;
		}
		catch(Exception e)
		{
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		
	}

	@Override
	public Integer updatePOStatus(Integer vePoId, Integer status)
			throws VendorException {
		Session aSession = itsSessionFactory.openSession();
		Vepo aVepo = null;
		Transaction aTransaction;
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			aVepo = (Vepo) aSession.get(Vepo.class, vePoId);
			Integer prevStatus = aVepo.getTransactionStatus(); 
			aVepo.setTransactionStatus(status);
			aSession.update(aVepo);
			aTransaction.commit();
			//checkSalesOrderInvoice(vePoId,status,prevStatus);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		return 1;
	}
	
	
	@Override
	public Vepodetail getPOinvoiceStatus(Integer vePoId) throws VendorException {
		
		//String aQry = "SELECT rxmaster.Name,address.Address1,address.Address2,cotax.county,address.State,address.Zip,address.rxAddressId FROM rxAddress AS address JOIN rxmaster ON rxmaster.rxMasterID = address.rxMasterID JOIN CoTaxTerritory AS cotax ON cotax.coTaxTerritoryId = address.coTaxTerritoryId WHERE address.rxMasterID = "+rxMasterID;
		Session aSession=null;
		Vepodetail objVepo = null;
		String aQry = null;
		try{
			aSession=itsSessionFactory.openSession();
			
			
				aQry = "SELECT COUNT(a.QuantityOrdered) AS acount ,COUNT(b.QuantityBilled) AS bcount FROM " +
						"vePODetail a LEFT JOIN veBillDetail b ON a.vePODetailID = b.vePODetailID WHERE a.vepoid="+vePoId;
				itsLogger.info("aQry"+aQry);
				Query aQuery = aSession.createSQLQuery(aQry);
				Iterator<?> aIterator = aQuery.list().iterator();
				if(aIterator.hasNext())
				{
					objVepo = new Vepodetail();
					Object[] aObj = (Object[])aIterator.next();
					objVepo.setOrderedCount((BigInteger) aObj[0]);
					objVepo.setBilledCount((BigInteger) aObj[1]);					
				}
				return objVepo;
			
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry=null;
		}	
		
	}

	@Override
	public List<TsUserLogin> getOrderedByLoginName() throws VendorException {

		String aQry = "FROM TsUserLogin WHERE inactive=0 order by FullName";
		Session aSession=null;
		try{
			aSession=itsSessionFactory.openSession();
			Query aQuery = aSession.createQuery(aQry);
			List<?> list = aQuery.list();
//			if(aQuery.list().size() > 0)
//			return aQuery.list();
			if(list.size() > 0)
				return (List<TsUserLogin>)list;
			else
				return null;
		} catch(Exception e) {
			itsLogger.error(e.getMessage(),e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
			aQry = null;
		}	
	}

	@Override
	public Integer getReceiveID(Integer vePOID) throws VendorException {
		
		Integer receiveID = 0;
		Session aSession = null;
		try
		{
			aSession = itsSessionFactory.openSession();
			receiveID = (Integer) aSession.createSQLQuery("select veReceiveID FROM  veReceive WHERE vePOID="+vePOID+" order by veReceiveID desc limit 1").uniqueResult();
		}
		catch(Exception e)
		{
			itsLogger.error(e.getMessage(), e);
			throw new VendorException(e.getMessage(), e);
		} finally {
			aSession.flush();
			aSession.close();
		}
		return receiveID;
	}
	
	@Override
	public Rxaddress getSelectedShiptoaddress(String type,Integer addressid) throws VendorException {
		Rxaddress aRxaddressList = null;
		
		Session aSession = itsSessionFactory.openSession();
		Query aQuery = null;
		try {
				
			if(type.equals("warehouse"))
			{
			aQuery = aSession.createSQLQuery("SELECT prWarehouseID,Description,Address1,Address2,City,State,Zip,coTaxTerritoryID FROM prWarehouse where prWarehouseID="+addressid);
			}
			else if(type.equals("customer"))
			{
			aQuery = aSession.createSQLQuery("Select rxA.rxAddressId,rxM.Name,rxA.address1,rxA.address2,rxA.city,rxA.state,rxA.zip,rxA.coTaxTerritoryID from rxAddress rxA,rxMaster rxM where rxM.rxMasterId =rxA.rxMasterId and rxA.rxAddressId="+ addressid);
			}
			else if(type.equals("jobsite"))
			{
			aQuery =  aSession.createSQLQuery("Select joMasterID,rm.Name,LocationAddress1,LocationAddress2,LocationCity,LocationState,LocationZip,coTaxTerritoryID,LocationName from joMaster jm,rxMaster rm WHERE rm.rxMasterID=jm.rxCustomerID and joMasterID = "+addressid);	
			}
			else
			{
			aQuery = aSession.createSQLQuery("Select rxA.rxAddressId,rxA.Name,rxA.address1,rxA.address2,rxA.city,rxA.state,rxA.zip,rxA.coTaxTerritoryID from rxAddress rxA where  rxA.rxAddressId="+ addressid);	
			}

			Iterator<?> aIterator = aQuery.list().iterator();
			while (aIterator.hasNext()) {
				aRxaddressList = new Rxaddress();
				Object[] aObj = (Object[]) aIterator.next();
				aRxaddressList.setRxAddressId((Integer)aObj[0]);
				aRxaddressList.setName((String)aObj[1]);
				aRxaddressList.setAddress1((String)aObj[2]);
				aRxaddressList.setAddress2((String)aObj[3]);
				aRxaddressList.setCity((String)aObj[4]);
				aRxaddressList.setState((String)aObj[5]);
				aRxaddressList.setZip((String)aObj[6]);
				aRxaddressList.setCoTaxTerritoryId((Integer)aObj[7]);
				if(type.equals("jobsite")){
					if(aObj[8]!=null && String.valueOf(aObj[8]).trim().length()>0){
						aRxaddressList.setName((String)aObj[8]);
					}
				}
			}
			
		} catch (Exception excep) {
			itsLogger.error(excep.getMessage(), excep);
			VendorException aVendorException = new VendorException(excep.getMessage(),
					excep);
			throw aVendorException;
		} finally {
			aSession.flush();
			aSession.close();
		}
		
		return aRxaddressList;
	}

	@Override
	public List<Cuinvoice> getAllcuInvoiceEmailList(String query) {
	
		Cuinvoice cuInvoiceEmailList=null;
		//System.out.println("Query"+query);
		Query aQuery = null;
		Session aSession = itsSessionFactory.openSession();
		
		aQuery=aSession.createSQLQuery(query);
		//List<?> list = aQuery.list();
		ArrayList<Cuinvoice> aQryList = new ArrayList<Cuinvoice>();
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			cuInvoiceEmailList = new Cuinvoice();
			Object[] aObj = (Object[]) aIterator.next();
			cuInvoiceEmailList.setCuInvoiceId( (Integer) aObj[0]);
			
			if(aObj[1] != null)
			{
				Timestamp stamp = (Timestamp)aObj[1];
				Date date = new Date(stamp.getTime());
				cuInvoiceEmailList.setCreatedOn(date); //(Date) aObj[1]
			}
			else
			cuInvoiceEmailList.setCreatedOn((Date)aObj[1] ); //(Date) aObj[1]	
			
			cuInvoiceEmailList.setCustomerPonumber((String) aObj[2]);
			cuInvoiceEmailList.setInvoiceNumber( (String) aObj[3]);
			cuInvoiceEmailList.setJoReleaseDetailId( (Integer) aObj[4]);
			cuInvoiceEmailList.setJobname((String) aObj[5]);
			cuInvoiceEmailList.setRxCustomerId( (Integer) aObj[6]);
			cuInvoiceEmailList.setCuSoid( (Integer) aObj[7]);
			cuInvoiceEmailList.setSubtotal( (BigDecimal) aObj[8]);
			cuInvoiceEmailList.setInvoiceAmount( (BigDecimal) aObj[9]);
			cuInvoiceEmailList.setcIopenStatus((boolean) aObj[10]);
			
			if(aObj[11] != null)
			{
				Timestamp stamp = (Timestamp)aObj[11];
				Date date = new Date(stamp.getTime());
				cuInvoiceEmailList.setSentEmailDate((String) DateFormatUtils
						.format(date, "MM/dd/yy hh:mm a"));
			}
			else
			cuInvoiceEmailList.setSentEmailDate((String) aObj[11]); 
			
			
			if(aObj[12] != null)
			{
				Timestamp stamp = (Timestamp)aObj[12];
				Date date = new Date(stamp.getTime());
				cuInvoiceEmailList.setInvoiceDate(date);
			}
			else
				cuInvoiceEmailList.setInvoiceDate((Date)aObj[12] );
			
			    cuInvoiceEmailList.setQuickJobName((String) aObj[13]);
			    cuInvoiceEmailList.setEmailStatus((Integer) aObj[14]);
			
			//cuInvoiceEmailList.setDescription((String) aObj[13]);
			String emailList=itscustomerService.getAllCustomerEmail(cuInvoiceEmailList.getRxCustomerId(),0);
			cuInvoiceEmailList.setEmailList(emailList);
			aQryList.add(cuInvoiceEmailList);
		
		}
		
		
		System.out.println("Size====>"+aQryList.size());
	return aQryList;
	}

	@Override
	public void updateEmailInvoiceDate(Integer cuInvoiceId,Integer invORstmt,Boolean emailStatus) {
		
		Session aSession = itsSessionFactory.openSession();
		Cuinvoice cinvoice = null;
		Transaction aTransaction;
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp currentDate=new java.sql.Timestamp(utilDate.getTime());
	  //  java.sql.Date currentDate = new java.sql.Date(utilDate.getTime());
	    System.out.println("currentDate"+currentDate);
		try {
			aTransaction = aSession.beginTransaction();
			aTransaction.begin();
			cinvoice =  (Cuinvoice) aSession.get(Cuinvoice.class, cuInvoiceId);
			
			if(invORstmt==0 && emailStatus==true ){
				cinvoice.setEmailDate(currentDate);
				cinvoice.setEmailStatus(1);
			}			
			else if(invORstmt==0 && emailStatus==false){
				cinvoice.setEmailDate(currentDate);
				cinvoice.setEmailStatus(2);
			}
			else if(invORstmt==1){
				cinvoice.setStatementEmailDate(currentDate);
			}
			
			aSession.update(cinvoice);
			aTransaction.commit();
			//checkSalesOrderInvoice(vePoId,status,prevStatus);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);

		} finally {
			aSession.flush();
			aSession.close();
		}
		
		
		
	}

	@Override
	public List<Cuinvoice> getAllcuStatementEmailList(String query) {
	
		Cuinvoice cuStatementEmailList=null;
		//System.out.println("Query"+query);
		Query aQuery = null;
		Session aSession = itsSessionFactory.openSession();
		
		aQuery=aSession.createSQLQuery(query);
		//List<?> list = aQuery.list();
		ArrayList<Cuinvoice> aQryList = new ArrayList<Cuinvoice>();
		Iterator<?> aIterator = aQuery.list().iterator();
		while (aIterator.hasNext()) {
			cuStatementEmailList = new Cuinvoice();
			Object[] aObj = (Object[]) aIterator.next();
			
			
			if(aObj[0] != null)
			{
				Timestamp stamp = (Timestamp)aObj[0];
				Date date = new Date(stamp.getTime());
				cuStatementEmailList.setStatementEmailDate(date); //(Date) aObj[1]
			}
			else
			cuStatementEmailList.setStatementEmailDate((Date)aObj[0] ); //(Date) aObj[1]	
			
			cuStatementEmailList.setCuInvoiceId( (Integer) aObj[1]);
			cuStatementEmailList.setInvoiceNumber( (String) aObj[2]);
			cuStatementEmailList.setRxCustomerId( (Integer) aObj[3]);
			cuStatementEmailList.setQuickJobName((String) aObj[4]);
			
			String emailList=itscustomerService.getAllCustomerEmail(cuStatementEmailList.getRxCustomerId(),1);
			cuStatementEmailList.setEmailList(emailList);
			aQryList.add(cuStatementEmailList);
		
		}
		
		
		
		
		return aQryList;
	}

	
	
}
