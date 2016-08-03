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
											<td>
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Accounting Cycles</b></label>
																</legend>
																<table>
																	<tr>
																		<td>Accounting Basics :</td>
																		<td><c:set var="account" value=""></c:set> <c:choose>
																				<c:when test="${sysInfo.cashBasisAccounting}">
																					<c:set var="account" value="Cash"></c:set>
																				</c:when>
																				<c:when test="${!sysInfo.cashBasisAccounting}">
																					<c:set var="account" value="Accrual"></c:set>
																				</c:when>
																			</c:choose> <input type="text" id="accountingBasics"
																			name="accountingBasics"
																			value="<c:out value="${account}" />"
																			readonly="readonly" />
																			<input type="hidden" id="accountingBasicsId"
																			name="accountingBasicsId"
																			value="<c:out value="${sysInfo.sysInfoId}" />"
																			 />
																			</td>
																	</tr>
																	<tr>
																		<td>Current Period :</td>
																		<td><input type="text" id="currentPeriod"
																			name="currentPeriod" readonly="readonly" value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentPeriod.startDate}" /> - <fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentPeriod.endDate}" />' />
																            <input type="hidden" id="currentPeriodId"
																			name="currentPeriodId"
																			value="<c:out value="${currentPeriod.coFiscalYearId}" />"
																			 />
																            </td>
																	</tr>
																	<tr>
																		<td>Current Fiscal Year :</td>
																		<td><input type="text" id="currentFiscalYear"
																			name="currentFiscalYear" readonly="readonly"
																			value='<fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentYear.startDate}" /> - <fmt:formatDate pattern="MM/dd/yyyy" 
																            value="${currentYear.endDate}" />' />
																            <input type="hidden" id="currentYearId"
																			name="currentYearId"
																			value="<c:out value="${currentFiscalYear.coFiscalPeriodId}" />"
																			 />
																            </td>
																	</tr>
																	<tr>
																		<td><input type="button"
																			class="savehoverbutton turbo-blue"
																			value="Close Period" onclick="closePeriodDialogForm()"></td>
																		<td><input type="button"
																			style="float: right; margin-right: 10px;"
																			class="savehoverbutton turbo-blue"
																			value="Reverse Period" onclick="reversePeriodDialogForm()"></td>
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
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Close Period</b></label>
																</legend>
																<table>
																	<tr>
																		<td>Next Period :</td>
																		<td><input type="text" id="nextPeriodFrom"
																			name="nextPeriodFrom" readonly="readonly" value='<fmt:formatDate pattern="MM/dd/yyyy" 
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
																</table>
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