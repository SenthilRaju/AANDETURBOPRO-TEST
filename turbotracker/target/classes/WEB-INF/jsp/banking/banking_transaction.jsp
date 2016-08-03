<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

								<div style="display: none;">
									<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
									<input type="hidden" name="hiddenmoaccountid" id="hiddenmoaccountid" />
								</div>
							
			<div id="transactionDetailsDiv">
								   <table style="width:979px;margin:0 auto;">
									<tr>
										<td>
											<div id="transactionDetailsDiv" style="padding: 0px;">
												<form id="transactionDetailsFromID">
													<table align="center">
														<tr height="10px;"></tr>
														<tr>
															<td>
																<fieldset  class= " ui-widget-content ui-corner-all" style="width: 300px;">
																	<legend><label><b>Account</b></label></legend>
																	<table>
																		<tr>
																			<td><label>Name: </label></td>
																			<td><label id="description_ID"></label></td>
																		</tr>
																		<tr height="15px;"></tr>
																		<tr>
																			<td><label>Type: </label></td>
																			<td><label id="type_ID"></label></td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="padding-right: 40px;">
																<fieldset  class= " ui-widget-content ui-corner-all" style="width: 300px;">
																	<legend><label><b>Balance</b></label></legend>
																	<table>
																		<tr>
																			<td><label>Current Balance: </label></td>
																			<td><label id="currentBalance_ID"></label></td>
																		</tr>
																		<tr height="15px;"></tr>
																		<tr>
																			<td><label>Resulting Balance: </label></td>
																			<td><label id="resultingBalance_ID"></label></td>
																		</tr>
																	</table>
																</fieldset>
															</td>
															<td style="padding-right: 20px;">
																<input type="button" value="Transaction Filter" onclick="transactionDetails()" class="savehoverbutton turbo-blue">
															
															</td>
														</tr>
														<tr height="10px;"></tr>
														<tr>
															<td style="padding-right: 20px;" colspan="3">
																<table id="transactionRegisterGrid"></table>
																<div id="transactionRegisterGridPager"></div>
															</td>
														</tr>
														<tr style="display: none;">
															<td>
																<input type="text" id="bankingPerPage" value="${requestScope.userDetails.bankingperpage}">
															</td>
													</tr>
													</table>
													<br>
													<hr>
													<table align="right" style="padding-right: 20px;">
														<tr>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:100px;" value="New Check" onclick="newCheck()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="New Transaction" onclick="newTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="Void Check" onclick="voidTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:120px;" value="Edit Transaction" onclick="editTransaction()"></td>
															<td><input type="button" class="savehoverbutton turbo-blue" style=" width:130px;" value="Delete Transaction" onclick="reasonfordelete('delete')"></td>
														</tr>
													</table>
												</form>
											</div>
											<!-- 
											Created by: Leo  Date: 27-10-2014
											Description: New Check
											
											 -->
											
												<div id="newCheckDetails" style="display: none;">
												<form id="newCheckFormID" name="newCheckFormName">
													<table style="width: 645px;">
													
													
														<tr>
															<td colspan ="2">
																<label style="font-size: 15px;font-weight: bold;" id="newCheckAccountNameID"></label><br>
																<label style="font-size: 12px;font-weight: bold; font-style: italic;" id="newCheckAccountTypeID"></label>
															</td>
															<td style="width:90px;">
															<label style="font-weight: bold;">CHECK NO.<label style="color: red;">*</label></label>
															</td>
															<td  align="right">
																<!-- <label style="color: red;">*</label> -->
																<input type="text" id="checkNo" class="alpha" name="checkNo" style="width:155px;" maxlength="15">
															</td>
														</tr>
														
														
														
														<tr>
															<td colspan="4" align="right">
																<input type="text" id="checkdateID" readonly="readonly" name="checkdateName" class="datepicker" style="width:155px;margin-top:-4px;">
															</td>
														</tr>
														
														
														<tr>
															<td style="width:90px;font-weight: bold;"><label>PAY TO:</label></td>
															<td>
															<input type="hidden" id="paytomasterID" name="paytomasterIDName" style="width:246px;"maxlength="30">
																<input type="text" id="paytoID" name="paytoName" style="width:246px;"maxlength="30">
																<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="On Hand" style="width:13px;">
															</td>
															<td ><label style="font-weight: bold;">DOLLARS:<label style="color: red;">*</label></label></td>
															<td>
																<input type="text" id="checkamountID" name="checkamountName" class="validate[custom[number]] number" style="width:155px;text-align:right" maxlength='11' >
																<input type="hidden" id="oldcheckamt">
																<label id="payableApprovedID" style="display: none"></label>
															</td>
														</tr>
														
														<tr>
															<td><label style="font-weight: bold;">MEMO:</label></td>
															<td colspan="2"><input type="text" id="memoID" name="memoName" style="width:246px;"maxlength="30">
															<span style="display:none"><input type="text" id="checkRxMasterID" name="checkRxMasterName" style="width:246px;"maxlength="30">
															<input type="text" id="moTrnsID" name="moTrnsName" style="width:246px;"maxlength="30">
															</span>
															
															</td>
															<td>
															<label style="vertical-align: middle;">Reconciled? </label>
															<input type="checkbox" id="reconciledCheckID" style="vertical-align: middle;">
															</td>
														</tr>
														
														<tr>
															<td colspan="4" >
															<label style="font-size: 15px;font-weight: bold;" >Misc Check</label>
															</td>
														</tr>
														
														<tr>
															<td ><label id="glaccountChecklabel" style="vertical-align: -5px;">GL Account:<label style="color: red;">*</label></label></td>
															<td>
																<select id="glAccountCheckID" name="glAccountCheckName" style="width:60%;vertical-align: -5px;">
																	<option value="-1"> - Select - </option>
																	<c:forEach var="intrestAccountBean" items="${requestScope.coAccountsDetails}">
																		<option value="${intrestAccountBean.coAccountId}">
																			<c:out value="${intrestAccountBean.number}" ></c:out> -<c:out value="${intrestAccountBean.description}" ></c:out>
																		</option>
																	</c:forEach>
																</select>   
															</td>
																
															<td colspan="2"><div id="newcheckDetailsDiv" style="color: red;"></div></td>
															</td>
														</tr>
													</table>
													<hr>
													<table align="right">
														<tr>
															<td>
															   <input type="button" id="savecheckPrintButton" name="savecheckprintButtonName" value="Print" class="savehoverbutton turbo-blue" onclick="printcheckDetails()" style=" width:75px;">
																<input type="button" id="savecheckButton" name="savechecksaveButtonName" value="Write Check" class="savehoverbutton turbo-blue" onclick="savecheckDetails()" style=" width:120px;">
																<input type="button" id="editcheckButton" name="editchecksaveButtonName" value="Write Check" class="savehoverbutton turbo-blue" onclick="reasonfordelete('newcheckedit')" style=" width:120px;">
															</td>
														</tr>
													</table>
												</form>
											</div>
											
											
											
											
											<div id="newTransactionDetails" style="display: none;">
												<form id="newTransactionFormID" name="newTransactionFormName">
													<table style="width: 480px;">
														<tr>
															<td>
																<label style="font-size: 15px;font-weight: bold;" id="newAccountNameID"></label><br>
																<label style="font-size: 12px;font-weight: bold;" id="newAccountTypeID"></label>
															</td>
															<td align="right">
																<label style="font-weight: bold;font-size: 30px;">BANK <br> TRANSACTION</label>
															</td>
														</tr>
														<tr><td colspan="2"><div id="newTransactionDetailsDiv" style="color: red;"></div></td></tr>
														<tr>
															<td><label>Type:</label></td>
															<td>
																<input type="radio" id="depositID" name="typeName" value="Deposit" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Deposit</label>
																<input type="radio" id="withdrawalID" name="typeName" value="Withdrawal" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Withdrawal</label>
																<input type="radio" id="feeID" name="typeName" value="Fee" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Fee</label>
																<input type="radio" id="interestID" name="typeName" value="Interest" style="vertical-align: middle;">
																<label style="vertical-align: middle;">Interest</label>
															</td>
														</tr>
														<tr>
															<td><label>Date:</label><label style="color: red;">*</label></td>
															<td><input type="text" id="dateID" readonly="readonly" name="dateName" class="datepicker"></td>
														</tr>
														
														<tr>
															<td><label>Reference:</label></td>
															<td colspan="4"><input type="text" id="referenceID" name="referenceName"></td>
														</tr>
														<tr>
															<td><label>Description:</label></td>
															<td>
																<input type="text" id="descriptionIDs" name="descriptionName">
													  		</td>
														</tr>
														<tr >
															<td><label id="amountlabelid" style="display: inline-block;">Amount:</label><label style="color: red;">*</label><input type="hidden" id="amounthiddenID"></td>
															<td colspan="4"><input type="text" id="amountID" name="amountName" class="validate[custom[number]]"></td>
														</tr>
														<tr>
															<td><label id="glaccountlabel">GL Account:</label></td>
															<td>
																<select id="glAccountID" name="glAccountName" style="width:38%">
																	<option value="-1"> - Select - </option>
																	<c:forEach var="intrestAccountBean" items="${requestScope.coAccountsDetails}">
																		<option value="${intrestAccountBean.coAccountId}">
																			<c:out value="${intrestAccountBean.number}" ></c:out> -<c:out value="${intrestAccountBean.description}" ></c:out>
																		</option>
																	</c:forEach>
																</select>   
																<input type="button" id="multipleAccountID" value="Multiple Accounts" class="savehoverbutton turbo-blue"  onclick="openmultipleaccountforonlybutton()" style="display: inline-block;">
																<input type="button" id="multipleAccountIDgrey" value="Multiple Accounts" class="turbo-grey"  onclick="openmultipleaccountforonlybutton()" style="display: none;">
																<label style="vertical-align: middle;">Reconciled? </label>
																<input type="checkbox" id="reconciledID" style="vertical-align: middle;">
															</td>
														</tr>
														<tr align="center">
														<td colspan="2" >
														<table id="glAccountsGrid" ></table>
														<div id="glAccountsGridPager" ></div>
														</td>
														</tr>
														<tr align="right">
														<td colspan="2" >
														<label id="totallabelid" style="display: none;">Total:</label>&nbsp;
														<input type="text"  id="glaccounttotalID" value="$0.00" style="display: none;" onclick="setgridtotal()" readonly="readonly">
														</td>
														</tr>
													</table>
													<hr>
													<table align="right">
														<tr>
															<td>
																<input type="button" id="saveAddButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveTransactionDetails()" style=" width:120px;">
																<input type="button" id="saveEditButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="reasonfordelete('edit')" style=" width:120px;">
																<input type="button" id="saveDeleteButton" name="saveUserButtonName" value="Delete" class="savehoverbutton turbo-blue" onclick="deleteTransactionDetails()" style=" width:120px;">
																<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="savehoverbutton turbo-blue" onclick="closeTransactionDetails()" style="width: 80px;">
															</td>
														</tr>
													</table>
												</form>
											</div>
											<div id="reasondialog" style="display: none;">
											<form name="reasonformname">
											<table>
											<tr><td>Reason:<label style="color: red;">*</label></td><td><textarea id="reasonttextid" name="reasonttextname" cols="35" rows="5"></textarea></td></tr>
											<tr><td colspan="2"><label id="errordivreason" style="color: red;"></label></td></tr>
											</table>
											</form>
											</div>
											<div id="findbytransactionRegisterGriddiv" style="display: none;">
											<table>
											<tr>
													<td style="padding-right: 20px;" colspan="3">
														<table id="findbytransactionRegisterGrid"></table>
														<div id="findbytransactionRegisterGridPager"></div>
													</td>
											</tr>
											</table>
											</div>
											<div id="transactionDetailsDialog" style="display: none;">
										<form id="transactionDetailsForm">
											<div>
												<table>
												<tr>
                                                                <td><label>Find</label></td>
                                                                <td>
                                                                    <select id="transactionfilterSelectBox1">
                                                                        <option value=''>--Select--</option>
                                                                        <option value='Amount'>Amount</option>
                                                                        <option value='Cleared Status'>Cleared Status</option>
                                                                        <option value='Date'>Date</option>
                                                                        <option value='Description'>Description</option>
                                                                        <option value='G/L Account #'>G/L Account #</option>
                                                                        <option value='Reference'>Reference</option>
                                                                        <option value='Type'>Type</option>
                                                                    </select>
                                                                </td>
                                                                <td>
                                                                    <select id="transactionfilterSelectBox2">
                                                                        <option value=''>--Select--</option>                                                                       
                                                                    </select>
                                                                </td>
                                                                 <td>
                                                                 <input type="text" name="transactionfiltertextboxID" id="transactionfiltertextboxID" style="display: none;" maxlength="40" />
                                                                 </td>
                                                                  <td>
                                                                  <select id='transfilTBdynamic' name="transfilTBdynamic" style="display: none;">
                                                                  	<option value=''>--Select--</option> 
                                                                   </select> 
                                                                  </td>
                                                                  <td>
                                                                   <input type='text' style='width: 75px;' id="transactionfilterfromdate" name="transactionfilterfromdate" placeholder='From Date' style="display: none;"/>
                                                                   
                                                                </td>
                                                                <td>
                                                                <input type='text' style='width: 75px;' id="transactionfiltertodate" name="transactionfiltertodate" placeholder='To Date' style="display: none;"/>
                                                                </td> 
                                                                <td>
                                                                    <!-- <input type="button" id="transactionDetailsID" value="Find" onclick="FindByTransactionDetails()" class="cancelhoverbutton turbo-blue"> -->
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td  colspan="8">
                                                                   &nbsp;&nbsp;&nbsp;&nbsp; <input id="transactionfilterCheckBox" type="checkbox"><label>Search Backwards</label>
                                                                </td>
                                                            </tr>
                                                            <tr></tr>
                                                            <tr><td colspan="8" id="findbytderrordiv" style="color: red"></td></tr>
                                                            
                                                            <tr>
                                                                <td   colspan="8">
                                                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="transactionDetailsID" value="Find" onclick="FindByTransactionDetails()" class="cancelhoverbutton turbo-blue"> &nbsp;&nbsp;<input type="button" id="trasactionCancelID" value="close" onclick="cancelTransactionDetails()" class="cancelhoverbutton turbo-blue">
                                                                </td>
                                                            </tr>
<!-- 												  <tr> -->
<!-- 												  		<td><label>Bank Account: </label></td> -->
<!-- 												  		<td> -->
<!-- 												  			<select id="bankAccountID" name="bankAccountName"> -->
<%-- 																<c:forEach var="bankingDetailsBean" items="${sessionScope.bankingDetails}"> --%>
<%-- 																	<option value="${bankingDetailsBean.moAccountId}"> --%>
<%-- 																		<c:out value="${bankingDetailsBean.description}" ></c:out> --%>
<!-- 																	</option> -->
<%-- 																</c:forEach> --%>
<!-- 												  			</select> -->
<!-- 												  		</td> -->
<!-- 												  </tr> -->
<!-- 												  <tr> -->
<!-- 												  		<td><label>Transaction Types: </label></td> -->
<!-- 												  		<td> -->
<!-- 												  			<input type="checkbox" id="transactionDepositID" style="vertical-align: middle;"><label style="vertical-align: middle;">Deposit</label> -->
<!-- 												  			<input type="checkbox" id="transactionWidthdrawID" style="vertical-align: middle;"><label style="vertical-align: middle;">Withdrawal</label> -->
<!-- 												  		</td> -->
<!-- 												  	</tr> -->
<!-- 												  	<tr> -->
<!-- 												  		<td></td> -->
<!-- 												  		<td> -->
<!-- 												  			<input type="checkbox" id="transactionCheckID"  style="vertical-align: middle;"><label style="vertical-align: middle;">Check</label> -->
<!-- 												  			<input type="checkbox" id="transactionInterstID"  style="vertical-align: middle;"><label style="vertical-align: middle;">Interest</label> -->
<!-- 												  		</td> -->
<!-- 												  	</tr> -->
<!-- 												  	<tr> -->
<!-- 												  		<td></td> -->
<!-- 												  		<td> -->
<!-- 												  			<input type="checkbox" id="transactionFeeID" style="vertical-align: middle;"><label style="vertical-align: middle;">Fee</label> -->
<!-- 												  		</td> -->
<!-- 												  </tr> -->
<!-- 												  <tr height="10px;"></tr> -->
<!-- 												  <tr> -->
<!-- 												  	<td> -->
<!-- 												  		<label>Data Range: </label> -->
<!-- 												  	</td> -->
<!-- 												  	<td> -->
<!-- 												  		<input type="text" id="fromDateRangeID" name="fromDateRangeName" class="datepicker" style="width: 80px;"> -->
<!-- 												  		<input type="text" id="toDateRangeID" name="toDateRangeName" class="datepicker" style="width: 80px;"> -->
<!-- 												  	</td> -->
<!-- 												  </tr> -->
												 </table>
<!-- 												 <hr> -->
<!-- 												 <table align="right"> -->
<!-- 												 	<tr> -->
<!-- 												 		<td> -->
<!-- 												 			<input type="button" id="transactionDetailsID" value="View Details" onclick="viewTransactionDetails()" class="cancelhoverbutton turbo-blue"> -->
<!-- 												 		</td> -->
<!-- 												 		<td> -->
<!-- 												 			<input type="button" id="trasactionCancelID" value="Cancel" onclick="cancelTransactionDetails()" class="cancelhoverbutton turbo-blue"> -->
<!-- 												 		</td> -->
<!-- 												 	</tr> -->
<!-- 												 </table> -->
											</div>
										</form>
									</div>
									</table>
								</div>
						<script type="text/javascript" src="./../resources/scripts/turbo_scripts/bankingAccounts.js"></script>			
<script>

$(function(){

	 $("#transactionfilterfromdate").datepicker();
	 $("#transactionfiltertodate").datepicker();
	 $("#checkdateID").datepicker();
	 $("#dateID").datepicker();
	 
	 
	loadTransactionRegisterDetails(aTranDetails);
	
	$('input.alpha[$id=checkNo]').keyup(function() { 
		if (this.value.match(/[^a-zA-Z0-9 ]/g)) { 
		this.value = this.value.replace(/[^a-zA-Z0-9 ]/g, ''); 
		} 
		}); 

		$(function() { var cache = {}; var lastXhr='';
		$("#paytoID").autocomplete({ minLength: 1,timeout :1000,
			select: function (event, ui) {
				var aValue = ui.item.value;
				var valuesArray = new Array();
				valuesArray = aValue.split("|");
				var id = valuesArray[0];
				var code = valuesArray[2];
				
				$("#checkRxMasterID").val(aValue);
				
			
				 $.ajax({
				        url: './getManufactureATTN?rxMasterId='+aValue,
				        type: 'POST',       
				        success: function (data) {
				        	$.each(data, function(key, valueMap) {					
								
								if("vendorName"==key)
								{
									$.each(valueMap, function(index, value){
										$('#paytoID').val(value.name);
										$('#paytomasterID').val(value.rxMasterId);
									}); 
									
								}	
							});
							
				        }
				    }); 
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
		}); 

	function printcheckDetails()
	{
		var currentDate = new Date();
		var currentMonth = currentDate.getMonth()+1;
		var currenDate = currentMonth+"/"+currentDate.getDate()+"/"+currentDate.getFullYear();
		var currentminute = currentDate.getMilliseconds();
		var filename = "NewChecks"+currenDate+currentminute+".pdf";

		if($("#moTrnsID").val()!="")
			{
			createtpusage('Banking-Transaction Details','Printing Check','Info','Banking,Printing Check,Check No:'+$("#checkNo").val()+'moTransactionID='+$("#moTrnsID").val());
		window.open("./banking/newCheckDetails?fileName="+filename+"&moTransactionID="+$("#moTrnsID").val());
		setTimeout($.unblockUI, 2000);
			}
		else
			{
				$("#newcheckDetailsDiv").html("Please Write Check then Print");
				setTimeout(function() {
	                $('#newcheckDetailsDiv').html("");
	                }, 3000);
			}
	}
	 function newTransaction(){

		 var grid = $("#bankingAccountsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var inActive = grid.jqGrid('getCell', rowId, 'inActive');
		 if(inActive != 1)
			{
		 document.getElementById("newTransactionDetailsDiv").innerHTML ="";
		 
		 $("#glAccountsGrid").jqGrid('setGridState','visible');
		 $("#transactionRegisterGrid").jqGrid('resetSelection');
		 mulaccbtn_status=1;
		 openmultipleaccount();
		 $("#saveAddButton").show();
		 $("#saveEditButton").css("display", "none");
		 $("#saveDeleteButton").css("display", "none");
		 document.getElementById("newTransactionFormID").reset();
		 $("#glAccountID").val("-1");
		 var currentDate = new Date();
		 
		 getcoFiscalPerliodDate("dateID");
		 
		 $("#dateID").datepicker("setDate",currentDate);
		 $("#newTransactionDetails").dialog("open");
		 $('#depositID').attr("checked",true);
		
		 return false;
			}
		 else
			 {
			 var newDialogDiv = jQuery(document.createElement('div'));
			 var errorText = "Please Enable Bank Account as active.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			 }
	 }
	 function newCheck(){

		 $("#moTrnsID").removeAttr("disabled");
		 $("#checkdateID").removeAttr("disabled");
		 $("#memoID").removeAttr("disabled");
		 $("#checkNo").removeAttr("disabled");
		 $("#paytoID").removeAttr("disabled");
		 $("#checkamountID").removeAttr("disabled");
		 $("#glAccountCheckID").removeAttr("disabled");
		 $("#reconciledCheckID").removeAttr("disabled");
		 
		 var grid = $("#bankingAccountsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var inActive = grid.jqGrid('getCell', rowId, 'inActive');
		 if(inActive != 1)
			{
			 $("#savecheckButton").removeAttr("disabled");
			 document.getElementById("newCheckFormID").reset();
			 $("#newCheckDetails").dialog("open");
			 $("#checkdateID").datepicker( "setDate", new Date());
			 $("#checkNo").val(aCheckno);
			 getcoFiscalPerliodDate("checkdateID");
			 $("#savecheckButton").show();	 
			 $("#editcheckButton").css('display','none');
			 $('#newcheckDetailsDiv').html("");
			 $("#glAccountCheckID option[value=-1]").attr("selected", "selected");
			}
		 else
			 {
			 var newDialogDiv = jQuery(document.createElement('div'));
			 var errorText = "Please Enable Bank Account as active.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:350, height:150, title:"Warning",
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			 }
		 }
		 
	 
</script>								
								