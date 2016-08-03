<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<div  style=" height: 435px;width: 700px;">
	<form id="testMailConfigure">
		<div>
			<table align="center" style="width:675px;">
				<tr>
					<td  colspan="2">
					<!-- <label style="color: green;font-size: 15px;">This feature is in our workshop.  Will be released soon.  Thank you for your patience</label> </td></tr>  -->
						  	<fieldset  class= " ui-widget-content ui-corner-all" style="width: 650px;">
								<legend><label><b>User Information</b></label></legend>
								<table>
									<tr><td colspan="2"><input type="checkbox" name="activeAdvancedEmailName" id="activeAdvancedEmailID"  style="vertical-align: middle;"><label style="vertical-align: middle;">Active Advanced Email</label></td>
									<tr>
										<td align="right"><label>Your Name:</label></td>
										<td><input type="text" name="emailName" id="emailNameID" style="width:120px"  class="validate[maxSize[64]" value="${requestScope.userDetails.emailName}"></td>
										<td width="5px;"></td>
										<td align="right"><label>Email Address:</label></td>
										<td><input type="text" name="emailAddressName" id="emailAddressID" style="width:200px" class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.emailAddr}"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td  colspan="2">
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 650px;">
								<legend><label><b>LogOn Information</b></label></legend>
								<table>
									<tr><td colspan="2"><input type="checkbox" name="logOnBox" id="logOnIDBox" style="vertical-align: middle;"><label style="vertical-align: middle;">LogOn using Secure Password Authentication</label></td></tr>
									<tr>
										<td align="right"><label>User Name: </label></td>
										<td><input type="text" name="userName" id="userNameID" style="width:200px"  class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.logOnName}"></td>
										<td width="5px;"></td>
										<td><label>Password: </label></td>
										<td><input type="password" name="passwordName" id="passwordNameID" style="width:150px" class="validate[maxSize[64	]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.logOnPswd}"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 280px;">
								<legend><label><b>Carbon Copy Email Address</b></label></legend>
								<table>
									<tr>
										<td><input type="text" name="CCEAAddress1" id="CCEAAddressID1" style="width:270px"  class="validate[maxSize[128]"  onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.ccaddr1}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCEAAddress2" id="CCEAAddressID2" style="width:270px" class=" validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.ccaddr2}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCEAAddress3" id="CCEAAddressID3" style="width:270px" class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.ccaddr3}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCEAAddress4" id="CCEAAddressID4" style="width:270px" class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.ccaddr4}"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 280px;">
								<legend><label><b>Carbon Copy Name</b></label></legend>
								<table>
									<tr>
										<td><input type="text" name="CCName1" id="CCNameID1" style="width:270px"  class="validate[maxSize[128]" value="${requestScope.userDetails.ccname1}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCName2" id="CCNameID2" style="width:270px" class="validate[maxSize[128]"value="${requestScope.userDetails.ccname2}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCName3" id="CCNameID3" style="width:270px" class="validate[maxSize[128]"value="${requestScope.userDetails.ccname3}"></td>
									</tr>
									<tr>
										<td><input type="text" name="CCName4" id="CCNameID4" style="width:270px" class="validate[maxSize[128]"value="${requestScope.userDetails.ccname4}"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td  colspan="2">	
							<fieldset class= " ui-widget-content ui-corner-all" style="width: 650px;">
								<table>
									<tr>
										<td align="right"><label>Blind Carbon Copy:</label></td>
										<td><input type="text" name="BCCName" id="BCCNameID" style="width:160px"  class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.bccaddr}"></td>
										<td width="5px;"></td>
										<td><label>SMTP Server:</label></td>
										<td><input type="text" name="smtpServerName" id="smtpServerNameID" style="width:120px" class="validate[maxSize[128]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.smtpsvr}"></td>
										<td><label>Port: </label></td>
										<td><input type="text" name="smtpPortNumber" id="smtpPortNumberID" style="width:30px" class="validate[maxSize[10]" onkeyup="nospacesEditUser(this)" value="${requestScope.userDetails.smtpport}"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
			</table>
		</div>
	</form>
	<hr>
	<table align="right" style="padding-right: 8px;">
		<tr>
			<td>
				<input type="button" id="testMailButtonID" name="testMailButtonName" style="width: 80px;" value="Save" class="cancelhoverbutton turbo-blue" onclick="saveAndTestMail()"> 
			</td>
		</tr>    
	</table></div></form>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var aActiveAdvancedEmailChk =  "${requestScope.userDetails.smtpemailActive}";
	var aUserAuthChk = "${requestScope.userDetails.useAuthentication}";
	if(aActiveAdvancedEmailChk === '1'){
		$("#activeAdvancedEmailID").attr("checked", true);
	}
	if(aUserAuthChk === '1'){
		$("#logOnIDBox").attr("checked", true);
	}
});

function saveAndTestMail(){
	var aUserloginID = getUrlVars()["userLoginId"];
	var aTestMailConfig = $("#testMailConfigure").serialize();
	var aTestEmailProperties = aTestMailConfig+"&userLoginID="+aUserloginID;
	$.ajax({
		url: "./userlistcontroller/saveAndTestMail",
		type: "POST",
		data : aTestEmailProperties,
		success: function(data){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color: green;">Email Properties Successfully Updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:350, height:120, title:"Information", 
				buttons:{
					"OK": function(){
						jQuery(this).dialog("close");
					}}}).dialog("open");
				return true;
		}
   });
	return true;
} 
</script>