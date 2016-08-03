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
						<input type="hidden" id="mytestid" >
							<fieldset class= "custom_fieldset" style="height: 160px;width: 340px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table>
										<tr>
					 						<td><input type="text" id="locationbillToAddressID" name="locationbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" onchange="POGeneralTabformChanges();" disabled="disabled" >
					 						<input type="text" id="customerBillToID" name="customerBillName" style="display: none;">
					 						</td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="locationbillToAddressID1" name="locationbillToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" onchange="POGeneralTabformChanges();"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="locationbillToAddressID2" name="locationbillToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled" onchange="POGeneralTabformChanges();"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="locationbillToCity" name="locationbillToCity" style="width: 100px;" disabled="disabled" onchange="POGeneralTabformChanges();">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="locationbillToState" name="locationbillToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled" onchange="POGeneralTabformChanges();">
					 								<label>Zip: </label><input type="text" id="locationbillToZipID" name="locationbillToZip" style="width: 75px;" disabled="disabled" onchange="POGeneralTabformChanges();">
					 						</td>
					 					</tr>
					 					<tr align="left">
					 					<input type="hidden" id="billToMode" /> 
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 48px;">
												<div id="billtoRadioSet">
													<!-- 	<input type="button" id="usBillto" class="usBill" onclick="usBilltoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px; border:none ">
														<input type="button" id="customerBillto" class="customerBill" onclick="customerBilltoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px; border:none">
														<input type="button" id="otherBillto" class="otherBill" onclick="otherBilltoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ; border:none">-->
														
														
	    											<input type="radio" id="billtoradio1" name="radio1" /><label id ="billtolabel1" for="billtoradio1" onclick="usBilltoAddress()" style="width: 63px; margin-right: -6px;" onchange="POGeneralTabformChanges();">Us</label>
	    											<input type="radio" id="billtoradio2" name="radio1" /><label id ="billtolabel2" for="billtoradio2" onclick="customerBilltoAddress()" style="margin-right: -6px;" onchange="POGeneralTabformChanges();">Customer</label>
	   												<input type="radio" id="billtoradio3" name="radio1" /><label id ="billtolabel3" for="billtoradio3" onclick="otherBilltoAddress()" style="margin-right: -6px;" onchange="POGeneralTabformChanges();">Other</label>
   												 
   												</div>
											</td>
										</tr>
					 				</table>
							</fieldset>
						</td>
						<td align="left" >
							<div id="PO_Shipto">
								<fieldset class= "custom_fieldset" style="height: 160px;width: 340px;">
								<legend class="custom_legend"><label><b>Ship To</b></label>	</legend>
									<%@ include file="ShipTo.jsp" %>
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
										<td><input type="text" style="width: 100px" id="orderId" name="orderName" value="${sessionScope.user.fullName}" onchange="POGeneralTabformChanges();"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;</td>
										<td><input type="hidden" id="orderhiddenId" name="orderhiddenName" style="width: 40px;" value="${sessionScope.user.userId}">
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Freight charges:</label></td>
										<td>
											<select style="width:120px" id="frieghtChangesId" name="frieghtChangesName" onchange="POGeneralTabformChanges();">
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
											<select style="width:120px;" id="shipViaId" name="shipViaName" onchange="POGeneralTabformChanges();">
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
										<td><input type="text" style="width: 100px" id="wantedId" name="wantedName" value="" class="datepicker" onchange="POGeneralTabformChanges();"></td>
										<td><select id="wantedComboId"  style="width: 90px" name="wantedCombo" onchange="POGeneralTabformChanges();">
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
										<td><input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" onchange="POGeneralTabformChanges();"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value=""> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" value=""></td>
									</tr>
									<tr>
										<td style="width:80px"><label>ATTN:</label></td>
										<td id="contactselectID">
											<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId();POGeneralTabformChanges();"><option value="-1">- Select -</option></select>
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
							<fieldset class= "custom_fieldset" style="height: 100px;width: 340px;">
								<table>
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="" style="width: 140px;" onchange="POGeneralTabformChanges();"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="" onchange="POGeneralTabformChanges();">
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
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value="" onchange="POGeneralTabformChanges();"></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" onchange="POGeneralTabformChanges();"></td>
									</tr>
									<tr align="left" id="qbtr" style="display: none;">
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
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName" onchange="POGeneralTabformChanges();"></textarea></td>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<fieldset class= "custom_fieldset" style="height: 195px;width: 320px;">
								<legend class="custom_legend"><label><b>Split Commission</b></label><span id="splitCommissionPOLabel" style="display: none;color: red;">*</span></legend>
								<table>
									<tr>
										<td>
										<span id="PoSplitCommission">
										<table id="PoSplitCommissionGrid"></table>
										<div id="PoSplitCommissionGridPager"></div>
										</span>
										</td>
								</table>
							</fieldset>
			</td>
		</tr>
		<tr>
			<td>
			<fieldset class= "custom_fieldset" style="height: 50px;width: 710px;">
				<legend class="custom_legend"><label><b>Total</b></label></legend>
					<table style="width: 735px">
						<tr>
							<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalGeneralId" name="" disabled="disabled"></td>
							<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat();POGeneralTabformChanges();" onfocus="frieghtCost()"></td>
							<td><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" readonly="readonly"></td>
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
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC" onclick="outsidepoEmailButtonAction()"></td>
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
			<input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Close" onclick="closePOGeneralItemTab()" style="width:106px;position: relative; left: -14px;">
			<input type="checkbox" id="ButtonClicked" style="display: none;"/>
		</td>
	</tr>
</table>
</div>
<div class="loadingDiv" id="loadingPOGenDiv"> </div>
<div style="display: none;">
<input type="text" id="dropshipTaxID_release" value="0"/>
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
	<%-- <jsp:include page="./Email_Attachment.jsp"></jsp:include> --%>
	
 </div>
 <%-- <input type="hidden" name="transactionStatus" value="${theVepo.transactionStatus}" id="POtransactionStatus"/> --%>

 <script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/PORelease_General.js"></script>

<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>


<script>

var incValue=0;


jQuery(document).ready(function() {
	//shipToAddress();
	
	billToAddress();
	
//	loadShipToAddress("0");
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
function loadShipToAddress(shiptomode,shipindex,customername){
	if(typeof shipindex === "undefined") 
		shipindex = 0;
	if(typeof customername === "undefined") 
		customername=$("#jobCustomerName_ID").text();
		
	$("#hiddenshiptoindex").val(shipindex);

	
	 
	
	$("#shipTo2").show();
	$("#shipTo").hide();
	$("#shipTo1").hide();



	   		if(parseInt(shiptomode)==0)
		   	{

			 //  	alert(shiptomode);

	   		 $("#shiptoradio1").attr("Checked","Checked");
			 $('#shiptolabel1').addClass("ui-state-active");
			
	   		 $("#cusfrandbw").css("display","none");
	   		 $("#usfrandbw").show();
	   		 
	   			var aWareHouse = "${requestScope.wareHouse}";
	   			if(aWareHouse !== ''){
	   				var reminders = []; //create a new array global
	   			    <c:forEach items="${requestScope.wareHouse}" var="reminder">
	   			        reminders.push({description: "${reminder.description}", 
	   				        address1: "${reminder.address1}", 
	   				        address2: "${reminder.address2}", 
	   				        city: "${reminder.city}", 
	   				        state: "${reminder.state}", 
	   				    	warehouseid: "${reminder.prWarehouseId}", 
	   				        zip: "${reminder.zip}",
	   				        coTaxTerritoryId:"${reminder.coTaxTerritoryId}" 
	   				        });
	   			    </c:forEach>
	   			    var cotaxID=reminders[shipindex].coTaxTerritoryId;
	   			    loaddropshiptaxterritory(cotaxID,'PickUP');
	   			 
	   				$("#customerShipToID").val(reminders[shipindex].warehouseid); 
					$("#locationShipToAddressID6").val(reminders[shipindex].description); 
					 $("#locationShipToAddressID7").val(reminders[shipindex].address1); 
					 $("#locationShipToAddressID8").val(reminders[shipindex].address2); 
					 $("#locationShipToCity2").val(reminders[shipindex].city);
					 $("#locationShipToState2").val(reminders[shipindex].state);
					 $("#locationShipToZipID2").val(reminders[shipindex].zip);
				
		   	}
		   	}
			   	
	   	 if(parseInt(shiptomode)==1)
		   	{

	   		 $("#shiptoradio2").attr("Checked","Checked");
	   		 $('#shiptolabel2').addClass("ui-state-active");
			
	   		  
	   			
	   			var rxAddress = "${requestScope.theRxaddress}";
	   			if(rxAddress !== ''){
	   				var rxAddressssss = []; //create a new array global
	   				var customercotaxID=0;
	   			    <c:forEach items="${requestScope.theRxaddress}" var="remindert">
	   			    rxAddressssss.push({    address1: "${remindert.address1}", 
   				        address2: "${remindert.address2}", 
   				        city: "${remindert.city}", 
   				        state: "${remindert.state}", 
   				        rxAddressId: "${remindert.rxAddressId}", 
   				        zip: "${remindert.zip}"});
	   					customercotaxID="${remindert.coTaxTerritoryId}";
	   			    </c:forEach>
	   			    loaddropshiptaxterritory(customercotaxID,'Customer');
	   			   // alert(rxAddressssss.length);
	   			    
	   			    	if(rxAddressssss.length>1)
		   			    {
	   			    	$("#usfrandbw").css("display","none");
	   		   		 	$("#cusfrandbw").show(); 
		   			    }
	   			    	else
		   			    {
	   			    	$("#usfrandbw").css("display","none");
	   		   		 	$("#cusfrandbw").css("display","none");
		   			    }
		   			    
	   			    	var imgUrlDisForward = "./../resources/images/DisabledArrowright.png";
	   					$('#cuforWardIdy').css('background', 'url('+imgUrlDisForward+') no-repeat');
	   					$('#cuforWardIdy').css('background-position','center');	

	   					var imgUrlDisForward = "./../resources/images/DisabledArrowright.png";
	   					$('#cubackWardIdy').css('background', 'url('+imgUrlDisForward+') no-repeat');
	   					$('#cubackWardIdy').css('background-position','center');	
	   			
	   			
	   			 $("#customerShipToID").val(rxAddressssss[shipindex].rxAddressId); 
	   			 $("#locationShipToAddressID6").val($("#jobCustomerName_ID").text()); 
				 $("#locationShipToAddressID7").val(rxAddressssss[shipindex].address1); 
				 $("#locationShipToAddressID8").val(rxAddressssss[shipindex].address2); 
				 $("#locationShipToCity2").val(rxAddressssss[shipindex].city);
				 $("#locationShipToState2").val(rxAddressssss[shipindex].state);
				 $("#locationShipToZipID2").val(rxAddressssss[shipindex].zip);

	   			}
		   	}
	   		else if(parseInt(shiptomode)==2)
		   	{
	   		 $("#shiptoradio3").attr("Checked","Checked");
	   		 $('#shiptolabel3').addClass("ui-state-active");
		   	}
	   		else if(parseInt(shiptomode)==3)
		   	{
	   		 $("#shiptoradio4").attr("Checked","Checked");
	   		$('#shiptolabel4').addClass("ui-state-active");
		   	}


		document.getElementById('locationShipToAddressID').disabled=true;
		document.getElementById('locationShipToAddressID1').disabled=true;
		document.getElementById('locationShipToAddressID2').disabled=true;
		document.getElementById('locationShipToCity').disabled=true;
		document.getElementById('locationShipToState').disabled=true;
		document.getElementById('locationShipToZipID').disabled=true;
	
}



function shipBackWardy(){

	/*    var imgUrlDisBackward = "./../resources/images/DisabledArrowleft.png";
		$('#backWardIdy').css('background', 'url('+imgUrlDisBackward+') no-repeat');
		$('#backWardIdy').css('background-position','center'); */

	var aVePOID = $("#vePO_ID").text();
	//updateShipToAddressValue(aVePOID, 1,"Subtract",0);
	$("#shipTo2").show();
	$("#shipTo").hide();
	$("#shipTo1").hide();

		var aWareHouse = "${requestScope.wareHouse}";
		if(aWareHouse !== ''){
			var reminders = []; //create a new array global
		    <c:forEach items="${requestScope.wareHouse}" var="reminder">
		        reminders.push({description: "${reminder.description}", 
			        address1: "${reminder.address1}", 
			        address2: "${reminder.address2}", 
			        city: "${reminder.city}", 
			        state: "${reminder.state}", 
			        zip: "${reminder.zip}"});
		    </c:forEach>

	
		  	   if(parseInt($("#hiddenshiptoindex").val())!=0)
			   {
			   updateShipToAddressValue(aVePOID, 1,"Subtract",0);
			   } 
		 
	}
	
}

function shipForWardy(){
	 /* var imgUrlDisForward = "./../resources/images/DisabledArrowright.png";
		$('#forWardIdy').css('background', 'url('+imgUrlDisForward+') no-repeat');
		$('#forWardIdy').css('background-position','center');	
 */
	var aVePOID = $("#vePO_ID").text();
	
	$("#shipTo2").show();
	$("#shipTo").hide();
	$("#shipTo1").hide();
	
		var aWareHouse = "${requestScope.wareHouse}";
		var taxID=0;
		if(aWareHouse !== ''){
			var reminders = []; //create a new array global
		    <c:forEach items="${requestScope.wareHouse}" var="reminder">
		        reminders.push({description: "${reminder.description}", 
			        address1: "${reminder.address1}", 
			        address2: "${reminder.address2}", 
			        city: "${reminder.city}", 
			        state: "${reminder.state}", 
			        zip: "${reminder.zip}"});
		        taxID="${reminder.coTaxTerritoryId}";
		    </c:forEach>
		    loaddropshiptaxterritory(taxID,"PickUp");
		    var arrayLength=reminders.length-1;

		  	   if(arrayLength > parseInt($("#hiddenshiptoindex").val()))
			   {
			   updateShipToAddressValue(aVePOID, 1,"Add",0);
			   }
	}
}

function cushipBackWard(){

	var aVePOID = $("#vePO_ID").text();
//	updateShipToAddressValue(aVePOID, 1,"Subtract",1);
	$("#shipTo2").show();
	$("#shipTo").hide();
	$("#shipTo1").hide();

	var rxAddress = "${requestScope.theRxaddress}";
		if(rxAddress !== ''){
			var rxAddressssss = []; //create a new array global
		    <c:forEach items="${requestScope.theRxaddress}" var="remindert">
		    rxAddressssss.push({    address1: "${remindert.address1}", 
		        address2: "${remindert.address2}", 
		        city: "${remindert.city}", 
		        state: "${remindert.state}", 
		        zip: "${remindert.zip}"});
		    </c:forEach>

	 if(parseInt($("#hiddenshiptoindex").val())!=0)
	   {
	   updateShipToAddressValue(aVePOID, 1,"Subtract",1);
	   } 
		}
	 
	}

	function cushipForWard(){


		var aVePOID = $("#vePO_ID").text();
	//	updateShipToAddressValue(aVePOID, 1,"Add",1);
		$("#shipTo2").show();
		$("#shipTo").hide();
		$("#shipTo1").hide();

		var rxAddress = "${requestScope.theRxaddress}";
			if(rxAddress !== ''){
				var rxAddressssss = []; //create a new array global
			    <c:forEach items="${requestScope.theRxaddress}" var="remindert">
			    rxAddressssss.push({    address1: "${remindert.address1}", 
			        address2: "${remindert.address2}", 
			        city: "${remindert.city}", 
			        state: "${remindert.state}", 
			        zip: "${remindert.zip}"});
			    </c:forEach>

	   	 var arrayLength1=rxAddressssss.length-1;

	   	// alert(arrayLength1);

		  	   if(arrayLength1 > parseInt($("#hiddenshiptoindex").val()))
			   {

		  		// alert( parseInt($("#hiddenshiptoindex").val()));
		  		 updateShipToAddressValue(aVePOID, 1,"Add",1);
		   } 
		}
	}



function savePo(){
	$("#ButtonClicked").prop('checked', true);
	savePORelease();
}
function outsidepoEmailButtonAction(){
	
	 
	var vePoId = $("#vePOID").val();
	var aContactID = $("#contactId").val();
	//alert(aContactID);
	var errorText = '';
	
	if($('#POtransactionStatus').val() == '-1'){
		errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	return false;
	}
	else if($('#POtransactionStatus').val() == '0'){
			 errorText = "You can not Send E-Mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	 else if($('#POtransactionStatus').val() == '2'){
		 errorText = "You can not Send E-Mail, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	else{
	var cusotmerPONumber = $("#ourPoId").val();
	if(vePoId === null || vePoId.length<= 0){
		errorText = "Please Save Purchase Order to Email Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aContactID === null && aContactID === '' && aContactID === '-1'){
		errorText = "Please Add Contact for Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{

		purchaseorderpdfwrite();
		
		}
} 
}

function purchaseorderpdfwrite(){
	try{
		 var vePOID = $("#vePOID").val();
			if(vePOID === null || vePOID.length<= 0){
				errorText = "Please Save Purchase Order to View PDF.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToID}";
			var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
			var joReleaseId = "";//"${theVepo.joReleaseId}";
			var jobNumber = "";
			var joMasterID=0;
			console.log($("#jobNumber_ID").text().trim());
			if($("#jobNumber_ID").text().trim() != null && $("#jobNumber_ID").text().trim()!= ""){
				jobNumber = $("#jobNumber_ID").text().trim()
			}
			var rxCustomerId = $("#rxCustomer_ID").text();
		$.ajax({
			type : "GET",
			url : "./purchasePDFController/viewPDFLineItemForm",
			data : {'joMasterID':joMasterID, 'vePOID' : vePOID, 'puchaseOrder' : '', 'jobNumber' :  jobNumber, 'rxMasterID' : rxCustomerId, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :'0' ,'WriteorView':'write'},
			documenttype: "application\pdf",
			async: false,
			cache: false,
			success : function (msg) {
				setemailpopupdetails();
				
			},
			error : function (msg) {}
		});
	 }catch(err){
		 alert(err.message);
	 }
	
	
}
function setemailpopupdetails(){
	
	clearemailattachmentForm();
	
	var cusotmerPONumber = $("#ourPoId").val();
	var vePoId = $("#vePOID").val();
	var aContactID = $("#contactId").val();
	var toemailaddress="";
	var fullname="";
	$.ajax({ 
		url: "./vendorscontroller/GetContactDetails",
		mType: "GET",
		async:false,
		data : { 'rxContactID' : aContactID},
		success: function(data){
			var aFirstname = data.firstName;
			var aLastname = data.lastName;
			toemailaddress = data.email;
			$("#etoaddr").val(toemailaddress);
		    fullname = aFirstname + ' '+aLastname;
		   // $('#loadingPODiv').css({"visibility": "visible"});
			
			
			
			
		}
	});
	$.ajax({ 
		url: "./vendorscontroller/GetFromAddressContactDetails",
		mType: "GET",
		async:false,
		data : { },
		success: function(data){
				$("#efromaddr").val(data.emailAddr);
				var ccaddr1=data.ccaddr1;
				var ccaddr2=data.ccaddr2;
				var ccaddr3=data.ccaddr3;
				var ccaddr4=data.ccaddr4;
		var ccaddress="";
		if(ccaddr1!=null && ccaddr1!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr1;
			}else{
				 ccaddress=ccaddress+","+ccaddr1;
			}
		}
		if(ccaddr2!=null && ccaddr2!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr2;
			}else{
				 ccaddress=ccaddress+","+ccaddr2;
			}
		}
		if(ccaddr3!=null && ccaddr3!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr3;
			}else{
				 ccaddress=ccaddress+","+ccaddr3;
			}
		}
		if(ccaddr4!=null && ccaddr4!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr4;
			}else{
				 ccaddress=ccaddress+","+ccaddr4;
			}
		}
		$("#eccaddr").val(ccaddress);
		$("#esubj").val("Purchase Order # "+cusotmerPONumber);
		$("#filelabelname").text("PurchaseOrder_"+cusotmerPONumber+".pdf");
		$('#emailpopup').data('type', "DialogPO");
		$("#emailpopup" ).dialog("open");
		}
	});
}

function sendsubmitMailFunction(){
	var vePoId = $("#vePOID").val();
	var cusotmerPONumber = $("#ourPoId").val();
	var aContactID = $("#contactId").val();
	var toemailAddress=$("#etoaddr").val();
	var ccaddress=$("#eccaddr").val();
	var subject=$("#esubj").val();
	var filename="PurchaseOrder_"+cusotmerPONumber+".pdf";
	//var content=$("#econt").val();
	var content=$('#emailform').find('.nicEdit-main').html();
	//content=$('#emailform').find('.nicEdit-main').html();
	//content=$(".nicEdit-main").html();
	$.ajax({ 
		url: "./sendMailServer/sendOutsitePurchaseOrderMail",
		mType: "POST",
		data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
				'toAddress':toemailAddress, 'subject':subject,
				'filename':filename,'ccaddress':ccaddress,
				'content':content
			},
		success: function(data){
			//$('#loadingPOGenDiv').css({"visibility": "hidden"});
			//$('#loadingPODiv').css({"visibility": "hidden"});
			var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
			if(data){
				errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
				}
			jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
				buttons: [{height:35,text: "OK",click: function() {
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; 
					var yyyy = today.getFullYear().toString().substr(2,2);
					var hours = today.getHours();
					var minutes = today.getMinutes();
					var ampm = hours >= 12 ? 'PM' : 'AM';
					hours = hours % 12;
					hours = hours ? hours : 12;
					if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
					if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
					$.ajax({ 
						url: "./jobtabs3/updateEmailStampValue",
						mType: "GET",
						data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
						success: function(data){ 
							$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
							$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); 
							},error:function(e){
								 $('#loadingDivForPO').css({
										"visibility" : "hidden","display": "none"
									}); 
								}
					});
					 $('#loadingDivForPO').css({
							"visibility" : "hidden","display": "none"
						}); 
					
					$(this).dialog("close"); 
					$("#emailpopup" ).dialog("close");
				}}] }).dialog("open");
		},error:function(e){
			 $('#loadingDivForPO').css({
					"visibility" : "hidden","display": "none"
				}); 
			}
	});	
	
}
 /* $(function(){
	$("#emailpopup" ).dialog({
		autoOpen: false,
		height: 570,
		width: 600,
		modal: true,
		buttons: {
					
			Send:function(){ 

				alert("Test"); 

				 $('#loadingDiv').css({
						"visibility" : "hidden"
					}); 		

				 if($("#vePOID").val()!="")
				 sendsubmitMailFunction();
				 else
				 sendsubmitMailFunction2();
	   		 },	
		
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},	
	});

	});  */


	function loaddropshiptaxterritory(taxid,Type){
		if(Type=="JobSite"){
			 taxid="${requestScope.joMasterDetails.coTaxTerritoryId}";
		}
		if(taxid != null && taxid != '')
		{
			$.ajax({
				url: "./salesOrderController/taxRateTerritory",
				type: "POST",
				data : {"coTaxTerritoryId" : taxid},
				success: function(data) {
					/* alert(data.county);
					alert(data.taxRate);
					alert(data.coTaxTerritoryId); */
					
					$("#taxGeneralId").val(formatCurrencynodollar(data.taxRate));
					$("#taxLineId").prop("disabled",false);
					$("#taxLineId").val(formatCurrencynodollar(data.taxRate));
					$("#taxLineId").prop("disabled",true);
					
				}
		});

	}

	}	
</script>
