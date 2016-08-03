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
	<table>
	<tr>
	<td style="vertical-align:top">
	<fieldset class= " ui-widget-content ui-corner-all" style="width:330px">
	<legend><label><b>Employee's Assigned</b></label></legend>
		<table>
			<tr><td><label>SalesMan:</label></td><td><select style="width:75px"><option></select></td>
	         <td><label>CSR:</label></td><td><select style="width:75px"><option></select></td></tr>
			<tr><td><label>SalesManager:</label></td><td><select style="width:75px"><option></select></td>
		     <td><label>Engineer:</label></td><td><select style="width:75px"><option></select></td></tr>
			<tr><td><label>Project Manager:</label></td><td><select style="width:75px"><option></select></td></tr>
		</table>
	</fieldset>
	</td>
	<td style="width:20px"></td>
	<td style="vertical-align:top">
		<fieldset class= " ui-widget-content ui-corner-all" style="width:400px">
		<legend><label><b>Misc</b></label></legend>
		<table>
		
		<tr><td><label>Promised:</label></td><td><input type="text" style="width:75px"></td>
			<td><label>Division:</label></td><td><select style="width:75px"><option></option></select></td></tr>
		<tr><td><label><a href="">Tax Territory:</a></label></td><td><select style="width:75px"><option></option></select></td>
		     <td><label>Customer PO #:</label></td><td><select style="width:75px"><option></option></select></td></tr>
		<tr><td><label>Terms:</label></td><td><select style="width:75px"><option></select></td>
		<td><label>Tag:</label></td><td colspan="2"><input type="text" style="width:75px"></td></tr>
       </table>
       </fieldset>
	</td>
	</tr>
	</table>
</body>
</html>