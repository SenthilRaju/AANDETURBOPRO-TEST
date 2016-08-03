<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Settings Tab</title>
<style type="text/css">
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		</style>
</head>
<body>
<div  style="background-color: #FAFAFA">
<div>
	 <jsp:include page="./headermenu.jsp"></jsp:include> 
</div>
<div class="tabs_main" style="padding-left: 0px;width:1007px;margin:0 auto; background-color: #FAFAFA;height: 960px;right:8px; box-shadow: 1px 6px 5px 5px #AAAAAA;">
	<ul>
		<li id="settingsFormDetails"><a href="">Company Settings</a></li>
	</ul>
	<br><br>
		
<div id="settingsFormDetails">
	<div id="uploadExcel_Form" style="width: 750px;" align="center">
		<form id="form2" method="post" action="" enctype="multipart/form-data">
			<!-- File input -->    
			<label>Upload Company Logo: </label> <input name="file2" id="file2" type="file" style="height:25px;" />
		</form>
		<button value="Upload" class="turbo-tan savehoverbutton" style="margin-left: 330px;margin-top: 3px;" onclick="uploadJqueryForm()"  >Upload</button>
	</div>
	<div id="showImage" >
	<img src="./companyLogo" style="width: 200px; height: 150px; position: absolute; left: 600px; top: 38px;"/>
	</div>
   <form action="" id="companyFormId" name="companyForm">
  	  <table align="left">
		 <tr>
			<td>
				<fieldset  class= " ui-widget-content ui-corner-all" style="width:978px;height:796px;border:0px">
					<table>
						<!-- <tr style="height: 20px;"><td></td></tr> -->
	       				<tr align="center">
	        				<!-- <td>
                				<label>Upload Company Logo:</label>
			    				<input id="avatar" type="file" name="avatar" size="30" style="height:21px;">
			    				<button class="turbo-blue" style="height: 21px;color: white;display: none;" id="upload">Upload</button>
							    <input type="button" class ="turbo-blue" value="Upload" id="ucompanydetailspload" name="uploadimage"style="height:21px;color:white;" onclick="uploadImage()">
	            			</td>  -->
						</tr>
						<tr align="center">
						  <td>
							<table style="position: relative; right: -19px;">
								<tr align="center">
									<td><label>Quote</label>
										<input type="checkbox" id="1quote" name="quote" style="vertical-align: middle;" >
									</td>
									<td><label>Quick Quote</label>
										<input type="checkbox" id="1quickquote" name="quickquote" style="vertical-align: middle;" >
									</td>
									<td><label>Invoices</label>
										<input type="checkbox" id="1invoices" name="invoices" style="vertical-align: middle;" >
									</td>
									<td><label>Purchase Orders</label>
										<input type="checkbox" id="1puchaseorder" name="puchaseorder" style="vertical-align: middle;" >
									</td>
								</tr>
							</table>
							<br>
						  </td>
						</tr>
						<tr align="center">
						<td>
							<table style="padding-top: 25px;">
								<tr>
	   								<td style="width: 160px;text-align: right;"><label>Header:</label></td>
									<td>
	   									<textarea cols="70" id="headerTextId" style="height: 90px;">${requestScope.headerTextSettings}</textarea>
	   								</td>
	   							</tr>
	   						</table>
	   					</td>
	   					</tr>
	   					<tr align="center"><td>
							<table style="position: relative; right: -21px;">
      							<tr align="center">
									<td><label>Quote</label>
										<input type="checkbox" id="quote11" name="quote1" style="vertical-align: middle;" >
									</td>
									<td><label>Quick Quote</label>
										<input type="checkbox" id="quickquote11" name="quickquote1" style="vertical-align: middle;" >
									</td>
									<td><label>Invoices</label>
										<input type="checkbox" id="invoices11" name="invoices1" style="vertical-align: middle;" >
									</td>
									<td><label>Purchase Orders</label>
										<input type="checkbox" id="puchaseorder11" name="puchaseorder1" style="vertical-align: middle;" >
									</td>
	   							</tr>
							</table><br>
						</td></tr>
						<tr align="center">
						<td>
							<table>
								<tr>
									<td style="width: 160px;text-align: right;"><label>Terms:</label></td>
									<td>
									<textarea cols="70" id="termId" style="height: 90px;">${requestScope.termsTextSettings}</textarea>
									</td>
								</tr>
							</table>
							</td></tr>
							<tr align="center"><td>
								<table style="position: relative; right: -21px;">
									<tr align="center">
										<td><label>Quote</label>
											<input type="checkbox" id="quote21" name="quote2" style="vertical-align: middle;" >
										</td>
										<td><label>Quick Quote</label>
											<input type="checkbox" id="quickquote21" name="quickquote2" style="vertical-align: middle;" >
										</td>
										<td><label>Invoices</label>
											<input type="checkbox" id="invoices21" name="invoices2" style="vertical-align: middle;" >
										</td>
										<td><label>Purchase Orders</label>
											<input type="checkbox" id="puchaseorder21" name="puchaseorder2" style="vertical-align: middle;" >
										</td>
									</tr>
								</table><br>
								</td></tr>
								<tr>
									<td>
										<fieldset class="ui-widget-content ui-corner-all" style="margin-left: 75px;width:325px">
												<legend class="custom_legend"><label><b>Bill To</b></label></legend>
										<table>
											<tr>
												<td>
													<table>
														<tr>
									 						<td><input type="text" id="billToAddressID" name="billToAddressName" class="validate[maxSize[100]" style="width: 300px;" value="${requestScope.userLoginSettings.billToDescription}"></td>
									 					</tr>
										 				<tr>
										 					<td><input type="text" id="billToAddressID1" name="billToAddress1Name" class="validate[maxSize[100]" style="width: 300px;" value="${requestScope.userLoginSettings.billToAddress1}"></td>
										 				</tr>
										 				<tr>
															<td><input type="text" id="billToAddressID2" name="billToAddress2Name" class="validate[maxSize[40]" style="width: 300px;" value="${requestScope.userLoginSettings.billToAddress2}"></td>
														</tr>
									 					<tr>
									 						<td><input type="text" id="billToCity" name="billToCityName" style="width: 100px;" value="${requestScope.userLoginSettings.billToCity}">
																	<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
									 								<input type="text" id="billToState" name="billToStateName" style="width: 30px; text-transform: uppercase" maxlength="2" value="${requestScope.userLoginSettings.billToState}">
									 								<label>Zip: </label><input type="text" id="billToZipID" name="billToZipName" style="width: 75px;" value="${requestScope.userLoginSettings.billToZip}">
									 						</td>
									 					</tr>
													</table>
												</td>
							 				  </tr>
										</table>
									 </fieldset>
									 <br>
									</td>
									</tr>
									<tr>
									<td>
										<fieldset class="ui-widget-content ui-corner-all" style="height: 130px;margin-left: 75px">
												<legend class="custom_legend"><label><b>Ship To</b></label></legend>
												<table>
												<tr><td>
													<table>
														<tr>
									 						<td><input type="text" id="NorcrossbillToAddressID" name="NorcrossbillToAddressName" class="validate[maxSize[100]" style="width: 300px;"></td>
									 					</tr>
										 				<tr>
										 					<td><input type="text" id="NorcrossbillToAddressID1" name="NorcrossbillToAddress1Name" class="validate[maxSize[100]" style="width: 300px;"></td>
										 				</tr>
										 				<tr>
															<td><input type="text" id="NorcrossbillToAddressID2" name="NorcrossbillToAddress2Name" class="validate[maxSize[40]" style="width: 300px;"></td>
														</tr>
									 					<tr>
									 						<td><input type="text" id="NorcrossbillToCity" name="NorcrossbillToCityName" style="width: 100px;">
																	<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
									 								<input type="text" id="NorcrossbillToState" name="NorcrossbillToStateName" style="width: 30px; text-transform: uppercase" maxlength="2">
									 								<label>Zip: </label><input type="text" id="NorcrossbillToZipID" name="NorcrossbillToZipName" style="width: 75px;">
									 						</td>
									 					</tr>
														</table>
													</td>
													<td>
													    <table>
														<tr>
									 						<td><input type="text" id="BirminghambillToAddressID" name="BirminghambillToAddressName" class="validate[maxSize[100]" style="width: 300px;"></td>
									 					</tr>
										 				<tr>
										 					<td><input type="text" id="BirminghambillToAddressID1" name="BirminghambillToAddress1Name" class="validate[maxSize[100]" style="width: 300px;"></td>
										 				</tr>
										 				<tr>
															<td><input type="text" id="BirminghambillToAddressID2" name="BirminghambillToAddress2Name" class="validate[maxSize[40]" style="width: 300px;"></td>
														</tr>
									 					<tr>
									 						<td><input type="text" id="BirminghambillToCity" name="BirminghambillToCityName" style="width: 100px;">
																	<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
									 								<input type="text" id="BirminghambillToState" name="BirminghambillToStateName" style="width: 30px; text-transform: uppercase" maxlength="2">
									 								<label>Zip: </label><input type="text" id="BirminghambillToZipID" name="BirminghambillToZipName" style="width: 75px;">
									 						</td>
									 					</tr>
									 				   </table>
									 				   </td>
									 				  </tr>
									 			</table>
											 </fieldset>
										 </td>
									</tr>
					</table>
					<br>
					<hr style="width: 935px;">
					<table align="center" style="width:600px;">
						<tr>
							<td></td>
							<td align="right" style="padding-left:735px;">
								<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveCompanySetting()" style=" width:80px;">
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	</form>
</div>
</div>
<div style="display: none;">
	<table>
		<tr>
			<td>
				<input type="text" id="quote_ID" name="quote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quote}">
				<input type="text" id="quickQuote_ID" name="quickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.quickQuote}">
				<input type="text" id="invoice_ID" name="invoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.invoices}">
				<input type="text" id="purchaseOrder_ID" name="purchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.purchaseOrders}">
				<input type="text" id="headerQuote_ID" name="headerQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuote}">
				<input type="text" id="headerQuick_ID" name="headerQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerQuickQuote}">
				<input type="text" id="headerInvoice_ID" name="headerInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerInvoices}">
				<input type="text" id="headerPurchaseOrder_ID" name="headerPurchaseOrder_Name" style="width: 30px;" value="${requestScope.userLoginSettings.headerPurchaseOrders}">
				<input type="text" id="termsQuote_ID" name="termsQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuote}">
				<input type="text" id="termsQuickQuote_ID" name="termsQuickQuote_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsQuickQuote}">
				<input type="text" id="termsInvoice_ID" name="termsInvoice_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsInvoices}">
				<input type="text" id="termsPO_ID" name="termsPO_Name" style="width: 30px;" value="${requestScope.userLoginSettings.termsPurchaseOrders}">
			</td>
		</tr>
	</table>
</div>
<table id="footer">
  <tr>
	<td colspan="2">
		<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
	</td>
 </tr>
</table>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.form.min.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/settings.js"></script>
<script type="text/javascript">
function loadwareHouseBilToAddress(){
	var reminders = [];
    <c:forEach items="${requestScope.wareHouse}" var="reminder">
        reminders.push({description: "${reminder.description}", address1: "${reminder.address1}", address2: "${reminder.address2}", city: "${reminder.city}", state: "${reminder.state}", zip: "${reminder.zip}"});
    </c:forEach>
    var aWareHouse = "${requestScope.wareHouse}";
    if(aWareHouse.length !== 0){
	   	var countWarehouse = aWareHouse.split(",");
	    if(countWarehouse.length == 1){
	    	$("#NorcrossbillToAddressID").val(reminders[0].description); $("#NorcrossbillToAddressID1").val(reminders[0].address1); $("#NorcrossbillToAddressID2").val(reminders[0].address2); $("#NorcrossbillToCity").val(reminders[0].city);
			 $("#NorcrossbillToState").val(reminders[0].state); $("#NorcrossbillToZipID").val(reminders[0].zip);
	    }
	    if(countWarehouse.length == 2){
			 $("#NorcrossbillToAddressID").val(reminders[0].description); $("#NorcrossbillToAddressID1").val(reminders[0].address1); $("#NorcrossbillToAddressID2").val(reminders[0].address2); $("#NorcrossbillToCity").val(reminders[0].city);
			 $("#NorcrossbillToState").val(reminders[0].state); $("#NorcrossbillToZipID").val(reminders[0].zip);
			 $("#BirminghambillToAddressID").val(reminders[1].description); $("#BirminghambillToAddressID1").val(reminders[1].address1); $("#BirminghambillToAddressID2").val(reminders[1].address2); $("#BirminghambillToCity").val(reminders[1].city);
			 $("#BirminghambillToState").val(reminders[1].state); $("#BirminghambillToZipID").val(reminders[1].zip);
	    }
	}
}
</script>
</body>
</html>