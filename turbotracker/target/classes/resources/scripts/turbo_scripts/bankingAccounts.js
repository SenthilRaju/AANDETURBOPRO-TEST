var newDialogDiv = jQuery(document.createElement('div'));
var aBankingName = getUrlVars()["bankAccountName"];
var aFromDate = getUrlVars()["fromDateRangeName"];
var aToDate = getUrlVars()["toDateRangeName"];
var aDeposit = getUrlVars()["deposit"];
var aCheck = getUrlVars()["check"];
var aInterst = getUrlVars()["intrest"];
var aWithDraw = getUrlVars()["withDraw"];
var aFees = getUrlVars()["fee"];
var newRowIndex;
var reasonoperation="";
var reasondialogtitle="";
var glacctglobalrowid;
var mulaccbtn_status=0;
var aTranDetails = new Array();
var oldformSerialize='';
var oldgridaccountgriddata ='';
var oldcheckformSerialize='';

var aCheckno ='';
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
		 }
		 
		 $("#saveEditButton").css("display", "none");
		 $("#saveDeleteButton").css("display", "none");
	 
	 
	 	$('.number').keypress(function (event) {
		    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
		        event.preventDefault();
		    }
	
		    var text = $(this).val();
		    
		    if((text.indexOf('.') == -1) && text.length > 7 && event.which != 46 )
		    	{
		    	 event.preventDefault();
		    	}
	
		    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
		        event.preventDefault();
		    }
		});
	 
	 
		 /*Start Glaccount grid is loading
		  *Created By:Velmurugan
		  *Created On:5-9-2014
		  *
		  * */
		 loadGlAccountRegisterDetails();
		 /*While page loading glaccountgrid should not display in the popup thats y set none */
		 $("#gbox_glAccountsGrid").css({ display: "none" });
		 $("#glAccountsGridPager").css({ display: "none" });
		 $("#multipleAccountID").css({ display: "inline-block" });
		 $("#multipleAccountIDgrey").css({ display: "none" });
		 /*While clicked the save icon jqgrid validation popup has not coming in front of the popup thts y set the z-index value as 10000
		  * And set the grid total initially $0.00*/
		 $("#glAccountsGrid_ilsave").click(function() {
				$("#info_dialog").css("z-index", "10000");
				var description=$("#"+glacctglobalrowid+"_description").val();
				var amount=$("#"+glacctglobalrowid+"_amount").val();
				if(description==undefined &&amount==undefined){
					$("#glAccountsGrid").jqGrid('resetSelection');
					var rowcount=$("#glAccountsGrid").getGridParam("reccount");
					rowcount=rowcount;
					var ids = $("#glAccountsGrid").jqGrid('getDataIDs');
					var glaccrrowid;
					
					if(ids.length==1){
						glaccrrowid = 0;
					}else{
						 ids.sort();
						 glaccrrowid= ids[ids.length-2];
					}
					
					if(glacctglobalrowid=="new_row")
						{
						$("#" + glacctglobalrowid).attr("id", Number(glaccrrowid)+1);
						}
					setgridtotal();
					}
			});
	 
			 $("#glAccountsGrid_iledit").click(function() {
				 $("#alertmod").css("z-index", "10000");
				 $("#alertmod").css("top","700px");
			 });
			 
});

function loadBankingAccounts(){
	var aBankingPage = $("#bankingPerPage").val();
	$("#bankingAccountsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/bankingAccountsList',
//		pager: jQuery('#bankingAccountsGridPager'), 
	   	colNames:['MoAccountID', 'InActive', 'Account Type', 'Description', 'CoAccountIDAsset', 'CoAccountIDDeposits', 'CoAccountIDInterest', 'CoAccountIDFees', 'Open Balance', 'Additions', 'Subtractions', 'UnDeposited Receipts', 'UnPrinted Payables', 'UnPrinted Payroll', 'NextCheck Number', 'Endind Balance', 
																																'Company1', 'Company2', 'Company3', 'Company4', 'Company5', 'Banking1', 'Banking2', 'Banking3', 'Banking4', 'Banking5', 'Checking Code', 'Rounting Number', 'Account number','logoyn','lineno' ],
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
			{name:'accountNumber',index:'accountNumber',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'logoYN',index:'logoYN',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'lineNo',index:'lineNo',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
			
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
		width: 320, rownumbers:true,rownumWidth:34,height:610, 
		loadonce: false,
		loadComplete:function(data) {
			
			$(this).setSelection($("#hiddenbagridrowid").val(), true);
			$("#bankingAccountsGrid").parents('div.ui-jqgrid-bdiv').css("max-height","610px");
			$(".ui-pg-selbox").attr("selected", aBankingPage);
			
		},gridComplete :function(data) {
			$(this).setSelection($("hiddenbagridrowid").val(), true);
			var grid = $("#bankingAccountsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var moAccountID = grid.jqGrid('getCell', rowId, 'moAccountId');
			$("#hiddenmoaccountid").val(moAccountID);
		//	loadTransactionRegisterDetails(aTranDetails);
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
    		
    		$("#hiddenbagridrowid").val(rowId);
    		var rowData = jQuery(this).getRowData(rowId);
    		var moAccountID = rowData['moAccountId'];
    		$("#saveAndCloseID").show();
    	//	alert("select hi");
    		loadBankingDetais(rowId);
    		getSingleMoAccount(moAccountID);
    		var grid = $("#bankingAccountsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var moAccountID = grid.jqGrid('getCell', rowId, 'moAccountId');
			$("#hiddenmoaccountid").val(moAccountID);
    		$("#transactionRegisterGrid").trigger("reloadGrid");
    	},
    	ondblClickRow: function(rowId) {
    		$("#hiddenbagridrowid").val(rowId);
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
	var moAccountID = rowData['moAccountId'];
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
	var logoYN=rowData['logoYN'];
	var lineNo=rowData['lineNo'];
	if(logoYN=='true'){
		$("#logoAppearYN").attr("checked", true);
	}else{
		$("#logoAppearYN").attr("checked", false);
	}
	if(lineNo==1){
		$("#singleLineYN").prop("checked", true);
		$("#doubleLineYN").prop("checked", false);
	}else if(lineNo==2){
		$("#singleLineYN").prop("checked", false);
		$("#doubleLineYN").prop("checked", true);
	}else{
		$("#singleLineYN").prop("checked", false);
		$("#doubleLineYN").prop("checked", false);
	}
	
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
	/*$("#currentBalanceID").empty();
	$("#currentBalanceID").append(formatCurrency(aCurrentBalance));*/
	$("#payableApprovedID").empty();
	$("#payableApprovedID").append(formatCurrency(unprintedPayable));
	$("#payablePreparedID").empty();
	$("#payablePreparedID").append(formatCurrency(unprintedPayRoll));
	console.log('payable Approved: '+unprintedPayable);
	
	var aResultingBal = Number(unprintedPayable)+Number(unprintedPayRoll);
	var aResultingBalance = Number(aCurrentBalance)-Number(aResultingBal);

	/*Created By : Velmurugan
	 *Created On :28-08-2014
	 *Description:Resulting Balance=currentbalance+(futuredeposit-futurewithdrawel) 
	 * */
	

	$.ajax({
 		url: "./banking/getResultingBalance",
 		type: "POST",
 		data : {"moAccountID": moAccountID},
 		success: function(data) {
 			//currentBalance_ID,resultingBalance_ID
 			//Transaction details balance
 		
 			
 			$("#currentBalance_ID").empty();
 			$("#currentBalance_ID").append(formatCurrency(data.currentbalance));
 			$("#resultingBalance_ID").empty();
 			//$("#resultingBalance_ID").append(formatCurrency(data.resultingbalance));
 			
 			//Banking details balance
 			$("#currentBalanceID").empty();
 			$("#currentBalanceID").append(formatCurrency(data.currentbalance));
 			console.log('currentBalanceID: '+data.currentbalance+' Payable app:'+data.withDrawel);
 			$("#payableApprovedID").empty();
 			$("#payableApprovedID").append(formatCurrency(data.withDrawel));
 				var payableApprovedID;
 				payableApprovedID =$('#payableApprovedID').val();
 				if($('#payableApprovedID').val()!="" && typeof($('#payableApprovedID').val())!="undefined")
 		 		payableApprovedID.replace(/[^0-9\.]+/g, "");
 			console.log('currentBalanceID: '+data.currentbalance+' payableApprovedID -1:'+payableApprovedID);
 			var resultBalance = (data.currentbalance)-(data.withDrawel);
 			$("#resultingBalanceID").empty();
 			$("#resultingBalanceID").append(formatCurrency(resultBalance));
 			$("#resultingBalance_ID").append(formatCurrency(resultBalance));
 			
 		}
 	});
	
	
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
	$("#typeID option:selected").removeAttr("selected");
	$("#typeID option[value=" + accountType + "]").attr("selected", true);
	$("#assetAccountID  option:selected").removeAttr("selected");
	$("#assetAccountID option[value=" + accountAsset + "]").attr("selected", true);
	$("#depositDefultAccountID  option:selected").removeAttr("selected");
	$("#depositDefultAccountID option[value=" + accountADeposits + "]").attr("selected", true);
	$("#interstDefultAccountID option:selected").removeAttr("selected");
	$("#interstDefultAccountID option[value=" + accountInterest + "]").attr("selected", true);
	$("#feesDefultAccountID  option:selected").removeAttr("selected");
	$("#feesDefultAccountID option[value=" + accountFees + "]").attr("selected", true);
	
	editBankGlobalForm = $("#bankingDetailsFromID").serialize();
	return true;
}

function addNewBankingDetails(){
	var editLocalForm = $("#bankingDetailsFromID").serialize();
	if(editBankGlobalForm != editLocalForm)
	{
	console.log(editBankGlobalForm+" AVAVA "+editLocalForm);
	var newDialogDiv = jQuery(document.createElement('div'));
	var errorText ="Bank Details modified, please save before Add new one!";
	jQuery(newDialogDiv).attr("id","msgDlg");
	jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		buttons: [{height:35,text: "OK",click: function() {
				$(this).dialog("close");
				$(this).dialog('destroy').remove();
				
			}
		}]}).dialog("open");
	return false;
	}
	var checkpermission=getGrantpermissionprivilage('Banking',0);
	if(checkpermission){
	//document.location.href = "./bankingAccounts?token=new";
		
		$("#descriptionID").val("");
		$("#typeID option:selected").removeAttr("selected");
		$("#inActiveID").removeAttr("checked");
		$("#assetAccountID  option:selected").removeAttr("selected");
		$("#depositDefultAccountID  option:selected").removeAttr("selected");
		$("#interstDefultAccountID option:selected").removeAttr("selected");
		$("#feesDefultAccountID  option:selected").removeAttr("selected");
		$("#openBalanceID").html("$0.00");
		$("#depositsID").html("$0.00");
		$("#withdrawOtherCreditID").html("$0.00");
		$("#currentBalanceID").html("$0.00");
		$("#payableApprovedID").html("$0.00");
		$("#resultingBalanceID").html("$0.00");
		$("#accountHolder1ID").val("");
		$("#accountHolder2ID").val("");
		$("#accountHolder3ID").val("");
		$("#accountHolder4ID").val("");
		$("#accountHolder5ID").val("");
		$("#bankName1ID").val("");
		$("#bankName2ID").val("");
		$("#bankName3ID").val("");
		$("#bankName4ID").val("");
		$("#bankName5ID").val("");
		$("#ckCodeID").val("");
		$("#ABARoutingID").val("");
		$("#accountNumberID").val("");
		$("#logoAppearYN").val("");
		$("#singleLineYN").val("");
		$("#doubleLineYN").val("");

		$("#bankingAccountsGrid").jqGrid("resetSelection");
		document.getElementById("bankingDetailsFromID").reset()
		window.history.pushState("test", "Title", "./bankingAccounts?token=new");
		
		
		}
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
					createtpusage('Banking','Deleting Banking Details','Info','Banking,Deleting Banking Details,moAccountID:'+moAccountID);
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
 			if(data == true)
 			{
 				
 			jQuery(newDialogDiv).html('<span><b style="color:red;">Transaction Exists for this account. Delete is not allowed.</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
					buttons: [{text: "OK",click: function() {  $(this).dialog("close"); 
					var checkpermission=getGrantpermissionprivilage('Banking',0);
					if(checkpermission){document.location.href = "./bankingAccounts";}
					}}]}).dialog("open");
 			}
 			else
 			{
 			jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Deleted Successfully.</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
 									buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close"); 
 									var checkpermission=getGrantpermissionprivilage('Banking',0);
 									if(checkpermission){document.location.href = "./bankingAccounts";}
 									}}]}).dialog("open");
 			}
 		}
 	});
 }

 function saveBankingDetails(){
	 
	 	if($("#assetAccountID").val()==-1 || $("#depositDefultAccountID").val()==-1 || $("#interstDefultAccountID").val()==-1 || $("#feesDefultAccountID").val()==-1){
		 jQuery(newDialogDiv).html('<span><b style="color:red;">*Mandatory fields required</b></span>');
		 jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
				buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");
					 }}]}).dialog("open");
		 return false;
		}
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
		var LogoYN='';
		var LineNo='';
		
		if($("#logoAppearYN").is(":checked"))
		{
			LogoYN=1;
		}
		if($("#singleLineYN").is(":checked"))
		{
			LineNo=1;
		}
		if($("#doubleLineYN").is(":checked"))
		{
			LineNo=2
		}
		
		if(rowId == null){
			aTokenID='new';
			if(aTokenID === 'new'){
				moAccountID = '';
				aBankingDetails = aBankingDetailsForm+"&inActiveBox="+aInactiveboxIDBox+"&moAccountID="+moAccountID+"&token="+aTokenID+"&nextCheckNumber"+nextCheckNumber+"&LogoYN="+LogoYN+"&LineNo="+LineNo;
			}
	 	}else{
	 			aBankingDetails = aBankingDetailsForm+"&inActiveBox="+aInactiveboxIDBox+"&moAccountID="+moAccountID+"&token="+aTokenID+"&nextCheckNumber"+nextCheckNumber+"&LogoYN="+LogoYN+"&LineNo="+LineNo;
	 	}
		
	 	$.ajax({
	 		url: "./banking/saveBankingDetailsDetails",
	 		type: "POST",
	 		data : aBankingDetails,
	 		success: function(data) {
	 			if(aTokenID === 'new'){
	 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Added Successfully.</b></span>');
	 				createtpusage('Banking','Saving Banking Details','Info','Banking,Saving Banking Details,moAccountID:'+moAccountID+',nextCheckNumber:'+nextCheckNumber);
	 			}else{
	 				jQuery(newDialogDiv).html('<span><b style="color:Green;">Banking Details Updated Successfully.</b></span>');
	 				createtpusage('Banking','Updating Banking Details','Info','Banking,Updating Banking Details,moAccountID:'+moAccountID+',nextCheckNumber:'+nextCheckNumber);
	 			}
	 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
	 				buttons: [{height:30,text: "OK",click: function() {  $(this).dialog("close");var checkpermission=getGrantpermissionprivilage('Banking',0);
						if(checkpermission){ document.location.href = "./bankingAccounts";} }}]}).dialog("open");
	 		}
	 	});
 }
 
 																																			/**				Transaction Register Details		        **/
 
 function loadTransactionRegisterDetails(aDetails){
	 
	 
	var aBankingPage = $("#bankingPerPage").val();
	//Based upon accountid the grid will populate
	$("#transactionRegisterGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/transactionRegisterList',
		pager: jQuery('#transactionRegisterGridPager'),
	   	colNames:['', 'MoTransactionId', 'RxMasterId', 'RxAddressId', 'CoAccountId', 'MoAccountId', 'Date', 'Type', 'MoTypeID', 'CheckType', 'Description','', 'Reference', 'Void', 'Reconciled', 'TempRec', 'Printed', 'Deposit', 'Withdrawal', 'Direct Deposit','Balance','oper' ,'FutureorCurrent','Memo'],
	   	colModel:[
	   	    {name:'void_',index:'void_',align:'right',width:20,editable: false,hidden: false, formatter: voidImage},
	   	    {name:'moTransactionId',index:'moTransactionId', width:40,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'rxMasterId',index:'rxMasterId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'rxAddressId',index:'rxAddressId',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountId',index:'coAccountId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'moAccountId',index:'moAccountId', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'transDate',index:'transDate', align:'center', width:82,editable:true,  hidden:false, editrules:{required:true}, editoptions:{size:10}},
			{name:'moTransactionTypeId',index:'moTransactionTypeId', align:'left', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}, formatter:statusFormatter},
			{name:'moTypeId',index:'moTypeId', align:'left', width:60,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'checkType',index:'checkType', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'displaydiscription',index:'displaydiscription', width:235,editable:true, align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description', width:60,editable:true, hidden:true,align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'reference',index:'reference', width:87,editable:true,  hidden:false, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'void_',index:'void_', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'reconciled',index:'reconciled', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'tempRec',index:'tempRec', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'printed',index:'printed', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'amount',index:'amount', width:132,editable:true, editrules:{required:true}, align:'right', editoptions:{size:10}, formatter:customDepositFormatter, hidden:false},
			{name:'withDrawel',index:'withDrawel', width:132,editable:true, editrules:{required:true}, align:'right', formatter:amountFilterFormatter, editoptions:{size:10}, hidden:false},
			{name:'directDeposit',index:'directDeposit', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'balance',index:'balance', width:140,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10},formatter:formatCurrencyformat, hidden:false},
			{name:'status',index:'status', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true},
			{name:'futureorcurrent',index:'futureorcurrent', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true},
			{name:'memo',index:'memo', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true}],
		rowNum: aBankingPage,	
		postData: {'transactionDetails' : aDetails,'moAccountID':function(){var moAccountID=$("#hiddenmoaccountid").val();return moAccountID;}},
		pgbuttons: true,	
		recordtext: '',
		rowList: [10, 50, 200, 500, 1000],
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
    		getSingleMoAccount($("#hiddenmoaccountid").val());
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
    		
			$("#glAccountsGrid").trigger("reloadGrid");
			var grid = $("#transactionRegisterGrid");
    		var moTransactionTypeId=grid.jqGrid('getCell', rowId, 'moTransactionTypeId');
    		var moAccountID=grid.jqGrid('getCell', rowId, 'moAccountId');
    	},
    	ondblClickRow: function(rowId) {
    		var grid = $("#transactionRegisterGrid");
    		var moTransactionTypeId=grid.jqGrid('getCell', rowId, 'moTransactionTypeId');
    		var moAccountID=grid.jqGrid('getCell', rowId, 'moAccountId');
    			if(moTransactionTypeId=="Check")
    			{
    			reOpenCheck();
    			}
    			else
    			{
    			editTransaction();
    			}
		}
	});
	return true;
}
 
 function voidImage(cellValue, options, rowObject){
	var element = '';
	var status=rowObject['status'];
	var reconciled=rowObject['reconciled'];
	
	if(status==1){
		element = "<img src='./../resources/images/delete.png' title='Cancelled Transaction'  style='padding: 2px;'>";	
	}
	else if(reconciled==1){
		element = "<img src='./../resources/images/greenTick.png' title='Cancelled Transaction'  style='padding: 2px;'>";	
	}
	
	else{
		element="";
	}
	if(reconciled==1&&status==1)
	{
	element = "<img src='./../resources/images/greenTick.png' title='Cancelled Transaction'  style='padding: 2px;'>";	
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
 function formatCurrencyformat(cellValue, options, rowObject) {
	 return formatCurrency(cellValue);
 }
 function amountFilterFormatter(cellValue, options, rowObject) {
	 if (cellValue > 0) {
		 return '';
	 }else{
		 cellValue=cellValue*(-1);
		 return formatCurrency(cellValue);
	 }
}
 
 function customDepositFormatter(cellValue, options, rowObject) {
	 if (cellValue > 0) {
		 return formatCurrency(cellValue);
	 }else{
		 return '';
	 }
 }
function reasonfordelete(oper){
	
	 var bkgrid = $("#bankingAccountsGrid");
	 var bkrowId = $("#hiddenbagridrowid").val();
	 var inActive = bkgrid.jqGrid('getCell', bkrowId, 'inActive');
	 var moAccountID=bkgrid.jqGrid('getCell', bkrowId, 'moAccountId');
	
	// alert("Reason for delete Inactive::"+inActive+"==RowID::"+bkrowId+"==moAccountID::"+moAccountID);
	 if(inActive != 1)
		{
	var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	
	if(rowId !== null){
		var status=grid.jqGrid('getCell', rowId, 'status');
		if(oper=="delete"){
			
			 var aType = grid.jqGrid('getCell', rowId, 'moTypeId');
		 if(aType!='2')
			 {
			reasondialogtitle="Reason for deleting";
			jQuery("#reasondialog").dialog('option', 'title', reasondialogtitle);
			if(status=="true"){
				var errorText = "Transaction rollback.You cant delete";
				
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				reasonoperation=oper;
				$("#reasonttextid").val("");
				jQuery( "#reasondialog" ).dialog("open");
			}
			 }
		 else
			 {
			 var errorText = "Check transaction can't delete. Please click Void Check";
				
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			 }
			                                                     
		}
		else if(oper=="newcheckedit")
		{
			var grid = $("#transactionRegisterGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var status=grid.jqGrid('getCell', rowId, '');
			var newcheckformSerialize=$("#newCheckFormID").serialize();	
			var checkdatachangesornotinform=true;
			
			var returnvalue=checktotalandamountequalornot();
			
			console.log(oldcheckformSerialize);
			console.log(newcheckformSerialize);
			
			//alert(oldcheckformSerialize)
			
			//alert(newcheckformSerialize);
			
			if(oldcheckformSerialize.trim() == newcheckformSerialize.trim())
			{
			checkdatachangesornotinform=false;		
			}
			
			console.log(checkdatachangesornotinform);
			
			if(checkdatachangesornotinform){	
				
				$("#reasonttextid").val("");
				reasondialogtitle="Reason for editing";
				jQuery("#reasondialog").dialog('option', 'title', reasondialogtitle);
				reasonoperation=oper;
				jQuery( "#reasondialog" ).dialog("open");
				
			}
			else{
				$("#newCheckDetails").dialog("close");
			}
		
		}
		else{
			
			
			var grid = $("#transactionRegisterGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var status=grid.jqGrid('getCell', rowId, '');
			
			var returnvalue=checktotalandamountequalornot();
			
			var newformSerialize=$("#newTransactionFormID").serialize();	
			var newgridaccountgriddata = glaccountgridserialize();
			var checkdatachangesornotinform=true;
			console.log(oldformSerialize);
			console.log(newformSerialize);
			
			console.log("====================================");
			
			console.log(oldgridaccountgriddata)
			console.log(newgridaccountgriddata)
			
			var returndatavalue=comparetwoarrayobject(oldgridaccountgriddata,newgridaccountgriddata);
			
		//	alert(returndatavalue);
			
			if(mulaccbtn_status==1)
				{
				if(oldformSerialize.trim() == newformSerialize.trim() && returndatavalue)
				{
				checkdatachangesornotinform=false;		
				}
				}
			else
				{			
				if(oldformSerialize.trim() == newformSerialize.trim())
				{
				checkdatachangesornotinform=false;		
				}
				}
			if(checkdatachangesornotinform){	
			//	alert(mulaccbtn_status);
			if(mulaccbtn_status==1){
			//	alert(returnvalue);
				if(returnvalue){
					$("#reasonttextid").val("");
					reasondialogtitle="Reason for editing";
					jQuery("#reasondialog").dialog('option', 'title', reasondialogtitle);
				//	alert(reasonoperation);
					reasonoperation=oper;
					jQuery( "#reasondialog" ).dialog("open");
				}else{
					 var errorText="The total distributed does not equal  the amount deposited";
					 jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open"); 
				 }
				
			}else{
				$("#reasonttextid").val("");
				reasondialogtitle="Reason for editing";
				jQuery("#reasondialog").dialog('option', 'title', reasondialogtitle);
				reasonoperation=oper;
				jQuery( "#reasondialog" ).dialog("open");
			}
		}else{
			$("#newTransactionDetails").dialog("close");
		}
			
		}
		
	}else{
		var errorText = "Please click one of the Transaction to Delete.";
		if(oper=="edit"){
			errorText = "Please click one of the Transaction to Edit.";
		}
		
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
		
}
else
	 {
	 var errorText = "Please Enable Bank Account as active.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	 }
	 
}	 
function comparetwoarrayobject(oldobject,newobject){
	
	if(oldobject.length==newobject.length){
		for(var i=0;i<oldobject.length;i++){
			//alert("[ "+oldobject[i].trim()+" ] <---> [ "+newobject[i].trim()+" ]");
			
			if(oldobject[i].trim()!=newobject[i].trim()){
				return false;
			}
		}
		
	}
	else
	{
		return false;	
	}
	return true;
}
function checktotalandamountequalornot(){
	if(mulaccbtn_status==1){
		 try{setgridtotal();
		 var total=$("#glaccounttotalID").val();
		 var amount=$("#amountID").val();
		 total=total.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 amount=amount.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 total=Number(total);
		 amount=Number(amount);
		 }catch(err){
			 alert(err);
		 }
		 if(total==amount){
			return true;
		 }

	 }else{
		 return true;
	 }

}
 function getSingleMoAccount(MoAccount){
	 $.ajax({
 		url: "./banking/singleAcccountDetails",
 		type: "POST",
 		data : {'moAccountID' : MoAccount},
 		success: function(data) {
 			var aBankingName = data.description;
 			 aCheckno = data.nextCheckNumber;
 			
 			$("#description_ID").empty();
 			$("#description_ID").append(aBankingName);
 			$("#newAccountNameID").empty();
 			$("#newAccountNameID").append(aBankingName);
 			$("#newCheckAccountNameID").empty();
 			$("#newCheckAccountNameID").append(aBankingName);
 			
 			//alert(data.accountType);
 			
 			if(data.accountType == 1) {
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Savings");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Savings");
 	 			$("#newCheckAccountTypeID").empty();
 	 			$("#newCheckAccountTypeID").append("Savings");
 			} else if(data.accountType == 2) {
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Cash");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Cash");
 	 			$("#newCheckAccountTypeID").empty();
 	 			$("#newCheckAccountTypeID").append("Cash");
 			} else if(data.accountType == 3){
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Credit Card");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Credit Card");
 	 			$("#newCheckAccountTypeID").empty();
 	 			$("#newCheckAccountTypeID").append("Credit Card");
 			}else{
 				$("#type_ID").empty();
 	 			$("#type_ID").append("Checking");
 	 			$("#newAccountTypeID").empty();
 	 			$("#newAccountTypeID").append("Checking");
 	 			$("#newCheckAccountTypeID").empty();
 	 			$("#newCheckAccountTypeID").append("Checking");
 			}
 			var aCurrentBal = Number(data.openBalance)+Number(data.additions);
 			var aCurrentBalMain = Number(aCurrentBal)-Number(data.subtractions);
 			var undepositedReceipts=data.undepositedReceipts;
 			var unprintedPayables=data.unprintedPayables;
 			var unprintedPayroll=data.unprintedPayroll;
 			//alert("undepositedReceipts"+undepositedReceipts+"unprintedPayables"+unprintedPayables+"unprintedPayroll"+unprintedPayroll);
 			var resultingbalance=Number(aCurrentBalMain)+Number(undepositedReceipts)-Number(unprintedPayables)-Number(unprintedPayroll);
 			/*Created By : Velmurugan
 			 *Created On :28-08-2014
 			 *Description:Resulting Balance=currentbalance+(futuredeposit-futurewithdrawel) 
 			 * */
 			
 			$.ajax({
 		 		url: "./banking/getResultingBalance",
 		 		type: "POST",
 		 		data : {"moAccountID": MoAccount},
 		 		success: function(data) {
 		 			//currentBalance_ID,resultingBalance_ID
 		 			//Transactiondetails balance
 		 			$("#currentBalance_ID").empty();
 		 			$("#currentBalance_ID").append(formatCurrency(data.currentbalance));
 		 			$("#resultingBalance_ID").empty();
 		 			//$("#resultingBalance_ID").append(formatCurrency(data.resultingbalance));
 		 			
 		 		//Banking details balance
 		 			$("#currentBalanceID").empty();
 		 			$("#currentBalanceID").append(formatCurrency(data.currentbalance));
 		 			console.log('currentBalanceID: '+data.currentbalance);
 		 			var payableApprovedID;
 		 			payableApprovedID =$('#payableApprovedID').val();
 		 			if($('#payableApprovedID').val()!="" && typeof($('#payableApprovedID').val())!="undefined")
 		 			payableApprovedID.replace(/[^0-9\.]+/g, "");
 		 			var resultBalance = (data.currentbalance)-(payableApprovedID);
 		 			console.log('currentBalanceID: '+data.currentbalance+'payableApprovedID :'+payableApprovedID);
 		 			/*$("#resultingBalanceID").empty();
 		 			$("#resultingBalanceID").append(formatCurrency(resultBalance));*/
 		 			$("#resultingBalance_ID").append(formatCurrency(resultBalance));
 		 		}
 		 	});
 			
 			/*$("#currentBalance_ID").empty();
 			$("#currentBalance_ID").append(formatCurrency(aCurrentBalMain));
 			$("#resultingBalance_ID").empty();
 			$("#resultingBalance_ID").append(formatCurrency(resultingbalance));*/
 		}
	});
 }
 
 jQuery( "#newCheckDetails" ).dialog({
		autoOpen: false,
		width: 670,
		title:"New Check",
		modal: true,
		buttons:{
			
		},
		close: function () {
			$('#newCheckFormID').validationEngine('hideAll');
			return true;
		}
	});

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
 
//transactionfilterSelectBox1
//transactionfilterSelectBox2
$('#transactionfilterSelectBox1').on('change', function() {
   //alert( this.value ); // or $(this).val()
   var thisValue = this.value;
   var transactionfilterSelectBox2 = $('#transactionfilterSelectBox2');
   var transactionfilterTextBox = $("#transactionfilterTextBox");
   $('#transactionfilterSelectBox2').html("");
   $('#transactionfiltertextboxID').val("");
   $('#transactionfilterfromdate').val("");
   $('#transactionfiltertodate').val("");
   $("#transfilTBdynamic").html("");
   transactionfilterSelectBox2.removeAttr("disabled");
   //transactionfilterTextBox.html("");    
   var html = "";
   if(thisValue === "Amount")
   {       
	   $("#transactionfilterfromdate").css({ display: "none"});
       $("#transactionfiltertodate").css({ display: "none"});
	   $("#transfilTBdynamic").css({ display: "none"});
	   $("#transactionfiltertextboxID").css({ display: "inline-block"});
	   
       html = "";
       html = "<option value='Exact'>Exact</option><option value='Greater'>Greater</option><option value='Greater or equal'>Greater or equal</option><option value='Less'>Less</option><option value='Less or equal'>Less or equal</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
   }
   if(thisValue === "Cleared Status")
   {
	   $("#transactionfilterfromdate").css({ display: "none"});
       $("#transactionfiltertodate").css({ display: "none"});
	   $("#transactionfiltertextboxID").css({ display: "none"});
	   $("#transfilTBdynamic").css({ display: "inline-block"});
	   
       html = "";
       html = "<option selected value=''>--Select--</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
       transactionfilterSelectBox2.attr("disabled", true);
       
       html = "";
       html = "<option value='Reconcile'>Reconcile</option><option value='Unreconciled'>Unreconciled</option>";
       $("#transfilTBdynamic").html("");
       $("#transfilTBdynamic").append(html);       
   }
   if(thisValue === "Date")
   {
	   $("#transactionfilterfromdate").css({ display: "inline-block"});
       $("#transactionfiltertodate").css({ display: "inline-block"});
	   $("#transactionfiltertextboxID").css({ display: "none"});
	   $("#transfilTBdynamic").css({ display: "none"});
	   
       html = "";
       html = "<option selected value=''>--Select--</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
       transactionfilterSelectBox2.attr("disabled", true);
       
   }
   if(thisValue === "Description")
   {
	   $("#transactionfilterfromdate").css({ display: "none"});
       $("#transactionfiltertodate").css({ display: "none"});
	   $("#transactionfiltertextboxID").css({ display: "inline-block"});
	   $("#transfilTBdynamic").css({ display: "none"});
	   
       html = "";
       html = "<option value='Contains'>Contains</option><option value='Ends with'>Ends with</option><option value='Exact'>Exact</option><option value='Starts with'>Starts with</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
   }
   if(thisValue === "G/L Account #")
   {
   $("#transactionfilterfromdate").css({ display: "none"});
   $("#transactionfiltertodate").css({ display: "none"});
   $("#transactionfiltertextboxID").css({ display: "none"});
   $("#transfilTBdynamic").css({ display: "inline-block"});

   html = "<option selected value=''>--Select--</option>";
   transactionfilterSelectBox2.html("");
   transactionfilterSelectBox2.append(html);
   transactionfilterSelectBox2.attr("disabled", true);

   //testttt
   $.ajax({

   url: "./banking/getglaccountlist",
   type: "GET",
   async: false,
   success: function(data) {
   html="";
   for(var i=0;i<data.length;i++){
   html=html+"<option value='"+data[i].coAccountId+"'>"+data[i].number+" - "+data[i].description+"</option>";
   console.log(data[i].coAccountId+"==="+data[i].number+"=="+data[i].description);
   }
   }
   });




   $("#transfilTBdynamic").html("");
   $("#transfilTBdynamic").append(html);
   }

   if(thisValue === "Reference")
   {
	   $("#transactionfilterfromdate").css({ display: "none"});
	   $("#transactionfiltertodate").css({ display: "none"});
	   $("#transactionfiltertextboxID").css({ display: "inline-block"});
	   $("#transfilTBdynamic").css({ display: "none"});
   
       //html = "<option selected value=''>--Select--</option>";
      
      // transactionfilterSelectBox2.attr("disabled", true);
       html = "<option value='Contains'>Contains</option><option value='Exact'>Exact</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
   }
   if(thisValue === "Type")
   {
	   $("#transactionfilterfromdate").css({ display: "none"});
       $("#transactionfiltertodate").css({ display: "none"});
	   $("#transactionfiltertextboxID").css({ display: "none"});
	   $("#transfilTBdynamic").css({ display: "inline-block"});
	   
       html = "<option selected value=''>--Select--</option>";
       transactionfilterSelectBox2.html("");
       transactionfilterSelectBox2.append(html);
       transactionfilterSelectBox2.attr("disabled", true);
       html = "";
       html = "<option value='Deposit'>Deposit</option><option value='Withdrawal'>Withdrawal</option><option value='Fee'>Fee</option><option value='Interest'>Interest</option>";
       $("#transfilTBdynamic").html("");
       $("#transfilTBdynamic").append(html);
   }
  
});

function findbyvalidation(){
	var transactionfilterSelectBox1=$("#transactionfilterSelectBox1").val();
	var transactionfilterSelectBox2=$("#transactionfilterSelectBox2").val();
	var transactionfiltertextboxID=$("#transactionfiltertextboxID").val();
	var transfilTBdynamic=$("#transfilTBdynamic").val();
	var transactionfilterfromdate=$("#transactionfilterfromdate").val();
	var transactionfiltertodate=$("#").val();
	
	if(transactionfilterSelectBox1!=null && transactionfilterSelectBox1!="" && transactionfilterSelectBox1.length!=0 ){
	if(transactionfilterSelectBox1 === "Amount")
	   { 
		 if(transactionfiltertextboxID==null || transactionfiltertextboxID=="" || transactionfiltertextboxID.length==0){
			$("#findbytderrordiv").html("*Required Amount");
			return false;
		}
		
	   }else if(transactionfilterSelectBox1 === "Date")
	   {
		   if(!(transactionfilterfromdate!=null && transactionfilterfromdate!="" && transactionfilterfromdate.length!=0) && !(transactionfiltertodate!=null && transactionfiltertodate!="" && transactionfiltertodate.length!=0)){
		   $("#findbytderrordiv").html("*Required Fromdate or Todate");
		   return false;
		   }
	   }else if(transactionfilterSelectBox1 === "Description"){
		 if(transactionfiltertextboxID==null || transactionfiltertextboxID=="" || transactionfiltertextboxID.length==0){
			 $("#findbytderrordiv").html("*Required Description");
			 return false;
			}
		   
	   }else  if(transactionfilterSelectBox1 === "Reference"){
		  if(transactionfiltertextboxID==null || transactionfiltertextboxID=="" || transactionfiltertextboxID.length==0){
			  $("#findbytderrordiv").html("*Required reference");
			  return false;
			}
	   }
	}else{
		$("#findbytderrordiv").html("*Required column name");
		return false;
	}
	return true;
}
function FindByTransactionDetails(){
  var validate=findbyvalidation();
	if(validate){
	
	var filarray = getfilterarraydata1();
	var arrlist =getfilterarraydata2();
	
   $("#findbytransactionRegisterGrid").jqGrid('GridUnload');
   findbyTransactionRegisterDetails(arrlist,filarray);
   jQuery( "#findbytransactionRegisterGriddiv" ).dialog("open");
 
   return false;
	}
}


function getfilterarraydata1()
{
	var transactionfilterSelectBox1=$("#transactionfilterSelectBox1").val();
	var transactionfilterSelectBox2=$("#transactionfilterSelectBox2").val();
	var transactionfiltertextboxID=$("#transactionfiltertextboxID").val();
	var transfilTBdynamic=$("#transfilTBdynamic").val();
	var transactionfilterfromdate=$("#transactionfilterfromdate").val();
	var transactionfiltertodate=$("#transactionfiltertodate").val();
	var transactionfilterCheckBox="0";
	if(document.getElementById("transactionfilterCheckBox").checked){
		transactionfilterCheckBox="1";
	}
	
	var filterarray=new Array();
	
	
	if(transactionfilterSelectBox1!=null && transactionfilterSelectBox1!="" && transactionfilterSelectBox1.length>0){
		filterarray.push(transactionfilterSelectBox1);
	}
	if(transactionfilterSelectBox2!=null && transactionfilterSelectBox2!="" && transactionfilterSelectBox2.length>0){
		filterarray.push(transactionfilterSelectBox2);
	}
	if(transactionfiltertextboxID!=null && transactionfiltertextboxID!="" && transactionfiltertextboxID.length>0){
		filterarray.push(transactionfiltertextboxID);
	}
	if(transfilTBdynamic!=null && transfilTBdynamic!="" && transfilTBdynamic.length>0){
		filterarray.push(transfilTBdynamic);
	}
	
	if(transactionfilterfromdate!=null && transactionfilterfromdate!="" && transactionfilterfromdate.length>0){
		filterarray.push(transactionfilterfromdate);
	}
	if(transactionfiltertodate!=null && transactionfiltertodate!="" && transactionfiltertodate.length>0){
		filterarray.push(transactionfiltertodate);
	}
	filterarray.push(transactionfilterCheckBox);
	
	
   
   return filterarray;
}

function getfilterarraydata2()
{

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

	   return aArrayList;
}


 jQuery( "#reasondialog" ).dialog({
		autoOpen: false,
		width: 400,
		title:"Reason",
		modal: true,
		buttons:{
			ok:function(){
				if($("#reasonttextid").val()==""){
					$("#errordivreason").empty();
					$("#errordivreason").append("Reason required");
				}else{
					$("#errordivreason").empty();
					if(reasonoperation=="delete"){
						deleteTransaction();
					}
					else if(reasonoperation == "newcheckedit")
						{
						console.log("i am here");
						editNewCheckDetails();
						}
					else{
						editTransactionDetails();
					}
					
				}
				
			}
		},
		close: function () {
			$('#reasondialog').validationEngine('hideAll');
			return true;
		}
	});

 function getcoFiscalPerliodDate(x)
 {
	 $.ajax({
		 
	 		url: "./banking/getcoFiscalPeriod",
	 		type: "GET",
	 		success: function(data) {
	 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
	 					
	 		}		 
	 });	 
 }
 
 function saveTransactionDetails(){
	 
	 createtpusage('Banking-Transaction Details','New Transaction Save','Info','Banking,New Trannsaction,glaccounttotalID:'+$("#glaccounttotalID").val());
	 
	 var aOper = "add";
	 //alert("mulaccbtn_status"+mulaccbtn_status);
	 if(mulaccbtn_status==1){
		 try{
			 setgridtotal();
		 var total=$("#glaccounttotalID").val();
		 var amount=$("#amountID").val();
		 total=total.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 amount=amount.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 total=Number(total);
		 amount=Number(amount);
		 }catch(err){
			 alert(err);
		 }
		 var gridrowcount = $("#glAccountsGrid").jqGrid('getDataIDs').length;
		 if(gridrowcount>0){
			 if(total==amount){
				 
				
					 	loadTransactionDetails(aOper);
					
				 }else{
					 var errorText="The total distributed does not equal  the amount deposited";
					 jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open"); 
				 }
		 }else{
			 var errorText="Add atleast one record";
			 jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open"); 
		 }
		 

	 }else{
		 loadTransactionDetails(aOper);
	 }
	 
	 return false;
 }
 
 function editTransactionDetails(){
	 var aOper = "edit";
	 loadTransactionDetails(aOper);
	 return false;
 }
 
 function editNewCheckDetails()
 {
	 var aOper = "new";
	 loadTransactionDetails(aOper);
	 return false;
 }
 
 
 function cancelTransactionDetails(){
	 $("#transactionDetailsDialog").dialog("close");
	 return false;
 }
 
 function reOpenCheck(){
	 
	 $('#newcheckDetailsDiv').html("");
	 var grid = $("#transactionRegisterGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var status=grid.jqGrid('getCell', rowId, 'status');
		
		//alert(rowId +"Test---->"+status)
		
		if(rowId !== null && status=="false"){
		
			 var aTransDate = grid.jqGrid('getCell', rowId, 'transDate');
			 var aType = grid.jqGrid('getCell', rowId, 'moTypeId');
			 var aDescription = grid.jqGrid('getCell', rowId, 'description');
			 var aReference = grid.jqGrid('getCell', rowId, 'reference');
			 var aWithDrawal = grid.jqGrid('getCell', rowId, 'withDrawel');
			 var aDeposit = grid.jqGrid('getCell', rowId, 'amount');
			 var aGLAccount1 = grid.jqGrid('getCell', rowId, 'coAccountId');
			 var aReconciled = grid.jqGrid('getCell', rowId, 'reconciled')
			 var transactionID = grid.jqGrid('getCell', rowId, 'moTransactionId');
			 var rxMasterId = grid.jqGrid('getCell', rowId, 'rxMasterId'); 
			 
			 
			 
			 var aMemo = grid.jqGrid('getCell', rowId, 'memo');
			 $("#savecheckButton").css('display','none');	 
			 $("#editcheckButton").show();
			 
			 $("#moTrnsID").val(transactionID);
			 $("#checkdateID").val(aTransDate);
			 $("#memoID").val(aMemo);
			 $("#checkNo").val(aReference);
			 $("#paytoID").val(aDescription);
			 
			 //alert(aWithDrawal);
			 if(aWithDrawal!=null)
				 {
				 $("#checkamountID").val(aWithDrawal.replace(/[^0-9\.]+/g,"").replace(".00", ""));
				 $("#oldcheckamt").val(aWithDrawal.replace(/[^0-9\.]+/g,"").replace(".00", ""));
				 }
			 else
				 {
				 $("#checkamountID").val(aDeposit.replace(/[^0-9\.]+/g,"").replace(".00", ""));
				 $("#oldcheckamt").val(aDeposit.replace(/[^0-9\.]+/g,"").replace(".00", ""));
				 }
			 
			 $("#checkRxMasterID").val(rxMasterId);
			 $("#glAccountCheckID option[value=" + aGLAccount1 + "]").attr("selected", "selected");
			 if(aReconciled === '0'){
				 $("#reconciledID").attr("checked", false);
			 }else{
				 $("#reconciledID").attr("checked", true);
			 }
			 
			 if(aDescription=="Vendor Write Checks")
				{
				 $("#editcheckButton").hide();
				 $("#moTrnsID").attr("disabled",true);
				 $("#checkdateID").attr("disabled",true);
				 $("#memoID").attr("disabled",true);
				 $("#checkNo").attr("disabled",true);
				 $("#paytoID").attr("disabled",true);
				 $("#checkamountID").attr("disabled",true);
				 $("#glAccountCheckID").attr("disabled",true);
				 $("#reconciledCheckID").attr("disabled",true);
				 				 
				} 
			 else
				 {
				 $("#moTrnsID").removeAttr("disabled");
				 $("#checkdateID").removeAttr("disabled");
				 $("#memoID").removeAttr("disabled");
				 $("#checkNo").removeAttr("disabled");
				 $("#paytoID").removeAttr("disabled");
				 $("#checkamountID").removeAttr("disabled");
				 $("#glAccountCheckID").removeAttr("disabled");
				 $("#reconciledCheckID").removeAttr("disabled");
				 }
			 $("#newCheckDetails").dialog("open");
			 
			 
			 oldcheckformSerialize = $("#newCheckFormID").serialize();
		}
		
 }
 

 function editTransaction(){
	 
	 var bkgrid = $("#bankingAccountsGrid");
	 var bkrowId = $("#hiddenbagridrowid").val();
	 var inActive = bkgrid.jqGrid('getCell', bkrowId, 'inActive');
	 
	// alert("Inactive::"+inActive)
	 if(inActive != 1)
		{ 
	 
	 document.getElementById("newTransactionDetailsDiv").innerHTML ="";
	 
	 $("#glAccountsGrid").jqGrid('setGridState','visible');
	var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var status=grid.jqGrid('getCell', rowId, 'status');
	
	//alert(grid.jqGrid('getCell', rowId, 'moAccountId'));
	
	if(rowId !== null && status=="false"){
		
		 var recordcount = $("#glAccountsGrid").jqGrid('getDataIDs').length; 
		 oldgridaccountgriddata= glaccountgridserialize();
		if(recordcount>0){
			mulaccbtn_status=0;
			openmultipleaccount();
			setgridtotal();
		//	alert("loaded"+recordcount);
			
		}else{
			mulaccbtn_status=1;
			openmultipleaccount();
		}
		
		 
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
		
		
		 if(aType != '2')
			 {
		 
		 createtpusage('Banking-Transaction Details','Edit Transaction','Info','Banking,Edit Transaction,Description:'+aDescription+',GLAccount ID:'+aGLAccount);
		 
		 if(aType === '0'){
			 $('#depositID').attr("checked",true);
		 }else if(aType === '1'){
			 $('#withdrawalID').attr("checked",true);
		 }else if(aType === '3'){
			 $('#feeID').attr("checked",true);
		 }else if(aType === '4'){
			 $('#interestID').attr("checked",true);
		 }
		 
		 $("#referenceID").val(aReference);
		 $("#descriptionIDs").val(aDescription);
		 
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
		 $("#amounthiddenID").val($("#amountID").val());
 		 
		$("#newTransactionDetails").dialog("open");
			 }
		 else
			 {
			 reOpenCheck();
			 }
	}else{
		if(status=="true"){
			 var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">Transaction Rollback.You cant edit</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
		}else{
			 var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:red;">Please Select the Record.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
		}
		
	}
	
	  oldformSerialize = $("#newTransactionFormID").serialize();
	  
 }
 else
	 {
	 var newDialogDiv = jQuery(document.createElement('div'));
	 var errorText = "Please Enable Bank Account as active.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	 }
	
 }
 
 function voidTransaction(){
	 
 var bkgrid = $("#bankingAccountsGrid");
 var bkrowId = bkgrid.jqGrid('getGridParam', 'selrow');
 var inActive = bkgrid.jqGrid('getCell', bkrowId, 'inActive');
 if(inActive != 1)
	{
	var grid = $("#transactionRegisterGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var moTypeId = grid.jqGrid('getCell', rowId, 'moTypeId');
	var aDescription = grid.jqGrid('getCell', rowId, 'displaydiscription');
	 var aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
	 
	if(moTypeId=="2")
		{
	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Void this Check Record?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Are you sure?", 
			buttons:{
				"Yes": function(){
				 createtpusage('Banking-Transaction Details','Void Check','Info','Banking,Void Check,Description:'+aDescription+',GLAccount ID:'+aGLAccount);
					var oper = "void";
					loadTransactionDetails(oper);
					jQuery(this).dialog("close");
					$("#transactionRegisterGrid").trigger("reloadGrid");
					
				},
				"No": function(){
					jQuery(this).dialog("close");
				}} 
		}).dialog("open");
		return true;
	}else{
		var errorText = "Please click one of the Check Transaction to Void.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
		}
	else
		{
		var errorText = "Please click one of the Check Transaction to Void.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
		}
	 else
		 {
		 var errorText = "Please Enable Bank Account as active.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		 }
 }
 
 function deleteTransaction(){
	 
	 var bkgrid = $("#bankingAccountsGrid");
	 var bkrowId = bkgrid.jqGrid('getGridParam', 'selrow');
	 var inActive = bkgrid.jqGrid('getCell', bkrowId, 'inActive');
	 if(inActive != 1)
		{
	 var grid = $("#transactionRegisterGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var aDescription = grid.jqGrid('getCell', rowId, 'displaydiscription');
		 var aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
		if(rowId !== null){
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete this Transaction Record?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
				buttons:{
					"Submit": function(){
						createtpusage('Banking-Transaction Details','Delete Transaction','Info','Banking,Delete Transaction,Description:'+aDescription+',GLAccount ID:'+aGLAccount);
						var oper = "delete";
						loadTransactionDetails(oper); 
						jQuery(this).dialog("close");
						$("#transactionRegisterGrid").trigger("reloadGrid");
						$("#reasonttextid").val("");
						$("#errordivreason").empty();
						jQuery( "#reasondialog" ).dialog("close");
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
	 else
		 {
		 var errorText = "Please Enable Bank Account as active.";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		 }
		
 }
 /*updated by:Velmurugan
  *updated on:31-08-2014
  *Description: when adding a new transaction one row has been inserted in motransaction table
  *while edit the transaction two rows has been inserter one is for nullify the old record and 
  *another one is for new edit amount has been inserted in table.
  *while delete the transaction one row has been inserted for nullify the amount .
  *deposit -withdrawel insert
  *withdrawel - deposit insert
  * */
 function loadTransactionDetails(oper){
	 if(oper == "new")
	 createtpusage('Banking-Transaction Details','Saving New Check','Info','Banking,Saving Check Details,Check No:'+$("#checkNo").val());
	
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
	 var adepo='';
	 var awithdraw='';
	 var newmotypeid='';
	 var aRxMasterID='';
	 var aRxAddressID='';
	 var aMemo='';
	if(oper !== 'void'){
		if(oper !== 'new' )
			{
		aDate = $("#dateID").val();
		aDeposit = $("#depositID").is(':checked');
		aWithDrawal = $("#withdrawalID").is(':checked');
		aFee = $("#feeID").is(':checked');
		aInterest = $("#interestID").is(':checked');
		aGLAccount = $("#glAccountID").val();
		aReconciled = $("#reconciledID").is(':checked');
		aAmount = $("#amountID").val();
		
		aReference = $("#referenceID").val();
		if(aAmount === ''){
			aAmount1 = 0;
		}else{
			aAmount1 = $("#amountID").val();
		}
		if(aDeposit == true){
			aTypeID = 0;
			newmotypeid=0;
		}else if(aWithDrawal == true){
			aTypeID = 1;
			newmotypeid=1;
		}else if(aFee == true){
			aTypeID = 3;
			newmotypeid=3;
		}else if(aInterest == true){
			aTypeID = 4;
			newmotypeid=4;
		}
		if(oper=='delete'){
			aDate = grid.jqGrid('getCell', rowId, 'transDate');
			aDescription=document.reasonformname.reasonttextname.value;
			aTypeID = grid.jqGrid('getCell', rowId, 'moTypeId');
			var aDepositAmt = grid.jqGrid('getCell', rowId, 'amount');
			var aWithDrawalAmt = grid.jqGrid('getCell', rowId, 'withDrawel');
			aReference = grid.jqGrid('getCell', rowId, 'reference');
			aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
			aRecouncil = grid.jqGrid('getCell', rowId, 'reconciled');
			aRxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
			aRxAddressID = grid.jqGrid('getCell', rowId, 'rxAddressId');
			
			if(aDepositAmt !== ''){
				aAmount1 = aDepositAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
				
			}else if(aWithDrawalAmt !== ''){
				aAmount1 = aWithDrawalAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
			}
			if(aTypeID==0){
				aTypeID=1;
			}else{
				aTypeID=0;
			}
			
		//	alert(aTypeID);
		}else if(oper=='edit'){
			aTypeID = grid.jqGrid('getCell', rowId, 'moTypeId');
			aDescription=document.reasonformname.reasonttextname.value;
		}else {
			aDescription = document.newTransactionFormName.descriptionName.value;	
		}
			}
		else
			{	
				aDate = $("#checkdateID").val();
				aGLAccount = $("#glAccountCheckID").val();
				aAmount = $("#checkamountID").val();
				aReference = $("#checkNo").val();
				aReconciled = $("#reconciledCheckID").is(':checked');
				aTypeID = 2;
				aRxMasterID=$("#checkRxMasterID").val();
				aMemo=$("#memoID").val();
				
				if(aAmount === ''){
					aAmount1 = 0;
				}else{
					aAmount1 = $("#checkamountID").val().replace(/[^0-9\.]+/g,"").replace(".00", "");
				}
				
				
				if($("#moTrnsID").val()=="")
				{
				aDescription = $("#paytoID").val(); 
				}
				else
				{
				aTypeID = grid.jqGrid('getCell', rowId, 'moTypeId');
				aDescription = $("#paytoID").val(); 
				}
			}
	}else{
		
		aVoid = 1;
		
		aDate = grid.jqGrid('getCell', rowId, 'transDate');
		aTypeID = grid.jqGrid('getCell', rowId, 'moTypeId');
		//aDescription = grid.jqGrid('getCell', rowId, 'description');
		aDescription = grid.jqGrid('getCell', rowId, 'displaydiscription');
		aReference = grid.jqGrid('getCell', rowId, 'reference');
		var aDepositAmt = grid.jqGrid('getCell', rowId, 'amount');
		var aWithDrawalAmt = grid.jqGrid('getCell', rowId, 'withDrawel');
		aGLAccount = grid.jqGrid('getCell', rowId, 'coAccountId');
		aRecouncil = grid.jqGrid('getCell', rowId, 'reconciled');
		aRxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
		aRxAddressID = grid.jqGrid('getCell', rowId, 'rxAddressId');
		
		if(aDepositAmt !== ''){
			aAmount1 = aDepositAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
		}else if(aWithDrawalAmt !== ''){
			aAmount1 = aWithDrawalAmt.replace(/[^0-9\.]+/g,"").replace(".00", "");
		}
		if(aTypeID==0){
			aTypeID=1;
		}else{
			aTypeID=0;
		}
		
	}
	 if(aReconciled == true){
		 aRecouncil = 1;
	 }else{
		 aRecouncil = 0;
	 }
	 if(oper !== 'add'){
		 if(oper !== 'new'){
			if(rowId !== null){
				aTransID = grid.jqGrid('getCell', rowId, 'moTransactionId');
				aMoAccID = grid.jqGrid('getCell', rowId, 'moAccountId');
			}
		 }
		 else
			 {
			 aTransID = grid.jqGrid('getCell', rowId, 'moTransactionId');
			 aMoAccID = gridBank.jqGrid('getCell', rowIdBank, 'moAccountId');
			 }
	 }else{
		 
		 aMoAccID = gridBank.jqGrid('getCell', rowIdBank, 'moAccountId');
	 }
	 var oldamount=Number($("#amounthiddenID").val());
	 var newamount=Number($("#amountID").val());
	 if(oper=="delete" || oper == "void"){
		 adepo=grid.jqGrid('getCell', rowId, 'moTransactionId');
		 awithdraw=grid.jqGrid('getCell', rowId, 'moTransactionId');
	 }
	
	 if(aTypeID === false){
			rowId ='delglacc_'+rowId;	 
			aMoAccID = grid.jqGrid('getCell', "'"+rowId+"'", 'moAccountId');
			aTypeID = grid.jqGrid('getCell', "'"+rowId+"'", 'moTypeId');
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
	 aTransactionArray.push(aMemo);
	 aTransactionArray.push(aRxMasterID);
	 aTransactionArray.push(aRxAddressID);
	 var currentbalance=$("#currentBalance_ID").text();
	 var resultingbalance=$("#resultingBalance_ID").text();
	 var reason=document.reasonformname.reasonttextname.value;
	 if(currentbalance==""){
		 currentbalance="0.00";
	 }else{
		 currentbalance = currentbalance.replace(/[^0-9\.-]+/g,"").replace(".00", "");
		 		 
	 }
	 if(resultingbalance==""){
		 resultingbalance="0.00";
	 }else{
		 resultingbalance=resultingbalance.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	 }
	 var checkformvalid=checkformvalidation();
	 
	 
	 if(checkformvalid || oper=="delete" || oper=="void" ){
		 var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
		 var datetoCheck = "";
		 var dataToSend = "";
		 	if(oper=="delete" || oper=="void")
			 {
			 datetoCheck = (new Date().getMonth() + 1) + "/" + new Date().getDate() + "/" + new Date().getFullYear();
			 }
		 	else if(oper == "new")
			 {
			 datetoCheck =$("#checkdateID").val();
			 }
		 	else
			 {
		 		datetoCheck = $('#dateID').val();
		 		
				var gridRows = $('#glAccountsGrid').getRowData();
				var rowData = new Array();
				for (var i = 0; i < gridRows.length; i++) {
					var row = gridRows[i];
					rowData.push($.param(row));
					}
				dataToSend = JSON.stringify(gridRows);
			 }
		 	
		 
		 $.ajax({
				url: "./checkAccountingCyclePeriods",
				data:{"datetoCheck":datetoCheck,"UserStatus":checkpermission},
				type: "POST",
				success: function(data) { 

					
					if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" || oper=="delete" || oper=="void" )
					{
						periodid=data.cofiscalperiod.coFiscalPeriodId;
						yearid = data.cofiscalperiod.coFiscalYearId;
		 
						$.ajax({
					 		url: "./banking/saveTransactionDetails",
					 		type: "POST",
					 		data : {
					 			'transactionDetails' :aTransactionArray,
					 			'oldamount':oldamount,
					 			'editamount':newamount,
					 			'currentbalance':currentbalance,
					 			'resultingbalance':resultingbalance,
					 			'NewMoTypeId':newmotypeid,
					 			'coFiscalPeriodId':periodid,
					 			'coFiscalYearId':yearid,
					 			'gridData':dataToSend,
					 			},
					 		success: function(data) {
					 			
					 			if(oper === 'add'){
					 					saveglaccountgriddata(data,aTransactionArray,yearid,periodid);
					 					$("#newTransactionDetails").dialog("close");
					 					$("#transactionRegisterGrid").jqGrid('GridUnload');
					 					loadTransactionRegisterDetails(aTranDetails);
					 					getSingleMoAccount(aMoAccID);
										$("#transactionRegisterGrid").trigger("reloadGrid"); /*}}]}).dialog("open");*/
					 		 			$("#bankingAccountsGrid").jqGrid('GridUnload');
					 		 			loadBankingAccounts(); 					
					 					$("#bankingAccountsGrid").trigger("reloadGrid");
					 					setTimeout("$('#bankingAccountsGrid').jqGrid('setSelection','"+rowIdBank+"')", 300); 
					 				return false;
					 			}else if(oper === 'edit'){
					 					saveglaccountgriddata(data,aTransactionArray,yearid,periodid);
					 					$("#newTransactionDetails").dialog("close");
					 					$("#transactionRegisterGrid").jqGrid('GridUnload');
					 					loadTransactionRegisterDetails(aTranDetails); 					
					 					getSingleMoAccount(aMoAccID);
					 					$("#transactionRegisterGrid").trigger("reloadGrid"); 
					
					 		 			$("#bankingAccountsGrid").jqGrid('GridUnload');
					 		 			loadBankingAccounts(); 					
					 					$("#bankingAccountsGrid").trigger("reloadGrid");
					 					setTimeout("$('#bankingAccountsGrid').jqGrid('setSelection','"+rowIdBank+"')", 300); 
					 					
					 					$("#reasonttextid").val("");
					 					$("#errordivreason").empty();
					 					jQuery( "#reasondialog" ).dialog("close");
					 				return false;
					 			}else if(oper === 'delete'){
					 					$("#newTransactionDetails").dialog("close");
					 					$("#transactionRegisterGrid").jqGrid('GridUnload');
					 					loadTransactionRegisterDetails(aTranDetails);
					 					getSingleMoAccount(aMoAccID);
					 					$("#transactionRegisterGrid").trigger("reloadGrid"); /*}}]}).dialog("open");*/
					 		 			$("#bankingAccountsGrid").jqGrid('GridUnload');
					 		 			loadBankingAccounts(); 					
					 					$("#bankingAccountsGrid").trigger("reloadGrid");
					 					setTimeout("$('#bankingAccountsGrid').jqGrid('setSelection','"+rowIdBank+"')", 300); 
					 				return false;
					 			}
					 			else if(oper === 'void'){
					 					$("#newTransactionDetails").dialog("close");
					 					$("#transactionRegisterGrid").jqGrid('GridUnload');
					 					loadTransactionRegisterDetails(aTranDetails);
					 					getSingleMoAccount(aMoAccID);
					 					$("#transactionRegisterGrid").trigger("reloadGrid"); /*}}]}).dialog("open");*/
					 		 			$("#bankingAccountsGrid").jqGrid('GridUnload');
					 		 			loadBankingAccounts(); 					
					 					$("#bankingAccountsGrid").trigger("reloadGrid");
					 					setTimeout("$('#bankingAccountsGrid').jqGrid('setSelection','"+rowIdBank+"')", 300); 
					 				return false;
					 			}
					 			
					 		}
						});
					}
					else
						{
						
						if(data.AuthStatus == "granted")
						{	
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
												buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
											}).dialog("open");
						}
						else
						{
							showDeniedPopup();
						}
						}
			  	},
	   			error:function(data){
	   				console.log('error');
	   				}
	   			});
		
		  }
	 else if(checkformvalid || oper=="new")
		 {
		 
		 	
			var aGLAccount = $("#glAccountCheckID").val();
			var aAmount = $("#checkamountID").val().replace(/[^0-9\.-]+/g,"").replace(".00", "");
			var aReference = $("#checkNo").val();
			var apaytoID =$("#paytomasterID").val();
			newmotypeid = 2;
			oldcheckamount = 0;
			
			if($("#moTrnsID").val()!="")
				{
					var index = aTransactionArray.indexOf("new");
		
					if (index !== -1) {
						aTransactionArray[index] = "edit";
						oper = "edit";
					}					
					oldcheckamount =$("#oldcheckamt").val().replace(/[^0-9\.-]+/g,"").replace(".00", "");
				}	
			
		 
		  if(aAmount==null || aAmount=="" || isNaN(aAmount) ){
				document.getElementById("newcheckDetailsDiv").innerHTML ="Required Dollars";
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 2000);
				returnvalue=false;
				
				}
			else if(Number(aAmount)<0){
				document.getElementById("newcheckDetailsDiv").innerHTML ="Required amount should be greaterthan 0";
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 2000);
				returnvalue=false;
			}

			else if(aGLAccount == "-1")
				{
				document.getElementById("newcheckDetailsDiv").innerHTML ="Required GL Account";
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 2000);
				returnvalue=false;
				}
		  
			else if(aReference == "")
			{
				document.getElementById("newcheckDetailsDiv").innerHTML ="Required Check No.";
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 2000);
				returnvalue=false;
			}
		  
			/*else if(apaytoID == "")
			{
				document.getElementById("newcheckDetailsDiv").innerHTML ="Required Valid PayTo.";
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 2000);
				returnvalue=false;
			}*/
		  
			else
				{
				var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
				
					$.ajax({
						url: "./checkAccountingCyclePeriods",
						data:{"datetoCheck":$('#checkdateID').val(),"UserStatus":checkpermission},
						type: "POST",
						success: function(data) { 
							if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
							{
								periodid=data.cofiscalperiod.coFiscalPeriodId;
								yearid = data.cofiscalperiod.coFiscalYearId;
								
								console.log(periodid+"<==>"+yearid);		
								
								$.ajax({
									url:'./banking/getCheckNumberExists',
									type:"POST",
									data: {checkno: $("#checkNo").val(),moAccountID:aMoAccID},
									success:function(data){ 
								
										if(data || oper == "edit")
											{
											$("#savecheckButton").attr("disabled",true);
												$.ajax({
										 		url: "./banking/saveTransactionDetails",
										 		type: "POST",
										 		data : {'transactionDetails' :aTransactionArray,
										 			'oldamount':oldcheckamount,
										 			'editamount':aAmount,
										 			'currentbalance':currentbalance,
										 			'resultingbalance':resultingbalance,
										 			'NewMoTypeId':newmotypeid,
										 			'coFiscalPeriodId':periodid,
										 			'coFiscalYearId':yearid
										 			},
										 		success: function(data) {
										 
										 		//	alert("hi leo");
										 			$("#newCheckDetails").dialog("close");
								 					$("#transactionRegisterGrid").jqGrid('GridUnload');
								 					loadTransactionRegisterDetails(aTranDetails);
								 					getSingleMoAccount(aMoAccID);
								 					$("#transactionRegisterGrid").trigger("reloadGrid");
								 					jQuery( "#reasondialog" ).dialog("close");
										 		}
										 		})
											}
											else
											{
									
												var Warnintext = "This Check Number Already Used.";
												$(newDialogDiv).attr("id","msgdlg");
												jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
												jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
													buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
											
											}
							 		
									}
									});
								
						   		
							}
							else
								{
								
								if(data.AuthStatus == "granted")
								{	
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
														buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
													}).dialog("open");
								}
								else
								{
									showDeniedPopup();
								}
								}
					  	},
			   			error:function(data){
			   				console.log('error');
			   				}
			   			});
					
					}
		 	 		
		 }
		 		
	 
 }
 

 
 function savecheckDetails()
 {
	// $("#savecheckButton").attr("disabled",true);
	 oper="new";
	 loadTransactionDetails(oper);
 }
 
 function transactionDetails(){
	 $("#transactionfilterfromdate").css({ display: "none"});
     $("#transactionfiltertodate").css({ display: "none"});
	 $("#transactionfiltertextboxID").css({ display: "none"});
	 $("#transfilTBdynamic").css({ display: "none"});
	 
	 
	 var transactionfilterSelectBox1 = $('#transactionfilterSelectBox1');
	 var transactionfilterSelectBox2 = $('#transactionfilterSelectBox2');
	 var transactionfilterTextBox = $("#transactionfilterTextBox");
	 var html = "";
	 transactionfilterSelectBox1.html("");
	 transactionfilterSelectBox1.html("<option value=''>--Select--</option> <option value='Amount'>Amount</option> <option value='Cleared Status'>Cleared Status</option> <option value='Date'>Date</option> <option value='Description'>Description</option> <option value='G/L Account #'>G/L Account #</option> <option value='Reference'>Reference</option> <option value='Type'>Type</option>");
	 $('#transactionfilterSelectBox2').html("");
	 transactionfilterSelectBox2.removeAttr("disabled");
	 html = "";
	 html = "<option selected value=''>--Select--</option>";
	 transactionfilterSelectBox2.append(html);

	 jQuery( "#transactionDetailsDialog" ).dialog("open");
	return true;
}
 
 function closeTransactionDetails(){
	 var newTransactionform=$("#newTransactionFormID").serialize();
	 if(oldformSerialize === newTransactionform){
	 jQuery( "#newTransactionDetails" ).dialog("close");
	 	return true;
	 }else{
		 var newDialogDiv = jQuery(document.createElement('div'));
			var errorText = "Bank Transaction Details modified, please save before leave the page!";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: [{height:35,text: "OK",click: function() {
						$(this).dialog("close");
						$(this).dialog('destroy').remove();
						return false;
					}
				}]}).dialog("open");
	 }
}

jQuery(function () {
	var newTransactionform=$("#newTransactionFormID").serialize();
	jQuery( "#transactionDetailsDialog" ).dialog({
		autoOpen: false,
		width: 560,
		height:150,
		title:"View Transaction Details",
		modal: true,
		buttons:{ }
	});
	
	jQuery( "#findbytransactionRegisterGriddiv" ).dialog({
		autoOpen: false,
		width: 1000,
		height:500,
		title:"Transaction Details",
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
	//loadTransactionRegisterDetails(aArrayList);
	$('#transactionRegisterGrid').jqGrid('setGridParam',{postData: {'transactionDetails' : aArrayList}});
	$("#transactionRegisterGrid").trigger("reloadGrid");
	jQuery( "#transactionDetailsDialog" ).dialog("close");
	return false;
}
/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:set if the momultipleaccount is null or space returns 0 this method is only for momultipleaccountid
 */
function setinteger(cellValue, options, rowObject){
	//alert(cellValue);
	if(cellValue==""||cellValue==undefined){
		return "0";
	}
	return cellValue;
	}

/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:Load the saved data in glaccount grid based upon motransactionid from momultipleaccount table
 */
function loadGlAccountRegisterDetails(){
	$("#glAccountsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/GlAccountRegisterList',
		pager: jQuery('#glAccountsGridPager'),
		postData: {'motransactionID':function(){
		var grid = $("#transactionRegisterGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var motransid=grid.jqGrid('getCell', rowId, 'moTransactionId');
		if(motransid==undefined || motransid==""){
			motransid="0";
		}
		return motransid;}},
	   	colNames:['id','AccountId','Account', 'Amount'],
	   	colModel:[
	   	    {name:'moMultipleAccountsId',index:'moMultipleAccountsId', width:40,editable:false, hidden:true,formatter:setinteger,editrules:{required:false}, editoptions:{size:10}},
	   	    {name:'coAccountId',index:'coAccountId', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
	   	    {name:'description',index:'description', width:40,editable:true, hidden:false,width: 220, editrules:{required:true},
	   	    	editoptions:{
	   				dataInit : function(elem) {
	   					$(elem).autocomplete({
	   						source : 'banking/accountList',
	   						minLength : 3,
	   						select : function(event, ui) {
	   							var id = ui.item.id;
	   							var name = ui.item.value;
	   							var rowobj=jQuery("#glAccountsGrid").jqGrid('setCell', glacctglobalrowid, 'coAccountId', id);
	   							
	   							//$("#rxManufacturerID").val(id);
	   							/*$.ajax({
	   								url : "./jobtabs2/getFactoryID",
	   								type : "GET",
	   								data : {
	   									'rxMasterID' : id,
	   									'descripition' : name
	   								},
	   								success : function(data) {
	   									$("#new_row_veFactoryId").val(data);
	   									$("#veFactoryId").val(data);
	   								}
	   							});*/
	   						}
	   					});
	   				}
	   			}
	   	    
	   	    },
			{name:'amount',index:'amount', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			],
		//rowNum: aBankingPage,	
			
		/*This below code inactive the pagination */
		rowList: [],        // disable page size dropdown
		pgbuttons: false,     // disable page control like next, back button
		pgtext: null,         // disable pager text like 'Page 0 of 10'
		viewrecords: false,
		/*This below code active the pagination */   
		/*pgbuttons: true,	
		recordtext: '',
		rowList: [10, 50, 200, 500, 1000],
		viewrecords: true,*/
		pager: '#glAccountsGridPager',
		sortorder: "asc",
		altRows: false,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: "GlAccount",
		width: 460, 
		rownumbers:true,
		rownumWidth:34,
		height : 100,
		loadonce: false,
		cellEdit: false,
		cellsubmit: 'clientArray',
		editurl: 'clientArray',
		
		loadComplete:function(data) {
			//var ids = $("#glAccountsGrid").jqGrid('getDataIDs');
			//alert("this is loadcomplete"+ids.length);
			setgridtotal();
			//$(".ui-pg-selbox").attr("selected", aBankingPage);
		},
		gridComplete:function(){
			//var ids = $("#glAccountsGrid").jqGrid('getDataIDs');
			//alert("GridComplete"+ids.length);
			
	
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
    		/*$("#glAccountsGrid").jqGrid('setSelection', rowId, false);
    		if (rowId && rowId !== glacctglobalrowid) {
    			$("#glAccountsGrid").jqGrid('restoreRow',glacctglobalrowid);
    			alert(rowId);
    			$("#glAccountsGrid").jqGrid('editRow', rowId  � in case of the client editing);
                glacctglobalrowid = rowId;
            }*/
    		
    		
    		glacctglobalrowid=rowId;
    	},
    	ondblClickRow: function(rowId) {
		},
	});
	

	$("#glAccountsGrid").jqGrid("navGrid","#glAccountsGridPager", {
		add : false,
		edit : false,
		del : false,
		search:false,refresh:false}
	);
	$("#glAccountsGrid").jqGrid("inlineNav","#glAccountsGridPager", {
		add : true,
		addtitle:"Add",
		edit : true,
		edittitle:"Edit",
		save: true,
		savetitle:"Save",
		cancel : true,
		addParams: {
	       // position: "afterSelected",
	        addRowParams: {
				//keys : true,
				oneditfunc : function(rowid) {
					$("#info_dialog").css("z-index", "10000");
					
				},
				successfunc : function(response) {
					
						return true;
				},
				aftersavefunc : function(response) {
					//alert("aftersavefunc");
					
					//alert("aftersavefunc");
				     var ids = $("#glAccountsGrid").jqGrid('getDataIDs');
				     var veaccrrowid;
				     if(ids.length==1){
				      veaccrrowid = 0;
				     }else{
				      var idd = jQuery("#glAccountsGrid tr").length;
				      for(var i=0;i<ids.length;i++){
				       if(idd<ids[i]){
				        idd=ids[i];
				       }
				      }
				       veaccrrowid= idd;
				     }
				     var aSelectedRowId = $("#glAccountsGrid").jqGrid('getGridParam', 'selrow');
				     var changerowid;
				     if(aSelectedRowId=="new_row"){
				      console.log("IFselectedLineItemGrid"+aSelectedRowId); 
				      $("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
				      changerowid=Number(veaccrrowid)+1;
				      $("#new_row_custombutton").attr("id", changerowid+"_custombutton");
				      
				     }else{
				       changerowid=aSelectedRowId;
				     }
				     var grid=$("#glAccountsGrid");
				     grid.jqGrid('resetSelection');
				        var dataids = grid.getDataIDs();
				        for (var i=0, il=dataids.length; i < il; i++) {
				            grid.jqGrid('setSelection',dataids[i], false);
				        }
				     setgridtotal();         
				    },
				    errorfunc : function(rowid, response) {
				     //alert("errorfunc");
				     return false;
				    },
				    afterrestorefunc : function(rowid) {
				     // alert("afterrestorefunc");
				    }
				   
					
			
				
	    },
	    editParams: {
	        // the parameters of editRow
	    	//  keys: true,
	          oneditfunc: function (rowid) {
	        	$("#info_dialog").css("z-index", "10000");
	        	console.log('OnEditfunc');
	        //   alert("row with rowid=" + rowid + " is editing.");
	        }
	        
	    },
	    aftersavefunc : function(response) {

	    	   var ids = $("#glAccountsGrid").jqGrid('getDataIDs');
	    	   var veaccrrowid;
	    	   if(ids.length==1){
	    	    veaccrrowid = 0;
	    	   }else{
	    	    var idd = jQuery("#glAccountsGrid tr").length;
	    	    for(var i=0;i<ids.length;i++){
	    	     if(idd<ids[i]){
	    	      idd=ids[i];
	    	     }
	    	    }
	    	     veaccrrowid= idd;
	    	   }
	    	   var aSelectedRowId = $("#glAccountsGrid").jqGrid('getGridParam', 'selrow');
	    	   var changerowid;
	    	   if(aSelectedRowId=="new_row"){
	    	    console.log("IFselectedLineItemGrid"+aSelectedRowId); 
	    	    $("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
	    	    changerowid=Number(veaccrrowid)+1;
	    	    $("#new_row_custombutton").attr("id", changerowid+"_custombutton");
	    	    
	    	   }else{
	    	     changerowid=aSelectedRowId;
	    	   }
	    	   var grid=$("#glAccountsGrid");
	    	   grid.jqGrid('resetSelection');
	    	      var dataids = grid.getDataIDs();
	    	      for (var i=0, il=dataids.length; i < il; i++) {
	    	          grid.jqGrid('setSelection',dataids[i], false);
	    	      }
	    	   
	    	   setgridtotal();  
	    	   
	    	  }
	    	         
	    	     }
	    	  }
	    	 );
		
	
	
	$("#glAccountsGrid").navButtonAdd('#glAccountsGridPager',
			{ 	caption:"", 
				buttonicon:"ui-icon-trash", 
				onClickButton: deleteglgrid,
				position: "last", 
				title:"Delete", 
				cursor: "pointer"
			} 
		);
		
	
}
function deleteglgrid(){
	$('#glAccountsGrid').jqGrid('delRowData',glacctglobalrowid);
	var ids = $("#glAccountsGrid").jqGrid('getDataIDs'); 
	 for(var i=0;i<ids.length;i++){
		 var id=ids[i];
		 var rowid=i+1;
		 rowid="delglacc_"+rowid;
		 //alert(id);
		// $("#" + id).attr("id", rowid);
		// $("#"+id).attr("id", i+1);
	 }
	 $("#glAccountsGrid_iladd").removeClass("ui-state-disabled");
	 $("#glAccountsGrid_iledit").removeClass("ui-state-disabled");
	 $("#glAccountsGrid_ilsave").addClass("ui-state-disabled");
	 $("#glAccountsGrid_ilcancel").addClass("ui-state-disabled");
	 $("#glAccountsGrid").jqGrid('resetSelection');
	 setgridtotal();
	
}
/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:This method for set grid total for amount column in glaccound grid 
 */
function setgridtotal(){
	
	 var ids = $("#glAccountsGrid").jqGrid('getDataIDs'); 
	 
	 var totalamount=0;
	 for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		
		 cellValue =$("#glAccountsGrid").jqGrid ('getCell', selectedRowId, 'amount');
		 totalamount=Number(totalamount)+Number(cellValue);
	 }
	 if(isNaN(totalamount)){
		 totalamount=0;
	 }
	 $("#glaccounttotalID").val(formatCurrency(totalamount));
	
}

/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:This method for changing multiple account status only for new transaction and edit transaction method 
 *			  "0" means the grid is shown in the popup
 *			  "1" means the grid is not  shown in the popup
 */
function openmultipleaccount(){
	$("#glAccountID").val("-1");
	$("#glaccounttotalID").val("$0.00");
	//glaccountrowid amountrowid
	
	if(mulaccbtn_status==0){
		mulaccbtn_status=1
	}else{
		mulaccbtn_status=0;
	}
	if(mulaccbtn_status==1){
		$("#multipleAccountID").css({ display: "none" });
		$("#multipleAccountIDgrey").css({ display: "inline-block"});
		//$("#amountlabelid").css({ display: "none" });
		//$("#amountID").css({ display: "none" });
		$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
		$("#glaccountlabel").css({ display: "none" });
		 $("#glAccountID").css({ display: "none" });
		$("#gbox_glAccountsGrid").css({ display: "inline-block" });
		$("#glAccountsGridPager").css({ display: "inline-block" });
		$("#totallabelid").css({ display: "inline-block" });
		$("#glaccounttotalID").css({ display: "inline-block" });
	}else{
		$("#multipleAccountID").css({ display: "inline-block" });
		$("#multipleAccountIDgrey").css({ display: "none"});
		//$("#amountlabelid").css({ display: "inline-block" });
		//$("#amountID").css({ display: "inline-block" });
		$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
		
		$("#glaccountlabel").css({ display: "inline-block" });
		$("#glAccountID").css({ display: "inline-block" });
		$("#gbox_glAccountsGrid").css({ display: "none" });
		$("#glAccountsGridPager").css({ display: "none" });
		$("#totallabelid").css({ display: "none" });
		$("#glaccounttotalID").css({ display: "none" });
	}
	
}
/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:Save all glaccount grid data to the server that is momultipleaccount table 
 */
function saveglaccountgriddata(motransactionid,aTransactionArray,yearid,periodid){
	/*var gridRows = $('#glAccountsGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);
	$.ajax({
 		url: "./banking/saveglaccountgrid",
 		type: "POST",
 		data : {"gridData": dataToSend,
 			'motransactionid':motransactionid,
 			'transactionDetails':aTransactionArray,
 			'coFiscalPeriodId':periodid,
 			'coFiscalYearId':yearid
 			},
 		success: function(data) {*/
 		$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
 		/*}
 	});*/
}
function glaccountgridserialize(){
	var gridRows = $('#glAccountsGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	//var dataToSend = JSON.stringify(gridRows);
	return rowData;
}
/*Created By:Velmurugan
 *Created On:5-9-2014
 *Description:This method for only multipleaccount button purpose 
 */
function openmultipleaccountforonlybutton(){
	
	$("#glAccountID").val("-1");
	$("#glaccounttotalID").val("$0.00");
	//glaccountrowid amountrowid
	
	if(mulaccbtn_status==0){
		mulaccbtn_status=1
	}else{
		mulaccbtn_status=0;
	}
	
	//alert(mulaccbtn_status);
	
	if(mulaccbtn_status==1){
		var amountID=$("#amountID").val();
		if(amountID!=null && amountID!=""){
			$("#multipleAccountID").css({ display: "none" });
			$("#multipleAccountIDgrey").css({ display: "inline-block"});
			
			$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
			$("#glaccountlabel").css({ display: "none" });
			 $("#glAccountID").css({ display: "none" });
			$("#gbox_glAccountsGrid").css({ display: "inline-block" });
			$("#glAccountsGridPager").css({ display: "inline-block" });
			$("#totallabelid").css({ display: "inline-block" });
			$("#glaccounttotalID").css({ display: "inline-block" });
			
		}else{
			$("#multipleAccountID").css({ display: "inline-block" });
			$("#multipleAccountIDgrey").css({ display: "none" });
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">Amount Required</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			mulaccbtn_status=0;
		}
			
		//$("#amountlabelid").css({ display: "none" });
		//$("#amountID").css({ display: "none" });
		
	}else{
		//$("#amountlabelid").css({ display: "inline-block" });
		//$("#amountID").css({ display: "inline-block" });
		$("#multipleAccountID").css({ display: "inline-block" });
		$("#multipleAccountIDgrey").css({ display: "none" });
		$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
		$("#glaccountlabel").css({ display: "inline-block" });
		$("#glAccountID").css({ display: "inline-block" });
		$("#gbox_glAccountsGrid").css({ display: "none" });
		$("#glAccountsGridPager").css({ display: "none" });
		$("#totallabelid").css({ display: "none" });
		$("#glaccounttotalID").css({ display: "none" });
	}
	
}

/*Created By:Velmurugan
 *Created on:5-9-2014
 *Description:New Transaction form validation
 **/
function checkformvalidation(){
	
	var returnvalue=true;
	var date=$("#dateID").val();
	var amount=$("#amountID").val();
	var glaccount=$("#glAccountID").val();
	var total=$("#glaccounttotalID").val().replace(/[^0-9\.-]+/g,"").replace(".00", "");;
	
	amount=amount.replace(/[^0-9\.-]+/g,"").replace(".00", "");
	if(date==null ||date==""){
		document.getElementById("newTransactionDetailsDiv").innerHTML ="Required Transaction Date"; 
		returnvalue=false;
		}
	else if(amount==null || amount=="" || isNaN(amount) ){
		document.getElementById("newTransactionDetailsDiv").innerHTML ="Required Amount";
		returnvalue=false;
		
		}
	else if(Number(amount)<0){
		document.getElementById("newTransactionDetailsDiv").innerHTML ="Required amount should be greaterthan 0";
		returnvalue=false;
	}

	else if(glaccount == "-1" && mulaccbtn_status == 0)
		{
		document.getElementById("newTransactionDetailsDiv").innerHTML ="Required Gl Account";
		returnvalue=false;
		}
	
	/*else if(glaccount == "-1" && mulaccbtn_status == 0){
		document.getElementById("newTransactionDetailsDiv").innerHTML ="Required Gl Account";
		returnvalue=false;
		
	} 
	*/
	
		
	return returnvalue;
}


function findbyTransactionRegisterDetails(aDetails,filterarray){
	var aBankingPage = $("#bankingPerPage").val();
	//Based upon accountid the grid will populate
	$("#findbytransactionRegisterGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./banking/findbytransactionRegisterList',
	   	colNames:['', 'MoTransactionId', 'RxMasterId', 'RxAddressId', 'CoAccountId', 'MoAccountId', 'Date', 'Type', 'MoTypeID', 'CheckType', 'Description','', 'Reference', 'Void', 'Reconciled', 'TempRec', 'Printed', 'Deposit', 'Withdrawal', 'Direct Deposit','Balance','oper' ,'FutureorCurrent','Memo'],
	   	colModel:[
	   	    {name:'void_',index:'void_',align:'right',width:20,editable: false,hidden: false, formatter: voidImage},
	   	    {name:'moTransactionId',index:'moTransactionId', width:40,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'rxMasterId',index:'rxMasterId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'rxAddressId',index:'rxAddressId',align:'center', width:100,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'coAccountId',index:'coAccountId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'moAccountId',index:'moAccountId', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'transDate',index:'transDate', align:'center', width:82,editable:true,  hidden:false, editrules:{required:true}, editoptions:{size:10}},
			{name:'moTransactionTypeId',index:'moTransactionTypeId', align:'left', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}, formatter:statusFormatter},
			{name:'moTypeId',index:'moTypeId', align:'left', width:60,editable:true, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'checkType',index:'checkType', width:60,editable:true,  hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'displaydiscription',index:'displaydiscription', width:235,editable:true, align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description', width:60,editable:true, hidden:true,align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'reference',index:'reference', width:87,editable:true,  hidden:false, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'void_',index:'void_', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'reconciled',index:'reconciled', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'tempRec',index:'tempRec', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'printed',index:'printed', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'amount',index:'amount', width:132,editable:true, editrules:{required:true}, align:'right', editoptions:{size:10}, formatter:customDepositFormatter, hidden:false},
			{name:'withDrawel',index:'withDrawel', width:132,editable:true, editrules:{required:true}, align:'right', formatter:amountFilterFormatter, editoptions:{size:10}, hidden:false},
			{name:'directDeposit',index:'directDeposit', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'balance',index:'balance', width:140,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10},formatter:formatCurrencyformat, hidden:false},
			{name:'status',index:'status', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true},
			{name:'futureorcurrent',index:'futureorcurrent', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true},
			{name:'memo',index:'memo', width:60,editable:true, editrules:{required:true},  align:'right',editoptions:{size:10}, hidden:true}],
		rowNum: 500,	
		postData: {'filterDetails':filterarray,'moAccountID':function(){var moAccountID=$("#hiddenmoaccountid").val();return moAccountID;}},
		pgbuttons: true,	
		recordtext: '',
		rowList: [10, 50, 200, 500, 1000],
		viewrecords: true,
		pager: '#findbytransactionRegisterGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: false,
		width: 950, rownumbers:true,rownumWidth:34, height : 360, 
		loadonce: false,
		/*grouping:true,
	   	groupingView : {
	   		groupField : ['futureorcurrent'],
	   		groupColumnShow: [false],
	   		groupCollapse:false,
	   		groupSummary : [true],
	   		groupDataSorted : true 
	   	},*/
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
   		
   		
			//$("#gbox_glAccountsGrid").css({ display: "inline-block" });
			//$("#glAccountsGridPager").css({ display: "inline-block" });
			//$("#glAccountsGrid").jqGrid("clearGridData", true).trigger("reloadGrid");
   		
   		
			//$("#glAccountsGrid").trigger("reloadGrid");
   	},
   	ondblClickRow: function(rowId) {
   		
   		/*var grid = $("#transactionRegisterGrid");
   		var moTransactionTypeId=grid.jqGrid('getCell', rowId, 'moTransactionTypeId');
   		
   		console.log(moTransactionTypeId);
   		
   		if(moTransactionTypeId=="Check")
   			{
   			reOpenCheck();
   			}
   		else
   			{
   			editTransaction();
   			}*/
		}
	}).navGrid('#findbytransactionRegisterGridPager',{add:false,edit:false,del:false,refresh:false,search:false},{},{},{});
	

	$("#findbytransactionRegisterGrid").navButtonAdd('#findbytransactionRegisterGridPager', {
		  caption: "CSV",
		  title: "CSV",
		  buttonicon: "ui-icon-document",
		  onClickButton: printbankgridcsv,
		  position: "last"
		});
	$("#findbytransactionRegisterGrid").navButtonAdd('#findbytransactionRegisterGridPager', {
		  caption: "Print",
		  title: "Print",
		  buttonicon: "ui-icon-print",
		  onClickButton:printbankgridpdf ,
		  position: "last"
		});
	
	return true;
}

function printbankgridcsv()
{
	var filarraycsv =getfilterarraydata1();
	//alert("["+filarraycsv+"]")
	var moAccountIDcsv = $("#hiddenmoaccountid").val();
	window.open("./banking/printBankingFilter?filterArray[]="+encodeURIComponent(filarraycsv)+"&moAccountID="+moAccountIDcsv+"&viewType=csv");
	
}

function printbankgridpdf()
{
	var filarraypdf = getfilterarraydata1();
	var moAccountIDpdf = $("#hiddenmoaccountid").val();
	window.open("./banking/printBankingFilter?filterArray[]="+encodeURIComponent(filarraypdf)+"&moAccountID="+moAccountIDpdf+"&viewType=pdf");
	
}



