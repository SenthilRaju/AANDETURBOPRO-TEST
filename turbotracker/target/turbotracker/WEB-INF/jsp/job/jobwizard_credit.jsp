<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
	.custom_fieldset {
	    padding-bottom: 10px;
	}
</style>
<table>
    	<tr><td colspan="2"><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
    	<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<div>
		<form action="" id="jobCreditForm">
			<table>
				<tr><td style="vertical-align:top">
					<table>
			   			<tr><td>
			    			<fieldset class= " custom_fieldset"style="width:510px">
				    			<legend class="custom_legend"><label><b>Credit</b></label></legend>
				           		<table>
				               		<tr>
				               			<td style="width:50px"><label>Status:</label></td>
				               			<td>
				               				<select style="width:150px" id="creditStatusListID" name="creditStatus" onchange="creditStatusChange()" >
				               					<option></option>
				               					<option value="0">Pending</option>
				               					<option value="1">Approved</option>
				               					<option value="2">Hold</option>
				               				</select>
				               			</td>
				               			<td class="creditStatusfields">
				                			<input type="text" class="datepicker" id="creditStatusDate" name="creditStatusDate" style="width: 100px">
				                		</td>
				               			<td class="creditStatusfields">
				               				<select id="CreditStatusBY" name="creditStatusChangedBy" style="width: 70px;" >
				               					<option></option>
				               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
													<option value="${userLoginCloneBean.userLoginId}">
														<c:out value="${userLoginCloneBean.initials}" ></c:out>
													 </option>
												</c:forEach>
				               				</select>
				               			</td>
				               		</tr>
				                	<tr>
				                		<td style="width:50px"><label>Type:</label></td>
				                		<td>
				                			<select style="width:150px" id="creditTypeListID" name="creditType" onchange="creditTypeChange()" >
				                				<option></option>
				                				<option value="0">Pending</option>
				                				<option value="1">Check Received</option>
				                				<option value="2">Credit Card</option>
				                				<option value="3">Joint Check</option>
				                				<option value="4">Open Account</option>
				                				<option value="5">Customer Terms</option>
				                			</select>
				                		</td>
				                		<td class="creditTypefields">
				                			<input type="text" class="datepicker" id="creditTypeDate" name="creditTypeDate" style="width: 100px">
				                		</td>
				               			<td class="creditTypefields">
				               				<select id="CreditTypeChangedBy" name="creditTypeChangedBy" style="width: 70px;">
				               					<option></option>
				               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
													<option value="${userLoginCloneBean.userLoginId}">
														<c:out value="${userLoginCloneBean.initials}" ></c:out>
													 </option>
												</c:forEach>
				               				</select>
				               			</td>
				                	</tr>
				          		</table>
			          		</fieldset>
			          	</td></tr>
			          	<tr><td>
			          		<fieldset class= "custom_fieldset"style="width:510px;"> 
							<legend class="custom_legend"><label><b>Owner</b></label></legend>
			            	<table>
			            		<tr>
			       					<td><label>Name:</label></td>
			       					<td>
			       						<input type="text" id="ownerNameID" value="${requestScope.ownerAddress.name}" name="ownerName" style="width: 330px">
			       						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long"> 
			       					</td>
			       					<td><input type="hidden" id="ownerHiddenNameID" name = "ownerhiddenName" style="width: 50px;"></td>
			       				</tr>
			             		<tr>
				 					<td style="width: 90px"><label>Address: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			 						<td><input type="text" id="ownerAddressID1" name="ownerAddress1" class="validate[maxSize[100]" style="width: 330px;" value="${requestScope.ownerAddress.address1}"></td>
		 						</tr>
		 						<tr>
		 							<td style="width: 90px"><label></label></td>
									<td><input type="text" id="ownerAddressID2" name="ownerAddress2" class="validate[maxSize[40]" style="width: 330px;" value="${requestScope.ownerAddress.address2}"></td>
								</tr>
	 							<tr>
	 								<td  style="width: 90px"><label>City-State: </label></td>
	 								<td>
	 									<input type="text" id="ownerCityID" name="ownerCity" style="width: 127px;" value="${requestScope.ownerAddress.city}">
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">&nbsp;<label> - </label>
	 									<input type="text" id="ownerStateID" name="ownerState" style="width: 30px;" value="${requestScope.ownerAddress.state}" maxlength="2">
	 									<label>Zip: </label><input type="text" id="ownerZipID" name="ownerZip" style="width: 75px;" value="${requestScope.ownerAddress.zip}">
	 								</td>
	 							</tr>
			                	<tr>
			                		<td style="width:50px"><label>Contact:</label></td>
			                		<td>
			                			<select style="width:150px" id="ownerContactsList" name="ownerContact" onchange="getOwnerID()">
			                				<option value="0"></option>
			                				<c:forEach var="rxContactBean" items="${requestScope.ownerContacts}">
												<option value="${rxContactBean.rxContactId}">
													<c:out value="${rxContactBean.firstName}" ></c:out> <c:out value=" ${rxContactBean.lastName}" ></c:out>
												 </option>
											</c:forEach>
											<option value="-1">(Add new)</option>
			                			</select>
			                			<td><input type="hidden" id="ownerSelectID" name="ownerSelectName" style="width: 50px;">
			                		</td>
			                	</tr>
			             	</table>
			        		</fieldset>
			        	</td></tr>
						<tr><td>
			       			<fieldset class= "custom_fieldset"style="width:510px">
							<legend class="custom_legend"><label><b>Notices</b></label></legend>
			            	<table style="width:385px">
			               		<tr>
			               			<td style="vertical-align:middle;width:120px"><label>Requested NOC?</label></td>
			               			<td>
			               				<c:set var="sel" value=""/>
										<c:if test="${requestScope.joMasterDetails.requestedNoc}">
		    								<c:set var="sel" value="checked"/>
										</c:if>
			               				<input type="checkbox" style="vertical-align:middle;" name="isRequestedNOC" id="RequestedNoc_Check" value="1" onclick="RequestedNoc()" <c:out value="${sel}"/> ></td>
			               			<td class="RequestedNOC">
			               				<input type="text" class="datepicker" id="credit_requestedNoc" style="width: 100px" value="" name="requestedNOCDate">
			               			</td>
			               			<td class="RequestedNOC">
			               				<select id="Credit_requestedNocBY" style="width: 70px;" name="requestedNOCBy">
			               					<option></option>
			               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
												<option value="${userLoginCloneBean.userLoginId}">
													<c:out value="${userLoginCloneBean.initials}" ></c:out>
												 </option>
											</c:forEach>
			               				</select>
			               			</td>
			                	</tr>
			                	<tr>
			                		<td style="vertical-align:middle;width:120px"><label>Received NOC?</label></td>
			                		<td>
			                			<c:set var="sel" value=""/>
										<c:if test="${requestScope.joMasterDetails.receivedNoc}">
		    								<c:set var="sel" value="checked"/>
										</c:if>
			                			<input type="checkbox" style="vertical-align:middle;" name="isReceivedNOC" id="ReceivedNoc_check" onclick="ReceivedNoc()" <c:out value="${sel}"/> ></td>
			                		<td class="ReceivedNOC">
			                			<input type="text" class="datepicker" id="credit_ReceivedNoc" name="receivedNOCDate" style="width: 100px">
			                		</td>
			               			<td class="ReceivedNOC">
			               				<select id="Credit_ReceivedNocBY" style="width: 70px;" name="receivedNOCBy">
			               					<option></option>
			               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
												<option value="${userLoginCloneBean.userLoginId}">
													<c:out value="${userLoginCloneBean.initials}" ></c:out>
												 </option>
											</c:forEach>
			               				</select>
			               			</td>
			                	</tr>
			               		<tr>
			               			<td style="vertical-align:middle;width:120px"><label>NTC Sent GC?</label></td>
			               			<td>
			               				<c:set var="sel" value=""/>
										<c:if test="${requestScope.joMasterDetails.sentNtc}">
		    								<c:set var="sel" value="checked"/>
										</c:if>
			               				<input type="checkbox" style="vertical-align:middle;" name="isNTCSentGC" id="NTCSentGC_Check" onclick="NTCSentGC()" <c:out value="${sel}"/> ></td>
			               			<td class="NTCSentGC">
			               				<input type="text" class="datepicker" name="NTCSentGCDate" id="credit_NTCSentGC" style="width: 100px">
			               			</td>
			               			<td class="NTCSentGC">
			               				<select id="Credit_NTCSentGCBY" name="NTCSentGCBy" style="width: 70px;">
			               					<option></option>
			               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
												<option value="${userLoginCloneBean.userLoginId}">
													<c:out value="${userLoginCloneBean.initials}" ></c:out>
												 </option>
											</c:forEach>
			               				</select>
			               			</td>
			               		</tr>
			                	<tr>
			                		<td style="vertical-align:middle;width:120px"><label>Reference #:</label></td>
			                		<td colspan="2"><input type="text" style="width:40%" name="creditReferenceNumber" value="${requestScope.joMasterDetails.creditReferenceNumber}"></td>
			                	</tr>
			            	</table>
			          		</fieldset>
			           </td></tr>
			         </table>
				</td>
				<td width="20px;"></td>
				<td style="vertical-align:top" >
				    <table>
						<tr><td>
						<fieldset class= "custom_fieldset" style="width:510px">
						<legend class="custom_legend"><label id="creditGCId"><b>GC:</b></label></legend>
						<table>
							<tr>
			       				<td><label>Contact:</label></td>
			       				<td>
			       					<select style="width:150px" name="GeneralContractorContact" id="gcContactsList">
			       						<option value="0"></option>
		       							<c:forEach var="rxContactBean" items="${requestScope.gcContacts}">
											<option value="${rxContactBean.rxContactId}">
												<c:out value="${rxContactBean.firstName}" ></c:out> <c:out value=" ${rxContactBean.lastName}" ></c:out>
											</option>
										</c:forEach>
										<option value="-1">(Add new)</option>
			       					</select>
			       				</td>
			       			</tr>
			       		</table>
			       		</fieldset>
			       		</td></tr>
			       		<tr><td>
			       		<fieldset class= "custom_fieldset" style="width:510px;height: 200px;">
			       	 	<legend class="custom_legend"><label><b>Bond Agent</b></label></legend>
			       		<table><tr><td></td></tr>
			       			<tr><td style="vertical-align:middle;"><label>Bonded Job?</label>
			       				<c:set var="sel" value=""></c:set>
								<c:if test="${requestScope.joMasterDetails.jobBonded}">
							    	<c:set var="sel" value="checked"></c:set>
								</c:if>
			       			<input type="checkbox" id="bond" name="isBondedJob" value="1" <c:out value="${sel}"/> onchange="bondAgentChange()" style="vertical-align:middle;"></td>
			       			</tr><tr><td>
			       			<div id="isBondedJob">
			       				<table>
			       					<tr>
			       						<td><label>Name:</label></td>
			       						<td>
			       							<input type="text" id="BondAgentNameID" name="bondAgentName" value="${requestScope.bondAgentAddress.name}" style="width: 330px">
			       							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
			       						</td>
			       						<td><input type="hidden" id="bondHiddenNameID" name = "bondhiddenName" style="width: 50px;"></td>
			       					</tr>
				             		<tr>
					 					<td style="width: 90px"><label>Address: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
				 						<td><input type="text" id="bondAddressID1" name="bondAddress1" class="validate[maxSize[100]" style="width: 330px;" value="${requestScope.bondAgentAddress.address1}"></td>
			 						</tr>
			 						<tr>
			 							<td style="width: 90px"><label></label></td>
										<td><input type="text" id="bondAddressID2" name="bondAddress2" class="validate[maxSize[40]" style="width: 330px;" value="${requestScope.bondAgentAddress.address2}"></td>
									</tr>
		 							<tr>
		 								<td  style="width: 90px"><label>City-State: </label></td>
		 								<td>
		 									<input type="text" id="bondCityID" name="bondCity" style="width: 130px;" value="${requestScope.bondAgentAddress.city}">
											<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">&nbsp;<label> - </label>
		 									<input type="text" id="bondStateID" name="bondState" style="width: 25px;" value="${requestScope.bondAgentAddress.state}" maxlength="2">
		 									<label>Zip: </label><input type="text" id="bondZipID" name="bondZip" style="width: 84px;" value="${requestScope.bondAgentAddress.zip}">
		 								</td>
		 							</tr>
			       					<tr>
			       						<td><label>Contact:</label></td>
			       						<td>
			       							<select style='width:150px;' name="bondAgentContact" id="condagentContactList" onchange="getContactId()">
			       								<option value="0"></option>
			       								<c:forEach var="rxContactBean" items="${requestScope.bondAgentContacts}">
													<option value="${rxContactBean.rxContactId}">
														<c:out value="${rxContactBean.firstName}" ></c:out> <c:out value=" ${rxContactBean.lastName}" ></c:out>
													 </option>
												</c:forEach>
												<option value="-1"> (Add new) </option>
			       							</select>
			       						</td>
			       						<td><input type="hidden" id="bondAgentID" name="bondAgentName" style="width: 50px;">
			       					</tr>
			       				</table>
			       			</div>
			       			</td></tr>
			       		</table>
			       		</fieldset>
			       		</td></tr>
			       		<tr>
			       			<td>
			       				<fieldset class= " custom_fieldset"style="width:510px">
					       			<legend class="custom_legend"><label><b>Waiver</b></label></legend>
					            	<table style="width:460px">
					               		<tr>
					               			<td style="vertical-align:middle;width:170px"><label>Lein Waiver signed?</label></td>
					               			<td>
					               				<c:set var="sel" value=""/>
												<c:if test="${requestScope.joMasterDetails.lienWaverSigned}">
				    								<c:set var="sel" value="checked"/>
												</c:if>
					               				<input type="checkbox" style="vertical-align:middle;" value="1" name="isLienWaverSigned" id="lienWaverSigned" onclick="lienWaverSignedCheck()" <c:out value="${sel}"/> ></td>
					               			<td class="lienWaverSigned">
					               				<input type="text" class="datepicker" name="lienWaverSignedDate" id="lienWaverSignedDate" style="width: 100px" value="">
					               			</td>
					               			<td class="lienWaverSigned">
					               				<select id="lienWaverSignedByList" name="lienWaverSignedBy" style="width: 70px;">
					               					<option></option>
					               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
														<option value="${userLoginCloneBean.userLoginId}">
															<c:out value="${userLoginCloneBean.initials}" ></c:out>
														 </option>
													</c:forEach>
					               				</select>
					               			</td>
					                	</tr>
					                	<tr>
					                		<td style="vertical-align:middle;width:170px"><label>Lein Waiver Through: </label></td>
					                		<td>
					                			<c:set var="sel" value=""/>
												<c:if test="${requestScope.joMasterDetails.lienWaverThrough}">
				    								<c:set var="sel" value="checked"/>
												</c:if>
					                			<input type="checkbox" style="vertical-align:middle;" value="1" name="isLienWaverThrough" id="lienWaverThrough" onclick="lienWaverThroughCheck()" <c:out value="${sel}"/> ></td>
					                		<td class="lienWaverThrough">
					                			<input type="text" class="datepicker" name="lienWaverThroughDate" id="lienWaverThroughDate" style="width: 100px">
					                		</td>
					               			<td class="lienWaverThrough">
					               				<select id="lienWaverThroughByList" style="width: 70px;" name="lienWaverThroughBy">
					               					<option></option>
					               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
														<option value="${userLoginCloneBean.userLoginId}">
															<c:out value="${userLoginCloneBean.initials}" ></c:out>
														 </option>
													</c:forEach>
					               				</select>
					               			</td>
					                	</tr>
					                	<tr>
					                		<td style="vertical-align:middle;width:170px"><label>Based on Recipt of:</label></td>
					                		<td colspan="2"><input type="text" style="width:140px" id="lienWaverThroughAmountID" name="lienWaverThroughAmountName" value="${requestScope.joMasterDetails.lienWaverThroughAmount}"></td>
					                		<td style="width: 170px;">
					                			<label>Final Waiver?</label>
					                			<c:set var="sel" value=""></c:set>
													<c:if test="${requestScope.joMasterDetails.lienWaverThroughFinal}">
												    	<c:set var="sel" value="checked"></c:set>
													</c:if>
					                			<input type="checkbox" id="isFinalWaiver" name="isFinalWaiver" <c:out value="${sel}"/> > 
					                		</td>
					                	</tr>
					            	</table>
				          		</fieldset>
			       			</td>
			       		</tr>
			       		<tr><td>	 
			       		  <fieldset class= " custom_fieldset"style="width:510px; height:55px">
			       		  <legend class="custom_legend"><label><b>Claim Filed</b></label></legend>
			       		  <table>
			       		     <tr>
			       		     	<td>
			       		     		<select id="claimFiledList" name="claimFiled" onchange="claimFiledListChange()">
			       		     			<option value="0">None</option>
			       		     			<option value="1">Bond</option>
			       		     			<option value="2">Lien</option>
			       		     		</select>
			       		     	</td>
			       		     	<td class="claimFiledDateField"><input type="text" class="datepicker" name="claimFiledDate" value="" id="claimFiledDate"> </td>
			       		     	<td class="claimFiledfeildsSelect">
			       		     		<select id="ClaimFiledByID" name="claimFiledBu">
			       		     			<option></option>
		               					<c:forEach var="userLoginCloneBean" items="${requestScope.creditByUserList}">
											<option value="${userLoginCloneBean.userLoginId}">
												<c:out value="${userLoginCloneBean.initials}" ></c:out>
											</option>
										</c:forEach>
			       		     		</select>
								</td>
							</tr>
							</table>
			       		  </fieldset>
			       		</td></tr>
			       	</table>
			    </td></tr>
			 </table>
			 <table>
			    <tr>
					<td>
			           <fieldset class= "custom_fieldset"style="width:1000px">
			           <legend class="custom_legend"><label><b>Notes</b></label></legend>
			            <table>
			                  <tr><td><textarea rows="3" id="creditNotesID" name="creditNotes" cols="120" >${requestScope.joMasterDetails.creditNotes}</textarea> 
			       		</table>
			       		</fieldset>
					</td>
		    	</tr>
			</table>
		</form>
	</div>
	<br>
	<table style="width:900px">
	 	<tr><td><hr style="width:1100px; color:#C2A472;"></td></tr>
		<tr><td align="right">
				<input type="button" value="Save" class="savehoverbutton turbo-tan" onclick="credit_save()" style="width:80px">
			</td>
		</tr> 
	</table>
	<script type="text/javascript">
	var preStatusValue;
	var pretypeValue;
		$(".datepicker").datepicker();
		jQuery(document).ready(function() {
			parseRequestModelMap();
			$(".RequestedNOC").hide();
			$(".ReceivedNOC").hide();
			$(".NTCSentGC").hide();
			RequestedNoc();
			ReceivedNoc();
			NTCSentGC();
			bondAgentChange();
			lienWaverSignedCheck();
			lienWaverThroughCheck();
			claimFiledListChange();
			var jobStatus = getUrlVars()["jobStatus"];
			if(jobStatus === "Booked"){
				$(".customerNameField").attr("disabled", true);
			}
		    creditStatusChange1(); 
			creditTypeChange1();
			$("#OriJobNumber").css("display", "none");
			var aQuoteNumber = "${requestScope.joMasterDetails.quoteNumber}";
			if(aQuoteNumber !== ''){
				$("#OriJobNumber").show();
				//$("#yourdropdownid option:selected").text();
			}
			preStatusValue=   $("#creditStatusListID").val();
			pretypeValue=   $("#creditTypeListID").val();
			 
			
		});
		//var lastSel = $("#creditStatusListID option:selected");
		
			
		function parseRequestModelMap() {
			var aRequestedNOCDate = customFormatDate("${requestScope.joMasterDetails.requestedNocdate}");
			var aReceivedNOCDate = customFormatDate("${requestScope.joMasterDetails.receivedNocdate}");
			var aNtcSentGCDate = customFormatDate("${requestScope.joMasterDetails.sentNtcdate}");
			$("#credit_requestedNoc").val(aRequestedNOCDate);
			$("#credit_ReceivedNoc").val(aReceivedNOCDate);
			$("#credit_NTCSentGC").val(aNtcSentGCDate);

			var bondAgentAddress1 = "${requestScope.bondAgentAddress.address1}";
			var bondAgentAddress2 = "${requestScope.bondAgentAddress.address2}";
			var bondAgentCity = "${requestScope.bondAgentAddress.city}";
			var bondAgentState = "${requestScope.bondAgentAddress.state}";
			var bondAgentZip = "${requestScope.bondAgentAddress.zip}";

			var bondAgentAddr = "";

			if (bondAgentAddress1 !== "") { bondAgentAddr = bondAgentAddr + bondAgentAddress1; }
			if (bondAgentAddress2 !=="") { bondAgentAddr = bondAgentAddr + ", " + bondAgentCity; }
			if (bondAgentState !== "") { bondAgentAddr = bondAgentAddr + ", " + bondAgentState; }
			if (bondAgentZip !== "") { bondAgentAddr = bondAgentAddr + ", " + bondAgentZip; }

			$("#bondAgentAddress").val(bondAgentAddr);

			var ownerAddress1 = "${requestScope.ownerAddress.address1}";
			var ownerAddress2 = "${requestScope.ownerAddress.address2}";
			var ownerAddressCity = "${requestScope.ownerAddress.city}";
			var ownerAddressState = "${requestScope.ownerAddress.state}";
			var ownerAddressZip = "${requestScope.ownerAddress.zip}";

			var ownerAddr = "";

			if (ownerAddress1 !== "") { ownerAddr = ownerAddr + ownerAddress1; }
			if (ownerAddress2 !== "") { ownerAddr = ownerAddr + ", " + ownerAddress2; }
			if (ownerAddressCity !== "") { ownerAddr = ownerAddr + ", " + ownerAddressCity; }
			if (ownerAddressState !== "") { ownerAddr = ownerAddr + ", " + ownerAddressState; }
			if (ownerAddressZip !== "") { ownerAddr = ownerAddr + ", " + ownerAddressZip; }

			$("#ownerAddressID").val(ownerAddr);
			
			var creditStatus = "${requestScope.joMasterDetails.creditStatus}";
			var creditType = "${requestScope.joMasterDetails.creditType}";
			var GC_Contact = "${requestScope.joMasterDetails.creditContact0}";
			var bondAgentContact = "${requestScope.joMasterDetails.creditContact2}";
			var ownerContact = "${requestScope.joMasterDetails.creditContact1}";
			
			$("#creditStatusListID option[value=" + creditStatus + "]").attr("selected", true);
			$("#creditTypeListID option[value=" + creditType + "]").attr("selected", true);
			$("#condagentContactList option[value=" + bondAgentContact + "]").attr("selected", true);
			$("#gcContactsList option[value=" + GC_Contact + "]").attr("selected", true);
			$("#ownerContactsList option[value=" + ownerContact + "]").attr("selected", true);
			

			var waverSighnedDate = customFormatDate("${requestScope.joMasterDetails.lienWaverSignedDate}");
			var lienWaverThroughDate = customFormatDate("${requestScope.joMasterDetails.lienWaverSignedDate}");
			$("#lienWaverSignedDate").val(waverSighnedDate);
			$("#lienWaverThroughDate").val(lienWaverThroughDate);

			var lienWaverSignedBy = "${requestScope.joMasterDetails.who5}";
			var lienWaverThroughBy = "${requestScope.joMasterDetails.who6}";
			
			$("#lienWaverSignedByList option[value=" + lienWaverSignedBy + "]").attr("selected", true);
			$("#lienWaverThroughByList option[value=" + lienWaverThroughBy + "]").attr("selected", true);

			
			var GC = "${requestScope.generalContractor}";
			if(GC === '') {
				$("#creditGCId").append("<label> (Not Set)</lable>");
			} else {
				$("#creditGCId").append("<label> " + GC + "</lable>");
			}
			return true;
		}
		
		function credit_save() {
			var creditFormData = $("#jobCreditForm").serialize();
			var creditFormDataWithoutEmpties = creditFormData.replace(/[^&]+=\.?(?:&|$)/g, '');
			var jobNumber = $(".jobHeader_JobNumber").val();
			var creditDataForm = creditFormData +"&jobNumber="+jobNumber;
			$.ajax({
				url: "./jobtabs4/SaveCreditDetailInfo",
				type: "POST",
				async:false,
				data : creditDataForm,
				success: function(data) {
					var jobNumber = data.jobNumber;
					var jobName = $("#jobName_ID").text();
					var jobStatus = getUrlVars()["jobStatus"];
					var aQryStr = "jobNumber=" + jobNumber + "&jobName=" + jobName+"&jobStatus=" + jobStatus+"&selectTab=credit";
					var errorText = "Credit Details Successfully Updated.";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); document.location.href = "./jobflow?token=view&" + aQryStr; }}]}).dialog("open");
					return false;
				}
			});
		}

		function selectTab(){
			$("#tabs_main_job").tabs("select", 3);
			return true;
		}
		
		function RequestedNoc() {
			//
			var requestedNocBY = "${requestScope.joMasterDetails.who2}";
			$("#Credit_requestedNocBY option[value=" + requestedNocBY + "]").attr("selected", true);
			
			if ($('#RequestedNoc_Check').is(':checked')) {
				$(".RequestedNOC").show();
		 	} else {
				$(".RequestedNOC").hide();
		 	}
		 	return true;
	 	}

		function ReceivedNoc() {
			var receivedNocBY = "${requestScope.joMasterDetails.who3}";
			$("#Credit_ReceivedNocBY option[value=" + receivedNocBY + "]").attr("selected", true);
			
			if ($('#ReceivedNoc_check').is(':checked')) {
				$(".ReceivedNOC").show();
		 	} else {
				$(".ReceivedNOC").hide();
		 	}
	 	}

		function NTCSentGC() {
			var aNTCSentGCBY = "${requestScope.joMasterDetails.who4}";
			$("#Credit_NTCSentGCBY option[value=" + aNTCSentGCBY + "]").attr("selected", true);
			
			if ($('#NTCSentGC_Check').is(':checked')) {
				$(".NTCSentGC").show();
		 	} else {
				$(".NTCSentGC").hide();
		 	}
	 	}
	 	
		function bondAgentChange() {
			var addbonded="";	               
			if(document.getElementById('bond').checked)	{
				$('#isBondedJob').show();
			} else {
				$('#isBondedJob').hide();
			}
			return true;
		}
		

		function lienWaverSignedCheck() {
			if ($('#lienWaverSigned').is(':checked')) { $(".lienWaverSigned").show();}
			else { $(".lienWaverSigned").hide();}
		}

		function lienWaverThroughCheck() {
			
			if ($('#lienWaverThrough').is(':checked')) { $(".lienWaverThrough").show();}
			else { $(".lienWaverThrough").hide();}
		}

		function creditStatusChange1() {
			var creditStatusDate = "${requestScope.joMasterDetails.creditStatusDate}";
			$("#creditStatusDate").val(customFormatDate(creditStatusDate));

			var creditStatusBY = "${requestScope.joMasterDetails.who0}";
			$("#CreditStatusBY option[value=" + creditStatusBY + "]").attr("selected", true);
			
			if($("#creditStatusListID").val() === "0" || $("#creditStatusListID").val() === "") {
				$(".creditStatusfields").hide();
			} else {
				$(".creditStatusfields").show();
			}
		}

			function creditStatusChange() {
				var isAdmin="${sessionScope.user.systemAdministrator}";
				if (isAdmin ==1){
				var creditStatusDate = "${requestScope.joMasterDetails.creditStatusDate}";
				$("#creditStatusDate").val(customFormatDate(creditStatusDate));

				var creditStatusBY = "${requestScope.joMasterDetails.who0}";
				$("#CreditStatusBY option[value=" + creditStatusBY + "]").attr("selected", true);
				
				if($("#creditStatusListID").val() === "0" || $("#creditStatusListID").val() === "") {
					$(".creditStatusfields").hide();
				} else {
					$(".creditStatusfields").show();
				}
				}
				else{
					$("#creditStatusListID").val(preStatusValue);
				    errorText = "Admin users only can change the credit details";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+' </span>');
					jQuery(newDialogDiv).dialog({modal: true, width:350, height:145, title:"Warning",
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
					}
				
			}
			
		function creditTypeChange1() {
			var creditTypeDate = "${requestScope.joMasterDetails.creditTypeDate}";
			$("#creditTypeDate").val(customFormatDate(creditTypeDate));

			var creditTypeChangedBy = "${requestScope.joMasterDetails.who1}";
			$("#CreditTypeChangedBy option[value=" + creditTypeChangedBy + "]").attr("selected", true);
			
			if($("#creditTypeListID").val() === "0" || $("#creditTypeListID").val() === "") {
				$(".creditTypefields").hide();
			} else {
				$(".creditTypefields").show();
			}
		}
		function creditTypeChange() {
			var isAdmin="${sessionScope.user.systemAdministrator}";
			if (isAdmin ==1){
			var creditTypeDate = "${requestScope.joMasterDetails.creditTypeDate}";
			$("#creditTypeDate").val(customFormatDate(creditTypeDate));

			var creditTypeChangedBy = "${requestScope.joMasterDetails.who1}";
			$("#CreditTypeChangedBy option[value=" + creditTypeChangedBy + "]").attr("selected", true);
			
			if($("#creditTypeListID").val() === "0" || $("#creditTypeListID").val() === "") {
				$(".creditTypefields").hide();
			} else {
				$(".creditTypefields").show();
			}
			}
			else{
				$("#creditTypeListID").val(pretypeValue);
			    errorText = "Admin users only can change the credit details";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:350, height:145, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				}
		}
		
		
		
		function claimFiledListChange() {
			var claimFiledDate = "${requestScope.joMasterDetails.claimFiledDate}";
			$("#claimFiledDate").val(customFormatDate(claimFiledDate));

			var claimFiled = "${requestScope.joMasterDetails.claimFiled}";
			$("#claimFiledList option[value=" + claimFiled + "]").attr("selected", true);

			var claimFiledBy = "${requestScope.joMasterDetails.who7}";
			$("#ClaimFiledByID option[value=" + claimFiledBy + "]").attr("selected", true);
			
			if($("#claimFiledList").val() === "0") {
				$(".claimFiledfeilds").hide();
			} else {
				$(".claimFiledfeilds").show();
			}
		}

		$(function() { var cache = {}, lastXhr; $( "#ownerNameID" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
			{ 
				var id = ui.item.id; $("#ownerHiddenNameID").val(id); $.ajax({
					url: "./jobtabs4/filterOwnerList",
					mType: "GET",
					data : { 'ownerId' : id },
					success: function(data){
						var select = '<option value=0></option><option value=-1>(Add new)</option>';
						$.each(data, function(index, value){
							var quoteId = value.id;
							var quoteName = value.value;
							select +='<option value='+quoteId+'>'+quoteName+'</option>';
						});
						select += '</select>';
						$("#ownerContactsList").empty();
						$("#ownerContactsList").append(select);
						$.ajax({
							url: "./jobtabs4/rxAddress",
							mType: "GET",
							data : { 'rxAddressId' : id },
							success: function(data){
								var Address1 = " "; var Address2 = " "; var City = " "; var State = " "; var Zip = " ";
								$.each(data, function(index, value){
									Address1 = value.address1;
									Address2 = value.address2;
									City = value.city;
									State = value.state;
									Zip = value.zip;
								});
								$("#ownerAddressID1").val(Address1); $("#ownerAddressID2").val(Address2); $("#ownerCityID").val(City); $("#ownerStateID").val(State);
								$("#ownerZipID").val(Zip);
							}
						});
					}
				});
				getOwnerID = function(){$("#ownerSelectID").val($("#ownerContactsList").val());}; 
			},
			source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
			lastXhr = $.getJSON( "jobtabs4/ownerNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

		$(function() { var cache = {}, lastXhr; $( "#BondAgentNameID" ).autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
			{ 
				var id = ui.item.id; $("#bondHiddenNameID").val(id); $.ajax({
					url: "./jobtabs4/filterOwnerList",
					mType: "GET",
					data : { 'ownerId' : id },
					success: function(data){
						var select = '<option value=0></option><option value=-1>(Add new)</option>';
						$.each(data, function(index, value){
							var quoteId = value.id;
							var quoteName = value.value;
							select +='<option value='+quoteId+'>'+quoteName+'</option>';
						});
						select += '</select>';
						$("#condagentContactList").empty();
						$("#condagentContactList").append(select);
						$.ajax({
							url: "./jobtabs4/rxAddress",
							mType: "GET",
							data : { 'rxAddressId' : id },
							success: function(data){
								var Address1 = " "; var Address2 = " "; var City = " "; var State = " "; var Zip = " ";
								$.each(data, function(index, value){
									Address1 = value.address1;
									Address2 = value.address2;
									City = value.city;
									State = value.state;
									Zip = value.zip;
								});
								$("#bondAddressID1").val(Address1); $("#bondAddressID2").val(Address2); $("#bondCityID").val(City); $("#bondStateID").val(State);
								$("#bondZipID").val(Zip);
							}
						});
					}
				});
				getContactId = function(){$("#bondAgentID").val($("#condagentContactList").val());}; 
			},
			source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
			lastXhr = $.getJSON( "jobtabs4/bondNameList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} }); });
	</script>
	