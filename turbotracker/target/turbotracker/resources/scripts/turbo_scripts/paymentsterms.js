jQuery(document).ready(function() {
	loadChartsAccount();
	$('#search').css('visibility','hidden');
	$('#date').datepicker();
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
	document.getElementById("paymentTermsFromID").reset();
});

var newDialogDiv = jQuery(document.createElement('div'));

function loadChartsAccount(){
	$("#chartsOfTermsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./getcuTerms',
		
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['cuTermsId', 'Description', 'inActive', 'dueDays', 'discountDays', 'dueOnDay', 'discOnDay',
		          'isGlobal', 'discountPercent', 'orderNote', 'pickTicketNote1', 'pickTicketNote2', 'pickTicketNote3', 'pickTicketNote4', 'pickTicketNote5'],
	   	colModel:[
			{name:'cuTermsId',index:'cuTermsId', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description',align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'dueDays',index:'dueDays', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'discountDays',index:'discountDays', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'dueOnDay',index:'dueOnDay', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'discOnDay',index:'discOnDay', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'isGlobal',index:'isGlobal', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'discountPercent',index:'discountPercent', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'pickTicketNote1',index:'pickTicketNote1', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'pickTicketNote2',index:'pickTicketNote2', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'pickTicketNote3',index:'pickTicketNote3', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'pickTicketNote4',index:'pickTicketNote4', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'pickTicketNote5',index:'pickTicketNote5', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'orderNote',index:'orderNote', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Terms',
		height:398,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
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
    		document.getElementById("paymentTermsFromID").reset();
    		loadAccountsDetais(rowId);
    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("paymentTermsFromID").reset();
    		loadAccountsDetais(rowId);
		}
	})/*.navGrid('#chartsOfTermsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function loadAccountsDetais(rowId){
	var rowData = jQuery("#chartsOfTermsGrid").getRowData(rowId); 
	var description = rowData['description'];
	var inActive = rowData['inActive'];
	var dueDays = rowData['dueDays'];
	var discountDays = rowData['discountDays'];
	var cuTermsId = rowData['cuTermsId'];
	var dueOnDay = rowData['dueOnDay'];
	var discOnDay = rowData['discOnDay'];
	var isGlobal = rowData['isGlobal'];
	var discountPercent = rowData['discountPercent'];
	var pickTicketNote1 = rowData['pickTicketNote1'];
	var orderNote = rowData['orderNote'];
	var pickTicketNote2 = rowData['pickTicketNote2'];
	var pickTicketNote3 = rowData['pickTicketNote3'];
	var pickTicketNote4 = rowData['pickTicketNote4'];
	var pickTicketNote5 = rowData['pickTicketNote5'];
	$("#cuTermsId").val(cuTermsId);
	$("#description").val(description);
	$("#dueDay").val(dueDays);
	$("#discOnDay").val(discOnDay);
	if(dueOnDay === 'true')
		{
		$("#dueDayoption").children("option[value='1']").prop('selected',true);
		$("#dueDayoption").children("option[value='0']").prop('selected',false);
		}
	else
		{
		$("#dueDayoption").children("option[value='0']").prop('selected',true);
		$("#dueDayoption").children("option[value='1']").prop('selected',false);
		}
	
	if(discountDay === 'true')
	{
	$("#discountDayoption").children("option[value='1']").prop('selected',true);
	$("#discountDayoption").children("option[value='0']").prop('selected',false);
	}
else
	{
	$("#discountDayoption").children("option[value='0']").prop('selected',true);
	$("#discountDayoption").children("option[value='1']").prop('selected',false);
	}
		
	$('#discountDay').val(discountDays);
	if(isGlobal==='true')
		{
		 $('#global').attr('checked', true);
		}
	else
		{
		$('#global').attr('checked', false);
		}
	if(inActive==='true')
	{
	 $('#active').attr('checked', true);
	}
else
	{
	$('#active').attr('checked', false);
	}
	$("#discountPercent").val(discountPercent);
	$("#pickTicket1").val(pickTicketNote1);
	$("#orderNote").val(orderNote);
	$('#pickTicket2').val(pickTicketNote2);
	$('pickTicket3').val(pickTicketNote3);
	$('#pickTicket4').val(pickTicketNote4);
	$('#pickTicket5').val(pickTicketNote5);
	return true;
}

function openAddNewTermsDialog(){
	document.getElementById("addNewTermsFormID").reset();
	jQuery("#addNewTermsDialog").dialog("open");	
	return true;
}

jQuery( function(){
	jQuery("#addNewTermsDialog").dialog({
		autoOpen:false,
		height:487,
		width:757,
		title:"Add New Payment Terms",
		modal:true,
		close:function(){
			return true;
		}
	});
});

function cancelAddTerms(){
	jQuery("#addNewTermsDialog").dialog("close");
	return true;
}

function editData() {
	var additionalInfo = $("#paymentTermsFromID").serialize();
	$.ajax({
		url: "./editTerms",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Terms are saved Successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("paymentTermsFromID").reset(); location.reload(true); }}]}).dialog("open");
		}
	});
}

function addData() {
	var additionalInfo = $("#addNewTermsFormID").serialize();
	$.ajax({
		url: "./editTerms",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Terms are saved Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#addNewTermsDialog").dialog("close");
									document.getElementById("addNewTermsDialog").reset(); location.reload(true); }}]}).dialog("open");
		}
	});
}
function deletePaymentTerms(){
	var grid = $("#chartsOfTermsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Payment Term?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					deleteData();
					jQuery(this).dialog("close");
					
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Payment terms to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
}


function deleteData() {
	var cuTermsId =  $("#cuTermsId").val();
	$.ajax({
		url: "./deleteTerms",
		type: "POST",
		data :  { 'cuTermsId' : cuTermsId },
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Terms are deleted successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); 
									document.getElementById("paymentTermsFromID").reset(); location.reload(true); }}]}).dialog("open");
		}
	});
}
