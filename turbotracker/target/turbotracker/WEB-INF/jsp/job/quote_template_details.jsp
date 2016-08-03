<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div id="addquoteTemplate" style="display: none;">
	<div id="addQuoteTemplateBasicInfo">
		<form action="" id="quoteTemplateManipulateForm">
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Quote Template</b></label></legend>
			<table style="width:1000px">
				<tr>
					<td style="width: 35px;"><label>Type:</label></td>
					<td>
						<select style="width:170px;margin-left: 3px;border:#DDBA82;" id="quoteTemplateTypeDetail" name="quoteTemplateTypeDetailName">
							<option value="-1"> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
					<td style="width: 60px;"><label>Shared:</label></td>
					<td><input type="checkbox" id="templateShared" name="templateShared"></td>
					<td style="width: 100px;"><label>Template Description:</label></td>
					<td>
						<input type="text" id="templateDescription" name="templateDescription"> 
					</td>
				</tr>
			</table>
			</fieldset>
			<br>
			<table id="quoteTemplateProductsGrid"></table><div id="quoteTemplateProductsGridPager"></div> 
			<table align="left">
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:50px;height:25px;background:#EEDEBC;">
			            	<table>
			            		<tr>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="addQuoteTemplateFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit" onclick="editTemplateLineItems()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete" onclick="deleteQuoteFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/lineItem_new.png" title="Line Items" onclick="addOpenLineItemDialog()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/images/printer.png" title="Print Preview" onclick="addLineItemDialog(); return false;" style="background: #EEDEBC"></td>
					         		<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/forward.png" title="Quick Add Line Item" onclick="addNewLineItemDialog(); return false;" style="background: #EEDEBC"></td>  
			            		</tr>
			            	</table>
		            	</fieldset>
					</td>
					<td align="right" style="padding-left: 865px;">
						<input type="button" title="Add Manufacturer" class="add turbo-tan" value="Add Manufacturer" onclick="addManufacture()" style="width: 160px;">
					</td>
				</tr>
			</table>
			<table style="width: 770px;">
				<tr style="display: none;"><td align="right"><input type="text" value="Manufacturer"  class="ui-button ui-widget ui-state-default ui-corner-all" style="color:#FFF;background:#e0842a; width:100px" ></td></tr>
				<tr>
					<td align="right">
						<input type="button" value="Manufacturer" style="color:#FFFFFF;background:#637c92;border-radius:5px;border:0px;padding:3px 5px;cursor:pointer;display: none;"> 
					</td>
				</tr>
				<tr>
					<td>
						<fieldset style="" class= "custom_fieldset">
						<legend class="custom_legend"><label><b>Remarks</b></label></legend>
							<table>
								<tr><td><textarea id="quoteTemplateRemarksID" name="quoteTemplateRemarksName" style="width: 770px; height: 57px;"></textarea></td></tr>
							</table>
						</fieldset>
					</td>
					<td style="padding-left: 20px;">
						<fieldset style="width:250px" class= "custom_fieldset">
				 			<table>
								<tr><td><label>Total Price: </label></td><td><input type="text" style="width:100px" id="quoteTemplateTotalPrice" disabled="disabled" name="quoteTemplateTotalPrice"></td></tr>
							</table>
						</fieldset>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div id= "addInLineItemTemp">
	<form action="" id="addInLineItemIDTemp">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="lineItemIdTemp" name="lineItemName" style="height: 232px; width:470px;"></textarea>
	   				<input id="lineItemLableId" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="LineItemInfoTemp()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelInLineNoteTemp()" style="width:80px;">
	   			</td>
			</tr>
		</table>
		</form>
	</div> 
		<hr width="1115px;">
		<table style="width:1080px;align:center">
		 	<tr>
			 	<td></td>
			 	<td id="addQuotesView" align="right" style="padding-right:1px;">
			 		<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuoteTemplate()" style=" width:125px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuoteTemplate()" style="width:80px;">  
				</td>
				<td id="editQuotesView" align="right" style="padding-right:1px;"></td>
				<td id="copyQuotesView" align="right" style="padding-right:1px;"></td>
			</tr>
		</table>
	</div>
	<div id="addTemplateLineItem">
	<form action="" id="addTemplateForm">
		<table onclick="" style="width: 550px;">
			<tr>
				<td><b>Description:<span style="color:red;"> *</span></b></td>
				<td>
					<input type="text"  id="productTemplate" name="productTemplate" style="vertical-align: middle;width: 300px;" class="validate[required]" placeholder="Minimum 2 characters required">
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;"><br>
				</td>
			</tr>
		 	<tr id="displayQuantityTemplate">
				<td><b>Quantity:</b></td>
				<td><input type="text"  id="itemQuantityTemplate" name="itemQuantityTemplate" style="width: 300px;" class="validate[maxSize[8]]" ></td>
			</tr>
			<tr id="displayParagraphTemplate">
				<td><b>Paragraph:</b></td>
				<td><input type="text"  id="paragraphTemplate" name="paragraphTemplate" style="width: 300px;"></td>
			</tr>
			<tr id="displayVendorTemplate">
				<td><b>Vendor:<span style="color:red;"> *</span></b></td>
				<td>
					<input type="text"  id="manufacturerTemplate" name="manufacturerTemplate" class="validate[required]" style="width: 300px;" placeholder="Minimum 1 character required">					
					<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;"><br>
				</td>
			</tr>
			<tr id="displayCostTemplate">
				<td><b>Cost:</b></td>
				<td><input type="text"  id="costTemplate" name="costTemplate" style="width: 300px;" class="validate[custom[number]]" ></td>
			</tr>
			<tr id="displayMarginTemplate">
				<td><b>Margin:</b></td>
				<td><input type="text" id="percentageTemplate" name="percentageTemplate" style="width: 116px;" class="validate[custom[number]]"  ><label>%</label></td>
			</tr>
			<tr>
				<td></td>
				<td><div id="marginAlertTemplate" style="display: none;color: red;"> The percentage value must be between 0 and 99.</div></td>
			</tr>
			<tr id="displayPriceTemplate">
				<td><b>Price:</b></td>
				<td><input type="text" id="priceTemplate" name="priceTemplate" style="width: 300px;" class="validate[custom[number]]"  onclick="pricePercentageCost()" onfocus="pricePercentageCost()"></td>
			</tr>
			<tr></tr>
			<tr id="inlneNoteHideShowTemplate" style="display: none;">
				<td><b>Inline Notes:</b></td>
				<td><textarea id="inlineNoteTemplate" name="inlineNoteTemplate" rows="3" cols="40" onclick="costPercentage()" onfocus="costPercentage()"></textarea></td>
			</tr>
			<tr></tr>
			<tr>
				<td><b>Foot Notes:</b></td>
				<td><textarea id="productNoteTemplate" name="footnoteTemplate" rows="3" cols="40"></textarea></td>
			</tr>
			<tr style="display: none;">
				<td><b>joQuoteTemplateDetailID:</b></td>
				<td><input type="text"  id="joQuoteTemplateDetailID" name="joQuoteTemplateDetailID" style="width: 300px;"></td>
			</tr>
			<tr style="display: none;">
				<td><b>RxManufacturerID:</b></td>
				<td><input type="text"  id="rxManufacturerIDTemplate" name="rxManufacturerIDTemplate"></td>
			</tr>
			<tr style="display: none;">
				<td><b>joQuoteTemplateHeaderID:</b></td>
				<td><input type="text"  id="joQuoteTemplateHeaderID" name="joQuoteTemplateHeaderID"></td>
			</tr>
			<tr style="display: none;">
				<td><b>VeFactoryId:</b></td>
				<td><input type="text"  id="veFactoryIdTemplate" name="veFactoryIdTemplate"></td>
			</tr>
		</table>
		<hr width="560px;">
		<table style="width:565px;" align="left">
		 	<tr>
			 	<td></td>
			 	<td align="right">
					<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveTemplateLineItem()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelTemplateLineItem()" style="width:80px;">  
				</td>
			</tr>
		</table>
	</form>

</div>