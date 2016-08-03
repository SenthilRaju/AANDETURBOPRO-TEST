var newDialogDiv = jQuery(document.createElement('div'));
var jobber = $(".jobHeader_JobNumber").val();
var CustomName = $(".customerNameField").val();
var jobLocation1 = $("#locationAddressID1").val();
var jobLocation2 = $("#locationAddressID2").val();
var jobCity = $("#locationCity").val();
var jobState = $("#locationState").val();
var jobZip = $("#locationZipID").val();
var globalVar='0';
var errorMessage = new Array();
var taxTerritory;
$(".datepickerCustom").datetimepicker();
$(".datepicker").datepicker();
var aSysAdmin = $("#userAdminID").val();
var aUserAdmin = $("#userAdminID").val();
var jobStatus = getUrlVars()["jobStatus"];
if(jobStatus === "Booked"){
	$("#customerLabel").css("display", "none");
	$("#customerFields").css("display", "none");
	var customer = '&nbsp;<b>Customer: </b><span style="color:red;" class="mandatory"> *</span>';
	var customerFieldAdmin = '<input id="jobHeader_JobCustomerName_id" name="jobHeader_JobCustomer_name" class="jobHeader_JobCustomer validate[required]" style="width: 300px;" type="text" value="" placeholder="Minimum 3 characters required to get Customer List">&nbsp;'+
						' <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" OnClick="showCustomerPage()">';
	/* Modified By Jenith on 2015-07-15 
	 *  for Enable the Customer text field editable for all the Users including Non-Admin users too
	 *  
	 * var customerField = '<input id="jobHeader_JobCustomerName_id" name="jobHeader_JobCustomer_name" class="jobHeader_JobCustomer validate[required]" style="width: 300px;" type="text" value="" placeholder="Minimum 3 characters required to get Customer List" disabled="disabled">&nbsp;'+
	' <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" OnClick="showCustomerPage()">';*/
	var customerField = '<input id="jobHeader_JobCustomerName_id" name="jobHeader_JobCustomer_name" class="jobHeader_JobCustomer validate[required]" style="width: 300px;" type="text" value="" placeholder="Minimum 3 characters required to get Customer List">&nbsp;'+
	' <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" OnClick="showCustomerPage()">';
	$("#customerBookedLabel").empty();
	$("#customerBookedFields").empty();
	$("#customerBookedLabel").append(customer);
	if(aSysAdmin !== '1'){
		$("#customerBookedFields").append(customerField);
	}else{
		$("#customerBookedFields").append(customerFieldAdmin);
	}
	$("#jobHeader_JobCustomerName_id").val($("#customerHiddenID").val());
	bookedType = 'bookedID';
	validateBookedJob();
	//updateBookedQuote(bookedType, bookedStatus);
	/*if($("#jobHeader_JobCustomerName_id").val() === ''){
		var errorText = "Please Add a Customer.";
		
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}*/
}
$(document).ready(function(){
	var jobStatesIs = $("#jobStatusList").val();
	
	if(jobStatesIs == "3") {
		/* on 2015-07-10
		 * $("#jobHeader_JobCustomerName_id").attr("readonly", "true"); 
	    $("#jobHeader_JobCustomerName_id").css("background-color" , "#F0F0F0");
	    
	    $("#customerNameFieldID").attr("readonly", "true");
	    $("#customerNameFieldID").css("background-color" , "#F0F0F0");
	    
	    $("#cusotmerName").attr("readonly", "true"); 
	    $("#cusotmerName").css("background-color" , "#F0F0F0");*/
    }else if(jobStatesIs == "4") {
    	/*on 2015-07-10
    	 * $("#jobHeader_JobCustomerName_id").attr("readonly", "true"); 
	    $("#jobHeader_JobCustomerName_id").css("background-color" , "#F0F0F0");
	    
	    $("#customerNameFieldID").attr("readonly", "true");
	    $("#customerNameFieldID").css("background-color" , "#F0F0F0");
	    
	    $("#cusotmerName").attr("readonly", "true"); 
	    $("#cusotmerName").css("background-color" , "#F0F0F0");*/
    }else{
	    /*on 2015-07-10
	     * $("#jobHeader_JobCustomerName_id").removeAttr("readonly");
	    $("#jobHeader_JobCustomerName_id").css("background-color" , "#FFFFFF");
	    $("#customerNameFieldID").removeAttr("readonly");
	    $("#customerNameFieldID").css("background-color" , "#FFFFFF");
	    $("#cusotmerName").removeAttr("readonly");
	    $("#cusotmerName").css("background-color" , "#FFFFFF");*/
    }
	
	
	if(jobber===''){
		$("#jobStatusList option[value='-1']").attr("disabled",true);
		$("#jobStatusList option[value='-2']").attr("disabled",true);
		$("#jobStatusList option[value='-3']").attr("disabled",true);
		$("#jobStatusList option[value='-4']").attr("disabled",true);
		$("#jobStatusList option[value='1']").attr("disabled",true);
		$("#jobStatusList option[value='2']").attr("disabled",true);
		$("#jobStatusList option[value='3']").attr("disabled",true);
		$("#jobStatusList option[value='4']").prop("disabled",true);
		$("#jobStatusList option[value='5']").attr("disabled",true);
		$("#jobStatusList option[value='6']").attr("disabled",true);
	}
	
	POAmountCalculation();
	var token = getUrlVars()["token"];
	if (token === 'new') {
		divisionFromLogin();
		$("#bidDate_Date").datepicker('setDate', new Date());
	}
	var selectTab = getUrlVars()["selectTab"];
	if(selectTab === 'credit'){
		$("#tabs_main_job").tabs("select", 3);
	}
	var bookedType = '';
	var bookedStatus = $('.jobHeader_ORI_JobNumber').val();
	$('#loadingMainDiv').css({"visibility": "hidden"});
	$("#contractAmount").val(formatCurrency($("#contractAmountID").val()));
	$("#totalContractamount").val(formatCurrency($("#contractAmountID").val()));
	
	return true;
});

function setHeaderDetails(){
	$("#jobHeader_JobCustomerName_id").val($("#customerHiddenID").val());
}

function validateBookedJob() {
	var aTaxTerritory = $("#TaxTerritory").val();
	var aTax = $("#jobMain_TaxTerritory").val();
	var aSalesRep = $("#jobMain_salesRepsList").val();
	var aSalesID = $("#salesRepId").val();
	var aEstimate = $("#estomattedHiddenID").val();
	var jobContractAmount = $("#jobContractAmount").val();
//	
	var errorText = "'Tax Territory'";
	var errorTextSales = "'Salesman'";
	var errorTextEstim = "'Contract Amount' and 'Estimated Cost' in Quote page";
	var errorTextCust = "'Customer'";
	
	if(aTax === ''){
		errorMessage.push(errorText);
	}
	if(aSalesRep === ''){
		errorMessage.push(errorTextSales);
	}
	/*if(aEstimate === '0.0000'){
		errorMessage.push(errorTextEstim);
	}*/
	if(!withorwithoutcreditamt){
	if(aEstimate === '' || jobContractAmount === '' || aEstimate === '0.0000' || jobContractAmount === '0.0000'){
		errorMessage.push(errorTextEstim);
	}
	}
//	if($(".customerNameField").val() === ''){
//		errorMessage.push(errorTextCust);
//	}else if($("#jobHeader_JobCustomerName_id").val() === ''){
//		errorMessage.push(errorTextCust);
//	}
	if($('#jobStatusList').val()==3){
	
	var errorMessageText = "Following fields are mandatory for a booked job: <br/><br/>";
	for(var i=0; i < errorMessage.length; i++){
		var index = i+1;
		errorMessageText = errorMessageText + index+". "+errorMessage[i] + '<br/>' ;  
		if (errorMessage !== ''){
			jQuery(newDialogDiv).html('<span style="color:#FF0066;">'+errorMessageText+'</span><br><hr style="color: #cb842e;">');
			 jQuery(newDialogDiv).dialog({modal: true, title:"Alert",width:420, 
					buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			 $('div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix').css('margin','-1.5em 0 0 0');
			 $('div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix').css('padding',' 0.3em 1em 0.1em .4em');
		}
	}
}

	return false;
}
	 
function updateBookedQuote(bookedType, bookedStatus){
	/*var aTaxTerritory = $("#TaxTerritory").val();
	var aTax = $("#jobMain_TaxTerritory").val();
	var aSalesRep = $("#jobMain_salesRepsList").val();
	var aSalesID = $("#salesRepId").val();
	var aEstimate = $("#estomattedHiddenID").val();
	var errorText = "'Tax Territory'.";
	var errorTextSales = "'Salesman'.";
	var errorTextEstim = "'Estimated Cost' in Quote page.";
	jQuery(newDialogDiv).attr("id","msgDlg");
	if(aTaxTerritory === ''){
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aTax === ''){
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aSalesRep === ''){
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorTextSales+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aSalesID === ''){
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorTextSales+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aEstimate === '0.0000'){
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorTextEstim+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}*/
	validateBookedJob();
	var joMasterID = $("#joMaster_ID").text();
	var aJobNumber = $("#jobNumber_ID").text();
	var curDate = new Date();
	var curYear = curDate.getFullYear();
	curYear = curYear.toString().slice(2);
	var curMonth = curDate.getMonth()+1;
	if(curMonth<=9){
		curMonth = "0"+curMonth;
	}
	var aNewNumber = curYear+curMonth;
	if(bookedType !== ''){
		if(bookedStatus === ''){
			$.ajax({
				url: './job_controller/ORI_QuoteNumber',
				type : 'POST',
				data: { 'joMasterID' : joMasterID, 'jobNumber' : aJobNumber,'aQuoteNumber' : aNewNumber },
				success: function (data){
					var jobNumber = data.jobNumber;
					var jobDescription = data.description;
					var urijobname=encodeBigurl(jobDescription);
					var aQryStr = "jobNumber="+jobNumber+"&jobName="+urijobname+"&jobStatus=Booked"+"&joMasterID="+joMasterID;
					var checkpermission=getGrantpermissionprivilage('Main',0);
					if(checkpermission){
					document.location.href = "./jobflow?token=view&" + aQryStr;
					}
				}
			});
		}
	}
	return true;
}

function openAddEngineer()
{
	jQuery("#addEngineerDialog").dialog("open");
}

jQuery(function(){
	jQuery("#addEngineerDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Add New Engineer",
			height: 400,
			width: 785,
			buttons : {  },
			close:function(){
				$('#addNewEngineerForm').validationEngine('hideAll');
				return true;
			}
	});
});
function saveNewEngineer(){
	if(!$('#addNewEngineerForm').validationEngine('validate')) {
		return false;
	}
	var areaCode=$("#areaCode").val();
	var exchangeCode = $("#exchangeCode").val();
	var subscriberNumber = $("#subscriberNumber").val();
	var contact1='';
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#areaCode1").val();
	var exchangeCode1 = $("#exchangeCode1").val();
	var subscriberNumber1 = $("#subscriberNumber1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#areaCode2").val();
	var exchangeCode2 = $("#exchangeCode2").val();
	var subscriberNumber2 = $("#subscriberNumber2").val();
	var fax='';
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var engineerForm = $("#addNewEngineerForm").serialize();
	$.ajax({
			type:'POST',
			url: "./jobtabs1/addNewEngineer",
			data: engineerForm+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax=" +fax,
			success:function(data){
				var rxMasterID = data.rxMasterId;
				$("#engineersRXId").val(rxMasterID); 
			}
		});
	$('#addNewEngineerForm').validationEngine('hideAll');
	jQuery("#addEngineerDialog").dialog("close");
	return $("#jobMain_engineersRXList").val($("#companyName1").val());
}
function openAddArchitect()
{
	jQuery("#addArchitectDialog").dialog("open");
}

jQuery(function(){
	jQuery("#addArchitectDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Add New Architect",
			height: 400,
			width: 785,
			buttons : {  },
			close:function(){
				$('#addNewArchitectForm').validationEngine('hideAll');
				return true;
			}
	});
});

function saveNewArchitect(){
	if(!$('#addNewArchitectForm').validationEngine('validate')) {
		return false;
	}
	var areaCode=$("#area_Code").val();
	var exchangeCode = $("#exchange_Code").val();
	var subscriberNumber = $("#subscriber_Number").val();
	var contact1='';
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#area_Code1").val();
	var exchangeCode1 = $("#exchange_Code1").val();
	var subscriberNumber1 = $("#subscriber_Number1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#area_Code2").val();
	var exchangeCode2 = $("#exchange_Code2").val();
	var subscriberNumber2 = $("#subscriber_Number2").val();
	var fax='';
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var engineerForm = $("#addNewArchitectForm").serialize();
	$.ajax({
			type:'POST',
			url: "./jobtabs1/addNewArchitect",
			data: engineerForm+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax="+ fax,
			success:function(data){
				var rxMasterID = data.rxMasterId;
				$("#architectId").val(rxMasterID); 
			}
		});
	$('#addNewArchitectForm').validationEngine('hideAll');
	jQuery("#addArchitectDialog").dialog("close");
	return $("#jobMain_architectsList").val($("#companyName2").val());
}

function openAddContractor()
{
	jQuery("#addContractorDialog").dialog("open");
}

jQuery(function(){
	jQuery("#addContractorDialog").dialog({
			autoOpen : false,
			modal : true,
			title:"Add New General Contractor",
			height: 400,
			width: 785,
			buttons : {  },
			close:function(){
				$('#addNewContractorForm').validationEngine('hideAll');
				return true;
			}
	});
});

function saveNewContractor(){
	if(!$('#addNewContractorForm').validationEngine('validate')) {
		return false;
	}
	var areaCode=$("#gcareaCode").val();
	var exchangeCode = $("#gcexchangeCode").val();
	var subscriberNumber = $("#gcsubscriberNumber").val();
	var contact1='';
	if(areaCode !== ''){
	contact1="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
	}
	var areaCode1=$("#gcareaCode1").val();
	var exchangeCode1 = $("#gcexchangeCode1").val();
	var subscriberNumber1 = $("#gcsubscriberNumber1").val();
	var contact2='';
	if(areaCode1 !== ''){
		contact2="("+areaCode1+") "+exchangeCode1+"-"+subscriberNumber1;
	}
	var areaCode2=$("#gcareaCode2").val();
	var exchangeCode2 = $("#gcexchangeCode2").val();
	var subscriberNumber2 = $("#gcsubscriberNumber2").val();
	var fax='';
	if(areaCode2 !== ''){
		fax="("+areaCode2+") "+exchangeCode2+"-"+subscriberNumber2;
	}
	var engineerForm = $("#addNewContractorForm").serialize();
	$.ajax({
			type:'POST',
			url: "./jobtabs1/addNewContractor",
			data: engineerForm+ "&USPhoneNumber="+ contact1 +"&USPhone_Number="+ contact2 +"&fax="+ fax,
			success:function(data){
				var rxMasterID = data.rxMasterId;
				$("#GCId").val(rxMasterID); 
			}
		});
	$('#addNewContrcatorForm').validationEngine('hideAll');
	jQuery("#addContractorDialog").dialog("close");
	return $("#jobMain_GCList").val($("#companyName3").val());
}

function jobMainForm_save() {
	if(!$('#jobHeaderFormId').validationEngine('validate')) {
		return false;
	}
	if(!$('#jobWizardMainForm').validationEngine('validate')) {
		return false;
	}
	var Divisionsysvariable=getSysvariableStatusBasedOnVariableName('RequireaDivisionwhencreatingJobs');
	if(Divisionsysvariable!=null && Divisionsysvariable[0].valueLong==1){
		if($("#jobMain_Divisions").val() === '-1'){
			
			jQuery(newDialogDiv).html('<span><b style="color: red;">Please select Division</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:200, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		}
	
	/* if($("#bidDate_jobStatusList").val() === '-1'){
	       
	        jQuery(newDialogDiv).html('<span><b style="color: red;">Please select BidStatus</b></span>');
	        jQuery(newDialogDiv).dialog({modal: true, width:200, height:150, title:"Warning",
	                                buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	        return false;
	    }*/
	
	var jobName_project = $("#jobHeaderFormId").serialize();
	var jobDescription = $(".jobHeader_JobName").val();
	//jobDescription = "`"+$(".jobHeader_JobName").val()+"`";
	//jobDescription = jobDescription.replace(/&/, "`");
	var jobCustomer = $(".jobHeader_JobCustomer").val();
	var jobNumber = $(".jobHeader_JobNumber").val();
	var jobCustomerId = $("#JobCustomerId").val();
	var joMasterId = $("#joMasterHiddenID").val();
	var jobStatusID = $('#jobStatusList').val();
	if($("#jobMain_salesRepsList").val() === ''){
		$("#salesRepId").val("");
	}if($("#jobMain_CSRList").val() === ''){
		$("#CSRId").val("");
	}if($("#jobMain_SalesMgrList").val() === ''){
		$("#salesMgrId").val("");
	}if($("#jobMain_EngineersList").val() === ''){
		$("#cus_engineerId").val("");
	}if($("#jobMain_PrjMgrList").val() === ''){
		$("#prjMgrId").val("");
	}if($("#jobMain_TakeOffList").val() === ''){
		$("#takeOffId").val("");
	}if($("#jobMain_QuoteByList").val() === ''){
		$("#quoteId").val("");
	}if($("#jobMain_architectsList").val() === ''){
		$("#architectId").val("");
	}if($("#jobMain_engineersRXList").val() === ''){
		$("#engineersRXId").val("");
	}if($("#jobMain_GCList").val() === ''){
		$("#GCId").val("");
	}
	if($("#jobMain_TaxTerritory").val() === ''){
		$("#TaxTerritory").val("");
	}
	var salesRepsList = $("#jobMain_salesRepsList").val();
	var CSRList = $("#jobMain_CSRList").val();
	var SalesMgrList = $("#jobMain_SalesMgrList").val();
	var EngineersList = $("#jobMain_EngineersList").val();
	var PrjMgrList = $("#jobMain_PrjMgrList").val();
	var TakeOffList = $("#jobMain_TakeOffList").val();
	var QuoteByList = $("#jobMain_QuoteByList").val();
	var architectsList = $("#jobMain_architectsList").val();
	var engineersRXList = $("#jobMain_engineersRXList").val();
	var GCList = $("#jobMain_GCList").val();
	var TaxTerritoryID = $("#TaxTerritory").val();
	var webpageurltoopen=$("#webpageurltoopen").val();
	var urijobname=encodeBigurl(jobDescription);
	var uricusname=encodeBigurl(jobCustomer);
	var data = $("#jobWizardMainForm").serialize();
	var jobMainFormQryStr = "jobCustomer=" + uricusname + "&jobDescription=" + urijobname + "&jobCustomerID=" + jobCustomerId + "&" + data + "&" + jobName_project;//+"&webpageurltoopen="+webpageurltoopen;
	if(joMasterId === "") {
		$.ajax({
			type:'POST',
			url: "./jobtabs1/createjob?"+jobMainFormQryStr,
			data: {"webpageurltoopen":webpageurltoopen},
			async:false,
			success: function(data) {
				var jobNumber = data.jobNumber;
				var jobName = data.description;
				var joMasterIDD=data.joMasterId;
				var jobStatus = "Bid";
				var urijobname=encodeBigurl(jobName);
				var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + urijobname + "&jobStatus=" + jobStatus+"&joMasterID="+joMasterIDD;
				createtpusage('job-Main Tab','Save New Job','Info','Job,Main Tab,Save New Job,JobNumber:'+jobNumber);
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(checkpermission){
				document.location.href = "./jobflow?token=view&" + aQryStr;
				}
			}
		});
	} else {
		
		var updateJobMainQryStr = "joMasterId=" + joMasterId + "&jobCustomer=" + uricusname +"&jobCusromerId=" + jobCustomerId +
												"&salesRep=" + salesRepsList + "&csrList=" + CSRList + "&salesMgrList=" + SalesMgrList + "&enggList=" + EngineersList +
												"&prjMgerList=" + PrjMgrList + "&takeoffList=" + TakeOffList + "&quoteByList=" + QuoteByList + "&architectList=" + architectsList +
												"&enggRXList=" + engineersRXList + "&GcList=" + GCList + "&taxTerritory=" + TaxTerritoryID + "&" + data + "&" + 
												jobName_project+"&joBidStatusId="+$('#bidDate_jobStatusList').val()+"&jobStatusID="+jobStatusID;//+"&webpageurltoopen="+webpageurltoopen;
		
		$.ajax({
			url: "./job_controller/updatejob?"+updateJobMainQryStr,
			type: "POST",
			async:false,
			data : {"jobDescription":jobDescription,"webpageurltoopen":webpageurltoopen},
			success: function(data) {
				var jobStatusheader = data.jobStatus;
				var jobStatusheaderName = "";
				if(jobStatusheader == 0){
					jobStatusheaderName = "Bid";
				}else if(jobStatusheader == -4){
					jobStatusheaderName = "Planning";
				}else if(jobStatusheader == 6){
					jobStatusheaderName = "Budget";
				}else if(jobStatusheader == 1){
					jobStatusheaderName = "Quote";
				}else if(jobStatusheader == 5){
					jobStatusheaderName = "Submitted";
				}else if(jobStatusheader == 3){
					jobStatusheaderName = "Booked";
				}else if(jobStatusheader == 4){
					jobStatusheaderName = "Closed";
				}else if(jobStatusheader == 2){
					jobStatusheaderName = "Lost";
				}else if(jobStatusheader == -1){
					jobStatusheaderName = "Abandoned";
				}else if(jobStatusheader == -2){
					jobStatusheaderName = "Rejected";
				}else if(jobStatusheader == -3){
					jobStatusheaderName = "Over Budget";
				}
				var urijobname=encodeBigurl(jobDescription);
				var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + urijobname +"&jobStatus=" + jobStatusheaderName+"&joMasterID="+joMasterId;
				createtpusage('job-Main Tab','Update Job','Info','Job,Main Tab,Updating Job,JobNumber:'+jobNumber);
				var checkpermission=getGrantpermissionprivilage('Main',0);
				if(globalVar=='0'){
				if(checkpermission){
				document.location.href = "./jobflow?token=view&" + aQryStr;
				}
				}
			}
	   });
	}
	$(".requiredSave").show();
	return true;
}

function successDialog(){
	var sucess = "Job successfully created";

jQuery(newDialogDiv).attr("id","msgDlg");
jQuery(newDialogDiv).html('<span><b style="color: #45BF07;">'+sucess+'</b></span>');
jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Alert.",
		open: function(event, ui){setTimeout("$('#msgDlg').dialog('close')",1000);}});
return true;
}


/**--- Ajax auto suggest functions ---*/
$(function() { var cache = {}; var lastXhr='';
$( "#jobMain_salesRepsList" ).autocomplete({ minLength: 1,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesRepId").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_CSRList" ).autocomplete({ minLength: 1, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#CSRId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_SalesMgrList" ).autocomplete({ minLength: 1,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#salesMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_EngineersList" ).autocomplete({ minLength: 1,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#engineerId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobHeader_JobCustomerName_id" ).autocomplete({
		minLength: 2, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerId").val(id);
		//Set the sales man and csr based upon customer .Call to job controller java
		$.ajax({
			url: './job_controller/Customerbaseddropdowns',
			type : 'POST',
			data: {'id':id },
			success: function (data){
				if(data!=null){
				if(data.cuAssignmentId0!=null && data.cuAssignmentId0!=""){
						$("#salesRepId").val(data.cuAssignmentId0);
						$( "#jobMain_salesRepsList" ).val(data.salesrep);
						
				}
				if(data.cuAssignmentId1!=null && data.cuAssignmentId1!=""){
					$("#CSRId").val(data.cuAssignmentId1);	
					$( "#jobMain_CSRList" ).val(data.csr);
				}
				
				if(data.cuAssignmentId2!=null && data.cuAssignmentId2!=""){
					$("#salesMgrId").val(data.cuAssignmentId2);	
					$( "#jobMain_SalesMgrList" ).val(data.salesmgr);
				}
				if(data.cuAssignmentId3!=null && data.cuAssignmentId3!=""){
					$("#engineerId").val(data.cuAssignmentId3);	
					$( "#jobMain_EngineersList" ).val(data.engineer);
				}
				if(data.cuAssignmentId4!=null && data.cuAssignmentId4!=""){
					$("#prjMgrId").val(data.cuAssignmentId4);	
					$( "#jobMain_PrjMgrList" ).val(data.projectmgr);
				}
				
				if(data.defaultTaxTerritory == 1)
				{
									$("#jobMain_TaxTerritory").val("");
									$("#TaxTerritory").val("");
									$("#TaxValue").html("0.00%");
					if(data.coTaxID!=null && data.coTaxID != "-1")
						{
									$("#jobMain_TaxTerritory").val(data.taxName);
									$("#TaxTerritory").val(data.coTaxID);
									$("#TaxValue").html(data.taxRate);
						}
				}
				
				}
			}
		});
		},
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
	$( "#jobMain_PrjMgrList" ).autocomplete({ minLength: 1,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#prjMgrId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_TakeOffList" ).autocomplete({ minLength: 1,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#takeOffId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_QuoteByList" ).autocomplete({ minLength: 1, timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#quoteId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "employeeCrud/quotes", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_engineersRXList" ).autocomplete({ minLength: 2,timeout :1000, clearcache : true,
		open: function(){ 
			$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="javascript:openAddEngineer()" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
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
		$( "#jobMain_architectsList" ).autocomplete({ minLength: 2,timeout :1000,
			open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="javascript:openAddArchitect()" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},
			select: function( event, ui ) { var id = ui.item.id; $("#architectId").val(id); },
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
		$( "#jobMain_GCList" ).autocomplete({ minLength: 2,timeout :1000,
			open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="javascript:openAddContractor()" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},
			select: function(event, ui){var id = ui.item.id; $("#GCId").val(id);},
			source: function(request, response) {var term = request.term;
				if ( term in cache ) { response( cache[ term ] );return;}
				lastXhr = $.getJSON("companycontroller/GCRXList", request, function( data, status, xhr ) { cache[ term ] = data; if(xhr === lastXhr){response(data);}});
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}
		}); 
	});
	
	

  $(function() { var cache = {}; var lastXhr='';
	$( "#jobMain_TaxTerritory" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#TaxTerritory").val(id); var tax = ui.item.taxValue; $("#TaxValue").val(tax); $('#TaxValue').empty(); $("#TaxValue").append(tax+"%");},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

  $(function() { var cache = {}; var lastXhr=''; $( "#locationCityID" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationStateID").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
  $(function() { var cache = {}; var lastXhr=''; $( "#locationCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#locationState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
  $(function() { var cache = {}; var lastXhr=''; $( "#city" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#state").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
  $(function() { var cache = {}; var lastXhr=''; $( "#contractorCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#contractorState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
  
  function deleteJob(){
	  jQuery(newDialogDiv).html('<span><b style="color: red;">Delete this Job Record?</b></span>');
	  jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
	 buttons:{
		"Submit": function(){
			jQuery(this).dialog("close");
			$('#loadingMainDiv').css({"visibility": "visible"});
			var aJoMasterID = $("#joMasterHiddenID").val();
			  var aJobNumber = $("#jobNumberHiddenID").val();
			  $.ajax({
				url: "./job_controller/deletejob",
				type: "POST",
				data : { 'joMasterID' : aJoMasterID, 'jobNumber' : aJobNumber },
				success: function(data) {
					$('#loadingMainDiv').css({"visibility": "hidden"});
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:green;">Job is Deleted Sucessfully.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message",
						buttons: [{height:35,text: "OK",click: function() {  $(this).dialog("close");  
						document.location.href = "./home";}}]}).dialog("open");
					return true;
				}
		  });
		},
		Cancel: function ()	{jQuery(this).dialog("close");} } }).dialog("open");
  }
  
  function salesmanAdminValidation(aSalesMan){
	  if(jobStatus === "Booked"){
//		  if(aUserAdmin !== '1'){
//			  jQuery(newDialogDiv).html('<span style="color:#FF0066;">You are not authorized to modify this field.  Admin only can change.</span>');
//			  jQuery(newDialogDiv).dialog({modal: true, title:"Booked Job Alert",width:400, 
//				  buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); $("#jobMain_salesRepsList").val($("#jobMain_salesRepsListHidden").val()); document.getElementById('jobMain_salesRepsList').disabled=true; }}]}).dialog("open");
//		  }else{
			  return aSalesMan;
		  }
//	  }
	}
  
  function jobSiteAdminValidation(aJobSite){
	  if(jobStatus === "Booked"){
//		  if(aUserAdmin !== '1'){
//			  jQuery(newDialogDiv).html('<span style="color:#FF0066;">You are not authorized to modify this field.  Admin only can change.</span>');
//			  jQuery(newDialogDiv).dialog({modal: true, title:"Booked Job Alert",width:400, 
//				  buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); $("#jobMain_TaxTerritory").val($("#jobMain_TaxTerritoryHidden").val()); document.getElementById('jobMain_TaxTerritory').disabled=true; }}]}).dialog("open");
//		  }else{
			  return aJobSite;
//		  }
	  }
	}
  
  function addcustomerpo(){
	  	jQuery("#openCustomerPODia").dialog("open");
	  	return true;
  }
  
  jQuery(function(){
		jQuery("#openCustomerPODia").dialog({
			autoOpen:false,
			width:475,
			title:"Customer PO",
			modal:true,
			buttons:{	},
			close:function(){$('#openCustomerPOForm').validationEngine('hideAll');
			return true;}	
		});
	}); 
  
  function POAmountCalculation(){
	  if(!$('#openCustomerPOForm').validationEngine('validate')) {
			return false;
	  }
	  var totalContractAmount=$("#totalContractamount").val().replace(/[^0-9\.]+/g,"");
	  var poAmount2=$("#po_amount2").val().replace(/[^0-9\.]+/g,"");
	  var poAmount3=$("#po_amount3").val().replace(/[^0-9\.]+/g,"");
	  var poAmount4=$("#po_amount4").val().replace(/[^0-9\.]+/g,"");
	  var poAmount5=$("#po_amount5").val().replace(/[^0-9\.]+/g,"");
	  var poAmount6=$("#po_amount6").val().replace(/[^0-9\.]+/g,"");
	  var sumContractAmount= Number(totalContractAmount) - (Number(poAmount2) + Number(poAmount3) + Number(poAmount4) + Number(poAmount5)+ Number(poAmount6));
	  $("#po_amount2").val(formatCurrency(poAmount2));
	  $("#po_amount3").val(formatCurrency(poAmount3));
	  $("#po_amount4").val(formatCurrency(poAmount4));
	  $("#po_amount5").val(formatCurrency(poAmount5));
	  $("#po_amount6").val(formatCurrency(poAmount6));
	  $("#contractAmount").val(formatCurrency(sumContractAmount));
	  return true;
  }

  function removeDollarSymbl(){
	  var poAmount2=$("#po_amount2").val().replace(/[^0-9\.]+/g,"").replace(".00","");
	  var poAmount3=$("#po_amount3").val().replace(/[^0-9\.]+/g,"").replace(".00","");
	  var poAmount4=$("#po_amount4").val().replace(/[^0-9\.]+/g,"").replace(".00","");
	  var poAmount5=$("#po_amount5").val().replace(/[^0-9\.]+/g,"").replace(".00","");
	  var poAmount6=$("#po_amount6").val().replace(/[^0-9\.]+/g,"").replace(".00","");
	  $("#po_amount2").val(poAmount2);
	  $("#po_amount3").val(poAmount3);
	  $("#po_amount4").val(poAmount4);
	  $("#po_amount5").val(poAmount5);
	  $("#po_amount6").val(poAmount6);
	  return true;
  }
  
  function saveMainCustomerPONumber(){
	 /* if(!$('#openCustomerPOForm').validationEngine('validate')) {
			return false;
		}*/
		/*var aOrderDetails = new Array();
		aOrderDetails.push($("#covered_material1").val());
		aOrderDetails.push($("#po_number1").val());
		aOrderDetails.push($("#contractAmount").val().replace(/[^0-9\.]+/g,""));
		
		aOrderDetails.push($("#covered_material2").val());
		aOrderDetails.push($("#po_number2").val());
		aOrderDetails.push($("#po_amount2").val().replace(/[^0-9\.]+/g,""));
		
		aOrderDetails.push($("#covered_material3").val());
		aOrderDetails.push($("#po_number3").val());
		aOrderDetails.push($("#po_amount3").val().replace(/[^0-9\.]+/g,""));
		
		aOrderDetails.push($("#covered_material4").val());
		aOrderDetails.push($("#po_number4").val());
		aOrderDetails.push($("#po_amount4").val().replace(/[^0-9\.]+/g,""));
		
		aOrderDetails.push($("#covered_material5").val());
		aOrderDetails.push($("#po_number5").val());
		aOrderDetails.push($("#po_amount5").val().replace(/[^0-9\.]+/g,""));
		
		aOrderDetails.push($("#covered_material6").val());
		aOrderDetails.push($("#po_number6").val());
		aOrderDetails.push($("#po_amount6").val().replace(/[^0-9\.]+/g,""));
		aOrderDetails.push($("#joMaster_ID").text());*/
		var aRequestObj1 = {
				"covered_material1": $("#covered_material1").val(),
				"po_number1":$("#po_number1").val(),
				"contractAmount":$("#contractAmount").val().replace(/[^0-9\.]+/g,"")
		};
		var aRequestObj2 = {
				"covered_material2": $("#covered_material2").val(),
				"po_number2":$("#po_number2").val(),
				"po_amount2":$("#po_amount2").val().replace(/[^0-9\.]+/g,"")
		};
		var aRequestObj3 = {
				"covered_material3": $("#covered_material3").val(),
				"po_number3":$("#po_number3").val(),
				"po_amount3":$("#po_amount3").val().replace(/[^0-9\.]+/g,"")
		};
		var aRequestObj4 = {
				"covered_material4": $("#covered_material4").val(),
				"po_number4":$("#po_number4").val(),
				"po_amount4":$("#po_amount4").val().replace(/[^0-9\.]+/g,"")
		};
		var aRequestObj5 = {
				"covered_material5": $("#covered_material5").val(),
				"po_number5":$("#po_number5").val(),
				"po_amount5":$("#po_amount5").val().replace(/[^0-9\.]+/g,"")
		};
		var aRequestObj6 = {
				"covered_material6": $("#covered_material6").val(),
				"po_number6":$("#po_number6").val(),
				"po_amount6":$("#po_amount6").val().replace(/[^0-9\.]+/g,"")
		};
		var aCustomerPOReqObj = {"aRequestObj1":aRequestObj1, "aRequestObj2":aRequestObj2,"aRequestObj3":aRequestObj3,"aRequestObj4":aRequestObj4,"aRequestObj5":aRequestObj5,"aRequestObj6":aRequestObj6,"joMasterID":$("#joMaster_ID").text()};
		$.ajax({
			url: "./jobtabs1/saveCustomerPODetails",
			type: "POST",
			data : {'aCustomerPOReqObj':JSON.stringify(aCustomerPOReqObj)},
			success: function(data) {
				createtpusage('job-Main Tab','Save Customer PO','Info','Job-Main Tab,Saving Customer PO');
				if(data.oper === 'add'){
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Change Order Details Added.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  $("#openCustomerPODia").dialog("close");  var mainPONumber = $("#po_number1").val(); $("#jobmain_ponumber").val(mainPONumber); location.reload(); }}]}).dialog("open");
					return true;
				}else if(data.oper === 'edit'){
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Change Order Details Updated.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success.", 
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#openCustomerPODia").dialog("close"); var mainPONumber = $("#po_number1").val(); $("#jobmain_ponumber").val(mainPONumber); location.reload(); }}]}).dialog("open");
					return true;
				}
			}
		});
  }
  
  function cancelMainCustomerPONumber(){
	  createtpusage('job-Main Tab','Cancel Customer PO','Info','Job-Main Tab,Cancelling Customer PO');
	  jQuery("#openCustomerPODia").dialog("close");
	  var mainPONumber = $("#po_number1").val();
	  $("#jobmain_ponumber").val(mainPONumber);
	  return true;
	  
  }
  
  function divisionFromLogin(){
		$.ajax({
		    url: './getUserDefaults',
		    type: 'POST',       
		    success: function (data) {
		    	var selected='';
		    	
				 $.each(data, function(key, valueMap) {
					if("divisions"==key)
					{
						console.log('new datd '+valueMap);
						$.each(valueMap, function(index, value){
//							alert(data.divisionID +' :: '+value.coDivisionId);
							 if(data.divisionID==value.coDivisionId){
								 
								 $("#jobMain_Divisions option[value=" + value.coDivisionId + "]").attr("selected", true);
							}
								 
							});
							}
						});  
					}
			});
	}