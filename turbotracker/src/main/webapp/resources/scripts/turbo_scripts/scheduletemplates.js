jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	loadScheduleGrid();
	loadScheduleDetailsGrid(-1);
	
	loadAccessoriesGrid(-1);
	$('#date').datepicker();
	$('#DateOfJournal').datepicker();
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
	document.getElementById("scheduleDetailsFromID").reset();
	
});

var newDialogDiv = jQuery(document.createElement('div'));
var aManufacturerId = 0;
$(function() { 
	
	var cache = {}; var  lastXhr='';
$("#manufacturer").autocomplete({ minLength: 2,timeout :1000, select: function(event, ui) {
		$("#manufacturer").val(ui.item.label);
	},
	source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
		lastXhr = $.getJSON("scheduleTempController/joSchedManufacturer", request, function(data, status, xhr) {
			cache[term] = data; if (xhr === lastXhr) {response(data); }
		});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});



var cache = {}; var  lastXhr='';
$("#aManufacturer").autocomplete({ minLength: 2,timeout :1000, select: function(event, ui) {
	$("#aManufacturer").val(ui.item.label);
	aManufacturerId = ui.item.id;
},
source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
	lastXhr = $.getJSON("scheduleTempController/joSchedManufacturer", request, function(data, status, xhr) {
		cache[term] = data; if (xhr === lastXhr) {response(data); }
	});
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} 
});

});

var rxMasterId = "";
var vendorName = "";
var aDescription ="";
var joSchedTempHeaderId = -1;
var orCode = "";

function loadScheduleGrid(){
	$("#scheduleGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'GET',
		url: './scheduleTempController/joscheduleDiscription',
		colNames:['Scheddule Header Id','Description','Manufacturer Id','Product','Land Scape'],
	   	colModel:[
{name:'joSchedTempHeaderId',index:'joSchedTempHeaderId', width:100,editable:true,hidden: true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'rxManufacturerId',index:'rxManufacturerId', width:100,hidden: true,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'product',index:'product', width:100,editable:true,hidden: true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'printLandScape',index:'printLandScape', width:100,hidden: true,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfAccountsGridPager',
		sortorder: "asc",
		altRows: true,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: 'Template Description',
		height:450,	width: 250,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		loadonce: false,
		
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
            userdata: "userdata"
    	},
    	onSelectRow: function(rowId){
    		
    		var rowData = jQuery("#scheduleGrid").getRowData(rowId); 
    		var description = rowData['description'];
    		orCode = description;
    		var product = rowData['product'];
    		aDescription  = product;
    		joSchedTempHeaderId = rowData['joSchedTempHeaderId'];
    		var aRxMasterId = rowData['rxManufacturerId'];
    		rxMasterId = aRxMasterId;
    		createtpusage('Company-Settings-Model Maintenance','Grid Data View','Info','Company-Settings-Model Maintenance,Viewing Schedule Template,rxMasterId:'+rxMasterId); 
    		$.ajax({
    			url: "./scheduleTempController/getMasterName",
    			type: "POST",
    			data :  {aRxMasterId: aRxMasterId},
    			success: function(data) {
    				vendorName = data;
    				$("#manufacturer").val(data);
    			}
    		});
    		$("#schedule").val(description);
    		$("#description").val(product);
    		 $("#scheduleDetailsGrid").jqGrid("setGridParam", {
    			 url: './scheduleTempController/getScheduleDetails',
    			 	mtype: 'POST',
    	            datatype: "json",
    	            	postData: {'schedTempHeaderID':joSchedTempHeaderId },
    	        }).trigger("reloadGrid");

    	}
	})/*.navGrid('#chartsOfAccountsGridPager',
		{add:false,edit:false,del:false,refresh:false,search:false})*/;
	return true;
}
function loadScheduleDetailsGrid(joSchedTempHeaderID) {
	$("#scheduleDetailsGrid").jqGrid({
		datatype: 'JSON',
		postData: {'schedTempHeaderID':joSchedTempHeaderID },
		mtype: 'POST',
		url: './scheduleTempController/getScheduleDetails',
		colNames: ['Id','Type', 'Display Text', 'Display Width', 'Print Text', 'Print Width', 'Inactive'
		           , 'Copy Defaults', 'Import To Order As','Column Number'],
		colModel:
		[	{name:'joSchedTempColumnId',index:'joSchedTempColumnId',align:'right',width:30, hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 	{name:'scheduleDescription',index:'scheduleDescription',align:'left',width:30, hidden: false, editable:true,edittype:'select',editoptions:{
		 		
                     dataUrl: "./scheduleTempController/getScheduleType",   
                            buildSelect: function(data) {  
                            	var myData = JSON.parse(data);
                                    var s = '<select id="scheduleDescription">';  
                                    $.each(myData, function() {
                                    	s += '<option value="'+this.joSchedTempColumnTypeId+'">'+this.description+'</option>';  
                                       });
                                    
                                   return s + "</select>";  
                           }  
                    }  },
		 	{name:'displayText',index:'displayText',align:'left',width:30, hidden: false, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
		 	{name:'displayWidth',index:'displayWidth',align:'right',width:30, editable:true,editrules:{required:false}},
		 	{name:'printText',index:'printText',align:'left',width:30, editable:true,editrules:{required:false}},
		 	{name:'printWidth',index:'printWidth',align:'right',width:30, editable:true,editrules:{required:false}},
		 	{name:'inactive',index:'inactive',align:'center',width:30,formatter:'checkbox', editable:true,edittype:'checkbox',editoptions:{value:'Yes:No'}},
		 	{name:'copyDefaults',index:'copyDefaults',align:'center',width:30,formatter:'checkbox', editable:true,edittype:'checkbox',editoptions:{value:'Yes:No'}},
		 	{name:'importToOrder',index:'importToOrder',align:'center',width:30,formatter:selectFilter,
		 		editable:true,edittype:'select', editoptions:{value:{0:'No',1:'Lin Itm',2:'Notes'}} , editrules:{required:true},display:false},
		 		{name:'columnNumber',index:'columnNumber',align:'left',hidden: true,width:30, editable:true,editrules:{required:false}}
		 ],
		 rowNum: 20,
		 rowList: [50, 100, 200, 500, 1000],
		 pgbuttons: true,
			altRows: true,
		 viewrecords: true,
		 pager: '#scheduleDetailsGridpager',
		 sortname:'columnNumber',
		 sortorder:"desc",
		 imgpath:'themes/basic/images',
		 height:260,
		recordtext:'',
		 altclass:'myAltRowClass',
	 	 width:840,
		onSelectRow : function(id) {
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
    	editurl:"./scheduleTempController/manpulaterScheduleTemplate",
		loadComplete: function(data){
			jQuery("#scheduleDetailsGrid").jqGrid('navGrid','#scheduleDetailsGridpager',{edit:true,add:true,del:true,view:false,search:false,refresh:false},
			//-----------------------edit options----------------------//
			{
				width:515, left:400, top: 300, zIndex:1040,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Schedule",
				beforeSubmit:function(postdata, formid) {
					var displayText = $('#displayText').val();
					if (displayText === ""){ return [false, "Alert: Please provide a valid Display Text."]; }
					return [true, ""];
				},
				onclickSubmit: function(params){
					var joSchedTempColumnId = $('#joSchedTempColumnId').val();
					var displayWidth = $('#displayWidth').val();
					var displayText = $('#displayText').val();
					var printText = $('#printText').val();
					var printWidth = $('#printWidth').val();
					var inactive = false;
					var copyDefaults = false;
					var scheduleDescription = $('#scheduleDescription').val();
					var importToOrder = $('#importToOrder').val();
					if($("#inactive").prop('checked') == true){
						inactive = true;
					}
					if($("#copyDefaults").prop('checked') == true){
						copyDefaults = true;
					}
					return { 'joSchedTempColumnId' : joSchedTempColumnId, 'displayText' : displayText, 'displayWidth' : displayWidth
						, 'printText' : printText, 'printWidth' : printWidth
						, 'inactive' : inactive, 'copyDefaults' : copyDefaults, 'importToOrder' : importToOrder,'scheduleDescription':scheduleDescription};
				}
			},
			//-----------------------add options----------------------//
			{
				width:500, left:400, top: 300, zIndex:1040,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add  Schedule",
				
				beforeSubmit:function(postdta, formid) {
					var displayText = $('#displayText').val();
					if (displayText === ""){ return [false, "Alert: Please provide a valid Display Text."]; }
					return [true, ""];
				},
				onclickSubmit: function(params){
					var displayWidth = $('#displayWidth').val();
					var displayText = $('#displayText').val();
					var printText = $('#printText').val();
					var printWidth = $('#printWidth').val();
					var inactive = false;
					var copyDefaults = false;
					var importToOrder = $('#importToOrder').val();
					if($("#inactive").prop('checked') == true){
						inactive = true;
					}
					if($("#copyDefaults").prop('checked') == true){
						copyDefaults = true;
					}
					return {  'displayText' : displayText, 'displayWidth' : displayWidth
						, 'printText' : printText, 'printWidth' : printWidth
						, 'inactive' : inactive, 'copyDefaults' : copyDefaults, 'importToOrder' : importToOrder,'joSchedTempHeaderId':joSchedTempHeaderId};
				},
				
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
				caption: "Delete Schedule Type",
				msg: 'Delete the Schedule Type?',

				onclickSubmit: function(params){
					var id = jQuery("#scheduleDetailsGrid").jqGrid('getGridParam','selrow');
					var key = jQuery("#scheduleDetailsGrid").getCell(id, 0);
					return {'joSchedTempColumnId':key};
				}
			}
			);
		}
	});
	
	return true;
}



function selectFilter(cellvalue, options, cellobject)
{
	var result = "";
	if(cellvalue === 0)result = "No";
	if(cellvalue === 1)result = "Lin Itm";
	if(cellvalue === 2)result = "Notes";
	return result;
	}

function search()
{
	if(vendorName != ""){
		var checkpermission=getGrantpermissionprivilage('Rolodex',0);
		if(checkpermission){
	document.location.href = "./rolodexdetails?rolodexNumber="+rxMasterId+"&name="+vendorName+"";
		}
	}
	}



jQuery( function(){
	jQuery("#addNewTemplateDialog").dialog({
		autoOpen:false,
		height:240,
		width:450,
		title:"Add New Template",
		modal:true
	});
});

jQuery( function(){
	jQuery("#accessoriesDialog").dialog({
		autoOpen:false,
		height:440,
		width:400,
		title:"Accessories (Acc'y)",
		modal:true
	});
});

function loadAccessoriesGrid(joSchedTempColumnId){
	$("#accessoriesGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		postData: {'joSchedTempColumnId':joSchedTempColumnId },
		 pager: '#accessoriesPager',
		url: './scheduleTempController/getAccessories',
		colNames:['Acessories Id','Scheddule Column Id','Code','Description'],
	   	colModel:[
{name:'joSchedAccessoryId',index:'joSchedAccessoryId', width:100,editable:true,hidden: true, editrules:{required:false}},
			{name:'joSchedTempColumnId',index:'joSchedTempColumnId', width:100,editable:true,hidden: true, editrules:{required:false}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'code',index:'code', width:100,hidden: false,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,hidden: false,editable:true, editrules:{required:false}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		viewrecords: false,
		//rowList: [500, 1000],
		//viewrecords: true,
		//pager: '#chartsOfAccountsGridPager',
		sortorder: "asc",
		altRows: false,
		hidegrid: false,
		altclass:'myAltRowClass',
		imgpath: 'themes/basic/images',
		caption: '',
		height:300,	width: 350,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
		editurl:"./scheduleTempController/manpulaterAccessories",
		loadComplete:function(data) {
			$("#alertmod").css('z-index', '1067');
			jQuery("#accessoriesGrid").jqGrid('navGrid','#accessoriesPager',
					{edit:true,add:true,del:true,view:false,search:false,refresh:false},
			//-----------------------edit options----------------------//
			{
				width:300, left:400, top: 300, zIndex:1020,
				closeAfterEdit:true, reloadAfterSubmit:true,
				modal:true, jqModel:true,
				editCaption: "Edit Accessories",
				
				beforeSubmit:function(postdta, formid) {
					var code = $('#code').val();
					if (code === ""){ return [false, "Alert: Please provide a valid Code."]; }
					return [true, ""];
				},
				onclickSubmit: function(params){
					var code = $('#code').val();
					var joSchedAccessoryId = $('#joSchedAccessoryId').val();
					var description = $('#description').val();
					return {'joSchedAccessoryId':joSchedAccessoryId,'code':code,'description':description};
				}
				
			},
			//-----------------------add options----------------------//
			{
				width:300, left:400, top: 300, zIndex:1020,
				closeAfterAdd:true,	reloadAfterSubmit:true,
				modal:true, jqModel:false,
				addCaption: "Add  Accessories",
				
				beforeSubmit:function(postdta, formid) {
					var code = $('#code').val();
					if (code === ""){ return [false, "Alert: Please provide a valid Code."]; }
					return [true, ""];
				},
				onclickSubmit: function(params){
					var id = jQuery("#scheduleDetailsGrid").jqGrid('getGridParam','selrow');
					var joSchedTempColumnId = jQuery("#scheduleDetailsGrid").getCell(id, 'joSchedTempColumnId');
					var code = $('#code').val();
					var description = $('#description').val();
					return {'joSchedTempColumnId':joSchedTempColumnId,'code':code,'description':description};
				}
				
			},
			//-----------------------Delete options----------------------//
			{	
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1020,
				caption: "Delete Schedule Type",
				msg: 'Delete the Schedule Type?',
				onclickSubmit: function(params){
					var id = jQuery("#accessoriesGrid").jqGrid('getGridParam','selrow');
					var key = jQuery("#accessoriesGrid").getCell(id, 'joSchedAccessoryId');
					return {'joSchedAccessoryId':key};
				}
			}
			);
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
    	onSelectRow: function(rowId){}
	});
	
	return true;
}

function openAccessories()
{
	var id = jQuery("#scheduleDetailsGrid").jqGrid('getGridParam','selrow');
	var joSchedTempColumnId = jQuery("#scheduleDetailsGrid").getCell(id, 'joSchedTempColumnId');
	var scheduleDescription = jQuery("#scheduleDetailsGrid").getCell(id, 'scheduleDescription');
	createtpusage('Company-Settings-Schedule Templates','Accessories','Info','Company-Settings-Schedule Templates,Accessories,scheduleDescription:'+scheduleDescription);
	if(scheduleDescription === "Accessories")
		{
		if(!joSchedTempColumnId)
		{
		jQuery(newDialogDiv).html('<span style="color:red"> Please select one Accessories</span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
			buttons:{
				
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return false;
		}
	else
		{
		 $("#accessoriesGrid").jqGrid("setGridParam", {
			 url: './scheduleTempController/getAccessories',
			 	mtype: 'POST',
	            datatype: "json",
	            	postData: {'joSchedTempColumnId':joSchedTempColumnId },
	        }).trigger("reloadGrid");
	$("#accessoriesDialog").dialog("open");	
		return true;
		}
		}else
			{

			jQuery(newDialogDiv).html('<span style="color:red"> Please select valid Accessories</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
				buttons:{
					
					Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
			return false;
			
			}

	}

function closeTemplate()
{
	$("#addNewTemplateDialog").dialog("close");	
	return true;
	}

function addDescription()
{
	var aCode = $("#aCode").val();
	var aDescription =  $("#aDescription").val();
	$.ajax({
		url: "./scheduleTempController/addTemplateDescription",
		type: "POST",
		data :  {aManufacturer: aManufacturerId,aCode : aCode, aDescription : aDescription },
		success: function(data) {
			if(data.success)
				{
				 $('#scheduleGrid').trigger("reloadGrid");
					$("#addNewTemplateDialog").dialog("close");	
					return true;
				}
			else
				{
				jQuery(newDialogDiv).html('<span style="color:red">'+data.message+'</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
					buttons:{
						"OK": function(){
							
							jQuery(this).dialog("close");
						},
						Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
				return false;

		     
				}
		},
	    error: function(errorThrown){
			jQuery(newDialogDiv).html('<span>'+errorThrown+'</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
				buttons:{
					"OK": function(){
						
						jQuery(this).dialog("close");
					},
					Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
			return true;

	     }
	});
	}

function copyTemplate(){
		jQuery(newDialogDiv).html('<span><lable>Description: </lable><input type="text" id="copytext" name="copytext" value="'+orCode+'"/></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Copy Template", 
			buttons:{
				"OK": function(){
					copyScheduleTemplate();
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Template Copied successfully.</b></span>');
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
		return true;
}

function copyScheduleTemplate()
{
	var code = $("#copytext").val();
	if(joSchedTempHeaderId != -1)
		{
		$.ajax({
			url: "./scheduleTempController/copyTemplate",
			type: "POST",
			data :  {rxMasterId: rxMasterId,vendorName:vendorName,code:code,aDescription:aDescription,joSchedTempHeaderId:joSchedTempHeaderId},
			success: function(data) {
			if(data.success)
				{
				createtpusage('Company-Settings-Schedule Templates','Copy Template','Info','Company-Settings-Schedule Templates,Copy Template,Description:'+$("#copytext").val());
				 $('#scheduleGrid').trigger("reloadGrid");
					return true;
				}
			else
				{
				jQuery(newDialogDiv).html('<span style="color:red">'+data.message+'</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Warnings", 
					buttons:{
						"OK": function(){
							
							jQuery(this).dialog("close");
						},
						Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
				return false;

		     
				}
		}
		});
		}
	
	}


function addTemplate() {
	document.getElementById("addNewTemplateFormID").reset();
jQuery("#addNewTemplateDialog").dialog("open");	
return true;
}

