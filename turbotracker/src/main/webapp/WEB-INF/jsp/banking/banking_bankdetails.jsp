<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

								<div style="display: none;">
									<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
									<input type="hidden" name="hiddenmoaccountid" id="hiddenmoaccountid" />
								</div>

<div id="bankingDetailsDiv" style="padding: 0px;">
									<form id="bankingDetailsFromID">
										<table align="center">
										<tr>
											<td colspan="1">
												<table>
													<tr>
														<td>
															<fieldset  class= " ui-widget-content ui-corner-all" style="width: 370px;height: 77px;">
																<legend><label><b>Account</b></label></legend>
																<table style="width: 360px;">
																	<tr>
																		<td><label>Description: </label></td>
																		<td><input type="text" id="descriptionID" name="descriptionName" style="width: 250px;" value=""></td>
																	</tr>
																	<tr>
																		<td><label>Type: </label></td>
																		<td>
																			<select id="typeID" name="typeName"  style="width: 130px;">
																				<option value="-1"> - Select - </option>
																				<option value="0"> Checking </option>
																				<option value="1"> Saving </option>
																				<option value="2"> Cash </option>
																				<option value="3"> Credit Card </option>				
																			</select>
																		<label style="padding-left: 60px;vertical-align: middle;">InActive</label><input type="checkbox" id="inActiveID" style="vertical-align: middle;"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
													<tr>
														<td>
															<fieldset class= " ui-widget-content ui-corner-all" style="width: 370px;height: 148px;">
																<legend><label><b>GL Accounts</b></label></legend>
																	<table>
																		<tr>
																			<td><label style="color: red">*</label><label>Asset Account: </label></td>
																			<td>
																				<select id="assetAccountID" name="assetAccountName">
																				<option value="-1"> - Select - </option>
																					<c:forEach var="assetAccountBean" items="${requestScope.coAccounts}">
																						<option value="${assetAccountBean.coAccountId}">
																							<c:out value="${assetAccountBean.number}" ></c:out>
																						</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr height="5px;"></tr>
																		<tr>
																			<td><label style="color: red">*</label><label>Deposits Default Account: </label></td>
																			<td>
																				<select id="depositDefultAccountID" name="depositDefultAccountName">
																					<option value="-1"> - Select - </option>
																					<c:forEach var="depositAccountBean" items="${requestScope.coAccounts}">
																						<option value="${depositAccountBean.coAccountId}">
																							<c:out value="${depositAccountBean.number}" ></c:out>
																						</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr height="5px;"></tr>
																		<tr>
																			<td><label style="color: red">*</label><label>Interest Default Account: </label></td>
																			<td>
																				<select id="interstDefultAccountID" name="interstDefultAccountName">
																					<option value="-1"> - Select - </option>
																					<c:forEach var="intrestAccountBean" items="${requestScope.coAccounts}">
																						<option value="${intrestAccountBean.coAccountId}">
																							<c:out value="${intrestAccountBean.number}" ></c:out>
																						</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																		<tr height="5px;"></tr>
																		<tr>
																			<td><label style="color: red">*</label><label>Fees Default Account: </label></td>
																			<td>
																				<select id="feesDefultAccountID" name="feesDefultAccountName">
																					<option value="-1"> - Select - </option>
																					<c:forEach var="feesAccountBean" items="${requestScope.coAccounts}">
																						<option value="${feesAccountBean.coAccountId}">
																							<c:out value="${feesAccountBean.number}" ></c:out>
																						</option>
																					</c:forEach>
																				</select>
																			</td>
																		</tr>
																	</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
											<td>
												<fieldset  class= " ui-widget-content ui-corner-all" style="width: 530px;height: 240px;">
													<legend><label><b>Balance</b></label></legend>
													<table style="width: 530px;">
														<tr>
															<td><label>Open Balance: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="openBalanceID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<tr>
															<td><label>Deposits & Other Credits: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="depositsID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<tr>
															<td><label>Withdrawals & Other Debits: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="withdrawOtherCreditID"></label></td>
														</tr>
														<tr height="15px;"></tr>
														<tr>
															<td><label>Current Balance: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="currentBalanceID"></label></td>
														</tr>
														<tr height="20px;"></tr>
														<tr>
															<td><label>Payable Approved: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="payableApprovedID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<!-- <tr>
															<td><label>Payroll Prepared: </label></td>
															<td><label id="payablePreparedID"></label></td>
														</tr> -->
														<tr height="10px;"></tr>
														<tr>
															<td><label>Resulting Balance: </label></td>
															<td style="text-align: right;padding-right: 20px;"><label id="resultingBalanceID"></label></td>
														</tr>
													</table>
												</fieldset>
											</td>
										</tr>
										</table>
										<table align="center">
											<tr>
												<td colspan="1">
													<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;">
														<legend><label><b>Account Holder Name & Address</b></label></legend>
															<table style="width: 288px;">
																<tr><td><input type="text" id="accountHolder1ID" name="accountHolder1Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="accountHolder2ID" name="accountHolder2Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="accountHolder3ID" name="accountHolder3Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="accountHolder4ID" name="accountHolder4Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="accountHolder5ID" name="accountHolder5Name" style="width: 260px;" value=""></td></tr>
															</table>
													</fieldset>
												</td>
												<td colspan="1">
													<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;">
														<legend><label><b>Bank Name & Address</b></label></legend>
															<table style="width: 288px;">
																<tr><td><input type="text" id="bankName1ID" name="bankName1Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="bankName2ID" name="bankName2Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="bankName3ID" name="bankName3Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="bankName4ID" name="bankName4Name" style="width: 260px;" value=""></td></tr>
																<tr><td><input type="text" id="bankName5ID" name="bankName5Name" style="width: 260px;" value=""></td></tr>
															</table>	
													</fieldset>
												</td>
												<td colspan="1">
													<fieldset  class= " ui-widget-content ui-corner-all" style="width: 302px;height: 155px;">
														<legend><label><b>Account Info</b></label></legend>
														<table style="width: 302px;">
															<tr>
																<td><label>Fractional #: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="ckCodeID" name="ckCodeName" maxlength="15" style="width: 270px;" value=""></td>
															</tr>
															<tr>
																<td><label>ABA Routing #: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="ABARoutingID" name="ABARoutingName" style="width: 270px;" value="" maxlength="20" class="number"></td>
															</tr>
															<tr>
																<td><label>Account #: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="accountNumberID" name="accountNumberName" style="width: 270px;" value="" maxlength="20" class="number"></td>
															</tr>
														</table>
													</fieldset>
												</td>
											</tr>
										</table>
										<br>
										<table align="center" width="880px">
											<tr>
												<td width="288px">
												</td>
												<td width="288px">
													<input type="checkbox" id="logoAppearYN" name="LogoYNvalue" style="vertical-align: middle;" ><label style="vertical-align: middle;">Logo Appear On Checks</label>
												</td>
												<td>
													<input type="radio" id="singleLineYN" name="LineNovalue" style="vertical-align: middle;" ><label style="vertical-align: middle;">Single Signature Line</label>
													<br>
													<input type="radio" id="doubleLineYN" name="LineNovalue" style="vertical-align: middle;" ><label style="vertical-align: middle;">Double Signature Line</label>
												</td>
											</tr>
										</table>
										<hr style="margin-top: 100px; width: 98%;">
										<table align="right" style="width:745px" id="saveAndCloseID">
											<tr>
												<td align="right" style="padding-right:25px;">
												<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveBankingDetails()" style=" width:120px;">
												<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Delete" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="deleteBankingDetails()"></td>
											</tr>
										</table>
									</form>
								</div>
								
<script>

$(function(){

	var grid = $("#bankingAccountsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var moAccountID = grid.jqGrid('getCell', rowId, 'moAccountId');
	loadBankingDetais(rowId);
	getSingleMoAccount(moAccountID);


	/* --------------- Validating for Numbers*/
	 $('.number').keypress(function (event) {
		    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
		        event.preventDefault();
		    }
		    var text = $(this).val();
		    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
		        event.preventDefault();
		    }
		});
})

</script>
								