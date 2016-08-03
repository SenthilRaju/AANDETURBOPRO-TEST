var fromDate='';
var toDate='';
var searchData='';	
var vepoid = $('#vendorid').val();
var sortbydate = $('#sortbydate').val();
var rangefromdate = $('#rangefromdate').val();

jQuery(document).ready(function(){
	$("#fromDate").prop('disabled',true);
	$("#toDate").prop('disabled',true);
	$("#resetbutton").css("display", "inline-block");
	showReceivedInventoryDeatils(searchData,sortbydate,fromDate,toDate);
});

function ResetDetails() {
	searchData = '';
	$('#searchJob').val('');
	$("#fromDate").val("");
	 $("#toDate").val("");
	 $('#dateRange').attr('checked',false);
	 callEnableRecieveDate();
	 fromDate="";toDate="";
	 $("#lineItemGrid").jqGrid('GridUnload');
	showReceivedInventoryDeatils(searchData,sortbydate,fromDate,toDate);
	$("#lineItemGrid").trigger("reloadGrid");
}

function getSearchDetails()
{
	if($('#searchJob').val() != null && $('#searchJob').val() != '')
	{
		$("#searchJob").autocomplete({
					disabled: true
				});
		searchData = $('#searchJob').val();			
		$("#lineItemGrid").jqGrid('GridUnload');
		showReceivedInventoryDeatils(searchData,sortbydate,fromDate,toDate);
		$("#lineItemGrid").trigger("reloadGrid");
		$('#searchJob').val('');
	}			
}	

function showReceivedInventoryDeatils(vepoid,sortbydate,fromdate1,todate1)
{
$("#lineItemGrid").jqGrid({
	url:'./receivedpolist',
	datatype: 'JSON',
	postData: {searchData : searchData,sortBy:sortbydate,fromDate:fromdate1,toDate:todate1},
	mtype: 'POST',
	pager: jQuery('#lineItemPager'),
	colNames:['vePOID','rxVendorID', 'joReleaseID', 'Received Date','PO #', 'Job Name', 'Received From', 'Total','','veReceiveID'],
	colModel :[
       	{name:'vePOID', index:'vePOID', align:'left', width:40, editable:true,hidden:true},
		{name:'rxVendorID', index:'rxVendorID', align:'', width:80,hidden:true},
		{name:'joReleaseID', index:'joReleaseID', align:'center', width:30,hidden:true},
		{name:'createdOn', index:'createdOn', align:'center', width:157,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
		{name:'ponumber', index:'ponumber', align:'center', width:136,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		{name:'jobName', index:'jobName', align:'', width:40,hidden:true},
		{name:'vendorName', index:'vendorName', align:'left', width:793, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
		{name:'subtotal', index:'subtotal', align:'right', width:40,hidden:true,formatter:customCurrencyFormatter},
		{name:'difference', index:'difference', align:'center', width:40,hidden:false,formatter:recieveApplyingFromatter},
		{name:'veReceiveID', index:'veReceiveID', align:'center', width:40,hidden:true}
	],
	rowNum: 50,
	pgbuttons: true,	
	recordtext: '',
	rowList: [50, 100, 200, 500, 1000],
	viewrecords: true,
	pager: '#lineItemPager',
	sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Purchase orders',
	//autowidth: true,
	height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
	loadComplete: function(data) {
		
    },
	loadError : function (jqXHR, textStatus, errorThrown) {
	    
	},
	ondblClickRow: function(rowId) {
		var rowData = jQuery(this).getRowData(rowId); 
		var aVePOId = rowData['vePOID'];
		var aVeReceiveID = rowData['veReceiveID'];
		var aQryStr = "aVePOId=" + aVePOId;
		document.location.href="./showReceivedInventory?vePOID="+aVePOId+"&veReceiveID="+aVeReceiveID;
		//                        document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
	editurl:"./salesOrderController/deletePoItem"
}).navGrid('#lineItemPager',//{cloneToTop:true},
	{add:false,edit:false,del:true,refresh:true,search:false},{},{},
	myDelOptions = { 
	    closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
	    caption: "Delete",
	    msg: 'Do you want to delete the line item?',
	    color:'red',

	    onclickSubmit: function(params){
	     var grid = $("#PurchaseOrdersGrid");
	     var rowId = grid.jqGrid('getGridParam', 'selrow');
	     var vePOID = grid.jqGrid('getCell', rowId, 'vePOID');
	     return { 'vePOID' : vePOID};
	    },
	    afterSubmit:function(response,postData){
	    	alert('after submit');
	      return [true, $('#PurchaseOrdersGrid').trigger("reloadGrid")];
	    }
	   }
);
}


function locateReceiveInventory()
{		
	try{
    var newDialogDiv = jQuery(document.createElement('div'));
    var so = "Enter PO#"	
    jQuery(newDialogDiv).attr("id", "locateReciveInventory");
	jQuery(newDialogDiv).html(
			'<span>'+ so + '<input type = "text" name="ponumber" id="ponumber"></input></span>'+
			'<span style="color:red; display: none;" id="errorSpan">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+
			'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Please Enter Correct PO Number</span>');
	
	jQuery(newDialogDiv).dialog({
		modal : true,
		width : 500,
		height : 250,
		title : "Info Required",
		buttons : [ {
			height : 35,
			text : "Ok",
			click : function() {
				var ponumber = $('#ponumber').val();
				
				getvePODetails(ponumber);
				
			}
		},{
			height : 35,
			text : "cancel",
			click : function() {				
				$(this).dialog("close");		
				
			}
		} ]
	}).dialog("open");
	
}catch(err){
	alert(err.message);
	}
}

function getvePODetails(ponumber){
	if(ponumber!=null && typeof(ponumber) != "undefined"){
		try{
		$.ajax({
			url: "./inventoryList/recieveInventory",
			type: "GET",
			data : "poNumber="+$('#ponumber').val(),
			success: function(data) {
				
				if(data.vePoid != null){
					var vepoid = data.vePoid;
					var reciveid = data.receiveID;
					if(reciveid!=null && reciveid!=0)
					{
						
						 var newDialogDiv1= jQuery(document.createElement('div'));
							jQuery(newDialogDiv1).html(
									'<span style="color:green;font-weight:bold ">Note: Some Items already have been received for this PO. ***CHECK QUANTITIES*** </span>');
							
							jQuery(newDialogDiv1).dialog({
								modal : true,
								width : 380,
								height : 150,
								title : "Warning",
								buttons : [ {
									height : 35,
									text : "Ok",
									click : function() {
										document.location.href="./showReceivedInventory?vePOID="+vepoid;
										//document.location.href="./showReceivedInventory?vePOID="+vepoid+"&veReceiveID="+reciveid;
									}
								},{
									height : 35,
									text : "cancel",
									click : function() {			
										$(this).dialog("close");		
									}
								} ]
							}).dialog("open");
							
					
					}
					else
					{
						document.location.href="./showReceivedInventory?vePOID="+vepoid;
					}
				}else{
					$('#errorSpan').css('display','block');
					setTimeout(function() {
						$('#errorSpan').css('display','none');
					}, 2000);
				}
				
			}
		});
		jQuery('#newSO').dialog("close");
		}catch(err){
			alert(err.message);
			}
		}
		
		$('#ponumber').val('');
		//jQuery('#newSO').dialog("close");
}

function callEnableRecieveDate(){
	
	if(document.getElementById("fromDate").disabled){
	 $("#fromDate").prop('disabled',false);
	 $("#toDate").prop('disabled',false);
	}else{
		$("#fromDate").val("");
		 $("#toDate").val("");
		 fromDate="";toDate="";
		 var vepoid = $('#vendorid').val();
		 $("#lineItemGrid").jqGrid('GridUnload');
		 showReceivedInventoryDeatils(vepoid,0,fromDate,toDate);
		$("#fromDate").prop('disabled',true);
		 $("#toDate").prop('disabled',true);
	}
}

$(function(){

$( "#fromDate" ).change(function() {
	
	var selectedacc = $('.chzn-select').val();
	var sortby = $('#recieveInventorySort').val();
	var fromDate=$('#fromDate').val();
	var toDate = $('#toDate').val();
	 var vepoid = $('#vendorid').val();
	//range = range+'to';
	$("#lineItemGrid").jqGrid('GridUnload');
	showReceivedInventoryDeatils(vepoid,0,fromDate,toDate);
	/*
	if(range!=''){
		range = range+'to';
		if(raangeto==''){
			alert("Please select 'to' date");
		}else{
			$("#lineItemGrid").jqGrid('GridUnload');
			showReceivedInventoryDeatils(0,0,range+raangeto);
		}
	}else{
		range = range+'to';
		$("#lineItemGrid").jqGrid('GridUnload');
		showReceivedInventoryDeatils(0,0,range+raangeto);
	}
	*/
	});

$( "#toDate" ).change(function() {
	
	var selectedacc = $('.chzn-select').val();
	var sortby = $('#recieveInventorySort').val();
	var fromDate=$('#fromDate').val();
	var toDate = $('#toDate').val();
	 var vepoid = $('#vendorid').val();
	//range = range+'to';
	$("#lineItemGrid").jqGrid('GridUnload');
	showReceivedInventoryDeatils(vepoid,0,fromDate,toDate);
	
	});
});

function recieveApplyingFromatter(cellvalue, options, rowObject){

	console.log("leo test");
	 var anReceiveDifference = rowObject.difference;
	 
	 if(anReceiveDifference==0)
		 {
		 image='<img alt="search" src="../resources/images/greenTick.png" style="cursor:pointer; text-align:center ">';
		 cellvalue = image;
		 }
	 else
		 {
		 image = '<img alt="search" src="../resources/images/redhalf.png" style="cursor:pointer;">';
			cellvalue = image;
		 }
	 return cellvalue;
	
}
