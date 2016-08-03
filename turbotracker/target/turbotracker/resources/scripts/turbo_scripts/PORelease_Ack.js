var 	aShipDate;
var 	aAckDate;
var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
		$(".datepicker").datepicker();
		if(aPurchaseOrderVar === "add"){
			$("#manufacture_ID").text(" ");
		}
		loadAck();
		$("#vendorAckNameId").val($("#vendor_ID").text()); $("#ourPoAckId").val($("#order_ID").text()); $("#subtotalKnowledgeId").val($("#Subtotal_ID").text()); 
		$("#totalKnowledgeId").val(Number($("#Total_ID").text())); $("#taxKnowledgeId").val($("#Tax_ID").text()); $("#freightKnowledgeId").val(formatCurrency(freight));  $("#KnowledgeID").val($("#Percentage_ID").text());
		$("#poDateAckId").val($("#poDate_ID").text());
		$("POReleaseID").css("display", "none");
		$('#loadingPOAckDiv').css({"visibility": "hidden"});
		var total=(Number($("#Subtotal_ID").text().replace(/[^0-9\.]+/g,"")) + Number($("#Freight_ID").text().replace(/[^0-9\.]+/g,"")));
		$("#totalKnowledgeId").val(formatCurrency(total));
});
	
function loadAck(){
	$("#Ack").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#Ackpager'),
		url:'./jobtabs5/jobReleaseAck',
		postData: {vePOID : function() { return $("#vePO_ID").text();}},
		colNames:['Product No','Description','Qty.','Ack.','Ship','Order #','VePOID','prMasterID','VePODetailID'],
		colModel:[
		          	{name:'note',index:'note',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false}},
		           	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
		          																													cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
					{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true}},
					{name:'ackDate', index:'ackDate', align:'center', width:50,hidden:false, editable:true,  editoptions:{size:15, align:'center',},editrules:{edithidden:true}},
					{name:'shipDate', index:'shipDate', align:'center', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'center'}, editrules:{edithidden:true}},
					{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
					{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
					{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
					{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}}],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:210,	width: 750, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Acknowledgment',
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
			loadComplete: function(data) { 	},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow: function(id){ },
			editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
		}).navGrid('#Ackpager', {add: false, edit:true,del: false,refresh:true,search: false},
				//-----------------------edit options----------------------//
				{
					width:300, left:700, top: 300, zIndex:1040,
					closeAfterEdit:true, reloadAfterSubmit:true,
					modal:true, jqModel:true,
					editCaption: "Edit Acknowledgment",
					beforeShowForm: function (form) 
					{
						/*$(".navButton").empty();
						$(".navButton").append('<td style="padding-left: 60px;"><input type="button" id="saveAllButton" class="savehoverbutton turbo-blue" value="All" onclick="saveAll()"></td>');*/
						var grid = $('#Ack');
						var id = jQuery("#Ack").jqGrid('getGridParam','selrow');
						var shipDate = grid.jqGrid('getCell', id, 'shipDate');
						var ackDate = grid.jqGrid('getCell', id, 'ackDate');
						jQuery("#tr_note", form).hide();
						jQuery("#tr_description", form).hide();
						jQuery("#tr_quantityOrdered", form).hide();
						jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').empty();
						jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').append('Ack.: ');
						jQuery('#TblGrid_Ack #tr_ackDate .DataTD').empty();
						jQuery('#TblGrid_Ack #tr_ackDate .DataTD').append('&nbsp;<input type="text" size="15" align="center" id="ackDate" name="ackDatename" role="textbox">&nbsp;');
						jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').empty();
						jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').append('Ship: ');
						jQuery('#TblGrid_Ack #tr_shipDate .DataTD').empty();
						jQuery('#TblGrid_Ack #tr_shipDate .DataTD').append('&nbsp;<input type="text" size="15" aligntext="center" id="shipDate" name="shipDatename" role="textbox">&nbsp;');
						jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').empty();
						jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').append('Order #: ');
						$("#ackDate").val(ackDate);
						$("#shipDate").val(shipDate);
						$( "#ackDate" ).datepicker({
						      showOn: "button",
						      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
						      buttonImageOnly: true
						    });
						$( "#shipDate" ).datepicker({
						      showOn: "button",
						      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
						      buttonImageOnly: true
						    });
					},
					onclickSubmit: function(params){
						var aAckDate =  $("#ackDate").val();
						var aShipDate =  $("#shipDate").val();
						return { 'operForAck' : 'acknowlegement', 'shipDatename' : aShipDate, 'ackDatename' : aAckDate };
					},
					afterSubmit:function(response,postData){
						 return [true, loadAck()];
					 }
				},
				{
					//-----------------------Add options----------------------//
						width:450, left:400, top: 300, zIndex:1040,
						closeAfterEdit:true, reloadAfterSubmit:true,
						modal:true, jqModel:true,
						editCaption: "Add Acknowledgment"
				}
		); 
}

function saveAll(){
	setTimeout("loadSaveAllFunction()", 500);
}

function loadSaveAllFunction(){
	var veAckPOId = new Array();
	var veShipPOId = new Array();
	var customerPOId = new Array();
	var gridData = jQuery("#Ack").getRowData();
	for(var index = 0; index < gridData.length; index++){
		var rowData = gridData[index];
		var ackDate = rowData["ackDate"];
		var shipDate = rowData["shipDate"];
		var orderNumber = rowData["vendorOrderNumber"];
		var vePoDetailID = rowData["vePodetailId"];
		if(ackDate === ''){
			veAckPOId.push(vePoDetailID);
		}if(shipDate === ''){
			veShipPOId.push(vePoDetailID);
		}if(orderNumber === ''){
			customerPOId.push(vePoDetailID);
		}
	}
	if(veShipPOId.length == 0 && veAckPOId.length == 0 && customerPOId.length == 0){
		$("#ackDateTR").show();
		$("#shipDateTR").show();
		$("#orderNumberTR").show();
	}else if(veAckPOId.length == 0 && veShipPOId.length == 0){
		$("#ackDateTR").css("display", "none");
		$("#shipDateTR").css("display", "none");
		$("#orderNumberTR").show();
	}else if(veShipPOId.length == 0 && customerPOId.length == 0){
		$("#shipDateTR").css("display", "none");
		$("#orderNumberTR").css("display", "none");
		$("#ackDateTR").show();
	}else if(customerPOId.length == 0 && veAckPOId.length == 0){
		$("#orderNumberTR").css("display", "none");
		$("#ackDateTR").css("display", "none");
		$("#shipDateTR").show();
	}
	$( "#ackDateTD" ).datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	$( "#shipDateTD" ).datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });
	$("#ackDateTD").val("");
	$("#shipDateTD").val("");
	$("#orderNumberTD").val("");
	jQuery( "#saveAllDialog" ).dialog("open");
	return false;
}

function saveAllAckAndShipDates(){
	var veAckPOId = new Array();
	var veShipPOId = new Array();
	var customerPOId = new Array();
	var veAckPOIdOver = new Array();
	var veShipPOIdOver = new Array();
	var customerPOIdOver = new Array();
	var gridData = jQuery("#Ack").getRowData();
	for(var index = 0; index < gridData.length; index++){
		var rowData = gridData[index];
		var ackDate = rowData["ackDate"];
		var shipDate = rowData["shipDate"];
		var orderNumber = rowData["vendorOrderNumber"];
		var vePoDetailID = rowData["vePodetailId"];
		if(ackDate === ''){
			veAckPOId.push(vePoDetailID);
		}if(shipDate === ''){
			veShipPOId.push(vePoDetailID);
		}if(orderNumber === ''){
			customerPOId.push(vePoDetailID);
		}
		veAckPOIdOver.push(vePoDetailID);
		veShipPOIdOver.push(vePoDetailID);
		customerPOIdOver.push(vePoDetailID);
	}
	if(veShipPOId.length == 0 && veAckPOId.length == 0 && customerPOId.length == 0){
		jQuery(newDialogDiv).html('<span><b style="color:green;">Do you need overwrite all values?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:130, title:"Message", 
		buttons:{
			"Submit": function(){
				var aAckDate = $("#ackDateTD").val();
				var aShipDate = $("#shipDateTD").val();
				var aOrderNumber = $("#orderNumberTD").val();
				veAckPOId = veAckPOIdOver;
				veShipPOId = veShipPOIdOver;
				customerPOId = customerPOIdOver;
				$.ajax({ 
					url: "./jobtabs5/manpulaterPOReleaseLineItem",
					type: "POST",
					data : {'ackPOID' : veAckPOId ,'shipPOID' : veShipPOId, 'operForAck' : 'all', 'ackDatename' : aAckDate, 'shipDatename' : aShipDate, 'oper' : 'edit', 'orderNumberPOID' : customerPOId, 'vendorOrderNumber' : aOrderNumber },
					success: function(data){
						$("#Ack").trigger("reloadGrid");
					}
				});
				jQuery(this).dialog("close");
			},
			Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
		return true;
	}else{
		var aAckDate = $("#ackDateTD").val();
		var aShipDate = $("#shipDateTD").val();
		var aOrderNumber = $("#orderNumberTD").val();
		$.ajax({ 
			url: "./jobtabs5/manpulaterPOReleaseLineItem",
			type: "POST",
			data : {'ackPOID' : veAckPOId ,'shipPOID' : veShipPOId, 'operForAck' : 'all', 'ackDatename' : aAckDate, 'shipDatename' : aShipDate, 'oper' : 'edit', 'orderNumberPOID' : customerPOId, 'vendorOrderNumber' : aOrderNumber },
			success: function(data){
				$("#Ack").trigger("reloadGrid");
			}
		});
	}
}

jQuery(function () {
	jQuery( "#saveAllDialog" ).dialog({
		autoOpen: false,
		width:280,
		title:"Edit Acknowledgment",
		modal: true,
		buttons:{	
			"Submit": function(){
				saveAllAckAndShipDates();
				jQuery(this).dialog("close");
				return true;
		},
			Cancel: function (){
				jQuery(this).dialog("close");
			}
		},
		close: function () {
			return true;
		}
	});
	return true;
});

function sendPOAckEmail(){
	var bidderGrid = $("#release");
	var aQuotePDF = "purchaseAck";
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var errorText = '';
	var cusotmerPONumber = $("#ourPoId").val();
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
						errorText = "Are you sure you want to send this PO Ack to"+ arxContact +"?";
					}else{
						errorText = "Are you sure you want to send this PO Ack to"+ arxContact +"("+ aEmail +")?";
					}
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
					buttons:{
						"Send": function(){
							$('#loadingPOAckDiv').css({"visibility": "visible"});
							openPOAckPDF(aQuotePDF);
							sentMailPOAckFunction(aContactID,aEmail, cusotmerPONumber);
							jQuery(this).dialog("close");
						},
						Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
					}
			});
			
		}
	return true;
}

function sentMailPOAckFunction(aContactID,aEmail, cusotmerPONumber){
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
					$('#loadingPOAckDiv').css({"visibility": "visible"});
					$.ajax({ 
						url: "./sendMailServer/sendPOAckMail",
						mType: "POST",
						data : {'contactID' : aContactID, 'POPDF' : 'poAck', 'PONumber' : cusotmerPONumber},
						success: function(data){
							$('#loadingPOAckDiv').css({"visibility": "hidden"});
							var errorText = "Mail Sent Successfully.";
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
		url: "./sendMailServer/sendPOAckMail",
		mType: "POST",
		data : {'contactID' : aContactID, 'POPDF' : 'poAck', 'PONumber' : cusotmerPONumber},
		success: function(data){
			$('#loadingPOAckDiv').css({"visibility": "hidden"});
			var errorText = "Mail Sent Successfully.";
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

function openPOAckPDF(aQuotePDF){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	if(aQuotePDF === 'purchaseAck'){
		$.ajax({
			   type : "GET",
			   url : "./purchasePDFController/viewPDFAckForm",
			   data : { 'vePOID' : vePOID, 'puchaseOrder' : aQuotePDF, 'rxMasterID' : rxMasterID, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aInteger, 'jobNumber' :  jobNumber },
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

function viewPOAckPDF1(){
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var aQuotePDF = "ack";
	window.open("./purchasePDFController/viewPDFAckForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aInteger+"&jobNumber="+ jobNumber);
	return true;
}
