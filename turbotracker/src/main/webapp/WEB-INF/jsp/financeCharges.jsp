<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Banking Accounts</title>
		<style type="text/css">
			#mainmenuBankingPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuBankingPage a{background:url('./../resources/styles/turbo-css/images/home_white_banking.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuBankingPage ul li a{background: none; }
		</style>
		
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include>
			</div>
			
			
			<table style="width:100%;">
			<tr>
			<td style="position: absolute;left: 20%;">
			<fieldset class= "custom_fieldset" style="height: 650px;width: 800px;">
						<legend class="custom_legend"><label><b></b></label></legend>
						<table>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 100px;width: 500px;">
						<legend class="custom_legend"><label><b>Cut Off Date</b></label></legend>
						<table>
						<tr>
						<td><p>Every balance due prior to this date will qualify for a finance charge</p></td>
						<td><input type="text" id="cutoffdateid" readonly="readonly" value=""></td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 100px;width: 500px;">
						<legend class="custom_legend"><label><b>Finance Charge Percent</b></label></legend>
						<table>
						<tr>
						<td><p>If you are computing a monthly finance charge,divide the APR by 12</p></td>
						<td><input type="number" id="financechargepercentid"  value="" /></td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 100px;width: 500px;">
						<legend class="custom_legend"><label><b>Minimum Finance Charge</b></label></legend>
						<table>
						<tr>
						<td><p>Finance charges smaller will be skipped                 </p></td>
						<td>$<input type="text" id="minumumchargeid"  value=""></td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 60px;width: 500px;">
						<legend class="custom_legend"><label><b>Invoice Date for Finance Charges</b></label></legend>
						<table>
						<tr>
						<td><p></p></td>
						<td><input type="text" id="invoicedateid" readonly="readonly"  value=""></td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 60px;width: 500px;">
						<legend class="custom_legend"><label><b>Sales Rep To Assign To Invoice</b></label></legend>
						<table>
						<tr>
						<td><p></p></td>
						<td>
						<select id="bankAccountsID" name="bankAccounts" style="width: 300px;">
										<option value="0"> -Select- </option>
											<c:forEach var="bankAccounts" items="${requestScope.bankAccounts}">
												<option value="${bankAccounts.rxMasterId}">
														<c:out value="${bankAccounts.firstName} ${bankAccounts.name}"></c:out>
												</option>
										</c:forEach>
								</select>
						</td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td><fieldset class= "custom_fieldset" style="height: 60px;width: 500px;">
						<legend class="custom_legend"><label><b>Product Item Code To Show In Detail</b></label></legend>
						<table>
						<tr>
						<td><p></p></td>
						<td><input type="text" id="productitemcode"  value=""></td>
						</tr>
						</table>
						</fieldset></td>
						</tr>
						<tr>
						<td>
						Include unpaid finance charges in balance?<input type="checkbox" id="unpaidbalancecheck" value="" />
						</td>
						</tr>
						</table>
						</fieldset>
							<input type="button" class="cancelhoverbutton turbo-tan" value="Create Finance Charges" id="createfinancecharges" onClick="createFinanceCharge()" style="width:150px;"/>
			</td>
			</tr>
			<tr>
			<td>
		
			</td>
			</tr>
			</table>
			
			
			<div style="padding-top: 22px;padding-top: 22px;position: absolute;top: 120%;width: 100%;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
<!-- 		<script type="text/javascript" src="./resources/scripts/turbo_scripts/bankingAccounts.js"></script> -->
<script>
		$( "#cutoffdateid" ).datepicker();
		$( "#invoicedateid" ).datepicker();
		 getcoFiscalPerliodDate("cutoffdateid");
		 getcoFiscalPerliodDate("invoicedateid");
		$('#minumumchargeid').keyup(function() {
			var inputval =$('#minumumchargeid').val();
			
		  if(isNaN(inputval)){
			  $('#minumumchargeid').val('')
		  }
		});

		function getcoFiscalPerliodDate(x)
		 {
			 $.ajax({
				 
			 		url: "./banking/getcoFiscalPeriod",
			 		type: "GET",
			 		success: function(data) {
			 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
			 					
			 		}		 
			 });	 
		 }
		</script>
	</body>
</html>