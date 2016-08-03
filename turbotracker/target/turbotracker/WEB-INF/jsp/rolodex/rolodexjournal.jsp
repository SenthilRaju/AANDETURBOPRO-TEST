	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div id="vendorJournal">
		<table>
			<tr height="5px;"></tr>
			<tr>
				<td style="padding-bottom: 10px;"><label><b>Name:</b></label>
					<input type="text" name="EmployeeName" size="32" id="customerNameHeader" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><hr style="width:1100px; color:#C2A472;"></td>
			</tr>
		</table>
		<table>
			<tr>
				<td colspan="3">
					<div id="journalDetails" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="journalDetailsGrid" class="scroll"></table>
						<div id="jounalDetailsGridpager" class="scroll" style="text-align: right;"></div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			var customerName = $('#customerName').text();
			$("input#customerNameHeader").val($.trim(customerName));
			loadJournalsGrid();
		});
		function loadJournalsGrid() {
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
			var grid = $('#journalDetailsGrid');
			$("#journalDetailsGrid").jqGrid({
				url : 'rolodexdetails/rolodexJournal',
				datatype : 'JSON',
				mtype: 'GET',
				postData: {'rolodexNumber' : rolodexNumber },
				pager:jQuery("#jounalDetailsGridpager"),
				colNames: [ 'Date', 'User', 'Entry',  'RxMasterId', 'RxJournalId' ],
				colModel:
					[{name:'entryDateString',index: 'entryDateString',align:'center',width:30, formatter: customDateFormatter, editable:false,editoptions:{readonly:true},editrules:{edithidden:true},hidden: false },
					 {name:'name',index: 'name',align:'left',width: 60,hidden:false,editable:true,editoptions:{readonly:true},editrules: {edithidden: true}}, 
					 {name:'entryMemo',index: 'entryMemo',align:'left',width: 90,hidden:false,editable:true,edittype:'textarea', editoptions:{rows:"4",cols:"35"}, editrules:{required:true},
						 cellattr: function (rowId, tv, rawObject, cm, rdata) {
							 if(tv.length > 85){ return 'style="word-wrap: break-word;white-space: normal;"'; }
							 else{return tv;}
							 }
						 },
					 {name:'rxMasterId',index:'rxMasterId',align:'left',width:50,hidden:true,editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
					 {name:'rxJournalId',index:'rxJournalId',align:'left',width:50, hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}}],
				recordtext: '', rowList: [], pgtext: null, viewrecords: false, sortname:'date',
				sortorder: "asc", imgpath:'themes/basic/images',caption:'Journals',pgbuttons:false,
				height: 450, width:1100, altRows: true, altclass:'myAltRowClass', rowNum: 0,
				loadComplete:function(data) { 	},
				ondblClickRow:function(rowId) {
					var rowData = jQuery(this).getRowData(rowId);
					var journalId = rowData['rxJournalId']; 
				},
				jsonReader : {
					root: "rows", page: "page", total: "total",
					records: "records", repeatitems: false, cell: "cell",
					id: "id", userdata: "userdata"
				},
				loadError : function(jqXHR, textStatus, errorThrown) {	},
				editurl:"./rolodexdetails/manpulaterxJournal"
			}).navGrid("#jounalDetailsGridpager",
			{
				add:true, edit:true, del:true, search:false, view:false,
				alertcap: "Warning",
				alerttext: "Please, select an entry."
			},
			//-----------------------edit options----------------------//
			{
				width:450, top: 350, left: 350,
				closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true, 
				modal:true, jqModel:true,
				
				editCaption: "Edit Journal Entry",
				beforeShowForm: function(form){
					$("#rxMasterId").val(rolodexNumber);
					$("#rxJournalId").val();
					$("#tr_entryDate").show();
					$("#tr_name").show();
					$("#tr_entryMemo").show();
					$("#pData").hide();
					$("#nData").hide();
				}
			},
			//-----------------------add options----------------------//
			{
				width:450, top: 350, left: 350,
				closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true, 
				modal:true, jqModel:true,
				
				addCaption: "New Journal Entry",
				beforeShowForm: function(form){ 
					jQuery('#TblGrid_journalDetailsGrid #tr_entryMemo .CaptionTD').empty();
					jQuery('#TblGrid_journalDetailsGrid #tr_entryMemo .CaptionTD').append('Entry: ');
					jQuery('#TblGrid_journalDetailsGrid #tr_entryMemo .CaptionTD').append('<span style="color:red;" class="mandatory">*</span>'); 
					$("#rxMasterId").val(rolodexNumber);
					$("#tr_entryDate").hide();
					$("#tr_name").hide();
					$("#tr_entryMemo").show();
				},
				'onInitializeForm' : function(formid){
				},
				errorTextFormat: function (data) {	}
			},
			//-----------------------Delete options----------------------//
			{
				closeOnEscape: true, reloadAfterSubmit: true,
				//modal:true,
				jqModal:false, width:300, top: 200, left: 350,
				caption: "Delete Journal",
				msg: 'Do you want delete selected Journal?',

				onclickSubmit: function(params){
					var id = jQuery("#journalDetailsGrid").jqGrid('getGridParam','selrow');
					var key = jQuery("#journalDetailsGrid").getCell(id, 4);
					return { 'rxJournalId' : key};
				}
			});
			emptyMsgDiv.insertAfter(grid.parent());
			return true;
		}
		function customDateFormatter(cellValue, options, rowObject) {
			return cellValue;
		}

		function textAreaFormatter(cellValue, options, rowObject) {
			if(cellValue.length > 85){
				return 'style="white-space: normal;';
			}else{
				return cellValue;
			}
		}
		
	</script>