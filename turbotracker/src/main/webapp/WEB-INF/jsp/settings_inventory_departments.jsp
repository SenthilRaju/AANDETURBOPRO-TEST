<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div style="background-color: #FAFAFA">
			<table style="width:798px;">
				<tr>
					<td >
						<table>
						    <tr>
						    	<td>
									<table id="inventoryDepartmentsGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					<td >
						<div id="inventoryDepartmentDetails">
							<fieldset class= " ui-widget-content ui-corner-all" style="margin-top:-55px;width:420px;">
								<legend><label><b>GL Accounts</b></label></legend>
								<form action="" id="departmentDetailsForm">
								<table>
									<tr>
										<td>
											<input type="text" id="invDepartmentId" name="invDepartmentId" style="display: none;">
										</td>
									</tr>
									<tr>
										<td><label class="overrideFont">Description: </label></td>
										<td><input type="text" id="departmentName" name="name" placeholder="Enter Description to add new Department" style="width: 80%;"></td>
										<td><label style="position: relative;left: -22%" class="overrideFont">In Active: </label><input type="checkbox" value='1' id="departmentInactive" name="departmentInactive" style="position:relative; left: -22%;top: 5px;"></td>
									</tr>
								<!-- 	<tr>
										<td colspan="2">
											<label>Override: </label><input type="checkbox" value='1' id="overrideAccounts" name="overrideAccounts" onclick="overrideAccounts()">
											<input type="text" id="overrideInput" name="overrideAccountsInput" style="" disabled="disabled">
										</td>
									</tr> -->
									<tr>
										<td><label class="overrideFont">Revenue: </label></td>
										<td  style="display:none"><label id="revenueLabel"></label></td>
										<td><input type="text" id="revenueAccount" name="revenue" placeholder="Enter minimum 2 character to search" style="width: 80%;"></td>
										<td></td>
										<td><input type="hidden" id="coAccountIdsales" name="coAccountIdsales"></td>
									</tr>
									<tr>
									<td style="display:none"><label id="cgsLabel" ></label></td>
										<td><label class="overrideFont">Cost of Goods Sold: </label></td>
										<td><input type="text" id="cgsAccount" name="cost"  placeholder="Enter minimum 2 character to search" style="width: 80%;"></td>
										<td></td>
										<td><input type="hidden" id="coAccountIdcogs" name="coAccountIdcogs"></td>
										
									</tr>
									
								</table>
								</form>
								
								<div id="inventorydeparterrordiv" style="color:red;"></div>
								<div id="inventorydepartdiv" style="color:green;"></div>
								<div>
									<input type="button" onclick="clearDepartmentDetails()" value="Clear" class="savehoverbutton turbo-blue">
									<input type="button" onclick="deleteDepartmentDetails()" value="Delete" class="savehoverbutton turbo-blue">
									<input type="button" onclick="SaveDepartmentDetails()" value="Save" class="savehoverbutton turbo-blue" style="margin-left: 180px;width: 80px;">
								</div>
							</fieldset>
						</div>
					</td>
				</tr>
			</table> 
		</div>
		
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/settings_inv_departments.js"></script>
		
