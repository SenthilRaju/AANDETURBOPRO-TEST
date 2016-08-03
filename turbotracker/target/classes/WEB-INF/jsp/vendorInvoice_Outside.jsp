<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<style>
.ui-jqgrid tr.jqgrow td {
		text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
.ui-jqgrid tr.jqgrow td { text-overflow: ellipsis !important; }
</style>
<div id="addNewVendorInvoiceFromPODlg" style="width:736px; ">
<input type="hidden" id="rxMasterIDPO" name="rxMasterIDPO"> 
<input type="hidden" id="rxMasterIDPayablePO" name="rxMasterIDPayablePO">
<input type="hidden" id="vepoidPO" name="vepoidPO">
<input type="hidden" id="veBillIdPO" name="veBillIdPO">
<input type="hidden" id="TaxID_vendorinvoice" name="TaxID_vendorinvoice" value="0">
			<form id="addNewVendorInvoiceFromPOForm">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Details</b></label></legend>
						<div>
						<span style="font-weight:bold;margin-left:400px;font-size: 13px; display:none;" id="paidStatus">
								<a href="javascript:showPaymentDetailsDialog();">Check #<span id="chk_nofmPO" style="margin-left: 5px;margin-right:20px"></span>Paid Date:<span id="date_paidfmPO" style="margin-left: 5px;"></span></a></span>
						<input type="button" id="viStatusButtonPO" name="viStatusButtonPO" value="Open" class="savehoverbutton turbo-blue" style="width: 90px;height: 25px;float: right;" onClick="vendorInvoiceStatusForPO();" />
						<input type="text" style="visibility: hidden" id="viStatusButtonPOtxt" name="viStatusButtonPOtxt" />
						</div>
						<table style = "margin: 0 auto;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all" style="height: 104px;margin-top: -81px;">
									<legend><label class="fontclass" style="height: 150px;"><b>Payable To</b></label></legend>
									<table>
										<tr>
											<td><input type="text" style="border-width: 1px;" id="payablePO" name="payable" placeholder="Minimum 1 characters required to get Vendor list"><span style="color:red;">*</span></td>
											<td></td>
											<td></td>											
											<td></td>
											<td></td>
										</tr>
										<tr><td><span id="vendorAddressPO"></span></td></tr>
										<tr><td><span id="vendorAddress1PO"></span></td></tr>	
									</table>
								</fieldset>
								</td>
								<td>
								<fieldset class= " ui-widget-content ui-corner-all" style="width: 605px;height: 190px;">
								<legend><label class="fontclass"><b>Invoice/Bill</b></label></legend>
								<table>
									<tr>
										<td><label class="fontclass">Rec'vd:</label></td><td><input type="text" class="datepicker" id="recDateIdPO" name="recDateIdPO" readonly="readonly" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<td><label class="fontclass">Vendor Inv:<span id="veInvnomandatory" data-manvalue = "0" style="display:none;color:red">*</span></label></td><td><input type="text" id="vendorInvoicePO" name="vendorInvoicePO" style="width: 77%;margin-left: 14px;" maxlength="20"></td>
									</tr>
									<tr>
										<td><label class="fontclass">Dated:</label></td><td><input type="text" class="datepicker" id="datePO"readonly=" readonly" name="datePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"><input type="hidden" id ="duedaysfmpo"/></td>
										<td width=70><label class="fontclass">A/P Acct :</label></td><td><select style="width: 81%;margin-left: 15px;" id="apacctPO" name="apacctPO" class="dropdownfont fontclass" onchange="changeapacct();">
														<option value="0"> - Select - </option>
															<c:forEach var="accounts" items="${requestScope.coAccount}">
																<option value="${accounts.coAccountId}">
																	<c:out value="${accounts.number}" ></c:out> | <c:out value="${accounts.description}" ></c:out>
																</option>
														</c:forEach>
												</select></td>
												</tr>
												<tr>
										<td><label class="fontclass">Due:</label></td><td><input type="text" class="datepicker" id="duePO" name="duePO" readonly="readonly" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<%-- <td style="width: 80px;"><label class="fontclass">Post Date:</label></td><td><input type="checkbox" id="postdatePO" name="postdatePO"  style="margin-left: 23px;"><input type="text" class="datepicker" id="postDate1PO" name="postDate1PO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;"></td> --%>
										<td><label class="fontclass">Ship:</label></td><td><input type="text" class="datepicker" id="shipDatePO" readonly="readonly" name="shipDatePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;margin-left: 15px;"></td>
									</tr>
									<tr>
										<td><label class="fontclass">PO#</label></td><td><span id="ponumberPO"></span></td>
										<%-- <td><label class="fontclass">Ship:</label></td><td><input type="text" class="datepicker" id="shipDatePO" readonly="readonly" name="shipDatePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;margin-left: 37px;"></td> --%>
										<td><label class="fontclass">Pro #</label></td><td><input type="text" id="proPO" name="proPO" style="width: 77%;margin-left: 15px;"></td>
									</tr>
									<tr>
									
										<td style="width: 80px;"><label class="fontclass">Ship Via :</label></td><td><select style="width: 74%;" id="shipviaPO" name="shipviaPO" class="dropdownfont fontclass" onchange="">
														<option value="0"> - Select - </option>
															<c:forEach var="shipViaBean" items="${requestScope.shipVia}">													
																<option value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
															</c:forEach>
												</select></td>
									<td class="invReason">
											<label class="fontclass">Reason :</label></td><td class="invReason"><input type="text" id="invwithPoReason" name="invwithPoReason" readonly="readonly" style="width: 77%;margin-left: 15px;"></span>
									</td>
												<!-- <td><label class="fontclass">Pro #</label></td><td><input type="text" id="proPO" name="proPO" style="width: 77%;margin-left: 37px;"></td> -->
												
									</tr>
									
									<tr>
									<td colspan ="2">
									<span style ="color:red;display:none" id="mandveinvno" >Vendor Invoice# is Mandatory</span>  
									</td>
									</tr>
									
								</table>
								
											<div id="invreasondialog" style="display: none;">
											<form name="invreasonformname">
											<table>
											<tr><td>Reason:<label style="color: red;">*</label></td><td><textarea id="invreasonttextid" value="" cols="35" rows="5"></textarea></td></tr>
											<tr><td colspan="2"><label id="inverrordivreason" style="color: red;"></label></td></tr>
											</table>
											</form>
											</div>
											
											<div id="showInvoiceInfoDialog" style="display: none;">
											<form name="showInvoiceInfoDialogName">
											<table width="100%" id="invoiceDetailspopup">
											
											</table>
											</form>
											</div>
											
								
								</fieldset>
								</td>
							</tr>
							<tr>
									<td>
										<fieldset class= " ui-widget-content ui-corner-all" style="height: 66px;margin-top: -83px;">
										<legend><label class="fontclass" style="height: 150px;"><b>Job Name</b></label></legend>
										<table>
									<tr>
										<td><!-- <a href="#" onclick="drillintojob()" style="text-decoration:none;"> --><label id="jobName" class="fontclass"></label><!-- </a> -->
										<input type="hidden" id = "joreleasedetid" />
										</td>
									</tr>
									</table>
										</fieldset>
									</td>
							</tr>
							</table>
							</form>
							<table style="margin: 0 auto;">
							<tr>
								<td>
								<!--	<fieldset class= " ui-widget-content ui-corner-all">
								 	<legend><label class="fontclass"><b>Line Items</b></label></legend> -->
									<table>
										<tr>
										<td>
											<div id="jqgridLine">
												<table id="lineItemGrid"></table>
												<div id="lineItemPager" style="display: none;"></div>
											</div>
										</td>
									</tr>
									</table>
									<!--</fieldset> -->
								</td>								
							</tr>
							</table >
							<form id="addNewVendorInvoiceFromTotalForm">
							<table style="margin: 0 auto;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Totals</b></label></legend>
									<table style="width: 850px">
										<tr>										
											<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 100px; text-align:right" id="subtotalGeneralId" name="" value="0.00" readonly="readonly"></td>
											<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" name="freightGeneralName"  value="0.00"></td>
											<td><label>Tax: </label></td><td><fmt:setLocale value="en_US"/><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName"  value="0.00"></td>
											<td><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalGeneralId" name="totalGeneralName" readonly="readonly" value="0.00"></td>	
											<td><label>Bal Due: </label></td><td><input type="text" style="width: 100px; text-align:right" id="balDuePO" name="balDuePO" readonly="readonly" value="0.00"></td>
											<!-- <td style="display: none;"></td>
											<td style="display: none;"></td> -->
										</tr>										
									</table>
									</fieldset>
								</td>								
							</tr>
					</table>
					</form>
					<input type="hidden" style="width: 60px; text-align:right" id="vePOID" name="vePOName" readonly="readonly" value="">
					<input type="hidden" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" readonly="readonly" value="">
					<div style ="margin-top:10px;float:right">
					<span style="color:green" id="saveStatus"></span>
					<input type="button" id="addNewVeInvFmDlgbuttonsave" name="saveTermsButtonName" value="Save" class="savehoverbutton turbo-blue addinv1" onclick="SaveVendorInvoicewithPO('Save')" style="display:inline-block; " />
					<!-- <input type="button" id="addNewVeInvFmDlgbuttonsaveandclose" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue addinv1" onclick="addVendorInvoiceFromPO('close')" style="display:inline-block; " /> -->
					<input type="button" id="addNewVeInvFmDlgclsbutton" name="saveTermsButtonName" value="Close" class="savehoverbutton turbo-blue addinv1" onclick="SaveVendorInvoicewithPO('close')"  />
					</div>
				</fieldset>
				
				

			
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