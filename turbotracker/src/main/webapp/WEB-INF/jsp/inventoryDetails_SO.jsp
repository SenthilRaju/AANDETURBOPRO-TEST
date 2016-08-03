
<div id="inventoryDetailsTab" style="display: none;">
	<label id="prMasterID"
		style="color: white; vertical-align: middle; display: none"></label> <label
		id="itemCode"
		style="color: white; vertical-align: middle; display: none"></label> <label
		id="prWarehouseInventoryId"
		style="color: white; vertical-align: middle; display: none"></label>
	<div id="inventoryDetailsForm">
		<form action="" id="inventoryDetailsFormId1">
			<div>
				<table align="center">
					<tr>
						<td align="center">
							<table>
								<tr>
									<td>
										<table style="width: 360px;">
											<!-- <tr><td></td><td></td><td align="center"><label><b>Quantity</b></label>&nbsp;</td><td align="center"><label><b>$ Value</b></label></td></tr>
													<tr><td width="50px"><label>On Hand:</label></td><td align="left"><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="On Hand"  onclick="onHand();"></td><td align="center"><label id="onHandProduct">0</label></td><td align="right"><label id="onHandAmount">$0.00</label></td></tr>
													<tr><td><label>Allocated:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Allocated"  onclick="allocated();"></td><td align="center"><label id="allocatedProduct" >0</label></td><td align="right"><label id="allocatedAmount" ></label></td></tr>
													<tr><td><label>Available:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Available" ></td><td align="center"><label id="availableProduct" >0</label></td><td align="right"><label id="availableAmount" >$0.00</label></td></tr>
													<tr><td><label>On Order:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="On Order"  onclick="onOrder();"></td><td align="center"><label id="onOrderProduct" >0</label></td><td align="right"><label id="onOrderAmount" ></label></td></tr>
													<tr><td><label>Submitted:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Submitted" ></td><td align="center"><label id="submittedProduct" >0</label></td><td align="right"><label id="submittedAmount" ></label></td></tr>
													<tr><td><label>YTD Sold:</label></td><td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="YTD Sold" ></td><td align="center"><label id="YTDSoldProduct" >0</label></td><td align="right"><label id="YTDSoldAmount" ></label></td></tr>
													 -->
											<tr>
												<td></td>
												<td></td>
												<td align="center"><label><b>Quantity</b></label>&nbsp;</td>
												<td align="center"><label><b>Warehouse Cost
													</b></label></td>
												<td align="right"><label id="onHandAmount">$0.00</label></td>
											</tr>
											<tr>
												<td width="50px"><label>On Hand:</label></td>
												<td align="left"><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="On Hand" onclick="onHand();"></td>
												<td align="center"><label id="onHandProduct">0</label></td>
											</tr>
											<tr>
												<td><label>Allocated:</label></td>
												<td><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="Allocated" onclick="allocated();"></td>
												<td align="center"><label id="allocatedProduct">0</label></td>
											</tr>
											<tr>
												<td><label>Available:</label></td>
												<td><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="Available"></td>
												<td align="center"><label id="availableProduct">0</label></td>
												<td align="right"></td>
											</tr>
											<tr>
												<td><label>On Order:</label></td>
												<td><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="On Order" onclick="onOrder();"></td>
												<td align="center"><label id="onOrderProduct">0</label></td>
											</tr>
											<tr>
												<td><label>Submitted:</label></td>
												<td><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="Submitted"></td>
												<td align="center"><label id="submittedProduct">0</label></td>
											</tr>
											<tr>
												<td><label>YTD Sold:</label></td>
												<td><img alt="search"
													src="./../resources/scripts/jquery-autocomplete/search.png"
													title="YTD Sold"></td>
												<td align="center"><label id="YTDSoldProduct">0</label></td>
											</tr>

										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>

				</table>


				<div id="jqgridInventoryDetails" style="display: none;">
					<fieldset class=" ui-widget-content ui-corner-all"
						style="width: 288px; height: 280px; margin-left: 26px;">
						<legend>
							<label><b>Allocated Details</b></label>
						</legend>
						<table id="inventoryDetailsGrid"></table>
						<div id="inventoryDetailsPager"></div>
						<table>
							<tr>
						</table>
					</fieldset>
				</div>

				<div id="jqgridOnOrderDetails" style="display: none;">
					<fieldset class=" ui-widget-content ui-corner-all"
						style="width: 288px; height: 280px; margin-left: 26px;">
						<legend>
							<label><b>On Order Details</b></label>
						</legend>
						<table id="OnOrderDetailsGrid"></table>
						<div id="OnOrderDetailsPager"></div>
					</fieldset>
					<input type="hidden" id="rxCustomer_ID" name="rxCustomer_ID">
					<input type="hidden" id="Cuso_ID" name="Cuso_ID"> <input
						type="hidden" id="operation" name="operation">
				</div>



				<div id="jqgridOnHandDetails" style="display: none;">
					<fieldset class=" ui-widget-content ui-corner-all"
						style="width: 288px; height: 280px; margin-left: 26px;">
						<legend>
							<label><b>On Hand Details</b></label>
						</legend>
						<table id="OnHandDetailsGrid"></table>
						<div id="OnHandDetailsPager"></div>
						<table>
						</table>
					</fieldset>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript"
	src="./../resources/scripts/turbo_scripts/inventoryDetails_SO.js"></script>

<script type="text/javascript"
	src="./../resources/scripts/turbo_scripts/inventoryDetails_SO.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#inventoryDetailsTab").dialog({
		modal: true,
	    resizable: false,
	    title: 'title',
	    buttons: {
	       Ok: function() {
	          $(this).dialog('close');
	       } //end function for Ok button
	    }
	});
	$("#inventoryDetailsTab").dialog('close');
});
	function preloadInventoryDetail(productID) {
		if (productID == null) {
			$("#onHandAmount").empty();
			$("#onHandProduct").empty();
			$("#allocatedProduct").empty();
			$("#onOrderProduct").empty();
			$("#submittedProduct").empty();
			$("#availableProduct").empty();
			$("#YTDSoldProduct").empty();
			jQuery('#OnHandDetailsGrid').jqGrid('clearGridData');
		} else if (typeof productID === "undefined") {
			$("#onHandAmount").empty();
			$("#onHandProduct").empty();
			$("#allocatedProduct").empty();
			$("#onOrderProduct").empty();
			$("#submittedProduct").empty();
			$("#availableProduct").empty();
			$("#YTDSoldProduct").empty();
			jQuery('#OnHandDetailsGrid').jqGrid('clearGridData');
		} else {
			jQuery('#OnHandDetailsGrid').jqGrid('clearGridData');
			$
					.ajax({
						url : "./inventoryList/getInventoryDetails",
						type : "POST",
						async : false,
						data : {
							"prMasterID" : productID
						},
						success : function(prMasterDetails) {
							$('#OnHandDetailsGrid').trigger( 'reloadGrid' );
							$("#prMasterID").text(productID);
							if (prMasterDetails != null) {
								var prDescription = prMasterDetails.description;
								var onHandAmt = prMasterDetails.inventoryOnHand;
								var factoryCost = prMasterDetails.lastCost;
								var multiplier = prMasterDetails.POMult;
								var onorder = prMasterDetails.inventoryOnOrder;
								var avgCost = prMasterDetails.averageCost;
								var orderquantity = parseFloat(onorder)
										* parseFloat(avgCost);
								var invalloc = prMasterDetails.inventoryAllocated;
								var allocamount = parseFloat(invalloc)
										* parseFloat(avgCost);
								if (allocamount == null
										|| allocamount == "undefined"
										|| allocamount == ""
										|| isNaN(allocamount)) {
									allocamount = parseFloat(0);
								}

								var prWarehouseCost = prMasterDetails.averageCost_New;
								$("#onHandAmount").empty();
								$("#onHandAmount")
										.append("$" + formatCurrencynodollar(prWarehouseCost));
								//alert('prWarehouseCost :1 '+prWarehouseCost);
								//$("#onHandProduct").text("$"+prWarehouseCost);
								/* $("#allocatedAmount").text(formatCurrency(allocamount));
								$('#whseCostAmount').text(formatCurrency(${requestScope.prWarehouseCost}));
								
								if(isNaN(orderquantity)){
									$("#onOrderAmount").text(formatCurrency(0.00));
								}else{
								 	var onorder_amount=parseFloat(onorder)*parseFloat(avgCost);
								 	if(isNaN(onorder_amount)){
								 		$("#onOrderAmount").text(formatCurrency(0.00));
									 	}else{
									 		$("#onOrderAmount").text(formatCurrency(onorder_amount));	
										 	}
									
								} */

								var averageCost = prMasterDetails.averageCost;
								var aInventoryOnHand = prMasterDetails.inventoryOnHand;
								var aInventoryAllocated = prMasterDetails.inventoryAllocated;
								var aInventoryOnOrder = prMasterDetails.inventoryOnOrder;
								var aSubmitted = prMasterDetails.submitted;
								/* onHandAmt=parseFloat(avgCost)*parseFloat(aInventoryOnHand);

								if(isNaN(onHandAmt) || onHandAmt==="undefined"){
									$("#onHandAmount").text(formatCurrency(0));
									}
								else{
									$("#onHandAmount").text(formatCurrency(onHandAmt));
								} */

								if (aInventoryOnHand !== "") {
									$("#onHandProduct").empty();
									$("#onHandProduct")
											.append(aInventoryOnHand);
								}
								if (aInventoryAllocated !== "") {
									$("#allocatedProduct").empty();
									$("#allocatedProduct").append(
											aInventoryAllocated);
								}
								if (aInventoryOnOrder !== "") {
									$("#onOrderProduct").empty();
									$("#onOrderProduct").append(
											aInventoryOnOrder);
								}
								if (aSubmitted !== "") {
									$("#submittedProduct").empty();
									$("#submittedProduct").append(aSubmitted);
								}
								$("#availableProduct").empty();
								if (Number(aInventoryAllocated) > 0) {
									$("#availableProduct").append(
											aInventoryOnHand
													- aInventoryAllocated);
								} else {
									$("#availableProduct").append(
											aInventoryOnHand);
								}
								/* $("#availableAmount").empty();
								if(Number(aInventoryAllocated)>0){
									$("#availableAmount").append(formatCurrency(Number(aInventoryOnHand-aInventoryAllocated)*Number(averageCost)));
								}else{
									$("#availableAmount").append(formatCurrency(Number(aInventoryOnHand)*Number(averageCost)));
								} */
							}
							onHand();
						},
						error : function(jqXHR, textStatus, errorThrown) {
							return false;
						}
					});
		}
	}

	/* function getprcategorymarkup(prcategoryid){
		 
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
	 } */
	function allocated() {
		//$('#tierPricingDetails').hide();
		$('#jqgridOnOrderDetails').hide();
		$('#jqgridOnHandDetails').hide();
		$('#jqgridInventoryDetails').show();	
		//$('.tabs_main').css("height","1054px");
		loadInventoryDetailsGrid();
	}

	function onOrder() {
		//$('#tierPricingDetails').hide();
		$('#jqgridInventoryDetails').hide();
		$('#jqgridOnHandDetails').hide();
		$('#jqgridOnOrderDetails').show();
		//$('.tabs_main').css("height","1054px");
		loadOnOrderDetailsGrid();
	}

	function onHand() {
		//$('#tierPricingDetails').hide();
		$('#jqgridInventoryDetails').hide();
		$('#jqgridOnOrderDetails').hide();
		$('#jqgridOnHandDetails').show();
		loadOnHandDetailsGrid();
	}
</script>

