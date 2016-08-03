<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
	<form id="submittalForm">
	<table>
		<tr><td colspan="2"><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
		<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<table>
		<tr>
			<td><div><table><tr><td><div><table id="submittalList"></table><div id="submittalListPager"></div></div></td></tr></table></div></td>
		</tr>
	</table>
	<div id="show_SubmittalSchedulesDiv" style="display: none; border: 1px solid Green; border-radius: 11px; width: 1080px; padding-left: 7px; margin-left: 11px; margin-bottom: 5px; padding-bottom: 12px;">
		<div style="padding-right: 20px; padding-top: 8px;" align="right">
			<label style="border: 1px solid Brown; border-radius: 5px; padding-left: 5px; padding-right: 5px; width: 54px;">
				<a onclick="hideScheduleGrid()">Close</a>
			</label>
		</div>
		<fieldset style="" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Schedule</b></label></legend>
			<ul>
				<li style="float: right;display: none;">
					<label id="jobNumber_ID">${requestScope.jobNumber}</label>
					<label id="jobName_ID">${requestScope.jobName}</label>
					<label id="jobCustomerName_ID">${requestScope.jobCustomer}</label>
					<label id="detailsHeader_ID"></label>
					<label id="productName_ID"></label>
				</li>
				<!-- <li id="submittalschedule"><a href="submittal_Schedule">Schedule</a></li> -->
			</ul>
			<table id="scheduleGrid"></table>
			<div id="scheduleGridpager"></div>
				
		</fieldset>
	</div>
	<table>
		<tr>
			<td valign="top" style="vertical-align:top; width:250px"><label>Total Estimated Cost:&nbsp;</label>
			<label id="totalCost" style="font-weight: bold;color: Green;"></label><br><br>
			<!-- <label>[</label>
			<img src='./../resources/images/generalIcon.png' title='General Form' align='middle' style='padding: 2px 2px;vertical-align: middle;'></a>
			<label>]</label> -->
			</td>
			<td style="display: none;">
				<input type="text" id="submittalMainHeaderID" value="${requestScope.joSubmittalDetails.joSubmittalHeaderId}" style="width: 60px;">
				<input type="text" id="quoteNumberID" value="${requestScope.joMasterDetails.quoteNumber}" style="width: 60px;">
				<input type="text" id="submittalNameID" value="${requestScope.submittalName}" style="width: 60px;">
				<input type="text" id="submittalSentID" value="${requestScope.joMasterDetails.submittalSent}" style="width: 60px;">
				<input type="text" id="submittalApprovedID" value="${requestScope.joMasterDetails.submittalApproved}" style="width: 60px;">
				<input type="text" id="submittalResentID" value="${requestScope.joMasterDetails.submittalResent}" style="width: 60px;">
				<input type="text" id="submittalSentDateID" value="${requestScope.joMasterDetails.submittalSentDate}" style="width: 60px;">
				<input type="text" id="submittalApproveDateID" value="${requestScope.joMasterDetails.submittalApprovedDate}" style="width: 60px;">
				<input type="text" id="submittalResentDateID" value="${requestScope.joMasterDetails.submittalResentDate}" style="width: 60px;">
				<input type="text" id="submittalMnaualQtyID" value="${requestScope.joMasterDetails.manualQty}" style="width: 60px;">
				<input type="text" id="submittalManualSentID" value="${requestScope.joMasterDetails.manualSent}" style="width: 60px;">
				<input type="text" id="submittalReuestManualID" value="${requestScope.joMasterDetails.requestManual}" style="width: 60px;">
				<input type="text" id="submittalManualDateID" value="${requestScope.joMasterDetails.manualDate}" style="width: 60px;">
				<input type="text" id="submittalReaquestDateID" value="${requestScope.joMasterDetails.requestDate}" style="width: 60px;">
				<input type="text" id="submittalCreditStatusID" value="${requestScope.joMasterDetails.creditStatus}" style="width: 60px;">
				<input type="text" id="submittalCreditStatusHoldID" value="${requestScope.cuMasterDetails.creditHold}" style="width: 60px;">
			</td>
			<td width="20px"></td>
			<td valign="top" style="vertical-align:top;">
				<div id="">
				<fieldset style="width:300px" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Submittal</b></label></legend>
					<table>
						<tr><td style="vertical-align:bottom;"><label>Sent</label></td><td><input type="checkbox" id="sent_ID" name="sent_name" onclick="sent()"  style="vertical-align:middle;"/></td>
							<td><input type="text" class="datepicker" name="sentdate_date" id="sentdate" value="" style="width:95px"></td></tr>
						<tr><td style="vertical-align:bottom;"><label>Resent</label></td><td><input type="checkbox" id="resent_ID" name="resent_name" onclick="resent()" style="vertical-align:middle;"/></td>
							<td><input type="text" class="datepicker" name="resentdate_date" id="resentdate" value="" style="width:95px"></td></tr>
						<tr><td style="vertical-align:bottom;"><label>Approved</label></td><td><input type="checkbox" id="approved_ID" name="approved_name" onclick="approved()"style="vertical-align:middle;"/></td>
							<td><input type="text" class="datepicker" name="approveddate_name" id="approveddate" style="width:95px"></td></tr>
					</table>
				</fieldset>
				</div>
			</td>
			<td width="50px;"></td>
			<td valign="top" style="vertical-align:top">
				<div id="">
				<fieldset style="width:440px" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>O & M Manuals</b></label></legend>
					<table>
						<tr>
							<td><label>Quantity:</label></td><td><input type="text" name="manualQty_name" style="width: 50px;" id="manualQtyId"/> &nbsp;&nbsp;</td>
						</tr>
						<tr>
							<td style="vertical-align:bottom;"><label>Requested</label></td><td><input type="checkbox" id="requested_ID" name="requested_name" onclick="requested()" style="vertical-align:middle;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" class="datepicker" name="date" id="requesteddate" value="" style="width:95px">
							</td>
						</tr>
						<tr>
							<td style="vertical-align:bottom;"><label>Sent</label></td>
							<td><input type="checkbox" id="OMsent_ID" onclick="OMsent()"style="vertical-align:middle;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<input type="text" class="datepicker" name="date" id="OMsentdate" value="" style="width:95px">
							</td>
						</tr>
					</table>
				</fieldset>
				</div>
			</td>
		</tr>
	</table>
	<br>
	<hr style="width:1100px; color:#C2A472;">
	<table style="width:1115px" id="turbopro-button">
		<tr>
			<td colspan="2" align="center"><div id="quoteSubmittal" style="display: none"></div></td>
		</tr>
		<tr>
			<td>
			 	<input  type="button" class="general turbo-tan" value="Launch Submittal" onclick='submittaldialog()'/>  
				<input type="button" value="PO Release" class="porelease turbo-tan" id="po" onclick="poreleasedialog()" /> 
				<input type="button" value="SO Release" class="sorelease turbo-tan"id="sales" onclick="soreleasedialog()"/>
				<input type="button" value="Print Cover" class="sorelease turbo-tan"id="cover" onclick="printSubmittalCover()"/>
			</td> 
		    <td align="right">
			   <input type="button" value="Save" class="savehoverbutton turbo-tan" onclick="submittal_save()" style="width:80px">
		   </td>
		</tr>
	</table>
	</form>
	<div id="submittalScheduleAddEditDialog" style="display: none;">
		<form id="submittalScheduleAddEditForm">
			<div id="submittalScheduleAddEditFormDiv">
				<!-- <table id="submittalScheduleAddEditFormTable"></table> -->
			</div>
			<div>
				<input type="button" id="saveSchedLineItemID" class="savehoverbutton turbo-tan" name="saveReleaseNam" value="Save" onclick="saveSchedLineItem(); createtpusage('job-Submittal Tab','Save Button','Info','job-Submittal Tab,Save');" style="width: 90px;">
				<input type="button" id="cancelSchedLineItemID" class="savehoverbutton turbo-tan" name="cancelReleaseNam" value="Cancel" onclick="cancelSchedLineItem()" style="width: 90px;">
			</div>
		</form>
		
	</div>
</div>
		<div><jsp:include page="../salesOrder.jsp"></jsp:include></div>
		<!-- <div><jsp:include page="../submittal.jsp"></jsp:include></div> -->
		<div><jsp:include page="../generalSubmittal.jsp"></jsp:include></div>
		<div><jsp:include page="../PoRelease.jsp"></jsp:include></div>
	<!--
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardSubmittal.min.js"></script>
	-->
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PORelease_General.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobWizardSubmittal.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/submittal_Schedule.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			/*$("#submittalTabs").tabs({
				cache: true,
				ajaxOptions: {
					data:{
						jobNumber: $("#jobNumber_ID").text(),
						jobName:"'" + $("#jobName_ID").text() + "'",
						jobCustomer:$("#jobCustomerName_ID").text()
					},
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 60px;padding-top: 30px;'>"+
								"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
								"</label></div>");
					}
				},
				load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
				select: function (e, ui) {
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
					}
				}
			});*/
			
			jQuery("#submittalScheduleAddEditDialog").dialog({
				autoOpen: false,
				top:190,
				height: 150,
				width: 750,
				title:"Add/Edit Line Item",
				modal: true,
				buttons:{
					Cancel: function () { jQuery(this).dialog("close"); return true; }
				},
				close: function () {
					return true;
				}
			});
		});
		function addScheduleLineItem() {
			var aTempHeaderID = aGlobal_TempHeaderID;
			var dataField = new Array();
			var displayText = new Array();
			var columnumber = new Array();
			$.ajax({
				url: "./jobtabs3/scheduledColumnName", 
				Type: 'GET',
				async:false,
				data: { 'tempHeaderID' : aTempHeaderID },
				success: function(data){
					$.each(data, function(index, value){
						columnumber[index] = value.columnNumber;
						dataField[index] = value.displayText;
					});
				}
			});
			jQuery('#submittalScheduleAddEditFormTable').empty();
			var aData = '<table>';
			for (var index = 0; index < dataField.length; index++) {
				if(dataField[index] === 'Model No.'){
					displayText[index] = 'modelNo';
				} else if(dataField[index] === 'Dwg. No.'){
					displayText[index] = 'DwgNo';
				} else if(dataField[index] === 'W x H'){
					displayText[index] = 'WxH';
				}
				displayText[index] = dataField[index].replace(/[^a-zA-Z ]/g, "").replace(" ", "");
				aData = aData + '<tr id="'+displayText[index]+'" rowpos="'+columnumber[index]+'">' +
						'<td>'+displayText[index]+'</td>'+
						'<td><input id="'+displayText[index]+'" type="text" role="textbox" name="'+displayText[index]+'"></td></tr>';
			}
			aData = aData + '</table>';
			jQuery('#submittalScheduleAddEditFormDiv').append(aData);
			/*jQuery('#submittalScheduleAddEditFormTable #tr_ModelNo .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
			jQuery('#submittalScheduleAddEditFormTable #tr_ModelNo .DataTD').append('<td><input type="hidden" id="modelID" name="modelID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
			jQuery('#submittalScheduleAddEditFormTable #tr_ModelNo .CaptionTD').append('<span style="color:red;"> *</span>'); 
			jQuery('#submittalScheduleAddEditFormTable #tr_Model .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
			jQuery('#submittalScheduleAddEditFormTable #tr_Model .DataTD').append('<td><input type="hidden" id="modelNoID" name="modelNoID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
			jQuery('#submittalScheduleAddEditFormTable #tr_Model .CaptionTD').append('<span style="color:red;"> *</span>'); */
			
			jQuery("#submittalScheduleAddEditDialog").dialog("open");
			return false;
		}

	</script>