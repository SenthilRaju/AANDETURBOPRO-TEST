<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Jobs</title>
<style type="text/css">
#mainMenuJobsPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #54A4DE;
}

#mainMenuJobsPage a span {
	color: #FFF
}
#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuInventoryPage ul li a{background: none; }
</style>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/recieveInventory.js"></script>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<div style="width:800px; margin:0 auto;">
		<table id="RecieveInventoryTable"
			style="width: 979px; margin: 0 auto;">
			<tr>
				<td align="center">
					<p><center>Refine your search by entering criteria below and click the<br>
						<center>'Locate' button below. Or if you wish to add an item <br>,click the
						'New' button. Begin a search with % to act as a wild card
					<p>
				</td>
			</tr>
			<tr>
			<td>Receive Date:
			<input type="checkbox" id="receiveInventoryRange" name="receiveInventoryRange" value="Range">
			 Range <label id="rangefromlabel" >From :</label> 
			 <input type="text" id="rangefromid" /> <label id="rangetolabel" >To:
			 </label><input type="text" id="rangetoid" />
			
			</td>
			</tr>
		    <tr><td><select class="chzn-select" name="bankAccounts" style="width: 300px;">
										<option value="0"> -Select- </option>
											<c:forEach var="bankAccounts" items="${bankAccounts}">
												<option value="${bankAccounts.veMasterId}">
														<c:out value="${bankAccounts.manufacturer}"></c:out>
												</option>
										</c:forEach>
								</select>
								</td></tr>
			<tr>
			<td>
				<fieldset class= " ui-widget-content ui-corner-all">
					<legend><label class="fontclass"><b>Sort by</b></label></legend>
				<table>
				  
				<tr>
					<td>
					<select name="recieveInventorySort" id="recieveInventorySort" size=0>
					<option name="select" value="0" > - Select - </option>
					<option name="Receive Date" value="date" >Receive Date</option>
					<option name="Received From" value="from" >Received from</option>
					</select>
					
					</td>
					</tr>
					<tr><td>(Make Default)</td></tr>
					</table>
					</fieldset>
					</td>
					
					</tr>
					<tr>
					<td>
					<input type="button" value="New" class="savehoverbutton turbo-blue" onclick="locateReceiveInventory()" style="width:100px;margin-left: 10px;" />
						<input type="button"  value="Locate" class="savehoverbutton turbo-blue" onclick="showReceivedInventory()" style="width:100px;" />
					</td>
					</tr>
					</table>
					</div>
		<div style="padding-top: 8%;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<%-- 		<div><jsp:include page="../salesOrder.jsp"></jsp:include></div> --%>

<script>
jQuery(document).ready(function(){
	
	jQuery(".chzn-select").chosen();
	$('#rangefromid').val('');
	$('#rangetoid').val('');
	$('#receiveInventoryRange').prop('checked', false);
});
$('#rangefromlabel').hide();
$('#rangetolabel').hide();
$('#rangefromid').hide();
$('#rangetoid').hide();

$('#rangefromid').datepicker();
$('#rangetoid').datepicker();
$('#receiveInventoryRange').click(function() {
    var $this = $(this);
       
    if ($this.is(':checked')) {
    	
    	$('#rangefromlabel').show();
    	$('#rangetolabel').show();
    	$('#rangefromid').show();
    	$('#rangetoid').show();
    	
    } else {
    	$('#rangefromlabel').hide();
    	$('#rangetolabel').hide();
    	$('#rangefromid').hide();
    	$('#rangetoid').hide();
    }
});

function showReceivedInventory(){
	var selectedacc = $('.chzn-select').val();
	var sortby = $('#recieveInventorySort').val();
	var range=$('#rangefromid').val();
	var raangeto = $('#rangetoid').val();
	
	if(range!=''){
		range = range+'to';
		if(raangeto==''){
			alert("Please select 'to' date");
		}else{
			
			document.location.href="./showReceivedInventoryList?vendorID="+selectedacc+"&sortBy="+sortby+"&rangeFrom="+range+raangeto;		
		}
	}else{
		range = range+'to';
		document.location.href="./showReceivedInventoryList?vendorID="+selectedacc+"&sortBy="+sortby+"&rangeFrom="+range+raangeto;
	}
	//document.location.href="./showReceivedInventoryList?vendorID="+selectedacc+"&sortBy="+sortby+"&rangeFrom="+range+raangeto;
	
	//alert('selectedacc'+selectedacc);
}

</script>

</body>
</html>