<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Inventory Departments</title>
		<style type="text/css">
			#search {display: none;}
			input[type='checkbox'] {
				margin-left: 0px;
				margin-right: 11px;
			}
			.accountRangeInput {width: 80px;}
			.accountRangeInputID {display: none;}
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			.overrideFont{font-family: Verdana,Arial,sans-serif; font-size: .8em;}
		</style>
	</head>
<body>
<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;padding-bottom: 30px; padding-top: 0px;height:620px">
				<tr>
					<td style="padding-right: 20px;">
						<table>
						    <tr>
						    	<td>
									<table id="inventoryDepartmentsGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					<td style="vertical-align: top;">
						<div id="inventoryDepartmentDetails" style="margin-bottom: 15px;margin-top:75px;">
							<fieldset class= " ui-widget-content ui-corner-all">
								<legend><label><b>GL Accounts</b></label></legend>
								<form action="" id="departmentDetailsForm">
								<table>
									<tr>
										<td>
											<input type="text" id="invDepartmentId" name="invDepartmentId" style="display: none;">
										</td>
									</tr>
									<tr>
										<td><label class="overrideFont">Description: </label></td>
										<td><input type="text" id="departmentName" name="name" placeholder="Enter Description to add new Department" style="width: 140%;"></td>
										<td><label style="position: relative;left: 112%" class="overrideFont">In Active: </label><input type="checkbox" value='1' id="departmentInactive" name="departmentInactive" style="position:relative; left: 109%;top: 5px;"></td>
									</tr>
								<!-- 	<tr>
										<td colspan="2">
											<label>Override: </label><input type="checkbox" value='1' id="overrideAccounts" name="overrideAccounts" onclick="overrideAccounts()">
											<input type="text" id="overrideInput" name="overrideAccountsInput" style="" disabled="disabled">
										</td>
									</tr> -->
									<tr>
										<td><label class="overrideFont">Revenue: </label></td>
										<td  style="display:none"><label id="revenueLabel"></label></td>
										<td><input type="text" id="revenueAccount" name="revenue" placeholder="Enter minimum 2 character to search" style="width: 140%;"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="position: relative;left: 88%;"></td>
										<td><input type="hidden" id="coAccountIdsales" name="coAccountIdsales"></td>
									</tr>
									<tr>
									<td style="display:none"><label id="cgsLabel" ></label></td>
										<td><label class="overrideFont">Cost of Goods Sold: </label></td>
										<td><input type="text" id="cgsAccount" name="cost"  placeholder="Enter minimum 2 character to search" style="width: 140%;"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="position: relative;left: 88%;"></td>
										<td><input type="hidden" id="coAccountIdcogs" name="coAccountIdcogs"></td>
										
									</tr>
									
								</table>
								</form>
								<div>
									<input type="button" onclick="clearDepartmentDetails()" value="Clear" class="savehoverbutton turbo-blue">
									<input type="button" onclick="deleteDepartmentDetails()" value="Delete" class="savehoverbutton turbo-blue">
									<input type="button" onclick="SaveDepartmentDetails()" value="Save" class="savehoverbutton turbo-blue" style="margin-left: 180px;width: 80px;">
								</div>
							</fieldset>
						</div>
					</td>
				</tr>
			</table> 
		</div>
		<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inv_departments.js"></script>
		
</body>
</html>