jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	loadJournalsGrid();
	loadJournalDetailsGrid(-1);
	$('#date').datepicker();
	$('#DateOfJournal').datepicker();
	$(".charts_tabs_main").tabs({
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
	document.getElementById("chartsDetailsFromID").reset();
});

var newDialogDiv = jQuery(document.createElement('div'));

function loadJournalsGrid(){
	$("#JournalsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./journalscontroller/getListOfJournals',
		//pager: jQuery('#chartsOfAccountsGridPager'),
		colNames:['Description', 'ref', 'Posted Date', 'posted', 'sourceNo', 'coLedgerSourceId', 'coFiscalPeriodId', 'coLedgerHeaderId'],
	   	colModel:[
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'reference',index:'reference',align:'center', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'postDate',index:'postDate', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10},formatter:'date', formatoptions: {srcformat: 'yyyy-mm-dd hh:mm:ss', newformat: "d/m/y"}},
			{name:'posted',index:'posted', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'sourceNo',index:'sourceNo', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'coLedgerSourceId',index:'coLedgerSourceId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'coFiscalPeriodId',index:'coFiscalPeriodId', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'coLedgerHeaderId',index:'coLedgerHeaderId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfAccountsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Journals',
		height:424,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: true,
		loadComplete:function(data) {
		},
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
    		document.getElementById("chartsDetailsFromID").reset();
    		loadJournalRowDetails(rowId);
    		 $("#JournalDetailsGrid").jqGrid("setGridParam", {
    			 	url: './journalscontroller/getListOfJournalDetails',
    			 	mtype: 'POST',
    	            datatype: "json",
    	            postData: {coLedgerHeaderId: $('#headerid').val()}
    	        }).trigger("reloadGrid");

    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("chartsDetailsFromID").reset();
    		loadJournalRowDetails(rowId);
		}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function loadJournalDetailsGrid(coLedgerHeaderId) {
	$("#JournalDetailsGrid").jqGrid({
		datatype: 'JSON',
		postData: {'coLedgerHeaderId':coLedgerHeaderId },
		mtype: 'POST',
		url: './journalscontroller/getListOfJournalDetails',
		pager:jQuery("#EmployeeDetailsGridpager"),
		colNames: ['Account Number', 'Description', 'Debit', 'credit'],
		colModel:
		[	{name:'number',index:'number',align:'left',width:50, hidden: false, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 	{name:'description',index:'description',align:'left',width:50, hidden: false, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 	{name:'coAccountId',index:'coAccountId',align:'left',width:50, editable:true,editoptions:{size:30},editrules:{required:true}},
		 	{name:'coAccountId',index:'coAccountId',align:'left',width:30,editable:true,editoptions:{size:30},editrules:{required:true}}
		 ],
		 rowNum: 0,
		 sortname:' ',
		 sortorder:"asc",
		 imgpath:'themes/basic/images',
		 height:200,
		 pgbuttons: false,recordtext:'',rowList:[],pgtext: null,
		 altRows:true,
		 altclass:'myAltRowClass',
		 rownumbers:true,
	 	 width:790,
		 loadComplete:function(data) {
			$("#EmployeeDetailsGrid").setSelection(1, true);
		},
		onSelectRow : function(id) {
		},
		jsonReader:{
			root:"rows",
			page:"page",
			total:"total",
			records:"records",
			repeatitems:false,
			cell:"cell",
			id:"id",
			userdata:"userdata"
    	},
		loadComplete: function(data){
			jQuery("#JournalDetailsGrid").jqGrid('navGrid','#JournalDetailsGridpager',{edit:true,add:true,del:true,view:true,search:false,refresh:true});
		}
	});
	return true;
}
function loadJournalRowDetails(rowId){
	var rowData = jQuery("#JournalsGrid").getRowData(rowId); 
	var coLedgerSourceId = rowData['coLedgerSourceId'];
	var coLedgerHeaderId = rowData['coLedgerHeaderId'];
	var coFiscalPeriodId = rowData['coFiscalPeriodId'];
	var number = rowData['number'];
	var Description = rowData['description'];
	var reference = rowData['reference'];
	var postDate = rowData['postDate'];
	$("#Number").val(number);
	$("#decriptionNameID").val(Description);
	if(reference==='')
		$("#References").val("--");
	else
		$("#References").val(reference);
	$('#DateOfJournal').val(postDate);
	$('#headerid').val(coLedgerHeaderId);
	$('#fisicalperiodid').val(coFiscalPeriodId);
	$('#coLedgerSourceId').val(coLedgerSourceId);
	$("#list").setGridParam().trigger('reloadGrid');
	loadJournalDetailsGrid(coLedgerHeaderId);
	$('#JournalDetailsGrid').jqGrid({datatype:'json', postData: { 'coLedgerHeaderId': coLedgerHeaderId },url:'./journalscontroller/getListOfJournals'});
	return true;
}


function openAddNewjournalDialog(){
	document.getElementById("addNewjournalFormID").reset();
	jQuery("#addNewJournalDialog").dialog("open");	
	return true;
}

jQuery( function(){
	jQuery("#addNewJournalDialog").dialog({
		autoOpen:false,
		height:240,
		width:350,
		title:"Add New Journal",
		modal:true,
		close:function(){
			//$('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

function cancelAddJournal(){
	jQuery("#addNewJournalDialog").dialog("close");
	return true;
}

function saveNewJournal(){
	var aChartDetais = $("#addNewjournalFormID").serialize();
	$.ajax({
		url: "./journalscontroller/updateData",
		type: "POST",
		data : aChartDetais,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Journal Added Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("addNewjournalFormID").reset(); jQuery("#addNewJournalDialog").dialog("close");location.reload(true);  }}]}).dialog("open");
		}
	});
	return true;
}


function deleteJournalDialog(){
	var grid = $("#JournalsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Do you really want to delete the journal?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					DeleteJournalRecord(); 
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Account to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
}



function DeleteJournalRecord(){
	var additionalInfo = $("#chartsDetailsFromID").serialize();
	$.ajax({
		url: "./journalscontroller/deleteRecord",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Journal Deleted successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset();location.reload(true);}}]}).dialog("open");
		}
	});
}

function saveEditedJournalData() {
	var additionalInfo = $("#chartsDetailsFromID").serialize();
	$.ajax({
		url: "./journalscontroller/updateData",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Journal details are Updated successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset(); $("#JournalsGrid").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
}

