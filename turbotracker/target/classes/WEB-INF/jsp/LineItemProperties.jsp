<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="LineItemProperties">
		<form id="LineItemPropertiesID">
			<table>
			<input type="hidden" name="LineItemPropertyName" id="LineItemPropertyID" value="0"/>
				<tr align="left">
					<th style="width:300"><u>Column</u></th>
					<th style="width:170"><u>Italicize</u></th>
					<th style="width:170"><u>Underline</u></th>
					<th style="width:170"><u>Bold</u></th>
				</tr>
				<tr height="5px;"></tr>
				
				
				<tr>				
					<td><label id="column1Label">Item</label></td>					
					<td><input type="checkbox" id="Italic_ItemID" name="Italic_ItemName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_ItemID','Italic_ItemlbID')"  value="false" required="true"><label style="vertical-align: middle;" id="Italic_ItemlbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ItemID" name="UnderLine_ItemName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_ItemID','UnderLine_ItemlbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ItemlbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ItemID" name="Bold_ItemID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_ItemID','Bold_ItemlbID')"  value="false"><label style="vertical-align: middle;" id="Bold_ItemlbID">(No)</label></td>
					
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column2Label">Quantity</label></td>
					<td><input type="checkbox" id="Italic_QuantityID" name="Italic_QuantityName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_QuantityID','Italic_QuantitylbID')" value="false"><label style="vertical-align: middle;" id="Italic_QuantitylbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_QuantityID" name="UnderLine_QuantityName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_QuantityID','UnderLine_QuantitylbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_QuantitylbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_QuantityID" name="Bold_QuantityName" style="vertical-align: middle;" onclick="TriggerLabel('Bold_QuantityID','Bold_QuantitylbID')" value="false"><label style="vertical-align: middle;" id="Bold_QuantitylbID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column3Label">Paragraph</label></td>
					<td><input type="checkbox" id="Italic_ParagraphID" name="Italic_ParagraphName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_ParagraphID','Italic_ParagraphlbID')" value="false"><label style="vertical-align: middle;" id="Italic_ParagraphlbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ParagraphID" name="UnderLine_ParagraphName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_ParagraphID','UnderLine_ParagraphlbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ParagraphlbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ParagraphID" name="Bold_ParagraphID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_ParagraphID','Bold_ParagraphlbID')" value="false"><label style="vertical-align: middle;" id="Bold_ParagraphlbID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column4Label">Manufacturer</label></td>
					<td><input type="checkbox" id="Italic_ManufacturerID" name="Italic_ManufacturerName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_ManufacturerID','Italic_ManufacturerlbID')" value="false"><label style="vertical-align: middle;" id="Italic_ManufacturerlbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_ManufacturerID" name="UnderLine_ManufacturerName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_ManufacturerID','UnderLine_ManufacturerlbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_ManufacturerlbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_ManufacturerID" name="Bold_ManufacturerID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_ManufacturerID','Bold_ManufacturerlbID')" value="false"><label style="vertical-align: middle;" id="Bold_ManufacturerlbID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column5Label">Spec</label></td>
					<td><input type="checkbox" id="Italic_SpecID" name="Italic_SpecName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_SpecID','Italic_SpeclbID')"  value="false"><label style="vertical-align: middle;" id="Italic_SpeclbID" >(No)</label></td>
					<td><input type="checkbox" id="UnderLine_SpecID" name="UnderLine_SpecName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_SpecID','UnderLine_SpeclbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_SpeclbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_SpecID" name="Bold_SpecID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_SpecID','Bold_SpeclbID')" value="false"><label style="vertical-align: middle;" id="Bold_SpeclbID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<!-- <tr>
					<td><label id="column6Label">Cost</label></td>
					<td><input type="checkbox" id="Italic_CostID" name="Italic_CostName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_CostID','Italic_CostlbID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="Italic_CostlbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_CostID" name="UnderLine_CostName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_CostID','UnderLine_CostlbID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="UnderLine_CostlbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_CostID" name="Bold_CostID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_CostID','Bold_CostlbID')" disabled="disabled" value="false"><label style="vertical-align: middle;" id="Bold_CostlbID">(No)</label></td>
				</tr> -->
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column7Label">Mult</label></td>
					<td><input type="checkbox" id="Italic_MultID" name="Italic_MultName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_MultID','Italic_MultlbID')"  value="false"><label style="vertical-align: middle;" id="Italic_MultlbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_MultID" name="UnderLine_MultName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_MultID','UnderLine_MultlbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_MultlbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_MultID" name="Bold_MultID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_MultID','Bold_MultlbID')" value="false"><label style="vertical-align: middle;" id="Bold_MultlbID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column8Label">Price</label></td>
					<td><input type="checkbox" id="Italic_PriceID" name="Italic_PriceName" style="vertical-align: middle;" onclick="TriggerLabel('Italic_PriceID','Italic_PricelbID')"  value="false"><label style="vertical-align: middle;" id="Italic_PricelbID">(No)</label></td>
					<td><input type="checkbox" id="UnderLine_PriceID" name="UnderLine_PriceName" style="vertical-align: middle;" onclick="TriggerLabel('UnderLine_PriceID','UnderLine_PricelbID')" value="false"><label style="vertical-align: middle;" id="UnderLine_PricelbID">(No)</label></td>
					<td><input type="checkbox" id="Bold_PriceID" name="Bold_PriceID" style="vertical-align: middle;" onclick="TriggerLabel('Bold_PriceID','Bold_PricelbID')" value="false"><label style="vertical-align: middle;" id="Bold_PricelbID">(No)</label></td>
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