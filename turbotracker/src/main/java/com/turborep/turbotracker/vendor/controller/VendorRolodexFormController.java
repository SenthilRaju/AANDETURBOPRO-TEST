package com.turborep.turbotracker.vendor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.VeFactory;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.dao.Vepodetail;
import com.turborep.turbotracker.vendor.dao.Vereceive;
import com.turborep.turbotracker.vendor.dao.Vereceivedetail;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

@Controller
@RequestMapping("/rolodexforms")
public class VendorRolodexFormController {

	Logger itsLogger = Logger.getLogger("controller");

	private ArrayList<Vemaster> itsVeMasterRecord;

	@Resource(name = "vendorService")
	private VendorServiceInterface itsVendorService;
	
	@Resource(name="userLoginService")
	private UserService itsUserService;

	@RequestMapping(value = "vendorform", method = RequestMethod.GET)
	public String getVendorForm(ModelMap theModel, HttpSession theSession) {
		 String aRxMasterId = (String) theSession.getAttribute("rxMasterId");
		try {
			theModel.addAttribute("current",itsVendorService.getVendorBillDetails("current",aRxMasterId));
			theModel.addAttribute("Days30",itsVendorService.getVendorBillDetails("30",aRxMasterId));
			theModel.addAttribute("Days60",itsVendorService.getVendorBillDetails("60",aRxMasterId));
			theModel.addAttribute("Days90",itsVendorService.getVendorBillDetails("90",aRxMasterId));
		} catch (VendorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			setaVeMasterRecord(aRxMasterId);
			if (!getaVeMasterRecord().isEmpty())
				theModel.addAttribute("veMasterRecord", getaVeMasterRecord().get(0));
			return "vendor";
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			return "Error";
		}
	}

	@RequestMapping(value = "manufacturers", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getVendorManufacturers(
			@RequestParam(value = "rxMasterId", required = true) String theRxMasterId) {
		List<?> aBids = null;
		try {
			aBids = itsVendorService.getManufacturers(theRxMasterId);
		} catch (VendorException e) {
			itsLogger.error(e.getMessage(), e);
		}
		CustomResponse aResponse = new CustomResponse();
		aResponse.setRows(aBids);
		return aResponse;
	}

	@RequestMapping(value = "updateManufacturers", method = RequestMethod.POST)
	public @ResponseBody Boolean updateManufacturers(
			@RequestParam(value = "description", required = true) String theManufacture,
			@RequestParam(value = "inActive", required = true) boolean theActive,
			@RequestParam(value = "veFactoryID", required = true) Integer theFactoryID,
			@RequestParam(value = "rxMasterID", required = true) Integer therxMasterID,
			@RequestParam(value = "oper", required = true) String oper
			) {
		VeFactory aVeFactory = new VeFactory();
		try {
			aVeFactory.setDescription(theManufacture);
			aVeFactory.setInActive(theActive);
			aVeFactory.setVeFactoryID(theFactoryID);
			aVeFactory.setRxMasterId(therxMasterID);
			if(oper.equals("add")){
				aVeFactory = itsVendorService.updateManufactures(aVeFactory);
			}else{
				Integer vefactoryid = itsVendorService.createManufactures(aVeFactory);
				aVeFactory.setVeFactoryID(vefactoryid);
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return true;
	}

	@RequestMapping(value = "updateReceiveInventory", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateReceiveInventory(
			@RequestParam(value = "vePoDetailId", required = true) Integer thevePoDEtailId,
			@RequestParam(value = "prMasterId", required = true) Integer theprMasterId,
			@RequestParam(value = "QuantityReceived", required = true) BigDecimal theQuantityReceived,
			@RequestParam(value = "difference", required = false) BigDecimal thePreviousReceived, 
			@RequestParam(value = "veReceiveID", required = true) Integer veReceiveID,HttpServletRequest request)
			{
		Vepodetail aVeFactory = new Vepodetail();
		int aFactory = 0;
		try {
			aVeFactory.setVePodetailId(thevePoDEtailId);
			aVeFactory.setPrMasterId(theprMasterId);
			// BigDecimal Qreceived = (BigDecimal)(theQuantityReceived);
//			if(thePreviousReceived.compareTo(BigDecimal.ZERO)==0){
			aVeFactory.setQuantityReceived(theQuantityReceived);
//			}else{
//				aVeFactory.setQuantityReceived(thePreviousReceived);
//			}
			itsLogger.info("1::" + thevePoDEtailId + " 2::" + theprMasterId
					+ " 3::" + aVeFactory.getQuantityReceived()+" :4: "+thePreviousReceived);
			//aFactory = itsVendorService.vepoDetailReceiveInventory(aVeFactory,thePreviousReceived);
			aFactory = itsVendorService.vepoDetailReceiveInventory(aVeFactory,theQuantityReceived,veReceiveID,new Date());
			
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return aFactory;
	}
	
	@RequestMapping(value = "updateReceiveAllInventory", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateReceiveAllInventory(@RequestParam(value = "gridData", required = false) String gridData,
			@RequestParam(value = "veReceiveID", required = true) Integer veReceiveID,
			@RequestParam(value = "recDate", required = false) Date theReceivedDate,
			@RequestParam(value = "vePOID", required = false) Integer vePoid,
			HttpSession session) throws VendorException {
		
		JsonParser parser = new JsonParser();
		Vepo vepo=new Vepo();
		Integer aFactory = 0;
		System.out.println("gridData "+gridData);
		
		
		if ( gridData!=null) {
			JsonElement ele = parser.parse(gridData);
			JsonArray array = ele.getAsJsonArray();
			System.out.println("array length==>"+array.size());
			for (JsonElement ele1 : array) {
				JsonObject obj = ele1.getAsJsonObject();
				
				Vepodetail aVeFactory = new Vepodetail();
				
				
				aVeFactory.setVePodetailId(obj.get("vePodetailId").getAsInt());
				aVeFactory.setPrMasterId(obj.get("prMasterId").getAsInt());
				aVeFactory.setQuantityReceived(JobUtil.ConvertintoBigDecimal(obj.get("quantityReceived").getAsString()));
				
				itsVendorService.vepoDetailReceiveInventory(aVeFactory,JobUtil.ConvertintoBigDecimal(obj.get("quantityReceived").getAsString()),veReceiveID,theReceivedDate);
				
			}
			}
		vepo.setVePoid(vePoid);
		vepo.setreceiveddate(theReceivedDate);
		aFactory = itsVendorService.vepoReceiveInventoryDate(vepo,veReceiveID,((UserBean)session.getAttribute(SessionConstants.USER)).getUserId(),((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());

		return aFactory;
	}

	public ArrayList<Vemaster> getaVeMasterRecord() {
		return itsVeMasterRecord;
	}

	public void setaVeMasterRecord(String rxMasterId) throws VendorException {
		this.itsVeMasterRecord = itsVendorService.getVendorMaster(rxMasterId);
	}
	@RequestMapping(value = "updateReceiveInventoryDate", method = RequestMethod.POST)
	public @ResponseBody
	Integer updateReceiveInventoryDate(
			@RequestParam(value = "receivedDate", required = true) Date theReceivedDate,
			@RequestParam(value = "veReceiveID", required = true) Integer veReceiveID,
			@RequestParam(value = "vePOId", required = true) Integer thevePoID) {
		Vepo aVeFactory = new Vepo();
		Integer aFactory = 0;
		try {
			aVeFactory.setreceiveddate(theReceivedDate);
			aVeFactory.setVePoid(thevePoID);
			//aFactory = itsVendorService.vepoReceiveInventoryDate(aVeFactory,veReceiveID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		//vePOId
		return aFactory;
	}
	
	@RequestMapping(value = "getAllpriorreceivedinventory", method = RequestMethod.POST)
	public @ResponseBody
	List<Vereceive> updateReceiveInventoryDate(@RequestParam(value = "vePOId", required = true) Integer thevePoID) {
		List<Vereceive> aFactory = null;
		try {
			aFactory = (List<Vereceive>) itsVendorService.getveReceiveDetails( thevePoID); 
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return aFactory;
	}
	
	
	@RequestMapping(value = "UpdateVendorFinancialTab", method = RequestMethod.GET)
	public @ResponseBody Boolean UpdateVendorFinancialTab(
			@RequestParam(value = "veMasterID", required = true) Integer veMasterID,
			@RequestParam(value = "DueDays", required = true) int DueDays,
			@RequestParam(value = "DueOnDay", required = true) Integer DueOnDay,
			@RequestParam(value = "DiscountPercent", required = true) BigDecimal DiscountPercent,
			@RequestParam(value = "DiscountDays", required = true) Integer DiscountDays,
			@RequestParam(value = "DiscOnDay", required = true) Integer DiscOnDay,
			@RequestParam(value = "DiscountIncludesFreight", required = true) Boolean DiscountIncludesFreight,
			@RequestParam(value = "Manufacturer", required = true) String Manufacturer,
			@RequestParam(value = "coExpenseAccountID", required = true) Integer coExpenseAccountID,
			@RequestParam(value = "ImportType", required = true) Integer ImportType,
			@RequestParam(value = "AccountNumber", required = true) String AccountNumber
			) {
		Vemaster aVemaster=new Vemaster();
		try {
			aVemaster.setVeMasterId(veMasterID);
			aVemaster.setDueDays(DueDays);
			boolean selectDueOnDay=false;
			if(DueOnDay==1){
				selectDueOnDay=true;
			}
			aVemaster.setDueOnDay(selectDueOnDay);
			aVemaster.setDiscountPercent(DiscountPercent);
			aVemaster.setDiscountDays(DiscountDays);
			boolean selectDiscOnDay=false;
			if(DiscOnDay==1){
				selectDiscOnDay=true;
			}
			aVemaster.setDiscOnDay(selectDiscOnDay);
			aVemaster.setDiscountIncludesFreight(DiscountIncludesFreight);
			aVemaster.setManufacturer(Manufacturer);
			aVemaster.setCoExpenseAccountId(coExpenseAccountID);
			aVemaster.setImportType(ImportType);
			aVemaster.setAccountNumber(AccountNumber);
			
		Boolean	returnValue = itsVendorService.UpdateVendorFinancialTab(aVemaster);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		//vePOId
		return true;
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

}
