<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Accounting Cycles</title>
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

				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 800px; margin: 0 auto; background-color: #FAFAFA; height: 550px;">
							<ul>
								<li id=""><a href="#chartsDetailsDiv">Details</a></li>
								<li style="float: right; padding-right: 10px; padding-top: 3px;">
									<label id="chartsName"
									style="color: white; vertical-align: middle;"></label>
								</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id=chartsDetailsFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td style="vertical-align: top;">
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Accounting Cycles</b></label>
																</legend>
																<table>
																<b>
																	<tr>
																		<td>Accounting Basics </td><td>:</td>
																		<td><c:set var="account" value=""></c:set> <c:choose>
																				<c:when test="${sysInfo.cashBasisAccounting}">
																					<c:set var="account" value="Cash"></c:set>
																				</c:when>
																				<c:when test="${!sysInfo.cashBasisAccounting}">
																					<c:set var="account" value="Accrual"></c:set>
																				</c:when>
																			</c:choose> 
																			<label id="accountingBasics"> <c:out value="${account}" /></label>
																			<input type="hidden" id="accountingBasicsId"
																			name="accountingBasicsId"
																			value="<c:out value="${sysInfo.sysInfoId}" />"
																			 />
																			</td>
																	</tr>
																	<tr>
																		<td>Current Period </td><td>:</td>
																		
																		<td>
																		
																		<input type="hidden" id="currentPeriodforref"
																			name="currentPeriodforref" readonly="readonly" value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentPeriod.startDate}" />' /> 
																            
																            
																		<label type="text" id="currentPeriod">
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentPeriod.startDate}"/> - 
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentPeriod.endDate}" />
																		</label>
																            <input type="hidden" id="currentPeriodId"
																			name="currentPeriodId"
																			value="<c:out value="${currentPeriod.coFiscalPeriodId}" />"
																			 />
																            </td>
																	</tr>
																	<tr>
																		<td>Current Fiscal Year </td><td>:</td>
																		<td>
																		
																		<input type="hidden" id="currentFiscalYearforref"
																			name="currentFiscalYearforref" readonly="readonly"
																			value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentYear.startDate}" />' />
																		
																		<label id="currentFiscalYear">
																		
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.startDate}" /> - 
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.endDate}" />
																            </label>
																            <input type="hidden" id="currentYearId"
																			name="currentYearId"
																			value="<c:out value="${currentPeriod.coFiscalYearId}" />"
																			 />
																            </td>
																	</tr>
																	<tr><td colspan="3">&nbsp;</td>
																	<tr>
																		<td ><input type="button"
																			class="savehoverbutton turbo-blue"
																			value="Close Period" onclick="closePeriodDialogForm()"></td>
																			<td></td>
																		<td><input type="button"
																			style="float: right; margin-right: 10px;"
																			class="savehoverbutton turbo-blue"
																			value="Reverse Period" onclick="reversePeriodDialogForm()"></td>
																			
																			
																	</tr>
																	</b>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
											<td style="vertical-align:top;padding-top: 10px;padding-left: 15px;">
											<table>
												<tr><td> <span style=""><input type="checkbox" id="chk_period1" value="1"></span></td><td><input type="text" id="txt_periodstart1" style="width: 90px;" data-stvalue="1"></td><td>-</td><td> <input type="text" id="txt_periodend1"  style="width: 90px;" data-endvalue="1"> Period1</td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period2" value="2"></span></td><td><input type="text" id="txt_periodstart2"  style="width: 90px;" data-stvalue="2"></td><td>-</td><td> <input type="text" id="txt_periodend2"  style="width: 90px;" data-endvalue="2"> Period2</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period3" value="3"></span></td><td><input type="text" id="txt_periodstart3"  style="width: 90px;" data-stvalue="3"></td><td>-</td><td> <input type="text" id="txt_periodend3"  style="width: 90px;"  data-endvalue="3"> Period3</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period4" value="4"></span></td><td><input type="text" id="txt_periodstart4"  style="width: 90px;"  data-stvalue="4"> </td><td>-</td><td> <input type="text" id="txt_periodend4"  style="width: 90px;" data-endvalue="4"> Period4</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period5" value="5"></span></td><td><input type="text" id="txt_periodstart5"  style="width: 90px;" data-stvalue="5"></td><td>-</td><td> <input type="text" id="txt_periodend5"  style="width: 90px;" data-endvalue="5"> Period5</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period6" value="6"></span></td><td><input type="text" id="txt_periodstart6"  style="width: 90px;" data-stvalue="6"></td><td>-</td><td> <input type="text" id="txt_periodend6"  style="width: 90px;" data-endvalue="6"> Period6</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period7" value="7"></span></td><td><input type="text" id="txt_periodstart7"  style="width: 90px;" data-stvalue="7"></td><td>-</td><td> <input type="text" id="txt_periodend7"  style="width: 90px;" data-endvalue="7"> Period7</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period8" value="8"></span></td><td><input type="text" id="txt_periodstart8"   style="width: 90px;"data-stvalue="8"></td><td>-</td><td> <input type="text" id="txt_periodend8"  style="width: 90px;" data-endvalue="8"> Period8</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period9" value="9"></span></td><td><input type="text" id="txt_periodstart9"  style="width: 90px;" data-stvalue="9"></td><td>-</td><td> <input type="text" id="txt_periodend9"  style="width: 90px;" data-endvalue="9"> Period9</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period10" value="10"></span></td><td><input type="text" id="txt_periodstart10"  style="width: 90px;" data-stvalue="10"></td><td>-</td><td> <input type="text" id="txt_periodend10"  style="width: 90px;" data-endvalue="10"> Period10</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period11" value="11"></span></td><td><input type="text" id="txt_periodstart11"  style="width: 90px;"  data-stvalue="11"></td><td>-</td><td> <input type="text" id="txt_periodend11"  style="width: 90px;" data-endvalue="11"> Period11</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period12" value="12"></span></td><td><input type="text" id="txt_periodstart12"  style="width: 90px;"  data-stvalue="12"></td><td>-</td><td> <input type="text" id="txt_periodend12"  style="width: 90px;" data-endvalue="12"> Period12</span></td></tr>
												<tr><td>  <span style=""><input type="checkbox" id="chk_period13" value="13"></span></td><td><input type="text" id="txt_periodstart13"  style="width: 90px;"  data-stvalue="13"></td><td>-</td><td> <input type="text" id="txt_periodend13"  style="width: 90px;" data-endvalue="13"> Period13</span></td></tr>
											 </table>
											</td>
											</tr>
									</table>
								</form>
								
							</div>
							<div>
							<span  style="float:right;margin-right:50px;"><input type ="button" class="savehoverbutton turbo-blue" value="Save" onclick="savePeriod()"></span>
							</div>

						</div>
					</div>
				</td>
			</tr>
		</table>
		<div id="reversePeriodDialog">
			<form action="" id="reversePeriodDialogFormID">
			<table style="padding-top: 8px;">
										<tr>
											<td>
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Reverse Period</b></label>
																</legend>
																<table>
																		<tr>
																		<td>Current Period :</td>
																		<td>
																		<input type="hidden" name="editreverseYearId" id="editreverseYearId"/>
																		<input type="hidden" name="editsysInfoId" id="editsysInfoId"/>
																		
																		<select name="editreversePeriodId" id="editreversePeriodId">
																		
																		<c:forEach items="${currentPeriodList}" var="cPeriod">
																		<c:if test="${cPeriod.coFiscalPeriodId eq currentPeriod.coFiscalPeriodId}" >
																	<option value="<c:out value="${cPeriod.coFiscalPeriodId}" />" selected="selected"><fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${cPeriod.startDate}" /> - <fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${cPeriod.endDate}" /></option>
																		</c:if>
																		<c:if test="${cPeriod.coFiscalPeriodId ne currentPeriod.coFiscalPeriodId}" >
																		<option value="<c:out value="${cPeriod.coFiscalPeriodId}" />"><fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${cPeriod.startDate}" /> - <fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${cPeriod.endDate}" /></option>
																		</c:if>
																		
																		</c:forEach>
																		</select>
																		   </td>
																	</tr>
																	<tr>
																		<td><input type="button"
																			style="float: right; margin-right: 10px;"
																			class="savehoverbutton"
																			value="Submit" onclick="reversePeriod()">
																			</td><td>
																			<input type="button"
																			class="savehoverbutton"
																			value="Close" onclick="closeReverse()"></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
									</table>
			</form>
			</div>
		
<div id="closePeriodDialog">
			<form action="" id="closePeriodDialogFormID">
			<table style="padding-top: 8px;">
										<tr>
											<td>
												<table>
												<tr><td><span style="color:Green;"><center>To begin using the accounting you must enter your opening balances
															for all accounts via Journal Entries. Start here by setting
			                                                the period to the period prior to the one you will be starting with
			                                                Are You Sure above dates are Correct? </center></span>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Close Period</b></label>
																</legend>
																
								<table>
								
								<tr> 
								<td>Next Period From :</td>
								
									<td>
										<input type="text" readonly="true" id="clPeriodStart" style="width: 100px; background-color: gainsboro;"
									 		value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentPeriod.endDate}" />"/>
			                    		<input type="hidden" id="currentYearId" name="currentYearId" value="<c:out value="${currentPeriod.coFiscalPeriodId}" />"/>
									</td>
									<td>Through :</td>
									<td>
										<input type="text" id="clPeriodEnd" style="width: 100px;"/>
										<input type="hidden" id="currentYearId" name="currentYearId" value="<c:out value="${currentPeriod.coFiscalPeriodId}" />"/>
								
									</td>
								</tr>
								
								
								<tr>
									<td>Next Year From :</td>
									<td>
										<input type="text" readonly="true"  id="clPeriodYearStart" style="width: 100px; background-color: gainsboro;" 
										value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYearforclosedyearDlg.startDate}"/>"/>
										<input type="hidden" id="currentYearId" name="currentYearId" value="<c:out value="${currentPeriod.coFiscalYearId}" />"/>
									</td>
									<td>Through :</td>
									<td>
										<input type="text" readonly="true" id="clPeriodYearEnd" style="width: 100px; background-color: gainsboro;"
										 value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYearforclosedyearDlg.endDate}" />"/>
									<input type="hidden" id="currentYearId" name="currentYearId" value="<c:out value="${currentPeriod.coFiscalYearId}" />"/>
								
									</td>
								</tr>
								
								<tr>
								<td>&nbsp;</td><td>&nbsp;</td>
								<td>
									<input type="hidden" name="closesysInfoId" id="closesysInfoId"/>
									<input type="button"
											style="float: right; margin-right: 10px;"
											class="savehoverbutton"
											value="Submit" onclick="closePeriodSubmit()"></td>
										<td><input type="button"
											class="savehoverbutton "
											value="Close" onclick="closePeriod()"></td>
										
									</tr>
								</table>
								
																
																
															<!-- 	<table>
																	<tr>
																		<td>Next Period :</td>
																		<td><input type="text" id="nextPeriodFrom"
																			name="nextPeriodFrom" value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentPeriod.startDate}" />' />
																            </td>
																            <td>Through :</td>
																		<td><input type="text" id="nextPeriodTo"
																			name="nextPeriodTo"  value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentPeriod.endDate}" />' />
																            </td>
																	</tr>
																		<tr>
																		<td>Next Year :</td>
																		<td><input type="text" id="nextYearFrom"
																			name="nextPeriodFrom"  value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentYear.startDate}" />' />
																            </td>
																            <td>Through :</td>
																		<td><input type="text" id="nextYearTo"
																			name="nextPeriodTo"  value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentYear.endDate}" />' />
																            </td>
																	</tr>
																	<tr><td>
																	<input type="hidden" name="closesysInfoId" id="closesysInfoId"/>
																	<input type="button"
																			style="float: right; margin-right: 10px;"
																			class="savehoverbutton"
																			value="Submit" onclick="closePeriodSubmit()"></td>
																		<td><input type="button"
																			class="savehoverbutton "
																			value="Close" onclick="closePeriod()"></td>
																		
																	</tr>
																</table> -->
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
									</table>
			</form></div> 

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
		src="./../resources/scripts/turbo_scripts/accountingcycles.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
	
</body>
</html>