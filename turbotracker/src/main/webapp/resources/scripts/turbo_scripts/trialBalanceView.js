var _global_periodEnding;
var _global_fromCoAccountid;
var _global_toCoAccountid;
var _global_showCurrentPeriod;

$(function(){
	
	$("#search").css({"display":"none"});
	$.urlParam = function(name){
	    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	    if (results==null){
	       return null;
	    }
	    else{
	       return results[1] || 0;
	    }
	}
	
	$("#perioddateEnding").html($.urlParam('periodEndingDate'));
	
	 _global_periodEnding = $.urlParam('periodEndingDate');
	 _global_fromCoAccountid =$.urlParam('fromCoAccountID');
	 _global_toCoAccountid = $.urlParam('toCoAccountID');
	 _global_showCurrentPeriod =  $.urlParam('showCurrentPeriod');
	 loadTrialBalanceGrid();
	
	 
//	 alert("1::"+_global_periodEnding+"||2::"+_global_fromCoAccountid+"||3::"+_global_toCoAccountid)
})

function loadTrialBalanceGrid(){
	$("#triaBalanceGrid").jqGrid({
		url:'./drillDown/trialBalanceGridView',
		postData: { periodEnding: _global_periodEnding, fromCoAccountid: _global_fromCoAccountid,toCoAccountid :_global_toCoAccountid},
		datatype: 'JSON',
		mtype: 'GET',
		pager: jQuery('#triaBalanceGridPager'),
		colNames:['AcctID','Acct.#','Account Description','Debit ','Credit','Debit','Credit'],
		colModel :[
		    {name:'coAccountID', index:'coAccountID', align:'left', width:120, editable:true,hidden:true},
           	{name:'coAccountNumber', index:'coAccountNumber', align:'left', width:120, editable:true,hidden:false},
        	{name:'coAccountDesc', index:'coAccountDesc', align:'', width:250,sorttype:'string',hidden:false},
        	{name:'pdebits', index:'pdebits', align:'right', width:100,sorttype:'string',hidden:false,formatter:customCurrencyFormatter},	
        	{name:'pcredits', index:'pcredits', align:'right', width:100,hidden:false,formatter:customCurrencyFormatter},    
        	{name:'ydebits', index:'ydebits',sorttype:'string', align:'right', width:100,hidden:false,formatter:customCurrencyFormatter},
        	{name:'ycredits', index:'ycredits', align:'right',sorttype:'integer', width:100,hidden:false,formatter:customCurrencyFormatter}
		],
		rowNum: 1000,	
		pgbuttons: false,	
		recordtext: '',
		sortname: 'coAccountID', sortorder: "desc",	imgpath: 'themes/basic/images',	
		caption: ' ',
		height:600,	width: 1050, rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		footerrow: true,
	    userDataOnFooter : true,
		loadComplete: function(data) {
			$(".ui-jqgrid-title").html("<span style='margin-left:600px;' >Current Period</span><span style='margin-left:150px' >Year to Date</span>");
			$("#trialBalanceGridPager").html("");
			$(".footrow").addClass("ui-jqgrid-titlebar ui-widget-header");
			$(".footrow").removeClass("footrow-ltr");
	    },
	    gridComplete:function(){
	    	
	    	 var grid = jQuery('#triaBalanceGrid');
	    	 
	    	 
	    	 var allRowsInGrid = $('#triaBalanceGrid').jqGrid('getRowData');
				var aPeriodcredit = new Array(); 
				var aPerioddebit = new Array();
				var aYearcredit = new Array(); 
				var aYeardebit = new Array();
				
				var sum = 0;
				var sum1 = 0;
				var sum2 = 0;
				var sum3 = 0;
				
				$.each(allRowsInGrid, function(index, value) {
					aPerioddebit[index] = value.pdebits;
					var number1 = aPerioddebit[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum = Number(sum) + Number(number6); 
				});
				
				$.each(allRowsInGrid, function(index, value) {
					aPeriodcredit[index] = value.pcredits;
					var number1 = aPeriodcredit[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum1 = Number(sum1) + Number(number6); 
				});
				
				
				$.each(allRowsInGrid, function(index, value) {
					aYeardebit[index] = value.ydebits;
					var number1 = aYeardebit[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum2 = Number(sum2) + Number(number6); 
				});
				
				$.each(allRowsInGrid, function(index, value) {
					aYearcredit[index] = value.ycredits;
					var number1 = aYearcredit[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					sum3 = Number(sum3) + Number(number6); 
				});
				
	         grid.jqGrid('footerData','set',{
	        	 coAccountDesc: 'Total',
	        	 pdebits: customCurrencyFormatter(sum),
	        	 pcredits:  customCurrencyFormatter(sum1),
	        	  ydebits:  customCurrencyFormatter(sum2),
	        	  ycredits:  customCurrencyFormatter(sum3)
	         });
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
	}).navGrid('#triaBalanceGridPager',
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
}


function trialBalancePrint()
{
	 
	window.open("./drillDown/printtrialBalance?periodEnding="+_global_periodEnding+"&fromCoAccountid="+_global_fromCoAccountid+"&toCoAccountid="+_global_toCoAccountid+"&showCurrentPeriod="+_global_showCurrentPeriod);

}
