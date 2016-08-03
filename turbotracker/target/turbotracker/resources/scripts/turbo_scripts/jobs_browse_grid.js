jQuery(document).ready(function(){
	//$("input:text").addClass("ui-state-default ui-corner-all");
	$("#jobsGrid").jqGrid({
		url:'./job_controller',
		datatype: 'JSON',
		mtype: 'POST',
		pager: jQuery('#jobsGridPager'),
		colNames:['Job #','Project','City', 'Rep', 'Status', 'Customer', 'Cust PO #', 'Rep. #'],
		colModel :[
           	{name:'jobNumber', index:'jobNumber', align:'left', width:40, editable:true,hidden:false, edittype:'text',
           					editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
			{name:'description', index:'description', align:'left', width:140,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'locationCity', index:'locationCity', align:'', width:80,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'initials', index:'initials', align:'center', width:30,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'jobStatus', index:'jobStatus', align:'center', width:50,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'customerName', index:'customerName', align:'', width:140,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'customerPONumber', index:'customerPONumber', align:'center', width:80, hidden:false, editable:true, edittype:'textarea', 
							editoptions:{}, editrules:{edithidden:true,required:false}},
			{name:'code', index:'code', align:'center', width:40,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}}
		],
		rowNum: 50,
		pgbuttons: true,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		pager: '#jobsGridPager',
		sortname: 'jobNumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Jobs',
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var jobNumber = rowData['jobNumber'];
			var jobName = "" + rowData['description'];
			var jobCustomer = rowData['customerName'];
			var jobStatus = rowData['jobStatus'];
			jobName = jobName.replace(/&/g, "``");
			jobName = jobName.replace(/#/, "__");
			var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + jobName + "&jobCustomer=" + jobCustomer;
			document.location.href = "./jobflow?token=view&" + aQryStr;
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
	}).navGrid('#jobsGridPager',//{cloneToTop:true},
		{add:false,edit:false,del:false,refresh:true,search:false}
	);
});
		
function getCellValue(content) {
    var k1 = content.indexOf(' value=', 0);
    var k2 = content.indexOf(' name=', k1);
    var val = '';
    if (k1 > 0) {
        val = content.substr(k1 + 7, k2 - k1 - 6);
    }
    return val;
}

function addNewJob() {
	document.location.href = "./jobflow?token=new";
}


$(function() { var cache = {}, lastXhr;
$( "#searchJob" ).autocomplete({ minLength: 3,
	select: function (event, ui) {
			var name = ui.item.value;
			var value = name.replace("[","_");
			var job = value.split("_");
			var jobName = job[0];
			jobName = jobName.replace(/&/g, "``");
			jobName = jobName.replace(/#/, "__");
			var number = job[1];
			var jobNumber = number.replace("]"," ");
			$.ajax({
				url: "./search/searchjobcustomer",
				mType: "GET",
				data : {'jobnumber': jobNumber, 'jobname': jobName},
				success: function(data){
					$.each(data, function(index, value){
						status = value.jobStatus;
						jobCustomer = value.customerName;
					});
					if(jobCustomer === null) {
						jobCustomer = "";
					}
          		window.location.href = "./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+jobName+"&jobCustomer="+jobCustomer+"&jobStatus="+status+""; 
			},
			error: function(Xhr) {
			}
		});
      },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "search/searchjoblist", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	} }); });