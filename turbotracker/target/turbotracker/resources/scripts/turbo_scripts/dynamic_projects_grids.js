jQuery(document).ready(function(){
	var projectUserID = $("#projectUserID").val();	
	getSalesReps(projectUserID);
	//$("input:button").button();
	$("#intro").hide();
	$('#search').hide();
	setTimeout("loadgrid()", 1000);
});
		
var arrColNamesRelease = ["Booked Date","Job #","Job Name"];
var arrColModelRelease = [{name:"bookedDate", index:"bookedDate", align:"center", width:60},{name:'jobNo', index:'jobNo', align:'center', width:30},{name:"jobName", index:"jobName", align:"", width:90}];

var arrColNamesDormantProjects = [ "Job #", "Job Name", "Customer"];
var arrColModelDormantProjects = [{name:'jobNo', index:'jobNo', align:'center', width:30}, {name:'jobName', index:'jobName', align:'left', width:60}, {name:'customer', index:'customer', align:'left', width:60}];

var arrColNamesOrderTracking = ['Purchase Order #','Order Date', "Vendor"];
var arrColModelOrderTracking = [{name:'purchaseOrderNo', index:'purchaseOrderNo', align:'center', width:60},{name:'orderDate', index:'orderDate', align:'center', width:60}, {name:'vendor',index:'vendor',align:'left',width:90}];


	var colName_salesMan = "SalesMan";
	var colModel_salesMan = {name:"assignedSalesman", index:"assignedSalesman", align:"center", width:50 };

	var colName_Customer = "Customer";
	var colModel_Customer = {name:"customer", index:"customer", align:"center", width:50};	

	var colName_CustomerContact = "Customer Contact";
	var colModel_CustomerContactr = {name:"cuStomerContact", index:"cuStomerContact", align:"center", width:50};

	var colName_Engineer = "Engineer";
	var colModel_Engineer = {name:"engineer", index:"engineer", align:"center", width:50};

	var colName_ContractAmount = "Contract Amount";
	var colModel_ContractAmount = {name:"contractAmount", index:"contractAmount", align:"center", width:50, formatter:customCurrencyFormatter};

	

	
	/** =================== col names and col models, currently these as predefined templates ================== */
	
	var colName_CustContact = "Customer Contact";
	var colModel_CustContact = {name:'customerContact', index:'customerContact', align:'center', width:60};
	
	var colName_Salesman = "Salesman";
	var colModel_Salesman = {name:'salesMan',index:'salesman',align:'left',width:60};
	
	var colName_LastActivityDate = "Last Activity Date";
	var colModel_LastActivityDate = {name:'lastActivityDate',index:'lastActivityDate',align:'center',width:60};

	var colName_vendor = "Vendor";
	var colModel_vendor = {name:'vendor',index:'vendor',align:'left',width:90};
	
	var colName_vendorOrderNo = "Vendor Order #";
	var colModel_vendorOrderNo = {name:'vendorOrderNo', index:'vendorOrderNo', align:'left', width:60};
	
	var colName_shipDate = "Ship Date";
	var colModel_shipDate = {name:'shipDate', index:'shipDate', align:'left', width:60};
	
	
	function loadgrid(){
		pendingcreditordergrid(arrColNamesRelease, arrColModelRelease);
		pendingreleaseordergrid (arrColNamesRelease, arrColModelRelease);
		bookedreleasegrid(arrColNamesRelease, arrColModelRelease);
		dormantProjects_grid(arrColNamesDormantProjects, arrColModelDormantProjects);
		orderTracking_grid(arrColNamesOrderTracking, arrColModelOrderTracking);
		
		return true;
	}

	function addRemoveBookedInSubmittalGridColumns() {
		
		var boxAmount = document.getElementsByClassName("BookedInSubmittal");
		
		for(var i=0;i<boxAmount.length;i++) {
			
			var box = boxAmount[i];
			if(box.type === "checkbox" && box.checked) {
				if (box.value != "BookedDate" && box.value != "Job #" && box.value != "Job Name" &&!isExistInArray(arrColNamesRelease, box.value)){
					if (box.value === "SalesMan" && !isExistInArray(arrColNamesRelease, "SalesMan")) {
						arrColNamesRelease.push(colName_salesMan);
						arrColModelRelease.push(colModel_salesMan);
						
					} else if (box.value === "Customer" && !isExistInArray(arrColNamesRelease, "Customer")) {
						arrColNamesRelease.push(colName_Customer);
						arrColModelRelease.push(colModel_Customer);
					} else if (box.value === "Customer Contact" && !isExistInArray(arrColNamesRelease, "Customer Contact")) {
						arrColNamesRelease.push(colName_CustomerContact);
						arrColModelRelease.push(colModel_CustomerContactr);
					} else if (box.value === "Engineer" && !isExistInArray(arrColNamesRelease, "Engineer")) {
						arrColNamesRelease.push(colName_Engineer);
						arrColModelRelease.push(colModel_Engineer);
					} else if (box.value === "Contract Amount" && !isExistInArray(arrColNamesRelease, "Contract Amount")) {
						arrColNamesRelease.push(colName_ContractAmount);
						arrColModelRelease.push(colModel_ContractAmount);
					}
				}
			}
				else if (box.type === "checkbox" && !(box.checked)) {
					if (box.value === "SalesMan"){
						removeA(arrColNamesRelease,colName_salesMan);
						removeA(arrColModelRelease,colModel_salesMan);
				} else if (box.value === "Customer"){
						removeA(arrColNamesRelease,colName_Customer);
						removeA(arrColModelRelease,colModel_Customer);
				}
				 else if (box.value === "Customer Contact"){
						removeA(arrColNamesRelease,colName_CustomerContact);
						removeA(arrColModelRelease,colModel_CustomerContactr);
				}
				 else if (box.value === "Engineer"){
						removeA(arrColNamesRelease,colName_Engineer);
						removeA(arrColModelRelease,colModel_Engineer);
				}
				 else if (box.value === "Contract Amount"){
						removeA(arrColNamesRelease,colName_ContractAmount);
						removeA(arrColModelRelease,colModel_ContractAmount);
				}
			}

		}

		$("#Booked").jqGrid('GridUnload');
		bookedreleasegrid(arrColNamesRelease, arrColModelRelease);
		$("#Booked").trigger("reloadGrid");
		return true;
	
	}

var bookedreleasegrid = function (arrColNamesRelease, arrColModelRelease){
	var salesrepId = jQuery("#SalesRepComboList").val();
	if (salesrepId === null) {
		salesrepId = 75;
	} else {
		salesrepId = jQuery("#SalesRepComboList").val();
	}
	$("#Booked").jqGrid({
		
		url:'projectscontroller/projects_submittal',
		datatype: 'JSON',
		mtype: 'GET',
		postData: {'salesRepId':salesrepId},
		colNames : arrColNamesRelease,
		colModel : arrColModelRelease,
			
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
		sortname: ' ', sortorder: "asc",	imgpath: 'themes/basic/images',	
		height:100,	width: 1130, rownumbers: true,
		loadComplete: function(data) {
			
	    },
	    
		loadError : function (jqXHR, textStatus, errorThrown){
			
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
    	ondblClickRow: function(rowid) {
			var sel_id = $('#Booked').jqGrid('getGridParam', 'selrow');
			var aJobName = $('#Booked').jqGrid('getCell', sel_id, 3);
			var aJobNo = $('#Booked').jqGrid('getCell', sel_id, 2);
			var qryStr = getQryStr(aJobNo, aJobName);
			var queryString = "token=view&" + qryStr;
			document.location.href = "./jobflow?" + queryString;
		}
	});
};

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function addRemovePendingOrderGridColumns(){
	var boxAmount = document.getElementsByClassName("PendingOrderOptions");
	
	for(var i=0;i<boxAmount.length;i++) {
		
		var box = boxAmount[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "BookedDate" && box.value != "Job #" && box.value != "Job Name" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "SalesMan" && !isExistInArray(arrColNamesRelease, "SalesMan")) {
					arrColNamesRelease.push(colName_salesMan);
					arrColModelRelease.push(colModel_salesMan);
				} else if (box.value === "Customer"	&& !isExistInArray(arrColNamesRelease, "Customer")) {
					arrColNamesRelease.push(colName_Customer);
					arrColModelRelease.push(colModel_Customer);
				} else if (box.value === "Customer Contact"	&& !isExistInArray(arrColNamesRelease, "Customer Contact")) {
					arrColNamesRelease.push(colName_CustomerContact);
					arrColModelRelease.push(colModel_CustomerContactr);
				} else if (box.value === "Engineer"	&& !isExistInArray(arrColNamesRelease, "Engineer")) {
					arrColNamesRelease.push(colName_Engineer);
					arrColModelRelease.push(colModel_Engineer);
				} else if (box.value === "Contract Amount" && !isExistInArray(arrColNamesRelease, "Contract Amount")) {
					arrColNamesRelease.push(colName_ContractAmount);
					arrColModelRelease.push(colModel_ContractAmount);
				}
			}
		} else if (box.type === "checkbox" && !(box.checked)) {
			if (box.value === "SalesMan"){
					removeA(arrColNamesRelease,colName_salesMan);
					removeA(arrColModelRelease,colModel_salesMan);
			} else if (box.value === "Customer"){
					removeA(arrColNamesRelease,colName_Customer);
					removeA(arrColModelRelease,colModel_Customer);
			} else if (box.value === "Customer Contact"){
					removeA(arrColNamesRelease,colName_CustomerContact);
					removeA(arrColModelRelease,colModel_CustomerContactr);
			} else if (box.value === "Engineer"){
					removeA(arrColNamesRelease,colName_Engineer);
					removeA(arrColModelRelease,colModel_Engineer);
			} else if (box.value === "Contract Amount"){
					removeA(arrColNamesRelease,colName_ContractAmount);
					removeA(arrColModelRelease,colModel_ContractAmount);
			}
		}

	}

	$("#pendingOrderGrid").jqGrid('GridUnload');
	pendingreleaseordergrid(arrColNamesRelease, arrColModelRelease);
	$("#pendingOrderGrid").trigger("reloadGrid");
	return true;
}

var pendingreleaseordergrid = function (arrColNamesRelease, arrColModelRelease){
	var salesrepId = jQuery("#SalesRepComboList").val();
	if (salesrepId === null) {
		salesrepId = 75;
	} else {
		salesrepId = jQuery("#SalesRepComboList").val();
	}
	$("#pendingOrderGrid").jqGrid({
		url:'projectscontroller/projects_pendingOrder',
		datatype: 'JSON',
		mtype: 'GET',
		postData: {'salesRepId':salesrepId},
		colNames : arrColNamesRelease,
		colModel : arrColModelRelease,
			
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
		sortname: ' ', sortorder: "asc",	imgpath: 'themes/basic/images',	
		height:100,	width: 1130, rownumbers: true,
		loadComplete: function(data) {
			
	    },
	    
		loadError : function (jqXHR, textStatus, errorThrown){
			
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
    	ondblClickRow: function(rowid) {
			var sel_id = $('#pendingOrderGrid').jqGrid('getGridParam', 'selrow');
			var aJobName = $('#pendingOrderGrid').jqGrid('getCell', sel_id, 3);
			var aJobNo = $('#pendingOrderGrid').jqGrid('getCell', sel_id, 2);
			var qryStr = getQryStr(aJobNo, aJobName);
			var queryString = "token=view&" + qryStr;
			document.location.href = "./jobflow?" + queryString;
		}
	});
};


/*********************************************************** *********************************************************/

function addRemovePendingCreditGridColumns(){
	var boxAmount = document.getElementsByClassName("PendingCreditOptions");
	
	for(var i=0;i<boxAmount.length;i++) {
		
		var box = boxAmount[i];
		if(box.type === "checkbox" && box.checked) {
			if (box.value != "BookedDate" && box.value != "Job #" && box.value != "Job Name" &&!isExistInArray(arrColNamesRelease, box.value)){
				if (box.value === "SalesMan" && !isExistInArray(arrColNamesRelease, "SalesMan")) {
					arrColNamesRelease.push(colName_salesMan);
					arrColModelRelease.push(colModel_salesMan);
					
				}else if (box.value === "Customer" && !isExistInArray(arrColNamesRelease, "Customer")) {
					arrColNamesRelease.push(colName_Customer);
					arrColModelRelease.push(colModel_Customer);
				} else if (box.value === "Customer Contact" && !isExistInArray(arrColNamesRelease, "Customer Contact")) {
					arrColNamesRelease.push(colName_CustomerContact);
					arrColModelRelease.push(colModel_CustomerContactr);
				}else if (box.value === "Engineer" && !isExistInArray(arrColNamesRelease, "Engineer")) {
					arrColNamesRelease.push(colName_Engineer);
					arrColModelRelease.push(colModel_Engineer);
				} else if (box.value === "Contract Amount" && !isExistInArray(arrColNamesRelease, "Contract Amount")) {
					arrColNamesRelease.push(colName_ContractAmount);
					arrColModelRelease.push(colModel_ContractAmount);
				}
			}
		} else if (box.type === "checkbox" && !(box.checked)) {
			if (box.value === "SalesMan"){
					removeA(arrColNamesRelease,colName_salesMan);
					removeA(arrColModelRelease,colModel_salesMan);
			} else if (box.value === "Customer"){
					removeA(arrColNamesRelease,colName_Customer);
					removeA(arrColModelRelease,colModel_Customer);
			} else if (box.value === "Customer Contact"){
					removeA(arrColNamesRelease,colName_CustomerContact);
					removeA(arrColModelRelease,colModel_CustomerContactr);
			} else if (box.value === "Engineer"){
					removeA(arrColNamesRelease,colName_Engineer);
					removeA(arrColModelRelease,colModel_Engineer);
			} else if (box.value === "Contract Amount"){
					removeA(arrColNamesRelease,colName_ContractAmount);
					removeA(arrColModelRelease,colModel_ContractAmount);
			}
		}
	}

	$("#PendingCreditGrid").jqGrid('GridUnload');
	pendingcreditordergrid(arrColNamesRelease, arrColModelRelease);
	$("#PendingCreditGrid").trigger("reloadGrid");
	return true;
}

var pendingcreditordergrid = function (arrColNamesRelease, arrColModelRelease){
	var salesrepId = jQuery("#SalesRepComboList").val();
	if (salesrepId === null) {
		salesrepId = 68;
	} else {
		salesrepId = jQuery("#SalesRepComboList").val();
	}
	$("#PendingCreditGrid").jqGrid({
		url:'projectscontroller/projects_pendingCredit',
		datatype: 'JSON',
		mtype: 'GET',
		postData: {'salesRepId':salesrepId},
		//pager: jQuery('#releasepager'),
		colNames : arrColNamesRelease,
		colModel : arrColModelRelease,
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
		sortname: ' ', sortorder: "asc",	imgpath: 'themes/basic/images',	
		height:100,	width: 1130, rownumbers: true,
		loadComplete: function(data) {
			
	    },
	    
		loadError : function (jqXHR, textStatus, errorThrown){
			
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
		ondblClickRow: function(rowid) {
			var sel_id = $('#PendingCreditGrid').jqGrid('getGridParam', 'selrow');
			var aJobName = $('#PendingCreditGrid').jqGrid('getCell', sel_id, 3);
			var aJobNo = $('#PendingCreditGrid').jqGrid('getCell', sel_id, 2);
			var qryStr = getQryStr(aJobNo, aJobName);
			var queryString = "token=view&" + qryStr;
			document.location.href = "./jobflow?" + queryString;
		}
	
	});
};

/**
 * This method will be used to add or remove the columns dynamically based on check box options.
 **/
function addRemoveDormantProjectsGridColumns() {
	var boxes = document.getElementsByClassName("DormantProjects");
	for (var i=0;i<boxes.length;i++) {
		var box = boxes[i];
		if (box.type === "checkbox" && box.checked) {
			if (box.value != "Job #" && box.value != "Job Name" && box.value != "Customer" && !isExistInArray(arrColNamesDormantProjects, box.value)){
				if (box.value === "Customer Contact" && !isExistInArray(arrColNamesDormantProjects, "Customer Contact")) {
					arrColNamesDormantProjects.push(colName_CustContact);
					arrColModelDormantProjects.push(colModel_CustContact);
				} else if(box.value === "Salesman" && !isExistInArray(arrColNamesDormantProjects, "Salesman")){
					arrColNamesDormantProjects.push(colName_Salesman);
					arrColModelDormantProjects.push(colModel_Salesman);
				} else if(box.value ==="Last Activity Date" && !isExistInArray(arrColNamesDormantProjects,"Last Activity Date")){
					arrColNamesDormantProjects.push(colName_LastActivityDate);
					arrColModelDormantProjects.push(colModel_LastActivityDate);
				}
			}
		} else if (box.type === "checkbox" && !(box.checked)) {
			if (box.value === "Customer Contact"){
				removeA(arrColNamesDormantProjects,colName_CustContact);
				removeA(arrColModelDormantProjects,colModel_CustContact);
			}else if(box.value==="Salesman"){
				removeA(arrColNamesDormantProjects,colName_Salesman);
				removeA(arrColModelDormantProjects,colModel_Salesman);
			}else if(box.value==="Last Activity Date"){
				removeA(arrColNamesDormantProjects,colName_LastActivityDate);
				removeA(arrColModelDormantProjects,colModel_LastActivityDate);
			}
		}
	}
	$("#dormantProjectsGrid").jqGrid('GridUnload');
	dormantProjects_grid(arrColNamesDormantProjects, arrColModelDormantProjects);
	$("#dormantProjectsGrid").trigger("reloadGrid");
	return true;
}

var dormantProjects_grid = function (arrColNamesDormantProjects, arrColModelDormantProjects) {
	var salesrepId = jQuery("#SalesRepComboList").val();
	if (salesrepId === null) {
		salesrepId = 75;
	} else {
		salesrepId = jQuery("#SalesRepComboList").val();
	}
	
	$("#dormantProjectsGrid").jqGrid({
		url:'projectscontroller/projects_dormant',
		datatype: 'JSON',
		mtype: 'GET',
		postData: {'salesRepId':salesrepId},
		pager: '',
		colNames: arrColNamesDormantProjects,
		colModel: arrColModelDormantProjects,
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
		sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
		height:100,	width:1130, rownumbers: true,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown){
		    
		},
		ondblClickRow: function(rowid) {
			var sel_id = $('#dormantProjectsGrid').jqGrid('getGridParam', 'selrow');
			var aJobName = $('#dormantProjectsGrid').jqGrid('getCell', sel_id, 2);
			var aJobNo = $('#dormantProjectsGrid').jqGrid('getCell', sel_id, 1);
			var qryStr = getQryStr(aJobNo, aJobName);
			var queryString = "token=view&" + qryStr;
			document.location.href = "./jobflow?" + queryString;
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
	});
	
	return true;
};


function addRemoveOrderTrackingGridColumns() {
	var boxes = document.getElementsByClassName("orderTracking");
	for (var i=0;i<boxes.length;i++) {
		var box = boxes[i];
		if (box.type === "checkbox" && box.checked) {
			if (box.value != "Purchase Order #" && box.value != "Order Date" && box.value != "Vendor" && !isExistInArray(arrColNamesOrderTracking, box.value)){
				/*if (box.value === "Vendor" && !isExistInArray(arrColNamesOrderTracking, "Vendor")) {
					arrColNamesOrderTracking.push(colName_vendor);
					arrColModelOrderTracking.push(colModel_vendor);
				} else */if(box.value === "Vendor Order #" && !isExistInArray(arrColNamesOrderTracking, "Vendor Order #")){
					arrColNamesOrderTracking.push(colName_vendorOrderNo);
					arrColModelOrderTracking.push(colModel_vendorOrderNo);
				} else if(box.value ==="Ship Date" && !isExistInArray(arrColNamesOrderTracking,"Ship Date")){
					arrColNamesOrderTracking.push(colName_shipDate);
					arrColModelOrderTracking.push(colModel_shipDate);
				}
			}
		} else if (box.type === "checkbox" && !(box.checked)) {
			/*if (box.value === "Vendor"){
				removeA(arrColNamesOrderTracking,colName_vendor);
				removeA(arrColModelOrderTracking,colModel_vendor);
			}else */if(box.value==="Vendor Order #"){
				removeA(arrColNamesOrderTracking,colName_vendorOrderNo);
				removeA(arrColModelOrderTracking,colModel_vendorOrderNo);
			}else if(box.value==="Ship Date"){
				removeA(arrColNamesOrderTracking,colName_shipDate);
				removeA(arrColModelOrderTracking,colModel_shipDate);
			}
		}
	}
	$("#orderTrackingGrid").jqGrid('GridUnload');
	orderTracking_grid(arrColNamesOrderTracking, arrColModelOrderTracking);
	$("#orderTrackingGrid").trigger("reloadGrid");
	return true;
}

var orderTracking_grid = function (arrColNamesOrderTracking, arrColModelOrderTracking) {
	var salesrepId = jQuery("#SalesRepComboList").val();
	if (salesrepId === null) {
		salesrepId = 75;
	} else {
		salesrepId = jQuery("#SalesRepComboList").val();
	}
	$("#orderTrackingGrid").jqGrid({
		url:'projectscontroller/projects_ordertracking',
		mtype: 'GET',
		datatype: 'JSON',
		postData: {'salesRepId':salesrepId},
		pager: '',
		colNames: arrColNamesOrderTracking,
		colModel: arrColModelOrderTracking,
		rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
		sortname: 'employeeId',pgbuttons:false, sortorder: "asc",	imgpath: 'themes/basic/images',	caption: false,
		height:100,	width: 1130, rownumbers: true,
		loadComplete: function(data) {
			
	    },
		loadError : function (jqXHR, textStatus, errorThrown){
		    
		},
//		ondblClickRow: function(rowid) {
//			
//			document.location.href = "./jobflow";
//		},
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
	});
	return true;
};

function changeSalesrep() {
	var salesid = jQuery("#SalesRepComboList").val();
	$('#Booked').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	$('#Booked').trigger("reloadGrid");
	
	$('#pendingOrderGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	$('#pendingOrderGrid').trigger("reloadGrid");
	
	$('#PendingCreditGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	$('#PendingCreditGrid').trigger("reloadGrid");
	
	$('#dormantProjectsGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	$('#dormantProjectsGrid').trigger("reloadGrid");
	
	$('#orderTrackingGrid').jqGrid('setGridParam',{postData:{'salesRepId':salesid}});
	$('#orderTrackingGrid').trigger("reloadGrid");
	
	return true;
}

function getSalesReps(selecteduserId) {
	$.ajax({
		url: "./salescontroller",
		success: function(data){
			var aSalesRepOptions ='';
			$.each(data, function(key, value) { 
				aSalesRepOptions = aSalesRepOptions + '<option value="'+value.userId+'">'+value.userName+'</option>';
			});
			$("#SalesRepComboList").append(aSalesRepOptions);
			$("#SalesRepComboList option[value=" +selecteduserId + "]").attr("selected", true);
		},
		error: function(Xhr) {
			
		}
	});	 
}

function getQryStr(jobNumber, jobName) {
	var qryStr = "";
	$.ajax({
		async: false,
		url : "./search/searchjobcustomer",
		mType : "GET",
		data : {
			'jobnumber' : jobNumber,
			'jobname' : jobName
		},
		success : function(data) {
			var jobCustomer = "";
			var status = "";
			$.each(data, function(index, value) {
				status = value.jobStatus;
				jobCustomer = value.customerName;
			});
			if (jobCustomer === null) {
				jobCustomer = "";
			}
			updateJobstatus(jobNumber,status);
			qryStr = "jobNumber=" + jobNumber	+ "&jobName=" + jobName + "&jobCustomer=" + jobCustomer	+ "";
			return qryStr;
		},
		error : function(Xhr) {
		}
	});
	return qryStr;
}

function isExistInArray(array, value) {
	if(jQuery.inArray(value, array) === -1){
		return false;
	} else {
		return true;
	}
}

function removeA(arr){
    var what, a = arguments, L = a.length, ax;
    while(L> 1 && arr.length){
        what = a[--L];
        while((ax= arr.indexOf(what)) != -1){
            arr.splice(ax, 1);
        }
    }
    return arr;
}

function formatCurrency(strValue)
{
	if(strValue == "" || strValue == null){
		return "$0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10)
		strCents = "0" + strCents;
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++)
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}
