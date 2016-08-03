<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
	width: 80px;
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
			<jsp:include page="./headermenu.jsp"></jsp:include>
		</div>
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td style="padding-right: 20px;">
					<table id="chartsOfTermsGrid"></table>
					<div id="chartsOfTermsGridPager"></div>
				</td>
				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 676px; margin: 0 auto; background-color: #FAFAFA; height: 440px;">
							<ul>
								<li id=""><a href="#chartsDetailsDiv">Payment Terms</a></li>
								<li style="float: right; padding-right: 10px; padding-top: 3px;">
									<label id="chartsName"
									style="color: white; vertical-align: middle;"></label>
								</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id=paymentTermsFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Payment Terms</b></label>
													</legend>
													<table>
														<tr>
															<td>Description :</td>
															<td colspan="2"><input type="text" id="description"
																name="description" /></td>
														</tr>
														<tr>
															<td>Set as a Global :</td>
															<td><input type="checkbox" id="global" name="global" /></td>
															<td>Inactive :</td>
															<td><input type="checkbox" id="active" name="active" />
															</td>
														</tr>
													</table>
												</fieldset>
											</td>
											<td><fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Terms Condition</b></label>
													</legend>
													<table>
														<tr>
															<td>Payment Due : Net</td>
															<td><input type="text" name="dueDay" id="dueDay"
																style="width: 15px" /> <select name="dueDayoption"
																id="dueDayoption">
																	<option value="0" selected="yes">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
														<tr>
															<td>Discount :</td>
															<td><input type="text" name="discountPercent"
																id="discountPercent" style="width: 30px" /> <input
																type="text" name="discountDay" id="discountDay"
																style="width: 15px" /> <select name="discountDayoption"
																id="discountDayoption">
																	<option value="0" selected="yes">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Order Note</b></label>
													</legend>
													<table>
														<tr>
															<td><textarea id="orderNote" name="orderNote"
																	style="width: 627px"></textarea></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Pick Ticket Note</b></label>
													</legend>
													<table>
														<tr>
															<td><input type="text" id="pickTicket1"
																name="pickTicket1" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket2"
																name="pickTicket2" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket3"
																name="pickTicket3" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket4"
																name="pickTicket4" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket5"
																name="pickTicket5" style="width: 627px" /></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td align="right" style="padding-right: 25px;">
											 <input
												type="button" id="addButton" name="addButtonName"
												value="Add" class="add"
												onclick="openAddNewTermsDialog()">
											<input
												type="hidden" name="cuTermsId" id="cuTermsId" /> <input
												type="button" id="saveButton" name="saveButtonName"
												value="Save" class="savehoverbutton turbo-blue"
												onclick="editData()">
												 <input
												type="button" id="deleteButton" name="deleteButtonName"
												value="Delete" class="savehoverbutton turbo-blue"
												onclick="deletePaymentTerms()"></td>
										</tr>
									</table>
								</form>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>

<div id="addNewTermsDialog">
			<form action="" id="addNewTermsFormID">
					<table style="padding-top: 8px;">
										<tr>
											<td>
												<fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Payment Terms</b></label>
													</legend>
													<table>
														<tr>
															<td>Description :</td>
															<td colspan="2"><input type="text" id="description"
																name="description" /></td>
														</tr>
														<tr>
															<td>Set as a Global :</td>
															<td><input type="checkbox" id="global" name="global"  /></td>
															<td>Inactive :</td>
															<td><input type="checkbox" id="active" name="active" />
															</td>
														</tr>
													</table>
												</fieldset>
											</td>
											<td><fieldset class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Terms Condition</b></label>
													</legend>
													<table>
														<tr>
															<td>Payment Due : Net</td>
															<td><input type="text" name="dueDay" id="dueDay"
																style="width: 15px" /> <select name="dueDayoption"
																id="dueDayoption">
																	<option value="0" selected="selected">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
														<tr>
															<td>Discount :</td>
															<td><input type="text" name="discountPercent"
																id="discountPercent" style="width: 30px" /> <input
																type="text" name="discountDay" id="discountDay"
																style="width: 15px" /> <select name="discountDayoption"
																id="discountDayoption">
																	<option value="0" selected="selected">days</option>
																	<option value="1">th</option>
															</select></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Order Note</b></label>
													</legend>
													<table>
														<tr>
															<td><textarea id="orderNote" name="orderNote"
																	style="width: 627px"></textarea></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
										<tr>
											<td colspan="2"><fieldset
													class=" ui-widget-content ui-corner-all"
													style="margin-left: 5px;">
													<legend>
														<label><b>Pick Ticket Note</b></label>
													</legend>
													<table>
														<tr>
															<td><input type="text" id="pickTicket1"
																name="pickTicket1" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket2"
																name="pickTicket2" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket3"
																name="pickTicket3" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket4"
																name="pickTicket4" style="width: 627px" /></td>
														</tr>
														<tr>
															<td><input type="text" id="pickTicket5"
																name="pickTicket5" style="width: 627px" /></td>
														</tr>
													</table>
												</fieldset></td>
										</tr>
									</table>
					<br>
					<hr>
					<table align="right">
						<tr>
							<td align="right" style="padding-right:25px;">
							<input type="hidden" id="cuTermsId" name="cuTermsId" value="0"/>
							<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="addData()" style=" width:120px;">
							<input type="button" id="cancelTermsButton" name="cancelTermsButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddTerms()"></td>
						</tr>
					</table>
			</form>
		</div>
		<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
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
		src="./../resources/scripts/turbo_scripts/paymentsterms.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>