<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Model Maintenance</title>
<style type="text/css">
th.ui-th-column div {
    word-wrap: break-word; /* IE 5.5+ and CSS3 */
    white-space: pre-wrap; /* CSS3 */
    white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
    white-space: -pre-wrap; /* Opera 4-6 */
    white-space: -o-pre-wrap; /* Opera 7 */
    overflow: hidden;
    height: auto !important;
    vertical-align: middle;
}
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
		<table style="width: 1140px;height:auto; margin: 0 auto;">
			<tr>
				
				<td>
				<input type="hidden" name="joSchedTempHeaderId" id="joSchedTempHeaderId" value="${joSchedTempHeaderId }"> 
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 1140px; margin: 0 auto; background-color: #FAFAFA; height: 730px;">
							<ul>
								<li id=""><a href="#modelDetailsDiv">Details</a></li>
							</ul>
							<div id="modelDetailsDiv" style="padding: 0px;">
								<form id=modelDetailsFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<table>
																	<tr>
																		<td>Products Category :</td>
																		<td><input type="text" id="productCategory"
																			name="productCategory" value=""  placeholder="Minimum 2 characters required"/></td>
																	</tr>
																</table>
															</fieldset>
														</td>
													</tr>
												</table>
											</td>
									</table>
								</form>
								<fieldset class=" ui-widget-content ui-corner-all"
									style="margin-left: 5px;">
									<legend>
										<label><b>Models</b></label>
									</legend>
									<table>
										<tr>
											<td>

												<div id="modelDetails" style="padding-left: -2px;">
													<table style="padding-left: 0px" id="modelDetailsGrid"
														class="scroll"></table>
													<div id="modelDetailsGridpager" class="scroll"
														style="text-align: right;"></div>
												</div>

											</td>
										</tr>
									</table>
									<table style="float: right;">
										<tr>
										<td><input type="button"
												style="float: right; margin-right: 10px;"
												class="savehoverbutton turbo-blue" value="Save"
												onclick=" saveproductCategory()"></td>
											<td><input type="button"
												style="float: right; margin-right: 10px;"
												class="savehoverbutton turbo-blue" value="Test Product Link"
												onclick="testLink()"></td>
											<!-- <td><input type="checkbox"
												style="float: right; margin-right: 10px;"></td> -->
											<!-- <td><label style="float: right; margin-right: 10px;">Print
													Landscape</label></td> -->
											<td><input type="button"
												style="float: right; margin-right: 10px;"
												class="savehoverbutton turbo-blue" value="Manufacturer's Link"
												onclick="testManufacurersLink()"></td>
										</tr>
										<!-- <input type="button" class="savehoverbutton turbo-blue" value="Get Grid" onclick="GetGridData()"> -->
									</table>
								</fieldset>
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
						<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/modelmaintenance.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>