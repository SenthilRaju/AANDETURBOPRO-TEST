jQuery(document).ready(function() {
	
});

jQuery(function() {

});
var aGlobal_TempHeaderID = ""; 
var joSubmittalId = 0;
function loadSubmittalSchedule(detaliID, tempHeaderID,joSubmittalID){
	aGlobal_TempHeaderID = tempHeaderID;
	joSubmittalId = joSubmittalID;
	var columnName = getColumnName(tempHeaderID);
	var columnModel = getColumnModel(tempHeaderID);
	$("#scheduleGrid").jqGrid('GridUnload');
	loadScheduleGridValues(columnName, columnModel, detaliID, tempHeaderID);
	$("#scheduleGrid").trigger("reload");
}

function loadScheduleGridValues(columnName, columnModel, detaliID, tempHeaderID) {
	var theParameters = "*";
	theParameters = getColumnsParameters(tempHeaderID);
	var theKeys = getColumnsKeys(tempHeaderID);
	var theJoScheduleDetailID = 0;
	$("#scheduleGrid").jqGrid({
		  url:'./jobtabs3/submittalSheduled',
		  datatype: 'JSON',
		  mtype: 'GET',
		  postData: { 'submittalDetailsID' : detaliID,'theParameters':theParameters,'theKeys':theKeys},
		  pager:jQuery('#scheduleGridpager'),
		  colNames: columnName,
    	  colModel: columnModel, 
          pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
          height:175,width: 1030,caption:"Line Item", altRows: true,
		  altclass:'myAltRowClass',
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
	    	editurl:"./jobtabs3/manpulaterSubmittal_Schedule"
		}).navGrid("#scheduleGridpager",
			{add:true,del:true,search: false,edit:true,pager:false,refresh:false, alertcap: "Warning", alerttext: 'Please select a Schedule'},
			//-----------------------edit options----------------------//
			{
				width:400, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Schedule",
				beforeShowForm:function(form) {
					// Hide the navigate
					$(".navButton").hide();
					// Reset the Grid field values
					jQuery('#FrmGrid_scheduleGrid').find("input[type=text], textarea").val("");
					
					// Auto complete for Model
					$(function() { var cache = {}; var lastXhr=''; $("#ModelNo").autocomplete({ minLength: 2,timeout :1000,
					select: function( event, ui ) 
					{ 
						var scheduleModelID = ui.item.id; $("#modelID").val(scheduleModelID);
						$.ajax({
							url: "./jobtabs3/scheduledModelID", mType: "GET", data : { 'scheduleModelId' : scheduleModelID },
							success: function(data){ var tag = data.col01;  $("#Tag").val(tag); var size = data.col02;  $("#Size").val(size); 
							var finish = data.col03;$("#Finish").val(finish);  
							var dwgNo = data.col04;  $("#DwgNo").val(dwgNo); var remarks = data.col05;  $("#Remarks").val(remarks); }
						});
					},
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	     			lastXhr = $.getJSON( "jobtabs3/modelNoList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	     			error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					} }); });

				$(function() { var cache = {}; var lastXhr=''; $("#Model").autocomplete({ minLength: 2,timeout :1000,
					select: function( event, ui ) 
					{ 
						var scheduleModelID = ui.item.id; $("#modelNoID").val(scheduleModelID);
						$.ajax({
							url: "./jobtabs3/scheduledModelID", mType: "GET", data : { 'scheduleModelId' : scheduleModelID },
							success: function(data){ var tag = data.col01;  $("#Tag").val(tag); var size = data.col02;  $("#Size").val(size); 
							var finish = data.col03;$("#Finish").val(finish);  
							var dwgNo = data.col04;  $("#DwgNo").val(dwgNo); var remarks = data.col05;  $("#Remarks").val(remarks); }
						});
					},
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	     			lastXhr = $.getJSON( "jobtabs3/modelNoList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	     			error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					} }); });
				
				var dataField = new Array();
				var displayText = new Array();
				var columnumber = new Array();
				$.ajax({
					url: "./jobtabs3/scheduledColumnName", 
					 Type: 'GET',
					 async:false,
					 data: { 'tempHeaderID' : tempHeaderID },
					success: function(data){
						$.each(data, function(index, value){
							columnumber[index] = value.columnNumber;
							dataField[index] = value.displayText;
						});
					}
				});
				var newKeys = getEditColumnsKeys(tempHeaderID);
				// Form the field Id
				var colName = new Array();
		    	for (var index = 0; index< dataField.length; index++) {
		    		if(dataField[index].toLowerCase() === 'quantity' || dataField[index].toLowerCase() === 'qty'|| dataField[index].toLowerCase() === 'qty.' ){
		    		colName.push("Qty");
		    		}
		    		if(dataField[index].toLowerCase() === 'model' || dataField[index].toLowerCase() === 'model no.' || dataField[index].toLowerCase() === 'model no' ){
		    			colName.push("Model");
		    			}
		    	}
		    	for (var index = 0; index< dataField.length; index++) {
		    		if(dataField[index].toLowerCase() != 'quantity' && dataField[index].toLowerCase() != 'qty' && dataField[index].toLowerCase() != 'qty.' &&
		    				dataField[index].toLowerCase() != 'model' && dataField[index].toLowerCase() != 'model no.' && dataField[index].toLowerCase() != 'model no'){
		    		colName.push(dataField[index]);
		    		}
		    	}
		    	var editValueKeys = getEditValueKeys(tempHeaderID);
				var id = jQuery("#scheduleGrid").jqGrid('getGridParam','selrow');
				theJoScheduleDetailID = jQuery("#scheduleGrid").getCell(id, 'joScheduleDetailID');
				// Get the field values and Set
				for (var index = 0; index < colName.length; index++) {
					displayText[index] = colName[index].replace(/[^a-zA-Z ]/g, "").replace(" ", "");
					var key = jQuery("#scheduleGrid").getCell(id, editValueKeys[index]);
					
					$('#'+newKeys[index]).val(key);
				}
				
				},
				onInitializeForm: function(form){

					var dataField = new Array();
					var displayText = new Array();
					var columnumber = new Array();
			    	$.ajax({
						url: "./jobtabs3/scheduledColumnName", 
						 Type: 'GET',
						 async:false,
						 data: { 'tempHeaderID' : tempHeaderID },
						success: function(data){
							$.each(data, function(index, value){
								columnumber[index] = value.columnNumber;
								dataField[index] = value.displayText;
							});
						}
					});
			    	 jQuery('#FrmGrid_scheduleGrid').empty();
			    		var colName = new Array();
				    	for (var index = 0; index< dataField.length; index++) {
				    		if(dataField[index].toLowerCase() === 'quantity' || dataField[index].toLowerCase() === 'qty'|| dataField[index].toLowerCase() === 'qty.' ){
				    		colName.push("Qty");
				    		}
				    		if(dataField[index].toLowerCase() === 'model' || dataField[index].toLowerCase() === 'model no.' || dataField[index].toLowerCase() === 'model no' ){
				    			colName.push("Model");
				    			}
				    	}
				    	for (var index = 0; index< dataField.length; index++) {
				    		if(dataField[index].toLowerCase() != 'quantity' && dataField[index].toLowerCase() != 'qty' && dataField[index].toLowerCase() != 'qty.' &&
				    				dataField[index].toLowerCase() != 'model' && dataField[index].toLowerCase() != 'model no.' && dataField[index].toLowerCase() != 'model no'){
				    		colName.push(dataField[index]);
				    		}
				    	}
				    	// Get the field values and Set
				    	var newKeys = getEditColumnsKeys(tempHeaderID);
					for (var index = 0; index < colName.length; index++) {
						displayText[index] = colName[index].replace(/[^a-zA-Z ]/g, "").replace(" ", "");
						modelValue = jQuery('#FrmGrid_scheduleGrid').append('<tr id="tr_'+displayText[index]+'" class="FormData" >' +
								'<td class="CaptionTD">'+displayText[index]+'</td>'+
								'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
								'<input id="'+newKeys[index]+'" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox"  name="'+newKeys[index]+'" /></td></tr>');
					}
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .DataTD').append('<td><input type="hidden" id="modelID" name="modelID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .CaptionTD').append('<span style="color:red;"> *</span>'); 
					jQuery('#FrmGrid_scheduleGrid #tr_Model .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
					jQuery('#FrmGrid_scheduleGrid #tr_Model .DataTD').append('<td><input type="hidden" id="modelNoID" name="modelNoID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
					jQuery('#FrmGrid_scheduleGrid #tr_Model .CaptionTD').append('<span style="color:red;"> *</span>');
					
				},
				onclickSubmit: function(params){
					var data = {};
					var i =1;
					rows = jQuery("#scheduleGrid").jqGrid('getGridParam', 'records');
					$("#FrmGrid_scheduleGrid input").each(function() {
						var id=$(this).attr("id");
						var value = $(this).attr("value");
						if(id.toLowerCase() === "modelnoid" || id.toLowerCase() === "model" || id.toLowerCase() === "qty" || id.toLowerCase() === "quantity")
							{
							data[id] = value;
							}
						else
							{
							data['col'+i] = value;
							i++;
							}
					});
					data['theJoScheduleDetailID'] = theJoScheduleDetailID;
					data['theHeaderID'] = tempHeaderID;
					data['joSubmittalId'] = joSubmittalId;
				
					return data;
				}
			},
			//-----------------------add options----------------------//
			{
				width:400, left:400, top: 300, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add  Schedule",
				
				beforeShowForm:function(form) {
					 jQuery('#FrmGrid_scheduleGrid').find("input[type=text], textarea").val("");
					$(function() { var cache = {}; var lastXhr=''; $("#ModelNo").autocomplete({ minLength: 2,timeout :1000,
					select: function( event, ui ) 
					{ 
						var scheduleModelID = ui.item.id; $("#modelID").val(scheduleModelID);
						$.ajax({
							url: "./jobtabs3/scheduledModelID", mType: "GET", data : { 'scheduleModelId' : scheduleModelID },
							success: function(data){ var tag = data.col01;  $("#Tag").val(tag); var size = data.col02;  $("#Size").val(size); 
							var finish = data.col03;$("#Finish").val(finish);  
							var dwgNo = data.col04;  $("#DwgNo").val(dwgNo); var remarks = data.col05;  $("#Remarks").val(remarks); }
						});
					},
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	     			lastXhr = $.getJSON( "jobtabs3/modelNoList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	     			error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					} }); });

				$(function() { var cache = {}; var lastXhr=''; $("#Model").autocomplete({ minLength: 2,timeout :1000,
					select: function( event, ui ) 
					{ 
						var scheduleModelID = ui.item.id; $("#modelNoID").val(scheduleModelID);
						$.ajax({
							url: "./jobtabs3/scheduledModelID", mType: "GET", data : { 'scheduleModelId' : scheduleModelID },
							success: function(data){ var tag = data.col01;  $("#Tag").val(tag); var size = data.col02;  $("#Size").val(size); 
							var finish = data.col03;$("#Finish").val(finish);  
							var dwgNo = data.col04;  $("#DwgNo").val(dwgNo); var remarks = data.col05;  $("#Remarks").val(remarks); }
						});
					},
					source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	     			lastXhr = $.getJSON( "jobtabs3/modelNoList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
	     			error: function (result) {
					     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
					} }); });
				},
				onInitializeForm: function(form){

					var dataField = new Array();
					var displayText = new Array();
					var columnumber = new Array();
			    	$.ajax({
						url: "./jobtabs3/scheduledColumnName", 
						 Type: 'GET',
						 async:false,
						 data: { 'tempHeaderID' : tempHeaderID },
						success: function(data){
							$.each(data, function(index, value){
								columnumber[index] = value.columnNumber;
								dataField[index] = value.displayText;
							});
						}
					});
			    	
			    	var newKeys = getEditColumnsKeys(tempHeaderID);
			    	// Form the field Id
					var colName = new Array();
			    	for (var index = 0; index< dataField.length; index++) {
			    		if(dataField[index].toLowerCase() === 'quantity' || dataField[index].toLowerCase() === 'qty'|| dataField[index].toLowerCase() === 'qty.' ){
			    		colName.push("Qty");
			    		}
			    		if(dataField[index].toLowerCase() === 'model' || dataField[index].toLowerCase() === 'model no.' || dataField[index].toLowerCase() === 'model no' ){
			    			colName.push("Model");
			    			}
			    	}
			    	for (var index = 0; index< dataField.length; index++) {
			    		if(dataField[index].toLowerCase() != 'quantity' && dataField[index].toLowerCase() != 'qty' && dataField[index].toLowerCase() != 'qty.' &&
			    				dataField[index].toLowerCase() != 'model' && dataField[index].toLowerCase() != 'model no.' && dataField[index].toLowerCase() != 'model no'){
			    		colName.push(dataField[index]);
			    		}
			    	}
					 jQuery('#FrmGrid_scheduleGrid').empty();
					for (var index = 0; index < dataField.length; index++) {
						
						displayText[index] = colName[index].replace(/[^a-zA-Z ]/g, "").replace(" ", "");
						modelValue = jQuery('#FrmGrid_scheduleGrid').append('<tr id="tr_'+displayText[index]+'" class="FormData" rowpos="'+columnumber[index]+'">' +
								'<td class="CaptionTD">'+displayText[index]+'</td>'+
								'<td class="DataTD">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ' + 
								'<input id="'+newKeys[index]+'" class="FormElement ui-widget-content ui-corner-all" type="text" role="textbox" name="'+newKeys[index]+'"></td></tr>');
					}
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .DataTD').append('<td><input type="hidden" id="modelID" name="modelID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
					jQuery('#FrmGrid_scheduleGrid #tr_ModelNo .CaptionTD').append('<span style="color:red;"> *</span>'); 
					jQuery('#FrmGrid_scheduleGrid #tr_Model .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
					jQuery('#FrmGrid_scheduleGrid #tr_Model .DataTD').append('<td><input type="hidden" id="modelNoID" name="modelNoID" title="Must be atleast 2 characters long" style="width: 50px;"</td>');
					jQuery('#FrmGrid_scheduleGrid #tr_Model .CaptionTD').append('<span style="color:red;"> *</span>');
				},
				onclickSubmit: function(params){
					var data = {};
					var i =1;
					var rows = 0;
					rows = jQuery("#scheduleGrid").jqGrid('getGridParam', 'records');
					$("#FrmGrid_scheduleGrid input").each(function() {
						var id=$(this).attr("id");
						var value = $(this).attr("value");
						if(id.toLowerCase() === "modelnoid" || id.toLowerCase() === "model" || id.toLowerCase() === "qty" || id.toLowerCase() === "quantity")
							{
							data[id] = value;
							}
						else
							{
							data['col'+i] = value;
							i++;
							}
					});
					data['theHeaderID'] = tempHeaderID;
					data['joSubmittalId'] = joSubmittalId;
					data['oper'] = 'add';
					data['line'] = rows+1;
					return data;
				},
				
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete Schedule Type",
				msg: 'Delete the Schedule Type?',

				onclickSubmit: function(params){
					var id = jQuery("#scheduleGrid").jqGrid('getGridParam','selrow');
					var key = jQuery("#scheduleGrid").getCell(id, 'joScheduleDetailID');
					return {'theJoScheduleDetailID':key};
				}
			}
	);
}

//Form the Column names for Grid
function getColumnName(tempHeaderID){
	var display = new Array(); 
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		 Type: 'GET',
		 async:false,
		 data: { 'tempHeaderID' : tempHeaderID },
		success: function(data){
			$.each(data, function(index, value){
				columnumber = value.columnNumber;
				display[index] = value.displayText;
			});
		}
	});
	var colName = new Array();
	colName.push('joScheduleDetailID');
	colName.push('Note');
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty'|| display[index].toLowerCase() === 'qty.' ){
		colName.push(display[index].split('.').join(""));
		}
		if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.' || display[index].toLowerCase() === 'model no' ){
			colName.push(display[index]);
			}
	}
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' && display[index].toLowerCase() != 'model no'){
		colName.push(display[index]);
		}
	}

	return colName;
}

//Form the Column Model for Grid
function getColumnModel(tempHeaderID){
	var columnModel = new Array();
	var width = new Array(); 
	var display = new Array(); 
	columnModel.push('col01'); columnModel.push('col02'); columnModel.push('col03'); columnModel.push('col04'); columnModel.push('col05'); 
	columnModel.push('col06'); columnModel.push('col07'); columnModel.push('col08'); columnModel.push('col09'); columnModel.push('col10');
	columnModel.push('col11'); columnModel.push('col12'); columnModel.push('col13'); columnModel.push('col14'); columnModel.push('col15');
	columnModel.push('col16'); columnModel.push('col17'); columnModel.push('col18'); columnModel.push('col19'); columnModel.push('col20');
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	
	var jsonObj = []; 
	jsonObj.push({name: 'joScheduleDetailID', index: 'joScheduleDetailID', editable: false, width: '500', hidden: true, edittype:'text', editoptions:{size:30}, editrules:{required: false}});
	jsonObj.push({name: 'note', index: 'note', editable: false, width: '500', hidden: false, edittype:'text', editoptions:{size:30}, editrules:{required: false}});
	for (var index = 0; index < display.length; index++) {
    	if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty' || display[index].toLowerCase() === 'qty.'){
    		
    		jsonObj.push({name: 'Quantity', index: 'Quantity', editable: true, width: width[index], hidden: false, edittype:'text',editoptions:{size:30},editrules:{required: true}});
    	}
    	if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.' ){
    		
    		jsonObj.push({name: 'ModelNo', index: 'ModelNo', editable: true, width: width[index], hidden: false, edittype:'text',editoptions:{size:30},editrules:{required: true}});
    	}
    }
	var newwidth = new Array();
	var newdisplay = new Array();
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' &&
				display[index].toLowerCase() != 'qty.' &&	display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newwidth.push(width[index]);
			newdisplay.push(display[index]);
		}}
	for (var index = 0; index< newdisplay.length; index++) {
			jsonObj.push({name: columnModel[index], index: columnModel[index], editable: true, width: newwidth[index], hidden: false, edittype:'text',editoptions:{size:30},editrules:{required: false}});
		}
	
    return jsonObj;
} 

//Form the Query parameter
function getColumnsParameters(tempHeaderID){
	var width = new Array(); 
	var columnModel = new Array();
	var display = new Array(); 
	var length = 0;
	columnModel.push('col01'); columnModel.push('col02'); columnModel.push('col03'); columnModel.push('col04'); columnModel.push('col05'); 
	columnModel.push('col06'); columnModel.push('col07'); columnModel.push('col08'); columnModel.push('col09'); columnModel.push('col10');
	columnModel.push('col11'); columnModel.push('col12'); columnModel.push('col13'); columnModel.push('col14'); columnModel.push('col15');
	columnModel.push('col16'); columnModel.push('col17'); columnModel.push('col18'); columnModel.push('col19'); columnModel.push('col20');
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	
	var parameter = ""; 
	parameter += "joScheduleDetailID,";
	parameter += "'' as note,";
	for (var index = 0; index < length; index++) {
    	if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty' || display[index].toLowerCase() === 'qty.' ){
    		parameter += "jsd.Quantity as Quantity,";
    	}
    	if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.'  || display[index].toLowerCase() === 'model no'){
    		parameter += "jsm.ModelNo as ModelNo,";    	}
    }
	var newdisplay = new Array();
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	for (var index = 0; index< newdisplay.length; index++) {
		parameter += "jsd."+columnModel[index]+" as "+columnModel[index]+",";
		}
	
	var str = parameter.substring(0, parameter.length - 1);
	
    return str;
} 

//Form the Model names for Grid
function getColumnsKeys(tempHeaderID){
	var width = new Array(); 
	var columnModel = new Array();
	var display = new Array(); 
	var length = 0;
	columnModel.push('col01'); columnModel.push('col02'); columnModel.push('col03'); columnModel.push('col04'); columnModel.push('col05'); 
	columnModel.push('col06'); columnModel.push('col07'); columnModel.push('col08'); columnModel.push('col09'); columnModel.push('col10');
	columnModel.push('col11'); columnModel.push('col12'); columnModel.push('col13'); columnModel.push('col14'); columnModel.push('col15');
	columnModel.push('col16'); columnModel.push('col17'); columnModel.push('col18'); columnModel.push('col19'); columnModel.push('col20');
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	
	var parameter = new Array(); 
	parameter.push('joScheduleDetailID');
	parameter.push('note');
	for (var index = 0; index < length; index++) {
    	if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty' || display[index].toLowerCase() === 'qty.'){
    		parameter.push('Quantity');
    	}
    	if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.'  || display[index].toLowerCase() === 'model no'){
    		parameter.push('ModelNo');
    		 	}
    }
	var newdisplay = new Array();
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	for (var index = 0; index< newdisplay.length; index++) {
		parameter.push(columnModel[index]);
		}
    return parameter;
} 

//Used to get the field Id to Edit and Delete.
function getEditColumnsKeys(tempHeaderID){
	var width = new Array(); 
	var columnModel = new Array();
	var display = new Array(); 
	var length = 0;
	columnModel.push('col01'); columnModel.push('col02'); columnModel.push('col03'); columnModel.push('col04'); columnModel.push('col05'); 
	columnModel.push('col06'); columnModel.push('col07'); columnModel.push('col08'); columnModel.push('col09'); columnModel.push('col10');
	columnModel.push('col11'); columnModel.push('col12'); columnModel.push('col13'); columnModel.push('col14'); columnModel.push('col15');
	columnModel.push('col16'); columnModel.push('col17'); columnModel.push('col18'); columnModel.push('col19'); columnModel.push('col20');
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	
	var parameter = new Array(); 
	for (var index = 0; index < length; index++) {
    	if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty' || display[index].toLowerCase() === 'qty.'){
    		parameter.push('Qty');
    	}
    	if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.'  || display[index].toLowerCase() === 'model no'){
    		parameter.push('Model');
    		 	}
    }
	var newdisplay = new Array();
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	for (var index = 0; index< newdisplay.length; index++) {
		parameter.push(columnModel[index]);
		}
    return parameter;
} 
function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

// Get the names simiailar to model names.
// We used this to retrieve data from Grid
function getEditValueKeys(tempHeaderID){
	var width = new Array(); 
	var columnModel = new Array();
	var display = new Array(); 
	var length = 0;
	columnModel.push('col01'); columnModel.push('col02'); columnModel.push('col03'); columnModel.push('col04'); columnModel.push('col05'); 
	columnModel.push('col06'); columnModel.push('col07'); columnModel.push('col08'); columnModel.push('col09'); columnModel.push('col10');
	columnModel.push('col11'); columnModel.push('col12'); columnModel.push('col13'); columnModel.push('col14'); columnModel.push('col15');
	columnModel.push('col16'); columnModel.push('col17'); columnModel.push('col18'); columnModel.push('col19'); columnModel.push('col20');
	$.ajax({
		url: "./jobtabs3/scheduledColumnName", 
		Type: 'GET',
		async: false,
		data: {'tempHeaderID': tempHeaderID},
		success: function(data){
			length = data.length-1;
			$.each(data, function(index, value){
				width[index] = value.displayWidth;
				display[index] = value.displayText;
			});
		}
	});
	
	var parameter = new Array(); 
	for (var index = 0; index < length; index++) {
    	if(display[index].toLowerCase() === 'quantity' || display[index].toLowerCase() === 'qty' || display[index].toLowerCase() === 'qty.'){
    		parameter.push('Quantity');
    	}
    	if(display[index].toLowerCase() === 'model' || display[index].toLowerCase() === 'model no.'  || display[index].toLowerCase() === 'model no'){
    		parameter.push('ModelNo');
    		 	}
    }
	var newdisplay = new Array();
	for (var index = 0; index< display.length; index++) {
		if(display[index].toLowerCase() != 'quantity' && display[index].toLowerCase() != 'qty' && display[index].toLowerCase() != 'qty.' &&
			display[index].toLowerCase() != 'model' && display[index].toLowerCase() != 'model no.' 
				&& display[index].toLowerCase() != 'model no'){
			newdisplay.push(display[index]);
		}}
	for (var index = 0; index< newdisplay.length; index++) {
		parameter.push(columnModel[index]);
		}
    return parameter;
} 
