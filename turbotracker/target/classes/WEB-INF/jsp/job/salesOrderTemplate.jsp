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
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<table style="width: 980px; margin: 0 auto;">
				<!-- <tr>
							<td align="center" style="" colspan="2">
								<h2  style="font-family: Verdana,Arial,sans-serif;"><label>Order Points</label></h2>
							</td>
					</tr> 
				-->
			<tr>
				<td style="padding-right: 5px;">
					<table id="salesOrderTemplateGrid"></table>
					<div id="salesOrderTemplateGridPager"></div>
				</td>
				<td>
				<table>
				<tr>
				<td><fieldset class= "custom_fieldset" style="height: 45px;width: 890px;">
							<legend class="custom_legend"><label><b>Template Description </b></label></legend>
								<input type="text" id="templateId" name="templateId" value="" style="width: 98%;"/><span style="color:red;">*</span>
								</fieldset></td>
				</tr>
				<tr><td><div id="errorMsg"></div></td></tr>
				<tr>
				<td>
				<div id="jqGrid">
				
					<table id="addSalesOrderTemplateGrid"></table>
					<div id="addSalesOrderTemplateGridPager"></div>
					</div>
				</td>
				</tr>
				<tr><td><input type="button" id="showhidePrice" value="Show Price" class="turbo-blue savehoverbutton" onclick="showPrice();" style="margin-left: 841px; font-size: 15px;"/></td></tr>
				<tr>
				<td><fieldset class= "custom_fieldset" style="height: 75px;width: 890px;">
							<legend class="custom_legend"><label><b>Totals </b></label></legend>
							<table style="width:100%">
							<tr id="priceTR" align="right">
								<td colspan="2"><label>Line item Whse Cost:</label></td><td >$<span id="whse"></span></td>
					       		<td><label>Total Order Cost:</label></td><td>$<span id="cost"></span></td>
					   			<td><label>Margin:</label></td><td>$<span id="margin"></span></td><td><span id="perc"></span>%</td>
							</tr>
   			<tr align="center">
	   			<td><label>Subtotal:</label></td><td><input type="text" style="width:122px" id="customerInvoice_subTotalIDLine" name="customerInvoice_subTotalName" value="0.00" disabled="disabled"></td>
	       		<td><label>Freight(Cost)</label></td><td><input type="text" style="width:122px" id="customerInvoice_frightID" onkeyup="setSOTempLinegridTotal()" name="customerInvoice_frightname"value="0.00"></td>
	   			<td><label>Tax:</label></td><td><input type="text" style="width:122px" id="customerInvoice_taxId" name="customerInvoice_taxName" onkeyup="setSOTempLinegridTotal()" value="${requestScope.taxRate}"></td><td>%</td>
	   			<td><label></label></td><td><input type="text" style="width:122px" id="customerInvoice_taxvalue" name="customerInvoice_taxName" value="0.00" disabled="disabled"></td>
	       		<td><label>Total:</label></td><td><input type="text" style="width:122px" id="customerInvoice_totalIDLine" name="customerInvoice_totalName" value="0.00" disabled="disabled"></td>
      	 	</tr>
 		 </table>
								
								</fieldset></td>
				</tr>
				</table>
				
				</td>
			</tr>
			<tr>
			<td><div style="display: none">
			<input>
			</div></td><td>
			<div id="showOrderPointsButtons" style="display: none;">
			<input type="hidden" id="prMasterId" name="prMasterId">
			<input type="hidden" id="cuSoid1" name="cuSoid1">			
			<table>
			<tr>
			<td align="left"><input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addSOTemplate();"/>
			</td>
			<td align=""><div id="showMessageSOTemplate" style="color: green;margin-left: 2000%;margin-bottom: 173%;"></div></td>
			<td align="right">
			<input type="button" class="turbo-blue savehoverbutton" value="CopyTemplate" onclick="CopySoTempLineItemNote()" style="margin-left:500px; font-size: 15px;display:none;">
			<input type="button" value="Save" class="turbo-blue savehoverbutton" onclick="saveSOTemplate();" style="margin-left: 30px; font-size: 15px;">
				<input type="button" value="Delete" class="turbo-blue savehoverbutton" onclick="deleteSOTemplate();" style="margin-left: 30px;font-size: 15px;">
			</td>
			</tr>			
			</table>				
			</div>
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
	<div id= "SoLineItemNote">
	<form action="" id="SoLineItemNoteForm">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="lineItemNoteID" name="lineItemNoteName" style="height: 252px; width:570px;"></textarea>
	   				<input id="lineItemNoteLabelID" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" id= "SaveInlineNoteID" value="Save" onclick="SaveSoTempLineItemNote()" style=" width:80px;display:inline-block;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="SoCancelTempInLineNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
	</form>
</div> 
	
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/salesOrderTemplate.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>