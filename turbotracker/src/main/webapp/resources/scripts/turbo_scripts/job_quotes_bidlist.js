var aftersaveHeaderID='';
var posit_job_quotesBidlist=0;
var idToSelect;
function loadBidListGrid(){
			var grid =$("#quotesBidlist"); 
			grid.jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/jobquotesbidlist',
				//pager: jQuery('#quotesBidlistPager'),
				colNames:['BidderId', 'Bidder','Low','Contact', 'Type', 'TypeId', 'Last Quote', 'Rev.', 'JobNumber','RxMasterId', 
				          'joMasterId', 'rxContactId', 'Rep.', 'Email','Emailstatus'],
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
					{name:'email',index:'email',align:'center',width:40,hidden: true,editable:true,edittype:'text',editrules:{edithidden:true,required:false}},
					{name:'quoteemailstatus',index:'quoteemailstatus',align:'center',width:40,hidden: true,editable:true,edittype:'text',editrules:{edithidden:true,required:false}}
				],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'employeeId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height: 200, width: 1080, rownumbers:true, altRows: true, altclass:'myAltRowClass',
				postData: {jobNumber: function() { return $("#jobNumber_ID").text();},joMasterID: $("#joMaster_ID").text()},
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
					
					if(row.quoteemailstatus!=null && row.quoteemailstatus!="" && row.quoteemailstatus==2){
					document.getElementById("faxIcon").setAttribute("title", "Printed");
					}else{
						document.getElementById("faxIcon").setAttribute("title", "Fax Quote");	
					}
				},
				ondblClickRow: function(rowId) {
					var rowData = jQuery(this).getRowData(rowId); 
					var rxMaster = rowData['rxMasterId'];
					var name = rowData['bidder'];
					var quotetypeid = rowData['quoteTypeID'];
					var name1 = name.replace('&', 'and');
		    		var name2 = name1.replace('&', 'and');
		    		var checkpermission=getGrantpermissionprivilage('Customers',0);
		    		if(checkpermission){
					document.location.href = "./customerdetails?rolodexNumber="+rxMaster+"&name="+'('+name2+')&quoteTypeId='+quotetypeid;
		    		}
		    		},
		    	loadBeforeSend: function(xhr) {
					//	posit_job_quotesBidlist= jQuery("#quotesBidlist").closest(".ui-jqgrid-bdiv").scrollTop();
					},
				loadComplete: function(data) {
					//jQuery("#quotesBidlist").setSelection(1,true);
				},gridComplete: function () {
		            // jQuery("#quotesBidlist").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_quotesBidlist);
		           //  posit_job_quotesBidlist=0;
					
					if(aBidderDialogVar  === "add"){
						$('#quotesBidlist tbody tr:last-child').click();
						var rowId1 = grid.jqGrid('getGridParam', 'selrow');
						scrollToRow ("#quotesBidlist", rowId1,"-1");
					}
					else if(aBidderDialogVar  === "edit" || aBidderDialogVar  === "delete" || aBidderDialogVar  === "mail"){
						var rowId = grid.jqGrid('getGridParam', 'selrow');
						scrollToRow ("#quotesBidlist",rowId,bidScrollPosition);
						}
					aBidderDialogVar = "";
					bidScrollPosition= "";
				   },
				loadError : function (jqXHR, textStatus, errorThrown){ },
				
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

//Prevent press Enter Key
$('input[type=text]').on('keyup', function(e) {
	if (e.which == 13) {
		return false;
	}
});

$(document).ready(function() {

	$(document).keypress(function(event) {

		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			if (event.target.className.indexOf('nicEdit-main') == '-1') {
				event.preventDefault();
				return false;
			}
		}
	});

	$("img").keyup(function(event) {
		if (event.keyCode == 13) {
			event.preventDefault();
			return false;
		}
	});
	loadnewquotesList_template();
	LineItemtypeonchange_template("-1");
	quoteCategorySelectBox_template();
	
	
	
	jQuery("#QuoteckEditordivbx_temp").dialog({
		autoOpen:false,
		width:900,
		title:"Editor",
		modal:true,
		buttons:{
			"Save":function(){
				var selrowid=$("#Quoteselrowid_temp").val();
				var htmlcontent=CKEDITOR.instances["Quoteeditor_temp"].getData();
				var contenttext=htmlcontent.replace(/<[^>]*>/ig, ' ')
				   .replace(/<\/[^>]*>/ig, ' ')
				   .replace(/&nbsp;|&#160;/gi, ' ')
				   .replace(/\s+/ig, ' ')
				   .trim();
				$("#addnewquotesTemplateList").jqGrid('setCell', selrowid, 'description', contenttext);
				$("#addnewquotesTemplateList").jqGrid('setCell', selrowid, 'texteditor', htmlcontent);
				jQuery('#QuoteckEditordivbx_temp').dialog("close");
				validatequotedisabledbuttons_temp();
				var typevalue=$("#"+selrowid+"_type").val();
				if(typevalue==1){
					$("#addnewquotesTemplateList_ilsave").trigger("click");
				}else{
					$("#"+selrowid+"_custombutton_temp").focus();
				}
				//CKEDITOR.instances["Quoteeditor_temp"].destroy(true);
				},
			"Cancel":function(){
					$('#QuoteckEditordivbx_temp').dialog("close");
					var selrowid=$("#Quoteselrowid_temp").val();
					$("#"+selrowid+"_custombutton_temp").focus();
					CKEDITOR.instances["Quoteeditor_temp"].destroy(true);
					}
				
				},
		close:function(){
			$('#QuoteckEditordivbx_temp').validationEngine('hideAll');
			var selrowid=$("#Quoteselrowid_temp").val();
			$("#"+selrowid+"_custombutton_temp").focus();
			CKEDITOR.instances["Quoteeditor_temp"].destroy(true);
		return true;}	
	});
});


function openQuoteHistoryDialog() {	
	var dialogWidth = 975;
	
	jQuery("#addquoteHistory").dialog({
		 closeOnEscape: true,  
		create: function (event, ui) {
			$("#addquoteHistory").parent().find(".ui-dialog-titlebar").addClass("mySpecialClass");
            $(".mySpecialClass").html('<span style="float:right; cursor:pointer;" id="myCloseIcon">Close</span>');
        },
		autoOpen:false, 
		height:350, 
		width:dialogWidth,
		top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], 
		modal:true, 
		title:'Quote History',  
		iframe: true,
		open: function(event, ui) {  
            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
        },
		close:function() {
				jQuery(this).dialog("close");
		}
	});
	 $("span#myCloseIcon").click(function() {
		
	        $( "#addquoteHistory" ).dialog( "close" );
	    });// Add quote History
	jQuery("#addquoteHistory").dialog("open");
	
}


function quoteHistory(){
	 createtpusage('job-Quote Tab','Quote History','Info','Job-Quote Tab,Quote History,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	openQuoteHistoryDialog();
	$("#quoteHistoryGrid").trigger("reloadGrid");
	$("#quoteHistoryGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		url:'./jobtabs2/jobQuoteHistory',
		//pager: jQuery('#quotesPager'),vendor
		colNames:['joQuoteHeaderID','JoMasterID','Date','Time','Action','By','Type','Rev.','Bidder','Contact', 'Amount($)'],
		colModel :[
			{name:'joQuoteHeaderID', index:'joQuoteHeaderID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
			{name:'joMasterID', index:'joMasterID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
			{name:'quoteHistoryDate', index:'quoteHistoryDate', align:'center', width:80, editoptions:{},editrules:{},formatter:'date',formatoptions:{ srcformat: 'Y-m-d h:i:s A',newformat:'m/d/Y '}},
			{name:'quoteHistoryDate', index:'quoteHistoryDate', align:'center', width:70,editrules:{},formatter:Hoursandmin},// formatoptions:{ srcformat: 'Y-m-d h:i:s A',newformat:'h:i A'}
			{name:'quoteStatus', index:'quoteStatus', align:'center', width:70, editoptions:{},editrules:{}},
			{name:'createdByName', index:'createdByName', align:'left', width:155,editrules:{}},
			{name:'quoteType', index:'quoteType', align:'', width:60,editoptions:{},editrules:{}},
			{name:'quoteRev', index:'quoteRev', align:'center', width:35,editoptions:{},editrules:{}},
			{name:'company', index:'company', align:'', width:155, editable:true, edittype:'text',editoptions:{size:30}, editrules:{required:true}},
			{name:'contact', index:'contact',align:'',width:155,editable: true,edittype:'text',editoptions:{size:1},editrules:{required: false}},
			{name:'quoteAmount', index:'quoteAmount', align:'center', width:80,formatter:customCurrencyFormatter,editoptions:{},editrules:{}}
			],
		rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
		sortname: 'createdByName', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
		height:'100%', width: '100%', rownumbers:true, altRows:true, altclass:'myAltRowClass',
		postData: {jobNumber: function() { return $("#jobNumber_ID").text(); }},
		loadComplete: function(data) {
		/*var aJobStatus = $("#jobStatusList").val();
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
		}*/
			/*var grid = $("#quotes");
			grid.setSelection(1, true);
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var data = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
			jQuery("#joQuoteheader").text(data);*/
		},
		ondblClickRow: function(rowid){
		},
		loadError : function (jqXHR, textStatus, errorThrown){	},
		onSelectRow: function(id){
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
		function mailImage(cellValue, options, rowObject){
			var status=rowObject["quoteemailstatus"];
			var element = '';
		   if(cellValue !== '' && cellValue !== null && status==1){
			   element = "<img src='./../resources/images/message-already-read.png' title='Mail Sent'  style='vertical-align:middle;'>" +cellValue ;
		   }else if(cellValue !== '' && cellValue !== null && status==2){
			   element = "<img src='./../resources/images/fax_new.png' title='Printed'  style='vertical-align:middle;'>" +cellValue ;
		   }else if(cellValue !== '' && cellValue !== null && status==0){
			   element = "<img src='./../resources/images/delete.png' title='Failed'  style='vertical-align:middle;'>" +cellValue ;
		   }
		   else if(cellValue === ''){
			   element = "";
		   }else if(cellValue === null){
			   element = "";
		   }else{
			   element = "";
		   }
		return element;
		} 
		
		function Hoursandmin(cellValue, options, rowObject)
		{
			var today=cellValue.split(" ");
			var hrcalc= today[1].split(":");
			return hrcalc[0]+":"+hrcalc[1]+" "+today[2];
		}
		
		function numberandtypeFormatter( cellvalue, options, rowObject )
		{
		     return $("#jobNumber_ID").text()+ '(' + rowObject.quoteType+ ')';
		}
		var posit_job_Quotes=0;
		function loadQuotesGrid(){
			//alert('Hi');
			$("#quotes").jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/jobquoteslist',
				//pager: jQuery('#quotesPager'),
				colNames:['joQuoteHeaderID','quoteTypeID','Type','Type','Rev.','Cost($)', 'Amount($)', 'Discount($)','CreatedByID','Created By','CreatedBYFullName', 'Internal Notes','Remarks','PQ',
						          'displayQuantity', 'printQuantity', 'displayParagraph', 'printParagraph', 'displayManufacturer', 'printManufacturer', 'displayCost', 'printCost', 'displayPrice', 
						          'printPrice', 'displayMult', 'printMult', 'displaySpec', 'printSpec', 'notesFullWidth', 'lineNumbers', 'printTotal', 'hidePrice','','','','',''],
				colModel :[
					{name:'joQuoteHeaderID', index:'joQuoteHeaderID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
					{name:'quoteTypeID', index:'quoteTypeID', align:'', hidden:true, width:60,editoptions:{},editrules:{}},
					{name:'numberandtype', index:'numberandtype', align:'',hidden:true, width:60,formatter:numberandtypeFormatter,editoptions:{},editrules:{}},
					{name:'quoteType', index:'quoteType', align:'', hidden:false,width:60,editoptions:{},editrules:{}},
					{name:'rev', index:'rev', align:'center', width:30,editoptions:{},editrules:{}},
					{name:'costAmount', index:'costAmount', align:'center', width:60, formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'quoteAmount', index:'quoteAmount', align:'center', width:60,formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'discountAmount', index:'discountAmount', align:'center', width:60, formatter:customCurrencyFormatter,editoptions:{},editrules:{}},
					{name:'createdByID', index:'createdByID', align:'center', hidden:true, width:40, editoptions:{},editrules:{}},
					{name:'createdByName', index:'createdByName', align:'center', width:90, editoptions:{},editrules:{}},
					{name:'createdBYFullName', index:'createdBYFullName', align:'center', hidden:true, width:90, editoptions:{},editrules:{}},
					{name:'internalNote', index:'internalNote', align:'center', width:90, editoptions:{},editrules:{}},
					{name:'quoteRemarks', index:'quoteRemarks', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'primaryQuote', index:'primaryQuote', align:'center', width:40, hidden:true,formatter:'checkbox', edittype:'checkbox',editoptions:{}, editrules:{}},
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
					{name:'hidePrice', index:'hidePrice', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					
					{name:'includeLSD', index:'includeLSD', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'donotQtyitem2and3', index:'donotQtyitem2and3', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'showTotPriceonly', index:'showTotPriceonly', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'includeTotprice', index:'includeTotprice', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					{name:'lsdValue', index:'lsdValue', align:'center', width:90, hidden:true, editoptions:{},editrules:{}},
					
					],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'employeeId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:86, width: 1080, rownumbers:true, altRows:true, altclass:'myAltRowClass',
				postData: {jobNumber: function() { return $("#jobNumber_ID").text(); },joMasterID: $("#joMaster_ID").text()},
				loadBeforeSend: function(xhr) {
					posit_job_Quotes= jQuery("#quotes").closest(".ui-jqgrid-bdiv").scrollTop();
				},
				loadComplete: function(data) {
				var aJobStatus = $("#jobStatusList").val();
				if(aJobStatus === '0'){
					if(jQuery("#quotes").getGridParam("records") !== 0){
						$("#jobStatusList option[value='1']").attr("selected","selected");
						aJobStatus = 1;
						var aJobNumber = $(".jobHeader_JobNumber").val();
						var joMasterID=$("#joMaster_ID").text();
						$.ajax({
							url: "./job_controller/updateJobStatus", mType: "GET", data: { 'jobStatus' : aJobStatus, 'jobNumber' : aJobNumber,'joMasterID':joMasterID },
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
					editQuoteDetails('edit');
					/*if(!AllowEditingofSentQuotes){
						var grid = $("#quotes");
						var rowId = grid.jqGrid('getGridParam', 'selrow');
						var rowdata=grid.jqGrid('getRowData',rowId);
						var type=rowdata['quoteType'];
						var rev=rowdata['rev'];
						var quoteemailstatus=rowdata["quoteemailstatus"];
						var gridData = jQuery("#quotesBidlist").getRowData();
						for (var index = 0; index < gridData.length; index++) {
							var rowData = gridData[index];
							if(type==rowData['quoteType'] && rev==rowData['rev']){
								if(rowData['lastQuote']!="" && quoteemailstatus==1){
									jQuery(newDialogDiv).html('<span style="color:#FF0066;">The Quote has been sent already and you cannot edit.Would you like to create a Revision?</span><br><hr style="color: #cb842e;">');
									 jQuery(newDialogDiv).dialog({modal: true, title:"Alert",width:300, 
											buttons: [{height:30,text: "Yes",
												keypress:function(e){
													var x = e.keyCode;
													if(x==13){$(newDialogDiv).dialog("close");
													editQuoteDetails('copy');
													return false;
													}
											    },
												click: function() {
												$(this).dialog("close");
												editQuoteDetails('copy');
												return false;
												}},
												{height:30,text: "No",
													keypress:function(e){
														var x = e.keyCode;
														if(x==13){$(newDialogDiv).dialog("close");}
												    },
													click: function() {
													$(this).dialog("close"); 
													return false;
													}},
												
												]}).dialog("open");
									return false;
									break;
								}else{
									break;
								}
							}
						}
					}
					
					
					onDoubleClickEditQuoteDetails(rowid);*/
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){
					var grid = $("#quotes");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var data = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
					jQuery("#joQuoteheader").empty();
					jQuery("#joQuoteheader").text(data);
					//$("#addnewquotesList").trigger("reloadGrid");
				},gridComplete: function(data) {  
					try {
											jQuery("#quotes").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_Quotes);
								             posit_job_Quotes=0;
											var ids = jQuery("#quotes").jqGrid('getDataIDs');

											console.log("copyquoteglobalvariable=="+copyquoteglobalvariable);
											if(copyquoteglobalvariable=='copy'){
												for (var i = 0; i < ids.length; i++) {
												var rowId = ids[i];
												var rowData = jQuery('#quotes').jqGrid('getRowData',rowId);
												if (copyquoteselectedgridid == rowData.joQuoteHeaderID) {
													$('#loadingDivForcopyQuote').css({"visibility": "visible","z-Index":"1234","display":"none"});
													onDoubleClickEditQuoteDetails(rowId);
												}
												}
												copyquoteselectedgridid="";
												copyquoteglobalvariable="";
												aGlobalVariable='copy';
											}
											
											
											if(pdfPreview_newQuote){
											var joQuoteHeaderIDD=Number($("#joQuoteheader").text());	
											var rowIds = $("#quotes").jqGrid('getDataIDs');
								            for (i = 0; i <= rowIds.length; i++) {
								               var rowData = $("#quotes").jqGrid('getRowData', rowIds[i]);
								                if (rowData['joQuoteHeaderID'] == joQuoteHeaderIDD ) {
								                   $("#quotes").jqGrid('setSelection',rowIds[i]); 
								                } 

								            }
								            pdfPreview_newQuote=false;
											}
										
											console.log("copyquoteglobalvariablereset=="+copyquoteglobalvariable);
										} catch (err) {
											alert('error' + err);
										} 

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
		var posit_job_quoteTemplates=0;
		function loadQuoteTemplates(){
			$("#quoteTemplates").jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs2/loadQuoteTemplates',
				//pager: jQuery('#quotesBidlistPager'),
				colNames:['joQuoteTemplateHeaderId', 'Template Name','ownerId', 'cuMasterTypeId','Remarks','',''],
				colModel :[ 
		            {name:'joQuoteTemplateHeaderId', index:'joQuoteTemplateHeaderId', align:'', width:40, editable:true,hidden:true},
					{name:'templateName', index:'templateName', align:'', width:110, editable:true, edittype:'text'},
					{name:'ownerId', index:'ownerId', align:'center', width:30,hidden:true,editable:true},
					{name:'cuMasterTypeId', index:'cuMasterTypeId', align:'center', width:30,hidden:true,editable:true},
					{name:'remarks', index:'remarks', align:'center', width:40,hidden:true,editable:true},
					{name:'quoteAmount', index:'quoteAmount', align:'center', width:40,hidden:true,editable:true},
					{name:'printTotal', index:'printTotal', align:'center', width:40,hidden:true,editable:true},
				],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'templateName', sortorder: "asc", imgpath: 'themes/basic/images', caption: 'Quote Templates',
				height: 140, width: 520, rownumbers:true, altRows: true, altclass:'myAltRowClass',
				postData: '',
				hiddengrid: false,
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
					var rowDatas = jQuery(this).getRowData(id);
					var joQuoteTemplateHeaderIds = rowDatas['joQuoteTemplateHeaderId'];
					$('#quoteTemplateHeaderID').val(joQuoteTemplateHeaderIds);
				},
				ondblClickRow: function(rowId) {
					var rowData = jQuery(this).getRowData(rowId); 
					var rxMasterId = rowData['rxMasterId'];
				},
				loadBeforeSend: function(xhr) {
					posit_job_quoteTemplates= jQuery("#quoteTemplates").closest(".ui-jqgrid-bdiv").scrollTop();
				},
				loadComplete: function(data) {  },
				gridComplete: function () {
		             jQuery("#quoteTemplates").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_quoteTemplates);
		             posit_job_quoteTemplates=0;
		             
		             if(pdfPreview_newQuoteTemplate){
		            	 var joQuoteTemplateHeaderIDD= Number($("#quoteTemplateHeaderID").val());
		            	 var rowIds = $("#quoteTemplates").jqGrid('getDataIDs');
				            for (i = 1; i <= rowIds.length; i++) {
				               var rowData = $("#quoteTemplates").jqGrid('getRowData', rowIds[i]);
				                if (rowData['joQuoteTemplateHeaderId'] == joQuoteTemplateHeaderIDD ) {
				                   $("#quoteTemplates").jqGrid('setSelection',rowIds[i]); 
				                } 

				            }
				          pdfPreview_newQuoteTemplate=false;
		             }
				  },
				loadError : function (jqXHR, textStatus, errorThrown){ }
				//editurl:"./jobtabs2/manpulateQuotebidList"
			});
		}
		
		jQuery(function(){
			var dialogWidth = 1150;
			jQuery("#addquoteTemplate").dialog({
				autoOpen:false, height:750, width:dialogWidth,top:1000,position: [($(window).width() / 2) - (dialogWidth / 2), 190], modal:true, title:'Add/Edit Template',  iframe: true,
				open: function(event, ui) {  
		            //jQuery('.ui-dialog-titlebar-close').removeClass("ui-dialog-titlebar-close").hide();  
		        },
				buttons:{
					
				},
				close:function() {
					//	jQuery(this).dialog("close");
				}
			});
		});
		
		
		

		
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
				buttons: [{height:35,text: "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click: function() {
						$(this).dialog("close");
						}
					    }]}).dialog("open");
				return false;
			}else{
				var errorText = "Do you want to delete the "+TemlateName+" template?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				 buttons: {
					"Yes": function(){
						jQuery(this).dialog("close");
						$.ajax({
							url:'./jobtabs2/DeleteEditTemplate',
							data:{'quoteTemplateHeaderId':joQuoteTemplateHeaderId},
							type:"POST",
							success: function(data) {
								createtpusage('job-Quote Tab','Delete Template','Info','Job-Quote Tab,Deleting Template,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val()+',joQuoteTemplateHeaderId:'+joQuoteTemplateHeaderId);
								var errorText = TemlateName+" template is deleted successfully.";
								var validateMsg = "<b style='color:Green; align:right;'>"+errorText+"</b>";	
								$("#createQuoteMsg").css("display", "block");
								$('#createQuoteMsg').html(validateMsg);
								setTimeout(function(){
								$('#createQuoteMsg').html("");						
								},2000);
								$('#quoteTemplates').trigger("reloadGrid");
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
				pager: '#quoteTemplateProductsGridPager',
				colNames:dynamicColNames,
				colModel:dynamicColModels,
				cellEdit : false,
				cellsubmit : 'remote',
				//editurl : "./jobtabs2/updateTemplateLineItem",
				/*cmTemplate : {
					editable : true
				},*/
				rowNum: 1000,
				pgbuttons: false,
				recordtext: '',
				rowList: [],
				pgtext: null,
				viewrecords: true,
				sortname: 'Product', 
				sortorder: "asc", 
				imgpath: 'themes/basic/images',
				caption: 'Line Items',
				height:190,width: 1100,
				loadComplete: function(data) {
					var gridData = jQuery("#quoteTemplateProductsGrid").getRowData();
					var totalPrice = 0;
					for(var index = 0; index < gridData.length; index++){
					var rowData = gridData[index];
					var price = rowData["price"].replace(/[^-0-9\.]+/g,"");
					totalPrice = totalPrice + Number(price);
					}
					$('#quoteTemplateTotalPrice').val(formatCurrency(totalPrice));
				},
				ondblClickRow: function(rowid){
					
				},
				gridComplete: function () {
					
				},
				afterInsertRow: function(rowid, aData) {

					console.log("Template Item Position Update");
					var aPositionID = $("#quoteTemplateProductsGrid").jqGrid('getCell',
							rowid, 'position');
					console.log(aPositionID);
					if (aPositionID === '0') {
						var aQuoteDetailID = $("#quoteTemplateProductsGrid").jqGrid(
								'getCell', rowid, 'joQuoteTemplateDetailId');
						var aQuoteHeaderID = $("#quoteTemplateProductsGrid").jqGrid(
								'getCell', rowid, 'joQuoteTemplateHeaderId');
						$.ajax({
							url : "./jobtabs2/updateQuoteTempItemPosition",
							type : "GET",
							data : {
								'joQuoteTempDetailID' : aQuoteDetailID,
								'joQuoteTempHeaderID' : aQuoteHeaderID
							},
							success : function(data) {
								$("#addquotesList").trigger("reloadGrid");
							}
						});
					}
					
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
			}).navGrid("#quoteTemplateProductsGridPager", {
				add : false,
				edit : false,
				del : false,
				alertzIndex : 10000,
				search : false,
				refresh : false,
				pager : false,
				alertcap : "Warning",
				alerttext : 'Please select a Product'
			},
			// -----------------------edit// options----------------------//
			{},
			// -----------------------add options----------------------//
			{},
			// -----------------------Delete options----------------------//
			{});
			$("#quoteTemplateProductsGrid").jqGrid(
					'inlineNav',
					'#quoteTemplateProductsGridPager',
					{
						edit : true,
						edittext : "Edit",
						add : true,
						zIndex : 10000,
						addtext : "Add",
						savetext : "Save",
						canceltext : "Cancel",
						refresh : false,
						cloneToTop : true,
						alertzIndex : 10000,
						addParams : {
							addRowParams : {
								keys : false,
								oneditfunc : function() {
									console.log("edited");
								},
								successfunc : function(response) {
									console.log("success"+response);
									return true;
								},
								aftersavefunc : function(response) {
									console.log('addParams-aftersavefunc'+response.responseText);
									$('#quoteTemplateHeaderID').val(response.responseText);
								},
								errorfunc : function(rowid, response) {
									console.log('errorfunc');
									$("#info_dialog").css("z-index", "10000");
									$(".jqmID1").css("z-index", "10000");
									$("#new_row_manufacturer").focus();
									return false;
								},
								afterrestorefunc : function(rowid) {
									console.log("afterrestorefunc");
								}
							}
						},
						editParams : {
							keys : false,
							dataInit : function (elem) {
						        console.log(elem);
						    },
						    successfunc : function(response) {
						    	console.log('editParams-aftersavefunc'+response.responseText);
						    	$('#quoteTemplateHeaderID').val(response.responseText);
						    	aftersaveHeaderID = response.responseText;
								return true;
							},
							aftersavefunc : function(id) {
								console.log('aftersavefunc '+id);
								console.log('aftersavefunc-aftersaveHeaderID: '+aftersaveHeaderID);
								$("#quoteTemplateProductsGrid").jqGrid('GridUnload'); 	
								loadTemplateLineItemDetails(aftersaveHeaderID,dynamicColNames,dynamicColModels);
								$("#quoteTemplateProductsGrid").trigger("reloadGrid");

							},
							errorfunc : function(rowid, response) {
								console.log('niaz::loadQuotesListDetails::10');
								$("#info_dialog").css("z-index", "10000");
								$(".jqmID1").css("z-index", "10000");
								return false;

							},

							// oneditfunc: setFareDefaults
							oneditfunc : function(id) {
								console.log('editParams-oneditfunc:'+id);
								//$('#' + id + '_price').prop('readonly',true);
								var spec = $('#' + id + '_spec').val();
								var mult = $('#' + id + '_mult').val();
								var price = $('#' + id + '_price').val();
								
								if(spec=='' ||spec== undefined){
									 $('#' + id + '_spec').val('0');
								}
								if(mult=='' ||mult== undefined){
									 $('#' + id + '_mult').val('0');
								}
								
								$('#' + id + '_price').live('focus', function() {
									var itemQuantity = $('#' + id + '_itemQuantity').val();
									var cost = $('#' + id + '_cost').val();
									cost = cost.replace(/[^-0-9\.]+/g, "").replace(".00", "");
									price = itemQuantity*cost;
									$('#' + id + '_price').val(price);
								});
								
								var sellPri = "";
								$('#' + id + '_percentage').live(
										'blur',
										function() {
											var pecent = $('#' + id + '_percentage')
													.val();
											var productCost = $('#' + id + '_cost')
													.val();
											productCost = productCost.replace(
													/[^0-9\.]+/g, "")
													.replace(".00", "");
											if (isNaN(productCost)) {
												productCost = 0;
											}
											if (isNaN(pecent)) {
												pecent = 0;
											}
											if (pecent !== '') {

												sellPri = productCost
														/ [ (100 - pecent) / 100 ];
												sellPri = Math.round(sellPri);
												sellPri = parseInt(sellPri);
												// pri=pri.replace(/[^-0-9\.]+/g,
												// "").replace(".11",
												// ".00");
											}
											$('#' + id + '_price').val(sellPri);
											var myLength = $('#' + id + '_percentage')
													.val().length;
											if (myLength > 2) {
												$('#' + id + '_percentage').focus();
												$('#' + id + '_percentage').css(
														'background', 'RED');
											} else {
												$('#' + id + '_percentage').css(
														'background', 'white');
											}
										});
							}
						}

					/*
					 * addParams: { addRowParams: { mtype: "POST", url: "", keys: true,
					 * oneditfunc: function () { alert("edited"); }, successfunc:
					 * function (response) { alert("success"); return true; },
					 * aftersavefunc: function (response) { alert("aftersave"); },
					 * errorfunc: function (rowid, response) { alert("errorfunc"); },
					 * afterrestorefunc :function (rowid) { alert("afterrestorefunc"); } } },
					 * editParams: { mtype: "POST", keys: true, url:
					 * "./jobtabs2/manpulaterProductQuotes?joQuoteDetailID=0&product=" +
					 * $("#new_row_product").val() + "&paragraph=" +
					 * $("#new_row_paragraph").val() + "&rxManufacturerID=" +
					 * $("#new_row_rxManufacturerID").val() + "&manufacturer="+
					 * $("#new_row_manufacturer").val() + "" + "&itemQuantity=" +
					 * $("#new_row_itemQuantity").val() + "&cost=" +
					 * $("#new_row_cost").val() + "&oper=add&joQuoteHeaderID=" +
					 * $("#new_row_joQuoteHeaderID").val()+
					 * "&quoteheaderid=0&veFactoryId="+ $("#new_row_veFactoryId").val() +
					 * "&inlineNote=&footnote=&productNote=&price=" +
					 * $("#new_row_price").val() }
					 */
					});
			
			
			$("#quoteTemplateProductsGrid_iledit").click(function(){
				
				var spec = $('#' + id + '_spec').val();
				var cost = $('#' + id + '_cost').val();
				var mult = $('#' + id + '_mult').val();
				var price = $('#' + id + '_price').val();
				
				if(spec=='' ||spec== undefined){
					 $('#' + id + '_spec').val('0');
				}
				if(mult=='' ||mult== undefined){
					 $('#' + id + 'mult').val('0');
				}
				
			});
			
			$("#quoteTemplateProductsGrid_iladd").click(function(){
				operForTemplateLineItem = 'add';
				var isDetailsGridExist = $("#quoteTemplateProductsGrid").getGridParam(
						"reccount");
				if (isDetailsGridExist < 0)
					templateAddEdit = "add";
				var newDialogDiv = jQuery(document.createElement('div'));
				/*if ($("#quoteTemplateTypeDetail").val() === "-1") {
					jQuery(newDialogDiv).html(
							'<span><b style="color:red;">Please Provide "Type"</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Warning",
						buttons : [ {
							height : 35,
							text : "OK",
							 keypress:function(e){
									var x = e.keyCode;
									if(x==13){$(newDialogDiv).dialog("close");}
							    },
							click : function() {
								$("#quoteTemplateProductsGrid_ilcancel")
								.trigger("click");
						$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					return false;
				} else*/ if ($("#templateDescription").val() === '') {
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:red;">Please provide Template Description</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Warning",
						buttons : [ {
							height : 35,
							text : "OK",
							 keypress:function(e){
									var x = e.keyCode;
									if(x==13){$(newDialogDiv).dialog("close");}
							    },
							click : function() {
								$("#quoteTemplateProductsGrid_ilcancel")
								.trigger("click");
						$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					return false;
				}
				else{
				$("#productTemplate").val("");
				$("#paragraphTemplate").val("");
				$("#manufacturerTemplate").val("");
				$("#itemQuantityTemplate").val("");
				$("#costTemplate").val("");
				$("#priceTemplate").prop('disabled', true);
				$("#joQuoteTemplateDetailID").val("");
				$("#rxManufacturerIDTemplate").val("");
				$("#veFactoryIdTemplate").val("");
				$("#percentageTemplate").val("");
				$("#inlineNoteTemplate").val("");
				$("#productNoteTemplate").val("");
				/*$("#addTemplateLineItem").dialog({
					height : 450
				});*/
				$("#new_row_itemQuantity").val('0');
				$("#new_row_spec").val('0');
				$("#new_row_mult").val('0');
				//$("#new_row_price").prop('readonly',true);
				
				saveANDcloseQuoteTemplateInline();
				//jQuery("#addTemplateLineItem").dialog("open");
				return true;
				}
				
				//For Template Ends
				
				/*
				$('#quoteTypeDetail').prop('disabled', true);
				$('#jobQuoteRevision').prop('disabled', true);
				var joHeaderId = $('#joHeaderID').val();

				$("#addquotesList").jqGrid('setCell', 1,
						'joQuoteHeaderID', joHeaderId);
				$("#new_row_joQuoteHeaderID").val(joHeaderId);

				isAddInline = true;
				gVar = 'add';
				var aAddandEdit = "add";
				if ($("#quoteTypeDetail").val() === "-1") {
					console.log('niaz::loadQuotesListDetails::21');
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:red;">Please Provide "Type"</b></span>');
					jQuery(newDialogDiv).dialog(
							{
								modal : true,
								width : 300,
								height : 150,
								title : "Warning",
								buttons : [ {
									height : 35,
									text : "OK",
									click : function() {
										$("#addquotesList_ilcancel")
												.trigger("click");
										$(this).dialog("close");
									}
								} ]
							}).dialog("open");
					return false;
				} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
					console.log('niaz::loadQuotesListDetails::21');
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:red;">Please Provide "Submitted By"</b></span>');
					jQuery(newDialogDiv).dialog(
							{
								modal : true,
								width : 300,
								height : 150,
								title : "Warning",
								buttons : [ {
									height : 35,
									text : "OK",
									click : function() {
										$("#addquotesList_ilcancel")
												.trigger("click");
										$(this).dialog("close");
									}
								} ]
							}).dialog("open");
					return false;
				}*/ 
				
				/*else {// if(aGlobalVariable!='edit')
					console.log('niaz::loadQuotesListDetails::23');
					var aJobNumber = $("#jobNumberHiddenID").val();
					var aQuoteType = $("#quoteTypeDetail").val();
					var aQuoteRev = $("#jobQuoteRevision").val();
					var isValidationNeed = false;
					// alert(aQuotetypePrev+" :: "+aQuoteType);
					// alert(aQuotePrev+" :: "+aQuoteRev);
					// alert($('#joHeaderID').val()+" :: "+aAddandEdit);
					// alert(aAddandEdit);
					var joHeaderId = $('#joHeaderID').val();

					// (aAddandEdit == 'add')&&
					if ((aAddandEdit == 'add' || aAddandEdit == 'edit')
							&& (aQuotePrev != aQuoteRev)
							&& (aQuotetypePrev != aQuoteType)) {
						isValidationNeed = true;
					}
					if ($('#joHeaderID').val() != '') {
						aAddandEdit = 'edit';
					}

					if (isValidationNeed) {
						console
								.log('niaz::loadQuotesListDetails::24:::aQuoteType='
										+ aQuoteType
										+ ":::aQuoteRev="
										+ aQuoteRev
										+ ":::joHeaderId="
										+ joHeaderId
										+ "::operation="
										+ aAddandEdit
										+ ":::aJobNumber=" + aJobNumber);
						$
								.ajax({
									url : "./jobtabs2/checkQuoteTypeAndRev",
									type : "GET",
									data : {
										'jobNumber' : aJobNumber,
										'quoteType' : aQuoteType,
										'quoteRev' : aQuoteRev,
										'joHeaderId' : joHeaderId,
										'operation' : aAddandEdit
									},
									success : function(data) {
										console
												.log('niaz::loadQuotesListDetails::25');
										// alert('check saving or add
										// 585'+data);
										if (data) {
											console
													.log('niaz::loadQuotesListDetails::26');
											jQuery("#addButtonDiv")
													.dialog("close");

											var newDialogDiv2 = jQuery(document
													.createElement('div'));
											var errorText = "A Quote with Same Type and Revision already exist. Please change 'Type' or 'Revision'.";
											jQuery(newDialogDiv2)
													.html(
															'<span><b style="color:red;">'
																	+ errorText
																	+ '</b></span>');
											jQuery(newDialogDiv2)
													.dialog(
															{
																modal : true,
																width : 350,
																height : 170,
																title : "Warning",
																buttons : [ {
																	height : 35,
																	text : "OK",
																	click : function() {
																		console
																				.log('niaz::loadQuotesListDetails::27');
																		$(
																				"#addquotesList_ilcancel")
																				.trigger(
																						"click");
																		$(
																				this)
																				.dialog(
																						"close");
																		return false;
																		// trigger("reloadGrid");
																	}
																} ]
															}).dialog(
															"open");
											// return false;
										} else {
											try {
												console
														.log('niaz::loadQuotesListDetails::28');
												console
														.log('saveQuoteDetailInfo uncommented 3');
												var joQuoteHeader = saveQuoteDetailInfo(true);
												$('#joHeaderID').val(
														joQuoteHeader);
												$("#addquotesList")
														.jqGrid(
																'setCell',
																1,
																'joQuoteHeaderID',
																joQuoteHeader);
												$("#joQuoteheader")
														.text(
																joQuoteHeader);
												$(
														'#new_row_joQuoteHeaderID')
														.val(
																joQuoteHeader);
												// alert('inside false
												// 610'+$('#new_row_joQuoteHeaderID').val());
											} catch (err) {
												// alert(err.message);
											}
											return true;
										}
									}
								});
					} else {
						console.log('niaz::loadQuotesListDetails::29');
						var count = $("#addquotesList").getGridParam(
								"reccount")
						if (count == 0) {
							console
									.log('saveQuoteDetailInfo uncommented 4');
							var joQuoteHeader = saveQuoteDetailInfo(gridInfo);
							$('#joHeaderID').val(joQuoteHeader);
							$("#joQuoteheader").text(joQuoteHeader);
							// alert('inside false 610'+joQuoteHeader);
						}

					}
				}
				return false;*/
				
			});
			
			
			//new_row_itemQuantity
			//new_row_cost
			//new_row_price

			// $("#new_row_percentage").on('keyup', function() {
			//
			// });
			//
			// $("#new_row_percentage").keypress(function() {
			//
			// });

			$("#new_row_cost").live('focus', function() {
				if ($("#new_row_cost").val() == '$0.00') {
					$("#new_row_cost").val(0);
				}
			});
			var sellPri = "";
			$("#new_row_cost").live('blur',	function() {
								var quantity = $("#new_row_itemQuantity").val();
								var productCost = $("#new_row_cost").val();
								productCost = productCost.replace(/[^-0-9\.]+/g, "").replace(".00", "");
								if (isNaN(productCost)) {
									productCost = 0;
								}
								if (isNaN(quantity)) {
									quantity = 0;
								}
								if (quantity !== '') {
									sellPri = productCost * quantity;
									sellPri = Math.round(sellPri);
									sellPri = parseInt(sellPri);
									// pri=pri.replace(/[^-0-9\.]+/g,
									// "").replace(".11", ".00");
								}
								$("new_row_price").val(sellPri);
							});
				$("#new_row_price").live('focus', function() {

				$("#new_row_price").val(sellPri);

			});
			
		}
		
		
		
/*
$("#quoteTemplateProductsGrid_iladd").live('click', function() {
	alert("Test in Ready Fn");
});*/

		
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
				jQuery(newDialogDiv).dialog({modal : true, width : 300, height : 150, title : "Warning",buttons : [ {height : 35,text : "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click : function() {$(this).dialog("close");}} ]}).dialog("open");
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
		function saveANDcloseQuoteTemplate(saveornot){
			var editTemplateSerialized = $("#quoteTemplateManipulateForm").serialize();
			var  newjoQuoteTemplateHeaderId = $('#quoteTemplateHeaderID').val();
			
			
			/*if ($("#quoteTemplateTypeDetail").val() === "-1") {
				jQuery(newDialogDiv).html(
						'<span><b style="color:red;">Please Provide "Type"</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
							
					$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				return false;
			} else */if ($("#templateDescription").val() === '') {
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please provide Template Description</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
					$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				return false;
			}else{
				var isDetailsGridExist = $("#quoteTemplateProductsGrid").getGridParam("reccount");
				console.log('Row Count: '+isDetailsGridExist);
				if (isDetailsGridExist < 1){
					jQuery(newDialogDiv).html(
					'<span><b style="color:red;">Please add atleast One line Item</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 300,
				height : 150,
				title : "Warning",
				buttons : [ {
					height : 35,
					text : "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click : function() {
						
				$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
				}else{
			/*templateAddEdit = "edit";
			var editTemplateSerialized = $("#quoteTemplateManipulateForm").serialize();
			//alert('QuoteTempLineItemHeader: '+QuoteTempLineItemHeader);
			joQuoteTemplateHeaderId = QuoteTempLineItemHeader;
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
				success:function(data){*/
					saveANDcloseQuoteTemplateInline();
					if(!saveornot){
						//jQuery("#addquoteTemplate").dialog("close");
						$('#quoteTemplates').trigger("reloadGrid");
						}
					var validateMsg = "<b style='color:Green; align:right;'>Add/Edit Template Changes are Saved.</b>";	
					$("#createQuoteMsg").css("display", "block");
					$('#createQuoteMsg').html(validateMsg);
					setTimeout(function(){
					$('#createQuoteMsg').html("");						
					},2000);
					
				/*},
				error:function(error){
					jQuery("#addquoteTemplate").dialog("close");
					var validateMsg = "<b style='color:Green; align:right;'>Edit Template not Saved. Please try again later.</b>";	
					$("#createQuoteMsg").css("display", "block");
					$('#createQuoteMsg').html(validateMsg);
					setTimeout(function(){
					$('#createQuoteMsg').html("");						
					},2000);
					$('#quoteTemplates').trigger("reloadGrid");
				}
			});*/
			}
			}
		}
		function saveANDcloseQuoteTemplateInline(){
			var newDialogDiv = jQuery(document.createElement('div'));
			var returnvalue_del=DeleteNewQuoteTempListLineItems("Save");
			templateAddEdit = "Add";
			var editTemplateSerialized = $("#quoteTemplateManipulateForm").serialize();
			//alert('QuoteTempLineItemHeader: '+QuoteTempLineItemHeader);
			var  newjoQuoteTemplateHeaderId = $('#quoteTemplateHeaderID').val();
			var quoteTotalPrice_template=$("#quoteTemplateTotalPrice").val();
			if(quoteTotalPrice_template!=null){
				quoteTotalPrice_template = quoteTotalPrice_template.replace(/[^-0-9\.]+/g, "");
			}else{
				quoteTotalPrice_template=0.00;
			}
			var costTotalPrice_template=$("#totalpriceTemplateLabel").text();
			if(costTotalPrice_template!=null){
				costTotalPrice_template = costTotalPrice_template.replace(/[^-0-9\.]+/g, "");
			}else{
				costTotalPrice_template=0.00;
			}
			
			var quotechkTotalPrice_template=false;
			var templatename=$("#templateDescription").val();
			/*if($('#quotechkTotalPrice_template').attr('checked')) {
				quotechkTotalPrice_template=true;
			}*/
			//  alert(newjoQuoteTemplateHeaderId);
			//if(joQuoteTemplateHeaderId!=false){
			//	joQuoteTemplateHeaderId=-1;
				if(newjoQuoteTemplateHeaderId>0)
					templateAddEdit = "edit";
				//}
				//alert(templateAddEdit);
			//editTemplateSerialized ='quoteTemplateHeaderId='+newjoQuoteTemplateHeaderId+'&quotechkTotalPrice_template='+quotechkTotalPrice_template+'&quoteTotalPrice_template='+quoteTotalPrice_template+'&oper='+templateAddEdit+"&costTotalPrice="+costTotalPrice_template;
				var checkvalidate_template=true;
				var errortext="";
				if(templatename==""){
								errortext="You should select enter Template Description";
								checkvalidate_template=false;
							}
				if(templateAddEdit=="Add" && checkvalidate_template && checktemplatenamethereornot()){
					errortext="Template name already existing";
					checkvalidate_template=false;
				}
				var count = $("#addnewquotesTemplateList").getGridParam("reccount");
				if (checkvalidate_template && count == 0) {
					errortext="Add atleast One Line Item.";
					checkvalidate_template=false;		
				}
				if(checkvalidate_template){

				
				var rows = jQuery("#addnewquotesTemplateList").getDataIDs();
				  var deleteQuoteLineDetailID=new Array();
					 for(var a=0;a<rows.length;a++)
					 {
					    row=jQuery("#addnewquotesTemplateList").getRowData(rows[a]);
					   var id="#joQuoteTemplateHeaderIDforimg_"+rows[a];
					   var canDeleteQuote=$(id).is(':checked');
					   if(canDeleteQuote){
						  var var_joQuoteDetailMstrID=row['joQuoteTempDetailMstrID'];
						  if(var_joQuoteDetailMstrID!=undefined && var_joQuoteDetailMstrID!=null && var_joQuoteDetailMstrID!="" && var_joQuoteDetailMstrID!=0){
							  deleteQuoteLineDetailID.push(var_joQuoteDetailMstrID);
						 	}
						 $('#addnewquotesTemplateList').jqGrid('delRowData',rows[a]);
					  }
				} 
				var gridRows = $('#addnewquotesTemplateList').getRowData();
				var dataToSend = JSON.stringify(gridRows);
				console.log(dataToSend);
				
				
				var newDialogDiv = jQuery(document.createElement('div'));
			$.ajax({
				url:'./jobtabs2/SaveQuoteTempLineItems',
				async:false,
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				data :{'quoteTemplateHeaderId':newjoQuoteTemplateHeaderId,
					'quotechkTotalPrice_template':quotechkTotalPrice_template,
					'quoteTotalPrice_template':quoteTotalPrice_template,
					'costTotalPrice':costTotalPrice_template,
					"gridData":dataToSend,
					"DelQUOData":deleteQuoteLineDetailID,
					"oper":templateAddEdit,
					"templatename":templatename
					},
				//data:editTemplateSerialized,
				type:"POST",
				success:function(data){
					//alert(data);
					//$("#quoteTemplates").jqGrid('setCell',1,'joQuoteTemplateHeaderId',data);
					$("#joQuoteTemplateHeaderId").text(data);
					$("#quoteTemplateHeaderId").text(data);
					$("#quoteTemplateHeaderID").val(data);
					//$('#new_row_joQuoteTemplateHeaderId').val(data);
					var validateMsg = "<b style='color:Green; align:right;'>Template Saved Successfully</b>";	
					$('#labelforsuccess').html(validateMsg);
					setTimeout(function(){
					$('#labelforsuccess').html("");						
					},2000);
					getquotetempformvalue=true;
					$('#addnewquotesTemplateList').trigger("reloadGrid");
					$('#quoteTemplates').trigger("reloadGrid");
					
//					validatequotedisabledbuttons_temp();
				},
				error:function(error){
					var validateMsg = "<b style='color:red; align:right;'>Edit Template not Saved. Please try again later.</b>";	
					$("#createQuoteMsg").css("display", "block");
					$('#createQuoteMsg').html(validateMsg);
					setTimeout(function(){
					$('#createQuoteMsg').html("");						
					},2000);
					$('#quoteTemplates').trigger("reloadGrid");
				}
			});
			
				}else{
					jQuery(newDialogDiv).html("<span><b style='color:red;'>"+errortext+"</b></span>");
									jQuery(newDialogDiv).dialog({modal : true,
										width : 300,height : 150,title : "Warning",buttons : [ {height : 35,text : "OK",
											 keypress:function(e){
													var x = e.keyCode;
													if(x==13){$(newDialogDiv).dialog("close");}
											    },
											click : function() {$(this).dialog("close");}} ]
									}).dialog("open");
					}
		}
		function cancelQuoteTemplate(){
			
			createtpusage('job-Quote Tab','Cancel Template','Info','Job-Quote Tab,Cancelling Template,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
			jQuery("#addquoteTemplate").dialog("close");
			$('#quoteTemplates').trigger("reloadGrid");
			
		}
		function saveTemplateLineItem(){	
			//Save Quote Template Line item Grid
			var templatevalue = $("#productTemplate").val();
			var manufacturertemplate = $("#manufacturerTemplate").val();
			
			
			if(manufacturertemplate != null && templatevalue!=null && manufacturertemplate != '' && templatevalue !='' ){
			var joQuoteTemplateHeaderID = $('#joQuoteTemplateHeaderID').val();
			var vendorId = $("#rxManufacturerIDTemplate").val();
			if(vendorId == null){
				var errorText = "Please provide a valid vendor from the List";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click: function() { $(this).dialog("close");}}]}).dialog("open");
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
					console.log('Template LineItem Updated-Jenith');
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
			}else{
				var errorText = "Please Fill Mandatory fields Marked by *.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
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
				buttons: [{height:35,text: "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click: function() { $(this).dialog("close"); }}]}).dialog("open");
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
							data : { 'joQuoteTemplateHeaderId' :joQuoteTemplateHeaderId,'jobNumber' : jobNumber,'joMasterID': $("#joMaster_ID").text()},
							success: function(data) {
								//alert('Success Data: '+data.joQuoteHeaderId);
								displayQuoteDetails(data);
								var validateMsg = "Quote Templates are saved, Please click on edit for making changes.";
								$("#quoteAddMsg").css("display", "block");
								$('#quoteAddMsg').html(validateMsg);
								setTimeout(function() {
									$('#quoteAddMsg').html("");
								}, 2000);
								$("#quotes").trigger("reloadGrid");
							}
						});
					},
					"No": function (){jQuery(this).dialog("close");	}
				}}).dialog("open");
			}
		}
		
		
		function displayQuoteDetails(data) {
			aGlobalVariable = "edit";
			aGlobalConstant = "edit";
			isquoteAddnew = "no";
			var editQuotesView = '<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
					+ '<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelEditQuote()" style="width:80px;">';
			$("#addQuotesView").empty();
			$("#copyQuotesView").empty();
			$("#editQuotesView").empty();
			$("#editQuotesView").append(editQuotesView);
			aCancelQuote = '';
			var joQuoteHeaderID =data.joQuoteHeaderId;
			var quoteTypeID = data.cuMasterTypeID;
			var createdByName =data.createdByName;
			var quoteRevision =data.quoteRev;
			var quoteRemarks = data.remarks;
			var displayQuantity = data.displayQuantity;
			var printQuantity = data.printQuantity;
			var displayParagraph = data.displayParagraph;
			var printParagraph = data.printParagraph;
			var displayManufacturer = data.displayManufacturer;
			var printManufacturer = data.printManufacturer;
			var displayCost = data.displayCost;
			var printCost =  data.printCost;
			var displayPrice = data.displayPrice;
			var printPrice = data.printPrice;
			var notesFullWidth =  data.notesFullWidth;
			var lineNumbers = data.lineNumbers;
			
			$("#quoteTypeDetail option[value=" + quoteTypeID + "]").attr("selected",true);
			$("#jobQuoteRevision").val(quoteRevision);
			$("#quoteRemarksID").val(quoteRemarks);
			$("#quantityValueDisplayID").val(displayQuantity);
			$("#quantityValuePrintID").val(printQuantity);
			$("#quantityValueDisplayParaID").val(displayParagraph);
			$("#quantityValuePrintParaID").val(printParagraph);
			$("#quantityValueDisplayManufID").val(displayManufacturer);
			$("#quantityValuePrintManufID").val(printManufacturer);
			$("#quantityValueprintCostID").val(displayCost);
			$("#quantityValuePrintCostID").val(printCost);
			$("#quantityValueDisplayPriceID").val(displayPrice);
			$("#quantityValuePrintPriceID").val(printPrice);
			$("#inlineNotePage").val(notesFullWidth);
			$("#printLineID").val(lineNumbers);
			$("#inlineNotePage").val();
			$("#joDetailID").val("");
			
			$('#addquotesList').trigger("reloadGrid");
			$("#joQuoteheader").text(joQuoteHeaderID);
			$("#joHeaderID").val(joQuoteHeaderID);
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails();
			$('#addquotesList').jqGrid('setGridParam', {
				postData : {
					'joQuoteHeaderID' : joQuoteHeaderID
				}
			});
			
			jQuery("#addquotes").dialog("open");
			return true;
		}

		
		
		function viewLinePDF() {
			var gridData = jQuery("#addnewquotesTemplateList").getRowData();
			
			var grid = $("#quoteTemplates");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var rowData = grid.getRowData(rowId);
			var joQuoteTemplateHeaderId = rowData['joQuoteTemplateHeaderId'];
			
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
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
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
			var joQuoteHeaderID =  $('#quoteTemplateHeaderID').val();//grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');//$('#joQuoteLineTemplateHeaderID').val();
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
			var joQuotePrice = $("#quoteTemplateTotalPrice").val();//grid.jqGrid('getCell', rowId, 'quoteAmount');
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
			
			createtpusage('job-Quote Tab','Quote PDF Preview','Info','Job-Quote Tab,Previewing Quote PDF,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
			
			//window.open("./quotePDFController/viewQuoteTemplatePdfForm?enginnerName="
			window.open("./quotePDFController1/viewNewQuoteTemplatePdfForm?enginnerName="
					+ encodeBigurl(aEngineer) + "&architectName=" + encodeBigurl(aArchitect) + "&bidDate="
					+ aBidDate + " " + "&projectName=" + encodeBigurl(aProjectName) + "&planDate="
					+ aPlanDate + "&locationCity=" + aLocationCity + "&locationState="
					+ aLocationState + "&quoteTOName=" + encodeBigurl(aQuoteTo) + ""
					+ "&bidderContact=" + encodeBigurl(aJoBidderContact) + "&joQuoteHeaderID="
					+ joQuoteHeaderID + "&totalPrice=" + joQuotePrice + "&quoteRev="
					+ joQuoteRev + "" + "&QuoteThru=" + encodeBigurl(aThruAddendum) + "&todayDate="
					+ currenDate + "&jobNumber=" + aJobNumber + "&discountAmount="
					+ joDiscountPrice + "&quoteRemarks=" + encodeBigurl(aRemarks)
					+ "&paragraphCheck=" + aParagraph + "&manufactureCheck=" + aVendor
					+ "&submittedBy=" + encodeBigurl(createdBYFullName)
					+ "&lineJobNumber=" + jobNumber
					+ "&viewLinePDF=" + aviewLinePDF,"_blank");
			
			
			return false;
		}
		
		
//New Template screen

		
		function openAddQuoteTemplateDialog() {				// Add quote template
			getquotetempformvalue=true;
			templateAddEdit = 'add';
			aAddEditTemplate = "add";
			$('#quoteTemplateHeaderID').val('');
			$("#joQuoteLineTemplateHeaderID").val('-1');
			$("#quoteTemplateTypeDetail").val('-1');
			$("#templateDescription").val('');
			$("#quoteTemplateRemarksID").val('');
			clearallfields_template(true);
			$('#addnewquotesTemplateList').trigger("reloadGrid", [{page:1}]);
			$('#quotechkTotalPrice_template').attr('checked',false);
			jQuery("#addquoteTemplate").dialog("open");
			//$('#quoteTemplateProductsGrid').jqGrid('GridUnload');
			//addQuotetemplateLineItemGridColums('');
			//$('#quoteTemplateProductsGrid').trigger("reloadGrid");
		}
		function openEditQuoteTemplateDialog(){			// Edit Quote TEmplate
			getquotetempformvalue=true;
			clearallfields_template(true);
			templateAddEdit = 'edit';
			aAddEditTemplate = "edit";
			var grid = $("#quoteTemplates");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			//var joQuoteTemplateHeaderId = grid.jqGrid('getCell', rowId, 'joQuoteTemplateHeaderId');	
			 
			var rowData = grid.getRowData(rowId);
			var joQuoteTemplateHeaderId = rowData['joQuoteTemplateHeaderId'];
			
			console.log("joQuoteLineTemplateHeaderID: "+joQuoteTemplateHeaderId +" RowID: "+rowId);
			$('#quoteTemplateHeaderID').val(joQuoteTemplateHeaderId);
			$('#joQuoteLineTemplateHeaderID').val(joQuoteTemplateHeaderId);
			
			var cuMasterTypeId = grid.jqGrid('getCell', rowId, 'cuMasterTypeId');
			$("#quoteTemplateTypeDetail").val(cuMasterTypeId);
			var ownerId = grid.jqGrid('getCell', rowId, 'ownerId');
			var remarks = grid.jqGrid('getCell', rowId, 'remarks');
			var TemlateName = grid.jqGrid('getCell', rowId, 'templateName');
			var printTotal= grid.jqGrid('getCell', rowId, 'printTotal');
			if(printTotal==1){
				$('#quotechkTotalPrice_template').attr('checked',true);
			}else{
				$('#quotechkTotalPrice_template').attr('checked',false);
			}
			$("#templateDescription").val(TemlateName);
			$("#quoteTemplateRemarksID").val(remarks);
			if(!joQuoteTemplateHeaderId){
				var errorText = "Please select a row from the list to edit";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
				buttons: [{height:35,text: "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click: function() { $(this).dialog("close");}}]}).dialog("open");
				return false;
			}
			
			$("#addnewquotesTemplateList").trigger("reloadGrid", [{page:1}]);
			jQuery("#addquoteTemplate").dialog("open");
			//$('#quoteTemplateProductsGrid').jqGrid('GridUnload');
			//addQuotetemplateLineItemGridColums(joQuoteTemplateHeaderId);
			//$('#quoteTemplateProductsGrid').trigger("reloadGrid");
		}
		
		
		function clearallfields_template(value){
			$("#Item2_TextBox_template").val("");
			$("#Price_TextBox1_template").val("");
			$("#Price_TextBox2_template").val("");
			$("#Item3_TextBox1_template").val("");
			$("#Item3_TextBox2_template").val("");
			$("#Cost_TextBox_template").val("");
			$("#Manufacturer_TextBox_template").val("");
			$("#manufacturertextboxid_template").val("");
			$("#joQuoteDetailMstrID_template").val("");
			$("quoteCategorySelect_template").val("-1");
			if(value){
				$("#lineitemtypeid_template").val(0);
				LineItemtypeonchange_template(0);
				}
		}
		function LineItemtypeonchange_template(value){
			   clearallfields_template();
				if(value==1){
					$("#Item2_TextBox_template").css("display", "none");
					$("#Price_TextBox1_template").css("display", "none");
					$("#Price_TextBox2_template").css("display", "none");
					$("#Item3_TextBox1_template").css("display", "none");
					$("#Item3_TextBox2_template").css("display", "none");
					$("#Cost_TextBox_template").css("display", "none");
					$("#Manufacturer_TextBox_template").css("display", "none");
					$("#quoteCategorySelect_template").css("display", "none");
					if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						myCustomInitInstance(edd);
						}
					TinymceTextEditorEnabledisable(true);
					//tinyMCE.get('TinyMCETextEditor_template').setContent("");
					if(edd.dom!=undefined){
						edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 10);
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontName", false, "Times New Roman,serif");    
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontSize", false, "10pt");
						$("#lineitemtypeid_template").focus();
					}else{
						setTimeout(function(){edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 10);
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontName", false, "Times New Roman,serif");    
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontSize", false, "10pt");
						$("#lineitemtypeid_template").focus();
						}, 500);
					}
					document.getElementById("AddButtonTemplate").tabIndex = "11";
					
					//setTimeout(function(){$("#lineitemtypeid_template").focus();console.log("temp");}, 1000);
					
				}else if(value==2){
					//one textbox qty and editor
					$("#Item2_TextBox_template").css("display", "block");
					$("#Item2_TextBox_template").css("margin-bottom", "3px");
					$("#Price_TextBox1_template").css("display", "none");
					$("#Price_TextBox2_template").css("display", "none");
					$("#Item3_TextBox1_template").css("display", "none");
					$("#Item3_TextBox2_template").css("display", "none");
					$("#Cost_TextBox_template").css("display", "block");
					$("#Cost_TextBox_template").css("margin-bottom", "3px");
					$("#Manufacturer_TextBox_template").css("display","block");
					$("#Manufacturer_TextBox_template").css("margin-bottom", "3px");
					$("#quoteCategorySelect_template").css("display", "block");
					$("#quoteCategorySelect_template").css("margin-bottom", "3px");
					if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						myCustomInitInstance(edd);
						}
					TinymceTextEditorEnabledisable(true);
					
					quoteCategorySelectBox_template(-1);
					document.getElementById("Item2_TextBox_template").tabIndex = "10";
				   document.getElementById("Cost_TextBox_template").tabIndex = "11";
				   document.getElementById("Manufacturer_TextBox_template").tabIndex = "12";
				   document.getElementById("quoteCategorySelect_template").tabIndex = "13";
					// document.getElementById(edd.id + "_ifr") ie.TinyMCETextEditor_ifr
				   if(edd.dom!=undefined){
						edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 14);
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontName", false, "Times New Roman,serif");    
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontSize", false, "10pt");
						$("#lineitemtypeid_template").focus();
					}else{
						setTimeout(function(){edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 14);
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontName", false, "Times New Roman,serif");    
						tinyMCE.get('TinyMCETextEditor_template').execCommand("fontSize", false, "10pt");
						$("#lineitemtypeid_template").focus();
						}, 500);
					}
					document.getElementById("AddButtonTemplate").tabIndex = "15";
					
				}else if(value==3){
					//2 textbox qty,sellprice and editor
					$("#Item2_TextBox_template").css("display", "none");
					$("#Price_TextBox1_template").css("display", "none");
					$("#Price_TextBox2_template").css("display", "none");
					$("#Item3_TextBox1_template").css("display", "block");
					$("#Item3_TextBox1_template").css("margin-bottom", "3px");
					
					$("#Item3_TextBox2_template").css("display", "block");
					$("#Item3_TextBox2_template").css("margin-bottom", "3px");

					$("#Cost_TextBox_template").css("display", "block");
					$("#Cost_TextBox_template").css("margin-bottom", "3px");
					$("#Manufacturer_TextBox_template").css("display","block");
					$("#Manufacturer_TextBox_template").css("margin-bottom", "3px");
					$("#quoteCategorySelect_template").css("display", "block");
					$("#quoteCategorySelect_template").css("margin-bottom", "3px");
					if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						myCustomInitInstance(edd);
						}
					TinymceTextEditorEnabledisable(true);
					quoteCategorySelectBox_template(-1);
					   document.getElementById("Item3_TextBox1_template").tabIndex = "10";
					   document.getElementById("Item3_TextBox2_template").tabIndex = "11";
					   document.getElementById("Cost_TextBox_template").tabIndex = "12";
					   document.getElementById("Manufacturer_TextBox_template").tabIndex = "13";
					   document.getElementById("quoteCategorySelect_template").tabIndex = "14";
					// document.getElementById(edd.id + "_ifr") ie.TinyMCETextEditor_ifr
					   if(edd.dom!=undefined){
							edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 15);
							tinyMCE.get('TinyMCETextEditor_template').setContent("");
							tinyMCE.get('TinyMCETextEditor_template').execCommand("fontName", false, "Times New Roman,serif");    
							tinyMCE.get('TinyMCETextEditor_template').execCommand("fontSize", false, "10pt");
							$("#lineitemtypeid_template").focus();
						}else{
							setTimeout(function(){edd.dom.setAttrib(document.getElementById("TinyMCETextEditor_template_ifr"), 'tabindex', 15);
							tinyMCE.get('TinyMCETextEditor_template').setContent("");
							
							$("#lineitemtypeid_template").focus();
							}, 500);
						}
						document.getElementById("AddButtonTemplate").tabIndex = "16";
				}else if(value==4){
					//2textbox textbox and sellprice
					$("#Item2_TextBox_template").css("display", "none");
					$("#Price_TextBox1_template").css("display", "block");
					$("#Price_TextBox1_template").css("margin-bottom", "3px");
					$("#Price_TextBox2_template").css("display", "block");
					$("#Price_TextBox2_template").css("margin-bottom", "3px");
					$("#Item3_TextBox1_template").css("display", "none");
					$("#Item3_TextBox2_template").css("display", "none");
					$("#Cost_TextBox_template").css("display", "none");
					$("#Manufacturer_TextBox_template").css("display", "none");
					$("#quoteCategorySelect_template").css("display", "none");
					if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						myCustomInitInstance(edd);
						}
					TinymceTextEditorEnabledisable(false);
					quoteCategorySelectBox_template(-1);
					document.getElementById("Price_TextBox1_template").tabIndex = "10";
					document.getElementById("Price_TextBox2_template").tabIndex = "11";
					document.getElementById("AddButtonTemplate").tabIndex = "12";
					$("#lineitemtypeid_template").focus();
				}else{
					$("#Item2_TextBox_template").css("display", "none");
					$("#Price_TextBox1_template").css("display", "none");
					$("#Price_TextBox2_template").css("display", "none");
					$("#Item3_TextBox1_template").css("display", "none");
					$("#Item3_TextBox2_template").css("display", "none");
					$("#Cost_TextBox_template").css("display", "none");
					$("#Manufacturer_TextBox_template").css("display", "none");
					$("#quoteCategorySelect_template").css("display", "none");
					if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
						tinyMCE.get('TinyMCETextEditor_template').setContent("");
						myCustomInitInstance(edd);
						}
					TinymceTextEditorEnabledisable(false);
					
					document.getElementById("Item2_TextBox_template").tabIndex = "";
					document.getElementById("Cost_TextBox_template").tabIndex = "";
					document.getElementById("Manufacturer_TextBox_template").tabIndex = "";
					document.getElementById("quoteCategorySelect_template").tabIndex = "";
					document.getElementById("Item3_TextBox1_template").tabIndex = "";
					document.getElementById("Item3_TextBox2_template").tabIndex = "";
					document.getElementById("Price_TextBox1_template").tabIndex = "";
					document.getElementById("Price_TextBox2_template").tabIndex = "";
					document.getElementById("AddButtonTemplate").tabIndex = "";
				}
				
			   }
		function myCustomFormatterforlaunchbutton_template(cellValue, options, rowObject){
			var id=options.rowId+"_custombutton_temp";
			var element = '';
			if(rowObject['type']!=4){
				element ="<input type='button' class='savehoverbutton turbo-tan' style='width:70px;display: block;' id='"+id+"'  value='Launch' onclick=\"launchTextEditor_temp('"+options.rowId+"')\"  >";
			}else{
				element ="<input type='button' class='savehoverbutton turbo-tan' style='width:70px;display:none;' id='"+id+"'  value='Launch' onclick=\"launchTextEditor_temp('"+options.rowId+"')\"  >";
			}
			
		return element;
		}
		function categorybaseddisplay_temp(id){

			  var value=$("#"+id+"_type").val();
		$("#"+id+"_type").attr('tabindex',201);
		if(value!=4){
		$("#"+id+"_custombutton_temp").attr('tabindex',202);	 
		}
		$("#"+id+"_quantity").attr('tabindex',203);
		if(value!=4){
		$("#"+id+"_textbox").attr('tabindex',204);
		$("#"+id+"_sellprice").attr('tabindex',205);
		}else{
		$("#"+id+"_textbox").attr('tabindex',202);
		$("#"+id+"_sellprice").attr('tabindex',203);	
		}
		$("#"+id+"_cost").attr('tabindex',206);
		$("#"+id+"_vendorname").attr('tabindex',207);
		$("#"+id+"_category").attr('tabindex',208);
		$("#joQuoteTemplateHeaderIDforimg_"+id).attr('tabindex',209);
		$("#"+id+"_linebreak").attr('tabindex',210);
//			   setQuotesvalidation(0);
				if(value==1){
//					setQuotesvalidation(1);
					$("#"+id+"_quantity").css("display", "none");
					$("#"+id+"_sellprice").css("display", "none");
					$("#"+id+"_cost").css("display", "none");
					$("#"+id+"_vendorname").css("display", "none");
					$("#"+id+"_category").css("display", "none");
					$("#"+id+"_textbox").css("display", "none");
					$("#"+id+"_custombutton_temp").css("display", "block");
					
					
					
					$("#"+id+"_quantity").val("");
					$("#"+id+"_sellprice").val("");
					$("#"+id+"_cost").val("");
					$("#"+id+"_vendorname").val("");
					$("#"+id+"_manufacturer").val("");
					$("#"+id+"_category").val("");
					$("#"+id+"_textbox").val("");
					$("#"+id+"_type").focus();
					
				}else if(value==2){
					//one textbox qty and editor
//					setQuotesvalidation(2);
					$("#"+id+"_quantity").css("display", "block");
					$("#"+id+"_sellprice").css("display", "none");
					$("#"+id+"_cost").css("display", "block");
					$("#"+id+"_vendorname").css("display", "block");
					$("#"+id+"_category").css("display", "block");
					$("#"+id+"_textbox").css("display", "none");
					$("#"+id+"_custombutton_temp").css("display", "block");
					
					$("#"+id+"_sellprice").val("");
					$("#"+id+"_textbox").val("");
					$("#"+id+"_type").focus();
					
				}else if(value==3){
//					setQuotesvalidation(3);
					//2 textbox qty,sellprice and editor
					$("#"+id+"_quantity").css("display", "block");
					$("#"+id+"_sellprice").css("display", "block");
					$("#"+id+"_cost").css("display", "block");
					$("#"+id+"_vendorname").css("display", "block");
					$("#"+id+"_category").css("display", "block");
					$("#"+id+"_textbox").css("display", "none");
					$("#"+id+"_custombutton_temp").css("display", "block");
							
					$("#"+id+"_textbox").val("");
					$("#"+id+"_type").focus();
				}else if(value==4){
//					setQuotesvalidation(4);
					//2textbox textbox and sellprice
					$("#"+id+"_quantity").css("display", "none");
					$("#"+id+"_sellprice").css("display", "block");
					$("#"+id+"_cost").css("display", "none");
					$("#"+id+"_vendorname").css("display", "none");
					$("#"+id+"_category").css("display", "none");
					$("#"+id+"_textbox").css("display", "block");
					$("#"+id+"_custombutton_temp").css("display", "none");
					
					
					$("#"+id+"_quantity").val("");
					$("#"+id+"_cost").val("");
					$("#"+id+"_vendorname").val("");
					$("#"+id+"_manufacturer").val("");
					$("#"+id+"_category").val("");
					$("#"+id+"_type").focus();
				}else{
					//setQuotesvalidation(0);
					$("#"+id+"_quantity").css("display", "none");
					$("#"+id+"_sellprice").css("display", "none");
					$("#"+id+"_cost").css("display", "none");
					$("#"+id+"_vendorname").css("display", "none");
					$("#"+id+"_textbox").css("display", "none");
					$("#"+id+"_category").css("display", "none");
					$("#"+id+"_custombutton_temp").css("display", "none");
					
					$("#"+id+"_quantity").val("");
					$("#"+id+"_sellprice").val("");
					$("#"+id+"_cost").val("");
					$("#"+id+"_vendorname").val("");
					$("#"+id+"_manufacturer").val("");
					$("#"+id+"_category").val("");
					$("#"+id+"_textbox").val("");
					$("#"+id+"_type").focus();
				}
		}
		function launchTextEditor_temp(rowid){
			var addclickedornot=$( "#addnewquotesTemplateList_iladd" ).hasClass( "ui-state-disabled" );
			var editclickedornot=$( "#addnewquotesTemplateList_iledit" ).hasClass( "ui-state-disabled" );
				if((addclickedornot || editclickedornot)&& $("#"+rowid+"_type").val()==undefined){
					$("#addnewquotesTemplateList_ilsave").trigger("click");
				}else{
			if(CKEDITOR.instances["Quoteeditor_temp"]!=undefined)			
			{CKEDITOR.instances["Quoteeditor_temp"].destroy(true);}	
		CKEDITOR.replace('Quoteeditor_temp', ckEditorconfig);
		var htmlcontent=jQuery("#addnewquotesTemplateList").jqGrid('getCell', rowid, 'texteditor');
		$("#Quoteselrowid_temp").val(rowid);
		CKEDITOR.instances['Quoteeditor_temp'].setData(htmlcontent);
		setTimeout(function(){
			jQuery("#QuoteckEditordivbx_temp").dialog("open");
		}, 500);
		}
			
		}
		
		function checkvalidationforquote_temp( value, colname ){
			setTimeout(function(){$("#info_dialog").css("z-index", "12345");
			$("#info_dialog").css("left", "500px");
			}, 200);
			var aSelectedRowId= $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
			var type=$("#"+aSelectedRowId+"_type").val();
			result=validatelineitemmethod_ckeditor_temp(aSelectedRowId,type);
			return result;
		}
		function validatelineitemmethod_ckeditor_temp(aSelectedRowId,lineitemtypeid){
			   //LineItem_error
			    var result=[true];
			  	var Item2_TextBox= $("#"+aSelectedRowId+"_quantity").val();
			  	var Price_TextBox1=$("#"+aSelectedRowId+"_textbox").val();
			  	var Price_TextBox2=$("#"+aSelectedRowId+"_sellprice").val();
			  	var Item3_TextBox1=$("#"+aSelectedRowId+"_quantity").val();
			  	var Item3_TextBox2=$("#"+aSelectedRowId+"_sellprice").val();
			  	var Cost_TextBox=$("#"+aSelectedRowId+"_cost").val();
			  	var manufacturertextboxid=$("#"+aSelectedRowId+"_manufacturer").val();
			  	var joQuoteDetailMstrID=$("#addnewquotesTemplateList").jqGrid('getCell',aSelectedRowId, 'joQuoteTempDetailMstrID');;
				var quoteCategorySelect= $("#"+aSelectedRowId+"_category").val();
				
				var texteditor=$("#addnewquotesTemplateList").jqGrid('getCell',aSelectedRowId, 'description');
				
				var position=$("#"+aSelectedRowId+"_position").val();
				var errortext=""; 
				if(lineitemtypeid==1){
					 if(texteditor=="" || texteditor==null || texteditor=="<div></div>" ){
						 errortext="Please enter  Description";
						 result=[false, errortext];
						 /*$('#LineItem_error').html(errortext);
							setTimeout(function() {
								$('#LineItem_error').html("");
							}, 2000);*/
						 }
					 
				}else if(lineitemtypeid==2){
					errortext="Please enter Description";
					var Qtyvalidation=(Item2_TextBox=="" || Item2_TextBox==null)?true:false;
					Qtyvalidation=(chkvalidateQty_YN && Qtyvalidation)?true:false;
					errortext=Qtyvalidation==true?errortext+",Qty":errortext;
					var Cstvalidation=(Cost_TextBox=="" || Cost_TextBox==null)?true:false;
					Cstvalidation=(chkvaldateCost_YN && Cstvalidation)?true:false;
					errortext=Cstvalidation==true?errortext+",Cost":errortext;
					var Manvalidation=(manufacturertextboxid=="" || manufacturertextboxid==null)?true:false;
					Manvalidation=(chkvalidateMan_YN && Manvalidation)?true:false;
					errortext=Manvalidation==true?errortext+",Manufacturer":errortext;
					var Catvalidation=(quoteCategorySelect==-1 || quoteCategorySelect==null)?true:false;
					Catvalidation=(chkvalidateCategory_YN && Catvalidation)?true:false;
					errortext=Catvalidation==true?errortext+",Category":errortext;
					if(!(texteditor=="" || texteditor==null || texteditor=="<div></div>" )){
						errortext=errortext.replace("Description,","");
						errortext=errortext.replace("Description","");
					}
					
					//one textbox qty and editor
					//if((texteditor=="" || texteditor==null || texteditor=="<div></div>" )||(Item2_TextBox=="" || Item2_TextBox==null) || (Cost_TextBox=="" || Cost_TextBox==null) || (manufacturertextboxid=="" || manufacturertextboxid==null) || (quoteCategorySelect=="" || quoteCategorySelect==null)){
					if((texteditor=="" || texteditor==null || texteditor=="<div></div>" )|| Qtyvalidation || Cstvalidation || Manvalidation || Catvalidation){
						returnvalue=false;
						result=[false, errortext];
						/*$('#LineItem_error').html(errortext);
						setTimeout(function() {
							$('#LineItem_error').html("");
						}, 2000);*/
						}
					
				}else if(lineitemtypeid==3){
					errortext="Please enter Description";
					var Qtyvalidation=(Item3_TextBox1=="" || Item3_TextBox1==null)?true:false;
					Qtyvalidation=(chkvalidateQty_YN && Qtyvalidation)?true:false;
					errortext=Qtyvalidation==true?errortext+",Qty":errortext;
					var Item3SPvalidation=(Item3_TextBox2=="" || Item3_TextBox2==null)?true:false;
					Item3SPvalidation=(chkvalidateSPItem3_YN && Item3SPvalidation)?true:false;
					errortext=Item3SPvalidation==true?errortext+",Sellprice":errortext;
					var Cstvalidation=(Cost_TextBox=="" || Cost_TextBox==null)?true:false;
					Cstvalidation=(chkvaldateCost_YN && Cstvalidation)?true:false;
					errortext=Cstvalidation==true?errortext+",Cost":errortext;
					var Manvalidation=(manufacturertextboxid=="" || manufacturertextboxid==null)?true:false;
					Manvalidation=(chkvalidateMan_YN && Manvalidation)?true:false;
					errortext=Manvalidation==true?errortext+",Manufacturer":errortext;
					var Catvalidation=(quoteCategorySelect==-1 || quoteCategorySelect==null)?true:false;
					Catvalidation=(chkvalidateCategory_YN && Catvalidation)?true:false;
					errortext=Catvalidation==true?errortext+",Category":errortext;
					
					if(!(texteditor=="" || texteditor==null || texteditor=="<div></div>" )){
						errortext=errortext.replace("Description,","");
						errortext=errortext.replace("Description","");
					}
					
					//2 textbox qty,sellprice and editor
					if((texteditor=="" || texteditor==null || texteditor=="<div></div>" )||Qtyvalidation || Item3SPvalidation || Cstvalidation || Manvalidation || Catvalidation){
						returnvalue=false;
						/*$('#LineItem_error').html(errortext);
						setTimeout(function() {
							$('#LineItem_error').html("");
						}, 2000);*/
						result=[false, errortext];
						}
					
					
				}else if(lineitemtypeid==4){
					errortext="Please enter  Textbox";
					var priceSPvalidation=(Price_TextBox2=="" || Price_TextBox2==null) ?true:false;
					priceSPvalidation=(chkvalidateSPonPrice_YN && priceSPvalidation)?true:false;
					errortext=priceSPvalidation==true?errortext+",Sellprice":errortext;
					//2textbox textbox and sellprice
					if((Price_TextBox1=="" || Price_TextBox1==null) || priceSPvalidation){
						returnvalue=false;
						/*$('#LineItem_error').html(errortext);
						setTimeout(function() {
							$('#LineItem_error').html("");
						}, 2000);*/
						result=[false, errortext];
						}
					
				}
				return result;
			   }
		function addquotegridrow_temp(){
			var aSelectedRowId= $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
			var type=$("#"+aSelectedRowId+"_type").val();
			if(type==undefined){
				$("#addnewquotesTemplateList_iladd").trigger("click");
			}
		}
		function validatequotedisabledbuttons_temp(){
			var newquotetempformvalue=ValidateOverallQuote_temp();
			console.log("oldQuotetempFormvalue==="+oldQuotetempFormvalue);
			console.log("newquotetempformvalue==="+newquotetempformvalue);
			if(oldQuotetempFormvalue==newquotetempformvalue){
				//allow
				$("#QuoteTemppreviewButton").css({ opacity: 'initial' });
				$("#QuoteTemppreviewButton").attr("disabled", false);
				//$("#saveastemplateid").css({ opacity: 'initial' });
			}else{
				//dont allow
				$("#QuoteTemppreviewButton").css({ opacity: 0.3 });
				$("#QuoteTemppreviewButton").attr("disabled", true);
				//$("#saveastemplateid").css({ opacity: 0.3 });
			}
		}
		function ValidateOverallQuote_temp(){
			//var value1=$("#quoteTypeDetail").val();
			//var value2=$("#jobQuoteRevision").val();
			//var value3=$("#jobQuoteInternalNote").val();
			//var value4=$("#jobQuoteSubmittedBYFullName").val();
			//var value5=$('#quotechkTotalPrice').prop('checked');
			//var value6=$('#chk_includeLSD').prop('checked');
			//var value7=$('#chk_donotQtyitem2and3').prop('checked');
			//var value8=$('#chk_showTotPriceonly').prop('checked');
			var value=$("#templateDescription").val();
			var gridRows = $('#addnewquotesTemplateList').getRowData();
		    var new_Quotestlinesform_lines_form =  JSON.stringify(gridRows);
		    value=value+new_Quotestlinesform_lines_form;
			return value;
		}
		
		function setoverallQuotetotal_temp(){
			 var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs'); 
			 var totalamount=0;
			 var costtotalamount=0;
			 
			 for(var i=0;i<ids.length;i++){
				 var selectedRowId=ids[i];
				 var id="#joQuoteTemplateHeaderIDforimg_"+selectedRowId;
				 var canDo=$(id).is(':checked');
				 
				 var cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'sellprice');
				 if(cellValue!=undefined && cellValue!=null && cellValue!="" && cellValue!="" &&  !canDo){
					 cellValue=cellValue.replace(/[^-0-9\.]+/g, "");
					 totalamount=Number(totalamount)+Number(cellValue);
				 }
				 
				 
				 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'cost');
				 if(cellValue!=undefined && cellValue!=null && cellValue!="" && cellValue!="" &&  !canDo){
					 cellValue=cellValue.replace(/[^-0-9\.]+/g, "");
					 costtotalamount=Number(costtotalamount)+Number(cellValue);
				 }
				 
			 }
			 if(isNaN(totalamount)){
				 totalamount=0;
			 }
			 
			 if(isNaN(costtotalamount)){
				 costtotalamount=0;
			 }
			 $("#quotecosttotalamount_temp").val(costtotalamount);
			 $("#quoteTotalPrice_temp").val(totalamount);
			 
			 $("#sellpriceTemplateLabel").text(addCommas(formatCurrency(totalamount)));
			 $("#totalpriceTemplateLabel").text(addCommas(formatCurrency(costtotalamount)));
		}
		
		function addCommas(nStr)
		{
			nStr += '';
			x = nStr.split('.');
			x1 = x[0];
			x2 = x.length > 1 ? '.' + x[1] : '';
			var rgx = /(\d+)(\d{3})/;
			while (rgx.test(x1)) {
				x1 = x1.replace(rgx, '$1' + ',' + '$2');
			}
			return x1 + x2;
		}
		
		
		var posit_job_addnewquotesTemplateList=0;
		function loadnewquotesList_template(){
			var typeload = { '1':'Title','2':'Item2','3':'Item3','4':'Price'};
			var overallcategoryList=getcategoryList();
			$("#addnewquotesTemplateList").jqGrid({
				datatype: 'json',
				mtype: 'POST',
				ajaxGridOptions: { contentType: "application/x-www-form-urlencoded; charset=UTF-8" },
				postData : {'quoteTemplateHeaderID':function(){
					var quoteTemplateHeaderID=$("#quoteTemplateHeaderID").val();
					return quoteTemplateHeaderID;
				}},
				url:'./jobtabs2/loadquoteLineItems_template',
				pager: jQuery('#addnewquotesTemplatepager'), 
			   	colNames:['Type','Launch','Qty','Description','Textbox','Sell Price','Cost','Manufacturer','Category','<img src="./../resources/images/delete.png" style="vertical-align: middle;" onclick="DeleteNewQuoteTempListLineItems()">','Page','joQuoteDetailMstrID','joQuoteHeaderID','Type','Texteditor','ManufacturerId','Position'],
			   	colModel:[
{name:'type',index:'type', width:16,editable:true, hidden:false, editrules:{},formatter:"select",edittype:"select",editoptions:{value: typeload,tabindex:6,
		 dataEvents: [{type: 'change',
                          fn: function(e) {
                       	   var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
                       	   categorybaseddisplay_temp(aSelectedRowId);
                          }
                      },
                      {
                          type: 'keydown',
                          fn: function(e) {
                       	   if (e.keyCode === 9) {
                       		   var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
                       		   var type=$("#"+aSelectedRowId+"_type").val();
                       		   if(type==2 || type==3){
                       			   /*setTimeout(function(){
                       				   $("#"+aSelectedRowId+"_quantity").focus();
                      				}, 1000);*/
                       		   }
                       	   }
                          }
                      
                      }
                      
                      ]
	 },editrules:{edithidden:false}},
{name:'custombutton_temp',index:'custombutton_temp', width:20,editable:false, hidden:false,  formatter:myCustomFormatterforlaunchbutton_template,editrules:{required:false}, editoptions:{size:10,
		 dataEvents: [{type: 'change',
        fn: function(e) {
     	   var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
        }
    }]	 }},
{name:'quantity',index:'quantity', width:15,editable:true, hidden:false, editrules:{custom:true,custom_func:checkvalidationforquote_temp}, editoptions:{size:10,
		 dataEvents: [{
        type: 'keydown',
        fn: function(e) {
     	  /* if (e.keyCode === 27) {
     		   return false;
     	   }*/
        }
    }]	 }},
{name:'description',index:'description', width:80,height:5,editable:false, hidden:false,formatter : showonlydescription, editrules:{required:false}, editoptions:{size:10,
		 dataEvents: [{
        type: 'keydown',
        fn: function(e) {
     	   /*if (e.keyCode === 27) {
     		   return false;
     	   }*/
        }
    }]	 }},
{name:'textbox',index:'textbox', width:30,editable:true, hidden:false, editrules:{required:false}, editoptions:{size:10,maxlength: 40,
		 dataEvents: [{
        type: 'keydown',
        fn: function(e) {
     	   /*if (e.keyCode === 27) {
     		   return false;
     	   }*/
        }
    }]	 }},
{name:'sellprice',index:'sellprice', width:20,editable:true, hidden:false,formatter : newquotescustomCurrencyFormatter, editrules:{required:false}, editoptions:{size:10,
		 dataEvents: [{
        type: 'keydown',
        fn: function(e) {
     	  /* if (e.keyCode === 27) {
     		   return false;
     	   }*/
        }
    }]	 }},
{name:'cost',index:'cost', width:20,editable:true, hidden:false,formatter : newquotescustomCurrencyFormatter, editrules:{required:false}, editoptions:{size:10,
		 dataEvents: [{
        type: 'keydown',
        fn: function(e) {
     	 /*  if (e.keyCode === 27) {
     		   return false;
     	   }*/
        }
    }]	 }},
{name:'vendorname',index:'vendorname', width:40,editable:true, hidden:false, editrules:{required:false},  editoptions:{size:40,
		dataInit: function (elem) {
           $(elem).autocomplete({
               source: 'jobtabs2/vendorsList',
               minLength: 1,
               select: function (event, ui) {
               	var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
               	var id = ui.item.id;
   				var name = ui.item.value;
   				$("#"+aSelectedRowId+"_manufacturer").val(id);
   				 $("#"+aSelectedRowId+"_vendorname").focus();
               }
           });
     },dataEvents: [
	       			  /*{ type: 'focus', data: { i: 7 }, fn: function(e) { e.target.select(); } },*/
	    			  /*{ type: 'click', data: { i: 7 }, fn: function(e) { e.target.select(); } },*/
	    			  {type: 'keydown',fn: function(e) {/*if (e.keyCode === 27) {  return false;}*/}}
	    			  ]
			}},
{name:'category',index:'category', width:40,editable:true, hidden:false, editrules:{required:false}, formatter:"select",edittype:"select",editoptions:{value: overallcategoryList,
		 dataInit: function (elem) {
           var v = $(elem).val();
           // to have short list of options which corresponds to the country
           // from the row we have to change temporary the column property
       },dataEvents: [
                      {type: 'change',fn: function(e) {}},
                      {type: 'keydown',fn: function(e) {/*if (e.keyCode === 27) {  return false;}*/}}
                      ]
	 },editrules:{edithidden:false}},	
{name:'joQuoteTemplateHeaderIDforimg',index:'joQuoteTemplateHeaderIDforimg', width:5,editable:false, hidden:false,  formatter:QuoteDeleteImageFormatter_template,editrules:{required:false}, editoptions:{size:10}},			 
{name:'linebreak', index:'linebreak', align:'center',  width:20, hidden:false, editable:true, formatter:greencheckmarkcheckbox, edittype:'checkbox', editrules:{edithidden:true}, editoptions:{value:"Yes:true;No:false",defaultValue: "false", dataInit: function (elem) {
		$(elem).addClass("checkgreen");
   // to have short list of options which corresponds to the country
   // from the row we have to change temporary the column property
}}}, 
{name:'joQuoteTempDetailMstrID',index:'joQuoteTempDetailMstrID', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},			
{name:'joQuoteTemplateHeaderID',index:'joQuoteTemplateHeaderID', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
{name:'typename',index:'typename', width:20,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10,
		 dataEvents: [{
                    type: 'keydown',
                    fn: function(e) {
                 	  /* if (e.keyCode === 27) {
                 		   return false;
                 	   }*/
                    }
                }]	 
	 }},				   	 			
{name:'texteditor',index:'texteditor', width:40,height:5,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},		 
{name:'manufacturer',index:'manufacturer', width:60,editable:true, hidden:true,formatter:manufacturerIntegerFormatter, editrules:{required:false}, editoptions:{size:10}},
	 {name:'position',index:'position', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}}, 
				   	 ],
				pgbuttons: true,	
				autoencode:true,
				recordtext: '',
				//rowList: [25, 50, 75, 100],
				viewrecords: true,
				sortorder: "asc",
				altRows: true,
				altclass:'myAltRowClass',
				imgpath: 'themes/basic/images',
				caption: '',
				height : 350,
				width : 1100,
				rownumbers:true,
				cellsubmit: 'clientArray',
				editurl: 'clientArray',
				rowNum: 100000,/*If need pagination should comment this line*If need pagination should comment this line
								*Commented By Velmurugan
								*As per Eric requirement
								*BID 491*/
				loadonce: false,
				ondblClickRow: function(rowid) {
		    		//editnewquoteslist(rowid);
		    		var lastSelQuotetemp = '';
				     if(rowid && rowid!==lastSelQuotetemp){ 
				        jQuery(this).restoreRow(lastSelQuotetemp); 
				        lastSelQuotetemp=rowid; 
				     }
				     $("#addnewquotesTemplateList_iledit").trigger("click");
		    	},
		    	loadBeforeSend: function(xhr) {
		    		
					posit_job_addnewquotesTemplateList= jQuery("#addnewquotesTemplateList").closest(".ui-jqgrid-bdiv").scrollTop();
		    	},
		    	gridComplete:function(data) {
		    		$("#addnewquotesTemplateList").unblock();
		    		jQuery("#addnewquotesTemplateList").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_addnewquotesTemplateList);
		             posit_job_addnewquotesTemplateList=0;
		    		var quotetemplateheaderid=$("#quoteTemplateHeaderID").val();
		    		if(getquotetempformvalue){
		    			oldQuotetempFormvalue=ValidateOverallQuote_temp();
		        		getquotetempformvalue=false;
		        		validatequotedisabledbuttons_temp();
		        		settotalAndCostAmountForQuote_template(quotetemplateheaderid);
		        		}
		    		/*
		    		 var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs'); 
		    		 var totalamount=0;
		    		 for(var i=0;i<ids.length;i++){
		    			 var selectedRowId=ids[i];
		    			 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'sellprice');
		    			 if(cellValue!=""){
		    				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		    				 totalamount=Number(totalamount)+Number(cellValue);
		    			 }
		    			
		    		 }
		    		 if(isNaN(totalamount)){
		    			 totalamount=0;
		    		 }
		    		 $("#quoteTemplateTotalPrice").val(formatCurrency(totalamount));
		    		 
		    		 var costtotalamount=0;
		    		 for(var i=0;i<ids.length;i++){
		    			 var selectedRowId=ids[i];
		    			 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'cost');
		    			 if(cellValue!=""){
		    				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		    				 costtotalamount=Number(costtotalamount)+Number(cellValue);
		    			 }
		    		 }
		    		 if(isNaN(costtotalamount)){
		    			 costtotalamount=0;
		    		 }*/
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
			}).navGrid('#addnewquotesTemplatepager',
				{add:false,edit:false,del:false,refresh:false,search:false}
			);
			 $("#addnewquotesTemplateList").jqGrid(
						'inlineNav',
						'#addnewquotesTemplatepager',
						{
							edit : true,
							add : true,
							zIndex : 1234,
							refresh : false,
							cloneToTop : true,
							alertzIndex : 1234,
							addParams : {
								position: "last",
								addRowParams : {
									keys : true,
									oneditfunc : function(id) {
										lasteditcel=id;
										$("#SaveQuoteTemplateButtonID").css({ opacity: 0.3 });
										$("#SaveQuoteTemplateButtonID").attr("disabled", true);
										categorybaseddisplay_temp(id);
									},
									successfunc : function(response) {
										return true;
									},
									aftersavefunc : function(response) {
										setoverallQuotetotal_temp();
										var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs');
										var veaccrrowid;
										if(ids.length==1){
											veaccrrowid = 0;
										}else{
											var idd = jQuery("#addnewquotesTemplateList tr").length;
											for(var i=0;i<ids.length;i++){
												if(idd<ids[i]){
													idd=ids[i];
												}
											}
											 veaccrrowid= idd;
										}
										var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
										var changerowid;
										if(aSelectedRowId=="new_row"){
											console.log("IFselectedLineItemGrid"+aSelectedRowId); 
											$("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
											changerowid=Number(veaccrrowid)+1;
											$("#new_row_custombutton_temp").attr("id", changerowid+"_custombutton_temp");
											$("#"+changerowid+"_custombutton_temp").attr("onclick", "launchTextEditor_temp('"+changerowid+"')");
											//$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
											}else{
												 changerowid=aSelectedRowId;
											}
										
										var grid=$("#addnewquotesTemplateList");
										grid.jqGrid('resetSelection');
									    var dataids = grid.getDataIDs();
									    for (var i=0, il=dataids.length; i < il; i++) {
									        grid.jqGrid('setSelection',dataids[i], false);
									    }
									    
									    $("#joQuoteTemplateHeaderIDforimg_"+changerowid).removeAttr("tabindex");
									    $("#"+changerowid+"_linebreak").removeAttr("tabindex");
									    $("#"+changerowid+"_custombutton_temp").removeAttr("tabindex");
									    validatequotedisabledbuttons_temp();
									    $("#SaveQuoteTemplateButtonID").css({ opacity: 'initial'});
									    $("#SaveQuoteTemplateButtonID").attr("disabled", false);
									    
									},
									errorfunc : function(rowid, response) {
										return false;
									},
									afterrestorefunc : function(rowid) {
									}
								}
							},
							editParams : {
								keys : true,
								successfunc : function(response) {
									return true;
								},
								aftersavefunc : function(id) {
									setoverallQuotetotal_temp();
									var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs');
									var veaccrrowid;
									if(ids.length==1){
										veaccrrowid = 0;
									}else{
										var idd = jQuery("#addnewquotesTemplateList tr").length;
										for(var i=0;i<ids.length;i++){
											if(idd<ids[i]){
												idd=ids[i];
											}
										}
										 veaccrrowid= idd;
									}
									var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
									var changerowid;
									if(aSelectedRowId=="new_row"){
										console.log("IF selectedLineItemGrid"+aSelectedRowId);
										$("#" + aSelectedRowId).attr("id", Number(veaccrrowid)+1);
										changerowid=Number(veaccrrowid)+1;
										$("#new_row_custombutton_temp").attr("id", changerowid+"_custombutton_temp");
										$("#"+changerowid+"_custombutton_temp").attr("onclick", "launchTextEditor_temp('"+changerowid+"')");
										//$("#canDeleteSOID_new_row").attr("id","canDeleteSOID_"+changeidnum);
										}else{
										 changerowid=aSelectedRowId;
										}
									
									var grid=$("#addnewquotesTemplateList");
									grid.jqGrid('resetSelection');
								    var dataids = grid.getDataIDs();
								    for (var i=0, il=dataids.length; i < il; i++) {
								        grid.jqGrid('setSelection',dataids[i], false);
								    }
								    $("#joQuoteTemplateHeaderIDforimg_"+changerowid).removeAttr("tabindex");
								    $("#"+changerowid+"_linebreak").removeAttr("tabindex");
								    $("#"+changerowid+"_custombutton_temp").removeAttr("tabindex");
								    validatequotedisabledbuttons_temp();
								    $("#SaveQuoteTemplateButtonID").css({ opacity: 'initial'});
								    $("#SaveQuoteTemplateButtonID").attr("disabled", false);
								},
								errorfunc : function(rowid, response) {
									
								},
								afterrestorefunc : function( id ) {
								},
								oneditfunc : function(id) {
									lasteditcel=id;
									$("#SaveQuoteTemplateButtonID").css({ opacity:0.3});
									$("#SaveQuoteTemplateButtonID").attr("disabled", true);
									categorybaseddisplay_temp(id);
//									$("#"+id+"_ackDate").datepicker();
//									$("#"+id+"_shipDate").datepicker();
									/*var unitcost=$("#"+id+"_unitCost").val();
									unitcost=unitcost.replace(/[^0-9\-.]+/g,"");
									if(unitcost==undefined ||unitcost==""||unitcost==null){
										unitcost=0.00;
									}
									var amount=$("#"+id+"_quantityBilled").val();
									amount=amount.replace(/[^0-9\-.]+/g,"");
									if(amount==undefined ||amount==""||amount==null){
										amount=0.00;
									}
									$("#"+id+"_quantityBilled").val(amount);
									$("#"+id+"_unitCost").val(unitcost);*/
								
								}
							},restoreAfterSelect :false
						});
			//Drag And DROP
				jQuery("#addnewquotesTemplateList").jqGrid('sortableRows');
				jQuery("#addnewquotesTemplateList").jqGrid('gridDnD');
				jQuery("#addnewquotesTemplateList").jqGrid('bindKeys', { 
					"onEnter" : function(rowid) {
						$("#addnewquotesTemplateList_ilsave").trigger("click");
					}
					});
				$("#gbox_addnewquotesTemplateList").find(".ui-jqgrid-bdiv").click(function(e) {
					var addclickedornot=$( "#addnewquotesTemplateList_iladd" ).hasClass( "ui-state-disabled" );
					var editclickedornot=$( "#addnewquotesTemplateList_iledit" ).hasClass( "ui-state-disabled" );
					if ( !$(e.target).closest('#addnewquotesTemplateList').length ) {
						console.log("inside=="+$(e.target).closest('#addnewquotesTemplateList').length);
						if(addclickedornot || editclickedornot){
							$("#addnewquotesTemplateList_ilsave").trigger("click");
						}
					}else{
						/*var Selid = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
						if(Selid!=lasteditcel){console.log("outside of js test");
						if(addclickedornot || editclickedornot){
							console.log("outside1 of js test");
							$("#addnewquotesTemplateList_ilsave").trigger("click");
						}
						}*/
						

						var Selid = $("#addnewquotesTemplateList").jqGrid('getGridParam', 'selrow');
						var types=$("#"+lasteditcel+"_type").val();
						
						if(lasteditcel!=Selid && types!=undefined){
							$("#addnewquotesTemplateList").setSelection(lasteditcel);
							$("#addnewquotesTemplateList_ilsave").trigger("click");
						//	var ret = validatelineitemmethod_ckeditor_temp(lasteditcel,type);
						}
						console.log("Testing/....................."+$(e.target).closest('#addnewquotesTemplateList').length +"=="+lasteditcel+"=="+Selid+"=="+types)
						
					}
					console.log($(e.target).closest('#addnewquotesTemplateList').length);
				});
		}
		/*function loadnewquotesList_template(){
			$("#addnewquotesTemplateList").jqGrid({
				datatype: 'json',
				mtype: 'POST',
				postData : {'quoteTemplateHeaderID':function(){
					var quoteTemplateHeaderID=$("#quoteTemplateHeaderID").val();
					return quoteTemplateHeaderID;
				}},
				url:'./jobtabs2/loadquoteLineItems_template',
				If need pagination should uncomment this line*If need pagination should comment this line
				*Commented By Velmurugan
				*As per Eric requirement
				*BID 491
				pager: jQuery('#addnewquotesTemplatepager'), 
			   	colNames:['joQuoteDetailMstrID','joQuoteHeaderID','TypeId','Type','Qty','Description','Texteditor','Textbox','SellPrice','Cost','Manufacturer','ManufacturerId','Position','<img src="./../resources/images/delete.png" style="vertical-align: middle;" onclick="DeleteNewQuoteTempListLineItems()">','category'],
			   	colModel:[
			   	     {name:'joQuoteTempDetailMstrID',index:'joQuoteTempDetailMstrID', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'joQuoteTemplateHeaderID',index:'joQuoteTemplateHeaderID', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'type',index:'type', width:16,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'typename',index:'typename', width:20,editable:false, hidden:false, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'quantity',index:'quantity', width:15,editable:false, hidden:false, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'description',index:'description', width:80,height:5,editable:false, hidden:false, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'texteditor',index:'texteditor', width:40,height:5,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'textbox',index:'textbox', width:30,editable:false, hidden:false, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'sellprice',index:'sellprice', width:20,editable:false, hidden:false,formatter : newquotescustomCurrencyFormatter, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'cost',index:'cost', width:20,editable:false, hidden:false,formatter : newquotescustomCurrencyFormatter, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'vendorname',index:'vendorname', width:40,editable:false, hidden:false, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'manufacturer',index:'manufacturer', width:60,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'position',index:'position', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'joQuoteTemplateHeaderIDforimg',index:'joQuoteTemplateHeaderIDforimg', width:5,editable:false, hidden:false,  formatter:QuoteDeleteImageFormatter_template,editrules:{required:false}, editoptions:{size:10}},
				   	 {name:'category',index:'category', width:40,editable:false, hidden:true, editrules:{required:false}, editoptions:{size:10}}
				   	 ],
				pgbuttons: true,	
				recordtext: '',
				//rowList: [25, 50, 75, 100],
				viewrecords: true,
				sortorder: "asc",
				altRows: true,
				altclass:'myAltRowClass',
				imgpath: 'themes/basic/images',
				caption: '',
				height : 350,
				width : 1100,
				rownumbers:true,
				rowNum: 10000,If need pagination should comment this line*If need pagination should comment this line
								*Commented By Velmurugan
								*As per Eric requirement
								*BID 491
				loadonce: false,
		    	ondblClickRow: function(rowid) {
		    		editnewquotestemplatelist(rowid);
		    	},
		    	loadBeforeSend: function(xhr) {
		    		
					posit_job_addnewquotesTemplateList= jQuery("#addnewquotesTemplateList").closest(".ui-jqgrid-bdiv").scrollTop();
		    	},
		    	gridComplete:function(data) {
		    		$("#addnewquotesTemplateList").unblock();
		    		jQuery("#addnewquotesTemplateList").closest(".ui-jqgrid-bdiv").scrollTop(posit_job_addnewquotesTemplateList);
		             posit_job_addnewquotesTemplateList=0;
		    		var quotetemplateheaderid=$("#quoteTemplateHeaderID").val();
		    		settotalAndCostAmountForQuote_template(quotetemplateheaderid);
		    		 var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs'); 
		    		 var totalamount=0;
		    		 for(var i=0;i<ids.length;i++){
		    			 var selectedRowId=ids[i];
		    			 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'sellprice');
		    			 if(cellValue!=""){
		    				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		    				 totalamount=Number(totalamount)+Number(cellValue);
		    			 }
		    			
		    		 }
		    		 if(isNaN(totalamount)){
		    			 totalamount=0;
		    		 }
		    		 $("#quoteTemplateTotalPrice").val(formatCurrency(totalamount));
		    		 
		    		 var costtotalamount=0;
		    		 for(var i=0;i<ids.length;i++){
		    			 var selectedRowId=ids[i];
		    			 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'cost');
		    			 if(cellValue!=""){
		    				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		    				 costtotalamount=Number(costtotalamount)+Number(cellValue);
		    			 }
		    		 }
		    		 if(isNaN(costtotalamount)){
		    			 costtotalamount=0;
		    		 }
//		    		 $("#sellpriceTemplateLabel").text(formatCurrency(totalamount));
//		    		 $("#totalpriceTemplateLabel").text(formatCurrency(costtotalamount));
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
			}).navGrid('#addnewquotesTemplatepager',
				{add:false,edit:false,del:false,refresh:false,search:false}
			);
			// *******Up and Down up and down********
			jQuery("#addnewquotesTemplateList")
					.jqGrid(
							'sortableRows',
							{
								update : function(ev, ui) {
									// alert("Hi");
									var item = ui.item[0], ri = item.rowIndex, itemId = item.id, message = "Start id="
											+ itemId
											+ " is moved. The new row End-2 index  "
											+ ri;
									var theSelectedRowID = itemId;
									// alert(message);
									var aSelectedRowId = $("#addnewquotesTemplateList").jqGrid(
											'getGridParam', 'selrow');
									var aSelectedQuoteDetailID = $('#addnewquotesTemplateList')
											.jqGrid('getRowData', ui.item[0].id)['position'];
									var aSelectedJoQuoteHeaderID = $('#addnewquotesTemplateList')
											.jqGrid('getRowData', ui.item[0].id)['joQuoteTemplateHeaderID'];
									// alert("Row: "+itemId+" Row Index: "+ri+ " Row
									// Data: "+$('#addquotesList').jqGrid('getRowData',
									// ui.item[0].id)['joQuoteDetailID']);
									var endQuoteDetailID = $('#addnewquotesTemplateList').jqGrid(
											'getRowData', ri)['position'];
									var upOrDown = '';
									var difference = "";
									if (itemId > ri) {
										upOrDown = 'upWards';
										difference = Number(itemId) - Number(ri);
									}
									if (itemId < ri) {
										upOrDown = 'downWards';
										difference = Number(ri) - Number(itemId);
									}
									
									updateQuotenewItemPosition_template_DragDrop(
											aSelectedQuoteDetailID,
											aSelectedJoQuoteHeaderID, upOrDown,
											difference, endQuoteDetailID);

									if (ri > 1 && ri < this.rows.length - 1) {
										
										 * alert(message + '\nThe row is inserted
										 * between item with 3 rowid=' +
										 * this.rows[ri-1].id + ' and the item with the
										 * 4 rowid=' + this.rows[ri+1].id);
										 										 
									} else if (ri > 1) {
										// alert(message +'\nThe row is inserted as the
										// last item after the item with rowid=' +
										// this.rows[ri-1].id);
									} else if (ri < this.rows.length - 1) {
										// alert(message +'\nThe row is inserted as the
										// first item before the item with rowid='
										// +this.rows[ri+1].id);
									} else {
										// alert(message);
									}
									return false;

								}
							});
			
		}*/
		var pdfPreview_newQuoteTemplate=false;
		function SaveDetailLineItems_template(){
		  	var Item2_TextBox_template= $("#Item2_TextBox_template").val();
		  	var Price_TextBox1_template=$("#Price_TextBox1_template").val();
		  	var Price_TextBox2_template=$("#Price_TextBox2_template").val();
		  	var Item3_TextBox1_template=$("#Item3_TextBox1_template").val();
		  	var Item3_TextBox2_template=$("#Item3_TextBox2_template").val();
		  	var Cost_TextBox_template=$("#Cost_TextBox_template").val();
		  	var manufacturertextboxid_template=$("#manufacturertextboxid_template").val();
		  	var joQuoteDetailMstrID_template=$("#joQuoteDetailMstrID_template").val();
			var lineitemtypeid_template= $("#lineitemtypeid_template").val();
			var texteditor_template="";
			if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
				texteditor_template=tinyMCE.get('TinyMCETextEditor_template').getContent();
				}
			
			var position_template=$("#newquotesposition_template").val();
			var quoteCategorySelect=$("#quoteCategorySelect_template").val();
			if(quoteCategorySelect==null){
				quoteCategorySelect=-1;
			}
			var quotechkTotalPrice_template=false;
			if($('#quotechkTotalPrice_template').attr('checked')) {
				quotechkTotalPrice_template=true;
			}
			
			
			//header information
			var quoteTemplateHeaderID=$("#quoteTemplateHeaderID").val();
			var quoteTemplateTypeDetail= $("#quoteTemplateTypeDetail").val();
			//var jobQuoteRevision=$("#jobQuoteRevision").val();
			//var jobQuoteSubmittedBYID=$("#jobQuoteSubmittedBYID").val();
			//var jobQuoteInternalNote=$("#jobQuoteInternalNote").val();
			var quoteTemplateTotalPrice=$("#quoteTemplateTotalPrice").val();
			var joMasterID=$("#joMasterID").val();
			var templateDescription=$("#templateDescription").val();
			//var jobQuoteSubmittedBYInitials=$("#jobQuoteSubmittedBYInitials").val();
			//var jobQuoteSubmittedBYFullName=$("#jobQuoteSubmittedBYFullName").val();
			//var quotecosttotalamount=$("#quotecosttotalamount").val();
			
			if(quoteTemplateTotalPrice!=null){
				quoteTemplateTotalPrice = quoteTemplateTotalPrice.replace(/[^-0-9\.]+/g, "");
			}
			if(quoteTemplateHeaderID==""){
				quoteTemplateHeaderID=0;
				}
			if(joQuoteDetailMstrID_template==""){
				joQuoteDetailMstrID_template=0;
				}
			if(position_template==""){
				position_template=0;
				}
			 
			if(Price_TextBox2_template!=null){
				Price_TextBox2_template = Price_TextBox2_template.replace(/[^-0-9\.]+/g, "");
				}
			if(Cost_TextBox_template!=null){
				Cost_TextBox_template = Cost_TextBox_template.replace(/[^-0-9\.]+/g, "");
				}
			if(Item3_TextBox2_template!=null){
				Item3_TextBox2_template = Item3_TextBox2_template.replace(/[^-0-9\.]+/g, "");
				}

			var errortext="";
			var checkvalidate_template=true;
			if(quoteTemplateTypeDetail==-1 || templateDescription==""){
				errortext="You should select Type,enter Template Description";
				checkvalidate_template=false;
			}
			if(checkvalidate_template){
				var validatelineitem_template=validatelineitemmethod_template();
			if(validatelineitem_template){	
				var returnvalue_del=DeleteNewQuoteTempListLineItems("Save");
			$.ajax({
				url : "./jobtabs2/addquoteLineItems_template",
				type : "POST",
				async:false,
				data : {
					 'lineitemtypeid_template':lineitemtypeid_template,
					'Item2_TextBox_template' : Item2_TextBox_template,
					'Price_TextBox1_template' :Price_TextBox1_template,
					'Price_TextBox2_template':Price_TextBox2_template,
					'Item3_TextBox1_template':Item3_TextBox1_template,
					'Item3_TextBox2_template':Item3_TextBox2_template,
					'Cost_TextBox_template':Cost_TextBox_template,
					'manufacturertextboxid_template':manufacturertextboxid_template,
					'joQuoteDetailMstrID_template':joQuoteDetailMstrID_template,
					'quoteTemplateHeaderID':quoteTemplateHeaderID,
					'texteditor_template':texteditor_template,
					'position_template':position_template,
					'quoteCategorySelect':quoteCategorySelect,   
					
					'quoteTemplateTypeDetail':quoteTemplateTypeDetail,
					'templateDescription':templateDescription ,
					'quoteTotalPrice':quoteTemplateTotalPrice,
					'joMasterID':joMasterID,
					'quotechkTotalPrice_template':quotechkTotalPrice_template
				},
				success : function(data) {
					createtpusage('job-Quote Tab','Add Line Items','Info','Job-Quote Tab,Adding Line Items,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val()+',quoteTemplateHeaderID:'+data);
					$("#quoteTemplateHeaderID").val(data);
					pdfPreview_newQuoteTemplate=true;
					$("#addnewquotesTemplateList").trigger("reloadGrid");
					$("#quoteTemplates").trigger("reloadGrid");
					/*if($("#addnewquotesTemplateList").getGridParam("reccount")==0){
						
						setTimeout(function() {
							var rowIds = $("#quoteTemplates").jqGrid('getDataIDs');
				            for (i = 1; i <= rowIds.length; i++) {
				               var rowData = $("#quoteTemplates").jqGrid('getRowData', rowIds[i]);
				                if (rowData['joQuoteTemplateHeaderId'] == data ) {
				                   $("#quoteTemplates").jqGrid('setSelection',rowIds[i]); 
				                } 

				            }
						}, 1000);
					}
					*/
		            
		            
		            
					clearallfields_template(true);
				}
			});
			}
			}else{
				jQuery(newDialogDiv).html("<span><b style='color:red;'>"+errortext+"</b></span>");
				jQuery(newDialogDiv).dialog({modal : true,
					width : 300,height : 150,title : "Warning",buttons : [ {height : 35,text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {$(this).dialog("close");}} ]
				}).dialog("open");
				}
			
		   }
	function validatelineitemmethod_template(){
		   //LineItem_error
		    var returnvalue=true;
		  	var Item2_TextBox_template= $("#Item2_TextBox_template").val();
		  	var Price_TextBox1_template=$("#Price_TextBox1_template").val();
		  	var Price_TextBox2_template=$("#Price_TextBox2_template").val();
		  	var Item3_TextBox1_template=$("#Item3_TextBox1_template").val();
		  	var Item3_TextBox2_template=$("#Item3_TextBox2_template").val();
		  	var Cost_TextBox_template=$("#Cost_TextBox_template").val();
		  	var manufacturertextboxid_template=$("#manufacturertextboxid_template").val();
		  	var quoteCategorySelect=$("#quoteCategorySelect_template").val();
		  	
		  	var joQuoteDetailMstrID_template=$("#joQuoteDetailMstrID_template").val();
			var lineitemtypeid_template= $("#lineitemtypeid_template").val();
			var texteditor_template="";
			if(tinyMCE.get('TinyMCETextEditor_template')!=undefined){
				texteditor_template=tinyMCE.get('TinyMCETextEditor_template').getContent();
				}
			var position_template=$("#newquotesposition_template").val();
			var errortext=""; 
			if(lineitemtypeid_template==1){
				 /*if(texteditor_template=="" || texteditor_template==null || texteditor_template=="" ){
					 errortext="Please enter  Description";
					 returnvalue=false;
					 }*/
				 
			}else if(lineitemtypeid_template==2){
				errortext="Please enter Description";
				var Qtyvalidation=(Item2_TextBox_template=="" || Item2_TextBox_template==null)?true:false;
				Qtyvalidation=(chkvalidateQty_YN && Qtyvalidation)?true:false;
				errortext=Qtyvalidation==true?errortext+",Qty":errortext;
				var Cstvalidation=(Cost_TextBox_template=="" || Cost_TextBox_template==null)?true:false;
				Cstvalidation=(chkvaldateCost_YN && Cstvalidation)?true:false;
				errortext=Cstvalidation==true?errortext+",Cost":errortext;
				var Manvalidation=(manufacturertextboxid_template=="" || manufacturertextboxid_template==null)?true:false;
				Manvalidation=(chkvalidateMan_YN && Manvalidation)?true:false;
				errortext=Manvalidation==true?errortext+",Manufacturer":errortext;
				var Catvalidation=(quoteCategorySelect=="" || quoteCategorySelect==null)?true:false;
				Catvalidation=(chkvalidateCategory_YN && Catvalidation)?true:false;
				errortext=Catvalidation==true?errortext+",Category":errortext;
				
				errortext=errortext.replace("Description,","");
				errortext=errortext.replace("Description","");
				//one textbox qty and editor
				if( Qtyvalidation || Cstvalidation || Manvalidation ||(quoteCategorySelect==null || quoteCategorySelect=="")){
					returnvalue=false;
					$('#LineItem_error_template').html(errortext);
					}
				
			}else if(lineitemtypeid_template==3){
				errortext="Please enter Description";
				var Qtyvalidation=(Item3_TextBox1_template=="" || Item3_TextBox1_template==null)?true:false;
				Qtyvalidation=(chkvalidateQty_YN && Qtyvalidation)?true:false;
				errortext=Qtyvalidation==true?errortext+",Qty":errortext;
				var Item3SPvalidation=(Item3_TextBox2_template=="" || Item3_TextBox2_template==null)?true:false;
				Item3SPvalidation=(chkvalidateSPItem3_YN && Item3SPvalidation)?true:false;
				errortext=Item3SPvalidation==true?errortext+",Sellprice":errortext;
				var Cstvalidation=(Cost_TextBox_template=="" || Cost_TextBox_template==null)?true:false;
				Cstvalidation=(chkvaldateCost_YN && Cstvalidation)?true:false;
				errortext=Cstvalidation==true?errortext+",Cost":errortext;
				var Manvalidation=(manufacturertextboxid_template=="" || manufacturertextboxid_template==null)?true:false;
				Manvalidation=(chkvalidateMan_YN && Manvalidation)?true:false;
				errortext=Manvalidation==true?errortext+",Manufacturer":errortext;
				var Catvalidation=(quoteCategorySelect=="" || quoteCategorySelect==null)?true:false;
				Catvalidation=(chkvalidateCategory_YN && Catvalidation)?true:false;
				errortext=Catvalidation==true?errortext+",Category":errortext;
				
				errortext=errortext.replace("Description,","");
				errortext=errortext.replace("Description","");
				//2 textbox qty,sellprice and editor
				if( Qtyvalidation || Item3SPvalidation || Cstvalidation || Manvalidation ||Catvalidation){
					returnvalue=false;
					$('#LineItem_error_template').html(errortext);
					}
				
				
			}else if(lineitemtypeid_template==4){
				errortext="Please enter  Textbox";
				var priceSPvalidation=(Price_TextBox2_template=="" || Price_TextBox2_template==null) ?true:false;
				priceSPvalidation=(chkvalidateSPonPrice_YN && priceSPvalidation)?true:false;
				errortext=priceSPvalidation==true?errortext+",Sellprice":errortext;
				//2textbox textbox and sellprice
				if((Price_TextBox1_template=="" || Price_TextBox1_template==null) || priceSPvalidation){
					returnvalue=false;
					$('#LineItem_error_template').html(errortext);
					setTimeout(function() {
						$('#LineItem_error_template').html("");
					}, 2000);
					}
				
			}else{
				returnvalue=false;
				errortext="Please select Lineitem Type";
				$('#LineItem_error_template').html(errortext);
				setTimeout(function() {
					$('#LineItem_error_template').html("");
				}, 2000);
				
			}
			return returnvalue;
		   }
	function editnewquotestemplatelist(rowid){
		var rowData = jQuery("#addnewquotesTemplateList").getRowData(rowid); 
		var joQuoteTempDetailMstrID  = rowData['joQuoteTempDetailMstrID'];
		var joQuoteTemplateHeaderID  = rowData['joQuoteTemplateHeaderID'];
		var type = rowData['type'];
		var typename  = rowData['typename'];
		var quantity = rowData['quantity'];
		var texteditor = rowData['texteditor'];
		var textbox  = rowData['textbox'];
		var sellprice = rowData['sellprice'];
		var cost = rowData['cost'];
		var vendorname = rowData['vendorname'];
		var manufacturer = rowData['manufacturer'];
		var position = rowData['position'];
		var category = rowData['category'];
		 

		/*$("#Item2_TextBox").val("");
		$("#Price_TextBox1").val("");
		$("#Price_TextBox2").val("");
		$("#Item3_TextBox1").val("");
		$("#Item3_TextBox2").val("");
		$("#Cost_TextBox").val("");
		$("#Manufacturer_TextBox").val("");
		$("#manufacturertextboxid").val("");
		$("#joQuoteDetailMstrID").val("");*/
		if(type==1){
			LineItemtypeonchange_template(type);
			$("#lineitemtypeid_template").val(type);
			setTimeout(function(){
				tinyMCE.get('TinyMCETextEditor_template').setContent(texteditor);
			}, 200);
			
		}else if(type==2){
			LineItemtypeonchange_template(type);
			$("#lineitemtypeid_template").val(type);
			$("#Item2_TextBox_template").val(quantity);
			$("#Cost_TextBox_template").val(cost.replace(/[^-0-9\.]+/g,""));
			$("#Manufacturer_TextBox_template").val(vendorname);
			$("#manufacturertextboxid_template").val(manufacturer);
			$("#quoteCategorySelect_template").val(category);
			setTimeout(function(){
				tinyMCE.get('TinyMCETextEditor_template').setContent(texteditor);
			}, 200);
		}else if(type==3){
			LineItemtypeonchange_template(type);
			$("#lineitemtypeid_template").val(type);
			$("#Item3_TextBox1_template").val(quantity);
			$("#Item3_TextBox2_template").val(sellprice.replace(/[^-0-9\.]+/g,""));
			$("#Cost_TextBox_template").val(cost.replace(/[^-0-9\.]+/g,""));
			$("#Manufacturer_TextBox_template").val(vendorname);
			$("#manufacturertextboxid_template").val(manufacturer);
			$("#quoteCategorySelect_template").val(category);
			setTimeout(function(){
				tinyMCE.get('TinyMCETextEditor_template').setContent(texteditor);
			}, 200);
		}else if(type==4){
			LineItemtypeonchange_template(type);
			$("#lineitemtypeid_template").val(type);
			$("#Price_TextBox1_template").val(textbox);
			$("#Price_TextBox2_template").val(sellprice.replace(/[^-0-9\.]+/g,""));
		}
		$("#joQuoteDetailMstrID_template").val(joQuoteTempDetailMstrID);
		$("#newquotesposition_template").val(position);
	}
	function QuoteDeleteImageFormatter_template(cellValue, options, rowObject){
		var position=rowObject['position'];
		var id="joQuoteTemplateHeaderIDforimg_"+options.rowId;
		var element = '';
		   //element = "<img src='./../resources/images/delete.png' style='vertical-align: middle;' onclick='deletenewquotelineitems_template("+cellValue+","+position+")'>";
		     element = "<input type='checkbox' id='"+id+"' onclick='setoverallQuoteTemptotal();clickQuotecheckboxChanges(this.id);' value=false>";
	return element;
	} 
	function deletenewquotelineitems_template(joquotetempDetailid,position){
		var quoteTemplateHeaderID=$("#quoteTemplateHeaderID").val();;
		if(quoteTemplateHeaderID==""){
			quoteTemplateHeaderID=0;
		}
		if(position==""){
			position=0;
		}
		$.ajax({
			url : "./jobtabs2/DeleteQuoteLineItems_template",
			type : "GET",
			async:false,
			data : {
				'joquotetempDetailid' : joquotetempDetailid,
				'quoteTemplateHeaderID' :quoteTemplateHeaderID,
				'position'		  :position
					},
			success : function(data) {
				$("#addnewquotesTemplateList").trigger("reloadGrid");
			}
			});
	}
	
	function updateQuotenewItemPosition_template_DragDrop(aSelectedQuotetempDetailID,
			aSelectedJoQuotetempHeaderID, upOrDown, difference, endQuotetempDetailID) {
		$("#addnewquotesTemplateList").block();
		$.ajax({
			url : "./jobtabs2/updatenewQuoteDetailsPosition_template",
			type : "POST",
			data : {
				'selectedQuoteDetailID' : aSelectedQuotetempDetailID,
				'selectedJoQuoteHeaderID' : aSelectedJoQuotetempHeaderID,
				'operate' : upOrDown,
				'difference' : difference,
				'endQuoteDetailID' : endQuotetempDetailID
			},
			success : function(data) {
				$("#addnewquotesTemplateList").trigger("reloadGrid");
				/*
				 * var newDialogDiv = jQuery(document.createElement('div'));
				 * jQuery(newDialogDiv).html('<span><b
				 * style="color:Green;">Quote details updated.</b></span>');
				 * jQuery(newDialogDiv).dialog({modal: true, width:300,
				 * height:150, title:"Success", buttons: [{height:35,text:
				 * "OK",click: function() { $(this).dialog("close");
				 * }}]}).dialog("open");
				 */
			}
		});
		return false;
	}
	
	function quoteCategorySelectBox_template(selboxid){
		$.ajax({
			url:"./job_controller/getjoQuotesCategoryData",
			type : "GET",
			async:false,
			data : {},
			success : function(data) {
				var ob = $("#quoteCategorySelect_template");
				ob.empty();
				if(selboxid=="-1"){
					ob.append("<option value='-1' selected='selected'>--Category--</option>");
				}else{
					ob.append("<option value='-1'>--None--</option>");
				}
				
				for (var i = 0; i < data.length; i++) {
				     var val = data[i].id;
				     var text =data[i].description;
				     ob.append("<option value="+val +">" + text + "</option>");
				}
			}
		});
	}
	function settotalAndCostAmountForQuote_template(joQuoteTemplateHeaderID){
		$.ajax({
			url : "./jobtabs2/settotalAndCostAmountForQuoteTemplate",
			type : "GET",
			data : {
				'joQuoteTemplateHeaderID' : joQuoteTemplateHeaderID
			},
			success : function(data) {
				$("#quoteTemplateTotalPrice").val(data.quoteAmount);
				$("#sellpriceTemplateLabel").text(addCommas(formatCurrency(data.quoteAmount)));
	   		 $("#totalpriceTemplateLabel").text(addCommas(formatCurrency(data.costAmount)));
			}
		});
	}
	function setoverallQuoteTemptotal(){
		 var ids = $("#addnewquotesTemplateList").jqGrid('getDataIDs'); 
		 var totalamount=0;
		 var costtotalamount=0;
		 
		 for(var i=0;i<ids.length;i++){
			 var selectedRowId=ids[i];
			 var id="#joQuoteTemplateHeaderIDforimg_"+selectedRowId;
			 var canDo=$(id).is(':checked');
			 
			 var cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'sellprice');
			 if(cellValue!=undefined && cellValue!=null && cellValue!="" && cellValue!="" &&  !canDo){
				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "");
				 totalamount=Number(totalamount)+Number(cellValue);
			 }
			 
			 
			 cellValue =$("#addnewquotesTemplateList").jqGrid ('getCell', selectedRowId, 'cost');
			 if(cellValue!=undefined && cellValue!=null && cellValue!="" && cellValue!="" &&  !canDo){
				 cellValue=cellValue.replace(/[^-0-9\.]+/g, "");
				 costtotalamount=Number(costtotalamount)+Number(cellValue);
			 }
			 
		 }
		 if(isNaN(totalamount)){
			 totalamount=0;
		 }
		 
		 if(isNaN(costtotalamount)){
			 costtotalamount=0;
		 }
		// $("#quoteTemplatecosttotalamount").val(costtotalamount);
		 $("#quoteTemplateTotalPrice").val(totalamount);
		 
		 $("#sellpriceTemplateLabel").text(formatCurrency(totalamount));
		 $("#totalpriceTemplateLabel").text(formatCurrency(costtotalamount));
	}
	
	function DeleteNewQuoteTempListLineItems(button){
		var deleteQuoteTempDetailMstrID=new Array();
		var deletePositionArray=new Array();
		var quoteTemplateHeaderID=$("#quoteTemplateHeaderID").val();
		if(quoteTemplateHeaderID==""){
			quoteTemplateHeaderID=0;
		}
		var rows = jQuery("#addnewquotesTemplateList").getDataIDs();
		 for(var a=0;a<rows.length;a++)
		 {
		    row=jQuery("#addnewquotesTemplateList").getRowData(rows[a]);
		   var id="#joQuoteTemplateHeaderIDforimg_"+rows[a];
		   var canDo=$(id).is(':checked');
			   if(canDo){
				  var joQuoteTempDetailMstrID=row['joQuoteTempDetailMstrID'];
				  if(joQuoteTempDetailMstrID!=undefined && joQuoteTempDetailMstrID!=null && joQuoteTempDetailMstrID!="" && joQuoteTempDetailMstrID!=0){
					  deleteQuoteTempDetailMstrID.push(joQuoteTempDetailMstrID);
					  deletePositionArray.push(row['position']);
				 	}
				 $('#addnewquotesTemplateList').jqGrid('delRowData',rows[a]);
			   }
		   }
		 if(deleteQuoteTempDetailMstrID!="" && deleteQuoteTempDetailMstrID.length>0){
			 
			 $.ajax({
					url : "./jobtabs2/DeleteQuoteTemplateMultipleLineItems",
					type : "GET",
					async:false,
					data : {
						'delData':deleteQuoteTempDetailMstrID,
						'delpositionData':deletePositionArray,
						'joQuoteTempHeaderID':quoteTemplateHeaderID
							},
					success : function(data) {
						if(button==null){
							$("#addnewquotesTemplateList").trigger("reloadGrid");
						}
						
					}
					});
		 }
		 
		 
		 
	}
	
	function checktemplatenamethereornot(){
		var templateDescription=$("#templateDescription").val();
		var returnvalue=false;
		$.ajax({
			url : "./jobtabs2/checktemplatenamethereornot",
			type : "GET",
			async:false,
			data : {
				'templateDescription' : templateDescription,
					},
			success : function(data) {
				if(data!=null && data){
					returnvalue= true;
				}else{
					returnvalue=false;
				}
				
			},
			error:function(data){
				returnvalue= false;
				}
			});
		return returnvalue;
	}