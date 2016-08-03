<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	  <div id="openvendorinvoice"> <form id="openvendorinvoiceFormID">
		<table class="font">
			<tr><td style="vertical-align: top">
			<fieldset class= "custom_fieldset " style="width:250px;padding-bottom: 10px;">
				<legend class="custom_legend"><label><b>Payable To</b></label></legend>
				<table style="padding-left:15px">
					<tr><td><input type="text" id="manufacurterNameID" name="manufacurterName" style="width: 180px;" disabled="disabled">
					<tr><td><textarea rows="2" cols="25" id="addressNameID" name="addressName" disabled="disabled"></textarea>
				</table>
			</fieldset>
			<fieldset class= "custom_fieldset " style="width:250px;padding-bottom: 10px;">
			<legend class="custom_legend"><label><b>Job #${requestScope.joMasterDetails.jobNumber}</b></label></legend>
				<table style="padding-left:15px">
					<tr><td><textarea rows="2" cols="25" id="jobaddressNameID" name="jobaddressName" disabled="disabled">${requestScope.joMasterDetails.description}</textarea>
				</table>
			</fieldset>
			</td>
			<td style="width:15px"></td>
			<td style="vertical-align:top;">
				<fieldset class= " custom_fieldset" style="">
					<legend class="custom_legend"><label><b>Invoice/Bill</b></label></legend>
					<table>
						<tr style="height: 30px">
							<td><label>Received:</label></td>
							<td><input type="text" style="width:75px" class="vendorInvDatepicker" id="vendorDateID" name="vendorDateName"></td>
							<td><label>Vendor Inv.#:</label></td><td><input type="text" style="width:75px" id="vendorInvoiceNum" name="vendorInvoiceNumName"></td>
						</tr>
						<tr style="height: 30px">
							<td><label>Dated:</label></td>
							<td><input type="text" style="width:75px" class="vendorInvDatepicker" id="datedID" disabled="disabled" name="datedName"></td>
							<td><label>A/p Acct:</label></td>
							<td>
								<select style="width:135px" id="coAccountID" name="coAccountName">
									<option value="-1"> - Select - </option>
									<c:forEach var="AcctSelectBean" items="${requestScope.coAccountDetails}">
										<option value="${AcctSelectBean.coAccountId}"><c:out value="${AcctSelectBean.description}"></c:out></option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr style="height: 30px">
							<td><label>Due:</label></td>
							<td><input type="text" style="width:75px" class="vendorInvDatepicker" id="dueDateID" name="dueDateName"></td>
							<td><label style="vertical-align: middle;">Post Date:</label><input type="checkbox" id="postDateChkID" name="postDateChkName" style="vertical-align: middle;" onclick="postDateChk()"></td>
							<td><input type="text" style="width:75px"  class="vendorInvDatepicker" id="postDateID" name="postDateName"></td>
						</tr>
						<tr style="height: 30px">
							<td><label>PO #:</label></td>
							<td><input type="text" style="width:75px" id="poNumberID" readonly="readonly"></td>
							<td><label>Ship:</label></td><td><input type="text" style="width:75px" class="vendorInvDatepicker" id="shipDateID" name="shipDateName"></td>
						</tr>
						<tr style="height: 30px">
							<td  style="width:105px;"><label>QuickBooks #:</label></td>
							<td colspan="2"><input type="text" style="width: 125px" id="poNumberID" readonly="readonly"></td>
						</tr>
						<tr style="height: 30px">
							<td><label>Ship Via:</label></td>
							<td>
								<select id="shipViaSelectID" name="shipViaSelectName">
									<c:forEach var="veShipViaBean" items="${requestScope.veShipViaDetails}">
										<option value="${veShipViaBean.veShipViaId}"><c:out value="${veShipViaBean.description}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
							<td><label>Pro #:</label></td><td><input type="text" style="width:75px" id="proNumberID" name="proNumberName"></td></tr>
					</table>
				</fieldset>	
			</td></tr> 
		</table>
			<br>
			<table style="padding-left:5px">
				<tr><td><table id="vendorinvoice1" ></table><div id="vendorinvoicepager"></div></td></tr>
			</table> 
			<br>
	  		<fieldset class= "custom_fieldset" style="padding-bottom: 0px;width: 30px; display: none;">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/view_new.png" title="View Invoice Details" onclick="viewInvoiceDetails()"  style="background: #EEDEBC"></td> 	
				  		<td align="right" style="padding-right: 7px;display: none;"><input id="contactEmailID" type="image" src="./../resources/images/lineItem_new.png" title="InLine Item" style="background: #EEDEBC"onclick=""></td>
		    		</tr>
		    	</table>
	   		</fieldset>
			<fieldset class= "custom_fieldset " style="width:780px">
				<legend class="custom_legend"><label class="font"><b>Totals</b></label></legend>
				<table style="padding-left:20px; width:750px" class="font">
					<tr>
						<td><label>Subtotal:</label><td><input type="text" style="width:75px" id="subtotal_ID" name="" readonly="readonly"></td>
						<td><label>Frieght:</label></td><td><input type="text" style="width:75px" id="freight_ID" name="" readonly="readonly"></td>
				    	<td><label>Tax:</label></td><td><input type="text" style="width:75px" id="tax_ID" name="" readonly="readonly"></td>
				    	<td><label>Total:</label></td><td><input type="text" style="width:75px" id="total_ID" name="" readonly="readonly"></td>
				    	<td><label>Bal Due:</label></td><td><input type="text" style="width:75px" id="bal_ID" name="" readonly="readonly"></td>
				    	<td style="display: none;"><label>VeBillID:</label><input type="text" style="width:75px" id="veBill_ID" name="" readonly="readonly"></td>
				    </tr>
				</table>
			</fieldset>
			<br>
			<table align="center" style="width:800px">
	 			<tr>
	 				<td align="right"><input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="savevendorinvoice()" style="width:80px;" ></td>
	 				<!-- <td align="right" width="80px"><input type="button" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="cancelvendorInvoice()" style="width:80px;" ></td> -->
	 			</tr>
			</table>
			<br>
		</form>
	</div>