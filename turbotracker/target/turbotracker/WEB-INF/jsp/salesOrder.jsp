<style type="text/css">
	.textSpecial{ vertical-align: top; }
	#ui-datepicker-div{border: 1px solid #FF864E; }
	.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:150px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:145px;  _margin:0; _overflow-x:hidden; width: 100px; }
</style>
<div id="salesrelease">
	<div id="salesreleasetab" style="width: 800px;">
		<ul>
			<li id="salesgeneral"><a href="SO_General">General</a></li>
			<li id="saleslineitems"><a href="SO_Lines">Line Items</a></li>
			<li id="salesacknowledgement"><a href="SO_Kit">Kit</a></li>
			<li style="float: right; display: none;">
	          		<label id="jobNumber_ID">${requestScope.jobNumber}</label>
				<label id="jobName_ID">${requestScope.jobName}</label>
				<label id="jobCustomerName_ID">${requestScope.jobCustomer}</label>
				<label id="Subtotal_ID"></label>
				<label id="Freight_ID"></label>
				<label id="cuInvoiceID"></label>
				<label id="Percentage_ID"></label>
				<label id="Total_ID"></label>
				<label id="prWareHouse_ID"></label>
				<label id="poDate_ID"></label>
				<label id="order_ID"></label>
				<label id="Jomasterid"></label>
				<label id="vePO_ID"></label>
				<label id = "Cuso_ID"></label>
				<label id ="rxmasterId"></label>
	          	</li>
		</ul>
	</div>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SalesRelease.js"></script>
