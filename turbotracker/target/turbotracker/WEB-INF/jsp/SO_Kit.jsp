<div id="SOKit" style="width: 600px;">
<table>
<tr>
	<table>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
							<legend class="custom_legend"><label><b>Customer </b></label></legend>
								<label>Customer Name :</label> <input type="text" disabled="disabled" value="" id="CustomerNameKit" />
								</fieldset>
						</td>
						<td>
						<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
						<legend class="custom_legend"><label><b>SO Details</b></label></legend>
							<label>Date:</label><input type="text" id="dateOfSoKit" value="" style="width: 85px;margin-left: 5px;"/>
							<label>Number</label><input type="text" disabled="disabled" id="SO_numberKit" value="" style="width: 126px; margin-left: 5px;"/>
						</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	</tr>
	<tr>
		<td>
			<div align="Center" style="text-align: justify;margin-left: 195px;">
				<p>
				<b>
					This order does not have a build up kit associated with it.For an assembly kit for this order press create new button or select existing recipe to copy and optionally edit.
				</b>
				</p>
			</div>
		</td>
	</tr>
	<tr>
		<td>
			<fieldset class= "custom_fieldset" style="height: 55px;width: 729px;">
				<legend class="custom_legend"><label><b>Standard Recipes:</b></label></legend>
					<select style="width: 170px;" id="divisionID" name="divisionName">
							<option value="${coDivisionBean.coDivisionId}">
								</option>
					 </select>
					 <input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Create Kit from Recipe" onclick="savePORelease()" style="width:185px;position: relative;margin-left:16px; ">
					 <input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Create New Kit" onclick="savePORelease()" style="width:170px;position: relative;margin-left: 100px;">
					 </fieldset>
		</td>
	</tr>
	</table>
	<br>
	<jsp:include page="customer/customer_so_bottom.jsp"></jsp:include>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SO_Kit.js"></script>
