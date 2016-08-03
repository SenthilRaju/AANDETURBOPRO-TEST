<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Banking Accounts</title>
		<style type="text/css">
			#mainmenuBankingPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuBankingPage a{background:url('./../resources/styles/turbo-css/images/home_white_banking.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuBankingPage ul li a{background: none; }
		</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td style="padding-left: 280px;">
						<table><tr><td><input type="button" value="  Add" class="add" id="addChartlist" onclick="addNewBankingDetails()"></td></tr></table>
			    	</td>
			    </tr>
				<tr>
					<td style="padding-right: 20px; vertical-align: top;">
						<table id="bankingAccountsGrid"></table>
						<div id="bankingAccountsGridPager"></div>
					</td>
					<td>
						<div id="bankingDetailsTab">
							<div class="banking_tabs_main" style="padding: 0px;width:1000px;margin:0 auto; background-color: #FAFAFA;height: 660px;">
								<ul>
									<li><a href="#bankingDetailsDiv">Banking Details</a></li>
									<li><a href="#transactionDetailsDiv">Transaction Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
										<label id="bankingName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
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
																			<select id="typeID" name="typeName" style="width: 130px;">
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
																			<td><label>Asset Account: </label></td>
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
																			<td><label>Deposits Default Account: </label></td>
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
																			<td><label>Interest Default Account: </label></td>
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
																			<td><label>Fees Default Account: </label></td>
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
															<td><label id="openBalanceID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<tr>
															<td><label>Deposits & Other Credits: </label></td>
															<td><label id="depositsID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<tr>
															<td><label>Withdrawals & Other Credits: </label></td>
															<td><label id="withdrawOtherCreditID"></label></td>
														</tr>
														<tr height="15px;"></tr>
														<tr>
															<td><label>Current Balance: </label></td>
															<td><label id="currentBalanceID"></label></td>
														</tr>
														<tr height="20px;"></tr>
														<tr>
															<td><label>Payable Approved: </label></td>
															<td><label id="payableApprovedID"></label></td>
														</tr>
														<tr height="5px;"></tr>
														<tr>
															<td><label>Payroll Prepared: </label></td>
															<td><label id="payablePreparedID"></label></td>
														</tr>
														<tr height="10px;"></tr>
														<tr>
															<td><label>Resulting Balance: </label></td>
															<td><label id="resultingBalanceID"></label></td>
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
																<td><label>Ck Code: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="ckCodeID" name="ckCodeName" style="width: 270px;" value=""></td>
															</tr>
															<tr>
																<td><label>ABA Routing #: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="ABARoutingID" name="ABARoutingName" style="width: 270px;" value=""></td>
															</tr>
															<tr>
																<td><label>Account #: </label></td>
															</tr>
															<tr>
																<td><input type="text" id="accountNumberID" name="accountNumberName" style="width: 270px;" value=""></td>
															</tr>
														</table>
													</fieldset>
												</td>
											</tr>
										</table>
										<br>
										<hr style="margin-top: 100px; width: 98%;">
										<table align="right" style="width:745px" id="saveAndCloseID">
											<tr>
												<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveBankingDetails()" style=" width:120px;">
												<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Delete" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="deleteBankingDetails()"></td>
											</tr>
										</table>
									</form>
								</div>
								<div id="transactionDetailsDiv">
								   <table style="width:979px;margin:0 auto;">
									<tr>
										<td>
											<div id="transactionDetailsDiv" style="padding: 0px;">
												<form id="transactionDetailsFromID">
													<table align="center">
														<tr height="10px;"></tr>
														<tr>
															<td>
																<fieldset  class= " ui-widget-content ui-corner-all" style="width: 300px;">
																	<legend><label><b>Account</b></label></legend>
																	<table>
																		<tr>
																			<td><label>Name: </label></td>
																			<td><label id="description_ID"></label></td>
																		</tr>
																		<tr height="15px;"></tr>
																		<tr>
																			<td><label>Type: </label></td>
																			<td><label id="type_ID"></label></td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="padding-right: 40px;">
																<fieldset  class= " ui-widget-content ui-corner-all" style="width: 300px;">
																	<legend><label><b>Balance</b></label></legend>
																	<table>
																		<tr>
																			<td><label>Current Balance: </label></td>
																			<td><label id="currentBalance_ID"></label></td>
																		</tr>
																		<tr height="15px;"></tr>
																		<tr>
																			<td><label>Resulting Balance: </label></td>
																			<td><label id="resultingBalance_ID"></label></td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="padding-right: 20px;">
																<input type="button" value="Transaction Filter" onclick="transactionDetails()" class="savehoverbutton turbo-blue">
															</td>
														</tr>
														<tr height="10px;"></tr>
														<tr>
															<td style="padding-right: 20px;" colspan="3">
																<table id="transactionRegisterGrid"></table>
																<div id="transactionRegisterGridPager"></div>
															</td>
														</tr>
														<tr style="display: none;">
															<td>
																<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
															</td>
													</tr>
													</table>
													<br>
													<hr>
													<table align="right" style="padding-right: 20px;">
														<tr>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:100px;" value="New Check" onclick="newCheck()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="New Transaction" onclick="newTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="Edit Transaction" onclick="editTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="Void Transaction" onclick="voidTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:130px;" value="Delete Transaction" onclick="deleteTransaction()"></td>
														</tr>
													</table>
												</form>
											</div>
											<div id="newTransactionDetails" style="display: none;">
												<form id="newTransactionFormID">
													<table style="width: 480px;">
														<tr>
															<td>
																<label style="font-size: 15px;font-weight: bold;" id="newAccountNameID"></label><br>
																<label style="font-size: 12px;font-weight: bold;" id="newAccountTypeID"></label>
															</td>
															<td align="right">
																<label style="font-weight: bold;font-size: 30px;">BANK <br> TRANSACTION</label>
															</td>
														</tr>
														<tr>
															<td><label>Date:</label></td>
															<td><input type="text" id="dateID" name="dateName" class="datepicker"></td>
														</tr>
														<tr>
															<td><label>Type:</label></td>
															<td>
																<input type="radio" id="depositID" name="typeName" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Deposit</label>
																<input type="radio" id="withdrawalID" name="typeName" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Withdrawal</label>
																<input type="radio" id="feeID" name="typeName" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Fee</label>
																<input type="radio" id="interestID" name="typeName" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Interest</label>
															</td>
														</tr>
														<tr>
															<td><label>Reference:</label></td>
															<td colspan="4"><input type="text" id="referenceID" name="referenceName"></td>
														</tr>
														<tr>
															<td><label>Description:</label></td>
															<td>
																<input type="text" id="descriptionID" name="descriptionName">
													  		</td>
														</tr>
														<tr>
															<td><label>GL Account:</label></td>
															<td>
																<select id="glAccountID" name="glAccountName">
																	<option value="-1"> - Select - </option>
																	<c:forEach var="intrestAccountBean" items="${requestScope.coAccountsDetails}">
																		<option value="${intrestAccountBean.coAccountId}">
																			<c:out value="${intrestAccountBean.number}" ></c:out>
																		</option>
																	</c:forEach>
																</select>
																<input type="button" id="multipleAccountID" value="Multiple Accounts" class="savehoverbutton turbo-blue">
																<label style="vertical-align: middle;">Reconciled? </label>
																<input type="checkbox" id="reconciledID" style="vertical-align: middle;">
															</td>
														</tr>
														<tr>
															<td><label>Amount:</label></td>
															<td colspan="4"><input type="text" id="amountID" name="amountName" class="validate[custom[number]]"></td>
														</tr>
													</table>
													<hr>
													<table align="right">
														<tr>
															<td>
																<input type="button" id="saveAddButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveTransactionDetails()" style=" width:120px;">
																<input type="button" id="saveEditButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="editTransactionDetails()" style=" width:120px;">
																<input type="button" id="saveDeleteButton" name="saveUserButtonName" value="Delete" class="savehoverbutton turbo-blue" onclick="deleteTransactionDetails()" style=" width:120px;">
																<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="savehoverbutton turbo-blue" onclick="closeTransactionDetails()" style="width: 80px;">
															</td>
														</tr>
													</table>
												</form>
											</div>
											<div id="transactionDetailsDialog" style="display: none;">
										<form id="transactionDetailsForm">
											<div>
												<table>
												  <tr>
												  		<td><label>Bank Account: </label></td>
												  		<td>
												  			<select id="bankAccountID" name="bankAccountName">
																<c:forEach var="bankingDetailsBean" items="${sessionScope.bankingDetails}">
																	<option value="${bankingDetailsBean.moAccountId}">
																		<c:out value="${bankingDetailsBean.description}" ></c:out>
																	</option>
																</c:forEach>
												  			</select>
												  		</td>
												  </tr>
												  <tr>
												  		<td><label>Transaction Types: </label></td>
												  		<td>
												  			<input type="checkbox" id="transactionDepositID" style="vertical-align: middle;"><label style="vertical-align: middle;">Deposit</label>
												  			<input type="checkbox" id="transactionWidthdrawID" style="vertical-align: middle;"><label style="vertical-align: middle;">Withdrawal</label>
												  		</td>
												  	</tr>
												  	<tr>
												  		<td></td>
												  		<td>
												  			<input type="checkbox" id="transactionCheckID"  style="vertical-align: middle;"><label style="vertical-align: middle;">Check</label>
												  			<input type="checkbox" id="transactionInterstID"  style="vertical-align: middle;"><label style="vertical-align: middle;">Interest</label>
												  		</td>
												  	</tr>
												  	<tr>
												  		<td></td>
												  		<td>
												  			<input type="checkbox" id="transactionFeeID" style="vertical-align: middle;"><label style="vertical-align: middle;">Fee</label>
												  		</td>
												  </tr>
												  <tr height="10px;"></tr>
												  <tr>
												  	<td>
												  		<label>Data Range: </label>
												  	</td>
												  	<td>
												  		<input type="text" id="fromDateRangeID" name="fromDateRangeName" class="datepicker" style="width: 80px;">
												  		<input type="text" id="toDateRangeID" name="toDateRangeName" class="datepicker" style="width: 80px;">
												  	</td>
												  </tr>
												 </table>
												 <hr>
												 <table align="right">
												 	<tr>
												 		<td>
												 			<input type="button" id="transactionDetailsID" value="View Details" onclick="viewTransactionDetails()" class="cancelhoverbutton turbo-blue">
												 		</td>
												 		<td>
												 			<input type="button" id="trasactionCancelID" value="Cancel" onclick="cancelTransactionDetails()" class="cancelhoverbutton turbo-blue">
												 		</td>
												 	</tr>
												 </table>
											</div>
										</form>
									</div>
									</table>
								</div>
								<div style="display: none;">
									<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
								</div>
							</div>
						</div>
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bankingAccounts.js"></script>
	</body>
</html>