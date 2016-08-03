var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
	$(".datepicker").datepicker();
	var aPrMasterID = getUrlVars()["inventoryId"];
	var aItemCode = getUrlVars()["itemCode"];
	var aTokenID = getUrlVars()["token"];
	$("#prMasterID").text(aPrMasterID);
	$("#itemCode").text(aItemCode);
	$(".tabs_main").tabs({
		cache: true,
		ajaxOptions: {
			data: { prMasterId  : aPrMasterID },
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			//window.location.hash = ui.tab.hash;
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
						"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
			}
		}
	});
 	if(aTokenID === 'new'){
 		$("#deleteInventory").css("display", "none");
 	}
});

function deleteInventoryDetails(){
 	var aInventoryID = getUrlVars()["inventoryId"];
 	jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Inventory Record?</b></span>');
 	jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
 		buttons:{
 			"Submit": function(){
 				deleteInventory(aInventoryID); 
 				jQuery(this).dialog("close");
 			},
 			Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
 	return true;
 }

 function deleteInventory(theInventoryID){
 	$.ajax({
 		url: "./inventoryList/deleteInventory",
 		type: "POST",
 		data : {"inventoryID": theInventoryID},
 		success: function(data) {
 			jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Details Deleted.</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
 									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); document.location.href = "./inventory"; }}]}).dialog("open");
 		}
 	});
 }

 function updateInventoryDetails(){
	var aInventoryUpdateDetails = '';
 	if(!$('#inventoryDetailsFormId').validationEngine('validate')) {
 		return false;
 	}
 	var aTokenID = getUrlVars()["token"];
 	var aInactiveboxIDBox = $("#inactiveboxIDBox").is(':checked');
 	var aInventoryboxIDBox = $("#inventoryIDBox").is(':checked');
 	var aConsignboxIDBox = $("#consignmentIDBox").is(':checked');
 	var aPOBoxboxIDBox = $("#poIDBox").is(':checked');
 	var aSOBoxboxIDBox = $("#soIDBox").is(':checked');
 	var aInvoiceboxIDBox = $("#invoiceIDBox").is(':checked');
 	var aPickTicketboxIDBox = $("#pickTicketIDBox").is(':checked');
 	var aTaxboxIDBox = $("#taxableIDBox").is(':checked');
 	var aSingleboxIDBox = $("#singleItemIDBox").is(':checked');
 	var aLabloyrboxIDBox = $("#labloyrIDBox").is(':checked');
 	var aAssembilboxIDBox = $("#assembliesIDBox").is(':checked');
 	var aSerializzeboxIDBox = $("#serialisedIDBox").is(':checked');
 	var aInventoryDetails = $("#inventoryDetailsFormId").serialize();
 	var aPrMasterID = getUrlVars()["inventoryId"];
 	if(aTokenID === 'view'){
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 	}else if(aTokenID === 'new'){
 		aPrMasterID = "";
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 	}
 	$.ajax({
 		url: "./inventoryList/updateInventoryDetails",
 		type: "POST",
 		data : aInventoryUpdateDetails,
 		success: function(data) {
 			if(aTokenID === 'view'){
 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Details Updated.</b></span>');
 			}else if(aTokenID === 'new'){
 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Added Successfully.</b></span>');
 			}
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
 				buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); document.location.href = "./inventory"; }}]}).dialog("open");
 		},
 		error: function(jqXHR, textStatus, errorThrown){
			var errorText = $(jqXHR.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: ' + errorText + '</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
									buttons: [{height:27,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
 	});
 }

$(function() { var cache = {}; var lastXhr='';
$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
	select: function (event, ui) {
	var aValue = ui.item.value;
	var valuesArray = new Array();
	valuesArray = aValue.split("|");
	$("#inventorysearch").val(valuesArray[1]);
	var id = valuesArray[0];
	var code = valuesArray[2];
	location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
},
open: function(){ 
	$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	 },
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/searchinventory", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#primaryVendorId" ).autocomplete({ minLength: 3,timeout :1000, clearcache : true,
select: function( event, ui ) { var id = ui.item.id; $("#vendorId").val(id); },
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "inventoryList/vendorListAuto", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
	});
});

function loadProductURL(url){
	if (url.indexOf("http") === -1){
		url = "http://"+url;
	}
	if (isValidUrl(url)) {
		window.open(url, '_blank');
	} else {
		$("#UrlValidationMsg").html('<span style="color:red;">Alert: URL is invalid. Please provide a valid URL.</span>');
		$("#UrlValidationMsg").show().delay(3000).fadeOut();
		return false;
	}
}

function openResetAvgCostForm(){
	$("#newAvgCostInput").val("");
	$("#resetFields").slideDown();
	
	//newAvgCostInput
}

function cancelReset() {
	$("#resetFields").slideUp();
}
function resetAvgCost() {
	var aInventoryId = $("#prMasterID").text();
	var aNewAvgCost = $("#newAvgCostInput").val();
	if(aNewAvgCost === ""){
		return false;
	}
	var token = "resetAvgCost";
	var aRequestArray = new Array();
	aRequestArray.push(aInventoryId);
	aRequestArray.push(aNewAvgCost);
	var result = ajaxUpdateInventory(token, aRequestArray);
	$("#avgCostId").text(formatCurrency(aNewAvgCost));
}

function ajaxUpdateInventory(token, requestArray){
	$.ajax({
 		url: "./inventoryList/updateInventoryCustom",
 		type: "POST",
 		data : {"token":token, "requestArray":requestArray},
 		success: function(data) {
 			$("#resetFields").slideUp();
 			return data;
 		},
 		error: function(jqXHR, textStatus, errorThrown){
			var errorText = $(jqXHR.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: ' + errorText + '</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
									buttons: [{height:27,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
 	});
}

function isValidUrl(str) {
	if(str === ""){
		return false;
	}
	var pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
			'((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
			'((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
			'(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
			'(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
			'(\\#[-a-z\\d_]*)?$','i'); // fragment locator
	if(!pattern.test(str)) {
		return false;
	} else {
		return true;
	}
}