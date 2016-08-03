<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbo Pro - Job List</title>
	<style type="text/css">
		.ui-tabs-nav {height: 32px;}
		#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
		#mainMenuJobsPage a span{color:#FFF}
	</Style>
</head>
<body>
<div class="bodyDiv">
<div> 
	<jsp:include page="./jobHeaderMenu.jsp"></jsp:include> 
</div>
	<table class="joblistTable">
		<tr>
			<td colspan="2">
				<div id="tabs_main_job" class="joblistDiv" style="box-shadow: 1px 10px 5px 5px rgb(170, 170, 170);">
					<jsp:include page="joblistwizard.jsp"></jsp:include>
				</div>
			</td>
		</tr>
	</table>
</div>
<div class="joblistfooterDiv">
	<table id="footer">
	<tr>
		<td colspan="2">
			<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
		</td>
	</tr>
	</table>
</div>
<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery-ui-1.8.23.custom.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
<script type="text/javascript">
function formatCurrency(strValue)
{
	if(strValue == "" || strValue == null){
		return "$0.00";
	}
	strValue = strValue.toString().replace(/\$|\,/g,'');
	dblValue = parseFloat(strValue);

	blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
	dblValue = Math.floor(dblValue*100+0.50000000001);
	intCents = dblValue%100;
	strCents = intCents.toString();
	dblValue = Math.floor(dblValue/100).toString();
	if(intCents<10)
		strCents = "0" + strCents;
	for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++)
		dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
		dblValue.substring(dblValue.length-(4*i+3));
	return (((blnSign)?'':'-') + '$' + dblValue + '.' + strCents);
}
</script>
</body>
</html>