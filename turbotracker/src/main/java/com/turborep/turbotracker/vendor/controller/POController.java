package com.turborep.turbotracker.vendor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cuinvoice;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JobPurchaseOrderBean;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Veshipvia;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
public class POController {

	Logger itsLogger = Logger.getLogger(POController.class);

	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;

	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name = "userLoginService")
	private UserService userService;

	@Resource(name = "companyService")
	private CompanyService companyService;

	@Resource(name = "vendorService")
	private VendorServiceInterface vendorServiceInterface;
	
	
	private Integer itsVePoID;

	private JobPurchaseOrderBean itsJobPurchaseOrderBean;

	private String itsManufactureName;

	@RequestMapping(value = "/purchaseorder", method = RequestMethod.GET)
	public String purchaseOrder(
			@RequestParam(value = "aVePOId", required = false) Integer aVePOId,
			HttpServletRequest theRequest) {
		itsLogger.debug("purchaseOrder");
		itsVePoID = aVePOId;
		if (!SessionConstants.checkSessionExist(theRequest)) {
			return "welcome";
		}
		return "purchaseOrder";
	}

	@RequestMapping(value = "/po_listgrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAll(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletResponse response, HttpSession session,HttpServletRequest therequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all PO List");
		CustomResponse aResponse = null;
		 String fromdate = "";
		 String todate = "";
		 SimpleDateFormat sdf =null;
		 SimpleDateFormat sdff =null;
		 Date convertedCurrentDate1 =null;
		 Date convertedCurrentDate2 =null;
		 StringBuilder aPOSelectQry =null;
		try {//fromdate,todate
			
			  sdf = new SimpleDateFormat("MM/dd/yyyy");
			  sdff = new SimpleDateFormat("yyyy-MM-dd");
			 
			if(fromDate!=null && !fromDate.trim().equals("")){
				
			    convertedCurrentDate1 = sdf.parse(fromDate);
			    fromdate=sdff.format(convertedCurrentDate1);
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDate!=null && !toDate.trim().equals("")){
			    convertedCurrentDate2 = sdf.parse(toDate);
			    todate=sdff.format(convertedCurrentDate2);
			    System.out.println("Formated End Date:"+todate);
			}
			
			aPOSelectQry = new StringBuilder("SELECT count(vp.vePOID) ")
							.append("FROM vePO vp LEFT JOIN rxMaster rm ON vp.rxVendorID = rm.rxMasterID LEFT JOIN joRelease jo ON jo.joReleaseID =  vp.joReleaseID LEFT JOIN joMaster AS mas ON jo.joMasterID = mas.joMasterID ");
							//.append("WHERE vp.vePOID LIKE '%'");
			if(searchData!=null && !searchData.equals("")){
				aPOSelectQry.append(" WHERE (vp.PONumber LIKE '%"+searchData+"%' OR vp.joReleaseID LIKE '%"+searchData+"%' " +" OR rm.Name LIKE '%"+searchData+"%' " +" OR vp.subtotal LIKE '%"+searchData+"%' )");
				}
			if(!fromdate.equals("")&& !todate.equals("")){
				if(aPOSelectQry.indexOf("WHERE")>0)
					aPOSelectQry.append(" and vp.CreatedOn BETWEEN '"+fromdate +" 00:00:00' AND '"+todate+" 23:59:59'");
				else
					aPOSelectQry.append(" WHERE vp.CreatedOn BETWEEN '"+fromdate +" 00:00:00' AND '"+todate+" 23:59:59'");
				
			}else if(!fromdate.equals("") && todate.equals("")){
			if(aPOSelectQry.indexOf("WHERE")>0)
				aPOSelectQry.append(" and vp.CreatedOn >='"+fromdate+" 00:00:00'");
			else
				aPOSelectQry.append(" WHERE vp.CreatedOn >='"+fromdate+" 00:00:00'");
			}else if(!todate.equals("") && fromdate.equals("")){
			if(aPOSelectQry.indexOf("WHERE")>0)
				aPOSelectQry.append(" and vp.CreatedOn<='"+todate+" 23:59:59'");
			else
				aPOSelectQry.append(" WHERE vp.CreatedOn<='"+todate+" 23:59:59'");
			}
			
			int aTotalCount = companyService.getNewQueryCount(aPOSelectQry.toString());
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			
			
			
			List<?> aPOs = itsPurchaseService.getAllPOsList(aFrom, aTo,searchData,fromdate,todate,theSidx,theSord);
			// List<?> aPOs = itsPurchaseService.getAllPOsList(aTotalCount-100,
			// aTotalCount);
			aResponse = new CustomResponse();
			aResponse.setRows(aPOs);
			aResponse.setRecords(String.valueOf(aPOs.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (VendorException excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getCause()
					.getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData ,"POController",excep,session,therequest);
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData ,"POController",excep,session,therequest);
		}finally{
			 fromdate = null;
			 todate = null;
			 sdf =null;
			 sdff =null;
			 convertedCurrentDate1 =null;
			 convertedCurrentDate2 =null;
			 aPOSelectQry =null;
		}

		return aResponse;
	}

	@RequestMapping(value = "/purchaseOrderGeneral", method = RequestMethod.GET)
	public ModelAndView purchaseOrderGeneral(HttpServletResponse response, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		itsLogger.debug("purchaseOrderGeneral");
		// JobPurchaseOrderBean aJobPurchaseOrderBean = new
		// JobPurchaseOrderBean();
		ModelAndView theView = new ModelAndView("purchaseOrderGeneral");
		if (!SessionConstants.checkSessionExist(therequest)) {
			return new ModelAndView("welcome");
		}

		try {
			if (itsVePoID != null)
				itsJobPurchaseOrderBean = itsPurchaseService
						.getPurchaseDetails(itsVePoID);
			theView.addObject("theVepo", itsJobPurchaseOrderBean);
			theView.addObject("shipVia", jobService.getVeShipVia());
			theView.addObject("wareHouse", jobService.getWareHouse());
			Integer theRxMasterId = itsPurchaseService.getRxMasterID(itsVePoID);
			TsUserSetting aSetting = userService
					.getSingleUserSettingsDetails(1);
			theView.addObject("aSetting", aSetting);
			Rxaddress theRxaddress = null;
			if (itsVePoID != null) {
				theRxaddress = itsPurchaseService
						.getRxaddress(itsJobPurchaseOrderBean.getRxBillToID());
				theView.addObject("theRxaddress", theRxaddress);
				theView.addObject("billToName", itsPurchaseService
						.getManufactureName(itsJobPurchaseOrderBean
								.getRxBillToID()));

			}

			theView.addObject("theRxMasterId", theRxMasterId);
			List<Rxcontact> getBuddiesList = itsPurchaseService
					.getContactList(theRxMasterId);
			theView.addObject("buddiesList", getBuddiesList);
			itsManufactureName = itsPurchaseService
					.getManufactureName(theRxMasterId);
			theView.addObject("manufactureName", itsManufactureName);
			theView.addObject("friedCharges", jobService.getVefreightcharges());
			jobService.getVeShipVia();
		} catch (JobException e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>purchaseOrderGeneral:</b>","POController",e,session,therequest);
			return theView;
		} catch (VendorException e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>purchaseOrderGeneral:</b>","POController",e,session,therequest);
			return theView;
		} catch (UserException e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>purchaseOrderGeneral:</b>","POController",e,session,therequest);
			return theView;
		}
		return theView;
	}

	@RequestMapping(value = "/purchaseOrderLines", method = RequestMethod.GET)
	public ModelAndView purchaseOrderLines() {
		ModelAndView theView = new ModelAndView("purchaseOrderLines");
		theView.addObject("aJobPurchaseOrderBean", itsJobPurchaseOrderBean);
		theView.addObject("manufactureName", itsManufactureName);
		theView.addObject("aVePoID", itsVePoID);
		System.out.println(itsVePoID);

		return theView;
	}

	@RequestMapping(value = "/purchaseOrderAck", method = RequestMethod.GET)
	public ModelAndView purchaseOrderAck() {
		ModelAndView theView = new ModelAndView("purchaseOrderAck");
		theView.addObject("aJobPurchaseOrderBean", itsJobPurchaseOrderBean);
		theView.addObject("manufactureName", itsManufactureName);
		theView.addObject("aVePoID", itsVePoID);
		return theView;
	}

	@RequestMapping(value = "/getBillToName", method = RequestMethod.GET)
	public @ResponseBody
	String getBilltoAddress(
			@RequestParam(value = "therxMasterId", required = false) Integer therxMasterId,HttpServletResponse response, HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		String aBillToName = null;
		try {
			if (therxMasterId != null) {
				aBillToName = itsPurchaseService
						.getManufactureName(therxMasterId);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>therxMasterId:</b>"+therxMasterId ,"POController",e,session,therequest);
		}
		return aBillToName;
	}

	// To add PO
	@RequestMapping(value = "/addpurchaseorder", method = RequestMethod.GET)
	public ModelAndView addpurchaseOrder(HttpServletResponse response, HttpSession session,HttpServletRequest theRequest,ModelMap theModel) throws IOException, MessagingException {
		itsLogger.debug("addpurchaseorder");
		ModelAndView theView = null;
		String FromPage=theRequest.getParameter("FromPage");
		
		if(FromPage!=null && FromPage.equalsIgnoreCase("Home")){
					theView=new ModelAndView("addpurchaseorderfromquickbook");
				}else{
					theView=new ModelAndView("addpurchaseorder");
				}
				
		TsUserSetting aUserLoginSetting = null;
		try {

			System.out.println("Current Date ----->" + new Date());
			theView.addObject("currentDate", new Date());
			aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
			theView.addObject("billtoaddress", aUserLoginSetting);
			theView.addObject("shiptoaddress",
					itsPurchaseService.getBilltoAddress());
			List<Object> alPrwarehouse = (List<Object>) jobService
					.getWareHousewithtaxtertory();
			List<Prwarehouse> alTaxPrwarehouse = new ArrayList<Prwarehouse>();
			CoTaxTerritory acotTerritory = null;
			
			
				Iterator<?> aIterator = alPrwarehouse.iterator();
				while (aIterator.hasNext()) {
					Object[] pair = (Object[]) aIterator.next();
					Prwarehouse probj = (Prwarehouse) pair[0];
					CoTaxTerritory acotTerritoryobj = (CoTaxTerritory) pair[1];
					probj.setEmail(acotTerritoryobj.getTaxRate().toString());
					alTaxPrwarehouse.add(probj);
				}
			
			/*for (Prwarehouse al : alPrwarehouse) {
				acotTerritory = new CoTaxTerritory();
				try {
					acotTerritory = itsPurchaseService.getSingleTaxTerritory(al
							.getCoTaxTerritoryId());
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				}
				al.setEmail(acotTerritory.getTaxRate().toString());
				alTaxPrwarehouse.add(al);

			}*/
			theModel.addAttribute("wareHouse", alTaxPrwarehouse);
			theModel.addAttribute("theRxaddress", new ArrayList());
			theView.addObject("orderedByNames",	itsPurchaseService.getOrderedByLoginName());
			String ourpo=theRequest.getParameter("OurPO");
			String customerpo=theRequest.getParameter("POnumber");
			String jobname=theRequest.getParameter("jobName");
			String Customername=theRequest.getParameter("CustomerName");
			String customerid=theRequest.getParameter("CustomerId");
			//Location1,Location2,LocationCity,LocationState,LocationZip
			String Location1=theRequest.getParameter("Location1");
			String Location2=theRequest.getParameter("Location2");
			String LocationCity=theRequest.getParameter("LocationCity");
			String LocationState=theRequest.getParameter("LocationState");
			String LocationZip=theRequest.getParameter("LocationZip");
			String taxterritory=theRequest.getParameter("TaxTerritory");
			double tax=0.0;
			try {
				tax=Double.parseDouble(taxterritory);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			Integer cusoid=0;
			
			try {
				 cusoid=Integer.parseInt(customerid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			Integer joreleaseid=0;
			try {
				 joreleaseid=Integer.parseInt(theRequest.getParameter("JoReleaseId"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			
			
			Rxaddress rxadr=itsPurchaseService.getCustomerAddressdetails(cusoid);
			itsLogger.info("frompage==>"+theRequest.getParameter("FromPage")+"ourpo==>"+theRequest.getParameter("OurPO")+" Customerpo==>"+theRequest.getParameter("POnumber"));
			itsLogger.info("rxadr"+rxadr);
			
			if(FromPage!=null && FromPage.equalsIgnoreCase("Home")){  // Added null check - Naveed
			theView.addObject("OurPO",ourpo);
			theView.addObject("POnumber",customerpo);
			theView.addObject("JobName",jobname);
			theView.addObject("Customername",Customername);
			theView.addObject("rxcustomerid",customerid);
			theView.addObject("Rxaddress",rxadr);
			theView.addObject("JoReleaseId",joreleaseid);
			//Location1,Location2,LocationCity,LocationState,LocationZip
			theView.addObject("Location1",Location1);
			theView.addObject("Location2",Location2);
			theView.addObject("LocationCity",LocationCity);
			theView.addObject("LocationState",LocationState);
			theView.addObject("LocationZip",LocationZip);
			theView.addObject("TaxTerritory",tax);
			}
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>addpurchaseOrder:</b>","POController",e,session,theRequest);
		} catch (VendorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>addpurchaseOrder:</b>","POController",e,session,theRequest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sendTransactionException("<b>addpurchaseOrder:</b>","POController",e,session,theRequest);
		}
		if (!SessionConstants.checkSessionExist(theRequest)) {
			return new ModelAndView("welcome");
		}
		return theView;
	}

	@RequestMapping(value = "/freightCharges", method = RequestMethod.GET)
	public @ResponseBody
	Map<String, ArrayList<?>> getfreightCharges(HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		ArrayList<Vefreightcharges> alVefreightcharges = null;
		ArrayList<Veshipvia> alVeshipvia = null;
		ArrayList<Rxcontact> getBuddiesList = new ArrayList<Rxcontact>();
		try {
			alVefreightcharges = (ArrayList<Vefreightcharges>) jobService
					.getVefreightcharges();
			alVeshipvia = (ArrayList<Veshipvia>) jobService.getVeShipVia();
		//	getBuddiesList = (ArrayList<Rxcontact>) itsPurchaseService.getContactList();

			map.put("frtCharges", alVefreightcharges);
			map.put("shipvia", alVeshipvia);
			map.put("buddiestList", getBuddiesList);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>freightCharges:</b>","POController",e,session,theRequest);
		}
		return map;

	}

	@RequestMapping(value = "/getManufactureATTN", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getManufacturerATTN(
			@RequestParam(value = "rxMasterId") Integer rxMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			map.put("vendorAddress", (ArrayList<Rxaddress>) itsPurchaseService.getCustomerAddresses(rxMasterId));
			map.put("manufacturerNames",(ArrayList<VeFactory>) itsPurchaseService.getManufactureNames(rxMasterId));
			map.put("attnNames", (ArrayList<Rxcontact>) itsPurchaseService.getATTNName(rxMasterId));
			//itsLogger.info("Query Size: "+itsPurchaseService.getCustomerAddresses(rxMasterId).size());
			map.put("vendorName", (ArrayList<Rxaddress>) itsPurchaseService.getVendorName(rxMasterId));
			
			ArrayList<Integer> veMasterlist = new ArrayList<Integer>();
			Vemaster veMaster = vendorServiceInterface.gettermsDiscountsfromveMaster(rxMasterId);
			
			if(veMaster!=null)
			{
				veMasterlist.add(veMaster.getDueDays());
				map.put("dueonDays",veMasterlist);
			}
			else
			{
				veMasterlist.add(0);
				map.put("dueonDays",veMasterlist);
			}
		
			//itsLogger.info("Q"+map);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"POController",e,session,theRequest);
		}
		return map;

	}

	
	
	
	
	@RequestMapping(value = "/getCustomerAddress", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getCustomerAddress(
			@RequestParam(value = "rxMasterId") Integer rxMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Rxaddress> alRxaddress = (ArrayList<Rxaddress>) itsPurchaseService
					.getCustomerAddress(rxMasterId);
			ArrayList<String> alTaxRate = new ArrayList<String>();
			ArrayList<String> alTaxTerritory = new ArrayList<String>();
			ArrayList<String> alTaxTerritoryID = new ArrayList<String>();
			map.put("customerAddress", alRxaddress);
			CoTaxTerritory acotTerritory = null;
			for (Rxaddress al : alRxaddress) {
				acotTerritory = new CoTaxTerritory();
				try {
					if (al.getCoTaxTerritoryId() != null)
						acotTerritory = itsPurchaseService
								.getSingleTaxTerritory(al.getCoTaxTerritoryId());
					else {
						acotTerritory.setTaxRate(new BigDecimal(0.00));
						acotTerritory.setCounty("");
						acotTerritory.setCoTaxTerritoryId(0);
					}
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
					sendTransactionException("<b>rxMasterId:</b>"+rxMasterId,"POController",e,session,theRequest);
				}
				if (null == acotTerritory.getTaxRate()) {
					acotTerritory.setTaxRate(new BigDecimal(0.00));
				}
				if (null == acotTerritory.getCounty()) {
					acotTerritory.setCounty("");
				}
				alTaxRate.add(acotTerritory.getTaxRate().toString());
				alTaxTerritory.add(acotTerritory.getCounty().toString());
				alTaxTerritoryID.add(acotTerritory.getCoTaxTerritoryId()
						.toString());
				map.put("taxRateforCity", alTaxRate);
				map.put("taxTerritory", alTaxTerritory);
				map.put("taxTerritoryID", alTaxTerritoryID);

			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>rxMasterId:</b>"+rxMasterId ,"POController",e,session,theRequest);
		}
		return map;

	}

	@RequestMapping(value = "/getLineItems", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getLineItems(
			@RequestParam(value = "prMasterId") Integer prMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			map.put("lineItems", (ArrayList<Prmaster>) itsPurchaseService
					.getLineItems(prMasterId));
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>prMasterId:</b>"+prMasterId,"POController",e,session,theRequest);
		}
		return map;

	}

	@RequestMapping(value = "/getLineItemsSO", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getLineItemsSO(
			@RequestParam(value = "prMasterId") Integer prMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			map.put("lineItems", (ArrayList<Prmaster>) itsPurchaseService
					.getLineItemsSO(prMasterId));
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>prMasterId:</b>"+prMasterId ,"POController",e,session,theRequest);
		}
		return map;

	}
	
	@RequestMapping(value = "/getRxMasterName", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getRxMasterName(
			@RequestParam(value = "rxMasterId") Integer prMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			map.put("lineItems", (ArrayList<Rxmaster>) itsPurchaseService
					.getRxMasterName(prMasterId));
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>prMasterId:</b>"+prMasterId ,"POController",e,session,theRequest);
		}
		return map;
 
	}

	// Added for PO Edit Screen

	@RequestMapping(value = "/editpurchaseorder", method = RequestMethod.GET)
	public ModelAndView editpurchaseorder(
			@RequestParam(value = "aVePOId", required = false) Integer aVePOId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException, CompanyException {
		ModelAndView theView = new ModelAndView("editpurchaseorder");
		if (!SessionConstants.checkSessionExist(theRequest)) {
			return new ModelAndView("welcome");
		}

		try {
			if (aVePOId != null)
				itsJobPurchaseOrderBean = itsPurchaseService.getPurchaseDetails(aVePOId);
			
			// For Ship to address
			Integer jomasterid = itsPurchaseService.getRxCustomerID(itsJobPurchaseOrderBean.getJoReleaseId());
			Jomaster joMasterID = new Jomaster();
			if(jomasterid!=null)
			{
			joMasterID = jobService.getSingleJobDetails(jomasterid);
			joMasterID.setDescription(itsPurchaseService.getVendor(joMasterID.getRxCustomerId()));
			theView.addObject("theNewShiptocusaddress",companyService.getAllAddress(joMasterID.getRxCustomerId()));
			theView.addObject("joMasterDetails", joMasterID);
			theView.addObject("rxCustomerID", joMasterID.getRxCustomerId());
			}
			else
			{
				theView.addObject("theNewShiptocusaddress",null);
				theView.addObject("rxCustomerID", itsPurchaseService.getRxCustomerID(itsJobPurchaseOrderBean.getJoReleaseId()));
			}
			
			theView.addObject("theVepo", itsJobPurchaseOrderBean);
			theView.addObject("billtoIndex",
					itsJobPurchaseOrderBean.getBillTo());
			theView.addObject("shiptoIndex",
					itsJobPurchaseOrderBean.getShipTo());

			theView.addObject("shipVia", jobService.getVeShipVia());
			List<Prwarehouse> alPrwarehouse = (List<Prwarehouse>) jobService
					.getWareHouse();
			List<Prwarehouse> alTaxPrwarehouse = new ArrayList<Prwarehouse>();
			CoTaxTerritory acotTerritory = null;
			for (Prwarehouse al : alPrwarehouse) {
				acotTerritory = new CoTaxTerritory();
				try {
					acotTerritory = itsPurchaseService.getSingleTaxTerritory(al
							.getCoTaxTerritoryId());
				} catch (Exception e) {
					itsLogger.error(e.getMessage(), e);
				}
				if(acotTerritory.getTaxRate()!=null){
				al.setEmail(acotTerritory.getTaxRate().toString());
				}
				alTaxPrwarehouse.add(al);

			}
			theView.addObject("wareHouse", alTaxPrwarehouse);
			Integer theRxMasterId = itsPurchaseService.getRxMasterID(aVePOId);
			TsUserSetting aSetting = userService
					.getSingleUserSettingsDetails(1);
			theView.addObject("aSetting", aSetting);
			Rxaddress theRxaddress = null;
			
			if (aVePOId != null) {
				theRxaddress = itsPurchaseService.getCustomerAddressauto(itsJobPurchaseOrderBean.getRxBillToID());
				theView.addObject("theRxaddress", theRxaddress);
				
				theRxaddress = itsPurchaseService.getCustomerAddressauto(itsJobPurchaseOrderBean.getRxShipToId());
				
				
				if (theRxaddress != null) {
					if (theRxaddress.getCoTaxTerritoryId() != null) {
						try {
							acotTerritory = itsPurchaseService
									.getSingleTaxTerritory(theRxaddress
											.getCoTaxTerritoryId());
						} catch (CompanyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						theView.addObject("theShipToTaxRate",
								acotTerritory.getTaxRate());
					} else {
						theView.addObject("theShipToTaxRate", "");
					}
				}

				theView.addObject("theRxaddress", theRxaddress);
				theView.addObject("billToName", itsPurchaseService
						.getManufactureName(itsJobPurchaseOrderBean
								.getRxBillToID()));
				theView.addObject("shipToName", itsPurchaseService
						.getManufactureName(itsJobPurchaseOrderBean
								.getRxShipToId()));

			}
			if (itsJobPurchaseOrderBean.getRxBillToAddressId() != null) {
				theRxaddress = itsPurchaseService
						.getRxaddress(itsJobPurchaseOrderBean
								.getRxBillToAddressId());
				theView.addObject("billToOtherName", theRxaddress);
			}

			if (itsJobPurchaseOrderBean.getRxShipToAddressId() != null) {
				theRxaddress = itsPurchaseService
						.getRxaddress(itsJobPurchaseOrderBean
								.getRxShipToAddressId());
				if(theRxaddress!=null){
				if(theRxaddress.getName()!= null && theRxaddress.getName().length()>0){
				 int count = 0;
				    for (int i=0; i < theRxaddress.getName().length(); i++)
				    {
				        if (theRxaddress.getName().charAt(i) == '"')
				        {
				             count++;
				        }
				    }
				
				if(theRxaddress.getName().contains("\"")){
					if(count>0){
						theRxaddress.setName(theRxaddress.getName().replaceAll("\"", "\\\""));
					}else{
					theRxaddress.setName(theRxaddress.getName().replace("\"", "\\\""));
					}
				}
			}
				
				if(theRxaddress.getAddress1() !=null && theRxaddress.getAddress1().length()>0){
					 int count = 0;
					    for (int i=0; i < theRxaddress.getAddress1().length(); i++)
					    {
					        if (theRxaddress.getAddress1().charAt(i) == '"')
					        {
					             count++;
					        }
					    }
					
					if(theRxaddress.getAddress1().contains("\"")){
						if(count>0){
							theRxaddress.setAddress1(theRxaddress.getAddress1().replaceAll("\"", "\\\""));
						}else{
						theRxaddress.setAddress1(theRxaddress.getAddress1().replace("\"", "\\\""));
						}
					}
				}
				if(theRxaddress.getAddress2()!=null && theRxaddress.getAddress2().length()>0){
					 int count = 0;
					    for (int i=0; i < theRxaddress.getAddress2().length(); i++)
					    {
					        if (theRxaddress.getAddress2().charAt(i) == '"')
					        {
					             count++;
					        }
					    }
					
					if(theRxaddress.getAddress2().contains("\"")){
						if(count>0){
							theRxaddress.setAddress2(theRxaddress.getAddress2().replaceAll("\"", "\\\""));
						}else{
						theRxaddress.setAddress2(theRxaddress.getAddress2().replace("\"", "\\\""));
						}
					}
				}
			}
				theView.addObject("shipToOtherName", theRxaddress);
			}

			theView.addObject("theRxMasterId", theRxMasterId);
			List<Rxcontact> getBuddiesList = itsPurchaseService
					.getContactList(theRxMasterId);
			theView.addObject("buddiesList", getBuddiesList);
			itsManufactureName = itsPurchaseService
					.getManufacture(theRxMasterId);
			theView.addObject("manufactureName", itsManufactureName);
			theView.addObject("friedCharges", jobService.getVefreightcharges());
			theView.addObject("aVePoID", aVePOId);
			theView.addObject("aJobPurchaseOrderBean", itsJobPurchaseOrderBean);
			theView.addObject("vendorName",
					itsPurchaseService.getVendor(theRxMasterId));
			theView.addObject("aVePoID", aVePOId);
			List<?> aPOLineItemDetails = jobService.getPOLineDetails(aVePOId);
			theView.addObject("lineGridDetails", aPOLineItemDetails);
			theView.addObject("orderedByNames",
					itsPurchaseService.getOrderedByLoginName());
			theView.addObject("theRxMasterId", theRxMasterId);

			System.out.println(itsVePoID);
			jobService.getVeShipVia();
		} catch (JobException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>aVePOId:</b>"+aVePOId ,"POController",e,session,theRequest);
			return theView;
		} catch (VendorException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>aVePOId:</b>"+aVePOId ,"POController",e,session,theRequest);
			return theView;
		} catch (UserException e) {
			e.printStackTrace();
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>aVePOId:</b>"+aVePOId ,"POController",e,session,theRequest);
			return theView;
		}
		return theView;
	}

	@RequestMapping(value = "/getTaxTerritory", method = RequestMethod.POST)
	public @ResponseBody
	CoTaxTerritory getTaxTerritory(
			@RequestParam(value = "country") String country,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		CoTaxTerritory acotTerritory = new CoTaxTerritory();
		try {
			acotTerritory = itsPurchaseService.getSingleTaxTerritory(country);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>country:</b>"+country,"POController",e,session,theRequest);
		}
		return acotTerritory;

	}

	@RequestMapping(value = "/so_listgrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAllSOList(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all PO List: "+theSidx+"::"+theSord);
		CustomResponse aResponse = null;
		try {
			//int aTotalCount = itsPurchaseService.getSORecordsCount();
			
			//itsLogger.info("aTotalCount"+aTotalCount);
			
			if(searchData==null || searchData.equals("null")){
				searchData="";
			}
			 String fromdate = "";
			 String todate = "";
			if(fromDate!=null && !fromDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(fromDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    fromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDate!=null && !toDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(toDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    todate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+todate);
			}
		    
			/*String fromDate,
			String toDate,*/
			String query=getAllSOsListQuery(searchData,fromdate,todate);
			int aTotalCount=companyService.getNewQueryCount(query);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			List<?> aSOs = itsPurchaseService.getAllSOsList(aFrom, aTo,searchData,fromdate,todate,theSidx,theSord);
			aResponse = new CustomResponse();
			aResponse.setRows(aSOs);
			aResponse.setRecords(String.valueOf(aSOs.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (VendorException excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getCause()
					.getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData,"POController",excep,session,theRequest);
		} catch (Exception excep) {
			excep.printStackTrace();
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData,"POController",excep,session,theRequest);
		}

		return aResponse;
	}

	@RequestMapping(value = "/cuInvoice_listgrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAllcuInvoiceList(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all PO List "+theSidx+" sorder :"+theSord);
		CustomResponse aResponse = null;
		
		try {
			 String fromdate = "";
			 String todate = "";
			if(fromDate!=null && !fromDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(fromDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    fromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDate!=null && !toDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(toDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    todate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+todate);
			}
			int aTotalCount =0;
			if(searchData==null){
				searchData="";
			}
			 String query =getAllCuInvoicesListCountQuery(searchData,fromdate,todate);
		     aTotalCount = companyService.getCount(query);
			System.out.println("aTotalCount "+aTotalCount);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			
			List<?> aCuInvoices = itsPurchaseService.getAllCuInvoicesList(
					aFrom, aTo,theSidx,theSord,searchData,fromdate,todate);
			aResponse = new CustomResponse();
			aResponse.setRows(aCuInvoices);
			aResponse.setRecords(String.valueOf(aCuInvoices.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (VendorException excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getCause()
					.getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData ,"POController",excep,session,theRequest);
		} catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
		}

		return aResponse;
	}

	@RequestMapping(value = "/cuInvoice_EmailListgrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAllcuInvoice_EmailList(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "batchInvoiceCuID", required = false) String batchInvoiceCuID,
			@RequestParam(value = "batchInvoiceFromDate", required = false) String batchInvoiceFromDate,
			@RequestParam(value = "batchInvoiceToDate", required = false) String batchInvoiceToDate,
			@RequestParam(value = "rangeInvoiceFrom", required = false) String rangeInvoiceFrom,
			@RequestParam(value = "rangeInvoiceTo", required = false) String rangeInvoiceTo,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all PO List "+theSidx+" sorder :"+theSord);
		
		
		System.out.println("FromDate"+batchInvoiceFromDate+"ToDate"+batchInvoiceToDate);
		CustomResponse aResponse = null;
		
		try {
			String invoicefromdate=null;
			String invoicetodate=null;
			String yesterDayDate=JobUtil.getYesterdayDateString();
			
			if(batchInvoiceFromDate!=null && !batchInvoiceFromDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(batchInvoiceFromDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    invoicefromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+invoicefromdate);
			}
			if(batchInvoiceToDate!=null && !batchInvoiceToDate.trim().equals("")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = sdf.parse(batchInvoiceToDate);
			    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    invoicetodate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+invoicetodate);
			}
				  
		    String query ="SELECT "+
		    		"cuInvoice.cuInvoiceID,cuInvoice.CreatedOn,cuInvoice.CustomerPONumber,cuInvoice.InvoiceNumber,"+ 
		    		"cuInvoice.joReleaseDetailID,joMaster.Description,cuInvoice.rxCustomerID,cuInvoice.cuSOID,"+
		    		"cuInvoice.Subtotal,cuInvoice.InvoiceAmount,cuInvoice.cIopenStatus,cuInvoice.EmailDate,cuInvoice.InvoiceDate,rxMaster.Name,cuInvoice.emailStatus "+
		    		
		    		/*+ " (CASE WHEN cuInvoiceDetail.PriceMultiplier IS NULL THEN (CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END *cuInvoiceDetail.QuantityBilled) ELSE ((CASE WHEN cuInvoiceDetail.UnitCost IS NULL THEN 0 ELSE cuInvoiceDetail.UnitCost END )*cuInvoiceDetail.QuantityBilled*cuInvoiceDetail.PriceMultiplier )END ) AS total,"+
" joRelease.ReleaseType, tsUserLogin.Initials AS SalesRep,veShipVia.Description AS ShippedVia,"+
" cuInvoiceDetail.PriceMultiplier,cuInvoiceDetail.UnitCost,cuInvoiceDetail.QuantityBilled,"+
" joMaster.Description AS Job, cuInvoice.*, tsUserLogin.Initials,"+
" cuTerms.Description AS termsdesc,cuInvoice.ShipToMode AS ShipToMode,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address1,billto.address1) AS address1,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.address2,billto.address2) AS address2,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.city,billto.city) AS city,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.state,billto.state) AS state,"+
" IF(rxAddress.rxAddressID IS NOT NULL, rxAddress.zip,billto.zip) AS zip,rxMaster.Name,"+
//" rxAddress.address1,rxAddress.address2,rxAddress.city,rxAddress.state,rxAddress.zip,rxMaster.Name,"+
" cuInvoice.prToWarehouseID AS prwarehouseid,cuInvoice.rxCustomerID AS customerID,joMaster.joMasterID,"+
" cuInvoice.rxShipToAddressID "+*/
" FROM cuInvoice"+
" LEFT JOIN cuInvoiceDetail ON cuInvoiceDetail.cuInvoiceID = cuInvoice.cuInvoiceID"+
" LEFT JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID = cuInvoice.joReleaseDetailID"+
" LEFT JOIN joRelease ON joReleaseDetail.joReleaseID=joRelease.joReleaseID"+
" LEFT JOIN joMaster ON joMaster.joMasterID = joRelease.joMasterID"+
" LEFT JOIN cuTerms ON cuTerms.cuTermsID = cuInvoice.cuTermsID"+
" LEFT JOIN tsUserLogin ON tsUserLogin.UserLoginID = cuInvoice.cuAssignmentID0"+
" LEFT JOIN rxMaster ON rxMaster.rxMasterID=cuInvoice.rxCustomerID"+
" LEFT JOIN rxAddress ON (rxMaster.rxMasterID=rxAddress.rxMasterID AND rxAddress.IsMailing = 1)"+
" LEFT JOIN rxAddress billto ON (rxMaster.rxMasterID=billto.rxMasterID AND billto.IsDefault = 1)"+
" LEFT JOIN veShipVia ON (veShipVia.veShipViaID = cuInvoice.veShipViaID)"+ 
" LEFT JOIN cuMaster ON (rxMaster.rxMasterID= cuMaster.cuMasterID AND  cuMaster.InvoiceMethod= 2)";
		    
   
		    
if(batchInvoiceCuID.length()>0 && !batchInvoiceCuID.equals("0")){
	 if(!query.contains("WHERE")){
		 query=query+" WHERE  rxMaster.rxMasterID ="+batchInvoiceCuID;
		}else{
			query=query+" And  rxMaster.rxMasterID ="+batchInvoiceCuID;
		}
	
}

			
			if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0 && rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0){
				if(!query.contains("WHERE")){
					query=query+" WHERE Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2 GROUP BY cuInvoice.cuInvoiceID";
				}else{
					query=query+" AND Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}
				
			}else if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0){
				if(!query.contains("WHERE")){
				query=query+" WHERE  Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND cuInvoice.DoNotMail!=1  AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}else{
				query=query+" And Date(cuInvoice.CreatedOn) BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}
			}
			else if(rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0){
				if(!query.contains("WHERE")){
				query=query+" WHERE  (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2   GROUP BY cuInvoice.cuInvoiceID";
				}else{
				query=query+" And (cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}
			}
//			else if(batchInvoiceCuID.length()>0 &&batchInvoiceCuID!="0")
//				query=query+" WHERE rxMaster.rxMasterID = "+batchInvoiceCuID+" GROUP BY cuInvoice.cuInvoiceID";
			/*else if(batchInvoiceFromDate.length()>0 && batchInvoiceToDate.length()>0)
				query=query+" WHERE cuInvoice.CreatedOn BETWEEN  '"+invoicefromdate+"' AND  '"+invoicetodate+"' GROUP BY cuInvoice.cuInvoiceID";
			else if(rangeInvoiceFrom.length()>0 && rangeInvoiceTo.length()>0)
				query=query+" WHERE ((cuInvoice.cuInvoiceID  <= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  >=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"')) OR (cuInvoice.cuInvoiceID  >= (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceFrom+"') AND cuInvoice.cuInvoiceID  <=  (SELECT cuInvoice.cuInvoiceID FROM cuInvoice WHERE cuInvoice.InvoiceNumber='"+rangeInvoiceTo+"'))) GROUP BY cuInvoice.cuInvoiceID";
			*/
			else{
				if(!query.contains("WHERE")){
					query=query+" where Date(cuInvoice.CreatedOn) ='"+yesterDayDate+"' AND cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}else{
					query=query+" And cuInvoice.DoNotMail!=1 AND cuMaster.InvoiceMethod= 2  GROUP BY cuInvoice.cuInvoiceID";
				}
				
			}
			query=query+" ORDER BY rxMaster.Name ASC";	
			
			List<?> cuInvoiceEmailList = itsPurchaseService.getAllcuInvoiceEmailList(query);
/*			
			System.out.println("cuInvoiceEmailListSize"+cuInvoiceEmailList.size());
			System.out.println("cuInvoiceEmailList"+cuInvoiceEmailList);*/
			aResponse = new CustomResponse();
			aResponse.setRows(cuInvoiceEmailList);
			aResponse.setRecords(String.valueOf(cuInvoiceEmailList.size()));
			aResponse.setPage(thePage);


		}  catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
		}

		return aResponse;
	}
	
	@RequestMapping(value = "/cStatementEmailListGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAllcuStatement_EmailList(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "rxMasterIds", required = false) String rxMasterIds,
			@RequestParam(value = "exclusionDate", required = false) String exclusiondate,
			@RequestParam(value = "warehouseInactive", required = false) Integer isCredit,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException, ParseException {
		itsLogger.debug("Received request to get all PO List "+theSidx+" sorder :"+theSord);
		
		System.out.println("rxMasterIds"+rxMasterIds+"exclusiondate"+exclusiondate+"isCredit"+isCredit);
	
		CustomResponse aResponse = null;
		String query ="";
	Date date = new SimpleDateFormat("MM/dd/yyyy").parse(exclusiondate);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	System.out.println(sdf.format(date)+" :: "+""+" "+"" );

		
		
		
		try {
			
			if(rxMasterIds.equals("-1")){

				query =  " SELECT I.StatementEmailDate,I.cuInvoiceID,I.InvoiceNumber,rxm.rxMasterID, rxm.Name AS CustomerName "
						+ " FROM cuInvoice I"
						+ "  LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  WHERE (ABS(I.InvoiceAmount) - ABS(I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1  AND IsCredit<>1) AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND rxm.`InActive`=0 GROUP BY I.cuInvoiceID ORDER BY CustomerName";
				
			}
			else{
				query =  " 	SELECT I.StatementEmailDate,I.cuInvoiceID,I.InvoiceNumber,rxm.rxMasterID, rxm.Name AS CustomerName "
						+ " FROM cuInvoice I"
						+ "  LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  WHERE (ABS(I.InvoiceAmount) - ABS((I.AppliedAmount+(IFNULL(I.DiscountAmt,0)))) > 0.01) AND IF(I.CreditMemo = 0,I.CreditMemo=0,I.memoStatus=1 AND IsCredit<>1) AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) GROUP BY I.cuInvoiceID ORDER BY CustomerName";
				
			}
				if(isCredit!=null && rxMasterIds.equals("-1")){ 
				query = "	SELECT I.StatementEmailDate,I.cuInvoiceID,I.InvoiceNumber,rxm.rxMasterID, rxm.Name AS CustomerName "
						+ " FROM cuInvoice I"
						+ "  LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  WHERE IF(I.CreditMemo = 1,I.memoStatus=1 AND I.InvoiceAmount <> (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01 OR (I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) < -0.01))  AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND rxm.`InActive`=0  GROUP BY I.cuInvoiceID ORDER BY CustomerName";
						
			}else if(isCredit!=null){
				query = "SELECT I.StatementEmailDate,I.cuInvoiceID,I.InvoiceNumber,rxm.rxMasterID, rxm.Name AS CustomerName "
						+ " FROM cuInvoice I"
						+ "  LEFT JOIN rxMaster rxm ON rxm.rxMasterID=I.rxCustomerID"
						+ "  WHERE IF(I.CreditMemo = 1,I.memoStatus=1 AND I.InvoiceAmount <> (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))),(I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) > 0.01) OR (I.InvoiceAmount - (I.AppliedAmount+(IFNULL(I.DiscountAmt,0))) < -0.01))  AND " 
						+ "  (I.TransactionStatus>0) AND (I.InvoiceDate <= '"+sdf.format(date)+"') AND (I.rxCustomerID in ("+rxMasterIds+")) GROUP BY I.cuInvoiceID ORDER BY CustomerName";
						
		
			}
			
		System.out.println("query"+query);
				
		List<Cuinvoice> cuInvoiceEmailList = itsPurchaseService.getAllcuStatementEmailList(query);
		

			aResponse = new CustomResponse();
			aResponse.setRows(cuInvoiceEmailList);
			aResponse.setRecords(String.valueOf(cuInvoiceEmailList.size()));
			aResponse.setPage(thePage);


		}  catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
		}

		return aResponse;
	}

	@RequestMapping(value = "/showReceivedInventory", method = RequestMethod.GET)
	public ModelAndView receiveInventoryGetVepoDetails(
			@RequestParam(value = "vePOID", required = false) String vePOID,
			@RequestParam(value = "veReceiveID", required = false) String veReceiveID,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		ModelAndView theView = new ModelAndView(
				"inventory/showReceivedInventory");
		itsLogger.debug("addpurchaseorder");
		System.out.println("123   ...321");
		Integer vepoID = Integer.parseInt(vePOID);

		if (!SessionConstants.checkSessionExist(theRequest)) {
			return null;
		}
		try {
			Vepo vePO = itsPurchaseService.getVePo(vepoID);
			Integer theRxMasterId = itsPurchaseService.getRxMasterID(vepoID);
			ArrayList<Rxaddress> alRxaddress = (ArrayList<Rxaddress>) itsPurchaseService
					.getCustomerAddress(theRxMasterId);

			theView.addObject("vendorName",
					itsPurchaseService.getVendor(theRxMasterId));
			theView.addObject("vePO", vePO);
			theView.addObject("PONumber", vePO.getPonumber());
			if (alRxaddress.size() > 0) {
				theView.addObject("address1", alRxaddress.get(0).getAddress1());
				theView.addObject("address2", alRxaddress.get(0).getAddress2());
				theView.addObject("city", alRxaddress.get(0).getCity());
				theView.addObject("state", alRxaddress.get(0).getState());
				theView.addObject("zip", alRxaddress.get(0).getZip());
			} else {
				theView.addObject("address1", "");
				theView.addObject("address2", "");
				theView.addObject("city", "");
				theView.addObject("state", "");
				theView.addObject("zip", "");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			if (vePO.getreceiveddate() != null) {
				theView.addObject("receivedDate",
						sdf.format(vePO.getreceiveddate()));
			} else {
				theView.addObject("receivedDate", sdf.format(new Date()));
			}

			theView.addObject("alRxaddress", alRxaddress);
			if(veReceiveID==null || veReceiveID.equals(""))
			theView.addObject("veReceiveID",0);
			else
			theView.addObject("veReceiveID",veReceiveID);  
			
		} catch (VendorException e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>vePOID:</b>"+vePOID,"POController",e,session,theRequest);
			return theView;
		}
		System.out.println("123   ...321" + theView);
		return theView;
	}

	@RequestMapping(value = "/showReceivedInventoryList", method = RequestMethod.GET)
	public ModelAndView showReceivedInventoryList(
			@RequestParam(value = "vendorID", required = false) String rxVendorId,
			@RequestParam(value = "sortBy", required = false) String sortBy,
			@RequestParam(value = "rangeFrom", required = false) String rangeFrom,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		ModelAndView theView = new ModelAndView(
				"inventory/showReceivedInventoryList");

		if (!SessionConstants.checkSessionExist(theRequest)) {
			return null;
		}
		try {
			theView.addObject("vendorID", rxVendorId);
			theView.addObject("sortBy", sortBy);
			theView.addObject("rangeFrom", rangeFrom);

		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException("<b>rxVendorId:</b>"+rxVendorId ,"POController",e,session,theRequest);
			return theView;
		}

		return theView;
	}

	@RequestMapping(value = "/receivedpolist", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getReceivedPO(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all PO List");
		CustomResponse aResponse = null;
		try {
			int aTotalCount = itsPurchaseService.getRecordsCountforReceiveInvenory();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			
			 String fromdate = "";
			 String todate = "";
			 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			 
			if(fromDate!=null && !fromDate.trim().equals("")){
				
			    Date convertedCurrentDate = sdf.parse(fromDate);
			    fromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDate!=null && !toDate.trim().equals("")){
			    Date convertedCurrentDate = sdf.parse(toDate);
			    todate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+todate);
			}
			//List<?> aPOs = itsPurchaseService.filterPOsList(aFrom, aTo,vendorId, sortBy, rangeFrom);
			List<?> aPOs = itsPurchaseService.filterPOsList(aFrom, aTo,searchData,fromdate,todate,theSidx,theSord);
			// List<?> aPOs = itsPurchaseService.getAllPOsList(aTotalCount-100,
			// aTotalCount);
			aResponse = new CustomResponse();
			aResponse.setRows(aPOs);
			aResponse.setRecords(String.valueOf(aPOs.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (VendorException excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getCause()
					.getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData ,"POController",excep,session,theRequest);
		} catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
			sendTransactionException("<b>searchData:</b>"+searchData,"POController",excep,session,theRequest);
		}

		return aResponse;
	}
	
	@RequestMapping(value = "/wh_transerList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getAllWarehouseTransfer(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, MessagingException {
		itsLogger.debug("Received request to get all Warehouse Transfer List");
		CustomResponse aResponse = null;
		try {
			int aTotalCount = itsPurchaseService.getRecordsCountForWhTransfer();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			List<?> aWarehouseTransfers = itsPurchaseService.getAllWarehouseTransfer(aFrom, aTo);
			System.out.println("Warehouse Transfer List size------------>"+aWarehouseTransfers.size());
			aResponse = new CustomResponse();
			aResponse.setRows(aWarehouseTransfers);
			aResponse.setRecords(String.valueOf(aWarehouseTransfers.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double) aTotalCount
					/ (double) theRows));
		} catch (VendorException excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(excep.getItsErrorStatusCode(), excep.getCause()
					.getMessage());
			sendTransactionException("<b>wh_transerList:</b>","POController",excep,session,theRequest);
		} catch (Exception excep) {
			itsLogger.error(excep.getCause().getMessage(), excep);
			response.sendError(500, excep.getCause().getMessage());
			sendTransactionException("<b>wh_transerList:</b>","POController",excep,session,theRequest);
		}

		return aResponse;
	}
	
	@RequestMapping(value = "/getLineItemsForSOTemplate", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getLineItemsForSOTemplate(
			@RequestParam(value = "prMasterId") Integer prMasterId,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			map.put("lineItems", (ArrayList<Prmaster>) itsPurchaseService
					.getLineItemsForSOTemplate(prMasterId));
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>prMasterId:</b>"+prMasterId ,"POController",e,session,theRequest);
		}
		return map;

	}
	
	@RequestMapping(value = "/setPOStatus", method = RequestMethod.POST)
	public @ResponseBody
	Boolean getCuSOID(
			@RequestParam(value = "vePoID", required = false) Integer vePoID,
			@RequestParam(value = "status", required = false) Integer status,
			HttpServletResponse response, HttpSession session,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		itsLogger.info("VePO ID: "+vePoID +" Status: "+status);
			try {
				itsPurchaseService.updatePOStatus(vePoID, status);
			} catch (VendorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>vePoID:</b>"+vePoID ,"POController",e,session,theRequest);
			}
return true;
		
	}
	@RequestMapping(value = "/getPOinvoiceStatus", method = RequestMethod.POST)
	public @ResponseBody
	String getPOinvoiceStatus(
			@RequestParam(value = "vePoID", required = false) Integer vePoID,
			HttpServletResponse httpresponse, HttpSession session,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		itsLogger.info("VePO ID: "+vePoID);
		String response =null;
			try {
				Vepodetail aVepodetail = itsPurchaseService.getPOinvoiceStatus(vePoID);
				itsLogger.info("qtyOrdered: "+aVepodetail.getOrderedCount() +" qtyBilled: "+aVepodetail.getBilledCount());
				 response = aVepodetail.getOrderedCount()+","+aVepodetail.getBilledCount();
			} catch (VendorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sendTransactionException("<b>vePoID:</b>"+vePoID ,"POController",e,session,theRequest);
			}
return response;
		
	}
	
	public void sendTransactionException(String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException{
		UserBean aUserBean=null;
		TsUserSetting objtsusersettings=null;
		try{
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
		 StringWriter errors = new StringWriter();
		 e.printStackTrace(new PrintWriter(errors));
		if(objtsusersettings.getItsmailYN()==1){
		Transactionmonitor transObj =new Transactionmonitor();
		 SendQuoteMail sendMail = new SendQuoteMail();
		 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
		 transObj.setTrackingId(trackingID);
		 transObj.setTimetotriggerd(new Date());
		 transObj.setJobStatus(jobstatus);
		 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
		 transObj.setDescription("Message :: " + errors.toString());
		 sendMail.sendTransactionInfo(transObj,therequest);
		}
		
		if(objtsusersettings.getItslogYN()==1){
			TpUsage aTpusage=new TpUsage(new Date(), jobstatus,trackingID,"Error",aUserBean.getUserId(),"Message :: " + errors.toString());
			userService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	public String getAllSOsListQuery(String searchData,String startDate,String endDate) throws VendorException {
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
		      
		
		aPOSelectQry = new StringBuilder("SELECT count(so.cuSOID) ")
		.append("FROM cuSO so LEFT JOIN rxMaster rm ON so.rxCustomerID = rm.rxMasterID  LEFT JOIN joRelease jo ON so.joReleaseID = jo.joReleaseID LEFT JOIN joMaster j ON jo.joMasterID = j.joMasterID ")
		.append(searchCondition);		
	
		return  aPOSelectQry.toString();
	}
	public String getAllCuInvoicesListCountQuery(String searchData,String startDate,String endDate) throws VendorException {
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
		
		
		StringBuilder aPOSelectQry = new StringBuilder("SELECT cu.cuInvoiceID ")
		.append(" FROM cuInvoice cu LEFT JOIN rxMaster rm ON cu.rxCustomerID = rm.rxMasterID ")
		.append(" LEFT JOIN joReleaseDetail jod ON(jod.joReleaseDetailID=cu.joReleaseDetailID) LEFT JOIN joMaster jo ON(jo.joMasterID=jod.joMasterID) ")
		.append(" LEFT JOIN cuPaymentGlpost cgl ON (cgl.cuInvoiceID = cu.cuInvoiceID AND cgl.postStatus<>0) LEFT JOIN cuLinkageDetail cuL ON (cuL.cuInvoiceID = cu.cuInvoiceID AND cuL.datePaid = (SELECT MAX(datePaid) FROM cuLinkageDetail WHERE cuInvoiceID = cu.cuInvoiceID ))  LEFT JOIN cuReceipt cur ON cur.cuReceiptID = cuL.cuReceiptID ")
		.append(searchCondition)
		.append(" GROUP BY cu.cuInvoiceID ");
		
		
		return  aPOSelectQry.toString();
	}
	
	@RequestMapping(value = "/getSelectedShiptoAddress", method = RequestMethod.POST)
	public @ResponseBody Rxaddress getSelectedShiptoAddress(
			@RequestParam(value="type", required= false) String type,
			@RequestParam(value="addressid", required= false) Integer addressid,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, JobException, MessagingException {
		Rxaddress rxAddress = new Rxaddress();
	
		try {
				
			rxAddress=itsPurchaseService.getSelectedShiptoaddress(type, addressid);
				
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			sendTransactionException( addressid+"","POController",e,theSession,theRequest);
		}
		return rxAddress;
	}
	
}
