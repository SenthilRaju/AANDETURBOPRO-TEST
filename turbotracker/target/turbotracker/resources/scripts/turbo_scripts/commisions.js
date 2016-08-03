jQuery(document).ready(function(){
	    loadCommissionsList();
	    $("#search").hide();
});

function loadCommissionsList(){
	$("#commissionsGridList").jqGrid({
		datatype: 'JSON',
		mtype: 'POST',
		url:'./employeeCrud/employeeCommisions',
		//pager: jQuery('#commissionsGridPager'),
		colNames:['User Id', 'Full Name', 'Commissions', 'Adjusments', 'Payments'],
	   	colModel:[
			{name:'userLoginId',index:'userLoginId', width:50,editable:true, hidden: true, editrules:{required:true}, editoptions:{size:10}},
			{name:'webName',index:'webName', width:170,editable:true,align:'left', editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
			{name:'jobCommissions',index:'jobCommissions',align:'right', width:250,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}},
			{name:'adjustments',index:'adjustments',  align:'right', width:50,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}},
			{name:'payment',index:'payment',align:'right', width:200,editable:true, editrules:{required:true}, formatter:customCurrencyFormatter, editoptions:{size:10}}],
	   	rowNum: 0,
		pgbuttons: false,
		altRows: true,
		altclass:'myAltRowClass',	
		recordtext: '',
		viewrecords: true,
		//pager: '#commissionsGridPager',
		sortname: '', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Commissions',
		height:550,	width: 1150,/*scrollOffset:0,*/ rownumbers:true,
		loadComplete: function(data) { },
		loadError : function (jqXHR, textStatus, errorThrown) {
			var errorText = $(jqXHR.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:RED;">Error: ' + errorText + '</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");}}]}).dialog("open");
			return false;
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
    	ondblClickRow: function(rowid) { }
	}).navGrid('#commissionsGridPager',
			{add:false,edit:false,del:false,refresh:false,search:false});
}