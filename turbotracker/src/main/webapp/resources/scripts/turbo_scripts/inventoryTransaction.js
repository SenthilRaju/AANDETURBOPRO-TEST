    var awarehouseListID = '';
	var afromDateID = '';
	var atoDateID='';
	var aprMasterID='';

jQuery(document).ready(function(){
  
	 awarehouseListID = $('#warehouseListID').val();
	 afromDateID = $('#fromDateID').val();
	 atoDateID=$('#toDateID').val();
	 aprMasterID=$('#prMasterID').val();
	 
	 $("#InventoryTransactionGrid").jqGrid({
			url:'./inventoryList/receiveInventoryTransaction',
			datatype: 'JSON',
			postData: {'warehouseListID' : function(){ return awarehouseListID;},'fromDateID':function(){ return afromDateID;},'toDateID':function(){ return atoDateID;},'prMasterID':aprMasterID},
			mtype: 'POST',
			pager: jQuery('#InventoryTransactionPager'),
			colNames:['vePOID','Date','Trans #', 'Transaction Type', '# In', '# Out','Running Balance','Total Cost'],
			colModel :[
		        {name:'vePOID', index:'vePOID', align:'', width:20,hidden:true},
				{name:'createdOn', index:'createdOn', align:'center', width:23,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
				{name:'ponumber', index:'ponumber', align:'center', width:30,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
				{name:'jobName', index:'jobName', align:'left', width:45,hidden:false},
				{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:15, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
				{name:'difference', index:'difference', align:'center', width:15,hidden:false},
				{name:'inventoryOnHand', index:'inventoryOnHand', align:'center', width:40,hidden:false},
				{name:'subtotal', index:'subtotal', align:'right', width:20,hidden:false,formatter:customCurrencyFormatter}
			],
			pager: '#InventoryTransactionPager',
			//sortname: 'TranDate', 
			//sortorder: "asc",
			altRows:true,
			altclass:'myAltRowClass',
			height:450,
			imgpath:'themes/basic/images',
			rowNum: -1,
			recordtext:'',
			rownumbers:true,
		 	width:800,
		    viewrecords: false,
		    loadonce: false,
		    rowList: [],        // disable page size dropdown
		    pgbuttons: false,     // disable page control like next, back button
		    pgtext: null,         // disable pager text like 'Page 0 of 10'
		    viewrecords: false ,
		    rownumWidth:34,
			loadComplete: function(data) {
				
		    },
			loadError : function (jqXHR, textStatus, errorThrown) {
			    
			},
			ondblClickRow: function(rowId) {
				
			},
			jsonReader : {
				root:"rows",
				page:"page",
				total:"total",
				records:"records",
				repeatitems:false,
				cell:"cell",
				id:"id",
				userdata:"userdata"}
		});

//showInventoryTransDeatils(warehouseListID,fromDateID,toDateID,prMasterID);

});

function ResetDetails() {
	$('#warehouseListID').val("");
	$('#fromDateID').val("");
    $('#toDateID').val("");
    $('#prMasterID').val("");
    $("#InventoryTransactionGrid").jqGrid("clearGridData", true);
  
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


function getInventoryTrans(){
	createtpusage('Inventory-Transactions','Inventory Transaction Grid View','Info','Inventory,Generating Inventory Transactions View,warehouseListID:'+ $('#warehouseListID').val()+',aprMasterID:'+$('#prMasterID').val());
	 awarehouseListID = $('#warehouseListID').val();
	 afromDateID = $('#fromDateID').val();
	 atoDateID=$('#toDateID').val();
	 aprMasterID=$('#prMasterID').val();

$("#InventoryTransactionGrid").trigger("reloadGrid");
	
}





