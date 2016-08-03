<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div id="selectPreiners" style="display: none;">
<div id="selectPrinter"  style="width: 270px; height: 150px" align="center">
	<table>
		<tr>
			<td>
				<input type="button" id="printId" class="cancelhoverbutton turbo-tan" value="Print"  style="width:60px;">
			</td>
			<td>
				<input type="button" id="saveId" class="cancelhoverbutton turbo-tan" value="File" style="width:60px;">
			</td>
					
		</tr>
		<tr>
			<td colspan="2">Printer<select name="printerList" id="printerId"><option value="0">-Select-</option> </select></td>
		</tr>
		<tr>
			<td colspan="2">Copies<input type="text" name="copies" size="4"></td>		
		</tr>
		<tr>
			<td>
				<input type="button" id="okId" class="cancelhoverbutton turbo-tan" value="OK" onclick="saveLineDetails()">
			</td>
			<td>
				<input type="button" id="cancelId" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="saveLineDetails()">
			</td>
					
		</tr>
	</table>
	
</div>
</div>

