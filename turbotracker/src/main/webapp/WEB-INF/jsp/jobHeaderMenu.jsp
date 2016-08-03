<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="no-cache">
<meta http-equiv="Expires" content="-1">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<link type="text/css" href="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/themes/humanity/jquery-ui-1.8.23.custom.css" rel="stylesheet"/>
	<link type="text/css" href="./../resources/web-plugins/jquery.jqGrid-4.4.0/css/ui.jqgrid.css" media="screen" rel="stylesheet"/>
	<link type="text/css" href="./../resources/styles/turbo-css/job_headermenu.css" rel="stylesheet" />
	<link type="text/css" href="./../resources/styles/turbo-css/job_turbo.css" rel="stylesheet" />
	<link rel="SHORTCUT ICON" href="/resources/Icons/TurboRepIcon.png">
	<style>
		.ui-widget-overlay { background: none repeat scroll 0 0 #000000; opacity: 0.6;}
	</style>
		<div id="turbo_app_home" style="height: 104px;">
			<div id="turbo_app_header">
				<div class="app_logo">
					<img src="./../resources/styles/turbo-css/images/turbopro_app_icon.png" style="padding-bottom: 4px;" />
					<label class="headerMenuVersionLable">Version <%=ResourceBundle.getBundle("version").getString("app.jdbc.appversion") %></label>
				</div>
				<div class="app_header_sec">
					<div class="app_top_navi">
						<ul>
							<li><a href="#"><img src="./../resources/styles/turbo-css/images/turbo_app_settings_icon.png"/></a>
								<ul class="child">
									<li><a href="#" onclick="openAddNewUserDefaultDialog()">My Profile</a></li>
									<li><a href="./usercontroller/logout">Logout</a></li>
								</ul>
							</li>
							<li class=".headerMenuBarColor">|</li>
							<li class="headerMenuNamePos"><a href="#">Welcome ${sessionScope.user.fullName}</a></li>
						</ul>
					</div>
					<div class="app_main_navi">
						<ul>
							<li id="mainMenuHomePage"><a class="home" href="#" onclick="homePage()">Home</a></li>
							<li id="mainMenuSalesPage"><a class="sales" href="#" onclick="salesPage()">Sales</a></li>
							<li id="mainMenuProjectsPage"><a class="projects" href="#" onclick="projectPage()">Projects</a></li>
							<li id="mainmenuBankingPage"><a class="banking" href="#" onclick="bankingPage()">Banking</a>
								<ul class="child">
									<li><a href="#" onclick="writeChecksPage()">Write Checks</a></li>
									<li><a href="#" onclick="reprintChecksPage()">Reissue Checks</a></li>
									<li><a href="#" onclick="reconcileAccountsPage()">Reconcile Accounts</a></li>
								</ul>
								
							</li>
							<li id="mainmenuInventoryPage"><a class="inventory" href="#" onclick="inventoryPage()">Inventory</a>
								<ul class="child">
									<li><a href="#" onclick="inventoryCategories()">Categories</a></li>
									<li><a href="#" onclick="inventoryDepartments()">Departments</a></li>
									<li><a href="#" onclick="inventoryWarehouses()">warehouses</a></li>
									<li><a href="#" onclick="inventoryRecieve()">Receive Inventory</a></li>
									<li><a href="#" onclick="transferPage()">Transfer</a></li>									
									<li><a href="#" onclick="orderPointsPage()">Order Points</a></li>
									<li><a href="#" onclick="inventoryValuePage()">Inventory Value</a></li>   <!-- New UI - Naveed -->
									<li><a href="#" onclick="countingInventoryPage()">Count</a></li>
									<li><a href="#" onclick="inventoryTransactionPage()">Transactions</a></li>
									<li><a href="#" onclick="inventoryAdjustmentsPage()">Adjustments</a></li>
								</ul>
							</li>
							<li id="mainMenuCompanyPage"><a class="company" href="#">Company</a>
								<ul class="child">
									<li><a href="#" onclick="customerPage()">Customers</a>
										<ul class="child-node" style=" width: 138px !important; ">
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="paymentsPage()">Payments</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="unappliedpaymentsPage()"><span style="font-size:13.25px;">Unapplied Payments</span></a></li>
											<li><a onclick="salesorderPage()" href="#">Sales Order</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="statementsPage()">Statements</a></li>
<!-- 											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="paymentTermsPage()" href="#">Payment Terms</a></li> -->
<!-- 											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="salesorderPage()" href="#">Sales Order</a></li> -->
											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="invoicePage()" href="#">Invoice</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="financeChargePage()" href="#">Finance Charges</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="taxAdjustments()" href="#">Tax Adjustments</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="creditDebitMemos()" href="#">Credit/Debit Memos</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a onclick="salesOrderTemplate()" href="#">Sales Order Template</a></li>
										</ul>
									</li>
									<li><a href="#" onclick="vendorPage()">Vendors</a>
										<ul class="child-node" style="width: 138px !important;">
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#"  onclick="purchaseOrderList()">Purchase Orders</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="payBillPage()" >Pay Bills</a></li>
											<li><a onclick="vendorInvoicesPage()" href="#">Invoices & Bills</a></li>
										</ul>
									</li>
									<li><a href="#" onclick="employeePage()">Employees</a>
										<ul class="child-node">
											<li><a href="#" onclick="commissionsPage()">Commissions</a></li>
										</ul>
									</li>
									<li><a href="#" onclick="rolodexPage()">Rolodex</a></li>
									<li><a href="#" onclick="userListPage()">Users</a></li>  
									<li><a href="#" onclick="companySettingPage()">Settings</a>
										<ul class="child-node" style="width: 138px !important;">
											<!-- <li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="getPage('./qbsettings')">QuickBooks</a></li> -->
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="getPage('./modelmaintenance')">Model Maintenance</a></li>
											<li style="border-radius: 10px 0px 0px 10px;"><a href="#" onclick="getPage('./scheduletemplates')">Schedule Templates</a></li>
											<li><a href="#" onclick="shipviaPage()">Ship Via</a></li>
										</ul>
									</li>
									<li><a href="#" onclick="chartsPage()">Chart Accounts</a></li>
									<li><a href="#" onclick="divisionsPage()">Divisions</a></li>
									<li><a href="#" onclick="taxTerritories()">Tax Territories</a></li>
									<!-- <li><a href="#" onclick="ledgerPage()">General Ledger</a></li> -->
									<li><a href="#" onclick="drillDownPage()">General Ledger</a></li>
									<li><a href="#" onclick="journalEntriesPage()">Journal Entries</a></li>
									<li><a href="#" onclick="accountCycles()">Accounting Cycles</a></li> 
									<li><a href="#" onclick="reportCriteriaPage()">Reports</a></li>
	<li><a href="#" onclick="glTransactionPage()">Gltransaction</a></li>  <!-- Demo edit by leo -->
								</ul>
							</li>
							<li id="mainmenuBankingPage"><a class="report" href="#" onclick="">Reports</a>
								<ul class="child"><li><a href="./openjobreport" onclick="">Open jobs</a></li>
								<li><a href="./bookreport" onclick="">Bookings</a></li>
								<li><a href="./openporeport" onclick="">Open PO</a></li>
								<li><a href="#" onclick="openPositivePay()">Positive Pay</a></li>
								</ul>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div align="center" class="headerMenuDivWidth" id="search">
			<table>
				<tr>
					<td>
						<label class="headerMenuSearchLabel"><b>Search:</b></label> &nbsp; 
						<input type="text" id="searchJob" placeholder="Minimum 3 characters required to get Job List"> &nbsp;
					</td>
					<td> 
						<input type="button" class="go turbo-tan" value="Go" onclick="getJobs()">
					</td>
					<td>
						<input type="button" id="advancedSearchButton" onclick="openAdvancedSearchPopup()" value="Advanced" class="cancelhoverbutton turbo-tan" style="height: 26px;">
					</td>
				</tr>
			</table>
			<table>
				<tr style="display: none;">
					<td  class="headerMenuPrevTD"> <b><a  id="previousId" href="javascript:previousJob()">Prevoius Job</a></b></td>
					<td  class="headerMenuNxtTD"> <b><a id="nextId" href="javascript:nextJob()">Next Job</a></b> </td>
				</tr>	
			</table>
		</div>
		<div id="searchAdvJob" style="display: none;">
			<form id="searchAdvJobForm">
				<div id="employeeDetailsFormGeneral">
					<table>
					  <tr>
			             <td ><label>Job #: </label></td>
			             <td class="space"><input type="text" class="" name="jobNumber_name"  id="jobNumberID"size="20" value="" /></td>
			             <td>&nbsp;</td>
			          </tr>
			          <tr>
			             <td ><label>City: </label></td>
			             <td class="space"><input type="text" name="city_name" id = "citynameID" size="20" value="" /></td>
			             <td>&nbsp;</td>
			          </tr>
			          <tr>
			              <td><label>Project Name: </label></td>
			              <td class="space"><input type="text" name="project_code" id = "projectID" size="20" value="" /></td>
			              <td>&nbsp;</td>
			              <td>&nbsp;</td>
			          </tr>
			          <tr>
			             <td ><label>Bid Date: </label></td>
			               <!-- <td><label>From </label><input type='text' class='datepicker' id='rangepickerID' name='rangepickerName' style='width:80px;'>
			             <label>To </label><input type='text' style='width:80px;' class='datepicker' id='thruPickerID' name='thruPickerName'></td>-->
			             <td colspan="2" style="padding-left:7px;">
			             <input type="checkbox" onchange="bidDateChange();" class="headermenuVerticalMidd" id="bidDateCheckbox" name="bidDateName" />
			             <label class="headermenuVerticalMidd">Range</label><div class="headermenuRangeDate" id="addDateRange"></div></td>
			             <td>&nbsp;</td>
			          </tr>
			          <tr>
			             <td ><label>Status: </label></td>
			             <td colspan="2" class="nospace">
			             	<table class="nospace">
			             		<tr class="nospace">
			             			<td class="nospace"  style="padding-left:6px;">
			             				<table class="nospace"><tr class="nospace">
					             			<td><input name="budget_name"  id="budgetnameID" type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Budget</label>&nbsp;</td>
					             			<td><input name="bid_name" id="bidnameID" type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Bid</label>&nbsp;</td>
					             			<td><input name="quote_name" id="quotenameID" type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Quote</label>&nbsp;</td>
					             			<td><input name="booked_name"  id="bookednameID"type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Booked</label>&nbsp;</td>
					             			<td><input name="closed_name"  id="closednameID" type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Closed</label>&nbsp;</td>
					             			<td><input name="submitted_name"  id ="submittednameID" type="checkbox" class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Submitted</label>&nbsp;</td>
			             			</tr></table>
			             			</td>
			             		</tr>
			             		<tr class="nospace">
			             			<td class="nospace" style="padding-left:6px;">
			             				<table class="nospace"><tr class="nospace">
					             			<td><input name="planning_name"  id="planningnameID" type="checkbox"  class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Planning</label>&nbsp;</td>
					             			<td><input name="lost_name"  id="lostnameID" type="checkbox"  class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Lost</label>&nbsp;</td>
					             			<td><input name="abondoned_name"  id="abondonednameID"  type="checkbox"  class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Abondoned</label>&nbsp;</td>
					             			<td><input name="reject_name"  id="rejectnameID" type="checkbox"  class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Reject</label>&nbsp;</td>
					             			<td><input name="overBudget_name"   id="overBudgetnameID" type="checkbox"  class="headermenuVerticalMidd" /><label class="headermenuVerticalMidd">Over Budget</label>&nbsp;</td>
			             				</tr></table>
			             			</td>
			             			
			             		</tr>
			             	</table>
			             </td>
			          </tr>
			          <tr>
						<td><select class="" id="teamStatusId"
						name="team_status_name" onchange="ChangeTeamDropDown(this.value)">
							<option value="1" onclick="">Customer</option>
							<option value="2" onclick="">Architect</option>
							<option value="3" onclick="">Engineer</option>
							<option value="4" onclick="">G.C.</option>
					</select></td>
					<td class="space">
						 <input type="text" id="customerId" style="width: 290px;" value="" placeholder="Minimum 2 characters required">
						 <input type="text" id="architectId" style="width: 290px; display:none;" value="" placeholder="Minimum 2 characters required" > <input type="text" id="architectRXId" class = "visible" style="display:none;">
						 <input type="text" id="engineerId"	style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required" > <input type="text" id="engineersRXId" style="display:none;">
						 <input type="text" id="gcId" style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required" ><input type="text" id="gcRXId" style="display:none;">
					</td>
						<td><div id="teamSelectLists"></div></td>
			          </tr>
			          <tr>
			              <td><select class=""
						id="employeeAssigneeId" name="employee_assignee_name"
						onchange="ChangeEmployeeDropDown(this.value)">
							<option value="1">Salesman</option>
							<option value="2">CSR</option>
							<option value="3">Sales Mgr.</option>
							<option value="4">Engineer</option>
							<option value="5">prj. Mgr.</option>
							<option value="6">Take off</option>
							<option value="7">Quote By</option>
					</select></td>
					<td class="space">
						<input type="text" id="salesRepEmpId" style="width: 290px;"value=""placeholder="Minimum 2 characters required"><input type="text" id="salesRepTextId" style="display:none;">
 						<input type="text" id="csrEmpId" style="width: 290px;  display: none;" value="" placeholder="Minimum 2 characters required" > <input type="text" id="csrTextId" style="display:none;">
						<input type="text" id="salesMgrEmpId"	style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required" ><input type="text" id="salesMgrTextId" style="display:none;">
						<input type="text" id="engineerEmpId" style="width: 290px;  display:none;" value=""placeholder="Minimum 2 characters required" ><input type="text" id="engineerEmpTextId" style="display:none;">
						<input type="text" id="prjMgrEmpId" style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required" ><input type="text" id="prjMgrTextId" style="display:none;">
						<input type="text" id="takeOffEmpId" style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required"><input type="text" id="takeOffTextId" style="display:none;">
						<input type="text" id="quoteByEmpId" style="width: 290px;  display:none;" value="" placeholder="Minimum 2 characters required"><input type="text" id="quoteByTextId" style="display:none;">
					</td>
			        <td><div id="assignedemployeeSelectLists"></div></td>
			          </tr>
			          <tr>
			              <td ><label>Customer PO #:</label></td>
			              <td class="space"><input name="customer_po_name" id="customerPoNameID" type="text" ></td>
			              <td>&nbsp;</td>
			          </tr>
			          <!--<tr>
			              <td><label>Report #:</label></td>
			              <td class="space"><input type="text" name="report_name" id="reportnameID"/></td>
			              <td>&nbsp;</td>
			          </tr> -->
			          <tr>
			              <td ><label>Division:</label></td>
			              <td class="space">
			              		<select class="" id="divisionID" name="division_name">
									<option value="-1"> - Select - </option>
									<c:forEach var="coDivision12" items="${requestScope.divisionsSearch}">
										<option value="${coDivision12.coDivisionId}">
											<c:out value="${coDivision12.description}" ></c:out>
										</option>
									</c:forEach>
								</select>
			              	</td>
			              <td><div id="divisionList"></div></td>
			          </tr>
					</table>
				</div>
				<hr>
				<table align="right">
			 	<tr>
			 	      <td ><label>Sort By:</label></td>
			              <td >
			              <select class="" id="sortbyId" name="sort_by_name">
			              	<!-- <option value = "-1">--Select--</option> -->
			              	<option value = "1">Bid Date</option>
			             	<option value = "2" selected="selected">Job #</option>
			             	<option value = "3">Project Name</option>
			              	<option value = "4">City</option>
			              	<option value = "5">Salesman</option>
			              	<option value = "6">Status</option>
			              	<option value = "7">Customer</option>
			              	<option value = "8">Customer P.O. #</option>
			              </select>
			              </td>
				 	<td align="right">
						<input type="button" class="cancelhoverbutton turbo-tan" value="Search" onclick="searchJOB()">
						<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelJOB()">  
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="systemAdministratorID" value= "${sessionScope.user.systemAdministrator}">
				</td>
			</tr>
		</table>
	</div>
	<div id="bidDateReport" style="display: none;">
			<form id="bidDateReportForm">
				<div id="bidDateReportMain">
					<table>
					  <tr>
					  	<td>
					  		<a href="./bidDateReport" style="color: blue;">1. Bid Date Report</a> 
					  	</td>
					  	<td></td>
					  </tr>
					  <tr>
					  	<td>
					  		<a href="./foreCastBySalesRep" style="color: blue;">2. Sales Forecast Report By Salesrep</a>
					  	</td>
					  	<td></td>
					  </tr>
					  <tr>
					  	<td>
					  		<a href="./foreCastByManufacturer" style="color: blue;">3. Sales Forecast Report By Manufacturer</a> <!-- ./foreCastByManufacturer -->
					  	</td>
					  	<td></td>
					  </tr>
					 </table>
					 <hr>
					  <table align="right">
					 	<tr>
					 		<td>
					 			<input type="button" id="bidDateID" value="Cancel" onclick="cancelReport()" class="cancelhoverbutton turbo-tan">
					 		</td>
					 	</tr>
					 </table>
				</div>
			</form>
		</div>
		<div style="display: none;">
			<table>
				<tr>
					<td>
						<input type="text" id="userLoginID">
					</td>
				</tr>
			</table>
		</div>
		<div style="display: none;">
			<input type="text" id="homeAccesProcedureID1">
			<input type="text" id="jobQuoteAccesProcedureID1">
			<input type="text" id="jobSubmittalAccesProcedureID1">
			<input type="text" id="jobCreditAccesProcedureID1">
			<input type="text" id="jobReleaseAccesProcedureID1">
			<input type="text" id="jobFinancialAccesProcedureID1">
			<input type="text" id="jobJournalAccesProcedureID1">
			<input type="text" id="salesAccesProcedureID1">
			<input type="text" id="companyCustomerAccesProcedureID1">
			<input type="text" id="companyPaymentAccesProcedureID1">
			<input type="text" id="companyVendorAccesProcedureID1">
			<input type="text" id="companyPayBillAccesProcedureID1">
			<input type="text" id="companyEmployeeAccesProcedureID1">
			<input type="text" id="companyCommisionsAccesProcedureID1">
			<input type="text" id="companyRolodexAccesProcedureID1">
			<input type="text" id="companyUserAccesProcedureID1">
			<input type="text" id="companySettingAccesProcedureID1">
			<input type="text" id="companyReportsAccesProcedureID1">
			<input type="text" id="companyAccountAccesProcedureID1">
			<input type="text" id="projectAccesProcedureID1">
			<input type="text" id="inventoryProcedureID1">
			<input type="text" id="ledgerID1">
			<input type="text" id="userLogin_Id" value="${sessionScope.user.userId}">
			<jsp:include page="./Email_Attachment.jsp"></jsp:include>
		</div>
		 <div id="PositivePayDialogBox" style="display: none;">
			<table>
			<tr>
			<td><label class="labelfont"><b>Check Date:</b><span style="color: red">*</span></label></td>
			<td><input type="text" name="positivepayDate" id="positivepayDate"/></td>
			</tr>
			<tr><td colspan="2" id="positivepayLabelID"></td></tr>
			</table>
	</div>
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/nicEdit-latest.min.js" ></script> 
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery-ui-1.8.23.custom.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bubbletip_js/jQuery.bubbletip-1.0.6.js" ></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery.ui.position.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/tinymce/tinymce.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/turbo_headermenu_script.js"></script> 
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/ckeditor/ckeditor.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/turbo_headermenu_script.min.js"></script> --> 
	<script type="text/javascript">
	var edd_object="";
	function TinyMCETextEditorForAllInitsetup(ed_width,ed_height,enableornot) {
		if(enableornot){
	tinymce.init({
		 mode : "specific_textareas",
	editor_selector : "TinyMCETextEditorForAll",
	   theme: "modern",
	   plugins: [
	        "advlist autolink lists link image charmap print preview hr anchor pagebreak",
	       "searchreplace wordcount visualblocks visualchars code fullscreen",
	       "insertdatetime media nonbreaking save table  directionality",
	       " template paste textcolor colorpicker textpattern tabindex tabfocus"
	   ],
	   toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor fontselect fontsizeselect",
	  	menubar: false,
	  	statusbar: false,
	  	/*toolbar1:"styleselect"
	        style_formats: [
					{title: 'Arial', inline: 'span', styles: { 'font-family':'arial'}},
					{title: 'Courier New', inline: 'span', styles: { 'font-family':'courier new,courier'}},
					{title: 'Helvetica', inline: 'span', styles: { 'font-family':'helvetica'}},
					{title: 'Times New Roman', inline: 'span', styles: { 'font-family':'times new roman,times'}}
	             {title: 'Open Sans', inline: 'span', styles: { 'font-family':'Open Sans'}},
	            {title: 'Arial', inline: 'span', styles: { 'font-family':'arial'}},
	            {title: 'Book Antiqua', inline: 'span', styles: { 'font-family':'book antiqua'}},
	            {title: 'Comic Sans MS', inline: 'span', styles: { 'font-family':'comic sans ms,sans-serif'}},
	            {title: 'Courier New', inline: 'span', styles: { 'font-family':'courier new,courier'}},
	            {title: 'Georgia', inline: 'span', styles: { 'font-family':'georgia,palatino'}},
	            {title: 'Helvetica', inline: 'span', styles: { 'font-family':'helvetica'}},
	            {title: 'Impact', inline: 'span', styles: { 'font-family':'impact,chicago'}},
	            {title: 'Symbol', inline: 'span', styles: { 'font-family':'symbol'}},
	            {title: 'Tahoma', inline: 'span', styles: { 'font-family':'tahoma'}},
	            {title: 'Terminal', inline: 'span', styles: { 'font-family':'terminal,monaco'}},
	            {title: 'Times New Roman', inline: 'span', styles: { 'font-family':'times new roman,times'}},
	            {title: 'Verdana', inline: 'span', styles: { 'font-family':'Verdana'}} 
	        ], */
	   toolbar_items_size: 'small',
	   force_br_newlines : true,
	   force_p_newlines : false,
	   forced_root_block : 'div',
	   width:ed_width,
	   height:ed_height,
	   setup: function (ed) {
           ed.on('keypress', function (e) {
           	//tinymce.activeEditor.selection.getNode().style.fontSize='10pt'; 
           	var text=$(tinymce.activeEditor.getContent()).text();
           	console.log("keypress="+text.trim().length+"=="+text+"=="+text.length);
	            if(text.trim().length==0 && tinyMCE.activeEditor.selection.getNode().style.fontFamily==""){
		            console.log("keypress");
	            	tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
	            	tinymce.activeEditor.execCommand("fontSize", false, "10pt");
	            	//myCustomInitInstance(ed);
		            }
               //your custom logic  
           });
           ed.on('keyup', function (e) {
           	//tinymce.activeEditor.selection.getNode().style.fontSize='10pt'; 
           	var text=$(tinymce.activeEditor.getContent()).text();
           	console.log("keypress"+text.trim().length+"=="+text+"=="+text.length);
	            if(text.trim().length==0 && tinyMCE.activeEditor.selection.getNode().style.fontFamily==""){
	            	console.log("keyup");
	            	tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
	            	tinymce.activeEditor.execCommand("fontSize", false, "10pt");
	            	//myCustomInitInstance(ed);
		            }
               //your custom logic  
           });
       },
	  /*  setup: function (ed) {
	       ed.on('keypress', function (e) {
	           if(tinymce.activeEditor.getContent()==""||(tinymce.activeEditor.selection.getNode().style.fontSize=="" && tinymce.activeEditor.selection.getNode().style.fontFamily=="")){
		            }
	           //your custom logic  
	       });
	       
	   }, */
	   init_instance_callback : "overallCustomInitInstance",
	   theme_advanced_buttons3_add : "pastetext,pasteword,selectall",
	   paste_auto_cleanup_on_paste : true,
	   paste_retain_style_properties: "all",
	   /*paste_remove_styles: true,
	paste_remove_styles_if_webkit: true,
	paste_strip_class_attributes: true, */
	   paste_preprocess : function(pl, o) {
	       // Content string containing the HTML from the clipboard
	      // alert("preprocess"+o.content);
	      var pastevalue=o.content;
	      pastevalue=pastevalue.replaceAll("<p", "<div ").replaceAll("</p>", "</div>");
	       o.content =pastevalue;
	       
	       
	       //tinymce.activeEditor.setContent(o.content);
	        } ,
	  paste_postprocess : function(pl, o) {
		            // Content string containing the HTML from the clipboard
		            // alert("postprocess"+o.node.innerHTML);
		           //o.node.innerHTML = o.node.innerHTML;
			         } 
	  
	});
	}else{
		tinymce.remove();
		}
	}
	function overallCustomInitInstance(ed) { 
		edd_object=ed;
			  try{
				//ed.selection.getNode().style.fontSize='10pt';
				//ed.selection.getNode().style.fontName='Helvetica';
				if(defaultfont_set){
					console.log("Initialize font");
					tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
	            	tinymce.activeEditor.execCommand("fontSize", false, "10pt");
					defaultfont_set=false; 
					}
			  }
			  catch(err){      
			  }
	}
	$(function() { var cache = {}; var lastXhr='';
	$( "#salesRepEmpId" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#salesRepTextId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });
	  $(function() { var cache = {}; var lastXhr='';
		$( "#csrEmpId" ).autocomplete({ minLength: 2, timeout :1000,
			select: function( event, ui ) {var id = ui.item.id; $("#csrTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	  $(function() { var cache = {}; var lastXhr='';
		$( "#salesMgrEmpId" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) {var id = ui.item.id; $("#salesMgrTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	  $(function() { var cache = {}; var lastXhr='';
		$( "#engineerEmpId" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#engineerEmpTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	  $(function() { var cache = {}; var lastXhr='';
		$( "#prjMgrEmpId" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#prjMgrTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	  $(function() { var cache = {}; var lastXhr='';
		$( "#takeOffEmpId" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) {var id = ui.item.id; $("#takeOffTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	  $(function() { var cache = {}; var lastXhr='';
		$( "#quoteByEmpId" ).autocomplete({ minLength: 2, timeout :1000,
			select: function( event, ui ) {var id = ui.item.id; $("#quoteByTextId").val(id);},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });


	  
		function getJobs() {
			var jobSearch = $('#searchJob').val();
			if(jobSearch === ""){
				errormessage="please select a job to search";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span style="color:red;"><b>'+errormessage+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Information",buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		
				return false;
			}
			
			$.ajax({
				url: "./search/searchjob",
				mType: "GET",
				data : {'jobsearch': jobSearch},
				success: function(result){
					createtpusage('Job','Inside Job','Info','Job,Searching Job,Job:'+jobSearch);
					/* var searchArray = new Array();
					$.each(result, function(index, value) { 
						searchArray[index] = value.searchText;
					}); */
					if(result==null || result.length==0){
						errormessage="Please provide a valid job from the suggested list.";
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errormessage+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Information",buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				
					}
					
					if(result.length==1){
						var job = result[0].searchText;
						var number = job.split("|");
						var jobNumber = number[0];
						var jobName = number[1];
						jobName = jobName.replace(/&/g, "``");
						jobName = jobName.replace(/#/, "__");
						var joMasterID=result[0].pk_fields;
						getjobSubMethod(jobNumber,jobName,joMasterID);
					}else{
						$("#projectID").val(jobSearch);
						searchJOB();
					}
					
				},
				error: function(Xhr) {
					var errorText = $(Xhr.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Fatal Error.",buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}	
			});
			return true;
		}

		function getjobSubMethod(jobNumber,jobName){
			$.ajax({
				url: "./search/searchjobcustomer",
				mType: "GET",
				data : {'jobnumber': jobNumber, 'jobname': jobName},
				success: function(result){
					$.each(result, function(index, value){
						status = value.jobStatus;
						jobCustomer = value.customerName;
					});
					
					if(jobCustomer === null) {
						jobCustomer = "";
					}
					var urijobname=encodeBigurl(jobName);
					var uricusname=encodeBigurl(jobCustomer);
					document.location.href="./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+"";
				},
				error: function(Xhr) {
				}
			});
		}
		function openPositivePay(){
			jQuery("#PositivePayDialogBox").dialog("open");
		}			
		var ckEditorconfig = {
        	    height:'400',
        	    startupOutlineBlocks:false,
        	    scayt_autoStartup:true,
        	    pasteFromWordPromptCleanup:true,
        	    pasteFromWordRemoveFontStyles:true, 
        	    pasteFromWordRemoveStyles:false,
        	    disableNativeSpellChecker:true,
        	    scayt_minWordLength:3, 
        	    font_defaultLabel : 'Times New Roman',
        	    fontSize_defaultLabel:'10',
        	   coreStyles_underline:{ element: 'span',overrides: 'u', styles: { 'text-decoration' : 'underline' },
            	    childRule: function( element ) {
        	        return false;
        	    }  },
        	    /* coreStyles_bold = 
        	    { 
        	       element: 'strong', 
        	       overrides: 'b', 
        	       childRule: function( element ) {
        	          return false;
        	       } 
        	    },  */
        	    allowedContent:true,
        	    /* font_style:{
				    element:        'span',
				    styles:         { 'line-height ':'1.6px' },
				    overrides:      [ { element: 'font', attributes: { 'face': null } } ]
				}, */
        	    //font_names: "Arial/arial,sans-serif;Times New Roman/times new roman,'serif';Helvetica/helvetica,'sans-serif'",
        	    fontSize_sizes:'6/6.0pt;7/7.0pt;8/8.0pt;9/9.0pt;10/10.0 pt;11/11.0pt;12/12.0pt;14/14.0pt;16/16.0pt;18/18.0pt;20/20.0pt;22/22.0pt;24/24.0pt;26/26.0pt;28/28.0pt;36/36.0pt;48/48.0pt;72/72.0pt;',
        	    removePlugins:'sourcearea,save,htmldataprocessor',
        	    toolbar_Full:[
        	        { name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
	{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
	{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
	{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
	'/',
	{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
	{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
	{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
	{ name: 'insert', items: [ 'Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe' ] },
	'/',
	{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
	{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
	{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
	{ name: 'others', items: [ '-' ] },
	{ name: 'about', items: [ 'About' ] }
        	    ]
        	}


// For Inline note config ckeditor
var ckEditorconfigforinline = {
	    height:'200',
	    startupOutlineBlocks:false,
	    scayt_autoStartup:false,
	    pasteFromWordPromptCleanup:true,
	    pasteFromWordRemoveFontStyles:true, 
	    pasteFromWordRemoveStyles:false,
	    disableNativeSpellChecker:true,
	    scayt_minWordLength:3, 
	    font_defaultLabel : 'Times New Roman',
	    fontSize_defaultLabel:'10',
	    /* coreStyles_underline:{ element: 'span',overrides: 'u', styles: { 'text-decoration' : 'underline' },
    	    childRule: function( element ) {
	        return false;
	    }  }, */
	    coreStyles_bold:{ element: 'b', overrides: 'strong' },
	    allowedContent:true,
	    enterMode :3,
	    forceEnterMode :3,
	    autoParagraph:false,
	    contentsCss : CKEDITOR.basePath + '\inline_contents.css',
	   	coreStyles_italic : {element: 'span',attributes: { 'style': 'font-style: italic;'},overrides: 'em'},
	   // font_names: "Arial/arial,sans-serif !important;Times New Roman/times new roman,'serif' !important;Helvetica/helvetica,'sans-serif' !important",
	    fontSize_sizes:'6/6.0pt;7/7.0pt;8/8.0pt;9/9.0pt;10/10.0 pt;11/11.0pt;12/12.0pt;14/14.0pt;16/16.0pt;18/18.0pt;20/20.0pt;22/22.0pt;24/24.0pt;26/26.0pt;28/28.0pt;36/36.0pt;48/48.0pt;72/72.0pt;',
	    removePlugins:'sourcearea,save,htmldataprocessor',
	    removeButtons:'Save,NewPage,Preview,Print,Templates,Cut,Copy,Paste,PasteText,PasteFromWord,Find,Replace,SelectAll,Scayt,Undo,Redo,Form,Checkbox,Radio,TextField,Textarea,Select,Button,ImageButton,HiddenField,Link,Unlink,Anchor,Image,Flash,Table,HorizontalRule,Smiley,SpecialChar,PageBreak,Iframe,Maximize,ShowBlocks,About,Language',
	    toolbar_Full:[
	        { name: 'document', groups: [ 'mode', 'document', 'doctools' ], items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
{ name: 'clipboard', groups: [ 'clipboard', 'undo' ], items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker' ], items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
{ name: 'forms', items: [ 'Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField' ] },
'/',
{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ], items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat' ] },
{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi' ], items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
{ name: 'links', items: [ 'Link', 'Unlink', 'Anchor' ] },
{ name: 'insert', items: [ 'Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak', 'Iframe' ] },
'/',
{ name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize' ] },
{ name: 'colors', items: [ 'TextColor', 'BGColor' ] },
{ name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
{ name: 'others', items: [ '-' ] },
{ name: 'about', items: [ 'About' ] }
	    ]
	}

    	

	function scrollToRow (targetGrid, id,scrollPosition) {
		
		var index =jQuery(targetGrid).getInd(id);
		
		if(scrollPosition!="-1")
	    jQuery(targetGrid).closest(".ui-jqgrid-bdiv").scrollTop(scrollPosition);
		else
		jQuery(targetGrid).closest(".ui-jqgrid-bdiv").scrollTop(200*index);	
	}
    	
	</script>	
	