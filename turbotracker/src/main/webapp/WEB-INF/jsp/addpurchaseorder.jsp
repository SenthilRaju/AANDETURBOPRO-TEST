<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro- Purchase Order</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
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
     
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	
	<div align="center" style="padding-top: 10px;">
		<label style="font-family: Verdana,Arial,sans-serif;"><b>STOCK PURCHASE ORDER</b></label>
	</div> 
	<div class="tabs_main" id="po_tabs" style="padding-left: 0px;width:960px;margin:0 auto; background-color: #FAFAFA;height: auto;margin-bottom: 40px;right:8px; box-shadow: 1px 6px 5px 5px #AAAAAA;">
			<ul>
				<li><a id="generalTab" href="#general" onclick="calculateSubTot();">General</a></li>
				<li><a id="lineTab" href="#line" onclick="loadDates();">Line Items</a></li>
				<!-- <li><a id="ackTab" href="#ack" onclick="loadACK();">Acknowledgement</a></li> -->
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Consign"  style="width:90px;display: none;"></li>
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Reorder"  style="width:90px;" onclick="ReOrderButtonClick()"></li>
				<li>&nbsp;&nbsp;&nbsp;&nbsp;</li>				
				<li><li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Cust Ack"  style="width:90px;"></li>
				<%--<li><input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Open PO"  style="width:90px;"></li> --%>
				<li id="salesacknowledgement" style="position: absolute;right: 1%;top: 1%; background:white;"><input type="button" id="PoStatusButton" value="Open" style="width: 90px;height: 24px;" /></li>
			</ul>
			<!-- General Tab Starts -->
			<div id="general">
				<form id="POGeneralForm" method="post">
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
									<legend class="custom_legend"><b>Vendor<label style="color: red;">*</label></b></legend>
								<table style="width: 400px">
									<tr>	
									<td><input type="text" style="border-width: 3px;" id="vendorsearch" placeholder="Minimum 1 characters required to get Vendor list"></td>									
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
									<input type="button" id="vendorBackwardId" value="" onclick="vendorAddressBackward()" style="width:20px; background: url('./../resources/images/Arrowleft.png') no-repeat;background-position: center; cursor:pointer;">
									<input type="button" id="vendorForwardId" value="" onclick="vendorAddressForward()" style="width:20px; background: url('./../resources/images/Arrowright.png') no-repeat;background-position: center; cursor:pointer;">
									</div>
										<input type="hidden" style="border-width: 3px;" id="vendorsearchRXMasterID" disabled="disabled"></td>
										<td><img alt="search" src="../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
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
										<select  style="width: 225px" id="manfactureName" name="manufacture" onchange="loadOtherTabs();">
												<option value="-1"> - Select - </option>
												<c:forEach var="VeFactory" items="${manufacturerNames}">
													<option value="${VeFactory.rxMasterId}"><c:out value="${VeFactory.description}" ></c:out></option>
												</c:forEach>
											</select>
										<!-- <input type="text" style="width: 225px" id="manfactureName" name="manufacture" value="" disabled="disabled"> -->
										</td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="display: none;"></td>
									</tr>
									<tr>
										<td><input type="text" style="width: 225px;display: none;" id="joReleaseid" name="jorelease" value="<c:out value='${JoReleaseId}' ></c:out>"> </td>
										<td><input type="text" style="width: 225px;display: none;" id="manfactureId" name="manufactName"></td>
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
										<td><input type="text" class="datepicker" id="poDateId" name="poDateName" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentDate}" />" style="width: 140px;"></td>
									</tr>
									<tr align="left" style="display: none;">
										<td><label>Customer PO#:</label></td>
										<td><input type="text" id="customerNAMEId" name="customerName" style="width: 140px;" value="<c:out value='${POnumber}' ></c:out>">
										</td>													
									</tr>
									<tr align="left">
										<td><label>Our PO#:</label></td>
										<td><input type="text" style="width: 140px" id="ourPoId" name="ourPoName" value="<c:out value='${OurPO}' ></c:out>" disabled="disabled"></td>
									</tr>
									
									<tr align="left" id="qbtr">
									<td style="width: 40px;"><label>Wanted:</label></td>
										<td><input type="text" style="width: 140px" id="wantedId" name="wantedName" readonly="readonly" value=""></td>										
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
						<td align="left" >
							<div>
								<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 340px;">
									<legend class="custom_legend"><b>Tag</b></legend>
								<table style="width: 400px">
									<tr>										
										<td>
											<input type="text" style="width: 210px" id="tagId" value="<c:out value='${JobName}' ></c:out>">										
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
										<td><select style="width:120px" id="orderId" name="orderName" >
												<option value="-1"> - Select - </option>
												<c:forEach var="TsUserLogin" items="${orderedByNames}">
												<c:set value="${TsUserLogin.fullName}" var="name" />
												
													<c:if test="${sessionScope.user.fullName eq name}">
														<option selected="selected" value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
													<c:if test="${sessionScope.user.fullName ne name}">
														<option value="${TsUserLogin.userLoginId}">${TsUserLogin.fullName}</option>
													</c:if>
												</c:forEach>
											</select>
											</td>
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
										<td><textarea rows="3" cols="39" id="specialInstructionID" name="specialInstructionName"></textarea></td>
								</table>
							</fieldset>
							</div>
						</td>
					</tr>
				</table>	
			</td>
		</tr>
		<%-- <input type="hidden" name="" value="${theVepo.transactionStatus}" id="POtransactionStatus"/> --%>
		<input type="hidden" name="transactionStatus" value="1" id="POtransactionStatus"/>
		
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
					 					<tr align="left">
											<td  id="billtoRadios" style="vertical-align: bottom;padding-left: 23px;">
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
			<fieldset class= "ui-widget-content ui-corner-all" style="height: 50px;width: 850px">
				<legend class="custom_legend"><label><b>Total</b></label></legend>
					<table style="width: 750px">
						<tr>
							<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 100px; text-align:right" id="subtotalGeneralId" name="" value="0.00" disabled="disabled"></td>
							<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat()" onfocus="frieghtCost()" onkeypress="return isNumberKey(this);" value="0.00"></td>
							<td><label>Tax: </label></td><td><fmt:setLocale value="en_US"/><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" onclick="taxCost()" onchange="taxFormat()" onfocus="taxCost()" value="0.00"></td>
							<td><label style="right: 0px;position: relative;">% &nbsp;</label></td><td><input type="text" id="generalID" name="generalName" style="width: 100px;text-align:right;" disabled="disabled" value="0.00"></td>
							<td><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalGeneralId" name="totalGeneralName" disabled="disabled" value="0.00"></td>	
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="vePOID" name="vePOName" disabled="disabled" value=""></td>
							<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" disabled="disabled" value=""></td>
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
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="outsidepoEmailButtonAction();"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStamp" style="color: green;"></label></td>
		<td style="display: none;">
			<input type="button" id="viewGeneralPdfId" class="cancelhoverbutton turbo-tan" value="Confirm" onclick="viewAsPdfGeneral()" style="width:80px;">
		</td>
		<td align="right" style="padding-left: 165px;">
			<div id="showMessage" style="color: green;"></div>
			<div id="msgDlg"><span><b style="color:Green;"></b></span></div>
			<div  id="generalErrorDiv" style="color: red;"></div >
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
		    <div id="lineItemsID" >
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
									<td><input type="text" class="datepicker" id="poDateLineId" name="poDateLineName" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${currentDate}" />" style="width: 140px;" disabled="disabled"  ></td>
									<td>&nbsp;&nbsp;</td>
									<td><label>Our PO #:</label></td>
									<td><input type="text" style="width: 150px" id="ourPoLineId" name="ourPoLineName" value="${aJobPurchaseOrderBean.ponumber}" disabled="disabled" ></td>
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
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightLineId" value="" name="freightLineName" onclick="frieghtCostLine()" onchange="frieghtFormatLine()" onfocus="frieghtCostLine()" onkeypress="return isNumberKeyLine(this);" value="0.00"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxLineId" name="taxLineName" onclick="taxCostLine()" onchange="taxFormatLine()" onfocus="taxCostLine()" value="0.00"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="lineID" name="lineName" style="width: 60px;text-align:right;" disabled="disabled" value="0.00"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalLineId" name="totalLineName" disabled="disabled" value="0.00"></td>
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
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOPDF()"  style="background: #EEDEBC;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="outsidepoEmailButtonAction()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
	  	<td style="padding-left: 20px;font-size: 15px;vertical-align: middle;width: 280px;"><label id="emailTimeStamp" style="color: green;"></label></td>
		<td style="display: none;">
			<input type="button" id="viewGeneralPdfId" class="cancelhoverbutton turbo-tan" value="Confirm" onclick="viewAsPdfGeneral()" style="width:80px;">
		</td>
		<td align="right" width="80px">
		<div id="ShowInfo" style="width: 380px;height:20px;color: green;"></div>
		
		
		<input id="Buttonchoosed" type="checkbox" style="display: none;"/>
		<input type="button" id="POSaveID" class="cancelhoverbutton turbo-tan" value="Save" onclick="SaveLinesPurchaseOrder()" style="width:90px;position: relative; left: -20px;">
		<input type="button" id="POSaveCloseID" class="cancelhoverbutton turbo-tan" value="Save & Close" onclick="closePurchaseOrderLineItemTab()" style="width:110px;position: relative; left: -20px;">
		<input type="checkbox" id="ButtonClicked" style="display: none;"/>
		<input type="text" id="setButtonValue" style="display: none;" value=""/>
		<!-- <input type="button" class="savehoverbutton turbo-tan IsButtonClicked" value="Save" onclick="savePOLineItems()" style="width:90px;" id="cancelpoRelease"> -->
		</td>
	</tr>
</table>
</div>
			</div>
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
<div id= "POInLineItem">
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
			
		
		 <jsp:include page="./Email_Attachment.jsp"></jsp:include>
		 
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/addPurchaseOrder.js"></script>
	<script type="text/javascript">
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
	var billToOtherName,billtoOtherAddress1,billtoOtherAddress2,billToOtherCity,billToOtherState,billToOtherZip,shipToOtherName,shiptoOtherAddress1,shiptoOtherAddress2,shipToOtherCity,shipToOtherState,shipToOtherZip;
	 var onChangeOther = 1;
	 var onChangeShipToOther = 1;
	var taxRateShipTo = 0;
	var taxRateOther = 0;
	// GlobalPage_Validation=1;
	$(function() {
		
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

	 

jQuery(document).ready(function() {
		
	    $( "#PurchaseOrderDiv ul li:nth-child(2)" ).removeClass("ui-state-disabled");
		if($('#shiptoradio2').is(':disabled')) { 
			 $('#shiptolabel2').attr('onclick',null);
		    }
		 if($('#shiptoradio4').is(':disabled')) { 
			 $('#shiptolabel4').attr('onclick',null);
		    }
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
		
		aPOkey = "POKey";
		$('#POFlag').val("POKey");
		$('#msgDlg').hide();
		aVePoId = "${aVePoID}";
		$("#billtoRadioSet").buttonset();
		$("#shipToRadioSet").buttonset();
		$(".datepicker").datepicker();
		loadBillAddress();
		loadShipAddress();
		populateFreightCharges();
		loadLineItemGrid();		
		//getTaxRate();
		$("#AcknowledgeAllDate").tabs();
		/* $("#ackDate").datepicker({
	        showOn: 'button',
	        buttonText: 'Show Date',
	        buttonImageOnly: true,
	        buttonImage: './../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif',
	        dateFormat: 'dd/mm/yy',
	        constrainInput: true
	    });
	    $("#shipDate").datepicker({
	        showOn: 'button',
	        buttonText: 'Show Date',
	        buttonImageOnly: true,
	        buttonImage: './../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif',
	        dateFormat: 'dd/mm/yy',
	        constrainInput: true
	    }); */

	    $(".ui-datepicker-trigger").mouseover(function() {
	        $(this).css('cursor', 'pointer');
	    });
		var size = "${fn:length(wareHouse)}";
		if(size > 0)
		{
			if(size == 1)
			{
				$('#forWardId').prop('hidden',true);
				$('#backWardId').prop('hidden',true);
			}
		}
	});

function populateFreightCharges()
{
	var sFrt = '<option value="-1"> - Select - </option>';
	var sShipvia = '<option value="-1"> - Select - </option>';
	var sBuddies = '<option value="-1"> - Select - </option>';
	$.ajax({
		url: "./freightCharges",
		type: "GET",
		success: function(data) {
			$.each(data, function(key, valueMap) {
				
				if("frtCharges"==key)
				{
					$.each(valueMap, function(index, value){
						sFrt+='<option value='+value.veFreightChargesId+'>'+value.description+'</option>';
					
					}); 
					
				}
				if("shipvia"==key)
				{
					$.each(valueMap, function(index, value){
						sShipvia+='<option value='+value.veShipViaId+'>'+value.description+'</option>';
					
					}); 
					
				}

				if("buddiestList" == key)
				{
					$.each(valueMap, function(index, value){
						sBuddies+='<option value='+value.rxContactId+'>'+value.firstName+' '+value.lastName+'</option>';
					
					});
				}
			});
			/* $.each(data, function(index, value){
				var coAccountID = value.veFreightChargesId;
				alert("id--"+coAccountID);
				sHtml+='<option value='+value.veFreightChargesId+'>'+value.description+'</option>';
			
			}); */
			$('#frieghtChangesId').html(sFrt);
			$('#shipViaId').html(sShipvia);
			//$('#contacthiddenID').html(sBuddies);
			
			
		}
	});

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

function inlineNoteImage(cellValue, options, rowObject){
	console.log('Cell Value: '+cellValue);
	var element = '';
	if(cellValue !== '' && cellValue !== null && cellValue != undefined){
		element = "<img src='./../resources/images/lineItem_new.png' title='Line Items' style='vertical-align: middle;'>";
	}
	return element;
}
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
var newDialogDiv = jQuery(document.createElement('div'));
function addPurchaseOrder()
{
	calculateTotal();
   
    $('#POFlag').val("");
    "use strict";

    $("#ButtonClicked").prop('checked', true);
     var newDialogDiv = jQuery(document.createElement('div'));
     if(!$('#POGeneralForm').validationEngine('validate')) {
        return false;
    }
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
     var tagVal =     $("#tagId").val();
     var emailTimeStamp = $("#emailTimeStamp").text();
     var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
     var asubTotal = $("#subtotalGeneralId").val().replace(/[^0-9\.]+/g,"");
     var aTaxPerc = $("#taxGeneralId").val().replace(/[^0-9\.]+/g,"");
     var aTaxTotal = $("#generalID").val().replace(/[^0-9\.]+/g,"");
     var transactionStatus = $('#POtransactionStatus').val();
     
     var attnName = $("#contacthiddenID").val();
     /** *********  IT IS IMPORTANT CODE  ************
      var aRequestObj = {
             JOB_NUMBER: "",
             Manu:"",
             asdfdas:""
     };
     aRequestObj = JSON.stringify(aRequestObj);
     ************************************************** */

         console.log("Form Values---"+aGenerelFormValues);
   
        console.log("All values--->"+aPOGenerelValues);

        $("#lineItemGrid").jqGrid('resetSelection');        
      
        var gridRows = $("#lineItemGrid").jqGrid('getRowData');
        var rowData = new Array();
        var jsonList1;
        var total = 0;
        for (var i = 0; i < gridRows.length; i++) {
            var row = gridRows[i];
             //var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
            total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
           
      		// var desc = row.description.toString().replace('"', '\\"');
      		
      		/* var desc = row.description.toString().replace(/"/g, '\\"');

            desc = escape(desc); */


         //   var decsrip = row.description.replace(/\"/g, '\"');
            /* var notesReal = row.notes.replace(/"/g, "'");
            var notesDescReal =row.notesdesc.replace(/"/g, "'");
            if(i == 0)
                jsonList1="{note:\""+row.note+"\",description:\""+desc+"\",notes:\""+notesReal+"\",notesdesc:\""+notesDescReal+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";
            else
                jsonList1 = jsonList1+','+"{note:\""+row.note+"\",description:\""+desc+"\",notes:\""+notesReal+"\",notesdesc:\""+notesDescReal+"\",quantityOrdered:\""+row.quantityOrdered+"\",unitCost:\""+row.unitCost+"\",priceMultiplier:\""+row.priceMultiplier+"\",taxable:\""+row.taxable+"\",quantityBilled:\""+row.quantityBilled+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\",prMasterId:\""+row.prMasterId+"\"}";
 */
            /* if(i == 0)
                jsonList1="{note:"+row.note+",description:"+desc+",notesdesc:"+row.notesdesc+",quantityOrdered:"+row.quantityOrdered+",unitCost:"+row.unitCost+",priceMultiplier:"+row.priceMultiplier+",taxable:"+row.taxable+",quantityBilled:"+row.quantityBilled+",ackDate:"+row.ackDate+",shipDate:"+row.shipDate+",vendorOrderNumber:"+row.vendorOrderNumber+",prMasterId:"+row.prMasterId+"}"; */
        }
       var mydata;
       /* try{
           mydata = eval ("[" + jsonList1 + "]");
           }
       catch(e)
       {
           mydata = "[" +jsonList1 + "]";
           //alert(e.name + "\n" + e.message)
           } */
        //var mydata = eval ("[" + jsonList1 + "]");   
        var dataToSend = JSON.stringify(gridRows);
        divflag = "#PO_Shiptooutside";
       

        var aPOGenerelValues = aGenerelFormValues+"&subtotalGeneralName="+asubTotal+"&freightGeneralName="+afreightTotal+"&TaxTotal="+aTaxTotal
        +"&customerBillToOtherID="+$('#customerBillToOtherID').val()+"&locationbillToAddressID="+$('#billToAddressID').val()+"&locationbillToAddress1="+$('#billToAddressID1').val()+"&locationbillToAddress2="+$('#billToAddressID2').val()+"&locationbillToCity="+$('#billToCity').val()+"&locationbillToState="+$('#billToState').val()+"&locationbillToZip="+$('#billToZipID').val()
        +"&buttonId="+id+"&vePOID="+$("#vePOID").val()+"&vePOID="+$("#vendorsearchRXMasterID").val()+"&taxPerc="+aTaxPerc+"&customerBillToAddressID="+$("#customerBillToAddressID").val()+"&ourpo="+$("#ourPoId").val()+"&transactionStatus="+transactionStatus+
        "&rxShiptoid="+ $(divflag).contents().find("#shiptoaddrhiddenfromuiid").val()+"&rxShiptomodevalue="+ $(divflag).contents().find("#shiptomoderhiddenid").val();
       

        console.log("All values--->"+aPOGenerelValues);

        if($("#vendorsearch").val()=="" || $("#vendorsearch").val()==undefined ||$("#vendorsearch").val()==null){
            $("#generalErrorDiv").empty();
            $("#generalErrorDiv").append("*Vendor field is required");
        }else{
            $("#generalErrorDiv").empty();
            $.ajax({
    	        url: './getUserDefaults',
    	        type: 'POST',       
    	        success: function (datas) {
    	        	//alert('WareHouseID:'+data.warehouseID);
    	        	if(datas.warehouseID > 0){
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

                	createtpusage('Company-Vendors-Purchase Order','Add New PO','Info','Company-Vendors-Purchase Order,New PO,PO ID:'+data.vePoid);

                    if("Save" == $('#setButtonValue').val())
                    {
                       
                        var aVepoID = data.vePoid;
                        console.log("Data--->"+data);

                        console.log("Data Taxtotal--->"+data.taxTotal);

                        
                        if(aVepoID != null || aVepoID !='')
                        {
                            $("#vePO_ID").text(aVepoID);
                            $("#vePOID").val(aVepoID);
                            document.location.href = "./editpurchaseorder?token=view&aVePOId="+aVepoID;
                            
                            
                            $("#ourPoId").val(data.ponumber);
                            $("#rxShipToId").val(data.rxShipToId);
                            $("#customerShipToID").val(data.rxShipToId);
                            $("#customerBillToID").val(data.rxBillToId);
                            $("#customerBillToAddressID").val(data.rxBillToAddressId);
                            $("#customerShipToAddressID").val(data.rxShipToAddressId);
                            $("#rxVendorContactId").val(data.rxVendorContactId);
                            if($('#customerShipToOtherID').val() == 'Other')
                            {
                            }

                            if($('#customerBillToOtherID').val() !== 'Us')
						    {
                            $("#lineID").val(data.taxTotal.toFixed(2));
                            $('#totalLineId').val( (parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat(data.taxTotal)).toFixed(2));
                            
						    }
                           
                            if($('#customerBillToOtherID').val() == 'Other')
                            {
                                billToOtherName = $('#billToAddressID').val();
                                billtoOtherAddress1 = $('#billToAddressID1').val();
                                billtoOtherAddress2 = $('#billToAddressID2').val();
                                billToOtherCity = $('#billToCity').val();
                                billToOtherState = $('#billToState').val();
                                billToOtherZip = $('#billToZipID').val();
                                 onChangeOther = 2;
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
                                 onChangeShipToOther = 2;
                            }   
                            var errorText = "";
                            $('#msgDlg').show();
                           
                            $('#msgDlg').prop('display','block');
                            jQuery(newDialogDiv).attr("id","msgDlg");
                            jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
                             if($('#ButtonClicked').is(':checked')){
                                $('#showMessage').html("Saved");
                                $('#ShowInfo').html("Saved");
                               
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
                            //alert("Purchase Order Stored Successfully");
                        }
                        else
                        {
                            var msg = "Purchase Order Failed.";
                           
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
                    if("SaveandClose" == $('#setButtonValue').val())
                    {
                        document.location.href = "./po_list";
                    }
                   
                }
         });
    	        	}else{
    	        		var errorDialogText = '';
    	        		var wareHouseDialogDiv = jQuery(document.createElement('div'));
    	        		errorDialogText = "Please set Default 'Warehouse' and 'Division' in MyProfile";
    	        			jQuery(wareHouseDialogDiv).html('<span><b style="color:red;">'+errorDialogText+'</b></span>');
    	        			jQuery(wareHouseDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
    	        			buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
    	        			return false;
    	        	}
    	        }
            });

            }
   
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

function onlyAlphabets(e, t) {
    try {
        if (window.event) {
            var charCode = window.event.keyCode;
        }
        else if (e) {
            var charCode = e.which;
        }
        else { return true; }
        if ((charCode > 64 && charCode < 91) || (charCode > 96 && charCode < 123))
            return true;
        else
            return false;
    }
    catch (err) {
        alert(err.Description);
    }
}

function numberOnly(txt, e) {
    var arr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
    var code;
    if (window.event)
        code = e.keyCode;
    else
        code = e.which;
    var char = keychar = String.fromCharCode(code);
    if (arr.indexOf(char) == -1)
        return false;
    
}

$("#wantedId, customerNAMEId, specialInstructionID").keypress(function(e){
    if (window.event)
        code = e.keyCode;
    else
        code = e.which;
    if(code == 32 || (code>=97 && code<=122)|| (code>=65 && code<=90))
        return true;
    else
        return false;
});


function isNumberKey(evt)
{
   var charCode = (evt.which) ? evt.which : event.keyCode;
   if (charCode != 46 && charCode > 31 
     && (charCode < 48 || charCode > 57))
      return false;

 	return true;
}
function isNumberKeyLine(evt)
{
var charCode = (evt.which) ? evt.which : event.keyCode;
if (charCode != 46 && charCode > 31 
&& (charCode < 48 || charCode > 57))
return false;
return true;
}

function isNumberKeyAck(evt)
{
var charCode = (evt.which) ? evt.which : event.keyCode;
if (charCode != 46 && charCode > 31 
&& (charCode < 48 || charCode > 57))
return false; 
return true;
}

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

function CancelLineDates()
{
	$("#AcknowledgeAllDate").dialog("close");
	$('#ackDateAll').val("");
	$('#shipDateAll').val("");
}


function viewPOPDF(quote){
	if(quote === 'purchase'){
		 try{
			 var vePOID = $("#vePOID").val();
				if(vePOID === null || vePOID.length<= 0){
					errorText = "Please Save Purchase Order to View PDF.";
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}
				var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToID}";
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
	}else{	
	var vePOID = $("#vePOID").val();
	if(vePOID === null || vePOID.length<= 0){
		errorText = "Please Save Purchase Order to View PDF.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	var aPDFType = "Po";
	var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToID}";
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
			var jobNumber="";
			if(data!=null && data.joMasterID!=null){
				jobNumber =data.jobNumber;
				joMasterID=data.joMasterID;
				}
			window.open("./purchasePDFController/viewPDFLineItemForm?vePOID="+vePOID+"&puchaseOrder="+aPDFType+"&jobNumber="+jobNumber+"&rxMasterID="+rxCustomerId+"&manufacturerID="+aManufacturerId+"&shipToAddrID=0"+"&joMasterID="+joMasterID);
		},
		error: function (msg) {
			
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

function getTaxRate()
{
	var country = $('#shipToCity').val();
	var taxRate = "${country}";
	$('#taxGeneralId').val(formatCurrencynodollar(taxRate));

	/* alert("country---->"+country);
	$.ajax({ 
		url: "./getTaxTerritory",
		mType: "POST",
		data : { 'country' : country },
		success: function(data){
			var taxRate = data.taxRate;
			$('#taxGeneralId').val(formatCurrencynodollar(taxRate));
		}
	});*/
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
					buttons : [{height:35,text: "Yes",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#PoStatusButton').val('Close');setStatus=2;}},
					           {height:35,text: "No",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#PoStatusButton').val('Open');setStatus=1;}},
					           {height:35,text: "Cancel",click: function() { $(this).dialog("close");$("#showPOOptions").dialog("destroy").remove();$('#PoStatusButton').val('Open');setStatus=1;}}]
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
function copyOption(){
	console.log("copyOption-selectedLineItem"+selectedLineItem);
	try{
	if(selectedLineItem != undefined && selectedLineItem!=''){
		
	var rows = jQuery("#lineItemGrid").getDataIDs();
	row=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
	var jsonListData = "{note:\""+row['note']+"\",inLineNoteImage:\""+row['notes']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+"\",quantityReceived:\""+row['quantityReceived']+"\",invoicedAmount:\""+row['invoicedAmount']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
	var jsonList1='';
	var rows = jQuery("#lineItemGrid").getDataIDs();
	console.log(rows);
	//rows =rows.replace(/[^0-9\.]+/g,"");
	selectedLineItem =undefined;
	for(a=0;a<rows.length;a++)
	{
			row=jQuery("#lineItemGrid").getRowData(rows[a]);
			if(a == 0)
		        	jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+row['notes']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+"\",quantityReceived:\""+row['quantityReceived']+"\",invoicedAmount:\""+row['invoicedAmount']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
	        else
	        	jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+row['notes']+"\",description:\""+row['description']+"\",notes:\""+row['notes']+"\",notesdesc:\""+row['notes']+"\",quantityOrdered:\""+row['quantityOrdered']+"\",quantityReceived:\""+row['quantityReceived']+"\",invoicedAmount:\""+row['invoicedAmount']+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
			
	}
	jsonList1=jsonList1+','+jsonListData;
	loadLineItemGrid(jsonList1);
	calculateTotal();
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
	}catch(err){
	
	}
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
	$("#freightLineId").val(formatCurrencynodollar(frieghtvalue));
	$("#taxLineId").val($("#Tax_ID").text());
	var last = $('#lineItemGrid').jqGrid('getGridParam','records');
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
		//var JopInlinetext =lineItemText.replace("&And", "'");
		/* areaLine = new nicEditor({buttonList : ['bold','italic','underline','left','center','right','justify','ol','ul','fontSize','fontFamily','fontFormat','forecolor'], maxHeight : 220}).panelInstance('inlineItemId');
		$(".nicEdit-main").empty();
		$(".nicEdit-main").append(lineItemText); */
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
//		var lineItemNote= $('#POInLineItemID').find('.nicEdit-main').html();
//		var grid = $("#lineItemGrid");
//		var rowId = grid.jqGrid('getGridParam', 'selrow');
//		console.log("Line Item Note: "+lineItem);
//		//alert('Selected Row Item: '+rowId);
//		//var lineItem = $("#lineItemNote").val();
//		$("#lineItemGrid").jqGrid('setCell', rowId,'notes', lineItem);
//		$("#lineItemGrid").jqGrid('setCell', rowId,'inlineNoteImage',lineItem);
//		var lineItem ='';
//		lineItem = lineItemNote.replace(/"/g, "'");


	//var inlineText= $('#POInLineItemID').find('.nicEdit-main').html();
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
//		 var jsonList1='';
//		try{
//		///var selectedRow=jQuery("#lineItemGrid").getRowData(rows[selectedLineItem-1]);
//		var rows = jQuery("#lineItemGrid").getDataIDs();
//		var rowr=jQuery("#lineItemGrid").getRowData(rows[rowId-1]);
//		var notes_test = rowr['inLineNote'];
//		rowId = rowId.replace(/[^0-9\.]+/g,"");
//		for(a=0;a<rows.length;a++)
//		{
//			var row=jQuery("#lineItemGrid").getRowData(rows[a]);
//			notes_test=row['inLineNote'];
//		if(a==(rowId-1)){
//		notes_test = lineItem;
//		}
	
//		if(a == 0){
//		jsonList1="{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
//		}else{
//		jsonList1 = jsonList1+','+"{vePodetailId:\""+row['vePodetailId']+"\",note:\""+row['note']+"\",inLineNoteImage:\""+notes_test+"\",description:\""+row['description']+"\",notes:\""+notes_test+"\",notesdesc:\""+notes_test+"\",quantityOrdered:\""+row['quantityOrdered']+ "\",inLineNote:\""+notes_test+"\",unitCost:\""+row['unitCost']+"\",priceMultiplier:\""+row['priceMultiplier']+"\",taxable:\""+row['taxable']+"\",quantityBilled:\""+row['quantityBilled']+"\",ackDate:\""+row['ackDate']+"\",shipDate:\""+row['shipDate']+"\",vendorOrderNumber:\""+row['vendorOrderNumber']+"\",prMasterId:\""+row['prMasterId']+"\"}";
//		}
//		console.log(jsonList1+a);
	
//		}
//		}
//		catch(e){
//			console.log('Error in load: '+e.message);
//		}
//		loadLinesItemGrid();
//		areaLine.removeInstance('inlineItemId');
//		jQuery("#POInLineItem").dialog("close");
//		$("#lineItemGrid").trigger("reloadGrid");
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
				areaLine.removeInstance('inlineItemId');
				return true;
			}	
	});
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
				var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToID}";
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
						if(dd<10){dd='0'+dd;} 
						if(mm<10){mm='0'+mm;} 
						if(hours<10){hours='0'+hours;} 	
						if(minutes<10){minutes='0'+minutes;} 
						today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
						$.ajax({ 
							url: "./jobtabs3/updateEmailStampValue",
							mType: "GET",
							data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
							success: function(data){ 
								$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
								$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); },
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
			},
			error:function(e){
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
			//	},	
		});

		});

	function submitemailattachment(){
		$('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		sendsubmitMailFunction();
	}


	 function loadBillAddress()
	 {
		 $("#billtolabel1").removeClass('ui-state-active');
		 $("#billtolabel2").removeClass('ui-state-active');
		 $("#billtolabel3").removeClass('ui-state-active');
		 var billToIndex = "${theVepo.billTo}";
		 var rxbillToAddId = "${theVepo.rxBillToId}";
		 if(billToIndex === 0)
			 {
			 $("#billtolabel1").addClass('ui-state-active');
			 billToAddress('Us');
			 }
		 else if(billToIndex === 1)
			 {
			 $("#billtolabel2").addClass('ui-state-active');
			 billToAddress('Customer');
			 }
		 else if(billToIndex === 2)
		 {
			 $("#billtolabel3").addClass('ui-state-active');
			 billToAddress('Other');
			 }
		 
		 else 
		 {
			 $("#billtolabel1").addClass('ui-state-active');
			 billToAddress('Us');
			 }
	 }
	function loadOtherTabs()
	{
		$('#vendorLineNameId').val($('#manfactureName option:selected').text());
		$('#vendorAckNameId').val($('#manfactureName option:selected').text());
		
	}
	
	function loadDates() {
		$('#poDateLineId').val($('#poDateId').val());
		//$('#poDateAckId').val($('#poDateId').val());
		$('#ourPoLineId').val($('#ourPoId').val());
		//$('#ourPoAckId').val($('#ourPoId').val());
		$('#vendorLineNameId').val($('#vendorsearch').val());

		$('#freightLineId').val($('#freightGeneralId').val());
		$('#taxLineId').val($('#taxGeneralId').val());
		if( isNaN($('#totalLineId').val()))
			$('#totalLineId').val("0.00")
	}

	function loadAck(data){
			
		var schgrid='<table id="Ack"></table><div id="Ackpager"></div>';	
		$('#jqgridAck').empty();
		$('#jqgridAck').append(schgrid);
		$("#Ack").jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			data: data,
			pager: jQuery('#Ackpager'),
			//url:'./jobtabs5/jobReleaseAck',	
			postData: {vePOID : $('#vePOID').val()},		
			colNames:['Product No','Description','Qty.','Ack.','Ship','Order #','VePOID','prMasterID','VePODetailID'],
			colModel:[
			          	{name:'note',index:'note',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false}},
			           	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
			          																													cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true}},
						{name:'ackDate', index:'ackDate', align:'center', width:50,hidden:false, editable:true,  editoptions:{size:15, align:'center',},editrules:{edithidden:true}},
						{name:'shipDate', index:'shipDate', align:'center', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'center'}, editrules:{edithidden:true}},
						{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
						{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
						{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
						{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}}],
					rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
					sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
					height:210,	width: 920, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Acknowledgment',
					jsonReader : {
						root: "rows",
						page: "page",
						total: "total",
						records: "records",
						repeatitems: false,
						cell: "cell",
						id: "id",
						userdata: "userdata"
					},
				loadComplete: function(data) { 	},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){
					 
				},
				editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
			}).navGrid('#Ackpager', {add: false, edit:true,del: false,refresh:true,search: false},
					//-----------------------edit options----------------------//
					{
						width:300, left:700, top: 300, zIndex:1040,
						closeAfterEdit:true, reloadAfterSubmit:true,
						modal:true, jqModel:true,
						editCaption: "Edit Acknowledgment",
						beforeShowForm: function (form) 
						{
							/*$(".navButton").empty();
							$(".navButton").append('<td style="padding-left: 60px;"><input type="button" id="saveAllButton" class="savehoverbutton turbo-blue" value="All" onclick="saveAll()"></td>');*/
							var grid = $('#Ack');
							var id = jQuery("#Ack").jqGrid('getGridParam','selrow');
							var shipDate = grid.jqGrid('getCell', id, 'shipDate');
							var ackDate = grid.jqGrid('getCell', id, 'ackDate');
							jQuery("#tr_note", form).hide();
							jQuery("#tr_description", form).hide();
							jQuery("#tr_quantityOrdered", form).hide();
							jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').append('Ack.: ');
							jQuery('#TblGrid_Ack #tr_ackDate .DataTD').empty();
							jQuery('#TblGrid_Ack #tr_ackDate .DataTD').append('&nbsp;<input type="text" size="15" align="center" id="ackDate" name="ackDatename" role="textbox">&nbsp;');
							jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').append('Ship: ');
							jQuery('#TblGrid_Ack #tr_shipDate .DataTD').empty();
							jQuery('#TblGrid_Ack #tr_shipDate .DataTD').append('&nbsp;<input type="text" size="15" aligntext="center" id="shipDate" name="shipDatename" role="textbox">&nbsp;');
							jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').append('Order #: ');
							$("#ackDate").val(ackDate);
							$("#shipDate").val(shipDate);
							$( "#ackDate" ).datepicker({
							      showOn: "button",
							      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
							      buttonImageOnly: true
							    });
							$( "#shipDate" ).datepicker({
							      showOn: "button",
							      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
							      buttonImageOnly: true
							    });
						},
						onclickSubmit: function(params){
							var aAckDate =  $("#ackDate").val();
							var aShipDate =  $("#shipDate").val();
							return { 'operForAck' : 'acknowlegement', 'shipDatename' : aShipDate, 'ackDatename' : aAckDate };
						},
						afterSubmit:function(response,postData){
							 return [true, loadAck()];
						 }
					},
					{
						//-----------------------Add options----------------------//
							width:450, left:400, top: 300, zIndex:1040,
							closeAfterEdit:true, reloadAfterSubmit:true,
							modal:true, jqModel:true,
							editCaption: "Add Acknowledgment"
					}
			); 
			var mydata = eval ("[" + data + "]");	
		
			for(var i=0;i<mydata.length;i++) {
		
			$("#Ack").jqGrid('addRowData',i+1,mydata[i]);
			
		}
	}
	
	function loadShipAddress() {
		
		 var shiptomode = "${theVepo.shipToMode}";	 
		 var divflag = "#PO_Shiptooutside";
		 $(divflag).contents().find("#shiptoaddrhiddenfromdbid").val("");
		 $(divflag).contents().find("#shiptomodehiddenfromdbid").val("");
		 var checkshiptoid;

		 $("#shiptoaddradio3").hide();
		 $("#shiptoaddlabel3").hide();
		 
		 $("#shiptoaddradio2").button({disabled:true});
		 $("#shiptoaddradio4").button({disabled:true});
		 $('#shiptoaddlabel2').attr('onclick','').unbind('click');
		 $('#shiptoaddlabel4').attr('onclick','').unbind('click');
			
		preloadShiptoAddress(divflag,null,null,'0','0','','');
		$(divflag).contents().find("#shiptomoderhiddenid").val('0');
		
	 }

	 
	function  shipToAddress(type){
		var size = "${fn:length(wareHouse)}";
		$("#shipToAddressID").attr("disabled",true);
		 $("#shipToAddressID1").attr("disabled",true);
		 $("#shipToAddressID2").attr("disabled",true);
		 $("#shipToCity").attr("disabled",true);
		 $("#shipToState").attr("disabled",true);
		 $("#shipToZipID").attr("disabled",true);

		if(type === 'Us')
		{			
			$('#customerShipToOtherID').val("Us");
			$("#shiptomodeId").val("0");
			//$('#customerShipToID').val("");
			if(size > 0) {
				if(size > 0) {
					if(size == 1) {
						$('#forWardId').prop('hidden',true);
						$('#backWardId').prop('hidden',true);
					}else {
						$('#forWardId').prop('hidden',false);
						$('#backWardId').prop('hidden',false);
					}
				}			
				console.log("${wareHouse[0].description}");
				$('#shipToAddressID').val("${wareHouse[0].description}");
				$('#shipToAddressID1').val("${wareHouse[0].address1}");
				$('#shipToAddressID2').val("${wareHouse[0].address2}");
				$('#shipToCity').val("${wareHouse[0].city}");
				$('#shipToState').val("${wareHouse[0].state}");
				$('#shipToZipID').val("${wareHouse[0].zip}");
				$('#prWarehouseID').val("${wareHouse[0].prWarehouseId}");
				var sTaxRate = "${wareHouse[0].email}";
				$('#taxGeneralId').val(formatCurrencynodollar(sTaxRate));
				//alert(formatCurrencynodollar(sTaxRate))
				var coTaxTerritoryId = "${wareHouse[0].coTaxTerritoryId}";
				//getTaxRate(coTaxTerritoryId);
				}else{
				$('#forWardId').prop('hidden',true);
				$('#backWardId').prop('hidden',true);
			}
		}
		if(type === 'Customer'){

			$("#shiptomodeId").val("1");
			$('#ShowHideShipTo').empty();
			$('#ShowHideShipTo').append('<input type="text" id="shipToAddressID" name="shipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled" placeholder="Minimum 1 character required to get Name List">')
			
			$('#customerShipToOtherID').val("Customer");
			$('#forWardId').prop('hidden',true);
			$('#backWardId').prop('hidden',true);
			$("#shipToAddressID").attr("disabled",false);

			if(typeof(shipToCustomer)  !== "undefined"){
				$.each(shipToCustomer, function(index, value){
					$('#shipToAddressID').val(value.name);
					$('#shipToAddressID1').val(value.address1);
					$('#shipToAddressID2').val(value.address2);
					$('#shipToCity').val(value.city);
					$('#shipToState').val(value.state);
					$('#shipToZipID').val(value.zip);
					$('#taxGeneralId').val(formatCurrencynodollar(taxRateShipTo));
				});
			}else{
				$('#shipToAddressID').val("${Rxaddress.name}");
				$('#shipToAddressID1').val("${Rxaddress.address1}");
				$('#shipToAddressID2').val("${Rxaddress.address2}");
				$('#shipToCity').val("${Rxaddress.city}");
				$('#shipToState').val("${Rxaddress.state}");
				$('#shipToZipID').val("${Rxaddress.zip}");
				$('#taxGeneralId').val(formatCurrencynodollar(taxRateShipTo));
			}
			
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
			$('#taxGeneralId').val(formatCurrencynodollar(0.0));
			$('#ShowHideShipTo').empty();
			$('#ShowHideShipTo').append('<input type="text" id="shipToAddressID" name="shipToAddressID" class="validate[maxSize[100]" style="width: 300px;" disabled="disabled">')
			$('#customerShipToOtherID').val("Other");
			$('#forWardId').prop('hidden',true);
			$('#backWardId').prop('hidden',true);
			//$('#customerShipToID').val("");
			$("#shipToAddressID").attr("disabled",false);
			 $("#shipToAddressID1").attr("disabled",false);
			 $("#shipToAddressID2").attr("disabled",false);
			 $("#shipToCity").attr("disabled",false);
			 $("#shipToState").attr("disabled",false);
			 $("#shipToZipID").attr("disabled",false);
			 if(onChangeShipToOther == 1)
				{
				 $('#shipToAddressID').val("");
					$('#shipToAddressID1').val("");
					$('#shipToAddressID2').val("");
					$('#shipToCity').val("");
					$('#shipToState').val("");
					$('#shipToZipID').val(""); 
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



   	var shipToAddressSize = "${fn:length(wareHouse)}";
   	var shipToAddressArray = [];
   	if(shipToAddressSize>0){
   		<c:forEach items="${wareHouse}" var="house">
   		shipToAddressArray.push({description: "${house.description}", address1: "${house.address1}", address2: "${house.address2}", city: "${house.city}", state: "${house.state}",wareHouseID:"${house.prWarehouseId}", zip: "${house.zip}", taxRate: "${house.email}"});
		</c:forEach>
   	   	}

	var shipAddressInc = 0;
	var zFlag = '0';
	
	function shipForWard()
	{
		
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
	
function savePOLineItems()
{
	    $("#lineItemGrid").jqGrid('resetSelection');
	    var gridRows = $("#lineItemGrid").jqGrid('getRowData');
	    var rowData = new Array();
	    for (var i = 0; i < gridRows.length; i++) {
	        var row = gridRows[i];
	        rowData.push($.param(row));
	    }
	    /* var dataToSend = JSON.stringify(gridRows);
	    alert('Data To Send ------->'+dataToSend);
	     $.ajax({
	        url: './jobtabs3/addPOLineDetails?vepoid='+$('#vePOID').val(),
	        type: 'POST',
	        data: { gridData: dataToSend },
	        dataType: 'json',
	        success: function (result) {
	            alert('success');
	        }
	    });  */
	    return true;
	
}

function loadACK()
{
	/* if($('#vePOID').val() == '' ){
		msgOnAjax("Please Save General Tab.","RED",6000);
		$( "#generalTab" ).tabs({ active: 0 });
		return false;
		} */
		$('#vendorAckNameId').val($('#vendorsearch').val());
		$('#poDateAckId').val($('#poDateId').val());

		$("#lineItemGrid").jqGrid('resetSelection');
	    var gridRows = $("#lineItemGrid").jqGrid('getRowData');
	    var rowData = new Array();
	    var jsonList1;
	    var total = 0;
	    for (var i = 0; i < gridRows.length; i++) {
	        var row = gridRows[i];
	         //var afreightTotal = $("#freightGeneralId").val().replace(/[^0-9\.]+/g,"");
			total = parseFloat(total)+parseFloat(row.quantityBilled.replace(/[^0-9\.]+/g,""));
	        
	        if(i == 0)
	        	jsonList1="{note:\""+row.note+"\",description:\""+row.description+"\",quantityOrdered:\""+row.quantityOrdered+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\"}";
	        else
	        	jsonList1 = jsonList1+','+"{note:\""+row.note+"\",description:\""+row.description+"\",quantityOrdered:\""+row.quantityOrdered+"\",ackDate:\""+row.ackDate+"\",shipDate:\""+row.shipDate+"\",vendorOrderNumber:\""+row.vendorOrderNumber+"\"}";

	       
	        
	        rowData.push($.param(row));
	    }
	     var jsonBerthList = {"table":rowData};
	    console.log("RowData--->"+rowData);
	    /* var acklist = "";
	    for (var i = 0; i < jsonBerthList.table.length; i++){
	    	alert(jsonBerthList.table[i].note);
	    	acklist+=jsonBerthList.table[i].note
	    } */	
		
	    $('#subtotalLineId').val(formatCurrencynodollar(total ));
	    $('#subtotalKnowledgeId').val($('#subtotalLineId').val());	
	    $('#freightKnowledgeId').val($('#freightGeneralId').val());
	       
	    //$('#subtotalGeneralId').val("$"+total);
	loadAck(jsonList1);
}
function calculateSubTot()
{
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
  //  $('#lineID').val(formatCurrencynodollar(taxCost));
	   if($('#customerBillToOtherID').val() !== 'Us')
		{
	    $('#generalID').val($('#lineID').val());
		}
 	  else
	   {
	   $('#generalID').val($('#lineID').val());
	   }

    
    $('#subtotalLineId').val(formatCurrencynodollar(total ));
    $('#subtotalKnowledgeId').val($('#subtotalLineId').val());
	$('#subtotalGeneralId').val($('#subtotalLineId').val());

	
	$('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
	$('#totalLineId').val($('#totalGeneralId').val());
	$('#totalKnowledgeId').val($('#totalGeneralId').val());
	
}

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

/******************** Vendor Name Search ***********************/
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
							//$('#customerBillToAddressID').val(value.rxAddressId);
						
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


function billToAddress(type)
{
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
		$('#billToAddressID').val("${billtoaddress.billToDescription}");
		$('#billToAddressID1').val("${billtoaddress.billToAddress1}");
		$('#billToAddressID2').val("${billtoaddress.billToAddress2}");
		$('#billToCity').val("${billtoaddress.billToCity}");
		$('#billToState').val("${billtoaddress.billToState}");
		$('#billToZipID').val("${billtoaddress.billToZip}");
		loadLineItems();
		}
	if(type === 'Customer')
		{			
		$('#customerBillToOtherID').val("Customer");
		$("#billToAddressID").attr("disabled",false);
		if(typeof(billToCustomer)  !== "undefined")
		{
			$.each(billToCustomer, function(index, value){
				$('#billToAddressID').val(value.name);
				$('#billToAddressID1').val(value.address1);
				$('#billToAddressID2').val(value.address2);
				$('#billToCity').val(value.city);
				$('#billToState').val(value.state);
				$('#billToZipID').val(value.zip);
			});
			
		}
		else
		{
			 $('#billToAddressID').val("");
				$('#billToAddressID1').val("");
				$('#billToAddressID2').val("");
				$('#billToCity').val("");
				$('#billToState').val("");
				$('#billToZipID').val("");
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
		//$('#customerBillToID').val("");
		$("#billToAddressID").attr("disabled",false);
		 $("#billToAddressID1").attr("disabled",false);
		 $("#billToAddressID2").attr("disabled",false);
		 $("#billToCity").attr("disabled",false);
		 $("#billToState").attr("disabled",false);
		 $("#billToZipID").attr("disabled",false);
		 if(onChangeOther == 1)
			{
			 $('#billToAddressID').val("");
				$('#billToAddressID1').val("");
				$('#billToAddressID2').val("");
				$('#billToCity').val("");
				$('#billToState').val("");
				$('#billToZipID').val("");
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


function loadLineItems()
{	
    var gridRows = $("#lineItemGrid").jqGrid('getRowData');
    var rowData = new Array();
    var jsonList1;
    var total = 0;
    var jsonList1;
    var total = 0;
    var taxTot = 0.0;
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
    var taxPerc = 0;
    var taxValue = $('#taxLineId').val().replace(/[^0-9\.]+/g,"");
    var taxCost = 0.0;
   // if($('#customerBillToOtherID').val() !== 'Us')
   // {
   	taxCost = (parseFloat(taxTot)*parseFloat(taxValue))/100;
   // }

    $('#lineID').val(formatCurrencynodollar(taxCost));
    $('#generalID').val($('#lineID').val());
    $('#subtotalLineId').val(formatCurrencynodollar(total ));
    $('#subtotalKnowledgeId').val($('#subtotalLineId').val());	
    $('#subtotalGeneralId').val($('#subtotalLineId').val());

    $('#totalGeneralId').val(formatCurrencynodollar(parseFloat($('#subtotalLineId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#freightGeneralId').val().replace(/[^0-9\.]+/g,""))+parseFloat($('#lineID').val().replace(/[^0-9\.]+/g,""))));
	$('#totalLineId').val($('#totalGeneralId').val());
	$('#totalKnowledgeId').val($('#totalGeneralId').val());
    loadLineItemGrid(jsonList1);
}
    </script>
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