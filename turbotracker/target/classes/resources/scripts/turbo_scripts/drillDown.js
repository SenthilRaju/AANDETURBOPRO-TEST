var accID;
var period;
var year;
var accDesc;
var d ;

jQuery(document).ready(function(){
	$('#date').datepicker();
	//document.getElementById("DrillDownFormID").reset();
	$('#search').css('visibility','hidden');
	$(".tabs_main").tabs({
		cache: true,	
		ajaxOptions: {
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
	
	$( "#periodAndYearID" ).prop( "checked", true );
	$('#periodsId').prop("disabled", false);
	$('#periodsToID').prop("disabled", false);
	$('#mostRecentPeriodID').val("");
	$('#mostRecentPeriodID').prop("disabled", true);  
	$("#mostRecentPeriodTr").hide();
    $("#periodsTr").show();
    
    var yearOptions="";
    $.ajax({
        url: './drillDown/getYearList',
        type: 'POST',       
        success: function (data) {
        	yearOptions="<option value=''></option>";
			$.each(data, function(key, valueMap) {								
					if("yearList" == key)
					{
						$.each(valueMap, function(index, value){
							if(value.fiscalYear != null && value.fiscalYear.trim() != '')
								yearOptions+='<option value='+value.fiscalYear+'>'+value.fiscalYear+'</option>';
						}); 
					} 
			});
			// $("#LoadingImage").hide();
			$('#yearID').html(yearOptions);
			setTimeout($.unblockUI, 500);
        }
    });
    
});

function validatePeriod(){
	var periodID = $("#periodsId").val();
	var periodIDTO = $("#periodsToID").val();
	if(periodIDTO===''){
	$('#periodsToID option[value="' + periodID + '"]').prop('selected', true);
	}
}
function generatePeriodTo(){
	var periodID = $("#periodsId").val();
	var optionPeriodTo="<option value=''></option>";
	for(var st=periodID;st<=13;st++){
		optionPeriodTo+='<option value='+st+'>'+st+'</option>';
	}
	$("#periodsToID").html(optionPeriodTo);
}


function selectPeriods(){
    if( $('input[name=selectBy]:radio:checked').length > 0 ) {
        var selectedPeriod =  $('input[name=selectBy]:radio:checked').val();
       // alert(selectedPeriod);
        if(selectedPeriod==='p'){
        	$('#periodsId').prop("disabled", false);
        	$('#periodsToID').prop("disabled", false);
        	$('#mostRecentPeriodID').val("");
        	$('#mostRecentPeriodID').prop("disabled", true);  
        	$("#mostRecentPeriodTr").hide();
            $("#periodsTr").show();
            $("#yearID").val("");
            
         }
        if(selectedPeriod==='m'){
        	$('#periodsId').val("");
        	$('#periodsToID').val("");
        	$('#periodsId').prop("disabled", true);
        	$('#periodsToID').prop("disabled", true);
        	$('#mostRecentPeriodID').prop("disabled", false);
        	$("#mostRecentPeriodTr").show();
            $("#periodsTr").hide();
            
            getcoFiscalPerliodDate();
          
        	
        }
    }
}

function getcoFiscalPerliodDate()
{
	
	 $.ajax({
	 		url: "./getMostRecentPeriod",
	 		type: "GET",
	 		success: function(data) {
	 			
	 			var splitString = data.split("--");
	 			
					 $("#yearID").val(splitString[0]);
				    $("#mostRecentPeriodID").val(splitString[1]);
				
	 		}		 
	 });	 
}



function getAccountDetails(){
	validatePeriod();
	
	var accountsID = $("#accountsID").val();
	var periodsId = $("#periodsId").val();
	var periodsToID = $("#periodsToID").val();
	var yearID = $("#yearID").val();
	var mostRecentPeriodID = $("#mostRecentPeriodID").val();
		var coAccountsID = $("#coAccountsID").val();
	if($("#mostRecentPeriodID").val()!="")
		{
		periodsId =$("#mostRecentPeriodID").val();
		periodsToID =$("#mostRecentPeriodID").val();
		}
	
	createtpusage('Company-General Ledger','Get-Account','Info','Company-General Ledger,Get-Account,accountsID:'+accountsID+',coAccountsID:'+coAccountsID+',periodsId:'+periodsId);
	
	//alert("AccountID: "+accountsID+" periodsId: "+periodsId+" periodsToID: "+periodsToID+" yearID: "+yearID+" mostRecentPeriodID: "+mostRecentPeriodID);
	
	try{
		
		
		if(coAccountsID!="" && accountsID!="" && periodsId!="" && periodsToID!="" && yearID!="")
		{
		$("#jqGrid").empty();
		$("#jqGrid").append("<table id='drillDownGrid'></table><div id='drillDownPager'></div>");
		$("#drillDownGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:'./drillDown/accountDtails',
		pager: jQuery('#drillDownPager'),
		postData : {
			'accountNumber' : accountsID,
			'periodsId' : periodsId,
			'periodsToID' : periodsToID,
			'yearID' : yearID,
			'accDesc' : $("#accDesc").html(),
			'coAccountsID' : coAccountsID
			},
		
		colNames:['coAccountsID','Account','Account Description','Year','Period','Debit','Credit',
		          'Cumulative Balance','Period Close','YTD Close'
		          ],
		           
		          
		          colModel:[
		                    {name:'coAccountID',index:'coAccountID',hidden:true, align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			{name:'coAccountNumber',index:'coAccountNumber',hidden:false, align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			{name:'coAccountDesc',index:'coAccountDesc',hidden:false, width:516,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			{name:'year',index:'year',hidden:false, width:69,editable:true, align:'right', editrules:{required:false}, editoptions:{size:10}},
		        			{name:'period',index:'period',hidden:false, width:69,editable:true, align:'right', editrules:{required:false}, editoptions:{size:10}},
		        			{name:'debit',index:'debit',hidden:true, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'credit',index:'credit',hidden:true, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		           			{name:'cumulativebalance',index:'cumulativebalance',hidden:true, width:60,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			/*{name:'periodbalance',index:'periodbalance',hidden:false, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'ytdBalance',index:'ytdBalance',hidden:false, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter}
		           			*/
		           			{name:'pBalance',index:'pBalance',hidden:true, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:currencyFormatter},
		        			{name:'yBalance',index:'yBalance',hidden:false, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:currencyFormatter}
		           			
		        		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortname: 'period', sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Accounts',
		height:200,	width: 1050,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "ecSplitCodeID",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		 var rowData = jQuery(this).getRowData(rowId); 
    		 $("#ecSplitCodeHiddenID").val(rowData["ecSplitCodeID"]);
    		 $("#splittypedescription").val(rowData["codeName"]);
    		 $("#spltypdefaultper").val(rowData["defaultPct"]);
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
   		 	accID = rowData["coAccountNumber"];
   		 	coAccountsID = rowData["coAccountID"];
   		 	period= rowData["period"];
   		 	year = rowData["year"];
   		 	accDesc=rowData["coAccountDesc"]
   		 	getJournalBalance(accID,period,year,accDesc,coAccountsID);
   		
   		$('#drillDown_tabs').tabs({ selected: 1 });
		$('#drillDown_tabs').tabs({ active: 1 });
		}
	})/*.navGrid('#drillDownPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	}
	else
		{
		
		if(coAccountsID == "" && accountsID !="")
		{
			$("#errorStatus").html("Invalid Account. Please select from the list.");
			setTimeout(function() {
	            $('#errorStatus').html("");
	            }, 2000);
		}
		else{
		$("#errorStatus").html("Mandatory fields are required.");
		setTimeout(function() {
            $('#errorStatus').html("");
            }, 2000);
		}
		}
	
	}
	catch(err){
		console.log(err.message+"message about error");
}
	//return true;
}

function getJournalBalance(accID,period,year,accDesc,coAccountsID){
	console.log('Test'+ accID +'period'+period);
	$("#journalBalancejqGrid").empty();
	$("#journalBalancejqGrid").append("<table id='journalBalanceGrid'></table><div id='journalBalanceGridPager'></div>");
	$("#journalBalanceGrid").jqGrid({
		datatype: "json",
		mtype: "POST",
		url:'./drillDown/journalBalance',
		postData : {
			'accountID' : accID,
			'periodsId' :period,
			'yearID' : year,
			'accDesc':accDesc,
			'coAccountsID':coAccountsID
			},
			pager: jQuery('#journalBalanceGridPager'),
			
			colNames:['coAccountsID','Account','Account Description','Period','Year','Journal ID','Journal Description',
			          'Debit','Credit','Balance'
			          ],         
	   	colModel:[
	   	          			{name:'coAccountID',index:'coAccountID',hidden:true, align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
	   	                	{name:'coAccountNumber',index:'coAccountNumber',hidden:false, align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			{name:'coAccountDesc',index:'coAccountDesc',hidden:false, width:296,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			{name:'period',index:'period',hidden:false, width:55,editable:true,align:'right', editrules:{required:false}, editoptions:{size:10}},
		        			{name:'year',index:'year',hidden:false, width:55,editable:true,align:'right', editrules:{required:false}, editoptions:{size:10}},
		        			{name:'journalID',index:'journalID',hidden:false, width:81,editable:true, editrules:{required:false}, editoptions:{size:10}},
		           			{name:'journalDesc',index:'journalDesc',hidden:false, width:152,editable:true, editrules:{required:false}, editoptions:{size:10}},
		           		//	{name:'journalBalance',index:'journalBalance',hidden:false,align:'right', width:60,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter}
		        			{name:'debit',index:'debit',hidden:true, align:'right', width:120,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'credit',index:'credit',hidden:true, align:'right', width:120,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'balanceDesc',index:'balanceDesc',hidden:false, align:'right', width:150,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:currencyFormatter}
		        			
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		sortname: 'journalID', 
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Journal Balances',
		height:400,	width: 1050,
		/*scrollOffset:0,*/
		rownumbers:true,
		rownumWidth:34,
		footerrow: true,
	    userDataOnFooter : true,
		loadonce: false,
		loadComplete:function(data) {
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "ecSplitCodeID",
            userdata: "userdata"
    	},
    	 loadComplete: function () {
 			var allRowsInGrid = $('#journalBalanceGrid').jqGrid('getRowData');
 			var aVal = new Array(); 
 			var aTax = new Array();
 			
 			var sum = 0;
 			var sum1 = 0;
 			var balSum = 0;
 			var creditSum = 0;
 			var debitSum = 0;
 			var finalVal ='';
 			$.each(allRowsInGrid, function(index, value) {
 				//if(value.status==0){
 				aVal[index] = value.credit;
 				var number1 = aVal[index].replace("$", "");
 				var number2 = number1.replace(".00", "");
 				var number3 = number2.replace(",", "");
 				var number4 = number3.replace(",", "");
 				var number5 = number4.replace(",", "");
 				var number6 = number5.replace(",", "");
 				sum = Number(sum) + Number(number6); 
 				//}
 			});
 			$.each(allRowsInGrid, function(index, value) {
 				//if(value.status==0){
 				aVal[index] = value.debit;
 				var number1 = aVal[index].replace("$", "");
 				var number2 = number1.replace(".00", "");
 				var number3 = number2.replace(",", "");
 				var number4 = number3.replace(",", "");
 				var number5 = number4.replace(",", "");
 				var number6 = number5.replace(",", "");
 				sum1 = Number(sum1) + Number(number6); 
 				//}
 			});
 			$.each(allRowsInGrid, function(index, value) {
 				aVal[index] = value.balanceDesc;
 				if (aVal[index].indexOf("CR") >= 0){
	 				aVal[index] = value.balanceDesc;
	 				var newValue = aVal[index].substring(0,aVal[index].length-3); //string 800
	 				var number1 = newValue.replace("$", "");
	 				var number2 = number1.replace(".00", "");
	 				var number3 = number2.replace(",", "");
	 				var number4 = number3.replace(",", "");
	 				var number5 = number4.replace(",", "");
	 				var number6 = number5.replace(",", "");
	 				creditSum = Number(creditSum) + Number(number6); 
 				}else{
 	 				var newValue = aVal[index].substring(0,aVal[index].length-3); //string 800
 	 				var number1 = newValue.replace("$", "");
 	 				var number2 = number1.replace(".00", "");
 	 				var number3 = number2.replace(",", "");
 	 				var number4 = number3.replace(",", "");
 	 				var number5 = number4.replace(",", "");
 	 				var number6 = number5.replace(",", "");
 	 				debitSum = Number(debitSum) + Number(number6); 
 	 				
 				}
 			});
 			
 			balSum = debitSum-creditSum;
 			if(balSum>0){
 				finalVal = formatCurrency(balSum)+' DB'
 			}else{
 				finalVal = formatCurrency(balSum*-1)+' CR'
 			}
 			$('.footrow').css('color','#405466');
 			console.log("Total :: "+formatCurrency(balSum)+" CreditSum:: "+creditSum+" DebitSum:: "+debitSum);
 		    $(this).jqGrid('footerData','set',{journalDesc:'TOTAL',debit:formatCurrency(sum1), credit:formatCurrency(sum),balanceDesc:finalVal});
 			
 		},
    	onSelectRow: function(rowId){
    		 var rowData = jQuery(this).getRowData(rowId); 
    		 $("#ecSplitCodeHiddenID").val(rowData["ecSplitCodeID"]);
    		 $("#splittypedescription").val(rowData["codeName"]);
    		 $("#spltypdefaultper").val(rowData["defaultPct"]);
    	},
    	ondblClickRow: function(rowId) {
    		
    		var rowData = jQuery(this).getRowData(rowId); 
    		
    		var coAccountsID =  rowData["coAccountID"];
    		var accNo =  rowData["coAccountNumber"];
    		var accDesc = rowData["coAccountDesc"];
    		var period = rowData["period"];
    		var yearID =  rowData["year"];
    		var journalID = rowData["journalID"];
    		
    		//var rowData = jQuery(this).getRowData(rowId); 
   		 	
   		 	getJournalDetails(accNo,accDesc,period,yearID,journalID,coAccountsID);
   		
   		 	$('#drillDown_tabs').tabs({ selected: 2 });
   		 	$('#drillDown_tabs').tabs({ active: 2 });  		
		}
	})/*.navGrid('#drillDownPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	return true;
}

function currencyFormatter(cellValue, options, rowObject){
	//alert(cellValue);
	var isCrDr='';
	isCrDr=cellValue.substr(-2); // CR or DR
	//alert(isCrDr);
	return formatCustomCurrency(cellValue)+' '+isCrDr;
}

function formatCustomCurrency(strValue)
{
	if(strValue === "" || strValue == null){
		return "$0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);
	//console.log('dblValue::'+dblValue);
	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = 	Math.floor(dblValue*100).toFixed(2);
	//console.log('dblValue##2::'+dblValue);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	//console.log('intCents -2::'+intCents);
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}


function getJournalDetails(accID,accDesc,period,yearID,journalID,coAccountsID){
	$("#journalDetailjqGrid").empty();
	$("#journalDetailjqGrid").append("<table id='journalDetailGrid'></table><div id='journalDetailGridPager'></div>");
	var sumVal=0;
	
	var sum = 0;
	var sum1 = 0;
	$("#journalDetailGrid").jqGrid({
		datatype: "json",
		mtype: "GET",
		url:'./drillDown/journalDetails',
		postData : {
			'accountID' : accID,
			'accountDesc' : accDesc,
			'period' : period,
			'yearID' : yearID,
			'journalID' : journalID,
			'coAccountsID' : coAccountsID
			},
		pager: jQuery('#journalDetailGridPager'),
		
		colNames:['coAccountsID','Account','Account Description','Period','Year','Journal ID','Source',
		          'Description','EntryDate','Date' ,'Transaction Number', 'Debit' , 'Credit'
		          ],
		          
	   	colModel:[
	   	          			{name:'coAccountID',index:'coAccountID',hidden:true, align:'left', width:91,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
	   	          			{name:'coAccountNumber',index:'coAccountNumber',hidden:false, align:'left', width:91,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'coAccountDesc',index:'coAccountDesc',hidden:false, width:240,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'period',index:'period',hidden:false, width:53,editable:true,align:'right', editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'year',index:'year',hidden:false, width:53,editable:true,align:'right', editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'journalID',index:'journalID',hidden:false, width:51,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'sourceid',index:'sourceid',align:'right',hidden:true, width:60,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'description',index:'description',hidden:false, width:140,editable:true, editrules:{required:false}, editoptions:{size:10},sortable:false},
		        			{name:'entrydate',index:'entrydate',align:'center',hidden:true, width:80,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d H:i:s',newformat:'m/d/Y'},sortable:false},
		        			{name:'transactionDate',index:'transactionDate',align:'center', hidden:false, width:96,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d H:i:s',newformat:'m/d/Y'}},
		        			{name:'gltransactionid',index:'gltransactionid',align:'right',hidden:false, width:54,editable:true, editrules:{required:false}, editoptions:{size:10}},
		        			//{name:'amount',index:'amount',hidden:false,align:'right', width:60,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'debit',index:'debit',hidden:false, align:'right', width:110,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
		        			{name:'credit',index:'credit',hidden:false, align:'right', width:110,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter}
		],
		rowNum: 20,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfTermsGridPager',
		rowList: [20,50,100,250,500,1000,2000],
		viewrecords: true,
		pgbuttons: true,
		sortname: 'entrydate', 
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Journal Details',
		height:400,	width: 1050,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		/*loadComplete:function(data) {
			 //$("#journalDetailGrid").jqGrid('footerData' , 'set' , {name:'TOTAL:' , amount: calculateTotal($("#journalDetailGrid") , 'Amount')});
			
		},*/
		footerrow: true,
	    userDataOnFooter : true,
		loadComplete: function () {
			var allRowsInGrid = $('#journalDetailGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.credit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.debit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum1 = Number(sum1) + Number(number6); 
			});
			
			
			console.log("Tot :: "+sum+"----");
			//alert(${requestScope.journalTotal});
			//sum = $("#journalTotalID").val();
		   
		},
		gridComplete :function(data) {
			
			$.ajax({
				url:'./drillDown/journalDetailsSum',
				type : "GET",
				data : {
					'accountID' : accID,
					'accountDesc' : accDesc,
					'period' : period,
					'yearID' : yearID,
					'journalID' : journalID,
					'coAccountsID' : coAccountsID
				},
				success : function(data) {
					console.log(data.debit+' '+data.credit);
					$('.footrow').css('color','#405466');
					 $('#journalDetailGrid').jqGrid('footerData','set',{gltransactionid:'TOTAL',debit:data.debit, credit:data.credit});
					
				}
			});
			
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "ecSplitCodeID",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		 var rowData = jQuery(this).getRowData(rowId); 
    		 $("#ecSplitCodeHiddenID").val(rowData["ecSplitCodeID"]);
    		 $("#splittypedescription").val(rowData["codeName"]);
    		 $("#spltypdefaultper").val(rowData["defaultPct"]);
    	},
    	ondblClickRow: function(rowId) {
    		var rowData = jQuery(this).getRowData(rowId); 
    		
   		 	var transactionReferenceID = rowData["gltransactionid"];   		 
   		 	var description = rowData["description"];
   		 	var sourceid = rowData["sourceid"];
   		 
    		getTransactionDetails(transactionReferenceID,description,sourceid);
    		$('#drillDown_tabs').tabs({ selected: 3 });
   		 	$('#drillDown_tabs').tabs({ active: 3 }); 
    		
		}
	})/*.navGrid('#drillDownPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	
  /*  calculateTotal = function() {
        var totalAmount = 0,
            iAmount=getColumnIndexByName(grid,'Amount');
        var i=0, rows = grid[0].rows, rowsCount = rows.length, row, status;
        for(;i<rowsCount;i++) {
            row = rows[i];
            if (row.className.indexOf('jqgrow') !== -1) {
                    totalAmount += Number(getTextFromCell(row.cells[iAmount]));
            }
        }
        $("#journalDetailGrid").jqGrid('footerData','set',{name:'TOTAL ',Amount:totalAmount});
    };*/
	return true;
}

function getTransactionDetails(transactionReferenceID,description,sourceid){
	$("#transactionDetailjqGrid").empty();
	$("#transactionDetailjqGrid").append("<table id='transactionDetailGrid'></table><div id='transactionDetailGridPager'></div>");
	$("#transactionDetailGrid").jqGrid({
		datatype: "json",
		mtype: "GET",
		url:'./drillDown/transactionDetails',
		postData : {
			'transactionReferenceID' : transactionReferenceID,
			'description' : description,
			'sourceid' :sourceid
			},
			pager: jQuery('#transactionDetailGridPager'),
			
			colNames:['Account','Account Description','Journal ID','Source',
			          'Description','EntryDate','Date', 'Debit','Credit' 
			          ],
	   	colModel:[
	   	          
				{name:'coAccountNumber',index:'coAccountNumber',hidden:false, align:'left', width:96,editable:true, editrules:{required:false}, editoptions:{size:10}},
				{name:'coAccountDesc',index:'coAccountDesc',hidden:false, width:304,editable:true, editrules:{required:false}, editoptions:{size:10}},
				{name:'journalID',index:'journalID',hidden:false, width:78,editable:true, editrules:{required:false}, editoptions:{size:10}},
				{name:'sourceid',index:'sourceid',align:'right',hidden:true, width:60,editable:true, editrules:{required:false}, editoptions:{size:10}},
				{name:'description',index:'description',hidden:false, width:140,editable:true, editrules:{required:false}, editoptions:{size:10}},
				//{name:'amount',index:'amount',hidden:false,align:'right', width:60,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
				{name:'entrydate',index:'entrydate',align:'center', hidden:true, width:96,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d H:i:s',newformat:'m/d/Y'}},
				{name:'transactionDate',index:'transactionDate',align:'center', hidden:false, width:96,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d H:i:s',newformat:'m/d/Y'}},
				{name:'debit',index:'debit',hidden:false, align:'right', width:128,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
    			{name:'credit',index:'credit',hidden:false, align:'right', width:128,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
				
					
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
		caption: 'Transaction Details',
		height:400,	width: 1050,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		/*loadComplete:function(data) {
			 //$("#journalDetailGrid").jqGrid('footerData' , 'set' , {name:'TOTAL:' , amount: calculateTotal($("#journalDetailGrid") , 'Amount')});
			
		},*/
		loadComplete: function () {
			var rowData = jQuery(this).getRowData(1); 
   		 //	var transactionReferenceID = rowData["transactionReferenceID"];
   		 	$("#transNoId").text(transactionReferenceID);
		},
	    jsonReader : {
            root: "rows",
            page: "page",
            total: "total",
            records: "records",
            repeatitems: false,
            cell: "cell",
            id: "ecSplitCodeID",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		 var rowData = jQuery(this).getRowData(rowId); 
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#drillDownPager',
		{add:false,edit:false,del:false,refresh:false,search:false});*/
	
  /*  calculateTotal = function() {
        var totalAmount = 0,
            iAmount=getColumnIndexByName(grid,'Amount');
        var i=0, rows = grid[0].rows, rowsCount = rows.length, row, status;
        for(;i<rowsCount;i++) {
            row = rows[i];
            if (row.className.indexOf('jqgrow') !== -1) {
                    totalAmount += Number(getTextFromCell(row.cells[iAmount]));
            }
        }
        $("#journalDetailGrid").jqGrid('footerData','set',{name:'TOTAL ',Amount:totalAmount});
    };*/
	return true;
}