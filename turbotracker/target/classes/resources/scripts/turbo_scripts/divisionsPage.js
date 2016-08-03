jQuery(document).ready(function() {
	$( "#search" ).hide();
	$("#descriptionWarning").hide();
	$("#addressWarning").hide();
	loadDivisions();
	$(".division_tabs_main").tabs({
		cache: true,
		ajaxOptions: {
			data: {  },
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
});
var newDialogDiv = jQuery(document.createElement('div'));

function loadDivisions(){
	$("#divisionsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./company/divisionList',
	   	colNames:['Code', 'Description', 'CoDivisionID', 'InActive', 'UseInvoiceSeqNo', 'InvoiceSeqNo', 'AddressQuote', 'Address1', 'Address2', 'Address3',
	   	'SubAccount', 'AccountDistribution', 'Address4', 'Additional1', 'Additional2', 'Additional3'],
	   	colModel:[{name:'code',index:'code', width:40,editable:false, hidden:false, editrules:{required:true}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'coDivisionId',index:'coDivisionId',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'useInvoiceSeqNo',index:'useInvoiceSeqNo', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'invoiceSeqNo',index:'invoiceSeqNo', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'addressQuote',index:'addressQuote', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'address1',index:'address1', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'address2',index:'address2', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'address3',index:'address3', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'subAccount',index:'subAccount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'accountDistribution',index:'accountDistribution', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'address4',index:'address4', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'additional1',index:'additional1', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'additional2',index:'additional2', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'additional3',index:'additional3', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
		rowNum: 500,	
		pgbuttons: false,	
		recordtext: '',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Divisions',
		height:350,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
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
    	onSelectRow: function(rowId){
    		loadDivisionsDetails(rowId);
    		$("#alternateGrid").jqGrid('GridUnload');
    		loadAlternate();
			$("#alternateGrid").trigger("reloadGrid");
    	},
    	ondblClickRow: function(rowId) {
    		loadDivisionsDetails(rowId);
		}
	});
	return true;
}
function loadAlternate(){
	CoDivisionId = $('#coDivisionID').val();
	$("#alternateGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		pager: jQuery('#alternateGridPager'),
		postData:{coDivisionId:CoDivisionId},
		loadOnce:false,
		url:'./company/divisionAlternateList',
	 	colNames:['coAccountPostId', 'coAccountAlternateId', 'CoDivisionID', 'CoDivisionPostingID','Original Account','Alternate Account','Original Number','Alternate Number'],
	 	 	   	colModel:[{name:'coAccountPostId',index:'coAccountPostId', width:120,editable:true, hidden:true, editrules:{required:false}, editoptions:{size:10}},
	 	 			{name:'coAccountAlternateId',index:'coAccountAlternateId', width:90,editable:false,hidden:true, editrules:{required:true}, editoptions:{size:10}},
	 	 			{name:'coDivisionId',index:'coDivisionId',align:'center', width:90,editable:true, hidden:true, editrules:{required:false}, editoptions:{size:10}},
	 	 			{name:'coDivisionPostingId',index:'coDivisionPostingId', width:90,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:10}},
	 	 			{name:'originalAccount',index:'Description', width:90,editable:true, editrules:{required:true}, hidden:false, editoptions:{}},
	 	 			{name:'alternateAccount',index:'Des', width:90,editable:true, editrules:{required:true}, hidden:false, editoptions:{}},
	 	 			{name:'originalID',index:'coAccountID', width:90,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:30}},
	 	 			{name:'alternateID',index:'AltID', width:90,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:30}}],
		rowNum: 200,pgtext: null,
		pgbuttons: false,	
		recordtext: '',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Accounts',
		height:150,	width: 400,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
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
    	alertzIndex:1050,
		editurl:'./company/manpulaterDivisionAccount',
	}).navGrid('#alternateGridPager', {add:true, edit:true, del:true, search:false, view:false},
	//-----------------------edit options----------------------//
	{
		width:450, left:400, top: 300, zIndex:1040,
		closeAfterEdit:true, reloadAfterSubmit:true,
		modal:true, jqModel:true,
		editCaption: "Edit Accounts",
		beforeShowForm: function (form) {
			$(function() { var cache = {}; var lastXhr=''; $("input#originalAccount").autocomplete({minLength: 1,timeout :1000,
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "./company/coAccountDivisionList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
				select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#originalID").val(ID);
				 },
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
			return;
			});
			$(function() { var cache = {}; var lastXhr=''; $("input#alternateAccount").autocomplete({minLength: 1,timeout :1000,
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "./company/coAccountDivisionList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
				select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#alternateID").val(ID);},
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
			return;
			});
	  },
	  beforeSubmit:function($form){
			if($('#originalID').val()===''){ return [false, "Please Select Original Account from suggest list."]; }	
			if($('#alternateID').val()===''){ return [false, "Please Select Alternate Account from suggest list."]; } 
			return [true, ""];
		},
	  onclickSubmit: function(params){
			var id = $('#coDivisionID').val();
			return { 'coDivisionId' : id};
		}
	},
	//-----------------------add options----------------------//
	{
		width:450, left:400, top: 300, zIndex:1040,
		closeAfterAdd:true,	reloadAfterSubmit:true,
		modal:true, jqModel:false,
		addCaption: "Add Accounts",
		afterShowForm: function($form) {

			$(function() { var cache = {}; var lastXhr=''; $("input#originalAccount.FormElement").autocomplete({minLength: 1,timeout :1000,
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "./company/coAccountDivisionList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
				select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#originalID").val(ID);},
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
			return;
			});
			$(function() { var cache = {}; var lastXhr=''; $("input#alternateAccount.FormElement").autocomplete({minLength: 1,timeout :1000,
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
					lastXhr = $.getJSON( "./company/coAccountDivisionList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
				select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#alternateID").val(ID);
				},
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
				}); 
			return;
			});
		},
		beforeSubmit:function($form){
			if($('#originalID').val()===''){ return [false, "Please Select Original Account from suggest list."]; }	
			if($('#alternateID').val()===''){ return [false, "Please Select Alternate Account from suggest list."]; } 
			return [true, ""];
		},
		onclickSubmit: function(params){
			var id = $('#coDivisionID').val();
			return { 'coDivisionId' : id};
		}
		},
	//-----------------------Delete options----------------------//
	{	
		closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
		caption: "Delete Account",
		msg: 'Delete the Account Record?',

		onclickSubmit: function(params){
			var id = jQuery("#alternateGrid").jqGrid('getGridParam','selrow');
			var key = jQuery("#alternateGrid").getCell(id, 4);
			return { 'coDivisionId' : key };
		}
	});
	return true;
}
function loadDivisionsDetails(rowId){
	var rowData = jQuery("#divisionsGrid").getRowData(rowId); 
	var CoDivisionId = rowData['coDivisionId'];
	var description = rowData['description'];
	var code = rowData['code'];
	var inActive = rowData['inActive'];
	var useInvSeqNo = rowData['useInvoiceSeqNo'];
	var invSeqNo = rowData['invoiceSeqNo'];
	var address1 = rowData['address1'];
	var address2 = rowData['address2'];
	var address3 = rowData['address3'];
	var address4 = rowData['address4'];
	var additional1 = rowData['additional1'];
	var additional2 = rowData['additional2'];
	var additional3 = rowData['additional3'];
	var subAccount = rowData['subAccount'];
	var accountDis = rowData['accountDistribution'];
	var addressQuote = rowData['addressQuote'];
	$("#CoDivisionNameID").val(description);
	$("#CoDivisionCodeID").val(code);
	$("#addressID1").val(address1);
	$("#addressID2").val(address2);
	$("#addressID3").val(address3);
	$("#addressID4").val(address4);
	$("#additionalID1").val(additional1);
	$("#additionalID2").val(additional2);
	$("#additionalID3").val(additional3);
	$("#invSeqNoID").val(invSeqNo);
	$('#bankAccountsID option[value='+subAccount+']').attr('selected','selected');
	
	$("#accountDisPercentID").val(accountDis);
	$("#coDivisionID").val('');
	$("#coDivisionID").val(CoDivisionId);
	if(inActive === 'true'){
		$("#inActiveNameID").attr("checked", true);
	}else{
		$("#inActiveNameID").attr("checked", false);
	}
	if(addressQuote === 'true'){
		$("#addressQuoteID").attr("checked", true);
	}else{
		$("#addressQuoteID").attr("checked", false);
	}
	if(useInvSeqNo === 'true'){
		$("#useInvSeqNoId").attr("checked",true);
	}else{
		$("#useInvSeqNoId").attr("checked", false);
	}
	return true;
}

function openAddNewDivisionDialog(){
	document.getElementById("addNewDivisionFormID").reset();
	jQuery("#addNewDivisionDialog").dialog("open");	
	$("#newDescriptionWarning").hide();
	$("#newAddressWarning").hide();
	$("#newchartaccountWarning").hide();
	
	return true;
}
jQuery( function(){
	jQuery("#addNewDivisionDialog").dialog({
		autoOpen:false,
		height:410,
		width:850,
		title:"Add New Division",
		modal:true,
		close:function(){
			return true;
		}
	});
});
function resetform(){
//	$("#divisionDetailsFromID").reset();
	$('#coDivisionID').val('');
	$(':input','#divisionDetailsFromID').not(':button, :submit, :reset, :hidden') .val('').removeAttr('checked').removeAttr('selected');
	//$('#divisionDetailsFromID')[0].reset();
}
function saveNewDivisions(){
	var divisionsDetails = $("#addNewDivisionFormID").serialize();
	//var aDivisionDetails = $("#addNewDivisionFormID").serialize();
	var aDivisionDetails = $("#divisionDetailsFromID").serialize();
	console.log(aDivisionDetails +' :::  '+divisionsDetails );
	
	
	var description = $.trim($("#CoDivisionNameID").val());
	var address1 = $.trim($("#addressID1").val());
	var address2 = $.trim($("#addressID2").val());
	var address3 = $.trim($("#addressID3").val());
	var address4 = $.trim($("#addressID4").val());
	if(description === null || description === ""){
		$("#descriptionWarning").show();
		$("#addressWarning").hide();
		$("#chAccountWarning").hide();
		return false;
	}
	if( address1 === "" && address2 === "" && address3 === "" && address4 === ""){
		$("#addressWarning").show();
		$("#chAccountWarning").hide();
		$("#descriptionWarning").hide();
		return false;
	}
	if($('#bankAccountsID').val()==-1){
		$("#chAccountWarning").text("Please select "+$('#segment2name').text()+'.');
		$("#addressWarning").hide();
		$("#descriptionWarning").hide();
		$("#chAccountWarning").show();
		/*$("#errorDIV").css({ color: "red" });
		$("#errorDIV").text("Please select "+$('#segment2name').text()+'.');
		setTimeout(function(){
			$('#errorDIV').html("");
			},3000);*/
		return false;
	}
	console.log(aDivisionDetails);
	$.ajax({
		url: "./company/addNewDivisions",
		type: "POST",
		data : aDivisionDetails,
		success: function(data) {
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Division Added Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("divisionDetailsFromID").reset(); jQuery("#addNewDivisionDialog").dialog("close"); $("#divisionsGrid").trigger("reloadGrid");$("#newDescriptionWarning").hide();$("#newAddressWarning").hide(); }}]}).dialog("open");*/
			
			$("#errorDIV").css({ color: "green" });
			$("#errorDIV").text("New Division added.");
			setTimeout(function(){
				$('#errorDIV').html("");
				},4000);
			document.getElementById("divisionDetailsFromID").reset(); jQuery("#addNewDivisionDialog").dialog("close"); $("#divisionsGrid").trigger("reloadGrid");$("#newDescriptionWarning").hide();$("#newAddressWarning").hide();
		}
	});
	return true;
}

function cancelAddDivision(){
	jQuery("#addNewDivisionDialog").dialog("close");
	return true;
}

function deleteDivisionDetails(){
	var grid = $("#divisionsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Division Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var codivisionId = grid.jqGrid('getCell', rowId, 'coDivisionId');
					deleteDivision(codivisionId); 
					jQuery(this).dialog("close");
					$("#divisionsGrid").trigger("reloadGrid");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Division to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
}

function deleteDivision(coDivisionId){
	$.ajax({
		url: "./company/deleteDivision",
		type: "POST",
		data : {"coDivisionID": coDivisionId},
		success: function(data) {
			createtpusage('Company-Divisions','Delete Divisions','Info','Company-Divisions,Deleting Division,coDivisionId:'+coDivisionId);
			$("#errorDIV").css({ color: "red" });
			$("#errorDIV").text("Division deleted.");
			document.getElementById("divisionDetailsFromID").reset(); $("#divisionsGrid").trigger("reloadGrid");
			
			
			setTimeout(function(){
				$('#errorDIV').html("");
				},4000);
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;"> Division Deleted Successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");document.getElementById("divisionDetailsFromID").reset(); $("#divisionsGrid").trigger("reloadGrid"); }}]}).dialog("open");*/
		}
	});
}

function saveDivisions(){
	if($('#coDivisionID').val()==''){
		
		saveNewDivisions();
		return false;
	}
	var grid = $("#divisionsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Division from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}

	var description = $.trim($("#CoDivisionNameID").val());
	var address1 = $.trim($("#addressID1").val());
	var address2 = $.trim($("#addressID2").val());
	var address3 = $.trim($("#addressID3").val());
	var address4 = $.trim($("#addressID4").val());
	if(description === null || description === ""){
		$("#descriptionWarning").show();
		$("#addressWarning").hide();
		$("#chAccountWarning").hide();
		return false;
	}
	if( address1 === "" && address2 === "" && address3 === "" && address4 === ""){
		$("#addressWarning").show();
		$("#chAccountWarning").hide();
		$("#descriptionWarning").hide();
		return false;
	}
	if($('#bankAccountsID').val()==-1){
		$("#chAccountWarning").text("Please select "+$('#segment2name').text()+'.');
		$("#addressWarning").hide();
		$("#descriptionWarning").hide();
		$("#chAccountWarning").show();
		/*$("#errorDIV").css({ color: "red" });
		$("#errorDIV").text("Please select "+$('#segment2name').text()+'.');
		setTimeout(function(){
			$('#errorDIV').html("");
			},3000);*/
		return false;
	}
	var divisionsDetails = $("#divisionDetailsFromID").serialize();
	$.ajax({
		url: "./company/updateDivision",
		type: "POST",
		data : divisionsDetails,
		success: function(data) {
			
			createtpusage('Company-Divisions','Save Divisions','Info','Company-Divisions,Saving Division,description:'+description);
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;"> Division Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("divisionDetailsFromID").reset();$("#divisionsGrid").trigger("reloadGrid"); $("#descriptionWarning").hide();$("#addressWarning").hide();}}]}).dialog("open");*/
			
			$("#errorDIV").css({ color: "green" });
			$("#errorDIV").text("Division details updated.");
			
			document.getElementById("divisionDetailsFromID").reset();
			$("#divisionsGrid").trigger("reloadGrid"); 
			$("#descriptionWarning").hide();
			$("#addressWarning").hide();
			$("#chAccountWarning").hide();
			
			
			setTimeout(function(){
				$('#errorDIV').html("");
				},4000);
		}
	});
	return true;
}
