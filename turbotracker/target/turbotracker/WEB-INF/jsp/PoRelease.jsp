<style type="text/css">
	.textSpecial{ vertical-align: top; }
	#ui-datepicker-div{border: 1px solid #FF864E; }
	.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:150px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:145px;  _margin:0; _overflow-x:hidden; width: 100px; }
</style>
<div id="porelease">
	<div id="poreleasetab" style="width: 800px;">
		<ul>
			<li id="pogeneral"><a href="PORelease_General">General</a></li>
			<li id="polineitems"><a href="PORelease_Lines">Line Items</a></li>
			<li id="poacknowledgement"><a href="PORelease_Ack">Acknowledgement</a></li>
			<li id="poCustAck" style="float: right;"><input type="button" class="savehoverbutton turbo-tan" value="Cust Ack" onclick="viewPOAckPDF()" style="width:80px" id="custAckpoRelease"></li>
			<li id="poconfirm" style="float: right;"><input type="button" class="savehoverbutton turbo-tan" value="Confirm" onclick="viewPOGENPDF()" style="width:80px" id="confirmpoRelease"></li>
			<li style="float: right; display: none;">
	          		<label id="jobNumber_ID">${requestScope.jobNumber}</label>
				<label id="jobName_ID">${requestScope.jobName}</label>
				<label id="jobCustomerName_ID">${requestScope.jobCustomer}</label>
				<label id="Subtotal_ID"></label>
				<label id="Freight_ID"></label>
				<label id="Tax_ID"></label>
				<label id="Percentage_ID"></label>
				<label id="Total_ID"></label>
				<label id="vendor_ID"></label>
				<label id="poDate_ID"></label>
				<label id="order_ID"></label>
				<label id="manufacture_ID"></label>
				<label id="vePO_ID"></label>
	          	</li>
		</ul>
	</div>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PoRelease.js"></script>