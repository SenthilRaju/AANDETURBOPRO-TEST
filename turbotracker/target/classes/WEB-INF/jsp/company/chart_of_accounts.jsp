<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Chart of Accounts</title>
<style type="text/css">
input[type='checkbox'] {
	margin-left: 0px;
	margin-right: 11px;
}

.accountRangeInput {
	width: 90px;
}

.accountRangeInputID {
	display: none;
}

#mainMenuCompanyPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #003961;
}

#mainMenuCompanyPage a {
	background:
		url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png')
		no-repeat 0px 4px;
	color: #FFF
}

#mainMenuCompanyPage ul li a {
	background: none;
}
</style>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<table style="width: 979px; margin: 0 auto;">
			<tr>

			</tr>
			<tr>
				<td style="padding-right: 20px;">
					<table id="chartsOfAccountsGrid"></table>
					<div id="chartsOfAccountsGridPager"></div>
				</td>
				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 800px; margin: 0 auto; background-color: #FAFAFA; height: 660px;">
							<ul>
								<li id=""><a href="#chartsDetailsDiv">General</a></li>
								<li id=""><a href="#accountSettings">Additional</a></li>
								<li style="float: right; padding-right: 10px; padding-top: 3px;">
									<label id="chartsName"
									style="color: white; vertical-align: middle;"></label>
								</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id="chartsDetailsFromID">
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Account</b></label>
																</legend>
																<table>

																	<tr>
																		<td colspan="4"><span
																			style="color: #00377A; size: 0.8em; float: right; margin-right: 25px; margin-top: -8px;">Type<label
																				style="color: red;">*</label></span><span style="display:none"><input type="text"
																			name="coAccountName" id="coAccountNameID"
																			style="width: 150px"><input type="hidden" id="segmentCount" /></span></td>
																	</tr>
																	<tr class="segment1">
																		<!-- <td style="display: none;"><label>Number: </label><input type="text" name="coAccountName" id="coAccountNameID" style="width:150px"></td> -->

																		<td colspan="2"><label id="seg1">Segment
																				#1: </label></td>

																		<td><label id="seg1"><span><input
																					type="text" name="numberName1" id="numberNameID1"
																					style="width: 110px;"
																					class="validate[required] validate[maxSize[12]]"
																					value=""></span> <span ><input type="hidden" name="segment1length" id="segment1length" /></span></label></td>
																		<td><select id="typeAccount" name="typeAccount" onchange="setAccountType()">
																				<option></option>
																				<option value="Asset">Asset</option>
																				<option value="Liability">Liability</option>
																				<option value="Equity">Equity</option>
																				<option value="Income">Income</option>
																				<option value="Expense">Expense</option>
																		</select></td>


																	</tr>
																	<tr class="segment2">


																		<td colspan="2"><label id="seg2">Segment
																				#2: </label></td>
																		<td><span style="float: right;"><input
																				type="text" name="numberName2" id="numberNameID2"
																				style="width: 110px; margin-top: 0px;"
																				class="validate[required] validate[maxSize[12]]"
																				value=""></span><span style="display:none;"><input type="hidden" name="segment2length" id="segment2length" /></span></td>
																		<td></td>


																	</tr>
																	<tr class="segment3">

																		<td colspan="2"><label id="seg3">Segment
																				#3: </label></td>

																		<td><span style="float: right;"><input
																				type="text" name="numberName3" id="numberNameID3"
																				style="width: 110px; margin-top: 0px;"
																				class="validate[required] validate[maxSize[12]]"
																				value=""></span> <span style="display:none;"><input type="hidden" name="segment3length" id="segment3length" /></span>
																	    </td>
																		<td></td>

																	</tr>


																	<tr class="segment4">

																		<td colspan="2"><label id="seg4">Segment
																				#4: </label></td>

																		<td><span style="float: right;"><input
																				type="text" name="numberName4" id="numberNameID4"
																				style="width: 110px; margin-top: 0px;"
																				class="validate[required] validate[maxSize[12]]"
																				value=""><span style="display:none;"><input type="hidden" name="segment4length" id="segment4length" /></span></td>
																		<td></td>

																	</tr>
																	<tr>
																		<td><label>Description: </label></td>
																		<td colspan="3"><input type="text"
																			name="decriptionName" id="decriptionNameID"
																			style="width: 297px"
																			class="validate[required] validate[maxSize[50]"></td>
																	</tr>
																	<tr>
																		<!-- <td style="width: 750px;"><label>Posting:
																		</label></td>
																		<td><select style="width: 95px;"
																			id="postingLevelID" name="postingLevelName">
																				<option value="0">Standard</option>
																				<option value="1">1 Level</option>
																				<option value="2">2 Level</option>
																				<option value="3">3 Level</option>
																				<option value="4">4 Level</option>
																				<option value="5">5 Level</option>
																				<option value="6">6 Level</option>
																				<option value="7">7 Level</option>
																				<option value="8">8 Level</option>
																				<option value="9">9 Level</option>
																				<option value="10">Title</option>
																		</select></td> -->
																		<td align="left"><label
																			style="margin-left: 10px;">Balance: </label>
																		<!-- <label id="balanceNameID" style="width: 100px;"></label> -->
																		</td>
																		<td><select id="accountTypeID"
																			name="accountTypeName" style="width: 82px;">
																				<option value="0">Debit</option>
																				<option value="1">Credit</option>
																		</select></td>
																		<td colspan="2">
																			<label style="vertical-align: middle; margin-left: 10px;">InActive:</label>
																			<input type="checkbox" name="inActiveName" id="inActiveNameID" style="vertical-align: middle;">
																		</td>
																	</tr>
																	 <%-- <tr>
																		<td></td>
																		<td colspan='2' style="width: 750px;"><label
																			style="vertical-align: middle;">1099 Expense?
																		</label><span style="margin-left: 10px;"><input
																				type="checkbox" name="expenseNameBox"
																				id="expenseIDBox" style="vertical-align: middle;"></span>

																			<label
																			style="vertical-align: middle; margin-left: 10px;">InActive:
																		</label></td>
																		<td align="right"><input type="checkbox"
																			name="inActiveName" id="inActiveNameID"></td>
																	</tr> --%>
																</table>
															</fieldset>
														</td>
													</tr>
													<tr>
														<td>
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Balance</b></label>
																</legend>
																<table>
																	<tr>
																		<td>
																			<table>
																				<tr>
																					<td></td>
																				</tr>
																				<tr>
																					<td></td>
																				</tr>
																				<tr>
																					<td></td>
																				</tr>
																				<tr>
																					<td></td>
																				</tr>
																				<tr>
																					<td><label>Open</label></td>
																				</tr>
																				<tr>
																					<td><label>Debits</label></td>
																				</tr>
																				<tr>
																					<td><label>Credits</label></td>
																				</tr>
																				<tr>
																					<td><label>Close</label></td>
																				</tr>
																			</table>
																		</td>
																		<td>
																			<table>
																				<tr>
																					<td><label>Period</label></td>
																				</tr>
																				<tr>
																					<td><label id="periodDBID">$0.00 DB</label></td>
																				</tr>
																				<tr>
																					<td><label id="periodDB1ID">$0.00 DB</label></td>
																				</tr>
																				<tr>
																					<td><label id="periodCRID">$0.00 CR</label></td>
																				</tr>
																				<tr>
																					<td><label id="periodDB2ID">$0.00 DB</label></td>
																				</tr>
																			</table>
																		</td>
																		<td width="40px;"></td>
																		<td>
																			<table>
																				<tr>
																					<td><label>Year To Date</label></td>
																				</tr>
																				<tr align="right">
																					<td><label id="yearToDateDBID">$0.00
																							DB</label></td>
																				</tr>
																				<tr align="right">
																					<td><label id="yearToDateDB1ID">$0.00
																							DB</label></td>
																				</tr>
																				<tr align="right">
																					<td><label id="yearToDateCRID">$0.00
																							CR</label></td>
																				</tr>
																				<tr align="right">
																					<td><label id="yearToDateDB2ID">$0.00
																							DB</label></td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
											<td style="vertical-align: top;">
												<fieldset class=" ui-widget-content ui-corner-all"
													style="height: 240px;width:330px">
													<legend>
														<label><b>Financial Report Formatting</b></label>
													</legend>
													
													<div id="incomeid" style ="display: none;">
													
														<table>
														<tr><td><label style="vertical-align: middle;">Income?</label><span style="color: red;">*</span></td> <td><input type="checkbox" name ="income" id="incomechkbox"></td></tr>
														<tr><td><label style="vertical-align: middle;">Cost of Goods Sold?</label><span style="color: red;">*</span></td> <td><input type="checkbox" name="cog" id="cogchkbox"></td></tr>
														</table>
													
													</div>
													
													<div id="expenseid" style ="display: none;">
													
														<table>
														<tr><td><label style="vertical-align: middle;">Expense?</label><span style="color: red;">*</span></td> <td><input type="checkbox" name ="exp" id="expchkbox"></td></tr>
														</table>
													
													</div>
													
													<div id="assetsid" style ="display: none;">
													
														<table>
														<tr><td><label style="vertical-align: middle;">Include On Balance Sheet?</label></td> <td><input type="checkbox" name ="assetincludeonbs" id="assetincludeonbsid"></td></tr>
														<tr><td><label style="vertical-align: middle;">Current Asset?</label></td> <td><input type="checkbox" class="cabsClass" name="cabs" id="cabs_caid"></td></tr>
														<tr><td><label style="vertical-align: middle;">Fixed Asset?</label></td> <td><input type="checkbox" class="cabsClass" name="cabs" id="cabs_faid"></td></tr>
														<tr><td><label style="vertical-align: middle;">Other Asset?</label></td> <td><input type="checkbox" class="cabsClass" name="cabs" id="cabs_oaid"></td></tr>
														<tr><td colspan="2"><span id="assetMsg" style="vertical-align: middle; color: red"></span></td></tr>
														</table>
													
													</div>
													<div id="liabilityid" style ="display: none;">
													
														<table>
														<tr><td><label style="vertical-align: middle;">Include On Balance Sheet?</label></td> <td><input type="checkbox" name ="liabsincludeonbs" id="liabsincludeonbsid"></td></tr>
														<tr><td><label style="vertical-align: middle;">Current Liability?</label></td> <td><input type="checkbox" class ="clbsClass" name="clbs" id="clbs_clid"></td></tr>
														<tr><td><label style="vertical-align: middle;">Long Term Liability?</label></td> <td><input type="checkbox" class ="clbsClass" name="clbs" id="clbs_ltlid"></td></tr>
														<tr><td colspan="2"><span id="liabilityMsg" style="vertical-align: middle; color: red"></span></td></tr>
														</table>
													
													</div>
													<div id="equityid" style ="display: none;">
													
														<table>
														<tr><td><label style="vertical-align: middle;">Include On Balance Sheet?</label></td> <td><input type="checkbox" name ="equityincludeonbs" id="equityincludeonbsid"></td></tr>
														<tr><td colspan="2"><span id="equityMsg" style="vertical-align: middle; color: red"></span></td></tr>
														</table>
													
													</div>
													
													<div id="othersid" style = "display:none">
														<table>
															<tr>
																<td><label style="vertical-align: middle;">Large
																</label><input type="checkbox" id="largeNameID" name="largeName"
																	style="vertical-align: middle;"></td>
																<td><label style="vertical-align: middle;">Bold
																</label><input type="checkbox" id="boldNameID" name="boldName"
																	style="vertical-align: middle;"></td>
																<td><label style="vertical-align: middle;">Italic
																</label><input type="checkbox" id="italicNameID"
																	name="italicName" style="vertical-align: middle;"></td>
																<td><label style="vertical-align: middle;">$
																</label><input type="checkbox" id="dollourNameID"
																	name="dollourName" style="vertical-align: middle;"></td>
															</tr>
															<tr>
																<td colspan="2"><label
																	style="vertical-align: middle;">Always Print </label><input
																	type="checkbox" id="alwaysNameID" name="alwaysName"
																	style="vertical-align: middle;"></td>
																<td colspan="2"><label
																	style="vertical-align: middle;">Underline </label><input
																	type="checkbox" id="underlineNameID"
																	name="underlineName" style="vertical-align: middle;"></td>
															</tr>
														</table>
														<table style="width: 330px;">
															<tr>
																<td><label style="vertical-align: middle;">Vertical
																		Spacing: </label></td>
																<td colspan="3"><select style="width: 150px;"
																	id="verticalSpacing" name="verticalSpacingName">
																		<option value="0">None</option>
																		<option value="1">1</option>
																		<option value="2">2</option>
																		<option value="3">3</option>
																		<option value="4">4</option>
																		<option value="5">5</option>
																		<option value="6">6</option>
																		<option value="7">7</option>
																		<option value="8">8</option>
																		<option value="9">9</option>
																		<option value="10">New Page</option>
																</select></td>
															</tr>
															<tr>
																<td><label style="vertical-align: middle;">Tab
																		Indentation: </label></td>
																<td colspan="3"><select style="width: 150px;"
																	id="tabIndentationID" name="tabIndentationName">
																		<option value="0">None</option>
																		<option value="1">1</option>
																		<option value="2">2</option>
																		<option value="3">3</option>
																		<option value="4">4</option>
																		<option value="5">5</option>
																		<option value="6">6</option>
																		<option value="7">7</option>
																		<option value="8">8</option>
																		<option value="9">9</option>
																		<option value="10">Center</option>
																</select></td>
															</tr>
															<tr>
																<td><label style="vertical-align: middle;">Balance
																		Sheet Column: </label></td>
																<td><select style="width: 150px;"
																	id="balanceSheetColumnID" name="balanceSheetColumnName">
																		<option value="1">1</option>
																		<option value="2">2</option>
																		<option value="3">3</option>
																</select></td>
															</tr>
															<tr>
																<td><label style="vertical-align: middle;">Line
																		above amount: </label></td>
																<td><select style="width: 150px;"
																	id="lineAboveAmountID" name="lineAboveAmountName">
																		<option value="0">None</option>
																		<option value="1">Single</option>
																		<option value="2">Double</option>
																</select></td>
															</tr>
															<tr>
																<td><label style="vertical-align: middle;">Line
																		below amount: </label></td>
																<td><select style="width: 150px;"
																	id="lineBelowAmount" name="lineBelowAmountName">
																		<option value="0">None</option>
																		<option value="1">Single</option>
																		<option value="2">Double</option>
																</select></td>
															</tr>
														</table>
													</div>
												</fieldset>
												<table>
													<tr>
														<td style="align: right; padding-left: 10x; width: 770px;"
															align="right"><input type="button" value="  Add"
															class="add" id="addChartlist"
															onclick="openAddNewChartDialog()"> <input
															type="button" id="saveUserButton"
															name="saveUserButtonName" value="Save"
															class="savehoverbutton turbo-blue" style="width: 80px;"
															onclick="saveChartsOfAccounts()"> <input
															type="button" id="deleteChartOfAccountID" value="Delete"
															class="savehoverbutton turbo-blue" style="width: 80px;"
															onclick="deleteAccountDetails()"></td>
													</tr>
													<tr class="alertstatus">
														<td>
															<div id="alertstatusforaccount"
																style="color: red; size: 14px; text-align: right;"></div>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</form>

							</div>
							<!-- --------------------------------- Additional Information -------------------------------- -->
							<div id="accountSettings" style="" style="height: 90%;">
								<form action="" id="accountsAdditionalInfoFormID">
									<table>
										<tr style="vertical-align: top;">
											<td>
												<table>
													<!-- <tr>
														<td>
															<fieldset  class= " ui-widget-content ui-corner-all" style="height: 180px;">
																<legend><label><b>Account Ranges</b></label></legend>
																<table>
																	<tr><td></td><td><label>From</label></td><td><label>Through</label></td></tr>
																	<tr><td><label>Asset: </label></td><td><input type="text" id="accountRangeAssetStart" name="accountRangeAssetStart" class="accountRangeInput"></td><td><input type="text" id="accountRangeAssetEnd" name="accountRangeAssetEnd" class="accountRangeInput"></td></tr>
																	<tr><td><label>Liability: </label></td><td><input type="text" id="accountRangeLiabilityStart" name="accountRangeLiabilityStart" class="accountRangeInput"></td><td><input type="text" id="accountRangeLiabilityEnd" name="accountRangeLiabilityEnd" class="accountRangeInput"></td></tr>
																	<tr><td><label>Equity: </label></td><td><input type="text" id="accountRangeEquityStart" name="accountRangeEquityStart" class="accountRangeInput"></td><td><input type="text" id="accountRangeEquityEnd" name="accountRangeEquityEnd" class="accountRangeInput"></td></tr>
																	<tr><td><label>Income: </label></td><td><input type="text" id="accountRangeIncomeStart" name="accountRangeIncomeStart" class="accountRangeInput"></td><td><input type="text" id="accountRangeIncomeEnd" name="accountRangeIncomeEnd" class="accountRangeInput"></td></tr>
																	<tr><td><label>Expense:</label> </td><td><input type="text" id="accountRangeExpenseStart" name="accountRangeExpenseStart" class="accountRangeInput"></td><td><input type="text" id="accountRangeExpenseEnd" name="accountRangeExpenseEnd" class="accountRangeInput"></td></tr>
																</table>
															</fieldset>
														</td>
													</tr> -->
													<tr>
														<td>
															<fieldset class=" ui-widget-content ui-corner-all"
																style="">
																<legend>
																	<label><b>Vendor</b></label>
																</legend>
																<table>
																	<tr>
																		<td><label>Accounts Payable: </label></td>
																		<td><input type="text" id="vendorAccountsPayable"
																			class="accountRangeInput"><input type="text"
																			size="5px" id="vendorAccountsPayableAccID"
																			name="vendorAccountsPayableAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Freight: </label></td>
																		<td><input type="text" id="vendorFreight"
																			class="accountRangeInput"><input type="text"
																			id="vendorFreightAccID" size="5px"
																			name="vendorFreightAccID" class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Sales Tax Paid: </label></td>
																		<td><input type="text" id="vendorSalesTaxPaid"
																			class="accountRangeInput"><input type="text"
																			id="vendorSalesTaxPaidAccID" size="5px"
																			name="vendorSalesTaxPaidAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Discounts Taken: </label></td>
																		<td><input type="text" id="vendorDiscountsTaken"
																			class="accountRangeInput"><input type="text"
																			id="vendorDiscountsTakenAccID" size="5px"
																			name="vendorDiscountsTakenAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Expense: </label></td>
																		<td><input type="text" id="vendorExpense"
																			class="accountRangeInput"><input type="text"
																			id="vendorExpenseAccID" size="5px"
																			name="vendorExpenseAccID" class="accountRangeInputID"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
													<tr>
														<td>
															<fieldset class=" ui-widget-content ui-corner-all"
																style="">
																<legend>
																	<label><b>Misc</b></label>
																</legend>
																<table>
																	<tr>
																		<td><label>Misc. Account: </label></td>
																		<td><input type="text" id="miscAccount"
																			class="accountRangeInput"><input type="text"
																			id="miscAccountAccID" size="5px"
																			name="miscAccountAccID" class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Inventory Adjustment:</label></td>
																		<td><input type="text"
																			id="miscInventoryAdjustment"
																			class="accountRangeInput"><input type="text"
																			id="miscInventoryAdjustmentAccID" size="5px"
																			name="miscInventoryAdjustmentAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
													
												</table>
											</td>
											<td style="vertical-align: top;">
												<table>
													<%-- <tr>
														<td>
															<fieldset class="ui-widget-content ui-corner-all">
																<legend>
																	<label><b>General Ledger</b></label>
																</legend>
																<table>
																	<tr>
																		<td><label>Net Sales:</label></td>
																		<td><input type="text" id="generalLedgerNetSales"
																			class="accountRangeInput"><input type="text"
																			id="generalLedgerNetSalesAccID" size="5px"
																			name="generalLedgerNetSalesAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Profit/Loss:</label></td>
																		<td><input type="text" class="accountRangeInput"
																			id="generalLedgerProfitLoss"><input
																			type="text" id="generalLedgerProfitLossAccID"
																			size="5px" name="generalLedgerProfitLossAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Current Year Earnings:</label></td>
																		<td><input type="text" class="accountRangeInput"
																			id="generalLedgerCurEarnings"><input
																			type="text" id="generalLedgerCurEarningsAccID"
																			size="5px" name="generalLedgerCurEarningsAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Retained Earnings:</label></td>
																		<td><input type="text" class="accountRangeInput"
																			id="generalLedgerRetainedEarnings"><input
																			type="text" id="generalLedgerRetainedEarningsAccID"
																			size="5px" name="generalLedgerRetainedEarningsAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr> --%>
													<tr>
														<td>
															<fieldset class=" ui-widget-content ui-corner-all"
																style="height: 215px;">
																<legend>
																	<label><b>Customer</b></label>
																</legend>
																<table>
																	<tr>
																		<td><label>Accounts Receivable:</label></td>
																		<td><input type="text"
																			id="customerAccountsReceivable"
																			class="accountRangeInput"><input type="text"
																			id="customerAccountsReceivableAccID" size="5px"
																			name="customerAccountsReceivableAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Discounts/Adjustments:</label></td>
																		<td><input type="text" id="customerDisc_Adjst"
																			class="accountRangeInput"><input type="text"
																			id="customerDisc_AdjstAccID" size="5px"
																			name="customerDisc_AdjstAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Sales Tax Invoiced: </label></td>
																		<td><input type="text"
																			id="customerSalesTaxInvoiced"
																			class="accountRangeInput"><input type="text"
																			id="customerSalesTaxInvoicedAccID" size="5px"
																			name="customerSalesTaxInvoicedAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Shipping Drop Ship: </label></td>
																		<td><input type="text"
																			id="customerShippingDropShip"
																			class="accountRangeInput"><input type="text"
																			id="customerShippingDropShipAccID" size="5px"
																			name="customerShippingDropShipAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Shipping Inventory: </label></td>
																		<td><input type="text" id="customerShpngInvntry"
																			class="accountRangeInput"><input type="text"
																			id="customerShpngInvntryAccID" size="5px"
																			name="customerShpngInvntryAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Other Charges: </label></td>
																		<td><input type="text" id="customerOthrChrgs"
																			class="accountRangeInput"><input type="text"
																			id="customerOthrChrgsAccID" size="5px"
																			name="customerOthrChrgsAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																	<tr>
																		<td><label>Payments Received: </label></td>
																		<td><input type="text" id="customerPaymntsRcvd"
																			class="accountRangeInput"><input type="text"
																			id="customerPaymntsRcvdAccID" size="5px"
																			name="customerPaymntsRcvdAccID"
																			class="accountRangeInputID"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr><td></td><td>
										<fieldset class=" ui-widget-content ui-corner-all"style="">
																<legend>
																	<label><b>General Ledger</b></label>
																</legend>
																<table><tr>
																<td style ="width:160px;"><label>Retained Earnings: </label></td>
																		<td><input type="text" id="generalLedgerRetainedEarnings" 
																			class="accountRangeInput"><input type="text"
																			id="generalLedgerRetainedEarningsAccID" size="5px"
																			name="generalLedgerRetainedEarningsAccID"
																			class="accountRangeInputID"></td>
																</tr></table>
																
										</fieldset>
										</td>
										</tr>
										
									</table>
								</form>
								<div style="padding-top: 0px; height: 80px;"></div>
								<input type="button" class="savehoverbutton turbo-blue"
									value="Save" onclick="saveAdditionalData()">
								<!-- <input type="button" class="savehoverbutton turbo-blue" value="Get Grid" onclick="GetGridData()"> -->
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>

		<div id="addNewChartDialog">
			<form action="" id="addNewChartFormID">
				<div id="">
					<table>
						<tr>
							<td>
								<table style="width: 520px;">
									<tr>
										<td>
											<table>
												<tr>
													<td style="vertical-align: top; padding-bottom: 135px;">
														<fieldset class=" ui-widget-content ui-corner-all"
															style="margin-left: 5px;">
															<legend>
																<label><b>Account</b></label>
															</legend>
															<table>
																<tr>
																	<td style="display: none;"><label>Number:
																	</label><input type="text" name="coAccountNewName"
																		id="coAccountNameNewID" style="width: 150px"></td>
																	<td><label>Number: </label></td>
																	<td><input type="text" name="numberNewName"
																		id="numberNewNameID" style="width: 150px"
																		class="validate[required] validate[maxSize[12]"
																		value=""></td>
																	<td><label style="vertical-align: middle;">InActive:
																	</label></td>
																	<td><input type="checkbox" name="inActiveNewName"
																		id="inActiveNewNameID"></td>
																</tr>
																<tr>
																	<td><label>Description: </label></td>
																	<td colspan="3"><input type="text"
																		name="decriptionNewName" id="decriptionNewNameID"
																		style="width: 275px"
																		class="validate[required] validate[maxSize[50]"></td>
																</tr>
																<tr>
																	<td><label>Posting: </label></td>
																	<td><select style="width: 150px;"
																		id="postingLevelNewID" name="postingLevelNewName">
																			<option value="0">Standard</option>
																			<option value="1">1 Level</option>
																			<option value="2">2 Level</option>
																			<option value="3">3 Level</option>
																			<option value="4">4 Level</option>
																			<option value="5">5 Level</option>
																			<option value="6">6 Level</option>
																			<option value="7">7 Level</option>
																			<option value="8">8 Level</option>
																			<option value="9">9 Level</option>
																			<option value="10">Title</option>
																	</select></td>
																	<td align="left"><label>Balance: </label> <label
																		id="balanceNewNameID" style="width: 100px;"></label> <select
																		id="addAccountTypeID" name="accountTypeName"
																		class="validate[required]">
																			<option value="0">Debit</option>
																			<option value="1">Credit</option>
																	</select></td>
																</tr>
																<tr>
																	<td></td>
																	<td colspan="4"><label
																		style="vertical-align: middle;">1099 Expense?
																	</label><input type="checkbox" name="expenseNewNameBox"
																		id="expenseNewIDBox" style="vertical-align: middle;"></td>
																</tr>
															</table>
														</fieldset>
													</td>
												</tr>
											</table>
										</td>
										<td>
											<fieldset class=" ui-widget-content ui-corner-all"
												style="height: 270px;">
												<legend>
													<label><b>Financial Report Formatting</b></label>
												</legend>
												<div>
													<table>
														<tr>
															<td><label style="vertical-align: middle;">Large
															</label><input type="checkbox" id="largeNewNameID"
																name="largeNewName" style="vertical-align: middle;"></td>
															<td><label style="vertical-align: middle;">Bold
															</label><input type="checkbox" id="boldNewNameID"
																name="boldNewName" style="vertical-align: middle;"></td>
															<td><label style="vertical-align: middle;">Italic
															</label><input type="checkbox" id="italicNewNameID"
																name="italicNewName" style="vertical-align: middle;"></td>
															<td><label style="vertical-align: middle;">$
															</label><input type="checkbox" id="dollourNewNameID"
																name="dollourNewName" style="vertical-align: middle;"></td>
														</tr>
														<tr>
															<td colspan="2"><label
																style="vertical-align: middle;">Always Print </label><input
																type="checkbox" id="alwaysNewNameID"
																name="alwaysNewName" style="vertical-align: middle;"></td>
															<td colspan="2"><label
																style="vertical-align: middle;">Underline </label><input
																type="checkbox" id="underlineNewNameID"
																name="underlineNewName" style="vertical-align: middle;"></td>
														</tr>
													</table>
													<table style="width: 330px;">
														<tr>
															<td><label style="vertical-align: middle;">Vertical
																	Spacing: </label></td>
															<td colspan="3"><select style="width: 150px;"
																id="verticalSpacingNew" name="verticalSpacingNewName">
																	<option value="0">None</option>
																	<option value="1">1</option>
																	<option value="2">2</option>
																	<option value="3">3</option>
																	<option value="4">4</option>
																	<option value="5">5</option>
																	<option value="6">6</option>
																	<option value="7">7</option>
																	<option value="8">8</option>
																	<option value="9">9</option>
																	<option value="10">New Page</option>
															</select></td>
														</tr>
														<tr>
															<td><label style="vertical-align: middle;">Tab
																	Indentation: </label></td>
															<td colspan="3"><select style="width: 150px;"
																id="tabIndentationNewID" name="tabIndentationNewName">
																	<option value="0">None</option>
																	<option value="1">1</option>
																	<option value="2">2</option>
																	<option value="3">3</option>
																	<option value="4">4</option>
																	<option value="5">5</option>
																	<option value="6">6</option>
																	<option value="7">7</option>
																	<option value="8">8</option>
																	<option value="9">9</option>
																	<option value="10">Center</option>
															</select></td>
														</tr>
														<tr>
															<td><label style="vertical-align: middle;">Balance
																	Sheet Column: </label></td>
															<td><select style="width: 150px;"
																id="balanceSheetColumnNewID"
																name="balanceSheetColumnNewName">
																	<option value="1">1</option>
																	<option value="2">2</option>
																	<option value="3">3</option>
															</select></td>
														</tr>
														<tr>
															<td><label style="vertical-align: middle;">Line
																	above amount: </label></td>
															<td><select style="width: 150px;"
																id="lineAboveAmountNewID" name="lineAboveAmountNewName">
																	<option value="0">None</option>
																	<option value="1">Single</option>
																	<option value="2">Double</option>
															</select></td>
														</tr>
														<tr>
															<td><label style="vertical-align: middle;">Line
																	below amount: </label></td>
															<td><select style="width: 150px;"
																id="lineBelowAmountNew" name="lineBelowAmountNewName">
																	<option value="0">None</option>
																	<option value="1">Single</option>
																	<option value="2">Double</option>
															</select></td>
														</tr>
													</table>
												</div>
											</fieldset>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
					<br>
					<hr>
					<table align="right" style="width: 745px">
						<tr>
							<td align="right" style="padding-right: 25px;"><input
								type="button" id="saveUserButton" name="saveUserButtonName"
								value="Save & Close" class="savehoverbutton turbo-blue"
								onclick="saveNewChart()" style="width: 120px;"> <input
								type="button" id="cancelUserButton" name="cancelUserButtonname"
								value="Cancel" class="cancelhoverbutton turbo-blue"
								style="width: 80px;" onclick="cancelAddChart()"></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div style="display: none;">
		<input type="text" id="chartsAccID" name="chartsAccName"
			value="${requestScope.userDetails.chartsperpage}">
	</div>
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/chartofaccounts.js"></script>

	<script type="text/javascript">
		$(function() {

			$(".segment2").hide();
			$(".segment3").hide();
			$(".segment4").hide();

			loadchsegment();

		})
		function loadchsegment() {
			var i = 1;

			<c:forEach var="ch" items="${requestScope.chsegments}">

			//	console.log("<c:out value="${ch.segmentsName}"></c:out>");

			$(".segment" + i).show();
			$("#numberNameID" + i).attr('maxlength','<c:out value="${ch.digitsallowed}"></c:out>');
			$("#seg" + i).html("<c:out value='${ch.segmentsName}'></c:out>");
			$("#segment"+i+"length").val('<c:out value="${ch.digitsallowed}"></c:out>');

			$("#segmentCount").val(i);

			i++;
			</c:forEach>

		}
	</script>

	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script>-->
</body>
</html>