var acc_number;
jQuery(document).ready(function() {
	$( "input#searchJob" ).attr("placeholder","Minimum 2 characters required to get Accounts list");
	$( ".alertstatus").hide();
	
	
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

	 var myselect = $("#typeAccount").val();
	 console.log('Selected Val: '+myselect);
	 if(myselect==='Asset'){
	  $('#accountTypeID').val('0');
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"inline"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
			
	  }
	  else if(myselect==='Liability'){
	   $('#accountTypeID').val('1');
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"inline"});
			$("#equityid").css({"display":"none"});
	  }
	  else if(myselect==='Equity'){
	  $('#accountTypeID').val('1');
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"inline"});
	  }
	  else if(myselect==='Income'){
	  $('#accountTypeID').val('1');
			$("#incomeid").css({"display":"inline"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
	  }
	  else if(myselect==='Expense'){
	  $('#accountTypeID').val('0');
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"inline"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
	  }else{
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
	  }
		  
		  $("#incomechkbox").click(function(){
		  if($("#incomechkbox").is(":checked"))
			  $("#cogchkbox").removeAttr("checked")
				  
		  })
		  
		  $("#cogchkbox").click(function(){
		  if($("#cogchkbox").is(":checked"))
			  $("#incomechkbox").removeAttr("checked")
		  })
		  
		  $(".cabsClass").click(function() {
			  $(".cabsClass").attr("checked", false); //uncheck all checkboxes
			  $(this).attr("checked", true);  //check the clicked one
			});
		  $(".clbsClass").click(function() {
			  $(".clbsClass").attr("checked", false); //uncheck all checkboxes
			  $(this).attr("checked", true);  //check the clicked one
			});
});


function setAccountType(){
	  var myselect = $("#typeAccount").val();
		 console.log('Selected Val: '+myselect);
			  if(myselect==='Asset'){
				  $('#accountTypeID').val('0');
					$("#incomeid").css({"display":"none"});
					$("#expenseid").css({"display":"none"});
					$("#othersid").css({"display":"none"});
					$("#assetsid").css({"display":"inline"});
					$("#liabilityid").css({"display":"none"});
					$("#equityid").css({"display":"none"});
			  }
			  else if(myselect==='Liability'){
				  $('#accountTypeID').val('1');
					$("#incomeid").css({"display":"none"});
					$("#expenseid").css({"display":"none"});
					$("#othersid").css({"display":"none"});
					$("#assetsid").css({"display":"none"});
					$("#liabilityid").css({"display":"inline"});
					$("#equityid").css({"display":"none"});
			  }
			  else if(myselect==='Equity'){
				  $('#accountTypeID').val('1');
					$("#incomeid").css({"display":"none"});
					$("#expenseid").css({"display":"none"});
					$("#equityid").css({"display":"inline"});
					$("#assetsid").css({"display":"none"});
					$("#liabilityid").css({"display":"none"});
					$("#othersid").css({"display":"none"});
			  }
			  else if(myselect==='Income'){
				  $('#accountTypeID').val('1');
					$("#incomeid").css({"display":"inline"});
					$("#expenseid").css({"display":"none"});
					$("#othersid").css({"display":"none"});
					$("#assetsid").css({"display":"none"});
					$("#liabilityid").css({"display":"none"});
					$("#equityid").css({"display":"none"});
			  }
			  else if(myselect==='Expense'){
				  $('#accountTypeID').val('0');
					$("#incomeid").css({"display":"none"});
					$("#expenseid").css({"display":"inline"});
					$("#othersid").css({"display":"none"});
					$("#assetsid").css({"display":"none"});
					$("#liabilityid").css({"display":"none"});
					$("#equityid").css({"display":"none"});
			  }else{
				  $('#accountTypeID').val('0');
					$("#incomeid").css({"display":"none"});
					$("#expenseid").css({"display":"none"});
					$("#othersid").css({"display":"inline"});
					$("#assetsid").css({"display":"none"});
					$("#liabilityid").css({"display":"none"});
					$("#equityid").css({"display":"none"});
			  }
 	//Asset - Asset
	//Liability - Liability
	//Equity - Equity
	//Income - Income
	//Expense - Expense
	//accountTypeID
	//0 Debit
	//1 Credit
}

var newDialogDiv = jQuery(document.createElement('div'));

function loadChartsAccount(){
	var aChartsPage = $("#chartsAccID").val();
	$("#chartsOfAccountsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./company/chartsAccountsList',
		//pager: jQuery('#chartsOfAccountsGridPager'),
	   	colNames:['Acct. #', 'Account Description', 'CoAccountID', 'InActive', 'Bal. Sheet Column', 'IncludeWhenZero', 'DebitBalance', 'ContraAccount', 'LineAboveAmount', 'LineBelowAmount', 'TotalingLevel', 'VerticalSpacing', 
																																'HorizontalSpacing', 'FontLarge', 'FontBold', 'FontItalic', 'FontUnderline', 'Tax1099', 'SubAccount', 'IsSubAccount', 'IsMasterAccount', 'DollarSign', 'AccountType','FinancialStatus'],
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
			{name:'dollarSign',index:'dollarSign', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'typeAccount',index:'typeAccount', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'financialStatus',index:'financialStatus', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}],
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
    		$( ".alertstatus").hide();
    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("chartsDetailsFromID").reset();
    		loadAccountsDetais(rowId);
    		$( ".alertstatus").hide();	
		}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function loadAccountsDetais(rowId){
	
	var rowData = jQuery("#chartsOfAccountsGrid").getRowData(rowId); 
	var coAccountID = rowData['coAccountId'];
	var number = ""+rowData['number'];
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
	var typeAccount=rowData['typeAccount'];
	var accountType = ((rowData['debitBalance'] === "true") ? 1 : 0 );
	var infinancialStatus = rowData['financialStatus'];
	var selectedaccSegmentcount=0;
	
	acc_number = number;
	var numberArray = number.split("-");
	console.log(number);
	for(var i=0;i<numberArray.length;i++)
		{
		console.log(numberArray[i]);
		$("#numberNameID"+(i+1)).val(numberArray[i]);
		
		if(numberArray[i]=="")
			{
			leavezerowhenBlank(i+1);			
			}
		selectedaccSegmentcount=i+1;
		}
	var segmentCount=parseInt($("#segmentCount").val())
	
	if(selectedaccSegmentcount<segmentCount)
		{
		for(var j=selectedaccSegmentcount;j<segmentCount;j++)
			{
			leavezerowhenBlank(j+1);			
			}
		}
	
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
	$("#typeAccount").val(typeAccount);
	$('#accountTypeID').val(accountType);
	
	$("#incomechkbox").attr("checked", false);
	$("#cogchkbox").attr("checked", false);
	$("#expchkbox").attr("checked", false);
	$("#cabs_caid").attr("checked", false);
	$("#cabs_faid").attr("checked", false);
	$("#cabs_oaid").attr("checked", false);
	$("#clbs_clid").attr("checked", false);
	$("#clbs_ltlid").attr("checked", false);
	$("#assetincludeonbsid").attr("checked", false);
	$("#equityincludeonbsid").attr("checked", false);
	
	/*//Asset - Asset 
	$("#cabs_caid").attr("checked", false); =4
	$("#cabs_faid").attr("checked", false); =5
	$("#cabs_oaid").attr("checked", false); =6
	//Liability - Liability
	$("#clbs_clid").attr("checked", false); =7
	$("#clbs_ltlid").attr("checked", false); =8
	//Equity - Equity
	$("#equityincludeonbsid").attr("checked", false); =9
	//Income - Income
	//Expense - Expense
	//accountTypeID
	//0 Debit
	//1 Credit */	
	
	if(typeAccount==='Asset'){
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"inline"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
			
			
			if(infinancialStatus == '4'){
				$("#cabs_caid").attr("checked", true);
				$("#assetincludeonbsid").attr("checked", true);
			}else if(infinancialStatus == '5'){
				$("#cabs_faid").attr("checked", true);
				$("#assetincludeonbsid").attr("checked", true);
			}else if(infinancialStatus == '6'){
				$("#cabs_oaid").attr("checked", true);
				$("#assetincludeonbsid").attr("checked", true);
			}
			
	  }
	  else if(typeAccount==='Liability'){
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"inline"});
			$("#equityid").css({"display":"none"});
			
			if(infinancialStatus == '7'){
				$("#clbs_clid").attr("checked", true);
				$("#liabsincludeonbsid").attr("checked", true);
			}else if(infinancialStatus == '8'){
				$("#clbs_ltlid").attr("checked", true);
				$("#liabsincludeonbsid").attr("checked", true);
			}
	  }
	  else if(typeAccount==='Equity'){
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"inline"});
			if(infinancialStatus == '9'){
				$("#equityincludeonbsid").attr("checked", true);
			}
	  }
	  else if(typeAccount==='Income'){
			$("#incomeid").css({"display":"inline"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
			if(infinancialStatus == '1'){
				$("#incomechkbox").attr("checked", true);
				//$("#expchkbox").attr("checked", false);
				//$("#cogchkbox").attr("checked", false);
			}else if(infinancialStatus == '2'){
				$("#cogchkbox").attr("checked", true);
				//$("#incomechkbox").attr("checked", false);
				//$("#expchkbox").attr("checked", false);
			}
	  }
	  else if(typeAccount==='Expense'){
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"inline"});
			$("#othersid").css({"display":"none"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
			if(infinancialStatus == '3'){
			$("#expchkbox").attr("checked", true);
			}
	  }else{
			$("#incomeid").css({"display":"none"});
			$("#expenseid").css({"display":"none"});
			$("#othersid").css({"display":"inline"});
			$("#assetsid").css({"display":"none"});
			$("#liabilityid").css({"display":"none"});
			$("#equityid").css({"display":"none"});
	  }
	
	
	$.ajax({
		url: "./company/getLedgerDetail",
		type: "GET",
		data : { 'accountID' : coAccountID },
		success: function(data) {
			$.each(data, function(key, valueMap) {										
				
        		if("yearToDate"==key)
				{				
					$.each(valueMap, function(index, value){						
						var creditAmount = formatCurrency(value.credit);
						var debitAmount = formatCurrency(value.debit);
						var openAmount =  formatCurrency(value.bankOpeningBalance);
						var closeAmount = 0;
						
						var checkCloseAmt=0;
						
						
						if(value.bankOpeningBalance==null)
						{
						closeAmount = formatCurrency(isNaN((0 + parseFloat(value.debit)) - parseFloat(value.credit))? 0 : (0 + parseFloat(value.debit)) - parseFloat(value.credit));
						
						checkCloseAmt = isNaN((0 + parseFloat(value.debit)))? 0 : (0 + parseFloat(value.debit));
						}
						else
						{
						closeAmount = formatCurrency(isNaN ((parseFloat(value.bankOpeningBalance) + parseFloat(value.debit)) - parseFloat(value.credit)) ? 0 : (parseFloat(value.bankOpeningBalance) + parseFloat(value.debit)) - parseFloat(value.credit));
						
						checkCloseAmt =isNaN ((parseFloat(value.bankOpeningBalance) + parseFloat(value.debit))) ? 0 : (parseFloat(value.bankOpeningBalance) + parseFloat(value.debit));
						}
					
						var crDr='';
						
						
						if(checkCloseAmt <= parseFloat(value.credit))
							{
							crDr = 'CR';
							closeAmount = closeAmount.replace('-','');
							}
						else
							{
							crDr = 'DB';
							}
						
						$("#yearToDateDBID").empty();
						$("#yearToDateDBID").append(openAmount+" DB");
						$("#yearToDateDB1ID").empty();
						$("#yearToDateDB1ID").append(debitAmount+" DB");
						$("#yearToDateCRID").empty();
						$("#yearToDateCRID").append(creditAmount+" CR");
						$("#yearToDateDB2ID").empty();
						//$("#yearToDateDB2ID").append(formatCurrency(value.bankClosingBalance)+" CR");
						$("#yearToDateDB2ID").append(closeAmount+" "+crDr);
						
					
					}); 
					
				}
        		if("periodData"==key)
				{				
					$.each(valueMap, function(index, value){						
						var creditAmount = formatCurrency(value.credit);
						var debitAmount = formatCurrency(value.debit);
						var openAmount =  value.bankOpeningBalance;
						var closeAmount =  value.bankClosingBalance;
						
					
						var crDr='';
						var crDrop='';
						
						
						if(closeAmount <= 0)
							{
							crDr = 'CR';
							closeAmount = closeAmount*-1;
							}
						else
							{
							crDr = 'DB';
							}
						
						if(openAmount <= 0)
						{
						crDrop = 'CR';
						openAmount = openAmount*-1;
						}
						else
						{
						crDrop = 'DB';
						}
					
						
						
						$("#periodDBID").empty();
						$("#periodDBID").append(formatCurrency(openAmount)+" "+crDrop);
						$("#periodDB1ID").empty();
						$("#periodDB1ID").append(debitAmount+" DB");
						$("#periodCRID").empty();
						$("#periodCRID").append(creditAmount+" CR");
						$("#periodDB2ID").empty();
						$("#periodDB2ID").append(formatCurrency(closeAmount)+" "+crDr);
						
					}); 
				}
			});
		}
	});
	return true;
}

function saveChartsOfAccounts(){
	var grid = $("#chartsOfAccountsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if($("#coAccountNameID").val()==""){
	/*	var errorText = "Please select the Account from the Grid to Edit.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;*/
		if($("#typeAccount").val()=="")
		{
		var errorText = "Account type is Required.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		
		}
		
		else
			{
			saveNewChart();
			}
		
	}else
		{
		if($("#typeAccount").val()=="")
		{
		var errorText = "Account type is Required.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		
		}
		else
		{
			 	if($("#typeAccount").val()=="Income")
				{
				if(!$("#incomechkbox").is(":checked") && !$("#cogchkbox").is(":checked"))
				{
				var errorText = "Please Choose Finacial Report Formatting.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				}
				}
			 	else if($("#typeAccount").val()=="Expense")
				{
				if(!$("#expchkbox").is(":checked"))
				{
				var errorText =  "Please Choose Finacial Report Formatting.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				}
				}
			 	else if($("#typeAccount").val()=="Asset"){
			 		
					if($("#assetincludeonbsid").is(":checked") &&  $(".cabsClass").is(":checked")==false)
					{
						var errorText =  "Please select any one of Asset!";
						$( "#assetMsg").empty();
						$( "#assetMsg").append(errorText);
						setTimeout(function() {
				            $('#assetMsg').empty();
				        },3500);
						return false;	
					}
					
				}else if($("#typeAccount").val()=="Liability"){
					if($("#liabsincludeonbsid").is(":checked")&&  $(".clbsClass").is(":checked")==false)
					{
						var errorText =  "Please select any one of Liability!";
						$("#liabilityMsg").empty();
						$("#liabilityMsg").append(errorText);
						setTimeout(function() {
				            $('#liabilityMsg').empty();
				        },3500);
						return false;
					}
					
				}
				
			var financialtype;
			var chartsDetails = $("#chartsDetailsFromID").serialize();
			
			if($("#typeAccount").val()=="Income")
				{
				if($("#incomechkbox").is(":checked"))
				financialtype = "1";
				else
				financialtype = "2";
				}
			else if($("#typeAccount").val()=="Expense")
				{
				financialtype = "3";
				}
			else if($("#typeAccount").val()=="Asset"){
				if($("#cabs_caid").is(":checked"))
					financialtype = "4";
				else if($("#cabs_faid").is(":checked"))
					financialtype = "5";
				else if($("#cabs_oaid").is(":checked"))
					financialtype = "6";
			}
			else if($("#typeAccount").val()=="Liability"){
				if($("#clbs_clid").is(":checked"))
					financialtype = "7";
				else if($("#clbs_ltlid").is(":checked"))
					financialtype = "8";
			}
			else if($("#typeAccount").val()=="Equity"){
					financialtype = "9";
			}
			else{
				financialtype = "0";
			}
		
			//alert(financialtype);
			chartsDetails = chartsDetails+"&financialtype="+financialtype;
			
			
			$.ajax({
				url: "./company/updateChartAccount",
				type: "POST",
				data : chartsDetails,
				success: function(data) {
					
					createtpusage('Company-Chart Accounts','Save Chart Accounts','Info','Company-Chart Accounts,Saving Chart Accounts,coAccountNameID'+$("#coAccountNameID").val());
					
					 var successText='<span style="color:Green;">Chart Account Details Updated.</span>';			
					 $("#chartsOfAccountsGrid").jqGrid('GridUnload');
					 loadChartsAccount();
					 $("#chartsOfAccountsGrid").trigger("reloadGrid");
					 document.getElementById("chartsDetailsFromID").reset(); 
					 $('#chartsDetailsFromID').find('input:checkbox').removeAttr('checked'); 
					 resetBalancevalues();
					 $( ".alertstatus").show();
					 $( "#alertstatusforaccount").html(successText);
					 setTimeout(function() {
			                $('#alertstatusforaccount').html("");
			                }, 3000);
					
				}
			
			});
			}
	return false;
}
}

function resetBalancevalues()
{
	 $("#periodDBID").html("$0.00 DB");
	 $("#periodDB1ID").html("$0.00 DB");
	 $("#periodDB2ID").html("$0.00 DB");
	 $("#periodCRID").html("$0.00 CR");
	 
	 $("#yearToDateDBID").html("$0.00 DB");
	 $("#yearToDateDB1ID").html("$0.00 DB");
	 $("#yearToDateDB2ID").html("$0.00 DB");
	 $("#yearToDateCRID").html("$0.00 CR");	
}

function leavezerowhenBlank(zerocount)
{
	var maxlength1=$("#numberNameID"+(zerocount)).attr("maxlength");
	
	if(maxlength1==1)
	$("#numberNameID"+(zerocount)).val("0");
	else if(maxlength1==2)
	$("#numberNameID"+(zerocount)).val("00");
	else if(maxlength1==3)
	$("#numberNameID"+(zerocount)).val("000");
	else if(maxlength1==4)
	$("#numberNameID"+(zerocount)).val("0000");
	else if(maxlength1==5)
	$("#numberNameID"+(zerocount)).val("00000");

}


function inactiveAccounts()
{
	console.log("I am Entered inactiveAccounts");
	var chartsDetails = $("#chartsDetailsFromID").serialize();
	

	$.ajax({
		url: "./company/updateChartAccount",
		type: "POST",
		data : chartsDetails,
		success: function(data) {
			//document.getElementById("chartsDetailsFromID").reset();			 
			 $("#chartsOfAccountsGrid").jqGrid('GridUnload');
			 loadChartsAccount();
			 $("#chartsOfAccountsGrid").trigger("reloadGrid");

		}
	});	

}

function openAddNewChartDialog(){
	/*document.getElementById("addNewChartFormID").reset();
	jQuery("#addNewChartDialog").dialog("open");	*/
	createtpusage('Company-Chart Accounts','Add Chart Accounts','Info','Company-Chart Accounts,Adding Chart Accounts');
	document.getElementById("chartsDetailsFromID").reset(); 

	$("#incomechkbox").attr("checked", false);
	$("#cogchkbox").attr("checked", false);
	$("#expchkbox").attr("checked", false);
	$("#cabs_caid").attr("checked", false);
	$("#cabs_faid").attr("checked", false);
	$("#cabs_oaid").attr("checked", false);
	$("#clbs_clid").attr("checked", false);
	$("#clbs_ltlid").attr("checked", false);
	$("#assetincludeonbsid").attr("checked", false);
	$("#equityincludeonbsid").attr("checked", false);
	
	resetBalancevalues();
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
//	var aChartDetais =  $("#chartsDetailsFromID").serialize();
	
	
	 if($("#typeAccount").val()=="Income")
		{
		if(!$("#incomechkbox").is(":checked") && !$("#cogchkbox").is(":checked"))
		{
		var errorText = "Please Choose Finacial Report Formatting.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
		}
	else if($("#typeAccount").val()=="Expense")
		{
		if(!$("#expchkbox").is(":checked"))
		{
		var errorText =  "Please Choose Finacial Report Formatting.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
		}
	 
	 var financialtype;
		var chartsDetails = $("#chartsDetailsFromID").serialize();
		
		if($("#typeAccount").val()=="Income")
			{
			if($("#incomechkbox").is(":checked"))
			financialtype = "1";
			else
			financialtype = "2";
			}
		else if($("#typeAccount").val()=="Expense")
			{
			financialtype = "3"
			}
		else
			{
			financialtype = "4";
			}
		
		chartsDetails = chartsDetails+"&financialtype="+financialtype;
	
	$.ajax({
		url: "./company/addNewChartAccount",
		type: "POST",
		data : chartsDetails,
		success: function(data) {
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart Account Added Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() 
										{  
										$(this).dialog("close"); 
										document.getElementById("addNewChartFormID").reset();
										
										 $("#chartsOfAccountsGrid").jqGrid('GridUnload');
										 loadChartsAccount();
										 $("#chartsOfAccountsGrid").trigger("reloadGrid");
										
										jQuery("#addNewChartDialog").dialog("close"); }}]}).dialog("open");*/
			
			if(data)
				{
			 var successText='<span style="color:Green;">Chart Account Added Sucessfully.</span>';			
			 $("#chartsOfAccountsGrid").jqGrid('GridUnload');
			 loadChartsAccount();
			 $("#chartsOfAccountsGrid").trigger("reloadGrid");
			 document.getElementById("chartsDetailsFromID").reset(); 
			 $('#chartsDetailsFromID').find('input:checkbox').removeAttr('checked'); 
			 resetBalancevalues();
			 $( ".alertstatus").show();
			 $( "#alertstatusforaccount").html(successText);
			 setTimeout(function() {
	                $('#alertstatusforaccount').html("");
	                }, 3000);
				}
			else
				{
				var errorText = "Account Number already exist.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				
				}
			
			
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
				var typeAccount = value.typeAccount;
				acc_number =number;
				var numberArray=number.split("-");
				for(var i=0;i<numberArray.length;i++)
				{
				console.log(numberArray[i]);
				$("#numberNameID"+(i+1)).val(numberArray[i]);
				
				if(numberArray[i]=="")
					{
					leavezerowhenBlank(i+1);			
					}
				selectedaccSegmentcount=i+1;
				}
				var segmentCount=parseInt($("#segmentCount").val())
			
				if(selectedaccSegmentcount<segmentCount)
				{
				for(var j=selectedaccSegmentcount;j<segmentCount;j++)
					{
					leavezerowhenBlank(j+1);			
					}
				}
				
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
				$("#typeAccount").val(typeAccount);
				$("#searchJob").val("");
			});
		}
	});
	
	$.ajax({
		url: "./company/getLedgerDetail",
		type: "GET",
		data : { 'accountID' : accountsID },
		success: function(data) {
			$.each(data, function(key, valueMap) {										
				
        		if("yearToDate"==key)
				{				
					$.each(valueMap, function(index, value){						
						var creditAmount = formatCurrency(value.credit);
						var debitAmount = formatCurrency(value.debit);
						var openAmount =  formatCurrency(value.bankOpeningBalance);
						var closeAmount = formatCurrency(parseInt(openAmount.replace('$',''))+parseInt(creditAmount.replace('$',''))-parseInt(debitAmount.replace('$','')));
						var crDr='';
						
						if(closeAmount.search("-") != -1) {
							crDr = 'DB';
							closeAmount = closeAmount.replace('-','');
							}
						else{
							crDr = 'CR';
						}
						
						$("#periodDBID").empty();
						$("#periodDBID").append(openAmount+" DB");
						$("#yearToDateDB1ID").empty();
						$("#yearToDateDB1ID").append(debitAmount+" DB");
						$("#yearToDateCRID").empty();
						$("#yearToDateCRID").append(creditAmount+" CR");
						$("#periodDB2ID").empty();
						$("#periodDB2ID").append(closeAmount+" "+crDr);
						
					
					}); 
					
				}
        		if("periodData"==key)
				{				
					$.each(valueMap, function(index, value){						
						var creditAmount = formatCurrency(value.credit);
						var debitAmount = formatCurrency(value.debit);
						var openAmount =  formatCurrency(value.bankOpeningBalance);
						var closeAmount = formatCurrency(parseInt(openAmount.replace('$',''))+parseInt(creditAmount.replace('$',''))-parseInt(debitAmount.replace('$','')));
						var crDr='';
						
						if(closeAmount.search("-") != -1) {
							crDr = 'DB';
							closeAmount = closeAmount.replace('-','');
							}
						else{
							crDr = 'CR';
						}
						$("#yearToDateDBID").empty();
						$("#yearToDateDBID").append(openAmount+" DB");
						$("#periodDB1ID").empty();
						$("#periodDB1ID").append(debitAmount+" DB");
						$("#periodCRID").empty();
						$("#periodCRID").append(creditAmount+" CR");
						$("#yearToDateDB2ID").empty();
						//$("#yearToDateDB2ID").append(formatCurrency(value.bankClosingBalance)+" CR");
						$("#yearToDateDB2ID").append(closeAmount+" "+crDr);
					}); 
					
				}
        		
				
			});
			
				
		
		}
	});
	return true;
}

function deleteAccountDetails(){
	var grid = $("#chartsOfAccountsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
//	if(rowId !== null){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Are sure you want to delete this account details?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:140, title:"Confirm Delete", 
			buttons:{
				"Delete   ": function(){
					/*var coAccountId = grid.jqGrid('getCell', rowId, 'coAccountId');
					var number = grid.jqGrid('getCell', rowId, 'number');*/
					var coAccountId=$("#coAccountNameID").val();
					createtpusage('Company-Chart Accounts','Delete Chart Accounts','Info','Company-Chart Accounts,Deleting Chart Accounts,coAccountId'+coAccountId);
					deleteChartOfAccount(coAccountId,acc_number); 
					
					jQuery(this).dialog("close");
					$("#chartsOfAccountsGrid").trigger("reloadGrid");
					setTimeout(function() {
		                $('#alertstatusforaccount').html("");
		                }, 3000);
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
	/*//}else{
		var errorText = "Please click one of the Account to Delete.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
			buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}*/
}

function deleteChartOfAccount(coAccountId,number){
	$.ajax({
		url: "./company/deleteAccount",
		type: "POST",
		data : {"coAccountID": coAccountId},
		success: function(data) {
			
			$( ".alertstatus").show();
			
			console.log(data);
			
			if(data == false) 	
				{
			jQuery(newDialogDiv).html('<span><b style="color:red;">You cannot delete this account.<br> Only you can inactive an account.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.", 
									buttons: [{height:29,text: 
										"Yes",click: function() {  
											$(this).dialog("close"); 											
										//	document.getElementById("chartsDetailsFromID").reset(); 
											$( "#inActiveNameID").prop( "checked", true );
											inactiveAccounts();																					
											$( "#alertstatusforaccount").html("Account inactivated.");
											
											$("#chartsOfAccountsGrid").trigger("reloadGrid"); 
											}											
											},
											{
											text:"No",click: function ()	{
											jQuery(this).dialog("close");}
												
											}]}).dialog("open");		
			
				}
			else
				{
				document.getElementById("chartsDetailsFromID").reset();
				 $("#chartsOfAccountsGrid").jqGrid('GridUnload');
				 loadChartsAccount();
				 $("#chartsOfAccountsGrid").trigger("reloadGrid");
				$( "#alertstatusforaccount").html("Account #"+number+" has been deleted successfully.");	
			
				}
		
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
			createtpusage('Company-Chart Accounts','Chart Accounts-Additional Save','Info','Company-Chart Accounts-Additional,Save');
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Chart Account Details Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
									buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); document.getElementById("chartsDetailsFromID").reset(); $('#chartsDetailsFromID').find('input:checkbox').removeAttr('checked');  $("#chartsOfAccountsGrid").trigger("reloadGrid"); }}]}).dialog("open");
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
