<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="refresh" content="900" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Report Criteria</title>
<link type="text/css" href="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/themes/custom-theme/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
<link type="text/css" href="./../resources/styles/turbo-css/turbo.css" rel="stylesheet" />
<link type="text/css" href="./../resources/web-plugins/Validation_Engine/validationEngine.jquery.css" rel="stylesheet" media="screen">
<link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
<style type="text/css">
	.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
	.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
	#mainMenuToolsPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
    #mainMenuToolsPage a{background:url('./../resources/styles/turbo-css/images/user_32_white.png') no-repeat 0px 4px;color:#FFF}
</style>
</head>
<body>
<div  style="background-color: #FAFAFA">
	<div>
		 <jsp:include page="./headermenu.jsp"></jsp:include> 
	</div>
	
	<div style="width:600px;margin:0 auto;margin-top: 50px; text-align:center">
		<h1><label>Trial Balance</label></h1>
	</div>
	<table style="width:600px;margin:0 auto;margin-top: 5px;">
		<tr>
			<td align="right">
				<table>
				    <tr>
				    	<td>
				    	<div align="center" style="margin-bottom: 30px;margin-top: 20px;">
							<form id="balanceSheetForm" action="" style="width: 835px;border: 2px solid #003961; border-radius: 10px 10px 10px 10px; height: 200px;">
								<table style="width:auto;margin:0 auto;">
									<tbody><tr align="center">
										<td>
											<h2><label>Enter Criteria</label></h2>
										</td>
									</tr>
									<tr>
										<td>
											<label>G/L Accounts From:</label>
										</td>
										<td>
											<select id="glAccountFrom" style="width: 200px;">
												<option value="-1">-Select-</option>
												<c:forEach items="${glAccount}" var="value">
												    <option value="${value.coAccountId}">${ value.number} - ${value.description}</option>
												</c:forEach>												
											</select>
										</td>
										<td>
											<label>To:</label>
										</td>
										<td>
											<select id="glAccountTo" style="width: 200px;">
												<option value="-1">-Select-</option>
												<c:forEach items="${glAccount}" var="value">
												    <option value="${value.coAccountId}">${ value.number} - ${value.description}</option>
												</c:forEach>												
											</select>
										</td>
									</tr>
									<tr>
										<td>
											<label>Period Ending:</label>
										</td>
										<td>
											<select type="text" id="periodEndingSelect" name="periodEndingSelect"  style="width: 200px;">
												<option value="0"> -Select- </option>
											</select>
										</td>
									</tr>
									<tr style="height: 5px;">
									</tr>
									<tr>
										<td>
											<label>Show current period? </label>
										</td>
										<td>
											<input type="radio" id="showCurrentPeriodyes" name="showAccount" value="Yes" checked style="vertical-align: bottom;">Yes
											<input type="radio" name="showAccount" value="No" style="vertical-align: bottom;">No
										</td>
									</tr>
									<tr style="height: 10px;">
									</tr>
									<tr>
										<td></td>
										<td>
											<input type="button" value="View" class="turbo-blue savehoverbutton" onclick="generateView();">
										</td>
										<td></td>
									</tr>
								</tbody></table>
							</form>
						</div>
				    	</td>
		    		</tr>
		    	</table>
	    	</td>
	   	</tr>
	</table>
   	<div style="padding-top: 25px;">
      	<table id="footer">
			<tr>
				<td colspan="2">
					<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
				</td>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#search").hide();
		$.ajax({
	        url: './employeeCrud/getEndingPeriodListfortrialBalance',
	        type: 'POST',       
	        success: function (data) {
	        	console.log("all--->"+data);
	        	checkRefs="<option value='0'>-Select-</option>";
				$.each(data, function(key, valueMap) {								
						if("endingPeriodList" == key)
						{
							
							$.each(valueMap, function(index, value){
								if(value.period !='')
								{
								if(value.coFiscalPeriodId == value.currentPeriodid)
									checkRefs+='<option value='+value.coFiscalPeriodId+' selected >'+value.strdate+'</option>';
								else
									checkRefs+='<option value='+value.coFiscalPeriodId+' >'+value.strdate+'</option>';	
								}
							}); 
						}
						
												
				});
				//$("#LoadingImage").hide();
				$('#periodEndingSelect').html(checkRefs);
	        }
	    });
	});
	
	function generateView()
	{
		var showCurrentPeriod;
		 if ($("#showCurrentPeriodyes").is(":checked"))
			 showCurrentPeriod = true;
		else
			showCurrentPeriod = false;

			
		 createtpusage('Company-General Ledger-Trial Balance','View Trial Balance','Info','Company-General Ledger-Trial Balance,Viewing Trial Balance,fromCoAccountID:'+$("#glAccountFrom option:selected").val()+',toCoAccountID:'+$("#glAccountTo option:selected").val());

		window.location.href = "./trialBalanceView?periodEndingDate="+$('#periodEndingSelect option:selected').text()+"&fromCoAccountID="+$("#glAccountFrom option:selected").val()+"&toCoAccountID="+$("#glAccountTo option:selected").val()+"&showCurrentPeriod="+showCurrentPeriod;
	}
</script>
</body>
</html>