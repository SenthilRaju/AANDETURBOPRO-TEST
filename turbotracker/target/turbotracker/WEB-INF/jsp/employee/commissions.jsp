<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Commissions</title>
  <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
     </style>
</head>
<body>
	<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<table height="100px;">
				<tr>
					<td>
					</td>
				</tr>
			</table>
			<table style="width:600px;margin:0 auto;">
				<tr><td align="right">
			    <tr id="commissionsList"><td><table id="commissionsGridList"></table><div id="commissionsGridPager"></div></td></tr>
	 	      </table>
		 	  <table height="30px;">
				<tr>
					<td>
					</td>
				</tr>
			</table>
 	        <div style="padding-top: 25px;">
	 	        <table id="footer">
					<tr>
						<td colspan="2">
							<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
						</td>
					</tr>
				</table>
			</div>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/commisions.js"></script>
</body>
</html>