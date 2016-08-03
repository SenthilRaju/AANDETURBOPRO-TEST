<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Divisions</title>
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
			<table style="width:979px;margin:0 auto;padding-top:5px">
<!-- 				<tr> -->
<!-- 					<td style="padding-left: 280px;"> -->
<!-- 						<table><tr><td><input type="button" value="  Add" class="add" id="addDivisionList" onclick="openAddNewDivisionDialog()"></td></tr></table> -->
<!-- 			    	</td> -->
<!-- 			    </tr> -->
				<tr>
					<td style="padding-right: 20px;vertical-align:top;">
						<table id="divisionsGrid"></table>
<!-- 						<table><tr><td style="padding-left: 280px;padding-top: 20px"><input type="button" value="  Add" class="add" id="addDivisionList" onclick="openAddNewDivisionDialog()"></td></tr></table> -->
					</td>
					<td style="vertical-align:top;">
						<div id="divisionsDetailsTab">
							<div class="division_tabs_main" style="padding: 0px;width:800px;margin:0 auto; background-color: #FAFAFA;height: 400px;">
								<ul>
									<li id=""><a href="#divisionDetailsDiv">Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
									<label id="divisionsName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
								<div id="divisionDetailsDiv" style="padding: 8px;">
									<form id="divisionDetailsFromID">
									<div id="descriptionWarning" style="color: red">Alert :Please Specify valid Description</div>
									<div id="addressWarning" style="color: red" >Alert :Please Specify valid Address</div>
									<div id="chAccountWarning" style="color: red" ></div>
									
										<table style="padding-top: 8px;">
											<tr>
												<td style="vertical-align: top;padding-top: 8px;">
													<table>	
														<tr>
															<td>
																<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
																	<legend><label><b>Division</b></label></legend>
																	<table>
																		<tr>
																			<td style="display:none;"><label>CoDivisionId: </label><input type="text" name="coDivisionIdName" id="coDivisionID" style="width:200px"></td>
																			<td><label>Description: </label><span class="mandatory" style="color:red;">*</span></td>
																			<td><input type="text" name="coDivisionName" id="CoDivisionNameID" value=""></td></tr>
																			<tr><td><label>Code: </label></td><td><input type="text" name="coDivisionCode" id="CoDivisionCodeID" maxlength="10">
																			<td><label style="vertical-align: middle;">InActive: </label></td>
																			<td><input type="checkbox" name="inActiveName" id="inActiveNameID"></td>
																		</tr>
																		<tr>
																		<td width=200px>
																			<label>Invoice Sequence Number</label>
																			<td><input type="checkbox" name="useInvSeqNo"  id="useInvSeqNoId">
																			<input type="text" name="inSeqNo" id="invSeqNoID" style="width:125px"></td>
																		</tr>
																		<tr>
																			<td><label id="segment2name">${requestScope.chsegments}</label><span class="mandatory" style="color:red;">*</span></td>
																			<td><select id="bankAccountsID" name="subAccountName" style="width: 150px;">
																			<option value="-1"> -Select- </option>
																				<c:forEach var="chaccounts" items="${requestScope.bankAccounts1}">
																				<option value="${chaccounts.number}">
<!-- 																				<script>alert("<c:out value='${chaccounts.coAccountId}'></c:out>");</script> -->
																						<c:out value="${chaccounts.number}"></c:out>
																				</option>
																			</c:forEach>
																			</select>
																			
<!-- 																			<input type="text" name="subAccountName" id="subAccountID"></td> -->
																		</tr>
																			<tr>
																			<td><label>Account Dist. Percentage </label></td>
																			<td><input type="text" name="accountDisPercent" id="accountDisPercentID" ></td>
																		</tr>
																	</table>
																</fieldset>
															</td>
														</tr>
														<tr>
															<td>
<!-- 																<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;"> -->
<!-- 																	<legend><label><b>Alternate GL Accounts</b></label></legend> -->
<!-- 																	<table> -->
<!-- 																		<tr> -->
<!-- 																			<td><label>Specify any accounts to be substituted with an alternative posting account for  -->
<!-- 																			transactions tagged with this division</label></td> -->
<!-- 																			</tr> -->
<!-- 																			<tr> -->
<!-- 																			<td><table id="alternateGrid"></table> -->
<!-- 																			<div id="alternateGridPager"> </div> -->
<!-- 																			</td> -->
<!-- 																		</tr> -->
<!-- 																	</table> -->
<!-- 																</fieldset> -->
															</td>
														</tr>
													</table>
												</td>
												<td style="padding-top: 8px;vertical-align:top">
													<fieldset  class= " ui-widget-content ui-corner-all" style="height: 150px;">
														<legend><label><b>Address</b><span class="mandatory" style="color:red;">*</span></label></legend>
														<div>
														<table style="width: 300px;">
															<tr>
																<td>
																	<input type="text" id="addressID1" name="addressName1"
																	style="vertical-align: middle; width: 250px">
																</td>
															</tr>
															<tr>
															<td><input type="text" id="addressID2" name="addressName2" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
																<td><input type="text" id="addressID3" name="addressName3" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
															<td><input type="text" id="addressID4" name="addressName4" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
															<td><input type="checkbox" id="addressQuoteID" name="addressQuoteName" style="vertical-align: middle">
															Use on quotes as alternate address </td>
															</tr>
														</table>
														</div>
													</fieldset>
													<fieldset  class= " ui-widget-content ui-corner-all" style="height: 120px;">
														<legend><label><b>Additional Details</b></label></legend>
														<div>
														<table style="width: 300px;">
															<tr>
																<td>
																	<input type="text" id="additionalID1" name="additionalName1" style="vertical-align: middle; width:250px">
																</td>
															</tr>
															<tr>
															<td><input type="text" id="additionalID2" name="additionalName2" style="vertical-align: middle;width:250px"></td>
															</tr>
															<tr>
																<td><input type="text" id="additionalID3" name="additionalName3" style="vertical-align: middle;width:250px"></td>
															</tr>
														</table>
														</div>
													</fieldset>
												</td>
											</tr>
										</table>
									</form>
									<table>
										<tr align="right">
											<td style="align:right;padding-left: 10x; width: 770px;float:right;" align="right">
												<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveDivisions()">
												<input type="button" value="Clear" class="savehoverbutton turbo-blue" id="addDivisionList" onclick="resetform(); createtpusage('Company-Divisions','Clear Divisions','Info','Company-Divisions,Clear Division');">
												<input type="button" id="deleteDivisionID" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteDivisionDetails()">
											</td>
											</tr>
											<tr align="right">
											<td style="align:right;padding-left: 10x; width: 770px;float:right;" align="right">
											<div id="errorDIV" style="width:200px" ></div>
											</td>
											
										</tr>
									</table>
								</div>
							</div>
						</div>
					</tr>
				</table>
		</div>
		
		<div id="addNewDivisionDialog">
			<form action="" id="addNewDivisionFormID">
				<div id="newDescriptionWarning" style="color: red">Alert :Please Specify valid Description</div>
				<div id="newAddressWarning" style="color: red" >Alert :Please Specify valid Address</div>
				<div id="newchartaccountWarning" style="color: red" >Alert :Please Select </div>
				<div id="">
					<table>
						<tr>
							<td style="vertical-align:top">
								<table>	
									<tr>
										<td>
											<fieldset  class= " ui-widget-content ui-corner-all">
												<legend><label><b>Division</b></label></legend>
													<table>
														<tr>
<!-- 														<td style="display: none;"><label>Description: </label><input type="text" name="newCoDivisionName" id="newCoDivisionID" style="width:200px"></td> -->
														<td><label>Description: </label><span class="mandatory" style="color:red;">*</span></td>
														<td><input type="text" name="newCoDivisionName" id="newCoDivisionNameID" value=""></td></tr>
														<tr><td><label>Code: </label></td><td><input type="text" name="newCoDivisionCode" id="newCoDivisionCodeID" >
														<td><label style="vertical-align: middle;">InActive: </label></td>
														<td><input type="checkbox" name="newInActiveName" id="newInActiveNameID"></td>
														</tr>
														<tr>
														<td>
														<label>Invoice Sequence Number</label>
														<td><input type="checkbox" name="newUseInvSeqNo"  id="newUseInvSeqNoId">
														<input type="text" name="newInSeqNo" id="newInvSeqNoID" style="width:125px"></td>
														</tr>
														<tr>
														<td><label id="segment2name">${requestScope.chsegments}</label><span class="mandatory" style="color:red;">*</span></td>
																			<td><select id="bankAccountsIDpop" name="newSubAccountName" style="width: 150px;">
																			<option value="-1"> -Select- </option>
																				<c:forEach var="chaccounts" items="${requestScope.bankAccounts1}">
																				<option value="${chaccounts.number}">
<!-- 																				<script>alert("<c:out value='${chaccounts.coAccountId}'></c:out>");</script> -->
																						<c:out value="${chaccounts.number}"></c:out>
																				</option>
																			</c:forEach>
																			</select>
																			</td>
<!-- 														<td><input type="text" name="newSubAccountName" id="newSubAccountID"></td> -->
														</tr>
														<tr>
														<td><label>Account Dist. Percentage </label></td>
														<td><input type="text" name="newAccountDisPercent" id="newAccountDisPercentID"></td>
														</tr>
														</table>
												</fieldset>
															</td>
														</tr>
														<tr>
															<td>
<!-- 																<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;"> -->
<!-- 																	<legend><label><b>Alternate GL Accounts</b></label></legend> -->
<!-- 																	<table> -->
<!-- 																		<tr> -->
<!-- 																			<td><label>Alternate GL accounts can be assigned after saving new division</label></td> -->
<!-- 																			<td></td> -->
<!-- 																			<td></td> -->
<!-- 																			<td></td> -->
<!-- 																		</tr> -->
<!-- 																	</table> -->
<!-- 																</fieldset> -->
															</td>
														</tr>
													</table>
												</td>
												<td>
													<fieldset  class= " ui-widget-content ui-corner-all">
														<legend><label><b>Address</b></label><span class="mandatory" style="color:red;">*</span></legend>
														<div>
														<table>
															<tr>
																<td>
																	<input type="text" id="newAddressID1" name="newAddressName1" style="vertical-align: middle; width:250px">
																</td>
															</tr>
															<tr>
															<td><input type="text" id="newAddressID2" name="newAddressName2" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
																<td><input type="text" id="newAddressID3" name="newAddressName3" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
															<td><input type="text" id="newAddressID4" name="newAddressName4" style="vertical-align: middle; width:250px"></td>
															</tr>
															<tr>
															<td><input type="checkbox" id="newAddressQuoteID" name="newAddressQuoteName" style="vertical-align: middle">
															Use on quotes as alternate address </td>
															</tr>
														</table>
														</div>
													</fieldset>
													<fieldset  class= " ui-widget-content ui-corner-all" style="height: 120px;">
														<legend><label><b>Additional Details</b></label></legend>
														<div>
														<table>
															<tr>
																<td>
																	<input type="text" id="newAdditionalID1" name="newAdditionalName1" style="vertical-align: middle; width:250px">
																</td>
															</tr>
															<tr>
															<td><input type="text" id="newAdditionalID2" name="newAdditionalName2" style="vertical-align: middle;width:250px"></td>
															</tr>
															<tr>
																<td><input type="text" id="newAdditionalID3" name="newAdditionalName3" style="vertical-align: middle;width:250px"></td>
															</tr>
														</table>
														</div>
													</fieldset>
												</td>
											</tr>
										</table>
						<table align="right" style="width:745px">
						<tr>
							<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveNewDivisions()" style=" width:120px;">
							<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddDivision()"></td>
						</tr>
						<tr>
						<td>
						<div id="errorDIVpop" style="width:200px" ></div>
						</td>
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
		<div style="display: none;">
			<input type="text" id="divisionsID" name="divisionsName" value="${requestScope.userDetails.chartsperpage}">
		</div>
		 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/divisionsPage.js"></script>
	</body>
</html>