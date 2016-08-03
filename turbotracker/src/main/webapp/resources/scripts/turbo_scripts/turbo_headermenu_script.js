var aGlobalVariable;
var aPOkey;
var _blockaddressjsfiles = true;
var editPOGlobalGridRowsOut="";
var editPOGlobalGridOut = "";
var editPOGlobalFormOut="";
var editInvGlobalForm="";
var editBankGlobalForm="";
var editInvCategoryGlobalForm="";
var editWarehouseGlobalForm="";
var editInventoryCountGlobalForm="{}";
var editCustomerpaymentGlobalForm="{}";
var editCustomerpayment='';
/** Add User Dialog Function **/

$(function(){
	$('#positivepayDate').datepicker().datepicker("setDate", new Date());
	$("#addUserDefaultsDialog").dialog({
		autoOpen:false,
		height:200,
		width:430,
		title:"User Settings",
		modal:true,
		close:function(){
			$('#userDefaultFormId').validationEngine('hideAll');
			return true;
		}
	});
	
	jQuery("#ChangePasswordDialogBox").dialog({
		autoOpen:false,
		width:300,
		title:"Change Password",
		modal:true,
		buttons:{
			"Submit":function(){
				changePasswordfunc();
				}
				},
		close:function(){
			
		return true;}	
	});
	
	jQuery("#PositivePayDialogBox").dialog({
		autoOpen:false,
		width:400,
		title:"Positive Pay",
		modal:true,
		buttons:{
			"Submit":function(){
				downloadpositivePayPdf();
				}
				},
		close:function(){
			
		return true;}	
	});
});

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
		
		if(data.length>0)
			{
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
			createtpusage('Home','Advanced Job Search','Info','Home,Searching Job,jobNumber:'+jobNumber+',project:'+project);
			var countJob = data.length; 
			if(countJob === 1){
				$.each(data, function(index, value){
					var jobNumber = value.jobNumber;
					var jobStatus = value.jobStatus;
					var descriptionval = value.description;
					var urijobname=encodeBigurl(descriptionval);
					var joMasterID_ID=value.joMasterId;
					document.location.href="./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+urijobname+"&jobStatus="+jobStatus+"&joMasterID"+joMasterID_ID;
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
				var urijobname=encodeBigurl(jobName);
				var uricusname=encodeBigurl(jobCustomer);
      			window.location.href = "./jobflow?token=view&joMasterID="+joMasterid+"&jobNumber="+jobNumber1+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+"";
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
      			var urijobname=encodeBigurl(jobName);
				var uricusname=encodeBigurl(jobCustomer);
  			window.location.href = "./jobflow?token=view&joMasterID="+joMasterid+"&jobNumber="+jobNumber1+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+""; 
		}
	});
}

function cancelJOB(){
	jQuery("#searchAdvJob").dialog( "close" );
	createtpusage('Home','Cancel Advanced Job Search','Info','Home,Cancel Advanced Job Search');
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


function updateJobstatus(jobNumber,status,joMasterID){
	if(status !== "Booked" && status !== "Closed" && status !== "Submitted"){
		$.ajax({
			url: "./job_controller/jobStatusHome", 
			mType: "GET", 
			data : { 'jobNumber' : jobNumber,'joMasterID':joMasterID },
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
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");reportCriteriaPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		$( "#bidDateReport" ).dialog("open");
		  return true;
	}
	
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
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
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
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");homePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		createtpusage("Home","View","Info","Home Tab");
		document.location.href = "./home";
		return true;
	}
		
//	}
	
}

function salesPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
    		var checkpermission=getGrantpermissionprivilage('Sales',0);
    		if(checkpermission)
    		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");employeePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Sales","View","Info","Sales Tab");
	    			document.location.href = "./sales";
	    			return true;
	    		}
	    	}
	
}

function showDeniedPopup()
{
	var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
							buttons: [{height:35,text: "OK",click: function() { 
								document.getElementById("CuInvoiceSaveID").disabled = false;
								document.getElementById("CuInvoiceSaveCloseID").disabled = false;
								$('#CuInvoiceSaveID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								$('#CuInvoiceSaveCloseID').css('background','-webkit-gradient(linear, left top, left bottom, from(#b47015), to(#6f4c23))');
								$(this).dialog("close"); }}]}).dialog("open");
	return true;
}

function projectPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	var checkpermission=getGrantpermissionprivilage('Project',0);
    	if(checkpermission)
    		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");projectPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Project","View","Info","Project Tab");
	    					document.location.href = "./projects";
	    		}
	    }
}

function getSysPrivilegeAccess(accessPage) {
	var access = "";
	$.ajax({
	    url: "./getSysPrivilage",
	    data: {'Page':accessPage,'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	if(data != "" && data != null && data.length > 0)
	    		return data;
	    	else
	    		return "denied";
	    	//$("#loading").hide();
	    }, error: function(error) {
			//$("#loading").hide();
		}
	});
}

function inventoryPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Inventory',0);
	if(checkpermission)
	{
				
	    	
			if($('#POFlag').val() === 'POKey')
			{
				var newDialogDiv = jQuery(document.createElement('div'));
				var errorText = "Do you want to save PO?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
					          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");inventoryPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
				event.preventDefault();
				/*event.stopImmediatePropagation();
				event.stopPropagation();
				return false;*/
			}
			else
			{
				createtpusage("Inventory","View","Info","Inventory Tab");
					document.location.href = "./inventory";
				}
	}
}

function inventoryValuePage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	var checkpermission=getGrantpermissionprivilage('Inventory_Value',0);
	if(checkpermission)
	{
	createtpusage("Inventory-Value","View","Info","Inventory,Value Tab");
	document.location.href = "./inventoryValue";
	}
	return true;
}

function bankingPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Banking',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");bankingPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Banking","View","Info","Banking Tab");
	    				document.location.href = "./bankingAccounts";
	    			}
	}
}

function customerPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	/*var editPOLocalGridRowsOut = $('#lineItemGrid').getRowData();
	var	editPOLocalGridOut = JSON.stringify(editPOLocalGridRowsOut);
	var editPOLocalFormOut = $("form[name=editPOOut]").serialize();
	console.log(editPOLocalFormOut+" AVAVA "+editPOLocalFormOut);
	if(editPOGlobalFormOut!=editPOLocalFormOut){
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Purchase Order Details modified, please save before leave the page!";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "OK",click: function() {
					$(this).dialog("close");
					$(this).dialog('destroy').remove();
					return false;
				}
			}]}).dialog("open");
		return false;
	}*/
	
	
	
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	var checkpermission=getGrantpermissionprivilage('Customers',0);
	    	if(checkpermission)
	    	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");customerPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			/*aUserLoginID = $("#userLoginID").val();
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
	    			}else{*/
	    			createtpusage("Company-Customers","View","Info","Company,Customers Tab");
	    				document.location.href = "./customers";
	    			//}
	    			return true;
	    		}
	    	}
}

function paymentsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);

	var checkpermission=getGrantpermissionprivilage('Payments',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");paymentsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{/*
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
	    			}else{*/
	    			createtpusage("Company-Customers-Payments","View","Info","Company,Customers,Payments Tab");
	    				document.location.href = "./customerpaymentlist";
	    			//}
	    			return true;
	    		}
	}
}


function unappliedpaymentsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);

	var checkpermission=getGrantpermissionprivilage('Payments',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");unappliedpaymentsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-Unapplied Payments","View","Info","Company,Customers,Unapplied Payments Tab");
	    				document.location.href = "./customerunappliedpaymentlist";
	    			
	    			return true;
	    		}
	}
}


function paymentTermsPage(){
	
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");paymentTermsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
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
	
	
}

function vendorPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Vendors',0);
	if(checkpermission)
	{
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");vendorPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		/*	aUserLoginID = $("#userLoginID").val();
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
		}else{*/
		createtpusage("Company-Vendor","View","Info","Company,Vendor Tab");
			document.location.href = "./vendors";
		/*}
		return true;*/
	}
	}
}

function purchaseOrderList(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	
	    var checkpermission=getGrantpermissionprivilage('Purchase_Orders',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");purchaseOrderList();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Vendor-PurchaseOrder","View","Info","Company,Vendor,PurchaseOrder Tab");
	    				document.location.href = "./po_list";
	    		}
	    }
	    
}

function payBillPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	
		 var checkpermission=getGrantpermissionprivilage('Pay_Bills',0);
			if(checkpermission)
			{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");payBillPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Vendor-PayBills","View","Info","Company,Vendor,PayBills Tab");
	    				document.location.href = "./vendorbills";
	    			}
			}	
	
}

function employeePage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	
		var checkpermission=getGrantpermissionprivilage('Employees',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");employeePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Employees","View","Info","Company,Employees Tab");
	    				document.location.href = "./employeelist";
	    		}
	    }
}
function orderPointsPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	var checkpermission=getGrantpermissionprivilage('Order_Points',0);
	if(checkpermission)
	{
		createtpusage("Inventory-OrderPoint","View","Info","Inventory,OrderPoint Tab");
					document.location.href = "./orderpoint";
	}
	
}


function countingInventoryPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Count',0);
	if(checkpermission)
	{
		createtpusage("Inventory-Count","View","Info","Inventory,Count Tab");
					document.location.href = "./countInventory";
	}
			
	
}
function inventoryAdjustmentsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Adjustments',0);
	if(checkpermission)
	{
		createtpusage("Inventory-Adjustment","View","Info","Inventory,Adjustment Tab");
					document.location.href = "./inventoryAdjustments";
	}
	
}

function inventoryTransactionPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Transactions',0);
	if(checkpermission)
	{
		createtpusage("Inventory-Transaction","View","Info","Inventory,Transaction Tab");
					document.location.href = "./inventoryTransactions";
	}
}

function commissionsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
		var checkpermission=getGrantpermissionprivilage('Commissions',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");commissionsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Employees-Commission","View","Info","Company,Employees,Commission Tab");
	    				document.location.href = "./employeeCommissions";
	    		}
		}
	
}

function rolodexPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	
		var checkpermission=getGrantpermissionprivilage('Rolodex',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");rolodexPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    }
	    else
		{
	    	createtpusage("Company-Rolodox","View","Info","Company,Rolodox Tab");
				document.location.href = "./rolodexList";
		}
		}
}

function drillDownPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('General_Ledger',0);
	if(checkpermission)
	{
		createtpusage("Company-GeneralLedger","View","Info","Company,GeneralLedger Tab");
	    			document.location.href = "./drillDown";
	}
	
}

function chartsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
    		var checkpermission=getGrantpermissionprivilage('Chart_Accounts',0);
    		if(checkpermission)
    		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");chartsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-ChartOfAccount","View","Info","Company,ChartOfAccount Tab");
	    					document.location.href = "./chartofaccounts";
	    		}
	    	}
}

function userListPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();

    		var checkpermission=getGrantpermissionprivilage('Users',0);
    		if(checkpermission)
    		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");userListPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Users","View","Info","Company,Users Tab");
	    				document.location.href ="./userlist";
	    		}
    		
    		}
}

function companySettingPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	
		var checkpermission=getGrantpermissionprivilage('Settings',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");companySettingPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Settings","View","Info","Company,Settings Tab");
	    				document.location.href ="./settings";
	    		}
	    }
}

function journalEntriesPage(){
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Journal_Entries',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");journalEntriesPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-GeneralLedger-JournalEntries","View","Info","Company,GeneralLedger,JournalEntries");
	    			document.location.href ="./companyjournals";
	    			return true;
	    		}
	}
}
function accountCycles(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Accounting_Cycles',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");accountCycles();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-AccountingCycle","View","Info","Company,AccountingCycle Tab");
	    			document.location.href ="./accountingcycles";
	    		}
	    		
	    	}
	    	
	
}
function shipviaPage(){
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");shipviaPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
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
	
}

function ledgerPage(){
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");ledgerPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
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
	
}

function reprintChecksPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}

	var checkpermission=getGrantpermissionprivilage('Reissue_Checks',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");reprintChecksPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Banking-ReissueChecks","View","Info","Banking,ReissueChecks Tab");
	    			document.location.href ="./reprintchecks";
	    			return true;
	    		}
	}	

}

function writeChecksPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Write_Checks',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");writeChecksPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Banking-WriteCheck","View","Info","Banking,WriteCheck Tab");
	    			document.location.href ="./writechecks";
	    			return true;
	    		}

	}
	
		
	/*}*/
	
}
function reconcileAccountsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Recouncile_Accounts',0);
	if(checkpermission)
	{
				if($('#POFlag').val() === 'POKey')
				{
					var newDialogDiv = jQuery(document.createElement('div'));
					var errorText = "Do you want to save PO?";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
						          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");writeChecksPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
					event.preventDefault();
					/*event.stopImmediatePropagation();
					event.stopPropagation();
					return false;*/
				}
				else
				{
					createtpusage("Banking-ReconcileAccounts","View","Info","Banking,ReconcileAccounts Tab");
					document.location.href ="./reconcileAccounts";
					return true;
				}

	
		}
}

function vendorInvoicesPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
		var checkpermission=getGrantpermissionprivilage('Invoice_Bills',0);
		if(checkpermission)
		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");vendorInvoicesPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Vendor-Invoice&Bills","View","Info","Company,Vendor,Invoice&Bills Tab");
	    			document.location.href ="./vendorinvoicelist";
	    			return true;
	    		}
			}
}

function inventoryCategories(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Categories',0);
	if(checkpermission)
	{
							if($('#POFlag').val() === 'POKey')
				{
					var newDialogDiv = jQuery(document.createElement('div'));
					var errorText = "Do you want to save PO?";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
						          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");inventoryCategories();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
					event.preventDefault();
					/*event.stopImmediatePropagation();
					event.stopPropagation();
					return false;*/
				}
				else
				{
					createtpusage("Inventory-Categories","View","Info","Inventory,Categories Tab");
					document.location.href ="./inventorycategories";
					return true;
				}
	}
	
		
	/*}*/
	
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
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");inventoryDepartments();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		document.location.href ="./inventorydepartments";
		return true;
	}
		
	/*}*/
	
}
function inventoryWarehouses(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Warehouses',0);
	if(checkpermission)
	{
							if($('#POFlag').val() === 'POKey')
				{
					var newDialogDiv = jQuery(document.createElement('div'));
					var errorText = "Do you want to save PO?";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
						          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");inventoryWarehouses();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
					event.preventDefault();
					/*event.stopImmediatePropagation();
					event.stopPropagation();
					return false;*/
				}
				else
				{
					createtpusage("Inventory-WareHouse","View","Info","Inventory,WareHouse Tab");
					document.location.href ="./warehouse";
				}
	}
	
	
}
function statementsPage(){

	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	    		var checkpermission=getGrantpermissionprivilage('Statements',0);
	    		if(checkpermission)
	    		{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");statementsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-Statements","View","Info","Company,Customers,Statements Tab");
	    			document.location.href ="./statements";
	    		}
	    	}
	    	
}
function inventoryRecieve(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Receive_Inventory',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");inventoryRecieve();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			//document.location.href ="./recieveInventory";
	    			createtpusage("Inventory-Recieve Inventory","View","Info","Inventory,Recieve Inventory Tab");
	    			document.location.href="./showReceivedInventoryList?vendorID=0&sortBy=0&rangeFrom=to";		
	    		}
	    	}
	    	
}

function salesorderPage()
{
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission =getGrantpermissionprivilage('Sales_Order',0);
	if(checkpermission){
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");salesorderPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}else
	{
		createtpusage("Company-Customers-SalesOrder","View","Info","Company,Customers,SalesOrder Tab");
		document.location.href ="./salesorder?oper=create";
	}
	}
	
	
}

function getGrantpermissionprivilage(accesspage,groupid){

	var returnvalue=false;
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	$.ajax({
	    url: "./getSysPrivilage",
	    async:false,
	    data: {'accessPage':accesspage,'userGroupID':groupid, 'UserLoginID':UserLoginID},
	    type: 'POST',
	    success: function(data){
	    	
	    	console.log(data.Value);
	    	
	    	if(data.Value == "granted")
	    	{
	    		returnvalue=true;
	    	}
	    	else if(data.Value == "deniedforOP"){
	    		returnvalue=false;
	    	}
	    	else
	    	{
	    		showDeniedPopup();
	    		returnvalue=false;
	    	}
	    }, error: function(error) {
	    	showDeniedPopup();
		}
	});
return returnvalue;
}
function invoicePage()
{
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	    	var checkpermission =getGrantpermissionprivilage('Invoice',0);
	    	if(checkpermission){
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");invoicePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-Invoice","View","Info","Company-Customers-Invoice Tab Click");
	    			document.location.href ="./createinvoice?oper=create";
	    		}
	    	}
	    	
}
function financeChargePage()
{
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	    		var checkpermission =getGrantpermissionprivilage('Finance_Changes',0);
		    	if(checkpermission){
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");invoicePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-Finance","View","Info","Company,Customers,Finance Tab");
	    			document.location.href ="./financeCharges";
	    		}
		    }
}


function taxAdjustments(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	    		var checkpermission =getGrantpermissionprivilage('Tax_Adjustments',0);
		    	if(checkpermission){
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");invoicePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-TaxAdjustment","View","Info","Company,Customers,TaxAdjustment Tab");
	    			document.location.href ="./taxAdjustments";
	    		}
		    	}
}

function creditDebitMemos(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	
    		var checkpermission =getGrantpermissionprivilage('CreditDebit_Memos',0);
	    	if(checkpermission){
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");invoicePage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Customers-CreditDebitMemos","View","Info","Company,Customers,CreditDebitMemos Tab");
	    			document.location.href ="./creditDebitMemos";
	    		}
		    }
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
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");OpenJobsCriteriaPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		createtpusage("Company-Reports-OpenJob","View","Info","Company,Reports,OpenJob Tab");
		document.location.href ="./openjobreport";
	}
	
}

function bookingsCriteriaPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");bookingsCriteriaPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		createtpusage("Company-Reports-Booking","View","Info","Company,Reports,Booking Tab");
		document.location.href ="./bookreport";
	}
	
}

function openPOCriteriaPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");openPOCriteriaPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		createtpusage("Company-Reports-Open PO","View","Info","Company,Open PO Tab");
		window.open('./job_controller/previewreport?reportName=openpo',target="_blank");
	}
	
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
//	console.log(cellValue+"<=====>"+floorFigure(cellValue,2))
//	return floorFigureoverall(cellValue,2);
	//return "$" + cellValue.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
}

function checkhowmanydecimaloverall(figure){
	console.log(figure);
	var x_str = figure.toString();
	var decimal_digits = x_str.length - x_str.lastIndexOf('.') - 1;
	console.log(x_str.length+"DEc"+decimal_digits);
	if(decimal_digits <=2){
		return false;
	} else{
		return true;
	}
	 
}
function cutitintodecimal(figurevalue){
	var x_str = figurevalue.toString();
	var str=x_str.split(".");
	if(str[1]!=undefined){
		var substr=str[1].toString();
		var suffix=substr.substring(0,2);
		return Number(str[0]+"."+suffix);
	}else{
		return Number(figurevalue);
	}
	
}
function floorFigureoverall(figure, decimals){
	
	if(figure == null || figure==undefined || figure=="NaN" || figure==""){
			figure = 0.00;
		}
	var check=checkhowmanydecimaloverall(figure);
	console.log(figure+"=="+check);
	if(check){
		var decimal_digits=getdecimaldigitval(figure);
		if(decimal_digits>4){
			 var power = Math.pow(10,4);
			figure=cutitintodecimalfourdigits(Number(figure*power)/power);
		}
		figure=Number(figure).toFixed(3);
		if (!decimals) decimals = 2;
	    var d = Math.pow(10,decimals);
	    //console.log("figure*d"+figure*d);
	    //console.log("parseFloat(figure*d)"+parseFloat(figure*d));
	    //console.log("parseFloat(figure*d)/d"+parseFloat(figure*d)/d);
	    return parseFloat(cutitintodecimal(Number(figure*d)/d)).toFixed(2);
	}else{//console.log("FloorFigureElse=="+Number(figure).toFixed(2));
		return Number(figure).toFixed(2);
	}
    
}
function cutitintodecimalfourdigits(figurevalue){
	var x_str = figurevalue.toString();
	var str=x_str.split(".");
	if(str[1]!=undefined){
		var substr=str[1].toString();
		var suffix=substr.substring(0,4);
		return Number(str[0]+"."+suffix);
	}else{
		return Number(figurevalue);
	}
	
}

function getdecimaldigitval(figure){
	var x_str = figure.toString();
	var decimal_digits = x_str.length - x_str.lastIndexOf('.') - 1;
	return decimal_digits;
}
function formatCurrency(strValue)
{
	if(strValue!=null && strValue!=undefined && strValue!="" && strValue!=0)
	{
	strValue = strValue.toString().replace(/\$|\,/g,'');
	if(Number(strValue)<0)
	{
	strValue=Number(strValue)*-1;
	console.log("==="+('-'+'$' + floorFigureoverall(strValue,2)).toString());
	return (('-'+'$' + floorFigureoverall(strValue,2)).toString());
	}
	else
	{
		console.log("==="+('-'+'$' + floorFigureoverall(strValue,2)).toString());
		return ( ('$' + floorFigureoverall(strValue,2)).toString());
	}
	}
	else
	{
	return ( '$0.00');
	}
	
}

function formatCurrencynodollar(strValue)
{
	var returnval=formatCurrency(strValue);
	returnval = returnval.toString().replace(/\$|\,/g,'');
	return returnval;
	
}
function divisionsPage(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Divisions',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");divisionsPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-Division","View","Info","Company,Division Tab");
	    				document.location.href = "./divisionsList";
	    			}
	}
}

function taxTerritories(){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Tax_Territories',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");taxTerritories();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-TaxTerritory","View","Info","Company,TaxTerritory Tab");
	    				document.location.href = "./taxTerritories";
	    			}
	}
}

function getPage(page){
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
//	var UserLoginID=$("#userLogin_Id").val();
//	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
//	$.ajax({
//	    url: "./getSysPrivilage",
//	    data: {'accessPage':'Recouncile_Accounts','userGroupID':0, 'UserLoginID':UserLoginID},
//	    type: 'POST',
//	    success: function(data){
//	    	if(data.Value == "granted")
//	    	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");getPage(page);return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
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
//	    	}
//	    	else
//	    		showDeniedPopup();
//	    }, error: function(error) {
//	    	showDeniedPopup();
//		}
//	});	
//	
}

/*$(".app_main_navi").click(function(event) {
	alert("Inside");
    alert('clicked inside'+$('#POFlag').val());
    alert('clicked aPOkey'+aPOkey);
    event.stopPropagation();
});*/

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

function POKey()
{
	
	if($('#POFlag').val() === 'POKey')
	{
		event.preventDefault();
		event.stopImmediatePropagation();
		event.stopPropagation();
		return false;
	}
	else
	{
		return true;
	}
	
}

function transferPage()
{
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('Transfer',0);
	if(checkpermission)
	{
							if($('#POFlag').val() === 'POKey')
				{
					var newDialogDiv = jQuery(document.createElement('div'));
					var errorText = "Do you want to save PO?";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
						          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");salesorderPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
					event.preventDefault();
					/*event.stopImmediatePropagation();
					event.stopPropagation();
					return false;*/
				}
				else
				{
					createtpusage("Inventory-transfer","View","Info","Inventory,transfer Tab");
					document.location.href ="./transfer?oper=create";
				}
	    	}
	    	
	
	
}

function salesOrderTemplate()
{
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var UserLoginID=$("#userLogin_Id").val();
	//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
	
	    		var checkpermission=getGrantpermissionprivilage('Sales_Order_Template',0);
	    		if(checkpermission)
	    		{
		    		if($('#POFlag').val() === 'POKey')
		    		{
		    			var newDialogDiv = jQuery(document.createElement('div'));
		    			var errorText = "Do you want to save PO?";
		    			jQuery(newDialogDiv).attr("id","msgDlg");
		    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
		    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");salesorderPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		    			event.preventDefault();
		    			/*event.stopImmediatePropagation();
		    			event.stopPropagation();
		    			return false;*/
		    		}
		    		else
		    		{
		    			createtpusage("Company-Customers-SalesOrderTemplate","View","Info","Company,Customers,SalesOrderTemplate Tab");
		    			document.location.href ="./salesOrderTemplate?oper=create";
		    		}
	    		}
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


function versionDialog(){
	
	window.open("https://sites.google.com/site/turboproversion", "_blank", "toolbar=yes, scrollbars=yes, resizable=yes, top=150, left=300, width=850, height=400");
	
	/*var page = "https://sites.google.com/site/turboproversion";
	var $dialog = $('<div></div>')
	               .html('<iframe sandbox="allow-scripts allow-forms" style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
	               .dialog({
	                   autoOpen: false,
	                   modal: true,
	                   height: 625,
	                   width: 500,
	                   title: "Version Information"
	               });
	$dialog.dialog('open');*/
		/*var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		console.log($('#versionData').val());
		jQuery(newDialogDiv).html('<span>'+$('#versionData').val()+'</span>');
		jQuery(newDialogDiv).dialog({modal: true, width:720, height:800, title:"Version Information",
		}).dialog("open");
		event.preventDefault();
		event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}


function glTransactionPage()
{
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('GL_Transactions',0);
	if(checkpermission)
	{
		createtpusage("Company-GlTransaction","View","Info","Company,GlTransaction Tab");
	    		document.location.href ="./gltransaction";
	 }
}
function TestForQuotes()
{
	document.location.href ="./TestForQuotes";
}

function openAddNewUserDefaultDialog(){
	
		//var aUserLoginID = "${requestScope.userSysAdmin}";
	aUserLoginID = $("#userLoginID").val();
	/*if(aUserLoginID !== '1'){
			var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
	}else{*/
		$.ajax({
	        url: './getUserDefaults',
	        type: 'POST',       
	        success: function (data) {
	        	var selected='';
	        	console.log("all--->"+data);
	        	var coDivision = "<option value='0'> -Select- </option>";
	        	var coWarehouse = "<option value='0'> -Select- </option>";
				$.each(data, function(key, valueMap) {
					if("divisions"==key)
					{
						$.each(valueMap, function(index, value){
							if(data.divisionID==value.coDivisionId){console.log(value.coDivisionId+' Selected');selected="selected";}else{selected="";}
							console.log('Jenith Division: '+value.coDivisionId+' '+value.description);
							if(value.description != null && value.description.trim() != '')
								coDivision+='<option value='+value.coDivisionId+' '+selected+'>'+value.description+'</option>';
						
						}); 
					}
					if("wareHouse"==key)
					{
						$.each(valueMap, function(index, value){
							console.log('Jenith prWHId: '+value.prWarehouseId+' '+value.searchName);
							if(value.searchName != null && value.searchName.trim() != '')
								if(data.warehouseID==value.prWarehouseId){console.log(value.prWarehouseId+' Selected');selected="selected";}else{selected="";}
								coWarehouse+='<option value='+value.prWarehouseId+' '+selected+'>'+value.searchName+'</option>';
						
						}); 
					}
					
				});

				$('#whrhouseheaderID').html(coWarehouse);
				$('#cODivisionID').html(coDivision);
				
	        }
	    });
		jQuery("#addUserDefaultsDialog").dialog("open");	
		document.getElementById("userDefaultFormId").reset();
	//}
	return true;
}




function saveUserDefault(){
	if(!$('#userDefaultFormId').validationEngine('validate')) {
		return false;
	}
	var userDetails = $('#userDefaultFormId').serialize();
	$("#loginuserID").val($("#loginNameID").val());
				$.ajax({
					url: "./userlistcontroller/addUserDefaults",
					type: "POST",
					async:false,
					data : userDetails,
					success: function(data){
						$('#userDefaultFormId').validationEngine('hideAll');
						$("#addUserDefaultsDialog").dialog("close");
						$("#addUserDefaultsDialog").dialog('destroy').remove();
						$("#addUserDefaultsDialog").dialog('destroy');
							return true;
					}
			   });
}

/**  Cancel Dialog For User  **/
function cancelUserDefault(){
	$('#userDefaultFormId').validationEngine('hideAll');
	$("#addUserDefaultsDialog").dialog("close");
	$("#addUserDefaultsDialog").dialog('destroy').remove();
	$("#addUserDefaultsDialog").dialog('destroy');
	return true;
}

function nospacesAddUser(t){
	if(t.value.match(/\s/g)){
		t.value=t.value.replace(/\s/g,'');
	}
}

function balanceSheet() {
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('General_Ledger',0);
	if(checkpermission)
	{
	    		if($('#POFlag').val() === 'POKey')
	    		{
	    			var newDialogDiv = jQuery(document.createElement('div'));
	    			var errorText = "Do you want to save PO?";
	    			jQuery(newDialogDiv).attr("id","msgDlg");
	    			jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
	    			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
	    				buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
	    				          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");customerPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
	    			event.preventDefault();
	    			/*event.stopImmediatePropagation();
	    			event.stopPropagation();
	    			return false;*/
	    		}
	    		else
	    		{
	    			createtpusage("Company-GeneralLedger-BalanceSheet","View","Info","Company,GeneralLedger,BalanceSheet Tab");
	    				document.location.href = "./balanceSheet";
	    			}
	}
}

function trialBalance() {
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('General_Ledger',0);
	if(checkpermission)
	{
	if($('#POFlag').val() === 'POKey')
	{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = "Do you want to save PO?";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
			          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");customerPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
		event.preventDefault();
		/*event.stopImmediatePropagation();
		event.stopPropagation();
		return false;*/
	}
	else
	{
		createtpusage("Company-GeneralLedger-TrialBalance","View","Info","Company,GeneralLedger,TrialBalance Tab");
			document.location.href = "./trialBalance";
		}
	}
}

function incomeStatement() {
	
	var pageIsEdited = validatePageEdited();
	if(pageIsEdited=='1'){
		return false;
	}
	
	var checkpermission=getGrantpermissionprivilage('General_Ledger',0);
	if(checkpermission)
	{
			if($('#POFlag').val() === 'POKey')
			{
				var newDialogDiv = jQuery(document.createElement('div'));
				var errorText = "Do you want to save PO?";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "Yes",click: function() { $(this).dialog("close");return false;/*$('#salesrelease').dialog("close");*/}},
					          {height:35,text: "No",click: function() { $(this).dialog("close");$('#POFlag').val("");customerPage();return true;/*$('#salesrelease').dialog("close");*/}}]}).dialog("open");
				event.preventDefault();
				/*event.stopImmediatePropagation();
				event.stopPropagation();
				return false;*/
			}
			else
			{
				createtpusage("Company-GeneralLedger-IncomeStatement","View","Info","Company,GeneralLedger,IncomeStatement Tab");
					document.location.href = "./incomeStatement";
				}
	}
}
function getSysvariableStatusBasedOnVariableName(VariableName){
	var returnValue=null;
	$.ajax({
		url: "./userlistcontroller/getSysvariableStatusBasedOnVariableName",
		type: "POST",
		async:false,
		data : {
			'VariableName':VariableName
		},
		success: function(data){
			if(data!=null && data.length>0){
				returnValue=data;
			}
			
		}
   });
	return returnValue;
}
function chngPswd(){
	$("#oldPswd").val("");
	$("#newPswd").val("");
	$("#cnfrmPswd").val("");
	jQuery("#ChangePasswordDialogBox").dialog("open");
}
function createtpusage(screen,action,loglevel,description){
	$.ajax({
	    url: "./userlistcontroller/CreateTpUsage",
	    async:false,
	    data: {'screen':screen,'action':action, 'logLevel':loglevel,'description':description},
	    type: 'POST',
	    success: function(data){
	    }, error: function(error) {
		}
	});
}

function validatePageEdited(){
	
	
	
	var page ='';
	var editPOLocalGridRowsOut = $('#lineItemGrid').getRowData();
	var	editPOLocalGridOut = JSON.stringify(editPOLocalGridRowsOut);
	var editPOLocalFormOut =$("form[name=editPOOut]").serialize();
	/*Inventory Details */
	var editInvLocalForm = $("#inventoryDetailsFormId").serialize();
	var editBankLocalForm = $("#bankingDetailsFromID").serialize();
	var editInvCategoryLocalForm = $("#categoryDetailsForm").serialize();
	var editWarehouseLocalForm = $("#warehouseDetails").serialize();
	
	var editCountInventoryGrid = $('#CountInventoryGrid').getRowData();
	var	editInventoryCountGridData = JSON.stringify(editCountInventoryGrid);
	

	if(editInventoryCountGridData=="[]"){
		editInventoryCountGridData="{}";
	}
	
	if($("#PurchaseOrderDiv").val()!=undefined){
		var generaltab=$( "#PurchaseOrderDiv ul li:nth-child(1)" ).hasClass("ui-state-active");
		var lineItemtab=$( "#PurchaseOrderDiv ul li:nth-child(2)" ).hasClass("ui-state-active");
		if(generaltab){
			var new_po_generalform_values=poGeneralTabFormValidation();
		    var new_po_general_form =  JSON.stringify(new_po_generalform_values);
		    var gridRows = $('#EditPoSplitCommissionGrid').getRowData();
		    var newPOcommissiongridform=JSON.stringify(gridRows);
		    var gridreturnvalue=(commission_PO_grid_form!=newPOcommissiongridform)?true:false;
		    if(commission_PO_grid_form==undefined){
		    	gridreturnvalue=false;
		    }
		    if(po_General_tab_form !=new_po_general_form  || gridreturnvalue){
		    	errorLevel='1';
				page='PurchaseOrder';
		    }else{
		    	errorLevel='0';
		    }
		}else{
			 var gridRows = $('#lineItemGrid').getRowData();
			 var new_PO_lines_form =  JSON.stringify(gridRows);
			 var freightLineId=$("#freightLineId").val();
			    if((freightLineId+"").contains("$")){
			    	freightLineId=freightLineId.replace(/[^0-9\.-]+/g,"");
			    }
			    freightLineId=formatCurrency(freightLineId);
			    new_PO_lines_form=new_PO_lines_form+freightLineId;
			 if(new_PO_lines_form != Purchase_lines_form){
				 errorLevel='1';
				 page='PurchaseOrder';
			 }else{
				 errorLevel='0';
			 }
		}
		//console.log(editPOGlobalFormOut+" AVAVA "+editPOLocalFormOut);
		
	}else if(editInvGlobalForm != editInvLocalForm)
		{
		console.log(editInvGlobalForm+" AVAVA "+editInvLocalForm);
		errorLevel='1';
		page='Inventory';
	}else if(editBankGlobalForm != editBankLocalForm)
		{
		console.log(editBankGlobalForm+" AVAVA "+editBankLocalForm);
		errorLevel='1';
		page='Bank';
	}else if(editInvCategoryGlobalForm != editInvCategoryLocalForm)
	{
		console.log(editInvCategoryGlobalForm+" AVAVA "+editInvCategoryLocalForm);
		errorLevel='1';
		page='Inventory Category';
	}else if(editWarehouseGlobalForm != editWarehouseLocalForm)
	{
		console.log(editWarehouseGlobalForm+" AVAVA "+editWarehouseLocalForm);
		errorLevel='1';
		page='Warehouse';	
	}else if(editInventoryCountGlobalForm != editInventoryCountGridData)
	{
		console.log(editInventoryCountGlobalForm+" AVAVA "+editInventoryCountGridData);
		errorLevel='1';
		page='Inventory Count';	
	}
	else if(editCustomerpayment == "cp")
	{
		errorLevel='1';
		page='Customer Payment';	
	}
	else{
		 errorLevel='0';
	}
	
	//alert("--"+errorLevel);
	
	if(errorLevel=='1'){
		
		if(page == "Customer Payment")	
		{
		var newDialogDiv = jQuery(document.createElement('div'));
		var errorText = page+" Details modified, please save before leave the page!";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		buttons:{
			"OK": function(){
				$(this).dialog("close");
				$(this).dialog('destroy').remove();
				errorLevel='1';
			}
		}
		}).dialog("open");
		}
		else
		{
			var newDialogDiv = jQuery(document.createElement('div'));
			var errorText = page+" Details modified, please save before leave the page!";
			jQuery(newDialogDiv).attr("id","msgDlg");
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
			buttons:{
				"OK": function(){
					$(this).dialog("close");
					$(this).dialog('destroy').remove();
					errorLevel='1';
				},"Cancel": function ()	{
					jQuery(this).dialog("close")
					 errorLevel='0'
				}
			}
			}).dialog("open");
		}
			
	}
	return errorLevel;
}
function downloadpositivePayPdf(){
	var positivepayDate=$("#positivepayDate").val();
	if(positivepayDate==null ||positivepayDate==""){
		$('#positivepayLabelID').html("<span style='color: red'>*Mandatory Field required</span>");
		setTimeout(function(){
		$('#positivepayLabelID').html("");						
		},2000);
	return false;
	}
	window.open("./companycontroller/positivepayPDFdownload?positivePayDate="+positivepayDate);
	jQuery("#PositivePayDialogBox").dialog("close");
}
function checkhowmanydecimal(figure){
	var x_str = figure.toString();
	var decimal_digits = x_str.length - x_str.lastIndexOf('.') - 1;
	//console.log(x_str.length+"DEc"+decimal_digits);
	if(decimal_digits <=2){
		return false;
	} else{
		return true;
	}
	 
}
function floorFigure(figure, decimals){
	floorFigureoverall(figure, decimals);
}
function removedollarsymbol(value,id){
	if(value!=null && value!=""){
		value=value.replace("$","");
		$("#"+id).val(value);
	}
}
function Round_priceMultiplier(value){
	if(value!=undefined){
		value=Number(value);
		return value.toFixed(4);
	}else{
		return 0.0000;
	}
}

function callFloorFigureMethod(cellValue, options, rowObject) {
	if(cellValue!=undefined && cellValue!=""){
		return floorFigureoverall(cellValue, 2);
	}else{
		return 0;
	}
	 
}
String.prototype.contains = function(it) { return this.indexOf(it) != -1; };


function encodeBigurl(value){
	var returnvalue="";
	if(value!=null && value!=undefined){
		returnvalue=encodeURIComponent(value);
	}
	return returnvalue;
}

