<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
<div id="addUserDefaultsDialog" style="display: none;">
	<form action="" id="userDefaultFormId">
		<div id="userDefaultPage">
			<table>
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 355px;height: 100px;">
							<legend><label><b></b></label></legend>
							<table style="width: 350px;">
								<tr>
									<td><label>Warehouse: </label></td>
									<td align="left">
											<select id="whrhouseheaderID" name="warehouseName" style="width: 245px;">
													<option value="0"> -Select- </option>														
											</select>
									</td>
								</tr>
								<tr>			
									<td><label>Division:</label></td>
									<td>
											<select id="cODivisionID" name="divisionName" style="width: 245px;">
													<option value="0"> -Select- </option>														
											</select>
									</td>
								</tr>
								 <input type="hidden" name="loginuserID" id="loginuserID"/>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveUserDefault()" style=" width:105px;">
					<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 65px;" onclick="cancelUserDefault()"></td>
				</tr>
			</table>
		</div>
	</form>
</div>
						

</body>
</html>