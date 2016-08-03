<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - General Ledger</title>
		<style type="text/css">
			input[type='checkbox'] {
				margin-left: 0px;
				margin-right: 11px;
			}
			.accountRangeInput {width: 80px;}
			.accountRangeInputID {display: none;}
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td>
						<div id="ledgerDetailsTab">
							<div class="ledger_tabs_main" style="padding-left: 0px;width:1050px;margin:0 auto; background-color: #FAFAFA;height: 700px;">
								<ul>
									<li id=""><a href="#ledgerDetailsDiv">General Ledger</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
										<label id="chartsName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
								<div id="ledgerDetailsDiv" style="padding: 0px;">
									<form id="ledgerDetailsFromID">
										<table style="padding-top: 8px;" align="center">
											<tr>
												<td style="vertical-align: top;">
													<fieldset  class= " ui-widget-content ui-corner-all">
														<legend><label><b>Post to General Ledger</b></label></legend>
														<table>	
															<tr><td><label>Select Areas to Post Or Reset:</label></td></tr>
															<tr><td><select id="customerInvoice"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Customer Invoice & Receipts</label></td></tr>
															<tr><td><select id="vendorBills"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Vendor bills & Payments</label></td><td><input type="button" class="savehoverbutton turbo-blue" value="Post All" onclick="postAll()"></td></tr>
															<tr><td><select id="bankingPayroll"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Employee Payroll</label></td></tr>
															<tr><td><select id="bankingTransaction"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Banking Transactions</label></td><td><input type="button" class="savehoverbutton turbo-blue" value="Reset All" onclick="resetAll()"></td></tr>
															<tr><td><select id="otherInventory"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Other Inventory Transactions</label></td></tr>
															<tr><td><select id="journalEntries"><option value="0"></option><option value="1">Post</option><option value="2">Reset</option></select></td><td><label>Journal Entries</label></td><td><input type="button" class="savehoverbutton turbo-blue" value="Begin Posting to G/L"></td></tr>
														</table>
													</fieldset>
												</td>
											</tr>
											<tr>
												<td style="vertical-align: top;">
													<fieldset  class= " ui-widget-content ui-corner-all">
														<legend><label><b>Posting Status</b></label></legend>
														<table>	
															<tr><td><input type="button" class="savehoverbutton turbo-blue" style="width: 300px" value="No Posting Errors"></td></tr>
														</table>
													</fieldset>
												</td>
											</tr>
											<tr>
												<td style="vertical-align: top;">
													<fieldset  class= " ui-widget-content ui-corner-all">
														<legend><label><b>General Ledger Report</b></label></legend>
														<table>	
															<tr>
																<td>
																	<fieldset  class= " ui-widget-content ui-corner-all">
																		<legend><label><b>Accounts</b></label></legend>
																		<table>
																			<tr>
																				<td>
																					<input type="radio" id="allID" name="accountName" checked="checked">
																				</td>
																				<td>
																					<label>All</label>
																				</td>
																				<td>
																					<input type="radio" id="fromID" name="accountName">
																				</td>
																				<td>
																					<label>From</label>
																				</td>
																			</tr>
																		</table>
																	</fieldset>
																</td>
															</tr>
															<tr>
																<td>
																	<fieldset  class= " ui-widget-content ui-corner-all">
																		<legend><label><b>Periods</b></label></legend>
																		<table>
																			<tr>
																				<td>
																					<input type="radio" id="urrentPeriodID" name="periodsName" checked="checked">
																				</td>
																				<td>
																					<label>Current Period Only</label>
																				</td>
																				<td>
																					<input type="radio" id="fiscalYearID" name="periodsName">
																				</td>
																				<td>
																					<label>Fiscal Year To Date</label>
																				</td>
																				<td>
																					<input type="radio" id="fromID" name="periodsName">
																				</td>
																				<td>
																					<label>From</label>
																				</td>
																			</tr>
																		</table>
																	</fieldset>
																</td>
															</tr>
															<tr>
																<td colspan="2" style="width: 460px;">
																	<fieldset  class= " ui-widget-content ui-corner-all">
																		<legend><label><b>Options</b></label></legend>
																		<table>
																			<tr>
																				<td>
																					<input type="checkbox" id="includeAccountID" name="includeAccountName">
																				</td>
																				<td>
																					<label>Include accounts with no activity?</label>
																				</td>
																				<td>
																					<input type="checkbox" id="runningBalID" name="runningBalName" checked="checked">
																				</td>
																				<td>
																					<label>Running Balance?</label>
																				</td>
																			</tr>
																			<tr>
																				<td>
																					<input type="checkbox" id="includedetailsID" name="includedetailsName" checked="checked">
																				</td>
																				<td>
																					<label>Include detail of each transaction?</label>
																				</td>
																				<td>
																					<input type="checkbox" id="journalEntriesID" name="runningBalName">
																				</td>
																				<td>
																					<label>Journal Entries Only</label>
																				</td>
																			</tr>
																		</table>
																	</fieldset>
																</td>
															</tr>
														</table>
													</fieldset>
												</td>
											</tr>
										</table>
									</form>
								</div>
							</div>
						</div>
					</td>
				</tr>
			</table>
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/generalLedger.js"></script>
	</body>
</html>