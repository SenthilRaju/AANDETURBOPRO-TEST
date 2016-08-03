<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="soreleasegeneral">
	<table>
		<tr>
			<td>
				
				<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
							<legend class="custom_legend"><label><b>Customer </b></label></legend>
								<label>Customer Name :</label> <input type="text" disabled="disabled" id="CustomerNameGeneral" />
								</fieldset>
						</td>
						<td>
						<fieldset class= "custom_fieldset" style="height: 45px;width: 350px;">
						<legend class="custom_legend"><label><b>SO Details</b></label></legend>
							<label>Date:</label><input type="text" id="dateOfcustomerGeneral" value="" style="width: 85px;margin-left: 5px;"/>
							<label>Number</label><input type="text" disabled="disabled" id="SOnumberGeneral" style="width: 126px; margin-left: 5px;"/>
						</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
			<table>
					<tr>
						<td>
							<fieldset class= "custom_fieldset" style="height: 150px;width: 350px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table>
										<tr>
					 						<td><input type="text" id="SOlocationbillToAddressname" name="locationbillToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="SOGenerallocationbillToAddressID1" name="locationbillToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="SOGenerallocationbillToAddressID2" name="locationbillToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="SOGenerallocationbillToCity" name="locationbillToCity" style="width: 100px;" disabled="disabled">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="SOGenerallocationbillToState" name="locationbillToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled">
					 								<label>Zip: </label><input type="text" id="SOGenerallocationbillToZipID" name="locationbillToZip" style="width: 75px;" disabled="disabled">
					 								<input type="button" id="SObillbackWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="SObillforWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
					 						</td>
					 					</tr>
					 				</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "custom_fieldset" style="height: 150px;width: 350px;">
								<legend class="custom_legend"><label><b>Ship To</b></label></legend>
								<input type="text" id="addressID" style="display: none;">
								<table id="regularTable">
									<tr>
					 					<td><input type="text" id="SOlocationShipToAddressID" name="locationShipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
					 				</tr>
					 				<tr>
					 					<td><input type="text" id="SOlocationShipToAddressID1" name="locationShipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
					 				</tr>
					 				<tr>
										<td><input type="text" id="SOlocationShipToAddressID2" name="locationShipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
									</tr>
				 					<tr>
				 						<td><input type="text" id="SOlocationShipToCity" name="locationShipToCity" style="width: 100px;" disabled="disabled">
												<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
				 								<input type="text" id="SOlocationShipToState" name="locationShipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
				 								<label>Zip: </label><input type="text" id="SOlocationShipToZipID" name="locationShipToZip" style="width: 75px;" disabled="disabled">
				 								<input type="button" id="SObackWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="SOforWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
				 						</td>
				 					</tr>
									<tr align="left">
										<td style="vertical-align: bottom;padding-left: 50px;">
										<table>
					 					<tr align="center">
											<td id="radioSett" style="vertical-align: bottom;padding-left: 0px; height: 10px">
												<div id="SOShiptoRadioSet">
													<input type="radio" id="SOshiptoradio1" name="radio" /><label for="shiptoradio1" id="shiptolabel1" onclick="pickupAddress()" style="width: 63px; margin-right: -6px;">Pick up</label>
													<input type="radio" id="SOshiptoradio2" name="radio" /><label for="shiptoradio2" id="shiptolabel2" onclick="CustomerAddress()" style="margin-right: -6px;">Customer</label>
													<input type="radio" id="SOshiptoradio3" name="radio" /><label for="shiptoradio3" id="shiptolabel3" onclick="shipToAddress()" style="margin-right: -6px;">Job Site</label>
													<input type="radio" id="SOshiptoradio4" name="radio" /><label for="shiptoradio4" id="shiptolabel4" onclick="otherShiptoAddress()">Other</label>
												</div>
											</td>
										</tr>
					 				</table>
										</td>
									</tr>
				 				</table>
				 				<table id ="hiddenTable" style="display: none;">
									<tr>
					 					<td><input type="text" id="SOlocationShipToAddressIDeditable" name="locationShipToAddressID" class="validate[maxSize[100]" style="width: 300px;" ></td>
					 				</tr>
					 				<tr>
					 					<td><input type="text" id="SOlocationShipToAddressID1editable" name="locationShipToAddress1" class="validate[maxSize[100]" style="width: 300px;" ></td>
					 				</tr>
					 				<tr>
										<td><input type="text" id="SOlocationShipToAddressID2editable" name="locationShipToAddress2" class="validate[maxSize[40]" style="width: 300px;"></td>
									</tr>
				 					<tr>
				 						<td><input type="text" id="SOlocationShipToCityeditable" name="locationShipToCity" style="width: 100px;" >
												<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
				 								<input type="text" id="SOlocationShipToState" name="locationShipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  >
				 								<label>Zip: </label><input type="text" id="SOlocationShipToZipIDeditable" name="locationShipToZip" style="width: 75px;" >
				 						</td>
				 					</tr>
									<tr align="left">
										<td style="vertical-align: bottom;padding-left: 50px;">
										<table>
					 					<tr align="center">
											<td id="radioSet" style="vertical-align: bottom;padding-left: 20px; height: 10px">
												<div id="SOShiptoRadioSet1">
													<input type="radio" id="SOshiptoradio01" name="radio" /><label for="shiptoradio1" id="shiptolabel1" onclick="pickupAddress()" style="width: 63px; margin-right: -6px;">Pick up</label>
													<input type="radio" id="SOshiptoradio02" name="radio" /><label for="shiptoradio2" id="shiptolabel2" onclick="CustomerAddress()" style="margin-right: -6px;">Customer</label>
													<input type="radio" id="SOshiptoradio03" name="radio" /><label for="shiptoradio3" id="shiptolabel3" onclick="addressToShip()" style="margin-right: -6px;">Job Site</label>
													<input type="radio" id="SOshiptoradio04" name="radio" /><label for="shiptoradio4" id="shiptolabel4" onclick="otherShiptoAddress()">Other</label>
												</div>
											</td>
										</tr>
					 				</table>
										</td>
									</tr>
				 				</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table>
					<tr>
						<td>
							<fieldset class=" custom_fieldset" style="height: 160px;width: 350px;">
								<legend class="custom_legend"><label><b>Employee Assigned :</b></label></legend>
								<table>
									<tr>
										<td><label>SalesMan:</label></td>
										<td><input type="text" style="width: 190px;" id="salesmanID" name="salesName" value="" placeholder="Minimun 2 character to search"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										</td>
										<td><input type="hidden" style="width: 50px;" id="salesmanhiddenID" name="saleshiddenName" value="${requestScope.joMasterDetail.cuAssignmentId0}">
										</td>
									</tr>
									<tr>
							         	<td><label>AE:</label></td><td><input type="text" style="width: 190px;" id="csrID" name="csrName" value="${requestScope.CSRs}" placeholder="Minimun 2 character to search"></td>
							         	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
							         	</td>
							         	<td><input type="hidden" style="width: 50px;" id="csrhiddenID" name="csrhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId1}"></td>
							         </tr>
							         <tr>
										<td><label>Costing:</label></td>
										<td><input type="text" style="width: 190px;" id="salesManagerID" name="salesManagerName" value="${requestScope.SalesMGRs}" placeholder="Minimun 2 character to search"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										</td>
										<td><input type="hidden" style="width: 50px;" id="salesManagerhiddenID" name="salesManagerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId2}">
										</td>
									</tr>
									<tr>
								     	<td><label>Submitting:</label></td>
								     	<td><input type="text" style="width: 190px;" id="engineerID" name="engineerName" value="${requestScope.Engineers}" placeholder="Minimun 2 character to search"></td>
								     	<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
								     	</td>
								     	<td><input type="hidden" style="width: 50px;" id="engineerhiddenID" name="engineerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId3}">
								     	</td>
								     </tr>
								     <tr>
										<td><label>Ordering:</label></td>
										<td><input type="text" style="width: 190px;" id="projectManagerID" name="projectManagerName" value="${requestScope.ProjManagers}" placeholder="Minimun 2 character to search"></td>
										<td>
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long"></td>
										<td><input type="hidden" style="width: 50px;" id="projectManagerhiddenID" name="projectManagerhiddenName" value="${requestScope.joMasterDetail.cuAssignmentId4}">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "custom_fieldset" style="height: 160px;width: 350px;">
							<legend class="custom_legend"><label><b>Shipping Details:</b></label></legend>
								<table>
									<tr>
										<td>
											<table style="text-align: right;">
											<tr>
												<td><label>Warehouse: </label></td>
												<td align="left">
													<select id="whrhouseID" name="whrhouseName" style="width: 170px;">
														<option value="0"> -Select- </option>
														<c:forEach var="wareHouseBean" items="${requestScope.wareHouse}">
															<option value="${wareHouseBean.prWarehouseId}">
																<c:out value="${wareHouseBean.description}"></c:out>
															</option>
														</c:forEach>
													</select>
												</td>
											</tr>
												<tr>
													<td><label>Via:</label></td>
													<td align="left">
													<select style="width:120px;" id="SOshipViaId" name="shipViaName">
														<option value="0"> - Select - </option>
															<c:forEach var="shipViaBean" items="${requestScope.shipVia}">
																<option value="${shipViaBean.veShipViaId}">
																	<c:out value="${shipViaBean.description}" ></c:out>
																</option>
														</c:forEach>
												</select>
												</tr>
												<tr>
													<td align="right"><label>Pro #:</label></td>
													<td align="left"><input type="text" style="width:120px" id="poID" name="poName"></td>
												</tr>
												<tr>
													<td align="right"><label>Shipping Date :</label></td>
													<td align="left"><input type="text" style="width:120px" id="SOShipDate" name="ShipDate"></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
		</td>
	</tr>
	<tr>
		<td>
			<table>
				<tr>
					<td>
						<fieldset class=" custom_fieldset" style="height: 106px;width: 723px;">
							<legend class="custom_legend"><label><b>Tax Territory:</b></label></legend>
							<table style="text-align: right;">
								<tr>
									<td>
									<label>Tax Territory :</label>	
										<input type="text" style="width: 137px;position:relative;margin-left: 18px; middle;" id="taxID" name="taxName" value="${requestScope.taxTerritoryID}">
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										<input type="hidden" style="width: 70px;vertical-align: middle;" id="taxhiddenID" name="taxhiddenName" 
												value="${requestScope.joMasterDetail.coTaxTerritoryId}">
									</td>
									<td style="padding-right: 8px;">
										<label>Terms:&nbsp;&nbsp;&nbsp;&nbsp;</label>
										<select id="terms" name="terms" style="width: 207px;">
										<option value="-1">-Select-</option>
										<c:forEach var="cuTermsBean" items="${requestScope.cuTerms}">
										<option value="${cuTermsBean.cuTermsId}">
										<c:out value="${cuTermsBean.description}"></c:out>
										</option>
										</c:forEach> 
										</select>
										<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 2 characters long">
										<input type="hidden" style="width: 50px;vertical-align: middle;" id="termhiddenID" 
												name="termhiddenName" value="">
								</td>
								</tr>
								<tr align="left">
									<td>
										<label>Customer Po #:</label>
										<input type="text" style="width: 137px;margin-left: 9px;" id="custID" name="CustomrPO" value="">
									</td>
									<td><label style="vertical-align: top;margin-left: 159px;">Tag:</label>&nbsp;&nbsp;&nbsp;&nbsp;
												<input type="text" id="tagJobID" name="tagJobName" style="width:219px">
										</td>
								</tr>
								<tr>
									<td><label style="margin-right: 41px;">Division:</label>
												<select style="width: 165px; margin-left: 7px;" id="SOdivisionID" name="divisionName">
													<option value="-1"> - Select - </option>
														<c:forEach var="coDivisionBean" items="${requestScope.coDivision}">
															<option value="${coDivisionBean.coDivisionId}">
																	<c:out value="${coDivisionBean.description}" ></c:out>
															</option>
													</c:forEach>
											</select>
									</td>
									<td><label>Promised:</label>
										<input type="text" style="width:220px" id="promisedID" name="promiseName">
									</td>
								</tr>
							</table>
						</fieldset>
					</td>
		   		</tr>
		   	</table>
		</td>
	</tr>
	<tr>
		<td>
			<fieldset class= "custom_fieldset" style="width:729px">
	<legend class="custom_legend"><label><b>Totals</b></label></legend>
   		<table style="width:700px">
   			<tr align="center">
	   			<td><label>Subtotal:</label></td><td><input type="text" style="width:85px" id="SOGeneral_subTotalID" name="customerInvoice_subTotalName" disabled="disabled"></td>
	       		<td><label>Freight:</label></td><td><input type="text" style="width:85px" id="SOGeneral_frightID" name="customerInvoice_frightname" value="$0.00"></td>
	   			<td><label>Tax:</label></td><td><input type="text" style="width:85px" id="SOGeneral_taxId" name="customerInvoice_taxName" value="${requestScope.taxRate}" disabled="disabled">%</td>
	   			<td><label></label></td><td><input type="text" style="width:85px" id="SOGeneral_taxvalue" name="customerInvoice_taxName" value="" disabled="disabled"></td>
	       		<td><label>Total:</label></td><td><input type="text" style="width:85px" id="SOGeneral_totalID" name="customerInvoice_totalName" disabled="disabled"></td>
      	 	</tr>
 		 </table>
</fieldset>
		</td>
	</tr>
	<tr>
	<td><input type="button" id="POReleaseID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="saveSORelease()" style="width:120px;position: relative;float: right;"></td>
	</tr>
</table>
</div>	
<script type="text/javascript">
date = new Date();
var month = date.getMonth()+1;
var day = date.getDate();
var year = date.getFullYear();
var subTotal = formatCurrency("${requestScope.CuSoDetail.subTotal}");
var freightTotal = formatCurrency("${requestScope.CuSoDetail.freight}");
var taxTotal = formatCurrency("${requestScope.CuSoDetail.taxTotal}");
var taxRate = formatCurrency("${requestScope.CuSoDetail.taxRate}");
var taxSub = formatCurrency("");
$("#subtotalId").val(subTotal);
$("#freightId").val(freightTotal);
$("#totalId").val(taxTotal);
$("#taxId").val(taxRate);
$("#subID").val(taxSub);

var aDivision = "${requestScope.CuSoDetail.prFromWarehouseId}";
$("#whrhouseID option[value=" + aDivision + "]").attr("selected", true);

/**--- Ajax auto suggest functions ---*/
$(function() { var cache = {}, lastXhr;
$( "#salesmanID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesmanhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#csrID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#csrhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#salesManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#salesManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#engineerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#engineerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#projectManagerID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#projectManagerhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	} }); });

$(function() { var cache = {}, lastXhr;
$( "#taxID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#taxhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "companycontroller/companyTax", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });	

$(function() { var cache = {}, lastXhr;
$( "#termID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#termhiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });

$(function() { var cache = {}, lastXhr;
$( "#viaID" ).autocomplete({ minLength: 2,timeout :1000,
	select: function( event, ui ) { var id = ui.item.id; $("#viahiddenID").val(id); },
	source: function( request, response ) { var term = request.term;
		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		lastXhr = $.getJSON( "jobtabs3/shipVia", request, function( data, status, xhr ) { cache[ term ] = data; 
			if ( xhr === lastXhr ) { response( data ); 	} });
	},
	error: function (result) {
	     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
	}  }); });
</script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SO_General.js"></script>	