<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Jobs</title>
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
			
		</style> 
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
				<td><input type="checkbox" name="dateRange" id="dateRange" onclick="callEnableDate();"/><span style="vertical-align:5px;">Date Range: <input type="text" name="fromDate" id="fromDate" class="datepicker"/>&nbsp;Thru<input type="text" name="toDate" id="toDate" class="datepicker"/></span></td>
					<td align="right">
						<div>
							<input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addNewSO()"/>&nbsp;
							<input  type="hidden" id="operation" name="operation"/>&nbsp;
							<input  type="hidden" id="pjcusoID" name="pjcusoID"/>&nbsp;	
							<input  type="hidden" id="rxCustomer_ID" name="rxCustomer_ID"/>&nbsp;	
							<input  type="hidden" id="pdfPrice" name="pdfPrice"/>&nbsp;
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table id="SalesOrdersGrid"></table>
						<div id="SalesOrdersGridPager"></div>
					</td>
				</tr>
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
		<div><jsp:include page="../salesOrder.jsp"></jsp:include></div>
		
	<div style="display: none;">
	<input type="text" id="CustomerCategory1hidden" name="CustomerCategory1hidden" value="${requestScope.CustomerCategory1}">
	<input type="text" id="CustomerCategory2hidden" name="CustomerCategory2hidden" value="${requestScope.CustomerCategory2}">
	<input type="text" id="CustomerCategory3hidden" name="CustomerCategory3hidden" value="${requestScope.CustomerCategory3}">
	<input type="text" id="CustomerCategory4hidden" name="CustomerCategory4hidden" value="${requestScope.CustomerCategory4}">
	<input type="text" id="CustomerCategory5hidden" name="CustomerCategory5hidden" value="${requestScope.CustomerCategory5}">
	</div>
		<script type="text/javascript">
		var oper = "${requestScope.operation}";
		$('#operation').val(oper);

		if(oper == "projectSO")
		{
		$('#pjcusoID').val("${requestScope.pjcusoid}");
		$('#rxCustomer_ID').val("${requestScope.pjcustomerid}");
		}

		$(function() { var cache = {}; var lastXhr='';
		$("#searchJob").autocomplete({ minLength: 1,timeout :1000,
			/*open: function(){ 
				$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
				$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			},*/
		select: function (event, ui) {
			var aValue = ui.item.value;
			var aText = ui.item.label;
			var valuesArray = new Array();
			valuesArray = aValue.split("|");
			
			
			var aQryStr = "cuSOID=" + aValue;
			//var acuSoid = rowData['vePOID'];
			//var aMasterID = rowData['rxVendorID'];
			//$('#rxCustomer_ID').text(aMasterID);
			$('#Cuso_ID').text(aValue);
			$('#operation').val('update');
			PreloadSOGeneralData(aValue);
			$('#salesrelease').dialog("open");
			//document.location.href = "./editpurchaseorder?token=view&" + aQryStr;
			
		},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchSONumber", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} });
		});
		</script>
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/salesOrderList.js"></script>		
		
	</body>
</html>
<script type="text/javascript">
$('#salesrelease').dialog("close");
function ResetDetails(){
	$('#fromDate').val("");
	$('#toDate').val("");
	$('#dateRange').prop('checked',false);
	 $("#fromDate").prop('disabled',true);
	 $("#toDate").prop('disabled',true);
	 $("#searchJob").val("");
	 $("#SalesOrdersGrid").jqGrid('GridUnload');
	 showsalesordergridList("","","");
}
</script>