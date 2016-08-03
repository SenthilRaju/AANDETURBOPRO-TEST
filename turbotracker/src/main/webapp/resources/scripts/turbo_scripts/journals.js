var aChartDetais = '';
var headerDetails='';
var accountID = '';
var customjournalbutton=false;
var fiscalPeriodDate='';
var oldformSerialize='';
var glacctglobalrowid;
var oldjeformSerialize='';
var oldgridaccountgriddata='';
var opervalue='';
var editiconsshow=true;
var _globalperiodid;
var _globalyearid;
var _globaloldGrid=true;
var journalDetailrowid;

jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	loadJournalsGrid();
	loadJournalDetailsGrid(-1);
	//loadjournaldetailshistoryGrid(-1);
	$('#date').datepicker();
	$("#DateOfJournal").prop("readonly",true);
	//Added by Jenith on 2014-10-06
	var pickerOpts = {
				//minDate: new Date(),maxDate: "+10" 	//from current date to 10 days
				minDate: 0						 		//For Only Future date
				//minDate: "-3M", maxDate: "+3D" 		//For Limited Periods of date 
				
		};
	$("#DateOfJournal").datepicker(); // Added by Jenith on 2014-10-06 For Only Future date 
	$('#DateOfJournal').val(new Date());
	$('#decriptionNameID').val('');
	$('#References').val('');
	$('#headerid').val('');
	$('#fisicalperiodid').val('');
	$('#coLedgerSourceId').val('');
	//$('#DateOfJournal').focus();
	$.ajax({
		url: "./journalscontroller/getRreferenceNo",
		type: "POST",
		success: function(data) {
			console.log(data);
			$('#References').val(data);
		}
	});
	
	$("#JournalDetailsGrid_iladd").click(function() {
		aChartDetais = $("#addNewjournalFormID").serialize();
		
	});
	
	 $("#JournalDetailsGrid_ilsave").click(function() {
		 
			$("#info_dialog").css("z-index", "10000");
				$("#JournalDetailsGrid").jqGrid('resetSelection');
				var rowcount=$("#JournalDetailsGrid").getGridParam("reccount");
				rowcount="glacc_"+rowcount;
				//$("#" + glacctglobalrowid).attr("id", rowcount);
				
				
				var ids = $("#JournalDetailsGrid").jqGrid('getDataIDs');
				
				//alert(ids.length);
				var glaccrrowid;
				
				if(ids.length==1){
					glaccrrowid = 0;
				}else{
					 glaccrrowid= ids[1];
					 
				}
					
				$("#" + glacctglobalrowid).attr("id", Number(glaccrrowid)+1);
			
		});
	
	
	
	
	
	$.ajax({
		 
 		url: "./banking/getcoFiscalPeriod",
 		type: "GET",
 		success: function(data) {
	 		fiscalPeriodDate = new Date(data.startDate);
	 		
 		}		 
 });
	
	
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
	/*$("#JournalDetailsGrid_ilcancel").click(function() {
		setloadjournaldetailgrid();
	});*/
	
});

var newDialogDiv = jQuery(document.createElement('div'));
function loadJournalsGrid(){
	$("#JournalsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./journalscontroller/getListOfJournals',
		pager: jQuery('#JournalsGridPager'),
		colNames:['Description','Ref','Posted Date','glTransactionId', 'coFiscalPeriodId', 'period', 'pStartDate', 'pEndDate', 'coFiscalYearId', 'yStartDate', 
		          'yEndDate','journalId','journalDesc','coAccountId','coAccountNumber','fyear','credit','debit'],
	   	colModel:[
		   	        {name:'transactionDesc',index:'transactionDesc', width:130,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:false},
		   	        {name:'poNumber',index:'poNumber', width:40,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:false},
		   	        {name:'entrydate',index:'entrydate', width:70,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
		   	        {name:'glTransactionId',index:'glTransactionId', width:100,editable:true, editrules:{required:true},hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'coFiscalPeriodId',index:'coFiscalPeriodId',align:'center', width:100,editable:true, editrules:{required:false}, editoptions:{size:10},hidden:true},
					{name:'period',index:'period', width:100,editable:true, editrules:{required:true}, editoptions:{size:10},hidden:true},
					{name:'pStartDate',index:'pStartDate', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
					{name:'pEndDate',index:'pEndDate', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
					{name:'coFiscalYearId',index:'coFiscalYearId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'yStartDate',index:'yStartDate', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
					{name:'yEndDate',index:'yEndDate', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'journalId',index:'journalId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'journalDesc',index:'journalDesc', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'coAccountId',index:'coAccountId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'coAccountNumber',index:'coAccountNumber', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'fyear',index:'fyear', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'credit',index:'credit', width:60,editable:true,align:'right', editrules:{required:true}, editoptions:{size:10}, hidden:true},
					{name:'debit',index:'debit', width:60,editable:true,align:'right', editrules:{required:true}, editoptions:{size:10}, hidden:true}
		],
		rowNum: 200,	
		pgbuttons: true,	
		recordtext: '',
		rowList: [200,500, 1000],
		viewrecords: true,
		pager: '#JournalsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Journals',
		height:446,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
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
            id: "id",
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		document.getElementById("chartsDetailsFromID").reset();
    		buttontrigger();
    		editiconsshow=false;
    		loadJournalRowDetails(rowId);
    		var selrowid=$("#JournalsGrid").jqGrid('getGridParam', 'selrow');
    		var ref=$("#JournalsGrid").jqGrid('getCell', selrowid, 'poNumber');
    		_globalperiodid=$("#JournalsGrid").jqGrid('getCell', selrowid, 'coFiscalPeriodId');
    		_globalyearid=$("#JournalsGrid").jqGrid('getCell', selrowid, 'coFiscalYearId');
    		$("#JournaldetailshistoryGrid").jqGrid('GridUnload');
    		loadjournaldetailshistoryGrid(ref);
    		oldformSerialize = $("#chartsDetailsDiv :input").serialize();
    		_globaloldGrid=true;

    	},
    	ondblClickRow: function(rowId) {
    		document.getElementById("chartsDetailsFromID").reset();
    		buttontrigger();
    		editiconsshow=false;
    		loadJournalRowDetails(rowId);
    		var selrowid=$("#JournalsGrid").jqGrid('getGridParam', 'selrow');
    		var ref=$("#JournalsGrid").jqGrid('getCell', selrowid, 'poNumber');
    		$("#JournaldetailshistoryGrid").jqGrid('GridUnload');
    		loadjournaldetailshistoryGrid(ref);
    		oldformSerialize = $("#chartsDetailsDiv :input").serialize();
    		
		}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function buttontrigger(){
	//journalnewsave journaleditbutton journaleditsave journaldelete
	$("#journalnewsave").css({ display: "none" });
	$("#journaleditsave").css({ display: "none" });

	$("#journaleditbutton").css({ display: "inline-block" });
	$("#journaldelete").css({ display: "none" });
}
function editbuttontrigger(){
	//journalnewsave journaleditbutton journaleditsave journaldelete
	var selrowid=$("#JournalsGrid").jqGrid('getGridParam', 'selrow');
	var ref=$("#JournalsGrid").jqGrid('getCell', selrowid, 'poNumber');
	$("#JournalDetailsGrid").jqGrid('GridUnload');
	editiconsshow=true;
	loadJournalDetailsGrid(ref);
	$("#journalnewsave").css({ display: "none" });
	$("#journaleditsave").css({ display: "inline-block" });

	$("#journaleditbutton").css({ display: "none" });
	$("#journaldelete").css({ display: "inline-block" });
}
function loadjournaldetailshistoryGrid(coLedgerHeaderId){
	
	$("#JournaldetailshistoryGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./journalscontroller/getListOfJournalshistory',
		postData: {'reference':coLedgerHeaderId },
		//pager: jQuery('#chartsOfAccountsGridPager'),
		colNames:['Description','CreatedDate','Reason','Reference', 'EditorDelete'],
	   	colModel:[
		   	        {name:'journalentryhistoryID',index:'journalentryhistoryID', width:130,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
		   	        {name:'createddate',index:'createddate', width:40,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
		   	        {name:'reasondesc',index:'reasondesc', width:70,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
		   	        {name:'reference',index:'reference',align:'center', width:100,editable:true, editrules:{required:false}, editoptions:{size:10},hidden:true},
					{name:'editordelete',index:'editordelete',align:'center', width:100,editable:true, editrules:{required:false}, editoptions:{size:10},hidden:true}
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
		caption: 'Journal History',
		height:80,	width: 350,/*scrollOffset:0,*/rownumbers:false,rownumWidth:34,
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
    		
    	},
    	ondblClickRow: function(rowId) {
    		
    		
		}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}

function addNewJournalEntry(){
	
	$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
	
	$("#journalnewsave").css({ display: "inline-block" });
	$("#journaleditsave").css({ display: "none" });
	

	$("#journaleditbutton").css({ display: "none" });
	$("#journaldelete").css({ display: "none" });
	
	$("#JournaldetailshistoryGrid").jqGrid('GridUnload');
	$("#JournalDetailsGrid").jqGrid('GridUnload');
	editiconsshow=true;
	loadJournalDetailsGrid(-1); 
	$("#JournalDetailsGrid").trigger("reloadGrid");
	$('#DateOfJournal').val('');
	$('#decriptionNameID').val('');
	$('#References').val('');
	$('#headerid').val('');
	$('#referenceID').val('');
	$('#fisicalperiodid').val('');
	$('#coLedgerSourceId').val('');
	//$('#DateOfJournal').focus();
	$.ajax({
		url: "./journalscontroller/getRreferenceNo",
		type: "POST",
		success: function(data) {
			 createtpusage('Company-Journal Entries','Add Journal','Info','Company-Journal Entries,Adding Journal,References:'+data);
			console.log(data);
			$('#References').val(data);
		}
	});
	
}

function setloadjournaldetailgrid(){
	var ids = $("#JournalDetailsGrid").jqGrid('getDataIDs');
	var creditamt=0;
	 var debitamt=0;
	for(var i=0;i<ids.length;i++){
		 var selectedRowId=ids[i];
		 var credit =$("#JournalDetailsGrid").jqGrid ('getCell', selectedRowId, 'credit');
		 var debit=$("#JournalDetailsGrid").jqGrid ('getCell', selectedRowId, 'debit');
		 creditamt=parseFloat(creditamt)+parseFloat(credit);
		 debitamt=parseFloat(debitamt)+parseFloat(debit)
	 }
	$('#JournalDetailsGrid').jqGrid('footerData','set',{credit:creditamt, debit:debitamt}); 
}

function voidImage(cellValue, options, rowObject){
	var element = '';
	if(cellValue==1){
		element = "<img src='./../resources/images/delete.png' title='Cancelled Transaction'  style='padding: 2px;'>";	
	}else{
		element="";
	}
   return element;
} 
function loadJournalDetailsGrid(coLedgerHeaderId) {
	console.log("coLedgerHeaderId"+coLedgerHeaderId);
	
	
	$("#JournalDetailsGrid").jqGrid({
		datatype: 'JSON',
		postData: {'coLedgerHeaderId':coLedgerHeaderId },
		mtype: 'POST',
		url: './journalscontroller/getListOfJournalDetails',
		pager:jQuery("#JournalDetailsGridpager"),

		colNames:['','hiddenstatus','Account Number','Description','TransDescription','Ref','Posted Date','glTransactionId', 'coFiscalPeriodId', 'period', 'pStartDate', 'pEndDate', 'coFiscalYearId', 'yStartDate', 
		          'yEndDate','journalId','journalDesc','coAccountId','fyear','Debit','Credit','oldDebit','oldCredit','oldcoaccountid','oldcoaccountnumber'],
	   	colModel:[
	   	          	{name:'status',index:'status', width:10,editable:false, editrules:{required:true}, editoptions:{}, hidden:false,formatter: voidImage},
	   	          	{name:'hiddenstatus',index:'hiddenstatus', width:10,editable:false, editrules:{required:true}, editoptions:{}, hidden:true},
	   	          	{name:'coAccountNumber',index:'coAccountNumber', width:60,editable:true, hidden:false,edittype : 'text',
	   	 			editoptions : {
	   					dataInit : function(elem) {
	   						$(elem).autocomplete(
	   										{
	   											source : 'journalscontroller/getAccountDetails',
	   											minLength : 2,
	   											select : function(event, ui) {
	   												var selrowid=$("#JournalDetailsGrid").jqGrid('getGridParam', 'selrow');
	   												 accountID = ui.item.id;
	   												var accountDescription = ui.item.accountDescription;
	   												console.log(accountID+' - '+accountDescription);
	   												$("#"+selrowid+"_coAccountDesc").val(accountDescription);
	   												$("#"+selrowid+"_coAccountId").val(accountID);
	   												//new_row_coAccountNumber
	   												headerDetails="accountId="+accountID+"&DateOfJournal="+$('#DateOfJournal').val()+"&References="+$('#References').val()+"&decriptionNameID="+$('#decriptionNameID').val()+"&headerid="+$('#headerid').val();
	   												//alert(headerDetails);
	   												var editurlname= "./journalscontroller/updateData?"+headerDetails;
	   												//$("#JournalDetailsGrid").jqGrid('setGridParam',{editurl:editurlname});
	   											 $("#"+selrowid+"_coAccountDesc").focus();
	   											}
	   										});
	   					},
	   	 		 dataEvents: [
	   	 		              	{ type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
	   	 		              	{ type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
		                        {
		                         type: 'blur',
		                         fn: function(e) {
		                        	 var selrowid=$("#JournalDetailsGrid").jqGrid('getGridParam', 'selrow');
		                        	 
		                        	 $("#"+selrowid+"_coAccountDesc").focus();
		                         }
		                        }
		                        
		                        
		                       ]	},
	   				
	   				editrules : {
	   					edithidden : false,
	   					required : true
	   				}},
	   	            {name:'coAccountDesc',index:'coAccountDesc', width:130,editable:true, editrules:{required:true}, editoptions:{
	   	            	dataEvents: [
	   	            	             { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
	 	   	 		              	{ type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } }]
	   	            }, hidden:false},
	   	           	{name:'transactionDesc',index:'transactionDesc', width:130,editable:true, editrules:{required:false}, editoptions:{}, hidden:true},
		   	        {name:'poNumber',index:'poNumber', width:40,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
		   	        {name:'entrydate',index:'entrydate', width:70,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:10},formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
		   	        {name:'glTransactionId',index:'glTransactionId', width:100,editable:true, editrules:{required:false},hidden:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
					{name:'coFiscalPeriodId',index:'coFiscalPeriodId',align:'center', width:100,editable:false, editrules:{required:false}, editoptions:{size:10},hidden:true},
					{name:'period',index:'period', width:100,editable:true, editrules:{required:false}, editoptions:{size:10},hidden:true},
					{name:'pStartDate',index:'pStartDate', width:60,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:10}},
					{name:'pEndDate',index:'pEndDate', width:60,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:10}},
					{name:'coFiscalYearId',index:'coFiscalYearId', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'yStartDate',index:'yStartDate', width:60,editable:true, editrules:{required:false}, hidden:true, editoptions:{size:10}, hidden:true},
					{name:'yEndDate',index:'yEndDate', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'journalId',index:'journalId', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'journalDesc',index:'journalDesc', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'coAccountId',index:'coAccountId', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'fyear',index:'fyear', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
					{name:'debit',index:'debit', width:60,editable:true,align:'right', editrules:{required:true}, editoptions:{size:10,
						 dataEvents: [
						              { type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
						              { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
				                        {
				                         type: 'keyup',
				                         fn: function(e) {
				                        	 var selrowid=$("#JournalDetailsGrid").jqGrid('getGridParam', 'selrow');
				                        	 
				                        	 $("#"+selrowid+"_credit").val('0.00');
				                    		 if (this.value.match(/[^0-9 .]/g)) {
				                                 this.value = this.value.replace(/[^0-9 .]/g, '');
				                             }
				                    		 
				                    		
				                         }
				                        }
				                        
				                        
				                       ]
						
					
					}, hidden:false,formatter:customCurrencyFormatter},
					{name:'credit',index:'credit', width:60,editable:true,align:'right', editrules:{required:true},
						editoptions:{
							 dataEvents: [
							              	{ type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },
						 	   	 		    { type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },
					                        {
					                         type: 'keyup',
					                         fn: function(e) {
					                        	 var selrowid=$("#JournalDetailsGrid").jqGrid('getGridParam', 'selrow');
					                        	 
					                        	 $("#"+selrowid+"_debit").val('0.00');
					                    		 if (this.value.match(/[^0-9 .]/g)) {
					                                 this.value = this.value.replace(/[^0-9 .]/g, '');
					                             }
					                         }
					                        }
					                        
					                        
					                       ]
							
							/*alignText:'left',
							 dataEvents: [
					                        {
					                         type: 'keyup',
					                         fn: function(e) {
					                        	 //setloadjournaldetailgrid();
					                         }
					                        },
					                         {
				                                    type: 'change',
				                                    fn: function (e) {
				                                    	//$("#JournalDetailsGrid").jqGrid('resetSelection');
				                                        var row = $(e.target).closest('tr.jqgrow');
				                                        var rowId = row.attr('id');
				                                        jQuery("#JournalDetailsGrid").saveRow(rowId, false, 'clientArray');
				                                    }
					                         }
					                        
					                        
					                        
					                       ]	*/
						
						}	, hidden:false,formatter:customCurrencyFormatter},
						{name:'olddebit',index:'olddebit', width:60,editable:true, editrules:{required:false}, editoptions:{}, hidden:true,formatter:customCurrencyFormatter},
						{name:'oldcredit',index:'oldcredit', width:60,editable:true, editrules:{required:false},editoptions:{alignText:'left'}	, hidden:true,formatter:customCurrencyFormatter},
						{name:'oldcoAccountId',index:'oldcoAccountId', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true},
						{name:'oldcoAccountNumber',index:'oldcoAccountNumber', width:60,editable:true, editrules:{required:false}, editoptions:{size:10}, hidden:true}
						
						],
		 altRows:true,
		 altclass:'myAltRowClass',
		 cellsubmit: 'clientArray',
		 editurl: 'clientArray',
		 height:200,
		 imgpath:'themes/basic/images',
		 rowNum: 0,
		 sortname:' ',
		 sortorder:"asc",
		 pgbuttons: false,
		 recordtext:'',
		 rowList:[],
		 pgtext: null,
		 rownumbers:true,
	 	 width:790,
	 	 footerrow: true,
	     userDataOnFooter : true,
	     viewrecords: false,
	     loadonce: false,
	     cellEdit: false,
	     gridComplete: function () {
	    	 if(_globaloldGrid){
	    		 oldgridaccountgriddata=  $('#JournalDetailsGrid').getRowData();
	    		 _globaloldGrid=false;
	    	 }
	    	
	     },
	     loadComplete: function () {
			//$("#JournalsGrid").setSelection(1, true);
			var allRowsInGrid = $('#JournalDetailsGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			
			var sum = 0;
			var sum1 = 0;
			$.each(allRowsInGrid, function(index, value) {
				if(value.status==0){
				aVal[index] = value.credit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
				}
			});
			$.each(allRowsInGrid, function(index, value) {
				if(value.status==0){
				aVal[index] = value.debit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum1 = Number(sum1) + Number(number6); 
				}
			});
			
			$('.footrow').css('color','#1A74FB');
			console.log("Tot :: "+sum+"----");
		    $(this).jqGrid('footerData','set',{credit:formatCurrency(sum), debit:formatCurrency(sum1)});
			
		},
		onSelectRow : function(id) {
			glacctglobalrowid=id;
			journalDetailrowid=id;
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
    	}
	}).navGrid("#JournalDetailsGridpager", {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : false,
		pager : false,
	});
	console.log("edit"+editiconsshow);
if(editiconsshow){
	$("#JournalDetailsGrid").jqGrid(
			'inlineNav',
			'#JournalDetailsGridpager',
			{
				edit : true,
				edittext : "Edit",
				add : true,
				addtext : "Add",
				cancel : false,
				savetext : "Save",
				refresh : true,
				alertzIndex : 10000,
				addParams : {
					addRowParams : {
								keys : false,
								oneditfunc : function() {
									var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
									$.ajax({
										url: "./checkAccountingCyclePeriods",
										data:{"datetoCheck":$('#DateOfJournal').val(),"UserStatus":checkpermission},
										type: "POST",
										success: function(data) { 
											if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
											{
												
												aChartDetais = $("#addNewjournalFormID").serialize();
												_globalperiodid=data.cofiscalperiod.coFiscalPeriodId;
												_globalyearid = data.cofiscalperiod.coFiscalYearId;
												
											}
											else
												{
												
												if(data.AuthStatus == "granted")
												{	
												$("#JournalDetailsGrid").trigger('reloadGrid');	
												var newDialogDiv = jQuery(document.createElement('div'));
												jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
												jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
																		buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
																	}).dialog("open");
												}
												else
												{
													$("#JournalDetailsGrid").trigger('reloadGrid');
													showDeniedPopup();
												}
												}
									  	},
							   			error:function(data){
							   				console.log('error');
							   				}
							   			});
									
									
									console.log("edited");
									$("#new_row_coAccountNumber").focus();
								
								},
								successfunc : function(response) {
									console.log(response);
									return true;
								},
								aftersavefunc : function(response) {
									var ids = $("#JournalDetailsGrid").jqGrid('getDataIDs');
									var jeaccrrowid;
									if(ids.length==1){
										jeaccrrowid = 0;
									}else{
										var idd = jQuery("#JournalDetailsGrid tr").length;
										for(var i=0;i<ids.length;i++){
											if(idd<ids[i]){
												idd=ids[i];
											}
										}
										jeaccrrowid= idd;
									}
									if(journalDetailrowid=="new_row"){
									$("#" + journalDetailrowid).attr("id", Number(jeaccrrowid)+1);
									}
									
									var grid=$("#JournalDetailsGrid");
									grid.jqGrid('resetSelection');
								    var dataids = grid.getDataIDs();
								    for (var i=0, il=dataids.length; i < il; i++) {
								        grid.jqGrid('setSelection',dataids[i], false);
								    }
								    
									console.log("addParams-Aftersave: "+response);
									//formatCurrency(sum)
								},
								errorfunc : function(rowid, response) {
									return false;
								},
								afterrestorefunc : function(rowid) {
									// alert("afterrestorefunc");
								}
							}
						},
				editParams : {
					keys : false,
					refresh : true,
					successfunc : function(response) {
							console.log(response.responseText);
							return true;
								},
					aftersavefunc : function(id) {

						var ids = $("#JournalDetailsGrid").jqGrid('getDataIDs');
						var jeaccrrowid;
						if(ids.length==1){
							jeaccrrowid = 0;
						}else{
							var idd = jQuery("#JournalDetailsGrid tr").length;
							for(var i=0;i<ids.length;i++){
								if(idd<ids[i]){
									idd=ids[i];
								}
							}
							jeaccrrowid= idd;
						}
						if(journalDetailrowid=="new_row"){
						$("#" + journalDetailrowid).attr("id", Number(jeaccrrowid)+1);
						}
						
						
							console.log("editParams-Aftersave: "+id);
							//$("#JournalDetailsGrid").setGridParam().trigger('reloadGrid');
							var allRowsInGrid = $('#JournalDetailsGrid').jqGrid('getRowData');
					 		var aVal = new Array(); 
					 		var aTax = new Array();
					 		var sum = 0;
					 		var sum1 = 0;
					 		$.each(allRowsInGrid, function(index, value) {
					 			if(value.status==0){
					 			aVal[index] = value.credit;
					 			var number1 = aVal[index].replace("$", "");
					 			var number2 = number1.replace(".00", "");
					 			var number3 = number2.replace(",", "");
					 			var number4 = number3.replace(",", "");
					 			var number5 = number4.replace(",", "");
					 			var number6 = number5.replace(",", "");
					 			sum = Number(sum) + Number(number6); 
					 			}
					 		});
					 		$.each(allRowsInGrid, function(index, value) {
					 			if(value.status==0){
					 			aVal[index] = value.debit;
					 			var number1 = aVal[index].replace("$", "");
					 			var number2 = number1.replace(".00", "");
					 			var number3 = number2.replace(",", "");
					 			var number4 = number3.replace(",", "");
					 			var number5 = number4.replace(",", "");
					 			var number6 = number5.replace(",", "");
					 			sum1 = Number(sum1) + Number(number6); 
					 			}
					 		});
					 		$('.footrow').css('color','#1A74FB');
					 		console.log("Tot :: "+sum+"----");
					 	    $('#JournalDetailsGrid').jqGrid('footerData','set',{credit:formatCurrency(sum), debit:formatCurrency(sum1)}); 
					 	   var grid=$("#JournalDetailsGrid");
							grid.jqGrid('resetSelection');
						    var dataids = grid.getDataIDs();
						    for (var i=0, il=dataids.length; i < il; i++) {
						        grid.jqGrid('setSelection',dataids[i], false);
						    }	
					},
					errorfunc : function(rowid, response) {
						console.log('EditParams ErrorFunc');
						return false;

					},

					oneditfunc : function(id) {
						console.log('On Edit it calls');
						var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
						$.ajax({
							url: "./checkAccountingCyclePeriods",
							data:{"datetoCheck":$('#DateOfJournal').val(),"UserStatus":checkpermission},
							type: "POST",
							success: function(data) { 
								if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
								{
									aChartDetais = $("#addNewjournalFormID").serialize();
									_globalperiodid=data.cofiscalperiod.coFiscalPeriodId;
									_globalyearid = data.cofiscalperiod.coFiscalYearId;

								}
								else
									{
									
									if(data.AuthStatus == "granted")
									{	
										$("#JournalDetailsGrid").trigger('reloadGrid');
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
															buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
														}).dialog("open");
									}
									else
									{
										$("#JournalDetailsGrid").trigger('reloadGrid');
										showDeniedPopup();
									}
									}
						  	},
				   			error:function(data){
				   				console.log('error');
				   				}
				   			});
						/*		$("#"+id+"_credit").live('keyup', function() {
									$("#"+id+"_debit").val('0.00');
								});
								$("#"+id+"_debit").live('keyup', function() {
									$("#"+id+"_credit").val('0.00');
								});
						*/

					}
					
				}
				
			});

	var custombuttonthereornot=document.getElementById("copyrowcustombutton");
	if(custombuttonthereornot == null){
	$("#JournalDetailsGrid").navButtonAdd('#JournalDetailsGridpager',
			{ 	caption:"Delete", 
		        id:"copyrowcustombutton",
				buttonicon:"ui-icon-trash", 
				onClickButton: deleteJournalDetailsgrid,
				position: "last", 
				title:"Delete", 
				cursor: "pointer"
			} 
		);
	customjournalbutton=true;
	}
}
}
	function customCurrencyFormatter(cellValue, options, rowObject) {
		return formatCurrency(cellValue);
	}
	
	function journalentrygridserialize(){
		var gridRows = $('#JournalDetailsGrid').getRowData();
		var rowData = new Array();
		/*for (var i = 0; i < gridRows.length; i++) {
			var row = gridRows[i];
			rowData.push($.param(row));
			}*/
		//var dataToSend = JSON.stringify(gridRows);
		return gridRows;
	}
	
	 
	 
function deleteJournalDetailsgrid(){
	var selrowid=$("#JournalDetailsGrid").jqGrid('getGridParam', 'selrow');
	
	//alert(selrowid);
	if(selrowid!="new_row")
	{
	var rowData = jQuery("#JournalDetailsGrid").getRowData(selrowid); 
	var glTransactionId = rowData['glTransactionId'];
	//$("#JournalDetailsGrid").trigger('reloadGrid');
	
	if(glTransactionId==null || glTransactionId==undefined || glTransactionId==""){
		$('#JournalDetailsGrid').jqGrid('delRowData',selrowid);
	}else{
		$('#JournalDetailsGrid').jqGrid('delRowData',selrowid);
		//$("#JournalDetailsGrid").setGridParam().trigger('reloadGrid');
		var allRowsInGrid = $('#JournalDetailsGrid').jqGrid('getRowData');
		var aVal = new Array(); 
		var aTax = new Array();
		var sum = 0;
		var sum1 = 0;
			$.each(allRowsInGrid, function(index, value) {
				if(value.status==0){
				aVal[index] = value.credit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
				}
			});
			$.each(allRowsInGrid, function(index, value) {
				if(value.status==0){
				aVal[index] = value.debit;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum1 = Number(sum1) + Number(number6); 
				}
			});
			
			$("#JournalDetailsGrid_iladd").removeClass("ui-state-disabled")
			$("#JournalDetailsGrid_iledit").removeClass("ui-state-disabled")
			$("#JournalDetailsGrid_ilsave").addClass("ui-state-disabled")
			 
			$('.footrow').css('color','#1A74FB');
			console.log("Tot :: "+sum+"----");
		    $('#JournalDetailsGrid').jqGrid('footerData','set',{credit:formatCurrency(sum), debit:formatCurrency(sum1)});
		    
	}
	
	}
	else
		{
		$('#JournalDetailsGrid').jqGrid('delRowData',selrowid);
		$("#JournalDetailsGrid_iladd").removeClass("ui-state-disabled")
		$("#JournalDetailsGrid_iledit").removeClass("ui-state-disabled")
		$("#JournalDetailsGrid_ilsave").addClass("ui-state-disabled")
		}
}

function loadJournalRowDetails(rowId){
	var rowData = jQuery("#JournalsGrid").getRowData(rowId); 
	var coLedgerSourceId = rowData['coLedgerSourceId'];
	var coLedgerHeaderId = rowData['glTransactionId'];
	var coFiscalPeriodId = rowData['coFiscalPeriodId'];
	var number = rowData['coAccountNumber'];
	var Description = rowData['transactionDesc'];
	var reference = rowData['poNumber'];
	var postDate = rowData['entrydate'];
	$("#Number").val(number);
	$("#decriptionNameID").val(Description);
	if(reference==='')
		$("#References").val("--");
	else
		$("#References").val(reference);
	
	$('#referenceID').val(reference);
	$('#DateOfJournal').val(postDate);
	$('#headerid').val(coLedgerHeaderId);
	$('#fisicalperiodid').val(coFiscalPeriodId);
	$('#coLedgerSourceId').val(coLedgerSourceId);
	$("#JournalDetailsGrid").jqGrid('GridUnload');
	loadJournalDetailsGrid(reference);
	$('#JournalDetailsGrid').jqGrid({datatype:'json', postData: { 'coLedgerHeaderId': reference },url:'./journalscontroller/getListOfJournals'});
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
	jQuery("#reasondialogbox").dialog({
		autoOpen:false,
		height:200,
		width:300,
		title:"Reason",
		modal:true,
		close:function(){
			//jQuery("#reasondialogbox").dialog("close");
			return true;
		},
		buttons:{
			Ok:function(){
				if($("#reasontext").val()!=null && $("#reasontext").val().trim()!=null && $("#reasontext").val().trim()!=""){
					saveEditedJournalData(opervalue);
					jQuery("#reasondialogbox").dialog("close");
				}
				
			}/*,
			Close:function(){
				jQuery("#reasondialogbox").dialog("close");
			}*/
		}
	});
});

function cancelAddJournal(){
	jQuery("#addNewJournalDialog").dialog("close");
	return true;
}

function saveNewJournal(){
	 aChartDetais = $("#addNewjournalFormID").serialize();
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
	var journalEntDate = new Date($('#DateOfJournal').val());
	var grid = $("#JournalsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');

	
	if(rowId !== null){
		 
		 console.log("Journal Date: "+journalEntDate);
		 console.log('Fiscal  Date: '+fiscalPeriodDate);
		if(journalEntDate<fiscalPeriodDate){
			jQuery(newDialogDiv).html('<span><b style="color: red;">You Cannot Delete this Journal Account?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Confirm Delete", 
				buttons:{
						"OK": function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		}else{
		jQuery(newDialogDiv).html('<span><b style="color: red;">Do you really want to delete the journal?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Confirm Delete", 
			buttons:{
				"Submit": function(){
					DeleteJournalRecord(); 
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		}
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
			
			var errorText = "<b style='color:red;'>Journal details are Deleted successfully.</b>";
			
			$("#journalMessage").css("display", "block");
			$('#journalMessage').html(errorText);
			
			setTimeout(function() {
				$('#journalMessage').html("");
				 document.getElementById("chartsDetailsFromID").reset();location.reload(true);
				}, 2000);
			
			}
	});
}
function journalentrygridserialize(){
	var gridRows = $('#JournalDetailsGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	//var dataToSend = JSON.stringify(gridRows);
	return rowData;
}
function comparetwoarrayobject(oldobject,newobject){
	var returnvalue=false;
	console.log('oldobject'+JSON.stringify(oldobject)+'newobject ::'+JSON.stringify(newobject));
	/*if(JSON.stringify(oldobject)==JSON.stringify(newobject)){
		returnvalue=true;
	}*/
	
	$.ajax({
		url: "./companycontroller/comparetwoGridObject",
		type: "GET",
		async:false,
	    data: {'oldGridData':JSON.stringify(oldobject),'newGridData':JSON.stringify(newobject)},
		success: function(data) {
			returnvalue=data;
		}
	});
	
	/*if(oldobject.length==newobject.length){*/
		/*for(var i=0;i<oldobject.length;i++){
			if(oldobject[i].trim()==newobject[i].trim()){
				//alert(oldobject[i].trim());
				alert(JSON.stringify(oldobject[i]));
				console.log(JSON.stringify(oldobject[i]));
				//alert(oldobject[i].credit);
				alert("old value"+oldobject[i].trim());
				alert("new value"+newobject[i].trim());
				returnvalue=true;
			}
			//alert(oldobject[i]+"=="+newobject[i]);
		}
	//}*/
	return returnvalue;
}
function beforeenteringtosave(operation){
	opervalue=operation;
	var newformseriallize=$("#chartsDetailsDiv :input").serialize();
	var newgridaccountgriddata =  $('#JournalDetailsGrid').getRowData();
	var returndatavalue=comparetwoarrayobject(oldgridaccountgriddata,newgridaccountgriddata);
	var checkdatachangesornotinform=true;
	var checkformdatacompare=checkformdataComparition();
	if(checkformdatacompare && returndatavalue){
		checkdatachangesornotinform=false;	
	}
	if(operation!="delete"){
		if(checkdatachangesornotinform){
			var checkreturnvalue=checkValuesAreNotEqual();
			if(checkreturnvalue){
				jQuery(newDialogDiv).html('<span><b style="color: red;">Credit and Debit Values are not Equal?</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:160, title:"Confirm Delete", 
					buttons:{
						"OK": function(){
							jQuery(this).dialog("close");
						return false;
						},
						Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
				return false;
			}else{
				$("#reasontext").val("");
				jQuery("#reasondialogbox").dialog("open");
			}
			
			
		}else{
			//alert("else");
			reloadpage();
			
		}
	}else{
		 $.ajax({
			 
		 		url: "./banking/getopenPeriods",
		 		type: "GET",
		 		success: function(data) {
		 			var fDate=new Date(data.startDate);
		 			var lDate=new Date(data.endDate);
		 			var selrowid=$("#JournalsGrid").jqGrid('getGridParam', 'selrow');
		 			var entrydate=$("#JournalsGrid").jqGrid('getCell', selrowid, 'entrydate');
		 			var cDate=new Date(entrydate);
		 			 if(cDate.getMonth() >= fDate.getMonth()) {
		 				$("#reasontext").val("");
		 				jQuery("#reasondialogbox").dialog("open");
		 		    }else{
		 		    	
		 		    	jQuery(newDialogDiv).attr("id","msgDlg");
		 				jQuery(newDialogDiv).html('<span><b style="color:red;">Current entry is less than fiscal period</b></span>');
		 				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		 					buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		 		    }
		 		
		 		}
		 });
	}
	
	
}
function reloadpage(){
	$("#journalnewsave").css({ display: "inline-block" });
	$("#journaleditsave").css({ display: "none" });
	
	$('#journalnewsave').removeAttr("disabled");
	$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
	

	$("#journaleditbutton").css({ display: "none" });
	$("#journaldelete").css({ display: "none" });
	
	$("#JournaldetailshistoryGrid").jqGrid('GridUnload');
	$("#JournalsGrid").jqGrid('GridUnload');
	loadJournalsGrid(); 
	$("#JournalsGrid").trigger("reloadGrid");
	
	$("#JournalDetailsGrid").jqGrid('GridUnload');
	loadJournalDetailsGrid(-1); 
	$("#JournalDetailsGrid").trigger("reloadGrid");

	$('#DateOfJournal').val('');
	$('#decriptionNameID').val('');
	$('#References').val('');
	$('#headerid').val('');
	$('#fisicalperiodid').val('');
	$('#coLedgerSourceId').val('');
	$("#reasontext").val("");
	//$('#DateOfJournal').focus();
	$.ajax({
		url: "./journalscontroller/getRreferenceNo",
		type: "POST",
		success: function(data) {
			console.log(data);
			$('#References').val(data);
		}
	});
}


function checkValuesAreNotEqual(){
	var returnvalue=false;
	var allRowsInGrid = $('#JournalDetailsGrid').jqGrid('getRowData');
	var sum = 0;
	var sum1 = 0;
	var aVal = new Array(); 
	var aTax = new Array();
	$.each(allRowsInGrid, function(index, value) {
		if(value.status==0){
		aVal[index] = value.credit;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum = Number(sum) + Number(number6);
		}
	});
	$.each(allRowsInGrid, function(index, value) {
		if(value.status==0){
		aVal[index] = value.debit;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum1 = Number(sum1) + Number(number6); 
		}
	});
	if(parseFloat(sum).toFixed(2)!=parseFloat(sum1).toFixed(2)){
		returnvalue=true;
	}
	
	return returnvalue;
}
function saveEditedJournalData(operation,formchange,gridchange) {
	
	$('#journalnewsave').attr("disabled",true);
	$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
			
	
	var allRowsInGrid = $('#JournalDetailsGrid').jqGrid('getRowData');
	var aVal = new Array(); 
	var aTax = new Array();
	
	if(allRowsInGrid.length==0 || allRowsInGrid==""){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Requesting to add Account Details</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:160, title:"Add Account Details", 
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$('#journalnewsave').removeAttr("disabled");
					$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
					
				return false;
				},
				Cancel: function ()	{jQuery(this).dialog("close");
				$('#journalnewsave').removeAttr("disabled");
				$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
				} } }).dialog("open");
		return true;
	}

	var sum = 0;
	var sum1 = 0;
	$.each(allRowsInGrid, function(index, value) {
		if(value.status==0){
		aVal[index] = value.credit;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum = Number(sum) + Number(number6);
		}
	});
	$.each(allRowsInGrid, function(index, value) {
		if(value.status==0){
		aVal[index] = value.debit;
		var number1 = aVal[index].replace("$", "");
		var number2 = number1.replace(".00", "");
		var number3 = number2.replace(",", "");
		var number4 = number3.replace(",", "");
		var number5 = number4.replace(",", "");
		var number6 = number5.replace(",", "");
		sum1 = Number(sum1) + Number(number6); 
		}
	});
	
	// comment removed by leo because of Journal entry will save the unbalanced entries
	if(parseFloat(sum).toFixed(2)!=parseFloat(sum1).toFixed(2)){
		jQuery(newDialogDiv).html('<span><b style="color: red;">Credit and Debit Values are not Equal?</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:160, title:"Confirm Delete", 
			buttons:{
				"OK": function(){
					jQuery(this).dialog("close");
					$('#journalnewsave').removeAttr("disabled");
					$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
					
				return false;
				},
				Cancel: function ()	{jQuery(this).dialog("close");
				$('#journalnewsave').removeAttr("disabled");
				$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
				
				} } }).dialog("open");
		return true;
	}
	else{
		
		var additionalInfo = ''; //$("#chartsDetailsFromID").serialize();
		
		var additionalInfo= headerDetails;
		if(operation!="add"){
			var reasontext=$("#reasontext").val();
			additionalInfo="DateOfJournal="+$('#DateOfJournal').val()+"&References="+$('#References').val()+"&decriptionNameID="+$('#decriptionNameID').val()+"&headerid="+$('#headerid').val()+"&formchange="+formchange+"&gridchange="+gridchange+"&reason="+reasontext;
		}
		 
		var gridRows = $('#JournalDetailsGrid').getRowData();
		var rowData = new Array();
		for (var i = 0; i < gridRows.length; i++) {
			var row = gridRows[i];
			rowData.push($.param(row));
			}
		var dataToSend = JSON.stringify(gridRows);
		
		dataToSend = dataToSend.replaceAll("&", "#");
		dataToSend = dataToSend.replaceAll("$", "");
		//dataToSend = dataToSend.replace();
		additionalInfo=additionalInfo+"&gridData="+dataToSend+"&operation="+operation;
				
					if(_globalperiodid!==undefined)
					{
						$.ajax({
							url: "./journalscontroller/updateData",
							type: "POST",
							data : additionalInfo+"&coFiscalPeriodId="+_globalperiodid+"&coFiscalYearId="+_globalyearid,
							success: function(data) {
								createtpusage('Company-Journal Entries','Save Journal','Info','Company-Journal Entries,Saving Journal,References:'+$('#References').val());
								var errorText = "<b style='color:Green;'>Journal details are Updated successfully.</b>";
								
								$("#journalMessage").css("display", "block");
								$('#journalMessage').html(errorText);
								
								$('#journalnewsave').removeAttr("disabled");
								$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
								
								
								setTimeout(function() {
									$('#journalMessage').html("");
									if(operation=="add" && data!=$("#References").val()){
										
										jQuery(newDialogDiv).html('<span><b style="color: Green;">Your Reference Number :'+data+'</b></span>');
										jQuery(newDialogDiv).dialog({modal: true, width:300, height:160, title:"Information", 
											buttons:{
												"OK": function(){
													jQuery(this).dialog("close");
													reloadpage();
												}} }).dialog("open");
									}else{
										reloadpage();
									}
									
									/*$("#journalnewsave").css({ display: "inline-block" });
									$("#journaleditsave").css({ display: "none" });
				
									$("#journaleditbutton").css({ display: "none" });
									$("#journaldelete").css({ display: "none" });
									
									$("#JournalsGrid").jqGrid('GridUnload');
									loadJournalsGrid(); 
									$("#JournalsGrid").trigger("reloadGrid");
									
									$("#JournalDetailsGrid").jqGrid('GridUnload');
									loadJournalDetailsGrid(-1); 
									$("#JournalDetailsGrid").trigger("reloadGrid");
				
									$('#DateOfJournal').val('');
									$('#decriptionNameID').val('');
									$('#References').val('');
									$('#headerid').val('');
									$('#fisicalperiodid').val('');
									$('#coLedgerSourceId').val('');
									//$('#DateOfJournal').focus();
									$.ajax({
										url: "./journalscontroller/getRreferenceNo",
										type: "POST",
										success: function(data) {
											console.log(data);
											$('#References').val(data);
										}
									});*/
								}, 2000);
														
							}
						});
						
					}
					else
						{
						$('#journalnewsave').removeAttr("disabled");
						$('#journalnewsave').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');	
						
						}
					

	}
	
	
}


function checkformdataComparition(){
	var returnValue=false;
	var New_decriptionNameID=$("#decriptionNameID").val();
	var New_date=$("#DateOfJournal").val();
	var New_References=$("#References").val();
	var newform=New_decriptionNameID+New_date+New_References;
	
	var rowId=$("#JournalsGrid").jqGrid('getGridParam', 'selrow');
	var rowData = jQuery("#JournalsGrid").getRowData(rowId); 
	var Old_decriptionNameID = rowData['transactionDesc'];
	var Old_References = rowData['poNumber'];
	var Old_date = rowData['entrydate'];
	var oldForm=Old_decriptionNameID+Old_date+Old_References;
	
	if(newform===oldForm){
		returnValue=true;
	}
	return returnValue;
}

String.prototype.replaceAll = function(target, replacement) {
	  return this.split(target).join(replacement);
	};
	
