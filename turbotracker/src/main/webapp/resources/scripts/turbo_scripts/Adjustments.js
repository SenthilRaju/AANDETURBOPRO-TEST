jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	$('#date').datepicker();
	$("#chartsOfTransferInventoryGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./product/transferInventoryGrid',
		//pager: jQuery('#chartsOfTermsGridPager'),
		
		colNames:['prTransferId','prFromWarehouseId', 'prToWarehouseId', 'Reference','Transfer Date'],
	   	colModel:[
			{name:'prTransferId',index:'prTransferId', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'prFromWarehouseId',index:'prFromWarehouseId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prToWarehouseId',index:'prToWarehouseId', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'description',index:'description', width:200,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'transferDate',index:'transferDate',align:'left', width:120,editable:true, editrules:{required:true}, editoptions:{size:10},hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}}
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
		caption: 'Transfer',
		height:425,	width: 350,/*scrollOffset:0,*/
		rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			$('#chartsOfTransferInventoryGrid').setSelection(1,true);
			var rowData = jQuery("#chartsOfTransferInventoryGrid").getRowData(1);   		
			loadTransferDetails(rowData['prTransferId']);
    		
	    	
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
    	gridComplete: function () {
    	
    	},
    	onSelectRow: function(rowId){
    		var rowData = jQuery("#chartsOfTransferInventoryGrid").getRowData(rowId);    		
    		loadTransferDetails(rowData['prTransferId']);
    	},
    	ondblClickRow: function(rowId) {
  
		}
	})/*.navGrid('#chartsOfTransferInventoryGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true; 

});

var newDialogDiv = jQuery(document.createElement('div'));
var prMasterId ="";
var inventoryOrderPoint = "";
var inventoryOrderQuantity = "";
var editUrl = "";
var wareHouse="";

function loadTransferDetails(transferId){
	if(isNaN(transferId)||transferId==undefined){
		transferId=0;
	}
	$.ajax({
        url: './product/getAdjustmentDetails?transferID='+transferId,
        type: 'POST',       
        success: function (data) {
        	console.log("all--->"+data);
        	wareHouse = "";
			$.each(data, function(key, valueMap) {
				if("transferList"==key)
				{				
					billToCustomer = valueMap;		
					if(valueMap.length==0){
						$('#transferDateID').val("");
					}
					$.each(valueMap, function(index, value){
						$('#transferDateID').val(value.transferDate);
						$('#referenceID').val(value.description);					
					}); 
				}
				if("Warehouses" == key)
				{
					customerMasterObj = valueMap;
					$.each(valueMap, function(index, value){
						if(value.searchName != null && value.searchName.trim() != '')
							wareHouse+='<option value='+value.prWarehouseId+'>'+value.searchName+'</option>';
					
					}); 
				}
				$('#warehouseListID').html(wareHouse);
			});
        }
    });
	
	$("#jqGrid").empty();
	$("#jqGrid").append("<table id='chartsOfTransferListGrid'></table><div id='chartsOfTransferListGridPager'></div>");
	$("#chartsOfTransferListGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./product/getTransferdetails?transferID='+transferId,
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['prTransferDetailId', 'prTransferId', 'prMasterId','Product No','description', 'quantityTransfered'],
	   	colModel:[
			{name:'prTransferDetailId',index:'prTransferDetailId', width:100,editable:false, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'prTransferId',index:'prTransferId',align:'left', width:79,editable:false, editrules:{required:true},hidden:true, editoptions:{size:10}},
			{name:'prMasterId',index:'prMasterId', width:250,editable:false, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prMasterId',index:'prMasterId', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'quantityTransfered',index:'quantityTransfered', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}}
			],
		rowNum: 50,	
		pgbuttons: true,
		recordtext: '',
		cellEdit: false,
		cellsubmit : 'remote',
		//editurl:'./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID,
	    //cellurl : './product/insertOrderPoints?prMasterId='+'2'+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity,
		rowList: [50,100,250,500,1000,2000,5000],
		viewrecords: true,
		pager: '#chartsOfTransferListGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Transfer Items',
		height:235,	width: 680,/*scrollOffset:0,*/rownumbers:true,rownumWidth:30,
		loadonce: false,
		 beforeSubmitCell : function(rowid,cellname,value,iRow,iCol) { 
			 },
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
            userdata: "userdata",
           sortorder: "asc",	
           imgpath: 'themes/basic/images'
    	},
    	loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow:  function(id){
		},
		onCellSelect : function (rowid,iCol, cellcontent, e) {
    		
			},
    	ondblClickRow: function(rowid,icol,cellcontent,e) {
    }
	}).navGrid('#chartsOfTransferListGridPager',
		{add:true,edit:true,del:true,refresh:true,search:false},
	{
			closeOnEscape: true,
			closeAfterEdit:true, 
			reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Order Points",
			beforeShowForm:function(formid) { 
					
				},
			onclickSubmit: function(params){
			},
		
	}	
	);
	$("#showOrderPointsButtons").css("display","block");
	return true;
}

function SaveCloseOrderPoints(){
	$("#chartsOfTransferListGrid").trigger("reloadGrid");
}
function SaveOrderPoints(){
	$("#chartsOfTransferListGrid").trigger("reloadGrid");
}
function editData() {
	var additionalInfo = $("#paymentTermsFromID").serialize();
	$.ajax({
		url: "./editTerms",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Terms are saved Successfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
		buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close");
		location.reload(true);
		document.getElementById("paymentTermsFromID").reset(); 
		location.reload(true); }}]}).dialog("open");*/
			$("#chartsOfTermsGrid").trigger("reloadGrid");
				var validateMsg = "<b style='color:Green;'>Payment Terms are saved Successfully.</b>";	
				$('#paymentTermsMsg').html(validateMsg);
				setTimeout(function(){
				$('#paymentTermsMsg').html("");						
				},3000);
			$('#settings_tabs').tabs({ selected: 1 });
			$('#settings_tabs').tabs({ active: 1 });
		}
	});
}

function addData() {
	var additionalInfo = $("#addNewTermsFormID").serialize();
	jQuery("#addNewTermsDialog").dialog("close");
	$.ajax({
		url: "./editTerms",
		type: "POST",
		data : additionalInfo,
		success: function(data) {
			/*jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Terms are saved Sucessfully.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
			buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close"); jQuery("#addNewTermsDialog").dialog("close");
			location.reload(true);
			document.getElementById("addNewTermsDialog").reset(); location.reload(true); }}]}).dialog("open");
*/
			$("#chartsOfTermsGrid").trigger("reloadGrid");
			$("#paymentTermsMsg").css({display: "block"});
			var validateMsg = "<b style='color:Green;'>Payment Terms are saved Successfully.</b>";	
			$('#paymentTermsMsg').html(validateMsg);
			setTimeout(function(){
			$('#paymentTermsMsg').html("");						
			},3000);
		}
	});
}

