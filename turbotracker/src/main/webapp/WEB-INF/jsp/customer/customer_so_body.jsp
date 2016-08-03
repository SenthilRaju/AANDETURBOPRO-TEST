<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<table>
	<tr><td style="vertical-align:top">
	  <fieldset class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Bill To</b></label></legend>
				<table>
					<tr>
 						<td><input type="text" id="locationbillToAddressID" name="locationbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 					</tr>
	 				<tr>
	 					<td><input type="text" id="locationbillToAddressID1" name="locationbillToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
	 				</tr>
	 				<tr>
						<td><input type="text" id="locationbillToAddressID2" name="locationbillToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
					</tr>
 					<tr>
 						<td><input type="text" id="locationbillToCity" name="locationbillToCity" style="width: 100px;" disabled="disabled">
								<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
 								<input type="text" id="locationbillToState" name="locationbillToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled">
 								<label>Zip: </label><input type="text" id="locationbillToZipID" name="locationbillToZip" style="width: 75px;" disabled="disabled">
 						</td>
 					</tr>
 				</table>
		</fieldset>
		<fieldset  class= "custom_fieldset">
		<legend class="custom_legend"><label><b>Job #${requestScope.joMasterDetails.jobNumber}</b></label></legend>
			<table>
			<tr><td><input type="text" readonly="readonly" value="${requestScope.joMasterDetails.description}" style="width: 280px;"></td></tr>
			</table>
		</fieldset>
	</td>
	<td style="width:20px"></td>
	<td style="vertical-align:top">
		<fieldset class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Ship To</b></label>
			</legend>
			<table  id="shipTo">
				<tr>
 					<td><input type="text" id="locationShipToAddressID" name="locationShipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="locationShipToAddressID1" name="locationShipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="locationShipToAddressID2" name="locationShipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="locationShipToCity" name="locationShipToCity" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="locationShipToState" name="locationShipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="locationShipToZipID" name="locationShipToZip" style="width: 75px;" disabled="disabled">
								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer ">
							<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table  id="shipTo1">
				<tr>
 					<td><input type="text" id="locationShipToAddressID4" name="locationShipToAddressID4" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="locationShipToAddressID5" name="locationShipToAddress5" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="locationShipToAddressID3" name="locationShipToAddress3" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="locationShipToCity1" name="locationShipToCity1" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="locationShipToState1" name="locationShipToState1" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="locationShipToZipID1" name="locationShipToZip1" style="width: 75px;" disabled="disabled">
								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer  ">
							<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table>
					<tr align="left">
					<td style="vertical-align: bottom;padding-left: 50px;">
						<input type="button" id="usShipto" class="usShip" onclick="usShiptoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none ">
						<input type="button" id="customerShipto" class="customerShip" onclick="customerShiptoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none">
						<input type="button" id="jobsiteShipto" class="jobsiteShip" onclick="jobsiteShiptoAddress()" style="background: url(./../resources/images/jobsite.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none ">
						<input type="button" id="otherShipto" class="otherShip" onclick="otherShiptoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none">
					</td>
				</tr>
				</table>
		</fieldset>
	</td>
	</tr>
	</table>