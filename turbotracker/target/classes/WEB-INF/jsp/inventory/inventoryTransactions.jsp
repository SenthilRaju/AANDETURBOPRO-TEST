<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Inventory Transaction</title>
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
		</style> 
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<div style="width:1090px; margin:0 auto;">
			<br>
			
			<input type="hidden" name="prMasterID" id="prMasterID" value="${requestScope.prMasterDetails.prMasterId}">
		
			<table width = "80%" align="center">
			<tr>
			<td align="right"  width = "40%">
				<span><label>Item Code: </label>
				</span></td>
			    <td align="left"><label id="itemcode">${requestScope.prMasterDetails.itemCode}</label></td>
				</tr>
				<tr>
			<td align="right">
				<span><label>Description: </label>
				</span></td>
				<td align="left"><label id="description">${requestScope.prMasterDetails.description}</label></td>
				</tr>
				<tr>
			<td align="right">
				<span><label>Current Cost: </label>
				</span></td>
				<td align="left"><label id="currentcost" ><%-- ${requestScope.prMasterDetails.salesPrice00} --%> </label></td>
				</tr>
				<tr>
			<td align="right">
				<span><label>Primary Vendor: </label>
				</span></td>
				<td align="left"><label id="primaryvendor" >${requestScope.prMasterDetails.vendorName} </label></td>
				</tr>
				<tr>
			<td align="right">
				<span><label>Category: </label> 
				</span></td>
			<td align="left"><label id="category" ><%-- ${requestScope.productCategory}  --%></label><select  id="categoryId" name="categoryName" style="display:none">
													<option value="-1"></option>
													<c:forEach var="categoryBean" items="${requestScope.productCategory}">
														<option value="${categoryBean.prCategoryId}">
															<c:out value="${categoryBean.description}" ></c:out>
														</option>
													</c:forEach>
												</select></td>
				</tr>

			</table>
			<br>
			<table width = "80%" align="center">
			<tr>
			<td >
				<span style="vertical-align:5px;"><label>Current On Hand:</label><label id="onhand" > ${requestScope.prMasterDetails.inventoryOnHand}  </label> 
				</span></td>
				<td >
				<span style="vertical-align:5px;"><label>Allocated:</label> <label id="allocated" >${requestScope.prMasterDetails.inventoryAllocated} </label>
				</span></td>
				<td >
				<span style="vertical-align:5px;"><label>On Order:</label> <label id="onorder" >${requestScope.prMasterDetails.inventoryOnOrder} </label>
				</span></td>
				</tr></table>
				<br>
			<table width = "80%" align="center">
			<tr>
			<td >
				<span style="vertical-align:5px;"><label>Warehouse:</label> <select id="warehouseListID" name="warehouseList" >
											<option value="0">All</option>
										  <c:forEach var="Warehouses" items="${requestScope.Warehouses}">
										<option value="${Warehouses.prWarehouseId}"><c:out value="${Warehouses.searchName}"></c:out></option>
										</c:forEach> 												
								</select>	
				</span></td>
				<td>
				<span style="vertical-align:5px;"><label>Date Range:</label>  <input type="text" class="datepicker" id="fromDateID" name="fromDateID"  />
				</span><span style="vertical-align:5px;"><label>Thru:</label>  <input type="text" class="datepicker" id="toDateID" name="toDateID"/></td>
				<td >
               <input type="button" class="go turbo-blue" value="Go" onclick="getInventoryTrans();"> <input type="button" id="resetbutton" class="go turbo-blue" value="Clear" onclick="ResetDetails();" > 
			</td>	</tr></table>
			
			</div>
			<br>

			<div id="InventoryTransactionGridDiv" align="center">
			<table id="InventoryTransactionGrid"></table>
			<div id="InventoryTransactionPager"></div>
			
			</div>
			<table width = "80%" >
			<tr><td align="right"><input type="button" class="go turbo-blue" value="Print" onclick="getInventoryTransReport()"></td></tr>
		
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
		<div>
		
	
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inventoryTransaction.js"></script>	
		<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/showReceivedInventoryList.js"></script>	 -->
		<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobwizardRelease.js"></script> -->	
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/invoiceList.js"></script> -->
		
	</body>
	<script type="text/javascript">

	var categoryoption="";
	
	$(function() { var cache = {}; var lastXhr='';
	$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
		select: function (event, ui) {
			var aValue = ui.item.value;
			var valuesArray = new Array();
			valuesArray = aValue.split("|");
			$("#inventorysearch").val(valuesArray[1]);
			var id = valuesArray[0];
			var code = valuesArray[2];
			location.href="./inventoryTransactions?token=view&inventoryId="+id + "&itemCode=" + code;
		},
		open: function(){ 
			$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./inventoryDetails?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			 },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchinventoryProduct", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

 	jQuery(document).ready(function() {

 		 $("#categoryId option[value=" + '${requestScope.prMasterDetails.prCategoryId}' + "]").attr("selected", true);
 		categoryoption=$( "#categoryId option:selected" ).text();
 		if(categoryoption !== "") {$("#category").empty(); $("#category").append(categoryoption);}

 		var currentCost = "${requestScope.prMasterDetails.averageCost}";
 		$("#currentcost").text(formatCurrency(currentCost));

 		var aInventoryOnHand = "${requestScope.prMasterDetails.inventoryOnHand}".replace(".0000", "");
	 	var aInventoryAllocated = "${requestScope.prMasterDetails.inventoryAllocated}".replace(".0000", "");
	 	var aInventoryOnOrder = "${requestScope.prMasterDetails.inventoryOnOrder}".replace(".0000", "");
	 	if(aInventoryOnHand !== "") {$("#onhand").empty(); $("#onhand").append(aInventoryOnHand);}
	 	if(aInventoryAllocated !== "") {$("#allocated").empty();	 $("#allocated").append(aInventoryAllocated);}
	 	if(aInventoryOnOrder !== "") {$("#onorder").empty();$("#onorder").append(aInventoryOnOrder);}


	} );

 	var newDialogDiv = jQuery(document.createElement('div'));

 	function getInventoryTransReport(){

 	 	//alert(categoryoption);
 		
 		var prMasterID=$('#prMasterID').val();
 		var warehouseListID = $('#warehouseListID').val();
 		var fromDateID = $('#fromDateID').val();
 		var toDateID=$('#toDateID').val();

 		if(prMasterID == 0){
 			errorText = "Please select Product and Dates.";
 			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
 			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
 									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
 			return false;
 		}
 		createtpusage('Inventory-Transactions','Inventory Transaction Report','Info','Inventory,Generating Inventory Transactions Report,warehouseListID:'+ $('#warehouseListID').val()+',aprMasterID:'+$('#prMasterID').val());
 		window.open("./inventoryList/getInventoryTransReportPdf?prMasterID="+prMasterID+"&warehouseListID="+warehouseListID+"&fromDateID="+fromDateID+"&toDateID="+toDateID+"&category="+categoryoption);
 	}
	</script>
</html>