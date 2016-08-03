jQuery(document).ready(function(){
	$("input:text").addClass("ui-state-default ui-corner-all");
	var jobNumber = getUrlVars()["jobNumber_name"];
	var city = getUrlVars()["city_name"];
	var project = getUrlVars()["project_code"];
	var rangeChk = getUrlVars()["bidDateName"];
	var rangepicker = getUrlVars()["rangepickerName"];
	var thrupicker = getUrlVars()["thruPickerName"];
	var budgetchk = getUrlVars()["budget_name"];
	var bidchk = getUrlVars()["bid_name"];
	var quotechk = getUrlVars()["quote_name"];
	var bookedchk = getUrlVars()["booked_name"];
	var closechk = getUrlVars()["closed_name"];
	var submittedchk = getUrlVars()["submitted_name"];
	var planningchk = getUrlVars()["planning_name"];
	var lostchk = getUrlVars()["lost_name"];
	var abondonedchk = getUrlVars()["abondoned_name"];
	var rejectchk = getUrlVars()["reject_name"];
	var overbudchk = getUrlVars()["overBudget_name"];
	var customerName = getUrlVars()["customer_name"];
	var architectName = getUrlVars()["architect_name"];
	var engineerName = getUrlVars()["engineer_name"];
	var gcName = getUrlVars()["gc_name"];
	var teamStatus = getUrlVars()["team_status_name"];
	var salesRep = getUrlVars()["salesrep_name"];
	var csr = getUrlVars()["csr_name"];
	var salesMgr = getUrlVars()["salesMgr_name"];
	var engineerEmp = getUrlVars()["engineerEmp_name"];
	var prjMgr = getUrlVars()["prjMgr_name"];
	var takeOff = getUrlVars()["takeOff_name"];
	var quoteBy = getUrlVars()["quoteBy_name"];
	var employeeAssign = getUrlVars()["employee_assignee_name"];
	var customerPo = getUrlVars()["customer_po_name"];
	var reportnum = getUrlVars()["report_name"];
	var division = getUrlVars()["division_name"];
	var sortBy = getUrlVars()["sort_by_name"];
	var aAdvancedSearchSeri = "jobNumber_name="+jobNumber+"&city_name="+city+"&project_code="+project+"&bidDateName="+rangeChk+"&rangepickerName="+rangepicker+"" +
		"&thruPickerName="+thrupicker+"&budget_name="+budgetchk+"&bid_name="+bidchk+"&quote_name="+quotechk+"&booked_name="+bookedchk+
		"&closed_name="+closechk+"&submitted_name="+submittedchk+"&planning_name="+planningchk+"&lost_name="+lostchk+"" +
		"&abondoned_name="+abondonedchk+"&reject_name="+rejectchk+"&overBudget_name="+overbudchk+"&customer_name="+customerName+
		"&architect_name="+architectName+"&engineer_name="+engineerName+"&gc_name="+gcName+"&team_status_name="+teamStatus+"&salesrep_name="+salesRep+"&csr_name="+
		csr+"&salesMgr_name="+salesMgr+"&engineerEmp_name="+engineerEmp+"&prjMgr_name="+prjMgr+"&takeOff_name="+takeOff+"&quoteBy_name="+quoteBy+
		"&employee_assignee_name="+employeeAssign+"&customer_po_name="+customerPo+"&report_name="+reportnum+"&division_name="+division+"&sort_by_name="+sortBy;
	loadAdvancedSearchList(aAdvancedSearchSeri);
});

function loadAdvancedSearchList(aAdvancedSearchSeri){
	$("#searchResultsGrid").jqGrid({
		url:'./jobtabs2/getAdvacedSearchJobList',
		datatype: 'JSON',
		mtype: 'POST',
		postData: aAdvancedSearchSeri,
		pager: jQuery('#advancedSearchGridpager'),
		colNames:['Job #','BidDate','Project', 'Salesman', 'Take Off', 'Quote By', 'City', 'Rep', 'Status', 'Customer', 'Cust PO #', 'Division','Rep. #', 'Bid List'],
		colModel :[
           	{name:'jobNumber', index:'jobNumber', align:'left', width:40, editable:true,hidden:true, edittype:'text',
           					editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
           	{name:'bidDate', index:'bidDate', align:'left', width:65, editable:true,hidden:false, edittype:'text',
	           					editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
			{name:'description', index:'description', align:'left', width:140,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'initials', index:'initials', align:'center', width:40,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'takeoff', index:'takeoff', align:'center', width:40,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'quoteBy', index:'quoteBy', align:'center', width:40,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'locationCity', index:'locationCity', align:'', width:80,hidden:true, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'initials', index:'initials', align:'center', width:80,hidden:true, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'jobStatus', index:'jobStatus', align:'center', width:65,hidden:false, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'customerName', index:'customerName', align:'', width:140,hidden:true, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'customerPONumber', index:'customerPONumber', align:'center', width:80, hidden:true, editable:true, edittype:'textarea', 
							editoptions:{}, editrules:{edithidden:true,required:false}},
			{name:'division', index:'division', align:'center', width:80, hidden:true, editable:true, edittype:'textarea', 
							editoptions:{}, editrules:{edithidden:true,required:false}},
			{name:'bidListCount', index:'bidListCount', align:'center', width:180,hidden:true, editable:true,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}},
			{name:'bidList', index:'bidList', align:'center', width:120,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, formatter: showBidderName,
							editoptions:{size:20,readonly:false, alignText:'right'},editrules:{edithidden:true,required:true}}
		],
		rowNum: 5000,
		pgbuttons: false,	
		recordtext: '',
		rowList: [50, 100, 200, 500, 1000],
		viewrecords: true,
		//pager: '#advancedSearchGridpager',
		sortname: 'jobNumber', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Resulted Jobs',
		//autowidth: true,
		height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
		loadComplete: function(data) {
			/*$(".ui-pg-input").css("display", "none");
			$("#sp_1_advancedSearchGridpager").css("display", "none");*/
	    },
	    jsonReader : {
	    	root: "rows",
            //page: "page",
            //total: "total",
            //records: "records",
            repeatitems: false,
            cell: "cell",
            id: "id",
            userdata: "userdata"
    	},
		loadError : function (jqXHR, textStatus, errorThrown) {
		    
		},
		ondblClickRow: function(rowId) {
			var rowData = jQuery(this).getRowData(rowId); 
			var jobNumber = rowData['jobNumber'];
			var jobName = "" + rowData['description'];
			//var jobCustomer = rowData['customerName'];
			var jobStatus = rowData['jobStatus'];
			if(jobStatus != "Booked" && jobStatus != "Closed" && jobStatus != "Submitted"){
				$.ajax({
					url: "./job_controller/jobStatusHome", 
					mType: "GET", 
					data : { 'jobNumber' : jobNumber },
					success: function(data){ 
						jobName = jobName.replace(/&/g, "``");
						jobName = jobName.replace(/#/, "__");
						var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + jobName + "&jobStatus=" + jobStatus;
						document.location.href = "./jobflow?token=view&" + aQryStr;
					}
			 	});
			}
			else{
				jobName = jobName.replace(/&/g, "``");
				jobName = jobName.replace(/#/, "__");
				var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + jobName + "&jobStatus=" + jobStatus;
				document.location.href = "./jobflow?token=view&" + aQryStr;
			}
		},
	}).navGrid('#advancedSearchGridpager',
			{add:false,edit:false,del:false,refresh:false,search:false});
	return true;
}

function showBidderName(cellValue, options, rowObject){
	var aBidListCounr = rowObject["bidListCount"];
	if(aBidListCounr !== 1){
		cellValue = "Multiple";
	}else if(aBidListCounr === ''){
		return cellValue;
	}else{
		return cellValue;
	}
	return cellValue;
}

function ChangeTeamDropDown(value) {
	if (value == "1") {
		document.getElementById('customerId').style.display = 'block';
		document.getElementById('architectId').style.display = 'none';
		document.getElementById('engineerId').style.display = 'none';
		document.getElementById('gcId').style.display = 'none';
	} else if (value == "2") {
		document.getElementById('customerId').style.display = 'none';
		document.getElementById('architectId').style.display = 'block';
		document.getElementById('engineerId').style.display = 'none';
		document.getElementById('gcId').style.display = 'none';
	} else if (value == "3") {
		document.getElementById('customerId').style.display = 'none';
		document.getElementById('architectId').style.display = 'none';
		document.getElementById('engineerId').style.display = 'block';
		document.getElementById('gcId').style.display = 'none';
	} else if (value == "4") {
		document.getElementById('customerId').style.display = 'none';
		document.getElementById('architectId').style.display = 'none';
		document.getElementById('engineerId').style.display = 'none';
		document.getElementById('gcId').style.display = 'block';
	}
	return false;
}

function ChangeEmployeeDropDown(value) {
	if (value == "1") {
		document.getElementById('salesRepId').style.display = 'block';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "2") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'block';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "3") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'block';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "4") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'block';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "5") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'block';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "6") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'block';
		document.getElementById('quoteById').style.display = 'none';
	} else if (value == "7") {
		document.getElementById('salesRepId').style.display = 'none';
		document.getElementById('csrId').style.display = 'none';
		document.getElementById('salesMgrId').style.display = 'none';
		document.getElementById('engineerEmpId').style.display = 'none';
		document.getElementById('prjMgrId').style.display = 'none';
		document.getElementById('takeOffId').style.display = 'none';
		document.getElementById('quoteById').style.display = 'block';
	}
	return false;
}

$(function() { var cache = {}; var lastXhr='';
$( "#salesRepId" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesRepTextId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#csrId" ).autocomplete({ minLength: 2, timeout :1000,
		select: function( event, ui ) {var id = ui.item.id; $("#csrTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#salesMgrId" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) {var id = ui.item.id; $("#salesMgrTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#engineerEmpId" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#engineerEmpTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#prjMgrId" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#prjMgrTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#takeOffId" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) {var id = ui.item.id; $("#takeOffTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#quoteById" ).autocomplete({ minLength: 2, timeout :1000,
		select: function( event, ui ) {var id = ui.item.id; $("#quoteByTextId").val(id);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

 $(function() { var cache = {}; var lastXhr='';
	$( "#customerId" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#customerTextId").val(id); },
		source: function( request, response ) { var term = request.term;
			if( term in cache ){ response( cache[ term ] ); return; }
			lastXhr = $.getJSON("customerList/customerNameList",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
});

  $(function() { var cache = {}; var lastXhr='';
	$( "#engineerId" ).autocomplete({ minLength: 2,timeout :1000, clearcache : true,
		open: function(){ 
		},
		select: function( event, ui ) { var id = ui.item.id; $("#engineersRXId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "companycontroller/engineerRxList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} 
	});
 });

$(function() { var cache = {}; var lastXhr='';
	$( "#architectId" ).autocomplete({ minLength: 2,timeout :1000,
		open: function(){ 
		},
		select: function( event, ui ) { var id = ui.item.id; $("#architectRXId").val(id); },
		source: function( request, response ) {var term = request.term;
			if (term in cache ) { response( cache[ term ] );return; }
			lastXhr = $.getJSON( "companycontroller/architectRxList", request, function(data, status, xhr) {cache[term] = data;if(xhr === lastXhr){response( data );}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}
	});
});

$(function() { var cache = {}; var lastXhr='';
	$( "#gcId" ).autocomplete({ minLength: 2,timeout :1000,
		open: function(){ 
		},
		select: function(event, ui){var id = ui.item.id; $("#gcRXId").val(id);},
		source: function(request, response) {var term = request.term;
			if ( term in cache ) { response( cache[ term ] );return;}
			lastXhr = $.getJSON("companycontroller/GCRXList", request, function( data, status, xhr ) { cache[ term ] = data; if(xhr === lastXhr){response(data);}});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}
	}); 
});