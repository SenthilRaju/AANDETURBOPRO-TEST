<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>QuickBook</title>
<style type="text/css">
div.growlUI {
	background: url(check48.png) no-repeat 10px 10px
}

div.growlUI h1,div.growlUI h2 {
	color: white;
	padding: 5px 5px 5px 75px;
	text-align: left
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
<body style="background-color: #FAFAFA">
	<div>
		<div>
			<jsp:include page="./headermenu.jsp"></jsp:include>
		</div>
		<table style="width: 979px; margin: 25px auto;">
			<tr>

				<td>
					<div id="chartsDetailsTab">
						<div class="charts_tabs_main"
							style="padding: 0px; width: 350px; margin: 0 auto 74px;; background-color: #FAFAFA; height: 345px;">
							<ul>
								<li id=""><a href="#chartsDetailsDiv">QuickBook
										Settings</a></li>
								<li style="float: right; padding-right: 10px; padding-top: 3px;">
									<label id="chartsName"
									style="color: white; vertical-align: middle;"></label>
								</li>
							</ul>
							<div id="chartsDetailsDiv" style="padding: 0px;">

								<table>
									<tr>
										<td style="position: absolute; top: 60px;">
											<form id=shipViaFormID>
													<table>
														<tr>
															<td><label>UserName:</label></td>
															<td><input type="text" name="qbuserName"
																id="qbuserName" style="width: 200px;"
																class="validate[required] validate[maxSize[100]"
																value="${requestScope.theQbpo.qbUserName }">
																<div id="divqbuserName" style="color: red">Please
																	Enter a Valid User Name</div></td>
														</tr>
														<tr style="height: 15px">
															<td></td>
														</tr>
														<tr>
															<td><label>Password: </label></td>
															<td><input type="text" name="qbpassword"
																id="qbpassword" style="width: 200px;"
																class="validate[required] validate[maxSize[100]"
																value="${requestScope.theQbpo.qbPassword }"></input>
																<div id="divqbpassword" style="color: red">Please
																	Enter a Valid Password</div></td>
														</tr>
														<tr style="height: 15px">
															<td></td>
														</tr>
														<tr>
															<td><label>API Key:</label></td>
															<td><input type="text" name="key" id="key"
																style="width: 200px;"
																class="validate[required] validate[maxSize[100]"
																value="${requestScope.theQbpo.qbUserKey }"></input>
																<div id="divkey" style="color: red">Please Enter a
																	Valid Key</div></td>
														</tr>
														<tr style="height: 15px">
															<td></td>
														</tr>
														<tr>
															<td><label>Host:</label></td>
															<td><input type="text" name="host" id="host"
																style="width: 200px;"
																class="validate[required] validate[maxSize[100]"
																value="${requestScope.theQbpo.qbHost }"></input>
																<div id="divhost" style="color: red">Please Enter
																	a Valid Host</div></td>
														</tr>
														<tr style="height: 20px"></tr>
														<tr>
															<td><label>Enable</label></td>
															<td><c:if test="${requestScope.theQbpo.qbEnabled eq '1' }">
																	<input type="checkbox" name="enable" id="enable" 
																			class="validate[required] validate[maxSize[1]" checked="checked" />
																</c:if>
																<c:if test="${requestScope.theQbpo.qbEnabled eq '0' || requestScope.theQbpo.qbEnabled eq null}">
																	<input type="checkbox" name="enable" id="enable" 
																			class="validate[required] validate[maxSize[1]" />
																</c:if> </td>
														</tr>
													</table>
											</form>
											<table style="position: relative; top: 25px;">
												<tr>

													<td style="align: right; padding-left: 10x; width: 770px;"
														align="right"><input type="button"
														id="saveqbsettings" name="saveqbsettings" value="Save"
														class="savehoverbutton turbo-blue" style="width: 80px;"
														onclick="validateQB();"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
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
</body>
<div class="growlUI" style="display: none">
	<h3>Growl Notification</h3>
</div>
<script type="text/javascript"
	src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript"
	src="./../resources/scripts/turbo_scripts/qbsettings.js"></script>
</html>