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
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<div style="width:100%; margin:0 auto;">
			
			<table style="width:100%;position: absolute;top: 40%;">
			<tr>
			<td style="position: absolute;left: 40%;">
			<fieldset class= "custom_fieldset" style="height: 200px;width: 350px;">
						<legend class="custom_legend"><label><b>Bank Account</b></label></legend>
						
						<label>Opening Balance:               </label><input type="text" id="reconcileopenbalance" name="reconcileopenbalance" value="${openbalance}" /><br><br>
						<label>Ending Balance:                </label><input type="text" id="reconcileendbalance" name="reconcileopenbalance" value="" /><br><br>
						<label>New Statement Balance: </label><input type="text" id="reconcileendbalancedate" name="reconcileopenbalance" value="" /><br><br>
						<input type="button" id="reconcileaccount" class="cancelhoverbutton turbo-tan" value="Ok" onclick="reconcileSelectAccount()" style="width:90px;margin-top:25px;">
 						<input type="button" id="reconcileaccount" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="reconcileSelectAccount()" style="width:90px;margin-top:25px;">
 						</fieldset>
			
			</td>
			</tr>
			<tr>
			<td>
			
			</td>
			</tr>
			</table>
			</div>
			<div style="padding-top: 22px;padding-top: 22px;position: absolute;top: 77%;width: 100%;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bankingAccounts.js"></script> -->
	</body>
</html>