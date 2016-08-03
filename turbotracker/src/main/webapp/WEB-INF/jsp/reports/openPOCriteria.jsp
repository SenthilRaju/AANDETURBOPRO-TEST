<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Reports-Open PO</title>
</head>
<body>
<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
	<div align="center" style="margin-bottom: 30px;">
				<form id="opeJobsCriteriaform" action="" style="width: 360px;border: 2px solid #003961; border-radius: 10px 10px 10px 10px; height: 200px;">
					<table style="width:auto;margin:0 auto;">
						<tr align="center">
							<td >
								<h2><label>Criteria</label></h2>
							</td>
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
								<input type="button" value="Print Reports" class="turbo-blue savehoverbutton" onclick="printReports()" />
							</td>
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
</body>
<script type="text/javascript">
jQuery(document).ready(function(){
	$('#search').hide();
});
function previewReports(){
	window.open('./job_controller/previewreport?reportName=openpo',target="_blank");
}
function printReports(){
	var wnd = window.open('./job_controller/previewreport?reportName=openpo',target="_blank");
	wnd.print();
}
</script>
</html>