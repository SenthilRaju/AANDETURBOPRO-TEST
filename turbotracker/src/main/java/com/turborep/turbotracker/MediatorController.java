package com.turborep.turbotracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.connection.ConnectionProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.turborep.turbotracker.Inventory.Exception.InventoryException;
import com.turborep.turbotracker.Inventory.service.InventoryService;
import com.turborep.turbotracker.Rolodex.service.RolodexService;
import com.turborep.turbotracker.banking.dao.MoAccount;
import com.turborep.turbotracker.banking.dao.Motransaction;
import com.turborep.turbotracker.banking.exception.BankingException;
import com.turborep.turbotracker.banking.service.BankingService;
import com.turborep.turbotracker.banking.service.GltransactionService;
import com.turborep.turbotracker.company.Exception.CompanyException;
import com.turborep.turbotracker.company.dao.ChSegments;
import com.turborep.turbotracker.company.dao.CoAccountBean;
import com.turborep.turbotracker.company.dao.CoTaxTerritory;
import com.turborep.turbotracker.company.dao.Coaccount;
import com.turborep.turbotracker.company.dao.Codivision;
import com.turborep.turbotracker.company.dao.Rxaddress;
import com.turborep.turbotracker.company.dao.Rxcontact;
import com.turborep.turbotracker.company.service.AccountingCyclesService;
import com.turborep.turbotracker.company.service.ChartOfAccountsService;
import com.turborep.turbotracker.company.service.CompanyService;
import com.turborep.turbotracker.company.service.TaxTerritoriesService;
import com.turborep.turbotracker.customer.dao.CuMasterType;
import com.turborep.turbotracker.customer.dao.CuTerms;
import com.turborep.turbotracker.customer.dao.Cumaster;
import com.turborep.turbotracker.customer.dao.Cureceipttype;
import com.turborep.turbotracker.customer.dao.Cuso;
import com.turborep.turbotracker.customer.dao.CustomerPaymentBean;
import com.turborep.turbotracker.customer.dao.RxCustomerTabViewBean;
import com.turborep.turbotracker.customer.exception.CustomerException;
import com.turborep.turbotracker.customer.service.CustomerService;
import com.turborep.turbotracker.employee.dao.Emmaster;
import com.turborep.turbotracker.employee.dao.RxMasterCategoryView;
import com.turborep.turbotracker.employee.dao.Rxmaster;
import com.turborep.turbotracker.employee.dao.Rxmastercategory2;
import com.turborep.turbotracker.employee.exception.EmployeeException;
import com.turborep.turbotracker.employee.service.EmployeeServiceInterface;
import com.turborep.turbotracker.job.dao.JoCustPO;
import com.turborep.turbotracker.job.dao.JobCustomerBean;
import com.turborep.turbotracker.job.dao.JobHistory;
import com.turborep.turbotracker.job.dao.Jomaster;
import com.turborep.turbotracker.job.dao.Joquotecolumn;
import com.turborep.turbotracker.job.dao.Josubmittalheader;
import com.turborep.turbotracker.job.dao.jocategory;
import com.turborep.turbotracker.job.exception.JobException;
import com.turborep.turbotracker.job.service.JobService;
import com.turborep.turbotracker.job.service.PDFService;
import com.turborep.turbotracker.json.AutoCompleteBean;
import com.turborep.turbotracker.json.CustomResponse;
import com.turborep.turbotracker.product.dao.PrCategory;
import com.turborep.turbotracker.product.dao.PrDepartment;
import com.turborep.turbotracker.product.dao.Prmaster;
import com.turborep.turbotracker.product.dao.Prwarehouse;
import com.turborep.turbotracker.product.dao.PrwarehouseTransfer;
import com.turborep.turbotracker.product.dao.Prwarehouseinventory;
import com.turborep.turbotracker.product.exception.ProductException;
import com.turborep.turbotracker.product.service.ProductService;
import com.turborep.turbotracker.quickbooks.service.QuickBooksService;
import com.turborep.turbotracker.sales.service.SalesService;
import com.turborep.turbotracker.system.dao.SysAccountLinkage;
import com.turborep.turbotracker.system.dao.SysUserDefault;
import com.turborep.turbotracker.system.dao.Sysassignment;
import com.turborep.turbotracker.system.dao.Sysinfo;
import com.turborep.turbotracker.system.dao.Sysprivilege;
import com.turborep.turbotracker.system.dao.Sysvariable;
import com.turborep.turbotracker.system.exception.SysException;
import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.AccessProcedureConstant;
import com.turborep.turbotracker.user.dao.TsAccessModule;
import com.turborep.turbotracker.user.dao.TsUserGroup;
import com.turborep.turbotracker.user.dao.TsUserGroupLink;
import com.turborep.turbotracker.user.dao.TsUserLogin;
import com.turborep.turbotracker.user.dao.TsUserSetting;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.user.service.UserServiceImpl;
import com.turborep.turbotracker.util.ReportService;
import com.turborep.turbotracker.util.ReportUtils;
import com.turborep.turbotracker.util.SessionConstants;
import com.turborep.turbotracker.vendor.dao.Vefreightcharges;
import com.turborep.turbotracker.vendor.dao.Veshipvia;

@Controller
@RequestMapping("/")
public class MediatorController {
	
	private Logger LOGGER = Logger.getLogger(MediatorController.class);

	@Resource(name = "jobService")
	private JobService jobService;

	@Resource(name = "salesServices")
	private SalesService salesServices;

	@Resource(name = "companyService")
	private CompanyService companyService;

	@Resource(name = "customerService")
	private CustomerService cuMasterService;

	@Resource(name = "userLoginService")
	private UserService userService;

	@Resource(name = "rolodexService")
	private RolodexService itsCustomerService;

	@Resource(name = "inventoryService")
	private InventoryService itsInventoryService;

	@Resource(name = "bankingService")
	private BankingService itsBankingService;

	@Resource(name = "employeeService")
	private EmployeeServiceInterface itsEmployeeServiceInter;

	@Resource(name = "chartOfAccountsService")
	private ChartOfAccountsService chartOfAccountsService;

	@Resource(name = "companyService")
	private CompanyService itsCompanyService;

	@Resource(name = "taxTerritoriesService")
	private TaxTerritoriesService itstaxTerritoriesService;

	@Resource(name = "customerService")
	private CustomerService itsCustomerPaymentsService;

	@Resource(name = "qbService")
	private QuickBooksService itsQuickBooksService;

	@Resource(name = "bankingService")
	private BankingService bankingAccountsService;

	@Resource(name = "rolodexService")
	private RolodexService itsEmployeeService;
	
	@Resource(name="rolodexService")
	private RolodexService itsRolodexService;
	
	@Resource(name = "productService")
	private ProductService itsProductService;

	@Resource(name="sysService")
	private SysService sysservice;
	
	@Resource(name="taxTerritoriesService")
	private TaxTerritoriesService taxTerritoriesService;
	
	@Resource(name = "pdfService")
	private PDFService itspdfService;
	
	@Resource(name="sessionFactory")
	private SessionFactory itsSessionFactory;
	
	@Resource(name="accountingCyclesService")
	private AccountingCyclesService accountingCyclesService;
	
	@Resource(name="gltransactionService")
	private GltransactionService itsGltransactionService;
	
	private List<PrDepartment> itsPrDepartment;

	private List<PrCategory> itsPrcategory;

	private Cumaster customerRecord;

	private String tableNameUserLogin = "tsUserLogin";

	private Jomaster jomasterDetails;

	private Rxmaster rxMasterDetails;

	private Emmaster commissionsDetails;

	private List<Vefreightcharges> itsFreightCharges;

	private List<Veshipvia> itsVeshipvia;

	private List<Prwarehouse> itsPrwarehouses;

	private List<Coaccount> itsCoAccount;

	private List<TsAccessModule> itsAccessModule;

	private List<MoAccount> itsBankingList;

	private List<Codivision> itsDivision;

	// private List<?> rxAddressDetails;

	private Rxaddress rxAddressDetails;

	private Josubmittalheader joSubmittalDetails;

	private List<JoCustPO> itsPODetails;

	private List<CuTerms> itsTerms;

	private List<CoTaxTerritory> itstaxTerritory;

	private List<Rxmaster> customers;

	private String[] printers;

	protected static Logger logger = Logger.getLogger("controller");

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String getDefaultHomePage(ModelMap modal, HttpServletRequest request) {
		String aPage = "home";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "welcome", method = RequestMethod.GET)
	public String getWelcomePage() {
		return "welcome";
	}
	
	@RequestMapping(value = "authError", method = RequestMethod.GET)
	public String getAuthErrorPage() {
		return "Noauthendication";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getloginPage(ModelMap modal, HttpServletRequest request)  {
	
			ResourceBundle rb = ResourceBundle.getBundle("version");
			String appversion=rb.getString("app.jdbc.appversion");
			String dbversion=rb.getString("app.jdbc.dbversion");
			boolean returnvalue=itsCompanyService.getdbVersion(appversion,dbversion);
			/*
			 * if (!SessionConstants.checkSessionExist(request)) { aPage =
			 * "welcome"; }
			 */
		
		modal.addAttribute("VersionChanges",returnvalue);
		modal.addAttribute("user-message", "User Name Or Password are Invalid.");
		return "login";
	}

	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String getHomePage(ModelMap modal, HttpServletRequest request,
			HttpSession theSession, HttpServletResponse theResponse,
			ModelMap theModel) throws IOException, UserException {
		String aPage = "home";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		try {
			setDivision((List<Codivision>)itsCompanyService.getCompanyDivisions());
			
			List<String> addlist=new ArrayList<String>();
			addlist.add("CheckcreditlimitinQuickBook");
			addlist.add("UseCustomersCreditLimitwhencreatingJobs");
			
			ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
			int i=0;
			for(Sysvariable theSysvariable:sysvariablelist){
				//Inventory Settings
				if(i==0){
					logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
					theModel.addAttribute("cusCreLiminQuickBookYes", theSysvariable.getValueLong());
				}
				if(i==1)
				{
					logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
					theModel.addAttribute("chkUserCustomerCreditYN", theSysvariable.getValueLong());
				}
				i++;
			}
			theModel.addAttribute("divisions", getDivision());
			boolean customers=getSysPrivilage ("Customers",0,theSession,theResponse);
			theSession.setAttribute("customerGroupPermission", customers);
			boolean	vendors=getSysPrivilage ("Vendors",0,theSession,theResponse);
			theSession.setAttribute("vendorGroupPermission", vendors);
			boolean	employees=getSysPrivilage ("Employees",0,theSession,theResponse);
			theSession.setAttribute("employeeGroupPermission", employees);
			/*boolean	sales=getSysPrivilage ("Sales",0,theSession,theResponse);
			theModel.addAttribute("salesGroupPermission", sales);
			boolean	project=getSysPrivilage ("Project",0,theSession,theResponse);
			theModel.addAttribute("projectGroupPermission", project);*/
			boolean	banking=getSysPrivilage ("Banking",0,theSession,theResponse);
			theSession.setAttribute("bankingGroupPermission", banking);
			boolean	inventory=getSysPrivilage ("Inventory",0,theSession,theResponse);
			theSession.setAttribute("inventoryGroupPermission", inventory);
			
			boolean	Main=getSysPrivilage ("Main",0,theSession,theResponse);
			theSession.setAttribute("MainGroupPermission", Main);
			boolean	Quotes=getSysPrivilage ("Quotes",0,theSession,theResponse);
			theSession.setAttribute("QuoteGroupPermission", Quotes);
			boolean	Submittal=getSysPrivilage ("Submittal",0,theSession,theResponse);
			theSession.setAttribute("SubmittalGroupPermission", Submittal);
			boolean	Credit=getSysPrivilage ("Credit",0,theSession,theResponse);
			theSession.setAttribute("CreditGroupPermission", Credit);
			boolean	Release=getSysPrivilage ("Release",0,theSession,theResponse);
			theSession.setAttribute("ReleaseGroupPermission", Release);
			boolean	Financial=getSysPrivilage ("Financial",0,theSession,theResponse);
			theSession.setAttribute("FinancialGroupPermission", Financial);
			boolean	Journal=getSysPrivilage ("Journal",0,theSession,theResponse);
			theSession.setAttribute("JournalGroupPermission", Journal);
			System.out.println("QuoteGroupPermission"+Quotes);
			
			
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	/**
	 * Retrieves the JSP page that contains our JqGrid
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getUsersPage(HttpServletRequest request) {
		String aPage = "users";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	
	@RequestMapping(value = "/drillDown", method = RequestMethod.GET)
	public String drillDown(ModelMap modal, HttpServletRequest request) {
		String aPage = "finance/drillDown";
		/*
		 * if (!SessionConstants.checkSessionExist(request)) { aPage =
		 * "welcome"; }
		 */

		return aPage;
	}
	
	@RequestMapping(value = "/recieveInventory", method = RequestMethod.GET)
	public String getRecieveInventory(ModelMap modal, HttpServletRequest request) {
		String aPage = "inventory/recieveInventory";
		/*
		 * if (!SessionConstants.checkSessionExist(request)) { aPage =
		 * "welcome"; }
		 */

		return aPage;
	}

	@RequestMapping(value = "/userlist", method = RequestMethod.GET)
	public String getUserlistPage(ModelMap model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String aPage = "userlist";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		if (aUserBean != null) {
			try {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
			} catch (UserException e) {
				logger.error(e.getMessage(), e);
				response.sendError(501, e.getMessage());
			}
			model.addAttribute("userSysAdmin",
					aUserLogin.getSystemAdministrator());
			model.addAttribute("userDetails", aUserLogin);
		}
		return aPage;
	}

	@RequestMapping(value = "/taxTerritoryList", method = RequestMethod.GET)
	public String getTaxTerritoryListPage(ModelMap model, HttpSession session,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String aPage = "taxTerritoryList";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String getSettingsPage(HttpServletRequest request, ModelMap model,
			HttpSession session, HttpServletResponse response)
			throws IOException, JobException {
		String aPage = "settings";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserSetting aUserLoginSetting = null;
		UserBean aUserBean;
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		jocategory jocategorylist=null;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			sysinfo=sysservice.getRoldexCategories(1);
			sysassignment=sysservice.getCustomerCategories();
			jocategorylist=sysservice.getJobCategories();
			Joquotecolumn Joquotecolumnlst=sysservice.getjoQuoteColumn();
			if (aUserBean != null) {
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				model.addAttribute("userLoginSettings", aUserLoginSetting);
				String aheaderText = aUserLoginSetting.getHeaderText();
				String aTerms = aUserLoginSetting.getTerms();
				String asofootertext = aUserLoginSetting.getSoFooterText();
				
				/*
				 * String aHeaderTxt = "("+aheaderText+")"; String aTermsTxt =
				 * "("+aTerms+")";
				 */
				String aHeaderTextReplace = aheaderText.replaceAll("`and`amp;",
						"&");
				String aTermsTextReplace = aTerms.replaceAll("`and`amp;", "&");
				
				String aHeaderSpaceReplace = aHeaderTextReplace.replaceAll(
						"`and`nbsp;", " ");
				String aTermsSpaceReplace = aTermsTextReplace.replaceAll(
						"`and`nbsp;", " ");
			
				String footernote = aUserLoginSetting.getQuotesFooter();
				model.addAttribute("headerTextSettings", aHeaderSpaceReplace);
				model.addAttribute("termsTextSettings", aTermsSpaceReplace);
				model.addAttribute("quoteFooterNotes", footernote);
				model.addAttribute("soFooterText", asofootertext);
				
				model.addAttribute("chsegments", userService.getchSegments());
				setItsPrwarehouses((List<Prwarehouse>) jobService
						.getWareHouse());
				List<?> aCustomer = itsEmployeeService.getEmployees(0, 1000, 0);
				/*Getting rolodox categories  from sysinfo table*/
				if(sysinfo!=null){
				model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
				model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
				model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
				model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
				model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
				model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
				model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
				model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
				model.addAttribute("prPriceLevel0", sysinfo.getPrPriceLevel0());
				model.addAttribute("prPriceLevel1", sysinfo.getPrPriceLevel1());
				model.addAttribute("prPriceLevel2", sysinfo.getPrPriceLevel2());
				model.addAttribute("prPriceLevel3", sysinfo.getPrPriceLevel3());
				model.addAttribute("prPriceLevel4", sysinfo.getPrPriceLevel4());
				model.addAttribute("prPriceLevel5", sysinfo.getPrPriceLevel5());
				model.addAttribute("prPriceLevel6", sysinfo.getPrPriceLevel6());
				}
				if(sysassignment!=null){
					model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
					model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
					model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
					model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
					model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
					model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
					model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
					model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
					model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
					model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());
				}
				
				//Job Categories
				
				if(jocategorylist!=null){
					model.addAttribute("JobCategory1", jocategorylist.getCategory1desc());
					model.addAttribute("JobCategory2",jocategorylist.getCategory2desc());
					model.addAttribute("JobCategory3", jocategorylist.getCategory3desc());
					model.addAttribute("JobCategory4", jocategorylist.getCategory4desc());
					model.addAttribute("JobCategory5", jocategorylist.getCategory5desc());
					model.addAttribute("JobCategory6", jocategorylist.getCategory6desc());
					model.addAttribute("JobCategory7", jocategorylist.getCategory7desc());
					model.addAttribute("JobCategoryId1",jocategorylist.getJoCategory1ID());
					model.addAttribute("JobCategoryId2", jocategorylist.getJoCategory2ID());
					model.addAttribute("JobCategoryId3", jocategorylist.getJoCategory3ID());
					model.addAttribute("JobCategoryId4", jocategorylist.getJoCategory4ID());
					model.addAttribute("JobCategoryId5", jocategorylist.getJoCategory5ID());
					model.addAttribute("JobCategoryId6", jocategorylist.getJoCategory6ID());
					model.addAttribute("JobCategoryId7", jocategorylist.getJoCategory7ID());
				}
				
				List<String> addlist=new ArrayList<String>();
				
				//Inventory Settings
				addlist.add("OverrideallWarehousingCostMarkupinsideCategory");
				addlist.add("SpecifyPurchaseOrdercost");
				addlist.add("MultipleWarehousesAverageCosting");
				addlist.add("ShowBinLocationonPickTickets");
				addlist.add("ShowWeightonPickTickets");
				addlist.add("UseWarehousesaddressonPickTickets");
				
				//Employee settings
				addlist.add("InvoicesCombinedforJobs");
				addlist.add("AdjustmentDescription1");
				addlist.add("AdjustmentDescription2");
				addlist.add("AdjustmentDescription3");
				addlist.add("AdjustmentDescription4");
				addlist.add("CommissionDescription1");
				addlist.add("CommissionDescription2");
				addlist.add("CommissionDescription3");
				addlist.add("CommissionDescription4");
				addlist.add("AllowEmployeestoviewtheirownCommissionStatements");
				addlist.add("Onlyshowallocatedprofitonsplitcommissions");
				addlist.add("ApplyCreditswhenUsed");
				
				//Job Settings
				addlist.add("PlanSpecLabel1");
				addlist.add("PlanSpecLabel2");
				addlist.add("SourceCheckBox1");
				addlist.add("SourceCheckBox2");
				addlist.add("SourceCheckBox3");
				addlist.add("SourceCheckBox4");
				addlist.add("SourceLabel1");
				addlist.add("SourceLabel2");
				
				//Customer settings
				addlist.add("StatementsshallbegroupedbyJob");
				addlist.add("StatementsshallshowBillingRemainder");
				addlist.add("CheckcreditlimitinSalesOrderOutsideofJob");
				addlist.add("CheckcreditlimitinQuickBook");
				addlist.add("RequireDivisioninSalesOrderOutsideofJob");
				addlist.add("RequireDivisioninCustomerInvoice");
				addlist.add("IncludeSalesTaxwhencalculatingdiscounts");
				addlist.add("IncludeFreightwhencalculatingdiscounts");
				addlist.add("UseDivisionaddressinPickTickets");
				addlist.add("AllowblanklineitemsinSalesOrderInsideOutsideofJob");
				addlist.add("AllowblanklineitemsinCustomerInvoice");
				addlist.add("AllowblankProductItemCodeinSalesOrderInsideOutsideofJob");
				addlist.add("RequirePromiseDateinSalesOrderoutsideofJob");
				
				
				//Job Settings
				addlist.add("JobNoticelabel1Txt");
				addlist.add("JobNoticelabel2Txt");
				addlist.add("JobNoticelabel3Txt");
				
				 addlist.add("AllowchangingJob");
				 addlist.add("OptionalJobQuoteCheckoff");
				 addlist.add("OptionalMemoFieldonQuote");
				 addlist.add("JobNoticelabel1");
				 addlist.add("JobNoticeReportType1");
				 addlist.add("JobNoticelabel2");
				 addlist.add("JobNoticeReportType2");
				 addlist.add("JobNoticelabel3");
				 addlist.add("JobNoticeReportType3");
				 addlist.add("DefaultBidDatetoCurrentDate");
				 addlist.add("DefaultTakeOfftoUsercreatingtheJob");
				 addlist.add("DefaultSalesReptoUserCreatingtheJob");
				 addlist.add("DefaultDivisiontoUserCreatingtheJob");
				 addlist.add("Musthaveaddendumquotethruandbiddatebeforesave");
				 addlist.add("DefaulttoApprovedcreditforanewJob");
				 addlist.add("DefaultQuoteBytoUSercreatingtheJob");
				 addlist.add("JobDetailsOtherlabel");
				 addlist.add("AllowEditingofSentQuotes");
				    
				    
				 addlist.add("JobDetailsReportlabel");
				 addlist.add("Applycoststocustomerinvoice");
				 addlist.add("Closeoutlastinvoicecheckdaysold");
				 addlist.add("RequireaDivisionwhencreatingJobs");
				 addlist.add("UseCustomersCreditLimitwhencreatingJobs");
				 addlist.add("AllowbookingJobswithnoContractAmount");
				 addlist.add("ImportPurchaseOrderdetailsintonewCustomerInvoices");
				 addlist.add("ShowBillingRemainderonJobStatements");
				 addlist.add("HidePostponedoptioninJobs");
				 addlist.add("OnlyallowownersorsupervisorstochangeQuoteTemplates");
				 addlist.add("AllowreleasesifFinalWaiverchecked");
				 addlist.add("WarnifNOCnotreceivedwhenordering");
				 addlist.add("TrackCostonChangeOrders");
				 addlist.add("ShowBranches");
				 addlist.add("ShowcreditsonJobStatements");
				 addlist.add("RequireaEngineerwhenbookingJobs");
				    
				addlist.add("JobNoticeReportType1Txt");
				addlist.add("JobNoticeReportType2Txt");
				addlist.add("JobNoticeReportType3Txt");
				addlist.add("JobDetailsReportlabelTxt");
				addlist.add("CloseoutlastinvoicecheckdaysoldDays");
				addlist.add("DefaultJobTaxTerritorytoCustomerTax");
//				addlist.add("UseCustomerscreditlimitwhencreatingjobs");
				/*Newly Added in customer Setting 
				 *Velmurugan 14-07-2015 
				 * 
				 * */
				addlist.add("RequireFreightwhencalculatingTaxonCustomerInvoices");
				addlist.add("SplitCommissionRequiredOnRelease");
				addlist.add("OverrideReleaseTaxTerritory");
				
				addlist.add("QuotechkI2I3QtyYN");
				addlist.add("QuotechkI2I3CostYN");
				addlist.add("QuotechkI3SellPriceYN");
				addlist.add("QuotechkI2I3ManufYN");
				addlist.add("QuotechkI2I3CatYN");
				addlist.add("QuotechkSPpriceYN");
				addlist.add("CustomerPOReqYN");
				addlist.add("RequireNewNumbersForCuInvoices");
				
				//Customer settings newly added
				addlist.add("IncludeListcolumnoninvoices");
				addlist.add("IncludeExtListcolumnoninvoices");
				addlist.add("IncludeMultcolumnoninvoices");
				
				//ID # 509
				addlist.add("RemoveEXTLISTcolumnfromSalesOrderPDF");
				addlist.add("RemoveMULTcolumnfromSalesOrderPDF");
				addlist.add("QuotechkfontsizepriceYN");
				
				//Vendor Settings
				addlist.add("DefaultPODescItemCode");
				addlist.add("UseCompanyLogoAndLbl");
				addlist.add("ShwVndrPNonPO");
				addlist.add("ShwVndrPIDonPO");
				addlist.add("AskClPOwnItemsRecieved");
				addlist.add("ReqInvWithPO");
				addlist.add("VndrInvDfltOnHold");
				addlist.add("IncTaxOnPOAndInvoices");
				
				//Customer settings
				//added by aravind 03-06-16 ID:534
				addlist.add("DoNotAllowTaxTerritoryAfterSavingCustomerInvoice");
				
				
				ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
				int i=0;
				for(Sysvariable theSysvariable:sysvariablelist){
					//Inventory Settings
					if(i==0){
						model.addAttribute("txt_overridewarehouse", theSysvariable.getValueString());
						model.addAttribute("chk_invoverridewarehouseYes", theSysvariable.getValueLong());
					}else if(i==1){
						model.addAttribute("chk_invpurOrderCostYes", theSysvariable.getValueLong());
					}else if(i==2){
						model.addAttribute("chk_invwareavgcostYes", theSysvariable.getValueLong());
					}else if(i==3){
						model.addAttribute("chk_invbinLocationYes", theSysvariable.getValueLong());
					}else if(i==4){
						model.addAttribute("chk_invWeightYes", theSysvariable.getValueLong());
					}else if(i==5){
						model.addAttribute("chk_invWarhouseaddressYes", theSysvariable.getValueLong());
					}
					
					//Employee Settings
					//txt_adjdescription1 txt_adjdescription2 txt_adjdescription3 txt_adjdescription4  txt_commissiondesc1 txt_commissiondesc2 txt_commissiondesc3 txt_commissiondesc4
//					chk_invoiceCombineYes  chk_viewCommissionYes chk_allocatedProfitYes chk_applyCreditYes
					else if(i==6){
						model.addAttribute("chk_invoiceCombineYes", theSysvariable.getValueLong());
					}
					else if(i==7){
						model.addAttribute("txt_adjdescription1", theSysvariable.getValueString());
					}
					else if(i==8){
						model.addAttribute("txt_adjdescription2", theSysvariable.getValueString());
					}
					else if(i==9){
						model.addAttribute("txt_adjdescription3", theSysvariable.getValueString());
					}
					else if(i==10){
						model.addAttribute("txt_adjdescription4", theSysvariable.getValueString());
					}
					else if(i==11){
						model.addAttribute("txt_commissiondesc1", theSysvariable.getValueString());
					}
					else if(i==12){
						model.addAttribute("txt_commissiondesc2", theSysvariable.getValueString());
					}
					else if(i==13){
						model.addAttribute("txt_commissiondesc3", theSysvariable.getValueString());
					}
					else if(i==14){
						model.addAttribute("txt_commissiondesc4", theSysvariable.getValueString());
					}
					else if(i==15){
						model.addAttribute("chk_viewCommissionYes", theSysvariable.getValueLong());
					}
					else if(i==16){
						model.addAttribute("chk_allocatedProfitYes", theSysvariable.getValueLong());
					}
					else if(i==17){
						model.addAttribute("chk_applyCreditYes", theSysvariable.getValueLong());
					}
					else if(i==18){
						model.addAttribute("txt_PlanSpecLabel1", theSysvariable.getValueString());
					}else if(i==19){
						model.addAttribute("txt_PlanSpecLabel2", theSysvariable.getValueString());
					}else if(i==20){
						model.addAttribute("txt_SourceCheckBox1", theSysvariable.getValueString());
					}else if(i==21){
						model.addAttribute("txt_SourceCheckBox2", theSysvariable.getValueString());
					}else if(i==22){
						model.addAttribute("txt_SourceCheckBox3", theSysvariable.getValueString());
					}else if(i==23){
						model.addAttribute("txt_SourceCheckBox4", theSysvariable.getValueString());
					}else if(i==24){
						model.addAttribute("txt_SourceLabel1", theSysvariable.getValueString());
					}else if(i==25){
						model.addAttribute("txt_SourceLabel2", theSysvariable.getValueString());
					}
					else if(i==26){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusStGpbyJobYes", theSysvariable.getValueLong());
					}
					else if(i==27){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusStShowBillRemYes", theSysvariable.getValueLong());
					}
					else if(i==28){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusCreLiminSalOrdYes", theSysvariable.getValueLong());
					}
					else if(i==29){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusCreLiminQuickBookYes", theSysvariable.getValueLong());
					}
					else if(i==30){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusReqDivinSalOrdYes", theSysvariable.getValueLong());
					}
					else if(i==31){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusReqDivinCusInvYes", theSysvariable.getValueLong());
					}
					else if(i==32){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusIncSalTaxYes", theSysvariable.getValueLong());
					}
					else if(i==33){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusIncFreightYes", theSysvariable.getValueLong());
					}
					else if(i==34){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusUseDivAddYes", theSysvariable.getValueLong());
					}
					else if(i==35){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusAllblCusInvYes", theSysvariable.getValueLong());
					}
					else if(i==36){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusAllblProdinSalOrdYes", theSysvariable.getValueLong());
					}
					else if(i==37){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusReqProinSalOrderYes", theSysvariable.getValueLong());
					}
//					else if(i==38){
//						model.addAttribute("chk_cusReqProinSalOrderYes", theSysvariable.getValueLong());
//					}
					else if(i==39){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueString());
						model.addAttribute("chkjobnoticelbl1TXT", theSysvariable.getValueString());
					}
					else if(i==40){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueString());
						model.addAttribute("chkjobnoticelbl2TXT", theSysvariable.getValueString());
					}
					else if(i==41){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueString());
						model.addAttribute("chkjobnoticelbl3TXT", theSysvariable.getValueString());
					}
					else if(i==42){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobcheckoffYN", theSysvariable.getValueLong());
					}
					else if(i==43){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkchangejobYN", theSysvariable.getValueLong());
					}
					else if(i==44){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkmemoYN", theSysvariable.getValueLong());
					}
					else if(i==45){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticelbl1YN", theSysvariable.getValueLong());
					}
					else if(i==46){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype1YN", theSysvariable.getValueLong());
					}
					else if(i==47){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticelbl2YN", theSysvariable.getValueLong());
					}
					else if(i==48){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype2YN", theSysvariable.getValueLong());
					}
					else if(i==49){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticelbl3YN", theSysvariable.getValueLong());
					}
					else if(i==50){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype3YN", theSysvariable.getValueLong());
					}
					else if(i==51){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkbidtocurdateYN", theSysvariable.getValueLong());
					}
					else if(i==52){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdeftakeoffYN", theSysvariable.getValueLong());
					}
					else if(i==53){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefsalesrepYN", theSysvariable.getValueLong());
					}
					else if(i==54){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefdivisionYN", theSysvariable.getValueLong());
					}
					else if(i==55){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkaddendumYN", theSysvariable.getValueLong());
					}
					else if(i==56){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefcreditYN", theSysvariable.getValueLong());
					}
					else if(i==53){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefsalesrepYN", theSysvariable.getValueLong());
					}
					else if(i==54){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefdivisionYN", theSysvariable.getValueLong());
					}
					else if(i==55){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkaddendumYN", theSysvariable.getValueLong());
					}
					else if(i==56){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefcreditYN", theSysvariable.getValueLong());
					}
					else if(i==57){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefaultquoteYN", theSysvariable.getValueLong());
					}
					else if(i==58){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobdetailotherlblYN", theSysvariable.getValueLong());
					}
					else if(i==59){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkeditsentquoteYN", theSysvariable.getValueLong());
					}
					else if(i==60){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjodetreplabTXT", theSysvariable.getValueLong());
					}
					else if(i==61){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkappcocusinvYN", theSysvariable.getValueLong());
					}
					else if(i==62){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkcloselastinvchkYN", theSysvariable.getValueLong());
					}
					else if(i==63){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkreqdivcreatjobYN", theSysvariable.getValueLong());
					}
					else if(i==64){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkUserCustomerCreditYN", theSysvariable.getValueLong());
					}
					else if(i==65){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkalwbookjobscamtYN", theSysvariable.getValueLong());
					}
					else if(i==66){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkimpponewcustomerYN", theSysvariable.getValueLong());
					}
					else if(i==67){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkshwbilremjobstYN", theSysvariable.getValueLong());
					}
					else if(i==68){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkhidepostjobsYN", theSysvariable.getValueLong());
					}
					else if(i==69){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkonalsuqottempYN", theSysvariable.getValueLong());
					}
					else if(i==70){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkalrwaichekedYN", theSysvariable.getValueLong());
					}
					else if(i==71){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkwnocrordYN", theSysvariable.getValueLong());
					}
					else if(i==72){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chktkcoschordYN", theSysvariable.getValueLong());
					}
					else if(i==73){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkreqengbookjobYN", theSysvariable.getValueLong());
					}
					else if(i==74){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkwnocrordYN", theSysvariable.getValueLong());
					}
					else if(i==75){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype1TXT", theSysvariable.getValueLong());
					}
					else if(i==76){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype2TXT", theSysvariable.getValueLong());
					}
					else if(i==77){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkjobnoticereporttype3TXT", theSysvariable.getValueLong());
					}
					else if(i==80){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkcloselastinvchkDays", theSysvariable.getValueLong());
					}
					else if(i==81){
						logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefJobTaxTerritoryYN", theSysvariable.getValueLong());
					}
					
					else if(i==82){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusReqfreinCuInvoicesYes", theSysvariable.getValueLong());
					}
					else if(i==83){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chkSplitCommissionYN", theSysvariable.getValueLong());
					}
					else if(i==84){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chkdefOverRideTaxTerritoryYN", theSysvariable.getValueLong());
					}
					
					else if(i==85){
						model.addAttribute("chkI2I3QtyYN", theSysvariable.getValueLong());
					}else if(i==86){
						model.addAttribute("chkI2I3CostYN", theSysvariable.getValueLong());
					}else if(i==87){
						model.addAttribute("chkI3SellPriceYN", theSysvariable.getValueLong());
					}else if(i==88){
						model.addAttribute("chkI2I3ManufYN", theSysvariable.getValueLong());
					}else if(i==89){
						model.addAttribute("chkI2I3CatYN", theSysvariable.getValueLong());
					}else if(i==90){
						model.addAttribute("chkSPpriceYN", theSysvariable.getValueLong());
					}else if(i==91){
						model.addAttribute("chkreqCPOcreatjobYN", theSysvariable.getValueLong());
					}else if(i==92){
						model.addAttribute("chk_cusReqSeqNumCuInvoicesYes", theSysvariable.getValueLong());
						model.addAttribute("inp_cusNewSeqNumCuInvoices", theSysvariable.getValueString());
					}
					
					
					//Customer Settings newly added
					else if(i==93){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusIncListYes", theSysvariable.getValueLong());
					}
					else if(i==94){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusIncExtListYes", theSysvariable.getValueLong());
					}
					else if(i==95){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusIncMultYes", theSysvariable.getValueLong());
					}
					else if(i==96){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusRemExtListfmSalOrdpdfYes", theSysvariable.getValueLong());
					}
					else if(i==97){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_cusRemMultfmSalOrdpdfYes", theSysvariable.getValueLong());
					}
					else if(i==98){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chkfontsizeonPriceYN", theSysvariable.getValueLong());
						model.addAttribute("inp_fontsizeonPriceYN", theSysvariable.getValueString());
					}
					
					//VendorSettings
					else if(i==99){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_venpoDesStatusYN", theSysvariable.getValueLong());
					}else if(i==100){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_vencmpanylogoStatusYN", theSysvariable.getValueLong());
					}else if(i==101){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_vendorPhnoYesYN", theSysvariable.getValueLong());
					}else if(i==102){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_venproductidStatusYN", theSysvariable.getValueLong());
					}else if(i==103){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_venclosePoStatusYN", theSysvariable.getValueLong());
					}else if(i==104){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_venreqInvStatusYN", theSysvariable.getValueLong());
					}else if(i==105){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_vendefaultInvStatusYN", theSysvariable.getValueLong());
					}else if(i==106){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_IncTaxPOInvStatusYN", theSysvariable.getValueLong());
					}
					
					//customer settings newly added
					else if(i==107){
						logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
						model.addAttribute("chk_taxTerCuInvAftSaveYes", theSysvariable.getValueLong());
					}
					
					
					i=i+1;
				}
				
				TsUserGroup agroupDefaults=new TsUserGroup();
				for(int k=1;k<9;k++){
					agroupDefaults=sysservice.getgroupDefaults(k);
					if(agroupDefaults==null){
						agroupDefaults=new TsUserGroup();
					}
					model.addAttribute("groupDefaults"+k, agroupDefaults);
				}
				try {
					model.addAttribute("productDepartment",(List<PrDepartment>) itsInventoryService.getPrDepartment());
				} catch (InventoryException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/settingsform", method = RequestMethod.GET)
	public String getSettingsFromPage(HttpServletRequest request,
			ModelMap model, HttpSession session, HttpServletResponse response)
			throws IOException, JobException {
		String aPage = "settingsform";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserSetting aUserLoginSetting = null;
		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
		try {
			if (aUserBean != null) {
				aUserLoginSetting = userService.getSingleUserSettingsDetails(1);
				model.addAttribute("userLoginSettings", aUserLoginSetting);
				String aheaderText = aUserLoginSetting.getHeaderText();
				String aTerms = aUserLoginSetting.getTerms();
				/*
				 * String aHeaderTxt = "("+aheaderText+")"; String aTermsTxt =
				 * "("+aTerms+")";
				 */
				String aHeaderTextReplace = aheaderText.replaceAll("andamp;",
						"&");
				String aTermsTextReplace = aTerms.replaceAll("andamp;", "&");
				model.addAttribute("headerTextSettings", aHeaderTextReplace);
				model.addAttribute("termsTextSettings", aTermsTextReplace);
				setItsPrwarehouses((List<Prwarehouse>) jobService
						.getWareHouse());
				model.addAttribute("wareHouse", getItsPrwarehouses());
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/employee", method = RequestMethod.GET)
	public String getEmployeePage(HttpServletRequest request) {
		String aPage = "employee";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/employeedetails", method = RequestMethod.GET)
	public String getEmployeeDetailsPage(
			@RequestParam(value = "rolodexNumber", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theEmployeeName,
			ModelMap model, HttpServletRequest request) throws JobException, UserException {

		String aPage = "addemployee";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Sysassignment sysassignment=null;
		Sysinfo sysinfo=null;
		model.addAttribute("warehouse", jobService.getWareHouse());
		if (theRolodexNumber != null && theRolodexNumber != "") {
			sysinfo=sysservice.getRoldexCategories(1);
			model.addAttribute("rolodexNumber", theRolodexNumber);
			setRxMasterDetails(theRolodexNumber);
			setCommissionsDetails(theRolodexNumber);
			Rxmaster aRxmaster = getRxMasterDetails();

			Emmaster aEmmaster = getCommissionsDetails();
			
			model.addAttribute("name", aRxmaster.getName());
			model.addAttribute("phone1", aRxmaster.getPhone1());
			model.addAttribute("Websight", aRxmaster.getWebsight());
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("enMasterDetails", aEmmaster);
			sysassignment=sysservice.getCustomerCategories();
			if(sysassignment!=null){
				model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				/*model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
				model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
				model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
				model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
				model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());*/
			}
			if(sysinfo!=null){
				model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
				model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
				model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
				model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
				model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
				model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
				model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
				model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
				}
		}
		return aPage;
	}

	@RequestMapping(value = "sales", method = RequestMethod.GET)
	public String getSalesPage(HttpServletRequest request) {
		String aPage = "sales";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/employeelist", method = RequestMethod.GET)
	public String getEmployeeListPage(HttpServletRequest request,
			ModelMap model, HttpSession session, HttpServletResponse response)
			throws IOException {
		String aPage = "employeelist";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/jobflow", method = RequestMethod.GET)
	public String getJobListPage(
			@RequestParam(value = "token", required = false) String theToken,
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value = "jobStatus", required = false) String theJobStatus,
			@RequestParam(value = "jobType", required = false) String theJobType,
			@RequestParam(value = "joMasterID", required = false) Integer thejoMasterID,
			HttpServletRequest thesRequest, HttpSession theSession,
			ModelMap theModel, HttpServletResponse theResponse)
			throws JobException, IOException, CompanyException, UserException {
		logger.debug("Received request to show job flow page");
		Cumaster aCumaster = null;
		Sysassignment sysassignment=null;
		Sysinfo sysinfo=null;
		
		try {
			if (!SessionConstants.checkSessionExist(thesRequest)) {
				String aPage = "welcome";
				return aPage;
			}
			sysinfo=sysservice.getRoldexCategories(1);
			sysassignment=sysservice.getCustomerCategories();
			
			theSession.setAttribute("selectedJobNumber", theJobNumber);
			if (theToken != null && theToken.equalsIgnoreCase("new")) {
				theSession.removeAttribute(SessionConstants.JOB_GRID_OBJ);
				theSession.setAttribute(SessionConstants.JOB_TOKEN, "newJob");
				//return "joblist";
			}
			if (theJobNumber != null && theToken.equalsIgnoreCase("view")) {
				theModel.addAttribute("jobNumber", theJobNumber);
				theModel.addAttribute("jobCustomer", theJobCustomer);
				theSession.setAttribute(SessionConstants.JOB_TOKEN, "viewJob");
				setJomasterDetails(theJobNumber,thejoMasterID);
				Jomaster aJomaster = getJomasterDetails();
				if (aJomaster.getRxCustomerId() != null) {
					aCumaster = new Cumaster();
					try {
						aCumaster = jobService
								.getSingleCuMasterDetails(aJomaster
										.getRxCustomerId());
					} catch (JobException e) {
						logger.error(e.getMessage(), e);
						theResponse.sendError(e.getItsErrorStatusCode(),
								e.getMessage());
					}
				}
				UserBean aUserBean;
				aUserBean = (UserBean) theSession
						.getAttribute(SessionConstants.USER);
				if (aUserBean.getSystemAdministrator() != 1) {
					theModel.addAttribute("isAdmin", false);
				} else {
					theModel.addAttribute("isAdmin", true);
				}

				setDivision((List<Codivision>) itsCompanyService
						.getCompanyDivisions());
				theModel.addAttribute("divisionsSearch", getDivision());
				theModel.addAttribute("joMasterID", aJomaster.getJoMasterId());
				theModel.addAttribute("jobStatusID", aJomaster.getJobStatus());
				theModel.addAttribute("jobName", aJomaster.getDescription());
				theModel.addAttribute("jobQuoteNumber",
						aJomaster.getQuoteNumber());
				theModel.addAttribute("cuMasterDetails", aCumaster);
				theModel.addAttribute("rxCustomerID",
						aJomaster.getRxCustomerId());
				
				String appletUrl = "";//userService.getUserDetails(aUserBean.getUserId()).getAppletLocalUrl();
				appletUrl = userService.getAppletUrl(theJobNumber, aUserBean.getUserId(),aJomaster.getJoMasterId());
				logger.info(" appletUrl = "+appletUrl);
				theModel.addAttribute("appletUrl", appletUrl);
				
				JobHistory aJobHistory = new JobHistory();
				aJobHistory.setJobNumber(theJobNumber);
				aJobHistory.setJobName(aJomaster.getDescription());
				aJobHistory.setBidDate(aJomaster.getBidDate());
				aJobHistory.setJobOpenedDate(new java.util.Date());
				aJobHistory.setJoMasterID(thejoMasterID);
				jobService.addLastOpened(aJobHistory);
			}
			
			if(sysinfo!=null){
				theModel.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
				theModel.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
				/*theModel.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
				theModel.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
				theModel.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
				theModel.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
				theModel.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
				theModel.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());*/
			}
			
			if(sysassignment!=null){
				theModel.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				theModel.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				theModel.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				theModel.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				theModel.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				/*model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
				model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
				model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
				model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
				model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());*/
			}
			logger.info("Job Type:"+theJobType);
			theModel.addAttribute("jobtype", theJobType);
			
			
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return "joblist";
	}

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String getProjectsPage(HttpServletRequest request, HttpSession theSession, ModelMap theModel) throws UserException {
		String aPage = "project";
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
		if (aUserBean != null) {
			aUserLogin = userService.getSingleUserDetails(aUserBean.getUserId());			
			theModel.addAttribute("userSysAdmin", aUserLogin.getSystemAdministrator());
			theModel.addAttribute("userDetails", aUserLogin);
		}
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/vendors", method = RequestMethod.GET)
	public String getVendorPage(HttpServletRequest request, ModelMap model,
			HttpSession session, HttpServletResponse response)
			throws IOException {
		String aPage = "vendorslist";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/architectDetails", method = RequestMethod.GET)
	public String getArchitectDetailsPage(
			@RequestParam(value = "rolodexNumber", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theArchitectName,
			ModelMap model, HttpSession session, HttpServletRequest request) throws UserException {
		String aPage = "architectDetails";
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			return aPage;
		}
		if (theRolodexNumber != null) {
			model.addAttribute("rolodexNumber",
					Integer.valueOf(theRolodexNumber));
			theArchitectName = theArchitectName.replace("`", "");
			theArchitectName = theArchitectName.replace("`", "");
			setRxMasterDetails(theRolodexNumber);
			Rxmaster aRxmaster = getRxMasterDetails();
			model.addAttribute("name", aRxmaster.getName());
			model.addAttribute("phone1", aRxmaster.getPhone1());
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("Websight", aRxmaster.getWebsight());
			
			sysinfo=sysservice.getRoldexCategories(1);
			if(sysinfo!=null){
						model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
						model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
						model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
						model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
						model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
						model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
						model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
						model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
						}
			
				sysassignment=sysservice.getCustomerCategories();
				if(sysassignment!=null){
					model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
					model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
					model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
					model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
					model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
					
				}
			
			// session.setAttribute("rxMasterId",
			// Integer.valueOf(theRolodexNumber));
			/* session.setAttribute("rolodexName", theCustomerName); */
		}
		return aPage;
	}

	@RequestMapping(value = "/engineerDetails", method = RequestMethod.GET)
	public String getEngineerDetailsPage(
			@RequestParam(value = "rolodexNumber", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theEngineerName,
			ModelMap model, HttpSession session, HttpServletRequest request) throws UserException {
		String aPage = "engineerDetails";
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			return aPage;
		}
		if (theRolodexNumber != null) {
			model.addAttribute("rolodexNumber",
					Integer.valueOf(theRolodexNumber));
			theEngineerName = theEngineerName.replace("`", "");
			theEngineerName = theEngineerName.replace("`", "");
			setRxMasterDetails(theRolodexNumber);
			Rxmaster aRxmaster = getRxMasterDetails();
			model.addAttribute("name", aRxmaster.getName());
			model.addAttribute("phone1", aRxmaster.getPhone1());
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("Websight", aRxmaster.getWebsight());
			
			sysinfo=sysservice.getRoldexCategories(1);
			if(sysinfo!=null){
						model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
						model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
						model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
						model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
						model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
						model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
						model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
						model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
						}
						
			sysassignment=sysservice.getCustomerCategories();
			if(sysassignment!=null){
				model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				
			}
			// session.setAttribute("rxMasterId",
			// Integer.valueOf(theRolodexNumber));
			/* session.setAttribute("rolodexName", theCustomerName); */
		}
		return aPage;
	}

	@RequestMapping(value = "/vendordetails", method = RequestMethod.GET)
	public String getVendorDetailsPage(
			@RequestParam(value = "rolodexNumber", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theVendorName,
			ModelMap model, HttpSession session, HttpServletRequest request) throws UserException {
		String aPage = "vendordetails";
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		if (theRolodexNumber != null && theRolodexNumber != "") {
			model.addAttribute("rolodexNumber", theRolodexNumber);
			setRxMasterDetails(theRolodexNumber);
			Rxmaster aRxmaster = getRxMasterDetails();
			model.addAttribute("name", aRxmaster.getName());
			model.addAttribute("phone1", aRxmaster.getPhone1());
			model.addAttribute("Websight", aRxmaster.getWebsight());
			model.addAttribute("rxMasterDetails", aRxmaster);
			session.setAttribute("rxMasterId", theRolodexNumber);
			sysinfo=sysservice.getRoldexCategories(1);
			if(sysinfo!=null){
				model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
				model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
				model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
				model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
				model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
				model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
				model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
				model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
			}
			
			sysassignment=sysservice.getCustomerCategories();
			if(sysassignment!=null){
				model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				/*model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
				model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
				model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
				model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
				model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());*/
			}
			
			
		}
		return aPage;
	}

	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public String getRetulsPage(HttpServletRequest request, ModelMap theModel) {
		String aPage = "search_results";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		try {
			setDivision((List<Codivision>) itsCompanyService
					.getCompanyDivisions());
			theModel.addAttribute("divisionsSearch", getDivision());
		} catch (Exception e) {
			e.getMessage();
		}
		return aPage;
	}

	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public String getProductsPage(HttpServletRequest request) {
		String aPage = "products";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public String getJobsPage(HttpServletRequest request) {
		String aPage = "jobs";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	public String getInventoryPage(@RequestParam(value = "token", required = false) String theToken,
			@RequestParam(value = "key", required = false) String theKey,HttpServletRequest request, ModelMap model,
			HttpSession session, HttpServletResponse response)
			throws IOException, JobException, SysException {
		String aPage = "inventory";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Session aUserSession =null;
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				model.addAttribute("warehouse", jobService.getWareHouse());
				
				SysUserDefault newSysUserDefault = new SysUserDefault();
				SysUserDefault aSysUserDefault = new SysUserDefault();
				newSysUserDefault = userService.getSysUserDefault(aUserLogin.getUserLoginId());
				aUserSession = itsSessionFactory.openSession();
				if(newSysUserDefault!=null &&newSysUserDefault.getSysUserDefaultID()!=null){
				aSysUserDefault = (SysUserDefault) aUserSession.get(SysUserDefault.class, newSysUserDefault.getSysUserDefaultID());
				Integer warehouseID = aSysUserDefault.getWarehouseID();
				System.out.println(" warehouseId :: "+warehouseID);
				model.addAttribute("warehouseID", warehouseID);
				model.addAttribute("key", theKey);
			}
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}finally{
			aUserSession.flush();
			aUserSession.close();
		}
		return aPage;
	}
	
	
	/* Start of Report Tab Sub Menus*/
	
	@RequestMapping(value = "/balanceSheet", method = RequestMethod.GET)
	public String getBalanceSheetPage(HttpServletRequest request, ModelMap model, HttpSession session, HttpServletResponse response) throws IOException, JobException {
		String aPage = "balanceSheet";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	
	@RequestMapping(value = "/printBalanceSheet", method = RequestMethod.GET)
	public @ResponseBody void printOrderPoint(HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, SQLException {
		Connection connection =null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String condition = "";
			String query = "SELECT A.coAccountID, A.Number, A.Description, B.coFiscalPeriodID, B.YearOpening, B.YearDebits, B.YearCredits," +
					" B.QuarterOpening, B.QuarterDebits, B.QuarterCredits, B.PeriodOpening, B.PeriodDebits, B.PeriodCredits" +
					" FROM coAccount AS A" +
					" LEFT JOIN coBalance AS B ON  A.coAccountID = B.coAccountID" +
					" WHERE (A.coAccountID<>112) AND  ((A.Number >= '01020')" +
					" AND (A.coAccountID<>112) AND ((A.Number <= '01020') AND A.IsMasterAccount=0" +
					" AND (A.coAccountID<>112) AND (A.TotalingLevel = 0) AND (B.coFiscalPeriodID = 139 OR B.coFiscalPeriodID IS NULL))) ORDER BY A.Number";
			System.out.println("Customer Qry : "+query);
		
		
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/orderpoints.jrxml");
			filename="orderPoints.pdf";
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			params.put("condition", condition);
			
			connection = con.getConnection();
			System.out.println(" condition :: "+condition);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);

		} catch (Exception e) {
			theResponse.sendError(500, e.getMessage());
		}finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}	
	
	@RequestMapping(value = "/trialBalance", method = RequestMethod.GET)
	public String getTrialBalancePage(HttpServletRequest request, ModelMap model, HttpSession session, HttpServletResponse response) throws CompanyException {
		String aPage = "trialBalance";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		setItsCoAccount((List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown());
		model.addAttribute("glAccount", getItsCoAccount());
		return aPage;
	}
	
	@RequestMapping(value = "/trialBalanceView", method = RequestMethod.GET)
	public String getTrialBalanceViewPage(HttpServletRequest request, ModelMap model, HttpSession session, HttpServletResponse response) throws CompanyException {
		String aPage = "trialbalanceview";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		/*setItsCoAccount((List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown());
		model.addAttribute("glAccount", getItsCoAccount());*/
		return aPage;
	}
	
	@RequestMapping(value = "/printTrailBalance", method = RequestMethod.GET)
	public @ResponseBody void printTrailBalance(HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, SQLException {
		Connection connection = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String condition = "";
			String query = "SELECT A.coAccountID, A.Number, A.Description, B.coFiscalPeriodID, B.YearOpening," +
					" B.YearDebits, B.YearCredits, B.QuarterOpening, B.QuarterDebits," +
					" B.QuarterCredits, B.PeriodOpening, B.PeriodDebits, B.PeriodCredits FROM coAccount" +
					" AS A LEFT JOIN coBalance AS B ON  A.coAccountID = B.coAccountID" +
					" WHERE (A.coAccountID<>112) AND  ((A.Number >= '01020') AND (A.coAccountID<>112)" +
						" AND ((A.Number <= '01020') AND A.IsMasterAccount=0 AND (A.coAccountID<>112)" +
						" AND (A.TotalingLevel = 0) AND (B.coFiscalPeriodID = 139 Or B.coFiscalPeriodID Is Null))) ORDER BY A.Number";
			System.out.println("Customer Qry : "+query);
		
		
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/orderpoints.jrxml");
			filename="orderPoints.pdf";
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			params.put("condition", condition);
			
			connection = con.getConnection();
			System.out.println(" condition :: "+condition);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);

		} catch (Exception e) {
			theResponse.sendError(500, e.getMessage());
		}finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}
	
	@RequestMapping(value = "/incomeStatement", method = RequestMethod.GET)
	public String getIncomeStatementPage(HttpServletRequest request, ModelMap model, HttpSession session, HttpServletResponse response) throws IOException, JobException {
		String aPage = "incomeStatement";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	
	@RequestMapping(value = "/printIncomeStatement", method = RequestMethod.GET)
	public @ResponseBody void printIncomeStatement(HttpServletResponse theResponse, HttpServletRequest theRequest) throws IOException, SQLException {
		Connection connection = null;
		ConnectionProvider con = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			String path_JRXML =null;
			String filename=null;
			String condition = "";
			String query = "SELECT A.*, B.coFiscalPeriodID, B.YearOpening, B.YearDebits, B.YearCredits, B.QuarterOpening," +
					" B.QuarterDebits, B.QuarterCredits, B.PeriodOpening, B.PeriodDebits, B.PeriodCredits" +
					" FROM coAccount AS A LEFT JOIN coBalance AS B ON A.coAccountID = B.coAccountID" +
					" WHERE ( (A.IncludeWhenZero=1 AND A.IsSubAccount=0 AND A.IsMasterAccount=0 and B.YearOpening=0 AND B.YearDebits=0" +
						" AND B.YearCredits=0 AND B.QuarterOpening=0 AND B.QuarterDebits=0 AND B.QuarterCredits=0" +
						" AND B.PeriodOpening=0 AND B.PeriodDebits=0 AND B.PeriodCredits=0)" +
						" OR A.TotalingLevel<>0 OR A.SubAccount='250') AND (A.Number>='13000') AND (A.Number<='999999999999')" +
						" AND (B.coFiscalPeriodID = 139 Or B.coFiscalPeriodID Is Null) ORDER BY A.Number";
			System.out.println("Customer Qry : "+query);
		
		
			path_JRXML = theRequest.getSession().getServletContext().getRealPath("/resources/jasper_reports/orderpoints.jrxml");
			filename="orderPoints.pdf";
			JasperDesign jd  = JRXmlLoader.load(path_JRXML);
			JRDesignQuery jdq=new JRDesignQuery();
			jdq.setText(query);
			jd.setQuery(jdq);
			con = itspdfService.connectionForJasper();
			params.put("condition", condition);
			
			connection = con.getConnection();
			System.out.println(" condition :: "+condition);
			ReportService.dynamicReportCall(theResponse,params,"pdf",jd,filename,connection);

		} catch (Exception e) {
			theResponse.sendError(500, e.getMessage());
		}finally{
			if(con!=null){
				con.closeConnection(connection);
				con=null;
				}
		}
	}	
	
	/* End of Report Tab Sub Menus*/

	// CreatedBy : Naveed	Date : 22 Sept 2014
	// Desc : Creating a new InventoryValue page
	@RequestMapping(value = "/inventoryValue", method = RequestMethod.GET)
	public String getInventoryValuePage(@RequestParam(value = "token", required = false) String theToken,
			@RequestParam(value = "key", required = false) String theKey,HttpServletRequest request, ModelMap model,
			HttpSession session, HttpServletResponse response)
			throws IOException, JobException {
		String aPage = "inventoryvalue";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				model.addAttribute("warehouse", jobService.getWareHouse());
				
				model.addAttribute("key", theKey);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	} 
	
	@RequestMapping(value = "/inventoryTransactions", method = RequestMethod.GET)
	public String inventoryTransactions(
			@RequestParam(value = "inventoryId", required = false) Integer theprMasterID,
			@RequestParam(value = "itemCode", required = false) String theItemCode,
			@RequestParam(value = "token", required = false) String theToken,
			ModelMap model, HttpSession theSession, HttpServletRequest request,
			HttpServletResponse response, ModelMap theModel)
			throws IOException, InventoryException, JobException, ProductException {
		String aPage = "inventory/inventoryTransactions";

		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			setItsPrdepartment((List<PrDepartment>) itsInventoryService
					.getPrDepartment());
			setItsPrcategory((List<PrCategory>) itsInventoryService
					.getPrCategory());
			setItsPrwarehouses((List<Prwarehouse>) jobService.getWareHouse());
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				theModel.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
			}
			Sysinfo sysinfo=sysservice.getRoldexCategories(1);
			theModel.addAttribute("prPriceLevel0",sysinfo.getPrPriceLevel0());
			theModel.addAttribute("prPriceLevel1",sysinfo.getPrPriceLevel1());
			theModel.addAttribute("prPriceLevel2",sysinfo.getPrPriceLevel2());
			theModel.addAttribute("prPriceLevel3",sysinfo.getPrPriceLevel3());
			theModel.addAttribute("prPriceLevel4",sysinfo.getPrPriceLevel4());
			theModel.addAttribute("prPriceLevel5",sysinfo.getPrPriceLevel5());
			
			theModel.addAttribute("Warehouses", (List<Prwarehouse>)itsProductService.getWarehouses());
			
			if (itsPrDepartment != null) {
				if (!itsPrDepartment.isEmpty()) {
					theModel.addAttribute("productDepartment",
							getItsPrdepartment());
				}
			}
			if (itsPrcategory != null) {
				if (!itsPrcategory.isEmpty()) {
					theModel.addAttribute("productCategory", getItsPrcategory());
				}
			}
			if (itsPrwarehouses != null) {
				if (!itsPrwarehouses.isEmpty()) {
					theModel.addAttribute("prWhare", getItsPrwarehouses());
				}
			}
			if (theToken != null && theToken.equalsIgnoreCase("new")) {
				theSession.removeAttribute(SessionConstants.INVENTORY_GRID_OBJ);
				return aPage;
			}
			if (theToken != null && theToken.equalsIgnoreCase("view")) {
				Prmaster aPrmaster = new Prmaster();
				Prwarehouseinventory aPrwarehouseinventory = new Prwarehouseinventory();
				aPrmaster = itsInventoryService
						.getSingleProductDetails(theprMasterID);
				aPrwarehouseinventory = itsInventoryService
						.getSingleWareDetails(theprMasterID);
				theModel.addAttribute("prMasterDetails", aPrmaster);
				theModel.addAttribute("prWareHouseDetails",
						aPrwarehouseinventory);
				theModel.addAttribute("prWarehouseCost", itsInventoryService.getWarehouseCost(theprMasterID));
				
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;

	}
	
	@RequestMapping(value = "/inventoryDetails", method = RequestMethod.GET)
	public String getInventoryDetailsPage(
			@RequestParam(value = "inventoryId", required = false) Integer theprMasterID,
			@RequestParam(value = "itemCode", required = false) String theItemCode,
			@RequestParam(value = "token", required = false) String theToken,
			ModelMap model, HttpSession theSession, HttpServletRequest request,
			HttpServletResponse response, ModelMap theModel)
			throws IOException, InventoryException, JobException {
		String aPage = "inventoryDetails";

		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			setItsPrdepartment((List<PrDepartment>) itsInventoryService
					.getPrDepartment());
			setItsPrcategory((List<PrCategory>) itsInventoryService
					.getPrCategory());
			setItsPrwarehouses((List<Prwarehouse>) jobService.getWareHouse());
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				theModel.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
			}
			Sysinfo sysinfo=sysservice.getRoldexCategories(1);
			theModel.addAttribute("prPriceLevel0",sysinfo.getPrPriceLevel0());
			theModel.addAttribute("prPriceLevel1",sysinfo.getPrPriceLevel1());
			theModel.addAttribute("prPriceLevel2",sysinfo.getPrPriceLevel2());
			theModel.addAttribute("prPriceLevel3",sysinfo.getPrPriceLevel3());
			theModel.addAttribute("prPriceLevel4",sysinfo.getPrPriceLevel4());
			theModel.addAttribute("prPriceLevel5",sysinfo.getPrPriceLevel5());
			if (itsPrDepartment != null) {
				if (!itsPrDepartment.isEmpty()) {
					theModel.addAttribute("productDepartment",
							getItsPrdepartment());
				}
			}
			if (itsPrcategory != null) {
				if (!itsPrcategory.isEmpty()) {
					theModel.addAttribute("productCategory", getItsPrcategory());
				}
			}
			if (itsPrwarehouses != null) {
				if (!itsPrwarehouses.isEmpty()) {
					theModel.addAttribute("prWhare", getItsPrwarehouses());
				}
			}
			if (theToken != null && theToken.equalsIgnoreCase("new")) {
				theSession.removeAttribute(SessionConstants.INVENTORY_GRID_OBJ);
				return aPage;
			}
			if (theToken != null && theToken.equalsIgnoreCase("view")) {
				Prmaster aPrmaster = new Prmaster();
				Prwarehouseinventory aPrwarehouseinventory = new Prwarehouseinventory();
				aPrmaster = itsInventoryService
						.getSingleProductDetails(theprMasterID);
				aPrwarehouseinventory = itsInventoryService
						.getSingleWareDetails(theprMasterID);
				theModel.addAttribute("prMasterDetails", aPrmaster);
				theModel.addAttribute("prWareHouseDetails",
						aPrwarehouseinventory);
				theModel.addAttribute("prWarehouseCost", itsInventoryService.getWarehouseCost(theprMasterID));
				
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String getCustomersPage(HttpServletRequest request, ModelMap model,
			HttpSession session, HttpServletResponse response)
			throws IOException {
		String aPage = "customers";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/customerdetails", method = RequestMethod.GET)
	public String getCustomerDetailsPage(
			@RequestParam(value = "rolodexNumber", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theCustomerName,
			ModelMap model, HttpSession session, HttpServletRequest request) throws UserException {
		String aPage = "generalCustomers";
		Sysinfo sysinfo=null;
		Sysassignment sysassignment=null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			return aPage;
		}
		System.out.println("theRolodexNumber"+theRolodexNumber);
		if (theRolodexNumber != null) {
			
			sysinfo=sysservice.getRoldexCategories(1);
			model.addAttribute("rolodexNumber",
					Integer.valueOf(theRolodexNumber));
			theCustomerName = theCustomerName.replace("`", "");
			theCustomerName = theCustomerName.replace("`", "");
			setRxMasterDetails(theRolodexNumber);
			Rxmaster aRxmaster = getRxMasterDetails();
			
			model.addAttribute("name", aRxmaster.getName());
			model.addAttribute("phone", aRxmaster.getPhone1());
			model.addAttribute("Websight", aRxmaster.getWebsight());
			List<?> aCustomer = itsEmployeeService.getEmployees(0, 1000, 0);
			/*Getting rolodox categories  from sysinfo table*/
			if(sysinfo!=null){
			model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
			model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
			model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
			model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
			model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
			model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
			model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
			model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
			}
			
			sysassignment=sysservice.getCustomerCategories();
			if(sysassignment!=null){
				model.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
				model.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
				model.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
				model.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
				model.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
				/*model.addAttribute("CustomerCategoryId1",sysassignment.getCustomerCategoryId1());
				model.addAttribute("CustomerCategoryId2", sysassignment.getCustomerCategoryId2());
				model.addAttribute("CustomerCategoryId3", sysassignment.getCustomerCategoryId3());
				model.addAttribute("CustomerCategoryId4", sysassignment.getCustomerCategoryId4());
				model.addAttribute("CustomerCategoryId5", sysassignment.getCustomerCategoryId5());*/
			}
			// session.setAttribute("rxMasterId",
			// Integer.valueOf(theRolodexNumber));
			/* session.setAttribute("rolodexName", theCustomerName); */
			model.addAttribute("rxMasterDetails", aRxmaster);
		}
		return aPage;
	}

	@RequestMapping(value = "/purchase_order", method = RequestMethod.GET)
	public String getPurchaseOrderPage(HttpServletRequest request) {
		String aPage = "vendor_po";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String getOrdersPage(HttpServletRequest request) {
		String aPage = "orders";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/submittalpage", method = RequestMethod.GET)
	public String getsubmittalPage(HttpServletRequest request) {
		String aPage = "submittalpage";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/invoice", method = RequestMethod.GET)
	public String getinvoicelPage(HttpServletRequest request) {
		String aPage = "invoice";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/bills", method = RequestMethod.GET)
	public String getbillsPage(HttpServletRequest request) {
		String aPage = "bills";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "rolodexList", method = RequestMethod.GET)
	public String getrolodexPage(ModelMap model, HttpServletRequest request,
			HttpSession session, HttpServletResponse response)
			throws IOException {
		String aPage = "rolodexList";
		Sysinfo sysinfo=null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			sysinfo=sysservice.getRoldexCategories(1);
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				if(sysinfo!=null){
					model.addAttribute("Category1Desc", sysinfo.getRxMasterCategory1desc());
					model.addAttribute("Category2Desc", sysinfo.getRxMasterCategory2desc());
					model.addAttribute("Category3Desc", sysinfo.getRxMasterCategory3desc());
					model.addAttribute("Category4Desc", sysinfo.getRxMasterCategory4desc());
					model.addAttribute("Category5Desc", sysinfo.getRxMasterCategory5desc());
					model.addAttribute("Category6Desc", sysinfo.getRxMasterCategory6desc());
					model.addAttribute("Category7Desc", sysinfo.getRxMasterCategory7desc());
					model.addAttribute("Category8Desc", sysinfo.getRxMasterCategory8desc());
				}
			}
			
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "rolodex/rolodexcontacts", method = RequestMethod.GET)
	public String getrolodexContactsPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			ModelMap model, HttpServletRequest request) {
		String aPage = "rolodex/rolodexcontacts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Rxmaster aRxmaster = null;
		Rxaddress aRxaddress = null;
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setRxMasterDetails(theRolodexNumber);
			model.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("rxAddressDetails", aRxaddress);
		}
		return aPage;
	}

	@RequestMapping(value = "rolodex/rolodexjournal", method = RequestMethod.GET)
	public String getrolodexJournalPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			HttpServletRequest theRequest) {
		String aPage = "rolodex/rolodexjournal";
		if (!SessionConstants.checkSessionExist(theRequest)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "vendorContacts", method = RequestMethod.GET)
	public String getvendorContactsPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			ModelMap model, HttpServletRequest request) {
		String aPage = "vendorContacts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Rxmaster aRxmaster = null;
		Rxaddress aRxaddress = null;
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setRxMasterDetails(theRolodexNumber);
			model.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("rxAddressDetails", aRxaddress);
		}
		return aPage;
	}

	@RequestMapping(value = "architectContacts", method = RequestMethod.GET)
	public String getarchitectContactsPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			ModelMap model, HttpServletRequest request) {
		String aPage = "architectContacts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Rxmaster aRxmaster = null;
		Rxaddress aRxaddress = null;
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setRxMasterDetails(theRolodexNumber);
			model.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("rxAddressDetails", aRxaddress);
		}
		return aPage;
	}

	@RequestMapping(value = "customer/customerContacts", method = RequestMethod.GET)
	public String getCustomerContactsPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			ModelMap model, HttpSession session, HttpServletRequest request) {
		String aPage = "customer/customerContacts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		Rxmaster aRxmaster = null;
		Rxaddress aRxaddress = null;
		CoTaxTerritory aCotaxterritory = new CoTaxTerritory();
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setRxMasterDetails(theRolodexNumber);
			model.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			int CoTaxTerritoryId=0;
			if(aRxaddress!=null && aRxaddress.getCoTaxTerritoryId()!=null){
				CoTaxTerritoryId=aRxaddress.getCoTaxTerritoryId();
			}
			aCotaxterritory = itsCustomerService.getCotaxterritory(CoTaxTerritoryId);
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("rxAddressDetails", aRxaddress);
			model.addAttribute("coTaxTerritory", aCotaxterritory);
			
			List<?> aCustomer = itsRolodexService.getContacts(theRolodexNumber);
			List<?> aCuMastertype = jobService.getCuMasterTypeForBidList();
			model.addAttribute("cuMastertype", aCuMastertype);
			model.addAttribute("bankAccounts", aCustomer);
			
		}
		return aPage;
	}

	@RequestMapping(value = "employee/employeeContacts", method = RequestMethod.GET)
	public String getEmployeeContactsPage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			ModelMap model, HttpServletRequest request) {
		String aPage = "employee/employeeContacts";
		Rxaddress aRxaddress = null;
		Rxmaster aRxmaster = null;
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setRxMasterDetails(theRolodexNumber);
			model.remove(aRxaddress);
			setRxAddressDetails(theRolodexNumber);
			aRxmaster = getRxMasterDetails();
			aRxaddress = getRxAddressDetails();
			model.addAttribute("rxMasterDetails", aRxmaster);
			model.addAttribute("rxAddressDetails", aRxaddress);
		}
		return aPage;
	}

	@RequestMapping(value = "/submittal_General", method = RequestMethod.GET)
	public String getSubmittalGeneralPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session, ModelMap model, HttpServletRequest theRequest,
			HttpServletResponse theResponse) throws JobException, IOException {
		String aSubmittalBy = null;
		String aSingedBy = null;
		Cumaster aCumaster = null;
		String aPage = "submittal_General";
		try {
			if (!SessionConstants.checkSessionExist(theRequest)) {
				aPage = "welcome";
			}
			setJomasterDetails(theJobNumber,joMasterID);
			Jomaster aJomaster = getJomasterDetails();
			setJoSubmittalDetails(theJobNumber,joMasterID);
			Josubmittalheader aJosubmittalheader = getJoSubmittalDetails();
			if (aJosubmittalheader != null) {
				if (aJosubmittalheader.getSubmittalById() != null) {
					aSubmittalBy = (String) jobService
							.getSubmittalName(aJosubmittalheader
									.getSubmittalById());
				}
				if (aJosubmittalheader.getSignedById() != null) {
					aSingedBy = (String) jobService
							.getSignedName(aJosubmittalheader.getSignedById());
				}
			}
			if (aJomaster.getRxCustomerId() != null) {
				aCumaster = new Cumaster();
				try {
					aCumaster = jobService.getSingleCuMasterDetails(aJomaster
							.getRxCustomerId());
				} catch (JobException e) {
					logger.error(e.getMessage(), e);
					theResponse.sendError(e.getItsErrorStatusCode(),
							e.getMessage());
				}
			}
			model.addAttribute("joMasterDetails", aJomaster);
			model.addAttribute("joSubmittalDetails", aJosubmittalheader);
			model.addAttribute("submittalName", aSubmittalBy);
			model.addAttribute("signedName", aSingedBy);
			model.addAttribute("cuMasterDetails", aCumaster);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	public Jomaster getJomasterDetails() {
		return jomasterDetails;
	}

	
	
	public void setJomasterDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.jomasterDetails = jobService.getSingleJobDetails(jobNumber,joMasterID);
	}
	
	public Josubmittalheader getJoSubmittalDetails() {
		return joSubmittalDetails;
	}

	private void setJoSubmittalDetails(String jobNumber,Integer joMasterID) throws JobException {
		this.joSubmittalDetails = jobService.getSingleSubmittalDetails(jobNumber,joMasterID);
	}

	@RequestMapping(value = "/submittal_Schedule", method = RequestMethod.GET)
	public String getSubmittalSchedulePage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value="joMasterID", required=false) Integer joMasterID,
			HttpSession session, ModelMap model, HttpServletRequest theRequest,
			HttpServletResponse theResponse) throws JobException, IOException {
		String aSubmittalBy = null;
		String aSingedBy = null;
		Cumaster aCumaster = null;
		String aPage = "submittal_Schedule";
		try {
			if (!SessionConstants.checkSessionExist(theRequest)) {
				aPage = "welcome";
			}
			setJomasterDetails(theJobNumber,joMasterID);
			Jomaster aJomaster = getJomasterDetails();
			setJoSubmittalDetails(theJobNumber,joMasterID);
			Josubmittalheader aJosubmittalheader = getJoSubmittalDetails();
			if (aJosubmittalheader != null) {
				if (aJosubmittalheader.getSubmittalById() != null) {
					aSubmittalBy = (String) jobService
							.getSubmittalName(aJosubmittalheader
									.getSubmittalById());
				}
				if (aJosubmittalheader.getSignedById() != null) {
					aSingedBy = (String) jobService
							.getSignedName(aJosubmittalheader.getSignedById());
				}
			}
			if (aJomaster.getRxCustomerId() != null) {
				aCumaster = new Cumaster();
				try {
					aCumaster = jobService.getSingleCuMasterDetails(aJomaster
							.getRxCustomerId());
				} catch (JobException e) {
					logger.error(e.getMessage(), e);
					theResponse.sendError(e.getItsErrorStatusCode(),
							e.getMessage());
				}
			}
			model.addAttribute("joMasterDetails", aJomaster);
			model.addAttribute("joSubmittalDetails", aJosubmittalheader);
			model.addAttribute("submittalName", aSubmittalBy);
			model.addAttribute("signedName", aSingedBy);
			model.addAttribute("cuMasterDetails", aCumaster);
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/PORelease_Ack", method = RequestMethod.GET)
	public String getPOReleaseAckPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			HttpSession session, ModelMap model, HttpServletRequest theRequest) {
		String aPage = "PORelease_Ack";
		if (!SessionConstants.checkSessionExist(theRequest)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/PORelease_General", method = RequestMethod.GET)
	public String getPOReleaseGeneralPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "rxManufactureID", required = false) Integer therxManufacturerId,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value = "joMasterID", required = false) Integer theJoMasterID,
			HttpSession theSession, ModelMap theModel,
			HttpServletRequest theRequest) throws UserException, CompanyException {
		String aPage = "PORelease_General";
		TsUserSetting aSetting = new TsUserSetting();
		Rxaddress aRxaddress=null;
		
		try {
			if (!SessionConstants.checkSessionExist(theRequest)) {
				aPage = "welcome";
			}
			setItsPrwarehouses((List<Prwarehouse>) jobService.getWareHouse());
			setItsVeshipvia((List<Veshipvia>) jobService.getVeShipVia());
			setItsFreightCharges((List<Vefreightcharges>) jobService
					.getVefreightcharges());
			if (!itsFreightCharges.isEmpty()) {
				theModel.addAttribute("friedCharges", getItsFreightCharges());
			}
			if (aSetting != null) {
				aSetting = userService.getSingleUserSettingsDetails(1);
				theModel.addAttribute("settingDetails", aSetting);
			}
			if (!itsVeshipvia.isEmpty()) {
				theModel.addAttribute("shipVia", getItsVeshipvia());
			}
			theModel.addAttribute("wareHouse", getItsPrwarehouses());
			if (theJobNumber != "") {
				setJomasterDetails(theJobNumber,theJoMasterID);
				Jomaster aJomaster = getJomasterDetails();
				theModel.addAttribute("joMasterDetails", aJomaster);
			}
			if (theJoMasterID != null) {
				setItsPODetails((List<JoCustPO>) jobService
						.getJoCustPODetail(theJoMasterID));
				theModel.addAttribute("custPONoDetails", getItsPODetails());
			}
			
			
			theModel.addAttribute("theNewShiptocusaddress", itsCompanyService.getAllAddress(therxManufacturerId));
			
		} catch (JobException e) {
			logger.error(e.getMessage(), e);
		}
		return aPage;
	}

	@RequestMapping(value = "/PORelease_Lines", method = RequestMethod.GET)
	public String getPOReleaseLinesPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			HttpSession session, ModelMap model, HttpServletRequest theRequest) {
		String aPage = "PORelease_Lines";
		if (!SessionConstants.checkSessionExist(theRequest)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/SO_Kit", method = RequestMethod.GET)
	public String getSOLinesPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			HttpSession session, ModelMap model, HttpServletRequest theRequest) {
		String aPage = "SO_Kit";
		if (!SessionConstants.checkSessionExist(theRequest)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/SO_General", method = RequestMethod.GET)
	public String getSOReleaseGeneralPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value = "cuSOID", required = false) String thecuSOID,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession session, ModelMap model, HttpServletRequest theRequest,
			HttpServletResponse theResponse) throws JobException, IOException,
			CompanyException, UserException {
		if(joMasterID==null){
			joMasterID=0;
		}
		String aPage = "SO_General";
		String tableNameRxMaster = "rxMaster";
		Jomaster aJomaster = null;
		Rxmaster aRxmaster = null;
		Cuso aCuso = null;
		Cuso aCuso1 = new Cuso();
		String	aSysPriSelectQry = null;
		Iterator<?> aIterator = null;
		List<Rxaddress> rxAddressDetails = null;
		
		
		setItsVeshipvia((List<Veshipvia>) jobService.getVeShipVia());
		setItsPrwarehouses((List<Prwarehouse>) jobService.getWareHouse());
		setDivision((List<Codivision>) itsCompanyService.getCompanyDivisions());
		setItsTerms((List<CuTerms>) itsCompanyService.getCuTermsList());
		
		int cusoID = jobService.getCuSOID(theJobNumber);
		/*
		 * if(cusoID == 0) { cusoID = jobService.populatecusoID(); }
		 */
		List<CoTaxTerritory> jobSiteTaxValue = null;
		RxCustomerTabViewBean aRxCustomerTabViewBean = new RxCustomerTabViewBean();
		try {
			if (!SessionConstants.checkSessionExist(theRequest)) {
				aPage = "welcome";
			}
			aJomaster = jobService.getSingleJobDetails(theJobNumber,joMasterID);
			if (!itsVeshipvia.isEmpty())
				model.addAttribute("shipVia", getItsVeshipvia());
			if (!itsDivision.isEmpty())
				model.addAttribute("coDivision", getDivision());
			if (!itsPrwarehouses.isEmpty())
				model.addAttribute("wareHouse", getItsPrwarehouses());
			if (!itsTerms.isEmpty())
				model.addAttribute("cuTerms", getItsTerms());
		
			
			aCuso1.setSonumber(theJobNumber);
			//aJomaster = jobService.getSingleJobDetails(theJobNumber);
			aCuso = jobService.getSingleCuSalesOrderDetails(aCuso1);
			if (aCuso == null && thecuSOID != null && thecuSOID != "")
				aCuso = jobService.getSingleCUSODetails(Integer
						.valueOf(thecuSOID));
			
			
			if (aJomaster != null) {
				try {
					jobSiteTaxValue = (List<CoTaxTerritory>) companyService
							.getCompanyTaxTerritory(
									aJomaster.getCoTaxTerritoryId(),
									tableNameRxMaster);
				} catch (CompanyException e) {
					CompanyException aCompanyException = new CompanyException(e
							.getCause().getMessage(), e);
					throw aCompanyException;
				}
				if (jobSiteTaxValue != null && aJomaster != null) {
					if (!(jobSiteTaxValue.isEmpty())) {
						CoTaxTerritory aTaxterritory = jobSiteTaxValue.get(0);
						model.addAttribute("taxTerritoryID",
								aTaxterritory.getCounty());
						model.addAttribute("taxRate",
								aTaxterritory.getTaxRate());
					}
				} else {
					model.addAttribute("taxTerritoryID", "");
				}
				
			}
			
			if(aJomaster!=null){
			if(aJomaster.getRxCustomerId()!=null){
				Cumaster acumaster=cuMasterService.getCustomerDetails(aJomaster.getRxCustomerId());
				if(acumaster!=null){
					model.addAttribute("defaultWHID", acumaster.getPrWarehouseId());
				}else{
					model.addAttribute("defaultWHID","0");
				}
				
			}else{
				model.addAttribute("defaultWHID","0");
			}
			}
			else{
				model.addAttribute("defaultWHID","0");
			}
			List<Integer> addlist=new ArrayList<Integer>();
			addlist.add(2014002004);//"CheckcreditlimitinQuickBook");
			addlist.add(2014002005);//"RequireDivisioninSalesOrderOutsideofJob"
			addlist.add(2014005023);//"UseCustomersCreditLimitwhencreatingJobs");
			
			Session aSession=null;
//			ArrayList<Sysvariable> aQueryList = new ArrayList<Sysvariable>();
			int sysvar = 0;
			try{
				aSession=itsSessionFactory.openSession();
				for(int i=0; i<addlist.size(); i++)
				{
					aSysPriSelectQry = "SELECT sysVariableID,ValueLong,ValueCurrency,ValueString,ValueDate,ValueMemo FROM sysVariable WHERE sysVariableID="+addlist.get(i);
					Query aQuery = aSession.createSQLQuery(aSysPriSelectQry);
					aIterator = aQuery.list().iterator();
			
					if(aIterator.hasNext()) {
						Object[] aObj = (Object[])aIterator.next();
						sysvar = (Integer) aObj[1];
						if(i==0)
							model.addAttribute("CheckcreditlimitinQuickBook", sysvar);
						if(i==1)
							model.addAttribute("chk_cusReqDivinSalOrdYes", sysvar);
						if(i==2)
							model.addAttribute("chkUserCustomerCreditYN", sysvar);
					}
				}
			} catch(Exception e) {
				logger.error(e.getMessage(),e);
				UserException aUserException = new UserException(e.getCause().getMessage(), e);
				throw aUserException;
			} 
			finally {
				aSession.flush();
				aSession.close();
			}
			
//			ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
//			int i=0;
//			for(Sysvariable theSysvariable:sysvariablelist){
//				//Inventory Settings
//				if(i==0){
//					logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
//					model.addAttribute("cusCreLiminQuickBookYes", theSysvariable.getValueLong());
//				}
//				if(i==2)
//				{
//					logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
//					model.addAttribute("chkUserCustomerCreditYN", theSysvariable.getValueLong());
//				}
//				i++;
//			}
			//logger.info("JoReleaseID::"+aCuso.getJoReleaseId()==null?0:aCuso.getJoReleaseId());
			
			model.addAttribute("joMasterDetail", aJomaster);
			model.addAttribute("joMasterDetails", aJomaster);
			model.addAttribute("rxMasterDetail", aRxmaster);
			model.addAttribute("CuSoDetail", aCuso);
			model.addAttribute("customerTabForm", aRxCustomerTabViewBean);
			model.addAttribute("cusoID", cusoID);
			model.addAttribute("prWareHouse", jobService.getWareHouse());
			model.addAttribute("wareHouse", jobService.getWareHouse());
			model.addAttribute("theNewShiptocusaddress", itsCompanyService.getAllAddress(aJomaster.getRxCustomerId()));
		
			
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		finally {
			tableNameRxMaster = null;
			aJomaster = null;
			aRxmaster = null;
			aCuso = null;
			aCuso1 = null;
			jobSiteTaxValue = null;
			aRxCustomerTabViewBean = null;
			aSysPriSelectQry = null;
			aIterator = null;
		}
		return aPage;
	}

	@RequestMapping(value = "/getUserDefaults", method = RequestMethod.POST)
	public  @ResponseBody Map<String, Object> getDefaultsDetails(
			 HttpServletRequest theRequest,HttpSession theSession) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserDefault newSysUserDefault = null;
		UserBean aUserBean = null;
		try {			
			
			aUserBean = (UserBean) theSession.getAttribute(SessionConstants.USER);
			newSysUserDefault = userService.getSysUserDefault(aUserBean.getUserId());
			if(newSysUserDefault==null){
				newSysUserDefault = new SysUserDefault();
				newSysUserDefault.setCoDivisionID(0);
				newSysUserDefault.setWarehouseID(0);
			}
			ArrayList<Codivision> alCodivision = (ArrayList<Codivision>)itsCompanyService.getCompanyDivisions();
			ArrayList<Prwarehouse> alWareHouse = (ArrayList<Prwarehouse>)jobService.getWareHouseForDefaults();
			
			map.put("divisions", alCodivision);
			map.put("wareHouse", alWareHouse);
			map.put("divisionID", newSysUserDefault.getCoDivisionID());
			map.put("warehouseID", newSysUserDefault.getWarehouseID());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}finally{
			newSysUserDefault = null;
			aUserBean = null;
		}
		return map;		
	
	}
	
	@RequestMapping(value = "/SO_Lines", method = RequestMethod.GET)
	public String getSOReleaseLinesPage(
			@RequestParam(value = "jobNumber", required = false) String theJobNumber,
			@RequestParam(value = "jobName", required = false) String theJobName,
			@RequestParam(value = "jobCustomer", required = false) String theJobCustomer,
			@RequestParam(value = "joMasterID", required = false) Integer joMasterID,
			HttpSession session, ModelMap model, HttpServletRequest theRequest,
			HttpServletResponse theResponse) throws JobException, IOException {
		String aPage = "SO_Lines";
		Jomaster aJomaster = null;
		String tableNameRxMaster = "rxMaster";
		List<CoTaxTerritory> jobSiteTaxValue = null;
		try {
			aJomaster = jobService.getSingleJobDetails(theJobNumber,joMasterID);
			if (aJomaster != null) {
				try {
					jobSiteTaxValue = (List<CoTaxTerritory>) companyService
							.getCompanyTaxTerritory(
									aJomaster.getCoTaxTerritoryId(),
									tableNameRxMaster);
				} catch (CompanyException e) {
					CompanyException aCompanyException = new CompanyException(e
							.getCause().getMessage(), e);
					throw aCompanyException;
				}
				if (jobSiteTaxValue != null && aJomaster != null) {
					if (!(jobSiteTaxValue.isEmpty())) {
						CoTaxTerritory aTaxterritory = jobSiteTaxValue.get(0);
						model.addAttribute("taxTerritoryID",
								aTaxterritory.getCounty());
						model.addAttribute("taxRate",
								aTaxterritory.getTaxRate());
					}
				} else {
					model.addAttribute("taxTerritoryID", "");
				}
				model.addAttribute("QuotedPricePrMasterID", salesServices.getQuotedPricePrMasterID());
			}
			if (!SessionConstants.checkSessionExist(theRequest)) {
				aPage = "welcome";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "employe", method = RequestMethod.GET)
	public String getrolodexEmployeePage(
			@RequestParam(value = "rolodexID", required = false) String theRolodexNumber,
			@RequestParam(value = "name", required = false) String theEmployeeName,
			ModelMap model, HttpServletRequest request) throws JobException, CompanyException {
		String aPage = "employe";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		model.addAttribute("warehouse", itsCompanyService.getCompanyDivisions());
		if (theRolodexNumber != null && theRolodexNumber != "") {
			setCommissionsDetails(theRolodexNumber);
			Emmaster aEmmaster = getCommissionsDetails();
			model.addAttribute("enMasterDetails", aEmmaster);
		}
		return aPage;
	}

	@RequestMapping(value = "employ_view", method = RequestMethod.GET)
	public String getrolodexEmployeePage(
			@RequestParam(value = "rolodexID", required = false) Integer theRolodexNumber,
			HttpServletRequest theRequest, ModelMap theModel,
			HttpServletResponse theResponse) throws IOException,
			CompanyException, CustomerException {
		logger.debug("Received request to show EmployeePage");
		String aPage = "employ_view";
		JobCustomerBean aJobCustomerBean = new JobCustomerBean();
		Cumaster cumaster = null;
		String assignedSalesRep = null;
		String assignedCSR = null;
		String assignedSalesMGR = null;
		String assignedEngineers = null;
		String assignedProjMgr = null;
		
		if (!SessionConstants.checkSessionExist(theRequest)) {
			aPage = "welcome";
		}
		RxMasterCategoryView arxmstrctg2=cuMasterService.getrxMasterCategoryView(theRolodexNumber);
		if (theRolodexNumber != null) {
			Integer rxMasterId = theRolodexNumber;
			setCustomerRecord(rxMasterId);
			theModel.clear();
			theModel.addAttribute(SessionConstants.JOB_GRID_OBJ,
					aJobCustomerBean);
			cumaster = getCustomerRecord();
			theModel.addAttribute("customerRecord", arxmstrctg2);
			if (customerRecord != null) {
				if (customerRecord.getCuAssignmentId0() != null) {
					assignedSalesRep = (String) salesServices
							.getAssignedEmployeeName(
									customerRecord.getCuAssignmentId0(),
									tableNameUserLogin);
				}
				if (customerRecord.getCuAssignmentId1() != null) {
					assignedCSR = (String) salesServices
							.getAssignedEmployeeName(
									customerRecord.getCuAssignmentId1(),
									tableNameUserLogin);
				}
				if (customerRecord.getCuAssignmentId2() != null) {
					assignedSalesMGR = (String) salesServices
							.getAssignedEmployeeName(
									customerRecord.getCuAssignmentId2(),
									tableNameUserLogin);
				}
				if (customerRecord.getCuAssignmentId3() != null) {
					assignedEngineers = (String) salesServices
							.getAssignedEmployeeName(
									customerRecord.getCuAssignmentId3(),
									tableNameUserLogin);
				}
				if (customerRecord.getCuAssignmentId4() != null) {
					assignedProjMgr = (String) salesServices
							.getAssignedEmployeeName(
									customerRecord.getCuAssignmentId4(),
									tableNameUserLogin);
				}

				theModel.addAttribute("AssignedSalesRep", assignedSalesRep);
				theModel.addAttribute("AssignedCSRs", assignedCSR);
				theModel.addAttribute("AssignedSalesMGRs", assignedSalesMGR);
				theModel.addAttribute("AssignedProjManagers", assignedProjMgr);
				theModel.addAttribute("AssignedEngineers", assignedEngineers);
				theModel.addAttribute("customerMasterObj", getCustomerRecord());
			}
		}
		return aPage;
	}

	@RequestMapping(value = "vendor", method = RequestMethod.GET)
	public String getRolodexVendorPage(HttpServletRequest request) {
		String aPage = "vendor";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "taxterritories", method = RequestMethod.GET)
	public String getTaxterritoriesPage(
			@RequestParam(value = "taxTerritoryId", required = false) Integer theTaxTerritoryId,
			@RequestParam(value = "county", required = false) String theCountyName,
			HttpServletRequest request, ModelMap model, HttpSession session,
			HttpServletResponse response) throws IOException, CompanyException {
		String aPage = "taxterritories";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		CoTaxTerritory aCotaxterritory = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				model.addAttribute("coTaxDetails", aCotaxterritory);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/userDetails", method = RequestMethod.GET)
	public String getUserDetailsPage(
			@RequestParam(value = "userLoginId", required = false) Integer theUserLoginId,
			@RequestParam(value = "loginName", required = false) String thetheUserLoginName,
			ModelMap model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JobException {
		String aPage = "userDetails";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				
				TsUserGroup agroupDefaults=new TsUserGroup();
				for(int k=1;k<9;k++){
					agroupDefaults=sysservice.getgroupDefaults(k);
					if(agroupDefaults==null){
						agroupDefaults=new TsUserGroup();
					}
					model.addAttribute("groupDefaults"+k, agroupDefaults);
				}
			}
			// getUserDetailsFrom(theUserLoginId, thetheUserLoginName, session,
			// model, request);
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/taxTerritoryDetails", method = RequestMethod.GET)
	public String getTaxTerritoryDetailsPage(
			@RequestParam(value = "taxID", required = false) Integer theTaxTerritoryId,
			@RequestParam(value = "county", required = false) String theCountyName,
			ModelMap model, HttpSession session, HttpServletRequest request,
			HttpServletResponse response) throws IOException, CompanyException {
		String aPage = "taxTerritoryDetails";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		CoTaxTerritory aCotaxterritory = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("coTaxDetails", aCotaxterritory);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/userDetailsMainFrom", method = RequestMethod.GET)
	public String getUserDetailsFrom(
			@RequestParam(value = "userLoginId", required = false) Integer theUserID,
			@RequestParam(value = "userName", required = false) String theLoginName,
			HttpSession session, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String aPage = "userDetailsMainFrom";
		TsUserLogin aUserLoginDetails = null;
		TsUserLogin aSysAdmin = null;
		try {
			if (!SessionConstants.checkSessionExist(request)) {
				aPage = "welcome";
			}
			aUserLoginDetails = userService.getSingleUserDetails(theUserID);
			model.addAttribute("userDetails", aUserLoginDetails);
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aSysAdmin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aSysAdmin.getSystemAdministrator());
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/userGroupDetails", method = RequestMethod.GET)
	public String getUserGroupDetails(
			@RequestParam(value = "userLogin", required = false) Integer theUserID,
			HttpSession session, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JobException {
		String aPage = "userGroupDetails";
		TsUserLogin aUserLoginDetails = null;
		TsUserLogin aSysAdmin = null;
		try {
			if (!SessionConstants.checkSessionExist(request)) {
				aPage = "welcome";
			}
			aUserLoginDetails = userService.getSingleUserDetails(theUserID);
			model.addAttribute("userDetails", aUserLoginDetails);
			UserBean aUserBean;
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aSysAdmin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aSysAdmin.getSystemAdministrator());
			}
			
			model.addAttribute("SalesReps", aUserBean.isSalesRep());
			model.addAttribute("CSRs", aUserBean.isCSR());
			model.addAttribute("Employees", aUserBean.isEmployee());
			model.addAttribute("Adminstrative", aUserBean.isAdmin());
			model.addAttribute("Warehouse", aUserBean.isWareHouse());
			
			TsUserGroup agroupDefaults=new TsUserGroup();
			TsUserGroupLink aUserGroupLink = new TsUserGroupLink();
			for(int k=1;k<9;k++){
				agroupDefaults=sysservice.getgroupDefaults(k);
				aUserGroupLink=sysservice.getUserGroupLink(theUserID, k);
				if(agroupDefaults==null){
					agroupDefaults=new TsUserGroup();
				}
				if(aUserGroupLink==null){
					aUserGroupLink=new TsUserGroupLink();
				}
				model.addAttribute("groupDefaults"+k, agroupDefaults);
				model.addAttribute("aUserGroupLink"+k, aUserGroupLink);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/userPermissionDetails", method = RequestMethod.GET)
	public String getUserPermissionDetails(
			@RequestParam(value = "userLogin", required = false) Integer theUserID,
			HttpSession session, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String aPage = "userPermissionDetails";
		TsUserLogin aUserLogin = null;
		try {
			if (!SessionConstants.checkSessionExist(request)) {
				aPage = "welcome";
			}
			aUserLogin = userService.getSingleUserDetails(theUserID);
			setAccessModule((List<TsAccessModule>) userService
					.getAccessModuleDetails());
			model.addAttribute("userAccessModule", getAccessModule());
			model.addAttribute("userDetails", aUserLogin);
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/userEmailDetails", method = RequestMethod.GET)
	public String getUserEmailDetails(
			@RequestParam(value = "userLogin", required = false) Integer theUserID,
			HttpSession session, ModelMap model, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String aPage = "userEmailDetails";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLoginDetails = null;
		TsUserLogin aSysAdmin = null;
		UserBean aUserBean = null;
		try {
			aUserLoginDetails = userService.getSingleUserDetails(theUserID);
			model.addAttribute("userDetails", aUserLoginDetails);
			aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aSysAdmin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aSysAdmin.getSystemAdministrator());
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/bidDateReport", method = RequestMethod.GET)
	public String getReportCriteriaPage(HttpServletRequest request) {
		String aPage = "bidDateReport";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/foreCastBySalesRep", method = RequestMethod.GET)
	public String getForeCastBySalesRepPage(HttpServletRequest request) {
		String aPage = "foreCastBySalesRep";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/foreCastByManufacturer", method = RequestMethod.GET)
	public String getForeCastByManufacturerPage(HttpServletRequest request) {
		String aPage = "foreCastByManufacturer";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/jobReports", method = RequestMethod.GET)
	public String getBidDateReportPage(HttpServletRequest request) {
		String aPage = "jobReports";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/vendorbills", method = RequestMethod.GET)
	public String vendorbills(ModelMap theModel, HttpServletRequest request,
			HttpServletResponse response) throws IOException, JobException, CompanyException {
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		try {
			ArrayList<MoAccount> aBankAccountsList = itsBankingService
					.getBankAccountDetails();
			theModel.addAttribute("bankAccountsList", aBankAccountsList);
			theModel.addAttribute("coAccount", (List<Coaccount>)chartOfAccountsService.getAccountNumber());
			theModel.addAttribute("shipVia",(List<Veshipvia>) jobService.getVeShipVia());
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		theModel.addAttribute("currentDate", new Date());
		return "billpay";
	}

/*	@RequestMapping(value = "/customerpayments", method = RequestMethod.GET)
	public String customerPayments(
			ModelMap model, HttpServletRequest request) {
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		return "customerpayments";
	}
	*/
	@RequestMapping(value = "/customerpayments", method = RequestMethod.GET)
	 public String customerPayments(
	   @RequestParam(value = "rxCustomerId", required = false) Integer rxCustomerId,
	   @RequestParam(value = "customer", required = false) String customer,
	   @RequestParam(value = "checkNo", required = false) String checkNo,
	   @RequestParam(value = "recieptID", required = false) String recieptID,
	   @RequestParam(value = "Date", required = false) String date,
	   @RequestParam(value = "memo", required = false) String memo,
	   @RequestParam(value = "recieptTypeId", required = false) String recieptTypeId,
	   @RequestParam(value = "amount", required = false) Double amount,
	   @RequestParam(value = "reversePaymentStatus", required = false) Boolean reversePaymentStatus,
	   ModelMap model, HttpServletRequest request) {
	  if (!SessionConstants.checkSessionExist(request)) {
	   return "welcome";
	  }
	  Rxmaster aRxmaster = new Rxmaster();
	  CustomerPaymentBean cuPaymentBean = new CustomerPaymentBean();
	  try {
	   
	   if(rxCustomerId!=null && rxCustomerId!=0)
	   {
	   aRxmaster = jobService.getSingleRxMasterDetails(rxCustomerId);
	   cuPaymentBean = jobService.getCuRecieptDetials(Integer.parseInt(recieptID));   
	   model.addAttribute("rxCustomerId", rxCustomerId);
	   model.addAttribute("customer", cuPaymentBean.getCustomer());
	   model.addAttribute("checkNo", cuPaymentBean.getReference());
	   model.addAttribute("amount", cuPaymentBean.getAmount());
	   model.addAttribute("rxMAsterDetails", aRxmaster);
	   model.addAttribute("recieptID", recieptID);
	   model.addAttribute("Date", cuPaymentBean.getReceiptDate());
	   model.addAttribute("memo", cuPaymentBean.getMemo());
	   model.addAttribute("recieptTypeId", cuPaymentBean.getCuReceiptTypeID());
	   model.addAttribute("reversePaymentStatus",cuPaymentBean.isReversePaymentStatus());
	   }
	   else
	   {
	   aRxmaster.setName("");
	   aRxmaster.setRxMasterId(0);
	   model.addAttribute("rxCustomerId", "0");
	   model.addAttribute("customer", "");
	   model.addAttribute("checkNo", "");
	   model.addAttribute("amount", "0.00");
	   model.addAttribute("rxMAsterDetails", aRxmaster);
	   model.addAttribute("recieptID", "0");
	   model.addAttribute("Date", new Date());
	   model.addAttribute("memo", "");
	   model.addAttribute("recieptTypeId", "0"); 
	   model.addAttribute("reversePaymentStatus",false);
	   }
	   
	  } catch (Exception e) {

	  }

	  return "customerpayments";
	 }
	
	@RequestMapping(value = "/customerunappliedpayments", method = RequestMethod.GET)
	public String customerunappliedpayments(
			@RequestParam(value = "rxCustomerId", required = false) Integer rxCustomerId,
			@RequestParam(value = "customer", required = false) String customer,
			@RequestParam(value = "checkNo", required = false) String checkNo,
			@RequestParam(value = "recieptID", required = false) String recieptID,
			@RequestParam(value = "Date", required = false) String date,
			@RequestParam(value = "memo", required = false) String memo,
			@RequestParam(value = "recieptTypeId", required = false) String recieptTypeId,
			@RequestParam(value = "amount", required = false) Double amount,
			ModelMap model, HttpServletRequest request) {
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		Rxmaster aRxmaster = new Rxmaster();
		CustomerPaymentBean cuPaymentBean = new CustomerPaymentBean();
		try {
			
			aRxmaster = jobService.getSingleRxMasterDetails(rxCustomerId);
			cuPaymentBean = jobService.getCuRecieptDetials(Integer.parseInt(recieptID));			
			model.addAttribute("rxCustomerId", rxCustomerId);
			model.addAttribute("customer", cuPaymentBean.getCustomer());
			model.addAttribute("checkNo", cuPaymentBean.getReference());
			model.addAttribute("amount", cuPaymentBean.getAmount());
			model.addAttribute("rxMAsterDetails", aRxmaster);
			model.addAttribute("recieptID", recieptID);
			model.addAttribute("Date", cuPaymentBean.getReceiptDate());
			model.addAttribute("memo", cuPaymentBean.getMemo());
			model.addAttribute("recieptTypeId", cuPaymentBean.getCuReceiptTypeID());
			model.addAttribute("unappliedamount", amount);
			
		} catch (Exception e) {

		}

		return "customerunappliedpayments";
	}

	@RequestMapping(value = "/customerpaymentlist", method = RequestMethod.GET)
	public String customerPaymentlist(HttpServletRequest request,
			ModelMap model, HttpServletResponse aResponse) throws IOException {
		List<Cureceipttype> recieptType = null;
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		try {
			recieptType = itsCustomerPaymentsService.getRecieptType();
		} catch (CustomerException e) {
			logger.error(e.getMessage(), e);
			aResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		model.addAttribute("pamenyType", recieptType);
		return "customerpaymentlist";
	}
	
	@RequestMapping(value = "/customerunappliedpaymentlist", method = RequestMethod.GET)
	public String customerunappliedpaymentlist(HttpServletRequest request,
			ModelMap model, HttpServletResponse aResponse) throws IOException {
		List<Cureceipttype> recieptType = null;
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		return "customerunappliedpaymentlist";
	}

	/***
	 * @RequestMapping(value="/excelimport",method = RequestMethod.GET) public
	 *                                             String importfromExcel(
	 *                                             HttpServletRequest request) {
	 *                                             return "excelimport"; }
	 */

	@RequestMapping(value = "/employeeCommissions", method = RequestMethod.GET)
	public String employeeCommissions(ModelMap theModel,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession theSession) throws IOException {
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		UserBean aUserBean = null;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			theModel.addAttribute("userDetails", aUserBean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "employee/commissions";
	}

	@RequestMapping(value = "/shipViaDetail", method = RequestMethod.GET)
	public String ShipVia(ModelMap theModel, HttpServletRequest request,
			HttpServletResponse response, HttpSession theSession)
			throws IOException {
		if (!SessionConstants.checkSessionExist(request)) {
			return "welcome";
		}
		UserBean aUserBean = null;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			theModel.addAttribute("userDetails", aUserBean);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "shipVia";
	}

	@RequestMapping(value = "/chartofaccounts", method = RequestMethod.GET)
	public String getChartofAccountsPage(ModelMap modal,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession theSession, ModelMap model) throws IOException {
		String aPage = "company/chart_of_accounts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		ArrayList<ChSegments> liSegment= new ArrayList<ChSegments>();
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				
				model.addAttribute("chsegments", userService.getchSegments());
				
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/bankingAccounts", method = RequestMethod.GET)
	public String getBankingAccountsPage(
			@RequestParam(value = "moAccountID", required = false) Integer theMoAccountID,
			@RequestParam(value = "description", required = false) String theBankingName,
			@RequestParam(value = "token", required = false) String theToken,
			ModelMap modal, HttpServletRequest request,
			HttpServletResponse response, HttpSession theSession, ModelMap model)
			throws IOException, BankingException, CompanyException {
		String aPage = "banking/bankingList";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				setItsCoAccount((List<Coaccount>) chartOfAccountsService
						.getAccountNumber());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				model.addAttribute("coAccounts", getItsCoAccount());
				setItsCoAccount((List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown());
				BigDecimal payableApprove = chartOfAccountsService.getPayableApproved();
				model.addAttribute("payableApproved",payableApprove);
				model.addAttribute("coAccountsDetails",getItsCoAccount());
				theSession.setAttribute(SessionConstants.BANKING_DETAILS,getBankingList());
			}
			if (theToken != null && theToken.equalsIgnoreCase("new")) {
				theSession.removeAttribute(SessionConstants.BANKING_GRID_OBJ);
				return aPage;
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/transactionRegister", method = RequestMethod.GET)
	public String getTransactionRegister(ModelMap modal,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession theSession, ModelMap model) throws IOException,
			BankingException, CompanyException {
		String aPage = "banking/transactionRegister";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			setItsCoAccount((List<Coaccount>) chartOfAccountsService
					.getAccountNumber());
			setBankingList((List<MoAccount>) itsBankingService
					.getBankAccountDetails());
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userDetails", aUserLogin);
				model.addAttribute("coAccountsDetails", getItsCoAccount());
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/ledger", method = RequestMethod.GET)
	public String getLedgerPage(ModelMap modal, HttpServletRequest request,
			HttpServletResponse response, HttpSession theSession, ModelMap model)
			throws IOException, BankingException, CompanyException {
		String aPage = "company/ledger";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/companyjournals", method = RequestMethod.GET)
	public String companyjournals(ModelMap modal, HttpServletRequest request) {
		String aPage = "company_journals";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}

	@RequestMapping(value = "/writechecks", method = RequestMethod.GET)
	public String writechecks(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "banking/writechecks";
		Boolean billExists = itsBankingService.billExistsToPay();
		setBankingList((List<MoAccount>) itsBankingService
				.getBankAccountDetails());
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";

			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		modal.addAttribute("bankAccounts", getBankingList());
		modal.addAttribute("billExists", billExists);
		return aPage;
	}

	@RequestMapping(value = "/reconcileAccounts", method = RequestMethod.GET)
	public String reconcileAccounts(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "banking/reconcileAccounts";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");

		}
		/*List<?> aBankingAccountsList = bankingAccountsService
				.getBankAccountList(0, 0);*/
		setBankingList((List<MoAccount>) itsBankingService
				.getBankAccountDetails());
		
		modal.addAttribute("bankAccounts", getBankingList());
		return aPage;
	}

	@RequestMapping(value = "/reconcileSelectedShow", method = RequestMethod.GET)
	public @ResponseBody
	ModelAndView reconcileSelectedShow(
			@RequestParam(value = "showReconcilingDetail", required = false) boolean sendDetail,
			@RequestParam(value = "moAccountID", required = true) Integer moAccountID,
			@RequestParam(value = "openbalance", required = false) BigDecimal theOpeningBalance,
			@RequestParam(value = "endbalance", required = false) BigDecimal theEndingBalance,
			HttpServletResponse theResponse) throws IOException {
		MoAccount aMoAccount;

		ModelAndView theView = new ModelAndView(
				"banking/reconcileAccountDetails");
		if (sendDetail) {
			theView = new ModelAndView("banking/reconcileView");
		}
		try {
			aMoAccount = bankingAccountsService
					.getSingleBankingDetails(moAccountID);
			theView.addObject("moaccount", aMoAccount);
			theView.addObject("openbalance", aMoAccount.getOpenBalance());
			if (sendDetail) {
			theView.addObject("openbalance", theOpeningBalance);
			}
			theView.addObject("endbalance", theEndingBalance);
		} catch (BankingException e) {
			logger.error(e.getMessage(), e);
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
			return null;
		}
		return theView;
	}

	// @RequestMapping(value = "/reconcileSelectedShow", method =
	// RequestMethod.GET)
	// public String reconcileSelectedShow(ModelMap modal, HttpServletRequest
	// request) throws BankingException {
	// String aPage = "banking/reconcileAccountDetails";
	// if (!SessionConstants.checkSessionExist(request)) {
	// aPage = "welcome";
	// modal.addAttribute("user-message", "User Name Or Password are Invalid.");
	//
	// }

	// MoAccount aBankDetail=
	// bankingAccountsService.getSingleBankingDetails(Integer.parseInt(request.getParameter("bankId")));

	// modal.addAttribute("bankAccounts",aBankDetail);
	// return aPage;
	// }
	@RequestMapping(value = "/financeCharges", method = RequestMethod.GET)
	public String financeCharges(ModelMap modal, HttpServletRequest request)
			throws EmployeeException {
		String aPage = "financeCharges";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");

		}
		List<?> aCustomer = itsEmployeeService.getEmployees(0, 1000, 0);
		modal.addAttribute("bankAccounts", aCustomer);
		return aPage;
	}
//
	@RequestMapping(value = "/taxAdjustments", method = RequestMethod.GET)
	public String taxAdjustments(ModelMap modal, HttpServletRequest request)
			throws EmployeeException, CompanyException {
		//String aPage = "taxAdjustments";
		String aPage = "taxAdjustmentsNewUI";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");

		}
		
		List<?> aChartsAccountsList = chartOfAccountsService.getChartsAccountsListforTax(0, 0);
		modal.addAttribute("bankAccounts", aChartsAccountsList);
		return aPage;
	}
	
	//creditDebitMemos
	
	@RequestMapping(value = "/creditDebitMemos", method = RequestMethod.GET)
	public String creditDebitMemos(ModelMap modal, HttpServletRequest request)
			throws EmployeeException, CompanyException, UserException {
		String aPage = "creditDebitMemos";
		
		System.out.println("creditDebitMemos");
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
			
			System.out.println("creditDebitMemos");
	

		}
		
		//GlAccount
		modal.addAttribute("glaccount",chartOfAccountsService.getListOfAccountsFordropdown());
		//Division
		modal.addAttribute("division",itsCompanyService.getCompanyDivisions());
		//Salesman
		modal.addAttribute("salesman", userService.getAllUserList());
		//Tax Territory  
		modal.addAttribute("taxterritory",taxTerritoriesService.getTaxTerritoryList());
		return aPage;
	}
	@RequestMapping(value = "/reprintchecks", method = RequestMethod.GET)
	public String reprintchecks(ModelMap modal, HttpServletRequest request) throws BankingException {
		String aPage = "banking/reprintchecks";
		logger.info("/reprintchecks");
		Boolean billExists = itsBankingService.billExistsToPay(); 
		setBankingList((List<MoAccount>) itsBankingService.getBankAccountDetails());
		ArrayList<Motransaction> moAccouuntList = (ArrayList<Motransaction>)itsBankingService.getChecksLists(0,"",(short) 0);
		BigInteger newReferenc =BigInteger.ZERO;
		if(moAccouuntList.size()>0){
		newReferenc = itsBankingService.getMaxCheckList(moAccouuntList.get(0).getMoAccountId());
		}
		if (!SessionConstants.checkSessionExist(request)){
			aPage = "welcome";
			modal.addAttribute("user-message", "User Name Or Password are Invalid.");
		}
		modal.addAttribute("bankAccounts",getBankingList());
		modal.addAttribute("motransList",moAccouuntList);
		modal.addAttribute("billExists",billExists);
		modal.addAttribute("newReference",newReferenc);
		return aPage;
	}
	
	@RequestMapping(value = "/getChecksRefs", method = RequestMethod.POST)
	public @ResponseBody Map<String, ArrayList<?>> getCheckList(
			@RequestParam(value = "moAccountId", required = false) Integer moAccountId,
			@RequestParam(value = "reference", required = false) String reference,
			@RequestParam(value = "checktype", required = false) String checktype,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		logger.info("/getChecksRefs");
		logger.info(" moAccountId "+ moAccountId);
		logger.info(" reference "+ reference);
		logger.info(" checktype "+ checktype);
		short checkType=0;
		if(checktype.equalsIgnoreCase("Vendorchecks"))
			checkType=1;
		else
			checkType=2;
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Motransaction> moAccouuntList = (ArrayList<Motransaction>)itsBankingService.getChecksLists(moAccountId,reference,checkType);
			logger.info("Checks List Size: "+moAccouuntList.size());
			
			BigInteger newReferenc = itsBankingService.getMaxCheckList(moAccountId);
			ArrayList<BigInteger> referenceList = new ArrayList<BigInteger>();
			referenceList.add(newReferenc);
			map.put("referenceKey", referenceList);
			if(moAccouuntList != null && moAccouuntList.size() > 0)
				map.put("checkList", moAccouuntList);
			else
				map.put("checkList", new ArrayList<Motransaction>());
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/getToWarehouse", method = RequestMethod.POST)
	public @ResponseBody Map<String, ArrayList<?>> getToWarehouse(
			@RequestParam(value = "selectedWHID", required = false) Integer selectedWHID,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		logger.info("/getChecksRefs");
		logger.info(" selectedWHID "+ selectedWHID);
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		try {
			ArrayList<Prwarehouse> aPrwarehouseList = (ArrayList<Prwarehouse>)itsBankingService.getWhseList(selectedWHID);
			logger.info("Checks List Size: "+aPrwarehouseList.size());
			
			if(aPrwarehouseList != null && aPrwarehouseList.size() > 0)
				map.put("prWarehouseList", aPrwarehouseList);
			else
				map.put("prWarehouseList", new ArrayList<Prwarehouse>());
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/wareHouseDetail", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> wareHouseDetail(
			@RequestParam(value = "warehouseID", required = false) Integer warehouseID,
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		logger.info("/getWarehouses Called");
		try {
			Prwarehouse  PrwarehouseList = itsProductService.getWarehouseDetail(warehouseID);
			if(PrwarehouseList != null)
				map.put("wareHouseDetail", PrwarehouseList);
			else
				map.put("wareHouseDetail", new Prwarehouse());
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/getWarehouses", method = RequestMethod.GET)
	public @ResponseBody Map<String, ArrayList<?>> getWarehouses(
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		Map<String, ArrayList<?>> map = new HashMap<String, ArrayList<?>>();
		logger.info("/getWarehouses Called");
		try {
			ArrayList<Prwarehouse> PrwarehouseList = (ArrayList<Prwarehouse>)itsProductService.getWarehouses();
			logger.info("Checks List Size: "+PrwarehouseList.size());
			if(PrwarehouseList != null && PrwarehouseList.size() > 0)
				map.put("wareHouseList", PrwarehouseList);
			else
				map.put("wareHouseList", new ArrayList<Prwarehouse>());
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/printchecks", method = RequestMethod.GET)
	public String printChecks(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "banking/PrintChecks";
		Rxaddress aRxaddress = new Rxaddress();
		// aRxaddress = itsBankingService.getAddressFromTransactionId();
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");

		}
		return aPage;
	}
	
	@RequestMapping(value = "/inventoryAdjustments", method = RequestMethod.GET)
	public String inventoryAdjustments(ModelMap modal, HttpServletRequest request)
			throws BankingException, ProductException, CompanyException {
		String aPage = "inventory/inventoryAdjustments";
		Rxaddress aRxaddress = new Rxaddress();
		// aRxaddress = itsBankingService.getAddressFromTransactionId();
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		System.out.println(" coAccounts.size() :: "+((List<Prwarehouse>)itsProductService.getWarehouses()).size());
		System.out.println(" coAccounts.size() :: "+((List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown()).size());
		modal.addAttribute("Warehouses", (List<Prwarehouse>)itsProductService.getWarehouses());
		modal.addAttribute("GLAccounts", (List<Coaccount>) chartOfAccountsService.getListOfAccountsFordropdown());
		return aPage;
	}
	
	@RequestMapping(value = "/orderpoint", method = RequestMethod.GET)
	public String orderPoints(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "inventory/orderPoints";
		// aRxaddress = itsBankingService.getAddressFromTransactionId();
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}
	
	@RequestMapping(value = "/countInventory", method = RequestMethod.GET)
	public String countingInventory(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "inventory/CountInventory";
		// aRxaddress = itsBankingService.getAddressFromTransactionId();
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}
	
	@RequestMapping(value = "/vendorinvoicelist", method = RequestMethod.GET)
	public String vendorinvoicelist(ModelMap modal, HttpServletRequest request) {
		String aPage = "vendor/vendorinvoicelist";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		try {
			modal.addAttribute("coAccount", (List<Coaccount>)chartOfAccountsService.getAccountNumber());
			modal.addAttribute("shipVia",(List<Veshipvia>) jobService.getVeShipVia());
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CompanyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modal.addAttribute("currentDate", new Date());
		return aPage;
	}

	@RequestMapping(value = "/divisionsList", method = RequestMethod.GET)
	public String getDivisions(ModelMap modal, HttpServletRequest request,
			HttpServletResponse response, HttpSession theSession, ModelMap model)
			throws IOException, CompanyException {
		String aPage = "company/divisions_page";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
				List<ChSegments> chSegments = userService.getchSegments();
				if(chSegments.size()>1)
				model.addAttribute("chsegments", chSegments.get(1).getSegmentsName());
				List<CoAccountBean> chaccounts= chartOfAccountsService.getChartsAccountSegment();
				model.addAttribute("bankAccounts1", chaccounts);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/taxTerritories", method = RequestMethod.GET)
	public String getTaxTerrotries(ModelMap modal, HttpServletRequest request,
			HttpServletResponse response, HttpSession theSession, ModelMap model)
			throws IOException {
		String aPage = "company/taxterritories_page";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		try {
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
				aUserLogin = userService.getSingleUserDetails(aUserBean
						.getUserId());
				model.addAttribute("userSysAdmin",
						aUserLogin.getSystemAdministrator());
				model.addAttribute("userDetails", aUserLogin);
			}
		} catch (UserException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		return aPage;
	}

	@RequestMapping(value = "/scheduletemplates", method = RequestMethod.GET)
	public String getScheduleTemplates(ModelMap modal,
			HttpServletRequest request) {
		String aPage = "schedule_templates";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}

	@RequestMapping(value = "/inventorycategories", method = RequestMethod.GET)
	public String getInventoryCategories(ModelMap modal,
			HttpServletRequest request) {
		String aPage = "inventory/inventory_categories";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}

	@RequestMapping(value = "/inventorydepartments", method = RequestMethod.GET)
	public String getInventoryDepartments(ModelMap modal,
			HttpServletRequest request) {
		String aPage = "inventory/inventory_departments";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}

	@RequestMapping(value = "/modelmaintenance", method = RequestMethod.GET)
	public String getModelMaintinence(ModelMap modal, HttpServletRequest request) {
		String aPage = "model_maintenance";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}

	@RequestMapping(value = "/warehouse", method = RequestMethod.GET)
	public String getwareHouse(ModelMap modal, HttpServletRequest request)
			throws CompanyException {
		String aPage = "product/wareHouse";
		setItsTaxTerritory((List<CoTaxTerritory>) itstaxTerritoriesService
				.getTaxTerritoryList());
		setItsCoAccount((List<Coaccount>) chartOfAccountsService
				.getAccountNumber());
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		modal.addAttribute("accounts", getItsCoAccount());
		modal.addAttribute("taxTerritories", getItsTaxTerritory());
		return aPage;
	}

	@RequestMapping(value = "/statements", method = RequestMethod.GET)
	public String getStatemets(ModelMap modal, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String aPage = "customer/statements";
		try {
			setCustomers((List<Rxmaster>) itsEmployeeServiceInter.getrxMasterTableValues());
			setPrinters((String[]) ReportUtils.getPrinters());
			if (!SessionConstants.checkSessionExist(request)) {
				aPage = "welcome";
				modal.addAttribute("user-message",
						"User Name Or Password are Invalid.");
			}
		} catch (EmployeeException e) {
			logger.error(e.getMessage(), e);
			response.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}
		modal.addAttribute("customers", getCustomers());
		modal.addAttribute("Printers", getPrinters());
		return aPage;
	}

	@RequestMapping(value = "/openjobreport", method = RequestMethod.GET)
	public String getOpenPOCriteris(HttpServletRequest request) {
		String aPage = "reports/openjobCriteria";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/bookreport", method = RequestMethod.GET)
	public String getBookingsCriteriaPage(HttpServletRequest request) {
		String aPage = "reports/bookedReportCriteria";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/openporeport", method = RequestMethod.GET)
	public String getopenPOCriteriaPage(HttpServletRequest request) {
		String aPage = "reports/openPOCriteria";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}

	@RequestMapping(value = "/po_list", method = RequestMethod.GET)
	public String getPOList(HttpServletRequest request) {
		String aPage = "vendor/po_list";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	
	
//	@RequestMapping(value = "project_so", method = RequestMethod.GET)
////	window.location.href = "./project_so?project_acuSoid="+cuSOID+"&project_aMasterID="+rxMasterID+'&project_vePOID='+vePOID
////	+'&project_jobNumber='+jobNumber+"&project_joReleaseDetailID="+joReleaseDetailID,
//	public String getProjectSOPage(HttpServletRequest request,
//			@RequestParam(value = "project_acuSoid", required = false) int project_acuSoid, 
//			@RequestParam(value = "project_aMasterID", required = false) int project_aMasterID,
//			@RequestParam(value = "project_vePOID", required = false) int project_vePOID,
//			@RequestParam(value = "project_jobNumber", required = false) int project_jobNumber,
//			@RequestParam(value = "project_joReleaseDetailID", required = false) int project_joReleaseDetailID,
//			ModelMap theModel) {
//		String aPage = "job/project_salesOrder";
//		if (!SessionConstants.checkSessionExist(request)) {
//			aPage = "welcome";
//		}
//		theModel.addAttribute("project_acuSoid",project_acuSoid);
//		theModel.addAttribute("project_aMasterID",project_aMasterID);
//		return aPage;
//	}
	
	@RequestMapping(value = "project_so", method = RequestMethod.GET)
	public String getProjectSOPage(HttpServletRequest request, @RequestParam(value = "project_acuSoid", required = false) int project_acuSoid, 
			@RequestParam(value = "project_aMasterID", required = false) int project_aMasterID, ModelMap theModel) {
		String aPage = "job/project_salesOrder";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		theModel.addAttribute("project_acuSoid",project_acuSoid);
		theModel.addAttribute("project_aMasterID",project_aMasterID);
		return aPage;
	}

	@RequestMapping(value = "/salesorder", method = RequestMethod.GET)
	public ModelAndView getsalesorder(
			@RequestParam(value = "cusoid", required = false) String cusoid,
			@RequestParam(value = "customerid", required = false) String customerid,
			@RequestParam(value = "oper", required = false) String operation,
			HttpServletRequest request,ModelMap theModel) throws UserException {
		Sysassignment sysassignment=null;
		String aPage = "job/salesOrderList";
		ModelAndView theView = new ModelAndView("job/salesOrderList");
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		List<Prwarehouse> alPrwarehouse = null;
		try {
			alPrwarehouse = (List<Prwarehouse>) jobService.getWareHouse();
		} catch (JobException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sysassignment=sysservice.getCustomerCategories();
		/*Commented By Velmurugan
		 * Issues found in mpnservices
		 * 14/1/2016 
		 * List<Prwarehouse> alTaxPrwarehouse = new ArrayList<Prwarehouse>();
		CoTaxTerritory acotTerritory = null;
		for (Prwarehouse al : alPrwarehouse) {
			acotTerritory = new CoTaxTerritory();
			try {
				acotTerritory = itstaxTerritoriesService
						.getSingleTaxTerritory(al.getCoTaxTerritoryId());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			al.setEmail(acotTerritory.getTaxRate().toString());
			alTaxPrwarehouse.add(al);

		}*/
		logger.info("sysassignment*****"+sysassignment);
		
		List<String> addlist=new ArrayList<String>();
		addlist.add("CheckcreditlimitinQuickBook");
		addlist.add("UseCustomersCreditLimitwhencreatingJobs");
		
		ArrayList<Sysvariable> sysvariablelist= userService.getInventorySettingsDetails(addlist);
		int i=0;
		for(Sysvariable theSysvariable:sysvariablelist){
			//Inventory Settings
			if(i==0){
				logger.info(i+" theSysvariable.getValueString() = "+theSysvariable.getValueLong());
				theModel.addAttribute("cusCreLiminQuickBookYes", theSysvariable.getValueLong());
			}
			if(i==1)
			{
				logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
				theModel.addAttribute("chkUserCustomerCreditYN", theSysvariable.getValueLong());
			}
//			if(i==2)
//			{
//				logger.info(i+" theSysvariable.getValueLong() = "+theSysvariable.getValueLong());
//				theModel.addAttribute("chkUserCustomerCreditYN", theSysvariable.getValueLong());
//			}
			i++;
		}
		
		if(sysassignment!=null){
			theModel.addAttribute("CustomerCategory1", sysassignment.getCustomerCategory1());
			theModel.addAttribute("CustomerCategory2", sysassignment.getCustomerCategory2());
			theModel.addAttribute("CustomerCategory3", sysassignment.getCustomerCategory3());
			theModel.addAttribute("CustomerCategory4", sysassignment.getCustomerCategory4());
			theModel.addAttribute("CustomerCategory5", sysassignment.getCustomerCategory5());
		}
		// theView.addObject("wareHouseSO", alTaxPrwarehouse);
		theView.addObject("operation", operation);
		theView.addObject("pjcusoid", cusoid);
		theView.addObject("pjcustomerid", customerid);
		theView.addObject("projectManager", operation);
		return theView;
	}

	@RequestMapping(value = "/createinvoice", method = RequestMethod.GET)
	public String getinvoice(
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "frompage", required = false) String frompage,
			@RequestParam(value = "invoiceID", required = false) String invoiceID,
			@RequestParam(value = "rxMasterId", required = false) String rxMasterId,
			HttpServletRequest request, HttpSession theSession,
			ModelMap theModel, HttpServletResponse theResponse) {
		String aPage = "job/invoiceList";
		try {
			System.out.println("MediatorController.getinvoice()::createinvoice");
			
			theModel.addAttribute("prWareHouseDetails",
					(List<Prwarehouse>) jobService.getWareHouse());
			
			theModel.addAttribute("veShipViaDetails",
					(List<Veshipvia>) jobService.getVeShipVia());

			theModel.addAttribute("divisions",
					(List<Codivision>) itsCompanyService.getCompanyDivisions());
			if(frompage==null || frompage==""){
				frompage="0";
			}
			if(invoiceID==null || invoiceID==""){
				invoiceID="0";
			}
			theModel.addAttribute("frompage",frompage);
			theModel.addAttribute("invoiceID",invoiceID);
			theModel.addAttribute("rxMasterId",rxMasterId);
			theModel.addAttribute("wareHouse", (List<Prwarehouse>) jobService.getWareHouse());
			//theModel.addAttribute("theNewShiptocusaddress", itsCompanyService.getAllAddress(aJomaster.getRxCustomerId()));
		} catch (JobException e) {
			e.printStackTrace();
		} catch (CompanyException e) {
			e.printStackTrace();
		}
		return aPage;
	}
	@RequestMapping(value = "/transfer", method = RequestMethod.GET)
	public String getTransfer(
			@RequestParam(value = "oper", required = false) String operation,
			HttpServletRequest request, HttpSession theSession,
			ModelMap theModel, HttpServletResponse theResponse) {
		String aPage = "inventory/transfer";
		try {
			theModel.addAttribute("prWareHouseDetails",
					(List<Prwarehouse>) jobService.getWareHouse());

			theModel.addAttribute("veShipViaDetails",
					(List<Veshipvia>) jobService.getVeShipVia());

			theModel.addAttribute("divisions",
					(List<Codivision>) itsCompanyService.getCompanyDivisions());
		} catch (JobException e) {
			e.printStackTrace();
		} catch (CompanyException e) {
			e.printStackTrace();
		}
		return aPage;
	}	
	@RequestMapping(value = "/warehouseTransfer", method = RequestMethod.GET)
	public ModelAndView getWarehouseTransfer(
			@RequestParam(value = "oper", required = false) String operation,
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "prTransferId", required = false) Integer prTransferId,
			HttpServletRequest request, HttpSession theSession,
			ModelMap theModel, HttpServletResponse theResponse) {
		String aPage = "inventory/add_warehousetransfer";
		ModelAndView theView = new ModelAndView("inventory/add_warehousetransfer");
		TsUserSetting aUserLoginSetting = null;
		
		try {
			
		
			theView.addObject("prWareHouseDetails",
					(List<Prwarehouse>) jobService.getWareHouse());
			if(prTransferId != null && prTransferId != 0 && !"copy".equals(token))
			{
				PrwarehouseTransfer objPrwarehouseTransfer = jobService.getWareHouseTransfer(prTransferId);
				theView.addObject("prWareHousetransfer",objPrwarehouseTransfer);
				theView.addObject("currentDate", objPrwarehouseTransfer.getTransferDate());
				theView.addObject("transactionNumber",
						objPrwarehouseTransfer.getTransactionNumber());
			}
			else if("copy".equalsIgnoreCase(token)){
				System.out.println("Inside Copy-------------------->");
				PrwarehouseTransfer objPrwarehouseTransfer = jobService.getWareHouseTransfer(prTransferId);
				java.util.Date now = new java.util.Date();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String createdDate = df.format(now);
				if ((createdDate != null && createdDate != "")) {
					try {
						objPrwarehouseTransfer.setReceivedDate(DateUtils.parseDate(createdDate,
								new String[] { "MM/dd/yyyy" }));
						objPrwarehouseTransfer.setTransferDate(DateUtils.parseDate(createdDate,
								new String[] { "MM/dd/yyyy" }));
						objPrwarehouseTransfer.setEstreceiveDate(DateUtils.parseDate(createdDate,
								new String[] { "MM/dd/yyyy" }));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				theView.addObject("prWareHousetransfer",objPrwarehouseTransfer);
				
				theView.addObject("currentDate", new Date());
				Integer transNo = jobService.getWareHouseTransactionNo();
				theView.addObject("transactionNumber",	transNo);
				theView.addObject("operation", "copy");
			}
			else{
				theView.addObject("prWareHousetransfer",
						new PrwarehouseTransfer());
				theView.addObject("currentDate", new Date());
				theView.addObject("transactionNumber",
						 jobService.getWareHouseTransactionNo());
			}			
			

			theView.addObject("divisions",
					(List<Codivision>) itsCompanyService.getCompanyDivisions());
		} catch (JobException e) {
			e.printStackTrace();
		} catch (CompanyException e) {
			e.printStackTrace();
		}
		return theView;
	}
	
	@RequestMapping(value = "/salesOrderTemplate", method = RequestMethod.GET)
	public String salesOrderTemplate(ModelMap modal, HttpServletRequest request)
			throws BankingException {
		String aPage = "job/salesOrderTemplate";
		// aRxaddress = itsBankingService.getAddressFromTransactionId();
		System.out.println("salesOrderTemplateGrid");
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		return aPage;
	}
	
   /** 
    * Created by : Leo             Date:15/9/2014
    * Description: GlTransaction Page call
    * */
	
	
	@RequestMapping(value = "/gltransaction", method = RequestMethod.GET)
	public String gltransaction(HttpServletRequest request) {
		String aPage = "glTransaction";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	@RequestMapping(value = "/TestForQuotes", method = RequestMethod.GET)
	public String TestForQuotes(HttpServletRequest request) {
		String aPage = "TestForQuotes";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
		}
		return aPage;
	}
	
	
	@RequestMapping(value = "/contactSystemAdministrator", method = RequestMethod.GET)
	public String contactSystemAdministrator(HttpServletRequest request,ModelMap modal) {
		String aPage = "ContactSystemAdministrator";
		modal.addAttribute("systemmessage", "DataBase is not compactable.Contact System Administrator");
		return aPage;
	}
	

	public Rxmaster getRxMasterDetails() {
		return rxMasterDetails;
	}

	public void setRxMasterDetails(String theRolodexNumber) {
		try {
			this.rxMasterDetails = companyService
					.getEmployeeDetails(theRolodexNumber);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
		}
		;
	}

	public Rxaddress getRxAddressDetails() {
		return rxAddressDetails;
	}

	/*
	 * public List<?> getRxAddressDetails() { return rxAddressDetails; }
	 */

	public void setRxAddressDetails(String theRolodexNumber) {
		try {
			this.rxAddressDetails = companyService
					.getEmployeeAddressDetails(theRolodexNumber);
		} catch (CompanyException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public Cumaster getCustomerRecord() {
		return customerRecord;
	}

	public void setCustomerRecord(int rxMasterId) {
		this.customerRecord = cuMasterService.getCustomerDetails(rxMasterId);
	}

	public List<Vefreightcharges> getItsFreightCharges() {
		return itsFreightCharges;
	}

	public void setItsFreightCharges(List<Vefreightcharges> itsFreightCharges) {
		this.itsFreightCharges = itsFreightCharges;
	}

	public List<Veshipvia> getItsVeshipvia() {
		return itsVeshipvia;
	}

	public void setItsVeshipvia(List<Veshipvia> itsVeshipvia) {
		this.itsVeshipvia = itsVeshipvia;
	}

	public List<Prwarehouse> getItsPrwarehouses() {
		return itsPrwarehouses;
	}

	public void setItsPrwarehouses(List<Prwarehouse> itsPrwarehouses) {
		this.itsPrwarehouses = itsPrwarehouses;
	}

	public List<TsAccessModule> getAccessModule() {
		return itsAccessModule;
	}

	public void setAccessModule(List<TsAccessModule> itsAccessModule) {
		this.itsAccessModule = itsAccessModule;
	}

	/**
	 * @return the itsInventoryService
	 */
	public InventoryService getItsInventoryService() {
		return itsInventoryService;
	}

	/**
	 * @param itsInventoryService
	 *            the itsInventoryService to set
	 */
	public void setItsInventoryService(InventoryService itsInventoryService) {
		this.itsInventoryService = itsInventoryService;
	}

	/**
	 * @return the itsPrdepartment
	 */
	public List<PrDepartment> getItsPrdepartment() {
		return itsPrDepartment;
	}

	/**
	 * @param itsPrdepartment
	 *            the itsPrdepartment to set
	 */
	public void setItsPrdepartment(List<PrDepartment> itsPrdepartment) {
		this.itsPrDepartment = itsPrdepartment;
	}

	public Emmaster getCommissionsDetails() {
		return commissionsDetails;
	}

	public void setCommissionsDetails(String theRolodexNumber) {
		this.commissionsDetails = itsEmployeeServiceInter
				.getEmployeeCommDetails(theRolodexNumber);
	}

	/**
	 * @return the itsPrcategory
	 */
	public List<PrCategory> getItsPrcategory() {
		return itsPrcategory;
	}

	/**
	 * @param itsPrcategory
	 *            the itsPrcategory to set
	 */
	public void setItsPrcategory(List<PrCategory> itsPrcategory) {
		this.itsPrcategory = itsPrcategory;
	}

	public List<Coaccount> getItsCoAccount() {
		return itsCoAccount;
	}

	public void setItsCoAccount(List<Coaccount> itsCoAccount) {
		this.itsCoAccount = itsCoAccount;
	}

	public List<MoAccount> getBankingList() {
		return itsBankingList;
	}

	public void setBankingList(List<MoAccount> itsBankingList) {
		this.itsBankingList = itsBankingList;
	}

	public List<Codivision> getDivision() {
		return itsDivision;
	}

	public void setDivision(List<Codivision> division) {
		this.itsDivision = division;
	}

	public List<JoCustPO> getItsPODetails() {
		return itsPODetails;
	}

	public void setItsPODetails(List<JoCustPO> itsPODetails) {
		this.itsPODetails = itsPODetails;
	}

	public List<CuTerms> getItsTerms() {
		return itsTerms;
	}

	public void setItsTerms(List<CuTerms> itsTerms) {
		this.itsTerms = itsTerms;
	}

	public List<CoTaxTerritory> getItsTaxTerritory() {
		return itstaxTerritory;
	}

	public void setItsTaxTerritory(List<CoTaxTerritory> itstaxTerritory) {
		this.itstaxTerritory = itstaxTerritory;
	}

	public List<Rxmaster> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Rxmaster> customers) {
		this.customers = customers;
	}

	public String[] getPrinters() {
		return printers;
	}

	public void setPrinters(String[] printers) {
		this.printers = printers;
	}
	
	@RequestMapping(value = "/getCustomerContacts", method = RequestMethod.POST)
	public @ResponseBody
	CustomResponse getCustomerContacts(
			@RequestParam(value = "rxMasterId", required = false) Integer rxMasterId,
			HttpSession session, HttpServletResponse theResponse)
			throws IOException, JobException {
		CustomResponse aResponse = new CustomResponse();
		
		try {
			List<?> aSOLineItemDetails = jobService
					.getContactFromRxMaster(rxMasterId); 
			aResponse.setRows(aSOLineItemDetails); 
		} catch (JobException e) {
			logger.error(e.getMessage());
			theResponse.sendError(e.getItsErrorStatusCode(), e.getMessage());
		}

		return aResponse; 
	}
	@RequestMapping(value = "/getPaymentTermsDueDate", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getPaymentTermsDueDate(
			@RequestParam(value = "cuTermsId", required = false) Integer cuTermsID,
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		Map<String, String> map = new HashMap<String, String>();
		logger.info("/getWarehouses Called");
		try {
			ArrayList<AutoCompleteBean> PrwarehouseList = (ArrayList<AutoCompleteBean>)itsEmployeeServiceInter.getTermsDueDate(cuTermsID);
			System.out.println(PrwarehouseList.get(0).getDueDays()+" :: "+PrwarehouseList.get(0).getDueOnDay());
				map.put("duedate", PrwarehouseList.get(0).getDueDays()+"");
				map.put("dueondays", PrwarehouseList.get(0).getDueOnDay()+"");
				
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return map;
	}
	
	@RequestMapping(value = "/getSysPrivilage", method = RequestMethod.POST)
	public @ResponseBody  Map<String, String> getSysPrivilage (
			@RequestParam(value = "accessPage", required = false) String accessPage,
			@RequestParam(value = "userGroupID", required = false) Integer userGroupID,
			@RequestParam(value = "UserLoginID", required = false) Integer UserLoginID,
			HttpSession theSession, HttpServletResponse response) throws IOException, UserException {
		
		logger.info(" accessPage :: "+accessPage+" || "+userGroupID+" || "+UserLoginID);
		String str = "";
		Sysprivilege aSysprivilege=null;
		Map<String, String> map =null;
		TsUserLogin aUserLogin=null;
		
		try {	
			aUserLogin = new TsUserLogin();
			map = new HashMap<String, String>();
			aUserLogin = userService.getSingleUserDetails(UserLoginID);
			aSysprivilege=new Sysprivilege();
			Integer privilagevalue= 0;
			aSysprivilege.setAccessProcedureId(AccessProcedureConstant.getConstantSysvariableId(accessPage));
			aSysprivilege.setPrivilegeValue(privilagevalue);
			if(userGroupID!=null){
				aSysprivilege.setUserGroupId(userGroupID);
			}
			if(UserLoginID!=null){
				aSysprivilege.setUserLoginId(UserLoginID);
			}
			
			String thecondition=" WHERE   AccessProcedureID="+aSysprivilege.getAccessProcedureId()+" And UserLoginID="+aSysprivilege.getUserLoginId();
			
			Sysprivilege obj = sysservice.getSysPrivileageLst(aSysprivilege, thecondition);
		//	logger.info(" obj.getPrivilegeValue() = "+obj.getPrivilegeValue());
			
			if(obj!=null && obj.getPrivilegeValue()==1)
				str = "granted";
			else if(aUserLogin.getSystemAdministrator() == 1)
				str = "granted";
			else if(accessPage.equals("OpenPeriod_PostingOnly"))
				str = "deniedforOP";
			else
				str = "denied";
			
			map.put("Value", str);
			logger.info(" getSysPrivilage data string = "+str);
		}
		catch(Exception e)
		{
			if(aUserLogin.getSystemAdministrator() == 1)
				str = "granted";
			else				
				str = "denied";
			e.printStackTrace();
		}finally{
			aUserLogin = null;
			aSysprivilege = null;
			aUserLogin = null;
			str=null;
		}
		return map;
	}
	public Boolean getSysPrivilage (String accessPage,Integer userGroupID,
			HttpSession theSession, HttpServletResponse response) throws IOException, UserException {
		
		Sysprivilege aSysprivilege=new Sysprivilege();
		TsUserLogin aUserLogin = null;
		UserBean aUserBean;
		boolean returnvalue=true;
		try {	
			
			aUserBean = (UserBean) theSession
					.getAttribute(SessionConstants.USER);
			if (aUserBean != null) {
			aUserLogin = userService.getSingleUserDetails(aUserBean.getUserId());
			
			Integer privilagevalue= 0;
			aSysprivilege.setAccessProcedureId(AccessProcedureConstant.getConstantSysvariableId(accessPage));
			aSysprivilege.setPrivilegeValue(privilagevalue);
			aSysprivilege.setUserGroupId(userGroupID);
			aSysprivilege.setUserLoginId(aUserLogin.getUserLoginId());
			
			String thecondition=" WHERE   AccessProcedureID="+aSysprivilege.getAccessProcedureId()+" And UserLoginID="+aSysprivilege.getUserLoginId();
			
			Sysprivilege obj = sysservice.getSysPrivileageLst(aSysprivilege, thecondition);
			
			if(obj.getPrivilegeValue()==1)
				returnvalue=true;
			else if(aUserLogin.getSystemAdministrator() == 1)
				returnvalue=true;
			else
				returnvalue=false;
			}
		}
		catch(Exception e)
		{
			if(aUserLogin.getSystemAdministrator() == 1)
				returnvalue=true;
			else				
				returnvalue=false;
			//e.printStackTrace();
		}
		return returnvalue;
	}
	
	@RequestMapping(value = "/requestForInsertintoemmaster", method = RequestMethod.GET)
	public @ResponseBody Boolean requestForInsertintoemmaster(
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		try {
			ArrayList<TsUserLogin> tsUserLoginlst = sysservice.getallUserID();
			for(TsUserLogin aTsUserLogin:tsUserLoginlst){
				TsUserLogin aUserLogin = userService.getSingleUserDetails(aTsUserLogin.getUserLoginId());
				Rxmaster arxmaster=new Rxmaster();
				arxmaster.setName(aUserLogin.getFullName());
				arxmaster.setSearchName("");
				arxmaster.setIsEmployee(true);
				boolean returnvalue=itsCustomerService.TemporaryMethodforInsert(arxmaster, aTsUserLogin.getUserLoginId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/convertrtftohtml", method = RequestMethod.GET)
	public @ResponseBody Boolean convertrtftohtml(
			@RequestParam(value = "betweenfrom", required = false) Integer betweenfrom,
			@RequestParam(value = "betweento", required = false) Integer betweento,
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		try {
			
				boolean returnvalue=jobService.getjoquotedetailrtftohtml(betweenfrom,betweento);
				
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return true;
	}
	@RequestMapping(value = "/convertrtftohtmlTemplate", method = RequestMethod.GET)
	public @ResponseBody Boolean convertrtftohtmlTemplate(
			@RequestParam(value = "betweenfrom", required = false) Integer betweenfrom,
			@RequestParam(value = "betweento", required = false) Integer betweento,
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		try {
			
				boolean returnvalue=jobService.getjoquoteTemplatedetailrtftohtml();
				
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return true;
	}
	@RequestMapping(value = "/updateposition", method = RequestMethod.GET)
	public @ResponseBody Boolean updateposition(HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		try {
			
				boolean returnvalue=jobService.getjoquotedetailupdateposition();
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/updateEmailStampValue", method = RequestMethod.GET)
	public @ResponseBody String updateEmailStampValue(
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "processID", required = false) Integer processID,
			HttpSession session, HttpServletResponse theResponse)throws IOException, ProductException {
		String aDateFormat = null;
		try {
			boolean returnvalue= jobService.updateEmailTimestamp(type, processID);
			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			if(returnvalue){
				aDateFormat = fromUser.format(new Date());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			theResponse.sendError(500, e.getMessage());
		}
		return aDateFormat;
	}
	
	@RequestMapping(value = "/viewInsideJobVendorInvoice", method = RequestMethod.GET)
	public String viewInsideJobVendorInvoice(ModelMap modal, HttpServletRequest request) {
		String aPage = "JobVendor_invoicePage";
		if (!SessionConstants.checkSessionExist(request)) {
			aPage = "welcome";
			modal.addAttribute("user-message",
					"User Name Or Password are Invalid.");
		}
		try {
			modal.addAttribute("coAccount", (List<Coaccount>)chartOfAccountsService.getAccountNumber());
			modal.addAttribute("shipVia",(List<Veshipvia>) jobService.getVeShipVia());
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CompanyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		modal.addAttribute("currentDate", new Date());
		return aPage;
	}
	
	public String getmyStatus(String accessPage,Integer UserLoginID ,HttpSession theSession,UserService userServic,SysService sysservic) throws IOException, UserException {
		
		logger.info(" accessPage :: "+accessPage+" || "+UserLoginID);
		String str = "";
		Sysprivilege aSysprivilege=new Sysprivilege();
		TsUserLogin aUserLogin = new TsUserLogin();
		String thecondition= null;
		try {	
		
			aUserLogin = userServic.getSingleUserDetails(UserLoginID);
			
			Integer privilagevalue= 0;
			aSysprivilege.setAccessProcedureId(AccessProcedureConstant.getConstantSysvariableId(accessPage));
			aSysprivilege.setPrivilegeValue(privilagevalue);
			if(UserLoginID!=null){
				aSysprivilege.setUserLoginId(UserLoginID);
			}
			
			thecondition=" WHERE  AccessProcedureID="+aSysprivilege.getAccessProcedureId()+" And UserLoginID="+aSysprivilege.getUserLoginId();
			
			Sysprivilege obj = sysservic.getSysPrivileageLst(aSysprivilege, thecondition);
		//	logger.info(" obj.getPrivilegeValue() = "+obj.getPrivilegeValue());
			
			if(obj!=null && obj.getPrivilegeValue()==1)
				str = "granted";
			else if(aUserLogin.getSystemAdministrator() == 1)
				str = "granted";
			else if(accessPage.equals("OpenPeriod_PostingOnly"))
				str = "deniedforOP";
			else
				str = "denied";
		}
		catch(Exception e)
		{
			if(aUserLogin.getSystemAdministrator() == 1)
				str = "granted";
			else				
				str = "denied";
			//e.printStackTrace();
		}
		finally{
			aSysprivilege = null;
			aUserLogin=null;
			thecondition = null;
		}
		return str;
	}
	
	
	@RequestMapping(value = "/insertMissingEntriesingl", method = RequestMethod.POST)
	public String insertMissingEntriesingl(
			@RequestParam(value = "fromId", required = false) Integer fromId,
			@RequestParam(value = "toId", required = false) Integer toId,ModelMap modal, HttpServletRequest request)throws IOException,BankingException {
		String status="error";
		try {
			
			System.out.println(fromId+"--"+toId);
			status = itsGltransactionService.insertMissingEntries(fromId, toId);
			
		} catch (BankingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	
	/**
	 * Created By Simon Date: 8th June 2016
	 * Reason For changing : ID #536    Inventory-Next/Previous Buttons
	 * 
	 */
	/*@RequestMapping(value="/getSNo",method=RequestMethod.GET)
	@ResponseBody
	public Integer getSNo(@RequestParam(value="inventoryId",required=false) Integer prMasterID){
		Integer sno=0;
		try {
			sno=itsInventoryService.getSNO(prMasterID);
		} catch (InventoryException e) {
			e.printStackTrace();
		}
		return sno;
	}*/
	/**
	 * Created By Simon Date: 8th June 2016
	 * Reason For changing : ID #536    Inventory-Next/Previous Buttons
	 * 
	 * @param key
	 * @return
	 */
	/*@RequestMapping(value="/getInventoryRecord",method=RequestMethod.GET)
	@ResponseBody
	public InventoryVo getInventory(@RequestParam("inventoryKey") Integer key){
		InventoryVo inventoryVo=null;
		try {
			inventoryVo=itsInventoryService.getInventory(key);
		} catch (InventoryException e) {
			e.printStackTrace();
		}
		if(inventoryVo==null){
			inventoryVo=new InventoryVo();
			inventoryVo.setInventoryId(0);
			inventoryVo.setItemCode("0");
			return inventoryVo;
		}else{
			return inventoryVo;
		}
	}*/
	@RequestMapping(value="/getAandD",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,String> getAccountAndDescription(@RequestParam(value="rxMasterId") String rxMasterId){
		Map<String,String> result=null;
		Coaccount coAccount=null;
		if(rxMasterId!=null){			
			coAccount=chartOfAccountsService.getAandD(rxMasterId);
			if(coAccount!=null){
				result=new HashMap<String, String>();
				result.put("number", coAccount.getNumber());
				result.put("description", coAccount.getDescription());
				result.put("ceaId", coAccount.getCoAccountId().toString());
			}
		}else{
			result=null;
		}
		return result;
	}
}