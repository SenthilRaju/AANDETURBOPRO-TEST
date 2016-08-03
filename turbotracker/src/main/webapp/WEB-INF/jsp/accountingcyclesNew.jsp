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
							style="padding: 0px; width: 1050px; margin: 0 auto; background-color: #FAFAFA; height: 675px;">
							<ul>
							<li style="color:white;padding-left:10px;padding-bottom:5px;">Accounting Cycle</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">
								<form id="chartsDetailsFromID">
									<table style="padding: 10px;" width=100%>
										<tr>
											<td>
												<table width=100%>
													<tr>
														<td>
														<div>
														
														<input type="hidden" id="accountingBasicsId" name="accountingBasicsId" value="<c:out value="${sysInfo.sysInfoId}" />" />
														<input type="hidden" id="currentPeriodId"	name="currentPeriodId"	value="<c:out value="${sysInfo.currentPeriodId}" />" />
														<input type="hidden" id="currentfiscalYearId" name="currentfiscalYearId" value="<c:out value="${sysInfo.currentFiscalYearId}" />" />
														
														<span><label><b>Fiscal Year</b></label></span>
														<span><select id="drp_yearList" style="width:100px;">
														<c:forEach items="${fiscalYearList}" var="fyear">
																		<c:if test="${fyear.coFiscalYearId eq currentYear.coFiscalYearId}" >
																			<option value="<c:out value="${fyear.coFiscalYearId}" />" selected="selected" data-description="<c:out value='${fyear.description}'/>" data-yearvalue="<c:out value='${fyear.fiscalYear}'/> "><c:out value="${fyear.fiscalYear}" /></option>
																		</c:if>
																		<c:if test="${fyear.coFiscalYearId ne currentYear.coFiscalYearId}" >
																			<option value="<c:out value="${fyear.coFiscalYearId}" />"><c:out value="${fyear.fiscalYear}" /></option>
																		</c:if>
														</c:forEach>
														
														<%-- <label id="currentFiscalYear">
																		
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.startDate}" /> - 
																			<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.endDate}" />
																            </label> --%>
														
														
														</select></span>
														<span><input type ="button" class="savehoverbutton turbo-blue" style="width:60px;" value="Add" onclick="cofiscalyearadd()"></span>
														</div>
														</td>
														<td>
														<span><label><b>Name<span style="color:red;">*</span></b></label></span>
														<span><input type ="text"  style="width:120px;" id="txt_name" maxlength="10" class="number"></span>
														</td>
														<td>
														<span><label><b>Start Date<span style="color:red;">*</span></b></label></span>
														<span><input type ="text"  style="width:110px;" id="dtp_startDate" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.startDate}" />" ></span>
														</td>
														<td>
														<span><label><b>End Date<span style="color:red;">*</span></b></label></span>
														<span><input type ="text"  style="width:110px;" id="dtp_endDate" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentYear.endDate}" />" ></span>
														</td>
														<td>
														</span>
														<span><input type ="button" class="savehoverbutton turbo-blue" style="width:125px;display:inline;" value="Close Fiscal Year" id="closefiscalyearid" onclick="closefiscalYear()"></span>
														</div>
														</td>
													</tr>
													<tr>
														<td colspan="5">
														<div style="padding-top:10px;margin:0 auto;">
														<span style="vertical-align:50px"><label><b>Description</b></label></span>
														<span><textarea rows="6" cols="113.5" style="resize: none;" id="txa_Description"></textarea></span>
														</div>
														</td>
													</tr>
													<tr>
														<td colspan="5" >
															<fieldset style="width:1000px;height:415px;">
																<legend>
																	<label><b>Periods</b></label>
																</legend>
																<table width="80%" style=" margin:0 auto;">
																	<tr>
																		<td>
																		</td>
																		<td align="center">
																		<span><label><b><u>Open</u></b></label></span>
																		</td>
																		<td align="center">
																		<span><label><b><u>Closed</u></b></label></span>
																		</td>
																		<td align="center">
																		<span><label><b><u>Start Date</u></b></label></span>
																		</td>
																		<td align="center">
																		<span><label><b><u>End Date</u></b></label></span>
																		</td>
																		<td align="center">
																		<span><label><b><u>Period</u></b></label></span>
																		</td>
																		<td align="center">
																		<span><label><b><u>Activity</u></b></label></span>
																		</td>
																	</tr>
																	<tr>
																		<td align="right">
																		<span id="green_arrow1" style="display:none"><label><img alt="green_arrow"   width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad1" id="rad_open1" value="1"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad1" id="rad_close1" value="1"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart1"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend1" onchange="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#1</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity1">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span id="green_arrow2" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad2" id="rad_open2" value="2"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio" name="rad2" id="rad_close2" value="2"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart2"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend2" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#2</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity2">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow3" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio" name="rad3" id="rad_open3" value="3"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad3" id="rad_close3" value="3"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart3"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend3" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#3</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity3">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow4" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad4" id="rad_open4" value="4"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad4" id="rad_close4" value="4"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart4"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend4" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#4</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity4">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow5" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad5" id="rad_open5" value="5"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad5" id="rad_close5" value="5"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart5"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend5" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#5</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity5">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow6" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad6" id="rad_open6" value="6"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad6" id="rad_close6" value="6"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart6"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend6" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#6</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity6">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow7" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad7" id="rad_open7" value="7"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad7" id="rad_close7" value="7"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart7"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend7" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#7</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;"  id="activity7">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow8" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad8" id="rad_open8" value="8"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad8" id="rad_close8" value="8"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart8"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend8" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#8</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity8">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow9" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad9" id="rad_open9" value="9"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad9" id="rad_close9" value="9"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart9"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend9" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#9</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity9">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow10" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad10" id="rad_open10" value="10"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad10" id="rad_close10" value="10"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart10"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend10" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#10</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity10">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow11" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad11" id="rad_open11" value="11"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad11" id="rad_close11" value="11"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart11"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend11" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#11</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity11">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow12" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad12" id="rad_open12" value="12"></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad12" id="rad_close12" value="12"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart12"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend12" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#12</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity12">0</span></b></label>
																		</td>
																	</tr>
																	
																	<tr>
																		<td align="right">
																		<span  id="green_arrow13" style="display:none"><label><img alt="green_arrow" width=35px; src="../resources/Icons/green_arrow.png" ></b></label></span>
																		</td>
																		<td align="center">
																		<span style="display: none;"><input type ="radio"  name="rad13" id="rad_open13" value="13" ></span>
																		</td>
																		<td align="center">
																		<span><input type ="radio"  name="rad13" id="rad_close13" value="13"  disabled ></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodstart13"></span>
																		</td>
																		<td align="center">
																		<span><input type ="text"  style="width:120px;" id="txt_periodend13" onClick="period_change(this.id)"></span>
																		</td>
																		<td align="center">
																		<span style="text-align:center;"><label><b>#13</b></label></span>
																		</td>
																		<td align="center">
																		<label><b><span style="text-align:center;" id="activity13">0</span></b></label>
																		</td>
																	</tr>
																	
																</table>
															</fieldset>
														</td>
													</tr>
													<tr>
														<td  colspan="5" >
														<div style="float:right; padding-right:20px;padding-top:5px;">
														<span style="padding-right:10px;color:green;font-weight:bold" id="statusMsg"></span>
														<!-- <span style="padding-right:10px;"><input type ="button" class="savehoverbutton turbo-blue" style="width:60px;" value="Cancel"></span> -->
														<span><input type ="button" class="savehoverbutton turbo-blue" style="width:60px;" value="Save" onclick="savecoFiscalPeriods()"></span>
														</div>
														</td>
													</tr>
												</table>
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
		 <div id="LoadingDialog" style ="display:none; width:980px; height:485px;z-index:5000; top:251px;  position: absolute; background: rgba(255,255,255,0.4);">
			<span style="margin: 0 auto;"><img src='../resources/images/loaderforcupay.gif' style ="margin-top:300px;margin-left:600px;width:150px;"></span>		
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
	<script type="text/javascript" 	src="./../resources/scripts/turbo_scripts/accountingcycles.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
	
</body>
</html>