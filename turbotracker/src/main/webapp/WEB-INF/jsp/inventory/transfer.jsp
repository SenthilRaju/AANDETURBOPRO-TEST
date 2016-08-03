<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - Jobs</title>
<link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
<style type="text/css">
#mainMenuJobsPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #54A4DE;
}

#mainMenuJobsPage a span {
	color: #FFF
}
#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainmenuInventoryPage a{background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
			#mainmenuInventoryPage ul li a{background: none; }
</style>
</head>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<div style="width:86%; margin:0 auto;">
		<div id="jqgridLine">
			<table id="transferGrid"></table>
			<div id="transferGridPager"></div>
			<table align="left">
				<tr>
					<td>
						<!-- <fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:50px;height:25px;background:#EEDEBC;"> -->
			            	<table>
			            		<tr>
			            		<td align="right" style="padding: 0 7px 0 1px;"><input  type="button"  class="add" value="   ADD" alt="Add new Job" onclick="addTransfer();"/></td>
			            		<td align="right" style="padding: 0 7px 0 1px;"><input  type="button"  class="edit" value="     EDIT" alt="Edit new Job" onclick="editTransfer()"/></td>
			            		<td align="right" style="padding: 0 7px 0 1px;"><input  type="button"  class="cancelhoverbutton turbo-tan" value="COPY" alt="Edit new Job" onclick="copy()" /></td>
			            		<td align="right" style="padding: 0 7px 0 1px;"><input  type="button"  class="cancelhoverbutton turbo-tan" value="PDF" alt="Edit new Job" onclick="viewWhTransferPDF()" /></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="addTransfer();" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit" onclick="editQuoteFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete" onclick="deleteQuoteFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/lineItem_new.png" title="Line Items" onclick="addOpenLineItemDialog()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/printer.png" title="Print Preview" onclick="addLineItemDialog(); return false;" style="background: #EEDEBC"></td>
					         		<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/forward.png" title="Quick Add Line Item" onclick="addNewLineItemDialog(); return false;" style="background: #EEDEBC"></td>  
			            		</tr>
			            	</table>
		            	<!-- </fieldset> -->
					</td>					
				</tr>
			</table>
		</div>
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
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/transfer.js"></script>



</body>
</html>