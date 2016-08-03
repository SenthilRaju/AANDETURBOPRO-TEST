<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="buttons">
	<table>
		<tr><td><input id="track" type="button" style="color:#FFFFFF;background:#87CEEB" value="Track" class="ui-button ui-widget ui-state-default ui-corner-all" ></td>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Vendor Invoice" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="openvendorinvoicedialog()"></td>
 	 		<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Customer Invoice" class="ui-button ui-widget ui-state-default ui-corner-all" onclick="opencustomerinvoicedialog()"></td>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Launch Submittal" id="sub" onclick="submittaldialog()"/>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="PO Release" id="po" onclick="poreleasedialog()" />
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="SO Release" id="sales" onclick="soreleasedialog()"/></td>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Preview" class="ui-button ui-widget ui-state-default ui-corner-all"></td>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Save" class="ui-button ui-widget ui-state-default ui-corner-all"></td>
			<td><input type="button" style="color:#FFFFFF;background:#87CEEB" value="Next" class="ui-button ui-widget ui-state-default ui-corner-all"></td></tr> 
	</table>
</div>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
</body>
</html>
