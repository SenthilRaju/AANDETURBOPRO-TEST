var areaLineItem;
jQuery(document).ready(function() {
		$("#lineitemdate").hide();
			$(".datepicker").datepicker();
			if(aPurchaseOrderVar === "add"){
				$("#manufacture_ID").text(" ");
			}
			setTimeout("loadLineItemGrid();", 1);
			loadLineItemsPreData();
			$("#lineItemGrid").trigger("reload");
			$('#alertmod').css('z-index','1030');
	});

function loadLineItemsPreData(){
	$("#vendorLineNameId").val($("#vendor_ID").text()); $("#ourPoLineId").val($("#order_ID").text()); $("#subtotalLineId").val($("#Subtotal_ID").text()); 
	$("#totalLineId").val($("#Total_ID").text()); $("#taxLineId").val($("#Tax_ID").text());$("#freightLineId").val(formatCurrency(freight));
	$("#lineID").val($("#Percentage_ID").text());
	$("#poDateLineId").val($("#poDate_ID").text());
};

function downloadExcel() {
	$.ajax({
		url: "./jobtabs5/jobReleaseLineItemExcel",
		type: "POST",
		data : {"vePoId": $("#vePO_ID").text()},
		success: function(data){
				var fileName = data[0];
				var fileLocation =data[1];
				window.location.href = "./jobtabs5/jobReleaseLineItemDownload?&fileName="+fileName+"&fileLocation="+fileLocation;
				},
		error:function(e){
				alert("failed"+e);
				console.log(e);
				}
	});
	return true;
}

function loadLineItemGrid() {
	$("#lineItemGrid").trigger("reloadGrid");
	 $('#lineItemGrid').jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#lineItemPager'),
		url:'./jobtabs5/jobReleaseLineItem',
		postData: {vePoId : function() { return $("#vePO_ID").text();}},
		colNames:['Product No', 'Description','Qty', 'Cost Each', 'Mult.', 'Tax','Net Each', 'Amount', 'VepoID', 'prMasterID' , 'vePodetailID','Posiion', 'Move', 'TaxTotal', 'InLineNote'],
		colModel :[
			{name:'note',index:'note',align:'left',width:60,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
				 dataInit: function (elem) {
			            $(elem).autocomplete({
			                source: 'jobtabs3/productCodeWithNameList',
			                minLength: 1,
			                select: function (event, ui) { var id = ui.item.id; alert(id); } }); }	},editrules:{edithidden:false,required: true}},
           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
				cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
			{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10}, formatter:customCurrencyFormatter, formatoptions:{decimalSeparator:".", thousandsSeparator: ",", decimalPlaces: 2, prefix: "$ "}, editrules:{required: true}},
			{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
			{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
			{name:'netCast',index:'netCast',width:50 , align:'right',formatter:customCurrencyFormatter},
			{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:false, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
			{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false,required: false}},
			{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
			{name:'upAndDown',index:'upAndDown',align:'left',width:50, formatter: upAndDownImage},
			{name:'taxTotal', index:'taxTotal', align:'center', width:20,hidden:true},
			{name:'inLineNote', index:'inLineNote', align:'center', width:20,hidden:true}],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:210,	width: 770, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
			$("a.ui-jqdialog-titlebar-close").show();
			$("#lineItemGrid").setSelection(1, true);
			var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
			var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'taxTotal');
			//setTimeout("loadLineItemsPreData()", 500);
			var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			$('#subtotalGeneralId').val(formatCurrency(sum));
			$('#subtotalLineId').val(formatCurrency(sum));
			$('#subtotalKnowledgeId').val(formatCurrency(sum));
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					var taxValue = $('#taxLineId').val();
					taxAmount = taxAmount + Number(number6)*(taxValue/100);
				}
			});
			$('#generalID').val(formatCurrency(taxAmount));
			$('#lineID').val(formatCurrency(aSelectedPositionDetailID));
			$('#KnowledgeID').val(formatCurrency(taxAmount));
			var freightLineVal= $('#freightLineId').val();
			var number1 = freightLineVal.replace("$", "");
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace(",", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			var frieghtvalue=Number($("#Freight_ID").text().replace(/[^0-9\.]+/g,""));
			aTotal = aTotal + sum + taxAmount + Number(number6)+frieghtvalue;
			$('#totalGeneralId').val(formatCurrency(aTotal));
			$('#totalLineId').val(formatCurrency(aTotal));
			$('#totalKnowledgeId').val(formatCurrency(aTotal));
			$("#lineItemGrid").trigger("reload");
			$("#freightLineId").val(formatCurrency(freight));
			$("#taxLineId").val($("#Tax_ID").text());
			var last = $(this).jqGrid('getGridParam','records');
			var hideDownIcon = Number(last)+1;
			var alignUpIcon = Number(last);
			if(last){
				$("#"+hideDownIcon+"_downIcon").css("display", "none");
				$("#"+alignUpIcon+"_upIcon").css({"position": "relative","left":"-9px","padding":"0px 12px "});
			}
			$("#lineItemGrid").trigger("reloadGrid");
			$("#Ack").trigger("reloadGrid");
		},
		gridComplete: function () {
			$(this).mouseover(function() {
		        var valId = $(".ui-state-hover").attr("id");
		        $(this).setSelection(valId, false);
		    });
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow: function(id){
			
		   },
		ondblClickRow: function(id){
			var lastSel = '';
//			var aCost = $("#lineItemGrid").jqGrid('getCell', id, 'unitCost');
		     if(id && id!==lastSel){ 
//			     $("#"+lastSel+"_unitCost").val(aCost).
		        jQuery(this).restoreRow(lastSel); 
		        lastSel=id; 
		     }
//		     $("#"+id+"_unitCost").val(aCost).replace(/[^0-9\.]+/g,"");
		     jQuery(this).editRow(id, true);
		},
		alertzIndex:1050,
		editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
	}).navGrid('#lineItemPager', {add:true, edit:true, del:true, search:false, view:false},
			//-----------------------edit options----------------------//
			{
				width:515, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Product",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('Product No: ');
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').append('Description: ');
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('Qty: ');
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').append('Cost Each.: ');
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('Mult.: ');
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').append('Tax: ');
					var unitcost = $("#unitCost").val();
					unitcost = unitcost.replace(/[^0-9\.]+/g,"");
					$("#unitCost").val(unitcost);
					$(function() {var cache = {}; var lastXhr=''; $("#note").autocomplete({minLength: 1,timeout :1000,select: function( event, ui ) 
						{ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
						if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
						error: function (result) {
						     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} }); 
					});
					$("#cData").click(function(){
						$("#note").autocomplete("destroy");
						 $(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
					});
				},
				beforeSubmit:function(postdata, formid) {
					$("#note").autocomplete("destroy"); 
					$(".ui-menu-item").hide();
					var aPrMasterID = $('#prMasterId').val();
					if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
					return [true, ""];
				},
				onclickSubmit: function(params){
					var aTaxValue = $('#taxLineId').val();
					return { 'taxValue' : aTaxValue, 'operForAck' : '' };
				},
				afterSubmit:function(response,postData){
					$("#note").autocomplete("destroy"); 
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					 return [true, loadLineItemGrid()];
				}
			},
			//-----------------------add options----------------------//
			{
				width:550, left:400, top: 300, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add Product",
				beforeShowForm: function (form) 
				{
					$("a.ui-jqdialog-titlebar-close").hide();
					if($("#vePOID").val() === ''){
						errorText = "Please Save the General Info Tab.";
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
						jQuery(".ui-icon-closethick").trigger('click');
						return false;
					}
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('Product No: ');
					jQuery('#TblGrid_lineItemGrid #tr_note .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_description .CaptionTD').append('Description: ');
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('Qty: ');
					jQuery('#TblGrid_lineItemGrid #tr_quantityOrdered .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_unitCost .CaptionTD').append('Cost Each: ');
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('Multiplier: ');
					jQuery('#TblGrid_lineItemGrid #tr_priceMultiplier .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
					jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').empty();
					jQuery('#TblGrid_lineItemGrid #tr_taxable .CaptionTD').append('Tax: ');
					//$('#editmodlineItemGrid').css('z-index','1030');
					$("#cData").click(function(){
						$("#note").autocomplete("destroy");
						 $(".ui-menu-item").hide();
						$("a.ui-jqdialog-titlebar-close").show();
					});
				},
				onInitializeForm: function(form){
					
				},
				afterShowForm: function($form) {

					$(function() { var cache = {}; var lastXhr=''; $("input#note.FormElement").autocomplete({minLength: 1,timeout :1000,
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
							lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
						select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
							if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
						error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
						}); 
					return;
					});
					/*$("input#note.FormElement").keypress(function() {
						$(function() { var cache = {}; var lastXhr=''; $("input#note.FormElement").autocomplete({minLength: 1,timeout :1000,
							source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); return; } 
								lastXhr = $.getJSON( "jobtabs3/productCodeWithNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
							select: function( event, ui ){ var ID = ui.item.id; var product = ui.item.label; $("#prMasterId").val(ID);
								if(product.indexOf('-[') !== -1){var pro = product.split("-["); var pro2 = pro[1].replace("]",""); $("#description").val(pro2);} },
							error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	}
							}); 
						return;
						});
						if($("ul.ui-autocomplete").is(":visible")){
						} else {
							$(".ui-autocomplete").show();
						}
						$(".ui-autocomplete").show();
					});*/

				},
				beforeSubmit:function(postdta, formid) {
					$("#note").autocomplete("destroy");
					$(".ui-menu-item").hide();
					var aPrMasterID = $('#prMasterId').val();
					if (aPrMasterID === ""){ return [false, "Alert: Please provide a valid Product (Select from suggest dropdown list)."]; } 
					return [true, ""];
				},
				onclickSubmit: function(params){
					var id = $("#vePO_ID").text();
					var aTaxValue = $('#taxLineId').val();
					return { 'vePoid' : id, 'taxValue' : aTaxValue, 'operForAck' : '' };
				},
				afterSubmit:function(response,postData){
					$("#note").autocomplete("destroy");
					$(".ui-menu-item").hide();
					$("a.ui-jqdialog-titlebar-close").show();
					 return [true, loadLineItemGrid()];
				}
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete Product",
				msg: 'Delete the Product Record?',

				onclickSubmit: function(params){
					var id = jQuery("#lineItemGrid").jqGrid('getGridParam','selrow');
					var key = jQuery("#lineItemGrid").getCell(id, 11);
					var aTaxValue = $('#taxLineId').val();
					return { 'vePodetailId' : key, 'operForAck' : '' ,'taxValue' : aTaxValue};
				}
			},
			//-----------------------search options----------------------//
			{	});
}

function upAndDownImage(cellValue, options, rowObject){
	var element = "<div>";
	var upIconID = options.rowId;
	var downID = options.rowId;
	var downIconID = Number(downID)+1;
	if(options.rowId === '1'){
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px 13px;vertical-align: middle;position: relative;left: 9px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += "<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='vertical-align: middle;'></a>";
		element += "</div>";
	}else {
		element +=	"<a id='"+upIconID+"_upIcon' onclick='upPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/upArrowLineItem.png' title='Move Up & Save'></a>";
		element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += 	"<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px;vertical-align: middle;'></a>";
		element += "</div>";
	}
	return element;
}

function upPOLineItem() {
	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId === null){
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'posistion');
	var aSelectedPODetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePodetailId');
	var aSelectedVePOID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePoid');
	var aAbovePositionRowID = Number(aSelectedRowId) - 1;
	var aAbovePODetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'vePodetailId');
	var aAbovePositionDetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'posistion');
	var aUpLineItem = 'upLineItem';
	updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem);
	return false;
}

function downPOLineItem() {
	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(aSelectedRowId === null){
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'posistion');
	var aSelectedPODetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePodetailId');
	var aSelectedVePOID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'vePoid');
	var aAbovePositionRowID = Number(aSelectedRowId) + 1;
	var aAbovePositionDetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'posistion');
	var aAbovePODetailID = $("#lineItemGrid").jqGrid('getCell', aAbovePositionRowID, 'vePodetailId');
	var aUpLineItem = 'downLineItem';
	updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem);
	return false;
}

function updatePOLineItemPosition(aSelectedRowId, aSelectedPositionDetailID, aSelectedPODetailID, aSelectedVePOID, aAbovePositionRowID, aAbovePositionDetailID, aAbovePODetailID, aUpLineItem){
	$.ajax({
		url: "./jobtabs5/updatePOLineItemPosition",
		type: "POST",
		data : {'selectedRowID' : aSelectedRowId, 'selectedPositionDetailID' : aSelectedPositionDetailID , 'selectedQuoteDetailID' : aSelectedPODetailID, 'selectedJoQuoteHeaderID' : aSelectedVePOID, 'abovePositionRowID' : aAbovePositionRowID,
											'abovePositionDetailID' : aAbovePositionDetailID, 'aboveQuoteDetailID' : aAbovePODetailID, 'oper' : aUpLineItem},
		success: function(data) {
			$("#lineItemGrid").jqGrid('GridUnload');
			loadLineItemGrid();
			$("#lineItemGrid").trigger("reloadGrid");
			/*var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");*/
		}
	});
	return false;
}

function inlineItem(){
	var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	if(rowId === null){
		var errorText = "Please select a line item to add Line Note";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var lineItemText = $("#lineItemGrid").jqGrid('getCell', rowId, 'inLineNote');
	var JopInlinetext =lineItemText.replace("&And", "'");
	areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('inlineItemId');
	$(".nicEdit-main").empty();
	$(".nicEdit-main").append(JopInlinetext);
	jQuery("#POInLineItem").dialog("open");
	$(".nicEdit-main").focus();
	return true;
}

function POLineItemInfo(){
	var inlineText= $('#POInLineItemID').find('.nicEdit-main').html();
	var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
	var vePodetailId = $("#lineItemGrid").jqGrid('getCell', rowId, 'vePodetailId');
	var aLineItem = new Array();
	aLineItem.push(inlineText);
	aLineItem.push(vePodetailId);
	$.ajax({
		url: "./jobtabs5/SavePOlinetextInfo",
		type: "POST",
		data : {'lineItem' : aLineItem},
		success: function(data){
			areaLine.removeInstance('inlineItemId');
			jQuery("#POInLineItem").dialog("close");
			$("#lineItemGrid").trigger("reloadGrid");
		}
	});
}

function POCancelInLineNote(){
	areaLine.removeInstance('inlineItemId');
	jQuery("#POInLineItem").dialog("close");
	return false;
}

jQuery(function(){
	jQuery("#POInLineItem").dialog({
			autoOpen : false,
			modal : true,
			title:"InLine Note",
			height: 350,
			width: 600,
			buttons : {  },
			close:function(){
				areaLine.removeInstance('inlineItemId');
				return true;
			}	
	});
});

function lineitem(){
	if ($('#lineitemcheck').is(':checked')) { 
	 	$("#lineitemdate").show();
 	} else {
	 	 $("#lineitemdate").hide();
 	} 
}

var savePOLineItems = function(){
	 var aVePOID = $("#vePO_ID").text();
	 var aPOLineItemsSubtotal = $("#subtotalLineId").val();
	 var errorText = "Line Items are Saved.";
	 jQuery(newDialogDiv).attr("id","msgDlg");
	 jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	 if(aPOLineItemsSubtotal.replace(/[^0-9\.]+/g,"") <= 0.00){
		 jQuery( "#porelease" ).dialog("close");
		 return false;
	 }
	 $.ajax({
			url: "./jobtabs5/savepolineitemssubtotal",
			type: "POST",
			data: {'aPOLineItemsSubtotal':aPOLineItemsSubtotal.replace(/[^0-9\.]+/g,""), 'aVePOID':aVePOID},
			success: function(data) {
				if(data){
					if($('#Buttonchoosed').is(':checked')){
						$('#ShowInfo').html("Your PO Release Line items are saved successfully.");
						/*setTimeout(function(){$('div#ShowInfo').text("");},2000);*/
						$('#Buttonchoosed').prop('checked', false);
					}else{
						$( "#note" ).autocomplete("destroy");
						$(".ui-menu-item").hide();
						jQuery( "#porelease" ).dialog("close");
						$("#release").trigger("reloadGrid");}
						return true;
					}			
				}
		});		
};
var showUploadForm = function(){
	$("#uploadExcel_Form").slideDown();
};

function enableTaxAllRecords(){
	var aTaxValue = $('#taxLineId').val();
	
	$.ajax({
			url: "./jobtabs5/selectAllTaxes",
			type: "POST",
			data : {"vePoId": $("#vePO_ID").text(),"aTaxValue":aTaxValue},
			success: function(data){
				$("#refresh_lineItemGrid").trigger("click");
				},
			error:function(e){
					alert("failed"+e);
					console.log(e);
				}
	});
	return true;
}
function uploadJqueryForm(){
	var vepoId = $("#vePO_ID").text();
	$("#vepoId").val(vepoId);
	$('#result').html('');
	/** blocking the UI**/
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
	/************/
	$("#form2").ajaxForm({
		url: './fileupload/excelupload',
		success:function(data) {
			$('#result').html('<span><b style="color:green;">'+data+'</b></span>');
			$('#result').show();
			$("#lineItemGrid").trigger("reloadGrid");
			$("#uploadExcel_Form").slideUp('slow');
			setTimeout($.unblockUI, 2000);
		},
		error: function(jqXhr, textStatus, errorThrown){
			$("#uploadExcel_Form").slideUp('slow');
			setTimeout($.unblockUI, 2000);
			var errorText = $(jqXhr.responseText).find('u').html();
			$('#result').html('<span><b style="color:red;">'+errorText+'</b></span>');
			$('#result').show();
		},
		dataType:"text"
	}).submit();
}
function cancelFormSubmit() {
	$("#uploadExcel_Form").slideUp('slow');
}
