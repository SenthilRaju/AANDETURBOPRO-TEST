<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Statements</title>
	<style type="text/css">
	input {height: 20px !important;padding-left: 5px; !important}
	labe{ font-size: .8em !important;}
	.labelfont{font-size: .8em !important; font-family: Verdana,Arial,sans-serif;}
	.dropdownfont{font-weight: normal;font-size: medium;font-variant: normal;font-style: normal;}
	.ui-jqgrid .ui-jqgrid-htable th div { font-size: 0.8em; !important}
	.ui-jqgrid tr.jqgrow td {font-size: .8em; !important}
	#outer {overflow:hidden; width:200px; height:400px;border:1px solid #ccc;}
	.inner { overflow:scroll; width:217px;height:417px;}
</style>
</head>
<body style="text-align: center;">
	<div><jsp:include page="./../headermenu.jsp"></jsp:include></div>
	<br><br>
	<table style="width:1000px;margin:0 auto;padding-bottom: 30px; padding-top: 0px;height:405px">
				<tr>
					<td style="padding-right: 20px;">
						<table>
						    <tr>
						    	<td>
									<table id="statementsGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					</tr>
					<tr>
					<td>
	<form id="statementsForm"style="width:400px;margin-left: 23%;padding-bottom: 55px">
	<fieldset class= " ui-widget-content ui-corner-all">
	<table>
		<tr>
		<td style="width: 100%;">
			<fieldset class= " ui-widget-content ui-corner-all">
			<legend><label class="labelfont"><b>Starting Customer</b></label></legend>
				<table>
					<tr>
						<td><label class="labelfont">Starting Customer:</label></td>
							<td>
								<select style="width:200px;" id="stCustomerID" name="stCustomerID">
									<option value="0"> - Select - </option>
										<c:forEach var="customers" items="${requestScope.customers}">
											<option value="${customers.rxMasterId}">
													<c:out value="${customers.name}" ></c:out>
											</option>
									</c:forEach>
								</select>
							</td>
						</tr>
				</table>
			</fieldset>
			</td>
		</tr>
		<tr>
			<td style="width: 100%;">
			<fieldset class= " ui-widget-content ui-corner-all">
			<legend><label class="labelfont"><b>Ending Customer</b></label></legend>
				<table>
					<tr>
						<td><label class="labelfont">Ending Customer:</label></td>
							<td>
								<select style="width:200px;" id="endCustomerID" name="endCustomerID">
									<option value="0"> - Select - </option>
										<c:forEach var="customers" items="${requestScope.customers}">
											<option value="${customers.rxMasterId}">
													<c:out value="${customers.name}" ></c:out>
											</option>
									</c:forEach>
								</select>
							</td>
						</tr>
				</table>
			</fieldset>
			</td>
		</tr>
		<tr>
			<td style="width: 50%;float: left;">
				<fieldset class= " ui-widget-content ui-corner-all">
				<legend><label class="labelfont"><b>Exclusion date:</b></label></legend>
					<input type="text" id="exclusionDate" name="exclusionDate"  style="width: 100%;">
				</fieldset>
			</td>
			<td style="width: 45%;float: right;">
				<fieldset class= " ui-widget-content ui-corner-all">
				<legend><label class="labelfont"><b>Statement Date:</b></label></legend>
					<input type="text" id="statementDate" name="statementDate"  style="width: 100%;">
				</fieldset>
			</td>
		</tr>
		<tr>
			<td>
				<fieldset class= " ui-widget-content ui-corner-all">
				<legend><label class="labelfont"><b>Starting Customer</b></label></legend>
					<input type="checkbox" value='1' id="warehouseInactive" name="warehouseInactive"><label class="labelfont" for="warehouseInactive">Show invoices with credit. </label>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td style="width: 100%;">
				<input type="button" value="Generate Reports" class="savehoverbutton turbo-blue" onclick="printStatements()" style="height: 29px !important;"  />
				<!-- <input type="button" value="Email First Statement" class="savehoverbutton turbo-blue" onclick="deleteWareHouses()"  style="height: 29px !important;" /> -->
			</td>
		</tr>
	</table>
	</fieldset>
	</form>
	</td>
	<td>
		<div style="position: fixed; left: 1078px; top: 151px; width: 340px;height: 300px ;overflow: hidden;">
			<div id="showStatements" style="position: fixed; left: 1078px; top: 151px; overflow:auto; width:357px;height:317px;">
			
			</div>
		</div>
	</td>
	</tr>
	</table>
	<div id="emailForm" style="display:none;">
		<table>
			<tr>
				<td>
					<label style="color: #FF0000;font-size: 0.8em;">Sorry, No emails found for the customer. Please enter a valid email id to Send statements. </label>
				</td>
			</tr>
					
			<tr>
				<td style="padding-bottom:15px;">
					<label style="padding-right:10px;">Email ID:</label><input type="text" id="mailId"/> <input type="button" id="emailSubmit" class="savehoverbutton turbo-blue" value="Submit" style=" height: 28px !important;" />
					<input type="button" id="cancelSubmit" class="savehoverbutton turbo-blue" value="Cancel" style=" height: 28px !important;" />
				</td>
			</tr>
		</table>
	</div>
</body>
<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/statements.js"></script>
</html>