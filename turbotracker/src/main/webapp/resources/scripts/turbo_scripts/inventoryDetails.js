var gridSelectedID;
var gridOnOrderSelectedID;
var inventory = 'InventoryPage';
var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
	var weight = $('#weightID').text();
	console.log(weight);
	var weights = weight.split('.');
	
	if(weights!="" && weights!=null){
		$('#weightId').val(weights[0]);
		$('#ouncesId').val(weights[1].substring(0, 2));
	}
	
	
	$("#searchJob").attr("placeholder","Minimum 2 characters required to get List");
	
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
 	
 	if($('#prPriceLevel0').text() ==''){
 		$('#RetailId').css("display","none");
 		$('#retailPercent').css("display","none");
 		
 	}
 	if($('#prPriceLevel1').text() ==''){
 		$('#wholeSaleId').css("display","none");
 		$('#wholeSalePercent').css("display","none");
 		
 	}
 	if($('#prPriceLevel2').text() ==''){
 		$('#dealerId').css("display","none");
 		$('#dealerPercent').css("display","none");
 		
 	}
 	if($('#prPriceLevel3').text() ==''){
 		$('#special1Id').css("display","none");
 		$('#special1Percent').css("display","none");
 		
 	}
 	if($('#prPriceLevel4').text() ==''){
 		$('#distributorId').css("display","none");
 		$('#distPercent').css("display","none");
 		
 	}
 	if($('#prPriceLevel5').text() ==''){
 		$('#special2Id').css("display","none");
 		$('#sp2Percent').css("display","none");
 	}
 	calculatePercentage();
 	
 	setTimeout(function() { 
 	    editInvGlobalForm = $("#inventoryDetailsFormId").serialize();
 	  }, 2000);
 	
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
 			createtpusage('Inventory','Inventory Product Delete','Info','Inventory,Product Delete,theInventoryID:'+theInventoryID);
 			jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Details Deleted.</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
 									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");var checkpermission=getGrantpermissionprivilage('Inventory',0);
 									if(checkpermission){ var checkpermission=getGrantpermissionprivilage('Inventory',0);
 									if(checkpermission){document.location.href = "./inventory";} }}}]}).dialog("open");
 		}
 	});
 }
 
 $('#ouncesId').bind("cut copy paste",function(e) {
     e.preventDefault();
 });
 $('#weightId').bind("cut copy paste",function(e) {
     e.preventDefault();
 });
 
 $( "#weightId" ).keyup(function(event) {
	 if (keyCode >= 48 && keyCode <= 57) {
		 event.preventDefault();
		 return false;
	}
 });
 
 
 $("#weightId").keydown(
		 function(e) {
			try{
		 // alert(e.keyCode);
		 // Allow: backspace, delete, tab, escape, enter and .
		 if ($.inArray(e.keyCode, [ 46, 8, 9, 27, 13 ]) !== -1 ||
		 // Allow: Ctrl+A
		 (e.keyCode == 65 && e.ctrlKey === true) ||
		 // Allow: home, end, left, right
		 (e.keyCode >= 35 && e.keyCode <= 39)) {
		 // let it happen, don't do anything
		 return;
		 }
		 // Ensure that it is a number and stop the keypress
		 // if((e.keyCode=='110') || (e.keyCode=='190')){
		 // alert(''+e.keyCode);
		 // }
		 if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57))
		 && (e.keyCode < 96 || e.keyCode > 105)) {
		 e.preventDefault();
		 }
			}
			catch(e){
				console.log(e.message());
			}
		 });
 
 $("#ouncesId").keydown(
		 function(e) {
			try{
		 // alert(e.keyCode);
		 // Allow: backspace, delete, tab, escape, enter and .
		 if ($.inArray(e.keyCode, [ 46, 8, 9, 27, 13 ]) !== -1 ||
		 // Allow: Ctrl+A
		 (e.keyCode == 65 && e.ctrlKey === true) ||
		 // Allow: home, end, left, right
		 (e.keyCode >= 35 && e.keyCode <= 39)) {
		 // let it happen, don't do anything
		 return;
		 }
		 // Ensure that it is a number and stop the keypress
		 // if((e.keyCode=='110') || (e.keyCode=='190')){
		 // alert(''+e.keyCode);
		 // }
		 if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57))
		 && (e.keyCode < 96 || e.keyCode > 105)) {
		 e.preventDefault();
		 }
			}
			catch(e){
				console.log(e.message());
			}
		 });

 $( "#ouncesId" ).keyup(function(event) {
	 var ounceValue = $("#ouncesId" ).val();
	 var ounceLength = ounceValue.length;
	 console.log('OunceValue: '+ounceValue+" ounceLength: "+ounceLength);
	 if(parseInt(ounceValue)>15){
		 jQuery(newDialogDiv).html('<span><b style="color:red;">Ounces Values not more than 15.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");
									$( "#ouncesId" ).val('');
									$( "#ouncesId" ).focus();
									}}]}).dialog("open");
	 }
	
	 });

function saveInventoryDetails(){
	
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
 	console.log('aInventoryDetails :: '+aInventoryDetails);
 	var aPrMasterID =  $("#prMasterID").text();
 	
 	if(aTokenID === 'new' && $("#prWarehouseInventoryId").text()==""){
 		aPrMasterID = "";
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 		console.log("new");
 		
 		//window.history.replaceState(null, null , "./inventoryDetails?token=view&inventoryId="+prMasterID111+"&itemCode="+itemCode111);
 	}
 	else{
 		
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 		console.log("update");
 	} 
 	
 	var checkvalidation=checkinventoryvalidation();
 	if(checkvalidation){
 	$.ajax({
 		url: "./inventoryList/updateInventoryDetails",
 		type: "POST",
 		data : aInventoryUpdateDetails,
 		 success: function(data) {
             setTimeout(function(){
                    $("#Inventoryerrordiv").text("");
    },3000);
             if(aTokenID === 'new' && $("#prWarehouseInventoryId").text()==""){

            	 $("#Inventoryerrordiv").text("Inventory Added Successfully.");
            	 $("#prMasterID").empty();
                 $("#prMasterID").append(data.prMasterId);
                 $("#prWarehouseInventoryId").text(data.prMasterId);
                 createtpusage('Inventory','Inventory Product Save','Info','Inventory,Product Save,prMasterID:'+data.prMasterId);
                  
                    //jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Details Updated.</b></span>');
            }else
            	{
            	  $("#Inventoryerrordiv").text("Inventory Details Updated.");
            	  createtpusage('Inventory','Inventory Product Update','Info','Inventory,Product Update,prMasterID:'+data.prMasterId);
                    //jQuery(newDialogDiv).html('<span><b style="color:Green;">Inventory Added Successfully.</b></span>');
            }
            calculatePercentage();
            /*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.",
                    buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");}}]}).dialog("open");*/
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
}
function checkinventoryvalidation(){
	var returnvalue=true
	var departmentid=$("#departmentId").val();
	if(departmentid==0 ||departmentid=="" || departmentid=="-1"){
		$("#Inventoryvalidatediv").text("*required Department");
		  setTimeout(function(){
			  $("#Inventoryvalidatediv").text("");
		  },3000);
		
		returnvalue=false;	
	}
	return returnvalue;
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
 	var aPrMasterID = $("#prMasterID").text();//getUrlVars()["inventoryId"];
 	
 	if(aTokenID === 'new' && $("#prWarehouseInventoryId").text()==""){

 		aPrMasterID = "";
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 		console.log("new");
 		
 	}else
 		{
 		aInventoryUpdateDetails = aInventoryDetails+"&inactiveboxNameBox="+aInactiveboxIDBox+"&inventoryNameBox="+aInventoryboxIDBox+"&consignmentNameBox="+aConsignboxIDBox+"&poNameBox="+aPOBoxboxIDBox
												 		+"&soNameBox="+aSOBoxboxIDBox+"&invoiceNameBox="+aInvoiceboxIDBox+"&pickTicketNameBox="+aPickTicketboxIDBox+"&prMasterID="+aPrMasterID
												 		+"&taxableNameBox="+aTaxboxIDBox+"&singleItemNameBox="+aSingleboxIDBox+"&labourNameBox="+aLabloyrboxIDBox+"&assembliesBox="+aAssembilboxIDBox
												 		+"&serialisedBox="+aSerializzeboxIDBox+"&token="+aTokenID;
 		console.log("update"+aPrMasterID);
 	 
 	}
 	
 	var checkvalidation=checkinventoryvalidation();
 	if(checkvalidation){
 	$.ajax({
 		url: "./inventoryList/updateInventoryDetails",
 		type: "POST",
 		data : aInventoryUpdateDetails,
 		success: function(data) {
 			if(aTokenID === 'new' && $("#prWarehouseInventoryId").text()==""){
 				 createtpusage('Inventory','Inventory Product Save','Info','Inventory,Product Save,prMasterID:'+data.prMasterId);
 				var checkpermission=getGrantpermissionprivilage('Inventory',0);
				if(checkpermission){
				var gridposition=getUrlVars()["gridposition"];
 				document.location.href = "./inventory?gridposition="+gridposition; 
				}
 			}else 
 				{
 				 createtpusage('Inventory','Inventory Product Update','Info','Inventory,Product Update,prMasterID:'+data.prMasterId);
 				var checkpermission=getGrantpermissionprivilage('Inventory',0);
				if(checkpermission){
				var gridposition=getUrlVars()["gridposition"];
 				document.location.href = "./inventory?gridposition="+gridposition;
				}
 			}
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
 }

$(function() { var cache = {}; var lastXhr='';
$( "#searchJob" ).autocomplete({ minLength: 2,timeout :1000,
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

function getSearchDetails()
{
	console.log("ItemCode is--------------------->"+$('#searchJob').val());
	if($('#searchJob').val() != null && $('#searchJob').val() != '')
	{
		var gridposition=getUrlVars()["gridposition"];
		location.href="./inventory?token=view&gridposition="+gridposition+"&key="+$('#searchJob').val();
		return true;
	}
	else
		return false;
		
}

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

function loadInventoryDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	try {
		 $('#inventoryDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#inventoryDetailsPager'),
				url:'./inventoryList/getItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['cuSoid','cuSodetailId','Order Date', 'Date Promised','Customer','Job', 'Warehouse','Quantity Ordered','RxCustomerID'],
				colModel :[
						{name:'cuSoid', index:'cuSoid', align:'center', width:40, editable:true,hidden:true},
						{name:'cuSodetailId', index:'cuSodetailId', align:'center', width:40, editable:true,hidden:true},
						{name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'datePromised', index:'datePromised', align:'center', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'name', index:'name', align:'center', width:80,hidden:false},
						{name:'jobName', index:'jobName', align:'center', width:30,hidden:false},
						{name:'city', index:'city', align:'center', width:40,hidden:false},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:40,hidden:false},
						{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:40, editable:true,hidden:true}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			pager: '#inventoryDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 945,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
		    },
			loadError : function (jqXHR, textStatus, errorThrown){	},
			jsonReader : {
	            root: "rows",
	            page: "page",
	            total: "total",
	            records: "records",
	            repeatitems: false,
	            cell: "cell",
	            id: "id",
	            userdata: "userdata"
	    	},
			onSelectRow: function(id){
				console.log("Selected id---->"+id);
				gridSelectedID = id;
			},
	    	ondblClickRow: function(rowid) {

				/*var rowData = jQuery(this).getRowData(rowid); 
				var acuSoid = rowData['vePOID'];
				var aMasterID = rowData['rxVendorID'];
				$('#rxCustomer_ID').text(aMasterID);
				$('#Cuso_ID').text(acuSoid);
				$('#operation').val('update');
				PreloadData(acuSoid);
				$('#salesrelease').dialog("open");*/
				
	    	}
		}).navGrid('#inventoryDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);
	 /*$('#inventoryDetailsGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#inventoryDetailsPager'),
			url:'./inventoryList/getItemDetails',
			postData: {'prMasterID':prMasterID},
			colNames:['cuSoid','cuSodetailId','Order Date', 'Date Promised','Customer','Job', 'Warehouse', 'Quantity Ordered'],
			colModel :[
					{name:'cuSoid', index:'cuSoid', align:'left', width:40, editable:true,hidden:true},
					{name:'cuSodetailId', index:'cuSodetailId', align:'left', width:40, editable:true,hidden:true},
					{name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
					{name:'datePromised', index:'datePromised', align:'left', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
					{name:'name', index:'name', align:'', width:80,hidden:false},
					{name:'jobName', index:'jobName', align:'center', width:30,hidden:false},
					{name:'city', index:'city', align:'', width:40,hidden:false},
					{name:'quantityOrdered', index:'quantityOrdered', align:'right', width:40,hidden:false}],
					rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
					sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
					height:210,	width: 945, rownumbers:true, altRows: true, altclass:'myAltRowClass',pager: '#inventoryDetailsPager',
					
			jsonReader : {
				root: "rows",
				page: "page",
				total: "total",
				records: "records",
				repeatitems: false,
				cell: "cell",
				id: "id",
				userdata: "userdata"
			},
			loadComplete: function(data) {
				$("#inventoryDetailsGrid").setSelection(1, true);
			},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				var rowData = jQuery(this).getRowData(id); 
				var cuSODetailId = rowData["cuSodetailId"];
				$('#jobCustomerName_ID').text(cuSODetailId);
			},
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
				 //console.log(e);
				},
			editurl:"./salesOrderController/manpulateSOReleaseLineItem"
	 }).navGrid('#inventoryDetailsPager',//{cloneToTop:true},
				{add:false,edit:false,del:false,refresh:true,search:false}
		);*/
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}

function showSOPopup()
{
	if(typeof(gridSelectedID) !== undefined && gridSelectedID !== null && gridSelectedID !== '')
	{
		var rowData = jQuery("#inventoryDetailsGrid").getRowData(gridSelectedID); 
		var acuSoid = rowData['cuSoid'];
		var rxMasterid = rowData['rxCustomerId'];
		console.log("CusoID--->"+acuSoid);
		
		$('#operation').val('update');
		if(rxMasterid != null && rxMasterid !='')
			$('#rxCustomer_ID').text(rxMasterid);
		$('#Cuso_ID').text(acuSoid);
		PreloadDataFromInventory(acuSoid);
		//$('#salesrelease').dialog({height:710});
		$('#salesrelease').dialog("open");
		//document.location.href ="./salesorder?oper=update&Cusoid="+acuSoid+"&rxMasterid="+rxMasterid;
	}
	
}

function loadOnOrderDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	try {
		 $('#OnOrderDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#OnOrderDetailsPager'),
				url:'./inventoryList/getOnOrderItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['vePOID','PO #','Order Date', 'Est. Ship Date','Vendor', 'Warehouse', 'Quantity Ordered'],
				colModel :[
						{name:'vePOID', index:'vePOID', align:'center', width:40,hidden:true},
						{name:'ponumber', index:'ponumber', align:'center', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'estimatedShipDate', index:'estimatedShipDate', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'vendorName', index:'vendorName', align:'center', width:80,hidden:false},
						{name:'city', index:'city', align:'center', width:40,hidden:false},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:40,hidden:false}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			pager: '#OnOrderDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 945,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
		    },
			loadError : function (jqXHR, textStatus, errorThrown){	},
			jsonReader : {
	            root: "rows",
	            page: "page",
	            total: "total",
	            records: "records",
	            repeatitems: false,
	            cell: "cell",
	            id: "id",
	            userdata: "userdata"
	    	},
			onSelectRow: function(id){
				console.log("Selected id---->"+id);
				gridOnOrderSelectedID = id;
			},
	    	ondblClickRow: function(rowid) {

				/*var rowData = jQuery(this).getRowData(rowid); 
				var acuSoid = rowData['vePOID'];*/
				/*$('#rxCustomer_ID').text(aMasterID);
				$('#Cuso_ID').text(acuSoid);
				$('#operation').val('update');
				PreloadData(acuSoid);
				$('#salesrelease').dialog("open");*/
				
	    	}
		}).navGrid('#OnOrderDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);	 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}
function showPOPopup()
{
	if(typeof(gridOnOrderSelectedID) !== undefined && gridOnOrderSelectedID !== null && gridOnOrderSelectedID !== '')
	{
		var rowData = jQuery("#OnOrderDetailsGrid").getRowData(gridOnOrderSelectedID); 
		var avePOID = rowData['vePOID'];
		var aQryStr = "aVePOId=" + avePOID;
		console.log("CusoID--->"+avePOID);
		var checkpermission=getGrantpermissionprivilage('Purchase_Orders',0);
		if(checkpermission){
		document.location.href = "./editpurchaseorder?token=view&" + aQryStr;
		}	
		
		//document.location.href ="./salesorder?oper=update&Cusoid="+acuSoid+"&rxMasterid="+rxMasterid;
	}
	
}
function loadOnHandDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	
	try {
		 $('#OnHandDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#OnHandDetailsPager'),
				url:'./inventoryList/getOnHandItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['Warehouse','Inventory On Hand','Inventory Allocated', 'Inventory Available','Inventory On Order',],
				colModel :[
						{name:'city', index:'city', align:'center', width:40, editable:true,hidden:false},
						{name:'inventoryOnHand', index:'inventoryOnHand', align:'center', width:60,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'inventoryAllocated', index:'inventoryAllocated', align:'center', width:60,hidden:false},						
						{name:'inventoryAvailable', index:'inventoryAvailable', align:'center', width:60,hidden:false},
						{name:'inventoryOnOrder', index:'inventoryOnOrder', align:'center', width:60,hidden:false}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			pager: '#OnHandDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 945,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
		    },
			loadError : function (jqXHR, textStatus, errorThrown){	},
			jsonReader : {
	            root: "rows",
	            page: "page",
	            total: "total",
	            records: "records",
	            repeatitems: false,
	            cell: "cell",
	            id: "id",
	            userdata: "userdata"
	    	},
			onSelectRow: function(id){
				
			},
	    	ondblClickRow: function(rowid) {			
				
	    	}
		}).navGrid('#OnHandDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);	 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}
function formatCurrencynodollar(strValue)
{
	if(strValue === "" || strValue == null){
		return "0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + dblValue + '.' + strCents);
}

function convert2places(num) {
	num = String(num);
	if(num.indexOf('.') !== -1) {
	var numarr = num.split(".");
	if (numarr.length == 1) {
	return Number(num);
	}
	else {
	return Number(numarr[0]+"."+numarr[1].charAt(0)+numarr[1].charAt(1));
	}
	}
	else {
	return Number(num);
	}
	}

	

function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
	return cellValue;
}

function showPriceTier()
{	
	$('#jqgridInventoryDetails').hide();
	$('#jqgridOnOrderDetails').hide();
	$('#jqgridOnHandDetails').hide();
	$('#tierPricingDetails').show();
	$('.tabs_main').css("height","825px");
}

function calculatePercentage(){
	/**
 	 * Implemented for Req : id 81
 	 * Percentage 
 	 *
 	 * */
 	try{
 	var factoryCost = $('#factoryCostId').val().replace(/[^0-9\.]+/g,"");
 	var multiplier = $('#multiplierId').val().replace(/[^0-9\.]+/g,"");
 	if(isNaN(factoryCost)){
 		factoryCost=0;
 	}
 	if(isNaN(multiplier)){
 		multiplier=0;
 	}
 	 var mulValue = multiplier*factoryCost;
 
 	
 	if(Number($('#RetailId').val())!=0){
 		$('#retailPercent').text(getPerecntage($('#RetailId').val(),mulValue)+'%');	
 	}else{
 		$('#retailPercent').text('0%');
 	}
 	if(Number($('#wholeSaleId').val())!=0){

 		$('#wholeSalePercent').text(getPerecntage($('#wholeSaleId').val(),mulValue)+'%');
 	}else{
 		$('#wholeSalePercent').text('0%');
 	}
 	
 	 	if(Number($('#dealerId').val())!=0){
 	 	$('#dealerPercent').text(getPerecntage($('#dealerId').val(),mulValue)+'%');
 	 	}else{
 	 		$('#dealerPercent').text('0%');
 	 	}
 	 	
 	 	if(Number($('#special1Id').val())!=0){
 	 		$('#special1Percent').text(getPerecntage($('#special1Id').val(),mulValue)+'%');
 	 	}else{
 	 		$('#special1Percent').text('0%');
 	 	}
 	 	

 	 	
 	 	 	if(Number($('#distributorId').val())!=0){
 	 	
 	 	 		$('#distPercent').text(getPerecntage($('#distributorId').val(),mulValue)+'%');
 	 	 	}else{
 	 	 		$('#distPercent').text('0%');
 	 	 	}
 	 	
 	 	 if(Number($('#special2Id').val())!=0){
 	 	 		$('#sp2Percent').text(getPerecntage($('#special2Id').val(),mulValue)+'%');
 	 	 			
 	 	 	}else{
 	 	 		$('#sp2Percent').text('0%');
 	 	 	}
 	
 	}catch(err){
 		
 	}
}
/***
 * Calculating percentage for Tier Pricing
 * 
 */
function getPerecntage(textValue,factoryCost){
	
	var result = parseFloat((Math.abs(textValue-factoryCost)/textValue)*100).toFixed(2);
	return result;
}