<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="LineItemTempProperties">
		<form id="LineItemTempPropertiesID">
			<table>
			<input type="hidden" name="LineItemtempPropertyName" id="LineItemtempPropertyID" value="0"/>
				<tr align="left">
					<th style="width:300"><u>Column</u></th>
					<th style="width:170"><u>Italicize</u></th>
					<th style="width:170"><u>Underline</u></th>
					<th style="width:170"><u>Bold</u></th>
				</tr>
				<tr height="5px;"></tr>
				
				
				<tr>				
					<td><label id="column1Label">Item</label></td>					
					<td><input type="checkbox" id="Italic_ItemTempID" name="Italic_ItemName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_ItemTempID','Italic_ItemlbTempID')"  value="false" required="true"><label style="vertical-align: middle;" id="Italic_ItemlbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ItemTempID" name="UnderLine_ItemName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_ItemTempID','UnderLine_ItemlbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ItemlbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ItemTempID" name="Bold_ItemTempName" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_ItemTempID','Bold_ItemlbTempID')"  value="false"><label style="vertical-align: middle;" id="Bold_ItemlbTempID">(No)</label></td>
					
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column2Label">Quantity</label></td>
					<td><input type="checkbox" id="Italic_QuantityTempID" name="Italic_QuantityName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_QuantityTempID','Italic_QuantitylbTempID')" value="false"><label style="vertical-align: middle;" id="Italic_QuantitylbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_QuantityTempID" name="UnderLine_QuantityName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_QuantityTempID','UnderLine_QuantitylbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_QuantitylbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_QuantityTempID" name="Bold_QuantityName" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_QuantityTempID','Bold_QuantitylbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_QuantitylbTempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column3Label">Paragraph</label></td>
					<td><input type="checkbox" id="Italic_ParagraphTempID" name="Italic_ParagraphName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_ParagraphTempID','Italic_ParagraphlbTempID')" value="false"><label style="vertical-align: middle;" id="Italic_ParagraphlbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ParagraphTempID" name="UnderLine_ParagraphName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_ParagraphTempID','UnderLine_ParagraphlbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ParagraphlbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ParagraphTempID" name="Bold_ParagraphTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_ParagraphTempID','Bold_ParagraphlbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_ParagraphlbTempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column4Label">Manufacturer</label></td>
					<td><input type="checkbox" id="Italic_ManufacturerTempID" name="Italic_ManufacturerName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_ManufacturerTempID','Italic_ManufacturerlbTempID')" value="false"><label style="vertical-align: middle;" id="Italic_ManufacturerlbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ManufacturerTempID" name="UnderLine_ManufacturerName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_ManufacturerTempID','UnderLine_ManufacturerlbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ManufacturerlbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ManufacturerTempID" name="Bold_ManufacturerTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_ManufacturerTempID','Bold_ManufacturerlbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_ManufacturerlbTempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column5Label">Spec</label></td>
					<td><input type="checkbox" id="Italic_SpecTempID" name="Italic_SpecName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_SpecTempID','Italic_SpeclbTempID')"  value="false"><label style="vertical-align: middle;" id="Italic_SpeclbTempID" >(No)</label></td>
					<td><input type="checkbox" id="UnderLine_SpecTempID" name="UnderLine_SpecName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_SpecTempID','UnderLine_SpeclbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_SpeclbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_SpecTempID" name="Bold_SpecTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_SpecTempID','Bold_SpeclbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_SpeclbTempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<!-- <tr>
					<td><label id="column6Label">Cost</label></td>
					<td><input type="checkbox" id="Italic_CostTempID" name="Italic_CostName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_CostTempID','Italic_CostlbTempID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="Italic_CostlbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_CostTempID" name="UnderLine_CostName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_CostTempID','UnderLine_CostlbTempID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="UnderLine_CostlbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_CostTempID" name="Bold_CostTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_CostTempID','Bold_CostlbTempID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="Bold_CostlbTempID">(No)</label></td>
				</tr> -->
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column7Label">Mult</label></td>
					<td><input type="checkbox" id="Italic_MultTempID" name="Italic_MultName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_MultTempID','Italic_MultlbTempID')"  value="false"><label style="vertical-align: middle;" id="Italic_MultlbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_MultTempID" name="UnderLine_MultName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_MultTempID','UnderLine_MultlbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_MultlbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_MultTempID" name="Bold_MultTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_MultTempID','Bold_MultlbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_MultlbTempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column8Label">Price</label></td>
					<td><input type="checkbox" id="Italic_PriceTempID" name="Italic_PriceName" style="vertical-align: middle;" onclick="TriggerTempLabel('Italic_PriceTempID','Italic_PricelbTempID')"  value="false"><label style="vertical-align: middle;" id="Italic_PricelbTempID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_PriceTempID" name="UnderLine_PriceName" style="vertical-align: middle;" onclick="TriggerTempLabel('UnderLine_PriceTempID','UnderLine_PricelbTempID')" value="false"><label style="vertical-align: middle;" id="UnderLine_PricelbTempID">(No)</label></td>
					<td><input type="checkbox" id="Bold_PriceTempID" name="Bold_PriceTempID" style="vertical-align: middle;" onclick="TriggerTempLabel('Bold_PriceTempID','Bold_PricelbTempID')" value="false"><label style="vertical-align: middle;" id="Bold_PricelbTempID">(No)</label></td>
				</tr>
				<tr height="10px;"></tr>
			</table>
			<hr width="310px;">
			
			<!-- <table align="right" id="jobquoteButtons">
				<tr>
				 	<td>
		   				<input type="button" class="printhoverbutton turbo-tan" value="Save" onclick="savequotelineitemproperties()" style=" width:80px;color: white;">
		   			   <input type="button" class="printhoverbutton turbo-tan" value="Close" onclick="$('#LineItemProperties').dialog('close')" style=" width:80px;color: white;">
		   			</td>
				</tr>
			</table>
			<table align="right" id="jobquoteTemplateButtons" style="display: none;">
				<tr>
				 	<td>
		   				<input type="button" class="printhoverbutton turbo-tan" value="Save" onclick="savequoteTemplatelineitemproperties()" style=" width:80px;color: white;">
		   			   <input type="button" class="printhoverbutton turbo-tan" value="Close" onclick="$('#LineItemProperties').dialog('close')" style=" width:80px;color: white;">
		   			</td>
				</tr>
			</table> -->
		</form>
	</div>