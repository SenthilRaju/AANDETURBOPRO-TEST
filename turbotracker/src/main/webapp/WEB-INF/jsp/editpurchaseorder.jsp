<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Purchase Order</title>
    <!--  <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" /> -->
    
     <style type="text/css">
     	#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		.ui-widget-overlay {
		    background: none repeat scroll 50% 50% #2B2922;
		    opacity: 0.6;
		}
		
     </style>
       <script type="text/javascript">var _blockjsfiles = true;</script>
    <!--  <link type="text/css" href="./../resources/styles/turbo-css/job_turbo.css" rel="stylesheet"> -->
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	
	<br><br>
	
	<div id="headingID" align="center" style="padding-top: 10px;">
		<label style="font-family: Verdana,Arial,sans-serif;"><b>STOCK PURCHASE ORDER</b></label>
	</div>
	
	<div class="tabs_main " id="PurchaseOrderDiv" style="padding-left: 0px;width:960px;margin:0 auto;  background-color: #FAFAFA;height: auto; margin-bottom: 40px;">
	 <!-- <link type="text/css" href="./../resources/styles/turbo-css/job_turbo.css" rel="stylesheet"> -->
			<ul>
				<li><a id="generalTab" href="#general" onclick="calculateSubTot();POLineItemTabformChanges('TabChange');">General</a></li>
				<li><a id="lineTab" href="#line" onclick="loadDates();POGeneralTabformChanges('TabChange');">Line Items</a></li>
				<!-- <li><a id="ackTab" href="#ack" onclick="loadACK();">Acknowledgement</a></li> -->
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Consign"  style="width:90px;display: none;"></li>
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Reorder"  style="width:90px;" onclick="ReOrderButtonClick()"></li>
				<li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Cust Ack"  style="width:90px;"></li>
				<%-- <li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Open PO"  style="width:90px;"></li> --%>
				<li id="salesacknowledgement" style="position: absolute;right: 1%;top: 1%; background:white;"><input type="button" id="PoStatusButton" value="" style="width: 90px;height: 24px;" onClick="callPOStatus();" /></li>
			</ul>
			<!-- General Tab Starts -->
			<div id="general">
				<form id="POGeneralForm" name="editPOOut" method="post">
	<table>
	<tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
						<table>
						<tr>
							<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 90px;width: 340px;">
									<legend class="custom_legend"><b>Vendor</b></legend>
								<table style="width: 400px">
									<tr>	
									<td><input type="text" style="width: 225px" id="vendorsearch" name="vendorName" value="${vendorName}" disabled="disabled" onchange="POGeneralTabformChanges();">
									<input type="hidden" style="width: 225px" id="vendorID" name="vendorID" value="${theRxMasterId}" disabled="disabled">
									<!-- <input type="button" id="vendorBackwardId" value="" onclick="vendorAddressBackward()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
									<input type="button" id="vendorForwardId" value="" onclick="vendorAddressForward()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
									 --></td>									
								<%-- <td>
										<select  style="width: 225px" id="manfactureName" name="manufacture" onchange="loadOtherTabs();">
												<option value="-1"> - Select - </option>
												<c:forEach var="Rxmaster" items="${manufacturerNames}">
													<option value="${Rxmaster.rxMasterId}"><c:out value="${Rxmaster.name}" ></c:out></option>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td> --%>
										<td>
										<div id="addressToggle" style="display: none" align="left">
									<input type="button" id="vendorBackwardId" value="" onclick="vendorAddressBackward()" onchange="POGeneralTabformChanges();" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
									<input type="button" id="vendorForwardId" value="" onclick="vendorAddressForward()" onchange="POGeneralTabformChanges();" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
									</div>
										<input type="hidden" style="border-width: 3px;" id="vendorsearchRXMasterID" disabled="disabled"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>	
									<tr><td><span id="vendorAddress"></span></td></tr>
									<tr><td><span id="vendorAddress1"></span></td></tr>							
								</table>
							</fieldset>
							</td>
							</tr>
							<tr>
							<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 340px;">
									<legend class="custom_legend"><b>Manufacturer</b></legend>
								<table style="width: 400px">
									<tr>										
										<td>
										<input type="hidden" name="rxCustomerID" id="rxCustomerIDs" value="${rxCustomerID}"/>
										<input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="${manufactureName}" disabled="disabled" onchange="POGeneralTabformChanges();">
										<input type="hidden" style="width: 225px" id="manfactureID" name="manufactureID" value="${theRxMasterId}" disabled="disabled">
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value=<c:out value="${theVepo.joReleaseId}" ></c:out> > </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" ></td>
									</tr>
									
								</table>
							</fieldset>
							</td>
						</tr>
						<%-- <tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 340px;">
									<legend class="custom_legend"><b>ATTN</b></legend>
								<table style="width: 400px">
									<tr>										
										<td>
										<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId()"><option value="-1">- Select -</option>
												<c:forEach var="buddies" items="${buddiesList}">
													<c:if test="${theVepo.rxVendorContactId eq buddies.rxContactId}">
														<option selected="selected" value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
													<c:if test="${theVepo.rxVendorContactId ne buddies.rxContactId}">
														<option value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>								
								</table>
							</fieldset>
						</td>
						</tr> --%>
						</table>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all"  style="height: 145px;width: 340px;">
								<legend class="custom_legend"><b>Purchase Order</b></legend>
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" onchange="POGeneralTabformChanges();" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${theVepo.orderDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left" style="display:none">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" onchange="POGeneralTabformChanges();" style="width: 140px;" value="${theVepo.customerPonumber}">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" onchange="POGeneralTabformChanges();" value="${theVepo.ponumber}" disabled="disabled"></td>
									</tr>
									
									<tr align="left" id="qbtr">
									<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 140px" id="wantedId" name="wantedName" onchange="POGeneralTabformChanges();" readonly="readonly" value="${theVepo.dateWanted }"></td>										
										<!-- <td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="" readonly="readonly" ><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td> -->
									</tr>
									<!-- <tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" ></td>
									</tr> -->
								</table>
							</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 340px;">
									<legend class="custom_legend"><b>ATTN</b></legend>
								<table style="width: 400px">
									<tr>										
										<td>
										<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId();POGeneralTabformChanges();"><option value="-1">- Select -</option>
												<c:forEach var="buddies" items="${buddiesList}">
													<c:if test="${theVepo.rxVendorContactId eq buddies.rxContactId}">
														<option selected="selected" value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
													<c:if test="${theVepo.rxVendorContactId ne buddies.rxContactId}">
														<option value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>								
								</table>
							</fieldset>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 340px;">
									<legend class="custom_legend"><b>Tag</b></legend>
								<table style="width: 400px">
									<tr>										
										<td>
											<input type="text" style="width: 210px" id="tagId" value="${theVepo.tag}" onchange="POGeneralTabformChanges();">										
										</td>
									</tr>								
								</table>
							</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 120px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 110px;"><label>Ordered By:</label></td>
										<td><select style="width:120px" id="orderId" name="orderName" onchange="POGeneralTabformChanges();">
												<option value="-1"> - Select - </option>
												<c:forEach var="TsUserLogin" items="${orderedByNames}">
												<c:set value="${TsUserLogin.fullName}" var="name" />
												
													<%-- <c:if test="${sessionScope.user.fullName eq name}"> --%>
													<c:if test="${theVepo.orderedById eq TsUserLogin.userLoginId}">
														<option selected="selected" value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
													<%-- <c:if test="${sessionScope.user.fullName ne name}"> --%>
													<c:if test="${theVepo.orderedById ne TsUserLogin.userLoginId}">
														<option value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
												</c:forEach>
											</select>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;</td>
										<td><input type="hidden" id="orderhiddenId" name="orderhiddenName" style="width: 40px;" value="${sessionScope.user.userId}">
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Freight charges:</label></td>
										<td>
											<select style="width:120px" id="frieghtChangesId" name="frieghtChangesName" onchange="POGeneralTabformChanges();">
												<option value="-1"> - Select - </option>
												<c:forEach var="friedChargesBean" items="${friedCharges}">
													<c:if test="${theVepo.veFreightChargesId eq friedChargesBean.veFreightChargesId}">
														<option selected="selected" value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veFreightChargesId ne friedChargesBean.veFreightChargesId}">
														<option value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Ship Via:</label></td>
										<td>
											<select style="width:120px;" id="shipViaId" name="shipViaName" onchange="POGeneralTabformChanges();">
												<option value="-1"> - Select - </option>
												<c:forEach var="shipViaBean" items="${shipVia}">
													<c:if test="${theVepo.veShipViaId eq shipViaBean.veShipViaId}">
														<option selected="selected" value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veShipViaId ne shipViaBean.veShipViaId}">
														<option value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<!-- <tr align="left">
										<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 100px" id="wantedId" name="wantedName" value="" class="datepicker"></td>
										<td><select id="wantedComboId"  style="width: 90px" name="wantedCombo" >
												<option value="-1"> - Select - </option>													
												<option value="0" selected="selected"> On or Before </option>
												<option value="1"> Not Before </option>
													
											</select>
										</td>
									</tr> -->
								</table>
							</fieldset>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName" onchange="POGeneralTabformChanges();">${theVepo.specialInstructions}</textarea></td>
								</table>
							</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<%-- <tr>
				<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
									<legend class="custom_legend"><b>Vendor</b></legend>
								<table style="width: 400px">
									<tr>
										<td style="width:120px"><label>Manufacturer:</label></td>
										<td>
										<select  style="width: 225px" id="manfactureName" name="manufacture" onchange="loadOtherTabs();">
												<option value="-1"> - Select - </option>
												<c:forEach var="Rxmaster" items="${manufacturerNames}">
													<option value="${Rxmaster.rxMasterId}"><c:out value="${Rxmaster.name}" ></c:out></option>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value="1"> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" value="1"></td>
									</tr>
									<tr>
										<td style="width:80px"><label>ATTN:</label></td>
										<td id="contactselectID">
											<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId()"><option value="-1">- Select -</option>
												<c:forEach var="buddies" items="${buddiesList}">
													<c:if test="${theVepo.rxVendorContactId eq buddies.rxContactId}">
														<option selected="selected" value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
													<c:if test="${theVepo.rxVendorContactId ne buddies.rxContactId}">
														<option value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
							</td>
							<td align="left">
				<table style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all"  style="height: 145px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="<fmt:formatDate pattern="MM/dd/yyyy"  value="${theVepo.orderDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value=""></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" ></td>
									</tr>
									<tr align="left" id="qbtr">
										<td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="" readonly="readonly" ><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<!-- <td style="vertical-align: top">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
						</td> -->
					</tr>
				</table>
							</td>
							</tr>
							</table>
						</td>
						<td>
				<table style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all"  style="height: 145px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="<fmt:formatDate pattern="MM/dd/yyyy"  value="${theVepo.orderDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value=""></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" ></td>
									</tr>
									<tr align="left" id="qbtr">
										<td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="" readonly="readonly" ><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<!-- <td style="vertical-align: top">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
						</td> -->
					</tr>
				</table>
			</td>
			<!-- <td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 340px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table style="width: 400px">
										<tr>
					 						<td><input type="text" id=billToAddressID name="billToAddressID" value="" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
					 						<input type="text" id="customerBillToID" name="customerBillName" style="display: none;" value="">
					 						</td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="billToAddressID1" name="billToAddressID1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" value=""></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="billToAddressID2" name="billToAddressID2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled" value=""></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="billToCity" name="billToCity" style="width: 100px;" disabled="disabled" value="">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="billToState" name="billToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled" value="">
					 								<label>Zip: </label><input type="text" id="billToZipID" name="billToZipID" style="width: 75px;" disabled="disabled">
					 						</td>
					 					</tr>
					 					<tr height="10px"></tr>
					 					<tr align="left">
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 48px;">
												<div id="billtoRadioSet">
	    											<input type="radio" id="billtoradio1" name="radio1" /><label id ="billtolabel1" for="billtoradio1" onclick="billToAddress('Us')" style="width: 63px; margin-right: -6px;">Us</label>
	    											<input type="radio" id="billtoradio2" name="radio1" /><label id ="billtolabel2" for="billtoradio2" onclick="billToAddress('Customer')" style="margin-right: -6px;" >Customer</label>
	   												<input type="radio" id="billtoradio3" name="radio1" /><label id ="billtolabel3" for="billtoradio3" onclick="billToAddress('Other')" style="margin-right: -6px;">Other</label>
   												 
   												</div>
											</td>
										</tr>
					 				</table>
							</fieldset>
						</td>
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 340px;">
									<legend class="custom_legend"><label><b>Ship To</b></label>
									</legend>
									<table  id="shipTo" style="width: 400px">
										<tr>
						 					<td><input type="text" id="shipToAddressID" name="shipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">
						 					<input type="text" id="customerShipToID" name="customerShipToName" style="display: none;" value="">
						 					</td>
						 				</tr>
						 				<tr>
						 					<td><input type="text" id="shipToAddressID1" name="shipToAddress1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="shipToAddressID2" name="shipToAddress2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="shipToCity" name="shipToCity" style="width: 100px;" disabled="disabled">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="shipToState" name="shipToState" style="width: 30px; text-transform: uppercase" maxlength="2"  disabled="disabled">
					 								<label>Zip: </label><input type="text" id="shipToZipID" name="shipToZip" style="width: 75px;" disabled="disabled">
					 								<input type="button" id="backWardId" value="" onclick="shipBackWard()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
													<input type="button" id="forWardId" value="" onclick="shipForWard()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
					 						</td>
					 					</tr>
					 				</table>
					 				
					 				<table>
					 				<tr height="10px"></tr>
					 					<tr align="center">
											<td id="radioSet" style="vertical-align: bottom;padding-left: 20px; height: 10px">
												<div id="shipToRadioSet">
													<input type="radio" id="shiptoradio1" name="radio"/><label for="shiptoradio1" id="shiptolabel1" onclick="shipToAddress('Us')" style="width: 63px; margin-right: -6px;" >Us</label>
													<input type="radio" id="shiptoradio2" name="radio"/><label for="shiptoradio2" id="shiptolabel2" onclick="shipToAddress('Customer')" style="margin-right: -6px;">Customer</label>
													<input type="radio" id="shiptoradio3" name="radio"/><label for="shiptoradio3" id="shiptolabel3" onclick="shipToAddress('Job Site')" style="margin-right: -6px;">Job Site</label>
													<input type="radio" id="shiptoradio4" name="radio"/><label for="shiptoradio4" id="shiptolabel4" onclick="shipToAddress('Other')">Other</label>
												</div>
											</td>
										</tr>
					 				</table>
								</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td> -->
		</tr> --%>	
		<%-- <tr>
			<td>
				<table  style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 120px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 110px;"><label>Ordered By:</label></td>
										<td><input type="text" style="width: 100px" id="orderId" name="orderName" value="${sessionScope.user.fullName}"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">&nbsp;&nbsp;&nbsp;</td>
										<td><input type="hidden" id="orderhiddenId" name="orderhiddenName" style="width: 40px;" value="${sessionScope.user.userId}">
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Freight charges:</label></td>
										<td>
											<select style="width:120px" id="frieghtChangesId" name="frieghtChangesName" >
												<option value="-1"> - Select - </option>
												<c:forEach var="friedChargesBean" items="${friedCharges}">
													<c:if test="${theVepo.veFreightChargesId eq friedChargesBean.veFreightChargesId}">
														<option selected="selected" value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veFreightChargesId ne friedChargesBean.veFreightChargesId}">
														<option value="${friedChargesBean.veFreightChargesId}"><c:out value="${friedChargesBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Ship Via:</label></td>
										<td>
											<select style="width:120px;" id="shipViaId" name="shipViaName">
												<option value="-1"> - Select - </option>
												<c:forEach var="shipViaBean" items="${shipVia}">
													<c:if test="${theVepo.veShipViaId eq shipViaBean.veShipViaId}">
														<option selected="selected" value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
													<c:if test="${theVepo.veShipViaId ne shipViaBean.veShipViaId}">
														<option value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr align="left">
										<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 100px" id="wantedId" name="wantedName" value="" class="datepicker"></td>
										<td><select id="wantedComboId"  style="width: 90px" name="wantedCombo" >
												<option value="-1"> - Select - </option>													
												<option value="0" selected="selected"> On or Before </option>
												<option value="1"> Not Before </option>
													
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
									<legend class="custom_legend"><b>Vendor</b></legend>
								<table style="width: 400px">
									<tr>
										<td style="width:120px"><label>Manufacturer:</label></td>
										<td>
										<select  style="width: 225px" id="manfactureName" name="manufacture" onchange="loadOtherTabs();">
												<option value="-1"> - Select - </option>
												<c:forEach var="Rxmaster" items="${manufacturerNames}">
													<option value="${Rxmaster.rxMasterId}"><c:out value="${Rxmaster.name}" ></c:out></option>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value="1"> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName" value="1"></td>
									</tr>
									<tr>
										<td style="width:80px"><label>ATTN:</label></td>
										<td id="contactselectID">
											<select style="width:225px;" id="contacthiddenID" name="attnName" onchange="getContactId()"><option value="-1">- Select -</option>
												<c:forEach var="buddies" items="${buddiesList}">
													<c:if test="${theVepo.rxVendorContactId eq buddies.rxContactId}">
														<option selected="selected" value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
													<c:if test="${theVepo.rxVendorContactId ne buddies.rxContactId}">
														<option value="${buddies.rxContactId}">${buddies.firstName} ${buddies.lastName}</option>
													</c:if>
												</c:forEach>
											</select>
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>	
			</td>
			<td style="vertical-align: top">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
						</td>
		</tr> --%>
		<tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td align="left">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 340px;">
								<legend class="custom_legend"><label><b>Bill To</b></label></legend>
									<table style="width: 400px">
										<tr>
					 						<td><!-- <span id="ShowHideBillTo"> --><input type="text" id=billToAddressID name="billToAddressID" value="" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" placeholder="Minimum 1 character required to get Name List"><!-- </span> -->
					 						<input type="text" id="customerBillToID" name="customerBillName" style="display: none;" value="">
					 						<input type="text" id="customerBillToAddressID" name="customerBillName" style="display: none;" value="">
					 						<input type="text" id="customerBillToOtherID" name="customerBillOtherName" style="display: none;" value="">
					 						</td>
					 					</tr>
						 				<tr>
						 					<td><input type="text" id="billToAddressID1" name="billToAddressID1" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" value="" onchange="POGeneralTabformChanges();"></td>
						 				</tr>
						 				<tr>
											<td><input type="text" id="billToAddressID2" name="billToAddressID2" class="validate[maxSize[40]" style="width: 300px;" disabled="disabled" value="" onchange="POGeneralTabformChanges();"></td>
										</tr>
					 					<tr>
					 						<td><input type="text" id="billToCity" name="billToCity" style="width: 100px;" disabled="disabled" value="" onchange="POGeneralTabformChanges();">
													<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;">
					 								<input type="text" id="billToState" name="billToState" style="width: 30px; text-transform: uppercase"  maxlength="2"  disabled="disabled" value="" onchange="POGeneralTabformChanges();">
					 								<label>Zip: </label><input type="text" id="billToZipID" name="billToZipID" style="width: 75px;" disabled="disabled" onchange="POGeneralTabformChanges();">
					 						</td>
					 					</tr>
					 					<tr height="10px"></tr>
					 					<tr align="left">
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 48px;">
												<div id="billtoRadioSet">
	    											<input type="radio" id="billtoradio1" name="radio1" onchange="POGeneralTabformChanges();"/><label id ="billtolabel1" for="billtoradio1" onclick="billToAddress('Us',this)" style="width: 63px; margin-right: -6px;">Us</label>
	    											<input type="radio" id="billtoradio2" name="radio1" onchange="POGeneralTabformChanges();"/><label id ="billtolabel2" for="billtoradio2" onclick="billToAddress('Customer',this)" style="margin-right: -6px;" >Customer</label>
	   												<input type="radio" id="billtoradio3" name="radio1" onchange="POGeneralTabformChanges();"/><label id ="billtolabel3" for="billtoradio3" onclick="billToAddress('Other',this)" style="margin-right: -6px;">Other</label>
   												 
   												</div>
											</td>
										</tr>
					 				</table>
							</fieldset>
						</td>
						<td align="left" >
							<div id="PO_Shiptooutside">
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 180px;width: 90%;">
								<legend class="custom_legend"><label><b>Ship To</b></label>
									</legend>
									<%@ include file="ShipTo.jsp" %>
								</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<%-- <tr>
			<td>
				<table style="width: 900px">
					<tr>
						<td>
							<fieldset class= "ui-widget-content ui-corner-all"  style="height: 145px;width: 340px;">
								<table style="width: 400px">
									<tr align="left">
										<td style="width: 120px;"><label>PO Date:</label></td>
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="<fmt:formatDate pattern="MM/dd/yyyy"  value="${theVepo.orderDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value=""></td>
									</tr>
									<tr align="left">
										<td ><label>Tag:</label></td>
										<td><input type="text" style="width: 210px" id="tagId" value="" ></td>
									</tr>
									<tr align="left" id="qbtr">
										<td ><label>QuickBooks #:</label></td>
										<td><input type="text" style="width: 100px" id="quickbookId" value="" readonly="readonly" ><input type="button" id="quickBookID" class="cancelhoverbutton turbo-tan" value="QuickBook" 
										onclick="createQuickBooks();" style="width:80px;position: relative;left: 20px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
						<td style="vertical-align: top">
							<fieldset class= "ui-widget-content ui-corner-all" style="height: 100px;width: 340px;">
								<legend class="custom_legend"><label><b>Special Instructions</b></label></legend>
								<table style="width: 400px">
									<tr>
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr> --%>
		
		<tr>
			<td>
			<div id="EditPOSplitCommissionDiv">
				<fieldset class= "custom_fieldset" style="height: 195px;width: 320px;">
								<legend class="custom_legend"><label><b>Split Commission</b></label></legend>
								<table>
									<tr>
										<td>
										<span id="EditPoSplitCommission">
											<table id="EditPoSplitCommissionGrid"></table>
											<div id="EditPoSplitCommissionGridPager"></div>
										</span>
										</td>
								</table>
				</fieldset>
			</div>
			</td>
		</tr>
		<tr>
			<td>
			<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 850px">
				<legend class="custom_legend"><label><b>Total</b></label></legend>
					<table style="width: 750px">
						<tr>
							<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 100px; text-align:right" id="subtotalGeneralId" name="" value="<fmt:formatNumber type="currency" pattern="#,##0.00" value="${theVepo.subtotal}" />" disabled="disabled" onchange="POGeneralTabformChanges();"></td>
							<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat()" onfocus="frieghtCost()" onkeypress="return isNumberKey(this);" value="<fmt:formatNumber type="currency" pattern="#,##0.00" value="${theVepo.freight}" />" onchange="POGeneralTabformChanges();"></td>
							<td><label>Tax: </label></td><td><fmt:setLocale value="en_US"/><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" onclick="taxCost()" onchange="taxFormat()" onfocus="taxCost()" value="${theVepo.taxRate}" onchange="POGeneralTabformChanges();"></td>
							<td><label style="right: 0px;position: relative;">% &nbsp;</label></td><td><input type="text" id="generalID" name="generalName" style="width: 100px;text-align:right;" disabled="disabled" value="<fmt:formatNumber type="currency" pattern="#,##0.00" value="${theVepo.taxTotal }" />" onchange="POGeneralTabformChanges();"></td>
							<td><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalGeneralId" name="totalGeneralName" disabled="disabled" value="<fmt:formatNumber type="currency" pattern="#,##0.00" value="${theVepo.taxTotal+theVepo.freight+theVepo.subtotal}" />" onchange="POGeneralTabformChanges();"></td>	
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="vePOID" name="vePOName" disabled="disabled" value="${theVepo.vePoid}"></td>
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" disabled="disabled" value="${theVepo.veFactoryId}"></td>
						</tr>
					</table>
			</fieldset>
			</td>
		</tr>
</table>
</form>
<table style="width: 900px">
	<tr>
		<td style="width: 60px;padding: 0px 1px;">
	  		<fieldset class= "ui-widget-content ui-corner-all"  style="padding-bottom: 0px;vertical-align: middle;">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;" onchange="POGeneralTabformChanges();" id="generalTabPDF"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC" onclick="outsidepoEmailButtonAction()" onchange="POGeneralTabformChanges();" id="generalTabMail"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td> 	
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStamp" style="colo 	 	r: green;">${theVepo.emailTimeStamp}</label></td>
		<td style="display: none;">
			<input type="button" id="viewGeneralPdfId" class="cancelhoverbutton turbo-tan" value="Confirm" onclick="viewAsPdfGeneral()" style="width:80px;">
		</td>
		<td align="right" style="padding-left: 165px;">
			<div id="showMessage" style="color: green;"></div>
			<div id="msgDlg"><span><b style="color:Green;"></b></span></div>
			<input type="button" id="POSaveID" class="cancelhoverbutton turbo-tan" value="Save" onclick="addPurchaseOrder()" style="width:90px;position: relative; left: -20px;">
			<input type="button" id="POSaveCloseID" class="savehoverbutton turbo-tan" value="Close" onclick="closePOGeneralItemTab()" style="width:110px;position: relative; left: -20px;">
			<input type="checkbox" id="ButtonClicked" style="display: none;"/>
			<input type="text" id="setButtonValue" style="display: none;" value=""/>
		</td>
	</tr>
</table>
			</div>
			<!-- General Tab Ends -->
			<!-- Line Tab Starts -->
		    <div id="line">
		    <table style="width: 900px">
	<tr>
		<td>
		
			<fieldset class=" ui-widget-content ui-corner-all">
				<table style="width: 900px">
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorLineNameId" name="vendorLineName" style="width:200px" disabled="disabled" value="${manufactureName}"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>						
						<td><input type="text" class="datepicker" id="poDateLineId" name="poDateLineName" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${theVepo.orderDate}" />" style="width: 140px;" disabled="disabled"  ></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 150px" id="ourPoLineId" name="ourPoLineName" value="${theVepo.ponumber}" disabled="disabled" ></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
	<table>
		<tr>
			<td></td>
		</tr>
	</table>
	<div id="jqgridLine" style="width:920px; overflow: auto;">
	<table id="lineItemGrid"></table>
	<div id="lineItemPager"></div>
	</div>
	<br>
	<!-- <div>
		<table >
			<tr>
				<td>
					<input type="button" class="turbo-tan savehoverbutton" onclick="showUploadForm()" value="Import from Excel">
					<input type="button" class="turbo-tan savehoverbutton excelDownload" onclick="downloadExcel()" value="Export to Excel">
				</td>
				<td style="padding-left: 7px;">
					<fieldset style="padding-bottom: 0px;padding-bottom: 0px; margin-top: 0px; box-shadow: 0px 1px 5px 3px rgb(170, 170, 170);" class="custom_fieldset" >
						<input type="image" onclick="enableTaxAllRecords()" src="./../resources/Icons/tp_select_all_v2.png" title="Tax All">
					</fieldset>
				</td>
			</tr>
		</table>
	</div> -->
	<div id="uploadExcel_Form" style="display: none; border: 3px 0px #FFFFFF solid">
		<form id="form2" method="post" action="" enctype="multipart/form-data">
			<!-- File input -->    
			<input name="file2" id="file2" type="file" />	<input name="vepoId" style="display: none;" id="vepoId" type="text" value="" />
		</form>
		<button value="Submit" class="turbo-tan savehoverbutton" onclick="uploadJqueryForm()" >Upload</button>
		<input type="button" id="cancelFormSubmit" class="turbo-tan savehoverbutton" value="Cancel" onclick="cancelFormSubmit()"> <br/>
	</div>
	<div id="ackAll" style="display: block; border: 3px 0px #FFFFFF solid;float: right;">	
			
		<button value="Acknowledge All" class="turbo-tan savehoverbutton" onclick="openAckShipPopup()" >Acknowledge All</button>		
	</div>
	<div id="result"></div>
	<table align="left" style="display: none;">
		<tr>
			<td><input type="checkbox" id="gridShowLineItem" onclick="show()" style="vertical-align: middle;"></td>
			<td><a style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Show Invoiced/Received</b></a>
			</td>
			<td><input type="checkbox" id="lineitemcheck" onclick="lineitem()" style="vertical-align: middle;"></td>
			<td><a onclick="lineitem()" id="showLineItem" style="font: bold 12px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;">
					<b style="vertical-align: middle;">Different Estimated Ship For Line Item</b>
					</a>&nbsp;&nbsp;<input type="text" style="width: 100px;vertical-align: middle;" id="lineitemdate" class="datepicker" >
			</td>
		</tr>
	</table><br>
	<table style="width: 900px">
		<tr>
			<td>
				<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;">
				<legend class="custom_legend"><label><b>Totals</b></label></legend>
					<table style="width: 900px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative; "><input type="text" style="width: 100px; text-align:right" id="subtotalLineId" name="subtotalLineName" disabled="disabled" value="0.00"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightLineId" value="" name="freightLineName" onclick="frieghtCostLine()" onchange="frieghtFormatLine();POLineItemTabformChanges();" onfocus="frieghtCostLine()" onkeypress="return isNumberKeyLine(this);" value="0.00"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxLineId" name="taxLineName" onclick="taxCostLine()" onchange="taxFormatLine()" onfocus="taxCostLine()" value="${theVepo.taxRate}" readonly="readonly"></td>
							<td><label style="right: 0px;position: relative;">% &nbsp;</label></td><td><input type="text" id="lineID" name="lineName" style="width: 60px;text-align:right;" disabled="disabled" pattern="#,##0.00" value="${theVepo.taxTotal }"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalLineId" name="totalLineName" disabled="disabled" value="<fmt:formatNumber type="currency" pattern="#,##0.00" value="${theVepo.taxTotal+theVepo.freight+theVepo.subtotal}" />"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>

<table style="width: 900px">
	<tr>
	<td style="width: 60px;padding: 0px 1px;">
	  		<fieldset class= "ui-widget-content ui-corner-all"  style="padding-bottom: 0px;vertical-align: middle;">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;" id="lineTabPDF"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC" id="lineTabMail" onclick="outsidepoEmailButtonAction()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStampLines" style="color: green;">${theVepo.emailTimeStamp}</label></td>
		<td style="display: none;">
			<input type="button" id="viewGeneralPdfId" class="cancelhoverbutton turbo-tan" value="Confirm" onclick="viewAsPdfGeneral()" style="width:80px;">
		</td>
		<td align="right" width="80px">
		<div id="ShowInfo" style="width: 380px;height:20px;color: green;"></div>
		<input id="Buttonchoosed" type="checkbox" style="display: none;"/>
		<input type="button" id="SaveLinesPOButton" class="cancelhoverbutton turbo-tan" value="Save" onclick="SaveLinesPurchaseOrder()" style="width:90px;position: relative; left: -20px;">
		<input type="button" id="CancelLinesPOButton" class="cancelhoverbutton turbo-tan" value="Close" onclick="closePurchaseOrderLineItemTab()" style="width:110px;position: relative; left: -20px;">
		<input type="checkbox" id="ButtonClicked" style="display: none;"/>
		<input type="text" id="setButtonValue" style="display: none;" value=""/>
		<!-- <input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Save" onclick="savePOLineItems()" style="width:90px;" id="cancelpoRelease"> -->
		</td>
	</tr>
</table>
			</div>
	<input type="hidden" name="transactionStatus" value="${theVepo.transactionStatus}" id="POtransactionStatus"/>
			<!-- Line Tab Ends -->
			<!--  Acknowledge All Popup Starts -->
<div id= "AcknowledgeAllDate"  disabled="disabled">
	<form action="" id="AckFornId">
		<div id="AckDate">
			<table align="center">
				<tr><td>
					<fieldset class= " ui-widget-content ui-corner-all" style="height:100px;">
						<legend><label><b>Date</b></label></legend>
							<table>
								<tr>
									<td style=" width : 80px;"><label>Ack Date:<span style="color:red;"> *</span></label></td>
									<td><!-- <input type="text" size="15" aligntext="center" id="ackDate" name="ackDatename" role="textbox"> -->
									
									<input type="text" class="datepicker" id="ackDateAll" name="ackDatename" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentDate}" />" style="width: 140px;">
									</td>
								</tr>
								<tr>
									<td style=" width : 80px;"><label>Ship Date:</label></td>
									<td><!-- <input type="text" size="15" aligntext="center" id="shipDate" name="shipDatename" role="textbox"> -->
									<input type="text" class="datepicker" id="shipDateAll" name="shipDatename" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentDate}" />" style="width: 140px;">
									</td>
								</tr>
								<tr>
									<td style=" width : 80px;"><label>Order #:</label></td>
									<td><input type="text" id="orderAll" name="ordername" value="" style="width: 140px;">
									</td>
								</tr>
							</table>
					</fieldset>
					</td>
										
				</tr>
				<tr>
					<td><input type="button" id="applyDates" class="cancelhoverbutton turbo-tan" value="Save" onclick="UpdateLineDates()" style="width:90px;position: relative; left: -20px;"></td>
					<td><input type="button" id="cancelDates" class="cancelhoverbutton turbo-tan" value="Cancel" onclick="CancelLineDates()" style="width:90px;position: relative; left: -20px;"></td>
				</tr>
			</table>
		</div>	
	</form>

</div>

<div id= "POInLineItem" style="display: none;">
	<form action="" id="POInLineItemID">
		<table align="right">
			<tr>
			 	<td>
	   				<textarea cols="70" id="inlineItemId" name="inlineItemName" style="height: 252px; width:570px;"></textarea>
	   				<input id="inlineItemLableId" style="display: none;">
	   			</td>
			</tr>
		</table>
		<table align="right">
			<tr>
			 	<td>
	   				<input type="button" id="PoSaveInLineItemID" class="savehoverbutton turbo-tan" value="Save" onclick="saveLineItemNote()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelLineItemNote()" style="width:80px;">
	   			</td>
			</tr>
		</table>
	</form>
</div> 

<!--  Acknowledge All Popup Ends -->
			<!-- ACK Tab Starts -->
			<%-- <div id="ack">
			<table  style="width: 900px">
	<tr>
		<td>
			<fieldset class=" ui-widget-content ui-corner-all">
			
				<table>
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorAckNameId" name="vendorAckName" style="width:200px" disabled="disabled" value="${manufactureName}"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>
						<td><input type="text" class="datepicker" id="poDateAckId" name="poDateAckName" style="width: 90px;"value="" disabled="disabled" onselect="loadDates();"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 90px" id="ourPoAckId" name="ourPoAckName" value="${aJobPurchaseOrderBean.ponumber}" disabled="disabled" disabled="disabled"></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
	<div id="jqgridAck">
		<table id="Ack"></table>
	<div id="Ackpager"></div>
	</div>
	
	<br>
	<table style="width: 900px">
		<tr>
			<td align="left">
				<input type="button" id="saveAllButton" class="savehoverbutton turbo-tan" value="All" onclick="saveAll()" style="width: 80px;">
			</td>
			<td align="right">
				<a onclick="" style="font: bold 14px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;"><label><b>
				<u>Apply To Rest of Order</u></b></label></a>
			</td>
		</tr>
	</table><br>
	<table style="width: 900px">
		<tr>
			<td>
				<fieldset class= " ui-widget-content ui-corner-all" style="height: 50px;">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
					<table  style="width: 900px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalKnowledgeId" name="subtotalKnowledgeName" disabled="disabled" value="$0.00"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightKnowledgeId" value="" name="freightKnowledgeName" onclick="frieghtCostAck()" onchange="frieghtFormatAck()" onfocus="frieghtCostAck()" onkeypress="return isNumberKeyAck(this);" value="$0.00"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxKnowledgeId" name="taxKnowledgeName" disabled="disabled" value="$0.00"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="KnowledgeID" name="KnowledgeName" style="width: 60px;text-align:right;" disabled="disabled" value="$0.00"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalKnowledgeId" name="totalKnowledgeName" disabled="disabled" value="$0.00"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>

<table  style="width: 900px">
	<tr>
		<td style="width: 60px;padding: 0px 1px; display: none">
	  		<fieldset class= " ui-widget-content ui-corner-all" style="padding-bottom: 0px; ">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOAckPDF()"  style="background: #EEDEBC; display: none;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOAckEmail()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
		<td align="right" style="padding-left: 490px;">
			<!-- <input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="cancelPORelease()" style="width:120px" id="cancelpoRelease"> -->
		</td>
	</tr>
</table>
			</div> --%>
			<!-- ACK Tab Ends -->
		
	</div>
	<div>
				 <jsp:include page="./Email_Attachment.jsp"></jsp:include>
	</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/editpurchaseOrder.js"></script>
	<script type="text/javascript">
	 //GlobalPage_Validation=1;
	bkLib.onDomLoaded(function() { 
		//nicEditors.allTextAreas(); 
		var myNicEditor = new nicEditor();
		myNicEditor.panelInstance(
	            document.getElementById('econt')
	        );
		/* myNicEditor.panelInstance(
	            document.getElementById('specialInstructionID')
	        ); */
		 });
	 
	var VendorAddresses = [];
	var incValue=0;
	var invoicedReceived=1;
	var selectedLineItem;
	 var onChange = 1;
	 var onChangeShipTo = 1;
	 var onChangeOther = 1;
	 var onChangeShipToOther = 1;
	 var billToName,billtoAddress1,billtoAddress2,billToCity,billToState,billToZip,shipToName,shiptoAddress1,shiptoAddress2,shipToCity,shipToState,shipToZip;
	 var billToOtherName,billtoOtherAddress1,billtoOtherAddress2,billToOtherCity,billToOtherState,billToOtherZip,shipToOtherName,shiptoOtherAddress1,shiptoOtherAddress2,shipToOtherCity,shipToOtherState,shipToOtherZip;
	 var taxRateShipTo = 0;


		var shipToAddressSize = "${fn:length(wareHouse)}";
	   	var shipToAddressArray = [];
	   	if(shipToAddressSize>0){
	   		<c:forEach items="${wareHouse}" var="house">
	   		shipToAddressArray.push({description: "${house.description}", address1: "${house.address1}", address2: "${house.address2}", city: "${house.city}", state: "${house.state}",wareHouseID:"${house.prWarehouseId}", zip: "${house.zip}", taxRate: "${house.email}"});
			</c:forEach>
	   	   	}
   	   	
	$(function() {

		$("#new_row_ackDate").datepicker();
		$("#new_row_shipDate").datepicker();
		
	$("#wantedId").datepicker();
		
		$( ".tabs_main" ).tabs();	
	    jQuery("#AcknowledgeAllDate").dialog({
			autoOpen:false,
			height:200,
			width:500,
			title:"Add Date",
			modal:true,
			close:function(){
				$('#AckFornId').validationEngine('hideAll');
				 return true; }
		});    
	    
	  });

	var joReleaseID = "${theVepo.joReleaseId}";
	if(joReleaseID===''){
		$('#EditPOSplitCommissionDiv').hide();
		$("#EditPOSplitCommissionDiv").attr("disabled","disabled");
		$("#EditPOSplitCommissionDiv").children().attr("disabled","disabled");
		
	}else{
		$("#EditPOSplitCommissionDiv").removeAttr("disabled");
		$("#EditPOSplitCommissionDiv").children().removeAttr("disabled");
		$('#EditPOSplitCommissionDiv').show();
		loadEditPOSplitCommissionList(0,joReleaseID);
		}
	
	
	var aPurchaseOrderObject = "${theVepo}";
	var aVePoId;
	var freight = "${theVepo.freight}";
	var rxShip = "${theVepo.rxShipToId}";
	if(rxShip == null || rxShip == '')
	{
		rxShip = "${theVepo.rxShipToId}";
		
	}
	var rxBill = "${theVepo.rxBillToId}";
	if(rxBill == null || rxBill == '')
	{
		rxBill = "${theVepo.rxBillToID}";
	}
	var rxVendorConctact = "${theVepo.rxVendorContactId}";
	var rxbilltoAddressId = "${theVepo.rxBillToAddressId}";
	var rxshiptoAddressId = "${theVepo.rxShipToAddressId}";
	$("#freightGeneralId").keypress(function (e) {
	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	            return false;
	    }
	   });
	//taxGeneralId
	$("#taxGeneralId").keypress(function (e) {
	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	            return false;
	    }
	   });
	
	$("#freightLineId").keypress(function (e) {
	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	            return false;
	    }
	   });

	$("#taxLineId").keypress(function (e) {
	     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
	            return false;
	    }
	   });
	var lastCharPO='';
	jQuery(document).ready(function() {
		var poNumber = $('#ourPoId').val();

		lastCharPO= poNumber.slice(-1);
		if($.isNumeric(lastCharPO)){
			 $('#shiptoradio3').hide();
			 $('#shiptolabel3').hide();
			 
			console.log('last char in PO :'+lastCharPO);
			 
			 if($('#shiptoradio1').is(':disabled')) { 
				 $('#shiptolabel1').attr('onclick',null);
			    }
			 if($('#billtoradio1').is(':disabled')) { 
				 $('#billtolabel1').attr('onclick',null);
			    }
			 if($('#billtoradio2').is(':disabled')) { 
				 $('#billtolabel2').attr('onclick',null);
			    }
			 if($('#billtoradio3').is(':disabled')) { 
				 $('#billtolabel3').attr('onclick',null);
			    }
			 if($('#shiptoradio4').is(':disabled')) { 
				 $('#shiptolabel4').attr('onclick',null);
			    }
		}else{

			 $('link[href$="turbo.css"]').attr("href","./../resources/styles/turbo-css/job_turbo.css");
				$('link[href$="jquery-ui-1.8.16.custom.css"]').attr("href","./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/themes/humanity/jquery-ui-1.8.23.custom.css");
				$('.tabs_main').css({'background-color':'#ddba82'});
				$('.tabs_main').css({'box-shadow':'10px 10px 5px #888888'}); 
			
			console.log('last char in PO :'+lastCharPO);
			$('#headingID').css('display','none');
			 if($('#shiptoradio2').is(':disabled')) { 
				 $('#shiptoradio2').removeAttr('disabled');
			    }
			 if($('#shiptoradio4').is(':disabled')) { 
				 $('#shiptoradio4').removeAttr('disabled');
			    }
			 if($('#shiptoradio1').is(':disabled')) { 
				 $('#shiptoradio1').removeAttr('disabled');
			    }
			 if($('#billtoradio1').is(':disabled')) { 
				 $('#billtoradio1').removeAttr('disabled');
			    }
			 if($('#billtoradio2').is(':disabled')) { 
				 $('#billtoradio2').removeAttr('disabled');
			    }
			 if($('#billtoradio3').is(':disabled')) { 
				 $('#billtoradio3').removeAttr('disabled');
			    }
			
		}	
		
		if($('#POtransactionStatus').val()=='2'){
			$('#PoStatusButton').val('Close');
		}
		else if($('#POtransactionStatus').val()=='0'){
			$('#PoStatusButton').val('Hold');
		}
		else if($('#POtransactionStatus').val()=='1'){
			$('#PoStatusButton').val('Open');
			$('#vendorsearch').removeAttr('disabled');
			
		}
		else if($('#POtransactionStatus').val()=='-1'){
			$('#PoStatusButton').val('Void');
		}
		
		$("#billtoRadioSet").buttonset();
		$("#shipToRadioSet").buttonset();
		$(".datepicker").datepicker();
		$("#customerShipToID").val(rxShip);
		$("#customerBillToID").val(rxBill);

		$("#customerBillToAddressID").val(rxbilltoAddressId);
		$("#customerShipToAddressID").val(rxshiptoAddressId);
		
		$("#rxVendorContactId").val(rxVendorConctact);
		loadBillAddress();
		setTimeout(function() { 
			loadShipAddress();
		 }, 500);	
		aVePoId = "${aVePoID}";
		var size = "${fn:length(lineGridDetails)}";
		var jsonList1;
		var lineGridData = []; //create a new array global
		if(size > 0)
		{
			<c:forEach items="${lineGridDetails}" var="lineGrid">
			var description1='${lineGrid.description}';
			//description1=description1.replace('"','');
			lineGridData.push({
				vePodetailId:"${lineGrid.vePodetailId}",
				note: "${lineGrid.note}",
				inLineNoteImage: "${lineGrid.note}",			
				description: description1, 
				quantityOrdered: "${lineGrid.quantityOrdered}",
				quantityReceived: "${lineGrid.quantityReceived}",
				invoicedAmount: "${lineGrid.invoicedAmount}", 
				unitCost: "${lineGrid.unitCost}", 
				priceMultiplier: "${lineGrid.priceMultiplier}", 
				taxable: "${lineGrid.taxable}", 
				quantityBilled: "${lineGrid.quantityBilled}",
				quantityReceived: "${lineGrid.quantityReceived}",
				invoicedAmount: "${lineGrid.invoicedAmount}", 
				ackDate: "${lineGrid.ackDate}",
				 shipDate: "${lineGrid.shipDate}", 
				 vendorOrderNumber: "${lineGrid.vendorOrderNumber}", 
				 prMasterId: "${lineGrid.prMasterId}", 
				 inLineNote: "${lineGrid.note}"});
				 
			</c:forEach>

			for(var i = 0 ; i <=(lineGridData.length-1); i++)
			{
				var stt='',nm='';
				var desc = lineGridData[i].description.toString().replace(/"/g, '\\"'); 
				//.replace('"', '\\"');
				lineGridData[i].description = desc;
				var quantityOrdered = parseInt(lineGridData[i].quantityOrdered);
				lineGridData[i].quantityOrdered = quantityOrdered;
				
				try{
					stt= lineGridData[i].note;
			        nm = stt.split('&^&');
				}catch(err){
				}
				if(i == 0)
 		        	jsonList1="{vePodetailId:\""+lineGridData[i].vePodetailId+"\",note:\""+nm[0]+"\",inLineNoteImage:\""+lineGridData[i].inLineNote+"\",description:\""+lineGridData[i].description+"\",notes:\""+nm[1]+"\",notesdesc:\""+lineGridData[i].note+"\",quantityOrdered:\""+lineGridData[i].quantityOrdered+"\",quantityReceived:\""+lineGridData[i].quantityReceived+"\",invoicedAmount:\""+parseInt(lineGridData[i].invoicedAmount)+"\",unitCost:\""+lineGridData[i].unitCost+"\",priceMultiplier:\""+lineGridData[i].priceMultiplier+"\",taxable:\""+lineGridData[i].taxable+"\",quantityBilled:\""+lineGridData[i].quantityBilled+"\",inLineNote:\""+lineGridData[i].inLineNote+"\",ackDate:\""+lineGridData[i].ackDate+"\",shipDate:\""+lineGridData[i].shipDate+"\",vendorOrderNumber:\""+lineGridData[i].vendorOrderNumber+"\",prMasterId:\""+lineGridData[i].prMasterId+"\"}";
		        else
		        	jsonList1 = jsonList1+','+"{vePodetailId:\""+lineGridData[i].vePodetailId+"\",note:\""+nm[0]+"\",inLineNoteImage:\""+lineGridData[i].inLineNote+"\",description:\""+lineGridData[i].description+"\",notes:\""+nm[1]+"\",notesdesc:\""+lineGridData[i].note+"\",quantityOrdered:\""+lineGridData[i].quantityOrdered+"\",quantityReceived:\""+lineGridData[i].quantityReceived+"\",invoicedAmount:\""+parseInt(lineGridData[i].invoicedAmount)+"\",unitCost:\""+lineGridData[i].unitCost+"\",priceMultiplier:\""+lineGridData[i].priceMultiplier+"\",taxable:\""+lineGridData[i].taxable+"\",quantityBilled:\""+lineGridData[i].quantityBilled+"\",inLineNote:\""+lineGridData[i].inLineNote+"\",ackDate:\""+lineGridData[i].ackDate+"\",shipDate:\""+lineGridData[i].shipDate+"\",vendorOrderNumber:\""+lineGridData[i].vendorOrderNumber+"\",prMasterId:\""+lineGridData[i].prMasterId+"\"}";
			}
		}
		var aValue = $( "#vendorID" ).val();
		 $.ajax({
		        url: './getManufactureATTN?rxMasterId='+aValue,
		        type: 'POST',
		        async:false,       
		        success: function (data) {
	        	$.each(data, function(key, valueMap){
				if("vendorAddress"==key)
					{
					$.each(valueMap, function(index, value){
					if(index==0){
						$('#vendorAddress').html(value.address1);
						$('#vendorAddress1').html(value.city).append(value.state).append(value.zip);
						}
					VendorAddresses.push({name: value.name,address1:value.address1 ,address2:value.address2,city: value.city, state:value.state, zip: value.zip});
					if(value.isDefault){
					$('#vendorAddress').html(value.address1);
					$('#vendorAddress1').html(value.city).append(value.state).append(value.zip);
					}
					}); 
					if(VendorAddresses.length>=2){
						$('#addressToggle').css('display','block');
						var imgUrlDisBackward = "./../resources/images/DisabledArrowleft.png";
						$('#vendorBackwardId').css('background', 'url('+imgUrlDisBackward+') no-repeat');
						$('#vendorBackwardId').css('background-position','center');
					}
				
				}
		    }); 
		
	}
	})
	
		
		$("#AcknowledgeAllDate").tabs();
		//loadLinesItemGrid(jsonList1);
		loadLinesItemGrid();
		setTimeout(function() { 
		 	   editPOGlobalFormOut = $("form[name=editPOOut]").serialize();
		 	   po_General_tab_form_val=poGeneralTabFormValidation();
		 	   po_General_tab_form=JSON.stringify(po_General_tab_form_val);
		 }, 1000);	
	});
	
	function vendorAddressBackward(){
		if(parseInt(incValue)>=VendorAddresses.length){
			incValue = (VendorAddresses.length)-2
		}
		if(VendorAddresses.length >0)
		{
				console.log('incValue Bac: '+incValue);
				//incValue=VendorAddresses.length-1;
				//for(var i = (VendorAddresses.length-1); i >= 0; i--)
				//{
					var imgUrlforward = "./../resources/images/Arrowright.png";
					$('#vendorForwardId').css('background', 'url('+imgUrlforward+') no-repeat');
				$('#vendorForwardId').css('background-position','center');
				console.log(VendorAddresses.length+" Address1: "+VendorAddresses[incValue].address1);
				$('#vendorAddress').empty().append(VendorAddresses[incValue].address1);
				$('#vendorAddress1').empty().append(VendorAddresses[incValue].city+''+VendorAddresses[incValue].state+''+VendorAddresses[incValue].zip);
				incValue=parseInt(incValue)-1;
				// break;
				//}
			if(parseInt(incValue)<0){
				console.log('Over Backward');
				var imgUrlDisBackward = "./../resources/images/DisabledArrowleft.png";
				
				$('#vendorBackwardId').css('background', 'url('+imgUrlDisBackward+') no-repeat');
				$('#vendorBackwardId').css('background-position','center');
				
			}
		}
	
 }

	function vendorAddressForward(){
		if(parseInt(incValue)<=0){
			incValue = 1;
		}
		if(VendorAddresses.length>0)
		{
			console.log('incValue For: '+incValue);
			//for(var i =0; i<=parseInt(incValue); i++)
				//{
				var imgUrlBackward = "./../resources/images/Arrowleft.png";
				$('#vendorBackwardId').css('background', 'url('+imgUrlBackward+') no-repeat');
				$('#vendorBackwardId').css('background-position','center');
				if(parseInt(incValue)>=VendorAddresses.length-1){
				console.log('Max Value - Z: '+incValue);
				var imgUrlDisForward = "./../resources/images/DisabledArrowright.png";
				
				$('#vendorForwardId').css('background', 'url('+imgUrlDisForward+') no-repeat');
				$('#vendorForwardId').css('background-position','center');
				
				
				
			}
				console.log(VendorAddresses.length+" Address1: "+VendorAddresses[incValue].address1);
				$('#vendorAddress').empty().append(VendorAddresses[incValue].address1);
				$('#vendorAddress1').empty().append(VendorAddresses[incValue].city+''+VendorAddresses[incValue].state+''+VendorAddresses[incValue].zip);
				incValue=parseInt(incValue)+1;
				//break;
				//}			
		}
	}


	
	function usShiptoAddress(){
		 $("#shiptoradio1").attr("Checked","Checked");
		 $('#shiptolabel1').css("font-weight","bold");
		 $('#shiptolabel2').css("font-weight","normal");
		 $('#shiptolabel3').css("font-weight","normal");
		 $('#shiptolabel4').css("font-weight","normal");
		 $('#shiptolabel1').addClass("ui-state-active");
		 $('#shiptolabel2').removeClass("ui-state-active");
		 $('#shiptolabel3').removeClass("ui-state-active");
		 $('#shiptolabel4').removeClass("ui-state-active");
		 var vePOID = $("#vePO_ID").text();
		 var usShipUSID = 0;
		 var updaetKey = 'shipToUS';
		 $('#forWardId').show();
		 $('#backWardId').show();
		 $('#usShipto').css({ "background-image": "url(./../resources/images/us_select.png)","width":"63px","height": "28px" });
		 $('#customerShipto').css({ "background-image": "url(./../resources/images/customer.png)","width":"63px","height": "28px" });
		 $('#jobsiteShipto').css({ "background-image": "url(./../resources/images/jobsite.png)","width":"63px","height": "28px" });
		 $('#otherShipto').css({ "background-image": "url(./../resources/images/other.png)","width":"63px","height": "28px" });
		 loadShipToAddress();
		 updateBillToAndShipToAddressSetting(vePOID, usShipUSID, updaetKey);
		 return true;
		 }
		 
		 function loadBillAddress()
		 {
			 $("#billtolabel1").removeClass('ui-state-active');
			 $("#billtolabel2").removeClass('ui-state-active');
			 $("#billtolabel3").removeClass('ui-state-active');
			 var billToIndex = "${billtoIndex}";
			 var rxbillToAddId = "${theVepo.rxBillToID}";
			 
			 if(billToIndex == 0)
				 {
				 $("#billtolabel1").addClass('ui-state-active');
				 billToAddress('Us');
				 }
			 else if(billToIndex ==1)
				 {
				 $("#billtolabel2").addClass('ui-state-active');
				 billToAddress('Customer');
				 }
			 else if(billToIndex == 2)
			 {
				 $("#billtolabel3").addClass('ui-state-active');
				 billToAddress('Other');
				 }
			 else if(rxbillToAddId != null && rxbillToAddId.length > 0)
			 {
				 $('#customerBillToID').val(rxbillToAddId);
				 $("#billtolabel2").addClass('ui-state-active');
				 billToAddress('Customer');
			}
			 else 
			 {
				 $("#billtolabel1").addClass('ui-state-active');
				 billToAddress('Us');
				 }
		 }
		 

		 function loadShipAddress() {
				
			 var divflag = "#PO_Shiptooutside";
			 loadshiptostateautocmpte(divflag);
			 
			 var shiptomode = "${theVepo.shipToMode}";
			 var vePOID = "${theVepo.vePoid}";
			 var rxShipToId = "${theVepo.rxShipToId}";	
			 var prWarehouseId =  "${theVepo.prWarehouseId}";
			 var rxShipToAddressId =  "${theVepo.rxShipToAddressId}";
			 var joreleaseid =  "${theVepo.joReleaseId}";

			 console.log(".........>"+joreleaseid);

				if(joreleaseid==null || joreleaseid == "" || typeof(joreleaseid) ==  'undefined' )
				{
				 $("#shiptoaddradio3").hide();
				 $("#shiptoaddlabel3").hide();
				 $("#shiptoaddradio2").button({disabled:true});
				 $("#shiptoaddradio4").button({disabled:true});
				 $('#shiptoaddlabel2').attr('onclick','').unbind('click');
				 $('#shiptoaddlabel4').attr('onclick','').unbind('click');
				}
			 
				$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val("");
				$(divflag).contents().find("#shiptomodehiddenfromdbid").val("");
				var checkshiptoid;
				
				
				if(shiptomode == "0")
				{
					checkshiptoid =prWarehouseId;
				}
				else if(shiptomode == "1" || shiptomode == "2")
				{
					checkshiptoid = rxShipToId;
				}
				else
				{
					checkshiptoid = rxShipToAddressId;
				}
				
				
				if(checkshiptoid!=null)
				{
					if(shiptomode == 0)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(prWarehouseId);
					}
					else if(shiptomode == 1)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(rxShipToId);
					}
					else if(shiptomode == 2)
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(rxShipToId);
					}
					else
					{
						$(divflag).contents().find("#shiptoaddrhiddenfromdbid").val(rxShipToAddressId);
					}
					
					$(divflag).contents().find("#shiptomodehiddenfromdbid").val(shiptomode);
						
					preloadShiptoAddress(divflag,vePOID,checkshiptoid,shiptomode,'0',"","");
					$(divflag).contents().find("#shiptomoderhiddenid").val(shiptomode);
				}
				else	
				{
					preloadShiptoAddress(divflag,vePOID,null,'2','0',"","");
					$(divflag).contents().find("#shiptomoderhiddenid").val('2');
				}
				
		 }
		 
		 
		 //var billToCustomerAddresses = []; //create a new array global
		//billToCustomerAddresses.push({name: "${billToName}", address1: "${theRxaddress.address1}", address2: "${theRxaddress.address2}", city: "${theRxaddress.city}", state: "${theRxaddress.state}", zip: "${theRxaddress.zip}"});
	function billToAddress(type,obj)
	{
		//alert("thisis--->"+this);
		
		
		 $("#billToAddressID").attr("disabled",true);
		 $("#billToAddressID1").attr("disabled",true);
		 $("#billToAddressID2").attr("disabled",true);
		 $("#billToCity").attr("disabled",true);
		 $("#billToState").attr("disabled",true);
		 $("#billToZipID").attr("disabled",true);
		 
		if(type === 'Us')
			{
			$('#customerBillToOtherID').val("Us");
			//$('#customerBillToID').val("");
			
			$('#billToAddressID').val("${aSetting.billToDescription}");
			$('#billToAddressID1').val("${aSetting.billToAddress1}");
			$('#billToAddressID2').val("${aSetting.billToAddress2}");
			$('#billToCity').val("${aSetting.billToCity}");
			$('#billToState').val("${aSetting.billToState}");
			$('#billToZipID').val("${aSetting.billToZip}");
			loadLineItems();
			}
		if(type === 'Customer')
			{
			
			$('#customerBillToOtherID').val("Customer");
			
			$("#billToAddressID").attr("disabled",false);
			 $("#billToAddressID1").attr("disabled",false);
			 $("#billToAddressID2").attr("disabled",false);
			 $("#billToCity").attr("disabled",false);
			 $("#billToState").attr("disabled",false);
			 $("#billToZipID").attr("disabled",false);

			 
			/* $('#billToAddressID').val(billToCustomerAddresses[i].name);
			$('#billToAddressID1').val(billToCustomerAddresses[i].address1);
			$('#billToAddressID2').val(billToCustomerAddresses[i].address2);
			$('#billToCity').val(billToCustomerAddresses[i].city);
			$('#billToState').val(billToCustomerAddresses[i].state);
			$('#billToZipID').val(billToCustomerAddresses[i].zip); */ 

			if(onChange == 1)
			{
				$('#billToAddressID').val("${billToName}");
				$('#billToAddressID1').val("${theRxaddress.address1}");
				$('#billToAddressID2').val("${theRxaddress.address2}");
				$('#billToCity').val("${theRxaddress.city}");
				$('#billToState').val("${theRxaddress.state}");
				$('#billToZipID').val("${theRxaddress.zip}");
			}
			else
			{				
				$('#billToAddressID').val(billToName);
				$('#billToAddressID1').val(billtoAddress1);
				$('#billToAddressID2').val(billtoAddress2);
				$('#billToCity').val(billToCity);
				$('#billToState').val(billToState);
				$('#billToZipID').val(billToZip);
			}
			$('#billToAddressID').focus(function(){
		        $(this).data('placeholder',$(this).attr('placeholder'))
		        $(this).attr('placeholder','Minimum 1 character required to get Name List');
		     });
			$('#billToAddressID').focus();
			loadLineItems();
			}
		if(type === 'Other')
			{
			$('#billToAddressID').focus(function(){
		        $(this).data('placeholder',$(this).attr('placeholder'))
		        $(this).attr('placeholder','');
		     });
			$('#customerBillToOtherID').val("Other");			
			$("#billToAddressID").attr("disabled",false);
			 $("#billToAddressID1").attr("disabled",false);
			 $("#billToAddressID2").attr("disabled",false);
			 $("#billToCity").attr("disabled",false);
			 $("#billToState").attr("disabled",false);
			 $("#billToZipID").attr("disabled",false);

			 if(onChangeOther == 1)
				{
				 $('#billToAddressID').val("${billToOtherName.name}");
					$('#billToAddressID1').val("${billToOtherName.address1}");
					$('#billToAddressID2').val("${billToOtherName.address2}");
					$('#billToCity').val("${billToOtherName.city}");
					$('#billToState').val("${billToOtherName.state}");
					$('#billToZipID').val("${billToOtherName.zip}");
				}
				else
				{				
					$('#billToAddressID').val(billToOtherName);
					$('#billToAddressID1').val(billtoOtherAddress1);
					$('#billToAddressID2').val(billtoOtherAddress2);
					$('#billToCity').val(billToOtherCity);
					$('#billToState').val(billToOtherState);
					$('#billToZipID').val(billToOtherZip);
				}			 
			
			 $('#billToAddressID').focus();
			 loadLineItems();
			}		
		}
		
		function  shipToAddress(type)
		{

			var size = "${fn:length(wareHouse)}";

			$("#shipToAddressID").attr("disabled",true);
			 $("#shipToAddressID1").attr("disabled",true);
			 $("#shipToAddressID2").attr("disabled",true);
			 $("#shipToCity").attr("disabled",true);
			 $("#shipToState").attr("disabled",true);
			 $("#shipToZipID").attr("disabled",true);
			if(type === 'Us')
			{
				$("#shiptomodeId").val("0");
				
				if(size > 0)
					{
					if(size > 0)
					{
						if(size == 1)
						{
							$('#forWardId').prop('hidden',true);
							$('#backWardId').prop('hidden',true);
						}
						else
						{
							$('#forWardId').prop('hidden',false);
							$('#backWardId').prop('hidden',false);
						}
					}	

						$('#customerShipToOtherID').val("Us");

						 for(var i=0;i<size;i++)
						{
								
							var prWarehouseIdfmvepo = "${theVepo.prWarehouseId}";
						    var prwhsid = shipToAddressArray[i].wareHouseID;

							if(prWarehouseIdfmvepo == prwhsid)
								{
								$('#shipToAddressID').val(shipToAddressArray[i].description);
								$('#shipToAddressID1').val(shipToAddressArray[i].address1);
								$('#shipToAddressID2').val(shipToAddressArray[i].address2);
								$('#shipToCity').val(shipToAddressArray[i].city);
								$('#shipToState').val(shipToAddressArray[i].state);
								$('#shipToZipID').val(shipToAddressArray[i].zip);
								var sTaxRate =shipToAddressArray[i].taxRate;
								$('#taxGeneralId').val(formatCurrencynodollar(sTaxRate));
								$('#prWarehouseID').val(prwhsid);
								}
							
						}
						
					}
				else
				{
					$('#forWardId').prop('hidden',true);
					$('#backWardId').prop('hidden',true);
				}
			}
			if(type === 'Customer')
			{
				$("#shiptomodeId").val("1");
				$('#customerShipToOtherID').val("Customer");
				$("#shipToAddressID").attr("disabled",false);
				 $("#shipToAddressID1").attr("disabled",false);
				 $("#shipToAddressID2").attr("disabled",false);
				 $("#shipToCity").attr("disabled",false);
				 $("#shipToState").attr("disabled",false);
				 $("#shipToZipID").attr("disabled",false);
				 $('#forWardId').prop('hidden',true);
				 $('#backWardId').prop('hidden',true);
				 
				 
				 if(onChangeShipTo == 1)
					{
					 $('#shipToAddressID').val("${shipToName}");
						$('#shipToAddressID1').val("${theShipAddress.address1}");
						$('#shipToAddressID2').val("${theShipAddress.address2}");
						$('#shipToCity').val("${theShipAddress.city}");
						$('#shipToState').val("${theShipAddress.state}");
						$('#shipToZipID').val("${theShipAddress.zip}");	
						if(rxShip != null && rxShip != '')
							$('#taxGeneralId').val(formatCurrencynodollar("${theShipToTaxRate}"));
						else
							$('#taxGeneralId').val(formatCurrencynodollar("${theVepo.taxRate}"));
					}
					else
					{
						
						$('#shipToAddressID').val(shipToName);
						$('#shipToAddressID1').val(shiptoAddress1);
						$('#shipToAddressID2').val(shiptoAddress2);
						$('#shipToCity').val(shipToCity);
						$('#shipToState').val(shipToState);
						$('#shipToZipID').val(shipToZip);
						$('#taxGeneralId').val(formatCurrencynodollar(taxRateShipTo));
					}
				 $('#shipToAddressID').focus(function(){
				        $(this).data('placeholder',$(this).attr('placeholder'))
				        $(this).attr('placeholder','Minimum 1 character required to get Name List');
				     });
				 $('#shipToAddressID').focus();
				
			}
			if(type === 'jobSite')
			{
				$("#shiptomodeId").val("2");
				var rxMasterId = $('#rxCustomerIDs').val();
				operationVar = "ship";
				 $.ajax({
					url: "./jobtabs3/getJobAddress",
					type: "GET",
					data : {"customerID" : rxMasterId,"oper" : operationVar},
					success: function(data) {
						var locationName = data.customerPonumber;
						var rxAddressId = data.customerPonumber;
						var locationAddress1 = data.locationAddress1;
						var locationAddress2 = data.locationAddress2;
						var locationCity = data.locationCity;
						var locationState = data.locationState;
						var locationZip = data.locationZip;
						/*  if(onChangeShipTo == 1)
							{
							 $('#shipToAddressID').val("${shipToName}");
								$('#shipToAddressID1').val("${theShipAddress.address1}");
								$('#shipToAddressID2').val("${theShipAddress.address2}");
								$('#shipToCity').val("${theShipAddress.city}");
								$('#shipToState').val("${theShipAddress.state}");
								$('#shipToZipID').val("${theShipAddress.zip}");	
								if(rxShip != null && rxShip != '')
									$('#taxGeneralId').val(formatCurrencynodollar("${theShipToTaxRate}"));
								else
									$('#taxGeneralId').val(formatCurrencynodollar("${theVepo.taxRate}"));
							}
							else
							{ */
								
								$('#shipToAddressID').val(locationName);
								$('#shipToAddressID1').val(locationAddress1);
								$('#shipToAddressID2').val(locationAddress2);
								$('#shipToCity').val(locationCity);
								$('#shipToState').val(locationState);
								$('#shipToZipID').val(locationZip);
								 $.ajax({
										url: "./product/getTaxTerritory",
										type: "GET",
										data : {"taxID" : data.coTaxTerritoryId},
										success: function(data) {
											$('#taxGeneralId').val(formatCurrencynodollar(data.taxRate));
										}
									});
								
							//}
		     			}
				});


					
				$('#customerShipToOtherID').val("Job site");
				$("#shipToAddressID").attr("disabled",false);
				 $("#shipToAddressID1").attr("disabled",false);
				 $("#shipToAddressID2").attr("disabled",false);
				 $("#shipToCity").attr("disabled",false);
				 $("#shipToState").attr("disabled",false);
				 $("#shipToZipID").attr("disabled",false);
				 $('#forWardId').prop('hidden',true);
				 $('#backWardId').prop('hidden',true);
				 
				 
				
				 $('#shipToAddressID').focus(function(){
				        $(this).data('placeholder',$(this).attr('placeholder'))
				        $(this).attr('placeholder','Minimum 1 character required to get Name List');
				     });
				 $('#shipToAddressID').focus();
				
			}
			if(type === 'Other')
			{
				$("#shiptomodeId").val("3");

				$('#shipToAddressID').focus(function(){
			        $(this).data('placeholder',$(this).attr('placeholder'))
			        $(this).attr('placeholder','');
			     });
				$('#customerShipToOtherID').val("Other");
				$('#forWardId').prop('hidden',true);
				$('#backWardId').prop('hidden',true);
				$("#shipToAddressID").attr("disabled",false);
				 $("#shipToAddressID1").attr("disabled",false);
				 $("#shipToAddressID2").attr("disabled",false);
				 $("#shipToCity").attr("disabled",false);
				 $("#shipToState").attr("disabled",false);
				 $("#shipToZipID").attr("disabled",false);

				 if(onChangeShipToOther == 1)
					{
					 $('#shipToAddressID').val("${fn:replace(fn:replace(shipToOtherName.name, "\\", "\\\\"), "\"", "\\\"")}");
					 $('#shipToAddressID1').val("${fn:replace(fn:replace(shipToOtherName.address1, "\\", "\\\\"), "\"", "\\\"")}");
						$('#shipToAddressID2').val("${fn:replace(fn:replace(shipToOtherName.address2, "\\", "\\\\"), "\"", "\\\"")}");
						$('#shipToCity').val("${fn:replace(fn:replace(shipToOtherName.city, "\\", "\\\\"), "\"", "\\\"")}");
						$('#shipToState').val("${fn:replace(fn:replace(shipToOtherName.state, "\\", "\\\\"), "\"", "\\\"")}");
						$('#shipToZipID').val("${fn:replace(fn:replace(shipToOtherName.zip, "\\", "\\\\"), "\"", "\\\"")}");
						/* $('#shipToAddressID1').val("${shipToOtherName.address1}");
						$('#shipToAddressID2').val("${shipToOtherName.address2}");
						$('#shipToCity').val("${shipToOtherName.city}");
						$('#shipToState').val("${shipToOtherName.state}");
						$('#shipToZipID').val("${shipToOtherName.zip}"); */
						$('#taxGeneralId').val(formatCurrencynodollar("${theVepo.taxRate}"));
					}
					else
					{
						
						$('#shipToAddressID').val(shipToOtherName);
						$('#shipToAddressID1').val(shiptoOtherAddress1);
						$('#shipToAddressID2').val(shiptoOtherAddress2);
						$('#shipToCity').val(shipToOtherCity);
						$('#shipToState').val(shipToOtherState);
						$('#shipToZipID').val(shipToOtherZip);
						$('#taxGeneralId').val(formatCurrencynodollar(taxRateOther));
						$('#taxLineId').val($('#taxGeneralId').val());
					}					
				 
				 $('#shipToAddressID').focus();
					
			}
		}



	   

		var shipAddressInc = 0;
		var zFlag = '0';
		
		function shipForWard()
		{

			 var shipindex = "${shiptoIndex}";

				//alert(shipindex);
			 
			if(parseInt(shipAddressInc)<=0){
				shipAddressInc = 1;
			}else{
			if(zFlag=='1'){
				shipAddressInc=parseInt(shipAddressInc)+2;
			}
			}
			console.log('Forward Log: '+shipAddressInc);
			if(shipToAddressArray.length>0)
			{
				 console.log('incValue For: '+shipAddressInc);
					var imgUrlBackwards = "./../resources/images/Arrowleft.png";
					$('#backWardId').css('background', 'url('+imgUrlBackwards+') no-repeat');
					$('#backWardId').css('background-position','center');
				if(parseInt(shipAddressInc)>=shipToAddressArray.length-1){
					console.log('Max Value - Z: '+shipAddressInc);
					var imgUrlDisForwards = "./../resources/images/DisabledArrowright.png";
					$('#forWardId').css('background', 'url('+imgUrlDisForwards+') no-repeat');
					$('#forWardId').css('background-position','center');			
					
				} 

				$('#shipToAddressID').val(shipToAddressArray[shipAddressInc].description);
				$('#shipToAddressID1').val(shipToAddressArray[shipAddressInc].address1);
				$('#shipToAddressID2').val(shipToAddressArray[shipAddressInc].address2);
				$('#shipToCity').val(shipToAddressArray[shipAddressInc].city);
				$('#shipToState').val(shipToAddressArray[shipAddressInc].state);
				$('#shipToZipID').val(shipToAddressArray[shipAddressInc].zip);
				
				$('#taxGeneralId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
				$('#taxLineId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
				$('#prWarehouseID').val(shipToAddressArray[shipAddressInc].wareHouseID);
				shipAddressInc=parseInt(shipAddressInc)+1;
			}
			zFlag='0';
			console.log('Next Forward : '+shipAddressInc);
		}


		function shipBackWard(){
			if(parseInt(shipAddressInc)>=(shipToAddressArray.length)-1){
				shipAddressInc = (shipToAddressArray.length)-2;
				
				}
			else{
			if(zFlag=='0'){
				shipAddressInc=parseInt(shipAddressInc)-2;
			}
			}
			console.log('Backward Log: '+shipAddressInc);
			if(shipToAddressArray.length >0)
			{ 
					var imgUrlforwards = "./../resources/images/Arrowright.png";
					$('#forWardId').css('background', 'url('+imgUrlforwards+') no-repeat');
					$('#forWardId').css('background-position','center');

					$('#shipToAddressID').val(shipToAddressArray[shipAddressInc].description);
					$('#shipToAddressID1').val(shipToAddressArray[shipAddressInc].address1);
					$('#shipToAddressID2').val(shipToAddressArray[shipAddressInc].address2);
					$('#shipToCity').val(shipToAddressArray[shipAddressInc].city);
					$('#shipToState').val(shipToAddressArray[shipAddressInc].state);
					$('#shipToZipID').val(shipToAddressArray[shipAddressInc].zip);
					
					$('#taxGeneralId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
					$('#taxLineId').val(formatCurrencynodollar(shipToAddressArray[shipAddressInc].taxRate));
					$('#prWarehouseID').val(shipToAddressArray[shipAddressInc].wareHouseID);
					
					shipAddressInc=parseInt(shipAddressInc)-1;

					if(parseInt(shipAddressInc)<0){
					console.log('Over Backward');
					var imgUrlDisBackwards = "./../resources/images/DisabledArrowleft.png";
					$('#backWardId').css('background', 'url('+imgUrlDisBackwards+') no-repeat');
					$('#backWardId').css('background-position','center');
					} 
					zFlag ='1';
					console.log('Next Backward : '+shipAddressInc);
			}
		
	 }
		/* var posit_outside_purchaseorder=0;
		function loadLinesItemGrid() {

			var schgrid='<table id="lineItemGrid"></table><div id="lineItemPager"></div>';	
			$('#jqgridLine').empty();
			$('#jqgridLine').append(schgrid);
			grid = $("#lineItemGrid"),
	        getColumnIndex = function (columnName) {
	            var cm = $(this).jqGrid('getGridParam', 'colModel'), i, l = cm.length;
	            for (i = 0; i < l; i++) {
	                if ((cm[i].index || cm[i].name) === columnName) {
	                    return i; 
	                }
	            }
	            return -1;
	        };
			 $('#lineItemGrid').jqGrid({
				datatype: 'JSON',
				mtype: 'POST',
				url:'./jobtabs5/jobReleaseLineItem',
				postData: {vePoId :function(){
					var vePOID=aVePoId;
					if(vePOID==null || vePOID==''){
						vePOID=0;
						}
					return vePOID;
					} },
				pager: jQuery('#lineItemPager'),
				colNames:["Product No","", "Description1","Notes",'NotesDesc','Qty','Received','Invoiced', 'Cost Each', 'Mult.', 'Tax','Net Each', 'Amount', 'VepoID', 'prMasterID' , 'vePodetailID','Posiion', 'Move', 'TaxTotal', 'Ack.','Ship','Order #','InLineNote'],
				colModel :[
					{name:'note',index:'note',align:'left',width:60,editable:true,hidden:false, edittype:'text', editoptions:{size:40,
						dataInit: function (elem) {
				            $(elem).autocomplete({
				                source: 'jobtabs3/productCodeWithNameList',
				                minLength: 1,
				                select: function (event, ui) {
				                	var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
				                	$("#"+aSelectedRowId+"_ackDate").datepicker();
				            		$("#"+aSelectedRowId+"_shipDate").datepicker();
				                	var id = ui.item.id;
				                	var product = ui.item.label;
				                	$("#"+aSelectedRowId+"_prMasterId").val(id);
				                	$("#new_row_prMasterId").val(id);
				                	
				                	var celValue = $('#vePOID').val();
				                	$.ajax({
								        url: './getLineItems?prMasterId='+id,
								        type: 'POST',       
								        success: function (data) {
								        	$.each(data, function(key, valueMap) {										
												
								        		if("lineItems"==key)
												{				
													$.each(valueMap, function(index, value){						
														
															$("#new_row_description").val(value.description);
															$("#"+aSelectedRowId+"_description").val(value.description);
															$("#new_row_unitCost").val(value.lastCost);
															$("#"+aSelectedRowId+"_unitCost").val(value.lastCost);
															$("#new_row_priceMultiplier").val(value.pomult);
															$("#"+aSelectedRowId+"_priceMultiplier").val(value.pomult);
															$("#new_row_sopopup").val(value.sopopup);
															$("#"+aSelectedRowId+"_sopopup").val(value.sopopup);
															$("#new_row_vePoid").val(celValue);
															$("#"+aSelectedRowId+"_vePoid").val(celValue);
															
															
															if(value.isTaxable == 1)
															{
																$("#new_row_taxable").prop("checked",true);
																$("#"+aSelectedRowId+"_taxable").prop("checked",true);
															}
															else
															{
																$("#new_row_taxable").prop("checked",false);
																$("#"+aSelectedRowId+"_taxable").prop("checked",false);
															}
													
													}); 												
													$("#new_row_quantityOrdered").focus();
													$("#"+aSelectedRowId+"_quantityOrdered").focus();
												}	
											});
																				
											
								        }
								    });
				                }
				            });
				      }
							},editrules:{edithidden:false,required: true}},
					{name:'inLineNoteImage', index:'inLineNoteImage', align:'center', width:10,hidden:false,editable:false,formatter: inlineNoteImageFormater},
		           	{name:'description', index:'description', align:'left', width:130, editable:true,hidden:false, edittype:'text', editoptions:{size:40,maxlength:50},editrules:{edithidden:false},  
						cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
					{name:'notes', index:'notes', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
					{name:'notesdesc', index:'notesdesc', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
					{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left',maxlength:7},editrules:{number:true,required: true}},
					{name:'quantityReceived', index:'quantityReceived', align:'center', width:40,hidden:true, editable:false, editoptions:{size:5, alignText:'right'},editrules:{edithidden:true}},
					{name:'invoicedAmount', index:'invoicedAmount', align:'center', width:40,hidden:true, editable:false, editoptions:{size:5, alignText:'right'},editrules:{edithidden:true}},
					{name:'unitCost', index:'unitCost', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right',maxlength:10}, formatter:customCurrencyFormatter, editrules:{edithidden: true}},
					{name:'priceMultiplier', index:'priceMultiplier', align:'right', width:40,hidden:false, editable:true, editoptions:{size:15, alignText:'right'}, formatter:'number', formatoptions:{decimalPlaces: 4},editrules:{number:true,required: true}},
					{name:'taxable', index:'taxable', align:'center',  width:20, hidden:false, editable:true, formatter:'checkbox', edittype:'checkbox', editrules:{edithidden:true}},
					{name:'netCast',index:'netCast',width:50 , align:'right',formatter:customCurrencyFormatter,hidden:true},
					{name:'quantityBilled', index:'quantityBilled', align:'right', width:50,hidden:false, editable:true, formatter:customCurrencyFormatter, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
					{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
					{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
					{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true,required: false}},
					{name:'posistion',index:'posistion',align:'left',width:70,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
					{name:'upAndDown',index:'upAndDown',align:'left',width:50, formatter: upAndDownImage,hidden:true},
					{name:'taxTotal', index:'taxTotal', align:'center', width:20,hidden:true},
					{name:'ackDate', index:'ackDate', align:'center', width:50,hidden:false, editable:true,   editoptions:{size:15, align:'center',},editrules:{edithidden:false}},//formatter:'date',, formatoptions:{ srcformat: 'd-m-Y',newformat:'m/d/Y'}
					{name:'shipDate', index:'shipDate', align:'center', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'center'}, editrules:{edithidden:false}},//formatter:'date',, formatoptions:{srcformat: 'd-m-Y',newformat:'m/d/Y'}
					{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
					{name:'inLineNote', index:'inLineNote', align:'center', width:20,hidden:true}],
				rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
				sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
				height:210,	width:910, rownumbers:true, altRows: true,viewrecords: true, altclass:'myAltRowClass',
				editurl:"./jobtabs5/manpulaterPOReleaseLineItem",
				caption: 'Line Item',
				
				jsonReader : {
					root: "rows",
					page: "page",
					total: "total",
					records: "records",
					repeatitems: false,
					cell: "cell",
					id: "id",
					userdata: "userdata",
					
				},
				loadBeforeSend: function(xhr) {
					posit_outside_purchaseorder= jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop();
				},
				loadComplete: function(data) {
					
					try{
						$('#lineItemGrid_inLineNoteImage').removeClass("ui-state-default ui-th-column ui-th-ltr");
						var ids = $('#lineItemGrid').jqGrid('getDataIDs');
					    if (ids) {
					    	console.log("if loop");
					        var sortName = $('#lineItemGrid').jqGrid('getGridParam','inLineNoteImage');
					        var sortOrder = $('#lineItemGrid').jqGrid('getGridParam','description');
					        for (var i=0;i<ids.length;i++) {
					        	console.log("if loop ::: "+ids[i]);
					        	$('#lineItemGrid').jqGrid('setCell', ids[i], 'inLineNoteImage', '', '',
					                        {style:'border-right-color: transparent !important;'});
					        	$('#lineItemGrid').jqGrid('setCell', ids[i], 'description', '', '',
				                        {style:'border-left-color: transparent !important;'});
					        }
					    }
					    editPOGlobalGridRowsOut = $('#lineItemGrid').getRowData();
						editPOGlobalGridOut = JSON.stringify(editPOGlobalGridRowsOut);
						}
						catch(e){
							alert(e.message);
						}
					
					var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
					var aSelectedPositionDetailID = $("#lineItemGrid").jqGrid('getCell', aSelectedRowId, 'taxTotal');
					var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
					var aVal = new Array(); 
					var aTax = new Array();
					var sum = 0;
					var taxAmount = 0;
					var aTotal = 0;
					$.each(allRowsInGrid, function(index, value) {
						aVal[index] = value.quantityBilled;
						var number1 = aVal[index].replace("$", "");
						var number2 = number1.replace(".00", "");
						var number3 = number2.replace(",", "");
						var number4 = number3.replace(",", "");
						var number5 = number4.replace(",", "");
						var number6 = number5.replace(",", "");
						sum = Number(sum) + Number(number6); 
					});
					$('#subtotalGeneralId').val(formatCurrencynodollar(sum));
					$('#subtotalLineId').val(formatCurrencynodollar(sum));
					$('#subtotalKnowledgeId').val(formatCurrencynodollar(sum));
					$.each(allRowsInGrid, function(index, value) { 
						aVal[index] = value.taxable;
						if (aVal[index] === 'Yes'){
							aTax[index] = value.quantityBilled;
							var number1 = aTax[index].replace("$", "");
							var number2 = number1.replace(".00", "");
							var number3 = number2.replace(",", "");
							var number4 = number3.replace(",", "");
							var number5 = number4.replace(",", "");
							var number6 = number5.replace(",", "");
							var taxValue = $('#taxLineId').val();
							taxAmount = taxAmount + Number(number6)*(taxValue/100);
						}
					});
					$('#generalID').val(formatCurrencynodollar(taxAmount));
					if(isNaN(aSelectedPositionDetailID)||aSelectedPositionDetailID=='false'||aSelectedPositionDetailID==false){
						aSelectedPositionDetailID = '0.00';
					}
					$('#lineID').val(formatCurrencynodollar(aSelectedPositionDetailID));
					$('#KnowledgeID').val(formatCurrencynodollar(taxAmount));
					var frieghtvalue=Number($("#freightGeneralId").val().replace(/[^0-9\.]+/g,""));
					aTotal = aTotal + sum + taxAmount +frieghtvalue;
					$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
					$('#totalLineId').val(formatCurrencynodollar(aTotal));
					$('#totalKnowledgeId').val(formatCurrencynodollar(aTotal));
					$("#lineItemGrid").trigger("reload");
					$("#freightLineId").val(formatCurrencynodollar(freight));
					$("#taxLineId").val($("#Tax_ID").text());
					var last = $(this).jqGrid('getGridParam','records');
					var hideDownIcon = Number(last)+1;
					var alignUpIcon = Number(last);
					if(last){
						$("#"+hideDownIcon+"_downIcon").css("display", "none");
						$("#"+alignUpIcon+"_upIcon").css({"position": "relative","left":"-9px","padding":"0px 12px "});
					}
					$("#lineItemGrid").trigger("reloadGrid");
					$("#Ack").trigger("reloadGrid");
					
			
				},
				gridComplete: function () {
			             jQuery("#lineItemGrid").closest(".ui-jqgrid-bdiv").scrollTop(posit_outside_purchaseorder);
			             posit_outside_purchaseorder=0;
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow:  function(id){
					id= id.replace(/[^0-9\.]+/g,"");					
					selectedLineItem = id;
				},	   
				ondblClickRow: function(id){
					var lastSel = '';
				     if(id && id!==lastSel){ 
				        jQuery(this).restoreRow(lastSel); 
				        lastSel=id; 
				     }
				},
				alertzIndex:1050
			 }).navGrid("#lineItemPager", {
					add : false,
					edit : false,
					del : true,
					alertzIndex : 10000,
					search : false,
					refresh : false,
					pager : true,
					alertcap : "Warning",
					alerttext : 'Please select a Product'
				},
				// -----------------------edit// options----------------------//
				{},
				// -----------------------add options----------------------//
				{},
				// -----------------------Delete options----------------------//
				{
					closeOnEscape: true, reloadAfterSubmit: true, modal:true, jqModal:true,width:300,left:450, top: 350, zIndex:1040,
					caption: "Delete Product",
					msg: 'Delete the Product Record?',
					beforeInitData : function(formid) {
						 if($('#POtransactionStatus').val() == '-1'){
								errorText = "You can not Delete Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
								jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
														buttons: [{height:35,text: "OK",click: function() {
															$(this).dialog("close");
															$("#cData").tigger('click');}}]}).dialog("open");
							return false;
							}
						 else if($('#POtransactionStatus').val() == '0'){
							 errorText = "You can not Delete Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
								jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
														buttons: [{height:35,text: "OK",click: function() {
															$(this).dialog("close");
															$("#cData").tigger('click');}}]}).dialog("open");
							return false;
						 }
						 else if($('#POtransactionStatus').val() == '2'){
							 errorText = "You can not Delete Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
								jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
														buttons: [{height:35,text: "OK",click: function() {
															$(this).dialog("close");
															$("#cData").tigger('click');}}]}).dialog("open");
							return false;
						 }
						 else{
						 return true;
						 }
					 },
					onclickSubmit: function(params){
						var id = jQuery("#lineItemGrid").jqGrid('getGridParam','selrow');
						var key = jQuery("#lineItemGrid").getCell(id, 16);
						var aTaxValue = $('#taxLineId').val();
						return { 'vePodetailId' : key, 'operForAck' : '' ,'taxValue' : aTaxValue};
					}
				}); 
			 $("#lineItemGrid").jqGrid(
						'inlineNav',
						'#lineItemPager',
						{
							refresh : false,
							cloneToTop : true,
							alertzIndex : 10000,
							addParams : {
								position: "last",
								addRowParams : {
									keys : false,
									oneditfunc : function() {
										console.log("edited");
										$("#del_lineItemGrid").addClass('ui-state-disabled');
										$("#new_row_ackDate").datepicker();
										$("#new_row_shipDate").datepicker();
										var vePOID = $('#vePOID').val();
										$("#new_row_vePoid").val(vePOID);
									},
									successfunc : function(response) {
										return true;
									},
									aftersavefunc : function(response) {
										
									},
									errorfunc : function(rowid, response) {
										console.log('An Error');
										return false;
									},
									afterrestorefunc : function(rowid) {
									}
								}
							},
							editParams : {
								keys : false,
								successfunc : function(response) {
									var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
				                	
				                	var celValue = $('#vePOID').val();
				                	$("#new_row_vePoid").val(celValue);
									$("#"+aSelectedRowId+"_vePoid").val(celValue);
				                	var q = $("#"+aSelectedRowId+"_quantityOrdered").val();
				                	var vv = $("#"+aSelectedRowId+"_unitCost").val();
				                	var u = vv.replace("$","").replace(",","");
				                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val();
				                	var t = (q*u)*p;
				                	
									 var rowData = $('#lineItemGrid').jqGrid('getRowData', aSelectedRowId);
							            rowData.quantityBilled = t;
							            $('#lineItemGrid').jqGrid('setRowData', aSelectedRowId, rowData);
							            $("#lineItemGrid").trigger("reload");
							            var tt = 0;
							            var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
										$.each(allRowsInGrid, function(index, value) {
											var ts = value.quantityBilled.replace("$","").replace(",","");
											console.log(">>>>> ts = "+ts);
											tt = tt+parseFloat(ts);
										});
										
										var val = (parseFloat(tt) * 8.25)/100;
										console.log("val = "+val);
										$("#lineID").val(formatCurrency(val.toFixed(2)));
										console.log(" lineId val = "+$("#lineID").val());
										$("#subtotalLineId").val(formatCurrency(tt.toFixed(2)));
										console.log(" subtotal lineId val = "+$("#subtotalLineId").val());
										var finalvl = formatCurrency( (tt+val).toFixed(2) );
										console.log(" finalVal = "+finalvl);
							            $("#totalLineId").val(finalvl);
							            $(this).trigger("reloadGrid");
							            $("#del_lineItemGrid").removeClass('ui-state-disabled');
								},
								aftersavefunc : function(id) {
									console.log('afterSavefunc editparams');
									$("#del_lineItemGrid").removeClass('ui-state-disabled');
								},
								errorfunc : function(rowid, response) {
									$("#del_lineItemGrid").removeClass('ui-state-disabled');
									return false;

								},
								afterrestorefunc : function( id ) {
									$(this).trigger("reloadGrid");
									$("#del_lineItemGrid").removeClass('ui-state-disabled');
							    },
								oneditfunc : function(id) {
									console.log('OnEditfunc');
									$("#del_lineItemGrid").removeClass('ui-state-disabled');
									$("#"+id+"_ackDate").datepicker();
									$("#"+id+"_shipDate").datepicker();
									var vePOID = $('#vePOID').val();
									var aSelectedRowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
						            $("#"+aSelectedRowId+"_vePoid").val(vePOID);
						            
						            var q = $("#"+aSelectedRowId+"_quantityOrdered").val().replace("$", "");
				                	var u = $("#"+aSelectedRowId+"_unitCost").val().replace("$", "");
				                	var p = $("#"+aSelectedRowId+"_priceMultiplier").val().replace("$", "");
				                	var l = $("#"+aSelectedRowId+"_quantityBilled").val().replace("$", "");
				                	$("#"+aSelectedRowId+"_quantityOrdered").val(q);
				                	$("#"+aSelectedRowId+"_unitCost").val(u);
				                	$("#"+aSelectedRowId+"_priceMultiplier").val(p);
				                	$("#"+aSelectedRowId+"_quantityBilled").val(l);
								}
							}
						});
			 		$("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-document", onClickButton:openLineItemNoteDialog, position: "after", title:"Inline Note", cursor: "pointer"});
			 		$("#lineItemGrid").jqGrid('navButtonAdd',"#lineItemPager",{ caption:"", buttonicon:"ui-icon-bookmark", onClickButton:showInvoicedReceived, position: "after", title:"Show Received/Invoiced", cursor: "pointer"});
			 	
		} */
		
		 function inlineNoteImageFormater(cellValue, options, rowObject){
			 console.log('Cell Value: q'+cellValue+'q');
				var element = '';
				if(cellValue !== '' && cellValue !== null && cellValue != undefined){
				   element = "<img src='./../resources/images/lineItem_new.png' title='Line Items' style='vertical-align: middle;'>";
			   }
			return element;
			}
		 
		/** Line Item Note Dialog Box **/
	
		
		function showInvoicedReceived(){
			// jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#gridid").getGridParam("colModel")[6].name);
			// jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#gridid").getGridParam("colModel")[6].name);
			//$("#lineItemGrid").trigger("reloadGrid");
			
			if(invoicedReceived==1){
				jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#lineItemGrid").getGridParam("colModel")[7].name);
				jQuery("#lineItemGrid").jqGrid('showCol', jQuery("#lineItemGrid").getGridParam("colModel")[8].name);
				$('#lineItemGrid').setGridWidth(990, true);
				invoicedReceived=0;
				}
			else
				{
				jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#lineItemGrid").getGridParam("colModel")[7].name);
				jQuery("#lineItemGrid").jqGrid('hideCol', jQuery("#lineItemGrid").getGridParam("colModel")[8].name);
				$('#lineItemGrid').setGridWidth(910, true);
				invoicedReceived=1;
				}
			
			/*  var jsonList1='';
			 var selectedRow=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
			                        var rows = jQuery("#lineItemGrid").getDataIDs();
			                        for(a=0;a<rows.length;a++)
			                        {
			                                        row=jQuery("#lineItemGrid").getRowData(rows[a]);
			                                        var notes_test = row['inLineNote'];
			                                        if(a==selectedRow){
			                                        notes_test  = textboxvalue;
			                                        }
			                                        if(a == 0)
			                                         jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
			                                else
			                                        jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
			                                        
			                        }
			                        $("#lineItemGrid").trigger("reloadGrid"); */
		}
		
		function copyOption(){
			invoicedReceived=0;
				 if($('#POtransactionStatus').val() == '-1'){
					 var newDialogDiv = jQuery(document.createElement('div'));
						errorText = "You can not Edit Line Items, \nTransaction Status is 'Void' \nChange Status to Open.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() {
													$(this).dialog("close");
													$("#cData").tigger('click');}}]}).dialog("open");
					return false;
					}
				 else if($('#POtransactionStatus').val() == '0'){
					 var newDialogDiv = jQuery(document.createElement('div'));
					 errorText = "You can not Edit Line Items, \nTransaction Status is 'Hold' \nChange Status to Open.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() {
													$(this).dialog("close");
													$("#cData").tigger('click');}}]}).dialog("open");
					return false;
				 }
				 else if($('#POtransactionStatus').val() == '2'){
					 var newDialogDiv = jQuery(document.createElement('div'));
					 errorText = "You can not Edit Line Items, \nTransaction Status is 'Close' \nChange Status to Open.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() {
													$(this).dialog("close");
													$("#cData").tigger('click');}}]}).dialog("open");
					return false;
				 }
				 else{
			console.log("Selected Lint: "+selectedLineItem);
			if(selectedLineItem != undefined && selectedLineItem!=''){
				
			var rows = jQuery("#lineItemGrid").getDataIDs();
			row=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
			var jsonListData = "{note:\""+row['note']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+"\",inLineNote:\""+row['inLineNote']+"\",inLineNoteImage:\""+row['notes']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
			console.log("Selected 2: "+selectedLineItem);
			var jsonList1='';
			var rows = jQuery("#lineItemGrid").getDataIDs();
			for(a=0;a<rows.length;a++)
			{
					row=jQuery("#lineItemGrid").getRowData(rows[a]);
					if(a == 0)
	 		        	jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+row['notes']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+row['inLineNote']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
			        else
			        	jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+row['notes']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+row['inLineNote']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
					
			}
			jsonList1=jsonList1+','+jsonListData;
			loadLinesItemGrid();
			selectedLineItem ='';
			calculateTotal()
			}else{
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:red;">Please Select a Row</b></span>');
											jQuery(newDialogDiv).dialog(
													{
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
				}
				return true;
			}
		}
		function upAndDownImage(cellValue, options, rowObject){
			var element = "<div>";
			var upIconID = options.rowId;
			var downID = options.rowId;
			var downIconID = Number(downID)+1;
			if(options.rowId === '1'){
				element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px 13px;vertical-align: middle;position: relative;left: 9px;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
				element += "<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='vertical-align: middle;'></a>";
				element += "</div>";
			}else {
				element +=	"<a id='"+upIconID+"_upIcon' onclick='upPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/upArrowLineItem.png' title='Move Up & Save'></a>";
				element += 	"<a id='"+downIconID+"_downIcon' onclick='downPOLineItem()' style='padding: 2px;vertical-align: middle;'><img src='./../resources/images/downArrowLineItem.png' title='Move Down & Save'></a>";
				element += 	"<a onclick='inlineItem()'><img src='./../resources/images/lineItem_new.png' title='Line Items' align='middle' style='padding: 2px;vertical-align: middle;'></a>";
				element += "</div>";
			}
			return element;
		}
		var newDialogDiv = jQuery(document.createElement('div'));
		function addPurchaseOrder()
		{
			var newDialogDiv = jQuery(document.createElement('div'));
			"use strict";

			$("#ButtonClicked").prop('checked', true);
			 var newDialogDiv = jQuery(document.createElement('div'));
			 if(!$('#POGeneralForm').validationEngine('validate')) {
				return false;
			}

			 /* var id = $(this).attr('id');
			if(id == undefined)
				{
				id = '';
				} 
				var id = '';
			 
			alert("Button id is---->"+id);  */
			 var IsSaveButtonClicked  = $('#ButtonClicked');
			 var aFreightVal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
			 var aFreightChk = aFreightVal.split(".");
			 var aFreightSpilt = aFreightChk[0];
			 if(aFreightSpilt.length > 5){
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Please provide 5 digit figures.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); document.getElementById('freightGeneralId').focus(); }}]}).dialog("open");
					return false;
			 }
			 var aGenerelFormValues = $("#POGeneralForm").serialize();
			 var aJobNumber = $("#jobNumber_ID").text();
			 var qbId = $("#quickbookId").val();
			 var aManufacture = $("#rxManuId").val();
			 var aShipAddress = $("#rxAddressShipID").val(); 
			 var aBillAddress = $("#rxAddressBillID").val(); 
			 var vendorname = $("#vendor_ID").text();
			 var veFactoryId = $("#veFactoryID").val();
			 var tagVal = 	$("#tagId").val(); 
			 var emailTimeStamp = $("#emailTimeStamp").text();
			 var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
			 var asubTotal = $("#subtotalGeneralId").val().replace(/[^0-9\.]+/g,"");
			 var aTaxPerc = $("#taxGeneralId").val().replace(/[^0-9\.]+/g,"");
			 var aTaxTotal = $("#generalID").val().replace(/[^0-9\.]+/g,"");
			 var transactionStatus = $('#POtransactionStatus').val();
			 var attnName = $("#contacthiddenID").val();

			 if(aTaxTotal.indexOf(".0") > -1){
				 }else{
					 aTaxTotal='0.0000';
					 }
			 /** *********  IT IS IMPORTANT CODE  ************
			  var aRequestObj = {
					 JOB_NUMBER: "",
					 Manu:"",
					 asdfdas:""
			 };
			 aRequestObj = JSON.stringify(aRequestObj);
			 ************************************************** */

			 	
			
				

				$("#lineItemGrid").jqGrid('resetSelection');	     
			   
			    var gridRows = $("#lineItemGrid").jqGrid('getRowData');
			    var rowData = new Array();
			    var jsonList1;
			    var total = 0;
			    for (var i = 0; i < gridRows.length; i++) {
			        var row = gridRows[i];
			         //var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
					total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));

					//var desc = row.description.toString().replace('"', '\\"')
					
				/* 	var desc = row.description.toString().replace(/"/g, '\\"');

					desc = escape(desc); */

					//var decsrip = row.notesdesc.replace(/\"/g, '\"');
					/* var decsrip = escape(row.notesdesc);
					var notes = escape(row.notes); */
					/* var decsrip = escape(row.notesdesc.replace(/"/g, "'"));
					var notes = escape(row.notes.replace(/"/g, "'"));
					console.log("desc--->"+decsrip);
			        if(i == 0)
			        	jsonList1="{note:\""+row.note+"\",description:\""+desc+"\",notesdesc:\""+decsrip+"\",notes:\""+notes+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";
			        else
			        	jsonList1 = jsonList1+','+"{note:\""+row.note+"\",description:\""+desc+"\",notesdesc:\""+decsrip+"\",notes:\""+notes+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";
 					*/
			       
			       
			    }
			   /* var mydata;
			   try{
				   mydata = eval ("[" + jsonList1 + "]");
				   }
			   catch(e)
			   {
				   mydata = "[" +jsonList1 + "]";
				   //alert(e.name + "\n" + e.message)
				   } */
			    //var mydata = eval ("[" + jsonList1 + "]");	
			    var dataToSend = JSON.stringify(gridRows);

			   var divflag = "#PO_Shiptooutside";

				var aPOGenerelValues = aGenerelFormValues+"&subtotalGeneralName="+asubTotal+"&freightGeneralName="+afreightTotal+"&TaxTotal="+aTaxTotal
				+"&customerBillToOtherID="+$('#customerBillToOtherID').val()+"&locationbillToAddressID="+$('#billToAddressID').val()+"&locationbillToAddress1="+$('#billToAddressID1').val()+"&locationbillToAddress2="+$('#billToAddressID2').val()+"&locationbillToCity="+$('#billToCity').val()+"&locationbillToState="+$('#billToState').val()+"&locationbillToZip="+$('#billToZipID').val()
				+"&buttonId="+id+"&vePOID="+$("#vePOID").val()+"&manfactureID="+$("#manfactureID").val()+"&taxPerc="+aTaxPerc+"&customerBillToAddressID="+$("#customerBillToAddressID").val()+"&transactionStatus="+transactionStatus+
				"&rxShiptoid="+ $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val()+"&rxShiptomodevalue="+ $(divflag).contents().find("#shiptomoderhiddenid").val();

				

				console.log("All values--->"+aPOGenerelValues);
				
			 $.ajax({
					url: './jobtabs3/addPONew?'+aPOGenerelValues,
					type : 'POST',
					data: {"tagName":$("#tagId").val(),"gridData":dataToSend},
					statusCode: {
			            404: function() {
			                alert("Employee not found");
			            },
			            500: function() {
			                alert("Failed to update Employee skills");
			            },
			            400: function(){
				            alert("Bad request");
				            }
			        },
					success: function (data){

						if("Save" == $('#setButtonValue').val())
						{
							
							var aVepoID = data.vePoid;
							if(aVepoID != null || aVepoID !='')
							{
								$("#vePO_ID").text(aVepoID);
								$("#vePOID").val(aVepoID);
								$("#ourPoId").val(data.ponumber);
								$("#rxShipToId").val(data.rxShipToId);
								$("#rxVendorContactId").val(data.rxVendorContactId);

							//	alert();
								
								$('#customerShipToID').val(data.rxShipToId);
								$('#customerBillToID').val(data.rxBillToId);								
								$("#customerBillToAddressID").val(data.rxBillToAddressId);
								$("#customerShipToAddressID").val(data.rxShipToAddressId);
								
							//	$("#rxVendorContactId").val(data.rxVendorContactId);

								if($('#customerBillToOtherID').val() == 'Other')
								{
									billToOtherName = $('#billToAddressID').val();
									billtoOtherAddress1 = $('#billToAddressID1').val();
									billtoOtherAddress2 = $('#billToAddressID2').val();
									billToOtherCity = $('#billToCity').val();
									billToOtherState = $('#billToState').val();
									billToOtherZip = $('#billToZipID').val();
		 							onChangeOther = 3;
								}

								if($('#customerShipToOtherID').val() == 'Other')
								{
									shipToOtherName = $('#shipToAddressID').val();
		 							shiptoOtherAddress1 = $('#shipToAddressID1').val();
		 							shiptoOtherAddress2 = $('#shipToAddressID2').val();
		 							shipToOtherCity = $('#shipToCity').val();
		 							shipToOtherState = $('#shipToState').val();
		 							shipToOtherZip = $('#shipToZipID').val();
		 							taxRateOther = data.taxRate;
		 							onChangeShipToOther = 3;
								}								
								
								var errorText = "";
								$('#msgDlg').show();
								$('#msgDlg').prop('display','block');
								jQuery(newDialogDiv).attr("id","msgDlg");
								jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
								if($('#ButtonClicked').is(':checked')){
									$('#showMessage').html("Saved");
									$('#ShowInfo').html("Saved");
									var new_po_generalform_values=poGeneralTabFormValidation();
									po_General_tab_form =  JSON.stringify(new_po_generalform_values);
									setTimeout(function(){
										$('#showMessage').html("");
										$('#ShowInfo').html("");
										},3000);
									$('#ButtonClicked').prop('checked', false);
								}else{
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");jQuery("#porelease").dialog("close");$("#release").trigger("reloadGrid"); }}]}).dialog("open");
								return true;
								}
								/* var msg = "Purchase Order Updated Successfully.";
								
								jQuery(newDialogDiv).attr("id", "msgDlg");
								jQuery(newDialogDiv).html(
										'<span><b style="color:red;">' + msg + '</b></span>');
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
								return false; */
								$( "#PurchaseOrderDiv ul li:nth-child(2)" ).removeClass("ui-state-disabled");
								$('#generalTabPDF').removeAttr('disabled');
					    		$('#generalTabMail').removeAttr('disabled');
					    		$('#lineTabPDF').removeAttr('disabled');
					    		$('#lineTabMail').removeAttr('disabled');
								
							}
							else
							{
								var msg = "Failed to update Purchase Order.";
								
								jQuery(newDialogDiv).attr("id", "msgDlg");
								jQuery(newDialogDiv).html(
										'<span><b style="color:red;">' + msg + '</b></span>');
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
						if("SaveandClose" == $('#setButtonValue').val())
						{
							
							document.location.href = "./po_list";
						}
						
						
					}
			 });
			 
			
		}
		 var id = '';
		 $('input[type="button"]').click(function(e){
		    id = e.target.id;
		    if("POSaveID" == id)
		    {
		    	$('#setButtonValue').val("Save");
		    }
		    if("POSaveCloseID" == id)
		    {
		    	$('#setButtonValue').val("SaveandClose");
		    }
		    
		});
		 
		 
		 function UpdateLineDates()
		 {
		 	
		 	$("#AcknowledgeAllDate").dialog("close");
		 	var sAckDate = $('#ackDateAll').val();
		 	var sShipDate = $('#shipDateAll').val();
		 	var sOrderAll = $('#orderAll').val();

			$('#ackDateAll').val("");
			$('#shipDateAll').val("");
			$('#orderAll').val("");
		 	
		 	/* var split = sAckDate.split('/');
		 	sAckDate = split[1]+'-'+split[0]+'-'+split[2];
		 	var splitShip = sShipDate.split('/');
		 	sShipDate = splitShip[1]+'-'+splitShip[0]+'-'+splitShip[2];
		 	console.log(sShipDate); */
		 	$("#lineItemGrid").jqGrid('resetSelection');
		     var gridRows = $("#lineItemGrid").getDataIDs();
		     var rowData = new Array();
		     var jsonList1;
		     var total = 0;
		     for (var i = 0; i < gridRows.length; i++) {
		         var row = gridRows[i];
		 		/* row.ackDate = sAckDate;
		 		row.shipDate = sShipDate; */
		 		$("#lineItemGrid").jqGrid('setCell',gridRows[i],'ackDate', sAckDate);
		 		$("#lineItemGrid").jqGrid('setCell',gridRows[i],'shipDate', sShipDate);
		 		$("#lineItemGrid").jqGrid('setCell',gridRows[i],'vendorOrderNumber', sOrderAll);
		        /*  if(i == 0)
		         	jsonList1="{note:\""+row.note+"\",description:\""+row.description+"\",notes:\""+row.notes+"\",notesdesc:\""+row.notesdesc+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+sOrderAll+"\",prMasterId:\""+row.prMasterId+"\"}";
		         else
		         	jsonList1 = jsonList1+','+"{note:\""+row.note+"\",description:\""+row.description+"\",notes:\""+row.notes+"\",notesdesc:\""+row.notesdesc+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+sOrderAll+"\",prMasterId:\""+row.prMasterId+"\"}";
 				*/
		        
		         
		         
		     }

		     
		 }
		 function openAckShipPopup()
		 {
		 	/* $("#AcknowledgeAllDate").show(); */
		 	$("#AcknowledgeAllDate").prop('disabled',false);
		 	$("#AcknowledgeAllDate").dialog("open");
		 }
		 function calculateSubTot()
		 {
		     var gridRows = $("#lineItemGrid").jqGrid('getRowData');
		     var rowData = new Array();
		     var jsonList1;
		     var total = 0;
		     var taxTot = 0;
		     for (var i = 0; i < gridRows.length; i++) {
		         var row = gridRows[i];
		 		total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,"")); 
		 		if(row.taxable == "on" || row.taxable == "Yes")
				{
					taxTot = parseFloat(taxTot)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
				}    
		        
		     }

		    var taxPerc = 0;
		    var taxValue = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
		    var taxCost = (parseFloat(taxTot)*parseFloat(taxValue))/100;
		    $('#lineID').val(formatCurrencynodollar(taxCost));
		    $('#generalID').val($('#lineID').val());
		     
		     $('#subtotalLineId').val(formatCurrencynodollar(total ));
		     $('#subtotalKnowledgeId').val($('#subtotalLineId').val());
		 	$('#subtotalGeneralId').val($('#subtotalLineId').val());

		 	
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());
		 			 	
		 }
		 function loadDates()
			{
				$('#poDateLineId').val($('#poDateId').val());
				//$('#poDateAckId').val($('#poDateId').val());
				$('#ourPoLineId').val($('#ourPoId').val());
				//$('#ourPoAckId').val($('#ourPoId').val());
				$('#vendorLineNameId').val($('#vendorsearch').val());
				$('#taxLineId').val($('#taxGeneralId').val());
				$('#lineID').val($('#generalID').val());				 
				$('#freightLineId').val($('#freightGeneralId').val());
				$('#subtotalLineId').val($('#subtotalGeneralId').val());
				$('#totalLineId').val($('#totalGeneralId').val());

			}

		
		 function CancelLineDates()
		 {
		 	$("#AcknowledgeAllDate").dialog("close");
		 	$('#ackDateAll').val("");
		 	$('#shipDateAll').val("");
		 }

		 var billToCustomer,shipToCustomer;
		 $(function() { var cache = {}; var lastXhr='';

		 $("#billToAddressID").autocomplete({ minLength: 1,timeout :1000,
		 	/* open: function(){ 
		 		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./test?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		 		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 	}, */
		 select: function (event, ui) {
		 	var aValue = ui.item.value;
		 	var valuesArray = new Array();
		 	valuesArray = aValue.split("|");
		 	var id = valuesArray[0];
		 	var code = valuesArray[2];
		 	var sManu = '<option value="-1"> - Select - </option>';
		 	var sAttn = '<option value="-1"> - Select - </option>';
		 	$('#customerBillToID').val(aValue);
		 	 $.ajax({
		 	        url: './getCustomerAddress?rxMasterId='+aValue,
		 	        type: 'POST',       
		 	        success: function (data) {

		 				$.each(data, function(key, valueMap) {
		 					/* private int veFactoryID;
		 					private String description;
		 					private boolean inActive;
		 					private int rxMasterId; */
		 					
		 					
		 					if("customerAddress"==key)
		 					{				
		 						billToCustomer = valueMap;		
		 						$.each(valueMap, function(index, value){							
		 							$('#billToAddressID').val(value.name);
		 							$('#billToAddressID1').val(value.address1);
		 							$('#billToAddressID2').val(value.address2);
		 							$('#billToCity').val(value.city);
		 							$('#billToState').val(value.state);
		 							$('#billToZipID').val(value.zip);

		 							billToName = value.name
		 							billtoAddress1 = value.address1;
		 							billtoAddress2 = value.address2;
		 							billToCity = value.city;
		 							billToState = value.state;
		 							billToZip = value.zip;
		 							onChange = 2;
		 							//$('#customerBillToAddressID').val(value.rxAddressId);

		 							/* var theRxaddress = "${theRxaddress}";
		 							alert("New Try---->"+"${theRxaddress}");
		 							 //$('#billToAddressID').val("${billToName}");
		 							 alert("aaa--->"+value.address1);
		 							alert("aaa JSTL Before--->"+"${theRxaddress.address1}");
		 							 "${theRxaddress.address1}" = value.address1;
		 							alert("aaa JSTL After--->"+"${theRxaddress.address1}");
		 							theRxaddress.address1 = value.address1
		 							theRxaddress.address2 = value.address2
		 							theRxaddress.city = value.city
		 							theRxaddress.state = value.state
		 							theRxaddress.zip = value.zip */
		 							
		 							//billToCustomerAddresses.pop();  

		 							//billToCustomerAddresses.push({name: value.name, address1: value.address1, address2: value.address2, city: value.city, state: value.state, zip: value.zip});
		 						
		 						}); 
		 						
		 					}
		 					

		 					
		 				});
		 				
		 	        }
		 	    }); 
		 	
		 	//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
		 },
		 source: function( request, response ) { 
		 	
		 	if($('#customerBillToOtherID').val() != "Other")
		 	{
		 		var term = request.term;
		 		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		 		lastXhr = $.getJSON( "search/customerAddress?rxMasterId="+$('#vendorsearchRXMasterID').val(), request, function( data, status, xhr ) { cache[ term ] = data; 
		 			if ( xhr === lastXhr ) { response( data ); 	} });
		 	}
		 },
		 error: function (result) {
		      $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 } });
		 $("#shipToAddressID").autocomplete({ minLength: 1,timeout :1000,
		 	/* open: function(){ 
		 		$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./test?token=new" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Inventory</a></b></div>');
		 		$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 	}, */
		 select: function (event, ui) {
		 	var aValue = ui.item.value;
		 	var valuesArray = new Array();
		 	valuesArray = aValue.split("|");
		 	
		 	var id = valuesArray[0];
		 	var code = valuesArray[2];
		 	var sManu = '<option value="-1"> - Select - </option>';
		 	var sAttn = '<option value="-1"> - Select - </option>';
		 	$('#customerShipToID').val(aValue);
		 	 $.ajax({
		 	        url: './getCustomerAddress?rxMasterId='+aValue,
		 	        type: 'POST',       
		 	        success: function (data) {

		 				$.each(data, function(key, valueMap) {
		 					/* private int veFactoryID;
		 					private String description;
		 					private boolean inActive;
		 					private int rxMasterId; */
		 					
		 					
		 					if("customerAddress"==key)
		 					{				
		 						shipToCustomer = valueMap;		
		 						$.each(valueMap, function(index, value){						

		 							$('#shipToAddressID').val(value.name);
		 							$('#shipToAddressID1').val(value.address1);
		 							$('#shipToAddressID2').val(value.address2);
		 							$('#shipToCity').val(value.city);
		 							$('#shipToState').val(value.state);
		 							$('#shipToZipID').val(value.zip);
		 							//$('#customerShipToAddressID').val(value.rxAddressId);
		 							

		 							shipToName = value.name
		 							shiptoAddress1 = value.address1;
		 							shiptoAddress2 = value.address2;
		 							shipToCity = value.city;
		 							shipToState = value.state;
		 							shipToZip = value.zip;
		 							onChangeShipTo = 2;
		 							
		 							
		 							
		 						}); 
		 						
		 					}

		 					if("taxRateforCity" == key)
							{
								$.each(valueMap, function(index, value){
									$('#taxGeneralId').val(formatCurrencynodollar(value));
									taxRateShipTo = value;
								
								}); 
							}
		 					

		 					
		 				});
		 				
		 	        }
		 	    }); 
		 	
		 	//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
		 },
		 source: function( request, response ) {

		 	if($('#customerShipToOtherID').val() != "Other")
		 	{
		 		var term = request.term;
		 		if ( term in cache ) { response( cache[ term ] ); 	return; 	}
		 		lastXhr = $.getJSON( "search/customerAddress?rxMasterId="+$('#vendorsearchRXMasterID').val(), request, function( data, status, xhr ) { cache[ term ] = data; 
		 			if ( xhr === lastXhr ) { response( data ); 	} });
		 	}
		 },
		 error: function (result) {
		      $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		 } });
		 });



		 function frieghtCost()
		 {
		 	var frtAmt = $('#freightGeneralId').val().replace(/[^0-9\.]+/g,"");
		 	var subTot = $('#subtotalGeneralId').val().replace(/[^0-9\.]+/g,"");
		 	var taxTot = $('#generalID').val().replace(/[^0-9\.]+/g,"");
		 	
		 	$('#freightKnowledgeId').val($('#freightGeneralId').val());
		 	$('#freightLineId').val($('#freightGeneralId').val());
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat(subTot)+parseFloat(frtAmt)+parseFloat(taxTot)));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());

		 	
		 	
		 	
		 }
		 function frieghtCostLine()
		 {
		 	var frtAmt = $('#freightLineId').val().replace(/[^0-9\.]+/g,"");
		 	var subTot = $('#subtotalGeneralId').val().replace(/[^0-9\.]+/g,"");
		 	var taxTot = $('#lineID').val().replace(/[^0-9\.]+/g,"");

		 	$('#freightKnowledgeId').val($('#freightLineId').val());
		 	$('#freightGeneralId').val($('#freightLineId').val());
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat(subTot)+parseFloat(frtAmt)+parseFloat(taxTot)));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());	
		 }
		 function frieghtFormat()
		 {
		 	
		 	var frtAmt = $('#freightGeneralId').val().replace(/[^0-9\.]+/g,"");
		    	var subTot = $('#subtotalGeneralId').val().replace(/[^0-9\.]+/g,"");
		    	var taxTot = $('#lineID').val().replace(/[^0-9\.]+/g,"");
		    	$('#freightLineId').val(formatCurrencynodollar($('#freightGeneralId').val()));
		    	$('#freightKnowledgeId').val(formatCurrencynodollar($('#freightGeneralId').val()));
		    	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat(subTot)+parseFloat(frtAmt)+parseFloat(taxTot)));
		    	$('#totalLineId').val($('#totalGeneralId').val());
		    	$('#totalKnowledgeId').val($('#totalGeneralId').val());
		 }

		 function frieghtFormatLine()
		 {
		 	var frtAmt = $('#freightLineId').val().replace(/[^0-9\.]+/g,"");
		 	var subTot = $('#subtotalGeneralId').val().replace(/[^0-9\.]+/g,"");
		 	var taxTot = $('#lineID').val().replace(/[^0-9\.]+/g,"");

		 	$('#freightGeneralId').val(formatCurrencynodollar($('#freightLineId').val()));
		   	$('#freightKnowledgeId').val(formatCurrencynodollar($('#freightLineId').val()));
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat(subTot)+parseFloat(frtAmt)+parseFloat(taxTot)));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());
		 }
		 function taxCost()
		 {
		 	var taxAmt = $('#taxGeneralId ').val().replace(/[^0-9\.]+/g,"");
		 	$('#taxLineId ').val(formatCurrencynodollar(taxAmt));	
		 }
		 function taxCostLine()
		 {
		 	var taxAmt = $('#taxLineId ').val().replace(/[^0-9\.]+/g,"");
		 	$('#taxGeneralId ').val(formatCurrencynodollar(taxAmt));	
		 	
		 }
		 function taxFormat()
		 {
		 	var taxAmt = $('#taxGeneralId').val().replace(/[^0-9\.]+/g,"");
		 	$('#taxLineId ').val(formatCurrencynodollar(taxAmt));

		 	$("#lineItemGrid").jqGrid('resetSelection');
		     var gridRows = $("#lineItemGrid").jqGrid('getRowData');
		     var rowData = new Array();
		     var jsonList1;
		     var total = 0;
		     var taxTot = 0;
		     for (var i = 0; i < gridRows.length; i++) {
		         var row = gridRows[i];
		 		total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));    
		 		if(row.taxable == "on" || row.taxable == "Yes")
		 		{
		 			taxTot = parseFloat(taxTot)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
		 		}  
		        
		     }

		     var taxPerc = 0;
		     var taxValue = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
		     var taxCost = (parseFloat(taxTot)*parseFloat(taxValue))/100;
		     $('#lineID').val(formatCurrencynodollar(taxCost));
		     $('#generalID').val($('#lineID').val());
		     
		     $('#subtotalLineId').val(formatCurrencynodollar(total ));
		     $('#subtotalKnowledgeId').val($('#subtotalLineId').val());
		 	$('#subtotalGeneralId').val($('#subtotalLineId').val());

		 	
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());
		    	
		 }
		 function taxFormatLine()
		 {
		 	
		 	var taxAmt = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
		 	$('#taxGeneralId ').val(formatCurrencynodollar(taxAmt));

		 	$("#lineItemGrid").jqGrid('resetSelection');
		     var gridRows = $("#lineItemGrid").jqGrid('getRowData');
		     var rowData = new Array();
		     var jsonList1;
		     var total = 0;
		     var taxTot = 0;
		     for (var i = 0; i < gridRows.length; i++) {
		         var row = gridRows[i];
		 		total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));    
		 		if(row.taxable == "on" || row.taxable == "Yes")
		 		{
		 			taxTot = parseFloat(taxTot)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
		 		}  
		        
		     }

		     var taxPerc = 0;
		     var taxValue = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
		     var taxCost = (parseFloat(taxTot)*parseFloat(taxValue))/100;
		     $('#lineID').val(formatCurrencynodollar(taxCost));
		     $('#generalID').val($('#lineID').val());
		     
		     $('#subtotalLineId').val(formatCurrencynodollar(total ));
		     $('#subtotalKnowledgeId').val($('#subtotalLineId').val());
		 	 $('#subtotalGeneralId').val($('#subtotalLineId').val());

		 	
		 	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
		 	$('#totalLineId').val($('#totalGeneralId').val());
		 	$('#totalKnowledgeId').val($('#totalGeneralId').val());
		    	
		 }

		 function viewPOPDF(quote){
			 if($('#POtransactionStatus').val() == '-1'){
					errorText = "You can not view PDF, \nTransaction Status is 'Void'\nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				}
				else if($('#POtransactionStatus').val() == '0'){
						 errorText = "You can not view PDF, \nTransaction Status is 'Hold' \nChange Status to Open.";
							jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
													buttons: [{height:35,text: "OK",click: function() {
														$(this).dialog("close");
														$("#cData").tigger('click');}}]}).dialog("open");
						return false;
				}
				 else if($('#POtransactionStatus').val() == '2'){
					 errorText = "You can not view PDF, \nTransaction Status is 'Close' \nChange Status to Open.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() {
													$(this).dialog("close");
													$("#cData").tigger('click');}}]}).dialog("open");
					return false;
				 }
				else{
			 if(quote == 'purchase'){
				 try{
					 var vePOID = $("#vePOID").val();
						if(vePOID === null || vePOID.length<= 0){
							errorText = "Please Save Purchase Order to View PDF.";
							jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
													buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
							return false;
						}
						var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToId}";
						var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
						var joReleaseId = "";//"${theVepo.joReleaseId}";
						var jobNumber = "";
						var rxCustomerId = "";
						var joMasterID=0;
					$.ajax({
						type : "GET",
						url : "./purchasePDFController/viewPDFLineItemForm",
						data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : quote, 'jobNumber' :  jobNumber, 'rxMasterID' : rxCustomerId, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :'0' },
						documenttype: "application\pdf",
						async: false,
						cache: false,
						success : function (msg) {
						},
						error : function (msg) {}
					});
				 }catch(err){
					 alert(err.message);
				 }
			} else{
				var vePOID = $("#vePOID").val();
				if(vePOID === null || vePOID.length<= 0){
					errorText = "Please Save Purchase Order to View PDF.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				var aPDFType = "Po";
				var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToId}";
				var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
				var joReleaseId = "${theVepo.joReleaseId}";//"${theVepo.joReleaseId}";
				var jobNumber = "";
				var rxCustomerId = "";
				$.ajax({
					type: "GET",
					url: "./jobtabs5/getjomasterbyjoreleaseid",
					data: { 'joReleaseID': joReleaseId},
					dataType: "json",
					success: function (data) {
						var joMasterID=0;
						var jobNumber="";
						if(data!=null && data.joMasterID!=null){
							jobNumber =data.jobNumber;
							joMasterID=data.joMasterID;
							}
						
						
						//rxCustomerId = "11671";//data.rxCustomerId;
						if($('#customerShipToID').val() !== undefined)
						{
							rxCustomerId = $('#customerShipToID').val();
						}
						window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxCustomerId+"&manufacturerID="+aManufacturerId+"&shipToAddrID=0"+"&joMasterID="+joMasterID);
					},
					error: function (msg) {
						alert("fail--->"+msg);
						
					}
				});
				
			}
			}
		 }
	 
		 function viewPOPDFVoid(quote){
			 if(quote == 'purchase'){
				 try{
					 var vePOID = $("#vePOID").val();
						if(vePOID === null || vePOID.length<= 0){
							errorText = "Please Save Purchase Order to View PDF.";
							jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
													buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
							return false;
						}
						var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToId}";
						var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
						var joReleaseId = "";//"${theVepo.joReleaseId}";
						var jobNumber = "";
						var rxCustomerId = "";
						var joMasterID=0;
					$.ajax({
						type : "GET",
						url : "./purchasePDFController/viewPDFLineItemFormVoid",
						data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : quote, 'jobNumber' :  jobNumber, 'rxMasterID' : rxCustomerId, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :aShipToAddrID },
						documenttype: "application\pdf",
						async: false,
						cache: false,
						success : function (msg) {
						},
						error : function (msg) {}
					});
				 }catch(err){
					 alert(err.message);
				 }
			} else{
				var vePOID = $("#vePOID").val();
				if(vePOID === null || vePOID.length<= 0){
					errorText = "Please Save Purchase Order to View PDF.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				var aPDFType = "Po";
				var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToId}";
				var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
				var joReleaseId = "";//"${theVepo.joReleaseId}";
				var jobNumber = "";
				var rxCustomerId = "";
				$.ajax({
					type: "GET",
					url: "./jobtabs5/getjomasterbyjoreleaseid",
					data: { 'joReleaseID': joReleaseId},
					dataType: "json",
					success: function (data) {
						//jobNumber = "EHB1403-001";//data.jobNumber;
						//rxCustomerId = "11671";//data.rxCustomerId;
						if($('#customerShipToID').val() !== undefined)
						{
							rxCustomerId = $('#customerShipToID').val();
						}
						var joMasterID=0;
						if(data!=null && data.joMasterID!=null){
							jobNumber =data.jobNumber;
							joMasterID=data.joMasterID;
							}
						window.open("./purchasePDFController/viewPDFLineItemFormVoid?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxCustomerId+"&manufacturerID="+aManufacturerId+"&shipToAddrID="+aShipToAddrID+"&joMasterID="+joMasterID);
					},
					error: function (msg) {
						alert("fail--->"+msg);
						
					}
				});
				
			}
		 }
		 
function sendPOEmail(poGeneralKey){
				var aQuotePDF = "purchase";
				var vePoId = $("#vePOID").val();
				var aContactID = $("#contacthiddenID").val();
				//alert(aContactID);
				var errorText = '';
				
				if($('#POtransactionStatus').val() == '-1'){
					errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
				}
				else if($('#POtransactionStatus').val() == '0'){
						 errorText = "You can not Send E-Mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
							jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
													buttons: [{height:35,text: "OK",click: function() {
														$(this).dialog("close");
														$("#cData").tigger('click');}}]}).dialog("open");
						return false;
				}
				 else if($('#POtransactionStatus').val() == '2'){
					 errorText = "You can not Send E-Mail, \nTransaction Status is 'Close' \nChange Status to Open.";
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
												buttons: [{height:35,text: "OK",click: function() {
													$(this).dialog("close");
													$("#cData").tigger('click');}}]}).dialog("open");
					return false;
				 }
				else{
				var cusotmerPONumber = $("#ourPoId").val();
				if(vePoId === null || vePoId.length<= 0){
					errorText = "Please Save Purchase Order to Email Purchase Order.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				if(aContactID === null && aContactID === '' && aContactID === '-1'){
					errorText = "Please Add Contact for Purchase Order.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}else{

					$.ajax({ 
						url: "./vendorscontroller/GetContactDetails",
						mType: "GET",
						data : { 'rxContactID' : aContactID},
						success: function(data){
							var aFirstname = data.firstName;
							var aLastname = data.lastName;
							var aEmail = data.email;
							var arxContact = aFirstname + ' '+aLastname;
							
							if(aEmail === ''){
								errorText = "Are you sure you want to send this PO to"+ arxContact +"?";
							}else{
								errorText = "Are you sure you want to send this PO to"+ arxContact +"("+ aEmail +")?";
							}
							jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:400, height:150, title:"Message", 
							buttons:{
								"Send": function(){
									$('#loadingPODiv').css({"visibility": "visible"});
									viewPOPDF(aQuotePDF);
									sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId);
									jQuery(this).dialog("close");
								},
								Cancel: function ()	{jQuery(this).dialog("close");}}}).dialog("open");
							
						}
					});
					}
				return true;
			}
			}
			function sentMailPOFunction(aContactID,aEmail, poGeneralKey, cusotmerPONumber, vePoId){
				if(aEmail === ''){
					var errorText = "Please Provide a Mail ID.";
					jQuery(newDialogDiv).html('<form id="mailToAddress"><table><tr><td><span><b style="color:red;">'+errorText+'</b></span></td></tr>'+
							'<tr><td style="height: 5px;"></td></tr>' +
					'<tr><td><label>Mail ID: </label><input type="text" id="mailToAddress_ID" name="mailToAddress_name" class="validate[custom[email]]" style="width: 250px;" /></td></tr></table></form><hr>');
					jQuery(newDialogDiv).dialog({modal: true, width:430, height:180, title:"Message", 
						buttons:{
							"Submit & Send": function(){
								if(!$('#mailToAddress').validationEngine('validate')) {
									return false;
								}
								var aEmailAddress = $("#mailToAddress_ID").val();
								saveMailAddress(aEmailAddress, aContactID);
								$('#loadingPODiv').css({"visibility": "visible"});
								$.ajax({ 
									url: "./sendMailServer/sendPOMail",
									mType: "POST",
									data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
									success: function(data){
										$('#loadingPODiv').css({"visibility": "hidden"});
										var errorText = "Mail Sent Successfully.";
										jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
										jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
										buttons: [{height:35,text: "OK",click: function() {
											var today = new Date();
											var dd = today.getDate();
											var mm = today.getMonth()+1; 
											var yyyy = today.getFullYear().toString().substr(2,2);
											var hours = today.getHours();
											var minutes = today.getMinutes();
											var ampm = hours >= 12 ? 'PM' : 'AM';
											hours = hours % 12;
											hours = hours ? hours : 12;
											if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
											if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
											$.ajax({ 
												url: "./jobtabs3/updateEmailStampValue",
												mType: "GET",
												data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
												success: function(data){ 
													$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
													$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today);
													
												 }
											});
											$(this).dialog("close"); 
										}}] }).dialog("open");
									}
								});
								jQuery(this).dialog("close");
								return true;
							},
							Cancel: function ()	{jQuery(this).dialog("close");}
						}}).dialog("open");
				}else{
					
					if(poGeneralKey === 'poGeneral'){
						$('#loadingPOGenDiv').css({"visibility": "visible"});
						$.ajax({ 
							url: "./sendMailServer/sendPOMail",
							mType: "POST",
							data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
							success: function(data){
								$('#loadingPOGenDiv').css({"visibility": "hidden"});
								$('#loadingPODiv').css({"visibility": "hidden"});
								var errorText = "Mail Sent Successfully.";
								jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
									buttons: [{height:35,text: "OK",click: function() {
										var today = new Date();
										var dd = today.getDate();
										var mm = today.getMonth()+1; 
										var yyyy = today.getFullYear().toString().substr(2,2);
										var hours = today.getHours();
										var minutes = today.getMinutes();
										var ampm = hours >= 12 ? 'PM' : 'AM';
										hours = hours % 12;
										hours = hours ? hours : 12;
										if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
										if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
										$.ajax({ 
											url: "./jobtabs3/updateEmailStampValue",
											mType: "GET",
											data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
											success: function(data){ 
												$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
												$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); }
										});
										$(this).dialog("close"); 
									}}] }).dialog("open");
							}
						});
					}else{
						$.ajax({ 
							url: "./sendMailServer/sendPOMail",
							mType: "POST",
							data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber},
							success: function(data){
								$('#loadingPODiv').css({"visibility": "hidden"});
								var errorText = "Mail Sent Successfully.";
								jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
								jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
									buttons: [{height:35,text: "OK",click: function() {
										var today = new Date();
										var dd = today.getDate();
										var mm = today.getMonth()+1; 
										var yyyy = today.getFullYear().toString().substr(2,2);
										var hours = today.getHours();
										var minutes = today.getMinutes();
										var ampm = hours >= 12 ? 'PM' : 'AM';
										hours = hours % 12;
										hours = hours ? hours : 12;
										if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
										if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
										$.ajax({ 
											url: "./jobtabs3/updateEmailStampValue",
											mType: "GET",
											data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
											success: function(data){ $("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
											$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); }
										});
										$(this).dialog("close"); 
									}}] }).dialog("open");
							}
						});
					}
				}
			}

			function saveMailAddress(aEmailAddress, aContactID){
				$.ajax({ 
					url: "./jobtabs2/updateEmailAddress",
					mType: "GET",
					data : { 'email' : aEmailAddress, 'contactID' : aContactID },
					success: function(data){
					}
				});
			}

			function formatCurrencynodollar(strValue)
			{
				if(strValue === "" || strValue == null){
					return "0.00";
				}
				strValue = strValue.toString().replace(/\$|\,/g,'');
				dblValue = parseFloat(strValue);

				blnSign = (dblValue == (dblValue = Math.abs(dblValue)));
				dblValue = Math.floor(dblValue*100+0.50000000001);
				intCents = dblValue%100;
				strCents = intCents.toString();
				dblValue = Math.floor(dblValue/100).toString();
				if(intCents<10){
					strCents = "0" + strCents;
				}
				for (var i = 0; i < Math.floor((dblValue.length-(1+i))/3); i++){
					dblValue = dblValue.substring(0,dblValue.length-(4*i+3))+','+
					dblValue.substring(dblValue.length-(4*i+3));
				}
				return (((blnSign)?'':'-') + dblValue + '.' + strCents);
			}
			/* $('#shiptoradio4').click(function() {
				  $('#shipToAddressID').focus();
				}) */
				
		$('.ui-state-active').onclick = function() {
        $('#shipToAddressID').focus();
    }

		function loadLineItems()
		{	
		    var gridRows = $("#lineItemGrid").jqGrid('getRowData');
		    var rowData = new Array();
		    var jsonList1;
		    var total = 0;
		    var jsonList1;
		    var total = 0;
		    var taxTot = 0.0;
		    var gridLen = gridRows.length;
		    for (var i = 0; i < gridRows.length; i++) {
		        var row = gridRows[i]; 
				if($('#customerBillToOtherID').val() === 'Us')// && (postdata.taxable == 'Yes' || postdata.taxable == 'on')
			    {
					row.taxable = 'off';
			    }
				else
					row.taxable = 'on';
				total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,"")); 
				if(row.taxable == "on" || row.taxable == "Yes")
				{
					taxTot = parseFloat(taxTot)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
				} 
				
		        if(i == 0)
		        	jsonList1="{note:\""+row.note+"\",description:\""+row.description+"\",notes:\""+row.notes+"\",notesdesc:\""+row.notesdesc+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";
		        else
		        	jsonList1 = jsonList1+','+"{note:\""+row.note+"\",description:\""+row.description+"\",notes:\""+row.notes+"\",notesdesc:\""+row.notesdesc+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";     
		        
		        
		    }
		    if(gridLen > 0)
		    {
		    	var taxPerc = 0;
			    var taxValue = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
			    var taxCost = 0.0;
			    if($('#customerBillToOtherID').val() !== 'Us')
			    {
			    	taxCost = (parseFloat(taxTot)*parseFloat(taxValue))/100;
			    }

			    $('#lineID').val(formatCurrencynodollar(taxCost));
			    $('#generalID').val($('#lineID').val());
			    $('#subtotalLineId').val(formatCurrencynodollar(total ));
			    $('#subtotalKnowledgeId').val($('#subtotalLineId').val());	
			    $('#subtotalGeneralId').val($('#subtotalLineId').val());

			    $('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
				$('#totalLineId').val($('#totalGeneralId').val());
				$('#totalKnowledgeId').val($('#totalGeneralId').val());
			    loadLinesItemGrid();
		    }
		    
		}
		
		
		function callPOStatus(){
			try{
				var dataid = $('#POtransactionStatus').val();
				console.log(dataid);
			    var newDialogDiv = jQuery(document.createElement('div'));
			    jQuery(newDialogDiv).attr("id", "showPOOptions");
			    jQuery(newDialogDiv).html('<span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Void" onclick="onSetPOStatus(this.value)" value="Void"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Hold" onclick="onSetPOStatus(this.value)" value="Hold"></span><br><br><span><input type = "button" style="width: 85%;" class="cancelhoverbutton turbo-tan" id="Open" onclick="onSetPOStatus(this.value)" value="Open"></span><br><br><span><input type = "button" class="cancelhoverbutton turbo-tan" style="width: 85%;" id="Close" onclick="onSetPOStatus(this.value)" value="Close" /></span>');
			  
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 200,
					height : 220,
					title : "PO Status",
					buttons : [  ]
				}).dialog("open");
				
				$('div#showPOOptions').bind('dialogclose', function(event) {
					$("#showPOOptions").dialog("destroy").remove();
				 });
				console.log('dataid'+dataid);
				  if(dataid=='2'){
					  console.log('dataid inside'+dataid);
					   	$('#Close').css("font-weight","bold");
				    	$('#Close').css("background","#0E2E55");
				    }else  if(dataid=='0'){
					   	$('#Hold').css("font-weight","bold");
				    	$('#Hold').css("background","#0E2E55");
				    }else  if(dataid=='1'){
					   	$('#Open').css("font-weight","bold");
				    	$('#Open').css("background","#0E2E55");
				    }else  if(dataid=='-1'){
					   	$('#Void').css("font-weight","bold");
				    	$('#Void').css("background","#0E2E55");
				    }		
			}catch(err){
				
				}
		}

		function onSetPOStatus(e){
			
			$("#showPOOptions").dialog("destroy").remove();
			var setStatus=0;
			var vePoid =  $('#vePOID').val();
			switch(e)
			{
			case 'Void':
				$('#PoStatusButton').val('Void');
				setStatus = -1;
				break;
			case 'Hold':
				$('#PoStatusButton').val('Hold');
				setStatus = 0;
				break;
			case 'Open':
				$('#PoStatusButton').val('Open');
				setStatus = 1;
				break;
			default:
				$('#PoStatusButton').val('Close');
				setStatus = 2;
				break;
			}
			if(setStatus==-1){
				
				 var newDialogDiv = jQuery(document.createElement('div'));
				    jQuery(newDialogDiv).attr("id", "showPDFoptiondialog");
				    jQuery(newDialogDiv).html('<span><label>Do you wish to print a cancel ticket?</label></span>');
				  
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 200,
						height : 250,
						title : "Cancel Ticket",
						buttons : [{height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();viewPOPDFVoid();}},
						           {height:35,text: "No",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();}},
						           {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();}}]
					}).dialog("open");
				
			}
			
			$('#showPOOptions').dialog('destroy').remove();
			if(setStatus==2){
				$.ajax({
				url: "./getPOinvoiceStatus",
				type: "POST",
				data :{vePoID:vePoid},
				success: function(data) {
					console.log("QtyOrdered: "+data);
					var orderedBilled = data.split(',');
					if(orderedBilled[0]!=orderedBilled[1]){
					 var newDialogDiv = jQuery(document.createElement('div'));
					    jQuery(newDialogDiv).attr("id", "showPOdialog");
					    jQuery(newDialogDiv).html('<span><label>Your Po Items not Invoiced !\n Do you wish Close Purchase Order? </label></span>');
					  
						jQuery(newDialogDiv).dialog({
							modal : true,
							width : 400,
							height : 150,
							title : "Close PO",
							buttons : [
							        {height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();
									$('#PoStatusButton').val('Close');
									setStatus=2;
									$('#Close').css("font-weight","bold");
					    			$('#Close').css("background","#0E2E55");
									}},
							        {height:35,text: "No",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();
							        $('#PoStatusButton').val('Open');
							        setStatus=1;
							       	$('#Open').css("font-weight","bold");
							    	$('#Open').css("background","#0E2E55");}},
							        {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();
							        $('#PoStatusButton').val('Open');
							        setStatus=1;
							       	$('#Open').css("font-weight","bold");
							    	$('#Open').css("background","#0E2E55");
							    	}}]
						}).dialog("open");
				}
					else{
						setStatus=2;
					}
				}
			});
			}
			$('#POtransactionStatus').val(setStatus);
			}
		function calculateTotal(){
			var allRowsInGrid = $('#lineItemGrid').jqGrid('getRowData');
			var aVal = new Array(); 
			var aTax = new Array();
			var sum = 0;
			var taxAmount = 0;
			var aTotal = 0;
			$.each(allRowsInGrid, function(index, value) {
				aVal[index] = value.quantityBilled;
				var number1 = aVal[index].replace("$", "");
				var number2 = number1.replace(".00", "");
				var number3 = number2.replace(",", "");
				var number4 = number3.replace(",", "");
				var number5 = number4.replace(",", "");
				var number6 = number5.replace(",", "");
				sum = Number(sum) + Number(number6); 
			});
			$('#subtotalGeneralId').val(formatCurrencynodollar(sum));
			$('#subtotalLineId').val(formatCurrencynodollar(sum));
			$('#subtotalKnowledgeId').val(formatCurrencynodollar(sum));
			$.each(allRowsInGrid, function(index, value) { 
				aVal[index] = value.taxable;
				if (aVal[index] === 'Yes'){
					aTax[index] = value.quantityBilled;
					var number1 = aTax[index].replace("$", "");
					var number2 = number1.replace(".00", "");
					var number3 = number2.replace(",", "");
					var number4 = number3.replace(",", "");
					var number5 = number4.replace(",", "");
					var number6 = number5.replace(",", "");
					var taxValue = $('#taxLineId').val();
					taxAmount = taxAmount + Number(number6)*(taxValue/100);
				}
			});
			$('#generalID').val(formatCurrencynodollar(taxAmount));
			$('#KnowledgeID').val(formatCurrencynodollar(taxAmount));
			var freightLineVal= $('#freightLineId').val();
			var number1 = freightLineVal.replace("$", "");
			var number2 = number1.replace(".00", "");
			var number3 = number2.replace(",", "");
			var number4 = number3.replace(",", "");
			var number5 = number4.replace(",", "");
			var number6 = number5.replace(",", "");
			var frieghtvalue=Number($("#Freight_ID").text().replace(/[^0-9\.]+/g,""));
			aTotal = aTotal + sum + taxAmount + Number(number6)+frieghtvalue;
			$('#totalGeneralId').val(formatCurrencynodollar(aTotal));
			$('#totalLineId').val(formatCurrencynodollar(aTotal));
			$('#totalKnowledgeId').val(formatCurrencynodollar(aTotal));
			$("#lineItemGrid").trigger("reload");
			$("#freightLineId").val(formatCurrencynodollar(freight));
			$("#taxLineId").val($("#Tax_ID").text());
			var last = $(this).jqGrid('getGridParam','records');
			var hideDownIcon = Number(last)+1;
			var alignUpIcon = Number(last);
			if(last){
				$("#"+hideDownIcon+"_downIcon").css("display", "none");
				$("#"+alignUpIcon+"_upIcon").css({"position": "relative","left":"-9px","padding":"0px 12px "});
			}
			$("#lineItemGrid").trigger("reloadGrid");
			$("#Ack").trigger("reloadGrid");
		}
		
		function openLineItemNoteDialog(){
			 CKEDITOR.replace('inlineItemId', ckEditorconfig);
			if($('#POtransactionStatus').val() == '-1'){
				 var newDialogDiv = jQuery(document.createElement('div'));
					errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Void' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
				}
			 else if($('#POtransactionStatus').val() == '0'){
				 var newDialogDiv = jQuery(document.createElement('div'));
				 errorText = "You can not Add/Edit Line Items Notes, \nTransaction Status is 'Hold' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
			 }else{
				var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
				if(rowId === null){
					var errorText = "Please select a line item to add Line Note";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				var lineItemText = $("#lineItemGrid").jqGrid('getCell', rowId, 'inLineNote');
			//	tinyMCE.get("inlineItemId").setContent(lineItemText);
				//var JopInlinetext =lineItemText.replace("&And", "'");
				//areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('inlineItemId');
				//$(".nicEdit-main").empty();
				//$(".nicEdit-main").append(lineItemText);
				
				CKEDITOR.instances['inlineItemId'].setData(lineItemText);
				if($('#POtransactionStatus').val()=='2'){
					$("#PoSaveInLineItemID").css("display", "none");
				}else{
					$("#PoSaveInLineItemID").css("display", "inline-block");
				}
				
				jQuery("#POInLineItem").dialog("open");
				//$(".nicEdit-main").focus();
				return true;
			 }
	}

	function saveLineItemNote(){
// 		var lineItemNote= $('#POInLineItemID').find('.nicEdit-main').html();
// 		var grid = $("#lineItemGrid");
// 		var rowId = grid.jqGrid('getGridParam', 'selrow');
// 		console.log("Line Item Note: "+lineItem);
// 		//alert('Selected Row Item: '+rowId);
// 		//var lineItem = $("#lineItemNote").val();
// 		$("#lineItemGrid").jqGrid('setCell', rowId,'notes', lineItem);
// 		$("#lineItemGrid").jqGrid('setCell', rowId,'inlineNoteImage',lineItem);
// 		var lineItem ='';
// 		lineItem = lineItemNote.replace(/"/g, "'");


		//var inlineText= $('#POInLineItemID').find('.nicEdit-main').html();
		//var inlineText= tinyMCE.get("inlineItemId").getContent();
		var inlineText=  CKEDITOR.instances["inlineItemId"].getData(); 
		var rowId = $("#lineItemGrid").jqGrid('getGridParam', 'selrow');
		var vePodetailId = $("#lineItemGrid").jqGrid('getCell', rowId, 'vePodetailId');
		console.log(vePodetailId);

		var image="<img src='./../resources/images/lineItem_new.png' style='vertical-align: middle;'>";
		  if(inlineText==null || inlineText==undefined || inlineText==""){
			  image="";
		  }
		  $("#lineItemGrid").jqGrid('setCell',rowId,'inLineNoteImage', image);
		  
		  $("#lineItemGrid").jqGrid('setCell',rowId,'inLineNote', inlineText);
		  
		  jQuery("#POInLineItem").dialog("close");
		  CKEDITOR.instances['inlineItemId'].destroy();
		//var aLineItem = new Array();
		//aLineItem.push(inlineText);
		//aLineItem.push(vePodetailId);
		/* $.ajax({
			url: "./jobtabs5/SavePOlinetextInfo",
			type: "POST",
			data : {'lineItem' : aLineItem},
			success: function(data){
				areaLine.removeInstance('inlineItemId');
				jQuery("#POInLineItem").dialog("close");
				$("#lineItemGrid").trigger("reloadGrid");
			}
		}); */
		
		/* element = "";
		var abillNote = $("#billNote").val();
		var grid = $("#release");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		var aJoReleaseId = grid.jqGrid('getCell', rowId, 'joReleaseId');
		var aJoMasterID = $("#joMaster_ID").text();
		var aJoReleasedDate = grid.jqGrid('getCell', rowId, 'released');
		var aJoReleaseType = grid.jqGrid('getCell', rowId, 'type');
		if(aJoReleaseType === 'Drop Ship')
			aJoReleaseType = 1;
		else if(aJoReleaseType === 'Stock Order')
			aJoReleaseType = 2;
		else if(aJoReleaseType === 'Bill Only')
			aJoReleaseType = 3;
		else if(aJoReleaseType === 'Commission')
			aJoReleaseType = 4;
		else if(aJoReleaseType === 'Service')
			aJoReleaseType = 5;
		var aReleaseNote = grid.jqGrid('getCell', rowId, 'note');
		var aEstimateAllocated = grid.jqGrid('getCell', rowId, 'estimatedBilling');
		var costAllocated = aEstimateAllocated.replace(/[^0-9\.]+/g,"");
		var aCostAllocated = Number(costAllocated);
		//http://localhost:8080/turbotracker/turbo/jobtabs5/billNote?&joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
		//joReleaseId=66153&billNote=This%20is%20Test&joMasterID=38191&joReleaseDate=03/03/2014%20&joReleaseType=Drop%20Ship&ReleaseNote=fans&EstimatedBilling=2688
		$.ajax({
			url: "./jobtabs5/billNote",
			type: "GET",
			data : "&joReleaseId="+aJoReleaseId+"&billNote=" +abillNote+"&joMasterID=" +aJoMasterID+"&joReleaseDate=" +aJoReleasedDate+
					"&joReleaseType=" +aJoReleaseType+"&ReleaseNote=" +aReleaseNote+"&EstimatedBilling=" +aCostAllocated,
			success: function(data) { 
		*/
		//$("#lineItemGrid").jqGrid('GridUnload');
// 		 var jsonList1='';
// 		try{
// 		///var selectedRow=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
// 		var rows = jQuery("#lineItemGrid").getDataIDs();
// 		var rowr=jQuery("#lineItemGrid").getRowData(rows[rowId-1]);
// 		var notes_test = rowr['inLineNote'];
// 		rowId = rowId.replace(/[^0-9\.]+/g,"");
// 		for(a=0;a<rows.length;a++)
// 		{
// 			var row=jQuery("#lineItemGrid").getRowData(rows[a]);
// 			notes_test=row['inLineNote'];
// 		if(a==(rowId-1)){
// 		notes_test = lineItem;
// 		}
		
// 		if(a == 0){
// 		jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
// 		}else{
// 		jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
// 		}
// 		console.log(jsonList1+a);
		
// 		}
// 		}
// 		catch(e){
// 			console.log('Error in load: '+e.message);
// 		}
// 		loadLinesItemGrid();
// 		areaLine.removeInstance('inlineItemId');
// 		jQuery("#POInLineItem").dialog("close");
// 		$("#lineItemGrid").trigger("reloadGrid");
		/* $("#release").trigger("reloadGrid");
				var errorText = "Bill Note Successfully Updated.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				/*jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");*/
			/* 	return false;
			}
		});  */
		
	}

	function cancelLineItemNote(){
		//areaLine.removeInstance('inlineItemId');
		jQuery("#POInLineItem").dialog("close");
		 CKEDITOR.instances['inlineItemId'].destroy();
		return false;
	}

	jQuery(function(){
		jQuery("#POInLineItem").dialog({
				autoOpen : false,
				modal : true,
				title:"InLine Note",
				height: 390,
				width: 635,
				buttons : {  },
				close:function(){
					return true;
				}	
		});

		$(function() { var cache = {}; var lastXhr='';

		$("#vendorsearch").autocomplete({ minLength:1,timeout :1000,
			change: function (event, ui) { },
			 open: function(){ 
				 var len = $('.ui-autocomplete > li').length;
				// var value=$(this).val();
				 var value=$('.ui-autocomplete > li').text();
				 if(len==1 && value==" "){
					 $("#vendorsearch").val("");
					 }

				   
			},  
			 response: function( event, ui ) {
		        // alert("iniresponse"+ui.content.length); 
		     },
			select: function (event, ui) {
				var aValue = ui.item.value;
				var valuesArray = new Array();
				valuesArray = aValue.split("|");
				var id = valuesArray[0];
				var code = valuesArray[2];
				$("#vendorsearchRXMasterID").val(aValue);
				$('#vendorID').val(aValue);
				$("#manfactureId").val(aValue);	
				var sManu = '';
				var sAttn = '<option value="-1"> - Select - </option>';
				 $.ajax({
				        url: './getManufactureATTN?rxMasterId='+aValue,
				        type: 'POST',       
				        success: function (data) {
				        	$.each(data, function(key, valueMap) {					
								
							
								if("manufacturerNames"==key)
								{
									$.each(valueMap, function(index, value){
										sManu+='<option value='+value.rxMasterId+'>'+value.description+'</option>';
									
									}); 
									
								}
								
								if("attnNames"==key)
								{
									$.each(valueMap, function(index, value){
										sAttn+='<option value='+value.rxContactId+'>'+value.firstName+' '+value.lastName+'</option>';
									
									}); 
									
								}
								if("vendorAddress"==key)
								{
									$.each(valueMap, function(index, value){
										console.log(valueMap.length);
										VendorAddresses.push({name: value.name,address1:value.address1 ,address2:value.address2,city: value.city, state:value.state, zip: value.zip});
										if(value.isDefault){
										$('#vendorAddress').html(value.address1);
										$('#vendorAddress1').html(value.city).append(value.state).append(value.zip);
									}
									});
								
										if(VendorAddresses.length>=2){
											$('#addressToggle').css('display','block');
											var imgUrlDisBackward = "./../resources/images/DisabledArrowleft.png";
											$('#vendorBackwardId').css('background', 'url('+imgUrlDisBackward+') no-repeat');
											$('#vendorBackwardId').css('background-position','center');
										}
								}

								if("vendorName"==key)
								{
									$.each(valueMap, function(index, value){
										$('#vendorsearch').val(value.name);
									
										
										
									}); 
									
								}	
							});
				        	$('#manfactureName').html(sManu);
							$('#contacthiddenID').html(sAttn);
							
				        }
				    }); 
				
				//location.href="./inventoryDetails?token=view&inventoryId="+id + "&itemCode=" + code;
			},
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchVendor", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		} });
		});


		$("#vendorsearch").keyup(function(e){
			 if(e.keyCode == 8 || e.keyCode == 46)
				 $('#vendorAddress').empty();
				 $('#vendorAddress1').empty();
				 VendorAddresses=[];
				 $('#addressToggle').css('display','none');
		});

			function vendorAddressBackward(){
				if(parseInt(incValue)>=VendorAddresses.length){
					incValue = (VendorAddresses.length)-2
				}
				if(VendorAddresses.length >0)
				{
						console.log('incValue Bac: '+incValue);
						//incValue=VendorAddresses.length-1;
						//for(var i = (VendorAddresses.length-1); i >= 0; i--)
						//{
							var imgUrlforward = "./../resources/images/Arrowright.png";
							$('#vendorForwardId').css('background', 'url('+imgUrlforward+') no-repeat');
						$('#vendorForwardId').css('background-position','center');
						console.log(VendorAddresses.length+" Address1: "+VendorAddresses[incValue].address1);
						$('#vendorAddress').empty().append(VendorAddresses[incValue].address1);
						$('#vendorAddress1').empty().append(VendorAddresses[incValue].city+''+VendorAddresses[incValue].state+''+VendorAddresses[incValue].zip);
						incValue=parseInt(incValue)-1;
						// break;
						//}
					if(parseInt(incValue)<0){
						console.log('Over Backward');
						var imgUrlDisBackward = "./../resources/images/DisabledArrowleft.png";
						$('#vendorBackwardId').css('background', 'url('+imgUrlDisBackward+') no-repeat');
						$('#vendorBackwardId').css('background-position','center');
						
					}
				}
			
		 }

			function vendorAddressForward(){
				if(parseInt(incValue)<=0){
					incValue = 1;
				}
				if(VendorAddresses.length>0)
				{
					console.log('incValue For: '+incValue);
					//for(var i =0; i<=parseInt(incValue); i++)
						//{
						var imgUrlBackward = "./../resources/images/Arrowleft.png";
						$('#vendorBackwardId').css('background', 'url('+imgUrlBackward+') no-repeat');
						$('#vendorBackwardId').css('background-position','center');
					if(parseInt(incValue)>=VendorAddresses.length-1){
						console.log('Max Value - Z: '+incValue);
						var imgUrlDisForward = "./../resources/images/DisabledArrowright.png";
						$('#vendorForwardId').css('background', 'url('+imgUrlDisForward+') no-repeat');
						$('#vendorForwardId').css('background-position','center');			
						
					}
						console.log(VendorAddresses.length+" Address1: "+VendorAddresses[incValue].address1);
						$('#vendorAddress').empty().append(VendorAddresses[incValue].address1);
						$('#vendorAddress1').empty().append(VendorAddresses[incValue].city+''+VendorAddresses[incValue].state+''+VendorAddresses[incValue].zip);
						incValue=parseInt(incValue)+1;
						//break;
						//}			
				}
			}


		
	});
		
	function outsidepoEmailButtonAction(){
		
		 
		var vePoId = $("#vePOID").val();
		var aContactID = $("#contacthiddenID").val();
		//alert(aContactID);
		var errorText = '';
		
		if($('#POtransactionStatus').val() == '-1'){
			errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
		}
		else if($('#POtransactionStatus').val() == '0'){
				 errorText = "You can not Send E-Mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() {
												$(this).dialog("close");
												$("#cData").tigger('click');}}]}).dialog("open");
				return false;
		}
		 else if($('#POtransactionStatus').val() == '2'){
			 errorText = "You can not Send E-Mail, \nTransaction Status is 'Close' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
		 }
		else{
		var cusotmerPONumber = $("#ourPoId").val();
		if(vePoId === null || vePoId.length<= 0){
			errorText = "Please Save Purchase Order to Email Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		if(aContactID === null && aContactID === '' && aContactID === '-1'){
			errorText = "Please Add Contact for Purchase Order.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}else{

			purchaseorderpdfwrite();
			
			}
	} 
	}

	function purchaseorderpdfwrite(){
		try{
			 var vePOID = $("#vePOID").val();
				if(vePOID === null || vePOID.length<= 0){
					errorText = "Please Save Purchase Order to View PDF.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToId}";
				var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
				var joReleaseId = "";//"${theVepo.joReleaseId}";
				var jobNumber = "";
				var rxCustomerId = "";
				var joMasterID=0;
			$.ajax({
				type : "GET",
				url : "./purchasePDFController/viewPDFLineItemForm",
				data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : '', 'jobNumber' :  jobNumber, 'rxMasterID' : rxCustomerId, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :'0' ,'WriteorView':'write'},
				documenttype: "application\pdf",
				async: false,
				cache: false,
				success : function (msg) {
					setemailpopupdetails();
					
				},
				error : function (msg) {}
			});
		 }catch(err){
			 alert(err.message);
		 }
		
		
	}
	function setemailpopupdetails(){
		clearemailattachmentForm();
		var cusotmerPONumber = $("#ourPoId").val();
		var vePoId = $("#vePOID").val();
		var aContactID = $("#contacthiddenID").val();
		var toemailaddress="";
		var fullname="";
		$.ajax({ 
			url: "./vendorscontroller/GetContactDetails",
			mType: "GET",
			async:false,
			data : { 'rxContactID' : aContactID},
			success: function(data){
				var aFirstname = data.firstName;
				var aLastname = data.lastName;
				toemailaddress = data.email;
				$("#etoaddr").val(toemailaddress);
			    fullname = aFirstname + ' '+aLastname;
			   // $('#loadingPODiv').css({"visibility": "visible"});
				
				
				
				
			}
		});
		$.ajax({ 
			url: "./vendorscontroller/GetFromAddressContactDetails",
			mType: "GET",
			async:false,
			data : { },
			success: function(data){
					$("#efromaddr").val(data.emailAddr);
					var ccaddr1=data.ccaddr1;
					var ccaddr2=data.ccaddr2;
					var ccaddr3=data.ccaddr3;
					var ccaddr4=data.ccaddr4;
			var ccaddress="";
			if(ccaddr1!=null && ccaddr1!=""){
				if(ccaddress==""){
					 ccaddress=ccaddr1;
				}else{
					 ccaddress=ccaddress+","+ccaddr1;
				}
			}
			if(ccaddr2!=null && ccaddr2!=""){
				if(ccaddress==""){
					 ccaddress=ccaddr2;
				}else{
					 ccaddress=ccaddress+","+ccaddr2;
				}
			}
			if(ccaddr3!=null && ccaddr3!=""){
				if(ccaddress==""){
					 ccaddress=ccaddr3;
				}else{
					 ccaddress=ccaddress+","+ccaddr3;
				}
			}
			if(ccaddr4!=null && ccaddr4!=""){
				if(ccaddress==""){
					 ccaddress=ccaddr4;
				}else{
					 ccaddress=ccaddress+","+ccaddr4;
				}
			}
			$("#eccaddr").val(ccaddress);
			$("#esubj").val("Purchase Order # "+cusotmerPONumber);
			$("#filelabelname").text("PurchaseOrder_"+cusotmerPONumber+".pdf");
			
			$("#emailpopup" ).dialog("open");
			}
		});
	}

	function sendsubmitMailFunction(){
		var vePoId = $("#vePOID").val();
		var cusotmerPONumber = $("#ourPoId").val();
		var aContactID = $("#contacthiddenID").val();
		var toemailAddress=$("#etoaddr").val();
		var ccaddress=$("#eccaddr").val();
		var subject=$("#esubj").val();
		var filename="PurchaseOrder.pdf";
		var content=$("#econt").val();
		//content=$('#econt').find('.nicEdit-main').html();
		content=nicEditors.findEditor('econt').getContent();
		$.ajax({ 
			url: "./sendMailServer/sendOutsitePurchaseOrderMail",
			mType: "POST",
			data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
					'toAddress':toemailAddress, 'subject':subject,
					'filename':filename,'ccaddress':ccaddress,
					'content':content
				},
			success: function(data){
				//$('#loadingPOGenDiv').css({"visibility": "hidden"});
				//$('#loadingPODiv').css({"visibility": "hidden"});
				var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
				if(data){
					errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
					}
				jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
					buttons: [{height:35,text: "OK",click: function() {
						var today = new Date();
						var dd = today.getDate();
						var mm = today.getMonth()+1; 
						var yyyy = today.getFullYear().toString().substr(2,2);
						var hours = today.getHours();
						var minutes = today.getMinutes();
						var ampm = hours >= 12 ? 'PM' : 'AM';
						hours = hours % 12;
						hours = hours ? hours : 12;
						if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
						if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
						$.ajax({ 
							url: "./jobtabs3/updateEmailStampValue",
							mType: "GET",
							data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
							success: function(data){ 
								$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
								$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); 
							},
							error:function(e){
									 $('#loadingDivForPO').css({
											"visibility" : "hidden","display": "none"
										}); 
							}
						});
						$('#loadingDivForPO').css({
							"visibility" : "hidden","display": "none"
						}); 
						$(this).dialog("close"); 
						$("#emailpopup" ).dialog("close");
					}}] }).dialog("open");
			},error:function(e){
				 $('#loadingDivForPO').css({
						"visibility" : "hidden","display": "none"
					}); 
				}
		});	
		
	}
	$(function(){
		$("#emailpopup" ).dialog({
			autoOpen: false,
			height: 570,
			width: 600,
			modal: true,
			//buttons: {
						
				/* Send:function(){  
					sendsubmitMailFunction();
		   		 },	
			
				Cancel: function() {
					$( this ).dialog( "close" );
				} */
			//},	
		});

		});

	/* function ReOrderButtonClick(){
		var WareHouseId=$("#prWarehouseID").val();
		var VendorID=$("#vendorID").val();
		var vePOID=$("#vePOID").val();
		$.ajax({ 
			url: "./vendorscontroller/ReorderInsert",
			mType: "GET",
			data : {'WareHouseId' : WareHouseId, 'VendorID' : VendorID,
					'vePOID':vePOID
				},
			success: function(data){
				jQuery("#lineItemGrid").trigger("reloadGrid");
			}
		});
		} */

	function submitemailattachment(){
		$('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		sendsubmitMailFunction();
	}
	// function ReOrderButtonClick(){
	//	var WareHouseId=$("#prWarehouseID").val();
	//	var VendorID=$("#vendorID").val();
	//	var vePOID=$("#vePOID").val();

	//	var gridRows = $('#lineItemGrid').getRowData();
	//	var dataToSend = JSON.stringify(gridRows);
	//	jQuery("#lineItemGrid").jqGrid('setGridParam',{url:"./vendorscontroller/PurchaseOrderlineitemsbasedonreorder?WareHouseId="+WareHouseId+"&VendorID="+VendorID+"&vePOID="+vePOID,postData: {'lineitemlist':dataToSend}}).trigger("reloadGrid");

	//} 

	

function validatePoCommissionTotals(id){
	console.log(id);
	var rows = jQuery("#EditPoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
		    row=jQuery("#EditPoSplitCommissionGrid").getRowData(rows[a]);
		    total=row['allocated'];
		    total = Number(total);
		    if(isNaN(total)){
		    	total=Number(1);
		    }
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
		 console.log(grandTotal);
		 if(parseInt(grandTotal)<=100){
			return true;
		 }else{ 
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should below 100'+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
					// jQuery("#PoSplitCommissionGrid").jqGrid('restoreRow',id);
					jQuery("#EditPoSplitCommissionGrid").jqGrid('setSelection',id, true);
					 $('#EditPoSplitCommissionGrid_iledit').trigger('click');
					}}]}).dialog("open");
				return false;
		 }
}
//PoSplitCommissionGrid_ilcancel
function validatePOSplitCommissionTotals(){
	var rows = jQuery("#EditPoSplitCommissionGrid").getDataIDs();
	var total = 0;
	var grandTotal = 0;
	var row = '';
		 for(a=0;a<rows.length;a++)
		 {
		    row=jQuery("#EditPoSplitCommissionGrid").getRowData(rows[a]);
		    total=row['allocated'];
		   // total+=parseInt(total);
		    grandTotal = Number(grandTotal) + Number(total); 
		 }
	var splitCommGridDatas = $('#EditPoSplitCommissionGrid').getRowData();
	var splitCommGridDataLocal = JSON.stringify(splitCommGridDatas);
		 
	if(parseInt(grandTotal)<100 && rows.length!=0){
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDilg");
				jQuery(newDialogDiv).html('<span><b style="color:Green;">Sum of allocated should be 100 </b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");
				return false;
		 }else{
			 return true;
		 }
}

/*	else if(splitCommissionGridGlobalVar == splitCommGridDataLocal){
	var newDialogDiv = jQuery(document.createElement('div'));
	jQuery(newDialogDiv).attr("id","msgDilg");
	jQuery(newDialogDiv).html('<span><b style="color:Green;">Split CommissionSum of allocated should be 100 </b></span>');
	jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information",
		buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close");  }}]}).dialog("open");*/

function canDeleteCheckboxFormatter(cellValue, options, rowObject){
	var id="canDeleteID_"+options.rowId;
	var element = "<input type='checkbox' id='"+id+"' onclick='deleteCheckboxChanges(this.id)'>";
	return element;
}

function deleteCheckboxChanges(id){
	id="#"+id;
	console.log("deleteCheckboxChanges::"+id);
    var canDo=$(id).is(':checked');
    if(canDo){
    	$(id).val("true");
    }else{
    	$(id).val("false");
    }
}
</script>
   <!--  <script type="text/javascript" src="./../../resources/scripts/turbo_scripts/poLineNoteNicEditor.js"></script> -->
</body>
<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
</html>
