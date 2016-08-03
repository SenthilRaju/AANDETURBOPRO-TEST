var specialKeys = new Array();
specialKeys.push(8); //Backspace 
specialKeys.push(46); //Period
function IsNumeric(e) {
    var keyCode = e.which ? e.which : e.keyCode
    console.log(keyCode);
    var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys.indexOf(keyCode) != -1);
    return ret;
}


function loadCommissionShipingGrid(joDetailId,releaseType){
	 //<table id="shiping" style="width:20px"></table><div id="shipingpager"></div>
	
	 document.getElementById("customerInvoicebtnID").disabled = true;
	 $('#customerInvoicebtnID').css('background','-webkit-gradient(linear, left top, left bottom, from(#FFD499), to(#8E6433))');
	 $("#shipingGrid").empty();
	 $("#shipingGrid").append("<table id='commissionReleaseGrid'></table><div id='commissionReleasePager'></div>");
	 //$("#commissionReleaseGrid").jqGrid('GridUnload');
	 try{
	var jobnumber = $("#jobNumber_ID").text();
	
	if(joDetailId==undefined || releaseType==undefined){
		return false;
	}
	
	 $("#commissionReleaseGrid").jqGrid({
	url:'./jobtabs5/commissionInvoiceList?jobNumber='+jobnumber+'&joDetailsID='+joDetailId+'&releaseType='+releaseType,
	datatype: 'JSON',
	mtype: 'GET',
	colNames:['Ship Date','Shipping Line ','Vendor Date','Vendor Invoice','Vendor Amount ($)','Commission Date','Commission Amount ($)', 'ShipID', 'JoReleaseID', 'VeBillID'],
	colModel:[
		{name:'shipDate',index:'shipDate',align:'center',width:50},
		{name:'shippingLine',index:'shippingLine',align:'center',width:50},
		{name:'vendorDate',index:'vendorDate',align:'center',width:60}, 
		{name:'vendorInvoice',index:'vendorInvoice',align:'center',width:50}, 
		{name:'vendorAmount',index:'vendorAmount',align:'right',width:60, formatter:customCurrencyFormatter}, 
		{name:'customerDate',index:'customerDate',align:'center',width:70}, 
		{name:'customerAmount',index:'customerAmount',align:'right',width:70, formatter:customCurrencyFormatter},
		{name:'veShipViaID',index:'veShipViaID',align:'right',width:60, hidden: true},//hidden: true changed to false for testing
		{name:'joReleaseDetailID',index:'joReleaseDetailID',align:'right',width:60, hidden: true},
		{name:'veCommDetailID',index:'veCommDetailID',align:'right',width:60, hidden: true}],
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
		height:86,	width: 1080, altRows: true, altclass:'myAltRowClass',rownumbers:true,
		loadComplete: function(data) {
			$("#financial").trigger("reloadGrid");
			
			//$("#shiping").setSelection(1, true);
			getCommissionAmount();
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
				var veCommissionDetailID = rowData["veCommDetailID"];
				$("#veCommDetailIdVal").val(veCommissionDetailID);
				//alert(veCommissionDetailID);
				//loadCommissionInvoiceDetails(veCommissionDetailID);
				//CheckCustomerInvoiceSave();
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
}catch(err){
	 alert(err.message);
}
}



function loadCommissionInvoiceDetails(veCommissionDetailID){
	commissionReleaseGrid
	$.ajax({
		url: "./jobtabs5/getVeCommissionDetails", 
		mType: "GET", 
		data : { 'veCommissionDetailID' : veCommissionDetailID},
		success: function(data){
			console.log(data);
			var shipDate = new Date(data.shipDate);
			var commDate = new Date(data.commDate);
			$("#commissionDialogShipDateVal").val(getCommissionFormattedDate(shipDate));
			$("#commissionDialogShipVIAVal").val(data.veShipViaId);
			$("#commissionDialogTrackingPROVal").val(data.trackingNumber);
			$("#commissionDialogVendorInvoiceVal").val(data.invoiceNumber);
			$("#commissionDialogInvoiceAmtVal").val(formatCurrency(data.billAmount));
			$("#commissionDialogCommissionDateVal").val(getCommissionFormattedDate(commDate));
			$("#commissionDialogCommissionsAmtVal").val(formatCurrency(data.commAmount));
			$("#veCommDetailIdVal").val(data.veCommDetailId);
			
			//private Integer rxMasterId;
			//private Integer joReleaseDetailId;

		}
	});
}

function openCommissionDialog(operation) {
	 
	var rowId = $("#release").jqGrid('getGridParam', 'selrow');
	var id = $("#release").jqGrid('getCell', rowId, 'shipToAddressID');
	var aJoMasterID = $("#joMaster_ID").text();
	
	var jobnumber=$("#jobNumber_ID").text();
	var vePoID =  $("#release").jqGrid('getCell', rowId, 'vePoId');
	$("#vePO_ID").text(vePoID);
	var shipViaId ='';
	$.ajax({
		url: "./jobtabs3/getPurchaseDetails", 
		mType: "GET", 
		data : { 'vePoID' : vePoID,'jobNumber':jobnumber },
		success: function(data){
			console.log(data);
			var releaseId = data.joReleaseId;
			shipViaId = data.veShipViaId;
			console.log('CommissionShipViaID::'+shipViaId);
			//$("#commissionDialogShipVIAVal").val(shipViaId);
		}
	});
	var veCommdetailID = $("#veCommDetailIdVal").val();
	console.log("veCommdetailID##"+veCommdetailID+"##");
	if(operation==='new'){
	//if(veCommdetailID==null && veCommdetailID==''){
		$("#veCommDetailIdVal").val('');
		var date = new Date();
		var dateVal = (date.getMonth()+1)+"/"+date.getDate()+"/"+date.getFullYear();
		$('#commissionDialogShipDateVal').val(dateVal);
		$('#commissionDialogShipDateVal').datepicker( "destroy" );
		$('#commissionDialogCommissionDateVal').val(dateVal);
		$("#commissionDialogTrackingPROVal").val('');
		$("#commissionDialogVendorInvoiceVal").val('');
		$("#commissionDialogInvoiceAmtVal").val('$0.00');
		$("#commissionDialogCommissionsAmtVal").val('$0.00');
		
	}
	else{
		$('#commissionDialogShipDateVal').blur();
	}
	$('#commissionDialogTrackingPROVal').focus();
	jQuery("#commissionDialogBox").dialog("open");
	 return true;
}

/*	 $("#commissionDialogCommissionsAmtVal").live('keyup', function(event) {
			
		 if (this.value.match(/[^0-9]/g)) {
	        this.value = this.value.replace(/[^0-9]/g, '');
	        $("#commissionDialogCommissionsAmtVal").val('');
	    }*/

function submitCommissionDialog() {
	var rowId = $("#release").jqGrid('getGridParam', 'selrow');
	var id = $("#release").jqGrid('getCell', rowId, 'joReleaseId'); 
	var aJoMasterID = $("#joMaster_ID").text();
	var aCommissionDetailsData = $("#commissionDialogForm").serialize();
	var veCommdetailID = $("#veCommDetailIdVal").val();
	console.log('veCommdetailID::'+veCommdetailID);
	var commissionData =aCommissionDetailsData+ "&joReleaseID="+id+"&joMasterID="+aJoMasterID;
		$.ajax({
			url: "./jobtabs5/saveCommissionInvoice",
			type: "POST",
			async:false,
			data : commissionData,
			success: function(data){
				console.log('JOReleaseDetailID:'+data);
				$('#commissionDialogFOrm').validationEngine('hideAll');
				jQuery("#commissionDialogBox").dialog("close");
				loadCommissionShipingGrid(id,'Commission');
				
			}
	   });
		
}

function cancelCommissionDialog() {
	$('#commissionDialogFOrm').validationEngine('hideAll');
	jQuery("#commissionDialogBox").dialog("close");
}

function getCommissionFormattedDate(date) {
	  var year = date.getFullYear();
	  var month = (1 + date.getMonth()).toString();
	  month = month.length > 1 ? month : '0' + month;
	  var day = date.getDate().toString();
	  day = day.length > 1 ? day : '0' + day;
	  return  month+ '/' + day + '/' + year;
}


