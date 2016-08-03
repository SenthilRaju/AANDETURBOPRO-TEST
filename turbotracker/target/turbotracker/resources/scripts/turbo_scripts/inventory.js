jQuery(document).ready(function() {
	loadInventoryGrid();
});

function loadInventoryGrid() {
	var aCustomerPage = $("#inventoryID").val();
	$("#inventoryGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./inventoryList',
		pager: jQuery('#inventoryGridpager'),
	   	colNames:['prMasterID', 'Item Code', 'Description','On Hand', 'Allocated', 'Available', 'On Order', 'Submitted'],
	   	colModel:[
			{name:'prMasterId',index:'prMasterId', width:50,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'itemCode',index:'itemCode', width:80,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inventoryOnHand',index:'inventoryOnHand', align:'center',width:50,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'inventoryAllocated',index:'inventoryAllocated',align:'center', width:50,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inventoryAvailable',index:'inventoryAvailable',align:'center', width:50,editable:true, editrules:{required:true},formatter:availableInventory, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'inventoryOnOrder',index:'inventoryOnOrder', align:'center', width:50,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'submitted',index:'submitted',align:'center', width:50,editable:true, editrules:{required:true}, editoptions:{size:10}}],
		rowNum: aCustomerPage,	
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#inventoryGridpager',
		sortname: 'name',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Inventory',
		height:540,	width: 1150,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			$(".ui-pg-selbox").attr("selected", aCustomerPage);
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
    	ondblClickRow: function(rowid) {
    		var rowData = jQuery(this).getRowData(rowid); 
			var prMasterID = rowData['prMasterId'];
			var ItemCode = rowData['itemCode'];
				document.location.href = "./inventoryDetails?token=view&inventoryId="+prMasterID+"&itemCode="+ItemCode;
	    	return true;
    	}
	}).navGrid('#inventoryGridpager',
		{add:false,edit:false,del:false,refresh:false,search:false}
	);
	return true;
}
function availableInventory(cellValue, options, rowObject){
	var onHand = rowObject.inventoryOnHand;
	var allocated = rowObject.inventoryAllocated;
	var available = onHand - allocated;
	return available;
}
$(function() { var cache = {}; var lastXhr='';
$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
	select: function (event, ui) {
		var aValue = ui.item.value;
		var valuesArray = new Array();
		valuesArray = aValue.split("|");
		$("#inventorysearch").val(valuesArray[1]);
		var id = valuesArray[0];
		var code = valuesArray[2];
		location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
	},
	open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/searchinventory", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

/** Add Inventory Dialog Box **/
/** Add User Dialog Function **/
jQuery( function(){
	jQuery("#addNewInventoryDialog").dialog({
		autoOpen:false,
		height:300,
		width:730,
		title:"Add New Inventory",
		modal:true,
		close:function(){
			$('#userFormId').validationEngine('hideAll');
			return true;
		}
	});
});

/** Add New Inventory **/
function addNewInventory(){
	document.location.href = "./inventoryDetails?token=new";
	return true;
}
