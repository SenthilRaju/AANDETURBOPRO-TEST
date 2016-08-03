<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Customers List</title>
		<style type="text/css">
			input#customerNameHeader {width:400px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
			input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#add { display: none; }
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			#search{display: block;}
			.ui-jqgrid-bdiv{height: 250px !important;}
			
		</style>
	</head>
	<body>
		<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		<div align="center" style="margin-left: -178px;">
		<jsp:include page="./vendorInvoice_Outside.jsp"></jsp:include> 
		</div>	
		 
	
		<div style="height: 20px;"></div>
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/vendorinvoice_outside.js"></script>
	</body>
</html>