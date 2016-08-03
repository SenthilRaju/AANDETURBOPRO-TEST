<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<table>
		<tr><td colspan="2"><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
		<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<table id="journal" style="width:20px"></table>
 	    <div id="journalpager"></div>
 	   	<br>
 	<table style="width:925px">
 		<tr height="20px;"></tr>
	</table>
	
	<script type="text/javascript">
		jQuery(document).ready(function(){
// 			$("#jobTabJournalHeader").html('');
// 			$("#jobTabJournalHeader").append('Journal <span>@#</span>');
			$("#OriJobNumber").css("display", "none");
			var aQuoteNumber = "${requestScope.joMasterDetails.quoteNumber}";
			if(aQuoteNumber !== ''){
				$("#OriJobNumber").show();
			}
			var jobStatus = getUrlVars()["jobStatus"];
			if(jobStatus === "Booked"){
				$(".customerNameField").attr("disabled", true);
			}
			$("#journal").jqGrid({
				url:'./jobtabs7/journal_grid?type=fetch',
				datatype: 'JSON',
				mtype: 'POST',
				pager: jQuery('#journalpager'),
				colNames: ['JoJournalId', 'Date','Entered By','Note','Status', 'joMasterId'],
				colModel :[
					{name:'joJournalId', index:'joJournalId', align:'center',hidden:true, width:60, editable:false, edittype:'text', 
								editoptions:{size:30}, editrules:{edithidden:false,required:false}},
		           	{name:'journalDateStr', index:'journalDateStr', align:'center', width:60, editable:false, edittype:'text', 
			        			editoptions:{size:30}, editrules:{edithidden:false,required:false}},
		           	{name:'journalByName', index:'journalByName', align:'left', width:60, editable:false, edittype:'text', 
			    				editoptions:{size:30}, editrules:{required:false}},
		           	{name:'journalNote', index:'journalNote', align:'left', width:480, editable:true, 
			    				edittype:'textarea', editoptions:{rows:"4",cols:"35"}, editrules:{required:true}},
		           	{name:'journalStatus', index:'journalStatus', align:'center', width:60, editable:true,hidden:false, edittype:'select', 
			    		        formatter:statusFormatter, editoptions:{value:{'':'',1:'Open',2:'Resolved'}}, editrules:{edithidden:false,required:false}},
    		        {name:'joMasterId', index:'joMasterId', align:'left', width:480, hidden:true, editable:false, 
			    				edittype:'textarea', editoptions:{rows:"4",cols:"35"}, editrules:{required:true}}
			    ],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Journal Details',
				height:420,	width: 1100, altRows: true, altclass:'myAltRowClass',
				postData: {
					jobNumber: function() { return $(".jobHeader_JobNumber").val(); },
					joMasterID: $("#joMaster_ID").text()
				},
				loadComplete: function(data) {
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				},
				jsonReader : {
					root: "rows", page: "page", total: "total", records: "records",
					repeatitems: false, cell: "cell", id: "id", userdata: "userdata"
				},
				editurl:"./jobtabs7/journal_grid?type=manipulate"
			}).navGrid('#journalpager',
				{add:true, edit:true,del:true,refresh:false,search:false}, //options
				//-----------------------edit options----------------------//
				{
					width:450, top: 350, left: 350,
					closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true, 
					modal:true, jqModel:true,
					editCaption: "Edit Job Journal Entry",
					beforeShowForm: function (form) 
					{
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').empty();
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').append('Note: ');
					},
					errorTextFormat: function (data) {	},
					onclickSubmit: function(params){
						var SelectedEveId = jQuery(this).jqGrid('getGridParam','selrow');
					    var joJournalId = jQuery(this).getCell(SelectedEveId, 0);
						var jobNumber = $(".jobHeader_JobNumber").val();
						return {'jobNumber':jobNumber,'joJournalId':joJournalId,'joMasterID':$("#joMasterID").val()};
					},
					afterSubmit : function(response, data) {
						$("#jobTabJournalHeader").html('');    
						var jomasterid = $("#joMasterID").val();
// 						alert(" jomasterid :: "+jomasterid);
						$.ajax({
							type : "POST",
							url : "./jobtabs7/journalHeaderIcon?joMasterID="+jomasterid,
							async:false,
							success: function(response){
 								//alert(" response :: "+response);
								$("#jobTabJournalHeader").html('');
								if(response == "resolved" ||response =="" )
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Used.bmp"/>');
								else if(response == "open" )
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Unresolved.bmp"/>');
								else if(response == "nojournals")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Empty.bmp"/>');
							},
							error : function(xhr, ajaxOptions, thrownError) {
							}
						});
						return [true,'',''];
					}
				},
				//-----------------------add options----------------------//
				{
					width:450, top: 350, left: 350,
					closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true, 
					modal:true, jqModel:true,
					addCaption: "New Job Journal Entry",
					beforeShowForm: function(form){
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').empty();
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').append('Note: ');
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>'); 
						jQuery('#TblGrid_journal #tr_journalStatus .CaptionTD').empty();
						jQuery('#TblGrid_journal #tr_journalStatus .CaptionTD').append('Status: ');
					},
					'onInitializeForm' : function(formid){
						jQuery('#TblGrid_journal #tr_journalNote .CaptionTD').append('<span style="color:red;" class="mandatory"> *</span>');
					},
					errorTextFormat: function (data) {	},
					onclickSubmit: function(params){
						var jobNumber = $(".jobHeader_JobNumber").val();
						return {'jobNumber':jobNumber,'joMasterID':$("#joMasterID").val()};
					},
					afterSubmit : function(response, data) {
						$("#jobTabJournalHeader").html('');	
						var jomasterid = $("#joMasterID").val();
						$.ajax({
							type : "POST",
							url : "./jobtabs7/journalHeaderIcon?joMasterID="+jomasterid,
							async:false,
							success: function(response){
// 								alert(" response :: "+response);
								$("#jobTabJournalHeader").html('');
								if(response == "resolved" || response =="")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Used.bmp"/>');
								else if(response == "open")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Unresolved.bmp"/>');
								else if(response == "nojournals")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Empty.bmp"/>');
							},
							error : function(xhr, ajaxOptions, thrownError) {
							}
						});
						return [true,'',''];
					}
				},
				//-----------------------Delete options----------------------//
				{
					closeOnEscape: true, reloadAfterSubmit: true,
					//modal:true,
					jqModal:false, width:300, top: 200, left: 350,
					caption: "Delete Journal",
					msg: 'Do you want delete selected Journal?',
					
					onclickSubmit: function(params){
						var SelectedEveId = jQuery(this).jqGrid('getGridParam','selrow');
					    var joJournalId = jQuery(this).getCell(SelectedEveId, 0);
					    return {'joJournalId':joJournalId,'joMasterID':$("#joMasterID").val()};
					},
					afterSubmit : function(response, data) {
						$("#jobTabJournalHeader").html('');	
						var jomasterid = $("#joMasterID").val();
						$.ajax({
							type : "POST",
							url : "./jobtabs7/journalHeaderIcon?joMasterID="+jomasterid,
							async:false,
							success: function(response){
// 								alert(" response :: "+response);
								$("#jobTabJournalHeader").html('');
								if(response == "resolved" || response =="" )
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Used.bmp"/>');
								else if(response == "open")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Unresolved.bmp"/>');
								else if(response == "nojournals")
									$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Empty.bmp"/>');
							},
							error : function(xhr, ajaxOptions, thrownError) {
							}
						});
						return [true,'',''];
					}
				},
				//-----------------------search options----------------------//
				{}
		
			);
	   	});
	   	 
		function statusFormatter(cellvalue, options, rowObject) {
			if(cellvalue === 1) {
				return "Open";
			} else if(cellvalue === 2) {
				return "Resolved";
			} else {
				return "";
			}
		}
	
	</script>