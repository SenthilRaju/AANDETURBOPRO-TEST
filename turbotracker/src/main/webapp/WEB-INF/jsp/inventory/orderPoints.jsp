<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Order Points</title>
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

#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
#mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
#mainmenuInventoryPage ul li a{background: none; }
</style>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<input id="suggestedButton" type="button" value="Suggested Reorder" class="turbo-blue savehoverbutton" style="margin-right: 120px; font-size: 15px;float:right"/>
		<br><br>
		<table style="width: 980px; margin: 0 auto;">
				<!-- <tr>
							<td align="center" style="" colspan="2">
								<h2  style="font-family: Verdana,Arial,sans-serif;"><label>Order Points</label></h2>
							</td>
					</tr> 
				-->
			<tr>
				<td style="padding-right: 5px;">
					<table id="chartsOfWarehouseGrid"></table>
					<div id="chartsOfWarehouseGridPager"></div>
				</td>
				<td>
				<div id="jqGrid">
					<table id="chartsOfOrderPointsGrid"></table>
					<div id="chartsOfOrderPointsGridPager"></div>
					</div>
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 			<td><div style="display: none"> -->
<!-- 			<input> -->
<!-- 			</div></td><td align="right"> -->
<!-- 			<div id="showOrderPointsButtons" style="display: none;"> -->
<!-- 				<input type="button" value="Save" class="turbo-blue savehoverbutton" onclick="SaveOrderPoints()" style="margin-left: 10px; font-size: 15px;" /> -->
<!-- 				<input type="button" value="Save & Close" class="turbo-blue savehoverbutton" onclick="SaveCloseOrderPoints()" style="margin-left: 10px;font-size: 15px;"/> -->
<!-- 			</div> -->
<!-- 			</td> -->
<!-- 			</tr> -->
		</table>

		<div id="suggestedReOrderPopUpDialog" style="display: none;">
			<table id='suggestedReOrderPopUpDialogBox'></table>
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
	</div>
	<div style="display: none;">
		<input type="text" id="chartsAccID" name="chartsAccName"
			value="${requestScope.userDetails.chartsperpage}">
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/orderPoints.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>