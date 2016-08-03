<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="requestSubstitutionlineitem">
	<table><tr><td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Project</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobHeader_JobName" class="projectClass">${requestScope.joMasterDescription}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Location</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="JoblocationID" class="projectClass"></label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 106px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Job Number</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobHeader_JobNumber" class="projectClass">${sessionScope.jobGridObj.jobNumber}</label></td> 
				</tr>
			</table>
	</fieldset></td>
	</tr></table>
	<table><tr><td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Architect</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobMain_architectsList" class="projectClass">${requestScope.joMasterDetails.rxCategory1}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 310px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Engineer</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="jobMain_EngineersList" class="projectClass">${requestScope.joMasterDetails.cuAssignmentId3}</label></td>
				</tr>
			</table>
	</fieldset></td>
	<td>
	<fieldset class="custom_fieldset" style="width: 106px;height: 42px;">
		<legend class="custom_legend">
			<label><b>Bid Date</b></label>
		</legend>
			<table>
				<tr>
					<td><label id="bidDate_Date" class="projectClass"></label></td> 
				</tr>
			</table>
	</fieldset></td>
	</tr></table>
	<table>
		<tr>
			<td height="10px;"></td>
		</tr>
	</table>
	<fieldset class="custom_fieldset"
		style="width: 785px;">
		<legend class="custom_legend">
			<label><b>Items for Substitution</b></label>
		</legend>
		<table>
			<tr>
				<td>
					<div>
						<table>
							<tr>
								<td><input type="button" id="" value="Insert" onclick="" class="cancelhoverbutton"></td>
								<td><input type="button" id="" value="Add New Items" onclick="" class="cancelhoverbutton"></td>
							</tr>
						</table>
						<table><tr><td height="10px;"></td></tr></table>
						<table>
							<tr align="right">
								<td>
									<label>Specified Item: </label><input type="text" style="width: 480px;">
								</td>
							</tr>
							<tr height="5px;"></tr>
							<tr align="right">
								<td>
									<label>Specification Section: </label><input type="text" style="width: 73px;">
									<label>Page Number:</label><input type="text" style="width: 50px;">
									<label>Article/Paragraph:</label><input type="text"style="width: 110px;">
								</td>
							</tr>
							<tr height="5px;"></tr>
							<tr align="right">
								<td>
									<label>Proposed Substitution: </label><input type="text" style="width: 480px;">
								</td>
							</tr>
							<tr height="5px;"></tr>
							<tr align="right">
								<td>
									<label>Manufacturer: </label><input type="text" style="width: 480px;">
								</td>
							</tr>
							<tr height="5px;"></tr>
							<tr align="right">
								<td>
									<label>Deviation From Specified Item: </label><input type="text" style="width: 480px;">
								</td>
							</tr>
							<tr height="5px;"></tr>
							<tr align="right">
								<td>
									<label>Changes Necessary for Use: </label><input type="text" style="width: 480px;">
								</td>
							</tr>
							<tr height="10px;"></tr>
							<tr align="right">
								<td>
									<label>ReMarks</label><textarea rows="4" cols="90"></textarea>
								</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
    </table>
	</fieldset>
</div>