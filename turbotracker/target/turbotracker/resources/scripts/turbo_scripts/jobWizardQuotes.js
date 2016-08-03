var aGlobalVariable;
var aBidderDialogVar;
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
setTimeout('loadQuotesGrid()', 1);
var aSysAdmin = $("#userAdminID").val();
var jobStatus = getUrlVars()["jobStatus"];
if (jobStatus === "Booked") {
	$(".customerNameField").attr("disabled", true);
}
jQuery(document).ready(function() {
	$(".datepicker").datepicker();
	$('#donedate').hide();
	$('#granteddate').hide();
	requestSubstitutionDialog();
	$('#loadingDiv').css({
		"visibility" : "hidden"
	});
	loadQuoteTemplates();
	addQuotesColumnDetails();
});

function saveANDcloseQuote() {
	var aJobNumber = $("#jobNumberHiddenID").val();
	var aQuoteType = $("#quoteTypeDetail").val();
	var aQuoteRev = $("#jobQuoteRevision").val();
	if (aGlobalVariable === "add") {
		if (!$('#quoteManipulateForm').validationEngine('validate')) {
			return false;
		}
		if (aQuoteRev.match(/^-\d+$/)) {
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
		saveQuoteDetailInfo();
		if (data1 !== null) {
			jQuery("#addquotes").dialog("close");
			return true;
		}
	} else if (aGlobalVariable === "edit") {
		if ($("#quoteTypeDetail").val() === '') {
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
			return false;
		} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
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
			return false;
		}
		if (!$('#quoteManipulateForm').validationEngine('validate')) {
			return false;
		}
		if (aQuoteRev.match(/^-\d+$/)) {
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
		saveQuoteDetailInfo();
		aListDetailId = '';
		jQuery("#addquotes").dialog("close");
		return true;
	} else if (aGlobalVariable === "copy") {
		if ($("#quoteTypeDetail").val() === '') {
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
			return false;
		} else if (aQuoteType === aQuoteTypeName
				&& $("#jobQuoteRevision").val() === '') {
			errorText = "Please Add revision value.";
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
		} else if (aQuoteType === aQuoteTypeName
				&& $("#jobQuoteRevision").val() === aRevision) {
			errorText = "Please Change revision value.";
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
		} else if ($("#jobQuoteSubmittedBYFullName").val() === '') {
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
			return false;
		}
		if (!$('#quoteManipulateForm').validationEngine('validate')) {
			return false;
		}
		if (aQuoteRev.match(/^-\d+$/)) {
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
		$
				.ajax({
					url : "./jobtabs2/checkQuoteTypeAndRev",
					type : "GET",
					data : {
						'jobNumber' : aJobNumber,
						'quoteType' : aQuoteType,
						'quoteRev' : aQuoteRev
					},
					success : function(data) {
						if (data) {
							var errorText = "A Quote with Same Type and Revision already exist. Please change 'Type' or 'Revision' to Copy Quote.";
							jQuery(newDialogDiv).html(
									'<span><b style="color:red;">' + errorText
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
										trigger("reloadGrid");
									}
								} ]
							}).dialog("open");
							return false;
						} else {
							saveQuote();
							jQuery("#addquotes").dialog("close");
							return true;
						}
					}
				});
	}
	return true;
}

function cancelQuote() {
	aCancelQuote = "cancelQuoteList";
	var gridData = jQuery("#addquotesList").getRowData();
	var headerID = '';
	for ( var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		headerID = rowData["joQuoteHeaderID"];
	}

	if (isquoteAddnew === 'yes') {
		deleteQuickQuote(headerID, aCancelQuote);
	}
	$('#quoteManipulateForm').validationEngine('hideAll');
	jQuery("#addquotes").dialog("close");
	return $("#addQuotesView").css("display", "none");
}

function cancelEditQuote() {
	if ($("#joDetailID").val() !== '') {
		var aQuoteDetailId = $("#joDetailID").val();
		var aDetailId = aQuoteDetailId.split(",");
		var Count = aDetailId.length;
		Count = Count - 1;
		for ( var i = 0; i <= Count; i++) {
			deleteDetailFrom(aDetailId[i]);
		}
	}
	$('#quoteManipulateForm').validationEngine('hideAll');
	jQuery("#addquoteTemplate").dialog("close");
	$(this).dialog("close");
	return $("#editQuotesView").css("display", "none");
}

function cancelCopyQuote() {
	aCancelQuote = "cancelQuoteList";
	var headerID = $("#joHeaderID").val();
	deleteQuickQuote(headerID, aCancelQuote);
	$('#quoteManipulateForm').validationEngine('hideAll');
	jQuery("#addquotes").dialog("close");
	return $("#copyQuotesView").css("display", "none");
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
		close : function() {
			return true;
		}
	});
});
function updateAddendums() {
	var addendumsFormSeri = $("#addendumsForm").serialize();
	var joMasterID = $("#joMasterHiddenID").val();
	var addendumsForm = addendumsFormSeri + "&joMasterID=" + joMasterID;
	$.ajax({
		url : "./jobtabs2/updateAddendums",
		data : addendumsForm,
		mType : "GET",
		success : function(data) {
			var quoteThru = data.addendumQuotedThru;
			var quoteReceived = data.addendumReceived;
			$("#received_id").val(quoteReceived);
			$("#quoteThru_id").val(quoteThru);
		}
	});
	jQuery("#addendumsDialog").dialog("close");
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
	if (aBidderDialogVar === "add") {
		$.ajax({
			url : "./jobtabs2/manpulateQuotebidList",
			type : "POST",
			async : false,
			data : bidderListValue + "&oper=" + aBidderDialogVar,
			success : function(data) {
				$("#quotesBidlist").trigger("reloadGrid");
			}
		});
	} else if (aBidderDialogVar === "edit") {
		$.ajax({
			url : "./jobtabs2/manpulateQuotebidList",
			type : "POST",
			async : false,
			data : bidderListValue + "&oper=" + aBidderDialogVar,
			success : function(data) {
				$("#quotesBidlist").trigger("reloadGrid");
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
						height : 750,
						width : dialogWidth,
						top : 1000,
						position : [
								($(window).width() / 2) - (dialogWidth / 2),
								190 ],
						modal : true,
						title : 'Add/Edit Quote',
						iframe : true,
						open : function(event, ui) {
							jQuery('.ui-dialog-titlebar-close').removeClass(
									"ui-dialog-titlebar-close").hide();
						},
						buttons : {

						},
						close : function() {
							jQuery(this).dialog("close");
						}
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
	return formatCurrency(cellValue);
}

function addquotesdialog() {
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
}

function editQuoteDetails() {
	aGlobalVariable = "edit";
	aGlobalConstant = "edit";
	isquoteAddnew = "no";
	var editQuotesView = '<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelEditQuote()" style="width:80px;">';
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
}

function onDoubleClickEditQuoteDetails(theRowID) {
	aGlobalVariable = "edit";
	aGlobalConstant = "edit";
	isquoteAddnew = "no";
	var editQuotesView = '<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="saveANDcloseQuote()" style=" width:125px;">&nbsp;'
			+ '<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelEditQuote()" style="width:80px;">';
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
	addQuotesColumnDetails();
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
}

function editBidDetails() {
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
	$("#lastQuoteBidderID").show();
	$("#revBidderID").show();
	$("#jobNumberBidderID").hide();
	$("#rxMasterBidderID").hide();
	$("#joMasterBidderID").hide();
	$("#rxContactIdBidderID").hide();
	$("#repBidderID").hide();
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
	setTimeout("$('#contactId option[value=" + rxContactID
			+ "]').attr('selected','selected')", 1000);
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
	var joQuoteHeaderID = 0;

	var quoteInfo = $("#quoteManipulateForm").serialize();
	if (aGlobalVariable === "edit") {
		joQuoteHeaderID = $("#joHeaderID").val();
		quoteInfo = quoteInfo + "&joHeaderQuoteID=" + joQuoteHeaderID;
	}
	if (gridInfo !== true) {
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
					click : function() {
						$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
		} else if (jQuery("#addquotesList").getGridParam("records") === 0) {
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
					click : function() {
						$(this).dialog("close");
					}
				} ]
			}).dialog("open");
			return false;
		}
	}
	var gridData = jQuery("#addquotesList").getRowData();
	var totalcost = 0;
	var totalPrice = 0;
	var cost = 0;
	var price=0;
	var disCountPrice = $("#quoteDiscountedPrice").val();
	var disAmt = disCountPrice.replace(/[^0-9\.]+/g, "");
	disAmt = Number(disAmt);
	for ( var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		if(rowData["cost"])
			cost = rowData["cost"].replace(/[^0-9\.]+/g, "");
		totalcost = totalcost + Number(cost);
		if(rowData["price"])
			price = rowData["price"].replace(/[^0-9\.]+/g, "");
		totalPrice = totalPrice + Number(price);
	}
	if (joQuoteHeaderID === false) {
		joQuoteHeaderID = "";
	}
	var data1 = '';
	$.ajax({
				url : "./jobtabs2/SaveQuoteDetailInfo",
				type : "POST",
				async : false,
				data : quoteInfo + "&jobNumber=" + $("#jobNumber_ID").text()
						+ "&token=" + aGlobalVariable + "&totalcost="
						+ totalcost + "&totalPrice=" + totalPrice
						+ "&joHeaderQuoteID=" + joQuoteHeaderID
						+ "&quoteDiscountedPrice=" + disAmt + "&quoteRemarks="
						+ $("#quoteRemarksID").val(),
				success : function(data) {

					if (aGlobalVariable === 'copy') {
						var joQuoteheaderID = data;
						var gridData = jQuery("#addquotesList").getRowData();
						for ( var index = 0; index < gridData.length; index++) {
							var rowData = gridData[index];
							var product = rowData["product"];
							var quantity = rowData["itemQuantity"];
							var manufacturerID = rowData["rxManufacturerID"];
							var paragraph = rowData["paragraph"];
							var factoryID = rowData["veFactoryId"];
							var cost = rowData["cost"]
									.replace(/[^0-9\.]+/g, "");
							totalcost = totalcost + Number(cost);
							var price = rowData["price"].replace(/[^0-9\.]+/g,
									"");
							totalPrice = totalPrice + Number(price);
							var addQuoteValues = "product=" + product
									+ "&paragraph=" + paragraph
									+ "&rxManufacturerID=" + manufacturerID
									+ "&itemQuantity=" + quantity + "&cost="
									+ cost + "&oper=add&joQuoteHeaderID="
									+ joQuoteheaderID + "&quoteheaderid="
									+ joQuoteheaderID + "&veFactoryId="
									+ factoryID + "&price=" + price;
							$.ajax({
										url : "./jobtabs2/manpulaterProductQuotes",
										type : "POST",
										data : addQuoteValues,
										success : function(data) {
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
												$("#addquotesList").trigger(
														"reloadGrid");
											}
										}
									});
						}
						data1 = data;
						$("#quotes").trigger("reloadGrid");
						jQuery(newDialogDiv)
								.html(
										'<span><b style="color:Green;">Quote details copied.</b></span>');
						jQuery(newDialogDiv).dialog({
							modal : true,
							width : 300,
							height : 150,
							title : "Success.",
							buttons : [ {
								height : 35,
								text : "OK",
								click : function() {
									$(this).dialog("close");
								}
							} ]
						}).dialog("open");
						$("#joHeaderID").val(data1);
						return true;
					}
					aGlobalVariable = "edit";
					data1 = data;
					if (aAddEditQuote !== 'addEditQuote') {
						$("#quotes").trigger("reloadGrid");
					}
					jQuery(newDialogDiv)
							.html(
									'<span><b style="color:Green;">Quote details updated.</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Success.",
						buttons : [ {
							height : 35,
							text : "OK",
							click : function() {
								$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					$("#joHeaderID").val(data1);
					return true;
				},
				error : function(jqXHR, textStatus, errorThrown) {
					errorText = $(jqXHR.responseText).find('u').html();
					jQuery(newDialogDiv).html(
							'<span><b style="color:red;">Error on modifying the data: '
									+ errorText + '</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Fatal Error.",
						buttons : [ {
							height : 35,
							text : "OK",
							click : function() {
								$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					return true;
				}
			});
	return data1;
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

function copyQuickQuote() {
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
	loadQuotesListDetails();
	jQuery("#joQuoteheader").text(joQuoteHeaderID);
	$("#joHeaderID").val(joQuoteHeaderID);
	saveQuoteDetail();
	jQuery("#addquotes").dialog("open");
	if (quoteRevision !== '') {
		errorText = "Please change to revision value.";
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
	} else {
		errorText = "Please Add revision value.";
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
}

function deleteQuickQuoteDialog() {
	var grid = $("#quotes");

	var rowId = grid.jqGrid('getGridParam', 'selrow');
	if (rowId !== null) {
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

function deleteQuote(joQuoteHeaderID, aCancelQuote) {

	$.ajax({
		url : "./jobtabs2/deleteQuickQuote",
		mType : "GET",
		data : {
			"joHeaderQuoteID" : joQuoteHeaderID
		},
		success : function(data) {
			var errorText = '';
			if (aCancelQuote !== '') {
				errorText = "Quote Cancelled";
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv).html(
						'<span><b style="color: green;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Information",
					buttons : [ {
						height : 35,
						text : "OK",
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				$("#quotes").trigger("reloadGrid");
			} else {
				errorText = "Quote deleted successfully.";
				jQuery(newDialogDiv).attr("id", "msgDlg");
				jQuery(newDialogDiv).html(
						'<span><b style="color: green;">' + errorText
								+ '</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 300,
					height : 150,
					title : "Information",
					buttons : [ {
						height : 35,
						text : "OK",
						click : function() {
							$(this).dialog("close");
						}
					} ]
				}).dialog("open");
				$("#quotes").trigger("reloadGrid");
			}
		}
	});
}
function deleteBidDialog() {
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
					$("#quotesBidlist").trigger("reloadGrid");
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
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
}

function deleteBidQuote(joBidderId) {
	$.ajax({
		url : "./jobtabs2/deleteQuotebidList",
		mType : "GET",
		data : {
			'selectBidderId' : joBidderId
		},
		success : function(data) {
			var errorText = "Bid deleted successfully.";

			jQuery(newDialogDiv).attr("id", "msgDlg");
			jQuery(newDialogDiv).html(
					'<span><b style="color: green;">' + errorText
							+ '</b></span>');
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
	if (aQuotePDF === 'QuotePDF') {
		$.ajax({
			type : "GET",
			url : "./quotePDFController/SaveAsBidList",
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
				'QuotePDF' : aQuotePDF
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
		window.open("./quotePDFController/SaveAsBidList?enginnerName="
				+ aEngineer + "&architectName=" + aArchitect + "&bidDate="
				+ aBidDate + "&projectName=" + aProjectName + "&planDate="
				+ aPlanDate + "&locationCity=" + aLocationCity
				+ "&locationState=" + aLocationState + "&quoteTOName="
				+ aQuoteFullName + "&bidderContact=" + aJoBidderContactFullName
				+ "&quoteTypeID=" + aJoBidderQuoteType + "&quoteRev="
				+ aJoBidderRev + "&joMasterID=" + joMasterID + "&QuoteThru="
				+ aThruAddendum + "&todayDate=" + currenDate + "&jobNumber="
				+ aJobNumber + "&paragraphCheck=" + aParagraph
				+ "&manufactureCheck=" + aVendor + "&QuotePDF=" + aQuotePDF);
	}
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
							click : function() {
								$(this).dialog("close");
							}
						} ]
					}).dialog("open");
					return false;
				} else {
					var aEmail = bidderGrid.jqGrid('getCell', bidderGridRowId,
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
					jQuery(newDialogDiv).html(
							'<span><b style="color:green;">' + errorText
									+ '</b></span>');
					jQuery(newDialogDiv).dialog(
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
					return true;
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
	$.ajax({
		url : "./sendMailServer/sendMail",
		type : "POST",
		data : aSendMailProperties,
		success : function(data) {
			$('#loadingDiv').css({
				"visibility" : "hidden"
			});
			var errorText = "Mail Sent Successfully.";

			jQuery(newDialogDiv).html(
					'<span><b style="color:green;">' + errorText
							+ '</b></span>');
			jQuery(newDialogDiv).dialog(
					{
						modal : true,
						width : 300,
						height : 150,
						title : "Message",
						buttons : [ {
							height : 35,
							text : "OK",
							click : function() {
								var today = new Date();
								var dd = today.getDate();
								var mm = today.getMonth() + 1;
								var yyyy = today.getFullYear().toString()
										.substr(2, 2);
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
								today = mm + '/' + dd + '/' + yyyy + " "
										+ hours + ":" + minutes + " " + ampm;
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
										$("#quotesBidlist")
												.jqGrid('GridUnload');
										loadBidListGrid();
										$("#quotesBidlist").trigger(
												"reloadGrid");
									}
								});
								$("#quotesBidlist").jqGrid('GridUnload');
								loadBidListGrid();
								$("#quotesBidlist").trigger("reloadGrid");
								$(this).dialog("close");
							}
						} ]
					}).dialog("open");
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
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return false;
	}
	var aInfo = true;
	if (aInfo) {
		var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
		jQuery(newDialogDiv).html(
				'<span><b style="color:green;">' + information + '</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 340,
			height : 170,
			title : "Information",
			buttons : [ {
				height : 35,
				text : "OK",
				click : function() {
					$(this).dialog("close");
				}
			} ]
		}).dialog("open");
		return true;
	}
}

function saveQuoteDetail() {
	var joQuoteHeaderID = 0;
	var quoteInfo = $("#quoteManipulateForm").serialize();
	var gridData = jQuery("#addquotesList").getRowData();
	var totalcost = 0;
	var totalPrice = 0;
	for ( var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var cost = rowData["cost"].replace(/[^0-9\.]+/g, "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^0-9\.]+/g, "");
		totalPrice = totalPrice + Number(price);
	}
	var disCountPrice = $("#quoteDiscountedPrice").val();
	var disAmt = disCountPrice.replace(/[^0-9\.]+/g, "");
	disAmt = Number(disAmt);
	if (joQuoteHeaderID === false) {
		joQuoteHeaderID = "";
	}
	var data1 = '';
	$.ajax({
		url : "./jobtabs2/SaveQuoteDetailInfo",
		type : "POST",
		async : false,
		data : quoteInfo + "&jobNumber=" + $("#jobNumber_ID").text()
				+ "&token=" + aGlobalVariable + "&totalcost=" + totalcost
				+ "&totalPrice=" + totalPrice + "&joHeaderQuoteID="
				+ joQuoteHeaderID + "&quoteRemarks="
				+ $("#quoteRemarksID").val() + "&quoteDiscountedPrice="
				+ disAmt,
		success : function(data) {
			if (aGlobalVariable === 'copy') {
				$("#headerIDForCopyQuote").val(data);
				var previousJoQuoteHeader = jQuery("#joQuoteheader").text();
				var currentJoQuoteheaderID = $("#headerIDForCopyQuote").val();
				$.ajax({
					url : "./jobtabs2/getLineItemsForCopyQuote",
					type : "POST",
					data : {
						'previousJoQuoteHeader' : previousJoQuoteHeader,
						'currentJoQuoteheaderID' : currentJoQuoteheaderID
					},
					success : function(data) {
						$("#joQuoteheader").text(currentJoQuoteheaderID);
						$("#addquotesList").jqGrid('GridUnload');
						loadQuotesListDetails();
						$('#addquotesList').jqGrid('setGridParam', {
							postData : {
								'joQuoteHeaderID' : currentJoQuoteheaderID
							}
						});
						$("#addquotesList").trigger("reloadGrid");
					}
				});
				data1 = data;
				$("#joHeaderID").val(data1);
				return true;
			}
		}
	});
	return data1;
}

function copyEachLineItem() {
	var joQuoteheaderID = $("#headerIDForCopyQuote").val();
	var gridData = jQuery("#addquotesList").getRowData();
	var totalcost = 0;
	var totalPrice = 0;
	for ( var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var product = rowData["product"];
		var quantity = rowData["itemQuantity"];
		var manufacturerID = rowData["rxManufacturerID"];
		var paragraph = rowData["paragraph"];
		var factoryID = rowData["veFactoryId"];
		var inlineNote = rowData["inlineNote"];
		var footerNote = rowData["productNote"];
		var cost = rowData["cost"].replace(/[^0-9\.]+/g, "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^0-9\.]+/g, "");
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

function saveQuote(gridInfo) {
	var joQuoteHeaderID = $("#joHeaderID").val();
	var quoteInfo = $("#quoteManipulateForm").serialize();
	var gridData = jQuery("#addquotesList").getRowData();
	var typeDetailId = $("#quoteTypeDetailID").val();
	var totalcost = 0;
	var totalPrice = 0;
	for ( var index = 0; index < gridData.length; index++) {
		var rowData = gridData[index];
		var cost = rowData["cost"].replace(/[^0-9\.]+/g, "");
		totalcost = totalcost + Number(cost);
		var price = rowData["price"].replace(/[^0-9\.]+/g, "");
		totalPrice = totalPrice + Number(price);
	}
	var disCountPrice = $("#quoteDiscountedPrice").val();
	var disAmt = disCountPrice.replace(/[^0-9\.]+/g, "");
	disAmt = Number(disAmt);
	var data1 = '';
	$.ajax({
				url : "./jobtabs2/SaveQuoteInfo",
				type : "POST",
				async : false,
				data : quoteInfo + "&jobNumber=" + $("#jobNumber_ID").text()
						+ "&token=" + aGlobalVariable + "&totalcost="
						+ totalcost + "&totalPrice=" + totalPrice
						+ "&joHeaderQuoteID=" + joQuoteHeaderID
						+ "&quoteRemarks=" + $("#quoteRemarksID").val()
						+ "&quoteDiscountedPrice=" + disAmt,
				success : function(data) {

					if (aGlobalVariable === 'copy') {
						var joQuoteheaderID = data;
						var gridData = jQuery("#addquotesList").getRowData();
						for ( var index = 0; index < gridData.length; index++) {
							var rowData = gridData[index];
							var joQuoteDetailId = rowData["joQuoteDetailID"];
							var product = rowData["product"];
							var quantity = rowData["itemQuantity"];
							var manufacturerID = rowData["rxManufacturerID"];
							var paragraph = rowData["paragraph"];
							var factoryID = rowData["veFactoryId"];
							var inlineNote = rowData["inlineNote"];
							var footerNote = rowData["productNote"];
							var cost = rowData["cost"]
									.replace(/[^0-9\.]+/g, "");
							totalcost = totalcost + Number(cost);
							var price = rowData["price"].replace(/[^0-9\.]+/g,
									"");
							totalPrice = totalPrice + Number(price);
							var addQuoteValues = "product=" + product+ "&joQuoteDetailID=" + joQuoteDetailId+ "&paragraph=" + paragraph+ "&rxManufacturerID=" + manufacturerID+ "&itemQuantity=" + quantity+ "&inlineNote=" + inlineNote+ "&footnote=" + footerNote + "&cost="+ cost + "&oper=add&joQuoteHeaderID="+ joQuoteheaderID + "&quoteheaderid="+ joQuoteheaderID + "&veFactoryId="+ factoryID + "&price=" + price;
							$
									.ajax({
										url : "./jobtabs2/manpulaProductQuotes",
										type : "POST",
										data : addQuoteValues,
										success : function(data) {
											$("#joQuoteheader").text(joQuoteHeaderIdentity);
											$("#addquotesList").jqGrid('GridUnload');
											loadQuotesListDetails();
											$('#addquotesList').jqGrid('setGridParam',{postData : {'joQuoteHeaderID' : joQuoteheaderID}});
											$("#addquotesList").trigger("reloadGrid");
											if (Number(joQuoteheaderID) === 0) {
												$("#addquotesList").jqGrid('GridUnload');
												loadQuotesListDetails();
												$('#addquotesList').jqGrid('setGridParam',{postData : {'joQuoteHeaderID' : name}});
												$("#addquotesList").trigger("reloadGrid");
											}
										}
									});
						}
						data1 = data;
						$("#quotes").trigger("reloadGrid");
						if (aTypeDetail !== typeDetailId) {
							errorText = "Type value Changed And Quote details copied";
							jQuery(newDialogDiv).attr("id", "msgDlg");
							jQuery(newDialogDiv).html(
									'<span><b style="color:green;">'
											+ errorText + '</b></span>');
							jQuery(newDialogDiv).dialog({modal : true,width : 300,height : 150,title : "Information",buttons : [ {height : 35,text : "OK",click : function() {$(this).dialog("close");}} ]}).dialog("open");
						} else {
							jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details copied.</b></span>');
							jQuery(newDialogDiv).dialog({
									modal : true,
									width : 300,
									height : 150,
									title : "Success.",
									buttons : [ {height : 35,text : "OK",click : function() {$(this).dialog("close");}} ]}).dialog("open");}
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
	var aContractAmount = $("#contractAmount").val();
	var aEstimatedCost = $("#estimatedCost").val();
	var aEstimatedProfit = $("#estimatedProfit").val();
	var aContractAmt = aContractAmount.replace(/[^0-9\.]+/g, "");
	aContractAmt = Number(aContractAmt);
	var aEstimateCot = aEstimatedCost.replace(/[^0-9\.]+/g, "");
	aEstimateCot = Number(aEstimateCot);
	var aEstimatePro = aEstimatedProfit.replace(/[^0-9\.]+/g, "");
	aEstimatePro = Number(aEstimatePro);
	var aEstimatedProfi = aContractAmt - aEstimateCot;
	var ajoMasterId = $("#joMasterHiddenID").val();
	$.ajax({
		url : "./jobtabs2/updateJobamount",
		mType : "GET",
		data : {
			'joMasterID' : ajoMasterId,
			'ContractAmount' : aContractAmt,
			'EstimateCost' : aEstimateCot,
			'EstimateProfit' : aEstimatedProfi
		},
		success : function(data) {

			errorText = "Amount is updated";
			jQuery(newDialogDiv).attr("id", "msgDlg");
			jQuery(newDialogDiv).html(
					'<span><b style="color:green;">' + errorText
							+ '</b></span>');
			jQuery(newDialogDiv).dialog({
				modal : true,
				width : 300,
				height : 150,
				title : "Information",
				buttons : [ {
					height : 35,
					text : "OK",
					click : function() {
						$(this).dialog("close");
						window.location.reload();
					}
				} ]
			}).dialog("open");
		}
	});
}

function trimContractAmount() {
	var aContractAmount = $("#contractAmount").val();
	var aContractAmt = aContractAmount.replace(/[^0-9\.]+/g, "");
	aContractAmt = Number(aContractAmt);
	$("#contractAmount").val(aContractAmt);
}

function trimEstimatedCost() {
	var aEstimatedCost = $("#estimatedCost").val();
	var aEstimatedCst = aEstimatedCost.replace(/[^0-9\.]+/g, "");
	aEstimatedCst = Number(aEstimatedCst);
	$("#estimatedCost").val(aEstimatedCst);
}

function estimateProfit() {
	var aContractAmount = $("#contractAmount").val();
	var aEstimatedCost = $("#estimatedCost").val();
	var aContractAmt = aContractAmount.replace(/[^0-9\.]+/g, "");
	aContractAmt = Number(aContractAmt);
	var aEstimateCot = aEstimatedCost.replace(/[^0-9\.]+/g, "");
	aEstimateCot = Number(aEstimateCot);
	var aEstimatedProfit = aContractAmt - aEstimateCot;
	$("#estimatedProfit").val("");
	$("#estimatedProfit").val(aEstimatedProfit);
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
		if (aUserAdmin !== '1') {
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
		} else {
			return aContractAmount;
		}
	}
}

function estimateCostAdminValidation(aEstimateAmount) {
	if (jobStatus === "Booked") {
		if (aUserAdmin !== '1') {
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
		} else {
			return aEstimateAmount;
		}
	}
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
				$("#quantityprintCosttempID").attr("checked", true);
				$("#costLabelPrinttempID").empty();
				$("#costLabelPrinttempID").append("(Yes)");
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
	return true;
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
			dynamicColNames = [ '', 'Description' ]
			defaultColNames = [ 'QuoteDetailID', 'ManufacturerID','QuoteHeaderID', 'FactoryID', 'LineItem', 'FootLine' , 'Inlinenote'];
		var defaultColmodels = [ {name : 'joQuoteTemplateDetailId',index : 'joQuoteTemplateDetailId',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}}, 
								{name : 'rxManufacturerId',index : 'rxManufacturerId',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}}, 
								{name : 'joQuoteTemplateHeaderId',index : 'joQuoteTemplateHeaderId',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}},
								{name : 'veFactoryId',index : 'veFactoryId',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}}, 
								{name : 'inlineNote',index : 'inlineNote',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}}, 
								{name : 'productNote',index : 'productNote',align : 'left',width : 90,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}},
								{name : 'edit',index : 'edit',align : 'center',width : 40,hidden : false,editrules : {edithidden : false},formatter : imgFmatterTemp}
								];
			var dynamicColModels = [ {name : 'inLineNoteImage',index : 'inLineNoteImage',align : 'right',width : 10,editable : false,hidden : false,formatter : inlineImage}, {name : 'product',index : 'product',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {},editrules : {}} ];
			if (data.displayQuantity === 1) {
				dynamicColNames.push('Qty');
				dynamicColModels.push({name : 'itemQuantity',index : 'itemQuantity',align : 'center',width : 30,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {}});
			} else {
				dynamicColNames.push('Qty');
				dynamicColModels.push({name : 'itemQuantity',index : 'itemQuantity',align : 'center',width : 30,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}});
			}
			if (data.displayParagraph === 1) {
				dynamicColNames.push('Paragraph');
				dynamicColModels.push({name : 'paragraph',index : 'paragraph',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {}});
			}
			if (data.displayManufacturer === 1) {
				dynamicColNames.push('Vendors');
				dynamicColModels.push({name : 'manufacturerName',index : 'manufacturerName',align : 'left',width : 90,editable : true,hidden : false,edittype : 'text',editoptions : {},editrules : {}});		
				}
			if (data.displaySpec === 1) {
				dynamicColNames.push('Spec');
				dynamicColModels.push({name : 'spec',index : 'spec',align : 'right',width : 20,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {}});}
			if (data.displayCost === 1) {
				dynamicColNames.push('Cost');
				dynamicColModels.push({name : 'cost',index : 'cost',align : 'right',width : 50,editable : true,hidden : false,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {}});
			} else {
				dynamicColNames.push('Cost');
				dynamicColModels.push({name : 'cost',index : 'cost',align : 'right',width : 50,editable : true,hidden : true,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {}});
			}
			if (data.displayMult === 1) {
				dynamicColNames.push('Multi');
				dynamicColModels.push({name : 'mult',index : 'mult',align : 'right',width : 20,editable : true,hidden : false,edittype : 'text',editoptions : {size : 30},editrules : {}});
			} else {
				dynamicColNames.push('Multi');
				dynamicColModels.push({name : 'mult',index : 'mult',align : 'right',width : 20,editable : true,hidden : true,edittype : 'text',editoptions : {size : 30},editrules : {}});}
			if (data.displayPrice === 1) {
				dynamicColNames.push('price');
				dynamicColModels.push({name : 'price',index : 'price',align : 'right',width : 50,editable : true,hidden : false,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {}});
			} else {
				dynamicColNames.push('price');
				dynamicColModels.push({name : 'price',index : 'price',align : 'right',width : 50,editable : true,hidden : true,formatter : customCurrencyFormatter,edittype : 'text',editoptions : {size : 30},editrules : {}});}
			for ( var i = 0; i < defaultColNames.length; i++) {
				dynamicColNames.push(defaultColNames[i]);
				dynamicColModels.push(defaultColmodels[i]);
			}
			console.log("colnames" + dynamicColNames);
			console.log(dynamicColModels);
			loadTemplateLineItemDetails(joQuoteHeaderID, dynamicColNames,
					dynamicColModels);
		},
		error : function(data) {
			console.log(data);
		}
	});
}


