var chartsOfTransferGridid;
jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	$('#date').datepicker();
	
	loadtransferInventoryGrid();
	loadTransferDetails('');
	
	$("#chartsOfTransferListGrid_ilsave").click(function() {
		var ids = $("#chartsOfTransferListGrid").jqGrid('getDataIDs');
		var veaccrrowid;
		if(ids.length==1){
			veaccrrowid = 0;
		}else{
			var idd=1;
			for(var i=0;i<ids.length;i++){
				if(idd<=ids[i]){
					idd=ids[i];
				}
			}
			 veaccrrowid= idd;
		}
		$("#" + chartsOfTransferGridid).attr("id", Number(veaccrrowid)+1);
		
		// $("#chartsOfTransferListGrid").jqGrid('resetSelection');
		
		if($("#info_head").text()!="Error")
		{
			$('#saveIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
			$('#clearIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
			document.getElementById("saveIAButtonID").disabled = false;
			document.getElementById("clearIAButtonID").disabled = false;
		}
	});
	
	
	
	
	$("#warehouseListID").change(function(){
		$("#cogErrorStatus").html("");
		$("#cogErrorStatustextbox").val("");
		
		if($(this).val()!=0)
		{
		$.ajax({
			url: "./product/getAdjustmentCOGInfoDetails",
			type: "GET",
			data : {wareHouseId:$(this).val()},
			success: function(data) {
				
				if(data == "Failed")
				{
				$("#cogErrorStatus").html("COG Account is not set yet. Please contact Admin.");
				$("#cogErrorStatustextbox").val("COG Account is not set yet. Please contact Admin.");
				setTimeout(function() {
	                $('#cogErrorStatus').html("");
	                }, 5000);
				}
			}
		});
		}
	})	
	
});




function ClearAdjustments(){
	$('#transferId').val('');
	$('#transferDateID').val('');
	$('#referenceID').val('');
	$('#glAccountListID').val('');
	$('#warehouseListID').val('');
	$('#reasonCodeID').val('');
	loadTransferDetails('');
	 $("#saveIAButtonID").attr("disabled",false);
	 $("#cogErrorStatustextbox").val("");
}
function loadtransferInventoryGrid(){
	$("#chartsOfTransferInventoryGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./product/transferInventoryGrid',
		//pager: jQuery('#chartsOfTermsGridPager'),
		
		colNames:['prTransferId','prFromWarehouseId', 'prToWarehouseId','Transfer Date', 'Reference','CoAccoundId','Reason Code','COGAccountID'],
	   	colModel:[
			{name:'prTransferId',index:'prTransferId', width:100,editable:true, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'prFromWarehouseId',index:'prFromWarehouseId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prToWarehouseId',index:'prToWarehouseId', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'transferDate',index:'transferDate',align:'left', width:125,editable:true, editrules:{required:true}, editoptions:{size:10},hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
			{name:'desc',index:'desc', width:200,editable:true, editrules:{required:true}, hidden:false, editoptions:{size:10}},
			{name:'coAccountID',index:'coAccountID', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'reasonCode',index:'reasonCode', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'adjustCogAccountID',index:'adjustCogAccountID', width:60,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10}}
//			{name:'prFromWarehouseId',index:'prFromWarehouseId', width:120,editable:true, editrules:{required:true}, editoptions:{size:10}}
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
		height:425,	width: 360,/*scrollOffset:0,*/
		rownumbers:false,rownumWidth:0,
		loadonce: false,
		loadComplete:function(data) {
			//$('#chartsOfTransferInventoryGrid').setSelection(1,true);
		//	var rowData = jQuery("#chartsOfTransferInventoryGrid").getRowData(1);   		
			//loadTransferDetails(rowData['prTransferId']);
    		
	    	
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
    		loadtransfermaindata(rowData['prTransferId']);
    		loadTransferDetails(rowData['prTransferId']);
    		checkOpenperiodOrNot();
    	},
    	ondblClickRow: function(rowId) {
  
		}
	})
	return true; 
}
var newDialogDiv = jQuery(document.createElement('div'));
var prMasterId ="";
var inventoryOrderPoint = "";
var inventoryOrderQuantity = "";
var editUrl = "";
var wareHouse="";
var glAccounts = "";



function loadtransfermaindata(transferId){
	
	if(isNaN(transferId)||transferId==undefined || transferId=="" || transferId==null ){
		transferId=0;
	}
	var rowId = $("#chartsOfTransferInventoryGrid").jqGrid('getGridParam', 'selrow');
	var rowData = jQuery("#chartsOfTransferInventoryGrid").getRowData(rowId);    
	$('#transferDateID').val(rowData['transferDate']);
	$('#referenceID').val(rowData['desc']);
	$('#warehouseListID').val(rowData['prFromWarehouseId']);
	$('#reasonCodeID').val(rowData['reasonCode']);
	$('#transferId').val(transferId);

}
function SaveCloseAdjustments(){
	var gridRows = $('#chartsOfTransferListGrid').getRowData();
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);
	var additionalInfo=$("#InvAdjustmentformId").serialize();
	additionalInfo=additionalInfo // +"&gridData="+dataToSend
	var checkpermission=getGrantpermissionprivilage('Adjustments',0);
if($("#cogErrorStatustextbox").val()=="")
	{
	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$('#transferDateID').val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 
			if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
			{
				periodid=data.cofiscalperiod.coFiscalPeriodId;
				yearid = data.cofiscalperiod.coFiscalYearId;
				
				$.ajax({
					url: "./product/createInventoryAdjustment?"+additionalInfo,
					type: "POST",
					data :{"gridData":dataToSend},
					success: function(data) {
						createtpusage('Inventory-Adjustments','Inventory Adjustment Save','Info','Inventory,Saving Inventory Adjustment,warehouseListID:'+ $('#warehouseListID').val());
						$("#chartsOfTransferInventoryGrid").trigger("reloadGrid");
						ClearAdjustments();
						
						$("#savebuttonsuccessdiv").html("Saved successfully");
						setTimeout(function() {
			                $('#savebuttonsuccessdiv').html("");
			                }, 3000);
					}
				});
			
			}
			else
				{
				
				if(data.AuthStatus == "granted")
				{	
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
									}).dialog("open");
				}
				else
				{
					showDeniedPopup();
				}
				}
	  	},
			error:function(data){
				console.log('error');
				}
			});
}
else
	{
	$("#cogErrorStatus").html($("#cogErrorStatustextbox").val());
	setTimeout(function() {
        $('#cogErrorStatus').html("");
        }, 5000);
	}
}

function loadTransferDetails(transferId){
	
	
	if(isNaN(transferId)||transferId==undefined || transferId=="" || transferId==null ){
		transferId=0;
	}
	
	
	
	$("#jqGrid").empty();
	$("#jqGrid").append("<table id='chartsOfTransferListGrid'></table><div id='chartsOfTransferListGridPager'></div>");
	$("#chartsOfTransferListGrid").jqGrid({
		datatype: 'json',
		mtype: 'POST',
		url:'./product/getTransferdetails?transferID='+transferId,
		colNames:['id','prTransferDetailId', 'prTransferId', 'prMasterId','Product No','Description','On Hand','Quantity', 'Difference','Inventory Cost'],
	   	colModel:[
            {name:'id',index:'id', width:100,editable:true, hidden:true, editoptions:{size:10},},
			{name:'prTransferDetailId',index:'prTransferDetailId', width:100,editable:false, editrules:{required:true}, hidden:true,cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'prTransferId',index:'prTransferId',align:'left', width:79,formatter: 'number', formatoptions: { defaultValue: '0' },editable:true, editrules:{required:true},hidden:true, editoptions:{size:10}},
			{name:'prMasterId',index:'prMasterId', width:100,editable:true, editrules:{required:true}, hidden:true, editoptions:{size:10},},
			{name:'itemCode',index:'itemCode', width:80,editable:true, editrules:{required:true}, hidden:false, editoptions : {
					dataInit : function(elem) {
						
						var wareHouseId = $('#warehouseListID').val();
						var aSelectedRowId = $("#chartsOfTransferListGrid").jqGrid('getGridParam', 'selrow');
   						$(elem).autocomplete(
							{
								source : './product/getAdjustmentProductNo?transferID='+transferId+"&wareHouseId="+wareHouseId,
								minLength : 3,
								select : function(event, ui) {
									var id = ui.item;
									$("#new_row_description").val(id.memo);
									$("#"+aSelectedRowId+"_description").val(id.memo);
									$("#new_row_prMasterId").val(id.id);
									$("#"+aSelectedRowId+"_prMasterId").val(id.id);
									$("#new_row_quantityAvailable").val(id.inventoryOnHand);
									$("#"+aSelectedRowId+"_quantityAvailable").val(id.inventoryOnHand);
									if(id.inventoryCost!=null)
									{
									$("#new_row_inventoryCost").val(id.inventoryCost);
									$("#"+aSelectedRowId+"_inventoryCost").val(id.inventoryCost);
									}
									else
									{
									$("#new_row_inventoryCost").val(0);
									$("#"+aSelectedRowId+"_inventoryCost").val(0);	
									}
								}
							});
   						},
   						dataEvents: [
	                        {
								 type: 'blur',
								 fn: function(e) {
									// $("#new_row_debit").focus();
								 }
	                        }]
   						},
				editrules : {
					edithidden : false,
					required : true
   			}},
			{name:'description',index:'description', width:80,editable:true, editrules:{required:true}, hidden:false, editoptions:{readonly: 'readonly'}},
			{name : 'quantityAvailable',index : 'quantityAvailable',align : 'center',width : 50,editable : true,editrules : {required : true},editoptions : {size : 10,readonly: 'readonly'}},
			{name:'quantityTransfered',index:'quantityTransfered', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{
					size:10,
					dataEvents: [{
						type: 'change', fn: function (e) {
							var aSelectedRowId = $("#chartsOfTransferListGrid").jqGrid('getGridParam', 'selrow');
                        	var newOnHand = $("#new_row_quantityAvailable").val();
                        	var newOnHand =$("#"+aSelectedRowId+"_quantityAvailable").val();
                        	var newQuatity =  $("#new_row_quantityTransfered").val();
                        	var newQuatity =$("#"+aSelectedRowId+"_quantityTransfered").val();
              /*          	if(newQuatity>newOnHand)
                        		alert('Qty is more than in hand,pls re-enter');
                        	return [false, "Please enter the pasword"];*/
                        	
                        	var newDifference = newQuatity-newOnHand;
                        	$("#new_row_difference").val(newDifference);
                        	$("#"+aSelectedRowId+"_difference").val(newDifference);
                        }
                    }]
				}},
			{name:'difference',index:'difference', width:100,editable:true, editrules:{required:true}, hidden:false, editoptions:{readonly: 'readonly',size:10}},
			{name:'inventoryCost',index:'inventoryCost', width:100,editable:true, editrules:{required:false}, hidden:false, editoptions:{readonly: 'readonly',size:10}},
			],
		rowNum: 50,	
		pgbuttons: true,
		recordtext: '',
		cellEdit: false,
		cellsubmit : 'clientArray',
		editurl: 'clientArray',
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
			chartsOfTransferGridid=id;
		},
		onCellSelect : function (rowid,iCol, cellcontent, e) {},
    	ondblClickRow: function(rowid,icol,cellcontent,e) {}
	}).navGrid('#chartsOfTransferListGridPager', {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : false,
		pager : false,
	});
	
	$("#showOrderPointsButtons").css("display","block");
	
	

	$("#chartsOfTransferListGrid").jqGrid('inlineNav','#chartsOfTransferListGridPager',
			{
				edit : true,
				edittext : "Edit",
				add : true,
				addtext : "Add",
				cancel : false,
				savetext : "Save",
				refresh : false,
				alertzIndex : 10000,
				addParams : {
					addRowParams : {
								keys : false,
								oneditfunc : function() {
									
									$('#saveIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
									$('#clearIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
									document.getElementById("saveIAButtonID").disabled = true;
									document.getElementById("clearIAButtonID").disabled = true;
									
								},
								successfunc : function(response) {
									
								},
								aftersavefunc : function(response) {
									
									var ids = $("#chartsOfTransferListGrid").jqGrid('getDataIDs');
									var veaccrrowid;
									if(ids.length==1){
										veaccrrowid = 0;
									}else{
										var idd=1;
										for(var i=0;i<ids.length;i++){
											if(idd<=ids[i]){
												idd=ids[i];
											}
										}
										 veaccrrowid= idd;
									}
									$("#" + chartsOfTransferGridid).attr("id", Number(veaccrrowid)+1);
									
									var grid=$("#chartsOfTransferListGrid");
									grid.jqGrid('resetSelection');
								    var dataids = grid.getDataIDs();
								    for (var i=0, il=dataids.length; i < il; i++) {
								        grid.jqGrid('setSelection',dataids[i], false);
								    }
									
									if($("#info_head").text()!="Error")
									{
										$('#saveIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
										$('#clearIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
										document.getElementById("saveIAButtonID").disabled = false;
										document.getElementById("clearIAButtonID").disabled = false;
									}
								},
								errorfunc : function(rowid, response) {
									return false;
								},
								afterrestorefunc : function(rowid) {}
							}
						},
				editParams : {
					keys : false,
					successfunc : function(response) {
							return true;
								},
					aftersavefunc : function(id) {
						
						var ids = $("#chartsOfTransferListGrid").jqGrid('getDataIDs');
						var veaccrrowid;
						if(ids.length==1){
							veaccrrowid = 0;
						}else{
							var idd=1;
							for(var i=0;i<ids.length;i++){
								if(idd<=ids[i]){
									idd=ids[i];
								}
							}
							 veaccrrowid= idd;
						}
						$("#" + chartsOfTransferGridid).attr("id", Number(veaccrrowid)+1);
						
						var grid=$("#chartsOfTransferListGrid");
						grid.jqGrid('resetSelection');
					    var dataids = grid.getDataIDs();
					    for (var i=0, il=dataids.length; i < il; i++) {
					        grid.jqGrid('setSelection',dataids[i], false);
					    }
												
						if($("#info_head").text()!="Error")
						{
							$('#saveIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
							$('#clearIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#637c92), to(#3b4f60))');
							document.getElementById("saveIAButtonID").disabled = false;
							document.getElementById("clearIAButtonID").disabled = false;
						}
						
					},
					errorfunc : function(rowid, response) {
						return false;
					},
					oneditfunc : function(id) {
						$('#saveIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
						$('#clearIAButtonID').css('background','-webkit-gradient(linear, left top, left bottom, from(#C1C3CA), to(#A4A4A5))');
						document.getElementById("saveIAButtonID").disabled = true;
						document.getElementById("clearIAButtonID").disabled = true;
					}
				}
				
			});

	var custombuttonthereornot=document.getElementById("copyrowcustombutton");
	if(custombuttonthereornot == null){
	$("#chartsOfTransferListGrid").navButtonAdd('#chartsOfTransferListGridPager',
			{ 	caption:"Delete", 
		        id:"copyrowcustombutton",
				buttonicon:"ui-icon-trash", 
				onClickButton: deletechartsOfTransfer,
				position: "last", 
				title:"Delete", 
				cursor: "pointer"
			} 
		);
	}

	
	
	
	
	//$("#chartsOfTransferListGrid").jqGrid('inlineNav', '#chartsOfTransferListGridPager', {addParams: {position: "last"}});
	return true;
}


function checkOpenperiodOrNot(){
	
	var checkpermission=getGrantpermissionprivilage('Adjustments',0);

	$.ajax({
		url: "./checkAccountingCyclePeriods",
		data:{"datetoCheck":$('#transferDateID').val(),"UserStatus":checkpermission},
		type: "POST",
		success: function(data) { 
			if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
			{
				periodid=data.cofiscalperiod.coFiscalPeriodId;
				yearid = data.cofiscalperiod.coFiscalYearId;
				
				 $("#chartsOfTransferListGrid_iladd").removeClass("ui-state-disabled");
				 $("#chartsOfTransferListGrid_iledit").removeClass("ui-state-disabled");
				 $("#chartsOfTransferListGrid_ilsave").addClass("ui-state-disabled");
				 $("#copyrowcustombutton").removeClass("ui-state-disabled");
				 $("#saveIAButtonID").attr("disabled",false);

			}
			else
				{
				
				if(data.AuthStatus == "granted")
				{	
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
										buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
									}).dialog("open");
				
				 $("#chartsOfTransferListGrid_iladd").addClass("ui-state-disabled");
				 $("#chartsOfTransferListGrid_iledit").addClass("ui-state-disabled");
				 $("#chartsOfTransferListGrid_ilsave").addClass("ui-state-disabled");
				 $("#copyrowcustombutton").addClass("ui-state-disabled");
				 $("#saveIAButtonID").attr("disabled","disabled");
				
				}
				else
				{
					 $("#chartsOfTransferListGrid_iladd").addClass("ui-state-disabled");
					 $("#chartsOfTransferListGrid_iledit").addClass("ui-state-disabled");
					 $("#chartsOfTransferListGrid_ilsave").addClass("ui-state-disabled");
					 $("#copyrowcustombutton").addClass("ui-state-disabled");
					 $("#saveIAButtonID").attr("disabled","disabled");
					showDeniedPopup();
				}
				}
	  	},
			error:function(data){
				console.log('error');
				}
			});
}



function deletechartsOfTransfer(){
	
/*	var gridRows = $('#chartsOfTransferListGrid').getRowData(selrowid);
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);*/
	
	
	var selrowid=$("#chartsOfTransferListGrid").jqGrid('getGridParam', 'selrow');
	var gridRows = $('#chartsOfTransferListGrid').getRowData(selrowid);
	var rowData = new Array();
	for (var i = 0; i < gridRows.length; i++) {
		var row = gridRows[i];
		rowData.push($.param(row));
		}
	var dataToSend = JSON.stringify(gridRows);
/*	var rowData = jQuery("#chartsOfTransferListGrid").getRowData(selrowid);
	var dataToSend = JSON.stringify(rowData);
	alert(dataToSend);
	alert(rowData['prTransferDetailId']);*/
	//$('#chartsOfTransferListGrid').jqGrid('delRowData',selrowid);
	/* $("#chartsOfTransferListGrid_iladd").removeClass("ui-state-disabled");
	 $("#chartsOfTransferListGrid_iledit").removeClass("ui-state-disabled");
	 $("#chartsOfTransferListGrid_ilsave").addClass("ui-state-disabled");*/
	var rowData1 = jQuery("#chartsOfTransferListGrid").getRowData(selrowid)
	if(rowData1['prTransferDetailId']>0){
	
	var additionalInfo=$("#InvAdjustmentformId").serialize();
	additionalInfo=additionalInfo; //+"&gridData="+dataToSend
	$.ajax({
		url: "./product/deleteInventoryAdjustment?"+additionalInfo,
		type: "POST",
		data : {"gridData":dataToSend},
		success: function(data) {
			
			$("#chartsOfTransferListGrid").trigger("reloadGrid");
			/*ClearAdjustments();
			
			$("#savebuttonsuccessdiv").html("Saved successfully");
			setTimeout(function() {
                $('#savebuttonsuccessdiv').html("");
                }, 3000);*/
		}
	});
	
}else{
	$('#chartsOfTransferListGrid').jqGrid('delRowData',selrowid);
	/*	$("#chartsOfTransferListGrid").trigger("reloadGrid");*/
	/* $("#chartsOfTransferListGrid_iladd").removeClass("ui-state-disabled");
	 $("#chartsOfTransferListGrid_iledit").removeClass("ui-state-disabled");
	 $("#chartsOfTransferListGrid_ilsave").addClass("ui-state-disabled");*/
}
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
		colNames:['PrWarehouseInventoryID' ,'prMasterId', 'ItemCode', 'Description', 'Primary Vendor','rDepartmentId','prCategoryId','Department', 'Category', 'On Hand','Counted'],
		colModel :[
		    {name:'prWarehouseInventoryID', index:'prWarehouseInventoryID', align:'left', width:40, editable:true, editrules:{required:true},hidden:true},
           	{name:'prMasterId', index:'prMasterId', align:'left', width:40, editable:true, editrules:{required:true},hidden:true},
			{name:'itemCode', index:'itemCode', align:'left', width:40,hidden:false,editable:false, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'description', index:'description', align:'', width:80,hidden:false,editable:false, editrules:{required:true}},
			{name:'primaryVendorName', index:'primaryVendorName', align:'left', width:90,hidden:false},
			{name:'rDepartmentId',index:'rDepartmentId', width:100,editable:false, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prCategoryId',index:'prCategoryId', width:100,editable:false, editrules:{required:true}, hidden:true, editoptions:{size:10}},
			{name:'prDepartment', index:'prDepartment', align:'', width:40,hidden:false},
			{name:'prCategory', index:'prCategory', align:'left', width:50, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
			{name:'inventoryOnHand', index:'inventoryOnHand', align:'center', width:30,hidden:false,editable:false, editrules:{required:true}},
			{name:'inventoryCounted', index:'inventoryCounted', align:'center', width:30,hidden:false,editable:true, editrules:{required:true},editoptions:{size:10}}
		],
		altRows: true, 
		altclass:'myAltRowClass',
		cellEdit: false,
		cellsubmit : 'remote',
		caption: 'Counting Inventory',
		height:350,
		imgpath: 'themes/basic/images',
		pgbuttons: true,	
		viewrecords: true,
		pager: '#CountInventoryGridPager',
		multiSort:true,
		sortable: true,
		sortname: 'description',
		sortorder: 'asc',
		width: 940,
		rownumbers:true, 
		rowNum: 30,
		rownumWidth: 45,
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000, 2000, 3000, 4000, 5000],
		loadComplete: function(data) {},
		loadError : function (jqXHR, textStatus, errorThrown) {},
		ondblClickRow: function(rowId) {
			jQuery('#CountInventoryGrid').jqGrid('editGridRow', rowId,
	    			{
	    			recreateForm:true,
	    			closeAfterEdit:true,
	                closeOnEscape:true,
	                reloadAfterSubmit:false,
	                });
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
		{add:false,edit:true,del:false,refresh:true,search:false},{

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
		     	//alert(inventoryCounted);
		     	//alert('./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID);
				$("#CountInventoryGrid").setGridParam({'editurl':'./product/insertInventoryCount?prMasterId='+prMasterId+'&inventoryCounted='+inventoryCounted+'&aWareHouseID='+wareHouseID});
				return [true, ""];
			},
		
	
		}
	);
	$("#pdfCountInventoryButton").css("display","block");
	$("#saveCountInventoryButton").css("display","block");
	//$("#getPdfButton").attr("disabled","disabled");
    //$('#getPdfButton').removeAttr('disabled');
}