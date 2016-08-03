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
			
			<table style="width:100%;position: absolute;top: 25%;">
			<tr><td align="center"><h2  style="font-family: Verdana,Arial,sans-serif;"><label>Reconcile Accounts</label></h2></td>
			<tr>
			<td style="position: absolute;left: 34%;">
			<fieldset class= "custom_fieldset" style="height: 180px;width: 395px;">
						<legend class="custom_legend"><label><b>Bank Account</b></label></legend>
						<table>
						<tr><td  style ="padding:5px;"><label>Opening Balance: </label></td><td style ="padding:5px;"><input type="text" id="reconcileopenbalance" name="reconcileopenbalance" value="${openbalance}" onchange="changeevent()" style ="padding-left:5px;"/></td></tr>
						<tr><td style ="padding:5px;"><label>Ending Balance: </label></td><td style ="padding:5px;"><input type="text" id="reconcileendbalance" name="reconcileopenbalance" value="$0.00" style ="padding-left:5px;" /></td></tr>
						<tr><td style ="padding:5px;"><label>New Statement Ending Date: </label></td><td style ="padding:5px;"><input type="text" id="reconcileendbalancedate" name="reconcileopenbalance" value="" style ="padding-left:5px;" /></td></tr>
						<tr><td colspan = 2><label id="errorhidden" style="color:red;"></label><br></td></tr>
						<tr><td style ="padding-right:5px; text-align:right;"> <input type="button" id="reconcileaccount" class="cancelhoverbutton turbo-tan" value="Ok" onclick="proceedToSummary()" style="width:90px;margin-top:5px;"></td><td style ="padding-left:5px;">
 						<input type="button" id="reconcileaccount" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="cancelReconcile()" style="width:90px;margin-top:5px;">
 						<input type="hidden" id="moAccountID" value="${moaccount.moAccountId}" /></td></tr>
 						
 						</table>
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bankingAccounts.js"></script>
		<script>
		jQuery(document).ready(function() {
			$('#errorhidden').hide();
			var openbalance = $('#reconcileopenbalance').val();
			try{
				
				//openbalance = parseFloat(openbalance).toFixed(2);
				if(!isNaN(openbalance))
				$('#reconcileopenbalance').val(formatCurrency(openbalance));
				else
				$('#reconcileopenbalance').val(formatCurrency(0.0));	
				
			}catch(err){
				
			}
	
	});
		//$('#reconcileendbalancedate').datepicker();
		$( "#reconcileendbalancedate" ).datepicker();

		
		Number.prototype.formatMoney = function(c, d, t){
			var n = this, 
			    c = isNaN(c = Math.abs(c)) ? 2 : c, 
			    d = d == undefined ? "." : d, 
			    t = t == undefined ? "," : t, 
			    s = n < 0 ? "-" : "", 
			    i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", 
			    j = (j = i.length) > 3 ? j % 3 : 0;
			   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
			 };
			function proceedToSummary(){
				var enddate = $( "#reconcileendbalancedate" ).val();
				
				//reconcileopenbalance
				//reconcileendbalance
				var openbal = $('#reconcileopenbalance').val();
				var endbal = $('#reconcileendbalance').val();
				if(openbal!=null){
					openbal=openbal.replace(/[^0-9\.]+/g, "");
				}else{
					openbal=0;
					}
				if(endbal!=null){
					endbal=endbal.replace(/[^0-9\.]+/g, "");
				}else{
					endbal=0;
					}
				if(enddate!=''){
					var id=$('#moAccountID').val();
					document.location.href="./reconcileSelectedShow?moAccountID="+id+"&showReconcilingDetail=true&openbalance="+openbal+"&endbalance="+endbal;	
					
				}else{
					$('#errorhidden').text('Please select ending date');
					$('#errorhidden').show();
					setTimeout(function(){
						$('#errorhidden').hide();				
						},2000);
				}
				
			}
	
			function cancelReconcile(){
				
					document.location.href ="./reconcileAccounts";	
				
			}		
			
			
			//reconcileopenbalance
		</script>
	</body>
</html>