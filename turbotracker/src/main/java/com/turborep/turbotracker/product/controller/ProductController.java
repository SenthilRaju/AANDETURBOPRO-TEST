package com.turborep.turbotracker.product.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.company.service.TaxTerritoriesService;
import com.turborep.turbotracker.customer.controller.CustomerListController;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.finance.dao.Transactionmonitor;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.mail.SendQuoteMail;
import com.turborep.turbotracker.product.dao.Prinventorycount;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prorderpoint;
import com.turborep.turbotracker.product.dao.Prtransfer;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwhtransferdetail;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.user.dao.TpUsage;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.JobUtil;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.SessionConstants;

@Controller
@RequestMapping("/product")
public class ProductController {

	protected static Logger itsLogger = Logger
			.getLogger(CustomerListController.class);

	@Resource(name = "userLoginService")
	private UserService itsUserService;
	
	@Resource(name = "productService")
	private ProductService itsProductService;
	
	@Resource(name="taxTerritoriesService")
	private TaxTerritoriesService itstaxTerritoriesService;
	
	@Resource(name = "jobService")
	private JobService jobService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name = "chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;
	
	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;
	
	private List<Coaccount> itsCoAccount;

	@RequestMapping(value = "/warehouseGrid", method = RequestMethod.GET)
	public @ResponseBody CustomResponse getWareHouses(HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		CustomResponse aResponse = new CustomResponse();
		itsLogger.info("Called");
		try {
			List<Prwarehouse> wareHouses = itsProductService.getWarehouses();
			aResponse.setRows(wareHouses);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> methodname==></b>warehouseGrid","Inventory",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value = "/getTaxTerritory", method = RequestMethod.GET)
	public @ResponseBody CoTaxTerritory getSingleTaxTerritory(
			@RequestParam(value="taxID", required=false) Integer taxID,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		CoTaxTerritory acotTerritory  = new CoTaxTerritory();
		try {
			acotTerritory = itstaxTerritoriesService.getSingleTaxTerritory(taxID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> taxID==></b>"+taxID,"Inventory",e,session,therequest);
		}
		return acotTerritory;
	}
	@RequestMapping(value = "/deleteWarehouse", method = RequestMethod.GET)
	public @ResponseBody boolean deleteWareHouse(
			@RequestParam(value="wareHouseID", required=false) Integer warehouseID,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		boolean deleted = false;
		try {
			itsProductService.deleteWarehouse(warehouseID);
			deleted = true;
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> warehouseID==></b>"+warehouseID,"Inventory",e,session,therequest);
		}
		return deleted;
	}
	
	@RequestMapping(value = "/updateWarehouse", method = RequestMethod.GET)
	public @ResponseBody boolean updateWarehouse(@RequestParam(value="warehouseID", required=false) Integer warehouseID,
																	@RequestParam(value="description", required=false) String description,
																	@RequestParam(value="companyName", required=false) String companyName,
																	@RequestParam(value="warehouseInactive", required=false) boolean warehouseInactive,
																	@RequestParam(value="assetName", required=false) Integer assetNameID,
																	@RequestParam(value="adjustcogName", required=false) Integer adjustCogAccountID,
																	@RequestParam(value="taxTerritoryName", required=false) Integer taxTerritoryNameID,
																	@RequestParam(value="emailPickUpName", required=false) String emailPickUp,
																	@RequestParam(value="Address1", required=false) String Address1,
																	@RequestParam(value="Address2", required=false) String Address2,
																	@RequestParam(value="city", required=false) String city,
																	@RequestParam(value="state", required=false) String state,
																	@RequestParam(value="zip", required=false) String zip,
																	@RequestParam(value="additionaladdressLine", required=false) String additionaladdressLine,
																	HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, MessagingException {
		boolean updated = false;
		Prwarehouse aWarehouse = new Prwarehouse();
		try {
			aWarehouse.setAddress1(Address1);
			aWarehouse.setAddress2(Address2);
			aWarehouse.setCity(city);
			aWarehouse.setCoAccountIdasset(assetNameID);
			aWarehouse.setAdjustCogAccountID(adjustCogAccountID);
			aWarehouse.setCoTaxTerritoryId(taxTerritoryNameID);
			aWarehouse.setSearchName(description);
			aWarehouse.setDescription(companyName);
			aWarehouse.setEmail(emailPickUp);
			aWarehouse.setInActive(warehouseInactive);
			aWarehouse.setState(state);
			aWarehouse.setZip(zip);
			aWarehouse.setIsLaser(true);
			aWarehouse.setPickTicketInfo(additionaladdressLine);
			if(warehouseID!=null){
				aWarehouse.setPrWarehouseId(warehouseID);
				itsProductService.updateWarehouseDetails(aWarehouse);
				updated = true;
			}else{
				itsProductService.addWarehouse(aWarehouse);
				updated = true;
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> warehouseID==></b>"+warehouseID,"Inventory",e,session,therequest);
		}
		return updated;
	}

	/**
	 * Description: This method is used for retreiving the warehouse details.
	 * @param: cuso {@link Cuso}
	 * @return:  acuso {@link Cuso}
	 * @throws MessagingException 
	 * @throws IOException 
	 * @exception: JobException
	 * */
	
	@RequestMapping(value="/getPickUpAddress", method = RequestMethod.POST)
	public @ResponseBody Prwarehouse getPickUpAddress(@RequestParam(value="CusoID", required=false) Integer CusoID,HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException{
		Cuso aCuso = new Cuso();
		Prwarehouse aPrwarehouse = new Prwarehouse();
		try {
			if(CusoID != null){
				aCuso = jobService.getCusoObj(CusoID);
				aPrwarehouse = itsProductService.getWareHouseAddress(aCuso.getPrFromWarehouseId());
			}
			else
			{
				aPrwarehouse = itsProductService.getWareHouseAddress(1);
			}
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> CusoID==></b>"+CusoID,"Inventory",e,session,therequest);
		}
		return aPrwarehouse;
	}
	
	@RequestMapping(value="/getOrderPoints", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getOrderPoints(
			@RequestParam(value="wareHouseID", required=false) Integer waerhouseId,
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			HttpServletRequest therequest, HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException{
		itsLogger.info("Its Called for Get Order Points"+waerhouseId);
		CustomResponse aResponse = null;
		List<Prmaster> orderPointsList=null;
		try {
			if(searchData == null)
				searchData = "";
			
			if(waerhouseId != null){
				int aTotalCount =  itsProductService.getOrderPointsCount(waerhouseId, searchData, thePage, theRows, theSidx, theSord);
				int aFrom, aTo;
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				if (aTo > aTotalCount)
					aTo = aTotalCount;
				orderPointsList= itsProductService.getOrderPoints(waerhouseId, searchData, aFrom, aTo, thePage, theRows, theSidx, theSord);
				aResponse = new CustomResponse();
				aResponse.setRows(orderPointsList);
				aResponse.setRecords(String.valueOf(orderPointsList.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));
			}
	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> waerhouseId==></b>"+waerhouseId,"Inventory",e,session,therequest);
		}
		return aResponse;
	}
	
	@RequestMapping(value="/getSuggestedOrderPoints", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getSuggestedOrderPoints(
			@RequestParam(value="wareHouseID", required=false) Integer waerhouseId,
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			@RequestParam(value = "searchData", required = false) String searchData,
			HttpServletRequest therequest, HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException{
		itsLogger.info("Its Called for Get Order Points"+waerhouseId);
		CustomResponse aResponse = null;
		List<Prmaster> orderPointsList=null;
		try {
			
			if(searchData == null)
				searchData = "";
			
			if(waerhouseId != null){
				int aTotalCount =  itsProductService.getSuggestedOrderPointsCount(waerhouseId, searchData, thePage, theRows, theSidx, theSord);
				int aFrom, aTo;
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				if (aTo > aTotalCount)
					aTo = aTotalCount;
				orderPointsList= itsProductService.getSuggestedOrderPoints(waerhouseId,searchData, aFrom, aTo);
				aResponse = new CustomResponse();
				aResponse.setRows(orderPointsList);
				aResponse.setRecords(String.valueOf(orderPointsList.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));
			}
	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> waerhouseId==></b>"+waerhouseId,"Inventory",e,session,therequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value="/insertOrderPoints", method = RequestMethod.POST)
	public @ResponseBody int insertOrderPoints(
			@RequestParam(value="prMasterId", required=false) Integer prMasterId,
			@RequestParam(value="cellname", required=false) String cellname, //cellname = inventoryOrderPoint or inventoryOrderQuantity
			@RequestParam(value="value", required=false) BigDecimal value,
			@RequestParam(value="aWareHouseID", required=false) Integer aWareHouseID,
			HttpSession session,
			HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException{
		itsLogger.info("Insert Order Points "+prMasterId);
		//itsLogger.info("Insert Order Points "+inventoryOrderPoint);
		//itsLogger.info("Insert Order Points "+inventoryOrderQuantity);
		itsLogger.info("Insert Order Points "+aWareHouseID);
		 Prorderpoint aProrderpoint = new Prorderpoint();
		
		try {
			aProrderpoint.setPrMasterId(prMasterId);
			if(cellname.equalsIgnoreCase("inventoryOrderPoint"))
				aProrderpoint.setInventoryOrderPoint(value);
			if(cellname.equalsIgnoreCase("inventoryOrderQuantity"))
				aProrderpoint.setInventoryOrderQuantity(value);
			aProrderpoint.setPrWarehouseId(aWareHouseID);
			itsProductService.updateOrderPoints(aProrderpoint);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> prMasterId,aWareHouseID==></b>"+prMasterId+","+aWareHouseID,"Inventory",e,session,therequest);
		}
		return 1;
	}
	
	@RequestMapping(value="/insertInventoryCount", method = RequestMethod.POST)
	public @ResponseBody int insertInventoryCount(
			@RequestParam(value="prMasterId", required=false) Integer prMasterId,
			@RequestParam(value="inventoryCounted", required=false) BigDecimal inventoryCounted,
			@RequestParam(value="aWareHouseID", required=false) Integer aWareHouseID,
			HttpSession session,
			HttpServletRequest therequest, HttpServletResponse theResponse) throws IOException, MessagingException{
		itsLogger.info("Insert Order Points "+prMasterId);
		itsLogger.info("Insert Inventory Counted "+inventoryCounted);
		itsLogger.info("Insert WH ID "+aWareHouseID);
		 Prinventorycount aPrinventorycount = new Prinventorycount();
		
		try {
			aPrinventorycount.setPrMasterId(prMasterId);
			aPrinventorycount.setCountedOnHand(inventoryCounted);
			//itsProductService.insertCountInventory(aPrinventorycount,aWareHouseID);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> prMasterId,aWareHouseID==></b>"+prMasterId+","+aWareHouseID,"Inventory",e,session,therequest);
		}
		return 1;
	}

	
	@RequestMapping(value="/countInventory", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getcountInventory(
			@RequestParam(value="wareHouseID", required=false) Integer waerhouseId,
			@RequestParam(value="sortValue", required=false) Integer sortValue,
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpServletRequest therequest, HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException{
		itsLogger.info("CountInventory  WareHouseID: "+waerhouseId +"SortVal : "+sortValue);
		CustomResponse aResponse = null;
		List<Prmaster> aCountInventoryList=null;
		try {
			if(waerhouseId != null){
				int aTotalCount = itsProductService.getcountInventoryCount(waerhouseId);
				int aFrom, aTo;
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				if (aTo > aTotalCount)
					aTo = aTotalCount;
				aFrom  = -1;
				aTo = -1;
				aCountInventoryList= itsProductService.getcountInventory(waerhouseId, aFrom, aTo,sortValue);
				aResponse = new CustomResponse();
				aResponse.setRows(aCountInventoryList);
				aResponse.setRecords(String.valueOf(aCountInventoryList.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));
			}
	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> aWareHouseID==></b>"+waerhouseId,"Inventory",e,session,therequest);
		}
		return aResponse;
	}

	@RequestMapping(value = "/getInventoryCountPDF", method = RequestMethod.GET)
	public @ResponseBody
	void getInventoryCountPDF(
			@RequestParam(value = "prMasterId", required = false) Integer prMasterId,
			@RequestParam(value = "prWarehouseInventoryID", required = false) Integer prWarehouseInventoryID,
			HttpServletResponse theResponse, HttpServletRequest therequest,HttpSession session)
			throws IOException, MessagingException, SQLException {
		Connection connection = null;TsUserSetting	objtsusersettings=null;
		ConnectionProvider con = null;
		itsLogger.info("PrMaster ID " +prMasterId);
		itsLogger.info("prWarehouseInventoryID" +prWarehouseInventoryID);
		InputStream imageStream =null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML = therequest
					.getSession()
					.getServletContext()
					.getRealPath(
							"/resources/jasper_reports/inventoryDetails.jrxml");
			con = itspdfService.connectionForJasper();
			params.put("prMasterId", prMasterId);
			params.put("prWarehouseInventoryID", prWarehouseInventoryID);
			objtsusersettings=(TsUserSetting) session.getAttribute(SessionConstants.TSUSERSETTINGS);
			Blob blob =  objtsusersettings.getCompanyLogo();
			imageStream =blob.getBinaryStream();
			//BufferedImage image = ImageIO.read(imageStream);
			params.put("HeaderImage", imageStream);
			params.put("HeaderText",((objtsusersettings.getHeaderText().replaceAll("`and`nbsp;", " ")).replaceAll("`", "")).replaceAll("amp;"," "));
			
			ServletOutputStream out = theResponse.getOutputStream();
			theResponse.setHeader("Content-Disposition", "inline");
			theResponse.setContentType("application/pdf");
			connection = con.getConnection();
			JasperReport report;
			report = JasperCompileManager.compileReport(path_JRXML);
			JasperPrint print = JasperFillManager.fillReport(report, params,
					connection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, baos);
			out.write(baos.toByteArray());
			out.flush();
			out.close();
		} catch (Exception e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b> prMasterId,prWarehouseInventoryID==></b>"+prMasterId+","+prWarehouseInventoryID,"Inventory",e,session,therequest);
		}
		finally{
			if(imageStream!=null){
				imageStream.close();
			}
			if(con!=null){
			con.closeConnection(connection);
			con=null;
			}
		}
	}
	
	@RequestMapping(value = "/getInventoryCountCSV", method = RequestMethod.GET)
	public @ResponseBody
	void getInventoryCountCSV(
			@RequestParam(value = "prWarehouseInventoryID", required = false) Integer prWarehouseInventoryID,
			@RequestParam(value = "docType", required = false) Integer docType,
			HttpServletResponse theResponse, HttpServletRequest request,HttpSession session)
			throws IOException, MessagingException, SQLException {
			Connection connection = null;
			ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			System.out.println("insid the printCSVCompanyContacts");
			if(docType==1){
				path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/InventoryCount.jrxml");
				filename="InventoryCount.pdf";
			}
			else{
				path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/InventoryCountCSV.jrxml");
				//path_JRXML = request.getSession().getServletContext().getRealPath("/resources/jasper_reports/InventoryCount.jrxml");
				filename="InventoryCount.csv";
			}
			Prwarehouse aPrwarehouse = new Prwarehouse();
			try {
				aPrwarehouse = itsInventoryService.getPrwarehouse(prWarehouseInventoryID);
			} catch (InventoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			con = itspdfService.connectionForJasper();
			params.put("warehouseId", prWarehouseInventoryID);
			params.put("warehouseName", aPrwarehouse.getSearchName());
			
			
			connection = con.getConnection();
			if(docType==1){
				ReportService.ReportCall(theResponse,params,"pdf",path_JRXML,filename,connection);
			}else{
				ReportService.ReportCall(theResponse,params,"xls",path_JRXML,filename,connection);
			}
			

		} catch (SQLException e) {
			itsLogger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
			sendTransactionException("<b>Tracking ID:</b> printCSVCompanyContacts","Company",e,session,request);
		}finally{
			try {
				if(con!=null){
					con.closeConnection(connection);
					con=null;
					}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@RequestMapping(value="/transferInventoryGrid", method = RequestMethod.POST)
	public @ResponseBody CustomResponse geAadjustmentData(
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpServletRequest therequest, HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException{
			itsLogger.info("transferInventoryGrid");
			CustomResponse aResponse = null;
			List<PrwarehouseTransfer> prTransferList=null;
		try {
			
				int aTotalCount = 0; //itsProductService.getOrderPointsCount(waerhouseId);
				int aFrom, aTo;
				aTo = (theRows * thePage);
				aFrom = aTo - theRows;
				if (aTo > aTotalCount)aTo = aTotalCount;
				prTransferList= itsProductService.getprTransferData(0);
				aResponse = new CustomResponse();
				aResponse.setRows(prTransferList);
				aResponse.setRecords(String.valueOf(prTransferList.size()));
				aResponse.setPage(thePage);
				aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> methodName==></b>transferInventoryGrid","Inventory",e,session,therequest);
		}
		return aResponse;
	}
	
	public List<Coaccount> getItsCoAccount() {
		return itsCoAccount;
	}

	public void setItsCoAccount(List<Coaccount> itsCoAccount) {
		this.itsCoAccount = itsCoAccount;
	}
	
	@RequestMapping(value = "/getAdjustmentDetails", method = RequestMethod.POST)
	public  @ResponseBody Map<String, List<?>> getAdjustmentDetails(
			@RequestParam(value="transferID") Integer transferID,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		Map<String, List<?>> map = new HashMap<String,List<?>>();
		
		try {
			//List<Prwarehouse> wareHouses = itsProductService.getWarehouses();
			List<PrwarehouseTransfer> transferList = itsProductService.getprTransferData(transferID);
			//setItsCoAccount((List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown());
			//List<Coaccount> coAccounts = getItsCoAccount();
			//System.out.println(" coAccounts.size() :: "+coAccounts.size());
			map.put("transferList",transferList);
			//map.put("Warehouses", wareHouses);
			//map.put("GLAccounts", coAccounts);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> transferID==></b>"+transferID,"Inventory",e,session,therequest);
		}
		return map;		
	
	}
	
	@RequestMapping(value = "/getAdjustmentProductNo", method = RequestMethod.GET)
	public  @ResponseBody List<?> getAdjustmentProductNos(@RequestParam("term") String theProductText, @RequestParam(value="wareHouseId") Integer wareHouseId,
			@RequestParam(value="transferID") Integer transferID,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException
	{
		ArrayList<AutoCompleteBean> aPrTransferDetailsID = new ArrayList<AutoCompleteBean>();
		
		try {
			aPrTransferDetailsID = (ArrayList<AutoCompleteBean>) itsProductService.getprTransferNos(transferID, theProductText, wareHouseId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> transferID,wareHouseId==></b>"+transferID+","+wareHouseId,"Inventory",e,session,therequest);
		}
		return aPrTransferDetailsID;	
	}
	
	@RequestMapping(value = "/getAdjustmentProductInfoDetails", method = RequestMethod.GET)
	public  @ResponseBody String getAdjustmentProductInfoDetails(@RequestParam("productNo") Integer productNo,
			@RequestParam(value="wareHouseId") Integer wareHouseId, @RequestParam(value="transferID") Integer transferID,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException
	{
		String str = "";
		
		try {
			str = itsProductService.getAdjustmentPrInfoDetails(productNo, transferID, wareHouseId);
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> transferID,wareHouseId==></b>"+transferID+","+wareHouseId,"Inventory",e,session,therequest);
		}
		return str;	
	}
	
	/*@RequestMapping(value="/getTransferdetails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getTransferdetails(
			@RequestParam(value = "transferID", required = true) Integer transferID,
			@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,
			HttpServletRequest theRequest, HttpServletResponse theResponse){
			itsLogger.info("getTransferdetails Loaded :"+transferID);
			CustomResponse aResponse = null;
		try {
			
				int aTotalCount = 0; //itsProductService.getOrderPointsCount(waerhouseId);
				int aFrom, aTo;
				//aTo = (theRows * thePage);
				//aFrom = aTo - theRows;
				//if (aTo > aTotalCount)aTo = aTotalCount;
				List<Prtransferdetail> aPrTransferDetails = itsProductService.getprTransferDetails(transferID);
				System.out.println(" aPrTransferDetails.size() :: "+aPrTransferDetails.size());
				aResponse = new CustomResponse();
				aResponse.setRows(aPrTransferDetails);
				
				aResponse.setRows(aPrTransferDetails);
				aResponse.setRecords(String.valueOf(aPrTransferDetails.size()));
				//aResponse.setPage(thePage);
				//aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
		}
		return aResponse;
	}*/
	
	@RequestMapping(value="/getTransferdetails", method = RequestMethod.POST)
	public @ResponseBody CustomResponse getTransferdetails(
			@RequestParam(value = "transferID", required = true) Integer transferID,
			/*@RequestParam(value = "page", required = false) Integer thePage,
			@RequestParam(value = "rows", required = false) Integer theRows,
			@RequestParam(value = "sidx", required = false) String theSidx,
			@RequestParam(value = "sord", required = false) String theSord,*/
			HttpServletRequest therequest, HttpServletResponse theResponse,HttpSession session) throws IOException, MessagingException{
			itsLogger.info("getTransferdetails Loaded :"+transferID);
			CustomResponse aResponse = null;
		try {
			
				int aTotalCount = 0; //itsProductService.getOrderPointsCount(waerhouseId);
				int aFrom, aTo;
				//aTo = (theRows * thePage);
				//aFrom = aTo - theRows;
				//if (aTo > aTotalCount)aTo = aTotalCount;
				List<Prwhtransferdetail> aPrTransferDetails = itsProductService.getprTransferDetails(transferID);
				System.out.println(" aPrTransferDetails.size() :: "+aPrTransferDetails.size());
				aResponse = new CustomResponse();
				aResponse.setRows(aPrTransferDetails);
				
				aResponse.setRows(aPrTransferDetails);
				aResponse.setRecords(String.valueOf(aPrTransferDetails.size()));
				//aResponse.setPage(thePage);
				//aResponse.setTotal((int) Math.ceil((double) aTotalCount/ (double) theRows));	
		
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> transferID==></b>"+transferID,"Inventory",e,session,therequest);
		}
		return aResponse;
	}
	
	
	@RequestMapping(value = "/getAdjustmentCOGInfoDetails", method = RequestMethod.GET)
	public  @ResponseBody String getAdjustmentCOGInfoDetails(@RequestParam(value="wareHouseId") Integer wareHouseId,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException
	{
		Prwarehouse aprwh=null;
		String successSatus = "Success";
		
		try {
			aprwh=itsProductService.getWarehouseDetail(wareHouseId);
			if(aprwh.getAdjustCogAccountID()==null)
				successSatus = "Failed";
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			sendTransactionException("<b> wareHouseId==></b>"+wareHouseId+",AdjcogAccountID"+aprwh.getAdjustCogAccountID(),"Inventory",e,session,therequest);
		}
		return successSatus;	
	}
	
	
	@RequestMapping(value = "/createInventoryAdjustment", method = RequestMethod.POST)
	public @ResponseBody boolean createInventoryAdjustment(@RequestParam(value = "gridData", required = false) String gridData,
													@RequestParam(value = "transferId", required = false) Integer transferId,
													@RequestParam(value = "transferDate", required = false) Date transferDateID,
													@RequestParam(value = "warehouseList", required = false) Integer warehouseListID,
													@RequestParam(value = "reference", required = false) String reference,
													@RequestParam(value = "glAccountListID", required = false) Integer glAccountListID,
													@RequestParam(value = "reasonCodeID", required = false) String reasonCode,
													HttpSession session,HttpServletResponse theResponse,HttpServletRequest therequest) throws IOException, ProductException, ParseException, BankingException, CompanyException, InventoryException, MessagingException {
				JsonParser parser = new JsonParser();
				boolean result=false;
				boolean resultfordateupdate=false;
				PrwarehouseTransfer aprwhtf=null;
				Prwarehouse aprwh=null;
				BigDecimal cdtotal=new BigDecimal(0.00);
				System.out.println("transferId "+transferId);
				try{
				UserBean aUserBean;
				aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
				
				if(warehouseListID>0)
					aprwh=itsProductService.getWarehouseDetail(warehouseListID);
				System.out.println("AdjustCogAccountID "+aprwh.getAdjustCogAccountID());
				
				if(transferId==null)
	               transferId=0;
					
				if(transferId==0){
			        aprwhtf=new PrwarehouseTransfer();
			        aprwhtf.setScreenno(2);
					aprwhtf.setTransferDate(transferDateID);
					aprwhtf.setPrFromWarehouseId(warehouseListID);
					aprwhtf.setDesc(reference);
					aprwhtf.setCoAccountID(aprwh.getCoAccountIdasset());
					aprwhtf.setReasonCode(reasonCode);
					
					if(aprwh!=null)
						aprwhtf.setAdjustCogAccountID(aprwh.getAdjustCogAccountID());	
					aprwhtf= itsProductService.createInventoryAdjustment(aprwhtf);
					
				}else{
					aprwhtf= itsProductService.getPrwarehouseTransfer(transferId);
					
					// Edited by: Leo Reason: update 
					if(aprwhtf.getTransferDate().compareTo(transferDateID)!=0 || aprwhtf.getPrFromWarehouseId()!=warehouseListID)
						resultfordateupdate=true;
					
					aprwhtf.setScreenno(2);
					aprwhtf.setTransferDate(transferDateID);
					aprwhtf.setPrFromWarehouseId(warehouseListID);
					aprwhtf.setDesc(reference);
					aprwhtf.setCoAccountID(aprwh.getCoAccountIdasset());
					aprwhtf.setReasonCode(reasonCode);
					if(aprwhtf.getAdjustCogAccountID()==null)
						aprwhtf.setAdjustCogAccountID(aprwh.getAdjustCogAccountID());
					
					itsProductService.updateInventoryAdjustment(aprwhtf);
				}
				System.out.println("AdjustCogAccountID "+aprwhtf.getAdjustCogAccountID());
				System.out.println("transferId "+aprwhtf.getPrTransferId());
				System.out.println("gridData "+gridData);
				
				if ( gridData!=null) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					if(array.size()==0)
						result=true;
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						Prwhtransferdetail aPrwhtransferdetail=new Prwhtransferdetail();
						
						aPrwhtransferdetail.setPrTransferDetailId(JobUtil.ConvertintoInteger(obj.get("prTransferDetailId").getAsString())) ;  
						aPrwhtransferdetail.setPrTransferId(aprwhtf.getPrTransferId());
						aPrwhtransferdetail.setPrMasterId(obj.get("prMasterId").getAsInt());
						aPrwhtransferdetail.setItemCode(obj.get("itemCode").getAsString());
						aPrwhtransferdetail.setDescription(obj.get("description").getAsString());
						aPrwhtransferdetail.setQuantityAvailable(obj.get("quantityAvailable").getAsBigDecimal());
						aPrwhtransferdetail.setQuantityTransfered(obj.get("quantityTransfered").getAsBigDecimal());
						aPrwhtransferdetail.setDifference(obj.get("difference").getAsBigDecimal());
						aPrwhtransferdetail.setInventoryCost(obj.get("inventoryCost").getAsBigDecimal());
						aPrwhtransferdetail.setUserId(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
						aPrwhtransferdetail.setUserName(((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
						cdtotal=cdtotal.add(obj.get("difference").getAsBigDecimal().multiply(obj.get("inventoryCost").getAsBigDecimal()));
						System.out.println("cdtotal "+cdtotal);
						result=itsProductService.addPrwhtransferdetail(aPrwhtransferdetail,warehouseListID);
					}
					}
				
				
				if(transferId == 0)
				{
				itsProductService.saveGLTransaction( aprwhtf,cdtotal,false,aUserBean.getFullName());
				}
				else
				{
					if(resultfordateupdate || result)
					itsProductService.saveGLTransaction( aprwhtf,cdtotal,true,aUserBean.getFullName());
				}
				
			
				}catch(Exception  e){
					sendTransactionException("<b> transferId,warehouseListID==></b>"+transferId+","+warehouseListID,"Inventory",e,session,therequest);
				}
				
		return true;
	}
	
	@RequestMapping(value = "/deleteInventoryAdjustment", method = RequestMethod.POST)
	public @ResponseBody void deleteInventoryAdjustment(@RequestParam(value = "gridData", required = false) String gridData,
													@RequestParam(value = "warehouseList", required = false) Integer warehouseListID,
													HttpServletResponse theResponse,HttpSession session,HttpServletRequest therequest) throws IOException, ProductException, ParseException, BankingException, CompanyException, MessagingException {
				
			try{
			JsonParser parser = new JsonParser();
				
				System.out.println("gridData "+gridData);
				
				gridData="["+gridData+"]";
				
				if ( gridData!=null) {
					JsonElement ele = parser.parse(gridData);
					JsonArray array = ele.getAsJsonArray();
					System.out.println("array length==>"+array.size());
					for (JsonElement ele1 : array) {
						JsonObject obj = ele1.getAsJsonObject();
						Prwhtransferdetail aPrwhtransferdetail=new Prwhtransferdetail();
						
						aPrwhtransferdetail.setPrTransferDetailId(JobUtil.ConvertintoInteger(obj.get("prTransferDetailId").getAsString())) ;  
						aPrwhtransferdetail.setPrMasterId(obj.get("prMasterId").getAsInt());
						aPrwhtransferdetail.setQuantityTransfered(obj.get("quantityTransfered").getAsBigDecimal());
						aPrwhtransferdetail.setUserId(((UserBean)session.getAttribute(SessionConstants.USER)).getUserId());
						aPrwhtransferdetail.setUserName(((UserBean)session.getAttribute(SessionConstants.USER)).getUserName());
						itsProductService.deletePrwhtransferdetail(aPrwhtransferdetail,warehouseListID);
					}
					}
			}catch(Exception e){
				
				sendTransactionException("<b> warehouseListID==></b>"+warehouseListID+","+warehouseListID,"Inventory",e,session,therequest);
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
	
	
	@RequestMapping(value = "/SaveInventoryCount", method = RequestMethod.POST)
	public @ResponseBody
	String SaveInventoryCount(
			@RequestParam(value = "warehouseListID",required=false) Integer warehouseListID,
			@RequestParam(value = "gridData",required=false) String gridData,
			HttpServletRequest therequest,HttpSession session) throws IOException, MessagingException {
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		List<Prinventorycount> objPrinventorycount=null;
		try {
			
			JsonParser parser = new JsonParser();
			if ( gridData!=null) {
				objPrinventorycount=new ArrayList<Prinventorycount>();
				itsLogger.info("gridData"+gridData);
				JsonElement ele = parser.parse(gridData);
				JsonArray array = ele.getAsJsonArray();
				itsLogger.info("array length==>"+array.size());
				for (JsonElement ele1 : array) {
					Prinventorycount aPrinventorycount=new Prinventorycount();
					JsonObject obj = ele1.getAsJsonObject();
					Integer prWarehouseInventoryID=obj.get("prWarehouseInventoryID").getAsInt();
					Integer prMasterId=obj.get("prMasterId").getAsInt();
					String Itemcode=obj.get("itemCode").getAsString();
					String description=obj.get("description").getAsString();
					String primaryVendorName=obj.get("primaryVendorName").getAsString();
					Integer prCategoryId=obj.get("prCategoryId").getAsInt();
					BigDecimal inventoryOnHand=obj.get("inventoryOnHand").getAsBigDecimal();
					//BigDecimal inventoryCounted=obj.get("inventoryCounted").getAsBigDecimal();
					BigDecimal Counted=null;
					if(obj.get("Counted")!=null){
						String val=obj.get("Counted").toString().replaceAll("\"", "");
						if(!val.equals("")){
							Counted=JobUtil.ConvertintoBigDecimal(val);
						}
						
					}
					
					if(Counted!=null){
						aPrinventorycount.setPrMasterId(prMasterId);
						aPrinventorycount.setPrwarehouseInventoryID(prWarehouseInventoryID);
						aPrinventorycount.setCountedOnHand(Counted);
						objPrinventorycount.add(aPrinventorycount);
					}
					
				}
				if(objPrinventorycount!=null)
				 itsProductService.insertCountInventory(objPrinventorycount,warehouseListID,aUserBean.getUserId(),aUserBean.getUserName());
			}
			
		} catch (Exception e) {
			itsLogger.error(e.getMessage(), e);
			Transactionmonitor transObj =new Transactionmonitor();
			 SendQuoteMail sendMail = new SendQuoteMail();
			 transObj.setHeadermsg("Exception Log << "+e.getMessage()+" >>");
			 transObj.setTrackingId("<b>Save Inventory Count==></b>"+warehouseListID);
			 transObj.setTimetotriggerd(new Date());
			 transObj.setJobStatus("Inventory");
			 transObj.setUsername(aUserBean.getFullName()+"["+aUserBean.getUserId()+"]");
			 StringWriter errors = new StringWriter();
			 e.printStackTrace(new PrintWriter(errors));
			 transObj.setDescription("Message :: " + errors.toString());
			 sendMail.sendTransactionInfo(transObj,therequest);
		}
		return null;

	}
}
