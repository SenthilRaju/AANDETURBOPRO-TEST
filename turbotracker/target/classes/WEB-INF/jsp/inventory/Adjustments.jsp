<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TurboPro - Inventory Adjustments</title>
<style type="text/css">
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
		<table style="width: 980px; margin: 0 auto;">
				<tr>
							<td align="center" style="" colspan="2">
								<h2  style="font-family: Verdana,Arial,sans-serif;"><label>Adjustments</label></h2>
							</td>
					</tr> 
			<tr>
				<td style="padding-right: 5px;">
					<table id="chartsOfTransferInventoryGrid"></table>
					<div id="chartsOfTransferInventoryGridPager"></div>
				</td>
				
				<td style="padding-right: 5px;" colspan="2">
				<table><tr><td>
				<fieldset class= "custom_fieldset" style="height: 120px;width: 650px;">
						<legend class="custom_legend"><label><b>Transfer Information</b></label></legend>
						<table>
							<tr>
								<td style="width: 100px"><label>Date: </label></td>
								<td>  <input type="text" class="datepicker" id="transferDateID" name="transferDate" style="width:180px;height:25px;" value="${currentDate}" /> </td>
								
							</tr>
							<tr> 
								<td style="width: 100px"><label>Warehouse: </label></td>
								<td>
								<select id="warehouseListID" name="warehouseList" onchange="getCountInventoryGrid()" style="width: 250px; height: 25px;">
									<option value="0"> -Select- </option>														
								</select>		
							</td>
							</tr>
							<tr>
								<td style="width: 100px"><label>Reference: </label></td>
								<td><input type="text" id="referenceID" name="reference" style="width: 450px; height: 25px"/></td>
							</tr>
						</table>
				</fieldset>
								
				</td>
			</tr>
			<tr>
				<td style="padding-right: 5px;" colspan="2">
	
									<div id="jqGrid">
										<table id="chartsOfTransferListGrid"></table>
										<div id="chartsOfTransferListGridPager"></div>
									</div>		
				</td>
			</tr>
			<tr>
			<td align="right">
			<div id="showAdjustmentButtons">
				<input type="button" value="Save" class="turbo-blue savehoverbutton" onclick="SaveAdjustments()" style="margin-left: 10px; font-size: 15px;" />
				<input type="button" value="Save & Close" class="turbo-blue savehoverbutton" onclick="SaveCloseAdjustments()" style="margin-left: 10px;font-size: 15px;"/>
			</div>
			</td>
			</tr>
		</table>
</td>
</tr>
</table>
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
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/Adjustments.js"></script>


</body>
</html>