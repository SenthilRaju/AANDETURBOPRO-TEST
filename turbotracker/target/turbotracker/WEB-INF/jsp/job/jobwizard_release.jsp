<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
			<fieldset class= " custom_fieldset"style="width:1080px;height: 360px">
				<legend class="custom_legend"><label><b>Releases</b></label></legend>
				<div>
					<form id="customerPOFormID">
						<table style="width: 1050px;">
			  				<tr>
						  		<td style="padding: 0px 20px 0px;width: 30%;" >
									PO Amount<input type="checkbox" class="PoCheck" value="poAmount" onchange="checkPoAmount()" style="vertical-align: middle;" id="poAmountID">&nbsp;
									Invoice Amount<input type="checkbox" class="InvoCheck" value="invoiceAmount" onchange="checkInvoAmount()" style="vertical-align: middle;" id="invoiceAmountID"></td>
								<td style=""><div id="releaseMessage" class="warningMsg"></div></td>
								<td style="padding-left: 10px;" align="right">
									<div>&nbsp;Customer PO:&nbsp;<Select  id="PONumberID">
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
				<div class="loadingDiv" id="loadingPODiv"> </div>
				<table style="width: 1080px;">
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
								  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOEmail()"></td>
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
<fieldset class= "custom_fieldset"style="width:1080px">
	<table id="shiping" style="width:20px"></table><div id="shipingpager"></div>
	<div style="margin-top: 10px;">
		<table>
			<tr>
				<td style="width: 30px;padding: 0px 1px;vertical-align: middle;">
					<fieldset class= "custom_fieldset" style="padding-bottom: 0px; ">
 						<table>
 							<tr>
		      					<td align="right" style="padding-right: 7px;vertical-align: middle;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Vendor Invoice" style="background: #EEDEBC; border: 0px;" onclick="addShipDetails()"></td>
				   			   	<td align="right" style="padding-right: 0px;vertical-align: middle;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Vendor Invoice" style="background: #EEDEBC; border: 0px;" onclick="deleteShipDetails()"></td>
 							</tr>
 						</table>
					</fieldset>
				</td>
				<td style="vertical-align: bottom;">
					<input type="button" class="track turbo-tan" value="Track" onclick="openReleaseTrack()">
					<input type="button" value="Vendor Invoice" class="vendorinvoice turbo-tan" onclick="openvendorinvoicedialog()">
					<input type="button" value="Customer Invoice" class="customerinvoice turbo-tan" onclick="opencustomerinvoicedialog()">
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
				<td><label>Type: </label></td>
				<td><select class="" id="releasesTypeID" name="ReleasesTypeName" >
		             	<option value = "-1">--Select--</option>
		              	<option value = "1" selected="selected">Drop Ship</option>
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
					<Select  id="customerPONumberSelectID" name="customerPONumberName">
						<option value="-1"> - Select - </option>
							<c:if test="${not empty requestScope.joMasterDetails.customerPonumber}">
							 	<option value="1" >${requestScope.joMasterDetails.customerPonumber} (${requestScope.custPONoDetails[0].podesc0}) </option>
							 </c:if>
							<c:forEach var="CustomerPONumber" items="${requestScope.custPONoDetails}">
								 <c:if test="${not empty CustomerPONumber.customerPonumber1}">
								 	<option value="2" >${CustomerPONumber.customerPonumber1} (${CustomerPONumber.podesc1} )</option>
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
				<td><label>Enter the Bill Note: </label></td>
			</tr>
			<tr>
				<td><textarea id="billNote" cols="35" rows="3"></textarea></td>
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
</div>
	<!--<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardRelease.min.js"></script> -->

	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script>

