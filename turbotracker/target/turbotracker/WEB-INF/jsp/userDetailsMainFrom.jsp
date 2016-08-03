<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="userDetailsDiv">
	<form action="" id="userDetailsFormId">
		<div >
			<table align="center">
				<tr>
					<td colspan="1">
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 520px;height: 160px;">
							<legend><label><b>User</b></label></legend>
							<table style="width: 520px;">
								<tr>
									<td align="right"><label>Login Name:</label><span style="color:red;" class="mandatory">*&nbsp;</span></td>
									<td><input type="text" name="loginName" id="loginNameID" style="width:150px"  class="validate[required] validate[maxSize[15]" value="${requestScope.userDetails.loginName}" onkeyup="nospacesEditUser(this)"></td>
									<td align="right"><label>First Name:<span style="color:red;" class="mandatory">*&nbsp;</span></label></td>
									<td><input type="text" name="firstName" id="firstNameID" style="width:110px" class="validate[required] validate[maxSize[15]" onkeyup="nospacesEditUser(this)"></td>
								</tr>
								<tr>
									<td align="right"><label>Last Name:<span style="color:red;" class="mandatory">*&nbsp;</span></label></td>
									<td><input type="text" name="lastName" id="lastNameID" style="width:150px" class="validate[required] validate[maxSize[20]" onkeyup="nospacesEditUser(this)"></td>
									<td align="right"><label>Middle Name:&nbsp;</label></td>
									<td><input type="text" name="middleName" id="middleNameID" style="width:110px" class="validate[maxSize[15]" onkeyup="nospacesEditUser(this)"></td>
								</tr>
								<tr>
									<td align="right"><label>Initials:</label><span style="color:red;" class="mandatory">*&nbsp;</span></td>
									<td><input type="text" name="initialsName" class="validate[required] validate[maxSize[6]" id="initialsNameID" style="width:80px;text-transform: uppercase;" maxlength="6" value="${requestScope.userDetails.initials}" onkeyup="nospacesEditUser(this)"></td>
									<td align="right"><label style="vertical-align: middle;">Sys Admin</label><input type="checkbox" name="sysAdminNameBox" id="sysAdminIDBox" style="vertical-align: middle;"></td>
								</tr>
								<tr>
									<td align="right"><label>Password:</label><span style="color:red;" class="mandatory">*&nbsp;</span></td>
									<td><input type="password" name="passwordName" id="passwordNameID" style="width:150px" class="validate[required] validate[maxSize[15]" value="${requestScope.userDetails.loginPassword}"></td>
									<td align="right"><label style="vertical-align: middle;">Inactive</label><input type="checkbox" name="inactiveboxNameBox" id="inactiveboxIDBox" style="vertical-align: middle;"></td>
								</tr>
								<tr>
									<td align="right"><label>ZIP code: &nbsp;</label></td>
									<td><input type="text" name="zipcode" id="ZipcodeID" style="width:150px" class="" value="${requestScope.userDetails.userzipcode}"></td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 100px;height: 160px;">
							<legend><label><b>Views</b></label></legend>
							<table>
								<tr>
									<td><input type="checkbox" name="salesRepName" id="salesRepboxID"></td>
									<td style=" width : 70px;color: #00377A;">Salesman</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="CSRBoxName" id="CSRBoxID"></td>
									<td style=" width : 70px;color: #00377A;">CSR</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="salesMgrName" id="salesMgrID"></td>
									<td style=" width : 70px;color: #00377A;">Sales Mgr</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="EnginnerName" id="EnginnerNameID"></td>
									<td style=" width : 70px;color: #00377A;">Engineer</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="projectMgrName" id="projectMgrID"></td>
									<td style=" width : 70px;color: #00377A;">Pro Mgr</td>
									<td style="display: none;"><input type="text" id="userDiffLoginID"></td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td style="align:right;padding-left: 750px">
						<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" style="width: 135px;" onclick="updateUserDetails()">
					</td>
				</tr>
			</table>
			<table align="center">
				<tr>
					<td>
						<hr style="width:665px;">
					</td>
				</tr>
			</table>
			<table align="center">
				<tr>
					<td colspan="2">
						<div id="usertabsdetails">
						<div class="tabs_main_details" style="padding-left: 0px;width:720px;margin:0 auto; background-color: #FAFAFA;height: 480px;">
							<ul>
								<li id="userGroupDetailsDiv"><a href="userGroupDetails">Groups</a></li>
								<li id="userpermissionDetailsDiv"><a href="userPermissionDetails">Permissions</a></li>
								<li id="userEmailDetailsDiv"><a href="userEmailDetails">Email</a></li>
							<!-- 	<li id="userAdvancedOptionsDiv"><a href="userlistcontroller/userAdvancedOptions">Advanced Options</a></li>  -->
								<li style="float: right; padding-right: 10px; padding-top:3px;">
									<label id="userLoginID" style="display: none;"></label>
								</li>
							</ul>
						</div>
					</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<script type="text/javascript">
	jQuery(document).ready(function(){
		var aFullName = "${requestScope.userDetails.fullName}";
		var aSplitName = aFullName.split(" ");
		$("#firstNameID").val(aSplitName[0]);
		$("#middleNameID").val(aSplitName[1]);
		$("#lastNameID").val(aSplitName[2]);
		var aSysAdmin = "${requestScope.userDetails.systemAdministrator}";
		var aInActive = "${requestScope.userDetails.inactive}";
		var aSalesRep = "${requestScope.userDetails.employee0}";
		var aCSR = "${requestScope.userDetails.employee1}";
		var aSalesMgr = "${requestScope.userDetails.employee2}";
		var aEngineer = "${requestScope.userDetails.employee3}";
		var aProjectMgr = "${requestScope.userDetails.employee4}"; 
		if(aSysAdmin === '1'){
			$("#sysAdminIDBox").attr("checked", true);
		}
		if(aInActive === '1'){
			$("#inactiveboxIDBox").attr("checked", true);
		}
		if(aSalesRep === '1'){
			$("#salesRepboxID").attr("checked", true);
		}
		if(aCSR === '1'){
			$("#CSRBoxID").attr("checked", true);
		}
		if(aSalesMgr === '1'){
			$("#salesMgrID").attr("checked", true);
		}
		if(aEngineer === '1'){
			$("#EnginnerNameID").attr("checked", true);
		}
		if(aProjectMgr === '1'){
			$("#projectMgrID").attr("checked", true);
		}
		var aUserloginID = getUrlVars()["userLoginId"];
		$("#userLoginID").text(aUserloginID);
		$(".tabs_main_details").tabs({
			cache: true,
			ajaxOptions: {
				data: { 'userLogin'  : $("#userLoginID").text() },
				error: function(xhr, status, index, anchor) {
					$(anchor.hash).html("<div align='center' style='height:180px;padding-top: 160px;'>"+
							"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
							"</label></div>");
				}
			},
			load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
			select: function (e, ui) {
				var $panel = $(ui.panel);
				if ($panel.is(":empty")) {
					$panel.append("<div class='tab-loading' align='center' style='height:180px;padding-top: 140px;background-color: #FAFAFA'>"+
							"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
				}
			}
		});
		
		$.ajax({
			url:'./userlistcontroller/getUserDetails',
			type: "POST",
			data : { 'userLoginID' :  aUserloginID },
			success: function(data){
					$("#userDiffLoginID").val(data.systemAdministrator);
				}
			});
	});

	
	/**  Update User Details Function **/
	function updateUserDetails(){
		if(!$('#userDetailsFormId').validationEngine('validate')) {
			return false;
		}
		/** Get Check Box Values **/
		var aUpdateSysAdmin = $("#sysAdminIDBox").is(':checked');
		var aUpdateInActive = $("#inactiveboxIDBox").is(':checked');
		var aUpdateSalesRep = $("#salesRepboxID").is(':checked');
		var aUpdateCSR = $("#CSRBoxID").is(':checked');
		var aUpdateSalesMgr = $("#salesMgrID").is(':checked');
		var aUpdateEngineer = $("#EnginnerNameID").is(':checked');
		var aUpdateProjMgr = $("#projectMgrID").is(':checked');
		/** Get User Details using Serialize **/
		var updateUserDetails = $("#userDetailsFormId").serialize();
		var aUserloginID = getUrlVars()["userLoginId"];
		var aUserloginName = getUrlVars()["loginName"];
		var userUpdateDetails = updateUserDetails+"&userLoginID="+aUserloginID+"&updateSYSAdmin="+aUpdateSysAdmin+
													"&updateActive="+aUpdateInActive+"&updateSalesRep="+aUpdateSalesRep+"&updateCSR="+aUpdateCSR+
													"&updateSalesMgr="+aUpdateSalesMgr+"&updateEngineer="+aUpdateEngineer+"&updateProjectMgr="+aUpdateProjMgr; 
		$.ajax({
			url: "./userlistcontroller/updateUser",
			type: "POST",
			data : userUpdateDetails,
			success: function(data){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Green;">User details updated.</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");
										document.location.href = "./userlist"; 
										/*document.location.href = "./userDetails?userLoginId="+aUserloginID+"&loginName="+aUserloginName; */ }}]}).dialog("open");
					return true;
			}
	   });
		$(".requiredSave").show();
		return true;
	}

	function nospacesEditUser(t){
		if(t.value.match(/\s/g)){
			t.value=t.value.replace(/\s/g,'');
		}
	}
</script>