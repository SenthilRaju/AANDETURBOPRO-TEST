<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>

.gridbold {
 font-weight: 600;
font-family: "Arial Black", Arial, sans-serif;
}
.griditalic {
font-style:italic;
}
.gridunderline {
    text-decoration: underline;
}
</style>
	<div id="addquotes">
	<div id="addquotesLineItem">
		<form action="" id="quoteManipulateForm">
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>New Quote for Job: </b>${requestScope.joMasterDetails.description}</label></legend>
			<table style="width:1000px">
				<tr>
					<td style="width: 35px;"><label>Type:</label></td>
					<td>
						<select style="width:170px; margin-left: 3px; border:#DDBA82;" id="quoteTypeDetail" name="quoteTypeDetailName">
							<option value="-1"> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
					<td style="width: 60px;"><label>Revision<span style="color: red;"></span>:</label></td>
					<td><input type="text" style="width:75px" id="jobQuoteRevision" name="jobQuoteRevision" class="validate[custom[number]]" maxlength="3">
					<span style="color:red;" id="revisionErrorMsg"></span>
					</td>
					
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
			<table id="addquotesList"></table>  <div id="addquotespager"></div>
			<table align="left">
				<tr>
					<td align="center" colspan="2"><div id="quoteAddMsg" style="display: none"></div></td>
				</tr>
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:25px;height:25px;background:#EEDEBC;">
			            	<table>
			            		<tr>
					            	<!-- <td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="#" style="background: #EEDEBC"></td> -->
					            	<td align="right" style="display:none;padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="addQuoteFrom()" style="background: #EEDEBC"></td>
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
					<th style="width:170"><u>Print</u></th>
					<th style="width:170"><u>Underline</u></th>
					<th style="width:170"><u>Bold</u></th>
				</tr>
				<tr height="5px;"></tr>
				<!-- <tr>				
					<td>Select All</td>					
					<td><input type="checkbox"  style="vertical-align: middle;" onchange="selectall('display')"><input type="hidden" name="selectalldisplay" id="selectalldisplay" value="true"/></td>
					<td><input type="checkbox"  style="vertical-align: middle;" onchange="selectall('print')"><input type="hidden" name="selectallPrint" id="selectallPrint" value="true"/></td>
					<td><input type="checkbox"  style="vertical-align: middle;" onchange="selectall('underline')"> <input type="hidden" name="selectallUnderline" id="selectallUnderline" value="true"/></td>
					<td><input type="checkbox"  style="vertical-align: middle;" onchange="selectall('bold')"> <input type="hidden" name="selectallBold" id="selectallBold" value="true"/></td>
				</tr> -->
				<tr>				 
					<td>Header</td>					
					<td><input type="checkbox" id="quantityDisplayHeaderID" name="quantityDisplayHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintHeaderID" name="quantityPrintHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineHeaderID" name="quantityUnderlineHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldHeaderID" name="quantityBoldHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelBoldID">(No)</label></td>
				</tr>
				<tr>				
					<td><label id="column1Label">Item</label></td>					
					<td><input type="checkbox" id="quantityDisplayItemID" name="quantityDisplayItemName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="itemLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintItemID" name="quantityPrintItemName" style="vertical-align: middle;" onchange="changeQuantity()" ><label style="vertical-align: middle;" id="itemLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineItemID" name="quantityUnderlineItemName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" ><label style="vertical-align: middle;display:none;" id="itemLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldItemID" name="quantityBoldItemName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="itemLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column2Label">Quantity</label></td>
					<td><input type="checkbox" id="quantityDisplayID" name="quantityDisplayName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintID" name="quantityPrintName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineID" name="quantityUnderlineName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="quantityLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldID" name="quantityBoldName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="quantityLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column3Label">Paragraph</label></td>
					<td><input type="checkbox" id="quantityDisplayParaID" name="quantityDisplayParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintParaID" name="quantityprintParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineParaID" name="quantityUnderlineParaName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="paraLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldParaID" name="quantityBoldParaName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="paraLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column4Label">Manufacturer</label></td>
					<td><input type="checkbox" id="quantityDisplayManufID" name="quantityDisplayManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintManufID" name="quantityPrintManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineManufID" name="quantityUnderlineManufName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="manufatuereLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldManufID" name="quantityBoldManufName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="manufatuereLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column5Label">Spec</label></td>
					<td><input type="checkbox" id="quantityDisplaySpecID" name="quantityDisplaySpecName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="specLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintSpecID" name="quantityprintSpecName" style="vertical-align: middle;" onchange="changeQuantity()" ><label style="vertical-align: middle;" id="specLabelPrintID" >(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineSpecID" name="quantityUnderlineSpecName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="specLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldSpecID" name="quantityBoldSpecName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="specLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column6Label">Cost</label></td>
					<td><input type="checkbox" id="quantityDisplayCostID" name="quantityDisplayCostName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="costLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintCostID" name="quantityprintCostName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="costLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineCostID" name="quantityUnderlineCostName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;display:none;" id="costLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldCostID" name="quantityBoldCostName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;display:none;" id="costLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column7Label">Mult</label></td>
					<td><input type="checkbox" id="quantityDisplayMultiID" name="quantityDisplayMultiName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="multiLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintMultiID" name="quantityprintMultiName" style="vertical-align: middle;" onchange="changeQuantity()" ><label style="vertical-align: middle;" id="multiLabelPrintID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineMultiID" name="quantityUnderlineMultiName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="multiLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldMultiID" name="quantityBoldMultiName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="multiLabelBoldID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td><label id="column8Label">Price</label></td>
					<td><input type="checkbox" id="quantityDisplayPriceID" name="quantityDisplayPriceName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="priceLabelID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintPriceID" name="quantityprintPriceName" style="vertical-align: middle;" onchange="changeQuantity()" ><label style="vertical-align: middle;" id="priceLabelPrintID">(Yes)</label></td>
					<td><input type="checkbox" id="quantityUnderlinePriceID" name="quantityUnderlinePriceName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="priceLabelUnderlineID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldPriceID" name="quantityBoldPriceName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="priceLabelBoldID">(No)</label></td>
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
					<th style="width:170"><u>Print</u></th>
					<th style="width:170"><u>Underline</u></th>
					<th style="width:170"><u>Bold</u></th>
				</tr>
				<tr height="5px;"></tr>
				<tr>				
					<td>Header</td>					
					<td><input type="checkbox" id="quantityDisplayHeadertempID" name="quantityDisplayHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabeltempID" for="quantityDisplayHeadertempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintHeadertempID" name="quantityPrintHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelPrinttempID" for="quantityPrintHeadertempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineHeadertempID" name="quantityUnderlineHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelUnderlinetempID" for="quantityUnderlineHeadertempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldHeadertempID" name="quantityBoldHeaderName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="headerLabelBoldtempID" for="quantityBoldHeadertempID">(No)</label></td>
				</tr>
				<tr>
					<td>Item</td>
					<td><input type="checkbox" id="quantityDisplayItemtempID" name="quantityDisplayItemName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="itemLabeltempID" for="quantityDisplayItemtempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintItemtempID" name="quantityPrintItemName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="itemLabelPrinttempID" for="quantityPrintItemtempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineItemtempID" name="quantityUnderlineItemName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="itemLabelUnderlinetempID" for="quantityUnderlineItemtempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldItemtempID" name="quantityBoldItemName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="itemLabelBoldtempID" for="quantityBoldItemtempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Quantity</td>
					<td><input type="checkbox" id="quantityDisplaytempID" name="quantityDisplayName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabeltempID" for="quantityDisplaytempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrinttempID" name="quantityPrintName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="quantityLabelPrinttempID" for="quantityPrinttempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlinetempID" name="quantityUnderlineName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="quantityLabelUnderlinetempID" for="quantityUnderlinetempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldtempID" name="quantityBoldName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="quantityLabelBoldtempID" for="quantityBoldtempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Paragraph</td>
					<td><input type="checkbox" id="quantityDisplayParatempID" name="quantityDisplayParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabeltempID" for="quantityDisplayParatempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintParatempID" name="quantityprintParaName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="paraLabelPrinttempID" for="quantityprintParatempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineParatempID" name="quantityUnderlineParaName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="paraLabelUnderlinetempID" for="quantityUnderlineParatempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldParatempID" name="quantityBoldParaName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="paraLabelBoldtempID" for="quantityBoldParatempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Manufacturer</td>
					<td><input type="checkbox" id="quantityDisplayManuftempID" name="quantityDisplayManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabeltempID" for="quantityDisplayManuftempID">(No)</label></td>
					<td><input type="checkbox" id="quantityPrintManuftempID" name="quantityPrintManufName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="manufatuereLabelPrinttempID" for="quantityPrintManuftempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineManuftempID" name="quantityUnderlineManufName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="manufatuereLabelUnderlinetempID" for="quantityUnderlineManuftempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldManuftempID" name="quantityBoldManufName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="manufatuereLabelBoldtempID" for="quantityBoldManuftempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Spec</td>
					<td><input type="checkbox" id="quantityDisplaySpectempID" name="quantityDisplaySpecName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="specLabeltempID" for="quantityDisplaySpectempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintSpectempID" name="quantityprintSpecName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="specLabelPrinttempID" for="quantityprintSpectempID" >(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineSpectempID" name="quantityUnderlineSpecName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="specLabelUnderlinetempID" for="quantityUnderlineSpectempID" >(No)</label></td>
					<td><input type="checkbox" id="quantityBoldSpectempID" name="quantityBoldSpecName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="specLabelBoldtempID" for="quantityBoldSpectempID" >(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Cost</td>
					<td><input type="checkbox" id="quantityDisplayCosttempID" name="quantityDisplayCostName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="costLabeltempID" for="quantityDisplayCosttempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintCosttempID" name="quantityprintCostName" style="vertical-align: middle;" onchange="changeQuantity()" disabled="disabled"><label style="vertical-align: middle;" id="costLabelPrinttempID" for="quantityprintCosttempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineCosttempID" name="quantityUnderlineCostName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" ><label style="vertical-align: middle;display:none;" id="costLabelUnderlinetempID" for="quantityUnderlineCosttempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldCosttempID" name="quantityBoldCostName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" ><label style="vertical-align: middle;display:none;" id="costLabelBoldtempID" for="quantityBoldCosttempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Mult</td>
					<td><input type="checkbox" id="quantityDisplayMultitempID" name="quantityDisplayMultiName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="multiLabeltempID" for="quantityDisplayMultitempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintMultitempID" name="quantityprintMultiName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="multiLabelPrinttempID" for="quantityprintMultitempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlineMultitempID" name="quantityUnderlineMultiName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="multiLabelUnderlinetempID" for="quantityUnderlineMultitempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldMultitempID" name="quantityBoldMultiName" style="vertical-align: middle;display:none;" onchange="changeQuantity()"><label style="vertical-align: middle;display:none;" id="multiLabelBoldtempID" for="quantityBoldMultitempID">(No)</label></td>
				</tr>
				<tr height="5px;"></tr>
				<tr>
					<td>Price</td>
					<td><input type="checkbox" id="quantityDisplayPricetempID" name="quantityDisplayPriceName" style="vertical-align: middle;" onchange="changeQuantity()"><label style="vertical-align: middle;" id="priceLabeltempID" for="quantityDisplayPricetempID">(No)</label></td>
					<td><input type="checkbox" id="quantityprintPricetempID" name="quantityprintPriceName" style="vertical-align: middle;" onchange="changeQuantity()" checked="checked"><label style="vertical-align: middle;" id="priceLabelPrinttempID" for="quantityprintPricetempID">(No)</label></td>
					<td><input type="checkbox" id="quantityUnderlinePricetempID" name="quantityUnderlinePriceName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" checked="checked"><label style="vertical-align: middle;display:none;" id="priceLabelUnderlinetempID" for="quantityUnderlinePricetempID">(No)</label></td>
					<td><input type="checkbox" id="quantityBoldPricetempID" name="quantityBoldPriceName" style="vertical-align: middle;display:none;" onchange="changeQuantity()" checked="checked"><label style="vertical-align: middle;display:none;" id="priceLabelBoldtempID" for="quantityBoldPricetempID">(No)</label></td>
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
				<td>Header</td>
				<td><input type="text" id="quantityValueDisplayHeaderID"></td>
				<td><input type="text" id="quantityValuePrintHeaderID"></td>
				<td><input type="text" id="quantityValueUnderlineHeaderID"></td>
				<td><input type="text" id="quantityValueBoldHeaderID"></td>
			</tr>
		<tr>
				<td>Item</td>
				<td><input type="text" id="quantityValueDisplayItemID"></td>
				<td><input type="text" id="quantityValuePrintItemID"></td>
				<td><input type="text" id="quantityValueUnderlineItemID"></td>
				<td><input type="text" id="quantityValueBoldItemID"></td>
			</tr>
			<tr>
				<td>Quantity</td>
				<td><input type="text" id="quantityValueDisplayID"></td>
				<td><input type="text" id="quantityValuePrintID"></td>
				<td><input type="text" id="quantityValueUnderlineID"></td>
				<td><input type="text" id="quantityValueBoldID"></td>
			</tr>
			<tr>
				<td>Paragraph</td>
				<td><input type="text" id="quantityValueDisplayParaID"></td>
				<td><input type="text" id="quantityValuePrintParaID"></td>
				<td><input type="text" id="quantityValueUnderlineParaID"></td>
				<td><input type="text" id="quantityValueBoldParaID"></td>
			</tr>
			<tr>
				<td>Manufacturer</td>
				<td><input type="text" id="quantityValueDisplayManufID">
				<td><input type="text" id="quantityValuePrintManufID"></td>
				<td><input type="text" id="quantityValueUnderlineManufID"></td>
				<td><input type="text" id="quantityValueBoldManufID"></td>
			</tr>
			<tr>
				<td>Spec</td>
				<td><input type="text" id="quantityValueDisplaySpecID">
				<td><input type="text" id="quantityValuePrintSpecID"></td>
				<td><input type="text" id="quantityValueUnderlineSpecID"></td>
				<td><input type="text" id="quantityValueBoldSpecID"></td>
			</tr>
			<tr>
				<td>Cost</td>
				<td><input type="text" id="quantityValueDisplayCostID">
				<td><input type="text" id="quantityValuePrintCostID"></td>
				<td><input type="text" id="quantityValueUnderlineCostID"></td>
				<td><input type="text" id="quantityValueBoldCostID"></td>
			</tr>
			<tr>
				<td>Multi</td>
				<td><input type="text" id="quantityValueDispalyMultiID">
				<td><input type="text" id="quantityValuePrintMultiID"></td>
				<td><input type="text" id="quantityValueUnderlineMultiID"></td>
				<td><input type="text" id="quantityValueBoldMultiID"></td>
			</tr>
			<tr>
				<td>Price</td>
				<td><input type="text" id="quantityValueDisplayPriceID">
				<td><input type="text" id="quantityValuePrintPriceID"></td>
				<td><input type="text" id="quantityValueUnderlinePriceID"></td>
				<td><input type="text" id="quantityValueBoldPriceID"></td>
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
	<div ><jsp:include page="./LineItemProperties.jsp"></jsp:include></div>
 	<!-- 	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jQuery_jqprint_0.3.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/addQuotes.js"></script>
   <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/addQuotes.min.js"></script>   -->
   
   <script>
   var newDialogDiv = jQuery(document.createElement('div'));
   $(document).ready(function(){
	  

	   jQuery("#LineItemProperties").dialog({
			autoOpen : false,
			height : 400,
			width : 350,
			title : "Properties",
			modal : true,
			buttons : {		
				'Save':function(){
					savequotelineitemproperties();
					},
				'Close':function(){
					$('#LineItemProperties').dialog('close');
					}
			},
			
			close : function() {
				//$(this).dialog("close");
			}
		});
		
	   });

   
 
 
   function addQuotesColumnDetails() {
		col1 == 0, col2 == 0, col3 == 0, col4 == 0, col5 == 0, col6 == 0, col7 == 0,col9=0,col10=0;
		var colName_description=col1lablel;
		var colModel_description={
			name : 'product',
			index : 'product',
			align : 'left',
			width : 90,
			editable : true,
			hidden : false,
			edittype : 'text',
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['itemClassName'];
			        return " class='"+classname+"'";
			},
			editoptions : {
				dataInit : function(elem) {
					$(elem)
							.autocomplete(
									{
										source : 'jobtabs2/productList',
										minLength : 3,
										select : function(event, ui) {
											var quoteDetailID = ui.item.id;
											var rxMasterID = ui.item.manufactureID;
											$
													.ajax({
														url : "./jobtabs2/quoteDetails",
														mType : "GET",
														data : {
															'quoteDetailID' : quoteDetailID,
															'rxMasterID' : rxMasterID
														},
														success : function(
																data) {
															$
																	.each(
																			data,
																			function(
																					index,
																					value) {
																				manufacture = value.inlineNote;
																				var detail = value.paragraph;
																				var quantity = value.itemQuantity;
																				var manufactureID = value.rxManufacturerID;
																				var factoryID = value.detailSequenceId;
																				$(
																						"#new_row_paragraph")
																						.val(
																								detail);
																				$(
																						"#new_row_manufacturer")
																						.val(
																								quantity); // $("#itemQuantity").val(quantity);
																				$(
																						"#new_row_rxManufacturerID")
																						.val(
																								manufactureID);
																				$(
																						"#new_row_veFactoryId")
																						.val(
																								factoryID);
																				$(
																						"#paragraph")
																						.val(
																								detail);
																				$(
																						"#manufacturer")
																						.val(
																								quantity);
																				$(
																						"#rxManufacturerID")
																						.val(
																								manufactureID);
																				$(
																						"#veFactoryId")
																						.val(
																								factoryID);
																			});
														}
													});
										}
									});
				}
			},
			editrules : {
				edithidden : false,
				required : true
			}
		};
		var colModel_Hidden_description=
		{
			name : 'product',
			index : 'product',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				dataInit : function(elem) {
					$(elem)
							.autocomplete(
									{
										source : 'jobtabs2/productList',
										minLength : 3,
										select : function(event, ui) {
											var quoteDetailID = ui.item.id;
											var rxMasterID = ui.item.manufactureID;
											$
													.ajax({
														url : "./jobtabs2/quoteDetails",
														mType : "GET",
														data : {
															'quoteDetailID' : quoteDetailID,
															'rxMasterID' : rxMasterID
														},
														success : function(
																data) {
															$
																	.each(
																			data,
																			function(
																					index,
																					value) {
																				manufacture = value.inlineNote;
																				var detail = value.paragraph;
																				var quantity = value.itemQuantity;
																				var manufactureID = value.rxManufacturerID;
																				var factoryID = value.detailSequenceId;
																				$(
																						"#new_row_paragraph")
																						.val(
																								detail);
																				$(
																						"#new_row_manufacturer")
																						.val(
																								quantity); // $("#itemQuantity").val(quantity);
																				$(
																						"#new_row_rxManufacturerID")
																						.val(
																								manufactureID);
																				$(
																						"#new_row_veFactoryId")
																						.val(
																								factoryID);
																				$(
																						"#paragraph")
																						.val(
																								detail);
																				$(
																						"#manufacturer")
																						.val(
																								quantity);
																				$(
																						"#rxManufacturerID")
																						.val(
																								manufactureID);
																				$(
																						"#veFactoryId")
																						.val(
																								factoryID);
																			});
														}
													});
										}
									});
				}
			},
			editrules : {
				edithidden : false,
				required : true
			}
		};
		
		/* var colName_qty = 'Qty.'; */
		var colName_qty = col2lablel;
		var colModel_qty = {
			name : 'itemQuantity',
			index : 'itemQuantity',
			align : 'center',
			width : 30,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['qtyClassName'];
			        return " class='"+classname+"'";
			}
		};

		var colModel_Hidden_qty={
				name : 'itemQuantity',
				index : 'itemQuantity',
				align : 'center',
				width : 30,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {
					edithidden : false,
					required : false
				}
			};
		/* var colName_Paragraph = 'Paragraph'; */
		var colName_Paragraph = col3lablel;
		var colModel_Paragraph = {
			name : 'paragraph',
			index : 'paragraph',
			align : 'left',
			width : 90,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['paraClassName'];
			        return " class='"+classname+"'";
			}
		};
		var colModel_Hidden_Paragraph = {
				name : 'paragraph',
				index : 'paragraph',
				align : 'left',
				width : 90,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {
					edithidden : false,
					required : false
				}
			};
		/* var colName_Vendors = 'Vendors.'; */
		var colName_Vendors = col4lablel;
		var colModel_Vendors = {
			name : 'manufacturer',
			index : 'manufacturer',
			align : 'left',
			width : 90,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				dataInit : function(elem) {
					$(elem).autocomplete({
						source : 'jobtabs2/vendorsList',
						minLength : 2,
						select : function(event, ui) {
							var id = ui.item.id;
							var name = ui.item.value;
							$("#new_row_rxManufacturerID").val(id);
							$("#" + curId123 + "_rxManufacturerID").val(id);
							$("#rxManufacturerID").val(id);
							$.ajax({
								url : "./jobtabs2/getFactoryID",
								type : "GET",
								data : {
									'rxMasterID' : id,
									'descripition' : name
								},
								success : function(data) {
									$("#new_row_veFactoryId").val(data);
									$("#veFactoryId").val(data);
								}
							});
						}
					});
				}
			},
			editrules : {
				edithidden : false,
				required : true
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['manfcClassName'];
			        return " class='"+classname+"'";
			}
		};
		var colModel_Hidden_Vendors = {
			name : 'manufacturer',
			index : 'manufacturer',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				dataInit : function(elem) {
					$(elem).autocomplete({
						source : 'jobtabs2/vendorsList',
						minLength : 3,
						select : function(event, ui) {
							var id = ui.item.id;
							var name = ui.item.value;
							$("#new_row_rxManufacturerID").val(id);

							$("#rxManufacturerID").val(id);
							$.ajax({
								url : "./jobtabs2/getFactoryID",
								type : "GET",
								data : {
									'rxMasterID' : id,
									'descripition' : name
								},
								success : function(data) {
									$("#new_row_veFactoryId").val(data);
									$("#veFactoryId").val(data);
								}
							});
						}
					});
				}
			},
			editrules : {
				edithidden : false,
				required : true
			}
		};
		
		/* var colName_Spec = 'Spec.'; */
		var colName_Spec=col5lablel;
		var colModel_Spec = {
			name : 'spec',
			index : 'spec',
			align : 'right',
			width : 20,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['specClassName'];
			        return " class='"+classname+"'";
			}
		};
		var colModel_Hidden_Spec = {
				name : 'spec',
				index : 'spec',
				align : 'right',
				width : 20,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {
					edithidden : false,
					required : false
				}
			};
		
		/* var colName_Cost = 'Cost.'; */
		var colName_Cost = col6lablel;
		var colModel_Cost = {
			name : 'cost',
			index : 'cost',
			align : 'right',
			width : 50,
			editable : true,
			hidden : false,
			formatter : customCurrencyFormatterCost,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		};
		var colName_CostHidden = col6lablel;
		var colModel_CostHidden = {
			name : 'cost',
			index : 'cost',
			align : 'right',
			width : 50,
			editable : true,
			hidden : true,
			formatter : customCurrencyFormatterCost,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		};
		
		/* var colName_Multi = 'Multi.'; */
		var colName_Multi = col7lablel;
		var colModel_Multi = {
			name : 'mult',
			index : 'mult',
			align : 'right',
			width : 20,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['multClassName'];
			        return " class='"+classname+"'";
			}
		};
		var colModel_Hidden_Multi = {
				name : 'mult',
				index : 'mult',
				align : 'right',
				width : 20,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {
					edithidden : false,
					required : false
				}
			};
		
		var colName_Percentage = 'Percentage';
		var colModel_Percentage = {
			name : 'percentage',
			index : 'percentage',
			align : 'right',
			width : 50,
			editable : true,
			hidden : false,
			edittype : 'text',
			editoptions : {
				size : 30,
				maxlengh : 2
			},
			editrules : {
				edithidden : false,
				required : false
			}
		};
		var colModel_Hidden_Percentage = {
				name : 'percentage',
				index : 'percentage',
				align : 'right',
				width : 50,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30,
					maxlengh : 2
				},
				editrules : {
					edithidden : false,
					required : false
				}
			};
		
		/* var colName_SP = 'Sell Price.'; */
		var colName_SP = col8lablel;
		var colModel_SP = {
			name : 'price',
			index : 'price',
			align : 'right',
			width : 50,
			editable : true,
			hidden : false,
			formatter : customCurrencyFormatterCost,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			},
			cellattr: function(rowId, val, rawObject) {
				var classname=rawObject['priceClassName'];
			        return " class='"+classname+"'";
			}
		};
		
		var colName_SPHidden = 'Sell Price.';
		var colModel_SPHidden = {
			name : 'price',
			index : 'price',
			align : 'right',
			width : 50,
			editable : true,
			hidden : true,
			formatter : customCurrencyFormatter,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		};
		var colPosition = colnamesaddQuotes.length;
		colnamesaddQuotes === [];
		colModeladdQuotes === [];
		colnamesaddQuotes = [ '' ,'','','','','','','',''];
		colnamesDefaults = [ 'QuoteDetailID', 'ManufacturerID', 'QuoteHeaderID',
				'FactoryID', 'LineItem', 'FootLine', 'Posiion', 'Move', 'Options' ];
		colModeladdQuotes = [
				{
					name : 'inLineNoteImage',
					index : 'inLineNoteImage',
					align : 'right',
					width : 15,
					editable : false,
					hidden : false,
					formatter : PropertyImage
				},
				{
					name : 'inLineNoteImage',
					index : 'inLineNoteImage',
					align : 'right',
					width : 10,
					editable : false,
					hidden : false,
					formatter : inlineImage
				} ,
				{
					name : 'itemClassName',
					index : 'itemClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'qtyClassName',
					index : 'qtyClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'paraClassName',
					index : 'paraClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'manfcClassName',
					index : 'manfcClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'specClassName',
					index : 'specClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'multClassName',
					index : 'multClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				}  ,
				{
					name : 'priceClassName',
					index : 'priceClassName',
					align : 'right',
					width : 10,
					editable : false,
					hidden : true
				} 
			


				];
		var columndefaults = [ {
			name : 'joQuoteDetailID',
			index : 'joQuoteDetailID',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'rxManufacturerID',
			index : 'rxManufacturerID',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'joQuoteHeaderID',
			index : 'joQuoteHeaderID',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'veFactoryId',
			index : 'veFactoryId',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'inlineNote',
			index : 'inlineNote',
			align : 'left',
			width : 90,
			editable : false,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'productNote',
			index : 'productNote',
			align : 'left',
			width : 90,
			editable : false,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'position',
			index : 'position',
			align : 'left',
			width : 90,
			editable : true,
			hidden : true,
			edittype : 'text',
			editoptions : {
				size : 30
			},
			editrules : {
				edithidden : false,
				required : false
			}
		}, {
			name : 'upAndDown',
			index : 'upAndDown',
			align : 'left',
			width : 30,
			formatter : upAndDownImage,
			hidden : true
		}, {
			name : 'edit',
			index : 'edit',
			align : 'center',
			width : 50,
			hidden : false,
			editable : false,
			editrules : {
				edithidden : false
			},
			formatter : imgFmatter
		} ];

		
		$.ajax({
					url : "./jobtabs2/getQuotePropertyIdOfUser",
					mType : "GET",
					async: false, 
					success : function(data) {
						dataCol === data;
						//if (data.displayQuantity) {
							/*
							 * alert("Quantity: "+data.displayQuantity+"\nParagraph:
							 * "+data.displayParagraph+"\nManufacturer: "+
							 * data.displayManufacturer+"\nDisplayMult:
							 * "+data.displayMult+" \nDisplaySpec:
							 * "+data.displaySpec+ "\nPrice:
							 * "+data.displayPrice+"\nDisplayCost:
							 * "+data.displayCost);
							 */
							$("#quantityValueDisplayID").val(data.displayQuantity);
							$("#quantityValueDisplayParaID").val(
									data.displayParagraph);
							$("#quantityValueDisplayManufID").val(
									data.displayManufacturer);
							$("#quantityValueDispalyMultiID").val(data.displayMult);
							$("#quantityValueDisplaySpecID").val(data.displaySpec);
							$("#quantityValueDisplayPriceID")
									.val(data.displayPrice);
							$("#quantityValuePrintID").val(data.printQuantity);
							$("#quantityValuePrintParaID").val(data.printParagraph);
							$("#quantityValuePrintManufID").val(data.printManufacturer);
							$("#quantityValuePrintSpecID").val(data.printSpec);
							$("#quantityValueDisplayCostID").val(data.displayCost);
							$("#quantityValuePrintCostID").val(data.printCost);
									
							$('#quantityValuePrintMultiID').val(data.printMult);

							console.log($("#quantityValuePrintMultiID").val());
						
							$("#quantityValueDisplayMultiID").val(data.displayMult);
							$("#quantityValuePrintPriceID").val(data.printPrice);

							$("#quantityValueUnderlineID").val(
									data.underlineQuantity);
							$("#quantityValueBoldID").val(data.boldQuantity);

							$("#quantityValueUnderlineParaID").val(
									data.underlineParagraph);
							$("#quantityValueBoldParaID").val(data.boldParagraph);

							$("#quantityValueUnderlineManufID").val(
									data.underlineManufacturer);
							$("#quantityValueBoldManufID").val(
									data.boldManufacturer);

							$("#quantityValueUnderlineSpecID").val(
									data.underlineSpec);
							$("#quantityValueBoldSpecID").val(data.boldSpec);

							$("#quantityValueUnderlineCostID").val(
									data.underlineCost);
							$("#quantityValueBoldCostID").val(data.boldCost);

							$("#quantityValueUnderlineMultiID").val(
									data.underlineMult);
							$("#quantityValueBoldMultiID").val(data.boldMult);

							$("#quantityValueUnderlinePriceID").val(
									data.underlinePrice);
							$("#quantityValueBoldPriceID").val(data.boldPrice);
							$("#quantityValueDisplayItemID").val(data.displayItem);
							$("#quantityValuePrintItemID").val(data.printItem);
							$("#quantityValueUnderlineItemID").val(
									data.underlineItem);
							$("#quantityValueBoldItemID").val(data.boldItem);

							$("#quantityValueDisplayHeaderID").val(
									data.displayHeader);
							$("#quantityValuePrintHeaderID").val(data.printHeader);
							$("#quantityValueUnderlineHeaderID").val(
									data.underlineHeader);
							$("#quantityValueBoldHeaderID").val(data.boldHeader);

							$("#inlineNotePage").val(data.notesFullWidth);
							$("#printLineID").val(data.lineNumbers);
							$("#summationID").val(data.printTotal);
							$("#showTotallingID").val(data.hidePrice);
							col1 = data.displayQuantity;
							col2 = data.displayParagraph;
							col3 = data.displayManufacturer;
							col4 = data.displaySpec;
							col5 = data.displayCost;
							col6 = data.displayMult;
							col8 = data.displayPrice;
							col9 = data.displayItem;
							col10=data.displayHeader;
							//alert("display"+data.displayItem);
							// console.log("Columnns"+col1, col2, col3, col4, col5, col6, col7+"==");
						/* } else {
							$('#joQuotePropertyID').val(0);
							colnamesaddQuotes.push(colName_Vendors);
							colModeladdQuotes.push(colModel_Hidden_Vendors);
							colnamesaddQuotes.push(colName_Cost);
							colModeladdQuotes.push(colModel_CostHidden);
							colnamesaddQuotes.push(colName_SPHidden);
							colModeladdQuotes.push(colModel_SPHidden);
						} */
					}
				});
		var checkbooleantype=false;
		for(var i=1;i<9;i++){
		if(tempprodlbl==col1columnname && col1ordnum==i){	
		if(col9==1){ 
			if(col10!=1){
				colName_description="";
				}
			colnamesaddQuotes.push(colName_description);
			colModeladdQuotes.push(colModel_description);
			colPosition = colPosition + 1;
			checkbooleantype=true;
		}else{
			colnamesaddQuotes.push(colName_description);
			colModeladdQuotes.push(colModel_Hidden_description);
			colPosition = colPosition + 1;
		}
		}
		if(tempqtylbl==col2columnname && col2ordnum==i){
		if (col1 == 1) {
			if(col10!=1){
				colName_qty="";
				}
			colnamesaddQuotes.push(colName_qty);
			colModeladdQuotes.push(colModel_qty);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol11qty=="+colName_qty+"=="+colModeladdQuotes+"=="+colPosition);
		}else{
			colnamesaddQuotes.push(colName_qty);
			colModeladdQuotes.push(colModel_Hidden_qty);
			colPosition = colPosition + 1;
			}
		}
		if(tempparalbl==col3columnname  && col3ordnum==i){
		if (col2 == 1) {
			if(col10!=1){
				colName_Paragraph="";
				}
			colnamesaddQuotes.push(colName_Paragraph);
			colModeladdQuotes.push(colModel_Paragraph);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol21Paragraph=="+colName_Paragraph+"=="+colModel_Paragraph+"=="+colPosition)
		}
		else{
			 
			colnamesaddQuotes.push(colName_Paragraph);
			colModeladdQuotes.push(colModel_Hidden_Paragraph);
			colPosition = colPosition + 1;
			}
		}
		if(tempmanlbl==col4columnname && col4ordnum==i){
		if (col3 == 1) {
			if(col10!=1){
				colName_Vendors="";
				}
			colnamesaddQuotes.push(colName_Vendors);
			colModeladdQuotes.push(colModel_Vendors);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol31colModel_Vendors=="+colName_Vendors+"=="+colModel_Vendors+"=="+colPosition)
		} else {
			colnamesaddQuotes.push(colName_Vendors);
			colModeladdQuotes.push(colModel_Hidden_Vendors);
			colPosition = colPosition + 1;
			console.log("ifcol3elsecolModel_Vendors=="+colName_Vendors+"=="+colModel_Hidden_Vendors+"=="+colPosition);
		}
		}
		if(tempspeclbl==col5columnname && col5ordnum==i){
		if (col4 == 1) {
			if(col10!=1){
				colName_Spec="";
				}
			colnamesaddQuotes.push(colName_Spec);
			colModeladdQuotes.push(colModel_Spec);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol41colName_Spec=="+colName_Spec+"=="+colModel_Spec+"=="+colPosition);
		}else{
			
			colnamesaddQuotes.push(colName_Spec);
			colModeladdQuotes.push(colModel_Hidden_Spec);
			colPosition = colPosition + 1;
			}
		}
		if(tempcostlbl==col6columnname && col6ordnum==i){
		if (col5 == 1) { 
			if(col10!=1){
				colName_Cost="";
				colName_Percentage="";
				}
			colnamesaddQuotes.push(colName_Cost);
			colModeladdQuotes.push(colModel_Cost);
			colPosition = colPosition + 1;
			console.log("ifcol51colName_Cost=="+colName_Cost+"=="+colModel_Cost+"=="+colPosition);
			colnamesaddQuotes.push(colName_Percentage);
			colModeladdQuotes.push(colModel_Percentage);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol51colName_Percentage=="+colName_Percentage+"=="+colModel_Percentage+"=="+colPosition);
		}else{
			colnamesaddQuotes.push(colName_Cost);
			colModeladdQuotes.push(colModel_CostHidden);
			colPosition = colPosition + 1;
			console.log("ifcol50colName_Cost=="+colName_Cost+"=="+colModel_Cost+"=="+colPosition);
			colnamesaddQuotes.push(colName_Percentage);
			colModeladdQuotes.push(colModel_Hidden_Percentage);
			colPosition = colPosition + 1;
			console.log("ifcol50colName_Percentage=="+colName_Percentage+"=="+colModel_Percentage+"=="+colPosition);
		}
		}
		if(tempmultlbl==col7columnname && col7ordnum==i){
		if (col6 == 1) {
			if(col10!=1){
				colName_Multi="";
				}
			colnamesaddQuotes.push(colName_Multi);
			colModeladdQuotes.push(colModel_Multi);
			colPosition = colPosition + 1;
			checkbooleantype=true;
			console.log("ifcol61colName_Multi=="+colName_Multi+"=="+colModel_Multi+"=="+colPosition);
		}else{
			colnamesaddQuotes.push(colName_Multi);
			colModeladdQuotes.push(colModel_Hidden_Multi);
			colPosition = colPosition + 1;
			}
		}
		if(temppricelbl==col8columnname  && col8ordnum==i){
		if (col8 == 1) {
			if(col10!=1){
				colName_SP="";
				}
			colnamesaddQuotes.push(colName_SP);
			colModeladdQuotes.push(colModel_SP);
			checkbooleantype=true;
			console.log("ifcol81colName_SP=="+colName_SP+"=="+colModel_SP+"==");
		}else{
			colnamesaddQuotes.push(colName_SPHidden);
			colModeladdQuotes.push(colModel_SPHidden);
			console.log("ifcol80olName_SPHidden=="+colName_SPHidden+"=="+colModel_SPHidden+"==");
		}
		}

		}
		if(checkbooleantype){
		for (var i = 0; i < colnamesDefaults.length; i++) {
			colnamesaddQuotes.push(colnamesDefaults[i]);
			colModeladdQuotes.push(columndefaults[i]);
		}
		}

	   }

   function selectall(columnname){
	    
	   if(columnname=='display'){
		   
		   var displayselection=$("#selectalldisplay").val();
		   if(displayselection==true){
			   $("#selectalldisplay").val(false);
			   }else{
				   $("#selectalldisplay").val(true);
				}
		   $("#quantityDisplayHeaderID").attr("checked", displayselection);
		   $("#headerLabelID").empty();
		   $("#headerLabelID").append("(Yes)");

		   $("#quantityDisplayItemID").attr("checked", displayselection);
		   $("#itemLabelID").empty();
		   $("#itemLabelID").append("(Yes)");

		   $("#quantityDisplayID").attr("checked", displayselection);
		   $("#quantityLabelID").empty();
		   $("#quantityLabelID").append("(Yes)");

		   $("#quantityDisplayParaID").attr("checked", displayselection);
		   $("#paraLabelID").empty();
		   $("#paraLabelID").append("(Yes)");

		   $("#quantityDisplayManufID").attr("checked", displayselection);
		   $("#manufatuereLabelID").empty();
		   $("#manufatuereLabelID").append("(Yes)");

		   $("#quantityDisplaySpecID").attr("checked", displayselection);
		   $("#specLabelID").empty();
		   $("#specLabelID").append("(Yes)");

		   $("#quantityDisplayCostID").attr("checked", displayselection);
		   $("#costLabelID").empty();
		   $("#manufatuereLabelID").append("(Yes)");

		   $("#quantityDisplayMultiID").attr("checked", displayselection);
		   $("#multiLabelID").empty();
		   $("#multiLabelID").append("(Yes)");

		   $("#quantityDisplayPriceID").attr("checked", displayselection);
		   $("#priceLabelID").empty();
		   $("#priceLabelID").append("(Yes)");

		   }else if(columnname=='print'){ 
			   var printselection=$("#selectallPrint").val();
			   if(printselection==true){
				   $("#selectallPrint").val(false);
				   }else{
					   $("#selectallPrint").val(true);
					}
			   $("#quantityPrintHeaderID").attr("checked", printselection);
			   $("#headerLabelPrintID").empty();
			   $("#headerLabelPrintID").append("(Yes)");

			   $("#quantityPrintItemID").attr("checked", printselection);
			   $("#itemLabelPrintID").empty();
			   $("#itemLabelPrintID").append("(Yes)");

			   $("#quantityPrintID").attr("checked", printselection);
			   $("#quantityLabelPrintID").empty();
			   $("#quantityLabelPrintID").append("(Yes)");

			   $("#quantityPrintParaID").attr("checked", printselection);
			   $("#paraLabelPrintID").empty();
			   $("#paraLabelPrintID").append("(Yes)");
		          
			   $("#quantityPrintManufID").attr("checked", printselection);
			   $("#manufatuereLabelPrintID").empty();
			   $("#manufatuereLabelPrintID").append("(Yes)");

			   $("#quantityPrintSpecID").attr("checked", printselection);
			   $("#specLabelPrintID").empty();
			   $("#specLabelPrintID").append("(Yes)");

			   $("#quantityPrintCostID").attr("checked", printselection);
			   $("#costLabelPrintID").empty();
			   $("#costLabelPrintID").append("(Yes)");

			   $("#quantityPrintMultiID").attr("checked", printselection);
			   $("#multiLabelPrintID").empty();
			   $("#multiLabelPrintID").append("(Yes)");

			   $("#quantityPrintPriceID").attr("checked", printselection);
			   $("#priceLabelPrintID").empty();
			   $("#priceLabelPrintID").append("(Yes)");
			   
			   
			   
			   }
		   else if(columnname=='underline'){
			    
			   var underlineselection=$("#selectallUnderline").val();
			   if(underlineselection==true){
				   $("#selectallUnderline").val(false);
				   }else{
					   $("#selectallUnderline").val(true);
					}
			   $("#quantityUnderlineHeaderID").attr("checked", underlineselection);
			   $("#headerLabelUnderlineID").empty();
			   $("#headerLabelUnderlineID").append("(Yes)");
			   
			   $("#quantityUnderlineItemID").attr("checked", underlineselection);
			   $("#itemLabelUnderlineID").empty();
			   $("#itemLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlineID").attr("checked", underlineselection);
			   $("#quantityLabelUnderlineID").empty();
			   $("#quantityLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlineParaID").attr("checked", underlineselection);
			   $("#paraLabelUnderlineID").empty();
			   $("#paraLabelUnderlineID").append("(Yes)");
		          
			   $("#quantityUnderlineManufID").attr("checked", underlineselection);
			   $("#manufatuereLabelUnderlineID").empty();
			   $("#manufatuereLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlineSpecID").attr("checked", underlineselection);
			   $("#specLabelUnderlineID").empty();
			   $("#specLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlineCostID").attr("checked", underlineselection);
			   $("#costLabelUnderlineID").empty();
			   $("#costLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlineMultiID").attr("checked", underlineselection);
			   $("#multiLabelUnderlineID").empty();
			   $("#multiLabelUnderlineID").append("(Yes)");

			   $("#quantityUnderlinePriceID").attr("checked", underlineselection);
			   $("#priceLabelUnderlineID").empty();
			   $("#priceLabelUnderlineID").append("(Yes)");

			   }else if(columnname=='bold'){
				   
				   var boldselection=$("#selectallBold").val();
				   if(boldselection==true){
					   $("#selectallBold").val(false);
					   }else{
						   $("#selectallBold").val(true);
						}
				   $("#quantityBoldHeaderID").attr("checked", boldselection);
				   $("#headerLabelBoldID").empty();
				   $("#headerLabelBoldID").append("(Yes)");
				   
				   $("#quantityBoldItemID").attr("checked", boldselection);
				   $("#itemLabelBoldID").empty();
				   $("#itemLabelBoldID").append("(Yes)");

				   $("#quantityBoldID").attr("checked", boldselection);
				   $("#quantityLabelBoldID").empty();
				   $("#quantityLabelBoldID").append("(Yes)");

				   $("#quantityBoldParaID").attr("checked", boldselection);
				   $("#paraLabelBoldID").empty();
				   $("#paraLabelBoldID").append("(Yes)");
			          
				   $("#quantityBoldManufID").attr("checked", boldselection);
				   $("#manufatuereLabelBoldID").empty();
				   $("#manufatuereLabelBoldID").append("(Yes)");

				   $("#quantityBoldSpecID").attr("checked", boldselection);
				   $("#specLabelBoldID").empty();
				   $("#specLabelBoldID").append("(Yes)");

				   $("#quantityBoldCostID").attr("checked", boldselection);
				   $("#costLabelBoldID").empty();
				   $("#costLabelBoldID").append("(Yes)");

				   $("#quantityBoldMultiID").attr("checked", boldselection);
				   $("#multiLabelBoldID").empty();
				   $("#multiLabelBoldID").append("(Yes)");

				   $("#quantityBoldPriceID").attr("checked", boldselection);
				   $("#priceLabelBoldID").empty();
				   $("#priceLabelBoldID").append("(Yes)");
				  

				}
	   }
   function PropertyImage(cellValue, options, rowObject) {
	   var lineitemid=rowObject['joQuoteDetailID'];
		var element = "<div><a onclick='editQuoteLineItemFrom("+lineitemid+")'><img src='./../resources/images/QuoteProperties.png' title='Properties' align='middle' style='padding: 2px 5px;'></a></div>";
		return element;
	}
	function editQuoteLineItemFrom(lineitemid){
		if(lineitemid!=null&&lineitemid!=0){
			$("#LineItemPropertyID").val(lineitemid);
			$.ajax({
				url : "./jobtabs2/getjoQLineItemsProp",
				type : "GET",
				async : false,
				data :{'joQuoteDetailID':lineitemid} ,
				success : function(data) {
						if(data!=null){
							checkedCheckboxFromDB(data);
							}else{
								resetvalues();
							}
					
					jQuery("#LineItemProperties").dialog("open");
				}
			});

			
			 

			}else{

				jQuery(newDialogDiv).html('<span style="color:#FF0066;">You cannot set the property before save the line item</span><br><hr style="color: #cb842e;">');
				 jQuery(newDialogDiv).dialog({modal: true, title:"Information",width:420, 
						buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				}
		
				 
		}
	function resetvalues(){

		$("#Italic_ItemID").attr('checked',false);
		$("#UnderLine_ItemID").attr('checked',false);
		$("#Bold_ItemID").attr('checked',false);
		$("#Italic_QuantityID").attr('checked',false);
		$("#UnderLine_QuantityID").attr('checked',false);
		$("#Bold_QuantityID").attr('checked',false);
		$("#Italic_ParagraphID").attr('checked',false);
		$("#UnderLine_ParagraphID").attr('checked',false);
		$("#Bold_ParagraphID").attr('checked',false);
		$("#Italic_ManufacturerID").attr('checked',false);
		$("#UnderLine_ManufacturerID").attr('checked',false);
		$("#Bold_ManufacturerID").attr('checked',false);
		$("#Italic_SpecID").attr('checked',false);
		$("#UnderLine_SpecID").attr('checked',false);
		$("#Bold_SpecID").attr('checked',false);
		$("#Italic_MultID").attr('checked',false);
		$("#UnderLine_MultID").attr('checked',false);
		$("#Bold_MultID").attr('checked',false);
		$("#Italic_PriceID").attr('checked',false);
		$("#UnderLine_PriceID").attr('checked',false);
		$("#Bold_PriceID").attr('checked',false);

		$("#Italic_ItemID").val("false");
		$("#UnderLine_ItemID").val("false");
		$("#Bold_ItemID").val("false");
		$("#Italic_QuantityID").val("false");
		$("#UnderLine_QuantityID").val("false");
		$("#Bold_QuantityID").val("false");
		$("#Italic_ParagraphID").val("false");
		$("#UnderLine_ParagraphID").val("false");
		$("#Bold_ParagraphID").val("false");
		$("#Italic_ManufacturerID").val("false");
		$("#UnderLine_ManufacturerID").val("false");
		$("#Bold_ManufacturerID").val("false");
		$("#Italic_SpecID").val("false");
		$("#UnderLine_SpecID").val("false");
		$("#Bold_SpecID").val("false");
		$("#Italic_MultID").val("false");
		$("#UnderLine_MultID").val("false");
		$("#Bold_MultID").val("false");
		$("#Italic_PriceID").val("false");
		$("#UnderLine_PriceID").val("false");
		$("#Bold_PriceID").val("false");
		
		$("#Italic_ItemlbID").text("(No)");
		$("#UnderLine_ItemlbID").text("(No)");
		$("#Bold_ItemlbID").text("(No)");
		$("#Italic_QuantitylbID").text("(No)");
		$("#UnderLine_QuantitylbID").text("(No)");
		$("#Bold_QuantitylbID").text("(No)");
		$("#Italic_ParagraphlbID").text("(No)");
		$("#UnderLine_ParagraphlbID").text("(No)");
		$("#Bold_ParagraphlbID").text("(No)");
		$("#Italic_ManufacturerlbID").text("(No)");
		$("#UnderLine_ManufacturerlbID").text("(No)");
		$("#Bold_ManufacturerlbID").text("(No)");
		$("#Italic_SpeclbID").text("(No)");
		$("#UnderLine_SpeclbID").text("(No)");
		$("#Bold_SpeclbID").text("(No)");
		$("#Italic_MultlbID").text("(No)");
		$("#UnderLine_MultlbID").text("(No)");
		$("#Bold_MultlbID").text("(No)");
		$("#Italic_PricelbID").text("(No)");
		$("#UnderLine_PricelbID").text("(No)");
		$("#Bold_PricelbID").text("(No)");
		}
	function checkedCheckboxFromDB(data){
		
		resetvalues();
		if(data.italicItem){
			$("#Italic_ItemID").attr('checked',true);
			$("#Italic_ItemlbID").text("(Yes)");
			$("#Italic_ItemID").val("true");
			}
		if(data.underlineItem){
			$("#UnderLine_ItemID").attr('checked',true);
			$("#UnderLine_ItemlbID").text("(Yes)");
			$("#UnderLine_ItemID").val("true");
			
			}	
		if(data.boldItem){
			$("#Bold_ItemID").attr('checked',true);
			$("#Bold_ItemlbID").text("(Yes)");
			$("#Bold_ItemID").val("true");
		}	
		if(data.italicQuantity){
			$("#Italic_QuantityID").attr('checked',true);
			$("#Italic_QuantitylbID").text("(Yes)");
			$("#Italic_QuantityID").val("true");
		}	
		if(data.underlineQuantity){
			$("#UnderLine_QuantityID").attr('checked',true);
			$("#UnderLine_QuantitylbID").text("(Yes)");
			$("#UnderLine_QuantityID").val("true");
		}	
		if(data.boldQuantity){
			$("#Bold_QuantityID").attr('checked',true);
			$("#Bold_QuantitylbID").text("(Yes)");
			$("#Bold_QuantityID").val("true");
		}	
		if(data.italicParagraph){
			$("#Italic_ParagraphID").attr('checked',true);
			$("#Italic_ParagraphlbID").text("(Yes)");
			$("#Italic_ParagraphID").val("true");
		}	
		if(data.underlineParagraph){
			$("#UnderLine_ParagraphID").attr('checked',true);
			$("#UnderLine_ParagraphlbID").text("(Yes)");
			$("#UnderLine_ParagraphID").val("true");
		}	
		if(data.boldParagraph){
			$("#Bold_ParagraphID").attr('checked',true);
			$("#Bold_ParagraphlbID").text("(Yes)");
			$("#Bold_ParagraphID").val("true");
		}	
		if(data.italicManufacturer){
			$("#Italic_ManufacturerID").attr('checked',true);
			$("#Italic_ManufacturerlbID").text("(Yes)");
			$("#Italic_ManufacturerID").val("true");
		}	
		if(data.underlineManufactur){
			$("#UnderLine_ManufacturerID").attr('checked',true);
			$("#UnderLine_ManufacturerlbID").text("(Yes)");
			$("#UnderLine_ManufacturerID").val("true");
			}
		if(data.boldManufacturer){
			$("#Bold_ManufacturerID").attr('checked',true);
			$("#Bold_ManufacturerlbID").text("(Yes)");
			$("#Bold_ManufacturerID").val("true");
			}
		if(data.italicSpec){
			$("#Italic_SpecID").attr('checked',true);
			$("#Italic_SpeclbID").text("(Yes)");
			$("#Italic_SpecID").val("true");
		}
		if(data.underlineSpec){
			$("#UnderLine_SpecID").attr('checked',true);
			$("#UnderLine_SpeclbID").text("(Yes)");
			$("#UnderLine_SpecID").val("true");
		}
		if(data.boldSpec){
			$("#Bold_SpecID").attr('checked',true);
			$("#Bold_SpeclbID").text("(Yes)");
			$("#Bold_SpecID").val("true");
		}
		if(data.italicMult){
			$("#Italic_MultID").attr('checked',true);
			$("#Italic_MultlbID").text("(Yes)");
			$("#Italic_MultID").val("true");
			}
		if(data.underlineMult){
			$("#UnderLine_MultID").attr('checked',true);
			$("#UnderLine_MultlbID").text("(Yes)");
			$("#UnderLine_MultID").val("true");
		}
		if(data.boldMult){
			$("#Bold_MultID").attr('checked',true);
			$("#Bold_MultlbID").text("(Yes)");
			$("#Bold_MultID").val("true");
		}
		if(data.italicPrice){
			$("#Italic_PriceID").attr('checked',true);
			$("#Italic_PricelbID").text("(Yes)");
			$("#Italic_PriceID").val("true");
		}
		if(data.underlinePrice){
			$("#UnderLine_PriceID").attr('checked',true);
			$("#UnderLine_PricelbID").text("(Yes)");
			$("#UnderLine_PriceID").val("true");
		}
		if(data.boldPrice){
			$("#Bold_PriceID").attr('checked',true);
			$("#Bold_PricelbID").text("(Yes)");
			$("#Bold_PriceID").val("true");
			}
		
		
		
		
		
		
		
		

		}

	 function TriggerLabel(checkboxid,labelid){
		  if($("#"+checkboxid).is(':checked')==true){
			  $("#"+labelid).text("(Yes)");
			  $("#"+checkboxid).val("true");
		  }else{
			  $("#"+labelid).text("(No)");
			  $("#"+checkboxid).val("false");
		  }
		  }
	   function savequotelineitemproperties(){
		var lineItemProperties=$("#LineItemPropertyID").val();
		var Italic_ItemID=$("#Italic_ItemID").val();
		var UnderLine_ItemID=$("#UnderLine_ItemID").val();
		var Bold_ItemID=$("#Bold_ItemID").val();
		var Italic_QuantityID=$("#Italic_QuantityID").val();
		var UnderLine_QuantityID=$("#UnderLine_QuantityID").val();
		var Bold_QuantityID=$("#Bold_QuantityID").val();
		var Italic_ParagraphID=$("#Italic_ParagraphID").val();
		var UnderLine_ParagraphID=$("#UnderLine_ParagraphID").val();
		var Bold_ParagraphID=$("#Bold_ParagraphID").val();
		var Italic_ManufacturerID=$("#Italic_ManufacturerID").val();
		var UnderLine_ManufacturerID=$("#UnderLine_ManufacturerID").val();
		var Bold_ManufacturerID=$("#Bold_ManufacturerID").val();
		var Italic_SpecID=$("#Italic_SpecID").val();
		var UnderLine_SpecID=$("#UnderLine_SpecID").val();
		var Bold_SpecID=$("#Bold_SpecID").val();
		var Italic_MultID=$("#Italic_MultID").val();
		var UnderLine_MultID=$("#UnderLine_MultID").val();
		var Bold_MultID=$("#Bold_MultID").val();
		var Italic_PriceID=$("#Italic_PriceID").val();
		var UnderLine_PriceID=$("#UnderLine_PriceID").val();
		var Bold_PriceID=$("#Bold_PriceID").val();
		/* Italic_ItemID UnderLine_ItemID  Bold_ItemID Italic_QuantityID UnderLine_QuantityID Bold_QuantityID
		Italic_ParagraphID UnderLine_ParagraphID Bold_ParagraphID
		
		Italic_ManufacturerID UnderLine_ManufacturerID Bold_ManufacturerID
		Italic_SpecID UnderLine_SpecID Bold_SpecID
		Italic_MultID UnderLine_MultID Bold_MultID 
		Italic_PriceID UnderLine_PriceID Bold_PriceID */
		var JoQLineItemsPropId=0;
		
		var overalldata="JoQLineItemsPropId="+JoQLineItemsPropId+"&LineItemProperties="+lineItemProperties+"&Italic_ItemID="+Italic_ItemID+
						"&UnderLine_ItemID="+UnderLine_ItemID+"&Bold_ItemID="+Bold_ItemID+
						"&Italic_QuantityID="+Italic_QuantityID+"&UnderLine_QuantityID="+UnderLine_QuantityID+"&Bold_QuantityID="+Bold_QuantityID+
						"&Italic_ParagraphID="+Italic_ParagraphID+"&UnderLine_ParagraphID="+UnderLine_ParagraphID+"&Bold_ParagraphID="+Bold_ParagraphID+
						"&Italic_ManufacturerID="+Italic_ManufacturerID+"&UnderLine_ManufacturerID="+UnderLine_ManufacturerID+"&Bold_ManufacturerID="+Bold_ManufacturerID+
						"&Italic_SpecID="+Italic_SpecID+"&UnderLine_SpecID="+UnderLine_SpecID+"&Bold_SpecID="+Bold_SpecID+
						"&Italic_MultID="+Italic_MultID+"&UnderLine_MultID="+UnderLine_MultID+"&Bold_MultID="+Bold_MultID+
						"&Italic_PriceID="+Italic_PriceID+"&UnderLine_PriceID="+UnderLine_PriceID+"&Bold_PriceID="+Bold_PriceID;
		   $.ajax({
				url : "./jobtabs2/LineItemProperties",
				type : "GET",
				async : false,
				data :overalldata ,
				success : function(data) {
					jQuery("#LineItemProperties").dialog("close");
					$("#addquotesList").trigger("reloadGrid");
				}
			});
		 //
		   }
   </script>