/**
addVendorInvoiceFromPO * Copyright (c) 2014 A & E Specialties, Inc.  All rights reserved.
 * This software is the confidential and proprietary information of A & E Specialties, Inc.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with A & E Specialties, Inc.
 * 
 * @author vish_pepala
 */
package com.turborep.turbotracker.vendor.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.banking.dao.GlRollback;
import com.turborep.turbotracker.banking.dao.Momultipleaccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.dao.VeBillPaymentHistory;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Coledgersource;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.Cusodetail;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.finance.exception.FinancePostingException;
import com.turborep.turbotracker.finance.service.FinancePostingService;
import com.turborep.turbotracker.job.dao.JoRelease;
import com.turborep.turbotracker.job.dao.JoReleaseDetail;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vebill;
import com.turborep.turbotracker.vendor.dao.Vebilldetail;
import com.turborep.turbotracker.vendor.dao.Vebilldistribution;
import com.turborep.turbotracker.vendor.dao.Vebillpay;
import com.turborep.turbotracker.vendor.dao.Vemaster;
import com.turborep.turbotracker.vendor.dao.VendorBillsBean;
import com.turborep.turbotracker.vendor.dao.Vepo;
import com.turborep.turbotracker.vendor.exception.VendorException;
import com.turborep.turbotracker.vendor.service.VendorServiceInterface;

/**
* Vendor Invoice Controller
* 
*<pre>
* Maintenance History:
* 
* Date				Release		Author
* ----				-------		------
* 18 Mar 2014			2.0.1		Vish Pepala
* 
* Copyright 2014 - A&E Specialties, Inc.
* </pre>
* 
* @author Vish Pepala
* @version 1.0
**/
@Controller
@RequestMapping("/veInvoiceBillController")
public class VendorInvoiceBillController {

	Logger logger = Logger.getLogger(VendorInvoiceBillController.class);
	
	@Resource(name="vendorService")
	private VendorServiceInterface vendorService;
	
	@Resource(name = "productService")
	private ProductService itsProductService;
	
	@Resource(name="financePostingService")
	private FinancePostingService itsFinancePostingService;
	
	@Resource(name="gltransactionService")
	private GltransactionService itsGltransactionService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@Resource(name = "companyService")
	private CompanyService companyService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeService;
	
	@Resource(name = "jobService")
	private JobService itsJobService;
	
	@InitBinder
	private void dateBinder(WebDataBinder binder) {
	            //The date format to parse or output your dates
	    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	            //Create a new CustomDateEditor
	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
	            //Register it as custom editor for the Date type
	    binder.registerCustomEditor(Date.class, editor);
	}
	
	@RequestMapping(value = "/getBillLineitemsList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getVeBillLineItemsList(@RequestParam(value="veBillId", required=false) Integer veBillId,
													@RequestParam(value="vePoId", required=false) Integer vePoId,
													@RequestParam(value="aJoReleaseDetailsID", required=false) Integer aJoReleaseDetailsID,
													@RequestParam(value="invoiceType", required=false) String vendorInvoiceType,
													HttpSession theSession, ModelMap theModel, HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException{
		CustomResponse aResponse = new CustomResponse();
		try{
			/*if(!vendorInvoiceType.equals("existing")){
				int isInserted = vendorService.insertLineItemsFromPODetailtoBillDetail(vePoId, veBillId);
			}*/
			List<?> aVeBillLineItemsList = vendorService.getVeBillLineItems(veBillId);
			aResponse.setRows(aVeBillLineItemsList);
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( veBillId+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value = "/manipulateBillLineitems", method = RequestMethod.POST)
	public @ResponseBody String manipulateBillLineitems(@RequestParam(value="description", required= false) String description,
										@RequestParam(value="prItemCode", required= false) String prItemCode,
										@RequestParam(value="prMasterId", required= false) Integer prMasterId,
										@RequestParam(value="priceMultiplier", required= false) BigDecimal priceMultiplier,
										@RequestParam(value="quantityOrdered", required= false) BigDecimal quantityOrdered,
										@RequestParam(value="unitCost", required= false) BigDecimal unitCost,
										@RequestParam(value="veBillId", required= false) Integer veBillId,
										@RequestParam(value="veBillDetailId", required= false) Integer veBillDetailId,
										@RequestParam(value="vePODetailId", required= false) Integer vePODetailId,
										@RequestParam(value="oper", required= false) String oper,
										HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		Vebilldetail aVeBillDetail = new Vebilldetail();
		try {
			
			System.out.println("manipulateBillLineitems :: Oper ::"+oper);
			
			aVeBillDetail.setDescription(description);
			aVeBillDetail.setPrMasterId(prMasterId);
			aVeBillDetail.setPriceMultiplier(priceMultiplier);
			aVeBillDetail.setQuantityBilled(quantityOrdered);
			aVeBillDetail.setUnitCost(unitCost);
			aVeBillDetail.setVeBillId(veBillId);
			aVeBillDetail.setVePodetailId(vePODetailId);
			if(oper.equals("add")){
				vendorService.saveVendorBillDetail(aVeBillDetail);
			} else if(oper.equals("edit")){
				aVeBillDetail.setVeBillDetailId(veBillDetailId);
				vendorService.updateVendorBillDetail(aVeBillDetail);
			} else if(oper.equals("del")){
				aVeBillDetail.setVeBillDetailId(veBillDetailId);
				vendorService.deleteVendorBillDetail(aVeBillDetail);
			}
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( description+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return "Success";
	}
	
	
	
	@RequestMapping(value = "/getPoTotal", method = RequestMethod.POST)
	public @ResponseBody String getPoTotal(
			@RequestParam(value="vePoID", required= false) String vePoID,
			@RequestParam(value="invNo", required= false) String invNo,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException {
		logger.info("vePOID: "+vePoID);
		Vepo aVepo = new Vepo();
		boolean invStatus = false ;
		
		Integer vepoID;
		if(vePoID!=null){
			vepoID = Integer.parseInt(vePoID);
		}else{
			vepoID=0;
		}
		try {
				aVepo = vendorService.getVePo(vepoID);
				invStatus = vendorService.checkInvoiceNumberExist(invNo,vepoID);
				
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( vePoID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aVepo.getSubtotal()+"-*-"+invStatus;
	}
	
	@RequestMapping(value="/updateVendorInvoiceDetails", method = RequestMethod.POST)
	public @ResponseBody Integer updateVendorInvoiceDetails(@RequestParam(value="vendorDateName", required= false) String theReleaseDate,
													   @RequestParam(value = "vendorInvoiceNumName", required= false) String theVendorInvoiceName,
													   @RequestParam(value = "dueDateName", required= false) String theDueDate,
													   @RequestParam(value = "datedName", required= false) String datedDate,
													   @RequestParam(value = "postDateChkName", required=false) boolean thePostChk,
													   @RequestParam(value = "postDateName", required= false) String thePostDateName, 
													   @RequestParam(value = "shipDateName", required= false) String theShipDate,
													   @RequestParam(value = "shipViaSelectName",required = false) Integer theShipViaID,
													   @RequestParam(value = "proNumberName",required = false) String thePRoNumber,
													   @RequestParam(value = "subtotal_Name",required = false) String theSubTotal,
													   @RequestParam(value = "freight_Name",required = false) BigDecimal theFrieght,
													   @RequestParam(value = "freight_ID",required = false) String freight_ID,
													   @RequestParam(value = "tax_Name",required = false) BigDecimal theTax,
													   @RequestParam(value = "total_Name",required = false) BigDecimal theTotal,
													   @RequestParam(value = "bal_Name",required = false) String theBalance,
													   @RequestParam(value = "veBill_Name",required = false) Integer theVeBillID,
													   @RequestParam(value = "oper",required = false) String theOper,
													   @RequestParam(value = "coAccountName",required = false) Integer theCoAccountID,
													   @RequestParam(value = "joReleaseID",required = false) Integer thejoReleaseID,
													   @RequestParam(value = "joReleaseDetailsID",required = false) Integer theJoReleaseDetailsID,
													   @RequestParam(value = "vePOID",required = false) Integer theVePOID,
													   @RequestParam(value = "rxMasterID",required = false) Integer theRxMasterID,
													   @RequestParam(value = "updatePO",required = false) String updatePO,
													   @RequestParam(value = "gridData", required = false) String gridData,
													   @RequestParam(value = "shippingCount", required = false) Integer shippingCount,
													   @RequestParam(value = "invReasonfmjob", required = false) String invReasonfmjob,
													   @RequestParam(value = "coFiscalPeriodId", required = false) Integer periodId,
													   @RequestParam(value = "coFiscalYearId", required = false) Integer yearId,
													   @RequestParam(value = "delData[]",required = false) ArrayList<String>  delData,
													   HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws ParseException,BankingException, IOException, CompanyException, MessagingException{
		
		Vebill aVebill = new Vebill();
		JoReleaseDetail aJoReleaseDetail = new JoReleaseDetail();
		
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);

		try{
			
			if(theReleaseDate != null && theReleaseDate != ""){
				aVebill.setReceiveDate(DateUtils.parseDate(theReleaseDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setInvoiceNumber(theVendorInvoiceName);
			if(theDueDate != null && theDueDate != ""){
				aVebill.setDueDate(DateUtils.parseDate(theDueDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setUsePostDate(thePostChk);
			System.out.println("thePostDateName="+thePostDateName+"thePostDateName");
			if(thePostDateName != null && thePostDateName != ""){
				aVebill.setPostDate(DateUtils.parseDate(thePostDateName, new String[]{"MM/dd/yyyy"}));
			}
			System.out.println(theShipDate);
			if(theShipDate != null && theShipDate != ""){
				aVebill.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
			}
			aVebill.setVeShipViaId(theShipViaID);
			aVebill.setTrackingNumber(thePRoNumber);
			aVebill.setFreightAmount(theFrieght);
			aVebill.setTaxAmount(theTax);
			//aVebill.setJoReleaseDetailId(theJoReleaseDetailsID);
			aVebill.setVeBillId(theVeBillID);
			aVebill.setApaccountId(theCoAccountID);
			aVebill.setRxMasterId(theRxMasterID);
			aVebill.setVePoid(theVePOID);
			aVebill.setTransactionStatus(1);
		
			BigDecimal billAmounttotal=new BigDecimal(0);
			if(theSubTotal != ""){
				billAmounttotal=new BigDecimal(theSubTotal).add(theFrieght).add(theTax);
			}
			aVebill.setBillAmount(billAmounttotal);
			if(theOper.equalsIgnoreCase("add")){ 
				aVebill.setAppliedAmount(new BigDecimal(0));
				aVebill.setBillDate(new Date());
				if(theShipDate!=null && theShipDate.length()>0){
				aJoReleaseDetail.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
				}
				aJoReleaseDetail.setJoReleaseId(thejoReleaseID);
				if(theReleaseDate!=null && theReleaseDate.length()>0){
				aJoReleaseDetail.setVendorInvoiceDate(DateUtils.parseDate(theReleaseDate, new String[]{"MM/dd/yyyy"}));
				}
				aJoReleaseDetail.setJoReleaseDetailId(theJoReleaseDetailsID);
				aJoReleaseDetail.setVendorInvoiceAmount(theTotal);
				
				logger.info("DatedDate=="+datedDate);
				if(datedDate!=null){
				aVebill.setBillDate(DateUtils.parseDate(datedDate, new String[]{"MM/dd/yyyy"}));
				}else{
					aVebill.setBillDate(new Date());
				}
				aVebill.setCreatedById(aUserBean.getUserId());
				aVebill.setCreatedOn(new Date());
				Vebill vebill =vendorService.addVendorInvoiceDetails(aVebill, aJoReleaseDetail,updatePO);
				
				/*if(delData!=null){
					for(String detailID:delData){
						Vebilldetail aVebilldetail=new Vebilldetail();
						System.out.println("delData[i]"+detailID);
						Integer vebillDetailID=JobUtil.ConvertintoInteger(detailID);
						aVebilldetail.setVeBillDetailId(vebillDetailID);
						aVebilldetail.setVeBillId(aVebill.getVeBillId());
						vendorService.saveVebillDetail(aVebilldetail,"delete");
					}
				}*/
				logger.info("JsonGridData"+gridData);
				JsonParser parser = new JsonParser();
				if (null != gridData ) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (int ki=0;ki<array.size()-1;ki++) {
						JsonElement ele1=array.get(ki);
						Vebilldetail thevebilldetail = new Vebilldetail();
						JsonObject obj = ele1.getAsJsonObject();
						//veBillID,vePODetailID,prMasterID,Description,Note,QuantityBilled,UnitCost,PriceMultiplier,FreightCost
						thevebilldetail.setVeBillId(vebill.getVeBillId());
						thevebilldetail.setVePodetailId(JobUtil.ConvertintoInteger(obj.get("vePodetailId").getAsString()));
						thevebilldetail.setPrMasterId(obj.get("prMasterId").getAsInt());
						thevebilldetail.setDescription(obj.get("description").getAsString());
						if(obj.get("note").getAsString()!=null)
						thevebilldetail.setNote(obj.get("note").getAsString());
						else
							thevebilldetail.setNote("");
						
				        System.out.println(obj.get("taxable"));
				        
				        if(obj.get("taxable").getAsString().equals("Yes"))
				        	thevebilldetail.setTaxable(true);
				        else
				        	thevebilldetail.setTaxable(false);
				        
						thevebilldetail.setQuantityBilled(obj.get("quantityOrdered").getAsBigDecimal());
						
						if(obj.get("unitCost")!=null){
						thevebilldetail.setUnitCost(new BigDecimal(obj.get("unitCost").getAsString().replaceAll("[$,]", "")));
						}else{
							thevebilldetail.setUnitCost(new BigDecimal(0));
						}
						String pricemultiplier="0";
						if(obj.get("priceMultiplier")!=null){
							pricemultiplier=obj.get("priceMultiplier").toString();
						}
							thevebilldetail.setPriceMultiplier(JobUtil.ConvertintoBigDecimal(pricemultiplier));
						
						thevebilldetail.setFreightCost(new BigDecimal(0.00));
						
						thevebilldetail.setUserId(((UserBean)theSession.getAttribute(SessionConstants.USER)).getUserId());
						thevebilldetail.setUserName(((UserBean)theSession.getAttribute(SessionConstants.USER)).getUserName());
						boolean returnvalue =vendorService.saveVeBillDetail(thevebilldetail);
						logger.info("returnvalue=="+returnvalue);
						
					}
					}
				//shippingCount
				vebill.setInsorouts("INS_JOB");
				vebill.setShippingrownum(shippingCount+1);
				itsGltransactionService.recieveVendorBill(null,vebill,  0,yearId,periodId,aUserBean.getFullName());
				
			}else if(theOper.equalsIgnoreCase("edit")){
				
				aVebill.setReason(invReasonfmjob);
				if(delData!=null){
					for(String detailID:delData){
						Vebilldetail aVebilldetail=new Vebilldetail();
						System.out.println("delData[i]"+detailID);
						Integer vebillDetailID=JobUtil.ConvertintoInteger(detailID);
						aVebilldetail.setVeBillDetailId(vebillDetailID);
						aVebilldetail.setVeBillId(aVebill.getVeBillId());
						vendorService.saveVebillDetail(aVebilldetail,"delete");
					}
				}
				Vebilldetail thevebilldetail = new Vebilldetail();
				JsonParser parser = new JsonParser();
				if (null != gridData) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (int ki=0;ki<array.size()-1;ki++) {
						JsonElement ele1=array.get(ki);
						thevebilldetail = new Vebilldetail();
						JsonObject obj = ele1.getAsJsonObject();
						//veBillID,vePODetailID,prMasterID,Description,Note,QuantityBilled,UnitCost,PriceMultiplier,FreightCost
						thevebilldetail.setVeBillId(theVeBillID);
				
						thevebilldetail.setVePodetailId(JobUtil.ConvertintoInteger(obj.get("vePodetailId").getAsString()));
						thevebilldetail.setPrMasterId(obj.get("prMasterId").getAsInt());
						thevebilldetail.setDescription(obj.get("description").getAsString());
						thevebilldetail.setVeBillDetailId(JobUtil.ConvertintoInteger(obj.get("veBillDetailId").getAsString()));
						
						
						if(obj.get("note")!=null)
						thevebilldetail.setNote(obj.get("note").getAsString());
						else
						thevebilldetail.setNote("");
						
						thevebilldetail.setQuantityBilled(obj.get("quantityOrdered").getAsBigDecimal());
						/*if(obj.get("quantityBilled")!=null){
						String billedqty=obj.get("quantityBilled").getAsString().replace("$", "");
						thevebilldetail.setQuantityBilled(new BigDecimal(billedqty));
						}else{
							thevebilldetail.setQuantityBilled(new BigDecimal(0));
						}*/
				       if(obj.get("taxable").getAsString().equals("Yes"))
				        	thevebilldetail.setTaxable(true);
				        else
				        	thevebilldetail.setTaxable(false);
						
						if(obj.get("unitCost")!=null){
						thevebilldetail.setUnitCost(new BigDecimal(obj.get("unitCost").getAsString().replaceAll("[$,]", "")));
						}else{
							thevebilldetail.setUnitCost(new BigDecimal(0));
						}
						thevebilldetail.setPriceMultiplier(obj.get("priceMultiplier").getAsBigDecimal());
						thevebilldetail.setFreightCost(new BigDecimal(0.00));
						boolean returnvalue =false;
						if(thevebilldetail.getVeBillDetailId()==0){
							returnvalue =vendorService.saveVeBillDetail(thevebilldetail);
						}else{
							vendorService.updateVendorBillDetail(thevebilldetail);
						}
						
						logger.info("returnvalue=="+returnvalue);
						
					}
					
				}
				
				aJoReleaseDetail.setJoReleaseDetailId(theJoReleaseDetailsID);
				aJoReleaseDetail.setShipDate(DateUtils.parseDate(theShipDate, new String[]{"MM/dd/yyyy"}));
				aJoReleaseDetail.setVendorInvoiceDate(DateUtils.parseDate(theReleaseDate, new String[]{"MM/dd/yyyy"}));
				if(theSubTotal != ""){
					aJoReleaseDetail.setVendorInvoiceAmount(new BigDecimal(theSubTotal));
				}
				logger.info("DatedDate=="+datedDate);
				if(datedDate!=null){
				aVebill.setBillDate(DateUtils.parseDate(datedDate, new String[]{"MM/dd/yyyy"}));
				}else{
					aVebill.setBillDate(new Date());
				}
				aVebill.setChangedById(aUserBean.getUserId());
				aVebill.setChangedOn(new Date());
				Vebill updateVebill =vendorService.updateVendorInvoiceDetails(aVebill, aJoReleaseDetail,thevebilldetail,updatePO);
				
				if(!invReasonfmjob.equals(""))
				{
				Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("VB");
				
				GlRollback glRollback = new GlRollback();
				glRollback.setVeBillID(updateVebill.getVeBillId());
				glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
				glRollback.setPeriodID(periodId);
				glRollback.setYearID(yearId);
				glRollback.setTransactionDate(updateVebill.getBillDate());
				itsGltransactionService.rollBackGlTransaction(glRollback);
				
				updateVebill.setInsorouts("INS_JOBEdit");
				updateVebill.setShippingrownum(shippingCount);
				itsGltransactionService.recieveVendorBill(null,updateVebill,  0,yearId,periodId,aUserBean.getFullName());
				}
			}
		}catch (VendorException e) {
			logger.error(e.getMessage());
			
			sendTransactionException( theReleaseDate+"","VendorInvoiceBillController",e,theSession,theRequest);
			
		}
		return 0;
	}
	@RequestMapping(value = "/getvebillpaylist",method = RequestMethod.POST)
	public @ResponseBody List<Vebillpay> getVeBillPayList(@RequestParam(value="moAccountID", required=false) Integer moAccountID,HttpSession theSession, ModelMap theModel,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException{
		List<Vebillpay> aPaidList = null;
		try {
			UserBean aUserBean;
			aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
			aPaidList = vendorService.getVeBillPayList(moAccountID,aUserBean.getUserId());
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( moAccountID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aPaidList;
	}
	
	@RequestMapping(value = "/getVeBillList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getVeBillList(@RequestParam(value="page", required=false) Integer thePage,
														@RequestParam(value="rows", required=false) Integer theRows,
														@RequestParam(value="sidx", required=false) String theSidx,
														@RequestParam(value="sord", required=false) String theSord,
														@RequestParam(value = "searchData", required = false) String searchData,
														@RequestParam(value = "fromDate", required = false) String fromDate,
														@RequestParam(value = "toDate", required = false) String toDate,
														ModelMap theModel,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException{
		CustomResponse aResponse = null;
		List<VendorBillsBean>  aInvoiceList = null;
		try {
			//int aTotalCount = vendorService.getBillsCount().intValue();
			
			String query=getveBillListQuery(searchData,fromDate,toDate);
		
			int aTotalCount=companyService.getCount(query);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			//aInvoiceList = vendorService.getVeBillList(aFrom, aTo);
			aInvoiceList = vendorService.getVeBillList(aFrom, aTo,searchData,fromDate,toDate,theSidx,theSord);
			aResponse = new CustomResponse();
			aResponse.setRows(aInvoiceList);
			aResponse.setRecords( String.valueOf(aInvoiceList.size()) );
			aResponse.setPage( thePage );
			aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( searchData+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value = "/getAccountsPayableList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getAccountsPayableList(@RequestParam(value="page", required=false) Integer thePage,
														@RequestParam(value="rows", required=false) Integer theRows,
														@RequestParam(value="sidx", required=false) String theSidx,
														@RequestParam(value="sord", required=false) String theSord,
														@RequestParam(value = "searchData", required = false) String searchData,
														@RequestParam(value = "fromDate", required = false) String fromDate,
														@RequestParam(value = "toDate", required = false) String toDate,
														ModelMap theModel,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException{
		CustomResponse aResponse = null;
		List<VendorBillsBean>  aInvoiceList = null;
		try {
			//int aTotalCount = vendorService.getBillsCount().intValue();
			
			String query=getveBillListQuery(searchData,fromDate,toDate);
		
			int aTotalCount=vendorService.getAccountsPayableCount(searchData,fromDate,toDate,theSidx,theSord);
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			//aInvoiceList = vendorService.getVeBillList(aFrom, aTo);
			aInvoiceList = vendorService.getAccountsPayableList(aFrom, aTo,searchData,fromDate,toDate,theSidx,theSord);
			aResponse = new CustomResponse();
			aResponse.setRows(aInvoiceList);
			aResponse.setRecords( String.valueOf(aInvoiceList.size()) );
			aResponse.setPage( thePage );
			aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( searchData+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value = "/getAccountsPayableListCSV", method = RequestMethod.GET)
	public @ResponseBody
	void printProjectsPDF(
			@RequestParam(value = "searchData", required = false) String searchData,
			@RequestParam(value = "fromDate", required = false) String fromDate,
			@RequestParam(value = "toDate", required = false) String toDate,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException, SQLException, JRException, VendorException {
		Connection connection = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String printtype = null;
			String frmBillDate = "''";
			String toBillDate ="''";
			String queryBuild=getAccountsPayableListQuery(null, fromDate, toDate);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			if(fromDate!=null && !fromDate.trim().equals("")){
			    Date convertedCurrentDate = null;
				try {
					convertedCurrentDate = sdf.parse(fromDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				fromDate=sdff.format(convertedCurrentDate );
			}
			if(toDate!=null && !toDate.trim().equals("")){
			    Date convertedCurrentDate = null;
				try {
					convertedCurrentDate = sdf.parse(toDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				toDate=sdff.format(convertedCurrentDate );
			}
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formattedfrom = format1.format(cal.getTime());
			System.out.println("formattedfrom "+formattedfrom);
			
			cal.add(Calendar.MONTH, -6);
			String formattedto = format1.format(cal.getTime());
			System.out.println("formattedto "+formattedto);
			
			if(!fromDate.equals("")&& !toDate.equals("")){
				
				frmBillDate= fromDate;
				toBillDate = toDate;
			}
			else if(!fromDate.equals("") && toDate.equals("")){
				frmBillDate= fromDate;
				toBillDate =formattedfrom;
				
			}else if(!toDate.equals("") && fromDate.equals("")){
				frmBillDate= "";
				toBillDate = toDate;
			}else{
				frmBillDate ="";
				toBillDate =formattedfrom;
			}
			
			
			
				printtype="xls";
				path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/VendorAccountsPayable.jrxml");
				filename="AccountsPayable.csv";
				logger.info("frmBillDate::"+frmBillDate+" ##toBillDate::"+toBillDate);
//				params.put("frmBillDate", frmBillDate);
				params.put("toBillDate", toBillDate);
			
			con = itspdfService.connectionForJasper();
			//Have to set Params
			//params.put("CuInvoice", CuInvoice);
			
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(queryBuild);
			jd.setQuery(jdq);
			//ReportService.ReportCall(theResponse,params,printtype,path_JRXML,filename,connection);
			ReportService.dynamicReportCall(theResponse, params, printtype, jd, filename, connection);

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> AccountsPayable ==></b>"+toDate,"Vendor Invoice",e,session,theRequest);
		}
		finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value = "/getUninvoiceList",method = RequestMethod.POST)
	public @ResponseBody CustomResponse getUninvoiceList(@RequestParam(value="page", required=false) Integer thePage,
														@RequestParam(value="rows", required=false) Integer theRows,
														@RequestParam(value="sidx", required=false) String theSidx,
														@RequestParam(value="sord", required=false) String theSord,
														@RequestParam(value = "searchData", required = false) String searchData,
														@RequestParam(value = "fromDate", required = false) String fromDate,
														@RequestParam(value = "toDate", required = false) String toDate,
														ModelMap theModel,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, MessagingException{
		CustomResponse aResponse = null;
		List<VendorBillsBean>  aInvoiceList = null;
		String query=null;
		try {
			query=getUninvoiceListCountQuery(searchData,fromDate,toDate);
			int aTotalCount =companyService.getCount(query);
			//int aTotalCount = vendorService.getBillsCount().intValue();
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			if (aTo > aTotalCount) aTo = aTotalCount;
			//aInvoiceList = vendorService.getVeBillList(aFrom, aTo);
			aInvoiceList = vendorService.getUninvoiceList(aFrom, aTo,searchData,fromDate,toDate,theSidx,theSord);
			aResponse = new CustomResponse();
			aResponse.setRows(aInvoiceList);
			aResponse.setRecords( String.valueOf(aInvoiceList.size()) );
			aResponse.setPage( thePage );
			aResponse.setTotal((int) Math.ceil((double)aTotalCount / (double) theRows));
		} catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( searchData+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/addVendorInvoice",method = RequestMethod.POST)
	public @ResponseBody Integer addVendorInvoice(@RequestParam(value="payable", required=false) String thePayable,
														@RequestParam(value="rxMasterID", required=false) Integer rxMasterID,
														@RequestParam(value="recDateId", required=false) Date recDateId,
														@RequestParam(value="vendorInvoice", required=false) String vendorInvoice,
														@RequestParam(value="date", required=false) Date dated,
														@RequestParam(value="apacct", required=false) Integer apacct,
														@RequestParam(value="due", required=false) Date dueDate,
														@RequestParam(value="postdate", required=false) String postdate,
														@RequestParam(value="postDate1", required=false) Date postDate1,
														@RequestParam(value="totalDist1", required=false) BigDecimal totalDist,
														@RequestParam(value="total1", required=false) BigDecimal total,
														@RequestParam(value="balDue1", required=false) BigDecimal balDue,
														@RequestParam(value="veBillIdJob", required=false) Integer veBillId,
														@RequestParam(value="buttonValue", required=false) String buttonValue,
														@RequestParam(value="reason", required=false) String invReason,
														@RequestParam(value="coFiscalPeriodId", required=false) Integer coFiscalPeriodId,
														@RequestParam(value="coFiscalYearId", required=false) Integer coFiscalYearId,
														@RequestParam(value = "gridData", required = false) String gridData,
														@RequestParam(value = "delData[]",required = false) ArrayList<String>  delData,
													 ModelMap theModel,HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, BankingException, MessagingException, CompanyException, JobException{
		Integer theVebill = null;	
		Vebill aVebill = new Vebill();
		Vebilldistribution vBDisObj= new Vebilldistribution();
		
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		
		System.out.println(coFiscalPeriodId+".....>>>>>>"+coFiscalYearId);
		
		
		try {
			if(rxMasterID != null)
				aVebill.setRxMasterId(rxMasterID);
			
			aVebill.setBillDate(dated);
			aVebill.setReceiveDate(recDateId);
			if("Void".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(-1);
			else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(-2);
			else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(0);
			else if("Open".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(1);
			else
				aVebill.setTransactionStatus(2);
			aVebill.setDueDate(dueDate);
			aVebill.setApaccountId(apacct);
			aVebill.setInvoiceNumber(vendorInvoice);
			if(postdate != null && "on".equalsIgnoreCase(postdate))
			{
				aVebill.setUsePostDate(true);
				aVebill.setPostDate(postDate1);				
			}
			else{
				aVebill.setUsePostDate(false);
				aVebill.setPostDate(postDate1);
			}
			
			aVebill.setBillAmount(balDue);
			aVebill.setAppliedAmount(new BigDecimal(0.00));
			aVebill.setTaxAmount(new BigDecimal(0.00));  
			aVebill.setFreightAmount(new BigDecimal(0.00));
			aVebill.setReason(invReason);
			//aVebill.setVePoid(1);
			aVebill.setApplied(false); // edited by leo
			if(veBillId != null)
				aVebill.setVeBillId(veBillId);
			
			theVebill = vendorService.addVendorInvoice(aVebill,vBDisObj,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName());
			aVebill.setVeBillId(theVebill);
			
			if(delData!=null){
				for(String detailID:delData){
					Vebilldistribution aVebilldistribution=new Vebilldistribution();
					aVebilldistribution.setVeBillDistributionId(Integer.valueOf(detailID));
					vendorService.saveVebillDistribution(aVebilldistribution,"delete");
				}
			}
			
			JsonParser parser = new JsonParser();
			if ( gridData!=null) {
				System.out.println("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				System.out.println("array length==>"+array.size());
				for (int ki=0;ki<array.size()-1;ki++) {
					JsonElement ele1=array.get(ki);
					JsonObject obj = ele1.getAsJsonObject();
					String expenseAmount_String=obj.get("expenseAmount").getAsString().replaceAll("\\$", "");
					expenseAmount_String=expenseAmount_String.replaceAll(",", "");
					BigDecimal expenseAmount=JobUtil.ConvertintoBigDecimal(expenseAmount_String);
					Integer veBillDistributionId=null;
					if(obj.get("veBillDistributionId")!=null && obj.get("veBillDistributionId").getAsString()!=""&& obj.get("veBillDistributionId").getAsString().length()>0){
						veBillDistributionId=obj.get("veBillDistributionId").getAsInt();
					}
					String Oper="add";
					if(veBillDistributionId!=null){
						Oper="edit";
					}
					
					Integer coAccountID=null;
					if(obj.get("coExpenseAccountId")!=null && obj.get("coExpenseAccountId").getAsString()!=""&& obj.get("coExpenseAccountId").getAsString().length()>0){
						coAccountID=obj.get("coExpenseAccountId").getAsInt();
					}
					Integer joMasterID=null;
					if(obj.get("joMasterId")!=null && obj.get("joMasterId").getAsString()!=""&& obj.get("joMasterId").getAsString().length()>0){
						joMasterID=obj.get("joMasterId").getAsInt();
					}
					
					
					Vebilldistribution aVebilldistribution=new Vebilldistribution();
					aVebilldistribution.setVeBillDistributionId(veBillDistributionId);
					aVebilldistribution.setCoExpenseAccountId(coAccountID);
					if(joMasterID != null)
						aVebilldistribution.setJoMasterId(joMasterID);
					
					aVebilldistribution.setExpenseAmount(expenseAmount);
					aVebilldistribution.setVeBillId(aVebill.getVeBillId());	
					vendorService.saveVebillDistribution(aVebilldistribution,Oper);
					
				}
				
				/*for(JsonElement ele1 : array){
					JsonObject obj = ele1.getAsJsonObject();
					System.out.println(obj.get("coExpenseAccountId").toString());
					System.out.println(obj.get("joMasterId").toString());
					
					
					if(obj.get("coExpenseAccountId").toString()!="")
					vBDisObj.setCoExpenseAccountId(obj.get("coExpenseAccountId").getAsInt());
					char ch='"';
					String jomasterID = obj.get("joMasterId").toString().replaceAll("[^0-9]+", "");
					if(!jomasterID.equals("")){
						vBDisObj.setJoMasterId(Integer.parseInt(jomasterID));	
					}else{
						System.out.println("jomaster id is empty");
					}
					
				}*/
				
			}
			vendorService.receivingMiscellaneousBill(aVebill,null,coFiscalYearId,coFiscalPeriodId,aUserBean.getFullName(),invReason);
	
		}
		catch (VendorException e) {
			logger.error(e.getMessage());
			
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Transaction Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Invoice No:</b>"+vendorInvoice+"<br><b>RxMasterID:</b>"+rxMasterID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Outside");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,theRequest);
			
		}
		return theVebill;
	}
	
	@RequestMapping(value = "/getCoAccountDetails", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getCoAccountDetails(
			@RequestParam("term") String theProductNameWithCode,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) vendorService
					.getCoAccountDetails(theProductNameWithCode);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( theProductNameWithCode+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aProductCodeWithNameList;
	}
	
	@RequestMapping(value = "/getJobList", method = RequestMethod.GET)
	public @ResponseBody
	List<?> getJobList(
			@RequestParam("term") String theProductNameWithCode,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException, MessagingException {
		logger.debug("Received request to get search Jobs Lists");
		ArrayList<AutoCompleteBean> aProductCodeWithNameList = null;
		try {
			aProductCodeWithNameList = (ArrayList<AutoCompleteBean>) vendorService
					.getJobList(theProductNameWithCode);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( theProductNameWithCode+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return aProductCodeWithNameList;
	}	
	
	@RequestMapping(value = "/getPO", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getPO(
			@RequestParam("po") String thePO,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException,BankingException, MessagingException, EmployeeException  {
		logger.debug("Received request to get search Jobs Lists");
		String poNumber = "";
		Vepo aVepo = null;
		String invoiceStatus = "";
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			
		    aVepo = vendorService.getPONumber(thePO);			
			Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("ReqInvWithPO");
			
			if(aVepo.getPonumber() != null && aVepo.getPonumber().length() > 0)
			{
				invoiceStatus=itsGltransactionService.getAllVeBillDetailswithvePoid(aVepo.getVePoid());
				if(!invoiceStatus.equals("Invoiced"))
				{
				
				Integer rxMasterId = aVepo.getRxVendorId();
				map.put("vendorAddress", (Rxaddress) vendorService.getCustomerAddresses(rxMasterId).get(0));				
				map.put("vendorName", (Rxaddress) vendorService.getVendorName(rxMasterId).get(0));
				map.put("Vepo",aVepo);
				map.put("rxMasterId",rxMasterId);
				map.put("veInvnoandatory",stmtgroupbyjob.getValueLong());
				map.put("SysAccountLinkage", (SysAccountLinkage)itsGltransactionService.getsysAccountLinkageDetail());
				}
				else
				{
				map.put("Registered",new String("Registered"));
				}
				
				Vemaster veMaster = vendorService.gettermsDiscountsfromveMaster(aVepo.getRxVendorId());
				if(veMaster!=null)
					map.put("dueonDaysPO",veMaster.getDueDays());
				else
					map.put("dueonDaysPO",0);
			}
			else
			{
				map.put("veInvnoandatory",stmtgroupbyjob.getValueLong());
				map.put("Vepo",new Vepo());
			}
			
		} catch (VendorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( thePO+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		
		return map;
	}
	
	@RequestMapping(value = "/getDueOnDays", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getDueOnDays(@RequestParam("rxMasterID") Integer rxMasterID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException,BankingException, MessagingException  {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
		System.out.println(rxMasterID);	
		Vemaster veMaster = vendorService.gettermsDiscountsfromveMaster(rxMasterID);
		if(veMaster!=null)
			map.put("dueonDaysPO",veMaster.getDueDays());
		else
			map.put("dueonDaysPO",0);
		} catch (VendorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	@RequestMapping(value = "/getApNumber", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getApNumber(
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException,BankingException  {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("SysAccountLinkage", (SysAccountLinkage)itsGltransactionService.getsysAccountLinkageDetail());
		return map;
		
	}
	
	
	@RequestMapping(value = "/polineitemGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getPOlineitemGrid(
			@RequestParam(value = "vepoID", required = false) Integer vepoID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Line Items VEPOID--->" + vepoID);
		try {
			List<?> aPOLineItemDetails = vendorService.getPOLineDetails(vepoID);
													/*
													 * Retrieve all sales orders
													 * from the service
													 */
			aResponse.setRows(aPOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( vepoID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}

		return aResponse; /*
						 * Spring will automatically convert our CustomResponse
						 * as JSON object. This is triggered by the
						 * @ResponseBody annotation. It knows this because the
						 * JqGrid has set the headers to accept JSON format when
						 * it make a request Spring by default uses Jackson to
						 * convert the object to JSON
						 */
	}	
	
	@RequestMapping(value = "/manpulateVendorInvoiceLineItem", method = RequestMethod.POST)
	public @ResponseBody
	Vebill ManipulateSOReleaseLineItem(
			@RequestParam(value = "note", required = false) String note,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "quantityOrdered", required = false) BigDecimal quantityOrder,
			@RequestParam(value = "unitCost", required = false) BigDecimal unitCost,
			@RequestParam(value = "priceMultiplier", required = false) BigDecimal priceMultiplies,
			@RequestParam(value = "veBillId", required = false) Integer veBillId,
			@RequestParam(value = "prMasterId", required = false) String prMasterID,
			@RequestParam(value = "veBillDetailId", required = false) String veBillDetailId,
			@RequestParam(value = "vePodetailId", required = false) String vePodetailId,
			@RequestParam(value = "oper", required = false) String Oper,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "recDateIdPO", required = false) String recDateIdPO,
			@RequestParam(value = "datePO", required = false) String datePO,
			@RequestParam(value = "duePO", required = false) String duePO,
			@RequestParam(value = "shipviaPO", required = false) Integer shipviaPO,
			@RequestParam(value = "vendorInvoicePO", required = false) String vendorInvoicePO,
			@RequestParam(value = "apacctPO", required = false) Integer apacctPO,
			@RequestParam(value = "postdatePO", required = false) String postdatePO,
			@RequestParam(value = "postDate1PO", required = false) String postDate1PO,
			@RequestParam(value = "shipDatePO", required = false) String shipDatePO,
			@RequestParam(value = "proPO", required = false) String proPO,
			@RequestParam(value = "subtotalGeneralId", required = false) BigDecimal subtotalGeneralId,
			@RequestParam(value = "freightGeneralId", required = false) BigDecimal freightGeneralId,
			@RequestParam(value = "taxGeneralId", required = false) BigDecimal taxGeneralId,
			@RequestParam(value = "totalGeneralId", required = false) BigDecimal totalGeneralId,
			@RequestParam(value = "balDuePO", required = false) BigDecimal balDuePO,
			@RequestParam(value = "vepoId", required = false) Integer vepoId,
			@RequestParam(value = "veBillIdPO", required = false) Integer veBillIdPO,
			@RequestParam(value = "prMasterIDPO", required = false) Integer prMasterIDPO,
			@RequestParam(value = "rxMasterIDPayablePO", required = false) Integer rxMasterIDPayablePO,
			@RequestParam(value = "buttonValue", required = false) String buttonValue,
	
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {			
		
		Vebilldetail aVebilldetail = new Vebilldetail();
		Vebill aVebill = new Vebill();
		UserBean aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		try {
			System.out.println("This is Test");
			if(veBillIdPO != null && veBillIdPO != 0)
			{
				if("del".equalsIgnoreCase(Oper))
				{
					Oper = "delete";
					if(veBillDetailId != null)
						aVebilldetail.setVeBillDetailId(Integer.valueOf(veBillDetailId));
					aVebilldetail.setDescription(desc);
					aVebilldetail.setNote(note);
					aVebilldetail.setQuantityBilled(quantityOrder);
					aVebilldetail.setPriceMultiplier(priceMultiplies);
					if(prMasterID != null)
						aVebilldetail.setPrMasterId(Integer.valueOf(prMasterID));
					else if(prMasterIDPO != null)
						aVebilldetail.setPrMasterId(Integer.valueOf(prMasterIDPO));
					aVebilldetail.setVeBillId(veBillIdPO);
					vendorService.rollbackupdatePrMaster(veBillIdPO);
					vendorService.saveVebillDetail(aVebilldetail,Oper);
				}
				else
				{
					if (recDateIdPO != null && recDateIdPO != "") {
						aVebill.setReceiveDate(DateUtils.parseDate(recDateIdPO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					if (datePO != null && datePO != "") {
						aVebill.setBillDate(DateUtils.parseDate(datePO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					if (duePO != null && duePO != "") {
						aVebill.setDueDate(DateUtils.parseDate(duePO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					
					if (shipDatePO != null && shipDatePO != "") {
						aVebill.setShipDate(DateUtils.parseDate(shipDatePO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					aVebill.setVeShipViaId(shipviaPO);
					aVebill.setApaccountId(apacctPO);
					aVebill.setInvoiceNumber(vendorInvoicePO);
			
					
					if(rxMasterIDPayablePO != null)
						aVebill.setRxMasterId(rxMasterIDPayablePO);
					/*if(postdatePO != null && "on".equalsIgnoreCase(postdatePO))
					{
						aVebill.setUsePostDate(true);
						if (postDate1PO != null && postDate1PO != "") {
							aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
									new String[] { "MM/dd/yyyy" }));
						}
						
					}
					else
					{
						aVebill.setUsePostDate(false);
						if (postDate1PO != null && postDate1PO != "") {
							aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
									new String[] { "MM/dd/yyyy" }));
						}
					}*/
					aVebill.setTrackingNumber(proPO);
					aVebill.setBillAmount(balDuePO);
					aVebill.setFreightAmount(freightGeneralId);
					aVebill.setTaxAmount(taxGeneralId);
					aVebill.setVePoid(vepoId);
					
					
					aVebill.setAppliedAmount(new BigDecimal(0.00));
					//aVebill.setTaxAmount(new BigDecimal(0.00));
					//aVebill.setFreightAmount(new BigDecimal(0.00));
					//aVebill.setVePoid(1);
					aVebill.setApplied(false);
					aVebill.setVoid_(false);
					aVebill.setVeBillId(veBillIdPO);
					if("Void".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(-1);
					else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(0);
					else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(-2);
					else if("Open".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(1);
					else
						aVebill.setTransactionStatus(2);
					
					if(veBillDetailId != null && veBillDetailId != "")
					vendorService.rollbackupdatePrMaster(veBillIdPO);
					
					Integer veBillid = vendorService.insertVebill(aVebill);
					aVebill.setVeBillId(veBillIdPO);
					if(veBillDetailId != null && veBillDetailId != "")
						aVebilldetail.setVeBillDetailId(Integer.valueOf(veBillDetailId));
					aVebilldetail.setDescription(desc);
					aVebilldetail.setNote(note);
					aVebilldetail.setQuantityBilled(quantityOrder);
					aVebilldetail.setPriceMultiplier(priceMultiplies);
					aVebilldetail.setUnitCost(unitCost);
					aVebilldetail.setVePodetailId(JobUtil.ConvertintoInteger(vePodetailId));
					//if("edit".equalsIgnoreCase(Oper))
					/*if(prMasterID != null && prMasterID != "")
						aVebilldetail.setPrMasterId(Integer.valueOf(prMasterID));
					else */if(prMasterIDPO != null && prMasterIDPO !=0)
						aVebilldetail.setPrMasterId(Integer.valueOf(prMasterIDPO));
					aVebilldetail.setVeBillId(veBillIdPO);
					
					vendorService.saveVebillDetail(aVebilldetail,Oper);
					
					
				}
				
			}
			else
			{
				if (recDateIdPO != null && recDateIdPO != "") {
					aVebill.setReceiveDate(DateUtils.parseDate(recDateIdPO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				if (datePO != null && datePO != "") {
					aVebill.setBillDate(DateUtils.parseDate(datePO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				if (duePO != null && duePO != "") {
					aVebill.setDueDate(DateUtils.parseDate(duePO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				
				if (shipDatePO != null && shipDatePO != "") {
					aVebill.setShipDate(DateUtils.parseDate(shipDatePO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				aVebill.setVeShipViaId(shipviaPO);
				aVebill.setApaccountId(apacctPO);
				aVebill.setInvoiceNumber(vendorInvoicePO);
				
				if(rxMasterIDPayablePO != null)
					aVebill.setRxMasterId(rxMasterIDPayablePO);
				/*if(postdatePO != null && "on".equalsIgnoreCase(postdatePO))
				{
					aVebill.setUsePostDate(true);
					if (postDate1PO != null && postDate1PO != "") {
						aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					
				}
				else
				{
					aVebill.setUsePostDate(false);
					if (postDate1PO != null && postDate1PO != "") {
						aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
				}*/
				aVebill.setTrackingNumber(proPO);
				aVebill.setBillAmount(balDuePO);
				aVebill.setFreightAmount(freightGeneralId);
				aVebill.setTaxAmount(taxGeneralId);
				aVebill.setVePoid(vepoId);
				if("Void".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(-1);
				else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(-2);
				else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(0);
				else if("Open".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(1);
				else
					aVebill.setTransactionStatus(2);
				
				aVebill.setAppliedAmount(new BigDecimal(0.00));
				//aVebill.setTaxAmount(new BigDecimal(0.00));
				//aVebill.setFreightAmount(new BigDecimal(0.00));
				//aVebill.setVePoid(1);
				aVebill.setApplied(false);
				aVebill.setVoid_(false);
				
				Integer veBillid = vendorService.insertVebill(aVebill);
				aVebill.setVeBillId(veBillid);
				Integer result = vendorService.insertVebillDetail(veBillid,vepoId,aUserBean.getUserId());
				aVebilldetail.setDescription(desc);
				aVebilldetail.setNote(note);
				aVebilldetail.setQuantityBilled(quantityOrder);
				aVebilldetail.setPriceMultiplier(priceMultiplies);
				aVebilldetail.setPrMasterId(Integer.valueOf(prMasterIDPO));
				aVebilldetail.setVeBillId(veBillid);
				aVebilldetail.setUnitCost(unitCost);
				aVebilldetail.setVePodetailId(Integer.parseInt(vePodetailId));
				vendorService.saveVebillDetail(aVebilldetail,Oper);
			}
			
		} catch (Exception e) {//JobException e
			logger.error(e.getMessage());
			sendTransactionException( veBillId+"","VendorInvoiceBillController",e,theSession,theRequest);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} finally {			
			
		}
		System.out.println("Bill Date :: "+aVebill.getBillDate());
		return aVebill;
	}
	@RequestMapping(value = "/manpulateVendorInvoiceLineItemFromJob", method = RequestMethod.POST)
	public @ResponseBody
	Vebill manpulateVendorInvoiceLineItemFromJob(
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "jobNumber", required = false) String jobNumber,
			@RequestParam(value = "expenseAmount", required = false) BigDecimal expenseAmount,
			@RequestParam(value = "veBillDistributionId", required = false) String veBillDistributionId,
			@RequestParam(value = "coExpenseAccountId", required = false) String coExpenseAccountId,
			@RequestParam(value = "joMasterId", required = false) String joMasterId,
			//@RequestParam(value = "veBillId", required = false) Integer veBillId,
			//@RequestParam(value = "prMasterId", required = false) String prMasterID,
			//@RequestParam(value = "veBillDetailId", required = false) String veBillDetailId,
			//@RequestParam(value = "vePodetailId", required = false) String vePodetailId,
			@RequestParam(value = "oper", required = false) String Oper,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "recDateId", required = false) String recDateIdPO,
			@RequestParam(value = "date", required = false) String datePO,
			@RequestParam(value = "due", required = false) String duePO,
			@RequestParam(value = "vendorInvoice", required = false) String vendorInvoicePO,
			@RequestParam(value = "apacct", required = false) Integer apacctPO,
			@RequestParam(value = "postdate", required = false) String postdatePO,
			@RequestParam(value = "postDate1", required = false) String postDate1PO,
			@RequestParam(value = "pro", required = false) String proPO,
			@RequestParam(value = "totalDist", required = false) BigDecimal totalDist,
			@RequestParam(value = "total", required = false) BigDecimal total,
			@RequestParam(value = "balDue", required = false) BigDecimal balDuePO,
			@RequestParam(value = "veBillId", required = false) Integer veBillIdPO,
			@RequestParam(value = "rxMasterId", required = false) Integer rxMasterId,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			@RequestParam(value = "coAccountID", required = false) Integer coAccountID,
			@RequestParam(value = "buttonValue", required = false) String buttonValue,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {			
		
		Vebilldistribution aVebilldistribution = new Vebilldistribution();
		Vebill aVebill = new Vebill();

		try {
			System.out.println("This is Test");
			if(veBillIdPO != null && veBillIdPO != 0)
			{
				if("del".equalsIgnoreCase(Oper))
				{
					Oper = "delete";
					if(veBillDistributionId != null)
						aVebilldistribution.setVeBillDistributionId(Integer.valueOf(veBillDistributionId));
					aVebilldistribution.setCoExpenseAccountId(coAccountID);
					if(joMasterID != null)
						aVebilldistribution.setJoMasterId(joMasterID);
					aVebilldistribution.setExpenseAmount(expenseAmount);
					aVebilldistribution.setVeBillId(veBillIdPO);	
					vendorService.saveVebillDistribution(aVebilldistribution,Oper);
				}
				else
				{
					if (recDateIdPO != null && recDateIdPO != "") {
						aVebill.setReceiveDate(DateUtils.parseDate(recDateIdPO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					if (datePO != null && datePO != "") {
						aVebill.setBillDate(DateUtils.parseDate(datePO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					if (duePO != null && duePO != "") {
						aVebill.setDueDate(DateUtils.parseDate(duePO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					
					
					aVebill.setApaccountId(apacctPO);
					aVebill.setInvoiceNumber(vendorInvoicePO);
					
					if(rxMasterId != null)
						aVebill.setRxMasterId(rxMasterId);
					/*if(postdatePO != null && "on".equalsIgnoreCase(postdatePO))
					{
						aVebill.setUsePostDate(true);
						if (postDate1PO != null && postDate1PO != "") {
							aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
									new String[] { "MM/dd/yyyy" }));
						}
						
					}
					else
					{
						aVebill.setUsePostDate(false);
						if (postDate1PO != null && postDate1PO != "") {
							aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
									new String[] { "MM/dd/yyyy" }));
						}
					}*/
					aVebill.setTrackingNumber(proPO);
					aVebill.setBillAmount(balDuePO);
					aVebill.setFreightAmount(new BigDecimal(0.0));
					aVebill.setTaxAmount(new BigDecimal(0.0));					
					
					
					aVebill.setAppliedAmount(new BigDecimal(0.00));					
					aVebill.setApplied(false);
					aVebill.setVoid_(false);
					aVebill.setVeBillId(veBillIdPO);
					if("Void".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(-1);
					else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(-2);
					else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(0);
					else if("Open".equalsIgnoreCase(buttonValue))
						aVebill.setTransactionStatus(1);
					else
						aVebill.setTransactionStatus(2);
					
					Integer veBillid = vendorService.insertVebill(aVebill);
					aVebill.setVeBillId(veBillIdPO);
					if(veBillDistributionId != null && veBillDistributionId != "")
						aVebilldistribution.setVeBillDistributionId(Integer.valueOf(veBillDistributionId));
					aVebilldistribution.setCoExpenseAccountId(coAccountID);
					if(joMasterID != null)
						aVebilldistribution.setJoMasterId(joMasterID);
					aVebilldistribution.setExpenseAmount(expenseAmount);
					aVebilldistribution.setVeBillId(veBillIdPO);	
					vendorService.saveVebillDistribution(aVebilldistribution,Oper);
					
				}
				
			}
			else
			{
				if (recDateIdPO != null && recDateIdPO != "") {
					aVebill.setReceiveDate(DateUtils.parseDate(recDateIdPO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				if (datePO != null && datePO != "") {
					aVebill.setBillDate(DateUtils.parseDate(datePO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				if (duePO != null && duePO != "") {
					aVebill.setDueDate(DateUtils.parseDate(duePO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				
				
				aVebill.setApaccountId(apacctPO);
				aVebill.setInvoiceNumber(vendorInvoicePO);
				
				if(rxMasterId != null)
					aVebill.setRxMasterId(rxMasterId);
				/*if(postdatePO != null && "on".equalsIgnoreCase(postdatePO))
				{
					aVebill.setUsePostDate(true);
					if (postDate1PO != null && postDate1PO != "") {
						aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
					
				}
				else
				{
					aVebill.setUsePostDate(false);
					if (postDate1PO != null && postDate1PO != "") {
						aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
								new String[] { "MM/dd/yyyy" }));
					}
				}*/
				aVebill.setTrackingNumber(proPO);
				aVebill.setBillAmount(balDuePO);
				aVebill.setFreightAmount(new BigDecimal(0.0));
				aVebill.setTaxAmount(new BigDecimal(0.0));					
				
				
				aVebill.setAppliedAmount(new BigDecimal(0.00));					
				aVebill.setApplied(false);
				aVebill.setVoid_(false);
				aVebill.setVeBillId(veBillIdPO);
				if("Void".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(-1);
				else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(-2);
				else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(0);
				else if("Open".equalsIgnoreCase(buttonValue))
					aVebill.setTransactionStatus(1);
				else
					aVebill.setTransactionStatus(2);
				
				
				Integer veBillid = vendorService.insertVebill(aVebill);
				aVebill.setVeBillId(veBillid);
				if(veBillDistributionId != null && veBillDistributionId != "")
					aVebilldistribution.setVeBillDistributionId(Integer.valueOf(veBillDistributionId));
				aVebilldistribution.setCoExpenseAccountId(coAccountID);
				if(joMasterID != null)
					aVebilldistribution.setJoMasterId(joMasterID);
				aVebilldistribution.setExpenseAmount(expenseAmount);
				aVebilldistribution.setVeBillId(veBillid);	
				
				vendorService.saveVebillDistribution(aVebilldistribution,Oper);
				
			}
			
		} catch (Exception e) {//JobException e
			logger.error(e.getMessage());
			sendTransactionException( number+"","VendorInvoiceBillController",e,theSession,theRequest);
			//theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		} finally {			
			
		}
		System.out.println("Bill Date :: "+aVebill.getBillDate());
		return aVebill;
	}
	
	//added by prasant

	@RequestMapping(value="/changeTransactionStatus",method = RequestMethod.POST)
	public @ResponseBody Integer CheckTransactionStatus(
			@RequestParam(value = "veBillId", required = true) Integer veBillId,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, VendorException, MessagingException {
		
		
		Integer status=0;
		try{
	
			if(veBillId!=null){
				status=vendorService.changeTransactionStatus(veBillId);
			
				
			}
			
		
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			sendTransactionException( "","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return status;
	}
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/vendorInvoicelineitemGrid", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse vendorInvoicelineitemGrid(
			@RequestParam(value = "vepoID", required = false) Integer vepoID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Vendor Invoice Line Items VEPOID--->" + vepoID);
		try {
			List<?> aPOLineItemDetails = vendorService.getVendorInvoicelineitem(vepoID);
													/*
													 * Retrieve all sales orders
													 * from the service
													 */
			aResponse.setRows(aPOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( vepoID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}

		return aResponse; /*
						 * Spring will automatically convert our CustomResponse
						 * as JSON object. This is triggered by the
						 * @ResponseBody annotation. It knows this because the
						 * JqGrid has set the headers to accept JSON format when
						 * it make a request Spring by default uses Jackson to
						 * convert the object to JSON
						 */
	}
	@RequestMapping(value = "/addVendorInvoiceFromPO",method = RequestMethod.POST)
	public @ResponseBody Vebill addVendorInvoiceFromPO(@RequestParam(value = "recDateIdPO", required = false) String recDateIdPO,
			@RequestParam(value = "datePO", required = false) String datePO,
			@RequestParam(value = "duePO", required = false) String duePO,
			@RequestParam(value = "shipviaPO", required = false) Integer shipviaPO,
			@RequestParam(value = "vendorInvoicePO", required = false) String vendorInvoicePO,
			@RequestParam(value = "apacctPO", required = false) Integer apacctPO,
			@RequestParam(value = "postdatePO", required = false) String postdatePO,
			@RequestParam(value = "postDate1PO", required = false) String postDate1PO,
			@RequestParam(value = "shipDatePO", required = false) String shipDatePO,
			@RequestParam(value = "proPO", required = false) String proPO,
			@RequestParam(value = "subtotalGeneralId", required = false) BigDecimal subtotalGeneralId,
			@RequestParam(value = "freightGeneralId", required = false) BigDecimal freightGeneralId,
			@RequestParam(value = "taxGeneralId", required = false) BigDecimal taxGeneralId,
			@RequestParam(value = "totalGeneralId", required = false) BigDecimal totalGeneralId,
			@RequestParam(value = "balDuePO", required = false) BigDecimal balDuePO,
			@RequestParam(value = "vepoId", required = false) Integer vepoId,
			@RequestParam(value = "veBillIdPO", required = false) Integer veBillIdPO,
			@RequestParam(value = "prMasterIDPO", required = false) Integer prMasterIDPO,
			@RequestParam(value = "rxMasterIDPayablePO", required = false) Integer rxMasterIDPayablePO,
			@RequestParam(value = "buttonValue", required = false) String buttonValue,
			@RequestParam(value = "updatePO", required = false) String updatePO,
			@RequestParam(value = "reason", required = false) String reason,
			@RequestParam(value = "coFiscalPeriodId", required = false) Integer periodId,
			@RequestParam(value = "coFiscalYearId", required = false) Integer yearId,
			@RequestParam(value = "joreleasedetid", required = false) Integer joreleasedetid,
			@RequestParam(value = "operatorStatus", required = false) String operatorStatus,
			@RequestParam(value = "gridData", required = false) String gridData,
			 @RequestParam(value = "delData[]",required = false) ArrayList<String>  delData,
			ModelMap theModel, HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, JobException, ParseException, BankingException, CompanyException, MessagingException{
		Vebill aVebill = new Vebill();
		String result = null;
		Integer insertStatus=0;
		logger.info("updatePO Status : "+updatePO +"REason"+reason);
		Vebilldetail vebilDetail = new Vebilldetail();
		System.out.println("check vendor invoice bill contr");
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		
		try {
			if (recDateIdPO != null && recDateIdPO != "") {
				aVebill.setReceiveDate(DateUtils.parseDate(recDateIdPO.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			if (datePO != null && datePO != "") {
				aVebill.setBillDate(DateUtils.parseDate(datePO.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			if (duePO != null && duePO != "") {
				aVebill.setDueDate(DateUtils.parseDate(duePO.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			
			if (shipDatePO != null && shipDatePO != "") {
				aVebill.setShipDate(DateUtils.parseDate(shipDatePO.trim(),
						new String[] { "MM/dd/yyyy" }));
			}
			aVebill.setVeShipViaId(shipviaPO);
			aVebill.setApaccountId(apacctPO);
			aVebill.setInvoiceNumber(vendorInvoicePO);
			
			if(joreleasedetid!=null)
				aVebill.setJoReleaseDetailId(joreleasedetid);
			
			if(rxMasterIDPayablePO != null)
				aVebill.setRxMasterId(rxMasterIDPayablePO);
			/*if(postdatePO != null && "on".equalsIgnoreCase(postdatePO))
			{
				aVebill.setUsePostDate(true);
				if (postDate1PO != null && postDate1PO != "") {
					aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
				
			}
			else
			{
				aVebill.setUsePostDate(false);
				if (postDate1PO != null && postDate1PO != "") {
					aVebill.setPostDate(DateUtils.parseDate(postDate1PO.trim(),
							new String[] { "MM/dd/yyyy" }));
				}
			}*/
			aVebill.setTrackingNumber(proPO);
			aVebill.setReason(reason);
			aVebill.setBillAmount(subtotalGeneralId.add(freightGeneralId).add(taxGeneralId));
			aVebill.setFreightAmount(freightGeneralId);
			aVebill.setTaxAmount(taxGeneralId);
			aVebill.setVePoid(vepoId);
			if("Void".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(-1);
			else if("Acct. HOLD".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(0);
			else if("Open".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(1);
			else if("Pmt. HOLD".equalsIgnoreCase(buttonValue))
				aVebill.setTransactionStatus(-2);
			else
				aVebill.setTransactionStatus(2);			
			aVebill.setAppliedAmount(new BigDecimal(0.00));
			
			//aVebill.setFreightAmount(new BigDecimal(0.00));
			//aVebill.setVePoid(1);
			aVebill.setApplied(false);
			aVebill.setVoid_(false);
			/*if(veBillIdPO != null && veBillIdPO != 0)
				aVebill.setVeBillId(veBillIdPO);*/
			
		//	Vebill theVebill = new Vebill();
		//	theVebill = vendorService.getVeBill(vepoId);
			
			aVebill.setCreatedById(aUserBean.getUserId());
			aVebill.setCreatedOn(new Date());
			/*
			 * Added by Simon
			 * Reason for Adding ID#573 for differentiating purchases.
			 */
			ArrayList<Prmaster> prMaster=new ArrayList<Prmaster>();
			if(veBillIdPO==null || veBillIdPO == 0)
			{
				if(vepoId!=null&&vepoId!=0){
				Vepo testvepo=itsJobService.getVepo(vepoId);
				if(testvepo!=null&&testvepo.getJoReleaseId()!=null&&testvepo.getJoReleaseId()!=0){
					JoReleaseDetail thejoreleaseDetail=new JoReleaseDetail();
					thejoreleaseDetail.setJoReleaseId(testvepo.getJoReleaseId());
					thejoreleaseDetail.setShipDate(aVebill.getShipDate());
					thejoreleaseDetail.setVendorInvoiceDate(aVebill.getBillDate());
					thejoreleaseDetail.setVendorInvoiceAmount(aVebill.getBillAmount());
					JoRelease thejorelease=itsJobService.getJoRelease(testvepo.getJoReleaseId());
					if(thejorelease!=null&&thejorelease.getJoMasterId()!=null&&thejorelease.getJoMasterId()!=0){
						thejoreleaseDetail.setJoMasterId(thejorelease.getJoMasterId());
					}
					Integer joReleaseDetailID=itsJobService.addJoReleaseDetail(thejoreleaseDetail);
					aVebill.setJoReleaseDetailId(joReleaseDetailID);
				}
				}
				
				Integer veBillid = vendorService.insertVebill(aVebill);
				
				if(veBillid != null && veBillid != 0)
					aVebill.setVeBillId(veBillid);
				else
					aVebill.setVeBillId(veBillIdPO);
				
				if(veBillIdPO == null || veBillIdPO == 0){
					
					//insertStatus = vendorService.insertVebillDetail(veBillid,vepoId,aUserBean.getUserId());
					JsonParser parser = new JsonParser();
					if ( gridData!=null) {
						System.out.println("gridData"+gridData);
						JsonElement ele = parser.parse(gridData);
						JsonArray array = ele.getAsJsonArray();
						System.out.println("array length==>"+array.size());
						for (int ki=0;ki<array.size()-1;ki++) {
							JsonElement ele1=array.get(ki);
							Prmaster master=new Prmaster();
							JsonObject obj = ele1.getAsJsonObject();
							String desc=obj.get("description").getAsString();
							String note=obj.get("note").getAsString();
							master.setItemCode(note);
							BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
							Integer prMasterID=obj.get("prMasterId").getAsInt();
							master.setPrMasterId(prMasterID);
							BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
							String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
							unitCost_String=unitCost_String.replaceAll(",", "");
							BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
							String quantityBilled_String=obj.get("quantityBilled").getAsString().replaceAll("\\$", "");
							quantityBilled_String=quantityBilled_String.replaceAll(",", "");
							BigDecimal quantityBilled=JobUtil.ConvertintoBigDecimal(quantityBilled_String);
							master.setLastCost(quantityBilled);
							Integer veBillDetailId=null;
							if(obj.get("veBillDetailId")!=null && obj.get("veBillDetailId").getAsString()!=""&& obj.get("veBillDetailId").getAsString().length()>0){
								veBillDetailId=obj.get("veBillDetailId").getAsInt();
							}
							String Oper="add";
							if(veBillDetailId!=null){
								Oper="edit";
							}
							
							Integer vePODetailId=null;
							if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
								vePODetailId=obj.get("vePodetailId").getAsInt();
							}
							
							Vebilldetail aVebilldetail=new Vebilldetail();
							aVebilldetail.setVeBillDetailId(veBillDetailId);
							aVebilldetail.setDescription(desc);
							aVebilldetail.setNote(note);
							aVebilldetail.setQuantityBilled(quantityOrder);
							aVebilldetail.setPriceMultiplier(priceMultiplier);
							aVebilldetail.setUnitCost(unitCost);
							aVebilldetail.setVePodetailId(vePODetailId);
							aVebilldetail.setPrMasterId(prMasterID);
							aVebilldetail.setVeBillId(aVebill.getVeBillId());
							
							 if(obj.get("taxable").getAsString().equals("Yes"))
								 aVebilldetail.setTaxable(true);
						        else
						         aVebilldetail.setTaxable(false);
							
							vendorService.saveVebillDetail(aVebilldetail,Oper);
							master.setInitialCost(freightGeneralId);
							prMaster.add(master);
						}
					}
					if((prMaster.size()==0) && (freightGeneralId.compareTo(BigDecimal.ZERO)!=0)){
						Prmaster master=new Prmaster();
						master.setInitialCost(freightGeneralId);
						prMaster.add(master);
					}
					prMaster.trimToSize();
					vendorService.insertVebillHistory(aVebill.getVeBillId(),vepoId,aUserBean.getUserId());
					vendorService.updateveBillHistory(aVebill.getVeBillId());
					
					if( updatePO.equalsIgnoreCase("yes")){
						vendorService.updateVepoStatus(vepoId,2);
					}
					
			if(operatorStatus.equals("close")){
				/*
				 *Modified Signature by Simon
				 *Reason for Modifying ID#573 for differentiating purchases 
				 */
				itsGltransactionService.recieveVendorBill(prMaster,aVebill, insertStatus,yearId,periodId,aUserBean.getFullName());	
			}		
			}
			}
			else
			{
			//update the edited vendor invoice	
				if(!reason.equals(""))
				{
				vendorService.rollbackupdatePrMaster(veBillIdPO);
				}
				aVebill.setVeBillId(veBillIdPO);
				System.out.println("delData"+delData);
				//functionality for delete the grid columns
				if(delData!=null){
					for(String detailID:delData){
						Vebilldetail aVebilldetail=new Vebilldetail();
						System.out.println("delData[i]"+detailID);
						Integer vebillDetailID=JobUtil.ConvertintoInteger(detailID);
						aVebilldetail.setVeBillDetailId(vebillDetailID);
						aVebilldetail.setVeBillId(aVebill.getVeBillId());
						vendorService.saveVebillDetail(aVebilldetail,"delete");
					}
				}
				
				JsonParser parser = new JsonParser();
				if ( gridData!=null) {
					System.out.println("gridData"+gridData);
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (int ki=0;ki<array.size()-1;ki++) {
						JsonElement ele1=array.get(ki);
						Prmaster master=new Prmaster();
						JsonObject obj = ele1.getAsJsonObject();
						String desc=obj.get("description").getAsString();
						String note=obj.get("note").getAsString();
						master.setItemCode(note);
						BigDecimal quantityOrder=obj.get("quantityOrdered").getAsBigDecimal();
						Integer prMasterID=obj.get("prMasterId").getAsInt();
						master.setPrMasterId(prMasterID);
						BigDecimal priceMultiplier=obj.get("priceMultiplier").getAsBigDecimal();
						String unitCost_String=obj.get("unitCost").getAsString().replaceAll("\\$", "");
						unitCost_String=unitCost_String.replaceAll(",", "");
						BigDecimal unitCost=JobUtil.ConvertintoBigDecimal(unitCost_String);
						String quantityBilled_String=obj.get("quantityBilled").getAsString().replaceAll("\\$", "");
						quantityBilled_String=quantityBilled_String.replaceAll(",", "");
						BigDecimal quantityBilled=JobUtil.ConvertintoBigDecimal(quantityBilled_String);
						master.setLastCost(quantityBilled);
						Integer veBillDetailId=null;
						if(obj.get("veBillDetailId")!=null && obj.get("veBillDetailId").getAsString()!=""&& obj.get("veBillDetailId").getAsString().length()>0){
							veBillDetailId=obj.get("veBillDetailId").getAsInt();
						}
						String Oper="add";
						if(veBillDetailId!=null){
							Oper="edit";
						}
						
						Integer vePODetailId=null;
						if(obj.get("vePodetailId")!=null && obj.get("vePodetailId").getAsString()!=""&& obj.get("vePodetailId").getAsString().length()>0){
							vePODetailId=obj.get("vePodetailId").getAsInt();
						}
						
						Vebilldetail aVebilldetail=new Vebilldetail();
						aVebilldetail.setVeBillDetailId(veBillDetailId);
						aVebilldetail.setDescription(desc);
						aVebilldetail.setNote(note);
						aVebilldetail.setQuantityBilled(quantityOrder);
						aVebilldetail.setPriceMultiplier(priceMultiplier);
						aVebilldetail.setUnitCost(unitCost);
						aVebilldetail.setVePodetailId(vePODetailId);
						aVebilldetail.setPrMasterId(prMasterID);
						aVebilldetail.setVeBillId(veBillIdPO);
						
						 if(obj.get("taxable").getAsString().equals("Yes"))
							 aVebilldetail.setTaxable(true);
					        else
					         aVebilldetail.setTaxable(false);
						
						vendorService.saveVebillDetail(aVebilldetail,Oper);
						master.setInitialCost(freightGeneralId);
						prMaster.add(master);
					}
				}
				if(prMaster.size()==0){
					Prmaster master=new Prmaster();
					master.setInitialCost(freightGeneralId);
					prMaster.add(master);
				}
				prMaster.trimToSize();
				
				if(!reason.equals(""))
				{
				Coledgersource aColedgersource = itsGltransactionService.getColedgersourceDetail("VB");
				GlRollback glRollback = new GlRollback();
				glRollback.setVeBillID(aVebill.getVeBillId());
				glRollback.setCoLedgerSourceID(aColedgersource.getCoLedgerSourceId());
				glRollback.setPeriodID(periodId);
				glRollback.setYearID(yearId);
				glRollback.setTransactionDate(aVebill.getBillDate());
				itsGltransactionService.rollBackGlTransaction(glRollback);
				//vendorService.rollbackupdatePrMaster(veBillIdPO);
				}
				aVebill.setChangedById(aUserBean.getUserId());
				aVebill.setChangedOn(new Date());
				vendorService.insertVebill(aVebill);
				vendorService.updateveBillHistory(veBillIdPO);
				if( operatorStatus!=null)
				if(veBillIdPO != null && veBillIdPO != 0 && operatorStatus.equals("close")&&!buttonValue.equals("Void"))
				itsGltransactionService.recieveVendorBill(prMaster,aVebill,0,yearId,periodId,aUserBean.getFullName());	
			}
			
		//	throw new VendorException("Testing Exception");
			
		}
		catch (VendorException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Transaction Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Invoice No:</b>"+aVebill.getInvoiceNumber()+"<br><b>RxMasterID:</b>"+aVebill.getRxMasterId());
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Outside");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,theRequest);
		}
		
		/* Commented by Jenith on 2014-09-11 11:52
		 * catch (FinancePostingException e){
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		catch (ProductException e) {
			logger.error(e.getMessage());
		}*/
		return aVebill;
	}
	
	@RequestMapping(value = "/getveBillDetails", method = RequestMethod.POST)
	public @ResponseBody
	HashMap<String, Object> getveBillDetails(
			@RequestParam("veBillId") Integer veBillId,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws JobException, IOException, MessagingException, EmployeeException {
		logger.debug("Received request to get search Jobs Lists");
		Vebill aVebill = null;
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			aVebill = vendorService.getVeBillObj(veBillId);
			Sysvariable stmtgroupbyjob= itsEmployeeService.getSysVariableDetails("ReqInvWithPO");
			
			if(aVebill.getVeBillId() != null)
			{
				Integer rxMasterId = aVebill.getRxMasterId();
				
				map.put("vendorAddress", (Rxaddress) vendorService
						.getCustomerAddresses(rxMasterId).get(0));				
			
				map.put("vendorName", (Rxaddress) vendorService
						.getVendorName(rxMasterId).get(0));
				
				
				map.put("veInvmandatory",stmtgroupbyjob.getValueLong());
				map.put("Vebill",aVebill);
				map.put("rxMasterId",rxMasterId);
				if(aVebill.getVePoid() != null)
					map.put("Vepo", vendorService.getVepo(aVebill.getVePoid()));
				else
					map.put("Vepo", new Vepo());
				
				
			}
			else
			{
				map.put("veInvmandatory",stmtgroupbyjob.getValueLong());
				map.put("Vebill",new Vebill());
			}
		} catch (VendorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( veBillId+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		
		return map;
	}
	@RequestMapping(value = "/getVendorInvoice", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getVendorInvoice(
			@RequestParam(value = "vepoID", required = false) Integer vepoID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest)
			throws IOException, JobException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		System.out.println("Vendor Invoice Line Items VEPOID--->" + vepoID);
		try {	
			List<?> aPOLineItemDetails = vendorService.getVendorInvoiceList(vepoID);
													/*
													 * Retrieve all sales orders
													 * from the service
													 */
			aResponse.setRows(aPOLineItemDetails); /*
													 * assign those objects to
													 * the custom response
													 */
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			sendTransactionException( vepoID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}

		return aResponse; /*
						 * Spring will automatically convert our CustomResponse
						 * as JSON object. This is triggered by the
						 * @ResponseBody annotation. It knows this because the
						 * JqGrid has set the headers to accept JSON format when
						 * it make a request Spring by default uses Jackson to
						 * convert the object to JSON
						 */
	}
	
	@RequestMapping(value = "/getInvoicepaymentdetailsfordialogbox", method = RequestMethod.POST)
	public @ResponseBody List<VeBillPaymentHistory> getInvoicepaymentdetailsfordialogbox(@RequestParam(value = "veBillID", required = false) Integer veBillID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException  {
		
		List<VeBillPaymentHistory> aveBillPaymentHistory = new ArrayList<VeBillPaymentHistory>();
		aveBillPaymentHistory = vendorService.getveBillPaymentHistory(veBillID);
		return aveBillPaymentHistory;
	}
	
	@RequestMapping(value = "/getVeBillListOutSide", method = RequestMethod.POST)
	public @ResponseBody VendorBillsBean getVeBillListOutSide(@RequestParam(value = "veBillID", required = false) Integer veBillID,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws VendorException  {
		
		VendorBillsBean aVendorBillsBean = new VendorBillsBean();
		aVendorBillsBean = vendorService.getVeBillListOutSide(veBillID);
		return aVendorBillsBean;
	}
	
	@RequestMapping(value = "/getPoTotalequalsvendorinvoice", method = RequestMethod.POST)
	public @ResponseBody Boolean getPoTotalequalsvendorinvoice(
			@RequestParam(value="vePoID", required= false) String vePoID,
			@RequestParam(value="invNo", required= false) String invNo,
			HttpSession theSession,HttpServletResponse theResponse,HttpServletRequest theRequest) throws IOException, JobException, MessagingException {
		logger.info("vePOID: "+vePoID);
		Vepo aVepo = new Vepo();
		boolean Status = false ;
		
		Integer vepoID;
		if(vePoID!=null){
			vepoID = Integer.parseInt(vePoID);
		}else{
			vepoID=0;
		}
		try {
				
			Status= vendorService.getPoTotalequalsvendorinvoice(vepoID);
				
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException( vePoID+"","VendorInvoiceBillController",e,theSession,theRequest);
		}
		return Status;
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
	public String getveBillListQuery(String searchData,String startDate,String endDate){
		String aVendorBillsListQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID, "
				//	+ "CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate,moTransaction.Reference,moTransaction.TransactionDate "
					+ "CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate, "
					+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.checkNo AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS reference,"
					+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.datePaid AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS datePaid,"
					+ "(SELECT DISTINCT GROUP_CONCAT(CAST(vph.amountVal AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS Amount,veBill.TransactionStatus,veBill.creditUsed"
					+ " FROM  veBill "
					+ "LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
					+ "LEFT OUTER JOIN vePO ON veBill.vePOID = vePO.vePOID  "
					//+ "LEFT OUTER JOIN moTransaction ON moTransactionID IN (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = veBill.veBillID Group by veBillID)"
					+ " WHERE (veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL) ";
			         
			if(searchData !=null && !searchData.equals("")){
				aVendorBillsListQry+= "And  (veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
						" OR veBill.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
						" OR AppliedAmount LIKE '%"+searchData+"%' OR veBill.vePOID LIKE '%"+searchData+"%' OR veBill.joReleaseDetailID LIKE '%"+searchData+"%')";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			if(startDate!=null && !startDate.trim().equals("")){
				//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = null;
				try {
					convertedCurrentDate = sdf.parse(startDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    startDate=sdff.format(convertedCurrentDate );
			    //System.out.println("Formated Date:"+startDate);
			}
			if(endDate!=null && !endDate.trim().equals("")){
				//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			    Date convertedCurrentDate = null;
				try {
					convertedCurrentDate = sdf.parse(endDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
			    endDate=sdff.format(convertedCurrentDate );
			    //System.out.println("Formated End Date:"+endDate);
			}
			
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			String formattedfrom = format1.format(cal.getTime());
			System.out.println("formattedfrom "+formattedfrom);
			
			cal.add(Calendar.MONTH, -6);
			String formattedto = format1.format(cal.getTime());
			System.out.println("formattedto "+formattedto);
			
			if(!startDate.equals("")&& !endDate.equals("")){
				
				aVendorBillsListQry+= " AND BillDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
			}
			else if(!startDate.equals("") && endDate.equals("")){
				aVendorBillsListQry+= " AND BillDate >='"+startDate+" 00:00:00'";
			}else if(!endDate.equals("") && startDate.equals("")){
				aVendorBillsListQry+= " AND BillDate<='"+endDate+" 23:59:59'";
			}else{
				aVendorBillsListQry+= " AND BillDate BETWEEN '"+formattedto +" 00:00:00' AND '"+formattedfrom+" 23:59:59'";
			}
					
		return 	aVendorBillsListQry;
			
	}
	public String getUninvoiceListCountQuery(String searchData,String startDate,String endDate){
		String aVendorBillsListQry = "SELECT DISTINCT veBillID" 
		+ "   FROM  veBill LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
		+ " LEFT OUTER JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID=veBill.joReleaseDetailID " 
		+ " LEFT OUTER JOIN joRelease ON joRelease.joReleaseID =joReleaseDetail.joReleaseID "
		+ " LEFT OUTER JOIN joMaster ON joMaster.joMasterID =joRelease.joMasterID "
		+ " LEFT OUTER JOIN vePO ON vePO.vePOID=veBill.vePOID "
		+ " LEFT OUTER JOIN moTransaction ON moTransactionID = (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = veBill.veBillID " 
		//+ " GROUP BY veBillID) WHERE (veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL) AND veBill.joReleaseDetailID IS NOT NULL AND "
		+ " GROUP BY veBillID) WHERE veBill.joReleaseDetailID IS NOT NULL AND "
		+ " joReleaseDetail.CustomerInvoiceDate IS NULL AND joReleaseDetail.CustomerInvoiceAmount IS NULL ";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR veBill.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR veBill.vePOID LIKE '%"+searchData+"%' OR veBill.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    startDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated Date:"+startDate);
		}
		if(endDate!=null && !endDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    endDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated End Date:"+endDate);
		}
		
		if(!startDate.equals("")&& !endDate.equals("")){
			
			aVendorBillsListQry+= " AND veBill.ReceiveDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
		}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND veBill.ReceiveDate >='"+startDate+" 00:00:00'";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND veBill.ReceiveDate<='"+endDate+" 23:59:59'";
		}
				
		
		return aVendorBillsListQry;
	}
	@RequestMapping(value = "/printUninvoiceList",method = RequestMethod.GET)
	public @ResponseBody void printUninvoiceList(@RequestParam(value = "searchData", required = false) String searchData,
														@RequestParam(value = "fromDate", required = false) String startDate,
														@RequestParam(value = "toDate", required = false) String endDate,
														ModelMap theModel,HttpSession theSession,HttpServletRequest request,HttpServletResponse theResponse,HttpServletRequest theRequest) throws SQLException {

		
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		 SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    startDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated Date:"+startDate);
		}
		if(endDate!=null && !endDate.trim().equals("")){
			//SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    //SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		    endDate=sdff.format(convertedCurrentDate );
		    //System.out.println("Formated End Date:"+endDate);
		}
		
		
				
		ConnectionProvider con=null;
		Connection connection =null;
		try {
			
			String aVendorBillsListQry = "SELECT DISTINCT veBillID, BillDate, PONumber, InvoiceNumber, veBill.rxMasterID," 
					+ " CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) AS PayableTo, BillAmount, AppliedAmount,veBill.vePOID,veBill.joReleaseDetailID,veBill.DueDate,"
					+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.checkNo AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS reference,"
			+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.datePaid AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS datePaid,"
			+ " (SELECT DISTINCT GROUP_CONCAT(CAST(vph.amountVal AS CHAR)) FROM veBillPaymentHistory vph WHERE vph.veBillID = veBill.veBillID) AS Amount,"
			+ " veBill.TransactionStatus,veBill.creditUsed,veBill.ReceiveDate as ReceiveDate,joMaster.JobNumber as JobNumber,joMaster.Description as JobName"
			+ "   FROM  veBill LEFT OUTER JOIN rxMaster ON veBill.rxMasterID = rxMaster.rxMasterID "
			+ " LEFT OUTER JOIN joReleaseDetail ON joReleaseDetail.joReleaseDetailID=veBill.joReleaseDetailID " 
			+ " LEFT OUTER JOIN joRelease ON joRelease.joReleaseID =joReleaseDetail.joReleaseID "
			+ " LEFT OUTER JOIN joMaster ON joMaster.joMasterID =joRelease.joMasterID "
			+ " LEFT OUTER JOIN vePO ON vePO.vePOID=veBill.vePOID "
			+ " LEFT OUTER JOIN moTransaction ON moTransactionID = (SELECT DISTINCT moTransactionID FROM moLinkageDetail WHERE veBillID = veBill.veBillID " 
			//+ " GROUP BY veBillID) WHERE (veBill.vePOID IS NULL OR veBill.vePOID IS NOT NULL) AND veBill.joReleaseDetailID IS NOT NULL AND "
			+ " GROUP BY veBillID) WHERE veBill.joReleaseDetailID IS NOT NULL AND "
			+ " joReleaseDetail.CustomerInvoiceDate IS NULL AND joReleaseDetail.CustomerInvoiceAmount IS NULL ";
					         
					if(searchData !=null && !searchData.equals("")){
						aVendorBillsListQry+= "And  (veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
								" OR veBill.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rxMaster.Name, ' ', rxMaster.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
								" OR AppliedAmount LIKE '%"+searchData+"%' OR veBill.vePOID LIKE '%"+searchData+"%' OR veBill.joReleaseDetailID LIKE '%"+searchData+"%')";
					}
					
					if(!startDate.equals("")&& !endDate.equals("")){
						
						aVendorBillsListQry+= " AND veBill.ReceiveDate BETWEEN '"+startDate +" 00:00:00' AND '"+endDate+" 23:59:59'";
					}
					else if(!startDate.equals("") && endDate.equals("")){
						aVendorBillsListQry+= " AND veBill.ReceiveDate >='"+startDate+" 00:00:00'";
					}else if(!endDate.equals("") && startDate.equals("")){
						aVendorBillsListQry+= " AND veBill.ReceiveDate<='"+endDate+" 23:59:59'";
					}
					
					System.out.println("aVendorBillsListQry"+aVendorBillsListQry);
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/uninvoice.jrxml");
			
			 con = itspdfService.connectionForJasper();
			//Have to set Params
			//params.put("tsUserLoginID", tsUserLoginID);
			String filename="Uninvoice.pdf";
			connection = con.getConnection();
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(aVendorBillsListQry);
			jd.setQuery(jdq);
			
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);
		}catch(Exception e){
			
		}finally{
			if(con!=null){
				try {
				con.closeConnection(connection);
				con=null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	
	public String getAccountsPayableListQuery(String searchData,String startDate,String endDate) throws VendorException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
		if(startDate!=null && !startDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(startDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    startDate=sdff.format(convertedCurrentDate );
		}
		
		if(endDate!=null && !endDate.trim().equals("")){
		    Date convertedCurrentDate = null;
			try {
				convertedCurrentDate = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		    endDate=sdff.format(convertedCurrentDate );
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedto = format1.format(cal.getTime());
		System.out.println("formattedfrom "+formattedto);
		
		cal.add(Calendar.MONTH, -6);
		String formattedfrom = format1.format(cal.getTime());
		System.out.println("formattedto "+formattedfrom);
		
		String customDate = "";
		
		if(!startDate.equals("")&& !endDate.equals("")){
			customDate = endDate;		}
		else if(!startDate.equals("") && endDate.equals("")){
			customDate = startDate;
		}else if(!endDate.equals("") && startDate.equals("")){
			customDate = endDate;
		}else{
			customDate = formattedto;
		}

		
		String aVendorBillsListQry = "SELECT vb.veBillID,vb.BillDate,vp.PONumber,vb.InvoiceNumber,vb.rxMasterID,CONCAT(rx.Name, ' ', rx.FirstName) AS PayableTo,"
		+ " vb.BillAmount,vb.AppliedAmount,vb.vePOID,vb.joReleaseDetailID,vb.DueDate,mo.Reference,mo.TransactionDate,(mLD.Amount+mLD.Discount) AS amountVal,"
		+ " vb.TransactionStatus,vb.creditUsed,(DATEDIFF('"+customDate+"',vb.BillDate)) AS age ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))<=30 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS currentAmount,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>30 AND DATEDIFF('"+customDate+"',vb.BillDate)<=60) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age30Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate)>60 AND DATEDIFF('"+customDate+"',vb.BillDate)<=90) THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age60Amount ,"
		+ " (CASE WHEN (DATEDIFF('"+customDate+"',vb.BillDate))>90 THEN (IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)) ELSE 0 END) AS age90Amount ,"
		+ " IF(DATE(mo.TransactionDate)>'"+customDate+"',vb.BillAmount-(vb.AppliedAmount-(mLD.Amount+mLD.Discount)),vb.BillAmount-vb.AppliedAmount)AS balance"
		+ " FROM veBill vb LEFT JOIN moLinkageDetail mLD ON vb.veBillID = mLD.veBillID LEFT JOIN moTransaction mo ON mLD.moTransactionID = mo.moTransactionID AND mo.Void <>1"
		+ " LEFT OUTER JOIN rxMaster rx ON vb.rxMasterID = rx.rxMasterID LEFT OUTER JOIN vePO vp ON vb.vePOID = vp.vePOID"
		+ " WHERE (vb.vePOID IS NULL OR vb.vePOID IS NOT NULL) and vb.TransactionStatus >0 or vb.TransactionStatus=-2 ";
		         
		if(searchData !=null && !searchData.equals("")){
			aVendorBillsListQry+= "And  (vb.veBillID LIKE '%"+searchData+"%' OR PONumber LIKE '%"+searchData+"%' OR InvoiceNumber LIKE '%"+searchData+"%'" +
					" OR vb.rxMasterID LIKE '%"+searchData+"%' OR CONCAT(rx.Name, ' ', rx.FirstName) LIKE '%"+searchData+"%' OR BillAmount LIKE '%"+searchData+"%'" +
					" OR AppliedAmount LIKE '%"+searchData+"%' OR vb.vePOID LIKE '%"+searchData+"%' OR vb.joReleaseDetailID LIKE '%"+searchData+"%')";
		}
		
		if(!startDate.equals("")&& !endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >= '"+startDate +"' AND Date(BillDate) <= '"+endDate+"' GROUP BY vb.veBillID ";	}
		else if(!startDate.equals("") && endDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) >='"+startDate+"' GROUP BY vb.veBillID ";
		}else if(!endDate.equals("") && startDate.equals("")){
			aVendorBillsListQry+= " AND Date(BillDate) <='"+endDate+"' GROUP BY vb.veBillID ";
		}else{
			aVendorBillsListQry+= " AND Date(BillDate) <= '"+formattedto+"' GROUP BY vb.veBillID ";
		}
				
		String orderByIndex="";
		String orderBy="ASC";
		
		     
		/*if(sortIndex.equals("billDate")){
			orderByIndex="BillDate";
		}else if(sortIndex.equals("ponumber")){
			orderByIndex="PONumber";
		}else if(sortIndex.equals("veInvoiceNumber")){
			orderByIndex="InvoiceNumber";
		}else if(sortIndex.equals("payableTo")){*/
			orderByIndex="PayableTo";
		/*}else if(sortIndex.equals("billAmount")){
			orderByIndex="BillAmount";
		}else if(sortIndex.equals("appliedAmount")){
			orderByIndex="AppliedAmount";
		}else if(sortIndex.equals("dueDate")){
			orderByIndex="vb.DueDate";
		}else if(sortIndex.equals("chkNo")){
			orderByIndex="reference";
		}else if(sortIndex.equals("age")){
			orderByIndex="age";
		}else if(sortIndex.equals("currentAmount")){
			orderByIndex="currentAmount";
		}else if(sortIndex.equals("age30Amount")){
			orderByIndex="age30Amount";
		}else if(sortIndex.equals("age60Amount")){
			orderByIndex="age60Amount";
		}else if(sortIndex.equals("age90Amount")){
			orderByIndex="age90Amount";
		}*/
		
		/*if(!sortOrder.equals("")){
			orderBy=sortOrder.toUpperCase();
		}*/
		aVendorBillsListQry+=" order by "+orderByIndex+"  "+orderBy;
		String overallQuery="SELECT accountpayable.*,IF(ABS(balance)>0.01,TRUE,FALSE) AS checkstatus from ("+aVendorBillsListQry+") AS accountpayable HAVING checkstatus=1 ";
		
		return overallQuery;
	}
	
}
