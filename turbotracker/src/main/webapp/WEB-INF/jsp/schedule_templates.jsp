<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Schedule Templates</title>
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
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td style="padding-right: 20px;">
				<table><tr >
				<td ><input type="button" class="savehoverbutton turbo-blue"
					id="add" name="add" value="Add"  onclick="addTemplate()" style="float: right;"/></td>
			</tr></table>
					<table id="scheduleGrid"></table>
					<div id="schedulePager"></div>
				</td>
				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 870px; margin: 0 auto; background-color: #FAFAFA; height: 530px;">
							<ul>
								<li id=""><a href="#scheduleDetailsDiv">Details</a></li>
							</ul>
							<div id="scheduleDetailsDiv" style="padding: 0px;">
								<form id=scheduleDetailsFromID>
									<table style="padding-top: 8px;">
										<tr>
											<td>
												<table>
													<tr>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Schedule Template</b></label>
																</legend>
																<table>
																	<tr>
																		<td>Code :</td>
																		<td><input type="text" id="schedule"
																			name="schedule" value=""/></td>
																	</tr>
																</table>
															</fieldset>
														</td>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Default Production Description</b></label>
																</legend>
																<table>
																	<tr>
																		<td colspan="2"><input type="text"
																			id="description" name="description" /></td>
																	</tr>
																</table>
															</fieldset>
														</td>
														<td style="vertical-align: top;">
															<fieldset class=" ui-widget-content ui-corner-all"
																style="margin-left: 5px;">
																<legend>
																	<label><b>Primary Manufacturer</b></label>
																</legend>
																<table>
																	<tr>

																		<td colspan="2"><input type="text" style="width: 220px"
																			id="manufacturer" name="manufacturer" placeholder="Minimum 2 characters required" /></td>
																			<td><img src="./../resources/scripts/jquery-autocomplete/search.png" alt="search"></td>

																		<td><input type="button"
																			class="savehoverbutton turbo-blue" id="searchSchedule"
																			name="searchSchedule" value="Search" onclick="search()" /></td>
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
										<label><b>Columns</b></label>
									</legend>
									<table>
										<tr>
											<td>

												<div id="scheduleDetails" style="padding-left: -2px;">
													<table style="padding-left: 0px" id="scheduleDetailsGrid"
														class="scroll"></table>
													<div id="scheduleDetailsGridpager" class="scroll"
														style="text-align: right;"></div>
												</div>

											</td>
										</tr>
									</table>
									<table style="float: right;">
										<tr>
											<td><input type="button"
												style="float: right; margin-right: 10px;"
												class="savehoverbutton turbo-blue" value="Accessories"
												onclick="openAccessories()"></td>
											<!-- <td><input type="checkbox"
												style="float: right; margin-right: 10px;"></td> -->
											<!-- <td><label style="float: right; margin-right: 10px;">Print
													Landscape</label></td> -->
											<td><input type="button"
												style="float: right; margin-right: 10px;"
												class="savehoverbutton turbo-blue" value="Copy Template"
												onclick="copyTemplate()"></td>
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
<div id="addNewTemplateDialog" >
			<form action="" id="addNewTemplateFormID">
				<div id="">
					<table>
						<tr>
							<td>
								<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
								<legend><label><b>Add New Template</b></label></legend>
									<table>
										<tr>
											<td>Code :</td><td><input type="text" id="aCode"  name="aCode"/></td>
										</tr>
										<tr>
											<td> Description : </td><td><input type="text" id="aDescription" name="aDescription"/></td>
										</tr>
										<tr>
											<td> Manufacturer : </td><td><input type="text" id="aManufacturer"  name="aManufacturer" placeholder="Minimum 2 characters required"/>
											</td><td><img src="./../resources/scripts/jquery-autocomplete/search.png" alt="search" ></td>						
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
					<br>
					<hr>
					<table align="right">
						<tr>
							<td align="right" style="padding-right:25px;">
							<input type="button" id="saveButton" name="saveButtonName" value="Save & Close" 
							class="savehoverbutton turbo-blue" onclick="addDescription()" style=" width:120px;">
							<input type="button" id="cancelButton" name="cancelButtonname" value="Cancel" 
							class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="closeTemplate()"></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		
		<div id="accessoriesDialog" >
			<form action="" id="accessoriesFormID">
				<div id="">
					<table id="accessoriesGrid"></table>
					<div id="accessoriesPager"></div>
				</div>
			</form>
		</div>
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/scheduletemplates.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
</body>
</html>