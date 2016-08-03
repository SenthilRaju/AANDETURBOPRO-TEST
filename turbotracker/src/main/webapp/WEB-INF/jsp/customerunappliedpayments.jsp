<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - CustomerPayments</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			
	
		/* .loading {
		  display: inline-block;
		  margin: 20em;
		  border-width: 50px;
		  border-radius: 50%;
		  -webkit-animation: spin 1s linear infinite;
		     -moz-animation: spin 1s linear infinite;
		       -o-animation: spin 1s linear infinite;
		          animation: spin 1s linear infinite;
		  } */
	
		.style-1 {
		  border-style: solid;
		  border-color: #444 transparent;
		  }
		
		.style-2 {
		  border-style: double;
		  border-color: #637C92 transparent;
		  }
		
		.style-3 {
		  border-style: double;
		  border-color: #444 #fff #fff;
		  }
			
     </style>
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:930px;margin:0 auto; background-color: #FAFAFA;height: 750px;">
			<ul>
				<li id="customerFormDetails"><a href="">Unapplied Payments</a></li>
			</ul>
		<br><br>
	<div class="bodyDiv" id="customerFormDetails">
			<form id="customerFormId"  method="post" >
			<table style="width: 900px;" align="center">
				<tr>
					<td>
					<table style="padding-bottom: 10px;">
							<tr>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 400px;height: 150px">
										<legend><label><b>Original Payment</b></label></legend>
											<table>
												<tr>
													<td align="right"><label>Customer: </label></td>
													<td><input type="text" value="${requestScope.rxMAsterDetails.name}" id="payCustomer" readonly="readonly" style="width: 300px;"/>
														<input type="text" value="${requestScope.rxCustomerId}" id="customerID" readonly="readonly"  style="display:none;">
														<input type="text" value="${requestScope.recieptID}" id="recieptID" readonly="readonly"  style="display:none;">
													</td>
												</tr>
												<tr>
													<td align="left" colspan = "2"><label>Original Amount: </label>
													<input id="payAmountId" value='<fmt:formatNumber value="${requestScope.amount}" type="currency" currencyCode="USD"/>' name="payAmountName" type="text" readonly="readonly"  style="width: 100px"><label style="padding-left: 10px;padding-right: 10px">Date</label><input id="paymentdateid" align="right" type="text" class="datepicker" value="${requestScope.Date}" disabled style="width: 89px"></td></tr>
												<tr><td align="left"><label>Type: </label></td><td><select id="paymentReciptTypeId" disabled name="paymentReciptTypeName" style="width: 100px;" >
														<option value="0">--Select--</option>
														<option value="1">Cash</option>
														<option value="2">Check</option>
														<option value="3">CreditCard</option>
														<option value="4">Other</option>
														</select>
														<input type ="hidden" id="paidStatus" />
														<input type="text" value="${requestScope.recieptTypeId}" id="PaytypeId" style="display:none;"></td></tr>
												<tr><td align="left"><label>Check #: </label></td><td><input type="text" id="payCheckId" value="${requestScope.checkNo}" name="payCheckName" readonly="readonly"  style="width: 100px"></td></tr>
												<tr><td align="left"><label>Memo: </label></td><td><input type="text" id="payMemoId" name="payMemoName" readonly="readonly"   style="width: 300px" value="${requestScope.memo}"></td></tr>
											</table>
									</fieldset>
								</td>
								<td style="width: 410px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 410px;height: 150px">
										<legend><label><b>Usage</b></label></legend>
										<table style="width: 400px;">
											<tr><td><label>Unapplied Payment: </label></td><td><input type="text" id="paymentId" name="paymentName"  readonly="readonly" value='<fmt:formatNumber value="${requestScope.unappliedamount}" type="currency" currencyCode="USD"/>'></td></tr>
											<tr><td><label>Credit using: </label></td><td><input type="text" id="creditusingId" name="creditusingName" readonly="readonly"></td></tr>
											<tr><td><label>Invoice Applied: </label></td><td><input type="text" id="invoiceappliedId" name="invoiceappliedName" readonly="readonly"></td></tr>
											<tr><td><label>Discount Given: </label></td><td><input type="text" id="discountgivenId" name="discountgivenName" readonly="readonly"></td></tr>
											<tr><td><label>Unapplied Payment: </label></td><td><input type="text" id="unappliedpaymentId" name="unappliedpaymentName" readonly="readonly"></td></tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table id="customerPaymets"></table>
						 <div id="customerPaymetsPager"></div>
					
					</td>
				</tr>
				<tr>
					<td>
						<table style = "width: 100%;">
							<tr style = "float:right">
								<td>
									<fieldset style="width: 130px;height:35px;display:none" class="ui-widget-content ui-corner-all" >
										<legend><label><b>Show Invoices</b></label></legend>
										<table>
											<tr>
												<td style="padding-bottom: 0px"><label><input type="radio" name="group1" id="dueRadio">Due</label></td>
												<td style="padding-bottom: 0px"><label><input type="radio" name="group1" id="payingRadio">Paying</label></td>
											</tr>
										</table>
									</fieldset>
								</td> 
								
								<!-- <td style="padding-left: 400px;" align="left"><input type="button" id="payDetailsId" name="payDetailsNmae" onclick="EditDetails()" value="Details" class="savehoverbutton turbo-blue"></td> -->
								<td style="padding-left: 0px;" align="left"><input type="button" id="editInvoiceId" name="payDetailsNmae" value="Edit Invoice" class="savehoverbutton turbo-blue" onclick="editCuInvoice()"></td>
								 <td style="padding-left: 0px;" align="left"><input type="button" id="payInvoiceId" name="payInvoiceName" onClick = "payInvoice()" value="Pay Invoice" class="savehoverbutton turbo-blue"></td>
								  <td style="padding-left: 0px;" align="left"><input type="button" id="saveInvoiceId" name="saveInvoiceName" onClick = "payToLedger()" value="Save & Close" class="savehoverbutton turbo-blue"></td>
								
							</tr>
						</table>
					</td>
				</tr>
				
			</table>
		</form>
	</div>
		  <div id="LoadingDialog" style ="display:none; width:933px; height:485px;z-index:5000; top:251px;  position: absolute; background: rgba(255,255,255,0.4);">
			<span style="margin: 0 auto;margin-left:380px;"><img src='../resources/images/loaderforcupay.gif' style ="margin-top:180px;width:150px;"></span>		
		 </div>
	
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/customerUnappliedpayments.js"></script> 

</body>
<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
</html>