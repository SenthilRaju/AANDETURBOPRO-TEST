<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
 <meta http-equiv="refresh" content="900" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Bid Date Report</title>
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainMenuToolsPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
            #mainMenuToolsPage a{background:url('./../resources/styles/turbo-css/images/user_32_white.png') no-repeat 0px 4px;color:#FFF}
		</style>
</head>
<body>
	<div  style="background-color: #FAFAFA">
		<div>
			 <jsp:include page="./headermenu.jsp"></jsp:include> 
		</div>
		<div style="height: 10px;"></div>
		<div align="center">
			<fieldset class="ui-widget-content ui-corner-all" style="width: 1150px;height: 600px;">
				<table>
					<tr>
					 	<td>
					 		<label>Job Reports:</label>
					 	</td>
					</tr>
				    <tr>
			    		<td>
			    			<a onclick="reportCriteriaPage()" class="alink">Bid Date Report</a>
			    		</td>
				    </tr>
				    </table>
				</fieldset>
			</div>
			<div style="height: 10px;"></div>
	 	        <table id="footer">
					<tr>
						<td colspan="2">
							<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
						</td>
					</tr>
				</table>
	</div>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
	<script type="text/javascript">
	function reportCriteriaPage(){
		  document.location.href ="./reportcriteria";
	}
</script>
</body>
</html>