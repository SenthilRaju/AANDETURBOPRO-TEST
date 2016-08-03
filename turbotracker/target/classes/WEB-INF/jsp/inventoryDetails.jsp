<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Inventory Details</title>
		<link rel="SHORTCUT ICON" href="./../resources/Icons/TurboRepIcon.png">
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainmenuInventoryPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
            #mainmenuInventoryPage:first-child {background:url('./../resources/styles/turbo-css/images/home_white_inventory.png') no-repeat 0px 4px;color:#FFF}
			.reset_button {
				background: url('./../resources/Icons/reset.png') no-repeat scroll 4px 4px/* , url('./../resources/styles/turbo-css/images/turbopro-blue-gradient-btn.png') repeat-x scroll 0 0 transparent */; 
				border: 0 none; 
				border-radius: 5px 5px 5px 5px; 
				cursor: pointer; 
				height: auto; 
				padding: 4px 5px;
				width: 20px;
			}
			.resetAvgCostGoClass {
				
			}
			.resetAvgCostCancelClass {
				
			}
		</style>
	</head>
	<body>
	<div  style="background-color: #FAFAFA">
	<div>
		 <jsp:include page="./headermenu.jsp"></jsp:include> 
	</div>
	<div id="inventoryDetailsTab">
		<div class="tabs_main" style="padding-left: 0px;width:1050px;margin:0 auto; background-color: #FAFAFA;height:1054px;">
			<ul>
				<li><a href="#inventoryDetailsForm">Inventory Details</a></li>
				<li style="float: right; padding-right: 10px; padding-top:3px;">
					<label id="prMasterID" style="color: white;vertical-align: middle;display: none">${requestScope.prMasterDetails.prMasterId}</label>
					<label id="itemCode" style="color: white;vertical-align: middle;display: none"></label>
					<label id="prWarehouseInventoryId" style="color: white;vertical-align: middle;display: none">${requestScope.prWareHouseDetails.prWarehouseInventoryId}</label>
					
					
				</li>
			</ul>
			<div id="inventoryDetailsForm">
			<form action="" id="inventoryDetailsFormId">
				<div>
					<table align="center">
						<tr>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 530px;height: 240px;">
									<legend><label><b>Product/Service</b></label></legend>
									<table style="width: 530px;">
										<tr>
											<td><label>Code:</label></td>
											<td><input type="text" name="codeName" id="codeId" style="width:230px" value="${requestScope.prMasterDetails.itemCode}">&nbsp;&nbsp;&nbsp;&nbsp;
											<label style="vertical-align: middle;">Inactive</label><input type="checkbox" id="inactiveboxIDBox" style="vertical-align: middle;"></td>
										</tr>
										<tr>
											<td><label>Description:</label></td>
											<td><input type="text" name="descriptionName" id="descriptionId" style="width:355px" value=""></td>
										</tr>
										<tr>
											<td><label>Department:</label><span style="color: red;">*</span></td>
												<td><select style="width:120px" id="departmentId" name="departmentName">
														<option value="-1"> - Select - </option>
														<c:forEach var="departmentBean" items="${requestScope.productDepartment}">
															<option value="${departmentBean.prDepartmentId}">
																<c:out value="${departmentBean.description}" ></c:out>
															</option>
														</c:forEach>
													</select>&nbsp;&nbsp;&nbsp;&nbsp;
												<label style="vertical-align: middle;">Inventory</label><input type="checkbox" id="inventoryIDBox" style="vertical-align: middle;" onclick="setInventoryItem()"/>&nbsp;&nbsp;&nbsp;&nbsp;
												<label style="vertical-align: middle;">Consignment</label><input type="checkbox" id="consignmentIDBox" style="vertical-align: middle;">
											</td>
										</tr>
										<tr>
											<td><label>Category:</label></td>
											<td><select style="width:120px" id="categoryId" name="categoryName" onchange="getprcategorymarkup(this.value)">
													<option value="-1"> - Select - </option>
													<c:forEach var="categoryBean" items="${requestScope.productCategory}">
														<option value="${categoryBean.prCategoryId}">
															<c:out value="${categoryBean.description}" ></c:out>
														</option>
													</c:forEach>
												</select>&nbsp;&nbsp;&nbsp;
												<div id="weightID" style="display: none">${requestScope.prMasterDetails.weight}</div>
												<label style="vertical-align: middle;">Box:&nbsp;</label><input type="text" name="boxName" id="boxId" class="validate[custom[number]]" style="width:35px;" value="${requestScope.prMasterDetails.boxQty}">&nbsp;
												<label style="vertical-align: middle;">Weight:&nbsp;</label><input type="text" name="weightName" class="validate[custom[number]]" id="weightId" value="" style="width:33px;" maxlength="4"><label style="vertical-align: middle;">Lbs</label>
												<input type="text" name="ounces" class="validate[custom[number]]" id="ouncesId" value="${requestScope.prMasterDetails.weight}" style="width:16px;" maxlength="2"><label style="vertical-align: middle;">oz</label>
											</td>
										</tr>
										<tr>
											<td><label>Bin # per Whse:</label></td>
											<td><select style="width:120px" name="binName" id="binId">
														<option value="-1"> - Select - </option>
														<c:forEach var="prWhareBean" items="${requestScope.prWhare}">
															<option value="${prWhareBean.prWarehouseId}">
																<c:out value="${prWhareBean.searchName}" ></c:out>
															</option>
														</c:forEach>
													</select>&nbsp;&nbsp;
												<input type="text" name="bingeneralName" id="bingeneralId" style="width:150px" class="" value="${requestScope.prWareHouseDetails.bin}">
											</td>
										</tr>
										<tr>
											<td><label>Printed on Forms:</label></td>
											<td>
												<label style="vertical-align: middle;">PO</label><input type="checkbox" id="poIDBox" style="vertical-align: middle;">&nbsp;&nbsp;&nbsp;
												<label style="vertical-align: middle;">SO</label><input type="checkbox" id="soIDBox" style="vertical-align: middle;">&nbsp;&nbsp;&nbsp;
												<label style="vertical-align: middle;">Invoice</label><input type="checkbox" id="invoiceIDBox" style="vertical-align: middle;">&nbsp;&nbsp;&nbsp;
												<label style="vertical-align: middle;">Pick Ticket</label><input type="checkbox" id="pickTicketIDBox" style="vertical-align: middle;">
											</td>
										</tr>
										<tr>
											<td><label>Product Web Page:</label></td>
											<td>
												<input type="text" name="productSiteName" id="productSiteId" style="width:230px" value="${requestScope.prMasterDetails.webSite}">
												<a class="alink" style="color: #4A5F71; text-decoration: underline;" id="Pro_URL" onclick="loadProductURL('${requestScope.prMasterDetails.webSite}')" href="#">Go</a>
											</td>
										</tr>
										<tr><td colspan="2"><div style="" id="UrlValidationMsg"></div></td></tr>
									</table>
								</fieldset>
							</td>
							<td>
								<table>
									<tr>
										<td>
											<fieldset class= " ui-widget-content ui-corner-all" style="width: 370px;height: 148px;">
												<legend><label><b>Inventory</b></label></legend>
												<table style="width: 360px;">
													<tr><td></td><td></td><td align="center"><label><b>Quantity</b></label>&nbsp;</td><td align="center"><label><b>$ Value</b></label></td></tr>
													<tr><td width="50px"><label>On Hand:</label></td><td align="left"><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="On Hand"  onclick="onHand();"></td><td align="center"><label id="onHandProduct">0</label></td><td align="right"><label id="onHandAmount">$0.00</label></td></tr>
													<tr><td><label>Allocated:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Allocated"  onclick="allocated();"></td><td align="center"><label id="allocatedProduct" >0</label></td><td align="right"><label id="allocatedAmount" ></label></td></tr>
													<tr><td><label>Available:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Available" ></td><td align="center"><label id="availableProduct" >0</label></td><td align="right"><label id="availableAmount" >$0.00</label></td></tr>
													<tr><td><label>On Order:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="On Order"  onclick="onOrder();"></td><td align="center"><label id="onOrderProduct" >0</label></td><td align="right"><label id="onOrderAmount" ></label></td></tr>
													<tr><td><label>Submitted:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Submitted" ></td><td align="center"><label id="submittedProduct" >0</label></td><td align="right"><label id="submittedAmount" ></label></td></tr>
													<tr><td><label>YTD Sold:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="YTD Sold" ></td><td align="center"><label id="YTDSoldProduct" >0</label></td><td align="right"><label id="YTDSoldAmount" ></label></td></tr>
													</table>
											</fieldset>
										</td>
									</tr>
									<tr>
										<td>
											<fieldset  class= " ui-widget-content ui-corner-all" style="width: 370px;height: 77px;">
												<legend><label><b>Purchasing</b></label></legend>
												<table style="width: 360px;">
													<tr>
														<td align="left" style="width:150px"><label class="">Primary Vendor:</label></td>
														<td><input  type="text" id="primaryVendorId" name="primaryVendorName" style="width: 180px;" value="${requestScope.prMasterDetails.vendorName}"  placeholder="Minimum 3 characters required"></td>
														<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
														<td><input type="hidden" id="vendorId" name="vendorName" value="${requestScope.prMasterDetails.rxMasterIdprimaryVendor}" style="width: 30px;"></td>
													</tr>
													<tr>
														<td align="left" style="width:150px"><label class="">Vendor Item Code:</label></td>
														<td><input  type="text" id="VendorItemCodeId" name = "vendorItemCodeName" style="width: 180px;" value="${requestScope.prMasterDetails.vendorItemNumber}"></td>
													</tr>
												</table>
											</fieldset>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="1" style="vertical-align: top;">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 530px;height: 110px;">
									<legend><label><b>Sales</b></label></legend>
									<table style="width: 530px;">
										<tr>
											<td style="width: 140px;"><label>Base Selling Price:</label></td>
											<td><input type="text" name="sellingPriceName" id="sellingPriceId" style="width:180px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice00}"></td>
										</tr>
										<tr>
											<td style="width: 140px;"><label>Taxable:</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="taxableIDBox" style="vertical-align: middle;"></td>
											<td><label style="vertical-align: middle;">Single Item Tax:</label>&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" id="singleItemIDBox" style="vertical-align: middle;">
											</td>
										</tr>
									</table>
									<table>
										<tr>
											<td><label>Profit Margin:</label></td><td style="width: 100px;"><label id="profitMarginId"></label><!-- <input type="text" name="profitMarginName" disabled="disabled"  style="width:70px" > --></td>
											<td><label>Pct:</label><td style="width: 100px;"><label id="pctID"></label><!--  <input type="text" name="pctIDName" disabled="disabled"  style="width:70px" > --></td>
										</tr>
									</table>
								</fieldset>
							</td>
							<td>
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 370px;">
									<legend><label><b>Average Cost</b></label></legend>
									<table style="width: 350px;">
										<tr>
											<td align="center"></td>
											<td align="center"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Secondary Vendors" class="savehoverbutton turbo-blue" style="width: 150px;"></td>
										</tr>
										<tr>
											<td style="width: 150px;">
												<label>Avg. Cost:</label><label id="avgCostId">$0.00</label>
												<!-- <input type="button" id="resetAvgCostForm" name="resetAvgCostButtonName" title="Reset Average Cost" value="" class="reset_button" style="" onclick="openResetAvgCostForm()"> -->
											</td>
											<td style="width: 150px;"><label>Whse Cost:</label><label id="whseCostAmount">$0.00</label></td>
										</tr>
										<tr>
											<td>
												<div id="resetFields" style="display: none">
													<input type="text" id="newAvgCostInput" style="width: 50px; margin-right: 3px;">
													<input type="button" class="savehoverbutton turbo-blue resetAvgCostGoClass" style="margin-right: 3px; padding: 4px 0px;" value="Go" id="go_ResetAvgCost" onclick="resetAvgCost()">
													<input type="button" class="savehoverbutton turbo-blue resetAvgCostCancelClass" style="padding: 4px 0px;" value="Cancel" id="cancel_ResetAvgCost" onclick="cancelReset()">
												</div>
											</td>
											<td></td>
										</tr>
										<tr>
											<td><label>Multiplier:&nbsp;</label><input type="text" name="multiplierName" id="multiplierId" class="validate[custom[number]]" style="width:60px" value="${requestScope.prMasterDetails.POMult}"></td>
											<td><label>Factory Cost:&nbsp;</label><input type="text" name="factoryCostName" id="factoryCostId" style="width:60px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.lastCost}"></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
					<!-- ID# 78  CommentedBy : Naveed
					 <table align="center">
						<tr>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 930px;height: 50px;">
									<legend><label><b>Product Attributes</b></label></legend>
									<table style="width: 930px;">
										<tr>
											<td align="left" style="width: 150px;"><label>Labor?</label><input type="checkbox" id="labloyrIDBox" style="vertical-align: middle;"></td>
											<td align="center" style="width: 250px;"><label>Auto Build Sub Assemblies?</label><input type="checkbox" id="assembliesIDBox" style="vertical-align: middle;"></td>
											<td align="right" style="width: 250px;"><label>Serialized Inventory?</label><input type="checkbox" id="serializedIDBox" style="vertical-align: middle;"></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table> -->
					<table align="center">
						<tr>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;height: 70px;">
									<legend><label><b>Popup when selected on Sales Order</b></label></legend>
									<table style="width: 288px;">
										<tr>
											<td><textarea rows="2" cols="32" id="salesOrderSelectedId" name="salesOrderSelectedName">${requestScope.prMasterDetails.SOPopup}</textarea>
										</tr>
									</table>
								</fieldset>
							</td>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;height: 70px;">
									<legend><label><b>Popup when selected on Invoice</b></label></legend>
									<table style="width: 288px;">
										<tr>
											<td><textarea rows="2" cols="32" id="invoiceSelectedId" name="invoiceSelectedName">${requestScope.prMasterDetails.CUPopup}</textarea>
										</tr>
									</table>
								</fieldset>
							</td>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 302px;height: 70px;">
									<legend><label><b>Popup when selected on Purchase Order</b></label></legend>
									<table style="width: 302px;">
										<tr>
											<td><textarea rows="2" cols="32" id="purchaseOrderSelectedId" name="purchaseOrderSelectedName">${requestScope.prMasterDetails.POPopup}</textarea>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
					
					<div id="jqgridInventoryDetails" style="display: none;">
					<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;height: 280px;margin-left: 26px;">
									<legend><label><b>Allocated Details</b></label></legend>
					<table id="inventoryDetailsGrid"></table>
					<div id="inventoryDetailsPager"></div>
					<table>
					<tr>
					<!--
						<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" onclick="showSOPopup();"></td>
						 <td></td>
						<td><input type="button" id='showpricetier' name='showpricetier' value="Resume Tier Pricing" onclick="showPriceTier();"> </td>
					</tr> -->
					</table>
					</fieldset>
					<input type="hidden" id="rxCustomer_ID" name="rxCustomer_ID"> 
					<input type="hidden" id="Cuso_ID" name="Cuso_ID">
					<input type="hidden" id="operation" name="operation">
					</div>
					<div id="jqgridOnOrderDetails" style="display: none;">
					<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;height: 280px;margin-left: 26px;">
									<legend><label><b>On Order Details</b></label></legend>
					<table id="OnOrderDetailsGrid"></table>
					<div id="OnOrderDetailsPager"></div>
					<table>
					<!-- <tr>
						<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" onclick="showPOPopup();"></td>
						 <td></td>
						<td><input type="button" id='showpricetier' name='showpricetier' value="Resume Tier Pricing" onclick="showPriceTier();"> </td>
					</tr> -->
					</table>
					</fieldset>
					<input type="hidden" id="rxCustomer_ID" name="rxCustomer_ID"> 
					<input type="hidden" id="Cuso_ID" name="Cuso_ID">
					<input type="hidden" id="operation" name="operation">
					</div>
					<div id="jqgridOnHandDetails" style="display: none;">
					<fieldset  class= " ui-widget-content ui-corner-all" style="width: 288px;height: 280px;margin-left: 26px;">
									<legend><label><b>On Hand Details</b></label></legend>
					<table id="OnHandDetailsGrid"></table>
					<div id="OnHandDetailsPager"></div>
					<table>
					<!-- <tr>						
						<td><input type="button" id='showpricetier' name='showpricetier' value="Resume Tier Pricing" onclick="showPriceTier();"> </td>
					</tr> -->
					</table>
					</fieldset>
					<input type="hidden" id="rxCustomer_ID" name="rxCustomer_ID"> 
					<input type="hidden" id="Cuso_ID" name="Cuso_ID">
					<input type="hidden" id="operation" name="operation">
					</div>
					<table align="center" id="tierPricingDetails">
						<tr>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 946px;height: 145px;">
									<legend><label><b>Tier Pricing</b></label></legend>
									<table style="width: 930px;">
										<tr>
											<td align="left" style="width: 150px;"><label>Quantity Breaks:</label>
											<input type="text" name="QuantityName" id="QuantityID" style="width:70px"  class="validate[custom[number]]"  value="${requestScope.prMasterDetails.quantityBreak0}">&nbsp;&nbsp;&nbsp;&nbsp;<label>Over:</label>
											<input type="text" id="overID" name="overName"  style="width: 50px;" class="validate[custom[number]]"  value="${requestScope.prMasterDetails.quantityBreak1}"></td>
										</tr>
										<tr>
											<td align="center">
												<hr style="width:910px;">
											</td>
										</tr>
									</table>
									<table>
										<tr>
											<td style="width: 80px;"><label id="prPriceLevel0">${requestScope.prPriceLevel0}</label></td><td style="width: 120px;"><input type="text" name="RetailName" id="RetailId" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice01}"></td><td style="width: 80px;"> <label id="retailPercent" ></label></td>
											<td style="width: 80px;"><label id="prPriceLevel1">${requestScope.prPriceLevel1}</label></td><td style="width: 120px;"><input type="text" name="wholeSaleName" id="wholeSaleId" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice30}"></td><td> <label id="wholeSalePercent" style="width: 80px;"></label></td>
										</tr>
										<tr>
											<td style="width: 80px;"><label id="prPriceLevel2">${requestScope.prPriceLevel2}</label></td><td style="width: 120px;"><input type="text" name="dealerName" id="dealerId" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice10}"></td><td> <label id="dealerPercent" style="width: 80px;"></label></td>
											<td style="width: 80px;"><label id="prPriceLevel4">${requestScope.prPriceLevel4}</label></td><td style="width: 120px;"><input type="text" name="distributorName" id="distributorId" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice20}"></td><td><label id="distPercent" style="width: 80px;"></label></td>
											
										</tr>
										<tr>
											<td style="width: 100px;"><label id="prPriceLevel3">${requestScope.prPriceLevel3}</label></td><td style="width: 120px;"><input type="text" name="special1Name" id="special1Id" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice40}"></td><td><label id="special1Percent" style="width: 80px;"></label></td>
											<td style="width: 80px;"><label id="prPriceLevel5">${requestScope.prPriceLevel5}</label></td><td style="width: 120px;"><input type="text" name="special2Name" id="special2Id" style="width:100px"  class="validate[custom[number]]" value="${requestScope.prMasterDetails.salesPrice50}"></td><td><label id="sp2Percent" style="width: 80px;"></label></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
					<table align="center" id="secondaryVendorDetails">
						<tr>
							<td colspan="1">
								<fieldset  class= " ui-widget-content ui-corner-all" style="width: 930px;height: 165px;">
									<legend><label><b>Purchasing:Secondary Vendors</b></label></legend>
									<table style="width: 930px;">
										<tr>
											<td align="left" style="width: 150px;">
											<input type="text" name="secondaryVendorName" id="secondaryVendor1ID"  style="width:250px"  class="validate[custom[number]]"  placeholder="Minimum of 3 character to search" />&nbsp;&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;&nbsp;<label>Vendor Item Code:</label>
											<input type="text" id="vendorItemCodeID1" name="vendorItemCode"  style="width: 250px;" class="validate[custom[number]]" /></td>
										</tr>
										<tr>
											<td align="left" style="width: 150px;">
											<input type="text" name="secondaryVendorName" id="secondaryVendor2ID" placeholder="Minimum of 3 character to search" style="width:250px"  class="validate[custom[number]]"  />&nbsp;&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;&nbsp;<label>Vendor Item Code:</label>
											<input type="text" id="vendorItemCode2ID" name="vendorItemCode"  style="width: 250px;" class="validate[custom[number]]" /></td>
										</tr>
										<tr>
											<td align="left" style="width: 150px;">
											<input type="text" name="secondaryVendorName" id="secondaryVendor3ID" placeholder="Minimum of 3 character to search" style="width:250px"  class="validate[custom[number]]" />&nbsp;&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;&nbsp;<label>Vendor Item Code:</label>
											<input type="text" id="vendorItemCode3ID" name="vendorItemCode"  style="width: 250px;" class="validate[custom[number]]" /></td>
										</tr>
										<tr>
											<td align="left" style="width: 150px;">
											<input type="text" name="secondaryVendorName" id="secondaryVendor4ID" placeholder="Minimum of 3 character to search" style="width:250px"  class="validate[custom[number]]" />&nbsp;&nbsp;<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;&nbsp;<label>Vendor Item Code:</label>
											<input type="text" id="vendorItemCode4ID" name="vendorItemCode"  style="width: 250px;" class="validate[custom[number]]"  /></td>
										</tr>
										<tr>
											<td align="center">
												<hr style="width:910px;">
											</td>
										</tr>
										<tr>
										<!-- <td>
										<label> Resume Tier Pricing</label>
										</td> -->
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table>
					<table align="center">
						<tr>
							<td>
								<hr style="width:950px;">
							</td>
						</tr>
					</table>
					<table align="right" style="padding-right: 45px;">
					<tr><td align="right"><label id="Inventoryerrordiv" style="color: green;"></label><label id="Inventoryvalidatediv" style="color: red;"></label></td></tr>

						<tr>
							<td>
								<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="saveInventoryDetails()">
								<input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" style="width: 135px;" onclick="updateInventoryDetails()">
								<input type="button" id="deleteInventory" name="saveUserButtonName" value="Delete" class="savehoverbutton turbo-blue" style="width: 80px;" onclick="deleteInventoryDetails()">
							</td>
						</tr>
						
					</table>
				</div>
			</form>
		</div>
		</div>
	</div>
	<div style="padding-top: 26px; background-color: #FAFAFA">
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
		</table>
	</div>
	</div>	
	<%-- <div><jsp:include page="./salesOrder.jsp"></jsp:include></div> --%>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/salesOrderList.js"></script>	
	<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inventoryDetails.js"></script>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			onHand();
			$('#tierPricingDetails').show();
			var prDescription = "${requestScope.prMasterDetails.description}";	
			$("#descriptionId").val(prDescription.replace(/``/g, "\""));
			<%-- 
		 	
		 	//$("#salesOrderSelectedId").val("${requestScope.prMasterDetails.SOPopup}");
		 	//$("#invoiceSelectedId").val("${requestScope.prMasterDetails.CUPopup}");
		 	//$("#purchaseOrderSelectedId").val("${requestScope.prMasterDetails.POPopup}");

			--%>
		 	
		 	var onHandAmt = "${requestScope.prMasterDetails.inventoryOnHand}";
		 	//onHandAmt=parseFloat(onHandAmt);
		 	var factoryCost = "${requestScope.prMasterDetails.lastCost}";
		 	//factoryCost=parseFloat(factoryCost);
		 	var multiplier = "${requestScope.prMasterDetails.POMult}";
		 	//multiplier=parseFloat(multiplier).toFixed(2);
		 	var onorder = "${requestScope.prMasterDetails.inventoryOnOrder}";		 	
		 	var avgCost = "${requestScope.prMasterDetails.averageCost}";/* "${requestScope.prMasterDetails.lastCost}"; */
		 	//avgCost=parseFloat(avgCost).toFixed(2);
		 	var orderquantity = parseFloat(onorder)*parseFloat(avgCost);
		 	var invalloc="${requestScope.prMasterDetails.inventoryAllocated}";
		 	var allocamount=parseFloat(invalloc)*parseFloat(avgCost);
		 	if(allocamount==null||allocamount=="undefined" || allocamount=="" || isNaN(allocamount)){
		 		allocamount=parseFloat(0);
			 	}
		 	$("#allocatedAmount").text(formatCurrency(allocamount));
		 	$('#whseCostAmount').text(formatCurrency(${requestScope.prWarehouseCost}));
		 	
		 	if(isNaN(orderquantity)){
		 		$("#onOrderAmount").text(formatCurrency(0.00));
		 	}else{
			 	/* if(onorder=="" || onorder==null ||isNaN(onorder)){
			 		onorder=0;
				 	}
			 	if(factoryCost=="" || factoryCost==null ||isNaN(factoryCost)){
			 		factoryCost=0;
				 	}
			 	if(multiplier=="" || multiplier==null ||isNaN(multiplier)){
			 		multiplier=0;
				 	} */
				 	var onorder_amount=parseFloat(onorder)*parseFloat(avgCost);
				 	if(isNaN(onorder_amount)){
				 		$("#onOrderAmount").text(formatCurrency(0.00));
					 	}else{
					 		$("#onOrderAmount").text(formatCurrency(onorder_amount));	
						 	}
		 		
		 	}
		 	
		 	$("#avgCostId").text(formatCurrency(avgCost));
		 	$("#departmentId option[value=" + '${requestScope.prMasterDetails.prDepartmentId}' + "]").attr("selected", true);
		 	$("#categoryId option[value=" + '${requestScope.prMasterDetails.prCategoryId}' + "]").attr("selected", true);
		 	/*var prcategoryId='${requestScope.prMasterDetails.prCategoryId}';
		 	 if(prcategoryId!="" && prcategoryId!=0 && prcategoryId!=null && prcategoryId!=-1){
		 		getprcategorymarkup(prcategoryId);
		 		} */
		 	$("#binId option[value=" + '${requestScope.prWareHouseDetails.prWarehouseId}' + "]").attr("selected", true);
		 	$('#secondaryVendorDetails').hide();
		 	var aActive = "${requestScope.prMasterDetails.inActive}";
		 	var aPOId = "${requestScope.prMasterDetails.printOnPos}";
		 	var aSOId = "${requestScope.prMasterDetails.printOnSos}";
		 	var aTax = "${requestScope.prMasterDetails.isTaxable}";
		 	var aInventory = "${requestScope.prMasterDetails.isInventory}";
		 	var aConsignment = "${requestScope.prMasterDetails.consignment}";
		 	var aInvoice = "${requestScope.prMasterDetails.printOnCUs}";
		 	var aPick = "${requestScope.prMasterDetails.printOnPTs}";
		 	var aSingle = "${requestScope.prMasterDetails.singleItemTax}";
		 	var aLabor = "${requestScope.prMasterDetails.labor}";
		 	var aAutoBuild = "${requestScope.prMasterDetails.autoAssemble}";
		 	var aSerialize = "${requestScope.prMasterDetails.serialized}";
		 	var averageCost = "${requestScope.prMasterDetails.averageCost}";
		 	var aInventoryOnHand = "${requestScope.prMasterDetails.inventoryOnHand}".replace(".0000", "");
		 	var aInventoryAllocated = "${requestScope.prMasterDetails.inventoryAllocated}".replace(".0000", "");
		 	var aInventoryOnOrder = "${requestScope.prMasterDetails.inventoryOnOrder}".replace(".0000", "");
		 	var aSubmitted = "${requestScope.prMasterDetails.submitted}".replace(".0000", "");
		 	//onHandAmt = Number(aInventoryOnHand)*Number(factoryCost)*Number(multiplier);
		 	onHandAmt=parseFloat(avgCost)*parseFloat(aInventoryOnHand);
		 	if(aActive === "1"){ 
		 		$("#inactiveboxIDBox").attr("checked", true);
		 	}
		 	if(aPOId === "1"){ 
		 		$("#poIDBox").attr("checked", true);
		 	}
		 	if(aSOId === "1"){ 
		 		$("#soIDBox").attr("checked", true);
		 	}
		 	if(aTax === "1"){ 
		 		$("#taxableIDBox").attr("checked", true);
		 	}
		 	if(aInventory === "1"){ 
		 		$("#inventoryIDBox").attr("checked", true);
		 	}
		 	if(aConsignment === "1"){ 
		 		$("#consignmentIDBox").attr("checked", true);
		 	}
		 	if(aInvoice === "1"){ 
		 		$("#invoiceIDBox").attr("checked", true);
		 	}
		 	if(aPick === "1"){ 
		 		$("#pickTicketIDBox").attr("checked", true);
		 	}
		 	if(aSingle === "1"){ 
		 		$("#singleItemIDBox").attr("checked", true);
		 	}
		 	if(aLabor === "1"){ 
		 		$("#labloyrIDBox").attr("checked", true);
		 	}
		 	if(aAutoBuild === "1"){ 
		 		$("#assembliesIDBox").attr("checked", true);
		 	}
		 	if(aSerialize === "1"){ 
		 		$("#serializedIDBox").attr("checked", true);
		 	}

		 	if(isNaN(onHandAmt) || onHandAmt==="undefined"){
		 		$("#onHandAmount").text(formatCurrency(0));
			 	}
		 	else{
		 		$("#onHandAmount").text(formatCurrency(onHandAmt));
		 	}
		 	
		 	if(aInventoryOnHand !== "") {$("#onHandProduct").empty(); $("#onHandProduct").append(aInventoryOnHand);}
		 	if(aInventoryAllocated !== "") {$("#allocatedProduct").empty();	 $("#allocatedProduct").append(aInventoryAllocated);}
		 	if(aInventoryOnOrder !== "") {$("#onOrderProduct").empty();$("#onOrderProduct").append(aInventoryOnOrder);}
		 	if (aSubmitted !== "") {$("#submittedProduct").empty(); $("#submittedProduct").append(aSubmitted);}
		 	$("#availableProduct").empty();
		 	if(aInventoryAllocated==null){
		 		aInventoryAllocated=0;
		 	}
		 	$("#availableProduct").append(aInventoryOnHand-aInventoryAllocated);
		 	$("#availableAmount").empty();
		 	if(Number(aInventoryAllocated)>0){
		 		$("#availableAmount").append(formatCurrency(Number(aInventoryOnHand-aInventoryAllocated)*Number(averageCost)));
		 	}else{
		 		$("#availableAmount").append(formatCurrency(Number(aInventoryOnHand)*Number(averageCost)));
		 	}
		 	if(Number("${requestScope.prMasterDetails.salesPrice00}" - avgCost) > 0){
		 		$("#profitMarginId").append(formatCurrency(Number("${requestScope.prMasterDetails.salesPrice00}" - avgCost)));
				var pct = Number(Number("${requestScope.prMasterDetails.salesPrice00}" - avgCost)/Number("${requestScope.prMasterDetails.salesPrice00}"))*100;
				pct = Math.round(pct);
				$("#pctID").append(pct + " %");
		 	} else {
		 		$("#profitMarginId").html().replace(/ \d/,"");
		 		$("#pctID").html().replace(/ \d/,"");
		 	}
		 	$('#saveUserButton').click(function(){
			 		if($('#tierPricingDetails').css('display') == 'none')
				 	{
				 		$('#secondaryVendorDetails').hide();
			 			$('#tierPricingDetails').show();
				 	}else{
			 			$('#secondaryVendorDetails').show();
			 			$('#tierPricingDetails').hide();
				 	}
			 	});
		 });
		function getprcategorymarkup(prcategoryid){
			 
			var avgcost="${requestScope.prMasterDetails.averageCost}";
			avgcost=convert2places(avgcost);
			var multi="${requestScope.prMasterDetails.POMult}";
			multi=convert2places(multi);
			//avgcost=parseFloat(avgcost)*parseFloat(multi);
		 	$.ajax({
		 		url: "./inventoryList/getprcategorymarkup",
		 		type: "POST",
		 		data : {"prcategoryid": prcategoryid},
		 		success: function(data) {
			 		if(data!=null){
				 		if(data.markupAmount>0){
					 		var markup=convert2places(data.markupAmount);
				 			avgcost=convert2places(avgcost*markup);
					 		}
				 		}
			 		$('#whseCostAmount').text(formatCurrency(avgcost));
			 		}
		 	});
		 }

		function setInventoryItem()
		{
		if($("#inventoryIDBox").is(':checked')){
			jQuery(newDialogDiv).html('<span><b style="color:green;">Do you really wish to change the Non-Inventory Item to Inventory ?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
			buttons:{
				"OK": function(){
					$("#inventoryIDBox").attr("checked", true);
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{
					$("#inventoryIDBox").attr("checked", false);
					jQuery(this).dialog("close");}}}).dialog("open");
			}
		else{
			jQuery(newDialogDiv).html('<span><b style="color:green;">Do you really wish to change the Inventory Item to Non-Inventory ?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
			buttons:{
				"OK": function(){
					$("#inventoryIDBox").attr("checked", false);
					jQuery(this).dialog("close");
				},
				Cancel: function ()	{
					$("#inventoryIDBox").attr("checked", true);
					jQuery(this).dialog("close");}}}).dialog("open");
			}
		}

		
		function allocated()
		{
			//$('#tierPricingDetails').hide();
			$('#jqgridOnOrderDetails').hide();
			$('#jqgridOnHandDetails').hide();
			$('#jqgridInventoryDetails').show();			
			$('.tabs_main').css("height","1054px");
			loadInventoryDetailsGrid();
		}

		function onOrder()
		{
			//$('#tierPricingDetails').hide();
			$('#jqgridInventoryDetails').hide();
			$('#jqgridOnHandDetails').hide();
			$('#jqgridOnOrderDetails').show();						
			$('.tabs_main').css("height","1054px");
			loadOnOrderDetailsGrid();
		}

		function onHand()
		{
			//$('#tierPricingDetails').hide();
			$('#jqgridInventoryDetails').hide();
			$('#jqgridOnOrderDetails').hide();
			$('#jqgridOnHandDetails').show();						
			$('.tabs_main').css("height","1054px");
			loadOnHandDetailsGrid();
		}
	</script>
</body>
</html>