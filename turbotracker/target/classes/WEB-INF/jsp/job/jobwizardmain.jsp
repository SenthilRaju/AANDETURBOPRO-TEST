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
		 					<td><input type="text" id="jobcontractorcustomer" name="jobcontractorcustomer" class="validate[maxSize[100]" style="width: 350px;" value="${requestScope.joMasterDetails.locationName}" placeholder="Add Contractor Name here for shipping"></td>
		 				</tr>
		 				<tr>
		 					<td style="width: 90px"><label></label></td>
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
				 			<td>
                                  <label style="color: #3F3731;">Status: </label>
                                  <select id="bidDate_jobStatusList" name="joBidStatusId" style="width: 85px;" class="validate[required] select">
                                    <option value="-1"> - Select - </option>
                                    <c:forEach var="joBidStatusBean" items="${requestScope.bidStatusForBidDateForm}">
                                        <option value="${joBidStatusBean.joBidStatusId}">
                                            <c:out value="${joBidStatusBean.description}" ></c:out>
                                        </option>
                                    </c:forEach>                                  
</select>
                            </td>
				 		</tr>
				 		<tr>
				 			<td style="width:145px"><label>Original Bid Date:</label></td>
				 			<td><input type="text" class="datepickerCustom" name="originalbidDate" id="originalbidDate_Date"></td>
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
						<tr id="JobCategory1Row" style="display: inline-block;"><td ><label id="JobCategory1label" style="display: inline-block;width: 99px;">Salesman:</label></td>
							<td>&nbsp;
									<input  type="text" id="jobMain_salesRepsList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedSalesRep}"  placeholder="Minimum 1 characters required"  onkeyup="salesmanAdminValidation(this)"></td>
<!-- 									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory1image" style="display: inline-block;"></td> -->
									<td><input type="hidden" id="salesRepId" name="empSalesMan" value="${requestScope.joMasterDetails.cuAssignmentId0}" style="width: 30px;"></td>
							</tr>
							<tr id="JobCategory2Row" style="display: inline-block;"><td><label id="JobCategory2label" style="display: inline-block;width: 99px;">CSR:</label></td>
							<td>&nbsp;
									<input  type="text" id="jobMain_CSRList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedCSRs}"  placeholder="Minimum 1 characters required"></td>
<!-- 									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory2image" style="display: inline-block;"></td> -->
									<td><input type="hidden" id="CSRId" value="${requestScope.joMasterDetails.cuAssignmentId1}" name="empCSR" style="width: 30px;">
							</td></tr>
							<tr id="JobCategory3Row" style="display: inline-block;"><td><label id="JobCategory3label" style="display: inline-block;width: 99px;">Sales Mgr.:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_SalesMgrList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedSalesMGRs}"  placeholder="Minimum 1 characters required"></td>
<!-- 										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory3image" style="display: inline-block;"></td> -->
										<td><input type="hidden" id="salesMgrId" name="empSalesMgr" value="${requestScope.joMasterDetails.cuAssignmentId2}" style="width: 30px;">
								</td>
							</tr>
							<tr id="JobCategory4Row" style="display: inline-block;"><td><label id="JobCategory4label" style="display: inline-block;width: 99px;">Engineer:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_EngineersList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedEngineers}"  placeholder="Minimum 1 characters required"></td>
<!-- 										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory4image" style="display: inline-block;"></td> -->
										<td><input type="hidden" id="engineerId" name="empEngineer" value="${requestScope.joMasterDetails.cuAssignmentId3}" style="width: 30px;">
								</td>
							</tr>
							<tr id="JobCategory5Row" style="display: inline-block;"><td><label id="JobCategory5label" style="display: inline-block;width: 99px;">Prj. Mgr.:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_PrjMgrList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedProjManagers}" placeholder="Minimum 1 characters required"></td>
<!-- 										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory5image" style="display: inline-block;"></td> -->
										<td><input type="hidden" id="prjMgrId" name="empPrjMgr" value="${requestScope.joMasterDetails.cuAssignmentId4}" style="width: 30px;">
								</td>
							</tr>
							<tr id="JobCategory6Row" style="display: inline-block;"><td><label id="JobCategory6label" style="display: inline-block;width: 99px;">Take off:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_TakeOffList" style="width: 290px;display: inline-block;"  value="${requestScope.AssignedTakeOff}"  placeholder="Minimum 1 characters required"></td>
<!-- 										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory6image" style="display: inline-block;"></td> -->
										<td><input type="hidden" id="takeOffId" name="empTakeOff" value="${requestScope.joMasterDetails.cuAssignmentId5}" style="width: 30px;">
								</td>
							</tr>
							<tr id="JobCategory7Row" style="display: inline-block;"><td><label id="JobCategory7label" style="display: inline-block;width: 99px;">Quote by:</label></td>
								<td>&nbsp;
										<input  type="text" id="jobMain_QuoteByList" style="width: 290px;display: inline-block;" value="${requestScope.AssignedQuoteBy}"  placeholder="Minimum 1 characters required"></td>
<!-- 										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="JobCategory7image" style="display: inline-block;"></td> -->
									   <td><input type="hidden" id="quoteId" name="empQuoteBy" value="${requestScope.joMasterDetails.cuAssignmentId6}" style="width: 30px;">
								</td>
							</tr>
							<tr><td><input type="checkbox" id="splitComId"  style="background-color: red;" readonly="readonly">
							<input type="button" value="Split Commission" class="savehoverbutton turbo-tan" style="width: 110px;" onclick="addempinsplitcom(); createtpusage('job-Main Tab','Split Commission','Info','Split Commission Button click on Inside Job-Main Tab');">
							    </td>
							    <td>
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
								    <td style="width:145px"><label id="Category1hiddenlabel">Architect:</label></td>
								  	<td><input  type="text" id="jobMain_architectsList" style="width: 250px;" value="${requestScope.AssignedArchitects}"  placeholder="Minimum 2 characters required"></td>
								  	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" OnClick="ShowRolodexDetails(1)"></td>
									<td><input type="hidden" id="architectId" name="teamArchitect" value="${requestScope.joMasterDetails.rxCategory1}" style="width: 30px;"></td>
								  </tr>
								  <tr>
								  	<td style="width:145px"><label id="Category2hiddenlabel">Engineer: &nbsp;</label></td>
									<td><input  type="text" id="jobMain_engineersRXList" style="width: 250px;" value="${requestScope.AssignedEngineering}"  placeholder="Minimum 2 characters required"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" OnClick="ShowRolodexDetails(2)"></td>
									<td><input type="hidden" id="engineersRXId" name="teamEngineer" value="${requestScope.joMasterDetails.rxCategory2}" style="width: 30px;"></td>
								  </tr>
								  <tr>
								  	<td style="width: 160px;"><label class="">General Contractor/ Construction Mgr.:</label></td>
									<td><input  type="text" id="jobMain_GCList" style="width: 250px;" value="${requestScope.AssignedGcConstrMgrs}"  placeholder="Minimum 2 characters required"></td>
									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" OnClick="ShowRolodexDetails(3)"></td>
									<td><input type="hidden" id="GCId"  name="teamGC" value="${requestScope.joMasterDetails.rxCategory3}" style="width: 30px;"></td></tr>
								</table>
							</fieldset>
						</td></tr>
						<tr><td>
						<br>
							<fieldset class= "custom_fieldset" style="width:500px;padding: 8px 8px 8px;">
							<table>
								<tr>
									<td style="width:145px"><label>Division:</label><span style="color:red;" id="jobDivisionreqspanID"></span></td><td>
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
<!-- 									<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td> -->
								  	<td><input type="hidden" id="TaxTerritory" name="taxPersent"  value="${requestScope.joMasterDetails.coTaxTerritoryId}" style="width: 30px;"></td>
									<td><label id = "TaxValue">%</label></td>
								</tr>
								<tr style="">
									<td style="width:150px"><label>Customer PO:</label><span style="color:red;" id="jobCPOreqspanID"></span></td>
									<td><input  type="text" id="jobmain_ponumber" maxlength="40" name ="jobmainPONumber" style="width: 200px;" value="${requestScope.joMasterDetails.customerPonumber}" ></td>
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
		<tr><td><hr style="width:1100px; color:#C2A472;" align="center"><div id="quoteMsg" style="display: none;"></div></td></tr>
		<tr>
			<td align="right"><input type="button"  value="Save" class="savehoverbutton turbo-tan" id="mainsave" onclick="jobMainForm_save()" style="width:80px;">
			<input type="button" class="savehoverbutton turbo-tan" value="Delete" onclick="deleteQuoteJob()" style="width :90px;"></td>
		</tr>
	</table>
	<div id="addEngineerDialog">
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
					<input type="text" id="jobSplitCommissionChkbx" value="${requestScope.Assignchkboxtickstatus}">
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
			<td><input type="text" id="po_number1"  maxlength="40" style="width: 110px" value="${requestScope.joMasterDetails.customerPonumber}"></td>
			<td><input type="text" id="contractAmount" style="width: 90px" readonly="readonly" value="${requestScope.customerPODetails.poamount0}"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material2" style="width: 200px" value="${requestScope.customerPODetails.podesc1}"></td>
			<td><input type="text" id="po_number2" maxlength="40" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber1}"></td>
			<td><input type="text" id="po_amount2" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount1}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material3" style="width: 200px" value="${requestScope.customerPODetails.podesc2}"></td>
			<td><input type="text" id="po_number3" maxlength="40" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber2}"></td>
			<td><input type="text" id="po_amount3" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount2}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material4" style="width: 200px" value="${requestScope.customerPODetails.podesc3}"></td>
			<td><input type="text" id="po_number4" maxlength="40" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber3}"></td>
			<td><input type="text" id="po_amount4" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount3}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material5" style="width: 200px" value="${requestScope.customerPODetails.podesc4}"></td>
			<td><input type="text" id="po_number5" maxlength="40" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber4}"></td>
			<td><input type="text" id="po_amount5" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount4}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td><input type="text" id="covered_material6" style="width: 200px" value="${requestScope.customerPODetails.podesc5}"></td>
			<td><input type="text" id="po_number6" maxlength="40" style="width: 110px" value="${requestScope.customerPODetails.customerPonumber5}"></td>
			<td><input type="text" id="po_amount6" style="width: 90px" class="validate[custom[number]]" value="${requestScope.customerPODetails.poamount5}" onchange="POAmountCalculation()" onclick="removeDollarSymbl()"></td>
		</tr>
		<tr>
			<td colspan="2" align="right"><label >Total Contract Amount:</label></td>
			<td><input type="text" id="totalContractamount" style="width: 90px"></td>
		</tr>
		<tr style="display: none;">
			<td><input type="text" id="joMasterID" style="width: 90px" value="${requestScope.joMasterDetails.joMasterId}"></td>
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
<div id="openSplitcommDia">
	<form action="" id="opensplitcommisionForm">
 	<input type="hidden" name="repId" id="repId" >
	<input type="hidden" name="splittypeId" id="splittypeId" > 
		<div id="addsplitcommission"></div>
		<table>
		        <tr>
					<td>
					<span id='CommissionSplitsGridDiv'>
						<table id="CommissionSplitsGrid"></table>
						<div id="CommissionSplitsGridPager"></div>
					</span>
					</td>
				</tr>
		</table>
		
	</form>
</div>

	<script type="text/javascript" src="./../resources/web-plugins/jquery.ui.datetimepicker.min.js"></script>
<!-- 	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardMain.min.js"></script>  -->
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobWizardMain.js"></script>
	<script type="text/javascript"> 
	var _blockjsfiles = true;
	var globalJoMasterID = 0;
		$(document).ready(function(){
			globalJoMasterID = "${requestScope.joMasterDetails.joMasterId}";
			var Divisionsysvariable=getSysvariableStatusBasedOnVariableName('RequireaDivisionwhencreatingJobs');
			if(Divisionsysvariable!=null && Divisionsysvariable[0].valueLong==1){
				$("#jobDivisionreqspanID").html("*");
				}else{
				$("#jobDivisionreqspanID").html("");
				}
			var CustomerPOsysvariable=getSysvariableStatusBasedOnVariableName('CustomerPOReqYN');
			if(CustomerPOsysvariable!=null && CustomerPOsysvariable[0].valueLong==1){
				$("#jobCPOreqspanID").html("*");
				}else{
				$("#jobCPOreqspanID").html("");
				}
		  $('#splitComId').attr('readonly',true)
			$('#loadingMainDiv').css({"visibility": "hidden"});
			//$("#jobHeader_JobCustomerName_id").val($("#customerHiddenID").val());
			parseModelMap();
			$("#OriJobNumber").css("display", "none");
			var aQuoteNumber = "${requestScope.joMasterDetails.quoteNumber}";
			if(aQuoteNumber !== ''){
				$("#OriJobNumber").show();
			}
			var chkboxstatus=document.getElementById("jobSplitCommissionChkbx").value;
			if(chkboxstatus=="true"){
				document.getElementById("splitComId").checked=true;
				document.getElementById("splitComId").disabled=true;
				}

			  
				jQuery("#openSplitcommDia").dialog({
					autoOpen:false,
					width:460,
					title:"Commission Splits",
					modal:true,
					buttons:{
						"Save & Close":function(){
							createtpusage('job-Main Tab','Save Split Commission','Info','Job-Main Tab,Saving Split Commission');
							var allRowsInGrid = $('#CommissionSplitsGrid').jqGrid('getRowData');
							var aVal = new Array(); 
							var aTax = new Array();
							var sum = 0;
							$.each(allRowsInGrid, function(index, value) {
								aVal[index] = value.allocated;
								var number1 = aVal[index];
								var number2 = number1.replace(".00", "");
								var number3 = number2.replace(",", "");
								var number4 = number3.replace(",", "");
								var number5 = number4.replace(",", "");
								var number6 = number5.replace(",", "");
								sum = Number(sum) + Number(number1); 
							});
							if(Number(sum)<100){
								var newDialogDiv2 = jQuery(document.createElement('div'));
								var errorText = "Allocated % is not Equal to 100%";
								jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
								jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
									buttons : [ {
														height : 35,
														text : "OK",
														click : function() {
															$(this).dialog("close");
															return false;
												}
											}]
												}).dialog("open");
							}
							else if(Number(sum)>100){
								var newDialogDiv2 = jQuery(document.createElement('div'));
								var errorText = "Allocated % is Greater than 100 Please Delete Excess Values";
								jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
								jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
									buttons : [ {
														height : 35,
														text : "OK",
														click : function() {
															$(this).dialog("close");
															return false;
												}
											}]
												}).dialog("open");
							}
							else{

								 var jorCommissionSplitGridRows = $('#CommissionSplitsGrid').getRowData();
								 var jorSplitCommissionGridData = JSON.stringify(jorCommissionSplitGridRows);
								// alert("Commission Grids::"+gridDataToSend);
								
								 var rows = jQuery("#CommissionSplitsGrid").getDataIDs();
								 deleteCommissionSplitJorID=new Array();
									 for(var a=0;a<rows.length;a++)
									 {
									    row=jQuery("#CommissionSplitsGrid").getRowData(rows[a]);
									   var id="#canDeleteJomID_"+rows[a];
									   var canDeleteJor=$(id).is(':checked');
									   if(canDeleteJor){
										  var ecSplitJobId=row['ecSplitJobId'];
										  if(ecSplitJobId!=undefined && ecSplitJobId!=null && ecSplitJobId!="" && ecSplitJobId!=0){
											  deleteCommissionSplitJorID.push(ecSplitJobId);
										 	}
										 $('#CommissionSplitsGrid').jqGrid('delRowData',rows[a]);
									  }
								}
								//alert(deleteCommissionSplitJorID);
								$.ajax({
						 			url: './jobtabs3/manipulateSplitCommission',
						 			type : 'POST',
						 			data: {'joMasterID':globalJoMasterID,'tabpage':'JoMaster','commissionSplitGridData':jorSplitCommissionGridData,'commissionSplitdelData':deleteCommissionSplitJorID},
						 			success: function (data){
							 			
						 				calculateSplitCommission();
						 				
						 				}
						        	});
								$("#jobreleasehiddenId").val("");
								$('#openSplitcommreleaseDia').dialog("close");
							
							//	$("#jobreleasehiddenId").val("");
								$('#openSplitcommDia').dialog("close");
							}
							}
							},
					close:function(){$('#opensplitcommisionForm').validationEngine('hideAll');
					return true;}	
				});
				
				
				
				
				
			var CommissionSplitsGridsum=0;
			var updatecommsplitgrid=0;
			var selectedid=0;
			
				 loadjobcategories();
				 loadEngorarclbl();
		});

		/*./commissionsplits_listgrid*/
		function calculateSplitCommission(){
			var rxmasterid=$('#releaserepId').val();
			var jomasterid=$("joMasterID").val();
			var percentage=$('#allocated').val();
			var splittypeID=$('#releasesplittypeId').val();
			var splittype=$('#splittype').val();
			var joreleaseid=$("#jobreleasehiddenId").val();
			if(typeof (jomasterid) == 'undefined'){
				jomasterid = "${requestScope.joMasterDetails.joMasterId}";
				$("joMasterID").val("${requestScope.joMasterDetails.joMasterId}");
				}
			console.log('JoMasterID::'+globalJoMasterID);
			//alert(jomasterid);
			/*createtpusage('job-Release Tab','Save Split-Commission','Info','Job,Release Tab,Save Split Commission,JoReleaseId:'+joreleaseid);*/
			
			createtpusage('Drop Ship-Release Tab','View Split-Commission','Info','Job,Release Tab,Drop Ship Split Commission ,JoReleaseId:'+$("#jobreleasehiddenId").val());
			$('#CommissionSplitsGrid').jqGrid('GridUnload');
			$("#CommissionSplitsGridDiv").empty();
			$("#CommissionSplitsGridDiv").append("<table id='CommissionSplitsGrid'></table><div id='CommissionSplitsGridPager'></div>");
			$('#CommissionSplitsGrid').jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#CommissionSplitsGridPager'),
			url:'./jobtabs3/jobCommissionListGrid',
			postData: {'JoMasterId':jomasterid,'tabpage':'JoMaster'},
			colNames:[ 'Id','Rep','', '% Allocated', 'Split Type','','<img src="./../resources/images/delete.png" style="vertical-align: middle;">'],
			colModel :[
						{name:'ecSplitJobId', index:'ecSplitJobId', align:'left',editable:true, width:25,hidden:true},
			           	{name:'rep', index:'rep', align:'left', width:48, editable:true,hidden:false, editoptions:{size:12,
							 dataInit: function (elem) {
								
									//alert("aSelectedRowId = "+aSelectedRowId+" || prMasteId = "+$("#"+aSelectedRowId+"_prMasterId").val());
						            $(elem).autocomplete({
						                source: 'jobtabs3/getEmployeewithNameList',
						                minLength: 1,
						                select: function (event, ui) {  ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
						                var product = ui.item.label; 
						                $("#releaserepId").val(id);
						                var selectedRowId = $("#CommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
						                $("#"+selectedRowId+"_rxMasterId").val(id);
						                
						                $.ajax({
									        url: "./jobtabs3/getEmployeeCommissionDetail",
									        data: {id:id},
									        type: 'GET',
									        success: function(data){
									        	 var aSelectedRowId = $("#CommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
										        if(data!=null)
										        	$("#"+aSelectedRowId+"_allocated").val(data.jobCommissions);
									        	
									        }
									   }); 
						                
						                } }); $(elem).keypress(function (e) {
							                
						                    if (!e) e = window.event;
						                    if (e.keyCode == '13') {
						                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
						                        return false;
						                    }
						                });}	},editrules:{edithidden:false,required: true}},
						{name:'rxMasterId', index:'rxMasterId', align:'left',editable:true, width:32,hidden:true, editoptions:{size:6}},
						{name:'allocated', index:'allocated', align:'left',editable:true, width:32,hidden:false, editoptions:{size:6}},
						//{name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:40,hidden:true},
						{name:'splittype', index:'splittype', align:'',editable:true, width:70,hidden:false,  editoptions:{size:19,
							 dataInit: function (elem) {
						            $(elem).autocomplete({
						                source: 'jobtabs3/getSplitTypewithNameList',
						                minLength: 1,
						                select: function (event, ui) { ;$(elem).focus().trigger({ type: 'keypress', charCode: 13 });var id = ui.item.id;  
						                var product = ui.item.label;
						                $("#releasesplittypeId").val(id);
						                var selectedRowId = $("#CommissionSplitsGrid").jqGrid('getGridParam', 'selrow');
						                $("#"+selectedRowId+"_ecSplitCodeID").val(id);
										//Ajax starting point
										/* Commented By Zenith
										 * $.ajax({
										        url: "./jobtabs3/getpercentagebasedonsplittype",
										        data: {id:id},
										        type: 'GET',
										        success: function(data){
											        if(data!=null)
											        	$("#allocated").val(data.defaultPct);
										        	
										        }
										   }); */
										//Ajax End Part
						               
						                } }); $(elem).keypress(function (e) {
							                
						                    if (!e) e = window.event;
						                    if (e.keyCode == '13') {
						                         setTimeout(function () { $(elem).autocomplete('close'); }, 500);
						                        return false;
						                    }
						                });}	}, editrules:{edithidden:false,required: true}},
						 {name:'ecSplitCodeID', index:'ecSplitCodeID', align:'left',editable:true, width:10,hidden:true, editoptions:{size:6}},
						 {name:'canDelete', index:'canDelete', align:'center',  width:20, hidden:false, editable:false, formatter:canDeleteJomCheckboxFormatter,   editrules:{edithidden:true}}],
			rowNum: 0, 
			pgbuttons: false, 
			recordtext: '', 
			rowList: [], 
			pgtext: null, 
			viewrecords: false,
			sortname: 'rep', 
			sortorder: "asc", 
			imgpath: 'themes/basic/images', 
			caption: false,
			height:200,	
			width: 425, 
			rownumbers:false, 
			altRows: true, 
			altclass:'myAltRowClass', 
			caption: '',
			cellsubmit: 'clientArray',
			editurl: 'clientArray',
			loadonce: false,
			cellEdit: false,
			jsonReader : {
				root: "rows",
				page: "page",
				total: "total",
				records: "records",
				repeatitems: false,
				cell: "cell",
				id: "ecSplitJobId",
				userdata: "userdata"
			},
			gridComplete:function(){
				},
			loadComplete: function(data) {
				var allRowsInGrid = $('#CommissionSplitsGrid').jqGrid('getRowData');
				var  count= $('#CommissionSplitsGrid').getGridParam('reccount');
				var rowid=$("#jorowhiddenId").val();
				
				if(rowid!=null && rowid!=""){}
				
				var aVal = new Array(); 
				var sum = 0;
				$.each(allRowsInGrid, function(index, value) {
					aVal[index] = value.allocated;
					sum = Number(sum) + Number(aVal[index]); 
				});
				releaseCommissionSplitsGridsum=sum;

				var rows = $('#CommissionSplitsGrid').getGridParam('records');
 				//jQuery("#CommissionSplitsGrid").getDataIDs();
				if(rows>0)
					{
 						document.getElementById("splitComId").checked=true;
 						document.getElementById("splitComId").disabled=true;
 					}else
	 				{
 						document.getElementById("splitComId").checked=false;
	 					document.getElementById("splitComId").disabled=true;
	 				}
				checkCommissionPaidJoMaster(jomasterid,'')
			},
			loadError : function (jqXHR, textStatus, errorThrown){	},
			onSelectRow:  function(id){
				SplitCommissionID = id;
				 var rowData = jQuery(this).getRowData(id); 
				 releaseselectedid=rowData["ecSplitJobId"];
				 releaseupdatecommsplitgrid=Number(releaseCommissionSplitsGridsum)-Number(rowData["allocated"]);
			 },
			onCellSelect : function (rowid,iCol, cellcontent, e) {
				 //alert(rowid+"--"+iCol+"--"+cellcontent+"--"+e);
				 //console.log(e);
				},
			//editurl:"./jobtabs3/manipulateSplitCommission"
			}).navGrid("#CommissionSplitsGridPager", {
				add : false,
				edit : false,
				del : false,
				search : false,
				refresh : false,
				pager : false,
				alertzIndex : 1234,
				alertcap : "Warning",
				alerttext : 'Please select Sales Rep'
			});
			$("#CommissionSplitsGrid").jqGrid(
					'inlineNav',
					'#CommissionSplitsGridPager',
					{
						edit : true,
						edittext : "Edit",
						add : true,
						addtext : "Add",
						cancel : true,
						canceltext :"Cancel",
						savetext : "Save",
						refresh : true,
						alertzIndex : 10000,
						addParams : {
							addRowParams : {
								keys : false,
								oneditfunc : function() {
									console.log("edited");
									 $("#del_CommissionSplitsGrid").addClass('ui-state-disabled');
								},
								successfunc : function(response) {
									console.log("successfunc");
									console.log(response);
									return true;
								},
								aftersavefunc : function(id) {
									console.log("aftersavefunc");
									var ids = $("#CommissionSplitsGrid").jqGrid('getDataIDs');
									var cuinvrowid;
										if(ids.length==1){
											cuinvrowid = 0;
										}else{
											var idd = jQuery("#CommissionSplitsGrid tr").length;
											for(var i=0;i<ids.length;i++){
												if(idd<ids[i]){
													idd=ids[i];
												}
											}
											cuinvrowid= idd;
										}
										if(SplitCommissionID=="new_row"){
											$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
										}
										var rids = $('#CommissionSplitsGrid').jqGrid('getDataIDs');
										var nth_row_id = rids[0];
										validateJoMasterCommissionTotals(nth_row_id);
										$("#CommissionSplitsGrid").trigger("reload");
										var grid=$("#CommissionSplitsGrid");
										grid.jqGrid('resetSelection');
										var dataids = grid.getDataIDs();
										for (var i=0, il=dataids.length; i < il; i++) {
										   grid.jqGrid('setSelection',dataids[i], false);
										}
								},
								errorfunc : function(rowid, response) {
									console.log('An Error');
									$("#info_dialog").css("z-index", "1234");
									$(".jqmID1").css("z-index", "1234");
									return false;
								},
								afterrestorefunc : function(rowid) {
									console.log("afterrestorefunc");
								}
							}
						},
						editParams : {
							keys : false,
							successfunc : function(response) {
								console.log(response.responseText);
								console.log('successfunc - editParams');
								return true;
							},
							aftersavefunc : function(id) {
						        $("#del_CommissionSplitsGrid").removeClass('ui-state-disabled');
						        var ids = $("#CommissionSplitsGrid").jqGrid('getDataIDs');
								var cuinvrowid;
									if(ids.length==1){
										cuinvrowid = 0;
									}else{
										var idd = jQuery("#CommissionSplitsGrid tr").length;
										for(var i=0;i<ids.length;i++){
											if(idd<ids[i]){
												idd=ids[i];
											}
										}
										cuinvrowid= idd;
									}
									if(SplitCommissionID=="new_row"){
										$("#" + SplitCommissionID).attr("id", Number(cuinvrowid)+1);
									}
									var rids = $('#CommissionSplitsGrid').jqGrid('getDataIDs');
									var nth_row_id = rids[0];
									validateJoMasterCommissionTotals(nth_row_id);
									$("#CommissionSplitsGrid").trigger("reload");
									var grid=$("#CommissionSplitsGrid");
									grid.jqGrid('resetSelection');
									var dataids = grid.getDataIDs();
									for (var i=0, il=dataids.length; i < il; i++) {
									   grid.jqGrid('setSelection',dataids[i], false);
									}
								console.log('afterSavefunc editparams');
							},
							errorfunc : function(rowid, response) {
								console.log(' editParams -->>>> An Error');
								$("#info_dialog").css("z-index", "1234");
								$(".jqmID1").css("z-index", "1234");
								$("#del_CommissionSplitsGrid").removeClass('ui-state-disabled');
								$("#CommissionSplitsGrid").trigger("reload");
								//return false;
			
							},
							afterrestorefunc : function( id ) {
								$("#del_CommissionSplitsGrid").removeClass('ui-state-disabled');
								console.log('editParams -> afterrestorefunc');
						    },
							// oneditfunc: setFareDefaults
							oneditfunc : function(id) {
								console.log('OnEditfunc');
								$("#del_CommissionSplitsGrid").addClass('ui-state-disabled');
			                	/*var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
			                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
			                	alert(" >>>>>>>>>>>> "+$("#"+aSelectedRowId+"_cuSodetailId").val());
			                	 */
								}
						}
				});
	}


		function checkCommissionPaidJoMaster(joMasterID,JoReleaseID){
			var comStatus = 0;
			$.ajax({
				url: "./jobtabs5/CheckCommissionPaidRelease",
				type: "POST",
				async:false,
				data : {"joReleaseID":JoReleaseID,"joMasterID":joMasterID},
				success: function(data) {
					comStatus = data;
				}
			});
			
			if(comStatus==1){
				  var idis = $("#CommissionSplitsGrid").jqGrid('getDataIDs');
				for(var i=0;i<=idis.length;i++){
					$("#canDeleteJomID_"+idis[i]).prop("disabled", true);
				}
				$("#CommissionSplitsGrid_iladd").addClass('ui-state-disabled');
				$("#CommissionSplitsGrid_iledit").addClass('ui-state-disabled');
		}
		}
				
		
		$("#jobTabJournalHeader").html('');		
		var jomasterid=$('#joMasterHiddenID').val();
		$.ajax({
			type : "POST",
			url : "./jobtabs7/journalHeaderIcon?joMasterID="+jomasterid,
			async:false,
			success: function(response){
				$("#jobTabJournalHeader").html('');
				if(response == "open" || response == "")
					$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Used.bmp"/>');
				else if(response == "resolved")
					$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Unresolved.bmp"/>');
				else if(response == "nojournals")
					$("#jobTabJournalHeader").append('Journal <img src="../resources/Icons/Note-Empty.bmp"/>');
			},
			error : function(xhr, ajaxOptions, thrownError) {
			}
		});
		
		function loadEngorarclbl(){
			var Category1hidden=$("#Category1hidden").val();
			var Category2hidden=$("#Category2hidden").val();

			$("#Category2hiddenlabel").text(Category2hidden);
			$("#Category1hiddenlabel").text(Category1hidden);
			}
		/*
		Updated by:Velmurugan
		Updated on:9-1-2014
		Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
		*/
		function loadjobcategories(){
			
			var Category1labDesc='${requestScope.JobCategory1}';
			var Category2labDesc="${requestScope.JobCategory2}";
			var Category3labDesc="${requestScope.JobCategory3}";
			var Category4labDesc="${requestScope.JobCategory4}";
			var Category5labDesc="${requestScope.JobCategory5}";
			var Category6labDesc="${requestScope.JobCategory6}";
			var Category7labDesc="${requestScope.JobCategory7}";
			//CustomerCategory1label
			$("#JobCategory1label").text(Category1labDesc);
			$("#JobCategory2label").text(Category2labDesc);
			$("#JobCategory3label").text(Category3labDesc);
			$("#JobCategory4label").text(Category4labDesc);
			$("#JobCategory5label").text(Category5labDesc);
			$("#JobCategory6label").text(Category6labDesc);
			$("#JobCategory7label").text(Category7labDesc);
			
			//jobMain_salesRepsList jobMain_CSRList jobMain_SalesMgrList jobMain_EngineersList 
			//jobMain_PrjMgrList jobMain_TakeOffList jobMain_QuoteByList
			if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
				$("#JobCategory1label").css({ display: "none" });
				$("#jobMain_salesRepsList").css({ display: "none" });
				$("#JobCategory1image").css({ display: "none" });
				$("#JobCategory1Row").css({ display: "none" });
				
			}else{
				$("#JobCategory1label").css({ display: "inline-block" });
				$("#jobMain_salesRepsList").css({ display: "inline-block" });
				$("#JobCategory1image").css({ display: "inline-block" });
				$("#JobCategory1Row").css({ display: "inline-block" });
			}
			if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
				$("#JobCategory2label").css({ display: "none" });
				$("#jobMain_CSRList").css({ display: "none" });
				$("#JobCategory2image").css({ display: "none" });
				$("#JobCategory2Row").css({ display: "none" });
			}else{
				$("#JobCategory2label").css({ display: "inline-block" });
				$("#jobMain_CSRList").css({ display: "inline-block" });
				$("#JobCategory2image").css({ display: "inline-block" });
				$("#JobCategory2Row").css({ display: "inline-block" });
			}
			if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
				$("#JobCategory3label").css({ display: "none" });
				$("#jobMain_SalesMgrList").css({ display: "none" });
				$("#JobCategory3image").css({ display: "none" });
				$("#JobCategory3Row").css({ display: "none" });
			}else{
				$("#JobCategory3label").css({ display: "inline-block" });
				$("#jobMain_SalesMgrList").css({ display: "inline-block" });
				$("#JobCategory3image").css({ display: "inline-block" });
				$("#JobCategory3Row").css({ display: "inline-block" });
			}
			if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
				$("#JobCategory4label").css({ display: "none" });
				$("#jobMain_EngineersList").css({ display: "none" });
				$("#JobCategory4image").css({ display: "none" });
				$("#JobCategory4Row").css({ display: "none" });
			}else{
				$("#JobCategory4label").css({ display: "inline-block" });
				$("#jobMain_EngineersList").css({ display: "inline-block" });
				$("#JobCategory4image").css({ display: "inline-block" });
				$("#JobCategory4Row").css({ display: "inline-block" });
			}
			if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
				$("#JobCategory5label").css({ display: "none" });
				$("#jobMain_PrjMgrList").css({ display: "none" });
				$("#JobCategory5image").css({ display: "none" });
				$("#JobCategory5Row").css({ display: "none" });
			}else{
				$("#JobCategory5label").css({ display: "inline-block" });
				$("#jobMain_PrjMgrList").css({ display: "inline-block" });
				$("#JobCategory5image").css({ display: "inline-block" });
				$("#JobCategory5Row").css({ display: "inline-block" });
			}
			if(Category6labDesc==undefined ||Category6labDesc==null ||Category6labDesc==""||Category6labDesc.length==0){
				$("#JobCategory6label").css({ display: "none" });
				$("#jobMain_TakeOffList").css({ display: "none" });
				$("#JobCategory6image").css({ display: "none" });
				$("#JobCategory6Row").css({ display: "none" });
			}else{
				$("#JobCategory6label").css({ display: "inline-block" });
				$("#jobMain_TakeOffList").css({ display: "inline-block" });
				$("#JobCategory6image").css({ display: "inline-block" });
				$("#JobCategory6Row").css({ display: "inline-block" });
			}
			if(Category7labDesc==undefined ||Category7labDesc==null ||Category7labDesc==""||Category7labDesc.length==0){
				$("#JobCategory7label").css({ display: "none" });
				$("#jobMain_QuoteByList").css({ display: "none" });
				$("#JobCategory7image").css({ display: "none" });
				$("#JobCategory7Row").css({ display: "none" });
			}else{
				$("#JobCategory7label").css({ display: "inline-block" });
				$("#jobMain_QuoteByList").css({ display: "inline-block" });
				$("#JobCategory7image").css({ display: "inline-block" });
				$("#JobCategory7Row").css({ display: "inline-block" });
			}
			
			
		}
		function addempinsplitcom(){
			  try{
			   	calculateSplitCommission();
			  	jQuery("#openSplitcommDia").dialog("open");
			  }catch(err){
				  alert(err.message);
			  }
			  return true;
			}
		
		function parseModelMap(){
			if("${sessionScope.jobToken}" === 'newJob'){
				$("#TaxValue").text("0.00%");
			} else if("${sessionScope.jobToken}" !== 'newJob'){
				var bidDate = customFormatDateTime("${requestScope.joMasterDetails.bidDate}");
				var bookedDate = customFormatDateTime("${requestScope.joMasterDetails.bookedDate}");
				var ClosedDate = customFormatDateTime("${requestScope.joMasterDetails.closedDate}");
				var OriginalBidDate = customFormatDateTime("${requestScope.joMasterDetails.originalBidDate}");
				$("#bidDate_Date").val(bidDate);
				$("#bookedDate_Date").val(bookedDate);
				$("#closedDate_Date").val(ClosedDate);
				$("#originalbidDate_Date").val(OriginalBidDate);
				
				
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
			    var bidDate_jobStatusList = "${requestScope.joMasterDetails.joBidStatusId}";
			    
                $("#bidDate_jobStatusList option[value=" + bidDate_jobStatusList + "]").attr("selected", true);
			}
			return true;
		}
		
		function deletemethod(){
			
			$("#CountInventoryGrid").trigger("reloadGrid");
			var validateMsg = "<b style='color:Green; align:right;'>Customer Type Saved Successfully.</b>";	
			$("#invTypeMsg").css("display", "block");
			$('#invTypeMsg').html(validateMsg);
			setTimeout(function(){
			$('#invTypeMsg').html("");						
			},2000);
		}
		
		function deleteQuoteJob(){
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete this Job Record?</b></span>');
			jQuery(newDialogDiv).dialog({modal:!0,width:300,height:120,title:"Confirm Delete",buttons:{
				Submit:function(){jQuery(this).dialog("close");
				$("#loadingMainDiv").css({visibility:"visible"});
				var b=$("#joMasterHiddenID").val(),d=$("#jobNumberHiddenID").val();
				 createtpusage('job-Main Tab','Delete Job','Info','Job-Main Tab,Deleting Job,JOMasterID:'+b+',jobNumber:'+d);
				$.ajax({url:"./job_controller/deletejob",
					type:"POST",
					data:{joMasterID:b,jobNumber:d},
					success:function(a){$("#loadingMainDiv").css({visibility:"hidden"});
					var validateMsg = "<b style='color:Green; align:right;'>Job Deleted Successfully.</b>";	
					createtpusage('job-Main Tab','Delete Job','Info','Job-Main Tab,Delete Job,JobNumber ID-'+d);
					$("#quoteMsg").css("display", "block");
					$('#quoteMsg').html(validateMsg);
					setTimeout(function(){
					$('#quoteMsg').html("");						
					},2000);
					document.location.href="./home";
					}
			});}}}).dialog("open")
			}

	/**Created by: Praveenkumar
	*Date : 2014-09-17
	*Purpose : Show rolodex page on click magnify
	*/	
		function ShowRolodexDetails(id){
			var rxNumber = '';
			var name = '';
			if(id==1){
				name  = $('#jobMain_architectsList').val();
				rxNumber = $('#architectId').val();
			}else if(id==2){
				name  = $('#jobMain_engineersRXList').val();
				rxNumber = $('#engineersRXId').val();
			}else{
				name  = $('#jobMain_GCList').val();
				rxNumber = $('#GCId').val();
			}
				if(rxNumber!='' && name!=''){
					var name1 = name.replace('&', 'and');
					var name2 = name1.replace('&', 'and');
					document.location.href = "./customerdetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
				}
		}

		function canDeleteJomCheckboxFormatter(cellValue, options, rowObject){
			var id="canDeleteJomID_"+options.rowId;
			var element = "<input type='checkbox' id='"+id+"' onclick='deleteCheckboxChangesJom(this.id)'>";
			return element;
		}

		function deleteCheckboxChangesJom(id){
			id="#"+id;
			console.log("deleteCheckboxChanges::"+id);
		    var canDo=$(id).is(':checked');
		    if(canDo){
		    	$(id).val("true");
		    }else{
		    	$(id).val("false");
		    }
		}

		function validateJoMasterCommissionTotals(id){
			var rows = jQuery("#CommissionSplitsGrid").getDataIDs();
			var total = 0;
			var grandTotal = 0;
			var row = '';
				 for(a=0;a<rows.length;a++)
				 {
				    row=jQuery("#CommissionSplitsGrid").getRowData(rows[a]);
				    total=row['allocated'];
				    total = Number(total);
				    if(isNaN(total)){
				    	total=Number(1);
				    }
				    grandTotal = Number(grandTotal) + total; 
				 }
				 if(parseInt(grandTotal)<=100){
					return true;
				 }else{ 
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).attr("id","msgDilg");
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100'+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
							 //jQuery("#CommissionSplitsGrid").jqGrid('restoreRow',id);
							 jQuery("#CommissionSplitsGrid").jqGrid('setSelection',id, true);
							 $('#CommissionSplitsGrid_iledit').trigger('click');
							}}]}).dialog("open");
						return false;
				 }
		}
		//PoSplitCommissionGrid_ilcancel
		function validateJoMasterCommissionTotalsSavePoRelease(){
			var rows = jQuery("#CommissionSplitsGrid").getDataIDs();
			var total = 0;
			var grandTotal = 0;
			var row = '';
				 for(a=0;a<rows.length;a++)
				 {
				    row=jQuery("#CommissionSplitsGrid").getRowData(rows[a]);
				    total=row['allocated'];
				   // total+=parseInt(total);
				    grandTotal = Number(grandTotal) + Number(total); 
				 }
			var splitCommGridDatas = $('#CommissionSplitsGrid').getRowData();
			var splitCommGridDataLocal = JSON.stringify(splitCommGridDatas);
				 
			if(parseInt(grandTotal)<100 && rows.length!=0){
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).attr("id","msgDilg");
						jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should be 100 </b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
						return false;
				 }else{
					 return true;
				 }
		}
		
	</script>
	