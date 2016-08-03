<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="submittalgeneral">
  <form action="" id="submittalForm">	
	<table>
		<tr>
			<td><div><jsp:include page="submittal_header.jsp"></jsp:include></div></td>
		</tr>
	</table>
	<hr width="820px">
	<table>
		<tr>
			<td>
				<fieldset class="custom_fieldset" style="width: 400px;">
					<table>
						<tr>
							<td><label>Architect:  &nbsp;&nbsp;&nbsp;</label><input type="text" id="architectsList" name="architectsList_name" style="width: 310px;" value=" " disabled="disabled"></td>
						</tr>
					</table>
					<table>
						<tr>
							<td><label>Engineer: &nbsp;&nbsp;&nbsp;</label><input type="text" id="engineersRXList" name="engineersRXList_name" style="width: 310px;" disabled="disabled" />
						</tr>
					</table>
					<table>
						<tr>
							<td><label>SalesRep: &nbsp;&nbsp;</label><input type="text" id="salesRep" name="salesRep_name" style="width: 310px;" disabled="disabled" /></td>
						</tr>
					</table>
				</fieldset>
			</td>
			<td>
				<fieldset class="custom_fieldset" style="width: 360px;height: 105px;">
					<table>
							<tr id="submittalbox" style="height: 25px;"><td>
								<label>Submittal By: &nbsp;&nbsp;&nbsp;</label>
								<input type="text" id="Submittal" name="Submittal_name" style="width: 80px;" title="Must be atleast 1 characters long" value="${requestScope.submittalName}"> 
								<img alt="search" id="searchimage" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long"></td>
							<td>
								<input type="hidden" id="submittalhidden" name="submittal_hiddenName" style="width: 15px;" value="${requestScope.joSubmittalDetails.submittalById}">
							</td>
						</tr>
					</table>
					<table>
						<tr id="signedbox" style="height: 25px;">
							<td>
								<label>Signed By: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<input type="text" id="Signed" name="signed_name" style="width: 80px;" title="Must be atleast 1 characters long" value="${requestScope.signedName}"> 
								<img alt="search" id="signedsearchimage" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long">
							</td>
							<td>
								<input type="hidden" id="signedhidden" name="signed_hiddenName" style="width: 15px;" value="${requestScope.joSubmittalDetails.signedById}"> 
							</td>
						</tr>
					</table>
					<table>
						<tr>
							<td><label>Revision:<b style="color: red;">* </b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
								<select id="revisionID" name="revision_name" style="width: 70px;margin-left: 2px;" onchange="revisionBoxOptions()">
									<option value="0">0</option>
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">+ new</option>
									<option value="5">Copy</option>
								</select>&nbsp;&nbsp;<label id="revisiontext" style="font-weight: bold; font-size: 15px;"></label></td>
						</tr>
						<tr>
							<td>
								<label id="revisionlable" style="color: #5E0707;">&nbsp;no submittal exists for job select Add New to begin</label>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<table>
					<tr>
						<td>
							<fieldset class="custom_fieldset" style="width: 255px;">
							 <table><tr><td>
								<table>
									<tr id="submittalDateBox" style="height: 25px;">
										<td><label>Submittal Date: &nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" class="datepicker" name="submittal_Date" id="submittalDate" value="" style="width: 120px"></td>
									</tr>
								</table>
								<table>
									<tr id="palndatebox" style="height: 25px;">
										<td>
											<input type="checkbox" id="plansDate" onclick="plansDateShow()"><br></td>
											<td><label>Planed Date:</label> 
											<input type="text" class="datepicker" name="date_PlanName" id="datePlan" style="width: 120px; margin-left: 2px;">
										</td>
									</tr>
								</table></td></tr></table></fieldset>
							</td>
							<td align="left">
								<fieldset class="custom_fieldset" style="width: 160px; height: 63px;">
								<table><tr><td>
								<td>
								<table>
									<tr id="copiesbox" style="height: 25px;">
										<td><label>Copies:</label><input type="text" id="copiestext" name="copiestext_name" value="${requestScope.joSubmittalDetails.copies}" style="width: 70px;margin-left: 9px;" />
									</tr>
								</table>
								<table>
									<tr>
										<td><label>Thru: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="text" id="thrutext" name="addendum_name" style="width: 50px; margin-left: 3px;" maxlength="1" value="${requestScope.joSubmittalDetails.addendum}"></td>
									</tr>
								</table></td></tr></table></fieldset>
							</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<fieldset class=" custom_fieldset" style="width: 782px; height: 85px;">
					<legend class="custom_legend">
						<label><b>Cover Sheet Remarks </b></label>
					</legend>
					<table>
						<tr id="remarksSheet">
							<td colspan="4"><textarea id="sheetremarks" name="remarkSheet" rows="3" cols="94">${requestScope.joSubmittalDetails.remarkNote}</textarea></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td>
				<fieldset class="custom_fieldset" style="width: 782px; height: 85px;">
					<legend class="custom_legend">
						<label><b>Internal comments </b></label>
					</legend>
					<table>
						<tr id="internalCommand">
							<td colspan="4"><textarea id="commentsinternal" name="internalComments" rows="3" cols="94">${requestScope.joSubmittalDetails.revisionNote}</textarea></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table></form>
</div>
<div style="display: none;">
	<table>
		<tr>
			<td><input type="text" id="planDate_ID" name="planDate_name" value="${requestScope.joSubmittalDetails.planDate}"></td>
			<td><input type="text" id="revision_ID" name="revision_name" value="${requestScope.joSubmittalDetails.revision}"></td>
			<td><input type="text" id="submittal_ID" name="submittal_name" value="${requestScope.joSubmittalDetails.submittalDate}"></td>
			<td><input type="text" id="submittalName_ID" name="submittalName_name" value="${sessionScope.user.initials}"></td>
		</tr>
	</table>
</div>
<br>
<table id="saveButton" align="center" style="width:750px">
	 <tr>
	 	<td></td><td align="right" style="padding-left: 625px;"><input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveSubmittal()" style="width:80px"></td>
	 	<td></td><td align="right"><input type="button" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="cancelSubmittal()" style="width:80px"></td>
	 </tr>
</table>
<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/submittal_General.js"></script>-->
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/submittal_General.min.js"></script>