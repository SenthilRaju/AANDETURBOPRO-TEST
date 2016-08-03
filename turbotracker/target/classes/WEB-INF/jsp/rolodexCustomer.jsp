<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.ui-menu-item a:hover { background-color: #637C92; color: #DCEDF9; border-color: #637C92 }
	.ui-autocomplete-w1 { background:url(./../resources/scripts/jquery-autocomplete/shadow.png) no-repeat bottom right; position:absolute; top:0px; left:0px; margin:6px 0 0 6px;  _background:none; _margin:1px 0 0 0; }
	.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:100px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:350px;  _margin:0; _overflow-x:hidden; }
	.ui-autocomplete .selected { background:#F0F0F0; }
	.ui-autocomplete div { padding:2px 5px; white-space:nowrap; overflow:hidden; }
	.ui-autocomplete strong { font-weight:normal; color:#3399FF; }
	.ui-autocomplete-loading { background: white url('./../resources/scripts/jquery-autocomplete/loading3.gif') right center no-repeat; }
	input[type=checkbox] {margin-bottom: 0px;vertical-align: bottom;}
	input[type=radio] {margin-bottom: 0;vertical-align: bottom;}
	.ui-autocomplete {cursor: default;height: 150px;overflow-y: scroll;overflow: none;}
	.ui-menu-item a:hover { background-color: #637C92; color: #DCEDF9; border-color: #637C92 }
</style>
<div id="customertab">
  <form action="" id="customerRolodexForm">
	<table>
		<tr height="5px;"></tr>
		<tr>
			<td width="115px;" style="padding-bottom: 10px;"><label>Customer Name: </label></td>
			<td width="" style="padding-bottom: 10px;">
				<input type="text" name="cuStomerName" size="32" id="customerNameHeader"/>
			</td>
			<td align="right" style="">
				<input type="button" class="savehoverbutton turbo-blue" value="Save"id="mainsave" onclick="CustomerForm_save()" style="">
			</td>
		</tr>
		<tr>
			<td colspan="3"><hr style="width:1100px; color:#C2A472;"></td>
		</tr>
	</table>
	<table style="width: 1100px">
		<tr>
			<td style="vertical-align: top; padding-left: 4px;">
				<fieldset style="width: 450px" class=" ui-widget-content ui-corner-all">
					<legend><label><b>Customer Account #</b></label></legend>					
					<table>
						<tr><td><input type="text" name="custAccountNumber" value="${requestScope.customerMasterObj.accountNumber}" id="customer_accountName"></td>
							<td><input type="checkbox" name="custFinanceCharge" id="financeCharge" value="1" onclick="financeValue()"><label>Finance Charge</label></td>
							<td><input type="checkbox" name="custFinStatement" id="statementId" value="1" onclick="statementValue()"><label>Statement</label></td>
						</tr>
					</table>
				</fieldset>
			</td>
			
			<td style="vertical-align: top; padding-left: 4px;">
				<fieldset style="width: 483px;margin-left:-131px; overflow-y: auto;height: 93px;" class=" ui-widget-content ui-corner-all">
					<legend><label><b>Tax Territories </b></label></legend>					
					<table id="taxterritorytable" style="overflow-y:scroll; height:50px;">
					<c:set var="rownumber" scope="session" value="0"/>
					<c:forEach var="ShippingList" items="${requestScope.ShippingList}">
						<tr><input type="hidden" id="rxaddressID" name="rxaddressID" value="<c:out value='${ShippingList.rxAddressID}' />">
							<td><input type="text" name="custFinanceaddr" id="custFinanceaddr" value="<c:out value='${ShippingList.label}' />" readonly="readonly"></td>
							<td>
							<select name="custFinancetaxterr" id="custFinancetaxterr" style="width: 180px;" onchange="gettaxterritory('<c:out value="${rownumber}" />')">
							<option value="-1">--Select--</option>
							<c:forEach var="AutoCompleteBean" items="${requestScope.AutoCompleteBean}">
													<option value="${AutoCompleteBean.id}"  <c:if test='${AutoCompleteBean.id == ShippingList.id}'>selected="selected" </c:if> >
														<c:out value="${AutoCompleteBean.label}" ></c:out>
													</option>
							</c:forEach>
							
							</select>
							</td>
							<td><label id="taxterritoryper<c:out value="${rownumber}" />" style="color: red;"><c:out value='${ShippingList.value}' ></c:out></label></td>
							<c:set var="rownumber" scope="session" value="${rownumber+1}"/>
						</tr>
					</c:forEach>
					</table>
				</fieldset>
			</td>
<!-- 		<td align="right" style="">
				<input type="button" class="savehoverbutton turbo-blue" value="Save"id="mainsave" onclick="CustomerForm_save()" style="width:80px">
			</td> -->
		</tr>
	</table>
	<table>
		<tr>
			<td style="vertical-align: top">
				<table>
					<tr>
						<td style="padding-top: 7px;">
							<fieldset style="width: 450px;" class=" ui-widget-content ui-corner-all">
								<table>
									<tr>
										<td><label><b>Payment terms: </b></label></td>
										<td><input type="text" id="customer_paymentTerms" value="${requestScope.customerTabFormDataBean.customerTerms}" name="paymentTerms" style="width: 200px;" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="hidden" id="paymentId" value="${requestScope.customerMasterObj.cuTermsId}" name="cuPaymentTermsId" style="width: 30px;"></td>
									</tr>
									<tr>
										<td><label><b>Customer Type: </b></label></td>
										<td>
											<select id="customer_customerType" name="customerType">
												<option value="-1"> - Select - </option>
												<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
													<option value="${cuTypeBean.cuMasterTypeId}">
														<c:out value="${cuTypeBean.code}" ></c:out>
													</option>
												</c:forEach>
											</select>
										</td>
									</tr>
									<tr>
										<td><label><b>Default Warehouse: </b></label></td>
										<td>
											<select id="prWarehouseID" name="prWarehouse" style="width: 205px;">
												<option value="-1"> - Select - </option>
												<c:forEach var="wareHouseBean" items="${requestScope.divisionsSearch}">
													<option value="${wareHouseBean.prWarehouseId}">
														<c:out value="${wareHouseBean.searchName}" ></c:out>
													</option>
												</c:forEach>
												<%-- <c:forEach var="coDivision" items="${requestScope.divisionsSearch}">
													<option value="${coDivision.coDivisionId}">
														<c:out value="${coDivision.description}" ></c:out>
													</option>
												</c:forEach> --%>
											</select>
										</td>
										<!-- ****** This commented code is needed in future, When we remove the hard coded ware house list ****** -->
										
										<!-- <td><input type="text" id="customer_warehouse" name="defaultWarehouse" style="width: 200px;"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
										<td><input type="text" id="warehouseId" value="" name="empWarehouse" style="width: 30px;"></td> -->
									</tr>
									<tr>
										<td><label><b>Tax Exempt #: </b></label>	</td>
										<td><input type="text" name="taxExemptNo" id="customer_exempt" style="width: 200px" value="${requestScope.customerMasterObj.taxExemptNumber}"></td>
									</tr>
									<tr>
										<td><input type="checkbox" name="isPORequired" id="PORequired" value="1" onclick="POrequired()"><label>PO Required</label></td>
									</tr>
									<tr><td style="height: 10px;"></td></tr>
								</table>
							</fieldset>
						</td>
						<td style="vertical-align: top">
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 480px;">
								<legend>
									<label><b>Employee's Assigned</b></label>
								</legend>
								<table>
									<tr>
										<td><label id="CustomerCategory1label" style="display: none;">Sales Rep: </label></td>
										<td>&nbsp;<input type="text" id="customer_salesRepsList" name="assignedSalesRep" style="width: 290px;display: none;" value="${requestScope.customerTabFormDataBean.assignedSalesRep}" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="CustomerCategory1image" style="display: none;"></td>
										<td><input type="hidden" id="salesRepId" name="assignedSalesRepId" value="${requestScope.customerMasterObj.cuAssignmentId0}" style="width: 30px;"></td>
									</tr>
									<tr>
										<td><label id="CustomerCategory2label" style="display: none;">CSR: </label></td>
										<td>&nbsp;<input type="text" id="customer_CSRList" name="assignedCSR" style="width: 290px;display: none;" value="${requestScope.customerTabFormDataBean.assignedCSRs}" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="CustomerCategory2image" style="display: none;"></td>
										<td><input type="hidden" id="CSRId" value="${requestScope.customerMasterObj.cuAssignmentId1}" name="assignedCSRId" style="width: 30px;"></td>
									</tr>
									<tr>
										<td><label id="CustomerCategory3label" style="display: none;">Sales Manager: </label></td>
										<td>&nbsp;<input type="text" id="customer_SalesMgrList" name="assignedSalesMgr" style="width: 290px;display: none;" value="${requestScope.customerTabFormDataBean.assignedSalesMGRs}" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="CustomerCategory3image" style="display: none;"></td>
										<td><input type="hidden" id="salesMgrId" name="assignedSalesMgrId" value="${requestScope.customerMasterObj.cuAssignmentId2}" style="width: 30px;"></td>
									</tr>
									<tr>
										<td><label id="CustomerCategory4label" style="display: none;">Engineer: </label></td>
										<td>&nbsp;<input type="text" id="customer_EngineersList" name="assignedEngineer" style="width: 290px;display: none;" value="${requestScope.customerTabFormDataBean.assignedEngineers}" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="CustomerCategory4image" style="display: none;"></td>
										<td><input type="hidden" id="engineerId" name="assignedEngineerId" value="${requestScope.customerMasterObj.cuAssignmentId3}" style="width: 30px;"></td>
									</tr>
									<tr>
										<td><label id="CustomerCategory5label" style="display: none;">Project Manager: </label></td>
										<td>&nbsp;<input type="text" id="customer_PrjMgrList" name="assignedPrjMgr" style="width: 290px;display: none;" value="${requestScope.customerTabFormDataBean.assignedProjManagers}" title="" placeholder="Minimum 2 characters required"></td>
										<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="CustomerCategory5image" style="display: none;"></td>
										<td><input type="hidden" id="prjMgrId" name="assignedPrjMgrId" value="${requestScope.customerMasterObj.cuAssignmentId4}" style="width: 30px;"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<table style="width: 910px;">
		<tr>
			<td style="vertical-align: top">
				<fieldset class="ui-widget-content ui-corner-all" style="width: 290px;">
					<legend>
						<label><b>Customer Overall A/R</b></label>
					</legend>
					<table>
						<tr><td><label>Current Due:</label></td><td><label id ="currentDue">$0.00</label></td></tr>
						<tr><td><label>30 Days:</label></td><td><label id ="thirtyDays">$0.00</label></td></tr>
						<tr><td><label>60 Days:</label></td><td><label id ="sixtyDays">$0.00</label></td></tr>
						<tr><td><label>90 Days:</label></td><td><label id ="nintyDays">$0.00</label></td></tr>
						<tr><td><label>Total Due:</label></td><td><label id ="totalDue">$0.00</label></td></tr>
						<tr><td style="width: 160px;"><label>Avg. Days to Pay Invoice:</label></td><td>0</td></tr>
						<tr><td><label>Since</label>&nbsp;&nbsp;&nbsp;<input type="checkbox" id="since" value="" onclick="sinceShowHide()"></td>
						<td><input type="text" class="datepicker" name="sinceAppDate" id="sinceDate" value="" style="width: 100px;"></td></tr>
					</table>
				</fieldset>
			</td>
			<td width="0px;"></td>
			<td style="vertical-align: top;width: 300px;">
				<table style="width: 100%">
					<tr>
						<td>
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 250px;">
								<legend><label><b>Credit</b></label></legend>
								<table>
									<tr>
										<td>
											<label style="">Credit App:</label>
											<input type="checkbox" name="isCreditApp" style="margin-bottom: 0; margin-top: 2px;vertical-align: middle;" id="creditApp" onclick="creditAppShowHide()" value="1">
											<input type="text" class="datepicker" name="creditAppDate" id="creditDate" style="width: 100px;">
										</td>
									</tr>
									<tr>
										<td>
											<label>Limit</label>&nbsp;
											<input type="text" style="width: 75px" id="customerCreditLimit" value="">&nbsp;
											<input type="checkbox" name="isCreditHold" id="hold" onclick="holdShowHide()" style="vertical-align: middle;margin-bottom: 3px;" value="1">
											<label>Hold</label>
										</td>
									</tr>
									<tr>
										<td id="creditHold">
											<label>Override Credit Hold For Today:</label>
											<input type="checkbox" name="creditHoldOverride" id="creditholdId" value="1" onclick="">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
					<tr>
						<td>
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 250px;">
								<legend><label><b>Sales</b></label></legend>
								<table>
									<tr><td><label>YTD:</label></td><td><label id="Year_To_Date">$0.00</label></td></tr>
									<tr><td><label id="thisYear">:</label></td><td><label id="This_Year">$0.00</label></td></tr>
									<tr><td><label>Last Sale:</label></td><td><label id="Last_Sale">$0.00</label></td></tr>
									<tr><td></td> <td><label id="Last_Sale_Date"></label></td></tr>
								</table>
							</fieldset>
						</td> 
					</tr>
				</table>
			</td>
			<td style="vertical-align: middle;">
				<table style="width: 210px">
				<input type="hidden" name="quoteMethod" value="0"/>
					<!-- <tr>
						<td height="">
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 220px;">
								<legend><label><b>Quote Method</b></label></legend>
								<table>
									<tr>
										<td>
											<input type="radio" id="quoteMethodFaxId" name="quoteMethod" value="1" onclick="quoteMethodShowHide()"><label>Fax</label>&nbsp;
											<input type="radio" id="quoteMethodEmailId" name="quoteMethod" value="2" onclick="quoteMethodShowEmailHide()"><label>Email</label>&nbsp;
											<input type="radio" id="quoteMethodMailId" name="quoteMethod" value="0" checked="checked" onclick="quoteMethodShowMailHide()"><label>Mail</label>
										</td>
									</tr>
									<tr>
										<td>
											<label id="quoteMethodFaxLable">Fax:</label>
											<label id="quoteMethodEmailLable">Email:</label>
												<input type="text" name="qm_text" style="width: 140px;" id="quoteMethodText">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr> -->
					<tr height="">
						<td style="vertical-align: top">
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 220px; height: 70px;">
								<legend><label><b>Invoicing Method</b></label></legend>
								<table>
									<tr>
										<td>
											<!-- <input type="radio" id="invoiceMethodFaxId" name="invoiceMethod" value="1" onclick="invoiceMethodShowHide()"><label>Fax</label>&nbsp; -->
											<input type="radio" id="invoiceMethodEmailId" name="invoiceMethod" value="2"  onclick="invoiceMethodShowEmailHide()"><label>Email</label>&nbsp;
											<input type="radio" id="invoiceMethodMailId" name="invoiceMethod" value="0" checked="checked" onclick="invoiceMethodHide()"><label>Mail</label>
										</td>
									</tr>
									<tr>
										<td>
											<label id="invoiceMethodFaxLable">Fax:</label>
											<label id="invoiceMethodEmailLable">Email:</label>
												<input type="text" name="im_text" style="width: 140px;" id="invoiceMethodText">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
						
						<td style="vertical-align: top">
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 220px; height: 70px;">
								<legend><label><b>Statement Method</b></label></legend>
								<table>
									<tr>
										<td>
											<!-- <input type="radio" id="statementMethodFaxId" name="statementMethod" value="1" onclick="statementMethodShowHide()"><label>Fax</label>&nbsp; -->
											<input type="radio" id="statementMethodEmailId" name="statementMethod" value="2" onclick="statementMethodShowEmailHide()"><label>Email</label>&nbsp;
											<input type="radio" id="statementMethodMailId" name="statementMethod" value="0" checked="checked" onclick="statementMethodHide()"><label>Mail</label>
										</td>
									</tr>
									<tr>
										<td>
											<label id="statementMethodFaxLable">Fax:</label>
											<label id="statementMethodEmailLable">Email:</label>
												<input type="text" name="sm_text" style="width: 140px;" id="statementMethodText">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				 <!-- <tr height="">  
						<td style="vertical-align: top">
							<fieldset class=" ui-widget-content ui-corner-all" style="width: 220px;">
								<legend><label><b>Statement Method</b></label></legend>
								<table>
									<tr>
										<td>
											<input type="radio" id="statementMethodFaxId" name="statementMethod" value="1" onclick="statementMethodShowHide()"><label>Fax</label>&nbsp;
											<input type="radio" id="statementMethodEmailId" name="statementMethod" value="2" onclick="statementMethodShowEmailHide()"><label>Email</label>&nbsp;
											<input type="radio" id="statementMethodMailId" name="statementMethod" value="0" checked="checked" onclick="statementMethodHide()"><label>Mail</label>
										</td>
									</tr>
									<tr>
										<td>
											<label id="statementMethodFaxLable">Fax:</label>
											<label id="statementMethodEmailLable">Email:</label>
												<input type="text" name="sm_text" style="width: 140px;" id="statementMethodText">
										</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>   -->
				</table>
			</td>
		</tr>
	</table>
	<div id="EmployeeDetails" style="padding-left: 0px;">
		<table style="padding-left: 0px" id="jobsGridDetails" class="scroll"></table>
		<div id="jobsGridDetailspager" class="scroll" style="text-align: right;"></div>
	</div>
	</form>
</div>
<script type="text/javascript">
    $('#customerRolodexForm')[0].reset();
	var date = new Date();
	$("#thisYear").text(date.getFullYear()-1 + ": ");
	jQuery(document).ready(function() {
		var customerName = $('#customerName').text();
		$("input#customerNameHeader").val($.trim(customerName));
		loadCustomerJobs();
		$('#creditDate').hide();
		$('#creditHold').hide();
		$('#sinceDate').hide();
		$('#quoteMethodText').hide();
		$('#quoteMethodFaxLable').hide();
		$('#quoteMethodEmailLable').hide();
		$('#invoiceMethodText').hide();
		$('#invoiceMethodFaxLable').hide();
		$('#invoiceMethodEmailLable').hide();
		$('#statementMethodText').hide();
		$('#statementMethodFaxLable').hide();
		$('#statementMethodEmailLable').hide();
		$(".datepicker").datepicker();
		$("#currentDue").text(formatCurrency("${requestScope.currentDue}"));
		$("#thirtyDays").text(formatCurrency("${requestScope.thirtyDays}"));
		$("#sixtyDays").text(formatCurrency("${requestScope.sixtyDays}"));
		$("#nintyDays").text(formatCurrency("${requestScope.nintyDays}"));
		var total = Number("${requestScope.currentDue}")+Number("${requestScope.thirtyDays}")+Number("${requestScope.sixtyDays}")+Number("${requestScope.nintyDays}");
		$("#totalDue").text(formatCurrency(total));
		parseModelMap();
		loadcustomercategories();
	});

	
	
	/*
	Updated by:Velmurugan
	Updated on:9-1-2014
	Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
	*/
	function loadcustomercategories(){
		var Category1labDesc=$("#CustomerCategory1hidden").val();
		var Category2labDesc=$("#CustomerCategory2hidden").val();
		var Category3labDesc=$("#CustomerCategory3hidden").val();
		var Category4labDesc=$("#CustomerCategory4hidden").val();
		var Category5labDesc=$("#CustomerCategory5hidden").val();
		
		//CustomerCategory1label
		$("#CustomerCategory1label").text(Category1labDesc);
		$("#CustomerCategory2label").text(Category2labDesc);
		$("#CustomerCategory3label").text(Category3labDesc);
		$("#CustomerCategory4label").text(Category4labDesc);
		$("#CustomerCategory5label").text(Category5labDesc);
		
		//customer_salesRepsList customer_CSRList customer_SalesMgrList customer_EngineersList customer_PrjMgrList
		//CustomerCategory1image
		if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
			$("#CustomerCategory1label").css({ display: "none" });
			$("#customer_salesRepsList").css({ display: "none" });
			$("#CustomerCategory1image").css({ display: "none" });
		}else{
			$("#CustomerCategory1label").css({ display: "inline-block" });
			$("#customer_salesRepsList").css({ display: "inline-block" });
			$("#CustomerCategory1image").css({ display: "inline-block" });
		}
		if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
			$("#CustomerCategory2label").css({ display: "none" });
			$("#customer_CSRList").css({ display: "none" });
			$("#CustomerCategory2image").css({ display: "none" });
		}else{
			$("#CustomerCategory2label").css({ display: "inline-block" });
			$("#customer_CSRList").css({ display: "inline-block" });
			$("#CustomerCategory2image").css({ display: "inline-block" });
		}
		if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
			$("#CustomerCategory3label").css({ display: "none" });
			$("#customer_SalesMgrList").css({ display: "none" });
			$("#CustomerCategory3image").css({ display: "none" });
		}else{
			$("#CustomerCategory3label").css({ display: "inline-block" });
			$("#customer_SalesMgrList").css({ display: "inline-block" });
			$("#CustomerCategory3image").css({ display: "inline-block" });
		}
		if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
			$("#CustomerCategory4label").css({ display: "none" });
			$("#customer_EngineersList").css({ display: "none" });
			$("#CustomerCategory4image").css({ display: "none" });
		}else{
			$("#CustomerCategory4label").css({ display: "inline-block" });
			$("#customer_EngineersList").css({ display: "inline-block" });
			$("#CustomerCategory4image").css({ display: "inline-block" });
		}
		if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
			$("#CustomerCategory5label").css({ display: "none" });
			$("#customer_PrjMgrList").css({ display: "none" });
			$("#CustomerCategory5image").css({ display: "none" });
		}else{
			$("#CustomerCategory5label").css({ display: "inline-block" });
			$("#customer_PrjMgrList").css({ display: "inline-block" });
			$("#CustomerCategory5image").css({ display: "inline-block" });
		}
		
		
	}
 
	function parseModelMap(){
		var creditHold = "${requestScope.customerMasterObj.creditHoldOverride}";
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1; 
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd;} 
		if(mm<10){mm='0'+mm;} 
		today = yyyy+ '-' + mm + '-' + dd;
		var aCreditDate = creditHold.split(" ");
		var creditday = aCreditDate[0];
		if(creditday === today){
			$("#creditholdId").prop("checked", true);
		}
		
		var prWarehouse = "${requestScope.customerMasterObj.prWarehouseId}";
		/*This Session Variable added in user login controller 
		  By Velmurugan
		  
		 */ 
		if(prWarehouse==-1||prWarehouse==""||prWarehouse==undefined){
			prWarehouse="${sessionScope.sysUserDefault.warehouseID}";
			}
		
		$("#prWarehouseID option[value=" + prWarehouse + "]").attr("selected", true);

		var cuType = "${requestScope.customerMasterObj.cuMasterTypeId}";
		$("#customer_customerType option[value=" + cuType + "]").attr("selected", true);
		
		var creditLimit = formatCurrency("${requestScope.customerMasterObj.creditLimit}");
		creditLimit = creditLimit.replace("$", "");
		$("#customerCreditLimit").val(creditLimit);
		
		var creditAppDate = "${requestScope.customerMasterObj.creditAppDate}";
		if(creditAppDate !== "") {
			creditAppDate = customFormatDate(creditAppDate);
			$("#creditDate").val(creditAppDate);
			$("#creditApp").prop("checked", true);
			$('#creditDate').show();
		}
		
		var acreditHold = "${requestScope.customerMasterObj.creditHold}";
		if (acreditHold === "true") {$("#hold").prop("checked", true);$('#creditHold').show();}

		var poRequired = "${requestScope.customerMasterObj.porequired}";
		if (poRequired === "true") {$("#PORequired").prop("checked", true);}

		var financeCharge = "${requestScope.customerMasterObj.finCharge}";
		if (financeCharge === "1") {$("#financeCharge").prop("checked", true);}

		var statement = "${requestScope.customerMasterObj.statements}";
		if (statement === "1") {$("#statementId").prop("checked", true);}

		var QuoteMethod = "${requestScope.customerMasterObj.quoteMethod}";
		if (QuoteMethod === "1") {
			$('#quoteMethodFaxLable').show();
			$('#quoteMethodText').show();
			$('#quoteMethodText').val("${requestScope.customerMasterObj.qmText}");
			$("input:radio[name='quoteMethod']:nth(0)").attr("checked", true);
		} else if (QuoteMethod === "2") {
			$('#quoteMethodEmailLable').show();
			$('#quoteMethodText').show();
			$('#quoteMethodText').val("${requestScope.customerMasterObj.qmText}");
			$("input:radio[name='quoteMethod']:nth(1)").attr("checked", true);
		}

		var invoiceMethod = "${requestScope.customerMasterObj.invoiceMethod}";
		if (invoiceMethod === "1") {
			$('#invoiceMethodFaxLable').show();
			$('#invoiceMethodText').show();
			$('#invoiceMethodText').val("${requestScope.customerMasterObj.imText}");
			$("input:radio[name='invoiceMethod']:nth(0)").attr("checked", true);
		} else if (invoiceMethod === "2") {
			$('#invoiceMethodEmailLable').show();
			$('#invoiceMethodText').show();
			$('#invoiceMethodText').val("${requestScope.customerMasterObj.imText}");
			$("input:radio[name='invoiceMethod']:nth(0)").attr("checked", true);
		}

		var statementMethod = "${requestScope.customerMasterObj.statementMethod}";
		if (statementMethod === "1") {
			$('#statementMethodFaxLable').show();
			$('#statementMethodText').show();
			$('#statementMethodText').val("${requestScope.customerMasterObj.smText}");
			$("input:radio[name='statementMethod']:nth(0)").attr("checked", true);
		} else if (statementMethod === "2") {
			$('#statementMethodEmailLable').show();
			$('#statementMethodText').show();
			$('#statementMethodText').val("${requestScope.customerMasterObj.smText}");
			$("input:radio[name='statementMethod']:nth(0)").attr("checked", true);
		}
		   
		 $("#Year_To_Date").text(formatCurrency("${requestScope.yearToDate}"));
		 $("#This_Year").text(formatCurrency("${requestScope.lastYearSale}"));
		 $("#Last_Sale").text(formatCurrency("${requestScope.aCuinvoice.subtotal}"));
		 $("#Last_Sale_Date").text(customFormatDate("${requestScope.aCuinvoice.invoiceDate}"));
		 
	}
	

	function loadCustomerJobs() {
		var rolodexNumber = getUrlVars()["rolodexNumber"];
		var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
		var grid = $('#jobsGridDetails');
		$("#jobsGridDetails").jqGrid({
				url:'customerList/customerJobs',
				datatype:'JSON',
				postData:{'rolodexNumber':rolodexNumber },
				mtype:'GET',
				colNames:[ 'Job #', 'Name', 'Cost', 'Sale Price'],
				colModel:[
					{name:'jobNo',index:'jobNo',align:'center',width:40, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';},editable:true,hidden:false,editoptions:{},	editrules:{}},
					{name:'jobName',index:'jobName',align:'left',width:90,hidden:false,editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';},editoptions:{},editrules:{}},
					{name:'quoteAmount',index:'quoteAmount',align:'right',	width:50,editable:true,formatter:currencyFormatter, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';},	editoptions:{},	editrules:{}},
					{name:'contractAmount',index:'contractAmount',	align:'right',width:50,editable:true,formatter:currencyFormatter,editoptions:{},editrules:{edithidden:true,required:false}}
				],
				recordtext:'',rowList:[],pgtext:null,viewrecords:false,
				sortname:' ',sortorder:"asc",imgpath:'themes/basic/images',
				caption:'Jobs',	height:140,width:1100,rownumbers:true,altRows: true,altclass:'myAltRowClass',
				loadComplete:function(data) {	},
				jsonReader:{
					root: "rows", page: "page", total: "total", records: "records", repeatitems: false,
					cell: "cell", id: "id", userdata: "userdata"
				},
				loadError:function(jqXHR, textStatus, errorThrown) {	}
			});
		emptyMsgDiv.insertAfter(grid.parent());
		return true;
	}
	
	function currencyFormatter(cellValue, options, rowObject) {
		return formatCurrency(cellValue);
	}

	function creditAppShowHide(){
		if($('#creditApp').is(':checked')) { $('#creditDate').show();}
		else {$('#creditDate').hide();}		
	}

	var holdValue;
	function holdShowHide(){
console.log("---?>")
		
//	var checkpermission=	getGrantpermissionprivilage(accesspage,groupid)

		
		if($('#hold').is(':checked')) {
			/* var aSysAdmin = "${sessionScope.user.systemAdministrator}"; 
			if(aSysAdmin !== '1'){
				var info = "You are not authorized to change the credit status to Hold.  Please contact your System Administrator.";
				var DialogBox = jQuery(document.createElement('div'));
				jQuery(DialogBox).html('<span><b style="color:green;">'+info+'</b></span>');
				jQuery(DialogBox).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); $("#hold").prop("checked", false); }}]}).dialog("open");
				return false;
			} */
			$('#creditHold').show();
		}
		else {	
			$('#creditHold').hide();
		}
	}

	function sinceShowHide(){
		if($('#since').is(':checked')) { $('#sinceDate').show();}
		else {$('#sinceDate').hide();}
	}

	var financevalues = 0;
	function financeValue(){
		if($('#financeCharge').is(':checked')){financevalues = 1;}
		else{financevalues = 0;}
	}

	var statement = 0;
	function statementValue(){
		if($('#statementId').is(':checked')){statement = 1;}
		else{statement = 0;}
	}

	var POrequiredValue = 0;
	function POrequired(){
		if($('#PORequired').is(':checked')){POrequiredValue = 1;}
		else{POrequiredValue =0;}
	}

	function CustomerForm_save(){
		if(!$('#customerRolodexForm').validationEngine('validate')) {
			return false;
		}
		if($("#customer_salesRepsList").val() === "") {$("#salesRepId").val("");}
		if($("#customer_CSRList").val() === "") {$("#CSRId").val("");}
		var assignedSalesMgr = $("#customer_SalesMgrList").val();
		if(assignedSalesMgr === ""){$("#salesMgrId").val("");}
		if($("#customer_EngineersList").val() === "") {$("#engineerId").val("");}
		if($("#customer_PrjMgrList").val() === "") {$("#prjMgrId").val("");}
		var customerSerialize = $("#customerRolodexForm").serialize();
		var rxMasterId = getUrlVars()["rolodexNumber"];
		/*var customerdetails = "rxMasterId=" + rxMasterId + "&financeChange=" + financevalues + "&statement=" + statement + 
								"&POrequired=" + POrequiredValue + "&hold=" + holdValue + "&" + customerdetails;*/
		var creditLimit = $("#customerCreditLimit").val().replace(",","");
		var customerdetails = "rxMasterId=" + rxMasterId + "&" + customerSerialize + "&creditLimit=" + creditLimit; 
		 $.ajax({
			type: "POST",
			url: "./customerList/addRolodexCustomerDetails",
			data: customerdetails,
			success: function(data) {
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span style="color:Green;"><b>Customer details Updated</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			},
			error: function(jqXHR, textStatus, errorThrown)	{
				var errorText = $(jqXHR.responseText).find('u').html();
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span style="color:red;"><b>'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]})
									.dialog("open");
				return false;
			}
		}); 
		return true;
	}

	function quoteMethodShowHide(){
		if($('#quoteMethodFaxId').is(':checked')) { 
			$('#quoteMethodText').show();
			$('#quoteMethodFaxLable').show();
			$('#quoteMethodEmailLable').hide();
		}		
	}

	function quoteMethodShowEmailHide(){
		if($('#quoteMethodEmailId').is(':checked')) { 
			$('#quoteMethodText').show();
			$('#quoteMethodFaxLable').hide();
			$('#quoteMethodEmailLable').show();
		}		
	}

	function quoteMethodShowMailHide(){
		if($('#quoteMethodMailId').is(':checked')) { 
			$('#quoteMethodText').hide();
			$('#quoteMethodFaxLable').hide();
			$('#quoteMethodEmailLable').hide();
		}		
	}

	function invoiceMethodShowHide(){
		if($('#invoiceMethodFaxId').is(':checked')) { 
			$('#invoiceMethodText').show();
			$('#invoiceMethodFaxLable').show();
			$('#invoiceMethodEmailLable').hide();
		}		
	}

	function invoiceMethodShowEmailHide(){
		if($('#invoiceMethodEmailId').is(':checked')) { 
			$('#invoiceMethodText').show();
			$('#invoiceMethodFaxLable').hide();
			$('#invoiceMethodEmailLable').show();
		}		
	}

	function invoiceMethodHide(){
		if($('#invoiceMethodMailId').is(':checked')) { 
			$('#invoiceMethodText').hide();
			$('#invoiceMethodFaxLable').hide();
			$('#invoiceMethodEmailLable').hide();
		}			
	}

	function statementMethodShowHide(){
		if($('#statementMethodFaxId').is(':checked')) { 
			$('#statementMethodText').show();
			$('#statementMethodFaxLable').show();
			$('#statementMethodEmailLable').hide();
		}		
	}

	function statementMethodShowEmailHide(){
		if($('#statementMethodEmailId').is(':checked')) { 
			$('#statementMethodText').show();
			$('#statementMethodFaxLable').hide();
			$('#statementMethodEmailLable').show();
		}		
	}

	function statementMethodHide(){
		if($('#statementMethodMailId').is(':checked')) { 
			$('#statementMethodText').hide();
			$('#statementMethodFaxLable').hide();
			$('#statementMethodEmailLable').hide();
		}
	}

	function gettaxterritory(value){
		//alert(value);
		var coTaxTerritoryID=document.getElementsByName("custFinancetaxterr")[value].value;
		if(coTaxTerritoryID!=-1){
		 $.ajax({
			type: "POST",
			url: "./customerList/gettaxratedependsoncotax",
			data : {"coTaxTerritoryID": coTaxTerritoryID},
			success: function(data) {
				
				var percentage="0.00 %"
				if(data!=null){
				 percentage=parseFloat(data.taxRate).toFixed(2)+" %";
				//alert(document.getElementsByName("custFinancetaxterr")[0].value);
				}
				var id="taxterritoryper"+value;
				document.getElementById(id).innerHTML=percentage;
				},
			error: function(jqXHR, textStatus, errorThrown)	{
				
			}
		}); 
		}else{

			var id="taxterritoryper"+value;
			document.getElementById(id).innerHTML="";
			}
		
	}	

	
	$(function() { var cache = {}, lastXhr;
	$( "#customer_salesRepsList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#salesRepId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });
	
	$(function() { var cache = {}, lastXhr;
		$( "#customer_CSRList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#CSRId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	$(function() { var cache = {}, lastXhr;
		$( "#customer_SalesMgrList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#salesMgrId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	$(function() { var cache = {}, lastXhr;
		$( "#customer_EngineersList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#engineerId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	$(function() { var cache = {}, lastXhr;
		$( "#customer_PrjMgrList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#prjMgrId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	$(function() { var cache = {}, lastXhr;
		$( "#customer_paymentTerms" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#paymentId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/paymentType", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	$(function() { var cache = {}, lastXhr;
		$( "#customer_customerType" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#customerId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/customerType", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	/** #######This commented code is needed in future, When we remove the hard coded warehouse list###### ***/
	  
	/*  $(function() { var cache = {}, lastXhr;
		$( "#customer_warehouse" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#warehouseId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/warehouseType", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			} }); });
	  */
	
</script>