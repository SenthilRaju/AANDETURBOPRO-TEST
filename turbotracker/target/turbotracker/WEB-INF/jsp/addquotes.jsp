<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<div id="addquotes">
	<div id="addquotesLineItem">
		<form action="" id="quoteManipulateForm">
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>New Quote for Job: </b>${requestScope.joMasterDetails.description}</label></legend>
			<table style="width:1000px">
				<tr>
					<td style="width: 35px;"><label>Type:</label></td>
					<td>
						<select style="width:170px;margin-left: 3px;border:#DDBA82;" id="quoteTypeDetail" name="quoteTypeDetailName">
							<option value="-1"> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
					<td style="width: 60px;"><label>Revision:</label></td>
					<td><input type="text" style="width:75px" id="jobQuoteRevision" name="jobQuoteRevision" class="validate[custom[number]]" maxlength="3"></td>
					<td style="width: 100px;"><label>Submitted by:</label></td>
					<td>
						<input type="text" id="jobQuoteSubmittedBYFullName" name="jobQuoteSubmittedBYFullName" placeholder="Minimum 1 character required"> 
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
					</td>
					<td style="display: none;">
						<input type="text" id="jobQuoteSubmittedBYID" name="jobQuoteSubmittedBYID"  style="display: none;">
						<input type="text" id="jobQuoteSubmittedBYInitials" name="jobQuoteSubmittedBYInitials"  style="display: none;">
						<input type="text" id="joHeaderID" name="joHeaderID"  style="display: none;">
						<input type="text" id="joDetailID" name="joDetailID"  style="display: none;">
						<input type="text" id="headerIDForCopyQuote" name="headerIDForCopyQuote" style="display: none;">
					</td>
				</tr>
			</table>
			</fieldset>
			<br>
			<table id="addquotesList"></table>  <div id="addquotespager" style="display: none;"></div>
			<table align="left">
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:50px;height:25px;background:#EEDEBC;">
			            	<table>
			            		<tr>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="addQuoteFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit" onclick="editQuoteFrom()" style="background: #EEDEBC"></td>
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
			</table></form>
			</div>
			<table style="width: 770px;">
				<tr style="display: none;"><td align="right"><input type="text" value="Manufacturer"  class="ui-button ui-widget ui-state-default ui-corner-all" style="color:#FFF;background:#e0842a; width:100px" ></td></tr>
				<tr>
					<td align="right">
					<input type="button" value="Manufacturer" style="color:#FFFFFF;background:#637c92;border-radius:5px;border:0px;padding:3px 5px;cursor:pointer;display: none;"> 
					</td>
				</tr>
			</table>
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Remarks</b></label></legend>
				<table>
					<tr><td><textarea id="quoteRemarksID" name="quoteRemarksName" style="width: 1067px; height: 57px;"></textarea></td></tr>
				</table>
			</fieldset>
			<br>
			<table>
				<tr><td style="padding-bottom: 4px;">
					<table>
						<tr>
							<td>
								<fieldset style="width:380px;" class= "custom_fieldset">
								<legend class="custom_legend"><label><b>Internal Notes</b></label></legend>
									<table>
										<tr><td><input type="text" id="jobQuoteInternalNote" name="jobQuoteInternalNote" style="width:350px;height:25px"></td>
										<td><input type="hidden" id="quoteRevisionExisting"></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
				</td>
				<td style="width:20px"></td>
				<td>
					<fieldset style="width:330px" class= "custom_fieldset">
			 			<table>
							<tr><td><label>Total Price: </label></td><td><input type="text" style="width:100px" id="quoteTotalPrice" disabled="disabled" name="quoteTotalPrice"></td></tr>
							<tr  style="display: none;"><td><label>Total Cost: </label></td><td><input type="text" style="width:100px" id="quoteTotalCost" disabled="disabled" name="quoteTotalCost"></td></tr>
							<tr><td><label>Discounted Price: </label></td><td><input type="text" style="width:100px" id="quoteDiscountedPrice" class="validate[custom[number]]" ></td></tr>
						</table>
					</fieldset>
				</td></tr>
			</table>
		<hr width="1115px;">
		<table style="width:1080px;align:center">
		 	<tr>
			 	<td></td>
			 	<td id="addQuotesView" align="right" style="padding-right:1px;">
			 		<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuote()" style="width:80px;">  
				</td>
				<td id="editQuotesView" align="right" style="padding-right:1px;"></td>
				<td id="copyQuotesView" align="right" style="padding-right:1px;"></td>
			</tr>
		</table>
	</div>
	<div id= "addInLineItem">
	<form action="" id="addInLineItemID">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="lineItemId" name="lineItemName" style="height: 252px; width:570px;"></textarea>
	   				<input id="lineItemLableId" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="LineItemInfo()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelInLineNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
		</form>
	</div> 
	<div id= "printLineItemDialog" onload="addLineItemDialog()">
	<form action="" id="printLineItemID" style="border: 0px;">
		<table cellpadding = "0" cellspacing = "0" border ="0" style="height:800px; width:100%; border: 0px;">
			<tr>
				<td style="height:90%; vertical-align:top;">
					<label style="font-weight: bold;font-size: 17px;">Quote Line Items</label>
					<label style="font-weight: bold;float: right;">Project Name: <span style="font-weight: normal;" id="projectName"></span></label>
						<hr>
					<table id="printValues" border="1" cellpadding="2" cellspacing="0" style="width: 100%;">
						<col width="5">
			 			<col width="90">
			  			<col width="10">
			  			<col width="30">
			  			<col width="40">
			  			<col width="30">
			  			<col width="50">
			  			<col width="30">
			  			<col width="50">
					</table>
				</td>
			</tr>
			<tr id="custombuttons">
				<td style="height:10%; vertical-align: bottom;">
					<table align="right">
						<tr>
						 	<td>
				   				<input type="button" class="printhoverbutton turbo-tan" value="Print" onclick="printLineItem()" style=" width:80px;color: white;">
								<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelPrintableView()" style="width:80px;color: white;">
				   			</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		</form>
	</div> 
	<div><jsp:include page="./quoteNavigator.jsp"></jsp:include></div>
	<div id="quotePropertiesDialog">
		<form id="quotePropertiesFormID">
			<table>
				<tr align="left">
					<th style="width:300"><u>Column</u></th>
					<th style="width:170"><u>Display</u></th>
					<th style="width:100"><u>Print</u></th>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Quantity</td>
					<td><input type="checkbox" id="quantityDisplayID" name="quantityDisplayName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintID" name="quantityPrintName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelPrintID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Paragraph</td>
					<td><input type="checkbox" id="quantityDisplayParaID" name="quantityDisplayParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintParaID" name="quantityprintParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelPrintID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Manufacturer</td>
					<td><input type="checkbox" id="quantityDisplayManufID" name="quantityDisplayManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintManufID" name="quantityPrintManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelPrintID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Spec</td>
					<td><input type="checkbox" id="quantityDisplaySpecID" name="quantityDisplaySpecName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="specLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintSpecID" name="quantityprintSpecName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="specLabelPrintID" >(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Cost</td>
					<td><input type="checkbox" id="quantityDisplayCostID" name="quantityDisplayCostName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="costLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintCostID" name="quantityprintCostName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled" ><label style="vertical-align: middle;" id="costLabelPrintID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Mult</td>
					<td><input type="checkbox" id="quantityDisplayMultiID" name="quantityDisplayMultiName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="multiLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintMultiID" name="quantityprintMultiName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="multiLabelPrintID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Price</td>
					<td><input type="checkbox" id="quantityDisplayPriceID" name="quantityDisplayPriceName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="priceLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintPriceID" name="quantityprintPriceName" style="vertical-align: middle;" onchange="changeQuantity()" checked="checked" disabled="disabled"><label style="vertical-align: middle;" id="priceLabelPrintID">(Yes)</label></td>
				</tr>
				<tr height="10px;"></tr>
			</table>
			<hr width="310px;">
			<table align="right">
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Inline note full width of page?</label>
						<input type="checkbox" id="inlineNoteFullPageID" name="inlineNoteFullPageName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Automatically Print Line Numbers?</label>
						<input type="checkbox" id="PrintLineNumID" name="PrintLineNumName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Print Summation at Bottom of Quote?</label>
						<input type="checkbox" id="printSummationID" name="printSummationName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Hide detail price and show totaling?</label>
						<input type="checkbox" id="hidePriceID" name="hidePriceName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr height="10px;"></tr>
			</table>
			<table align="right">
				<tr>
				 	<td>
		   				<input type="button" class="printhoverbutton turbo-tan" value="Save" onclick="saveQuoteProperties()" style=" width:80px;color: white;">
						<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuoteProperties()" style="width:80px;color: white;">
		   			</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="quotePropertiesTempDialog">
		<form id="quoteTempPropertiesFormID">
			<table>
				<tr align="left">
					<th style="width:300"><u>Column</u></th>
					<th style="width:170"><u>Display</u></th>
					<th style="width:100"><u>Print</u></th>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Quantity</td>
					<td><input type="checkbox" id="quantityDisplaytempID" name="quantityDisplayName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabeltempID" for="quantityDisplaytempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrinttempID" name="quantityPrintName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelPrinttempID" for="quantityPrinttempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Paragraph</td>
					<td><input type="checkbox" id="quantityDisplayParatempID" name="quantityDisplayParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabeltempID" for="quantityDisplayParatempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintParatempID" name="quantityprintParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelPrinttempID" for="quantityprintParatempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Manufacturer</td>
					<td><input type="checkbox" id="quantityDisplayManuftempID" name="quantityDisplayManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabeltempID" for="quantityDisplayManuftempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintManuftempID" name="quantityPrintManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelPrinttempID" for="quantityPrintManuftempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Spec</td>
					<td><input type="checkbox" id="quantityDisplaySpectempID" name="quantityDisplaySpecName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="specLabeltempID" for="quantityDisplaySpectempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintSpectempID" name="quantityprintSpecName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="specLabelPrinttempID" for="quantityprintSpectempID" >(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Cost</td>
					<td><input type="checkbox" id="quantityDisplayCosttempID" name="quantityDisplayCostName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="costLabeltempID" for="quantityDisplayCosttempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintCosttempID" name="quantityprintCostName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled" ><label style="vertical-align: middle;" id="costLabelPrinttempID" for="quantityprintCosttempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Mult</td>
					<td><input type="checkbox" id="quantityDisplayMultitempID" name="quantityDisplayMultiName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="multiLabeltempID" for="quantityDisplayMultitempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintMultitempID" name="quantityprintMultiName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="multiLabelPrinttempID" for="quantityprintMultitempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Price</td>
					<td><input type="checkbox" id="quantityDisplayPricetempID" name="quantityDisplayPriceName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="priceLabeltempID" for="quantityDisplayPricetempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintPricetempID" name="quantityprintPriceName" style="vertical-align: middle;" onchange="changeQuantity()" checked="checked" disabled="disabled"><label style="vertical-align: middle;" id="priceLabelPrinttempID" for="quantityprintPricetempID">(Yes)</label></td>
				</tr>
				<tr height="10px;"></tr>
			</table>
			<hr width="310px;">
			<table align="right">
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Inline note full width of page?</label>
						<input type="checkbox" id="inlineNoteFullPagetempID" name="inlineNoteFullPageName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Automatically Print Line Numbers?</label>
						<input type="checkbox" id="PrintLineNumtempID" name="PrintLineNumName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Print Summation at Bottom of Quote?</label>
						<input type="checkbox" id="printSummationtempID" name="printSummationName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr>
					<td style="float: right;">
						<label  style="vertical-align: middle;">Hide detail price and show totaling?</label>
						<input type="checkbox" id="hidePricetempID" name="hidePriceName"  style="vertical-align: middle;">
					</td>
				</tr>
				<tr height="10px;"></tr>
			</table>
			<table align="right">
				<tr>
		   			<td>
		   				<input type="button" id="quoteTempSaveButton" class="printhoverbutton turbo-tan" value="Save" onclick="saveQuoteTemplateProperties()" style=" width:80px;color: white;">
						<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuoteProperties()" style="width:80px;color: white;">
		   			</td>
				</tr>
			</table>
		</form>
	</div>	
	<div style="display: none;">
		<table>
			<tr>
				<td>Quantity</td>
				<td><input type="text" id="quantityValueDisplayID"></td>
				<td><input type="text" id="quantityValuePrintID"></td>
			</tr>
			<tr>
				<td>Paragraph</td>
				<td><input type="text" id="quantityValueDisplayParaID"></td>
				<td><input type="text" id="quantityValuePrintParaID"></td>
			</tr>
			<tr>
				<td>Manufacturer</td>
				<td><input type="text" id="quantityValueDisplayManufID">
				<td><input type="text" id="quantityValuePrintManufID"></td>
			</tr>
			<tr>
				<td>Spec</td>
				<td><input type="text" id="quantityValueDisplaySpecID">
				<td><input type="text" id="quantityValuePrintSpecID"></td>
			</tr>
			<tr>
				<td>Cost</td>
				<td><input type="text" id="quantityValueprintCostID">
				<td><input type="text" id="quantityValuePrintCostID"></td>
			</tr>
			<tr>
				<td>Multi</td>
				<td><input type="text" id="quantityValueDispalyMultiID">
				<td><input type="text" id="quantityValuePrintMultiID"></td>
			</tr>
			<tr>
				<td>Price</td>
				<td><input type="text" id="quantityValueDisplayPriceID">
				<td><input type="text" id="quantityValuePrintPriceID"></td>
			</tr>
			<tr>
				<td>NotePage</td>
				<td><input type="text" id="inlineNotePage"></td>
			</tr>
			<tr>
				<td>Print Line</td>
				<td><input type="text" id="printLineID"></td>
			</tr>
			<tr>
				<td>Print Summation at Bottom<td>
				<td><input type="text" id="summationID"></td>
			</tr>
			<tr>
				<td>show totaling</td>
				<td><input type="text" id="showTotallingID"></td>
			</tr>
		</table>
	</div>
 	<!-- 	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jQuery_jqprint_0.3.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/addQuotes.js"></script>
   <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/addQuotes.min.js"></script>   -->