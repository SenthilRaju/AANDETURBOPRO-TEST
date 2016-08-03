<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="jobListWizardDivColor">
	<form action="" id="jobHeaderFormId">
	 <table class="joblistwizardHeaderDiv">
		<tr id="headerTab">
			<td><b>Project: </b><span class="mandatory"> *</span>
				<input id="jobHeader_JobName_id" name="jobHeader_JobName_name" type="text" class="jobHeader_JobName validate[required] validate[maxSize[50]]" style="width:300px;"></td>
		<td id="customerLabel">&nbsp;<b>Customer: </b>
			<td id="customerFields">
				<input id="customerNameFieldID" name="jobHeader_JobCustomer_name" type="text" value="${requestScope.CustomerName}"  class="customerNameField" style="width: 250px;">
				<input id="cusotmerName" type="text" value="${requestScope.CustomerName}"  class="customerNameField" style="width: 250px;display: none;">
				 <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" OnClick="showCustomerPage()">
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
		select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id); 
		$.ajax({
			url: './job_controller/Customerbaseddropdowns',
			type : 'POST',
			data: {'id':id },
			success: function (data){
				if(data!=null){

					$("#salesRepId").val("");
					$( "#jobMain_salesRepsList" ).val("");
					$("#CSRId").val("");	
					$( "#jobMain_CSRList" ).val("");
					$("#salesMgrId").val("");	
					$( "#jobMain_SalesMgrList" ).val("");
					$("#engineerId").val("");	
					$( "#jobMain_EngineersList" ).val("");
					$("#prjMgrId").val("");	
					$( "#jobMain_PrjMgrList" ).val("");	
					if(data.cuAssignmentId0!=null && data.cuAssignmentId0!=""){
						$("#salesRepId").val(data.cuAssignmentId0);
						$( "#jobMain_salesRepsList" ).val(data.salesrep);
					}
					if(data.cuAssignmentId1!=null && data.cuAssignmentId1!=""){
						$("#CSRId").val(data.cuAssignmentId1);	
						$( "#jobMain_CSRList" ).val(data.csr);
					}
					if(data.cuAssignmentId2!=null && data.cuAssignmentId2!=""){
						$("#salesMgrId").val(data.cuAssignmentId2);	
						$( "#jobMain_SalesMgrList" ).val(data.salesmgr);
					}
					if(data.cuAssignmentId3!=null && data.cuAssignmentId3!=""){
						$("#engineerId").val(data.cuAssignmentId3);	
						$( "#jobMain_EngineersList" ).val(data.engineer);
					}
					if(data.cuAssignmentId4!=null && data.cuAssignmentId4!=""){
						$("#prjMgrId").val(data.cuAssignmentId4);	
						$( "#jobMain_PrjMgrList" ).val(data.projectmgr);
					}
					if(data.defaultTaxTerritory == 1)
					{
										$("#jobMain_TaxTerritory").val("");
										$("#TaxTerritory").val("");
										$("#TaxValue").html("0.00%");
						if(data.coTaxID!=null && data.coTaxID != "-1")
							{
										$("#jobMain_TaxTerritory").val(data.taxName);
										$("#TaxTerritory").val(data.coTaxID);
										$("#TaxValue").html(data.taxRate);
							}
					}
				
				}
			}
		});
		},
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
	/**
	*Created by: Praveenkumar
	*Date : 2014-09-17
	*Magnifying lookup icon action for customers
	*/
function showCustomerPage(){
		var checkpermission=getGrantpermissionprivilage('Customers',0);
		if(checkpermission){
		var rxNumber = $('#JobCustomerId').val();
		var name = $('#jobHeader_JobCustomerName_id').val();

		    		var name1 = name.replace('&', 'and');
		    		var name2 = name1.replace('&', 'and');
					document.location.href = "./customerdetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
		}
	}
</script>