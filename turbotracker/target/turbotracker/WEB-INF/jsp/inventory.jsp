<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Inventory List</title>
	<style type="text/css">
		input#customerNameHeader {width:400px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
		input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
		.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
		.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
		#add { display: none; }
		/* Select the header Icon */
			#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
            #mainmenuInventoryPage:first-child {background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
	</style>
</head>
<body>
	<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		<!--<jsp:include page="addNewCustomer.jsp"></jsp:include>-->
		<table style="width:600px;margin:0 auto;">
			<tr>
				<td align="right">
					<table>
				    	<tr><td> 
							<input type="button" value="  Add" class="add" id="addCustomersButton" onclick="addNewInventory()"> 
						</td></tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="Inventory" style="padding-left: 0px;">
						<table style="padding-left:0px" id="inventoryGrid" class="scroll"></table>
						<div id="inventoryGridpager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				</td>
			</tr>
		</table>
		<div style="height: 20px;"></div>
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<div style="display: none;">
		<input type="text" id="inventoryID" value="${requestScope.userDetails.customerperpage}">
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inventory.js"></script>
</body>
</html>