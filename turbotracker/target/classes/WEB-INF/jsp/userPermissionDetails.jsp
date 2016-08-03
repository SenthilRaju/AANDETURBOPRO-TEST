<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
	<!-- <div id="demo1">
				<ul>
					<c:forEach var="coDivisionBean" items="${requestScope.userAccessModule}">
							<li>
								<label class="toggle-button"></label><label id="userAccessModule">${coDivisionBean.moduleName}</label>
							</li>
							<option value="${coDivisionBean.accessModuleID}">
								<c:out value="${coDivisionBean.moduleName}" ></c:out>
							</option> 
						</c:forEach>
				</ul>
	</div> -->
	<div id="systemPermissionLable"></div>
	<div id="systemAccessID">
		<form id="groupDefaultsForm" name="groupDefaultsForm">
			<table>	
			<tr>
				<td width="230px;" valign="top" >
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Company</label>
							</td>
						</tr>
						<tr>
							<td>
							<!-- GroupByprocedureSelect('Customer') -->
								<input type="checkbox" id="CompanyCustomerID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Customers')">
								<label style="vertical-align: middle;">Customers</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyPaymentsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Payments')">
								<label style="vertical-align: middle;">Payments</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyStatementsID" name="CompanyPaymentsName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Statements')">
								<label style="vertical-align: middle;">Statements</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanySales_OrderID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Sales_Order')">
								<label style="vertical-align: middle;">Sales Order</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyInvoiceID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Invoice') ">
								<label style="vertical-align: middle;">Invoice</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyFinance_ChangesID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Finance_Changes') ">
								<label style="vertical-align: middle;">Finance Charges</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyTax_AdjustmentsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Tax_Adjustments') ">
								<label style="vertical-align: middle;">Tax Adjustments</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyCreditDebit_MemosID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'CreditDebit_Memos')">
								<label style="vertical-align: middle;">Credit/Debit Memos</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanySales_Order_TemplateID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Sales_Order_Template')">
								<label style="vertical-align: middle;">Sales Order Template</label>
							</td>
						</tr>
						<tr>
							<td><!-- GroupByprocedureSelect('Vendor') -->
								<input type="checkbox" id="CompanyVendorID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Vendors')">
								<label style="vertical-align: middle;">Vendors</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyPurchase_OrdersID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Purchase_Orders')">
								<label style="vertical-align: middle;">Purchase Orders</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyPay_BillsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Pay_Bills')">
								<label style="vertical-align: middle;">Pay Bills</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyInvoice_BillsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Invoice_Bills')">
								<label style="vertical-align: middle;">Invoice & Bills</label>
							</td>
						</tr>
						<tr>
							<td><!--GroupByprocedureSelect('Employee')   -->
								<input type="checkbox" id="CompanyEmployeesID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Employees')">
								<label style="vertical-align: middle;">Employees</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="CompanyCommissionsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Commissions')">
								<label style="vertical-align: middle;">Commissions</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyRolodexID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Rolodex') ">
								<label style="vertical-align: middle;">Rolodex</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyUsersID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Users')">
								<label style="vertical-align: middle;">Users</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanySettingsID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Settings')  ">
								<label style="vertical-align: middle;">Settings</label>
							</td>
						</tr>

					</table>
				</td>
				<td width="175px;" valign="top" >
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Home</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="HomenewJobID"  style="vertical-align: middle;" class="userPermission" onclick="updateUserPermission(this.id,'New_Job')">
								<label style="vertical-align: middle;">New Job</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="HomeQuick_bookID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Quick_book')">
								<label style="vertical-align: middle;">Quick book</label>
							</td>
						</tr>
						<tr height="20px;"></tr>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Job</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobMainID"  style="vertical-align: middle;" class="userPermission" onchange="onclickjobmaintab(this.id,'Main')">
								<label style="vertical-align: middle;">Main</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobQuotesID"  style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Quotes')">
								<label style="vertical-align: middle;">Quotes</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobSubmittalID" style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Submittal') ">
								<label style="vertical-align: middle;">Submittal</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobCreditID"  style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Credit') ">
								<label style="vertical-align: middle;">Credit</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobReleaseID"  style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Release')"><label style="vertical-align: middle;">Release</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobFinancialID"  style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Financial')"><label style="vertical-align: middle;">Financial</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobJournalID" style="vertical-align: middle;" class="userPermission" onchange="updateJobTabClick(this.id,'Journal')"><label style="vertical-align: middle;">Journal</label>
							</td>
						</tr>
					</table>
				</td>
				<td width="200px;" valign="top" >
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Sales</label>
							</td>
						</tr>
						<tr>
							<td><!--GroupByprocedureSelect('Sales')  -->
								<input type="checkbox" id="salesID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Sales')">
								<label style="vertical-align: middle;">Sales</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="sales_FilterID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Sales_Filter')  ">
								<label style="vertical-align: middle;">Filter</label>
							</td>
						</tr>
						<tr height="20px"></tr>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Financial</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialChart_AccountsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Chart_Accounts') ">
								<label style="vertical-align: middle;">Chart Accounts</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialDivisionsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Divisions')  ">
								<label style="vertical-align: middle;">Divisions</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialTax_TerritoriesID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Tax_Territories')">
								<label style="vertical-align: middle;">Tax Territories</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialGeneral_LedgerID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'General_Ledger')">
								<label style="vertical-align: middle;">General Ledger</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialJournal_EntriesID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Journal_Entries') ">
								<label style="vertical-align: middle;">Journal Entries</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialAccounting_CyclesID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Accounting_Cycles')">
								<label style="vertical-align: middle;">Accounting Cycles</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="FinancialGL_TransactionsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'GL_Transactions')">
								<label style="vertical-align: middle;">GL Transactions</label>
							</td>
						</tr>	
						<tr>
							<td>
								<input type="checkbox" id="FinancialOpenPeriod_PostingID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'OpenPeriod_PostingOnly')">
								<label style="vertical-align: middle;">O.P. Posting Only</label>
							</td>
						</tr>					
					</table>
				</td>
				<td width="230px;" valign="top" >
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Project</label>
							</td>
						</tr>
						<tr>
							<td><!-- GroupByprocedureSelect('Project') -->
								<input type="checkbox" id="projectID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Project')"><label style="vertical-align: middle;">Project</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="project_FilterID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Project_Filter') ">
								<label style="vertical-align: middle;">Filter</label>
							</td>
						</tr>
						<tr height="20px"></tr>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Banking</label>
							</td>
						</tr>
						<tr>
							<td><!-- GroupByprocedureSelect('Banking') -->
								<input type="checkbox" id="BankingID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Banking')">
								<label style="vertical-align: middle;">Banking</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="BankingWrite_ChecksID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Write_Checks')">
								<label style="vertical-align: middle;">Write Checks</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="BankingReissue_ChecksID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Reissue_Checks')">
								<label style="vertical-align: middle;">Reissue Checks</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="BankingRecouncile_AccountsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Recouncile_Accounts')">
								<label style="vertical-align: middle;">Recouncile Accounts</label>
							</td>
						</tr>
					</table>
				</td>
<!-- 				<td width="200px;" valign="top" > -->
<!-- 					<table>						 -->
						
<!-- 						<tr height="20px;"></tr> -->
											
<!-- 					</table> -->
<!-- 				</td> -->
				
				<div id="loading" style="display: none; width: 100%; height: 100%; position: absolute; z-index: 1000;" align="center">
					<img src="../resources/scripts/jquery-autocomplete/loading.gif"/>
				</div>
				<td width="230px;" valign="top" >
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Inventory</label>
							</td>
						</tr>
						<tr>
							<td><!--GroupByprocedureSelect('Inventory')  -->
								<input type="checkbox" id="inventoryID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Inventory') ">
								<label style="vertical-align: middle;">Inventory</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryCategoriesID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Categories')">
								<label style="vertical-align: middle;">Categories</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryWarehousesID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Warehouses')">
								<label style="vertical-align: middle;">Warehouses</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryReceive_InventoryID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Receive_Inventory') ">
								<label style="vertical-align: middle;">Receive Inventory</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryTransferID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Transfer')">
								<label style="vertical-align: middle;">Transfer</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryOrder_PointsID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Order_Points')">
								<label style="vertical-align: middle;">Order Points</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryInventory_ValueID" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Inventory_Value')">
								<label style="vertical-align: middle;">Inventory Value</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryCountID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Count')  ">
								<label style="vertical-align: middle;">Count</label>
							</td>
						</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryTransactionsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Transactions') ">
								<label style="vertical-align: middle;">Transactions</label>
							</td>
			 			</tr>
						<tr>
							<td>&nbsp;
								<input type="checkbox" id="inventoryAdjustmentsID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Adjustments') ">
								<label style="vertical-align: middle;">Adjustments</label>
							</td>
			 			</tr>
						<tr height="20px;"></tr>
						
					</table>
				</td>
<!-- 				<td width="200px;" valign="top" > -->
<!-- 					<table> -->
						
<!-- 					</table> -->
<!-- 				</td> -->
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<input class="quickQuotehoverbutton turbo-blue" id ="allowId" name="allowName" type="button" style="width:100px;" value="Allow" onclick="AllowClickEvent('Allow')">
					<input class="quickQuotehoverbutton turbo-blue" id ="denyId" name="denyName" type="button" style="width:100px;" onclick="AllowClickEvent('Deny')" value="Deny">
					<input class="quickQuotehoverbutton turbo-blue" id ="groupDefaultId" name="groupDefaultName" type="button" style="width:120px;" onclick="GroupDefaultEvent('Group Default')" value="Group Default">
					<input class="quickQuotehoverbutton turbo-blue" id ="groupDefaultId" name="groupDefaultName" type="button" style="width:145px;" onclick="GroupDefaultEvent('Reset Group Default')" value="Reset Group Default">
				</td>
			</tr>
		</table>
		</form>
	</div>
	<div style="display: none;">
		<input type="text" id="loginID" value="${sessionScope.user.userId}">
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/userPermissions.js">	</script>
	</body>
	</html>