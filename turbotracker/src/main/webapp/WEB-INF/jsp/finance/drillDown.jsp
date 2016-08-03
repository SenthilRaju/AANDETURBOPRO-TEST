<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - DrillDown Tab</title>
<style type="text/css">
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			.ui-state-default{background-color: E8E3E3;}
		</style>
</head>
<body>
<div  style="background-color: #FAFAFA">
<div>
	 <jsp:include page="./../headermenu.jsp"></jsp:include> 
</div>
<div class="tabs_main" id="drillDown_tabs" style="padding-left: 0px;width:1090px;margin:0 auto; background-color: #FAFAFA;height: 600px;right:8px; box-shadow: 1px 6px 5px 5px #AAAAAA;">
	<ul>
		<li id="accountDetailsTab"><a href="#accountDetailsListID">Account</a></li>
		<li id="journalBalancesDetailsTab"><a href="#journalBalancesDetailsListID">Journal Balances</a></li>
		<li id="journalDetailsTab"><a href="#journalDetailsListID">Journal Detail</a></li>
		<li id="transactionDetailsTab"><a href="#transactiondetailsListID">Transaction Detail</a></li>
		<li id="documentDetailsTab"><a href="#documentdetailsListID">Document Links</a></li>
		<li id="budgetsDetailsTab"><a href="#budgetsdetailsListID">Budgets</a></li>
		<li id="transactionNotesDetailsTab"><a href="#transactionNotesDetailsListID">Transaction Notes</a></li>
		<li id="timestampDetailsTab"><a href="#timestampDetailsListID">Timestamp</a></li>
		
	</ul>
	<br><br>
<div id="accountDetailsListID">
<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>
							<div id="AccountsDetailsDiv" style="padding: 0px;">
								<form id="DrillDownFormID">
									<table style="padding-top: 8px; padding-left: 40px;">
									<tr>
											<td style="width: 140px"><label><u>Company ID</u></label></td>
											<td><label>#</label></td>
											<td><input type="text" id="companyID" name="companyID"/> </td>
											<td colspan="2"><input type="text" id="companyDescID" name="companyDescID" style="width:203px"/></td>
									</tr>
									<tr>
											<td style="width: 140px"><label><u>Accounts</u><span style="color:red;">*</span></label></td>
											<td><label>#</label></td>
											<td><input type="text" id="accountsID" name="accounts"/>
											<input type="hidden" id="coAccountsID" name="coAccountsID"/>
											 </td >
											 <td colspan="3">	<span id="accDesc" ></span></td>
											<!-- <td><label>To&nbsp;&nbsp;#</label></td>
											<td colspan="2"><input type="text" id="accountsToID" name="accountsTo"/></td> -->
									</tr>
									<%-- <tr>
											<td style="width: 140px"><label>Account Mask</label></td>
											<td><label>#</label></td>
											<td><input type="text" id="companyID" name="companyID"/> </td>
											<td colspan="3"><input type="text" id="companyDescID" name="companyDescID" style="width:203px"/></td>
									</tr> --%>
									<tr>
											<td style="width: 140px"><label>Select By</label></td>
											<td>&nbsp;</td>
											<td><input type="radio" name="selectBy" id="periodAndYearID" value="p" onclick="selectPeriods();"/>Period And Year </td>
											<td colspan="2"><input type="radio" name="selectBy" id="mostRecentPeriodsID" style="margin-left:0px;" value="m" onclick="selectPeriods();"/>Most Recent Period</td>
								  	</tr>
								  	<tr id="periodsTr">
											<td style="width: 140px;"><label>Periods<span style="color:red;">*</span></label></td>
											<td><label>#</label></td>
											<td>
												<select id="periodsId" name="periods" style="width:61px;margin-left:3px;" onchange="generatePeriodTo();validatePeriod();">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												</select> </td>
											<td><label>To<span style="color:red;">*</span>&nbsp;&nbsp;#</label></td>
											<td colspan="2">
											<select id="periodsToID" name="periodsTo" style="width:61px;">
												<option value=""></option>
												</select>
											</td>
								 	</tr>
								 	<tr>
											<td style="width: 140px"><label>Year<span style="color:red;">*</span></label></td>
											<td><label>#</label></td>
											<td colspan="3">
											<select style="margin-left:3px;" id="yearID" name="year" onchange="validatePeriod();">
											<option value=""></option>
											</select>
											</td>
									</tr>
									<tr id="mostRecentPeriodTr">
											<td style="width: 140px"><label>Most Recent Periods</label></td>
											<td><label>#</label></td>
											<td colspan="3">
											<select id="mostRecentPeriodID" name="mostRecentPeriod" style="width:61px;margin-left:3px;">
												<option value=""></option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
												<option value="13">13</option>
												</select>
											</td>
									</tr>
									<tr><td><div style="display: none;" id="drillDownMsg">&nbsp;</div></td></tr>
									<tr>
											<td colspan="5">
											<span id ="errorStatus" style ="color:red"> </span>
											<span style="float: right;">
											<input type="hidden" name="journalTotal" id="journalTotalID" value="${requestScope.journalTotal}" /> 
											<input type="button" id="saveDDButton" name="saveButtonName" value="Get" class="savehoverbutton turbo-blue" onclick="getAccountDetails()">
										
												</span>
												</td>
												
									</tr>
									</table>
									<table>
									<tr>
											<td colspan="5">
												<div id="jqGrid">					
													<table id="drillDownGrid"></table>
													<div id="drillDownPager"></div>
												</div>
											</td>
									</tr>
									</table>
									
								</form>
							</div>
				</td>
			</tr>
		</table>
</div>
<div id="journalBalancesDetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>
				<div id="journalBalancejqGrid">
					<table id="journalBalanceGrid"></table>
					<div id="journalBalanceGridPager"></div>
				</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="journalDetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>
				<div id="journalDetailjqGrid">
					<table id="journalDetailGrid"></table>
					<div id="journalDetailGridPager"></div>
				</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="transactiondetailsListID">
	
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>
				Transaction No: <span id="transNoId"></span>
				</td>
			</tr>
			<tr>
				<td>
				<div id="transactionDetailjqGrid">
					<table id="transactionDetailGrid"></table>
					<div id="transactionDetailGridPager"></div>
				</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="documentdetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div id="budgetsdetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div id="transactionNotesDetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div id="timestampDetailsListID">
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
</div>

<%-- <div style="display: none;">
	<table>
		<tr>
			<td>
				<input type="text" id="quote_ID" name="quote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quote}">
				<input type="text" id="quickQuote_ID" name="quickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quickQuote}">
				<input type="text" id="invoice_ID" name="invoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.invoices}">
				<input type="text" id="purchaseOrder_ID" name="purchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.purchaseOrders}">
				<input type="text" id="headerQuote_ID" name="headerQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuote}">
				<input type="text" id="headerQuick_ID" name="headerQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuickQuote}">
				<input type="text" id="headerInvoice_ID" name="headerInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerInvoices}">
				<input type="text" id="headerPurchaseOrder_ID" name="headerPurchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerPurchaseOrders}">
				<input type="text" id="termsQuote_ID" name="termsQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuote}">
				<input type="text" id="termsQuickQuote_ID" name="termsQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuickQuote}">
				<input type="text" id="termsInvoice_ID" name="termsInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsInvoices}">
				<input type="text" id="termsPO_ID" name="termsPO_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsPurchaseOrders}">
			</td>
		</tr>
	</table>
</div> --%>
<table id="footer">
  <tr>
	<td colspan="2">
		<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
	</td>
 </tr>
</table>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.form.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/drillDown.js"></script>
<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
<script type="text/javascript">
jQuery(document).ready(function(){
	$("#accountDetailsTab").removeClass("ui-state-disabled");
	$("#journalBalancesDetailsTab").removeClass("ui-state-disabled");
	$("#journalDetailsTab").removeClass("ui-state-disabled");
	$("#transactionDetailsTab").removeClass("ui-state-disabled");
	$("#documentDetailsTab").removeClass("ui-state-disabled");
	$("#budgetsDetailsTab").removeClass("ui-state-disabled");
	$("#transactionNotesDetailsTab").removeClass("ui-state-disabled");
	$("#timestampDetailsTab").removeClass("ui-state-disabled");



	/* $('#drillDownPager #pg_drillDownPager .ui-pg-table').css("display","none");
	$('#journalBalanceGridPager #pg_journalBalanceGridPager .ui-pg-table').css("display","none");
	$('#journalDetailGridPager #pg_journalDetailGridPager .ui-pg-table').css("display","none");
	$('#transactionDetailGridPager #pg_transactionDetailGridPager .ui-pg-table').css("display","none"); */
	
	
}); 

/**
 * Created by:leo  Desc: AccountsId Auto Complete field
 *
 */

$(function() { var cache = {}; var lastXhr='';
$("#accountsID").autocomplete({ minLength: 1,timeout :1000,
	/* open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},  */
	select: function (event, ui) {
		var aValue = ui.item.label;
		var aId = ui.item.id;

		var valuesArray = new Array();
		valuesArray = aValue.split("[");	

		$("#accDesc").html(""+valuesArray[0]);
		$("#coAccountsID").val(aId);
		
	},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	}
	lastXhr = $.getJSON( "drillDown/getAccountNoList", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} });
});

</script>
</body>
</html>