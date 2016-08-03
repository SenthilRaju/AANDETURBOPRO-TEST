<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - CustomerDetails</title>
		<style type="text/css">
			input#customerNameHeader {width:250px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
			input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{ background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		</style>
		
	</head>
	<body>
		<div  style="background-color: #FAFAFA">
		<div>
			 <jsp:include page="./headermenu.jsp"></jsp:include> 
		</div>
<div id="general_journal">
	<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA">
		<ul>
			<li><a href="customer/customerContacts">Contacts</a></li>
			<li><a href="rolodex/rolodexjournal">Journal</a></li>
			<li id="customer"><a href="rxdetailedviewtabs/rolodexCustomer">Financial</a></li><!-- Customer Financial Information -->
			<li id="employee"><a href="employe">Employee</a></li>
			<li id="vendor"><a href="#vendortab">Vendor</a></li>
			<li id="engineer"><a href="rxdetailedviewtabs/engineer">Engineer</a></li>
			<li id="architect"><a href="rxdetailedviewtabs/architect">Architect</a></li>
			<li style="float: right; padding-right: 10px; padding-top:3px;">
				<label id="customerName" style="color: white;vertical-align: middle;">${requestScope.name}</label>
				<label id="phoneCenter" style="color: white;vertical-align: middle;"></label><label id="phoneName" style="color: white;vertical-align: middle;"></label><label id="phone_secondField" style="color: white;vertical-align: middle;"></label>
				<label id="CustomerId" style="display: none;">${requestScope.rolodexNumber}</label>
				<label id="customerAddress1"></label>
				<label id="customerAddress2"></label>
				<label id="customerState"></label>
				<label id="customerCity"></label>
				<label id="customer"></label>
			</li>
		</ul>
		<jsp:include page="vendor.jsp"></jsp:include>
	</div>
</div>
<div style="padding-top: 26px; background-color: #FAFAFA">
	<table id="footer">
	<tr>
		<td colspan="2">
			<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
		</td>
	</tr>
	</table>
</div>
</div>
<div style="display: none;">
	<input type="text" id="isCategory1" value="${requestScope.rxMasterDetails.isCategory1}">
	<input type="text" id="isCategory2" value="${requestScope.rxMasterDetails.isCategory2}">
	<input type="text" id="isCategory3" value="${requestScope.rxMasterDetails.isCategory3}">
	<input type="text" id="isCustomer" value="${requestScope.rxMasterDetails.isCustomer}">
	<input type="text" id="isVendor" value="${requestScope.rxMasterDetails.isVendor}">
	<input type="text" id="isEmployee" value="${requestScope.rxMasterDetails.isEmployee}">
	<input type="text" id="lableName" value="${requestScope.name}">
	<input type="text" id="labelPhone" value="${requestScope.phone}">
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/generalCustomers.js"></script> 
</body>
</html>