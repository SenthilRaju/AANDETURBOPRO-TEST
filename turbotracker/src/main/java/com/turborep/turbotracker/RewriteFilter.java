package com.turborep.turbotracker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.turborep.turbotracker.system.service.SysService;
import com.turborep.turbotracker.user.dao.UserBean;
import com.turborep.turbotracker.user.exception.UserException;
import com.turborep.turbotracker.user.service.UserService;
import com.turborep.turbotracker.util.SessionConstants;

public class RewriteFilter implements Filter {

	// private static Pattern siteDomainPattern =
	// Pattern.compile("(\\w+).prospectclick.com");
	private UserService userServ;
	private SysService sysServ;

	private static Pattern siteDomainPattern = Pattern
			.compile("(\\w+)\\.([A-Za-z0-9-]+)\\.(\\w+)");
	private static Pattern siteDomainPattern1 = Pattern
			.compile("([A-Za-z0-9-]+)\\.(\\w+)");
	Logger log = Logger.getLogger(RewriteFilter.class);

	// private static Pattern siteDomainPattern = Pattern.compile("(\\w+).com");

	public void destroy() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	public void forwardRequest(ServletRequest req, ServletResponse res,
			String user, String domain,
			RewriteHttpServletRequestWrapper wrappedRequest)
			throws IOException, ServletException, UserException {
		// System.out.println("I am entered into  forwardRequest");
		 System.out.println("User ----------->>>>|"+user+"|"+("ProspectNewgetMediaURL".equals(user.toLowerCase())));

		HttpSession session = ((HttpServletRequest) req).getSession();

		UserBean aUserBean;
		aUserBean = (UserBean) session.getAttribute(SessionConstants.USER);

		int lastinx = user.lastIndexOf('/');
		int indx = user.indexOf('?');
		String urlText ="";
		if(indx!=-1)
		urlText = user.substring(lastinx + 1,indx);
		else
		urlText = user.substring(lastinx + 1);
		
		System.out.println(urlText + "=== ");
		if (urlText.equals("")) {
			RequestDispatcher rd = req.getRequestDispatcher("/turbo/");
			rd.forward(req, res);
			return;
		} else {

			if (aUserBean != null) {

				MediatorController mm = new MediatorController();

				if (getAccesspage(urlText.toLowerCase()).equals("noprivilage")) {

					RequestDispatcher rd = req.getRequestDispatcher(user);
					rd.forward(req, res);
					return;
				} else {

					String str = mm.getmyStatus(
							getAccesspage(urlText.toLowerCase()),
							aUserBean.getUserId(), session, userServ, sysServ);

					if (!str.equals("denied")) {
						RequestDispatcher rd = req.getRequestDispatcher(user);
						rd.forward(req, res);
						return;
					} else {
						RequestDispatcher rd = req
								.getRequestDispatcher("/turbo/login");
						rd.forward(req, res);
						return;
					}
				}
			} else {
				RequestDispatcher rd = req.getRequestDispatcher(user);
				rd.forward(req, res);
				return;
			}

		}
	}

	public String getAccesspage(String accesspage) {
		String strObj = "";

		Map<String, String> hasmapObj = new HashMap<String, String>();
		hasmapObj.put("vendors", "Vendors");
		hasmapObj.put("po_list", "Purchase_Orders");
		hasmapObj.put("vendorbills", "Pay_Bills");
		hasmapObj.put("vendorinvoicelist", "Invoice_Bills");
		hasmapObj.put("employeelist", "Employees");
		hasmapObj.put("employeeCommissions", "Commissions");
		hasmapObj.put("rolodexList", "Rolodex");
		hasmapObj.put("userlist", "Users");
		hasmapObj.put("settings", "Settings");
		hasmapObj.put("jobflow ", "New_Job");
		hasmapObj.put("sales", "Sales");
		hasmapObj.put("projects", "Project");
		hasmapObj.put("chartofaccounts", "Chart_Accounts");
		hasmapObj.put("divisionsList", "Divisions");
		hasmapObj.put("taxTerritories", "Tax_Territories");
		hasmapObj.put("drillDown", "General_Ledger");
		hasmapObj.put("companyjournals", "Journal_Entries");
		hasmapObj.put("accountingcycles", "Accounting_Cycles");
		hasmapObj.put("gltransaction", "GL_Transactions");
		hasmapObj.put("bankingAccounts", "Banking");
		hasmapObj.put("writechecks", "Write_Checks");
		hasmapObj.put("reprintchecks", "Reissue_Checks");
		hasmapObj.put("reconcileAccounts", "Recouncile_Accounts");
		hasmapObj.put("inventory", "Inventory");
		hasmapObj.put("inventorycategories", "Categories");
		hasmapObj.put("warehouse", "Warehouses");
		hasmapObj.put("showReceivedInventoryList", "Receive_Inventory");
		hasmapObj.put("transfer", "Transfer");
		hasmapObj.put("orderpoint", "Order_Points");
		hasmapObj.put("inventoryValue", "Inventory_Value");
		hasmapObj.put("countInventory", "Count");
		hasmapObj.put("inventoryTransactions", "Adjustments");
		hasmapObj.put("inventoryAdjustments", "Transactions");
		hasmapObj.put("customers", "Customers");
		hasmapObj.put("customerpaymentlist", "Payments");
		hasmapObj.put("statements", "Statements");
		hasmapObj.put("salesorder", "Sales_Order");
		hasmapObj.put("createinvoice", "Invoice");
		hasmapObj.put("financeCharges", "Finance_Changes");
		hasmapObj.put("taxAdjustments", "Tax_Adjustments");
		hasmapObj.put("creditDebitMemos", "CreditDebit_Memos");
		hasmapObj.put("salesOrderTemplate", "Sales_Order_Template");

		// System.out.println("====================="+hasmapObj.get(accesspage));

		if (hasmapObj.get(accesspage) != null)
			strObj = hasmapObj.get(accesspage);
		else
			strObj = "noprivilage";

		return strObj;
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		// System.out.println("i am inside doFileter");
		RewriteHttpServletRequestWrapper wrappedRequest = new RewriteHttpServletRequestWrapper(
				(HttpServletRequest) req);
		String uri = ((HttpServletRequest) req).getRequestURI();
		String url = ((HttpServletRequest) req).getRequestURL().toString();

		 System.out.println(req.getServerName() + " Server Name " + url );
		 
		 String SubStr1 = new String("turbotracker");
		 int lengthofUri =uri.indexOf(SubStr1);
		// System.out.println(" My Test Name " + uri.indexOf(SubStr1)+"=="+uri.substring(lengthofUri+12) );
		 if(lengthofUri!=-1)
		 uri = uri.substring(lengthofUri+12);
		 
		try {
			forwardRequest(
					req,
					res,
					uri,
					req.getServerName().replaceAll("/", "")
							.replaceAll("www.", ""), wrappedRequest);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
		ServletContext servletContext = arg0.getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		// System.out.println("==?"+wac);
		sysServ = (SysService) wac.getBean("userServiceDao");
		// System.out.println("==?"+sysServ);
		userServ = (UserService) wac.getBean("sysServiceDao");
	}

}
