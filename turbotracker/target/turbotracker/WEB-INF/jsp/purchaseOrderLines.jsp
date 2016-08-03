<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="polineitems">
	<table style="width: 900px">
	<tr>
		<td>
		
			<fieldset class=" ui-widget-content ui-corner-all">
				<table style="width: 900px">
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorLineNameId" name="vendorLineName" style="width:200px" disabled="disabled" value="${manufactureName}"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>
						<td><input type="text" class="datepicker" id="poDateLineId" name="poDateLineName" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${aJobPurchaseOrderBean.orderDate}" />" style="width: 90px;" disabled="disabled"  ></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 150px" id="ourPoLineId" name="ourPoLineName" value="${aJobPurchaseOrderBean.ponumber}" disabled="disabled" ></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
	<table>
		<tr>
			<td></td>
		</tr>
	</table>
	<table id="lineItemGrid"></table>
	<div id="lineItemPager"></div>
	<br>
	<div>
		<table >
			<tr>
				<td>
					<input type="button" class="turbo-tan savehoverbutton" onclick="showUploadForm()" value="Import from Excel">
					<input type="button" class="turbo-tan savehoverbutton excelDownload" onclick="downloadExcel()" value="Export to Excel">
				</td>
				<td style="padding-left: 7px;">
					<fieldset style="padding-bottom: 0px;padding-bottom: 0px; margin-top: 0px; box-shadow: 0px 1px 5px 3px rgb(170, 170, 170);" class="custom_fieldset" >
						<input type="image" onclick="enableTaxAllRecords()" src="./../resources/Icons/tp_select_all_v2.png" title="Tax All">
					</fieldset>
				</td>
			</tr>
		</table>
	</div>
	<div id="uploadExcel_Form" style="display: none; border: 3px 0px #FFFFFF solid">
		<form id="form2" method="post" action="" enctype="multipart/form-data">
			<!-- File input -->    
			<input name="file2" id="file2" type="file" />	<input name="vepoId" style="display: none;" id="vepoId" type="text" value="" />
		</form>
		<button value="Submit" class="turbo-tan savehoverbutton" onclick="uploadJqueryForm()" >Upload</button>
		<input type="button" id="cancelFormSubmit" class="turbo-tan savehoverbutton" value="Cancel" onclick="cancelFormSubmit()"> <br/>
	</div>
	<div id="result"></div>
	<table align="left" style="display: none;">
		<tr>
			<td><input type="checkbox" id="gridShowLineItem" onclick="show()" style="vertical-align: middle;"></td>
			<td><a style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Show Invoiced/Received</b></a>
			</td>
			<td><input type="checkbox" id="lineitemcheck" onclick="lineitem()" style="vertical-align: middle;"></td>
			<td><a onclick="lineitem()" id="showLineItem" style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Different Estimated Ship For Line Item</b>
					</a>&nbsp;&nbsp;<input type="text" style="width: 100px;vertical-align: middle;" id="lineitemdate" class="datepicker" >
			</td>
		</tr>
	</table><br>
	<table style="width: 900px">
		<tr>
			<td>
				<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;">
				<legend class="custom_legend"><label><b>Totals</b></label></legend>
					<table style="width: 900px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative; "><input type="text" style="width: 75px; text-align:right" id="subtotalLineId" name="subtotalLineName" disabled="disabled"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightLineId" value="<fmt:formatNumber type="currency" pattern="$#,##0.00" value="${theVepo.freight}" />" name="freightLineName" disabled="disabled"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxLineId" name="taxLineName" disabled="disabled"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="lineID" name="lineName" style="width: 60px;text-align:right;" disabled="disabled"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalLineId" name="totalLineName" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>

<table style="width: 900px">
	<tr>
		<td align="right" width="80px">
		<div id="ShowInfo" style="width: 380px;height:20px;color: green;"></div>
		<input id="Buttonchoosed" type="checkbox" style="display: none;"/>
		<input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Save" onclick="savePOLineItems()" style="width:90px;" id="cancelpoRelease">
		</td>
	</tr>
</table>
</div>

<script type="text/javascript">
$('.IsButtonClicked').click(function(){
	$('#Buttonchoosed').attr('checked','checked');
});

var aVePoId;
var freight = "${theVepo.freight}";

jQuery(document).ready(function() {
	aVePoId = "${aVePoID}";
	loadLineItemGrid();
});

function loadLineItemGrid() {
	$("#lineItemGrid").trigger("reloadGrid");
	 $('#lineItemGrid').jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#lineItemPager'),
		url:'./jobtabs5/jobReleaseLineItem',
		postData: {vePoId : function() {return aVePoId}},
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
		height:210,	width: 920, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Line Item',
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
		     if(id && id!==lastSel){ 
		        jQuery(this).restoreRow(lastSel); 
		        lastSel=id; 
		     }
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
</script>