jQuery(document).ready(function() {
	$( "input#searchJob" ).attr("placeholder","Minimum 2 characters required to get Accounts list");
	loadChartsAccount();
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

function loadChartsAccount(){
	var aChartsPage = $("#chartsAccID").val();
	$("#chartsOfAccountsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./company/chartsAccountsList',
		//pager: jQuery('#chartsOfAccountsGridPager'),
	   	colNames:['Acct. #', 'Account Description', 'CoAccountID', 'InActive', 'Bal. Sheet Column', 'IncludeWhenZero', 'DebitBalance', 'ContraAccount', 'LineAboveAmount', 'LineBelowAmount', 'TotalingLevel', 'VerticalSpacing', 
																																'HorizontalSpacing', 'FontLarge', 'FontBold', 'FontItalic', 'FontUnderline', 'Tax1099', 'SubAccount', 'IsSubAccount', 'IsMasterAccount', 'DollarSign'],
	   	colModel:[{name:'number',index:'number', width:40,editable:false, hidden:false, editrules:{required:true}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'coAccountId',index:'coAccountId',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'balanceSheetColumn',index:'balanceSheetColumn', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'includeWhenZero',index:'includeWhenZero', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'debitBalance',index:'debitBalance', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'contraAccount',index:'contraAccount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'lineAboveAmount',index:'lineAboveAmount', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'lineBelowAmount',index:'lineBelowAmount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'totalingLevel',index:'totalingLevel', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'verticalSpacing',index:'verticalSpacing', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'horizontalSpacing',index:'horizontalSpacing', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'fontLarge',index:'fontLarge', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'fontBold',index:'fontBold', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'fontItalic',index:'fontItalic', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'fontUnderline',index:'fontUnderline', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'tax1099',index:'tax1099', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'subAccount',index:'subAccount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'isSubAccount',index:'isSubAccount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'isMasterAccount',index:'isMasterAccount',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'dollarSign',index:'dollarSign', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
		rowNum: 500,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfAccountsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Chart of Accounts',
		height:580,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: true,
		loadComplete:function(data) {
			$(".ui-pg-selbox").attr("selected", aChartsPage);
			if(jQuery("#chartsOfAccountsGrid").getGridParam("records") !== 0){
				loadAdditionalData();
			}
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
    		loadAccountsDetais(rowId);
    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("chartsDetailsFromID").reset();
    		loadAccountsDetais(rowId);
		}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function loadAccountsDetais(rowId){
	var rowData = jQuery("#chartsOfAccountsGrid").getRowData(rowId); 
	var coAccountID = rowData['coAccountId'];
	var number = rowData['number'];
	var description = rowData['description'];
	var inActive = rowData['inActive'];
	var balanceSheetColumn = rowData['balanceSheetColumn'];
	var includeWhenZero = rowData['includeWhenZero'];
	var lineAboveAmount = rowData['lineAboveAmount'];
	var lineBelowAmount = rowData['lineBelowAmount'];
	var totalingLevel = rowData['totalingLevel'];
	var verticalSpacing = rowData['verticalSpacing'];
	var horizontalSpacing = rowData['horizontalSpacing'];
	var fontLarge = rowData['fontLarge'];
	var fontBold = rowData['fontBold'];
	var fontItalic = rowData['fontItalic'];
	var fontUnderline = rowData['fontUnderline'];
	var tax1099 = rowData['tax1099'];
	var dollarSign = rowData['dollarSign'];
	var accountType = ((rowData['debitBalance'] === "true") ? 1 : 0 );
	$("#numberNameID").val(number);
	$("#decriptionNameID").val(description);
	$("#coAccountNameID").val(coAccountID);
	if(inActive === 'true'){
		$("#inActiveNameID").attr("checked", true);
	}else{
		$("#inActiveNameID").attr("checked", false);
	}
	if(tax1099 === 'true'){
		$("#expenseIDBox").attr("checked", true);
	}else{
		$("#expenseIDBox").attr("checked", false);
	}
	if(fontLarge === 'true'){
		$("#largeNameID").attr("checked", true);
	}else{
		$("#largeNameID").attr("checked", false);
	}
	if(fontBold === 'true'){
		$("#boldNameID").attr("checked", true);
	}else{
		$("#boldNameID").attr("checked", false);
	}
	if(fontItalic === 'true'){
		$("#italicNameID").attr("checked", true);
	}else{
		$("#italicNameID").attr("checked", false);
	}
	if(dollarSign === 'true'){
		$("#dollourNameID").attr("checked", true);
	}else{
		$("#dollourNameID").attr("checked", false);
	}
	if(fontUnderline === 'true'){
		$("#underlineNameID").attr("checked", true);
	}else{
		$("#underlineNameID").attr("checked", false);
	}
	if(includeWhenZero === 'true'){
		$("#alwaysNameID").attr("checked", true);
	}else{
		$("#alwaysNameID").attr("checked", false);
	}
	$("#postingLevelID option[value=" + totalingLevel + "]").attr("selected", true);
	$("#verticalSpacing option[value=" + verticalSpacing + "]").attr("selected", true);
	$("#balanceSheetColumnID option[value=" + balanceSheetColumn + "]").attr("selected", true);
	$("#lineAboveAmountID option[value=" + lineAboveAmount + "]").attr("selected", true);
	$("#lineBelowAmount option[value=" + lineBelowAmount + "]").attr("selected", true);
	$("#tabIndentationID option[value=" + horizontalSpacing + "]").attr("selected", true);
	$('#accountTypeID').val(accountType);
	return true;
}

function saveChartsOfAccounts(){
	var grid = $("#chartsOfAccountsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select the Account from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var chartsDetails = $("#chartsDetailsFromID").serialize();
	$.ajax({
		url: "./company/updateChartAccount",
		type: "POST",
		data : chartsDetails,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart Account Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset(); $("#chartsOfAccountsGrid").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
	return false;
}

function openAddNewChartDialog(){
	document.getElementById("addNewChartFormID").reset();
	jQuery("#addNewChartDialog").dialog("open");	
	return true;
}

jQuery( function(){
	jQuery("#addNewChartDialog").dialog({
		autoOpen:false,
		height:410,
		width:850,
		title:"Add New Chart of Account",
		modal:true,
		close:function(){
			//$('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

function cancelAddChart(){
	jQuery("#addNewChartDialog").dialog("close");
	return true;
}

function saveNewChart(){
	var aChartDetais = $("#addNewChartFormID").serialize();
	$.ajax({
		url: "./company/addNewChartAccount",
		type: "POST",
		data : aChartDetais,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart Account Added Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("addNewChartFormID").reset();loadChartsAccount(); jQuery("#addNewChartDialog").dialog("close"); }}]}).dialog("open");
		}
	});
	return true;
}

/** Account Search List **/
$(function() { var cache = {}; var lastXhr='';
	$( "#searchJob" ).autocomplete({ minLength: 2,timeout :1000,
		select: function (event, ui) { var accountsID = ui.item.id; getAccoutsDetails(accountsID);	},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchAccountsList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) { $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); }
	}); 
});

function getAccoutsDetails(accountsID){
	$.ajax({
		url: "./company/getAccountsDetails",
		type: "GET",
		data : { 'accountID' : accountsID },
		success: function(data) {
			$.each(data, function(index, value){
				var coAccountID = value.coAccountId;
				var number = value.number;
				var description = value.description;
				var inActive = value.inActive;
				var balanceSheetColumn = value.balanceSheetColumn;
				var includeWhenZero = value.includeWhenZero;
				var lineAboveAmount = value.lineAboveAmount;
				var lineBelowAmount = value.lineBelowAmount;
				var totalingLevel = value.totalingLevel;
				var verticalSpacing = value.verticalSpacing;
				var horizontalSpacing = value.horizontalSpacing;
				var fontLarge = value.fontLarge;
				var fontBold = value.fontBold;
				var fontItalic = value.fontItalic;
				var fontUnderline = value.fontUnderline;
				var tax1099 = value.tax1099;
				var dollarSign = value.dollarSign;
				$("#numberNameID").val(number);
				$("#decriptionNameID").val(description);
				$("#coAccountNameID").val(coAccountID);
				if(inActive == true){
					$("#inActiveNameID").attr("checked", true);
				}else{
					$("#inActiveNameID").attr("checked", false);
				}
				if(tax1099 == true){
					$("#expenseIDBox").attr("checked", true);
				}else{
					$("#expenseIDBox").attr("checked", false);
				}
				if(fontLarge == true){
					$("#largeNameID").attr("checked", true);
				}else{
					$("#largeNameID").attr("checked", false);
				}
				if(fontBold == true){
					$("#boldNameID").attr("checked", true);
				}else{
					$("#boldNameID").attr("checked", false);
				}
				if(fontItalic == true){
					$("#italicNameID").attr("checked", true);
				}else{
					$("#italicNameID").attr("checked", false);
				}
				if(dollarSign == true){
					$("#dollourNameID").attr("checked", true);
				}else{
					$("#dollourNameID").attr("checked", false);
				}
				if(fontUnderline == true){
					$("#underlineNameID").attr("checked", true);
				}else{
					$("#underlineNameID").attr("checked", false);
				}
				if(includeWhenZero == true){
					$("#alwaysNameID").attr("checked", true);
				}else{
					$("#alwaysNameID").attr("checked", false);
				}
				$("#postingLevelID option[value=" + totalingLevel + "]").attr("selected", true);
				$("#verticalSpacing option[value=" + verticalSpacing + "]").attr("selected", true);
				$("#balanceSheetColumnID option[value=" + balanceSheetColumn + "]").attr("selected", true);
				$("#lineAboveAmountID option[value=" + lineAboveAmount + "]").attr("selected", true);
				$("#lineBelowAmount option[value=" + lineBelowAmount + "]").attr("selected", true);
				$("#tabIndentationID option[value=" + horizontalSpacing + "]").attr("selected", true);
				$("#searchJob").val("");
			});
		}
	});
	return true;
}

function deleteAccountDetails(){
	var grid = $("#chartsOfAccountsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Account Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var coAccountId = grid.jqGrid('getCell', rowId, 'coAccountId');
					deleteChartOfAccount(coAccountId); 
					jQuery(this).dialog("close");
					$("#chartsOfAccountsGrid").trigger("reloadGrid");
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

function deleteChartOfAccount(coAccountId){
	$.ajax({
		url: "./company/deleteAccount",
		type: "POST",
		data : {"coAccountID": coAccountId},
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart of Account Deleted.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset(); $("#chartsOfAccountsGrid").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
}

function loadAdditionalData() {
	$.ajax({
		url: "./company/getAdditional",
		type: "GET",
		success: function(data) {
			var aVal = new Array(); 
			var aTax = new Array();
			var allRowsInGrid = $('#chartsOfAccountsGrid').jqGrid('getRowData');
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.coAccountId;
				aTax[index] = value.number;
				if(aVal[index] == data.coAccountIdnetSales){
					$("#generalLedgerNetSales").val(aTax[index]);
					$("#generalLedgerNetSalesAccID").val(data.coAccountIdnetSales);
				}
				if(aVal[index] == data.coAccountIdpl){
					$("#generalLedgerProfitLoss").val(aTax[index]);
					$("#generalLedgerProfitLossAccID").val(data.coAccountIdpl);
				}
				if(aVal[index] == data.coAccountIdcurEarnings){
					$("#generalLedgerCurEarnings").val(aTax[index]);
					$("#generalLedgerCurEarningsAccID").val(data.coAccountIdcurEarnings);
				}
				if(aVal[index] == data.coAccountIdretainedEarnings){
					$("#generalLedgerRetainedEarnings").val(aTax[index]);
					$("#generalLedgerRetainedEarningsAccID").val(data.coAccountIdretainedEarnings);
				}
				if(aVal[index] == data.coAccountIdar){
					$("#customerAccountsReceivable").val(aTax[index]);
					$("#customerAccountsReceivableAccID").val(data.coAccountIdar);
				}
				if(aVal[index] == data.coAccountIddiscounts){
					$("#customerDisc_Adjst").val(aTax[index]);
					$("#customerDisc_AdjstAccID").val(data.coAccountIddiscounts);
				}
				if(aVal[index] == data.coAccountIdsalesTaxInv){
					$("#customerSalesTaxInvoiced").val(aTax[index]);
					$("#customerSalesTaxInvoicedAccID").val(data.coAccountIdsalesTaxInv);
				}
				if(aVal[index] == data.coAccountIdshipping){
					$("#customerShippingDropShip").val(aTax[index]);
					$("#customerShippingDropShipAccID").val(data.coAccountIdshipping);
				}
				if(aVal[index] == data.coAccountIdshipInventory){
					$("#customerShpngInvntry").val(aTax[index]);
					$("#customerShpngInvntryAccID").val(data.coAccountIdshipInventory);
				}
				if(aVal[index] == data.coAccountIdotherCharges){
					$("#customerOthrChrgs").val(aTax[index]);
					$("#customerOthrChrgsAccID").val(data.coAccountIdotherCharges);
				}
				if(aVal[index] == data.coAccountIdpayments){
					$("#customerPaymntsRcvd").val(aTax[index]);
					$("#customerPaymntsRcvdAccID").val(data.coAccountIdpayments);
				}
				if(aVal[index] == data.coAccountIdap){
					$("#vendorAccountsPayable").val(aTax[index]);
					$("#vendorAccountsPayableAccID").val(data.coAccountIdap);
				}
				if(aVal[index] == data.coAccountIdfreight){
					$("#vendorFreight").val(aTax[index]);
					$("#vendorFreightAccID").val(data.coAccountIdfreight);
				}
				if(aVal[index] == data.coAccountIdsalesTaxPaid){
					$("#vendorSalesTaxPaid").val(aTax[index]);
					$("#vendorSalesTaxPaidAccID").val(data.coAccountIdsalesTaxPaid);
				}
				if(aVal[index] == data.coAccountIddiscountsTaken){
					$("#vendorDiscountsTaken").val(aTax[index]);
					$("#vendorDiscountsTakenAccID").val(data.coAccountIddiscountsTaken);
				}
				if(aVal[index] == data.coAccountIdmisc){
					$("#miscAccount").val(aTax[index]);
					$("#miscAccountAccID").val(data.coAccountIdmisc);
				}
				if(aVal[index] == data.coAccountIdinventoryAdjustment){
					$("#miscInventoryAdjustment").val(aTax[index]);
					$("#miscInventoryAdjustmentAccID").val(data.coAccountIdinventoryAdjustment);
				}
			});
			$("#accountRangeAssetStart").val(data.coRangeAsset1);
			$("#accountRangeAssetEnd").val(data.coRangeAsset2);
			$("#accountRangeLiabilityStart").val(data.coRangeLiability1);
			$("#accountRangeLiabilityEnd").val(data.coRangeLiability2);
			$("#accountRangeEquityStart").val(data.coRangeEquity1);
			$("#accountRangeEquityEnd").val(data.coRangeEquity2);
			$("#accountRangeIncomeStart").val(data.coRangeIncome1);
			$("#accountRangeIncomeEnd").val(data.coRangeIncome2);
			$("#accountRangeExpenseStart").val(data.coRangeExpense1);
			$("#accountRangeExpenseEnd").val(data.coRangeExpense2);	
			$("#vendorExpense").val('');
		}
	});
	
}

function saveAdditionalData() {
	var additionalInfo = $("#accountsAdditionalInfoFormID").serialize();
	$.ajax({
		url: "./company/updateAdditional",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart Account Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset(); $("#chartsOfAccountsGrid").trigger("reloadGrid"); }}]}).dialog("open");
		}
	});
}

$(function() { var cache = {}; var lastXhr='';
	/** Vendor legend related ***/
	$( "#vendorAccountsPayable" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#vendorAccountsPayable" ).val(accNumber); $("#vendorAccountsPayableAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#vendorFreight" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#vendorFreight" ).val(accNumber); $("#vendorFreightAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	
	$( "#vendorSalesTaxPaid" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#vendorSalesTaxPaid" ).val(accNumber); $("#vendorSalesTaxPaidAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#vendorDiscountsTaken" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#vendorDiscountsTaken" ).val(accNumber); $("#vendorDiscountsTakenAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#vendorExpense" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#vendorExpense" ).val(accNumber); $("#vendorExpenseAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	/******** Misc *******/
	$( "#miscAccount" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#miscAccount" ).val(accNumber); $("#miscAccountAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#miscInventoryAdjustment" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#miscInventoryAdjustment" ).val(accNumber); $("#miscInventoryAdjustmentAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	/********* General Ledger *********/
	$( "#generalLedgerNetSales" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#generalLedgerNetSales" ).val(accNumber); $("#generalLedgerNetSalesAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#generalLedgerProfitLoss" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#generalLedgerProfitLoss" ).val(accNumber); $("#generalLedgerProfitLossAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#generalLedgerCurEarnings" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#generalLedgerCurEarnings" ).val(accNumber); $("#generalLedgerCurEarningsAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#generalLedgerRetainedEarnings" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#generalLedgerRetainedEarnings" ).val(accNumber); $("#generalLedgerRetainedEarningsAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	/*********** Customer ***********/
	$( "#customerAccountsReceivable" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerAccountsReceivable" ).val(accNumber); $("#customerAccountsReceivableAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerDisc_Adjst" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerDisc_Adjst" ).val(accNumber); $("#customerDisc_AdjstAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerSalesTaxInvoiced" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerSalesTaxInvoiced" ).val(accNumber); $("#customerSalesTaxInvoicedAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerShippingDropShip" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerShippingDropShip" ).val(accNumber); $("#customerShippingDropShipAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerShpngInvntry" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerShpngInvntry" ).val(accNumber); $("#customerShpngInvntryAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerOthrChrgs" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) { 
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerOthrChrgs" ).val(accNumber); $("#customerOthrChrgsAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	$( "#customerPaymntsRcvd" ).autocomplete({ minLength: 1,timeout :1000,
		select: function (event, ui) {
			var accountsID = ui.item.id; var accDetail = ui.item.label; var accNumber = accDetail.split("[")[1].replace("]",""); $( "#customerPaymntsRcvd" ).val(accNumber); $("#customerPaymntsRcvdAccID").val(accountsID);
		},
		open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./company/getAccountNumberAutoSuggest", request, function( data, status, xhr ) { cache[ term ] = data; 	if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
	});
	/*********************/
});
