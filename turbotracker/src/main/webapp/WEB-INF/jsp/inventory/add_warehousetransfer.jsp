<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
</style>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<div style="width:86%; margin:0 auto;">
		<form id="WarehouseTransferForm" method="post">
		<fieldset class= "ui-widget-content ui-corner-all" style="height: 140px;width: 1000px;">
		  <legend class="custom_legend"><b>Warehouse Transfer Shipping</b></legend>
			<table>
				<tr>
					<!-- <td>
					<fieldset class= "ui-widget-content ui-corner-all" style="height: 90px;width: 340px;">
									<legend class="custom_legend"><b>Warehouse Transfer Shipping</b></legend>
								<table style="width: 400px">
									<tr> -->	
									<td style="width: 120px;"><label>Transfer Date<span style="color: red;">*</span>:</label></td>
										<td><input type="text" class="datepicker" id="transferDateId"  readonly="readonly"name="transferDateId" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${prWareHousetransfer.transferDate}" />" style="width: 140px;"></td>
									<!-- </tr>							
								</table>
							</fieldset> -->
					</td>
					<td><label>From<span style="color: red;">*</span>:</label></td>
					<td>
					<select style="width:225px;" id="warehouseFrom" name="warehouseFrom"  onchange="getToWarehouse();"><option value="-1">- Select -</option>
												<c:forEach var="prWarehouse" items="${prWareHouseDetails}">
														<option value="${prWarehouse.prWarehouseId}">${prWarehouse.city}</option>
												</c:forEach>
											</select>
					</td>
				</tr>
				<tr>
					<td style="width: 120px;"><label>Est.R'cvd Date<span style="color: red;">*</span>:</label></td>
					<td><input type="text" class="datepicker" id="estDateId"  readonly="readonly" name="estDateId" style="width: 140px;" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${prWareHousetransfer.estreceiveDate}" />"></td>
					<td><label>To<span style="color: red;">*</span>:</label></td>
					<td>
					<select style="width:225px;" id="warehouseTo" name="warehouseTo"><option value="-1">- Select -</option>
												<c:forEach var="prWarehouse" items="${prWareHouseDetails}">
														<option value="${prWarehouse.prWarehouseId}">${prWarehouse.city}</option>
												</c:forEach>
											</select>
					</td>
				</tr>
				<tr>
					<td style="width: 120px;"><label>Received Date:</label></td>
					<td><input type="text" class="datepicker" id="recDateId" readonly="readonly" name="recDateId" style="width: 140px;" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${prWareHousetransfer.receivedDate}" />" disabled="disabled"></td>
					<td><label>Transaction Number<span style="color: red;">*</span>:</label></td>
					<td>
					<input type="text" id="transNo" name="transNo" value="${transactionNumber}" style="width: 223px;" readonly="readonly">
					</td>
				</tr>
				<tr>
				<td><label>Reference<span style="color: red;">*</span>:</label></td>
				<td>
					<input type="text" id="ref" name="ref" style="width: 390px;" value="${prWareHousetransfer.desc}">
					</td>
				</tr>
			</table>
			<input type="hidden" id="prTransferId" name="prTransferId">
			<input type="hidden" id="prMasterId" name="prMasterId">
			<input type="hidden" id="prTransferDetailId" name="prTransferDetailId">
			</fieldset>
			<br/><br/><div id="showErrorMsg"></div><br/>
			<fieldset class= "ui-widget-content ui-corner-all" style="height: 283px;width: 1000px;">
		  <legend class="custom_legend"><b>Warehouse Transfer Shipping Items</b></legend>
		  <div id="jqgridLine">
			<table id="addtransferGrid"></table>
			<div id="addtransferGridPager"></div>
		</div>
		  </fieldset>
		  <table>
		  <tr><td><div id="showMessageReceive" style="color: green;margin-left: 540%;margin-bottom: 0%;width: 112%"></div></td></tr>
				<tr>	
				<td><input type="button" id="WarehouseTransferSaveID" class="cancelhoverbutton turbo-tan" value="Save" onclick="copyTransfer()" style="width:80px;position: relative;"></td>			
				<td><input type="button" id="WarehouseTransferID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="saveTransfer()" style="width:120px;position: relative;margin-left: 680px;"></td>
				<td><input type="button" id="WarehouseTransferReceiveID" class="cancelhoverbutton turbo-tan" value="Receive items" onclick="receiveitems()" style="width:120px;position: relative;margin-left: 0px;"></td>
				</tr>
				</table>
		</form>
	</div>
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
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/add_warehousetransfer.js"></script>

<script type="text/javascript">

$('#warehouseFrom').val("${prWareHousetransfer.prFromWarehouseId}");
$('#warehouseTo').val("${prWareHousetransfer.prToWarehouseId}");
$('#prTransferId').val("${prWareHousetransfer.prTransferId}");

/* getcoFiscalPerliodDate("recDateId");
getcoFiscalPerliodDate("transferDateId");
getcoFiscalPerliodDate("estDateId");
 */
/*  var revDate=$("#recDateId").val();
 var transfDate=$("#transferDateId").val();
 var estDate=$("#estDateId").val();
 getcoFiscalPerliodDate(revDate);
 getcoFiscalPerliodDate(transfDate);
 getcoFiscalPerliodDate(estDate);  */

var oper = "${operation}";
if("copy" == oper)
	{
		$('#WarehouseTransferID').hide();
		$('#WarehouseTransferReceiveID').hide();
		$('#recDateId').val('');
	//FormatDates("${currentDate}");
	}
else
	{
	$('#WarehouseTransferSaveID').hide();
	}

function FormatDates(createdDate){
	/*var date = new Date(createdDate);
	var CreatedOn = date.getDate();
	var createdMonth = date.getMonth()+1; 
	var createdYear = date.getFullYear();
	if(CreatedOn<10){CreatedOn='0'+CreatedOn;} 
	if(createdMonth<10){createdMonth='0'+createdMonth;} 
	createdDate = createdMonth+"/"+CreatedOn+"/"+createdYear;*/
	var d = new Date(createdDate);
    var day = d.getDate();
    var month = d.getMonth() + 1;
    var year = d.getFullYear();
    if (month < 10) {
        month = "0" + month;
    }
    createdDate = year + "/" + month + "/" + day;
	/* createdDate = createdDate.split('/')
	if(createdDate[0]<10){createdDate[0]='0'+createdDate[0];}
	if(createdDate[1]<10){createdDate[1]='0'+createdDate[1];} 
	createdDate = createdDate[0]+"/"+createdDate[1]+"/"+createdDate[2]; */
	$("#recDateId").val(createdDate);
	$("#transferDateId").val(createdDate);
	$("#estDateId").val(createdDate);
	
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