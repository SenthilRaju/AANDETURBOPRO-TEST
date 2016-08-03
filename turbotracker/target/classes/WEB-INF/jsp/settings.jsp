<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/tinymce/tinymce.min.js"></script>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Settings Tab</title>
<style type="text/css">
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			.ui-state-default{background-color: E8E3E3;}
			.cke_contents {
				height: 100px !important;
			}
			#cke_headerTextId, #cke_termId, #cke_footerTextId,#cke_footerId
			{
			width:800px !important;
			}
		</style>
</head>
<body>
<div  style="background-color: #FAFAFA">
<div>
	 <jsp:include page="./headermenu.jsp"></jsp:include> 
</div>
<div class="tabs_main" id="settings_tabs" style="padding-left: 0px;width:1110px;margin:0 auto; background-color: #FAFAFA;height: 1650px;right:8px; box-shadow: 1px 6px 5px 5px #AAAAAA;">
	<ul>
		<li id="settingsFormDetails"><a href="#settingsFormDetailsBlock">Company Settings</a></li>
		<li id="settingsCustmerDetails"><a href="#settingsCustmerDetailsBlock">Customer Settings</a></li>
		<li id="settingsVendorDetails"><a href="#settingsVendorDetailsBlock">Vendor Settings</a></li>
		<li id="settingsEmployeeDetails"><a href="#settingsEmployeeDetailsBlock">Employee Settings</a></li>
		<li id="settingsJobDetails"><a href="#settingsJobDetailsBlock">Job Settings</a></li>
		<li id="settingsInventoryDetails"><a href="#settingsInventoryDetailsBlock">Inventory Settings</a></li>
		<li id="settingsBankingDetails"><a href="#settingsBankingDetailsBlock">Banking Settings</a></li>
		
	</ul>
	<br><br>
	
	<div id="groupDefaults">
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
							<td><!-- GroupByprocedureSelect('Employee') -->
								<input type="checkbox" id="CompanyEmployeesID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Employees') ">
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
							<td><!-- GroupByprocedureSelect('Sales')  -->
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
							<td><!--GroupByprocedureSelect('Banking')-->
								<input type="checkbox" id="BankingID" style="vertical-align: middle;" class="userPermission" onchange=" updateUserPermission(this.id,'Banking') ">
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
							<td><!-- GroupByprocedureSelect('Inventory')  -->
								<input type="checkbox" id="inventoryID"  style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission(this.id,'Inventory')  ">
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
		</form>
	</div>	
	<div id="settingsFormDetailsBlock" >
		<div align="right" style="margin-top: -12px;">
			<img alt="Export Csv" src="./../resources/images/csv_text.png" width="25" onclick="exportCompanyContacts()"></td>
		</div>
		<div id="uploadExcel_Form" style="width: 750px;" align="center">
			<form id="form2" method="post" action="" enctype="multipart/form-data">
				<!-- File input -->    
				<label>Upload Company Logo: </label> <input name="file2" id="file2" type="file" style="height:25px;" />
			</form>
			<button value="Upload" class="turbo-tan savehoverbutton" style="margin-left: 330px;margin-top: 3px;" onclick="uploadJqueryForm()"  >Upload</button>
		</div>
	
		<form action="" id="companyFormId" name="companyForm">
  	  		<table align="left">
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width:978px;height:796px;border:0px">
							<table>
								<!-- <tr style="height: 20px;"><td></td></tr> -->
			     				<tr align="center">
				      				<!-- <td>
				             				<label>Upload Company Logo:</label>
				    				<input id="avatar" type="file" name="avatar" size="30" style="height:21px;">
				    				<button class="turbo-blue" style="height: 21px;color: white;display: none;" id="upload">Upload</button>
								    <input type="button" class ="turbo-blue" value="Upload" id="ucompanydetailspload" name="uploadimage"style="height:21px;color:white;" onclick="uploadImage()">
				          			</td>  -->
				          			<td>
				          				<div id="showImage" >
									   		<img src="./companyLogo" style="width: 200px; height: 150px; position: absolute; left: 620px; top: 38px;"/>
								    	</div>
									</td>
									<td>
										<table style="margin-top:-69px;float:right">
											<tr>
												<td>
													<input type="checkbox" id="1quote" name="quote" style="vertical-align: middle;" >
													<label>Quote</label>
												</td>
											</tr>
											<tr>
												<td>
													<input type="checkbox" id="1quickquote" name="quickquote" style="vertical-align: middle;" >
													<label>Quick Quote</label>
												</td>
											</tr>
											<tr>
												<td>
														<input type="checkbox" id="1invoices" name="invoices" style="vertical-align: middle;" >
														<label>Invoices</label>
												</td>
											</tr>
											<tr>
												<td>
													<input type="checkbox" id="1puchaseorder" name="puchaseorder" style="vertical-align: middle;" >
													<label>Purchase Orders</label>
												</td>
											</tr>									
										</table>
									</td>
								</tr>
								
								<tr align="center">
									<td colspan="2">
										<table style="padding-top: 25px;margin-left: -128px;">
											<tr>
			 									<td style="width: 160px;text-align: right;"><label>Header:</label></td>
												<%-- <td><textarea cols="70" id="headerTextId" style="height: 90px;">${requestScope.headerTextSettings}</textarea></td> --%>
												<td><textarea cols="70" id="headerTextId" style="height: 90px;">${requestScope.headerTextSettings}</textarea></td>
				 								<td>
					 								<table>
						 								<tr>
							 								<td>
																<input type="checkbox" id="quote11" name="quote1" style="vertical-align: middle;" >
																<label>Quote</label>
															</td>
														</tr>
						 								<tr>
						 									<td>
																<input type="checkbox" id="quickquote11" name="quickquote1" style="vertical-align: middle;" >
																<label>Quick Quote</label>
															</td>
														</tr>
						 								<tr>
						 									<td>
																<input type="checkbox" id="invoices11" name="invoices1" style="vertical-align: middle;" >
																<label>Invoices</label>
															</td>
														</tr>
						 								<tr>
						 									<td>
																<input type="checkbox" id="puchaseorder11" name="puchaseorder1" style="vertical-align: middle;" >
																<label>Purchase Orders</label>
															</td>
														</tr>
					 								</table>
				 								</td>
				 							</tr>
				 						</table>
				 					</td>
			 					</tr>
					 					
								<tr align="center">
									<td colspan="2">
										<table style="margin-left: -128px;">
											<tr>
												<td style="width: 160px;text-align: right;"><label>Terms:</label></td>
												<td>
												<textarea cols="70" id="termId" style="height: 90px;">${requestScope.termsTextSettings}</textarea>
												</td>
												<td>
												<table>
												<tr><td>
														<input type="checkbox" id="quote21" name="quote2" style="vertical-align: middle;" >
													    <label>Quote</label>
													</td>
												</tr>
												<tr><td>
														<input type="checkbox" id="quickquote21" name="quickquote2" style="vertical-align: middle;" >
														<label>Quick Quote</label>
													</td>
												</tr>
												<tr><td>
														<input type="checkbox" id="invoices21" name="invoices2" style="vertical-align: middle;" >
														<label>Invoices</label>
													</td>
												</tr>
												<tr><td>
														<input type="checkbox" id="puchaseorder21" name="puchaseorder2" style="vertical-align: middle;" >
														<label>Purchase Orders</label>
													</td>
												</tr>
												<!-- <tr><td>
												<img alt="Export Csv" src="./../resources/images/csv_text.png" width="25" onclick="exportCompanyContacts()"></td>
												</tr> -->
												</table>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td colspan="2"><table><tr>
									<td>
										<fieldset class="ui-widget-content ui-corner-all" style="margin-left: -6px;width:275px; height: 154px;">
											<legend class="custom_legend"><label><b>Bill To</b></label></legend>
											<table>
												<tr>
													<td>
														<table>
															<tr>
															
										 						<td><input type="text" id="billToAddressID" name="billToAddressName" class="validate[maxSize[100]" style="width: 250px;" value="${requestScope.userLoginSettings.billToDescription}"></td>
										 					</tr>
											 				<tr>
											 					<td><input type="text" id="billToAddressID1" name="billToAddress1Name" class="validate[maxSize[100]" style="width: 250px;" value="${requestScope.userLoginSettings.billToAddress1}"></td>
											 				</tr>
											 				<tr>
																<td><input type="text" id="billToAddressID2" name="billToAddress2Name" class="validate[maxSize[40]" style="width: 250px;" value="${requestScope.userLoginSettings.billToAddress2}"></td>
															</tr>
										 					<tr>
										 						<td><input type="text" id="billToCity" name="billToCityName" style="width: 78px;" value="${requestScope.userLoginSettings.billToCity}">
																		<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
										 								<input type="text" id="billToState" name="billToStateName" style="width: 30px; text-transform: uppercase" maxlength="2" value="${requestScope.userLoginSettings.billToState}">
										 								<label>Zip: </label><input type="text" id="billToZipID" name="billToZipName" style="width: 75px;" value="${requestScope.userLoginSettings.billToZip}">
										 						</td>
										 					</tr>
														</table>
													</td>
								 				  </tr>
											</table>
										</fieldset>
									</td>
									<td>
										<fieldset class="ui-widget-content ui-corner-all" style="width:275px; height: 154px;">
											<legend class="custom_legend"><label><b>Remit To</b></label></legend>
											<table>
												<tr>
													<td>
														<table>
															<tr>
															
										 						<td><input type="text" id="remitToAddressID" name="remitToAddressName" class="validate[maxSize[100]" style="width: 250px;" value="${requestScope.userLoginSettings.remitToDescription}"></td>
										 					</tr>
											 				<tr>
											 					<td><input type="text" id="remitToAddressID1" name="remitToAddress1Name" class="validate[maxSize[100]" style="width: 250px;" value="${requestScope.userLoginSettings.remitToAddress1}"></td>
											 				</tr>
											 				<tr>
																<td><input type="text" id="remitToAddressID2" name="remitToAddress2Name" class="validate[maxSize[40]" style="width: 250px;" value="${requestScope.userLoginSettings.remitToAddress2}"></td>
															</tr>
										 					<tr>
										 						<td><input type="text" id="remitToCity" name="remitToCityName" style="width: 78px;" value="${requestScope.userLoginSettings.remitToCity}">
																		<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
										 								<input type="text" id="remitToState" name="remitToStateName" style="width: 30px; text-transform: uppercase" maxlength="2" value="${requestScope.userLoginSettings.remitToState}">
										 								<label>Zip: </label><input type="text" id="remitToZipID" name="remitToZipName" style="width: 75px;" value="${requestScope.userLoginSettings.remitToZip}">
										 						</td>
										 					</tr>
														</table>
													</td>
								 				  </tr>
											</table>
										</fieldset>
									</td>
											
									<td>
										<!-- <fieldset class="ui-widget-content ui-corner-all" style="height: 130px;margin-left: 8px;"> -->
										<fieldset class="ui-widget-content ui-corner-all" style="width: 430px;height: 154px;">
										<legend class="custom_legend"><label><b>Chart of Accounts Segments</b></label></legend>
										<table style = "width: 100%;">
											<tr>
												<td></td>
												<td></td>
												<td  align="center">Required?</td>
												<!-- <td>&nbsp;&nbsp;&nbsp;</td> -->
												<td  align="center"># of Digits</td>
											</tr>
										
											<tr>
												<td>Segment #1:</td>
												<td><input type="text" name="segment1" id="segment1id" style="width:125px;"  maxlength="20" /></td>
												<!-- <td>&nbsp;&nbsp;&nbsp;</td> -->
												 <td></td>
												<td align="center">
													<input type="text" name="digits1" id="digits1id" style="width:20px;text-align: center"  maxlength="1" onkeypress="return numbersonly(event)" /> 
												</td>
											</tr>
											
											<tr>
												<td>Segment #2:</td>
												<td><input type="text" name="segment2" id="segment2id" style="width:125px;" maxlength="20" /></td>
												<td>
													<input type="checkbox" name="required1" id="required1yesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
													<input type="checkbox" name="required1" id="required1noid" value = "No" /> <span style="vertical-align: 5px;">No</span>
												</td>
												<!-- <td>&nbsp;&nbsp;&nbsp;</td> -->
												<td align="center">
													<input type="text" name="digits2" id="digits2id" style="width:20px;text-align: center" maxlength="1" onkeypress="return numbersonly(event)" /> 
												</td>
											</tr>
											
											<tr>
												<td>Segment #3:</td>
												<td>
													<input type="text" name="segment3" id="segment3id" style="width:125px;" maxlength="20" /> 
												</td>
												<td>
													<input type="checkbox" name="required2" id="required2yesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
													<input type="checkbox" name="required2" id="required2noid" value = "No" /> <span style="vertical-align: 5px;">No</span>
												</td>
												<!-- <td>&nbsp;&nbsp;&nbsp;</td> -->
													<td align="center">
													<input type="text" name="digits3" id="digits3id" style="width:20px;text-align: center" maxlength="1" onkeypress="return numbersonly(event)"/> 
												</td>
											</tr>
											
											<tr>
												<td>Segment #4:</td>
												<td>
													<input type="text" name="segment4" id="segment4id" style="width:125px;" maxlength="20" /> 
												</td>
												<td>
													<input type="checkbox" name="required3" id="required3yesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
													<input type="checkbox" name="required3" id="required3noid" value = "No" /> <span style="vertical-align: 5px;">No</span> 
												</td>
												<!-- <td>&nbsp;&nbsp;&nbsp;</td> -->
												<td align="center">
													<input type="text" name="digits4" id="digits4id" style="width:20px;text-align: center" maxlength="1" onkeypress="return numbersonly(event)"/> 
												</td>
											</tr>
											
										</table>
										<div style="margin-top:70px;"><span style="color:red;" id="digstatus">Note: 1 to 5 numbers only allowed. </span></div>
									<!-- </fieldset> -->
										<!-- <legend class="custom_legend"><label><b>Ship To</b></label></legend>
										<table>
										<tr><td>
											<table>
												<tr>
							 						<td><input type="text" id="NorcrossbillToAddressID" name="NorcrossbillToAddressName" class="validate[maxSize[100]" style="width: 300px;"></td>
							 					</tr>
								 				<tr>
								 					<td><input type="text" id="NorcrossbillToAddressID1" name="NorcrossbillToAddress1Name" class="validate[maxSize[100]" style="width: 300px;"></td>
								 				</tr>
								 				<tr>
													<td><input type="text" id="NorcrossbillToAddressID2" name="NorcrossbillToAddress2Name" class="validate[maxSize[40]" style="width: 300px;"></td>
												</tr>
							 					<tr>
							 						<td><input type="text" id="NorcrossbillToCity" name="NorcrossbillToCityName" style="width: 100px;">
															<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
							 								<input type="text" id="NorcrossbillToState" name="NorcrossbillToStateName" style="width: 30px; text-transform: uppercase" maxlength="2">
							 								<label>Zip: </label><input type="text" id="NorcrossbillToZipID" name="NorcrossbillToZipName" style="width: 75px;">
							 						</td>
							 					</tr>
												</table>
											</td>
											<td>
											    <table>
												<tr>
							 						<td><input type="text" id="BirminghambillToAddressID" name="BirminghambillToAddressName" class="validate[maxSize[100]" style="width: 300px;"></td>
							 					</tr>
								 				<tr>
								 					<td><input type="text" id="BirminghambillToAddressID1" name="BirminghambillToAddress1Name" class="validate[maxSize[100]" style="width: 300px;"></td>
								 				</tr>
								 				<tr>
													<td><input type="text" id="BirminghambillToAddressID2" name="BirminghambillToAddress2Name" class="validate[maxSize[40]" style="width: 300px;"></td>
												</tr>
							 					<tr>
							 						<td><input type="text" id="BirminghambillToCity" name="BirminghambillToCityName" style="width: 100px;">
															<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
							 								<input type="text" id="BirminghambillToState" name="BirminghambillToStateName" style="width: 30px; text-transform: uppercase" maxlength="2">
							 								<label>Zip: </label><input type="text" id="BirminghambillToZipID" name="BirminghambillToZipName" style="width: 75px;">
							 						</td>
							 					</tr>
							 				   </table>
							 				   </td>
							 				  </tr>
							 			</table> -->
									 	</fieldset>
									</td>
								</tr>
							</table>
						</td>
					</tr>
							
					<tr>
						<td style="width:85px;">
							<table><tr><td>
								<fieldset class="ui-widget-content ui-corner-all" style="margin-left: -6px;width:325px">
										<legend class="custom_legend"><label><b>Defined Rolodex Categories</b></label></legend>
								<table>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category1Desc" id="Category1Desc" value="${requestScope.Category1Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory1Desc" id="Category1Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory1Desc" id="Category1Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category2Desc" id="Category2Desc" value="${requestScope.Category2Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory2Desc" id="Category2Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory2Desc" id="Category2Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category3Desc" id="Category3Desc" value="${requestScope.Category3Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory3Desc" id="Category3Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory3Desc" id="Category3Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category4Desc" id="Category4Desc" value="${requestScope.Category4Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory4Desc" id="Category4Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory4Desc" id="Category4Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category5Desc" id="Category5Desc" value="${requestScope.Category5Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory5Desc" id="Category5Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory5Desc" id="Category5Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category6Desc" id="Category6Desc" value="${requestScope.Category6Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory6Desc" id="Category6Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory6Desc" id="Category6Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category7Desc" id="Category7Desc" value="${requestScope.Category7Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory7Desc" id="Category7Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory7Desc" id="Category7Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									<tr>
										<td><input type="text" style ="width:187px" maxlength="12" name="Category8Desc" id="Category8Desc" value="${requestScope.Category8Desc}"></td>
										<td>
											<input type="checkbox" name="chkCategory8Desc" id="Category8Descyesid" value = "Yes" /> <span style="vertical-align: 5px;">Yes</span>&nbsp;&nbsp;&nbsp;
											<input type="checkbox" name="chkCategory8Desc" id="Category8Descnoid" value = "No" /> <span style="vertical-align: 5px;">No</span>
										</td>
									</tr>
									
									<!-- <tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="1"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="2"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="3"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="4"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="5"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="6"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="7"><input type="text" style="width: 5%;height:120%; " maxlength="1" tabindex="11"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr>
									<tr>
									<td><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"><input type="text" style="width: 5%;height:120%; " maxlength="1"></td>
									</tr> -->
								</table>
								
								</fieldset>
								
								</td>
								
								
							<td style="vertical-align: top">
							
							</td>
							</tr>
							<tr>
							<td>
							
							</td>
							</tr>
						</table>
					</td>
					<td>
						<table><tr><td>
								<fieldset class="ui-widget-content ui-corner-all" style="margin-left: -6px;width:204px">
										<legend class="custom_legend"><label><b>Defined Group Defaults</b></label></legend>
								<table>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_1" id="GroupDefaults_1" value="${requestScope.groupDefaults1.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_1id" id="GroupDefaults_1id" value="1">											
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(1)" id="permission1" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_2" id="GroupDefaults_2" value="${requestScope.groupDefaults2.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_2id" id="GroupDefaults_2id" value="2">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(2);" id="permission2" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_3" id="GroupDefaults_3" value="${requestScope.groupDefaults3.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_3id" id="GroupDefaults_3id" value="3">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(3);" id="permission3" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_4" id="GroupDefaults_4" value="${requestScope.groupDefaults4.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_4id" id="GroupDefaults_4id" value="4">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(4);" id="permission4" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_5" id="GroupDefaults_5" value="${requestScope.groupDefaults5.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_5id" id="GroupDefaults_5id" value="5">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(5);" id="permission5" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_6" id="GroupDefaults_6" value="${requestScope.groupDefaults6.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_6id" id="GroupDefaults_6id" value="6">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(6);" id="permission6" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_7" id="GroupDefaults_7" value="${requestScope.groupDefaults7.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_7id" id="GroupDefaults_7id" value="7">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(7);" id="permission7" value="Permissions">
										</td>
									</tr>
									<tr>
										<td>
											<input type="text" style ="width:187px" maxlength="25" name="GroupDefaults_8" id="GroupDefaults_8" value="${requestScope.groupDefaults8.groupName}">
											<input type="hidden" style ="width:187px" maxlength="25" name="GroupDefaults_8id" id="GroupDefaults_8id" value="8">
										</td>
										<td>
											<input type="button" class="savehoverbutton turbo-blue" style ="width:187px" onclick="callPermissionDialog(8);" id="permission8" value="Permissions">
										</td>
									</tr>
									
								</table>
								
								</fieldset>
								
								</td>
								
								
							<td style="vertical-align: top">
							
							</td>
							</tr>
							<tr>
							<td>
							
							</td>
							</tr>
						</table>
					</td>
						
					</tr> 
											
							</table>
							<br>
							<hr style="width: 935px;">
							<table align="center" style="width:600px;">
							<tr>
									
									<td colspan="2" align="right" style="padding-left:648px;">
									<div id="errorDIV" style="width:200px" ></div>
									<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveCompanySetting()" style=" width:80px;float:right;" />
									</td>
								</tr>
								<tr>
									
									<td colspan="2" align="right" style="padding-left:735px;">
											
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
				</tr>
	</table>
	</form>
</div>
<div id="settingsCustmerDetailsBlock">
<table>
<tr><td colspan="2" align="right">
<div align="right"><img alt="Export Csv" src="./../resources/images/csv_text.png" width="25" onclick="exportCustomerContacts()" ></div></td>
</tr>
<tr><td  style="vertical-align: top;">
<fieldset class=" ui-widget-content ui-corner-all" style="margin-left: -16px;" >

													<legend>
														<label><b>Customer Type</b></label>
													</legend>
<table style="width: 709px; margin: 0 auto;">
			<tr>
				<td style="padding-right: 0px;">
					<table id="chartsOfCuTypesGrid" style="width:263px;"></table>
					<div id="chartsOfTypesGridPager" ></div>
				</td>
				<td>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id=customerTypeFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;width:380px;height:101px;">
													<legend>
														<label><b></b></label>
													</legend>
													<table style="margin-top: 30px;">
														<tr>
															<td>Description :</td>
															<td colspan="2"><input type="text" id="typeDescriptionId"
																name="typeDescription" /></td>
														</tr>
														<tr>
															<td>Code :</td>
															<td><input type="text" id="typeCodeId" name="typeCode" maxlength="6"/></td>
															<td align="right">Inactive :
																<input type="checkbox" id="typeActiveId" name="typeActive" />
															</td>
														</tr>
													</table>
												</fieldset>
											</td>
										</tr>
										<tr><td><div style="display: none;" id="CusTypeMsg">&nbsp;</div></td></tr>
										<tr>
											<td>
											 <input
												type="reset" id="addButton" name="addButtonName"
												value="Clear" class="savehoverbutton">
												 <input
												type="button" id="deleteButton" name="deleteButtonName"
												value="Delete" class="savehoverbutton turbo-blue"
												onclick="deleteCuType()">
												
											<span style="float: right;">
											<input
												type="hidden" name="cuMasterTypeId" id="cuMasterTypeIdId" /> <input
												type="button" id="saveButton" name="saveButtonName"
												value="Save" class="savehoverbutton turbo-blue"
												onclick="saveCuTypeData()">
												</span>
												</td>
										</tr>
									</table>
								</form>
							</div>
				</td>
			</tr>
		</table>
		
		
</fieldset>
</td>

<td>

											<fieldset class="ui-widget-content ui-corner-all" style="margin-left: 19px;width:225px;height:185px;">
												<legend class="custom_legend"><label><b>Employees Assigned</b></label></legend>
										<form name="customercategoryform" id="customercategoryform">
										<table style="width:242px;">
											<tr>
											<td>
											<input type="hidden" name="customerCategory1Id" class="customerCategory1Id" id="customerCategory1Id" value="${requestScope.CustomerCategoryId1}"/>
											<input type="text" maxlength="12" name="customerCategory1Desc" class="customerCategory1Desc"  id="customerCategory1Desc" value="${requestScope.CustomerCategory1}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="customerCategory2Id" class="customerCategory2Id" id="customerCategory2Id" value="${requestScope.CustomerCategoryId2}"/>
											<input type="text" maxlength="12" name="customerCategory2Desc" class="customerCategory2Desc" id="customerCategory2Desc" value="${requestScope.CustomerCategory2}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="customerCategory3Id" class="customerCategory3Id"  id="customerCategory3Id" value="${requestScope.CustomerCategoryId3}"/>
											<input type="text" maxlength="12" name="customerCategory3Desc" class="customerCategory3Desc" id="customerCategory3Desc" value="${requestScope.CustomerCategory3}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="customerCategory4Id" class="customerCategory4Id" id="customerCategory4Id" value="${requestScope.CustomerCategoryId4}"/>
											<input type="text" maxlength="12" name="customerCategory4Desc" class="customerCategory4Desc"  id="customerCategory4Desc" value="${requestScope.CustomerCategory4}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="customerCategory5Id" class="customerCategory5Id"  id="customerCategory5Id" value="${requestScope.CustomerCategoryId5}"/>
											<input type="text" maxlength="12" name="customerCategory5Desc" class="customerCategory5Desc" id="customerCategory5Desc" value="${requestScope.CustomerCategory5}"></td>
											</tr>
											
											<tr align="left">
											<td>
											<input	type="button" id="savecucategory" name="savecucategory"	value="Save" class="savehoverbutton turbo-blue"	onclick="customercategorysave()">
											</td>
											</tr>
											<tr>
											<td><div id="CustomerErrorDiv"></div></td>
											</tr>
											
										</table>
										</form>
										</fieldset>
										</td>
										</tr>
										<tr><td colspan="2">
<fieldset class=" ui-widget-content ui-corner-all" style="margin-left:-16px; ">
													<legend>
														<label><b>Customer Payment Terms</b></label>
													</legend>
<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td style="padding-right: 20px;">
					<table id="chartsOfTermsGrid"></table>
					<div id="chartsOfTermsGridPager"></div>
				</td>
				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 676px; margin: 0 auto; background-color: #FAFAFA; height: 450px;">
							<ul>
								<li id=""><a href="#chartsDetailsDiv">Payment Terms</a></li>
								<li style="float: right; padding-right: 10px; padding-top: 3px;">
									<label id="chartsName"
									style="color: white; vertical-align: middle;"></label>
								</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id=paymentTermsFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Payment Terms</b></label>
													</legend>
													<table>
														<tr>
															<td>Description :</td>
															<td colspan="2"><input type="text" id="description"
																name="description" /></td>
														</tr>
														<tr>
															<td>Set as a Global :</td>
															<td><input type="checkbox" id="global" name="global" /></td>
															<td>Inactive :</td>
															<td><input type="checkbox" id="active" name="active" />
															</td>
														</tr>
													</table>
												</fieldset>
											</td>
											<td><fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Terms Condition</b></label>
													</legend>
													<table>
														<tr>
															<td>Payment Due : Net</td>
															<td><input type="text" name="dueDay" id="dueDay"
																style="width: 15px" /> <select name="dueDayoption"
																id="dueDayoption">
																	<option value="0" selected="yes">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
														<tr>
															<td>Discount :</td>
															<td><input type="text" name="discountPercent"
																id="discountPercent" style="width: 30px" /> <input
																type="text" name="discountDay" id="discountDay"
																style="width: 15px" /> <select name="discountDayoption"
																id="discountDayoption">
																	<option value="0" selected="yes">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Order Note</b></label>
													</legend>
													<table>
														<tr>
															<td><textarea id="orderNote" name="orderNote"
																	style="width: 627px"></textarea></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Pick Ticket Note</b></label>
													</legend>
													<table>
														<tr>
															<td><input type="text" id="pickTicket1"
																name="pickTicket1" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket2"
																name="pickTicket2" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket3"
																name="pickTicket3" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket4"
																name="pickTicket4" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket5"
																name="pickTicket5" style="width: 627px" /></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr><td>&nbsp;<span style="float: right; display: none;" id="paymentTermsMsg"></span></td></tr>
										<tr>
											<td align="right" style="padding-right: 25px;">
											 <input
												type="button" id="addButton" name="addButtonName"
												value="Add" class="add"
												onclick="openAddNewTermsDialog()">
											<input
												type="hidden" name="cuTermsId" id="cuTermsId" /> <input
												type="button" id="saveButton" name="saveButtonName"
												value="Save" class="savehoverbutton turbo-blue"
												onclick="editData()">
												 <input
												type="button" id="deleteButton" name="deleteButtonName"
												value="Delete" class="savehoverbutton turbo-blue"
												onclick="deletePaymentTerms()">
												
												</td>
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
				</td>
			</tr>
			<tr>
			<td>
			
			</td>
			</tr>
		</table>

<div id="addNewTermsDialog">
			<form action="" id="addNewTermsFormID">
					<table style="padding-top: 8px;">
										<tr>
											<td>
												<fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Payment Terms</b></label>
													</legend>
													<table>
														<tr>
															<td>Description :</td>
															<td colspan="2"><input type="text" id="description"
																name="description" /></td>
														</tr>
														<tr>
															<td>Set as a Global :</td>
															<td><input type="checkbox" id="global" name="global"  /></td>
															<td>Inactive :</td>
															<td><input type="checkbox" id="active" name="active" />
															</td>
														</tr>
													</table>
												</fieldset>
											</td>
											<td><fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Terms Condition</b></label>
													</legend>
													<table>
														<tr>
															<td>Payment Due : Net</td>
															<td><input type="text" name="dueDay" id="dueDay"
																style="width: 15px" /> <select name="dueDayoption"
																id="dueDayoption">
																	<option value="0" selected="selected">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
														<tr>
															<td>Discount :</td>
															<td><input type="text" name="discountPercent"
																id="discountPercent" style="width: 30px" /> <input
																type="text" name="discountDay" id="discountDay"
																style="width: 15px" /> <select name="discountDayoption"
																id="discountDayoption">
																	<option value="0" selected="selected">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Order Note</b></label>
													</legend>
													<table>
														<tr>
															<td><textarea id="orderNote" name="orderNote"
																	style="width: 627px"></textarea></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Pick Ticket Note</b></label>
													</legend>
													<table>
														<tr>
															<td><input type="text" id="pickTicket1"
																name="pickTicket1" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket2"
																name="pickTicket2" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket3"
																name="pickTicket3" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket4"
																name="pickTicket4" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket5"
																name="pickTicket5" style="width: 627px" /></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
									</table>
					<br>
					<hr>
					<table align="right">
						<tr>
							<td align="right" style="padding-right:25px;">
							<input type="hidden" id="cuTermsId" name="cuTermsId" value="0"/>
							<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="addData()" style=" width:120px;">
							<input type="button" id="cancelTermsButton" name="cancelTermsButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddTerms()">
							
							</td>
						</tr>
					</table>
			</form>
		</div>
		</fieldset>
		</td>
		</tr>
		
		<!-- 
		Created by              : Leo.N     Date  :  22/02/2016
		Description             : ID# 508.
			-->
			
		<tr>
		<td colspan="2">
		<%-- <fieldset class= " ui-widget-content ui-corner-all" style="margin-left:-16px;" >
				<legend class="custom_legend"><label><b>Sales Order Footer Note</b></label></legend>
				<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
				<tr>
				<td style="width: 160px;text-align: right;"><label>Footnote Terms:</label></td>
				<td style ="width:400px;"><textarea rows="50" cols="70" id="footerTextId" style="height: 90px;">${requestScope.soFooterText}</textarea></td>
				<td><span style="display: inline"><input type="button" id="savefooter" name="savefooter"	value="Save" class="savehoverbutton turbo-blue"	onclick="saveSOfooterNote()"></span> &nbsp;&nbsp;<span id="SofooterTextSuccess"></span></td>
				</tr>
				</table>
				
		</fieldset> --%>
		
		<!-- Created by:Naveed   Date  :  06/09/2014 -->
			<fieldset class="ui-widget-content ui-corner-all" style="margin-left: 4px;width:1030px;height:280px;">
				<legend><label><b>Footer Note</b></label></legend>
				<table >
					<tr align="center">
						<td style="width: 160px;text-align: right;"><label>Footnote Terms:</label></td>
						<td style="height : 150px">
							<textarea  id="footerTextId" value="teest"  style="visibility: hidden;">${requestScope.soFooterText}</textarea> <!-- class="TinyMCETextEditor" -->
						</td>	
						<td>
							<input	type="button" id="savefooter" name="savefooter"	value="Save" class="savehoverbutton turbo-blue"	onclick="saveSOfooterNote()">
						</td>
					</tr>
					
					<tr>
					<td colspan="3" style="text-align: right;padding-right:40px;">
					<span id="SofooterTextSuccess"></span>
					</td>
					</tr>
				</table>							
												
			</fieldset>
		</td>
		</tr>
		
		<tr>
		<td colspan="2">
			<!-- 

Created by              : Leo.N     Date  :  10/09/2014
Description             : Vendor Settings fieldset UI.


 -->
				
				
					<fieldset class= " ui-widget-content ui-corner-all" style="margin-left:-16px;" >
											      <legend class="custom_legend"><label><b>Settings</b></label></legend>
						<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
						<td width="70%">					
						<div Style="width:75%;display:block;float:left;line-height:20px;"><span>Statements shall be grouped by Job?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusStGpbyJobYes==1 }">
								<span><input type="radio" id="chk_cusStGpbyJobYes" name="chk_cusStGpbyJobStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusStGpbyJobYes" name="chk_cusStGpbyJobStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusStGpbyJobYes==0 || empty requestScope.chk_cusStGpbyJobYes }">
								<span><input type="radio" id="chk_cusStGpbyJobYes" name="chk_cusStGpbyJobStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusStGpbyJobYes" name="chk_cusStGpbyJobStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>						
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Statements shall show Billing Remainder?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusStShowBillRemYes==1 }">
								<span><input type="radio" id="chk_cusStShowBillRemYes" name="chk_cusStShowBillRemStatus" style="vertical-align: top;" checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusStShowBillRemYes" name="chk_cusStShowBillRemStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusStShowBillRemYes==0 || empty requestScope.chk_cusStShowBillRemYes}">
								<span><input type="radio" id="chk_cusStShowBillRemYes" name="chk_cusStShowBillRemStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusStShowBillRemYes" name="chk_cusStShowBillRemStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Check credit limit in Sales Order(Outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusCreLiminSalOrdYes==1 }">
								<span><input type="radio" id="chk_cusCreLiminSalOrdYes" name="chk_cusCreLiminSalOrdStatus" style="vertical-align: top;" checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusCreLiminSalOrdYes" name="chk_cusCreLiminSalOrdStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusCreLiminSalOrdYes==0 || empty requestScope.chk_cusCreLiminSalOrdYes}">
								<span><input type="radio" id="chk_cusCreLiminSalOrdYes" name="chk_cusCreLiminSalOrdStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusCreLiminSalOrdYes" name="chk_cusCreLiminSalOrdStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							
						</div>		
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Check credit limit in Quick Book?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusCreLiminQuickBookYes==1 }">
								<span><input type="radio" id="chk_cusCreLiminQuickBookYes" name="chk_cusCreLiminQuickBookStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusCreLiminQuickBookYes" name="chk_cusCreLiminQuickBookStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusCreLiminQuickBookYes==0 || empty requestScope.chk_cusCreLiminQuickBookYes}">
								<span><input type="radio" id="chk_cusCreLiminQuickBookYes" name="chk_cusCreLiminQuickBookStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusCreLiminQuickBookYes" name="chk_cusCreLiminQuickBookStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>
						</td>
						<td width="27%"></td>
						</tr>	
						<!--  Added by aravind Date 03-06-16 ID#534 -->
						<%-- <tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Do Not Allow Tax Territory to be changed on Customer Invoice after saving?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_taxTerCuInvAftSaveYes==1 }">
								<span><input type="radio" id="chk_taxTerCuInvAftSaveYes" name="chk_taxTerCuInvAftSaveStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_taxTerCuInvAftSaveYes" name="chk_taxTerCuInvAftSaveStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_taxTerCuInvAftSaveYes==0 || empty requestScope.chk_taxTerCuInvAftSaveYes}">
								<span><input type="radio" id="chk_taxTerCuInvAftSaveYes" name="chk_taxTerCuInvAftSaveStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_taxTerCuInvAftSaveYes" name="chk_taxTerCuInvAftSaveStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>
						</td>
						<td width="27%"></td>
						</tr> --%>
						
							
						<!-- Added By leo Date:24-02-16 ID#:509 -->
						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Remove EXT LIST column from Sales Order PDF?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusRemExtListfmSalOrdpdfYes==1 }">
								<span><input type="radio" id="chk_cusRemExtListfmSalOrdpdfYes" name="chk_cusRemExtListfmSalOrdpdfStatus" style="vertical-align: top;" checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusRemExtListfmSalOrdpdfYes" name="chk_cusRemExtListfmSalOrdpdfStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusRemExtListfmSalOrdpdfYes==0 || empty requestScope.chk_cusRemExtListfmSalOrdpdfYes}">
								<span><input type="radio" id="chk_cusRemExtListfmSalOrdpdfYes" name="chk_cusRemExtListfmSalOrdpdfStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusRemExtListfmSalOrdpdfYes" name="chk_cusRemExtListfmSalOrdpdfStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Remove MULT column from Sales Order PDF?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusRemMultfmSalOrdpdfYes==1 }">
								<span><input type="radio" id="chk_cusRemMultfmSalOrdpdfYes" name="chk_cusRemMultfmSalOrdpdfStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusRemMultfmSalOrdpdfYes" name="chk_cusRemMultfmSalOrdpdfStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusRemMultfmSalOrdpdfYes==0 || empty requestScope.chk_cusRemMultfmSalOrdpdfYes}">
								<span><input type="radio" id="chk_cusRemMultfmSalOrdpdfYes" name="chk_cusRemMultfmSalOrdpdfStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusRemMultfmSalOrdpdfYes" name="chk_cusRemMultfmSalOrdpdfStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>	
										
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Require Division in Sales Order(Outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusReqDivinSalOrdYes==1 }">
								<span><input type="radio" id="chk_cusReqDivinSalOrdYes" name="chk_cusReqDivinSalOrdStatus" style="vertical-align: top;" checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqDivinSalOrdYes" name="chk_cusReqDivinSalOrdStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusReqDivinSalOrdYes==0 || empty requestScope.chk_cusReqDivinSalOrdYes}">
								<span><input type="radio" id="chk_cusReqDivinSalOrdYes" name="chk_cusReqDivinSalOrdStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqDivinSalOrdYes" name="chk_cusReqDivinSalOrdStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Require Division in Customer Invoice(Outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusReqDivinCusInvYes==1 }">
								<span><input type="radio" id="chk_cusReqDivinCusInvYes" name="chk_cusReqDivinCusInvStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqDivinCusInvYes" name="chk_cusReqDivinCusInvStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusReqDivinCusInvYes==0 || empty requestScope.chk_cusReqDivinCusInvYes}">
								<span><input type="radio" id="chk_cusReqDivinCusInvYes" name="chk_cusReqDivinCusInvStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqDivinCusInvYes" name="chk_cusReqDivinCusInvStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Include Sales Tax when calculating discounts?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusIncSalTaxYes==1 }">
								<span><input type="radio" id="chk_cusIncSalTaxYes" name="chk_cusIncSalTaxStatus" style="vertical-align: top;" checked="checked" value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncSalTaxYes" name="chk_cusIncSalTaxStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusIncSalTaxYes==0 || empty requestScope.chk_cusIncSalTaxYes}">
								<span><input type="radio" id="chk_cusIncSalTaxYes" name="chk_cusIncSalTaxStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncSalTaxYes" name="chk_cusIncSalTaxStatus" checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Include Freight when calculating discounts?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusIncFreightYes==1 }">
								<span><input type="radio" id="chk_cusIncFreightYes" name="chk_cusIncFreightStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncFreightYes" name="chk_cusIncFreightStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusIncFreightYes==0 || empty requestScope.chk_cusIncFreightYes}">
								<span><input type="radio" id="chk_cusIncFreightYes" name="chk_cusIncFreightStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncFreightYes" name="chk_cusIncFreightStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>	
						
						<tr style="display: none;">
						<td width="70%" >
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Include 'List' column on invoices?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusIncListYes==1 }">
								<span><input type="radio" id="chk_cusIncListYes" name="chk_cusIncListStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncListYes" name="chk_cusIncListStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusIncListYes==0 || empty requestScope.chk_cusIncListYes}">
								<span><input type="radio" id="chk_cusIncListYes" name="chk_cusIncListStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncListYes" name="chk_cusIncListStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>	
						<tr style="display: none;">
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Include 'Ext List' column on invoices?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusIncExtListYes==1 }">
								<span><input type="radio" id="chk_cusIncExtListYes" name="chk_cusIncExtListStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncExtListYes" name="chk_cusIncExtListStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusIncExtListYes==0 || empty requestScope.chk_cusIncExtListYes}">
								<span><input type="radio" id="chk_cusIncExtListYes" name="chk_cusIncExtListStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncExtListYes" name="chk_cusIncExtListStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>		
						<tr style="display: none;">
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Include 'Mult' column on invoices?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusIncMultYes==1 }">
								<span><input type="radio" id="chk_cusIncMultYes" name="chk_cusIncMultStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncMultYes" name="chk_cusIncMultStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusIncMultYes==0 || empty requestScope.chk_cusIncMultYes}">
								<span><input type="radio" id="chk_cusIncMultYes" name="chk_cusIncMultStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusIncMultYes" name="chk_cusIncMultStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>
											
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:24px;"><span>Use Division address in Pick Tickets?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusUseDivAddYes==1 }">
								<span><input type="radio" id="chk_cusUseDivAddYes" name="chk_cusUseDivAddStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusUseDivAddYes" name="chk_cusUseDivAddStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusUseDivAddYes==0 || empty requestScope.chk_cusUseDivAddYes}">
								<span><input type="radio" id="chk_cusUseDivAddYes" name="chk_cusUseDivAddStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusUseDivAddYes" name="chk_cusUseDivAddStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%"></td>
						</tr>						
						<!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #219
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Allow blank line items in Sales Order(Inside/Outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusAllblCusInvYes==1 }">
								<span><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusallowblinSalOrdStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusallowblinSalOrdStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusAllblCusInvYes==0 || empty requestScope.chk_cusAllblCusInvYes}">
								<span><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusallowblinSalOrdStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusallowblinSalOrdStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							
						</div>
						</td>
						</tr> -->
						
						<!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #220					
						<tr>
						<td width="75%">
						<div Style="width:50%;display:block;float:left;line-height:25px;"><span>Allow blank line items in Customer Invoice?</span></div>
						<div Style="width:50%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusAllblCusInvYes==1 }">
								<span><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusAllblCusInvStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusAllblCusInvStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusAllblCusInvYes==0 || empty requestScope.chk_cusAllblCusInvYes}">
								<span><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusAllblCusInvStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblCusInvYes" name="chk_cusAllblCusInvStatus" checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>
						</td>
						</tr> -->
						<!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #221						
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Allow blank Product Item Code in Sales Order(Inside/Outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusAllblProdinSalOrdYes==1 }">
								<span><input type="radio" id="chk_cusAllblProdinSalOrdYes" name="chk_cusAllblProdinSalOrdStatus" style="vertical-align: top;"  checked="checked"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblProdinSalOrdYes" name="chk_cusAllblProdinSalOrdStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusAllblProdinSalOrdYes==0 || empty requestScope.chk_cusAllblProdinSalOrdYes}">
								<span><input type="radio" id="chk_cusAllblProdinSalOrdYes" name="chk_cusAllblProdinSalOrdStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusAllblProdinSalOrdYes" name="chk_cusAllblProdinSalOrdStatus"  checked="checked" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>
						</td>
						</tr>	
						-->					
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Require Promise Date in Sales Order(outside of Job)?</span></div>
						<div Style="width:25%;display:block;float:right;">
							<c:if test="${requestScope.chk_cusReqProinSalOrderYes==1 }">
								<span><input type="radio" id="chk_cusReqProinSalOrderYes" name="chk_cusReqProinSalOrderStatus" style="vertical-align: top;"  checked="checked"   value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqProinSalOrderYes" name="chk_cusReqProinSalOrderStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusReqProinSalOrderYes==0 || empty requestScope.chk_cusReqProinSalOrderYes}">
								<span><input type="radio" id="chk_cusReqProinSalOrderYes" name="chk_cusReqProinSalOrderStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqProinSalOrderYes" name="chk_cusReqProinSalOrderStatus"  checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
																				
						</td>
						<td width="27%"></td>
						</tr>
						<!-- This row commented for ID #540 -->
						<tr style="display: none;">
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Require Freight when calculating Tax on Customer Invoices?</span></div>
						<div Style="width:25%;display:block;float:right;">
						<input type="hidden" value="${requestScope.chk_cusReqfreinCuInvoicesYes}">
							<c:if test="${requestScope.chk_cusReqfreinCuInvoicesYes==1 }">
								<span><input type="radio" id="chk_cusReqfreinCuInvoicesYes" name="chk_cusReqfreinCuInvoicesStatus" style="vertical-align: top;"  checked="checked"   value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqfreinCuInvoicesYes" name="chk_cusReqfreinCuInvoicesStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusReqfreinCuInvoicesYes==0 || empty requestScope.chk_cusReqfreinCuInvoicesYes}">
								<span><input type="radio" id="chk_cusReqfreinCuInvoicesYes" name="chk_cusReqfreinCuInvoicesStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqfreinCuInvoicesYes" name="chk_cusReqfreinCuInvoicesStatus"  checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
																				
						</td>
						<td width="27%"></td>
						</tr>
						<tr>
						<td width="70%">
						<div Style="width:75%;display:block;float:left;line-height:25px;"><span>Use different Invoice Sequence Number for All Customer Invoices?</span></div>
						<div Style="width:25%;display:block;float:right;">
						<input type="hidden" value="${requestScope.chk_cusReqSeqNumCuInvoicesYes}">
							<c:if test="${requestScope.chk_cusReqSeqNumCuInvoicesYes==1 }">
								<span><input type="radio" id="chk_cusReqSeqNumCuInvoicesYes" name="chk_cusReqSeqNumCuInvoicesYes" style="vertical-align: top;"  checked="checked"   value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqSeqNumCuInvoicesYes" name="chk_cusReqSeqNumCuInvoicesYes" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
							<c:if test="${requestScope.chk_cusReqSeqNumCuInvoicesYes==0 || empty requestScope.chk_cusReqSeqNumCuInvoicesYes}">
								<span><input type="radio" id="chk_cusReqSeqNumCuInvoicesYes" name="chk_cusReqSeqNumCuInvoicesYes" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<span style="margin-left:75px;"><input type="radio" id="chk_cusReqSeqNumCuInvoicesYes" name="chk_cusReqSeqNumCuInvoicesYes"  checked="checked"  value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
							</c:if>
						</div>	
						</td>
						<td width="27%">
						
						<div Style="width:100%;display:block;float:left; margin-left: 25px;">
							<input type="text" name ="inp_cusNewSeqNumCuInvoices" id="inp_cusNewSeqNumCuInvoices" style="width: 30%;" maxlength="9" class="validate[custom[number]]"  value="${requestScope.inp_cusNewSeqNumCuInvoices}"/>
							
						</div>
						</td>
						</tr>
						<tr>
						<td width="100%" colspan="2">
							<div Style="width:100%;display:block;float:left;">
								<span id="newCustInvoiceNumberMsg" style="color: red;float: right; margin-right:55px"></span>
							</div>
						</td>
						</table>
						
						<div Style="width:100%;clear:both;margin-right:2px;margin-top:40px;">
							<span id="but_vendorsettings" style="color: green;float: right;margin-top: -22px"></span>
							<input type="button" value="Save" onclick="saveCompanySettingsSysVariable();" class="savehoverbutton turbo-blue" style="float: right;width: 80px;" />
						</div>
						
						</fieldset>	
		</td>
		
		</tr>
		
		</table>
	</div>
<div id="settingsVendorDetailsBlock" height="980px" style="paddin-top:0px;margin-top:-37px;">&nbsp;
<table>

<tr>
<td>
<div align="right"><img alt="Export Csv" src="./../resources/images/csv_text.png" width="25" onclick="exportVendorContacts()" ></div>
				<fieldset class= " ui-widget-content ui-corner-all" >
<!--  Modified by leo on 10/9/2014  -->				
	<legend class="custom_legend"><label><b>Ship Via</b></label></legend>
	
	<table style="width:979px;margin:25px auto; margin-top:0px;margin-bottom:0px;">

				<tr>
		
					<td style="padding-right: 20px;">
						<table id="shipViaListGrid"> </table>
					</td>
					<td style = "vertical-align: top;">
						<div id="chartsDetailsTab" >
							<div class="charts_tabs_main" style="padding: 0px;width:495px;margin:0 auto; background-color: #FAFAFA;height: 208px;">
								<ul>
									<li id=""><a  href="#chartsDetailsDiv">Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
									<label id="chartsName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
								<div id="chartsDetailsDiv" style="padding: 0px;">
									<form id="chartsDetailsFromID">
										
								   	</form>
									<table>
										<tr>
										   <td style="position: absolute;top: 40px;">
										      <form id =shipViaFormID>
											      <fieldset class= " ui-widget-content ui-corner-all" >
														    <table>
																<tr>
															    	<td><label>Description: </label></td>
																	<td><input type="text" name="Description_ID" id="DescriptionID" style="width:333px;height: 24px;"  class="validate[required] validate[maxSize[12]" value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>Tracking URL: </label></td>
																	 <td><textarea name="trackingUrl" id="trackingUrlID" rows="2" cols="40"></textarea></td>
																</tr>
																<!--<tr style="height: 15px"><td></td></tr>
															 	<tr>
																   	 <td><label>Tracking Prefix:</label></td>
																	 <td><textarea name="trackingPrefix" id="trackingPrefixID" rows="2" cols="40"></textarea></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
															 		 <td><label>Tracking Suffix:</label></td>
																	 <td><textarea name="trackingSuffix" id="trackingSuffixID" rows="2" cols="40"></textarea></td>
																	 <td style="display:none"><input type="text" id="shipViaID" name="shipVia_ID"></td>
																	 <td style="display:none"><input type="text" id="inactive" name="Inactive"></td>
																</tr> -->
														 </table>
														</fieldset>
														</form>
												<table >
												
											 		<tr>
											 			<td align="left">
													    	<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveshipviaDetails()">
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button" id="saveshipviadetails" name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveshipviaDetails()">
													    	<input type="button" id="deleteshipviadetails" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteshipviaDetails()">
										     		    </td>
													</tr>
												<tr>
													<td colspan="2"><label id="messsagetouser"></label></td>
												</tr>
												</table>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</td>
					<!-- </legend> -->
					</tr>
			</table>
					</fieldset>
				
           </td>			
			</tr>
			
			<tr>
			<td>
			<!-- Eidted By:Naveed  ID #424-->
			<fieldset class= " ui-widget-content ui-corner-all" >
					<legend class="custom_legend"><label><b>Product/Services</b></label></legend>
					<table>
							<tr>
								<td><label>Code <span><font color="red">*</font></span></label></td>
								<td><input type="text" name="prodCodeName" id="prodCodeID" style="width:333px;height: 20px;" placeholder="Minimum 1 characters required to get Product List" />
								<input type="hidden" name="prodIDName" id="prodID" style="width:333px;height: 20px;"  value="${requestScope.userLoginSettings.vendorProductID}" />
								<input type="hidden" name="prodDeptName" id="prodDeptIDs" style="width:333px;height: 20px;"  value="${requestScope.userLoginSettings.vendorProductDeptID}" />
								</td>
								<td width="515">
									<span id="prodCodeNameMsg" style="color: red;float: left;"></span>
								</td>
							</tr>
							<tr>
								<td><label>Description </label></td>
								<td><input type="text" name="prodDescName" id="prodDescID" style="width:333px;height: 20px;" /></td>
								<td></td>
							</tr>
							<tr>
								<td><label>Department <span><font color="red">*</font></span></label></td>
								<td><select style="width:120px" id="prodDepartmentId" name="prodDepartmentName">
										<option value="-1"> - Select - </option>
										<c:forEach var="departmentBean" items="${requestScope.productDepartment}">
											<option value="${departmentBean.prDepartmentId}">
											<c:out value="${departmentBean.description}" ></c:out>
											</option>
										</c:forEach>
									</select>			
								</td>
								<td width="515px"><span id="prodDeptMsg" style="color: red;float: left;"></span></td>
							</tr>
							<tr>
								<td colspan='3'>
									<table align="center" style="width:100%;">
										<tr>
											<td align="right" style="padding-left:925px;">
												<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveProductsSetting()" style=" width:80px;">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan='3' align="right">
									<span id="vendorServiceID" style="color:green"></span>
								</td>
							</tr>
					</table>
			</fieldset>
			
			<fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Freight Charges</b></label></legend>
			<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
				<tr>
					<td style="padding-right: 20px; width:25%">
						<table id="freightChargesGrid"></table>
					</td>
					<td style="float:left;">
						<div id="freightDetailsTab" >
							<div class="charts_tabs_main" style="padding: 0px;width:520px;margin:0 auto; background-color: #FAFAFA;height: 300px;">
								<ul>
									<li id=""><a  href="#chartsDetailsDiv">Freight Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
									<label id="chartsName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
								<div id="chartsDetailsDiv" style="padding: 0px;">
									
									<table>
										<tr>
										   <td style="position: absolute;top: 60px;">
										      <form id =freightdetailsid>
											      <fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Freight Charges</b></label></legend>
														    <table>
																<tr>
															    	<td><label>Description: </label></td>
																	<td><input type="text" name="Description_ID" id="freigthDescriptionID" style="width:272px;height: 24px;"  class="validate[required] validate[maxSize[12]" value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>InActive: </label></td>
																	 <td><input type="checkbox" name="inactivefreight" id="inactivefreight"  value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																   	 <td><label>Prompt for additional details when selected?:</label></td>
																	 <td><input type="checkbox" name="askfornote" id="askfornote"  value=""></td>
																</tr>
																<tr>
																<td><input type="hidden" id="freightid" /></td>
																
																</tr>
																
														 </table>
														</fieldset>
														</form>
												<table style="position:relative;">
												<tr>
												<td><label id="messsagetouser"></label></td>
												</tr>
											 		<tr>
											 			<td align="left">
													    	<input type="button" value="Add" id="addnewfreight" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="addFreightDetails()">
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button" id="saveshipviadetails" id="saveFrieght" name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveFreightDetails()">
													    	<input type="button" id="deleteshipviadetails" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteFreightDetail()">
										     		    </td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
			</fieldset>
			</td></tr>
			
					<tr>
				<td>
				
				
<!-- 

Created by              : Leo.N     Date  :  10/09/2014
Description             : Vendor Settings fieldset UI.


 -->
				
				
					<fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Settings</b></label></legend>
						<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
						<td>					
						<div Style="width:50%;display:block;float:left;line-height:20px;"><span>Default PO Description to item code?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_venpoDesStatusYN == 1}">
						<span><input type="checkbox" id="chk_venpoDesYes" name="chk_venpoDesStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venpoDesNo" name="chk_venpoDesStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_venpoDesStatusYN == 0||empty requestScope.chk_venpoDesStatusYN}">
						<span><input type="checkbox" id="chk_venpoDesYes" name="chk_venpoDesStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venpoDesNo" name="chk_venpoDesStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>						
						
						<div Style="width:50%;display:block;float:left;line-height:25px;"><span>Use company logo and label on Shipping Confirmations?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_vencmpanylogoStatusYN == 1}">
						<span><input type="checkbox" id="chk_vencmpanylogoYes" name="chk_vencmpanylogoStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vencmpanylogoNo" name="chk_vencmpanylogoStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_vencmpanylogoStatusYN == 0||empty requestScope.chk_vencmpanylogoStatusYN}">
						<span><input type="checkbox" id="chk_vencmpanylogoYes" name="chk_vencmpanylogoStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vencmpanylogoNo" name="chk_vencmpanylogoStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Show Vendor phone number on PO?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_vendorPhnoYesYN == 1}">
						<span><input type="checkbox" id="chk_vendorPhnoYes" name="chk_vendorPhnoStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendorPhnoNo" name="chk_vendorPhnoStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_vendorPhnoYesYN == 0||empty requestScope.chk_vendorPhnoYesYN}">
						<span><input type="checkbox" id="chk_vendorPhnoYes" name="chk_vendorPhnoStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendorPhnoNo" name="chk_vendorPhnoStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>		
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Show Vendor Product IDs on PO?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_venproductidStatusYN == 1}">
						<span><input type="checkbox" id="chk_venproductidYes" name="chk_venproductidStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;" >Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venproductidNo" name="chk_venproductidStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_venproductidStatusYN == 0||empty requestScope.chk_venproductidStatusYN}">
						<span><input type="checkbox" id="chk_venproductidYes" name="chk_venproductidStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venproductidNo" name="chk_venproductidStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Ask to close POs when all items invoiced?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_venclosePoStatusYN == 1}">
						<span><input type="checkbox" id="chk_venclosePoYes" name="chk_venclosePoStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;" >Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venclosePoNo" name="chk_venclosePoStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_venclosePoStatusYN == 0||empty requestScope.chk_venclosePoStatusYN}">
						<span><input type="checkbox" id="chk_venclosePoYes" name="chk_venclosePoStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venclosePoNo" name="chk_venclosePoStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>	
						
						<!-- <div Style="width:50%;display:block;float:left;line-height:24px;"><span>Don't allow Blank Lines in Purchase Orders?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendontbllinespurorderYes" name="chk_vendontbllinespurorderStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendontbllinespurorderNo" name="chk_vendontbllinespurorderStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Don't allow Blank Lines in Vendor Invoices?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendontbllinesinvoiceYes" name="chk_vendontbllinesinvoiceStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendontbllinesinvoiceNo" name="chk_vendontbllinesinvoiceStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Don't allow Blank Product Item code in Vendor Purchase Order?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendontprodItemYes" name="chk_vendontprodItemStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendontprodItemNo" name="chk_vendontprodItemStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	 -->
						
						<div Style="width:50%;display:block;float:left;line-height:24px;"><span>Require Invoice # for Invoices with PO?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_venreqInvStatusYN == 1}">
						<span><input type="checkbox" id="chk_venreqInvYes" name="chk_venreqInvStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;" >Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venreqInvNo" name="chk_venreqInvStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_venreqInvStatusYN == 0||empty requestScope.chk_venreqInvStatusYN}">
						<span><input type="checkbox" id="chk_venreqInvYes" name="chk_venreqInvStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_venreqInvNo" name="chk_venreqInvStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>	
						
						<div Style="width:50%;display:block;float:left;line-height:25px;"><span>Vendor invoice default on hold?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_vendefaultInvStatusYN == 1}">
						<span><input type="checkbox" id="chk_vendefaultInvYes" name="chk_vendefaultInvStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;" >Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendefaultInvNo" name="chk_vendefaultInvStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_vendefaultInvStatusYN == 0||empty requestScope.chk_vendefaultInvStatusYN}">
						<span><input type="checkbox" id="chk_vendefaultInvYes" name="chk_vendefaultInvStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendefaultInvNo" name="chk_vendefaultInvStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>	
						
						<div Style="width:50%;display:block;float:left;line-height:25px;"><span>Include tax on Purchase Orders and Invoices?</span></div>
						<div Style="width:50%;display:block;float:right;">
						<c:if test="${requestScope.chk_IncTaxPOInvStatusYN == 1}">
						<span><input type="checkbox" id="chk_IncTaxPOInvYes" name="chk_IncTaxPOInvStatus" style="vertical-align: top;"  value="yes" checked="checked"></span>&nbsp;<span style="color:#00377A;" >Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_IncTaxPOInvNo" name="chk_IncTaxPOInvStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						<c:if test="${requestScope.chk_IncTaxPOInvStatusYN == 0||empty requestScope.chk_IncTaxPOInvStatusYN}">
						<span><input type="checkbox" id="chk_IncTaxPOInvYes" name="chk_IncTaxPOInvStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_IncTaxPOInvNo" name="chk_IncTaxPOInvStatus" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</c:if>
						</div>	
																				
						</td>
						</tr>
						
						
						
						</table>
						
						<div Style="width:100%;clear:both;margin-right:2px;">
						<span id="but_vndrsettings" style="color: green;float: right;margin-top: -22px"></span>
						<input type="button" value="Save"  class="savehoverbutton turbo-blue" style="float: right;width: 80px;" onclick="saveVendorSettings()" />
						</div>
						
						</fieldset></td>
						</tr>
			

</table>
			
			

</div>
<div id="settingsEmployeeDetailsBlock">&nbsp;

<fieldset class= " ui-widget-content ui-corner-all" >
<legend class="custom_legend"><label><b>Commission Split types</b></label></legend>
<td>
<table>

				<tr>
					<td style="padding-right: 20px;">
						<table id="empSplitTypesGrid"></table>
					</td>
					<td>
						<div id="empSplitTypesTab" ></div>
							
								
								<div id="employeeDetailsDiv" style="padding: 0px;width:240px; top:50px;background-color: #FAFAFA;">
									</div>
									<table>
										<tr>
										   <td style="position: absolute;top: 10%;right: 36%;width: 260px;">
										      <form id =empSplitTypesFormID>
										      <input type="hidden" name="ecSplitCodeHiddenID" id="ecSplitCodeHiddenID">
											      <fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Commission Split Types</b></label></legend>
											      
														    <table>
																<tr>
															    	<td><label>Description: </label></td>
																	<td><input type="text" name="splittypedescription" id="splittypedescription" style="width:280px;height: 24px;"  class="validate[required] validate[maxSize[12]" value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>Default %: </label></td>
																	 <td><textarea name="spltypdefaultper" id="spltypdefaultper" rows="2" cols="50"></textarea></td>
																</tr>
																
																<tr style="height: 15px"><td></td></tr>
														 </table>
														</fieldset>
														<table style="position:relative;top:25px;">
												<tr>
												<td><label id="messsagetouser"></label></td>
												</tr>
											 		<tr>
											 			<td align="left">
													    	<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="addCommissionSplitTypes()">
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button" id="saveshipviadetails" name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="editCommissionSplitTypes()">
													    	<input type="button" id="deleteshipviadetails" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteCommissionSplitTypes()">
										     		    </td>
													</tr>
												</table>
														</form>
												
											</td>
										</tr>
									</table>
								
							
						
					</td>
				</tr>
			</table>
			<td>
			</fieldset>

<fieldset class= " ui-widget-content ui-corner-all" >
<legend class="custom_legend"><label><b>Assign reps</b></label></legend>
<td>
<table>
	<tr>
		<td style="padding-right: 20px;">
	<table id="assignrepsgrid"></table>
		</td>
		<td>
		<div id="assignrepstab" ></div>
							<div id="assignrepsdiv" style="padding: 0px;width:240px; top:50px;background-color: #FAFAFA;">
									</div>
									<table>
										<tr>
										   <td style="position: absolute;top: 30%;right: 34%;width: 260px;">
										      <form id =assignrepsform>
											      <fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Assign reps</b></label></legend>
											      
														    <table>
																<tr>
															    	<td><label>Rep: </label></td>
																	<td><select id="bankAccountsID" name="bankAccounts" style="width: 300px;">
																		<option value="0"> -Select- </option>
																					<c:forEach var="bankAccounts" items="${requestScope.bankAccounts}">
																					<option value="${bankAccounts.rxMasterId}">
																					<c:out value="${bankAccounts.firstName} ${bankAccounts.name}"></c:out>
																					</option>
																					</c:forEach>
																					</select></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>CSR: </label></td>
																	 <td><select id="bankAccountsID" name="bankAccounts" style="width: 300px;">
																		<option value="0"> -Select- </option>
																					<c:forEach var="bankAccounts" items="${requestScope.bankAccounts}">
																					<option value="${bankAccounts.rxMasterId}">
																					<c:out value="${bankAccounts.firstName} ${bankAccounts.name}"></c:out>
																					</option>
																					</c:forEach>
																					</select></td>
																</tr>
																
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     
																	 <td><input type="checkbox" name="trackingUrl" ></td>
																	 <td><label>Assign all CUSTOMERS with above Rep to above CSR? </label></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     
																	 <td><input type="checkbox" name="trackingUrl" ></td>
																	 <td><label>Assign all open JOBS with above Rep to above CSR? </label></td>
																</tr>
														 </table>
														</fieldset>
														<table style="position:relative;top:25px;">
												<tr>
												<td><label id="messsagetouser"></label></td>
												</tr>
											 		<tr>
											 			<td align="left">
<!-- 													    	<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick=""> -->
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button" id="saveshipviadetails" name="saveshipviadetails" value="Update" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="">
													    	<input type="button" id="deleteshipviadetails" value="Cancel" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="">
										     		    </td>
													</tr>
												</table>
											</form>
										</td>
									</tr>
							</table>
					</td>
				</tr>
			</table>
			<td>
			</fieldset>
					
<!-- Created by:Naveed   Date  :  03/11/2014 -->

	<fieldset class= " ui-widget-content ui-corner-all" >
						<legend class="custom_legend"><label><b>Commission Settings</b></label></legend>
						<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
							<td>					
								<label>Invoices Combined for Jobs</label>
							</td>
							<td>
								<input type="radio" id="chk_invoiceCombineYes" name="chk_invoiceCombineYes" style="vertical-align: top;" value="1">&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_invoiceCombineNo" name="chk_invoiceCombineYes"  style="vertical-align: top;"  value="0" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
								
							</td>
						 </tr>
						 <tr>
						 	<td><label>Adjustment Description 1</label></td>
							<td><input type="text" id="txt_adjdescription1" name="txt_adjdescription1" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						<tr>
						 	<td><label>Adjustment Description 2</label></td>
							<td><input type="text" id="txt_adjdescription2"  name="txt_adjdescription2" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						
						<tr>
						 	<td><label>Adjustment Description 3</label></td>
							<td><input type="text" id="txt_adjdescription3" name="txt_adjdescription3" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						 
						<tr>
						 	<td><label>Adjustment Description 4</label></td>
							<td><input type="text" id="txt_adjdescription4" name="txt_adjdescription4"  maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						
						<tr>
						 	<td><label>Commission Description 1</label></td>
							<td><input type="text" id="txt_commissiondesc1" name="txt_commissiondesc1" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>			
						
						<tr>
						 	<td><label>Commission Description 2</label></td>
							<td><input type="text" id="txt_commissiondesc2" name="txt_commissiondesc2" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						
						<tr>
						 	<td><label>Commission Description 3</label></td>
							<td><input type="text" id="txt_commissiondesc3" name="txt_commissiondesc3" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						
						<tr>
						 	<td><label>Commission Description 4</label></td>
							<td><input type="text" id="txt_commissiondesc4" name="txt_commissiondesc4" maxlength="25" style="text-align:center;width:185px;" /></td>	
						 </tr>
						<tr>
							<td>					
								<span><label>Allow Employees to view their own Commission Statements</label></span>
							</td>
							<td>
								<span><input type="radio" id="chk_viewCommissionYes" name="chk_viewCommissionYes" style="vertical-align: top;" value="1" ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_viewCommissionNo" name="chk_viewCommissionYes"  style="vertical-align: top;" value="0" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
								</div>
							</td>
						 </tr>
						 <tr>
							<td>					
								<span><label>*Only show allocated profit on split commissions</label></span>
							</td>
							<td>
								<input type="radio" id="chk_allocatedProfitYes" name="chk_allocatedProfitYes" style="vertical-align: top;" value="1" >&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_allocatedProfitNo" name="chk_allocatedProfitYes" style="vertical-align: top;" value="0" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
								</div>
							</td>
						 </tr>
						  <tr>
							<td>					
								<label>*Apply Credits when Used</label>
							</td>
							<td>
								<input type="radio" id="chk_applyCreditYes" name="chk_applyCreditYes" style="vertical-align: top;" value="1" >&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_applyCreditNo" name="chk_applyCreditYes"  style="vertical-align: top;" value="0" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
								</div>
							</td>
						 </tr>
						</table>
												
						<div Style="width:100%;clear:both;margin-right:2px;margin-top:40px;">
						<span id="commissionSettingMsg" style="color: green;float: right;margin-top: -22px"></span>
						<input type="button" value="Save" id="btnCommissionSettings" class="savehoverbutton turbo-blue" style="float: right;width:80px;" onclick="saveCommissionSettings()"/>
						</div>
	</fieldset>
	
	<!-- Created by:Naveed   Date  :  03/11/2014 -->

	<fieldset class= " ui-widget-content ui-corner-all" >
						<legend class="custom_legend"><label><b>Employee General</b></label></legend>
						<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
							<td><label>Limit employees to their own division in reports</label></td>
							<td>
								<input type="radio" id="chk_limitEmpYes" name="chk_limitEmpYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_limitEmpYes" name="chk_limitEmpYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
							</td>
						 </tr>
						 <tr>
						 	<td><label>Use branch level access when viewing reports</label></td>
							<td>
								<input type="radio" id="chk_branchLevelViewYes" name="chk_branchLevelViewYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_branchLevelViewYes" name="chk_branchLevelViewYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
							</td>
						 </tr>
						 <tr>
						 	<td><label>Limit Employee/Billing Profit Report to Logged in SalesRep?</label></td>
							<td>
								<input type="radio" id="chk_limitEmpProfitYes" name="chk_limitEmpProfitYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_limitEmpProfitYes" name="chk_limitEmpProfitYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
							</td>
						 </tr>
						  <tr>
						 	<td><label>Limit Customer/Invoice Cost Detail Report to Logged in SalesRep?</label></td>
							<td>
								<input type="radio" id="chk_limitCusCostYes" name="chk_limitCusCostYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_limitCusCostYes" name="chk_limitCusCostYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;" >No</span>
							</td>
						 </tr>
						   <tr>
						 	<td><label>Limit Employee/Billing Profit Detail Report to Logged in SalesRep?</label></td>
							<td>
								<input type="radio" id="chk_limitEmpProfitDetailYes" name="chk_limitEmpProfitDetailYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_limitEmpProfitDetailYes" name="chk_limitEmpProfitDetailYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
							</td>
						 </tr>
						   <tr>
						 	<td><label>Limit Customer/Billing Profit Detail Report to Logged in SalesRep?</label></td>
							<td>
								<input type="radio" id="chk_limitCusProfitDetailYes" name="chk_limitCusProfitDetailStatusYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
								<input type="radio" id="chk_limitCusProfitDetailYes" name="chk_limitCusProfitDetailStatusYes"  style="vertical-align: top;" checked="checked">&nbsp;<span style="color:#00377A;">No</span>
							</td>
						 </tr>
						 </table>
												
						<div Style="width:100%;clear:both;margin-right:2px;margin-top:40px;">
						<input type="button" value="Save" id="btnEmployGeneral" class="savehoverbutton turbo-blue" style="float: right;width:80px;" />
						</div>
	</fieldset>	

</div>
<div id="settingsJobDetailsBlock">&nbsp;
<fieldset class= " ui-widget-content ui-corner-all" >
<legend class="custom_legend"><label><b>Bid status</b></label></legend>
<td>
<table>

				<tr>
					<td style="padding-right: 20px;">
						<table id="bidstatusgrid"></table>
					</td>
					<td>
						<div id="bidstatustab" ></div>
							
								
								<div id="bidstatusdiv" style="padding: 0px;width:240px; top:50px;background-color: #FAFAFA;">
									</div>
									<table>
										<tr>
										   <td style="position: absolute;top: 10%;right: 34%;width: 260px;">
										      <form id =bidstatusgridform>
											      <fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"></legend>
											      
														    <table>
																<tr>
															    	<td><label>Description: </label></td>
																	<td><input type="text" name="Description_ID" id="BidDescriptionID" style="width:280px;height: 24px;"  maxlength="20" class="validate[required] validate[maxSize[12]" value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>Code: </label></td>
																	 <td><input type="text" name="trackingUrl" id="bidcodeid" maxlength="6" /></td>
																	 <td>Inactive: <input type="checkbox" name="Description_ID" id="inactivebid" value=""></td>
																	 <td><input type="hidden" name="trackingUrl" id="bidstatusid"  /></td>
																	 
																</tr>
																
																<tr style="height: 15px"><td></td></tr>
														 </table>
														</fieldset>
														<table style="position:relative;top:25px;">
												<tr>
												<td><label id="messsagetouser"></label></td>
												</tr>
											 		<tr>
											 			<td align="left">
													    	<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="addBidStatus()">
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button"  name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="editBidStatus()">
													    	<input type="button"  value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteBidStatus()">
										     		    </td>
													</tr>
												</table>
														</form>
												
											</td>
										</tr>
									</table>
								
							
						
					</td>
				</tr>
			</table>
			<td>
			</fieldset>
			
				<!-- Created by:Naveed   Date  :  06/09/2014 -->
			<fieldset class="ui-widget-content ui-corner-all" style="margin-left: 4px;width:1030px;height:280px;">
				<legend><label><b>Footer Note</b></label></legend>
				<table >
					<tr align="center">
						<td style="width: 160px;text-align: right;"><label>Footnote Terms:</label></td>
						<td style="height : 150px">
							<textarea  id="footerId" value="teest"  style="visibility: hidden;" >${requestScope.quoteFooterNotes}</textarea>
						</td>	
						<td>
							<input	type="button" id="saveQuoteFooter" name="saveQuoteFooter"	value="Save" class="savehoverbutton turbo-blue"	style="width: 80px;" onclick="saveQuoteFooter()">
							&nbsp;
							<!-- <span id="footerNoteSuccess"></span> -->
						</td>
					</tr>
					<tr>
					<td colspan ="3" style ="text-align:right;padding-right:100px;">
					<span id="footerNoteSuccess"></span>
					</td>
					</tr>
				</table>							
												
			</fieldset>
			
			<fieldset class="ui-widget-content ui-corner-all" style="margin-left: 4px;width:253px;height:300px;">
												<legend class="custom_legend"><label><b>Employees Assigned</b></label></legend>
										<form name="jobcustomercategoryform" id="jobcustomercategoryform">
										<table>
											<tr>
											<td>
											<input type="hidden" name="joCategory1Id"  id="joCategory1Id" value="${requestScope.JobCategoryId1}"/>
											<input type="text" maxlength="12" name="joCategory1Desc"   id="joCategory1Desc" value="${requestScope.JobCategory1}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory2Id"  id="joCategory2Id" value="${requestScope.JobCategoryId2}"/>
											<input type="text" maxlength="12" name="joCategory2Desc"  id="joCategory2Desc" value="${requestScope.JobCategory2}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory3Id"   id="joCategory3Id" value="${requestScope.JobCategoryId3}"/>
											<input type="text" maxlength="12" name="joCategory3Desc" class="jobcustomerCategory3Desc" id="joCategory3Desc" value="${requestScope.JobCategory3}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory4Id"  id="joCategory4Id" value="${requestScope.JobCategoryId4}"/>
											<input type="text" maxlength="12" name="joCategory4Desc" class="jobcustomerCategory4Desc"  id="joCategory4Desc" value="${requestScope.JobCategory4}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory5Id"   id="joCategory5Id" value="${requestScope.JobCategoryId5}"/>
											<input type="text" maxlength="12" name="joCategory5Desc" class="jobcustomerCategory5Desc" id="joCategory5Desc" value="${requestScope.JobCategory5}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory6Id"   id="joCategory6Id" value="${requestScope.JobCategoryId6}"/>
											<input type="text" maxlength="12" name="joCategory6Desc"  id="joCategory6Desc" value="${requestScope.JobCategory6}"></td>
											</tr>
											<tr>
											<td>
											<input type="hidden" name="joCategory7Id"   id="joCategory7Id" value="${requestScope.JobCategoryId7}"/>
											<input type="text" maxlength="12" name="joCategory7Desc"  id="joCategory7Desc" value="${requestScope.JobCategory7}"></td>
											</tr>
											<tr><td></td></tr>
											<tr align="left">
											<td>
											<input	type="button" id="savecucategory" name="savecucategory"	value="Save" class="savehoverbutton turbo-blue"	onclick="checkjobcategorysave()" style="position: absolute;top: 54%;right: 842px">
											</td>
											</tr>
											<tr>
											<td><div id="jobCustomerErrorDiv"></div></td>
											</tr>
											
										</table>
										</form>
										</fieldset>
										<fieldset class="ui-widget-content ui-corner-all" style="position: absolute;top: 43.5%;right: 2%;width: 745px;height: 300px;">
											<legend class="custom_legend"><label><b>Quotes Category</b></label></legend>
											<form name="jobquotecolumnform" id="jobquotecolumnform">
												<table>
													<tr>
														<td>
															<table id="quotesCategoryGrid"></table>
														</td>
														<td>
															<div id="quoteCategorytab" ></div>
															<div id="quoteCategorydiv" style="padding: 0px;width:240px; top:50px;background-color: #FAFAFA;"></div>
																<table>
																	<tr>
																	   <td style="position: absolute;width: 260px;">
																	      <form id =quoteCategorygridform>
																		      <legend class="custom_legend"></legend>
																		      
																			    <table>
																					<tr>
																				    	<td><label>Description: </label></td>
																						<td><input type="text" name="quotesCategoryDescription" id="quotesCategoryDescription" style="width:210px;height: 24px;"  maxlength="20" class="validate[required] validate[maxSize[12]" value=""></td>
																						<input type="hidden" name="quotesCategoryid" id="quotesCategoryid"  />
																					</tr>																						
																				 </table>
																				<table style="position:relative;top:25px;">
																					<tr>
																						<td><label id="quoteCategorymesssagetouser"></label></td>
																					</tr>
																					<tr>
																						<td colspan="2" align="center">
																				    		<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="addQuotesCategory()">
																			    			<input type="button"  name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="editQuotesCategory()">
																							<input type="button"  value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteQuotesCategory()">
																			  		    </td>
																					</tr>
																				</table>
																			</form>																			
																		</td>
																	</tr>
																</table>
														</td>
													</tr>
												</table>
											</form>
										</fieldset>
										
										
										
										<fieldset class= " ui-widget-content ui-corner-all" >
                    <legend class="custom_legend"><label><b>Settings</b></label></legend>
                    <td>
                        <table>
                            <tr>
                                <form name="jobsettingscolumnform" id="jobsettingscolumnform">
                                <td style="padding-right: 20px;">            
									<table>
                                    	<tr>
                                        <td colspan="2"><label>Allow booking Jobs with no Contract Amount</label></td>
	                                        <c:if test="${requestScope.chkalwbookjobscamtYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkalwbookjobscamtYN" name="chkalwbookjobscamtYN" value="Yes" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkalwbookjobscamtYN" name="chkalwbookjobscamtYN" value="No">No</input></td>
	                                       	</c:if>
	                                       	<c:if test="${requestScope.chkalwbookjobscamtYN == 0||empty requestScope.chkalwbookjobscamtYN}">
		                                        <td colspan="2"><input type="radio" id="chkalwbookjobscamtYN" name="chkalwbookjobscamtYN" value="Yes">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkalwbookjobscamtYN" name="chkalwbookjobscamtYN" value="No" checked="checked">No</input></td>
	                                       	</c:if>
                                        </tr>
										<tr>
                                        <td colspan="2"><label>Allow Editing of Sent Quotes?</label></td>
                                        	<c:if test="${requestScope.chkeditsentquoteYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkeditsentquoteYN"     value="Yes"       name="chkeditsentquoteYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkeditsentquoteYN"    value="No"        name="chkeditsentquoteYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkeditsentquoteYN == 0 ||empty requestScope.chkeditsentquoteYN}">
		                                        <td colspan="2"><input type="radio" id="chkeditsentquoteYN"      value="Yes"      name="chkeditsentquoteYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkeditsentquoteYN"     value="No"       name="chkeditsentquoteYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        <tr>
                                       
                                        <tr>
                                        	<td colspan="2"><label>Allow releases if Final Waiver checked?</label></td>
                                        	<c:if test="${requestScope.chkalrwaichekedYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkalrwaichekedYN"  value="Yes" name="chkalrwaichekedYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkalrwaichekedYN" value="No" name="chkalrwaichekedYN">No</input></td>
	                                       	</c:if>
	                                       	<c:if test="${requestScope.chkalrwaichekedYN == 0 ||empty requestScope.chkalrwaichekedYN}">
		                                        <td colspan="2"><input type="radio" id="chkalrwaichekedYN" value="Yes" name="chkalrwaichekedYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkalrwaichekedYN" value="No" name="chkalrwaichekedYN" checked="checked">No</input></td>
	                                       	</c:if>
                                        </tr>
                                        
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Allow changing Job#</label></td> -->
<!--                                         <td colspan="2"><input type="radio" id="chkjobcheckoffYN"			  name="chkjobcheckoffYN"><label style="vertical-align: super;">Yes</label></td> -->
<!--                                         <td colspan="2"><input type="radio" id="chkjobcheckoffYN"			  name="chkjobcheckoffYN" checked="checked"><label style="vertical-align: super;">No</label></td> -->
<!--                                         </tr> -->
                                        
                                        <tr>
                                       	 	<td colspan="2"><label>Closeout last invoice check -</label>
                                        	<input type="text" class="number" id="chkcloselastinvchkDays" value="${requestScope.chkcloselastinvchkDays}" style="width: 50px;"/><label> days old</label></td>
                                        	<c:if test="${requestScope.chkcloselastinvchkYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkcloselastinvchkYN" value="Yes" name="chkcloselastinvchkYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkcloselastinvchkYN" value="No" name="chkcloselastinvchkYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkcloselastinvchkYN == 0 ||empty requestScope.chkcloselastinvchkYN}">
		                                        <td colspan="2"><input type="radio" id="chkcloselastinvchkYN" value="Yes" name="chkcloselastinvchkYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkcloselastinvchkYN" value="No" name="chkcloselastinvchkYN" checked="checked">No</input></td>
		                                    </c:if>
		                                    
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Default to Approved credit for a new Job</label></td>
	                                        <c:if test="${requestScope.chkdefcreditYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdefcreditYN" value="Yes"  name="chkdefcreditYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefcreditYN" value="No"  name="chkdefcreditYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkdefcreditYN == 0 ||empty requestScope.chkdefcreditYN}">
		                                        <td colspan="2"><input type="radio" id="chkdefcreditYN"  value="Yes" name="chkdefcreditYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefcreditYN" value="No"  name="chkdefcreditYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Default Division to User Creating the Job</label></td>
	                                        <c:if test="${requestScope.chkdefdivisionYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdefdivisionYN"       value="Yes"       name="chkdefdivisionYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefdivisionYN"   value="No"           name="chkdefdivisionYN">No</input></td>
	                                       	</c:if>
	                                       	<c:if test="${requestScope.chkdefdivisionYN == 0 || empty requestScope.chkdefdivisionYN}">
		                                        <td colspan="2"><input type="radio" id="chkdefdivisionYN"    value="Yes"          name="chkdefdivisionYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefdivisionYN"    value="No"          name="chkdefdivisionYN" checked="checked">No</input></td>
	                                       	</c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Default 'Job Tax Territory' to Customer Tax</label></td>
<%-- 	                                        <c:if test="${requestScope.chkdefJobTaxTerritoryYN == 1}"> --%>
		                                        <td colspan="2"><input type="radio" id="chkdefJobTaxTerritoryYN"  value="Yes" name="chkdefJobTaxTerritoryYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefJobTaxTerritoryYN" value="No"  name="chkdefJobTaxTerritoryYN">No</input></td>
<%-- 		                                    </c:if> --%>
<%-- 	                                     	<c:if test="${requestScope.chkdefJobTaxTerritoryYN == 0}"> --%>
<!-- 		                                        <td colspan="2"><input type="radio" id="chkdefJobTaxTerritoryYN"  value="Yes" name="chkdefJobTaxTerritoryYN">Yes</input></td> -->
<!-- 		                                        <td colspan="2"><input type="radio" id="chkdefJobTaxTerritoryYN" value="No"  name="chkdefJobTaxTerritoryYN" checked="checked">No</input></td> -->
<%-- 		                                    </c:if> --%>
                                        </tr>
                                        <tr>
                                         <!--  EditedBy: Naveed	Date: 19 Oct 2015  Ref : ID #430 -->
                                          <td colspan="2"><label>Override Release Tax Territory with Customer Tax</label></td>
		                                     <c:if test="${requestScope.chkdefOverRideTaxTerritoryYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdefOverRideTaxTerritoryYN"  value="Yes" name="chkdefOverRideTaxTerritoryYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefOverRideTaxTerritoryYN" value="No"  name="chkdefOverRideTaxTerritoryYN" >No</input></td>
                                      		</c:if>
	                                       	<c:if test="${requestScope.chkdefOverRideTaxTerritoryYN == 0 || empty requestScope.chkdefOverRideTaxTerritoryYN}">
	                                        	<td colspan="2"><input type="radio" id="chkdefOverRideTaxTerritoryYN"  value="Yes" name="chkdefOverRideTaxTerritoryYN" >Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefOverRideTaxTerritoryYN" value="No"  name="chkdefOverRideTaxTerritoryYN" checked="checked">No</input></td>
	                                       	</c:if>
                                        </tr>
                                        <!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #276
                                        <tr>
	                                        <td colspan="2"><label>Default Quote By to User creating the Job</label></td>
	                                        <c:if test="${requestScope.chkdefaultquoteYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdefaultquoteYN"     value="Yes"   name="chkdefaultquoteYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefaultquoteYN"  value="No"      name="chkdefaultquoteYN">No</input></td>
	                                       	</c:if>
	                                       	 <c:if test="${requestScope.chkdefaultquoteYN == 0|| empty requestScope.chkdefaultquoteYN}">
		                                        <td colspan="2"><input type="radio" id="chkdefaultquoteYN"   value="Yes"     name="chkdefaultquoteYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefaultquoteYN"  value="No"      name="chkdefaultquoteYN" checked="checked">No</input></td>
	                                       	</c:if>
                                        </tr>
                                        -->
                                        <!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #277
                                        <tr>
	                                        <td colspan="2"><label>Default Sales Rep to User Creating the Job</label></td>
	                                        <c:if test="${requestScope.chkdefsalesrepYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdefsalesrepYN"      value="Yes"        name="chkdefsalesrepYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefsalesrepYN"    value="No"          name="chkdefsalesrepYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkdefsalesrepYN == 0 || empty requestScope.chkdefsalesrepYN}">
		                                        <td colspan="2"><input type="radio" id="chkdefsalesrepYN"      value="Yes"        name="chkdefsalesrepYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdefsalesrepYN"    value="No"          name="chkdefsalesrepYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        -->
                                        <!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #278
                                        <tr>
	                                        <td colspan="2"><label>Default Take Off to User creating the Job</label></td>
	                                        <c:if test="${requestScope.chkdeftakeoffYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkdeftakeoffYN"     value="Yes"          name="chkdeftakeoffYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdeftakeoffYN"      value="No"         name="chkdeftakeoffYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkdeftakeoffYN == 0 || empty requestScope.chkdeftakeoffYN}">
		                                        <td colspan="2"><input type="radio" id="chkdeftakeoffYN"        value="Yes"       name="chkdeftakeoffYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkdeftakeoffYN"     value="No"          name="chkdeftakeoffYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        -->
                                        <!--  EditedBy: Naveed	Date: 20 Aug2015  Ref : ID #279
                                        <tr>
	                                        <td colspan="2"><label>Import Purchase Order details into new Customer Invoices</label></td>
	                                        <c:if test="${requestScope.chkimpponewcustomerYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkimpponewcustomerYN" value="Yes" name="chkimpponewcustomerYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkimpponewcustomerYN" value="No" name="chkimpponewcustomerYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkimpponewcustomerYN == 0 || empty requestScope.chkimpponewcustomerYN}">
		                                        <td colspan="2"><input type="radio" id="chkimpponewcustomerYN" value="Yes" name="chkimpponewcustomerYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkimpponewcustomerYN" value="No" name="chkimpponewcustomerYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        -->
                                        <tr>
	                                       <!--  <td colspan="2"><label>Must have addendum,quote thru before save</label></td> -->
	                                       <td colspan="2"><label>Require Received and Quote Thru in Addendum box</label></td>
	                                        <c:if test="${requestScope.chkaddendumYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkaddendumYN"		 value="Yes"	      name="chkaddendumYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkaddendumYN"		 value="No"	      name="chkaddendumYN" >No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkaddendumYN == 0|| empty requestScope.chkaddendumYN}">
		                                        <td colspan="2"><input type="radio" id="chkaddendumYN"		 value="Yes"	      name="chkaddendumYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkaddendumYN"		 value="No"	      name="chkaddendumYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                       
                                        <tr>
	                                        <td colspan="2"><label>Require Customer PO when Booking or Quick Booking Jobs?</label></td>
	                                        <c:if test="${requestScope.chkreqCPOcreatjobYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkreqCPOcreatjobYN" value="Yes" name="chkreqCPOcreatjobYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqCPOcreatjobYN" value="No" name="chkreqCPOcreatjobYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkreqCPOcreatjobYN == 0|| empty requestScope.chkreqCPOcreatjobYN}">
		                                        <td colspan="2"><input type="radio" id="chkreqCPOcreatjobYN" value="Yes" name="chkreqCPOcreatjobYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqCPOcreatjobYN" value="No" name="chkreqCPOcreatjobYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        <tr>
	                                        <td colspan="2"><label>Require a Division when creating Jobs</label></td>
	                                        <c:if test="${requestScope.chkreqdivcreatjobYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkreqdivcreatjobYN" value="Yes" name="chkreqdivcreatjobYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqdivcreatjobYN" value="No" name="chkreqdivcreatjobYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkreqdivcreatjobYN == 0|| empty requestScope.chkreqdivcreatjobYN}">
		                                        <td colspan="2"><input type="radio" id="chkreqdivcreatjobYN" value="Yes" name="chkreqdivcreatjobYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqdivcreatjobYN" value="No" name="chkreqdivcreatjobYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Require a Engineer when booking Jobs?</label></td>
	                                        <c:if test="${requestScope.chkreqengbookjobYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkreqengbookjobYN" value="Yes" name="chkreqengbookjobYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqengbookjobYN" value="No" name="chkreqengbookjobYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkreqengbookjobYN == 0 || empty requestScope.chkreqengbookjobYN}">
		                                        <td colspan="2"><input type="radio" id="chkreqengbookjobYN" value="Yes" name="chkreqengbookjobYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkreqengbookjobYN" value="No" name="chkreqengbookjobYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        <tr>
	                                        <td colspan="2"><label>Require Split Commissions on Line Item Release?</label></td>
	                                        <c:if test="${requestScope.chkSplitCommissionYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkSplitCommissionYN" value="Yes" name="chkSplitCommissionYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkSplitCommissionYN" value="No" name="chkSplitCommissionYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkSplitCommissionYN == 0 || empty requestScope.chkSplitCommissionYN}">
		                                        <td colspan="2"><input type="radio" id="chkSplitCommissionYN" value="Yes" name="chkSplitCommissionYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkSplitCommissionYN" value="No" name="chkSplitCommissionYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Show Billing Remainder on Job Statements?</label></td>
	                                        <c:if test="${requestScope.chkshwbilremjobstYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkshwbilremjobstYN" value="Yes" name="chkshwbilremjobstYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkshwbilremjobstYN" value="No" name="chkshwbilremjobstYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkshwbilremjobstYN == 0 || empty requestScope.chkshwbilremjobstYN}">
		                                        <td colspan="2"><input type="radio" id="chkshwbilremjobstYN" value="Yes" name="chkshwbilremjobstYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkshwbilremjobstYN" value="No" name="chkshwbilremjobstYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Show credits on Job Statements?</label></td>
<%-- 	                                        <c:if test="${requestScope.chkscrejobstYN == 1}"> --%>
		                                        <td colspan="2"><input type="radio" id="chkscrejobstYN" value="Yes" name="chkscrejobstYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkscrejobstYN" value="No" name="chkscrejobstYN">No</input></td>
<%-- 		                                    </c:if> --%>
<%-- 		                                    <c:if test="${requestScope.chkscrejobstYN == 0}"> --%>
<!-- 		                                        <td colspan="2"><input type="radio" id="chkscrejobstYN" value="Yes" name="chkscrejobstYN">Yes</input></td> -->
<!-- 		                                        <td colspan="2"><input type="radio" id="chkscrejobstYN" value="No" name="chkscrejobstYN" checked="checked">No</input></td> -->
<%-- 		                                    </c:if> --%>
                                        </tr>
                                        
                                        <%-- <tr>
	                                        <td colspan="2"><label>Track Cost on Change Orders?</label></td>
	                                        <c:if test="${requestScope.chktkcoschordYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chktkcoschordYN" value="Yes" name="chktkcoschordYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chktkcoschordYN" value="No" name="chktkcoschordYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chktkcoschordYN == 0}">
		                                        <td colspan="2"><input type="radio" id="chktkcoschordYN" value="Yes" name="chktkcoschordYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chktkcoschordYN" value="No" name="chktkcoschordYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr> --%>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Use Customer's credit limit when creating jobs</label></td>
	                                        <c:if test="${requestScope.chkUserCustomerCreditYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkUserCustomerCreditYN" value="Yes" name="chkUserCustomerCreditYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkUserCustomerCreditYN" value="No" name="chkUserCustomerCreditYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkUserCustomerCreditYN == 0 || empty requestScope.chkUserCustomerCreditYN}">
		                                        <td colspan="2"><input type="radio" id="chkUserCustomerCreditYN" value="Yes" name="chkUserCustomerCreditYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkUserCustomerCreditYN" value="No" name="chkUserCustomerCreditYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                        
                                        <tr>
	                                        <td colspan="2"><label>Warn if NOC not received when ordering?</label></td>
	                                        <c:if test="${requestScope.chkdefdivisionYN == 1}">
		                                        <td colspan="2"><input type="radio" id="chkwnocrordYN" value="Yes" name="chkwnocrordYN" checked="checked">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkwnocrordYN" value="No" name="chkwnocrordYN">No</input></td>
		                                    </c:if>
		                                    <c:if test="${requestScope.chkdefdivisionYN == 0|| empty requestScope.chkdefdivisionYN}">
		                                        <td colspan="2"><input type="radio" id="chkwnocrordYN" value="Yes" name="chkwnocrordYN">Yes</input></td>
		                                        <td colspan="2"><input type="radio" id="chkwnocrordYN" value="No" name="chkwnocrordYN" checked="checked">No</input></td>
		                                    </c:if>
                                        </tr>
                                                                   
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Optional Job/Quote Check off</label></td> -->
<!--                                         <td><input type="radio" id="chkchangejobYN"				  name="chkchangejobYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkchangejobYN"				  name="chkchangejobYN" checked="checked"><label>No</label></td></tr> -->
<!--                                         <tr><td colspan="2"><label>Optional Memo Field on Quote</label></td> -->
<!--                                         <td><input type="radio" id="chkmemoYN"                    name="chkmemoYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkmemoYN"                    name="chkmemoYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td><label>Job Notice label#1</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticelbl1TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl1YN"           name="chkjobnoticelbl1YN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl1YN"           name="chkjobnoticelbl1YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
<!--                                         <tr> -->
<!--                                         <td><label>Job Notice Report Type#1</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticereporttype1TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype1YN"    name="chkjobnoticereporttype1YN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype1YN"    name="chkjobnoticereporttype1YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                       
<!--                                         <tr> -->
<!--                                         <td><label>Job Notice label#2</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticelbl2TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl2YN"           name="chkjobnoticelbl2YN" ><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl2YN"           name="chkjobnoticelbl2YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->

<!--                                         <tr> -->
<!--                                         <td><label>Job Notice Report Type#2</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticereporttype2TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype2YN"    name="chkjobnoticereporttype2YN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype2YN"    name="chkjobnoticereporttype2YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td><label>Job Notice label#3</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticelbl3TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl3YN"           name="chkjobnoticelbl3YN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticelbl3YN"           name="chkjobnoticelbl3YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td><label>Job Notice Report Type#3</label></td> -->
<!--                                         <td><input type="text" id="chkjobnoticereporttype3TXT" style="width: 125px;"/></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype3YN"    name="chkjobnoticereporttype3YN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobnoticereporttype3YN"    name="chkjobnoticereporttype3YN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Default Bid Date to Current Date</label></td> -->
<!--                                         <td><input type="radio" id="chkbidtocurdateYN"            name="chkbidtocurdateYN"><label>Yes</label></td><td> -->
<!--                                         <input type="radio" id="chkbidtocurdateYN"            name="chkbidtocurdateYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Job Details 'Other' label</label></td> -->
<!--                                         <td><input type="radio" id="chkjobdetailotherlblYN"       name="chkjobdetailotherlblYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjobdetailotherlblYN"       name="chkjobdetailotherlblYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
                                       
<!--                                         <td colspan="4"> -->
<!--                                           <fieldset class= " ui-widget-content ui-corner-all" > -->
<!--                     						<legend class="custom_legend"><label><b>Source</b></label></legend> -->
<!--                     						<table> -->
<!--                             					<tr> -->
<!--                             						<td><label>CheckBox#1</label></td> -->
<%--                                       			  	<td><input id="sourceCheckBox1" type="text" style="width: 125px;"  value="${requestScope.txt_SourceCheckBox1}"/></td> --%>
<!--                                       				<td><label>CheckBox#3</label></td> -->
<%--             	                            		<td><input id="sourceCheckBox2" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox3}"/></td> --%>
<!--             	                            	</tr> -->
<!--             	                            	<tr> -->
<!--                                        				<td><label>CheckBox#2</label></td> -->
<%--                                       			  	<td><input id="sourceCheckBox3" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox2}"/></td> --%>
<!--                                       				<td><label>CheckBox#4</label></td> -->
<%--             	                            		<td><input id="sourceCheckBox4" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox4}"/></td> --%>
<!--                                       		  	</tr> -->
<!--                                       		  	<tr> -->
<!--                                        				<td><label>Label#1</label></td> -->
<%--                                       			  	<td><input id="sourceLabel1" type="text" style="width: 125px;" value="${requestScope.txt_SourceLabel1}"/></td> --%>
<!--                                       				<td><label>Label#2</label></td> -->
<%--             	                            		<td><input id="sourceLabel2" type="text" style="width: 125px;" value="${requestScope.txt_SourceLabel1}"/></td> --%>
<!--                                       		  	</tr> -->
<!--                                       		</table> -->
<!--                             			</fieldset> -->
<!--                                          </td> -->
<!--                                       </tr> -->
                                    </table>
                                </td>
                                 
                                <td style="padding-right: 20px;vertical-align: top;">
                                    <table>
<!--                                         <tr> -->
<!--                                         <td><label>Job Details 'Report' label</label></td> -->
<!--                                         <td><input type="text" id="chkjodetreplabTXT" style="width: 125px;"></td> -->
<!--                                         <td style="width: 44px;"><input type="radio" id="chkjodetreplabYN" name="chkjodetreplabYN"><label style="vertical-align: super;">Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkjodetreplabYN" name="chkjodetreplabYN" checked="checked"><label style="vertical-align: super;">No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Apply costs to customer invoice</label></td> -->
<!--                                         <td><input type="radio" id="chkappcocusinvYN" name="chkappcocusinvYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkappcocusinvYN" name="chkappcocusinvYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                       
                                       
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Use Customer's Credit Limit when creating Jobs</label></td> -->
<!--                                         <td><input type="radio" id="chkcuscredlimcrjobsYN" name="chkcuscredlimcrjobsYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkcuscredlimcrjobsYN" name="chkcuscredlimcrjobsYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
                                       
                                       
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Hide Postponed option in Jobs?</label></td> -->
<!--                                         <td><input type="radio" id="chkhidepostjobsYN" name="chkhidepostjobsYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkhidepostjobsYN" name="chkhidepostjobsYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Only allow owners or supervisors to change Quote Templates</label></td> -->
<!--                                         <td><input type="radio" id="chkonalsuqottempYN" name="chkonalsuqottempYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkonalsuqottempYN" name="chkonalsuqottempYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
                                       
                                       
                                       
                                        
                                        
<!--                                         <tr> -->
<!--                                         <td colspan="2"><label>Show Branches?</label></td> -->
<!--                                         <td><input type="radio" id="chkshowbranchYN" name="chkshowbranchYN"><label>Yes</label></td> -->
<!--                                         <td><input type="radio" id="chkshowbranchYN" name="chkshowbranchYN" checked="checked"><label>No</label></td> -->
<!--                                         </tr> -->
                                        
										<tr>
											<td colspan="4">
												<fieldset class= " ui-widget-content ui-corner-all" >
												<legend class="custom_legend"><label><b>Source</b></label></legend>
												<table>
													<tr>
													<td><label>CheckBox#1</label></td>
													<td><input id="sourceCheckBox1" type="text" style="width: 125px;"  value="${requestScope.txt_SourceCheckBox1}"/></td>
													<td><label>CheckBox#3</label></td>
													<td><input id="sourceCheckBox2" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox3}"/></td>
													</tr>
													<tr>
													<td><label>CheckBox#2</label></td>
														<td><input id="sourceCheckBox3" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox2}"/></td>
													<td><label>CheckBox#4</label></td>
													<td><input id="sourceCheckBox4" type="text" style="width: 125px;" value="${requestScope.txt_SourceCheckBox4}"/></td>
													</tr>
													<tr>
													<td><label>Label#1</label></td>
													<td><input id="sourceLabel1" type="text" style="width: 125px;" value="${requestScope.txt_SourceLabel1}"/></td>
													<td><label>Label#2</label></td>
													<td><input id="sourceLabel2" type="text" style="width: 125px;" value="${requestScope.txt_SourceLabel2}"/></td>
													</tr>
												</table>
												</fieldset>
											</td>
										</tr>
                                        
										<tr>
											<td colspan="4">
											<fieldset class= " ui-widget-content ui-corner-all" >
											<legend class="custom_legend"><label><b>Plan & Spec</b></label></legend>
												<table>
													<tr>
														<td><label>Label#1</label></td>
														<td><input id="planSpecLabel1" type="text" style="width: 125px;" value="${requestScope.txt_PlanSpecLabel1}"/></td>
														<td><label>Label#2</label></td>
														<td><input id="planSpecLabel2" type="text" style="width: 125px;" value="${requestScope.txt_PlanSpecLabel2}"/></td>
													</tr>
												</table>
											</fieldset>
											</td>
										</tr>
										<tr>
											<td colspan="4">
												<fieldset class= " ui-widget-content ui-corner-all" >
												<legend class="custom_legend"><label><b>Notices</b></label></legend>
													<table>
				                                        <tr>
					                                        <td><label>Job Notice label#1</label></td>
					                                        <td><input type="text" id="chkjobnoticelbl1TXT" style="width: 125px;" value="${requestScope.chkjobnoticelbl1TXT}"/></td>
					                                         <c:if test="${requestScope.chkjobnoticelbl1YN == 1}">
						                                        <td><input type="radio" id="chkjobnoticelbl1YN"      checked="checked"   value="Yes"   name="chkjobnoticelbl1YN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl1YN"      value="No"     name="chkjobnoticelbl1YN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkjobnoticelbl1YN == 0}">
						                                        <td><input type="radio" id="chkjobnoticelbl1YN"    value="Yes"     name="chkjobnoticelbl1YN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl1YN"  value="No"    checked="checked"      name="chkjobnoticelbl1YN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                      	
				                                      	<tr>
					                                        <td><label>Job Notice label#2</label></td>
					                                        <td><input type="text" id="chkjobnoticelbl2TXT" style="width: 125px;" value="${requestScope.chkjobnoticelbl2TXT}"/></td>
					                                        <c:if test="${requestScope.chkjobnoticelbl2YN == 1}">
						                                        <td><input type="radio" id="chkjobnoticelbl2YN"  value="Yes"  checked="checked"        name="chkjobnoticelbl2YN" ><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl2YN"   value="No"        name="chkjobnoticelbl2YN"><label>No</label></td>
						                                    </c:if>
						                                    <c:if test="${requestScope.chkjobnoticelbl2YN == 0}">
					                                         <td><input type="radio" id="chkjobnoticelbl2YN"     value="Yes"      name="chkjobnoticelbl2YN" ><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl2YN"    value="No"       name="chkjobnoticelbl2YN" checked="checked"><label>No</label></td>
						                                    </c:if>
				                                        </tr>

				                                        <tr>
					                                        <td><label>Job Notice label#3</label></td>
					                                        <td><input type="text" id="chkjobnoticelbl3TXT" style="width: 125px;" value="${requestScope.chkjobnoticelbl3TXT}"/></td>
					                                         <c:if test="${requestScope.chkjobnoticelbl3YN == 1}">
						                                        <td><input type="radio" id="chkjobnoticelbl3YN"   checked="checked"  value="Yes"       name="chkjobnoticelbl3YN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl3YN"     value="No"      name="chkjobnoticelbl3YN"><label>No</label></td>
						                                     </c:if>
						                                      <c:if test="${requestScope.chkjobnoticelbl3YN == 0}">
						                                        <td><input type="radio" id="chkjobnoticelbl3YN"   value="Yes"        name="chkjobnoticelbl3YN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkjobnoticelbl3YN"   value="No"        name="chkjobnoticelbl3YN" checked="checked"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                        
				                                    
				                                        
													</table>
												</fieldset>
											</td>
										</tr>
										<tr>
											<td colspan="4">
												<fieldset class= " ui-widget-content ui-corner-all" >
												<legend class="custom_legend"><label><b>Quote Settings</b></label></legend>
													<table>
				                                        <tr>
					                                        <td><label>Require 'Qty' on Item2 and Item3</label></td>
					                                         <c:if test="${requestScope.chkI2I3QtyYN == 1}">
						                                        <td><input type="radio" id="chkI2I3QtyYN"      checked="checked"   value="Yes"   name="chkI2I3QtyYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3QtyYN"      value="No"     name="chkI2I3QtyYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkI2I3QtyYN == 0}">
						                                        <td><input type="radio" id="chkI2I3QtyYN"    value="Yes"     name="chkI2I3QtyYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3QtyYN"  value="No"    checked="checked"      name="chkI2I3QtyYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                         <tr>
					                                        <td><label>Require 'Cost' on Item2 and Item3</label></td>
					                                         <c:if test="${requestScope.chkI2I3CostYN == 1}">
						                                        <td><input type="radio" id="chkI2I3CostYN"      checked="checked"   value="Yes"   name="chkI2I3CostYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3CostYN"      value="No"     name="chkI2I3CostYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkI2I3CostYN == 0}">
						                                        <td><input type="radio" id="chkI2I3CostYN"    value="Yes"     name="chkI2I3CostYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3CostYN"  value="No"    checked="checked"      name="chkI2I3CostYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                         <tr>
					                                        <td><label>Require 'Sell Price' on Item3</label></td>
					                                         <c:if test="${requestScope.chkI3SellPriceYN == 1}">
						                                        <td><input type="radio" id="chkI3SellPriceYN"      checked="checked"   value="Yes"   name="chkI3SellPriceYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI3SellPriceYN"      value="No"     name="chkI3SellPriceYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkI3SellPriceYN == 0}">
						                                        <td><input type="radio" id="chkI3SellPriceYN"    value="Yes"     name="chkI3SellPriceYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI3SellPriceYN"  value="No"    checked="checked"      name="chkI3SellPriceYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                         <tr>
					                                        <td><label>Require 'Manufacturer' on Item2 and Item3</label></td>
					                                         <c:if test="${requestScope.chkI2I3ManufYN == 1}">
						                                        <td><input type="radio" id="chkI2I3ManufYN"      checked="checked"   value="Yes"   name="chkI2I3ManufYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3ManufYN"      value="No"     name="chkI2I3ManufYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkI2I3ManufYN == 0}">
						                                        <td><input type="radio" id="chkI2I3ManufYN"    value="Yes"     name="chkI2I3ManufYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3ManufYN"  value="No"    checked="checked"      name="chkI2I3ManufYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                         <tr>
					                                        <td><label>Require 'Category' on Item2 and Item3</label></td>
					                                         <c:if test="${requestScope.chkI2I3CatYN == 1}">
						                                        <td><input type="radio" id="chkI2I3CatYN"      checked="checked"   value="Yes"   name="chkI2I3CatYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3CatYN"      value="No"     name="chkI2I3CatYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkI2I3CatYN == 0}">
						                                        <td><input type="radio" id="chkI2I3CatYN"    value="Yes"     name="chkI2I3CatYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkI2I3CatYN"  value="No"    checked="checked"      name="chkI2I3CatYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                         <tr>
					                                        <td><label>Require 'Sell Price' on Price</label></td>
					                                         <c:if test="${requestScope.chkSPpriceYN == 1}">
						                                        <td><input type="radio" id="chkSPpriceYN"      checked="checked"   value="Yes"   name="chkSPpriceYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkSPpriceYN"      value="No"     name="chkSPpriceYN"><label>No</label></td>
						                                     </c:if>
						                                     <c:if test="${requestScope.chkSPpriceYN == 0}">
						                                        <td><input type="radio" id="chkSPpriceYN"    value="Yes"     name="chkSPpriceYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkSPpriceYN"  value="No"    checked="checked"      name="chkSPpriceYN"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                        
				                                            <!-- Created By: Leo  Date: 26-02-2016 Purpose :ID#485  -->
				                                         <tr>
					                                        <td><label>Font Size on Price shall be</label>
					                                        <select name="fontsizeonPrice"
																id="fontsizeonPrice">
																	<option value="8" >8pt</option>
																	<option value="10">10pt</option>
																	<option value="12" >12pt</option>
																	<option value="14">14pt</option>
																	<option value="18" >18pt</option>
															</select> 
															<input type="hidden" name="fontsizeonPricehiddenID"  id="fontsizeonPricehiddenID" value="${requestScope.inp_fontsizeonPriceYN}"/>
															<input type="hidden" name="chkfontsizeonPricehiddenID"  id="chkfontsizeonPricehiddenID" value="${requestScope.chkfontsizeonPriceYN}"/>
															</td>
					                                         <c:if test="${requestScope.chkfontsizeonPriceYN == 1}">
						                                        <td><input type="radio" id="chkfontsizeonPriceY"   checked="checked"  value="Yes"       name="chkfontsizeonPricelblYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkfontsizeonPriceN"     value="No"      name="chkfontsizeonPricelblYN"><label>No</label></td>
						                                     </c:if>
						                                      <c:if test="${requestScope.chkfontsizeonPriceYN == 0}">
						                                        <td><input type="radio" id="chkfontsizeonPriceY"   value="Yes"        name="chkfontsizeonPricelblYN"><label>Yes</label></td>
						                                        <td><input type="radio" id="chkfontsizeonPriceN"   value="No"        name="chkfontsizeonPricelblYN" checked="checked"><label>No</label></td>
						                                     </c:if>
				                                        </tr>
				                                       <!--  -->  
				                                        
				                                        
				                                        </table>
				                                   </fieldset>
				                                </td>
										</tr>
									</table>
                                </td>
                                </form>
                            </tr>
<!--                             <tr> -->
<!--                                 <td></td> -->
<!--                                 <td align="right" style="padding-left:300px;"> -->
<!--                                  	<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="" style=" width:80px;">    -->
<!--                                 </td> -->
<!--                             </tr> -->
                        </table>
                    </td>
	                    <div Style="width:100%;clear:both;margin-right:2px;margin-top:30px;">
							<span id="but_jobsettings" style="color: green;float: right;margin-top: -22px"></span>
							<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveJobSettingsSysVariable();" style=" width:80px;">
						</div>
                    </fieldset>
										
										

</div>

<div id="settingsInventoryDetailsBlock" style="margin-top:-37px;">&nbsp;
<!-- 

Created by              : Leo.N     Date  :  10/09/2014
Description             : Inventory Settings fieldset.


 -->
<fieldset class= " ui-widget-content ui-corner-all" >
											      <legend class="custom_legend"><label><b>Settings</b></label></legend>
						<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
						<td>					
						<div Style="width:70%;display:block;float:left;line-height:15px;"><span>Override all Warehousing Cost Mark-up inside Category with</span>
						<span><input type="text" id="txt_overridewarehouse" class="number" style="text-align:right;width:125px;" />?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invoverridewarehouseYes" name="chk_invoverridewarehouseYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invoverridewarehouseYes" name="chk_invoverridewarehouseYes"  style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>						
						
						<div Style="width:70%;display:block;float:left;line-height:26px;"><span>Specify Purchase Order cost?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invpurOrderCostYes" name="chk_invpurOrderCostYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invpurOrderCostYes" name="chk_invpurOrderCostYes"  style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Multiple Warehouses Average Costing?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invwareavgcostYes" name="chk_invwareavgcostYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invwareavgcostYes" name="chk_invwareavgcostYes"  style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>		
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Show Bin Location on Pick Tickets?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invbinLocationYes" name="chk_invbinLocationYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invbinLocationYes" name="chk_invbinLocationYes" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Show Weight on Pick Tickets?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invWeightYes" name="chk_invWeightYes" style="vertical-align: top;"  ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invWeightYes" name="chk_invWeightYes"  style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Use Warehouses address on Pick Tickets?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="radio" id="chk_invWarhouseaddressYes" name="chk_invWarhouseaddressYes" style="vertical-align: top;" ></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="radio" id="chk_invWarhouseaddressYes" name="chk_invWarhouseaddressYes"  style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>						
														
						</td>
						</tr>
						</table>
												
						<div Style="width:100%;clear:both;margin-right:2px;margin-top:30px;">
						<span id="but_inventorysettingserrordiv" style="color: green;float: right;margin-top: -22px"></span>
						<input type="button" value="Save" id="but_inventorysettings" class="savehoverbutton turbo-blue" style="float: right;width:80px;" onclick="saveinventorysettings()"/>
						</div>
						</fieldset>

<fieldset class= " ui-widget-content ui-corner-all" style="width:120px">
<legend class="custom_legend"><label><b>Tier Pricing Levels</b></label></legend>
<form name="tierpricingformname" id="tierpricingformid">
										<table style="width:120px;">
										<tr>
											<td>
											<input type="text" name="tierpricingid0" class="tierpricingid0" id="prPriceLevel0" maxlength="12" value="${requestScope.prPriceLevel0}"/>
											</td>
											</tr>
											<tr>
											<td>
											<input type="text" name="tierpricingid1" class="tierpricingid1" id="prPriceLevel1"  maxlength="12" value="${requestScope.prPriceLevel1}"/>
											</td>
											</tr>
											<tr>
											<td>
											<input type="text" name="tierpricingid2" class="tierpricingid2" id="prPriceLevel2"  maxlength="12" value="${requestScope.prPriceLevel2}"/>
											</td>
											</tr>
											<tr>
											<td>
											<input type="text" name="tierpricingid3" class="tierpricingid3"  id="prPriceLevel3"  maxlength="12" value="${requestScope.prPriceLevel3}"/>
											</td>
											</tr>
											<tr>
											<td>
											<input type="text" name="tierpricingid4" class="tierpricingid4" id="prPriceLevel4" maxlength="12" value="${requestScope.prPriceLevel4}"/>
											</td>
											</tr>
											<tr>
											<td>
											<input type="text" name="tierpricingid5" class="tierpricingid5" id="prPriceLevel5" maxlength="12" value="${requestScope.prPriceLevel5}"/>
											</td>
											</tr>
											
											
											<tr align="left">
											<td>
											<input	type="button" id="savecucategory" name="savecucategory"	value="Save" class="savehoverbutton turbo-blue"	onclick="savePriceTier()">
											</td>
											</tr>
											<tr>
											<td><div id="tierpricingmsgdiv"></div></td>
											</tr>
											
										</table>
										</form>
</fieldset>
<fieldset class= " ui-widget-content ui-corner-all" style="width:798px;margin-left:233px ;margin-top:-203px ;">
<legend class="custom_legend"><label><b>Departments</b></label></legend>
 <jsp:include page="settings_inventory_departments.jsp" />
</fieldset>
</div>

<div id="settingsBankingDetailsBlock">&nbsp;
<!--  Created by: Naveed     Date  :  10/21/2014        Description: Banking Settings fieldset. -->

	<fieldset class= " ui-widget-content ui-corner-all" >
			<legend class="custom_legend"><label><b>Settings</b></label></legend>
					<table style="width:979px;margin:25px auto;margin-top:0px;margin-bottom:0px;">
						<tr>
						<td>					
						<div Style="width:70%;display:block;float:left;line-height:15px;"><span>Payroll Check has (2)Stubs?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_payrollchkYes" name="chk_payrollchkStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_payrollchkNo" name="chk_payrollchkStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>						
						
						<div Style="width:70%;display:block;float:left;line-height:26px;"><span>Payroll Check in the middle?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_payrollchkMiddleYes" name="chk_payrollchkMiddleStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_payrollchkMiddleNo" name="chk_payrollchkMiddleStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Payroll Check print Verbose dollars?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_verbosedollarYes" name="chk_verbosedollarStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_verbosedollarNo" name="chk_verbosedollarStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>		
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Vendor Check in the middle?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendorchkMiddleYes" name="chk_vendorchkMiddleStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendorchkMiddleNo" name="chk_vendorchkMiddleStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Vendor Check print Verbose dollars?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendorchkVerboseDollarYes" name="chk_vendorchkVerboseDollarStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendorchkVerboseDollarNo" name="chk_vendorchkVerboseDollarStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	
						
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Vendor Check on bottom and (2)Stubs?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_vendorchkbottomYes" name="chk_vendorchkbottomStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_vendorchkbottomNo" name="chk_vendorchkbottomStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>						
							
						<div Style="width:70%;display:block;float:left;line-height:25px;"><span>Number of lines on detail on each check?</span>
						<span><input type="text" id="txt_numberoflines" class="number" style="text-align:right;width:50px;" />?</span></div>
						<div Style="width:30%;display:block;float:right;">
						<span><input type="checkbox" id="chk_numberoflinesYes" name="chk_numberoflinesStatus" style="vertical-align: top;"  value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
						<span style="margin-left:75px;"><input type="checkbox" id="chk_numberoflinesNo" name="chk_numberoflinesStatus" value="no" style="vertical-align: top;"></span>&nbsp;<span style="color:#00377A;">No</span>
						</div>	
													
						</td>
						</tr>
					</table>
						
						<div Style="width:100%;clear:both;margin-right:2px;">
						<input type="button" value="Save" id="but_inventorysettings" class="savehoverbutton turbo-blue" style="float: right;width:80px;" />
						</div>
	</fieldset>
</div>

	<div style="display: none;">
		<input type="text" id="chartsAccID" name="chartsAccName"
			value="${requestScope.userDetails.chartsperpage}">
	</div>
</div>
</div>

<div style="display: none;">
	<table>
		<tr>
			<td>
				<input type="text" id="quote_ID" name="quote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quote}">
				<input type="text" id="quickQuote_ID" name="quickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quickQuote}">
				<input type="text" id="invoice_ID" name="invoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.invoices}">
				<input type="text" id="purchaseOrder_ID" name="purchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.purchaseOrders}">
				<input type="text" id="headerQuote_ID" name="headerQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuote}">
				<input type="text" id="headerQuick_ID" name="headerQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuickQuote}">
				<input type="text" id="headerInvoice_ID" name="headerInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerInvoices}">
				<input type="text" id="headerPurchaseOrder_ID" name="headerPurchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerPurchaseOrders}">
				<input type="text" id="termsQuote_ID" name="termsQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuote}">
				<input type="text" id="termsQuickQuote_ID" name="termsQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuickQuote}">
				<input type="text" id="termsInvoice_ID" name="termsInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsInvoices}">
				<input type="text" id="termsPO_ID" name="termsPO_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsPurchaseOrders}">
			</td>
		</tr>
	</table>
</div>
<table id="footer">
  <tr>
	<td colspan="2">
		<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
	</td>
 </tr>
</table>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.form.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/settings.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/paymentsterms.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/shipVia.js"></script>
<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
<script type="text/javascript">
jQuery(document).ready(function(){

	tinyMCE.init({
        mode : "textareas",
        theme : "modern",
        editor_selector : "TinyMCETextEditor",
        toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor fontselect fontsizeselect",
	   	menubar: false,
	   	statusbar: false,
	    toolbar_items_size: 'small',
	    force_br_newlines : true,
        force_p_newlines : false,
        forced_root_block : 'div',
	    width: "700",
        height: "100",
        setup: function (ed) {
            ed.on('keypress', function (e) {
	            if(tinymce.activeEditor.getContent()==""||(tinymce.activeEditor.selection.getNode().style.fontSize=="" && tinymce.activeEditor.selection.getNode().style.fontFamily=="")){
	            	myCustomInitInstance(ed);
		            }
            });
        },
        //init_instance_callback : "myCustomInitInstance",
        theme_advanced_buttons3_add : "pastetext,pasteword,selectall",
	    paste_auto_cleanup_on_paste : true,
	    paste_retain_style_properties: "all",
        paste_preprocess : function(pl, o) {
           var pastevalue=o.content;
           pastevalue=pastevalue.replaceAll("<p", "<div ").replaceAll("</p>", "</div>");
            o.content =pastevalue;
	       } 
});

	var edd="";
	function myCustomInitInstance(ed) {  
	edd=ed;
	  try{
		//ed.selection.getNode().style.fontSize='10pt';
		//ed.selection.getNode().style.fontName='Helvetica';
	    ed.execCommand("fontName", false, "Helvetica");    
	    ed.execCommand("fontSize", false, "10pt");    
	  }
	  catch(err){      
	  }
	}
	
	
	$("#digstatus").hide();
	
	$("#settingsCustmerDetails").removeClass("ui-state-disabled");
	$("#settingsVendorDetails").removeClass("ui-state-disabled");
	$("#settingsEmployeeDetails").removeClass("ui-state-disabled");
	$("#settingsJobDetails").removeClass("ui-state-disabled");
	$("#settingsInventoryDetails").removeClass("ui-state-disabled");
	$("#settingsBankingDetails").removeClass("ui-state-disabled");


		/*
		 * Create by         :  Leo                  Date: 10/09/2014
		 * Description       :  Numeric Validation process with class name 
		 */
			$('.number').keypress(function (event) {
			    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
			        event.preventDefault();
			    }
		
			    var text = $(this).val();
		
			    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
			        event.preventDefault();
			    }
			});

		/* 	
			$("#segment3id").attr('readonly', 'readonly');
			$("#segment4id").attr('readonly', 'readonly');
			
			$("#digits3id").attr('readonly', 'readonly');
			$("#digits4id").attr('readonly', 'readonly'); */

			$("#required1noid").prop('checked',true);
			$("#required2noid").prop('checked',true);
			$("#required3noid").prop('checked',true);


			$("#required1yesid").click(function(){

				
				$("#required1noid").prop('checked',false);
				$("#segment2id").removeAttr('readonly');
				$("#digits2id").removeAttr('readonly');
			});

			$("#required1noid").click(function()
			{
				$("#required1yesid").prop('checked',false);
				$("#segment2id").val("");
				$("#digits2id").val("");
				$("#segment2id").attr('readonly', 'readonly');
				$("#digits2id").attr('readonly', 'readonly');
			});



			$("#required2yesid").click(function(){
			
				$("#required2noid").prop('checked',false);
				$("#segment3id").removeAttr('readonly');
				$("#digits3id").removeAttr('readonly');
				
			});

			$("#required2noid").click(function()
			{
				$("#required2yesid").prop('checked',false);
				$("#segment3id").attr('readonly', 'readonly');
				$("#digits3id").attr('readonly', 'readonly');
				$("#segment3id").val("");
				$("#digits3id").val("");
			});
			
			
			$("#required3yesid").click(function(){
			
				$("#required3noid").prop('checked',false);
				$("#segment4id").removeAttr('readonly');
				$("#digits4id").removeAttr('readonly');
			});

			$("#required3noid").click(function()
			{	
				$("#required3yesid").prop('checked',false);
				$("#segment4id").attr('readonly', 'readonly');
				$("#digits4id").attr('readonly', 'readonly');
				$("#segment4id").val("");
				$("#digits4id").val("");
			});
	loadchSegments();

	if($("#required1noid").is(":checked"))
	{
		$("#segment2id").attr('readonly', 'readonly');
		$("#digits2id").attr('readonly', 'readonly');
	}
	if($("#required2noid").is(":checked"))
	{
		$("#segment3id").attr('readonly', 'readonly');
		$("#digits3id").attr('readonly', 'readonly');
	}
	if($("#required3noid").is(":checked"))
	{
		$("#segment4id").attr('readonly', 'readonly');
		$("#digits4id").attr('readonly', 'readonly');
	}

	/* Defined Rolodex Categories - CheckBox click handler */
	$("#Category1Descyesid").click(function(){
		$("#Category1Descnoid").prop('checked',false);
	});

	$("#Category1Descnoid").click(function(){
		$("#Category1Descyesid").prop('checked',false);
	});

	$("#Category2Descyesid").click(function(){
		$("#Category2Descnoid").prop('checked',false);
	});

	$("#Category2Descnoid").click(function(){
		$("#Category2Descyesid").prop('checked',false);
	});

	$("#Category3Descyesid").click(function(){
		$("#Category3Descnoid").prop('checked',false);
	});

	$("#Category3Descnoid").click(function(){
		$("#Category3Descyesid").prop('checked',false);
	});

	$("#Category4Descyesid").click(function(){
		$("#Category4Descnoid").prop('checked',false);
	});

	$("#Category4Descnoid").click(function(){
		$("#Category4Descyesid").prop('checked',false);
	});

	$("#Category5Descyesid").click(function(){
		$("#Category5Descnoid").prop('checked',false);
	});

	$("#Category5Descnoid").click(function(){
		$("#Category5Descyesid").prop('checked',false);
	});

	$("#Category6Descyesid").click(function(){
		$("#Category6Descnoid").prop('checked',false);
	});

	$("#Category6Descnoid").click(function(){
		$("#Category6Descyesid").prop('checked',false);
	});

	$("#Category7Descyesid").click(function(){
		$("#Category7Descnoid").prop('checked',false);
	});

	$("#Category7Descnoid").click(function(){
		$("#Category7Descyesid").prop('checked',false);
	});

	$("#Category8Descyesid").click(function(){
		$("#Category8Descnoid").prop('checked',false);
	});

	$("#Category8Descnoid").click(function(){
		$("#Category8Descyesid").prop('checked',false);
	});
	
	SetInventoryEditSettings();
	setEmployeeSettings();


	$("#chk_venpoDesYes").click(function(){
		$("#chk_venpoDesNo").prop('checked',false);
	});
	$("#chk_vencmpanylogoYes").click(function(){
		$("#chk_vencmpanylogoNo").prop('checked',false);
	});
	$("#chk_vendorPhnoYes").click(function(){
		$("#chk_vendorPhnoNo").prop('checked',false);
	});
	$("#chk_venproductidYes").click(function(){
		$("#chk_venproductidNo").prop('checked',false);
	});
	$("#chk_venclosePoYes").click(function(){
		$("#chk_venclosePoNo").prop('checked',false);
	});
	$("#chk_venreqInvYes").click(function(){
		$("#chk_venreqInvNo").prop('checked',false);
	});
	$("#chk_vendefaultInvYes").click(function(){
		$("#chk_vendefaultInvNo").prop('checked',false);
	});
	$("#chk_IncTaxPOInvYes").click(function(){
		$("#chk_IncTaxPOInvNo").prop('checked',false);
	});


	$("#chk_venpoDesNo").click(function(){
		$("#chk_venpoDesYes").prop('checked',false);
	});
	$("#chk_vencmpanylogoNo").click(function(){
		$("#chk_vencmpanylogoYes").prop('checked',false);
	});
	$("#chk_vendorPhnoNo").click(function(){
		$("#chk_vendorPhnoYes").prop('checked',false);
	});
	$("#chk_venproductidNo").click(function(){
		$("#chk_venproductidYes").prop('checked',false);
	});
	$("#chk_venclosePoNo").click(function(){
		$("#chk_venclosePoYes").prop('checked',false);
	});
	$("#chk_venreqInvNo").click(function(){
		$("#chk_venreqInvYes").prop('checked',false);
	});
	$("#chk_vendefaultInvNo").click(function(){
		$("#chk_vendefaultInvYes").prop('checked',false);
	});
	$("#chk_IncTaxPOInvNo").click(function(){
		$("#chk_IncTaxPOInvYes").prop('checked',false);
	});
});

function setEmployeeSettings(){
	var chk_invoiceCombineYes="${requestScope.chk_invoiceCombineYes}";
	var txt_adjdescription1="${requestScope.txt_adjdescription1}";
	var txt_adjdescription2="${requestScope.txt_adjdescription2}";
	var txt_adjdescription3="${requestScope.txt_adjdescription3}";
	var txt_adjdescription4="${requestScope.txt_adjdescription4}";
	var txt_commissiondesc1="${requestScope.txt_commissiondesc1}";
	var txt_commissiondesc2="${requestScope.txt_commissiondesc2}";
	var txt_commissiondesc3="${requestScope.txt_commissiondesc3}";
	var txt_commissiondesc4="${requestScope.txt_commissiondesc4}";
	var chk_viewCommissionYes="${requestScope.chk_viewCommissionYes}";
	var chk_allocatedProfitYes="${requestScope.chk_allocatedProfitYes}";
	var chk_applyCreditYes="${requestScope.chk_applyCreditYes}";



	if(chk_invoiceCombineYes!="" && chk_invoiceCombineYes!=null && chk_invoiceCombineYes=="1"){
		$('#chk_invoiceCombineYes').prop('checked', true);
	}
	if(chk_viewCommissionYes!="" && chk_viewCommissionYes!=null && chk_viewCommissionYes=="1"){
		$('#chk_viewCommissionYes').prop('checked', true);
	}
	if(chk_allocatedProfitYes!="" && chk_allocatedProfitYes!=null && chk_allocatedProfitYes=="1"){
		$('#chk_allocatedProfitYes').prop('checked', true);
	}
	if(chk_applyCreditYes!="" && chk_applyCreditYes!=null && chk_applyCreditYes=="1"){
		$('#chk_applyCreditYes').prop('checked', true);
	}
	
	
	if(txt_adjdescription1!="" && txt_adjdescription1!=null){
		$("#txt_adjdescription1").val(txt_adjdescription1);
	}
	if(txt_adjdescription2!="" && txt_adjdescription2!=null){
		$("#txt_adjdescription2").val(txt_adjdescription2);
	}
	if(txt_adjdescription3!="" && txt_adjdescription3!=null){
		$("#txt_adjdescription3").val(txt_adjdescription3);
	}
	if(txt_adjdescription4!="" && txt_adjdescription4!=null){
		$("#txt_adjdescription4").val(txt_adjdescription4);
	}
	if(txt_commissiondesc1!="" && txt_commissiondesc1!=null){
		$("#txt_commissiondesc1").val(txt_commissiondesc1);
	}
	if(txt_commissiondesc2!="" && txt_commissiondesc2!=null){
		$("#txt_commissiondesc2").val(txt_commissiondesc2);
	}
	if(txt_commissiondesc3!="" && txt_commissiondesc3!=null){
		$("#txt_commissiondesc3").val(txt_commissiondesc3);
	}
	if(txt_commissiondesc4!="" && txt_commissiondesc4!=null){
		$("#txt_commissiondesc4").val(txt_commissiondesc4);
	}
	
	}
function SetInventoryEditSettings(){
var txt_overridewarehouse="${requestScope.txt_overridewarehouse}";
var chk_invoverridewarehouseYes="${requestScope.chk_invoverridewarehouseYes}";
var chk_invpurOrderCostYes="${requestScope.chk_invpurOrderCostYes}";
var chk_invwareavgcostYes="${requestScope.chk_invwareavgcostYes}";
var chk_invbinLocationYes="${requestScope.chk_invbinLocationYes}";
var chk_invWeightYes="${requestScope.chk_invWeightYes}";
var chk_invWarhouseaddressYes="${requestScope.chk_invWarhouseaddressYes}";
if(txt_overridewarehouse!="" && txt_overridewarehouse!=null){
	$("#txt_overridewarehouse").val(txt_overridewarehouse);
}
if(chk_invoverridewarehouseYes!="" && chk_invoverridewarehouseYes!=null && chk_invoverridewarehouseYes=="1"){
	$('#chk_invoverridewarehouseYes').prop('checked', true);
}

if(chk_invpurOrderCostYes!="" && chk_invpurOrderCostYes!=null && chk_invpurOrderCostYes=="1"){
	$('#chk_invpurOrderCostYes').prop('checked', true);
}
if(chk_invwareavgcostYes!="" && chk_invwareavgcostYes!=null && chk_invwareavgcostYes=="1"){
	$('#chk_invwareavgcostYes').prop('checked', true);
}
if(chk_invbinLocationYes!="" && chk_invbinLocationYes!=null && chk_invbinLocationYes=="1"){
	$('#chk_invbinLocationYes').prop('checked', true);
}
if(chk_invWeightYes!="" && chk_invWeightYes!=null && chk_invWeightYes=="1"){
	$('#chk_invWeightYes').prop('checked', true);
}
if(chk_invWarhouseaddressYes!="" && chk_invWarhouseaddressYes!=null && chk_invWarhouseaddressYes=="1"){
	$('#chk_invWarhouseaddressYes').prop('checked', true);
}
}

function loadchSegments()
{
	var i=1;
	var reqstate;
	console.log("hi");
	 <c:forEach var="ch" items="${requestScope.chsegments}">
		console.log("<c:out value="${ch.segmentsName}"></c:out>");

		if(i==1)
			{
			$("#segment"+i+"id").val("<c:out value="${ch.segmentsName}"></c:out>");
			$("#digits"+i+"id").val("<c:out value="${ch.digitsallowed}"></c:out>");
			}
		else
			{
			reqstate=0;
			$("#segment"+i+"id").val("<c:out value="${ch.segmentsName}"></c:out>");
			$("#digits"+i+"id").val("<c:out value="${ch.digitsallowed}"></c:out>");
			
			reqstate="<c:out value="${ch.requiredstatus}"></c:out>"

			console.log(reqstate+"--"+i);

			if(reqstate==1)
				{
				$("#required"+(i-1)+"yesid").attr("checked",true);
				$("#required"+(i-1)+"noid").attr("checked",false);
				}
			else
				{
				$("#required"+(i-1)+"noid").attr("checked",true);
				$("#segment"+i+"id").attr('readonly', true);
				$("#digits"+i+"id").attr('readonly', true);
				}
		
			}
i++;

console.log(i);
	</c:forEach> 

}

function loadwareHouseBilToAddress(){
	var reminders = [];
    <c:forEach items="${requestScope.wareHouse}" var="reminder">
        reminders.push({description: "${reminder.description}", address1: "${reminder.address1}", address2: "${reminder.address2}", city: "${reminder.city}", state: "${reminder.state}", zip: "${reminder.zip}"});
    </c:forEach>
    var aWareHouse = "${requestScope.wareHouse}";
    if(aWareHouse.length !== 0){
	   	var countWarehouse = aWareHouse.split(",");
	    if(countWarehouse.length == 1){
	    	$("#NorcrossbillToAddressID").val(reminders[0].description); $("#NorcrossbillToAddressID1").val(reminders[0].address1); $("#NorcrossbillToAddressID2").val(reminders[0].address2); $("#NorcrossbillToCity").val(reminders[0].city);
			 $("#NorcrossbillToState").val(reminders[0].state); $("#NorcrossbillToZipID").val(reminders[0].zip);
	    }
	    if(countWarehouse.length == 2){
			 $("#NorcrossbillToAddressID").val(reminders[0].description); $("#NorcrossbillToAddressID1").val(reminders[0].address1); $("#NorcrossbillToAddressID2").val(reminders[0].address2); $("#NorcrossbillToCity").val(reminders[0].city);
			 $("#NorcrossbillToState").val(reminders[0].state); $("#NorcrossbillToZipID").val(reminders[0].zip);
			 $("#BirminghambillToAddressID").val(reminders[1].description); $("#BirminghambillToAddressID1").val(reminders[1].address1); $("#BirminghambillToAddressID2").val(reminders[1].address2); $("#BirminghambillToCity").val(reminders[1].city);
			 $("#BirminghambillToState").val(reminders[1].state); $("#BirminghambillToZipID").val(reminders[1].zip);
	    }
	}
}
function addCommissionSplitTypes(){
	var description = $('#splittypedescription').val();
	var percentage = $('#spltypdefaultper').val();
	var oper = true;
	 $.ajax({
        url: "./job_controller/updateEmployeeCommissionsplitype",
        data: {description:description,percentage:percentage,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Add Commission Split Types','Info','Company-Settings,Adding Commission Split Types,description:'+description);
        	 $('#splittypedescription').val("");
        	 $('#spltypdefaultper').val("");
        	 $("#ecSplitCodeHiddenID").val("");
        	$("#empSplitTypesGrid").trigger("reloadGrid");
        	
        }
   }); 
	
}
function editCommissionSplitTypes(){
	var description = $('#splittypedescription').val();
	var percentage = $('#spltypdefaultper').val();
	var ecSplitCodeId=$("#ecSplitCodeHiddenID").val();
	var oper = false;
	 $.ajax({
        url: "./job_controller/updateEmployeeCommissionsplitype",
        data: {ecSplitCodeId:ecSplitCodeId,description:description,percentage:percentage,operation:oper},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Edit Commission Split Types','Info','Company-Settings,Editing Commission Split Types,description:'+description);
        	 $('#splittypedescription').val("");
        	 $('#spltypdefaultper').val("");
        	 $("#ecSplitCodeHiddenID").val("");
        	$("#empSplitTypesGrid").trigger("reloadGrid");
        	
        }
   }); 
}
function deleteCommissionSplitTypes(){
	var ecSplitCodeId=$("#ecSplitCodeHiddenID").val();
	 $.ajax({
        url: "./job_controller/deleteEmployeeCommissionsplitype",
        data: {ecSplitCodeId:ecSplitCodeId},
        type: 'POST',
        success: function(data){
        	createtpusage('Company-Settings','Delete Commission Split Types','Info','Company-Settings,Deleting Commission Split Types,ecSplitCodeId:'+ecSplitCodeId);
        	 $('#splittypedescription').val("");
        	 $('#spltypdefaultper').val("");
        	 $("#ecSplitCodeHiddenID").val("");
        	$("#empSplitTypesGrid").trigger("reloadGrid");
        	
        }
   }); 
}

String.prototype.replaceAll = function(target, replacement) {
	  return this.split(target).join(replacement);
	};
		  
</script>
</body>
</html>