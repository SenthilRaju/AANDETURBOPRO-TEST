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
		<table>	
			<tr>
				<td width="200px;">
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Home</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="homeID" name="homeName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Home</label>
								<input type="text" id="homeAccesModuleID" name="homeAccesModuleName" style="display: none;" value="10000"><input type="text" id="homeAccesProcedureID" name="homeAccesProcedureName" style="display: none;" style="display: none;" value="1001000">
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
								<input type="checkbox" id="jobQuotesID" name="jobQuotesName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Quotes</label>
								<input type="text" id="jobQuoteAccesModuleID" name="jobQuoteAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobQuoteAccesProcedureID" name="jobQuoteAccesProcedureName" style="display: none;" value="1401000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobSubmittalID" name="jobSubmittalName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Submittal</label>
								<input type="text" id="jobSubmittalAccesModuleID" name="jobSubmittalAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobSubmittalAccesProcedureID" name="jobSubmittalAccesProcedureName" style="display: none;"  value="1402000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobCreditID" name="jobCreditName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Credit</label>
								<input type="text" id="jobCreditAccesModuleID" name="jobCreditAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobCreditAccesProcedureID" name="jobCreditAccesProcedureName" style="display: none;"  value="1403000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobReleaseID" name="jobReleaseName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Release</label>
								<input type="text" id="jobReleaseAccesModuleID" name="jobReleaseAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobReleaseAccesProcedureID" name="jobReleaseAccesProcedureName" style="display: none;"  value="1404000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobFinancialID" name="jobFinancialName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Financial</label>
								<input type="text" id="jobFinancialAccesModuleID" name="jobFinancialAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobFinancialAccesProcedureID" name="jobFinancialAccesProcedureName" style="display: none;"  value="1405000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="jobJournalID" name="jobJournalName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Journal</label>
								<input type="text" id="jobJournalAccesModuleID" name="jobJournalAccesModuleName" style="display: none;" value="14000"><input type="text" id="jobJournalAccesProcedureID" name="jobJournalAccesProcedureName" style="display: none;" style="display: none;"  value="1406000">
							</td>
						</tr>
						<tr height="137px;"></tr>
					</table>
				</td>
				<td width="200px;">
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Sales</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="salesID" name="salesName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Sales</label>
								<input type="text" id="salesAccesModuleID" name="salesAccesModuleName" style="display: none;" value="11000"><input type="text" id="salesAccesProcedureID" name="salesAccesProcedureName" style="display: none;" style="display: none;" value="1101000">
							</td>
						</tr>
						<tr height="20px;"></tr>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Company</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyCustomerID" name="CompanyCustomerName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Customers</label>
								<input type="text" id="companyCustomerAccesModuleID" name="companyCustomerAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyCustomerAccesProcedureID" name="companyCustomerAccesProcedureName" style="display: none;" value="1501000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyPaymentsID" name="CompanyPaymentsName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Payments</label>
								<input type="text" id="companyPaymentAccesModuleID" name="companyPaymentAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyPaymentAccesProcedureID" name="companyPaymentAccesProcedureName" style="display: none;" value="1501100">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyVendorID" name="CompanyVendorName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Vendors</label>
								<input type="text" id="companyVendorAccesModuleID" name="companyVendorAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyVendorAccesProcedureID" name="companyVendorAccesProcedureName" style="display: none;" value="1502000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyPayBillID" name="CompanyPayBillName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Pay Bills</label>
								<input type="text" id="companyPayBillAccesModuleID" name="companyPayBillAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyPayBillAccesProcedureID" name="companyPayBillAccesProcedureName" style="display: none;" value="1502100">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyEmployeeID" name="CompanyEmployeeName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Employees</label>
								<input type="text" id="companyEmployeeAccesModuleID" name="companyEmployeeAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyEmployeeAccesProcedureID" name="companyEmployeeAccesProcedureName" style="display: none;" value="1503000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyCommisionsID" name="CompanyCommisionsName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Commissions</label>
								<input type="text" id="companyCommisionsAccesModuleID" name="companyCommisionsAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyCommisionsAccesProcedureID" name="companyCommisionsAccesProcedureName" style="display: none;" value="1503100">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyRolodexID" name="CompanyRolodexName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Rolodex</label>
								<input type="text" id="companyRolodexAccesModuleID" name="companyRolodexAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyRolodexAccesProcedureID" name="companyRolodexAccesProcedureName" style="display: none;" value="1504000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyUserID" name="CompanyUserName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Users</label>
								<input type="text" id="companyUserAccesModuleID" name="companyUserAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyUserAccesProcedureID" name="companyUserAccesProcedureName" style="display: none;" value="1505000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanySettingID" name="CompanySettingName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Settings</label>
								<input type="text" id="companySettingAccesModuleID" name="companySettingAccesModuleName" style="display: none;" value="15000"><input type="text" id="companySettingAccesProcedureID" name="companySettingAccesProcedureName" style="display: none;" value="1506000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanReportID" name="CompanyReplortName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Reports</label>
								<input type="text" id="companyReportsAccesModuleID" name="companyReportsAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyReportsAccesProcedureID" name="companyReportsAccesProcedureName" style="display: none;" value="1507000">
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="CompanyAccountID" name="CompanyAccountName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Chart Accounts</label>
								<input type="text" id="companyAccountAccesModuleID" name="companyAccountAccesModuleName" style="display: none;" value="15000"><input type="text" id="companyAccountAccesProcedureID" name="companyAccountAccesProcedureName" style="display: none;" value="1508000">
							</td>
						</tr>
					</table>
				</td>
				<td width="200px;">
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Project</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="projectID" name="projectName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Project</label>
								<input type="text" id="projectAccesModuleID" name="projectAccesModuleName" style="display: none;" value="12000"><input type="text" id="projectAccesProcedureID" name="projectAccesProcedureName" style="display: none;" value="1201000">
							</td>
						</tr>
						<tr height="350px;"></tr>
					</table>
				</td>
				<td width="200px;">
					<table>
						<tr>
							<td>
								<label style="font-weight: bold;font-size: 15px;">Inventory</label>
							</td>
						</tr>
						<tr>
							<td>
								<input type="checkbox" id="inventoryID" name="inventoryName" style="vertical-align: middle;" class="userPermission" onchange="updateUserPermission()"><label style="vertical-align: middle;">Inventory</label>
								<input type="text" id="inventoryAccesModuleID" name="inventoryAccesModuleName" style="display: none;" value="13000"><input type="text" id="inventoryProcedureID" name="inventoryProcedureName" style="display: none;" style="display: none;" value="1301000">
							</td>
						</tr>
						<tr height="350px;"></tr>
					</table>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td>
					<input class="quickQuotehoverbutton turbo-blue" id ="allowId" name="allowName" type="button" style="width:100px;" value="Allow">
					<input class="quickQuotehoverbutton turbo-blue" id ="denyId" name="denyName" type="button" style="width:100px;" onclick="" value="Deny">
					<input class="quickQuotehoverbutton turbo-blue" id ="groupDefaultId" name="groupDefaultName" type="button" style="width:100px;" onclick="" value="Group Default">
				</td>
			</tr>
		</table>
	</div>
	<div style="display: none;">
		<input type="text" id="loginID" value="${sessionScope.user.userId}">
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/userPermissions.js">	</script>
	</body>
	</html>