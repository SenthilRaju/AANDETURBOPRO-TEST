jQuery(document).ready(function() {
	$('#search').css('visibility','hidden');
	loadScheduleGrid();
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
var joSchedTempHeaderId = -1;

function loadScheduleGrid(){
	
	$("#scheduleGrid").jqGrid({
		datatype: 'JSON',
		mtype: 'GET',
		url: './modelMaintenanceController/joscheduleDiscription',
		colNames:['Scheddule Header Id','Description','Product','Manufacturer Name'],
	   	colModel:[
{name:'joSchedTempHeaderId',index:'joSchedTempHeaderId', width:100,editable:true,hidden: true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'description',index:'description', width:100,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'product',index:'product', width:100,hidden: false,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'name',index:'name', width:100,hidden: false,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}}
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
		caption: 'Schedule Template Description',
		height:400,	width: 600,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
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
    		joSchedTempHeaderId = rowData['joSchedTempHeaderId'];
    		var aQryStr = "./modelDetails?joSchedTempHeaderId="+joSchedTempHeaderId;
			document.location.href = aQryStr;
    	}
	});
	return true;
}
