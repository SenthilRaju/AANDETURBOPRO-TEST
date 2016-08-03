<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <div id=cusinvoicetab>
	<ul>
	   <li><a href="#soreleasegeneral">General</a></li>
	   <li><a href="#soreleaselineitem">Line Items</a></li>
	    <li><a href="#hidden" style="display: none;">Hidden</a></li>
	</ul>
	
	<div id="soreleasegeneral">
	<form id="custoemrInvoiceFormID">
		<table>
		<tr><td>
		<fieldset class= "custom_fieldset" style="width:370px">
			<legend class="custom_legend"><label><b>Customer</b></label><span style="color:red;" class="mandatory"> *</span></legend>
				<table>
					<tr><td><input type="text" style="width:328px" id="customerInvoice_customerInvoiceID" readonly="readonly" name="customerInvoice_customerInvoiceName"></td>
						<td style="display: none;"><input type="text" id="customerInvoice_customerHiddnID" name="customerInvoice_customerHiddnName"></td>
						<td><input type="text" id="customerInvoice_ID" name="customerInvoice_Name" style="display: none"></td>
					</tr>
					<tr><td><label>Date:&nbsp;</label><input type="text" class="datepicker" style="width:70px" id="customerInvoice_invoiceDateID" name = "customerInvoice_invoiceName">
							<label style="padding-left: 10px">Number:&nbsp;</label>&nbsp;<input type="text" style="width:139px;" id="customerInvoice_invoiceNumberId"  name="customerInvoice_invoiveNumberName">
						</td>
						<td></td>
					</tr>
				</table>
		</fieldset>
		</td>
		<td style="width:30px"></td>
		<td style="vertical-align:top">
		<fieldset style="width:370px;height: 89px" class= "custom_fieldset">
		<legend class="custom_legend"><label><b>Shipping Details</b></label></legend>
			<table>
				<tr>
					<td><label>Whse:</label></td>
					<td><select id="prWareHouseSelectID" name="prWareHouseSelectName" style="width:110px">
							<option value="-1"> - Select - </option>
							<c:forEach var="prWareHouseBean" items="${requestScope.prWareHouseDetails}">
								<option value="${prWareHouseBean.prWarehouseId}"><c:out value="${prWareHouseBean.searchName}" ></c:out></option>
							</c:forEach>
						</select>
					</td>
					<td><label>Via:</label></td>
					<td><select id="shipViaCustomerSelectID" name="shipViaCustomerSelectName">
							<option value="-1"> - Select - </option>
							<c:forEach var="veShipCustomerViaBean" items="${requestScope.veShipViaDetails}">
								<option value="${veShipCustomerViaBean.veShipViaId}"><c:out value="${veShipCustomerViaBean.description}" ></c:out></option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label>Ship:</label></td>
					<td><input type="text" class="datepicker" style="width:100px" id="customerInvoice_shipDateID" name="customerInvoice_shipDateName"></td>
					<td><label>Pro #:</label></td>
					<td><input type="text" style="width:73%" id="customerInvoice_proNumberID" name="customerInvoice_proNumberName"></td>
				</tr>
		  </table>
		</fieldset>	
		</td></tr>
		</table>
		
		<table>
		<tr><td style="vertical-align:top">
	  	<fieldset class= "custom_fieldset" style="width:370px;height: 125px">
			<legend class="custom_legend"><label><b>Bill To</b></label></legend>
				<table>
					<tr>
 						<td><input type="text" id="customerbillToAddressIDcuInvoice" name="customerbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 					</tr>
	 				<tr>
	 					<td><input type="text" id="customerbillToAddressID1cuInvoice" name="customerbillToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
	 				</tr>
	 				<tr>
						<td><input type="text" id="customerbillToAddressID2cuInvoice" name="customerbillToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
					</tr>
 					<tr>
 						<td><input type="text" id="customerbillToCitycuInvoice" name="customerbillToCity" style="width: 100px;" disabled="disabled">
								<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
 								<input type="text" id="customerbillToStatecuInvoice" name="customerbillToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled">
 								<label>Zip: </label><input type="text" id="customerbillToZipIDcuInvoice" name="customerbillToZip" style="width: 75px;" disabled="disabled">
 						</td>
 					</tr>
 				</table>
		</fieldset>
		<fieldset  class= "custom_fieldset" style="width:370px;height: 45px">
		<legend class="custom_legend"><label><b>Job #${requestScope.joMasterDetails.jobNumber}</b></label></legend>
			<table>
				<tr><td><input type="text" readonly="readonly" value="${requestScope.joMasterDetails.description}" style="width: 280px;"></td></tr>
			</table>
		</fieldset>
		</td>
		<td style="width:20px"></td>
		<td style="vertical-align:top">
		<fieldset class= "custom_fieldset" style="width:370px;height: 205px">
			<legend class="custom_legend"><label><b>Ship To</b></label>
			</legend>
			<table  id="lineshipTo">
				<tr>
 					<td><input type="text" id="customerShipToAddressID" name="customerShipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="customerShipToAddressID1" name="customerShipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="customerShipToAddressID2" name="customerShipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="customerShipToCity" name="customerShipToCity" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="customerShipToState" name="customerShipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="customerShipToZipID" name="customerShipToZip" style="width: 75px;" disabled="disabled">
								<input type="button" id="customerbackWardId" value="" onclick="customershipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer ">
							<input type="button" id="customerforWardId" value="" onclick="customershipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table  id="lineshipTo1">
				<tr>
 					<td><input type="text" id="customerShipToAddressID4" name="customerShipToAddressID4" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
 					<td><input type="text" id="customerShipToAddressID5" name="customerShipToAddress5" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
 				</tr>
 				<tr>
					<td><input type="text" id="customerShipToAddressID3" name="customerShipToAddress3" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
				</tr>
					<tr>
						<td><input type="text" id="customerShipToCity1" name="customerShipToCity1" style="width: 100px;" disabled="disabled">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
								<input type="text" id="customerShipToState1" name="customerShipToState1" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
								<label>Zip: </label><input type="text" id="customerShipToZipID1" name="customerShipToZip1" style="width: 75px;" disabled="disabled">
								<input type="button" id="customerbackWardId" value="" onclick="customershipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer  ">
							<input type="button" id="customerforWardId" value="" onclick="customershipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer ">
						</td>
					</tr>
				</table>
				<table>
					<tr align="left">
					<td style="vertical-align: bottom;padding-left: 50px;">
						<input type="button" id="usinvoiceShipto" class="usinvoiceShip" onclick="usinvoiceShiptoAddress()" style="background: url(./../resources/images/us.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none ">
						<input type="button" id="customerinvoiceShipto" class="customerinvoiceShip" onclick="customerinvoiceShiptoAddress()" checked="checked" style="background: url(./../resources/images/customer_select.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px;border:none">
						<input type="button" id="jobsiteinvoiceShipto" class="jobsiteinvoiceShip" onclick="jobsiteinvoiceShiptoAddress()" style="background: url(./../resources/images/jobsite.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none ">
						<input type="button" id="otherinvoiceShipto" class="otherinvoiceShip" onclick="otherinvoiceShiptoAddress()" style="background: url(./../resources/images/other.png) no-repeat;vertical-align: bottom;background-position: center; cursor:pointer ; width:63px;height: 28px ;border:none">
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
				<fieldset class= "custom_fieldset" style="width:370px;height: 161px">
					<legend class="custom_legend"><label><b>Employee's Assigned</b></label></legend>
					<table>
							<tr>
								<td><label>Salesrep: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_salesRepsList" name="customerInvoice_assignedSalesRep" style="width: 200px;" value="" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_salesRepId" name="customerInvoice_assignedSalesRepId" value="" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>CSR: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_CSRList" name="customerInvoice_assignedCSR" style="width: 200px;" value="" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_CSRId" value="" name="customerInvoice_assignedCSRId" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Sales Manager: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_SalesMgrList" name="customerInvoice_assignedSalesMgr" style="width: 200px;" value="" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_salesMgrId" name="customerInvoice_assignedSalesMgrId" value="" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Engineer: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_EngineersList" name="customerInvoice_assignedEngineer" style="width: 200px;" value="" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_engineerId" name="customerInvoice_assignedEngineerId" value="" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Project Manager: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_PrjMgrList" name="customerInvoice_assignedPrjMgr" style="width: 200px;" value="" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_prjMgrId" name="customerInvoice_assignedPrjMgrId" value="" style="width: 30px;"></td>
							</tr>
						</table>
				</fieldset>
			</td>
			<td style="width:10px"></td>
			<td style="vertical-align:top">
				<fieldset style="width:370px;padding-bottom:10px;height: 170px" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Misc</b></label></legend>
				<table>
					<tr>
						<td><label>Division:</label></td>
						<td><select  id="customer_Divisions" name="customer_DivisionName" class="validate[required] select" style="width: 60%;">
								<option value="-1"> - Select - </option>
								<c:forEach var="coDivisionCustomerBean" items="${requestScope.divisions}">
									<option value="${coDivisionCustomerBean.coDivisionId}"><c:out value="${coDivisionCustomerBean.description}" ></c:out></option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr><td><label>Customer PO #:</label></td>
						<td><input type="text" style="width: 56%;" id="customerInvoie_PONoID" name="customerInvoie_PONoName"><label style="padding-left: 5px">Do Not Mail:</label><input type="checkbox" id="customerInvoie_doNotMailID" name="customerInvoie_doNotMailName"></td>
					</tr>
					<tr><td><label>Tax Territory:</label></td>
								<td><input  type="text" id="customerInvoice_TaxTerritory" style="width: 56%;" value="${requestScope.taxTerritory}"  placeholder="Min. 2 char. required">
									<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
								  		  <input type="hidden" id="customerTaxTerritory" name="customerTaxPersent"  value="${requestScope.joMasterDetails.coTaxTerritoryId}" style="width: 30px;">
											<label id = "customer_TaxTextValue" style="display: none;">%</label></td>
					</tr>
					<tr><td><label>Terms:</label></td><td>
							<input type="text" id="customerinvoice_paymentTerms" name="customerinvoice_TermsName" value="${requestScope.customerTabFormDataBean.customerTerms}" style="width: 56%;" title="" placeholder="Min. 2 char. required">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-right: 10px">
							<input type="hidden" id="customerinvoicepaymentId" value="${requestScope.customerMasterObj.cuTermsId}" name="customerinvoicePaymentTermsName" style="width: 30px;"></td>
						</tr>
						<tr>
							<td>
								<label>Due Date:</label>
							</td>
							<td>
								<input type="text" id="customerInvoice_dueDateID"  name="customerInvoice_dueDateName" style="width:54%;padding-left: 10px" class="datepicker">
							</td>
						</tr>
		       </table>
		      </fieldset>
			</td>
		</tr>
	</table>
		<fieldset class= "custom_fieldset" style="width:770px">
		<legend class="custom_legend"><label><b>Totals</b></label></legend>
	   		<table style="width:700px">
	   			<tr align="center">
		   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="customerInvoice_subTotalID"  readonly="readonly"></td>
		       		<td><label>Frieght:</label></td><td><input type="text" style="width:85px" id="customerInvoice_frightIDcu"  readonly="readonly"></td>
		   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxIdcu"  readonly="readonly"></td>
		       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="customerInvoice_totalID"  readonly="readonly"></td>
	      	 	</tr>
	 		 </table>
		</fieldset>
		<br>
	<hr/>
	<table align="center" style="width:780px">
	 	<tr>
		 	<td></td>
		 	<td align="right" style="padding-right:1px;">
				<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="savecustomerinvoice()" style="width:80px; " >
			</td>
			<td align="right" width="80px"><input type="button" class="cancelhoverbutton turbo-tan" value="Close" onclick="cancelcustomerinvoice()" style="width:80px;" ></td>
		</tr>
	</table>
	</form>
	</div>

	<div id="soreleaselineitem">
	<div id="InfoSave" style="display: none; font-size: 17px;margin-left: 120px;height:210px;padding-top:200px; middle;color: red; font-family: Verdana,Arial,sans-serif;">Please Save the PO general tab and try reloading the Line items Grid</div>
	<div id="regularTab">
	<table>
		<tr><td>
		<fieldset class= "custom_fieldset" style="width:370px">
			<legend class="custom_legend"><label><b>Customer</b></label><span style="color:red;" class="mandatory"> *</span></legend>
				<table>
					<tr><td><input type="text" style="width:298px" id="customerInvoice_linecustomerInvoiceID" readonly="readonly" name="customerInvoice_linecustomerInvoiceName"></td><td style="display: none;"><input type="text" id="customerInvoice_linecustomerHiddnID" name="customerInvoice_linecustomerHiddnName"></td>
						<td><input type="text" id="customerInvoice_lineID" name="customerInvoice_lineName" style="display: none"></td>
					</tr>
					<tr><td><label>Date:&nbsp;</label><input type="text" class="datepicker" style="width:70px" id="customerInvoice_lineinvoiceDateID" name = "customerInvoice_lineinvoiceName">
							<label style="padding-left: 10px">Number:&nbsp;</label>&nbsp;<input type="text" style="width:108px;" id="customerInvoice_lineinvoiceNumberId"  name="customerInvoice_lineinvoiveNumberName">
							<input id="cuinvoiceIDhidden" type="text" style="display: none;"/>
						</td>
						<td></td>
					</tr>
				</table>
		</fieldset>
		</td>
		<td style="width:30px"></td>
		<td style="vertical-align:top">
		<fieldset style="width:370px;height: 89px" class= "custom_fieldset">
		<legend class="custom_legend"><label><b>Shipping Details</b></label></legend>
			<table>
				<tr>
					<td><label>Whse:</label></td>
					<td><select id="prWareHouseSelectlineID" name="prWareHouseSelectlineName" style="width:110px">
							<option value="-1"> - Select - </option>
							<c:forEach var="prWareHouseBean" items="${requestScope.prWareHouseDetails}">
								<option value="${prWareHouseBean.prWarehouseId}"><c:out value="${prWareHouseBean.searchName}" ></c:out></option>
							</c:forEach>
						</select>
					</td>
					<td><label>Via:</label></td>
					<td><select id="shipViaCustomerSelectlineID" name="shipViaCustomerSeleclinetName" style="width: 154px;">
							<option value="-1"> - Select - </option>
							<c:forEach var="veShipCustomerViaBean" items="${requestScope.veShipViaDetails}">
								<option value="${veShipCustomerViaBean.veShipViaId}"><c:out value="${veShipCustomerViaBean.description}" ></c:out></option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td><label>Ship:</label></td><td><input type="text" class="datepicker" style="width:100px" id="customerInvoice_lineshipDateID" name="customerInvoice_lineshipDateName"></td>
					<td><label>Pro #:</label></td><td><input type="text" style="width:141px" id="customerInvoice_lineproNumberID" name="customerInvoice_lineproNumberName"></td>
				</tr>
		  </table>
		</fieldset>	
		</td></tr>
		</table>
		<table>
		<tr><td><hr width="800px"></td></tr>
		</table>

		<table id="customerInvoice_lineitems"></table><div id="customerInvoice_lineitemspager"></div>
		<br>
      <fieldset class= "custom_fieldset" style="width:770px">
		<legend class="custom_legend"><label><b>Totals</b></label></legend>
   		<table style="width:700px">
   			<tr align="center">
	   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="customerInvoice_linesubTotalID" readonly="readonly"></td>
	       		<td><label>Frieght:</label></td><td><input type="text" style="width:85px" id="customerInvoice_linefrightID"  readonly="readonly"></td>
	       		<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_linetaxId" name="customerInvoice_taxName" value="${requestScope.taxValue}" readonly="readonly">%</td>
	   			<td><label></label></td><td><input type="text" style="width:85px" id="cuGeneral_taxvalue" name="customerInvoice_taxName" value="" readonly="readonly"></td>
	       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="customerInvoice_linetotalID"  readonly="readonly"></td>
      	 	</tr>
 		 </table>
		</fieldset> 
			<br>
			<hr/>
			<table align="center" style="width:780px">
			 	<tr>
				 	<td></td>
					<td align="right" width="80px"><input type="button" class="cancelhoverbutton turbo-tan" value="Close" onclick="cancelcustomerinvoice()" style="width:80px;" ></td>
				</tr>
			</table> 
		</div>
	</div>
	<div id="hidden">
	</div>
</div>
