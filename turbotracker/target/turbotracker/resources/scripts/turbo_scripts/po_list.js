jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");
	$("#PurchaseOrdersGrid").jqGrid({
		url:'./po_listgrid',
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#PurchaseOrdersGridPager'),
		colNames:['vePOID','PO #','rxVendorID', 'joReleaseID', 'Created On', 'Job Name', 'Vendor', 'subtotal'],
		colModel :[
           	{name:'vePOID', index:'vePOID', align:'left', width:40, editable:true,hidden:true},
			{name:'ponumber', index:'ponumber', align:'left', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'rxVendorID', index:'rxVendorID', align:'', width:80,hidden:true},
			{name:'joReleaseID', index:'joReleaseID', align:'center', width:30,hidden:true},
			{name:'createdOn', index:'createdOn', align:'center', width:50,hidden:false},
			{name:'jobName', index:'jobName', align:'', width:40,hidden:false},
			{name:'vendorName', index:'vendorName', align:'left', width:80, hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}},
			{name:'subtotal', index:'subtotal', align:'right', width:40,hidden:false,formatter:customCurrencyFormatter}
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#PurchaseOrdersGridPager',
		sortname: 'ponumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Jobs',
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var aVePOId = rowData['vePOID'];
			var aQryStr = "aVePOId=" + aVePOId;
			document.location.href = "./editpurchaseorder?token=view&" + aQryStr;//./purchaseorder?token=view&" + aQryStr;
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
	}).navGrid('#PurchaseOrdersGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
});

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function addNewPO()
{
	document.location.href = "./addpurchaseorder";
	
}