<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="addButtonDiv">
	<form action="" id="addButtonForm">
		<table onclick="costPercentage(); pricePercentageCost();" style="width: 550px;">
			<tr id="displayProduct">
				<td>
					<b>Description:<span style="color:red;"> *</span></b>		
				</td>
				<td>
					<input type="text"  id="product" name="product" style="vertical-align: middle;width: 300px;" class="validate[required]" placeholder="Minimum 2 characters required">
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" 
											 style="padding-left: 10px;"><br>
				</td>
			</tr>
		 	<tr id="displayQuantity">
				<td>
					<b>Quantity:</b>
				</td>
				<td>
					<input type="text"  id="itemQuantity" name="itemQuantity" style="width: 300px;" class="validate[maxSize[8]]" >					
				</td>
			</tr> 
			<tr id="displayParagraph">
				<td>
					<b>Paragraph:</b>
				</td>
				<td>
					<input type="text"  id="paragraph" name="paragraph" style="width: 300px;">					
				</td>
			</tr>
			<tr id="displayVendor">
				<td>
					<b>Vendor:<span style="color:red;"> *</span></b>
				</td>
				<td>
					<input type="text"  id="manufacturer" name="manufacturer" class="validate[required]" style="width: 300px;" placeholder="Minimum 1 character required">					
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"
							  style="padding-left: 10px;"><br>
				</td>
			</tr>
			<tr id="displayCost">
				<td>
					<b>Cost:</b>
				</td>
				<td>
					<input type="text"  id="cost" name="cost" style="width: 300px;" class="validate[custom[number]]" >
				</td>
			</tr>
			<tr id="displayMargin">
				<td>
					<b>Margin:</b>
				</td>
				<td>
					<input type="text" id="percentage" name="percentage" style="width: 116px;" class="validate[custom[number]]"  ><label>%</label>
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
						<div id="maginAlert" style="display: none;color: red;"> The percentage value must be between 0 and 99.</div>
				</td>
			</tr>
			<tr id="displayPrice">
				<td>
					<b>Price:</b>
				</td>
				<td>
					<input type="text" id="price" name="price" style="width: 300px;" class="validate[custom[number]]"  onclick="pricePercentageCost()" onfocus="pricePercentageCost()">					
				</td>
			</tr>
			<tr></tr>
			<tr id="inlneNoteHideShow" style="display: none;">
				<td>
					<b>Inline Notes:</b>
				</td>
				<td>
					<textarea id="inlineNote" name="inlineNote" rows="3" cols="40" onclick="costPercentage()" onfocus="costPercentage()"></textarea>
				</td>
			</tr>
			<tr></tr>
			<tr>
				<td>
					<b>Foot Notes:</b>
				</td>
				<td>
					<textarea id="productNote" name="footnote" rows="3" cols="40"></textarea>
				</td>
			</tr>
			<tr style="display: none;">
				<td>
					<b>JoQuoteDetailID:</b>
				</td>
				<td>
					<input type="text"  id="joQuoteDetailID" name="joQuoteDetailID" style="width: 300px;">					
				</td>
			</tr>
			<tr style="display: none;">
				<td>
					<b>RxManufacturerID:</b>
				</td>
				<td>
					<input type="text"  id="rxManufacturerID" name="rxManufacturerID">					
				</td>
			</tr>
			<tr style="display: none;">
				<td>
					<b>JoQuoteHeaderID:</b>
				</td>
				<td>
					<input type="text"  id="joQuoteHeaderID" name="joQuoteHeaderID">					
				</td>
			</tr>
			<tr style="display: none;">
				<td>
					<b>VeFactoryId:</b>
				</td>
				<td>
					<input type="text"  id="veFactoryId" name="veFactoryId">					
				</td>
			</tr>
		</table>
		<hr width="560px;">
		<table style="width:565px;" align="left">
		 	<tr>
			 	<td></td>
			 	<td align="right">
			 	<table><tr>
			 	<td style="width: 70%"></td>
			 	<td style="width: 10%"></td>
			 	<td style="width: 10%">
			 	<input id="importFactory" type="button" class="cancelhoverbutton turbo-tan"  value="Import Factory Quote" onclick="" style="width:150px;">  
					<input id="buildFromStock" type="button" class="cancelhoverbutton turbo-tan"  value="Build from Stock" onclick="" style="width:125px;">  
					<input id="customBuild" type="button" class="cancelhoverbutton turbo-tan"  value="Custom Build" onclick="" style="width:100px;">  
					
			 	</td>
			 		<td>
			 			<input id="saveQuoteNote" type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveQuoteNavigator()" style=" width:80px;">
			 			<input id="saveQuoteTempNote" type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveQuoteTempNavigator()" style=" width:80px; display: none">
			 		</td>
			 		<td>
			 			<input id="cancelQuoteNote" type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuoteNavigator()" style="width:80px;">
						<input id="cancelQuoteTempNote" type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuoteTempNavigator()" style="width:80px; display: none;">
			 		</td>
			 	</tr>
			 	</table>
					
					
				</td>
			</tr>
		</table>
	</form>
</div>