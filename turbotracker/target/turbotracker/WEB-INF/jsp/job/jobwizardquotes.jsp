<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<table>
		<tr><td colspan="2"><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
		<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<table>
		<tr>
			<td>
				<fieldset class= "custom_fieldset" style="padding: 2px 8px; width: 1082px;">
					<legend class="custom_legend"><label><b> Bid List </b></label></legend>
						<div style="background: url('images/ui-bg_inset-soft_100_f4f0ec_1x100.png') repeat-x scroll 50% bottom #F4F0EC;">
							<table id="quotesBidlist"></table>
							<div id="quotesBidlistPager">
							</div>
						</div>
						<div class="loadingDiv" id="loadingDiv"> </div>
					<table>
						<tr>
							<td style="padding-right: 595px;padding-bottom: 8px;">
								<fieldset class= "custom_fieldset" style="FONT-FAMILY: 'Courier 10 Pitch'; padding-bottom: 0px; background:#EEDEBC;">
					            	<table>
					            		<tr>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Bidder" onclick="openAddBid()" style="background: #EEDEBC"></td>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit Bidder" onclick="editBidDetails()" style="background: #EEDEBC"></td>
							            	<td align="right" style="padding: 0 5px 0 1px;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Bidder" onclick="deleteBidDialog()" style="background: #EEDEBC"></td>
					            		</tr>
					            	</table>
				            	</fieldset>
							</td>
					 		<td align="right" style="padding-bottom: 8px;padding-left: 264px;">
								<fieldset class= " custom_fieldset" style="FONT-FAMILY: 'Courier 10 Pitch'; padding-bottom: 0px;width:85px;height:25px;background:none repeat scroll 0 0 #EEDEBC;">
					            	<table >
					            		<tr>
							            	<td align="right" style="padding: 0 7px 0 1px;">	<a onclick="openPDF()">
												<input type="image" src="./../resources/Icons/PDF_new.png" title="Export to PDF" style="background: #EEDEBC">
												</a>
							            	</td>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Quote" style="background: #EEDEBC"onclick="sendEmail()"></td>
							            	<td align="right" style="padding: 0 5px 0 1px;"><input type="image" src="./../resources/Icons/fax_new.png" title="Fax Quote" onclick="faxQuote()" style="background: #EEDEBC"></td>
							            	<!--  <td align="right" style="padding: 0 5px 0 1px;"><input type="image" src="./../resources/images/lineItem_new.png" title="Fax Quote" onclick="reportCriteriaPage()" style="background: #EEDEBC"></td> -->
					            		</tr>
					            	</table>
				            	</fieldset>
							</td> 
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td valign="top" style="vertical-align:top;">
				<fieldset class= "custom_fieldset" style="padding: 2px 8px; width: 1085px;">
					<legend class="custom_legend"><label><b> Quotes </b></label></legend>
					<div>
						<table id="quotes"></table><div id="quotesPager"></div>
					</div>
					<table align="right">
						<tr>
						<td style=" padding-right: 880px;padding-bottom: 8px;">
								<fieldset class= "custom_fieldset" style="FONT-FAMILY: 'Courier 10 Pitch'; padding-bottom: 0px; background:#EEDEBC;">
					            	<table>
					            		<tr>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/plus_new.png" title="Add Quote" onclick="addquotesdialog()" style="background: #EEDEBC"></td>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/edit_new.png" title="Edit Quote" onclick="editQuoteDetails()" style="background: #EEDEBC"></td>
							            	<td align="right" style="padding: 0 7px 0 1px;"><input type="image" src="./../resources/Icons/delete_new.png" title="Delete Quote" onclick="deleteQuickQuoteDialog()" style="background: #EEDEBC"></td>
							            	<td align="right" style="padding: 0 5px 0 1px;"><input type="image" src="./../resources/Icons/copy_new.png" title="Copy Quote" onclick="copyQuickQuote()" style="background: #EEDEBC"></td>
							            	<td><input type="image" src="./../resources/images/QuoteProperties.png" title="Quote Properties" onclick="setQuotePorperties()" style="background: #EEDEBC;vertical-align: middle;"></td>
					            		</tr>
					            	</table>
				            	</fieldset>
							</td>
							<td align="right" style="padding-bottom: 8px; ">
								<fieldset class= "custom_fieldset" style="padding-bottom: 0px; background:#EEDEBC;">
					            	<table>
					            		<tr>
							             	<td align="right" style="padding: 0 3px 0 1px;">	<a onclick="viewPDF()">
												<input type="image" src="./../resources/Icons/PDF_new.png" title="View Quote" onclick="" style="background: #EEDEBC">
												</a>
							            	</td> 	
					            		</tr>
					            	</table>
				            	</fieldset>
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<table style="width:1110px">
		<tr>
			<td style="vertical-align: top;">
				<fieldset class= " custom_fieldset" style="width: 248px;padding: 10px;height: 160px;">
						<legend class="custom_legend"><label><b>Amount</b><input type="checkbox" id="isAmount" onchange="Amount()" style="vertical-align: middle;display: none;" checked="checked"/></label></legend>
						<table style="padding-top: 15px;" id="amounthiiden" align="center">
							<tr>
								<td style="width: 150px;"><label>Contract Amount:</label></td>
								<td><input type="text" style="width: 90px;" id="contractAmount"  onclick="trimContractAmount()"  onkeyup="contractAmountAdminValidation(this)"/></td>
							</tr>
							<tr>
								<td style="width: 150px;"><label>Estimated Cost:</label></td>
								<td><input type="text" style="width: 90px;" id="estimatedCost"   onclick="trimEstimatedCost()"  onkeyup="estimateCostAdminValidation(this)"/></td>
							</tr>
							<tr>
								<td style="width: 150px;"><label>Estimated Profit:</label></td>
								<td><input type="text" style="width: 90px;" id="estimatedProfit" onclick="estimateProfit()"  onkeyup="estimateProfitAdminValidation(this)" /></td>
							</tr>
						</table>
						<table>
							<tr align="right">
								<td width="220px"><input type="button" class="cancelhoverbutton turbo-tan" style="width: 70px;" onClick="saveJobAmount()" value="Save"/></td>
							</tr>
						</table>
				</fieldset>
				<div style="padding-top: 10px;">
					<input type="button"  value=" Plan & Spec" class="cancelhoverbutton turbo-tan" onclick="openPlanandSpec()" style="width: 130px;">
					<input type="button"  value=" Prior Approval" class="cancelhoverbutton turbo-tan" onclick="openPriorApproval()" style="width: 130px;">
					<input type="button"  value=" Source" class="cancelhoverbutton turbo-tan" onclick="openSource()" style="width: 130px;">
					<input type="button"  value=" Addendums" class="cancelhoverbutton turbo-tan" onclick="openAddendums()" style="width: 130px;">
				  <!-- <input type="button"  value=" Amount" class="cancelhoverbutton turbo-blue" onclick="openAmount()" style="width: 90px;"> -->
				</div>
			</td>
			<td style="width: 550px;vertical-align: top;">
				<fieldset class= " custom_fieldset" style="padding-bottom: 6px;">
					<table id="quoteTemplates"></table>
					<table>
						<tr>
							<td>
								<div style="width: 125px; border-radius: 6px; border: 1px solid rgb(160, 82, 45); padding-top: 4px; margin-top: 5px; margin-left: 7px;">
									<img src='./../resources/Icons/plus_new.png' title='Inline Item' onclick="openAddQuoteTemplateDialog()" style='padding:1px 4px;'>
									<img src='./../resources/Icons/edit_new.png' title='Inline Item' onclick='openEditQuoteTemplateDialog()' style='padding:1px 4px;'>
									<img src='./../resources/Icons/delete_new.png' title='Inline Item' onclick='deleteQuoteTemplateDialog()' style='padding:1px 4px;'>
									<img src="./../resources/images/QuoteProperties.png" title="Quote Properties" onclick="setQuoteTemplateProperties()" style=padding:1px 4px;>  
								</div>
							</td>
							<td><input type="button"  value="Create Quote" class="cancelhoverbutton turbo-tan" onclick="createQuoteFromTemplate()" style="width: 130px;"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	<hr style="width:1100px; color:#C2A472;">
	<table style="width:1110px">
		<tr>
			<td align="right">
				<input type="button" value="Save" class="savehoverbutton turbo-tan"  onclick="" style="width:80px">
			</td>
		</tr>
	</table>
	<label id="joQuoteheader" style="display: none;"></label>
	<div ><jsp:include page="../addquotes.jsp"></jsp:include></div>
	<div ><jsp:include page="../job/quote_template_details.jsp"></jsp:include></div>
	<div ><jsp:include page="../requestSubstitution.jsp"></jsp:include></div>
	<div id=planandSpecDialog>
		<form action="" id="planandSpecFormID">
			<table>
			  	<tr>
					<td>
						<fieldset class= "custom_fieldset" style="width: 235px;height:135px; padding-bottom: 0px; padding-top: 0px;">
							<legend class="custom_legend"><label><b style="vertical-align: middle;">Plan & Spec </b> <input type="checkbox" id="isPlanAndSpecJob" onchange="planAndSpecJobChange()" style="vertical-align: middle;" checked="checked" /> </label></legend>
							  	<table id="planAndSpecJobTable">
									<tr>
										<td colspan="2"></td>
									</tr>
									<tr>
										<td><label class="">Bin #</label></td>
										<td><input type="text" id="bin_number" name="bin_number_name" style="width: 130px" value="${requestScope.joMasterDetails.binNumber}"></td>
									</tr>
									<tr>
										<td><label class="">Plan Date: </label></td>
										<td><input type="text" id="plan_date_format" name="plan_date_name" class="datepicker" style="width: 130px"></td>
									</tr>
									<tr>
										<td style=" "><label class="">Plan #'s: </label></td>
										<td><input type="text" id="plan_nuber_id" name ="plan_nuber_name" value="${requestScope.joMasterDetails.planNumbers}"  style="width: 130px"></td>
									</tr>
								</table>
						</fieldset>
						<hr width="250px;">
						<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="planAndSpec()" style="FONT-FAMILY: 'Bitstream Charter'; width : 134px;">
				</td>
			</tr>
		</table>
	</form>
	</div> 
	<div id="priorApprovalDialog">
		<table>
			<tr>
				<td>
					<fieldset class= "custom_fieldset" style="width: 180px; height: 123px;">
						<legend class="custom_legend"><label><b style="vertical-align: middle;">Prior Approval</b><input type="checkbox" id="isPriorApproval" onchange="PriorApproval()" style="vertical-align: middle;display: none;" checked="checked"/></label></legend>
							<table id="priorApproval">
								<tr>
									<td style="vertical-align:top;">
										<label>Required</label>&nbsp;<input type="checkbox" style="vertical-align:middle;"/>
									</td>
								</tr>
								<tr align="center" style="width:100px">
									<td style="vertical-align:top;" colspan="3" align="left">
										<input type="button" value="Request for Substitution" class="requestSubstitution turbo-tan" onclick="requestsubstitutiondialog()">
									<td>
								</tr>
								<tr>
									<td style="vertical-align:top;"><label>Done</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="checkbox" style="vertical-align:middle;" id="donecheck" onclick="done()" />
										<input type="text" class="datepicker" style="width:80px" id="donedate">
									</td>
								</tr>
								<tr>
									<td style="vertical-align:top;"><label>Granted</label>&nbsp;
										<input type="checkbox" style="vertical-align:middle;" id="grantedcheck" onclick="granted()"/>
										<input type="text" class="datepicker" style="width:80px" id="granteddate">
									</td> 
								</tr>
						<tr>
							<td></td>
						</tr>
					</table>
				</fieldset>
			 	<hr width="200px;">
					<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="closeApproval()" style="FONT-FAMILY: 'Bitstream Charter'; width : 134px;">
			</td>
		</tr>
	</table>
	</div>
	<div id="sourceDialog">
		<table>
			<tr><td>
				<fieldset class= "custom_fieldset" style="width:179px; height: 130px;">
					<legend class="custom_legend"><label><b>Source</b><input type="checkbox" id="isSource" onchange="Source()" style="vertical-align: middle;display: none;" checked="checked"/></label></legend>
					<table style="width: 180px;" id="sourcehidden">
						<tr>
							<td colspan="2" style="vertical-align:middle"><label>Dodge</label></td>
							<td colspan="2"><input type="checkbox" style="vertical-align:middle;" id="sourceDodge"/></td>
							<td style="width:10px"></td>
							<td colspan="2" style="vertical-align:middle"><label>ISqFT</label></td>
							<td colspan="2"><input type="checkbox" style="vertical-align:middle;" id="sourceISqft"/></td>
						</tr>
						<tr>
							<td colspan="2" style="vertical-align:middle"><label style="padding-right: 10px;">LDI</label></td>
							<td colspan="2"><input type="checkbox" style="vertical-align:middle;" id="sourceLDI"/></td>
							<td style="width:10px"></td>
							<td colspan="2" style="vertical-align:middle"><label>Other</label></td>
							<td colspan="2"><input type="checkbox" style="vertical-align:middle;" id="sourceOther"/></td>
						</tr>
					</table>
					<table style="width: 180px;" id="reporthidden">
						<tr>
							<td><label>Report #: </label></td>
							<td><input type="text" style="width: 80px;" value="${requestScope.joMasterDetails.sourceReport1}"/></td>
						</tr>
						<tr>
							<td><label>Other: &nbsp;</label></td>
							<td><input type="text" style="width: 80px;" value="${requestScope.joMasterDetails.otherSource}"/></td>
						</tr>
					</table>
				</fieldset>
				<hr width="200px;">
					<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="closeSource()" style="FONT-FAMILY: 'Bitstream Charter'; width : 134px;">
			</td></tr>
		</table>
   </div>
   <div id="addendumsDialog">
   	  <form action="" id="addendumsForm">
		   <table>
			   <tr>
				   <td>
						<fieldset class= "custom_fieldset" style="width: 190px; height: 126px">
							<legend class="custom_legend"><label><b>Addendums</b><input type="checkbox" id="isAddendums" onchange="Addendums()" style="vertical-align: middle;display: none;" checked="checked"/></label></legend>
								<table style="padding-top: 15px;" id="addendums">
									<tr>
										<td>
											<label>Received:</label>
										</td>
										<td>
											<input type="text" id="received_id" name="received_name" style="width: 80px;" value="${requestScope.joMasterDetails.addendumReceived}"/>
										</td>
									</tr> 
									<tr>
										<td>
											<label>Quote Thru:&nbsp;</label>
										</td>
										<td>
											<input type="text" style="width: 80px;" id="quoteThru_id" name="quoteThru_name" value="${requestScope.joMasterDetails.addendumQuotedThru}"/>
										</td>
									</tr>
							</table>
						</fieldset>
						<hr width="200px;">
						<input type="button" class="savehoverbutton turbo-tan" value="Save & Close" onclick="updateAddendums()" style="FONT-FAMILY: 'Bitstream Charter'; width : 134px;">
				</td>
			</tr>
		</table>
	 </form>
	</div>
	 <div id="BidDialogCustom">
	 	<form action="" id="BidDialogCustomForm">
			<table>
				<tr id="bidderIDTD">	
					<td width="100px;">
						<b>BidderID:</b>
					</td>
					<td>
						<input type="text" id="bidderId" name="bidderId" style="width:300px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Bidder:<span style="color:red;"> *</span></b>
					</td>
					<td>
						<input type="text" id="bidder" name="bidder"  class="validate[required]" style="width:240px;margin-left: 3px;"  placeholder="Minimum 3 characters required">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" style="padding-left: 10px;">
					</td>
				</tr>
				<tr id="lowBiddderID">
					<td width="100px;">
						<b>Low:</b>
					</td>
					<td>
						<input type="checkbox" id="low" name="low" style="margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Contact:</b>
					</td>
					<td id="contactselectID">
						<select style="width:227px;margin-left: 3px;border:#DDBA82;" id="contacthiddenID" onchange="getContactId()"><option value="-1"></option></select>
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Type:<span style="color:red;"> *</span></b>
					</td>
					<td>
						<select style="width:170px;margin-left: 3px;border:#DDBA82;" id="customer_quoteType" name="quoteType" class="validate[required]" >
							<option value=""> - Select - </option>
							<c:forEach var="cuTypeBean" items="${requestScope.customerType}">
								<option value="${cuTypeBean.cuMasterTypeId}">
									<c:out value="${cuTypeBean.code}" ></c:out>
								</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr id="lastQuoteBidderID">
					<td width="100px;">
						<b>Last Quote:</b>
					</td>
					<td>
						<input type="text" class="" id="lastQuote" name="lastQuote" style="width: 130px;margin-left: 3px;" readonly>
					</td>
				</tr>
				<tr id="revBidderID">
					<td width="100px;">
						<b>Rev.:</b>
					</td>
					<td>
						<input type="text" id="rev" name="rev" style="width: 70px;margin-left: 3px;" readonly>
					</td>
				</tr>
				<tr id="jobNumberBidderID">
					<td width="100px;">
						<b>JobNumber:</b>
					</td>
					<td>
						<input type="text" id="jobNumber" name="jobNumber" style="width: 70px;margin-left: 3px;">
					</td>
				</tr>
				<tr id="rxMasterBidderID">
					<td width="100px;">
						<b>RxMaster:</b>
					</td>
					<td>
						<input type="text" id="rxMasterId" name="rxMasterId" style="width: 70px;margin-left: 3px;">
					</td>
				</tr>
				<tr id="joMasterBidderID">
					<td width="100px;">
						<b>JoMaster:</b>
					</td>
					<td>
						<input type="text" id="joMasterId" name="joMasterId" style="width: 70px;margin-left: 3px;">
					</td>
				</tr>
				<tr id="rxContactIdBidderID">
					<td width="100px;">
						<b>RxContactID:</b>
					</td>
					<td>
						<input type="text" id="rxContactId" name="rxContactId" style="width: 70px;margin-left: 3px;">
					</td>
				</tr>
				<tr id="repBidderID">
					<td width="100px;">
						<b>ReP.:</b>
					</td>
					<td>
						<input type="text" id="rep" name="rep" style="width: 70px;margin-left: 3px;">
					</td>
				</tr>
			</table>
		</form>
		<hr width="500px;">
		<table align="right">
			<tr>
				<td><input type="button" class="cancelhoverbutton turbo-tan"  value="Submit" onclick="submitBid()" style="width:80px;"/></td> 
				<td><input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelBid()" style="width:80px;"/> </td>
			</tr>
		</table>
	  </div>
	<div id="contactCustom">
	 	<form action="" id="contactForm">
			<table>
				<tr id="ContactIDTD" style="display: none;">
					<td width="100px;">
						<b>ContactId:<span style="color:red;"> *</span></b>
					</td>
					<td>
						<input type="text" id="rxContactId" name="rxContactId"  class="validate[required]" style="width:240px;margin-left: 3px;" >
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Last Name:<span style="color:red;"> *</span></b>
					</td>
					<td>
						<input type="text" id="lastName" name="lastName"  class="validate[required]" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>First Name:</b>
					</td>
					<td>
						<input type="text" id="firstName" name="firstName" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Role:</b>
					</td>
					<td>
						<input type="text" id="jobPosition" name="jobPosition" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b>Email:</b>
					</td>
					<td>
						<input type="text" id="email" name="email" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td style=" width : 100px;">
					<b><label>Direct Line:</label></b>
					</td>
					<td>
						&nbsp;<input type="text" id="areaCodeDirID" name="areaCodeDir" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocus(this)">&nbsp;
						<input type="text" id="exchangeCodeDirID" name="exchangeCodeDir" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value="" onkeyup="autoFocusNext(this)"> - 
						<input type="text" id="subscriberNumberDirID" name="subscriberNumberDir" style="width:90px" class="validation[custom[onlyNumber]]" maxlength="4" value="">
					</td>
					<td style="display: none;">
						<input type="text" id="directLine" name="directLine" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td style=" width : 100px;">
						<b><label>Cell:</label></b>
					</td>
					<td>
						&nbsp;<input type="text" id="area" style="width:45px"  maxlength="3" value="">
						<input type="text" id="exchange" style="width:45px" maxlength="3"  value=""> - 
						<input type="text" id="subscriber" style="width:107px" value="">
					</td>
				</tr>
				<tr>
					<td style=" width : 100px;">
						<b><label>Division:</label></b>
					</td>
					<td>
						<input type="text" id="division" name="division" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				</table>
			</form>
		<table>
			<tr>
				<td><hr width="350px;"></td>
			</tr>
			<tr>
				<td  align="right">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Submit" onclick="submitContact()" style="width:80px;">
					<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelContact()" style="width:80px;"> 
				</td>
			</tr>
		</table>
	  </div>
	  <div>
	  	<form id="hiddenValueForm" style="display: none;">
			<table>
				<tr>
					<td>
						<input type="text" id="joMasterHiddenID" value= "${requestScope.joMasterDetails.joMasterId}">
					</td>
					<td>
						<input type="text" id="source1HiddenID" value= "${requestScope.joMasterDetails.source1}">
					</td>
					<td>
						<input type="text" id="source2HiddenID" value= "${requestScope.joMasterDetails.source2}">
					</td>
					<td>
						<input type="text" id="source3HiddenID" value= "${requestScope.joMasterDetails.source3}">
					</td>
					<td>
						<input type="text" id="source4HiddenID" value= "${requestScope.joMasterDetails.source4}">
					</td>
					<td>
						<input type="text" id="planAndSpecJobHiddenID" value= "${requestScope.joMasterDetails.planAndSpecJob}">
					</td>
					<td>
						<input type="text" id="engineerHiddenID" value= "${requestScope.AssignedEngineering}">
					</td>
					<td>
						<input type="text" id="architectHiddenID" value= "${requestScope.AssignedArchitects}">
					</td>
					<td>
						<input type="text" id="bidDateHiddenID" value= "">
					</td>
					<td>
						<input type="text" id="projectNameHiddenID" value= "${requestScope.joMasterDescriptionForPDF}">
					</td>
					<td>
						<input type="text" id="locationCityHiddenID" value= "${requestScope.joMasterDetails.locationCity}">
					</td>
					<td>
						<input type="text" id="locationStateHiddenID" value= "${requestScope.joMasterDetails.locationState}">
					</td>
					<td>
						<input type="text" id="jobNumberHiddenID" value= "${requestScope.joMasterDetails.jobNumber}">
					</td>
					<td>
						<input type="text" id="locationCityHiddenID" value= "${requestScope.joMasterDetails.locationCity}">
					</td>
					<td>
						<input type="text" id="loginNameHiddenID" value= "${requestScope.userDetails.fullName}">
					</td>
					<td>
						<input type="text" id="userUDHiddenID" value= "${requestScope.userDetails.userLoginId}">
					</td>
					<td>
						<input type="text" id="userInitialsHiddenID" value= "${requestScope.userDetails.initials}">
					</td>
					<td>
						<input type="text" id="emailNameHiddenID" value= "${requestScope.userDetails.emailName}">
					</td>
					<td>
						<input type="text" id="emailAddrHiddenID" value= "${requestScope.userDetails.emailAddr}">
					</td>
					<td>
						<input type="text" id="logOnNameHiddenID" value= "${requestScope.userDetails.logOnName}">
					</td>
					<td>
						<input type="text" id="logOnPswdHiddenID" value= "${requestScope.userDetails.logOnPswd}">
					</td>
					<td>
						<input type="text" id="ccaddr1HiddenID" value= "${requestScope.userDetails.ccaddr1}">
					</td>
					<td>
						<input type="text" id="ccaddr2HiddenID" value= "${requestScope.userDetails.ccaddr2}">
					</td>
					<td>
						<input type="text" id="ccaddr3HiddenID" value= "${requestScope.userDetails.ccaddr3}">
					</td>
					<td>
						<input type="text" id="ccaddr4HiddenID" value= "${requestScope.userDetails.ccaddr4}">
					</td>
					<td>
						<input type="text" id="ccname1HiddenID" value= "${requestScope.userDetails.ccname1}">
					</td>
					<td>
						<input type="text" id="ccname2HiddenID" value= "${requestScope.userDetails.ccname2}">
					</td>
					<td>
						<input type="text" id="ccname3HiddenID" value= "${requestScope.userDetails.ccname3}">
					</td>
					<td>
						<input type="text" id="ccname4HiddenID" value= "${requestScope.userDetails.ccname4}">
					</td>
					<td>
						<input type="text" id="bccaddrHiddenID" value= "${requestScope.userDetails.bccaddr}">
					</td>
					<td>
						<input type="text" id="smtpsvrHiddenID" value= "${requestScope.userDetails.smtpsvr}">
					</td>
					<td>
						<input type="text" id="smtpportHiddenID" value= "${requestScope.userDetails.smtpport}">
					</td>
					<td>
						<input type="text" id="QuoteRevHiddenID">
					</td>
				</tr>
			</table>
		</form>
	  </div>
	 <div id="searchCriteria" style="display: none;">
			<form id="searchCriteriaForm">
				<div id="searchCriteriaFormID">
				<table>
				<tr>
				<td><label>Enter Criteria: </label><br><br></td>
				</tr>
				 <tr>
			              <td><label>Division:</label></td>
			              <td class="space">
			              		<select class="" id="divisionID" name="division_name">
			              			<option value ="-1">--Select--</option>
			              			<option value = "3">Norcross</option>
			              			<option value = "4">Birmingham</option>
			              		</select>
			              	</td>
			              <td><div id="divisionList"></div></td>
			          </tr>
			           <tr>
			             <td><label>Date Range From: </label></td>
			             <td><input type='text' class='datepicker' id='rangepickerID' name='rangepickerName' style='width:80px;'>
			             <label>To </label><input type='text' style='width:80px;' class='datepicker' id='thruPickerID' name='thruPickerName'></td>
			          </tr>
			           <tr>
			             <td><label>Include unknown bid dates: </label></td>
			              <td colspan="2" style="padding-left:7px;" >
			             <input type="checkbox" onchange="" class="headermenuVerticalMidd" id="" name="" />
			             <td>&nbsp;</td>
			          </tr>
			           <tr>
			             <td><label>Include job bid details: </label></td>
			             <td colspan="2" style="padding-left:7px;" >
			             <input type="checkbox" onchange="" class="headermenuVerticalMidd" id="" name="" />
			             <td>&nbsp;</td>
			          </tr>
			           <tr>
			             <td><label>Include postponed bids: </label></td>
			              <td colspan="2" style="padding-left:7px;" >
			             <input type="checkbox" onchange="" class="headermenuVerticalMidd" id="" name="" />
			             <td>&nbsp;</td>
			          </tr>
			           <tr>
			             <td><label>Group by division: </label></td>
			              <td colspan="2" style="padding-left:7px;" >
			             <input type="checkbox" onchange="" class="headermenuVerticalMidd" id="" name="" />
			             <td>&nbsp;</td>
			          </tr>	
			          <tr><td colspan="2"><hr width="450px;"></td></tr>
				</table>
				
				<table>
			 	<tr>
			         <td>
				            <input type="button" class="cancelhoverbutton turbo-tan"  value="View" onclick="" style="width:80px;">  
						    <input type="button" class="cancelhoverbutton turbo-tan"  value="Print" style="width:80px;" onclick="">
						 	<input type="button" class="cancelhoverbutton turbo-tan"  value="File" onclick="" style="width:80px;">  
							<input type="button" class="cancelhoverbutton turbo-tan" value="Export" onclick="" style=" width:80px;">
							<input type="button" class="cancelhoverbutton turbo-tan"  value="Cancel" onclick="cancelReport()" style="width:80px;">  
			         </td>
				</tr>
			</table>
				
				</div>
				</form>
		</div>
	<!--
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobWizardQuotes.min.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/job_Quotes_Bidlist.min.js"></script>
 	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/addQuotes.min.js"></script>    
 	-->
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jQuery_jqprint_0.3.min.js"></script>
 	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/addQuotes.js"></script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/job_quotes_bidlist.js"></script> 
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jobWizardQuotes.js"></script>
	<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#contractAmount").val(formatCurrency("${requestScope.joMasterDetails.contractAmount}"));
		$("#estimatedCost").val(formatCurrency("${requestScope.joMasterDetails.estimatedCost}"));
		$("#estimatedProfit").val(formatCurrency("${requestScope.joMasterDetails.estimatedProfit}"));
		$("#plan_date_format").val(customFormatDateTime("${requestScope.joMasterDetails.planDate}"));
		$("#bidDateHiddenID").val(customFormatDateTime("${requestScope.joMasterDetails.bidDate}"));
		$("#OriJobNumber").css("display", "none");
		var aQuoteNumber = "${requestScope.joMasterDetails.quoteNumber}";
		if(aQuoteNumber !== ''){
			$("#OriJobNumber").show();
		}
		// following code is to toggle the text for quote template properties dialog.
		$( "#quotePropertiesTempDialog input:checkbox" ).change(function(){
			if ($(this).is(":checked"))
				$(this).next("label").text("(Yes)");
			else 
				$(this).next("label").text("(No)");
		});

	});
	</script>