<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - TaxTerrotries</title>
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
		<form id="TaxPageForm">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;padding-bottom: 30px">
				<tr>
					<td style="padding-right: 20px;">
						<table>
							<tr>
								<td style="padding-left: 280px;">
									<table><tr><td><input type="button" value="  Add" class="add" id="addChartlist" onclick="openAddtaxTerritoryDialog()"></td></tr></table>
						    	</td>
						    </tr>
						    <tr>
						    	<td>
									<table id="taxTerritoryGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					<td>
						<div id="chartsDetailsTab">
							<div class="charts_tabs_main" style="padding: 0px;width:800px;margin:0 auto; background-color: #FAFAFA;height: 565px;">
								<ul>
									<li id=""><a href="#taxTerritoryDetailsDiv">Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;"><label id="taxTerritoryName" style="color: white;vertical-align: middle;"></label></li>
								</ul>
								<div id="taxTerritoryDetailsDiv" style="padding: 0px;">
									<form id="taxTerritoryFromID">
										<table style="padding-top: 8px;">
											<tr>
												<td>
													<table>	
														<tr>
															<td style="vertical-align: top;">
																<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
																	<legend><label><b>Tax Territory</b></label></legend>
																	<table>
																		<tr>
																			<td style="display: none;"><label>CoTaxTerritoryID: </label><input type="text" name="coTaxTerritoryName" id="coTaxTerritoryID" style="width:150px"></td>
																			<td><label>State: </label></td>
																			<td><input type="text" name="stateName" id="stateID" style="width:50px" maxlength="2" value=""></td>
																			<td><label style="vertical-align: middle;">code: </label></td>
																			<td><input type="text" name="CountyCodeName" id="CountyCodeId" style="width:50px" class="number" maxlength="6" value=""></td>
																		</tr>
																		<tr>
																			<td><label>Description: </label></td>
																			<td colspan="2"><input type="text" name="CountyDecription" id="CountyDecriptionId" style="width:200px"></td>
																			<td><label>Prior Rate: </label></td>
																		</tr>
																		<tr>
																			<td Style="width:100px" colspan="2"><label>Total Tax Rate for this Territory </label></td>
																			<td><input type="text" name="taxRate" id="taxRateID" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRTaxRate" id="GRTaxRateId" style="width:50px"  value=""></td>
																		</tr>
																		<tr>
																			<td Style="width:100px"><label>Marta </label></td>
																			<td><input type="text" name="DistDesc1" id="DistDescId1" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution1" id="DistributionId1" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution1" id="GRDistributionId1" style="width:50px"  value=""></td>
																		</tr>
																			<tr>
																			<td Style="width:100px">Local</td>
																			<td><input type="text" name="DistDesc2" id="DistDescId2" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution2" id="DistributionId2" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution2" id="GRDistributionId2" style="width:50px"  value=""></td>
																		</tr>
																			<tr>
																			<td Style="width:100px"><label>Towns</label></td>
																			<td><input type="text" name="DistDesc3" id="DistDescId3" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution3" id="DistributionId3" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution3" id="GRDistributionId3" style="width:50px"  value=""></td>
																		</tr>
																			<tr>
																			<td Style="width:100px">Special</td>
																			<td><input type="text" name="DistDesc4" id="DistDescId4" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution4" id="DistributionId4" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution4" id="GRDistributionId4" style="width:50px"  value=""></td>
																		</tr>
																			<tr>
																			<td Style="width:100px">Edu-cation</td>
																			<td><input type="text" name="DistDesc5" id="DistDescId5" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution5" id="DistributionId5" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution5" id="GRDistributionId5" style="width:50px"  value=""></td>
																		</tr>
																			<tr>
																			<td Style="width:100px"><label>Home-Stead </label></td>
																			<td><input type="text" name="DistDesc6" id="DistDescId6" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution6" id="DistributionId6" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution6" id="GRDistributionId6" style="width:50px"  value=""></td>
																		</tr>
																		<tr>
																			<td Style="width:100px"><label>Others </label>1</td>
																			<td><input type="text" name="DistDesc7" id="DistDescId7" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution7" id="DistributionId7" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution7" id="GRDistributionId7" style="width:50px"  value=""></td>
																		</tr>
																		<tr>
																			<td Style="width:100px"><label>Others 2</label></td>
																			<td><input type="text" name="DistDesc8" id="DistDescId8" style="width:150px"  value=""></td>
																			<td><input type="text" name="Distribution8" id="DistributionId8" style="width:50px"  value=""></td>
																			<td><input type="text" name="GRDistribution8" id="GRDistributionId8" style="width:50px"  value=""></td>
																		</tr>
																		<tr>
																			<td Style="width:100px"><label>Tax Freight?</label></td>
																			<td colspan="3">
																			<input type="hidden" value="">
																					<span><input type="radio" id="chk_TaxFreightID" name="chk_TaxFreightName" style="vertical-align: top;"   value="yes"></span>&nbsp;<span style="color:#00377A;">Yes</span>
																					<span style="margin-left:75px;"><input type="radio" id="chk_TaxFreightID" name="chk_TaxFreightName" value="no" style="vertical-align: top;" checked="checked"></span>&nbsp;<span style="color:#00377A;">No</span>
																			</td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="vertical-align: top;">
															<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
															<legend><label><b>Single Item Rates</b></label></legend>
																<table id="lineItemRates">
																	<tr>
																		<td><input type="text" name="rateId2" id="rateId2" style="width:120px"  value=""></td>
																		<td><input type="text" name="from2" id="fromId2" style="width:120px"  value=""></td>
																	</tr>
																	<tr>
																		<td><input type="text" name="rateId3" id="rateId3" style="width:120px"  value=""></td>
																		<td><input type="text" name="from3" id="from3" style="width:120px"  value=""></td>
																	</tr>
																	<tr>
																		<td><input type="text" name="rateId4" id="rateId4" style="width:120px"  value=""></td>
																		<td><input type="text" name="from4" id="from4" style="width:120px"  value=""></td>
																	</tr>
																	<tr>
																		<td><input type="text" name="rateId5" id="rateId5" style="width:120px"  value=""></td>
																		<td><input type="text" name="from5" id="from5" style="width:120px"  value=""></td>
																	</tr>
																	<tr>
																		<td><input type="text" name="rateId6" id="rateId6" style="width:120px"  value=""></td>
																		<td><input type="text" name="from6" id="from6" style="width:120px"  value=""></td>
																	</tr>
																</table>
															</fieldset>
														<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
															<legend><label><b>SurTax</b></label></legend>
																<table id="lineItemRates">
																	<tr>
																		<td Style="width:100px"><label>Cap:</label></td>
																		<td><input type="text" name="capName" id="capID" style="width:120px"  value=""></td>
																	</tr>
																	<tr>
																		<td Style="width:100px"><label>Rate:</label></td>
																		<td><input type="text" name="rateName" id="rateID" style="width:120px"  value=""></td>
																	</tr>
																</table>
															</fieldset>
														</td>				
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</form>
								<table>
									<tr>
								<td Style="width:100px; display: none;" ><label>InActive :</label></td>
								<td><input type="checkbox" name="inActiveChkbx" id="inActiveChkbx"  style="vertical-align: middle; display: none;"></td>
										<td style="align:right;padding-left: 10x; width: 770px;" align="right">
											<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveTaxTerritory()">
											<input type="button" id="deleteChartOfAccountID" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteTaxTerritoryDetails()">
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
		</form>
	</div>	
	<div id="addNewTaxTerritoryDialog">
		<form action="" id="addNewTaxTerritoryFormID">
			<div id="">
				<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
					<legend><label><b>Tax Territory</b></label></legend>
					<table>
						<tr>
							<td><label>State: </label></td>
							<td><input type="text" name="stateName" id="stateID" style="width:50px"  value="" maxlength="2"></td>
							<td><label style="vertical-align: middle;">code: </label></td>
							<td><input type="text" name="stateCodeName" id="stateCodeID" style="width:50px" class="number" value="" maxlength="6"></td>
						</tr>
						<tr>
							<td><label>Description: </label></td>
							<td colspan="4"><input type="text" name="decriptionName" id="decriptionID" style="width:200px"></td>
						</tr>
					</table>
				</fieldset>
				<table align="right" style="width:745px">
						<tr>
							<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveNewTaxTerritory()" style=" width:120px;">
							<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddTaxTerritory()"></td>
						</tr>
					</table>
				</div>
			</form>
			</div>
			<div>
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/taxTerritories.js"></script>
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->

<script>
$(function(){
	
	$('.number').keypress(function (event) {
	    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
	        event.preventDefault();
	    }
	    var text = $(this).val();
	    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
	        event.preventDefault();
	    }
	});
	
})
</script>
	</body>
</html>