var errorMessage = new Array();
var withorwithoutcreditamt=false;
var AllowreleasesifFinalWaiverchecked=false;
var AllowEditingofSentQuotes=false;
jQuery(document).ready(function(){
	var jobtype = $('#jobType_ID').text();
	var myLink = $('#jobreleasetab').find('a');
		if(jobtype=='quick'){
			//$("#tabs_main_job ul>li :eq(0)").trigger('click');
			// $("#jobreleasetab").trigger("click");
			// $('#tabs_main_job').tabs('select', 4);
			// $("#tabs_main_job").tabs( "load" , 4);
			//$("#jobreleasetab").trigger("click");
			
			//$("#tabs_main_job" ).tabs({ active: 4 });
			//$("#tabs_main_job ul>li a").trigger('click'); 
			
			//$('#tabs_main_job').tabs({selected: 4});
			//$("#tabs_main_job ul>li :eq(4)").trigger('click'); 
		}
				
	var isAdmin = $('#isAdmin').text();
	console.log($('#jobStatus_ID').text() + " " + isAdmin);
	var status =  $('#jobStatus_ID').text() ;
	/* Commented By Jenith for Bartos_Action List item: "Allow Non-Admin To Close Jobs"
	 * if(isAdmin === "false" && status === "3" ) {
		$('#jobStatusList').prop('disabled', true);
		$('#jobStatusList').attr('title', 'Only Administrators can change the Booked Status jobs.');
	}*/
		
	$(".datepicker").datepicker();
	$("#changeStatusId").hide();
//	var jobName = $("#jobName_ID").text();
	$("#tabs_main_job").tabs({
		cache: true,
		ajaxOptions: {
			data:{
				joMasterID: $("#joMaster_ID").text(),
				jobNumber: $("#jobNumber_ID").text(),
				jobName: $("#jobName_ID").text(),
				jobCustomer:$("#jobCustomerName_ID").text()
			},
			error: function(xhr, status, index, anchor) {
				$(anchor.hash).html("<div align='center' style='height: 350px;padding-top: 200px;'>"+
						"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>" + 
							"Sorry, Couldn't load this tab. Please try again later."+
						"</label></div>");
			}
		},
		load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
		select: function (e, ui) {
			//window.location.hash = ui.tab.hash;
			var $panel = $(ui.panel);
			if ($panel.is(":empty")) {
				$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #DDBA82'><img src='./../resources/scripts/jquery-autocomplete/loading11.gif'></div>");
			}
		}
	});
	$("#textarea").css("display","none");

	// Added By Zenith for Booked Jobs
	var newJobStatus = $('#jobStatus_ID').text();
	//alert(newJobStatus);
	if(newJobStatus == '3') {
		console.log("Condition Correct: "+newJobStatus);
		$("#jobStatusList option[value='0']").prop("disabled",true);
		$("#jobStatusList option[value='3']").attr("disabled",true);
		$("#jobStatusList option[value='-4']").attr("disabled",true);
		$("#jobStatusList option[value='6']").attr("disabled",true);
		$("#jobStatusList option[value='1']").attr("disabled",true);
		$("#jobStatusList option[value='5']").attr("disabled",true);
		$("#jobStatusList option[value='2']").attr("disabled",true);
		$("#jobStatusList option[value='-1']").attr("disabled",true);
		$("#jobStatusList option[value='-2']").attr("disabled",true);
		$("#jobStatusList option[value='-3']").attr("disabled",true);
    }
	if(newJobStatus == '4') {
		console.log("Condition Correct: "+newJobStatus);
		$("#jobStatusList option[value='0']").prop("disabled",true);
		$("#jobStatusList option[value='4']").attr("disabled",true);
		$("#jobStatusList option[value='-4']").attr("disabled",true);
		$("#jobStatusList option[value='6']").attr("disabled",true);
		$("#jobStatusList option[value='1']").attr("disabled",true);
		$("#jobStatusList option[value='5']").attr("disabled",true);
		$("#jobStatusList option[value='2']").attr("disabled",true);
		$("#jobStatusList option[value='-1']").attr("disabled",true);
		$("#jobStatusList option[value='-2']").attr("disabled",true);
		$("#jobStatusList option[value='-3']").attr("disabled",true);
    }
	/*jobSettingscheckboxStatus*/
	//getJobSettingStatus(systemconstant variable)
	getJobSettingStatus("AllowbookingJobswithnoContractAmount");
	getJobSettingStatus("AllowreleasesifFinalWaiverchecked");
	getJobSettingStatus("AllowEditingofSentQuotes");
});


	
function openWebpagePopup(){
	var path=$("#webpageurltoopen").val();
	window.open("http://turbo.dsdar.com/turbotracker/uattest.jsp?path="+path);
//	try{
//	 var newDialogDiv = jQuery(document.createElement('div'));	
//		jQuery(newDialogDiv).html(
//				'<span><input type = "text" name="geturl" id="url"></input></span><br/>'+
//				'<span style="color:red; id="errorSpan"><div id="errormsg" value="Please Enter URL"></div></span>');
//		
//		jQuery(newDialogDiv).dialog({
//			modal : true,
//			width : 500,
//			height : 250,
//			title : "Enter WEb Page Url",
//			buttons : [ {
//				height : 35,
//				text : "Open",
//				click : function() {
//					var geturl = $('#url').val();
//					if(geturl==''){
//						alert('Enter URL');
//					}else{
//						window.location.href = geturl;
//					}
//					
//				}
//			},{
//				height : 35,
//				text : "cancel",
//				click : function() {				
//					$(this).dialog("close");		
//					
//				}
//			} ]
//		}).dialog("open");
//	}catch(err){alert(err.message);}
}
function ChangeJobStatus(){
	globalVar ='1';
	aUserLoginID = $("#userLoginID").val();
	if(aUserLoginID !== '1'){
	}
	var date = new Date();
	var month = date.getMonth()+1;
	var aDate = "";
	aDate = aDate + month + "/" + date.getDate() + "/" + date.getFullYear();
	var newJobStatus = $('#jobStatusList').val();
	var jobNumber = $('.jobHeader_JobNumber').val();
	var dateType = '';
	if(newJobStatus === "3"){
		
		errorMessage=[];
	/*	var aTax = $("#jobMain_TaxTerritory").val();
		var aSalesRep = $("#jobMain_salesRepsList").val();
		var aEstimate = $("#estomattedHiddenID").val();
		
		var errorTax = "'Tax Territory'";
		var errorSales = "'Salesman'";
		var errorEstim = "'Estimated Cost' in Quote page";
		var errorText = "'Tax Territory' and 'Salesman' and 'Estimated Cost' in Quote page";
		var errorSaTa = "'Tax Territory' and 'Salesman'";
		var errorSaEs = "'Salesman' and 'Estimated Cost' in Quote page";
		var errorTaEs = "'Tax Territory' and 'Estimated Cost' in Quote page";
		var errorTextCust = "'Customer'";*/
//		jQuery(newDialogDiv).attr("id","msgDlg");
		var newDialogDiv = jQuery(document.createElement('div'));
		var aTax = $("#jobMain_TaxTerritory").val();
		var aSalesRep = $("#jobMain_salesRepsList").val();
		var aEstimate = $("#estimatedCost").val();
		if(aEstimate!=undefined){
		aEstimate = aEstimate.replace(/[^0-9\.]+/g, "").replace("0.00", "");
		}else{
			errorTextEstim = "'Contract Amount' and 'Estimated Cost' in Quote page";
		}
		var jobContractAmount = $("#contractAmount").val();
		if(jobContractAmount!=undefined){
		jobContractAmount = jobContractAmount.replace(/[^0-9\.]+/g, "").replace("0.00", "");
		}
		else{
			errorTextEstim = "'Contract Amount' and 'Estimated Cost' in Quote page";
		}
		var errorText = "'Tax Territory'";
		var errorTextSales = "'Salesman'";
		var errorTextEstim = "'Contract Amount' and 'Estimated Cost' in 'Quote' page";
		var errorTextCust = "'Customer'";
		var errorTextEng = "'Engineer'";
		var statusName = '';
		var customerPONumber = "'Customer PO' in Main Tab";
		if(aTax === ''){
			errorMessage.push(errorText);
		}
		if(aSalesRep === ''){
			errorMessage.push(errorTextSales);
		}
		/*if(aEstimate === '0.0000' || jobContractAmount === '0.0000'){
			errorMessage.push(errorTextEstim);
		}*/
		var CustomerPOsysvariable=getSysvariableStatusBasedOnVariableName('CustomerPOReqYN');
		if((CustomerPOsysvariable!=null && CustomerPOsysvariable[0].valueLong==1) && $('#jobmain_ponumber').val()==''){
			errorMessage.push(customerPONumber);
			}
		
		if(!withorwithoutcreditamt){
		if(aEstimate === '' || jobContractAmount === '' || aEstimate === '0.0000' || jobContractAmount === '0.0000'){
			
			errorMessage.push(errorTextEstim);
		}
		}
		
		if($(".customerNameField").val() === ''){
			errorMessage.push(errorTextCust);
		}else if($("#jobHeader_JobCustomerName_id").val() === ''){
			errorMessage.push(errorTextCust);
		}
		var aEngineer = $("#jobMain_engineersRXList").val();
		var Engineersysvariable=getSysvariableStatusBasedOnVariableName('RequireaEngineerwhenbookingJobs');
		if(Engineersysvariable!=null && Engineersysvariable[0].valueLong==1){
			if(aEngineer === ''){
				errorMessage.push(errorTextEng);
			}
		}
		
		
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
		
		if(errorMessage.length != 0){
			var status = getUrlVars()["jobStatus"];
			if(status === "Bid"){
				statusName = '0';
			}else if(status === 'Planning'){
				statusName = "-4";
			}else if(status === 'Budget'){
				statusName = "6";
			}else if(status === 'Quote'){
				
				statusName = "1";
			}else if(status === 'Submitted'){
				statusName = "5";
			}else if(status === 'Booked'){
				
				statusName = "3";
			}else if(status === 'Closed'){
				statusName = "4";
			}else if(status === 'Lost'){
				statusName = "2";
			}else if(status === 'Abandoned'){
				statusName = "-1";
			}else if(status === 'Rejected'){
				statusName = "-2";
			}else if(status === 'Over Budget'){
				statusName = "-3";
			}
			$("#jobStatusList option[value=" + statusName + "]").attr("selected", true);
			return false;
		}
		/*Added By Velmurugan 
		 *Date:23/2/2015
		 *For:if checked in job settings tab then have to change the status approved in credit tab
		 *Bartos ID :274  
		 * */
		//Start
		ChangeCreditStatusasDefault();
		//End
	}
	
	if(newJobStatus === "3"){
		dateType = 'BookedDate';
	} else if(newJobStatus === "4"){
		dateType = 'ClosedDate';
	}else if(newJobStatus === '5'){
		dateType = 'Submitted';
	}/*else if(newJobStatus === '-1'){
		dateType = 'Abandoned';
	}else if(newJobStatus === '-2'){
		dateType = 'Rejected';
	}*/
	
	jobMainForm_save();
	var joMasterID=getUrlVars()["joMasterID"];
	//
	var aQryStr = "joMasterID="+joMasterID+"&jobNumber=" + jobNumber + "&jobStatus=" + newJobStatus + "&dateType=" + dateType + "&date=" + aDate;
	createtpusage('job-Main Tab','Job Status Change','Info','Job,Main Tab,Job Status Change,JobNumber:'+jobNumber+'jobStatus=' + newJobStatus);
	$.ajax({
		url: 'job_controller/jobStatus',
		mtype : 'POST',
        data: aQryStr,
		success: function (result) {
			var jobNumber = getUrlVars()["jobNumber"];
			var jobName = getUrlVars()["jobName"];
//			var jobCustomer = getUrlVars()["jobCustomer"];
			var jobStatus = newJobStatus;
			var jobStatusName = "";
			if(jobStatus === '0'){
				jobStatusName = "Bid";
			}else if(jobStatus === '-4'){
				jobStatusName = "Planning";
			}else if(jobStatus === '6'){
				jobStatusName = "Budget";
			}else if(jobStatus === '1'){
				jobStatusName = "Quote";
			}else if(jobStatus === '5'){
				jobStatusName = "Submitted";
			}else if(jobStatus === '3'){
				jobStatusName = "Booked";
				jobNumber = result;
			}else if(jobStatus === '4'){
				jobStatusName = "Closed";
			}else if(jobStatus === '2'){
				jobStatusName = "Lost";
			}else if(jobStatus === '-1'){
				jobStatusName = "Abandoned";
			}else if(jobStatus === '-2'){
				jobStatusName = "Rejected";
			}else if(jobStatus === '-3'){
				jobStatusName = "Over Budget";
			}
			
			$(".jobHeader_JobNumber").val(jobNumber);
			//alert("./jobflow?token=view&jobNumber="+jobNumber+"&jobName="+jobName+ "&jobStatus="+jobStatusName+"&jobType=");
			 var checkpermission=getGrantpermissionprivilage('Customers',0);
				if(checkpermission){
					var urijobname=encodeBigurl(jobName);
					document.location.href = "./jobflow?token=view&joMasterID="+joMasterID+"&jobNumber="+jobNumber+"&jobName="+urijobname+ "&jobStatus="+jobStatusName+"&jobType=";
				}
		}
	});
	return 0;
}

function saveJob() {
	$(".requiredSave").show();
	$("#jobStatusField").val("Bid");
}

function customFormatDate(date) {
	/*2003-02-18 00:00:00.0 ----------- YYYY-mm-dd*/
	if(date === ""){
		return "";
	}
	var aDateArr1 = date.split(" ");
	if(aDateArr1.length > 2){
		date = parseSpecializedDate();
		aDateArr1 = date.split(" ");
	}
	var aDateArr2 = aDateArr1[0].split("-");
	var newDate = aDateArr2[1] + "/" + aDateArr2[2] + "/" + aDateArr2[0];
	return newDate;
}
	
function customFormatDateTime(date) {
	/*2003-02-18 00:00:00.0 ----------- YYYY-mm-dd*/
	var newDate;
	if(date === ""){
		return "";
	}
	var aDateArr1 = date.split(" ");
	if(aDateArr1.length > 2){
		date = parseSpecializedDate(date);
		aDateArr1 = date.split(" ");
	}
	var aDateArr2 = aDateArr1[0].split("-");
	if(aDateArr1[1] !== undefined){
		var aTimeArr = aDateArr1[1].split(":");
		var timestr = " " + aTimeArr[0] + ":" + aTimeArr[1];
		newDate= aDateArr2[1] + "/" + aDateArr2[2] + "/" + aDateArr2[0] + timestr;
		return newDate;
	}
	newDate = aDateArr2[1] + "/" + aDateArr2[2] + "/" + aDateArr2[0];
	return newDate;
	return date;
}
	
/**
 * Function to parse "Wed Sep 18 00:00:00 IST 2013" date format.   CRITICAL one.
 * **/
function parseSpecializedDate(date){
	//Wed Mar 05 00:00:00 IST 2014
	//alert(date);
	var aDate = date;                  //"Wed Sep 19 00:00:00 IST 2013";
	var aDateArray = aDate.split(" ");
	var month=new Array();
	month["Jan"]="01";
	month["Feb"]="02";
	month["Mar"]="03";
	month["Apr"]="04";
	month["May"]="05";
	month["Jun"]="06";
	month["Jul"]="07";
	month["Aug"]="08";
	month["Sep"]="09";
	month["Oct"]="10";
	month["Nov"]="11";
	month["Dec"]="12";
	var monthNumber = month[aDateArray[1]];
	var aDateOldFormat = aDateArray[5] + "-" + monthNumber + "-" + aDateArray[2] + " " + aDateArray[3];
	return aDateOldFormat;
}

$(function() { var cache = {}; var lastXhr = '';
$( "#searchJob" ).autocomplete({ minLength: 3, timeout :1000,
open: function(){ 
	$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./jobflow?token=new" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New</a></b></div>');
	$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
},
select: function (event, ui) {
		var name = ui.item.value;
		var joMasterID=ui.item.id;
		var value = name.replace("[","`");
		var job = value.split("`");
		var jobName = job[0];
		jobName = jobName.replace(/&/g, "``");
		jobName = "`"+jobName+"`";
		var number = job[1];
		var jobNumber = number.replace("]"," ");
		$.ajax({
			url: "./search/searchjobcustomer",
			mType: "GET",
			data : {'jobnumber': jobNumber, 'jobname': jobName,'joMasterID':joMasterID},
			success: function(data){
				$.each(data, function(index, value){
					status = value.jobStatus;
					jobCustomer = value.customerName;
				});
				if(jobCustomer === null) {
					jobCustomer = "";
				}
				updateJobstatus(jobNumber,status,joMasterID);
				var urijobname=encodeBigurl(jobName);
				var uricusname=encodeBigurl(jobCustomer);
				window.location.href = "./jobflow?token=view&joMasterID="+joMasterID+"&jobNumber="+jobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+status+"&jobType="; 
		},
		error: function(Xhr) {
			var errorText = $(Xhr.responseText).find('u').html();
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		}
	});
},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "search/searchjoblist", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
}  }); });

/*Added By Velmurugan 
 *Date:23/2/2015
 *For:if checked in job settings tab then have to change the status approved in credit tab
 *Bartos ID :274  
 * */
//Start
function ChangeCreditStatusasDefault(){
	var jomasterId=$('#joMasterID').val();
	if(jomasterId==""){
		jomasterId=0;
	}
	
	$.ajax({
		url: 'job_controller/getJobCreditStatus',
		async:false,
		mtype : 'GET',
		data: {'jomasterID':jomasterId},
		success: function (result) {
			
		}
	});
}
function getJobSettingStatus(Constantvariablename){
	$.ajax({
		url: 'job_controller/getJobSettingStatus',
		async:false,
		mtype : 'GET',
		data: {'Constantvariablename':Constantvariablename},
		success: function (result) {
			if(result!=null){
				
			if(Constantvariablename=='AllowbookingJobswithnoContractAmount'){
					if(result.valueLong==1){
						withorwithoutcreditamt=true;
					}else{
						withorwithoutcreditamt=false;
					}
			}else if(Constantvariablename=='AllowreleasesifFinalWaiverchecked'){
					if(result.valueLong==1){
						AllowreleasesifFinalWaiverchecked=true;
					}else{
						AllowreleasesifFinalWaiverchecked=false;
					}
			}
			else if(Constantvariablename=='AllowEditingofSentQuotes'){
				if(result.valueLong==1){
					AllowEditingofSentQuotes=true;
				}else{
					AllowEditingofSentQuotes=false;
				}
			}
			
			}else{
				if(Constantvariablename=='AllowbookingJobswithnoContractAmount'){
				withorwithoutcreditamt=false;
				}else if(Constantvariablename=='AllowreleasesifFinalWaiverchecked'){
				AllowreleasesifFinalWaiverchecked=false;
				}else if(Constantvariablename=='AllowEditingofSentQuotes'){
				AllowEditingofSentQuotes=false;
				}
			}
		}
	});
}
function JobTabRedirect(accessprocedurename,redirecturl){
	var checkpermission=getGrantpermissionprivilage(accessprocedurename,0);
	if(checkpermission)
	{
		document.location.href=redirecturl;
	}
	}
//End