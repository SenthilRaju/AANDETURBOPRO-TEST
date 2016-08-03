<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<table>
	<tr><td>
		<fieldset class= "custom_fieldset" style="width:330px">
			<legend class="custom_legend"><label><b>Customer</b></label><span style="color:red;" class="mandatory"> *</span></legend>
				<table>
					<tr><td><input type="text" style="width:250px" id="customerInvoice_customerInvoiceID" name="customerInvoice_customerInvoiceName"></td><td style="display: none;"><input type="text" id="customerInvoice_customerHiddnID" name="customerInvoice_customerHiddnName"></td>
					<td><input type="text" id="customerInvoice_ID" name="customerInvoice_Name"></td></tr>
					<tr><td><label>Date:&nbsp;</label><input type="text" class="datepicker" style="width:100px" id="customerInvoice_invoiceDateID" name = "customerInvoice_invoiceName"></td>
						<td></td>
						<td><label>Number:&nbsp;</label>&nbsp;<input type="text" style="width:35%" id="customerInvoice_invoiceNumberId"  name="customerInvoice_invoiveNumberName"></td></tr>
				</table>
		</fieldset>
	</td>
	<td style="width:30px"></td>
	<td style="vertical-align:top">
		<fieldset style="width:400px" class= "custom_fieldset">
		<legend class="custom_legend"><label><b>Shipping Details</b></label></legend>
			<table>
				<tr>
					<td><label>Whse:</label><select id="prWareHouseSelectID" name="prWareHouseSelectName"><option value="-1"> - Select - </option><c:forEach var="prWareHouseBean" items="${requestScope.prWareHouseDetails}">
																										<option value="${prWareHouseBean.prWarehouseId}">
																												<c:out value="${prWareHouseBean.searchName}" ></c:out>
																										</option></c:forEach></select></td>
					<td><label>Via:</label><select id="shipViaCustomerSelectID" name="shipViaCustomerSelectName"><option value="-1"> - Select - </option><c:forEach var="veShipCustomerViaBean" items="${requestScope.veShipViaDetails}">
																										<option value="${veShipCustomerViaBean.veShipViaId}">
																												<c:out value="${veShipCustomerViaBean.description}" ></c:out>
																										</option></c:forEach></select></td>
				</tr>
				<tr>
					<td><label>Ship:</label><input type="text" class="datepicker" style="width:100px" id="customerInvoice_shipDateID" name="customerInvoice_shipDateName"></td>
					<td><label>Pro #:</label><input type="text" style="width:75px" id="customerInvoice_proNumberID" name="customerInvoice_proNumberName"></td>
				</tr>
		  </table>
	</fieldset>	
		</td></tr>
	</table>
<script type="text/javascript">
jQuery (function(){
	$('#datepicker').datepicker();
});
</script>