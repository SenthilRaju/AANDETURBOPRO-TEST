<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Credit/Debit Memos</title>
		<style type="text/css">
			#mainmenuBankingPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuBankingPage a{background:url('./../resources/styles/turbo-css/images/home_white_banking.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuBankingPage ul li a{background: none; }
			input[type="text"] {padding-left:5px;}
		</style>
		
		
		
	</head>
	<body style="background-color: #FAFAFA;margin: 0 auto;">
		<div style="margin: 0 auto;">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include>
				
			</div>
			<div   style="margin: 0 auto;margin-left: 220px; margin-top: 30px;">
			<div style="margin-left: 820px"><input type="button" class="cancelhoverbutton turbo-tan" value="Add" style="width:80px;margin-bottom:10px;" onclick="createNewcreditdebitmemo()"/></div>
				<table id="creditDebitMemosGrid"></table>
						<div id="creditDebitMemosGridPager"></div>
					
			</div>
			
			<div id="addcreditDebitMemosDlg" style="width:736px; display:none">
			
			<div id="headingview" style="width:380px;margin:0 auto;"><h1 style="margin-left:50px;">Credit/Debit Memos</h1>
			
			</div>
			<table style="width:100%; margin:0 auto;  color:#00377A;">
			<tr>
			<td style="position: absolute;">
			<form name="creditdebitmemoform" id="creditdebitmemoformID">
			<input type="hidden" id="taxfreight" name="taxfreight" value=0 />
			<fieldset class= "custom_fieldset" style="height: 440px;margin:0 auto; margin-left:20px; width: 600px;color:#00377A;">
			<legend class="custom_legend"><label><b></b></label></legend>
			<table style="padding-top:20px;">
			<tr>
			<td>Date:<input type='text'  style='position:relative; top:-500px;width:0px;' /></td>			
			<td><input type="text" id="datepickerbox" name="invoiceDate" readonly="readonly" /></td>
			<td width="100;">Memo:</td>
			<td><input type="text" id="memoID" name ="memoName" readonly="readonly" /> 
			<input type="hidden" id="invoiceID" name="invoiceID" />
			<input type="hidden" id="cuinvoiceInvoiceID" name="cuinvoiceInvoiceID" /></td>
			</tr>
			<tr>
			<td>Customer:<span style="color:red;size:13px">*</span></td>			
			<td><input type="text" id="customerID" name="customerName" style="width:178px;"/>&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="" style="size:10px;" onclick="">
			<input type="text" style="display: none;" id="JobCustomerId" name="customerIDName"></td>
			</td>
			</tr>
			<tr>
			<td>MemoType:<span style="color:red;size:13px">*</span></td>			
			<td><select id="memotypeID" name="memotypeName" style="width: 80px;">
										<option value="-1"> -Select- </option>
										<option value="1"> Credit </option>
										<option value="0"> Debit </option>
									</select></td>
			</tr>
			<tr>
			<td>Description:</td>			
			<td><input type="text" class="alpha" id="DescriptionID" name="DescriptionName" style="padding-left:4px; width:209px;"/>
			
			</td>
			</tr>
			<tr>
			<td>Note:</td>			
			<td><textarea id="notestextareaID" name="notestextareaName" class="alpha" rows="4" cols="20" style="padding-left:5px;width:205px;max-width:235px;max-height:60px;"></textarea></td>
			</tr>
			<tr>
			<td>Amount:</td>			
			<td><input type="text" id="amountID"  name="amountName" style="text-align:right; padding-right:5px" value="$0.00"/></td>
			<td><span style="white-space: nowrap;">Customer PO#:</span></td>
			<td><input type="text" id="customerpoID" name="customerpoName"  style="text-align:right; padding-right:5px;" /></td>
			</tr>
			<tr>
			<td>Freight:</td>			
			<td><input type="text" id="frieghtID" name="frieghtName" style="text-align:right;padding-right:5px" value="$0.00" /></td>
			<td>GL Account:<span style="color:red;size:13px">*</span></td>
			<td>
				<select id="glAccountsID" name="glAccountsName" style="width:175px;">
					<option value="-1"> - Select - </option>
							<c:forEach var="glaccount" items="${requestScope.glaccount}">
								<option value="${glaccount.coAccountId}">
									<c:out value="${glaccount.number}  - ${glaccount.description}" ></c:out>
								</option>
							</c:forEach>
					</select>					
									
									
									
			</td>
			</tr>
			<tr>
			<td><span id="taxrateID" >0</span><input type="hidden" id="tax_Rate" name="tax_RateName" />% Tax:</td>			
			<td><input type="text" id="taxAmountID" name="taxAmountName" readonly="readonly" style="text-align:right;padding-right:5px" value="$0.00" /></td>
			<td>Salesman:<span style="color:red;size:13px">*</span></td>
			<td><select id="salesmanID" name="salesmanName" style="width:175px;">
					<option value="-1"> - Select - </option>
							<c:forEach var="salesman" items="${requestScope.salesman}">
								<option value="${salesman.userLoginId}">
									<c:out value="${salesman.fullName}" ></c:out>
								</option>
							</c:forEach>
					</select>	
					</td>
			</tr>
			<td>Grand Total:</td>			
			<td><input type="text" id="grandtotalID" name="grandtotalName" readonly="readonly" style="text-align:right;padding-right:5px;" value="$0.00" /></td>
			<td>Division:<span style="color:red;size:13px">*</span></td>
			<td><select id="divisonID" name="divisonName" style="width:175px;">
					<option value="-1"> - Select - </option>
							<c:forEach var="division" items="${requestScope.division}">
								<option value="${division.coDivisionId}">
									<c:out value="${division.description}" ></c:out>
								</option>
							</c:forEach>
					</select>
					
					</td>
			</tr>
			<tr>
			<td>Taxable:</td>			
			<td><input type="radio" id ="checkyesID" name="taxabe" disabled value="yes"/><label>Yes</label>
			<input type="radio"  id ="checknoID" name="taxabe" value="no" checked /><label>No</label>
			</td>
			
			</tr>
			<tr>
			<td><span style="white-space: nowrap;">Tax Territory:</span></td>		
			<td><select id="taxterritoryID" name="taxterritoryName" style="width:175px;">
					<option value="-1"> - Select - </option>
							<c:forEach var="taxterritory" items="${requestScope.taxterritory}">
								<option value="${taxterritory.coTaxTerritoryId}" data-value="${taxterritory.taxRate}" data-taxfreight="${taxterritory.taxfreight}">
									<c:out value="${taxterritory.county} ${taxterritory.state}" ></c:out>
								</option>
							</c:forEach>
					</select>
			</td>
			
			</tr>
			<tr>
			<td  style="width:150px;"><!-- <input type="button" class="cancelhoverbutton turbo-tan" value="Add" style="width:150px;"/> --></td>			
			<td><!-- <input type="button" class="cancelhoverbutton turbo-tan" value="Save" style="width:100px;margin-top:20px;" onclick="saveMemoDetails()"/> -->
			<div style="margin-top:10px;">
			
		    	<table>
		    		<tbody><tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Credit/Debit Memo" style="background: #EEDEBC;" onclick="pdfprint()"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="" type="image" src="./../resources/Icons/mail_new.png" title="Email Credit/Debit Memo" style="background: #EEDEBC"> <!-- onclick="sendMemoEmail()" --></td>
				  		<td><input type="button" class="cancelhoverbutton turbo-tan" value="Save & Close" id="savememo"style="width:100px;vertical-align: 9px;" onclick="saveMemoDetails()"/>
				  		<input type="button" class="cancelhoverbutton turbo-tan" value="Save & Close" id="editmemo" style="width:100px;vertical-align: 9px;display:none" onclick="editMemoDetails()"/></td>
		    		</tr>
		    	</tbody></table>
	   		</div>
			</td>
			<td colspan="2">
				<div id="creditdebiterrorstatus" style="color:red"></div>
			<td>
			
			</tr>
			</table>			
			</fieldset>
			</form>
			</td>
			</tr>
			
			
			</table>
			</div>
			
			
			<div style="padding-top: 22px;padding-top: 22px;position: absolute;top: 120%;width: 100%;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		
<!-- 		<script type="text/javascript" src="./resources/scripts/turbo_scripts/bankingAccounts.js"></script> -->
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/creditDebitMemo.js"></script>
		<script>

		
			$("#datepickerbox").datepicker();
			$("#grandtotalID").val("0");
			
			getcoFiscalPerliodDate("datepickerbox");

			function getcoFiscalPerliodDate(x)
			 {
				 $.ajax({
					 
				 		url: "./banking/getcoFiscalPeriod",
				 		type: "GET",
				 		success: function(data) {
				 			$("#"+x).datepicker("option","minDate",new Date(data.startDate));
				 			$("#"+x).datepicker("setDate",new Date());		
				 		}		 
				 });	 
			 }

			 function pdfprint()
			 {

					var currentDate = new Date();
					var currentMonth = currentDate.getMonth()+1;
					var currenDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
					var currentminute = currentDate.getMilliseconds();
					var filename = "Memo"+currenDate+currentminute+".pdf";

					window.open("./creditdebitmemo/memoPdfPrint?filename="+filename+"&invoiceID="+$("#invoiceID").val()+"&memotype="+$("#memotypeID").val());
					
			 }

			 function sendMemoEmail(){
					var aQuotePDF = "purchase";
					var invoiceID = $("#invoiceID").val();
					var errorText = '';
					
					if(invoiceID == ""){
						errorText = "Please Save Memo Details and Send Email.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
					}
					else{
						$.ajax({ 
							url: "./vendorscontroller/GetContactDetails",
							mType: "GET",
							data : { 'rxContactID' : aContactID},
							success: function(data){
								var aFirstname = data.firstName;
								var aLastname = data.lastName;
								var aEmail = data.email;
								var arxContact = aFirstname + ' '+aLastname;
								
								if(aEmail === ''){
									errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
								}else{
									errorText = "Are you sure you want to send this PO to"+ arxContact +"("+ aEmail +")?";
								}
								jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
								buttons:{
									"Send": function(){
										$('#loadingPODiv').css({"visibility": "visible"});
										viewPOPDF(aQuotePDF);
										sentMailPOFunction(aContactID,aEmail, cusotmerPONumber, vePoId);
										jQuery(this).dialog("close");
									},
									Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
								
							}
						});
						}
					return true;
				
				}

			 
		</script>
		
		</div>
	</body>
</html>