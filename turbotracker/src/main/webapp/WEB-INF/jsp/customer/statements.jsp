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
	.loadingDivBulkEmailAttachment{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 127%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}
	
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
	<form id="statementsForm" method="POST" action = "./customerList/printCustomerStatement" target="_blank" style="width:400px;margin-left: 23%;padding-bottom: 55px">
	<fieldset class= " ui-widget-content ui-corner-all">
	<table>
	<input type="hidden" name="rxMasterIds" id="rxMasterIds" value=""/>
		<tr>
		<td style="width: 100%;">
			<fieldset class= " ui-widget-content ui-corner-all">
			<legend><label class="labelfont"><b>Starting Customer</b></label></legend>
				<table>
					<tr>
						<td><label class="labelfont">Starting Customer:</label></td>
							<td>
								<select style="width:200px;" id="stCustomerID" name="stCustomerID">
									<%-- <option value="<c:out value='${requestScope.customers[0].rxMasterId}' ></c:out>"> - Select - </option> --%>
									<option value="-1"> - Select - </option>
										<c:forEach var="customers" items="${requestScope.customers}">
										
											<option value="${customers.rxMasterId}">
													<c:out value="${customers.name} ${customers.firstName}" ></c:out>
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
								<select style="width:200px;" id="endCustomerID" name="endCustomerID" disabled >
									 <%-- <option value="<c:out value='${requestScope.customers[requestScope.customers.size()-1].rxMasterId}' ></c:out>"> - Select - </option>  --%>
									<option value="0"> - Select - </option>
										<c:forEach var="customers" items="${requestScope.customers}">
											<option value="${customers.rxMasterId}">
													<c:out value="${customers.name} ${customers.firstName}" ></c:out>
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
			<td>
				<input type="button" value="Email" class="savehoverbutton turbo-blue" onclick="sendBulkEmail()" style="height: 27px !important;width: 50px; display:none"  />
				<input type="button" value="Print" class="savehoverbutton turbo-blue" onclick="printStatements()" style="height: 27px !important;width: 50px"  />
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
	
		<div id="emailDetailsDialog">
	
	<div class="loadingDivBulkEmailAttachment" id="loadingDivForBulkEmail" style="display: none; opacity: 0.7; z-index: 1234; text-align: center; background-color: rgb(255, 255, 255);"></div>
		     
			<table id="cStatementEmailGrid"></table>
			<div id="cStatementEmailGridPager"></div>
    <br>
    <input type="button"  id="selectCheckBox" value="Select All" class="turbo-blue savehoverbutton" onclick="selectAllCheckBox()" style="margin-left:50px; font-size: 15px;">
	<input type="button" value="Cancel" class="turbo-blue savehoverbutton" onclick="cancelEmail()" style="margin-left:700px; font-size: 15px;">
	<input type="button" value="Email" class="turbo-blue savehoverbutton" onclick="sendEmail()" style="margin-left: 30px;font-size: 15px;">
		<br>	
	</div>
	
	
	
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
	<script>
	
	function printStatements() {
		
		/*Changed by: Leo   Date:09-05-2015  
		* Description: One customer statement only allow until performance tuning will made.
		* Start==================
		*/
			endIndex = stIndex;
		/* End================== */
 		var startingid = $('#stCustomerID').val();
		var endingid = $('#endCustomerID').val();
		/* if(startingid=="-1" ||  endingid=="0"){
			var newDialogDiv2 = jQuery(document.createElement('div'));
			var errorText = "Please select the customer";
			jQuery(newDialogDiv2).html('<span><b style="color:red;">'+ errorText+ '</b></span>');
			jQuery(newDialogDiv2).dialog({modal : true, width : 350, height : 170, title : "Warning", 
				buttons : [ {
									height : 35,
									text : "OK",
									click : function() {
										$(this).dialog("close");
										return false;
							}
						}]
							}).dialog("open");
			return false;
			}  */

		createtpusage('Company-Customer-Statement','Print Customer Statement','Info','Company-Customers-Statement,View PDF,startingid:'+startingid+',endingid:'+endingid);
		
		var rxMasterIds = [];
		for(var i =parseInt(stIndex);i<=parseInt(endIndex);i++)
		{
			if($("#stCustomerID option").eq(parseInt(i)).val()!==''){
				rxMasterIds.push($("#stCustomerID option").eq(parseInt(i)).val()); 
			}
		}
		var daterange = $('#exclusionDate').val();
		var date = new Date($('#exclusionDate').val());
		var isCredit=0;

		if($('#warehouseInactive').is(":checked"))
			{
			isCredit =1;
			}
		console.log("isCredit::"+isCredit)
		if(Number(startingid)>Number(endingid)){
			var temp =startingid;
			startingid =endingid;
			endingid=temp;
		}
		$("#rxMasterIds").val(rxMasterIds.join());
		
		<%--
		/* window.open('./customerList/printCustomerStatement?startingId='
				+ startingid + '&endingid=' + endingid + '&exclusiondate='
				+ daterange+'&isCredit='+isCredit); */
		
	/* 	window.open('./customerList/printCustomerStatement?startingId='
				+ startingid + '&endingid=' + endingid + '&exclusiondate='
				+ daterange+'&isCredit='+isCredit);

		 */

		 /* window.open('./customerList/printCustomerStatement?stCustomer='+ $('#stCustomerID option:selected').text()+'&endCustomer='+ $('#endCustomerID option:selected').text()+'&exclusiondate='+ daterange+'&isCredit='+isCredit); */
					
					

 /* $.ajax({
     type: "POST", 
     url: "./customerList/printCustomerStatement",
     data:{"rxMasterIDs":rxMasterIds,"exclusiondate":daterange,"isCredit":isCredit},
     dataType: "json",
     success: function(data){
         var win = window.open();
         win.document.write(data);
     }
 })   */
		/*
		 * $.ajax({ url: "./customerList/printCustomerStatement", type: "GET", data :
		 * 'startingId='+startingid+'&endingid='+endingid+'&exclusiondate='+daterange,
		 * success: function(data) { }, error: function(data){ console.log(data); }
		 * });
		 */

		/*
		 * return false; var statementDetails = $('#statementsForm').serialize();
		 * var msg ='There are no statements for this criteria.'; var statementDate =
		 * $('#statementDate').val(); $.ajax({ url:
		 * "./customerList/printStatementConfirmation", type: "GET", data :
		 * statementDetails+'&startingCustomerName='+stCustomer+'&endingCustomerName='+endCustomer+'&style=inline',
		 * success: function(data) { console.log(data); if(data == 0){ msg ='There
		 * are no statements for this criteria.'; jQuery(newDialogDiv).html('<span
		 * style="color:red;">'+msg+'</b></span>');
		 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:120,
		 * title:"Information.", buttons: [{height:35,text: "OK",click: function() {
		 * $(this).dialog("close"); }}]}).dialog("open"); }else{ msg ='Do you want
		 * to preview the statements.?'; jQuery(newDialogDiv).attr("id","msgDlg");
		 * jQuery(newDialogDiv).html('<span><b style="color:red;">'+msg+'</b></span>');
		 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
		 * title:"Information.", buttons:{ "Yes": function(){
		 * jQuery(this).dialog("close"); $.blockUI({ css: { message: "Please Wait
		 * for the preview.", border: 'none', padding: '15px', backgroundColor:
		 * '#000', '-webkit-border-radius': '10px', '-moz-border-radius': '10px',
		 * opacity: .5, 'z-index':'1010px', color: 'aqua' } }); if(data.length>0){
		 * var table = '<table style="border:1px solid;font-weight: normal
		 * !important;background-color:#FAFAFA;"><tr style="background-color:#485E70;color:#FAFAFA;font-size:.9em;"><td style="border: solid 1px;">Customer
		 * Name</td><td style="border: solid 1px;">Preview</td><td style="border: solid 1px;">Email</td ></tr>';
		 * for(var i=0;i<data.length;i++){ var url
		 * ='./customerList/printStatement?filename='+data[i].rxMasterID+'_statements.pdf&rxMasterID='+data[i].rxMasterID+'&statementDate='+statementDate;
		 * table = table+'<tr style="height:31px;font-size:.8em;">' +'<td style="width: 65%; border-right: 1px solid;">'+data[i].name+'</td>' +'<td style=" border-right: 1px solid;"><a
		 * href='+url+' target="_blank">' +'<input type="button" value="Preview"
		 * class="savehoverbutton turbo-blue" style="height:26px !important;"/>' +'</a></td>' +'<td>' +'<input
		 * type="button" value="Email" class="savehoverbutton turbo-blue"
		 * onclick="emailStatement(this)"
		 * id="'+data[i].email+'rxMasterID='+data[i].rxMasterID+'"
		 * style="height:26px !important;"/>' +'</td>' /*+'<td style="display:hidden;">' +'<input
		 * type="text" value='+data[i].email+' id ="'+i+'"/>' +'</td>' +'</tr>'; }
		 * table = table+'</table>'; setTimeout($.unblockUI, 2000);
		 * $('#showStatements').empty(); $('#showStatements').append(table); }
		 * setTimeout($.unblockUI, 2000); }, "No": function () {
		 * jQuery(this).dialog("close"); } }}).dialog("open"); } }, error:
		 * function(data){ console.log(data); } });
		 */
		--%>
		$('form#statementsForm').submit();
	}

//for All Customer 
function printStatementsforAllCustomer() {
	
			endIndex = stIndex;
		var startingid = $('#stCustomerID').val();
		var endingid = $('#endCustomerID').val();
		
		createtpusage('Company-Customer-Statement','Print Customer Statement','Info','Company-Customers-Statement,View PDF,startingid:'+startingid+',endingid:'+endingid);
		
		var daterange = $('#exclusionDate').val();
		var date = new Date($('#exclusionDate').val());
		var isCredit=0;

		if($('#warehouseInactive').is(":checked"))
			{
			isCredit =1;
			}
		$("#rxMasterIds").val("-1");
		
		$('form#statementsForm').submit();
	}
	</script>
</html>