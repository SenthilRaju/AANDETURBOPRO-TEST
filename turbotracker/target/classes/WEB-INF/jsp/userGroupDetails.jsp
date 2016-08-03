<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div>
	<form action="" id="groupDetailsFrom">
		<div>
			<table>
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 840px;height: 300px;">
							<legend><label><b>Group Name</b></label></legend>
							<table>
							<c:if test="${not empty fn:trim(requestScope.groupDefaults1.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink1.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="salesRepsName" id="salesRepsboxID" onchange="checkbox_change('salesRepsboxIDValue');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink1.userGroupLinkId)}">
											<input type="checkbox" name="salesRepsName" id="salesRepsboxID" onchange="checkbox_change('salesRepsboxIDValue');">
										</c:if>
										<input type="hidden" id="salesRepsboxIDValue" value="${requestScope.aUserGroupLink1.userGroupLinkId}#${requestScope.groupDefaults1.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;"><!-- Salesreps -->${requestScope.groupDefaults1.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults2.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink2.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="CSRsBoxName" id="CSRsBoxID" onchange="checkbox_change('CSRsBoxIDValue');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink2.userGroupLinkId)}">
											<input type="checkbox" name="CSRsBoxName" id="CSRsBoxID" onchange="checkbox_change('CSRsBoxIDValue');">
										</c:if>
										<input type="hidden" id="CSRsBoxIDValue" value="${requestScope.aUserGroupLink2.userGroupLinkId}#${requestScope.groupDefaults2.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;"><!-- CSRs -->${requestScope.groupDefaults2.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults3.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink3.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="employeesName" id="employeesID" onchange="checkbox_change('employeesIDValue');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink3.userGroupLinkId)}">
											<input type="checkbox" name="employeesName" id="employeesID" onchange="checkbox_change('employeesIDValue');">
										</c:if>
										<input type="hidden" id="employeesIDValue" value="${requestScope.aUserGroupLink3.userGroupLinkId}#${requestScope.groupDefaults3.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;"><!-- Employees -->${requestScope.groupDefaults3.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults4.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink4.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="administrativeName" id="administrativeID" onchange="checkbox_change('administrativeIDValue');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink4.userGroupLinkId)}">
											<input type="checkbox" name="administrativeName" id="administrativeID" onchange="checkbox_change('administrativeIDValue');">
										</c:if>
										
										<input type="hidden" id="administrativeIDValue" value="${requestScope.aUserGroupLink4.userGroupLinkId}#${requestScope.groupDefaults4.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;"><!-- Administrative -->${requestScope.groupDefaults4.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults5.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink5.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="wareHouseName" id="wareHouseID" onchange="checkbox_change('wareHouseIDValue');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink5.userGroupLinkId)}">
											<input type="checkbox" name="wareHouseName" id="wareHouseID" onchange="checkbox_change('wareHouseIDValue');">
										</c:if>
										<input type="hidden" id="wareHouseIDValue" value="${requestScope.aUserGroupLink5.userGroupLinkId}#${requestScope.groupDefaults5.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;"><!-- Warehouse -->${requestScope.groupDefaults5.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults6.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink6.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="" id="id6" onchange="checkbox_change('id6Value');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink6.userGroupLinkId)}">
											<input type="checkbox" name="" id="id6" onchange="checkbox_change('id6Value');">
										</c:if>
										<input type="hidden" id="id6Value" value="${requestScope.aUserGroupLink6.userGroupLinkId}#${requestScope.groupDefaults6.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;">${requestScope.groupDefaults6.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults7.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink7.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="" id="id7" onchange="checkbox_change('id7Value');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink7.userGroupLinkId)}">
											<input type="checkbox" name="" id="id7" onchange="checkbox_change('id7Value');">
										</c:if>
										<input type="hidden" id="id7Value" value="${requestScope.aUserGroupLink7.userGroupLinkId}#${requestScope.groupDefaults7.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;">${requestScope.groupDefaults7.groupName}</td>
								</tr>
								</c:if>
								<c:if test="${not empty fn:trim(requestScope.groupDefaults8.groupName)}">
								<tr>
									<td>
										<c:if test="${not empty fn:trim(requestScope.aUserGroupLink8.userGroupLinkId)}">
											<input type="checkbox" checked="checked" name="" id="id8" onchange="checkbox_change('id8Value');">
										</c:if>
										<c:if test="${empty fn:trim(requestScope.aUserGroupLink8.userGroupLinkId)}">
											<input type="checkbox" name="" id="id8" onchange="checkbox_change('id8Value');">
										</c:if>
										<input type="hidden" id="id8Value" value="${requestScope.aUserGroupLink8.userGroupLinkId}#${requestScope.groupDefaults8.userGroupId}"/>
									</td>
									<td style=" width : 70px;color: #00377A;">${requestScope.groupDefaults8.groupName}</td>
								</tr>
								</c:if>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
	<!-- 		<hr>
			<table align="right">
				<tr>
					<td><input type="button" id="saveUserButton" name="saveUserButtonName" class="savehoverbutton turbo-blue" value="Save"style="width: 80px;background: #44596B url(' ./../resources/styles/turbo-css/images/Save.png') no-repeat 6px 4px;" onclick="updateGroupID()"></td>
				</tr>
			</table> -->
		</div>
	</form>
</div>
<script type="text/javascript">
	jQuery(document).ready(function() {
		var aSalesReps = "${requestScope.SalesReps}";
		var aCSRs = "${requestScope.CSRs}";
		var aEmployees = "${requestScope.Employees}";
		var aAdministrative = "${requestScope.Adminstrative}";
		var aWarehouse = "${requestScope.Warehouse}";
		if(aSalesReps === 'true'){
			$("#salesRepsboxID").attr("checked", true);
		}if(aCSRs === 'true'){ 
			$("#CSRsBoxID").attr("checked", true);
		}if(aEmployees === 'true'){
			$("#employeesID").attr("checked", true);
		}if(aAdministrative === 'true'){
			$("#administrativeID").attr("checked", true);
		}if(aWarehouse === 'true'){
			$("#wareHouseID").attr("checked", true);
		}
	});

	function checkbox_change(groupID)
	{
		//alert("check box checked");
		var groupIdSplit = groupID.split("Value")[0];
		var deleteBool = false;
		if($("#"+groupIdSplit).attr("checked"))
        {
            
        }
        else
        	deleteBool = true;
		
		var strv = $("#"+groupID).val();
		//alert("check box checked "+strv);
		var userGroupLinkId = strv.split("#")[0];
		var idValue = strv.split("#")[1];
		var aUserloginID = getUrlVars()["userLoginId"];
		var aSalesReps = $("#salesRepsboxID").is(':checked');
		var aCSRs = $("#CSRsBoxID").is(':checked');
		var aEmployees = $("#employeesID").is(':checked');
		var aAdmin = $("#administrativeID").is(':checked');
		var aWarehouse = $("#wareHouseID").is(':checked');
		var groupUpdateDetails = "userLoginID="+aUserloginID+"&Admins="+aAdmin+"&Employees="+aEmployees+
														"&SalesReps="+aSalesReps+"&CSRs="+aCSRs+"&Warehouse="+aWarehouse+"&groupID="+idValue
														+"&deleteBool="+deleteBool+"&userGroupLinkId="+userGroupLinkId;
		
		
		//alert(groupID+" || "+idValue+ " || "+groupUpdateDetails);
		$.ajax({
			url: "./userlistcontroller/updateGroupLink",
			type: "POST",
			async:false,
			data : groupUpdateDetails,
			success: function(data){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Group details updated.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return true;
			}
	   });
	}
	
	/**  Update Group Details Function **/
	function updateGroupID(){
		var aInfo = true;
		if(aInfo){
			var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	/*	var aUserloginID = getUrlVars()["userLoginId"];
		var aSalesReps = $("#salesRepsboxID").is(':checked');
		var aCSRs = $("#CSRsBoxID").is(':checked');
		var aEmployees = $("#employeesID").is(':checked');
		var aAdmin = $("#administrativeID").is(':checked');
		var aWarehouse = $("#wareHouseID").is(':checked');
		var groupUpdateDetails = "userLoginID="+aUserloginID+"&Admins="+aAdmin+"&Employees="+aEmployees+
														"&SalesReps="+aSalesReps+"&CSRs="+aCSRs+"&Warehouse="+aWarehouse;
		$.ajax({
			url: "./userlistcontroller/updateGroupDetails",
			type: "POST",
			async:false,
			data : groupUpdateDetails,
			success: function(data){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Group details updated.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return true;
			}
	   });
		$(".requiredSave").show();
		return true; */
	} 
</script>
