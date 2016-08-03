<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="addNewUserDialog">
	<form action="" id="userFormId">
		<div id="userDetailsPage">
			<table>
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 520px;height: 180px;">
							<legend><label><b>User</b></label></legend>
							<table style="width: 520px;">
								<tr>
									<td align="right"><label>Login Name:</label><span style="color:red;" class="mandatory"> *&nbsp;</span></td>
									<td><input type="text" name="loginName" id="loginNameID" style="width:150px"  class="validate[required] validate[maxSize[15]" onkeyup="nospacesAddUser(this)"></td>
								</tr>
						 		<tr>
									<td align="right"><label>First Name:<span style="color:red;" class="mandatory"> *&nbsp;</span></label></td>
									<td><input type="text" name="firstName" id="firstNameID" style="width:110px" class="validate[required] validate[maxSize[15]" onkeyup="nospacesAddUser(this)"></td>
									<td align="right"><label>Middle Name:</label>
									<td><input type="text" name="middleName" id="middleNameID" style="width:110px" class="validate[maxSize[15]" onkeyup="nospacesAddUser(this)"></td>
								</tr>
								<tr>
									<td align="right"><label>Last Name:<span style="color:red;" class="mandatory"> *&nbsp;</span></label></td>
									<td><input type="text" name="lastName" id="lastNameID" style="width:150px" class="validate[required] validate[maxSize[20]" onkeyup="nospacesAddUser(this)"></td>
								</tr>
								<tr>
									<td align="right"><label>Initials:</label><span style="color:red;" class="mandatory"> *&nbsp;</span></td>
									<td><input type="text" name="initialsName" id="initialsNameID" style="width:80px;text-transform: uppercase;" maxlength="6" class="validate[required] validate[maxSize[6]" onkeyup="nospacesAddUser(this)"></td>
									<td align="right"><label style="vertical-align: middle;">Sys Admin</label><input type="checkbox" name="sysAdminName" id="sysAdminID" style="vertical-align: middle;"></td>
								</tr>
								<tr>
									<td align="right"><label>Password:</label><span style="color:red;" class="mandatory"> *&nbsp;</span></td>
									<td><input type="password" name="passwordName" id="passwordNameID" style="width:150px" class="validate[required] validate[maxSize[15]"></td>
									<td align="right"><label style="vertical-align: middle;">Inactive</label><input type="checkbox" name="inactiveboxName" id="inactiveboxID" style="vertical-align: middle;"></td>
								</tr>  	
							</table>
						</fieldset>
					</td>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 100px;height: 180px;">
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
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<br>
			<table align="right" style="width:745px">
				<tr>
					<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveNewUser()" style=" width:120px;">
					<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 80px;" onclick="cancelAddUser()"></td>
				</tr>
			</table>
		</div>
	</form>
</div>
<script type="text/javascript">
		/** Document Ready Function **/
		jQuery(document).ready(function(){

		});

		/**  Save User Details Function **/
		function saveNewUser(){
			if(!$('#userFormId').validationEngine('validate')) {
				return false;
			}
			var userDetails = $('#userFormId').serialize();
			var userName = $("#loginNameID").val();
			$.ajax({
				url: "./userlistcontroller/CheckUserName",
				mType: "GET",
				data : {'checkUserName' : userName},
				success: function(data){
					var	aExistingUser = data;
					if(aExistingUser === ''){
						$.ajax({
							url: "./userlistcontroller/addNewUser",
							type: "POST",
							async:false,
							data : userDetails,
							success: function(data){
								var newDialogDiv = jQuery(document.createElement('div'));
								jQuery(newDialogDiv).html('<span><b style="color: green;">Use this Login Name and Password Now?</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:350, height:120, title:"Redirect Login Page", 
									buttons:{
										"Yes": function(){
											document.location.href = "./login";
										},
										"No": function (){
											var aUserLoginID = "${requestScope.userSysAdmin}";
								    		if(aUserLoginID !== '1'){
								    			var aInfo = true;
								    			if(aInfo){
								    				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
								    				var newDialogDiv = jQuery(document.createElement('div'));
								    				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
								    				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
								    										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
								    				return true;
								    			}
									    	}else{
												var userLoginId=data.userLoginId;
												var userLoginName=data.loginName;
												document.location.href = "./userDetails?userLoginId="+userLoginId+"&loginName="+userLoginName; 
									    	}
										}}}).dialog("open");
									return true;
							}
					   });
					}else{
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color: red;">Login Name already exists.  Please provide different Login Name.</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning", 
							buttons:{
								"Ok": function(){ $(this).dialog("close"); }}}).dialog("open");
							return true;
					}
				}
			});
			return true;
		}

		/**  Cancel Dialog For User  **/
		function cancelAddUser(){
			$('#userFormId').validationEngine('hideAll');
			$("#addNewUserDialog").dialog("close");
			return true;
		}

		function nospacesAddUser(t){
			if(t.value.match(/\s/g)){
				t.value=t.value.replace(/\s/g,'');
			}
		}
</script>