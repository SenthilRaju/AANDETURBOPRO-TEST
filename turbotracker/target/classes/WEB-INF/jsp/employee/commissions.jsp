<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Commissions</title>
	<style type="text/css">
		#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
		#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
		#mainMenuCompanyPage ul li a{background: none; }
		#LoadingDialog {
		  width: 100%;
		  height: 100%;
		  top: 0px;
		  left: 0px;
		  position: fixed;
		  display: block;
		  opacity: 0.7;
		  background-color: #fff;
		  z-index: 99;
		  text-align: center;
		}
		.loading {
		  display: inline-block;
		  margin: 20em;
		  border-width: 50px;
		  border-radius: 50%;
		  -webkit-animation: spin 1s linear infinite;
		     -moz-animation: spin 1s linear infinite;
		       -o-animation: spin 1s linear infinite;
		          animation: spin 1s linear infinite;
		  }
	
		.style-1 {
		  border-style: solid;
		  border-color: #444 transparent;
		  }
		
		.style-2 {
		  border-style: double;
		  border-color: #637C92 transparent;
		  }
		
		.style-3 {
		  border-style: double;
		  border-color: #444 #fff #fff;
		  }
		
		@-webkit-keyframes spin {
		  100% { -webkit-transform: rotate(359deg); }
		  }
		
		@-moz-keyframes spin {
		  100% { -moz-transform: rotate(359deg); }
		  }
		
		@-o-keyframes spin {
		  100% { -moz-transform: rotate(359deg); }
		  }
		
		@keyframes spin {
		  100% {  transform: rotate(359deg); }
		  }
		
		/* Base styles */
		html {
		  background: #eee url('http://subtlepatterns.subtlepatterns.netdna-cdn.com/patterns/billie_holiday.png');
		  text-align: center;
		  }			
     </style>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			 <jsp:include page="./../headermenu.jsp"></jsp:include> 
		</div>
		<table height="100px;">
			<tr>
				<td>
				</td>
			</tr>
		</table>
		<table style="width:600px;margin:0 auto;">
			<tr>
				<td align="left" width="30%">
					<input type="button" class="savehoverbutton turbo-blue" value="New Period" onclick="clicknewPeriod()"/>
					<input id="recalculateButton" type="button" class="savehoverbutton turbo-blue" value="Recalculate" onclick="clickRecalculate()"/>
					<input id="reversePeriodButton" type="button" class="savehoverbutton turbo-blue" value="Reverse Period" onclick="clickReversePeriod()"/>
				</td>
				<td align="left" ><div id='recalculateErrorMsg' style="display: none;" ></div></td>
				<td style="position:absolute; right:100px;">
					Period Ending:<select type="text" id="endperioddate" name="enperioddate" onchange="getPrevData()"><option value="0"> -Select- </option></select>
				</td>
			</tr>	
			<tr id="commissionsList">
				<td colspan="3">
					<table id="commissionsGridList"></table>
					<div id="employeeCommissionsPopupDialog" style="display: none;height:290px;">
						<table style="padding-top: 8px;">
							<tbody>
							<tr><td>
								<table><tbody>
									<tr><td style="vertical-align: top;">
										<fieldset class=" ui-widget-content ui-corner-all" style="margin-left: 5px;">
											<legend>
												<label><b>Adjustments</b></label>
											</legend>
											<table>
												<tbody>
													<tr>
														<td id="employeeCommissionLabel1"></td>
														<td> 
															<input type="text" id="txt1InputBx" name="txt1InputBx" value="0.00" style="width:100px;">
														</td>
														<td id="employeeCommissionLabel2"></td>
														<td> 
															<input type="text" id="txt2InputBx" name="txt2InputBx" value="0.00" style="width:100px;">
														</td>
													</tr>
													<tr>
														<td id="employeeCommissionLabel3"></td>
														<td> 
															<input type="text" id="txt3InputBx" name="txt3InputBx" value="0.00" style="width:100px;">
														</td>
														<td id="employeeCommissionLabel4"></td>
														<td> 
															<input type="text" id="txt4InputBx" name="txt4InputBx" value="0.00" style="width:100px;">
														</td>
													</tr>
												</tbody>
											</table>
										</fieldset>
									</td></tr>
								</tbody></table>
							</td>
							<td>
								<table style=" margin-top: -30px;">
									<tbody><tr><td style="vertical-align: top;">
										<fieldset class=" ui-widget-content ui-corner-all" style="margin-left: 5px;">
										<legend><label><b>Payment</b></label></legend>
										<table><tbody>
											<tr>
												<td>
													<input type="text" id="txt5InputBx" name="txt5InputBx" value="0.00" style="width:100px;">
												</td>
											</tr>
										</tbody></table>
										</fieldset>
									</td></tr></tbody>
								</table>
							</td></tr>
						</tbody></table>
						<table style="padding-top: 8px;">
							<tbody>
								<tr>
									<td>
										<table style="width: 535px;">
											<tbody>
												<tr>
													<td style="vertical-align: top;">
														<fieldset class=" ui-widget-content ui-corner-all" style="margin-left: 5px;">
															<legend>
																<label><b>Comments [max 3 lines will print]</b></label>
															</legend>
															<table>
																<tbody>
																	<tr>
																		<textarea id="commentTxt" style="width:470px; height:50px;"></textarea>
																	</tr>
																</tbody>
															</table>
														</fieldset>
													</td>
												</tr>
											</tbody>
										</table>
									</td>
								</tr>
							</tbody>
						</table>
						<table align="center">
							<tbody>
								<tr>
									<td colspan="4">
										<input type="button" id="okButton" value="Ok" style="width:100px;" class="savehoverbutton turbo-blue" onclick="saveCommissionAdjustment();">
										<input type="button" id="cancelButton" value="Cancel" style="width:100px;" class="savehoverbutton turbo-blue" onclick="jQuery( '#employeeCommissionsPopupDialog' ).dialog('close');">
									</td>
								</tr>
							</tbody>
						</table>
						<input type="hidden" id="userLoginID"/>
						<input type="hidden" id="ecStatementID"/>
					</div>
					<div id="commissionsGridPager"></div>
				</td>
			</tr>
		</table>
		<table height="30px;" >
			<tr>
				<td style="position:absolute; left:100px;">
					<input id="contactEmailID" type="image" src="./../resources/images/printerz.png" title="View Commisions Cover sheet" onclick="generateEmployeeCommissionsSheet()"/>
				</td>
			</tr>
		</table>
		<div style="padding-top: 25px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>			
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/commisions.js"></script>
</body>

<div id="NewPeriodDialog" style="display: none;">
	<form id="NewPeriodForm">
		<table>
			<tr><td align="center" >Enter the new ending date for commissions. All commissions up
			to and including this date will be calculated</td></tr>
			<tr><td align="center" ><input type="text" name="newperioddate" id="newperioddate"/></td></tr>
			<tr><td align="center" ><div id='newDateErrorMsg' style="display: none;" ></div></td></tr>
		</table>
	</form>
</div>

<div id="ReCalculateDialog" style="display: none;">
	<form id="ReCalculateForm">
		<table>
			<tr><td align="left" >Commissions will be recalculated. Do you want to leave the
			payments as they are?
			<br>
			<br>
			Click YES if you want the payments to be left alone.
			<br><br>
			Click NO if you want the payments to be recalculated
			(any changes you type to the payment will be lost).
			<br><br>
			Click CANCEL and commissions will not be
			recalculated. Everything will be left alone.
			</td></tr>
		</table>
	</form>
</div>

<div id="ReversePeriodDialog" style="display: none;">
	<form id="ReversePeriodForm">
		<table>
			<tr><td align="left" >You have indicated that you wish to reverse the period for
			commissions. This will revert back to the previous period
			and all information in the current period will be lost.
			</td></tr>
			<br><br>
			<tr><td>If you understand this and are ready to proceed enter
			AGREE belowto begin the update.
			</td></tr>
			<tr><td><input type="text" id="rpAcceptTextID" name="rpAcceptText" style="width: 454px;"></td></tr>
			<tr><td align="center" ><div id='reversePeriodErrorMsg' style="display: none;" ></div></td></tr>
			<tr>		
		</table>
	</form>
</div>

<div id="LoadingDialog">
	<span class="loading style-2"> </span>
</div>
</html>