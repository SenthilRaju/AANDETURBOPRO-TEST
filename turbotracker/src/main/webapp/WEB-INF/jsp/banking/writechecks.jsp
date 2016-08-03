<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Write Checks</title>
		<style type="text/css">
			input[type='checkbox'] {
				margin-left: 0px;
				margin-right: 11px;
			}
			.accountRangeInput {width: 80px;}
			.accountRangeInputID {display: none;}
			#mainmenuBankingPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuBankingPage a{background:url('./../resources/styles/turbo-css/images/home_white_banking.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuBankingPage ul li a{background: none; }
			.checkstr {height: 40px; }
			#search {display: none;}
		</style>
	</head>
	<body >
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<div align="center" style="margin-bottom: 300px;">
				<form id="writeChecksFromID" action="" style="width: 660px;border: 2px solid #003961; border-radius: 10px 10px 10px 10px; height: 350px;">
					<table style="width:650px;margin:0 auto;">
						<tr>
							<td style="" colspan="3">
								<h2 style="font-family: Verdana,Arial,sans-serif;"><label>Write Checks</label></h2>
							</td>
							<td style="display: none;">
							<input type="text" id="billToPayExistsId" value="${requestScope.billExists}"/>
							</td>
						</tr>
						<tr class="checkstr">
							<td><label>Check Type: </label></td>
							<td><input type="radio" name="checkType" value="Vendorchecks" checked="checked"><label>Vendor Checks</label>
							<input type="radio" name="checkType" value="Vendorchecks" disabled ><label>Payroll Checks</label></td>
						</tr>
						<tr>
							<td><label>Using Account:</label>
							<td align="left">
									<select id="bankAccountsID" name="bankAccounts" style="width: 250px;">
										<!-- <option value="0"> -Select- </option> -->
											<c:forEach var="bankAccounts" items="${requestScope.bankAccounts}">
												<option value="${bankAccounts.moAccountId}">
														<c:out value="${bankAccounts.description}"></c:out>
												</option>
										</c:forEach>
								</select>
							</td>
							<td align="right" style="display: none;">
									<select id="CheckStartingID" name="CheckStartingID" style="width: 170px;">
										<option value="0"> -Select- </option>
											<c:forEach var="CheckStartingID" items="${requestScope.bankAccounts}">
												<option value="${CheckStartingID.moAccountId}">
														<c:out value="${CheckStartingID.nextCheckNumber}"></c:out>
												</option>
										</c:forEach>
								</select>
							</td>
						</tr>
						<tr class="checkstr">
							<td colspan="3"><input type="checkbox"><label> Void checks for direct deposit Vendors</label></td>
						</tr>
						<tr class="checkstr">
							<td colspan="3"><label>Using new Check # Starting with: </label>
							<input type="text" id="checkNumberID" name="checkNumber"/></td>
						</tr>
						<tr class="checkstr">
							<td colspan="3"><label>Date Checks:</label><input type="text" class="datepicker" id="checkDateId" readonly="readonly" name="checksDate"/></td>
						</tr>
						<tr class="checkstr" style="margin-top: 10px; height: 60px;">
							<td colspan="3" style="vertical-align: bottom;">
								<input type="button" value="Print" class="turbo-blue savehoverbutton" onclick="printCheck()" style="margin-left: 10px; font-size: 15px;" />
								<input type="button" value="Select Printer" onclick="selectPrinter()" style="margin-left: 10px;font-size: 15px;"	 class="turbo-blue savehoverbutton"/>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<!-- <input type="button" id="test" value="testGrawlMessage" />
			<input type="button" id="test1" value="testGrawlMessage1" /> -->
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
		<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				 getNewCheckNO();
				 $("#bankAccountsID").change(function(){
					getNewCheckNO();
				});
									
				var billExists = $('#billToPayExistsId').val();
				if(billExists==='true'){
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; //January is 0!
					var yyyy = today.getFullYear();
					if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = mm+'/'+dd+'/'+yyyy;
					$("#search").css("display", "none");
					
					$("#checkDateId").val(today);
					 getcoFiscalPerliodDate("checkDateId");
					
					
				}else{
					var newDialogDiv = document.createElement("div");
					var errorText = "No Bills are Selected...Please go the vendor pay bills and select the bills to be payed.";
					$(newDialogDiv).attr("id","msgdlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#writeChecksFromID :input").attr("disabled", true);document.location.href="./vendorbills";return false;}}]}).dialog("open");
				}
			/* 	$('#test').click(function() {
					msgOnAjax("error","red");});
				$('#test1').click(function() { 
					msgOnAjax("Success", "green");}); */

				 
				
			});

			function getNewCheckNO(){
				 $.ajax({
					 
				 		url: "./banking/getMaxReference?accountID="+$('#bankAccountsID').val(),
				 		type: "GET",
				 		success: function(data) {
				 			console.log('NewRef:'+data);
											 			
				 			if(data === 'null')
					 			{
				 			$('#checkNumberID').val("1");	
				 			console.log('NewRef:---0');
					 			}
				 			else
					 			{
				 			$('#checkNumberID').val(data);	
				 			console.log('NewRef:==='+data);
					 			}
				 		}		 
				 });
		}

		
			
			function printCheck1(){
				 $.ajax({
					 
				 		url: "./banking/getNegativeAmountinVB",
				 		type: "GET",
				 		success: function(data) {
					 		
				 			console.log('NewRef:'+data);
				 		
				 		}		 
				 });
			}
			
			function printCheck(){

				var checkNoStatus=false;
				var currentDate = new Date();
				var currentMonth = currentDate.getMonth()+1;
				var currenDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
				var currentminute = currentDate.getMilliseconds();
				var filename = "PrintChecks"+currenDate+currentminute+".pdf";
				var newDialogDiv = document.createElement("div");
				var writeChecksDetails = $('#writeChecksFromID').serialize();

			 	createtpusage('Banking-Transaction Details','Printing Check','Info','Banking,Printing Check,Check No:'+$("#checkNumberID").val()+'moTransactionID='+$("#bankAccountsID").val());
				
			 $.ajax({
			 		url: "./banking/checkBillAvailability",
			 		type: "GET",
			 		data: {moAccountID:$('#bankAccountsID').val()},
			 		success: function(data) {

				 	if(data)	
					{
					 $.ajax({
							 
				 		url: "./banking/getNegativeAmountinVB",
				 		type: "GET",
				 		success: function(data) {
					 		
				 		console.log('NewRef:'+data);
						 		if(data)
							 		{
										$.ajax({
											url:'./banking/getCheckNumberExists',
											type:"POST",
											data: {checkno:$('#checkNumberID').val(),moAccountID:$('#bankAccountsID').val()},
											success:function(data){ 
		
											checkNoStatus = data;
											console.log(checkNoStatus);
		
													if(checkNoStatus)
														{
														var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
														$.ajax({
															url: "./checkAccountingCyclePeriods",
															data:{"datetoCheck":$('#checkDateId').val(),"UserStatus":checkpermission},
															type: "POST",
															success: function(data) { 
																if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
																{
																	periodid=data.cofiscalperiod.coFiscalPeriodId;
																	yearid = data.cofiscalperiod.coFiscalYearId;
														
																$.ajax({
																url:'./banking/printCheck',
																type:"POST",
																data: writeChecksDetails+'&coFiscalPeriodId='+periodid+"&coFiscalYearId="+yearid,
																success:function(data){ 
																	createtpusage('Write Checks','Printing Check','Info','Write Checks,Printing Check,Check No:'+$("#checkNumberID").val());
																	document.getElementById("writeChecksFromID").reset();
																	window.open("./banking/printCheckDetails?fileName="+filename);
																	setTimeout($.unblockUI, 2000);
															 	},
																error:function(data){
																	var Warnintext = "Printing Checks is not Possible Now. Try a alittle Later..!!";
																	$(newDialogDiv).attr("id","msgdlg");
																	jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
																	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
																		buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
																}
																});
																}
																else
																	{
																	
																	if(data.AuthStatus == "granted")
																	{	
																	var newDialogDiv = jQuery(document.createElement('div'));
																	jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
																	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
																							buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
																						}).dialog("open");
																	}
																	else
																	{
																		showDeniedPopup();
																	}
																	}
														  	},
												   			error:function(data){
												   				console.log('error');
												   				}
												   			});
														}
													else
														{
												
															var Warnintext = "This Check Number Already Used.";
															$(newDialogDiv).attr("id","msgdlg");
															jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
															jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
																buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
														
														}
											}
											});	
		
							 		}
						 			else
							 		{
		
						 				$.ajax({
											url:'./banking/getCheckNumberExists',
											type:"POST",
											data: {checkno:$('#checkNumberID').val(),moAccountID:+$('#bankAccountsID').val()},
											success:function(data){ 
		
											checkNoStatus = data;
											console.log(checkNoStatus);
		
										if(checkNoStatus)
										{
										
						 				var Warnintext = "Check Printing Complete. Some Checks where skipped \n since check amount would be negative.\n See vendor approved report for details.<br><br> See the report now?";
						 				var newDialogDivnegcheck = jQuery(document.createElement('div'));
										$(newDialogDivnegcheck).attr("id","msgdlg");
										jQuery(newDialogDivnegcheck).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
										jQuery(newDialogDivnegcheck).dialog({modal: true, width:400, height:200, title:"Warning",
											buttons: [{
												
												text: "Yes",
												click: 
													function() {
													var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
													$.ajax({
														url: "./checkAccountingCyclePeriods",
														data:{"datetoCheck":$('#checkDateId').val(),"UserStatus":checkpermission},
														type: "POST",
														success: function(data) { 
															if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
															{
																periodid=data.cofiscalperiod.coFiscalPeriodId;
																yearid = data.cofiscalperiod.coFiscalYearId;

													
																	$.ajax({
																		url:'./banking/printCheck',
																		type:"POST",
																		data: writeChecksDetails+'&coFiscalPeriodId='+periodid+"&coFiscalYearId="+yearid,
																		success:function(data){ 
												
																			document.getElementById("writeChecksFromID").reset();														
																			jQuery(newDialogDivnegcheck).dialog("close");
																			//document.location.href="./vendorbills";
																			
																			
																			var filename = "ApprovedBills"+currenDate+currentminute+".pdf";
																			window.open("./banking/creditCheckDetails?fileName="+filename+"&moAccountId="+$('#bankAccountsID').val());
																			setTimeout($.unblockUI, 2000);

																			filename = "PrintChecks"+currenDate+currentminute+".pdf";
																			window.open("./banking/printCheckDetails?fileName="+filename);
																			setTimeout($.unblockUI, 2000);
																			jQuery(newDialogDivnegcheck).dialog("close");	

																			
																	 	},
																		error:function(data){
																			var Warnintext = "Printing Checks is not Possible Now. Try a alittle Later..!!";
																			$(newDialogDiv).attr("id","msgdlg");
																			jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
																			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
																				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
																		}
																		});
															}
															else
																{
																
																if(data.AuthStatus == "granted")
																{	
																var newDialogDiv = jQuery(document.createElement('div'));
																jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
																jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
																						buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
																					}).dialog("open");
																}
																else
																{
																	showDeniedPopup();
																}
																}
													  	},
											   			error:function(data){
											   				console.log('error');
											   				}
											   			});
													 }
											 },
											 {
												 text: "No",
												 click: 
													function() {
													 var checkpermission=getGrantpermissionprivilage('OpenPeriod_PostingOnly',0);
													 $.ajax({
															url: "./checkAccountingCyclePeriods",
															data:{"datetoCheck":$('#checkDateId').val(),"UserStatus":checkpermission},
															type: "POST",
															success: function(data) { 
																if(data.cofiscalperiod!=null && typeof(data.cofiscalperiod.period) !== "undefined" )
																{
																	periodid=data.cofiscalperiod.coFiscalPeriodId;
																	yearid = data.cofiscalperiod.coFiscalYearId;


																	 $.ajax({
																			url:'./banking/printCheck',
																			type:"POST",
																			data: writeChecksDetails+'&coFiscalPeriodId='+periodid+"&coFiscalYearId="+yearid,
																			success:function(data){ 
																				document.getElementById("writeChecksFromID").reset();
				
																				if(data=="Success")
																				{
																				window.open("./banking/printCheckDetails?fileName="+filename);
																				setTimeout($.unblockUI, 2000);
																				jQuery(newDialogDivnegcheck).dialog("close");
																				}
																				else
																				{
																				$.post("./banking/clearPayBills", {moAccountId: $('#bankAccountsID').val()}, function(result){ });
																				jQuery(newDialogDivnegcheck).dialog("close");
																				}
																		 	},
																			error:function(data){
																				var Warnintext = "Printing Checks is not Possible Now. Try a alittle Later..!!";
																				$(newDialogDiv).attr("id","msgdlg");
																				jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
																				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
																					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
																			}
																			});
																}
																else
																	{
																	
																	if(data.AuthStatus == "granted")
																	{	
																	var newDialogDiv = jQuery(document.createElement('div'));
																	jQuery(newDialogDiv).html('<span><b style="color:red;">Current Transcation Date is not under open period.</b></span>');
																	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.", 
																							buttons: [{text: "OK",click: function(){$(this).dialog("close"); }}]
																						}).dialog("open");
																	}
																	else
																	{
																		showDeniedPopup();
																	}
																	}
														  	},
												   			error:function(data){
												   				console.log('error');
												   				}
												   			});
													 
													 }
											 },
											 {
												 text: "Cancel",
												 click: 
													function() {
													 $(this).dialog("close");
													 }
											 },
													 
		
											 ]}).dialog("open");
		
							 				}
											
											else
												{
										
													var Warnintext = "This Check Number Already Used.";
													$(newDialogDiv).attr("id","msgdlg");
													jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
													jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
														buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");
												
												}
									}
									});
							
										
							}	}	 
					});

					}
				 	else
					 	{
				 		var Warnintext = "No bills found under this bank.";
						$(newDialogDiv).attr("id","msgdlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+Warnintext+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");}}]}).dialog("open");

					 	}
			 		}
		 		
			 	});
	 
			}
			function msgOnAjax(msg,msgcolor){
				var message = '';
				if(msg==='Success'){
					message="<h2><img style='height:30px;' src='./../resources/Icons/tick_job.png'>"+msg+"</h2>";
				}else{
					message = "<h2><img style='height:30px;' src='./../resources/Icons/delete_new.png'>"+msg+"</h2>";
				}
				 $.blockUI({ 
		             message: message, 
		             fadeIn: 700, 
		             fadeOut: 700, 
		             timeout: 2000, 
		             showOverlay: false, 
		             centerY: false, 
		             css: { 
		                  width: '350px',
		                 height: 'auto',
		                 top: '80px', 
		                 left: '', 
		                 right: '10px', 
		                 border: 'none',
		                 padding: '5px', 
		                 backgroundColor: '#000', 
		                 opacity: .5, 
		                 color: msgcolor,
		                 "border-radius":"15px 15px 15px 15px"
		             } 	
		         }); 
			}

			function selectPrinter(){
				$.ajax({
					url: "http://192.168.43.30:3333/qb/getponumber/1",
					type: "GET",
					async:false,
					data:1,
					success: function(data) {
					},
					error: function(data){
						msgOnAjax(("this feature is under our workshop. Thanks..", "red",5000));	
					}
					});
			}
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