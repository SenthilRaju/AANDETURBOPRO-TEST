<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Pay Bills</title>
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
</head>
<body>
	<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
	<br><br>
	<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto; background-color: #FAFAFA;height: 750px;">
			<ul>
				<li id="billFormDetails"><a href="">Vendor Pay Bills</a></li>
			</ul>
		<br><br>
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
						<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Cancel" class="savehoverbutton turbo-blue" onclick="cancel()" style="float: right;" />
						<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="OK" class="savehoverbutton turbo-blue" onclick="ok()" style="float: right;margin-right:5px;" />
			</fieldset>
			</form>
		</div>
		<%-- <div id="addNewVendorInvoiceDlg" style="width:736px;">
			<form id="addNewVendorInvoiceForm">
						<fieldset class= " ui-widget-content ui-corner-all">
						<legend><label ><b>Details</b></label></legend>
						<input type="button" id="viStatusButton" name="viStatusButton" value="Open" class="savehoverbutton turbo-blue" style="width: 90px;height: 25px;float: right;" onClick="vendorInvoiceStatusJob();" />
						<table>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all" style="height: 133px;">
									<legend><label class="fontclass" style="height: 150px;"><b>Payable To</b></label></legend>
									<table>
										<tr>
											<td><input type="text" style="border-width: 1px;" id="payable" name="payable" placeholder="Minimum 1 characters required to get Vendor list"><span style="color:red;">*</span></td>
											<td><input type="hidden" id="rxMasterID" name="rxMasterID"></td>
											<td><input type="hidden" id="veBillIdJob" name="veBillIdJob"></td>
											<td><input type="hidden" id="coAccountID" name="coAccountID"></td>
											<td><input type="hidden" id="joMasterID" name="joMasterID"></td>
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
										<td><label class="fontclass">Dated:</label></td><td><input type="text" class="datepicker" id="date" readonly="readonly" name="date" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 70%;"></td>
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
										<td><label class="fontclass">Post Date:</label></td><td><input type="checkbox" id="postdate" name="postdate" readonly="readonly" style="margin-left: 23px;"><input type="text" class="datepicker" id="postDate1" name="postDate1" value="<fmt:formatDate pattern="MM/dd/yyyy" value="${requestScope.currentDate}" />" style="width: 72%;"></td>
									</tr>
									<td></td>
									<td></td>
									
									<td  class="invReason">Reason:</td><td  class="invReason"><input type="text" id="invreason" readonly="readonly" style="width: 72%;margin-left: 39px;">
									
									
									</td>
									</tr>
									
								</table>
								</fieldset>
								</td>
							</tr>
							</table>
							<table>
							<tr>
								<td>
									<fieldset class= " ui-widget-content ui-corner-all">
									<legend><label class="fontclass"><b>GL Account Distribution</b></label></legend>
									<table>
										<tr>
										<td>
											<div id="jqgridLine">
												<table id="vendorInvoiceGrid"></table>
												<div id="vendorInvoicePager"></div>
											</div>
											</td>
									</tr>
									</table>
									</fieldset>
								</td>								
							</tr>
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
					</table>
					<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Save & Close" class="savehoverbutton turbo-blue addinv" onclick="addVendorInvoice()" style="float: right;" />
				</fieldset>
			</form>
		</div> --%>
		<div id="vendorQuickPay" style="width:736px;">
			<form id="vendorQuickForm">
			<fieldset class= " ui-widget-content ui-corner-all">
				<table>
					<tr>
						<td>
							<label class="fontclass">Vendor:<span style="color:red">*</span></label>
						</td>
						<td>
							<select id="vendorList" style="width:180px;">
								<option value="All">All</option>
								
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<label class="fontclass">Up to Amount:<span style="color:red">*</span></label>
						</td>
						<td>
							<input id="vendorQuickPayAmount" style="padding-right: 5px;padding-left: 5px;text-align:right;width:168px;"/>
						</td>
					</tr>
					<tr>
						<td>
							<label class="fontclass">Due Before:<span style="color:red">*</span></label>
						</td>
						<td>
							<input type="text" name="fromDate1" id="fromDate1" size="10"><label>
						</td>
					</tr>
				</table>
				<div>
				<span id="errorStatusquickpay" style="color:red;"></span>
				<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="Cancel" class="savehoverbutton turbo-blue" onclick="closeVendorQuick();" style="float: right;" />
				<input type="button" id="saveTermsButton" name="saveTermsButtonName" value="OK" class="savehoverbutton turbo-blue" onclick="okVendorQuick()" style="float: right;margin-right:5px;" />
				</div>
			</fieldset>
			</form>
		</div>
		<div class="bodyDiv" id="billFormDetails">
		<form id="billpayFormId" name="fname" method="post" >
			<table align="center" width="1060px">
				<tr>
					<td align="left">
						<table style="width: 1100px; padding-bottom: 30px;">
							<tr>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 250px; height: 84px;">
										<legend><b>Bank Account</b></legend>
										<input type="hidden" id="expandGrid" />
										<select style="width: 250px;" id="bankAccount" onchange="changeBankAccount()">
											<c:forEach var="bankAccountsList" items="${requestScope.bankAccountsList}">
												<option value='${bankAccountsList.moAccountId}'>
													<c:out value="${bankAccountsList.description}" ></c:out>
												</option>
											</c:forEach>
										</select>
									</fieldset>
								</td>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 260px;height: 84px;">
										<legend><b>Prepared Transactions</b></legend>
										<table>
											<tr>
												<td><label>Deposits/Payroll:</label></td>
												<td><input type="text" id="depositsORPayrollId" style="width:80px;" disabled="disabled" value="0"></td>
											</tr>
											<tr>
												<td><label>Bills approved to Pay:</label></td>
												<td><input type="text" id="billsApprovedAmountId" style="width:80px;" disabled="disabled" value="0"></td>
											</tr>
										</table>
									</fieldset>
								</td>
								<td>
									<fieldset class= "ui-widget-content ui-corner-all" style="width: 190px; height: 84px;">
										<legend><b>Balance</b></legend>
										<table>
											<tr><td><label>Current:</label></td><td><input type="text" style="width:100px;" disabled="disabled" id="current_balance"></td></tr>
											<tr><td><label>Resulting:</label></td><td><input type="text" style="width:100px;" disabled="disabled" id="resulting_ID"></td></tr>
										</table>
									</fieldset>
								</td>
								<td style="width: 250px;">
									<fieldset class= "ui-widget-content ui-corner-all" style="height: 84px;">
										<legend><b>Vendor Group</b></legend>
										<table>
											<tr><td><input type="text" style="width: 290px;" id="vendorBillPay_groupID" name="vendorBillPay_groupName" placeholder="Minimum 3 characters required"></td>
											<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"><input type="hidden" id="vendorBillPay_rxmasterID" /></td></tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="padding-left: 10px;">
						<span style="float:left;margin-top:-30px;margin-right:10px;"><input type="button" class="cancelhoverbutton turbo-tan"  id="vendorGroupOrUnGroup" value=" Vendor Ungroup" onclick="vendorHeaderClick();" style="color: white;"></span>
						<span style="float:left;margin-top:-30px;margin-left:130px;">
						<input type="button" class="cancelhoverbutton turbo-tan"  id="filterButton" value=" Displaying: All Types " onclick="" style="color: white;" />
						
						</span>
						<div id="filtertypesDiv" style="width:160px;height:89px;background-color: white;position:absolute;margin-left:130px;margin-top:-3px;z-index:100;border:1 solid;box-shadow:4px 3px 16px; border-bottom-left-radius:10px;border-bottom-right-radius:10px; display:none;">
						<table  style="width:100%" class="displayfilter" >
						<tr><td style="padding:7px;"><span style="text-decoration:none;padding:10px;width:100%" class="displayFilterText">All Types</span></td></tr>
						<tr><td style="padding:7px;"><span  style="text-decoration:none;padding:10px;width:100%" class="displayFilterText">Purchases</span></td></tr>
						<tr><td style="padding:7px;"><span  style="text-decoration:none;padding:10px;width:100%" class="displayFilterText">Expenses</span></td></tr>
						</table>
						
						
						</div>
						<span style="float:right;margin-top:-30px;margin-right:10px;"><input type="button" class="cancelhoverbutton turbo-tan"  value="Clear" onclick="clearAllCheckedBills();" style="width:80px;color: white;"></span>
						<table id="vendorBills" style="width:20px"></table>
					    <div id="vendorBillsPager">
					    	<div style="margin-top: -5px;">
								<img src="../resources/Icons/copyQuote.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="addNewVendorInvoice();">
								<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="addNewVendorInvoice();">New Bill</label>
<!-- 								<img src="../resources/Icons/copyQuote.png" style="margin-top: 0px;width: 20px;height: 14px;">
									<label style="vertical-align: super;position: relative;top: 2px;">New Bill</label> -->
								<input type="hidden" id="invStatus">
								<input type="checkbox" style="margin-top: 10px;width: 20px;height: 14px;" id="chk_termsDiscount" onclick="">
								<label style="vertical-align: super;position: relative;top: 2px;margin-left:-7px;">Take Terms Discount</label>
								<img src="../resources/images/search.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openEditDialog();">
								<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openEditDialog();">Edit Bill</label>
<!-- 								<input type="button" class="searchbutton" style="width: auto;" value="    Edit Bill" onclick="openEditDialog();"> -->
								<img src="../resources/images/lineItem_new.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openvendorQuickPay();">
								<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openvendorQuickPay();">Quick Pay</label>
<!-- 								<input type="button" class="add" style="width: auto;" value="    Quick Pay" onclick="openvendorQuickPay();"> -->
							</div>
					    </div>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
	<div id="openAddPayDig">
		<form id="addPayFormID">
			<table>
				<tr>
					<td><label>Original Amount: </label></td>
					<td><label id="originalAmonut_ID"></label></td>
				</tr>
				<tr>
					<td><label>Balance: </label></td>
					<td><label id="balance_ID"></label></td>
				</tr>
				<tr>
					<td><label>Discount: </label></td>
					<td><input type="text" id="discount_ID" name="discount_Name" value="0.00" style="width: 40px;"><label> % &nbsp;</label><input type="text" id="discount1_ID" name="discount1_Name" value="0.00" style="width: 98px;"></td>
				</tr>
				<tr>
					<td><label>Paying Now: </label></td>
					<td><input type="text" id="Paying_ID" name="Paying_Name"></td>
				</tr>
			</table>
			<hr>
			<table align="right">
				<tr>
				 	<td>
		   				<input type="button" class="cancelhoverbutton turbo-tan" value="Save" onclick="saveAddPayDig()" style=" width:80px;color: white;">
						<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelAddPayDig()" style="width:80px;color: white;">
		   			</td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		var moAccountsGlobal = [];
		var gloabalArrayID=[];
		var globalrxVendorArray=[];
		var negStatus=true;
		var partialCredit=true;
		var gP=1;
		
		jQuery(function() {
			jQuery("#EnterPODlg").dialog({
				autoOpen : false,
				height : 210,
				width : 360,
				title : "Enter PO",
				modal : true,
				close : function() {
					// $('#userFormId').validationEngine('hideAll');
					return true;
				}
			});

			$("#filterButton").click(function(e){
				$("#filtertypesDiv").slideToggle("fast");
			     e.stopPropagation();
			});

			$("#filtertypesDiv").click(function(e){
			    e.stopPropagation();
			});

			$(document).click(function(){
			    $("#filtertypesDiv").hide();
			});

			
			$(".displayFilterText").click(function(e){
				
				$("#filterButton").val(" Displaying: "+$(this).text()+" ");
				$("#filtertypesDiv").hide();

				
				$("#vendorBills").jqGrid('GridUnload');
				loadVendorBillPay($("#vendorBillPay_rxmasterID").val());
				var html = '<div style="margin-top: -5px;"><img src="../resources/Icons/copyQuote.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="addNewVendorInvoice();">'+
				'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="addNewVendorInvoice();">New Bill</label>'+
					'<input type="hidden" id="invStatus">'+
				'<input type="checkbox" style="margin-top: 10px;width: 20px;height: 14px;" id="chk_termsDiscount" onclick="">'+
				'<label style="vertical-align: super;position: relative;top: 2px;margin-left:-7px;">Take Terms Discount</label>'+
				'<img src="../resources/images/search.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openEditDialog();">'+
				'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openEditDialog();">Edit Bill</label>'+
				'<img src="../resources/images/lineItem_new.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openvendorQuickPay();">'+
				'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openvendorQuickPay();">Quick Pay</label>'+
				'</div>';
				$("#vendorBillsPager").html('');
				$("#vendorBillsPager").html(html);
				$("#vendorBills").trigger("reloadGrid"); 

				var val = $('#vendorGroupOrUnGroup').val(); 
				
				if(val === ' Vendor Ungroup')
					{
					  $('#vendorBills').jqGrid('groupingGroupBy', 'vendor');
					}
				else
					{
					  $('#vendorBills').jqGrid('groupingRemove');
					}
				
			});
						
		});


		//vendorQuickPay
		jQuery(function() {
			jQuery("#vendorQuickPay").dialog({
				autoOpen : false,
				height : 170,
				width : 360,
				title : "Pay Vendor",
				modal : true,
				close : function() {
					// $('#userFormId').validationEngine('hideAll');
					return true;
				}
			});
		});
		
		function closeVendorQuick()
		{
			jQuery("#vendorQuickPay").dialog("close");
			return true;
		}
		
		function okVendorQuick()
		{
			
			if($("#vendorQuickPayAmount").val()=="")
				{
				
				$('#errorStatusquickpay').html("All Fields are Required.");
				setTimeout(function(){
					$('#errorStatusquickpay').html("");
					},2000);
				}
			else if($("#fromDate1").val()=="")
				{
				$('#errorStatusquickpay').html("All Fields are Required.");
				setTimeout(function(){
					$('#errorStatusquickpay').html("");
					},2000);
				}
			else
				{	

				var termsCheckedorNot; 
				
				if($("#chk_termsDiscount").is(":checked"))
					{
					termsCheckedorNot = "checked";
					}
				else
					{
					termsCheckedorNot = "not checked";
					}
				
				if($("#vendorList").val()=="All")
					{

			 	$.ajax({
				url: "./vendorscontroller/vendorbills_data_quickpay",
				type: "POST",
				data : {"moAccountID": $("#bankAccount").val(),"fromDate":$("#fromDate1").val(),"vendorQuickPayAmount":$("#vendorQuickPayAmount").val(),"rxMasterID":globalrxVendorArray,"termsCheckedorNot":termsCheckedorNot},
				success: function(data) {
					createtpusage('Company-Vendors-Pay Bills','Save Quick Pay','Info','Company-Vendors-Pay Bills,Saving Bills,moAccountID:'+$("#bankAccount").val()+',rxMasterID:'+globalrxVendorArray);
					jQuery("#vendorQuickPay").dialog("close");
					
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:green;">Payment Applied.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Quick Payment", 
											buttons: [{text: "OK",click: function(){ $(this).dialog("close");$("#vendorBills").trigger("reloadGrid"); }}]
										}).dialog("open");
					
					
						}
					}); 
				}
				else
					{
				 var globalrxVendorArray1 =[];

				 globalrxVendorArray1.push($("#vendorList").val());

					$.ajax({
						url: "./vendorscontroller/vendorbills_data_quickpay",
						type: "POST",
						data : {"moAccountID": $("#bankAccount").val(),"fromDate":$("#fromDate1").val(),"vendorQuickPayAmount":$("#vendorQuickPayAmount").val(),"rxMasterID":globalrxVendorArray1,"termsCheckedorNot":termsCheckedorNot},
						success: function(data) {

							jQuery("#vendorQuickPay").dialog("close");
							
							var newDialogDiv = jQuery(document.createElement('div'));
							jQuery(newDialogDiv).html('<span><b style="color:green;">Payment Applied.</b></span>');
							jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Quick Payment", 
													buttons: [{text: "OK",click: function(){ $(this).dialog("close");$("#vendorBills").trigger("reloadGrid"); }}]
												}).dialog("open");
							
							
								}
							});
					
					}
				}
			
		}
	
		
		function openvendorQuickPay() {
			document.getElementById("vendorQuickForm").reset();
			jQuery("#vendorQuickPay").dialog("open");
			return true;
		}

		function vendorHeaderClick()
		{
			var val = $('#vendorGroupOrUnGroup').val(); 

				if(val === ' Vendor Ungroup')
				{

					 $('#vendorBills').jqGrid('groupingRemove');
					 $('#vendorGroupOrUnGroup').val(' Vendor Group');
				}
			else
				{
				 $('#vendorBills').jqGrid('groupingGroupBy', 'vendor');
				 $('#vendorGroupOrUnGroup').val(' Vendor Ungroup');
				 $('#expandGrid').val("");
				}
		}


		function clearAllCheckedBills()
		{
			var grid = $("#vendorBills");

			
			$.ajax({
				url: "./vendorscontroller/clearAllCheckedBills",
				type: "POST",
				data : {"moAccountID": $("#bankAccount").val()},
				success: function(data) {

					//location.reload();
					
					jQuery('#vendorBills').trigger('reloadGrid');
					
					//$("#vendorBills").trigger("reloadGrid"); 
					loadVebillPaydetails();
					
					var val = $('#vendorGroupOrUnGroup').val(); 
					
					if(val === ' Vendor Ungroup')
						{
						  $('#vendorBills').jqGrid('groupingGroupBy', 'vendor');
						}
					else
						{
						
						  $('#vendorBills').jqGrid('groupingRemove');
						}
					
				}

			
			});
		
		}

		
		jQuery(document).ready(function(){
			
			$('#fromDate1').datepicker();
			$("#fromDate1").prop('disabled',false);
			$("#vendorQuickPayAmount").keypress(function (event) {
				if(event.which == 8 || event.which == 9 || event.which==45 || event.which == 0) {}
				else if( event.which < 45 || event.which > 46) {
					if(event.which < 48 || event.which > 57)
						event.preventDefault();
				}
				else if(event.which == 46 && $(this).val().indexOf('.') != -1) {
					event.preventDefault();
				}
				else {}
		    });
			
			var manufacturer = "";
			moAccountsGlobal = loadBankAccountsDetails();
			$(".tabs_main").tabs({
				cache: true,
				ajaxOptions: {
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 300px;padding-top: 200px;'>"+
								"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
								"</label></div>");
					}
				},
				load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
				select: function (e, ui) {
					//window.location.hash = ui.tab.hash;
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 300px;padding-top: 200px;background-color: #FAFAFA'>"+
								"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
			loadVendorBillPay(manufacturer);
			setTimeout('loadBankAccount()', 200);
			$("#search").css("display", "none");
	   	});

		function loadBankAccountsDetails(){
			var moAccounts = []; //create a new array global
		    <c:forEach items="${requestScope.bankAccountsList}" var="moAccount">
			    moAccounts.push({moAccountId:"${moAccount.moAccountId}",
				    			description:"${moAccount.description}",
				    			additions:"${moAccount.additions}",
				    			openBalance:"${moAccount.openBalance}",
				    			subtractions:"${moAccount.subtractions}",
				    			undepositedReceipts:"${moAccount.undepositedReceipts}",
				    			unprintedPayables:"${moAccount.unprintedPayables}",
				    			unprintedPayroll:"${moAccount.unprintedPayroll}"});
		    </c:forEach>

		    return moAccounts;
		}

		function loadBankAccount(){

			$(function() {
			$.ajax({
		 		url: "./banking/getResultingBalance",
		 		type: "POST",
		 		data : {"moAccountID": $("#bankAccount").val()},
		 		success: function(data) {
		 			
		 			$("#current_balance").empty();
		 			$("#current_balance").val(formatCurrency(data.currentbalance));
		 			$("#resulting_ID").empty();
					var payableApprovedID =$("#billsApprovedAmountId").val().replace(/[^0-9\.-]+/g,"");		 			
		 			var resultBalance = (data.currentbalance)-(payableApprovedID);
		 			$("#resulting_ID").val(formatCurrency(resultBalance));
		 			
		 		
		 		}
		 	});
			});
			
			return false;
		}
		
		function changeBankAccount(){

			$(function() {
			$.ajax({
		 		url: "./banking/getResultingBalance",
		 		type: "POST",
		 		data : {"moAccountID": $("#bankAccount").val()},
		 		success: function(data) {
		 			
		 			$("#current_balance").empty();
		 			$("#current_balance").val(formatCurrency(data.currentbalance));
		 			$("#resulting_ID").empty();
		 			
					var bilapvalue=$("#billsApprovedAmountId").val().replace(/[^0-9\.-]+/g,"");
		 			$("#billsApprovedAmountId").empty();
		 			if(data.withDrawel!=null)
		 			$("#billsApprovedAmountId").val(formatCurrency(-parseInt(data.withDrawel)));
		 			else
		 			$("#billsApprovedAmountId").val(formatCurrency(-parseInt(0)));	

		 			var resultBalance = (data.currentbalance)-(bilapvalue);
		 			$("#resulting_ID").val(formatCurrency(resultBalance));
		 		
		 		}
		 	});
			});

			return false;
		}
		


		$("#discount_ID").bind("keyup",function(){
				
			var currency =$("#originalAmonut_ID").html();
			var number = Number(currency.replace(/[^0-9\.]+/g,""));
			var amt=number*(parseFloat($("#discount_ID").val())/100);

				if(!isNaN(amt))
				{
				$("#discount1_ID").val(formatCurrency(number*(parseFloat($("#discount_ID").val())/100)));
				}
				else
				{
				$("#discount1_ID").val("0.00");
				}
			
			})
			
		$(".ui-icon-circlesmall-minus").live("click",function(){
			//console.log($(this).closest('tr').attr('id'));
			var expandgridvalue = $("#expandGrid").val();		
			$("#expandGrid").val("");
			$("#expandGrid").val(expandgridvalue+","+$(this).closest('tr').attr('id'));
		});
		
		var vendorList = [];
		var rxVendorList = [];
		
		 function tempArr(arr) {
	        newArr = new Array();
	        for (i = 0; i < arr.length; i++) {
	            if (!duplValuescheck(newArr, arr[i])) {
	                newArr.length += 1;
	                newArr[newArr.length - 1] = arr[i];
	            }
	        }
	        //alert(newArr);
	    }
		function duplValuescheck(arr, e) {
	        for (j = 0; j < arr.length; j++) if (arr[j] == e) return true;
	        return false;
	    }
		
		
		
	function loadVendorBillPay(manufacturer){

			
			$("#vendorBills").jqGrid({
				url:'./vendorscontroller/vendorbills_data',
				datatype: 'JSON',
				mtype: 'GET',
				pager: jQuery('#vendorBillsPager'),
				colNames: ['Pay','RxMasterID', 'Vendor', 'Due Date', 'PO #', 'Invoice #', 'Bill Date', 'Balance', 'VeBillID', 'RxMasterID','Applying Amount','DiscountAmount','tranStatus','reason'],
				colModel: [
					{name:'payBill', index:'payBill', align:'center',sortable: false , hidden:false, width:20, editable:true, edittype:'text', editoptions:{size:30}, formatter:billApplyingFromatter, editrules:{required:false},cellattr: function (rowId, val, rawObject) {
						
						if(rawObject.tranStatus ==-2)
						 console.log("-------------***********"+rawObject.reason);
						return rawObject.tranStatus ==-2 ? 'title="' + rawObject.reason + '"': 'title=""';
				
					}},
					{name:'rxMasterID', index:'rxMasterID', align:'center', hidden:true, width:20, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'vendor', index:'vendor', align:'left', hidden:false, width:130, editable:true, edittype:'text', editoptions:{size:30},
                         cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editrules:{required:false},
                         formatter:function(cellvalue, options, rowObject){
                        	 //console.log("cellvalue :: "+cellvalue);
                        	
                        	 vendorList.push(cellvalue);
                        	 //console.log("cellvalue :: "+vendorList);
                        	 return cellvalue;
                         }
                    },
					{name:'dueDate', index:'dueDate', align:'center', hidden:false, width:40, editable:true, edittype:'text',sorttype: 'dueDate', editoptions:{size:30}, editrules:{required:false}},
		           	{name:'ponumber', index:'ponumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'billDate', index:'billDate', align:'center', width:40, editable:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'balance', index:'balance', align:'right', width:40, editable:true,hidden:false, editrules:{required:false}, formatter:customCurrencyFormatter },
		           	{name:'veBillID', index:'veBillID', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}},
		           	{name:'vendorRxMasterID', index:'vendorRxMasterID', align:'right', width:10, editable:true,hidden: true, editrules:{required:false},
			           	formatter:function(cellvalue, options, rowObject){
					    rxVendorList.push(cellvalue);
                    	 return cellvalue;
                     }},
		           	{name:'applyingAmount', index:'applyingAmount', align:'right', width:50, editable:true,hidden: false, editrules:{required:false}, formatter:customCurrencyFormatter},
                     {name:'discountAmount', index:'discountAmount', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}, formatter:customCurrencyFormatter},
		           	{name:'tranStatus', index:'tranStatus', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}},
		           	{name:'reason', index:'reason', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}}
			    ],
				rowNum: 10000,
				pgbuttons: false,
				recordtext: '',
				rowList: [],
				pgtext: null,
				viewrecords: false,
				sortname: 'vendor',
				sortorder: "asc",
				imgpath: 'themes/basic/images',
				caption: 'Pay Bills',
				height:420,
				width: 1127,
				altRows: true,
				altclass:'myAltRowClass',
				grouping:true,
				groupingView:{		
					groupField : ['vendor'],
			   		groupColumnShow : [true],
			   		groupCollapse : true		   	
			   		},
				postData: {
					manufacturerID: function() { return manufacturer; },
					displayType: function(){return $("#filterButton").val()}
				},
				loadComplete: function(data) {
					
					var uniqueArray = vendorList.filter(function(elem, pos) {
					    return vendorList.indexOf(elem) == pos;
					    
					    
					   /*  var ids = jQuery("#vendorBills").jqGrid('getDataIDs');
					    for (var i = 0; i < ids.length; i++) 
					    {
					        var rowId = ids[i];
					        var tranStatus=$("#vendorBills").jqGrid('getCell', rowId,'tranStatus');
					        if(tracStatus==-2){
					        var reason=$("#vendorBills").jqGrid('getCell', rowId,'reason');
					        //$("#vendorBills").jqGrid('setCell', rowid, 'payBill', reason);
					        $("#vendorBills").setCell(rowId,'payBill','','',{'title':reason});
					        }
					       

					        console.log(rowData.Phrase);
					        console.log(rowId); */
					   // }
					    
					  }); 

					var rxVendoruniqueArray = rxVendorList.filter(function(elem, pos) {
					    return rxVendorList.indexOf(elem) == pos;
					  });

					console.log(" vendorList.length :: "+vendorList.length+" || uniqueArray :: "+uniqueArray.length+" || rxVendorArray :: "+rxVendoruniqueArray.length);
					var select = $("#vendorList");
					select.html("");
					var html = "<option value='All' >All</option>";
					globalrxVendorArray = [];
					for(var i=0; i<uniqueArray.length; i++)
						{
						html +="<option value="+rxVendoruniqueArray[i]+">"+uniqueArray[i]+"</option>";
						globalrxVendorArray.push(rxVendoruniqueArray[i]);
						
						}
					select.append(html);
					loadVebillPaydetails();

					console.log($("#expandGrid").val());
					
					if($("#expandGrid").val()!="")
						{
						var expandGridArray=$("#expandGrid").val().split(",");
						
						for(var i=1; i < expandGridArray.length ;i++)
							{
							console.log(expandGridArray[i]);
							$('#vendorBills').jqGrid('groupingToggle', expandGridArray[i]);

							}
						}
			    },
			    ondblClickRow: function(rowid) {

					
			    	document.getElementById("addPayFormID").reset();
			    	
			    	var rowData = jQuery(this).getRowData(rowid); 
					var balanceAmount = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var balance = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var vebillID = rowData["veBillID"];
					var applyingAmount = rowData["applyingAmount"];
					var vendorID =  rowData["vendorRxMasterID"];
					var billDate = new Date (rowData["billDate"]);
					var curDate = new Date();

					
					$("#originalAmonut_ID").empty();
					$("#originalAmonut_ID").text(formatCurrency(balanceAmount));
					$("#balance_ID").empty();
					$("#balance_ID").text(formatCurrency(balance));

					 console.log("balanceAmount::"+balanceAmount+"|| balance::"+balance);
					$.ajax({
						url: "./vendorscontroller/getBillPayDetails",
						type: "GET",
						data: {'vebilID' : vebillID},
						success: function(data) {

							
							if(data != ''){
								$.each(data, function(index, value) { 
									var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
									var aDisCount1 = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
									
									var aCalCulateNo = (value.discountAmount/aDisCount1)*100;
									$("#discount_ID").val("");
									$("#discount_ID").val(aCalCulateNo.toFixed(2));
									$("#Paying_ID").val("");
									$("#Paying_ID").val(formatCurrency(value.applyingAmount));
									$("#discount1_ID").val("");
									$("#discount1_ID").val(formatCurrency(value.discountAmount));
								});
							}
							else
								{
								if($("#chk_termsDiscount").is(":checked"))
								{
								var aDisCountAmt = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
								$.ajax({
									url: "./vendorscontroller/gettermsDiscountsfromveMaster",
									type: "GET",
									data: {'vendorID' : vendorID},
									success: function(data) {

										console.log(data);

									//	alert(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear());

										if(data.discOnDay==true)
										{
											billDate.setMonth(billDate.getMonth()+Number(data.discountDays));	
										}
										else
										{
											billDate.setDate(billDate.getDate()+Number(data.discountDays));
										}


										console.log(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear()+"---"+curDate.getDay()+"/"+curDate.getMonth()+"/"+curDate.getFullYear());

								//		alert(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear());
										
											if(billDate >= curDate)
											{
											$("#discount_ID").val("");
											$("#discount_ID").val(Number(data.discountPercent));
											$("#discount1_ID").val("");
											$("#discount1_ID").val(formatCurrency((Number(data.discountPercent)*aDisCountAmt)/100));
											}
									}
								});
									
								}
								}
						}
					});
			    	openAddPayDialog();
		    	},
				loadError : function (jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				},
				jsonReader : {
					root: "rows",
					page: "page",
					total: "total",
					records: "records",
					repeatitems: false,
					cell: "cell",
					id: "id",
					sortorder: "asc",
					userdata: "userdata"
				}
			});
		}
		
		/* function loadVendorBillPay(manufacturer){

			
			$("#vendorBills").jqGrid({
				url:'./vendorscontroller/vendorbills_data',
				datatype: 'JSON',
				mtype: 'GET',
				pager: jQuery('#vendorBillsPager'),
				colNames: ['Pay','RxMasterID', 'Vendor', 'Due Date', 'PO #', 'Invoice #', 'Bill Date', 'Balance', 'VeBillID', 'RxMasterID','Applying Amount','DiscountAmount'],
				colModel: [
					{name:'payBill', index:'payBill', align:'center',sortable: false , hidden:false, width:20, editable:true, edittype:'text', editoptions:{size:30}, formatter:billApplyingFromatter, editrules:{required:false}},
					{name:'rxMasterID', index:'rxMasterID', align:'center', hidden:true, width:20, editable:true, edittype:'text', editoptions:{size:30}, editrules:{required:false}},
					{name:'vendor', index:'vendor', align:'left', hidden:false, width:130, editable:true, edittype:'text', editoptions:{size:30},
                         cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editrules:{required:false},
                         formatter:function(cellvalue, options, rowObject){
                        	 //console.log("cellvalue :: "+cellvalue);
                        	
                        	 vendorList.push(cellvalue);
                        	 //console.log("cellvalue :: "+vendorList);
                        	 return cellvalue;
                         }
                    },
					{name:'dueDate', index:'dueDate', align:'center', hidden:false, width:40, editable:true, edittype:'text',sorttype: 'dueDate', editoptions:{size:30}, editrules:{required:false}},
		           	{name:'ponumber', index:'ponumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'center', width:40, editable:true, edittype:'text', 	editoptions:{size:30}, editrules:{required:false}},
		           	{name:'billDate', index:'billDate', align:'center', width:40, editable:true, edittype:'text', editoptions:{}, editrules:{required:false}},
		           	{name:'balance', index:'balance', align:'right', width:40, editable:true,hidden:false, editrules:{required:false}, formatter:customCurrencyFormatter },
		           	{name:'veBillID', index:'veBillID', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}},
		           	{name:'vendorRxMasterID', index:'vendorRxMasterID', align:'right', width:10, editable:true,hidden: true, editrules:{required:false},
			           	formatter:function(cellvalue, options, rowObject){
					    rxVendorList.push(cellvalue);
                    	 return cellvalue;
                     }},
		           	{name:'applyingAmount', index:'applyingAmount', align:'right', width:50, editable:true,hidden: false, editrules:{required:false}, formatter:customCurrencyFormatter},
                     {name:'discountAmount', index:'discountAmount', align:'right', width:50, editable:true,hidden: true, editrules:{required:false}, formatter:customCurrencyFormatter}
			    ],
				rowNum: 10000,
				pgbuttons: false,
				recordtext: '',
				rowList: [],
				pgtext: null,
				viewrecords: false,
				sortname: 'vendor',
				sortorder: "asc",
				imgpath: 'themes/basic/images',
				caption: 'Pay Bills',
				height:420,
				width: 1127,
				altRows: true,
				altclass:'myAltRowClass',
				grouping:true,
				groupingView:{		
					groupField : ['vendor'],
			   		groupColumnShow : [true],
			   		groupCollapse : true		   	
			   		},
				postData: {
					manufacturerID: function() { return manufacturer; },
					displayType: function(){return $("#filterButton").val()}
				},
				loadComplete: function(data) {
					
					var uniqueArray = vendorList.filter(function(elem, pos) {
					    return vendorList.indexOf(elem) == pos;
					  }); 

					var rxVendoruniqueArray = rxVendorList.filter(function(elem, pos) {
					    return rxVendorList.indexOf(elem) == pos;
					  });

					console.log(" vendorList.length :: "+vendorList.length+" || uniqueArray :: "+uniqueArray.length+" || rxVendorArray :: "+rxVendoruniqueArray.length);
					var select = $("#vendorList");
					select.html("");
					var html = "<option value='All' >All</option>";
					globalrxVendorArray = [];
					for(var i=0; i<uniqueArray.length; i++)
						{
						html +="<option value="+rxVendoruniqueArray[i]+">"+uniqueArray[i]+"</option>";
						globalrxVendorArray.push(rxVendoruniqueArray[i]);
						
						}
					select.append(html);
					loadVebillPaydetails();

					console.log($("#expandGrid").val());
					
					if($("#expandGrid").val()!="")
						{
						var expandGridArray=$("#expandGrid").val().split(",");
						
						for(var i=1; i < expandGridArray.length ;i++)
							{
							console.log(expandGridArray[i]);
							$('#vendorBills').jqGrid('groupingToggle', expandGridArray[i]);

							}
						}
			    },
			    ondblClickRow: function(rowid) {

					
			    	document.getElementById("addPayFormID").reset();
			    	
			    	var rowData = jQuery(this).getRowData(rowid); 
					var balanceAmount = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var balance = rowData["balance"].replace(/[^0-9\.]+/g,"");
					var vebillID = rowData["veBillID"];
					var applyingAmount = rowData["applyingAmount"];
					var vendorID =  rowData["vendorRxMasterID"];
					var billDate = new Date (rowData["billDate"]);
					var curDate = new Date();

					
					$("#originalAmonut_ID").empty();
					$("#originalAmonut_ID").text(formatCurrency(balanceAmount));
					$("#balance_ID").empty();
					$("#balance_ID").text(formatCurrency(balance));

					 console.log("balanceAmount::"+balanceAmount+"|| balance::"+balance);
					$.ajax({
						url: "./vendorscontroller/getBillPayDetails",
						type: "GET",
						data: {'vebilID' : vebillID},
						success: function(data) {

							
							if(data != ''){
								$.each(data, function(index, value) { 
									var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
									var aDisCount1 = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
									
									var aCalCulateNo = (value.discountAmount/aDisCount1)*100;
									$("#discount_ID").val("");
									$("#discount_ID").val(aCalCulateNo.toFixed(2));
									$("#Paying_ID").val("");
									$("#Paying_ID").val(formatCurrency(value.applyingAmount));
									$("#discount1_ID").val("");
									$("#discount1_ID").val(formatCurrency(value.discountAmount));
								});
							}
							else
								{
								if($("#chk_termsDiscount").is(":checked"))
								{
								var aDisCountAmt = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
								$.ajax({
									url: "./vendorscontroller/gettermsDiscountsfromveMaster",
									type: "GET",
									data: {'vendorID' : vendorID},
									success: function(data) {

										console.log(data);

									//	alert(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear());

										if(data.discOnDay==true)
										{
											billDate.setMonth(billDate.getMonth()+Number(data.discountDays));	
										}
										else
										{
											billDate.setDate(billDate.getDate()+Number(data.discountDays));
										}


										console.log(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear()+"---"+curDate.getDay()+"/"+curDate.getMonth()+"/"+curDate.getFullYear());

								//		alert(billDate.getDay()+"/"+billDate.getMonth()+"/"+billDate.getFullYear());
										
											if(billDate >= curDate)
											{
											$("#discount_ID").val("");
											$("#discount_ID").val(Number(data.discountPercent));
											$("#discount1_ID").val("");
											$("#discount1_ID").val(formatCurrency((Number(data.discountPercent)*aDisCountAmt)/100));
											}
									}
								});
									
								}
								}
						}
					});
			    	openAddPayDialog();
		    	},
				loadError : function (jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error: '+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				},
				jsonReader : {
					root: "rows",
					page: "page",
					total: "total",
					records: "records",
					repeatitems: false,
					cell: "cell",
					id: "id",
					sortorder: "asc",
					userdata: "userdata"
				}
			});
		} */

		function openEditDialog()
		{

			try{
	// 			var myGrid = $('#vendorBills'),
	// 			rowid = myGrid.jqGrid ('getGridParam', 'selrow');
				var grid = $("#vendorBills");
				var rowid = grid.jqGrid('getGridParam', 'selrow');
				//alert(rowid);
			//	cellValue = myGrid.jqGrid ('getCell', rowid, 'Vendor');
				
		    	document.getElementById("addPayFormID").reset();
		    	
		    	var rowData = $('#vendorBills').jqGrid('getRowData', rowid);
	//	    	var rowData = jQuery(this).getRowData(rowid); 
				var balanceAmount = rowData["balance"].replace(/[^0-9\.]+/g,"");
				var balance = rowData["balance"].replace(/[^0-9\.]+/g,"");
				var vebillID = rowData["veBillID"];
				var applyingAmount = rowData["applyingAmount"];
				var vendorID =  rowData["vendorRxMasterID"];
				var billDate = new Date (rowData["billDate"]);
				var curDate = new Date();
				

				
				$("#originalAmonut_ID").empty();
				$("#originalAmonut_ID").text(formatCurrency(balanceAmount));
				$("#balance_ID").empty();
				$("#balance_ID").text(formatCurrency(balance));

				$.ajax({
					url: "./vendorscontroller/getBillPayDetails",
					type: "GET",
					data: {'vebilID' : vebillID},
					success: function(data) {

						if(data != ''){
							$.each(data, function(index, value) { 
								var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
								var aDisCount1 = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
								
								var aCalCulateNo = (value.discountAmount/aDisCount1)*100;
								$("#discount_ID").val("");
								$("#discount_ID").val(aCalCulateNo.toFixed(2));
								$("#Paying_ID").val("");
								$("#Paying_ID").val(formatCurrency(value.applyingAmount));
								$("#discount1_ID").val("");
								$("#discount1_ID").val(formatCurrency(value.discountAmount));
							});
						}
						else
							{
							if($("#chk_termsDiscount").is(":checked"))
							{
							var aDisCountAmt = $("#originalAmonut_ID").html().replace(/[^0-9\.]+/g,"");
							$.ajax({
								url: "./vendorscontroller/gettermsDiscountsfromveMaster",
								type: "GET",
								data: {'vendorID' : vendorID},
								success: function(data) {

									console.log(data);

									

									if(data.discOnDay==true)
									{
										billDate.setMonth(billDate.getMonth()+Number(data.discountDays));	
									}
									else
									{
										billDate.setDate(billDate.getDate()+Number(data.discountDays));
									}

										if(billDate >= curDate)
										{
										$("#discount_ID").val("");
										$("#discount_ID").val(Number(data.discountPercent));
										$("#discount1_ID").val("");
										$("#discount1_ID").val(formatCurrency((Number(data.discountPercent)*aDisCountAmt)/100));
										}
								}
							});
								
							}
							}
					}
				});
		    	openAddPayDialog();
			}
			catch(exception){}
		}
		
		/* function billApplyingFromatter(cellvalue, options, rowObject){

		
			var anApplyingAmount = rowObject.applyingAmount;
			var aBillAmount = rowObject.billAmount;
			var balanceAmt = rowObject.balance;
			var disamt = rowObject.discountAmount;
			var image = "";

			console.log(disamt);

			if(isNaN(balanceAmt))
			{
				balanceAmt = balanceAmt.replace(/[$,]+/g,"");
			}

			if(disamt!=null && disamt!="$0.00" && disamt != "0")
			{
				image = '<img alt="search" src="../resources/Icons/View_Discount_Taken.BMP" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
				cellvalue = image;
			}
			else
				{
					if(balanceAmt < 0 && (anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0  || (anApplyingAmount > balanceAmt && anApplyingAmount > 0)))
					{
						image='<img alt="search" src="../resources/Icons/cr_grey.png" onclick="payDiscountFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					}
					else if(balanceAmt < 0 && (balanceAmt === anApplyingAmount|| anApplyingAmount < balanceAmt || (anApplyingAmount > balanceAmt && anApplyingAmount<0)))
					{
						image = '<img alt="search" src="../resources/Icons/cr_red.png" onclick="payDiscountFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					}
					else if(balanceAmt > 0 && anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0 ) {				
						image = '<img alt="search" src="../resources/Icons/bill_empty.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					} 
					else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
		
						image = '<img alt="search" src="../resources/Icons/bill_half.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					}
					else if(anApplyingAmount === aBillAmount) {
						image = '<img alt="search" src="../resources/Icons/bill_full.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					}
					else if(anApplyingAmount>aBillAmount){
						image='<img alt="search" src="../resources/Icons/add-1.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
						cellvalue = image;
					}
				}
			
			return cellvalue;
		}

 */
 function billApplyingFromatter(cellvalue, options, rowObject){

		
		var anApplyingAmount = rowObject.applyingAmount;
		var aBillAmount = rowObject.billAmount;
		var balanceAmt = rowObject.balance;
		var disamt = rowObject.discountAmount;
		var image = "";
		//edited by prasant id=#548 date 27/06/2016
		var transactionStatus=String(rowObject.tranStatus);
		console.log(disamt);
		
     
 	console.log("Transaction status--->"+transactionStatus);
		
 
		//upto
		
		if(isNaN(balanceAmt))
		{
			balanceAmt = balanceAmt.replace(/[$,]+/g,"");
		}

		if(disamt!=null && disamt!="$0.00" && disamt != "0")
		{
			image = '<img alt="search" src="../resources/Icons/View_Discount_Taken.BMP" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
			cellvalue = image;
		}
		else
			{
				if(balanceAmt < 0 && (anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0  || (anApplyingAmount > balanceAmt && anApplyingAmount > 0)))
				{
					image='<img alt="search" src="../resources/Icons/cr_grey.png" onclick="payDiscountFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				}
				else if(balanceAmt < 0 && (balanceAmt === anApplyingAmount|| anApplyingAmount < balanceAmt || (anApplyingAmount > balanceAmt && anApplyingAmount<0)))
				{
					image = '<img alt="search" src="../resources/Icons/cr_red.png" onclick="payDiscountFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				}
				else if(balanceAmt > 0 && anApplyingAmount === null || anApplyingAmount === "" || anApplyingAmount===0 ) {				
					image = '<img alt="search" src="../resources/Icons/bill_empty.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				} 
				else if(anApplyingAmount > 0 && anApplyingAmount < aBillAmount){
	
					image = '<img alt="search" src="../resources/Icons/bill_half.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				}
				else if(anApplyingAmount === aBillAmount) {
					image = '<img alt="search" src="../resources/Icons/bill_full.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				}
				else if(anApplyingAmount>aBillAmount){
					image='<img alt="search" src="../resources/Icons/add-1.png" onclick="payFullAmount('+balanceAmt+','+options.rowId+',this)">';
					cellvalue = image;
				}
				

			}
		 if(transactionStatus=="-2")
			{
				
			image = '<img  alt="search" src="../resources/Icons/redHold3.png" onclick="generateDailogForHoldVendorpayBill()">';
			cellvalue = image;		
			}
		
		return cellvalue;
	}

 
 
 
 function generateDailogForHoldVendorpayBill()
	{
	//var option=$("#vendorAccountList option:selected" ).text();

	
		var newDialogDiv = jQuery(document.createElement('div'));
						
		jQuery(newDialogDiv).html('<span><b style="color:red;">Would you like to take invoice off Payment hold </b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, 
								buttons: [{text: "Yes",click: function(){ $(".ui-dialog-content").dialog("close");changeTransactionStatus();}},
								          {text: "No",click: function(){ $(".ui-dialog-content").dialog("close"); }}]
							}).dialog("open");
		
	 }
 
 
//added by prasant  id=#484 date 27/06/2016
	
	function changeTransactionStatus(){
		var aSelectedRowId =$("#vendorBills").jqGrid('getGridParam', 'selrow');
		
		 var veBillId=$("#vendorBills").jqGrid('getCell', aSelectedRowId,'veBillID');
				
		$.ajax({
			url: "./veInvoiceBillController/changeTransactionStatus",
			type: "POST",
			data :  { "veBillId":veBillId},
			success: function(data) {

					$("#vendorBills").trigger("reloadGrid"); 
			},
	});

	} 

 
 
 
 
		function payFullAmount(billAmt,rowId,img)
		{
			
			  if(img.src.match("bill_empty.png"))
			   {				  
			 	 

			 	var grid = $('#vendorBills');
			 	var vendorID = grid.jqGrid('getCell',rowId, 'vendorRxMasterID'); 
				var billDate = new Date (grid.jqGrid('getCell',rowId, 'billDate'));
				var curDate = new Date();
				var aDisCount = 0;
 				var aPayingAmount = billAmt;


			 	if($("#chk_termsDiscount").is(":checked"))
				{
					$.ajax({
						url: "./vendorscontroller/gettermsDiscountsfromveMaster",
						type: "GET",
						data: {'vendorID' : vendorID},
						success: function(data) {

							if(data.discOnDay==true)
							{
								billDate.setMonth(billDate.getMonth()+Number(data.discountDays));	
							}
							else
							{
								billDate.setDate(billDate.getDate()+Number(data.discountDays));
							}

							if(billDate >= curDate)
							{
							aDisCount = (Number(data.discountPercent)*Number(billAmt))/100;
							aPayingAmount = Number(aPayingAmount)-Number(aDisCount);

							img.src="../resources/Icons/View_Discount_Taken.BMP";							
						 	saveAddPayDigfromimageclick(aPayingAmount,aDisCount,rowId);
							}
							else
							{
								img.src="../resources/Icons/bill_full.png";							
							 	saveAddPayDigfromimageclick(aPayingAmount,aDisCount,rowId);
							}	

							

							var rowData = $('#vendorBills').jqGrid('getRowData', rowId);
							rowData.applyingAmount = aPayingAmount;
							$('#vendorBills').jqGrid('setRowData', rowId, rowData);
						}
					});
				}
			 	else
				{
			 		img.src="../resources/Icons/bill_full.png";
			 	 	saveAddPayDigfromimageclick(aPayingAmount,aDisCount,rowId);

					var rowData = $('#vendorBills').jqGrid('getRowData', rowId);
					rowData.applyingAmount = aPayingAmount;
					$('#vendorBills').jqGrid('setRowData', rowId, rowData);
				}

			 	
			   }
			  else
				{
				img.src="../resources/Icons/bill_empty.png";
			
				removePayBills(rowId);
				
		    	var rowData = $('#vendorBills').jqGrid('getRowData', rowId);
				rowData.applyingAmount = 0;
				$('#vendorBills').jqGrid('setRowData', rowId, rowData);
				
				var rowData1 = $('#vendorBills').jqGrid('getRowData', rowId);
				rowData1.discountAmount = 0;
				$('#vendorBills').jqGrid('setRowData', rowId, rowData1);
				
				}
		}

	function payDiscountFullAmount(billAmt,rowId,img)
	{

		 if(img.src.match("cr_grey.png"))
		  {
		  billAmt = billAmt;

		  img.src="../resources/Icons/cr_red.png";
		  saveAddPayDigfromimageclick(billAmt,0,rowId);

		  var rowData = $('#vendorBills').jqGrid('getRowData', rowId);
			rowData.applyingAmount = billAmt;
		 $('#vendorBills').jqGrid('setRowData', rowId, rowData);
		  
		  }
	 	 else
		  {
			img.src="../resources/Icons/cr_grey.png";
			removePayBills(rowId);
			img.src="../resources/Icons/cr_grey.png";

			var rowData = $('#vendorBills').jqGrid('getRowData', rowId);
			rowData.applyingAmount = 0;
			$('#vendorBills').jqGrid('setRowData', rowId, rowData);
		  }
	}
		
	
		

		$(function() {

			$( "#vendorBillPay_groupID" ).live("keyup",function()
			{
				if($( "#vendorBillPay_groupID" ).val().length==0)
				{
					manufacturer ="";
					$("#vendorBills").jqGrid('GridUnload');
					loadVendorBillPay(manufacturer);
					var html = '<div style="margin-top: -5px;"><img src="../resources/Icons/copyQuote.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="addNewVendorInvoice();">'+
					'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="addNewVendorInvoice();">New Bill</label>'+
						'<input type="hidden" id="invStatus">'+
					'<input type="checkbox" style="margin-top: 10px;width: 20px;height: 14px;" id="chk_termsDiscount" onclick="">'+
				'<label style="vertical-align: super;position: relative;top: 2px;margin-left:-7px;">Take Terms Discount</label>'+
				'<img src="../resources/images/search.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openEditDialog();">'+
				'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openEditDialog();">Edit Bill</label>'+
				'<img src="../resources/images/lineItem_new.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openvendorQuickPay();">'+
				'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openvendorQuickPay();">Quick Pay</label>'+
				'</div>';
				$("#vendorBillsPager").html('');
				$("#vendorBillsPager").html(html);
					$("#vendorBills").trigger("reloadGrid"); 
					$("#vendorBillPay_rxmasterID").val("");
				}
			})

			 var cache = {}; var lastXhr='';
			$( "#vendorBillPay_groupID" ).autocomplete({ minLength: 3,timeout :1000,
				open: function(){ $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");	},
				select: function (event, ui) {
					var manufacturer = ui.item.manufactureID;
					$("#vendorBills").jqGrid('GridUnload');
					loadVendorBillPay(manufacturer);
					var html = '<div style="margin-top: -5px;"><img src="../resources/Icons/copyQuote.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="addNewVendorInvoice();">'+
						'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="addNewVendorInvoice();">New Bill</label>'+
							'<input type="hidden" id="invStatus">'+
						'<input type="checkbox" style="margin-top: 10px;width: 20px;height: 14px;" id="chk_termsDiscount" onclick="">'+
					'<label style="vertical-align: super;position: relative;top: 2px;margin-left:-7px;">Take Terms Discount</label>'+
					'<img src="../resources/images/search.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openEditDialog();">'+
					'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openEditDialog();">Edit Bill</label>'+
					'<img src="../resources/images/lineItem_new.png" style="margin-top: 0px;width: 20px;height: 14px;cursor: pointer;" onclick="openvendorQuickPay();">'+
					'<label style="vertical-align: super;position: relative;top: 2px;cursor: pointer;" onclick="openvendorQuickPay();">Quick Pay</label>'+
					'</div>';
					$("#vendorBillsPager").html('');
					$("#vendorBillsPager").html(html);
					$('#vendorBills').jqGrid('setGridParam',{postData:{'manufacturerID' : manufacturer }});
					$("#vendorBills").trigger("reloadGrid"); 
					$("#vendorBillPay_rxmasterID").val(manufacturer);
				},
				source: function( request, response ) { var term = request.term;
					if ( term in cache ) { response( cache[ term ] ); 	return; 	}
					lastXhr = $.getJSON( "search/getVendorBillPayList", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) { $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading"); }
			
			}); 
		});

		function openAddPayDialog() {
			jQuery("#openAddPayDig").dialog("open");
			return true;
		}

	function removePayBills(rowid)
	{
		var grid = $("#vendorBills");
		var rowId = grid.jqGrid('getGridParam', 'selrow');	
		var aVeBillID = grid.jqGrid('getCell',rowid, 'veBillID');

		var aDetails = "veBillID="+aVeBillID;
		
		$.ajax({
			url: "./vendorscontroller/removeBillPayDetails",
			type: "POST",
			data : aDetails,
			success: function(data) {

			/*	var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Red;">Payment Cancelled</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Cancellation", 
										buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");
										$("#vendorBills").trigger("reloadGrid");  }}]}).dialog("open"); */
				$("#vendorBills").trigger("reloadGrid"); 
				loadVebillPaydetails();
			}

		
		});

		
	}

		
	function saveAddPayDigfromimageclick(billAmt,discAmt,rowid){

		console.log("1"+billAmt);
		
			var grid = $("#vendorBills");
			var rowId = grid.jqGrid('getGridParam', 'selrow');			
			var aVeBillID = grid.jqGrid('getCell',rowid, 'veBillID');
			var existingApplyingAmount =  grid.jqGrid('getCell', rowid, 'applyingAmount').replace(/[^0-9\.]+/g,"");
		

			console.log("2");			
			var isAlreadyExist = false;
			if(existingApplyingAmount > 0){
				isAlreadyExist = true;
			} else {
				isAlreadyExist = false;
			}
			console.log("3");	
			var aDisCount = discAmt;
			var aPayingAmount =parseFloat(billAmt);
			console.log("4");	

				var aDetails = "veBillID="+aVeBillID+"&disCountPrice="+aDisCount+"&payingAmount="+aPayingAmount+"&isAlreadyExist="+isAlreadyExist+"&moAccountId="+$("#bankAccount").val();
					console.log("5" +aDetails);		
					
					$.ajax({
						url: "./vendorscontroller/addBillPayDetails",
						type: "POST",
						data : aDetails,
						success: function(data) {
						loadVebillPaydetails();							
						return true;
						},
						error: function(jqXHR, textStatus, errorThrown){
						}
					});
					document.getElementById("addPayFormID").reset();
		
		}

		
		function saveAddPayDig(){
						
			var grid = $("#vendorBills");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var aVeBillID = grid.jqGrid('getCell', rowId, 'veBillID');

			var isAlreadyExist = false;

			
			var existingApplyingAmount = grid.jqGrid('getCell', rowId, 'applyingAmount').replace("$","");
			var existingBillAmount = grid.jqGrid('getCell', rowId, 'balance').replace("$","");

			
			var aDisCount = $("#discount1_ID").val().replace(/[^0-9\.]+/g,"");
			var aPayingAmount = $("#Paying_ID").val().replace(/[^0-9\.]+/g,"");
			
			console.log(parseFloat(existingBillAmount) +'  '+parseFloat($("#Paying_ID").val().replace(/[^0-9\.]+/g,"")));

	 		if(parseFloat(existingBillAmount)<0)
				{
	 			partialCredit=true;
	 			aPayingAmount = "-"+aPayingAmount;
				}
			
			if(parseFloat(existingBillAmount) > 0 && parseFloat($("#Paying_ID").val().replace(/[^0-9\.]+/g,"")) <= 0)
				{
				console.log('inside false');
				negStatus=false;
				}
				

			if(negStatus && partialCredit)
			{				
			if(existingApplyingAmount != 0){
				isAlreadyExist = true;
			} else {
				isAlreadyExist = false;
			}
			
			
			var aDetails = "veBillID="+aVeBillID+"&disCountPrice="+aDisCount+"&payingAmount="+aPayingAmount+"&isAlreadyExist="+isAlreadyExist+"&moAccountId="+$("#bankAccount").val();
			$.ajax({
				url: "./vendorscontroller/addBillPayDetails",
				type: "POST",
				data : aDetails,
				success: function(data) {

					createtpusage('Company-Vendors-Pay Bills','Save Payment Details','Info','Company-Vendors-Pay Bills,Saving Bills,VeBillID:'+aVeBillID);
					
					jQuery("#openAddPayDig").dialog("close");
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Payment Details Added Successfully.</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
											buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
											$("#vendorBills").trigger("reloadGrid");  }}]}).dialog("open");
					
						return true;
				},
				error: function(jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error:'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
					
					
					
				}
			});
				}
			else
				{
				jQuery("#openAddPayDig").dialog("close");

				if(partialCredit==false)
					{
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Partial Credit Not Allowed</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
					}
				else
					{
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Negative Value Not Allowed</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Fatal Error", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
					}

				negStatus=true;
				partialCredit=true;
				
				}
			document.getElementById("addPayFormID").reset();
				
			
		}
		
		function cancelAddPayDig(){
			jQuery("#openAddPayDig").dialog("close");
			return false;
		}

		jQuery(function(){
			jQuery("#openAddPayDig").dialog({
				autoOpen:false,
				title:"Payment Details",
				width: "350",
				modal:true,
				close:function(){ return true;}	
			});
		});
		var loadVebillPaydetails = function() {
			$.ajax({
				url: "./veInvoiceBillController/getvebillpaylist",
				type: "POST",
				data:{"moAccountID":$("#bankAccount").val()},
				success: function(data) {
					var applyingAmount = 0;
					for (var index = 0; index < data.length; index++) {
						applyingAmount = applyingAmount + Number(data[index].applyingAmount);
					}
					//alert(data[0].applyingAmount);
					$("#billsApprovedAmountId").val(formatCurrency(-applyingAmount));
		 			
					return true;
				},
				error: function(jqXHR, textStatus, errorThrown){
					var errorText = $(jqXHR.responseText).find('u').html();
					if($("#bankAccount").val()==null){
						errorText = " There are no active bank accounts."
						}
					
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Red;">Error:'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning.", 
											buttons: [{height:35,text: "OK",click: function(){ $(this).dialog("close");}}]
										}).dialog("open");
				}
			});
		};
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
	<div id="BillPayVendorInvoice" style="display: none;">
	<jsp:include page="./vendor/vendorinvoicelist.jsp"></jsp:include> 
	</div>
	
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/vendorinvoicelist.js"></script>
</html>