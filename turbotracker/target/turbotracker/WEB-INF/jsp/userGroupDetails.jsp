<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<form action="" id="groupDetailsFrom">
		<div>
			<table>
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 570px;height: 300px;">
							<legend><label><b>Group Name</b></label></legend>
							<table>
								<tr>
									<td><input type="checkbox" name="salesRepsName" id="salesRepsboxID" ></td>
									<td style=" width : 70px;color: #00377A;">Salesreps</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="CSRsBoxName" id="CSRsBoxID"></td>
									<td style=" width : 70px;color: #00377A;">CSRs</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="employeesName" id="employeesID"></td>
									<td style=" width : 70px;color: #00377A;">Employees</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="administrativeName" id="administrativeID"></td>
									<td style=" width : 70px;color: #00377A;">Administrative</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="wareHouseName" id="wareHouseID"></td>
									<td style=" width : 70px;color: #00377A;">Warehouse</td>
								</tr>
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
