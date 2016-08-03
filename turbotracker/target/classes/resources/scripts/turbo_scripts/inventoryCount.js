jQuery(document).ready(function(){
var checkRefs="";
	//$("#LoadingImage").show();
	/* $.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		'z-index':'1010px',
		color: '#fff'
	}});
	*/
	$.ajax({
        url: './getWarehouses',
        type: 'GET',       
        success: function (data) {
        	//console.log("all--->"+data);
        	checkRefs="<option value='0'>-Select-</option>";
			$.each(data, function(key, valueMap) {								
					if("wareHouseList" == key)
					{
						$.each(valueMap, function(index, value){
							if(value.searchName != null && value.searchName.trim() != '')
								checkRefs+='<option value='+value.prWarehouseId+'>'+value.searchName+'</option>';
						
						}); 
					} 
			});
			//$("#LoadingImage").hide();
			$('#warehouseListID').html(checkRefs);
			//setTimeout($.unblockUI, 500);
        }
    });
    
	//$("input:text").addClass("ui-state-default ui-corner-all");
	
}); 
function changeheightbasedonsord(){
		var sortID = $("#sortId").val();
		if(sortID==2){
			return 1180;
		}else if(sortID==3){
			return 1440;
		}else if(sortID==4){
			return 1560;
		}else{
			return 1150;
		}
}
function getCountInventoryGrid(){
	$("#jqGrid").empty();
	$("#jqGrid").append("<table id='CountInventoryGrid'></table><div id='CountInventoryGridPager'></div>");
	var wareHouseID = $("#warehouseListID").val();
	var sortValue = $("#sortId").val();
	//alert(sortValue);
	
	$("#CountInventoryGrid").jqGrid({
		url:'./product/countInventory?wareHouseID='+wareHouseID+'&sortValue='+sortValue,
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#CountInventoryGridPager'),
		colNames:['PrWarehouseInventoryID' ,'prMasterId', 'ItemCode', 'Description', 'Primary Vendor','rDepartmentId','prCategoryId','Department', 'Category', 'On Hand','update count','Counted'],
		colModel :[
		    {name:'prWarehouseInventoryID', index:'prWarehouseInventoryID', align:'left', width:40, editable:true, editrules:{required:true},hidden:true},
           	{name:'prMasterId', index:'prMasterId', align:'left', width:40, editable:true, editrules:{required:true},hidden:true},
			{name:'itemCode', index:'itemCode', align:'left', width:40,hidden:false,editable:false, editrules:{required:true}},
			{name:'description', index:'description', align:'', width:80,hidden:false,editable:false, editrules:{required:true}},
			{name:'primaryVendorName', index:'primaryVendorName', align:'left', width:90,hidden:false},
			{name:'rDepartmentId',index:'rDepartmentId', width:100,editable:false, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prCategoryId',index:'prCategoryId', width:100,editable:false, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prDepartment', index:'prDepartment', align:'', width:40,hidden:false},
			{name:'prCategory', index:'prCategory', align:'left', width:50, hidden:false},
			{name:'inventoryOnHand', index:'inventoryOnHand', align:'center', width:30,hidden:false,editable:false, editrules:{required:true}},
			{name:'inventoryCounted', index:'inventoryCounted', align:'center', width:30,hidden:true,editable:true, editrules:{required:true},editoptions:{size:10}},
			{name:'Counted', index:'Counted', align:'center', width:30,hidden:false,editable:true, editoptions:{size:10
					
			}}
		],
		altRows: true, 
		altclass:'myAltRowClass',
		cellEdit: true,
		cellsubmit : 'clientArray',
        afterSaveCell: function(rowid,name,val,iRow,iCol) {
        	$('#saveCountInventoryButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
        	 $( "#saveCountInventoryButtonID" ).prop( "disabled", false );
        },
        afterRestoreCell: function (rowid, value, iRow, iCol) {
        	 $('#saveCountInventoryButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
        	 $( "#saveCountInventoryButtonID" ).prop( "disabled", true );
        },
		beforeEditCell : function(rowid, cellname, value, iRow, iCol) {
			 $('#saveCountInventoryButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
        	 $( "#saveCountInventoryButtonID" ).prop( "disabled", true );
		},
		afterEditCell: function(rowid, cellname, value, iRow, iCol) {

		    // Get a handle to the actual input field
		    var inputControl = jQuery('#' + (iRow) + '_' + cellname);

		    // Listen for enter (and shift-enter)
		    inputControl.bind("keydown",function(e) {

		      if (e.keyCode === 9) {  // Enter key:
		        var increment = e.shiftKey ? -1 : 1;

		        // Do not go out of bounds
		        var lastRowInd = jQuery("#CountInventoryGrid").jqGrid("getGridParam","reccount")
		        if ( iRow+increment == 0 || iRow+increment == lastRowInd+1)
		          return;   // we could re-edit current cell or wrap
		        else
		          jQuery("#CountInventoryGrid").jqGrid("editCell",iRow+increment,iCol,true);
		      }
		      
		      if(e.keyCode === 13){
		    	  $('#saveCountInventoryButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
             	 $( "#saveCountInventoryButtonID" ).prop( "disabled", false );
		      }
		    }); // End keydown binding
		  },
		caption: 'Counting Inventory',
		height:1150,
		imgpath: 'themes/basic/images',
		pgbuttons: false,	
		viewrecords: false,
		pager: '#CountInventoryGridPager',
		multiSort:true,
		sortable: true,
		sortname: 'description',
		sortorder: 'asc',
		width: 940,
		rownumbers:true, 
		rowNum: -1,
		rownumWidth: 45,
		recordtext: '',
		rowList: [],
		pgtext: null,
		loadBeforeSend: function(xhr) {
			$('#loadingDivForInventoryCount').css({"visibility": "visible","z-Index":"1234","display":"block","height":"1678px"});
		},
		loadComplete: function(data) {
			
		},
		gridComplete: function () {
			editInventoryCountGlobalForm = JSON.stringify($('#CountInventoryGrid').getRowData());
			$('#loadingDivForInventoryCount').css({"visibility": "hidden","z-Index":"1234","display":"none"});
		},
		loadError : function (jqXHR, textStatus, errorThrown) {},
		ondblClickRow: function(rowId) {
			/*jQuery('#CountInventoryGrid').jqGrid('editGridRow', rowId,
	    			{
	    			recreateForm:true,
	    			closeAfterEdit:true,
	                closeOnEscape:true,
	                reloadAfterSubmit:false,
	                });*/
			//inventoryCounted =$("#inventoryCounted").val();
	     	//prMasterId =$("#prMasterId").val();
	        //alert('./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID);
			//$("#CountInventoryGrid").setGridParam({'editurl':'./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID});
		
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
	}).navGrid('#CountInventoryGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:false,search:false},{/*

			closeOnEscape: true,
			closeAfterEdit:true, 
			reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Invenotry Count",
			beforeShowForm:function(formid) {
				},
			onclickSubmit: function(params){
				inventoryCounted =$("#inventoryCounted").val();
		     	prMasterId =$("#prMasterId").val();
		     	alert(inventoryCounted);
		     	//alert('./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID);
				//$("#CountInventoryGrid").setGridParam({'editurl':'./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID});
				return [true, ""];
			},
		*/
	
		}
	);
	$("#pdfCountInventoryButton").css("display","block");
	$("#saveCountInventoryButton").css("display","block");
	//$("#getPdfButton").attr("disabled","disabled");
    //$('#getPdfButton').removeAttr('disabled');
}


function SaveCloseOrderPoints(){
	var editCountInventoryGrid = $('#CountInventoryGrid').getRowData();
	var	editInventoryCountGridData = JSON.stringify(editCountInventoryGrid);
	if(editInventoryCountGlobalForm != editInventoryCountGridData)
	{
	$("#CountInventoryGrid").trigger("reloadGrid");
	var validateMsg = "<b style='color:Green; align:right;'>Saved Successfully.</b>";	
	$("#invTypeMsg").css("display", "block");
	$('#invTypeMsg').html(validateMsg);
	setTimeout(function(){
	$('#invTypeMsg').html("");						
	},2000);
	}
}
function SaveOrderPoints(){
	var warehouseListID=$("#warehouseListID").val();
	var sortList=$("#sortList").val();
	var gridRows = $('#CountInventoryGrid').getRowData();
	var dataToSend = JSON.stringify(gridRows);
	
	
	
	if(editInventoryCountGlobalForm != dataToSend)
	{
		$('#loadingDivForInventoryCount').css({"visibility": "visible","z-Index":"1234","display":"block","height":"1678px"});
		$.ajax({
 		url: "./product/SaveInventoryCount",
 		type: "POST",
 		data : {"warehouseListID":warehouseListID,"gridData":dataToSend},
 		success: function(data) {
 		$("#CountInventoryGrid").trigger("reloadGrid");
 		var validateMsg = "<b style='color:Green;align:right;'>Saved Successfully.</b>";	
 		$("#invTypeMsg").css("display", "block");
 		$('#invTypeMsg').html(validateMsg);
 		setTimeout(function(){
 		$('#invTypeMsg').html("");						
 		},2000);
 		}
 	});
	}
	
	
}

function getPDF()
{
	var aSelectedRowId = $("#CountInventoryGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId != null){
	var rowId = $("#CountInventoryGrid").getRowData(aSelectedRowId);
	var prTransferId = rowId['prMasterId'];
	var prWarehouseInventoryID = rowId['prWarehouseInventoryID'];
	//alert(prWarehouseInventoryID);
	var aQryStr = "prMasterId=" + prTransferId+"&prWarehouseInventoryID="+prWarehouseInventoryID;
	console.log(aQryStr+"-----------"+name);
	var checkpermission=getGrantpermissionprivilage('Count',0);
	if(checkpermission){
		
		window.open( "./product/getInventoryCountPDF?" + aQryStr);
	}
	}
	else{
		alert('Please select any record before print');
	}
	//document.location.href = "./warehouseTransfer";
}

function getCSV(type)
{
	var aSelectedRowId = $("#CountInventoryGrid").jqGrid('getGridParam', 'selrow');
	/*if(aSelectedRowId != null){*/
	var rowId = $("#CountInventoryGrid").getRowData(aSelectedRowId);
	var prTransferId = rowId['prMasterId'];
	var prWarehouseInventoryID = rowId['prWarehouseInventoryID'];
	//alert(prWarehouseInventoryID);
	var docTypes = 0;
	if(type=='pdf'){
		docTypes=1;
	}else if(type=='csv'){
		docTypes=2;
	}else{
		docTypes=1;
	}
	var prWarehouseID = $("#warehouseListID").val();
	var aQryStr = "prWarehouseInventoryID="+prWarehouseID+"&docType="+docTypes;
	console.log(aQryStr+"-----------"+name);
	/*var checkpermission=getGrantpermissionprivilage('Count',0);
	if(checkpermission){*/
	window.open( "./product/getInventoryCountCSV?" + aQryStr);
	/*}
	}
	else{
		alert('Please select any record before print');
	}*/
	//document.location.href = "./warehouseTransfer";
/*}*/
}

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