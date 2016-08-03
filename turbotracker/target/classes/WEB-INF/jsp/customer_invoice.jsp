<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<div id=cusinvoicetab style="margin: 0 auto">
<input type="hidden" id="CI_taxfreight" />
<input type="hidden" id="CI_taxsubtotal" />
	<ul>
		<li><a href="#soreleasegeneral" onclick="sogeneralclick()">General</a></li>
		<li><a href="#soreleaselineitem" onclick="solineitemclick()">Line
				Items</a></li>
		<li><a href="#hidden" style="display: none;">Hidden</a></li>
	</ul>

	<div id="soreleasegeneral">
		<form id="custoemrInvoiceFormID">
			<table style="margin: 0 auto;">
				<tr>
					<td colspan="3" align="right"><span id='dollarImage'></span><span
						id="checkRefs" style="vertical-align: top; display: none;"></span><span
						id="paymentDate" style="vertical-align: top;"></span></td>
				</tr>
				<tr>
					<td>
						<fieldset class="custom_fieldset"
							style="width: 370px; height: 89px">
							<legend class="custom_legend">
								<label><b>Customer</b></label><span style="color: red;"
									class="mandatory"> *</span>
							</legend>
							<table width="100%;">
								<tr>
									<td><input type="text" style="width: 328px"
										id="customerInvoice_customerInvoiceID" readonly="readonly"
										name="customerInvoice_customerInvoiceName"
										placeholder="Minimun 3 characters required"></td>
									<td style="display: none;"><input type="text"
										id="customerInvoice_customerHiddnID"
										name="customerInvoice_customerHiddnName"></td>
									<td><input type="text" id="customerInvoice_ID"
										name="customerInvoice_Name" style="display: none"></td>
								</tr>
								<tr>
									<td><label>Date:&nbsp;</label><input type="text"
										class="datepicker" style="width: 23%"
										id="customerInvoice_invoiceDateID"
										name="customerInvoice_invoiceName"> <label
										style="padding-left: 10px">Number:&nbsp;</label>&nbsp;<input
										type="text" style="width: 31%;"
										id="customerInvoice_invoiceNumberId" readonly="readonly"
										name="customerInvoice_invoiveNumberName"></td>
									<td></td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td style="width: 20px"></td>
					<td style="vertical-align: top">
						<fieldset style="width: 370px; height: 89px"
							class="custom_fieldset">
							<legend class="custom_legend">
								<label><b>Shipping Details</b></label>
							</legend>
							<table>
								<tr>
									<td><label>Whse:</label></td>
									<td><select id="prWareHouseSelectID"
										name="prWareHouseSelectName" style="width: 110px"
										onchange="changeDropDown('prWareHouseSelectlineID','prWareHouseSelectID')">
											<option value="-1">- Select -</option>
											<c:forEach var="prWareHouseBean"
												items="${requestScope.prWareHouseDetails}">
												<option value="${prWareHouseBean.prWarehouseId}"><c:out
														value="${prWareHouseBean.searchName}"></c:out></option>
											</c:forEach>
									</select></td>
									<td><label>Via:</label></td>
									<td><select id="shipViaCustomerSelectID"
										style="width: 150px" name="shipViaCustomerSelectName"
										onchange="changeDropDown('shipViaCustomerSelectlineID','shipViaCustomerSelectID')">
											<option value="-1">- Select -</option>
											<c:forEach var="veShipCustomerViaBean"
												items="${requestScope.veShipViaDetails}">
												<option value="${veShipCustomerViaBean.veShipViaId}"><c:out
														value="${veShipCustomerViaBean.description}"></c:out></option>
											</c:forEach>
									</select></td>
								</tr>
								<tr>
									<td><label>Ship:</label></td>
									<td><input type="text" class="datepicker"
										style="width: 100px" id="customerInvoice_shipDateID"
										name="customerInvoice_shipDateName"
										onchange="changeDropDown('customerInvoice_lineshipDateID','customerInvoice_shipDateID')"></td>
									<td><label>Pro#:</label></td>
									<td><input type="text"
										value="${requestScope.vendorInvoiceNumber}" style="width: 90%"
										id="customerInvoice_proNumberID"
										name="customerInvoice_proNumberName"
										onchange="changeDropDown('customerInvoice_lineproNumberID','customerInvoice_proNumberID')"></td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>

			<table style="margin: 0 auto;">
				<tr>
					<td style="vertical-align: top">
						<fieldset class="custom_fieldset"
							style="width: 370px; height: 146px">
							<legend class="custom_legend">
								<label><b>Bill To</b></label>
							</legend>
							<table width="100%">
								<tr>
									<td><input type="text"
										id="customerbillToAddressIDcuInvoice"
										name="customerbillToAddressID" class="validate[maxSize[100]"
										style="width: 300px;" disabled="disabled"></td>
								</tr>
								<tr>

									<td><input type="text"
										id="customerbillToAddressID1cuInvoice"
										name="customerbillToAddress1" class="validate[maxSize[100]"
										style="width: 300px;" disabled="disabled"></td>
								</tr>
								<tr>
									<td><input type="text"
										id="customerbillToAddressID2cuInvoice"
										name="customerbillToAddress2" class="validate[maxSize[40]"
										style="width: 300px;" disabled="disabled"></td>
								</tr>
								<tr>
									<td><input type="text" id="customerbillToCitycuInvoice"
										name="customerbillToCity" style="width: 100px;"
										disabled="disabled"> <img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										style="display: none;"> <input type="text"
										id="customerbillToStatecuInvoice" name="customerbillToState"
										style="width: 30px; text-transform: uppercase" maxlength="2"
										disabled="disabled"> <label>Zip: </label><input
										type="text" id="customerbillToZipIDcuInvoice"
										name="customerbillToZip" style="width: 75px;"
										disabled="disabled"></td>
								</tr>
								<tr>
									<td><label>Select Email: </label> <select id="emailListCU"
										name="emailListCU" style="width: 245px;">
											<option value="0">-Select-</option>
									</select></td>
								</tr>
							</table>
						</fieldset>
						<fieldset class="custom_fieldset"
							style="width: 370px; height: 45px">
							<legend class="custom_legend">
								<label><b>Job
										#${requestScope.joMasterDetails.jobNumber}</b></label>
							</legend>
							<table>
								<tr>
									<td><input type="text" id="jobnodescription"
										name="jobnodescriptionname"
										value="${requestScope.joMasterDetails.description}"
										style="width: 280px;"></td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td style="width: 20px"></td>
					<td style="vertical-align: top">
						<fieldset class="custom_fieldset" style="width: 370px; height: 207px">
						<legend class="custom_legend"><label><b>Ship To</b></label>	</legend>
							<div id="CI_Shipto">
							<%@ include file="ShipTo.jsp" %>
							</div>
						</fieldset>
					</td>
					
				</tr>
			</table>

			<table style="margin: 0 auto;">
				<tr>
					<td style="vertical-align: top">
						<fieldset class="custom_fieldset"
							style="width: 370px; height: 190px">
							<legend class="custom_legend">
								<label><b>Employee's Assigned</b></label>
							</legend>
							<table width="100%">
								<tr>
									<td><label id="JobCu_inv_Category1label"
										style="display: inline-block;">Salesrep: </label></td>
									<td>&nbsp;<input type="text"
										id="customerInvoice_salesRepsList"
										name="customerInvoice_assignedSalesRep"
										style="width: 200px; display: inline-block;" value="" title=""
										placeholder="Minimum 2 characters required"></td>
									<td><img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										id="JobCu_inv_Category1image" style="display: inline-block;"></td>
									<td><input type="hidden" id="customerInvoice_salesRepId"
										name="customerInvoice_assignedSalesRepId" value=""
										style="width: 30px;"></td>
								</tr>
								<tr>
									<td><label id="JobCu_inv_Category2label"
										style="display: inline-block;">CSR: </label></td>
									<td>&nbsp;<input type="text" id="customerInvoice_CSRList"
										name="customerInvoice_assignedCSR"
										style="width: 200px; display: inline-block;" value="" title=""
										placeholder="Minimum 2 characters required"></td>
									<td><img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										id="JobCu_inv_Category2image" style="display: inline-block;"></td>
									<td><input type="hidden" id="customerInvoice_CSRId"
										value="" name="customerInvoice_assignedCSRId"
										style="width: 30px;"></td>
								</tr>
								<tr>
									<td><label id="JobCu_inv_Category3label"
										style="display: inline-block;">Sales Manager: </label></td>
									<td>&nbsp;<input type="text"
										id="customerInvoice_SalesMgrList"
										name="customerInvoice_assignedSalesMgr"
										style="width: 200px; display: inline-block;" value="" title=""
										placeholder="Minimum 2 characters required"></td>
									<td><img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										id="JobCu_inv_Category3image" style="display: inline-block;"></td>
									<td><input type="hidden" id="customerInvoice_salesMgrId"
										name="customerInvoice_assignedSalesMgrId" value=""
										style="width: 30px;"></td>
								</tr>
								<tr>
									<td><label id="JobCu_inv_Category4label"
										style="display: inline-block;">Engineer: </label></td>
									<td>&nbsp;<input type="text"
										id="customerInvoice_EngineersList"
										name="customerInvoice_assignedEngineer"
										style="width: 200px; display: inline-block;" value="" title=""
										placeholder="Minimum 2 characters required"></td>
									<td><img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										id="JobCu_inv_Category4image" style="display: inline-block;"></td>
									<td><input type="hidden" id="customerInvoice_engineerId"
										name="customerInvoice_assignedEngineerId" value=""
										style="width: 30px;"></td>
								</tr>
								<tr>
									<td><label id="JobCu_inv_Category5label"
										style="display: inline-block;">Project Manager: </label></td>
									<td>&nbsp;<input type="text"
										id="customerInvoice_PrjMgrList"
										name="customerInvoice_assignedPrjMgr"
										style="width: 200px; display: inline-block;" value="" title=""
										placeholder="Minimum 2 characters required"></td>
									<td><img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										id="JobCu_inv_Category5image" style="display: inline-block;"></td>
									<td><input type="hidden" id="customerInvoice_prjMgrId"
										name="customerInvoice_assignedPrjMgrId" value=""
										style="width: 30px;"></td>
								</tr>
								<tr>
									<td></td>
									<td colspan="3" align="right" onclick="showPaidCommissions()"
										id="paidCommissionID" style="display: none"><img
										alt="search" src="./../resources/Icons/customers.png">&nbsp;<font
										color="blue"><span id="commissionLabel">CommissionPaid</font></td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td style="width: 20px"></td>
					<td style="vertical-align: top">
						<fieldset
							style="width: 370px; padding-bottom: 10px; height: 190px"
							class="custom_fieldset">
							<legend class="custom_legend">
								<label><b>Misc</b></label>
							</legend>
							<table>
								<tr>
									<td><label>Division: <span  id="cudivisionlabel" style="display: none;color: red;">*</span>
											</label></td>
									<td><select id="customer_Divisions"
										name="customer_DivisionName" class="validate[required] select"
										style="width: 60%;">
											<option value="-1">- Select -</option>
											<c:forEach var="coDivisionCustomerBean"
												items="${requestScope.divisions}">
												<option value="${coDivisionCustomerBean.coDivisionId}"><c:out
														value="${coDivisionCustomerBean.description}"></c:out></option>
											</c:forEach>
									</select></td>
								</tr>
								<tr>
									<td></td>
									<td><div id="soDivisionId" style="color: red;"></div></td>
								</tr>
								<tr>
									<td><label>Customer PO#:</label></td>
									<td><input type="text" style="width: 56%;"
										id="customerInvoie_PONoID" name="customerInvoie_PONoName"></td>
								</tr>
								<tr>
									<td><label>Do Not Mail:</label></td>
									<td><input type="checkbox" id="customerInvoie_doNotMailID"
										name="customerInvoie_doNotMailName"></td>
								</tr>
								<tr>
									<td>Tax Territorys:<label style="color: red">*</label></td>
									<td><input type="text" id="customerInvoice_TaxTerritory"
										style="width: 56%;" value="${requestScope.taxTerritory}"
										placeholder="Min. 2 char. required"> <img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png">
										<input type="hidden" id="customerTaxTerritory"
										name="customerTaxPersent"
										value="${requestScope.joMasterDetails.coTaxTerritoryId}"  
										style="width: 30px;"> <label
										id="customer_TaxTextValue" style="display: none;">%</label></td>
								</tr>
								<tr>
									<td><label>Terms:</label></td>
									<td><input type="text" id="customerinvoice_paymentTerms"
										name="customerinvoice_TermsName"
										value="${requestScope.customerTabFormDataBean.customerTerms}"
										style="width: 56%;" title=""
										placeholder="Min. 2 char. required"> <img alt="search"
										src="./../resources/scripts/jquery-autocomplete/search.png"
										style="padding-right: 10px"> <input type="hidden"
										id="customerinvoicepaymentId"
										value="${requestScope.customerMasterObj.cuTermsId}"
										name="customerinvoicePaymentTermsName" style="width: 30px;"></td>
								</tr>
								<tr>
									<td><label>Due Date:</label></td>
									<td><input type="text" id="customerInvoice_dueDateID"
										name="customerInvoice_dueDateName"
										style="width: 54%; padding-left: 10px" class="datepicker">
									</td>
								</tr>
							</table>
						</fieldset>
						<BR>
					<BR>
					</td>
				</tr>
			</table>
			<%-- <fieldset class="custom_fieldset"
				style="width: 800px; margin: 0 auto">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
				<table width="100%">
					<tr align="center">
						<td><label>Subtotal:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_subTotalID"
							name="customerInvoice_subTotalname" readonly="readonly"></td>
						<td><label>Frieght:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_frightIDcu"
							name="customerInvoice_frightIDcuname" onkeyup="settotal()"></td>
						<!-- <td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxIdcu"  readonly="readonly"></td> -->
						<td><label>Tax:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_generaltaxId"
							name="customerInvoice_generaltaxName"
							value="${requestScope.taxValue}" readonly="readonly"></td>
						<td><span>%</span></td>
						<td><label></label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_taxIdcu" value=""
							name="customerInvoice_taxIdcuname" readonly="readonly"></td>
						<td><label>Total:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_totalID" name="customerInvoice_totalIDname"
							readonly="readonly"></td>
					</tr>
				</table>
			</fieldset> --%>
	<%-- <br>
			<hr/>

			<table align="center" style="width: 780px">
				<tr>
		<td><div id="showMessageCuInvoice" style="color: green;margin-left: 566%;"></div></td>
	</tr>
				<tr>
					<td align="left" width="5%"><input type="image"
						src="./../resources/Icons/PDF_new.png" title="View CuInvoice"
						onclick="viewCuInvoicePDF(); return false;"
						style="background: #EEDEBC;"></td>
					<td align="left" width="5%" style="padding-right: 0px;"><input
						id="contactEmailID" type="image"
						src="./../resources/Icons/mail_new.png"
						title="Email Customer Invoice" style="background: #EEDEBC;"
						onclick="sendPOEmail('CuInvoice');return false;"></td>
					<td></td>
		 	<td align="right" style="padding-right:1px;">
				<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="savecustomerinvoice()" style="width:80px; " >
			</td>
					<td align="center" width="80%">
					<span id="mailTimestampGeneral" style="color: green;"></span>
						<input type="button" id="createRebuildButton" class="cancelhoverbutton turbo-tan" value="Credit Rebuild" onclick="creditRebuild()" style="width:120px;position: relative;display: none;">
					</td>
					<td align="right" width="5%"><input type="button"
						id="CuInvoiceSaveID" class="cancelhoverbutton turbo-tan"
						value="Save" onclick="savecustomerinvoice('save')"
						style="width: 90px; position: relative;"></td>
					<td><input type="button" id="CuInvoiceSaveCloseID"
						class="savehoverbutton turbo-tan" value="Save & Close"
						onclick="savecustomerinvoice('close')" style="width: 110px;"></td>
					<td align="right"><input type="button" id="CuInvoiceCancel"
						class="cancelhoverbutton turbo-tan" value="Cancel"
						onclick="cancelcustomerinvoice()" style="width: 80px;"></td>
					<input type="text" id="setButtonValue" style="display: none;"
						value="" />
				</tr>
			</table> -->
			<!-- <table>
				<tbody>
					<tr style="width: 0%;">
						<td style=""><div id="showMessageCuInvoice"
								style="color: green; width: 100%;"></div></td>
					</tr>
				</tbody>
			</table> --%>
		</form>
	</div>

	<div id="soreleaselineitem" height="300px;">
		<!-- 	<div id="InfoSave" style="display: none; font-size: 17px;margin-left: 120px;height:210px;padding-top:200px; middle;color: red; font-family: Verdana,Arial,sans-serif;">Please Save the PO general tab and try reloading the Line items Grid</div> -->
		<div id="regularTab">
			<table>
				<tr>
					<td>
						<fieldset class="custom_fieldset"
							style="width: 370px; height: 89px;">
							<legend class="custom_legend">
								<label><b>Customer</b></label><span style="color: red;"
									class="mandatory"> *</span>
							</legend>
							<table>
								<tr>
									<td><input type="text" style="width: 327px"
										id="customerInvoice_linecustomerInvoiceID" readonly="readonly"
										name="customerInvoice_linecustomerInvoiceName"></td>
									<td style="display: none;"><input type="text"
										id="customerInvoice_linecustomerHiddnID"
										name="customerInvoice_linecustomerHiddnName"></td>
									<td><input type="text" id="customerInvoice_lineID"
										name="customerInvoice_lineName" style="display: none"></td>
								</tr>
								<tr>
									<td><label>Date:&nbsp;</label><input type="text"
										class="datepicker" style="width: 82px"
										id="customerInvoice_lineinvoiceDateID"
										name="customerInvoice_lineinvoiceName" onchange="setongeneraltabdate();"> <label
										style="padding-left: 10px">Number:&nbsp;</label>&nbsp;<input
										type="text" style="width: 108px;"
										id="customerInvoice_lineinvoiceNumberId"
										name="customerInvoice_lineinvoiveNumberName"> </td>
									<td></td>
								</tr>
							</table>
						</fieldset>
					</td>
					<td style="width: 30px"></td>
					<td style="vertical-align: top">
						<fieldset style="width: 370px; height: 89px"
							class="custom_fieldset">
							<legend class="custom_legend">
								<label><b>Shipping Details</b></label>
							</legend>
							<table>
								<tr>
									<td><label>Whse:</label></td>
									<td><select id="prWareHouseSelectlineID"
										name="prWareHouseSelectlineName" style="width: 110px"
										onchange="changeDropDown('prWareHouseSelectID','prWareHouseSelectlineID')">
											<option value="-1">- Select -</option>
											<c:forEach var="prWareHouseBean"
												items="${requestScope.prWareHouseDetails}">
												<option value="${prWareHouseBean.prWarehouseId}"><c:out
														value="${prWareHouseBean.searchName}"></c:out></option>
											</c:forEach>
									</select></td>
									<td><label>Via:</label></td>
									<td><select id="shipViaCustomerSelectlineID"
										name="shipViaCustomerSeleclinetName" style="width: 154px;"
										onchange="changeDropDown('shipViaCustomerSelectID','shipViaCustomerSelectlineID')">
											<option value="-1">- Select -</option>
											<c:forEach var="veShipCustomerViaBean"
												items="${requestScope.veShipViaDetails}">
												<option value="${veShipCustomerViaBean.veShipViaId}"><c:out
														value="${veShipCustomerViaBean.description}"></c:out></option>
											</c:forEach>
									</select></td>
								</tr>
								<tr>
									<td><label>Ship:</label></td>
									<td><input type="text" class="datepicker"
										style="width: 100px" id="customerInvoice_lineshipDateID"
										name="customerInvoice_lineshipDateName"
										onchange="changeDropDown('customerInvoice_shipDateID','customerInvoice_lineshipDateID')"></td>
									<td><label>Pro #:</label></td>
									<td><input type="text" style="width: 141px"
										id="customerInvoice_lineproNumberID"
										name="customerInvoice_lineproNumberName"
										value="${requestScope.vendorInvoiceNumber}"
										onchange="changeDropDown('customerInvoice_proNumberID','customerInvoice_lineproNumberID')"></td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			<table>
				<tr>
					<td><hr width="830px"></td>
				</tr>
			</table>
			<div id='customerInvoice_lineItemsGrid'>
			<table id="customerInvoice_lineitems"></table>
			<div id="customerInvoice_lineitemspager"></div>
			</div>
			<br>
			<%--
			<fieldset class="custom_fieldset" style="width: 809px">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
				<table width="100%">
					<tr align="center">
						<td><label>Subtotal:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_linesubTotalID" readonly="readonly"></td>
						<td><label>Frieght:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_linefrightID" readonly="readonly"></td>
						<td><label>Tax:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_linetaxId" name="customerInvoice_taxName"
							value="${requestScope.taxValue}" readonly="readonly"></td>
						<td>%</td>
						<td><label></label></td>
						<td><input type="text" style="width: 85px"
							id="cuGeneral_taxvalue" name="customerInvoice_taxName" value=""
							readonly="readonly"></td>
						<td><label>Total:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_linetotalID" readonly="readonly"></td>
					</tr>
				</table>
			</fieldset>
			 <br>
			<hr />

			<table align="center" style="width: 780px">
				<tr>
				<td><div id="showMessageCuInvoiceLine" style="color: green;margin-left: 566%;"></div></td>
			</tr>
				<tr>
					<td align="left"><input type="image"
						src="./../resources/Icons/PDF_new.png" title="View CuInvoice"
						onclick="viewCuInvoicePDF(); return false;"
						style="background: #EEDEBC;"></td>
					<td align="left" style="padding-right: 0px;"><input
						id="contactEmailID" type="image"
						src="./../resources/Icons/mail_new.png"
						title="Email Customer Invoice"
						style="background: #EEDEBC; margin-left: -125px;"
						onclick="sendPOEmail('poGeneral')"></td>

					<td><input type="button" id="CuInvoiceLineSaveCloseID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="savecustomerinvoice()" style="width:110px;position: relative; left: 99%;display:none;"></td>
					<td align="center" width="400px">
						<span id="mailTimestampLines" style="color: green;"></span>
					</td>
					<td align="right" width="20px"><input type="button"
						id="CuInvoiceLineSaveID" class="cancelhoverbutton turbo-tan"
						value="Save" onclick="savecustomerinvoice('save')"
						style="width: 90px;"></td>
					<td align="right" width="80px"><input type="button"
						id="CuInvoiceLineCancel" class="cancelhoverbutton turbo-tan"
						value="Save & Close" onclick="savecustomerinvoice('close')"
						style="width: 110px;"></td>

				</tr>
			</table> --%>
			
		</div>
	</div>
	<div id="invoiceTotals">
	<form id="custoemrInvoiceFormTotalID">
		<fieldset class="custom_fieldset"
				style="width: 800px; margin: 0 auto">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
				<table width="100%">
					<tr align="center">
						<td><label>Subtotal:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_subTotalID"
							name="customerInvoice_subTotalname" readonly="readonly"></td>
						<td><label>Freight:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_frightIDcu"
							name="customerInvoice_frightIDcuname" onkeyup="setTaxTotal_CI()"></td>
						<!-- <td><label>Tax:</label></td><td><input type="text" style="width:85px" id="customerInvoice_taxIdcu"  readonly="readonly"></td> -->
						<td><label>Tax:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_generaltaxId"
							name="customerInvoice_generaltaxName"
							value="${requestScope.taxValue}" readonly="readonly"></td>
						<td><span>%</span></td>
						<td><label></label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_taxIdcu" value=""
							name="customerInvoice_taxIdcuname" readonly="readonly"></td>
						<td><label>Total:</label></td>
						<td><input type="text" style="width: 85px"
							id="customerInvoice_totalID" name="customerInvoice_totalIDname"
							readonly="readonly"></td>
					</tr>
				</table>
			</fieldset>
			</form>
			<div style="width: 835px; margin: 0 auto">
				<br>
				<hr/>
			</div>
			<table>
				<tbody>
					<tr style="width: 0%;">
						<td style=""><div id="showMessageCuInvoice" 
								style="color: green; margin-left: 300%; width: 100%;"></div></td>
					</tr>
				</tbody>
			</table>
			<table align="center" style="width: 780px">
					<!-- <tr>
						<td><div id="showMessageCuInvoice" style="color: green;margin-left: 566%;"></div></td>
					</tr> -->
					<tr>
						<td align="left" width="5%">
						<div id="imgInvoicePDF"><input type="image"
							src="./../resources/Icons/PDF_new.png" title="View CuInvoice"
							onclick="viewCuInvoicePDF(); return false;"
							style="background: #EEDEBC;"> </div> </td>
						<td align="left" width="5%" style="padding-right: 0px;">
						<div id="imgInvoiceEmail"> <input
							id="contactEmailID" type="image"
							src="./../resources/Icons/mail_new.png"
							title="Email Customer Invoice" style="background: #EEDEBC;"
							onclick="sendPOEmail('CuInvoice');return false;"></div></td>
						<!-- <td></td>
					 	<td align="right" style="padding-right:1px;">
							<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="savecustomerinvoice()" style="width:80px; " >
						</td> -->
						<td align="center" width="80%">
						<span id="mailTimestampGeneral" style="color: green;"></span>
							<!-- <input type="button" id="createRebuildButton" class="cancelhoverbutton turbo-tan" value="Credit Rebuild" onclick="creditRebuild()" style="width:120px;position: relative;display: none;"> -->
						</td>
						<td align="right" width="5%"><input type="button"
							id="CuInvoiceSaveID" class="cancelhoverbutton turbo-tan"
							value="Save" onclick="savecustomerinvoice('close')"
							style="width: 90px; position: relative;"></td>
						<td><input type="button" id="CuInvoiceSaveCloseID"
							class="savehoverbutton turbo-tan" value="Close"
							onclick="savecustomerinvoice('closedialog')" style="width: 90px;"></td>
						<!--<td align="right"><input type="button" id="CuInvoiceCancel"
							class="cancelhoverbutton turbo-tan" value="Cancel"
							onclick="cancelcustomerinvoice()" style="width: 80px;"></td> -->
						<input type="text" id="setButtonValue" style="display: none;"
							value="" />
						<input type="text" id="operationID" style="display: none;"
							value="edit" />
						<input type="text" id="transactionID" style="display: none;"
							value="close" />
						<input id="cuinvoiceIDhidden" type="text" style="display: none;" />
						<input id="ciOpenStatusID" type="text" style="display: none;" />
					</tr>
				</table>
	</div>
	<div id="hidden"></div>
</div>

<div id="openPaidCommissionDialog" style="display: none">
	<table>
		<tr>
			<td align="right"><span id="invoicePaidLabel"> </span></td>
			<td align="left"><b> <span id="invoicePaidDate"></span></b></td>
		</tr>
		<tr>
			<td align="right"><span id="commissionMsg"></span> <b><span
					id="commissionPaidDate"></span></b></td>
		</tr>
		<tr></tr>
		<tr>
			<td width="60%" align="right" style="vertical-align: top;"><span
				id='commissionPaidLabel'></td>
			<td align="left"><b><span id="commissionPaid"></span></b></td>
		</tr>
		<tr>
			<td colspan="3"><hr></td>
		</tr>
	</table>

	</form>
</div>
<div id="invreasondialog" style="display: none;">
	<form name="invreasonformname">
		<table>
			<tr>
				<td>Reason:<label style="color: red;">*</label></td>
				<td><textarea id="invreasonttextid" value=""
						 cols="35" rows="5"></textarea></td>
			</tr>
			<tr>
				<td colspan="2"><label id="inverrordivreason"
					style="color: red;"></label></td>
			</tr>
			<!-- <tr id="customerInvoiceokrowidbutton"><td colspan="2" align="right"><input type="button" id="customerInvoiceokidbutton" class="savehoverbutton turbo-tan" value="Ok" onclick="customerInvoicereasonokButton()" style="width:90px;" ></td></tr> -->
		</table>
	</form>
</div>
<div id="InvoiceLineItemNote">
	<form action="" id="InvoiceLineItemNoteForm">
		<table align="right">
			<tr>
				<td><textarea cols="70" id="InvoiceLineItemNoteID"
						name="InvoiceLineItemNoteName"
						style="height: 252px; width: 570px;"></textarea> <input
					id="InvoiceLineItemNoteLabelID" style="display: none;"></td>
			</tr>
		</table>
		<table align="right">
			<tr>
				<td><input type="button" class="savehoverbutton turbo-tan"
					value="Save" onclick="SaveInvoiceLineItemNote()"
					style="width: 80px;"> <input type="button"
					class="cancelhoverbutton turbo-tan" value="Cancel"
					onclick="CancelInvoiceInLineNote()" style="width: 80px;"></td>
			</tr>
		</table>
	</form>
</div>

<script>
	$("#CiShipToRadioSet").buttonset();
	$("#CiShipToRadioSet").buttonset("enable");
	var shipToAddressSize = "${fn:length(requestScope.prWareHouseDetails)}";
	var shipToAddressArray = [];
	if (shipToAddressSize > 0) {
		<c:forEach items="${requestScope.prWareHouseDetails}" var="house">
		shipToAddressArray.push({
			description : "${house.description}",
			address1 : "${house.address1}",
			address2 : "${house.address2}",
			city : "${house.city}",
			state : "${house.state}",
			wareHouseID : "${house.prWarehouseId}",
			coTaxTerritoryId : "${house.coTaxTerritoryId}",
			zip : "${house.zip}",
			taxRate : "${house.email}"
		});
		</c:forEach>
	}

	$(function() {

	});

	function loadShipToAddressing(dataID) {
		console.log('inside customer_invoice.jsp Data:' + dataID);
		$("#cuinvoiceUs").show();
		$('#shiptolabel1').removeClass("ui-state-active");
		$('#shiptolabel2').removeClass("ui-state-active");
		$('#shiptolabel3').removeClass("ui-state-active");
		$('#shiptolabel4').removeClass("ui-state-active");

		var size = "${fn:length(requestScope.prWareHouseDetails)}";

		if (size > 0) {
			if (size == 1) {
				$('#SOforWardId').hide();
				$('#SObackWardId').hide();
			} else {
				$('#SOforWardId').show();
				$('#SObackWardId').show();
			}

			$('#SOforWardId').show();
			$('#SObackWardId').show();
		}
		//var prWareHouseIDs = $('#prToWarehouseId').val();
		//console.log($('#prToWarehouseId').val());
		if (dataID != null && dataID != '') {
			$
					.ajax({
						url : "./wareHouseDetail",
						type : "POST",
						async:false,
						data : {
							"warehouseID" : dataID
						},
						success : function(data) {
							var warehouse = data.wareHouseDetail.description;
							$("#customerShipToAddressID").val(
									data.wareHouseDetail.description);
							$("#customerShipToAddressID1").val(
									data.wareHouseDetail.address1);
							$("#customerShipToAddressID2").val(
									data.wareHouseDetail.address2);
							$("#customerShipToCity").val(
									data.wareHouseDetail.city);
							$("#customerShipToState").val(
									data.wareHouseDetail.state);
							$("#locationShipToZipID").val(
									data.wareHouseDetail.zip);
							$("#prShiptowarehouseID").val(
									data.wareHouseDetail.prWarehouseId);
							loadTaxTerritoryRateforinsideJob(data.wareHouseDetail.coTaxTerritoryId);

							// alert(data.wareHouseDetail.coTaxTerritoryId);

						}
					});
		} else {

			var aWareHouse = "${requestScope.prWareHouseDetails}";
			if (aWareHouse !== '') {
				var reminders = []; //create a new array global
				<c:forEach items="${requestScope.prWareHouseDetails}" var="reminder">
				reminders.push({
					description : "${reminder.description}",
					address1 : "${reminder.address1}",
					address2 : "${reminder.address2}",
					city : "${reminder.city}",
					state : "${reminder.state}",
					warehouseid : "${reminder.prWarehouseId}",
					coTaxTerritoryId : "${reminder.coTaxTerritoryId}",
					zip : "${reminder.zip}"
				});
				</c:forEach>
				$("#customerShipToAddressID").val(reminders[0].description);
				$("#customerShipToAddressID1").val(reminders[0].address1);
				$("#customerShipToAddressID2").val(reminders[0].address2);
				$("#customerShipToCity").val(reminders[0].city);
				$("#customerShipToState").val(reminders[0].state);
				$("#locationShipToZipID").val(reminders[0].zip);
				$("#prShiptowarehouseID").val(reminders[0].warehouseid);

				console.log("--->" + reminders[0].coTaxTerritoryId);

				loadTaxTerritoryRateforinsideJob(reminders[0].coTaxTerritoryId);
				/* setTimeout(function(){
					 loadTaxTerritoryRateforinsideJob(reminders[0].coTaxTerritoryId);
					},3000); */
			}
		}

		document.getElementById('customerShipToAddressID').disabled = true;
		document.getElementById('customerShipToAddressID1').disabled = true;
		document.getElementById('customerShipToAddressID2').disabled = true;
		document.getElementById('customerShipToCity').disabled = true;
		document.getElementById('customerShipToState').disabled = true;
		document.getElementById('customerShipToZipID').disabled = true;

		$("#CiShiptoradio1").attr("Checked", "Checked");
		$('#CiShiptolabel1').css("font-weight", "bold");
		$('#CiShiptolabel2').css("font-weight", "normal");
		$('#CiShiptolabel3').css("font-weight", "normal");
		$('#CiShiptolabel4').css("font-weight", "normal");
		$('#CiShiptolabel1').addClass("ui-state-active");
		$('#CiShiptolabel2').removeClass("ui-state-active");
		$('#CiShiptolabel3').removeClass("ui-state-active");
		$('#CiShiptolabel4').removeClass("ui-state-active");

	}

	function solineitemclick() {
		if( $('input:text[name=jobHeader_JobNumber_name]').val()!='undefined' ||  $('input:text[name=jobHeader_JobNumber_name]').val()!='')
		createtpusage('job-Release Tab','Customer Invoice-Line Tab','Info','job,Release Tab,Customer Invoice-Line,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
		jQuery("#cusinvoicetab").dialog({
			width : 900,
			height : 700
		});
		var jobNum ='${requestScope.joMasterDetails.jobNumber}';
		if(jobNum!==null && jobNum!=""){
			loadCustomerInvoice();
			}
	}

	function sogeneralclick() {
		if( $('input:text[name=jobHeader_JobNumber_name]').val()!='undefined' ||  $('input:text[name=jobHeader_JobNumber_name]').val()!='')
		createtpusage('job-Release Tab','Customer Invoice-General Tab','Info','job,Release Tab,Customer Invoice,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
		jQuery("#cusinvoicetab").dialog({
			width : 900,
			height : 850
		});
	}

	var shipAddressInc = 0;
	var zFlag = '0';

	function cuinvoiceshipForWards() {

		if (parseInt(shipAddressInc) <= 0) {
			shipAddressInc = 1;
		} else {
			if (zFlag == '1') {
				shipAddressInc = parseInt(shipAddressInc) + 2;
			}
		}
		console.log('Forward Log: ' + shipAddressInc);
		if (shipToAddressArray.length > 0) {
			console.log('incValue For: ' + shipAddressInc);
			var imgUrlBackwards = "./../resources/images/Arrowleft.png";
			$('#backWardId').css('background',
					'url(' + imgUrlBackwards + ') no-repeat');
			$('#backWardId').css('background-position', 'center');
			if (parseInt(shipAddressInc) >= shipToAddressArray.length - 1) {
				console.log('Max Value - Z: ' + shipAddressInc);
				var imgUrlDisForwards = "./../resources/images/DisabledArrowright.png";
				$('#forWardId').css('background',
						'url(' + imgUrlDisForwards + ') no-repeat');
				$('#forWardId').css('background-position', 'center');
			}

			$('#customerShipToAddressID').val(
					shipToAddressArray[shipAddressInc].description);
			$('#customerShipToAddressID1').val(
					shipToAddressArray[shipAddressInc].address1);
			$('#customerShipToAddressID2').val(
					shipToAddressArray[shipAddressInc].address2);
			$('#customerShipToCity').val(
					shipToAddressArray[shipAddressInc].city);
			$('#customerShipToState').val(
					shipToAddressArray[shipAddressInc].state);
			$('#locationShipToZipID').val(
					shipToAddressArray[shipAddressInc].zip);

			/* $('#taxGeneralId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
			$('#taxLineId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
			$('#prWarehouseID').val(shipToAddressArray[shipAddressInc].wareHouseID); */

			$('#prShiptowarehouseID').val(
					shipToAddressArray[shipAddressInc].wareHouseID);

			loadTaxTerritoryRateforinsideJob(shipToAddressArray[shipAddressInc].coTaxTerritoryId);

			shipAddressInc = parseInt(shipAddressInc) + 1;
		}
		zFlag = '0';
		console.log('Next Forward : ' + shipAddressInc);
	}

	function cuinvoiceshipBackWards() {

		if (parseInt(shipAddressInc) >= (shipToAddressArray.length) - 1) {
			shipAddressInc = (shipToAddressArray.length) - 2;

		} else {
			if (zFlag == '0') {
				shipAddressInc = parseInt(shipAddressInc) - 2;
			}
		}
		console.log('Backward Log: ' + shipAddressInc);
		if (shipToAddressArray.length > 0) {
			var imgUrlforwards = "./../resources/images/Arrowright.png";
			$('#forWardId').css('background',
					'url(' + imgUrlforwards + ') no-repeat');
			$('#forWardId').css('background-position', 'center');

			$('#customerShipToAddressID').val(
					shipToAddressArray[shipAddressInc].description);
			$('#customerShipToAddressID1').val(
					shipToAddressArray[shipAddressInc].address1);
			$('#customerShipToAddressID2').val(
					shipToAddressArray[shipAddressInc].address2);
			$('#customerShipToCity').val(
					shipToAddressArray[shipAddressInc].city);
			$('#customerShipToState').val(
					shipToAddressArray[shipAddressInc].state);
			$('#locationShipToZipID').val(
					shipToAddressArray[shipAddressInc].zip);

			$('#prShiptowarehouseID').val(
					shipToAddressArray[shipAddressInc].wareHouseID);
			loadTaxTerritoryRateforinsideJob(shipToAddressArray[shipAddressInc].coTaxTerritoryId);

			shipAddressInc = parseInt(shipAddressInc) - 1;

			if (parseInt(shipAddressInc) < 0) {
				console.log('Over Backward');
				var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
				$('#backWardId').css('background',
						'url(' + imgUrlDisBackwards + ') no-repeat');
				$('#backWardId').css('background-position', 'center');
			}
			zFlag = '1';
			console.log('Next Backward : ' + shipAddressInc);
		}

	}
	$(function() { var cache = {}; var lastXhr=''; $( "#customerShipToCity" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var stateSelect =	ui.item.label; var stateSplit = stateSelect.split(" ("); 
		var stateName = stateSplit[1]; var stateCode = stateName.replace(")", ""); $("#customerShipToState").val(stateCode);},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "job_controller/cityAndState", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} }); });
	function setTaxTotal_CI(){
		var taxRate=$('#customerInvoice_generaltaxId').val();
		var subtot = $("#customerInvoice_subTotalID").val().replace(/[^0-9\.-]+/g,"");
		var frieght = $("#customerInvoice_frightIDcu").val().replace(/[^0-9\.-]+/g,"");
		var sum = 0;
		var taxAmt = 0;
		var cuinvoiceIDhidden=$("#cuinvoiceIDhidden").val();
		var rows = jQuery("#customerInvoice_lineitems").getDataIDs();
			var taxsubtotal = 0;
		if(cuinvoiceIDhidden=="" && $("#CI_taxsubtotal").val()!=null && $("#CI_taxsubtotal").val()!=""){
			taxsubtotal=$("#CI_taxsubtotal").val();
		}else{
			 for(a=0;a<rows.length;a++)
			 {
			    row=jQuery("#customerInvoice_lineitems").getRowData(rows[a]);
			    var total=row['amount'].replace(/[^0-9\-.]+/g,"");
			    var taxable=row['taxable'];
			    var id="#canDoID_"+rows[a];
			    var canDo=$(id).is(':checked');
			      if(!isNaN(total)&& !canDo && taxable==1){
			    	  taxsubtotal=Number(taxsubtotal)+Number(floorFigureoverall(total,2));
			    	}
			      }
		}
		var allowfreightinTax=false;
		 var allowreqcheckfreightintax=$('#CI_taxfreight').val();
			if(allowreqcheckfreightintax!=null && allowreqcheckfreightintax==1){
				allowfreightinTax=true;
			}
		 if(allowfreightinTax){
			 taxAmt = Number(Number(floorFigureoverall(taxsubtotal, 2))+Number(floorFigureoverall(frieght,2)))*Number(taxRate)/100;
		 }else{
			 taxAmt = Number(floorFigureoverall(taxsubtotal, 2))*Number(taxRate)/100;
		 }
		 sum = Number(subtot) + taxAmt + Number(frieght);
		 $("#customerInvoice_taxIdcu").val(Number(floorFigureoverall(taxAmt, 2)));
		 $("#customerInvoice_totalID").val(Number(floorFigureoverall(sum, 2)));
		
		}

	function CIGeneralTabSeriallize(){
		var value1=$("#customerInvoice_invoiceNumberId		").val();
		var value2=$("#customerbillToAddressIDcuInvoice   "  ).val();
		var value3=$("#customerbillToAddressID1cuInvoice  "  ).val();
		var value4=$("#customerbillToAddress2             "  ).val();
		var value5=$("#customerbillToCitycuInvoice        "  ).val();
		var value6=$("#customerbillToZipIDcuInvoice       "  ).val();
		var value7=$("#emailListCU                        "  ).val();
		var value8=$("#customerInvoice_salesRepsList      "  ).val();
		var value9=$("#customerInvoice_CSRList            "  ).val();
		var value10=$("#customerInvoice_SalesMgrList       "  ).val();
		var value11=$("#customerInvoice_EngineersList      "  ).val();
		var value12=$("#customerInvoice_PrjMgrList         "  ).val();
		var value13=$("#customer_Divisions                 "  ).val();
		var value14=$("#customerInvoie_PONoID              "  ).val();
		var value15=$("#customerInvoice_TaxTerritory       "  ).val();
		var value16=$("#customerinvoice_paymentTerms       "  ).val();
		var value17=$("#customerInvoice_dueDateID          "  ).val();
		var value18=$("#prWareHouseSelectID                "  ).val();
		var value19=$("#shipViaCustomerSelectID            "  ).val();
		var value20=$("#customerInvoice_shipDateID         "  ).val();
		var value21=$("#customerInvoice_proNumberID        "  ).val();
		var value22=$("#CI_Shipto").contents().find("#shipToName").val();	
		var value23=$("#CI_Shipto").contents().find("#shipToAddress1").val();
		var value24=$("#CI_Shipto").contents().find("#shipToAddress2").val();
		var value25=$("#CI_Shipto").contents().find("#shipToCity").val();
		var value26=$("#CI_Shipto").contents().find("#shipToState").val();
		var value27=$("#CI_Shipto").contents().find("#shipToZip").val();
		var value28=$("#CI_Shipto").contents().find("#shiptomoderhiddenid").val();
		var value29=$("#customerInvoie_doNotMailID").is(':checked');
		var value30=$("#CI_Shipto").contents().find("#shiptoindexhiddenid").val();
		var value31=$("#CI_Shipto").contents().find("#shiptocusindexhiddenid").val();
		var value32=$("#customerInvoice_invoiceDateID").val();
		var value=value1+value2+value3+value4+value5+value6+value7+value8+value9+value10
				  +value11+value12+value13+value14+value15+value16+value17+value18+value19+value20
				  +value21+value22+value23+value24+value25+value26+value27+value28+value29+value30+value31+value32;
		return value;
	}
	
	function setongeneraltabdate(){
		var lineItemDate= $("#customerInvoice_lineinvoiceDateID").val();
		$("#customerInvoice_invoiceDateID").val(lineItemDate);
	}
</script>

<div><jsp:include page="./job/creditRebuild.jsp"></jsp:include></div>