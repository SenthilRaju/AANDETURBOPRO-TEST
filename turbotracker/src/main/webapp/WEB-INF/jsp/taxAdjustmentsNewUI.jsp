<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - Tax Adjustments</title>
<style type="text/css">
#mainmenuBankingPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #003961;
}

#mainmenuBankingPage a {
	background:
		url('./../resources/styles/turbo-css/images/home_white_banking.png')
		no-repeat 0px 4px;
	color: #FFF
}

#mainmenuBankingPage ul li a {
	background: none;
}

input[type="text"] {
	padding-left: 5px;
}
</style>

</head>
<body style="background-color: #FAFAFA; margin: 0 auto;">
	<div style="margin: 0 auto; margin-left: 10px;">
		<div>
			<jsp:include page="./headermenu.jsp"></jsp:include>
		</div>
		<div style="width: 300px; margin: 0 auto;" id="headerText">
			<h1 style="margin-left: 60px;">Tax Adjustments</h1>
		</div>
		<div id="taxAdjustmentUIDiv" style="display: none;">
			<form id="txAdForm">
			<input type="hidden" id="taxsum">
				<table style="width: 250px; margin: 0 auto;">
					<tr>
						<td>
							<fieldset class="custom_fieldset"
								style="height: 440px; width: 475px;">
								<legend class="custom_legend">
									<label><b></b></label>
								</legend>
								<table width="250px" style="margin: 0 auto;">
									<tr>
										<td colspan="2">
											<table width="100%">
												<tr>
													<td style="width: 66px;"><label>Date :</label></td>
													<td style="width: 300px;"><input type="text"
														id="dateid" readonly="readonly" /></td>
												</tr>

												<tr>
													<td><label>Customer:</label></td>
													<td><input type="text" id="customertext" /><img
														alt="search"
														src="./../resources/scripts/jquery-autocomplete/search.png"
														style="margin-left: 5px;" title="" onclick=""> <input
														type="hidden" id="hiddenSalerep" /> <input type="hidden"
														id="rxCustomerID" /> <input type="hidden"
														id="cuInvoiceID" /></td>
												</tr>

												<tr>
													<td></td>
													<td>
														<div>
															<label>Invoice # <span id="invoiceNoAdj"></span></label>
														</div>
													</td>
												</tr>
											</table>

										</td>
									</tr>
							
									<tr>
										<td colspan="2">
											<table width="110%">
												<tr>
													<td><label>Note :</label></td>
													<td><textarea id="notestext" rows="4" cols="40"
															style="resize: none;"></textarea></td>
												</tr>
											</table>
									</tr>
					<tr>
						<td>
							<fieldset class="custom_fieldset"
								style="height: 210px; width: 335px;">
								<legend class="custom_legend">
									<label><b></b></label>Original tax
								</legend>
								<table>
									<tr>
										<td><label>Amount :</label></td>
										<td><input type="text" id="ogamounttext"
											style="text-align: right; padding-right: 5px;" /></td>
									</tr>
									<tr>
										<td><label>Freight :</label></td>
										<td><input type="text" id="ogfrieghttext"
											style="text-align: right; padding-right: 5px;" /></td>
									</tr>
									<tr>
										<td><label>GLAccount:</label></td>
										<td><select id="ogbankAccountsID" name="bankAccounts"
											style="width: 200px;">
												<option value="0">-Select-</option>
												<c:forEach var="bankAccounts"
													items="${requestScope.bankAccounts}">
													<option value="${bankAccounts.coAccountId}">
														<c:out value="${bankAccounts.description}"></c:out>
													</option>
												</c:forEach>
										</select> <input type="hidden" id="ogDivisionID" /></td>
									</tr>
									<tr>
										<td><label>Taxable:</label></td>
										<td><input type="checkbox" id="ogtaxcheck"
											onclick="checktaxable(this.value,'og')" /></td>
									</tr>
									<tr>
										<td><label>Tax Territory:</label></td>
										<td><input type="text" id="ogtaxterritoryoriginal" /> <input
											type="hidden" id="ogtaxterritoryid" /> <span id="ogTaxValue"></span></td>
									</tr>
									<tr>
										<td><label>Tax :</label></td>
										<td><input type="text" id="ogtax" readonly
											style="text-align: right; padding-right: 5px;" /> <input
											type="hidden" id="taxsetting" /> </td>
									</tr>
									<tr>
										<td><label>GrandTotal :</label></td>
										<td><input type="text" id="oggrandtotaltext" readonly
											style="text-align: right; padding-right: 5px;" /></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td></td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<div id="updatedstatus" style="color: green;"></div>
						</td>
					</tr>

					<tr>
						<td align="left"><input type="button"
							class="cancelhoverbutton turbo-tan" value="Update"
							style="width: 150px; margin-right: 10px;"
							onclick="saveTaxAdjustments()" /> <input type="button"
							class="cancelhoverbutton turbo-tan" value="Cancel"
							style="width: 150px; margin-left: 5px;"
							onclick="cancelTaxAdjustments()" /></td>


					</tr>

				</table>

				</fieldset>
				</td>
				</tr>
				</table>
			</form>
		</div>
		<div id="dgshowPos" style="margin-top: 170px;"></div>
	</div>

	<div id="TaxAdjustmentDlg" style="width: 800px;">
		<form id="taxAdForm">
			<fieldset class=" ui-widget-content ui-corner-all"
				style="height: 230px;">
				<legend>
					<label><b>Info Required</b></label>
				</legend>
				<table>
					<tr>
						<td><label class="fontclass">If you have an Invoice
								number enter it now. If there is no Invoice Number simply click
								OK now.</label></td>
					</tr>
					<tr>
						<td><input type="text" name="cuInvNoName" id="cuInvNoid"
							style="margin-top: 100px; width: 330px;"></td>
					</tr>

					<tr>
						<td><input type="button" id="saveTermsButton"
							name="saveTermsButtonName" value="OK"
							class="savehoverbutton turbo-blue"
							onclick="getCustomerInvoicefrominvNo()"
							style="float: left; width: 80px; margin-top: 10px" /> <input
							type="button" id="saveTermsButton" name="saveTermsButtonName"
							value="Cancel" class="savehoverbutton turbo-blue"
							onclick="cancel()"
							style="float: right; margin-top: 10px; width: 80px;" /></td>
					</tr>
					<tr>
						<td>
							<div id="invErrorStatus" style="color: red; font-weight: bold;"></div>
						</td>
					</tr>
				</table>

			</fieldset>
		</form>
	</div>

	<div
		style="padding-top: 22px; padding-top: 22px; position: absolute; top: 120%; width: 100%;">
		<table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
				</td>
			</tr>
		</table>
	</div>
	</div>

	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/taxAdjusmentsNewUI.js"></script>
	<script>
		$("#dateid").datepicker();

		getcoFiscalPerliodDate("dateid");

		function getcoFiscalPerliodDate(x) {
			$.ajax({

				url : "./banking/getcoFiscalPeriod",
				type : "GET",
				success : function(data) {
					
						var date = new Date();
						var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
						var lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

						$("#" + x).datepicker("option", "minDate",
								firstDay);
							$("#" + x).datepicker("option", "maxDate",
									lastDay); 
				}
			});
		}
	</script>
</body>
</html>