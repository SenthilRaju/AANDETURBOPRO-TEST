<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
.loadingDivForReleaseSave{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}
.cke_contents {
				height: 200px !important;
			}

/* For Tooltip */

/* img.tooltip > span {
    width: 300px;
    padding: 2px 2px;
    margin-top: 0;
    margin-left: -120px;
    opacity: 0;
    visibility: hidden;
    z-index: 10;
    position: absolute;
    font-family: Arial;
    font-size: 12px;
    font-style: normal;
    border-radius: 3px;
    box-shadow: 2px 2px 2px #999;
    -webkit-transition-property: opacity, margin-top, visibility, margin-left;
    -webkit-transition-duration: 0.4s, 0.3s, 0.4s, 0.3s;
    -webkit-transition-timing-function: ease-in-out, ease-in-out, ease-in-out, ease-in-out;
    transition-property: opacity, margin-top, visibility, margin-left;
    transition-duration: 0.4s, 0.3s, 0.4s, 0.3s;
    transition-timing-function: 
        ease-in-out, ease-in-out, ease-in-out, ease-in-out;
} */

/* a.tooltip > span:hover, */
/* img.tooltip:hover > span {
    opacity: 1;
    text-decoration: none;
    visibility: visible;
    overflow: visible;
    margin-top: 50px;
    display: inline;
    margin-left: -90px;
} */
/* 
img.tooltip span b {
    width: 15px;
    height: 15px;
    margin-left: 40px;
    margin-top: -19px;
    display: block;
    position: absolute;
    -webkit-transform: rotate(-45deg);
    -moz-transform: rotate(-45deg);
    -o-transform: rotate(-45deg);
    transform: rotate(-45deg);
    -webkit-box-shadow: inset -1px 1px 0 #fff;
    -moz-box-shadow: inset 0 1px 0 #fff;
    -o-box-shadow: inset 0 1px 0 #fff;
    box-shadow: inset 0 1px 0 #fff;
    display: none\0/;
    *display: none;
}    

img.tooltip > span {
	color: #000000; 
	background: #FBF5E6;
	background: -webkit-linear-gradient(top, #FBF5E6, #FFFFFF);
	background: linear-gradient(top, #FBF5E6, #FFFFFF);	    
	border: 2px solid #CFB57C;	     
}    
	  
img.tooltip span b {
	background: #FBF5E6;
	border-top: 2px solid #CFB57C;
	border-right: 2px solid #CFB57C;
}
 */
/* tooltip completed */

</style>
<script type="text/javascript">

</script>
<table>
	<tr><td><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
	<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
</table>
<table>
<tr>
	<td style="padding-left: 0px;vertical-align: top;">
		<fieldset class= "custom_fieldset"style="width:500px;height:102px">
		<legend class="custom_legend"><label><b>Release Notes</b></label></legend>
		<table>
			<tr><td ><textarea id="releaseNote" cols="55" rows="3">${requestScope.joMasterDetails.releaseNotes}</textarea></td></tr>
		</table>
		</fieldset>
	</td>
	<td width="50px;"></td>
	<td style="padding-left: 0px;vertical-align: top;">
		<fieldset class= "custom_fieldset"style="width:500px; height:100px">
		<legend class="custom_legend"><label><b>Billing Entire Job</b></label>&nbsp;<input type="checkbox" checked="checked" style="vertical-align:bottom" id="billing" onchange="changeBillingDetails()"></legend>
		<table>
			<tr><td style="width:100px"><label>Estimated:</label></td><td><label id="estimate"></label></td></tr>   
			<tr><td style="width:100px"><label>Allocated:</label></td><td><label id="allocate"></label></td></tr>
			<tr><td style="width:100px"><label>Unallocated:</label></td><td><label id="unAllocated"></label></td></tr>
		</table>
	</fieldset>
	</td>
</tr>
</table>
<br>
<table>
	<tr>
		<td>
			<fieldset class= " custom_fieldset"style="width:1080px;height: 390px">
				<legend class="custom_legend"><label><b>Releases</b></label></legend>
				<div>
					<form id="customerPOFormID">
						<table style="width: 1050px;">
						<tr><td colspan="3" align="center">OtherContact<input type="checkbox" class="noticeContact" value="invoiceAmount" onchange="noticeContact()" style="vertical-align: middle;" id="noticeContactID"></td></tr>
			  				<tr>
						  		<td style="padding: 0px 20px 0px;width: 30%;" >
									PO Amount<input type="checkbox" class="PoCheck" value="poAmount" onchange="checkPoAmount()" style="vertical-align: middle;" id="poAmountID">&nbsp;
									Invoice Amount<input type="checkbox" class="InvoCheck" value="invoiceAmount" onchange="checkInvoAmount()" style="vertical-align: middle;" id="invoiceAmountID"></td>
									<td width="40%"><Select  id="noticeId" style="width:24%">
										<option value="-1"> - Select - </option>
										<option value="0"> Notice To </option>
										<option value="1"> 24Hrs Notice To </option>
										<option value="2"> 48Hrs Notice To </option>
										</Select>
										<Select  id="contactId_Release" style="width:30%">
										<option value="-1"> - Select - </option>
										</Select>
										<input type="text" name="noticeName" id="noticeNameID" maxlength='20' style="width:25%;display: none;">
										<input type="text" id="notice" name="notice" maxlength='20' style="width: 160px;"/>
									</td>
								<td style="padding-left: 10px;" align="right">
									<div>&nbsp;Customer PO:&nbsp;<Select  id="PONumberID" style="width: 155px;">
										<option value="-1"> - Select - </option>
											<%-- <c:if test="${not empty requestScope.joMasterDetails.customerPonumber}"> --%>
											 	<option value="1" >
											 		<c:out value="${requestScope.joMasterDetails.customerPonumber}"></c:out>
											 	</option>
											 <%-- </c:if> --%>
											<c:forEach var="CustomerPONumber" items="${requestScope.custPONoDetails}">
												 <c:if test="${not empty CustomerPONumber.customerPonumber1}">
												 	<option value="2" >
												 		<c:out value="${CustomerPONumber.customerPonumber1}"></c:out>
												 	</option>
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
										</select>
										<input type="text" id="customerPONumberID" style="vertical-align: middle;width: 120px; display:none" class="validate[maxSize[17]]" onclick="removeDollorSym()">&nbsp; 
										<input type="image" src="./../resources/Icons/tick_job.png" title="Save Customer PO-Number" style="background: #EEDEBC; border: 0px;vertical-align: middle;margin-left: -6px; margin-right: 10px;display: none;" onclick="saveCustomerPONumber()">
										<a onclick="openBillDialog()" style="cursor: hand;" href="#"><img src="./../resources/Icons/dollar.png" style="vertical-align: middle;">Bill Note</a>
									</div>
					  			</td>
		  					</tr>
		  				</table>
	  				</form>
	  			</div>
				<table id="release" style="width:20px;" align="center"></table>
				<div id="releasepager"></div>
				<%-- <div class="loadingDiv" id="loadingPODiv"> </div> --%>
				<table style="width: 1080px;">
					<tr>
						<td colspan="4" align="center"><div id="releaseMessage" class="warningMsg"></div></td>
					</tr>
					<tr>
						<td style="width: 10%;padding: 0px 1px;">
	  						<fieldset class= "custom_fieldset" style="padding-bottom: 0px; ">
	    						<table>
	    							<tr>
						      			<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Release" style="background: #EEDEBC; border: 0px;" onclick="addRelease()"></td>
						    		  	<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit Release" style="background: #EEDEBC; border: 0px;" onclick="editRelease()"></td>
						   			   	<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Release" style="background: #EEDEBC; border: 0px;" onclick="deleteRelease()"></td>
	    							</tr>
	    						</table>
	   						</fieldset>
	  					</td>
	  					<td>
	  						
	  					</td>
	  					<td style="padding-left:20px; width: 15%;">
	  						<fieldset class= "custom_fieldset" style="padding-bottom: 0px; ">
	    						<table>
	    							<tr>
	    								<td align="right" style="padding-right: 7px;"><a onclick="viewPOPDF()"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="" style="background: #EEDEBC"></a></td> 
	    								<!-- <td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC" onclick="sendPOEmail()"></td> -->	
								  		 <td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOEmailfromrelease('jobrelease')"></td> 
										<td align="right" style="padding-right: 0px;"><label class="newlinkLabel" style="padding-top: 4px;"><a onclick="editreleasedialog()" style="padding-bottom: 3px; padding-top: 4px;" class="alink">Order</a></label></td>
									</tr>
								</table>
						</fieldset>
						</td>
						<td style="padding-left: 10px;padding-top: 9px;width: 10%;">
						<input type="button" value="Change Order" onclick="changeorderdialog()" class="changeOrder turbo-tan">
						</td>
					</tr>
				</table>
				</fieldset>
		</td>
	</tr>
</table>
<br>
<jsp:include page="../changeorder.jsp"></jsp:include>
<jsp:include page="../addCost.jsp"></jsp:include>

	<div id="commissionDialogBox">
	 	<form action="" id="commissionDialogForm">
			<table>
				<tr>	
					<td><b>Ship Date:</b></td>
					<td><input type="text" name="commissionShipDate" id="commissionDialogShipDateVal" style="margin-left: 3px;"></td>
				</tr>
				<tr>
					<td><b>Ship VIA:<span style="color:red;"> *</span></b></td>
					<td>
						<select style="margin-left: 3px;border:#DDBA82; width: 190px;" name="commissionShipVia" id="commissionDialogShipVIAVal">
							<option value="-1"> - Select - </option>
							<c:forEach var="shipViaBean" items="${requestScope.veShipViaDetails}">
								<option value="${shipViaBean.veShipViaId}">
									<c:out value="${shipViaBean.description}" ></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><b>Tracking/Pro #:</b></td>
					<td><input type="text" name="commissionTrackingPro" id="commissionDialogTrackingPROVal" style="margin-left: 3px;" maxlength="30"></td>
				</tr>
				<tr>
					<td><b>Vendor Invoice #:</b></td>
					<td><input type="text" name="commissionVendorInvoiceNo" id="commissionDialogVendorInvoiceVal" style="margin-left: 3px;" maxlength="30"></td>
				</tr>
				<tr>
					<td><b>Invoice Amount:</b></td>
					<td><input type="text" name="commissionInvoiceAmt" id="commissionDialogInvoiceAmtVal" style="margin-left: 3px; text-align: right;" maxlength="9" onfocus="removedollarsymbol(this.value,this.id)" ></td>
				</tr>
				<tr>
					<td><b>Commissions Date:</b></td>
					<td><input type="text" name="commissionDateVal" id="commissionDialogCommissionDateVal" style="margin-left: 3px;" ></td>
				</tr>
				<tr>
					<td><b>Commissions Amount:</b></td>
					<td><input type="text" name="commissionAmountVal" id="commissionDialogCommissionsAmtVal" style="margin-left: 3px; text-align: right;" maxlength="9" onfocus="removedollarsymbol(this.value,this.id)">
					<input type="hidden" id="veCommDetailIdVal" name="veCommdetailIDName">
					</td>
				</tr>
			</table>
		</form>
		<hr width="500px;">
		<table align="right">
			<tr>
				<td><input type="button" class="cancelhoverbutton turbo-tan"  value="Submit" onclick="submitCommissionDialog()" style="width:80px;"/></td> 
				<td><input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelCommissionDialog()" style="width:80px;"/> </td>
			</tr>
		</table>
	  </div>
	  
<fieldset class= "custom_fieldset"style="width:1080px">
	<div id="shipingGrid">
		<table id="shiping" style="width:20px"></table><div id="shipingpager"></div>
	</div>
	<div style="margin-top: 10px;">
		<table style="width:100%;">
		<tr>
			<td colspan="2" align="right"><div id="shipperMsg" style="display: none"></div></td>
		</tr>
			<tr>
				<td style="width: 30px;padding: 0px 1px;vertical-align: middle;">
					<fieldset class= "custom_fieldset" style="padding-bottom: 0px; ">
 						<table>
 							<tr>
		      					<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Vendor Invoice" style="background: #EEDEBC; border: 0px;" onclick="redirecttovieworaddvendorinvoice()"></td>
				   			   	<!-- <td align="right" style="padding-right: 0px;vertical-align: middle;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Vendor Invoice" style="background: #EEDEBC; border: 0px;" onclick="deleteShipDetails()"></td> -->
				   			   	<td align="right" style="padding-right: 0px;vertical-align: middle;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Vendor Invoice" style="background: #EEDEBC; border: 0px;cursor:default" ></td>
 							</tr>
 						</table>
					</fieldset>
				</td>
				<td style="width: 300px;vertical-align: bottom;">
					<input type="button" class="track turbo-tan" value="Track" id = "trackId" onclick="openReleaseTrack()">
					<input type="button" value="Vendor Invoice" class="vendorinvoice turbo-tan" onclick="editvendorinvoice()"> 
					<!-- <input type="button" value="Vendor Invoice" class="vendorinvoice turbo-tan" onclick="redirecttovieworaddvendorinvoice()"> -->
					<input type="button" value="Customer Invoice" id="customerInvoicebtnID" class="customerinvoice turbo-tan" onclick="opencustomerinvoicedialog()">
				</td>
				<td align="right">
				<table><tr>
				<td>
				<div id="commissionFields"><label>Expected Commission:&nbsp;</label>
				<input type="text" name="expCommission" id="expCommissionID" style="width: 70px" maxlength="9" onfocus="removedollarsymbol(this.value,this.id)" onkeypress="return IsNumeric(event);" ondrop="return false;" onpaste="return false;" />
				
				&nbsp;&nbsp;&nbsp;<input type="checkbox" name="commissionClosed" id="commissionClosedID" readonly="readonly" value="closed" onclick="UpdateCommissionRecieveornot()">&nbsp;Closed</div>
				</td>
				<td id="billedTDID" align="right" style="display:none;">&nbsp;&nbsp;&nbsp;
					<label style="width:50px;font-weight:bold;" id="billedID">Billed:</label>
					<label style="width:50px;" id="billedamount"></label>
					<label style="width:50px;font-weight:bold;" id="unBilledID">Unbilled:</label>
					<label style="width:50px;" id="unbilledamount"></label>
					<label style="width:50px;font-weight:bold;" id="freightBillID">Freight:</label>
     				<label style="width:50px;" id="freightBillAmount"></label>
				</td>
				
				<td id="commissionID" align="right" style="display:none;">&nbsp;&nbsp;&nbsp;
					<label style="width:50px;font-weight:bold;" id="commissionReceived">Received: </label>
					<label style="width:50px;" id="CommissionReceivedAmount">$0.00</label>
					<label style="width:50px;font-weight:bold;" id="commissionBalance">Balance: </label>
					<label style="width:50px;" id="commissionBalanceAmount">$0.00</label>
				</td>
				</table>
				
				
				</td>
			</tr>
		</table>
	</div>
</fieldset>
<br>
<hr style="width:1100px; color:#C2A472;">
<jsp:include page="../vendorinvoicepage.jsp"></jsp:include>
<jsp:include page="../customer_invoice.jsp"></jsp:include>
<table style="width:1110px">
	<tr>
		<td colspan="2" align="center"><div id="saveReleaseMsg" style="display: none"></div></td>
	</tr>
	<tr>
		<td align="right">&nbsp;&nbsp;
			<input type="button" value="Save" class="savehoverbutton turbo-tan"  onclick="release_save()" style="width:80px">
		</td>
	</tr> 
</table>
<table style="display: none;">
	<tr>
		<td>
			<input type="text" id="quoteNumber" value="${requestScope.joMasterDetails.quoteNumber}">
		</td>
	</tr>
</table>
<div id="openReleaseDig">
<div class="loadingDivForReleaseSave" id="loadingopenReleaseDlg" style="display: none;opacity: 0.7;background-color: #fff;z-index: 1234;text-align: center;"/>
	<form action="" id="openReleaseDigForm">
		<div id="message1" class="warningMsg" ></div>
		<table id="ReleaseTable">
			<tr style="display: none;">
				<td><label>ReleaseId: </label></td>
				<td><input type="text" id="joReleaseId" name="joReleaseName" style="width: 200px;"/>
			</tr>
			<tr style="display: none;">
				<td><label>ReleaseDetailId: </label></td>
				<td><input type="text" id="joReleaseDetailId" name="joReleaseDetailName" style="width: 200px;"/>
			</tr>
			<tr style="display: none;">
				<td><label>VePoId: </label></td>
				<td><input type="text" id="vePoId" name="vePoName" style="width: 200px;"/>
			</tr>
			<tr>
				<td style="width: 150px;"><label id="releasedLableID">Released:<span style="color:red;"> *</span> </label></td>
				<td><input type="text" id="ReleasesID" name="ReleasesName" style="width: 100px;margin-right: 3px;" class="validate[required]"></td>
			</tr>
			<tr>
				<td><label>Type: <span style="color:red;">*</span></label></td>
				<td><select class="" id="releasesTypeID" name="ReleasesTypeName" >
		             	<option value = "-1">--Select--</option>
		              	<option value = "1">Drop Ship</option>
		             	<option value = "2">Stock Order</option>
		             	<option value = "3">Bill Only</option>
		              	<option value = "4">Commission</option>
		              	<option value = "5">Service</option>
		              </select>
		         </td>
			</tr>
			<tr style="display: none;">
				<td><label>ManufacturerId: </label></td>
				<td><input type="text" id="ManufacturerId" name="ManufacturerId" style="width: 200px;" />
			</tr>
			<tr>
				<td ><label>Manufacturer:<span style="color:red;">*</span></label></td>
				<td><input type="text" id="ReleasesManuID" name="ReleasesManuName" class="validate[required]" style="width: 200px;" placeholder="Minimum 2 characters required"></td>
				<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
			</tr>
			<tr style="display: none;">
				<td><label>veFactoryID: </label></td>
				<td><input type="text" id="veFactoryId" name="veFactoryId" style="width: 200px;" />
			</tr>
			<tr>
				<td><label>Notes: </label></td>
				<td><input type="text" id="NoteID" name="NoteName" style="width: 200px;"/>
			</tr>
			<tr>
				<td><label>Allocated: </label></td>
				<td><input type="text" id="AllocatedID" name="AllocatedName" class="validate[custom[number]]" style="width: 200px;text-align: right;"/>
			</tr>
			<tr id="poAmountFieldID" style="display: none;">
				<td><label>PO Amount: </label></td>
				<td><input type="text" id="POAmountID" name="POAmountName" class="validate[custom[number]]" style="width: 200px;text-align: right;"/>
			</tr>
			<tr id="invoiceAmountFieldID" style="display: none;">
				<td><label>Invoice Amount: </label></td>
				<td><input type="text" id="ReleasesInvoiceID" name="ReleasesInvoiceName" class="validate[custom[number]]" style="width: 200px;text-align: right;"/>
			</tr>
		</table>
		<hr>
		<table align="right">
		<tr>
				<td><label>Customer PO: </label></td>
				<td>
					<Select  id="customerPONumberSelectID" name="customerPONumberName" style="width: 155px;">
						<option value="-1"> - Select - </option>
							<c:if test="${not empty requestScope.joMasterDetails.customerPonumber}">
							 	<option value="1" >
							 		<c:out value="${requestScope.joMasterDetails.customerPonumber} (${requestScope.custPONoDetails[0].podesc0})"></c:out> 
							 	</option>
							 </c:if>
							<c:forEach var="CustomerPONumber" items="${requestScope.custPONoDetails}">
								 <c:if test="${not empty CustomerPONumber.customerPonumber1}">
								 	<option value="2" >
								 		<c:out value="${CustomerPONumber.customerPonumber1} (${CustomerPONumber.podesc1})"></c:out>
								 	</option>
								</c:if>
								<c:if test="${not empty CustomerPONumber.customerPonumber2}">
									 <option value="3" >
								 		<c:out value="${CustomerPONumber.customerPonumber2} (${CustomerPONumber.podesc2}) " ></c:out>
								 	</option>
								</c:if>
								<c:if test="${not empty CustomerPONumber.customerPonumber3}">
								 	<option value="4" >
								 		<c:out value="${CustomerPONumber.customerPonumber3} (${CustomerPONumber.podesc3})" ></c:out>
								 	</option>
								</c:if>
								<c:if test="${not empty CustomerPONumber.customerPonumber4}">
								 	<option value="5" >
								 		<c:out value="${CustomerPONumber.customerPonumber4} (${CustomerPONumber.podesc4})" ></c:out>
								 	</option>
								 </c:if>
								 <c:if test="${not empty CustomerPONumber.customerPonumber5}">
								 	<option value="6" >
								 		<c:out value="${CustomerPONumber.customerPonumber5} (${CustomerPONumber.podesc5})" ></c:out>
								 	</option>
								 </c:if>
							</c:forEach>
					</select>
		        </td>
		        <td style="display:none"><input type="text" id="customerpodetails" name="CustomerPOdetails" value="${requestScope.joMasterDetails.customerPonumber}"></td>
				<td><input type="button" id="saveReleaseID" class="savehoverbutton turbo-tan" name="saveReleaseNam" value="Save" onclick="saveRelease()" style="width: 90px;"></td>
				<td><input type="button" id="cancelReleaseID" class="savehoverbutton turbo-tan" name="cancelReleaseNam" value="Cancel" onclick="cancelRelease()" style="width: 90px;"></td>
			</tr>
		</table>
	</form>
</div>
<div id="openBillNoteDialog">
	<form action="" id="openBillNoteDialogForm">
		<table>
			<tr>
				<td><!-- <label>Enter the Bill Note: </label> --></td>
			</tr>
			<tr>
				<td><textarea id="billNote" cols="70" rows="100"></textarea></td>
			</tr>
		</table>
		<hr>
		<table align="right">
			<tr>
				<td><input type="button" id="saveReleaseID" class="savehoverbutton turbo-tan" name="saveReleaseNam" value="Save" onclick="saveBillNote()" style="width: 90px;"></td>
				<td><input type="button" id="cancelReleaseID" class="savehoverbutton turbo-tan" name="cancelReleaseNam" value="Cancel" onclick="cancelBillNote()" style="width: 90px;"></td>
				<td><input type="text" id="hiddenInvoiceId" style="display: none;"/></td>
			</tr>
		</table>
	</form>
</div>

<div><jsp:include page="../PoRelease.jsp"></jsp:include></div>
<div><jsp:include page="../salesOrder.jsp"></jsp:include></div>
<div style="display: none;">
	
	<input type="text" id="contractAmount_release" value="${requestScope.estimatedAmount}">
	<input type="text" id="userInitials" value="${sessionScope.user.initials}">
	<input type="text" id="userInitials1" value="${requestScope.custPONo}">
	<input type="text" id="hiddenaccountId" value="${requestScope.SysAccountLinkage.coAccountIdap}">
	<input type="text" id="hiddennoticeId" value="${requestScope.joMasterDetails.noticeId}">
	<input type="text" id="hiddencontactId" value="${requestScope.joMasterDetails.contactId}">
	<input type="text" id="hiddenContactNameId" value="${requestScope.joMasterDetails.contactName}">
	<input type="text" id="hiddennotice" value="${requestScope.joMasterDetails.notice}">
	<input type="hidden" name="credittabfinwaiverchck" id="credittabfinwaiverchck" value="${requestScope.joMasterDetails.lienWaverThroughFinal}">
	<input type="text" id="otherContactChkID" value="${requestScope.joMasterDetails.otherContact}">
</div>
<div id="openSplitcommreleaseDia">
	<form action="" id="opensplitcommisionreleaseForm">
  	<input type="hidden" name="releaserepId" id="releaserepId" >
	<input type="hidden" name="releasesplittypeId" id="releasesplittypeId" >  
	<input type="hidden" name="jobreleasehiddenId" id="jobreleasehiddenId" >  
	<input type="hidden" name="jorowhiddenId" id="jorowhiddenId" > 
	<input type="hidden" name="jotitlehiddenId" id="jotitlehiddenId" >
		<div id="addsplitcommissionrelease"></div>
		<table>
		        <tr>
					<td>
					<span id='CommissionSplitsGridDivID'>
						<table id="releaseCommissionSplitsGrid"></table>
						<div id="releaseCommissionSplitsGridPager"></div>
					</span>
					</td>
				</tr>
		</table>
		<table style="display: none;">
		        <tr>
					<td>
					<span id='showCommissionSplitsGridDivID'>
						<table id="showReleaseCommissionSplitsGrid"></table>
						<div id="showReleaseCommissionSplitsGridPager"></div>
					</span>
					</td>
				</tr>
		</table>
	</form>
</div>

<div id= "veInvLineItemNote">
	<form action="" id="veInvLineItemNoteForm">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="lineItemNoteID" name="lineItemNoteName" style="height: 252px; width:570px;"></textarea>
	   				<input id="lineItemNoteLabelID" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" id= "SaveInlineNoteID" value="Save" onclick="SaveSoLineItemNote()" style=" width:80px;display:inline-block;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="SoCancelInLineNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
	</form>
</div> 

	<!--<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardRelease.min.js"></script> -->

	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script>
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/releaseemail.js"></script>
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobWizardCommissions.js"></script>
