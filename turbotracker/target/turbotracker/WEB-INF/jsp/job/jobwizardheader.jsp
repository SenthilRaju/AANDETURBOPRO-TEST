<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="jobListWizardDivColor">
	<form action="" id="jobHeaderFormId">
	 <table class="joblistwizardHeaderDiv">
		<tr id="headerTab">
			<td><b>Project: </b><span class="mandatory"> *</span>
				<input id="jobHeader_JobName_id" name="jobHeader_JobName_name" type="text" class="jobHeader_JobName validate[required] validate[maxSize[50]]" style="width:300px;"></td>
		<td id="customerLabel">&nbsp;<b>Customer: </b>
			<td id="customerFields">
				<input name="jobHeader_JobCustomer_name" type="text" value="${requestScope.CustomerName}"  class="customerNameField" style="width: 250px;">
				<input id="cusotmerName" type="text" value="${requestScope.CustomerName}"  class="customerNameField" style="width: 250px;display: none;">
			</td> 
			<td id="customerBookedLabel"></td>
			<td id="customerBookedFields">
			</td> 
			<td><input type="hidden" id="JobCustomerId" name="empCustomerID" value="${requestScope.joMasterDetails.rxCustomerId}" class="jobCustomerID">
			<td style="float:right;">&nbsp;<b>Job #: </b>
				<input type="text" class="jobHeader_JobNumber" name="jobHeader_JobNumber_name" disabled="disabled"></td>
		</tr>
		<tr id= "OriJobNumber" align="right" style="display: none;">
			<td colspan="7" align="right"><b>Original Quote#:</b>
			<input type="text" class="jobHeader_ORI_JobNumber" name="jobHeader_ORI_JobNumber_name" disabled="disabled" value="${requestScope.joMasterDetails.quoteNumber}"></td>
		</tr>
	</table> 
	</form>
</div>
<script type="text/javascript">
	var newDialogDiv = jQuery(document.createElement('div'));
	$("#jobStatusField").val("${sessionScope.jobGridObj.jobStatus}");
	var jobStatus = "${requestScope.joMasterDetails.jobStatus}";
	$("#jobStatusList option[value="+jobStatus+"]").attr("selected", true);
	$("#jobCustomerName_ID").text("${requestScope.CustomerName}");
	jQuery(document).ready(function(){
		$("#jobNoMain").val($.trim($("#jobNumber_ID").text()));
		$(".jobHeader_JobNumber").empty();
		$(".jobHeader_JobName").empty();
		$(".jobHeader_JobCustomer").empty();
		$(".jobHeader_JobNumber").val($.trim($("#jobNumber_ID").text()));
		$(".jobHeader_JobName").val($("#jobName_ID").text());
		$(".jobHeader_JobCustomer").val($("#jobCustomerName_ID").text());
		getUrlVars();
		var token = getUrlVars()["token"];
		if (token === 'new') {
			$("#bidDate_Date").datepicker('setDate', new Date());
			$("#created").datepicker('setDate', new Date());
		}
	});

	$(function() { var cache = {}, lastXhr;
		$( ".jobHeader_JobCustomer" ).autocomplete({
			minLength: 2, timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id); },
			source: function( request, response ) { var term = request.term;
				if( term in cache ){ response( cache[ term ] ); return; }
				lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} 
		});
	});

	$(function() { var cache = {}, lastXhr;
	$( ".customerNameField" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id); },
		source: function( request, response ) { var term = request.term;
			if( term in cache ){ response( cache[ term ] ); return; }
			lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
});
	
	function customFormatDate(date) {
		/*2003-02-18 00:00:00.0 ----------- YYYY-mm-dd*/
		if(date === ""){
			return "";
		}
		var arr1 = date.split(" ");
		var arr2 = arr1[0].split("-");
		var newDate = arr2[1] + "/" + arr2[2] + "/" + arr2[0];
		return newDate;
	}

</script>