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
	<div id="addquoteTemplate" style="display: none;">
	<div id="addQuoteTemplateBasicInfo">
		<form action="" id="quoteTemplateManipulateForm">
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>Quote Template</b></label></legend>
			<table style="width:1000px">
				<tr>
					<td style="width: 35px;"><label>Template Description:<!-- Type: --></label></td>
					<td>
						<select style="width:170px;margin-left: 3px;border:#DDBA82;display:none;" id="quoteTemplateTypeDetail" name="quoteTemplateTypeDetailName">
							<option value="-1"> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select>
						<input type="text" id="templateDescription" name="templateDescription"> 
					</td>
					<td style="width: 60px;display: none;"><label>Shared:</label></td>
					<td><input type="checkbox" id="templateShared" name="templateShared" style="display: none;"></td>
					<td style="width: 100px;"><label><!-- Template Description: --></label></td>
					<td>
						<!-- <input type="text" id="templateDescription" name="templateDescription">  -->
					</td>
				</tr>
				<tr style="display: none;">
				<td colspan="6">
					<input type="text"  id="quoteTemplateHeaderID" name="quoteTemplateHeaderID">
					<input type="text"  id="joQuoteLineTemplateHeaderID" name="joQuoteLineTemplateHeaderID">
				</td>
			</tr>
			</table>
			</fieldset>
			<label id="LineItem_error_template" style="vertical-align: middle;top: 152px;margin-left: 484px;color: red;"></label>
			<fieldset style="width:1080px" class= "custom_fieldset">
			<legend class="custom_legend"><label><b>LineItems</b></label></legend>
			<table style="width:1000px;height:162px;display:none;"><input type="hidden" name="manufacturertextboxid_template" id="manufacturertextboxid_template"><input type="hidden" name="joQuoteDetailMstrID_template" id="joQuoteDetailMstrID_template" value="0"><input type="hidden" name="newquotesposition_template" id="newquotesposition_template">
				<tr>
				<td >
				<select name="lineitemtypeid_template" id="lineitemtypeid_template" style="width:188px;" onchange="LineItemtypeonchange_template(this.value)" tabindex="9">
				<option value="0">--None--</option>
				<option value="1">Title</option>
				<option value="2">Item2</option>
				<option value="3">Item3</option>
				<option value="4">Price</option>
				</select>
				</td>
				<td rowspan="5"><textarea name="TinyMCETextEditor_template" id="TinyMCETextEditor_template" class="TinyMCETextEditor" style="visibility: hidden;"></textarea></td>
				<td rowspan="5"><input type="button" name="AddButton" id="AddButtonTemplate" class="add turbo-tan" value="ADD"  style="float: right;" onclick="SaveDetailLineItems_template()" onfocus="focusMethod('QuoteTemplate')" onblur="blurMethod('QuoteTemplate')"  onkeypress="keypressQuote(event,'QuoteTemplate')"/> </td>
				</tr>
				<tr><td style="height:25px;width:205px;" rowspan="4">
				<input type="text" name="Item2_TextBox_template" id="Item2_TextBox_template" placeholder="Qty" maxlength="15"/>
				<input type="text" name="Item3_TextBox1_template" id="Item3_TextBox1_template" placeholder="Qty" maxlength="15"/>
				<input type="text" name="Price_TextBox1_template" id="Price_TextBox1_template" placeholder="Textbox" maxlength="50"/>
				<input type="text" name="Item3_TextBox2_template" id="Item3_TextBox2_template" placeholder="SellPrice"/>
				<input type="text" name="Price_TextBox2_template" id="Price_TextBox2_template" placeholder="SellPrice" />
				<input type="text" name="Cost_TextBox_template" id="Cost_TextBox_template" placeholder="Cost" maxlength="12"/>
				<input type="text" name="Manufacturer_TextBox_template" id="Manufacturer_TextBox_template" placeholder="Manufacturer"/>
				<select name="quoteCategorySelect_template" id="quoteCategorySelect_template" style="width:188px;" onchange="">
				</td><td></td></tr>
				<tr><td style="height:25px;"></td></tr>
				<tr><td style="height:25px;"></td></tr>
				<tr><td style="height:25px;"></td></tr>
			</table>
			<table>
			</fieldset>
			<br>
			<table id="addnewquotesTemplateList"></table>  <div id="addnewquotesTemplatepager" style="display:none;"> </div>
			<br>
			<table align="left">
			<tr width="100%">
			<td width="20%"> <input type="button" name="QuoteTemppreviewButton" id="QuoteTemppreviewButton" class="add turbo-tan" value="PDF Preview" title="PDF Preview"  style="width: 100px;" onclick="viewLinePDF()"/>&nbsp;<input type="button" name="addquotegridButton_temp" id="addquotegridButton_temp" class="add turbo-tan" value="Add" title="Add"  style="width: 100px;" onclick="addquotegridrow_temp();validatequotedisabledbuttons();"/></td>
			<!-- <td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" style="width:100px;margin-left: -42px;display:none;" id="quotechkTotalPrice_template" name="quotechkTotalPrice_template" onclick="saveANDcloseQuoteTemplate(true)"><label style="margin-left: -39px;display:none;"><b>Include on Quote</b></label></td> -->
			<td width="80%" colspan="2" align="right"><b><label>Total Price:&nbsp;</label><label id="sellpriceTemplateLabel" style="color: green;font-size:18px;"></label></b>&nbsp;&nbsp;&nbsp;&nbsp;<b><label><b>Total Cost:</b>&nbsp;</label><label id="totalpriceTemplateLabel" style="color: green;font-size:18px;"></label></b></td></tr>
			</tr>
			</table>
			<!-- <table id="quoteTemplateProductsGrid"></table><div id="quoteTemplateProductsGridPager"></div> 
			<table align="left">
				<tr>
					<td>
						<fieldset class= "custom_fieldset" style="padding:2px 8px 0px;width:50px;height:25px;background:#EEDEBC;">
			            	<table>
			            		<tr>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add" onclick="addQuoteTemplateFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit" onclick="editTemplateLineItems()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete" onclick="deleteQuoteFrom()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/lineItem_new.png" title="Line Items" onclick="addOpenLineItemDialog()" style="background: #EEDEBC"></td>
					            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/images/printer.png" title="Print Preview" onclick="addLineItemDialog(); return false;" style="background: #EEDEBC"></td>
					         		<td align="right" style="padding: 0 7px 0 1px; display: none;"><input type="image" src="./../resources/images/forward.png" title="Quick Add Line Item" onclick="addNewLineItemDialog(); return false;" style="background: #EEDEBC"></td>
					         		<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Quote" onclick="viewLinePDF();return false;" style="background: #EEDEBC">  </td>
					         		<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/images/QuoteProperties.png" title="Quote Properties" onclick="setQuoteTemplateProperties();" style="background: #EEDEBC">  </td>
			            		</tr>
			            	</table>
		            	</fieldset>
					</td>
					<td align="right" style="padding-left: 865px;">
						<input type="button" title="Add Manufacturer" class="add turbo-tan" value="Add Manufacturer" onclick="addManufacture()" style="width: 160px;">
					</td>
				</tr>
			</table> -->
			<table style="width: 770px;display: none;">
				<tr style="display: none;"><td align="right"><input type="button" title="Add Manufacturer" class="add turbo-tan" value="Add Manufacturer" onclick="addManufacture()" style="width: 160px;"><input type="text" value="Manufacturer"  class="ui-button ui-widget ui-state-default ui-corner-all" style="color:#FFF;background:#e0842a; width:100px" ></td></tr>
				<tr>
					<td align="right">
						<input type="button" value="Manufacturer" style="color:#FFFFFF;background:#637c92;border-radius:5px;border:0px;padding:3px 5px;cursor:pointer;display: none;"> 
					</td>
				</tr>
				<tr>
					<!-- <td>
						<fieldset style="" class= "custom_fieldset">
						<legend class="custom_legend"><label><b>Remarks</b></label></legend>
							<table>
								<tr><td><textarea id="quoteTemplateRemarksID" name="quoteTemplateRemarksName" style="width: 770px; height: 57px;"></textarea></td></tr>
							</table>
						</fieldset>
					</td> -->
					<!-- <td style="padding-left: 20px;">
						<fieldset style="width:250px" class= "custom_fieldset">
				 			<table>
								<tr><td><label>Total Price: </label></td><td><input type="text" style="width:100px" id="quoteTemplateTotalPrice" disabled="disabled" name="quoteTemplateTotalPrice"></td></tr>
							</table>
						</fieldset>
					</td> -->
				</tr>
			</table>
			<br><br>
			<table style="width: 770px;">
			<tr>
			<td >
						<fieldset style="width:250px;display:none" class= "custom_fieldset">
				 			<table>
								<tr><td><label>Total Price: </label></td><td><input type="text" style="width:100px" id="quoteTemplateTotalPrice" disabled="disabled" name="quoteTemplateTotalPrice"></td></tr>
								<tr><!-- <td><input type="checkbox" style="width:100px;margin-left: -42px;" id="quotechkTotalPrice_template" name="quotechkTotalPrice_template"></td><td><label style="margin-left: -57px;"><b>Include on Quote</b></label></td> --></tr>
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
	   				<input type="button" class="savehoverbutton turbo-tan" id="SaveQuoteTemplateButtonID" value="Save" onclick="LineItemInfoTemp()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan" id="CloseQuoteTemplateButtonID" value="Cancel" onclick="cancelInLineNoteTemp()" style="width:80px;">
	   			</td>
			</tr>
		</table>
		</form>
	</div> 
		<hr width="1115px;">
		<table style="width:1080px;align:center">
		<tr><td colspan="4" align="right" id="labelforsuccess"></td></tr>
		 	<tr>
			 	<td></td>
			 	<td id="addQuotesTemplateView" align="right" style="padding-right:1px;">
			 		<input type="button" id="quoteTemplateSaveId" class="savehoverbutton turbo-tan" value="Save" onclick="saveANDcloseQuoteTemplate()" style=" width:125px;">
					<input type="button" id="quoteTemplateCancel" class="cancelhoverbutton turbo-tan"  value="Close" onclick="cancelQuoteTemplate()" style="width:80px;">  
				</td>
				<td id="editQuotesTemplateView" align="right" style="padding-right:1px;"></td>
				<td id="copyQuotesTemplateView" align="right" style="padding-right:1px;"></td>
			</tr>
		</table>
	</div>
	<div id="addTemplateLineItem">
	<form action="" id="addTemplateForm">
	<input type="hidden" name="quotecosttotalamount_temp" id="quotecosttotalamount_temp" value="0"/>
	<input type="hidden" name="quoteTotalPrice_temp" id="quoteTotalPrice_temp" value="0"/>
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
				<td><input type="text"  id="rxManufacturerID" name="rxManufacturerIDTemplate"></td>
			</tr>
			<tr style="display: none;">
				<td><b>joQuoteTemplateHeaderID:</b></td>
				<td><input type="text"  id="joQuoteTemplateHeaderID" name="joQuoteTemplateHeaderID"></td>
				<td><input type="text"  id="joQuoteLineTemplateHeaderID" name="joQuoteLineTemplateHeaderID"></td>
			</tr>
			<tr style="display: none;">
				<td><b>VeFactoryId:</b></td>
				<td><input type="text"  id="veFactoryId" name="veFactoryIdTemplate"></td>
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
<div id= "addInLineItemNote" style="display: none;">
	<form action="" id="addInLineItemNoteID">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="lineItemNoteId" name="lineItemNoteName" style="height: 252px; width:570px;"></textarea>
	   				<input id="lineItemNoteLableId" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="saveLineItemNoteInfo()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelLintItemNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
		</form>
	</div>
	<div id="QuoteckEditordivbx_temp" style="display: none;">
	<input type="hidden" id="Quoteselrowid_temp" value="">
	<textarea name="Quoteeditor_temp" class="Quoteeditor_temp" id="Quoteeditor_temp"></textarea>
	</div> 
	<div ><jsp:include page="../LineItemTemplateProperties.jsp"></jsp:include></div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script>
   var newDialogDiv = jQuery(document.createElement('div'));
   $(document).ready(function(){
	   
	   
	   jQuery("#LineItemTempProperties").dialog({
			autoOpen : false,
			height : 350,
			width : 350,
			title : "Properties",
			modal : true,
			buttons : {		
				'Save':function(){
					savequoteTemplineitemproperties();
					},
				'Close':function(){
					$('#LineItemTempProperties').dialog('close');
					}
			},
			
			close : function() {
				//$(this).dialog("close");
			}
		});
		
	   });

   function editQuoteTemplateLineItemFrom(lineitemid){
	  
		 
	    if(lineitemid!=null&&lineitemid!=0){
	    $("#LineItemtempPropertyID").val(lineitemid);
			$.ajax({
				url : "./jobtabs2/getjoQLineItemsTempProp",
				type : "GET",
				async : false,
				data :{'joQuoteDetailID':lineitemid} ,
				success : function(data) {
						if(data!=null){
							TempcheckedCheckboxFromDB(data);
							}else{
								resettemppropvalues();
							}
					
					jQuery("#LineItemTempProperties").dialog("open");
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
   function savequoteTemplineitemproperties(){
		
		var lineItemTempProperties=$("#LineItemtempPropertyID").val();
		var Italic_ItemTempID=$("#Italic_ItemTempID").val();
		var UnderLine_ItemTempID=$("#UnderLine_ItemTempID").val();
		var Bold_ItemTempID=$("#Bold_ItemTempID").val();
		var Italic_QuantityTempID=$("#Italic_QuantityTempID").val();
		var UnderLine_QuantityTempID=$("#UnderLine_QuantityTempID").val();
		var Bold_QuantityTempID=$("#Bold_QuantityTempID").val();
		var Italic_ParagraphTempID=$("#Italic_ParagraphTempID").val();
		var UnderLine_ParagraphTempID=$("#UnderLine_ParagraphTempID").val();
		var Bold_ParagraphTempID=$("#Bold_ParagraphTempID").val();
		var Italic_ManufacturerTempID=$("#Italic_ManufacturerTempID").val();
		var UnderLine_ManufacturerTempID=$("#UnderLine_ManufacturerTempID").val();
		var Bold_ManufacturerTempID=$("#Bold_ManufacturerTempID").val();
		var Italic_SpecTempID=$("#Italic_SpecTempID").val();
		var UnderLine_SpecTempID=$("#UnderLine_SpecTempID").val();
		var Bold_SpecTempID=$("#Bold_SpecTempID").val();
		var Italic_MultTempID=$("#Italic_MultTempID").val();
		var UnderLine_MultTempID=$("#UnderLine_MultTempID").val();
		var Bold_MultTempID=$("#Bold_MultTempID").val();
		var Italic_PriceTempID=$("#Italic_PriceTempID").val();
		var UnderLine_PriceTempID=$("#UnderLine_PriceTempID").val();
		var Bold_PriceTempID=$("#Bold_PriceTempID").val();
		
		var JoQLineItemstempPropId=0;
		var overalldata="JoQLineItemsPropId="+JoQLineItemstempPropId+"&LineItemProperties="+lineItemTempProperties+"&Italic_ItemID="+Italic_ItemTempID+
		"&UnderLine_ItemID="+UnderLine_ItemTempID+"&Bold_ItemID="+Bold_ItemTempID+
		"&Italic_QuantityID="+Italic_QuantityTempID+"&UnderLine_QuantityID="+UnderLine_QuantityTempID+"&Bold_QuantityID="+Bold_QuantityTempID+
		"&Italic_ParagraphID="+Italic_ParagraphTempID+"&UnderLine_ParagraphID="+UnderLine_ParagraphTempID+"&Bold_ParagraphID="+Bold_ParagraphTempID+
		"&Italic_ManufacturerID="+Italic_ManufacturerTempID+"&UnderLine_ManufacturerID="+UnderLine_ManufacturerTempID+"&Bold_ManufacturerID="+Bold_ManufacturerTempID+
		"&Italic_SpecID="+Italic_SpecTempID+"&UnderLine_SpecID="+UnderLine_SpecTempID+"&Bold_SpecID="+Bold_SpecTempID+
		"&Italic_MultID="+Italic_MultTempID+"&UnderLine_MultID="+UnderLine_MultTempID+"&Bold_MultID="+Bold_MultTempID+
		"&Italic_PriceID="+Italic_PriceTempID+"&UnderLine_PriceID="+UnderLine_PriceTempID+"&Bold_PriceID="+Bold_PriceTempID;
		  $.ajax({
				url : "./jobtabs2/LineItemTemplateProperties",
				type : "GET",
				async : false,
				data :overalldata ,
				success : function(data) {
					jQuery("#LineItemTempProperties").dialog("close");
					$("#quoteTemplateProductsGrid").trigger("reloadGrid");
				}
			});
		 //
		   }


   function resettemppropvalues(){

	   $("#Italic_ItemTempID").attr('checked',false);
		$("#UnderLine_ItemTempID").attr('checked',false);
		$("#Bold_ItemTempID").attr('checked',false);
		$("#Italic_QuantityTempID").attr('checked',false);
		$("#UnderLine_QuantityTempID").attr('checked',false);
		$("#Bold_QuantityTempID").attr('checked',false);
		$("#Italic_ParagraphTempID").attr('checked',false);
		$("#UnderLine_ParagraphTempID").attr('checked',false);
		$("#Bold_ParagraphTempID").attr('checked',false);
		$("#Italic_ManufacturerTempID").attr('checked',false);
		$("#UnderLine_ManufacturerTempID").attr('checked',false);
		$("#Bold_ManufacturerTempID").attr('checked',false);
		$("#Italic_SpecTempID").attr('checked',false);
		$("#UnderLine_SpecTempID").attr('checked',false);
		$("#Bold_SpecTempID").attr('checked',false);
		$("#Italic_MultTempID").attr('checked',false);
		$("#UnderLine_MultTempID").attr('checked',false);
		$("#Bold_MultTempID").attr('checked',false);
		$("#Italic_PriceTempID").attr('checked',false);
		$("#UnderLine_PriceTempID").attr('checked',false);
		$("#Bold_PriceTempID").attr('checked',false);

		$("#Italic_ItemTempID").val("false");
		$("#UnderLine_ItemTempID").val("false");
		$("#Bold_ItemTempID").val("false");
		$("#Italic_QuantityTempID").val("false");
		$("#UnderLine_QuantityTempID").val("false");
		$("#Bold_QuantityTempID").val("false");
		$("#Italic_ParagraphTempID").val("false");
		$("#UnderLine_ParagraphTempID").val("false");
		$("#Bold_ParagraphTempID").val("false");
		$("#Italic_ManufacturerTempID").val("false");
		$("#UnderLine_ManufacturerTempID").val("false");
		$("#Bold_ManufacturerTempID").val("false");
		$("#Italic_SpecTempID").val("false");
		$("#UnderLine_SpecTempID").val("false");
		$("#Bold_SpecTempID").val("false");
		$("#Italic_MultTempID").val("false");
		$("#UnderLine_MultTempID").val("false");
		$("#Bold_MultTempID").val("false");
		$("#Italic_PriceTempID").val("false");
		$("#UnderLine_PriceTempID").val("false");
		$("#Bold_PriceTempID").val("false");
		
		$("#Italic_ItemlbTempID").text("(No)");
		$("#UnderLine_ItemlbTempID").text("(No)");
		$("#Bold_ItemlbTempID").text("(No)");
		$("#Italic_QuantitylbTempID").text("(No)");
		$("#UnderLine_QuantitylbTempID").text("(No)");
		$("#Bold_QuantitylbTempID").text("(No)");
		$("#Italic_ParagraphlbTempID").text("(No)");
		$("#UnderLine_ParagraphlbTempID").text("(No)");
		$("#Bold_ParagraphlbTempID").text("(No)");
		$("#Italic_ManufacturerlbTempID").text("(No)");
		$("#UnderLine_ManufacturerlbTempID").text("(No)");
		$("#Bold_ManufacturerlbTempID").text("(No)");
		$("#Italic_SpeclbTempID").text("(No)");
		$("#UnderLine_SpeclbTempID").text("(No)");
		$("#Bold_SpeclbTempID").text("(No)");
		$("#Italic_MultlbTempID").text("(No)");
		$("#UnderLine_MultlbTempID").text("(No)");
		$("#Bold_MultlbTempID").text("(No)");
		$("#Italic_PricelbTempID").text("(No)");
		$("#UnderLine_PricelbTempID").text("(No)");
		$("#Bold_PricelbTempID").text("(No)");
		
		
		}
	function TempcheckedCheckboxFromDB(data){
		
		
		resettemppropvalues();
		if(data.italicItem){
			$("#Italic_ItemTempID").attr('checked',true);
			$("#Italic_ItemlbTempID").text("(Yes)");
			$("#Italic_ItemTempID").val("true");
			}
		if(data.underlineItem){
			$("#UnderLine_ItemTempID").attr('checked',true);
			$("#UnderLine_ItemlbTempID").text("(Yes)");
			$("#UnderLine_ItemTempID").val("true");
			
			}	
		if(data.boldItem){
			$("#Bold_ItemTempID").attr('checked',true);
			$("#Bold_ItemlbTempID").text("(Yes)");
			$("#Bold_ItemTempID").val("true");
		}	
		if(data.italicQuantity){
			$("#Italic_QuantityTempID").attr('checked',true);
			$("#Italic_QuantitylbTempID").text("(Yes)");
			$("#Italic_QuantityTempID").val("true");
		}	
		if(data.underlineQuantity){
			$("#UnderLine_QuantityTempID").attr('checked',true);
			$("#UnderLine_QuantitylbTempID").text("(Yes)");
			$("#UnderLine_QuantityTempID").val("true");
		}	
		if(data.boldQuantity){
			$("#Bold_QuantityTempID").attr('checked',true);
			$("#Bold_QuantitylbTempID").text("(Yes)");
			$("#Bold_QuantityTempID").val("true");
		}	
		if(data.italicParagraph){
			$("#Italic_ParagraphTempID").attr('checked',true);
			$("#Italic_ParagraphlbTempID").text("(Yes)");
			$("#Italic_ParagraphTempID").val("true");
		}	
		if(data.underlineParagraph){
			$("#UnderLine_ParagraphTempID").attr('checked',true);
			$("#UnderLine_ParagraphlbTempID").text("(Yes)");
			$("#UnderLine_ParagraphTempID").val("true");
		}	
		if(data.boldParagraph){
			$("#Bold_ParagraphTempID").attr('checked',true);
			$("#Bold_ParagraphlbTempID").text("(Yes)");
			$("#Bold_ParagraphTempID").val("true");
		}	
		if(data.italicManufacturer){
			$("#Italic_ManufacturerTempID").attr('checked',true);
			$("#Italic_ManufacturerlbTempID").text("(Yes)");
			$("#Italic_ManufacturerTempID").val("true");
		}	
		if(data.underlineManufactur){
			$("#UnderLine_ManufacturerTempID").attr('checked',true);
			$("#UnderLine_ManufacturerlbTempID").text("(Yes)");
			$("#UnderLine_ManufacturerTempID").val("true");
			}
		if(data.boldManufacturer){
			$("#Bold_ManufacturerTempID").attr('checked',true);
			$("#Bold_ManufacturerlbTempID").text("(Yes)");
			$("#Bold_ManufacturerTempID").val("true");
			}
		if(data.italicSpec){
			$("#Italic_SpecTempID").attr('checked',true);
			$("#Italic_SpeclbTempID").text("(Yes)");
			$("#Italic_SpecTempID").val("true");
		}
		if(data.underlineSpec){
			$("#UnderLine_SpecTempID").attr('checked',true);
			$("#UnderLine_SpeclbTempID").text("(Yes)");
			$("#UnderLine_SpecTempID").val("true");
		}
		if(data.boldSpec){
			$("#Bold_SpecTempID").attr('checked',true);
			$("#Bold_SpeclbTempID").text("(Yes)");
			$("#Bold_SpecTempID").val("true");
		}
		if(data.italicMult){
			$("#Italic_MultTempID").attr('checked',true);
			$("#Italic_MultlbTempID").text("(Yes)");
			$("#Italic_MultTempID").val("true");
			}
		if(data.underlineMult){
			$("#UnderLine_MultTempID").attr('checked',true);
			$("#UnderLine_MultlbTempID").text("(Yes)");
			$("#UnderLine_MultTempID").val("true");
		}
		if(data.boldMult){
			$("#Bold_MultTempID").attr('checked',true);
			$("#Bold_MultlbTempID").text("(Yes)");
			$("#Bold_MultTempID").val("true");
		}
		if(data.italicPrice){
			$("#Italic_PriceTempID").attr('checked',true);
			$("#Italic_PricelbTempID").text("(Yes)");
			$("#Italic_PriceTempID").val("true");
		}
		if(data.underlinePrice){
			$("#UnderLine_PriceTempID").attr('checked',true);
			$("#UnderLine_PricelbTempID").text("(Yes)");
			$("#UnderLine_PriceTempID").val("true");
		}
		if(data.boldPrice){
			$("#Bold_PriceTempID").attr('checked',true);
			$("#Bold_PricelbTempID").text("(Yes)");
			$("#Bold_PriceTempID").val("true");
			}
		
		
		
		
		
		
		
		

		}

	 function TriggerTempLabel(checkboxid,labelid){
		  if($("#"+checkboxid).is(':checked')==true){
			  $("#"+labelid).text("(Yes)");
			  $("#"+checkboxid).val("true");
		  }else{
			  $("#"+labelid).text("(No)");
			  $("#"+checkboxid).val("false");
		  }
		  }
</script>
