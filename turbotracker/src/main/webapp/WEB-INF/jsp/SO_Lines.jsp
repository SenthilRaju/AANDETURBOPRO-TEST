<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="soreleaselineitem">
<input type="hidden" name="QuotedPricePrMasterID" id="QuotedPricePrMasterID" value="${requestScope.QuotedPricePrMasterID}" />
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
	<div id="SOlineItemPager"></div>
	<fieldset class= "custom_fieldset" style="height: 55px;width: 730px;">
	<legend class="custom_legend"><label><b>Price Details</b></label></legend>
	<table>
		<tr>
			<td>
				 <input type="button" id="SOReleaseSuggestedPriceID" class="cancelhoverbutton turbo-tan" value="Suggested Price" onclick="suggestedorQuotePrice('SuggestedPrice')" style="width:185px;position: relative;margin-left:16px; ">
			</td>
			<td>
				 <input type="button" id="SOReleaseQuotedPriceID" class="cancelhoverbutton turbo-tan" value="Quoted Price" onclick="suggestedorQuotePrice('QuotedPrice')" style="width:185px;position: relative;margin-left:16px; ">
			</td>
		</tr>
	</table>
	</fieldset>
	<fieldset class= "custom_fieldset" style="width:729px">
	<legend class="custom_legend"><label><b>Totals</b></label></legend>
	<table>
   		<tr id="costdetails" align="center" >
	   			<td  style="width:200px"><label>Line Item Whse cost:</label></td><td><input type="text" style="width:85px" id="salesorder_whsecost" ></td>
	       		<td  style="width:200px"><label >Total Order Cost:</label></td><td><input type="text" style="width:85px" id="salesorder_ordercost" value="$0.00" readonly="readonly"></td>
	   			<td  style="width:120px"> <label>Margin:</label></td><td><input type="text" style="width:85px" id="salesorder_margintotal" readonly="readonly"></td>
	   		</tr>
	   		</table>
   		<table style="width:700px">
   		
   			<tr align="center">
	   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="customerInvoice_subTotalIDLine" name="customerInvoice_subTotalName" disabled="disabled"></td>
	       		<td><label>Freight:</label></td><td><input type="text" style="width:85px" id="customerInvoice_frightID" name="customerInvoice_frightname"value="$0.00" disabled="disabled"></td>
	   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxId" name="customerInvoice_taxName" value="${requestScope.taxRate}" disabled="disabled"></td><td>%</td>
	   			<td><label></label></td><td><input type="text" style="width:85px" id="customerInvoice_taxvalue" name="customerInvoice_taxName" value="0.00" disabled="disabled"></td>
	       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="customerInvoice_totalIDLine" name="customerInvoice_totalName" disabled="disabled"></td>
      	 	</tr>
 		 </table>
</fieldset>
<table>
	<tr>
		<td>&nbsp;</td>
		<td><div id="showMessageLineSO" style="color: green;margin-left: 1498%;margin-bottom: 0%;"></div></td>
	</tr>
	<tr style="width: 100%;">
			<td align="left" style="padding-right: 0px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Sales Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
			<td align="left" style="padding-right: 0px;"><input id="contactEmailID_lines" type="image" src="./../resources/Icons/mail_new.png" onmouseover="triggertitle()"  title="Email Sales Order" style="background: #EEDEBC;" onclick="sendPOEmail('SOJ')"></td>
			<td align="left" style="padding-left: 10px;"><input id="withPriceLine" type="checkbox" style="background: #EEDEBC;margin-left: 5px;" onclick="line(this);"></td>
			<td align="left" style="padding-right: 0px;" width="30%"><label id="withPriceLineLabel" style="">With&nbsp;Price</label></td>
			<td width="40%"><font color="green" size="3"><div id="soLinesEmailTimeStamp"></div></font> </td>
			<td><input type="button" id="SaveLineSOReleaseID" class="cancelhoverbutton turbo-tan" value="Save" onclick="saveLineDetails()" style="width:60px;position: relative;"></td>
			<td><input type="button" id="closeLineSOReleaseID" class="cancelhoverbutton turbo-tan" value="Close" onclick="closeSOLineItemTab()" style="width:120px;position: relative;"></td>
			<td><input type="text" id="setButtonValue" style="display: none;" value=""/></td>
	</tr>
</table>


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
	   				<input type="button" class="savehoverbutton turbo-tan" id= "SaveInlineNoteID" value="Save" onclick="SaveSoLineItemNote()" style=" width:80px;display:inline-block;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="SoCancelInLineNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
	</form>
</div> 
<script type="text/javascript">
var id = '';
$('input[type="button"]').click(function(e){
   id = e.target.id;
   if("SavePOReleaseID" == id)
   {
   	$('#setButtonValue').val("Save");
   }
   if("POReleaseID" == id)
   {
   	$('#setButtonValue').val("SaveandClose");
   }
   
});

</script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SO_Lines.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
