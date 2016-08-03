
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SO_General.js"></script>	
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/releaseemail.js"></script>
<div id="soreleasegeneral">
<!-- <form id="sogeneralForm">    -->
<input type="hidden" id="so_taxfreight" value="0" />
	<table>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
							<legend class="custom_legend"><label><b>Customer </b></label></legend>
								<label>Customer Name :</label> <input type="text" id="CustomerNameGeneral" onchange="soGeneralformChanges()"/>
								<input type="hidden" disabled="disabled" id="billToCustomerNameGeneralID" />
								<input type="hidden" disabled="disabled" id="transactionStatus" />
								<input type="hidden"  id="cuSOid" name="cuSOid"/>
								
							</fieldset>
						</td>
						<td>
						<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
						<legend class="custom_legend"><label><b>SO Details</b></label></legend>
							<label>Date:</label><input type="text"  id="dateOfcustomerGeneral" value="" style="width: 85px;margin-left: 5px;" onchange="soGeneralformChanges()"/>
							<label>Number</label><input type="text" disabled="disabled" id="SOnumberGeneral" style="width: 126px; margin-left: 5px;" onchange="soGeneralformChanges()"/>
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
							<fieldset class= "custom_fieldset" style="height: 160px;width: 350px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table>
										<tr>
					 						<td><input type="text" id="SOlocationbillToAddressname" name="locationbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" onchange="soGeneralformChanges()"></td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="SOGenerallocationbillToAddressID1" name="locationbillToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" onchange="soGeneralformChanges()"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="SOGenerallocationbillToAddressID2" name="locationbillToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled" onchange="soGeneralformChanges()"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="SOGenerallocationbillToCity" name="locationbillToCity" style="width: 100px;" disabled="disabled" onchange="soGeneralformChanges()">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="SOGenerallocationbillToState" name="locationbillToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled" onchange="soGeneralformChanges()">
					 								<label>Zip: </label><input type="text" id="SOGenerallocationbillToZipID" name="locationbillToZip" style="width: 75px;" disabled="disabled">
					 								<!-- <input type="button" id="SObillbackWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="SObillforWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;"> -->
					 						</td>
					 					</tr>
					 					<tr></tr>
					 					<tr>
					 						<td><label style="display:none;">Select Email: </label>
					 						<select id="emailList" name="emailList" style="width: 245px;display:none;" onchange="soGeneralformChanges()">
														<option value="0"> -Select- </option>														
													</select></td>												
					 					</tr>
					 				</table>
							</fieldset>
						</td>
						<td>
							<div id="SO_Shipto">
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
							<fieldset class=" custom_fieldset" style="height: 160px;width: 350px;">
								<legend class="custom_legend"><label><b>Employee Assigned :</b></label></legend>
								<table>
									<tr id="CustomerCategory1row">
										<td style="width: 99px"><label id="CustomerCategory1label">SalesMan:</label></td>
										<td><input type="text" style="width: 190px;" id="salesmanID" name="salesName" value="" placeholder="Minimun 2 character to search" onchange="soGeneralformChanges()"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										</td>
										<td><input type="hidden" style="width: 50px;" id="salesmanhiddenID" name="saleshiddenName" value="${requestScope.joMasterDetail.cuAssignmentId0}">
										</td>
									</tr>
									<tr id="CustomerCategory2row">
							         	<td style="width: 99px"><label id="CustomerCategory2label">AE:</label></td><td><input type="text" style="width: 190px;" id="csrID" name="csrName" value="${requestScope.CSRs}" placeholder="Minimun 2 character to search" onchange="soGeneralformChanges()"></td>
							         	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
							         	</td>
							         	<td><input type="hidden" style="width: 50px;" id="csrhiddenID" name="csrhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId1}"></td>
							         </tr>
							         <tr id="CustomerCategory3row">
										<td style="width: 99px"><label id="CustomerCategory3label">Costing:</label></td>
										<td><input type="text" style="width: 190px;" id="salesManagerID" name="salesManagerName" value="${requestScope.SalesMGRs}" placeholder="Minimun 2 character to search" onchange="soGeneralformChanges()"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										</td>
										<td><input type="hidden" style="width: 50px;" id="salesManagerhiddenID" name="salesManagerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId2}">
										</td>
									</tr>
									<tr id="CustomerCategory4row">
								     	<td style="width: 99px"><label id="CustomerCategory4label">Submitting:</label></td>
								     	<td><input type="text" style="width: 190px;" id="engineerID" name="engineerName" value="${requestScope.Engineers}" placeholder="Minimun 2 character to search" onchange="soGeneralformChanges()"></td>
								     	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
								     	</td>
								     	<td><input type="hidden" style="width: 50px;" id="engineerhiddenID" name="engineerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId3}">
								     	</td>
								     </tr>
								     <tr id="CustomerCategory5row">
										<td style="width: 99px"><label id="CustomerCategory5label">Ordering:</label></td>
										<td><input type="text" style="width: 190px;" id="projectManagerID" name="projectManagerName" value="${requestScope.ProjManagers}" placeholder="Minimun 2 character to search" disabled="disabled" onchange="soGeneralformChanges()"></td>
										<td>
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long"></td>
										<td><input type="hidden" style="width: 50px;" id="projectManagerhiddenID" name="projectManagerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId4}">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "custom_fieldset" style="height: 160px;width: 350px;">
							<legend class="custom_legend"><label><b>Shipping Details:</b></label></legend>
								<table>
									<tr>
										<td>
											<table style="text-align: right;">
											<tr>
												<td><label>Warehouse: </label></td>
												<td align="left">
													<select id="whrhouseID" name="whrhouseName" style="width: 170px;" onchange="soGeneralformChanges()">
														<!-- <option value="0"> -Select- </option> -->
														<c:forEach var="wareHouseBean" items="${requestScope.wareHouse}">
															<option value="${wareHouseBean.prWarehouseId}">
																<c:out value="${wareHouseBean.searchName}"></c:out>
															</option>
														</c:forEach>
													</select>
												</td>
											</tr>
												<tr>
													<td><label>Via:</label></td>
													<td align="left">
													<select style="width:120px;" id="SOshipViaId" name="shipViaName" onchange="soGeneralformChanges()">
														<option value="0"> - Select - </option>
															<c:forEach var="shipViaBean" items="${requestScope.shipVia}">
																<option value="${shipViaBean.veShipViaId}">
																	<c:out value="${shipViaBean.description}" ></c:out>
																</option>
														</c:forEach>
												</select>
												</tr>
												<tr>
													<td align="right"><label>Pro #:</label></td>
													<td align="left"><input type="text" style="width:120px" id="poID" name="poName" onblur="soGeneralformChanges()"></td>
												</tr>
												<tr>
													<td align="right"><label>Shipping Date :<!-- font color="red">*</font--></label></td>
													<td align="left"><input type="text" style="width:120px" id="SOShipDate" name="ShipDate" onchange="soGeneralformChanges()"></td>
												</tr>
												<tr><td colspan="2"><div id="soShippingDate" style="color: red;"></div></td></tr>
											</table>
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
		<div id='splitDivs'>
			<table width="100%">
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="height: 195px;width: 320px;">
								<legend class="custom_legend"><label><b>Split Commission</b></label><span id="splitCommissionLabel" style="display: none;color: red;">*</span></legend>
								<table>
									<tr>
										<td>
										<span id="SoSplitCommission">
										<table id="SoSplitCommissionGrid"></table>
										<div id="SoSplitCommissionGridPager"></div>
										</span>
										</td>
								</table>
						</fieldset>
					</td>
					<td>
						<fieldset class=" custom_fieldset" style="height: 195px;width: 195px;">
							<legend class="custom_legend"><label><b>Tax Territory:</b></label></legend>
							<table style="text-align: right; width: 270px;">
								<tr>
									<td>
										<label>Tax Territory :</label>	</td><td>
										<input type="text" style="width: 126px;position:relative;" id="taxID" name="taxName" value="${requestScope.taxTerritoryID}">
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" onchange="soGeneralformChanges()">
										<input type="hidden" style="width: 126px;vertical-align: middle;" id="taxhiddenID" name="taxhiddenName" 
												value="${requestScope.joMasterDetail.coTaxTerritoryId}">
									</td>
								</tr>
								<tr>
									<td>
										<label>Terms:</label></td><td>
										<select id="terms" name="terms" style="width: 138px;" onchange="soGeneralformChanges()">
										<option value="-1">-Select-</option>
										<c:forEach var="cuTermsBean" items="${requestScope.cuTerms}">
										<option value="${cuTermsBean.cuTermsId}">
										<c:out value="${cuTermsBean.description}"></c:out>
										</option>
										</c:forEach> 
										</select>
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										<input type="hidden" style="width: 50px;vertical-align: middle;" id="termhiddenID" 
												name="termhiddenName" value="">
								</td>
								</tr>
								<tr align="left">
									<td>
										<label>Customer Po #:</label></td><td>
										<input type="text" style="width: 144px;" id="custID" name="CustomrPO" value="" onchange="soGeneralformChanges()">
									</td>
								</tr>
								<tr>
									<td>
										<label style="vertical-align: top;">Tag:</label></td><td>
										<input type="text" id="tagJobID" name="tagJobName" style="width:144px" onchange="soGeneralformChanges()">
									</td>
								</tr>
								<tr>
									<td>
										<label>Division: <span  id="divisionlabel" style="display: none;color: red;">*</span>
										</label></td><td>
												<select style="width: 155px;" id="SOdivisionID" name="divisionName" onchange="soGeneralformChanges()">
													<option value="-1"> - Select - </option>
														<c:forEach var="coDivisionBean" items="${requestScope.coDivision}">
															<option value="${coDivisionBean.coDivisionId}">
																	<c:out value="${coDivisionBean.description}" ></c:out>
															</option>
													</c:forEach>
											</select>
									</td>
								</tr>
								<tr>
									<td>
										<label>Promised:<font color="red">*</font></label></td><td>
										<input type="text" style="width:144px" id="promisedID" name="promiseName" onchange="soGeneralformChanges()"><br>
									</td>
								</tr>
								<tr>
									
									<td colspan = '2''><div id="soPromisedId" style="color: red;"></div></td>
								</tr>
								<tr><td colspan = '2'><div id="soDivisionId" style="color: red;"></div></td></tr>
							</table>
						</fieldset>
					</td>
		   		</tr>
		   	</table>
		   	</div>
		   	<div id='OriginalData'>
		   		<table>
				<tr>
					<td>
						<fieldset class=" custom_fieldset" style="height: 136px;width: 723px;">
							<legend class="custom_legend"><label><b>Tax Territory:</b></label></legend>
							<table style="text-align: right;">
								<tr>
									<td>
									<label>Tax Territory :</label>	
										<input type="text" style="width: 137px;position:relative;margin-left: 15px; margin-right: 7px;middle;" id="taxIDwz" name="taxName" value="${requestScope.taxTerritoryID}">
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long" onchange="soGeneralformChanges()">
										<input type="hidden" style="width: 70px;vertical-align: middle;" id="taxhiddenIDwz" name="taxhiddenName" 
												value="${requestScope.joMasterDetail.coTaxTerritoryId}">
									</td>
									<td style="padding-right: 8px;">
										<label>Terms:&nbsp;&nbsp;&nbsp;&nbsp;</label>
										<select id="termswz" name="terms" style="width: 207px;" onchange="soGeneralformChanges()">
										<option value="-1">-Select-</option>
										<c:forEach var="cuTermsBean" items="${requestScope.cuTerms}">
										<option value="${cuTermsBean.cuTermsId}">
										<c:out value="${cuTermsBean.description}"></c:out>
										</option>
										</c:forEach> 
										</select>
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										<input type="hidden" style="width: 50px;vertical-align: middle;" id="termhiddenIDwz" 
												name="termhiddenName" value="">
								</td>
								</tr>
								<tr align="left">
									<td>
										<label>Customer Po #:</label>
										<input type="text" style="width: 137px;margin-left: 9px;" id="custIDwz" name="CustomrPO" value="" onchange="soGeneralformChanges()">
									</td>
									<td><label style="vertical-align: top;margin-left: 140px;">Tag:</label>
												<input type="text" id="tagJobIDwz" name="tagJobName" style="width:219px" onchange="soGeneralformChanges()">
										</td>
								</tr>
								<tr>
									<td>
										<label style="margin-right: 41px;">Division: <span  id="divisionlabelwz" style="display: none;color: red;">*</span>
										</label>
												<select style="width: 165px; margin-left: 7px;" id="SOdivisionIDwz" name="divisionName" onchange="soGeneralformChanges()">
													<option value="-1"> - Select - </option>
														<c:forEach var="coDivisionBean" items="${requestScope.coDivision}">
															<option value="${coDivisionBean.coDivisionId}">
																	<c:out value="${coDivisionBean.description}" ></c:out>
															</option>
													</c:forEach>
											</select>
									</td>
									<td><label>Promised:<font color="red">*</font></label>
										<input type="text" style="width:220px" id="promisedIDwz" name="promiseName" onchange="soGeneralformChanges()"><br>
										
									</td>
								</tr>
								<tr>
									<td><div id="soDivisionIdwz" style="color: red;"></div></td>
									<td><div id="soPromisedIdwz" style="color: red;"></div></td>
								</tr>
							</table>
						</fieldset>
					</td>
		   		</tr>
		   	</table>
		   	</div>
		</td>
	</tr>
	<tr>
		<td>
			<fieldset class= "custom_fieldset" style="width:729px">
				<legend class="custom_legend"><label><b>Totals</b></label></legend>
   				<table style="width:700px">
   					<tr align="center">
			   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="SOGeneral_subTotalID" name="customerInvoice_subTotalName" disabled="disabled" value="$0.00" onchange="soGeneralformChanges()"></td>
			       		<td><label>Freight:</label></td><td><input type="text" style="width:85px" id="SOGeneral_frightID" name="customerInvoice_frightname" value="$0.00" onchange="setTaxTotal_SO();soGeneralformChanges();"></td>
			   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="SOGeneral_taxId" name="customerInvoice_taxName" value="${requestScope.taxRate}" disabled="disabled" onchange="soGeneralformChanges()"></td><td>%</td>
			   			<td><label></label></td><td><input type="text" style="width:85px" id="SOGeneral_taxvalue" name="customerInvoice_taxName" value="$0.00" disabled="disabled" onchange="soGeneralformChanges()"></td>
			       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="SOGeneral_totalID" name="customerInvoice_totalName" disabled="disabled" value="$0.00" onchange="soGeneralformChanges()"></td>
      	 			</tr>
 				</table>
			</fieldset>
		</td>
	</tr>
	<tr>
	<td><div id="showMessageSO" style="color: green;margin-left: 90%;margin-bottom: 0%;"></div></td>
	</tr>
	<tr>
	<td style="width:729px">
	<table >
	<tr style="width: 100%;">
	<td align="left" style="padding-right: 0px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Sales Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
	<td align="left" style="padding-right: 0px;">
	<td><input id="contactEmailID_general"  onmouseover="triggertitle()"  type="image" src="./../resources/Icons/mail_new.png" title="Email Sales Order" style="background: #EEDEBC;" onclick="sendPOEmail('SOJ')"></td>
	<td align="left" style="padding-left: 10px;"><input id="withPrice" type="checkbox" style="background: #EEDEBC;" onclick="general(this);"></td>
	<td width="30%"><label id='withPriceLabel' display: none;">With Price</label></td>
	<td width="40%"><font color="green" size="3"><div id="soEmailTimeStamp"></div></font> </td>
	
	<td align="right">
	<input type="button" id="SavePOReleaseID" class="cancelhoverbutton turbo-tan" value="Save" onclick="saveSORelease()" style="width:60px;position: relative;"></td>
	<td align="right"><input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Close" onclick="closeSOGeneralItemTab()" style="width:120px;position: relative;"></td>
	<td align="right"><input type="text" id="setButtonValue" style="display: none;" value="" >
	</td>
	</tr>
	</table>
	</td>
	</tr>
<!-- </table> -->
</form>
</div>	
</div>
<input id="chkUserCustomerCreditYN" type="hidden" value="${requestScope.chkUserCustomerCreditYN }"/>
<input id="chk_cusReqDivinSalOrdYes" type="hidden" value="${requestScope.chk_cusReqDivinSalOrdYes }"/>
<input id="CreditHold" type="hidden"/>
<input type="text" id="joMasterID" style="display: none;" value="${requestScope.joMasterDetail.joMasterId}" >
<input type="text" id="joReleaseID" style="display: none;" value="${requestScope.CuSoDetail.joReleaseId}" >
<div id="sogeneralAlertDiv" >
	<table style="display: none;">
	        <tr>
	        	<td>
	        		<div>Customer is on Hold. You must take off Hold in order to proceed.</div>
	        	</td>
			</tr>					
	</table>
</div>
<div id="salesTemplateDiv" style="display:none">	
<select id="templateListId" name="salesTemplate" style="width: 200px;">
</select>

</div>

	<script type="text/javascript">
	
	
	 
var billToOtherName,billtoOtherAddress1,billtoOtherAddress2,billToOtherCity,billToOtherState,billToOtherZip,shipToOtherName,shiptoOtherAddress1,shiptoOtherAddress2,shipToOtherCity,shipToOtherState,shipToOtherZip;
var onChangeOther = 1;
var onChangeShipToOther = 1;
//var taxRateShipTo = 0;
var taxRateOther = 0;
date = new Date();
var month = date.getMonth()+1;
var day = date.getDate();
var year = date.getFullYear();
var CushipAddresses = []; 
var subTotal = formatCurrency("${requestScope.CuSoDetail.subTotal}");
var freightTotal = formatCurrency("${requestScope.CuSoDetail.freight}");
var taxTotal = formatCurrency("${requestScope.CuSoDetail.taxTotal}");
var taxRate = formatCurrency("${requestScope.CuSoDetail.taxRate}");
var taxSub = formatCurrency("");
var newcusoID;

if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
	newcusoID = "${requestScope.cusoID}";

	}
console.log('WarehouseID-JSP:'+ "${requestScope.prTOWarehouseID}");
console.log('ShipTOMode-JSP:'+ "${requestScope.CuSoDetail.shipToMode}");

$("#subtotalId").val(subTotal);
$("#freightId").val(freightTotal);
$("#totalId").val(taxTotal);
$("#taxId").val(taxRate);
$("#taxIdwz").val(taxRate);
$("#subID").val(taxSub);

$("#SOShiptoRadioSet").buttonset();
$("#SOShiptoRadioSet1").buttonset();
$('#SOforWardId').show();
$('#SObackWardId').show();



if($('#operation').val() === 'create' || $('#operation').val() === 'edit'){
loadShipAddress();
loadBillAddress();
}
billtoAddess();
/* if($('#operation').val() === 'create'){
	console.log('operation is crete:'+$('#operation').val());
	shipToAddressCreate('Pick Up');
	
 } */

 	/* var sizess = "${fn:length(requestScope.wareHouse)}";
	var wareHousesList; //create a new array global
		if(sizess > 0)
		{
		<c:forEach var="wareHouseBean" items="${requestScope.wareHouse}">
			wareHousesList+='<option value='+${wareHouseBean.prWarehouseId}+'>'+${wareHouseBean.searchName}+'</option>';
		</c:forEach>
			 
		$('#whrhouseID').html(ssEmail);
		}	
 */
 var aDivision = "${requestScope.defaultWHID}";
 $("#whrhouseID").val(aDivision);
 
 function billtoAddess(){
console.log('Customer ID:'+$("#billToCustomerNameGeneralID").val());
if($("#billToCustomerNameGeneralID").val()!==''){
 $.ajax({
     url: 'rxdetailedviewtabs/SOAddressDetails?rxMasterId='+$("#billToCustomerNameGeneralID").val(),
     type: 'POST',       
     success: function (data) {
    	/*  $('#SOforWardId').show();
    	 $('#SObackWardId').show(); */
     	console.log("all--->"+data);
     	var billtocont=0;
     	var ssEmail = "";
			$.each(data, function(key, valueMap) {


				
				if("customerAddress"==key)
				{
					billToCustomer = valueMap;		
					$.each(valueMap, function(index, value){
						if(value.isBillTo==1){
									
							if(value.isMailing==1)
							{		
							$('#SOlocationbillToAddressname').val(value.name);
							$('#SOGenerallocationbillToAddressID1').val(value.address1);
							$('#SOGenerallocationbillToAddressID2').val(value.address2);
							$('#SOGenerallocationbillToCity').val(value.city);
							$('#SOGenerallocationbillToState').val(value.state);
							$('#SOGenerallocationbillToZipID').val(value.zip);
							//$('#customerBillToAddressID').val(value.rxAddressId);
							CustomerName = value.name;
							}
							else
							{
							$('#SOlocationbillToAddressname').val(value.name);
							}
						}
					
					});
				}
				if("emailList" == key)
				{
					customerMasterObj = valueMap;
					$.each(valueMap, function(index, value){
						if(value.email != null && value.email.trim() != '')
							ssEmail+='<option value='+value.rxContactId+'>'+value.email+'</option>';
					
					}); 
					$('#emailList').html(ssEmail);
				}
			});
     }
     });
}
 }
function loaddivision(){
	$.ajax({
	    url: './getUserDefaults',
	    type: 'POST',       
	    success: function (data) {
	    	var selected='';
	    	
			 $.each(data, function(key, valueMap) {
				if("divisions"==key)
				{
					console.log('new datd '+valueMap);
					$.each(valueMap, function(index, value){
//						//alert(data.divisionID +' :: '+value.coDivisionId);
						 if(data.divisionID==value.coDivisionId){
							 $("#SOdivisionID option[value=" + value.coDivisionId + "]").attr("selected", true);
						}
							 
						});
						}
					});  
				}
		});
		
}


function loadBillAddress()
{
	
	if(typeof(billToCustomer)  !== "undefined")
	{
		
		$.each(billToCustomer, function(index, value){					
			//$('#SOGeneral_taxId').val(formatCurrencynodollar(sTaxRate));
			$('#SOlocationbillToAddressname').val(value[0].name);
			$('#SOGenerallocationbillToAddressID1').val(value[0].address1);
			$('#SOGenerallocationbillToAddressID2').val(value[0].address2);
			$('#SOGenerallocationbillToCity').val(value[0].city);
			$('#SOGenerallocationbillToState').val(value[0].state);
			$('#SOGenerallocationbillToZipID').val(value[0].zip);
			$('#SOGeneral_taxId').val(Number(floorFigureoverall(taxRateShipTo, 2)));
			
			
		});
		
	}
	if(typeof(customerMasterObj)  !== "undefined")
	{
		$.each(customerMasterObj, function(index, value){
			$('#salesmanhiddenID').val(value.cuAssignmentId0);
			$('#csrhiddenID').val(value.cuAssignmentId1);
			$('#salesManagerhiddenID').val(value.cuAssignmentId2);
			$('#engineerhiddenID').val(value.cuAssignmentId3);
			$('#projectManagerhiddenID').val(value.cuAssignmentId4);
		
		});
	}

	if(typeof(CustomerName)  !== "undefined")
	{
		$('#CustomerNameGeneral').val(CustomerName);
	}

	if(typeof(taxRateShipTo)  !== "undefined")
	{
		$('#SOGeneral_taxId').val(taxRateShipTo);
	}

	if(typeof(billToCustomerNameGeneralID)  !== "undefined")
	{
		$('#billToCustomerNameGeneralID').val(billToCustomerNameGeneralID);
	}

	if(typeof(shipTorxCustomer_ID)  !== "undefined")
	{
		$('#shipTorxCustomer_ID').val(shipTorxCustomer_ID);
	}
	if(typeof(taxTerritory)  !== "undefined")
	{
		$('#taxID').val(taxTerritory);
		$('#taxIDwz').val(taxTerritory);
	}
	if(typeof(taxTerritoryhiddenID)  !== "undefined")
	{
		$('#taxhiddenID').val(taxTerritoryhiddenID);
		$('#taxhiddenIDwz').val(taxTerritoryhiddenID);
	}
}
var shipToIndex =0;
function loadShipAddress()
{
     $("#SOshiptolabel1").removeClass('ui-state-active');
	 $("#SOshiptolabel2").removeClass('ui-state-active');
	 $("#SOshiptolabel3").removeClass('ui-state-active');
	 $("#SOshiptolabel4").removeClass('ui-state-active');
	 shipToIndex = "${requestScope.CuSoDetail.shipToMode}";	
	
	 console.log('Ship TO Mode: '+shipToIndex);	 
	 if(shipToIndex === 0)
		 {
		 $("#SOshiptolabel1").addClass('ui-state-active');
		 shipToAddressCreate('Pick Up');
		 }
	 else if(shipToIndex === 1)
		 {
		$("#SOshiptolabel2").addClass('ui-state-active');
		 shipToAddressCreate('Customer');
		 }
	 else if(shipToIndex === 3)
	 {
		$("#SOshiptolabel4").addClass('ui-state-active');
		 shipToAddressCreate('Other');
		 }
	 else if(shipToIndex === 2)
	 {
		$("#SOshiptolabel3").addClass('ui-state-active');
		 shipToAddressCreate('Job Site');
	}		 
}

function  shipToAddressCreate(type)
{
	$('#SOforWardId').hide();
	$('#SObackWardId').hide();
	 $("#shiptomodefortoggle").val(type);
	 
	 	var imgUrlBackwards = "./../resources/images/Arrowleft.png";
		$('#SObackWardId').css('background', 'url('+imgUrlBackwards+') no-repeat');
		$('#SObackWardId').css('background-position','center');
		var imgUrlforwards = "./../resources/images/Arrowright.png";
		$('#SOforWardId').css('background', 'url('+imgUrlforwards+') no-repeat');
		$('#SOforWardId').css('background-position','center');
	
	if(type === 'Pick Up') 
	{	
		$('#customerShipToOtherID').val('Pick Up');
		console.log('SO_General.jsp # Pick Up');
		if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){
			
			$('#regularTable').show();
			$('#hiddenTable').hide();
			var size = "${fn:length(wareHouse)}";
			$("#SOlocationShipToAddressID").attr("disabled",true);
			 $("#SOlocationShipToAddressID1").attr("disabled",true);
			 $("#SOlocationShipToAddressID2").attr("disabled",true);
			 $("#SOlocationShipToCity").attr("disabled",true);
			 $("#SOlocationShipToState").attr("disabled",true);
			 $("#SOlocationShipToZipID").attr("disabled",true);
			$('#customerShipToOtherID').val("Pick Up");
			if(size > 0)
				{
					if(size == 1)
					{
						$('#SOforWardId').hide();
						$('#SObackWardId').hide();
					}
					else
					{
						$('#SOforWardId').show();
						$('#SObackWardId').show();
					}

					$('#SOforWardId').show();
					$('#SObackWardId').show();			
				
				var prWareHouseIDs = $('#prToWarehouseId').val();
				
			 	console.log($('#prToWarehouseId').val());
				if(prWareHouseIDs!= null && prWareHouseIDs !=''){

					$.ajax({
						url: "./wareHouseDetail",
						type: "POST",
						data : {"warehouseID" : prWareHouseIDs},
						success: function(data) {
							var warehouse = data.wareHouseDetail.description;
							$('#SOlocationShipToAddressID').val(data.wareHouseDetail.description);
							$('#SOlocationShipToAddressID1').val(data.wareHouseDetail.address1);
							$('#SOlocationShipToAddressID2').val(data.wareHouseDetail.address2);
							$('#SOlocationShipToCity').val(data.wareHouseDetail.city);
							$('#SOlocationShipToState').val(data.wareHouseDetail.state);
							$('#SOlocationShipToZipID').val(data.wareHouseDetail.zip);
							var sTaxRate = data.wareHouseDetail.email;
							var coTaxTerritoryId = data.wareHouseDetail.coTaxTerritoryId;
							$('#prWarehouseID').val(data.wareHouseDetail.prWarehouseId);
							$('#prToWarehouseId').val(data.wareHouseDetail.prWarehouseId);
							loadTaxTerritoryRate(coTaxTerritoryId,"Pick Up");
						}
				});
				}else{
					
							var warehouse = "${wareHouse[0].description}";
							$('#SOlocationShipToAddressID').val("${wareHouse[0].description}");
							$('#SOlocationShipToAddressID1').val("${wareHouse[0].address1}");
							$('#SOlocationShipToAddressID2').val("${wareHouse[0].address2}");
							$('#SOlocationShipToCity').val("${wareHouse[0].city}");
							$('#SOlocationShipToState').val("${wareHouse[0].state}");
							$('#SOlocationShipToZipID').val("${wareHouse[0].zip}");
							var sTaxRate = "${wareHouse[0].email}";
							$('#prWarehouseID').val("${wareHouse[0].prWarehouseId}");
							$('#prToWarehouseId').val("${wareHouse[0].prWarehouseId}");
							$('#SOGeneral_taxId').val(Number(floorFigureoverall(sTaxRate, 2)));
							var coTaxTerritoryId = "${prWareHouse[0].coTaxTerritoryId}";
							loadTaxTerritoryRate(coTaxTerritoryId,"Pick Up");
				}
							
				}
			else
			{
				$('#SOforWardId').hide();
				$('#SObackWardId').hide();
			}
			 $("#SOshiptoradio01").attr("Checked",true);
			 $("#SOshiptoradio02").attr("checked",false);
			 $("#SOshiptoradio03").attr("checked",false);
			 $("#SOshiptoradio04").attr("checked",false);
			$('#SOshiptolabel1').addClass('ui-state-active');
			$('#SOshiptolabel2').removeClass('ui-state-active');
			$('#SOshiptolabel3').removeClass('ui-state-active');
			$('#SOshiptolabel4').removeClass('ui-state-active');
		}
		else
		{
			loadPickUpAddress();
		}
	}
	
	if(type == 'Customer')
	{
		console.log('SO_General.jsp # Customer');
		$('#SOcustomerforWardId').hide();
		$('#SOcustomerbackWardId').hide();
		$('#SOforWardId').show();
		$('#SObackWardId').show();		
		$('#customerShipToOtherID').val("Customer");


		$.ajax({
			url: "./salesOrderController/getCustomerShipToAddressforToggle",
			type: "GET",
			data : {"customerID" : $("#shipTorxCustomer_ID").val()},
			success: function(data) {
				CushipAddresses = data;
				
			}
		});


		if($('#operation').val() === 'create')
			{
				$('#regularTable').show();
				$('#hiddenTable').hide();
				
				$("#SOlocationShipToAddressID").attr("disabled",false);
				if(typeof(shipToCustomer)  !== "undefined")
				{
					$.each(shipToCustomer, function(index, value){					
						$('#SOlocationShipToAddressID').val(value.name);
						$('#SOlocationShipToAddressID1').val(value.address1);
						$('#SOlocationShipToAddressID2').val(value.address2);
						$('#SOlocationShipToCity').val(value.city);
						$('#SOlocationShipToState').val(value.state);
						$('#SOlocationShipToZipID').val(value.zip);
						$('#SOGeneral_taxId').val(Number(floorFigureoverall(taxRateShipTo, 2)));
					});
					
				}
				else
				{
					$('#SOlocationShipToAddressID').val("");
					$('#SOlocationShipToAddressID1').val("");
					$('#SOlocationShipToAddressID2').val("");
					$('#SOlocationShipToCity').val("");
					$('#SOlocationShipToState').val("");
					$('#SOlocationShipToZipID').val("");
				} 
	
				if(typeof(taxTerritory)  !== "undefined")
				{
					$('#taxID').val(taxTerritory);
					$('#taxIDwz').val(taxTerritory);
				}
				if(typeof(taxTerritoryhiddenID)  !== "undefined")
				{
					$('#taxhiddenID').val(taxTerritoryhiddenID);
					$('#taxhiddenIDwz').val(taxTerritoryhiddenID);
				}			
				
				$('#SOlocationShipToAddressID').focus(function(){
			        $(this).data('placeholder',$(this).attr('placeholder'))
			        $(this).attr('placeholder','Minimum 1 character required to get Name List');
			     });
		}
		else
		{
			$('#SOlocationShipToAddressID').val("");
			$('#SOlocationShipToAddressID1').val("");
			$('#SOlocationShipToAddressID2').val("");
			$('#SOlocationShipToCity').val("");
			$('#SOlocationShipToState').val("");
			$('#SOlocationShipToZipID').val("");
			
			ShipToCustomerAddress();
	 	}
		 $("#SOshiptoradio02").attr("Checked",true);
		 $("#SOshiptoradio01").attr("checked",false);
		 $("#SOshiptoradio03").attr("checked",false);
		 $("#SOshiptoradio04").attr("checked",false);
			$('#SOshiptolabel2').addClass('ui-state-active');
			$('#SOshiptolabel1').removeClass('ui-state-active');
			$('#SOshiptolabel3').removeClass('ui-state-active');
			$('#SOshiptolabel4').removeClass('ui-state-active');
			$('#SOlocationShipToAddressID').focus();
	}
	if(type === 'Other')
	{
		console.log('SO_General.jsp # Other');
		$('#SOforWardId').hide();
		$('#SObackWardId').hide();
		//alert("hi");
		$('#customerShipToOtherID').val("Other");
		 
		if($('#operation').val() === 'create' ){
			$('#regularTable').hide();
			$('#hiddenTable').show();			
			$('#SOlocationShipToAddressIDeditable').focus(function(){
		        $(this).data('placeholder',$(this).attr('placeholder'))
		        $(this).attr('placeholder','');
		     });
			$('#SOGeneral_taxId').val(Number(floorFigureoverall(0.0, 2)));
			$('#customerShipToOtherID').val("Other");
			$('#customerShipToOtherIDeditable').val("Other");
			//$('#customerShipToID').val("");
			$("#SOlocationShipToAddressIDeditable").attr("disabled",false);
			 $("#SOlocationShipToAddressID1editable").attr("disabled",false);
			 $("#SOlocationShipToAddressID2editable").attr("disabled",false);
			 $("#SOlocationShipToCityeditable").attr("disabled",false);
			 $("#SOlocationShipToStateeditable").attr("disabled",false);
			 $("#SOlocationShipToZipIDeditable").attr("disabled",false);
			 	$('#SOlocationShipToAddressIDeditable').focus();
			 	loadTaxTerritoryRate("","Other");
		}   
		else
		{
			
			$('#customerShipToOtherIDeditable').val("Other");
			 $("#SOlocationShipToAddressIDeditable").attr("disabled",false);
			 $("#SOlocationShipToAddressID1editable").attr("disabled",false);
			 $("#SOlocationShipToAddressID2editable").attr("disabled",false);
			 $("#SOlocationShipToCityeditable").attr("disabled",false);
			 $("#SOlocationShipToStateeditable").attr("disabled",false);
			 $("#SOlocationShipToZipIDeditable").attr("disabled",false);

			 $("#SOlocationShipToAddressIDeditable").val("");
			 $("#SOlocationShipToAddressID1editable").val("");
			 $("#SOlocationShipToAddressID2editable").val("");
			 $("#SOlocationShipToCityeditable").val("");
			 $("#SOlocationShipToStateeditable").val("");
			 $("#SOlocationShipToZipIDeditable").val("");
			 
			 otherShiptoAddress();
		}
		 $("#SOshiptoradio04").attr("Checked",true);
		 $("#SOshiptoradio01").attr("checked",false);
		 $("#SOshiptoradio02").attr("checked",false);
		 $("#SOshiptoradio03").attr("checked",false);
			 $('#SOshiptolabel4').addClass('ui-state-active');
			 $('#SOshiptolabel1').removeClass('ui-state-active');
			 $('#SOshiptolabel2').removeClass('ui-state-active');
			 $('#SOshiptolabel3').removeClass('ui-state-active');
		loadTaxTerritoryRate("","Other");
		 
	}
	if(type === 'Job Site')
	{
		$('#customerShipToOtherID').val("Job Site");
		$('#SOforWardId').hide();
		$('#SObackWardId').hide();
		
		 $("#SOshiptoradio03").attr("Checked",true);
		 $("#SOshiptoradio01").attr("checked",false);
		 $("#SOshiptoradio02").attr("checked",false);
		 $("#SOshiptoradio04").attr("checked",false);
		 
			 $('#SOshiptolabel3').addClass('ui-state-active');
			 $('#SOshiptolabel1').removeClass('ui-state-active');
			 $('#SOshiptolabel2').removeClass('ui-state-active');
			 $('#SOshiptolabel4').removeClass('ui-state-active');
			 $('#SOlocationShipToAddressID').focus();
			console.log('SO_General.jsp # Job Site');
			if($('#operation').val() === 'create'){
			$('#regularTable').show();
			$('#hiddenTable').hide();
			$('#customerShipToOtherID').val("Job Site");
			$("#SOlocationShipToAddressID").attr("disabled",false);
			ShipToJobSite();

			//shiptoJobsitefromOutside();
			
			}
			else{
			 $("#SOshiptoradio03").attr("Checked",true);
			 $("#SOshiptoradio01").attr("checked",false);
			 $("#SOshiptoradio02").attr("checked",false);
			 $("#SOshiptoradio04").attr("checked",false);
			 
			 $('#SOshiptolabel3').addClass('ui-state-active');
			 $('#SOshiptolabel1').removeClass('ui-state-active');
			 $('#SOshiptolabel2').removeClass('ui-state-active');
			 $('#SOshiptolabel4').removeClass('ui-state-active');
			 $('#SOlocationShipToAddressID').focus();
				
				$('#SOlocationShipToAddressID').val("");
				$('#SOlocationShipToAddressID1').val("");
				$('#SOlocationShipToAddressID2').val("");
				$('#SOlocationShipToCity').val("");
				$('#SOlocationShipToState').val("");
				$('#SOlocationShipToZipID').val("");
				//$('#SOGeneral_taxId').val(formatCurrencynodollar(taxRateShipTo));
				ShipToJobSite();
				shiptoJobsitefromOutside();
			} 
			if(typeof(taxTerritory)  !== "undefined")
			{
				$('#taxID').val(taxTerritory);
				$('#taxIDwz').val(taxTerritory);
			}
			if(typeof(taxTerritoryhiddenID)  !== "undefined")
			{
				$('#taxhiddenID').val(taxTerritoryhiddenID);
				$('#taxhiddenIDwz').val(taxTerritoryhiddenID);
			}			
			
			$('#SOlocationShipToAddressID').focus(function(){
		        $(this).data('placeholder',$(this).attr('placeholder'))
		        $(this).attr('placeholder','Minimum 1 character required to get Name List');
		     });
		}
}

	var sizes = "${fn:length(wareHouse)}";
	var shipAddresses = []; //create a new array global
	var shipAddressInc = 0;
	var zFlag = '0';
	if(sizes > 0)
	{
		<c:forEach items="${wareHouse}" var="house">
		shipAddresses.push({description: "${house.description}", address1: "${house.address1}", address2: "${house.address2}", city: "${house.city}", state: "${house.state}", zip: "${house.zip}",warehouseID:"${house.prWarehouseId}", coTaxTerritoryId: "${house.coTaxTerritoryId}"});
		</c:forEach>
	}	
	
	function shipForWard(){

		 if($("#shiptomodefortoggle").val() === 'Pick Up')
		 {
			 prWforwordclick();
		 }
		 else
		 {   
			 customerforwardclick();
		 }
	}

	function shipBackWard(){
		
		 if($("#shiptomodefortoggle").val() === 'Pick Up')
		 {
			 prWbackwordclick()
		 }
		 else 
		 {
			 customerbackwardclick();
		 }
	}



var CushipAddressInc = 0;
var cuFlag = '0';

function customerforwardclick()
{
	
	if(parseInt(CushipAddressInc)<=0){
		CushipAddressInc = 1;
	}else{
	if(cuFlag=='1'){
		CushipAddressInc=parseInt(CushipAddressInc)+2;
	}
	}
	console.log('Forward Log: '+CushipAddressInc);
	if(CushipAddresses.length>0 && CushipAddressInc<CushipAddresses.length)
	{
		 console.log('incValue For: '+CushipAddressInc);
			var imgUrlBackwards = "./../resources/images/Arrowleft.png";
			$('#SObackWardId').css('background', 'url('+imgUrlBackwards+') no-repeat');
			$('#SObackWardId').css('background-position','center');
		if(parseInt(CushipAddressInc)>=CushipAddresses.length-1){
			console.log('Max Value - Z: '+CushipAddressInc);
			var imgUrlDisForwards = "./../resources/images/DisabledArrowright.png";
			$('#SOforWardId').css('background', 'url('+imgUrlDisForwards+') no-repeat');
			$('#SOforWardId').css('background-position','center');			
			
		} 
		$('#SOlocationShipToAddressID').val(CushipAddresses[CushipAddressInc].name);
		$('#SOlocationShipToAddressID1').val(CushipAddresses[CushipAddressInc].address1);
		$('#SOlocationShipToAddressID2').val(CushipAddresses[CushipAddressInc].address2);
		$('#SOlocationShipToCity').val(CushipAddresses[CushipAddressInc].city);
		$('#SOlocationShipToState').val(CushipAddresses[CushipAddressInc].state);
		$('#SOlocationShipToZipID').val(CushipAddresses[CushipAddressInc].zip);
		$('#shiptoCuAddressIDfortoggle').val(CushipAddresses[CushipAddressInc].rxAddressId);
		
		var bcoTaxTerritoryId = CushipAddresses[CushipAddressInc].coTaxTerritoryId;
		//alert("bcoTaxTerritoryId"+bcoTaxTerritoryId);
		loadTaxTerritoryRate(bcoTaxTerritoryId,"");			
		//$('#prWarehouseID').val(CushipAddresses[CushipAddressInc].warehouseID);
		//$('#prToWarehouseId').val(CushipAddresses[CushipAddressInc].warehouseID);
		CushipAddressInc=parseInt(CushipAddressInc)+1;
		//alert($('#prToWarehouseId').val());
	}
	cuFlag='0';
	console.log('Next Forward : '+CushipAddressInc);

}
function customerbackwardclick()
{

	if(parseInt(CushipAddressInc)>=(CushipAddresses.length)-1){
		CushipAddressInc = (CushipAddresses.length)-2;
		}
	else{
	if(cuFlag=='0'){
		CushipAddressInc=parseInt(CushipAddressInc)-2;
	}
	}
	
	console.log('Backward Log: '+CushipAddressInc);
	if(CushipAddresses.length >0 && CushipAddressInc>-1)
	{
			var imgUrlforwards = "./../resources/images/Arrowright.png";
			$('#SOforWardId').css('background', 'url('+imgUrlforwards+') no-repeat');
			$('#SOforWardId').css('background-position','center');

			$('#SOlocationShipToAddressID').val(CushipAddresses[CushipAddressInc].name);
			$('#SOlocationShipToAddressID1').val(CushipAddresses[CushipAddressInc].address1);
			$('#SOlocationShipToAddressID2').val(CushipAddresses[CushipAddressInc].address2);
			$('#SOlocationShipToCity').val(CushipAddresses[CushipAddressInc].city);
			$('#SOlocationShipToState').val(CushipAddresses[CushipAddressInc].state);
			$('#SOlocationShipToZipID').val(CushipAddresses[CushipAddressInc].zip);
			$('#shiptoCuAddressIDfortoggle').val(CushipAddresses[CushipAddressInc].rxAddressId);
			var bcoTaxTerritoryId = CushipAddresses[CushipAddressInc].coTaxTerritoryId;
			//alert("bcoTaxTerritoryId"+bcoTaxTerritoryId);
			loadTaxTerritoryRate(bcoTaxTerritoryId,"");			
			CushipAddressInc=parseInt(CushipAddressInc)-1;
			//alert($('#prToWarehouseId').val());
			if(parseInt(CushipAddressInc)<0){
			console.log('Over Backward');
			var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
			$('#SObackWardId').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
			$('#SObackWardId').css('background-position','center');
			} 
			cuFlag ='1';
			console.log('Next Backward : '+CushipAddressInc);


}
}
	

	
function prWforwordclick()
{
	
	if(parseInt(shipAddressInc)<=0){
		shipAddressInc = 1;
	}else{
	if(zFlag=='1'){
		shipAddressInc=parseInt(shipAddressInc)+2;
	}
	}
	console.log('Forward Log: '+shipAddressInc);
	if(shipAddresses.length>0 && shipAddressInc<shipAddresses.length)
	{
		 console.log('incValue For: '+shipAddressInc);
			var imgUrlBackwards = "./../resources/images/Arrowleft.png";
			$('#SObackWardId').css('background', 'url('+imgUrlBackwards+') no-repeat');
			$('#SObackWardId').css('background-position','center');
		if(parseInt(shipAddressInc)>=shipAddresses.length-1){
			console.log('Max Value - Z: '+shipAddressInc);
			var imgUrlDisForwards = "./../resources/images/DisabledArrowright.png";
			$('#SOforWardId').css('background', 'url('+imgUrlDisForwards+') no-repeat');
			$('#SOforWardId').css('background-position','center');			
			
		} 
		$('#SOlocationShipToAddressID').val(shipAddresses[shipAddressInc].description);
		$('#SOlocationShipToAddressID1').val(shipAddresses[shipAddressInc].address1);
		$('#SOlocationShipToAddressID2').val(shipAddresses[shipAddressInc].address2);
		$('#SOlocationShipToCity').val(shipAddresses[shipAddressInc].city);
		$('#SOlocationShipToState').val(shipAddresses[shipAddressInc].state);
		$('#SOlocationShipToZipID').val(shipAddresses[shipAddressInc].zip);

		var bcoTaxTerritoryId = shipAddresses[shipAddressInc].coTaxTerritoryId;
		//alert("bcoTaxTerritoryId"+bcoTaxTerritoryId);
		loadTaxTerritoryRate(bcoTaxTerritoryId,"");			
		$('#prWarehouseID').val(shipAddresses[shipAddressInc].warehouseID);
		$('#prToWarehouseId').val(shipAddresses[shipAddressInc].warehouseID);
		shipAddressInc=parseInt(shipAddressInc)+1;
		//alert($('#prToWarehouseId').val());
	}
	zFlag='0';
	console.log('Next Forward : '+shipAddressInc);
	
}
function prWbackwordclick()
{
	if(parseInt(shipAddressInc)>=(shipAddresses.length)-1){
		shipAddressInc = (shipAddresses.length)-2;
		}
	else{
	if(zFlag=='0'){
		shipAddressInc=parseInt(shipAddressInc)-2;
	}
	}
	
	console.log('Backward Log: '+shipAddressInc);
	if(shipAddresses.length >0 && shipAddressInc>-1)
	{
			var imgUrlforwards = "./../resources/images/Arrowright.png";
			$('#SOforWardId').css('background', 'url('+imgUrlforwards+') no-repeat');
			$('#SOforWardId').css('background-position','center');

			$('#SOlocationShipToAddressID').val(shipAddresses[shipAddressInc].description);
			$('#SOlocationShipToAddressID1').val(shipAddresses[shipAddressInc].address1);
			$('#SOlocationShipToAddressID2').val(shipAddresses[shipAddressInc].address2);
			$('#SOlocationShipToCity').val(shipAddresses[shipAddressInc].city);
			$('#SOlocationShipToState').val(shipAddresses[shipAddressInc].state);
			$('#SOlocationShipToZipID').val(shipAddresses[shipAddressInc].zip);
			var bcoTaxTerritoryId = shipAddresses[shipAddressInc].coTaxTerritoryId;
			//alert("bcoTaxTerritoryId"+bcoTaxTerritoryId);
			loadTaxTerritoryRate(bcoTaxTerritoryId,"");			
			$('#prWarehouseID').val(shipAddresses[shipAddressInc].warehouseID);
			$('#prToWarehouseId').val(shipAddresses[shipAddressInc].warehouseID);
			shipAddressInc=parseInt(shipAddressInc)-1;
			//alert($('#prToWarehouseId').val());
			if(parseInt(shipAddressInc)<0){
			console.log('Over Backward');
			var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
			$('#SObackWardId').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
			$('#SObackWardId').css('background-position','center');
			} 
			zFlag ='1';
			console.log('Next Backward : '+shipAddressInc);
	}
}
	

	

function loadPickUpAddress()
{		
	$("#shiptolabel1").addClass('ui-state-active');
	$('#SOlocationShipToAddressID').val("${wareHouse[0].description}");
	$('#SOlocationShipToAddressID1').val("${wareHouse[0].address1}");
	$('#SOlocationShipToAddressID2').val("${wareHouse[0].address2}");
	$('#SOlocationShipToCity').val("${wareHouse[0].city}");
	$('#SOlocationShipToState').val("${wareHouse[0].state}");
	$('#SOlocationShipToZipID').val("${wareHouse[0].zip}");
	var sTaxRate = "${wareHouse[0].email}";
	$('#SOGeneral_taxId').val(Number(floorFigureoverall(sTaxRate, 2)));
	var coTaxTerritoryId = "${prWareHouse[0].coTaxTerritoryId}";
}

function loadTaxTerritoryRate(coTaxTerritoryId,type)
{
  // alert(coTaxTerritoryId+"Type::"+type);
	if(coTaxTerritoryId != null && coTaxTerritoryId != '')
	{
		$.ajax({
			url: "./salesOrderController/taxRateTerritory",
			type: "POST",
			data : {"coTaxTerritoryId" : coTaxTerritoryId},
			success: function(data) {
				$('#taxID').val(data.county);
				$('#taxIDwz').val(data.county);
				$('#SOGeneral_taxId').val(Number(floorFigureoverall(data.taxRate, 2)));
				$('#taxhiddenID').val(data.coTaxTerritoryId);
				$('#taxhiddenIDwz').val(data.coTaxTerritoryId);
				var subtot = $("#SOGeneral_subTotalID").val().replace(/[^0-9\-\.]+/g,"");
				var frieght = $("#SOGeneral_frightID").val().replace(/[^0-9\-\.]+/g,"");
				var sum = 0;
				var taxAmt = Number(subtot)*(data.taxRate/100);
				sum = Number(subtot) + taxAmt + Number(frieght);

				 $("#SOGeneral_taxvalue").val(Number(floorFigureoverall(taxAmt, 2)));
				 $("#SOGeneral_totalID").val(Number(floorFigureoverall(sum, 2)));
				
			}
	});
	}
	
if(type === 'Pick Up')
{
	$('#shiptolabel1').addClass('ui-state-active');
	$("#SOshiptoradio1").attr("Checked","Checked");
	$('#shiptolabel2').removeClass('ui-state-active');
	$('#shiptolabel3').removeClass('ui-state-active');
	$('SO#shiptolabel4').removeClass('ui-state-active');
}
else if(type === 'Customer')
{
	$('#shiptolabel1').removeClass('ui-state-active');
	$('#shiptolabel2').addClass('ui-state-active');
	$("#SOshiptoradio2").attr("Checked","Checked");
	$('#shiptolabel3').removeClass('ui-state-active');
	$('#shiptolabel4').removeClass('ui-state-active');
}
else if(type === 'Other')
{
	$('#shiptolabel1').removeClass('ui-state-active');
	$('#shiptolabel2').removeClass('ui-state-active');
	$('#shiptolabel3').removeClass('ui-state-active');
	$('#shiptolabel4').addClass('ui-state-active');
	$("#SOshiptoradio4").attr("Checked","Checked");
}
else if(type === 'Job Site')
{
	$('#shiptolabel1').removeClass('ui-state-active');
	$('#shiptolabel2').removeClass('ui-state-active');
	$('#shiptolabel4').removeClass('ui-state-active');
	$('#shiptolabel3').addClass('ui-state-active');
	$("#SOshiptoradio3").attr("Checked","Checked");
}
	
}


function shiptoJobsitefromOutside()
{

	if($('#operation').val() === 'create' || $('#operation').val() === 'edit' || $('#operation').val() === 'update'){	
	$.ajax({
		url: "./salesOrderController/getJobsiteAddressfromoutside",
		type: "GET",
		data : {"cusoid" : $('#Cuso_ID').text()},
		success: function(data) {

			if(data!=null)
			{
			$('#SOlocationShipToAddressID').val($('#CustomerNameGeneral').val());
			$('#SOlocationShipToAddressID1').val(data.locationAddress1);
			$('#SOlocationShipToAddressID2').val(data.locationAddress2);
			$('#SOlocationShipToCity').val(data.locationCity);
			$('#SOlocationShipToState').val(data.locationState);
			$('#SOlocationShipToZipID').val(data.locationZip);
			loadTaxTerritoryRate(data.coTaxTerritoryId,"Job Site");
			}
			else
			{
				ShipToJobSite();
			}
		}
	});

	}
	else
	{
		ShipToJobSite();
	}
	$("#SOshiptoradio3").attr("Checked","Checked");
	$('#SOshiptolabel3').addClass('ui-state-active');
	$('#SOshiptolabel1').removeClass('ui-state-active');
	$('#SOshiptolabel2').removeClass('ui-state-active');
	$('#SOshiptolabel4').removeClass('ui-state-active');
	
}



/**--- Ajax auto suggest functions ---*/
$(function() { var cache = {}, lastXhr;
$( "#salesmanID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesmanhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#csrID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#csrhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#salesManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#engineerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#engineerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#projectManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#projectManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	} }); });

$(function() { var cache = {}, lastXhr;
$( "#taxID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) {  var id = ui.item.id; $("#taxhiddenID").val(id);
	toggledivflag="#SO_Shipto";
	loadmaintabtaxterritory(toggledivflag,id);
	},
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });	
function loadmaintabtaxterritory(toggledivflag,coTaxTerritoryId){
	if(coTaxTerritoryId != null && coTaxTerritoryId != '')
	{
		$.ajax({
			url: "./salesOrderController/taxRateTerritory",
			type: "POST",
			data : {"coTaxTerritoryId" : coTaxTerritoryId},
			success: function(data) {
				if(data.taxfreight==1){
					$("#so_taxfreight").val(1);
				}else{
					$("#so_taxfreight").val(0);
				}
				SetTaxTerritory(toggledivflag,data);
			}
	});
	}else{
		SetTaxTerritory(toggledivflag,null);
		}
}
$(function() { var cache = {}, lastXhr;
$( "#termID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#termhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#viaID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#viahiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "jobtabs3/shipVia", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
var billToCustomer,shipToCustomer,billToCustomerNameGeneralID,shipTorxCustomer_ID,customerMasterObj,CustomerName,shipToCustomerName,taxRateShipTo;
var taxTerritory,taxTerritoryhiddenID,sEmail;
$(function() { var cache = {}, lastXhr;
$( "#CustomerNameGeneral" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; billToCustomerNameGeneralID = id;shipTorxCustomer_ID = id;$("#billToCustomerNameGeneralID").val(id); $("#shipTorxCustomer_ID").val(id);
	$.ajax({
        url: 'rxdetailedviewtabs/SOAddressDetails?rxMasterId='+$("#billToCustomerNameGeneralID").val(),
        type: 'POST',       
        success: function (data) {
        	var shiptocount = 0;
        	var billtocount = 0;
        	console.log("all--->"+data);
        	sEmail = "";

			loadCustomerAddress(id);

			 $("#shiptoaddradio3").button({disabled:true});
			 $('#shiptoaddlabel3').attr('onclick','').unbind('click');
        	
			$.each(data, function(key, valueMap) {
// 				alert(key);
				if("customerMasterObj" == key)
				{
// 					alert(" >>>>> ");
					var vl = valueMap;
					$.each(valueMap, function(index, value){
// 						alert("value.creditHold == "+value.creditHold);
						$("#CreditHold").val(value.creditHold);
					});
				}
				if("customerAddress"==key)
				{				
					
					billToCustomer = valueMap;		
					$.each(valueMap, function(index, value){
						if(value.isBillTo==1 ){	
						
								if(value.isMailing==1)
								{
								billtocount++;	
								if(billtocount == 1)
								{
								$('#SOlocationbillToAddressname').val(value.name);
								$('#SOGenerallocationbillToAddressID1').val(value.address1);
								$('#SOGenerallocationbillToAddressID2').val(value.address2);
								$('#SOGenerallocationbillToCity').val(value.city);
								$('#SOGenerallocationbillToState').val(value.state);
								$('#SOGenerallocationbillToZipID').val(value.zip);
								}
								}
								else
								{
								$('#SOlocationbillToAddressname').val(value.name);
								}
						//$('#customerBillToAddressID').val(value.rxAddressId);
						CustomerName = value.name;
						}

						if(value.isShipTo==1){
						console.log('Data applied Here');
						shiptocount++;
						shipToCustomer = valueMap;	
						if(shiptocount == 1)
						{

						$("#SO_Shipto").contents().find("#shiptomodehiddenfromdbid").val("1");
						$("#SO_Shipto").contents().find("#shiptoaddrhiddenfromdbid").val(value.rxAddressId);
						preloadShiptoAddress("#SO_Shipto",null,value.rxAddressId,'1','0',$("#jobCustomerName_ID").text());
						$("#SO_Shipto").contents().find("#shiptomoderhiddenid").val('1');
						}
						
						
						$("#SOshiptoradio2").attr("Checked","Checked");
						$('#SOshiptolabel2').addClass('ui-state-active');
						$('#SOshiptolabel1').removeClass('ui-state-active');
						$('#SOshiptolabel3').removeClass('ui-state-active');
						$('#SOshiptolabel4').removeClass('ui-state-active');
						$('#SOlocationShipToAddressID').focus();
						}
						
					}); 
				}
/* 
					if("taxRateforCity" == key)
					{
						$.each(valueMap, function(index, value){
							$('#SOGeneral_taxId').val(formatCurrencynodollar(value));
							taxRateShipTo = value;
						}); 
					}

					if("taxTerritory" == key)
					{
						$.each(valueMap, function(index, value){
							$('#taxID').val(value);
							$('#taxIDwz').val(value);
							taxTerritory = value;
						
						}); 
					}
					if("taxTerritoryID" == key)
					{
						$.each(valueMap, function(index, value){
							$('#taxhiddenID').val(value);
							$('#taxhiddenIDwz').val(value);
							taxTerritoryhiddenID = value;
						
						}); 
					} */

					/* if("divisionsSearch" == key)
					{
						$.each(valueMap, function(index, value){
							$('select[name="divisionName"]').find('option:contains("Blue")').attr("selected",true);
							$('#SOdivisionID').val(value);
						
						}); 
					} */

					if("customerMasterObj" == key)
					{
						customerMasterObj = valueMap;
						$.each(valueMap, function(index, value){
							$('#salesmanhiddenID').val(value.cuAssignmentId0);
							$('#csrhiddenID').val(value.cuAssignmentId1);
							$('#salesManagerhiddenID').val(value.cuAssignmentId2);
							$('#engineerhiddenID').val(value.cuAssignmentId3);
							$('#projectManagerhiddenID').val(value.cuAssignmentId4);
							if(value.prWarehouseId!=null && value.prWarehouseId!="" && value.prWarehouseId>0){
								$("#whrhouseID option[value=" + value.prWarehouseId + "]").attr("selected", true);
								}
								
						}); 
					} 

					if("customerTabFormDataBean" == key)
					{
						$.each(valueMap, function(index, value){
							$('#salesmanID').val(value.assignedSalesRep);
							$('#csrID').val(value.assignedCSRs);
							$('#salesManagerID').val(value.assignedSalesMGRs);
							$('#engineerID').val(value.assignedEngineers);
							//$('#projectManagerID').val(value.assignedProjManagers);
							$('select[name="terms"]').each(function(){ 							     
						        $(this).find('option').each(function(){ 
						            if($.trim($(this).text()) == value.customerTerms){
						                $(this).prop('selected','selected');                    
						            }   
						         
						        });
						 
						    });
						
						}); 
					}
					
					if("emailList" == key)
					{
						customerMasterObj = valueMap;
						$.each(valueMap, function(index, value){
							if(value.email != null && value.email.trim() != '')
							sEmail+='<option value='+value.rxContactId+'>'+value.email+'</option>';
						
						}); 
					} 

					$.ajax({
						url: "./salesOrderController/getCustomerShipToAddressforToggle",
						type: "GET",
						data : {"customerID" :$("#billToCustomerNameGeneralID").val()},
						success: function(data) {
							CushipAddresses = data;
							
						}
					});
			});

			if(shiptocount == 0 || shiptocount == 1)
			{
			$('#SOforWardId').hide();
			$('#SObackWardId').hide();
			}
			else
			{
				$('#SOforWardId').show();
				$('#SObackWardId').show();
			}
			$("#SOlocationShipToAddressID").attr("disabled",false);
			$('#customerShipToOtherIDeditable').val("Customer");
			$('#customerShipToOtherID').val("Customer");

			$('#emailList').html(sEmail);
			setTimeout(function(){
				var taxID=$("#taxhiddenIDwz").val();
				loadTaxterritoryforSO(taxID);
				}, 250);
        }
    });
	 },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/customerName", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });


function loadTaxterritoryforSO(cotaxterittoryID){
	if(cotaxterittoryID!=null && cotaxterittoryID>0){
		$.ajax({
			url: "./product/getTaxTerritory",
			type: "GET",
			data : {'taxID':cotaxterittoryID},
			success: function(data) {
				if(data!=null){
					$("#so_taxfreight").val(data.taxfreight);
					}
			}
		});
		}else{
			$("#so_taxfreight").val(0);
			}
	
	}
$('#projectManagerID').val("${sessionScope.user.fullName}");

$(function() { var cache = {}; var lastXhr='';
$("#SOlocationShipToAddressID").autocomplete({ minLength: 1,timeout :1000,
	/* open: function(){ 
		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	},  */
	select: function (event, ui) {
		var aValue = ui.item.value;
		var valuesArray = new Array();
		valuesArray = aValue.split("|");
		var id = valuesArray[0];
		var code = valuesArray[2];
		shipTorxCustomer_ID = aValue;
		$("#shipTorxCustomer_ID").val(aValue);
		loadCustomerAddress(id);
		
		$.ajax({
	        url: 'rxdetailedviewtabs/SOAddressDetails?rxMasterId='+aValue,
	        type: 'POST',       
	        success: function (data) {

				$.each(data, function(key, valueMap) {
					
					if("customerAddress"==key)
					{				
						//if(valueMap.isShipTo==1){
						shipToCustomer = valueMap;		
						$.each(valueMap, function(index, value){							

							if(value.isShipTo==1){
								console.log('Data applied Here');
								shiptocount++;
								shipToCustomer = valueMap;	
								if(shiptocount == 1)
								{

								$("#SO_Shipto").contents().find("#shiptomodehiddenfromdbid").val("1");
								$("#SO_Shipto").contents().find("#shiptoaddrhiddenfromdbid").val(value.rxAddressId);
								preloadShiptoAddress("#SO_Shipto",null,value.rxAddressId,'1','0',$("#jobCustomerName_ID").text());
								$("#SO_Shipto").contents().find("#shiptomoderhiddenid").val('1');

								}
								
								$("#SOshiptoradio2").attr("Checked","Checked");
								$('#SOshiptolabel2').addClass('ui-state-active');
								$('#SOshiptolabel1').removeClass('ui-state-active');
								$('#SOshiptolabel3').removeClass('ui-state-active');
								$('#SOshiptolabel4').removeClass('ui-state-active');
								$('#SOlocationShipToAddressID').focus();
								}
								
						
						}); 
						
					//}
					}

					if("taxRateforCity" == key)
					{
						$.each(valueMap, function(index, value){
							$('#SOGeneral_taxId').val(Number(floorFigureoverall(value, 2)));
							taxRateShipTo = value;
						
						}); 
					}

					if("taxTerritory" == key)
					{
						$.each(valueMap, function(index, value){
							$('#taxID').val(value);
							$('#taxIDwz').val(value);
							taxTerritory = value;
						}); 
					}
					if("taxTerritoryID" == key)
					{
						$.each(valueMap, function(index, value){
							$('#taxhiddenID').val(value);
							$('#taxhiddenIDwz').val(value);
							taxTerritoryhiddenID = value;
						
						}); 
					}

					/* if("divisionsSearch" == key)
					{
						$.each(valueMap, function(index, value){
							$('select[name="divisionName"]').find('option:contains("Blue")').attr("selected",true);
							$('#SOdivisionID').val(value);
						
						}); 
					} */

					/* if("customerMasterObj" == key)
					{
						$.each(valueMap, function(index, value){
							$('#salesmanhiddenID').val(value.cuAssignmentId0);
							$('#csrhiddenID').val(value.cuAssignmentId1);
							$('#salesManagerhiddenID').val(value.cuAssignmentId2);
							$('#engineerhiddenID').val(value.cuAssignmentId3);
							$('#projectManagerhiddenID').val(value.cuAssignmentId4);
						
						}); 
					} 

					if("customerTabFormDataBean" == key)
					{
						$.each(valueMap, function(index, value){
							$('#salesmanID').val(value.assignedSalesRep);
							$('#csrID').val(value.assignedCSRs);
							$('#salesManagerID').val(value.assignedSalesMGRs);
							$('#engineerID').val(value.assignedEngineers);
							$('#projectManagerID').val(value.assignedProjManagers);
							
						
						}); 
					} */
					
					

					
				});
				
	        }
	    });  
		
		//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
	},
source: function( request, response ) { var term = request.term;
	if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
	lastXhr = $.getJSON( "search/customerAddress", request, function( data, status, xhr ) { cache[ term ] = data; 
		if ( xhr === lastXhr ) { response( data ); 	} });
},
error: function (result) {
     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
} });
});

function formatCurrencynodollar(strValue)
{
	if(strValue === "" || strValue == null){
		return "0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10){
		strCents = "0" + strCents;
	}
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	}
	return (((blnSign)?'':'-') + dblValue + '.' + strCents);
}

var id = '';
$('input[type="button"]').click(function(e){
   id = e.target.id;
   if("SavePOReleaseID" == id)
   {
   	$('#setButtonValue').val("Save");
   }
   if("POReleaseID" == id)
   {
   	$('#setButtonValue').val("SaveandClose");
   }
   
});


function viewPOPDFSave(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof,withPrice){
	
	var newDialogDiv = jQuery(document.createElement('div'));
	var check = withPrice;
	var cusoID = $('#Cuso_ID').text();
	if(cusoID != '' && cusoID != undefined)
	/*	jQuery(newDialogDiv).dialog({close:false,modal: true, width:300, height:150, title:"Generating PDF", 
			buttons: [] }).dialog("open");*/
		$.ajax({ 
			url: "./salesOrderController/printSalesOrderReport",
			mType: "GET",
			data : { 'cusoID' : cusoID,"price":check,"WriteorView":"write"},
			success: function(data){ 
				//jQuery(newDialogDiv).dialog("close");
				sendsubmitMailFunction1(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof); 
				}
		});
		
	return true;
}

/* $(function(){
	$("#emailpopup" ).dialog({
		autoOpen: false,
		height: 570,
		width: 600,
		modal: true,
		buttons: {
					
			Send:function(){  
				 var $led = $("#emailpopup");
				 
				// alert("hifff");

				if (typeof $led.data('cuSOid') === "undefined")
					{
					sendsubmitMailFunction();
					}
				else
					{
					viewPOPDFSave($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuSOid'),$led.data('withprice'));
					}
	   		 },	
		
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},	
	});
	}) */
</script>
