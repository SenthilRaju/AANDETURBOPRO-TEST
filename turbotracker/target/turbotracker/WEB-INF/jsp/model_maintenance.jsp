<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Model Maintenance</title>
<style type="text/css">
th.ui-th-column div {
    word-wrap: break-word; /* IE 5.5+ and CSS3 */
    white-space: pre-wrap; /* CSS3 */
    white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
    white-space: -pre-wrap; /* Opera 4-6 */
    white-space: -o-pre-wrap; /* Opera 7 */
    overflow: hidden;
    height: auto !important;
    vertical-align: middle;
}
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
		<table style="width: 979px; margin: 0 auto;">
			<tr>
			<td  style="width:200px"></td>
				<td>
				
					<table id="scheduleGrid"></table>
					<div id="schedulePager"></div>
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

	
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/modeldescription.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>