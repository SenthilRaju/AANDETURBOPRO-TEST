<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="pogeneral">
	<form id="POGeneralForm">
	<table>
		<tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 340px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table style="width: 400px">
										<tr>
					 						<td><input type="text" id=billToAddressID name="billToAddressID" value="" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
					 						<input type="text" id="customerBillToID" name="customerBillName" style="display: none;">
					 						</td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="billToAddressID1" name="billToAddressID1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="billToAddressID2" name="billToAddressID2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="billToCity" name="billToCity" style="width: 100px;" disabled="disabled">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="billToState" name="billToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled">
					 								<label>Zip: </label><input type="text" id="billToZipID" name="billToZipID" style="width: 75px;" disabled="disabled">
					 						</td>
					 					</tr>
					 					<tr height="10px"></tr>
					 					<tr align="left">
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 48px;">
												<div id="billtoRadioSet">
	    											<input type="radio" id="billtoradio1" name="radio1" /><label id ="billtolabel1" for="billtoradio1" onclick="billToAddress('Us')" style="width: 63px; margin-right: -6px;">Us</label>
	    											<input type="radio" id="billtoradio2" name="radio1" /><label id ="billtolabel2" for="billtoradio2" onclick="billToAddress('Customer')" style="margin-right: -6px;" >Customer</label>
	   												<input type="radio" id="billtoradio3" name="radio1" /><label id ="billtolabel3" for="billtoradio3" onclick="billToAddress('Other')" style="margin-right: -6px;">Other</label>
   												 
   												</div>
											</td>
										</tr>
					 				</table>
							</fieldset>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 340px;">
									<legend class="custom_legend"><label><b>Ship To</b></label>
									</legend>
									<table  id="shipTo" style="width: 400px">
										<tr>
						 					<td><input type="text" id="shipToAddressID" name="shipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
						 					<input type="text" id="customerShipToID" name="customerShipToName" style="display: none;">
						 					</td>
						 				</tr>
						 				<tr>
						 					<td><input type="text" id="shipToAddressID1" name="shipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="shipToAddressID2" name="shipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="shipToCity" name="shipToCity" style="width: 100px;" disabled="disabled">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="shipToState" name="shipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
					 								<label>Zip: </label><input type="text" id="shipToZipID" name="shipToZip" style="width: 75px;" disabled="disabled">
					 								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
					 						</td>
					 					</tr>
					 				</table>
					 				
					 				<table>
					 				<tr height="10px"></tr>
					 					<tr align="center">
											<td id="radioSet" style="vertical-align: bottom;padding-left: 20px; height: 10px">
												<div id="shipToRadioSet">
													<input type="radio" id="shiptoradio1" name="radio"/><label for="shiptoradio1" id="shiptolabel1" onclick="shipToAddress('Us')" style="width: 63px; margin-right: -6px;" >Us</label>
													<input type="radio" id="shiptoradio2" name="radio"/><label for="shiptoradio2" id="shiptolabel2" onclick="shipToAddress('Customer')" style="margin-right: -6px;">Customer</label>
													<input type="radio" id="shiptoradio3" name="radio"/><label for="shiptoradio3" id="shiptolabel3" onclick="shipToAddress('Job Site')" style="margin-right: -6px;">Job Site</label>
													<input type="radio" id="shiptoradio4" name="radio"/><label for="shiptoradio4" id="shiptolabel4" onclick="shipToAddress('Other')">Other</label>
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
				<table  style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 120px;width: 340px;">
								<table style="width: 400px">
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
												<c:forEach var="friedChargesBean" items="${friedCharges}">
													<c:if test="${theVepo.veFreightChargesId eq friedChargesBean.veFreightChargesId}">
														<option selected="selected" value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veFreightChargesId ne friedChargesBean.veFreightChargesId}">
														<option value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Ship Via:</label></td>
										<td>
											<select style="width:120px;" id="shipViaId" name="shipViaName">
												<option value="-1"> - Select - </option>
												<c:forEach var="shipViaBean" items="${shipVia}">
													<c:if test="${theVepo.veShipViaId eq shipViaBean.veShipViaId}">
														<option selected="selected" value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veShipViaId ne shipViaBean.veShipViaId}">
														<option value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 100px" id="wantedId" name="wantedName" value="${theVepo.dateWanted }" class="datepicker"></td>
										<td><select id="wantedComboId"  style="width: 90px" name="wantedCombo" >
												<option value="-1"> - Select - </option>
													<c:if test="${theVepo.wantedOnOrBefore eq '0'}">
														<option value="0" selected="selected"> On or Before </option>
														<option value="1"> Not Before </option>
													</c:if>
													<c:if test="${theVepo.wantedOnOrBefore ne '0'}">
														<option value="0"> On or Before </option>
														<option value="1" selected="selected"> Not Before </option>
													</c:if>
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
									<legend class="custom_legend"><b>Vendor</b></legend>
								<table style="width: 400px">
									<tr>
										<td style="width:120px"><label>Manufacturer:</label></td>
										<td><input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="${manufactureName}" disabled="disabled"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value=""> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" value=""></td>
									</tr>
									<tr>
										<td style="width:80px"><label>ATTN:</label></td>
										<td id="contactselectID">
											<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId()"><option value="-1">- Select -</option>
												<c:forEach var="buddies" items="${buddiesList}">
													<c:if test="${theVepo.rxVendorContactId eq buddies.rxContactId}">
														<option selected="selected" value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
													<c:if test="${theVepo.rxVendorContactId ne buddies.rxContactId}">
														<option value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
												</c:forEach>
											</select>
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
				<table style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all"  style="height: 145px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="<fmt:formatDate pattern="MM/dd/yyyy"  value="${theVepo.orderDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="${theVepo.customerPonumber}">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value="${theVepo.ponumber}"></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="${theVepo.tag}" ></td>
									</tr>
									<tr align="left" id="qbtr">
										<td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="${theVepo.qbPO}" readonly="readonly" ><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td style="vertical-align: top">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName">${theVepo.specialInstructions}</textarea></td>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
			<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 850px">
				<legend class="custom_legend"><label><b>Total</b></label></legend>
					<table style="width: 750px">
						<tr>
							<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalGeneralId" name="" value="<fmt:formatNumber type="currency" pattern="$#,##0.00" value="${theVepo.subtotal}" />" disabled="disabled"></td>
							<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat()" onfocus="frieghtCost()" value="<fmt:formatNumber type="currency" pattern="$#,##0.00" value="${theVepo.freight}" />"></td>
							<td><label>Tax: </label></td><td><fmt:setLocale value="en_US"/><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" disabled="disabled" value="${theVepo.taxRate}"></td>
							<td><label style="right: 13px;position: relative;">% &nbsp;</label></td><td><input type="text" id="generalID" name="generalName" style="width: 60px;text-align:right;" disabled="disabled" value="<fmt:formatNumber type="currency" value="${theVepo.taxTotal }" />"></td>
							<td><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalGeneralId" name="totalGeneralName" disabled="disabled" value="<fmt:formatNumber type="currency" pattern="$#,##0.00" value="${theVepo.taxTotal+theVepo.freight+theVepo.subtotal}" />"></td>	
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="vePOID" name="vePOName" disabled="disabled" value="${theVepo.vePoid}"></td>
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" disabled="disabled" value="${theVepo.veFactoryId}"></td>
						</tr>
					</table>
			</fieldset>
			</td>
		</tr>
</table>
</form>

<table style="width: 900px">
	<tr>
		<td style="width: 60px;padding: 0px 1px;">
	  		<fieldset class= "ui-widget-content ui-corner-all"  style="padding-bottom: 0px;vertical-align: middle;">
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
			<input type="checkbox" id="ButtonClicked" style="display: none;"/>
		</td>
	</tr>
</table>
</div>

<script type="text/javascript">

var aPurchaseOrderObject = "${theVepo}";

jQuery(document).ready(function() {
	$("#billtoRadioSet").buttonset();
	$("#shipToRadioSet").buttonset();
	$(".datepicker").datepicker();
	loadBillAddress();
	loadShipAddress();
});
function usShiptoAddress(){
	 $("#shiptoradio1").attr("Checked","Checked");
	 $('#shiptolabel1').css("font-weight","bold");
	 $('#shiptolabel2').css("font-weight","normal");
	 $('#shiptolabel3').css("font-weight","normal");
	 $('#shiptolabel4').css("font-weight","normal");
	 $('#shiptolabel1').addClass("ui-state-active");
	 $('#shiptolabel2').removeClass("ui-state-active");
	 $('#shiptolabel3').removeClass("ui-state-active");
	 $('#shiptolabel4').removeClass("ui-state-active");
	 var vePOID = $("#vePO_ID").text();
	 var usShipUSID = 0;
	 var updaetKey = 'shipTo';
	 $('#forWardId').show();
	 $('#backWardId').show();
	 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
	 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
	 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
	 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
	 loadShipToAddress();
	 updateBillToAndShipToAddressSetting(vePOID, usShipUSID, updaetKey);
	 return true;
	 }
	 
	 function loadBillAddress()
	 {
		 $("#billtolabel1").removeClass('ui-state-active');
		 $("#billtolabel2").removeClass('ui-state-active');
		 $("#billtolabel3").removeClass('ui-state-active');
		 var billToIndex = "${theVepo.billTo}";
		 if(billToIndex === 0)
			 {
			 $("#billtolabel1").addClass('ui-state-active');
			 billToAddress('Us');
			 }
		 else if(billToIndex === 1)
			 {
			 $("#billtolabel2").addClass('ui-state-active');
			 billToAddress('Customer');
			 }
		 else if(billToIndex === 2)
		 {
			 $("#billtolabel3").addClass('ui-state-active');
			 billToAddress('Other');
			 }
		 else 
		 {
			 $("#billtolabel1").addClass('ui-state-active');
			 billToAddress('Us');
			 }
	 }
	 
	 function loadShipAddress()
	 {
		 $("#shiptolabel1").removeClass('ui-state-active');
		 $("#shiptolabel2").removeClass('ui-state-active');
		 $("#shiptolabel3").removeClass('ui-state-active');
		 var shipToIndex = "${theVepo.billTo}";
		 if(shipToIndex === 0)
			 {
			 $("#shiptolabel1").addClass('ui-state-active');
			 shipToAddress('Us');
			 }
		 else if(shipToIndex === 1)
			 {
			 $("#shiptolabel2").addClass('ui-state-active');
			 shipToAddress('Customer');
			 }
		 else if(shipToIndex === 2)
		 {
			 $("#shiptolabel3").addClass('ui-state-active');
			 shipToAddress('Other');
			 }
		 else if(shipToIndex === 3)
		 {
			 $("#shiptolabel4").addClass('ui-state-active');
			 shipToAddress('Job Site');
			 }
		 else 
		 {
			 $("#shiptolabel1").addClass('ui-state-active');
			 shipToAddress('Customer');
			 }
	 }
function billToAddress(type)
{
	 $("#billToAddressID").attr("disabled",true);
	 $("#billToAddressID1").attr("disabled",true);
	 $("#billToAddressID2").attr("disabled",true);
	 $("#billToCity").attr("disabled",true);
	 $("#billToState").attr("disabled",true);
	 $("#billToZipID").attr("disabled",true);
	 
	if(type === 'Us')
		{
		$('#billToAddressID').val("${aSetting.billToDescription}");
		$('#billToAddressID1').val("${aSetting.billToAddress1}");
		$('#billToAddressID2').val("${aSetting.billToAddress2}");
		$('#billToCity').val("${aSetting.billToCity}");
		$('#billToState').val("${aSetting.billToState}");
		$('#billToZipID').val("${aSetting.billToZip}");
		}
	if(type === 'Customer')
		{
		$('#billToAddressID').val("${billToName}");
		$('#billToAddressID1').val("${theRxaddress.address1}");
		$('#billToAddressID2').val("${theRxaddress.address2}");
		$('#billToCity').val("${theRxaddress.city}");
		$('#billToState').val("${theRxaddress.state}");
		$('#billToZipID').val("${theRxaddress.zip}");
		}
	if(type === 'Other')
		{
		$("#billToAddressID").attr("disabled",false);
		 $("#billToAddressID1").attr("disabled",false);
		 $("#billToAddressID2").attr("disabled",false);
		 $("#billToCity").attr("disabled",false);
		 $("#billToState").attr("disabled",false);
		 $("#billToZipID").attr("disabled",false);
		$('#billToAddressID').val("");
		$('#billToAddressID1').val("");
		$('#billToAddressID2').val("");
		$('#billToCity').val("");
		$('#billToState').val("");
		$('#billToZipID').val("");
		}
	}
	
	function  shipToAddress(type)
	{
		var size = "${fn:length(wareHouse)}";
		$("#shipToAddressID").attr("disabled",true);
		 $("#shipToAddressID1").attr("disabled",true);
		 $("#shipToAddressID2").attr("disabled",true);
		 $("#shipToCity").attr("disabled",true);
		 $("#shipToState").attr("disabled",true);
		 $("#shipToZipID").attr("disabled",true);
		if(type === 'Us')
		{
			if(size > 0)
				{
				$('#shipToAddressID').val("${wareHouse[0].description}");
				$('#shipToAddressID1').val("${wareHouse[0].address1}");
				$('#shipToAddressID2').val("${wareHouse[0].address2}");
				$('#shipToCity').val("${wareHouse[0].city}");
				$('#shipToState').val("${wareHouse[0].state}");
				$('#shipToZipID').val("${wareHouse[0].zip}");
				}
		}
		if(type === 'Customer')
		{
			$('#shipToAddressID').val("${billToName}");
			$('#shipToAddressID1').val("${theRxaddress.address1}");
			$('#shipToAddressID2').val("${theRxaddress.address2}");
			$('#shipToCity').val("${theRxaddress.city}");
			$('#shipToState').val("${theRxaddress.state}");
			$('#shipToZipID').val("${theRxaddress.zip}");
		}
		if(type === 'Job Site')
		{
			$('#shipToAddressID').val("${billToName}");
			$('#shipToAddressID1').val("");
			$('#shipToAddressID2').val("");
			$('#shipToCity').val("");
			$('#shipToState').val("");
			$('#shipToZipID').val("");
		}
		if(type === 'Other')
		{
			$("#shipToAddressID").attr("disabled",false);
			 $("#shipToAddressID1").attr("disabled",false);
			 $("#shipToAddressID2").attr("disabled",false);
			 $("#shipToCity").attr("disabled",false);
			 $("#shipToState").attr("disabled",false);
			 $("#shipToZipID").attr("disabled",false);
			 $('#shipToAddressID').val("");
				$('#shipToAddressID1').val("");
				$('#shipToAddressID2').val("");
				$('#shipToCity').val("");
				$('#shipToState').val("");
				$('#shipToZipID').val("");
		}
	}
	function shipBackWard(){
		var size = "${fn:length(wareHouse)}";
		var shipAddresses = []; //create a new array global
		if(size > 0)
		{
			<c:forEach items="${wareHouse}" var="house">
			shipAddresses.push({description: "${house.description}", address1: "${house.address1}", address2: "${house.address2}", city: "${house.city}", state: "${house.state}", zip: "${house.zip}"});
			</c:forEach>
			for(var i = (shipAddresses.length-1); i >= 0; i--)
				{
				$('#shipToAddressID').val(shipAddresses[i].description);
				$('#shipToAddressID1').val(shipAddresses[i].address1);
				$('#shipToAddressID2').val(shipAddresses[i].address2);
				$('#shipToCity').val(shipAddresses[i].city);
				$('#shipToState').val(shipAddresses[i].state);
				$('#shipToZipID').val(shipAddresses[i].zip);
				}
		}
	}

	function shipForWard(){
		var size = "${fn:length(wareHouse)}";
		
		if(size > 0)
		{
			<c:forEach items="${wareHouse}" var="house" >
			$('#shipToAddressID').val("${house.description}");
			$('#shipToAddressID1').val("${house.address1}");
			$('#shipToAddressID2').val("${house.address2}");
			$('#shipToCity').val("${house.city}");
			$('#shipToState').val("${house.state}");
			$('#shipToZipID').val("${house.zip}");
			 </c:forEach>
		}
	}
	function viewPOPDF(){
		var vePOID = $("#vePOID").val();
		var aPDFType = "purchase";
		var aShipToAddrID = "${theVepo.rxShipToID}";
		var aManufacturerId = "${theVepo.rxVendorId}";
		var joReleaseId = "${theVepo.joReleaseId}";
		var jobNumber = "";
		var rxCustomerId = "";
		$.ajax({
			type: "GET",
			url: "./jobtabs5/getjomasterbyjoreleaseid",
			data: { 'joReleaseID': joReleaseId},
			dataType: "json",
			success: function (data) {
				jobNumber = data.jobNumber;
				rxCustomerId = data.rxCustomerId;
				window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxCustomerId+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToAddrID);
			},
			error: function (msg) {
				
			}
		});
		

	}
</script>