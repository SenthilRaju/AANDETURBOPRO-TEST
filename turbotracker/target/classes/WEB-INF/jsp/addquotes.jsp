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
.focus{
	border: 2px solid #000000;
	
}
.ui-jqgrid tr.jqgrow td {
		text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
 .ui-jqgrid tr.jqgrow td { text-overflow: ellipsis !important; }
 
 .checkgreen:checked:after {content: '';
   position: absolute;
   width: 1.2ex;
   height: 0.4ex;
   background: rgba(0, 0, 0, 0);
     
border: 4px solid #09ad7e;
   border-top: none;
   border-right: none;
   -webkit-transform: rotate(-45deg);
   -moz-transform: rotate(-45deg);
   -o-transform: rotate(-45deg);
   -ms-transform: rotate(-45deg);
   transform: rotate(-45deg);
}
.checkgreen:not checked {
    height: 25px;
    width: 25x;
}
</style>
	<div id="addquotes">
	<input type="hidden" name="Quotes_add_edit" id="Quotes_add_edit" />
	<input type="hidden" name="Quotes_editorCopy" id="Quotes_editorCopy" />
	<div class="loadingDivEmailAttachment" id="addquotesloader" style="display: none;opacity: 0.7;background-color: #fff;z-index: 1234;text-align: center;" />
	<div id="addquotesLineItem">
		<form action="" id="quoteManipulateForm">
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>New Quote for Job: </b>${requestScope.joMasterDetails.description}</label></legend>
			<table style="width:1000px">
				
				<tr><td style="width: 19%;"><select style="width:170px; margin-left: 3px; border:#DDBA82;" id="quoteTypeDetail" name="quoteTypeDetailName"  onchange="validatequotedisabledbuttons()">
							<option value="-1"> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select></td>
					<td style="width:10%;"><label>Revision<span style="color: red;"></span>:</label><input  type="text"  id="jobQuoteRevision" name="jobQuoteRevision" class="validate[custom[number]]" maxlength="3" style="width:20px;" onchange="validatequotedisabledbuttons()">
					<span style="color:red;" id="revisionErrorMsg"></span></td>
					<td style="width:31%;"><label>Submitted By:</label>
						<input   type="text" id="jobQuoteSubmittedBYFullName" name="jobQuoteSubmittedBYFullName" placeholder="Minimum 1 character required" onchange="validatequotedisabledbuttons()"> 
						<!-- <img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" > -->
					</td>
					<td>
					<select name="templateID" id="templateID"   style="width:208px;" onchange="Loadtemplatetoquote(this.value)">
					<option value="-1">--None--</option>
					</select>
					<input type="button" name="saveastemplateid" id="saveastemplateid"   class="add turbo-tan" value="Save As Template"  style="width:39%" onclick="validatequotedisabledbuttons();ValidateNewQuoteTemplate();"/>
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
			<label id="LineItem_error" style="vertical-align: middle;top: 152px;margin-left: 484px;color: red;"></label>
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>LineItems</b></label></legend>
			<table style="width:1000px;height:160px;"><input type="hidden" name="manufacturertextboxid" id="manufacturertextboxid"><input type="hidden" name="joQuoteDetailMstrID" id="joQuoteDetailMstrID" value="0"><input type="hidden" name="newquotesposition" id="newquotesposition">
				<tr style="display: none;">
					<td >
						<select   name="lineitemtypeid" id="lineitemtypeid" style="width:188px;" onchange="LineItemtypeonchange(this.value)">
							<option value="0">--None--</option>
							<option value="1">Title</option>
							<option value="2">Item2</option>
							<option value="3">Item3</option>
							<option value="4">Price</option>
						</select>
					</td>
					<td style="width: 10px;"></td>
					<td rowspan="5"><textarea name="TinyMCETextEditor" id="TinyMCETextEditor" class="TinyMCETextEditor" style="visibility: hidden;"></textarea></td>
					<td rowspan="5"><input type="button" name="AddButtonQuote" id="AddButtonQuote" class="add turbo-tan" value="ADD"  style="float: right;" onclick="SaveDetailLineItems()"  onfocus="focusMethod('Quote')" onblur="blurMethod('Quote')"  onkeypress="keypressQuote(event,'Quote')"/> </td>
				</tr>
				<tr style="display: none;"><td style="height:25px;width:190px;" rowspan="4">
					<input type="text" name="Item2_TextBox" id="Item2_TextBox" placeholder="Qty" maxlength="15"/>
					<input type="text" name="Item3_TextBox1" id="Item3_TextBox1" placeholder="Qty" maxlength="15"/>
					<input type="text" name="Price_TextBox1" id="Price_TextBox1" placeholder="Textbox" maxlength="50"/>
					<input type="text" name="Cost_TextBox" id="Cost_TextBox" placeholder="Cost" maxlength="12"/>
					<input type="text" name="Item3_TextBox2" id="Item3_TextBox2" placeholder="SellPrice"/>
					<input type="text" name="Price_TextBox2" id="Price_TextBox2" placeholder="SellPrice" />
					<input type="text" name="Manufacturer_TextBox" id="Manufacturer_TextBox" placeholder="Manufacturer"/>
					<select name="quoteCategorySelect" id="quoteCategorySelect" style="width:188px;" onchange="">
					</select>
				</td>
				<td style="width: 19px;" rowspan="4">
				<!-- <label id="Item2_TextBox_label" style="color: red;height: 0px;"></label>
				<label id="Item3_TextBox1_label" style="color: red;height: 24px;"></label>
				<label id="Price_TextBox1_label" style="color: red;height: 24px;"></label>
				<label id="Cost_TextBox_label" style="color: red;height: 24px;"></label>
				<label id="Item3_TextBox2_label" style="color: red;height: 24px;"></label>
				<label id="Price_TextBox2_label" style="color: red;height: 24px;"></label>
				<label id="Manufacturer_TextBox_label" style="color: red;height: 24px;"></label>
				<label id="quoteCategorySelect_label" style="color: red;height: 24px;"></label> -->
				<div id="Item2_TextBox_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Item3_TextBox1_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Price_TextBox1_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Cost_TextBox_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Item3_TextBox2_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Price_TextBox2_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="Manufacturer_TextBox_label" style="color: red;position: relative;height: 20px;"></div>
				<div id="quoteCategorySelect_label" style="color: red;position: relative;height: 20px;"></div>
				</td>
				<td></td></tr>
				<tr style="display: none;"><td style="height:25px;"></td></tr>
				<tr style="display: none;"><td style="height:25px;"></td></tr>
				<tr style="display: none;"><td style="height:25px;"></td></tr>
			</table>
			<table>
			</fieldset>
			<br>
			<table id="addnewquotesList"></table>  <div id="addnewquotespager" style="display:none"></div>
			<br>
			<table align="left">
			<tr width="100%">
			<td width="20%"><input type="button" name="quotepdfpreviewButton" id="quotepdfpreviewButton" class="add turbo-tan" value="PDF Preview" title="PDF Preview"  style="width: 100px;" onclick="validatequotedisabledbuttons();pdfpreviewButton()"/>&nbsp;<input type="button" name="addquotegridButton" id="addquotegridButton" class="add turbo-tan" value="Add" title="Add"  style="width: 100px;" onclick="addquotegridrow();validatequotedisabledbuttons();"/></td>
			<!-- <td style="width: 464px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td> -->
			<td colspan=2 width="80%" align="right"><b><label>Total Price:&nbsp;</label><label id="sellpriceLabel" style="color: green;font-size:18px;"></label></b>&nbsp;&nbsp;&nbsp;&nbsp;<b><label><b>Total Cost:</b>&nbsp;</label><label id="totalpriceLabel" style="color: green;font-size:18px;"></label></b>
			</td></tr>
			
			<td></td>
			<td style="width: 464px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<!-- <td ><input type="checkbox" style="width:100px;margin-left: -42px;" id="quotechkTotalPrice" name="quotechkTotalPrice" onclick="validatequotedisabledbuttons();"><label style="margin-left: -39px;"><b>Include on Quote</b></label></td> -->
			</tr>
			</table>
			<!-- <table align="left">
				<tr>
					<td align="center" colspan="2"><div id="quoteAddMsg" style="display: none"></div></td>
				</tr>
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:25px;height:25px;background:#EEDEBC;">
			            	<table>
			            		<tr>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="#" style="background: #EEDEBC"></td>
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
			</table> --></form>
			</div>
			<table style="width: 770px;">
				<tr style="display: none;"><td align="right"><input type="text" value="Manufacturer"  class="ui-button ui-widget ui-state-default ui-corner-all" style="color:#FFF;background:#e0842a; width:100px" ></td></tr>
				<tr>
					<td align="right">
					<input type="button" value="Manufacturer" style="color:#FFFFFF;background:#637c92;border-radius:5px;border:0px;padding:3px 5px;cursor:pointer;display: none;"> 
					</td>
				</tr>
			</table>
			<!-- <fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Remarks</b></label></legend>
				<table>
					<tr><td><textarea id="quoteRemarksID" name="quoteRemarksName" style="width: 1067px; height: 57px;"></textarea></td></tr>
				</table>
			</fieldset> -->
			<table>
				<tr><td style="padding-bottom: 4px;">
					<table>
						<tr>
							<td>
								<fieldset style="width:380px;" class= "custom_fieldset">
								<legend class="custom_legend"><label><b>Internal Notes</b></label></legend>
									<table>
										<tr><td><input type="text" id="jobQuoteInternalNote" name="jobQuoteInternalNote" style="width:350px;height:25px" onchange="validatequotedisabledbuttons();"></td>
										<td><input type="hidden" id="quoteRevisionExisting"></td>
										</tr>
									</table>
								</fieldset>
							</td>
							<td style="padding-left:120px;">
							<table>
							<tr><td><input type="checkbox" name="chk_inclueLSD" id ="chk_includeLSD" onclick="validatequotedisabledbuttons();"/><td><td>Include Lump Sum Discount <span><input type="text" id="txt_LSDValue" class="number" style="display:none"></span> </td></tr>
							<tr><td><input type="checkbox" name="chk_donotQtyitem2and3" id ="chk_donotQtyitem2and3" onclick="validatequotedisabledbuttons();"/><td><td>Do Not Show 'Qty' Column for Item2 and Item3 on PDF</td></tr>
							<tr><td><input type="checkbox" name="chk_showTotPriceonly" id ="chk_showTotPriceonly" onclick="validatequotedisabledbuttons();"/><td><td>Show Total Price Only on PDF</td></tr>
							<tr><td ><input type="checkbox" id="quotechkTotalPrice" name="quotechkTotalPrice" onclick="validatequotedisabledbuttons();"><td><td><label >Include Total Price on PDF</label></td></tr>
							</table>
							</td>
						</tr>
					</table>
				</td>
				<td style="width:20px"></td>
				<td>
					<fieldset style="width:219px;height: 61px;display:none;" class= "custom_fieldset">
					<legend class="custom_legend"><label><b>Total Price</b></label></legend>
			 			<table><input type="hidden" name="quotecosttotalamount" id="quotecosttotalamount" value="0"/>
							<tr><td></td><td><input type="text" style="width:100px" id="quoteTotalPrice" disabled="disabled" name="quoteTotalPrice"></td></tr>
							<tr><td></td><!-- <td><input type="checkbox" style="width:100px;margin-left: -42px;" id="quotechkTotalPrice" name="quotechkTotalPrice"><label style="margin-left: -39px;"><b>Include on Quote</b></label></td> --></tr>
							<tr  style="display: none;"><td><label>Total Cost: </label></td><td><input type="text" style="width:100px" id="quoteTotalCost" disabled="disabled" name="quoteTotalCost"></td></tr>
							<!-- <tr><td><label>Discounted Price: </label></td><td><input type="text" style="width:100px" id="quoteDiscountedPrice" class="validate[custom[number]]" ></td></tr> -->
						</table>
					</fieldset>
				</td></tr>
			</table>
		<hr width="1115px;">
		<table style="width:1080px;align:center">
		<tr><td colspan="3" align="right"><span id="QuotesSaveSuccessMessage" style="color: green;display: none"></span></td></tr>
		 	<tr>
			 	<td></td>
			 	<td id="addQuotesView" align="right" style="padding-right:1px;">
			 		<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveANDcloseQuote()" style=" width:125px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Close" onclick="cancelQuote()" style="width:80px;">  
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
	<div id="QuoteckEditordivbx" style="display: none;">
	<input type="hidden" id="Quoteselrowid" value="">
	<textarea name="Quoteeditor" class="Quoteeditor" id="Quoteeditor"></textarea>
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

	   //number validation
	   $('.number').keypress(function (event) {
		    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
		        event.preventDefault();
		    }
	
		    var text = $(this).val();
	
		    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
		        event.preventDefault();
		    }
		});

		 $("#chk_includeLSD").click(function(){
				if( $("#chk_includeLSD").is(":checked"))
					{
					$("#txt_LSDValue").css("display","inherit");
					$('input[name="chk_showTotPriceonly"]').attr('checked', false);
					}
				else
					{$("#txt_LSDValue").css("display","none");
					$("#txt_LSDValue").val("0");}

			 })
			 
		 $("#chk_showTotPriceonly").click(function(){
				if( $("#chk_showTotPriceonly").is(":checked")){
					$('input[name="quotechkTotalPrice"]').attr('checked', false);
					$('input[name="chk_inclueLSD"]').attr('checked', false);
					$("#txt_LSDValue").css("display","none");
					$("#txt_LSDValue").val("0");
				}
				else
					$('input[name="chk_showTotPriceonly"]').attr('checked', false);
			 })	 
			 
		 $("#quotechkTotalPrice").click(function(){
				if( $("#quotechkTotalPrice").is(":checked"))
					$('input[name="chk_showTotPriceonly"]').attr('checked', false);
				else
					$('input[name="quotechkTotalPrice"]').attr('checked', false);
			})
			 
	  
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
			},close : function() {
				//$(this).dialog("close");
			}
		});
	   LineItemtypeonchange("-1");
	   });
   String.prototype.replaceAll = function(target, replacement) {
		  return this.split(target).join(replacement);
		};
  var defaultfont_set=true;
   function TinymceTextEditorEnabledisable(value,id){
	   defaultfont_set=true; 
	   if(value){
		   
		   tinymce.init({
				 mode : "specific_textareas",
	   editor_selector : "TinyMCETextEditor",
			    theme: "modern",
			    content_style:"p { padding: 0; margin: 2px 0;}",
			    plugins: [
			         "advlist autolink lists link image charmap print preview hr anchor pagebreak",
			        "searchreplace wordcount visualblocks visualchars code fullscreen",
			        "insertdatetime media nonbreaking save table  directionality",
			        " template paste textcolor colorpicker textpattern tabindex tabfocus"
			    ],
			    toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor fontselect fontsizeselect",
			    //entity_encoding : "raw",
			   	menubar: false,
			   	statusbar: false,
			    toolbar_items_size: 'small',
			    force_br_newlines : true,
		        force_p_newlines : false,
		        fullpage_default_font_family: "Times New Roman,serif;",
		        fullpage_default_fontsize: "10pt",
		        //forced_root_block : 'div',
			    width: "700",
		        height: "100",
		        setup: function (ed) {
		            ed.on('keypress', function (e) {
		            	//tinymce.activeEditor.selection.getNode().style.fontSize='10pt'; 
		            	var text=$(tinymce.activeEditor.getContent()).text();
		            	console.log("keypress="+text.trim().length+"=="+text+"=="+text.length);
			            if(text.trim().length==0 && tinyMCE.activeEditor.selection.getNode().style.fontFamily==""){
				            console.log("keypress");
			            	tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
			            	tinymce.activeEditor.execCommand("fontSize", false, "10pt");
			            	//myCustomInitInstance(ed);
				            }
		                //your custom logic  
		            });
		            ed.on('keyup', function (e) {
		            	//tinymce.activeEditor.selection.getNode().style.fontSize='10pt'; 
		            	var text=$(tinymce.activeEditor.getContent()).text();
		            	console.log("keypress"+text.trim().length+"=="+text+"=="+text.length);
			            if(text.trim().length==0 && tinyMCE.activeEditor.selection.getNode().style.fontFamily==""){
			            	console.log("keyup");
			            	tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
			            	tinymce.activeEditor.execCommand("fontSize", false, "10pt");
			            	//myCustomInitInstance(ed);
				            }
		                //your custom logic  
		            });
		        },
			    init_instance_callback : "myCustomInitInstance",
			    theme_advanced_buttons3_add : "pastetext,pasteword,selectall",
			    paste_auto_cleanup_on_paste : true,
			    paste_retain_style_properties: "all",
			    paste_convert_word_fake_lists:false,
			    paste_remove_styles:true,
			    paste_remove_styles_if_webkit:"link anchor autolink font-family font-size", 
		        /*paste_remove_styles: true,
	            paste_remove_styles_if_webkit: true,
	            paste_strip_class_attributes: true, */
		        paste_preprocess : function(pl, o) {
		            // Content string containing the HTML from the clipboard
		           var pastevalue=o.content;
		           /* 
		           pastevalue=pastevalue.replaceAll("<p", "<div ").replaceAll("</p>", "</div>");
		           
		           pastevalue=pastevalue.replaceAll("size: xx-small;", "size: 7pt");
		           pastevalue=pastevalue.replaceAll("size: x-small","size: 7.5pt");
		           pastevalue=pastevalue.replaceAll("size: xx-large","size: 7.5pt");
		           pastevalue=pastevalue.replaceAll("size: x-large","size: 7.5pt");
		           pastevalue=pastevalue.replaceAll("size: smaller","size: 10pt");
		           pastevalue=pastevalue.replaceAll("size: small", "size: 10pt");
		           pastevalue=pastevalue.replaceAll("size: medium", "size: 12pt");
		           pastevalue=pastevalue.replaceAll("size: larger", "size: 14pt");
		           pastevalue=pastevalue.replaceAll("size: large", "size: 13.5pt") */;
		            //o.content =pastevalue;
		            
		            
			         } ,
			   paste_postprocess : function(pl, o) {
				   
				   var pastevalue=o.node.innerHTML;
				  
		           //pastevalue=pastevalue.replaceAll("Helvetica, sans-serif","Helvetica");
		          // o.node.innerHTML=pastevalue;

		           console.log("o.node.innerHTML=="+o.node.innerHTML);
				            // Content string containing the HTML from the clipboard
				            // alert("postprocess"+o.node.innerHTML);
				           //o.node.innerHTML = o.node.innerHTML;
		           setTimeout(function(){
		        	   if(tinymce.activeEditor.queryCommandValue("fontName")==""){
		        		   tinymce.activeEditor.selection.select(tinymce.activeEditor.getBody(),true);
		        		  //alert(tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif"));
		        		   tinymce.activeEditor.execCommand("fontName", true, "Times New Roman,serif");
		        		   console.log("test");
		        		   console.log("next test");
		        		   }
		        	   
		               
		        		}, 300);
		        	   
					         } 
			   
			});
			
		   }else{
			   tinymce.remove();
			   }
	  
	   }
   var edd="";

  
   function myCustomInitInstance(ed) { 
	   
	   edd=ed;
		  try{
			//ed.selection.getNode().style.fontSize='10pt';
			//ed.selection.getNode().style.fontName='Helvetica';
			if(defaultfont_set){
				console.log("Initialize font");
				//tinymce.activeEditor.execCommand("fontName", false, "Times New Roman,serif");    
            	//tinymce.activeEditor.execCommand("fontSize", false, "10pt");
				defaultfont_set=false; 
				}
		  }
		  catch(err){      
		  }
		}
	function clearallfields(value){
		$("#Item2_TextBox").val("");
		$("#Price_TextBox1").val("");
		$("#Price_TextBox2").val("");
		$("#Item3_TextBox1").val("");
		$("#Item3_TextBox2").val("");
		$("#Cost_TextBox").val("");
		$("#Manufacturer_TextBox").val("");
		$("#manufacturertextboxid").val("");
		$("#joQuoteDetailMstrID").val("");
		$("quoteCategorySelect").val("-1");
		if(value){
			$("#lineitemtypeid").val(0);
			LineItemtypeonchange(0);
			}
	}
   function LineItemtypeonchange(value){
	  
	  
	   clearallfields();
	   setQuotesvalidation(0);
	   console.log("LineItemtypeonchange");
		if(value==1){
			setQuotesvalidation(1);
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "none");
			$("#Manufacturer_TextBox").css("display", "none");
			$("#quoteCategorySelect").css("display", "none");
			if(tinymce.activeEditor!=undefined){
				texteditor=tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
				}
			TinymceTextEditorEnabledisable(true);
			//tinymce.activeEditor.setContent("");
			//document.getElementById("Item2_TextBox").tabIndex = "7";
			// document.getElementById(edd.id + "_ifr") ie.TinyMCETextEditor_ifr
			if(edd.dom!=undefined){
				edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 8);
				tinymce.activeEditor.setContent("");
				edd.execCommand("fontName", false, "Times New Roman,serif");    
				edd.execCommand("fontSize", false, "10pt");
				$("#lineitemtypeid").focus();
			}else{
				setTimeout(function(){edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 8);
				tinymce.activeEditor.setContent("");
				edd.execCommand("fontName", false, "Times New Roman,serif");    
				edd.execCommand("fontSize", false, "10pt");
				$("#lineitemtypeid").focus();
				}, 500);
				
			}
			
			document.getElementById("AddButtonQuote").tabIndex = "9";		   	
		}else if(value==2){
			//one textbox qty and editor
			setQuotesvalidation(2);
			$("#Item2_TextBox").css("display", "block");
			$("#Item2_TextBox").css("margin-bottom", "3px");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "block");
			$("#Cost_TextBox").css("margin-bottom", "3px");
			$("#Manufacturer_TextBox").css("display","block");
			$("#Manufacturer_TextBox").css("margin-bottom", "3px");
			$("#quoteCategorySelect").css("display", "block");
			$("#quoteCategorySelect").css("margin-bottom", "3px");
			if(tinymce.activeEditor!=undefined){
				tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
			}
// 			$("#Item2_TextBox").attr('tabindex', 2);
// 			$("#Cost_TextBox").attr('tabindex', 3);
// 			$("#Manufacturer_TextBox").attr('tabindex', 4);
// 			$("#quoteCategorySelect").attr('tabindex', 5);
			TinymceTextEditorEnabledisable(true);
			//tinymce.activeEditor.setContent("");
			quoteCategorySelectBox(-1);
			document.getElementById("Item2_TextBox").tabIndex = "7";
		   document.getElementById("Cost_TextBox").tabIndex = "8";
		   document.getElementById("Manufacturer_TextBox").tabIndex = "9";
		   document.getElementById("quoteCategorySelect").tabIndex = "10";
			// document.getElementById(edd.id + "_ifr") ie.TinyMCETextEditor_ifr
		   if(edd.dom!=undefined){
				edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 11);
				tinymce.activeEditor.setContent("");
				edd.execCommand("fontName", false, "Times New Roman,serif");    
				edd.execCommand("fontSize", false, "10pt");
				$("#lineitemtypeid").focus();
			}else{
				setTimeout(function(){edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 11);
				tinymce.activeEditor.setContent("");
				edd.execCommand("fontName", false, "Times New Roman,serif");    
				edd.execCommand("fontSize", false, "10pt");
				$("#lineitemtypeid").focus();
				}, 500);
			}
		   //document.getElementById("tinymce").tabIndex = "11";
			document.getElementById("AddButtonQuote").tabIndex = "12";
				
		}else if(value==3){
			setQuotesvalidation(3);
			//2 textbox qty,sellprice and editor
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			
			$("#Item3_TextBox1").css("display", "block");
			$("#Item3_TextBox1").css("margin-bottom", "3px");
			
			$("#Item3_TextBox2").css("display", "block");
			$("#Item3_TextBox2").css("margin-bottom", "3px");

			$("#Cost_TextBox").css("display", "block");
			$("#Cost_TextBox").css("margin-bottom", "3px");
			
			$("#Manufacturer_TextBox").css("display","block");
			$("#Manufacturer_TextBox").css("margin-bottom", "3px");
			
			$("#quoteCategorySelect").css("display", "block");
			$("#quoteCategorySelect").css("margin-bottom", "3px");
			
			if(tinymce.activeEditor!=undefined){
				tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
			}
			TinymceTextEditorEnabledisable(true);
			//tinymce.activeEditor.setContent("");			
			quoteCategorySelectBox(-1);
			
			
			
			document.getElementById("Item3_TextBox1").tabIndex = "7";
			   document.getElementById("Cost_TextBox").tabIndex = "8";
			   document.getElementById("Item3_TextBox2").tabIndex = "9";
			   document.getElementById("Manufacturer_TextBox").tabIndex = "10";
			   document.getElementById("quoteCategorySelect").tabIndex = "11";
			// document.getElementById(edd.id + "_ifr") ie.TinyMCETextEditor_ifr
			   if(edd.dom!=undefined){
					edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 12);
					tinymce.activeEditor.setContent("");
					edd.execCommand("fontName", false, "Times New Roman,serif");    
					edd.execCommand("fontSize", false, "10pt");
					$("#lineitemtypeid").focus();
				}else{
					setTimeout(function(){edd.dom.setAttrib(document.getElementById(edd.id + "_ifr"), 'tabindex', 12);
					tinymce.activeEditor.setContent("");
					edd.execCommand("fontName", false, "Times New Roman,serif");    
					edd.execCommand("fontSize", false, "10pt");
					$("#lineitemtypeid").focus();
					}, 500);
				}
			 //  document.getElementById("tinymce").tabIndex = "12";
				document.getElementById("AddButtonQuote").tabIndex = "13";
					
		}else if(value==4){
			setQuotesvalidation(4);
			//2textbox textbox and sellprice
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "block");
			$("#Price_TextBox1").css("margin-bottom", "3px");
			$("#Price_TextBox2").css("display", "block");
			$("#Price_TextBox2").css("margin-bottom", "3px");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "none");
			$("#Manufacturer_TextBox").css("display", "none");
			$("#quoteCategorySelect").css("display", "none");
			if(tinymce.activeEditor!=undefined){
				tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
			}
			TinymceTextEditorEnabledisable(false);
			
			document.getElementById("Price_TextBox1").tabIndex = "7";
			   document.getElementById("Price_TextBox2").tabIndex = "8";
				document.getElementById("AddButtonQuote").tabIndex = "9";
				$("#lineitemtypeid").focus();
		}else{
			//alert("value == "+value);
			setQuotesvalidation(0);
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "none");
			$("#Manufacturer_TextBox").css("display", "none");
			$("#quoteCategorySelect").css("display", "none");
			
			if(tinymce.activeEditor!=undefined){
				tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
			}
			$("#lineitemtypeid").val("0");
			TinymceTextEditorEnabledisable(false);
		}
		
	   }
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
						buttons: [{height:30,text: "OK",
							 keypress:function(e){
									var x = e.keyCode;
									if(x==13){$(newDialogDiv).dialog("close");}
							    },
							click: function() { $(this).dialog("close"); }}]}).dialog("open");
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
	   function focusMethod(CheckquoteorTemp){
			   if(CheckquoteorTemp=="Quote"){
				   $('#AddButtonQuote').css('border','3px solid #FF0000');
				   $('#AddButtonQuote').css(' border-radius','2px');
			   }else{
				   $('#AddButtonTemplate').css('border','3px solid #FF0000');
				   $('#AddButtonTemplate').css(' border-radius','2px');
			   }
		   }
	   function blurMethod(CheckquoteorTemp){
		   if(CheckquoteorTemp=="Quote"){
		   $('#AddButtonQuote').css('border','');
		   $('#AddButtonQuote').css(' border-radius','');
		   }else{
		   $('#AddButtonTemplate').css('border','');
		   $('#AddButtonTemplate').css(' border-radius','');
		   }
		   }
	   function keypressQuote(event,CheckquoteorTemp){
		   var x = event.keyCode;
		   if(x==13){
			   if(CheckquoteorTemp=="Quote"){
			   SaveDetailLineItems();
			   }else{
				   SaveDetailLineItems_template();
				   }
			   }
		   }
	   function clickdeleteicon(){
		   DeleteNewQuoteListLineItems();
		   $('#addquotesloader').css("display","block");
		//$('#addquotesloader').css({"display":"block",function(){DeleteNewQuoteListLineItems();}});
			//setTimeout(DeleteNewQuoteListLineItems(), 5000);
		}

	   function clearAddDetails(){
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "none");
			$("#Manufacturer_TextBox").css("display", "none");
			$("#quoteCategorySelect").css("display", "none");

			setQuotesvalidation(0);
			$("#Item2_TextBox").css("display", "none");
			$("#Price_TextBox1").css("display", "none");
			$("#Price_TextBox2").css("display", "none");
			$("#Item3_TextBox1").css("display", "none");
			$("#Item3_TextBox2").css("display", "none");
			$("#Cost_TextBox").css("display", "none");
			$("#Manufacturer_TextBox").css("display", "none");
			$("#quoteCategorySelect").css("display", "none");
			
			if(tinymce.activeEditor!=undefined){
				tinymce.activeEditor.setContent("");
				myCustomInitInstance(edd);
			}
			$("#lineitemtypeid").val("0");
			TinymceTextEditorEnabledisable(false);
			$("#Quotes_add_edit").val("add");
		}

function setQuotesvalidation(type){
	$("#Item2_TextBox_label").html("&nbsp;"); 
	$("#Item3_TextBox1_label").html("&nbsp;");
	$("#Price_TextBox1_label").html("&nbsp;");
	$("#Cost_TextBox_label").html("&nbsp;");
	$("#Item3_TextBox2_label").html("&nbsp;");
	$("#Price_TextBox2_label").html("&nbsp;");
	$("#quoteCategorySelect_label").html("&nbsp;");
	$("#Manufacturer_TextBox_label").html("&nbsp;");
	
	if(type==1){
		$("#Item2_TextBox_label").css("display", "none");
		$("#Item2_TextBox1_label").css("display", "none");
		$("#Price_TextBox1_label").css("display", "none");
		$("#Price_TextBox2_label").css("display", "none");
		$("#Item3_TextBox1_label").css("display", "none");
		$("#Item3_TextBox2_label").css("display", "none");
		$("#Cost_TextBox_label").css("display", "none");
		$("#Manufacturer_TextBox_label").css("display", "none");
		$("#quoteCategorySelect_label").css("display", "none");
	}else if(type==2){
		if(chkvalidateQty_YN){
			$("#Item2_TextBox_label").html("*"); 
		}else{
			$("#Item2_TextBox_label").html("&nbsp;"); 
		}
		if(chkvaldateCost_YN){
			$("#Cost_TextBox_label").html("*"); 
		}else{
			$("#Cost_TextBox_label").html("&nbsp;"); 
		}
		if(chkvalidateMan_YN){
			$("#Manufacturer_TextBox_label").html("*"); 
		}else{
			$("#Manufacturer_TextBox_label").html("&nbsp;"); 
		}
		if(chkvalidateCategory_YN){
			$("#quoteCategorySelect_label").html("*"); 
		}else{
			$("#quoteCategorySelect_label").html("&nbsp;"); 
		}

		
		$("#Item2_TextBox_label").css("display", "block");
		$("#Item2_TextBox_label").css("margin-bottom", "5px");
		$("#Price_TextBox1_label").css("display", "none");
		$("#Price_TextBox2_label").css("display", "none");
		$("#Item3_TextBox1_label").css("display", "none");
		$("#Item3_TextBox2_label").css("display", "none");
		$("#Cost_TextBox_label").css("display", "block");
		$("#Cost_TextBox_label").css("margin-bottom", "5px");
		$("#Manufacturer_TextBox_label").css("display", "block");
		$("#Manufacturer_TextBox_label").css("margin-bottom", "5px");
		$("#quoteCategorySelect_label").css("display", "block");
		$("#quoteCategorySelect_label").css("margin-bottom", "-9px");
	}else if(type==3){
		
		
		if(chkvalidateQty_YN){
			$("#Item3_TextBox1_label").html("*"); 
		}else{
			$("#Item3_TextBox1_label").html("&nbsp;"); 
		} 
		if(chkvalidateSPItem3_YN){
			$("#Item3_TextBox2_label").html("*"); 
		}else{
			$("#Item3_TextBox2_label").html("&nbsp;"); 
		} 
		if(chkvaldateCost_YN){
			$("#Cost_TextBox_label").html("*"); 
		}else{
			$("#Cost_TextBox_label").html("&nbsp;"); 
		} 
		if(chkvalidateMan_YN){
			$("#Manufacturer_TextBox_label").html("*"); 
		}else{
			$("#Manufacturer_TextBox_label").html("&nbsp;"); 
		} 
		if(chkvalidateCategory_YN){
			$("#quoteCategorySelect_label").html("*"); 
		}else{
			$("#quoteCategorySelect_label").html("&nbsp;"); 
		}
		
		$("#Item2_TextBox_label").css("display", "none");
		$("#Price_TextBox2_label").css("display", "none");
		$("#Price_TextBox1_label").css("display", "none");
		$("#Item3_TextBox1_label").css("display", "block");
		$("#Item3_TextBox1_label").css("margin-bottom", "5px");
		$("#Item3_TextBox2_label").css("display", "block");
		$("#Item3_TextBox2_label").css("margin-bottom", "5px");
		$("#Cost_TextBox_label").css("display", "block");
		$("#Cost_TextBox_label").css("margin-bottom", "5px");
		$("#Manufacturer_TextBox_label").css("display", "block");
		$("#Manufacturer_TextBox_label").css("margin-bottom", "5px");
		$("#quoteCategorySelect_label").css("display", "block");
		$("#quoteCategorySelect_label").css("margin-bottom", "-9px");
	}else if(type==4){
		if(chkvalidateSPonPrice_YN){
			$("#Price_TextBox2_label").html("*"); 
		}else{
			$("#Price_TextBox2_label").html("&nbsp;"); 
		}
		$("#quoteCategorySelect_label").css("display", "none");
		$("#Manufacturer_TextBox_label").css("display", "none");
		$("#Cost_TextBox_label").css("display", "none");
		$("#Item3_TextBox2_label").css("display", "none");
		$("#Item3_TextBox1_label").css("display", "none");
		$("#Item2_TextBox_label").css("display", "none");
		$("#Price_TextBox1_label").css("display", "none");
		$("#Price_TextBox2_label").css("display", "block");
		$("#Price_TextBox2_label").css("margin-bottom", "-19px");
	}else{
		$("#quoteCategorySelect_label").css("display", "none");
		$("#Manufacturer_TextBox_label").css("display", "none");
		$("#Cost_TextBox_label").css("display", "none");
		$("#Item2_TextBox_label").css("display", "none");
		$("#Price_TextBox1_label").css("display", "none");
		$("#Price_TextBox2_label").css("display", "none");
		$("#Item3_TextBox1_label").css("display", "none");
		$("#Item3_TextBox2_label").css("display", "none");
	}
	
	}
   </script>
  