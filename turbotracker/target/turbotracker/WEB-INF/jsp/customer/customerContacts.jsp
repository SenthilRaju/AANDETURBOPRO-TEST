<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="vendorGeneral">
	<form action="" id="customerFromID">
	<table>
		<tr height="0px;"></tr>
		<tr>
			<td width="125px;" style="padding-bottom: 10px;"><label>Customer Name: </label></td>
			 <td width="615px;" style="padding-bottom: 10px;">
				<input type="text" name="EmployeeName" size="32" id="customerNameHeader"  class="validate[maxSize[40]]"/>
				<input type="button" class="savebutton"  value="    " onclick="updateCustomer()" id="saveButton" >
			</td>
			<td align="right">
				<input type="button" class="quickQuotehoverbutton turbo-blue" value="Bid List" onclick="openBidList()" style="width:90px;">
				<input type="button" class="quickQuotehoverbutton turbo-blue" value="Address" onclick="openCustomerAddress()" style="width:90px;"> 
				<input type="button" class="quickQuotehoverbutton turbo-blue" value="Quick Quote" onclick="openQuickQuote()" style="width:120px;">
				<input type="hidden" id="dummyContactName">
				<input type="hidden" id="contactIDhiden">
				<input type="hidden" id="rxMasterIDhiden">
			</td>
		</tr>
		<tr>
			<td colspan="5"><hr width="1100px"></td>
		</tr>
	</table>
		<table>
			<tr>
				<td colspan="3">
					<div id="EmployeeDetails" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="EmployeeDetailsGrid" class="scroll"></table>
						<div id="EmployeeDetailsGridpager" class="scroll" style="text-align: right;"></div>
					</div>
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td align="right"><label>Categories:&nbsp;</label>
					<input type="checkbox" id="customerchek" style="vertical-align: middle;" onclick="customer()" onChange="updateRolodex()" ><label>&nbsp;Customer</label>
					<input type="checkbox" id="employeeche"	style="vertical-align: middle;" onclick="employee()" onChange="updateRolodex()"><label>&nbsp;Employee</label>
					<input type="checkbox" id="vendorcheck"	style="vertical-align: middle;" onclick="vendor()" onChange="updateRolodex()"><label>&nbsp;Vendor</label>
					<input type="checkbox" id="engineerche"	style="vertical-align: middle;" onclick="engineer()" onChange="updateRolodex()"><label>&nbsp;Engineer</label>
					<input type="checkbox" style="vertical-align: middle;" id="architectche" onclick="architect()" onChange="updateRolodex()"><label>&nbsp;Architect</label>
					<input type="checkbox" id="caustMGR" name="caustMGR" style="vertical-align: middle;" onChange="updateRolodex()"><label>&nbsp;G.C/Constr. MGR</label>
					<input type="checkbox" id="owner" name="owner" style="vertical-align: middle;" onChange="updateRolodex()"><label>&nbsp;Owner</label>
					<input type="checkbox" id="bondAgent" name="bondAgent" style="vertical-align: middle;" onChange="updateRolodex()"><label>&nbsp;Bond Agent</label>
				</td>
			</tr>
		</table> 
		<table id="customerChecked">
			<tr id="customerGridQuotes">
				<td colspan="3">
					<div id="contactIdbasedOpenQuotes" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="OpenGridQuotes" class="scroll"></table>
						<div id="OpenGridQuotespager" class="scroll" style="text-align: right;"></div>
					</div>
				</td>
			</tr>
			<tr id="lostQuotes">
				<td colspan="3">
					<div id="customerLostQuotes" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="customerLostQuotesGrid" class="scroll"></table>
						<div id="customerLostQuotesGridPager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			<tr id="Quotes">
				<td colspan="3">
					<div id="customerQuotes" style="padding-left: 0px;">
						<table style="padding-left: 0px" id="customerQuotesGrid" class="scroll"></table>
						<div id="customerQuotesGridPager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			<tr height="5px;"></tr>
			<tr >
				<td style="width: 120px" id = "lostQuotesCheck"><label style="vertical-align: middle;">Lost Quotes:</label>
						<input type="checkbox" id="lostQuote" name="lostQuotes" value="" style="vertical-align:middle;" onclick="lostQuotesShow()"/>
				</td>
				<td id = "QuotesCheck"><label style="vertical-align: middle;">Quick Quote:</label>
						<input type="checkbox" id="Quote" name="Quotes" value="" style="vertical-align:middle;" onclick="QuotesShow()"/>
				</td>
			</tr>
		</table>
			</form>
	</div>
	<div id="editQuickQuoteDialog">
		<form action="" id="editQuickQuoteFormID">
			<table>
				<tr id="editQuoteJobNumber"><td><label>Job Number</label></td><td><input type="text"  id="editQuoteJobNumberID" name="editQuoteJobNumberName" style="width: 275px;height: 22px;" readonly="readonly"></td></tr>
				<tr id="editQuoteJoBidder" style="display: none;"><td><label>JoBidderId</label></td><td><input type="text"  id="editQuoteJoBidderID" name="editQuoteJoBidderIdName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteJoMaster" style="display: none;"><td><label>JoMasterId</label></td><td><input type="text"  id="editQuoteJobMasterID" name="editQuoteJobMasterName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteRxContact" style="display: none;"><td><label>rxContactId</label></td><td><input type="text"  id="editQuoteRxContactID" name="editQuoteRxContactName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteRxMaster" style="display: none;"><td><label>rxMasterId</label></td><td><input type="text"  id="editQuoteRxMasterID" name="editQuoteRxMasterName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Job Name:<b style="color: #BA3851;">*</b></label></td>
						<td><input type="text" id="editQuoteProductListID" name="editQuoteProductListName"  class="validate[required]" style="width: 275px;height: 22px;" placeholder="Minimum 2 characters required">
				<tr><td><label>Date:</label></td><td><input type="text" class="datepicker" id="editQuoteDateID" name="editQuoteDateName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Contact:<b style="color: #BA3851;">*</b></label></td>
						<td><input type="text" id="editQuoteCustomerID" name="editQuoteCustomerName" style="width: 275px;height: 22px;"  class="validate[required]" placeholder="Minimum 2 characters required">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
						<input id="editQuoteCustomerHiddenID" name="editQuoteCustomerHiddenName" style="width: 100px;display: none;">
						<input id="editQuoteRxMasterHiddenID" name="editQuoteRxMasterHiddenName" style="width: 100px;display: none;">
						<input id="editQuoteCustomerContactHiddenID" name="editQuoteCustomerContactHiddenName" style="width: 275px;height: 22px;display: none;"></td></tr>
				<tr  style="display: none;"><td><label>Description:</label></td>
						<td><textarea id="editQuoteDescriptionID" name="editQuoteDescriptionName"  rows="4" cols="32"></textarea>
				<tr  style="display: none;"><td><label>Manufacturer:</label></td>
						<td><input type="text" id="editQuoteManufacturerID" name="editQuoteManufacturerName" style="width: 275px;height: 22px;" placeholder="Minimum 1 character required">
				<tr><td><label>Estimated Cost:</label></td><td><input type="text" id="editQuoteCostID" name="editQuoteCostName" style="width: 275px;height: 22px;text-align: right;"></td></tr>
				<tr><td><label>Contract Amount:</label></td><td><input type="text" id="editQuoteSellPriceID" name="editQuoteSellPriceName" style="width: 275px;height: 22px;text-align: right;"></td></tr>
				<tr>
				<td><label>Revision:</label></td><td><input type="text" id="editQuoteRevisionID" name="editQuoteRevisionName" style="width: 70px;height: 22px;"></td></tr>
				<tr><td><label>Status:</label></td>
				<td>
					<select id="editQuoteJobStatusID" name="editQuoteJobStatusName">
					<option value="3">Booked</option>
					<option value="2">Lost</option>
					<option value="1">Quote</option>
					</select>
				</td></tr>
				<tr height="5px;"></tr>
				<tr><td colspan="2"><hr width="450px;"></td></tr>
			</table>
			<table style="FONT-FAMILY: 'Century Schoolbook L'; width:450px">
		 	<tr>
			 	<td></td>
			 	<td align="right" style="padding-right:1px;">
					<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="updateQuickQuote()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelUpdateQuickQuote()" style="width:80px;">  
				</td>
			</tr>
		</table>
		</form>
	</div>
	<div id="quickQuoteDialog">
		<form action="" id="quickQuoteFormID">
			<table>
				<tr><td><label>To:<b style="color: #BA3851;">*</b></label></td>
						<td><input type="text" id="customerID" name="customerName" style="width: 275px;height: 22px;"  class="validate[required]" readonly="readonly" placeholder="Minimum 2 characters required">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
						<input type="hidden" id="customerHiddenID" name="customerHiddenName" style="width: 100px;">
						<input type="hidden" id="rxMasterHiddenID" name="rxMasterHiddenName" style="width: 100px;">
						<input type="hidden" id="customerContactHiddenID" name="customerContactHiddenName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Re:<b style="color: #BA3851;">*</b></label></td><td><input type="text"  id="productListID"  name="productListName" style="width: 275px;height: 22px;"   class="validate[required]" placeholder="Minimum 2 characters required">
				<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" ></td></tr>
				<tr><td><label>Description:</label></td>
						<td><textarea  id="pharagraphID"  name="pharagraphName" rows="4" cols="32"></textarea>
				<tr><td><label>Estimated Cost:</label></td><td><input type="text" id="costID" name="costName" style="width: 275px;height: 22px;text-align: right;"></td></tr>
				<tr><td><label>Manufacturer:</label></td>
						<td><input type="text" id="manufacturerID" name="manufacturerName" style="width: 275px;height: 22px;" placeholder="Minimum 1 character required">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
						<input type="hidden" id="manufacterHiddenID" name="manufacterHiddenName" style="width: 100px;"></td>
						</tr>
				<tr><td><label>Contract Amount:</label></td><td><input type="text" id="sellPriceID" name="sellPriceName" style="width: 275px;height: 22px;text-align: right;"></td></tr>
		  <!-- <tr><td><label>Attach:</label></td><td><input type="hidden" class="file" id="attachedID" name="attachedName" style="width: 260px;"></td></tr> -->
		  		<tr>
				<td><label>Revision:</label></td><td><input type="text" id="revision" name="revision" style="width: 70px;height: 22px;"></td></tr>
				<tr><td><label>Status:</label></td>
				<td>
					<select id="jobStatusID" name="jobStatusName">
					<option value="3">Booked</option>
					<option value="2">Lost</option>
					<option value="1" selected="selected">Quote</option>
					</select>
				</td></tr>
				<tr height="5px;"></tr>
				<tr><td colspan="2"><hr width="450px;"></td></tr>
			</table>
			<table style="FONT-FAMILY: 'Century Schoolbook L'; width:450px">
		 	<tr>
			 	<td></td>
			 	<td align="right" style="padding-right:1px;">
			 		<input type="button" class="cancelhoverbutton turbo-blue"  value="View" onclick="viewQuickQuote()" style="width:80px;">  
			 		<input type="button" class="cancelhoverbutton turbo-blue"  value="Email" style="width:80px;" onclick="emailQuickQuote()">
			 		<!-- <a href="mailto:info@excelblaze.com"><input type="button" class="cancelhoverbutton turbo-blue"  value="Email" style="width:80px;" onclick="faxQuickQuote()"></a> -->  
			 		<input type="button" class="cancelhoverbutton turbo-blue"  value="Fax" onclick="faxQuickQuote()" style="width:80px;">  
					<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveQuickQuote()" style=" width:80px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelQuickQuote()" style="width:80px;">  
				</td>
			</tr>
		</table>
		</form>
	</div>
	<div id=bidListDialog>
	<form action="" id="bidListFormID">
		<table id="customerFilterSearchGrid"></table>
		<hr width="710px;">
		<table align="right">
			<tr>
			 	<td align="right" style="padding-right:1px:">
			 		<input type="button" class="quickQuotehoverbutton turbo-blue"  value="Find Project" onclick="openFindProject()" style=" width:100px;">			 	
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelUpComingCustomer()" style="width:80px;">
					<input type="hidden" id="changeID">  
				</td>
			</tr>
		</table>
		</form>
	</div> 
	<div id=bidListsingleDialog>
	<form action="" id="bidListsingleFormID">
		<table id="singlecustomerGrid"></table>
		<hr width="710px;">
		<table align="right">
			<tr>
			 	<td align="right" style="padding-right:1px:">
			 		<input type="button" class="quickQuotehoverbutton turbo-blue"  value="Find Project" onclick="openFindProject()" style=" width:100px;">			 	
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelUpComingSingleCustomer()" style="width:80px;">  
				</td>
			</tr>
		</table>
		</form>
	</div> 
	<div id=findProjectDialog>
		<form action="" id="findProjectFormID">
		<label>Project Name: </label>&nbsp; 
		<input type="text" id="customerFilterListID" name="customerFilterListName" style="width: 300px;"/>&nbsp;
		<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
		<input type="button" id ="add" class="quickQuotehoverbutton turbo-blue"  value="Add Customer" onclick="addcustomer()" style=" width:100px;">
		</form>
	</div> 
	<div id="customerAddressDialog" >
		<form action="" id="customerAddressForm">
		<div id="customerItem">
			<table>
			<tr><td>
			  <fieldset style="width:350px; height:223px" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Address</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
						<label>Address:</label>
					</td>
					<td>
						<input type="text" style="width: 261px;" id="locationAddressID1" name="locationAddress1" value="${requestScope.rxAddressDetails.address1}">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
					<label></label>
					</td>
					<td>
						<input type="text" style="width: 261px;" id="locationAddressID2" name="locationAddress2" value="${requestScope.rxAddressDetails.address2}">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>City:</label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="city"  name="locationCity" value="${requestScope.rxAddressDetails.city}">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
							<label>State:</label>
							<input type="text" style="width: 25px;text-transform: uppercase" id="state" name="locationState" value="${requestScope.rxAddressDetails.state}" maxlength="2">
						</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>Zip: </label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="locationZipID" class="validate[custom[number]]" name="locationZip" value="${requestScope.rxAddressDetails.zip}">
						</td>
				</tr>
				<tr id="CustomerCounty">
					<td style=" width : 50px;">
						<label>CountyId:</label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="countyId"  name="CountyId" value="${requestScope.rxAddressDetails.coTaxTerritoryId}">
						</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
						<label>County:</label>
						</td>
						<td>
							<input type="text" style="width: 160px;" id="County" value="${requestScope.coTaxTerritory.county}">
							<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >
							<!-- <label>State:</label>
							<input type="text" style="width: 25px;text-transform: uppercase" id="countyState"  value="${requestScope.coTaxTerritory.state}" maxlength="2"> -->
						</td>
				</tr>
				</table>
				<br>
				<table style="float: right;">
				<tr>
				<td>
				<input type = "checkbox" id=isMailing style="vertical-align: middle;width: 40px;"<c:if test="${requestScope.rxAddressDetails.isMailing}">checked="checked"</c:if>/><label for="MailingAddr">Mailing</label>
				</td>
				<td>
				<input type = "checkbox" id=isShipping style="vertical-align: middle;" <c:if test="${requestScope.rxAddressDetails.isShipTo}">checked="checked"</c:if> />&nbsp;<label for="ShippingAddr">Shipping</label>
				</td>
				</tr>
				</table>
				
			 </fieldset>
			</td>
			<td style="vertical-align:top">
				<fieldset style="width:350px; height: 160px;" class= " ui-widget-content ui-corner-all">
				<legend><label><b>Phone</b></label></legend>
				<table>
				<tr>
					<td style=" width : 50px;">
					<label>Phone1:</label>
					</td>
					<td>
						<input type="text" id="area_Code" name="contact1" style="width:50px" maxlength="3" value="">&nbsp;
						<input type="text" id="exchange_Code" name="contact1" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
						<input type="text" id="subscriber_Number" name="contact1" style="width:100px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
					<label>Phone2:</label>
					</td>
					<td>
						<input type="text" id="area_Code1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp;
						<input type="text" id="exchange_Code1" name="contact2" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
						<input type="text" id="subscriber_Number1" name="contact2" style="width:100px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 50px;">
					<label>Fax:</label>
					</td>
					<td>
						<input type="text" id="area_Code2" name="contact3" style="width:50px" maxlength="3" value="">&nbsp;
						<input type="text" id="exchange_Code2" name="contact3" style="width:50px" maxlength="3" value="">&nbsp; - &nbsp;
						<input type="text" id="subscriber_Number2" name="contact3" style="width:100px" value="">
					</td>
				</tr>
				</table>
				</fieldset>
			</td>
			</tr>
			</table>
			</div>
			<table style="position: relative; top: -33px;left: 260px;" align="center">
		 	<tr>
			  <td align="right" style="padding-right:1px;">
					<input type="button" class="savehoverbutton turbo-blue" value="Save & Close" onclick="savenewcustomerAddress()" style=" width:120px;">
					<input type="button" class="cancelhoverbutton turbo-blue"  value="Cancel" onclick="cancelnewcustomerAddress()" style="width:80px;">  
			  </td>
			</tr>
		</table>	
		</form>
	</div>
	<div id="addProductDialog" align="left">
		<div>
		<table><tr>
				<td style="width: 35px;"><label>Type:</label></td>
				<td><input type="text" id="TypeDetail"  name="TypeDetailName" disabled="disabled">
			<!-- 	<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" >  -->
				<input type="text" id="TypeDetailID" name="TypeDetailID" style="display:none;"></td>
				<td style="width: 35px;"><label>Revision:</label></td>
				<td><input type="text" style="width: 50px;" id="revisionId" name="revision" disabled="disabled">
				</td>
			</tr>
		</table>
		<table id="addquotesList"><div id="addquotespager"></div></table><br>
		<table style="padding-right: 20px;" align="right">
			<tr>
			<td id="QuoteId"><label>HeaderID: </label><input type="text" id="joQuoteHeaderID" style="width:100px; display: none;"></td>
				<td><label>Cost: </label><input type="text" id="totalCostID" name="totalCostName" readonly="readonly" style="width:100px"></td>
				<td><label>Price: </label><input type="text" id="totalPriceID" name="totalPriceName" readonly="readonly" style="width:100px"></td>
			</tr>
		</table>
		</div>
	</div>
	<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="isCategory1ID" value="${requestScope.rxMasterDetails.isCategory1}">
					<input type="text" id="isCategory2ID" value="${requestScope.rxMasterDetails.isCategory2}">
					<input type="text" id="isCategory3ID" value="${requestScope.rxMasterDetails.isCategory3}">
					<input type="text" id="isCategory4ID" value="${requestScope.rxMasterDetails.isCategory4}">
					<input type="text" id="isCategory5ID" value="${requestScope.rxMasterDetails.isCategory5}">
					<input type="text" id="isCustomerID" value="${requestScope.rxMasterDetails.isCustomer}">
					<input type="text" id="isVendorID" value="${requestScope.rxMasterDetails.isVendor}">
					<input type="text" id="isEmployeeID" value="${requestScope.rxMasterDetails.isEmployee}">
					<input type="text" id="countyID" value="${requestScope.coTaxTerritory.county}">
					<input type="text" id="stateID" value="${requestScope.coTaxTerritory.state}">
					<input type="text" id="phone1ID" value="${requestScope.rxMasterDetails.phone1}">
					<input type="text" id="phone2ID" value="${requestScope.rxMasterDetails.phone2}">
					<input type="text" id="faxID" value="${requestScope.rxMasterDetails.fax}">
					<input type="text" id="nameID" value="${requestScope.rxMasterDetails.name}">
					<input type="text" id="address1ID" value="${requestScope.rxAddressDetails.address1}">
					<input type="text" id="address2ID" value="${requestScope.rxAddressDetails.address2}">
					<input type="text" id="isActiveID" value="${requestScope.rxAddressDetails.inActive}">
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/customerContacts.js">	</script>