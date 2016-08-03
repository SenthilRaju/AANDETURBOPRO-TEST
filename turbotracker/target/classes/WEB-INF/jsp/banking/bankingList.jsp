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
			.turbo-grey {
						background-color: gray;
						height: 25px;
						margin-top: 6px;
						}
		</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td style="padding-left: 280px;">
						<table><tr><td><input type="button" value="  Add" class="add" id="addChartlist" onclick="addNewBankingDetails()"></td></tr></table>
			    	</td>
			    </tr>
				<tr>
				<input type="hidden" name="hiddenbagridrowid" id="hiddenbagridrowid" style="width:80px;" value = "1"/>
				<input type="hidden" name="hiddentrgridrowid" id="hiddentrgridrowid" style="width:80px;" value = "0" />
				<input type="hidden" name="hiddenmoaccountid" id="hiddenmoaccountid" style="width:80px;" />
					<td style="padding-right: 20px;height: 475px; vertical-align: top;">
						<table id="bankingAccountsGrid"></table>
						<div id="bankingAccountsGridPager"></div>
					</td>
					<td>
						<div id="bankingDetailsTab">
							<div class="banking_tabs_main" style="padding: 0px;width:1000px;margin:0 auto; background-color: #FAFAFA;height: 660px;">
								<ul>
									<li><a href="banking/banking_bankdetails">Banking Details</a></li>
									<li><a href="banking/banking_transaction">Transaction Details</a></li>
									<li style="float: right; padding-right: 10px; padding-top:3px;">
										<label id="bankingName" style="color: white;vertical-align: middle;"></label>
									</li>
								</ul>
							
								<div style="display: none;">
									<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
								</div>
							</div>
						</div>
				</table>
			<div style="padding-top: 22px;">
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
		<!-- <script>
		$(function() { 
			$('input.alpha[$id=checkNo]').keyup(function() { 
			if (this.value.match(/[^a-zA-Z0-9 ]/g)) { 
			this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, ''); 
			} 
			}); 

			$(function() { var cache = {}; var lastXhr='';
			$("#paytoID").autocomplete({ minLength: 1,timeout :1000,
				select: function (event, ui) {
					var aValue = ui.item.value;
					var valuesArray = new Array();
					valuesArray = aValue.split("|");
					var id = valuesArray[0];
					var code = valuesArray[2];
					
					$("#checkRxMasterID").val(aValue);
					
				
					 $.ajax({
					        url: './getManufactureATTN?rxMasterId='+aValue,
					        type: 'POST',       
					        success: function (data) {
					        	$.each(data, function(key, valueMap) {					
									
									if("vendorName"==key)
									{
										$.each(valueMap, function(index, value){
											$('#paytoID').val(value.name);
										}); 
										
									}	
								});
								
					        }
					    }); 
				},
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "search/searchVendor", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} });
			});
			}); 

		function printcheckDetails()
		{
			var currentDate = new Date();
			var currentMonth = currentDate.getMonth()+1;
			var currenDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
			var currentminute = currentDate.getMilliseconds();
			var filename = "NewChecks"+currenDate+currentminute+".pdf";

			if($("#moTrnsID").val()!="")
				{
			window.open("./banking/newCheckDetails?fileName="+filename+"&moTransactionID="+$("#moTrnsID").val());
			setTimeout($.unblockUI, 2000);
				}
			else
				{
					$("#newcheckDetailsDiv").html("Please Write Check then Print");
					setTimeout(function() {
		                $('#newcheckDetailsDiv').html("");
		                }, 3000);
				}
		}
		
		</script>
		 -->
	</body>
</html>