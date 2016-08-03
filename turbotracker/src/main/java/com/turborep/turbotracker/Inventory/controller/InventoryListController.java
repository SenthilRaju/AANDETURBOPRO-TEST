/**

 * This Inventory List Controller is Carried out of Inventory Page Details. 
 * @author Madhan Kumar
 *
 */

package com.turborep.turbotracker.Inventory.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lowagie.text.DocumentException;
import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.service.InventoryConstant;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.customer.controller.CustomerListController;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.PrCategory;
import com.turborep.turbotracker.product.dao.PrDepartment;
import com.turborep.turbotracker.product.dao.PrDepartmentAccounts;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.PurchaseOrdersBean;
import com.turborep.turbotracker.vendor.dao.SalesOrderBean;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.PurchaseService;

@Controller
@RequestMapping("/inventoryList")
public class InventoryListController {

	protected static Logger itsLogger = Logger
			.getLogger(CustomerListController.class);

	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;

	@Resource(name = "purchaseService")
	private PurchaseService itsPurchaseService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getAll(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			//@RequestParam(value = "itemCode", required = false) String theItemCode,
			@RequestParam(value = "Inactive", required = false) Integer theInactive,
			@RequestParam(value = "Warehouse", required = false) Integer thewarehouse,
			@RequestParam(value = "searchData", required = false) String searchData,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, InventoryException, MessagingException {
		System.out.println("thePage:"+thePage+" theRows:"+theRows+" theSidx"+theSidx+" theSord:"+theSord+"Warehouse"+thewarehouse);
		UserBean aUserBean;
		/*if(theItemCode.equals(",")){
			theItemCode="";
		}*/
		int aFrom = 0, aTo = 0;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		if(searchData == null)
			searchData = "";
		int aTotalCount = itsInventoryService.getInventoryRecordCount(thewarehouse, searchData, theInactive,theSidx,theSord);
		CustomResponse aResponse = null;
		TsUserLogin aUserLogin = null;
		try {
			if(theRows==null || theRows==0){
				theRows=50;
			}
			aUserLogin = new TsUserLogin();
			aUserLogin.setInventoryperpage(theRows);
			aUserLogin.setUserLoginId(aUserBean.getUserId());
			itsUserService.updatePerPage(aUserLogin, "inventory");
			
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			List<?> inventory = itsInventoryService.getInventory(aFrom, aTo,thewarehouse, searchData, theInactive,theSidx,theSord);
			/*if(inventory.size() == 1)
			{
				System.out.println("inventory.size() == 1");
				Prmaster aPrmaster = (Prmaster) inventory.get(0);
				String redirect  = redirectToInventoryPage(aPrmaster);
				response.sendRedirect(redirect);
				
			}
			else
			{*/
				aResponse = new CustomResponse();
				aResponse.setRows(inventory);
				aResponse.setRecords(String.valueOf(inventory.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double) aTotalCount
						/ (double) theRows));
			//}
			
		} catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
		 Transactionmonitor transObj =new Transactionmonitor();
		 SendQuoteMail sendMail = new SendQuoteMail();
		 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
		 transObj.setTrackingId("<b>aFrom, aTo,thewarehouse, searchData==></b>"+aFrom+","+aTo+","+thewarehouse+","+ searchData);
		 transObj.setTimetotriggerd(new Date());
		 transObj.setJobStatus("Inventory.");
		 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
		 StringWriter errors = new StringWriter();
		 e.printStackTrace(new PrintWriter(errors));
		 transObj.setDescription("Message :: " + errors.toString());
		 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}
	public String redirectToInventoryPage(Prmaster aPrmaster)
	{
		/*RedirectAttributes redirectAttributes;
		
		redirectAttributes.addFlashAttribute("inventoryId", aPrmaster.getPrMasterId());
		redirectAttributes.addFlashAttribute("itemCode", aPrmaster.getItemCode());
		redirectAttributes.addFlashAttribute("token", aPrmaster.getItemCode());
		./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
*/	    
		System.out.println("redirectToInventoryPage");
		return "redirect:/inventoryDetails?token=view&inventoryId="+aPrmaster.getPrMasterId() + "&itemCode=" + aPrmaster.getItemCode();
	}

	@RequestMapping(value = "/vendorListAuto", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getEngineerRxList(@RequestParam("term") String theSearchString,
			HttpServletResponse response,HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> sales = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			sales = (ArrayList<AutoCompleteBean>) itsInventoryService
					.getVendorList(theSearchString);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theSearchString==></b>"+theSearchString);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return sales;
	}

	@RequestMapping(value = "/deleteInventory", method = RequestMethod.POST)
	public @ResponseBody
	Boolean deleteInventoryDetails(
			@RequestParam(value = "inventoryID", required = false) Integer theInventoryID,
			HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException,
			InventoryException, MessagingException {
		Boolean isDeleted;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			isDeleted = itsInventoryService
					.deleteInventoryDetails(theInventoryID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getCause().getMessage());
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theInventoryID==></b>"+theInventoryID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
			
			return false;
		}
		return isDeleted;
	}

	@RequestMapping(value = "/updateInventoryDetails", method = RequestMethod.POST)
	public @ResponseBody
	Prmaster updateInventoryDetails(
			@RequestParam(value = "codeName", required = false) String theCodeName,
			@RequestParam(value = "descriptionName", required = false) String theDescriptionName,
			@RequestParam(value = "departmentName", required = false) String theDepartment,
			@RequestParam(value = "categoryName", required = false) String theCategoryName,
			@RequestParam(value = "boxName", required = false) String theBoxName,
			@RequestParam(value = "weightName", required = false) String theWeightName,
			@RequestParam(value = "ounces", required = false) String ounces,
			@RequestParam(value = "binName", required = false) String theBinName,
			@RequestParam(value = "bingeneralName", required = false) String theBingernalName,
			@RequestParam(value = "productSiteName", required = false) String theProductName,
			@RequestParam(value = "primaryVendorName", required = false) String thePrimeryVendorName,
			@RequestParam(value = "vendorName", required = false) String theVendorName,
			@RequestParam(value = "vendorItemCodeName", required = false) String theVendorItemCode,
			@RequestParam(value = "sellingPriceName", required = false) String theSellingPriceName,
			@RequestParam(value = "profitMarginName", required = false) String theProfitMarginName,
			@RequestParam(value = "pctIDName", required = false) String thePctName,
			@RequestParam(value = "multiplierName", required = false) String theMultiplierName,
			@RequestParam(value = "factoryCostName", required = false) String theFactoryCostNAme,
			@RequestParam(value = "salesOrderSelectedName", required = false) String theSalesOderSelectedName,
			@RequestParam(value = "invoiceSelectedName", required = false) String theInvoiceName,
			@RequestParam(value = "purchaseOrderSelectedName", required = false) String thePurcheaseOderName,
			@RequestParam(value = "QuantityName", required = false) String theQuantityName,
			@RequestParam(value = "overName", required = false) String theOverName,
			@RequestParam(value = "RetailName", required = false) String theRetailName,
			@RequestParam(value = "wholeSaleName", required = false) String theWholeSaleName,
			@RequestParam(value = "dealerName", required = false) String theDealerName,
			@RequestParam(value = "special1Name", required = false) String theSpecial1Name,
			@RequestParam(value = "distributorName", required = false) String theDistributorName,
			@RequestParam(value = "special2Name", required = false) String theSpecial2Name,
			@RequestParam(value = "inactiveboxNameBox", required = false) boolean theInActiveName,
			@RequestParam(value = "inventoryNameBox", required = false) boolean theInventoryName,
			@RequestParam(value = "consignmentNameBox", required = false) boolean theConsignmentName,
			@RequestParam(value = "poNameBox", required = false) boolean thePOName,
			@RequestParam(value = "soNameBox", required = false) boolean theSOName,
			@RequestParam(value = "invoiceNameBox", required = false) boolean theInvoiceBoxName,
			@RequestParam(value = "pickTicketNameBox", required = false) boolean thePickTicketName,
			@RequestParam(value = "taxableNameBox", required = false) boolean theTaxableName,
			@RequestParam(value = "singleItemNameBox", required = false) boolean theSingleItemName,
			@RequestParam(value = "labourNameBox", required = false) boolean theLabourName,
			@RequestParam(value = "assembliesBox", required = false) boolean theAssembliesBoxName,
			@RequestParam(value = "serialisedBox", required = false) boolean theSerializeBoxNAme,
			@RequestParam(value = "prMasterID", required = false) String thePrMasterID,
			@RequestParam(value = "token", required = false) String theToken,
			HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest)
			throws ParseException, JobException, IOException,
			InventoryException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		Prmaster aPrMaster = null;
		Prwarehouseinventory aPrwarehouseinventory = null;
		Prwarehouseinventory aPrwarehouseinventoryaftsave = null;
		BigDecimal aBoxName = null;
		BigDecimal aWeight = null;
		BigDecimal aSellingPrice = null;
		// BigDecimal aProfitName = null;
		BigDecimal aMultiplier = null;
		BigDecimal aFactory = null;
		BigDecimal aQuantity = null;
		BigDecimal aRetailName = null;
		BigDecimal aDealer = null;
		BigDecimal aDistributor = null;
		BigDecimal aWholeSales = null;
		BigDecimal aSpec1 = null;
		BigDecimal aSpec2 = null;
		BigDecimal aOver = null;
		try {
			aPrMaster = new Prmaster();
			aPrwarehouseinventory = new Prwarehouseinventory();
			aPrwarehouseinventoryaftsave = new Prwarehouseinventory();
			
			
			byte aInActive = (byte) (theInActiveName ? 1 : 0);
			byte aInventory = (byte) (theInventoryName ? 1 : 0);
			byte aConsignment = (byte) (theConsignmentName ? 1 : 0);
			byte aPOName = (byte) (thePOName ? 1 : 0);
			byte aSoName = (byte) (theSOName ? 1 : 0);
			byte aInvoiceBox = (byte) (theInvoiceBoxName ? 1 : 0);
			byte aPickTicket = (byte) (thePickTicketName ? 1 : 0);
			byte aTaxBox = (byte) (theTaxableName ? 1 : 0);
			byte aSingleItemBox = (byte) (theSingleItemName ? 1 : 0);
			byte aLabourBox = (byte) (theLabourName ? 1 : 0);
			byte aAssembleBox = (byte) (theAssembliesBoxName ? 1 : 0);
			byte aSerializeBox = (byte) (theSerializeBoxNAme ? 1 : 0);
			if (theBoxName != "") {
				aBoxName = new BigDecimal(theBoxName);
			}
			if(ounces==null){
				ounces="00";
			}
			itsLogger.info("ounces===>"+ounces);
			if(ounces.length()==1){
				ounces="0"+ounces;
			}
			if (theWeightName != "") {
				aWeight = new BigDecimal(theWeightName+"."+ounces).setScale(2);
			}
			if (theSellingPriceName != "") {
				aSellingPrice = new BigDecimal(theSellingPriceName);
				/*
				 * }if(theProfitMarginName!= ""){ aProfitName = new
				 * BigDecimal(theProfitMarginName);
				 */
			}
			if (theMultiplierName != "") {
				aMultiplier = new BigDecimal(theMultiplierName);
			}
			if (theFactoryCostNAme != "") {
				aFactory = new BigDecimal(theFactoryCostNAme);
			}
			if (theQuantityName != "") {
				aQuantity = new BigDecimal(theQuantityName);
			}
			if (theRetailName != "") {
				aRetailName = new BigDecimal(theRetailName);
			}
			if (theDealerName != "") {
				aDealer = new BigDecimal(theDealerName);
			}
			if (theDistributorName != "") {
				aDistributor = new BigDecimal(theDistributorName);
			}
			if (theWholeSaleName != "") {
				aWholeSales = new BigDecimal(theWholeSaleName);
			}
			if (theSpecial1Name != "") {
				aSpec1 = new BigDecimal(theSpecial1Name);
			}
			if (theSpecial2Name != "") {
				aSpec2 = new BigDecimal(theSpecial2Name);
			}
			if (theOverName != "") {
				aOver = new BigDecimal(theOverName);
			}
			aPrMaster.setItemCode(theCodeName);
			aPrMaster.setDescription(theDescriptionName);
			if (theDepartment != "") {
				aPrMaster.setPrDepartmentId(Integer.valueOf(theDepartment));
			}
			if (theCategoryName != "") {
				aPrMaster.setPrCategoryId(Integer.valueOf(theCategoryName));
			}
			aPrMaster.setWebSite(theProductName);
			aPrMaster.setBoxQty(aBoxName);
			aPrMaster.setWeight(aWeight);
			aPrMaster.setPOMult(aMultiplier);
			aPrMaster.setSalesPrice00(aSellingPrice);
			aPrMaster.setLastCost(aFactory);
			aPrMaster.setSOPopup(theSalesOderSelectedName);
			aPrMaster.setPOPopup(thePurcheaseOderName);
			aPrMaster.setCUPopup(theInvoiceName);
			aPrMaster.setQuantityBreak0(aQuantity);
			aPrMaster.setQuantityBreak1(aOver);
			aPrMaster.setSalesPrice01(aRetailName);
			aPrMaster.setSalesPrice10(aDealer);
			aPrMaster.setSalesPrice20(aDistributor);
			aPrMaster.setSalesPrice30(aWholeSales);
			aPrMaster.setSalesPrice40(aSpec1);
			aPrMaster.setSalesPrice50(aSpec2);
			aPrMaster.setInActive(aInActive);
			aPrMaster.setIsInventory(aInventory);
			aPrMaster.setIsTaxable(aTaxBox);
			aPrMaster.setConsignment(aConsignment);
			aPrMaster.setPrintOnPos(aPOName);
			aPrMaster.setPrintOnSos(aSoName);
			aPrMaster.setSerialized(aSerializeBox);
			aPrMaster.setAutoAssemble(aAssembleBox);
			aPrMaster.setLabor(aLabourBox);
			aPrMaster.setSingleItemTax(aSingleItemBox);
			aPrMaster.setPrintOnPTs(aPickTicket);
			aPrMaster.setPrintOnCUs(aInvoiceBox);
			aPrMaster.setVendorItemNumber(theVendorItemCode);
			if (theVendorName != "") {
				aPrMaster.setRxMasterIdprimaryVendor(Integer
						.valueOf(theVendorName));
			}
			if (thePrMasterID != "") {
				aPrMaster.setPrMasterId(Integer.valueOf(thePrMasterID));
				aPrwarehouseinventory.setPrMasterId(Integer
						.valueOf(thePrMasterID));
			}
			if (theBinName != "") {
				aPrwarehouseinventory.setPrWarehouseId(Integer
						.valueOf(theBinName));
			}
				aPrwarehouseinventory.setBin(theBingernalName);
			
			 if (theToken.equalsIgnoreCase("new") && thePrMasterID == "") {
					byte aHasInitialCost = 0;
					aPrMaster.setHasInitialCost(aHasInitialCost);
					aPrMaster.setInventoryOnHand(new BigDecimal("0.0000"));
					aPrMaster.setInventoryOnOrder(new BigDecimal("0.0000"));
					aPrMaster.setInventoryAllocated(new BigDecimal("0.0000"));
					aPrMaster.setInventoryOrderPoint(new BigDecimal("0.0000"));
					aPrMaster.setInventoryOrderQuantity(new BigDecimal("0.0000"));
					aPrwarehouseinventory.setHasInitialCost(aHasInitialCost);
					aPrwarehouseinventory.setInventoryOnHand(new BigDecimal("0.0000"));
					aPrwarehouseinventory.setInventoryOnOrder(new BigDecimal("0.0000"));
					aPrwarehouseinventory.setInventoryAllocated(new BigDecimal("0.0000"));
					Integer prwinvd = itsInventoryService.addNewInventory(aPrMaster,
							aPrwarehouseinventory);
					aPrwarehouseinventoryaftsave = itsInventoryService.getPrwarehouseinventoryDetails(prwinvd);
					aPrMaster.setPrMasterId(aPrwarehouseinventoryaftsave.getPrMasterId());
				 }
				 else
				 {
					Integer aPRWareID = itsInventoryService
							.getPrWareID(aPrwarehouseinventory);
					aPrwarehouseinventory.setPrWarehouseInventoryId(aPRWareID);
					itsInventoryService.updateInventoryDetails(aPrMaster,
							aPrwarehouseinventory);
				} 
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getCause().getMessage());
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>thePrMasterID==></b>"+thePrMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
			
		}
		return aPrMaster;
	}

	@RequestMapping(value = "/updateInventoryCustom", method = RequestMethod.POST)
	public @ResponseBody
	Boolean updateInventoryCustom(
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "requestArray[]", required = false) String[] requestArray,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		System.out.println("Came here");
		String aToken = token;
		Integer aPrMasterID = Integer.valueOf(requestArray[0]);
		boolean aResult = false;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			Prmaster aPrmaster = itsInventoryService
					.getSingleProductDetails(aPrMasterID);
			if (aToken.equals("resetAvgCost")) {
				BigDecimal aNewAvgCost = BigDecimal.valueOf(Double
						.valueOf(requestArray[1]));
				aPrmaster.setAverageCost(aNewAvgCost);
			}
			aResult = itsInventoryService.updateInventory(aPrmaster);
			return aResult;
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>aPrMasterID==></b>"+aPrMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResult;
	}

	@RequestMapping(value = "/categoriesList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getChartsAccounts(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, CompanyException, UserException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = null;
		try {
			System.out.println("sidx==>"+theSidx);
			System.out.println("sord==>"+theSord);
			List<PrCategory> aCategotyList = itsInventoryService.getInventoryCategories(theSidx,theSord);
			aResponse = new CustomResponse();
			aResponse.setRows(aCategotyList);
			aResponse.setRecords(String.valueOf(aCategotyList.size()));
			aResponse.setPage(thePage);
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());

			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Method==></b>/categoriesList");
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}

	/**
	 * description=hdfghsss+ssss&categoryInactive=1&overrideMarkup=1&
	 * overrideMarkupInput=hjfffff+frffffff&oper=add
	 * 
	 * @throws InventoryException
	 * @throws MessagingException 
	 * **/
	@RequestMapping(value = "/manipulateCategory", method = RequestMethod.POST)
	public @ResponseBody
	Boolean manipulateCategory(
			@RequestParam(value = "oper", required = false) String oper,
			@RequestParam(value = "categoryInactive", required = false) Boolean categoryInactive,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "overrideMarkup", required = false) Integer overrideMarkup,
			@RequestParam(value = "overrideMarkupInput", required = false) BigDecimal overrideMarkupInput,
			@RequestParam(value = "invCategoryId", required = false) Integer invCategoryId,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, CompanyException, UserException,
			InventoryException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		PrCategory aCategory = new PrCategory();
		try {
		if(!oper.equals("del")){
		if (description == null || description.isEmpty()) {
			throw new InventoryException("Description is Empty");
		}
		}
		aCategory.setDescription(description);
		
		aCategory.setMarkupCost(false);
		if(overrideMarkup==1){
			aCategory.setMarkupCost(true);
		}
		aCategory.setInActive(categoryInactive);
		aCategory.setMarkupAmount(overrideMarkupInput);
		
			if (oper.equals("add")) {
				itsInventoryService.addCategory(aCategory);
			} else if (oper.equals("edit")) {
				aCategory.setPrCategoryId(invCategoryId);
				itsInventoryService.editCategory(aCategory);
			} else if (oper.equals("del")) {
				aCategory.setPrCategoryId(invCategoryId);
				itsInventoryService.deleteCategory(aCategory);
			}
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(501, e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>invCategoryId==></b>"+invCategoryId);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return true;
	}

	/**
	 * invDepartmentId=&name=Test+Test&departmentInactive=1&revenue=&cost=&oper=
	 * add
	 * @throws MessagingException 
	 * */
	@RequestMapping(value = "/manipulateDepartment", method = RequestMethod.POST)
	public @ResponseBody
	Boolean manipulateDepartment(
			@RequestParam(value = "oper", required = false) String oper,
			@RequestParam(value = "departmentInactive", required = false) Boolean departmentInactive,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "coAccountIdsales", required = false) Integer coAccountIdsales,
			@RequestParam(value = "coAccountIdcogs", required = false) Integer coAccountIdCogs,
			@RequestParam(value = "invDepartmentId", required = false) Integer invDepartmentId,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, CompanyException, UserException, MessagingException {
		PrDepartment aDepartment = new PrDepartment();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
		aDepartment.setDescription(name);
		if (departmentInactive == null) {
			departmentInactive = false;
		}
		aDepartment.setInActive(departmentInactive);
		aDepartment.setCoAccountIdsales(coAccountIdsales);
		aDepartment.setCoAccountIdcogs(coAccountIdCogs);
		
			if (oper.equals("add")) {
				itsInventoryService.addDepartment(aDepartment);
			} else if (oper.equals("edit")) {
				aDepartment.setPrDepartmentId(invDepartmentId);
				itsInventoryService.editDepartment(aDepartment);
			} else if (oper.equals("del")) {
				aDepartment.setPrDepartmentId(invDepartmentId);
				itsInventoryService.deleteDepartment(aDepartment);
			}
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(501, e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>invDepartmentId==></b>"+invDepartmentId);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return true;
	}

	@RequestMapping(value = "/departmentsList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getDepartmentsAccounts(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, CompanyException, UserException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = null;
		try {
			List<PrDepartment> aDepartments = itsInventoryService
					.getInventoryDepartments();
			aResponse = new CustomResponse();
			aResponse.setRows(aDepartments);
			aResponse.setRecords(String.valueOf(aDepartments.size()));
			aResponse.setPage(thePage);
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>method==></b>departmentsList");
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}

	@RequestMapping(value = "/departmentsAccountsList", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getDepartmentsAccountNo(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpSession session, HttpServletResponse response,HttpServletRequest therequest)
			throws IOException, CompanyException, UserException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = null;
		try {
			List<PrDepartmentAccounts> aDepartmentAccountNo = itsInventoryService
					.getDepartmentAccountNo();
			aResponse = new CustomResponse();
			aResponse.setRows(aDepartmentAccountNo);
			aResponse.setRecords(String.valueOf(aDepartmentAccountNo.size()));
			aResponse.setPage(thePage);
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>method==></b>departmentsAccountsList");
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}

	@RequestMapping(value = "/AccountListAuto", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getAccountList(@RequestParam("term") String theSearchString,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<AutoCompleteBean> itsAccountList = null;
		AutoCompleteBean autocompleteBean = new AutoCompleteBean();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			itsAccountList = (ArrayList<AutoCompleteBean>) itsInventoryService
					.getAccountList(theSearchString, autocompleteBean);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theSearchString==></b>"+theSearchString);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return itsAccountList;
	}

	@RequestMapping(value = "/recieveInventory", method = RequestMethod.GET)
	public @ResponseBody Vepo receiveInventory(
			@RequestParam(value = "poNumber", required = false) String poNumber,
			HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		System.out.println("123   ...321.....POnumber::"+poNumber);
		Integer vePOID=null;
		Vepo vePO=null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
		ModelAndView theView = new ModelAndView("inventory/recieveInventory");
		if (!SessionConstants.checkSessionExist(therequest)) {
			return null;
		}
			 vePOID = itsPurchaseService.getVepoIDFRomPoNumber(poNumber);
			 itsLogger.info("VepoID Is:"+vePOID);
			 if(vePOID!=null){
				 vePO = itsPurchaseService.getVePo(vePOID);
				 vePO.setReceiveID(itsPurchaseService.getReceiveID(vePOID));
			 }
//			Integer theRxMasterId = itsPurchaseService.getRxMasterID(vePOID);
//			ArrayList<Rxaddress>  alRxaddress= (ArrayList<Rxaddress>)itsPurchaseService.getCustomerAddress(theRxMasterId);
//			
//			theView.addObject("vendorName",
//					itsPurchaseService.getVendor(theRxMasterId));
//			theView.addObject("poNumber",poNumber );
//			theView.addObject("vePO",vePO );
//			theView.addObject("alRxaddress",alRxaddress);
		} 
		 catch (VendorException e) {
			itsLogger.error(e.getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>poNumber==></b>"+poNumber);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
			
			return vePO;
		} 
		return vePO;
	}
	
	@RequestMapping(value = "/getItemDetails", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getItemDetails(@RequestParam("prMasterID") Integer theprMasterID,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<SalesOrderBean> salesOrderList = new ArrayList<SalesOrderBean>();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = new CustomResponse();
		try {
			salesOrderList = (ArrayList<SalesOrderBean>) itsInventoryService
					.getsalesOrderList(theprMasterID);
			aResponse.setRows(salesOrderList);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theprMasterID==></b>"+theprMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/getOnOrderItemDetails", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getOnOrderItemDetails(@RequestParam("prMasterID") Integer theprMasterID,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		ArrayList<PurchaseOrdersBean> onOrderList = new ArrayList<PurchaseOrdersBean>();
		CustomResponse aResponse = new CustomResponse();
		itsLogger.info("theprMasterID::"+theprMasterID);
		try {
			onOrderList = (ArrayList<PurchaseOrdersBean>) itsInventoryService
					.getOnOrderList(theprMasterID);
			aResponse.setRows(onOrderList);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theprMasterID==></b>"+theprMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/getOnHandItemDetails", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getOnHandItemDetails(@RequestParam("prMasterID") Integer theprMasterID,
			HttpServletResponse response,HttpServletRequest therequest,HttpSession thesession) throws IOException, MessagingException {
		ArrayList<PurchaseOrdersBean> onOrderList = new ArrayList<PurchaseOrdersBean>();
		CustomResponse aResponse = new CustomResponse();
		UserBean aUserBean;
		aUserBean = (UserBean) thesession.getAttribute(SessionConstants.USER);
		try {
			System.out.println("Its Calling. .. getOnHandItemDetails");
			onOrderList = (ArrayList<PurchaseOrdersBean>) itsInventoryService
					.getOnHandList(theprMasterID);
			aResponse.setRows(onOrderList);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theprMasterID==></b>"+theprMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}
	@RequestMapping(value = "/getTransferDetails", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getTransferDetails(@RequestParam("prTransferId") Integer theprTransferId,
			HttpServletResponse response,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		ArrayList<Prwhtransferdetail> transferList = new ArrayList<Prwhtransferdetail>();
		CustomResponse aResponse = new CustomResponse();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
					 
			System.out.println("Line Item Grid ID--->"+theprTransferId);
			transferList = (ArrayList<Prwhtransferdetail>) itsInventoryService
					.getWarehouseTransferList(theprTransferId);
			aResponse.setRows(transferList);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>theprTransferId==></b>"+theprTransferId);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/getTransferLineItems", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, ArrayList<?>> getTransferLineItems(
			@RequestParam(value = "prMasterId") Integer prMasterId,
			@RequestParam(value = "warehouseid") Integer warehouseid,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			map.put("lineItems", (ArrayList<Prmaster>) itsInventoryService
					.getTransferLineItems(prMasterId,warehouseid));
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>prMasterId,warehouseid==></b>"+prMasterId+","+warehouseid);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return map;

	}
	
	@RequestMapping(value = "/saveTransferDetails", method = RequestMethod.POST)
	public @ResponseBody
	PrwarehouseTransfer saveTransferDetails(
			@RequestParam(value = "transferDateId",required=false) Date transferDateId,
			@RequestParam(value = "warehouseFrom",required=false) Integer warehouseFrom,
			@RequestParam(value = "estDateId",required=false) Date estDateId,
			@RequestParam(value = "warehouseTo",required=false) Integer warehouseTo,
			@RequestParam(value = "recDateId",required=false) String recDateId,
			@RequestParam(value = "transNo",required=false) Integer transNo,
			@RequestParam(value = "ref",required=false) String ref,
			@RequestParam(value = "prTransferId",required=false) Integer prTransferId,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		//transferDateId=07%2F10%2F2014&warehouseFrom=1&estDateId=07%2F15%2F2014&warehouseTo=2&recDateId=07%2F30%2F2014&transNo=1&ref=test&prTransferId=&prMasterId=
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		PrwarehouseTransfer aPrwarehouseTransfer = new PrwarehouseTransfer();
		try {
			if(transferDateId != null)			
				aPrwarehouseTransfer.setTransferDate(transferDateId);
			
			aPrwarehouseTransfer.setCreatedOn(new Date());
			aPrwarehouseTransfer.setCreatedByID(aUserBean.getUserId());
			aPrwarehouseTransfer.setScreenno(1);
			if(warehouseFrom != null && warehouseFrom != 0)
				aPrwarehouseTransfer.setPrFromWarehouseId(warehouseFrom);
			/*if(recDateId != null)			
				aPrwarehouseTransfer.setReceivedDate(recDateId);*/
			if(estDateId != null)			
				aPrwarehouseTransfer.setEstreceiveDate(estDateId);
			if(warehouseTo != null && warehouseTo != 0)
				aPrwarehouseTransfer.setPrToWarehouseId(warehouseTo);
			if(transNo != null && transNo != 0)
				aPrwarehouseTransfer.setTransactionNumber(transNo);
			if(ref != null)
				aPrwarehouseTransfer.setDesc(ref);
			if(prTransferId != null && prTransferId != 0){
				aPrwarehouseTransfer.setPrTransferId(prTransferId);	
				aPrwarehouseTransfer = itsInventoryService.updateTransferDetails(aPrwarehouseTransfer);
			}
			else{
				aPrwarehouseTransfer = itsInventoryService.saveTransferDetails(aPrwarehouseTransfer);
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>warehouseFrom,warehouseTo==></b>"+warehouseFrom+","+warehouseTo);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return aPrwarehouseTransfer;

	}
	
	@RequestMapping(value = "/manpulateTransferLineItem", method = RequestMethod.POST)
	public @ResponseBody
	PrwarehouseTransfer ManipulateTransferLineItem(
			@RequestParam(value = "transferDateId",required=false) Date transferDateId,
			@RequestParam(value = "warehouseFrom",required=false) Integer warehouseFrom,
			@RequestParam(value = "estDateId",required=false) Date estDateId,
			@RequestParam(value = "warehouseTo",required=false) Integer warehouseTo,
			@RequestParam(value = "recDateId",required=false) String recDateId,
			@RequestParam(value = "transNo",required=false) Integer transNo,
			@RequestParam(value = "ref",required=false) String ref,
			@RequestParam(value = "prTransferId",required=false) Integer prTransferId,
			@RequestParam(value = "prMasterId",required=false) Integer prMasterId,
			@RequestParam(value = "prTransferDetailId",required=false) Integer prTransferDetailId,
			@RequestParam(value = "quantityAvailable", required = false) BigDecimal quantityAvailable,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "itemCode", required = false) String itemCode,
			@RequestParam(value = "quantityTransfered", required = false) BigDecimal quantityTransfered,
			@RequestParam(value = "oper", required = false) String Oper,
			/*@RequestParam(value = "prMasterId", required = false) Integer prMasterID,
			@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,
			@RequestParam(value = "quantityOrdered", required = false) BigDecimal quantityOrder,
			@RequestParam(value = "taxable", required = false) String Taxable,
			@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,
			@RequestParam(value = "cuSodetailId", required = false) Integer cuSODetailID,
			@RequestParam(value = "taxRate", required = false) Double taxRate,
			@RequestParam(value = "freight", required = false) BigDecimal freight,*/
			HttpServletRequest therequest,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		CustomResponse aResponse = new CustomResponse();
		PrwarehouseTransfer aPrwarehouseTransfer = new PrwarehouseTransfer();
		Prwhtransferdetail aPrwhtransferdetail = new Prwhtransferdetail();
		PrwarehouseTransfer aPrwarehouseTransferObj = null;

		System.out.println("inside main "+prTransferDetailId+Oper);
		
		try {
		if(transferDateId != null)			
			aPrwarehouseTransfer.setTransferDate(transferDateId);
		
		if(warehouseFrom != null && warehouseFrom != 0)
			aPrwarehouseTransfer.setPrFromWarehouseId(warehouseFrom);
		/*if(recDateId != null)			
			aPrwarehouseTransfer.setReceivedDate(recDateId);*/
		if(estDateId != null)			
			aPrwarehouseTransfer.setEstreceiveDate(estDateId);
		if(warehouseTo != null && warehouseTo != 0)
			aPrwarehouseTransfer.setPrToWarehouseId(warehouseTo);
		if(transNo != null && transNo != 0)
			aPrwarehouseTransfer.setTransactionNumber(transNo);
		if(ref != null)
			aPrwarehouseTransfer.setDesc(ref);
		
		if(quantityAvailable != null)
			aPrwhtransferdetail.setQuantityAvailable(quantityAvailable);
		if(quantityTransfered != null)
			aPrwhtransferdetail.setQuantityTransfered(quantityTransfered);
		if(desc != null)
			aPrwhtransferdetail.setDescription(desc);
		if(prMasterId != null && prMasterId != 0)
			aPrwhtransferdetail.setPrMasterId(prMasterId);
		
		if(prTransferDetailId != null)
			aPrwhtransferdetail.setPrTransferDetailId(prTransferDetailId);
		if(itemCode != null)
			aPrwhtransferdetail.setItemCode(itemCode);
		aPrwarehouseTransfer.setPrTransferId(prTransferId);	
		aPrwhtransferdetail.setPrTransferId(prTransferId);
		aPrwarehouseTransfer.setScreenno(1);
		/*aPrwhtransferdetail.setDifference((quantityAvailable==null?new BigDecimal(0):quantityAvailable).subtract(quantityTransfered==null?new BigDecimal(0):quantityTransfered));*/
		aPrwhtransferdetail.setUserId(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
		aPrwhtransferdetail.setUserName(((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
		
		if(prTransferDetailId != null && prTransferDetailId != 0){
			itsInventoryService.saveTransferLineItems(aPrwarehouseTransfer,aPrwhtransferdetail,Oper);
			aPrwarehouseTransfer = itsInventoryService.updateTransferDetails(aPrwarehouseTransfer);
		}
		else{
			if(aPrwarehouseTransfer.getPrTransferId()!=null){
				itsInventoryService.saveTransferLineItems(aPrwarehouseTransfer,aPrwhtransferdetail,Oper);
				aPrwarehouseTransfer = itsInventoryService.updateTransferDetails(aPrwarehouseTransfer);
			}else{
				aPrwarehouseTransfer.setCreatedByID(aUserBean.getUserId());
				aPrwarehouseTransfer.setCreatedOn(new Date());
				aPrwarehouseTransfer = itsInventoryService.saveTransferDetails(aPrwarehouseTransfer);
				aPrwhtransferdetail.setPrTransferId(aPrwarehouseTransfer.getPrTransferId());
				itsInventoryService.saveTransferLineItems(aPrwarehouseTransfer,aPrwhtransferdetail,Oper);
			}
		}
		} catch (InventoryException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			 //sentransactionException(String HeaderMsg,String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest)
			sendTransactionException("<b>prTransferId==></b>"+prTransferId,"Inventory",e,session,therequest);
		} finally {
		}
		return aPrwarehouseTransfer;
	}
	
	
	@RequestMapping(value = "/receiveItemsTransferDetails", method = RequestMethod.POST)
	public @ResponseBody
	String receiveItemsTransferDetails(@RequestParam(value = "prTransferId",required=false) Integer prTransferId,
			@RequestParam(value = "operation",required=false) String operation,
			HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		String result = "";
		
		try {
			
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			itsLogger.info("Operation::"+operation);
			result = itsInventoryService.updatePrMasterForReceiveItemsWTransfer(prTransferId, operation,aUserBean.getUserId(),aUserBean.getFullName());
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//sentransactionException(String HeaderMsg,String trackingID,String jobstatus,Exception e,HttpSession session,HttpServletRequest therequest)
			sendTransactionException("<b>prTransferId==></b>"+prTransferId,"Inventory",e,session,therequest);
		}
		return result;
	}	
	
	
	@RequestMapping(value = "/copyreceiveItems", method = RequestMethod.POST)
	public @ResponseBody
	PrwarehouseTransfer copyreceiveItems(
			@RequestParam(value = "transferDateId",required=false) Date transferDateId,
			@RequestParam(value = "warehouseFrom",required=false) Integer warehouseFrom,
			@RequestParam(value = "estDateId",required=false) Date estDateId,
			@RequestParam(value = "warehouseTo",required=false) Integer warehouseTo,
			@RequestParam(value = "recDateId",required=false) String recDateId,
			@RequestParam(value = "transNo",required=false) Integer transNo,
			@RequestParam(value = "ref",required=false) String ref,
			@RequestParam(value = "prTransferId",required=false) Integer prTransferId,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		//transferDateId=07%2F10%2F2014&warehouseFrom=1&estDateId=07%2F15%2F2014&warehouseTo=2&recDateId=07%2F30%2F2014&transNo=1&ref=test&prTransferId=&prMasterId=
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		PrwarehouseTransfer aPrwarehouseTransfer = new PrwarehouseTransfer();
		try {
			if(transferDateId != null)			
				aPrwarehouseTransfer.setTransferDate(transferDateId);
			
			if(warehouseFrom != null && warehouseFrom != 0)
				aPrwarehouseTransfer.setPrFromWarehouseId(warehouseFrom);
			/*if(recDateId != null)			
				aPrwarehouseTransfer.setReceivedDate(recDateId);*/
			if(estDateId != null)			
				aPrwarehouseTransfer.setEstreceiveDate(estDateId);
			if(warehouseTo != null && warehouseTo != 0)
				aPrwarehouseTransfer.setPrToWarehouseId(warehouseTo);
			if(transNo != null && transNo != 0)
				aPrwarehouseTransfer.setTransactionNumber(transNo);
			if(ref != null)
				aPrwarehouseTransfer.setDesc(ref);
			/*if(prTransferId != null && prTransferId != 0){
				aPrwarehouseTransfer.setPrTransferId(prTransferId);	
				aPrwarehouseTransfer = itsInventoryService.updateTransferDetails(aPrwarehouseTransfer);
			}*/
			//else
			
			aPrwarehouseTransfer.setScreenno(1);
			aPrwarehouseTransfer = itsInventoryService.copyTransferDetails(aPrwarehouseTransfer);
			System.out.println("prTransferId in Copy is :::"+prTransferId);
			itsInventoryService.copyTransferLineDetails(prTransferId,aPrwarehouseTransfer.getPrTransferId());
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b>prTransferId==></b>"+prTransferId,"Inventory",e,session,therequest);
		}
		return aPrwarehouseTransfer;

	}
	
	@RequestMapping(value = "/printInventoryCSV", method = RequestMethod.GET)
	public @ResponseBody
	void printInventoryCSV(
			@RequestParam(value = "inactive", required = false) Integer inactive,
			@RequestParam(value = "itemCode", required = false) String itemCode,
			@RequestParam(value = "warehouseName", required = false) String warehouseName,
			@RequestParam(value = "warehouseID", required = false) Integer warehouseId,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			throws IOException, MessagingException, SQLException {
		
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String condition = "";
			if(warehouseId==null || warehouseId==-1){
				condition += " pw.prWarehouseID <> -1 ";
			}else{
				condition += " pw.prWarehouseID = "+warehouseId+" ";
			}
			if(inactive==null || inactive==0){
				condition +=" AND pr.InActive=0 ";
			}else{
				condition +=" AND pr.InActive=1 ";
			}
			if(itemCode != null){
				condition+=" AND pr.ItemCode LIKE '%"+itemCode+"%'";
				}
			
				path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/InventoryList.jrxml");
				filename="InventoryList.csv";
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			String query = "SELECT pr.prMasterID,pr.ItemCode,pr.Description,pw.InventoryOnHand,pw.InventoryAllocated,pw.InventoryOnOrder,pr.Submitted,(pw.InventoryOnHand-pw.InventoryAllocated) AS Available,pd.Description as Department,pc.Description as Category,rx.Name,IFNULL(pr.AverageCost*pw.InventoryOnHand,0) AS TotalCost,pr.AverageCost,pwn.SearchName  FROM prMaster pr LEFT JOIN prWarehouseInventory pw ON pr.prMasterID = pw.prMasterID LEFT JOIN prDepartment pd ON pd.prDepartmentID=pr.prDepartmentID LEFT JOIN prCategory pc ON pc.prCategoryID = pr.prCategoryID LEFT JOIN rxMaster rx ON rx.rxMasterID = pr.rxMasterIDPrimaryVendor  LEFT JOIN prWarehouse pwn ON pw.prWarehouseID=pwn.prWarehouseID WHERE  "+condition+" ORDER BY  pr.ItemCode ASC";
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			//Have to set Params
			params.put("condition", condition);
			params.put("warehouseName", warehouseName);
			
			connection = con.getConnection();
			System.out.println(query+ " || condition :: "+condition);
			ReportService.dynamicReportCall(theResponse,params,"xls",jd,filename,connection);

		} catch (Exception e) {
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> itemCode,warehouseId==></b>"+ itemCode+","+warehouseId,"Inventory",e,session,therequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				} 
		}
	}
	
	@RequestMapping(value = "/printOrderPoint", method = RequestMethod.GET)
	public @ResponseBody
	void printOrderPoint(
			@RequestParam(value = "inactive", required = false) Integer inactive,
			@RequestParam(value = "itemCode", required = false) String theSearchString,
			@RequestParam(value = "warehouseID", required = false) Integer warehouseId,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			throws IOException, MessagingException, SQLException {
		Connection connection  = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String condition = "";
			
		//String query = "SELECT a.prMasterID,a.ItemCode,a.Description,a.BoxQty, a.InventoryOnHand, a.InventoryAllocated,(a.InventoryOnHand-a.InventoryAllocated) AS Available, a.InventoryOnOrder, b.InventoryOrderPoint, (b.InventoryOrderPoint+b.InventoryOrderQuantity) AS suggestedOrder, 0 AS YTDSales, rx.Name as vName FROM prMaster a LEFT JOIN rxMaster rx ON rx.rxMasterID = a.rxMasterIDPrimaryVendor LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID LEFT JOIN prDepartment c ON a.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID "+aStringBuilder;
			String query="SELECT P.prMasterID, P.ItemCode, P.Description,P.BoxQty,W.InventoryOnHand,W.InventoryAllocated, W.InventoryOnHand-W.InventoryAllocated AS Available,W.InventoryOnOrder, O.InventoryOrderPoint, "+
						 //"(O.InventoryOrderPoint+O.InventoryOrderQuantity) AS suggestedOrder,"+
						// "IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) )) AS suggestedOrder ,"+
						 "IF((W.InventoryOnHand-W.InventoryAllocated)>O.InventoryOrderPoint || W.InventoryOnOrder>O.InventoryOrderPoint,0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ) AS suggestedOrder,"+
						 "getYeartoDate(P.prMasterID) AS YTDSales, rx.Name AS vName ,prw.SearchName,P.rxMasterIDPrimaryVendor AS vendorid "+
						 "FROM (prMaster AS P LEFT JOIN (SELECT * FROM prWarehouseInventory WHERE prWarehouseID = "+warehouseId+")  AS W ON P.prMasterID = W.prMasterID)  "+
						 "LEFT JOIN (SELECT * FROM prOrderPoint WHERE prWarehouseID = "+warehouseId+")  AS O  ON P.prMasterID = O.prMasterID "+
						 "LEFT JOIN rxMaster rx ON rx.rxMasterID = P.rxMasterIDPrimaryVendor "+
						 "LEFT JOIN prDepartment c ON P.prDepartmentID = c.prDepartmentID   "+
						 "LEFT JOIN prCategory d ON P.prCategoryID=d.prCategoryID LEFT JOIN prWarehouse prw ON prw.prWarehouseID="+warehouseId+
						 " WHERE P.IsInventory=1 AND P.InActive=0 AND "+
						 //" (O.InventoryOrderPoint+O.InventoryOrderQuantity)>0 "+
						 //"IF(O.InventoryOrderPoint+O.InventoryOrderQuantity IS NULL,0,IF((O.InventoryOrderPoint+O.InventoryOrderQuantity)>60 &&(W.InventoryOnHand>60 || W.InventoryOnOrder>60),0,(O.InventoryOrderPoint+O.InventoryOrderQuantity) ))>0 "+
						 //"IFNULL((W.InventoryOnHand-W.InventoryAllocated),0)<IFNULL(O.InventoryOrderPoint,0) AND IFNULL(W.InventoryOnOrder,0)<IFNULL(O.InventoryOrderPoint,0 ) AND "+
						 "(IFNULL(O.InventoryOrderPoint,0)-(IFNULL(W.InventoryOnOrder, 0)+(IFNULL(W.InventoryOnHand,0)-IFNULL(W.InventoryAllocated,0))))>0 ";
			
			if(theSearchString!=null && theSearchString!=""){
				query = query+" And (P.Description LIKE '%"+theSearchString+"%' OR" +
							" P.ItemCode LIKE '%"+theSearchString+"%' OR" +
							" d.Description LIKE '%"+theSearchString+"%') ";
				 
			}	
			
			query=query+" ORDER BY rx.Name,P.ItemCode";
			
			System.out.println("Customer Qry : "+query);
		
		
			path_JRXML = therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/orderpoints.jrxml");
				filename="orderPoints.pdf";
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			//String query = "SELECT a.prMasterID,a.ItemCode,a.Description,a.BoxQty, a.InventoryOnHand, a.InventoryAllocated,(a.InventoryOnHand-a.InventoryAllocated) AS Available, a.InventoryOnOrder, b.InventoryOrderPoint, (b.InventoryOrderPoint+b.InventoryOrderQuantity) AS suggestedOrder FROM prMaster a LEFT JOIN prOrderPoint b ON a.prMasterID =b.prMasterID LEFT JOIN prDepartment c ON a.prDepartmentID = c.prDepartmentID LEFT JOIN prCategory d ON a.prCategoryID=d.prCategoryID LEFT JOIN rxMaster rx ON rx.rxMasterID = pr.rxMasterIDPrimaryVendor  LEFT JOIN prwarehouse pwn ON pw.prWarehouseID=pwn.prWarehouseID WHERE  "+condition+" ORDER BY  pr.ItemCode ASC";
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			//Have to set Params
			
			connection = con.getConnection();
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);

		} catch (Exception e) {
			theResponse.sendError(500, e.getMessage()); 
			sendTransactionException("<b> warehouseId,theSearchString==></b>"+ warehouseId+","+theSearchString,"Inventory",e,session,therequest);
		}
		finally
		{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}	
	
	@RequestMapping(value = "/getprcategorymarkup", method = RequestMethod.POST)
	public @ResponseBody PrCategory getprcategorymarkup(@RequestParam(value = "prcategoryid",required=false) Integer prcategoryid,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		PrCategory prcat=new PrCategory();
		try {
			prcat = itsInventoryService.getprCategories(prcategoryid);
		} catch (InventoryException e) {
			// TODO Auto-generated catch block
			itsLogger.info(e.getMessage());
			sendTransactionException("<b> prcategoryid==></b>"+ prcategoryid,"Inventory",e,session,therequest);
		}
		return prcat;
	}	
	
	
	@RequestMapping(value = "/saveInventorysettings", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> saveInventorysettings(
			@RequestParam(value = "txt_overridewarehouse",required=false)String txt_overridewarehouse,
			@RequestParam(value = "chk_invoverridewarehouseYes",required=false) Integer chk_invoverridewarehouseYes,
			@RequestParam(value = "chk_invpurOrderCostYes",required=false) Integer chk_invpurOrderCostYes,
			@RequestParam(value = "chk_invwareavgcostYes",required=false) Integer chk_invwareavgcostYes,
			@RequestParam(value = "chk_invbinLocationYes",required=false) Integer chk_invbinLocationYes,
			@RequestParam(value = "chk_invWeightYes",required=false) Integer chk_invWeightYes,
			@RequestParam(value = "chk_invWarhouseaddressYes",required=false) Integer chk_invWarhouseaddressYes,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		  ArrayList<Sysvariable> asysvariable = new  ArrayList<Sysvariable>();
		try {
			//Textbox 1row
			Sysvariable thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_overridewarehouse);
			thesysvariable.setValueLong(chk_invoverridewarehouseYes);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("OverrideallWarehousingCostMarkupinsideCategory"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("OverrideallWarehousingCostMarkupinsideCategory"));
			asysvariable.add(thesysvariable);
			
			//secondrow chk_invpurOrderCostYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invpurOrderCostYes);
			System.out.println("sysvariableid chk_invpurOrderCostYes==>"+InventoryConstant.getConstantSysvariableId("SpecifyPurchaseOrdercost"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SpecifyPurchaseOrdercost"));
			asysvariable.add(thesysvariable);
			
			//thirdrow MultipleWarehousesAverageCosting
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invwareavgcostYes);
			System.out.println("sysvariableid MultipleWarehousesAverageCosting==>"+InventoryConstant.getConstantSysvariableId("MultipleWarehousesAverageCosting"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("MultipleWarehousesAverageCosting"));
			asysvariable.add(thesysvariable);
			
			//fourthrow chk_invbinLocationYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invbinLocationYes);
			System.out.println("sysvariableid ShowBinLocationonPickTickets==>"+InventoryConstant.getConstantSysvariableId("ShowBinLocationonPickTickets"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShowBinLocationonPickTickets"));
			asysvariable.add(thesysvariable);
			
			//fifthrow chk_invWeightYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invWeightYes);
			System.out.println("sysvariableid ShowWeightonPickTickets==>"+InventoryConstant.getConstantSysvariableId("ShowWeightonPickTickets"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShowWeightonPickTickets"));
			asysvariable.add(thesysvariable);
			
			//sixthrow chk_invWarhouseaddressYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invWarhouseaddressYes);
			System.out.println("sysvariableid UseWarehousesaddressonPickTickets==>"+InventoryConstant.getConstantSysvariableId("UseWarehousesaddressonPickTickets"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("UseWarehousesaddressonPickTickets"));
			asysvariable.add(thesysvariable);
			
			
			boolean	insertintosysvariable = itsInventoryService.saveInventorysettings(asysvariable);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveInventorysettings","Inventory",e,session,therequest);
		}
		return asysvariable;

	}
	

	@RequestMapping(value = "/saveJobSettingsSysVariable", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> saveJobSettingsSysVariable(
			@RequestParam(value = "chkjobcheckoffYN",required=false) Integer chkjobcheckoffYN,
			@RequestParam(value = "chkchangejobYN",required=false) Integer chkchangejobYN,
			@RequestParam(value = "chkmemoYN",required=false) Integer chkmemoYN,
			@RequestParam(value = "chkjobnoticelbl1YN",required=false) Integer chkjobnoticelbl1YN,
			@RequestParam(value = "chkjobnoticereporttype1YN",required=false) Integer chkjobnoticereporttype1YN,
			@RequestParam(value = "chkjobnoticelbl2YN",required=false) Integer chkjobnoticelbl2YN,
			@RequestParam(value = "chkjobnoticereporttype2YN",required=false) Integer chkjobnoticereporttype2YN,
			@RequestParam(value = "chkjobnoticelbl3YN",required=false) Integer chkjobnoticelbl3YN,
			@RequestParam(value = "chkjobnoticereporttype3YN",required=false) Integer chkjobnoticereporttype3YN,
			
			@RequestParam(value = "chkjobnoticelbl1TXT",required=false) String chkjobnoticelbl1TXT,
			@RequestParam(value = "chkjobnoticereporttype1TXT",required=false) String chkjobnoticereporttype1TXT,
			@RequestParam(value = "chkjobnoticelbl2TXT",required=false) String chkjobnoticelbl2TXT,
			@RequestParam(value = "chkjobnoticereporttype2TXT",required=false) String chkjobnoticereporttype2TXT,
			@RequestParam(value = "chkjobnoticelbl3TXT",required=false) String chkjobnoticelbl3TXT,
			@RequestParam(value = "chkjobnoticereporttype3TXT",required=false) String chkjobnoticereporttype3TXT,
			
			@RequestParam(value = "chkbidtocurdateYN",required=false) Integer chkbidtocurdateYN,
			@RequestParam(value = "chkdeftakeoffYN",required=false) Integer chkdeftakeoffYN,
			@RequestParam(value = "chkdefsalesrepYN",required=false) Integer chkdefsalesrepYN,
			@RequestParam(value = "chkdefdivisionYN",required=false) Integer chkdefdivisionYN,
			@RequestParam(value = "chkaddendumYN",required=false) Integer chkaddendumYN,
			@RequestParam(value = "chkdefcreditYN",required=false) Integer chkdefcreditYN,
			@RequestParam(value = "chkdefaultquoteYN",required=false) Integer chkdefaultquoteYN,
			@RequestParam(value = "chkjobdetailotherlblYN",required=false) Integer chkjobdetailotherlblYN,
			@RequestParam(value = "chkeditsentquoteYN",required=false) Integer chkeditsentquoteYN,
			
			@RequestParam(value = "sourceCheckBox1",required=false) String sourceCheckBox1,
			@RequestParam(value = "sourceCheckBox2",required=false) String sourceCheckBox2,
			@RequestParam(value = "sourceCheckBox3",required=false) String sourceCheckBox3,
			@RequestParam(value = "sourceCheckBox4",required=false) String sourceCheckBox4,			
			@RequestParam(value = "sourceLabel1",required=false) String sourceLabel1,
			@RequestParam(value = "sourceLabel2",required=false) String sourceLabel2,
			
			@RequestParam(value = "chkjodetreplabYN",required=false) Integer chkjodetreplabYN,
			@RequestParam(value = "chkjodetreplabTXT",required=false) String chkjodetreplabTXT,
			@RequestParam(value = "chkappcocusinvYN",required=false) Integer chkappcocusinvYN,
			@RequestParam(value = "chkcloselastinvchkDays",required=false) Integer chkcloselastinvchkDays,
			@RequestParam(value = "chkcloselastinvchkYN",required=false) Integer chkcloselastinvchkYN,
			@RequestParam(value = "chkreqdivcreatjobYN",required=false) Integer chkreqdivcreatjobYN,
			@RequestParam(value = "chkcuscredlimcrjobsYN",required=false) Integer chkcuscredlimcrjobsYN,
			@RequestParam(value = "chkalwbookjobscamtYN",required=false) Integer chkalwbookjobscamtYN,
			@RequestParam(value = "chkimpponewcustomerYN",required=false) Integer chkimpponewcustomerYN,
			@RequestParam(value = "chkshwbilremjobstYN",required=false) Integer chkshwbilremjobstYN,
			@RequestParam(value = "chkhidepostjobsYN",required=false) Integer chkhidepostjobsYN,
			@RequestParam(value = "chkonalsuqottempYN",required=false) Integer chkonalsuqottempYN,
			@RequestParam(value = "chkalrwaichekedYN",required=false) Integer chkalrwaichekedYN,
			@RequestParam(value = "chkwnocrordYN",required=false) Integer chkwnocrordYN,
			@RequestParam(value = "chktkcoschordYN",required=false) Integer chktkcoschordYN,
			@RequestParam(value = "chkshowbranchYN",required=false) Integer chkshowbranchYN,
			@RequestParam(value = "chkscrejobstYN",required=false) Integer chkscrejobstYN,
			@RequestParam(value = "chkreqengbookjobYN",required=false) Integer chkreqengbookjobYN,
			
			@RequestParam(value = "planSpecLabel1",required=false) String planSpecLabel1,
			@RequestParam(value = "planSpecLabel2",required=false) String planSpecLabel2,
			@RequestParam(value = "chkdefJobTaxTerritoryYN",required=false) Integer chkdefJobTaxTerritoryYN,
			@RequestParam(value = "chkUserCustomerCreditYN",required=false) Integer chkUserCustomerCreditYN,
			@RequestParam(value = "chkReqSplitCommissionYN",required=false) Integer chkReqSplitCommissionYN,
			@RequestParam(value = "chkdefOverRideTaxTerritoryYN", required=false)Integer chkdefOverRideTaxTerritoryYN,
			
			@RequestParam(value = "chkI2I3QtyYN",required=false) Integer chkI2I3QtyYN,
			@RequestParam(value = "chkI2I3CostYN",required=false) Integer chkI2I3CostYN,
			@RequestParam(value = "chkI3SellPriceYN",required=false) Integer chkI3SellPriceYN,
			@RequestParam(value = "chkI2I3ManufYN",required=false) Integer chkI2I3ManufYN,
			@RequestParam(value = "chkI2I3CatYN",required=false) Integer chkI2I3CatYN,
			@RequestParam(value = "chkSPpriceYN",required=false) Integer chkSPpriceYN,
			@RequestParam(value = "chkreqCPOcreatjobYN",required=false) Integer chkreqCPOcreatjobYN,
			
			@RequestParam(value = "chkfontsizeonPriceYN",required=false) Integer chkfontsizeonPriceYN,
			@RequestParam(value = "fontsizeonPriceValue",required=false) String fontsizeonPriceValue,
			
			HttpSession session,
			HttpServletRequest therequest) throws IOException, MessagingException {
		  ArrayList<Sysvariable> asysvariable = new  ArrayList<Sysvariable>();
		  
		  itsLogger.info(chkjobcheckoffYN+" || >>> "+chkchangejobYN+" || >>> "+chkmemoYN+" || >>> "+chkjobnoticelbl1YN+" || >>> "+" || >>> "+chkjobnoticereporttype1YN+" || >>> "+chkjobnoticelbl2YN+" || >>> "+chkjobnoticereporttype2YN
				  +" || >>> "+chkjobnoticelbl3YN+" || >>> "+chkjobnoticereporttype3YN+" || >>> "+chkbidtocurdateYN+" || >>> "+chkdeftakeoffYN+" || >>> "+chkdefsalesrepYN+" || >>> "+chkdefdivisionYN+" || >>> "+chkaddendumYN
				  		+" || >>> "+chkdefcreditYN+" || >>> "+chkdefaultquoteYN+" || >>> "+chkjobdetailotherlblYN+" || >>> "+chkeditsentquoteYN+" || >>> "+chkjodetreplabYN+" || >>> "+chkappcocusinvYN+" || >>> "+
				  		chkcloselastinvchkYN+" || >>> "+chkreqdivcreatjobYN+" || >>> "+chkcuscredlimcrjobsYN+" || >>> "+chkalwbookjobscamtYN+" || >>> "+chkimpponewcustomerYN+" || >>> "+chkshwbilremjobstYN+" || >>> "+chkhidepostjobsYN+" || >>> "+
				  		chkonalsuqottempYN+" || >>> "+chkalrwaichekedYN+" || >>> "+chkwnocrordYN+" || >>> "+chktkcoschordYN+" || >>> "+chkshowbranchYN+" || >>> "+chkscrejobstYN+" || >>> "+chkreqengbookjobYN);
				  	
		  
		try {
			Sysvariable thesysvariable=new Sysvariable();
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobcheckoffYN);
			System.out.println("sysvariableid AllowchangingJob==>"+InventoryConstant.getConstantSysvariableId("AllowchangingJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowchangingJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdefJobTaxTerritoryYN);
			System.out.println("sysvariableid DefaultJobTaxTerritorytoCustomerTax ==> "+InventoryConstant.getConstantSysvariableId("DefaultJobTaxTerritorytoCustomerTax"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultJobTaxTerritorytoCustomerTax"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkUserCustomerCreditYN);
			System.out.println("sysvariableid UseCustomersCreditLimitwhencreatingJobs ==> "+InventoryConstant.getConstantSysvariableId("UseCustomersCreditLimitwhencreatingJobs"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("UseCustomersCreditLimitwhencreatingJobs"));
			asysvariable.add(thesysvariable);
			
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkchangejobYN);
			System.out.println("sysvariableid OptionalJobQuoteCheckoff ==> "+InventoryConstant.getConstantSysvariableId("OptionalJobQuoteCheckoff"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("OptionalJobQuoteCheckoff"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkmemoYN);
			System.out.println("sysvariableid OptionalMemoFieldonQuote ==> "+InventoryConstant.getConstantSysvariableId("OptionalMemoFieldonQuote"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("OptionalMemoFieldonQuote"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticelbl1YN);
			System.out.println("sysvariableid JobNoticelabel1 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel1"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticereporttype1YN);
			System.out.println("sysvariableid JobNoticeReportType1 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType1"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticelbl2YN);
			System.out.println("sysvariableid JobNoticelabel2 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel2"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticereporttype2YN);
			System.out.println("sysvariableid JobNoticeReportType2 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType2"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticelbl3YN);
			System.out.println("sysvariableid JobNoticelabel3 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel3"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel3"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobnoticereporttype3YN);
			System.out.println("sysvariableid JobNoticeReportType3 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType3"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType3"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticelbl1TXT);
			thesysvariable.setValueLong(chkjobnoticelbl1YN);
			System.out.println("sysvariableid JobNoticelabel1 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel1Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel1Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticereporttype1TXT);
			System.out.println("sysvariableid JobNoticeReportType1Txt ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType1Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType1Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticelbl2TXT);
			thesysvariable.setValueLong(chkjobnoticelbl2YN);
			System.out.println("sysvariableid JobNoticelabel2 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel2Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel2Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticereporttype2TXT);
			System.out.println("sysvariableid JobNoticeReportType2Txt ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType2Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType2Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticelbl3TXT);
			thesysvariable.setValueLong(chkjobnoticelbl3YN);
			System.out.println("sysvariableid JobNoticelabel3 ==> "+InventoryConstant.getConstantSysvariableId("JobNoticelabel3Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticelabel3Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjobnoticereporttype1TXT);
			System.out.println("sysvariableid JobNoticeReportType1Txt ==> "+InventoryConstant.getConstantSysvariableId("JobNoticeReportType1Txt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobNoticeReportType1Txt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkbidtocurdateYN);
			System.out.println("sysvariableid DefaultBidDatetoCurrentDate ==> "+InventoryConstant.getConstantSysvariableId("DefaultBidDatetoCurrentDate"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultBidDatetoCurrentDate"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdeftakeoffYN);
			System.out.println("sysvariableid DefaultTakeOfftoUsercreatingtheJob ==> "+InventoryConstant.getConstantSysvariableId("DefaultTakeOfftoUsercreatingtheJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultTakeOfftoUsercreatingtheJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdefsalesrepYN);
			System.out.println("sysvariableid DefaultSalesReptoUserCreatingtheJob ==> "+InventoryConstant.getConstantSysvariableId("DefaultSalesReptoUserCreatingtheJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultSalesReptoUserCreatingtheJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdefdivisionYN);
			System.out.println("sysvariableid DefaultDivisiontoUserCreatingtheJob ==> "+InventoryConstant.getConstantSysvariableId("DefaultDivisiontoUserCreatingtheJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultDivisiontoUserCreatingtheJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkaddendumYN);
			System.out.println("sysvariableid Musthaveaddendumquotethruandbiddatebeforesave ==> "+InventoryConstant.getConstantSysvariableId("Musthaveaddendumquotethruandbiddatebeforesave"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("Musthaveaddendumquotethruandbiddatebeforesave"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdefcreditYN);
			System.out.println("sysvariableid DefaulttoApprovedcreditforanewJob ==> "+InventoryConstant.getConstantSysvariableId("DefaulttoApprovedcreditforanewJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaulttoApprovedcreditforanewJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkdefaultquoteYN);
			System.out.println("sysvariableid DefaultQuoteBytoUSercreatingtheJob ==> "+InventoryConstant.getConstantSysvariableId("DefaultQuoteBytoUSercreatingtheJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultQuoteBytoUSercreatingtheJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjobdetailotherlblYN);
			System.out.println("sysvariableid JobDetailsOtherlabel ==> "+InventoryConstant.getConstantSysvariableId("JobDetailsOtherlabel"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobDetailsOtherlabel"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkeditsentquoteYN);
			System.out.println("sysvariableid AllowEditingofSentQuotes ==> "+InventoryConstant.getConstantSysvariableId("AllowEditingofSentQuotes"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowEditingofSentQuotes"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceCheckBox1);
			System.out.println("sysvariableid SourceCheckBox1 ==> "+InventoryConstant.getConstantSysvariableId("SourceCheckBox1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceCheckBox1"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceCheckBox2);
			System.out.println("sysvariableid SourceCheckBox2 ==> "+InventoryConstant.getConstantSysvariableId("SourceCheckBox2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceCheckBox2"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceCheckBox3);
			System.out.println("sysvariableid SourceCheckBox3 ==> "+InventoryConstant.getConstantSysvariableId("SourceCheckBox3"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceCheckBox3"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceCheckBox4);
			System.out.println("sysvariableid SourceCheckBox4 ==> "+InventoryConstant.getConstantSysvariableId("SourceCheckBox4"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceCheckBox4"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceLabel1);
			System.out.println("sysvariableid SourceLabel1 ==> "+InventoryConstant.getConstantSysvariableId("SourceLabel1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceLabel1"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(sourceLabel2);
			System.out.println("sysvariableid SourceLabel2 ==> "+InventoryConstant.getConstantSysvariableId("SourceLabel2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SourceLabel2"));
			asysvariable.add(thesysvariable);
	
			
			/////////////
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkjodetreplabYN);
			System.out.println("sysvariableid JobDetailsReportlabel ==> "+InventoryConstant.getConstantSysvariableId("JobDetailsReportlabel"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobDetailsReportlabel"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(chkjodetreplabTXT);
			System.out.println("sysvariableid JobDetailsReportlabelTxt ==> "+InventoryConstant.getConstantSysvariableId("JobDetailsReportlabelTxt"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("JobDetailsReportlabelTxt"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkappcocusinvYN);
			System.out.println("sysvariableid Applycoststocustomerinvoice ==> "+InventoryConstant.getConstantSysvariableId("Applycoststocustomerinvoice"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("Applycoststocustomerinvoice"));
			asysvariable.add(thesysvariable);
			
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkcloselastinvchkYN);
			System.out.println("sysvariableid Closeoutlastinvoicecheckdaysold ==> "+InventoryConstant.getConstantSysvariableId("Closeoutlastinvoicecheckdaysold"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("Closeoutlastinvoicecheckdaysold"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkcloselastinvchkDays);
			System.out.println("sysvariableid CloseoutlastinvoicecheckdaysoldDays ==> "+InventoryConstant.getConstantSysvariableId("CloseoutlastinvoicecheckdaysoldDays"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CloseoutlastinvoicecheckdaysoldDays"));
			asysvariable.add(thesysvariable);
			/////////////
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkreqdivcreatjobYN);
			System.out.println("sysvariableid RequireaDivisionwhencreatingJobs ==> "+InventoryConstant.getConstantSysvariableId("RequireaDivisionwhencreatingJobs"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireaDivisionwhencreatingJobs"));
			asysvariable.add(thesysvariable);
			
//			thesysvariable=new Sysvariable();
//			thesysvariable.setValueLong(chkUserCustomerCreditYN);
//			System.out.println("sysvariableid UseCustomersCreditLimitwhencreatingJobs ==> "+InventoryConstant.getConstantSysvariableId("UseCustomersCreditLimitwhencreatingJobs"));
//			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("UseCustomersCreditLimitwhencreatingJobs"));
//			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkalwbookjobscamtYN);
			System.out.println("sysvariableid AllowbookingJobswithnoContractAmount ==> "+InventoryConstant.getConstantSysvariableId("AllowbookingJobswithnoContractAmount"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowbookingJobswithnoContractAmount"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkimpponewcustomerYN);
			System.out.println("sysvariableid ImportPurchaseOrderdetailsintonewCustomerInvoices ==> "+InventoryConstant.getConstantSysvariableId("ImportPurchaseOrderdetailsintonewCustomerInvoices"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ImportPurchaseOrderdetailsintonewCustomerInvoices"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkshwbilremjobstYN);
			System.out.println("sysvariableid ShowBillingRemainderonJobStatements ==> "+InventoryConstant.getConstantSysvariableId("ShowBillingRemainderonJobStatements"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShowBillingRemainderonJobStatements"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkhidepostjobsYN);
			System.out.println("sysvariableid HidePostponedoptioninJobs ==> "+InventoryConstant.getConstantSysvariableId("HidePostponedoptioninJobs"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("HidePostponedoptioninJobs"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkonalsuqottempYN);
			System.out.println("sysvariableid OnlyallowownersorsupervisorstochangeQuoteTemplates ==> "+InventoryConstant.getConstantSysvariableId("OnlyallowownersorsupervisorstochangeQuoteTemplates"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("OnlyallowownersorsupervisorstochangeQuoteTemplates"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkalrwaichekedYN);
			System.out.println("sysvariableid AllowreleasesifFinalWaiverchecked ==> "+InventoryConstant.getConstantSysvariableId("AllowreleasesifFinalWaiverchecked"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowreleasesifFinalWaiverchecked"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkwnocrordYN);
			System.out.println("sysvariableid WarnifNOCnotreceivedwhenordering ==> "+InventoryConstant.getConstantSysvariableId("WarnifNOCnotreceivedwhenordering"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("WarnifNOCnotreceivedwhenordering"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chktkcoschordYN);
			System.out.println("sysvariableid TrackCostonChangeOrders ==> "+InventoryConstant.getConstantSysvariableId("TrackCostonChangeOrders"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("TrackCostonChangeOrders"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkshowbranchYN);
			System.out.println("sysvariableid ShowBranches ==> "+InventoryConstant.getConstantSysvariableId("ShowBranches"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShowBranches"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkscrejobstYN);
			System.out.println("sysvariableid ShowcreditsonJobStatements ==> "+InventoryConstant.getConstantSysvariableId("ShowcreditsonJobStatements"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShowcreditsonJobStatements"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkreqengbookjobYN);
			System.out.println("sysvariableid RequireaEngineerwhenbookingJobs ==> "+InventoryConstant.getConstantSysvariableId("RequireaEngineerwhenbookingJobs"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireaEngineerwhenbookingJobs"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(planSpecLabel1);
			System.out.println("sysvariableid PlanSpecLabel1 ==> "+InventoryConstant.getConstantSysvariableId("PlanSpecLabel1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("PlanSpecLabel1"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueString(planSpecLabel2);
			System.out.println("sysvariableid PlanSpecLabel2 ==> "+InventoryConstant.getConstantSysvariableId("PlanSpecLabel2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("PlanSpecLabel2"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chkReqSplitCommissionYN);
			System.out.println("sysvariableid chkReqSplitCommissionYN ==> "+InventoryConstant.getConstantSysvariableId("SplitCommissionRequiredOnRelease"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("SplitCommissionRequiredOnRelease"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkdefOverRideTaxTerritoryYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("OverrideReleaseTaxTerritory"));
			asysvariable.add(thesysvariable);
			
			/*-------- Quote Settings------*/
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkI2I3QtyYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkI2I3QtyYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkI2I3CostYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkI2I3CostYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkI3SellPriceYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkI3SellPriceYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkI2I3ManufYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkI2I3ManufYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkI2I3CatYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkI2I3CatYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkSPpriceYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkSPpriceYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkreqCPOcreatjobYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CustomerPOReqYN"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chkfontsizeonPriceYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("QuotechkfontsizepriceYN"));
			thesysvariable.setValueString(fontsizeonPriceValue);
			asysvariable.add(thesysvariable);
			
			boolean	insertintosysvariable = itsInventoryService.saveInventorysettings(asysvariable);
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveJobSettingsSysVariable","Inventory",e,session,therequest);
		}
		return asysvariable;

	}
	
	//saveCustomerSettingsSysVariable",
    //chk_cusStGpbyJobYes chk_cusStShowBillRemYes chk_cusCreLiminSalOrdYes chk_cusCreLiminQuickBookYes chk_cusReqDivinSalOrdYes chk_cusReqDivinCusInvYes
  	//chk_cusIncSalTaxYes chk_cusIncFreightYes , chk_cusIncListYes, chk_cusIncExtListYes,chk_cusIncMultYes chk_cusUseDivAddYes chk_cusallowblinSalOrdYes chk_cusAllblCusInvYes chk_cusAllblProdinSalOrdYes chk_cusReqProinSalOrderYes
	@RequestMapping(value = "/saveCustomerSettingsSysVariable", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> saveCustomerSettingsSysVariable(
			@RequestParam(value = "chk_cusStGpbyJobYes",required=false) Integer chk_cusStGpbyJobYes,
			@RequestParam(value = "chk_cusStShowBillRemYes",required=false) Integer chk_cusStShowBillRemYes,
			@RequestParam(value = "chk_cusCreLiminSalOrdYes",required=false) Integer chk_cusCreLiminSalOrdYes,
			@RequestParam(value = "chk_cusCreLiminQuickBookYes",required=false) Integer chk_cusCreLiminQuickBookYes,
			@RequestParam(value = "chk_taxTerCuInvAftSaveYes",required=false) Integer chk_taxTerCuInvAftSaveYes,
			@RequestParam(value = "chk_cusReqDivinSalOrdYes",required=false) Integer chk_cusReqDivinSalOrdYes,
			@RequestParam(value = "chk_cusReqDivinCusInvYes",required=false) Integer chk_cusReqDivinCusInvYes,
			@RequestParam(value = "chk_cusIncSalTaxYes",required=false) Integer chk_cusIncSalTaxYes,
			@RequestParam(value = "chk_cusIncFreightYes",required=false) Integer chk_cusIncFreightYes,
			
			@RequestParam(value = "chk_cusIncListYes",required=false) Integer chk_cusIncListYes,
			@RequestParam(value = "chk_cusIncExtListYes",required=false) Integer chk_cusIncExtListYes,
			@RequestParam(value = "chk_cusIncMultYes",required=false) Integer chk_cusIncMultYes,
			
			@RequestParam(value = "chk_cusUseDivAddYes",required=false) Integer chk_cusUseDivAddYes,			
			@RequestParam(value = "chk_cusAllblCusInvYes",required=false) Integer chk_cusAllblCusInvYes,
			@RequestParam(value = "chk_cusAllblProdinSalOrdYes",required=false) Integer chk_cusAllblProdinSalOrdYes,
			@RequestParam(value = "chk_cusReqProinSalOrderYes",required=false) Integer chk_cusReqProinSalOrderYes,
			@RequestParam(value = "chk_cusReqfreinCuInvoicesYes",required=false) Integer chk_cusReqfreinCuInvoicesYes,
			@RequestParam(value = "chk_cusReqSeqNumCuInvoicesYes",required=false) Integer chk_cusReqSeqNumCuInvoicesYes,
			@RequestParam(value = "inp_cusNewSeqNumCuInvoices",required=false) String inp_cusNewSeqNumCuInvoices,
			@RequestParam(value = "chk_cusRemExtListfmSalOrdpdfYes",required=false) Integer chk_cusRemExtListfmSalOrdpdfYes,
			@RequestParam(value = "chk_cusRemMultfmSalOrdpdfYes",required=false) Integer chk_cusRemMultfmSalOrdpdfYes,
			
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		  ArrayList<Sysvariable> asysvariable = new  ArrayList<Sysvariable>();
		try {
			Sysvariable thesysvariable=new Sysvariable();
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusStGpbyJobYes);
			System.out.println("sysvariableid StatementsshallbegroupedbyJob==>"+InventoryConstant.getConstantSysvariableId("StatementsshallbegroupedbyJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("StatementsshallbegroupedbyJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusStShowBillRemYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("StatementsshallshowBillingRemainder"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("StatementsshallshowBillingRemainder"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusCreLiminSalOrdYes);
			System.out.println("sysvariableid CheckcreditlimitinSalesOrderOutsideofJob==>"+InventoryConstant.getConstantSysvariableId("CheckcreditlimitinSalesOrderOutsideofJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CheckcreditlimitinSalesOrderOutsideofJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusCreLiminQuickBookYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("CheckcreditlimitinQuickBook"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CheckcreditlimitinQuickBook"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_taxTerCuInvAftSaveYes);
			System.out.println("sysvariableid CheckTaxTerritoriesAfterSavingCustomerInvoice==>"+InventoryConstant.getConstantSysvariableId("DoNotAllowTaxTerritoryAfterSavingCustomerInvoice"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DoNotAllowTaxTerritoryAfterSavingCustomerInvoice"));
			asysvariable.add(thesysvariable);		
					
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqDivinSalOrdYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("RequireDivisioninSalesOrderOutsideofJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireDivisioninSalesOrderOutsideofJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqDivinCusInvYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("RequireDivisioninCustomerInvoice"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireDivisioninCustomerInvoice"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusIncSalTaxYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("IncludeSalesTaxwhencalculatingdiscounts"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncludeSalesTaxwhencalculatingdiscounts"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusIncFreightYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("IncludeFreightwhencalculatingdiscounts"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncludeFreightwhencalculatingdiscounts"));
			asysvariable.add(thesysvariable);
			
			
//			chk_cusIncListYes chk_cusIncExtListYes chk_cusIncMultYes
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusIncListYes);
			System.out.println("sysvariableid IncludeListcolumnoninvoices==>"+InventoryConstant.getConstantSysvariableId("IncludeListcolumnoninvoices"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncludeListcolumnoninvoices"));
			asysvariable.add(thesysvariable);

			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusIncExtListYes);
			System.out.println("sysvariableid IncludeExtListcolumnoninvoices==>"+InventoryConstant.getConstantSysvariableId("IncludeExtListcolumnoninvoices"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncludeExtListcolumnoninvoices"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusIncMultYes);
			System.out.println("sysvariableid IncludeMultcolumnoninvoices==>"+InventoryConstant.getConstantSysvariableId("IncludeMultcolumnoninvoices"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncludeMultcolumnoninvoices"));
			asysvariable.add(thesysvariable);
			
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusUseDivAddYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("UseDivisionaddressinPickTickets"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("UseDivisionaddressinPickTickets"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusAllblCusInvYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("AllowblanklineitemsinSalesOrderInsideOutsideofJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowblanklineitemsinSalesOrderInsideOutsideofJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusAllblProdinSalOrdYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("AllowblanklineitemsinCustomerInvoice"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowblanklineitemsinCustomerInvoice"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqProinSalOrderYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("AllowblankProductItemCodeinSalesOrderInsideOutsideofJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowblankProductItemCodeinSalesOrderInsideOutsideofJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqProinSalOrderYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("RequirePromiseDateinSalesOrderoutsideofJob"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequirePromiseDateinSalesOrderoutsideofJob"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqfreinCuInvoicesYes);
			System.out.println("sysvariableid RequireFreightwhencalculatingTaxonCustomerInvoices==>"+InventoryConstant.getConstantSysvariableId("RequireFreightwhencalculatingTaxonCustomerInvoices"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireFreightwhencalculatingTaxonCustomerInvoices"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusReqSeqNumCuInvoicesYes);
			thesysvariable.setValueString(inp_cusNewSeqNumCuInvoices);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RequireNewNumbersForCuInvoices"));
			asysvariable.add(thesysvariable);
			
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusRemExtListfmSalOrdpdfYes);
			System.out.println("sysvariableid RemoveEXTLISTcolumnfromSalesOrderPDF==>"+InventoryConstant.getConstantSysvariableId("RemoveEXTLISTcolumnfromSalesOrderPDF"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RemoveEXTLISTcolumnfromSalesOrderPDF"));
			asysvariable.add(thesysvariable);
			
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_cusRemMultfmSalOrdpdfYes);
			System.out.println("sysvariableid StatementsshallshowBillingRemainder==>"+InventoryConstant.getConstantSysvariableId("RemoveMULTcolumnfromSalesOrderPDF"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("RemoveMULTcolumnfromSalesOrderPDF"));
			asysvariable.add(thesysvariable);
			
			boolean	insertintosysvariable = itsInventoryService.saveInventorysettings(asysvariable);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveCustomerSettingsSysVariable","Inventory",e,session,therequest);
		}
		return asysvariable;

	}
	
	@RequestMapping(value = "/saveCommissionSettings", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> saveCommissionSettings(
			@RequestParam(value = "txt_adjdescription1",required=false)String txt_adjdescription1,
			@RequestParam(value = "txt_adjdescription2",required=false)String txt_adjdescription2,
			@RequestParam(value = "txt_adjdescription3",required=false)String txt_adjdescription3,
			@RequestParam(value = "txt_adjdescription4",required=false)String txt_adjdescription4,
			@RequestParam(value = "txt_commissiondesc1",required=false)String txt_commissiondesc1,
			@RequestParam(value = "txt_commissiondesc2",required=false)String txt_commissiondesc2,
			@RequestParam(value = "txt_commissiondesc3",required=false)String txt_commissiondesc3,
			@RequestParam(value = "txt_commissiondesc4",required=false)String txt_commissiondesc4,
			@RequestParam(value = "chk_invoiceCombineYes",required=false) Integer chk_invoiceCombineYes,
			@RequestParam(value = "chk_viewCommissionYes",required=false) Integer chk_viewCommissionYes,
			@RequestParam(value = "chk_allocatedProfitYes",required=false) Integer chk_allocatedProfitYes,
			@RequestParam(value = "chk_applyCreditYes",required=false) Integer chk_applyCreditYes,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		  ArrayList<Sysvariable> asysvariable = new  ArrayList<Sysvariable>();
		try {
//			txt_adjdescription1 txt_adjdescription2 txt_adjdescription3 txt_adjdescription4  txt_commissiondesc1 txt_commissiondesc2 txt_commissiondesc3 txt_commissiondesc4

			//Textbox 1row
			Sysvariable thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_adjdescription1);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("AdjustmentDescription1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AdjustmentDescription1"));
			asysvariable.add(thesysvariable);
			
			//Textbox 2row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_adjdescription2);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("AdjustmentDescription2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AdjustmentDescription2"));
			asysvariable.add(thesysvariable);
			
			//Textbox 3row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_adjdescription3);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("AdjustmentDescription3"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AdjustmentDescription3"));
			asysvariable.add(thesysvariable);

			//Textbox 4row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_adjdescription4);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("AdjustmentDescription4"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AdjustmentDescription4"));
			asysvariable.add(thesysvariable);
			
			//Textbox 5row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_commissiondesc1);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("CommissionDescription1"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CommissionDescription1"));
			asysvariable.add(thesysvariable);
			
			//Textbox 6row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_commissiondesc2);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("CommissionDescription2"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CommissionDescription2"));
			asysvariable.add(thesysvariable);
			
			//Textbox 7row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_commissiondesc3);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("CommissionDescription3"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CommissionDescription3"));
			asysvariable.add(thesysvariable);
			
			
			//Textbox 8row
			 thesysvariable=new Sysvariable();
			thesysvariable.setValueString(txt_commissiondesc4);
			System.out.println("sysvariableid==>"+InventoryConstant.getConstantSysvariableId("CommissionDescription4"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("CommissionDescription4"));
			asysvariable.add(thesysvariable);
			
//			chk_invoiceCombineYes  chk_viewCommissionYes chk_allocatedProfitYes chk_applyCreditYes
			
			//9row chk_invWarhouseaddressYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_viewCommissionYes);
			System.out.println("sysvariableid AllowEmployeestoviewtheirownCommissionStatements==>"+InventoryConstant.getConstantSysvariableId("AllowEmployeestoviewtheirownCommissionStatements"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AllowEmployeestoviewtheirownCommissionStatements"));
			asysvariable.add(thesysvariable);
			
			//10row chk_invWarhouseaddressYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_invoiceCombineYes);
			System.out.println("sysvariableid InvoicesCombinedforJobs==>"+InventoryConstant.getConstantSysvariableId("InvoicesCombinedforJobs"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("InvoicesCombinedforJobs"));
			asysvariable.add(thesysvariable);
			
			//11row chk_invWarhouseaddressYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_allocatedProfitYes);
			System.out.println("sysvariableid Onlyshowallocatedprofitonsplitcommissions==>"+InventoryConstant.getConstantSysvariableId("Onlyshowallocatedprofitonsplitcommissions"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("Onlyshowallocatedprofitonsplitcommissions"));
			asysvariable.add(thesysvariable);
			
			//12row chk_invWarhouseaddressYes
			thesysvariable=new Sysvariable();
			thesysvariable.setValueLong(chk_applyCreditYes);
			System.out.println("sysvariableid ApplyCreditswhenUsed==>"+InventoryConstant.getConstantSysvariableId("ApplyCreditswhenUsed"));
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ApplyCreditswhenUsed"));
			asysvariable.add(thesysvariable);
			
			boolean	insertintosysvariable = itsInventoryService.saveInventorysettings(asysvariable);
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveCommissionSettings","Inventory",e,session,therequest);
		}
		return asysvariable;

	}
	
	@RequestMapping(value = "/receiveInventoryTransaction", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getReceiveInventoryTransaction(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "warehouseListID", required = false) int warehouseListID,
			@RequestParam(value = "fromDateID", required = false) String fromDateID,
			@RequestParam(value = "toDateID", required = false) String toDateID,
			@RequestParam(value = "prMasterID", required = false) int prMasterID,
			HttpServletResponse response, HttpSession session,HttpServletRequest therequest)
			throws IOException, MessagingException {
		itsLogger.debug("Inventory Transaction List");
		CustomResponse aResponse = null;
		try {
			System.out.println("date "+fromDateID+toDateID);
			
			 String fromdate = "";
			 String todate = "";
			 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			 
			if(fromDateID!=null && !fromDateID.trim().equals("")){
				
			    Date convertedCurrentDate = sdf.parse(fromDateID);
			    fromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDateID!=null && !toDateID.trim().equals("")){
			    Date convertedCurrentDate = sdf.parse(toDateID);
			    todate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+todate);
			}
			
		//	int aTotalCount = itsInventoryService.getRecordsCountforReceiveInvenory(warehouseListID,fromDateID,toDateID,prMasterID);
	/*		int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;*/
			//List<?> aPOs = itsPurchaseService.filterPOsList(aFrom, aTo,vendorId, sortBy, rangeFrom);
			List<?> aPOsList = itsInventoryService.filterInventoryTransactionListSize(fromdate,todate,warehouseListID,prMasterID);
			int aTotalCount=aPOsList.size();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			List<?> aPOs = itsInventoryService.filterInventoryTransactionList(fromdate,todate,warehouseListID,prMasterID,theSidx,theSord,-1,-1);
			// List<?> aPOs = itsPurchaseService.getAllPOsList(aTotalCount-100,
			// aTotalCount);
			
			aTo = (theRows * thePage);
			if (aTo > aTotalCount)
				aTo = aTotalCount;
			aResponse = new CustomResponse();
			aResponse.setRows(aPOs);
			aResponse.setRecords(String.valueOf(aPOs.size()));
			//aResponse.setPage(thePage);
			//aResponse.setTotal((int) Math.ceil((double) aTotalCount	/ (double) theRows));
		}catch (Exception e) {
			itsLogger.error(e.getCause().getMessage(), e);
			response.sendError(500, e.getCause().getMessage());
			sendTransactionException("<b> prMasterID,warehouseListID==></b>"+prMasterID+","+warehouseListID,"Inventory",e,session,therequest);
			
		}

		return aResponse;
	}
	
	@RequestMapping(value = "/getInventoryTransReportPdf", method = RequestMethod.GET)
	public @ResponseBody void getInventoryTransReportPdf( 
			@RequestParam(value="prMasterID", required= false) Integer prMasterID,
			@RequestParam(value="warehouseListID", required= false) Integer warehouseListID, 
			@RequestParam(value="fromDateID", required= false) String fromDateID,
			@RequestParam(value="toDateID", required= false) String toDateID,
			@RequestParam(value="category", required= false) String category,
			HttpSession session, HttpServletResponse theResponse, HttpServletRequest therequest) throws IOException, DocumentException, ParseException, InventoryException, JRException, VendorException, JobException, MessagingException, SQLException{
	
		System.out.println("prMasterID "+prMasterID+warehouseListID+fromDateID+toDateID);
		Connection connection = null;
		ConnectionProvider con =null;
			try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =therequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/InventoryTransactions.jrxml");
			String filename=null;
			String warehouse=null;
		
			con = itspdfService.connectionForJasper();
			
			 String fromdate = "";
			 String todate = "";
			 SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
            if(fromDateID!=null && !fromDateID.trim().equals("")){
				
			    Date convertedCurrentDate = sdf.parse(fromDateID);
			    fromdate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated Date:"+fromdate);
			}
			if(toDateID!=null && !toDateID.trim().equals("")){
			    Date convertedCurrentDate = sdf.parse(toDateID);
			    todate=sdff.format(convertedCurrentDate );
			    System.out.println("Formated End Date:"+todate);
			}
			
			String DATE_FORMAT_NOW = "MM/dd/yy h:mm a";
		    Date date = new Date();
		    SimpleDateFormat sdfs = new SimpleDateFormat(DATE_FORMAT_NOW);
		    String stringDate = sdfs.format(date );
		    System.out.println("stringDate "+stringDate);
		    
		    filename="InvTrans_"+stringDate+".pdf";
		    
		    Prmaster aPrmaster =  itsInventoryService.getSingleProductDetails(prMasterID);
			Prwarehouseinventory aPrwarehouseinventory = itsInventoryService.getSingleWareDetails(prMasterID);
			
			if(warehouseListID==0)
				warehouse="All";
			else{
			List<Prwarehouse> prwarehouselist=jobService.getWareHouse();
			for (Prwarehouse temp : prwarehouselist) {
				if(temp.getPrWarehouseId()==warehouseListID)
					warehouse=temp.getSearchName();
			}}
			System.out.println(warehouse);
			
			params.put("fromdate",fromDateID);
			params.put("todate",toDateID);
			params.put("warehouse",warehouse);
			params.put("itemcode",aPrmaster.getItemCode());
			params.put("description",aPrmaster.getDescription());
			params.put("currentcost",aPrmaster.getAverageCost());
			params.put("primaryvendor",aPrmaster.getVendorName());
			params.put("category",category);
			params.put("onhand",aPrmaster.getInventoryOnHand());
			params.put("allocated",aPrmaster.getInventoryAllocated());
			params.put("onorder",aPrmaster.getInventoryOnOrder());
			
			List<PurchaseOrdersBean> aQueryList = itsInventoryService.filterInventoryTransactionList(fromdate, todate, warehouseListID, prMasterID, "TranDate","asc",-1,-1);
			
			connection = con.getConnection();
			
			System.out.println("aQueryList "+aQueryList);
			
			JRBeanCollectionDataSource beanColDataSource = new
					JRBeanCollectionDataSource(aQueryList);
			
			JasperReport jasperReport = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params, beanColDataSource);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ReportService.export("pdf", jasperPrint,theResponse,  baos,filename);
			ReportService.write(theResponse, baos);
			//connection.close();
			
		} catch (SQLException e) {
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> warehouseListID, prMasterID==></b>"+warehouseListID+","+ prMasterID,"Inventory",e,session,therequest);
		}
			finally{
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			}
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
			itsUserService.createTpUsage(aTpusage);
		}
		
		}catch(Exception ex){
			e.printStackTrace();
		}finally{
			aUserBean=null;
			objtsusersettings=null;
		}
	}
	
	
	@RequestMapping(value = "/getInventoryDetails", method = RequestMethod.POST)
	public @ResponseBody  Prmaster getInventoryDetails(
			@RequestParam(value = "prMasterID",required=false) Integer theprMasterID,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		  Prmaster aPrmaster =null;
		try {
			aPrmaster = itsInventoryService.getSingleProductDetails(theprMasterID);
			BigDecimal prWarehouseCost= itsInventoryService.getWarehouseCost(theprMasterID);
			aPrmaster.setAverageCost_New(prWarehouseCost);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveCustomerSettingsSysVariable","Inventory",e,session,therequest);
		}
		return aPrmaster;

	}
	
	
	
	@RequestMapping(value = "/saveVendorSettings", method = RequestMethod.POST)
	public @ResponseBody  ArrayList<Sysvariable> saveVendorSettings(
			@RequestParam(value = "chk_venpoDesStatusYN",required=false) Integer chk_venpoDesStatusYN,
			@RequestParam(value = "chk_vencmpanylogoStatusYN",required=false) Integer chk_vencmpanylogoStatusYN,
			@RequestParam(value = "chk_vendorPhnoYesYN",required=false) Integer chk_vendorPhnoYesYN,
			@RequestParam(value = "chk_venproductidStatusYN",required=false) Integer chk_venproductidStatusYN,
			@RequestParam(value = "chk_venclosePoStatusYN",required=false) Integer chk_venclosePoStatusYN,
			@RequestParam(value = "chk_venreqInvStatusYN",required=false) Integer chk_venreqInvStatusYN,
			@RequestParam(value = "chk_vendefaultInvStatusYN",required=false) Integer chk_vendefaultInvStatusYN,
			@RequestParam(value = "chk_IncTaxPOInvStatusYN",required=false) Integer chk_IncTaxPOInvStatusYN,
			HttpSession session,
			HttpServletRequest therequest) throws IOException, MessagingException {
		  ArrayList<Sysvariable> asysvariable = new  ArrayList<Sysvariable>();
		  
			
		
		try {
			Sysvariable thesysvariable=new Sysvariable();
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_venpoDesStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("DefaultPODescItemCode"));
			asysvariable.add(thesysvariable);


			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_vencmpanylogoStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("UseCompanyLogoAndLbl"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_vendorPhnoYesYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShwVndrPNonPO"));
			asysvariable.add(thesysvariable);

			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_venproductidStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ShwVndrPIDonPO"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_venclosePoStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("AskClPOwnItemsRecieved"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_venreqInvStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("ReqInvWithPO"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_vendefaultInvStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("VndrInvDfltOnHold"));
			asysvariable.add(thesysvariable);
			
			thesysvariable = new Sysvariable();
			thesysvariable.setValueLong(chk_IncTaxPOInvStatusYN);
			thesysvariable.setSysVariableId(InventoryConstant.getConstantSysvariableId("IncTaxOnPOAndInvoices"));
			asysvariable.add(thesysvariable);
			
			
			boolean	insertintosysvariable = itsInventoryService.saveInventorysettings(asysvariable);
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> MethodName==></b>saveVendorSettings","Inventory",e,session,therequest);
		}
		return asysvariable;

	}
	
}
