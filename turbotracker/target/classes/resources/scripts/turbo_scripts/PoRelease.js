/** PO Realse function **/
var po_General_tab_form;
jQuery(document).ready(function() {
	//setPoReleaseTotal();
	
	
	$(".datepicker").datepicker();
	$("#poreleasetab").tabs({
		cache: false,
		ajaxOptions: {
			data:{
				jobNumber: $("#jobNumber_ID").text(),
				jobName:"'" + $("#jobName_ID").text() + "'",
				rxManufactureID:$('#rxCustomer_ID').text(),
				joMasterID: $("#joMaster_ID").text(),
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
	});
	var currentTab= $("#poreleasetab").tabs('option', 'selected');
    var currentTabAnchor = $("#poreleasetab").data('tabs').anchors[currentTab];
    $(currentTabAnchor).data('cache.tabs', true);
    
    //for Status
    
    if($('#POtransactionStatus').val()=='2'){
		$('#PoStatusButton').val('Close');
	}
	else if($('#POtransactionStatus').val()=='0'){
		$('#PoStatusButton').val('Hold');
	}
	else if($('#POtransactionStatus').val()=='1'){
		$('#PoStatusButton').val('Open');
	}
	else if($('#POtransactionStatus').val()=='-1'){
		$('#PoStatusButton').val('Void');
	}
}); 

jQuery( "#porelease" ).dialog({
	autoOpen: false,
	width: 840,
	title:"Purchase Order",
	modal: true,
	open: function(){
		//GlobalPage_Validation=1;
		 $("#lineItemGrid").jqGrid('clearGridData');
		 $( "#poreleasetab ul li:nth-child(1)" ).removeClass("ui-state-disabled");
		 $( "#poreleasetab ul li:nth-child(2)" ).removeClass("ui-state-disabled");
		 $( "#poreleasetab ul li:nth-child(3)" ).removeClass("ui-state-disabled");
		 
      },
	closeOnEscape: false,
	open: function() {                         // open event handler
        $(this)                                // the element being dialogged
            .parent()                          // get the dialog widget element
            .find(".ui-dialog-titlebar-close") // find the close button for this dialog
            .hide();                           // hide it
    },
	close: function () {
		$("#note").autocomplete("destroy");
		 $(".ui-menu-item").hide();
		return true;
	}
});

function cancelPORelease(){
		jQuery("#porelease").dialog("close");
	$("#release").trigger("reloadGrid");
	return true;
}

function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}

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

function viewPOAckPDF(){
	createtpusage('job-Release Tab','PO-Cust Ack','Info','Job-Release Tab,PO-Cust Ack,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var bidderGrid = $("#release");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var vePOID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'vePoId');
	var aManufacturerId = bidderGrid.jqGrid('getCell',bidderGridRowId,'manufacturerId');
	var aShipToModeId = bidderGrid.jqGrid('getCell',bidderGridRowId,'shipToMode');
	var aInteger = Number(aShipToModeId);
	var jobNumber = $.trim($("#jobNumber_ID").text());
	var rxMasterID = $("#rxCustomer_ID").text();
	var joMasterID = $("#joMaster_ID").text();
	var aQuotePDF = "ack";
	window.open("./purchasePDFController/viewPDFAckForm?vePOID="+vePOID+"&puchaseOrder="+aQuotePDF+"&rxMasterID="+rxMasterID+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aInteger+"&jobNumber="+ jobNumber+"&joMasterID="+joMasterID);
	return true;
}

function callPOStatus(){
	createtpusage('job-Release Tab','PO-Status Button','Info','Job-Release Tab,Changing PO-Status,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	try{
		var dataid = $('#POtransactionStatus').val();
		console.log(dataid);
	    var newDialogDiv = jQuery(document.createElement('div'));
	    jQuery(newDialogDiv).attr("id", "showPOOptions");
	    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetPOStatus(this.value)" value="Void"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Hold" onclick="onSetPOStatus(this.value)" value="Hold"></span><br><br><span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="Open" onclick="onSetPOStatus(this.value)" value="Open"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Close" onclick="onSetPOStatus(this.value)" value="Close" /></span>');
	  
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 200,
			height : 220,
			title : "PO Status",
			buttons : [  ]
		}).dialog("open");
		
		$('div#showPOOptions').bind('dialogclose', function(event) {
			$("#showPOOptions").dialog("destroy").remove();
		 });
		console.log('dataid'+dataid);
		  if(dataid=='2'){
			  console.log('dataid inside'+dataid);
			   	$('#Close').css("font-weight","bold");
		    	$('#Close').css("background","#0E2E55");
		    }else  if(dataid=='0'){
			   	$('#Hold').css("font-weight","bold");
		    	$('#Hold').css("background","#0E2E55");
		    }else  if(dataid=='1'){
			   	$('#Open').css("font-weight","bold");
		    	$('#Open').css("background","#0E2E55");
		    }else  if(dataid=='-1'){
			   	$('#Void').css("font-weight","bold");
		    	$('#Void').css("background","#0E2E55");
		    }		
	}catch(err){
		
		}
}

function onSetPOStatus(e){
	
	$("#showPOOptions").dialog("destroy").remove();
	var setStatus=0;
	var vePoid =  $('#vePOID').val();
	switch(e)
	{
	case 'Void':
		$('#PoStatusButton').val("Void");
		setStatus = -1;
		break;
	case 'Hold':
		$('#PoStatusButton').val("Hold");
		setStatus = 0;
		break;
	case 'Open':
		$('#PoStatusButton').val("Open");
		setStatus = 1;
		break;
	default:
		$('#PoStatusButton').val("Close");
		setStatus = 2;
		break;
	}
	if(setStatus==-1){
		
		 var newDialogDiv = jQuery(document.createElement('div'));
		    jQuery(newDialogDiv).attr("id", "showPDFoptiondialog");
		    jQuery(newDialogDiv).html('<span><label>Do you wish to print a cancel ticket?</label></span>');
		  
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 200,
				height : 250,
				title : "Cancel Ticket",
				buttons : [{height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();viewPOPDFVoid();}},
				           {height:35,text: "No",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();}},
				           {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();}}]
			}).dialog("open");
		
	}
	$("#POtransactionStatus").val(setStatus);
	$('#showPOOptions').dialog('destroy').remove();
	if(setStatus==2){
		$.ajax({
		url: "./getPOinvoiceStatus",
		type: "POST",
		data :{vePoID:vePoid},
		success: function(data) {
			console.log("QtyOrdered: "+data);
			var orderedBilled = data.split(',');
			if(orderedBilled[0]!=orderedBilled[1]){
			 var newDialogDiv = jQuery(document.createElement('div'));
			    jQuery(newDialogDiv).attr("id", "showPOdialog");
			    jQuery(newDialogDiv).html('<span><label>Your Po Items not Invoiced !\n Do you wish Close Purchase Order? </label></span>');
			  
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 400,
					height : 150,
					title : "Close PO",
					buttons : [{height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#POtransactionStatus').val(2);}},
					           {height:35,text: "No",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#POtransactionStatus').val(1);}},
					           {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#POtransactionStatus').val(1);}}]
				}).dialog("open");
		}
			else{
				setStatus=2;
			}
		}
	});
	}
	$("#POtransactionStatus").val(setStatus);
	
}

function poGeneralTabFormValidation(){
	var value1=$("#locationbillToAddressID").val();	var value2=$("#locationbillToAddressID1").val();
	var value3=$("#locationbillToAddressID2").val();	var value4=$("#locationbillToCity").val();
	var value5=$("#locationbillToState").val();	var value6=$("#locationbillToZipID").val();
	var value7=$("#locationShipToAddressID").val();	var value8=$("#locationShipToAddressID1").val();
	var value9=$("#locationShipToAddressID2").val();	var value10=$("#locationShipToCity").val();
	var value11=$("#locationShipToState").val();	var value12=$("#locationShipToZipID").val();
	var value13=$("#orderId").val();	var value14=$("#frieghtChangesId").val();
	var value15=$("#shipViaId").val();	var value16=$("#wantedId").val();
	var value17=$("#wantedComboId").val();	var value18=$("#manfactureName").val();
	var value19=$("#contactId").val();	var value20=$("#poDateId").val();
	var value21=$("#customerNAMEId").val();	var value22=$("#ourPoId").val();
	var value23=$("#tagId").val();	var value24=$("#specialInstructionID").val();
	var value25=$("#subtotalGeneralId").val();	var value26=$("#freightGeneralId").val();
	var value27=$("#generalID").val();	var value28=$("#totalGeneralId").val();
	var value29=$("#shiptoradio1").is(":checked");var value30=$("#shiptoradio2").is(":checked");
	var value31=$("#shiptoradio3").is(":checked");var value32=$("#shiptoradio4").is(":checked");
	var value=value1+value2+value3+value4+value5+value6+value7+value8+value9+value10+value11+value12+value13+value14+
	value15+value16+value17+value18+value19+value20+value21+value22+value23+value24+value25+value26+value27+
	value28+value29+value30+value31+value32;
	return value;
}