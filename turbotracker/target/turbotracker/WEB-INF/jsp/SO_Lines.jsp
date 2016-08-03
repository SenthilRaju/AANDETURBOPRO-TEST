<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="soreleaselineitem">
	<table>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
							<legend class="custom_legend"><label><b>Customer </b></label></legend>
								<label>Customer Name :</label> <input type="text" disabled="disabled" id="CustomerNameLine" value="" />
								</fieldset>
						</td>
						<td>
						<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
						<legend class="custom_legend"><label><b>SO Details</b></label></legend>
							<label>Date:</label><input type="text" id="dateOfcustomerLine" style="width: 85px; margin-left: 5px;" value="" disabled="disabled"/>
							<label>Number</label><input type="text" disabled="disabled" id="SOnumberLine" style="width: 126px; margin-left: 5px;" value=""/>
						</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table id="SOlineItemGrid"></table>
	<div id="lineItemPager"></div>
	<fieldset class= "custom_fieldset" style="height: 55px;width: 730px;">
	<legend class="custom_legend"><label><b>Price Details</b></label></legend>
	<table>
		<tr>
			<td>
				 <input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Suggested Price" onclick="" style="width:185px;position: relative;margin-left:16px; ">
			</td>
			<td>
				 <input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Quoted Price" onclick="" style="width:185px;position: relative;margin-left:16px; ">
			</td>
		</tr>
	</table>
	</fieldset>
	<fieldset class= "custom_fieldset" style="width:729px">
	<legend class="custom_legend"><label><b>Totals</b></label></legend>
   		<table style="width:700px">
   			<tr align="center">
	   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="customerInvoice_subTotalIDLine" name="customerInvoice_subTotalName" disabled="disabled"></td>
	       		<td><label>Freight:</label></td><td><input type="text" style="width:85px" id="customerInvoice_frightID" name="customerInvoice_frightname"value="$0.00" disabled="disabled"></td>
	   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxId" name="customerInvoice_taxName" value="${requestScope.taxRate}" disabled="disabled">%</td>
	   			<td><label></label></td><td><input type="text" style="width:85px" id="customerInvoice_taxvalue" name="customerInvoice_taxName" value="0.00" disabled="disabled"></td>
	       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="customerInvoice_totalIDLine" name="customerInvoice_totalName" disabled="disabled"></td>
      	 	</tr>
 		 </table>
</fieldset>
<table>
	<tr>
		<td>
			<input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="saveLineDetails()" style="width:120px; top: 8px;position: relative;float: right;left: 625px;">
		</td>
	</tr>
</table>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SO_Lines.js"></script>
