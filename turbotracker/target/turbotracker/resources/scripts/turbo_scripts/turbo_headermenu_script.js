var aGlobalVariable;
$(function(){
    $("#click").click(function(){
        $(this).hide();
        return false;
    });
    var selectTab = getUrlVars()["quickQuote"];
    if(selectTab === 'product'){
    	$("#tabs_main_job").tabs("select", 2);
	 }
});

var aLoginID = $("#userLogin_Id").val();
var aUserLoginID;
$.ajax({
	url:'./userlistcontroller/getUserDetails',
	type: "POST",
	data : { 'userLoginID' :  aLoginID },
	success: function(data){
			$("#userLoginID").val(data.systemAdministrator);
		}
	});

$.ajax({
	url:'./userlistcontroller/getSysPrivilagePermissions',
	type: "GET",
	success: function(data){
		$.each(data, function(index, value){
			if(value.accessProcedureId == 1001000){
				$("#homeAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1401000){
				$("#jobQuoteAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1402000){
				$("#jobSubmittalAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1403000){
				$("#jobCreditAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1404000){
				$("#jobReleaseAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1405000){
				$("#jobFinancialAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1406000){
				$("#jobJournalAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1101000){
				$("#salesAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1501000){
				$("#companyCustomerAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1501100){
				$("#companyPaymentAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1502000){
				$("#companyVendorAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1502100){
				$("#companyPayBillAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1503000){
				$("#companyEmployeeAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1503100){
				$("#companyCommisionsAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1504000){
				$("#companyRolodexAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1505000){
				$("#companyUserAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1506000){
				$("#companySettingAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1507000){
				$("#companyReportsAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1508000){
				$("#companyAccountAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1201000){
				$("#projectAccesProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1301000){
				$("#inventoryProcedureID1").val(value.accessProcedureId);
			}if(value.accessProcedureId == 1601000){
				$("#bankingProcedureID1").val(value.accessProcedureId);
			}if(value.ledgerID == 1701000){
				$("#ledgerID1").val(value.accessProcedureId);
			}
		});
	}
});

function searchJOB(){
	var jobNumber = $("#jobNumberID").val();
	var city = $("#citynameID").val();
	var project = $("#projectID").val();
	var rangeChk = $("#bidDateCheckbox").is(':checked'); 
	var rangepicker = $("#rangepickerID").val();
	var thrupicker = $("#thruPickerID").val();
	var budgetchk = $("#budgetnameID").is(':checked'); 
	var bidchk = $("#bidnameID").is(':checked'); 
	var quotechk = $("#quotenameID").is(':checked'); 
	var bookedchk = $("#bookednameID").is(':checked'); 
	var closechk = $("#closednameID").is(':checked'); 
	var submittedchk = $("#submittednameID").is(':checked'); 
	var planningchk = $("#planningnameID").is(':checked'); 
	var lostchk = $("#lostnameID").is(':checked'); 
	var abondonedchk = $("#abondonednameID").is(':checked'); 
	var rejectchk = $("#rejectnameID").is(':checked'); 
	var overbudchk = $("#overBudgetnameID").is(':checked');
	var customerName = $("#customerId").val();
	var architectName = $("#architectRXId").val();
	var engineerName = $("#engineersRXId").val();
	var gcName = $("#gcRXId").val();
	var teamStatus = $("#teamStatusId").val();
	var salesRep = $("#salesRepTextId").val();
	var csr = $("#csrTextId").val();
	var salesMgr =$("#salesMgrTextId").val();
	var engineerEmp =$("#engineerEmpTextId").val();
	var prjMgr =$("#prjMgrTextId").val();
	var takeOff =$("#takeOffTextId").val();
	var quoteBy =$("#quoteByTextId").val();
	var employeeAssign = $("#employeeAssigneeId").val();
	var customerPo = $("#customerPoNameID").val();
	var reportnum = $("#reportnameID").val();
	var division = $("#divisionID").val();
	var sortBy = $("#sortbyId").val();
	
	var aAdvancedSearchSeri = "jobNumber_name="+jobNumber+"&city_name="+city+"&project_code="+project+"&bidDateName="+rangeChk+"&rangepickerName="+rangepicker+"" +
								"&thruPickerName="+thrupicker+"&budget_name="+budgetchk+"&bid_name="+bidchk+"&quote_name="+quotechk+"&booked_name="+bookedchk+
								"&closed_name="+closechk+"&submitted_name="+submittedchk+"&planning_name="+planningchk+"&lost_name="+lostchk+"" +
								"&abondoned_name="+abondonedchk+"&reject_name="+rejectchk+"&overBudget_name="+overbudchk+"&customer_name="+customerName+
								"&architect_name="+architectName+"&engineer_name="+engineerName+"&gc_name="+gcName+"&team_status_name="+teamStatus+"&salesrep_name="+salesRep+"&csr_name="+
								csr+"&salesMgr_name="+salesMgr+"&engineerEmp_name="+engineerEmp+"&prjMgr_name="+prjMgr+"&takeOff_name="+takeOff+"&quoteBy_name="+quoteBy+
								"&employee_assignee_name="+employeeAssign+"&customer_po_name="+customerPo+"&report_name="+reportnum+"&division_name="+division+"&sort_by_name="+sortBy;
	
	$.ajax({
		url:'./jobtabs2/getAdvacedSearchChkJobList',
		type: "POST",
		data : aAdvancedSearchSeri,
		success: function(data){
			var countJob = data.length; 
			if(countJob === 1){
				$.each(data, function(index, value){
					var jobNumber = value.jobNumber;
					var jobStatus = value.jobStatus;
					var descriptionval = value.description;
					document.location.href="./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+descriptionval+"&jobStatus="+jobStatus+"";
					return true;
				});
			}else{
				window.location.href = "./results?jobNumber_name="+jobNumber+"&city_name="+city+"&project_code="+project+"&bidDateName="+rangeChk+"&rangepickerName="+rangepicker+"" +
				"&thruPickerName="+thrupicker+"&budget_name="+budgetchk+"&bid_name="+bidchk+"&quote_name="+quotechk+"&booked_name="+bookedchk+
				"&closed_name="+closechk+"&submitted_name="+submittedchk+"&planning_name="+planningchk+"&lost_name="+lostchk+"" +
				"&abondoned_name="+abondonedchk+"&reject_name="+rejectchk+"&overBudget_name="+overbudchk+"&customer_name="+customerName+
				"&architect_name="+architectName+"&engineer_name="+engineerName+"&gc_name="+gcName+"&team_status_name="+teamStatus+"&salesrep_name="+salesRep+"&csr_name="+
				csr+"&salesMgr_name="+salesMgr+"&engineerEmp_name="+engineerEmp+"&prjMgr_name="+prjMgr+"&takeOff_name="+takeOff+"&quoteBy_name="+quoteBy+
				"&employee_assignee_name="+employeeAssign+"&customer_po_name="+customerPo+"&report_name="+reportnum+"&division_name="+division+"&sort_by_name="+sortBy;
			}
		}
	});
}
		
function previousJob(){
		aGlobalVariable="previous";
		var jobNumber=getUrlVars()["jobNumber"];
		var jobNo= jobNumber.split("%");
		$.ajax({
			url: 'job_controller/jobPreviousandNext',
			mtype : 'GET',
    	    data: "&jobNumber="+jobNo[0]+ "&term=" +aGlobalVariable,
			success: function (data) {
				$.each(data, function(index, value){
					joMasterid = value.joMasterId;
					jobNumber1 = value.jobNumber;
					jobName = value.description;
					status = value.jobStatus;
					jobCustomer = value.customerName;
				});
				if(jobCustomer === null) {
					jobCustomer = "";
				}
      			window.location.href = "./jobflow?token=view&jobNumber="+jobNumber1+"&jobName="+jobName+"&jobCustomer="+jobCustomer+"&jobStatus="+status+"";
		}
	});
}

function nextJob(){
	aGlobalVariable="next";
	var jobNumber=getUrlVars()["jobNumber"];
	var jobNo= jobNumber.split("%");
	$.ajax({
		url: 'job_controller/jobPreviousandNext',
		mtype : 'GET',
	    data: "&jobNumber="+jobNo[0]+ "&term=" +aGlobalVariable,
		success: function (data) {
			$.each(data, function(index, value){
					joMasterid = value.joMasterId;
					jobNumber1 = value.jobNumber;
					jobName = value.description;
					status = value.jobStatus;
					jobCustomer = value.customerName;
				});
      			if(jobCustomer === null) {
					jobCustomer = "";
				}
  			window.location.href = "./jobflow?token=view&jobNumber="+jobNumber1+"&jobName="+jobName+"&jobCustomer="+jobCustomer+"&jobStatus="+status+""; 
		}
	});
}

function cancelJOB(){
	jQuery("#searchAdvJob").dialog( "close" );
}

jQuery(function () {
		$(".datepicker").datepicker();
		jQuery( "#searchAdvJob" ).dialog({
			autoOpen: false,
			width: 670,
			title:"Advanced Search",
			modal: true,
			buttons:{		},
			close: function () {
				return true;
			}
		});
		return true;
	});

function getSearchDetails(){
	var searchGo = $('#searchJob').val();
	if(searchGo === ""){
		return false;
	}
	$.ajax({
		url: "./search/searchrolodex",
		mType: "GET",
		data : {'rolodex': $('#searchJob').val()},
		success: function(data){
			 var rxId="";
			$.each(data, function(index, value){
				entityValue = value.entity;
				rxId =value.pk_fields; 
			});
			var value = $('#searchJob').val().split(":");
			var entity = value[0];
			var text = value[1];
			var text1 = text.split(",");
			var searchText = text1[0];
			var searchlist = "";
			if(entity == "EMP")	{
				searchlist = entity.replace("EMP","employeedetails");
			}if(entity == "VEND") {
				searchlist = entity.replace("VEND","vendordetails");
			}if(entity == "CUST") {
				searchlist = entity.replace("CUST","customerdetails");
			}if(entity == "ARCH") {
				searchlist = entity.replace("ARCH","architectDetails");
			}if(entity == "ENGR") {
				searchlist = entity.replace("ENGR","engineerDetails");
			}if(entity == "ARCH/ENGR"){
				searchlist = entity.replace("ARCH/ENGR","architectDetails");
			}if(entity == "G.C") {
				searchlist = entity.replace("G.C","rolodexdetails");
			}
			document.location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+searchText+"";
		},
		error: function(Xhr) {
			var errorText = $(Xhr.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span style="color:red;"><b>'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		}
	});
	return true;
}
	
function openAdvancedSearchPopup() {
	jQuery( "#searchAdvJob" ).dialog( "open" );
}

function search_TeamChange() {
	var teamSelectTxt = "<select onchange='' id='teamSelectTag_options'><option>Autry State Prision</option><option>Aux Mechanical Inc.</option><option>Avent Construction</option><option>Axis Mechanical</option><option>B&B Contracting</option><option>B. Hipp Services</option><option>Bache Services</option></select>";
	jQuery("#teamSelectLists").find("select").remove();
	
	jQuery("#teamSelectLists").append(teamSelectTxt);
}

function search_EmployeeChange() {
	var assignedemployeeSelectTxt = "<select onchange='' id='teamSelectTag_options'><option>Adam Deener</option><option>Alex Skandalakis</option><option>Amanda Roeubuck</option><option>Andrew T. Holden</option><option>Ashley Dean</option><option>BenHayes</option><option>Bret Pettit</option><option>Brayan Colvin</option></select>";
	jQuery("#assignedemployeeSelectLists").find("select").remove();
	jQuery("#assignedemployeeSelectLists").append(assignedemployeeSelectTxt);
}
function bidDateChange() {
	var addDateRangeFields = "<input type='text' class='datepicker' id='rangepickerID' name='rangepickerName' style='width:80px;'><label>  &nbsp;Thru  </label><input type='text' style='width:80px;' class='datepicker' id='thruPickerID' name='thruPickerName'>";
	if (document.getElementById('bidDateCheckbox').checked) {
		$('#addDateRange').append(addDateRangeFields);
		$(".datepicker").datepicker();
	} else {
		$('#addDateRange').empty();
	}
}

function getUrlVars(){
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++) { 
	    hash = hashes[i].split('=');
	    vars.push(hash[0]);
	    vars[hash[0]] = hash[1];
	}
    return vars;
}


function updateJobstatus(jobNumber,status){
	if(status !== "Booked" && status !== "Closed" && status !== "Submitted"){
		$.ajax({
			url: "./job_controller/jobStatusHome", 
			mType: "GET", 
			data : { 'jobNumber' : jobNumber },
			success: function(data){ 
			}
	 	});
	}
}
	
jQuery(function () {
	jQuery( "#bidDateReport" ).dialog({
		autoOpen: false,
		width: 350,
		height: 150,
		title:"Bid Date Report",
		modal: true,
		buttons:{		},
		close: function () {
			return true;
		}
	});
	return true;
});

function reportCriteriaPage(){
	$( "#bidDateReport" ).dialog("open");
	  return true;
}

function generationReport(){
	document.location.href ="./bidDateReport";
	return false;
}

function cancelReport(){
	$( "#bidDateReport" ).dialog("close");
	return false;
}
	
function homePage(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#homeAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}
	}else{*/
		document.location.href = "./home";
//	}
	return true;
}

function salesPage(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#salesAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}
	}else{*/
		document.location.href = "./sales";
//	}
	return true;
}

function projectPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#projectAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./projects";
		}
	}else{
		document.location.href = "./projects";
	}
	return true;
}

function inventoryPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#inventoryProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./inventory";
		}
	}else{
		document.location.href = "./inventory";
	}
	return true;
}

function bankingPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#bankingProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./bankingAccounts";
		}
	}else{
		document.location.href = "./bankingAccounts";
	}
	return true;
}

function customerPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyCustomerAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./customers";
		}
	}else{
		document.location.href = "./customers";
	}
	return true;
}

function paymentsPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyPaymentAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./customerpaymentlist";
		}
	}else{
		document.location.href = "./customerpaymentlist";
	}
	return true;
}
function paymentTermsPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyPaymentAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./paymentterms";
		}
	}else{
		document.location.href = "./paymentterms";
	}
	return true;
}

function vendorPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyVendorAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./vendors";
		}
	}else{
		document.location.href = "./vendors";
	}
	return true;
}

function purchaseOrderList(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyPayBillAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./po_list";
		}
	}else{
		document.location.href = "./po_list";
	}
	return true;
}

function payBillPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyPayBillAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./vendorbills";
		}
	}else{
		document.location.href = "./vendorbills";
	}
	return true;
}

function employeePage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyEmployeeAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./employeelist";
		}
	}else{
		document.location.href = "./employeelist";
	}
	return true;
}

function commissionsPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyCommisionsAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./employeeCommissions";
		}
	}else{
		document.location.href = "./employeeCommissions";
	}
	return true;
}

function rolodexPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyRolodexAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./rolodexList";
		}
	}else{
		document.location.href = "./rolodexList";
	}
	return true;
}

function chartsPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyUserAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./chartofaccounts";
		}
	}else{
		document.location.href = "./chartofaccounts";
	}
	return true;
}

function userListPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		var aInfo = true;
		if(aInfo){
			var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}else{
			document.location.href ="./userlist";
		}
	}else{
		document.location.href ="./userlist";
	}
	return true;
}

function companySettingPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		var aInfo = true;
		if(aInfo){
			var info = "You are not authorized to enter this area.  Please contact your System Administrator.";
			var DialogBox = jQuery(document.createElement('div'));
			jQuery(DialogBox).html('<span><b style="color:green;">'+info+'</b></span>');
			jQuery(DialogBox).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}else{
			document.location.href ="./settings";
		}
	}else{
		document.location.href ="./settings";
	}
	return true;
}

function journalEntriesPage(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
			var info = "You are not authorized to enter this area.  Please contact your System Administrator.";
			var DialogBox = jQuery(document.createElement('div'));
			jQuery(DialogBox).html('<span><b style="color:green;">'+info+'</b></span>');
			jQuery(DialogBox).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
	}else{*/
		document.location.href ="./companyjournals";
	/*}*/
	return true;
}
function accountCycles(){
	document.location.href ="./accountingcycles";
}
function shipviaPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		else{
			document.location.href = "./shipViaDetail";
		}
	}else{
		document.location.href = "./shipViaDetail";
	}
	return true;
}

function ledgerPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#ledgerID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href ="./ledger";
		}
	}else{
		document.location.href ="./ledger";
	}
	return true;
}

function writeChecksPage(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#ledgerID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
		}else{
			document.location.href ="./ledger";
		}
	}else{*/
		document.location.href ="./writechecks";
	/*}*/
	return true;
}

function vendorInvoicesPage(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#ledgerID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
		}else{
			document.location.href ="./ledger";
		}
	}else{*/
		document.location.href ="./vendorinvoicelist";
	/*}*/
	return true;
}

function inventoryCategories(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#ledgerID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
		}else{
			document.location.href ="./ledger";
		}
	}else{*/
		document.location.href ="./inventorycategories";
	/*}*/
	return true;
}
function inventoryDepartments(){
	/*aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#ledgerID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
		}else{
			document.location.href ="./ledger";
		}
	}else{*/
		document.location.href ="./inventorydepartments";
	/*}*/
	return true;
}
function inventoryWarehouses(){
	document.location.href ="./warehouse";
}
function statementsPage(){
	document.location.href ="./statements";
}

function salesorderPage()
{	
	document.location.href ="./salesorder?oper=create";
}
/*function transactionDetails(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#bankingProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var info = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var DialogBox = jQuery(document.createElement('div'));
				jQuery(DialogBox).html('<span><b style="color:green;">'+info+'</b></span>');
				jQuery(DialogBox).dialog({modal: true, width:340, height:170, title:"Information", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}
	}else{
		$( "#transactionDetails" ).dialog("open");
	}
	return true;
}*/
function OpenJobsCriteriaPage(){
	document.location.href ="./openjobreport";
}

function bookingsCriteriaPage(){
	document.location.href ="./bookreport";
}

function openPOCriteriaPage(){
	window.open('./job_controller/previewreport?reportName=openpo',target="_blank");
}
/**  *  Format phone numbers */ 
function formatPhone(phonenum) {
	if(contains(phonenum, "Ext")) {
		var phNoArray = new Array();
		phNoArray = phonenum.split("Ext");
		phonenum = $.trim(phNoArray[0]);
	}
	var regexObj = /^(?:\+?1[-. ]?)?(?:\(?([0-9]{3})\)?[-. ]?)?([0-9]{3})[-. ]?([0-9]{4})$/;    
	if (regexObj.test(phonenum)) {
         var parts = phonenum.match(regexObj);
         var phone = "";
         if (parts[1]) { phone += "(" + parts[1] + ") "; }
        phone += parts[2] + "-" + parts[3];
        return phone;
	} else {
        //invalid phone number
         return phonenum;
	}
}

function contains(str, text) {
	return str.indexOf(text) >= 0;
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

function autoFocus(t){
	if(t.value.length == 3){
		$("#exchangeCodeDirID").focus();					
	}
}

function autoFocusNext(t){
	if(t.value.length == 3){
		$("#subscriberNumberDirID").focus();
	}					
}

function autoFocusCell(t){
	if(t.value.length == 3){
		$("#exchangeCode1").focus();
	}	
}

function autoFocusCellNext(t){
	if(t.value.length == 3){
		$("#subscriberNumber1").focus();
	}	
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	return formatCurrency(cellValue);
}

function formatCurrency(strValue)
{
	if(strValue === "" || strValue == null){
		return "$0.00";
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
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}
function divisionsPage(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyUserAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./divisionsList";
		}
	}else{
		document.location.href = "./divisionsList";
	}
	return true;
}

function taxTerritories(){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyUserAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = "./taxTerritories";
		}
	}else{
		document.location.href = "./taxTerritories";
	}
	return true;
}

function getPage(page){
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
		if($("#companyUserAccesProcedureID1").val() === ''){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
		}else{
			document.location.href = page;
		}
	}else{
		document.location.href = page;
	}
	return true;
}

/*function cancelTransactionDetails(){
	$( "#transactionDetails" ).dialog("close");
	return false;
}

jQuery(function () {
	jQuery( "#transactionDetails" ).dialog({
		autoOpen: false,
		width: 400,
		title:"View Transaction Details",
		modal: true,
		buttons:{		},
		close: function () {
			return true;
		}
	});
	return true;
});

function viewTransactionDetails(){
	var aDeposit = $("#transactionDepositID").is(':checked');
	var aWithDraw = $("#transactionWidthdrawID").is(':checked');
	var aCheck = $("#transactionCheckID").is(':checked');
	var aIntrest = $("#transactionInterstID").is(':checked');
	var aFee = $("#transactionFeeID").is(':checked');
	var aBankAccount = $("#bankAccountID").val();
	var aFromDate = $("#fromDateRangeID").val();
	var aToDate = $("#toDateRangeID").val();
	var aDepositChk = '';
	var aWithDrawChk = '';
	var aCheckChk = '';
	var aIntrestChk = '';
	var aFeeChk = '';
	if(aDeposit == true){
		aDepositChk = 0;
	}if(aWithDraw == true){
		aWithDrawChk = 1;
	}if(aCheck == true){
		aCheckChk = 2;
	}if(aIntrest == true){
		aIntrestChk = 4;
	}if(aFee == true){
		aFeeChk = 3;
	}
	var aArrayList = new Array();
	aArrayList.push(aDepositChk);
	aArrayList.push(aWithDrawChk);
	aArrayList.push(aCheckChk);
	aArrayList.push(aIntrestChk);
	aArrayList.push(aFeeChk);
	aArrayList.push(aBankAccount);
	aArrayList.push(aFromDate);
	aArrayList.push(aToDate);
	var aDetails = $('#transactionDetailsForm').serialize()+"&deposit="+aDepositChk+"&check="+aCheckChk+"&intrest="+aIntrestChk+"&fee="+aFeeChk+"&withDraw="+aWithDrawChk;
	document.location.href ="./transactionRegister?"+aDetails;
	return false;
}*/


function msgOnAjax(msg, color, time){
	var def_color = 'WHITE';
	if(color != '')
	def_color = color;
	var def_time = 10000;
	if(time != '')
	def_time = time;
	$.blockUI({ 
			message:msg, 
			fadeIn: 700, 
			fadeOut: 700, 
			timeout: def_time, 
			showOverlay: false, 
			centerY: false, 
			css: { 
				width: '350px', 
				//height: '50px',
				top: '50%', 
				left: '40%', 
				right: '10px', 
				border: 'none', 
				padding: '5px', 
				backgroundColor: '#000',
				"border-radius": '10px',
				"font-size": '15px',
				"font-family": 'Verdana,Arial,sans-serif',
				'-webkit-border-radius': '10px', 
				'-moz-border-radius': '10px', 
				opacity: .7, 
				color: def_color, 
				'font-weight': '900',
		    	'font-family':'verdana,arial'
			} 
		});
	}

/*Added to get Search List*/

$(function() { var cache = {}; var lastXhr='';
$("#searchJob").autocomplete({ minLength: 1,timeout :1000,
	/*open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},*/
select: function (event, ui) {
	var aValue = ui.item.value;
	var aText = ui.item.label;
	var valuesArray = new Array();
	valuesArray = aValue.split("|");
	
	
	var aQryStr = "aVePOId=" + aValue;
	document.location.href = "./editpurchaseorder?token=view&" + aQryStr;
	
},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/searchPONumber", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} });
});