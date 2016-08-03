var save = 1;
jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");
	/*$("#addtransferGrid").jqGrid({
		private Integer prTransferDetailId;
		private Integer prTransferId;
		private Integer prMasterId;
		private String description;
		private BigDecimal quantityTransfered;
		private BigDecimal inventoryCost;
		url:'./po_listgrid',
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#addtransferGridPager'),
		colNames:['prMasterId','prTransferId','Product No','Description', 'Available Qty', 'Transfer Qty', 'Job Name', 'Vendor', 'Total'],
		colModel :[
           	{name:'prMasterId', index:'prMasterId', align:'left', width:40, editable:true,hidden:true},
           	{name:'prMasterId', index:'prMasterId', align:'left', width:40, editable:true,hidden:true},
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
		pager: '#addtransferGridPager',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Orders in Transit',
		//autowidth: true,
		height:547,	width: 1140,scrollOffset:0, rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			$('.ui-jqgrid-title').css('text-align','center');
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var aVePOId = rowData['vePOID'];
			var aQryStr = "aVePOId=" + aVePOId;
			document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
	}).navGrid('#addtransferGridPager',//{cloneToTop:true},
		{add:true,edit:true,del:true,refresh:true,search:false},{},{},
		myDelOptions = { 
		    closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
		    caption: "Delete",
		    msg: 'Do you want to delete the line item?',
		    color:'red',

		    onclickSubmit: function(params){
		     var grid = $("#addtransferGrid");
		     var rowId = grid.jqGrid('getGridParam', 'selrow');
		     var vePOID = grid.jqGrid('getCell', rowId, 'vePOID');
		     return { 'vePOID' : vePOID};
		    },
		    afterSubmit:function(response,postData){
		    	alert('after submit');
		      return [true, $('#addtransferGrid').trigger("reloadGrid")];
		    }
		   }
	);*/
	
	/*$("#addtransferGrid").closest("div.ui-jqgrid-view")
    .children("div.ui-jqgrid-titlebar")
    .css("text-align", "center")
    .children("span.ui-jqgrid-title")
    .css("float", "none");*/
	loadaddtransferGrid();
	$('#showErrorMsg').hide();
	
	if($('#recDateId').val()!='' && $('#recDateId').val().length>0){
		$("#edit_addtransferGrid").addClass('ui-state-disabled');
		$("#add_addtransferGrid").addClass('ui-state-disabled');
		$("#del_addtransferGrid").addClass('ui-state-disabled');
		
	}
	
});
var dupl= null;
var count = 1;
var flags = 0;
function loadaddtransferGrid(){	
	
	$("#addtransferGrid").jqGrid('GridUnload');
	//$("#addtransferGrid").trigger("reloadGrid");
	var id = $('#prTransferId').val();	
	if(id != null && id != '' && count === 1){
		console.log("Length---"+id.length);
		dupl = id;
		id = null;
		count = 2;
		loadaddtransferGrid();
	}
/*$('#jqgridLine').empty();
$('#jqgridLine').append('<table id="addtransferGrid"></table><div id="addtransferGridPager"></div>')*/
	try {
	 $('#addtransferGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#addtransferGridPager'),
			url:'./inventoryList/getTransferDetails',
			postData: {'prTransferId':function () { return dupl; }},			
			colNames:['prTransferDetailId','prMasterId','prTransferId','Product No','Description', 'Available Qty', 'Transfer Qty','Inventory Cost'],
			colModel :[
		{name:'prTransferDetailId', index:'prTransferDetailId', align:'left', width:40, editable:false,hidden:true},
		{name:'prMasterId', index:'prMasterId', align:'left', width:40, editable:false,hidden:true},
		{name:'prTransferId', index:'prTransferId', align:'left', width:40, editable:false,hidden:true},
		{name:'itemCode',index:'itemCode',align:'left',width:50,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false,required: true}},
      	{name:'description', index:'description', align:'left', width:120, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
			cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		{name:'quantityAvailable', index:'quantityAvailable', align:'center', width:50,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
		{name:'quantityTransfered', index:'quantityTransfered', align:'center', width:50,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true,required: false}},
		{name:'inventoryCost', index:'inventoryCost', align:'right', width:50,hidden:true, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}}
		],
			rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
			sortname: 'itemCode', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
			height:210,	width: 950, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: '',
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
			loadComplete: function(data) {
				$("#addtransferGrid").setSelection(1, true);
				var rowData = jQuery(this).getRowData(1);
				var prTransferDetailId = rowData["prTransferDetailId"];
				var prMasterId = rowData["prMasterId"];
				$('#prTransferDetailId').val(prTransferDetailId);
				$("#prMasterId").val(prMasterId);
				
				// For Received Items Button changes
				var count = $("#addtransferGrid").getGridParam("reccount");
				if($('#recDateId').val()!='' && $('#recDateId').val().length>0 && count >0){
					$('#WarehouseTransferReceiveID').val("Undo Received");
				}
				
			},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				var rowData = jQuery(this).getRowData(id); 
				var prTransferDetailId = rowData["prTransferDetailId"];
				var prMasterId = rowData["prMasterId"];
				$('#prTransferDetailId').val(prTransferDetailId);
				$("#prMasterId").val(prMasterId);
			},
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
				 //console.log(e);
				},
			editurl:"./inventoryList/manpulateTransferLineItem"
	 }).navGrid('#addtransferGridPager', {add:true, edit:true,del:true,refresh:true,search:false},
				//-----------------------edit options----------------------//
				{
		 	width:515, left:400, top: 300, zIndex:1040,
			closeAfterEdit:true, reloadAfterSubmit:true,
			modal:true, jqModel:true,
			editCaption: "Edit Product",
			beforeShowForm: function (form) 
			{
				$("a.ui-jqdialog-titlebar-close").hide();
				jQuery('#TblGrid_addtransferGrid #tr_itemCode .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_itemCode .CaptionTD').append('Product No: ');
				jQuery('#TblGrid_addtransferGrid #tr_itemCode .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_addtransferGrid #tr_description .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_description .CaptionTD').append('Description: ');
				jQuery('#TblGrid_addtransferGrid #tr_quantityAvailable .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_quantityAvailable .CaptionTD').append('Qty Available: ');
				jQuery('#TblGrid_addtransferGrid #tr_quantityAvailable .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_addtransferGrid #tr_unitCost .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_unitCost .CaptionTD').append('Cost Each.: ');
				jQuery('#TblGrid_addtransferGrid #tr_quantityTransfered .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_quantityTransfered .CaptionTD').append('Qty Transfered: ');
				jQuery('#TblGrid_addtransferGrid #tr_quantityTransfered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				jQuery('#TblGrid_addtransferGrid #tr_taxable .CaptionTD').empty();
				jQuery('#TblGrid_addtransferGrid #tr_taxable .CaptionTD').append('Tax: ');
				/*var unitPrice = $('#unitCost').val();
				unitPrice=  parseFloat(unitPrice.replace(/[^0-9-.]/g, ''));
				$('#unitCost').val(unitPrice);
				var priceMultiplier = $('#priceMultiplier').val();
				priceMultiplier=  parseFloat(priceMultiplier.replace(/[^0-9-.]/g, ''));
				$('#priceMultiplier').val(priceMultiplier);*/
				 return [false,""];
			},
			beforeInitData:function(formid) {
				
				if($('#transferDateId').val()=== null || $('#transferDateId').val().trim()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
						|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
						/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
							|| $('#ref').val()=== null || $('#ref').val().trim()==='')
				{
					$('#showErrorMsg').show();
					$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
					return false; 
				}
				else if($('#transferDateId').val()=== null || $('#transferDateId').val()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
						|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
						/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
							|| $('#ref').val()=== null || $('#ref').val().trim()==='' || oper === 'copy')
				{
					$('#showErrorMsg').show();
					$('#showErrorMsg').html('<span style="color:red;">*Please Save Before Edit</span>');
					return false; 
				}
				else
					{
					$('#showErrorMsg').hide();
					}
				
			},
			beforeSubmit:function(postdata, formid) {
				$("#note").autocomplete("destroy"); 
				$(".ui-menu-item").hide();
				var aPrMasterID = $('#prMasterId').val();
				 var available = $("#quantityAvailable").val();
				 var transfer = $("#quantityTransfered").val();
				
				    if(!isNaN(available) && !isNaN(transfer))
			    	{
				    	available = parseFloat(available);
				    	transfer = parseFloat(transfer);
				    	if(available < transfer || available < 0)
				    	{
				    		//$("#quantityTransfered").val(0);
				    			if(flags===0){
				    			var newDialogDiv = jQuery(document.createElement('div'));
				    			jQuery(newDialogDiv).html('<span><b>Warehouse not have enough Quantity to Transfer.</b></span>');
				    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",zIndex:"12345", 
				    					buttons: [{height:30, text: "OK",click: function() {
				    						$(this).dialog("close");
				    					flags =1;
				    					}}]}).dialog("open");
				    			return [false, ""];
				    			}else{
				    				return [true, ""];
				    			}
				    	}
				    	if(transfer<=0)
				    	{
				    		//$("#quantityTransfered").val(0);
				    			var newDialogDiv = jQuery(document.createElement('div'));
				    			jQuery(newDialogDiv).html('<span><b>Transfer Quantity less than 0.</b></span>');
				    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",zIndex:"12345", 
				    					buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close");
				    					}}]}).dialog("open");
				    			return [false, ""];
				    			}else{
				    				return [true, ""];
				    		
				    	}
			    	}
				if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
				return [true, ""];
			},
			onclickSubmit: function(params){
				/*var msg = saveTransfer();
				if('success' === msg)
					return {'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'edit'};*/
				
				
				return {'transferDateId':$('#transferDateId').val(),'warehouseFrom' : $('#warehouseFrom').val(),'estDateId':$('#estDateId').val(), 'warehouseTo' : $('#warehouseTo').val(),'recDateId' :$('#recDateId').val(),'transNo' : $('#transNo').val(),'ref':$('#ref').val(),'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'edit'};
			},
			afterSubmit:function(response,postData){
				$("#note").autocomplete("destroy"); 
				$(".ui-menu-item").hide();
				$("a.ui-jqdialog-titlebar-close").show();
				 return [true, loadaddtransferGrid()];
			}
		

				},
				//-----------------------add options----------------------//
				{
					width:550, left:400, top: 300, zIndex:1040,
					closeAfterAdd:true,	reloadAfterSubmit:true,
					modal:true, jqModel:false,
					addCaption: "Add Product",
					onInitializeForm: function(form){
						
					},
					beforeInitData:function(formid) {
						if($('#transferDateId').val()=== null || $('#transferDateId').val().trim()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
								|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
								/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
									|| $('#ref').val()=== null || $('#ref').val()==='' && oper !== 'copy')
						{
							$('#showErrorMsg').show();
							$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
							return false; 
						}
						else if($('#transferDateId').val()=== null || $('#transferDateId').val().trim()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
								|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
								/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
									|| $('#ref').val()=== null || $('#ref').val().trim()==='' || oper === 'copy')
						{
							$('#showErrorMsg').show();
							$('#showErrorMsg').html('<span style="color:red;">*Please Save Before Add</span>');
							return false; 
						}
						else
							{
							$('#showErrorMsg').hide();
							}
						
					},
					afterShowForm: function($form) {
						$(function() { var cache = {}; var lastXhr=''; $("input#itemCode").autocomplete({minLength: 1,timeout :1000,
							source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
								lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
							select: function( event, ui ){
								var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
								var fromwarehouseid=$("#warehouseFrom").val();
								/*if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} */
								$.ajax({
							        url: './inventoryList/getTransferLineItems?prMasterId='+$("#prMasterId").val()+"&warehouseid="+fromwarehouseid,
							        type: 'POST',       
							        success: function (data) {
							        	$.each(data, function(key, valueMap) {										
											
							        		if("lineItems"==key)
											{				
												$.each(valueMap, function(index, value){						
													
														$("#description").val(value.description);
														$("#unitCost").val(value.lastCost);
														$("#priceMultiplier").val(value.pomult);
														if(value.isTaxable == 1)
														{
															$("#taxable").prop("checked",true);
														}
														else
															$("#taxable").prop("checked",false);
														
														var available=Number(value.inventoryOnHand)-Number(value.inventoryAllocated);
														$("#quantityAvailable").val(available);
												
												}); 												
											}	
										});
																			
										
							        }
							    });
								},
							error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
							}); 
						return;
						});
						
						$("#quantityTransfered").keyup(function(event){
						    var available = $("#quantityAvailable").val();
						    var transfer = $("#quantityTransfered").val();
						    if(isNaN(available) && isNaN(transfer))
					    	{
						    	available = parseFloat(available);
						    	transfer = parseFloat(transfer);
						    	/*if(available > transfer){}
						    	else
						    		$("#quantityTransfered").val(0);*/
					    	}						    
						    
						});
					},
					beforeSubmit:function(postdta, formid) {
						$("#note").autocomplete("destroy");
						 $(".ui-menu-item").hide();
						var aPrMasterID = $('#prMasterId').val();
						 var available = $("#quantityAvailable").val();
						 var transfer = $("#quantityTransfered").val();
						
						    if(!isNaN(available) && !isNaN(transfer))
					    	{
						    	available = parseFloat(available);
						    	transfer = parseFloat(transfer);
						    	if(available < transfer || available < 0 || transfer<0)
						    	{
						    		//$("#quantityTransfered").val(0);
						    			if(flags===0){
						    			var newDialogDiv = jQuery(document.createElement('div'));
						    			jQuery(newDialogDiv).html('<span><b>Warehouse not have enough Quantity to Transfer.</b></span>');
						    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",zIndex:"12345", 
						    					buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close");
						    					flags =1;
						    					}}]}).dialog("open");
						    			return [false, ""];
						    			}else{
						    				return [true, ""];
						    			}
						    		
						    	}
						    	if(transfer<=0)
						    	{
						    		//$("#quantityTransfered").val(0);
						    			var newDialogDiv = jQuery(document.createElement('div'));
						    			jQuery(newDialogDiv).html('<span><b>Transfer Quantity less than 0.</b></span>');
						    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",zIndex:"12345", 
						    					buttons: [{height:30, text: "OK",click: function() { $(this).dialog("close");
						    					}}]}).dialog("open");
						    			return [false, ""];
						    			}else{
						    				return [true, ""];
						    		
						    	}
						    		
					    	}		
						if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
						return [true, ""];
					},
					onclickSubmit: function(params){
						return {'transferDateId':$('#transferDateId').val(),'warehouseFrom' : $('#warehouseFrom').val(),'estDateId':$('#estDateId').val(), 'warehouseTo' : $('#warehouseTo').val(),'recDateId' :$('#recDateId').val(),'transNo' : $('#transNo').val(),'ref':$('#ref').val(),'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'add'};
						/*var msg = saveTransfer();
						if('success' === msg)
							return {'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'add'};*/
					},
					afterSubmit:function(response,postData){
						$("#note").autocomplete("destroy");
						$(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
						console.log("----------->"+response.responseText);
						var row = response.responseText;
						console.log("prTransferId--->"+row['prTransferId']);
						$.each(row,function(key,value){
							if('prTransferId' === key)
								console.log("TTTTTTTTTTTTTTTT----------->"+value);
						});
						console.log("WWWWWWWWWWWWWWW----------->"+response.responseText.prTransferId);
						var obj = $.parseJSON(response.responseText);
						console.log("TTTTTTTTTTTTTTTT----------->"+obj['prTransferId']);
						$('#prTransferId').val(obj['prTransferId']);
						
						return [true, loadaddtransferGrid()];
					}
				},
				//-----------------------Delete options----------------------//
				{	
					closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
					caption: "Delete Product",
					msg: 'Delete the Product Record?',
					beforeInitData:function(formid) {
						
						if($('#transferDateId').val()=== null || $('#transferDateId').val()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
								|| $('#estDateId').val()=== null || $('#estDateId').val()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
								/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val()===''
									|| $('#ref').val()=== null || $('#ref').val()==='')
						{
							$('#showErrorMsg').show();
							$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
							return false; 
						}
						else if($('#transferDateId').val()=== null || $('#transferDateId').val().trim()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
								|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
								/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
									|| $('#ref').val()=== null || $('#ref').val().trim()==='' || oper === 'copy')
						{
							$('#showErrorMsg').show();
							$('#showErrorMsg').html('<span style="color:red;">*Please Save Before Delete</span>');
							return false; 
						}
						else
							{
							$('#showErrorMsg').hide();
							}
						
					},

					onclickSubmit: function(params){
						/*var msg = saveTransfer();
						if('success' === msg)
							return {'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'del'};*/
					return {'transferDateId':$('#transferDateId').val(),'warehouseFrom' : $('#warehouseFrom').val(),'estDateId':$('#estDateId').val(), 'warehouseTo' : $('#warehouseTo').val(),'recDateId' :$('#recDateId').val(),'transNo' : $('#transNo').val(),'ref':$('#ref').val(),'prTransferId' : $('#prTransferId').val(),'prMasterId' : $('#prMasterId').val(),'prTransferDetailId': $('#prTransferDetailId').val(),'oper': 'del'};

						
					},
					afterSubmit:function(response,postData){
						 return [true, loadaddtransferGrid()];
					}
				}
			);
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
	$("#addtransferGrid").closest("div.ui-jqgrid-view")
    .children("div.ui-jqgrid-titlebar")
    .css("text-align", "center")
    .children("span.ui-jqgrid-title")
    .css("float", "none");
};


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


function saveTransfer()
{
	console.log($("#WarehouseTransferForm").serialize());
	if($('#transferDateId').val()=== null || $('#transferDateId').val().trim()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
			|| $('#estDateId').val()=== null || $('#estDateId').val().trim()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
			/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val().trim()===''
				|| $('#ref').val()=== null || $('#ref').val().trim()==='')
	{
		$('#showErrorMsg').show();
		$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
		return false; 
	}
	else{
	var WarehouseTransferForm = $("#WarehouseTransferForm").serialize();
	$.ajax({
        url: './inventoryList/saveTransferDetails',        
        type: 'POST',    
        data: WarehouseTransferForm,
        success: function (data) {
        	console.log(data.prTransferId);
        	$('#prTransferId').val(data.prTransferId);
        	createtpusage('Inventory-Transfer','Inventory Transfer Save','Info','Inventory,Transfer Save,transNo:'+$('#transNo').val());
        }
    });
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission){
	document.location.href = "./transfer?oper=create";
	}
	}
}

function receiveitems()
{
	if($('#transferDateId').val()=== null || $('#transferDateId').val()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
			|| $('#estDateId').val()=== null || $('#estDateId').val()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
			/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val()===''
				|| $('#ref').val()=== null || $('#ref').val()==='')
	{
		$('#showErrorMsg').show();
		$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
		return false; 
	}
	else{
		$('#showErrorMsg').hide();
		var count = $("#addtransferGrid").getGridParam("reccount");
		if(count > 0){
	var operation = '';
	if($('#WarehouseTransferReceiveID').val() === 'Receive items')
	{
		
		operation = 'del';
	}
	else{
		operation = 'add';
	}
	if(prTransferId !== null && prTransferId !== '')
	{
		var prTransferId =$('#prTransferId').val();
		$.ajax({
	        url: './inventoryList/receiveItemsTransferDetails',        
	        type: 'POST',    
	        data: 'prTransferId='+prTransferId+'&operation='+operation,
	        success: function (data) {
	        	console.log(data);
	        	if(operation === 'del'){
	        		$('#WarehouseTransferReceiveID').val("Undo Received");
	        		$('#showMessageReceive').html("**Status: Items Received**");
	        		createtpusage('Inventory-Transfer','Inventory Transfer Receive Items','Info','Inventory,Transfer Receive Items,transNo:'+$('#transNo').val()+',recDateId:'+recDateId);
	        	}
	        	else{
	        		$('#WarehouseTransferReceiveID').val("Receive items");
	        		$('#showMessageReceive').html("");
	        	}
	        	if(data != null && data != '')
	        		FormatDate(data);
	        	else
	        		$('#recDateId').val("");
	        	
	        	
	        	if($('#recDateId').val()!='' && $('#recDateId').val().length>0){
	        		$("#edit_addtransferGrid").addClass('ui-state-disabled');
	        		$("#add_addtransferGrid").addClass('ui-state-disabled');
	        		$("#del_addtransferGrid").addClass('ui-state-disabled');
	        	}else{
	        		$("#edit_addtransferGrid").removeClass('ui-state-disabled');
	        		$("#add_addtransferGrid").removeClass('ui-state-disabled');
	        		$("#del_addtransferGrid").removeClass('ui-state-disabled');
	        	}
	        }
	    });
		
		//$('#ShowInfo').html("Saved");
		
		
		/*setTimeout(function(){
			$('#showMessageReceive').html("");						
			},3000);*/
	}
	}
		else
			{
			$('#showErrorMsg').show();
			$('#showErrorMsg').html('<span style="color:red;">*Please Add Warehouse Transfer Shipping Items</span>');
			return false; 
			}
	}	
}
function copyTransfer()
{
	console.log($("#WarehouseTransferForm").serialize());
	if($('#transferDateId').val()=== null || $('#transferDateId').val()==='' || $('#warehouseFrom').val()=== null || $('#warehouseFrom').val()== -1
			|| $('#estDateId').val()=== null || $('#estDateId').val()==='' || $('#warehouseTo').val()=== null || $('#warehouseTo').val()== -1
			/*|| $('#recDateId').val()=== null || $('#recDateId').val()===''*/ || $('#transNo').val()=== null || $('#transNo').val()===''
				|| $('#ref').val()=== null || $('#ref').val()==='')
	{
		$('#showErrorMsg').show();
		$('#showErrorMsg').html('<span style="color:red;">*All the fields are mantatory</span>');
		return false; 
	}
	else{
	var WarehouseTransferForm = $("#WarehouseTransferForm").serialize();
	var prTransferId =$('#prTransferId').val();
	WarehouseTransferForm+"&prTransferId="+prTransferId;
	$.ajax({
        url: './inventoryList/copyreceiveItems',        
        type: 'POST',    
        data: WarehouseTransferForm,
        success: function (data) {
        	console.log(data.prTransferId);
        	$('#prTransferId').val(data.prTransferId);
        	oper = "edit";
        	$('#WarehouseTransferID').show();
    		$('#WarehouseTransferReceiveID').show();
    		$('#WarehouseTransferSaveID').hide();
    		count = 1;
    		document.location.href="./warehouseTransfer?token=edit&prTransferId="+data.prTransferId;
        }
    });
	//document.location.href = "./transfer?oper=create";
	}
}
function FormatDate(createdDate){
	/*var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;*/
	createdDate = createdDate.split('/')
	if(createdDate[0]<10){createdDate[0]='0'+createdDate[0];}
	if(createdDate[1]<10){createdDate[1]='0'+createdDate[1];} 
	createdDate = createdDate[0]+"/"+createdDate[1]+"/"+createdDate[2];
	$("#recDateId").val(createdDate);
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

function getToWarehouse(){				
	//$("#LoadingImage").show();
	 $.blockUI({ css: { 
		border: 'none', 
		padding: '15px', 
		backgroundColor: '#000', 
		'-webkit-border-radius': '10px', 
		'-moz-border-radius': '10px', 
		opacity: .5, 
		'z-index':'1010px',
		color: '#fff'
	} });  
	$.ajax({
        url: './getToWarehouse?selectedWHID='+$("#warehouseFrom").val(),
        type: 'POST',       
        success: function (data) {
        	console.log("all--->"+data);
        	checkRefs="<option value='-1'>-Select-</option>";
			$.each(data, function(key, valueMap) {								
					if("prWarehouseList" == key)
					{
						$.each(valueMap, function(index, value){
							console.log('You r right');
							if(value.prWarehouseId != '')
								checkRefs+='<option value='+value.prWarehouseId+'>'+value.city+'</option>';
						}); 
					}
					
			});
			//$("#LoadingImage").hide();
			$('#warehouseTo').html(checkRefs);
			setTimeout($.unblockUI, 500);
        }
    });
}
