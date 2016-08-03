var selectedId;
jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");
	$("#transferGrid").jqGrid({	
		
		url:'./wh_transerList',
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#transferGridPager'),
		colNames:['prTransferId','Date','Trans #', 'Reference', 'From', 'To', 'Est.Receive Date'],
		colModel :[
           	{name:'prTransferId', index:'prTransferId', align:'center', width:40, editable:true,hidden:true},
           	{name:'transferDate', index:'transferDate', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
           	{name:'transactionNumber', index:'transactionNumber', align:'center', width:80,hidden:false},
			{name:'desc', index:'desc', align:'center', width:80,hidden:false},
			{name:'from', index:'from', align:'center', width:30,hidden:false},
			{name:'to', index:'to', align:'center', width:30,hidden:false},
			{name:'receivedDate', index:'receivedDate', align:'center', width:50,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}}
			
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [0,50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#transferGridPager',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Orders in Transit',
		//autowidth: true,height:547
		height:310,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			$('.ui-jqgrid-title').css('text-align','center');
			$('#transferGrid').setSelection(1,true);
			selectedId = 1;
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		onSelectRow:  function(id){
			var rowData = jQuery(this).getRowData(id); 
			var prTransferId = rowData['prTransferId'];
			var aQryStr = "prTransferId=" + prTransferId;
			selectedId = id;
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var prTransferId = rowData['prTransferId'];
			var aQryStr = "prTransferId=" + prTransferId;
			var checkpermission=getGrantpermissionprivilage('Transfer',0);
			if(checkpermission){
			document.location.href = "./warehouseTransfer?token=edit&" + aQryStr;
			}
			//document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
	}).navGrid('#transferGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:false,search:false},{},{},
		myDelOptions = { 
		    closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
		    caption: "Delete",
		    msg: 'Do you want to delete the line item?',
		    color:'red',

		    onclickSubmit: function(params){
		     var grid = $("#transferGrid");
		     var rowId = grid.jqGrid('getGridParam', 'selrow');
		     var vePOID = grid.jqGrid('getCell', rowId, 'vePOID');
		     return { 'vePOID' : vePOID};
		    },
		    afterSubmit:function(response,postData){
		      return [true, $('#transferGrid').trigger("reloadGrid")];
		    }
		   }
	);
	
	$("#transferGrid").closest("div.ui-jqgrid-view")
    .children("div.ui-jqgrid-titlebar")
    .css("text-align", "center")
    .children("span.ui-jqgrid-title")
    .css("float", "none");
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

function addTransfer()
{
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission){
	document.location.href = "./warehouseTransfer";
	}
}

function editTransfer()
{
	var aSelectedRowId = $("#transferGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId != null){
	var rowId = $("#transferGrid").getRowData(selectedId);
	
	var prTransferId = rowId['prTransferId'];
	var aQryStr = "prTransferId=" + prTransferId;
	console.log(aQryStr+"-----------"+name);
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission){
	document.location.href = "./warehouseTransfer?token=edit&" + aQryStr;
	}
	}
	//document.location.href = "./warehouseTransfer";
}

function copy()
{
	
	var aSelectedRowId = $("#transferGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId != null){
	var rowId = $("#transferGrid").getRowData(selectedId);
	
	var prTransferId = rowId['prTransferId'];
	var aQryStr = "prTransferId=" + prTransferId;
	console.log(aQryStr+"-----------"+name);
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission){
	document.location.href = "./warehouseTransfer?token=copy&" + aQryStr;
	}
	}
}
function viewWhTransferPDF(){
	var aSelectedRowId = $("#transferGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId != null){
	var rowId = $("#transferGrid").getRowData(selectedId);
	
	var prTransferId = rowId['prTransferId'];
	var aQryStr = "prTransferID=" + prTransferId;
	console.log(aQryStr+"-----------"+name);	
	window.open("./salesOrderController/printWarehouseTransferReport?"+aQryStr);
	return true;
}
}

$(function() { var cache = {}; var lastXhr='';
$("#searchJob").autocomplete({ minLength: 1,timeout :1000,
	/*open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},*/
select: function (event, ui) {
	var aValue = ui.item.value;
	var aText = ui.item.label;
	var valuesArray = new Array();
	valuesArray = aValue.split("|");
	
	
	var aQryStr = "prTransferId=" + aValue;
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission){
	document.location.href = "./warehouseTransfer?token=edit&" + aQryStr;
	}
	
},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/getWarehouseTransferSearch", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} });
});
