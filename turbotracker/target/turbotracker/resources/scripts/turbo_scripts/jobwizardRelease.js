/** document ready Function for Release Tab **/
var aReleaseDialogVar;
var aPurchaseOrderVar;
var  aShipDialogVar;
var errorText;
var estimatedamountRelease;
var newDialogDiv = jQuery(document.createElement('div'));
var aDate = new Date();
var currentMonth = aDate.getMonth()+1;
var currenDate = currentMonth+"/"+aDate.getDate()+"/"+aDate.getFullYear();
jQuery(document).ready(function(){
	$(".datepicker").datepicker();
	$(".vendorInvDatepicker").datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	setTimeout("loadReleaseGrid();", 1);
	loadShipingGrid();
	$("#OriJobNumber").css("display", "none");
	var aQuoteNumber = $("#quoteNumber").val();
	if(aQuoteNumber !== ''){
		$("#OriJobNumber").show();
	}
	$("#datedID").val(currenDate);
	$("#postDateID").css("display", "none");
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Booked"){
		$(".customerNameField").attr("disabled", true);
	}
	$("#loadingPODiv").css({"visibility": "hidden"});
	//loadVendorInvoice(vePOID);
	estimatedamountRelease =$("#contractAmount_release").val();
	if(typeof estimatedamountRelease != 'undefined' && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
		$("#estimate").empty();
		estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.]+/g,"");
		$("#estimate").text(formatCurrency(estimatedamountRelease));
	}
	
	$( "#ReleasesID" ).datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	$( "#releasesTypeID" ).change(function() {
		if($(this).val()==2){
			$('#ReleaseTable tr:nth-child(7)').hide();
			$('#ManufacturerId').val(0);
			$('#ReleasesManuID').val('SalesOrder');
		}else{
			$('#ReleaseTable tr:nth-child(7)').show();
			$('#ManufacturerId').val('');
			$('#ReleasesManuID').val('');
		}
		});
});

/** cancel dialog for Vendor Invoice **/
function cancelvendorInvoice(){
	$("#release").trigger("reloadGrid");
	jQuery("#openvendorinvoice").dialog("close");
	return false;
}

/** Currency formatter **/
function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}
	
/** dialog for Vendor Invoice **/
jQuery(function(){
	jQuery("#openvendorinvoice").dialog({
		autoOpen:false,
		width:880,
		modal:true,
		close:function(){ $("#release").trigger("reloadGrid"); return true;}	
	});
}); 
	
/** load release grid with Data **/
function loadReleaseGrid() {
	release_grid (arrColNamesRelease, arrColModelRelease);
	return true;
}

/** release grid columns with Data **/
var arrColNamesRelease = [" "," ","ReleaseId","Released","Type","ManufacturerId","rxMasterId","Manufacturer","Note","Allocated", "JoDetailedID", "VePOID","CusoID", "Bill Note", "Web Sight" , "PONumber", "ShipTo", "rxContact", 'Email Time', 'BillToAdd', 'ShipToAdd', 'AddressID', 'CustomerPONumber', 'POID'];
var arrColModelRelease = [
                          	{name:'ponumber',index:'ponumber',align:'center',width:8,editable: false,hidden: false, formatter: alphabetSeq},
                          	{name:'billNoteImage',index:'billNoteImage',align:'right',width:8,editable: false,hidden: true, formatter: billImage},
                          	{name:'joReleaseId', index:'joReleaseId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'released', index:'released', align:'center', width:20, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'type', index:'type', align:'center', width:30,editable:true,hidden:false, edittype:'select',formatter:typeFormatter, editoptions:{value:{1:'Drop Ship',2:'Stock Order',3:'Bill Only',4:'Commission',5:'Service'}}},
							{name:'manufacturerId', index:'manufacturerId', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxMasterId', index:'rxMasterId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'manufacturer', index:'manufacturer', align:'left', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'note', index:'note', align:'left', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'estimatedBilling', index:'estimatedBilling', align:'right', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}, formatter:customCurrencyFormatter},
							{name:'joReleaseDetailid', index:'joReleaseDetailid', align:'left', width:30, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'vePoId', index:'vePoId', align:'left', width:20, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'CusoID', index:'CusoID', align:'left', width:20, editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'billNote', index:'billNote', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'webSight', index:'webSight', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'ponumber', index:'ponumber', align:'left', width:20, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'shipToMode', index:'shipToMode', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxVendorContactID', index:'rxVendorContactID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'emailTimeStamp', index:'emailTimeStamp', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'billToAddressID', index:'billToAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'shipToAddressID', index:'shipToAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'rxAddressID', index:'rxAddressID', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'customerPONumber', index:'customerPONumber', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
							{name:'poid', index:'poid', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}}];

var colName_poAmount = "PO Amount";
var colModel_poAmount = {name:'po', index:'po', align:'right', width:30 , formatter:customCurrencyFormatter};

var colName_InvoiceAmount = "Invoice Amount";
var colModel_InvoiceAmount = {name:'invoiceAmount', index:'invoiceAmount', align:'right', width:30, formatter:customCurrencyFormatter};

/** release grid columns hide/show function **/

function checkPoAmount() {
	var poAmt = document.getElementsByClassName("PoCheck");
	for(var i=0;i<poAmt.length;i++) {
		var box = poAmt[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "Released" && box.value != "Type" && box.value != "Manufacturer" && box.value != "Note" && box.value != "Allocated" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "poAmount" && !isExistInArray(arrColNamesRelease, "poAmount")) {
					arrColNamesRelease.push(colName_poAmount);
					arrColModelRelease.push(colModel_poAmount);
				}
			}
		}else if (box.type === "checkbox" && !(box.checked)) {
			if (box.value === "poAmount"){
				removeA(arrColNamesRelease,colName_poAmount);
				removeA(arrColModelRelease,colModel_poAmount);
			}
		}
	}
	$("#release").jqGrid('GridUnload');
	release_grid(arrColNamesRelease, arrColModelRelease);
	$("#release").trigger("reloadGrid");
	return true;
}


function checkInvoAmount() {
	var poAmt = document.getElementsByClassName("InvoCheck");
	for(var i=0;i<poAmt.length;i++) {
		var box = poAmt[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "Released" && box.value != "Type" && box.value != "Manufacturer" && box.value != "Note" && box.value != "Allocated" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "invoiceAmount" && !isExistInArray(arrColNamesRelease, "invoiceAmount")) {
					arrColNamesRelease.push(colName_InvoiceAmount);
					arrColModelRelease.push(colModel_InvoiceAmount);
				  }
			}
		}else if (box.type === "checkbox" && !(box.checked)) {
			 if (box.value === "invoiceAmount"){
				removeA(arrColNamesRelease,colName_InvoiceAmount);
				removeA(arrColModelRelease,colModel_InvoiceAmount);
			}
		}
	}
	$("#release").jqGrid('GridUnload');
	release_grid(arrColNamesRelease, arrColModelRelease);
	$("#release").trigger("reloadGrid");
	return true;
}
/**
 * Javascript method to get/seperate alphabet sequence number from PoNumber.  
 * By default it will get all the alphabets.  So to het the exact Release letter, we need to split the POnumber at "-".
 * @param cellValue
 * @param options
 * @param rowObject
 * @returns {String} alphabet sequence of PO
 */
function alphabetSeq(cellValue, options, rowObject){
	try {
		 var element = "";
		 if(cellValue !== null){
			 var aPonumber=cellValue;
			 var aPONumSplitArray = new Array();
			 aPONumSplitArray = $.trim(aPonumber).split("-");
			 var aStr = aPONumSplitArray[1].match(/[A-z]/g);
			 for(var i=0;i<aStr.length;i++){
				 element = element + (aStr[i]);
			 }
		 }
		return element;
	} catch (e) {
		alert("Error:" + e + "\n\nFalse will be returned.");
		return false;
	}
} 

function billImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue !== '' && cellValue !== null){
	   element = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>";
   }else if(cellValue === ''){
	   element = "";
   }else if(cellValue === null){
	   element = "";
   }else{
	   element = "";
   }
return element;
} 

/** get release grid values **/
var release_grid = function (arrColNamesRelease, arrColModelRelease){
	$("#release").jqGrid({
		url:'./jobtabs5/release',
		datatype: 'JSON',
		mtype: 'GET',
		//pager: jQuery('#releasepager'),
		colNames : arrColNamesRelease,
		colModel : arrColModelRelease,
		rowNum: 0,	pgbuttons: false,/*autowidth: true,*/recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	
		height:240,	width: 1080, altRows: true, altclass:'myAltRowClass',
		postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }},
		loadComplete: function(data) {
			$("#release").setSelection(1, true);
			var grid = $("#release");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
			if(isNaN(aCustomerPONumber)===false){
				$("#PONumberID option[value=" + aCustomerPONumber + "]").attr("selected", true);
			}else{
				$("#PONumberID option[value=-1]").attr("selected", true);
			}
			billAmountCal();
			var allocateamount=$("#allocate").text();
			var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.]+/g,"");
			
			var sumTotal = '';
			var allocatedamount=allocateamount.replace(/[^0-9\.]+/g,"");
			sumTotal = Number(allocatedamount) + Number(unAllocatedAmount);
//			$("#estimate").empty();
//			$("#estimate").text(formatCurrency(sumTotal));
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
			var rowData = jQuery(this).getRowData(id); 
			var joDetailId = rowData["joReleaseDetailid"];
			var manufacturerID = rowData["rxMasterId"];
			var manufacturerName = rowData["manufacturer"];
			var PONumberID = rowData["ponumber"];
			var customerPONumber = rowData["customerPONumber"];
			var aCustomerPONumber = rowData["poid"];
			var cuInvoiceID = rowData["cuInvoiceID"];
			var vePOID = rowData["vePoId"];
			var shipToID = rowData["shipToMode"];
			var joReleaseId = rowData["joReleaseId"];
			var releaseType = rowData["type"];
			$("#shiping").jqGrid('GridUnload');
			loadShipingGrid(joDetailId,releaseType);
			$('#shiping').jqGrid('setGridParam',{postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }, "joDetailsID" : joDetailId}});
			$("#shiping").trigger("reloadGrid"); 
			$("#manufacture_ID").text(manufacturerID);
			$("#vendorinvoice1").jqGrid('GridUnload');
			//loadVendorInvoice(vePOID);
			$('#vendorinvoice1').jqGrid('setGridParam',{postData: {vePoId: function() { return vePOID; }}});
			$("#vendorinvoice1").trigger("reloadGrid");
			$("#customerInvoice_lineitems").jqGrid('GridUnload');
			$('#customerInvoice_lineitems').jqGrid('setGridParam',{postData: {cuInvoiceID: function() { return cuInvoiceID; }}});
			$("#customerInvoice_lineitems").trigger("reloadGrid");
			$("#manufacurterNameID").val(manufacturerName);
			$("#poNumberID").val("");
			$("#poNumberID").val(PONumberID);
			loadcusoID(joReleaseId);
			if(shipToID == 0){
				$("#shipTo").show();
				$("#shipTo1").hide();
			}else{
				$("#shipTo1").show();
				$("#shipTo").hide();
			}
			getVendorDetails(joDetailId, vePOID);
			$("#customerPONumberID").val("");
			$("#customerPONumberID").val(customerPONumber);
			if(isNaN(aCustomerPONumber)===false){
				$("#PONumberID option[value=" + aCustomerPONumber + "]").attr("selected", true);
			}else{
				$("#PONumberID option[value=-1]").attr("selected", true);
			}
		},
    	ondblClickRow: function(rowid) {
    		editreleasedialog();
    	}
	});
};

/** Shipping grid  column with values**/
 function loadShipingGrid(joDetailId,releaseType){
	var jobnumber = $("#jobNumber_ID").text();
	 $("#shiping").jqGrid({
	url:'./jobtabs5/shipping?jobNumber='+jobnumber+'&joDetailsID='+joDetailId+'&releaseType='+releaseType,
	datatype: 'JSON',
	mtype: 'GET',
	colNames:['Ship Date','Shipping Line ','Vendor Date','Vendor Invoice','Vendor Amount ($)','Customer Date','Customer Amount ($)', 'ShipID', 'JoReleaseID', 'VeBillID', 'CoAccID', 'CuInvoiceID'],
	colModel:[
		{name:'shipDate',index:'shipDate',align:'center',width:50},
		{name:'shippingLine',index:'shippingLine',align:'center',width:50},
		{name:'vendorDate',index:'vendorDate',align:'center',width:60}, 
		{name:'vendorInvoice',index:'vendorInvoice',align:'center',width:50}, 
		{name:'vendorAmount',index:'vendorAmount',align:'right',width:60, formatter:customCurrencyFormatter}, 
		{name:'customerDate',index:'customerDate',align:'center',width:70}, 
		{name:'customerAmount',index:'customerAmount',align:'right',width:60, formatter:customCurrencyFormatter},
		{name:'veShipViaID',index:'veShipViaID',align:'right',width:60, hidden: true},
		{name:'joReleaseDetailID',index:'veShipViaID',align:'right',width:60, hidden: true},
		{name:'veBillID',index:'veBillID',align:'right',width:60, hidden: true},
		{name:'coAccountID',index:'coAccountID',align:'right',width:60, hidden: true},
		{name:'cuInvoiceID',index:'cuInvoiceID',align:'right',width:60, hidden: true}],
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
		height:86,	width: 1080, altRows: true, altclass:'myAltRowClass',
		loadComplete: function(data) { $("#shiping").setSelection(1, true); },
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
				var rowData = jQuery(this).getRowData(id); 
				var cusoInvId = rowData["cuInvoiceID"];
				$('#cuinvoiceIDhidden').val(cusoInvId);
				CheckCustomerInvoiceSave();
				/*var rowData = jQuery(this).getRowData(id);
				var aShipViaId = rowData["veShipViaID"];
				var aVendorInvoice = rowData["vendorInvoice"];
				var aVendorDate = rowData["vendorDate"];
				var aShipDate = rowData["shipDate"];
				$("#shipViaSelectID option[value=" + aShipViaId + "]").attr("selected", true);
				$("#vendorInvoiceNum").val("");
				$("#vendorInvoiceNum").val(aVendorInvoice);
				$("#vendorDateID").val("");
				$("#vendorDateID").val(aVendorDate);
				$("#shipDateID").val("");
				$("#shipDateID").val(aShipDate);*/
			}
		});
  }

 function addShipDetails(){
	 var aTodayDate = new Date();
	 var curr_date = aTodayDate.getDate();
	 var curr_month = aTodayDate.getMonth()+1;
	 var curr_year = aTodayDate.getFullYear();
	 if(jQuery("#shiping").getGridParam("records") === 0){
		 var errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
						jQuery(this).dialog("close");
						 $("#vendorDateID").val("");
						 $("#vendorDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 document.getElementById('datedID').disabled = false;
						 $("#datedID").val("");
						 $("#datedID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 $("#dueDateID").val("");
						 $("#shipDateID").val("");
						 $("#shipDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
						 $("#postDateID").val("");
						 $('#openvendorinvoice').dialog('option', 'title', '');
						 $('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
						 jQuery("#openvendorinvoice").dialog("open");
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
		}}).dialog("open");
	 }else{
		 $("#vendorDateID").val("");
		 $("#vendorDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 document.getElementById('datedID').disabled = false;
		 $("#datedID").val("");
		 $("#datedID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 $("#dueDateID").val("");
		 $("#shipDateID").val("");
		 $("#shipDateID").val(curr_month + "/" + curr_date + "/" + curr_year);
		 $("#postDateID").val("");
		 $('#openvendorinvoice').dialog('option', 'title', '');
		 $('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
		 jQuery("#openvendorinvoice").dialog("open");
	 }
	 return true;
 }
 
 function deleteShipDetails(){
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Shipping Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var aVeShipViaID = grid.jqGrid('getCell', rowId, 'veShipViaID');
					var aJoReleaseDetailsID = grid.jqGrid('getCell', rowId, 'joReleaseDetailID');
					var aCuInvoiceID = grid.jqGrid('getCell', rowId, 'cuInvoiceID');
					var aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
					deleteShip(aVeShipViaID, aJoReleaseDetailsID, aVeBillID, aCuInvoiceID);
					jQuery(this).dialog("close");
					$("#release").trigger("reloadGrid");
				},
				Cancel: function ()	{jQuery(this).dialog("close");}
				}}).dialog("open");
			return true;
		}else{
			var errorText = "Please click one of the Shipping record to Delete.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
 }
 
 function deleteShip(aVeShipViaID, aJoReleaseDetailsID, aVeBillID, aCuInvoiceID){
	 $.ajax({
		url: "./jobtabs5/deleteShipDetails",
		type: "GET",
		data : {"shipDetailsID" : aVeShipViaID, "releaseID" : aJoReleaseDetailsID , "billID" : aVeBillID, 'cuInvoiceID' : aCuInvoiceID },
		success: function(data) {
			var errorText = "Shipping record deleted successfully.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color: green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#shiping").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
 }
 
 function changeBillingDetails(){
	var billAmount = $("#billing").is(':checked');
	if(billAmount === false){
		billAmountCalUnchk();
	}else{
		billAmountCal();
	}
 }

 function billAmountCal(){
	 $('#release').jqGrid('getGridParam', 'userData');
	var allRowsInGrid = $('#release').jqGrid('getRowData');
	var allocateamount=$("#allocate").text();
	var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.]+/g,"");
	var aVal = new Array();
	var sum = 0;
	$.each(allRowsInGrid, function(index, value) { 
		aVal[index] = value.estimatedBilling;
		sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.]+/g,""));
	});
	if(typeof estimatedamountRelease != 'undefined' && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
		estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.]+/g,"");
		$("#estimated").text(formatCurrency(estimatedamountRelease));
		$('#allocate').empty();
		$("#allocate").text(formatCurrency(sum));
		$('#unAllocated').text(formatCurrency(estimatedamountRelease-sum));
	}
	var sumTotal = '';
	var allocatedamount=allocateamount.replace(/[^0-9\.]+/g,"");
	if(unAllocatedAmount > allocatedamount){
		sumTotal = Number(allocatedamount) - Number(unAllocatedAmount);
	}if(allocatedamount > unAllocatedAmount){
		sumTotal = Number(unAllocatedAmount) - Number(allocatedamount);
	}
//	$("#estimate").empty();
//	$("#estimate").text(formatCurrency(sumTotal));
 }
  
 function billAmountCalUnchk(){
	 $('#release').jqGrid('getGridParam', 'userData');
	var allRowsInGrid = $('#release').jqGrid('getRowData');
	var aVal = new Array();
	var sum = 0;
	$.each(allRowsInGrid, function(index, value) { 
		aVal[index] = value.estimatedBilling;
		sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.]+/g,""));
	});
	var billAmount = $("#estimatedCost").val();
	if(billAmount <sum ){
		jQuery(newDialogDiv).html('<span><b style="color:Green;">Allocated billing exceeds contract amount.</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 	$('#estimated').empty();	//$("#unAllocated").empty();
								$('#allocate').empty();
								$("#estimated").text(formatCurrency(sum)); 
								$("#allocate").text(formatCurrency(sum)); }}]}).dialog("open");
		return false;
	}else{
		$('#estimated').empty();	
		$('#allocate').empty();
		//$("#unAllocated").empty();
		/*var unAllocated = Number(billAmount) - Number(sum);
		unAllocated =unAllocated.replace(/[^0-9\.]+/g,""); */
		var estimatedcost=$("#estimatedCost").val();
		estimatedcost=estimatedcost.replace(/[^0-9\.]+/g,"");
		$("#estimated").text(formatCurrency(estimatedcost));
		$("#allocate").text(formatCurrency(sum));
//		$("#unAllocated").text(formatCurrency(unAllocated));
	}
	return true;
 }
 
 /** formatter for release grid **/
function typeFormatter(cellvalue, options, rowObject) {
	if(cellvalue === null){
		return "";
	}else if(cellvalue === -1){
		return "";
	}else if(cellvalue === 1) {
		return "Drop Ship";
	} else if(cellvalue === 2) {
		return "Stock Order";
	} else if(cellvalue === 3) {
		return "Bill Only";
	} else if(cellvalue === 4) {
		return "Commission";
	} else if(cellvalue === 5) {
		return "Service";
	}
}
		
/** hide/remove column function **/
function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}

/** remove column function **/
function removeA(arr){
    var what, a = arguments, L = a.length, ax;
    while(L> 1 && arr.length){
        what = a[--L];
        while((ax= arr.indexOf(what)) != -1){
            arr.splice(ax, 1);
        }
    }
    return arr;
}

/** Dialog box for Add/Edit Release **/
jQuery(function(){
	jQuery("#openReleaseDig").dialog({
		autoOpen:false,
		width:400,
		title:"Add/Edit Release",
		modal:true,
		buttons:{	},
		closeOnEscape: false,
		close:function(){$('#openReleaseDigForm').validationEngine('hideAll'); return true;}	
	});
}); 

/*$("#ReleasesManuID").keypress(function() {
	$(function() { var cache = {}; var lastXhr='';
	$( "#ReleasesManuID" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; $("#ManufacturerId").val(id);},
		source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
		lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
			cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
			error: function (result) {
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	if($("ul.ui-autocomplete").is(":visible")){
	} else {
		$(".ui-autocomplete").show();
	}
	$(".ui-autocomplete").show();
});*/

/** add Function for Release **/
function addRelease(){
	$('#ReleaseTable tr:nth-child(7)').show();
	$('#ManufacturerId').val('');
	$('#ReleasesManuID').val('');
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Booked"){ 
	var releaseCount = $("#release").getGridParam("reccount");
	if(releaseCount === 0) {
		var validNewRelease = validateNewRelease();
		if(!validNewRelease) {
			jQuery(newDialogDiv).html('<span><b style="color:red;">Credit approval required.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
     		}
    	}
	}
	else {
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	aReleaseDialogVar = "add";
	document.getElementById("openReleaseDigForm").reset();
	var aPOAmount = $("#poAmountID").is(':checked');
	var aInvoiceAmount = $("#invoiceAmountID").is(':checked');
	if(aPOAmount === true){
		$("#poAmountFieldID").show();
	}else{
		$("#poAmountFieldID").css("display", "none");
	}
	if(aInvoiceAmount === true){
		$("#invoiceAmountFieldID").show();
	}else{
		$("#invoiceAmountFieldID").css("display", "none");
	}
	// 07/29/2013
	var date = new Date();
    var curr_date = date.getDate();
    var curr_month = date.getMonth() + 1; //Months are zero based
    var curr_year = date.getFullYear();
    var formattedDate = curr_month + "/" + curr_date + "/" + curr_year;
	$("#ReleasesID").val(formattedDate);
	var aCustomerPO = $("#customerpodetails").val();
	if(aCustomerPO !== ''){
		$("#customerPONumberSelectID option[value=1]").attr("selected", true);
	}else{
		$("#customerPONumberSelectID option[value=-1]").attr("selected", true);
	}
	jQuery("#openReleaseDig").dialog("open");
	return true;
}

function validateNewRelease(){
	var isValidToAddRelease = false;
			var aJoMasterID = $("#joMaster_ID").text();
			var aJobNumber = $("#jobNumber_ID").text();
			var releaseValidateQryStr = "&joMasterID="+aJoMasterID+"&jobNumber="+aJobNumber;
			$.ajax({
				async:false,
				url: "./jobtabs5/validatefirstrelease",
				type: "GET",
				data: releaseValidateQryStr,
				success: function(result) {
					isValidToAddRelease = result;
					return true;
				}
		   });
	return isValidToAddRelease;
}
/** Auto suggest for Manufacturer list **/
$(function() { var cache = {}; var lastXhr='';
$( "#ReleasesManuID" ).autocomplete({ minLength: 2, select: function( event, ui ) { var id = ui.item.id; $("#ManufacturerId").val(id);},
	source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
	lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
		cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
		error: function (result) {
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

/** Save Function for Release **/
function saveRelease(){
	if(!$('#openReleaseDigForm').validationEngine('validate')) {
		return false;
	}
	var aReleaseValues = $("#openReleaseDigForm").serialize();
	var aJoMasterID = $("#joMaster_ID").text();
	var aJobNumberBefore = $("#jobNumber_ID").text();
	var aRxMasterID = $("#rxCustomer_ID").text();
	var manufacturerId = $("#ManufacturerId").val();
	var releaseDate = $('#ReleasesID').val();
	if (manufacturerId === ""){
		$("#message1").html('<span style="color:red;">Alert: Please provide a valid Manufacturer.</span>');
		$("#message1").show().delay(4000).fadeOut();
		return false;
	}
	var aJobNumber = aJobNumberBefore;
	var aReleaseFormValues = aReleaseValues+"&joMasterID="+aJoMasterID+"&oper=" +aReleaseDialogVar+"&jobNumber="+aJobNumber+"&rxMasterID="+aRxMasterID;
	$.ajax({
		url: "./jobtabs5/addRelease",
		type: "POST",
		data : aReleaseFormValues,
		success: function(data) {
			$('#openReleaseDigForm').validationEngine('hideAll');
			jQuery("#openReleaseDig").dialog("close");
			$("#release").trigger("reloadGrid");
			if(aReleaseDialogVar === "add"){ 
				/*<div id="releaseMessage" class="warningMsg"></div>*/
				/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Added Sucessfully.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");*/
				$("#releaseMessage").html('<span style="color:green;">Success: Release Details Added Sucessfully</span>');
				$("#releaseMessage").show().delay(5000).fadeOut();
			}else{ 
				/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Updated Sucessfully.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");*/
				$("#releaseMessage").html('<span style="color:green;">Success: Release Details Updated Sucessfully</span>');
				$("#releaseMessage").show().delay(5000).fadeOut();
			}
		}
   });
	return true;
}

/** Edit Function for Release **/
function editRelease(){
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Booked"){ 
	aReleaseDialogVar = "edit";
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
	}
	else{
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
//	loadCustomerPONumber();
	var aVepoID = grid.jqGrid('getCell', rowId, 'vePoId');
	var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
	var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
	if(aJoReleaseType === "Drop Ship") {
		aJoReleaseType = 1;
	} else if(aJoReleaseType ===  "Stock Order") {
		aJoReleaseType = 2;
	} else if(aJoReleaseType === "Bill Only") {
		aJoReleaseType = 3;
	} else if(aJoReleaseType === "Commission") {
		aJoReleaseType = 4;
	} else if(aJoReleaseType === "Service") {
		aJoReleaseType = 5;
	}
	var aManufacturerId = grid.jqGrid('getCell',rowId,'rxMasterId');
	var aFactoryId = grid.jqGrid('getCell',rowId,'manufacturerId');
	var aManufacturer = grid.jqGrid('getCell', rowId, 'manufacturer');
	var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
	var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	var aJoReleaseDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	var aReleasePoAmount = grid.jqGrid('getCell', rowId, 'po');
	var aReleaseInvoiceAmount = grid.jqGrid('getCell', rowId, 'invoiceAmount');
	var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
	var costAllocated = aEstimateAllocated.replace(/[^0-9\.]+/g,"");
	costAllocated = Number(costAllocated);
	var aPOAmount = $("#poAmountID").is(':checked');
	var aInvoiceAmount = $("#invoiceAmountID").is(':checked');
	if(aPOAmount === true){
		$("#poAmountFieldID").show();
		var costPoAmount = aReleasePoAmount.replace(/[^0-9\.]+/g,"");
		costPoAmount = Number(costPoAmount);
		$("#POAmountID").val(costPoAmount);
		
	}else{
		$("#poAmountFieldID").css("display", "none");
	}
	if(aInvoiceAmount === true){
		$("#invoiceAmountFieldID").show();
		var costInvoiceAmount = aReleaseInvoiceAmount.replace(/[^0-9\.]+/g,"");
		costInvoiceAmount = Number(costInvoiceAmount);
		$("#ReleasesInvoiceID").val(costInvoiceAmount);
	}else{
		$("#invoiceAmountFieldID").css("display", "none");
	}
	$("#vePoId").val(aVepoID);
	$("#ReleasesID").val(aJoReleasedDate);
	$("#ReleasesTypeID").val(aJoReleaseType);
	$("#ManufacturerId").val(aManufacturerId);
	$("#ReleasesManuID").val(aManufacturer);
	$("#NoteID").val(aReleaseNote);
	$("#AllocatedID").val(costAllocated);
	$("#joReleaseDetailId").val(aJoReleaseDetailId);
	$("#joReleaseId").val(aJoReleaseId);
	$("#veFactoryId").val(aFactoryId);
	$("#customerPONumberSelectID option[value=" + aCustomerPONumber + "]").attr("selected", true);
	jQuery("#openReleaseDig").dialog("open");
	return true;
}

/** Delete Function for Release **/
function deleteRelease(){ 
	aReleaseDialogVar = "del";
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var newDialogDiv = jQuery(document.createElement('div'));
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	jQuery(newDialogDiv).html('<span><b style="color: red;">Do You Want to Delete the Release Record?</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Confirm Delete", 
		buttons:{
			"Delete": function(){
				var grid = $("#release");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var aVepoID = grid.jqGrid('getCell', rowId, 'vePoId');
				var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
				var aJoReleaseDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
				var cuSOID =	$('#Cuso_ID').text();
				var aReleaseFormValues = "&vePoName="+aVepoID+"&joReleaseName=" +aJoReleaseId+"&joReleaseDetailName=" +aJoReleaseDetailId+"&oper=" +aReleaseDialogVar+"&cuSOID="+cuSOID;
				jQuery(this).dialog("close");
				$.ajax({
					url: "./jobtabs5/addRelease",
					type: "POST",
					data : aReleaseFormValues,
					success: function(data) {
						/*var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Releases Details Delete Sucessfully.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");*/
						$("#releaseMessage").html('<span style="color:green;">Success: Releases Details Deleted Sucessfully</span>');
						$("#releaseMessage").show().delay(5000).fadeOut();
						$("#release").trigger("reloadGrid");
					}
				});
			},
			Cancel : function ()	{
				jQuery(this).dialog("close");} }
	}).dialog("open");
	return true;
}

/** Cancel Function for Release **/
function cancelRelease(){
	$('#openReleaseDigForm').validationEngine('hideAll');
	jQuery("#openReleaseDig").dialog("close");
	return true;
}

/** Release tab Save **/
function release_save(){
	var releaseNotes = $("#releaseNote").val();
	var aJoMasterID = $("#joMaster_ID").text();
	var description = getUrlVars()["jobName"];
	var job_Number = getUrlVars()["jobNumber"];
	var bidDate = $("#bidDate_Date").val();
	$.ajax({
		url: "./jobtabs5/updateReleaseNote",
		type: "POST",
		data : "&joMasterID="+aJoMasterID+"&releaseNote=" +releaseNotes+"&jobName=" +description+"&jobNumber=" +job_Number+"&joBidDate=" +bidDate,
		success: function(data) {
			var errorText = "Release Details Successfully Updated.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
   });
}

/** Bill Note Dialog Box **/
function openBillDialog(){
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Release from the Grid to Add/Edit Bill Note.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{
		var billNotes = grid.jqGrid('getCell', rowId, 'billNote');
		$("#billNote").val(billNotes);
		jQuery("#openBillNoteDialog").dialog("open");
		return true;
	}
}

jQuery(function(){
	jQuery("#openBillNoteDialog").dialog({
		autoOpen:false,
		width:340,
		title:"Bill Note",
		modal:true,
		buttons:{	},
		close:function(){ return true;}	
	});
}); 


function saveBillNote(){
	var abillNote = $("#billNote").val();
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
	var aJoMasterID = $("#joMaster_ID").text();
	var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
	var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
	var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
	var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
	var costAllocated = aEstimateAllocated.replace(/[^0-9\.]+/g,"");
	var aCostAllocated = Number(costAllocated);
	$.ajax({
		url: "./jobtabs5/billNote",
		type: "GET",
		data : "&joReleaseId="+aJoReleaseId+"&billNote=" +abillNote+"&joMasterID=" +aJoMasterID+"&joReleaseDate=" +aJoReleasedDate+
				"&joReleaseType=" +aJoReleaseType+"&ReleaseNote=" +aReleaseNote+"&EstimatedBilling=" +aCostAllocated,
		success: function(data) {
			jQuery("#openBillNoteDialog").dialog("close");
			$("#release").trigger("reloadGrid");
			var errorText = "Bill Note Successfully Updated.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
	});
}


function cancelBillNote(){
	jQuery("#openBillNoteDialog").dialog("close");
}

function openReleaseTrack(){
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var awebSight= grid.jqGrid('getCell', rowId, 'webSight'); 
	if(awebSight === null || awebSight === ''){
		var errorText = "Tracking Web Site is not there.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	}else{
		var webSpilt = awebSight.split(":");
		///var webPage= "http://"+webSight;
		if(webSpilt[0] === "http"){
			window.open(awebSight);
		}else{
			window.open("http://"+awebSight);
		}
	}
}

/** Edit Purchase Order in release tab**/
function editreleasedialog() {
	//alert('editreleasedialog');
	var aManufacture = $("#rxCustomer_ID").text();
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var isSalesOrder = grid.jqGrid('getCell',rowId,'type');
	var creditTypeID = false;
	
	$.ajax({
		url: "./jobtabs3/getHoldCredit",
		type: "GET",
		data : {"customerID" : aManufacture},
		success: function(data) {
			creditTypeID = data.creditHold;
		}
	});
	
	
	
	//console.log("oooo", $('.ui-tabs-panel:not(.ui-tabs-hide)').index());
	var jobStatus = getUrlVars()["jobStatus"];
	if(jobStatus === "Closed" || jobStatus === "Bid" || jobStatus === "Planning" || jobStatus === "Budget" || jobStatus === "Quote" ||
			jobStatus === "Submitted" ||  jobStatus === "Closed" || jobStatus === "Lost" ||
			jobStatus === "Abandoned" || jobStatus === "Rejected" || jobStatus === "Over Budget"){
		errorText = "Please change the status to 'Booked'.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else if(jobStatus === "Booked"){ 
		if(creditTypeID === true){
			errorText = "This job is on Pending.  Approval required.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); document.location.href = "./customerdetails?rolodexNumber="+$("#rxCustomer_ID").text()+"&name="+'`'+$("#jobCustomerName_ID").text()+'`&creditHold=false'; }}]}).dialog("open");
			return false;
		} else if(rowId === null){
			errorText = "Please click one of the Order to Edit Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		} else {
			//alert('else---');
			var manufacturer = grid.jqGrid('getCell', rowId, 'rxMasterId');
			var manufacturerName =  grid.jqGrid('getCell', rowId, 'manufacturer');
			var vePoId = grid.jqGrid('getCell', rowId, 'vePoId');
			var addressID =  grid.jqGrid('getCell', rowId, 'rxAddressID');
			var aCustomerPONumber = grid.jqGrid('getCell', rowId, 'poid');
			var rxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
			var date = grid.jqGrid('getCell', rowId, 'released');
			$("#poDate_ID").text(date);
			$("#dateOfcustomerGeneral").val(date);
			$('#order_ID').text(rxMasterID);
			var cusoid=$('#Cuso_ID').text();
			var x = $('#rxCustomer_ID').text();
			$('#rxmasterId').text(x);
/*			$.ajax({ 
				url: "./jobtabs5/getAddressAddressInformation",
				mType: "GET",
				data : { 'rxAddressID' : addressID },
				success: function(data){
					$("#vendorAddress1").val(data.address1); $("#vendorAddress2").val(data.address2); $("#vendorAddressCity").val(data.city);
					$("#vendorAddressState").val(data.state); $("#vendorAddressZip").val(data.zip);
				}
			});
			
*/			
			if(isSalesOrder!=='Stock Order'){
				//alert('Stock Order');
				loadPORelease(manufacturer,vePoId,manufacturerName, aCustomerPONumber);
			}
			else{
				//alert('Stock Order else');
				$("#salesrelease").dialog("open");
				$("#lineItemGrid").trigger("reloadGrid");
				$("#Ack").trigger("reloadGrid");
				$('#saleslineitems').removeClass('ui-tabs-selected').removeClass('ui-state-active');
				$('#salesgeneral').addClass('ui-tabs-selected').addClass('ui-state-active');
				$('#ui-tabs-11').removeClass('ui-tabs-hide');
				$('#ui-tabs-12').addClass('ui-tabs-hide'); 
				PreloadData(cusoid);
			}
		}
	 } 
	
	return true;
	 
}
function loadcusoID(joReleaseId){
	$.ajax({
		url: "./salesOrderController/getCuSOID",
		type: "POST",
		data : {"jobNumber" : joReleaseId},
		success: function(data) {
			$('#Cuso_ID').text(data);
		}
	});	
}
function disableoption(option){
	console.log(option);
	//$("#releasesTypeID").val(2);
	return true;
}

function sendPOEmail(poGeneralKey){
	var bidderGrid = $("#release");
	var aQuotePDF = "purchase";
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePoId = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxVendorContactID');
	var errorText = '';
	var cusotmerPONumber = $("#ourPoId").val();
	if(bidderGridRowId === null){
		errorText = "Please click one of the Order to Email Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aContactID === null && aContactID === '' && aContactID === '-1'){
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
				if(aEmail === ''){
					errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
				}else{
					errorText = "Are you sure you want to send this PO to"+ arxContact +"("+ aEmail +")?";
				}
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
				buttons:{
					"Send": function(){
						$('#loadingPODiv').css({"visibility": "visible"});
						viewPOPDF(aQuotePDF);
						sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId);
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
			}
		});
		}
	return true;
}

function sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId){
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
					$('#loadingPODiv').css({"visibility": "visible"});
					$.ajax({ 
						url: "./sendMailServer/sendPOMail",
						mType: "POST",
						data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
						success: function(data){
							$('#loadingPODiv').css({"visibility": "hidden"});
							var errorText = "Mail Sent Successfully.";
							jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
							buttons: [{height:35,text: "OK",click: function() {
								var today = new Date();
								var dd = today.getDate();
								var mm = today.getMonth()+1; 
								var yyyy = today.getFullYear().toString().substr(2,2);
								var hours = today.getHours();
								var minutes = today.getMinutes();
								var ampm = hours >= 12 ? 'PM' : 'AM';
								hours = hours % 12;
								hours = hours ? hours : 12;
								if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
								if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
								$.ajax({ 
									url: "./jobtabs3/updateEmailStampValue",
									mType: "GET",
									data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
									success: function(data){ $("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today); }
								});
								$(this).dialog("close"); 
							}}] }).dialog("open");
						}
					});
					jQuery(this).dialog("close");
					return true;
				},
				Cancel: function ()	{jQuery(this).dialog("close");}
			}}).dialog("open");
	}else{
		if(poGeneralKey === 'poGeneral'){
			$('#loadingPOGenDiv').css({"visibility": "visible"});
			$.ajax({ 
				url: "./sendMailServer/sendPOMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
				success: function(data){
					$('#loadingPOGenDiv').css({"visibility": "hidden"});
					$('#loadingPODiv').css({"visibility": "hidden"});
					var errorText = "Mail Sent Successfully.";
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
						buttons: [{height:35,text: "OK",click: function() {
							var today = new Date();
							var dd = today.getDate();
							var mm = today.getMonth()+1; 
							var yyyy = today.getFullYear().toString().substr(2,2);
							var hours = today.getHours();
							var minutes = today.getMinutes();
							var ampm = hours >= 12 ? 'PM' : 'AM';
							hours = hours % 12;
							hours = hours ? hours : 12;
							if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
							if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
							$.ajax({ 
								url: "./jobtabs3/updateEmailStampValue",
								mType: "GET",
								data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
								success: function(data){ $("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today); }
							});
							$(this).dialog("close"); 
						}}] }).dialog("open");
				}
			});
		}else{
			$.ajax({ 
				url: "./sendMailServer/sendPOMail",
				mType: "POST",
				data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
				success: function(data){
					$('#loadingPODiv').css({"visibility": "hidden"});
					var errorText = "Mail Sent Successfully.";
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
						buttons: [{height:35,text: "OK",click: function() {
							var today = new Date();
							var dd = today.getDate();
							var mm = today.getMonth()+1; 
							var yyyy = today.getFullYear().toString().substr(2,2);
							var hours = today.getHours();
							var minutes = today.getMinutes();
							var ampm = hours >= 12 ? 'PM' : 'AM';
							hours = hours % 12;
							hours = hours ? hours : 12;
							if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
							if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
							$.ajax({ 
								url: "./jobtabs3/updateEmailStampValue",
								mType: "GET",
								data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
								success: function(data){ $("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today); }
							});
							$(this).dialog("close"); 
						}}] }).dialog("open");
				}
			});
		}
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

function viewPOPDF(aPDFType){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var releaseType = bidderGrid.jqGrid('getCell',bidderGridRowId,'type');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var cusoID =  $('#Cuso_ID').text();
	
	if(aPDFType === 'purchase'){
		
			$.ajax({
				type : "GET",
				url : "./purchasePDFController/viewPDFLineItemForm",
				data : { 'vePOID' : vePOID, 'puchaseOrder' : aPDFType, 'jobNumber' :  jobNumber, 'rxMasterID' : rxMasterID, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aInteger },
				documenttype: "application\pdf",
				async: false,
				cache: false,
				success : function (msg) {
				},
				error : function (msg) {}
			});
	} else {
		if(bidderGridRowId === null){
			errorText = "Please click one of the Order to View Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		aPDFType = "Po";
		if (releaseType=='Drop Ship') {				/*Check wether the user print a Purchase order report.*/
			window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToModeId);
			return true;
		} else {									/* check wether the user prints a sales order report.*/
			if (cusoID == null || cusoID == 'undefined') {
				errorText = "Please select a sales order from release grid.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID);
			return true;
		}
	}
	 
}
                      /**-------------------------------          Vendor Invoice Functions              ------------------------------------------- **/

/** open dialog for Vendor Invoice **/

function openvendorinvoicedialog() {
	 if(jQuery("#release").getGridParam("records") === '0'){
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please add one release in "Release Grid".</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aVendorDate = grid.jqGrid('getCell', rowId, 'vendorDate');
	var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
	
	var releasesGrid = $("#release");
	var selectedRelease = releasesGrid.jqGrid('getGridParam', 'selrow');
	var aJoReleaseDetailsID = releasesGrid.jqGrid('getCell', selectedRelease, 'joReleaseDetailid');
	var aVePOID = releasesGrid.jqGrid('getCell', selectedRelease, 'vePoId');
	if(!aVeBillId){
		aVeBillId = 0;
	}
	
	//alert("release detail ID: "+aJoReleaseDetailsID +"\naVePOID: "+aVePOID);
	//loadVendorInvoice(aVePOID);
	if(jQuery("#shiping").getGridParam("records") !== 0){
		if(aVendorDate !== ''){
			var invoiceType = 'existing';
			loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
			getVendorDetails(aJoReleaseDetailsID, aVePOID);
			$('#openvendorinvoice').dialog('option', 'title', '');
			$('#openvendorinvoice').dialog('option', 'title', aVendorDate);
			jQuery("#openvendorinvoice").dialog("open");
		 }else{
			 errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
				"Yes": function(){
					var invoiceType = 'new';
					loadVendorInvoice(aVePOID);
					//loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
					jQuery(this).dialog("close");
					//getVendorDetails(aJoReleaseDetailsID, aVePOID);
					$("#postDateID").val('');
					$("#postDateID").val(currenDate);
					$("#dueDateID").val('');
					$("#dueDateID").val(currenDate);
					$("#shipDateID").val('');
					$("#shipDateID").val(currenDate);
					$("#vendorDateID").val('');
					$("#vendorDateID").val(currenDate);
					$('#openvendorinvoice').dialog('option', 'title', '');
					$('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
					jQuery("#openvendorinvoice").dialog("open");
				},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
			}}).dialog("open");
		 }
	}else{
		errorText = "Invoice not found, do you wish to create a new vendor invoice for this release?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
		buttons:{
			"Yes": function(){
				var invoiceType = 'new';
				loadVendorInvoice(aVePOID);
				loadPOGeneralDetails (aVePOID);
				//loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType);
				jQuery(this).dialog("close");
				//getVendorDetails(aJoReleaseDetailsID, aVePOID);
				$("#postDateID").val('');
				$("#postDateID").val(currenDate);
				$("#dueDateID").val('');
				$("#dueDateID").val(currenDate);
				$("#shipDateID").val('');
				$("#shipDateID").val(currenDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(currenDate);
				$('#openvendorinvoice').dialog('option', 'title', '');
				$('#openvendorinvoice').dialog('option', 'title', 'New Vendor Invoice');
				jQuery("#openvendorinvoice").dialog("open");
			},
			"No": function ()	{
				jQuery(this).dialog("close");
			}
		}}).dialog("open");
	}
	 return true;
}

function loadVendorInvoiceLineItems(aVeBillId, aJoReleaseDetailsID, aVePOID, invoiceType){
	$("#vendorinvoice1").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager:jQuery('#vendorinvoicepager'),
		url:'./veInvoiceBillController/getBillLineitemsList',
		postData: {vePoId: function() { return aVePOID;}, veBillId: aVeBillId, aJoReleaseDetailsID: aJoReleaseDetailsID, invoiceType: invoiceType},
		colNames:['veBillDetailId','Product No', 'Description','Qty','Cost Ea', 'Mult.', 'Amount', 'VeBillId', 'prMasterID' , 'vePodetailID'],
		colModel :[
			{name:'veBillDetailId', index:'veBillDetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prItemCode',index:'prItemCode',align:'left',width:70,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				dataInit: function (elem) {
					$(elem).autocomplete({
						source: 'jobtabs3/productCodeWithNameList',
						minLength: 1,
						select: function( event, ui ){ 
							var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} 
						},
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
					}); 
				}
				},
				editrules:{edithidden:false,required: true}},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityBilled', index:'quantityBilled', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}, formatter:customCurrencyFormatter},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, editrules:{edithidden:true}},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'veBillId', index:'veBillId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}}
		],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'veBillDetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 820, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Items',
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
			$("#vendorinvoice1").setSelection(1, true);
			var allRowsInGrid = $('#vendorinvoice1').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace(/[^0-9\.]+/g,"");
				sum = Number(sum) + Number(number1); 
			});
			$('#subtotal_ID').val(formatCurrency(sum));
			var taxValue = $('#tax_ID').val().replace(/[^0-9\.]+/g,"");
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace(/[^0-9\.]+/g,"");
					taxAmount = taxAmount + Number(number1)*(taxValue/100);
				}
			});
			var freightLineVal= $('#freight_ID').val();
			var freightAmount = '';
			if(freightLineVal !== ''){
				freightAmount = freightLineVal.replace(/[^0-9\.]+/g,"");
			}
			aTotal = aTotal + sum + taxAmount + Number(freightAmount);
			$('#total_ID').val(formatCurrency(aTotal));
			/***var txtBalanceDue = Dollar(veBill.Recordset("BillAmount")) - Dollar(veBill.Recordset("AppliedAmount"));*/
			var aBal =  aTotal-(sum+7+taxValue);
			$("#bal_ID").val(aBal);
			$("#vendorinvoice1").trigger("reload");
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		editurl:"./veInvoiceBillController/manipulateBillLineitems"
	}).navGrid('#vendorinvoicepager',
			{add:true, edit:true,del:true,refresh:true,search:false}, //options
		//-----------------------edit options----------------------//
		{
			width:450, left:400, top: 300,
			closeAfterEdit:true, reloadAfterSubmit:true,
			modal:true, jqModel:false,
			editCaption: "Edit Product",
			beforeShowForm: function (form) {
				var unitcost = $("#unitCost").val();
				unitcost = unitcost.replace(/[^0-9\.]+/g,"");
				$("#unitCost").val(unitcost);
			},
			afterShowForm: function($form) {
				$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
					select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
					if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
					error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
				return;
				});
			},
			beforeSubmit:function(postdta, formid) {
				$("#prItemCode").autocomplete("destroy");
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			afterSubmit:function(response,postData){
				$("#vendorinvoice1").trigger("reloadGrid");
				return true;
			}
		},
		//-----------------------add options----------------------//
		{
			width:515, left:400, top: 300,
			closeAfterAdd:true,	reloadAfterSubmit:true,
			modal:true, jqModel:true,
			
			addCaption: "Add Product",
			beforeShowForm: function (form){
				var grid = $("#shiping");
				var rowId = grid.jqGrid('getGridParam', 'selrow');
				var aVeBillId = grid.jqGrid('getCell', rowId, 'veBillID');
				$("#veBillId").val(aVeBillId);
				/*$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
						lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
					select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
						if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
					error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
					}); 
				return;
				});*/
			},
			afterShowForm: function($form) {
				$(function() { var cache = {}; var lastXhr=''; $("input#prItemCode .FormElement").autocomplete({minLength: 1,timeout :1000,
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
					select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
					if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
					error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
				return;
				});
			},
			beforeSubmit:function(postdta, formid) {
				$("#prItemCode").autocomplete("destroy");
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			onclickSubmit: function(params){	},
			afterSubmit:function(response,postData){
				$("#vendorinvoice1").trigger("reloadGrid");
				return true;
			}
		},
		//-----------------------Delete options----------------------//
		{	
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
			caption: "Delete Product",
			msg: 'Delete the Product Record?',

			onclickSubmit: function(params){
				var id = jQuery("#vendorinvoice1").jqGrid('getGridParam','selrow');
				var key = jQuery("#vendorinvoice1").getCell(id, 1);
				return { 'veBillDetailId' : key};
			}
		},
		//-----------------------search options----------------------//
		{	});
}

/** vendor Invoice Grid **/
function loadVendorInvoice(vePOID){
	$("#vendorinvoice1").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager:jQuery('#vendorinvoicepager'),
		url:'./jobtabs5/jobReleaseLineItem',
		postData: {vePoId : function() { return vePOID;}},
		colNames:['Product No', 'Description','Qty','Cost Ea', 'Mult.', 'Tax', 'Amount', 'VepoID', 'prMasterID' , 'vePodetailID','Posiion', 'Move'],
		colModel :[
			{name:'note',index:'note',align:'left',width:70,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false,required: true}},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:15,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}, formatter:customCurrencyFormatter},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, editrules:{edithidden:true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'upAndDown',index:'upAndDown',align:'left',width:40, hidden: true}],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 770, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Items',
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
			$("#vendorinvoice1").setSelection(1, true);
			var allRowsInGrid = $('#vendorinvoice1').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace(/[^0-9\.]+/g,"");
				sum = Number(sum) + Number(number1); 
			});
			$('#subtotal_ID').val(formatCurrency(sum));
			var taxValue = $('#tax_ID').val().replace(/[^0-9\.]+/g,"");
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace(/[^0-9\.]+/g,"");
					taxAmount = taxAmount + Number(number1)*(taxValue/100);
				}
			});
			var freightLineVal= $('#freight_ID').val();
			var number1 = '';
			if(freightLineVal !== ''){
				number1 = freightLineVal.replace(/[^0-9\.]+/g,"");
			}
			aTotal = aTotal + sum + taxAmount + Number(number1);
			$('#total_ID').val(formatCurrency(aTotal));
			/*var aBal =  aTotal-(sum+7+taxValue);
			$("#bal_ID").val(aBal);*/
			$("#vendorinvoice1").trigger("reload");
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
	}).navGrid('#vendorinvoicepager',
			{add:false, edit:false,del:false,refresh:false,search:false}, //options
		//-----------------------edit options----------------------//
		{
			width:450, left:400, top: 300,
			closeAfterEdit:true, reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Product",
			beforeShowForm: function (form) {
				
			},
			afterSubmit:function(response,postData){	 }
		},
		//-----------------------add options----------------------//
		{
			width:515, left:400, top: 300,
			closeAfterAdd:true,	reloadAfterSubmit:true,
			modal:true, jqModel:true,
			
			addCaption: "Add Product",
			beforeShowForm: function (form){	},
			onclickSubmit: function(params){	},
			afterSubmit:function(response,postData){	}
		},
		//-----------------------Delete options----------------------//
		{	
			closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350,
			caption: "Delete Product",
			msg: 'Delete the Product Record?',
			onclickSubmit: function(params){	}
		},
		//-----------------------search options----------------------//
		{	});
}

/**  View Vendor Invoice Details **/
function viewInvoiceDetails(){
	var grid = $("#vendorinvoice1");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aVepoID = grid.jqGrid('getCell', rowId, 'prMasterId');
	var aItemCode = grid.jqGrid('getCell', rowId, 'note');
	document.location.href="./inventoryDetails?token=view&inventoryId="+aVepoID+"&itemCode="+aItemCode+"";
	return true;
}

/** Get Vendor Details **/
function getVendorDetails(joDetailId, vePOID){
	if(joDetailId === "" && vePOID === ""){
		return false;
	}
	var url ="./jobtabs5/getVendorInvoiceDetails";
	$.ajax({
		url: "./jobtabs5/getVendorInvoiceDetails",
		type: "POST",
		data : {'releaseDetailID' : joDetailId, 'vePODetailId' : vePOID},
		success: function(data) {
			if(data !== ''){
				var postdate = new Date(data.postDate);
				var dueDate = new Date(data.dueDate);
				var shipDate = new Date(data.shipDate);
				var receiveDate = new Date(data.receiveDate);
				var aPostDate = postdate.getUTCMonth()+1+"/"+ postdate.getUTCDate()+"/"+postdate.getUTCFullYear();
				var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();
				var aReceiveDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();
				document.getElementById('datedID').disabled = true;
				$("#postDateID").val('');
				$("#proNumberID").val(data.trackingNumber);
				$("#postDateID").val('');
				$("#postDateID").val(aPostDate);
				$("#dueDateID").val('');
				$("#dueDateID").val(aDueDate);
				$("#shipDateID").val('');
				$("#shipDateID").val(aShipDate);
				$("#vendorDateID").val('');
				$("#vendorDateID").val(aReceiveDate);
				$("#freight_ID").val(formatCurrency(data.freightAmount));
				$("#tax_ID").val(formatCurrency(data.taxAmount));
				$("#bal_ID").val("$0.00");
				$("#vendorInvoiceNum").val(data.invoiceNumber);
				$("#veBill_ID").val(data.veBillId);
				$("#coAccountID option[value=" + data.apaccountId + "]").attr("selected", true);
				$("#shipViaSelectID option[value=" + data.veShipViaId + "]").attr("selected", true);
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
			}
		},
		error: function(jqXHR, textStatus, errorThrown){
			var errorText = $(jqXHR.responseText).find('u').html();
			jQuery(newDialogDiv).html('<span><b style="color:red;">Error on modifying the data: '+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	});
	return true;
}

/** Post Date Show and hidden **/
function postDateChk(){
	if ($('#postDateChkID').is(':checked')) { 
		$("#postDateID").show(); 
 	} else {
		$("#postDateID").hide();
 	} 
}

/** Save invoice Details **/
function savevendorinvoice(){
	var aInvoiceDetails = $("#openvendorinvoiceFormID").serialize();
	var aSubTotal = $("#subtotal_ID").val().replace(/[^0-9\.]+/g,"");
	var aFreight = $("#freight_ID").val().replace(/[^0-9\.]+/g,"");
	var aTax = $("#tax_ID").val().replace(/[^0-9\.]+/g,"");
	var aTotal = $("#total_ID").val().replace(/[^0-9\.]+/g,"");
	var aBalance = $("#bal_ID").val().replace(/[^0-9\.]+/g,"");
	var title = $('#openvendorinvoice').dialog('option', 'title');
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var aVeBillID = '';
	var joReleaseID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'joReleaseDetailid');
	var vePoID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var rxMasterID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxMasterId');
	var aAddOREdit = ''; 
	if(title === 'New Vendor Invoice'){
		aAddOREdit = 'add';
	}else{
		aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
		aAddOREdit = 'edit';
	}
	var aVendorInvoiceDetails = aInvoiceDetails+"&subtotal_Name="+aSubTotal+"&freight_Name="+aFreight+"&tax_Name="+aTax+"&total_Name="+aTotal+"&bal_Name="+aBalance+
																		"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseID+"&veBill_Name="+aVeBillID+"&vePOID="+vePoID+"&rxMasterID="+rxMasterID;
	updateVendorInvoiceDetails(aVendorInvoiceDetails);
	/*$.ajax({
		url: "./veInvoiceBillController/updateVendorInvoiceDetails",
		type: "POST",
		data : aVendorInvoiceDetails,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Vendor Invoice Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#openvendorinvoice").dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});*/
	$("#release").trigger("reloadGrid");
	jQuery("#openvendorinvoice").dialog("close");
}

var updateVendorInvoiceDetails = function(aVendorInvoiceDetails){
	$.ajax({
		url: "./veInvoiceBillController/updateVendorInvoiceDetails",
		type: "POST",
		data : aVendorInvoiceDetails,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Vendor Invoice Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#openvendorinvoice").dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
};

 			/** ----------------------------------------------------              Vendor Invoice              ---------------------------------------------------- **/
 
 jQuery( function(){
		jQuery("#cusinvoicetab").dialog({
			autoOpen:false,
			width:850,
			title:"Customer Invoice",
			modal:true,
			close:function(){
				return true;
			}
		});
	});
 
 function invoiceshipToAddress(){
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
			$("#customerShipToAddressID").val(locationName); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
			$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
			document.getElementById('customerShipToAddressID').disabled=true;
			document.getElementById('customerShipToAddressID1').disabled=true;
			document.getElementById('customerShipToAddressID2').disabled=true;
			document.getElementById('customerShipToCity').disabled=true;
			document.getElementById('customerShipToState').disabled=true;
			document.getElementById('customerShipToZipID').disabled=true;
			}
		});
 }
 
function invoicebillToAddress(){
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
		$("#customerbillToAddressID").val(locationName); $("#customerbillToAddressID1").val(locationAddress1); $("#customerbillToAddressID2").val(locationAddress2); $("#customerbillToCity").val(locationCity);
		$("#customerbillToState").val(locationState); $("#customerbillToZipID").val(locationZip);
		document.getElementById('customerbillToAddressID').disabled=true;
		document.getElementById('customerbillToAddressID1').disabled=true;
		document.getElementById('customerbillToAddressID2').disabled=true;
		document.getElementById('customerbillToCity').disabled=true;
		document.getElementById('customerbillToState').disabled=true;
		document.getElementById('customerbillToZipID').disabled=true;
		}
	});
}
 
 function savecustomerinvoice() {
	var cuinvId = $('#cuinvoiceIDhidden').val();
	var customerID = $('#rxCustomer_ID').text();
	var cusoId =  $('#Cuso_ID').text();
	var aInvoiceDetails = $("#custoemrInvoiceFormID").serialize();
	var aSubTotal = $("#customerInvoice_subTotalID").val().replace(/[^0-9\.]+/g,"");
	var aFreight = $("#customerInvoice_frightIDcu").val().replace(/[^0-9\.]+/g,"");
	var aTax = $("#customerInvoice_taxIdcu").val().replace(/[^0-9\.]+/g,"");
	var aTotal = $("#customerInvoice_totalID").val().replace(/[^0-9\.]+/g,"");
	var title = $('#cusinvoicetab').dialog('option', 'title');
	var grid = $("#shiping");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var poNumber = $('#customerInvoice_proNumberID').val().replace(/[^0-9\.]+/g,"");
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var aVeBillID = '';
	var taxrate = $('#customerInvoice_linetaxId').val().replace(/[^0-9\.]+/g,"");
	var InvoiceNo = $('#customerInvoice_invoiceNumberId').val();
	var shipDate = $('#customerInvoice_shipDateID').val();
	var joReleaseID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'joReleaseDetailid');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var releaseType = 2;
	if(vePOID != ''){
		releaseType = 1;
		cusoId = vePOID;
	}
	var aAddOREdit = ''; 
	if(title === 'New Customer Invoice'){
		aAddOREdit = 'add';
	}else{
		aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');
		aAddOREdit = 'edit';
	}
	var aCustomerInvoiceDetails = aInvoiceDetails+"&customerInvoice_subTotalName="+aSubTotal+"&customerInvoice_frightname="+aFreight+"&customerInvoice_taxName="+aTax+"&customerInvoice_totalName="+aTotal+"&oper="+aAddOREdit+"&joReleaseDetailsID="+joReleaseID+'&cuSOID='+cusoId+'&poNumber='+poNumber
								+'&InvoiceNo='+InvoiceNo+'&shipDate='+shipDate+'&taxRate='+taxrate+'&cuInvHiddenId='+cuinvId+"&releaseType="+releaseType+'&customerID='+customerID;
	$.ajax({
		url: "./jobtabs5/updateCustomerInvoiceDetails",
		type: "POST",
		data : aCustomerInvoiceDetails,
		success: function(data) {
			$('#cuInvoiceID').text(data.cuInvoiceId);
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Customer Invoice Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#cusinvoicetab").dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
}

 /** open dialog for Customer Invoice **/
 function opencustomerinvoicedialog(){
	 if(jQuery("#release").getGridParam("records") === '0'){
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please add one release in "Release Grid".</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	 } 
	 var grid = $("#release");
	 var rowId = grid.jqGrid('getGridParam', 'selrow');
	 var releaseType = grid.jqGrid('getCell', rowId, 'type');
	 
	 
	 var grid = $("#shiping");
	 var rowId = grid.jqGrid('getGridParam', 'selrow');
	 var aCusotmerDate = grid.jqGrid('getCell', rowId, 'customerDate');
	 var joDetailId = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	 var shipViaID = grid.jqGrid('getCell', rowId, 'veShipViaID');
	 if(jQuery("#shiping").getGridParam("records") !== 0){
		 if(aCusotmerDate === ''){
			 	errorText = "Invoice not found, do you wish to create a new customer invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
						jQuery(this).dialog("close");
						var aCustomerName = $(".customerNameField").val();
						 var aCustomerID = $("#JobCustomerId").val();
						 PreloadDataInvoice();
						 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
						 $("#customerInvoice_customerHiddnID").val(aCustomerID);
						 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
						 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
						 $("#customerInvoice_invoiceDateID").val('');
						$("#customerInvoice_invoiceDateID").val(currenDate);
						$("#customerInvoice_shipDateID").val('');
						$("#customerInvoice_shipDateID").val(currenDate);
						$("#customerInvoice_dueDateID").val('');
						$("#customerInvoice_dueDateID").val(currenDate);
						$("#customerInvoice_lineshipDateID").val('');
						$("#customerInvoice_lineshipDateID").val(currenDate);
						$("#customerInvoice_lineinvoiceDateID").val('');
						$("#customerInvoice_lineinvoiceDateID").val(currenDate);
						$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
//						$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
						 invoiceshipToAddress();
						 invoicebillToAddress();
						 $("#lineshipTo1").hide();
						 $('#cusinvoicetab').dialog('option', 'title', '');
						 $('#cusinvoicetab').dialog('option', 'title', 'New Customer Invoice');
						 jQuery("#cusinvoicetab").dialog("open");
						 $( "#cusinvoicetab ul li" ).first().addClass( "ui-tabs-selected ui-state-active" );
						 $( "#cusinvoicetab ul li:nth-child(2)" ).removeClass().addClass("ui-state-default ui-corner-top");
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
			}}).dialog("open");
		}else{
			 var aCustomerName = $(".customerNameField").val();
			 var aCustomerID = $("#JobCustomerId").val();
			 $("#customerInvoice_customerInvoiceID").val(aCustomerName);
			 $("#customerInvoice_customerHiddnID").val(aCustomerID);
			 $("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
			 $("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
			 $("#customerInvoice_invoiceDateID").val('');
			$("#customerInvoice_invoiceDateID").val(currenDate);
			$("#customerInvoice_shipDateID").val('');
			$("#customerInvoice_shipDateID").val(currenDate);
			$("#customerInvoice_dueDateID").val('');
			$("#customerInvoice_dueDateID").val(currenDate);
			$("#customerInvoice_lineshipDateID").val('');
			$("#customerInvoice_lineshipDateID").val(currenDate);
			$("#customerInvoice_lineinvoiceDateID").val('');
			$("#customerInvoice_lineinvoiceDateID").val(currenDate);
			$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
			$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
			 $("#lineshipTo1").hide();
			 $('#cusinvoicetab').dialog('option', 'title', '');
			 $('#cusinvoicetab').dialog('option', 'title', aCusotmerDate);
			 jQuery("#cusinvoicetab").dialog("open");
		}}else{
			 var errorText = "Invoice not found, do you wish to create a new customer invoice for this release?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
				buttons:{
					"Yes": function(){
								PreloadDataInvoice();
								jQuery(this).dialog("close");
								var aCustomerName = $(".customerNameField").val();
								var aCustomerID = $("#JobCustomerId").val();
								$("#customerInvoice_customerInvoiceID").val(aCustomerName);
								$("#customerInvoice_customerHiddnID").val(aCustomerID);
								$("#customerInvoice_linecustomerInvoiceID").val(aCustomerName);
								$("#customerInvoice_linecustomerHiddnID").val(aCustomerID);
								$("#customerInvoice_invoiceDateID").val('');
								$("#customerInvoice_invoiceDateID").val(currenDate);
								$("#customerInvoice_shipDateID").val('');
								$("#customerInvoice_shipDateID").val(currenDate);
								$("#customerInvoice_dueDateID").val('');
								$("#customerInvoice_dueDateID").val(currenDate);
								$("#customerInvoice_lineshipDateID").val('');
								$("#customerInvoice_lineshipDateID").val(currenDate);
								$("#customerInvoice_lineinvoiceDateID").val('');
								$("#customerInvoice_lineinvoiceDateID").val(currenDate);
								$("#shipViaCustomerSelectlineID option[value="+ shipViaID + "]").attr("selected", true);
								$("#shipViaCustomerSelectID option[value="+ shipViaID + "]").attr("selected", true);
								$("#lineshipTo1").hide();
								$('#cusinvoicetab').dialog('option','title', '');
								$('#cusinvoicetab').dialog('option','title', 'New Customer Invoice');
								jQuery("#cusinvoicetab").dialog("open");
					},
				"No": function ()	{
					jQuery(this).dialog("close");
				}
		}}).dialog("open");
	 }
	 return true;
 }
 
function cancelcustomerinvoice(){
	$("#release").trigger("reloadGrid");
	jQuery("#cusinvoicetab").dialog("close");
}

function customershipBackWard(){
	$("#lineshipTo1").hide();
	$("#lineshipTo").show();
}

function customershipForWard(){
	$("#lineshipTo1").show();
	$("#lineshipTo").hide();
}

function usinvoiceShiptoAddress(){
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 loadShipToAddress();
	 return true;
}
function customerinvoiceShiptoAddress(){
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer_select.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
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
					$("#customerShipToAddressID").val(locationName); $("#customerShipToAddressID1").val(locationAddress1); $("#customerShipToAddressID2").val(locationAddress2); $("#customerShipToCity").val(locationCity);
					$("#customerShipToState").val(locationState); $("#customerShipToZipID").val(locationZip);
					document.getElementById('customerShipToAddressID').disabled=true;
					document.getElementById('customerShipToAddressID1').disabled=true;
					document.getElementById('customerShipToAddressID2').disabled=true;
					document.getElementById('customerShipToCity').disabled=true;
					document.getElementById('customerShipToState').disabled=true;
					document.getElementById('customerShipToZipID').disabled=true;
					}
				});
		 return true;
}
function jobsiteinvoiceShiptoAddress(){
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite_select.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 $("#customerShipToAddressID").val(CustomName); $("#customerShipToAddressID1").val(jobLocation1); $("#customerShipToAddressID2").val(jobLocation2); $("#customerShipToCity").val(jobCity);
		$("#customerShipToState").val(jobState); $("#customerShipToZipID").val(jobZip);
		document.getElementById('customerShipToAddressID').disabled=true;
		document.getElementById('customerShipToAddressID1').disabled=true;
		document.getElementById('customerShipToAddressID2').disabled=true;
		document.getElementById('customerShipToCity').disabled=true;
		document.getElementById('customerShipToState').disabled=true;
		document.getElementById('customerShipToZipID').disabled=true;
		return true;
}
function otherinvoiceShiptoAddress(){
	 $('#forWardId').css({"display": "none"});
	 $('#backWardId').css({"display": "none"});
	 $('#usinvoiceShipto').css({ "background-image": "url(./../resources/images/us.png)","width":"63px","height": "28px" });
	 $('#customerinvoiceShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteinvoiceShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherinvoiceShipto').css({ "background-image": "url(./../resources/images/other_select.png)","width":"63px","height": "28px" });
	 $("#customerShipToAddressID").val(""); $("#customerShipToAddressID1").val(""); $("#customerShipToAddressID2").val(""); $("#customerShipToCity").val("");
		$("#customerShipToState").val(""); $("#customerShipToZipID").val("");
		document.getElementById('customerShipToAddressID').disabled=false;
		document.getElementById('customerShipToAddressID1').disabled=false;
		document.getElementById('customerShipToAddressID2').disabled=false;
		document.getElementById('customerShipToCity').disabled=false;
		document.getElementById('customerShipToState').disabled=false;
		document.getElementById('customerShipToZipID').disabled=false;
		return true;
}

$(function() { var cache = {}; var lastXhr='';
$( "#customerinvoice_paymentTerms" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerinvoicepaymentId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#customerInvoice_TaxTerritory" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerTaxTerritory").val(id); var tax = ui.item.taxValue; $("#customer_TaxTextValue").val(tax); $('#customer_TaxTextValue').empty(); $("#customer_TaxTextValue").append(tax+"%");},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
$( "#customerInvoice_salesRepsList" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_salesRepId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_CSRList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_CSRId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_SalesMgrList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_salesMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_EngineersList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_engineerId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

$(function() { var cache = {}; var lastXhr='';
	$( "#customerInvoice_PrjMgrList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerInvoice_prjMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

function getCustomerInvoiceDetails(joDetailId, CustomerID){
	$.ajax({
		url: "./jobtabs5/getCustomerInvoiceDetails",
		type: "POST",
		data : {'releaseDetailID' : joDetailId, 'customerID' : CustomerID },
		success: function(data) {
			if(data !== ''){
				var dueDate = new Date(data.dueDate);
				var receiveDate = new Date(data.invoiceDate);
				var shipDate = new Date(data.shipDate);
				var invoiceNumber = data.invoiceNumber;
				var customerPO = data.customerPonumber;
				var fright = data.freight;
				var costTotal = data.costTotal;
				var subTotal = data.subtotal;
//				var taxTotal = data.taxTotal;
				var taxrate = data.taxRate;
//				var rxBillId = data.rxBillToId;
//				var rxBillAddressID = data.rxBillToAddressId;
//				var shipID = data.rxShipToId;
//				var shipAddressId  = data.rxShipToAddressId;	 
				var shipViaID = data.veShipViaId;
//				var prFromWareHouse = data.prFromWarehouseId;
				var prWarehouse = data.prToWarehouseId;
//				var  taxTeritory = data.coTaxTerritoryId;
				var divisionID = data.coDivisionId;
				var proNumber = data.trackingNumber;
				var doNotMAil = data.doNotMail;
				var cuInvoiceID = data.cuInvoiceId;
				var  assign0 = Number(data.cuAssignmentId0);
				var  assign1 = Number(data.cuAssignmentId1);
				var  assign2 = Number(data.cuAssignmentId2);
				var  assign3 = Number(data.cuAssignmentId3);
				var  assign4 = Number(data.cuAssignmentId4);
				$("#customerInvoice_salesRepId").val(assign0);
				$("#customerInvoice_CSRId").val(assign1);
				$("#customerInvoice_salesMgrId").val(assign2);
				$("#customerInvoice_engineerId").val(assign3);
				$("#customerInvoice_prjMgrId").val(assign4);
				var aDueDate = dueDate.getUTCMonth()+1+"/"+ dueDate.getUTCDate()+"/"+dueDate.getUTCFullYear();
				var aInvoiceDate = receiveDate.getUTCMonth()+1+"/"+ receiveDate.getUTCDate()+"/"+receiveDate.getUTCFullYear();
				var aShipDate = shipDate.getUTCMonth()+1+"/"+ shipDate.getUTCDate()+"/"+shipDate.getUTCFullYear();
				$("#customerInvoice_dueDateID").val(aDueDate);
				$("#customerInvoice_invoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_lineinvoiceDateID").val(aInvoiceDate);
				$("#customerInvoice_invoiceNumberId").val(invoiceNumber);
				$("#customerInvoice_lineinvoiceNumberId").val(invoiceNumber);
				$("#customerInvoice_shipDateID").val(aShipDate);
				$("#customerInvoice_lineshipDateID").val(aShipDate);
				$("#customerInvoice_proNumberID").val(proNumber);
				$("#customerInvoice_lineproNumberID").val(proNumber);
				$("#customerInvoice_subTotalID").val(formatCurrency(subTotal));
				$("#customerInvoice_frightID").val(formatCurrency(fright));
				$("#customerInvoice_taxId").val(formatCurrency(taxrate));
				$("#customerInvoice_totalID").val(formatCurrency(costTotal));
				$("#customerInvoice_linesubTotalID").val(formatCurrency(subTotal));
//				$("#customerInvoice_linefrightID").val(formatCurrency(fright));
				$("#customerInvoice_linetaxId").val(formatCurrency(taxrate));
				$("#customerInvoice_linetotalID").val(formatCurrency(costTotal));
				$("#customerInvoice_ID").val(cuInvoiceID);
				if(doNotMAil == 1){
					$("#customerInvoie_doNotMailID").attr("checked", true);
				}
				$("#customer_Divisions option[value=" + divisionID + "]").attr("selected", true);
				$("#prWareHouseSelectID option[value=" + prWarehouse + "]").attr("selected", true);
				$("#shipViaCustomerSelectID option[value=" + shipViaID + "]").attr("selected", true);
				$("#prWareHouseSelectlineID option[value=" + prWarehouse + "]").attr("selected", true);
				$("#shipViaCustomerSelectlineID option[value=" + shipViaID + "]").attr("selected", true);
				var aPostChk = data.usePostDate;
				if(aPostChk === true){
					$("#postDateChkID").prop("checked", true);
					$("#postDateID").show(); 
				}
				$.ajax({
					url: "./jobtabs5/getAssigneeDetails",
					type: "POST",
					data : {'assignee0' : assign0,  'assignee1' : assign1, 'assignee2' : assign2, 'assignee3' : assign3, 'assignee4' : assign4},
					success: function(data) {
						$("#customerInvoice_salesRepsList").val(data.cuAssignmentId0);
						$("#customerInvoice_CSRList").val(data.cuAssignmentId1);
						$("#customerInvoice_SalesMgrList").val(data.cuAssignmentId2);
						$("#customerInvoice_EngineersList").val(data.cuAssignmentId3);
						$("#customerInvoice_PrjMgrList").val(data.cuAssignmentId4);
					}
				});
			}
		}
	});
	return true;
}

/** Save Customer PO Number **/

function saveCustomerPONumber(){
	if(!$('#customerPOFormID').validationEngine('validate')) {
		return false;
	}
	var grid = $("#release");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var aReleaseDetailID = grid.jqGrid('getCell', rowId, 'joReleaseDetailid');
	var aJoMasterID = $("#joMaster_ID").text();
	var customerPO = $("#customerPONumberID").val().replace("$ ", "");
	$.ajax({
		url: "./jobtabs5/saveCustomerPONumber",
		type: "GET",
		data : {"customerPONumber" : customerPO, 'releaseDetail' : aReleaseDetailID, 'joMasterID' : aJoMasterID },
		success: function(data) {
			var errorText = "Customer PO Number Saved Successfully.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color: green;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#release").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
	return false;
}

function removeDollorSym(){
	var customerPO = $("#customerPONumberID").val().replace("$ ", "");
	$("#customerPONumberID").val(customerPO);
	return false;
}

function loadCustomerPONumber(){
	var joMasterID = $("#joMaster_ID").text();
	var CO1 = ''; var CO2 = ''; var CO3 = ''; var CO4 = ''; var CO5 = '';
	$.ajax({
		url: "./jobtabs5/getCustomerPO", type: "POST", data: { 'joMasterId' : joMasterID },
		success: function(data) {
			var select = '<select id="customerPONumberID"><option value="-1"></option>';
			var aJoMasterCO = $("#customerpodetails").val();
			if(aJoMasterCO !== "")
				select +='<option value=CustomerPONumber1>'+aJoMasterCO+'</option>';
			$.each(data, function(index, value){
				if(CO1 !== "")
					CO1 = value.customerPonumber1;
					select +='<option value=CustomerPONumber2>'+CO1+'</option>';
				if(CO2 !== "")
					CO2 = value.customerPonumber2;
					select +='<option value=CustomerPONumber3>'+CO2+'</option>';
				if(CO3 !== "")
					CO3 = value.customerPonumber3;
					select +='<option value=CustomerPONumber4>'+CO3+'</option>';
				if(CO4 !== "")
					CO4 = value.customerPonumber4;
					select +='<option value=CustomerPONumber5>'+CO4+'</option>';
				if(CO5 !== "")
					CO5 = value.customerPonumber5;
					select +='<option value=CustomerPONumber6>'+CO5+'</option>';
			});
			select += '</select>';
			$('#customerPONumberSelectID').empty();
			$('#customerPONumberSelectID').append(select);
		}
	});
}
/**
 * method to get the purchase order general detail to load in vendor invoice details
 * @param vepoID
 */
function loadPOGeneralDetails (vepoID) {
	$.ajax({
		type : "GET",
		url : "./jobtabs5/getPOGeneralDetails",
		data : { 'vepoID' : vepoID},
		async: false,
		success : function (data) {
			$('#freight_ID').val(formatCurrency(data.freight));
			$('#tax_ID').val(formatCurrency(data.taxTotal));
		},
		error : function (msg) {}
	});
}