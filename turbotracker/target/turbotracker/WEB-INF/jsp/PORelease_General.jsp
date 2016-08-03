<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="pogeneral">
	<form id="POGeneralForm">
	<table>
		<tr>
			<td>
				<table>
					<tr>
						<td align="left">
							<fieldset class= "custom_fieldset" style="height: 160px;width: 340px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table>
										<tr>
					 						<td><input type="text" id="locationbillToAddressID" name="locationbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
					 						<input type="text" id="customerBillToID" name="customerBillName" style="display: none;">
					 						</td>
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
					 					<tr align="left">
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 48px;">
												<div id="billtoRadioSet">
													<!-- 	<input type="button" id="usBillto" class="usBill" onclick="usBilltoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px; border:none ">
														<input type="button" id="customerBillto" class="customerBill" onclick="customerBilltoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px; border:none">
														<input type="button" id="otherBillto" class="otherBill" onclick="otherBilltoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ; border:none">-->
														
														
	    											<input type="radio" id="billtoradio1" name="radio1" /><label id ="billtolabel1" for="billtoradio1" onclick="usBilltoAddress()" style="width: 63px; margin-right: -6px;">Us</label>
	    											<input type="radio" id="billtoradio2" name="radio1" /><label id ="billtolabel2" for="billtoradio2" onclick="customerBilltoAddress()" style="margin-right: -6px;" >Customer</label>
	   												<input type="radio" id="billtoradio3" name="radio1" /><label id ="billtolabel3" for="billtoradio3" onclick="otherBilltoAddress()" style="margin-right: -6px;">Other</label>
   												 
   												</div>
											</td>
										</tr>
					 				</table>
							</fieldset>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "custom_fieldset" style="height: 160px;width: 340px;">
									<legend class="custom_legend"><label><b>Ship To</b></label>
									</legend>
									<table  id="shipTo">
										<tr>
						 					<td><input type="text" id="locationShipToAddressID" name="locationShipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
						 					<input type="text" id="customerShipToID" name="customerShipToName" style="display: none;">
						 					</td>
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
					 								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
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
					 								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style=" width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="forWardId" value="" onclick="shipForWard()" style=" width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer; ">
					 						</td>
					 					</tr>
					 				</table>
					 				<table  id="shipTo2">
										<tr>
						 					<td><input type="text" id="locationShipToAddressID6" name="locationShipToAddressID6" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
						 					<input type="text" id="customerShipToID" name="customerShipToName" style="display: none;">
						 					</td>
						 				</tr>
						 				<tr>
						 					<td><input type="text" id="locationShipToAddressID7" name="locationShipToAddress7" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="locationShipToAddressID8" name="locationShipToAddress8" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="locationShipToCity2" name="locationShipToCity2" style="width: 100px;" disabled="disabled">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="locationShipToState2" name="locationShipToState2" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
					 								<label>Zip: </label><input type="text" id="locationShipToZipID2" name="locationShipToZip2" style="width: 75px;" disabled="disabled">
					 								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
					 						</td>
					 					</tr>
					 				</table>
					 				<table>
					 					<tr align="center">
											<td id="radioSet" style="vertical-align: bottom;padding-left: 20px; height: 10px">
												<div id="shipToRadioSet">
													<!-- <input type="button" id="usShipto" class="usShip" onclick="usShiptoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none ">
													<input type="button" id="customerShipto" class="customerShip" onclick="customerShiptoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none">
													<input type="button" id="jobsiteShipto" class="jobsiteShip" onclick="jobsiteShiptoAddress()" style="background: url(./../resources/images/jobsite.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none ">
													<input type="button" id="otherShipto" class="otherShip"  onclick="otherShiptoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none">-->
												
													<input type="radio" id="shiptoradio1" name="radio"/><label for="shiptoradio1" id="shiptolabel1" onclick="usShiptoAddress()" style="width: 63px; margin-right: -6px;">Us</label>
													<input type="radio" id="shiptoradio2" name="radio"/><label for="shiptoradio2" id="shiptolabel2" onclick="customerShiptoAddress()" style="margin-right: -6px;">Customer</label>
													<input type="radio" id="shiptoradio3" name="radio"/><label for="shiptoradio3" id="shiptolabel3" onclick="jobsiteShiptoAddress()" style="margin-right: -6px;">Job Site</label>
													<input type="radio" id="shiptoradio4" name="radio"/><label for="shiptoradio4" id="shiptolabel4" onclick="otherShiptoAddress()">Other</label>
												</div>
											</td>
										</tr>
					 				</table>
								</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>	
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 100px;width: 340px;">
								<table>
									<tr align="left">
										<td style="width: 110px;"><label>Ordered By:</label></td>
										<td><input type="text" style="width: 100px" id="orderId" name="orderName" value="${sessionScope.user.fullName}"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;</td>
										<td><input type="hidden" id="orderhiddenId" name="orderhiddenName" style="width: 40px;" value="${sessionScope.user.userId}">
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Freight charges:</label></td>
										<td>
											<select style="width:120px" id="frieghtChangesId" name="frieghtChangesName" >
												<option value="-1"> - Select - </option>
												<c:forEach var="friedChargesBean" items="${requestScope.friedCharges}">
													<option value="${friedChargesBean.veFreightChargesId}">
														<c:out value="${friedChargesBean.description}" ></c:out>
													</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Ship Via:</label></td>
										<td>
											<select style="width:120px;" id="shipViaId" name="shipViaName">
												<option value="-1"> - Select - </option>
												<c:forEach var="shipViaBean" items="${requestScope.shipVia}">
													<option value="${shipViaBean.veShipViaId}">
														<c:out value="${shipViaBean.description}" ></c:out>
													</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 100px" id="wantedId" name="wantedName" value="" class="datepicker"></td>
										<td><select id="wantedComboId"  style="width: 90px" name="wantedCombo" >
												<option value="-1"> - Select - </option>
												<option value="0"> On or Before </option>
												<option value="1"> Not Before </option>
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "custom_fieldset" style="height: 100px;width: 340px;">
									<legend class="custom_legend"><b>Vendor</b></legend>
								<table>
									<tr>
										<td style="width:120px"><label>Manufacturer:</label></td>
										<td><input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value=""> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" value=""></td>
									</tr>
									<tr>
										<td style="width:80px"><label>ATTN:</label></td>
										<td id="contactselectID">
											<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId()"><option value="-1">- Select -</option></select>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 145px;width: 340px;">
								<table>
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="" style="width: 140px;"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="">
										<!-- <select id="customerNAMEId" name="customerName">
												<option value="-1"> - Select - </option>
													<c:if test="${not empty requestScope.joMasterDetails.customerPonumber}">
											 	<option value="1" >${requestScope.joMasterDetails.customerPonumber}</option>
											 </c:if>
											<c:forEach var="CustomerPONumber" items="${requestScope.custPONoDetails}">
												 <c:if test="${not empty CustomerPONumber.customerPonumber1}">
												 	<option value="2" >${CustomerPONumber.customerPonumber1}</option>
												</c:if>
												<c:if test="${not empty CustomerPONumber.customerPonumber2}">
													 <option value="3" >
												 		<c:out value="${CustomerPONumber.customerPonumber2}" ></c:out>
												 	</option>
												</c:if>
												<c:if test="${not empty CustomerPONumber.customerPonumber3}">
												 	<option value="4" >
												 		<c:out value="${CustomerPONumber.customerPonumber3}" ></c:out>
												 	</option>
												</c:if>
												<c:if test="${not empty CustomerPONumber.customerPonumber4}">
												 	<option value="5" >
												 		<c:out value="${CustomerPONumber.customerPonumber4}" ></c:out>
												 	</option>
												 </c:if>
												 <c:if test="${not empty CustomerPONumber.customerPonumber5}">
												 	<option value="6" >
												 		<c:out value="${CustomerPONumber.customerPonumber5}" ></c:out>
												 	</option>
												 </c:if>
											</c:forEach>
											</select> -->
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value=""></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" ></td>
									</tr>
									<tr align="left" id="qbtr">
										<td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="" readonly="readonly"><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td style="vertical-align: top">
							<fieldset class= "custom_fieldset" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table>
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
			<fieldset class= "custom_fieldset" style="height: 50px;width: 710px;">
				<legend class="custom_legend"><label><b>Total</b></label></legend>
					<table style="width: 735px">
						<tr>
							<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalGeneralId" name="" disabled="disabled"></td>
							<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat()" onfocus="frieghtCost()"></td>
							<td><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" disabled="disabled"></td>
							<td><label style="right: 13px;position: relative;">% &nbsp;</label></td><td><input type="text" id="generalID" name="generalName" style="width: 60px;text-align:right;" disabled="disabled"></td>
							<td><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalGeneralId" name="totalGeneralName" disabled="disabled"></td>
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="vePOID" name="vePOName" disabled="disabled"></td>
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" disabled="disabled"></td>
						</tr>
					</table>
			</fieldset>
			</td>
		</tr>
</table>
</form>
<hr>
<table>
	<tr>
		<td style="width: 60px;padding: 0px 1px;">
	  		<fieldset class= " custom_fieldset" style="padding-bottom: 0px;vertical-align: middle;">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOEmail('poGeneral')"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStamp" style="color: green;"></label></td>
		<td style="display: none;">
			<input type="button" id="viewGeneralPdfId" class="cancelhoverbutton turbo-tan" value="Confirm" onclick="viewAsPdfGeneral()" style="width:80px;">
		</td>
		<td align="right" style="padding-left: 165px;">
			<div id="showMessage" style="color: green;"></div>
			<input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Save" onclick="savePo()" style="width:90px;position: relative; left: -20px;">
			<input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="savePORelease()" style="width:106px;position: relative; left: -14px;">
			<input type="checkbox" id="ButtonClicked" style="display: none;"/>
		</td>
	</tr>
</table>
</div>
<div class="loadingDiv" id="loadingPOGenDiv"> </div>
<div style="display: none;">
	<table>
		<tr>
			<td>
				<input type="text" id="rxAddressShipID" name="shiptoAddress" value= "">
			</td>
			<td>
				<input type="text" id="rxAddressBillID" name="billtoAddress" value= "">
			</td>
			<td>
				<input type="text" id="rxManuId" name="rxManuName" value= "">
			</td>
			<td>
				<input type="text" id="shiptomode" name="shipto" value= "">
			</td>
			<td>
				<input type="text" id="billtodescription" name="billtodescription" value= "${requestScope.settingDetails.billToDescription}">
			</td>
			<td>
				<input type="text" id="billtoaddress1" name="billtoaddress1" value= "${requestScope.settingDetails.billToAddress1}">
			</td>
			<td>
				<input type="text" id="billtoaddress2" name="billtoaddress2" value= "${requestScope.settingDetails.billToAddress2}">
			</td>
			<td>
				<input type="text" id="billtocity" name="billtocity" value= "${requestScope.settingDetails.billToCity}">
			</td>
			<td>
				<input type="text" id="billtostate" name="billtostate" value= "${requestScope.settingDetails.billToState}">
			</td>
			<td>
				<input type="text" id="billtozip" name="billtozip" value= "${requestScope.settingDetails.billToZip}">
			</td>
			<td>
				<input type="text" id="customerPONumberID" name="customerPONumberName" value= "${requestScope.joMasterDetails.customerPonumber}">
			</td>
		</tr>
	</table>
 </div>
 <script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PORelease_General.js"></script>

<script>
jQuery(document).ready(function() {
	shipToAddress();
	billToAddress();
	loadShipToAddress();
	$("#shipTo1").hide();
	$("#shipTo2").hide();
	$("#shipTo").hide();
	$('#forWardId').css({"display": "none"});
	$('#backWardId').css({"display": "none"});
	$("#billtoRadioSet").buttonset();
	$("#shipToRadioSet").buttonset();
	var qbenabled = $('#qbEnabled').val();
	if(qbenabled==='false'){
		$('#qbtr').hide();
	}
	//$('#billtoradio2').attr('checked', 'checked');
});
function loadShipToAddress(){
	var aWareHouse = "${requestScope.wareHouse}";
	if(aWareHouse !== ''){
		var reminders = []; //create a new array global
	    <c:forEach items="${requestScope.wareHouse}" var="reminder">
	        reminders.push({description: "${reminder.description}", address1: "${reminder.address1}", address2: "${reminder.address2}", city: "${reminder.city}", state: "${reminder.state}", zip: "${reminder.zip}"});
	    </c:forEach>
	    var sfe = "${requestScope.wareHouse}";
	   	var countWarehouse = sfe.split(",");
	    if(countWarehouse.length == 1){
			 $("#locationShipToAddressID6").val(reminders[0].description); $("#locationShipToAddressID7").val(reminders[0].address1); $("#locationShipToAddressID8").val(reminders[0].address2); $("#locationShipToCity2").val(reminders[0].city);
			 $("#locationShipToState2").val(reminders[0].state); $("#locationShipToZipID2").val(reminders[0].zip);
			 $("#customerShipToAddressID").val(reminders[0].description); $("#customerShipToAddressID1").val(reminders[0].address1); $("#customerShipToAddressID2").val(reminders[0].address2); $("#customerShipToCity").val(reminders[0].city);
			 $("#customerShipToState").val(reminders[0].state); $("#locationShipToZipID").val(reminders[0].zip);
	    }
		 if(countWarehouse.length == 2){
			 $("#locationShipToAddressID6").val(reminders[0].description); $("#locationShipToAddressID7").val(reminders[0].address1); $("#locationShipToAddressID8").val(reminders[0].address2); $("#locationShipToCity2").val(reminders[0].city);
			 $("#locationShipToState2").val(reminders[0].state); $("#locationShipToZipID2").val(reminders[0].zip);
			 $("#customerShipToAddressID").val(reminders[0].description); $("#customerShipToAddressID1").val(reminders[0].address1); $("#customerShipToAddressID2").val(reminders[0].address2); $("#customerShipToCity").val(reminders[0].city);
			 $("#customerShipToState").val(reminders[0].state); $("#locationShipToZipID").val(reminders[0].zip);
			 $("#locationShipToAddressID4").val(reminders[1].description); $("#locationShipToAddressID5").val(reminders[1].address1); $("#locationShipToAddressID3").val(reminders[1].address2); $("#locationShipToCity1").val(reminders[1].city);
			 $("#locationShipToState1").val(reminders[1].state); $("#locationShipToZipID1").val(reminders[1].zip);
			 $("#customerShipToAddressID4").val(reminders[1].description); $("#customerShipToAddressID5").val(reminders[1].address1); $("#customerShipToAddressID3").val(reminders[1].address2); $("#customerShipToCity1").val(reminders[1].city);
			 $("#customerShipToState1").val(reminders[1].state); $("#customerShipToZipID1").val(reminders[1].zip);
		 }
		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
	}
}

function shipBackWard(){
	var aVePOID = $("#vePO_ID").text();
	updateShipToAddressValue(aVePOID, 0);
	$("#shipTo2").hide();
	$("#shipTo").hide();
	$("#shipTo1").show();
}

function shipForWard(){
	var aVePOID = $("#vePO_ID").text();
	updateShipToAddressValue(aVePOID, 1);
	$("#shipTo2").show();
	$("#shipTo").hide();
	$("#shipTo1").hide();
	loadShipToAddress();
}
function savePo(){
	$("#ButtonClicked").prop('checked', true);
	savePORelease();
}
</script>