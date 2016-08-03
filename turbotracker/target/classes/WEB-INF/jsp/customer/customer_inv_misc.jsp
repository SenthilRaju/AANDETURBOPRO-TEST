<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<table>
		<tr>
			<td style="vertical-align:top">
				<fieldset class= "custom_fieldset">
					<legend class="custom_legend"><label><b>Employee's Assigned</b></label></legend>
					<table>
							<tr>
								<td><label>Salesrep: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_salesRepsList" name="customerInvoice_assignedSalesRep" style="width: 200px;" value="${requestScope.customerTabFormDataBean.assignedSalesRep}" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_salesRepId" name="customerInvoice_assignedSalesRepId" value="${requestScope.customerMasterObj.cuAssignmentId0}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>CSR: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_CSRList" name="customerInvoice_assignedCSR" style="width: 200px;" value="${requestScope.customerTabFormDataBean.assignedCSRs}" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_CSRId" value="${requestScope.customerMasterObj.cuAssignmentId1}" name="customerInvoice_assignedCSRId" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Sales Manager: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_SalesMgrList" name="customerInvoice_assignedSalesMgr" style="width: 200px;" value="${requestScope.customerTabFormDataBean.assignedSalesMGRs}" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_salesMgrId" name="customerInvoice_assignedSalesMgrId" value="${requestScope.customerMasterObj.cuAssignmentId2}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Engineer: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_EngineersList" name="customerInvoice_assignedEngineer" style="width: 200px;" value="${requestScope.customerTabFormDataBean.assignedEngineers}" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_engineerId" name="customerInvoice_assignedEngineerId" value="${requestScope.customerMasterObj.cuAssignmentId3}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Project Manager: </label></td>
								<td>&nbsp;<input type="text" id="customerInvoice_PrjMgrList" name="customerInvoice_assignedPrjMgr" style="width: 200px;" value="${requestScope.customerTabFormDataBean.assignedProjManagers}" title="" placeholder="Minimum 2 characters required"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="customerInvoice_prjMgrId" name="customerInvoice_assignedPrjMgrId" value="${requestScope.customerMasterObj.cuAssignmentId4}" style="width: 30px;"></td>
							</tr>
						</table>
				</fieldset>
			</td>
			<td style="width:10px"></td>
			<td style="vertical-align:top">
				<fieldset style="width:400px;padding-bottom:10px;" class= "custom_fieldset">
				<legend class="custom_legend"><label><b>Misc</b></label></legend>
				<table>
					<tr><td style="width: 30px;"><label style="display: none;">Sales Order #:</label></td><td style="display: none;"><select style="width:75px"><option></option></select></td>
						<td><label>Division:</label><select  id="customer_Divisions" name="customer_DivisionName" class="validate[required] select">
																												<option value="-1"> - Select - </option>
																												<c:forEach var="coDivisionCustomerBean" items="${requestScope.divisions}">
																													<option value="${coDivisionCustomerBean.coDivisionId}">
																														<c:out value="${coDivisionCustomerBean.description}" ></c:out>
																													</option>
																												</c:forEach>
																											</select></td></tr>
					<tr><td><label>Customer PO #:</label><input type="text" style="width: 15px;" id="customerInvoie_PONoID" name="customerInvoie_PONoName"></td>
						<td><label>Do Not Mail:</label><input type="checkbox" id="customerInvoie_doNotMailID" name="customerInvoie_doNotMailName"></td></tr>
					<tr><td><label>Tax Territory:</label></td>
								<td><input  type="text" id="customerInvoice_TaxTerritory" style="width: 120px;" value="${requestScope.taxTerritory}"  placeholder="Min. 2 char. required">
									<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
								  		  <input type="hidden" id="customerTaxTerritory" name="customerTaxPersent"  value="${requestScope.joMasterDetails.coTaxTerritoryId}" style="width: 30px;">
											<label id = "customer_TaxTextValue" style="display: none;">%</label></td>
						<td><label>Due Date:</label><input type="text" id="customerInvoice_dueDateID" name="customerInvoice_dueDateName" style="width:75px" class="datepicker"></td></tr>
					<tr><td><label>Terms:</label></td><td>
							<input type="text" id="customerinvoice_paymentTerms" name="customerinvoice_TermsName" value="${requestScope.customerTabFormDataBean.customerTerms}" style="width: 120px;" title="" placeholder="Min. 2 char. required">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
							<input type="hidden" id="customerinvoicepaymentId" value="${requestScope.customerMasterObj.cuTermsId}" name="customerinvoicePaymentTermsName" style="width: 30px;"></td>
						</tr>
		       </table>
		      </fieldset>
			</td>
		</tr>
	</table>
