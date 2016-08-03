<div id="polineitems">
	<table style="width: 600px">
	<tr>
		<td>
			<fieldset style="width: 727px" class=" ui-widget-content ui-corner-all">
				<table>
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorLineNameId" name="vendorLineName" style="width:200px" disabled="disabled"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>
						<td><input type="text" class="datepicker" id="poDateLineId" name="poDateLineName" value="" style="width: 90px;" disabled="disabled"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 150px" id="ourPoLineId" name="ourPoLineName" value="" disabled="disabled"></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
	<table>
		<tr>
			<td><hr width="750px"></td>
		</tr>
	</table>
	<div id="lineGrid" style="width:770px;overflow:auto;">
	<table id="lineItemGrid"></table>
	<div id="lineItemPager"></div>
	</div>
	<br>
	<div>
		<table>
			<tr>
			
			<td >
					<input type="button" class="turbo-tan savehoverbutton" id="importFromXML" onclick="showXmlUploadForm()" value="Import from XML">
						<div id="uploadXml_Form" style="display: none; border: 3px 0px #FFFFFF solid">
		<form id="XmlUploadID" method="post" action="" enctype="multipart/form-data">
			<!-- File input -->    
			<input id="xmlfileid" name="xmlfilename" type="file" />	<input name="vepoId1" style="display: none;" id="vepoId1" type="text" value="" />
			<input name="MANId1" style="display: none;" id="MANId1" type="text" value="" />
		</form>
		<button value="Submit" class="turbo-tan savehoverbutton" onclick="savexmlfunction()" >Upload</button>
		<input type="button" id="cancelFormSubmit" class="turbo-tan savehoverbutton" value="Cancel" onclick="cancelXmlFormSubmit()"> <br/>
	</div>
				</td>
				<td>
					<input type="button" class="turbo-tan savehoverbutton" onclick="showUploadForm()" value="Import from Excel" style="display: none;">
					<input type="button" class="turbo-tan savehoverbutton excelDownload" onclick="downloadExcel()" value="Export to Excel" style="display: none;">
				</td>
				<td style="padding-left: 7px;display:none;">
					<fieldset style="padding-bottom: 0px;padding-bottom: 0px; margin-top: 0px; box-shadow: 0px 1px 5px 3px rgb(170, 170, 170);" class="custom_fieldset" >
						<input type="image" onclick="enableTaxAllRecords()" src="./../resources/Icons/tp_select_all_v2.png" title="Tax All">
					</fieldset>
				</td>
				
			</tr>

		</table>
	</div>
	<div id="uploadExcel_Form" style="display: none; border: 3px 0px #FFFFFF solid">
		<form id="form2" method="post" action="" enctype="multipart/form-data">
			<!-- File input -->    
			<input name="file2" id="file2" type="file" />	<input name="vepoId" style="display: none;" id="vepoId" type="text" value="" />
		</form>
		<button value="Submit" class="turbo-tan savehoverbutton" onclick="uploadJqueryForm()" >Upload</button>
		<input type="button" id="cancelFormSubmit" class="turbo-tan savehoverbutton" value="Cancel" onclick="cancelFormSubmit()"> <br/>
	</div>

<!-- 	<div id="uploadXml_Form" >
		<form enctype="multipart/form-data">        
            <input  id="xmlfileid" name="xmlfilename" type="file" />
            <input id="uploadfile" type="button" value="Upload" onclick="savexmlfunction()" /> 
 </form>
	</div> -->
	<div id="result"></div>
	<div id="result1"></div>
	<table align="left" style="display: none;">
		<tr>
			<td><input type="checkbox" id="gridShowLineItem" onclick="show()" style="vertical-align: middle;"></td>
			<td><a style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Show Invoiced/Received</b></a>
			</td>
			<td><input type="checkbox" id="lineitemcheck" onclick="lineitem()" style="vertical-align: middle;"></td>
			<td><a onclick="lineitem()" id="showLineItem" style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Different Estimated Ship For Line Item</b>
					</a>&nbsp;&nbsp;<input type="text" style="width: 100px;vertical-align: middle;" id="lineitemdate" class="datepicker" >
			</td>
		</tr>
	</table><br>
	<table>
		<tr>
			<td>
				<fieldset class= "custom_fieldset" style="height: 50px;width: 710px;">
				<legend class="custom_legend"><label><b>Totals</b></label></legend>
					<table style="width: 735px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative; "><input type="text" style="width: 75px; text-align:right" id="subtotalLineId" name="subtotalLineName" disabled="disabled"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightLineId" name="freightLineName" disabled="disabled"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxLineId" name="taxLineName" disabled="disabled"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="lineID" name="lineName" style="width: 60px;text-align:right;" disabled="disabled"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalLineId" name="totalLineName" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<hr>
<table  style="width: 750px;">

	<tr>
	<td style="width: 60px;padding: 0px 1px;">
	  		<fieldset class= " custom_fieldset" style="padding-bottom: 0px;vertical-align: middle;">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
				  		<!-- <td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOEmail('poGeneral')"></td> -->
		    			<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="outsidepoEmailButtonAction()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStampLines" style="color: green;"></label></td>
		<td align="right" width="80px">
		<div id="ShowInfo" style="width: 380px;height:20px;color: green;"></div>
		<input id="Buttonchoosed" type="checkbox" style="display: none;"/>
		<input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Save" onclick="SaveLinesPurchaseOrder()" style="width:90px;" id="SaveLinesPOButton">
		<input type="button" class="savehoverbutton turbo-tan" value="Close" onclick="closePurchaseOrderLineItemTab()" style="width:120px" id="CancelLinesPOButton">
		</td>
	</tr>
</table>
</div>
<div id= "POInLineItem">
	<form action="" id="POInLineItemID">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea  id="inlineItemId" name="inlineItemName" ></textarea>
	   				<input id="inlineItemLableId" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" id="PoSaveInLineItemID" value="Save" onclick="saveLineItemNote()" style=" width:80px;display:inline-block;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="POCancelInLineNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
	</form>
</div> 
<!-- <div class="loadingDiv" id="loadingPOlineDiv" > </div> -->
<script type="text/javascript">
$('.IsButtonClicked').click(function(){
	$('#Buttonchoosed').attr('checked','checked');
});

</script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.form.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PORelease_Lines.js"></script>
