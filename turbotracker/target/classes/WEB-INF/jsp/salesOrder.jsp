
<style type="text/css">
	.textSpecial{ vertical-align: top; }
	#ui-datepicker-div{border: 1px solid #FF864E; }
	.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:150px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:145px;  _margin:0; _overflow-x:hidden; width: 100px; }
	.ui-tabs-nav {height: 32px;}
		#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
		#mainMenuJobsPage a span{color:#FFF}
	
</style>
<div id="salesrelease" class="jobListWizardDivColor">
 <div class="loadingDivEmailAttachment" id="loadingDivForSOGeneralTab" style="display: none;opacity: 0.7;background-color: #fff;z-index: 1234;text-align: center;"></div>
	<div id="salesreleasetab" style="width: 800px;">
		<ul>
			<li id="salesgeneral"><a href="SO_General" onclick="salesgeneralclick()" >General</a></li>
			<li id="saleslineitems"><a href="SO_Lines" onclick="saleslineitemsclick()">Line Items</a></li>
			<!-- <li id="salesacknowledgement"><a href="SO_Kit" onclick="salesacknowledgementclick()" >Kit</a></li> -->
			
			<li style="float: right; display: none;">
	          	<label id="jobNumber_ID">${requestScope.jobNumber}</label>
	          	<label id="joMaster_ID">${requestScope.joMasterID}</label>
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
	          	<li id="salesacknowledgement" style="position: absolute;right: 1%;top: 1%; background:white;"><input type="button" id="soStatusButton" value="Open" style="width: 90px;height: 25px;" onClick="callSalesOrderStatus();" /></li>
		</ul>
	</div>
	<input type="hidden" id="operation" name="operation"/>
  <jsp:include page="./Email_Attachment.jsp"></jsp:include>
  <jsp:include page="./inventoryDetails_SO.jsp"></jsp:include>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>

	<script type="text/javascript">
	bkLib.onDomLoaded(function() { 
		//nicEditors.allTextAreas(); 
		var myNicEditor = new nicEditor();
		myNicEditor.panelInstance(
	            document.getElementById('econt')
	        );
		/* myNicEditor.panelInstance(
	            document.getElementById('specialInstructionID')
	        ); */
		 });

	
			 
	 </script> 
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SalesRelease.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery-ui-1.8.23.custom.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
<script>
function salesgeneralclick(){
	 $('#loadingDivForSOGeneralTab').css({
			"display": "block"
		});
	 so_openornot=1; 
	soLineItemformChanges("TabChange");
	if( $('input:text[name=jobHeader_JobNumber_name]').length>0 )
		 createtpusage('job-Release Tab','SO Tab-General','Info','Job-Release Tab,View,SO Tab-General,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
}
function saleslineitemsclick(){
	 $('#loadingDivForSOGeneralTab').css({
			"display": "block"
		}); 
	 so_openornot=1; 
	soGeneralformChanges("TabChange");
	if( $('input:text[name=jobHeader_JobNumber_name]').length>0 )
	createtpusage('job-Release Tab','SO Tab-Line Items','Info','Job-Release Tab,View,SO Tab-Line Items,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
}
function salesacknowledgementclick(){
	if( $('input:text[name=jobHeader_JobNumber_name]').length>0 )
    createtpusage('job-Release Tab','SO Tab-Kit','Info','Job-Release Tab,View,SO Tab-Kit,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
}
</script>