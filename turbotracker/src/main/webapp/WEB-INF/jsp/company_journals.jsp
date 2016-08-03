<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Chart of Accounts</title>
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
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include>
			</div>
			
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td style="padding-right: 20px;">
						<table id="JournalsGrid"></table>
						<div id="JournalsGridPager"></div>
					</td>
					<td>
						<div id="chartsDetailsTab">
							<div class="charts_tabs_main" style="padding: 0px;width:800px;margin:0 auto; background-color: #FAFAFA;height: 515px;">
								<ul>
									<li id=""><a href="#chartsDetailsDiv">Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
										<label id="chartsName" style="color: white;vertical-align: middle;"></label>
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
																<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
																	<legend><label><b>Details Of Journal</b></label></legend>
																	<table>
																	<tr>
																	<td>Memo&nbsp;: </td>
																	<td><input type="text" id="decriptionNameID" name="decriptionNameID" maxlength="30"/></td>
																	</tr>
																	<tr>
																	<td>Date&nbsp;: </td>
																	<td><input type="text" id="DateOfJournal" readonly="readonly"  name="DateOfJournal"/></td>
																	</tr>
																	
																	<tr>
																	<td> Reference&nbsp;: </td>
																	<td><input type="text" id="References"  name="References" readonly="readonly"/> </td>
																	</tr>
																	</table>
																</fieldset>
															</td>
															<td>
															<div id="JournaldetailshistoryGridd" style="padding-left: -2px;">
															<table style="padding-left: 0px" id="JournaldetailshistoryGrid" class="scroll"></table>
															<div id="JournaldetailshistoryGridpager" class="scroll" style="text-align: right;"></div>
															</div>
															</td>
														</tr>
													</table>
													<input type="hidden" style="display: hidden;" id="headerid" name = "headerid" />
													<input type="hidden" style="display: hidden;" id="referenceID" name = "referenceID" />
													<input type="hidden" style="display: hidden;" id="fisicalperiodid" name ="fisicalperiodid"/>
													<input type="hidden" style="display: hidden;" id="coLedgerSourceId" name="coLedgerSourceId"/>
												</td>
												<td width="100%" align="right" style="vertical-align: top;"><input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addNewJournalEntry()"/>&nbsp;</td>
										</table>
									</form>
								</div >
								<table>
									<tr>
										<td>
											<div id="JournalDetails" style="padding-left: -2px;">
											<table style="padding-left: 0px" id="JournalDetailsGrid" class="scroll"></table>
											<div id="JournalDetailsGridpager" class="scroll" style="text-align: right;"></div>
											</div>
										</td>
									</tr>
								</table>
								<!-- --------------------------------- Additional Information -------------------------------- -->
								<div id="accountSettings" >
									<form action="" id="accountsAdditionalInfoFormID">
									<table>
										<tr>
											<td>
											</td>
											<td style="vertical-align: top;">
											</td>
										</tr>
									</table>
									</form>
									<div style="padding-top: 0px;"></div>
									<table style="float: right;">
									<tr>
										<td>
											<div style="display: none" id="journalMessage"></div>
										</td>
									</tr>
									<tr>
									<td><!-- <input type="button" style="float: right;margin-right: 10px;" class="savehoverbutton turbo-blue" value="Add" onclick="openAddNewjournalDialog()"> -->
									<input type="button" id="journalnewsave" style="float: right;display: inline-block;" class="savehoverbutton turbo-blue" value="Save" onclick="saveEditedJournalData('add')">
									<input type="button" id="journaleditbutton" style="float: right;display: none;" class="savehoverbutton turbo-blue" value="Edit" onclick="editbuttontrigger()">
									<input type="button" id="journaleditsave"  style="float: right;display: none;" class="savehoverbutton turbo-blue" value="Save" onclick="beforeenteringtosave('edit')"> 
									<input type="button" id="journaldelete" style="float: right;margin-right: 10px;display: none;" class="savehoverbutton turbo-blue" value="Delete" onclick="beforeenteringtosave('delete')">
									<!-- <input type="button" style="float: right;margin-right: 10px;" class="savehoverbutton turbo-blue" value="Delete" onclick="deleteJournalDialog()"> -->
									</td>
									</tr>
									<!-- <input type="button" class="savehoverbutton turbo-blue" value="Get Grid" onclick="GetGridData()"> -->
									</table>								
								</div>
							</div>
						</div>
						
					</td>
				</tr>
			</table>
		
		</div>
		</form>
		<div style="padding-top: 12px;">
		<table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
				</td>
			</tr>
		</table>
		</div>
			
		<div style="display: none;">
			<input type="text" id="chartsAccID" name="chartsAccName" value="${requestScope.userDetails.chartsperpage}">
		</div>
		<div id="addNewJournalDialog">
			<form action="" id="addNewjournalFormID">
				<div id="">
					<table>
						<tr>
							<td>
								<fieldset  class= " ui-widget-content ui-corner-all" style="margin-left: 5px;">
								<legend><label><b>Add New Journal</b></label></legend>
									<table>
										
										<tr>
											<td> Date :</td><td><input type="text" id="date"  name="DateOfJournal"/></td>
										</tr>
										<tr>
											<td> Memo : </td><td><input type="text" id="Memo" name="decriptionNameID"/></td>
										</tr>
										<tr>
											<td> Reference : </td><td><input type="text" id="Reference"  name="References"/></td>						
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
							<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveNewJournal()" style=" width:120px;">
							<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddJournal()"></td>
						</tr>
					</table>
				</div>
			</form>
		</div>
		<div id="reasondialogbox">
		<table align="center">
		<tr align="center"><td><textarea id="reasontext" name="reasontext" rows="5" cols="25"></textarea></td></tr>
		</table>
		</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/journals.js"></script>
			<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/chartofaccounts.min.js"></script> -->
	<script>
	 getcoFiscalPerliodDate("DateOfJournal");
	 function getcoFiscalPerliodDate(x)
	 {
		 $.ajax({
			 
		 		url: "./banking/getcoFiscalPeriod",
		 		type: "GET",
		 		success: function(data) {
			 		console.log('Fiscal Period SD: '+new Date(data.startDate));
		 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
		 					
		 		}		 
		 });	 
	 }
	</script>		
	</body>
</html>