<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
	input {height: 25px !important;padding-left: 5px; !important}
	labe{ font-size: .8em !important;}
	.fontclass{font-size: .8em !important; font-family: Verdana,Arial,sans-serif;}
	.dropdownfont{font-weight: normal;font-size: medium;font-variant: normal;font-style: normal;}
	.ui-jqgrid .ui-jqgrid-htable th div { font-size: 0.8em; !important}
	.ui-jqgrid tr.jqgrow td {font-size: .8em; !important}
</style>
<link type="text/css" href="./../resources/web-plugins/jquery.jqGrid-4.4.0/css/ui.jqgrid.css" media="screen" rel="stylesheet"/>
<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:1000px;margin:0 auto;padding-bottom: 30px; padding-top: 0px;height:620px">
				<tr>
					<td style="padding-right: 20px;">
						<table>
						    <tr>
						    	<td>
									<table id="wareHouseGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					<td style="vertical-align: top;width: 200px;">
						<div id="warehouse" style="margin-bottom: 15px;margin-top:75px;">
						<form id="warehouseDetails">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label class="fontclass"><b>Details</b></label></legend>
						<table>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>WareHouse</b></label></legend>
									<table>
										<tr>
											<td> <input type="text" style="display: none;"  id="warehouseID" name="warehouseID"/></td>
											<td><label class="fontclass">Description: </label></td>
											<td><input type="text" id="description" name="description"  style="width: 168px;"></td>
											<td><label class="fontclass">In Active: </label><input type="checkbox" value='1' id="warehouseInactive" name="warehouseInactive"></td>
										</tr>
									</table>
								</fieldset>
								</td>
								<td>
								<fieldset class= " ui-widget-content ui-corner-all">
								<legend><label class="fontclass"><b>GL Accounts</b></label></legend>
								<table>
									<tr>
										<td><label class="fontclass">Asset:</label></td>
										<td>
											<select style="width:250px;" id="asset" name="assetName">
														<option value="0"> - Select - </option>
															<c:forEach var="accounts" items="${requestScope.accounts}">
																<option value="${accounts.coAccountId}">
																	<c:out value="${accounts.description}" ></c:out>
																</option>
														</c:forEach>
												</select>
											</td>
									</tr>
								</table>
								</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Company</b></label></legend>
									<table>
										<tr>
											<td><input type="text" id="companyName" name="companyName" style="width:386px;"></td>
										</tr>
									</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Pickup Tax Territory</b></label></legend>
									<table>
										<tr>
											<td>
											<select style="width:250px;" id="taxTerritory" name="taxTerritoryName">
														<option value="0"> - Select - </option>
															<c:forEach var="taxTerritories" items="${requestScope.taxTerritories}">
																<option value="${taxTerritories.coTaxTerritoryId}">
																	<c:out value="${taxTerritories.county}" ></c:out>
																</option>
														</c:forEach>
												</select>
											</td>
											<td>
												<label id="taxRate"></label>%
											</td>
										</tr>
										<tr><td><br></td></tr>
										<tr>
											<td><label class="fontclass">Email( Pickup Tickets): </label><input style="width:205px;" type="text" id="emailPickUp" name="emailPickUpName"></td>
										</tr>
									</table>
									</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Address</b></label></legend>
									<table>
										<tr>
											<td><input type="text" id="Address1" name="Address1" style="width: 100%;"></td>
										</tr>
										<tr>
											<td><input type="text" id="Address2" name="Address2" style="width: 100%;"></td>
										</tr>
										<tr>
											<td>
												<input type="text" id="city" name="city" style="width: 60%;" placeholder="Minimum 2 character for city & state">
												<input type="text" id="state" name="state" style="width: 15%;" placeholder="Minimum 2 character">
												<input type="text" id="zip" name="zip" style="width: 20%;">
											</td>
										</tr>
										<tr>
											<td><label class="fontclass">Additional line for print ticket</label></td>
										</tr>
										<tr>
											<td>
												<input type="text" id="additionaladdressLine" name="additionaladdressLine">
											</td>
										</tr>
									</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>WareHouse Printer</b></label></legend>
									<table>
										<tr>
												<td><input type="radio" name="checkType" value="dotMatrix" checked="checked"><label class="fontclass">Dot matrix</label></td>
												<td><input type="radio" name="checkType" value="laser" checked="checked"><label class="fontclass">Laser</label></td>
										</tr>
										<tr>
											<td>
												<input type="button" value="Assign a Printer" class="turbo-blue savehoverbutton" onclick="()" style="margin-left: 10px; font-size: 15px;" />
											</td>
										</tr>
									</table>
									</fieldset>
								</td>
							</tr>
						</table>
						<input type="button" value="Delete" class="savehoverbutton turbo-blue" onclick="deleteWareHouses()" style="float: right;width:100px;margin-left: 10px;" />
						<input type="button"  value="Save" class="savehoverbutton turbo-blue" onclick="saveWareHouses()" style="float: right;width:100px;" />
						<input type="button" value="  Add" class="add" id="warehouseDlg" onclick="OpenNewWarehouseDialog()" style="float: right;width:100px;margin-right: 10px;">
						</fieldset>
						</form>
						</div>
					</td>
				</tr>
			</table> 
		</div>
		<div id="addNewWarehouseDlg" style="width:736px;">
			<form id="addNewWarehouseForm">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Details</b></label></legend>
						<table>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>WareHouse</b></label></legend>
									<table>
										<tr>
											<td><label class="fontclass">Description: </label></td>
											<td><input type="text" id="adddescription" name="description" style="width: 168px;"> </td>
											<td><label class="fontclass">In Active: </label><input type="checkbox" value='1' id="warehouseInactive" name="warehouseInactive"></td>
										</tr>
									</table>
								</fieldset>
								</td>
								<td>
								<fieldset class= " ui-widget-content ui-corner-all">
								<legend><label class="fontclass"><b>GL Accounts</b></label></legend>
								<table>
									<tr>
										<td><label class="fontclass">Asset:</label></td>
									</tr>
									<tr>
										<td>
											<select style="width:250px;" id="addasset" name="assetName" class="dropdownfont fontclass" >
														<option value="0"> - Select - </option>
															<c:forEach var="accounts" items="${requestScope.accounts}">
																<option value="${accounts.coAccountId}">
																	<c:out value="${accounts.description}" ></c:out>
																</option>
														</c:forEach>
												</select>
											</td>
									</tr>
								</table>
								</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Company</b></label></legend>
									<table>
										<tr>
											<td><input type="text" id="companyName" name="companyName" style="width:386px;"></td>
										</tr>
									</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Pickup Tax Territory</b></label></legend>
									<table>
										<tr>
											<td>
											<select style="width:250px;" id="addtaxTerritory" name="taxTerritoryName" class="dropdownfont fontclass">
														<option value="0"> - Select - </option>
															<c:forEach var="taxTerritories" items="${requestScope.taxTerritories}">
																<option value="${taxTerritories.coTaxTerritoryId}">
																	<c:out value="${taxTerritories.county}" ></c:out>
																</option>
														</c:forEach>
												</select>
											</td>
											<td>
												<label id="taxRate"></label>
											</td>
										</tr>
										<tr><td><br></td></tr>
										<tr>
											<td><label class="fontclass">Email( Pickup Tickets): </label><input style="width:197px;" type="text" id="emailPickUp" name="emailPickUpName"></td>
										</tr>
									</table>
									</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Address</b></label></legend>
									<table>
										<tr>
											<td><input type="text" id="Address1" name="Address1" style="width: 100%;"></td>
										</tr>
										<tr>
											<td><input type="text" id="Address2" name="Address2" style="width: 100%;"></td>
										</tr>
										<tr>
											<td>
												<input type="text" id="addcity" name="city" style="width: 60%;"  placeholder="Minimum 2 character for city & state">
												<input type="text" id="addstate" name="state" style="width: 15%;"  placeholder="Minimum 2 character.">
												<input type="text" id="addzip" name="zip" style="width: 20%;">
											</td>
										</tr>
										<tr>
											<td><label class="fontclass">Additional line for print ticket</label></td>
										</tr>
										<tr>
											<td>
												<input type="text" id="additionaladdressLine" name="additionaladdressLine">
											</td>
										</tr>
									</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>WareHouse Printer</b></label></legend>
									<table>
										<tr>
												<td><input type="radio" name="checkType" value="dotMatrix" checked="checked"><label class="fontclass">Dot matrix</label></td>
												<td><input type="radio" name="checkType" value="laser" checked="checked"><label class="fontclass">Laser</label></td>
										</tr>
										<tr>
											<td>
												<input type="button" value="Assign a Printer" class="turbo-blue savehoverbutton" onclick="()" style="margin-left: 10px; font-size: 15px;" />
											</td>
										</tr>
									</table>
									</fieldset>
								</td>
							</tr>
					</table>
					<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="addWareHouses()" style="float: right;" />
				</fieldset>
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/warehouse.js"></script>
