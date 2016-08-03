var newDialogDiv = jQuery(document.createElement('div'));
var aBankingName = getUrlVars()["bankAccountName"];
var aFromDate = getUrlVars()["fromDateRangeName"];
var aToDate = getUrlVars()["toDateRangeName"];
var aDeposit = getUrlVars()["deposit"];
var aCheck = getUrlVars()["check"];
var aInterst = getUrlVars()["intrest"];
var aWithDraw = getUrlVars()["withDraw"];
var aFees = getUrlVars()["fee"];
var aTranDetails = new Array();
aTranDetails.push(aBankingName);
aTranDetails.push(aFromDate);
aTranDetails.push(aToDate);
aTranDetails.push(aDeposit);
aTranDetails.push(aCheck);
aTranDetails.push(aInterst);
aTranDetails.push(aWithDraw);
aTranDetails.push(aFees);
jQuery(document).ready(function() {
	loadBankingAccounts();
	loadTransactionRegisterDetails(aTranDetails);
	$(".banking_tabs_main").tabs({
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
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
						"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
			}
		}
	});
	
	 var aTokenID = getUrlVars()["token"];
	 if(aTokenID === 'new'){
		 $("#saveAndCloseID").show();
	 }else{
		 $("#saveAndCloseID").css("display", "none");
	 }
	 $("#saveEditButton").css("display", "none");
	 $("#saveDeleteButton").css("display", "none");
});

function loadBankingAccounts(){
	var aBankingPage = $("#bankingPerPage").val();
	$("#bankingAccountsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/bankingAccountsList',
//		pager: jQuery('#bankingAccountsGridPager'), 
	   	colNames:['MoAccountID', 'InActive', 'Account Type', 'Description', 'CoAccountIDAsset', 'CoAccountIDDeposits', 'CoAccountIDInterest', 'CoAccountIDFees', 'Open Balance', 'Additions', 'Subtractions', 'UnDeposited Receipts', 'UnPrinted Payables', 'UnPrinted Payroll', 'NextCheck Number', 'Endind Balance', 
																																'Company1', 'Company2', 'Company3', 'Company4', 'Company5', 'Banking1', 'Banking2', 'Banking3', 'Banking4', 'Banking5', 'Checking Code', 'Rounting Number', 'Account number' ],
	   	colModel:[
	   	    {name:'moAccountId',index:'moAccountId', width:40,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'accountType',index:'accountType',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, hidden:false, editoptions:{size:10}},
			{name:'coAccountIDAsset',index:'coAccountIDAsset', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountIDDeposits',index:'coAccountIDDeposits', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountIDInterest',index:'coAccountIDInterest', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountIDFees',index:'coAccountIDFees', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'openBalance',index:'openBalance', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'additions',index:'additions', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'subtractions',index:'subtractions', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'undepositedReceipts',index:'undepositedReceipts', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'unprintedPayables',index:'unprintedPayables', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'unprintedPayroll',index:'unprintedPayroll', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'nextCheckNumber',index:'nextCheckNumber', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'endingBalance',index:'endingBalance', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'company1',index:'company1', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'company2',index:'company2', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'company3',index:'company3', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'company4',index:'company4', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'company5',index:'company5', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'bank1',index:'bank1', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'bank2',index:'bank2', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'bank3',index:'bank3', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'bank4',index:'bank4',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'bank5',index:'bank5', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'checkCode',index:'checkCode',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'routingNumber',index:'routingNumber',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'accountNumber',index:'accountNumber',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
		rowNum: 0,	
		pgbuttons: true,	
		recordtext: '',
		rowList: [25, 50, 75, 100],
		viewrecords: true,
		//pager: '#bankingAccountsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Banking Accounts',
		width: 350, rownumbers:true,rownumWidth:34,
		loadonce: true,
		loadComplete:function(data) {
			$(this).setSelection(1, true);
			$("#bankingAccountsGrid").parents('div.ui-jqgrid-bdiv').css("max-height","580px");
			$(".ui-pg-selbox").attr("selected", aBankingPage);
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
    		var rowData = jQuery(this).getRowData(rowId);
    		var moAccountID = rowData['moAccountId'];
    		$("#saveAndCloseID").show();
    		loadBankingDetais(rowId);
    		getSingleMoAccount(moAccountID);
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId);
    		var moAccountID = rowData['moAccountId'];
    		$("#saveAndCloseID").show();
    		loadBankingDetais(rowId);
    		getSingleMoAccount(moAccountID);
		}
	});
	return true;
}

function loadBankingDetais(rowId){
	var rowData = jQuery("#bankingAccountsGrid").getRowData(rowId);
	/*var moAccountID = rowData['moAccountId'];*/
	var inActive = rowData['inActive'];
	var accountType = rowData['accountType'];
	var description = rowData['description'];
	var accountAsset = rowData['coAccountIDAsset'];
	var accountADeposits = rowData['coAccountIDDeposits'];
	var accountInterest = rowData['coAccountIDInterest'];
	var accountFees = rowData['coAccountIDFees'];
	var openBalance = rowData['openBalance'];
	var additions = rowData['additions'];
	var substraction = rowData['subtractions'];
//	var undepositedReceipts = rowData['undepositedReceipts'];
	var unprintedPayable = rowData['unprintedPayables'];
	var unprintedPayRoll = rowData['unprintedPayroll'];
	/*var nextCheckNumber = rowData['nextCheckNumber'];
	var endingBalance = rowData['endingBalance'];*/
	var company1 = rowData['company1'];
	var company2 = rowData['company2'];
	var company3 = rowData['company3'];
	var company4 = rowData['company4'];
	var company5 = rowData['company5'];
	var bank1 = rowData['bank1'];
	var bank2 = rowData['bank2'];
	var bank3 = rowData['bank3'];
	var bank4 = rowData['bank4'];
	var bank5 = rowData['bank5'];
	var checkCode = rowData['checkCode'];
	var routingNumber = rowData['routingNumber'];
	var accountingNumber = rowData['accountNumber'];
	$("#descriptionID").val(description);
	if(inActive === '1'){
		$("#inActiveID").attr("checked", true);
	}else{
		$("#inActiveID").attr("checked", false);
	}
	$("#openBalanceID").empty();
	$("#openBalanceID").append(formatCurrency(openBalance));
	$("#depositsID").empty();
	$("#depositsID").append(formatCurrency(additions));
	$("#withdrawOtherCreditID").empty();
	$("#withdrawOtherCreditID").append(formatCurrency(substraction));
	var aCurrentBal = Number(openBalance)+Number(additions);
	var aCurrentBalance = Number(aCurrentBal)-Number(substraction);
	$("#currentBalanceID").empty();
	$("#currentBalanceID").append(formatCurrency(aCurrentBalance));
	$("#payableApprovedID").empty();
	$("#payableApprovedID").append(formatCurrency(unprintedPayable));
	$("#payablePreparedID").empty();
	$("#payablePreparedID").append(formatCurrency(unprintedPayRoll));
	var aResultingBal = Number(unprintedPayable)+Number(unprintedPayRoll);
	var aResultingBalance = Number(aCurrentBalance)-Number(aResultingBal);
	$("#resultingBalanceID").empty();
	$("#resultingBalanceID").append(formatCurrency(aResultingBalance));
	$("#accountHolder1ID").val(company1);
	$("#accountHolder2ID").val(company2);
	$("#accountHolder3ID").val(company3);
	$("#accountHolder4ID").val(company4);
	$("#accountHolder5ID").val(company5);
	$("#bankName1ID").val(bank1);
	$("#bankName2ID").val(bank2);
	$("#bankName3ID").val(bank3);
	$("#bankName4ID").val(bank4);
	$("#bankName5ID").val(bank5);
	$("#ckCodeID").val(checkCode);
	$("#ABARoutingID").val(routingNumber);
	$("#accountNumberID").val(accountingNumber);
	$("#typeID option[value=" + accountType + "]").attr("selected", true);
	$("#assetAccountID option[value=" + accountAsset + "]").attr("selected", true);
	$("#depositDefultAccountID option[value=" + accountADeposits + "]").attr("selected", true);
	$("#interstDefultAccountID option[value=" + accountInterest + "]").attr("selected", true);
	$("#feesDefultAccountID option[value=" + accountFees + "]").attr("selected", true);
	return true;
}

function addNewBankingDetails(){
	document.location.href = "./bankingAccounts?token=new";
	return true;
}

function deleteBankingDetails(){
	var grid = $("#bankingAccountsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Banking Details?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var moAccountID = grid.jqGrid('getCell', rowId, 'moAccountId');
					deleteBanking(moAccountID); 
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Banking Record to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
 }

 function deleteBanking(moAccountID){
 	$.ajax({
 		url: "./banking/deleteBankingDetails",
 		type: "POST",
 		data : {"moAccountID": moAccountID},
 		success: function(data) {
 			jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Deleted Successfully.</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
 									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); document.location.href = "./bankingAccounts"; }}]}).dialog("open");
 		}
 	});
 }

 function saveBankingDetails(){
	 var aBankingDetailsForm = $("#bankingDetailsFromID").serialize();
	 var aTokenID = getUrlVars()["token"];
	 var grid = $("#bankingAccountsGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var moAccountID = grid.jqGrid('getCell', rowId, 'moAccountId');
		var nextCheckNumber = grid.jqGrid('getCell', rowId, 'nextCheckNumber');
		var aInactiveboxIDBox = $("#inActiveID").is(':checked');
	 var aBankingDetails = '';
	 var grid = $("#bankingAccountsGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		if(rowId == null){
			if(aTokenID === 'new'){
				moAccountID = '';
				aBankingDetails = aBankingDetailsForm+"&inActiveBox="+aInactiveboxIDBox+"&moAccountID="+moAccountID+"&token="+aTokenID+"&nextCheckNumber"+nextCheckNumber;
			}
	 	}else{
	 			aBankingDetails = aBankingDetailsForm+"&inActiveBox="+aInactiveboxIDBox+"&moAccountID="+moAccountID+"&token="+aTokenID+"&nextCheckNumber"+nextCheckNumber;
	 	}
	 	$.ajax({
	 		url: "./banking/saveBankingDetailsDetails",
	 		type: "POST",
	 		data : aBankingDetails,
	 		success: function(data) {
	 			if(aTokenID === 'new'){
	 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Updated Successfully.</b></span>');
	 			}else{
	 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Added Successfully.</b></span>');
	 			}
	 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
	 				buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); document.location.href = "./bankingAccounts"; }}]}).dialog("open");
	 		}
	 	});
 }
 
 																																			/**				Transaction Register Details		        **/
 
 function loadTransactionRegisterDetails(aDetails){
	var aBankingPage = $("#bankingPerPage").val();
	$("#transactionRegisterGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/transactionRegisterList',
		pager: jQuery('#transactionRegisterGridPager'),
	   	colNames:['', 'MoTransactionId', 'RxMasterId', 'RxAddressId', 'CoAccountId', 'MoAccountId', 'Date', 'Type', 'MoTypeID', 'CheckType', 'Description', 'Reference', 'Void', 'Reconciled', 'TempRec', 'Printed', 'Deposit', 'Withdrawal', 'Direct Deposit' ],
	   	colModel:[
	   	    {name:'void_',index:'void_',align:'right',width:10,editable: false,hidden: false, formatter: voidImage},
	   	    {name:'moTransactionId',index:'moTransactionId', width:40,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'rxMasterId',index:'rxMasterId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'rxAddressId',index:'rxAddressId',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountId',index:'coAccountId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'moAccountId',index:'moAccountId', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'transDate',index:'transDate', align:'center', width:60,editable:true,  hidden:false, editrules:{required:true}, editoptions:{size:10}},
			{name:'moTransactionTypeId',index:'moTransactionTypeId', align:'left', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}, formatter:statusFormatter},
			{name:'moTypeId',index:'moTypeId', align:'left', width:60,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'checkType',index:'checkType', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'description',index:'description', width:60,editable:true, align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'reference',index:'reference', width:60,editable:true,  hidden:false, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'void_',index:'void_', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'reconciled',index:'reconciled', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'tempRec',index:'tempRec', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'printed',index:'printed', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'amount',index:'amount', width:60,editable:true, editrules:{required:true}, align:'right', editoptions:{size:10}, formatter:customDepositFormatter, hidden:false},
			{name:'withDrawel',index:'withDrawel', width:60,editable:true, editrules:{required:true}, align:'right', formatter:amountFilterFormatter, editoptions:{size:10}, hidden:false},
			{name:'directDeposit',index:'directDeposit', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
		rowNum: aBankingPage,	
		postData: {'transactionDetails' : aDetails},
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#transactionRegisterGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: false,
		width: 950, rownumbers:true,rownumWidth:34, height : 360, 
		loadonce: false,
		loadComplete:function(data) {
			$(".ui-pg-selbox").attr("selected", aBankingPage);
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
    		
    	},
    	ondblClickRow: function(rowId) {
    		editTransaction();
		}
	});
	return true;
}
 
 function voidImage(cellValue, options, rowObject){
	var element = '';
   if(cellValue == 1){
	   element = "<img src='./../resources/images/delete.png' title='Cancelled Transaction'  style='padding: 2px;'>";
   }else if(cellValue == 0){
	   element = "";
   }else if(cellValue == null){
	   element = "";
   }else{
	   element = "";
   }
   return element;
} 
 
 function statusFormatter(cellvalue, options, rowObject) {
	if(cellvalue === 0) {
		return "Deposit";
	} else if(cellvalue === 1) {
		return "Withdrawal";
	} else if(cellvalue === 2){
		return "Check";
	}else if(cellvalue === 3){
		return "Fee";
	}else if(cellvalue === 4){
		return "Interest";
	}else{
		return "";
	}
}
 
 function amountFilterFormatter(cellValue, options, rowObject) {
	 if (cellValue >= 0) {
		 return '';
	 }else{
		 return formatCurrency(cellValue);
	 }
}
 
 function customDepositFormatter(cellValue, options, rowObject) {
	 if (cellValue >= 0) {
		 return formatCurrency(cellValue);
	 }else{
		 return '';
	 }
 }
	 
 function getSingleMoAccount(MoAccount){
	 $.ajax({
 		url: "./banking/singleAcccountDetails",
 		type: "POST",
 		data : {'moAccountID' : MoAccount},
 		success: function(data) {
 			var aBankingName = data.description;
 			$("#description_ID").empty();
 			$("#description_ID").append(aBankingName);
 			$("#newAccountNameID").empty();
 			$("#newAccountNameID").append(aBankingName);
 			if(data.accountType == 1) {
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Savings");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Savings");
 			} else if(data.accountType == 2) {
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Cash");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Cash");
 			} else if(data.accountType == 3){
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Credit Card");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Credit Card");
 			}else{
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Checking");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Checking");
 			}
 			var aCurrentBal = Number(data.openBalance)+Number(data.additions);
 			var aCurrentBalMain = Number(aCurrentBal)-Number(data.subtractions);
 			$("#currentBalance_ID").empty();
 			$("#currentBalance_ID").append(formatCurrency(aCurrentBalMain));
 			$("#resultingBalance_ID").empty();
 			$("#resultingBalance_ID").append(formatCurrency(aCurrentBalMain));
 		}
	});
 }

 jQuery( "#newTransactionDetails" ).dialog({
		autoOpen: false,
		width: 500,
		title:"Add/Edit Transaction",
		modal: true,
		buttons:{
			
		},
		close: function () {
			$('#newTransactionFormID').validationEngine('hideAll');
			return true;
		}
	});
 
 function newTransaction(){
	 $("#saveAddButton").show();
	 $("#saveEditButton").css("display", "none");
	 $("#saveDeleteButton").css("display", "none");
	 document.getElementById("newTransactionFormID").reset();
	 $("#newTransactionDetails").dialog("open");
	 $('#depositID').attr("checked",true);
	 return false;
 }
 
 function saveTransactionDetails(){
	 var aOper = "add";
	 loadTransactionDetails(aOper);
	 return false;
 }
 
 function editTransactionDetails(){
	 var aOper = "edit";
	 loadTransactionDetails(aOper);
	 return false;
 }
 
 function cancelTransactionDetails(){
	 $("#transactionDetailsDialog").dialog("close");
	 return false;
 }
 
 function editTransaction(){
	var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		$("#saveEditButton").show();
		$("#saveAddButton").css("display", "none");
		 $("#saveDeleteButton").css("display", "none");
		 var aTransDate = grid.jqGrid('getCell', rowId, 'transDate');
		 var aType = grid.jqGrid('getCell', rowId, 'moTypeId');
		 var aDescription = grid.jqGrid('getCell', rowId, 'description');
		 var aReference = grid.jqGrid('getCell', rowId, 'reference');
		 var aDepoist = grid.jqGrid('getCell', rowId, 'amount');
		 var aWithDrawal = grid.jqGrid('getCell', rowId, 'withDrawel');
		 var aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
		 var aReconciled = grid.jqGrid('getCell', rowId, 'reconciled');
		 $("#dateID").val(aTransDate);
		
		 if(aType === '0'){
			 $('#depositID').attr("checked",true);
		 }else if(aType === '1'){
			 $('#withdrawalID').attr("checked",true);
		 }else if(aType === '3'){
			 $('#feeID').attr("checked",true);
		 }else if(aType === '4'){
			 $('#interestID').attr("checked",true);
		 }
		 $("#descriptionID").val(aDescription);
		 $("#referenceID").val(aReference);
		 if(aReconciled === '0'){
			 $("#reconciledID").attr("checked", false);
		 }else{
			 $("#reconciledID").attr("checked", true);
		 }
		 $("#glAccountID option[value=" + aGLAccount + "]").attr("selected", "selected");
		 if(aDepoist !== ''){
			 $("#amountID").val(aDepoist.replace(/[^0-9\.]+/g,"").replace(".00", ""));
		 }else if(aWithDrawal !== ''){
			 $("#amountID").val(aWithDrawal.replace(/[^0-9\.]+/g,"").replace(".00", ""));
		 }
		$("#newTransactionDetails").dialog("open");
	}else{
		jQuery(newDialogDiv).html('<span><b style="color:red;">Please Select the Record.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
	}
 }
 
 function voidTransaction(){
	 var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Void this Transaction Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					var oper = "void";
					loadTransactionDetails(oper);
					jQuery(this).dialog("close");
					$("#transactionRegisterGrid").trigger("reloadGrid");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Transaction to Void.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
 }
 
 function deleteTransaction(){
	 var grid = $("#transactionRegisterGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		if(rowId !== null){
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete this Transaction Record?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
				buttons:{
					"Submit": function(){
						var oper = "delete";
						loadTransactionDetails(oper); 
						jQuery(this).dialog("close");
						$("#transactionRegisterGrid").trigger("reloadGrid");
					},
					Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
			return true;
		}else{
			var errorText = "Please click one of the Transaction to Delete.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
 }
 
 function loadTransactionDetails(oper){
	 var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var gridBank = $("#bankingAccountsGrid");
	var rowIdBank = gridBank.jqGrid('getGridParam', 'selrow');
	var aDate = '';
	 var aDeposit = '';
	 var aWithDrawal = '';
	 var aFee = '';
	 var aInterest = '';
	 var aGLAccount = '';
	 var aReconciled = '';
	 var aAmount = '';
	 var aDescription = '';
	 var aReference = '';
	 var aAmount1 = '';
	 var aTypeID = '';
	 var aRecouncil = '';
	 var aTransID = '';
	 var aMoAccID = '';
	 var aVoid = '';
	if(oper !== 'void'){
		aDate = $("#dateID").val();
		aDeposit = $("#depositID").is(':checked');
		aWithDrawal = $("#withdrawalID").is(':checked');
		aFee = $("#feeID").is(':checked');
		aInterest = $("#interestID").is(':checked');
		aGLAccount = $("#glAccountID").val();
		aReconciled = $("#reconciledID").is(':checked');
		aAmount = $("#amountID").val();
		aDescription = $("#descriptionID").val();
		aReference = $("#referenceID").val();
		if(aAmount === ''){
			aAmount1 = 0;
		}else{
			aAmount1 = $("#amountID").val();
		}
		if(aDeposit == true){
			aTypeID = 0;
		}else if(aWithDrawal == true){
			aTypeID = 1;
		}else if(aFee == true){
			aTypeID = 3;
		}else if(aInterest == true){
			aTypeID = 4;
		}
	}else{
		aVoid = 1;
		aDate = grid.jqGrid('getCell', rowId, 'transDate');
		aTypeID = grid.jqGrid('getCell', rowId, 'moTypeId');
		aDescription = grid.jqGrid('getCell', rowId, 'description');
		aReference = grid.jqGrid('getCell', rowId, 'reference');
		var aDepositAmt = grid.jqGrid('getCell', rowId, 'amount');
		var aWithDrawalAmt = grid.jqGrid('getCell', rowId, 'withDrawel');
		aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
		aRecouncil = grid.jqGrid('getCell', rowId, 'reconciled');
		if(aDepositAmt !== ''){
			aAmount1 = aDepositAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
		}else if(aWithDrawalAmt !== ''){
			aAmount1 = aWithDrawalAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
		}
	}
	 if(aReconciled == true){
		 aRecouncil = 1;
	 }else{
		 aRecouncil = 0;
	 }
	 if(oper !== 'add'){
		if(rowId !== null){
			aTransID = grid.jqGrid('getCell', rowId, 'moTransactionId');
			aMoAccID = grid.jqGrid('getCell', rowId, 'moAccountId');
		}
	 }else{
		 aMoAccID = gridBank.jqGrid('getCell', rowIdBank, 'moAccountId');
	 }
	 var aTransactionArray = new Array();
	 aTransactionArray.push(aDate);
	 aTransactionArray.push(aTypeID);
	 aTransactionArray.push(aReference);
	 aTransactionArray.push(aDescription);
	 aTransactionArray.push(aGLAccount);
	 aTransactionArray.push(aRecouncil);
	 aTransactionArray.push(aAmount1);
	 aTransactionArray.push(oper);
	 aTransactionArray.push(aTransID);
	 aTransactionArray.push(aMoAccID);
	 aTransactionArray.push(aVoid);
	 $.ajax({
 		url: "./banking/saveTransactionDetails",
 		type: "POST",
 		data : {'transactionDetails' :aTransactionArray},
 		success: function(data) {
 			if(oper === 'add'){
 				jQuery(newDialogDiv).html('<span><b style="color:green;">Transaction details Added successfully.</b></span>');
 				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
 					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#newTransactionDetails").dialog("close");
 					$("#transactionRegisterGrid").jqGrid('GridUnload');
 					loadTransactionRegisterDetails(aTranDetails);
					$("#transactionRegisterGrid").trigger("reloadGrid"); }}]}).dialog("open");
 				return false;
 			}else if(oper === 'edit'){
 				jQuery(newDialogDiv).html('<span><b style="color:green;">Transaction details Updated successfully.</b></span>');
 				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
 					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#newTransactionDetails").dialog("close");
 					$("#transactionRegisterGrid").jqGrid('GridUnload');
 					loadTransactionRegisterDetails(aTranDetails);
 					$("#transactionRegisterGrid").trigger("reloadGrid"); }}]}).dialog("open");
 				return false;
 			}else if(oper === 'delete'){
 				jQuery(newDialogDiv).html('<span><b style="color:green;">Transaction details Deleted successfully.</b></span>');
 				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
 					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#newTransactionDetails").dialog("close");
 					$("#transactionRegisterGrid").jqGrid('GridUnload');
 					loadTransactionRegisterDetails(aTranDetails);
 					$("#transactionRegisterGrid").trigger("reloadGrid"); }}]}).dialog("open");
 				return false;
 			}else if(oper === 'void'){
 				jQuery(newDialogDiv).html('<span><b style="color:green;">Transaction details Voided successfully.</b></span>');
 				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
 					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");
 					$("#transactionRegisterGrid").jqGrid('GridUnload');
 					loadTransactionRegisterDetails(aTranDetails);
 					$("#transactionRegisterGrid").trigger("reloadGrid"); }}]}).dialog("open");
 				return false;
 			}
 		}
	});
 }
 
 function newCheck(){
	 var aInfo = true;
	 if(aInfo){
	 	var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
	 	jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	 	jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
	 							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	 	return false;
	 }
 }
 
 function transactionDetails(){
	 jQuery( "#transactionDetailsDialog" ).dialog("open");
	return true;
}
 
 function closeTransactionDetails(){
	 jQuery( "#newTransactionDetails" ).dialog("close");
	return true;
}

jQuery(function () {
	jQuery( "#transactionDetailsDialog" ).dialog({
		autoOpen: false,
		width: 400,
		title:"View Transaction Details",
		modal: true,
		buttons:{		},
		close: function () {
			return true;
		}
	});
	return true;
});

function viewTransactionDetails(){
	var aDeposit = $("#transactionDepositID").is(':checked');
	var aWithDraw = $("#transactionWidthdrawID").is(':checked');
	var aCheck = $("#transactionCheckID").is(':checked');
	var aIntrest = $("#transactionInterstID").is(':checked');
	var aFee = $("#transactionFeeID").is(':checked');
	var aBankAccount = $("#bankAccountID").val();
	var aFromDate = $("#fromDateRangeID").val();
	var aToDate = $("#toDateRangeID").val();
	var aDepositChk = '';
	var aWithDrawChk = '';
	var aCheckChk = '';
	var aIntrestChk = '';
	var aFeeChk = '';
	if(aDeposit === true){
		aDepositChk = 0;
	}if(aWithDraw === true){
		aWithDrawChk = 1;
	}if(aCheck === true){
		aCheckChk = 2;
	}if(aIntrest === true){
		aIntrestChk = 4;
	}if(aFee == true){
		aFeeChk = 3;
	}
	var aArrayList = new Array();
	aArrayList.push(aBankAccount);
	aArrayList.push(aFromDate);
	aArrayList.push(aToDate);
	aArrayList.push(aDepositChk);
	aArrayList.push(aCheckChk);
	aArrayList.push(aIntrestChk);
	aArrayList.push(aWithDrawChk);
	aArrayList.push(aFeeChk);
	$("#transactionRegisterGrid").jqGrid('GridUnload');
	loadTransactionRegisterDetails(aArrayList);
	$('#transactionRegisterGrid').jqGrid('setGridParam',{postData: {'transactionDetails' : aArrayList}});
	$("#transactionRegisterGrid").trigger("reloadGrid");
	jQuery( "#transactionDetailsDialog" ).dialog("close");
	return false;
}
