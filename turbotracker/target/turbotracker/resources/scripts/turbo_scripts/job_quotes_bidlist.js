function loadBidListGrid(){
			var grid =$("#quotesBidlist"); 
			grid.jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/jobquotesbidlist',
				//pager: jQuery('#quotesBidlistPager'),
				colNames:['BidderId', 'Bidder','Low','Contact', 'Type', 'TypeId', 'Last Quote', 'Rev.', 'JobNumber','RxMasterId', 'joMasterId', 'rxContactId', 'Rep.', 'Email'],
				colModel :[ 
		            {name:'bidderId', index:'bidderId', align:'', width:40, editable:true,hidden:true, edittype:'text',editoptions:{size:30},editrules:{required: false}},
					{name:'bidder', index:'bidder', align:'', width:120, editable:true, edittype:'text',editoptions:{size:30}, editrules:{required:true}},
					{name:'low', index:'low', align:'center', width:30,editable:true, formatter:'checkbox', edittype:'checkbox'},
					{name:'contact', index:'contact',align:'',width:80,editable: true,edittype:'text',editoptions:{size:1},editrules:{required: false}},
					{name:'quoteType', index:'quoteType', align:'center', width:40,editable:true,editoptions:{size:30,readonly:false},editrules:{required: true}},
					{name:'quoteTypeID', index:'quoteTypeID',width:30,hidden:true, editable:true,editoptions:{size:30,readonly:false}},
					{name:'lastQuote', index:'lastQuote',align:'center',width:65,editable:true,editoptions:{size:10,readonly:false,dataInit:function(element) {$(element).datepicker({dateFormat: 'mm/dd/yy hh:mm a'});}},editrules:{required: false},formatter: mailImage},
					{name:'rev',index:'rev', align:'center',width:30,editable:true,editoptions:{size:6,readonly:false},editrules:{required: false}},
					{name:'jobNumber', index:'jobNumber',width:30,hidden:true,editable:true,edittype:'text',editrules:{required:false}},
					{name:'rxMasterId', index:'rxMasterId',width:30,hidden:true,editable:true,edittype:'text',editrules:{required:false}},
					{name:'joMasterId', index:'joMasterId',width:40,hidden:true,editable:true,edittype:'text',editrules:{required:false}},
					{name:'rxContactId', index:'rxContactId',width:40,hidden:true,editable:true,edittype:'text',editrules:{required:false}},
					{name:'rep',index:'rep',align:'center',width:30,hidden:false,editable:true,edittype:'text',editrules:{edithidden:true,required:false}},
					{name:'email',index:'email',align:'center',width:40,hidden: true,editable:true,edittype:'text',editrules:{edithidden:true,required:false}}
				],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'employeeId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height: 200, width: 1080, rownumbers:true, altRows: true, altclass:'myAltRowClass',
				postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }},
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
				onSelectRow: function(id){
					var  row= jQuery("#quotesBidlist").jqGrid('getRowData',id);
					$("#selectBidderId").val(row.bidderId);
				},
				ondblClickRow: function(rowId) {
					var rowData = jQuery(this).getRowData(rowId); 
					var rxMaster = rowData['rxMasterId'];
					var name = rowData['bidder'];
					var name1 = name.replace('&', 'and');
		    		var name2 = name1.replace('&', 'and');
					document.location.href = "./customerdetails?rolodexNumber="+rxMaster+"&name="+'('+name2+')';
				},
				loadComplete: function(data) {  },
				loadError : function (jqXHR, textStatus, errorThrown){ }
				//editurl:"./jobtabs2/manpulateQuotebidList"
			}).navGrid('',
				{add:true, edit:true,del:true,refresh:true,search:false, alertcap: "Warning", alerttext: 'Please select a Bidder' }, //options
				//-----------------------edit options----------------------//
				{
					closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
					modal:true,
					jqModal:false,
					viewPagerButtons: false,editCaption: "Edit Bidder",	width: 520, top: 10, left: 300,
					
					beforeShowForm: function (form) 
					{
						jQuery("#tr_rep", form).hide();
						jQuery("#tr_low", form).show();
						$(function() {var cache = {}; var lastXhr=''; $("#bidder").autocomplete({ minLength: 2,select: function( event, ui ) 
						{ 
							var rxMasterid = ui.item.id; $("#rxMasterId").val(rxMasterid); $.ajax({
								url: "./jobtabs2/filterBidderList", mType: "GET", data: { 'rxMasterId' : rxMasterid },
								success: function(data) {
									var select = '<select style="width:180px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option>';
									$.each(data, function(index, value){
										var quoteId = value.id;
										var quoteName = value.value;
										select +='<option value='+quoteId+'>'+quoteName+'</option>';
									});
									select += '</select>';
									jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').empty();
									jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').append(select);
								}
							});
							getContactId = function(){$("#rxContactId").val($("#contactId").val());}; 
						},
						source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	     				lastXhr = $.getJSON( "jobtabs2/bidderList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); } }); });
	
						$(function() { var cache = {}; var lastXhr='';
							$( "#quoteType" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; $("#quoteTypeId").val(id); },
								source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
									lastXhr = $.getJSON( "employeeCrud/customerType", request, function( data, status, xhr ) {
										cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } 
									});
								}
							});
						});
						
						var rxMasterId = $("#rxMasterId").val();
						$.ajax({
							url: "./jobtabs2/filterBidderList", mType: "GET", data : { 'rxMasterId' : rxMasterId },
							success: function(data){
							 var select = '<select style="width:180px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option>';
								$.each(data, function(index, value){
									var quoteId = value.id;
									var quoteName = value.value;
									select +='<option value='+quoteId+'>'+quoteName+'</option>';
								});
								select += '</select>';
								jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').empty();
								jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').append(select);
							}
						});
						getContactId = function(){$("#rxContactId").val($("#contactId").val());};
						var rxContactId = $("#rxContactId").val();
						setTimeout("jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD option[value="+rxContactId+"]').attr('selected','selected')",1000);
						return true;
					},
					'onInitializeForm' : function(formid){
						$('#contact').empty();
						jQuery('#TblGrid_quotesBidlist #tr_bidder .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
						jQuery('#TblGrid_quotesBidlist #tr_quoteType .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;">');
						jQuery('#TblGrid_quotesBidlist #tr_bidder .CaptionTD').append('<span style="color:red;"> *</span>');
						//jQuery('#TblGrid_quotesBidlist #tr_low .CaptionTD').append('<span style="color:red;"> *</span>');
						jQuery('#TblGrid_quotesBidlist #tr_quoteType .CaptionTD').append('<span style="color:red;"> *</span>');			
					},
					onclickSubmit: function(params){
						var id = jQuery("#quotesBidlist").jqGrid('getGridParam','selrow');
						var bidderId = jQuery("#quotesBidlist").getCell(id, 1);
						var quoteTypeName = jQuery("#quotesBidlist").getCell(id, 5);
						var jobNumber = jQuery("#quotesBidlist").getCell(id, 9);
						var rxMasterId = jQuery("#quotesBidlist").getCell(id, 10);
						var joMasterId = jQuery("#quotesBidlist").getCell(id, 11);
						var rxContactId = jQuery("#quotesBidlist").getCell(id, 12); 
						rxMasterId = $("#rxMasterId").val();
						rxContactId = $("#contactId").val();
						if(rxContactId === ""){
							rxContactId = -1;
						}
						quoteTypeId = $("#quoteTypeId").val();
						return { 'selectBidderId' : bidderId, 'rxMasterId' : rxMasterId, 'joMasterId' : joMasterId, 'rxContactId' : rxContactId, 'jobNumber' : jobNumber, 'quoteType' : quoteTypeName, 'quoteTypeId' : quoteTypeId};
					}
				},
				//-----------------------add options----------------------//
				{
					closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
					//modal:true,
					jqModal:false,
					viewPagerButtons:false,
					addCaption:"Add Bidder",
					width: 520, top: 10, left: 300,
					
					beforeShowForm: function (form) 
					{
						jQuery("#tr_rep", form).hide();
						jQuery("#tr_low", form).hide();
						jQuery("#tr_lastQuote", form).hide();
						jQuery("#tr_rev", form).hide();
						
						$(function() { var cache = {}; var lastXhr=''; $( "#bidder" ).autocomplete({ minLength: 2,select: function( event, ui ) 
							{ 
								var rxMasterid = ui.item.id; $("#rxMasterId").val(rxMasterid); 
								$.ajax({
									url:"./jobtabs2/filterBidderList", mType:"GET", data:{'rxMasterId':rxMasterid},
									success: function(data){
										var select = '<select style="width:180px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option>';
										$.each(data, function(index, value){
											var quoteId = value.id;
											var quoteName = value.value;
											select +='<option value='+quoteId+'>'+quoteName+'</option>';
										});
										select += '</select>';
										jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').empty();
										jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').append(select);
									}
								}); 
								getContactId = function(){
									$("#rxContactId").val($("#contactId").val());
								};
							},
							source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
		     				lastXhr = $.getJSON( "jobtabs2/bidderList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); } }); });
	
							$(function() { var cache = {}; var lastXhr='';
							$( "#quoteType" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; $("#quoteTypeId").val(id); },
							source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	}
							lastXhr = $.getJSON( "employeeCrud/customerType", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); } }); });
							
							var rxMasterId = $("#rxMasterId").val();
							$.ajax({
								url: "./jobtabs2/filterBidderList",
								mType: "GET",
								data : { 'rxMasterId' : rxMasterId },
								success: function(data){
									var select = '<select disabled="disabled" style="width:180px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option>';
									$.each(data, function(index, value){
										var quoteId = value.id;
										var quoteName = value.value;
										select +='<option value='+quoteId+'>'+quoteName+'</option>';
									});
									select += '</select>';
									jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').empty();
									jQuery('#TblGrid_quotesBidlist #tr_contact .DataTD').append(select);
								}
							}); 
							getContactId = function(){
								$("#rxContactId").val($("#contactId").val());
						};
						return true;
					},
					'onInitializeForm' : function(formid){
						$('#contact').empty();	
						jQuery('#TblGrid_quotesBidlist #tr_bidder .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" style="padding-left: 10px;">');
						jQuery('#TblGrid_quotesBidlist #tr_quoteType .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;">');
						jQuery('#TblGrid_quotesBidlist #tr_bidder .CaptionTD').append('<span style="color:red;"> *</span>');
						jQuery('#TblGrid_quotesBidlist #tr_quoteType .CaptionTD').append('<span style="color:red;"> *</span>');
					},
					onclickSubmit: function(params){
						var jobNumber = $(".jobHeader_JobNumber").val();
						var rxMaster = $("#rxMasterId").val();
						var rxContact = $("#rxContactId").val();
						if(rxContact === ""){
							rxContact = -1;
						}
						return {'jobNumber':jobNumber,'rxMasterId':rxMaster,'rxContactId':rxContact};
					}
				},
				//-----------------------Delete options----------------------//
				{
					closeOnEscape: true, reloadAfterSubmit: true,
					//modal:true,
					jqModal:false, top: 10, left: 300,
					caption: "Delete Bidder",
					msg: 'Do you want delete selected Bidder?',
	
					onclickSubmit: function(params){
						var id = jQuery("#quotesBidlist").jqGrid('getGridParam','selrow');
						var key = jQuery("#quotesBidlist").getCell(id, 1);
						return { 'selectBidderId' : key};
					} 
				},
				//-----------------------search options----------------------//
				{}
			);
			return true;
		}

		function mailImage(cellValue, options, rowObject){
			var element = '';
		   if(cellValue !== '' && cellValue !== null){
			   element = "<img src='./../resources/images/message-already-read.png' title='Mail Sent'  style='vertical-align:middle;'>" +cellValue ;
		   }else if(cellValue === ''){
			   element = "";
		   }else if(cellValue === null){
			   element = "";
		   }else{
			   element = "";
		   }
		return element;
		} 
		
		function loadQuotesGrid(){
			$("#quotes").jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/jobquoteslist',
				//pager: jQuery('#quotesPager'),
				colNames:['joQuoteHeaderID','quoteTypeID','Type','Rev.','Cost($)', 'Amount($)', 'Discount($)','CreatedByID','Created By','CreatedBYFullName', 'Internal Notes','Remarks','PQ',
						          'displayQuantity', 'printQuantity', 'displayParagraph', 'printParagraph', 'displayManufacturer', 'printManufacturer', 'displayCost', 'printCost', 'displayPrice', 
						          'printPrice', 'displayMult', 'printMult', 'displaySpec', 'printSpec', 'notesFullWidth', 'lineNumbers', 'printTotal', 'hidePrice'],
				colModel :[
					{name:'joQuoteHeaderID', index:'joQuoteHeaderID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
					{name:'quoteTypeID', index:'quoteTypeID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
					{name:'quoteType', index:'quoteType', align:'', width:60,editoptions:{},editrules:{}},
					{name:'rev', index:'rev', align:'center', width:30,editoptions:{},editrules:{}},
					{name:'costAmount', index:'costAmount', align:'center', width:60, formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'quoteAmount', index:'quoteAmount', align:'center', width:60,formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'discountAmount', index:'discountAmount', align:'center', width:60, formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'createdByID', index:'createdByID', align:'center', hidden:true, width:40, editoptions:{},editrules:{}},
					{name:'createdByName', index:'createdByName', align:'center', width:90, editoptions:{},editrules:{}},
					{name:'createdBYFullName', index:'createdBYFullName', align:'center', hidden:true, width:90, editoptions:{},editrules:{}},
					{name:'internalNote', index:'internalNote', align:'center', width:90, editoptions:{},editrules:{}},
					{name:'quoteRemarks', index:'quoteRemarks', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'primaryQuote', index:'primaryQuote', align:'center', width:40, formatter:'checkbox', edittype:'checkbox',editoptions:{}, editrules:{}},
					{name:'displayQuantity', index:'displayQuantity', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printQuantity', index:'printQuantity', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displayParagraph', index:'displayParagraph', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printParagraph', index:'printParagraph', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displayManufacturer', index:'displayManufacturer', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printManufacturer', index:'printManufacturer', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displayCost', index:'displayCost', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printCost', index:'printCost', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displayPrice', index:'displayPrice', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printPrice', index:'printPrice', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displayMult', index:'displayMult', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printMult', index:'printMult', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'displaySpec', index:'displaySpec', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printSpec', index:'printSpec', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'notesFullWidth', index:'notesFullWidth', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'lineNumbers', index:'lineNumbers', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'printTotal', index:'printTotal', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'hidePrice', index:'hidePrice', align:'center', width:90, hidden:true, editoptions:{},editrules:{}}],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'employeeId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:86, width: 1080, rownumbers:true, altRows:true, altclass:'myAltRowClass',
				postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }},
				loadComplete: function(data) {
				var aJobStatus = $("#jobStatusList").val();
				if(aJobStatus === '0'){
					if(jQuery("#quotes").getGridParam("records") !== 0){
						$("#jobStatusList option[value='1']").attr("selected","selected");
						aJobStatus = 1;
						var aJobNumber = $(".jobHeader_JobNumber").val();
						$.ajax({
							url: "./job_controller/updateJobStatus", mType: "GET", data: { 'jobStatus' : aJobStatus, 'jobNumber' : aJobNumber },
							success: function(data) {
								$("#jobStatusList option[value='1']").attr("selected","selected");
							}
						});
					}
				}
					/*var grid = $("#quotes");
					grid.setSelection(1, true);
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var data = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
					jQuery("#joQuoteheader").text(data);*/
				},
				ondblClickRow: function(rowid){
					onDoubleClickEditQuoteDetails(rowid);
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){
					var grid = $("#quotes");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var data = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
					jQuery("#joQuoteheader").empty();
					jQuery("#joQuoteheader").text(data);
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
				}
			});
			return true;
		}
		/**************************************** Quote Template related code start ************************************/
		/***
		 * Function to load Quote Templates.
		 */
		var aAddEditTemplate = "";
		function loadQuoteTemplates(){
			$("#quoteTemplates").jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/loadQuoteTemplates',
				//pager: jQuery('#quotesBidlistPager'),
				colNames:['joQuoteTemplateHeaderId', 'Template Name','ownerId', 'cuMasterTypeId','Remarks'],
				colModel :[ 
		            {name:'joQuoteTemplateHeaderId', index:'joQuoteTemplateHeaderId', align:'', width:40, editable:true,hidden:true},
					{name:'templateName', index:'templateName', align:'', width:110, editable:true, edittype:'text'},
					{name:'ownerId', index:'ownerId', align:'center', width:30,hidden:true,editable:true},
					{name:'cuMasterTypeId', index:'cuMasterTypeId', align:'center', width:30,hidden:true,editable:true},
					{name:'remarks', index:'remarks', align:'center', width:40,hidden:true,editable:true}
				],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'templateName', sortorder: "asc", imgpath: 'themes/basic/images', caption: 'Quote Templates',
				height: 140, width: 520, rownumbers:true, altRows: true, altclass:'myAltRowClass',
				postData: '',
				hiddengrid: true,
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
				onSelectRow: function(id){
					$('#joQuoteTemplateHeaderID').val($('#quoteTemplates').jqGrid ('getCell', id, 'joQuoteTemplateHeaderId'));
				},
				ondblClickRow: function(rowId) {
					var rowData = jQuery(this).getRowData(rowId); 
					var rxMasterId = rowData['rxMasterId'];
				},
				loadComplete: function(data) {  },
				loadError : function (jqXHR, textStatus, errorThrown){ }
				//editurl:"./jobtabs2/manpulateQuotebidList"
			});
		}
		
		jQuery(function(){
			var dialogWidth = 1150;
			jQuery("#addquoteTemplate").dialog({
				autoOpen:false, height:650, width:dialogWidth,top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], modal:true, title:'Add/Edit Template',  iframe: true,
				open: function(event, ui) {  
		            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
		        },
				buttons:{
					
				},
				close:function() {
						jQuery(this).dialog("close");
				}
			});
		});
		
		function openAddQuoteTemplateDialog() {				// Add quote template
			templateAddEdit = 'add';
			aAddEditTemplate = "add";
			$('#joQuoteTemplateHeaderID').val('');
			$("#quoteTemplateTypeDetail").val('-1');
			$("#templateDescription").val('');
			$("#quoteTemplateRemarksID").val('');
			jQuery("#addquoteTemplate").dialog("open");
			$('#quoteTemplateProductsGrid').jqGrid('GridUnload');
			addQuotetemplateLineItemGridColums('');
			$('#quoteTemplateProductsGrid').trigger("reloadGrid");
		}
		
		function openEditQuoteTemplateDialog(){			// Edit Quote TEmplate
					templateAddEdit = 'edit';
					aAddEditTemplate = "edit";
					var grid = $("#quoteTemplates");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var joQuoteTemplateHeaderId = grid.jqGrid('getCell', rowId, 'joQuoteTemplateHeaderId');					
					$('#joQuoteLineTemplateHeaderID').val(joQuoteTemplateHeaderId);
					var cuMasterTypeId = grid.jqGrid('getCell', rowId, 'cuMasterTypeId');
					$("#quoteTemplateTypeDetail").val(cuMasterTypeId);
					var ownerId = grid.jqGrid('getCell', rowId, 'ownerId');
					var remarks = grid.jqGrid('getCell', rowId, 'remarks');
					var TemlateName = grid.jqGrid('getCell', rowId, 'templateName');
					$("#templateDescription").val(TemlateName);
					$("#quoteTemplateRemarksID").val(remarks);
					if(!joQuoteTemplateHeaderId){
						var errorText = "Please select a row from the list to edit";
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
						return false;
					}
					jQuery("#addquoteTemplate").dialog("open");
					$('#quoteTemplateProductsGrid').jqGrid('GridUnload');
					addQuotetemplateLineItemGridColums(joQuoteTemplateHeaderId);
					$('#quoteTemplateProductsGrid').trigger("reloadGrid");
				}

		
		function deleteQuoteTemplateDialog(){
			aAddEditTemplate = "delete";
			var grid = $("#quoteTemplates");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var joQuoteTemplateHeaderId = grid.jqGrid('getCell', rowId, 'joQuoteTemplateHeaderId');
			var TemlateName = grid.jqGrid('getCell', rowId, 'templateName');
			if(!joQuoteTemplateHeaderId){
				var errorText = "Please select a template from the list to delete";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
				return false;
			}else{
				var errorText = "Do you want to delete the "+TemlateName+" template?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				 buttons: {
					"Yes": function(){
						jQuery(this).dialog("close");
						editTemplateSerialized ='&joQuoteTemplateHeaderId='+joQuoteTemplateHeaderId+'&oper='+aAddEditTemplate;
						$.ajax({
							url:'./jobtabs2/saveEditTemplate',
							data:editTemplateSerialized,
							type:"POST",
							success: function(data) {
								var errorText = TemlateName+" template is deleted successfully.";
								jQuery(newDialogDiv).attr("id","msgDlg");
								jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$("#quoteTemplates").trigger("reloadGrid");}}]}).dialog("open");
							}
						});
					},
					"No": function (){jQuery(this).dialog("close");	}
				}}).dialog("open");
				return true;
			}
		}
		
		/**Method to load the line item grid of quote template grid, this is called at the end of another method addQuotetemplateLineItemGridColums which 
		 * sends the dynamic colmn names and models of user choice, from joQuoteTemplateProperties. */
		function loadTemplateLineItemDetails(joQuoteTemplateHeaderId,dynamicColNames,dynamicColModels){
			$("#quoteTemplateProductsGrid").jqGrid({
				datatype: 'JSON',
				url:'./jobtabs2/getQuoteTemplateLineItems',
				mtype: 'POST',
				postData: {'joQuoteTemplateHeaderId': joQuoteTemplateHeaderId},
				pager: '',
				colNames:dynamicColNames,
				colModel:dynamicColModels,
				rowNum: 1000,pgbuttons: false,recordtext: '',rowList: [],pgtext: null,viewrecords: true,
				sortname: 'Product', sortorder: "asc", imgpath: 'themes/basic/images',caption: 'Line Items',
				height:190,width: 1100,
				loadComplete: function(data) {
					var gridData = jQuery("#quoteTemplateProductsGrid").getRowData();
					var totalPrice = 0;
					for(var index = 0; index < gridData.length; index++){
					var rowData = gridData[index];
					var price = rowData["price"].replace(/[^0-9\.]+/g,"");
					totalPrice = totalPrice + Number(price);
					}
					$('#quoteTemplateTotalPrice').val(formatCurrency(totalPrice));
				},
				ondblClickRow: function(rowid){
					
				},
				gridComplete: function () {
					
				},
				afterInsertRow: function(rowid, aData) {
					
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
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
				onSelectRow: function(id){
					QuoteTempLineItemHeader = $('#quoteTemplates').jqGrid ('getCell', id, 'joQuoteTemplateHeaderId');
					$('#joQuoteTemplateHeaderID').val(QuoteTempLineItemHeader);
				}
			});
		}
				/***********************************************Quote Template Inline Notes (Rich text editor) Nice Edit plugin ***************************************************************/
		
		/**image formatter that send back the image to the quote template Grid for the inline notes*/
		function imgFmatterTemp(cellValue, options, rowObject) {
			var element = "<div>"
					+ "<a onclick='addOpenTempLineItemDialog()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px 5px;'></a></div>";
			return element;
		}
		
		/**Method to show the inline notes dialog*/
		function addOpenTempLineItemDialog() {
			var rowId = $("#quoteTemplateProductsGrid").jqGrid('getGridParam', 'selrow');
			if (rowId === null) {
				var errorText = "Please select a line item to add Line Note";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv).html(
						'<span><b style="color:red;">' + errorText + '</b></span>');
				jQuery(newDialogDiv).dialog({modal : true, width : 300, height : 150, title : "Warning",buttons : [ {height : 35,text : "OK",click : function() {$(this).dialog("close");}} ]}).dialog("open");
				return false;
			}
			var lineItemText = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'inlineNote');
			var JopInlinetext = lineItemText.replace("&And", "'");
			area = new nicEditor({
				buttonList : [ 'bold', 'italic', 'underline', 'left', 'center','right', 'justify', 'ol', 'ul', 'fontSize', 'fontFamily','fontFormat', 'forecolor' ],
				maxHeight : 240}).panelInstance('lineItemIdTemp');
			$(".nicEdit-main").empty();
			$(".nicEdit-main").append(JopInlinetext);
			jQuery("#addInLineItemTemp").dialog("open");
			$(".nicEdit-main").focus();
			return true;
		}
		
		/**jquery dialog function for the inline notes in quote template grid */
		jQuery(function() {
			jQuery("#addInLineItemTemp").dialog({autoOpen : false,modal : true,title : "InLine Note",height : 370,width : 500,buttons : {},close : function() {return true;}});
		});
		
		/** method to save the inline notes in the quote template line items grid */
		function LineItemInfoTemp() {
			var inlineText = $('#addInLineItemIDTemp').find('.nicEdit-main').html();
			var rowId = $("#quoteTemplateProductsGrid").jqGrid('getGridParam', 'selrow');
			var quoteDetailID = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId, 'joQuoteTemplateDetailId');
			var joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId, 'joQuoteTemplateHeaderId');
			$.ajax({
				url : "./jobtabs2/saveQuoteTempInlineNote",
				type : "POST",
				data : { "joQuoteTempDetailId":quoteDetailID, "inlineNote": inlineText },
				success : function(data) {
					area.removeInstance('lineItemIdTemp');
					jQuery("#addInLineItemTemp").dialog("close");
					$("#quoteTemplateProductsGrid").jqGrid('GridUnload');
					addQuotetemplateLineItemGridColums(joQuoteTemplateHeaderId);
					$("#quoteTemplateProductsGrid").trigger("reloadGrid");
				}
			});
		}
		
		/**cancel method in quote template line item grid inline notes*/
		function cancelInLineNoteTemp() {
			area.removeInstance('lineItemIdTemp');
			jQuery("#addInLineItemTemp").dialog("close");
			return false;
		}

		
		/**Method to save the quote template (save & close button)*/
		function saveANDcloseQuoteTemplate(){
			templateAddEdit = "edit";
			var editTemplateSerialized = $("#quoteTemplateManipulateForm").serialize();
			joQuoteTemplateHeaderId == QuoteTempLineItemHeader;
			if(joQuoteTemplateHeaderId==false){
				joQuoteTemplateHeaderId=-1;
				if($("#quoteTemplateProductsGrid").getGridParam("reccount")==0)
					templateAddEdit = "Add";
				else{
					joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell',1,'joQuoteTemplateHeaderId');
				}
			}
			editTemplateSerialized =editTemplateSerialized+'&joQuoteTemplateHeaderId='+joQuoteTemplateHeaderId+'&oper='+templateAddEdit;
			var newDialogDiv = jQuery(document.createElement('div'));
			$.ajax({
				url:'./jobtabs2/saveEditTemplate',
				data:editTemplateSerialized,
				type:"POST",
				success:function(data){
					var errorText = "Add/Edit Template Changes are Saved.";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() {$('#quoteTemplates').trigger("reloadGrid"); $(this).dialog("close");$('#addquoteTemplate').dialog("close");}}]}).dialog("open");
				},
				error:function(error){
					var errorText = "Edit Template not Saved. Please try again later.";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$('#addquoteTemplate').dialog("close");}}]}).dialog("open");
				}
			});
		}
		function cancelQuoteTemplate(){
			$('#quoteTemplates').trigger("reloadGrid");
			jQuery("#addquoteTemplate").dialog("close");
		}
		function saveTemplateLineItem(){			//Save Quote Template Line item Grid
			var joQuoteTemplateHeaderID = $('#joQuoteTemplateHeaderID').val();
			var vendorId = $("#rxManufacturerIDTemplate").val();
			if(vendorId == null){
				var errorText = "Please provide a valid vendor from the List";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
			}
			if(joQuoteTemplateHeaderID=='false')
				$('#joQuoteTemplateHeaderID').val('');
			var aLineItemFormData = $("#addTemplateForm").serialize();
			var aQryStr = '';
			if(operForTemplateLineItem=='add'){
				var templateSaveQueryStr = $('#quoteTemplateManipulateForm').serialize();
				aQryStr="oper="+operForTemplateLineItem+"&operLineItem="+operForTemplateLineItem+"&"+aLineItemFormData+"&"+templateSaveQueryStr;
			}else{
				aQryStr="oper="+operForTemplateLineItem+"&operLineItem"+operForTemplateLineItem+"&"+aLineItemFormData;
			}
			$.ajax({
				url: "./jobtabs2/updateTemplateLineItem", 
				type: "POST", 
				data: aQryStr,
				success: function(data) {
					joQuoteTemplateHeaderId=data;
					$('#joQuoteTemplateHeaderID').val(data);
					$('#joQuoteLineTemplateHeaderID').val(data);
					templateAddEdit = "edit";
					$('#quoteTemplateProductsGrid').jqGrid('GridUnload');
					addQuotetemplateLineItemGridColums(data);
					$('#quoteTemplateProductsGrid').trigger("reloadGrid");
					$("#quoteTemplates").trigger("reloadGrid");
					$("#addTemplateLineItem").dialog("close");
					$("#inLineNoteImage").trigger("reloadGrid");
				}
			});
		}

		function cancelTemplateLineItem(){
			$("#addTemplateLineItem").dialog("close");
		}
		
		$(function() {var cache = {}; var lastXhr=''; 
			$("#productTemplate").autocomplete({ minLength: 2,
				select: function( event, ui ) { 
					var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); 
					$.ajax({
						url: "./jobtabs2/quoteDetails", type: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
						success: function(data) {
							$.each(data, function(index,value){
								manufacture = value.inlineNote;
								var detail = value.paragraph;
								var quantity = value.itemQuantity;
								var manufactureID = value.rxManufacturerID;
								var factoryID = value.detailSequenceId;
								var priceID = value.detailSequenceId;
								var costID = value.detailSequenceId;
								$("#paragraphTemplate").val(detail); $("#manufacturerTemplate").val(quantity); //$("#itemQuantity").val(quantity); 
								$("#rxManufacturerIDTemplate").val(manufactureID);	$("#veFactoryIdTemplate").val(factoryID);	
								//$("#priceTemplate").val(priceID);$("#costTemplate").val(costID);
								
							});
						} });
				},
				source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; }
					lastXhr = $.getJSON( "jobtabs2/productList", request, function( data, status, xhr ) { cache[ term ] = data; if (xhr === lastXhr){ response(data); } }); 
				},
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
			});
		});

		$(function() { var cache = {}; var lastXhr='';
			$( "#manufacturerTemplate" ).autocomplete({ minLength: 1, 
				select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value; $("#rxManufacturerIDTemplate").val(id);
				 	$.ajax({
						url: "./jobtabs2/getFactoryID",
						type: "GET",
						data : { 'rxMasterID' : id,'descripition' : name },
						success: function(data) {$("#veFactoryId").val(data);}
				 	});
				 },
				source: function( request, response ) {
					var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
					lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); }	}); 
				},
				error: function (result) {$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");}
			});
		});
		function createQuoteFromTemplate(){
			var newDialogDiv = jQuery(document.createElement('div'));
			var grid = $('#quoteTemplates');
			var selectedRowId = grid.jqGrid ('getGridParam', 'selrow');
			var joQuoteTemplateHeaderId = grid.jqGrid ('getCell', selectedRowId, 'joQuoteTemplateHeaderId');
			if(selectedRowId===null){
				var errorText = "Please click one of the template to save.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			} else {
				var jobNumber = $('.jobHeader_JobNumber').val();
				var errorText = "This will save the template into Quotes. Do you really want to save the Template.?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
				buttons: {
					"Yes": function(){
						
						
						jQuery(this).dialog("close");
						$.ajax({
							url:'./jobtabs2/insertQuotefromTemplate',
							type: "GET",
							data : { 'joQuoteTemplateHeaderId' :joQuoteTemplateHeaderId,'jobNumber' : jobNumber},
							success: function(data) {
								var errorText = "Quote Templates are saved, Please click on edit for making changes.";
								jQuery(newDialogDiv).attr("id","msgDlg");
								jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");$("#quotes").trigger("reloadGrid");}}]}).dialog("open");
							}
						});
					},
					"No": function (){jQuery(this).dialog("close");	}
				}}).dialog("open");
			}
		}
		
		
		function viewLinePDF() {
			var gridData = jQuery("#quoteTemplateProductsGrid").getRowData();
			var grid = $("#quoteTemplateProductsGrid");
			var gridRows = grid.jqGrid('getRowData');
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			if (gridData.length <= 0) {
				var errorText = "Please add one Line Item to Show Quote to View.";
				
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv).html(
						'<span><b style="color:red;">' + errorText + '</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				return false;
			}
			var aParagraph = 1;
			var aVendor = 1;
			if ($('#viewParagraphID').is(':checked')) {
				aParagraph = 0;
			}
			if ($('#viewManufactureID').is(':checked')) {
				aVendor = 0;
			}
			var aJoBidderContact = '';
			var aQuoteTo = '';  
			var joQuoteHeaderID =  $('#joQuoteLineTemplateHeaderID').val();//grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');//$('#joQuoteLineTemplateHeaderID').val();
			var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
			var joQuoteRev = '';//grid.jqGrid('getCell', rowId, 'rev');
			var name = $("#engineerHiddenID").val();
			var enggName = name.replace('&', 'and');
			var aEngineer = enggName.replace('&', 'and');
			var architectName = $("#architectHiddenID").val();
			var archName = architectName.replace('&', 'and');
			var aArchitect = archName.replace('&', 'and');
			var aBidDate = $("#bidDateHiddenID").val();
			var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
			var aPlanDate = $("#plan_date_format").val();
			var aLocationCity = $("#locationCityHiddenID").val();
			var aLocationState = $("#locationStateHiddenID").val();
			var aThruAddendum = $("#quoteThru_id").val();
			var joQuotePrice = '';//grid.jqGrid('getCell', rowId, 'quoteAmount');
			var joDiscountPrice = '';//grid.jqGrid('getCell', rowId, 'discountAmount');
			var joRemarks = "";
			var aRemarks = "(" + joRemarks + ")";
			var aJobNumber = $("#jobNumberHiddenID").val();
			var jobNumber = $('.jobHeader_JobNumber').val();
			var currentDate = new Date();
			var currentMonth = currentDate.getMonth() + 1;
			var date = currentDate.getDate();
			if (currentMonth < 10)
				currentMonth = "0" + currentMonth.toString();
			if (date < 10) {
				date = "0" + date.toString();
			}
			var currenDate = currentMonth + "/" + date + "/"
					+ currentDate.getFullYear(); 
			var aviewLinePDF = 'viewLinePDF';
			window.open("./quotePDFController/viewQuotePdfForm?enginnerName="
					+ aEngineer + "&architectName=" + aArchitect + "&bidDate="
					+ aBidDate + " " + "&projectName=" + aProjectName + "&planDate="
					+ aPlanDate + "&locationCity=" + aLocationCity + "&locationState="
					+ aLocationState + "&quoteTOName=" + aQuoteTo + ""
					+ "&bidderContact=" + aJoBidderContact + "&joQuoteHeaderID="
					+ joQuoteHeaderID + "&totalPrice=" + joQuotePrice + "&quoteRev="
					+ joQuoteRev + "" + "&QuoteThru=" + aThruAddendum + "&todayDate="
					+ currenDate + "&jobNumber=" + aJobNumber + "&discountAmount="
					+ joDiscountPrice + "&quoteRemarks=" + aRemarks
					+ "&paragraphCheck=" + aParagraph + "&manufactureCheck=" + aVendor
					+ "&submittedBy=" + createdBYFullName
					+ "&lineJobNumber=" + jobNumber
					+ "&viewLinePDF=" + aviewLinePDF,"_blank");
			
			
			return false;
		}