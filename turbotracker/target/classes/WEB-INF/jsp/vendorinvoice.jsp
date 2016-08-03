<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Job List</title>
		<style type="text/css">
			
		</Style>
	</head>
	<body>
		<div>
			<table id="header">
				<tr>
					<td colspan="2">
						<div><jsp:include page="./headermenu.jsp" /></div>
					</td>
				</tr>
			</table>
			 <table style="width:850px;margin:0 auto;">
				 
				<tr>
					<td colspan="2">
						<div id="tabs_main_job" style="width: 99%;">
							<jsp:include page="job_vendor_invoice.jsp"></jsp:include>
						</div>
					</td>
				</tr>
			</table> 
		</div>
		
				
		<script type="text/javascript">
				
			/* jQuery(document).ready(function(){
				
				$("#tabs_main_job").tabs(); 
					
			}); */
		</script>
		
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script> 
	</body>
</html>