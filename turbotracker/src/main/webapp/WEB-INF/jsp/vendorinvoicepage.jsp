<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<style>
.ui-jqgrid tr.jqgrow td {
		text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
.ui-jqgrid tr.jqgrow td { text-overflow: ellipsis !important; }
</style>
	  <div id="openvendorinvoice"> 
	  
	  <div style="font-weight:bold;float:right;font-size: 13px;display:none;" id="jobpaidStatus">
		<a href="javascript:jobshowPaymentDetailsDialog();"><label style="cursor:pointer">Check #<span id="jobchk_nofmPO" style="margin-left: 5px;margin-right:20px"></span>  Paid Date:<span id="jobdate_paidfmPO" style="margin-left: 5px;"></span></label></a></div>
	  
	  <form id="openvendorinvoiceFormID">
		<table class="font">
			<tr><td style="vertical-align: top">
			<fieldset class= "custom_fieldset " style="width:250px;padding-bottom: 10px;">
				<legend class="custom_legend"><label><b>Payable To</b></label></legend>
				<table style="padding-left:15px">
					<tr><td><input type="text" id="manufacurterNameID" name="manufacurterName" style="width: 209px;"><!--  disabled="disabled" -->
					<input type="hidden" id="rxMasterID" name="rxMasterIDName" style="width: 180px;">
					<tr><td><textarea rows="5" cols="25" id="addressNameID" name="addressName" disabled="disabled"></textarea>
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
					<legend class="custom_legend"><label><b>Invoice/Bill </b></label></legend>
					<table>
						<tr style="height: 30px">
							<td><label>Received:</label></td>
							<td><input type="text" style="width:82px" class="vendorInvDatepicker" id="vendorDateID" name="vendorDateName"></td>
							<td><label>Vendor Inv.#:<span id="veInvnomandatoryfromjob" data-manvalue = "0" style="display:none;color:red">*</span></label></td><td><input type="text" style="width:123px" id="vendorInvoiceNum" name="vendorInvoiceNumName" maxlength="20"></td>
						</tr>
						<tr style="height: 30px">
							<td><label>Dated:</label></td>
							<td><input type="text" style="width:82px" class="vendorInvDatepicker" id="datedID"  name="datedName"></td>
							<td><label>A/p Acct:</label><%-- <c:out value="${requestScope.SysAccountLinkage.coAccountIdap}"> </c:out>--%></td>
							<td>
								<select style="width:135px" id="coAccountID" name="coAccountName">
									<option value="-1"> - Select - </option>
									<c:forEach var="AcctSelectBean" items="${requestScope.coAccountDetails}">
										<option value="${AcctSelectBean.coAccountId}"         ><c:out value="${AcctSelectBean.number}" ></c:out> | <c:out value="${AcctSelectBean.description}" ></c:out></option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr style="height: 30px">
							<td><label>Due:</label></td>
							<td><input type="text" style="width:82px" class="vendorInvDatepicker" id="dueDateID" name="dueDateName"><input type="hidden" id ="duedaysfmpojob"/></td>
							<!-- <td><label style="vertical-align: middle;">Post Date:</label><input type="checkbox" id="postDateChkID" name="postDateChkName" style="vertical-align: middle;" onclick="postDateChk()"></td>
							<td><input type="text" style="width:75px"  class="vendorInvDatepicker" id="postDateID" name="postDateName"></td> -->
							<td><label>Ship:</label></td><td><input type="text" style="width:82px" class="vendorInvDatepicker" id="shipDateID" name="shipDateName"></td>
							</tr>
						<tr style="height: 30px">
							<td><label>PO #:</label></td>
							<td><input type="text" style="width:120px" id="poNumberID" readonly="readonly"></td>
							<td><label>Pro #:</label></td><td><input type="text" style="width:123px" id="proNumberID" name="proNumberName"></td></tr>
						</tr>
					<!-- 	<tr style="height: 30px">
							<td  style="width:105px;"><label>QuickBooks #:</label></td>
							<td colspan="2"><input type="text" style="width: 125px" id="poNumberID" readonly="readonly"></td>
						</tr> -->
						<tr style="height: 30px">
							<td><label>Ship Via:</label></td>
							<!-- <td>
								<select id="shipViaSelectID" name="shipViaSelectName">
										<option value="0"> -Select- </option>
								</select>
							</td> -->
							<td>
								<select id="shipViaSelectID" name="shipViaSelectName">
								<option value="-1">--Select--</option>
									<c:forEach var="veShipViaBean" items="${requestScope.veShipViaDetails}">
										<option value="${veShipViaBean.veShipViaId}"><c:out value="${veShipViaBean.description}" ></c:out></option>
								   </c:forEach>
								   
								   <%-- 	<c:forEach var="TsUserLogin" items="${orderedByNames}">
												   <c:set value="${TsUserLogin.fullName}" var="name" />
												
													<c:if test="${sessionScope.user.fullName eq name}">
													<c:if test="${theVepo.orderedById eq TsUserLogin.userLoginId}">
														<option selected="selected" value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
													<c:if test="${sessionScope.user.fullName ne name}">
													<c:if test="${theVepo.orderedById ne TsUserLogin.userLoginId}">
														<option value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
									</c:forEach>
								    --%>
								   
								</select>
							</td>
							<td  style="width:105px;display: none;" ><label>QuickBooks #:</label></td>
							<td colspan="2" style="display: none;"><input type="text" style="width: 125px" id="quickbookId" readonly="readonly"></td>
						
							<tr>
							<td  class="invReasonfmjob"><label class="fontclass">Reason:</label></td><td colspan="2"  class="invReasonfmjob"><input type="text" id="invreasonfmjob" readonly="readonly" style="width: 62%;"></td>
							<td></td>
							</tr>
							<tr>
							<td colspan ="2">
							<span style ="color:red;display:none" id="mandveinvno" >Vendor Invoice# is Mandatory</span>  
							</td>
							</tr>
					</table>
				</fieldset>	
			</td></tr> 
		</table>
			<br>
			<table style="padding-left:5px">
				<tr><td><table id="vendorinvoice1" ></table><div id="vendorinvoicepager" style="display: none;"></div></td></tr>
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
			<fieldset class= "custom_fieldset " style="width:825px">
				<legend class="custom_legend"><label class="font"><b>Totals</b></label></legend>
				<table style="padding-left:20px; width:750px" class="font">
					<tr>
						<td><label>Subtotal:</label><td><input type="text" style="width:75px" id="subtotal_ID" name="" readonly="readonly"></td>
						<td><label>Freight :</label></td><td><input type="text" style="width:75px" id="freight_ID" name="freight_ID" onkeyup="setvendorinvoicetotal()"></td>
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
	 				<td align="right"></td>
	 				<td align="right">
	 				<input type="button" id="vendorinvoiceidbutton" class="savehoverbutton turbo-tan" value="Save" onclick="callvendorinvoicesave()" style="width:90px;" >
	 				<input type="button" id="vendorinvoiceidclosebutton" class="savehoverbutton turbo-tan" value="Close" onclick="cancelvendorInvoice()" style="width:90px;display:none; " ></td>
	 				<!-- <td align="right" width="80px"><input type="button" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="cancelvendorInvoice()" style="width:80px;" ></td> -->
	 			</tr>
			</table>
			<br>
		</form>
	</div>
	
	<div id="jobinvreasondialog" style="display: none;">
											<form name="jobinvreasonformname">
											<table>
											<tr><td>Reason:<label style="color: red;">*</label></td><td><textarea id="jobinvreasonttextid" value="" name="jobinvreasonttextid" cols="35" rows="5"></textarea></td></tr>
											<tr><td colspan="2"><label id="jobinverrordivreason" style="color: red;"></label></td></tr>
											<!-- <tr><td colspan="2" align="right"><input type="button" id="vendorinvoiceokidbutton" class="savehoverbutton turbo-tan" value="Ok" onclick="vendorinvoiceOk()" style="width:90px;" ></td></tr> -->
											
											</table>
											</form>
											</div>
	
	<div id="jobshowInvoiceInfoDialog" style="display: none;">
											<form name="jobshowInvoiceInfoDialogName" style="border:0; border-bottom:1px solid;">
											<table width="100%" id="invoiceDetailspopup">
											</table>
											</form>
											</div>
	
<script type="text/javascript">
function changeShipVia(shipViaID){
	console.log('ShipVia ID in JSP:'+shipViaID);
	$("#shipViaSelectID").val(shipViaID).change();
	$('select[name^="shipViaSelectName"] option[value="'+shipViaID+'"]').attr("selected","selected");
}

$(function() {
	var cache = {};
	var lastXhr='';
	$("#manufacurterNameID").autocomplete({ 
		minLength:3,
		timeout :1000,
		change: function (event, ui) { },
	 	open: function(){ 
			var len = $('.ui-autocomplete > li').length;
			var value=$('.ui-autocomplete > li').text();
		 	if(len==1 && value==" "){
				$("#manufacurterNameID").val("");
			}
		},  
	 	response: function( event, ui ) {
         
     	},
		select: function (event, ui) {
			var ID = ui.item.value;
			var product = ui.item.label;
			console.log(ID+" || "+product);
			$("#manufacurterNameID").val("");
			$("#manufacurterNameID").val(product);
			$("#rxMasterID").val(ID);
			/* $.ajax({
				url: './jobtabs5/rxAddressInfo?rxMasterID='+ID,
		        type: 'GET',       
		        success: function (data) {
		        	$("#manufacurterNameID").val(product);
		        	$("#rxMasterID").val(ID);
		        	$("#addressNameID").text(data.address1+"\n"+data.address2+"\n"+data.city+"\n"+data.state+data.zip);
		        }
		    }); */
			 $.ajax({
			        url: './getManufactureATTN?rxMasterId='+ID,
			        type: 'POST',       
			        success: function (data) {
			        	$("#manufacurterNameID").val(product);
			        	$("#rxMasterID").val(ID);
			        	$.each(data, function(key, valueMap) {									
							if("vendorAddress"==key)
							{
								$.each(valueMap, function(index, value){
									$("#addressNameID").text(value.address1+"\n"+value.address2+"\n"+value.city+"\n"+value.state+value.zip);
								}); 
								
							}
							if("dueonDays" == key)
								{
								$("#duedaysfmpojob").val(valueMap);
								var d = new Date($("#datedID").val());
								d.setDate(Number(d.getDate())+Number(valueMap));
								var day = ("0" + d.getDate()).slice(-2);
								var month = ("0" + (d.getMonth() + 1)).slice(-2);
								var today = (month)+"/"+(day)+"/"+d.getFullYear();
								$("#dueDateID").val(today);
								}
							
						});
			        }
			    });
		    
		},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "./search/searchVendor", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} $("#manufacurterNameID").val("");
				});
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}
	});
});

</script>