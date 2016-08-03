<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <div id="creditRebuild" style="display:none;">
	<form id="creditRebuildFormID">
		<table>
		<tr><td style="vertical-align:top">
	  	<fieldset class= "custom_fieldset" style="width:370px;height: 190px">
			<legend class="custom_legend"><label><b>Bill To</b></label></legend>
				<table>
					<tr>
 						<td><input type="text" id="customerbillToAddressIDcuInvoiceRebuild" name="customerbillToAddressIDRebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 					</tr>
	 				<tr>
	 					<td><input type="text" id="customerbillToAddressID1cuInvoiceRebuild" name="customerbillToAddress1Rebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
	 				</tr>
	 				<tr>
						<td><input type="text" id="customerbillToAddressID2cuInvoiceRebuild" name="customerbillToAddress2Rebuild" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
					</tr>
 					<tr>
 						<td><input type="text" id="customerbillToCitycuInvoiceRebuild" name="customerbillToCityRebuild" style="width: 100px;" disabled="disabled">
								<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
 								<input type="text" id="customerbillToStatecuInvoiceRebuild" name="customerbillToStateRebuild" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled">
 								<label>Zip: </label><input type="text" id="customerbillToZipIDcuInvoiceRebuild" name="customerbillToZipRebuild" style="width: 75px;" disabled="disabled">
 						</td>
 					</tr>
 					<tr>
 						<td><label>Select Email: </label>
	 						<select id="emailListCURebuild" name="emailListCURebuild" style="width: 245px;">
										<option value="0"> -Select- </option>														
							</select>
						</td>												
					</tr>
 				</table>
		</fieldset>
		</td>
		<td style="width:20px"></td>
		<td style="vertical-align:top">
		<fieldset class= "custom_fieldset" style="width:370px;height: 190px">
			<legend class="custom_legend"><label><b>Ship To</b></label>
			</legend>
			<table  id="lineshipToRebuild">
				<tr>
 					<td><input type="text" id="customerShipToAddressIDRebuild" name="customerShipToAddressIDRebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="customerShipToAddressID1Rebuild" name="customerShipToAddress1Rebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="customerShipToAddressID2Rebuild" name="customerShipToAddress2Rebuild" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="customerShipToCityRebuild" name="customerShipToCity" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="customerShipToStateRebuild" name="customerShipToStateRebuild" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="customerShipToZipIDRebuild" name="customerShipToZipRebuild" style="width: 75px;" disabled="disabled">
								<input type="button" id="customerbackWardIdRebuild" value="" onclick="customershipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer ">
							<input type="button" id="customerforWardIdRebuild" value="" onclick="customershipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table  id="lineshipTo1Rebuild">
				<tr>
 					<td><input type="text" id="customerShipToAddressID4Rebuild" name="customerShipToAddressID4Rebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="customerShipToAddressID5Rebuild" name="customerShipToAddress5Rebuild" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="customerShipToAddressID3Rebuild" name="customerShipToAddress3Rebuild" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="customerShipToCity1" name="customerShipToCity1" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="customerShipToState1Rebuild" name="customerShipToState1Rebuild" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="customerShipToZipID1Rebuild" name="customerShipToZip1Rebuild" style="width: 75px;" disabled="disabled">
								<input type="button" id="customerbackWardIdRebuild" value="" onclick="customershipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer  ">
							<input type="button" id="customerforWardIdRebuild" value="" onclick="customershipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table>
					<tr align="left">
					<td style="vertical-align: bottom;padding-left: 50px;">						
						<input type="button" id="usinvoiceShiptoRebuildRebuild" class="usinvoiceShip" onclick="usinvoiceShiptoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none ">
						<input type="button" id="customerinvoiceShiptoRebuild" class="customerinvoiceShip" onclick="customerinvoiceShiptoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none">
						<input type="button" id="jobsiteinvoiceShiptoRebuild" class="jobsiteinvoiceShip" onclick="jobsiteinvoiceShiptoAddress()" style="background: url(./../resources/images/jobsite.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none ">
						<input type="button" id="otherinvoiceShiptoRebuild" class="otherinvoiceShip" onclick="otherinvoiceShiptoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none">
					</td>
				</tr>
				</table>
		</fieldset>
		</td>
		</tr>
		</table>
	
		<table>
		<tr>
			<td style="vertical-align:top">
				<fieldset  class= "custom_fieldset" style="width:370px;height: 45px">
				<legend class="custom_legend"><label><b>Job #${requestScope.joMasterDetails.jobNumber}</b></label></legend>
					<table>
				<tr><td><input type="text" readonly="readonly" value="${requestScope.joMasterDetails.description}" style="width: 280px;"></td></tr>
					</table>
				</fieldset>
				<fieldset  class= "custom_fieldset" style="width:370px;height: 114px">
				<legend class="custom_legend"><label><b>Credit Rebuild</b></label></legend>
					<table>
						<tr>
							<td><label>Credit&nbsp;Rebuild&nbsp;Reason&nbsp;Code: </label></td>
							
							<td>
									<select id="rebuildReasonID" name="rebuildReason" style="width: 145px;">
												<option value="0"> -Select- </option>														
									</select>
							</td>							
						</tr>
						<tr>
							<td><label>Original&nbsp;Invoice&nbsp;Number: </label></td>
							<td><input type="text" id="customerInvoice_invoiceNumberIdRebuild" name="originalInvoiceNo" style="width: 145px;"></td>							
						</tr>
						<tr>
							<td><label>New&nbsp;Invoice&nbsp;Number: </label></td>
							<td><input type="text" id="newInvoiceNumberID" name="newInvoiceNumber" style="width: 145px;"></td>							
						</tr>
					</table>
					</fieldset>
			</td>
			<td style="width:20px"></td>
			<td style="vertical-align:top">
				<fieldset style="width:370px;padding-bottom:10px;height: 177px" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Misc</b></label></legend>
				<table>
					<tr>
						<td><label>Division:</label></td>
						<td><select  id="customer_DivisionsRebuild" name="customer_DivisionNameRebuild" class="validate[required] select" style="width: 60%;">
								<option value="-1"> - Select - </option>
								<c:forEach var="coDivisionCustomerBean" items="${requestScope.divisions}">
									<option value="${coDivisionCustomerBean.coDivisionId}"><c:out value="${coDivisionCustomerBean.description}" ></c:out></option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr><td><label>Customer PO#:</label></td>
						<td><input type="text" style="width: 56%;" id="customerInvoie_PONoIDRebuild" name="customerInvoie_PONoNameRebuild"></td>
					</tr>
					<tr><td><label>Do Not Mail:</label></td>
						<td><input type="checkbox" id="customerInvoie_doNotMailIDRebuild" name="customerInvoie_doNotMailNameRebuild"></td>
					</tr>
					<tr><td><label>Tax Territory:</label></td>
								<td><input  type="text" id="customerInvoice_TaxTerritoryRebuild" style="width: 56%;" value="${requestScope.taxTerritory}"  placeholder="Min. 2 char. required">
									<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
								  		  <input type="hidden" id="customerTaxTerritoryRebuild" name="customerTaxPersentRebuild"  value="${requestScope.joMasterDetails.coTaxTerritoryId}" style="width: 30px;">
											<label id = "customer_TaxTextValueRebuild" style="display: none;">%</label></td>
					</tr>
					<tr><td><label>Terms:</label></td><td>
							<input type="text" id="customerinvoice_paymentTermsRebuild" name="customerinvoice_TermsNameRebuild" value="${requestScope.customerTabFormDataBean.customerTerms}" style="width: 56%;" title="" placeholder="Min. 2 char. required">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-right: 10px">
							<input type="hidden" id="customerinvoicepaymentIdRebuild" value="${requestScope.customerMasterObj.cuTermsId}" name="customerinvoicePaymentTermsName" style="width: 30px;"></td>
						</tr>
						<tr>
							<td>
								<label>Due Date:</label>
							</td>
							<td>
								<input type="text" id="customerInvoice_dueDateIDRebuild"  name="customerInvoice_dueDateNameRebuild" style="width:54%;padding-left: 10px" class="datepicker">
							</td>
						</tr>
		       </table>
		      </fieldset>
			</td>
		</tr>
	</table>
	<table id="customerInvoice_lineitemsRebuild"></table><div id="customerInvoice_lineitemspagerRebuild"></div>
		<br>
	
		<fieldset class= "custom_fieldset" style="width:770px">
		<legend class="custom_legend"><label><b>Totals</b></label></legend>
	   		<table style="width:750px">
	   			<tr align="center">
		   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="customerInvoice_subTotalIDRebuild"  readonly="readonly"></td>
		       		<td><label>Freight :</label></td><td><input type="text" style="width:85px" id="customerInvoice_frightIDcuRebuild"></td>
		   			<!-- <td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxIdcu"  readonly="readonly"></td> -->
		   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_generaltaxIdRebuild" name="customerInvoice_generaltaxNameRebuild" value="${requestScope.taxValue}" readonly="readonly"></td><td><span>%</span></td>
	   				<td><label></label></td><td><input type="text" style="width:85px" id="customerInvoice_taxIdcuRebuild" value="" readonly="readonly"></td>
		       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="customerInvoice_totalIDRebuild"  readonly="readonly"></td>
	      	 	</tr>
	 		 </table>
		</fieldset>
		<br>
	<hr/>
	
	<table align="center" style="width:780px">
	 	<tr align="right">
			<td><input type="button" id="CuInvoiceSaveCloseIDRebuild" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveCreditRebuild()" style="width:110px;position: relative; display:none;"></td>
			<input type="text" id="setButtonValue" style="display: none;" value=""/>
		</tr>
	</table>
	</form>

	<div id="InfoSave" style="display: none; font-size: 17px;margin-left: 120px;height:210px;padding-top:200px; middle;color: red; font-family: Verdana,Arial,sans-serif;">Please Save the PO general tab and try reloading the Line items Grid</div>
	
	</div>
</div>
