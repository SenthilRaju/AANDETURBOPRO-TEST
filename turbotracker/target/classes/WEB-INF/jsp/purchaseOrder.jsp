<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Purchase Order</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		.ui-widget-overlay {
		    background: none repeat scroll 50% 50% #2B2922;
		    opacity: 0.6;
		}
		
     </style>
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:960px;margin:0 auto; background-color: #FAFAFA;height: auto; margin-bottom: 40px;">
			<ul>
				<li id="pogeneral"><a href="purchaseOrderGeneral">General</a></li>
				<li id="polineitems"><a href="purchaseOrderLines">Line Items</a></li>
				<li id="poacknowledgement"><a href="purchaseOrderAck">Acknowledgement</a></li>
				<li id="poCustAck" style="float: right;"><input type="button" class="savehoverbutton turbo-tan" value="Cust Ack" onclick="viewPOAckPDF()" style="width:80px" id="custAckpoRelease"></li>
				<li id="poconfirm" style="float: right;"><input type="button" class="savehoverbutton turbo-tan" value="Confirm" onclick="viewPOGENPDF()" style="width:80px" id="confirmpoRelease"></li>
			</ul>
		
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			$(".tabs_main").tabs({
				cache: true,
				ajaxOptions: {
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 300px;padding-top: 200px;'>"+
								"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
								"</label></div>");
					}
				},
				load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
				select: function (e, ui) {
					//window.location.hash = ui.tab.hash;
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 300px;padding-top: 200px;background-color: #FAFAFA'>"+
								"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
			$("#search").css("display", "none");
	   	});

	
    </script>
</body>
<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
</html>