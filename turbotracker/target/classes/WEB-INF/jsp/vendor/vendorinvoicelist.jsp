<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro-Vendor Invoices</title>
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
			#gview_uninvoicesGrid .ui-jqgrid-titlebar {
						  display:none !important;
						}
		</style> 
		<style>
.ui-jqgrid tr.jqgrow td {
		text-overflow: ellipsis !important;
        white-space: nowrap !important;
    }
.ui-jqgrid tr.jqgrow td { text-overflow: ellipsis !important; }
</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
				<td align="left"><div style="float: left;"><input type="checkbox" id="dateRange" name="dateRange" onclick="enableVendorDate();">
						<label>Date Range:</label><input type="text" name="fromDate" id="fromDate" size="10"><label>Thru</label><input type="text" name="toDate" id="toDate" size="10"></div>
					</td>
					<td align="right">
						<div>
						<span id="accountsPayableImgID"><img src='./../resources/images/csv_text.png' title='Export CSV' align='middle' style='vertical-align: middle; width: 24px; height: 24px; cursor: pointer;' onclick="printAccountsPayable()"/> </span>
					     	<!-- <input type="button" class="cancelhoverbutton turbo-tan" id="uninvoicedButton" value="Uninvoiced" onclick="uninvoicedClick();" style="color: white;"> -->
							<select id="vendorAccountList" onchange="vendorAccountsDetails()">
								<option value="0">-Select-</option>
								<option value="1">Accounts Payable</option>
								<option value="2">Invoices</option>
								<option value="3">Purchasing Summary</option>
								<option value="4">Uninvoiced</option>
							</select>
							<input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addNewVendorInvoice()"/>
							<input type="hidden" id="invStatus" />
						</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div id="invoicesGridID" style="display: block;">
							<table id="invoicesGrid"></table>
							<div id="invoicesGridPager"></div>
						</div>
						<div id="accountsPayableGridID" style="display: none;">
							<table id="accountsPayableGrid"></table>
							<div id="accountsPayableGridPager"></div>
						</div>
					</td>
				</tr>
				
			</table>
			<div id="EnterPODlg" style="width:736px;">
			<form id="poForm">
			<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Info Required</b></label></legend>
						<table>
							<tr>
								<td>
									<label class="fontclass">For Purchase enter the PO then click OK.For bills that have no PO simply click OK without entering a PO #:</label>
								</td>
							</tr>
							<tr>
								<td>
									<input type="text" name="po" id="po">
								</td>
							</tr>
						</table>
						<input type="button" id="saveTermsokButton" name="saveTermsButtonName" value="Cancel" class="savehoverbutton turbo-blue" onclick="cancel()" style="float: right;" />
						<input type="button" id="saveTermscancelButton" name="saveTermsButtonName" value="OK" class="savehoverbutton turbo-blue" onclick="ok()" style="float: right;margin-right:5px;" />
			</fieldset>
			</form>
			</div>
			<div id="InvalidPODlg" style="width:736px;">
			<form id="InvalidPO">
			<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Information</b></label></legend>
						<table>
						<tr><td></td></tr><tr><td></td></tr><tr><td></td></tr>
							<tr>
								<td>
									<label class="fontclass"><span id="InvalidPOMsg"></span></label>
								</td>
							</tr>
							
						</table>						
						<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="OK" class="savehoverbutton turbo-blue" onclick="closeMsg();" style="float: right; margin-top:10px;" />
			</fieldset>
			</form>
			</div>
			
			
			<input type="hidden" name="VendorInvoiceTypeName" id="VendorInvoiceTypeID" value="" />
			<div id="addNewVendorInvoiceDlg" style="width:736px;">
			<form id="addNewVendorInvoiceForm">
				<input type="hidden" id="rxMasterID" name="rxMasterID">
				<input type="hidden" id="veBillIdJob" name="veBillIdJob">
				<input type="hidden" id="coAccountID" name="coAccountID">
				<input type="hidden" id="joMasterID" name="joMasterID">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Details</b></label></legend>
						<div><span style="font-weight:bold;margin-left:400px;font-size: 13px;display:none;" id="paidStatus">
								<a href="javascript:showPaymentDetailsDialog();"><label>Check #<span id="chk_nofmPO" style="margin-left: 5px;margin-right:20px"></span>  Paid Date:<span id="date_paidfmPO" style="margin-left: 5px;"></span></label></a></span>
						<input type="button" id="viStatusButton" name="viStatusButton" value="Open" class="savehoverbutton turbo-blue" style="width: 90px;height: 25px;float: right;" onClick="vendorInvoiceStatusJob();" />
						<input type="hidden" id="viStatusButtontxt" name="viStatusButtontxt">
						</div>
						<form id="vendorInvoiceWithoutPO1">
						<table style="margin: 0 auto;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all" style="height: 133px;">
									<legend><label class="fontclass" style="height: 150px;"><b>Payable To</b></label></legend>
									<table>
										<tr>
											<td><input type="text" style="border-width: 1px;" id="payable" name="payable" placeholder="Minimum 1 characters required to get Vendor list"><span style="color:red;">*</span></td>
											<td></td>
											<td></td>
											<td></td>
											<td></td>
										</tr>
										<tr><td><span id="vendorAddress"></span></td></tr>
										<tr><td><span id="vendorAddress1"></span></td></tr>	
										<tr><td><div id="errorMsg"></div></td></tr>
									</table>
								</fieldset>
								</td>
								<td>
								<fieldset class= " ui-widget-content ui-corner-all" style="width: 605px;height: 133px;">
								<legend><label class="fontclass"><b>Invoice/Bill</b></label></legend>
								<table>
									<tr>
										<td><label class="fontclass">Rec'vd:</label></td><td><input type="text" class="datepicker" id="recDateId" readonly="readonly" name="recDateId" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<td style="width: 14%;"><label class="fontclass">Vendor Inv:</label></td><td><input type="text" id="vendorInvoice" name="vendorInvoice" class="validate[required,maxSize[50]]" style="width: 73%;margin-left: 37px;"></td>
									</tr>
									<tr>
										<td><label class="fontclass">Dated:</label></td><td><input type="text" class="datepicker" id="date" readonly="readonly" name="date" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"><input type="hidden" id ="duedaysfmdr"/></td>
										<td><label class="fontclass">A/P Acct :</label></td><td><select style="width: 76%;margin-left: 37px;" id="apacct" name="apacct" class="dropdownfont fontclass" onchange="changeapacct();">
														<option value="0"> - Select - </option>
															<c:forEach var="accounts" items="${requestScope.coAccount}">
																<option value="${accounts.coAccountId}">
																	<c:out value="${accounts.number}" ></c:out> | <c:out value="${accounts.description}" ></c:out>
																</option>
														</c:forEach>
												</select></td>
												</tr>
												<tr>
										<td><label class="fontclass">Due:</label></td><td><input type="text" class="datepicker" id="due" readonly="readonly" name="due" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<%-- <td><label class="fontclass">Post Date:</label></td><td><input type="checkbox" id="postdate" name="postdate" readonly="readonly" style="margin-left: 23px;">
										<input type="text" class="datepicker" id="postDate1" name="postDate1" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 72%;"></td> --%>
											<td  class="invReason"><label class="fontclass">Reason:</label></td><td  class="invReason"><input type="text" id="invreason" readonly="readonly" style="width: 72%;margin-left: 39px;"></td>
									</tr>
								</table>
								</fieldset>
								</td>
							</tr>
							</table>
							</form>
							<table  style="margin: 0 auto;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>GL Account Distribution</b></label></legend>
									<table>
										<tr>
										<td>
											<div id="jqgridLine">
												<table id="vendorInvoiceGrid"></table>
												<div id="vendorInvoicePager" style="display:none;"></div>
											</div>
											</td>
									</tr>
									</table>
									</fieldset>
								</td>								
							</tr>
							<form id="vendorInvoiceWithoutPO2">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Totals</b></label></legend>
									<table>
										<tr>
											<td><label class="fontclass">Total Distributed:</label></td><td><input type="text" id="totalDist" name="totalDist" value="0.00" style="width: 93%;"></td>
											<td><label class="fontclass">Total :</label></td><td><input type="text" id="total" name="total" value="0.00" style="width: 93%;"></td>
											<td><label class="fontclass">Bal Due:</label></td><td><input type="text" id="balDue" name="balDue" value="0.00" style="width: 100%;"></td>
										</tr>										
									</table>
									</fieldset>
								</td>								
							</tr>
							</form>
					</table>
					<div  align="right">
					<table><tr><td id="vendorinvoiceWoPO" style="color: green;"></td></tr>
					<tr><td>
					<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Save" class="savehoverbutton turbo-blue addinv" onclick="SaveVendorInvoicewithoutPO('Save')"  />
					<input type="button" id="addNewVeInvFmDlgclsbuttonwithoutpo" name="saveTermsButtonName" value="Close" class="savehoverbutton turbo-blue addinv" onclick="SaveVendorInvoicewithoutPO('close')"  /></td></tr>
					</table>
					</div>
				</fieldset>
			</form>
		</div>
		<%-- <div id="addNewVendorInvoiceFromPODlg" style="width:736px; ">
			<form id="addNewVendorInvoiceFromPOForm">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Details</b></label></legend>
						<div><span style="font-weight:bold;margin-left:400px;font-size: 13px; display:none;" id="paidStatus">
								<a href="javascript:showPaymentDetailsDialog();">Check #<span id="chk_nofmPO" style="margin-left: 5px;margin-right:20px"></span>Paid Date:<span id="date_paidfmPO" style="margin-left: 5px;"></span></a></span>
						<input type="button" id="viStatusButtonPO" value="Open" class="savehoverbutton turbo-blue" style="width: 90px;height: 25px;float: right;" onClick="vendorInvoiceStatusForPO();" />
						
						</div>
						<table style = "margin: 0 auto;">
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all" style="height: 104px;margin-top: -81px;">
									<legend><label class="fontclass" style="height: 150px;"><b>Payable To</b></label></legend>
									<table>
										<tr>
											<td><input type="text" style="border-width: 1px;" id="payablePO" name="payable" placeholder="Minimum 1 characters required to get Vendor list"><span style="color:red;">*</span></td>
											<td><input type="hidden" id="rxMasterIDPO" name="rxMasterIDPO"></td>
											<td><input type="hidden" id="rxMasterIDPayablePO" name="rxMasterIDPayablePO"></td>											
											<td><input type="hidden" id="vepoidPO" name="vepoidPO"></td>
											<td><input type="hidden" id="veBillIdPO" name="veBillIdPO"></td>
										</tr>
										<tr><td><span id="vendorAddressPO"></span></td></tr>
										<tr><td><span id="vendorAddress1PO"></span></td></tr>	
									</table>
								</fieldset>
								</td>
								<td>
								<fieldset class= " ui-widget-content ui-corner-all" style="width: 605px;height: 190px;">
								<legend><label class="fontclass"><b>Invoice/Bill</b></label></legend>
								<table>
									<tr>
										<td><label class="fontclass">Rec'vd:</label></td><td><input type="text" class="datepicker" id="recDateIdPO" name="recDateIdPO" readonly="readonly" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<td><label class="fontclass">Vendor Inv:</label></td><td><input type="text" id="vendorInvoicePO" name="vendorInvoicePO" style="width: 77%;margin-left: 15px;" maxlength="20"></td>
									</tr>
									<tr>
										<td><label class="fontclass">Dated:</label></td><td><input type="text" class="datepicker" id="datePO"readonly=" readonly" name="datePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<td width=70><label class="fontclass">A/P Acct :</label></td><td><select style="width: 81%;margin-left: 15px;" id="apacctPO" name="apacctPO" class="dropdownfont fontclass" onchange="changeapacct();">
														<option value="0"> - Select - </option>
															<c:forEach var="accounts" items="${requestScope.coAccount}">
																<option value="${accounts.coAccountId}">
																	<c:out value="${accounts.number}" ></c:out> | <c:out value="${accounts.description}" ></c:out>
																</option>
														</c:forEach>
												</select></td>
												</tr>
												<tr>
										<td><label class="fontclass">Due:</label></td><td><input type="text" class="datepicker" id="duePO" name="duePO" readonly="readonly" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
										<td style="width: 80px;"><label class="fontclass">Post Date:</label></td><td><input type="checkbox" id="postdatePO" name="postdatePO"  style="margin-left: 23px;"><input type="text" class="datepicker" id="postDate1PO" name="postDate1PO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;"></td>
										<td><label class="fontclass">Ship:</label></td><td><input type="text" class="datepicker" id="shipDatePO" readonly="readonly" name="shipDatePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;margin-left: 15px;"></td>
									</tr>
									<tr>
										<td><label class="fontclass">PO#</label></td><td><span id="ponumberPO"></span></td>
										<td><label class="fontclass">Ship:</label></td><td><input type="text" class="datepicker" id="shipDatePO" readonly="readonly" name="shipDatePO" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 77%;margin-left: 37px;"></td>
										<td><label class="fontclass">Pro #</label></td><td><input type="text" id="proPO" name="proPO" style="width: 77%;margin-left: 15px;"></td>
									</tr>
									<tr>
									
										<td style="width: 80px;"><label class="fontclass">Ship Via :</label></td><td><select style="width: 74%;" id="shipviaPO" name="shipviaPO" class="dropdownfont fontclass" onchange="">
														<option value="0"> - Select - </option>
															<c:forEach var="shipViaBean" items="${requestScope.shipVia}">													
																<option value="${shipViaBean.veShipViaId}"><c:out value="${shipViaBean.description}" ></c:out></option>
															</c:forEach>
												</select></td>
									<td class="invReason">
											<label class="fontclass">Reason :</label></td><td class="invReason"><input type="text" id="invwithPoReason" name="invwithPoReason" readonly="readonly" style="width: 77%;margin-left: 15px;"></span>
									</td>
												<!-- <td><label class="fontclass">Pro #</label></td><td><input type="text" id="proPO" name="proPO" style="width: 77%;margin-left: 37px;"></td> -->
									</tr>
									
								</table>
								
											<div id="invreasondialog" style="display: none;">
											<form name="invreasonformname">
											<table>
											<tr><td>Reason:<label style="color: red;">*</label></td><td><textarea id="invreasonttextid" value="" name="invreasonttextid" cols="35" rows="5"></textarea></td></tr>
											<tr><td colspan="2"><label id="inverrordivreason" style="color: red;"></label></td></tr>
											</table>
											</form>
											</div>
											
											<div id="showInvoiceInfoDialog" style="display: none;">
											<form name="showInvoiceInfoDialogName">
											<table width="100%" id="invoiceDetailspopup">
											
											</table>
											</form>
											</div>
											
								
								</fieldset>
								</td>
							</tr>
							<tr>
									<td>
										<fieldset class= " ui-widget-content ui-corner-all" style="height: 66px;margin-top: -83px;">
										<legend><label class="fontclass" style="height: 150px;"><b>Job Name</b></label></legend>
										<table>
									<tr>
										<td><label id="jobName" class="fontclass"></label></td>
									</tr>
									</table>
										</fieldset>
									</td>
							</tr>
							</table>
							<table style="margin: 0 auto;">
							<tr>
								<td>
								<!--	<fieldset class= " ui-widget-content ui-corner-all">
								 	<legend><label class="fontclass"><b>Line Items</b></label></legend> -->
									<table>
										<tr>
										<td>
											<div id="jqgridLine">
												<table id="lineItemGrid"></table>
												<div id="lineItemPager"></div>
											</div>
											</td>
									</tr>
									</table>
									<!--</fieldset> -->
								</td>								
							</tr>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>Totals</b></label></legend>
									<table style="width: 850px">
										<tr>										
											<td><label>Subtotal: </label></td><td  style="right: 10px;position: relative;"><input type="text" style="width: 100px; text-align:right" id="subtotalGeneralId" name="" value="0.00" disabled="disabled"></td>
											<td><label>Freight: </label></td><td  ><input type="text" style="width: 75px; text-align:right" id="freightGeneralId" onclick="frieghtCost()" onchange="frieghtFormat()" onfocus="frieghtCost()" onkeypress="return isNumberKey(this);" value="0.00"></td>
											<td><label>Tax: </label></td><td><fmt:setLocale value="en_US"/><input type="text" style="width: 60px; text-align: right;" id="taxGeneralId" name="taxGeneralName" onclick="taxCost()" onchange="taxFormat()" onfocus="taxCost()" value="0.00"></td>
											<td><label>Total: </label></td><td><input type="text" style="width: 100px; text-align:right" id="totalGeneralId" name="totalGeneralName" disabled="disabled" value="0.00"></td>	
											<td><label>Bal Due: </label></td><td><input type="text" style="width: 100px; text-align:right" id="balDuePO" name="balDuePO" disabled="disabled" value="0.00"></td>
											<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="vePOID" name="vePOName" disabled="disabled" value=""></td>
											<td style="display: none;"><input type="text" style="width: 60px; text-align:right" id="veFactoryID" name="veFactoryName" disabled="disabled" value=""></td>
										</tr>										
									</table>
									</fieldset>
								</td>								
							</tr>
					</table>
					<div style ="margin-top:10px;">
					<input type="button" id="addNewVeInvFmDlgbutton" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue addinv1" onclick="addVendorInvoiceFromPO()" style="float: right;display:inline-block; " />
					<input type="button" id="addNewVeInvFmDlgclsbutton" name="saveTermsButtonName" value="Close" class="savehoverbutton turbo-blue addinv1" onclick="closethepopup()" style="float: right;display:none;" />
					</div>
				</fieldset>
			</form>
		</div>	 --%>
<jsp:include page="../vendorInvoice_Outside.jsp"></jsp:include> 
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
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/vendorinvoicelist.js"></script>
		<script>
		getcoFiscalPerliodDate("recDateId");
		getcoFiscalPerliodDate("recDateIdPO");
		getcoFiscalPerliodDate("date");
		getcoFiscalPerliodDate("datePO");
		getcoFiscalPerliodDate("due");
		getcoFiscalPerliodDate("duePO");
		getcoFiscalPerliodDate("shipDatePO");


		function ResetDetails(){
	    	$('#fromDate').val("");
	    	$('#toDate').val("");
	    	$('#dateRange').prop('checked',false);
	    	 $("#fromDate").prop('disabled',true);
	    	 $("#toDate").prop('disabled',true);
	    	 $("#searchJob").val("");
	    	 $("#invoicesGrid").jqGrid('GridUnload');
	    	 loadInvoicesList("","","");
	    	 loadAccountsPayableList("","","");
	    }

	    
		</script>
		<div id="uninvoicedDlg" >
		<form id="UninvoicedForm">
          <table style="width:979px;margin:0 auto;">
				<tr>
				<td align="left"><div style="float: left;"><input type="checkbox" id="uninvoicedateRange" name="uninvoicedateRange" onclick="enableuninvoiceDate();">
						<label>Date Range:</label><input type="text" name="uninvoicefromDate" id="uninvoicefromDate" size="10" onchange="uninvoicefromfun()"><label>Thru</label><input type="text" name="uninvoicetoDate" id="uninvoicetoDate" size="10" onchange="uninvoicetofun()">
						<input type="button" value="Print" onclick="printuninvoiceList()" />
						</div>
					</td>
				</tr>
			</table>
			<table>
				<tr>
				<td ><div id="jqgrid">
												<table id="uninvoicesGrid"></table>
												<div id="uninvoicesGridPager"></div>
											</div>
					</td>
				</tr>
			</table>
			</form>
			</div>
			
	</body>
</html>