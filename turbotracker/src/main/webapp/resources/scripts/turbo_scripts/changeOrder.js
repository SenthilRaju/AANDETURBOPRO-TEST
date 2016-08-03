var newDialogDiv = jQuery(document.createElement('div'));
var aOper;
jQuery(document).ready(function(){
	 changeOrderGrid();/*
try{
	jQuery("#Date").datepicker();
	alert('after');
}catch(err){
	alert(err.message);
}*/
	 

	 /*$("#Date").datepicker({
	      showOn: "button",
	      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
	      buttonImageOnly: true
	    });*/
});

function changeOrderGrid(){
	$("#changeOrderGrid").jqGrid({
		url:'./jobtabs5/changeOrderList',
		datatype: 'JSON',
		mtype: 'POST',
		colNames: ["Date","PO Number","Entered By","Reason for Change","Amount","joMasterID" ,"joChangeID","ChangeById", 'Cost'],
		colModel :[
	        {name:'changdate', index:'changdate', align:'center', width:40, editable:true,hidden:false,editoptions:{size:20,readonly:false,dataInit:function(element) {$(element).datepicker({dateFormat: 'mm/dd/yy'});}},editrules:{required: false}},
	        {name:'customerPonumber', index:'customerPonumber', align:'center', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'changeByName', index:'changeByName', align:'center', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'changeReason', index:'changeReason', align:'left', width:60, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'changeAmount', index:'changeAmount', align:'right', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}, formatter:customCurrencyFormatter},
	        {name:'joMasterId', index:'joMasterId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'joChangeId', index:'joChangeId', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'changeById', index:'changeById', align:'left', width:30, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
	        {name:'changeCost', index:'changeCost', align:'right', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}, formatter:customCurrencyFormatter}],
	        postData: {jobNumber: function() { return $("#jobNumber_ID").text(); },joMasterID: $("#joMaster_ID").text()},
	        loadComplete: function(data) {
	        	
//	        	calculatePrice();
	        	
	        	
	        	var gridData = jQuery("#changeOrderGrid").getRowData();
	    		var totalPrice = 0;
	    		for(var index = 0; index < gridData.length; index++){
	    			var rowData = gridData[index];
	    			var price = rowData["changeAmount"].replace(/[^0-9\.]+/g,"");
	    			totalPrice = totalPrice + Number(price);
	    		}
	    		var amount = $("#contractAmount_release").val();
	    		if (typeof (amount) != 'undefined'){
	    			amount = amount.replace(/[^0-9\.]+/g,"");
	    		}
	    		
	    		amount = amount+totalPrice;
//	    		$('#estimate').text(formatCurrency(amount));
//	        	$("#unAllocated").empty();
//	        	$("#unAllocated").append(formatCurrency(totalPrice));
	        },
	        rowNum: 0, 
	        pgbuttons: false, 
	        recordtext: '', 
	        rowList: [], 
	        pgtext: null, 
	        viewrecords: false,
			sortname: 'Name', 
			sortorder: "asc", 
			width: 950, 
			altRows:true, 
			altclass:'myAltRowClass',
	        jsonReader : {
				root: "rows",
				page: "page",
				total: "total",
				records: "records",
				repeatitems: false,
				cell: "cell",
				id: "id",
				userdata: "userdata"
			}
	});
}

jQuery(function () {
	jQuery( "#changeorder" ).dialog({
		autoOpen: false,
		closeOnEscape: false,
		width: 970,
		title:"Change Order ",
		modal: true,	
		buttons:{} 
	});
});

function addOrder(){
	aOper = "add";
	document.getElementById("changeOrderFormID").reset();
	jQuery("#changeOrderDialog").dialog("open");
	return true;
}
	
function editOrder(){
	aOper = "edit";
	var grid = $("#changeOrderGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a order from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var adate = grid.jqGrid('getCell',rowId,'changdate');
	var aponumber = grid.jqGrid('getCell', rowId, 'customerPonumber');
	var aenteredby = grid.jqGrid('getCell', rowId, 'changeByName');
	var areasonforchange = grid.jqGrid('getCell', rowId, 'changeReason');
	var aamount = grid.jqGrid('getCell', rowId, 'changeAmount').replace(/[^0-9\.]+/g,"");
	var ajomasterid = grid.jqGrid('getCell', rowId, 'joMasterId');
	var ajochangeid = grid.jqGrid('getCell', rowId, 'joChangeId');
	var achangebyid = grid.jqGrid('getCell', rowId, 'changeById');
	var aCost = grid.jqGrid('getCell', rowId, 'changeCost').replace(/[^0-9\.]+/g,"");
	$("#Date").val(adate);
	$("#PONumber").val(aponumber);
	$("#EnteredBy").val(aenteredby);
	$("#ReasonForChange").val(areasonforchange);
	$("#Amount").val(aamount);
	$("#joMasterID").val(ajomasterid);
	$("#joChangeID").val(ajochangeid);
	$("#ChangeById").val(achangebyid);
	$("#cost_id").val(aCost);
	jQuery("#changeOrderDialog").dialog("open");
	return true;
}

function saveOrder(){
	if(!$('#changeOrderFormID').validationEngine('validate')) {
		return false;
	}
	var aOrderDetails = new Array();
	aOrderDetails.push($("#releaseddate").val());
	aOrderDetails.push($("#PONumber").val());
	aOrderDetails.push($("#EnteredBy").val());
	aOrderDetails.push($("#ReasonForChange").val());
	aOrderDetails.push($("#Amount").val());
	aOrderDetails.push($("#joMaster_ID").text());
	aOrderDetails.push($("#joChangeID").val());
	aOrderDetails.push($("#ChangeById").val());
	aOrderDetails.push($("#cost_id").val());
	aOrderDetails.push(aOper);
	$.ajax({
		url: "./jobtabs5/saveChangeOrderDetails",
		type: "POST",
		data : {'changeOrderDetails' : aOrderDetails},
		success: function(data) {
			if(aOper === 'add'){
				var validateMsg = "<b style='color:Green; align:right;'>Change Order Details Added.</b>";	
				$("#changeOrderMsg").css("display", "block");
				$('#changeOrderMsg').html(validateMsg);
				setTimeout(function(){
				$('#changeOrderMsg').html("");						
				},2000);
				 $("#changeOrderDialog").dialog("close"); 
				 $("#changeOrderGrid").trigger("reloadGrid"); 
				 $("#release").trigger("reloadGrid"); 
				 
				 	var grid = $("#changeOrderGrid");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var aponumber = grid.jqGrid('getCell', rowId, 'customerPonumber');
					
					
			}else if(aOper === 'edit'){
				var validateMsg = "<b style='color:Green; align:right;'>Change Order Details Updated.</b>";	
				$("#changeOrderMsg").css("display", "block");
				$('#changeOrderMsg').html(validateMsg);
				setTimeout(function(){
				$('#changeOrderMsg').html("");						
				},2000);
				 $("#changeOrderDialog").dialog("close"); 
				 $("#changeOrderGrid").trigger("reloadGrid"); 
				 $("#release").trigger("reloadGrid");
				
			}
			
			 return true;
			 
		}
	});
}

function deleteOrder(){ 
	aOper = "del";
	var grid = $("#changeOrderGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a order from the Grid to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	jQuery(newDialogDiv).html('<span><b style="color: red;">Do You Want to Delete this Record?</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
		buttons:{
			"Submit": function(){
				var ajochangeid = grid.jqGrid('getCell', rowId, 'joChangeId');
				deleteChangeOrder(ajochangeid);
				jQuery(this).dialog("close");
			},
			Cancel : function ()	{
				jQuery(this).dialog("close");} }
	}).dialog("open");
	return true;
}

function deleteChangeOrder(JochangeId){
	aOper = "del";
	var aOrderDetails = new Array();
	aOrderDetails.push($("#Date").val());
	aOrderDetails.push($("#PONumber").val());
	aOrderDetails.push($("#EnteredBy").val());
	aOrderDetails.push($("#ReasonForChange").val());
	aOrderDetails.push($("#Amount").val());
	aOrderDetails.push($("#joMaster_ID").text());
	aOrderDetails.push(JochangeId);
	aOrderDetails.push($("#ChangeById").val());
	aOrderDetails.push($("#cost_id").val());
	aOrderDetails.push(aOper);
	$.ajax({
		url: "./jobtabs5/saveChangeOrderDetails",
		type: "POST",
		data : {'changeOrderDetails' : aOrderDetails},
		success: function(data) {
			if(aOper === 'del'){
				var validateMsg = "<b style='color:Green; align:right;'>Change Order Details Deleted.</b>";	
				$("#changeOrderMsg").css("display", "block");
				$('#changeOrderMsg').html(validateMsg);
				setTimeout(function(){
				$('#changeOrderMsg').html("");						
				},2000);
				 $("#changeOrderDialog").dialog("close"); 
				 $("#changeOrderGrid").trigger("reloadGrid");
				return true;
			}
		}
	});
}

jQuery(function(){
	jQuery("#changeOrderDialog").dialog({
		autoOpen:false,
		width:390,
		title:"Add/Edit changeorder",
		modal:true,
		buttons:{	},
		close:function(){
			$('#changeOrderFormID').validationEngine('hideAll');
			 return true;
		}	
	});
});

function cancelOrder(){
	$('#changeOrderFormID').validationEngine('hideAll');
	$("#release").trigger("reloadGrid");
	jQuery("#changeOrderDialog").dialog("close");
	return false;
}

function changeOrderClose(){
	$("#release").trigger("reloadGrid");
	jQuery( "#changeorder" ).dialog( "close" );
	loadBillingEntireJob();
	return false;
}
		
function changeorderdialog() {
	createtpusage('job-Release Tab','Change Order','Info','Job-Release Tab,Changing Order,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	jQuery( "#changeorder" ).dialog( "open" );
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

$(function() { var cache = {}; var lastXhr='';
$( "#EnteredBy" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/userInitials", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
