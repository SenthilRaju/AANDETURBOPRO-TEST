<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
			#mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuInventoryPage ul li a{background: none; }
	</style>
</head>
<body>

	<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		<!--<jsp:include page="addNewCustomer.jsp"></jsp:include>-->
		<table style="width:600px;margin:0 auto;">
		<tr >
		<td align="left">
		<label>Warehouse:</label><select id="bankAccountsID" name="warehouse" style="width: 180px;" onchange="ShowInventoryList()">
										
 										<option value="-1">All</option>
											<c:forEach var="warehouse" items="${requestScope.warehouse}">
												<option value="${warehouse.prWarehouseId}">
														<c:out value="${warehouse.searchName}"></c:out>
												</option>
										</c:forEach>
								</select>
		</td>
		
				<td align="right">
					<table>
				    	<tr>
				    	<td>
				    	<img alt="Export Csv" src="./../resources/images/csv_text.png" width="25" onclick="exportInventoryList()">
<!-- 				    	<Label>Show Inactive:</Label><input type="checkbox" id="inactivelist" onchange="ShowInactiveList()"/> -->
				    	</td>
				    	<td> 
<!-- 							<input type="button" value="  Add" class="add" id="addCustomersButton" onclick="addNewInventory()">  -->
						</td></tr>
					</table>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="Inventory" style="padding-left: 0px;">
						<table style="padding-left:0px" id="inventoryValueGrid" class="scroll"></table>
						<div id="inventoryValueGridpager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 			<td colspan="2"> -->
<!-- 				<fieldset  class= " ui-widget-content ui-corner-all" style="width: 1100px;height: 260px;"> -->
<!-- 									<legend><label><b>On Hand Details</b></label></legend> -->
<!-- 					<table id="OnHandDetailsGrid"></table> -->
<!-- 					<div id="OnHandDetailsPager"></div> -->
<!-- 					<table> -->
<!-- 					<tr>						
<!-- 						<td><input type="button" id='showpricetier' name='showpricetier' value="Resume Tier Pricing" onclick="showPriceTier();"> </td> -->
<!-- 					</tr> -->
<!-- 					</table> -->
<!-- 					</fieldset> -->
				
<!-- 				</td> -->
<!-- 			</tr> -->
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
		<input type="text" id="inventoryID" value="${requestScope.userDetails.inventoryperpage}">
		<input type="text" id="key" value="${requestScope.key}">
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inventoryValue.js"></script>
</body>
</html>