<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Trial Balance </title>
	<style type="text/css">
input[type='checkbox'] {
	margin-left: 0px;
	margin-right: 11px;
}

.accountRangeInput {
	width: 80px;
}

.accountRangeInputID {
	display: none;
}

#mainMenuCompanyPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #003961;
}

#mainMenuCompanyPage a {
	background:
		url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png')
		no-repeat 0px 4px;
	color: #FFF
}

#mainMenuCompanyPage ul li a {
	background: none;
}
</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<div style="width:600px;margin:0 auto;margin-top: 10px; text-align:center">
						<label style="font-size:30px;">Trial Balance</label><br>
						<label style="font-size:20px;">Period Ending <span id="perioddateEnding"></span></label>
					</div>
				</tr>
				<tr>
					<td colspan="2">
						<table id="triaBalanceGrid"></table>
						<div id="trialBalanceGridPager"></div>
					</td>
				</tr>
				<tr>
				<td align="right">
						<div>
							<input  type="button"  class = "savehoverbutton turbo-blue" value="Print" alt="Add new Job" style="width:80px;" onclick="trialBalancePrint()"/>&nbsp;
						</div>
				</td>
				</tr>
			</table>
			<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/trialBalanceView.js"></script>
	</body>
</html>

