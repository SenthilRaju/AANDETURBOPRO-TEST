<div id="poacknowledgement">
	<table style="width: 600px">
	<tr>
		<td>
			<fieldset style="width: 727px" class=" ui-widget-content ui-corner-all">
				<table>
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorAckNameId" name="vendorAckName" style="width:200px" disabled="disabled"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>
						<td><input type="text" class="datepicker" id="poDateAckId" name="poDateAckName" value="" style="width: 90px;" disabled="disabled"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 90px" id="ourPoAckId" name="ourPoAckName" value="" disabled="disabled"></td>
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
	<table id="Ack"></table>
	<div id="Ackpager"></div>
	<br>
	<table width="750px">
		<tr>
			<td align="left">
				<input type="button" id="saveAllButton" class="savehoverbutton turbo-tan" value="All" onclick="saveAll()" style="width: 80px;">
			</td>
			<td align="right">
				<a onclick="" style="font: bold 14px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;"><label><b>
				<u>Apply To Rest of Order</u></b></label></a>
			</td>
		</tr>
	</table><br>
	<table>
		<tr>
			<td>
				<fieldset class= "custom_fieldset" style="height: 50px;width: 710px;">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
					<table style="width: 735px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalKnowledgeId" name="subtotalKnowledgeName" disabled="disabled"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightKnowledgeId" name="freightKnowledgeName" disabled="disabled"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxKnowledgeId" name="taxKnowledgeName" disabled="disabled"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="KnowledgeID" name="KnowledgeName" style="width: 60px;text-align:right;" disabled="disabled"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalKnowledgeId" name="totalKnowledgeName" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<hr>
<table  style="width: 750px;">
	<tr>
		<td style="width: 60px;padding: 0px 1px; display: none">
	  		<fieldset class= " custom_fieldset" style="padding-bottom: 0px; ">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOAckPDF()"  style="background: #EEDEBC; display: none;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOAckEmail()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
		<td align="right" style="padding-left: 490px;">
			<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="cancelPORelease()" style="width:120px" id="cancelpoRelease">
		</td>
	</tr>
</table>
</div>
<div id="saveAllDialog">
	<table>
		<tr id="ackDateTR">
			<td style="width: 60px;"><label>Ack.: </label></td>
			<td><input type="text" id="ackDateTD" size="15"></td>
		</tr>
		<tr id="shipDateTR">
			<td style="width: 60px;"><label>Ship: </label></td>
			<td><input type="text" id="shipDateTD" size="15"></td>
		</tr>
		<tr id="orderNumberTR">
			<td style="width: 60px;"><label>Order #: </label></td>
			<td><input type="text" id="orderNumberTD" size="15"></td>
		</tr>
	</table>
	<hr>
</div>
<div class="loadingDiv" id="loadingPOAckDiv"> </div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PORelease_Ack.js"></script>