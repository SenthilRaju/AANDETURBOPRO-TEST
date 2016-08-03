var joSchedTempHeaderId = -1;

jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	$('#productCategory').val("");
	joSchedTempHeaderId = $('#joSchedTempHeaderId').val();
	loadScheduleDetailsGrid(joSchedTempHeaderId);
	$(".charts_tabs_main").tabs({
		cache: true,
		ajaxOptions: {
			data: {  },
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			//window.location.hash = ui.tab.hash;
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
						"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
			}
		}
		
	});
	
	
});

var newDialogDiv = jQuery(document.createElement('div'));
var columnIds = new Array();

var modelID = -1;

function loadScheduleDetailsGrid(joSchedTempHeaderID) {
	var theParameters = getQueryParameters(joSchedTempHeaderId);
	var theKeys = getColumnsKeys(joSchedTempHeaderId);
	var columnNames = getColumnNames(joSchedTempHeaderId);
	var columnModel = getColumnModel(joSchedTempHeaderId);
	$("#modelDetailsGrid").jqGrid({
		datatype: 'JSON',
		postData: {'joSchedTempHeaderID':joSchedTempHeaderID,'theKeys':theKeys,'theParameters':theParameters},
		mtype: 'POST',
		url: './modelMaintenanceController/getModelMaintenaceList',
		colNames: columnNames,
		colModel:columnModel,
		 rowNum: 20,
		 rowList: [50, 100, 200, 500, 1000],
		 pgbuttons: true,
			altRows: true,
		 viewrecords: true,
		 pager: '#modelDetailsGridpager',
		 sortname:'columnNumber',
		 sortorder:"desc",
		 imgpath:'themes/basic/images',
		 height:360,
		recordtext:'',
		 altclass:'myAltRowClass',
	 	 width:1100,
		onSelectRow : function(rowId) {
    		
    		var rowData = jQuery("#modelDetailsGrid").getRowData(rowId); 
    		var joScheduleModelID = rowData['joScheduleModelID'];
    		$.ajax({
    			url: "./modelMaintenanceController/getProductCategoryName",
    			type: "GET",
    			 dataType: "text",
    			data :  {'joScheduleModelID': joScheduleModelID},
    			success: function(data) {
    				if(data != null)
    				$("#productCategory").val(data);
    				else
    					$("#productCategory").val("");
    			}
    		});

    	},
		jsonReader:{
			root:"rows",
			page:"page",
			total:"total",
			records:"records",
			repeatitems:false,
			cell:"cell",
			id:"id",
			userdata:"userdata"
    	},
    	editurl:"./modelMaintenanceController/manpulaterModel",
		loadComplete: function(data){
			jQuery("#modelDetailsGrid").jqGrid('navGrid','#modelDetailsGridpager',{edit:true,add:true,del:true,view:false,search:true,refresh:false},
			//-----------------------edit options----------------------//
			{
				width:400, left:600, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Model",
				beforeShowForm:function(form) {
					$(".navButton").hide();
					 jQuery('#FrmGrid_modelDetailsGrid').empty();
					var fieldNames = getFieldNames(joSchedTempHeaderID);
					var fieldIds = getFieldIds(joSchedTempHeaderID);
					var id = jQuery("#modelDetailsGrid").jqGrid('getGridParam','selrow');
					var editKeys = getColumnsEditKeys(joSchedTempHeaderID);
					var itemCode = jQuery("#modelDetailsGrid").getCell(id, 'ItemCode');
					modelValue = jQuery('#FrmGrid_modelDetailsGrid').append('<tr id="tr_Item Code" class="FormData" >' +
							'<td class="CaptionTD">Item Code</td>'+
							'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
							'<input id="itemCode" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox"  name="itemCode"  value="'+itemCode+'"/></td></tr> ');
					for (var index = 0; index < fieldNames.length; index++) {
						var value = jQuery("#modelDetailsGrid").getCell(id, editKeys[index]);
						modelValue = jQuery('#FrmGrid_modelDetailsGrid').append('<tr id="tr_'+fieldNames[index]+'" class="FormData" >' +
								'<td class="CaptionTD">'+fieldNames[index]+'</td>'+
								'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
								'<input id="'+fieldIds[index]+'" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox"  name="'+fieldIds[index]+'" value="'+value+'" /></td></tr> ');
					}
					jQuery('#FrmGrid_modelDetailsGrid #tr_Model .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				},
				afterShowForm: function($form) {
					$(function() { 
						
						var cache = {}; var  lastXhr='';
					$("#itemCode").autocomplete({ minLength: 2,timeout :1000, select: function(event, ui) {
						modelID = ui.item.id; 
							$("#itemCode").val(ui.item.label);
						},
						source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
							lastXhr = $.getJSON("modelMaintenanceController/getItemCode", request, function(data, status, xhr) {
								cache[term] = data; if (xhr === lastXhr) {response(data); }
							});
						},
						error: function (result) {
						     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} 
					});

					});
				},
				onclickSubmit: function(params){
					var data = {};
					var id = jQuery("#modelDetailsGrid").jqGrid('getGridParam','selrow');
					var joScheduleModelID = jQuery("#modelDetailsGrid").getCell(id, 'joScheduleModelID');
					data['joSchedTempHeaderID'] = joSchedTempHeaderID;
					data['joScheduleModelId'] = joScheduleModelID;
					data['prMasterId'] = modelID;
					data['model'] = $("#model").val();
					data['webAddress'] = $("#webAddress").val();
					data['oper'] = 'edit';
					for(var index=0;index < columnIds.length;index++)
						{
						data[columnIds[index]] = $("#"+columnIds[index]).val();
						
						}
					modelID = -1;
					return data;
				}
			},
			//-----------------------add options----------------------//
			{
				width:400, left:600, top: 300, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add  Model",
				beforeShowForm:function(form) {
					
					 jQuery('#FrmGrid_modelDetailsGrid').empty();
					var fieldNames = getFieldNames(joSchedTempHeaderID);
					var fieldIds = getFieldIds(joSchedTempHeaderID);
					
					modelValue = jQuery('#FrmGrid_modelDetailsGrid').append('<tr id="tr_Item Code" class="FormData" >' +
							'<td class="CaptionTD">Item Code</td>'+
							'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
							'<input id="itemCode" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox"  name="itemCode"  /></td></tr> ');
					for (var index = 0; index < fieldNames.length; index++) {
						modelValue = jQuery('#FrmGrid_modelDetailsGrid').append('<tr id="tr_'+fieldNames[index]+'" class="FormData" >' +
								'<td class="CaptionTD">'+fieldNames[index]+'</td>'+
								'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
								'<input id="'+fieldIds[index]+'" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox"  name="'+fieldIds[index]+'" /></td></tr> ');
					}
					jQuery('#FrmGrid_modelDetailsGrid #tr_Model .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>');
				},
				afterShowForm: function($form) {
					$(function() { 
						
						var cache = {}; var  lastXhr='';
					$("#itemCode").autocomplete({ minLength: 2,timeout :1000, select: function(event, ui) {
						modelID = ui.item.id; 
							$("#itemCode").val(ui.item.label);
						},
						source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
							lastXhr = $.getJSON("modelMaintenanceController/getItemCode", request, function(data, status, xhr) {
								cache[term] = data; if (xhr === lastXhr) {response(data); }
							});
						},
						error: function (result) {
						     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} 
					});

					});
				},
				onclickSubmit: function(params){
					var data = {};
					data['joSchedTempHeaderID'] = joSchedTempHeaderID;
					data['prMasterId'] = modelID;
					data['model'] = $("#model").val();
					data['webAddress'] = $("#webAddress").val();
					data['oper'] = 'add';
					for(var index=0;index < columnIds.length;index++)
						{
						data[columnIds[index]] = $("#"+columnIds[index]).val();
						
						}
					modelID = -1;
					return data;
				},
				
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete Model",
				msg: 'Delete the Model?',

				onclickSubmit: function(params){
					var id = jQuery("#modelDetailsGrid").jqGrid('getGridParam','selrow');
					var joScheduleModelID = jQuery("#modelDetailsGrid").getCell(id, 'joScheduleModelID');
					modelID = -1;
					return {'joScheduleModelId':joScheduleModelID,'prMasterId':modelID};
				}
			},
			 {         // search
                sopt:['bw'],
                caption:'Search Item Code',
                closeOnEscape: true,
                multipleSearch: false,
                closeAfterSearch: true
        }
			);
		}
	});
	 
	return true;
}
var productCategoryId = -1;
$(function() { 
	
	var cache = {}; var  lastXhr='';
$("#productCategory").autocomplete({ minLength: 2,timeout :1000, select: function(event, ui) {
	productCategoryId = ui.item.id;
		$("#productCategory").val(ui.item.label);
	},
	source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
		lastXhr = $.getJSON("modelMaintenanceController/getProductCategory", request, function(data, status, xhr) {
			cache[term] = data; if (xhr === lastXhr) {response(data); }
		});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});

});




function getColumnNames(joSchedTempHeaderId){
	var width = new Array(); 
	var display = new Array(); 
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': joSchedTempHeaderId},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	var newdisplay = new Array();
	newdisplay.push('joScheduleModelID');
	newdisplay.push('joSchedTempHeaderID');
	newdisplay.push('prMasterID');
	newdisplay.push('Item Code');
	newdisplay.push('Description');
	newdisplay.push('Model');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	newdisplay.push('Web Address');
	newdisplay.push('Created On');
	newdisplay.push('By');
	newdisplay.push('CategoryID');
    return newdisplay;
} 

function getFieldNames(joSchedTempHeaderId){
	var width = new Array(); 
	var display = new Array(); 
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': joSchedTempHeaderId},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	var newdisplay = new Array();
	newdisplay.push('Model');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	newdisplay.push('Web Address');
    return newdisplay;
} 

function getFieldIds(joSchedTempHeaderId){
	var width = new Array(); 
	var display = new Array(); 
	var colNumber = new Array();
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': joSchedTempHeaderId},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
				colNumber[index] = value.columnNumber;
			});
		}
	});
	var newdisplay = new Array();
	newdisplay.push('model');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			if((colNumber[index]+"").length === 1)
				{
				newdisplay.push('col0'+colNumber[index]);
				columnIds.push('col0'+colNumber[index]);
				}
				else
					{
					newdisplay.push('col'+colNumber[index]);
					columnIds.push('col'+colNumber[index]);
					}
		}}
	newdisplay.push('webAddress');
    return newdisplay;
} 

function getColumnModel(joSchedTempHeaderId)
{
	var width = new Array(); 
	var display = new Array(); 
	var colNumber = new Array();
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': joSchedTempHeaderId},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
				colNumber[index] = value.columnNumber;
			});
		}
	});
	var newdisplay = new Array();
	newdisplay.push({name: 'joScheduleModelID', index: 'joScheduleModelID', editable: false, width: '1', hidden: true,search : false, edittype:'text', editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'joSchedTempHeaderID', index: 'joSchedTempHeaderID', editable: false, width: '1', hidden: true,search : false, edittype:'text', editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'prMasterID', index: 'prMasterID', editable: false, width: '1', hidden: true, edittype:'text', search : false,editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'ItemCode', index: 'ItemCode', editable: false, width: '200', hidden: false, edittype:'text', editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'Description', index: 'Description', editable: false, width: '2', hidden: true, edittype:'text',search : false, editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'ModelNo', index: 'ModelNo', editable: false, width: '250', hidden: false, edittype:'text',search : false, editoptions:{size:30}, editrules:{required: false}});
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			if((colNumber[index]+"").length === 1)
			newdisplay.push({name: 'col0'+colNumber[index], index: 'col'+colNumber[index], editable: false, width:width[index], hidden: false, edittype:'text', search : false,editoptions:{size:30}, editrules:{required: false}});
			else
				newdisplay.push({name: 'col'+colNumber[index], index: 'col'+colNumber[index], editable: false, width:width[index], hidden: false, edittype:'text', search : false,editoptions:{size:30}, editrules:{required: false}});
		}}
	newdisplay.push({name: 'WebAddress', index: 'WebAddress', editable: false, width: '150', hidden: false, edittype:'text',search : false, editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'CreatedOn', index: 'CreatedOn', editable: false, width: '150', hidden: false, edittype:'text',search : false, editoptions:{size:30}, editrules:{required: false}});
	newdisplay.push({name: 'CreatedByID', index: 'CreatedByID', editable: false, width: '50', hidden: false, edittype:'text', search : false,editoptions:{size:30},editrules:{required: false}});
	newdisplay.push({name: 'CategoryID', index: 'CategoryID', editable: false, width: '500', hidden: true, edittype:'text', search : false,editoptions:{size:30}, editrules:{required: false}});
    return newdisplay;
}

function getColumnsKeys(tempHeaderID){
	var width = new Array(); 
	var colNumber = new Array();
	var display = new Array(); 
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
				colNumber[index] = value.columnNumber;
			});
		}
	});
	
	var newdisplay = new Array();
	newdisplay.push('joScheduleModelID');
	newdisplay.push('joSchedTempHeaderID');
	newdisplay.push('prMasterID');
	newdisplay.push('ItemCode');
	newdisplay.push('Description');
	newdisplay.push('ModelNo');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			if((colNumber[index]+"").length === 1)
			newdisplay.push('col0'+colNumber[index]);
			else
				newdisplay.push('col'+colNumber[index]);
		}}
	newdisplay.push('WebAddress');
	newdisplay.push('CreatedOn');
	newdisplay.push('CreatedByID');
	newdisplay.push('CategoryID');
    return newdisplay;
} 


function getColumnsEditKeys(tempHeaderID){
	var width = new Array(); 
	var colNumber = new Array();
	var display = new Array(); 
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
				colNumber[index] = value.columnNumber;
			});
		}
	});
	
	var newdisplay = new Array();
	newdisplay.push('ModelNo');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			if((colNumber[index]+"").length === 1)
			newdisplay.push('col0'+colNumber[index]);
			else
				newdisplay.push('col'+colNumber[index]);
		}}
	newdisplay.push('WebAddress');
    return newdisplay;
} 


//Form the Query parameter
/*Parameters are,
 * joScheduleModelID
 * joSchedTempHeaderID
 * ModelNo
 * CategoryID
 * prMasterID
 * WebAddress
 * CreatedOn
 * CreatedByID
 * ItemCode
 * Description
*/

function getQueryParameters(tempHeaderID){
	var width = new Array(); 
	var colNumber = new Array();
	var display = new Array(); 
	$.ajax({
		url: "./modelMaintenanceController/getColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
				colNumber[index] = value.columnNumber;
			});
		}
	});
	
	var parameters = "";
	parameters += 'jsm.joScheduleModelID,';
	parameters += 'jsm.joSchedTempHeaderID,';
	parameters += 'jsm.prMasterID,';
	parameters += 'pm.ItemCode,';
	parameters += 'pm.Description,';
	parameters += 'jsm.ModelNo,';
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			if((colNumber[index]+"").length === 1)
			parameters += 'jsm.col0'+colNumber[index]+',';
			else
				parameters += 'jsm.col'+colNumber[index]+',';
		}}
	parameters += 'jsm.WebAddress,';
	parameters += "DATE_FORMAT(jsm.CreatedOn,'%m/%d/%Y') as CreatedOn,";
	parameters += 'tul.Initials as CreatedByID,';
	parameters += 'jsm.CategoryID';
    return parameters;
} 

function testLink()
{
	
	var id = jQuery("#modelDetailsGrid").jqGrid('getGridParam','selrow');
	var webAddress = jQuery("#modelDetailsGrid").getCell(id, 'WebAddress');
	createtpusage('Company-Settings-Model Maintenance','Test Product Link','Info','Company-Settings-Model Maintenance,Test Product Link,webAddress:'+webAddress);
	if(!isEmpty(webAddress))
		{
		var site = webAddress.search("http");
		if(site == -1)
		window.open("http://"+webAddress, '_blank');
		else
			window.open(webAddress, '_blank');
		}else
			{

			jQuery(newDialogDiv).html('<span style="color:red"> Please select valid Model</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
				buttons:{
					
					Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
			return false;
			
			}
	}

function testManufacurersLink()
{
	if(joSchedTempHeaderId != -1)
		{
		$.ajax({
			url: "./modelMaintenanceController/getManufacurersLink", 
			Type: 'GET',
			async: false,
			data: {'joSchedTempHeaderId': joSchedTempHeaderId},
			success: function(data){
				createtpusage('Company-Settings-Model Maintenance','Manufacturer Link','Info','Company-Settings-Model Maintenance,Manufacturer Link,joSchedTempHeaderId:'+joSchedTempHeaderId);
				var site = data.search("http");
				if(site == -1)
				window.open("http://"+data, '_blank');
				else
					window.open(data, '_blank');
			}
		});
		
		}else
			{

			jQuery(newDialogDiv).html('<span style="color:red"> Please select valid Model</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
				buttons:{
					
					Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
			return false;
			
			}
	}

function saveproductCategory()
{
	var id = jQuery("#modelDetailsGrid").jqGrid('getGridParam','selrow');
	var joScheduleModelID = jQuery("#modelDetailsGrid").getCell(id, 'joScheduleModelID');
	if(joScheduleModelID != null && productCategoryId != -1)
	{
		$.ajax({
			url: "./modelMaintenanceController/saveproductCategory", 
			Type: 'GET',
			async: false,
			data: {'joScheduleModelID': joScheduleModelID,'prMasterId':productCategoryId},
			success: function(data){
				createtpusage('Company-Settings-Model Maintenance','Save Models','Info','Company-Settings-Model Maintenance,Saving Model,joScheduleModelID:'+joScheduleModelID);

				jQuery(newDialogDiv).html('<span style="color:green"> Product category updated Successfully</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Success", 
					buttons:{
						
						Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
				return true;
				
				
				productCategoryId != -1;}
		});
		productCategoryId != -1;
	}else
		{

		jQuery(newDialogDiv).html('<span style="color:red"> Please select valid Model</span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
			buttons:{
				
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return false;
		
		}
	}

function isEmpty(str) {
    return (!str || 0 === str.length);
}
