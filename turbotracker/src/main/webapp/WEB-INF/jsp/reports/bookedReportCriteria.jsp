<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports-Open Jobs</title>
</head>
<body>
<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
	<div align="center" style="margin-bottom: 30px;margin-top: 70px;">
				<form id="opeJobsCriteriaform" action="" style="width: 524px;border: 2px solid #003961; border-radius: 10px 10px 10px 10px; height: 200px;">
					<table style="width:auto;margin:0 auto;">
						<tr align="center">
							<td >
								<h2><label>Criteria</label></h2>
							</td>
						</tr>
						<tr>
							<td>
								<label>Customer:</label>
							</td>
							<td>
								<input type="text" id="CustomerName" name="customer" style="width: 147%;" placeholder="Minimum 2 character to search.">
								<input type="text" style="display: none;" id="JobCustomerIdreport">
							</td>
						</tr>
						<tr style="height: 5px;">
						</tr>
						<tr style="height: 10px;">
						</tr>
						<tr>
							<td>
							</td>
							<td>
								<input type="button" value="Preview Reports" class="turbo-blue savehoverbutton" onclick="previewReports()" />
							</td>
							<td>
								<input type="button" value="Print Reports" class="turbo-blue savehoverbutton" onclick="printReports()" style="margin-right: -34px;"/>
							</td>
						</tr>
					</table>
				</form>
		</div>
		<div style="padding-top: 300px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
<script type="text/javascript">
jQuery(document).ready(function(){
	$('#search').hide();
});
function previewReports(){
	var customerID = $('#JobCustomerIdreport').val();
	createtpusage('Reports-Bookings','Preview Report','Info','Reports-Bookings,Previewing Report,customerID:'+customerID);
	window.open('./job_controller/previewreport?salesRepID='+customerID+'&reportName=Booked',target="_blank");
}
function printReports(){
var customerID = $('#JobCustomerIdreport').val();
createtpusage('Reports-Bookings','Print Report','Info','Reports-Bookings,Printing Report,customerID:'+customerID);
var wnd = window.open('./job_controller/previewreport?salesRepID='+customerID+'&reportName=Booked',target="_blank");
	wnd.print();
}
$(function() { var cache = {}; var lastXhr='';
$( "#CustomerName" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#JobCustomerIdreport").val(id); },
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("salescontroller/salesrep",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});
$( "#jobname" ).autocomplete({
	minLength: 2, timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#JobMasterIdreport").val(id); },
	source: function( request, response ) { var term = request.term;
		if( term in cache ){ response( cache[ term ] ); return; }
		lastXhr = $.getJSON("search/searchjoblist",request,function( data, status, xhr ) {cache[term] = data; if (xhr === lastXhr) {response(data);}});
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	} 
});
});
</script>
</html>