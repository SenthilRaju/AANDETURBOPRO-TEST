var warehouse = $('#bankAccountsID').val();
var inactive = $('#inactivelist').prop('checked');
jQuery(document).ready(function() {
	
	$("#searchJob").attr("placeholder","Minimum 2 characters required to get List");
	$("#resetbutton").show();
	loadInventoryGrid();
	//loadInventoryValueGrid();

});

var posit_outside_inventoryGrid=0;
function loadInventoryGrid() {
	var checked = 0;
	if(inactive==false){
		checked =0;
	}else{
		checked =1;
	}

	
	
	warehouse = $('#bankAccountsID').val();
	
	var key =$("#searchJob").val(); 
	var aCustomerPage = $("#inventoryID").val();
	//alert("customerpage=="+aCustomerPage);
	$("#inventoryGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./inventoryList',
		postData: {'itemCode':key,'Inactive':checked,'Warehouse':warehouse},
		postData: {'searchData':key,'Inactive':checked,'Warehouse':function(){//alert("inside grid"+key); 
			return warehouse;}},
		pager: jQuery('#inventoryGridpager'),
	   	colNames:['prMasterID', 'Item Code', 'Description','Department','Category','Primary Vendor','On Hand', 'Allocated', 'Available', 'On Order', 'Submitted'],
	   	colModel:[
			{name:'prMasterId',index:'prMasterId', width:50,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'itemCode',index:'itemCode', width:80,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'prDepartment',index:'prDepartment',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'prCategory',index:'prCategory',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'vendorName',index:'vendorName',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
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
		sortname: 'itemCode',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Inventory',
		height:540,	width: 1150,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadBeforeSend: function(xhr) {
			posit_outside_inventoryGrid=getUrlVars()["gridposition"];
			if(posit_outside_inventoryGrid==null||posit_outside_inventoryGrid==undefined ){
				posit_outside_inventoryGrid=0;
			}
		},
		loadComplete:function(data) {
			if(aCustomerPage!=null){
				$(".ui-pg-selbox").attr("selected", aCustomerPage);	
			}
			
			var allRowsInGrid = $('#inventoryGrid').jqGrid('getRowData');
			//alert(allRowsInGrid.length);
			if(allRowsInGrid.length == 1)
			{
				posit_outside_inventoryGrid=jQuery("#inventoryGrid").closest(".ui-jqgrid-bdiv").scrollTop();
				var prMaster;
				var itemCode;
				$.each(allRowsInGrid, function(index, value) {
					prMaster = value.prMasterId;
					itemCode = value.itemCode;
					console.log("prMaster---"+prMaster+'===itemCode ::'+itemCode);
					 
				});
				location.href="./inventoryDetails?token=view&inventoryId="+prMaster + "&itemCode=" + itemCode+"&gridposition="+posit_outside_inventoryGrid;
			}
			
			$("#searchJob").autocomplete({
				disabled: false
			});
			
		},gridComplete: function () {
			
            jQuery("#inventoryGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_inventoryGrid);
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
    		posit_outside_inventoryGrid=jQuery("#inventoryGrid").closest(".ui-jqgrid-bdiv").scrollTop();
    		var rowData = jQuery(this).getRowData(rowid); 
			var prMasterID = rowData['prMasterId'];
			var ItemCode = rowData['itemCode'];
			var checkpermission=getGrantpermissionprivilage('Inventory',0);
			if(checkpermission){
				document.location.href = "./inventoryDetails?token=view&inventoryId="+prMasterID+"&itemCode="+ItemCode+"&gridposition="+posit_outside_inventoryGrid;
			}
	    	return true;
    	}
	}).navGrid('#inventoryGridpager',
		{add:false,edit:false,del:false,refresh:false,search:false}
	);
	return true;
}

function ResetDetails() {
	$('#searchJob').val('');
	$("#inventoryGrid").jqGrid('GridUnload');
	loadInventoryGrid();
	//$("#inventoryValueGrid").trigger("reloadGrid");

}

function loadInventoryValueGrid() {
	var checked = 0;
	if(inactive==false){
		checked =0;
	}else{
		checked =1;
	}
	
	var key = $('#key').val();
	
	//alert("Test for---?"+key)
	var aCustomerPage = $("#inventoryID").val();

	$("#inventoryValueGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./inventoryList',
		postData: {'searchData':key,'Inactive':checked,'Warehouse':warehouse,},
		pager: jQuery('#inventoryValueGridpager'),
	   	colNames:['prMasterID', 'Item Code', 'Description','Department','Category','Primary Vendor','On Hand', 'Allocated', 'Available', 'On Order', 'Submitted'],
	   	colModel:[
			{name:'prMasterId',index:'prMasterId', width:50,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'itemCode',index:'itemCode', width:80,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'prDepartment',index:'prDepartment',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'prCategory',index:'prCategory',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'vendorName',index:'vendorName',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
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
		pager: '#inventoryValueGridpager',
		sortname: 'itemCode',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Inventory',
		height:540,	width: 1150,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			if(aCustomerPage!=null){
				$(".ui-pg-selbox").attr("selected", aCustomerPage);	
			}
			
			var allRowsInGrid = $('#inventoryValueGrid').jqGrid('getRowData');
			//alert(allRowsInGrid.length);
			if(allRowsInGrid.length == 1)
			{
				var prMaster;
				var itemCode;
				$.each(allRowsInGrid, function(index, value) {
					prMaster = value.prMasterId;
					itemCode = value.itemCode;
					console.log("prMaster---"+prMaster+'===itemCode ::'+itemCode);
					 
				});
				location.href="./inventoryDetails?token=view&inventoryId="+prMaster + "&itemCode=" + itemCode;
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
    	ondblClickRow: function(rowid) {
    		var rowData = jQuery(this).getRowData(rowid); 
			var prMasterID = rowData['prMasterId'];
			var ItemCode = rowData['itemCode'];
			var checkpermission=getGrantpermissionprivilage('Inventory',0);
			if(checkpermission){
				document.location.href = "./inventoryDetails?token=view&inventoryId="+prMasterID+"&itemCode="+ItemCode;
			}
	    	return true;
    	}
	}).navGrid('#inventoryValueGridpager',
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
$( "#searchJob" ).autocomplete({ minLength: 2,timeout :1000,
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
function getSearchDetails()
{
	/*warehouse = $('#bankAccountsID').val()
	console.log("ItemCode is--------------------->"+$('#searchJob').val());
	if($('#searchJob').val() != null && $('#searchJob').val() != '')
	{
		location.href="./inventory?token=view&key="+$('#searchJob').val();
		return true;
	}
	else
		return false;*/
	
	if($('#searchJob').val() != null && $('#searchJob').val() != '')
	{
		$("#searchJob").autocomplete({
			disabled: true
		});
		searchData = $('#searchJob').val();
		$("#inventoryGrid").jqGrid('GridUnload');
		loadInventoryGrid();
		//$("#inventoryValueGrid").trigger("reloadGrid");
		$('#searchJob').val('');
	}
		
}
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
	var checkpermission=getGrantpermissionprivilage('Inventory',0);
	if(checkpermission){
	document.location.href = "./inventoryDetails?token=new";
	}
	return true;
}

/***
 * ShowInactiveList() - Showing inactive
 * 
 */
function ShowInventoryList(){
	
	
	
	
	if(warehouse==0){
		return false;
	}
	jQuery("#inventoryValueGrid")
    .jqGrid()
    .setGridParam({
        url : './inventoryList?itemCode=&Inactive=&Warehouse='+warehouse+"&page=1&rows=50"
    })
    .trigger("reloadGrid");
		
}

function ShowInactiveList(){
//	alert(jQuery(this).val());
	var warehouse = $('#bankAccountsID').val();
	
	
	if(warehouse==0){
		return false;
	}
	
	
	var checked =0;
	var inactive = $('#inactivelist').prop('checked');	
	if(inactive==false){
		checked =0;
	}else{
		checked =1;
	}
	var itemcodevalue='';
	
	//jQuery("#inventoryGrid").jqGrid('GridUnload');
	//loadInventoryValueGrid();
	//jQuery("#inventoryGrid").trigger("reloadGrid");
	
	jQuery("#inventoryGrid").jqGrid().setGridParam({url : './inventoryList?itemCode=&Inactive='+checked+'&Warehouse='+warehouse}).trigger("reloadGrid", [{page:1}]);
	
	/*jQuery('#inventoryGrid').jqGrid('setGridParam',{postData:{'Inactive':checked,'Warehouse':warehouse}});
	jQuery('#inventoryGrid').trigger("reloadGrid");*/
		
}