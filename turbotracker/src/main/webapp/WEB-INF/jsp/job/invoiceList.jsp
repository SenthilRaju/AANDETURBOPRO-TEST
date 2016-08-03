<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - CustomerInvoice</title>
<style type="text/css">
#mainMenuJobsPage {
	text-decoration: none;
	color: #FFFFFF;
	background-color: #54A4DE;
}

#mainMenuJobsPage a span {
	color: #FFF
}
.loadingDivBulkEmailAttachment{
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    height: 127%;
    width: 100%;
    background: url('../resources/scripts/jquery-autocomplete/send_mail_waiting.gif') 50% 50% no-repeat;
}
</style>
</head>

<script type="text/javascript">
	var _blockjsfiles = true;
</script>
<body>
	<div style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./../headermenu.jsp"></jsp:include>
		</div>
		<input type="hidden" id="chk_cusReqDivinCusInvYes"
			value="${requestScope.chk_cusReqDivinCusInvYes }" />
		<table style="width: 979px; margin: 0 auto;">
			<tr>
				<td align="left"><div style="float: left;">
						<input type="checkbox" id="dateRange" name="dateRange"
							onclick="callEnableDate();"> <label>Date Range:</label><input
							type="text" name="fromDate" id="fromDate" size="10"><label>Thru</label><input
							type="text" name="toDate" id="toDate" size="10">
					</div>
				<td align="left">
					<div>
						<input type="button" class="add" value="   Add"
							alt="Add new Invoice" onclick="addNewInvoice()"
							style="margin-left: -100%;" />&nbsp; <input type="button"
							class="go turbo-blue" value="Batch" alt="Add Batch"
							style="margin-left: -197%;" onclick="addBatchInvoice()" />&nbsp;
						<!--onclick="addBatchInvoice()" -->
						<input type="hidden" id="operation" name="operation" /> <input
							type="hidden" id="rxCustomerID" name="rxCustomerID" /> <input
							type="hidden" id="rxShipToID" name="rxShipToID" /> <input
							type="hidden" id="rxShipToOtherAddressID"
							name="rxShipToOtherAddressID" /> <input type="hidden" id="cusoid"
							name="cusoid" /> <input type="hidden" id="shipToMode"
							name="shipToMode" /> <input type="hidden" id="clickedButtonID"
							name="clickedButtonID" />

					</div>
				</td>
			</tr>
			<tr>
				<td>
					<table id="CustomerInvoiceGrid"></table>
					<div id="CustomerInvoiceGridPager"></div>
				</td>
			</tr>
			<tr>
				<td>

					<div style="float: right; margin-right: 0%">
						<input type="checkbox" id="taxAll" name="taxAll"><label
							style="padding-left: -10px;">Include&nbsp;Tax&nbsp;in&nbsp;Total</label>
					</div>
				</td>
			</tr>

		</table>
		<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div><jsp:include page="../customer_invoice.jsp"></jsp:include><input
			type="hidden" id="frompage"><input type="hidden"
			id="invoiceIDfrompage"><input type="hidden"
			id="rxMasterIDfrompage">
		<jsp:include page="../Email_Attachment.jsp"></jsp:include>
	</div>
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<script type="text/javascript">
		bkLib.onDomLoaded(function() {
			//nicEditors.allTextAreas(); 
			var myNicEditor = new nicEditor();
			myNicEditor.panelInstance(document.getElementById('econt'));
			/* myNicEditor.panelInstance(
			        document.getElementById('specialInstructionID')
			    ); */
		});

		$(document).ready(function() {
			$("#cusinvoicetab").dialog({
				autoOpen : false
			});

			//$("#customerInvoice_invoiceDateID").attr("readOnly",true);
			$("#customerInvoice_shipDateID").attr("readOnly", true)
			$("#customerInvoice_dueDateID").attr("readOnly", true)

			getcoFiscalPerliodDate("customerInvoice_invoiceDateID");
			getcoFiscalPerliodDate("customerInvoice_shipDateID");
			getcoFiscalPerliodDate("customerInvoice_dueDateID");
			var frompage = "${requestScope.frompage}";
			$("#frompage").val(frompage);
			var invoiceID = "${requestScope.invoiceID}";
			$("#invoiceIDfrompage").val(invoiceID);
			var rxmast = "${requestScope.rxMasterId}";
			$("#rxMasterIDfrompage").val(rxmast);
		});

		var id = '';
		$('input[type="button"]')
				.click(
						function(e) {
							id = e.target.id;
							if ("CuInvoiceSaveID" == id
									|| "CuInvoiceLineSaveID" == id) {
								$('#setButtonValue').val("Save");
								$('#clickedButtonID').val(this.id);

							}
							if ("CuInvoiceSaveCloseID" == id
									|| "CuInvoiceLineSaveCloseID" == id) {
								$('#setButtonValue').val("SaveandClose");
								$('#clickedButtonID').val(this.id);
							}

						});

		$("#prWareHouseSelectlineID").change(
				function() {
					var aValue = $("#prWareHouseSelectlineID option:selected")
							.val();
					$("#prWareHouseSelectID option[value=" + aValue + "]")
							.attr("selected", true);
				});

		$("#prWareHouseSelectID").change(
				function() {
					var aValue = $("#prWareHouseSelectID option:selected")
							.val();
					$("#prWareHouseSelectlineID option[value=" + aValue + "]")
							.attr("selected", true);
				});
		$("#shipViaCustomerSelectlineID").change(
				function() {
					var aValue = $(
							"#shipViaCustomerSelectlineID option:selected")
							.val();
					$("#shipViaCustomerSelectID option[value=" + aValue + "]")
							.attr("selected", true);
				});

		$("#shipViaCustomerSelectID").change(
				function() {
					var aValue = $("#shipViaCustomerSelectID option:selected")
							.val();
					$(
							"#shipViaCustomerSelectlineID option[value="
									+ aValue + "]").attr("selected", true);
				});
		var oper = "${requestScope.operation}";
		$('#operation').val(oper);

		$(function() {
			var cache = {};
			var lastXhr = '';
			$("#searchJob").autocomplete(
					{
						minLength : 3,
						timeout : 1000,
						/*open: function(){ 
							$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
							$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
						},*/
						select : function(event, ui) {
							var aValue = ui.item.value;
							var aText = ui.item.label;
							var valuesArray = new Array();
							valuesArray = aValue.split("|");
							labelsArray = aText.split("||");

							console.log("Value---" + aValue);
							console.log("Label---" + labelsArray);

							var aQryStr = "cuSOID=" + aValue;
							//var acuSoid = rowData['vePOID'];
							//var aMasterID = rowData['rxVendorID'];
							//$('#rxCustomer_ID').text(aMasterID);
							$('#Cuso_ID').text(aValue);
							$('#operation').val('update');
							$('#cuinvoiceIDhidden').val(aValue);
							$('#rxCustomerID').val(labelsArray[5]);
							PreloadDataFromInvoiceTable();

							//$('#salesrelease').dialog("open");
							//document.location.href = "./editpurchaseorder?token=view&" + aQryStr;

						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("search/searchCuInvoiceNumber",
									request, function(data, status, xhr) {
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
			$("#batchInvoiceCuID").autocomplete(
					{
						minLength : 3,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.label;
							var value = ui.item.id;
							$("#batchInvoiceCuIDValue").val(value);
							$("#batchInvoiceCuID").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("search/searchCustomer",
									request, function(data, status, xhr) {
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
			$("#rangeInvoiceFrom").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.label;
							$("#rangeInvoiceFrom").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("search/searchBatchInvoice",
									request, function(data, status, xhr) {
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
			$("#rangeInvoiceTo").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.label;
							$("#rangeInvoiceTo").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("search/searchBatchInvoice",
									request, function(data, status, xhr) {
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

		/**--- Ajax auto suggest functions ---*/
		$(function() {
			var cache = {}, lastXhr;
			$("#customerInvoice_salesRepsList").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerInvoice_salesRepId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("salescontroller/salesrep",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#customerInvoice_CSRList").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerInvoice_CSRId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("employeeCrud/CSRList",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#customerInvoice_SalesMgrList").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerInvoice_salesMgrId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("employeeCrud/salesMGR",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#customerInvoice_EngineersList").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerInvoice_engineerId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("employeeCrud/engineer",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#customerInvoice_PrjMgrList").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerInvoice_prjMgrId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("employeeCrud/projectManager",
									request, function(data, status, xhr) {
										cache[term] = data;
										if (xhr === lastXhr) {
											response(data);
										}
									});
						}
					});
		});

		$(function() {
			var cache = {}, lastXhr;
			$("#customerInvoice_TaxTerritory").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							var value = ui.item.value;
							$("#customerTaxTerritory").val(id);
							var taxRate = ui.item.taxValue;
							$('#customerInvoice_linetaxId').val(taxRate);
							$('#customerInvoice_generaltaxId').val(taxRate);
							var subTotal = Number($(
									"#customerInvoice_subTotalID").val()
									.replace(/[^0-9\.]+/g, ""));
							var fieghtTotal = Number($(
									"#customerInvoice_frightIDcu").val()
									.replace(/[^0-9\.]+/g, ""));
							var thetaxTotal = parseFloat(subTotal)
									* (parseFloat(taxRate) / 100);
							$("#customerInvoice_taxIdcu").val(
									formatCurrency(thetaxTotal));
							$("#cuGeneral_taxvalue").val(
									formatCurrency(thetaxTotal));
							$("#customerInvoice_totalID").val(
									formatCurrency(Number(subTotal)
											+ Number(fieghtTotal)
											+ Number(thetaxTotal)));
							$("#customerInvoice_linetotalID").val(
									formatCurrency(Number(subTotal)
											+ Number(fieghtTotal)
											+ Number(thetaxTotal)));
							$('#CI_taxfreight').val(ui.item.taxfreight);
							setTaxTotal_CI();
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("companycontroller/companyTax",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#customerinvoice_paymentTerms").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#customerinvoicepaymentId").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("employeeCrud/paymentType",
									request, function(data, status, xhr) {
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
			var cache = {}, lastXhr;
			$("#viaID").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var id = ui.item.id;
							$("#viahiddenID").val(id);
						},
						source : function(request, response) {
							var term = request.term;
							if (term in cache) {
								response(cache[term]);
								return;
							}
							lastXhr = $.getJSON("jobtabs3/shipVia", request,
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
			var cache = {}, lastXhr;
			$("#customerShipToAddressID").autocomplete(
					{
						minLength : 2,
						timeout : 1000,
						select : function(event, ui) {
							var aValue = ui.item.value;
							var valuesArray = new Array();
							valuesArray = aValue.split("|");

							var id = valuesArray[0];
							var code = valuesArray[2];
							$('#rxShipToID').val(aValue);
							$.ajax({
								url : './getCustomerAddress?rxMasterId='
										+ aValue,
								type : 'POST',
								success : function(data) {

									$.each(data, function(key, valueMap) {

										if ("customerAddress" == key) {
											//shipToCustomer = valueMap;										
											$.each(valueMap, function(index,
													value) {
												$('#customerShipToAddressID')
														.val(value.name);
												$('#customerShipToAddressID1')
														.val(value.address1);
												$('#customerShipToAddressID2')
														.val(value.address2);
												$('#customerShipToCity').val(
														value.city);
												$('#customerShipToState').val(
														value.state);
												$('#customerShipToZipID').val(
														value.zip);
											});
										}
										/* if("taxRateforCity" == key)
										{
											$.each(valueMap, function(index, value){
												$('#taxGeneralId').val(formatCurrencynodollar(value));
												taxRateShipTo = value;
											
											}); 
										} */
									});
								}
							});
						},
						source : function(request, response) {
							var term = request.term;
							if ($('#shipToMode').val() !== '4') {
								if (term in cache) {
									response(cache[term]);
									return;
								}
								lastXhr = $.getJSON(
										"search/customerAddress?rxMasterId=0",
										request, function(data, status, xhr) {
											cache[term] = data;
											if (xhr === lastXhr) {
												response(data);
											}
										});
							}

						},
						error : function(result) {
							$('.ui-autocomplete-loading').removeClass(
									"ui-autocomplete-loading");
						}
					});
		});
		//$('#customerInvoice_SalesMgrList').val("${sessionScope.user.fullName}");
		var billToCustomer, shipToCustomer, billToCustomerNameGeneralID, shipTorxCustomer_ID, customerMasterObj, CustomerName, shipToCustomerName, taxRateShipTo;
		var taxTerritory, taxTerritoryhiddenID, sEmail;
		$(function() {
			var cache = {}, lastXhr;
			$("#customerInvoice_customerInvoiceID")
					.autocomplete(
							{
								minLength : 2,
								timeout : 1000,
								select : function(event, ui) {
									var id = ui.item.id;
									billToCustomerNameGeneralID = id;
									shipTorxCustomer_ID = id;
									$("#rxCustomerID").val(id);
									//$('#rxShipToID').val(id);
									var CIdivFlag="#CI_Shipto";
									loadCustomerAddress(id);
									preloadShiptoAddress(CIdivFlag,shipTorxCustomer_ID,null,'1','0','');
									$(CIdivFlag).contents().find("#shiptomoderhiddenid").val('1');

									 $("#shiptoaddradio3").button({disabled:true});
									 $('#shiptoaddlabel3').attr('onclick','').unbind('click');
									
									$
											.ajax({
												url : 'rxdetailedviewtabs/SOAddressDetails?rxMasterId='
														+ $('#rxCustomerID')
																.val(),
												type : 'POST',
												success : function(data) {

													console.log("all--->"
															+ data);
													sEmail = "";
													$
															.each(
																	data,
																	function(
																			key,
																			valueMap) {

																		if ("customerAddress" == key) {
																			billToCustomer = valueMap;
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerbillToAddressIDcuInvoice')
																										.val(
																												value.name);
																								$(
																										'#customerbillToAddressID1cuInvoice')
																										.val(
																												value.address1);
																								$(
																										'#customerbillToAddressID2cuInvoice')
																										.val(
																												value.address2);
																								$(
																										'#customerbillToCitycuInvoice')
																										.val(
																												value.city);
																								$(
																										'#customerbillToStatecuInvoice')
																										.val(
																												value.state);
																								$(
																										'#customerbillToZipIDcuInvoice')
																										.val(
																												value.zip);
																								//$('#customerBillToAddressID').val(value.rxAddressId);

																								CustomerName = value.name;

																								shipToCustomer = valueMap;
																								
																								/* $(
																										'#customerShipToAddressID')
																										.val(
																												value.name);
																								$(
																										'#customerShipToAddressID1')
																										.val(
																												value.address1);
																								$(
																										'#customerShipToAddressID2')
																										.val(
																												value.address2);
																								$(
																										'#customerShipToCity')
																										.val(
																												value.city);
																								$(
																										'#customerShipToState')
																										.val(
																												value.state);
																								$(
																										'#customerShipToZipID')
																										.val(
																												value.zip);

																								$(
																										"#shiptolabel3")
																										.addClass(
																												'ui-state-active');
																								$(
																										"#shiptolabel1")
																										.removeClass(
																												'ui-state-active');
																								$(
																										"#shiptolabel2")
																										.removeClass(
																												'ui-state-active');
																								$(
																										"#shiptolabel4")
																										.removeClass(
																												'ui-state-active');

																								$(
																										"#CiShiptolabel2")
																										.addClass(
																												'ui-state-active');
																								$(
																										"#CiShiptolabel1")
																										.removeClass(
																												'ui-state-active');
																								$(
																										"#CiShiptolabel3")
																										.removeClass(
																												'ui-state-active');
																								$(
																										"#CiShiptolabel4")
																										.removeClass(
																												'ui-state-active'); */

																							});
																		}

																		if ("taxRateforCity" == key) {
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerInvoice_generaltaxId')
																										.val(
																												value);
																								$(
																										'#customerInvoice_linetaxId')
																										.val(
																												value);
																								taxRateShipTo = value;
																							});
																		}

																		if ("taxTerritory" == key) {
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerInvoice_TaxTerritory')
																										.val(
																												value);
																								taxTerritory = value;

																							});
																		}
																		if ("taxTerritoryID" == key) {
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerTaxTerritory')
																										.val(
																												value);
																								taxTerritoryhiddenID = value;

																							});
																		}

																		/* if("divisionsSearch" == key)
																		{
																			$.each(valueMap, function(index, value){
																				$('select[name="divisionName"]').find('option:contains("Blue")').attr("selected",true);
																				$('#SOdivisionID').val(value);
																			
																			}); 
																		} */

																		if ("customerMasterObj" == key) {
																			customerMasterObj = valueMap;
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerInvoice_salesRepId')
																										.val(
																												value.cuAssignmentId0);
																								$(
																										'#customerInvoice_CSRId')
																										.val(
																												value.cuAssignmentId1);
																								$(
																										'#customerInvoice_salesMgrId')
																										.val(
																												value.cuAssignmentId2);
																								$(
																										'#customerInvoice_engineerId')
																										.val(
																												value.cuAssignmentId3);
																								$(
																										'#customerInvoice_prjMgrId')
																										.val(
																												value.cuAssignmentId4);
																								$(
																										'#customerinvoicepaymentId')
																										.val(
																												value.cuTermsId);
																								$
																										.ajax({
																											url : './getPaymentTermsDueDate?cuTermsId='
																													+ value.cuTermsId,
																											type : 'GET',
																											success : function(
																													data) {
																												var invoiceDate = new Date(
																														$(
																																'#customerInvoice_invoiceDateID')
																																.val());
																												var dd = invoiceDate
																														.getDate();

																												if (data.dueondays == 0) {
																													invoiceDate
																															.setDate(Number(invoiceDate
																																	.getDate())
																																	+ Number(data.duedate));
																													dd = invoiceDate
																															.getDate();
																												} else {
																													invoiceDate
																															.setDate(invoiceDate
																																	.getDate() + 32);
																													dd = data.duedate;
																												}
																												var mm = invoiceDate
																														.getMonth() + 1;
																												var y = invoiceDate
																														.getFullYear();
																												var someFormattedDate = mm
																														+ '/'
																														+ dd
																														+ '/'
																														+ y;
																												$(
																														'#customerInvoice_dueDateID')
																														.val(
																																someFormattedDate);

																											}
																										});

																							});
																		}

																		if ("customerTabFormDataBean" == key) {

																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								$(
																										'#customerInvoice_salesRepsList')
																										.val(
																												value.assignedSalesRep);
																								$(
																										'#customerInvoice_CSRList')
																										.val(
																												value.assignedCSRs);
																								$(
																										'#customerInvoice_SalesMgrList')
																										.val(
																												value.assignedSalesMGRs);
																								$(
																										'#customerInvoice_EngineersList')
																										.val(
																												value.assignedEngineers);
																								$(
																										'#customerInvoice_PrjMgrList')
																										.val(
																												value.assignedProjManagers);
																								$(
																										'#customerinvoice_paymentTerms')
																										.val(
																												value.customerTerms);
																							});
																		}

																		if ("emailList" == key) {
																			customerMasterObj = valueMap;
																			$
																					.each(
																							valueMap,
																							function(
																									index,
																									value) {
																								if (value.email != null
																										&& value.email
																												.trim() != '')
																									sEmail += '<option value='+value.rxContactId+'>'
																											+ value.email
																											+ '</option>';

																							});
																		}

																	});

													/* $('#SOforWardId').hide();
													$('#SObackWardId').hide();
													$("#SOlocationShipToAddressID").attr("disabled",false);
													$('#customerShipToOtherIDeditable').val("Customer");
													$('#customerShipToOtherID').val("Customer"); */

													//$('#emailList').html(sEmail);
													$('#shipToMode').val(1);
													$('#forWardId').css({
														"display" : "none"
													});
													$('#backWardId').css({
														"display" : "none"
													});
													$('#customerforWardId')
															.css(
																	{
																		"display" : "none"
																	});
													$('#customerbackWardId')
															.css(
																	{
																		"display" : "none"
																	});
													$('#emailList')
															.html(sEmail);
													$('#emailListCU').html(
															sEmail);

												}
											});
								},
								source : function(request, response) {
									var term = request.term;
									if (term in cache) {
										response(cache[term]);
										return;
									}
									lastXhr = $.getJSON(
											"salescontroller/customerName",
											request,
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

		function getcoFiscalPerliodDate(x) {
			$
					.ajax({

						url : "./banking/getcoFiscalPeriod",
						type : "GET",
						success : function(data) {
							try {
								var date1 = new Date();
								var date2 = new Date(data.endDate);
								var timeDiff = Math.abs(date2.getTime()
										- date1.getTime());
								var diffDays = Math.ceil(timeDiff
										/ (1000 * 3600 * 24));
								$("#" + x).datepicker("option", "maxDate",
										diffDays);
								console.log('diffDays' + diffDays);
							} catch (err) {
								console.log(err.message);
							}
							$("#" + x).datepicker("option", "minDate", 0);

						}
					});
		}
		function ResetDetails() {
			$('#fromDate').val("");
			$('#toDate').val("");
			$('#dateRange').prop('checked', false);
			$("#fromDate").prop('disabled', true);
			$("#toDate").prop('disabled', true);
			$("#searchJob").val("");
			$("#CustomerInvoiceGrid").jqGrid('GridUnload');
			showCustomerInvoiceList("", "", "");
		}
	</script>

	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/SalesRelease.js"></script>	
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script> -->
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/invoiceList.js"></script>
	<script type="text/javascript"
		src="./../resources/scripts/turbo_scripts/releaseemail.js"></script>

	<div id="addBatchInvoiceDialog">
		<form action="" id="BatchInvoiceFormId">
			<table>
				<tr>

					<td><label>Customer</label><label style="color: red;"
						id="batchinvoicecustomerlabel"></label></td>
					<td><input type="hidden" id="batchInvoiceCuIDValue"
						name="batchInvoiceCuIDValue" value="0" /> <input type="text"
						id="batchInvoiceCuID" name="batchInvoiceCuID"
						placeholder="Minimum 3 characters required to get List"
						class="ui-autocomplete-input" autocomplete="off" role="textbox"
						aria-autocomplete="list" aria-haspopup="true"
						style="width: 300px;"></td>
				</tr>
				<tr>
					<td><label>Date Range from:</label></td>
					<td><input type="text" id="batchInvoiceFromDate"
						name="batchInvoiceFromDate" style="width: 90px;"
						readonly="readonly"> <label>To:</label> <input type="text"
						id="batchInvoiceToDate" name="batchInvoiceToDate"
						style="width: 90px;" readonly="readonly"></td>
				</tr>
				<tr>
					<td><label>Range of Invoices:</label></td>
					<td><input type="text" id="rangeInvoiceFrom"
						name="rangeInvoiceFrom" class="ui-autocomplete-input"
						autocomplete="off" role="textbox" aria-autocomplete="list"
						aria-haspopup="true" style="width: 90px;"> <label>To:</label>
						<input type="text" id="rangeInvoiceTo" name="rangeInvoiceTo"
						style="width: 90px;"></td>
				</tr>
				<br>
				<tr>
				</tr>
				<tr>
					<td></td>
					<td align="right">
						<input type="button"
						id="emailButton" name="emailButtonName"
						value="Email" class="savehoverbutton turbo-blue"
						onclick="emailDetails()" style="width: 65px;">
						<input type="button"
						id="generateBatchButton" name="generateBatchButtonName"
						value="Print" class="savehoverbutton turbo-blue"
						onclick="generateBatchInvoicePdf()" style="width: 65px;">
						<input type="button" id="cancelBIButton" name="cancelBIButtonName"
						value="Clear" class="cancelhoverbutton turbo-blue"
						style="width: 65px;" onclick="clearBatchInvoice()"> <input
						type="button" id="cancelBIButton" name="cancelBIButtonName"
						value="Cancel" class="cancelhoverbutton turbo-blue"
						style="width: 65px;" onclick="cancelBAtchInvFun()"></td>
				</tr>
			</table>

			</td>
			</tr>
			</table>
			<br>

		</form>
	</div>
	
	<div id="emailDetailsDialog">
	
	<div class="loadingDivBulkEmailAttachment" id="loadingDivForBulkEmail" style="display: none; opacity: 0.7; z-index: 1234; text-align: center; background-color: rgb(255, 255, 255);"></div>
			<table id="cInvoiceEmailGrid"></table>
			<div id="cInvoiceEmailGridPager"></div>
    <br>
    <input type="button"  id="selectCheckBox" value="Select All" class="turbo-blue savehoverbutton" onclick="selectAllCheckBox()" style="margin-left:50px; font-size: 15px;">
	<input type="button" value="Cancel" class="turbo-blue savehoverbutton" onclick="cancelEmail()" style="margin-left:700px; font-size: 15px;">
	<input type="button" value="Email" class="turbo-blue savehoverbutton" onclick="sendEmail()" style="margin-left: 30px;font-size: 15px;">	
	</div>

</body>
</html>