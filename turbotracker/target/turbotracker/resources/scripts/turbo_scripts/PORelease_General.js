var shipdateMode;
var freight;
var totalGeneral ;
$(document).ready(function() {
	$(".datepicker").datepicker();
	$("#POReleaseID").show();
	$("#shipTo1").hide();
	$("#rxManuId").val();
	$('#loadingPOGenDiv').css({"visibility": "hidden"});
	checkQB();
});
var operationVar;
var asubManu;
var aVepoID;
function loadContactList(manufacture){ 
	if(!manufacture){
		var errorText = "Please select a release to edit the Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning", 
								buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		$("#vendorLineNameId").val(""); $("#ourPoLineId").val(""); $("#subtotalLineId").val("");
		$("#totalLineId").val(""); $("#taxLineId").val("");$("#freightLineId").val("");
		$("#lineID").val("");
		$("#poDateLineId").val("");
	}
	$.ajax({
		url: "./jobtabs2/filterBidderList", mType: "GET", data: { 'rxMasterId' : manufacture },
		success: function(data) {						
			var select = '<select style="width:227px;margin-left: 3px;" id="contactId" name="attnName" onchange="getContactId()"><option value="-1"> - Select -</option>';
			$.each(data, function(index, value){
				quoteId = value.id;
				var quoteName = value.value;
				select +='<option value='+quoteId+'>'+quoteName+'</option>';
			});
			select += '</select>';
			$("#contacthiddenID").hide();
			$('#contactselectID').empty();
			$('#contactselectID').append(select);
		}
	});
	getContactId = function(){
		var contactId = $("#contactId").val();
		$("#rxContactId").val(contactId);
	};
}
$("#usBillto").onclick=function () {
	$('#usBillimage').attr('src', '');
};
function checkQB(){
	$.ajax({
		url: "./qbsetting", 
		mType: "GET", 
		data : {},
		success: function(data){
			if(data===null){
				$('#qbtr').show();
				$('#qbtr input').removeAttr('disabled');
			}else{
				if(data.qbEnabled==1){
					$('#qbtr').show();
					$('#qbtr input').removeAttr('disabled');
				}else if(data.qbEnabled==0){
					$('#qbtr input').prop('disabled', true);
				}
			}
			return true;
		}
	});
}
function addPOrelease(manufacture, jobNumber, quoteNumber,manufactureName,joreleaseID,vePOId,veFactoryId,aNewPONumber){ 
	loadContactList(manufacture);
	var aManuId = $("#rxCustomer_ID").text();
	$("#rxManuId").val(aManuId);
	$("#joReleaseid").val(joreleaseID);
	$("#manfactureId").val(manufacture);
	$("#manfactureName").val(manufactureName);
	$("#customerNAMEId").val(""); 
//	$("#customerNAMEId option[value=-1]").attr("selected", true);
	$("#subtotalGeneralId").val(formatCurrency(0)); 
	$("#totalGeneralId").val(formatCurrency(0)); 
	var tax2 = taxTerritory.split("%");
	$("#taxGeneralId").val(tax2[0]); 
	$("#freightGeneralId").val(formatCurrency(0));
	$("#generalID").val(formatCurrency(0)); 
	$("#Subtotal_ID").text(formatCurrency(0));
	$("#Freight_ID").text(formatCurrency(0));
	$("#Tax_ID").text(tax2[0]);
	$("#order_ID").text("");
	$("#Percentage_ID").text(formatCurrency(0));
	$("#Total_ID").text(formatCurrency(0));
	$("#veFactoryID").val(veFactoryId);
	$("#vePOID").val(vePOId);
	$("#vePO_ID").text(vePOId);
	$("#vendor_ID").text(manufactureName);
	$("#specialInstructionID").val(""); 
	$("#wantedId").val("");
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd;} 
	if(mm<10){mm='0'+mm;} 
	today = mm+'/'+dd+'/'+yyyy;
	$("#poDateId").val(today);
	$("#poDate_ID").empty();
	$("#tagId").val($("#jobName_ID").text());
	$("#ourPoId").val(aNewPONumber); 
	$("#poDate_ID").text(today);
	$("#frieghtChangesId option[value=" + -1 + "]").attr("selected", true);
	$("#shipViaId option[value=" + -1 + "]").attr("selected", true);
	jQuery("#porelease").dialog("open");
	$("#tabs_main_job").tabs("select", 4);
	return true;
}
var dataObj = new Object();
function loadPORelease(manufacturer,vePoId,manufacturerName, aCustomerPONumber){
    //	totalGeneral=0;
    // aTotal=0;
    //	freight=0;
		loadContactList(manufacturer);
	$("#vePO_ID").text(vePoId);
	$.ajax({
		url: "./jobtabs3/getPurchaseDetails", 
		mType: "GET", 
		data : { 'vePoID' : vePoId },
		success: function(data){
			dataObj = data;
			setTimeout(function(){prePopulateGeneralInfo(data,manufacturer,vePoId,manufacturerName, aCustomerPONumber);}, 500);
			jQuery("#porelease").dialog("open");
			return true;
		}
	});
	$("#poreleasetab").tabs("select", 0);
}
function prePopulateGeneralInfo(data,manufacturer,vePoId,manufacturerName, aCustomerPONumber){
	//var data = dataObj;
	var aManuId = $("#rxCustomer_ID").text();
	$("#rxManuId").val(aManuId);
	var vePOid = data.vePoid;
	var tag = data.tag;
	var poNumber = data.ponumber;
	var customerPoNum = data.customerPonumber;
	var releaseId = data.joReleaseId;
	var shipViaId = data.veShipViaId;
	var qbPO = data.qbPO;
	var freightCharges = data.veFreightChargesId;
	var orderedId = data.orderedById;
	var subTotal = data.subtotal;
	var taxTotal = data.taxTotal;
	var taxRate = data.taxRate;
	var userName = data.tsFullName;
	freight = data.freight;
	var spcInstr = data.specialInstructions;
	var wanted = data.dateWanted;
	var wantedOnOrBefore = data.wantedOnOrBefore;
	var poDate = data.orderDate;
	shipdateMode = data.shipToMode;
	var contactID = data.rxVendorContactId;
	var factoryID = data.veFactoryId;
	var billToID = data.billTo;
	var shipToID = data.shipTo;
	var emailDate = data.emailTimeStamp;
	$("#quickbookId").val(qbPO); 
	$("#ourPoId").val(poNumber); 
	$("#order_ID").text(poNumber);
	$("#joReleaseid").val(releaseId);
	$("#vePOID").val(vePOid);
	$("#vePO_ID").text(vePOid);
	$("#veFactoryID").val(factoryID);
	$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(emailDate);
	if(qbPO != null){
		$("#quickBookID").attr('disabled','disabled');
		$("#quickbookId").attr('disabled','disabled');
	}
	if(freightCharges !== '' && freightCharges !== null){ 
		//$("#frieghtChangesId option[value=" + freightCharges + "]").attr("selected", true);
		$("#frieghtChangesId").val(freightCharges);
	}else{ 
		$("#frieghtChangesId option[value=" + -1 + "]").attr("selected", true);
	}
	if(shipViaId !== '' && shipViaId !== null){ 
		//$("#shipViaId option[value=" + shipViaId + "]").attr("selected", true);
		$("#shipViaId").val(shipViaId);
	}else{ 
		$("#shipViaId option[value=" + -1 + "]").attr("selected", true);
	}
	if(contactID !== '' && contactID !== null){ 
		//$("#contactId option[value=" + contactID + "]").attr("selected", true);
		$("#contactId").val(contactID);
	}else{ 
		$("#contactId option[value=" + -1 + "]").attr("selected", true);
	}
	$("#manfactureName").val(manufacturerName);
	$("#vendor_ID").text(manufacturerName);
	if(tag !== '' && tag !== null){
		$("#tagId").val(tag); 
	}else{
		$("#tagId").val($("#jobName_ID").text());
	}
	if(orderedId !== '' && orderedId !== null){
		$("#orderhiddenId").val(orderedId);
	$("#orderId").val(userName); 
	}
	/*if(isNaN(aCustomerPONumber)===false){
		$("#customerNAMEId option[value=-1]").attr("selected", true);
		$("#customerNAMEId option[value=" + aCustomerPONumber + "]").attr("selected", true);
	}else{
		$("#customerNAMEId option[value=-1]").attr("selected", true);
	}*/
	if(billToID == 0){
		usBilltoSelectAddress();
	}else if(billToID == 1){
		 customerBilltoSelectAddress();
	}else if(billToID == 2){
		otherBilltoSelectAddress();
	}
	if(shipToID == 0){
		 usShiptoSelectAddress();
	}else if(shipToID == 1){
		customerShiptoSelectAddress();
	}else if(shipToID == 2){
		 jobsiteShiptoSelectAddress();
	}else if(shipToID == 3){
		 otherShiptoSelectAddress();
	}
	$("#manfactureId").val(manufacturer); 
	$("#ShiphiddenId").val(shipViaId);
	$("#customerNAMEId").val(customerPoNum); 
	$("#subtotalGeneralId").val(formatCurrency(subTotal));
	totalGeneral = Number(subTotal) + Number(taxTotal) + Number(freight);
	$("#totalGeneralId").val(formatCurrency(totalGeneral));
	$("#generalID").val(formatCurrency(taxTotal));
	if(taxRate !== '' && taxRate !== null){
		$("#taxGeneralId").val(taxRate); 
		$("#Tax_ID").text(taxRate);
	}else{
		var tax2 = taxTerritory.split("%");
		$("#taxGeneralId").val(tax2[0]);
		$("#Tax_ID").text(tax2[0]);
	}
	$("#freightGeneralId").val(formatCurrency(freight));
	$("#generalID").val(formatCurrency("")); 
	$("#specialInstructionID").val(spcInstr); 
	$("#wantedId").val(wanted);
	$("#wantedComboId").val(wantedOnOrBefore);
	$("#Subtotal_ID").text(formatCurrency(subTotal));
	$("#Freight_ID").text(formatCurrency(freight));
	$("#Percentage_ID").text();
	$("#Total_ID").text();

	if(poDate !== '' && poDate !== null){
		var poDay = new Date(poDate).getDate();
		var poMonth = new Date(poDate).getMonth();
		var poYear = new Date(poDate).getFullYear();
		if(poDay<10){poDay='0'+poDay;} 
		if(poMonth<10){poMonth='0'+poMonth;} 
		poDate = (Number(poMonth)+1) +"/"+ poDay +"/"+ poYear;
		$("#poDateId").val(poDate);
		$("#poDate_ID").empty();
		$("#poDate_ID").text(poDate);
	}else{
		/*var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; 
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd;} 
		if(mm<10){mm='0'+mm;} 
		today = mm+'/'+dd+'/'+yyyy;*/
		var grid = $("#release");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var vePOdate = grid.jqGrid('getCell', rowId, 'released');
		$("#poDateId").val(vePOdate);
		$("#poDate_ID").empty();
		$("#tagId").val($("#jobName_ID").text());
		$("#poDate_ID").text(vePOdate);
	}
	
	/**
	 * When we opeen line items first time onlt the data is loading properly.  if we open second time, the line item js file is already cached by DOM.
	 * So these values will not be reloaded correctly.
	 * 
	 * IT IS A SEVER BUG
	 * **/
	/****************** Data related to line items ***********************/
	$("#vendorLineNameId").val($("#vendor_ID").text()); $("#ourPoLineId").val($("#order_ID").text()); $("#subtotalLineId").val($("#Subtotal_ID").text()); 
	$("#totalLineId").val($("#Total_ID").text()); $("#taxLineId").val($("#Tax_ID").text());$("#freightLineId").val(formatCurrency(freight));
	$("#lineID").val($("#Percentage_ID").text());
	$("#poDateLineId").val($("#poDate_ID").text());
	/*********************************************************************/
	
	/********************* Data related to line items ********************/
	$("#vendorAckNameId").val($("#vendor_ID").text()); $("#ourPoAckId").val($("#order_ID").text()); $("#subtotalKnowledgeId").val($("#Subtotal_ID").text()); 
	$("#totalKnowledgeId").val($("#Total_ID").text()); $("#taxKnowledgeId").val($("#Tax_ID").text()); $("#freightKnowledgeId").val(formatCurrency(freight));
	$("#KnowledgeID").val($("#Percentage_ID").text());
	$("#poDateAckId").val($("#poDate_ID").text());
	/*********************************************************************/
}

$(function() { var cache = {}; var lastXhr='';
$( "#orderId" ).autocomplete({ minLength: 1,timeout :1000, select: function( event, ui ) { var id = ui.item.id; $("#orderhiddenId").val(id); var name = ui.item.value;$("#orderId").val(name);},
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
lastXhr = $.getJSON( "jobtabs3/userName", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

 $(function() { var cache = {}; var lastXhr=''; $("#manfactureId" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) {   },
source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
lastXhr = $.getJSON( "jobtabs3/manufactureList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); }); 
 
 $(function() { var cache = {}; var lastXhr=''; $( "#locationbillToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationbillToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
 
 $(function() { var cache = {}; var lastXhr=''; $( "#locationShipToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationShipToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
 
 function frieghtCost(){
	 var aEstimatedCost=$("#freightGeneralId").val();
	 var aEstimatedCst=aEstimatedCost.replace(/[^0-9\.]+/g,"");
	 aEstimatedCst = Number(aEstimatedCst);
	 $("#freightGeneralId").val(aEstimatedCst);
	 return true;
 }
 
 function frieghtFormat(){
	 	var aEstimatedCost=$("#freightGeneralId").val();
 		var aSubTotal= $('#subtotalGeneralId').val();
 		var subNumber = aSubTotal.replace(/[^0-9\.]+/g,"");
 		var taxValue = $('#generalID').val();
 		var taxNumber = taxValue.replace(/[^0-9\.]+/g,"");
 		var totalGeneral  = 0;
 		totalGeneral  = totalGeneral  + Number(aEstimatedCost) + Number(taxNumber) + Number(subNumber);
 		$('#totalGeneralId').val(formatCurrency(totalGeneral ));
 		$("#freightGeneralId").val(formatCurrency(aEstimatedCost));
 		$("#freightLineId").val(formatCurrency(aEstimatedCost));
 		$("#Freight_ID").text(formatCurrency(aEstimatedCost));
		$("#Total_ID").text(formatCurrency(aTotal));
		return true;
 }
 
 function savePORelease(){
	 
	 var newDialogDiv = jQuery(document.createElement('div'));
	 if(!$('#POGeneralForm').validationEngine('validate')) {
		return false;
	}
	 var IsSaveButtonClicked  = $('#ButtonClicked');
	 var aFreightVal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var aFreightChk = aFreightVal.split(".");
	 var aFreightSpilt = aFreightChk[0];
	 if(aFreightSpilt.length > 5){
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Please provide 5 digit figures.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); document.getElementById('freightGeneralId').focus(); }}]}).dialog("open");
			return false;
	 }
	 var aGenerelFormValues = $("#POGeneralForm").serialize();
	 var aJobNumber = $("#jobNumber_ID").text();
	 var qbId = $("#quickbookId").val();
	 var aManufacture = $("#rxManuId").val();
	 var aShipAddress = $("#rxAddressShipID").val(); 
	 var aBillAddress = $("#rxAddressBillID").val(); 
	 var vendorname = $("#vendor_ID").text();
	 var veFactoryId = $("#veFactoryID").val();
	 var tagVal = 	$("#tagId").val(); 
	 var emailTimeStamp = $("#emailTimeStamp").text();
	 var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var asubTotal = $("#subtotalGeneralId").val().replace(/[^0-9\.]+/g,"");
	 var aTaxTotal = $("#generalID").val().replace(/[^0-9\.]+/g,"");
	 /** *********  IT IS IMPORTANT CODE  ************
	  var aRequestObj = {
			 JOB_NUMBER: "",
			 Manu:"",
			 asdfdas:""
	 };
	 aRequestObj = JSON.stringify(aRequestObj);
	 ************************************************** */
	 var aPOGenerelValues = aGenerelFormValues+"&jobNumber="+aJobNumber+"&manufaturer="+aManufacture+"&freightGeneralName="+afreightTotal+"&subtotalGeneralName="+asubTotal+"&vendorName=" +vendorname
	 			+"&toBillAddress="+aBillAddress+"&toShipAddress="+aShipAddress+
	 			"&TaxTotal="+aTaxTotal+"&vefactoryID="+veFactoryId+"&tagName="+tagVal+
	 			"&emailTimeStamp="+emailTimeStamp+"&isSaveButtonClicked="+IsSaveButtonClicked+"&qbId="+qbId;
	 $.ajax({
			url: './jobtabs3/addPORelease',
			type : 'POST',
			data: aPOGenerelValues,
			success: function (data){
				aVepoID = data.vePoid;
				$("#vePO_ID").text(aVepoID);
				$("#vePOID").val(aVepoID);
				var errorText = "Purchase Order General Tab details are Saved.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
				if($('#ButtonClicked').is(':checked')){
					$('#showMessage').html("Saved");
					setTimeout(function(){
						$('#showMessage').html("");
						},3000);
					$('#ButtonClicked').prop('checked', false);
				}else{
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");jQuery("#porelease").dialog("close");$("#release").trigger("reloadGrid"); }}]}).dialog("open");
				return true;
				}
			}
	 });
	 return true;
 }
 
 function usShiptoAddress(){
	 if (shipdateMode =1){
	 $("#shipTo2").show();
	 $("#shipTo").hide();
	 $("#shipTo1").hide();
	 }else {
		 $("#shipTo1").show();
		 $("#shipTo").hide();
		 $("#shipTo2").hide();
	 }
	 $("#shiptoradio1").attr("Checked","Checked");
	 $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').addClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usShipUSID = 0;
	 var updaetKey = 'shipTo';
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 loadShipToAddress();
	 updateBillToAndShipToAddressSetting(vePOID, usShipUSID, updaetKey);
	 return true;
	 }
 
 function customerShiptoAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio2").attr("Checked","Checked");
	 $('#shiptolabel2').addClass("ui-state-active");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 $('#shiptolabel2').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 var vePOID = $("#vePO_ID").text();
	 var usShipCustID = 1;
	 var updaetKey = 'shipTo';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "ship";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
     			}
		});
		 updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);
		 return true;
 }
 function jobsiteShiptoAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio3").attr("Checked","Checked");
	 $('#shiptolabel3').addClass("ui-state-active");
	 $('#shiptolabel3').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usShipCustID = 2;
	 var updaetKey = 'shipTo';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
		$("#locationShipToState").val(jobState); $("#locationShipToZipID").val(jobZip);
		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
		updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);
		return true;
 }
 function otherShiptoAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio4").attr("Checked","Checked");
	 $('#shiptolabel4').addClass("ui-state-active");
	 $('#shiptolabel4').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'shipToAddressID');
	 var usShipCustID = 3;
	 var updaetKey = 'shipTo';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(data.name); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=false;
				document.getElementById('locationShipToAddressID1').disabled=false;
				document.getElementById('locationShipToAddressID2').disabled=false;
				document.getElementById('locationShipToCity').disabled=false;
				document.getElementById('locationShipToState').disabled=false;
				document.getElementById('locationShipToZipID').disabled=false;
			}
		});
		updateBillToAndShipToAddressSetting(vePOID, usShipCustID, updaetKey);
		return true;
 }
 
 function usShiptoSelectAddress(){
	 if (shipdateMode =1){
		 $("#shipTo2").show();
		 $("#shipTo").hide();
		 $("#shipTo1").hide();
		 }else {
			 $("#shipTo1").show();
			 $("#shipTo").hide();
			 $("#shipTo2").hide();
		 }
	 $("#shiptoradio1").attr("Checked","Checked");
	 $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').addClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	/* $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 loadShipToAddress();
	 return true;
 }
 function customerShiptoSelectAddress(){
	 $("#shipTo1").hide();
		$("#shipTo2").hide();
		$("#shipTo").show();
	 $("#shiptoradio2").attr("Checked","Checked");
	 $('#shiptolabel2').addClass("ui-state-active");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 $('#shiptolabel2').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 /*$('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $("#shipTo1").hide();
		$("#shipTo").show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "ship";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
				}
		});
		 return true;
 }
 function jobsiteShiptoSelectAddress(){
	 $("#shipTo1").hide();
	 $("#shipTo2").hide();
	 $("#shipTo").show();
	 $("#shiptoradio3").attr("Checked","Checked");
	 $('#shiptolabel3').addClass("ui-state-active");
	 $('#shiptolabel3').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");/*
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");*/
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
		$("#locationShipToState").val(jobState); $("#locationShipToZipID").val(jobZip);
		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
		return true;
 }
 function otherShiptoSelectAddress(){
	 $("#shipTo1").hide();
		$("#shipTo2").hide();
		$("#shipTo").show();
	 $("#shiptoradio4").attr("Checked","Checked");
	 $('#shiptolabel4').addClass("ui-state-active");
	 $('#shiptolabel4').css("font-weight","bold");
	 $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel1').removeClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	/* $('#shiptolabel1').css("font-weight","normal");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal")*/;
	 var updaetKey = 'shipTo';
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'shipToAddressID');
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				$("#locationShipToAddressID").val(data.name); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=false;
				document.getElementById('locationShipToAddressID1').disabled=false;
				document.getElementById('locationShipToAddressID2').disabled=false;
				document.getElementById('locationShipToCity').disabled=false;
				document.getElementById('locationShipToState').disabled=false;
				document.getElementById('locationShipToZipID').disabled=false;
			}
		});
		return true;
 }
 
 function shipToAddress(){
	var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "ship";
		$.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressShipID").val(rxAddressId); 
				//$("#locationShipToAddressID").val(locationName); $("#locationShipToAddressID1").val(locationAddress1); $("#locationShipToAddressID2").val(locationAddress2); $("#locationShipToCity").val(locationCity);
				 $("#locationShipToAddressID").val(CustomName); $("#locationShipToAddressID1").val(jobLocation1); $("#locationShipToAddressID2").val(jobLocation2); $("#locationShipToCity").val(jobCity);
				$("#locationShipToState").val(locationState); $("#locationShipToZipID").val(locationZip);
				document.getElementById('locationShipToAddressID').disabled=true;
				document.getElementById('locationShipToAddressID1').disabled=true;
				document.getElementById('locationShipToAddressID2').disabled=true;
				document.getElementById('locationShipToCity').disabled=true;
				document.getElementById('locationShipToState').disabled=true;
				document.getElementById('locationShipToZipID').disabled=true;
				}
		});
 }

 function usBilltoAddress(){
	 $('#billtolabel1').css("font-weight","bold");
	 $('#billtolabel2').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio1").attr("Checked","Checked");
	 
	 $('#billtolabel1').addClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	 
	 var vePOID = $("#vePO_ID").text();
	 var usBillUSID = 0;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var description= $("#billtodescription").val();
	 var address1= $("#billtoaddress1").val();
	 var address2= $("#billtoaddress2").val();
	 var city= $("#billtocity").val();
	 var state= $("#billtostate").val();
	 var zipcode= $("#billtozip").val();
	 $("#locationbillToAddressID").val(description);
	 $("#locationbillToAddressID1").val(address1); 
	 $("#locationbillToAddressID2").val(address2); 
	 $("#locationbillToCity").val(city);
	 $("#locationbillToState").val(state); 
	 $("#locationbillToZipID").val(zipcode);
		document.getElementById('locationbillToAddressID').disabled=true;
		document.getElementById('locationbillToAddressID1').disabled=true;
		document.getElementById('locationbillToAddressID2').disabled=true;
		document.getElementById('locationbillToCity').disabled=true;
		document.getElementById('locationbillToState').disabled=true;
		document.getElementById('locationbillToZipID').disabled=true;
		updateBillToAndShipToAddressSetting(vePOID, usBillUSID, updaetKey);
		 return true;
 }
 function customerBilltoAddress(){
	 
	 $('#billtolabel2').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio2").attr("Checked","Checked");
	 
	 $('#billtolabel2').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	    
	 var vePOID = $("#vePO_ID").text();
	 var usBillCustID = 1;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
 	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=true;
				document.getElementById('locationbillToAddressID1').disabled=true;
				document.getElementById('locationbillToAddressID2').disabled=true;
				document.getElementById('locationbillToCity').disabled=true;
				document.getElementById('locationbillToState').disabled=true;
				document.getElementById('locationbillToZipID').disabled=true;
			}
		 });
		 updateBillToAndShipToAddressSetting(vePOID, usBillCustID, updaetKey);
		 return true;
 }
function otherBilltoAddress(){
	 $('#billtolabel3').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel2').css("font-weight","normal");
	 $("#billtoradio3").attr("Checked","Checked");
	 $('#billtolabel3').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usBillOtherID = 2;
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px"});
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px"});
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px"});
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'billToAddressID');
	 $.ajax({
		url: "./jobtabs3/getBilltoAddress",
		type: "GET",
		data : {"customerID" : rxMasterId,"oper" : updaetKey },
		success: function(data) {
			var locationName = $("#jobCustomerName_ID").text();
			var rxAddressId = data.rxAddressId;
			var locationAddress1 = data.address1;
			var locationAddress2 = data.address2;
			var locationCity = data.city;
			var locationState = data.state;
			var locationZip = data.zip;
			$("#rxAddressBillID").val(rxAddressId); 
			$("#locationbillToAddressID").val(data.name); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
			$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
			document.getElementById('locationbillToAddressID').disabled=false;
			document.getElementById('locationbillToAddressID1').disabled=false;
			document.getElementById('locationbillToAddressID2').disabled=false;
			document.getElementById('locationbillToCity').disabled=false;
			document.getElementById('locationbillToState').disabled=false;
			document.getElementById('locationbillToZipID').disabled=false;
		}
	 });
	 updateBillToAndShipToAddressSetting(vePOID, usBillOtherID, updaetKey);
	return true;
}	


function usBilltoSelectAddress(){
	$('#billtolabel1').css("font-weight","bold");
	$('#billtolabel2').css("font-weight","normal");
	$('#billtolabel3').css("font-weight","normal");
	$("#billtoradio1").attr("Checked","Checked");
	
	$('#billtolabel1').addClass("ui-state-active");
	$('#billtolabel2').removeClass("ui-state-active");
	$('#billtolabel3').removeClass("ui-state-active");
	var description= $("#billtodescription").val();
	var address1= $("#billtoaddress1").val();
	var address2= $("#billtoaddress2").val();
	var city= $("#billtocity").val();
	var state= $("#billtostate").val();
	var zipcode= $("#billtozip").val();
	$("#locationbillToAddressID").val(description);
	$("#locationbillToAddressID1").val(address1); 
	$("#locationbillToAddressID2").val(address2); 
	$("#locationbillToCity").val(city);
	$("#locationbillToState").val(state); 
	$("#locationbillToZipID").val(zipcode);
	document.getElementById('locationbillToAddressID').disabled=true;
	document.getElementById('locationbillToAddressID1').disabled=true;
	document.getElementById('locationbillToAddressID2').disabled=true;
	document.getElementById('locationbillToCity').disabled=true;
	document.getElementById('locationbillToState').disabled=true;
	document.getElementById('locationbillToZipID').disabled=true;
	return true;
}
function customerBilltoSelectAddress(){
	 $('#billtolabel2').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel3').css("font-weight","normal");
	 $("#billtoradio2").attr("Checked","Checked");
	 $('#billtolabel2').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel3').removeClass("ui-state-active");
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : operationVar},
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=true;
				document.getElementById('locationbillToAddressID1').disabled=true;
				document.getElementById('locationbillToAddressID2').disabled=true;
				document.getElementById('locationbillToCity').disabled=true;
				document.getElementById('locationbillToState').disabled=true;
				document.getElementById('locationbillToZipID').disabled=true;
			}
		 });
		 return true;
}
function otherBilltoSelectAddress(){
	 $('#billtolabel3').css("font-weight","bold");
	 $('#billtolabel1').css("font-weight","normal");
	 $('#billtolabel2').css("font-weight","normal");
	 $("#billtoradio3").attr("Checked","Checked");
	 $('#billtolabel3').addClass("ui-state-active");
	 $('#billtolabel1').removeClass("ui-state-active");
	 $('#billtolabel2').removeClass("ui-state-active");
	 var updaetKey = 'billTo';
	 $('#usBillto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px"});
	 $('#customerBillto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px"});
	 $('#otherBillto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px"});
	 	var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var rxMasterId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'billToAddressID');
		 $.ajax({
			url: "./jobtabs3/getBilltoAddress",
			type: "GET",
			data : {"customerID" : rxMasterId,"oper" : updaetKey },
			success: function(data) {
				var locationName = $("#jobCustomerName_ID").text();
				var rxAddressId = data.rxAddressId;
				var locationAddress1 = data.address1;
				var locationAddress2 = data.address2;
				var locationCity = data.city;
				var locationState = data.state;
				var locationZip = data.zip;
				$("#rxAddressBillID").val(rxAddressId); 
				$("#locationbillToAddressID").val(data.name); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
				$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
				document.getElementById('locationbillToAddressID').disabled=false;
				document.getElementById('locationbillToAddressID1').disabled=false;
				document.getElementById('locationbillToAddressID2').disabled=false;
				document.getElementById('locationbillToCity').disabled=false;
				document.getElementById('locationbillToState').disabled=false;
				document.getElementById('locationbillToZipID').disabled=false;
			}
		 });
		return true;
}	

 function billToAddress(){
	/* if($('#usBillId').is(':checked')){
		 $("#locationbillToAddressID").val("Bache Sales Agency"); $("#locationbillToAddressID1").val("P.O. Box 921369"); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("Norcross");
			$("#locationbillToState").val("GA"); $("#locationbillToZipID").val("30010");
			document.getElementById('locationbillToAddressID').disabled=true;
			document.getElementById('locationbillToAddressID1').disabled=true;
			document.getElementById('locationbillToAddressID2').disabled=true;
			document.getElementById('locationbillToCity').disabled=true;
			document.getElementById('locationbillToState').disabled=true;
			document.getElementById('locationbillToZipID').disabled=true;
	 }else if($('#customerBillId').is(':checked')){*/
		var rxMasterId = $("#rxCustomer_ID").text();
		operationVar = "bill";
		 $.ajax({
				url: "./jobtabs3/getBilltoAddress",
				type: "GET",
				data : {"customerID" : rxMasterId,"oper" : operationVar},
				success: function(data) {
					var locationName = $("#jobCustomerName_ID").text();
					var rxAddressId = data.rxAddressId;
					var locationAddress1 = data.address1;
					var locationAddress2 = data.address2;
					var locationCity = data.city;
					var locationState = data.state;
					var locationZip = data.zip;
					$("#rxAddressBillID").val(rxAddressId); 
					 $("#locationbillToAddressID").val("Bache Sales Agency"); $("#locationbillToAddressID1").val("P.O. Box 921369"); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("Norcross");
					//$("#locationbillToAddressID").val(locationName); $("#locationbillToAddressID1").val(locationAddress1); $("#locationbillToAddressID2").val(locationAddress2); $("#locationbillToCity").val(locationCity);
					$("#locationbillToState").val(locationState); $("#locationbillToZipID").val(locationZip);
					document.getElementById('locationbillToAddressID').disabled=true;
					document.getElementById('locationbillToAddressID1').disabled=true;
					document.getElementById('locationbillToAddressID2').disabled=true;
					document.getElementById('locationbillToCity').disabled=true;
					document.getElementById('locationbillToState').disabled=true;
					document.getElementById('locationbillToZipID').disabled=true;
					}
				});
	/* }else if($('#otherBillId').is(':checked')){ 
		 $("#locationbillToAddressID").val(""); $("#locationbillToAddressID1").val(""); $("#locationbillToAddressID2").val(""); $("#locationbillToCity").val("");
			$("#locationbillToState").val(""); $("#locationbillToZipID").val("");
			document.getElementById('locationbillToAddressID').disabled=false;
			document.getElementById('locationbillToAddressID1').disabled=false;
			document.getElementById('locationbillToAddressID2').disabled=false;
			document.getElementById('locationbillToCity').disabled=false;
			document.getElementById('locationbillToState').disabled=false;
			document.getElementById('locationbillToZipID').disabled=false;
	 }*/
 }

 function sendPOGENEmail(){
		var bidderGrid = $("#release");
		var aQuotePDF = "purchaseGen";
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var errorText = '';
		var cusotmerPONumber = $("#ourPoId").val();
		var newDialogDiv = jQuery(document.createElement('div'));
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to Email Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxVendorContactID');
		if(aContactID === ''){
			errorText = "Please Add Contact for Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}else{
			$.ajax({ 
				url: "./vendorscontroller/GetContactDetails",
				mType: "GET",
				data : { 'rxContactID' : aContactID},
				success: function(data){
					var aFirstname = data.firstName;
					var aLastname = data.lastName;
					var aEmail = data.email;
					var arxContact = aFirstname + ' '+aLastname;
					if(aEmail ==''){
						errorText = "Are you sure you want to send this PO General to"+ arxContact +"?";
					}else{
						errorText = "Are you sure you want to send this PO General to"+ arxContact +"("+ aEmail +")?";
					}
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
					buttons:{
						"Send": function(){
							$('#loadingPOGenDiv').css({"visibility": "visible"});
							openPOGenPDF(aQuotePDF);
							sentMailPOGenFunction(aContactID,aEmail, cusotmerPONumber);
							jQuery(this).dialog("close");
						},
						Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
				}
			});
		}
		return true;
	}

	function sentMailPOGenFunction(aContactID,aEmail, cusotmerPONumber){
		if(aEmail === ''){
			var errorText = "Please Provide a Mail ID.";
			jQuery(newDialogDiv).html('<form id="mailToAddress"><table><tr><td><span><b style="color:red;">'+errorText+'</b></span></td></tr>'+
					'<tr><td style="height: 5px;"></td></tr>' +
			'<tr><td><label>Mail ID: </label><input type="text" id="mailToAddress_ID" name="mailToAddress_name" class="validate[custom[email]]" style="width: 250px;" /></td></tr></table></form><hr>');
			jQuery(newDialogDiv).dialog({modal: true, width:430, height:180, title:"Message", 
				buttons:{
					"Submit & Send": function(){
						if(!$('#mailToAddress').validationEngine('validate')) {
							return false;
						}
						var aEmailAddress = $("#mailToAddress_ID").val();
						saveMailAddress(aEmailAddress, aContactID);
						$('#loadingPOGenDiv').css({"visibility": "visible"});
						$.ajax({ 
							url: "./sendMailServer/sendPOGenMail",
							mType: "POST",
							data : {'contactID' : aContactID, 'POPDF' : 'poGeneral' , 'PONumber' : cusotmerPONumber},
							success: function(data){
								$('#loadingPOGenDiv').css({"visibility": "hidden"});
								var errorText = "Mail Sent Successfully.";
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
							}
						});
						jQuery(this).dialog("close");
						return true;
					},
					Cancel: function ()	{jQuery(this).dialog("close");}
				}}).dialog("open");
		}else{
			$.ajax({ 
				url: "./sendMailServer/sendPOGenMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'POPDF' : 'poGeneral' , 'PONumber' : cusotmerPONumber},
				success: function(data){
					$('#loadingPOGenDiv').css({"visibility": "hidden"});
					var errorText = "Mail Sent Successfully.";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
				}
			});
		}
	}

	function saveMailAddress(aEmailAddress, aContactID){
		$.ajax({ 
			url: "./jobtabs2/updateEmailAddress",
			mType: "GET",
			data : { 'email' : aEmailAddress, 'contactID' : aContactID },
			success: function(data){
			}
		});
	}
	function openPOGenPDF(aQuotePDF){
		var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
		if(aQuotePDF === 'purchaseGen'){
			$.ajax({
				   type : "GET",
				   url : "./purchasePDFController/viewPurchasePdfForm",
				   data : { 'vePOID' : vePOID, 'puchaseOrder' : aQuotePDF },
				   documenttype: "application\pdf",
			        async: false,
			        cache: false,
			   success : function (msg) {
			   },
			   error : function (msg) {}
			});
			 return true;
		}
	}
	
	function viewPOGENPDF(){
		var bidderGrid = $("#release");
		var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
		var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
		var aQuotePDF = "PO";
		window.open("./purchasePDFController/viewPurchasePdfForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF);
		return true;
	}
	
	function updateBillToAndShipToAddressSetting(vePOID, usAddressID, updateKey){
		$.ajax({
		   url : "./jobtabs3/updateBillToAndShipToSetting",
		   type : "GET",
		   data : { 'vePOID': vePOID, 'isAddressID': usAddressID, 'updaetKey': updateKey},
		   success : function (msg) {
			   
		   },
		   error : function (msg) {}
		});
		return true;
	}
	
	function updateShipToAddressValue(theVePOID, addressValue){
		$.ajax({
			   url : "./jobtabs3/updateShipToValue",
			   type : "GET",
			   data : { 'vePOID': theVePOID, 'isAddressID': addressValue},
			   success : function (msg) {
			   },
			   error : function (msg) {}
			});
			return true;
	}
	
	function createQuickBooks(){
		var jobNumber = $('.jobHeader_JobNumber').val();
		var jobName = $('#jobHeader_JobName_id').val();
		var customerName = $('#jobHeader_JobCustomerName_id').val();
		var manfactureName = $('#manfactureName').val();
		var vePOID = $("#vePOID").val();
		var items = [];
		var qbinputs = [];
		var inputs = {};
		var iteminputs = {};
		
		$.ajax({
			 url : "./jobtabs3/getVepodetailList",
			 type : "GET",
			 async: false,
			 data : { 'vePOID': vePOID},
			 success : function (data) {
				 $.each(data, function( index, value ) {
					 $.ajax({
						   url : "./jobtabs3/getItemCode",
						   type : "GET",
						   async: false,
						   data : { 'prMasterId': value.prMasterId},
						   success : function (data) {
							   iteminputs["ItemName"] = data;
						   },
						   error : function (msg) { alert(msg); }
					 });
					 iteminputs["Description"] = value.description;
					 iteminputs["Quantity"] = value.quantityOrdered+"";
					 iteminputs["rate"] = value.unitCost+"";
					 items.push(iteminputs);
				 });
				 
			 },
			 
			 error : function (msg) { alert(msg); }
		});
		var details = {};
		details["VendorName"] = manfactureName;
		details["customername"] = customerName;
		inputs["details"] = details;
		inputs["item"] = items;
		qbinputs.push(inputs);

		$.blockUI({ css: { 
				border: 'none', 
				padding: '15px', 
				backgroundColor: '#000', 
				'-webkit-border-radius': '10px', 
				'-moz-border-radius': '10px', 
				opacity: .5, 
				'z-index':'1010px',
				color: '#fff'
			} }); 

		  $.ajax({  
		        url: 'http://50.192.42.51/api/values' ,
		        data:  {'details':JSON.stringify(inputs)},  
		        type: "POST",  
		        crossDomain: true,
		        dataType:'application/json',
		        success: function(data) {
	        }, 
		        error: function(data) {
		        	var response = data.responseText;
		        	var obj =  $.parseJSON(response);
		        	if(obj.Data.ponumber !== null){
		        		setTimeout($.unblockUI, 200);
		        		$('#quickbookId').val(obj.Data.ponumber);
		        		savePo();
		        		$("#quickBookID").attr('disabled','disabled');
		        		$("#quickbookId").attr('disabled','disabled');
		        	} else{
		        		setTimeout($.unblockUI, 200);
		        		var errorText = obj.Data.status;
		        		var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error", 
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}] }).dialog("open");
		        	}
		        }
		    });  
		    return true;
	}
