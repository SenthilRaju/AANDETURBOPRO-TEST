package com.turborep.turbotracker.projectsView.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.dao.JobsBean;
import com.turborep.turbotracker.job.exception.QuoteTemplateException;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.projectsView.ProjectBean;
import com.turborep.turbotracker.projectsView.service.ProjectService;
import com.turborep.turbotracker.sales.dao.SalesRepBean;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vepo;

@Controller
@RequestMapping("/projectscontroller")
public class ProjectsController {

	protected static Logger logger = Logger.getLogger("controller");

	@Resource(name="projectServices")
	private ProjectService projectService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "companyService")
	private CompanyService itsCompanyService;
	
	@Resource(name = "userLoginService")
	private UserService userService;
	
	@RequestMapping(value="/projects_dormant",method = RequestMethod.GET)
	public @ResponseBody CustomResponse upcoming(@RequestParam("salesRepId") int theSalesRepNo, HttpSession theSession,
			HttpServletRequest therequest) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		try{
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.PROJECTS_USERID);
		if(aSalesUserId != theSalesRepNo) {
			theSession.setAttribute(SessionConstants.PROJECTS_USERID, theSalesRepNo);
		}
		int aSalesRepId = theSalesRepNo;
		logger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		ArrayList<JobsBean> aDormantProjectsList = (ArrayList<JobsBean>) projectService.getDormantProjects(aSalesRep);
		
		aResponse.setRows(aDormantProjectsList);
		aResponse.setRecords( String.valueOf(aDormantProjectsList.size()));
		aResponse.setPage(1);
		}catch(Exception e){
			sendTransactionException("<b> theSalesRepNo==></b>"+theSalesRepNo,"Project",e,theSession,therequest);
			return aResponse;
		}
		return aResponse;
	}
	
	@RequestMapping(value="/projects_ordertracking",method = RequestMethod.GET)
	public @ResponseBody CustomResponse orderTracking(@RequestParam("salesRepId") int theSalesRepNo, HttpSession theSession,HttpServletRequest therequest) throws IOException, MessagingException {
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.PROJECTS_USERID);
		CustomResponse aResponse = new CustomResponse();
		if(aSalesUserId != theSalesRepNo) {
			theSession.setAttribute(SessionConstants.PROJECTS_USERID, theSalesRepNo);
		}
		try{
		int aSalesRepId = theSalesRepNo;
		logger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		ArrayList<JobsBean> aOrderTrackingList = (ArrayList<JobsBean>) projectService.getOrderTrackingDetails(aSalesRep);
		
		aResponse.setRows(aOrderTrackingList);
		aResponse.setRecords( String.valueOf(aOrderTrackingList.size()) );
		aResponse.setPage(1);
		
		}catch(Exception e){
			sendTransactionException("<b> theSalesRepNo==></b>"+theSalesRepNo,"Project",e,theSession,therequest);
			return aResponse;
		}
		return aResponse;
	}
	
	@RequestMapping(value="/projects_submittal",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAllBookedSubmittal(@RequestParam("salesRepId") int theSalesRepNo, HttpSession theSession,HttpServletRequest therequest) throws IOException, MessagingException {
		int salesUserId = (Integer)theSession.getAttribute(SessionConstants.PROJECTS_USERID);
		CustomResponse aResponse = new CustomResponse();
		if(salesUserId != theSalesRepNo) {
			theSession.setAttribute(SessionConstants.PROJECTS_USERID, theSalesRepNo);
		}
		try{
		int aSalesRepId = theSalesRepNo;
		logger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		ArrayList<JobsBean> aBokkedList = (ArrayList<JobsBean>) projectService.getBookedSubmit(aSalesRep);
		aResponse.setRows(aBokkedList);
		aResponse.setRecords( String.valueOf(aBokkedList.size()) );
		aResponse.setPage(1);
		}catch(Exception e){
			sendTransactionException("<b> theSalesRepNo==></b>"+theSalesRepNo,"Project",e,theSession,therequest);
			return aResponse;
		}
		return aResponse;
	}
	
	@RequestMapping(value="/projects_pendingCredit",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getBookedPendingCredit(@RequestParam("salesRepId") int theSalesRepNo,
			HttpSession theSession,HttpServletRequest therequest) throws IOException, MessagingException {
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.PROJECTS_USERID);
		CustomResponse aResponse = new CustomResponse();
		if(aSalesUserId != theSalesRepNo) {
			theSession.setAttribute(SessionConstants.PROJECTS_USERID, theSalesRepNo);
		}
		try{
		int aSalesRepId = theSalesRepNo;
		logger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		ArrayList<JobsBean> aBokkedList = (ArrayList<JobsBean>) projectService.getBookedPendingCredit(aSalesRep);
		
		aResponse.setRows(aBokkedList);
		aResponse.setRecords( String.valueOf(aBokkedList.size()) );
		aResponse.setPage(1);
		}catch(Exception e){
			sendTransactionException("<b> theSalesRepNo==></b>"+theSalesRepNo,"Project",e,theSession,therequest);
			return aResponse;
		}
		return aResponse;
	}
	
	@RequestMapping(value="/projects_pendingOrder",method = RequestMethod.GET)
	public @ResponseBody CustomResponse getAllBookedCredit(@RequestParam("salesRepId") int theSalesRepNo, HttpSession theSession,
			HttpServletRequest therequest) throws IOException, MessagingException {
		int aSalesUserId = (Integer)theSession.getAttribute(SessionConstants.PROJECTS_USERID);
		CustomResponse aResponse = new CustomResponse();
		if(aSalesUserId != theSalesRepNo) {
			theSession.setAttribute(SessionConstants.PROJECTS_USERID, theSalesRepNo);
		}
		try{
		int aSalesRepId = theSalesRepNo;
		logger.debug("Sales rep id: " + aSalesRepId);
		SalesRepBean aSalesRep = new SalesRepBean();
		aSalesRep.setSalesRepId(theSalesRepNo);
		ArrayList<JobsBean> aCreditList = (ArrayList<JobsBean>) projectService.getBookedPendingOrder(aSalesRep);
		
		aResponse.setRows(aCreditList);
		aResponse.setRecords( String.valueOf(aCreditList.size()) );
		aResponse.setPage(1);
		}catch(Exception e){
			sendTransactionException("<b> theSalesRepNo==></b>"+theSalesRepNo,"Project",e,theSession,therequest);
			return aResponse;
		}
		return aResponse;
	}
	@RequestMapping(value = "/getCustomerARDetails", method = RequestMethod.GET)
	public  @ResponseBody Map<String, Object> getDefaultsDetails(@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			 HttpServletRequest therequest,HttpSession theSession) throws IOException, MessagingException {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<BigDecimal> getArdetails =null;
		ArrayList<BigDecimal> getProfitDetails=null;
		BigDecimal getPriorDetail =null;
		ArrayList<BigDecimal> getUnappliedArdetails =null;
		try{
		getArdetails = projectService.getCustomerARDetails(tsUserLoginID);
		//getUnappliedArdetails = projectService.getCustomerUnappliedARDetails(tsUserLoginID);
		getProfitDetails= projectService.getProjectCalculation(tsUserLoginID);
		getPriorDetail = projectService.getProjectPriorCalculation(tsUserLoginID);
		
			/*map.put("ar", getArdetails.get(0).add(getUnappliedArdetails.get(0)));
			map.put("thirty", getArdetails.get(1).add(getUnappliedArdetails.get(1)));
			map.put("sixty", getArdetails.get(2).add(getUnappliedArdetails.get(2)));
			map.put("ninty", getArdetails.get(3).add(getUnappliedArdetails.get(3)));*/
		map.put("ar", getArdetails.get(0));
		map.put("thirty", getArdetails.get(1));
		map.put("sixty", getArdetails.get(2));
		map.put("ninty", getArdetails.get(3));
			
			/*map.put("ar", getArdetails.get(0));
			map.put("thirty", getArdetails.get(1));
			map.put("sixty", getArdetails.get(2));
			map.put("ninty", getArdetails.get(3));*/
			map.put("margin", getProfitDetails.get(0));
			map.put("ytd", getProfitDetails.get(1));
			map.put("priorytd", getPriorDetail);
		}catch(Exception e){
			sendTransactionException("<b> tsUserLoginID==></b>"+tsUserLoginID,"Project",e,theSession,therequest);
			return map;
		}finally{
			getArdetails =null;
			getProfitDetails=null;
			getPriorDetail =null;
			getUnappliedArdetails =null;
		}	
		return map;		
	
	}
	
	@RequestMapping(value = "/getOpenedJobs", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getOpenedJobs(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value="fromdate", required=false) String fromdate,
			@RequestParam(value="todate", required=false) String todate,
			@RequestParam(value = "rxCustomerID", required = false) Integer rxCustomerID,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException
			 {
		
		String fromdateDate ="", todateDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		CustomResponse aResponse = new CustomResponse();
		
		try {
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			
		
			if(fromdate!=null && !fromdate.equals("")){
				fromdateDate  = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(fromdate));
			}
			if(todate!=null && !todate.equals("")){
				todateDate = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(todate));
			}
			int aTotalCount =projectService.getOpenedJobs(rxCustomerID,tsUserLoginID,fromdateDate,todateDate);	
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<?> aSOLineItemDetails = projectService.getOpenedJobs(rxCustomerID,tsUserLoginID,aFrom, aTo,theSidx,theSord,fromdateDate,todateDate);
			aResponse.setRecords(String.valueOf(aSOLineItemDetails.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));

			aResponse.setRows(aSOLineItemDetails); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b> tsUserLoginID,rxCustomerID==></b>"+tsUserLoginID+","+rxCustomerID,"Project",e,session,therequest);
		}

		return aResponse; 
	}
	
	@RequestMapping(value = "/getProfitMargin", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getProfitMargin(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value="fromdate", required=false) String fromdate,
			@RequestParam(value="todate", required=false) String todate,
			@RequestParam(value = "rxCustomerID", required = false) Integer rxCustomerID,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException
			 {
		
		String fromdateDate ="", todateDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		CustomResponse aResponse = new CustomResponse();
		logger.info("Page::"+thePage+"rows::"+theRows);
		try {
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			
		
			if(fromdate!=null && !fromdate.equals("")){
				fromdateDate  = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(fromdate));
			}
			if(todate!=null && !todate.equals("")){
				todateDate = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(todate));
			}
			int aTotalCount =projectService.getProfitMarginTotal(rxCustomerID,tsUserLoginID,aFrom, aTo,theSidx,theSord,fromdateDate,todateDate);	
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<?> aProfitMargin = projectService.getProfitMargin(rxCustomerID,tsUserLoginID,aFrom, aTo,theSidx,theSord,fromdateDate,todateDate);
			aResponse.setRecords(String.valueOf(aProfitMargin.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));

			aResponse.setRows(aProfitMargin); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b> tsUserLoginID,rxCustomerID==></b>"+tsUserLoginID+","+rxCustomerID,"Project",e,session,therequest);
			
		}finally{
			fromdateDate =null;
			todateDate =null;
			sdf =null;
		}

		return aResponse; 
	}

	@RequestMapping(value = "/getPurchaseSalesOrder", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getPurchaseSalesOrder(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value="fromdate", required=false) String fromdate,
			@RequestParam(value="todate", required=false) String todate,
			@RequestParam(value = "rxCustomerID", required = false) Integer rxCustomerID,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException
			 {
		
		String fromdateDate ="", todateDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		CustomResponse aResponse = new CustomResponse();
		logger.info("Called PO & SO \nPage:"+thePage+"\nRows:"+theRows+"\nSort:"+theSidx+"\nSorder:"+theSord+"\nFromDate:"+fromdate+"\nToDate:"+todate);
		
		try {
			int aFrom = 0, aTo = 0;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			
		
			if(fromdate!=null && !fromdate.equals("")){
				fromdateDate  = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(fromdate));
			}
			if(todate!=null && !todate.equals("")){
				todateDate = sdf.format(new SimpleDateFormat("MM/dd/yyyy").parse(todate));
			}
			int aTotalCount =projectService.getPurchaseSalesOrder(rxCustomerID,tsUserLoginID,fromdateDate,todateDate);	
			logger.info("Total Values"+aTotalCount);
			if (aTo > aTotalCount) aTo = aTotalCount;
			List<Vepo> aPoSoDetails = projectService.getPurchaseSalesOrder(rxCustomerID,tsUserLoginID,aFrom, aTo,theSidx,theSord,fromdateDate,todateDate);
			aResponse.setRecords(String.valueOf(aPoSoDetails.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));
			aResponse.setRows(aPoSoDetails); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b> tsUserLoginID,rxCustomerID==></b>"+tsUserLoginID+","+rxCustomerID,"Project",e,session,therequest);
		}finally{
			fromdateDate =null;
			todateDate = null;
			sdf = null;
		}

		return aResponse; 
	}
	
	/*@RequestMapping(value = "/getCustomerAccountRecieveDetails", method = RequestMethod.GET)
	public @ResponseBody
	CustomResponse getCustomerAccountRecieveDetails(
			@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			HttpSession session, HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, MessagingException
			 {

		CustomResponse aResponse = new CustomResponse();
		
		try {
			int aFrom, aTo;
			aTo = (theRows * thePage);
			aFrom = aTo - theRows;
			
			int aTotalCount =projectService.getCustomerAccountRecieveDetails(tsUserLoginID);	
			System.out.println("aTotalCount "+aTotalCount);
			
			if (aTo > aTotalCount) aTo = aTotalCount;
			ArrayList<?> getArdetails = projectService.getCustomerAccountRecieveDetails(tsUserLoginID,aFrom, aTo,theSidx,theSord);
		//	List<?> aSOLineItemDetails = projectService.getOpenedJobs(rxCustomerID,tsUserLoginID,aFrom, aTo,theSidx,theSord,fromdateDate,todateDate);
			aResponse.setRecords(String.valueOf(getArdetails.size()));
			aResponse.setPage(thePage);
			aResponse.setTotal((int) Math.ceil((double)aTotalCount/ (double) theRows));

			aResponse.setRows(getArdetails); 
		} catch (Exception e) {
			logger.error(e.getMessage());
			sendTransactionException("<b> tsUserLoginID==></b>"+tsUserLoginID+","+tsUserLoginID,"Project",e,session,therequest);
		}

		return aResponse; 
	}*/
	
	@RequestMapping(value = "/getCustomerAccountRecieveDetails", method = RequestMethod.GET)
	public  @ResponseBody	CustomResponse   getCustomerAccountRecieveDetails(@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "asofardate", required = false) String asofardate,
			 HttpServletRequest theRequest,HttpSession theSession) {
		System.out.println("inside the getCustomerAccountRecieveDetails"+theSidx+theSord);
		CustomResponse aResponse = new CustomResponse();
		ArrayList<ProjectBean> getArdetails = projectService.getCustomerAccountRecieveDetails(tsUserLoginID,customerID,asofardate);
		System.out.println("size==>"+getArdetails.size());
		if (getArdetails.size() > 0) {
			
			if(theSidx.equals("customerName")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getCustomerName().compareTo(object2.getCustomerName());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("currentAmt")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getCurrentAmt().compareTo(object2.getCurrentAmt());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("thirtyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getThirtyDays().compareTo(object2.getThirtyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("sixtyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getSixtyDays().compareTo(object2.getSixtyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("ninetyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getNinetyDays().compareTo(object2.getNinetyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("totalDaysAmt")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getTotalDaysAmt().compareTo(object2.getTotalDaysAmt());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}	
		   }
		
		
    	aResponse.setRows(getArdetails); 
    	return aResponse;
	}
	
	@RequestMapping(value = "/getARDetailsBasedonCustomer", method = RequestMethod.GET)
	public  @ResponseBody	CustomResponse   getARDetailsBasedonCustomer(@RequestParam(value="page", required=false) Integer thePage,
			@RequestParam(value="rows", required=false) Integer theRows,
			@RequestParam(value="sidx", required=false) String theSidx,
			@RequestParam(value="sord", required=false) String theSord,
			@RequestParam(value = "tsUserLoginID", required = false) Integer tsUserLoginID,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "inputDate", required = false) String inputDate,
			@RequestParam(value = "Customername", required = false) Integer Customername,
			 HttpServletRequest theRequest,HttpSession theSession) throws IOException, MessagingException {
		System.out.println("inside the getCustomerAccountRecieveDetails");
		CustomResponse aResponse = new CustomResponse();
		try{
		ArrayList<ProjectBean> getArdetails = projectService.getARDetailsBasedonCustomer(tsUserLoginID,customerID,inputDate,Customername);
		System.out.println("size==>"+getArdetails.size());
if (getArdetails.size() > 0) {
			
			if(theSidx.equals("invoiceDate")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getInv_date().compareTo(object2.getInv_date());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("invoiceNumber")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getInvoiceNumber().compareTo(object2.getInvoiceNumber());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("poNumber")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getPoNumber().compareTo(object2.getPoNumber());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("poAmount")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getPoAmount().compareTo(object2.getPoAmount());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("days")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getDays().compareTo(object2.getDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("currentAmt")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getCurrentAmt().compareTo(object2.getCurrentAmt());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("thirtyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getThirtyDays().compareTo(object2.getThirtyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("sixtyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getSixtyDays().compareTo(object2.getSixtyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("ninetyDays")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getNinetyDays().compareTo(object2.getNinetyDays());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}else if(theSidx.equals("totalDaysAmt")){
				Collections.sort(getArdetails, new Comparator<ProjectBean>() {
			        @Override
			        public int compare(final ProjectBean object1, final ProjectBean object2) {
			            return object1.getTotalDaysAmt().compareTo(object2.getTotalDaysAmt());
			        }
			       });
				if(theSord.equals("desc")){
					Collections.reverse(getArdetails);
				}
			}				
		   }
		
		
		
    	aResponse.setRows(getArdetails); 
		}catch(Exception e){
			sendTransactionException("<b> tsUserLoginID,customerID==></b>"+tsUserLoginID+","+customerID,"Project",e,theSession,theRequest);
		}
    	return aResponse; 
	}
	
	
	@RequestMapping(value = "/printProjectsPDF", method = RequestMethod.GET)
	public @ResponseBody
	void printProjectsPDF(
			@RequestParam(value = "number", required = false) Integer number,
			@RequestParam(value = "selectedUser", required = false) Integer selectedUser,
			@RequestParam(value = "customerID", required = false) Integer customerID,
			@RequestParam(value = "AsOf", required = false) String inputDate,
			@RequestParam(value = "Customername", required = false) Integer Customername,
			HttpServletResponse theResponse, HttpServletRequest theRequest,HttpSession session)
			throws IOException, MessagingException, SQLException, JRException, ParseException {
		Connection connection = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String printtype="pdf";
			
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			
			java.sql.Date asofdt =null;
			 java.util.Date utilDate = null;
			
			if(inputDate!=null && inputDate.length()>0 && inputDate!=""){
				
			/*	
				java.util.Date javaDate=new java.util.Date();
			 asofdt = new java.sql.Date(javaDate.getTime());*/
			 
			 
			 
			 SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
			 SimpleDateFormat cdyFormat = new SimpleDateFormat("yyyy-MM-dd");
			    Date date = null;
		
					date = mdyFormat.parse(inputDate);
					 asofdt = new java.sql.Date(date.getTime());
			/*    
				utilDate = formatter.parse(inputDate);
				asofdt = new java.sql.Date(utilDate.getTime());*/
			}
			else
			{
				utilDate = new java.util.Date();
				asofdt = new java.sql.Date(utilDate.getTime());
			}
			con=itspdfService.connectionForJasper();
			if(number==0){
					
					printtype="xls";
					path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/AccountReceivablesCSV.jrxml");
					filename="AccountReceivables.csv";
					params.put("UserLoginId", selectedUser);
					params.put("asOfDate", inputDate);
					String query =null;
	
					JasperDesign jd  = JRXmlLoader.load(path_JRXML);
					if(selectedUser>0)
						query ="CALL sp_arreport1('PDF','"+selectedUser+"','"+asofdt+"')";
					else
						query ="CALL sp_arreport1('PDF','0','"+asofdt+"')";
					

					JRDesignQuery jdq=new JRDesignQuery();
					jdq.setText(query);
					jd.setQuery(jdq);
					//ConnectionProvider con = itspdfService.connectionForJasper();
					connection = con.getConnection();
					ReportService.dynamicReportCall(theResponse,params,printtype,jd,filename,connection);
					connection.close();	
				//	return;	
			}
			
			if(number==1){
				path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/AccountReceivables.jrxml");
				String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
				filename="AccountRecievablefirstgrid.pdf";
				params.put("UserLoginId", selectedUser);
				params.put("asOfDate", inputDate);
				String query =null;

			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			if(selectedUser>0)
				query ="CALL sp_arreport1('PDF','"+selectedUser+"','"+asofdt+"')";
			else
				query ="CALL sp_arreport1('PDF','0','"+asofdt+"')";
					
			
			System.out.println("Query::"+query);
			
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			//ConnectionProvider con = itspdfService.connectionForJasper();
			connection = con.getConnection();
			ReportService.dynamicReportCall(theResponse,params,printtype,jd,filename,connection);
			connection.close();	
		//	return;	
				
			}else if(number==2){
				String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
				path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/AccountRecievablesecondgrid.jrxml");
				filename="AccountRecievablesecondgrid.pdf";
		
				
				if(inputDate!=null && inputDate.length()>0 && inputDate!=""){
					 SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
					 SimpleDateFormat cdyFormat = new SimpleDateFormat("yyyy-MM-dd");
					    Date date = null;
						try {
							date = mdyFormat.parse(inputDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    todayDate=cdyFormat.format(date);
				}
				
				if(Customername!=null && Customername!=0 ){
				//	customerID=Customername;
				}
				Rxmaster rxmaster=new Rxmaster();
				 try {
					 rxmaster= itsCompanyService.getEmployeeDetails(customerID.toString());
				} catch (CompanyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				params.put("AsOf", todayDate);
				params.put("UserLoginId", selectedUser);
				params.put("CustomerId", customerID);
				params.put("CustomerName", rxmaster.getName());
				
				String query="";
				
				JasperDesign jd  = JRXmlLoader.load(path_JRXML);
				/*if(selectedUser>0)
					query ="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID ,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total FROM   (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days, InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance FROM cuInvoice LEFT JOIN cuLinkageDetail cdp on cdp.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID  LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID   WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND IF( DATE(cdp.datePaid) > '"+todayDate+"',0,(ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02))  AND "+selectedUser+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)  AND cuInvoice.rxCustomerID="+customerID+" "
							+ " UNION ALL "
							+ " (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
							+ " sum(cdp.PaymentApplied+cdp.DiscountUsed) AS Balance  FROM  cuLinkageDetail cdp ,cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID "
							+ " LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
							+ " AND cdp.cuInvoiceID = cuInvoice.cuInvoiceID AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(cdp.datePaid) > '"+todayDate+"' AND DATE(InvoiceDate)<= '"+todayDate+"' Group by cuInvoice.cuInvoiceID) "
							+ " UNION ALL "
							+ " (SELECT 0 AS cuInvoiceID,ReceiptDate AS InvoiceDate,'Unapplied' AS InvoiceNumber,(CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
							+ " WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,( SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount ) AS totalamount,DATEDIFF('"+todayDate+"',ReceiptDate) AS Days,(SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount) AS Balance"
							+ " FROM cuReceipt cr LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID AND DATE(cdp.datePaid) <= '"+todayDate+"')"
							+ " WHERE cr.rxCustomerID IN ('"+customerID+"') AND  DATE(ReceiptDate)<= '"+todayDate+"' AND cr.reversePaymentStatus <>1  GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID  HAVING Balance<0))AS SubQuery WHERE Days>=0";
				else
				query ="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID ,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total FROM   (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days, InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance FROM cuInvoice LEFT JOIN cuLinkageDetail cdp on cdp.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID  LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID   WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND IF( DATE(cdp.datePaid) > '"+todayDate+"',0,(ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02)) AND cuInvoice.rxCustomerID="+customerID+" "
						+ " UNION ALL "
						+ " (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
						+ " sum(cdp.PaymentApplied+cdp.DiscountUsed) AS Balance  FROM  cuLinkageDetail cdp ,cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID "
						+ " LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
						+ " AND cdp.cuInvoiceID = cuInvoice.cuInvoiceID AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(cdp.datePaid) > '"+todayDate+"' AND DATE(InvoiceDate)<= '"+todayDate+"' Group by cuInvoice.cuInvoiceID) "
						+ " UNION ALL "
						+ " (SELECT 0 AS cuInvoiceID,ReceiptDate AS InvoiceDate,'Unapplied' AS InvoiceNumber,(CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
						+ " WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,( SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount ) AS totalamount,DATEDIFF('"+todayDate+"',ReceiptDate) AS Days,(SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount) AS Balance"
						+ " FROM cuReceipt cr LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID AND DATE(cdp.datePaid) <= '"+todayDate+"')"
						+ " WHERE cr.rxCustomerID IN ('"+customerID+"') AND  DATE(ReceiptDate)<= '"+todayDate+"' AND cr.reversePaymentStatus <>1 GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID  HAVING Balance<0))AS SubQuery WHERE Days>=0";
						
			*/
					String employeeCondn =" ";String empCond="";
						if(selectedUser!=null && selectedUser != 0){
						employeeCondn= " AND "+selectedUser+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
						empCond= " AND "+selectedUser+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) AND rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
						
						
						}
						else
						{
						employeeCondn ="AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
						empCond ="AND rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
						
						} 
					
					query="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, "
							+" CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,"
							+" CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total  "
							+" FROM ("
							+" SELECT cuInvoice.cuInvoiceID,InvoiceDate,"
							+" InvoiceNumber,CustomerPONumber,ar.InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
							+" ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance "
							+ "From (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory WHERE DATE(createdOn)<='"+todayDate+"' AND `inv_rec`=0 GROUP BY cuInvoiceID) sub  JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)"
							+" JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID) "
							+" LEFT JOIN cuMaster  ON cuMaster.cuMasterID = cuInvoice.rxCustomerID"
							+" WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND `inv_rec`=0 "
							+ employeeCondn
							+" GROUP BY cuInvoiceID "
							
							+" Union All "
							
							+" SELECT 0 AS cuInvoiceID, ReceiptDate  AS InvoiceDate,'Unapplied' AS InvoiceNumber, "
							+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
							+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
							+" (SUM(IFNULL(Appliedamount,0))-receiptamount) AS totalamount , "
							+" DATEDIFF('"+todayDate+"',ReceiptDate) AS Days, (SUM(IFNULL(Appliedamount,0))-receiptamount) AS Balance FROM ( "
							+" SELECT ar.arhistoryID,Reference,cuReceiptTypeID,cuReceipt.rxCustomerID,cuReceipt.cuReceiptID,IF(revornot=1,0,Amount) AS receiptamount,DiscountAmt,Appliedamount,revornot,`createdOn`,`ReceiptDate` FROM  "
							+" (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory  WHERE  DATE(createdOn)<='"+todayDate+"' AND `inv_rec`=1  GROUP BY cuReceiptID,cuInvoiceID) sub "
							+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)   "
							+" LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
							+" WHERE `inv_rec`=1 )AS sub LEFT JOIN rxMaster ON(sub.rxCustomerID=rxMaster.rxMasterID) WHERE rxCustomerID='"+customerID+"' GROUP BY cuReceiptID "
							
										
					
								+" Union All "
					
							
							+"  SELECT 0 AS cuInvoiceID,ar.createdOn AS InvoiceDate,'Unapplied' AS InvoiceNumber,"
							+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
							+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
							+"ar.Appliedamount AS totalamount,DATEDIFF('"+todayDate+"' ,ar.`createdOn`) AS Days,(ar.Appliedamount*-1) AS Balance "
							+" FROM  (	"
							+" SELECT MAX(arhistoryID) AS arhistoryID,inv_rec FROM arhistory WHERE (inv_rec=1 OR inv_rec=2) AND  DATE(createdOn)<='"+todayDate+"' "
							+" GROUP BY cuReceiptID "
							+" ) sub JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID) "  
							+ " LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
							+"  LEFT JOIN rxMaster ON(cuReceipt.rxCustomerID=rxMaster.rxMasterID) "
							+"  LEFT JOIN cuMaster ON cuMaster.cuMasterID = rxMaster.rxMasterID "
							+" WHERE ar.inv_rec=2 "+empCond
									+" ) AS SubQuery WHERE Days>=0 and ABS(Balance)>0";
							
							
					
					
					
					
					
					

							
				
				System.out.println("Query::"+query);
				
				JRDesignQuery jdq=new JRDesignQuery();
				jdq.setText(query);
				jd.setQuery(jdq);
				//ConnectionProvider con = itspdfService.connectionForJasper();
				connection = con.getConnection();
				ReportService.dynamicReportCall(theResponse,params,printtype,jd,filename,connection);
				connection.close();	
				//return;	
				
				
			}else if(number==3){
				printtype="xls";
				String todayDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
				path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/AccountRecievablesecondgrid.jrxml");
				filename="AccountRecievablesecondgrid.csv";
				
				
				if(inputDate!=null && inputDate.length()>0 && inputDate!=""){
					 SimpleDateFormat mdyFormat = new SimpleDateFormat("MM/dd/yyyy");
					 SimpleDateFormat cdyFormat = new SimpleDateFormat("yyyy-MM-dd");
					    Date date = null;
						try {
							date = mdyFormat.parse(inputDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    todayDate=cdyFormat.format(date);
				}
				
				if(Customername!=null && Customername!=0 ){
					customerID=Customername;
				}
				Rxmaster rxmaster=new Rxmaster();
				 try {
					 rxmaster= itsCompanyService.getEmployeeDetails(customerID.toString());
				} catch (CompanyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 params.put("AsOf", todayDate);
					params.put("UserLoginId", selectedUser);
					params.put("CustomerId", customerID);
					params.put("CustomerName", rxmaster.getName());
					
					String query="";
					
					JasperDesign jd  = JRXmlLoader.load(path_JRXML);
					/*if(selectedUser>0)
						query ="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID ,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total FROM   (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days, InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance FROM cuInvoice LEFT JOIN cuLinkageDetail cdp on cdp.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID  LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID   WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND IF( DATE(cdp.datePaid) > '"+todayDate+"',0,(ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02)) AND "+selectedUser+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4)  AND cuInvoice.rxCustomerID="+customerID+" "
								+ " UNION ALL "
								+ " (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
								+ " sum(cdp.PaymentApplied+cdp.DiscountUsed) AS Balance  FROM  cuLinkageDetail cdp ,cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID "
								+ " LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
								+ " AND cdp.cuInvoiceID = cuInvoice.cuInvoiceID AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(cdp.datePaid) > '"+todayDate+"' AND DATE(InvoiceDate)<= '"+todayDate+"' Group by cuInvoice.cuInvoiceID) "
								+ " UNION ALL "
								+ " (SELECT 0 AS cuInvoiceID,ReceiptDate AS InvoiceDate,'Unapplied' AS InvoiceNumber,(CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
								+ " WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,( SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount ) AS totalamount,DATEDIFF('"+todayDate+"',ReceiptDate) AS Days,(SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount) AS Balance"
								+ " FROM cuReceipt cr LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID AND DATE(cdp.datePaid) <= '"+todayDate+"')"
								+ " WHERE cr.rxCustomerID IN ('"+customerID+"') AND  DATE(ReceiptDate)<= '"+todayDate+"' AND cr.reversePaymentStatus <>1 GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID  HAVING Balance<0))AS SubQuery WHERE Days>=0";
								
					else
						query ="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID ,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total FROM   (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days, InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0)) AS Balance FROM cuInvoice LEFT JOIN cuLinkageDetail cdp on cdp.cuInvoiceID = cuInvoice.cuInvoiceID LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID  LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID   WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND IF( DATE(cdp.datePaid) > '"+todayDate+"',0,(ABS(InvoiceAmount-(AppliedAmount+IFNULL(DiscountAmt,0))) > .02)) AND cuInvoice.rxCustomerID="+customerID+" "
								+ " UNION ALL "
								+ " (SELECT cuInvoice.cuInvoiceID,InvoiceDate,InvoiceNumber,CustomerPONumber,InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
								+ " sum(cdp.PaymentApplied+cdp.DiscountUsed) AS Balance  FROM  cuLinkageDetail cdp ,cuInvoice LEFT JOIN cuMaster ON cuMaster.cuMasterID = cuInvoice.rxCustomerID "
								+ " LEFT JOIN rxMaster ON rxMaster.rxMasterID = cuInvoice.rxCustomerID  WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) "
								+ " AND cdp.cuInvoiceID = cuInvoice.cuInvoiceID AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(cdp.datePaid) > '"+todayDate+"' AND DATE(InvoiceDate)<= '"+todayDate+"' Group by cuInvoice.cuInvoiceID) "
								+ " UNION ALL "
								+ " (SELECT 0 AS cuInvoiceID,ReceiptDate AS InvoiceDate,'Unapplied' AS InvoiceNumber,(CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
								+ " WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,( SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount ) AS totalamount,DATEDIFF('"+todayDate+"',ReceiptDate) AS Days,(SUM(IFNULL(cdp.PaymentApplied,0))-cr.Amount) AS Balance"
								+ " FROM cuReceipt cr LEFT JOIN cuLinkageDetail AS cdp ON (cr.rxCustomerID = cdp.rxCustomerID AND cdp.cuReceiptID = cr.cuReceiptID AND DATE(cdp.datePaid) <= '"+todayDate+"')"
								+ " WHERE cr.rxCustomerID IN ('"+customerID+"') AND  DATE(ReceiptDate)<= '"+todayDate+"' AND cr.reversePaymentStatus <>1 GROUP BY cr.cuReceiptID,cr.ReceiptDate, cr.rxCustomerID  HAVING Balance<0))AS SubQuery WHERE Days>=0";
								
					*/
					
					String employeeCondn =" ";
					if(selectedUser!=null && selectedUser != 0){
					employeeCondn= " AND "+selectedUser+" IN(cuMaster.cuAssignmentID0,cuMaster.cuAssignmentID1,cuMaster.cuAssignmentID2,cuMaster.cuAssignmentID3,cuMaster.cuAssignmentID4) AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
					}
					else
					{
					employeeCondn ="AND cuInvoice.rxCustomerID='"+customerID+"' AND DATE(ar.createdOn)<='"+todayDate+"'";
					} 
				
				query="SELECT InvoiceDate,InvoiceNumber,CustomerPONumber,totalamount,Days,CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END AS AmtCur, "
						+" CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END AS Amt30, CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END AS Amt60,"
						+" CASE WHEN Days>90 THEN Balance ELSE 0 END AS Amt90,cuInvoiceID,(CASE WHEN Days>=0 AND Days<=30 THEN Balance ELSE 0 END +  CASE WHEN Days>30 AND Days<=60 THEN Balance ELSE 0 END +  CASE WHEN Days>60 AND Days<=90 THEN Balance ELSE 0 END +  CASE WHEN Days>90 THEN Balance ELSE 0 END ) AS total  "
						+" FROM ("
						+" SELECT cuInvoice.cuInvoiceID,InvoiceDate,"
						+" InvoiceNumber,CustomerPONumber,ar.InvoiceAmount AS totalamount,DATEDIFF('"+todayDate+"',InvoiceDate) AS Days,"
						+" ar.InvoiceAmount-(ar.AppliedAmount+IFNULL(ar.DiscountAmt,0)) AS Balance "
						+ "From (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory WHERE DATE(createdOn)<='"+todayDate+"' AND `inv_rec`=0 GROUP BY cuInvoiceID) sub  JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)"
						+" JOIN cuInvoice ON(ar.cuInvoiceID=cuInvoice.cuInvoiceID) "
						+" LEFT JOIN cuMaster  ON cuMaster.cuMasterID = cuInvoice.rxCustomerID"
						+" WHERE IF(CreditMemo = 0,CreditMemo=0,memoStatus=1) AND (TransactionStatus>0) AND `inv_rec`=0 "
						+ employeeCondn
						+" GROUP BY cuInvoiceID "
						
						+" Union All "
						
						+" SELECT 0 AS cuInvoiceID, ReceiptDate  AS InvoiceDate,'Unapplied' AS InvoiceNumber, "
						+" (CASE cuReceiptTypeID WHEN 1 THEN CONCAT('CH-',IFNULL(Reference,'')) WHEN 2 THEN CONCAT('CK-',IFNULL(Reference,'')) WHEN 3 THEN CONCAT('CC-',IFNULL(Reference,''))"
						+" WHEN 4 THEN CONCAT('OR-',IFNULL(Reference,'')) END) AS CustomerPONumber,"
						+" (SUM(IFNULL(Appliedamount,0))-receiptamount) AS totalamount , "
						+" DATEDIFF('"+todayDate+"',ReceiptDate) AS Days, (SUM(IFNULL(Appliedamount,0))-receiptamount) AS Balance FROM ( "
						+" SELECT ar.arhistoryID,Reference,cuReceiptTypeID,cuReceipt.rxCustomerID,cuReceipt.cuReceiptID,IF(revornot=1,0,Amount) AS receiptamount,DiscountAmt,Appliedamount,revornot,`createdOn`,`ReceiptDate` FROM  "
						+" (SELECT MAX(arhistoryID) AS arhistoryID,cuInvoiceID FROM arhistory  WHERE  DATE(createdOn)<='"+todayDate+"' AND `inv_rec`=1  GROUP BY cuReceiptID,cuInvoiceID) sub "
						+" JOIN `arhistory` ar ON(sub.arhistoryID=ar.arhistoryID)   "
						+" LEFT JOIN cuReceipt ON(ar.cuReceiptID=cuReceipt.cuReceiptID) "
						+" WHERE `inv_rec`=1 )AS sub LEFT JOIN rxMaster ON(sub.rxCustomerID=rxMaster.rxMasterID) WHERE rxCustomerID='"+customerID+"' GROUP BY cuReceiptID "
						
						+" )AS SubQuery WHERE Days>=0 and ABS(Balance)>0";
					System.out.println("Query::"+query);
					
					JRDesignQuery jdq=new JRDesignQuery();
					jdq.setText(query);
					jd.setQuery(jdq);
				//	ConnectionProvider con = itspdfService.connectionForJasper();
					connection = con.getConnection();
					ReportService.dynamicReportCall(theResponse,params,printtype,jd,filename,connection);
					connection.close();	
				//	return;	
					
			}
			else // new added else part..
			{
			//ConnectionProvider con = itspdfService.connectionForJasper();
			//Have to set Params
			//params.put("CuInvoice", CuInvoice);
			
			connection = con.getConnection();
			
			ReportService.ReportCall(theResponse,params,printtype,path_JRXML,filename,connection);
			}
			

		} catch (SQLException e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> customerID==></b>"+customerID,"Project",e,session,theRequest);
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> customerID==></b>"+customerID,"Project",e,session,theRequest);
			}
		finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getJoMasterData", method = RequestMethod.GET)
	public @ResponseBody String getNewQuoteTemplateHeader(@RequestParam(value = "joReleaseId", required = false) Integer joReleaseId,
			HttpSession theSession, HttpServletResponse theResponse,HttpServletRequest therequest)
			throws ParseException, IOException, QuoteTemplateException, MessagingException {
		String returnString = "";
		try {
			returnString = projectService.getJoMasterData(joReleaseId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> joReleaseId==></b>"+joReleaseId,"Project",e,theSession,therequest);
		}
		return returnString;
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
}