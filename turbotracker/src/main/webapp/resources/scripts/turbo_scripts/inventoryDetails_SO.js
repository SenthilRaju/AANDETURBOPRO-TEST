
	
	


function loadInventoryDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	try {
		 $('#inventoryDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				//pager: jQuery('#inventoryDetailsPager'),
				url:'./inventoryList/getItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['cuSoid','cuSodetailId','Order Date', 'Date Promised','Customer','Job', 'Warehouse','QTY','RxCustomerID'],
				colModel :[
						{name:'cuSoid', index:'cuSoid', align:'center', width:20, editable:true,hidden:true},
						{name:'cuSodetailId', index:'cuSodetailId', align:'center', width:20, editable:true,hidden:true},
						{name:'createdOn', index:'createdOn', align:'center', width:20,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'datePromised', index:'datePromised', align:'center', width:20,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'name', index:'name', align:'center', width:40,hidden:false},
						{name:'jobName', index:'jobName', align:'center', width:20,hidden:false},
						{name:'city', index:'city', align:'center', width:20,hidden:false},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false},
						{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:20, editable:true,hidden:true}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			//pager: '#inventoryDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 700,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
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
				console.log("Selected id---->"+id);
				gridSelectedID = id;
			},
	    	ondblClickRow: function(rowid) {
				
	    	}
		}).navGrid('#inventoryDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);
	 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}


function loadOnOrderDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	try {
		 $('#OnOrderDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				//pager: jQuery('#OnOrderDetailsPager'),
				url:'./inventoryList/getOnOrderItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['vePOID','PO #','Order Date', 'Est. Ship Date','Vendor', 'Warehouse', 'QTY'],
				colModel :[
						{name:'vePOID', index:'vePOID', align:'center', width:20,hidden:true},
						{name:'ponumber', index:'ponumber', align:'center', width:20,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'createdOn', index:'createdOn', align:'center', width:20,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'estimatedShipDate', index:'estimatedShipDate', align:'center', width:20,hidden:false,formatter:'date',formatoptions:{ srcformat: 'Y-m-d',newformat:'m/d/Y'}},
						{name:'vendorName', index:'vendorName', align:'center', width:40,hidden:false},
						{name:'city', index:'city', align:'center', width:20,hidden:false},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:10,hidden:false}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			//pager: '#OnOrderDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 700,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
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
				console.log("Selected id---->"+id);
				gridOnOrderSelectedID = id;
			},
	    	ondblClickRow: function(rowid) {

				/*var rowData = jQuery(this).getRowData(rowid); 
				var acuSoid = rowData['vePOID'];*/
				/*$('#rxCustomer_ID').text(aMasterID);
				$('#Cuso_ID').text(acuSoid);
				$('#operation').val('update');
				PreloadData(acuSoid);
				$('#salesrelease').dialog("open");*/
				
	    	}
		}).navGrid('#OnOrderDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);	 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}

function loadOnHandDetailsGrid()
{	
	var prMasterID = $("#prMasterID").text();
	try {
		$("#OnHandDetailsGrid").jqGrid('GridUnload');
		 $('#OnHandDetailsGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				//pager: /*jQuery('#OnHandDetailsPager')*/"",
				url:'./inventoryList/getOnHandItemDetails',
				postData: {'prMasterID':prMasterID},
				colNames:['','On Hand','Allocated', 'Available','On Order',],
				colModel :[
						{name:'city', index:'city', align:'center', width:20, editable:true,hidden:false},
						{name:'inventoryOnHand', index:'inventoryOnHand', align:'center', width:20,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'inventoryAllocated', index:'inventoryAllocated', align:'center', width:20,hidden:false},						
						{name:'inventoryAvailable', index:'inventoryAvailable', align:'center', width:20,hidden:false},
						{name:'inventoryOnOrder', index:'inventoryOnOrder', align:'center', width:20,hidden:false}],
			rowNum: 50,
			pgbuttons: true,	
			recordtext: '',
			rowList: [50, 100, 200, 500, 1000],
			viewrecords: true,
			//pager: '#OnHandDetailsPager',
			sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: '',
			//autowidth: true,
			height:210,	width: 700,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
			loadComplete: function(data) {
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
				
			},
	    	ondblClickRow: function(rowid) {			
				
	    	}
		}).navGrid('#OnHandDetailsPager',//{cloneToTop:true},
			{add:false,edit:false,del:false,refresh:false,search:false}
		);	 
	}
	catch(err) {
        var text = "There was an error on this page.\n\n";
        text += "Error description: " + err.message + "\n\n";
        text += "Click OK to continue.\n\n";
        console.log(text);
    }
}
function formatCurrencynodollar(strValue)
{
	if(strValue === "" || strValue == null){
		return "0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + dblValue + '.' + strCents);
}

function convert2places(num) {
	num = String(num);
	if(num.indexOf('.') !== -1) {
	var numarr = num.split(".");
	if (numarr.length == 1) {
	return Number(num);
	}
	else {
	return Number(numarr[0]+"."+numarr[1].charAt(0)+numarr[1].charAt(1));
	}
	}
	else {
	return Number(num);
	}
	}

	

function customCurrencyFormatterWithoutDollar(cellValue, options, rowObject) {
	return cellValue;
}

