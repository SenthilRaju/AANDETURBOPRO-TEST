<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Inventory Details</title>
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
            #mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
			.reset_button {
				background: url('./../resources/Icons/reset.png') no-repeat scroll 4px 4px/* , url('./../resources/styles/turbo-css/images/turbopro-blue-gradient-btn.png') repeat-x scroll 0 0 transparent */; 
				border: 0 none; 
				border-radius: 5px 5px 5px 5px; 
				cursor: pointer; 
				height: auto; 
				padding: 4px 5px;
				width: 20px;
			}
			.resetAvgCostGoClass {
				
			}
			.resetAvgCostCancelClass {
				
			}
		</style>
	</head>
	<body>
	<div  style="background-color: #FAFAFA">
	<div>
		 <jsp:include page="./../headermenu.jsp"></jsp:include> 
	</div>
	<div id="vendorInvoicedetailsDiv">
		<jsp:include page="./../vendorinvoicepage.jsp"></jsp:include>
	</div>
	<div style="padding-top: 26px; background-color: #FAFAFA">
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
			</td>
		</tr>
		</table>
	</div>
	</div>

	<script type="text/javascript">
		jQuery(document).ready(function() {
			
		 });
	</script>
</body>
</html>