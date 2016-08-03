<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Reissue Checks</title>
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
								<h2 style="font-family: Verdana,Arial,sans-serif;"><label>Reissue Checks</label></h2>
							</td>
							<td style="display: none;">
							<input type="text" id="billToPayExistsId" value="${requestScope.billExists}"/>
							</td>
						</tr>
						<tr class="checkstr">
							<td><label>Check Type: </label></td>
							<td><input type="radio" name="checkType" value="Vendorchecks" id="checkTypeId" checked="checked"><label>Vendor Checks</label>&nbsp;&nbsp;<input type="radio" name="checkType" value="Payrollchecks" readonly="readonly" disabled><label>Payroll Checks</label></td>
						</tr>
						<tr>
							<td><label>Using Account:</label>
							<td align="left">
								<select id="bankAccountsID" name="bankAccounts" style="width: 170px;" onchange="getCheckRef()">
										<option value="0"> -Select- </option>
											<c:forEach var="bankAccounts" items="${requestScope.bankAccounts}">
												<option value="${bankAccounts.moAccountId}">
														<c:out value="${bankAccounts.description}"></c:out>
												</option>
										</c:forEach>
								</select>
							</td>
							</tr>
							<tr>
							<td><label>Reissue Check #</label></td>
							<td>
								<select id="CheckStartingID" name="CheckStartingID" style="width: 170px;" onchange="getEndCheckRef()">
										<option value="0"> -Select- </option>
								</select>
								<label>through</label>
								<select id="CheckEndingID" name="CheckEndingID" style="width: 170px;">
										<option value="0"> -Select- </option>
								</select>
							</td>
						</tr>
						<tr class="checkstr">
							<td colspan="3"><label>Using new Check # Starting with: </label>
							<input type="text" id="checkNumberID" name="checkNumber" value="${requestScope.newReference}"/></td>
						</tr>
						<tr class="checkstr">
							<td colspan="3"><label>Date Checks:</label><input type="text" readonly="readonly" id="checkDateId" name="checksDate"/></td>
						</tr>
						<tr class="checkstr" style="margin-top: 10px; height: 60px;">
							<td colspan="3" style="vertical-align: bottom;">
								<input type="button" value="Print" class="turbo-blue savehoverbutton" onclick="printCheck()" style="margin-left: 10px; font-size: 15px;" />
								<input type="button" value="Select Printer" class="turbo-blue savehoverbutton" onclick="selectPrinter()" style="margin-left: 10px;font-size: 15px; display: none;"/>
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
		
		<div id="LoadingImage" style="position: absolute; left: 0px; top: 0px; text-align: center; width: 450px;display: none;"><img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>
		<div><jsp:include page="../selectPrinter.jsp"></jsp:include></div>
		<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery-ui-1.8.23.custom.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.form.min.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/blockUI/jquery.blockUI.min.js"></script>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				var billExists = $('#billToPayExistsId').val();
				$("#checkDateId").datepicker();
				getcoFiscalPerliodDate("checkDateId");
				/* if(billExists==='true')
				{
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; //January is 0!
					var yyyy = today.getFullYear();
					if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = mm+'/'+dd+'/'+yyyy;
					$("#search").css("display", "none");
					$("#checkDateId").val(today);
					$("#bankAccountsID").val(1);
					$("#bankAccountsID").change(function(){
							$('#CheckStartingID').val($('#bankAccountsID').val());
							$('#checkNumberID').val(($("#CheckStartingID option:selected").text()).trim());
					});
				}else{
					var newDialogDiv = document.createElement("div");
					var errorText = "No Bills are Selected...Please go the vendor pay bills and select the bills to be payed.";
					$(newDialogDiv).attr("id","msgdlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Error",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#writeChecksFromID :input").attr("disabled", true);document.location.href="./vendorbills";return false;}}]}).dialog("open");
				 } */
			/* 	$('#test').click(function() {
					msgOnAjax("error","red");});
				$('#test1').click(function() { 
					msgOnAjax("Success", "green");}); */

				/* var date = new Date();
	     		var month = date.getMonth()+1;
		     	var day = date.getDate();
		     	var year = date.getFullYear();
		     	console.log(month+'/'+day+'/'+year);
     		 	$("#checkDateId").val(month+'/'+day+'/'+year); */

				var today = new Date();
				var dd = today.getDate();
				var mm = today.getMonth()+1; //January is 0!
				var yyyy = today.getFullYear();
				if(dd<10){dd='0'+dd} if(mm<10){mm='0'+mm} today = mm+'/'+dd+'/'+yyyy;
				console.log(today);
				//alert(today);
				$("#checkDateId").val(today);
			});
			function printCheck(){

				/*  $.blockUI({ css: { 
					border: 'none', 
					padding: '15px', 
					backgroundColor: '#000', 
					'-webkit-border-radius': '10px', 
					'-moz-border-radius': '10px', 
					opacity: .5, 
					'z-index':'1010px',
					color: '#fff'
				} }); */
				var startingId = $("#CheckStartingID option:selected" ).text();
				var endingId = $("#CheckEndingID option:selected" ).text();
				 
				var currentDate = new Date();
				var currentMonth = currentDate.getMonth()+1;
				var currenDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
				var currentminute = currentDate.getMilliseconds();
				var filename = "PrintChecks"+currenDate+currentminute+".pdf";
				var newDialogDiv = document.createElement("div");
				var writeChecksDetails = $('#writeChecksFromID').serialize();
				
				var selectedType = $("input[type='radio'][name='checkType']:checked");
				var checkType=selectedType.val();bankAccountsID
				var bankAccounts=$("#bankAccountsID").val();
				var CheckStartingID= $("#CheckStartingID option:selected" ).text();
				var CheckEndingID=$("#CheckEndingID option:selected" ).text();
				var checkNumber=$("#checkNumberID").val();
				var checksDate=$("#checkDateId").val();


				$.ajax({
					url:'./banking/getCheckNumberExists',
					type:"POST",
					data: {checkno:$('#checkNumberID').val()},
					success:function(data){ 
						createtpusage('Reissue Checks','Printing Check','Info','Reissue Checks,Printing Check,Check No:'+$("#checkNumberID").val());
				console.log(data);
				if(data)		
				{
				var newDialogDivreissuecheck = jQuery(document.createElement('div'));
				$(newDialogDivreissuecheck).attr("id","msgdlg");
				var Warnintext = "You are about to Void the selected checks and reissue, are you sure?";
				jQuery(newDialogDivreissuecheck).html('<span><b style="color:blue;">'+Warnintext+'</b></span>');
				jQuery(newDialogDivreissuecheck).dialog({
					modal : true,
					height : 150,
					width : 300,
					title : "Warning",
					buttons : {
						"Submit" : function() {
							var dataUrl = 'moAccountId='+bankAccounts+"&checktype="+checkType+'&checkStartId='+CheckStartingID+'&checkEndId='+CheckEndingID+'&newCheckNo='+checkNumber+'&checksDate='+checksDate;
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
												url:'./banking/rePrintCheck?'+dataUrl+'&coFiscalPeriodId='+periodid+'&coFiscalYearId='+yearid,
												type:"POST",
												success:function(data){
													console.log("firstcheckNumber: "+data.lastCheckNo);
													console.log("lastcheckNumber: "+data.lastCheckNo);
													var CheckStartingID=$("#CheckStartingID option:selected").text();
													var CheckEndingID=$("#CheckEndingID option:selected").text();
													var count= parseInt(CheckEndingID)-parseInt(CheckStartingID);
													var firstcheckNumber=parseInt(checkNumber);
													var lastcheckNumber=parseInt(checkNumber)+parseInt(count);
													window.open("./banking/reIssueCheckDetails?firstcheckNumber="+firstcheckNumber+"&lastcheckNumber="+lastcheckNumber);
													//return false;
													//window.open("./banking/rePrintCheckDetails?fileName="+filename+"&lastNo="+data.lastCheckNo+"&"+dataUrl);
													//ssetTimeout($.unblockUI, 2000);
													
											 	},
											 	complete:function(data)
											 	{
											 		document.getElementById("writeChecksFromID").reset();
													jQuery(newDialogDivreissuecheck).dialog("close");

												},
												error:function(data){
													var Warnintext = "Printing Checks is not Possible Now. Try a a little Later..!!";
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

							
						},
						"Cancel" : function() {
							//$('#writeChecksFromID').reset();
							document.getElementById("bankAccountsID").selectedIndex = 0;
							document.getElementById("CheckStartingID").selectedIndex = 0;
							document.getElementById("CheckEndingID").selectedIndex = 0;
							jQuery(this).dialog("close");
						}
					}
					}).dialog("open");
			
					}
					else
						{
						console.log("Enter");

						var newDialogDiv = document.createElement("div");						
						var Warnintext = "This Check Number Already Used.";
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
		                 color: msgcolor
		                // "border-radius":"15px 15px 15px 15px"
		             } 	
		         }); 
			}

			function selectPrint()
			{
				$('#selectPreiners').dialog({height:200});
				$('#selectPreiners').dialog("open");
				
			}
			function selectPrinter(){
				$("#selectPreiners").css("display", "block");
				var printers;
				$.ajax({
					//url: "http://192.168.43.30:3333/qb/getponumber/1",
					url: './banking/selectprinter',
					type: "POST",
					data: printers,
					success: function(data) {
						selectPrint();
						var printerList;
						var valus="<option value='0'>-Select-</option>";
						printerList = data.split(",");
						for(i=0;i<printerList.length;i++){
							valus+='<option value='+i+'>'+printerList[i]+'</option>';
						}
						$('#printerId').html(valus);
					},
					error: function(data){
						msgOnAjax("this feature is under our workshop. Thanks..", "red");	
					}
					});
			}
			var checkRefs="";
			var selectedVal = "";
			var selected = $("input[type='radio'][name='checkType']:checked");
			if (selected.length > 0) {
			    selectedVal = selected.val();
			}
			function getCheckRef(){				
				//$("#LoadingImage").show();
				 $.blockUI({ css: { 
					border: 'none', 
					padding: '15px', 
					backgroundColor: '#000', 
					'-webkit-border-radius': '10px', 
					'-moz-border-radius': '10px', 
					opacity: .5, 
					'z-index':'1010px',
					color: '#fff'
				} });  
				$.ajax({
			        url: './getChecksRefs?moAccountId='+$("#bankAccountsID").val()+"&checktype="+selectedVal,
			        type: 'POST',       
			        success: function (data) {
			        	console.log("all--->"+data);
			        	checkRefs="<option value='0'>-Select-</option>";
						$.each(data, function(key, valueMap) {								
								if("checkList" == key)
								{
									
									$.each(valueMap, function(index, value){
										console.log('You r right');
										if(value.moAccountId != '')
											checkRefs+='<option value='+value.moAccountId+'>'+value.reference+'</option>';
									
									}); 
								}
								if("referenceKey" == key)
								{
									$.each(valueMap, function(index, value){
										console.log('New Key');
										$('#checkNumberID').val(value);
									}); 
								}
								
						});
						//$("#LoadingImage").hide();
						$('#CheckStartingID').html(checkRefs);
						setTimeout($.unblockUI, 500);
			        }
			    });
			}
			var endCheckRefs="";
			function getEndCheckRef(){
				var reference = $("#CheckStartingID option:selected" ).text();
				//$("#LoadingImage").show();
				$.blockUI({ css: { 
					border: 'none', 
					padding: '15px', 
					backgroundColor: '#000', 
					'-webkit-border-radius': '10px', 
					'-moz-border-radius': '10px', 
					opacity: .5, 
					'z-index':'1010px',
					color: '#fff'
				} });
				$.ajax({
			        url: './getChecksRefs?moAccountId='+$("#bankAccountsID").val()+'&checktype='+selectedVal+'&reference='+reference,
			        type: 'POST',       
			        success: function (data) {
			        	console.log("all--->"+data);
			        	endCheckRefs="<option value='0'>-Select-</option>";
						$.each(data, function(key, valueMap) {								
								if("checkList" == key)
								{
									$.each(valueMap, function(index, value){
										if(value.reference != null && value.reference.trim() != '')
											endCheckRefs+='<option value='+value.moAccountId+'>'+value.reference+'</option>';
									
									}); 
								} 
								if("referenceKey" == key)
								{
									$.each(valueMap, function(index, value){
										console.log('New Key');
										$('#checkNumberID').val(value);
									}); 
								}
						});
						// $("#LoadingImage").hide();
						$('#CheckEndingID').html(endCheckRefs);
						setTimeout($.unblockUI, 500);
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