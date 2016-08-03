<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	<table style="width:600px;margin:0 auto;margin-top: 67px;">
		<tr>
			<td align="right">
				<table>
				    <tr>
				    	<td>
				    	<div align="center" style="margin-bottom: 30px;margin-top: 70px;">
							<form id="balanceSheetForm" action="" style="width: 524px;border: 2px solid #003961; border-radius: 10px 10px 10px 10px; height: 200px;">
								<table style="width:auto;margin:0 auto;">
									<tbody><tr align="center">
										<td>
											<h2><label>Enter Criteria</label></h2>
										</td>
									</tr>
									<tr>
										<td>
											<label>Period Ending:</label>
										</td>
										<td>
											<select type="text" id="periodEndingSelect" name="periodEndingSelect" style="width: 200px;">
												<option value="0"> -Select- </option>
											</select>
										</td>
									</tr>
									<tr style="height: 5px;">
									</tr>
									<tr>
										<td>
											<label>Show Account No :</label>
										</td>
										<td>
											<input type="Checkbox" name="showAccount" id="showAccount"   >
											<!-- <input type="radio" name="showAccount" value="No" style="vertical-align: bottom;">No -->
										</td>
									</tr>
									<tr style="height: 10px;">
									</tr>
									<tr>
										<td></td>
										<td>
											<input type="button" value="View" class="turbo-blue savehoverbutton" onclick="viewpdf()">
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
	/* 	
		$.ajax({
	        url: './employeeCrud/getCoFiscalPeriodList',
	        type: 'POST',       
	        success: function (data) {
	        	console.log("all--->"+data);
	        	checkRefs="<option value='0'>-Select-</option>";
				$.each(data, function(key, valueMap) {								
						if("endingPeriodList" == key)
						{
							
							$.each(valueMap, function(index, value){
								console.log('You r right');
								if(value.ecPeriodId != '')
									checkRefs+='<option value='+value.coFiscalPeriodId+'>'+value.strdate+'</option>';
							
							}); 
						}
												
				});
				//$("#LoadingImage").hide();
				$('#periodEndingSelect').html(checkRefs);
	        }
	    });
 */
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
									
								console.log("period Ending:-->"+value.strdate);
								}
							}); 
						}
					
												
				});
				//$("#LoadingImage").hide();
				$('#periodEndingSelect').html(checkRefs);
	        }
	    });

	    
	});

	function viewpdf(){
		
		var showaccount=$("#showAccount").is(':checked');
		var cofiscalperiodid=$("#periodEndingSelect").val();
		var date=$("#periodEndingSelect option:selected").text();
		createtpusage('Company-General Ledger-Balance Sheet','View Balance Sheet','Info','Company-General Ledger-Balance Sheet,Viewing Balance Sheet,cofiscalperiodid:'+cofiscalperiodid+',date:'+date);
		/* window.open("./quotePDFController/printBalanceSheet?cofiscalperiodid="+cofiscalperiodid+"&showaccount="+showaccount+"&asofdate="+date); */
		window.open("./drillDown/printBalanceSheet?cofiscalperiodid="+cofiscalperiodid+"&showaccount="+showaccount+"&asofdate="+date);
		return true; 
		}
</script>
</body>
</html>