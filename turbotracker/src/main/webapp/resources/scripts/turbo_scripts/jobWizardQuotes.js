var iscopyQuickQuoteClick = true;
var aGlobalVariable;
var aQuotetypePrev;
var aQuotePrev;
var aBidderDialogVar;
var bidScrollPosition;
var isquoteAddnew;
var aAddEditQuote;
var aGlobalConstant;
var aCancelQuote;
var aTypeDetail;
var aRevision;
var aQuoteTypeName;
var operForTemplateLineItem;
var QuoteTempLineItemHeader;
var newDialogDiv = jQuery(document.createElement('div'));
loadBidListGrid();
var dynamicColNames = [];
var defaultColNames = [];

var tempprodlbl='column1';//'Description';
var tempqtylbl='column2';//'Qty';
var tempparalbl='column3';//'Paragraph';
var tempmanlbl='column4';//'Manufacturer';
var tempspeclbl='column5';//'Spec';
var tempcostlbl='column6';//'Cost';
var tempmultlbl='column7';//'Mult';
var temppricelbl='column8';//'Price';
setTimeout('loadQuotesGrid()', 1);
var aSysAdmin = $("#userAdminID").val();
var jobStatus = getUrlVars()["jobStatus"];
var getquoteformvalue=true;
var getquotetempformvalue=true;
var oldQuoteFormvalue;
var oldQuotetempFormvalue;
 
var chkvalidateQty_YN=getSysvariableStatusBasedOnVariableName("QuotechkI2I3QtyYN");
if(chkvalidateQty_YN!=null && chkvalidateQty_YN[0].valueLong==1){
	chkvalidateQty_YN=true;
}else{
	chkvalidateQty_YN=false;
}
var chkvaldateCost_YN=getSysvariableStatusBasedOnVariableName("QuotechkI2I3CostYN");
if(chkvaldateCost_YN!=null && chkvaldateCost_YN[0].valueLong==1){
	chkvaldateCost_YN=true;
}else{
	chkvaldateCost_YN=false;
}
var chkvalidateSPItem3_YN=getSysvariableStatusBasedOnVariableName("QuotechkI3SellPriceYN");
if(chkvalidateSPItem3_YN!=null && chkvalidateSPItem3_YN[0].valueLong==1){
	chkvalidateSPItem3_YN=true;
}else{
	chkvalidateSPItem3_YN=false;
}
var chkvalidateMan_YN=getSysvariableStatusBasedOnVariableName("QuotechkI2I3ManufYN");
if(chkvalidateMan_YN!=null && chkvalidateMan_YN[0].valueLong==1){
	chkvalidateMan_YN=true;
}else{
	chkvalidateMan_YN=false;
}
var chkvalidateCategory_YN=getSysvariableStatusBasedOnVariableName("QuotechkI2I3CatYN");
if(chkvalidateCategory_YN!=null && chkvalidateCategory_YN[0].valueLong==1){
	chkvalidateCategory_YN=true;
}else{
	chkvalidateCategory_YN=false;
}
var chkvalidateSPonPrice_YN=getSysvariableStatusBasedOnVariableName("QuotechkSPpriceYN");
if(chkvalidateSPonPrice_YN!=null && chkvalidateSPonPrice_YN[0].valueLong==1){
	chkvalidateSPonPrice_YN=true;
}else{
	chkvalidateSPonPrice_YN=false;
}

var quotetempproduct={
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
						source : 'jobtabs2/templateProductList',
						minLength : 3,
						select : function(event, ui) {
							var quoteDetailID = ui.item.id;
							var rxMasterID = ui.item.manufactureID;
							$
									.ajax({
										url : "./jobtabs2/quoteTemplateDetails",
										mType : "GET",
										data : {
											'quoteDetailID' : quoteDetailID,
											'rxMasterID' : rxMasterID
										},
										success : function(data) {
											$.each(data,function(index,value) {
									console.log('Template Data:-->'+value.paragraph+'  '+value.manufacturerName+'  '+value.rxManufacturerId);
													//manufacture = value.inlineNote;
													var detail = value.paragraph;
													var manufacName = value.manufacturerName;
													var manufactureID = value.rxManufacturerId;
													var factoryID = value.detailSequenceId;
													//alert(manufactureID +':: '+manufacName );
													$("#new_row_paragraph").val(detail);
													$("#new_row_manufacturerName").val(manufacName); // $("#itemQuantity").val(quantity);
													$("#new_row_rxManufacturerId").val(manufactureID);
													$("#rxManufacturerId").val(manufactureID);
													$("#new_row_veFactoryId").val(factoryID);
													$("#paragraph").val(detail);
													$("#manufacturer").val(manufacName);
													$("#veFactoryId").val(factoryID);
												});
										}
									});
						}
					});
			}
		},
		editrules : {}
	};
var quotetemphiddenproduct={
		name : 'product',
		index : 'product',
		align : 'left',
		width : 90,
		editable : true,
		hidden : true,
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
						source : 'jobtabs2/templateProductList',
						minLength : 3,
						select : function(event, ui) {
							var quoteDetailID = ui.item.id;
							var rxMasterID = ui.item.manufactureID;
							$
									.ajax({
										url : "./jobtabs2/quoteTemplateDetails",
										mType : "GET",
										data : {
											'quoteDetailID' : quoteDetailID,
											'rxMasterID' : rxMasterID
										},
										success : function(data) {
											$.each(data,function(index,value) {
									console.log('Template Data:-->'+value.paragraph+'  '+value.manufacturerName+'  '+value.rxManufacturerId);
													//manufacture = value.inlineNote;
													var detail = value.paragraph;
													var manufacName = value.manufacturerName;
													var manufactureID = value.rxManufacturerId;
													var factoryID = value.detailSequenceId;
													//alert(manufactureID +':: '+manufacName );
													$("#new_row_paragraph").val(detail);
													$("#new_row_manufacturerName").val(manufacName); // $("#itemQuantity").val(quantity);
													$("#new_row_rxManufacturerId").val(manufactureID);
													$("#rxManufacturerId").val(manufactureID);
													$("#new_row_veFactoryId").val(factoryID);
													$("#paragraph").val(detail);
													$("#manufacturer").val(manufacName);
													$("#veFactoryId").val(factoryID);
												});
										}
									});
						}
					});
			}
		},
		editrules : {}
	};
var quotetempQuantity={
		name : 'itemQuantity',
		index : 'itemQuantity',
		align : 'center',
		width : 30,
		editable : true,
		hidden : false,
		edittype : 'text',
		cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['qtyClassName'];
		        return " class='"+classname+"'";
		},
		editoptions : {
			size : 30
		},
		editrules : {}
	};
var quotetemphiddenQuantity={
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
		editrules : {}
	};
var quotetemppara={
		name : 'paragraph',
		index : 'paragraph',
		align : 'left',
		width : 90,
		editable : true,
		hidden : false,
		
		edittype : 'text',
		editoptions : {
			size : 30
		},cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['paraClassName'];
	        return " class='"+classname+"'";
		},
		editrules : {}
	};
var quotetemphiddenpara={
		name : 'paragraph',
		index : 'paragraph',
		align : 'left',
		width : 90,
		editable : true,
		hidden : true,
		
		edittype : 'text',
		editoptions : {
			size : 30
		},cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['paraClassName'];
	        return " class='"+classname+"'";
		},
		editrules : {}
	};
var quotetempmanufac={
		name : 'manufacturerName',
		index : 'manufacturerName',
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
						$("#new_row_rxManufacturerId").val(id);
						$("#" + curId123 + "_rxManufacturerId").val(id);
						$("#rxManufacturerId").val(id);
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
		}
	,
	cellattr: function(rowId, val, rawObject) {
		var classname=rawObject['manfcClassName'];
	        return " class='"+classname+"'";
	}
	};
var quotetemphiddenmanufac={
		name : 'manufacturerName',
		index : 'manufacturerName',
		align : 'left',
		width : 90,
		editable : true,
		hidden : true,
		edittype : 'text',
		editoptions : {
			dataInit : function(elem) {
				$(elem).autocomplete({
					source : 'jobtabs2/vendorsList',
					minLength : 2,
					select : function(event, ui) {
						var id = ui.item.id;
						var name = ui.item.value;
						$("#new_row_rxManufacturerId").val(id);
						$("#" + curId123 + "_rxManufacturerId").val(id);
						$("#rxManufacturerId").val(id);
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
		}
	,
	cellattr: function(rowId, val, rawObject) {
		var classname=rawObject['manfcClassName'];
	        return " class='"+classname+"'";
	}
	};
var quotetempspec={
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
		editrules : {}	,
		cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['specClassName'];
		        return " class='"+classname+"'";
		}
	};
var quotetemphiddenspec={
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
		editrules : {}	,
		cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['specClassName'];
		        return " class='"+classname+"'";
		}
	};
var quotetempcost={
		name : 'cost',
		index : 'cost',
		align : 'right',
		width : 50,
		editable : true,
		hidden : false,
		formatter : customCurrencyFormatter,
		edittype : 'text',
		editoptions : {
			size : 30
		},
		editrules : {}
	};
var quotetemphiddencost={
		name : 'cost',
		index : 'cost',
		align : 'right',
		width : 50,
		editable : true,
		hidden : true,
		formatter : customCurrencyFormatter,
		edittype : 'text',
		editoptions : {
			size : 30
		},
		editrules : {}
	};
var quotetempmulti={
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
		editrules : {},
		cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['multClassName'];
		        return " class='"+classname+"'";
		}
	};
var quotetemphiddenmulti={
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
		editrules : {}
	};
var quotetempprice={
		name : 'price',
		index : 'price',
		align : 'right',
		width : 50,
		editable : true,
		hidden : false,
		
		formatter : customCurrencyFormatter,
		edittype : 'text',
		editoptions : {
			size : 30
		},
		editrules : {},
		cellattr: function(rowId, val, rawObject) {
			var classname=rawObject['priceClassName'];
		        return " class='"+classname+"'";
		}
		
	};
var quotetemphiddenprice={
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
		editrules : {}
	};

var quotetemphiddenposition={
		name : 'position',
		index : 'position',
		align : 'right',
		width : 50,
		editable : true,
		hidden : true,
		edittype : 'text'
	};



if (jobStatus === "Booked") {
	$(".customerNameField").attr("disabled", true);
}
jQuery(document).ready(
		function() {
			$(".datepicker").datepicker();
			$('#donedate').hide();
			$('#granteddate').hide();
			requestSubstitutionDialog();
			$('#loadingDiv').css({
				"visibility" : "hidden"
			});
			loadQuoteTemplates();
			addQuotesColumnDetails();

			$("#jobQuoteRevision").keydown(
					function(e) {
						// alert(e.keyCode);
						// Allow: backspace, delete, tab, escape, enter and .
						if ($.inArray(e.keyCode, [ 46, 8, 9, 27, 13 ]) !== -1 ||
						// Allow: Ctrl+A
						(e.keyCode == 65 && e.ctrlKey === true) ||
						// Allow: home, end, left, right
						(e.keyCode >= 35 && e.keyCode <= 39)) {
							// let it happen, don't do anything
							return;
						}
						// Ensure that it is a number and stop the keypress
						// if((e.keyCode=='110') || (e.keyCode=='190')){
						// alert(''+e.keyCode);
						// }
						if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57))
								&& (e.keyCode < 96 || e.keyCode > 105)) {
							e.preventDefault();
						}
					});
			
			TinymceTextEditorEnabledisable(true);
			
		});


/*function saveANDcloseQuote() {

	console.log('niaz::saveANDcloseQuote::' + aGlobalVariable);

	console.log('niaz::saveANDcloseQuote::2');
	var count = $("#addquotesList").getGridParam("reccount");
	if (count == 0) {
		console.log('niaz::saveANDcloseQuote::3');
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Add atleast One Line Item.</b></span>');
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
	console.log('niaz::saveANDcloseQuote::4');
	if (($('#new_row_joQuoteDetailID').val() == '')) {
		console.log('niaz::saveANDcloseQuote::5');
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color:red;">Save a Lineitem and proceed.</b></span>');
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
	try {
		console.log('niaz::saveANDcloseQuote::6');
		var aJobNumber = $("#jobNumberHiddenID").val();

		var aQuoteType = $("#quoteTypeDetail").val();

		var aQuoteRev = $("#jobQuoteRevision").val();
		gVar = '';

		if (aGlobalVariable === "add") {
			console.log('niaz::saveANDcloseQuote::7');
			if (!$('#quoteManipulateForm').validationEngine('validate')) {

				return false;
			}

			if (aQuoteRev.match(/^-\d+$/)) {
				console.log('niaz::saveANDcloseQuote::8');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						click : function() {
							// alert('23');
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				console.log('niaz::saveANDcloseQuote::9');
				return false;
			} else if (aQuoteRev === '+' || aQuoteRev === '-') {
				console.log('niaz::saveANDcloseQuote::10');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
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
				console.log('niaz::saveANDcloseQuote::11');
				return false;
			}
			
			var joHeaderId = $('#joHeaderID').val();
			$
					.ajax({
						url : "./jobtabs2/checkQuoteTypeAndRev",
						type : "GET",
						data : {
							'jobNumber' : aJobNumber,
							'quoteType' : aQuoteType,
							'quoteRev' : aQuoteRev,
							'joHeaderId' : $('#joHeaderID').val(),
							'operation' : aGlobalVariable
						},
						success : function(data) {
							console.log('niaz::saveANDcloseQuote::13');
							if (data) {
								console.log('niaz::saveANDcloseQuote::14');
								var newDialogDiv2 = jQuery(document
										.createElement('div'));
								var errorText = "A Quote with Same Type and Revision already exist. Please change 'Type' or 'Revision'.";
								jQuery(newDialogDiv2).html(
										'<span><b style="color:red;">'
												+ errorText + '</b></span>');
								jQuery(newDialogDiv2).dialog({
									modal : true,
									width : 350,
									height : 170,
									title : "Warning",
									buttons : [ {
										height : 35,
										text : "OK",
										click : function() {
											$(this).dialog("close");
											return false;
											// trigger("reloadGrid");
										}
									} ]
								}).dialog("open");
								// return false;
							} else {
								console.log('niaz::saveANDcloseQuote::15');
								console
										.log('niaz::calling saveQuoteDetailInfo wo param::'
												+ aGlobalVariable);
								saveQuoteDetailInfo();
								if (data1 !== null) {
									console.log('niaz::saveANDcloseQuote::16');
									jQuery("#addquotes").dialog("close");
									$("#addquotesList").jqGrid('GridUnload');
									$("#quotes").jqGrid('GridUnload');
									loadQuotesGrid();
									$("#quotes").trigger("reloadGrid");
									return true;
								}
								console.log('niaz::saveANDcloseQuote::17');
								$("#addquotesList").jqGrid('GridUnload');
								jQuery("#addquotes").dialog("close");
								// return true;
							}
						}
					});

		} else if ((aGlobalVariable === "edit") || (aGlobalVariable === "copy")) {
			console.log('niaz::saveANDcloseQuote::18');
			if ($("#quoteTypeDetail").val() === '') {
				console.log('niaz::saveANDcloseQuote::19');
				jQuery(newDialogDiv)
						.html(
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
				console.log('niaz::saveANDcloseQuote::20');
				return false;
			} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
				console.log('niaz::saveANDcloseQuote::21');
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide "Submitted By."</b></span>');
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
				console.log('niaz::saveANDcloseQuote::22');
				return false;
			}

			if (!$('#quoteManipulateForm').validationEngine('validate')) {
				console.log('niaz::saveANDcloseQuote::23');
				return false;
			}

			if (aQuoteRev.match(/^-\d+$/)) {
				console.log('niaz::saveANDcloseQuote::24');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
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
			} else if (aQuoteRev === '+' || aQuoteRev === '-') {
				console.log('niaz::saveANDcloseQuote::25');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
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

			// alert("aQuotePrev"+aQuotePrev+" aQuoteRev:"+aQuoteRev+"
			// aQuotetypePrev:"+aQuotetypePrev+" aQuoteType:"+aQuoteType);
			if ((aQuotePrev != aQuoteRev) || (aQuotetypePrev != aQuoteType)) {
				console.log('niaz::saveANDcloseQuote::26');
				try {
					$
							.ajax({
								url : "./jobtabs2/checkQuoteTypeAndRev",
								type : "GET",
								data : {
									'jobNumber' : aJobNumber,
									'quoteType' : aQuoteType,
									'quoteRev' : aQuoteRev,
									'joHeaderId' : $('#joHeaderID').val(),
									'operation' : aGlobalVariable
								},
								success : function(data) {
									console.log('niaz::saveANDcloseQuote::27');
									// alert(data);
									if (data) {
										console
												.log('niaz::saveANDcloseQuote::28');
										var errorText = "A Quote with Same Type and Revision already exist. Please change 'Type' or 'Revision'.";
										jQuery(newDialogDiv).html(
												'<span><b style="color:red;">'
														+ errorText
														+ '</b></span>');
										jQuery(newDialogDiv).dialog({
											modal : true,
											width : 350,
											height : 170,
											title : "Warning",
											buttons : [ {
												height : 35,
												text : "OK",
												click : function() {
													$(this).dialog("close");
													// trigger("reloadGrid");
												}
											} ]
										}).dialog("open");
										console
												.log('niaz::saveANDcloseQuote::29');
										return false;
									} else {
										console
												.log('niaz::saveANDcloseQuote::30');
										saveQuote();
										console
												.log('niaz::saveANDcloseQuote::31');
										// Added by Niaz 20140904 to change
										// Global var to Edit if it is Copy
										if (aGlobalVariable == 'copy') {
											console
													.log('niaz::saveANDcloseQuote::1');
//											alert('niaz::saveANDcloseQuote::1');
											aGlobalVariable = 'edit';
										}
										saveQuoteDetailInfo();
										console
												.log('niaz::saveANDcloseQuote::32');
										jQuery("#addquotes").dialog("close");
										$("#addquotesList")
												.jqGrid('GridUnload');
										aListDetailId = '';
										// loadQuotesGrid();
										// $("#quotes").jqGrid('GridUnload');
										$("#quotes").trigger("reloadGrid");
										console
												.log('niaz::saveANDcloseQuote::33');
										return true;
									}
								}
							});
				} catch (err) {
					// alert(err.message);
				}
			} else {
				console.log('niaz::saveANDcloseQuote::34');
				saveQuoteDetailInfo();
				console.log('niaz::saveANDcloseQuote::35');
				jQuery("#addquotes").dialog("close");
				$("#addquotesList").jqGrid('GridUnload');
				$("#quotes").trigger("reloadGrid");

			}

			return true;
		} else if (aGlobalVariable === "copy") {
			console.log('niaz::saveANDcloseQuote::36');
			// alert('3');
			if ($("#quoteTypeDetail").val() === '') {
				console.log('niaz::saveANDcloseQuote::37');
				// alert('4');
				var newDialogDiv1 = jQuery(document.createElement('div'));
				jQuery(newDialogDiv1)
						.html(
								'<span><b style="color:red;">Please Provide "Type"</b></span>');
				jQuery(newDialogDiv1).dialog({
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
			} else if (aQuoteType === aQuoteTypeName
					&& $("#jobQuoteRevision").val() === '') {
				console.log('niaz::saveANDcloseQuote::38');
				// alert('5');
				// alert('46');
				var newDialogDiv1 = jQuery(document.createElement('div'));
				errorText = "Please Add revision value.";
				jQuery(newDialogDiv1).attr("id", "msgDlg");
				jQuery(newDialogDiv1).html(
						'<span><b style="color:red;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv1).dialog({
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
			} else if (aQuoteType === aQuoteTypeName
					&& $("#jobQuoteRevision").val() === aQuotePrev) {
				console.log('niaz::saveANDcloseQuote::39');
				var newDialogDiv1 = jQuery(document.createElement('div'));
				errorText = "Please Change revision value.";
				jQuery(newDialogDiv1).attr("id", "msgDlg");
				jQuery(newDialogDiv1).html(
						'<span><b style="color:red;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv1).dialog({
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
				console.log('niaz::saveANDcloseQuote::40');
				return false;
			} else if ($("#jobQuoteRevision").val() === aQuotePrev) {
				console.log('niaz::saveANDcloseQuote::41');
				// alert('48');
				var newDialogDiv1 = jQuery(document.createElement('div'));
				errorText = "Please Change revision value.";
				jQuery(newDialogDiv1).attr("id", "msgDlg");
				jQuery(newDialogDiv1).html(
						'<span><b style="color:red;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv1).dialog({
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
				console.log('niaz::saveANDcloseQuote::42');
				return false;
			} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
				console.log('niaz::saveANDcloseQuote::43');
				// alert('49');
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide "Submitted By."</b></span>');
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
				console.log('niaz::saveANDcloseQuote::44');
				return false;
			}

			if (!$('#quoteManipulateForm').validationEngine('validate')) {
				// alert('50');
				return false;
			}
			if (aQuoteRev.match(/^-\d+$/)) {
				console.log('niaz::saveANDcloseQuote::45');
				// alert('51');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
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
				console.log('niaz::saveANDcloseQuote::46');
				return false;
			} else if (aQuoteRev === '+' || aQuoteRev === '-') {
				console.log('niaz::saveANDcloseQuote::3');
				// alert('52');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide a Valuable Number.</b></span>');
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
				console.log('niaz::saveANDcloseQuote::47');
				return false;
			}
			console.log('niaz::saveANDcloseQuote::48');
			// alert('123123'+aJobNumber+' type:'+aQuoteType+' rev:');
			$
					.ajax({
						url : "./jobtabs2/checkQuoteTypeAndRev",
						type : "GET",
						data : {
							'

' : aJobNumber,
							'quoteType' : aQuoteType,
							'quoteRev' : aQuoteRev,
							'joHeaderId' : $('#joHeaderID').val(),
							'operation' : aGlobalVariable
						},
						success : function(data) {
							console.log('niaz::saveANDcloseQuote::49');
							// alert(data);
							if (data) {
								console.log('niaz::saveANDcloseQuote::50');
								var errorText = "A Quote with Same Type and Revision already exist. Please change 'Type' or 'Revision'.";
								jQuery(newDialogDiv).html(
										'<span><b style="color:red;">'
												+ errorText + '</b></span>');
								jQuery(newDialogDiv).dialog({
									modal : true,
									width : 350,
									height : 170,
									title : "Warning",
									buttons : [ {
										height : 35,
										text : "OK",
										click : function() {
											$(this).dialog("close");
											trigger("reloadGrid");
										}
									} ]
								}).dialog("open");
								console.log('niaz::saveANDcloseQuote::51');
								return false;
							} else {
								console.log('niaz::saveANDcloseQuote::52');
								saveQuote();
								console.log('niaz::saveANDcloseQuote::53');
								$("#quotes").jqGrid('GridUnload');
								console.log('niaz::saveANDcloseQuote::54');
								loadQuotesGrid();
								console.log('niaz::saveANDcloseQuote::55');
								$("#addquotesList").jqGrid('GridUnload');
								$("#quotes").trigger("reloadGrid");
								jQuery("#addquotes").dialog("close");
								console.log('niaz::saveANDcloseQuote::56');
								return true;
							}
						}
					});
		}
		console.log('niaz::saveANDcloseQuote::57');
		return true;
	} catch (err) {
		console.log('niaz::saveANDcloseQuote::58');
	}

}*/

/*function cancelQuote() {
	// alert('i am here::');
	console.log('niaz::cancelQuote::aGlobalVariable=' + aGlobalVariable
			+ "::gVar=" + gVar);
	$("#addquotesList").jqGrid('GridUnload');
	aCancelQuote = "cancelQuoteList";
	var gridData = jQuery("#addquotesList").getRowData();
	var headerID = '';
	for (var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		headerID = rowData["joQuoteHeaderID"];
	}
	console.log('niaz::cancelQuote::1');
	if (isquoteAddnew === 'yes') {
		console.log('niaz::cancelQuote::2');
		// deleteQuickQuote(headerID, aCancelQuote);
	}
	$('#quoteManipulateForm').validationEngine('hideAll');
	gVar = '';
	// alert('i am here:: 1 ' + aGlobalVariable);
	console.log('niaz::cancelQuote::3');
	if (aGlobalVariable === "copy") {
		console.log('niaz::cancelQuote::4');
		// alert('i am here:: 1 copy');
		var rows = jQuery("#quotes").getDataIDs();
		// $("#quotes").trigger("reloadGrid");
		var grid = $("#quotes");
		var joQuoteHeaderID = grid.jqGrid('getCell', rows.length,
				'joQuoteHeaderID');
		var aCancelQuote = '';
		console.log('niaz::cancelQuote::5');
		deleteCopyQuote(joQuoteHeaderID, aCancelQuote);
	}
	console.log('niaz::cancelQuote::6');
	if (aGlobalVariable === "edit") {
		console.log('niaz::cancelQuote::7');
		// alert('niaz1');
		// saveQuoteDetailInfo();
		var count = $("#addquotesList").getGridParam("reccount");
		if (count == 0) {
			console.log('niaz::cancelQuote::8');
			jQuery(newDialogDiv)
					.html(
							'<span><b style="color:red;">Add atleast One Line Item.</b></span>');
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
			console.log('niaz::cancelQuote::9');
			return false;
		}

	}
	// $("#quotes").trigger("reloadGrid");
	console.log('niaz::cancelQuote::10');
	$("#quotes").jqGrid('GridUnload');
	console.log('niaz::cancelQuote::11');
	// commented by Niaz 2014-09-03 since updating main amount as 0
	// loadQuotesGrid();
	console.log('niaz::cancelQuote::12');
	$("#quotes").trigger("reloadGrid");
	console.log('niaz::cancelQuote::13');
	try {
		// document.getElementById(").removeEventListener("click", l.close,
		// false);

		$("#quotes").jqGrid('GridUnload');
		jQuery("#addquotes").dialog("close");
		loadQuotesGrid();
		setTimeout(function() {
			$("#quotes").trigger("reloadGrid");

		}, 1000);

	} catch (err) {
		alert(err);
	}
	 $('body').on('click','.ui-widget-overlay',function(){
	 $('#addquotes').dialog('close'); });
	console.log('niaz::cancelQuote::14');
//	$("#addQuotesView").css("display", "none");
	console.log('niaz::cancelQuote::15');
	return false;
}*/
function deletequotedetailforCopyQuote(){
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId,
			'joQuoteHeaderID');
	aCancelQuote = '';
	deleteQuickQuoteforCopyCancel(joQuoteHeaderID, aCancelQuote);
}
function deleteQuickQuoteforCopyCancel(joQuoteHeaderID, aCancelQuote) {
	$.ajax({
		url : "./jobtabs2/deleteQuoteDetailID",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {
			deleteQuoteforCopyCancel(joQuoteHeaderID, aCancelQuote);
		}
	});
}

function deleteQuoteforCopyCancel(joQuoteHeaderID, aCancelQuote) {
	$.ajax({
				url : "./jobtabs2/deleteQuickQuote",
				mType : "GET",
				data : {
					"joHeaderQuoteID" : joQuoteHeaderID
				},
				success : function(data) {
					$("#quotes").trigger("reloadGrid");
				}
			});
}
function cancelEditQuote() {
//	/*if(aGlobalVariable=="copy"){
//		deletequotedetailforCopyQuote();
//	}
//	if(aGlobalVariable === "edit"){
//		saveANDcloseQuote();
//	}
//	if ($("#joDetailID").val() !== '') {
//		var aQuoteDetailId = $("#joDetailID").val();
//		var aDetailId = aQuoteDetailId.split(",");
//		var Count = aDetailId.length;
//		Count = Count - 1;
//		for (var i = 0; i <= Count; i++) {
//			deleteDetailFrom(aDetailId[i]);
//		}
//	}
//	$('#quoteManipulateForm').validationEngine('hideAll');
//	jQuery("#addquoteTemplate").dialog("close");
//	$(this).dialog("close");
//	//$("#quotes").trigger("reloadGrid");
	var newquoteformvalue=ValidateOverallQuote();
	console.log("oldQuoteFormvalue==="+oldQuoteFormvalue);
	console.log("newquoteformvalue==="+newquoteformvalue);
	if(oldQuoteFormvalue==newquoteformvalue){
		jQuery("#addquotes").dialog("close");
		$("#quotes").trigger("reloadGrid");
	}else{
		jQuery(newDialogDiv).html(
				'<span><b style="color:Green;">'+"You have made changes,would you like to save?</b>"+'</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "Yes",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){
							$(this).dialog("close");
							saveANDcloseQuote('copy');
							jQuery("#addquotes").dialog("close");
							$("#quotes").trigger("reloadGrid");
							}
				    },
				click : function() {
					$(this).dialog("close");
					saveANDcloseQuote('copy');
					jQuery("#addquotes").dialog("close");
					$("#quotes").trigger("reloadGrid");
				}
			},
			{
				height : 35,
				text : "No",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){
							$(this).dialog("close");
							jQuery("#addquotes").dialog("close");
							$("#quotes").trigger("reloadGrid");
						}
				    },
				click : function() {
					$(this).dialog("close");
					jQuery("#addquotes").dialog("close");
					$("#quotes").trigger("reloadGrid");
				}
			}
			
			]
		}).dialog("open");
		return false;
	}
	
	
	
}

function cancelCopyQuote() {

	/*aCancelQuote = "cancelQuoteList";
	var headerID = $("#joHeaderID").val();
	deleteQuickQuote(headerID, aCancelQuote);
	$('#quoteManipulateForm').validationEngine('hideAll');
	jQuery("#addquotes").dialog("close");
	return $("#copyQuotesView").css("display", "none");*/
	
	var newquoteformvalue=ValidateOverallQuote();
	console.log("oldQuoteFormvalue==="+oldQuoteFormvalue);
	console.log("newquoteformvalue==="+newquoteformvalue);
	if(oldQuoteFormvalue==newquoteformvalue){
		jQuery("#addquotes").dialog("close");
		$("#quotes").trigger("reloadGrid");
	}else{
		jQuery(newDialogDiv).html(
				'<span><b style="color:Green;">'+"You have made changes,would you like to save?</b>"+'</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "Yes",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){
							$(this).dialog("close");
							saveANDcloseQuote();
							jQuery("#addquotes").dialog("close");
							$("#quotes").trigger("reloadGrid");
							}
				    },
				click : function() {
					$(this).dialog("close");
					saveANDcloseQuote();
					jQuery("#addquotes").dialog("close");
					$("#quotes").trigger("reloadGrid");
				}
			},
			{
				height : 35,
				text : "No",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){
							$(this).dialog("close");
							jQuery("#addquotes").dialog("close");
							$("#quotes").trigger("reloadGrid");
						}
				    },
				click : function() {
					$(this).dialog("close");
					jQuery("#addquotes").dialog("close");
					$("#quotes").trigger("reloadGrid");
				}
			}
			
			]
		}).dialog("open");
		return false;
	}
}

function planAndSpec() {
	var planAndSpecSeri = $("#planandSpecFormID").serialize();
	var joMasterID = $("#joMasterHiddenID").val();
	var planAndSpec = planAndSpecSeri + "&joMasterID=" + joMasterID;
	$.ajax({
		url : "./jobtabs2/planAndSpec",
		data : planAndSpec,
		mType : "GET",
		success : function(data) {
			var planDate = data.planDate;
			var binNumber = data.binNumber;
			var planNumber = data.planNumbers;
			 createtpusage('job-Quote Tab','Save Plan & Spec','Info','Job-Quote Tab,Saving Plan & Spec,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val()+',planDate:'+planDate);
			$("#plan_date_format").val(customFormatDateTime(planDate));
			$("#bin_number").val(binNumber);
			$("#plan_nuber_id").val(planNumber);
		}
	});
	jQuery("#planandSpecDialog").dialog("close");
}
jQuery(function() {
	jQuery("#planandSpecDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Plan & Spec",
		height : 250,
		width : 300,
		buttons : {},
		close : function() {
			return true;
		}
	});
});
function openPlanandSpec() {
	jQuery("#planandSpecDialog").dialog("open");
}
function openPriorApproval() {
	jQuery("#priorApprovalDialog").dialog("open");
}
jQuery(function() {
	jQuery("#priorApprovalDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Prior Approval",
		height : 250,
		width : 250,
		buttons : {},
		close : function() {

			return true;
		}
	});
});
function closeApproval() {
	createtpusage('job-Quote Tab','Save Prior Approval','Info','Job-Quote Tab,Saving Prior Approval,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	jQuery("#priorApprovalDialog").dialog("close");
}
function openSource() {
	jQuery("#sourceDialog").dialog("open");
}
jQuery(function() {
	jQuery("#sourceDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Source",
		height : 270,
		width : 250,
		buttons : {},
		close : function() {
			return true;
		}
	});
});
function closeSource() {
	createtpusage('job-Quote Tab','Save Source','Info','Job-Quote Tab,Saving Source,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var joMasterID = $("#joMasterHiddenID").val();
	var sourceDodge = 0;
	var sourceISqft = 0;
	var sourceLDI = 0;
	var sourceOther = 0;
	var sourceReport = $("#sourceReport").val();
	var otherSource = $("#otherSource").val();
	if($('#sourceDodge').prop('checked')) {
		sourceDodge = 1;
	} else {
		sourceDodge = 0;
	}
	if($('#sourceISqft').prop('checked')) {
		sourceISqft =1;
	} else {
		sourceISqft = 0;
		}
	if($('#sourceLDI').prop('checked')) {
		sourceLDI=1;
	} else {
		sourceLDI=0;
	}
	if($('#sourceOther').prop('checked')) {
		sourceOther=1;
	} else {
		sourceOther=0;
	}

	$.ajax({
		url : "./jobtabs2/updateJobSource",
		data : {
			'joMasterID' : joMasterID,
			'sourceDodge':sourceDodge,
			'sourceISqft':sourceISqft,
			'sourceLDI':sourceLDI,
			'sourceOther':sourceOther,
			'sourceReport':sourceReport,
			'otherSource':otherSource
		},
		mType : "GET",
		success : function(data) {
			var source1 = data.source1;
			var source2 = data.source2;
			var source3 = data.source3;
			var source4 = data.source4;
			var sourceReport = data.sourceReport1;
			var otherSource = data.otherSource;
			$("#sourceDodge").val(source1);
			$("#sourceISqft").val(source2);
			$("#sourceLDI").val(source3);
			$("#sourceOther").val(source4);
			$("#sourceReport").val(sourceReport);
			$("#otherSource").val(otherSource);
		}
	});
	jQuery("#sourceDialog").dialog("close");
}
function openAddendums() {
	jQuery("#addendumsDialog").dialog("open");
}
jQuery(function() {
	
	jQuery("#addendumsDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Addendums",
		height : 270,
		width : 250,
		buttons : {},
	    open : function(event, ui) { 
	    	$("#received_id").val(quoteReceived_id);
			$("#quoteThru_id").val(quoteThru_id);
		   },
		close : function() {
			return true;
		}
	});
});
function updateAddendums() {
	var addendumsFormSeri = $("#addendumsForm").serialize();
	var joMasterID = $("#joMasterHiddenID").val();
	var addendumsForm = addendumsFormSeri + "&joMasterID=" + joMasterID;

	if($("#received_id").val()!=null && $("#quoteThru_id").val()!=null)
	{
	if($("#received_id").val().trim().length>0 && $("#quoteThru_id").val().trim().length>0)	
		{
			$.ajax({
				url : "./jobtabs2/updateAddendums",
				data : addendumsForm,
				mType : "GET",
				success : function(data) {
					createtpusage('job-Quote Tab','Save Addendums','Info','Job-Quote Tab,Saving Addendums,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
					 quoteThru_id = data.addendumQuotedThru;
					 quoteReceived_id = data.addendumReceived;
					$("#received_id").val(quoteReceived_id);
					$("#quoteThru_id").val(quoteThru_id);
				}
			});
			jQuery("#addendumsDialog").dialog("close");
		}
	else
		{
		$('#adundumerrorStatus').html("Invalid Inputs.");
		setTimeout(function() {
			$('#adundumerrorStatus').html("");
		}, 2000);
		}
	}

}
function openAmount() {
	jQuery("#amountDialog").dialog("open");
}
jQuery(function() {
	jQuery("#amountDialog").dialog({
		autoOpen : false,
		modal : true,
		title : "Amount",
		height : 250,
		width : 330,
		buttons : {},
		close : function() {
			return true;
		}
	});
});

jQuery(function() {
	jQuery("#BidDialogCustom").dialog({
		autoOpen : false,
		modal : true,
		title : "Add/Edit Bidder",
		width : 520,
		left : 300,
		top : 290,
		buttons : {},
		close : function() {
			$('#BidDialogCustomForm').validationEngine('hideAll');
			return true;
		}
	});
});

function submitBid() {
	if (aBidderDialogVar === "add") {
		if (!$('#BidDialogCustomForm').validationEngine('validate')) {
			return false;
		}
		saveBidderNaavigatorList();
		jQuery("#BidDialogCustom").dialog("close");

	} else if (aBidderDialogVar === "edit") {
		if (!$('#BidDialogCustomForm').validationEngine('validate')) {
			return false;
		}
		saveBidderNaavigatorList();
		$('#BidDialogCustomForm').validationEngine('hideAll');
		jQuery("#BidDialogCustom").dialog("close");
	}
	return true;
}

function saveBidderNaavigatorList() {
	var bidderListValue = $("#BidDialogCustomForm").serialize();
	var grid = $("#quotesBidlist");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	
	if (aBidderDialogVar === "add") {
		var frompage = "bidlistadd";
		$
				.ajax({
					url : "./jobtabs2/manpulateQuotebidList",
					type : "POST",
					async : false,
					data : bidderListValue + "&oper=" + aBidderDialogVar
							+ "&from=" + frompage,
					success : function(data) {
						createtpusage('job-Quote Tab','Bid Save','Info','Job,Quote Tab,Save Bid,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+',Bid:'+ $("#bidder").val());
						var scrollPosition = jQuery("#quotesBidlist").closest(".ui-jqgrid-bdiv").scrollTop()
						$("#quotesBidlist").trigger("reloadGrid");
						/*setTimeout(function() {
							$('#quotesBidlist tbody tr:last-child').click();
							var rowId1 = grid.jqGrid('getGridParam', 'selrow');
							scrollToRow ("#quotesBidlist", rowId1,"-1")
						}, 500);
						*/
						var errorText = "<b style='color:Green; align:right;'>Bid Saved successfully.</b>";
						$("#quoteBidMsg").css("display", "block");
						$('#quoteBidMsg').html(errorText);
						setTimeout(function() {
							$('#quoteBidMsg').html("");
						}, 2000);
					}
				});
	} else if (aBidderDialogVar === "edit") {
		$
				.ajax({
					url : "./jobtabs2/manpulateQuotebidList",
					type : "POST",
					async : false,
					data : bidderListValue + "&oper=" + aBidderDialogVar,
					success : function(data) {
						createtpusage('job-Quote Tab','Bid Update','Info','Job,Quote Tab,Update Bid,JobNumber:'+$('input:text[name=jobHeader_JobNumber_name]').val()+',Bid:'+ $("#bidder").val());
						var scrollPosition = jQuery("#quotesBidlist").closest(".ui-jqgrid-bdiv").scrollTop();
						bidScrollPosition=scrollPosition;
						aBidderDialogVar="edit";
						$("#quotesBidlist").trigger("reloadGrid",[{current:true}]);
						/*setTimeout(function() {
							scrollToRow ("#quotesBidlist", rowId,scrollPosition)
						}, 2000);*/
						var errorText = "<b style='color:Green; align:right;'>Bid Updated successfully.</b>";
						$("#quoteBidMsg").css("display", "block");
						$('#quoteBidMsg').html(errorText);
						setTimeout(function() {
							$('#quoteBidMsg').html("");
						}, 2000);
					}
				});
	}
	return true;
}

jQuery(function() {
	jQuery("#contactCustom")
			.dialog(
					{
						autoOpen : false,
						modal : true,
						title : "Add Contact",
						width : 400,
						left : 300,
						top : 290,
						buttons : {},
						close : function() {
							$('#contactForm').validationEngine('hideAll');
							var rxMasterID = $("#rxMasterId").val();
							$
									.ajax({
										url : "./jobtabs2/filterBidderList",
										mType : "GET",
										data : {
											'rxMasterId' : rxMasterID
										},
										success : function(data) {
											var select = '<select style="width:227px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option><option value="-2" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-weight: bold;">+ Add New</option>';
											$
													.each(
															data,
															function(index,
																	value) {
																quoteId = value.id;
																var quoteName = value.value;
																select += '<option value='
																		+ quoteId
																		+ '>'
																		+ quoteName
																		+ '</option>';
															});
											select += '</select>';
											$("#contacthiddenID").hide();
											$('#contactselectID').empty();
											$('#contactselectID')
													.append(select);
										}
									});
							getContactId = function() {
								var contactId = $("#contactId").val();
								if (contactId === '-2') {
									openContact();
								}
								$("#rxContactId").val(contactId);
							};
							return true;
						}
					});
});

function openContact() {
	$("#firstName").val("");
	$("#jobPosition").val("");
	$("#email").val("");
	$("#directLine").val("");
	$("#area").val("");
	$("#exchange").val("");
	$("#subscriber").val("");
	$("#division").val("");
	$("#lastName").val("");
	jQuery("#contactCustom").dialog("open");
}

function cancelContact() {
	$('#contactForm').validationEngine('hideAll');
	jQuery("#contactCustom").dialog("close");
	var rxMasterID = $("#rxMasterId").val();
	$
			.ajax({
				url : "./jobtabs2/filterBidderList",
				mType : "GET",
				data : {
					'rxMasterId' : rxMasterID
				},
				success : function(data) {
					var select = '<select style="width:227px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option><option value="-2" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-weight: bold;">+ Add New</option>';
					$.each(data, function(index, value) {
						quoteId = value.id;
						var quoteName = value.value;
						select += '<option value=' + quoteId + '>' + quoteName
								+ '</option>';
					});
					select += '</select>';
					$("#contacthiddenID").hide();
					$('#contactselectID').empty();
					$('#contactselectID').append(select);
				}
			});
	getContactId = function() {
		var contactId = $("#contactId").val();
		if (contactId === '-2') {
			openContact();
		}
		$("#rxContactId").val(contactId);
	};
}

function submitContact() {
	if (!$('#contactForm').validationEngine('validate')) {
		return false;
	}
	var contactForm = $("#contactForm").serialize();
	var rxMasterID = $("#rxMasterId").val();
	var areaCode = $("#area").val();
	var exchangeCode = $("#exchange").val();
	var subscriberNumber = $("#subscriber").val();
	var Cell = ' ';
	if (areaCode !== "") {
		Cell = "(" + areaCode + ") " + exchangeCode + "-" + subscriberNumber;
	}

	var contactID;
	$
			.ajax({
				url : "./jobtabs2/addContact",
				type : "POST",
				data : contactForm + "&rxMasterId=" + rxMasterID + "&cell="
						+ Cell,
				success : function(data) {
					contactID = data.rxContactId;
					$
							.ajax({
								url : "./jobtabs2/filterBidderList",
								mType : "GET",
								data : {
									'rxMasterId' : rxMasterID
								},
								success : function(data) {
									var select = '<select style="width:227px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option><option value="-2" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-weight: bold;">+ Add New</option>';
									$.each(data,
											function(index, value) {
												quoteId = value.id;
												var quoteName = value.value;
												select += '<option value='
														+ quoteId + '>'
														+ quoteName
														+ '</option>';
											});
									select += '</select>';
									$("#contacthiddenID").hide();
									$('#contactselectID').empty();
									$('#contactselectID').append(select);
								}
							});
					getContactId = function() {
						var contactId = $("#contactId").val();
						if (contactId === '-2') {
							openContact();
						}
						$("#rxContactId").val(contactId);
					};
					setTimeout("$('#contactId option[value=" + contactID
							+ "]').attr('selected','selected')", 50);
					$("#rxContactId").val(contactID);
				}
			});
	jQuery("#contactCustom").dialog("close");
	return true;
}

function cancelBid() {
	$('#BidDialogCustomForm').validationEngine('hideAll');
	jQuery("#BidDialogCustom").dialog("close");
}

function openAddBid() {
	createtpusage('job-Quote Tab','Add Bidder','Info','Job-Quote Tab,Add Bidder,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	aBidderDialogVar = "add";
	$("#bidderId").val("");
	$("#bidder").val("");
	$("#low").attr("checked", false);
	$("#contactId").empty();
	$("#customer_quoteType option[value=" + -1 + "]").attr("selected", true);
	$("#lastQuote").val("");
	$("#rev").val("");
	$("#jobNumber").val($(".jobHeader_JobNumber").val());
	$("#rxMasterId").val("");
	$("#joMasterId").val("");
	$("#rxContactId").val("");
	$("#rep").val("");
	$("#bidderIDTD").hide();
	$("#lowBiddderID").hide();
	$("#quoteTypeID").hide();
	$("#lastQuoteBidderID").hide();
	$("#revBidderID").hide();
	$("#jobNumberBidderID").hide();
	$("#rxMasterBidderID").hide();
	$("#joMasterBidderID").hide();
	$("#rxContactIdBidderID").hide();
	$("#repBidderID").hide();
	jQuery("#BidDialogCustom").dialog("open");
}

$(function() {
	var cache = {};
	var lastXhr = '';
	$("#bidder")
			.autocomplete(
					{
						minLength : 3,
						timeout : 1000,
						select : function(event, ui) {
							var rxMasterid = ui.item.id;
							$("#rxMasterId").val(rxMasterid);
							$.ajax({
								url : "./jobtabs2/filterQuoteTypeID",
								mType : "GET",
								data : {
									'rxMasterId' : rxMasterid
								},
								success : function(data) {
									var quoteTypeID = data.cuMasterTypeId;
									$(
											"#customer_quoteType option[value="
													+ quoteTypeID + "]").attr(
											"selected", true);
								}
							});
							$
									.ajax({
										url : "./jobtabs2/filterBidderList",
										mType : "GET",
										data : {
											'rxMasterId' : rxMasterid
										},
										success : function(data) {
											var select = '<select style="width:227px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option><option value="-2" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-weight: bold;">+ Add New</option>';
											$
													.each(
															data,
															function(index,
																	value) {
																quoteId = value.id;
																var quoteName = value.value;
																select += '<option value='
																		+ quoteId
																		+ '>'
																		+ quoteName
																		+ '</option>';
															});
											select += '</select>';
											$("#contacthiddenID").hide();
											$('#contactselectID').empty();
											$('#contactselectID')
													.append(select);
										}
									});
							getContactId = function() {
								var contactId = $("#contactId").val();
								if (contactId === '-2') {
									openContact();
								}
								$("#rxContactId").val(contactId);
							};
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("jobtabs2/bidderList", request,
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

function closeAmount() {
	jQuery("#amountDialog").dialog("close");
}

jQuery(function() {
	var dialogWidth = 1150;
	jQuery("#addquotes")
			.dialog(
					{
						autoOpen : false,
						closeOnEscape: false,
						height : 1000,
						width : dialogWidth,
						top : 1000,
						position : [
								($(window).width() / 2) - (dialogWidth / 2),
								190 ],
						modal : true,
						title : 'Add/Edit Quote',
						iframe : true,
						open : function(event, ui) {
							getquoteformvalue=true;
							$("#quotepdfpreviewButton").css({ opacity: 'initial' });
							$("#saveastemplateid").css({ opacity: 'initial' });
							jQuery('.ui-dialog-titlebar-close').removeClass(
									"ui-dialog-titlebar-close").hide();
						},
						buttons : {

						}
					// ,
					// close : function() {
					// jQuery(this).dialog("close");
					// }
					});
});

function planAndSpecJobChange() {
	if ($('#isPlanAndSpecJob').is(':checked')) {
		$('#planAndSpecJobTable').show();
		$('#priorapproval').show();
	} else {
		$('#planAndSpecJobTable').hide();
		$('#priorapproval').hide();
	}
}

function PriorApproval() {
	if ($('#isPriorApproval').is(':checked')) {
		$('#priorApproval').show();
	} else {
		$('#priorApproval').hide();
	}
}

function Source() {
	if ($('#isSource').is(':checked')) {
		$('#sourcehidden').show();
		$('#reporthidden').show();
	} else {
		$('#sourcehidden').hide();
		$('#reporthidden').hide();
	}
}

function Addendums() {
	if ($('#isAddendums').is(':checked')) {
		$('#addendums').show();
	} else {
		$('#addendums').hide();
	}
}

function Amount() {
	if ($('#isAmount').is(':checked')) {
		$('#amounthiiden').show();
	} else {
		$('#amounthiiden').hide();
	}
}

function customCurrencyFormatter(cellValue, options, rowObject) {
	if (isNaN(cellValue)) {
		try {
			var disAmt = cellValue.replace(/[^-0-9\.]+/g, "");
			cellValue = Number(disAmt);
		} catch (err) {
			cellValue = 0;
		}

	}
	return formatCurrency(cellValue);
}
/**
 * Seperate currency formatter for Cost and sell price for Quotes validation
 * Created by: Praveen kumar Created at:08/20/2014
 * 
 */
function customCurrencyFormatterCost(cellValue, options, rowObject) {
	if (isNaN(cellValue)) {
		try {
			var disAmt = cellValue.replace(/[^-0-9\.]+/g, "");
			cellValue = Number(disAmt);
		} catch (err) {
			cellValue = 0;
		}

	}
	return formatCurrency(cellValue);
}

/*function addquotesdialog() {

	aGlobalVariable = "add";
	aGlobalConstant = "add";
	isquoteAddnew = "yes";
	var editQuotesView = '<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelQuote()" style="width:80px;">';
	$("#editQuotesView").empty();
	$("#copyQuotesView").empty();
	$("#addQuotesView").empty();
	$("#addQuotesView").append(editQuotesView);
	jQuery("#joQuoteheader").text(0);
	loadQuotesListDetails();
	var fullName = $("#loginNameHiddenID").val();
	var userID = $("#userUDHiddenID").val();
	var initials = $("#userInitialsHiddenID").val();
	$("#jobQuoteSubmittedBYFullName").val(fullName);
	$("#jobQuoteSubmittedBYID").val(userID);
	$("#quoteDiscountedPrice").val(formatCurrency(0));
	$("#quoteTypeDetailID").val("");
	$("#quoteTypeDetail").val("");
	$("#jobQuoteRevision").val("");
	$("#jobQuoteInternalNote").val("");
	$("#quoteRemarksID").val("");
	$("#joHeaderID").val("");
	$("#joDetailID").val("");
	$("#jobQuoteSubmittedBYInitials").val(initials);
	$("#quoteTypeDetail option[value=" + -1 + "]").attr("selected", true);
	$('#addquotesList').jqGrid('setGridParam', {
		postData : {
			'joQuoteHeaderID' : 0
		}
	});
	$('#addquotesList').trigger("reloadGrid");
	jQuery("#addquotes").dialog("open");
}*/

function editQuoteDetails(editorCopy) {
	if(editorCopy=='copy'){
		 getquoteformvalue=false;
	}else{
		 getquoteformvalue=true;
	}
	 
	$("#Quotes_editorCopy").val(editorCopy);
	//checkeditquotedetails();
	 createtpusage('job-Quote Tab','Edit Quote','Info','Job-Quote Tab,Edit Quote,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	if(!AllowEditingofSentQuotes && editorCopy!='copy'){
		var grid = $("#quotes");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var rowdata=grid.jqGrid('getRowData',rowId);
		var type=rowdata['quoteType'];
		var rev=rowdata['rev'];
		
		var gridData = jQuery("#quotesBidlist").getRowData();
		for (var index = 0; index < gridData.length; index++) {
			var rowData = gridData[index];
			if(type==rowData['quoteType'] && rev==rowData['rev']){
				var quoteemailstatus=rowData["quoteemailstatus"];
				var lastQuote=rowData['lastQuote'];
				if(lastQuote!="" && quoteemailstatus==1){
					jQuery(newDialogDiv).html('<span style="color:#FF0066;">The Quote has been sent already and you cannot edit.Would you like to create a Revision?</span><br><hr style="color: #cb842e;">');
					 jQuery(newDialogDiv).dialog({modal: true, title:"Alert",width:300, 
							buttons: [{height:30,text: "Yes",
								 keypress:function(e){
										var x = e.keyCode;
										if(x==13){
											$(newDialogDiv).dialog("close");editQuoteDetails('copy');
											return false;
										}
								    },
								click: function() {
								$(this).dialog("close");
								editQuoteDetails('copy');
								return false;
								}},
								{height:30,text: "No",
									keypress:function(e){
										var x = e.keyCode;
										if(x==13){
											$(newDialogDiv).dialog("close");
											return false;
										}
								    },
									click: function() {
									$(this).dialog("close"); 
									return false;
									}},
								
								]}).dialog("open");
					return false;
					break;
				}
//				else{
//					break;
//				}
			}
		}
		
	}
	
	
	
	aGlobalVariable = "edit";
	aGlobalConstant = "edit";

	isquoteAddnew = "no";
	var editQuotesView = '<input type="button" id="SaveQuoteButtonID" class="savehoverbutton turbo-tan" value="Save" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" id="CloseQuoteButtonID" class="cancelhoverbutton turbo-tan"  value="Close" onclick="cancelEditQuote()" style="width:80px;">';
	$("#addQuotesView").empty();
	$("#copyQuotesView").empty();
	$("#editQuotesView").empty();
	$("#editQuotesView").append(editQuotesView);
	aCancelQuote = '';
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please click one of the Quote to Edit.";

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	onDoubleClickEditQuoteDetails(rowId);
	/*
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
	var quoteTypeID = grid.jqGrid('getCell', rowId, 'quoteTypeID');
	var createdByID = grid.jqGrid('getCell', rowId, 'createdByID');
	var createdByName = grid.jqGrid('getCell', rowId, 'createdByName');
	var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
	var quoteRevision = grid.jqGrid('getCell', rowId, 'rev');
	aQuotetypePrev = quoteTypeID;
	aQuotePrev = quoteRevision;
	var internalNote = grid.jqGrid('getCell', rowId, 'internalNote');
	var quoteRemarks = grid.jqGrid('getCell', rowId, 'quoteRemarks');
	var discountAmt = grid.jqGrid('getCell', rowId, 'discountAmount');
	$("#quoteDiscountedPrice").val(discountAmt);
	$("#jobQuoteSubmittedBYFullName").val(createdBYFullName);
	$("#jobQuoteSubmittedBYID").val(createdByID);
	$("#jobQuoteSubmittedBYInitials").val(createdByName);
	$("#jobQuoteRevision").val(quoteRevision);
	$("#jobQuoteInternalNote").val(internalNote);
	$("#quoteRemarksID").val(quoteRemarks);
	$("#joDetailID").val("");
	$("#quoteTypeDetail option[value=" + quoteTypeID + "]").attr("selected",
			true);
	$("#addquotesList").jqGrid('GridUnload');
	loadQuotesListDetails();
	$('#addquotesList').jqGrid('setGridParam', {
		postData : {
			'joQuoteHeaderID' : joQuoteHeaderID
		}
	});
	$('#addquotesList').trigger("reloadGrid");
	jQuery("#joQuoteheader").text(joQuoteHeaderID);
	$("#joHeaderID").val(joQuoteHeaderID);
	jQuery("#addquotes").dialog("open");
	return true;
	*/
}

function onDoubleClickEditQuoteDetails(theRowID) {
	
	
	templateheaderselectbox();
	aGlobalVariable = "edit";
	aGlobalConstant = "edit";
	isquoteAddnew = "no";
	var editQuotesView = '<input type="button" id="SaveQuoteButtonID" class="savehoverbutton turbo-tan" value="Save" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" id="CloseQuoteButtonID" class="cancelhoverbutton turbo-tan"  value="Close" onclick="cancelEditQuote()" style="width:80px;">';
	$("#addQuotesView").empty();
	$("#copyQuotesView").empty();
	$("#editQuotesView").empty();
	$("#editQuotesView").append(editQuotesView);
	aCancelQuote = '';
	var joQuoteHeaderID = $("#quotes").jqGrid('getCell', theRowID,
			'joQuoteHeaderID');
	var quoteTypeID = $("#quotes").jqGrid('getCell', theRowID, 'quoteTypeID');
	var createdByID = $("#quotes").jqGrid('getCell', theRowID, 'createdByID');
	var createdByName = $("#quotes").jqGrid('getCell', theRowID,
			'createdByName');
	var createdBYFullName = $("#quotes").jqGrid('getCell', theRowID,
			'createdBYFullName');
	var quoteRevision = $("#quotes").jqGrid('getCell', theRowID, 'rev');
	var internalNote = $("#quotes").jqGrid('getCell', theRowID, 'internalNote');
	var quoteRemarks = $("#quotes").jqGrid('getCell', theRowID, 'quoteRemarks');
	var discountAmt = $("#quotes")
			.jqGrid('getCell', theRowID, 'discountAmount');
	var displayQuantity = $("#quotes").jqGrid('getCell', theRowID,
			'displayQuantity');
	var printQuantity = $("#quotes").jqGrid('getCell', theRowID,
			'printQuantity');
	var displayParagraph = $("#quotes").jqGrid('getCell', theRowID,
			'displayParagraph');
	var printParagraph = $("#quotes").jqGrid('getCell', theRowID,
			'printParagraph');
	var displayManufacturer = $("#quotes").jqGrid('getCell', theRowID,
			'displayManufacturer');
	var printManufacturer = $("#quotes").jqGrid('getCell', theRowID,
			'printManufacturer');
	var displayCost = $("#quotes").jqGrid('getCell', theRowID, 'displayCost');
	var printCost = $("#quotes").jqGrid('getCell', theRowID, 'printCost');
	var displayPrice = $("#quotes").jqGrid('getCell', theRowID, 'displayPrice');
	var printPrice = $("#quotes").jqGrid('getCell', theRowID, 'printPrice');
	var displayMult = $("#quotes").jqGrid('getCell', theRowID, 'displayMult');
	var printMult = $("#quotes").jqGrid('getCell', theRowID, 'printMult');
	var displaySpec = $("#quotes").jqGrid('getCell', theRowID, 'displaySpec');
	var printSpec = $("#quotes").jqGrid('getCell', theRowID, 'printSpec');

	var notesFullWidth = $("#quotes").jqGrid('getCell', theRowID,
			'notesFullWidth');
	var lineNumbers = $("#quotes").jqGrid('getCell', theRowID, 'lineNumbers');
	var printTotal = $("#quotes").jqGrid('getCell', theRowID, 'printTotal');
	var hidePrice = $("#quotes").jqGrid('getCell', theRowID, 'hidePrice');
	
	//Added by Leo ID# 486
	var includeLSD = $("#quotes").jqGrid('getCell', theRowID, 'includeLSD');
	var donotQtyitem2and3 = $("#quotes").jqGrid('getCell', theRowID, 'donotQtyitem2and3');
	var showTotPriceonly = $("#quotes").jqGrid('getCell', theRowID, 'showTotPriceonly');
	var includeTotprice = $("#quotes").jqGrid('getCell', theRowID, 'includeTotprice');
	var LSDValue = $("#quotes").jqGrid('getCell', theRowID, 'lsdValue');
	
	//alert(includeLSD + ""+donotQtyitem2and3+""+showTotPriceonly+""+includeTotprice+""+LSDValue.toFixed(2))
	
	if(includeLSD =="true")
		{
		$("#chk_includeLSD").attr("checked",true)
		$("#txt_LSDValue").val(LSDValue);
		$("#txt_LSDValue").css("display","inherit");
		}
	else
		{
		$("#chk_includeLSD").attr("checked",false);
		$("#txt_LSDValue").css("display","none");
		}
	
	if(donotQtyitem2and3=="true")
		$("#chk_donotQtyitem2and3").attr("checked",true)
	else
		$("#chk_donotQtyitem2and3").attr("checked",false)
		
	if(showTotPriceonly =="true")
		$("#chk_showTotPriceonly").attr("checked",true)
	else
		$("#chk_showTotPriceonly").attr("checked",false)
		
	
		
	
	$("#quoteTypeDetail option[value=" + quoteTypeID + "]").attr("selected",
			true);
	$("#quoteDiscountedPrice").val(discountAmt);
	$("#jobQuoteSubmittedBYFullName").val(createdBYFullName);
	$("#jobQuoteSubmittedBYID").val(createdByID);
	$("#jobQuoteSubmittedBYInitials").val(createdByName);
	$("#jobQuoteRevision").val(quoteRevision);
	$("#jobQuoteInternalNote").val(internalNote);
	$("#quoteRemarksID").val(quoteRemarks);
	$("#quantityValueDisplayID").val(displayQuantity);
	$("#quantityValuePrintID").val(printQuantity);
	$("#quantityValueDisplayParaID").val(displayParagraph);
	$("#quantityValuePrintParaID").val(printParagraph);
	$("#quantityValueDisplayManufID").val(displayManufacturer);
	$("#quantityValuePrintManufID").val(printManufacturer);
	$("#quantityValueDisplaySpecID").val(displaySpec);
	$("#quantityValuePrintSpecID").val(printSpec);
	$("#quantityValueprintCostID").val(displayCost);
	$("#quantityValuePrintCostID").val(printCost);
	$("#quantityValueDispalyMultiID").val(displayMult);
	$("#quantityValueprintMultiID").val(printMult);
	$("#quantityValueDisplayPriceID").val(displayPrice);
	$("#quantityValuePrintPriceID").val(printPrice);
	$("#inlineNotePage").val(notesFullWidth);
	$("#printLineID").val(lineNumbers);
	$("#summationID").val(printTotal);
	$("#showTotallingID").val(hidePrice);
	$("#inlineNotePage").val();
	$("#joDetailID").val("");
	if(printTotal==1){
		$("#quotechkTotalPrice").attr("checked",true);
	}else{
		$("#quotechkTotalPrice").attr("checked",false);
	}
	
	//addQuotesColumnDetails();
	LineItemtypeonchange("-1");
	//loadnewquotesList();
	//$("#addquotesList").jqGrid('GridUnload');
	//loadQuotesListDetails();
//	$('#addnewquotesList').jqGrid('setGridParam', {
//		postData : {
//			'joQuoteHeaderID' : joQuoteHeaderID
//		}
//	});
	//$('#addquotesList').trigger("reloadGrid");
	jQuery("#joQuoteheader").text(joQuoteHeaderID);
	$("#joHeaderID").val(joQuoteHeaderID);
	//$('#addnewquotesList').trigger("reloadGrid", [{page:1}]);
	jQuery("#addnewquotesList").jqGrid('setGridParam',{url:"./jobtabs2/loadquoteLineItems",postData : {'joquoteheaderid':function(){
		var joquoteheaderid=$("#joQuoteheader").text();
		return joquoteheaderid;
	}}}).trigger("reloadGrid");
	jQuery("#addquotes").dialog("open");
	return true;
}

function editBidDetails() {
    createtpusage('job-Quote Tab','Edit Bidder','Info','Job-Quote Tab,Edit Bidder,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	aBidderDialogVar = "edit";
	var grid = $("#quotesBidlist");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please click one of the Bidder to Edit.";

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var joBiddderID = grid.jqGrid('getCell', rowId, 'bidderId');
	var joBidder = grid.jqGrid('getCell', rowId, 'bidder');
	var joBidderLow = grid.jqGrid('getCell', rowId, 'low');
	var joQuoteTypeID = grid.jqGrid('getCell', rowId, 'quoteTypeID');
	var joLastQuote = grid.jqGrid('getCell', rowId, 'lastQuote');
	var joLost = joLastQuote.split("> ");
	var joRev = grid.jqGrid('getCell', rowId, 'rev');
	var jobNumber = grid.jqGrid('getCell', rowId, 'jobNumber');
	var rxMasterID = grid.jqGrid('getCell', rowId, 'rxMasterId');
	var joMasterID = grid.jqGrid('getCell', rowId, 'joMasterId');
	var rxContactID = grid.jqGrid('getCell', rowId, 'rxContactId');
	var joRep = grid.jqGrid('getCell', rowId, 'rep');
	$("#customer_quoteType option[value=" + joQuoteTypeID + "]").attr(
			"selected", true);
	$("#bidderId").val(joBiddderID);
	$("#bidder").val(joBidder);
	if (joBidderLow === 'Yes') {
		$("#low").attr("checked", true);
	} else {
		$("#low").attr("checked", false);
	}
	$("#rev").val(joRev);
	$("#lastQuote").val(joLost[1]);
	$("#jobNumber").val(jobNumber);
	$("#rxMasterId").val(rxMasterID);
	$("#joMasterId").val(joMasterID);
	$("#rxContactId").val(rxContactID);
	$("#rep").val(joRep);
	$("#bidderIDTD").hide();
	$("#lowBiddderID").show();
	$("#quoteTypeID").hide();
	$("#lastQuoteBidderID").hide();
	$("#revBidderID").hide();
	$("#jobNumberBidderID").hide();
	$("#rxMasterBidderID").hide();
	$("#joMasterBidderID").hide();
	$("#rxContactIdBidderID").hide();
	$("#repBidderID").hide();
	$
			.ajax({
				url : "./jobtabs2/filterBidderList",
				mType : "GET",
				async:false,
				data : {
					'rxMasterId' : rxMasterID
				},
				success : function(data) {
					var select = '<select style="width:227px;margin-left: 3px;" id="contactId" onchange="getContactId()"><option value="-1"></option><option value="-2" style="color:#CB842E;font-family: Verdana,Arial,sans-serif;font-weight: bold;">+ Add New</option>';
					$.each(data, function(index, value) {
						quoteId = value.id;
						var quoteName = value.value;
						select += '<option value=' + quoteId + '>' + quoteName
								+ '</option>';
					});
					select += '</select>';
					$("contacthiddenID").hide();
					$('#contactselectID').empty();
					$('#contactselectID').append(select);
				}
			});
	getContactId = function() {
		var contactId = $("#contactId").val();
		if (contactId === '-2') {
			openContact();
		}
		$("#rxContactId").val(contactId);
	};
	$("#contactId option[value=" + rxContactID+ "]").attr('selected','selected');
//	setTimeout("$('#contactId option[value=" + rxContactID
//			+ "]').attr('selected','selected')", 1000);
	jQuery("#BidDialogCustom").dialog("open");
	return true;
}

function parseRequestValues() {
	var jobSource1 = $("#source1HiddenID").val();
	var jobSource2 = $("#source2HiddenID").val();
	var jobSource3 = $("#source3HiddenID").val();
	var jobSource4 = $("#source4HiddenID").val();
	var aPlanAndSpecJob = $("#planAndSpecJobHiddenID").val();

	if (jobSource1 === 'true') {
		$("#sourceDodge").attr("checked", true);
	}
	if (jobSource2 === 'true') {
		$("#sourceISqft").attr("checked", true);
	}
	if (jobSource3 === 'true') {
		$("#sourceLDI").attr('checked', true);
	}
	if (jobSource4 === 'true') {
		$("#sourceOther").attr("checked", true);
	}
	if (aPlanAndSpecJob === 'true') {
		$("#isPlanAndSpecJob").attr("checked", true);
		$('#planAndSpecJobTable').show();
	} else {
		$('#planAndSpecJobTable').hide();
	}
	return true;
}

function done() {
	if ($('#donecheck').is(':checked')) {
		$('#donedate').show();
	} else {
		$('#donedate').hide();
	}
}

function granted() {
	if ($('#grantedcheck').is(':checked')) {
		$('#granteddate').show();
	} else {
		$('#granteddate').hide();
	}
}

function requestsubstitutiondialog() {
	jQuery("#requestSubstitution").dialog("open");
}
function requestSubstitutionDialog() {
	jQuery("#requestSubstitution").dialog({
		autoOpen : false,
		height : 820,
		width : 880,
		title : "Request For Substitution",
		modal : true,
		buttons : {
			"Submit" : function() {
			},
			Cancel : function() {
				jQuery(this).dialog("close");
			}
		},
		close : function() {
			return true;
		}
	});
	return true;
}

function saveQuoteDetailInfo(gridInfo) {
	// alert('niaz::calling saveQuoteDetailInfo');
	console.log('niaz::saveQuoteDetailInfo::1');
	console.log('niaz::calling saveQuoteDetailInfo::' + aGlobalVariable);
	try {
		console.log('niaz::saveQuoteDetailInfo::2');
		var joQuoteHeaderID = 0;
		var id = jQuery("#quotesBidlist").jqGrid('getGridParam', 'selrow');
		var rxContactId = jQuery("#quotesBidlist").getCell(id, 12);
		var bidderId = jQuery("#quotesBidlist").getCell(id, 1);

		if (rxContactId == '') {
			rxContactId = 0;
		}
		var quoteInfo = $("#quoteManipulateForm").serialize();
		if (aGlobalVariable === "edit" || aGlobalVariable == "copy") {
			console.log('niaz::saveQuoteDetailInfo::3');
			joQuoteHeaderID = $("#joHeaderID").val();
			// alert("&jobQuoteRevision="+$('#jobQuoteRevision').val()+"&quoteTypeDetailName="+$('#quoteTypeDetail').val());
			quoteInfo = quoteInfo + "&joHeaderQuoteID=" + joQuoteHeaderID
					+ "&jobQuoteRevision=" + $('#jobQuoteRevision').val()
					+ "&quoteTypeDetailName=" + $('#quoteTypeDetail').val();
		}

		console.log('niaz::saveQuoteDetailInfo::4');
		if (gridInfo !== true) {
			console.log('niaz::saveQuoteDetailInfo::5');
			if ($("#quoteTypeDetail").val() === "-1") {
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Please Provide "Type"</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				console.log('niaz::saveQuoteDetailInfo::6');
				return false;
			} else if (jQuery("#addquotesList").getGridParam("records") === 0) {
				console.log('niaz::saveQuoteDetailInfo::7');
				jQuery(newDialogDiv)
						.html(
								'<span><b style="color:red;">Add  atleast one Line item</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Alert Message.",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				console.log('niaz::saveQuoteDetailInfo::8');
				return false;
			}
		}
		console.log('niaz::saveQuoteDetailInfo::9');
		var gridData = jQuery("#addquotesList").getRowData();
		var totalcost = 0;
		var totalPrice = 0;// ($('#quoteTotalPrice').val()).replace(/[^0-9\.]+/g,
		// "").replace(".00","");
		var cost = 0;
		var price = 0;
		var disCountPrice = $("#quoteDiscountedPrice").val();
		var disAmt = disCountPrice.replace(/[^-0-9\.]+/g, "");
		disAmt = Number(disAmt);
		for (var index = 0; index < gridData.length; index++) {
			var rowData = gridData[index];
			if (rowData["cost"])
				cost = rowData["cost"].replace(/[^-0-9\.]+/g, "");
			console.log(cost);
			if (isNaN(cost)) {
				cost = 0;
			}
			console.log('niaz::saveQuoteDetailInfo::10');
			totalcost = totalcost + Number(cost);
			if (rowData["price"]) {
				price = rowData["price"].replace(/[^-0-9\.]+/g, "");
				if (isNaN(price)) {
					price = 0;
				}
				totalPrice = totalPrice + Number(price);
			}

		}
		console.log('niaz::saveQuoteDetailInfo::11');
		if (joQuoteHeaderID === false) {
			joQuoteHeaderID = "";
		}
		var data1 = '';
		// alert('niaz::saveQuoteDetailInfo::12'+quoteInfo);
		console.log('niaz::saveQuoteDetailInfo::12' + quoteInfo);
		$
				.ajax({
					url : "./jobtabs2/SaveQuoteDetailInfo",
					type : "POST",
					async : false,
					data : quoteInfo + "&jobNumber="
							+ $("#jobNumber_ID").text() + "&token="
							+ aGlobalVariable + "&totalcost=" + totalcost
							+ "&totalPrice=" + totalPrice + "&joHeaderQuoteID="
							+ joQuoteHeaderID + "&quoteDiscountedPrice="
							+ disAmt + "&quoteRemarks="
							+ $("#quoteRemarksID").val() + "&rxContactID="
							+ rxContactId + "&jobQuoteInternalNote="
							+ $('#jobQuoteInternalNote').val()+"&joMasterID="+ $("#joMaster_ID").text(),
					success : function(data) {
						console.log('niaz::saveQuoteDetailInfo::13');
						if (aGlobalVariable === 'copy') {
							var joQuoteheaderID = data;
							var gridData = jQuery("#addquotesList")
									.getRowData();
							for (var index = 0; index < gridData.length; index++) {
								var rowData = gridData[index];
								var product = rowData["product"];
								var quantity = rowData["itemQuantity"];
								var manufacturerID = rowData["rxManufacturerID"];
								var paragraph = rowData["paragraph"];
								var factoryID = rowData["veFactoryId"];
								var cost = rowData["cost"].replace(
										/[^0-9\.]+/g, "");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(
										/[^0-9\.]+/g, "");
								//var spec = rowData["spec"];
								//var mult = rowData["mult"];
								/*if(mult.length==0){
									mult=0;
								}
								if(spec.length==0){
									spec==0;
								}*/
								totalPrice = totalPrice + Number(price);
								var addQuoteValues = "product=" + product
										+ "&paragraph=" + paragraph
										+ "&rxManufacturerID=" + manufacturerID
										+ "&itemQuantity=" + quantity
										+ "&cost=" + cost
										+ "&oper=add&joQuoteHeaderID="
										+ joQuoteheaderID + "&quoteheaderid="
										+ joQuoteheaderID + "&veFactoryId="
										+ factoryID + "&price=" + price/*+"&spec="+spec+"&mult="+mult*/;
								$
										.ajax({
											url : "./jobtabs2/manpulaterProductQuotes",
											type : "POST",
											data : addQuoteValues,
											success : function(data) {
												console
														.log('niaz::saveQuoteDetailInfo::14');
												$("#joQuoteheader").text(
														joQuoteHeaderIdentity);
												$("#addquotesList").jqGrid(
														'GridUnload');
												loadQuotesListDetails(joQuoteheaderID);
												$('#addquotesList')
														.jqGrid(
																'setGridParam',
																{
																	postData : {
																		'joQuoteHeaderID' : joQuoteHeaderIdentity
																	}
																});
												$("#addquotesList").trigger(
														"reloadGrid");
												if (Number(joQuoteHeaderIdentity) === 0) {
													$("#addquotesList").jqGrid(
															'GridUnload');
													loadQuotesListDetails(joQuoteheaderID);
													$('#addquotesList')
															.jqGrid(
																	'setGridParam',
																	{
																		postData : {
																			'joQuoteHeaderID' : name
																		}
																	});
													$("#addquotesList")
															.trigger(
																	"reloadGrid");
												}
											}
										});
							}
							console.log('niaz::saveQuoteDetailInfo::15');
							data1 = data;
							// if(aGlobalVariable!='edit'){
							$("#quotes").trigger("reloadGrid");
							// }

							// jQuery(newDialogDiv)
							// .html(
							// '<span><b style="color:Green;">Quote details
							// copied.</b></span>');
							// jQuery(newDialogDiv).dialog({
							// modal : true,
							// width : 300,
							// height : 150,
							// title : "Success.",
							// buttons : [ {
							// height : 35,
							// text : "OK",
							// click : function() {
							// $(this).dialog("close");
							// }
							// } ]
							// }).dialog("open");
							$("#joHeaderID").val(data1);
							console.log('niaz::saveQuoteDetailInfo::16');
							return true;
						}
						if (aGlobalVariable != 'copy') {
							console.log('niaz::saveQuoteDetailInfo::17');
							aGlobalVariable = "edit";
						}
						console.log('niaz::saveQuoteDetailInfo::18');
						data1 = data;

						// if (aAddEditQuote !== 'addEditQuote'&&aGlobalVariable
						// != 'edit') {
						$("#quotes").trigger("reloadGrid");
						// }
						// alert(aAddandEdit);
						if (aAddandEdit === 'add') {
							console.log('niaz::saveQuoteDetailInfo::18');
							$("#quotes").trigger("reloadGrid");
						}
						var validateMsg = "<b style='color:Green; align:right;'>Quote details updated.</b>";
						$("#quoteAddMsg").css("display", "block");
						$('#quoteAddMsg').html(validateMsg);
						setTimeout(function() {
							$('#quoteAddMsg').html("");
						}, 2000);
						$("#joHeaderID").val(data1);
						$("#quotes").jqGrid('GridUnload');
						console.log('niaz::saveQuoteDetailInfo::19');
						loadQuotesGrid();
						console.log('niaz::saveQuoteDetailInfo::20');
						$("#quotes").trigger("reloadGrid");
						return true;
					},
					error : function(jqXHR, textStatus, errorThrown) {
						console.log('niaz::saveQuoteDetailInfo::21');
						errorText = $(jqXHR.responseText).find('u').html();
						var validateMsg = "<b style='color:red; align:right;'>Error on modifying the Data details updated.</b>";
						$("#quoteAddMsg").css("display", "block");
						$('#quoteAddMsg').html(validateMsg);
						setTimeout(function() {
							$('#quoteAddMsg').html("");
						}, 2000);
						return true;
					}
				});
		return data1;
	} catch (err) {

	}
}

$(function() {
	var cache = {};
	var lastXhr = '';
	$("#jobQuoteSubmittedBYFullName").autocomplete(
			{
				minLength : 1,
				timeout : 1000,
				select : function(event, ui) {
					var id = ui.item.id;
					$("#jobQuoteSubmittedBYID").val(id);
					var value = new Array();
					var label = ui.item.label;
					value = label.split("(");
					var value1 = value[1].replace(")", "");
					$("#jobQuoteSubmittedBYInitials").val(value1);
				},
				source : function(request, response) {
					var term = request.term;
					if (term in cache) {
						response(cache[term]);
						return;
					}
					lastXhr = $.getJSON(
							"userlistcontroller/getuserautofilldata", request,
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

/*function copyQuickQuote() {
	if(iscopyQuickQuoteClick){
		iscopyQuickQuoteClick = false;
	aGlobalVariable = "copy";
	var copyQuotesView = '<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelCopyQuote()" style="width:80px;">';
	$("#addQuotesView").empty();
	$("#editQuotesView").empty();
	$("#copyQuotesView").empty();
	$("#copyQuotesView").append(copyQuotesView);
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');

	if (rowId === null) {
		var errorText = "Please click one of the Quote to Copy.";
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

	var joQuoteHeaderID = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
	var quoteTypeID = grid.jqGrid('getCell', rowId, 'quoteTypeID');
	var createdByID = grid.jqGrid('getCell', rowId, 'createdByID');
	var createdByName = grid.jqGrid('getCell', rowId, 'createdByName');
	var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
	var quoteRevision = grid.jqGrid('getCell', rowId, 'rev');
	var internalNote = grid.jqGrid('getCell', rowId, 'internalNote');
	var quoteRemarks = grid.jqGrid('getCell', rowId, 'quoteRemarks');

	aTypeDetail = quoteTypeID;
	aQuoteTypeName = quoteTypeDetail;
	aRevision = quoteRevision;
	var discountAmt = grid.jqGrid('getCell', rowId, 'discountAmount');
	$("#quoteDiscountedPrice").val(discountAmt);
	$("#jobQuoteSubmittedBYFullName").val(createdBYFullName);
	$("#jobQuoteSubmittedBYID").val(createdByID);
	$("#jobQuoteSubmittedBYInitials").val(createdByName);
	$("#jobQuoteRevision").val(quoteRevision);
	$("#jobQuoteInternalNote").val(internalNote);
	$("#quoteRemarksID").val(quoteRemarks);
	$("#joDetailID").val("");
	$("#quoteTypeDetail option[value=" + quoteTypeID + "]").attr("selected",
			true);
	
	saveQuoteDetail();
	
//	jQuery("#joQuoteheader").text(joQuoteHeaderID);
//	$("#joHeaderID").val(joQuoteHeaderID);
//	loadQuotesListDetails();
	        
	// Following statements Added by Jenith
//	saveQuoteDetail();
//	jQuery("#joQuoteheader").text(parseInt(joQuoteHeaderID)+1);
//	$("#joHeaderID").val(joQuoteHeaderID);
//	loadQuotesListDetails();
	
	// added by niaz for debug 201409011548
	// aGlobalVariable = 'edit';
	aGlobalVariable = 'copy';

	// jQuery("#addquotes").dialog("open");

	// if (quoteRevision != '') {

	// var newDialogDiv1 = jQuery(document.createElement('div'));
	// errorText = "Please change revision value.";
	// jQuery(newDialogDiv1).attr("id", "msgDlg");
	// jQuery(newDialogDiv1).html(
	// '<span><b style="color:red;">' + errorText + '</b></span>');
	// jQuery(newDialogDiv1).dialog({
	// modal : true,
	// width : 300,
	// height : 150,
	// title : "Warning",
	// buttons : [ {
	// height : 35,
	// text : "OK",
	// click : function() {
	// $(this).dialog("close");
	// }
	// } ]
	// }).dialog("open");
	// return false;
	// } else {
	// alert('inside revisison check 1910');
	// var newDialogDiv = jQuery(document.createElement('div'));
	// errorText = "Please Add revision value.";
	// jQuery(newDialogDiv1).attr("id", "msgDlg");
	// jQuery(newDialogDiv1).html(
	// '<span><b style="color:red;">' + errorText + '</b></span>');
	// jQuery(newDialogDiv1).dialog({
	// modal : true,
	// width : 300,
	// height : 150,
	// title : "Warning",
	// buttons : [ {
	// height : 35,
	// text : "OK",
	// click : function() {
	// $(this).dialog("close");
	// }}
	// ]
	// }).dialog("open");
	// return false;
	// }
}}*/

function deleteQuickQuoteDialog() {
	createtpusage('job-Quote Tab','Delete Quote','Info','Job-Quote Tab,Delete Quote,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId !== null) {

		var rowdata=grid.jqGrid('getRowData',rowId);
		var type=rowdata['quoteType'];
		var rev=rowdata['rev'];
		
		var gridData = jQuery("#quotesBidlist").getRowData();
		for (var index = 0; index < gridData.length; index++) {
			var rowData = gridData[index];
			if(type==rowData['quoteType'] && rev==rowData['rev']){
				var quoteemailstatus=rowData["quoteemailstatus"];
				var lastQuote=rowData['lastQuote'];
				if(lastQuote!="" && quoteemailstatus==1){
					jQuery(newDialogDiv).html('<span style="color:#FF0066;">The Quote has been sent already and you cannot Delete.</span><br><hr style="color: #cb842e;">');
					 jQuery(newDialogDiv).dialog({modal: true, title:"Alert",width:300, 
							buttons: [{height:30,text: "Yes",
								 keypress:function(e){
										var x = e.keyCode;
										if(x==13){
											$(newDialogDiv).dialog("close");
											return false;
										}
								    },
								click: function() {
								$(this).dialog("close");
								return false;
								}}
								
								]}).dialog("open");
					return false;
					break;
				}
			}
		}
		
	
		
		
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color: red;">Delete the Quote Record?</b></span>');
		jQuery(newDialogDiv).dialog(
				{
					modal : true,
					width : 300,
					height : 120,
					title : "Confirm Delete",
					buttons : {
						"Submit" : function() {
							var grid = $("#quotes");
							var rowId = grid.jqGrid('getGridParam', 'selrow');
							var joQuoteHeaderID = grid.jqGrid('getCell', rowId,
									'joQuoteHeaderID');
							aCancelQuote = '';
							deleteQuickQuote(joQuoteHeaderID, aCancelQuote);
							jQuery(this).dialog("close");
							$("#quotes").trigger("reloadGrid");
						},
						Cancel : function() {
							jQuery(this).dialog("close");
						}
					}
				}).dialog("open");
		return true;
	} else {
		var errorText = "Please click one of the Quote to Delete.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
}

function deleteQuickQuote(joQuoteHeaderID, aCancelQuote) {
	$.ajax({
		url : "./jobtabs2/deleteQuoteDetailID",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {
			deleteQuote(joQuoteHeaderID, aCancelQuote);
		}
	});
}
function deleteCopyQuote(joQuoteHeaderID, aCancelQuote) {

	$.ajax({
		url : "./jobtabs2/deleteQuickQuote",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {

			// $("#quotes").trigger("reloadGrid");
		}
	});

}
function deleteQuote(joQuoteHeaderID, aCancelQuote) {
	$
			.ajax({
				url : "./jobtabs2/deleteQuickQuote",
				mType : "GET",
				data : {
					"joHeaderQuoteID" : joQuoteHeaderID
				},
				success : function(data) {
					var errorText = '';
					if (aCancelQuote !== '') {
						errorText = "<b style='color:Green; align:right;'>Quote Cancelled</b>";
						$("#quoteAddMsg").css("display", "block");
						$('#quoteAddMsg').html(errorText);
						setTimeout(function() {
							$('#quoteAddMsg').html("");
						}, 2000);
						// $("#quotes").trigger("reloadGrid");
					} else {
						errorText = "<b style='color:Green; align:right;'>Quote deleted successfully.</b";
						$("#quoteAddMsg").css("display", "block");
						$('#quoteAddMsg').html(errorText);
						setTimeout(function() {
							$('#quoteAddMsg').html("");
						}, 2000);
						$("#quotes").jqGrid('GridUnload');
						loadQuotesGrid();
						$("#quotes").trigger("reloadGrid");
					}
				}
			});
}
function deleteBidDialog() {
    createtpusage('job-Quote Tab','Delete Bidder','Info','Job-Quote Tab,Delete Bidder,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var grid = $("#quotesBidlist");

	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId !== null) {
		jQuery(newDialogDiv)
				.html(
						'<span><b style="color: red;">Delete the Bid Record?</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 120,
			title : "Confirm Delete",
			buttons : {
				"Submit" : function() {
					var grid = $("#quotesBidlist");
					var rowId = grid.jqGrid('getGridParam', 'selrow');
					var joBidderId = grid.jqGrid('getCell', rowId, 'bidderId');
					deleteBidQuote(joBidderId);
					jQuery(this).dialog("close");
					var scrollPosition = jQuery("#quotesBidlist").closest(".ui-jqgrid-bdiv").scrollTop();
					bidScrollPosition=scrollPosition;
					aBidderDialogVar="delete";
					$("#quotesBidlist").trigger("reloadGrid",[{current:true}]);
					/*setTimeout(function() {
						scrollToRow ("#quotesBidlist", rowId,scrollPosition);
					}, 2000);*/
					
				//	$("#quotesBidlist").trigger("reloadGrid");
				},
				Cancel : function() {
					jQuery(this).dialog("close");
				}
			}
		}).dialog("open");
		return true;
	} else {
		var errorText = "Please click one of the Bidder to Delete.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
}

function deleteBidQuote(joBidderId) {
	$
			.ajax({
				url : "./jobtabs2/deleteQuotebidList",
				mType : "GET",
				data : {
					'selectBidderId' : joBidderId
				},
				success : function(data) {
					var errorText = "<b style='color:Green; align:right;'>Bid deleted successfully.</b>";
					$("#quoteBidMsg").css("display", "block");
					$('#quoteBidMsg').html(errorText);
					setTimeout(function() {
						$('#quoteBidMsg').html("");
					}, 2000);
					aBidderDialogVar="delete";
					$("#quotesBidlist").trigger("reloadGrid");
				
				}
			});
}

function openPDF(aQuotePDF) {
	var bidderGrid = $("#quotesBidlist");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	if (bidderGridRowId === null) {
		var errorText = "Please click one of the Bidder to Export PDF.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	if (jQuery("#quotes").getGridParam("records") === 0) {
		var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId,
				'quoteType');
		$('#loadingDiv').css({
			"visibility" : "hidden"
		});
		var errorText = "Please Add a Quote for '" + aQuoteType
				+ " Type' to save pdf.";
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Message",
			buttons : [ {
				height : 35,
				text : "OK",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aParagraph = 1;
	var aVendor = 1;
	if ($('#viewParagraphSaveAsPDFID').is(':checked')) {
		aParagraph = 0;
	}
	if ($('#viewManufactureSaveAsPDFID').is(':checked')) {
		aVendor = 0;
	}
	
	
	
	
	
	var aQuoteTo = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidder');
	var aBidderID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidderId');
	var aQuoteFull = aQuoteTo.replace("&", "and").replace("#", "_").replace(
			"#", "_").replace("#", "_").replace("#", "_").replace("#", "_")
			.replace("#", "_");
	var aQuoteFullName = aQuoteFull.replace("&", "and").replace("#", "_")
			.replace("#", "_").replace("#", "_").replace("#", "_").replace("#",
					"_").replace("#", "_");
	var aJoBidderContact = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'contact');
	var aJoBidderContactFullName = aJoBidderContact.replace("&", "and")
			.replace("#", "_").replace("#", "_").replace("#", "_").replace("#",
					"_").replace("#", "_").replace("#", "_");
	var aJoBidderQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteTypeID');
	var aJoBidderRev = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rev');
	var joMasterID = bidderGrid
			.jqGrid('getCell', bidderGridRowId, 'joMasterId');
	var joQuotePrice = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteAmount');
	var quotes_numberandtype=bidderGrid.jqGrid('getCell', bidderGridRowId,'quoteType');
	var name = $("#engineerHiddenID").val();
	var enggName = name.replace('&', 'and');
	var aEngineer = enggName.replace('&', 'and');
	var architectName = $("#architectHiddenID").val();
	var archName = architectName.replace('&', 'and');
	var aArchitect = archName.replace('&', 'and');
	var aBidDate = $("#bidDateHiddenID").val();
	var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
	var aPlanDate = $("#plan_date_format").val();
	var aThruAddendum = $("#quoteThru_id").val();
	var aLocationCity = $("#locationCityHiddenID").val();
	var aLocationState = $("#locationStateHiddenID").val();
	var currentDate = new Date();
	var currentMonth = currentDate.getMonth() + 1;
	var currenDate = currentMonth + "/" + currentDate.getDate() + "/"
			+ currentDate.getFullYear();
	var aJobNumber = $("#jobNumberHiddenID").val();
	
	
	var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId, 'quoteType');
	
	$.ajax({
		url : "./jobtabs2/checkQuoteType",
		mType : "GET",
		data : {
			'revision' : aJoBidderRev,
			'joBidderID' : aBidderID,
			'joMasterID' : joMasterID,
			'quoteTypeID' : aJoBidderQuoteType
		},
		success : function(data) {
			aJoBidderRev=data.quoteRev;
			if (data.joQuoteHeaderId === null) {
				errorText = "Please Add a Quote for '" + aQuoteType
						+ " Type' to View Pdf.";
				jQuery(newDialogDiv).html(
						'<span><b style="color:red;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Warning",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				return false;
			}else{
				if (aQuotePDF === 'QuotePDF') {
					$.ajax({
						type : "GET",
						//url : "./quotePDFController/SaveAsBidList",
						url : "./quotePDFController1/viewNewQuoteBidPdfForm",
						data : {
							'enginnerName' : aEngineer,
							'architectName' : aArchitect,
							'bidDate' : aBidDate,
							'projectName' : aProjectName,
							'planDate' : aPlanDate,
							'locationCity' : aLocationCity,
							'locationState' : aLocationState,
							'quoteTOName' : aQuoteFullName,
							'bidderContact' : aJoBidderContactFullName,
							'quoteTypeID' : aJoBidderQuoteType,
							'quoteRev' : aJoBidderRev,
							'joMasterID' : joMasterID,
							'QuoteThru' : aThruAddendum,
							'todayDate' : currenDate,
							'jobNumber' : aJobNumber,
							'paragraphCheck' : aParagraph,
							'manufactureCheck' : aVendor,
							'QuotePDF' : aQuotePDF,
							'quotes_numberandtype':quotes_numberandtype,
							'WriteorView':'write'
						},
						documenttype : "application\pdf",
						async : false,
						cache : false,
						success : function(msg) {
						},
						error : function(msg) {
						}
					});
				} else {
				window.open("./quotePDFController1/viewNewQuoteBidPdfForm?enginnerName="		
						+ aEngineer + "&architectName=" + aArchitect + "&bidDate="
						+ aBidDate + "&projectName=" + aProjectName + "&planDate="
						+ aPlanDate + "&locationCity=" + aLocationCity
						+ "&locationState=" + aLocationState + "&quoteTOName="
						+ aQuoteFullName + "&bidderContact=" + aJoBidderContactFullName
						+ "&quoteTypeID=" + aJoBidderQuoteType + "&quoteRev="
						+ aJoBidderRev + "&joMasterID=" + joMasterID + "&QuoteThru="
						+ aThruAddendum + "&todayDate=" + currenDate + "&jobNumber="
						+ aJobNumber + "&paragraphCheck=" + aParagraph
						+ "&manufactureCheck=" + aVendor + "&QuotePDF=" + aQuotePDF+"&quotes_numberandtype="+quotes_numberandtype);
				}
				
			}
		}
	});
	
	
	
	
	
//	if (aQuotePDF === 'QuotePDF') {
//		$.ajax({
//			type : "GET",
//			//url : "./quotePDFController/SaveAsBidList",
//			url : "./quotePDFController/viewNewQuoteBidPdfForm",
//			data : {
//				'enginnerName' : aEngineer,
//				'architectName' : aArchitect,
//				'bidDate' : aBidDate,
//				'projectName' : aProjectName,
//				'planDate' : aPlanDate,
//				'locationCity' : aLocationCity,
//				'locationState' : aLocationState,
//				'quoteTOName' : aQuoteFullName,
//				'bidderContact' : aJoBidderContactFullName,
//				'quoteTypeID' : aJoBidderQuoteType,
//				'quoteRev' : aJoBidderRev,
//				'joMasterID' : joMasterID,
//				'QuoteThru' : aThruAddendum,
//				'todayDate' : currenDate,
//				'jobNumber' : aJobNumber,
//				'paragraphCheck' : aParagraph,
//				'manufactureCheck' : aVendor,
//				'QuotePDF' : aQuotePDF,
//				'WriteorView':'write'
//			},
//			documenttype : "application\pdf",
//			async : false,
//			cache : false,
//			success : function(msg) {
//			},
//			error : function(msg) {
//			}
//		});
//	} else {
//		//window.open("./quotePDFController/SaveAsBidList?enginnerName="
		
	//}
	return true;
}

function viewPDF() {
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please click one of the Quote to View.";

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aParagraph = 1;
	var aVendor = 1;
	if ($('#viewParagraphID').is(':checked')) {
		aParagraph = 0;
	}
	if ($('#viewManufactureID').is(':checked')) {
		aVendor = 0;
	}
	var aJoBidderContact = '';
	var aQuoteTo = '';
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
	var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
	var joQuoteRev = grid.jqGrid('getCell', rowId, 'rev');
	var name = $("#engineerHiddenID").val();
	var enggName = name.replace('&', 'and');
	var aEngineer = enggName.replace('&', 'and');
	var architectName = $("#architectHiddenID").val();
	var archName = architectName.replace('&', 'and');
	var aArchitect = archName.replace('&', 'and');
	var aBidDate = $("#bidDateHiddenID").val();
	var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
	var aPlanDate = $("#plan_date_format").val();
	var aLocationCity = $("#locationCityHiddenID").val();
	var aLocationState = $("#locationStateHiddenID").val();
	var aThruAddendum = $("#quoteThru_id").val();
	var joQuotePrice = grid.jqGrid('getCell', rowId, 'quoteAmount');
	var joDiscountPrice = grid.jqGrid('getCell', rowId, 'discountAmount');
	var joRemarks = "";
	var aRemarks = "(" + joRemarks + ")";
	var aJobNumber = $("#jobNumberHiddenID").val();
	var currentDate = new Date();
	var currentMonth = currentDate.getMonth() + 1;
	var date = currentDate.getDate();
	if (currentMonth < 10)
		currentMonth = "0" + currentMonth.toString();
	if (date < 10) {
		date = "0" + date.toString();
	}
	var currenDate = currentMonth + "/" + date + "/"
			+ currentDate.getFullYear();
	window.open("./quotePDFController/viewQuotePdfForm?enginnerName="
			+ aEngineer + "&architectName=" + aArchitect + "&bidDate="
			+ aBidDate + " " + "&projectName=" + aProjectName + "&planDate="
			+ aPlanDate + "&locationCity=" + aLocationCity + "&locationState="
			+ aLocationState + "&quoteTOName=" + aQuoteTo + ""
			+ "&bidderContact=" + aJoBidderContact + "&joQuoteHeaderID="
			+ joQuoteHeaderID + "&totalPrice=" + joQuotePrice + "&quoteRev="
			+ joQuoteRev + "" + "&QuoteThru=" + aThruAddendum + "&todayDate="
			+ currenDate + "&jobNumber=" + aJobNumber + "&discountAmount="
			+ joDiscountPrice + "&quoteRemarks=" + aRemarks
			+ "&paragraphCheck=" + aParagraph + "&manufactureCheck=" + aVendor
			+ "&submittedBy=" + createdBYFullName);
	return true;
}

function sendEmail() {
	createtpusage('job-Quote Tab','Email Quote','Info','Job-Quote Tab,Email Quote,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var datass=getSysvariableStatusBasedOnVariableName('Musthaveaddendumquotethruandbiddatebeforesave');
	if(datass!=null && datass[0].valueLong==1){
		var received_id=$("#received_id").val();
		var quoteThru_id=$("#quoteThru_id").val();
		if(received_id==null || received_id==undefined || received_id=="" || received_id=="null" ||
		  quoteThru_id==null || quoteThru_id==undefined || quoteThru_id=="" || quoteThru_id=="null"){
			var txtlabel="User needs to fill out the Addendums";
			jQuery(newDialogDiv).html(
					'<span><b style="color:red;">'+txtlabel+'</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 300,
				height : 150,
				title : "Warning",
				buttons : [ {
					height : 35,
					text : "OK",
					 keypress:function(e){
							var x = e.keyCode;
							if(x==13){$(newDialogDiv).dialog("close");}
					    },
					click : function() {
						$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
		}
		
	}
	var bidderGrid = $("#quotesBidlist");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var errorText = '';

	if (bidderGridRowId === null) {
		errorText = "Please click one of the Bidder to Email Quote.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aContact = bidderGrid.jqGrid('getCell', bidderGridRowId, 'contact');
	var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'rxContactId');
	var aRevision = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rev');
	var aBidderID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidderId');
	var aJoMasterID = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'joMasterId');
	var aQuoteTypeID = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteTypeID');
	var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId, 'quoteType');
	if (aContactID === '') {
		errorText = "Please Add Contact for Bidder.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	} else {
		$.ajax({
			url : "./jobtabs2/checkQuoteType",
			mType : "GET",
			data : {
				'revision' : aRevision,
				'joBidderID' : aBidderID,
				'joMasterID' : aJoMasterID,
				'quoteTypeID' : aQuoteTypeID
			},
			success : function(data) {
				var bidRev = data.quoteRev;
				if (data.joQuoteHeaderId === null) {
					errorText = "Please Add a Quote for '" + aQuoteType
							+ " Type' to Send Mail.";
					jQuery(newDialogDiv).html(
							'<span><b style="color:red;">' + errorText
									+ '</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Warning",
						buttons : [ {
							height : 35,
							text : "OK",
							 keypress:function(e){
									var x = e.keyCode;
									if(x==13){$(newDialogDiv).dialog("close");}
							    },
							click : function() {
								$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					return false;
				} else {
					setQuoteemailpopupdetails(aContactID,bidRev);
					
				/*	var aEmail = bidderGrid.jqGrid('getCell', bidderGridRowId,
							'email');
					var aSubject = $("#jobNumberHiddenID").val()
							+ "  Quote for project:  "
							+ $("#projectNameHiddenID").val();
					var aEmailProperties = "emailName='"
							+ $("#emailNameHiddenID").val() + "'&emailAddr='"
							+ $("#emailAddrHiddenID").val() + "'&logOnName='"
							+ $("#logOnNameHiddenID").val()
							+ "'&logOnPassword='"
							+ $("#logOnPswdHiddenID").val() + "'&CCAddr1='"
							+ $("#ccaddr1HiddenID").val() + "'&CCAddr2='"
							+ $("#ccaddr2HiddenID").val() + "'&CCAddr3='"
							+ $("#ccaddr3HiddenID").val() + "'&CCAddr4='"
							+ $("#ccaddr4HiddenID").val() + "'&CCName1='"
							+ $("#ccname1HiddenID").val() + "'&CCName2='"
							+ $("#ccname2HiddenID").val() + "'&CCName3='"
							+ $("#ccname3HiddenID").val() + "'&CCName4='"
							+ $("#ccname4HiddenID").val() + "'&BCCAddr='"
							+ $("#bccaddrHiddenID").val() + "'&SMTPSer='"
							+ $("#smtpsvrHiddenID").val() + "'&SMTPPort='"
							+ $("#smtpportHiddenID").val() + "'&jobNumber='"
							+ $("#jobNumberHiddenID").val() + "'";
					if (aEmail === '' && bidRev !== '') {
						errorText = "Are you sure you want to send "
								+ aQuoteType + " 'Rev." + bidRev + "' to "
								+ aContact + "?";
					} else if (aEmail === '') {
						errorText = 'Are you sure you want to send '
								+ aQuoteType + ' to ' + aContact + '?';
					} else if (bidRev === '') {
						errorText = 'Are you sure you want to send '
								+ aQuoteType + ' to ' + aContact + ' ('
								+ aEmail + ')?';
					} else {
						errorText = "Are you sure you want to send "
								+ aQuoteType + " 'Rev." + bidRev + "' to "
								+ aContact + "(" + aEmail + ")?";
					}
					
					
					/*jQuery(newDialogDiv).html(
							'<span><b style="color:green;">HHHHH' + errorText
									+ '</b></span>');*/
					/*jQuery(newDialogDiv).dialog(
							{
								modal : true,
								width : 400,
								height : 150,
								title : "Message",
								buttons : {
									"Send" : function() {
										sentMailFunction(aEmail, aSubject,
												aContactID, aRevision,
												aBidderID, aEmailProperties,
												aJoMasterID, aQuoteTypeID);
										jQuery(this).dialog("close");
									},
									Cancel : function() {
										jQuery(this).dialog("close");
									}
								}
							}).dialog("open");
					return true;*/
				}
			}
		});
	}
	return true;
}

function sentMailFunction(aEmail, aSubject, aContactID, aRevision, aBidderID,
		aEmailProperties, aJoMasterID, aQuoteTypeID) {
	var aQuotePDF = "QuotePDF";

	var aMessage = "The attached quote is in PDF format.  If you have trouble reading it a free reader can be download at http://www.adobe.com/ \n\n";
	aMessage += "Thank you,\n";
	aMessage += "A & E Specialties.";
	var aAttach = "file:///var/quotePDF/Quotes.pdf";
	if (aEmail === '') {
		var errorText = "Please Provide a Mail ID.";
		jQuery(newDialogDiv)
				.html(
						'<form id="mailToAddress"><table><tr><td><span><b style="color:red;">'
								+ errorText
								+ '</b></span></td></tr>'
								+ '<tr><td style="height: 5px;"></td></tr>'
								+ '<tr><td><label>Mail ID: </label><input type="text" id="mailToAddress_ID" name="mailToAddress_name" class="validate[custom[email]]" style="width: 250px;" /></td></tr></table></form><hr>');
		jQuery(newDialogDiv)
				.dialog(
						{
							modal : true,
							width : 430,
							height : 180,
							title : "Message",
							buttons : {
								"Submit & Send" : function() {
									if (!$('#mailToAddress').validationEngine(
											'validate')) {
										return false;
									}
									var aEmailAddress = $("#mailToAddress_ID")
											.val();
									saveMailAddress(aEmailAddress, aContactID);
									$('#loadingDiv').css({
										"visibility" : "visible"
									});
									setTimeout(
											function() {
												if (jQuery("#quotes")
														.getGridParam("records") === 0) {
													$('#loadingDiv').css({
														"visibility" : "hidden"
													});
													var errorText = "Please add a Quote to send mail.";
													jQuery(newDialogDiv)
															.html(
																	'<span><b style="color:green;">'
																			+ errorText
																			+ '</b></span>');
													jQuery(newDialogDiv)
															.dialog(
																	{
																		modal : true,
																		width : 300,
																		height : 150,
																		title : "Message",
																		buttons : [ {
																			height : 35,
																			text : "OK",
																			 keypress:function(e){
																					var x = e.keyCode;
																					if(x==13){$(newDialogDiv).dialog("close");}
																			    },
																			click : function() {
																				$(
																						this)
																						.dialog(
																								"close");
																			}
																		} ]
																	}).dialog(
																	"open");
													return false;
												} else {
													openPDF(aQuotePDF);
													/*
													 * location.href =
													 * 'mailto:'+aEmail+'?from=thulasi.ram@sysvine.com&cc='+$("#ccaddr1HiddenID").val()+','+$("#ccaddr2HiddenID").val()+','+$("#ccaddr3HiddenID").val()+','+$("#ccaddr4HiddenID").val()+'&bcc='+$("#bccaddrHiddenID").val()+
													 * '&subject='+$("#jobNumberHiddenID").val()+'
													 * Quote for project:
													 * '+$("#projectNameHiddenID").val() +
													 * '&body='+encodeURIComponent(aMessage)+'&attachment=@'+aAttach+'';
													 * $('#loadingDiv').css({"visibility":
													 * "hidden"});
													 */
													sendMail(aSubject,
															aEmailAddress,
															aRevision,
															aBidderID,
															aEmailProperties,
															aJoMasterID,
															aQuoteTypeID);
													return true;
												}
											}, 800);
									jQuery(this).dialog("close");
									return true;
								},
								Cancel : function() {
									jQuery(this).dialog("close");
								}
							}
						}).dialog("open");
	} else {
		$('#loadingDiv').css({
			"visibility" : "visible"
		});
		setTimeout(function() {
			if (jQuery("#quotes").getGridParam("records") === 0) {
				$('#loadingDiv').css({
					"visibility" : "hidden"
				});
				var errorText = "Please add a Quote to send mail.";
				jQuery(newDialogDiv).html(
						'<span><b style="color:green;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Message",
					buttons : [ {
						height : 35,
						text : "OK",
						 keypress:function(e){
								var x = e.keyCode;
								if(x==13){$(newDialogDiv).dialog("close");}
						    },
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				return false;
			} else {
				openPDF(aQuotePDF);
				/*
				 * location.href =
				 * 'mailto:'+aEmail+'?from=thulasi.ram@sysvine.com&cc='+$("#ccaddr1HiddenID").val()+','+$("#ccaddr2HiddenID").val()+','+$("#ccaddr3HiddenID").val()+','+$("#ccaddr4HiddenID").val()+'&bcc='+$("#bccaddrHiddenID").val()+
				 * '&subject='+$("#jobNumberHiddenID").val()+' Quote for
				 * project: '+$("#projectNameHiddenID").val() +
				 * '&body='+encodeURIComponent(aMessage)+'&attachment=@'+aAttach+'';
				 * $('#loadingDiv').css({"visibility": "hidden"});
				 */
				sendMail(aSubject, aEmail, aRevision, aBidderID,
						aEmailProperties, aJoMasterID, aQuoteTypeID);
				return true;
			}
		}, 800);
		return true;
	}
	return true;
}

function sendMail(theSubject, theContectName, theRevision, theBidderID,
		theEmailProperties, theJoMasterID, theQuoteTypeID) {
	var aSendMailProperties = theEmailProperties + "&subject='" + theSubject
			+ "'&toAddress='" + theContectName + "'";
	$
			.ajax({
				url : "./sendMailServer/sendMail",
				type : "POST",
				data : aSendMailProperties,
				success : function(data) {
					$('#loadingDiv').css({
						"visibility" : "hidden"
					});
					var validateMsg = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
					$("#quoteBidMsg").css("display", "block");
					$('#quoteBidMsg').html(validateMsg);
					setTimeout(function() {
						$('#quoteBidMsg').html("");
					}, 2000);
					/*
					 * jQuery(newDialogDiv).dialog( { modal : true, width : 300,
					 * height : 150, title : "Message", buttons : [ { height :
					 * 35, text : "OK", click : function() {
					 */
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth() + 1;
					var yyyy = today.getFullYear().toString().substr(2, 2);
					var hours = today.getHours();
					var minutes = today.getMinutes();
					var ampm = hours >= 12 ? 'PM' : 'AM';
					hours = hours % 12;
					hours = hours ? hours : 12;
					if (dd < 10) {
						dd = '0' + dd;
					}
					if (mm < 10) {
						mm = '0' + mm;
					}
					if (hours < 10) {
						hours = '0' + hours;
					}
					if (minutes < 10) {
						minutes = '0' + minutes;
					}
					today = mm + '/' + dd + '/' + yyyy + " " + hours + ":"
							+ minutes + " " + ampm;
					$.ajax({
						url : "./jobtabs2/updateLastQuoteAndRev",
						mType : "GET",
						data : {
							'revision' : theRevision,
							'joBidderID' : theBidderID,
							'joMasterID' : theJoMasterID,
							'quoteTypeID' : theQuoteTypeID,
							'quoteDate' : today
						},
						success : function(data) {
							$("#quotesBidlist").jqGrid('GridUnload');
							loadBidListGrid();
							$("#quotesBidlist").trigger("reloadGrid");
						}
					});
					$("#quotesBidlist").jqGrid('GridUnload');
					loadBidListGrid();
					$("#quotesBidlist").trigger("reloadGrid");
					$(this).dialog("close");
					/*
					 * } } ] }).dialog("open");
					 */
				}
			});
}

function saveMailAddress(aEmailAddress, aContactID) {
	$.ajax({
		url : "./jobtabs2/updateEmailAddress",
		mType : "GET",
		data : {
			'email' : aEmailAddress,
			'contactID' : aContactID
		},
		success : function(data) {

		}
	});
}

function faxQuote() {

	var bidderGrid = $("#quotesBidlist");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	if (bidderGridRowId === null) {
		var errorText = "Please click one of the Bidder to Fax Quote.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	
	
	var bidderGrid = $("#quotesBidlist");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	var aContact = bidderGrid.jqGrid('getCell', bidderGridRowId, 'contact');
	var aContactID = bidderGrid.jqGrid('getCell', bidderGridRowId,'rxContactId');
	var theRevision = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rev');
	var theBidderID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidderId');
	var theJoMasterID = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'joMasterId');
	var theQuoteTypeID = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteTypeID');
	var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId, 'quoteType');
	var arxMasterId=bidderGrid.jqGrid('getCell', bidderGridRowId, 'rxMasterId');
	var jobnumber=$("#jobNumber_ID").text();
	var theContactID = $("#contacthiddenID").val();
	
			var emailstatus='Printed';
//			var validateMsg = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
//			$("#quoteBidMsg").css("display", "block");
//			$('#quoteBidMsg').html(validateMsg);
//			setTimeout(function() {
//				$('#quoteBidMsg').html("");
//			}, 2000);
			var today = new Date();
			var dd = today.getDate();
			var mm = today.getMonth() + 1;
			var yyyy = today.getFullYear().toString().substr(2, 2);
			var hours = today.getHours();
			var minutes = today.getMinutes();
			var ampm = hours >= 12 ? 'PM' : 'AM';
			hours = hours % 12;
			hours = hours ? hours : 12;
			if (dd < 10) {
				dd = '0' + dd;
			}
			if (mm < 10) {
				mm = '0' + mm;
			}
			if (hours < 10) {
				hours = '0' + hours;
			}
			if (minutes < 10) {
				minutes = '0' + minutes;
			}
			today = mm + '/' + dd + '/' + yyyy + " " + hours + ":"
					+ minutes + " " + ampm;

			
			updateEmailQuoteHistory(theRevision,theBidderID,theJoMasterID,theQuoteTypeID,today,aContactID,arxMasterId,emailstatus);

			$.ajax({
				url : "./jobtabs2/updateLastQuoteAndRev",
				mType : "GET",
				async: "false",
				data : {
					'revision' : theRevision,
					'joBidderID' : theBidderID,
					'joMasterID' : theJoMasterID,
					'quoteTypeID' : theQuoteTypeID,
					'quoteDate' : today,
					'status' :emailstatus
				},
				success : function(data) {
					//$("#quotesBidlist").jqGrid('GridUnload');
					//loadBidListGrid();
					$("#quotesBidlist").trigger("reloadGrid");
					$('#loadingDivForPO').css({
						"visibility" : "hidden","display": "none"
					}); 
				},error:function(e){
					 $('#loadingDivForPO').css({
							"visibility" : "hidden","display": "none"
						}); 
					}
			});
			//$("#quotesBidlist").jqGrid('GridUnload');
			//loadBidListGrid();
			$("#quotesBidlist").trigger("reloadGrid");
}

/*function saveQuoteDetail() {

	var revision = $('#jobQuoteRevision').val();
	// commented by Niaz 20140902
	// if (revision != '') {
	//		
	// // revision = Number(revision) + 1;
	// revision = Number(revision);
	// } else {
	// revision = 0;
	// }
	$('#jobQuoteRevision').val(revision);
	// alert(revision);
	var joQuoteHeaderID = 0;
	var quoteInfo = $("#quoteManipulateForm").serialize();
	var gridData = jQuery("#addquotesList").getRowData();
	var totalcost = 0;
	var totalPrice = 0;
	var gridquotes = $("#quotes");
	var rowIdquotes = gridquotes.jqGrid('getGridParam', 'selrow');

	for (var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var cost = rowData["cost"].replace(/[^-0-9\.]+/g, "");
		cost = cost.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^-0-9\.]+/g, "");
		price = price.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		totalPrice = totalPrice + Number(price);
	}
	if (aGlobalVariable === 'copy') {
		totalPrice = gridquotes.jqGrid('getCell', rowIdquotes, 'quoteAmount');
		totalPrice = totalPrice.replace(/[^-0-9\.]+/g, "").replace(".00", "");
		totalcost = gridquotes.jqGrid('getCell', rowIdquotes, 'costAmount');
		totalcost = totalcost.replace(/[^-0-9\.]+/g, "").replace(".00", "");
	}
	var disCountPrice = $("#quoteDiscountedPrice").val();
	var disAmt = disCountPrice.replace(/[^-0-9\.]+/g, "");
	disAmt = Number(disAmt);
	if (joQuoteHeaderID === false) {
		joQuoteHeaderID = "";
	}
	var data1 = '';
	var id = jQuery("#quotesBidlist").jqGrid('getGridParam', 'selrow');
	var rxContactId = jQuery("#quotesBidlist").getCell(id, 12);
	var bidderId = jQuery("#quotesBidlist").getCell(id, 1);
	if (bidderId == false || bidderId == '') {
		bidderId = 0;
	}
	if (rxContactId == '') {
		rxContactId = 0;
	}

	$
			.ajax({
				url : "./jobtabs2/SaveQuoteDetailInfo",
				type : "POST",
				async : false,
				data : quoteInfo + "&jobNumber=" + $("#jobNumber_ID").text()
						+ "&token=" + aGlobalVariable + "&totalcost="
						+ totalcost + "&totalPrice=" + totalPrice
						+ "&joHeaderQuoteID=" + joQuoteHeaderID
						+ "&quoteRemarks=" + $("#quoteRemarksID").val()
						+ "&quoteDiscountedPrice=" + disAmt + "&rxContactID="
						+ rxContactId + "&bidderId=" + bidderId
						+ "&jobQuoteInternalNote="
						+ $('#jobQuoteInternalNote').val(),
				success : function(data) {
					var copydata = data;
					
					if (aGlobalVariable === 'copy') {
						$("#headerIDForCopyQuote").val(data);
						var previousJoQuoteHeader = jQuery("#joQuoteheader")
								.text();
						var currentJoQuoteheaderID = $("#headerIDForCopyQuote")
								.val();
						$
								.ajax({
									url : "./jobtabs2/getLineItemsForCopyQuote",
									type : "POST",
									data : {
										'previousJoQuoteHeader' : previousJoQuoteHeader,
										'currentJoQuoteheaderID' : currentJoQuoteheaderID
									},
									success : function(data) {
//										 alert("copydata="+copydata+"::::joQuoteheader="+jQuery("#joQuoteheader")
//													.text()+":::currentJoQuoteheaderID="+currentJoQuoteheaderID);
										 $("#joQuoteheader").text(currentJoQuoteheaderID);
										 iscopyQuickQuoteClick = true;
										 
										 * $("#addquotesList").jqGrid('GridUnload');
										 * 
										 * loadQuotesListDetails();
										 * $('#addquotesList').jqGrid('setGridParam', {
										 * postData : { 'joQuoteHeaderID' :
										 * currentJoQuoteheaderID } });
										 * $("#addquotesList").trigger("reloadGrid");
										 
										//Added by Niaz 2014-09-05
										
										
										

										$("#quotes").jqGrid('GridUnload');

										loadQuotesGrid();
										// alert('aGlobalVariable='+aGlobalVariable);
										if (aGlobalVariable != 'copy') {
											$("#quotes").trigger("reloadGrid");
										}
										var errorText = "<b style='color:Green; align:right;'>Quote Copied Successfully</b>";
										$("#quoteAddMsg").css("display",
												"block");
										// $('#quoteAddMsg').html(errorText);

										setTimeout(
												function() {
													$('#quoteAddMsg').html("");
													try {
														// added by Niaz
														// 2014-09-02 for Copy
														// quote open in dialog
														
														 * alert('copydata=' +
														 * copydata); // var
														 * selrowcopy =
														 * jQuery('#quotes
														 * tr:eq('+copydata+')').attr('joQuoteHeaderID');
														 * var rowIds =
														 * $('#quotes').jqGrid('getDataIDs');
														 * 
														 * for (i = 1; i <=
														 * rowIds.length; i++) {
														 * var rowData =
														 * $(this).jqGrid('getRowData',
														 * i);
														 * 
														 * if
														 * (rowData['joQuoteHeaderID'] ==
														 * copydata ) {
														 * alert('selrowcopy=' +
														 * i);
														 * onDoubleClickEditQuoteDetails(selrowcopy); }
														 * //if } //for //
														 * onDoubleClickEditQuoteDetails(2);
														 
														console.log('a1');
														var ids = jQuery(
																"#quotes")
																.jqGrid(
																		'getDataIDs');
														console.log('a1');
														for (var i = 0; i < ids.length; i++) {
															console.log('a1::'
																	+ i);
															var rowId = ids[i];
															var rowData = jQuery(
																	'#quotes')
																	.jqGrid(
																			'getRowData',
																			rowId);
															if (copydata == rowData.joQuoteHeaderID) {
																console
																		.log("b2::"
																				+ rowId);
																onDoubleClickEditQuoteDetails(rowId);
																aGlobalVariable = 'copy';
																return;
															}
														}

													} catch (err) {
														alert('error' + err);
													}
												}, 300);

									}
								});
						data1 = data;
						$("#joHeaderID").val(data1);
						return true;
					}
				}
			});
	return data1;
}*/

function copyEachLineItem() {
	var joQuoteheaderID = $("#headerIDForCopyQuote").val();
	var gridData = jQuery("#addquotesList").getRowData();
	var totalcost = 0;
	var totalPrice = 0;
	for (var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var product = rowData["product"];
		var quantity = rowData["itemQuantity"];
		var manufacturerID = rowData["rxManufacturerID"];
		var paragraph = rowData["paragraph"];
		var factoryID = rowData["veFactoryId"];
		var inlineNote = rowData["inlineNote"];
		var footerNote = rowData["productNote"];
		var cost = rowData["cost"].replace(/[^-0-9\.]+/g, "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^-0-9\.]+/g, "");
		totalPrice = totalPrice + Number(price);
		var addQuoteValues = "product=" + product + "&paragraph=" + paragraph
				+ "&rxManufacturerID=" + manufacturerID + "&itemQuantity="
				+ quantity + "&inlineNote=" + inlineNote + "&footnote="
				+ footerNote + "&cost=" + cost + "&oper=add&joQuoteHeaderID="
				+ joQuoteheaderID + "&quoteheaderid=" + joQuoteheaderID
				+ "&veFactoryId=" + factoryID + "&price=" + price;
		$.ajax({
			url : "./jobtabs2/manpulaterProductQuotes",
			type : "POST",
			data : addQuoteValues,
			success : function(data) {
				$("#joQuoteheader").text(joQuoteheaderID);
				$("#addquotesList").jqGrid('GridUnload');
				loadQuotesListDetails();
				$('#addquotesList').jqGrid('setGridParam', {
					postData : {
						'joQuoteHeaderID' : joQuoteheaderID
					}
				});
				$("#addquotesList").trigger("reloadGrid");
			}
		});
	}
}

function saveQuote() {
	console.log('calling::saveQuote::');
	var joQuoteHeaderID = $("#joHeaderID").val();
	var quoteInfo = $("#quoteManipulateForm").serialize();
	var gridData = jQuery("#addquotesList").getRowData();
	var typeDetailId = $("#quoteTypeDetailID").val();
	var totalcost = 0;
	var totalPrice = 0;
	for (var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var cost = rowData["cost"].replace(/[^-0-9\.]+/g, "").replace(".00", "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^-0-9\.]+/g, "").replace(".00",
				"");
		totalPrice = Number(totalPrice) + Number(price);
	}

	var disCountPrice = $("#quoteDiscountedPrice").val();
	var disAmt = disCountPrice.replace(/[^-0-9\.]+/g, "");
	disAmt = Number(disAmt);
	var data1 = '';
	$.ajax({
		url : "./jobtabs2/SaveQuoteInfo",
		type : "POST",
		async : false,
		data : quoteInfo + "&jobNumber=" + $("#jobNumber_ID").text()
				+ "&token=" + aGlobalVariable + "&totalcost=" + totalcost
				+ "&totalPrice=" + totalPrice + "&joHeaderQuoteID="
				+ joQuoteHeaderID + "&quoteRemarks="
				+ $("#quoteRemarksID").val() + "&quoteDiscountedPrice="
				+ disAmt+"&joMasterID="+$("#joMaster_ID").text(),
		success : function(data) {

			if (aGlobalVariable === 'copy') {
				var joQuoteheaderID = data;
				var gridData = jQuery("#addquotesList").getRowData();
				for (var index = 0; index < gridData.length; index++) {
					var rowData = gridData[index];
					var joQuoteDetailId = rowData["joQuoteDetailID"];
					var product = rowData["product"];
					var quantity = rowData["itemQuantity"];
					var manufacturerID = rowData["rxManufacturerID"];
					var paragraph = rowData["paragraph"];
					var factoryID = rowData["veFactoryId"];
					var inlineNote = rowData["inlineNote"];
					var footerNote = rowData["productNote"];
					var cost = rowData["cost"].replace(/[^-0-9\.]+/g, "");
					totalcost = totalcost + Number(cost);
					var price = rowData["price"].replace(/[^-0-9\.]+/g, "");
					var percentage = rowData["percentage"];
					// totalPrice = totalPrice + Number(price);
					var addQuoteValues = "product=" + product
							+ "&joQuoteDetailID=" + joQuoteDetailId
							+ "&paragraph=" + paragraph + "&rxManufacturerID="
							+ manufacturerID + "&itemQuantity=" + quantity
							+ "&inlineNote=" + inlineNote + "&footnote="
							+ footerNote + "&cost=" + cost
							+ "&oper=add&joQuoteHeaderID=" + joQuoteheaderID
							+ "&quoteheaderid=" + joQuoteheaderID
							+ "&veFactoryId=" + factoryID + "&price=" + price
							+ "&percentage=" + percentage;
					$.ajax({
						url : "./jobtabs2/manpulaProductQuotes",
						type : "POST",
						data : addQuoteValues,
						success : function(data) {
							$("#joQuoteheader").text(joQuoteHeaderIdentity);
							$("#addquotesList").jqGrid('GridUnload');
							loadQuotesListDetails();
							$('#addquotesList').jqGrid('setGridParam', {
								postData : {
									'joQuoteHeaderID' : joQuoteheaderID
								}
							});
							$("#addquotesList").trigger("reloadGrid");
							if (Number(joQuoteheaderID) === 0) {
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
				}
				data1 = data;
				$("#quotes").trigger("reloadGrid");
				if (aTypeDetail !== typeDetailId) {
					errorText = "Type value Changed And Quote details copied";
					var validateMsg = "<b style='color:Green; align:right;'>"
							+ errorText + "</b>";
					$("#quoteAddMsg").css("display", "block");
					$('#quoteAddMsg').html(validateMsg);
					setTimeout(function() {
						$('#quoteAddMsg').html("");
					}, 2000);
				} else {
					errorText = "Quote details copied";
					var validateMsg = "<b style='color:Green; align:right;'>"
							+ errorText + "</b>";
					$("#quoteAddMsg").css("display", "block");
					$('#quoteAddMsg').html(validateMsg);
					setTimeout(function() {
						$('#quoteAddMsg').html("");
					}, 2000);
				}
				$("#joHeaderID").val(data1);
				return true;
			}
		}
	});
	return data1;
}

function deleteQuoteNoRev(joQuoteHeaderID, aCancelQuote) {
	$.ajax({
		url : "./jobtabs2/deleteQuoteDetailID",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {
			deleteQuick(joQuoteHeaderID, aCancelQuote);
		}
	});
}

function deleteQuick(joQuoteHeaderID, aCancelQuote) {
	$.ajax({
		url : "./jobtabs2/deleteQuickQuote",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {
			$("#quotes").trigger("reloadGrid");
		}
	});
}

function deleteDetailFrom(aQuoteDetailID) {
	var joQuoteID = aQuoteDetailID;
	$.ajax({
		url : "./jobtabs2/deleteQuickQuoteDetail",
		mType : "GET",
		data : {
			'joHeaderQuoteDetailID' : joQuoteID
		},
		success : function(data) {
		}
	});
	return true;
}

function saveJobAmount() {
	
	 createtpusage('job-Quote Tab','Save Amount','Info','Job-Quote Tab,Saving Amount,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	
	var aContractAmount = $("#contractAmount").val();
	var aEstimatedCost = $("#estimatedCost").val();
	var aEstimatedProfit = $("#estimatedProfit").val();
	var aContractAmt = aContractAmount.replace(/[^-0-9\.]+/g, "");
	aContractAmt = Number(aContractAmt);
	var aEstimateCot = aEstimatedCost.replace(/[^-0-9\.]+/g, "");
	aEstimateCot = Number(aEstimateCot);
	var aEstimatePro = aEstimatedProfit.replace(/[^-0-9\.]+/g, "");
	aEstimatePro = Number(aEstimatePro);
	var aEstimatedProfi = aContractAmt - aEstimateCot;
	var ajoMasterId = $("#joMasterHiddenID").val();
	$
			.ajax({
				url : "./jobtabs2/updateJobamount",
				mType : "GET",
				data : {
					'joMasterID' : ajoMasterId,
					'ContractAmount' : aContractAmt,
					'EstimateCost' : aEstimateCot,
					'EstimateProfit' : aEstimatedProfi
				},
				success : function(data) {
					$("#estimatedCost").val(formatCurrency(aEstimateCot));
					$("#contractAmount").val(formatCurrency(aContractAmt));
					$("#estimatedProfit").val(formatCurrency(aEstimatedProfi));
					$("#estimatedCostlabel").text(formatCurrency(aEstimateCot));
					var validateMsg = "<b style='color:Green; align:right;'>Amount is updated</b>";
					$("#addAmountMsg").css("display", "block");
					$('#addAmountMsg').html(validateMsg);
						var taxRate = Number($('#TaxValue').text().replace(/[^-0-9\.]+/g, ""));
					var estTax = ((aContractAmt*taxRate)/100);
					var closeOut = Number(aContractAmt)+Number(estTax);
					$('#billingReminder').text(formatCurrency(aContractAmt));
					$('#estimatedTax').text(formatCurrency(estTax));
					$('#closeout').text(formatCurrency(closeOut));
					setTimeout(function() {
						$('#addAmountMsg').html("");
					}, 2000);
				}
			});
}

function trimContractAmount() {
	var aContractAmount = $("#contractAmount").val();
	var aContractAmt = aContractAmount.replace(/[^-0-9\.]+/g, "");
	aContractAmt = parseFloat(aContractAmt).toFixed(2);
	$("#contractAmount").val(aContractAmt);
}

function trimEstimatedCost() {
	var aEstimatedCost = $("#estimatedCost").val();
	var aEstimatedCst = aEstimatedCost.replace(/[^-0-9\.]+/g, "");
	aEstimatedCst = parseFloat(aEstimatedCst).toFixed(2);
	$("#estimatedCost").val(aEstimatedCst);
}

function estimateProfit() {
	var aContractAmount = $("#contractAmount").val();
	var aEstimatedCost = $("#estimatedCost").val();
	var aContractAmt = aContractAmount.replace(/[^-0-9\.]+/g, "");
	aContractAmt = parseFloat(aContractAmt).toFixed(2);
	var aEstimateCot = aEstimatedCost.replace(/[^-0-9\.]+/g, "");
	aEstimateCot = parseFloat(aEstimateCot).toFixed(2);
	var aEstimatedProfit = aContractAmt - aEstimateCot;
	$("#estimatedProfit").val("");
	$("#estimatedProfit").val(parseFloat(aEstimatedProfit).toFixed(2));
}

function cancelReport() {
	jQuery("#searchCriteria").dialog("close");
}

jQuery(function() {
	$(".datepicker").datepicker();
	jQuery("#searchCriteria").dialog({
		autoOpen : false,
		width : 486,
		title : "Report Criteria",
		modal : true,
		buttons : {},
		close : function() {
			return true;
		}
	});
	return true;
});

function reportCriteria() {
	jQuery("#searchCriteria").dialog("open");
}

function reportCriteriaPage() {
	document.location.href = "./reportcriteria";
}

function contractAmountAdminValidation(aContractAmount) {
	if (jobStatus === "Booked") {
		/*if (aUserAdmin !== '1') {
			jQuery(newDialogDiv)
					.html(
							'<span style="color:#FF0066;">You are not authorized to modify this field.  Admin only can change.</span>');
			jQuery(newDialogDiv)
					.dialog(
							{
								modal : true,
								title : "Booked Job Alert",
								width : 400,
								buttons : [ {
									height : 30,
									text : "OK",
									 keypress:function(e){
											var x = e.keyCode;
											if(x==13){$(newDialogDiv).dialog("close");
											$("#contractAmount").val(formatCurrency($("#jobContractAmountHidden").val()));
									document.getElementById('contractAmount').disabled = true;
											}
									    },
									click : function() {
										$(this).dialog("close");
										$("#contractAmount")
												.val(
														formatCurrency($(
																"#jobContractAmountHidden")
																.val()));
										document
												.getElementById('contractAmount').disabled = true;
									}
								} ]
							}).dialog("open");
		} else {*/
			var aContractAmount = $("#contractAmount").val();
			var aEstimatedCost = $("#estimatedCost").val();
			var aEstimatedProfit = $("#estimatedProfit").val();
			var aContractAmt = aContractAmount.replace(/[^-0-9\.]+/g, "");
			console.log("Before"+aContractAmt);
			aContractAmt = parseFloat(aContractAmt).toFixed(2);
			console.log("After"+aContractAmt);
			var aEstimateCot = aEstimatedCost.replace(/[^-0-9\.]+/g, "");
			aEstimateCot = parseFloat(aEstimateCot).toFixed(2);
			var aEstimatePro = aEstimatedProfit.replace(/[^-0-9\.]+/g, "");
			aEstimatePro = parseFloat(aEstimatePro).toFixed(2);
			var aEstimatedProfi = aContractAmt - aEstimateCot;
			$("#estimatedCost").val(formatCurrency(aEstimateCot));
			$("#contractAmount").val(aContractAmt);
			$("#estimatedProfit").val(formatCurrency(aEstimatedProfi));

			return aContractAmount;
		//}
	}
}

function estimateCostAdminValidation(aEstimateAmount) {
	if (jobStatus === "Booked") {
		/*if (aUserAdmin !== '1') {
			jQuery(newDialogDiv)
					.html(
							'<span style="color:#FF0066;">You are not authorized to modify this field.  Admin only can change.</span>');
			jQuery(newDialogDiv)
					.dialog(
							{
								modal : true,
								title : "Booked Job Alert",
								width : 400,
								buttons : [ {
									height : 30,
									text : "OK",
									 keypress:function(e){
											var x = e.keyCode;
											if(x==13){
												$(newDialogDiv).dialog("close");
												$("#estimatedCost").val(formatCurrency($("#estomattedHiddenIDHidden").val()));
												document.getElementById('estimatedCost').disabled = true;
											}
									    },
									click : function() {
										$(this).dialog("close");
										$("#estimatedCost")
												.val(
														formatCurrency($(
																"#estomattedHiddenIDHidden")
																.val()));
										document
												.getElementById('estimatedCost').disabled = true;
									}
								} ]
							}).dialog("open");
		} else {*/
			var aContractAmount = $("#contractAmount").val();
			var aEstimatedCost = $("#estimatedCost").val();
			var aEstimatedProfit = $("#estimatedProfit").val();
			var aContractAmt = aContractAmount.replace(/[^-0-9\.]+/g, "");
			aContractAmt = Number(aContractAmt);
			var aEstimateCot = aEstimatedCost.replace(/[^-0-9\.]+/g, "");
			aEstimateCot = Number(aEstimateCot);
			var aEstimatePro = aEstimatedProfit.replace(/[^-0-9\.]+/g, "");
			aEstimatePro = Number(aEstimatePro);
			var aEstimatedProfi = aContractAmt - aEstimateCot;
			$("#estimatedCost").val(aEstimateCot);
			$("#contractAmount").val(formatCurrency(aContractAmt));
			$("#estimatedProfit").val(formatCurrency(aEstimatedProfi));

			return aEstimateAmount;
		}
//	}
}

/** Method to set the joQuoteTemplate Properties in grid */
function setQuoteTemplateProperties() {
	$.ajax({
		url : "./jobtabs2/getquoteTempproperties",
		type : "GET",
		success : function(data) {
			console.log(data);
			if (data.displayQuantity === 1) {
				$("#quantityDisplaytempID").attr("checked", true);
				$("#quantityLabeltempID").empty();
				$("#quantityLabeltempID").append("(Yes)");
			}
			if (data.printQuantity === 1) {
				$("#quantityPrinttempID").attr("checked", true);
				$("#quantityLabelPrinttempID").empty();
				$("#quantityLabelPrinttempID").append("(Yes)");
			}
			if (data.displayParagraph === 1) {
				$("#quantityDisplayParatempID").attr("checked", true);
				$("#paraLabeltempID").empty();
				$("#paraLabeltempID").append("(Yes)");
			}
			if (data.printParagraph === 1) {
				$("#quantityprintParatempID").attr("checked", true);
				$("#paraLabelPrinttempID").empty();
				$("#paraLabelPrinttempID").append("(Yes)");
			}
			if (data.displayManufacturer === 1) {
				$("#quantityDisplayManuftempID").attr("checked", true);
				$("#manufatuereLabeltempID").empty();
				$("#manufatuereLabeltempID").append("(Yes)");
			}
			if (data.printManufacturer === 1) {
				$("#quantityPrintManuftempID").attr("checked", true);
				$("#manufatuereLabelPrinttempID").empty();
				$("#manufatuereLabelPrinttempID").append("(Yes)");
			}
			if (data.displaySpec === 1) {
				$("#quantityDisplaySpectempID").attr("checked", true);
				$("#specLabeltempID").empty();
				$("#specLabeltempID").append("(Yes)");
			}
			if (data.printSpec === 1) {
				$("#quantityprintSpectempID").attr("checked", true);
				$("#specLabelPrinttempID").empty();
				$("#specLabelPrinttempID").append("(Yes)");
			}
			if (data.displayCost === 1) {
				$("#quantityDisplayCosttempID").attr("checked", true);
				$("#costLabeltempID").empty();
				$("#costLabeltempID").append("(Yes)");
			}
			if (data.printCost === 1) {
//				$("#quantityprintCosttempID").attr("checked", true);
//				$("#costLabelPrinttempID").empty();
//				$("#costLabelPrinttempID").append("(Yes)");
			}

			if (data.displayMult === 1) {
				$("#quantityDisplayMultitempID").attr("checked", true);
				$("#multiLabeltempID").empty();
				$("#multiLabeltempID").append("(Yes)");
			}
			if (data.printMult === 1) {
				$("#quantityprintMultitempID").attr("checked", true);
				$("#multiLabelPrinttempID").empty();
				$("#multiLabelPrinttempID").append("(Yes)");
			}
			if (data.displayPrice === 1) {
				$("#quantityDisplayPricetempID").attr("checked", true);
				$("#priceLabeltempID").empty();
				$("#priceLabeltempID").append("(Yes)");
			}
			if (data.printPrice === 1) {
				$("#quantityprintPricetempID").attr("checked", true);
				$("#priceLabelPrinttempID").empty();
				$("#priceLabelPrinttempID").append("(Yes)");
			}
			if (data.underlineCost === 1) {
				$("#quantityUnderlineCosttempID").attr("checked", true);
				$("#costLabelUnderlinetempID").empty();
				$("#costLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldCost === 1) {
				$("#quantityBoldCosttempID").attr("checked", true);
				$("#costLabelBoldtempID").empty();
				$("#costLabelBoldtempID").append("(Yes)");
			}
			if (data.underlineManufacturer === 1) {
				$("#quantityUnderlineManuftempID").prop("checked", true);
				$("#manufatuereLabelUnderlinetempID").empty();
				$("#manufatuereLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldManufacturer === 1) {
				$("#quantityBoldManuftempID").attr("checked", true);
				$("#manufatuereLabelBoldtempID").empty();
				$("#manufatuereLabelBoldtempID").append("(Yes)");
			}
			if (data.underlineMult === 1) {
				$("#quantityUnderlineMultitempID").attr("checked", true);
				$("#multiLabelUnderlinetempID").empty();
				$("#multiLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldMult === 1) {
				$("#quantityBoldMultitempID").attr("checked", true);
				$("#multiLabelBoldtempID").empty();
				$("#multiLabelBoldtempID").append("(Yes)");
			}
			if (data.underlineParagraph === 1) {
				$("#quantityUnderlineParatempID").attr("checked", true);
				$("#paraLabelUnderlinetempID").empty();
				$("#paraLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldParagraph === 1) {
				$("#quantityBoldParatempID").prop("checked", true);
				$("#paraLabelBoldtempID").empty();
				$("#paraLabelBoldtempID").append("(Yes)");
			}
			if (data.underlinePrice === 1) {
				$("#quantityUnderlinePricetempID").attr("checked", true);
				$("#priceLabelUnderlinetempID").empty();
				$("#priceLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldPrice === 1) {
				$("#quantityBoldPricetempID").attr("checked", true);
				$("#priceLabelBoldtempID").empty();
				$("#priceLabelBoldtempID").append("(Yes)");
			}
			if (data.underlineQuantity === 1) {
				$("#quantityUnderlinetempID").attr("checked", true);
				$("#quantityLabelUnderlinetempID").empty();
				$("#quantityLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldQuantity === 1) {
				$("#quantityBoldtempID").attr("checked", true);
				$("#quantityLabelBoldtempID").empty();
				$("#quantityLabelBoldtempID").append("(Yes)");
			}
			if (data.underlineSpec === 1) {
				$("#quantityUnderlineSpectempID").attr("checked", true);
				$("#specLabelUnderlinetempID").empty();
				$("#specLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldSpec === 1) {
				$("#quantityBoldSpectempID").attr("checked", true);
				$("#specLabelBoldtempID").empty();
				$("#specLabelBoldtempID").append("(Yes)");
			}

			if (data.displayItem === 1) {
				$("#quantityDisplayItemtempID").attr("checked", true);
				$("#ItemLabelDisplaytempID").empty();
				$("#ItemLabelDisplaytempID").append("(Yes)");
			}
			if (data.printItem === 1) {
				$("#quantityPrintItemtempID").attr("checked", true);
				$("#ItemLabelPrinttempID").empty();
				$("#ItemLabelPrinttempID").append("(Yes)");
			}
			if (data.underlineItem === 1) {
				$("#quantityUnderlineItemtempID").attr("checked", true);
				$("#ItemLabelUnderlinetempID").empty();
				$("#ItemLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldItem === 1) {
				$("#quantityBoldItemtempID").attr("checked", true);
				$("#ItemLabelboldtempID").empty();
				$("#ItemLabelboldtempID").append("(Yes)");
			}
			if (data.displayHeader === 1) {
				$("#quantityDisplayHeadertempID").attr("checked", true);
				$("#headerLabelDisplaytempID").empty();
				$("#headerLabelDisplaytempID").append("(Yes)");
			}
			if (data.printHeader === 1) {
				$("#quantityPrintHeadertempID").attr("checked", true);
				$("#headerLabelPrinttempID").empty();
				$("#headerLabelPrinttempID").append("(Yes)");
			}
			if (data.underlineHeader === 1) {
				$("#quantityUnderlineHeadertempID").attr("checked", true);
				$("#headerLabelUnderlinetempID").empty();
				$("#headerLabelUnderlinetempID").append("(Yes)");
			}
			if (data.boldHeader === 1) {
				$("#quantityBoldHeadertempID").attr("checked", true);
				$("#headerLabelboldtempID").empty();
				$("#headerLabelboldtempID").append("(Yes)");
			}

			if (data.notesFullWidth === 1) {
				$("#inlineNoteFullPagetempID").attr("checked", true);
			}
			if (data.lineNumbers === 1) {
				$("#PrintLineNumtempID").attr("checked", true);
			}
			if (data.printTotal === 1) {
				$("#printSummationtempID").attr("checked", true);
			}
			if (data.hidePrice === 1) {
				$("#hidePricetempID").attr("checked", true);
			}
			jQuery("#quotePropertiesTempDialog").dialog("open");
		},
		error : function(data) {

		}
	});
	$('#quotePropertiesTempDialog').dialog("open");
}

/** Method to save the joQuoteTemplate Properties */
function saveQuoteTemplateProperties() {
	var aProperties = $("#quoteTempPropertiesFormID").serialize();
	console.log("Quote Template Temp--->" + aProperties);
	var aQuoteProperties = aProperties + "&isQuoteTempProp=true";
	console.log(aQuoteProperties);
	$.ajax({
		url : "./jobtabs2/updateQuoteProperties",
		type : "POST",
		data : aQuoteProperties,
		async : false,
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
	$('#quotePropertiesTempDialog').dialog("close");
	
	loadjoquotecolumn();
	return true;
}
function PropertyTemplateImage(cellValue, options, rowObject) {
	   var lineitemid=rowObject['joQuoteTemplateDetailId'];
		var element = "<div><a onclick='editQuoteTemplateLineItemFrom("+lineitemid+")'><img src='./../resources/images/QuoteProperties.png' title='Properties' align='middle' style='padding: 2px 5px;'></a></div>";
		return element;
	}
/**
 * The following method sends back the users property as an array to load the
 * grid
 */
function addQuotetemplateLineItemGridColums(joQuoteHeaderID) {
	$.ajax({
		url : "./jobtabs2/getquoteTempproperties",
		type : "GET",
		success : function(data) {
			dynamicColNames = [ '','','','','','','','', '','' ]
			defaultColNames = [ 'QuoteDetailID', 'ManufacturerID',
					'QuoteHeaderID', 'FactoryID', 'LineItem', 'FootLine',
					'Inlinenote','position' ];
			var defaultColmodels = [ {
				name : 'joQuoteTemplateDetailId',
				index : 'joQuoteTemplateDetailId',
				align : 'left',
				width : 90,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {}
			}, {
				name : 'rxManufacturerId',
				index : 'rxManufacturerId',
				align : 'left',
				width : 90,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {}
			}, {
				name : 'joQuoteTemplateHeaderId',
				index : 'joQuoteTemplateHeaderId',
				align : 'left',
				width : 90,
				editable : true,
				hidden : true,
				edittype : 'text',
				editoptions : {
					size : 30
				},
				editrules : {}
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
				editrules : {}
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
				editrules : {}
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
				editrules : {}
			},
			
			{
				name : 'edit',
				index : 'edit',
				align : 'center',
				width : 50,
				hidden : false,
				editable : false,
				editrules : {
					edithidden : false
				},
				formatter : imgFormatterTemp
			},
			{
				name : 'position',
				index : 'position',
				align : 'left',
				width : 90,
				editable : false,
				hidden : true,
				edittype : 'text'
			}
			
			];
			var dynamicColModels = [
			     {
				name : 'inLineNoteImage',
				index : 'inLineNoteImage',
				align : 'right',
				width : 15,
				editable : false,
				hidden : false,
				formatter : PropertyTemplateImage
				},{
				name : 'inLineNoteImage',
				index : 'inLineNoteImage',
				align : 'right',
				width : 10,
				editable : false,
				hidden : false,
				formatter : inlineImage
				},
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
					},
					{
						name : 'position',
						index : 'position',
						align : 'left',
						width : 90,
						editable : false,
						hidden : true
					}
				 ];
			var checkquotetempdisp=false;
			
			
			for(var k=1;k<10;k++){
			if(tempprodlbl==col1columnname && col1ordnum==k){	
			if(data.displayItem===1){
				dynamicColNames.push(col1lablel);
				dynamicColModels.push(quotetempproduct);
				checkquotetempdisp=true;
			}else{
				dynamicColNames.push(col1lablel);
				dynamicColModels.push(quotetemphiddenproduct);
			}
			}
			if(tempqtylbl==col2columnname && col2ordnum==k){
			if (data.displayQuantity === 1) {
				dynamicColNames.push(col2lablel);
				dynamicColModels.push(quotetempQuantity);
				checkquotetempdisp=true;
			} else {
				dynamicColNames.push(col2lablel);
				dynamicColModels.push(quotetemphiddenQuantity);
			}
			}
			if(tempparalbl==col3columnname && col3ordnum==k){
			if (data.displayParagraph === 1) {
				dynamicColNames.push(col3lablel);
				dynamicColModels.push(quotetemppara);
				checkquotetempdisp=true;
			}else{
				dynamicColNames.push(col3lablel);
				dynamicColModels.push(quotetemphiddenpara);
			}
			}
			if(tempmanlbl==col4columnname && col4ordnum==k){
			if (data.displayManufacturer === 1) {
				dynamicColNames.push(col4lablel);
				dynamicColModels.push(quotetempmanufac);
				checkquotetempdisp=true;
			}else{
				dynamicColNames.push(col4lablel);
				dynamicColModels.push(quotetemphiddenmanufac);
				
			}
			}
			if(tempspeclbl==col5columnname && col5ordnum==k){
			if (data.displaySpec === 1) {
				dynamicColNames.push(col5lablel);
				dynamicColModels.push(quotetempspec);
				checkquotetempdisp=true;
			}else{
				dynamicColNames.push(col5lablel);
				dynamicColModels.push(quotetemphiddenspec);
			}
			}
			if(tempcostlbl==col6columnname && col6ordnum==k){
			if (data.displayCost === 1) {
				dynamicColNames.push(col6lablel);
				dynamicColModels.push(quotetempcost);
				checkquotetempdisp=true;
			} else {
				dynamicColNames.push(col6lablel);
				dynamicColModels.push(quotetemphiddencost);
			}
			}
			if(tempmultlbl==col7columnname && col7ordnum==k){
			if (data.displayMult === 1) {
				dynamicColNames.push(col7lablel);
				dynamicColModels.push(quotetempmulti);
				checkquotetempdisp=true;
			} else {
				dynamicColNames.push(col7lablel);
				dynamicColModels.push(quotetemphiddenmulti);
			}
			}
			if(temppricelbl==col8columnname && col8ordnum==k){
			if (data.displayPrice === 1) {
				dynamicColNames.push(col8lablel);
				dynamicColModels.push(quotetempprice);
				checkquotetempdisp=true;
			} else {
				dynamicColNames.push(col8lablel);
				dynamicColModels.push(quotetemphiddenprice);
			}
			}
			}
			if(checkquotetempdisp){
			for (var i = 0; i < defaultColNames.length; i++) {
				dynamicColNames.push(defaultColNames[i]);
				dynamicColModels.push(defaultColmodels[i]);
			}
			}else{
		        var myGrid = $('#quoteTemplateProductsGrid');
		        myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[1].name);
		        myGrid.jqGrid('hideCol', myGrid.getGridParam("colModel")[2].name);
			}
		
		
			loadTemplateLineItemDetails(joQuoteHeaderID, dynamicColNames,
					dynamicColModels);
			
		},
		error : function(data) {
			console.log(data);
		}
	});
}


/*Created By: Velmurugan
 *Created On: 26-9-2014 
 * Description:Pdf Export-sorting,bold,underline,headerbold,headerunderline
 * 
 */
function viewQuotePDF() {
	 createtpusage('job-Quote Tab','Quote PDF','Info','Job-Quote Tab,Quote PDF,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please click one of the Quote to View.";

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aParagraph = 1;
	var aVendor = 1;
	if ($('#viewParagraphID').is(':checked')) {
		aParagraph = 0;
	}
	if ($('#viewManufactureID').is(':checked')) {
		aVendor = 0;
	}
	var aJoBidderContact = '';
	var aQuoteTo = $("#jobHeader_JobCustomerName_id").val();
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
	var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
	var quotes_numberandtype=grid.jqGrid('getCell', rowId, 'quoteType');
	var joQuoteRev = grid.jqGrid('getCell', rowId, 'rev');
	var name = $("#engineerHiddenID").val();
	var enggName = name.replace('&', 'and');
	var aEngineer = enggName.replace('&', 'and');
	var architectName = $("#architectHiddenID").val();
	var archName = architectName.replace('&', 'and');
	var aArchitect = archName.replace('&', 'and');
	var aBidDate = $("#bidDateHiddenID").val();
	var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
	var aPlanDate = $("#plan_date_format").val();
	var aLocationCity = $("#locationCityHiddenID").val();
	var aLocationState = $("#locationStateHiddenID").val();
	var aThruAddendum = $("#quoteThru_id").val();
	var joQuotePrice = grid.jqGrid('getCell', rowId, 'quoteAmount');
	var joDiscountPrice = grid.jqGrid('getCell', rowId, 'discountAmount');
	var joRemarks = "";
	var aRemarks = "(" + joRemarks + ")";
	var aJobNumber = $("#jobNumberHiddenID").val();
	var currentDate = new Date();
	var currentMonth = currentDate.getMonth() + 1;
	var date = currentDate.getDate();
	if (currentMonth < 10)
		currentMonth = "0" + currentMonth.toString();
	if (date < 10) {
		date = "0" + date.toString();
	}
	if(aQuoteTo==undefined){
		aQuoteTo="";	
	}
	var currenDate = currentMonth + "/" + date + "/"
			+ currentDate.getFullYear();
	/*window.open("./quotePDFController/viewnewQuotePdfForm?enginnerName="*/
	  window.open("./quotePDFController1/viewoldQuotePdfForm?enginnerName="
			+ encodeBigurl(aEngineer) + "&architectName=" + encodeBigurl(aArchitect) + "&bidDate="
			+ aBidDate + " " + "&projectName=" + encodeBigurl(aProjectName) + "&planDate="
			+ aPlanDate + "&locationCity=" + aLocationCity + "&locationState="
			+ aLocationState + "&quoteTOName=" + encodeBigurl(aQuoteTo) + ""
			+ "&bidderContact=" + encodeBigurl(aJoBidderContact) + "&joQuoteHeaderID="
			+ joQuoteHeaderID + "&totalPrice=" + joQuotePrice + "&quoteRev="
			+ joQuoteRev + "" + "&QuoteThru=" + encodeBigurl(aThruAddendum) + "&todayDate="
			+ currenDate + "&jobNumber=" + aJobNumber + "&discountAmount="
			+ joDiscountPrice + "&quoteRemarks=" + aRemarks
			+ "&paragraphCheck=" + aParagraph + "&manufactureCheck=" + aVendor
			+ "&submittedBy=" + encodeBigurl(createdBYFullName)+"&quotes_numberandtype="+quotes_numberandtype);
	return true;
}
function viewQuotePDFsample() {
	var grid = $("#quotes");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId === null) {
		var errorText = "Please click one of the Quote to View.";

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aParagraph = 1;
	var aVendor = 1;
	if ($('#viewParagraphID').is(':checked')) {
		aParagraph = 0;
	}
	if ($('#viewManufactureID').is(':checked')) {
		aVendor = 0;
	}
	var aJoBidderContact = '';
	var aQuoteTo = '';
	var joQuoteHeaderID = grid.jqGrid('getCell', rowId, 'joQuoteHeaderID');
	var createdBYFullName = grid.jqGrid('getCell', rowId, 'createdBYFullName');
	var joQuoteRev = grid.jqGrid('getCell', rowId, 'rev');
	var name = $("#engineerHiddenID").val();
	var enggName = name.replace('&', 'and');
	var aEngineer = enggName.replace('&', 'and');
	var architectName = $("#architectHiddenID").val();
	var archName = architectName.replace('&', 'and');
	var aArchitect = archName.replace('&', 'and');
	var aBidDate = $("#bidDateHiddenID").val();
	var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
	var aPlanDate = $("#plan_date_format").val();
	var aLocationCity = $("#locationCityHiddenID").val();
	var aLocationState = $("#locationStateHiddenID").val();
	var aThruAddendum = $("#quoteThru_id").val();
	var joQuotePrice = grid.jqGrid('getCell', rowId, 'quoteAmount');
	var joDiscountPrice = grid.jqGrid('getCell', rowId, 'discountAmount');
	var joRemarks = "";
	var aRemarks = "(" + joRemarks + ")";
	var aJobNumber = $("#jobNumberHiddenID").val();
	var currentDate = new Date();
	var currentMonth = currentDate.getMonth() + 1;
	var date = currentDate.getDate();
	if (currentMonth < 10)
		currentMonth = "0" + currentMonth.toString();
	if (date < 10) {
		date = "0" + date.toString();
	}
	var currenDate = currentMonth + "/" + date + "/"
			+ currentDate.getFullYear();
	/*window.open("./quotePDFController/viewnewQuotePdfForm?enginnerName="*/
	  window.open("./quotePDFController/viewsampleQuotePdfForm?enginnerName="
			+ aEngineer + "&architectName=" + aArchitect + "&bidDate="
			+ aBidDate + " " + "&projectName=" + aProjectName + "&planDate="
			+ aPlanDate + "&locationCity=" + aLocationCity + "&locationState="
			+ aLocationState + "&quoteTOName=" + aQuoteTo + ""
			+ "&bidderContact=" + aJoBidderContact + "&joQuoteHeaderID="
			+ joQuoteHeaderID + "&totalPrice=" + joQuotePrice + "&quoteRev="
			+ joQuoteRev + "" + "&QuoteThru=" + aThruAddendum + "&todayDate="
			+ currenDate + "&jobNumber=" + aJobNumber + "&discountAmount="
			+ joDiscountPrice + "&quoteRemarks=" + aRemarks
			+ "&paragraphCheck=" + aParagraph + "&manufactureCheck=" + aVendor
			+ "&submittedBy=" + createdBYFullName);
	return true;
}



function addFootNote(rowId) {

	aAddandEdit = "edit";
	$("#inlneNoteHideShow").css("display", "none");
	$("#displayProduct").hide();
	$("#displayQuantity").hide();
	$("#displayParagraph").hide();
	$("#displayVendor").hide();
	$("#displayCost").hide();
	$("#displayMargin").hide();
	$("#displayMsg").hide();
	$("#displayPrice").hide();
	$("#importFactory").css("display", "none");
	$("#buildFromStock").css("display", "none");
	$("#customBuild").css("display", "none");
	var grid = $("#addquotesList");
	var rowIds = grid.jqGrid('getGridParam', 'selrow');
	rowIds = rowId;
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	if (gVar != '') {

		var errorText = "Please Save line item";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;

	}
	// var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var joQuoteDetailId = $("#addquotesList").jqGrid('getCell', rowId,
			'joQuoteDetailID');
	if (joQuoteDetailId == false) {

		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">Please Save LineItem</b></span>');
		errorText = "Please Save LineItem and add Footer Notes.";
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}

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
	var costCovert = cost.replace(/[^-0-9\.]+/g, "");
	var priceCovert = price.replace(/[^-0-9\.]+/g, "");
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
		title : "Foot Note",
		height : 200
	});
	jQuery("#addButtonDiv").dialog("open");
	return true;
}

function imgFormatterTemp(cellValue, options, rowObject) {
	var id = options.rowId;
	var notevalue = rowObject['productNote'];
	var footnoteimage = "<img  onclick='addFootNoteTemplate("
			+ id
			+ ")' src='./../resources/images/footNote.png' title='Delete Line Items' align='middle' style='padding: 2px 5px;'>";
	if (notevalue == "" || notevalue == undefined || notevalue == null) {
		footnoteimage = "<img   onclick='addFootNoteTemplate("
				+ id
				+ ")' src='./../resources/images/footnoteinfo.png' title='Edit Line Items' align='middle' style='padding: 2px 5px;'>";
	}
	var element = "<div>"
			+ footnoteimage
			+ "</a>"
			+ "<a onclick='deleteQuoteFromTemplate("
			+ id
			+ ")'><img src='./../resources/images/delete.png' title='Delete Line Items' align='middle' style='padding: 2px 5px;'></a>"
			+ "<a onclick='addOpenLineItemDialogTemplate("
			+ id
			+ ")'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px 5px;'></a></div>";
	return element;
}


function addOpenLineItemDialogTemplate(rowId) {

	var rowId = rowId;// $("#addquotesList").jqGrid('getGridParam', 'selrow');

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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}

	if (gVar != '') {

		var errorText = "Please Save line item";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;

	}
	var joQuoteDetailId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,
			'joQuoteTemplateDetailId');
	if (joQuoteDetailId == false) {
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">Please Save LineItem</b></span>');
		errorText = "Please Save LineItem and add Footer Notes.";
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
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
	var lineItemText = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,
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
	}).panelInstance('lineItemNoteId');
	$(".nicEdit-main").empty();
	$(".nicEdit-main").append(JopInlinetext);
	jQuery("#addInLineItemNote").dialog("open");
	$(".nicEdit-main").focus();
	return true;
}

jQuery(function() {
	jQuery("#addInLineItemNote").dialog({
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

function saveLineItemNoteInfo() {
	var inlineText = $('#addInLineItemNoteID').find('.nicEdit-main').html(); // nicEditors.findEditor('lineItemId').getContent(); 
	var rowId = $("#quoteTemplateProductsGrid").jqGrid('getGridParam', 'selrow');
	var joQuoteTemplateDetailId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,
			'joQuoteTemplateDetailId');
	var joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,
			'joQuoteTemplateHeaderId');
	var aLineItem = "joQuoteTemplateDetailId="+joQuoteTemplateDetailId+"&joQuoteTemplateHeaderId="+joQuoteTemplateHeaderId+"&inlineNote="+inlineText;
	/*aLineItem.push(inlineText);
	aLineItem.push(joQuoteHeaderID);
	aLineItem.push(quoteDetailID);*/
	
	$.ajax({
		url : "./jobtabs2/updateTemplateInlineNote",
		type : "POST",
		data : {
			'joQuoteTemplateDetailId' : joQuoteTemplateDetailId,'joQuoteTemplateHeaderId':joQuoteTemplateHeaderId,'inlineNote':inlineText
		},
		success : function(data) {
			area.removeInstance('lineItemNoteId');
			jQuery("#addInLineItemNote").dialog("close");
			$("#addquotesList").jqGrid('GridUnload');
			loadQuotesListDetails();
			$("#quoteTemplateProductsGrid").trigger("reloadGrid");
			$.each(data, function(index, value) {
			});
		}
	});
}

function cancelLintItemNote() {
	area.removeInstance('lineItemNoteId');
	jQuery("#addInLineItemNote").dialog("close");
	return false;
}

function deleteQuoteFromTemplate(rowid) {

	var grid = $("#quoteTemplateProductsGrid");
	var newDialogDiv = jQuery(document.createElement('div'));
	var rowId = rowid;// grid.jqGrid('getGridParam', 'selrow');
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
							var rowId = $("#quoteTemplateProductsGrid").jqGrid(
									'getGridParam', 'selrow');
							var joQuoteID = $("#quoteTemplateProductsGrid").jqGrid(
									'getCell', rowId, 'joQuoteTemplateDetailId');
							var joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,
							'joQuoteTemplateHeaderId');
							$.ajax({
								url : "./jobtabs2/deleteTemplateLineItem",
								mType : "GET",
								data : {
									'joQuoteTemplateDetailId' : joQuoteID, 'joQuoteTemplateHeaderId':joQuoteTemplateHeaderId,'oper':'delete'
								},
								success : function(data) {
									$("#quoteTemplateProductsGrid").trigger("reloadGrid");
									$.each(data, function(index, value) {
									});
								}
							});
							// deleteQuote(joQuoteID);
							jQuery(this).dialog("close");
							$("#quoteTemplateProductsGrid").trigger("reloadGrid");
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	return true;
}

function addFootNoteTemplate(rowId) {
	console.log('Product Note:1 '+$("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'productNote'));
	var product_note = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'productNote');
	$("#productNote").val(product_note);
	aAddandEdit = "edit";
	$("#inlneNoteHideShow").css("display", "none");
	$("#displayProduct").hide();
	$("#displayQuantity").hide();
	$("#displayParagraph").hide();
	$("#displayVendor").hide();
	$("#displayCost").hide();
	$("#displayMargin").hide();
	$("#displayMsg").hide();
	$("#displayPrice").hide();
	$("#importFactory").css("display", "none");
	$("#buildFromStock").css("display", "none");
	$("#customBuild").css("display", "none");

	$("#saveQuoteTempNote").css("display", "block");
	$("#cancelQuoteTempNote").css("display", "block");
	$("#saveQuoteNote").css("display", "none");
	$("#cancelQuoteNote").css("display", "none");
	
	var grid = $("#quoteTemplateProductsGrid");
	var rowIds = grid.jqGrid('getGridParam', 'selrow');
	rowIds = rowId;
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	if (gVar != '') {

		var errorText = "Please Save line item";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;

	}
	// var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
	var joQuoteDetailId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'joQuoteTemplateDetailId');
	var joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'joQuoteTemplateHeaderId');
	 $("#quoteTemplateHeaderID").val(joQuoteTemplateHeaderId);
	 $("#joQuoteLineTemplateHeaderID").val(joQuoteDetailId);
	if (joQuoteDetailId == false) {

		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).attr("id", "msgDlg");
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">Please Save LineItem</b></span>');
		errorText = "Please Save LineItem and add Footer Notes.";
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Warning",
			buttons : [ {
				height : 35,
				text : "OK",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	
	
	$("#addButtonDiv").dialog({
		title : "Foot Note",
		height : 200
	});
	jQuery("#addButtonDiv").dialog("open");
	return true;
}

function cancelQuoteTempNavigator() {
	$('#addButtonForm').validationEngine('hideAll');
	jQuery("#addButtonDiv").dialog("close");
	return true;
}

function saveQuoteTempNavigator() {
	var grid = $("#quoteTemplateProductsGrid");
	var rowId = grid.jqGrid('getGridParam', 'selrow');
	
	var joQuoteDetailId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'joQuoteTemplateDetailId');
	var joQuoteTemplateHeaderId = $("#quoteTemplateProductsGrid").jqGrid('getCell', rowId,'joQuoteTemplateHeaderId');

	var newDialogDiv = jQuery(document.createElement('div'));
	if ($("#joQuoteLineTemplateHeaderID").val() === '-1') {
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	} 
		
	var addQuoteValues = "joQuoteTemplateDetailId="+ $("#joQuoteLineTemplateHeaderID").val()+"&joQuoteTemplateHeaderId="+ $("#quoteTemplateHeaderID").val()+"&productNote="+$("#productNote").val();
	$.ajax({
		url : "./jobtabs2/updateTemplateProductNote",
		type : "POST",
		data : addQuoteValues,
		success : function(data) {
			$("#quoteTemplateProductsGrid").trigger("reloadGrid");
			/*
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
		*/
			
			$('#addButtonForm').validationEngine('hideAll');
			jQuery("#addButtonDiv").dialog("close");	
		}
	});
	return true;
}
$(function() {
	var cache = {};
	var lastXhr = '';
$("#Manufacturer_TextBox").autocomplete(
		{
			minLength : 1,
			select : function(event, ui) {
				var id = ui.item.id;
				var name = ui.item.value;
				$("#manufacturertextboxid").val(id);
				/*$.ajax({
					url : "./jobtabs2/getFactoryID",
					type : "GET",
					data : {
						'rxMasterID' : id,
						'descripition' : name
					},
					success : function(data) {
						$("#veFactoryId").val(data);
					}
				});*/
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


$(function() {
	var cache = {};
	var lastXhr = '';
$("#Manufacturer_TextBox_template").autocomplete(
		{
			minLength : 1,
			select : function(event, ui) {
				var id = ui.item.id;
				var name = ui.item.value;
				$("#manufacturertextboxid_template").val(id);
				/*$.ajax({
					url : "./jobtabs2/getFactoryID",
					type : "GET",
					data : {
						'rxMasterID' : id,
						'descripition' : name
					},
					success : function(data) {
						$("#veFactoryId").val(data);
					}
				});*/
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
function emailwriteQuotespdf(){
	var returnvalue=false;
	var bidderGrid = $("#quotesBidlist");
	var bidderGridRowId = bidderGrid.jqGrid('getGridParam', 'selrow');
	if (bidderGridRowId === null) {
		var errorText = "Please click one of the Bidder to Export PDF.";
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
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	if (jQuery("#quotes").getGridParam("records") === 0) {
		var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId,
				'quoteType');
		$('#loadingDiv').css({
			"visibility" : "hidden"
		});
		var errorText = "Please Add a Quote for '" + aQuoteType
				+ " Type' to save pdf.";
		jQuery(newDialogDiv).html(
				'<span><b style="color:red;">' + errorText + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Message",
			buttons : [ {
				height : 35,
				text : "OK",
				 keypress:function(e){
						var x = e.keyCode;
						if(x==13){$(newDialogDiv).dialog("close");}
				    },
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	
	var aParagraph = 1;
	var aVendor = 1;
	if ($('#viewParagraphSaveAsPDFID').is(':checked')) {
		aParagraph = 0;
	}
	if ($('#viewManufactureSaveAsPDFID').is(':checked')) {
		aVendor = 0;
	}
	
	
	
	
	
	var aQuoteTo = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidder');
	var aBidderID = bidderGrid.jqGrid('getCell', bidderGridRowId, 'bidderId');
	var aQuoteFull = aQuoteTo.replace("&", "and").replace("#", "_").replace(
			"#", "_").replace("#", "_").replace("#", "_").replace("#", "_")
			.replace("#", "_");
	var aQuoteFullName = aQuoteFull.replace("&", "and").replace("#", "_")
			.replace("#", "_").replace("#", "_").replace("#", "_").replace("#",
					"_").replace("#", "_");
	var aJoBidderContact = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'contact');
	var aJoBidderContactFullName = aJoBidderContact.replace("&", "and")
			.replace("#", "_").replace("#", "_").replace("#", "_").replace("#",
					"_").replace("#", "_").replace("#", "_");
	var aJoBidderQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteTypeID');
	var aJoBidderRev = bidderGrid.jqGrid('getCell', bidderGridRowId, 'rev');
	var joMasterID = bidderGrid
			.jqGrid('getCell', bidderGridRowId, 'joMasterId');
	var joQuotePrice = bidderGrid.jqGrid('getCell', bidderGridRowId,
			'quoteAmount');
	var name = $("#engineerHiddenID").val();
	var enggName = name.replace('&', 'and');
	var aEngineer = enggName.replace('&', 'and');
	var architectName = $("#architectHiddenID").val();
	var archName = architectName.replace('&', 'and');
	var aArchitect = archName.replace('&', 'and');
	var aBidDate = $("#bidDateHiddenID").val();
	var aProjectName = $("#projectNameHiddenID").val().replace('&', '`and`');
	var aPlanDate = $("#plan_date_format").val();
	var aThruAddendum = $("#quoteThru_id").val();
	var aLocationCity = $("#locationCityHiddenID").val();
	var aLocationState = $("#locationStateHiddenID").val();
	var currentDate = new Date();
	var currentMonth = currentDate.getMonth() + 1;
	var currenDate = currentMonth + "/" + currentDate.getDate() + "/"
			+ currentDate.getFullYear();
	var aJobNumber = $("#jobNumberHiddenID").val();
	
	var aQuoteType = bidderGrid.jqGrid('getCell', bidderGridRowId, 'quoteType');
	
	$.ajax({
		url : "./jobtabs2/checkQuoteType",
		mType : "GET",
		async:false,
		data : {
			'revision' : aJoBidderRev,
			'joBidderID' : aBidderID,
			'joMasterID' : joMasterID,
			'quoteTypeID' : aJoBidderQuoteType
		},
		success : function(data) {
			aJoBidderRev=data.quoteRev; 
							$.ajax({
								type : "GET",
								//url : "./quotePDFController/SaveAsBidList",
								url : "./quotePDFController1/viewNewQuoteBidPdfForm",
								async:false,
								data : {
									'enginnerName' : aEngineer,
									'architectName' : aArchitect,
									'bidDate' : aBidDate,
									'projectName' : aProjectName,
									'planDate' : aPlanDate,
									'locationCity' : aLocationCity,
									'locationState' : aLocationState,
									'quoteTOName' : aQuoteFullName,
									'bidderContact' : aJoBidderContactFullName,
									'quoteTypeID' : aJoBidderQuoteType,
									'quoteRev' : aJoBidderRev,
									'joMasterID' : joMasterID,
									'QuoteThru' : aThruAddendum,
									'todayDate' : currenDate,
									'jobNumber' : aJobNumber,
									'paragraphCheck' : aParagraph,
									'manufactureCheck' : aVendor,
									'QuotePDF' : 'QuotePDF',
									'quotes_numberandtype':aQuoteType,
									'WriteorView':'write'
								},
								documenttype : "application\pdf",
								async : false,
								cache : false,
								success : function(msg) {
									returnvalue=true;	
								},
								error : function(msg) {
								}
							});
		}
	});
	
	return returnvalue;
}

function ValidateOverallQuote(){
	var value1=$("#quoteTypeDetail").val();
	var value2=$("#jobQuoteRevision").val();
	var value3=$("#jobQuoteInternalNote").val();
	var value4=$("#jobQuoteSubmittedBYFullName").val();
	var value5=$('#quotechkTotalPrice').prop('checked');
	var value6=$('#chk_includeLSD').prop('checked');
	var value7=$('#chk_donotQtyitem2and3').prop('checked');
	var value8=$('#chk_showTotPriceonly').prop('checked');
	var gridRows = $('#addnewquotesList').getRowData();
    var new_Quotestlinesform_lines_form =  JSON.stringify(gridRows);
    value=value1+value2+value3+value4+value5+value6+value7+value8+new_Quotestlinesform_lines_form
	return value;
}
function validatequotedisabledbuttons(){
	var newquoteformvalue=ValidateOverallQuote();
	console.log("oldQuoteFormvalue==="+oldQuoteFormvalue);
	console.log("newquoteformvalue==="+newquoteformvalue);
	if(oldQuoteFormvalue==newquoteformvalue){
		//allow
		$("#quotepdfpreviewButton").css({ opacity: 'initial' });
		$("#quotepdfpreviewButton").attr("disabled", false);
		$("#saveastemplateid").css({ opacity: 'initial' });
	}else{
		//dont allow
		$("#quotepdfpreviewButton").css({ opacity: 0.3 });
		$("#quotepdfpreviewButton").attr("disabled", true);
		$("#saveastemplateid").css({ opacity: 0.3 });
	}
}
