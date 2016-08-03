var estimatedamountRelease;
jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");	
	$("#SalesOrdersGrid").jqGrid({
		url:'./so_listgrid',
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#SalesOrdersGridPager'),
		colNames:['cuSoid','SO #','rxCustomerId', 'joReleaseId', 'Created On', 'costTotal'],
		colModel :[
           	{name:'cuSoid', index:'cuSoid', align:'left', width:40, editable:true,hidden:true},
			{name:'sonumber', index:'sonumber', align:'left', width:40,hidden:false, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
			{name:'rxCustomerId', index:'rxCustomerId', align:'', width:80,hidden:true},
			{name:'joReleaseId', index:'joReleaseId', align:'center', width:30,hidden:true},
			{name:'createdOn', index:'createdOn', align:'center',formatter: dateFormatter, width:50,hidden:false},			
			{name:'costTotal', index:'costTotal', align:'right', width:40,hidden:false,formatter:customCurrencyFormatter}
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#SalesOrdersGridPager',
		sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
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

			var rowData = jQuery(this).getRowData(rowid); 
			var acuSoid = rowData['cuSoid'];
			var aMasterID = rowData['rxCustomerId'];
			$('#rxCustomer_ID').text(aMasterID);
			$('#Cuso_ID').text(acuSoid);
			$('#operation').val('update');
			PreloadData(acuSoid);
			$('#salesrelease').dialog("open");
			
    	}
	}).navGrid('#SalesOrdersGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
});

function dateFormatter(cellValue, options, rowObject) {
	/**var aDate = cellValue;
	var aDateArray = aDate.split("-");//2012-05-04
	var newDate = "" + aDateArray[1] + "/" + aDateArray[2] + "/" + aDateArray[0];*/
	if(cellValue == null)
		cellValue = "";
	return cellValue;
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function addNewSO()
{
	
	$('#salesrelease').dialog("open");
	
	
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function alphabetSeq(cellValue, options, rowObject){
	try {
		 var element = "";
		 if(cellValue !== null){
			 var aPonumber=cellValue;
			 var aPONumSplitArray = new Array();
			 aPONumSplitArray = $.trim(aPonumber).split("-");
			 var aStr = aPONumSplitArray[1].match(/[A-z]/g);
			 for(var i=0;i<aStr.length;i++){
				 element = element + (aStr[i]);
			 }
		 }
		return element;
	} catch (e) {
		alert("Error:" + e + "\n\nFalse will be returned.");
		return false;
	}
}
	
	function billImage(cellValue, options, rowObject){
		var element = '';
	   if(cellValue !== '' && cellValue !== null){
		   element = "<img src='./../resources/Icons/dollar.png' style='vertical-align: middle;'>";
	   }else if(cellValue === ''){
		   element = "";
	   }else if(cellValue === null){
		   element = "";
	   }else{
		   element = "";
	   }
	return element;
	} 

	
	function typeFormatter(cellvalue, options, rowObject) {
		if(cellvalue === null){
			return "";
		}else if(cellvalue === -1){
			return "";
		}else if(cellvalue === 1) {
			return "Drop Ship";
		} else if(cellvalue === 2) {
			return "Stock Order";
		} else if(cellvalue === 3) {
			return "Bill Only";
		} else if(cellvalue === 4) {
			return "Commission";
		} else if(cellvalue === 5) {
			return "Service";
		}
	}
	
	function billAmountCal(){
		 $('#SalesOrdersGrid').jqGrid('getGridParam', 'userData');
		var allRowsInGrid = $('#SalesOrdersGrid').jqGrid('getRowData');
		var allocateamount=$("#allocate").text();
		var unAllocatedAmount = $("#unAllocated").text().replace(/[^0-9\.]+/g,"");
		var aVal = new Array();
		var sum = 0;
		$.each(allRowsInGrid, function(index, value) { 
			aVal[index] = value.estimatedBilling;
			sum = Number(sum) + Number(value.estimatedBilling.replace(/[^0-9\.]+/g,""));
		});
		/*if(typeof(estimatedamountRelease)!== undefined && estimatedamountRelease !== '' &&  estimatedamountRelease!== null){
			estimatedamountRelease=estimatedamountRelease.replace(/[^0-9\.]+/g,"");
			$("#estimated").text(formatCurrency(estimatedamountRelease));
			$('#allocate').empty();
			$("#allocate").text(formatCurrency(sum));
			$('#unAllocated').text(formatCurrency(estimatedamountRelease-sum));
		}*/
		var sumTotal = '';
		var allocatedamount=allocateamount.replace(/[^0-9\.]+/g,"");
		if(unAllocatedAmount > allocatedamount){
			sumTotal = Number(allocatedamount) - Number(unAllocatedAmount);
		}if(allocatedamount > unAllocatedAmount){
			sumTotal = Number(unAllocatedAmount) - Number(allocatedamount);
		}
//		$("#estimate").empty();
//		$("#estimate").text(formatCurrency(sumTotal));
	 }
	
	function viewPOPDF(){
		var cusoID = $('#Cuso_ID').text();
		if(cusoID != '' && cusoID != undefined)
			window.open("./salesOrderController/printSalesOrderReport?cusoID="+cusoID);
		
		return true;
	}
	
