/*$("#allowId").click(function () {
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
*/
jQuery(document).ready(function(){
clearallCheckboxes();
LoadPermissionDefaultCheckboxes(getUrlVars()["userLoginId"]);
});
function updateUserPermission(checkboxvalue,accessProcedurename){
	$("#loading").show();
	var UserLoginID=getUrlVars()["userLoginId"];
	var checkedornot=$('input:checkbox[id='+checkboxvalue+']').is(':checked');//$("#"+checkboxvalue).is(':checked');
	$.ajax({
    url: "./userlistcontroller/updatesysprivilage",
    data: {'checkedornot':checkedornot,'accessProcedurename':accessProcedurename,'UserLoginID':UserLoginID},
    type: 'POST',
    success: function(data){
    	$("#loading").hide();
    }, error: function(error) {
		$("#loading").hide();
	}
});
}
function LoadPermissionDefaultCheckboxes(UserLoginID){
	$.ajax({
        url: "./userlistcontroller/GetGroupDefaultPermission",
        data: {'UserLoginID':UserLoginID},
        type: 'GET',
        success: function(data){
        	for(var i=0;i<data.length;i++){
        		checkedcheckboxes(data[i].procedureName,data[i].privilegeValue);
        	}
        }
   });
}

function checkedcheckboxes(procedurename,privilagevalue){
	
	if(procedurename=="New Job"){
		checkboxenableOrdisable('HomenewJobID',privilagevalue);
	}else if(procedurename=="Quick book"){
		checkboxenableOrdisable('HomeQuick_bookID',privilagevalue);
	}
	
	else if(procedurename=="Main"){
		checkboxenableOrdisable('jobMainID',privilagevalue);
	}else if(procedurename=="Quotes"){
		checkboxenableOrdisable('jobQuotesID',privilagevalue);
	}else if(procedurename=="Submittal"){
		checkboxenableOrdisable('jobSubmittalID',privilagevalue);
	}else if(procedurename=="Credit"){
		checkboxenableOrdisable('jobCreditID',privilagevalue);
	}else if(procedurename=="Release"){
		checkboxenableOrdisable('jobReleaseID',privilagevalue);
	}else if(procedurename=="Financial"){
		checkboxenableOrdisable('jobFinancialID',privilagevalue);
	}else if(procedurename=="Journal"){
		checkboxenableOrdisable('jobJournalID',privilagevalue);
	}
	
	
	
	else if(procedurename=="Sales"){
		checkboxenableOrdisable('salesID',privilagevalue);
	}else if(procedurename=="Sales Filter"){
		checkboxenableOrdisable('sales_FilterID',privilagevalue);
	}
	
	else if(procedurename=="Project"){
		checkboxenableOrdisable('projectID',privilagevalue);
	}else if(procedurename=="Project Filter"){
		checkboxenableOrdisable('project_FilterID',privilagevalue);
	}
	
	else if(procedurename=="Customers"){
		checkboxenableOrdisable('CompanyCustomerID',privilagevalue);
	}else if(procedurename=="Payments"){
		checkboxenableOrdisable('CompanyPaymentsID',privilagevalue);
	}else if(procedurename=="Statements"){
		checkboxenableOrdisable('CompanyStatementsID',privilagevalue);
	}else if(procedurename=="Sales Order"){
		checkboxenableOrdisable('CompanySales_OrderID',privilagevalue);
	}else if(procedurename=="Invoice"){
		checkboxenableOrdisable('CompanyInvoiceID',privilagevalue);
	}else if(procedurename=="Finance Changes"){
		checkboxenableOrdisable('CompanyFinance_ChangesID',privilagevalue);
	}else if(procedurename=="Tax Adjustments"){
		checkboxenableOrdisable('CompanyTax_AdjustmentsID',privilagevalue);
	}else if(procedurename=="Credit/Debit Memos"){
		checkboxenableOrdisable('CompanyCreditDebit_MemosID',privilagevalue);
	}
	
		
	else if(procedurename=="Sales Order Template"){
		checkboxenableOrdisable('CompanySales_Order_TemplateID',privilagevalue);
	}else if(procedurename=="Purchase Orders"){
		checkboxenableOrdisable('CompanyPurchase_OrdersID',privilagevalue);
	}else if(procedurename=="Vendors"){
		checkboxenableOrdisable('CompanyVendorID',privilagevalue);
	}else if(procedurename=="Pay Bills"){
		checkboxenableOrdisable('CompanyPay_BillsID',privilagevalue);
	}else if(procedurename=="Invoice & Bills"){
		checkboxenableOrdisable('CompanyInvoice_BillsID',privilagevalue);
	}else if(procedurename=="Employees"){
		checkboxenableOrdisable('CompanyEmployeesID',privilagevalue);
	}else if(procedurename=="Commissions"){
		checkboxenableOrdisable('CompanyCommissionsID',privilagevalue);
	}else if(procedurename=="Rolodex"){
		checkboxenableOrdisable('CompanyRolodexID',privilagevalue);
	}else if(procedurename=="Users"){
		checkboxenableOrdisable('CompanyUsersID',privilagevalue);
	}else if(procedurename=="Settings"){
		checkboxenableOrdisable('CompanySettingsID',privilagevalue);
	}
	
	
	else if(procedurename=="Inventory"){
		checkboxenableOrdisable('inventoryID',privilagevalue);
	}else if(procedurename=="Categories"){
		checkboxenableOrdisable('inventoryCategoriesID',privilagevalue);
	}else if(procedurename=="Warehouses"){
		checkboxenableOrdisable('inventoryWarehousesID',privilagevalue);
	}else if(procedurename=="Receive Inventory"){
		checkboxenableOrdisable('inventoryReceive_InventoryID',privilagevalue);
	}else if(procedurename=="Transfer"){
		checkboxenableOrdisable('inventoryTransferID',privilagevalue);
	}else if(procedurename=="Order Points"){
		checkboxenableOrdisable('inventoryOrder_PointsID',privilagevalue);
	}else if(procedurename=="Inventory Value"){
		checkboxenableOrdisable('inventoryInventory_ValueID',privilagevalue);
	}else if(procedurename=="Count"){
		checkboxenableOrdisable('inventoryCountID',privilagevalue);
	}else if(procedurename=="Adjustments"){
		checkboxenableOrdisable('inventoryAdjustmentsID',privilagevalue);
	}else if(procedurename=="Banking"){
		checkboxenableOrdisable('BankingID',privilagevalue);
	}else if(procedurename=="Transactions"){
		checkboxenableOrdisable('inventoryTransactionsID',privilagevalue);
	}
	
	else if(procedurename=="Write Checks"){
		checkboxenableOrdisable('BankingWrite_ChecksID',privilagevalue);
	}else if(procedurename=="Reissue Checks"){
		checkboxenableOrdisable('BankingReissue_ChecksID',privilagevalue);
	}else if(procedurename=="Recouncile Accounts"){
		checkboxenableOrdisable('BankingRecouncile_AccountsID',privilagevalue);
	}else if(procedurename=="Chart Accounts"){
		checkboxenableOrdisable('FinancialChart_AccountsID',privilagevalue);
	}else if(procedurename=="Divisions"){
		checkboxenableOrdisable('FinancialDivisionsID',privilagevalue);
	}else if(procedurename=="Tax Territories"){
		checkboxenableOrdisable('FinancialTax_TerritoriesID',privilagevalue);
	}else if(procedurename=="General Ledger"){
		checkboxenableOrdisable('FinancialGeneral_LedgerID',privilagevalue);
	}else if(procedurename=="Journal Entries"){
		checkboxenableOrdisable('FinancialJournal_EntriesID',privilagevalue);
	}else if(procedurename=="Accounting Cycles"){
		checkboxenableOrdisable('FinancialAccounting_CyclesID',privilagevalue);
	}else if(procedurename=="GL Transactions"){
		checkboxenableOrdisable('FinancialGL_TransactionsID',privilagevalue);
	}else if(procedurename=="Open Period Posting Only"){
		checkboxenableOrdisable('FinancialOpenPeriod_PostingID',privilagevalue);
	}
		
	
	
		
}
function checkboxenableOrdisable(id,privilagevalue){
	if(privilagevalue==1){
		$('input:checkbox[id='+id+']').attr("checked", true);	
		//$("#"+id).attr("checked", true);	
	}else{
		$('input:checkbox[id='+id+']').attr("checked", false);
		//$("#"+id).attr("checked", false);
	}
}

function clearallCheckboxes(){
	$("#HomenewJobID").attr("checked", false);
	$("#HomeQuick_bookID").attr("checked", false);
	$("#jobMainID").attr("checked", false);
	$("#jobQuotesID").attr("checked", false);
	$("#jobSubmittalID").attr("checked", false);
	$("#jobCreditID").attr("checked", false);
	$("#jobReleaseID").attr("checked", false);
	$("#jobFinancialID").attr("checked", false);
	$("#jobJournalID").attr("checked", false);
	$("#salesID").attr("checked", false);
	$("#sales_FilterID").attr("checked", false);
	$("#projectID").attr("checked", false);
	$("#project_FilterID").attr("checked", false);
	$("#CompanyCustomerID").attr("checked", false);
	$("#CompanyPaymentsID").attr("checked", false);
	$("#CompanyStatementsID").attr("checked", false);
	$("#CompanySales_OrderID").attr("checked", false);
	$("#CompanyInvoiceID").attr("checked", false);
	$("#CompanyFinance_ChangesID").attr("checked", false);
	$("#CompanyTax_AdjustmentsID").attr("checked", false);
	$("#CompanyCreditDebit_MemosID").attr("checked", false);
	$("#CompanySales_Order_TemplateID").attr("checked", false);
	$("#CompanyVendorID").attr("checked", false);
	$("#CompanyPurchase_OrdersID").attr("checked", false);
	$("#CompanyPay_BillsID").attr("checked", false);
	$("#CompanyInvoice_BillsID").attr("checked", false);
	$("#CompanyEmployeesID").attr("checked", false);
	$("#CompanyCommissionsID").attr("checked", false);
	$("#CompanyRolodexID").attr("checked", false);
	$("#CompanyUsersID").attr("checked", false);
	$("#CompanySettingsID").attr("checked", false);
	$("#inventoryID").attr("checked", false);
	$("#inventoryCategoriesID").attr("checked", false);
	$("#inventoryWarehousesID").attr("checked", false);
	$("#inventoryReceive_InventoryID").attr("checked", false);
	$("#inventoryTransferID").attr("checked", false);
	$("#inventoryOrder_PointsID").attr("checked", false);
	$("#inventoryInventory_ValueID").attr("checked", false);
	$("#inventoryCountID").attr("checked", false);
	$("#inventoryAdjustmentsID").attr("checked", false);
	$("#BankingID").attr("checked", false);
	$("#BankingWrite_ChecksID").attr("checked", false);
	$("#BankingReissue_ChecksID").attr("checked", false);
	$("#BankingRecouncile_AccountsID").attr("checked", false);
	$("#FinancialChart_AccountsID").attr("checked", false);
	$("#FinancialDivisionsID").attr("checked", false);
	$("#FinancialTax_TerritoriesID").attr("checked", false);
	$("#FinancialGeneral_LedgerID").attr("checked", false);
	$("#FinancialJournal_EntriesID").attr("checked", false);
	$("#FinancialAccounting_CyclesID").attr("checked", false);
	$("#FinancialGL_TransactionsID").attr("checked", false);
	$("#FinancialOpenPeriod_PostingID").attr("checked", false);
	
}
function AllowClickEvent(value){
	if(value=="Allow"){
		$("#HomenewJobID").attr("checked", true);
		$("#HomeQuick_bookID").attr("checked", true);
		$("#jobMainID").attr("checked", true);
		$("#jobQuotesID").attr("checked", true);
		$("#jobSubmittalID").attr("checked", true);
		$("#jobCreditID").attr("checked", true);
		$("#jobReleaseID").attr("checked", true);
		$("#jobFinancialID").attr("checked", true);
		$("#jobJournalID").attr("checked", true);
		$("#salesID").attr("checked", true);
		$("#sales_FilterID").attr("checked", true);
		$("#projectID").attr("checked", true);
		$("#project_FilterID").attr("checked", true);
		$("#CompanyCustomerID").attr("checked", true);
		$("#CompanyPaymentsID").attr("checked", true);
		$("#CompanyStatementsID").attr("checked", true);
		$("#CompanySales_OrderID").attr("checked", true);
		$("#CompanyInvoiceID").attr("checked", true);
		$("#CompanyFinance_ChangesID").attr("checked", true);
		$("#CompanyTax_AdjustmentsID").attr("checked", true);
		$("#CompanyCreditDebit_MemosID").attr("checked", true);
		$("#CompanySales_Order_TemplateID").attr("checked", true);
		$("#CompanyVendorID").attr("checked", true);
		$("#CompanyPurchase_OrdersID").attr("checked", true);
		$("#CompanyPay_BillsID").attr("checked", true);
		$("#CompanyInvoice_BillsID").attr("checked", true);
		$("#CompanyEmployeesID").attr("checked", true);
		$("#CompanyCommissionsID").attr("checked", true);
		$("#CompanyRolodexID").attr("checked", true);
		$("#CompanyUsersID").attr("checked", true);
		$("#CompanySettingsID").attr("checked", true);
		$("#inventoryID").attr("checked", true);
		$("#inventoryCategoriesID").attr("checked", true);
		$("#inventoryWarehousesID").attr("checked", true);
		$("#inventoryReceive_InventoryID").attr("checked", true);
		$("#inventoryTransferID").attr("checked", true);
		$("#inventoryOrder_PointsID").attr("checked", true);
		$("#inventoryInventory_ValueID").attr("checked", true);
		$("#inventoryCountID").attr("checked", true);
		$("#inventoryTransactionsID").attr("checked", true);
		$("#inventoryAdjustmentsID").attr("checked", true);
		$("#BankingID").attr("checked", true);
		$("#BankingWrite_ChecksID").attr("checked", true);
		$("#BankingReissue_ChecksID").attr("checked", true);
		$("#BankingRecouncile_AccountsID").attr("checked", true);
		$("#FinancialChart_AccountsID").attr("checked", true);
		$("#FinancialDivisionsID").attr("checked", true);
		$("#FinancialTax_TerritoriesID").attr("checked", true);
		$("#FinancialGeneral_LedgerID").attr("checked", true);
		$("#FinancialJournal_EntriesID").attr("checked", true);
		$("#FinancialAccounting_CyclesID").attr("checked", true);
		$("#FinancialGL_TransactionsID").attr("checked", true);
		$("#FinancialOpenPeriod_PostingID").attr("checked", true);
	}else if(value=="Deny"){
		$("#HomenewJobID").attr("checked", false);
		$("#HomeQuick_bookID").attr("checked", false);
		$("#jobMainID").attr("checked", false);
		$("#jobQuotesID").attr("checked", false);
		$("#jobSubmittalID").attr("checked", false);
		$("#jobCreditID").attr("checked", false);
		$("#jobReleaseID").attr("checked", false);
		$("#jobFinancialID").attr("checked", false);
		$("#jobJournalID").attr("checked", false);
		$("#salesID").attr("checked", false);
		$("#sales_FilterID").attr("checked", false);
		$("#projectID").attr("checked", false);
		$("#project_FilterID").attr("checked", false);
		$("#CompanyCustomerID").attr("checked", false);
		$("#CompanyPaymentsID").attr("checked", false);
		$("#CompanyStatementsID").attr("checked", false);
		$("#CompanySales_OrderID").attr("checked", false);
		$("#CompanyInvoiceID").attr("checked", false);
		$("#CompanyFinance_ChangesID").attr("checked", false);
		$("#CompanyTax_AdjustmentsID").attr("checked", false);
		$("#CompanyCreditDebit_MemosID").attr("checked", false);
		$("#CompanySales_Order_TemplateID").attr("checked", false);
		$("#CompanyVendorID").attr("checked", false);
		$("#CompanyPurchase_OrdersID").attr("checked", false);
		$("#CompanyPay_BillsID").attr("checked", false);
		$("#CompanyInvoice_BillsID").attr("checked", false);
		$("#CompanyEmployeesID").attr("checked", false);
		$("#CompanyCommissionsID").attr("checked", false);
		$("#CompanyRolodexID").attr("checked", false);
		$("#CompanyUsersID").attr("checked", false);
		$("#CompanySettingsID").attr("checked", false);
		$("#inventoryID").attr("checked", false);
		$("#inventoryCategoriesID").attr("checked", false);
		$("#inventoryWarehousesID").attr("checked", false);
		$("#inventoryReceive_InventoryID").attr("checked", false);
		$("#inventoryTransferID").attr("checked", false);
		$("#inventoryOrder_PointsID").attr("checked", false);
		$("#inventoryInventory_ValueID").attr("checked", false);
		$("#inventoryCountID").attr("checked", false);
		$("#inventoryTransactionsID").attr("checked", false);
		$("#inventoryAdjustmentsID").attr("checked", false);
		$("#BankingID").attr("checked", false);
		$("#BankingWrite_ChecksID").attr("checked", false);
		$("#BankingReissue_ChecksID").attr("checked", false);
		$("#BankingRecouncile_AccountsID").attr("checked", false);
		$("#FinancialChart_AccountsID").attr("checked", false);
		$("#FinancialDivisionsID").attr("checked", false);
		$("#FinancialTax_TerritoriesID").attr("checked", false);
		$("#FinancialGeneral_LedgerID").attr("checked", false);
		$("#FinancialJournal_EntriesID").attr("checked", false);
		$("#FinancialAccounting_CyclesID").attr("checked", false);
		$("#FinancialGL_TransactionsID").attr("checked", false);
		$("#FinancialOpenPeriod_PostingID").attr("checked", false);
	}
	updateUserPermission('HomenewJobID','New_Job');
	updateUserPermission('HomeQuick_bookID','Quick_book');
	updateUserPermission('jobMainID','Main');
	updateUserPermission('jobQuotesID','Quotes');
	updateUserPermission('jobSubmittalID','Submittal');
	updateUserPermission('jobCreditID','Credit');
	updateUserPermission('jobReleaseID','Release');
	updateUserPermission('jobFinancialID','Financial');
	updateUserPermission('jobJournalID','Journal');
	updateUserPermission('salesID','Sales');
	updateUserPermission('sales_FilterID','Sales_Filter');
	updateUserPermission('projectID','Project');
	updateUserPermission('project_FilterID','Project_Filter'); 
	updateUserPermission('CompanyCustomerID','Customers');
	updateUserPermission('CompanyPaymentsID','Payments');
	updateUserPermission('CompanyStatementsID','Statements');
	updateUserPermission('CompanySales_OrderID','Sales_Order');
	updateUserPermission('CompanyInvoiceID','Invoice');
	updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
	updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
	updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
	updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
	updateUserPermission('CompanyVendorID','Vendors');
	updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
	updateUserPermission('CompanyPay_BillsID','Pay_Bills');
	updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
	updateUserPermission('CompanyEmployeesID','Employees');
	updateUserPermission('CompanyCommissionsID','Commissions');
	updateUserPermission('CompanyRolodexID','Rolodex');
	updateUserPermission('CompanyUsersID','Users');
	updateUserPermission('CompanySettingsID','Settings'); 
	updateUserPermission('inventoryID','Inventory');
	updateUserPermission('inventoryCategoriesID','Categories');
	updateUserPermission('inventoryWarehousesID','Warehouses');
	updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
	updateUserPermission('inventoryTransferID','Transfer');
	updateUserPermission('inventoryOrder_PointsID','Order_Points');
	updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
	updateUserPermission('inventoryCountID','Count'); 
	updateUserPermission('inventoryTransactionsID','Transactions'); 
	updateUserPermission('inventoryAdjustmentsID','Adjustments');
	updateUserPermission('BankingID','Banking');
	updateUserPermission('BankingWrite_ChecksID','Write_Checks');
	updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
	updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
	updateUserPermission('FinancialChart_AccountsID','Chart_Accounts');
	updateUserPermission('FinancialDivisionsID','Divisions');
	updateUserPermission('FinancialTax_TerritoriesID','Tax_Territories');
	updateUserPermission('FinancialGeneral_LedgerID','General_Ledger');
	updateUserPermission('FinancialJournal_EntriesID','Journal_Entries');
	updateUserPermission('FinancialAccounting_CyclesID','Accounting_Cycles');
	updateUserPermission('FinancialGL_TransactionsID','GL_Transactions');
	updateUserPermission('FinancialOpenPeriod_PostingID','OpenPeriod_PostingOnly');
}

function GroupDefaultEvent(value){
	if(value=='Reset Group Default'){
		AllowClickEvent('Deny');
	}
	    
	if(document.getElementById("salesRepsboxID")!=undefined){
		var checkedorno=$('#salesRepsboxID').is(':checked');
		if(checkedorno){
			LoadGroupDefaultCheckboxes(1,value);
		}
	}
	if(document.getElementById("CSRsBoxID")!=undefined){
		var checkedorno=$('#CSRsBoxID').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(2,value);
		}
	}
	if(document.getElementById("employeesID")!=undefined){
		var checkedorno=$('#employeesID').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(3,value);
		}
	}
	if(document.getElementById("administrativeID")!=undefined){
		var checkedorno=$('#administrativeID').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(4,value);
		}
	}
	if(document.getElementById("wareHouseID")!=undefined){
		var checkedorno=$('#wareHouseID').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(5,value);
		}
	}
	if(document.getElementById("id6")!=undefined){
		var checkedorno=$('#id6').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(6,value);
		}
	}
	if(document.getElementById("id7")!=undefined){
		var checkedorno=$('#id7').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(7,value);
		}
	}
	if(document.getElementById("id8")!=undefined){
		var checkedorno=$('#id8').is(':checked')
		if(checkedorno){
			LoadGroupDefaultCheckboxes(8,value);
		}
	}
	

}
function LoadGroupDefaultCheckboxes(userGroupID,value){
	$.ajax({
        url: "./userlistcontroller/GetGroupDefaultPermission",
        data: {'userGroupID':userGroupID},
        type: 'GET',
        success: function(data){
        	for(var i=0;i<data.length;i++){
        		if(value=='Group Default'){
        			if(data[i].privilegeValue==1){
            			checkedcheckboxes(data[i].procedureName,data[i].privilegeValue);
            			updateGroupDefaultButtonAction(data[i].procedureName);
            		}
        		}else if(value=='Reset Group Default'){
        			if(data[i].privilegeValue==1){
        			   checkedcheckboxes(data[i].procedureName,data[i].privilegeValue);
        			   updateGroupDefaultButtonAction(data[i].procedureName);
        			}
        		}
        	}
        }
   });
}
function updateGroupDefaultButtonAction(procedurename){
	if(procedurename=="New Job"){
		updateUserPermission('HomenewJobID','New_Job');
	}else if(procedurename=="Quick book"){
		updateUserPermission('HomeQuick_bookID','Quick_book');
	}
	
	else if(procedurename=="Main"){
		updateUserPermission('jobMainID','Main');
	}else if(procedurename=="Quotes"){
		updateUserPermission('jobQuotesID','Quotes');
	}else if(procedurename=="Submittal"){
		updateUserPermission('jobSubmittalID','Submittal');
	}else if(procedurename=="Credit"){
		updateUserPermission('jobCreditID','Credit');
	}else if(procedurename=="Release"){
		updateUserPermission('jobReleaseID','Release');
	}else if(procedurename=="Financial"){
		updateUserPermission('jobFinancialID','Financial');
	}else if(procedurename=="Journal"){
		updateUserPermission('jobJournalID','Journal');
	}
	
	
	else if(procedurename=="Sales"){
		updateUserPermission('salesID','Sales');
	}else if(procedurename=="Sales Filter"){
		updateUserPermission('sales_FilterID','Sales_Filter');
	}
	
	else if(procedurename=="Project"){
		updateUserPermission('projectID','Project');
	}else if(procedurename=="Project Filter"){
		updateUserPermission('project_FilterID','Project_Filter');
	}
	
	else if(procedurename=="Customers"){
		updateUserPermission('CompanyCustomerID','Customers');
	}else if(procedurename=="Payments"){
		updateUserPermission('CompanyPaymentsID','Payments');
	}else if(procedurename=="Statements"){
		updateUserPermission('CompanyStatementsID','Statements');
	}else if(procedurename=="Sales Order"){
		updateUserPermission('CompanySales_OrderID','Sales_Order');
	}else if(procedurename=="Invoice"){
		updateUserPermission('CompanyInvoiceID','Invoice');
	}else if(procedurename=="Finance Changes"){
		updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
	}else if(procedurename=="Tax Adjustments"){
		updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
	}else if(procedurename=="Credit/Debit Memos"){
		updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
	}
	
	
	else if(procedurename=="Sales Order Template"){
		updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
	}else if(procedurename=="Purchase Orders"){
		updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
	}else if(procedurename=="Vendors"){
		updateUserPermission('CompanyVendorID','Vendors');
	}else if(procedurename=="Pay Bills"){
		updateUserPermission('CompanyPay_BillsID','Pay_Bills');
	}else if(procedurename=="Invoice & Bills"){
		updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
	}else if(procedurename=="Employees"){
		updateUserPermission('CompanyEmployeesID','Employees');
	}else if(procedurename=="Commissions"){
		updateUserPermission('CompanyCommissionsID','Commissions');
	}else if(procedurename=="Rolodex"){
		updateUserPermission('CompanyRolodexID','Rolodex');
	}else if(procedurename=="Users"){
		updateUserPermission('CompanyUsersID','Users');
	}else if(procedurename=="Settings"){
		updateUserPermission('CompanySettingsID','Settings'); 
	}
	
	
	else if(procedurename=="Inventory"){
		updateUserPermission('inventoryID','Inventory');
	}else if(procedurename=="Categories"){
		updateUserPermission('inventoryCategoriesID','Categories');
	}else if(procedurename=="Warehouses"){
		updateUserPermission('inventoryWarehousesID','Warehouses');
	}else if(procedurename=="Receive Inventory"){
		updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
	}else if(procedurename=="Transfer"){
		updateUserPermission('inventoryTransferID','Transfer');
	}else if(procedurename=="Order Points"){
		updateUserPermission('inventoryOrder_PointsID','Order_Points');
	}else if(procedurename=="Inventory Value"){
		updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
	}else if(procedurename=="Count"){
		updateUserPermission('inventoryCountID','Count'); 
	}else if(procedurename=="Adjustments"){
		updateUserPermission('inventoryAdjustmentsID','Adjustments');
	}else if(procedurename=="Banking"){
		updateUserPermission('BankingID','Banking');
	}else if(procedurename=="Transactions"){
		updateUserPermission('inventoryTransactionsID','Transactions');
	}
	
	else if(procedurename=="Write Checks"){
		updateUserPermission('BankingWrite_ChecksID','Write_Checks');
	}else if(procedurename=="Reissue Checks"){
		updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
	}else if(procedurename=="Recouncile Accounts"){
		updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
	}else if(procedurename=="Chart Accounts"){
		updateUserPermission('FinancialChart_AccountsID','Chart_Accounts');
	}else if(procedurename=="Divisions"){
		updateUserPermission('FinancialDivisionsID','Divisions');
	}else if(procedurename=="Tax Territories"){
		updateUserPermission('FinancialTax_TerritoriesID','Tax_Territories');
	}else if(procedurename=="General Ledger"){
		updateUserPermission('FinancialGeneral_LedgerID','General_Ledger');
	}else if(procedurename=="Journal Entries"){
		updateUserPermission('FinancialJournal_EntriesID','Journal_Entries');
	}else if(procedurename=="Accounting Cycles"){
		updateUserPermission('FinancialAccounting_CyclesID','Accounting_Cycles');
	}else if(procedurename=="GL Transactions"){
		updateUserPermission('FinancialGL_TransactionsID','GL_Transactions');
	}else if(procedurename=="Open Period Posting Only"){
		updateUserPermission('FinancialOpenPeriod_PostingID','OpenPeriod_PostingOnly');
	}
		
	
	
	
}

function GroupByprocedureSelect(selectedgroup){
	if(selectedgroup=='Customer'){
		if($('#CompanyCustomerID').is(':checked')){
			checkboxenableOrdisable('CompanyCustomerID',1);
			updateUserPermission('CompanyCustomerID','Customers');
			checkboxenableOrdisable('CompanyPaymentsID',1);
			updateUserPermission('CompanyPaymentsID','Payments');
			checkboxenableOrdisable('CompanyStatementsID',1);
			updateUserPermission('CompanyStatementsID','Statements');
			checkboxenableOrdisable('CompanySales_OrderID',1);
			updateUserPermission('CompanySales_OrderID','Sales_Order');
			checkboxenableOrdisable('CompanyInvoiceID',1);
			updateUserPermission('CompanyInvoiceID','Invoice');
			checkboxenableOrdisable('CompanyFinance_ChangesID',1);
			updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
			checkboxenableOrdisable('CompanyTax_AdjustmentsID',1);
			updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
			checkboxenableOrdisable('CompanyCreditDebit_MemosID',1);
			updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
			checkboxenableOrdisable('CompanySales_Order_TemplateID',1);
			updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
		}else{
			checkboxenableOrdisable('CompanyCustomerID',-1);
			updateUserPermission('CompanyCustomerID','Customers');
			checkboxenableOrdisable('CompanyPaymentsID',-1);
			updateUserPermission('CompanyPaymentsID','Payments');
			checkboxenableOrdisable('CompanyStatementsID',-1);
			updateUserPermission('CompanyStatementsID','Statements');
			checkboxenableOrdisable('CompanySales_OrderID',-1);
			updateUserPermission('CompanySales_OrderID','Sales_Order');
			checkboxenableOrdisable('CompanyInvoiceID',-1);
			updateUserPermission('CompanyInvoiceID','Invoice');
			checkboxenableOrdisable('CompanyFinance_ChangesID',-1);
			updateUserPermission('CompanyFinance_ChangesID','Finance_Changes');
			checkboxenableOrdisable('CompanyTax_AdjustmentsID',-1);
			updateUserPermission('CompanyTax_AdjustmentsID','Tax_Adjustments');
			checkboxenableOrdisable('CompanyCreditDebit_MemosID',-1);
			updateUserPermission('CompanyCreditDebit_MemosID','CreditDebit_Memos');
			checkboxenableOrdisable('CompanySales_Order_TemplateID',-1);
			updateUserPermission('CompanySales_Order_TemplateID','Sales_Order_Template');
		}
	}else if(selectedgroup=='Vendor'){
		if($('#CompanyVendorID').is(':checked')){
			checkboxenableOrdisable('CompanyVendorID',1);
			updateUserPermission('CompanyVendorID','Vendors');
			checkboxenableOrdisable('CompanyPurchase_OrdersID',1);
			updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
			checkboxenableOrdisable('CompanyPay_BillsID',1);
			updateUserPermission('CompanyPay_BillsID','Pay_Bills');
			checkboxenableOrdisable('CompanyInvoice_BillsID',1);
			updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
			
		}else{
			checkboxenableOrdisable('CompanyVendorID',-1);
			updateUserPermission('CompanyVendorID','Vendors');
			checkboxenableOrdisable('CompanyPurchase_OrdersID',-1);
			updateUserPermission('CompanyPurchase_OrdersID','Purchase_Orders');
			checkboxenableOrdisable('CompanyPay_BillsID',-1);
			updateUserPermission('CompanyPay_BillsID','Pay_Bills');
			checkboxenableOrdisable('CompanyInvoice_BillsID',-1);
			updateUserPermission('CompanyInvoice_BillsID','Invoice_Bills');
			
		}
	}else if(selectedgroup=='Employee'){
		if($('#CompanyEmployeesID').is(':checked')){
				checkboxenableOrdisable('CompanyEmployeesID',1);
				updateUserPermission('CompanyEmployeesID','Employees');
				checkboxenableOrdisable('CompanyCommissionsID',1);
				updateUserPermission('CompanyCommissionsID','Commissions');
			}else{
				checkboxenableOrdisable('CompanyEmployeesID',-1);
				updateUserPermission('CompanyEmployeesID','Employees');
				checkboxenableOrdisable('CompanyCommissionsID',-1);
				updateUserPermission('CompanyCommissionsID','Commissions');
			}
	}else if(selectedgroup=='Sales'){
		if($('#salesID').is(':checked')){
				checkboxenableOrdisable('salesID',1);
				updateUserPermission('salesID','Sales');
				checkboxenableOrdisable('sales_FilterID',1);
				updateUserPermission('sales_FilterID','Sales_Filter');
			}else{
				alert('else');
				checkboxenableOrdisable('salesID',-1);
				updateUserPermission('salesID','Sales');
				checkboxenableOrdisable('sales_FilterID',-1);
				updateUserPermission('sales_FilterID','Sales_Filter');
			}
	}else if(selectedgroup=='Project'){
		
		if($('input:checkbox[id=projectID]').is(':checked')){
				checkboxenableOrdisable('projectID',1);
				updateUserPermission('projectID','Project');
				checkboxenableOrdisable('project_FilterID',1);
				updateUserPermission('project_FilterID','Project_Filter'); 
			}else{
				checkboxenableOrdisable('projectID',-1);
				updateUserPermission('projectID','Project');
				checkboxenableOrdisable('project_FilterID',-1);
				updateUserPermission('project_FilterID','Project_Filter');
			}
	}else if(selectedgroup=='Banking'){
		if($('#BankingID').is(':checked')){
				checkboxenableOrdisable('BankingID',1);
				updateUserPermission('BankingID','Banking');
				checkboxenableOrdisable('BankingWrite_ChecksID',1);
				updateUserPermission('BankingWrite_ChecksID','Write_Checks');
				checkboxenableOrdisable('BankingReissue_ChecksID',1);
				updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
				checkboxenableOrdisable('BankingRecouncile_AccountsID',1);
				updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
			}else{
				checkboxenableOrdisable('BankingID',-1);
				updateUserPermission('BankingID','Banking');
				checkboxenableOrdisable('BankingWrite_ChecksID',-1);
				updateUserPermission('BankingWrite_ChecksID','Write_Checks');
				checkboxenableOrdisable('BankingReissue_ChecksID',-1);
				updateUserPermission('BankingReissue_ChecksID','Reissue_Checks');
				checkboxenableOrdisable('BankingRecouncile_AccountsID',-1);
				updateUserPermission('BankingRecouncile_AccountsID','Recouncile_Accounts');
				
			}
	}else if(selectedgroup=='Inventory'){
		if($('#inventoryID').is(':checked')){
				checkboxenableOrdisable('inventoryID',1);
				updateUserPermission('inventoryID','Inventory');
				checkboxenableOrdisable('inventoryCategoriesID',1);
				updateUserPermission('inventoryCategoriesID','Categories');
				checkboxenableOrdisable('inventoryWarehousesID',1);
				updateUserPermission('inventoryWarehousesID','Warehouses');
				checkboxenableOrdisable('inventoryReceive_InventoryID',1);
				updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
				checkboxenableOrdisable('inventoryTransferID',1);
				updateUserPermission('inventoryTransferID','Transfer');
				checkboxenableOrdisable('inventoryOrder_PointsID',1);
				updateUserPermission('inventoryOrder_PointsID','Order_Points');
				checkboxenableOrdisable('inventoryInventory_ValueID',1);
				updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
				checkboxenableOrdisable('inventoryCountID',1);
				updateUserPermission('inventoryCountID','Count'); 
				checkboxenableOrdisable('inventoryAdjustmentsID',1);
				updateUserPermission('inventoryAdjustmentsID','Adjustments');
				
			}else{
				checkboxenableOrdisable('inventoryID',-1);
				updateUserPermission('inventoryID','Inventory');
				checkboxenableOrdisable('inventoryCategoriesID',-1);
				updateUserPermission('inventoryCategoriesID','Categories');
				checkboxenableOrdisable('inventoryWarehousesID',-1);
				updateUserPermission('inventoryWarehousesID','Warehouses');
				checkboxenableOrdisable('inventoryReceive_InventoryID',-1);
				updateUserPermission('inventoryReceive_InventoryID','Receive_Inventory');
				checkboxenableOrdisable('inventoryTransferID',-1);
				updateUserPermission('inventoryTransferID','Transfer');
				checkboxenableOrdisable('inventoryOrder_PointsID',-1);
				updateUserPermission('inventoryOrder_PointsID','Order_Points');
				checkboxenableOrdisable('inventoryInventory_ValueID',-1);
				updateUserPermission('inventoryInventory_ValueID','Inventory_Value');
				checkboxenableOrdisable('inventoryCountID',-1);
				updateUserPermission('inventoryCountID','Count'); 
				checkboxenableOrdisable('inventoryAdjustmentsID',-1);
				updateUserPermission('inventoryAdjustmentsID','Adjustments');
				
			}
	}
	
	
}

function updateJobTabClick(divid,accessProcedurename){
	if($('input:checkbox[id='+divid+']').is(':checked')){
		checkboxenableOrdisable('jobMainID',1);
		updateUserPermission('jobMainID','Main');
		updateUserPermission(divid,accessProcedurename);
	}else{
		updateUserPermission(divid,accessProcedurename);
	}
}

function onclickjobmaintab(divid,accessProcedurename){
	if($('input:checkbox[id='+divid+']').is(':checked')){
		updateUserPermission('jobMainID','Main');
	}else{
		updateUserPermission('jobMainID','Main');
		checkboxenableOrdisable('jobQuotesID',-1);
		updateUserPermission('jobQuotesID','Quotes');
		checkboxenableOrdisable('jobSubmittalID',-1);
		updateUserPermission('jobSubmittalID','Submittal');
		checkboxenableOrdisable('jobCreditID',-1);
		updateUserPermission('jobCreditID','Credit');
		checkboxenableOrdisable('jobReleaseID',-1);
		updateUserPermission('jobReleaseID','Release');
		checkboxenableOrdisable('jobFinancialID',-1);
		updateUserPermission('jobFinancialID','Financial');
		checkboxenableOrdisable('jobJournalID',-1);
		updateUserPermission('jobJournalID','Journal');
	}
}
