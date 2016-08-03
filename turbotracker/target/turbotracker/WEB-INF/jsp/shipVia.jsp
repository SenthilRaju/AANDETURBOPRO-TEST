<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>ShipVia</title>
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
<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
				<table style="width:979px;margin:25px auto;">
				<tr>
					<td style="padding-right: 20px;">
						<table id="shipViaListGrid"></table>
					</td>
					<td>
						<div id="chartsDetailsTab" >
							<div class="charts_tabs_main" style="padding: 0px;width:580px;margin:0 auto; background-color: #FAFAFA;height: 670px;">
								<ul>
									<li id=""><a  href="#chartsDetailsDiv">Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
									<label id="chartsName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
								<div id="chartsDetailsDiv" style="padding: 0px;">
									<form id="chartsDetailsFromID">
										
								   	</form>
									<table>
									
										<tr>
										   <td style="position: absolute;top: 60px;">
										      <form id =shipViaFormID>
											      <fieldset class= " ui-widget-content ui-corner-all" >
														    <table>
																<tr>
															    	<td><label>Description: </label></td>
																	<td><input type="text" name="Description_ID" id="DescriptionID" style="width:372px;height: 24px;"  class="validate[required] validate[maxSize[12]" value=""></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																     <td><label>Tracking URL: </label></td>
																	 <td><textarea name="trackingUrl" id="trackingUrlID" rows="2" cols="50"></textarea></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
																   	 <td><label>Tracking Prefix:</label></td>
																	 <td><textarea name="trackingPrefix" id="trackingPrefixID" rows="2" cols="50"></textarea></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
																<tr>
															 		 <td><label>Tracking Suffix:</label></td>
																	 <td><textarea name="trackingSuffix" id="trackingSuffixID" rows="2" cols="50"></textarea></td>
																	 <td style="display:none"><input type="text" id="shipViaID" name="shipVia_ID"></td>
																	 <td style="display:none"><input type="text" id="inactive" name="Inactive"></td>
																</tr>
																<tr style="height: 15px"><td></td></tr>
														 </table>
														</fieldset>
														</form>
												<table style="position:relative;top:25px;">
												<tr>
												<td><label id="messsagetouser"></label></td>
												</tr>
											 		<tr>
											 			<td align="left">
													    	<input type="button" value="Add" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="addShipViaDetails()">
										     		    </td>
											   			<td style="align:right;padding-left: 10x; width: 770px;" align="right">
												     		<input type="button" id="saveshipviadetails" name="saveshipviadetails" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveshipviaDetails()">
													    	<input type="button" id="deleteshipviadetails" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteshipviaDetails()">
										     		    </td>
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
	</body>

<script type="text/javascript" src="./../resources/scripts/turbo_scripts/shipVia.js"></script> 
</html>