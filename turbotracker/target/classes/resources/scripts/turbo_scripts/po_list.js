var fromDate='';
var toDate='';
var searchData='';
jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");
	$("#resetbutton").css("display", "inline-block");
	$("#fromDate").prop('disabled',true);
	 $("#toDate").prop('disabled',true);
	 searchData = $('#searchJob').val();
	 showPurchaseOrderList(searchData,fromDate,toDate);
});

function showPurchaseOrderList(searchData,fromDate,toDate){
	$("#PurchaseOrdersGrid").jqGrid({
		url:'./po_listgrid?searchData='+searchData+'&fromDate='+fromDate+'&toDate='+toDate,
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#PurchaseOrdersGridPager'),
		colNames:['vePOID','PO #','rxVendorID', 'joReleaseID', 'Created On', 'Job Name', 'Vendor', 'Total'],
		colModel :[
           	{name:'vePOID', index:'vePOID', align:'left', width:40, editable:true,hidden:true},
			{name:'ponumber', index:'ponumber', align:'left', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'rxVendorID', index:'rxVendorID', align:'', width:80,hidden:true},
			{name:'joReleaseID', index:'joReleaseID', align:'center', width:30,hidden:true},
			{name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
			{name:'jobName', index:'jobName', align:'', width:40,hidden:false},
			{name:'vendorName', index:'vendorName', align:'left', width:80, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
			{name:'subtotal', index:'subtotal', align:'right', width:40,hidden:false,formatter:customCurrencyFormatter}
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#PurchaseOrdersGridPager',
		sortname: 'vePOID', sortorder: "desc",	imgpath: 'themes/basic/images',	caption: 'Purchase orders',
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var aVePOId = rowData['vePOID'];
			var aQryStr = "aVePOId=" + aVePOId;
			createtpusage('Company-Vendors-Purchase Orders','PO View from Grid Row','Info','Company-Vendors-Purchase Orders,View PO,PO ID:'+aVePOId);
			var checkpermission=getGrantpermissionprivilage('Purchase_Orders',0);
			if(checkpermission){
			document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
    	editurl:"./salesOrderController/deletePoItem"
	}).navGrid('#PurchaseOrdersGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false},{},{},
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

function callEnableDate(){
	if(document.getElementById("fromDate").disabled){
	 $("#fromDate").prop('disabled',false);
	 $("#toDate").prop('disabled',false);
	}else{
		$("#fromDate").val("");
		 $("#toDate").val("");
		 fromDate="";toDate="";
		 $("#PurchaseOrdersGrid").jqGrid('GridUnload');
		 showPurchaseOrderList(searchData,fromDate,toDate);
		$("#fromDate").prop('disabled',true);
		 $("#toDate").prop('disabled',true);
	}
}

function getSearchDetails()
{
	if($('#searchJob').val() != null && $('#searchJob').val() != '')
	{
		$("#searchJob").autocomplete({
					disabled: true
		});

		searchData = $('#searchJob').val();			
		$("#PurchaseOrdersGrid").jqGrid('GridUnload');
		showPurchaseOrderList(searchData,fromDate,toDate);
		$("#PurchaseOrdersGrid").trigger("reloadGrid");
		$('#searchJob').val('');
	}			
}	
function ResetDetails() {
	searchData = '';
	$("#fromDate").val("");
	 $("#toDate").val("");
	 fromDate="";toDate="";
	 $('#dateRange').attr('checked',false);
	$('#searchJob').val('');
	$("#PurchaseOrdersGrid").jqGrid('GridUnload');
	showPurchaseOrderList(searchData,fromDate,toDate);
	$("#PurchaseOrdersGrid").trigger("reloadGrid");
}

$( "#fromDate" ).change(function() {
	fromDate = $("#fromDate").val();
	$("#PurchaseOrdersGrid").jqGrid('GridUnload');
	showPurchaseOrderList(searchData,fromDate,toDate);
	$("#PurchaseOrdersGrid").trigger("reloadGrid");
	});
$( "#toDate" ).change(function() {
	toDate = $("#toDate").val();
	$("#PurchaseOrdersGrid").jqGrid('GridUnload');
	showPurchaseOrderList(searchData,fromDate,toDate);
	$("#PurchaseOrdersGrid").trigger("reloadGrid");
	});

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function addNewPO()
{
	var checkpermission=getGrantpermissionprivilage('Purchase_Orders',0);
	if(checkpermission){
	document.location.href = "./addpurchaseorder";
	}
	
}