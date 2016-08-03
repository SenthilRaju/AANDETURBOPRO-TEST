/** Add Quotes function * */
var colnamesaddQuotes = [];
var colnamesDefaults = [];
var colModeladdQuotes = [];
var lastSel,dataCol;
var area;
var col1='';
var col2='';
var col3='';
var col4='';
var col5='';
var col6='';
var col7='';
var col8 = '';
var joQuoteTemplateHeaderId = '';
$('input[type=text]').on('keyup', function(e) {
	if (e.which == 13) {
		return false;
	}
});
function addQuotesColumnDetails() {
	col1==0,col2==0,col3==0,col4==0,col5==0,col6==0,col7==0;
	var colName_qty = 'Qty.';
	var colModel_qty = {name : 'itemQuantity',index : 'itemQuantity',align : 'center',width : 30,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_Paragraph = 'Paragraph';
	var colModel_Paragraph = {name : 'paragraph',index : 'paragraph',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_Vendors = 'Vendors.';
	var colModel_Vendors = {name : 'manufacturer',index : 'manufacturer',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {dataInit : function(elem) {$(elem).autocomplete({source : 'jobtabs2/vendorsList',minLength : 3,select : function(event, ui) {var id = ui.item.id;var name = ui.item.value;$("#new_row_rxManufacturerID").val(id);$("#rxManufacturerID").val(id);$.ajax({url : "./jobtabs2/getFactoryID",type : "GET",data : {'rxMasterID' : id,'descripition' : name},success : function(data) {$("#new_row_veFactoryId").val(data);$("#veFactoryId").val(data);}});}});}},editrules : {edithidden : false,required : true}};
	var colModel_Hidden_Vendors = {name : 'manufacturer',index : 'manufacturer',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {dataInit : function(elem) {$(elem).autocomplete({source : 'jobtabs2/vendorsList',minLength : 3,select : function(event, ui) {var id = ui.item.id;var name = ui.item.value;$("#new_row_rxManufacturerID").val(id);$("#rxManufacturerID").val(id);$.ajax({url : "./jobtabs2/getFactoryID",type : "GET",data : {'rxMasterID' : id,'descripition' : name},success : function(data) {$("#new_row_veFactoryId").val(data);$("#veFactoryId").val(data);}});}});}},editrules : {edithidden : false,required : true}};
	var colName_Spec = 'Spec.';
	var colModel_Spec = {name : 'spec',index : 'spec',align : 'right',width : 20,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_Cost = 'Cost.';
	var colModel_Cost = {name : 'cost',index : 'cost',align : 'right',width : 50,editable : true,hidden : false,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_CostHidden = 'Cost.';
	var colModel_CostHidden = {name : 'cost',index : 'cost',align : 'right',width : 50,editable : true,hidden : true,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_Multi = 'Multi.';
	var colModel_Multi = {name : 'mult',index : 'mult',align : 'right',width : 20,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_SP = 'Sell Price.';
	var colModel_SP = {name : 'price',index : 'price',align : 'right',width : 50,editable : true,hidden : false,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colName_SPHidden = 'Sell Price.';
	var colModel_SPHidden = {name : 'price',index : 'price',align : 'right',width : 50,editable : true,hidden : true,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}};
	var colPosition = colnamesaddQuotes.length;
	colnamesaddQuotes===[];
	colModeladdQuotes===[];
	colnamesaddQuotes = [ '','', 'Description' ];
	colnamesDefaults = [ 'QuoteDetailID', 'ManufacturerID', 'QuoteHeaderID', 'FactoryID', 'LineItem', 'FootLine', 'Posiion', 'Move','Percentage', 'Options' ];
	colModeladdQuotes = [
	        {name : 'inLineNoteImage',index : 'inLineNoteImage',align : 'right',width : 10,editable : false,hidden : false,formatter : PropertyImage},
			{name : 'inLineNoteImage',index : 'inLineNoteImage',align : 'right',width : 10,editable : false,hidden : false,formatter : inlineImage},
			{name : 'product',index : 'product',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {dataInit : function(elem) {
						$(elem).autocomplete({source : 'jobtabs2/productList',minLength : 3,select : function(event, ui) {var quoteDetailID = ui.item.id;var rxMasterID = ui.item.manufactureID;
						$.ajax({url : "./jobtabs2/quoteDetails",mType : "GET",data : {'quoteDetailID' : quoteDetailID,'rxMasterID' : rxMasterID},
							success : function(data) {
								$.each(data,function(index,value) {
									manufacture = value.inlineNote;
									var detail = value.paragraph;
									var quantity = value.itemQuantity;
									var manufactureID = value.rxManufacturerID;
									var factoryID = value.detailSequenceId;
									$("#new_row_paragraph").val(detail);
									$("#new_row_manufacturer").val(quantity); // $("#itemQuantity").val(quantity);
									$("#new_row_rxManufacturerID").val(manufactureID);
									$("#new_row_veFactoryId").val(factoryID);
									$("#paragraph").val(detail);
									$("#manufacturer").val(quantity); 
									$("#rxManufacturerID").val(manufactureID);
									$("#veFactoryId").val(factoryID);
								});
							}});}});}},editrules : {edithidden : false,required : true}} ];
	var columndefaults = [ {name : 'joQuoteDetailID',index : 'joQuoteDetailID',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'rxManufacturerID',index : 'rxManufacturerID',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}},
	                       {name : 'joQuoteHeaderID',index : 'joQuoteHeaderID',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'veFactoryId',index : 'veFactoryId',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}},
	                       {name : 'inlineNote',index : 'inlineNote',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'productNote',index : 'productNote',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'position',index : 'position',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'upAndDown',index : 'upAndDown',align : 'left',width : 30,formatter : upAndDownImage}, 
	                       {name : 'percentage',index : 'percentage',align : 'right',width : 50,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {edithidden : false,required : false}}, 
	                       {name : 'edit',index : 'edit',align : 'center',width : 40,hidden : false,editrules : {edithidden : false},formatter : imgFmatter} 
	                       ];
	
	$.ajax({url: "./jobtabs2/getQuotePropertyIdOfUser", mType: "GET",
		success: function(data) {	
			dataCol===data;
			if(data.displayQuantity){
				$("#quantityValueDisplayID").val( data.displayQuantity);
				$("#quantityValueDisplayParaID").val(data.displayParagraph);
				$("#quantityValueDisplayManufID").val(data.displayManufacturer);
				$("#quantityValueDispalyMultiID").val(data.displayMult);
				$("#quantityValueDisplaySpecID").val(data.displaySpec);
				$("#quantityValueDisplayPriceID").val(data.displayPrice);
				$("#quantityValuePrintID").val(data.printQuantity);
				$("#quantityValuePrintParaID").val(data.printParagraph);
				$("#quantityValuePrintManufID").val(data.printManufacturer);
				$("#quantityValuePrintSpecID").val(data.printSpec);
				$("#quantityValueprintCostID").val(data.displayCost);
				$("#quantityValuePrintCostID").val(data.printCost);
				$("#quantityValueprintMultiID").val(data.displayMult);
				$("#quantityValuePrintPriceID").val(data.printPrice);
				/*@Column(nullable=false)
				private byte underlineCost;

				@Column(nullable=false)
				private byte underlineManufacturer;

				@Column(nullable=false)
				private byte underlineMult;

				@Column(nullable=false)
				private byte underlineParagraph;

				@Column(nullable=false)
				private byte underlinePrice;

				@Column(nullable=false)
				private byte underlineQuantity;

				@Column(nullable=false)
				private byte underlineSpec;
				@Column(nullable=false)
				private byte boldCost;

				@Column(nullable=false)
				private byte boldManufacturer;

				@Column(nullable=false)
				private byte boldMult;

				@Column(nullable=false)
				private byte boldParagraph;

				@Column(nullable=false)
				private byte boldPrice;

				@Column(nullable=false)
				private byte boldQuantity;

				@Column(nullable=false)
				private byte boldSpec;	
				
				@Column(nullable=false)
				private byte displayItem;
				
				@Column(nullable=false)
				private byte printItem;
				
				@Column(nullable=false)
				private byte underlineItem;
				
				@Column(nullable=false)
				private byte boldItem;*/
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
				console.log(col1, col2, col3, col4, col5, col6, col7);
			}else {
				$('#joQuotePropertyID').val(0);
				colnamesaddQuotes.push(colName_Vendors);
				colModeladdQuotes.push(colModel_Hidden_Vendors);
				colnamesaddQuotes.push(colName_Cost);
				colModeladdQuotes.push(colModel_CostHidden);
				colnamesaddQuotes.push(colName_SPHidden);
				colModeladdQuotes.push(colModel_SPHidden);
			}
			}});

	if (col1 == 1) {
		colnamesaddQuotes.push(colName_qty);
		colModeladdQuotes.push(colModel_qty);
		colPosition = colPosition + 1;
	}
	if (col2 == 1) {
		colnamesaddQuotes.push(colName_Paragraph);
		colModeladdQuotes.push(colModel_Paragraph);
		colPosition = colPosition + 1;
	}
	if (col3 == 1) {
		colnamesaddQuotes.push(colName_Vendors);
		colModeladdQuotes.push(colModel_Vendors);
		colPosition = colPosition + 1;
	} else {
		colnamesaddQuotes.push(colName_Vendors);
		colModeladdQuotes.push(colModel_Hidden_Vendors);
		colPosition = colPosition + 1;
	}
	if (col4 == 1) {
		colnamesaddQuotes.push(colName_Spec);
		colModeladdQuotes.push(colModel_Spec);
		colPosition = colPosition + 1;
	}
	if (col5 == 1) {
		colnamesaddQuotes.push(colName_Cost);
		colModeladdQuotes.push(colModel_Cost);
		colPosition = colPosition + 1;
	}
	if (col5 == 0){
		colnamesaddQuotes.push(colName_CostHidden);
		colModeladdQuotes.push(colModel_CostHidden);
		colPosition = colPosition + 1;
	}
	if (col6 == 1) {
		colnamesaddQuotes.push(colName_Multi);
		colModeladdQuotes.push(colModel_Multi);
		colPosition = colPosition + 1;
	}
	if (col8 == 0){
		colnamesaddQuotes.push(colName_SPHidden);
		colModeladdQuotes.push(colModel_SPHidden);
	}
	if (col8 == 1) {
		colnamesaddQuotes.push(colName_SP);
		colModeladdQuotes.push(colModel_SP);
	}
	for ( var i = 0; i < colnamesDefaults.length; i++) {
		colnamesaddQuotes.push(colnamesDefaults[i]);
		colModeladdQuotes.push(columndefaults[i]);
	}
	}
function PropertyImage(cellValue, options, rowObject) {
	var element = "<div><a onclick='editQuoteLineItemFrom()'><img src='./../resources/images/edit.png' title='Properties' align='middle' style='padding: 2px 5px;'></a></div>";
	return element;
}
function loadQuotesListDetails() {
	addQuotesColumnDetails();
	alert(colnamesaddQuotes);
	var joQuoteheaderID = $("#joQuoteheader").text();
	$("#addquotesList").jqGrid({
		datatype : 'JSON',
		url : './jobtabs2/getQuoteListDetails',
		mtype : 'GET',
		async: false,
		postData : {
			'joQuoteHeaderID' : joQuoteheaderID
			},
		pager : jQuery('#addquotespager'),
		colNames : colnamesaddQuotes,
		colModel : colModeladdQuotes,
		rowNum : 1000,pgbuttons : false,recordtext : '',rowList : [],pgtext : null,viewrecords : true,sortname : 'Product',sortorder : "asc",imgpath : 'themes/basic/images',caption : 'Line Items',height : 190,width : 1100,
		loadComplete : function(data) {
							/*
							 * $("#addquotesList").setSelection(1, true); var
							 * gridData = jQuery("#addquotesList").getRowData();
							 * var totalcost = 0; var totalPrice = 0; for(var
							 * index = 0; index < gridData.length; index++){ var
							 * rowData = gridData[index]; var cost =
							 * rowData["cost"].replace(/[^0-9\.]+/g,"");
							 * totalcost = totalcost + Number(cost); var price =
							 * rowData["price"].replace(/[^0-9\.]+/g,"");
							 * totalPrice = totalPrice + Number(price); }
							 * $("#quoteTotalPrice").val(formatCurrency(totalPrice));
							 * $("#quoteTotalCost").val(formatCurrency(totalcost));
							 * var last =
							 * $(this).jqGrid('getGridParam','records'); var
							 * hideDownIcon = Number(last)+1; if(last){
							 * $("#"+hideDownIcon+"_downIcon").css("display",
							 * "none"); }
							 */
						},
						ondblClickRow : function(rowid) {
							onDoubleClickEditLineItems(rowid);
						},
						gridComplete : function() {
							$(this).mouseover(function() {
								var valId = $(".ui-state-hover").attr("id");
								$(this).setSelection(valId, false);
							});
						},
						afterInsertRow : function(rowid, aData) {
							var aPositionID = $("#addquotesList").jqGrid(
									'getCell', rowid, 'position');
							if (aPositionID === '0') {
								var aQuoteDetailID = $("#addquotesList")
										.jqGrid('getCell', rowid,
												'joQuoteDetailID');
								$
										.ajax({
											url : "./jobtabs2/updateInlineItemPosition",
											type : "GET",
											data : {
												'joQuoteDetailID' : aQuoteDetailID
											},
											success : function(data) {
												$("#addquotesList").trigger(
														"reloadGrid");
											}
										});
							}
						},
						loadError : function(jqXHR, textStatus, errorThrown) {
						},
						jsonReader : {
							root : "rows",
							page : "page",
							total : "total",
							records : "records",
							repeatitems : false,
							cell : "cell",
							id : "id",
							userdata : "userdata"
						},
						onSelectRow : function(id) {
							if (id && id !== lastSel) {
								jQuery('#addquotesList').restoreRow(lastSel);
								lastSel = id;
							}
							// jQuery('#addquotesList').editRow(id, true);
						},
						editurl : './jobtabs2/manpulaterProductQuotes'
					})
			.navGrid(
					'#addquotespager',
					{
						alertzIndex : 3234,
						search : false,
						pager : false,
						alertcap : "Warning",
						alerttext : 'Please select a Product'
					},
					// -----------------------edit
					// options----------------------//
					{
						closeAfterEdit : true,
						reloadAfterSubmit : true,
						reloadGridAfterSubmit : true,
						closeOnEscape : true,
						zIndex : 1234,
						modal : true,
						jqModal : true,
						viewPagerButtons : false,
						editCaption : "Edit Product",
						width : 430,
						top : 235,
						left : 320,
						beforeShowForm : function(form) {
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append('Description: ');
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery(
									'#TblGrid_addquotesList #tr_itemQuantity .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_itemQuantity .CaptionTD')
									.append('Qty.: ');
							jQuery(
									'#TblGrid_addquotesList #tr_paragraph .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_paragraph .CaptionTD')
									.append('Paragraph: ');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append('Vendors: ');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD')
									.empty();
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD')
									.append('Cost: ');
							jQuery(
									'#TblGrid_addquotesList #tr_price .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_price .CaptionTD')
									.append('Sell Price: ');
							jQuery(
									'#TblGrid_addquotesList #tr_inlineNote .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_inlineNote .CaptionTD')
									.append('Inline Note: ');
							jQuery(
									'#TblGrid_addquotesList #tr_productNote .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_productNote .CaptionTD')
									.append('Foot Note: ');
							var cost = $("#cost").val().replace(/[^0-9\.]+/g,
									"");
							var price = $("#price").val().replace(/[^0-9\.]+/g,
									"");
							$("#cost").val(Number(cost));
							$("#price").val(Number(price));
							$(function() {
								var cache = {};
								var lastXhr = '';
								$("#product")
										.autocomplete(
												{
													minLength : 2,
													select : function(event, ui) {
														var quoteDetailID = ui.item.id;
														var rxMasterID = ui.item.manufactureID;
														$("#quoteDetailID")
																.val(
																		quoteDetailID);
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
																									"#paragraph")
																									.val(
																											detail);
																							$(
																									"#manufacturer")
																									.val(
																											quantity); // $("#itemQuantity").val(quantity);
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
													},
													source : function(request,
															response) {
														var term = request.term;
														if (term in cache) {
															response(cache[term]);
															return;
														}
														lastXhr = $
																.getJSON(
																		"jobtabs2/productList",
																		request,
																		function(
																				data,
																				status,
																				xhr) {
																			cache[term] = data;
																			if (xhr === lastXhr) {
																				response(data);
																			}
																		});
													},
													error : function(result) {
														$(
																'.ui-autocomplete-loading')
																.removeClass(
																		"ui-autocomplete-loading");
													}
												});
							});

							$(function() {
								var cache = {};
								var lastXhr = '';
								$("#manufacturer")
										.autocomplete(
												{
													minLength : 1,
													select : function(event, ui) {
														var id = ui.item.id;
														var name = ui.item.value;
														$("#rxManufacturerID")
																.val(id);
														$
																.ajax({
																	url : "./jobtabs2/getFactoryID",
																	type : "GET",
																	data : {
																		'rxMasterID' : id,
																		'descripition' : name
																	},
																	success : function(
																			data) {
																		$(
																				"#veFactoryId")
																				.val(
																						data);
																	}
																});
													},
													source : function(request,
															response) {
														var term = request.term;
														if (term in cache) {
															response(cache[term]);
															return;
														}
														lastXhr = $
																.getJSON(
																		"jobtabs2/vendorsList",
																		request,
																		function(
																				data,
																				status,
																				xhr) {
																			cache[term] = data;
																			if (xhr === lastXhr) {
																				response(data);
																			}
																		});
													},
													error : function(result) {
														$(
																'.ui-autocomplete-loading')
																.removeClass(
																		"ui-autocomplete-loading");
													}
												});
							});
						},
						'onInitializeForm' : function(formid) {
							jQuery('#TblGrid_addquotesList #tr_product .DataTD')
									.append(
											'<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .DataTD')
									.append(
											'<label>(Must be atleast 2 characters long)</label>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .DataTD')
									.append(
											'<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .DataTD')
									.append(
											'<label>(Must be atleast 2 characters long)</label>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_inlineNote')
									.show();
							jQuery('#TblGrid_addquotesList #tr_productNote')
									.show();
						},
						beforeSubmit : function(postdata, formid) {

							if ($("#quoteTypeDetail").val() === '-1') {
								return [ false,
										"Please provide 'Type' and 'Submitted By'" ];
							} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
								return [ false,
										"Please provide 'Type' and 'Submitted By'" ];
							}
							return [ true, "" ];
						},
						afterSubmit : function() {
							$('#cData').trigger('click');
							$("#addquotesList").trigger("reloadGrid");
							var gridData = jQuery("#addquotesList")
									.getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for ( var index = 0; index < gridData.length; index++) {
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(
										/[^0-9\.]+/g, "");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(
										/[^0-9\.]+/g, "");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = $("#cost").val().replace(
									/[^0-9\.]+/g, "");
							var aCurrentPrice = $("#price").val().replace(
									/[^0-9\.]+/g, "");
							totalcost = totalcost + Number(aCurrentCost);
							totalPrice = totalPrice + Number(aCurrentPrice);
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$
									.ajax({
										url : "./jobtabs2/SaveQuoteCustomerDetailInfo",
										type : "GET",
										data : {
											'totalCost' : totalcost,
											'totalPrice' : totalPrice,
											'joQuoteHeaderID' : joQuoteHeader
										},
										success : function(data) {
											var newDialogDiv = jQuery(document
													.createElement('div'));
											jQuery(newDialogDiv)
													.html(
															'<span><b style="color:Green;">Quote details updated successfully.</b></span>');
											jQuery(newDialogDiv)
													.dialog(
															{
																modal : true,
																width : 300,
																height : 150,
																title : "Success",
																buttons : [ {
																	height : 35,
																	text : "OK",
																	click : function() {
																		$(this)
																				.dialog(
																						"close");
																	}
																} ]
															}).dialog("open");
											jQuery(this).dialog("close");
											return true;
										}
									});
						},
						onclickSubmit : function(params) {

						}
					},
					// -----------------------add
					// options----------------------//
					{
						closeAfterAdd : true,
						reloadAfterSubmit : true,
						reloadGridAfterSubmit : true,
						closeOnEscape : true,
						modal : true,
						jqModal : true,
						viewPagerButtons : false,
						addCaption : "Add Product",
						width : 430,
						top : 235,
						left : 320,
						zIndex : 1234,
						beforeShowForm : function(form) {
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append('Description: ');
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery(
									'#TblGrid_addquotesList #tr_itemQuantity .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_itemQuantity .CaptionTD')
									.append('Qty.: ');
							jQuery(
									'#TblGrid_addquotesList #tr_paragraph .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_paragraph .CaptionTD')
									.append('Paragraph: ');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append('Vendors: ');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD')
									.empty();
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD')
									.append('Cost: ');
							jQuery(
									'#TblGrid_addquotesList #tr_price .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_price .CaptionTD')
									.append('Sell Price: ');
							jQuery(
									'#TblGrid_addquotesList #tr_inlineNote .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_inlineNote .CaptionTD')
									.append('Inline Note: ');
							jQuery(
									'#TblGrid_addquotesList #tr_productNote .CaptionTD')
									.empty();
							jQuery(
									'#TblGrid_addquotesList #tr_productNote .CaptionTD')
									.append('Foot Note: ');
							$(function() {
								var cache = {};
								var lastXhr = '';
								$("#product")
										.autocomplete(
												{
													minLength : 2,
													select : function(event, ui) {
														var quoteDetailID = ui.item.id;
														var rxMasterID = ui.item.manufactureID;
														$("#quoteDetailID")
																.val(
																		quoteDetailID);
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
																									"#paragraph")
																									.val(
																											detail);
																							$(
																									"#manufacturer")
																									.val(
																											quantity); // $("#itemQuantity").val(quantity);
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
													},
													source : function(request,
															response) {
														var term = request.term;
														if (term in cache) {
															response(cache[term]);
															return;
														}
														lastXhr = $
																.getJSON(
																		"jobtabs2/productList",
																		request,
																		function(
																				data,
																				status,
																				xhr) {
																			cache[term] = data;
																			if (xhr === lastXhr) {
																				response(data);
																			}
																		});
													},
													error : function(result) {
														$(
																'.ui-autocomplete-loading')
																.removeClass(
																		"ui-autocomplete-loading");
													}
												});
							});

							$(function() {
								var cache = {};
								var lastXhr = '';
								$("#manufacturer")
										.autocomplete(
												{
													minLength : 1,
													select : function(event, ui) {
														var id = ui.item.id;
														var name = ui.item.value;
														$("#rxManufacturerID")
																.val(id);
														$
																.ajax({
																	url : "./jobtabs2/getFactoryID",
																	type : "GET",
																	data : {
																		'rxMasterID' : id,
																		'descripition' : name
																	},
																	success : function(
																			data) {
																		$(
																				"#veFactoryId")
																				.val(
																						data);
																	}
																});
													},
													source : function(request,
															response) {
														var term = request.term;
														if (term in cache) {
															response(cache[term]);
															return;
														}
														lastXhr = $
																.getJSON(
																		"jobtabs2/vendorsList",
																		request,
																		function(
																				data,
																				status,
																				xhr) {
																			cache[term] = data;
																			if (xhr === lastXhr) {
																				response(data);
																			}
																		});
													},
													error : function(result) {
														$(
																'.ui-autocomplete-loading')
																.removeClass(
																		"ui-autocomplete-loading");
													}
												});
							});
						},
						'onInitializeForm' : function(formid) {
							jQuery('#TblGrid_addquotesList #tr_product .DataTD')
									.append(
											'<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .DataTD')
									.append(
											'<label>(Must be atleast 2 characters long)</label>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .DataTD')
									.append(
											'<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
							jQuery(
									'#TblGrid_addquotesList #tr_product .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .DataTD')
									.append(
											'<label>(Must be atleast 2 characters long)</label>');
							jQuery(
									'#TblGrid_addquotesList #tr_manufacturer .CaptionTD')
									.append(
											'<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_inlineNote')
									.show();
							jQuery('#TblGrid_addquotesList #tr_productNote')
									.show();
						},
						onclickSubmit : function(params) {
							aGlobalVariable = "add";
							var joQuoteHeaderIdentity = joQuoteheaderID;
							if (aGlobalVariable !== "edit") {
								$("#joQuoteHeaderID")
										.val(joQuoteHeaderIdentity);
								$('#addquotesList')
										.jqGrid(
												'setGridParam',
												{
													postData : {
														'joQuoteHeaderID' : joQuoteHeaderIdentity
													}
												});
								jQuery("#joQuoteheader").text(
										joQuoteHeaderIdentity);
								joQuoteheaderID = joQuoteHeaderIdentity;
								$("#joHeaderID").val(joQuoteheaderID);
							}
							if (Number(joQuoteHeaderIdentity) === 0) {
								var name = $("#joHeaderID").val();
								$('#addquotesList').jqGrid('setGridParam', {
									postData : {
										'joQuoteHeaderID' : name
									}
								});
								jQuery("#joQuoteheader").text(name);
								joQuoteheaderID = name;
								$("#joHeaderID").val(joQuoteheaderID);
							}
							if (joQuoteHeaderIdentity > joQuoteheaderID) {
								joQuoteheaderID = joQuoteHeaderIdentity;
							}
							return {
								'joQuoteHeaderID' : joQuoteheaderID,
								'quoteheaderid' : joQuoteHeaderIdentity
							};
						},
						beforeSubmit : function(postdata, formid) {
							aGlobalVariable = "edit";
							if ($("#quoteTypeDetail").val() === '-1') {
								return [ false,
										"Please provide 'Type' and 'Submitted By'" ];
							} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
								return [ false,
										"Please provide 'Type' and 'Submitted By'" ];
							}
							return [ true, "" ];
						},
						afterSubmit : function() {
							$('#cData').trigger('click');
							$("#addquotesList").trigger("reloadGrid");
							var gridData = jQuery("#addquotesList")
									.getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for ( var index = 0; index < gridData.length; index++) {
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(
										/[^0-9\.]+/g, "");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(
										/[^0-9\.]+/g, "");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = $("#cost").val().replace(
									/[^0-9\.]+/g, "");
							var aCurrentPrice = $("#price").val().replace(
									/[^0-9\.]+/g, "");
							totalcost = totalcost + Number(aCurrentCost);
							totalPrice = totalPrice + Number(aCurrentPrice);
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$
									.ajax({
										url : "./jobtabs2/SaveQuoteCustomerDetailInfo",
										type : "GET",
										data : {
											'totalCost' : totalcost,
											'totalPrice' : totalPrice,
											'joQuoteHeaderID' : joQuoteHeader
										},
										success : function(data) {
											var newDialogDiv = jQuery(document
													.createElement('div'));
											jQuery(newDialogDiv)
													.html(
															'<span><b style="color:Green;">Quote details updated successfully.</b></span>');
											jQuery(newDialogDiv)
													.dialog(
															{
																modal : true,
																width : 300,
																height : 150,
																title : "keySuccess.",
																buttons : [ {
																	height : 35,
																	text : "OK",
																	click : function() {
																		$(this)
																				.dialog(
																						"close");
																	}
																} ]
															}).dialog("open");
											jQuery(this).dialog("close");
											return true;
										}
									});
						}
					},
					// -----------------------Delete
					// options----------------------//
					{
						closeOnEscape : true,
						reloadAfterSubmit : true,
						modal : true,
						jqModal : true,
						width : 270,
						top : 296,
						left : 450,
						zIndex : 1234,
						caption : "Delete Product",
						msg : 'Delete the Product?',
						onclickSubmit : function(params) {
							aGlobalVariable = "del";
							var id = jQuery("#addquotesList").jqGrid(
									'getGridParam', 'selrow');
							var key = jQuery("#addquotesList").getCell(id, 7);
							var costValue = jQuery("#addquotesList").getCell(
									id, 5);
							var priceValue = jQuery("#addquotesList").getCell(
									id, 6);
							var gridData = jQuery("#addquotesList")
									.getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for ( var index = 0; index < gridData.length; index++) {
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(
										/[^0-9\.]+/g, "");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(
										/[^0-9\.]+/g, "");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = costValue.replace(/[^0-9\.]+/g,
									"");
							var aCurrentPrice = priceValue.replace(
									/[^0-9\.]+/g, "");
							totalcost = totalcost - Number(aCurrentCost);
							totalPrice = totalPrice - Number(aCurrentPrice);
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$
									.ajax({
										url : "./jobtabs2/SaveQuoteCustomerDetailInfo",
										type : "GET",
										data : {
											'totalCost' : totalcost,
											'totalPrice' : totalPrice,
											'joQuoteHeaderID' : joQuoteHeader
										},
										success : function(data) {
											var newDialogDiv = jQuery(document
													.createElement('div'));
											jQuery(newDialogDiv)
													.html(
															'<span><b style="color:Green;">Quote details updated successfully.</b></span>');
											jQuery(newDialogDiv)
													.dialog(
															{
																modal : true,
																width : 300,
																height : 150,
																title : "Success",
																buttons : [ {
																	height : 35,
																	text : "OK",
																	click : function() {
																		$(this)
																				.dialog(
																						"close");
																	}
																} ]
															}).dialog("open");
											jQuery(this).dialog("close");
											return true;
										}
									});
							return {
								'joQuoteDetailID' : key
							};
						}
					}).delay( 800 );
	var oldAddRowData = $.fn.jqGrid.addRowData;
	$.jgrid.extend({
		addRowData : function(rowid, rdata, pos, src) {
			if (pos === 'afterSelected' || pos === 'beforeSelected') {
				if (typeof src === 'undefined' && this[0].p.selrow !== null) {
					src = this[0].p.selrow;
					pos = (pos === "afterSelected") ? 'after' : 'before';
				} else {
					pos = (pos === "afterSelected") ? 'last' : 'first';
				}
			}
			return oldAddRowData.call(this, rowid, rdata, pos, src);
		}
	});
	jQuery("#addquotesList").jqGrid('inlineNav', "#addquotespager", {
		addParams : {
			position : "afterSelected"
		}
	});
	jQuery("#addquotesList").jqGrid('bindKeys', {
		"onEnter" : function(rowid) {
			saveProductNote();
		}
	});
}
$(function() {
	var cache = {};
	var lastXhr = '';
	$("#quoteTypeDetail").autocomplete(
			{
				minLength : 1,
				timeout : 1000,
				select : function(event, ui) {
					var id = ui.item.id;
					$("#quoteTypeDetailID").val(id);
				},
				source : function(request, response) {
					var term = request.term;
					if (term in cache) {
						response(cache[term]);
						return;
					}
					lastXhr = $.getJSON("employeeCrud/customerType", request,
							function(data, status, xhr) {
								cache[term] = data;
								if (xhr === lastXhr) {
									response(data);
								}
							});
				},
				error : function(result) {
					$('.ui-autocomplete-loading').removeClass(
							"ui-autocomplete-loading");
				}
			});
});

function inlineFormatter(cellValue, options, rowObject) {
	var element = "<div style='width: 52px;'><a onclick='openLineItemDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/lineItem.png' title='Inline Item' align='middle' style='vertical-align: middle;'></a>"
			+ "<a onclick='openFooterLineDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/footNote.png' title='Foot Note' align='middle' style='vertical-align: middle;'></a></div>";
	return element;
}

function inlineImage(cellValue, options, rowObject) {
	var element = '';
	if (cellValue !== '' && cellValue !== null) {
		element = "<img src='./../resources/images/lineItem_new.png' title='Inline Item'  style='padding: 2px;'>";
	} else if (cellValue === '') {
		element = "";
	} else if (cellValue === null) {
		element = "";
	} else {
		element = "";
	}
	return element;
}

function onDoubleClickEditLineItems(rowId) {
	aAddandEdit = "edit";
	$("#inlneNoteHideShow").css("display", "none");
	/*
	 * var rowIds = grid.jqGrid('getGridParam', 'selrow'); if(rowIds === null){
	 * var errorText = "Please select a line item to edit"; var newDialogDiv =
	 * jQuery(document.createElement('div'));
	 * jQuery(newDialogDiv).attr("id","msgDlg"); jQuery(newDialogDiv).html('<span><b
	 * style="color:red;">'+errorText+'</b></span>');
	 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
	 * title:"Warning", buttons: [{height:35,text: "OK",click: function() {
	 * $(this).dialog("close"); }}]}).dialog("open"); return false; } // var
	 * rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	 */var productName = $("#addquotesList").jqGrid('getCell', rowId, 'product');
	var paragraph = $("#addquotesList").jqGrid('getCell', rowId, 'paragraph');
	var manufactuer = $("#addquotesList").jqGrid('getCell', rowId,
			'manufacturer');
	var quantity = $("#addquotesList").jqGrid('getCell', rowId, 'itemQuantity');
	var cost = $("#addquotesList").jqGrid('getCell', rowId, 'cost');
	var price = $("#addquotesList").jqGrid('getCell', rowId, 'price');
	var quoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteDetailID');
	var rxManufactuer = $("#addquotesList").jqGrid('getCell', rowId,
			'rxManufacturerID');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	var factoryID = $("#addquotesList").jqGrid('getCell', rowId, 'veFactoryId');
	var inlinenote = $("#addquotesList").jqGrid('getCell', rowId, 'inlineNote');
	var productNote = $("#addquotesList").jqGrid('getCell', rowId,
			'productNote');
	var costCovert = cost.replace(/[^0-9\.]+/g, "");
	var priceCovert = price.replace(/[^0-9\.]+/g, "");
	var cost2 = costCovert.replace(".00", "");
	var price2 = priceCovert.replace(".00", "");
	var percentage = $("#addquotesList").jqGrid('getCell', rowId, 'percentage');
	if (cost2 === 0) {
		cost2 = '';
	}
	$("#cost").val(cost2);
	if (price2 === 0) {
		price2 = '';
	}
	$("#price").val(price2);
	$("#product").val(productName);
	$("#paragraph").val(paragraph);
	$("#manufacturer").val(manufactuer);
	$("#itemQuantity").val(quantity);
	$("#joQuoteDetailID").val(quoteDetailID);
	$("#rxManufacturerID").val(rxManufactuer);
	$("#joQuoteHeaderID").val(joQuoteHeaderID);
	$("#veFactoryId").val(factoryID);
	$("#percentage").val(percentage);
	$("#productNote").val(productNote);
	$("#inlineNote").val(inlinenote);
	$("#addButtonDiv").dialog({
		height : 400
	});
	jQuery("#addButtonDiv").dialog("open");
	return true;
}

function imgFmatter(cellValue, options, rowObject) {
	var element = "<div><a onclick='editQuoteFrom()'><img src='./../resources/images/edit.png' title='Edit Line Items' align='middle' style='padding: 2px 5px;'></a>"
			+ "<a onclick='deleteQuoteFrom()'><img src='./../resources/images/delete.png' title='Delete Line Items' align='middle' style='padding: 2px 5px;'></a>"
			+ "<a onclick='addOpenLineItemDialog()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px 5px;'></a></div>";
	return element;
}

function addOpenLineItemDialog() {
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please select a line item to add Line Note";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	/*
	 * var lineItemText = $("#addquotesList").jqGrid('getCell', rowId,
	 * 'inlineNote'); $("#lineItemId").val(lineItemText);
	 * jQuery("#addInLineItem").dialog("open"); return true;
	 */
	var lineItemText = $("#addquotesList").jqGrid('getCell', rowId,
			'inlineNote');
	/*
	 * var Aop =lineItemText.replace(/`AOP`/g, "!"); var Bop
	 * =Aop.replace(/`BOP`/g, "@"); var Cop =Bop.replace(/`COP`/g, "#"); // var
	 * Dop =Cop.replace("$" , "`DOP`"); var Eop =Cop.replace(/`EOP`/g, "%"); var
	 * Fop =Eop.replace(/`FOP`/g, "^" ); var Gop =Fop.replace(/`GOP`/g,"&"); var
	 * Hop =Gop.replace(/`HOP`/g, "*"); var Iop =Hop.replace("`IOP`", "("); var
	 * Anop = Iop.replace("andnbsp;", " ");
	 */
	var JopInlinetext = lineItemText.replace("&And", "'");
	// var joQuoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
	// 'joQuoteDetailID');
	// $.ajax({
	// url: "./jobtabs2/getQuoteDetils",
	// mType: "GET",
	// data : {'joDetailsID' : joQuoteDetailID},
	// success: function(data){
	// var inlineNoteText = data.inlineNote;
	// $("#lineItemLableId").val(inlineNoteText);
	// }
	// });
	// $("#lineItemLableId").val(JopInlinetext);
	// var editorValue = $("#lineItemLableId").val();
	area = new nicEditor({
		buttonList : [ 'bold', 'italic', 'underline', 'left', 'center',
				'right', 'justify', 'ol', 'ul', 'fontSize', 'fontFamily',
				'fontFormat', 'forecolor' ],
		maxHeight : 240
	}).panelInstance('lineItemId');
	$(".nicEdit-main").empty();
	$(".nicEdit-main").append(JopInlinetext);
	jQuery("#addInLineItem").dialog("open");
	$(".nicEdit-main").focus();
	return true;
}

function openFooterLineDialog() {
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var footerLineText = $("#addquotesList").jqGrid('getCell', rowId,
			'productNote');
	$("#footerLine").val("");
	$("#footerLine").val(footerLineText);
	if (footerLineText === false) {
		footerLineText = "";
	}
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html(
			'<table><tr><td><textarea id="footerLine" rows="5" cols="58">'
					+ footerLineText + '</textarea></td></tr></table>');
	jQuery(newDialogDiv).dialog({
		modal : true,
		height : 210,
		width : 535,
		title : "Footnote",
		buttons : {
			"Submit" : function() {
				var footerNote = $("#footerLine").val();
				footerNoteInfo(footerNote);
				jQuery(this).dialog("close");
				$("#addquotesList").trigger("reloadGrid");
			},
			"Cancel" : function() {
				jQuery(this).dialog("close");
			}
		}
	}).dialog("open");
}

function addQuoteFrom() {
	// $("#displayQuantity").css("display", "none");
	aAddandEdit = "add";
	$("#inlneNoteHideShow").show();
	var newDialogDiv = jQuery(document.createElement('div'));
	if ($("#quoteTypeDetail").val() === "-1") {
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">Please Provide "Type"</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Please Provide "Submitted By"</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	/*
	 * var displayQuantity = $("#quantityValueDisplayID").val(); var
	 * displayParagraph = $("#quantityValueDisplayParaID").val(); var
	 * displayManufacturer= $("#quantityValueDisplayManufID").val(); var
	 * displaySpec= $("#quantityValueDisplaySpecID").val(); var displayCost =
	 * $("#quantityValueprintCostID").val(); var displayMult
	 * =$("#quantityValueDispalyMultiID").val(); var displayPrice =
	 * $("#quantityValueDisplayPriceID").val(); if(displayQuantity === '1'){
	 * $("#displayQuantity").show(); } if(displayParagraph === '1'){
	 *  } if(displayManufacturer === '1'){
	 *  } if(displaySpec === '1'){
	 *  } if(displayCost === '1'){
	 *  } if(displayMult === '1'){
	 *  } if(displayPrice === '1'){
	 *  }
	 */
	$("#product").val("");
	$("#paragraph").val("");
	$("#manufacturer").val("");
	$("#itemQuantity").val("");
	$("#cost").val("");
	$("#price").val("");
	$("#joQuoteDetailID").val("");
	$("#joQuoteDetailID").val("");
	$("#rxManufacturerID").val("");
	$("#joQuoteHeaderID").val("");
	$("#veFactoryId").val("");
	$("#percentage").val("");
	$("#inlineNote").val("");
	$("#productNote").val("");
	$("#addButtonDiv").dialog({
		height : 450
	});
	jQuery("#addButtonDiv").dialog("open");
	return true;
}

function editQuoteFrom() {
	aAddandEdit = "edit";
	$("#inlneNoteHideShow").css("display", "none");
	var grid = $("#addquotesList");
	var rowIds = grid.jqGrid('getGridParam', 'selrow');
	if (rowIds === null) {
		var errorText = "Please select a line item to edit";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}

	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var productName = $("#addquotesList").jqGrid('getCell', rowId, 'product');
	var paragraph = $("#addquotesList").jqGrid('getCell', rowId, 'paragraph');
	var manufactuer = $("#addquotesList").jqGrid('getCell', rowId,
			'manufacturer');
	var quantity = $("#addquotesList").jqGrid('getCell', rowId, 'itemQuantity');
	var cost = $("#addquotesList").jqGrid('getCell', rowId, 'cost');
	var price = $("#addquotesList").jqGrid('getCell', rowId, 'price');
	var quoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteDetailID');
	var rxManufactuer = $("#addquotesList").jqGrid('getCell', rowId,
			'rxManufacturerID');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	var factoryID = $("#addquotesList").jqGrid('getCell', rowId, 'veFactoryId');
	var inlinenote = $("#addquotesList").jqGrid('getCell', rowId, 'inlineNote');
	var productNote = $("#addquotesList").jqGrid('getCell', rowId,
			'productNote');
	var costCovert = cost.replace(/[^0-9\.]+/g, "");
	var priceCovert = price.replace(/[^0-9\.]+/g, "");
	var cost2 = costCovert.replace(".00", "");
	var price2 = priceCovert.replace(".00", "");
	var percentage = $("#addquotesList").jqGrid('getCell', rowId, 'percentage');
	if (cost2 === 0) {
		cost2 = '';
	}
	$("#cost").val(cost2);
	if (price2 === 0) {
		price2 = '';
	}
	$("#price").val(price2);
	$("#product").val(productName);
	$("#paragraph").val(paragraph);
	$("#manufacturer").val(manufactuer);
	$("#itemQuantity").val(quantity);
	$("#joQuoteDetailID").val(quoteDetailID);
	$("#rxManufacturerID").val(rxManufactuer);
	$("#joQuoteHeaderID").val(joQuoteHeaderID);
	$("#veFactoryId").val(factoryID);
	$("#percentage").val(percentage);
	$("#productNote").val(productNote);
	$("#inlineNote").val(inlinenote);
	$("#addButtonDiv").dialog({
		height : 400
	});
	jQuery("#addButtonDiv").dialog("open");
	return true;
}

function LineItemInfo() {
	var inlineText = $('#addInLineItemID').find('.nicEdit-main').html(); /* nicEditors.findEditor('lineItemId').getContent(); */
	/*
	 * var Aop =inlineText.replace("!" , "`AOP`"); var Bop =Aop.replace(/@/g ,
	 * "`BOP`"); var Cop =Bop.replace(/#/g , "`COP`"); // var Dop
	 * =Cop.replace("$" , "`DOP`"); var Eop =Cop.replace(/%/g , "`EOP`"); var
	 * Fop =Eop.replace(/"^"/g , "`FOP`"); var Gop =Fop.replace(/&/g,"`GOP`");
	 * var Hop =Gop.replace(/"*"/g , "`HOP`"); var Iop =Hop.replace("(" ,
	 * "`IOP`"); var JopInlinetext =Iop.replace(")" , "`JOP`"); var
	 * inlineTextReplace = JopInlinetext.replace(/&/g,"and");
	 */
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var quoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteDetailID');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	var aLineItem = new Array();
	aLineItem.push(inlineText);
	aLineItem.push(joQuoteHeaderID);
	aLineItem.push(quoteDetailID);
	/*
	 * var inlineTotalValue =
	 * inlineValue+"&quoteDetailID="+quoteDetailID+"&quoteHeaderID="+joQuoteHeaderID+"&inlineTextEditor="+inlineText;
	 */
	/*
	 * var inlineNoteWithOutSpace = $("#lineItemId").val().replace(
	 * /^\s+|\s+$/g, ''); if(inlineNoteWithOutSpace.length === 0){ var
	 * newDialogDiv = jQuery(document.createElement('div'));
	 * jQuery(newDialogDiv).html('<span><b style="color: red;">White Spaces
	 * are not Allowed</b></span>'); jQuery(newDialogDiv).dialog({modal: true,
	 * width:300, height:150, title:"Warning", buttons: [{height:35,text:
	 * "OK",click: function() { $(this).dialog("close");
	 * $("#lineItemId").val().replace(/\s/g,'');
	 * document.getElementById('lineItemId').focus(); }}]}).dialog("open");
	 * return false; } var inlineValue = $("#addInLineItemID").serialize(); var
	 * rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow'); var
	 * quoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
	 * 'joQuoteDetailID'); var joQuoteHeaderID =
	 * $("#addquotesList").jqGrid('getCell', rowId, 'joQuoteHeaderID'); var
	 * inlineTotalValue =
	 * inlineValue+"&quoteDetailID="+quoteDetailID+"&quoteHeaderID="+joQuoteHeaderID;
	 */
	$.ajax({
		url : "./jobtabs2/SavelinetextInfo",
		type : "POST",
		data : {
			'lineItem' : aLineItem
		},
		success : function(data) {
			area.removeInstance('lineItemId');
			jQuery("#addInLineItem").dialog("close");
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails();
			$("#addquotesList").trigger("reloadGrid");
			$.each(data, function(index, value) {
			});
		}
	});
}

function footerNoteInfo(footerNote) {
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var quoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteDetailID');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	$.ajax({
		url : "./jobtabs2/SavelinetextInfo",
		type : "POST",
		data : {
			'footerLine' : footerNote,
			'quoteDetailID' : quoteDetailID,
			'quoteHeaderID' : joQuoteHeaderID
		},
		success : function(data) {
			$("#footerLineText").val(footerNote);
			$.each(data, function(index, value) {
			});
		}
	});
}

function addManufacture() {
	document.location.href = "./vendors";
	jQuery("#addNewVendor").dialog("open");
}

jQuery(function() {
	jQuery("#addInLineItem").dialog({
		autoOpen : false,
		modal : true,
		title : "InLine Note",
		height : 350,
		width : 600,
		buttons : {},
		close : function() {
			return true;
		}
	});
});

function cancelInLineNote() {
	area.removeInstance('lineItemId');
	jQuery("#addInLineItem").dialog("close");
	return false;
}


function addNewLineItemDialog() {
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	/*
	 * var last = jQuery('#addquotesList').jqGrid('getGridParam','records'); var
	 * datarow = {inLineNoteImage: '', product: '', itemQuantity: '', paragraph:
	 * '', manufacturer: '', cost: '', price: '', joQuoteDetailID: '',
	 * rxManufacturerID: '', joQuoteHeaderID: '', veFactoryId: '', inlineNote:
	 * '', productNote: ''}; var lastsel2 = Number(last) + 1; var
	 * su=jQuery("#addquotesList").addRowData(lastsel2, datarow, "last"); if
	 * (su) { jQuery('#addquotesList').addRow(datarow, lastsel2, true); }
	 * jQuery('#addquotesList').addRow(datarow, lastsel2, 'afterSelected');
	 * for(var i = 0; i < last.length; i++){
	 * $("#addquotesList").jqGrid('addRowData', i + 1, last[i]); }
	 */
	$('#addquotesList_iladd').trigger('click');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	$("#new_row_joQuoteHeaderID").val(joQuoteHeaderID);
	return true;
}

function nospacesEditUser(t) {
	if (t.value.match(/\s/g)) {
		t.value = t.value.replace(/\s/g, '');
	}
}

function saveProductNote() {
	var product = $("#new_row_product").val();
	var quantity = $("#new_row_itemQuantity").val();
	var paragraph = $("#new_row_paragraph").val();
	var manufacture = $("#new_row_manufacturer").val();
	var cost = $("#new_row_cost").val().replace(/[^0-9\.]+/g, "").replace(
			".00", "");
	var price = $("#new_row_price").val().replace(/[^0-9\.]+/g, "").replace(
			".00", "");
	var manufactureID = $("#new_row_rxManufacturerID").val();
	var joQuoteHeaderID = $("#new_row_joQuoteHeaderID").val();
	var factoryID = $("#new_row_veFactoryId").val();
	var joQuoteHeaderDetailID = '';
	var inlineNote = '';
	var footerNote = '';
	var addQuoteValues = "product=" + product + "&paragraph=" + paragraph
			+ "&rxManufacturerID=" + manufactureID + "&manufacturer="
			+ manufacture + "" + "&itemQuantity=" + quantity + "&cost=" + cost
			+ "&oper=add&joQuoteHeaderID=" + joQuoteHeaderID
			+ "&quoteheaderid=" + joQuoteHeaderDetailID + "" + "&veFactoryId="
			+ factoryID + "&inlineNote=" + inlineNote + "&footnote="
			+ footerNote + "&productNote=" + footerNote + "&price=" + price;
	$.ajax({
				url : "./jobtabs2/manpulaterProductQuotes",
				type : "POST",
				data : addQuoteValues,
				success : function(data) {
					jQuery("#addButtonDiv").dialog("close");
					$("#addquotesList").jqGrid('GridUnload');
					loadQuotesListDetails();
					$("#addquotesList").trigger("reloadGrid");
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:Green;">Quote details updated.</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Success",
						buttons : [ {
							height : 35,
							text : "OK",
							click : function() {
								$(this).dialog("close");
								jQuery("#addquoteTemplate").dialog("close");
							}
						} ]
					}).dialog("open");
				}
			});
	return false;
}

function upAndDownImage(cellValue, options, rowObject) {
	var element = "<div>";
	var upIconID = options.rowId;
	var downID = options.rowId;
	var downIconID = Number(downID) + 1;
	if (options.rowId === '1') {
		element += "<a id='"
				+ downIconID
				+ "_downIcon' onclick='downLineItem()' style='padding: 40px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += "</div>";
	} else {
		element += "<a id='"
				+ upIconID
				+ "_upIcon' onclick='upLineItem()' style='padding: 8px;'><img src='./../resources/images/upArrowLineItem.png' title='Move Up & Save'></a>";
		element += "<a id='"
				+ downIconID
				+ "_downIcon' onclick='downLineItem()' style='padding: 8px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
		element += "</div>";
	}
	return element;
}

/*
 * function upLineItem() { var rowId =
 * $("#addquotesList").jqGrid('getGridParam', 'selrow'); if(rowId === null){ var
 * errorText = "Please select a line item to move to up or down."; var
 * newDialogDiv = jQuery(document.createElement('div'));
 * jQuery(newDialogDiv).attr("id","msgDlg"); jQuery(newDialogDiv).html('<span><b
 * style="color:red;">'+errorText+'</b></span>');
 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
 * title:"Warning", buttons: [{height:35,text: "OK",click: function() {
 * $(this).dialog("close"); }}]}).dialog("open"); return false; } var
 * aLineItemPosition = Number(rowId) - 1; var aPosition =
 * $("#addquotesList").jqGrid('getCell', aLineItemPosition, 'position'); var
 * aQuoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
 * 'joQuoteDetailID'); var aJoQuoteHeaderID =
 * $("#addquotesList").jqGrid('getCell', rowId, 'joQuoteHeaderID'); var
 * upLineItem = 'upLineItem'; updateLineItemPosition(aLineItemPosition,
 * aPosition, aQuoteDetailID, aJoQuoteHeaderID, upLineItem); return false; }
 */

function upLineItem() {
	var aSelectedRowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	if (aSelectedRowId === null) {
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'position');
	var aSelectedQuoteDetailID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'joQuoteDetailID');
	var aSelectedJoQuoteHeaderID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'joQuoteHeaderID');
	var aAbovePositionRowID = Number(aSelectedRowId) - 1;
	var aAbovePositionDetailID = $("#addquotesList").jqGrid('getCell',
			aAbovePositionRowID, 'position');
	var aAboveQuoteDetailID = $("#addquotesList").jqGrid('getCell',
			aAbovePositionRowID, 'joQuoteDetailID');
	var aUpLineItem = 'upLineItem';
	updateLineItemPosition(aSelectedRowId, aSelectedPositionDetailID,
			aSelectedQuoteDetailID, aSelectedJoQuoteHeaderID,
			aAbovePositionRowID, aAbovePositionDetailID, aAboveQuoteDetailID,
			aUpLineItem);
	return false;
}

function downLineItem() {
	var aSelectedRowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	if (aSelectedRowId === null) {
		var errorText = "Please select a line item to move to up or down.";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aSelectedPositionDetailID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'position');
	var aSelectedQuoteDetailID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'joQuoteDetailID');
	var aSelectedJoQuoteHeaderID = $("#addquotesList").jqGrid('getCell',
			aSelectedRowId, 'joQuoteHeaderID');
	var aAbovePositionRowID = Number(aSelectedRowId) + 1;
	var aAbovePositionDetailID = $("#addquotesList").jqGrid('getCell',
			aAbovePositionRowID, 'position');
	var aAboveQuoteDetailID = $("#addquotesList").jqGrid('getCell',
			aAbovePositionRowID, 'joQuoteDetailID');
	var aUpLineItem = 'downLineItem';
	updateLineItemPosition(aSelectedRowId, aSelectedPositionDetailID,
			aSelectedQuoteDetailID, aSelectedJoQuoteHeaderID,
			aAbovePositionRowID, aAbovePositionDetailID, aAboveQuoteDetailID,
			aUpLineItem);
	return false;
}

/*
 * function downLineItem() { return false; var rowId =
 * $("#addquotesList").jqGrid('getGridParam', 'selrow'); if(rowId === null){ var
 * errorText = "Please select a line item to move to up or down."; var
 * newDialogDiv = jQuery(document.createElement('div'));
 * jQuery(newDialogDiv).attr("id","msgDlg"); jQuery(newDialogDiv).html('<span><b
 * style="color:red;">'+errorText+'</b></span>');
 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
 * title:"Warning", buttons: [{height:35,text: "OK",click: function() {
 * $(this).dialog("close"); }}]}).dialog("open"); return false; } var
 * aLineItemPosition = Number(rowId) + 1; var aPosition =
 * $("#addquotesList").jqGrid('getCell', aLineItemPosition, 'position'); var
 * aQuoteDetailID = $("#addquotesList").jqGrid('getCell', rowId,
 * 'joQuoteDetailID'); var aJoQuoteHeaderID =
 * $("#addquotesList").jqGrid('getCell', rowId, 'joQuoteHeaderID'); var
 * upLineItem = 'downLineItem'; updateLineItemPosition(aLineItemPosition,
 * aPosition, aQuoteDetailID, aJoQuoteHeaderID, upLineItem); return false; }
 */

/*
 * function updateLineItemPosition(aLineItemPosition, aPosition, aQuoteDetailID,
 * aJoQuoteHeaderID, upLineItem){ $.ajax({ url:
 * "./jobtabs2/updateLineItemPosition", type: "POST", data : {'lineItemPositon' :
 * aPosition, 'joQuoteDetaildID' : aQuoteDetailID, 'joQuoteHeaderID' :
 * aJoQuoteHeaderID, 'oper' : upLineItem}, success: function(data) {
 * $("#addquotesList").jqGrid('GridUnload'); loadQuotesListDetails();
 * $("#addquotesList").trigger("reloadGrid"); var newDialogDiv =
 * jQuery(document.createElement('div')); jQuery(newDialogDiv).html('<span><b
 * style="color:Green;">Quote details updated.</b></span>');
 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
 * title:"Success", buttons: [{height:35,text: "OK",click: function() {
 * $(this).dialog("close"); }}]}).dialog("open"); } }); return false; }
 */

function updateLineItemPosition(aSelectedRowId, aSelectedPositionDetailID,
		aSelectedQuoteDetailID, aSelectedJoQuoteHeaderID, aAbovePositionRowID,
		aAbovePositionDetailID, aAboveQuoteDetailID, aUpLineItem) {
	$.ajax({
		url : "./jobtabs2/updateLineItemPosition",
		type : "POST",
		data : {
			'selectedRowID' : aSelectedRowId,
			'selectedPositionDetailID' : aSelectedPositionDetailID,
			'selectedQuoteDetailID' : aSelectedQuoteDetailID,
			'selectedJoQuoteHeaderID' : aSelectedJoQuoteHeaderID,
			'abovePositionRowID' : aAbovePositionRowID,
			'abovePositionDetailID' : aAbovePositionDetailID,
			'aboveQuoteDetailID' : aAboveQuoteDetailID,
			'oper' : aUpLineItem
		},
		success : function(data) {
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails();
			$("#addquotesList").trigger("reloadGrid");
			/*
			 * var newDialogDiv = jQuery(document.createElement('div'));
			 * jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote
			 * details updated.</b></span>');
			 * jQuery(newDialogDiv).dialog({modal: true, width:300, height:150,
			 * title:"Success", buttons: [{height:35,text: "OK",click:
			 * function() { $(this).dialog("close"); }}]}).dialog("open");
			 */
		}
	});
	return false;
}

function removeNull(strValue) {
	if (strValue === null) {
		return '';
	} else {
		return strValue;
	}
}
jQuery(function() {
	var dialogWidth = 1160;
	jQuery("#printLineItemDialog")
			.dialog(
					{
						autoOpen : false,
						modal : true,
						height : 840,
						width : dialogWidth,
						top : 1000,
						position : [
								($(window).width() / 2) - (dialogWidth / 2),
								190 ],
						buttons : {},
						create : function(event, ui) {
							$(this).parents(".ui-dialog:first").find(
									".ui-dialog-titlebar").css("display",
									"none");
							$(this).parents(".ui-dialog").css(
									"background-color", '#FAFAFA');
							$(this).parents(".ui-widget-content").css(
									"padding", "0");
							$(this).parents(".ui-dialog").css("border",
									"0 none");
						},
						close : function() {
							return true;
						}
					});
});

function addLineItemDialog() {
	var printQuantity = $("#quantityValuePrintID").val();
	var printParagraph = $("#quantityValuePrintParaID").val();
	var printManufacturer = $("#quantityValuePrintManufID").val();
	var printSpec = $("#quantityValuePrintSpecID").val();
	// var printMult = $("#quantityValueprintMultiID").val();
	var printPrice = $("#quantityValuePrintPriceID").val();
	$("#custombuttons").show();
	var grid = $("#addquotesList");
	var rowIds = grid.jqGrid('getGridParam', 'selrow');
	if (rowIds === null) {
		var errorText = "Please select a line item to edit";
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	$("#projectName").empty();
	var jobName = " " + $("#jobName_ID").text();
	$("#projectName").text(jobName);
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var joQuoteHeaderID = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	$.ajax({
		url : "./jobtabs2/getLineItemValues",
		type : "GET",
		data : {
			'joQuoteHeaderID' : joQuoteHeaderID
		},
		success : function(data) {
			var printTable = '<tr>' + '<th align="center">No.</th>'
					+ '<th align="center">Description</th>';
			if (printQuantity === '1') {
				printTable += '<th align="center">Qty.</th>';
			}
			if (printParagraph === '1') {
				printTable += '<th align="center">Paragraph</th>';
			}
			if (printManufacturer === '1') {
				printTable += '<th align="center">Vendors</th>';
			}
			if (printSpec === '1') {
				printTable += '<th align="center">Spec</th>';
			}
			printTable += '<th align="center">Cost</th>';
			printTable += '<th align="center">Mult</th>';
			if (printPrice === '1') {
				printTable += '<th align="center">Sell Price</th>';
			}
			printTable += '</tr>';
			$.each(data, function(index, value) {
				printTable += '<tr>' + '<td align="center"><label>'
						+ (index + 1) + '.</label></td>' + '<td><label>'
						+ removeNull(value.product) + '</label></td>';
				if (printQuantity === '1') {
					printTable += '<td align="center"><label>'
							+ removeNull(value.itemQuantity) + '</label></td>';
				}
				if (printParagraph === '1') {
					printTable += '<td><label>' + removeNull(value.paragraph)
							+ '</label></td>';
				}
				if (printManufacturer === '1') {
					printTable += '<td><label>'
							+ removeNull(value.manufacturer) + '</label></td>';
				}
				if (printSpec === '1') {
					printTable += '<td><label>' + removeNull(value.spec)
							+ '</label></td>';
				}
				printTable += '<td align="right"><label>'
						+ formatCurrency(value.cost) + '</label></td>';
				printTable += '<td><label>' + removeNull(value.mult)
						+ '</label></td>';
				if (printPrice === '1') {
					printTable += '<td align="right"><label>'
							+ formatCurrency(value.price) + '</label></td>';
				}
				printTable += '</tr>';
			});
			$("#printValues").empty();
			$("#printValues").append(printTable);
		}
	});
	jQuery("#printLineItemDialog").dialog("open");
	/* setTimeout("$('#printLineItemDialog').jqprint()", 300); */
	return true;
}

function printLineItem() {
	$("#custombuttons").css("display", "none");
	$('#printLineItemDialog').jqprint();
	return jQuery("#printLineItemDialog").dialog("close");
}

function cancelPrintableView() {
	jQuery("#printLineItemDialog").dialog("close");
	return false;
}

jQuery(function() {
	jQuery("#quotePropertiesDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Quote Properties",
		height : 450,
		width : 345,
		buttons : {},
		close : function() {
			return true;
		}
	});
});
jQuery(function() {
	jQuery("#quotePropertiesTempDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Quote Properties",
		height : 450,
		width : 345,
		buttons : {},
		close : function() {
			return true;
		}
	});
});

function changeQuantity() {
	if ($('#quantityDisplayID').is(':checked')) {
		$("#quantityLabelID").empty();
		$("#quantityLabelID").append("(Yes)");
	} else {
		$("#quantityLabelID").empty();
		$("#quantityLabelID").append("(No)");
	}
	if ($('#quantityPrintID').is(':checked')) {
		$("#quantityLabelPrintID").empty();
		$("#quantityLabelPrintID").append("(Yes)");
	} else {
		$("#quantityLabelPrintID").empty();
		$("#quantityLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlineID').is(':checked')) {
		$("#quantityLabelUnderlineID").empty();
		$("#quantityLabelUnderlineID").append("(Yes)");
	} else {
		$("#quantityLabelUnderlineID").empty();
		$("#quantityLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldID').is(':checked')) {
		$("#quantityLabelBoldID").empty();
		$("#quantityLabelBoldID").append("(Yes)");
	} else {
		$("#quantityLabelBoldID").empty();
		$("#quantityLabelBoldID").append("(No)");
	}
	if ($('#quantityDisplayParaID').is(':checked')) {
		$("#paraLabelID").empty();
		$("#paraLabelID").append("(Yes)");
	} else {
		$("#paraLabelID").empty();
		$("#paraLabelID").append("(No)");
	}
	if ($('#quantityprintParaID').is(':checked')) {
		$("#paraLabelPrintID").empty();
		$("#paraLabelPrintID").append("(Yes)");
	} else {
		$("#paraLabelPrintID").empty();
		$("#paraLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlineParaID').is(':checked')) {
		$("#paraLabelUnderlineID").empty();
		$("#paraLabelUnderlineID").append("(Yes)");
	} else {
		$("#paraLabelUnderlineID").empty();
		$("#paraLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldParaID').is(':checked')) {
		$("#paraLabelBoldID").empty();
		$("#paraLabelBoldID").append("(Yes)");
	} else {
		$("#paraLabelBoldID").empty();
		$("#paraLabelBoldID").append("(No)");
	}
	if ($('#quantityDisplayManufID').is(':checked')) {
		$("#manufatuereLabelID").empty();
		$("#manufatuereLabelID").append("(Yes)");
	} else {
		$("#manufatuereLabelID").empty();
		$("#manufatuereLabelID").append("(No)");
	}
	if ($('#quantityPrintManufID').is(':checked')) {
		$("#manufatuereLabelPrintID").empty();
		$("#manufatuereLabelPrintID").append("(Yes)");
	} else {
		$("#manufatuereLabelPrintID").empty();
		$("#manufatuereLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlineManufID').is(':checked')) {
		$("#manufatuereLabelUnderlineID").empty();
		$("#manufatuereLabelUnderlineID").append("(Yes)");
	} else {
		$("#manufatuereLabelUnderlineID").empty();
		$("#manufatuereLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldManufID').is(':checked')) {
		$("#manufatuereLabelBoldID").empty();
		$("#manufatuereLabelBoldID").append("(Yes)");
	} else {
		$("#manufatuereLabelBoldID").empty();
		$("#manufatuereLabelBoldID").append("(No)");
	}
	if ($('#quantityDisplaySpecID').is(':checked')) {
		$("#specLabelID").empty();
		$("#specLabelID").append("(Yes)");
	} else {
		$("#specLabelID").empty();
		$("#specLabelID").append("(No)");
	}
	if ($('#quantityprintSpecID').is(':checked')) {
		$("#specLabelPrintID").empty();
		$("#specLabelPrintID").append("(Yes)");
	} else {
		$("#specLabelPrintID").empty();
		$("#specLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlineSpecID').is(':checked')) {
		$("#specLabelUnderlineID").empty();
		$("#specLabelUnderlineID").append("(Yes)");
	} else {
		$("#specLabelUnderlineID").empty();
		$("#specLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldSpecID').is(':checked')) {
		$("#specLabelBoldID").empty();
		$("#specLabelBoldID").append("(Yes)");
	} else {
		$("#specLabelBoldID").empty();
		$("#specLabelBoldID").append("(No)");
	}
	if ($('#quantityDisplayCostID').is(':checked')) {
		$("#costLabelID").empty();
		$("#costLabelID").append("(Yes)");
	} else {
		$("#costLabelID").empty();
		$("#costLabelID").append("(No)");
	}
	  if ($('#quantityprintCostID').is(':checked')){
		  $("#costLabelPrintID").empty(); $("#costLabelPrintID").append("(Yes)");
	  }else{ $("#costLabelPrintID").empty();
	  	$("#costLabelPrintID").append("(No)"); 
	  	}
	  if ($('#quantityUnderlineCostID').is(':checked')){
		  $("#costLabelUnderlineID").empty(); $("#costLabelUnderlineID").append("(Yes)");
	  }else{ $("#costLabelUnderlineID").empty();
	  	$("#costLabelUnderlineID").append("(No)"); 
	  	}
	  if ($('#quantityBoldCostID').is(':checked')){
		  $("#costLabelBoldID").empty(); $("#costLabelBoldID").append("(Yes)");
	  }else{ $("#costLabelBoldID").empty();
	  	$("#costLabelBoldID").append("(No)"); 
	  	}
	if ($('#quantityDisplayMultiID').is(':checked')) {
		$("#multiLabelID").empty();
		$("#multiLabelID").append("(Yes)");
	} else {
		$("#multiLabelID").empty();
		$("#multiLabelID").append("(No)");
	}
	if ($('#quantityprintMultiID').is(':checked')) {
		$("#multiLabelPrintID").empty();
		$("#multiLabelPrintID").append("(Yes)");
	} else {
		$("#multiLabelPrintID").empty();
		$("#multiLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlineMultiID').is(':checked')) {
		$("#multiLabelUnderlineID").empty();
		$("#multiLabelUnderlineID").append("(Yes)");
	} else {
		$("#multiLabelUnderlineID").empty();
		$("#multiLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldMultiID').is(':checked')) {
		$("#multiLabelBoldID").empty();
		$("#multiLabelBoldID").append("(Yes)");
	} else {
		$("#multiLabelBoldID").empty();
		$("#multiLabelBoldID").append("(No)");
	}
	if ($('#quantityDisplayPriceID').is(':checked')) {
		$("#priceLabelID").empty();
		$("#priceLabelID").append("(Yes)");
	} else {
		$("#priceLabelID").empty();
		$("#priceLabelID").append("(No)");
	}
	if ($('#quantityprintPriceID').is(':checked')) {
		$("#priceLabelPrintID").empty();
		$("#priceLabelPrintID").append("(Yes)");
	} else {
		$("#priceLabelPrintID").empty();
		$("#priceLabelPrintID").append("(No)");
	}
	if ($('#quantityUnderlinePriceID').is(':checked')) {
		$("#priceLabelUnderlineID").empty();
		$("#priceLabelUnderlineID").append("(Yes)");
	} else {
		$("#priceLabelUnderlineID").empty();
		$("#priceLabelUnderlineID").append("(No)");
	}
	if ($('#quantityBoldPriceID').is(':checked')) {
		$("#priceLabelBoldID").empty();
		$("#priceLabelBoldID").append("(Yes)");
	} else {
		$("#priceLabelBoldID").empty();
		$("#priceLabelBoldID").append("(No)");
	}
}

function setQuotePorperties() {
	$("#joQuotePropertyID").val();
	var displayQuantity = $("#quantityValueDisplayID").val();
	var printQuantity = $("#quantityValuePrintID").val();
	var displayParagraph = $("#quantityValueDisplayParaID").val();
	var printParagraph = $("#quantityValuePrintParaID").val();
	var displayManufacturer = $("#quantityValueDisplayManufID").val();
	var printManufacturer = $("#quantityValuePrintManufID").val();
	var displaySpec = $("#quantityValueDisplaySpecID").val();
	var printSpec = $("#quantityValuePrintSpecID").val();
	var displayCost = $("#quantityValueprintCostID").val();
	var printCost = $("#quantityValuePrintCostID").val();
	var displayMult = $("#quantityValueDispalyMultiID").val();
	var printMult = $("#quantityValueprintMultiID").val();
	var displayPrice = $("#quantityValueDisplayPriceID").val();
	var printPrice = $("#quantityValuePrintPriceID").val();
	var notesFullWidth = $("#inlineNotePage").val();
	var lineNumbers = $("#printLineID").val();
	var printTotal = $("#summationID").val();
	var hidePrice = $("#showTotallingID").val();
	if (displayQuantity === '1') {
		$("#quantityDisplayID").attr("checked", true);
		$("#quantityLabelID").empty();
		$("#quantityLabelID").append("(Yes)");
	}
	if (printQuantity === '1') {
		$("#quantityPrintID").attr("checked", true);
		$("#quantityLabelPrintID").empty();
		$("#quantityLabelPrintID").append("(Yes)");
	}
	if (displayParagraph === '1') {
		$("#quantityDisplayParaID").attr("checked", true);
		$("#paraLabelID").empty();
		$("#paraLabelID").append("(Yes)");
	}
	if (printParagraph === '1') {
		$("#quantityprintParaID").attr("checked", true);
		$("#paraLabelPrintID").empty();
		$("#paraLabelPrintID").append("(Yes)");
	}
	if (displayManufacturer === '1') {
		$("#quantityDisplayManufID").attr("checked", true);
		$("#manufatuereLabelID").empty();
		$("#manufatuereLabelID").append("(Yes)");
	}
	if (printManufacturer === '1') {
		$("#quantityPrintManufID").attr("checked", true);
		$("#manufatuereLabelPrintID").empty();
		$("#manufatuereLabelPrintID").append("(Yes)");
	}
	if (displaySpec === '1') {
		$("#quantityDisplaySpecID").attr("checked", true);
		$("#specLabelID").empty();
		$("#specLabelID").append("(Yes)");
	}
	if (printSpec === '1') {
		$("#quantityprintSpecID").attr("checked", true);
		$("#specLabelPrintID").empty();
		$("#specLabelPrintID").append("(Yes)");
	}
	if (displayCost === '1') {
		$("#quantityDisplayCostID").attr("checked", true);
		$("#costLabelID").empty();
		$("#costLabelID").append("(Yes)");
	}
	  if(printCost === '1'){ $("#quantityprintCostID").attr("checked", true);
	  $("#costLabelPrintID").empty(); $("#costLabelPrintID").append("(Yes)"); }
	 
	if (displayMult === '1') {
		$("#quantityDisplayMultiID").attr("checked", true);
		$("#multiLabelID").empty();
		$("#multiLabelID").append("(Yes)");
	}
	if (printMult === '1') {
		$("#quantityprintMultiID").attr("checked", true);
		$("#multiLabelPrintID").empty();
		$("#multiLabelPrintID").append("(Yes)");
	}
	if (displayPrice === '1') {
		$("#quantityDisplayPriceID").attr("checked", true);
		$("#priceLabelID").empty();
		$("#priceLabelID").append("(Yes)");
	}
	if (printPrice === '1') {
		$("#quantityprintPriceID").attr("checked", true);
		$("#priceLabelPrintID").empty();
		$("#priceLabelPrintID").append("(Yes)");
	}
	if (notesFullWidth === '1') {
		$("#inlineNoteFullPageID").attr("checked", true);
	}
	if (lineNumbers === '1') {
		$("#PrintLineNumID").attr("checked", true);
	}
	if (printTotal === '1') {
		$("#printSummationID").attr("checked", true);
	}
	if (hidePrice === '1') {
		$("#hidePriceID").attr("checked", true);
	}
	jQuery("#quotePropertiesDialog").dialog("open");
	return true;
}

function saveQuoteProperties() {
	var aProperties = $("#quotePropertiesFormID").serialize();
	var aQuoteHeaderID = $("#joHeaderID").val();
	var aQuoteProperties = aProperties + "&joQuoteHeaderID=" + aQuoteHeaderID  +"&isQuoteTempProp=false";
	$.ajax({
		url : "./jobtabs2/updateQuoteProperties",
		type : "POST",
		data : aQuoteProperties,
		async: false,
		success : function(data) {
			$("#quantityValueDisplayID").val(data.displayQuantity);
			$("#quantityValuePrintID").val(data.printQuantity);
			$("#quantityValueDisplayParaID").val(data.displayParagraph);
			$("#quantityValuePrintParaID").val(data.printParagraph);
			$("#quantityValueDisplayManufID").val(data.displayManufacturer);
			$("#quantityValuePrintManufID").val(data.printManufacturer);
			$("#quantityValueDisplaySpecID").val(data.displaySpec);
			$("#quantityValuePrintSpecID").val(data.printSpec);
			$("#quantityValueprintCostID").val(data.displayCost);
			$("#quantityValuePrintCostID").val(data.printCost);
			$("#quantityValueDispalyMultiID").val(data.displayMult);	
			$("#quantityValueprintMultiID").val(data.printMult);
			$("#quantityValueDisplayPriceID").val(data.displayPrice);
			$("#quantityValuePrintPriceID").val(data.printPrice);
			$("#inlineNotePage").val(data.notesFullWidth);
			$("#printLineID").val(data.lineNumbers);
			$("#summationID").val(data.printTotal);
			$("#showTotallingID").val(data.hidePrice);
		}
	});
	colnamesaddQuotes = [];
	colModeladdQuotes = [];
	jQuery("#quotePropertiesDialog").dialog("close");
	$("#addquotesList").jqGrid('GridUnload');
	addQuotesColumnDetails();
	/*loadQuotesListDetails();
	$('#addquotesList').jqGrid('setGridParam',{postData:{'joQuoteHeaderID' : joQuoteheaderID}});
	$("#addquotesList").trigger("reloadGrid");*/
	return true;
}

function cancelQuoteProperties() {
	jQuery("#quotePropertiesDialog").dialog("close");
	jQuery("#quotePropertiesTempDialog").dialog("close");
	return false;
}

/** Quote Navigator function * */

var aAddandEdit;
var aDetailId;
var aListDetailId = '';
var cost = $("#cost").val().replace(/[^0-9\.]+/g, "");
var price = $("#price").val().replace(/[^0-9\.]+/g, "");
$("#cost").val(cost);
$("#price").val(price);
$(function() {
	var cache = {};
	var lastXhr = '';
	$("#product").autocomplete(
			{
				minLength : 2,
				select : function(event, ui) {
					var quoteDetailID = ui.item.id;
					var rxMasterID = ui.item.manufactureID;
					$("#quoteDetailID").val(quoteDetailID);
					$.ajax({
						url : "./jobtabs2/quoteDetails",
						mType : "GET",
						data : {
							'quoteDetailID' : quoteDetailID,
							'rxMasterID' : rxMasterID
						},
						success : function(data) {
							$.each(data, function(index, value) {
								manufacture = value.inlineNote;
								var detail = value.paragraph;
								var quantity = value.itemQuantity;
								var manufactureID = value.rxManufacturerID;
								var factoryID = value.detailSequenceId;
								$("#paragraph").val(detail);
								$("#manufacturer").val(quantity); // $("#itemQuantity").val(quantity);
								$("#rxManufacturerID").val(manufactureID);
								$("#veFactoryId").val(factoryID);
							});
						}
					});
				},
				source : function(request, response) {
					var term = request.term;
					if (term in cache) {
						response(cache[term]);
						return;
					}
					lastXhr = $.getJSON("jobtabs2/productList", request,
							function(data, status, xhr) {
								cache[term] = data;
								if (xhr === lastXhr) {
									response(data);
								}
							});
				},
				error : function(result) {
					$('.ui-autocomplete-loading').removeClass(
							"ui-autocomplete-loading");
				}
			});
});

$(function() {
	var cache = {};
	var lastXhr = '';
	$("#manufacturer").autocomplete(
			{
				minLength : 1,
				select : function(event, ui) {
					var id = ui.item.id;
					var name = ui.item.value;
					$("#rxManufacturerID").val(id);
					$.ajax({
						url : "./jobtabs2/getFactoryID",
						type : "GET",
						data : {
							'rxMasterID' : id,
							'descripition' : name
						},
						success : function(data) {
							$("#veFactoryId").val(data);
						}
					});
				},
				source : function(request, response) {
					var term = request.term;
					if (term in cache) {
						response(cache[term]);
						return;
					}
					lastXhr = $.getJSON("jobtabs2/vendorsList", request,
							function(data, status, xhr) {
								cache[term] = data;
								if (xhr === lastXhr) {
									response(data);
								}
							});
				},
				error : function(result) {
					$('.ui-autocomplete-loading').removeClass(
							"ui-autocomplete-loading");
				}
			});
});

function deleteQuoteFrom() {
	var grid = $("#addquotesList");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId !== null) {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color: red;">Delete the Line Item?</b></span>');
		jQuery(newDialogDiv).dialog(
				{
					modal : true,
					width : 300,
					height : 120,
					title : "Confirm Delete",
					buttons : {
						"Submit" : function() {
							var rowId = $("#addquotesList").jqGrid(
									'getGridParam', 'selrow');
							var joQuoteID = $("#addquotesList").jqGrid(
									'getCell', rowId, 'joQuoteDetailID');
							$.ajax({
								url : "./jobtabs2/deleteQuickQuoteDetail",
								mType : "GET",
								data : {
									'joHeaderQuoteDetailID' : joQuoteID
								},
								success : function(data) {
									$("#addquotesList").trigger("reloadGrid");
									$.each(data, function(index, value) {
									});
								}
							});
							// deleteQuote(joQuoteID);
							jQuery(this).dialog("close");
							$("#addquotesList").trigger("reloadGrid");
						},
						"Cancel" : function() {
							jQuery(this).dialog("close");
						}
					}
				}).dialog("open");
	} else {
		var errorText = "Please select a line item to delete";
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	return true;
}
jQuery(function() {
	jQuery("#addButtonDiv").dialog({
		autoOpen : false,
		height : 450,
		width : 600,
		title : "Add/Edit Line Item",
		modal : true,
		buttons : {
		/*
		 * "Import Factory Quote" : function(){ }, "Build from Stock" :
		 * function(){ }, "Custom Build" : function(){ }, "Submit": function() {
		 * if(aAddandEdit === "add"){
		 * if(!$('#addButtonForm').validationEngine('validate')) { return false; }
		 * addQuoteFormDialog(); jQuery(this).dialog("close"); return true; }
		 * else if(aAddandEdit === "edit"){
		 * if(!$('#addButtonForm').validationEngine('validate')) { return false; }
		 * addQuoteFormDialog(); jQuery(this).dialog("close"); return true; } },
		 * Cancel: function () { jQuery(this).dialog("close"); return true; }
		 */
		},
		close : function() {
			$('#addButtonForm').validationEngine('hideAll');
			return true;
		}
	});
});

function pricePercentageCost() {
	if (!$('#addButtonForm').validationEngine('validate')) {
		return false;
	}
	var percentage = $("#percentage").val();
	var costNew = $("#cost").val().replace(/[^0-9\.]+/g, "");
	if (percentage === '0') {
		priceEmpty = costNew;
		$("#price").val(priceEmpty);
		return false;
	} else if (percentage >= 100) {
		$("#maginAlert").fadeOut("slow");
		$("#maginAlert").fadeIn("slow", function() {
			$("#maginAlert").fadeOut(7000);
		});
		return false;
	} else {
		if (percentage !== '') {
			price = costNew / [ (100 - percentage) / 100 ];
			$("#price").val("");
			$("#price").val(Math.round(price));
		}
		return true;
	}
}

function costPercentage() {
	if (!$('#addButtonForm').validationEngine('validate')) {
		return false;
	}
	var percentage1 = $("#percentage").val();
	var cost = $("#cost").val().replace(/[^0-9\.]+/g, "");
	var price = $("#price").val().replace(/[^0-9\.]+/g, "");
	if ((price !== '' && price !== '0') && (cost !== '' && cost !== '0')
			&& (percentage !== '' && percentage !== '0')) {
		percent = [ 1 - (cost / price) ] * 100;
		// var percentageCostPrice = Math.round(percent);
		$("#percentage").val("");
		$("#percentage").val(percentage1);
		return true;
	}
}

function openLineItemDialog() {
	var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var lineItemText = $("#addquotesList").jqGrid('getCell', rowId,
			'inlineNote');
	$("#lineItem").val("");
	$("#lineItem").val(lineItemText);
	if (lineItemText === false) {
		lineItemText = "";
	}
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).html(
			'<table><tr><td><textarea id="lineItem" rows="24" cols="54">'
					+ lineItemText + '</textarea></td></tr></table>');
	jQuery(newDialogDiv).dialog({
		modal : true,
		width : 500,
		height : 500,
		title : "InLine Note",
		buttons : {
			"Submit" : function() {
				var lineitem = $("#lineItem").val();
				LineItemInfo(lineitem);
				jQuery(this).dialog("close");
				$("#addquotesList").trigger("reloadGrid");
			},
			"Cancel" : function() {
				jQuery(this).dialog("close");
			}
		}
	}).dialog("open");
}

function saveQuoteNavigator() {
	var newDialogDiv = jQuery(document.createElement('div'));
	if (aAddandEdit === "add") {
		if (!$('#addButtonForm').validationEngine('validate')) {
			return false;
		}
		if ($("#rxManufacturerID").val() === '') {
			jQuery(newDialogDiv)
					.html(
							'<span><b style="color:red;">This Vendor is not available. Please choose another Vendor (or) Add the Vendor Using Add Manufacture.</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 320,
				height : 160,
				title : "Warning",
				buttons : [ {
					height : 35,
					text : "OK",
					click : function() {
						$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
		}
		addQuoteFormDialog();
		jQuery("#addButtonDiv").dialog("close");
		return true;
	} else if (aAddandEdit === "edit") {
		if (!$('#addButtonForm').validationEngine('validate')) {
			return false;
		}
		if ($("#rxManufacturerID").val() === '') {
			jQuery(newDialogDiv)
					.html(
							'<span><b style="color:red;">This Vendor is not available. Please choose another Vendor (or) Add the Vendor Using Add Manufacture.</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 320,
				height : 160,
				title : "Warning",
				buttons : [ {
					height : 35,
					text : "OK",
					click : function() {
						$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
		}
		addQuoteFormDialog();
		jQuery("#addButtonDiv").dialog("close");
		return true;
	}
}

function cancelQuoteNavigator() {
	$('#addButtonForm').validationEngine('hideAll');
	jQuery("#addButtonDiv").dialog("close");
	return true;
}

function addQuoteFormDialog() {
	operForTemplateLineItem = "add";
	var newDialogDiv = jQuery(document.createElement('div'));
	if ($("#quoteTypeDetail").val() === '-1') {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Please provide "Type" and "Submitted By"</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Please provide "Type" and "Submitted By"</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var addFormValues = $("#addButtonForm").serialize();
	var joQuoteHeaderIdentity = jQuery("#joQuoteheader").text();
	if (aAddandEdit === "edit") {
		addFormValues = $("#addButtonForm").serialize();
	}
	var gridInfo = true;
	if (aGlobalVariable === "copy") {
		joQuoteHeaderIdentity = $("#joHeaderID").val();
	} else {
		joQuoteHeaderIdentity = saveQuoteDetailInfo(gridInfo);
	}
	$("#joQuoteHeaderID").val(" ");
	$("#joQuoteHeaderID").val(joQuoteHeaderIdentity);
	$("#addquotesList").trigger("reloadGrid");
	$('#addquotesList').jqGrid('setGridParam', {
		postData : {
			'joQuoteHeaderID' : joQuoteHeaderIdentity
		}
	});
	jQuery("#joQuoteheader").empty();
	jQuery("#joQuoteheader").text(joQuoteHeaderIdentity);
	var joQuoteheaderID = joQuoteHeaderIdentity;
	$("#joHeaderID").val(" ");
	$("#joHeaderID").val(joQuoteheaderID);
	var name = '';
	if (Number(joQuoteHeaderIdentity) === 0) {
		name = $("#joHeaderID").val();
		$("#addquotesList").trigger("reloadGrid");
		$('#addquotesList').jqGrid('setGridParam', {
			postData : {
				'joQuoteHeaderID' : name
			}
		});
		jQuery("#joQuoteheader").text(name);
		joQuoteheaderID = name;
		$("#joHeaderID").val(joQuoteheaderID);
	}
	if (joQuoteHeaderIdentity > joQuoteheaderID) {
		joQuoteheaderID = joQuoteHeaderIdentity;
	}
	var addQuoteValues = addFormValues + "&joQuoteHeaderID=" + joQuoteheaderID
			+ "&quoteheaderid=" + joQuoteHeaderIdentity + "&oper="
			+ aAddandEdit;
	$.ajax({
		url : "./jobtabs2/manpulaterProductQuotes",
		type : "POST",
		data : addQuoteValues,
		success : function(data) {
			$("#joQuoteheader").text(joQuoteHeaderIdentity);
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails();
			$('#addquotesList').jqGrid('setGridParam', {
				postData : {
					'joQuoteHeaderID' : joQuoteHeaderIdentity
				}
			});
			$("#addquotesList").trigger("reloadGrid");
			if (aGlobalConstant === "edit") {
				aDetailId = data + ",";
				aListDetailId += aDetailId;
				$("#joDetailID").val(aListDetailId);
			} else {
				$("#joDetailID").val('');
			}
			if (Number(joQuoteHeaderIdentity) === 0) {
				$("#addquotesList").jqGrid('GridUnload');
				loadQuotesListDetails();
				$('#addquotesList').jqGrid('setGridParam', {
					postData : {
						'joQuoteHeaderID' : name
					}
				});
				$("#addquotesList").trigger("reloadGrid");
			}
		}
	});
	return true;
}

function deleteQuote(joQuoteID) {
	$.ajax({
		url : "./jobtabs2/deleteQuickQuoteDetail",
		mType : "GET",
		data : {
			'joHeaderQuoteDetailID' : joQuoteID
		},
		success : function(data) {
			$("#addquotesList").trigger("reloadGrid");
			$.each(data, function(index, value) {
			});
		}
	});
}

/**
 * ************************************* Quote Templates code
 * ******************************************
 */
jQuery(function() {
	jQuery("#addTemplateLineItem").dialog({
		autoOpen : false,
		height : 450,
		width : 600,
		title : "Add/Edit Template Line Item",
		modal : true,
		buttons : {

		},
		close : function() {
			$('#addTemplateLineItem').validationEngine('hideAll');
			return true;
		}
	});
});
var templateAddEdit = "";

function addQuoteTemplateFrom() {	 		// Add line item in Quote Template
	operForTemplateLineItem = 'add';
	var isDetailsGridExist = $("#quoteTemplateProductsGrid").getGridParam("reccount");
	if (isDetailsGridExist < 0)
		templateAddEdit = "add";
	var newDialogDiv = jQuery(document.createElement('div'));
	if ($("#quoteTemplateTypeDetail").val() === "-1") {
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">Please Provide "Type"</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	} else if ($("#templateDescription").val() === '') {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Please provide Template Description</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	$("#productTemplate").val("");
	$("#paragraphTemplate").val("");
	$("#manufacturerTemplate").val("");
	$("#itemQuantityTemplate").val("");
	$("#costTemplate").val("");
	$("#priceTemplate").prop('disabled', true);
	$("#joQuoteTemplateDetailID").val("");
	$("#rxManufacturerIDTemplate").val("");
	$("#veFactoryIdTemplate").val("");
	$("#percentageTemplate").val("");
	$("#inlineNoteTemplate").val("");
	$("#productNoteTemplate").val("");
	$("#addTemplateLineItem").dialog({
		height : 450
	});
	jQuery("#addTemplateLineItem").dialog("open");
	return true;
}
function editTemplateLineItems() {	// Edit template line item grid
	operForTemplateLineItem = 'edit';
	templateAddEdit = "edit";
	$("#addTemplateLineItem").dialog({
		height : 400
	});
	jQuery("#addTemplateLineItem").dialog("open");
	$("#inlneNoteHideShowTemplate").css("display", "none");
	var grid = $("#quoteTemplateProductsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	// var joQuoteTemplateHeaderId = grid.jqGrid('getCell', rowId,
	// 'joQuoteTemplateHeaderId');
	var productName = grid.jqGrid('getCell', rowId, 'product');
	var paragraph = grid.jqGrid('getCell', rowId, 'paragraph');
	var manufactuer = grid.jqGrid('getCell', rowId, 'manufacturerName');
	var quantity = grid.jqGrid('getCell', rowId, 'itemQuantity');
	var cost = grid.jqGrid('getCell', rowId, 'cost');
	var price = grid.jqGrid('getCell', rowId, 'price');
	var quoteDetailID = grid
			.jqGrid('getCell', rowId, 'joQuoteTemplateDetailId');
	var rxManufactuer = grid.jqGrid('getCell', rowId, 'rxManufacturerId');
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId,
			'joQuoteTemplateHeaderId');
	var factoryID = grid.jqGrid('getCell', rowId, 'veFactoryId');
	var inlinenote = grid.jqGrid('getCell', rowId, 'inlineNote');
	var productNote = grid.jqGrid('getCell', rowId, 'productNote');
	var costCovert = cost.replace(/[^0-9\.]+/g, "");
	var priceCovert = price.replace(/[^0-9\.]+/g, "");
	var cost2 = costCovert.replace(".00", "");
	var price2 = priceCovert.replace(".00", "");
	var percentage = $("#addquotesList").jqGrid('getCell', rowId, 'percentage');
	if (cost2 === 0) {
		cost2 = '';
	}
	$("#costTemplate").val(cost2);
	if (price2 === 0) {
		price2 = '';
	}
	if (!percentage) {
		percentage = 0;
	}
	$("#productTemplate").val(productName);
	$("#paragraphTemplate").val(paragraph);
	$("#manufacturerTemplate").val(manufactuer);
	$("#itemQuantityTemplate").val(quantity);
	$("#costTemplate").val(cost2);
	$("#priceTemplate").prop('disabled', true);
	$("#priceTemplate").val(price2);
	$("#joQuoteTemplateDetailID").val(quoteDetailID);
	$("#rxManufacturerIDTemplate").val(rxManufactuer);
	$("#joQuoteTemplateHeaderID").val(joQuoteHeaderID);
	$("#veFactoryIdTemplate").val(factoryID);
	$("#percentageTemplate").val(percentage);
	$("#inlineNoteTemplate").val(inlinenote);
	$("#productNoteTemplate").val(productNote);
	$("#addTemplateLineItem").dialog({
		height : 400
	});
	jQuery("#addTemplateLineItem").dialog("open");
	return true;
}