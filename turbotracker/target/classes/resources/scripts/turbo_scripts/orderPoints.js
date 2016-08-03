var searchData = '';
var searchstring='';
jQuery(function () {
	//$("#searchJob").prop('id', 'searchProduct');
	//$("#searchJob").removeClass("ui-autocomplete-input");
	//$("#searchJob").removeClass("ui-autocomplete-loading");
	jQuery( "#suggestedReOrderPopUpDialog" ).dialog({
		autoOpen: false,
		height: 560,
		width: 960,
		//top: 55,
		left: 185,
		title:"Suggested Reorder",
		modal: true,
		buttons:{		},
		close: function () {
			return true;
		}
	});
	return true;
	
	
});


$(function() { var cache = {}, lastXhr;
$( "#searchJob" ).autocomplete({ minLength: 2,timeout :1000,
	select: function (event, ui) {
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	}  }); });

function ResetDetails(){
	searchstring="";
	$('#searchJob').val('');
	$("#resetbutton").show();
	$('#date').datepicker();
	$("#chartsOfOrderPointsGrid").jqGrid('GridUnload');
	$('#chartsOfWarehouseGrid').setSelection(1,true);
	var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(1);    		
	loadOrderPointsDetails(rowData['prWarehouseId'], '');
	$("#chartsOfOrderPointsGrid").trigger("reloadGrid");
	return true;
}


jQuery(document).ready(function() {
	$('#search').css('visibility','visible');
	$("#searchJob").attr("placeholder","Minimum 2 characters required to get List");
	$("#resetbutton").show();
	$('#date').datepicker();
	loadWareHouseGridAndOrderPointsDetails();
	return true;

});

function loadWareHouseGridAndOrderPointsDetails() {
	$("#chartsOfWarehouseGrid").jqGrid({
		datatype: 'json',
		mtype: 'GET',
		url:'./product/warehouseGrid',
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['prWarehouseId', 'searchName', 'description', 'inActive', 'address1', 'address2', 'city',
		          'state', 'zip', 'coAccountIdasset', 'coTaxTerritoryId', 'printerName', 'email', 'isLaser', 'PickTicketInfo','PrinterCopies'],
	   	colModel:[
			{name:'prWarehouseId',index:'prWarehouseId', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'searchName',index:'searchName',align:'left', width:100,editable:true, editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'inActive',index:'inActive', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'address1',index:'address1', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'address2',index:'address2', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'city',index:'city', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'state',index:'state', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'zip',index:'zip', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'coAccountIdasset',index:'coAccountIdasset', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'coTaxTerritoryId',index:'coTaxTerritoryId', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'printerName',index:'printerName', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}, hidden:true},
			{name:'email',index:'email', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'isLaser',index:'isLaser', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'PickTicketInfo',index:'PickTicketInfo', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
			{name:'PrinterCopies',index:'PrinterCopies', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true}
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
		caption: 'Warehouse',
		height:425,	width: 200,/*scrollOffset:0,*/
		rownumbers:true,rownumWidth:34,
		loadonce: false,
		loadComplete:function(data) {
			searchData = '';
			$('#chartsOfWarehouseGrid').setSelection(1,true);
			var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(1);    		
    		loadOrderPointsDetails(rowData['prWarehouseId'], searchData);
	    	
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
    		$("#searchJob").val('');
			searchData = $("#searchJob").val();
    		var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(rowId);    		
    		loadOrderPointsDetails(rowData['prWarehouseId'], searchData);
    		$("#suggestedButton").attr("onclick", "showSuggestedReOrderPopUp("+rowData['prWarehouseId']+", '')");
    	},
    	ondblClickRow: function(rowId) {
    		
		}
	})/*.navGrid('#chartsOfTermsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
}

var newDialogDiv = jQuery(document.createElement('div'));
var prMasterId ="";
var inventoryOrderPoint = "";
var inventoryOrderQuantity = "";
var editUrl = "";

function loadOrderPointsDetails(prwarid, searchData){
	searchstring=$('#searchJob').val();
	$("#jqGrid").empty();
	$("#jqGrid").append("<table id='chartsOfOrderPointsGrid'></table><div id='chartsOfOrderPointsGridPager'></div>");
	var rowData = "";
	
	var aWareHouseID = prwarid;	
	$("#chartsOfOrderPointsGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./product/getOrderPoints?wareHouseID='+aWareHouseID+"&searchData="+searchData,
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['prMasterId', 'ItemCode', 'Description','Department', 'Category', 'Available', 'On Order',
		          'Order Point', 'Order Quantity'],
	   	colModel:[
			{name:'prMasterId',index:'prMasterId', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'itemCode',index:'itemCode',align:'left', width:140,editable:false, editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:200,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'prDepartment',index:'prDepartment', width:110,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'prCategory',index:'prCategory', width:100,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'inventoryAvailable',index:'inventoryAvailable',align:'center', width:100,editable:false, editrules:{required:false}, editoptions:{size:10}},
			{name:'inventoryOnOrder',index:'inventoryOnOrder',align:'center', width:100,editable:false, editrules:{required:true}, editoptions:{size:10}, hidden:false},
			{name:'inventoryOrderPoint',index:'inventoryOrderPoint',align:'center', width:100,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inventoryOrderQuantity',index:'inventoryOrderQuantity',align:'center', width:125,editable:true, editrules:{required:true}, editoptions:{size:10}}
		],
		
		recordtext: '',
		cellEdit: true,
		cellsubmit : 'remote',
		//editurl:'./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID,
	    //cellurl : './product/insertOrderPoints?prMasterId='+'2'+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity,
		rowList: [50,100,250,500,1000,2000,5000],
		viewrecords: true,
		rowNum: 50,
		pgbuttons: true,
		pager: '#chartsOfOrderPointsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Order Points',
		height:398,	width: 900,/*scrollOffset:0,*/rownumbers:false,rownumWidth:0,
		loadonce: false,
		beforeSubmitCell : function(rowid,cellname,value,iRow,iCol) {
	    	rowData = jQuery("#chartsOfOrderPointsGrid").getRowData(rowid);
			var prMasterId = rowData['prMasterId'];
			
			var inventoryOrderQuantity=rowData['inventoryOrderQuantity'];
//			if(cellname=="inventoryOrderQuantity"){
//				inventoryOrderQuantity=value;
//			}
			
			var inventoryOrderPoint=rowData['inventoryOrderPoint'];
//			if(cellname=="inventoryOrderPoint"){
//				inventoryOrderPoint=value;
//			}
			
				$("#chartsOfOrderPointsGrid").setGridParam({'cellurl':'./product/insertOrderPoints?prMasterId='+prMasterId+'&cellname='+cellname+'&value='+value+'&aWareHouseID='+aWareHouseID});
				createtpusage('Inventory-Order Points','Order Points Grid Save','Info','Inventory,Order Points Grid Save,prMasterId:'+prMasterId+',aWareHouseID:'+aWareHouseID);
	     	//alert('./product/insertOrderPoints?prMasterId='+prMasterId+'&cellname='+cellname+'&value='+value+'&aWareHouseID='+aWareHouseID);
	     	
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
		onSelectRow:  function(id){},
		onCellSelect : function (rowid,iCol, cellcontent, e) {}
//    	ondblClickRow: function(rowid,icol,cellcontent,e) {
//	    	jQuery('#chartsOfOrderPointsGrid').jqGrid('editGridRow', rowid,
//			{
//				recreateForm:true,
//				closeAfterEdit:true,
//	            closeOnEscape:true,
//	            reloadAfterSubmit:false,
//            });
//	    	var rowId = jQuery("#chartsOfWarehouseGrid").jqGrid('getGridParam','selrow');
//	    	alert(" >>>><<<< "+rowId);
//	    	rowData = jQuery("#chartsOfOrderPointsGrid").getRowData(rowId);
//			inventoryOrderPoint = $("#"+rowId+"_inventoryOrderPoint").text();
//	     	inventoryOrderQuantity = $("#"+rowId+"_inventoryOrderQuantity").text();
//	     	prMasterId = rowData['prMasterId'];
//	     	alert('>>>>>>>>>> ./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID);
//	    	$("#chartsOfOrderPointsGrid").setGridParam({'editurl':'./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID});
//		}
		}).navGrid('#chartsOfOrderPointsGridPager',	{add:false,edit:true,del:false,refresh:false,search:false},	{
			closeOnEscape: true,
			closeAfterEdit:true, 
			reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Order Points",
			beforeShowForm:function(formid) {},
			onclickSubmit: function(params){
					//rowData = jQuery("#chartsOfOrderPointsGrid").getRowData(rowid);
					//if(iCol==8){
					//inventoryOrderPoint = $("#"+rowid+"_inventoryOrderPoint").val();
//						inventoryOrderQuantity = $("#inventoryOrderQuantity").val();
					//}else{
//						inventoryOrderPoint =$("#inventoryOrderPoint").val();
				     //	inventoryOrderQuantity = $("#"+rowid+"_inventoryOrderQuantity").val();
					//}
					//alert(aWareHouseID);
//				     	prMasterId =$("#prMasterId").val();
			     	var rowId = jQuery("#chartsOfWarehouseGrid").jqGrid('getGridParam','selrow');   
					var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(rowId);
					inventoryOrderPoint = $("#"+rowid+"_inventoryOrderPoint").text();
			     	inventoryOrderQuantity = $("#"+rowid+"_inventoryOrderQuantity").text();
			     	prMasterId = rowData['prMasterId'];
			     	//alert(' ??????????? ./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID);
		    		$("#chartsOfOrderPointsGrid").setGridParam({'editurl':'./product/insertOrderPoints?prMasterId='+prMasterId+'&inventoryOrderPoint='+inventoryOrderPoint+'&inventoryOrderQuantity='+inventoryOrderQuantity+'&aWareHouseID='+aWareHouseID});
					return [true, ""];
			}			
		}	
	);
	$("#showOrderPointsButtons").css("display","block");
	return true;
}

function getSearchDetails() {
	console.log("ItemCode is = " + $('#searchJob').val());
	if ($('#searchJob').val() != null && $('#searchJob').val() != '') {
		searchData = $('#searchJob').val();
		$("#chartsOfOrderPointsGrid").jqGrid('GridUnload');
		var rowId = jQuery("#chartsOfWarehouseGrid").jqGrid('getGridParam','selrow');   
		var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(rowId);	
		loadOrderPointsDetails(rowData['prWarehouseId'], searchData);
		$("#chartsOfOrderPointsGrid").trigger("reloadGrid");
		$('#searchJob').val("");
		return true;
	} else
		return false;

}

function showSuggestedReOrderPopUp(prwarid, searchData){
	$("#suggestedReOrderPopUpDialog").empty();
	$("#suggestedReOrderPopUpDialog").append("<input id='orderPointPrint' onclick='exportOrderPoints();' type='button' value='Print' class='turbo-blue savehoverbutton' style='margin-right: 120px; font-size: 15px;float:right'/><br><br><table id='suggestedReOrderPopUpDialogBox'></table><div id='suggestedReOrderPopUpDialogBoxPager'></div>");
	var rowData = "";
	var aWareHouseID = prwarid;	
	$("#suggestedReOrderPopUpDialogBox").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:"./product/getSuggestedOrderPoints?wareHouseID="+aWareHouseID+"&searchData="+searchstring,
		//pager: jQuery('#chartsOfTermsGridPager'),
		colNames:['prMasterId', 'ItemCode', 'Description','Box Qty', 'On Hand', 'Allocated', 'Available', 'On Order',
		          'Projected Qty', 'Order Point', 'Suggested Order', 'YTD'],
	   	colModel:[
	   	    {name:'prMasterId',index:'prMasterId',  width:140,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'itemCode',index:'itemCode',align:'left', width:140,editable:false, editrules:{required:false}, editoptions:{size:10}},
			{name:'description',index:'description', width:200,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'boxQty',index:'boxQty', width:100,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'inventoryOnHand',index:'inventoryOnHand', width:110,editable:false, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'inventoryAllocated',index:'inventoryAllocated',align:'center', width:120,editable:false, editrules:{required:false}, editoptions:{size:10}},
			{name:'inventoryAvailable',index:'inventoryAvailable',align:'center', width:120,editable:false, editrules:{required:false}, editoptions:{size:10}},
			{name:'inventoryOnOrder',index:'inventoryOnOrder',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inventoryProjected',index:'inventoryProjected',align:'center', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'inventoryOrderPoint',index:'inventoryOrderPoint',align:'center', width:140,editable:false, editrules:{required:true}, editoptions:{size:10}, hidden:false},
			{name:'inventorySuggestedOrder',index:'inventorySuggestedOrder',align:'center', width:150,editable:true, editrules:{required:true}, editoptions:{size:10}},
			{name:'ytd',index:'ytd',align:'center', width:90,editable:true, editrules:{required:true}, editoptions:{size:10}}
		],
		
		recordtext: '',
		rowList: [50,100,250,500,1000,2000,5000],
		viewrecords: true,
		rowNum: 50,	
		pgbuttons: true,
		pager: '#suggestedReOrderPopUpDialogBoxPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Order Points',
		height:398,	width: 935,rownumbers:false,rownumWidth:10,
		loadonce: false,
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
		loadComplete:function(data) {},
    	loadError : function (jqXHR, textStatus, errorThrown){	}
	});
	$("#showOrderPointsButtons").css("display","block");
	jQuery( "#suggestedReOrderPopUpDialog" ).dialog("open");
	return true;
}

function exportOrderPoints()
{
	var rowIdd = jQuery("#chartsOfWarehouseGrid").jqGrid('getGridParam','selrow');
	var rowData = jQuery("#chartsOfWarehouseGrid").getRowData(rowIdd);    
	window.open("./inventoryList/printOrderPoint?inactive=0&itemCode="+searchstring+"&warehouseID="+rowData['prWarehouseId']);
	return false;
	
}

function SaveCloseOrderPoints(){
	$("#chartsOfOrderPointsGrid").trigger("reloadGrid");
}

function SaveOrderPoints(){
	$("#chartsOfOrderPointsGrid").trigger("reloadGrid");
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

