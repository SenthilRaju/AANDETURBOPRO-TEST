$("#allowId").click(function () {
	$(".userPermission").attr('checked',true);
});

$("#denyId").click(function () {
	$(".userPermission").attr('checked',false);
});

var aUserID = getUrlVars()["userLoginId"];

$.ajax({
	url:'./userlistcontroller/getSysPrivilageChk',
	type: "GET",
	data: {'userLoginID' : aUserID },
	success: function(data){
		$.each(data, function(index, value){
			if(value.accessProcedureId == 1001000){
				$("#homeID").attr("checked", true);
			}if(value.accessProcedureId == 1401000){
				$("#jobQuotesID").attr("checked", true);
			}if(value.accessProcedureId == 1402000){
				$("#jobSubmittalID").attr("checked", true);
			}if(value.accessProcedureId == 1403000){
				$("#jobCreditID").attr("checked", true);
			}if(value.accessProcedureId == 1404000){
				$("#jobReleaseID").attr("checked", true);
			}if(value.accessProcedureId == 1405000){
				$("#jobFinancialID").attr("checked", true);
			}if(value.accessProcedureId == 1406000){
				$("#jobJournalID").attr("checked", true);
			}if(value.accessProcedureId == 1101000){
				$("#salesID").attr("checked", true);
			}if(value.accessProcedureId == 1501000){
				$("#CompanyCustomerID").attr("checked", true);
			}if(value.accessProcedureId == 1501100){
				$("#CompanyPaymentsID").attr("checked", true);
			}if(value.accessProcedureId == 1502000){
				$("#CompanyVendorID").attr("checked", true);
			}if(value.accessProcedureId == 1502100){
				$("#CompanyPayBillID").attr("checked", true);
			}if(value.accessProcedureId == 1503000){
				$("#CompanyEmployeeID").attr("checked", true);
			}if(value.accessProcedureId == 1503100){
				$("#CompanyCommisionsID").attr("checked", true);
			}if(value.accessProcedureId == 1504000){
				$("#CompanyRolodexID").attr("checked", true);
			}if(value.accessProcedureId == 1505000){
				$("#CompanyUserID").attr("checked", true);
			}if(value.accessProcedureId == 1506000){
				$("#CompanySettingID").attr("checked", true);
			}if(value.accessProcedureId == 1507000){
				$("#CompanReportID").attr("checked", true);
			}if(value.accessProcedureId == 1508000){
				$("#CompanyAccountID").attr("checked", true);
			}if(value.accessProcedureId == 1201000){
				$("#projectID").attr("checked", true);
			}if(value.accessProcedureId == 1301000){
				$("#inventoryID").attr("checked", true);
			}
		});
	}
});

function updateUserPermission(){
	var aHome = '';	var aSales = ''; var aProject = ''; var aQuotes = ''; var aSubmittal = ''; var aCredit = ''; var aRelease = ''; var aFinancial = ''; var aJournal = '';
	var aInventory = '';	var aCustomers = '';	var aPayments = '';	var aVendors = '';	var aPayBills = '';	var aEmployee = '';	var aCommission = '';	var aRolodex = '';
	var aUsers = ''; 	var aSettings = '';	var aReports = '';	var aChartsAccount = '';	var aHomeOper = '';	 var aSaleOper = ''; 	var aProjectOper = ''; 	var aCustomerOper = '';
	var aPaymentOper = '';   var aVendorOper = ''; 	var aPayBillOper = '';	var aEmployeeOper = '';var aCommssionOper = '';	var aRolodexOper = '';	var aUserOper = '';	var aSettingOper = '';	var aReportOper = '';
	var aAcountsOper = ''; 	var aQuoteOper = '';	 	var aSubmittalOper = '';		var aCreditOper = '';	var aReleaseOper = '';		var aFinancialOper = '';		var aJournalOper = ''; var aInventoryOper = '';
	var aHomeAddOper = '';	 var aSaleAddOper = ''; 	var aProjectAddOper = ''; 	var aCustomerAddOper = ''; var aPaymentAddOper = '';   var aVendorAddOper = ''; 	var aPayBillAddOper = '';	var aEmployeeAddOper = '';
	var aCommssionAddOper = '';	var aRolodexAddOper = '';	var aUserAddOper = '';	var aSettingAddOper = '';	var aReportAddOper = ''; var aAcountsAddOper = ''; 	var aQuoteAddOper = '';	 
	var aSubmittalAddOper = '';		var aCreditAddOper = '';	var aReleaseAddOper = '';		var aFinancialAddOper = '';		var aJournalAddOper = ''; var aInventoryAddOper = '';
	var aUserloginID = getUrlVars()["userLoginId"];
	if ($('#homeID').is(':checked')){
		aHome = $("#homeAccesProcedureID").val();
		aHomeAddOper = 'homeAdd';
	}else{
		aHome = $("#homeAccesProcedureID").val();
		aHomeOper = 'homeDelete';
	}
	if ($('#salesID').is(':checked')){
		aSales =$("#salesAccesProcedureID").val();
		aSaleAddOper = 'saleAdd';
	}else{
		aSales =$("#salesAccesProcedureID").val();
		aSaleOper = 'salesDelete';
	}
	if ($('#projectID').is(':checked')){
		aProject = $("#projectAccesProcedureID").val();
		aProjectAddOper = 'projectAdd';
	}
	else{
		aProject = $("#projectAccesProcedureID").val();
		aProjectOper = 'projectDelete';
	}
	if ($('#inventoryID').is(':checked')){
		aInventory =$("#inventoryProcedureID").val();
		aInventoryAddOper = 'inventoryAdd';
	}else{
		aInventory =$("#inventoryProcedureID").val();
		aInventoryOper = 'inventoryDelete';
	}
	if ($('#jobQuotesID').is(':checked')){
		aQuotes = $("#jobQuoteAccesProcedureID").val();
		aQuoteAddOper = 'jobQuoteAdd';
	}else{
		aQuotes = $("#jobQuoteAccesProcedureID").val();
		aQuoteOper = 'jobQuoteDelete';
	}
	if ($('#jobSubmittalID').is(':checked')){
		aSubmittal =$("#jobSubmittalAccesProcedureID").val();
		aSubmittalAddOper = 'jobSubmittalAdd';
	}else{
		aSubmittal =$("#jobSubmittalAccesProcedureID").val();
		aSubmittalOper = 'jobSubmittalDelete';
	}
	if ($('#jobCreditID').is(':checked')){
		aCredit =$("#jobCreditAccesProcedureID").val();
		aCreditAddOper = 'jobCreditAdd';
	}else{
		aCredit =$("#jobCreditAccesProcedureID").val();
		aCreditOper = 'jobCreditDelete';
	}
	if ($('#jobReleaseID').is(':checked')){
		aRelease =$("#jobReleaseAccesProcedureID").val();
		aReleaseAddOper = 'jobReleaseAdd';
	}else{
		aRelease =$("#jobReleaseAccesProcedureID").val();
		aReleaseOper = 'jobReleaseDelete';
	}
	if ($('#jobFinancialID').is(':checked')){
		aFinancial =$("#jobFinancialAccesProcedureID").val();
		aFinancialAddOper = 'jobFinancialAdd';
	}else{
		aFinancial =$("#jobFinancialAccesProcedureID").val();
		aFinancialOper = 'jobFinancialDelete';
	}
	if ($('#jobJournalID').is(':checked')){
		aJournal =$("#jobJournalAccesProcedureID").val();
		aJournalAddOper = 'jobJournalAdd';
	}else{
		aJournal =$("#jobJournalAccesProcedureID").val();
		aJournalOper = 'jobJournalDelete';
	}
	if ($('#CompanyCustomerID').is(':checked')){
		aCustomers =$("#companyCustomerAccesProcedureID").val();
		aCustomerAddOper = 'customerAdd';
	}else{
		aCustomers =$("#companyCustomerAccesProcedureID").val();
		aCustomerOper = 'customerDelete';
	}
	if ($('#CompanyPaymentsID').is(':checked')){
		aPayments =$("#companyPaymentAccesProcedureID").val();
		aPaymentAddOper = 'paymentsAdd';
	}else{
		aPayments =$("#companyPaymentAccesProcedureID").val();
		aPaymentOper = 'paymentsDelete';
	}
	if ($('#CompanyVendorID').is(':checked')){
		aVendors =$("#companyVendorAccesProcedureID").val();
		aVendorAddOper = 'vendorAdd';
	}else{
		aVendors =$("#companyVendorAccesProcedureID").val();
		aVendorOper = 'vendorDelete';
	}
	if ($('#CompanyPayBillID').is(':checked')){
		aPayBills =$("#companyPayBillAccesProcedureID").val();
		aPayBillAddOper = 'payBillAdd';
	}else{
		aPayBills =$("#companyPayBillAccesProcedureID").val();
		aPayBillOper = 'payBillDelete';
	}
	if ($('#CompanyEmployeeID').is(':checked')){
		aEmployee =$("#companyEmployeeAccesProcedureID").val();
		aEmployeeAddOper = 'employeeAdd';
	}else{
		aEmployee =$("#companyEmployeeAccesProcedureID").val();
		aEmployeeOper = 'employeeDelete';
	}
	if ($('#CompanyCommisionsID').is(':checked')){
		aCommission =$("#companyCommisionsAccesProcedureID").val();
		aCommssionAddOper = 'commissionsAdd';
	}else{
		aCommission =$("#companyCommisionsAccesProcedureID").val();
		aCommssionOper = 'commissionsDelete';
	}
	if ($('#CompanyRolodexID').is(':checked')){
		aRolodex =$("#companyRolodexAccesProcedureID").val();
		aRolodexAddOper = 'rolodexAdd';
	}else{
		aRolodex =$("#companyRolodexAccesProcedureID").val();
		aRolodexOper = 'rolodexDelete';
	}
	if ($('#CompanyUserID').is(':checked')){
		aUsers =$("#companyUserAccesProcedureID").val();
		aUserAddOper = 'userAdd';
	}else{
		aUsers =$("#companyUserAccesProcedureID").val();
		aUserOper = 'userDelete';
	}
	if ($('#CompanySettingID').is(':checked')){
		aSettings =$("#companySettingAccesProcedureID").val();
		aSettingAddOper = 'settingAdd';
	}else{
		aSettings =$("#companySettingAccesProcedureID").val();
		aSettingOper = 'settingsDelete';
	}
	if ($('#CompanReportID').is(':checked')){
		aReports =$("#companyReportsAccesProcedureID").val();
		aReportAddOper = 'reportAdd';
	}else{
		aReports =$("#companyReportsAccesProcedureID").val();
		aReportOper = 'reportDelete';
	}
	if ($('#CompanyAccountID').is(':checked')){
		aChartsAccount =$("#companyAccountAccesProcedureID").val();
		aAcountsAddOper = 'accountAdd';
	}else{
		aChartsAccount =$("#companyAccountAccesProcedureID").val();
		aAcountsOper = 'accountDelete';
	}
	
	$.ajax({
		url: "userlistcontroller/updateUserPermissions",
		type: "POST",
		async: false,
		data : "&homeName=" +aHome+"&salesName="+aSales+"&projectName="+aProject+"&inventoryName="+aInventory+"&jobQuotesName="+aQuotes+"&jobSubmittalName="+aSubmittal+
					"&jobCreditName="+aCredit+"&jobReleaseName="+aRelease+"&jobFinancialName="+aFinancial+"&jobJournalName="+aJournal+"&CompanyCustomerName="+aCustomers+"&CompanyPaymentsName="+aPayments+"&CompanyVendorName="+aVendors+
					"&CompanyPayBillName"+aPayBills+"&CompanyEmployeeName="+aEmployee+"&CompanyCommisionsName="+aCommission+"&CompanyRolodexName="+aRolodex+"&CompanyUserName="+aUsers+"&CompanySettingName="+aSettings+
					"&CompanyReplortName="+aReports+"&CompanyAccountName="+aChartsAccount+"&homeOperDelete="+aHomeOper+"&salesOperDelete="+aSaleOper+"&projectOperDelete="+aProjectOper+"&inventoryOperDelete="+aInventoryOper+
					"&quoteOperDelete="+aQuoteOper+"&submittalOperDelete="+aSubmittalOper+"&creditOperDelete="+aCreditOper+"&releaseOperDelete="+aReleaseOper+"&financialOperDelete="+aFinancialOper+"&jouralOperDelete="+aJournalOper+
					"&customerOperDelete="+aCustomerOper+"&paymentsOperDelete="+aPaymentOper+"&vendorOperDelete="+aVendorOper+"&paybillOperDelete="+aPayBillOper+"&employeeOperDelete="+aEmployeeOper+
					"&commissionOperDelete="+aCommssionOper+"&rolodexOperDelete="+aRolodexOper+"&userOperDelete="+aUserOper+	"&settingOperDelete="+aSettingOper+"&reportOperDelete="+aReportOper+"&chartAccOperDelete="+aAcountsOper+
					"&homeOperAdd="+aHomeAddOper+"&salesOperAdd="+aSaleAddOper+"&projectOperAdd="+aProjectAddOper+"&inventoryOperAdd="+aInventoryAddOper+
					"&quoteOperAdd="+aQuoteAddOper+"&submittalOperAdd="+aSubmittalAddOper+"&creditOperAdd="+aCreditAddOper+"&releaseOperAdd="+aReleaseAddOper+"&financialOperAdd="+aFinancialAddOper+"&jouralOperAdd="+aJournalAddOper+
					"&customerOperAdd="+aCustomerAddOper+"&paymentsOperAdd="+aPaymentAddOper+"&vendorOperAdd="+aVendorAddOper+"&paybillOperAdd="+aPayBillAddOper+"&employeeOperAdd="+aEmployeeAddOper+"&userLoginID="+aUserloginID+
					"&commissionOperAdd="+aCommssionAddOper+"&rolodexOperAdd="+aRolodexAddOper+"&userOperAdd="+aUserAddOper+	"&settingOperAdd="+aSettingAddOper+"&reportOperAdd="+aReportAddOper+"&chartAccOperAdd="+aAcountsAddOper,
		success: function(data) {
			
		}
	});
}

var aUserLoginID = $("#userDiffLoginID").val();
if(aUserLoginID !== '1'){
	$("#systemPermissionLable").append("");
	$("#systemAccessID").show();
}else{
	$("#systemAccessID").css("display", "none");
	$("#systemPermissionLable").append("<table><tr><td  style='font-size: 25px;padding-top: 170px; padding-left: 160px;'><label style='color: green;'>System Admin has permission to<br> all areas of the system.</label></td></tr></table>");
}
		