<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<table>
		<tr><td colspan="2"><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
		<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<div id=mainTab>
	<form action="" id="jobWizardMainForm">
	<table>
	 	<tr>
	 		<td>
	 			<fieldset style="width:480px" class= "custom_fieldset">
	 				<legend class="custom_legend"><label><b>Job Location</b></label></legend>
	 				<table id="addressField">
		 				<tr>
		 					<td style="width: 90px"><label>Address: </label></td>
		 					<td><input type="text" id="locationAddressID1" name="locationAddress1" class="validate[maxSize[100]" style="width: 350px;" value="${requestScope.joMasterDetails.locationAddress1}"></td>
		 				</tr>
		 				<tr>
		 					<td style="width: 90px"><label></label></td>
							<td><input type="text" id="locationAddressID2" name="locationAddress2" class="validate[maxSize[40]" style="width: 350px;" value="${requestScope.joMasterDetails.locationAddress2}"></td>
						</tr>
	 					<tr>
	 						<td  style="width: 90px"><label>City-State: </label></td>
	 						<td><input type="text" id="locationCity" name="locationCity" style="width: 140px;" value="${requestScope.joMasterDetails.locationCity}" placeholder="Minimum 2 characters required">
									<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;<label> - </label>
	 								<input type="text" id="locationState" name="locationState" style="width: 30px; text-transform: uppercase" value="${requestScope.joMasterDetails.locationState}" maxlength="2"  placeholder="1 char. requi.">
	 								<label>Zip: </label><input type="text" id="locationZipID" name="locationZip" style="width: 75px;" value="${requestScope.joMasterDetails.locationZip}">
	 						</td>
	 					</tr>
	 				</table>
				</fieldset>
			</td>
			<td width="50px"></td>
			<td style="vertical-align:top">
			 	<fieldset class= "custom_fieldset" style="width:500px;">
				 	<legend class="custom_legend"><b><label>Bid Date</label></b></legend>
				 	<table>
				 		<tr>
				 			<td style="width:145px"><label>Bid Date:</label></td>
				 			<td><input type="text" class="datepickerCustom" name="bidDate" id="bidDate_Date"></td>
				 		</tr>
						<tr>
							<td style="width:145px"><label>Book Date:</label></td>
							<td><input type="text" class="datepickerCustom" name="bookedDate" id="bookedDate_Date" disabled="disabled"></td>
						</tr>
						<tr>
							<td style="width:145px"><label>Close Date:</label></td>
							<td><input type="text" class="datepickerCustom" name="closedDate" id="closedDate_Date" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr><td style="vertical-align:top;">
		 		<fieldset class= " custom_fieldset" style="width:480px">
				<legend class="custom_legend"><b><label>Employee's Assigned</label></b></legend>
					<table> 
						<tr><td ><label>Salesman:</label></td>
							<td>&nbsp;
									<input  type="text" id="jobMain_salesRepsList" style="width: 290px;" value="${requestScope.AssignedSalesRep}"  placeholder="Minimum 1 characters required"  onkeyup="salesmanAdminValidation(this)"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									<td><input type="hidden" id="salesRepId" name="empSalesMan" value="${requestScope.joMasterDetails.cuAssignmentId0}" style="width: 30px;"></td>
							</tr>
							<tr><td><label>CSR:</label></td>
							<td>&nbsp;
									<input  type="text" id="jobMain_CSRList" style="width: 290px;" value="${requestScope.AssignedCSRs}"  placeholder="Minimum 1 characters required"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									<td><input type="hidden" id="CSRId" value="${requestScope.joMasterDetails.cuAssignmentId1}" name="empCSR" style="width: 30px;">
							</td></tr>
							<tr><td><label>Sales Mgr.:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_SalesMgrList" style="width: 290px;" value="${requestScope.AssignedSalesMGRs}"  placeholder="Minimum 1 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="hidden" id="salesMgrId" name="empSalesMgr" value="${requestScope.joMasterDetails.cuAssignmentId2}" style="width: 30px;">
								</td>
							</tr>
							<tr><td><label>Engineer:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_EngineersList" style="width: 290px;" value="${requestScope.AssignedEngineers}"  placeholder="Minimum 1 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="hidden" id="engineerId" name="empEngineer" value="${requestScope.joMasterDetails.cuAssignmentId3}" style="width: 30px;">
								</td>
							</tr>
							<tr><td><label>Prj. Mgr.:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_PrjMgrList" style="width: 290px;" value="${requestScope.AssignedProjManagers}" placeholder="Minimum 1 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="hidden" id="prjMgrId" name="empPrjMgr" value="${requestScope.joMasterDetails.cuAssignmentId4}" style="width: 30px;">
								</td>
							</tr>
							<tr><td><label>Take off:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_TakeOffList" style="width: 290px;"  value="${requestScope.AssignedTakeOff}"  placeholder="Minimum 1 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="hidden" id="takeOffId" name="empTakeOff" value="${requestScope.joMasterDetails.cuAssignmentId5}" style="width: 30px;">
								</td>
							</tr>
							<tr><td><label>Quote by:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_QuoteByList" style="width: 290px;" value="${requestScope.AssignedQuoteBy}"  placeholder="Minimum 1 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									   <td><input type="hidden" id="quoteId" name="empQuoteBy" value="${requestScope.joMasterDetails.cuAssignmentId6}" style="width: 30px;">
								</td>
							</tr>
						<!-- <tr hidden="hidden" class="createdChangedBy">
								<td><label>Created EHB:</label></td>
								<td>&nbsp;&nbsp;<label id="created"></label> <input type="text" disabled="disabled" style="width:200px" name="createdDate" class="datepicker" id="created">
								</td>
							</tr> 
							<tr hidden="hidden" class="createdChangedBy">
								<td><label>Chg. EHB:</label></td>
								<td>&nbsp;&nbsp;<label id="created"></label> <input type="text" disabled="disabled" style="width:200px"  name="changedDate" class="datepicker" id="changed"></td>
							</tr> -->
							<tr height="20px"></tr>
						</table>
					</fieldset>
				</td>
				<td style="width:25px"></td>
				<td style="vertical-align:top">
				 	<table> 
				 		<tr><td>
				 			<fieldset class= "custom_fieldset" style="width:500px;padding-bottom: 15px;">
							<legend class="custom_legend"><label><b>Design/Construct Team</b></label></legend>
								<table>
								  <tr>
								    <td style="width:145px"><label>Architect:</label></td>
								  	<td><input  type="text" id="jobMain_architectsList" style="width: 250px;" value="${requestScope.AssignedArchitects}"  placeholder="Minimum 2 characters required"></td>
								  	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									<td><input type="hidden" id="architectId" name="teamArchitect" value="${requestScope.joMasterDetails.rxCategory1}" style="width: 30px;"></td>
								  </tr>
								  <tr>
								  	<td style="width:145px"><label class="">Engineer: &nbsp;</label></td>
									<td><input  type="text" id="jobMain_engineersRXList" style="width: 250px;" value="${requestScope.AssignedEngineering}"  placeholder="Minimum 2 characters required"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									<td><input type="hidden" id="engineersRXId" name="teamEngineer" value="${requestScope.joMasterDetails.rxCategory2}" style="width: 30px;"></td>
								  </tr>
								  <tr>
								  	<td style="width: 160px;"><label class="">General Contractor/ Construction Mgr.:</label></td>
									<td><input  type="text" id="jobMain_GCList" style="width: 250px;" value="${requestScope.AssignedGcConstrMgrs}"  placeholder="Minimum 2 characters required"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
									<td><input type="hidden" id="GCId"  name="teamGC" value="${requestScope.joMasterDetails.rxCategory3}" style="width: 30px;"></td></tr>
								</table>
							</fieldset>
						</td></tr>
						<tr><td>
						<br>
							<fieldset class= "custom_fieldset" style="width:500px;padding: 8px 8px 8px;">
							<table>
								<tr>
									<td style="width:145px"><label>Division:</label><span style="color:red;"> *</span></td><td>
										<select style="width:170px" id="jobMain_Divisions" name="coDivision" class="validate[required] select">
											<option value="-1"> - Select - </option>
											<c:forEach var="coDivisionBean" items="${requestScope.divisions}">
												<option value="${coDivisionBean.coDivisionId}">
													<c:out value="${coDivisionBean.description}" ></c:out>
												</option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td style="width:160px"><label>Job Site Tax Territory:</label></td>
									<td><input  type="text" id="jobMain_TaxTerritory" style="width: 200px;" value="${requestScope.taxTerritory}"  placeholder="Minimum 2 characters required" onkeyup="jobSiteAdminValidation(this)"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								  	<td><input type="hidden" id="TaxTerritory" name="taxPersent"  value="${requestScope.joMasterDetails.coTaxTerritoryId}" style="width: 30px;"></td>
									<td><label id = "TaxValue">%</label></td>
								</tr>
								<tr style="">
									<td style="width:150px"><label>Customer PO:</label></td>
									<td><input  type="text" id="jobmain_ponumber" name ="jobmainPONumber" style="width: 200px;" value="${requestScope.joMasterDetails.customerPonumber}" ></td>
									<td><input type="button" value="More" class="savehoverbutton turbo-tan" style="width: 50px;" onclick="addcustomerpo()"></td>
								</tr>
							</table>
							</fieldset>
						</td></tr>
					</table>
				</td>
			</tr>
		</table>
		</form>
		<div class="loadingDiv" id="loadingMainDiv"> </div>
		</div>
	<br>
	<div style="height: 30px;">
		<div id="warning_div" style="COLOR: red;font-size: 15px;FONT-FAMILY: Verdana,Arial,sans-serif;position: absolute;margin-right:492px;width: 455px;right: 157px;bottom: 45px;height: 93px;">
		</div>
	</div>
	<table style="width:1100px;">
		<tr><td><hr style="width:1100px; color:#C2A472;"></td></tr>
		<tr>
			<td align="right"><input type="button"  value="Save" class="savehoverbutton turbo-tan" id="mainsave" onclick="jobMainForm_save()" style="width:80px;">
			<input type="button" class="savehoverbutton turbo-tan" value="Delete" onclick="deleteJob()" style="width :90px;"></td>
		</tr>
	</table>
	<div id=addEngineerDialog>
	<form action="" id="addNewEngineerForm">
		<div id="addressField">
			<table>
				<tr><td>
					<fieldset class= "custom_fieldset" >
						<legend class="custom_legend"><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
									<td style=" width : 50px;">Name:</td>
									<td><input type="text" name="name" id="companyName1" style="width:300px" class="validate[required]"></td>
								</tr>
							</table>
					</fieldset>
					</td>
				</tr>
			</table>
		</div>
		<div id="engineerItem">
			<table>
				<tr><td colspan="2"><hr style="color:#C2A472;"></td></tr>
				<tr>
					<td style="vertical-align:top">
			 			 <fieldset style="width:370px;" class= "custom_fieldset">
							<legend class="custom_legend"><label><b>Address</b></label></legend>
							<table>
								<tr>
									<td style=" width : 50px;">
										<label>Address:</label>
									</td>
									<td>
										<input type="text" style="width: 265px;" id="locationAddressID1" name="locationAddress1" value="">
									</td>
								</tr>
								<tr>
									<td style=" width : 50px;">
										<label></label>
									</td>
									<td>
										<input type="text" style="width: 265px;" id="locationAddressID2" name="locationAddress2" value="">
									</td>
								</tr>
								<tr>
									<td style=" width : 50px;">
										<label>City:</label>
									</td>
									<td>
										<input type="text" style="width: 160px;" id="locationCityID" name="locationCity" value="">
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
										<label>State:</label>
										<input type="text" style="width: 25px;text-transform: uppercase" id="locationStateID" name="locationState" value="" maxlength="2">
									</td>
								</tr>
								<tr>
									<td style=" width : 50px;">
										<label>Zip: </label>
									</td>
									<td>
										<input type="text" style="width: 100px;" id="locationZipID" class="validate[custom[number]]" name="locationZip" value="">
									</td>
								</tr>
							</table>
			 			</fieldset>
					</td>
					<td style="vertical-align:top">
						<fieldset style="width:320px; height: 142px;" class= "custom_fieldset">
							<legend class="custom_legend"><label><b>Phone</b></label></legend>
							<table>
							<tr>
								<td style=" width : 50px;">
									<label>Phone1:</label>
								</td>
								<td>
									<input type="text" id="areaCode" name="contact1" style="width:50px" maxlength="3" value="">
									<input type="text" id="exchangeCode" name="contact1" style="width:50px" maxlength="3" value="">-
									<input type="text" id="subscriberNumber" name="contact1" style="width:80px" value="">
								</td>
							</tr>
							<tr>
								<td style=" width : 50px;">
									<label>Phone2:</label>
								</td>
								<td>
									<input type="text" id="areaCode1" name="contact2" style="width:50px" maxlength="3" value="">
									<input type="text" id="exchangeCode1" name="contact2" style="width:50px" maxlength="3" value="">-
									<input type="text" id="subscriberNumber1" name="contact2" style="width:80px" value="">
								</td>
							</tr>
						<tr>
							<td style=" width : 50px;">
								<label>Fax:</label>
							</td>
							<td>
									<input type="text" id="areaCode2" name="contact3" style="width:50px" maxlength="3" value="">
									<input type="text" id="exchangeCode2" name="contact3" style="width:50px" maxlength="3" value="">-
									<input type="text" id="subscriberNumber2" name="contact3" style="width:80px" value="">
								</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</table>
	</div>
	<br>
	<table align="center" style="width:660px">
		 <tr>
			 <td align="right" style="padding-right:1px;">
					<input type="button" id="button1" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveNewEngineer()" style="FONT-FAMILY: 'Bitstream Charter'; width :134px;">
			</td>
		</tr>
	</table>
	</form>
	</div>
	<div id="addArchitectDialog">
	<form action="" id="addNewArchitectForm">
		<div id="addressField">
			<table>
				<tr><td>
					<fieldset class= " custom_fieldset" style="width:360px">
						<legend class="custom_legend"><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
									<td style=" width : 50px;">Name:</td>
									<td><input type="text" name="name" id="companyName2" style="width:300px" class="validate[required]"></td>
								</tr>
							</table>
					</fieldset>
				</td>
				</tr>
			</table>
		</div>
		<div id="ArchitectItem">
			<table>
			<tr><td colspan="2"><hr></td></tr>
			<tr><td style="vertical-align:top">
			  <fieldset style="width:370px;" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Address</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
						<label>Address:</label>
					</td>
					<td>
						<input type="text" style="width: 265px;" id="locationAddressID1" name="locationAddress1" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
					<label></label>
					</td>
					<td>
						<input type="text" style="width: 265px;" id="locationAddressID2" name="locationAddress2" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>City:</label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="city" name="locationCity" value="">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
							<label>State:</label>
							<input type="text" style="width: 25px;text-transform: uppercase" id="state" name="locationState" value="" maxlength="2">
						</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Zip: </label>
						</td>
						<td>
							<input type="text" style="width: 100px; margin-left: 4px;" id="locationZipID" class="validate[custom[number]]" name="locationZip" value="">
						</td>
				</tr>
				</table>
			 </fieldset>
			</td>
			<td style="vertical-align:top">
				<fieldset style="width:320px; height: 142px;" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Phone</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
					<label>Phone1:</label>
					</td>
					<td>
						<input type="text" id="area_Code" name="contact1" style="width:50px" maxlength="3" value="">
						<input type="text" id="exchange_Code" name="contact1" style="width:50px" maxlength="3" value="">-
						<input type="text" id="subscriber_Number" name="contact1" style="width:80px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
					<label>Phone2:</label>
					</td>
					<td>
						<input type="text" id="area_Code1" name="contact2" style="width:50px" maxlength="3" value="">
						<input type="text" id="exchange_Code1" name="contact2" style="width:50px" maxlength="3" value="">-
						<input type="text" id="subscriber_Number1" name="contact2" style="width:80px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Fax:</label>
						</td>
						<td>
						<input type="text" id="area_Code2" name="contact3" style="width:50px" maxlength="3" value="">
						<input type="text" id="exchange_Code2" name="contact3" style="width:50px" maxlength="3" value="">-
						<input type="text" id="subscriber_Number2" name="contact3" style="width:80px" value="">
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
			</table>
		</div>
		<br>
		<table align="center" style="width:650px">
		 	<tr>
			 	<td></td>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" id="button1" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveNewArchitect()" style="FONT-FAMILY: 'Bitstream Charter'; width :134px;">
				</td>
			</tr>
		</table>
		</form>
	</div>	
	<div id="addContractorDialog">
	<form action="" id="addNewContractorForm">
		<div id="addressField">
			<table>
				<tr><td>
					<fieldset class= "custom_fieldset" style="width:360px">
						<legend class="custom_legend"><label><b>Name</b></label><span style="color:red;"> *</span></legend>
							<table>
								<tr>
								<td style=" width : 50px;">Name:</td>
								<td><input type="text" name="name" id="companyName3" style="width:300px" class="validate[required]"></td>
								</tr>
							</table>
					</fieldset>
				</td>
				</tr>
			</table>
		</div>
		<div id="contractorItem">
		<table>
			<tr><td colspan="2"></td></tr>
			<tr><td style="vertical-align:top">
			  <fieldset style="width:370px;" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Address</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
						<label>Address:</label>
					</td>
					<td>
						<input type="text" style="width: 265px;" id="locationAddressID1" name="locationAddress1" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label></label>
					</td>
					<td>
						<input type="text" style="width: 265px;" id="locationAddressID2" name="locationAddress2" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>City:</label>
					</td>
					<td>
						<input type="text" style="width: 160px;;" id="contractorCity" name="locationCity" value="">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
						<label>State:</label>
						<input type="text" style="width: 25px;text-transform: uppercase" id="contractorState" name="locationState" value="" maxlength="2">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Zip: </label>
					</td>
					<td>
						<input type="text" style="width: 100px;" id="locationZipID" class="validate[custom[number]]" name="locationZip" value="">
					</td>
				</tr>
				</table>
			 </fieldset>
			</td>
			<td style="vertical-align:top">
				<fieldset style="width:320px; height: 142px;" class= "custom_fieldset">
					<legend class="custom_legend"><label><b>Phone</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
						<label>Phone1:</label>
					</td>
					<td>
						<input type="text" id="gcareaCode" name="contact1" style="width:50px" maxlength="3" value="">
						<input type="text" id="gcexchangeCode" name="contact1" style="width:50px" maxlength="3" value="">-
						<input type="text" id="gcsubscriberNumber" name="contact1" style="width:80px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Phone2:</label>
					</td>
					<td>
						<input type="text" id="gcareaCode1" name="contact2" style="width:50px" maxlength="3" value="">
						<input type="text" id="gcexchangeCode1" name="contact2" style="width:50px" maxlength="3" value="">-
						<input type="text" id="gcsubscriberNumber1" name="contact2" style="width:80px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Fax:</label>
					</td>
					<td>
						<input type="text" id="gcareaCode2" name="contact3" style="width:50px" maxlength="3" value="">
						<input type="text" id="gcexchangeCode2" name="contact3" style="width:50px" maxlength="3" value="">-
						<input type="text" id="gcsubscriberNumber2" name="contact3" style="width:80px" value="">
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
		</table>
	</div>
	<br>
		<table align="center" style="width:650px">
		 	<tr>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" id="button1" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveNewContractor()" style="FONT-FAMILY: 'Bitstream Charter'; width :134px;">
				</td>
			</tr>
		</table>
		</form>
	</div>	
	<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="userAdminID" value="${sessionScope.user.systemAdministrator}" >
				</td>
				<td>
					<input type="text" id="joMasterHiddenID" value="${requestScope.joMasterDetails.joMasterId}">
				</td>
				<td>
					<input type="text" id="jobNumberHiddenID" value= "${requestScope.joMasterDetails.jobNumber}">
				</td>
				<td>
					<input type="text" id="customerHiddenID" value="${requestScope.CustomerName}">
				</td>
				<td>
					<input type="text" id="estomattedHiddenID" value="${requestScope.joMasterDetails.estimatedCost}">
					<input type="text" id="jobContractAmount" value="${requestScope.joMasterDetails.contractAmount}">
					<input type="text" id="jobMain_salesRepsListHidden" value="${requestScope.AssignedSalesRep}">
					<input type="text" id="jobMain_TaxTerritoryHidden" value="${requestScope.taxTerritory}">
					<input type="text" id="estomattedHiddenIDHidden" value="${requestScope.joMasterDetails.estimatedCost}">
					<input type="text" id="jobContractAmountHidden" value="${requestScope.joMasterDetails.contractAmount}">
				</td>
			</tr>
		</table>
	</div>
	<div id="openCustomerPODia">
	<form action="" id="openCustomerPOForm">
		<div id="addcustomerpo"></div>
		<table>
		<tr style="height: 10px"><td></td></tr>
		<tr>
			<td><label><b><u>Covered Material</b></u></label></td>
			<td><label><b><u>Po Number</b></u></label></td>
			<td><label><b><u>Po Amount</b></u></label></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material1" style="width: 200px" value="${requestScope.customerPODetails.podesc0}"></td>
			<td><input type="text" id="po_number1" style="width: 110px" value="${requestScope.joMasterDetails.customerPonumber}"></td>
			<td><input type="text" id="contractAmount" style="width: 90px" readonly="readonly" value="${requestScope.customerPODetails.poamount0}"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material2" style="width: 200px" value="${requestScope.customerPODetails.podesc1}"></td>
			<td><input type="text" id="po_number2" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber1}"></td>
			<td><input type="text" id="po_amount2" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount1}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material3" style="width: 200px" value="${requestScope.customerPODetails.podesc2}"></td>
			<td><input type="text" id="po_number3" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber2}"></td>
			<td><input type="text" id="po_amount3" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount2}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material4" style="width: 200px" value="${requestScope.customerPODetails.podesc3}"></td>
			<td><input type="text" id="po_number4" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber3}"></td>
			<td><input type="text" id="po_amount4" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount3}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material5" style="width: 200px" value="${requestScope.customerPODetails.podesc4}"></td>
			<td><input type="text" id="po_number5" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber4}"></td>
			<td><input type="text" id="po_amount5" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount4}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material6" style="width: 200px" value="${requestScope.customerPODetails.podesc5}"></td>
			<td><input type="text" id="po_number6" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber5}"></td>
			<td><input type="text" id="po_amount6" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount5}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><label >Total Contract Amount:</label></td>
			<td><input type="text" id="totalContractamount" style="width: 90px"></td>
		</tr>
		<tr style="display: none;">
			<td><input type="text" id="joMasterID" style="width: 90px" value=""></td>
			<td><input type="text" id="customerPONumberID" style="width: 90px" value="${requestScope.joMasterDetails.customerPonumber}"></td>
			<td><input type="text" id="contractAmountID" style="width: 90px" value="${requestScope.joMasterDetails.contractAmount}"></td>
		</tr>
		</table>
		<hr>
		<table align="right" style="height: 45px">
			<tr>
				<td><input type="button" id="savecustomerpo" class="savehoverbutton turbo-tan" name="savecustomerpo" value="Save" style="width: 90px;" onclick="saveMainCustomerPONumber()"></td>
				<td><input type="button" id="cancelcustomerpo" class="savehoverbutton turbo-tan" name="cancelcustomerpo" value="Close" style="width: 90px;" onclick="cancelMainCustomerPONumber()"></td>
			</tr>
		</table>
	</form>
</div>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.ui.datetimepicker.min.js"></script>
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobWizardMain.js"></script> -->
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardMain.min.js"></script>   
	<script type="text/javascript"> 
		$(document).ready(function(){
			$('#loadingMainDiv').css({"visibility": "hidden"});
			//$("#jobHeader_JobCustomerName_id").val($("#customerHiddenID").val());
			parseModelMap();
			$("#OriJobNumber").css("display", "none");
			var aQuoteNumber = "${requestScope.joMasterDetails.quoteNumber}";
			if(aQuoteNumber !== ''){
				$("#OriJobNumber").show();
			}
		});

		function parseModelMap(){
			if("${sessionScope.jobToken}" === 'newJob'){
				$("#TaxValue").text("0.00%");
			} else if("${sessionScope.jobToken}" !== 'newJob'){
				var bidDate = customFormatDateTime("${requestScope.joMasterDetails.bidDate}");
				var bookedDate = customFormatDateTime("${requestScope.joMasterDetails.bookedDate}");
				var ClosedDate = customFormatDateTime("${requestScope.joMasterDetails.closedDate}");
				$("#bidDate_Date").val(bidDate);
				$("#bookedDate_Date").val(bookedDate);
				$("#closedDate_Date").val(ClosedDate);
				
				var changedOn = customFormatDate("${requestScope.joMasterDetails.changedOn}");
				var createdOn = customFormatDate("${requestScope.joMasterDetails.createdOn}");
				$("#created").val(createdOn);
				$("#changed").val(changedOn);
				var aDivision = "${requestScope.joMasterDetails.coDivisionId}";
				$("#jobMain_Divisions option[value=" + aDivision + "]").attr("selected", true);

				var taxpercent = "${requestScope.taxValue}";
				taxpercent = taxpercent.replace(".0000", ".00").concat("%");
				$("#TaxValue").text(taxpercent);
				taxTerritory  =  $("#TaxValue").text();
			}
			return true;
		}
	</script>