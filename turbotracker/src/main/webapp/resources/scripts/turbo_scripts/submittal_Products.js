jQuery(document).ready(function() {
	var subHeaderID = $("#submittal_header_ID").val();
	loadProductGrid(subHeaderID);
});

function loadProductGrid(subHeaderID){
	$("#product").jqGrid({
		url:'./jobtabs3/submittalProducts',
		datatype: 'JSON',
		mtype: 'GET',
		postData: { 'submittalHeaderID' : subHeaderID },
		pager:jQuery("#productpager"),
		colNames:['Schedule','Product','Qty.','Details','Manufacturer','Est.Cost ($)', 'JoSubmittalID'],
		colModel:[
		          {name:'schedule',index:'schedule',align:'left',width:40,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:25},editrules:{edithidden:false,required:false}},
		          {name:'product',index:'product',align:'left',width:80,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:25},editrules:{edithidden:false,required:false}},
		          {name:'quantity',index:'quantity',align:'center',width:20,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:5},editrules:{edithidden:false,required:false}},
		          {name:'released',index:'released',align:'center',width:50,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:20},editrules:{edithidden:false,required:false}},
		          {name:'manufacture',index:'manufacture',align:'left',width:50,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:25},editrules:{edithidden:false,required:false}},
		          {name:'estimatecost',index:'estimatecost',align:'right',width:40,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, formatter:customCurrencyFormatter,  editoptions:{size:20},editrules:{edithidden:false,required:false}},
		          {name:'joSubmittalID',index:'joSubmittalID',align:'right',width:40,editable:true,hidden: true, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';},  editoptions:{size:20},editrules:{edithidden:false,required:false}}],
		          pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
		          height:325,width: 810,caption:"Line Item", altRows: true,
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
		          loadComplete: function(data) {
		        	  
		          },
		          onSelectRow: function(id){
		        	  var rowData = jQuery(this).getRowData(id); 
		        	  var product = rowData["joSubmittalID"];
		        	  var productName = rowData["product"];
		        	  $("#detailsHeader_ID").text(product);
		        	  $("#productName_ID").text(productName);
		        	  //$("#submittalschedule").empty();
		        	  //$("#submittalschedule").append("<a href= submittal_Schedule>Schedule ("+productName+")</a></li>");
		          },
		          editurl:"./jobtabs3/manpulaterSubmittal_Product"
	}).navGrid("#productpager",
			{add:true,del:true,search: false,edit:false,pager:false},
			//-----------------------edit options----------------------//
			{
				closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
				modal:true,jqModal:true,viewPagerButtons: false,editCaption: "Edit Product",	width: 390, top: 356, left: 207
			},
			//-----------------------add options----------------------//
			{
				closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
				modal:true,
				jqModal:true,
				viewPagerButtons: false,
				addCaption: "Add Product",
				width: 390, top: 356, left: 207,
				
				beforeShowForm: function (form) 
				{
					$(function() { var cache = {}; var lastXhr=''; $("#schedule" ).autocomplete({ minLength: 2,timeout :1000,
						select: function( event, ui ) { 
							var scheduleId = ui.item.id; $("#scheduledID").val(scheduleId);
							$.ajax({
								url: "./jobtabs3/productName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
								success: function(data){ var name = data.description;  $("#product").val(name); var manufactureID = data.rxManufacturerId;
								$("#manufactureID").val(manufactureID); }
							});
							$.ajax({
								url: "./jobtabs3/ManufactureName", mType: "GET", data : { 'scheduleHeaderId' : scheduleId },
								success: function(data){ var manufaurer = data; $("#manufacture").val(manufaurer); } 
							});
						},
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs3/scheduleList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
						error: function (result) {
							$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} }); });
					
					$(function() { var cache = {}; var lastXhr=''; $("#product" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) { },
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs3/productList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
						error: function (result) {
							$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} }); });
					
					$(function() { var cache = {}; var lastXhr=''; $("#manufacture" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) {   },
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
						lastXhr = $.getJSON( "jobtabs3/manufactureList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
						error: function (result) {
							$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						} }); });
				},
				'onInitializeForm' : function(formid){
					jQuery('#TblGrid_product #tr_schedule .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;">');
					jQuery('#TblGrid_product #tr_schedule .DataTD').append('<td><input type="hidden" id="scheduledID" name="scheduledID" style="width: 50px;"</td>');
					jQuery('#TblGrid_product #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;">');
					jQuery('#TblGrid_product #tr_manufacture .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;">');
					jQuery('#TblGrid_product #tr_manufacture .DataTD').append('<td><input type="hidden" id="manufactureID" name="manufactureID" style="width: 50px;"</td>');
					jQuery('#TblGrid_product #tr_schedule .CaptionTD').append('<span style="color:red;"> *</span>');
				},
				onclickSubmit: function(params){
					var scheduledID = $("#scheduledID").val();
					var manufacureID = $("#manufactureID").val();
					var submittalHeaderID = "${requestScope.joSubmittalDetails.joSubmittalHeaderId}";
					return { 'scheduledID' : scheduledID, 'manufacturerID' : manufacureID, 'submittalHeaderID' : submittalHeaderID };
				}
			},
			//-----------------------Delete options----------------------//
			{
				closeOnEscape: true, reloadAfterSubmit: true, modal:true, width:270, jqModal:true, top: 356, left: 207,
				caption: "Delete Product",
				msg: 'Do you want delete selected Product?',
				
				onclickSubmit: function(params){
					var id = jQuery("#product").jqGrid('getGridParam','selrow');
					var key = jQuery("#product").getCell(id, 6);
					return { 'productID' : key};
				}
			}
	);
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}