<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="rolodexGeneral">
	<form id="rolodexdfromID">
		<table>
			<tr height="0px;"></tr>
			<tr>
				<td width="125px;" style="padding-bottom: 10px;"><label>Customer Name: </label></td>
			 	<td width="615px;" style="padding-bottom: 10px;">
					<input type="text" name="EmployeeName" size="32" id="customerNameHeader"  class="validate[maxSize[40]]"/>
					<input type="button" class="savebutton" value="    " onclick="updateRolodexName()" id="saveButton" >
				</td>
				<td align="right">
					<input type="button" class="quickQuotehoverbutton turbo-blue" value="Address" onclick="callRxAddressDetails()" style="width:90px;"> 
					<input type="button" id="customerQuickQuote" class="quickQuotehoverbutton turbo-blue" value="Quick Quote" onclick="openQuickQuote()" style="width:120px;">
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
						<table style="padding-left: 0px" id="VendorDetailsGrid" class="scroll"></table>
						<div id="EmployeeDetailsGridpager" class="scroll" style="text-align: right;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td align="right">
					<input type="button" class="add" id="add"	value="   Add" onclick="addRolodexContacts()">
					<input type="button" class="cancelhoverbutton turbo-blue" id="add"	value=" Edit " style="width:60px" onclick="editRolodexContacts()">
					<input type="button" class="cancelhoverbutton turbo-blue" id="delete"	value=" Delete" style="width:80px" onclick="deleteRolodexContact()">
				</td>
			</tr>
		</table>
		<table>
			<tr>
				<td align="right"><label>Categories : &nbsp;</label> 
					<input type="checkbox" id="customerchek" style="vertical-align: middle;" onclick="customer();updateRolodex();" ><label>&nbsp;Customer</label>
					<input type="checkbox" id="vendorcheck" style="vertical-align: middle;" onclick="vendor();updateRolodex();" ><label>&nbsp;Vendor</label>
					<input type="checkbox" id="employeeche" style="vertical-align: middle;" onclick="employee();updateRolodex();" ><label>&nbsp;Employee</label>
					<input type="checkbox" id="engineerche"	style="vertical-align: middle;display: none;" onclick="engineer();updateRolodex();"><!-- <label>&nbsp;Engineer</label> --><label id="Category1lab" style="display: none;"></label>
					<input type="checkbox" id="architectche" style="vertical-align: middle;display: none;" onclick="architect();updateRolodex();" onChange=""><!-- <label>&nbsp;Architect</label> --><label id="Category2lab" style="display: none;"></label>
					<input type="checkbox" id="caustMGR" name="caustMGR" style="vertical-align: middle;display: none;" onClick="updateRolodex()"><!-- <label>&nbsp;G.C/Constr. MGR</label> --><label id="Category3lab" style="display: none;"></label>
					<input type="checkbox" id="owner" name="owner" style="vertical-align: middle;display: none;" onClick="updateRolodex()"><!-- <label>&nbsp;Owner</label> --><label id="Category4lab" style="display: none;"></label>
					<input type="checkbox" id="bondAgent" name="bondAgent" style="vertical-align: middle;" onClick="updateRolodex()"><!-- <label>&nbsp;Bond Agent</label> --><label id="Category5lab" style="display: none;"></label>
					<input type="checkbox" id="Category6Desc" name="Category6Desc" style="vertical-align: middle;display: none;" onclick="updateRolodex()"><label id="Category6lab" style="display: none;"></label>
					<input type="checkbox" id="Category7Desc" name="Category7Desc" style="vertical-align: middle;display: none;" onclick="updateRolodex()"><label id="Category7lab" style="display: none;"></label>
					<input type="checkbox" id="Category8Desc" name="Category8Desc" style="vertical-align: middle;display: none;" onclick="updateRolodex()"><label id="Category8lab" style="display: none;"></label>
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
	 <div id="rolodexAddressGrid">
		<%@ include file="rolodexaddress_details.jsp" %>
	</div>
	<div id="rolodexContactCustom">
	 	<form action="" id="rolodexContactCustomForm">
			<table>
				<tr id="ContactIDTD">
					<td width="100px;">
						<b><label>ContactId:</label><span style="color:red;"> *</span></b>
					</td>
					<td>
						<input type="text" id="rxContactId" name="rxContactId"  class="validate[required]" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b><label>Last Name:</label><span style="color:red;"> *</span></b>
					</td>
					<td>
						<input type="text" id="lastName" name="lastName" maxlength ="40"  class="validate[required]" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b><label>First Name:</label></b>
					</td>
					<td>
						<input type="text" id="firstName" name="firstName" maxlength ="40" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b><label>Role:</label></b>
					</td>
					<td>
						<input type="text" id="jobPosition" name="jobPosition" style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td width="100px;">
						<b><label>Email:</label></b>
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
					<td width="100px;">
						<b><label>Extension:</label></b>
					</td>
					<td>
						<input type="text" id="extension" name="extension" maxlength=25 style="width:240px;margin-left: 3px;">
					</td>
				</tr>
				<tr>
					<td style=" width : 100px;">
						<b><label>Cell: </label></b>
					</td>
					<td>
						&nbsp;<input type="text" id="areaCode1" name="contact2" style="width:50px" class="validation[custom[onlyNumber]]" maxlength="3" value=""  onkeyup="autoFocusCell(this)">&nbsp;
						<input type="text" id="exchangeCode1" name="contact2" style="width:50px" maxlength="3" class="validation[custom[onlyNumber]]" value=""  onkeyup="autoFocusCellNext(this)"> - 
						<input type="text" id="subscriberNumber1" name="contact2" style="width:90px" class="validation[custom[onlyNumber]]" maxlength="4" value="">
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
	
<div id="editQuickQuoteDialog">
		<form action="" id="editQuickQuoteFormID">
			<table>
				<tr id="editQuoteJobNumber"><td><label>Job Number</label></td><td><input type="text"  id="editQuoteJobNumberID" name="editQuoteJobNumberName" style="width: 275px;height: 22px;" readonly="readonly"></td></tr>
				<tr id="editQuoteJoBidder" style="display: none;"><td><label>JoBidderId</label></td><td><input type="text"  id="editQuoteJoBidderID" name="editQuoteJoBidderIdName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteJoMaster" style="display: none;"><td><label>JoMasterId</label></td><td><input type="text"  id="editQuoteJobMasterID" name="editQuoteJobMasterName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteRxContact" style="display: none;"><td><label>rxContactId</label></td><td><input type="text"  id="editQuoteRxContactID" name="editQuoteRxContactName" style="width: 275px;height: 22px;"></td></tr>
				<tr id="editQuoteRxMaster" style="display: none;"><td><label>rxMasterId</label></td><td><input type="text"  id="editQuoteRxMasterID" name="editQuoteRxMasterName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Job Name:<b style="color: #BA3851;">*</b></label></td>
						<td><input type="text" id="editQuoteProductListID" name="editQuoteProductListName"placeholder="Minimum 2 characters required" class="validate[required]" style="width: 275px;height: 22px;">
				<tr><td><label>Date:</label></td><td><input type="text" class="datepicker" id="editQuoteDateID" name="editQuoteDateName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Contact:<b style="color: #BA3851;">*</b></label></td>
						<td><input type="text" id="editQuoteCustomerID" name="editQuoteCustomerName" style="width: 275px;height: 22px;" placeholder="Minimum 2 characters required" class="validate[required]">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
						<input type="hidden" id="editQuoteCustomerHiddenID" name="editQuoteCustomerHiddenName" style="width: 100px;">
						<input type="hidden" id="editQuoteRxMasterHiddenID" name="editQuoteRxMasterHiddenName" style="width: 100px;">
						<input type="hidden" id="editQuoteCustomerContactHiddenID" name="editQuoteCustomerContactHiddenName" style="width: 275px;height: 22px;"></td></tr>
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
						<td><input type="text" id="customerID" name="customerName" style="width: 275px;height: 22px;" placeholder="Minimum 2 characters required" class="validate[required]" readonly="readonly">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
						<input type="hidden" id="customerHiddenID" name="customerHiddenName" style="width: 100px;">
						<input type="hidden" id="rxMasterHiddenID" name="rxMasterHiddenName" style="width: 100px;">
						<input type="hidden" id="customerContactHiddenID" name="customerContactHiddenName" style="width: 275px;height: 22px;"></td></tr>
				<tr><td><label>Re:<b style="color: #BA3851;">*</b></label></td><td><input type="text"  id="productListID"  name="productListName" style="width: 275px;height: 22px;" placeholder="Minimum 2 characters required" class="validate[required]">
				<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td></tr>
				<tr><td><label>Description:</label></td>
						<td><textarea  id="pharagraphID"  name="pharagraphName" rows="4" cols="32"></textarea>
				<tr><td><label>Estimated Cost:</label></td><td><input type="text" id="costID" name="costName" style="width: 275px;height: 22px;text-align: right;"></td></tr>
				<tr><td><label>Manufacturer:</label></td>
						<td><input type="text" id="manufacturerID" name="manufacturerName" style="width: 275px;height: 22px;" placeholder="Minimum 2 characters required">
						<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">
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
	   <div id="addProductDialog" align="left">
		<div>
		<table><tr>
				<td style="width: 35px;"><label>Type:</label></td>
				<td><input type="text" id="TypeDetail" placeholder="Minimum 2 characters required" name="TypeDetailName" disabled="disabled">
			<!-- 	<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png">  -->
				<input type="text" id="TypeDetailID" name="TypeDetailID" style="display:none;"></td>
				<td style="width: 35px;"><label>Revision:</label></td>
				<td><input type="text" style="width: 50px;" id="revisionId" name="revision" disabled="disabled">
				</td>
			</tr>
		</table>
		<table id="addquotesList"><div id="addquotespager"></div></table><br>
		<table  style="padding-right: 20px;" align="right">
			<tr>
			<td id="QuoteId"><label>HeaderID: </label><input type="hidden" id="joQuoteHeaderID" disabled="disabled" style="width:100px"></td>
				<td><label>Cost: </label><input type="text" id="totalCostID" name="totalCostName" disabled="disabled" style="width:100px"></td>
				<td><label>Price: </label><input type="text" id="totalPriceID" name="totalPriceName" disabled="disabled" style="width:100px"></td>
			</tr>
		</table>
		</div>
	</div>
	<script type="text/javascript">
		var aGlobalVariable;
		jQuery(document).ready(function() {
			var name =$("#customerName").text(); /*""+ getUrlVars()["name"]; 
			var arr = name.split("%20");
			name = "";
			for(var index = 0; index < arr.length; index++){
				name = name + arr[index] + " ";
			}
			//name = name.replace("%20", " "); */
			name = name.replace('(', ' ');
			name = name.replace(')', ' ');
				$("#customerNameHeader").val('');
				$("#customerNameHeader").val(name);
				$("#customerChecked").css("display", "none");
				$("#QuoteId").css("display", "none");
				//loadAddressGrid();
				loadContactsGrid();
				loadGridOpenQuotes();
				$("#lostQuotesCheck").hide();
				$("#QuotesCheck").hide();
				var archTech = "${requestScope.rxMasterDetails.isCategory1}";
				var Engg = "${requestScope.rxMasterDetails.isCategory2}";
				var generalContract = "${requestScope.rxMasterDetails.isCategory3}";
				var owner = "${requestScope.rxMasterDetails.isCategory4}";
				var bondAgent = "${requestScope.rxMasterDetails.isCategory5}";
				var customer = "${requestScope.rxMasterDetails.isCustomer}"; 
				var vendor = "${requestScope.rxMasterDetails.isVendor}";
			    var Emp = "${requestScope.rxMasterDetails.isEmployee}";
			    var category6="${requestScope.rxMasterDetails.isCategory6}";
			    var category7="${requestScope.rxMasterDetails.isCategory7}";
			    var category8="${requestScope.rxMasterDetails.isCategory8}";
			    if(category6== 'true'){
			    	$("#Category6Desc").attr("checked", true);
			    }
			    if(category7== 'true'){
			    	$("#Category7Desc").attr("checked", true);
			    }
			    if(category8== 'true'){
			    	$("#Category8Desc").attr("checked", true);
			    }
				if(archTech === 'true'){
					$("#engineerche").attr("checked", true);
				}
				if(Engg === 'true'){
					$("#architectche").attr("checked", true);
				}
				if(generalContract === 'true'){
					$("#caustMGR").attr("checked", true);
				}
				if(customer === 'true'){
					$("#customerchek").attr("checked", true);
					jQuery("#VendorDetailsGrid").setGridHeight(200);
					$("#customerChecked").show();
				}
				if(vendor === 'true'){
					$("#vendorcheck").attr("checked", true);
				}
				if(Emp === 'true'){
					$("#employeeche").attr("checked", true);
				}
				if(owner === 'true'){
					$("#owner").attr("checked", true);
				}
				if(bondAgent === 'true'){
					$("#bondAgent").attr("checked", true);
				}
				
				loadrolodexcategories();
			});
		/*while loading the page label and checkbox enable hide and show based upon sysinfo table*/
		function loadrolodexcategories(){
			var Category1Desc=$("#Category1Descset").val();
			var Category2Desc=$("#Category2Descset").val();
			var Category3Desc=$("#Category3Descset").val();
			var Category4Desc=$("#Category4Descset").val();
			var Category5Desc=$("#Category5Descset").val();
			var Category6Desc=$("#Category6Descset").val();
			var Category7Desc=$("#Category7Descset").val();
			var Category8Desc=$("#Category8Descset").val();
			
			//Category1lab
			$("#Category1lab").text(Category1Desc);
			$("#Category2lab").text(Category2Desc);
			$("#Category3lab").text(Category3Desc);
			$("#Category4lab").text(Category4Desc);
			$("#Category5lab").text(Category5Desc);
			$("#Category6lab").text(Category6Desc);
			$("#Category7lab").text(Category7Desc);
			$("#Category8lab").text(Category8Desc);
			if(Category1Desc==undefined ||Category1Desc==null ||Category1Desc==""||Category1Desc.length==0){
				$("#engineerche").css({ display: "none" });
				$("#Category1lab").css({ display: "none" });
			}else{
				$("#engineerche").css({ display: "inline-block" });
				$("#Category1lab").css({ display: "inline-block" });
				
			}
			if(Category2Desc==undefined ||Category2Desc==null ||Category2Desc==""||Category2Desc.length==0){
				$("#architectche").css({ display: "none" });
				$("#Category2lab").css({ display: "none" });
			}else{
				$("#architectche").css({ display: "inline-block" });
				$("#Category2lab").css({ display: "inline-block" });
			}
			if(Category3Desc==undefined ||Category3Desc==null ||Category3Desc==""||Category3Desc.length==0){
				$("#caustMGR").css({ display: "none" });
				$("#Category3lab").css({ display: "none" });
			}else{
				$("#caustMGR").css({ display: "inline-block" });
				$("#Category3lab").css({ display: "inline-block" });
			}
			if(Category4Desc==undefined ||Category4Desc==null ||Category4Desc==""||Category4Desc.length==0){
				$("#owner").css({ display: "none" });
				$("#Category4lab").css({ display: "none" });
			}else{
				$("#owner").css({ display: "inline-block" });
				$("#Category4lab").css({ display: "inline-block" });
			}
			//engineerche ,architectche,caustMGR,owner,bondAgent,Category6Desc,Category7Desc,Category8Desc
			if(Category5Desc==undefined ||Category5Desc==null ||Category5Desc==""||Category5Desc.length==0){
				$("#bondAgent").css({ display: "none" });
				$("#Category5lab").css({ display: "none" });
			}else{
				document.getElementById("bondAgent").style.display="inline-block";
				document.getElementById("Category5lab").style.display="inline-block";
			}
			if(Category6Desc==undefined ||Category6Desc==null ||Category6Desc==""||Category6Desc.length==0){
				$("#Category6Desc").css({ display: "none" });
				$("#Category6lab").css({ display: "none" });
			}else{
				$("#Category6Desc").css({ display: "inline-block" });
				$("#Category6lab").css({ display: "inline-block" });
			}
			if(Category7Desc==undefined ||Category7Desc==null ||Category7Desc==""||Category7Desc.length==0){
				$("#Category7Desc").css({ display: "none" });
				$("#Category7lab").css({ display: "none" });
			}else{
				$("#Category7Desc").css({ display: "inline-block" });
				$("#Category7lab").css({ display: "inline-block" });
			}
			if(Category8Desc==undefined ||Category8Desc==null ||Category8Desc==""||Category8Desc.length==0){
				$("#Category8Desc").css({ display: "none" });
				$("#Category8lab").css({ display: "none" });
			}else{
				$("#Category8Desc").css({ display: "inline-block" });
				$("#Category8lab").css({ display: "inline-block" });
			}
			
			
		}
		function addRolodexContacts()
		{	
			aGlobalVariable = "add";
			document.getElementById("rolodexContactCustomForm").reset();
			$("#ContactIDTD").hide(); 
			jQuery("#rolodexContactCustom").dialog("open");
		}
		
		function editRolodexContacts()
		{
			aGlobalVariable = "edit";
			var grid = $("#VendorDetailsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			if(rowId === null){
				var errorText = "Please select a Contact.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			var rxContactID = grid.jqGrid('getCell', rowId, 'rxContactId');
			var lastName = grid.jqGrid('getCell', rowId, 'lastName');
			var firstName = grid.jqGrid('getCell', rowId, 'firstName');
			var role = grid.jqGrid('getCell', rowId, 'jobPosition');
			var directline = grid.jqGrid('getCell', rowId, 'directLine');
			var email = grid.jqGrid('getCell', rowId, 'email');
			var division = grid.jqGrid('getCell', rowId, 'division');
			var phone = grid.jqGrid('getCell', rowId, 'cell');

			var extension=grid.jqGrid('getCell',rowId,'extension');
						
			if(phone === null || phone.length === 0){
				$("#subscriberNumber1").val(''); $("#areaCode1").val(''); $("#exchangeCode1").val('');
				$("#ContactIDTD").hide();
			}else{
				var areacode='';
				var exchangecode='';
				if(phone!=="undefined"){
					areacode = phone.split(" ");
				}
				if(typeof(areacode[1]) !=="undefined"){
					exchangecode = areacode[1].split("-");
				}
				var pincode = areacode[0].replace("(", "");
				var pincode1 = pincode.replace(")", "");
				$("#areaCode1").val(pincode1); $("#exchangeCode1").val(exchangecode[0]); 
				$("#subscriberNumber1").val(exchangecode[1]);
			}
			 if(directline == null || directline.length == 0){
				 $("#areaCodeDirID").val('');
					$("#exchangeCodeDirID").val('');
					$("#subscriberNumberDirID").val('');
					$("#ContactIDTD").hide();
			 }else{
				var areacodeDir = directline.split(" ");
				var exchangecodeDir = areacodeDir[1].split("-");
				var pincodeDir = areacodeDir[0].replace("(", "");
				var pincode1Dir = pincodeDir.replace(")", "");
				$("#areaCodeDirID").val(pincode1Dir);
				$("#exchangeCodeDirID").val(exchangecodeDir[0]);
				$("#subscriberNumberDirID").val(exchangecodeDir[1]);
			 } 
			$("#rxContactId").val(rxContactID); $("#lastName").val(lastName); $("#firstName").val(firstName); 
			$("#jobPosition").val(role); $("#email").val(email); $("#division").val(division);   $("#directLine").val(directline); 
			$("#extension").val(extension);
			$("#ContactIDTD").hide();
			jQuery("#rolodexContactCustom").dialog("open");
			return true;
		}

		function deleteRolodexContact()
		{
			aGlobalVariable = "del";
			var grid = $("#VendorDetailsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var newDialogDiv = jQuery(document.createElement('div'));
			if(rowId === null){
				var errorText = "Please select a Contact.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
			
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Contact Record?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
				buttons:{
					"Submit": function(){
						deleteContact();
						jQuery(this).dialog("close");
						$("#VendorDetailsGrid").trigger("reloadGrid");
					},
					Cancel: function ()	{jQuery(this).dialog("close");}
					}}).dialog("open");
			return true;
			}
		}

		function deleteContact(){
			var grid = $("#VendorDetailsGrid");
			var rowId = grid.jqGrid('getGridParam', 'selrow');
			var rxContactID = grid.jqGrid('getCell', rowId, 'rxContactId');
			var rxMasterID= getUrlVars()['rolodexNumber'];
			$.ajax({
				url: "rolodexdetails/manpulaterxContact",
				type: "POST",
				async:false,
				data : "&oper="+aGlobalVariable +"&rxMasterId=" +rxMasterID +"&rxContactId=" + rxContactID,
				success: function(data) {
					$("#VendorDetailsGrid").trigger("reloadGrid");
					$("#OpenGridQuotes").trigger("reloadGrid"); 
					$("#customerLostQuotesGrid").trigger("reloadGrid"); 
				}
			});
		}

		function saveRolodexContact(){
			var contactListValue = $("#rolodexContactCustomForm").serialize();
			var rxMasterId = getUrlVars()["rolodexNumber"];
			var areaCode=$("#areaCode1").val();
			var exchangeCode = $("#exchangeCode1").val();
			var subscriberNumber = $("#subscriberNumber1").val();
			var areaCodeDir = $("#areaCodeDirID").val();
			var exchageCodeDir =$("#exchangeCodeDirID").val();
			var subscriberNumberDir =$("#subscriberNumberDirID").val();
			var cell= '';
			var dir = '';
			if(areaCode !== ""){
				cell="("+areaCode+") "+exchangeCode+"-"+subscriberNumber;
			}
			if(areaCodeDir !== ""){
				dir ="("+areaCodeDir+") "+exchageCodeDir+"-"+subscriberNumberDir;
			}
			if(aGlobalVariable === "add"){
				$.ajax({
					url: "rolodexdetails/manpulaterxContact",
					type: "POST",
					async:false,
					data : contactListValue + "&oper="+aGlobalVariable +"&rxMasterId=" +rxMasterId +"&cell=" + cell+"&directDir="+dir,
					success: function(data) {
						$("#VendorDetailsGrid").trigger("reloadGrid");
					}
				});
			}else if(aGlobalVariable === "edit"){
				$.ajax({
					url: "rolodexdetails/manpulaterxContact",
					type: "POST",
					async:false,
					data : contactListValue + "&oper="+aGlobalVariable +"&rxMasterId=" + rxMasterId +"&cell=" + cell+"&directDir="+dir,
					success: function(data) {
						$("#VendorDetailsGrid").trigger("reloadGrid");
						var errorText = "Contact Successfully Edited.";
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:Green;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					}
				});
			}
		}
		
		jQuery(function(){
			jQuery("#rolodexContactCustom").dialog({
					autoOpen : false,
					modal : true,
					title:"Add/Edit Contact",
					width: 400,  left: 300, top: 290,
					buttons : {  },
					close:function(){
						$('#rolodexContactCustomForm').validationEngine('hideAll');
						return true;
					}
			});
		});
		
		function submitContact(){
			if(aGlobalVariable === "add"){
				if(!$('#rolodexContactCustomForm').validationEngine('validate')) {
					return false;
				}
			saveRolodexContact();
			jQuery("#rolodexContactCustom").dialog("close");
			}else if(aGlobalVariable === "edit"){
				if(!$('#rolodexContactCustomForm').validationEngine('validate')) {
					return false;
				}
				saveRolodexContact();
				$('#BidDialogCustomForm').validationEngine('hideAll');
				jQuery("#rolodexContactCustom").dialog("close");
			}
			return true;
		}

		function cancelContact(){
			$('#rolodexContactCustomForm').validationEngine('hideAll');
			jQuery("#rolodexContactCustom").dialog("close");
		}

		function updateRolodex(){
			var rolodexNumber=getUrlVars()["rolodexNumber"];
			var aCustomer=false;
			var aVendor=false;
			var aEmployee=false;
			var aArchitect=false;
			var aEngineer=false;
			var aGCorConstrMGR=false;
			var aOwner=false;
			var aBondAgent=false;
			var category6=false;
			var category7=false;
			var category8=false;
			if ($('#customerchek').is(':checked')){
					aCustomer=true;
			}if ($('#vendorcheck').is(':checked')){
					aVendor=true;
			}if ($('#employeeche').is(':checked')){
					aEmployee=true;
			}if ($('#engineerche').is(':checked')){
					aEngineer=true;
			}if ($('#architectche').is(':checked')){
					aArchitect=true;
			}if ($('#caustMGR').is(':checked')){
				aGCorConstrMGR=true;
			}if ($('#owner').is(':checked')){
				aOwner=true;
			}if ($('#bondAgent').is(':checked')){
				aBondAgent=true;
			}if ($('#Category6Desc').is(':checked')){
				category6=true;
			}
			if ($('#Category7Desc').is(':checked')){
				category7=true;
			}
			if ($('#Category8Desc').is(':checked')){
				category8=true;
			}
			$.ajax({
				url: "rolodexdetails/updateRolodex",
				type: "POST",
				async:false,
				data :"&rolodexNumber=" +rolodexNumber+"&isCustomer="+aCustomer +"&isVendor="+aVendor +"&isEmployee="+aEmployee +"&isArchitect="+aArchitect +"&isEngineer="+aEngineer +"&isGCAndConstr="+aGCorConstrMGR +"&isOwner="+aOwner +"&isBondAgent="+aBondAgent+"&category6="+category6+"&category7="+category7+"&category8="+category8,
				success: function(data) {
				}
			});
		}
		
	function loadContactsGrid() {
		var rolodexNumber = getUrlVars()["rolodexNumber"];
		var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
		var grid = $('#VendorDetailsGrid');
		$("#VendorDetailsGrid").jqGrid({
			url: 'vendorscontroller/vendorcontact',
			datatype: 'JSON',
			postData: {'rolodexNumber' : rolodexNumber },
			mtype: 'GET',
			colNames: [ 'RxContactId','Last', 'First', 'Role', 'Phone', 'Direct Line','Extension', 'Email', 'CellPhone', 'Division' ],
			colModel: 
			[{name: 'rxContactId',index:'rxContactId',align:'left',width:50,hidden: true, editable:true,editoptions:{},editrules:{required:false,edithidden:false}},
			 {name: 'lastName',index : 'lastName',align : 'left',width : 50, editable : true,hidden : false,editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
			 {name: 'firstName',index : 'firstName',align : 'left',width : 50,hidden : false,editable : true, editoptions : {size : 20,readonly : false,alignText : 'right'	},editrules : {edithidden : true,required : true}},
			 {name: 'jobPosition',index : 'jobPosition',align : 'left',	width : 90,	hidden : false,editable : true,	editoptions : {},editrules: {edithidden: true,required : false	}},
			 {name: 'phone',index : 'phone',align : 'left',width : 50,hidden : true,	editable : true,editoptions : {},editrules : {edithidden: true, required : false }},
			 {name: 'directLine',index : 'directLine',align : 'left',width : 50,hidden : false,editable : true,editoptions : {},editrules : {edithidden: true,required : false}},
			 {name: 'extension', index : 'extension', align : 'left',width : 90,hidden : false,editable : true,editoptions: {},editrules : {	edithidden: true, required : false}},
			 {name: 'email', index : 'email', align : 'left',width : 90,hidden : false,editable : true,editoptions: {},editrules : {	edithidden: true, required : false}},
			 {name: 'cell',index : 'cell',align : 'left',width : 50,hidden : false,	editable : true,editoptions : {},editrules : {edithidden: true, required : false	}},
			 {name: 'division',index : 'division',align : 'left',width : 50,hidden : false,editable : true,editoptions : {},editrules : {edithidden: true,	required : false}}],
			 rowNum: 0,
			recordtext: '',
			rowList: [],
			pgtext: null,
			viewrecords: false,
			sortname: ' ',
			sortorder: "asc",
			imgpath: 'themes/basic/images',
			caption: 'Contacts',
			height: 400,
			altRows: true,
			altclass:'myAltRowClass',
			rownumbers:true,
			width: 1100,
			loadComplete: function(data) {	
				$("#VendorDetailsGrid").setSelection(1, true);
			},
			onSelectRow : function(id) {
				$("#customerQuotes").show();
				$("#lostQuotesCheck").show();
				$("#QuotesCheck").show();
				lostQuotesShow();
				QuotesShow();
				var rowData = jQuery(this).getRowData(id); 
				var rxContactId = rowData['rxContactId'];
				var rxMasterId1 = rowData['rxMasterId'];
				var aFirstName = rowData['firstName'];
				var aLastName = rowData['lastName'];
				$("#dummyContactName").val(aFirstName+" "+aLastName);
				$("#contactIDhiden").val(rxContactId);
				$("#rxMasterIDhiden").val(rxMasterId1);
				var rolodexNumber = getUrlVars()["rolodexNumber"];
				$("#OpenGridQuotes").jqGrid('GridUnload');
				loadGridOpenQuotes(rxContactId);
				$('#OpenGridQuotes').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
				$("#OpenGridQuotes").trigger("reloadGrid"); 
				$("#customerLostQuotesGrid").jqGrid('GridUnload');
				loadLostQuotesGrid(rxContactId);
				$('#customerLostQuotesGrid').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
				$("#customerLostQuotesGrid").trigger("reloadGrid");
				$("#customerQuotesGrid").jqGrid('GridUnload');
				loadQuotesGrid(rxContactId);
				$('#customerQuotesGrid').jqGrid('setGridParam',{postData:{'rxContactId' : rxContactId, 'rolodexNumber':rolodexNumber }});
				$("#customerQuotesGrid").trigger("reloadGrid"); 
			},
			jsonReader: {
	            root: "rows",
	            page: "page",
	            total: "total",
	            records: "records",
	            repeatitems: false,
	            cell: "cell",
	            id: "id",
	            userdata: "userdata"
	    	},
			loadError : function(jqXHR, textStatus, errorThrown) {	}
		});
		emptyMsgDiv.insertAfter(grid.parent());
		return true;
	}

   // Rolodex Address grid while Address button click
	function callRxAddressDetails()	{	
			var rolodexid = getUrlVars()["rolodexNumber"];
			loadrxAddressGrid(rolodexid);
		/* 	$("#extensionaddress").hide();
			$("#addressSetting").hide();
			$("#remitaddress").hide(); */
			jQuery("#rxAddressGrid").dialog('option', 'title', 'Rolodex Address');
			jQuery("#rxAddressGrid").dialog("open");
	}


	
	function openQuickQuote(){
		if ($('#customerchek').is(':checked')) {
			var rowId = $("#VendorDetailsGrid").jqGrid('getGridParam', 'selrow');
			if(rowId === null){
						var errorText = "Please add a record in contact grid.";
					var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
						buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
					return false;
				}else{
					var arxContactId = $("#VendorDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
					//var arxMasterId = $("#VendorDetailsGrid").jqGrid('getCell', rowId, 'rxMasterId');
					var aLastName = $("#VendorDetailsGrid").jqGrid('getCell', rowId, 'lastName');
					var aFirstName = $("#VendorDetailsGrid").jqGrid('getCell', rowId, 'firstName');
					$("#customerHiddenID").val(arxContactId); $("#customerID").val(aFirstName+' '+aLastName);
					var customer = $("#customerNameHeader").val(); $("#customerContactHiddenID").val(customer);
					$("#productListID").val('');$("#pharagraphID").val(''); $("#costID").val(''); $("#manufacturerID").val(''); $("#sellPriceID").val('');
					$("#revision").val('');
					var rxMasterID = getUrlVars()["rolodexNumber"]; $("#rxMasterHiddenID").val(rxMasterID); 
				/* 	$.ajax({
						url: "customerList/quickQuoteRevision",
						mType: "GET",
						data : {'rxMasterID': arxMasterId, 'rxContactID' : arxContactId},
						success: function(data){
							$("#revision").val(data);
						}
					}); */ 
					jQuery("#quickQuoteDialog").dialog("open");
			} 
			/*var customer = $("#customerNameHeader").val(); $("#customerContactHiddenID").val(customer);
			var rxMasterID = getUrlVars()["rolodexNumber"]; $("#rxMasterHiddenID").val(rxMasterID);
			jQuery("#quickQuoteDialog").dialog("open");*/
			return true;
		} else {
			var errorTe = "The 'Customer' box must be checked in order to use this feature.";
			var newDialog = jQuery(document.createElement('div'));
			jQuery(newDialog).attr("id","msgDlg");
			jQuery(newDialog).html('<span><b style="color:red;">'+errorTe+'</b></span>');
			jQuery(newDialog).dialog({modal: true, width:340, height:170, title:"Warning", 
				buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return false;
		}
		}
		jQuery(function(){
			jQuery("#quickQuoteDialog").dialog({
					autoOpen : false,
					modal : true,
					title:"Quick Quote",
					height: 420,
					width: 520,
					buttons : {  },
					close:function(){
						$('#quickQuoteDialog').validationEngine('hideAll');
						return true;
					}
			});
	});

	function saveQuickQuote(){
		if(!$('#quickQuoteFormID').validationEngine('validate')) {
			return false;
		}
		var quickQuoteValues = $("#quickQuoteFormID").serialize();
		//var choosefile = $("#attachedID").val();
		//var quickQuote = quickQuoteValues/*& + "&choosefileattach="+choosefile*/;
		$.ajax({
			url: "./customerList/addQuickQuote",
			type: "POST",
			data: quickQuoteValues,
			success: function(data) {
				$("#OpenGridQuotes").trigger("reloadGrid"); 
				$("#customerLostQuotesGrid").trigger("reloadGrid"); 
				$("#customerQuotesGrid").trigger("reloadGrid"); 
				jQuery("#quickQuoteDialog").dialog("close");
			}
		});
		return true;
	}

	function viewQuickQuote(){
		var aInfo = true;
		if(aInfo){
			var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	}

	function faxQuickQuote(){
		var aInfo = true;
		if(aInfo){
			var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}		
	}
	
	function emailQuickQuote(){
		var aInfo = true;
		if(aInfo){
			var information = "This feature is in our workshop.  Will be released soon.  Thank you for your patience.";
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			return true;
		}
	}
	
	function cancelQuickQuote(){
		jQuery("#quickQuoteDialog").dialog("close");
		$('#quickQuoteDialog').validationEngine('hideAll');
		return true;
	}

	$(function() {var cache = {}; var lastXhr=''; $("#customerID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
		{ var rxMasterid = ui.item.id; $("#customerHiddenID").val(rxMasterid); /* var rxMasterid = ui.item.manufactureID; $("#rxMasterHiddenID").val(rxMasterid);*/},
		source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
		lastXhr = $.getJSON( "jobtabs4/customerContactList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

		$(function() {var cache = {}; var lastXhr=''; $("#productListID").autocomplete({ minLength: 2,timeout :1000,select: function( event, ui ) 
			{ 
				var quoteDetailID = ui.item.id; $("#productHiddenID").val(quoteDetailID); $.ajax({
					url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID },
					success: function(data) {
						$.each(data, function(index,value){
							var manufacture = value.inlineNote;
							var manufactureID = value.rxManufacturerID;
							$("#manufacturerID").val(manufacture);   $("#manufacterHiddenID").val(manufactureID);							
						});
					} }); },
			source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
			lastXhr = $.getJSON( "jobtabs2/productList", request, function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} }); });

		$(function() { var cache = {}; var lastXhr=''; $( "#manufacturerID" ).autocomplete({ minLength: 1, timeout :1000,
		select: function( event, ui ) { /*var id = ui.item.id;*/ var manufacId = ui.item.manufactureID; $("#manufacterHiddenID").val(manufacId); },
		source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
		lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
		cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); 
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

		function loadGridOpenQuotes(rxContactId){
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
			var grid = $('#OpenGridQuotes');
			$("#OpenGridQuotes").jqGrid({
				url:'customerList/contactIdbasedOpenQuotes',
				datatype:'JSON',
				postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
				mtype:'GET',
				colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Edit & View'],
				colModel:
					[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
	 				 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
					 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
				rowNum: 0, recordtext:'',rowList:[],pgtext:null,viewrecords:false,sortname:' ',sortorder:"asc",imgpath:'themes/basic/images',
				caption:'Open Quotes',height:180,rownumbers:true,altRows:true,altclass:'myAltRowClass',width:1100,
				loadComplete:function(data) {
					if (jQuery("#OpenGridQuotes").getGridParam("records") === 0) {
						emptyMsgDiv.show();
						emptyMsgDiv.insertAfter(grid.parent());
					} else {
						emptyMsgDiv.hide();
					}
				},
				gridComplete: function () {
					$(this).mouseover(function() {
				        var valId = $(".ui-state-hover").attr("id");
				        $(this).setSelection(valId, false);
				    });
				},
				jsonReader:{
					root:"rows",page:"page",total:"total",records:"records",repeatitems:false,
		            cell:"cell",id:"id",userdata:"userdata"
		    	},
		    	ondblClickRow: function(rowId) {
		    		var rowData = jQuery(this).getRowData(rowId); 
		    		var invoiceDate= rowData['quoteDate'];
		    		var jobNumber = rowData['jobNo'];
		    		var jobName = rowData['jobName'];
		    		var customerCon= rowData['customerContact'];
		    		var Quoteamt=rowData['quoteAmount'];
		    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
		    		var cost1=cost.replace(".00","");
		    		var contractAmt= rowData['contractAmount'];
		    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
		    		var price1=price.replace(".00","");
		    		var rev=rowData['quoteRev'];
		    		var joBidder=rowData['joBidderId'];
		    		var joMast=rowData['joMasterId'];
		    		$("#editQuoteJobNumberID").val(jobNumber);
		    		$("#editQuoteProductListID").val(jobName);
		    		$("#editQuoteDateID").val(invoiceDate);
		    		$("#editQuoteCustomerHiddenID").val(customerCon);
		    		$("#editQuoteCostID").val(cost1);
		    		$("#editQuoteSellPriceID").val(price1);
		    		$("#editQuoteRevisionID").val(rev);
		    		$("#editQuoteJobStatusID").val(1);
		    		$("#editQuoteJoBidderID").val(joBidder);
		    		$("#editQuoteJobMasterID").val(joMast);
		    		openAddnewProduct();
				},
				loadError: function(jqXHR, textStatus, errorThrown) {	}
			});
			return true;
		}
		function lostQuotesShow(){
			if($('#lostQuote').is(':checked')) {
				$("#Quote").prop("checked", false);
				$("#customerGridQuotes").hide();
				$("#Quotes").hide();
				$("#lostQuotes").show();
			}else if($('#Quote').is(':checked')){
				$("#lostQuote").prop("checked", false);
				$("#lostQuotes").hide();
				$("#customerGridQuotes").hide();
				$("#Quotes").show();
			}else{
				$("#Quote").prop("checked", false);
				$("#lostQuote").prop("checked", false);
				$("#customerGridQuotes").show();
				$("#lostQuotes").hide();
				$("#Quotes").hide();
			}
		}
		function loadLostQuotesGrid(rxContactId) {
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
			var grid = $('#customerLostQuotesGrid');
			$("#customerLostQuotesGrid").jqGrid({
				url:'customerList/customerLostQuotes',
				datatype:'JSON',
				postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
				mtype:'GET',
				colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Edit & View'],
				colModel:
					[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
	 				 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
					 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
				rowList:[],
				rowNum: 0, viewrecords:false, sortorder:"asc", imgpath: 'themes/basic/images', caption: 'Lost Quotes',
				pgbuttons: false,recordtext:'',rowList:[],pgtext: null, height:180, width:1100, rownumbers:true,
				altRows:true, altclass:'myAltRowClass',
				loadComplete:function(data) {
					if (jQuery("#customerLostQuotesGrid").getGridParam("records") === 0) {
						emptyMsgDiv.show();
						emptyMsgDiv.insertAfter(grid.parent());
						$("#customerLostQuotesGridPager").hide();
					} else {
						$("#customerLostQuotesGridPager").show();
						emptyMsgDiv.hide();
					}
				},
				gridComplete: function () {
					$(this).mouseover(function() {
				        var valId = $(".ui-state-hover").attr("id");
				        $(this).setSelection(valId, false);
				    });
				},
				jsonReader:{
					root:"rows", page:"page", total:"total", records:"records", repeatitems:false,
		            cell:"cell", id:"id", userdata:"userdata"
		    	},
		    	ondblClickRow: function(rowId) {
		    		var rowData = jQuery(this).getRowData(rowId); 
		    		var invoiceDate= rowData['quoteDate'];
		    		var jobNumber = rowData['jobNo'];
		    		var jobName = rowData['jobName'];
		    		var customerCon= rowData['customerContact'];
		    		var Quoteamt=rowData['quoteAmount'];
		    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
		    		var cost1=cost.replace(".00","");
		    		var contractAmt= rowData['contractAmount'];
		    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
		    		var price1=price.replace(".00","");
		    		var rev=rowData['quoteRev'];
		    		var joBidder=rowData['joBidderId'];
		    		var joMast=rowData['joMasterId'];
		    		$("#editQuoteJobNumberID").val(jobNumber);
		    		$("#editQuoteProductListID").val(jobName);
		    		$("#editQuoteDateID").val(invoiceDate);
		    		$("#editQuoteCustomerHiddenID").val(customerCon);
		    		$("#editQuoteCostID").val(cost1);
		    		$("#editQuoteSellPriceID").val(price1);
		    		$("#editQuoteRevisionID").val(rev);
		    		$("#editQuoteJobStatusID").val(1);
		    		$("#editQuoteJoBidderID").val(joBidder);
		    		$("#editQuoteJobMasterID").val(joMast);
		    		openAddnewProduct();
				},
				loadError: function(jqXHR, textStatus, errorThrown) {	}
			});
			return true;
		}


		function loadQuotesGrid(rxContactId) {
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			var emptyMsgDiv = $('<div align="center" style="font-size: 18px; padding-top: 50px;padding-right: 50px;"><b class="field_label"> Not Available </b></div>');
			var grid = $('#customerQuotesGrid');
			$("#customerQuotesGrid").jqGrid({
				url:'customerList/customerQuotes',
				datatype:'JSON',
				postData:{'rolodexNumber':rolodexNumber, 'rxContactId': rxContactId },
				mtype:'GET',
				colNames:[ 'Date', 'Job Number', 'Job Name', 'Contact',	'Est. Cost', 'Contract Amount','Revision','joBidderID','joMasterID', 'ContactID', 'RxMasterID', 'Job Status', 'Edit & View'],
				colModel:
					[{name:'quoteDate', index:'quoteDate',align:'center',width:40, editable:true, edittype:'text', editoptions:{size:30,readonly:true}, editrules:{edithidden:false,required:false}},
	 				 {name:'jobNo',index:'jobNo',align:'center',width:40,editable:true, editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobName',index:'jobName',align:'left',width:90,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'customerContact',index:'customerContact',align:'left',width:30,editable:true,edittype:'',editoptions:{},editrules:{edithidden:true,required:false}},
					 {name:'quoteAmount',index:'quoteAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'contractAmount',index:'contractAmount',align:'right',width:30,editable:true,edittype:'',formatter:currencyFormatter,editoptions:{}, editrules:{edithidden:true,required:false}},
					 {name:'quoteRev',index:'quoteRev',align:'middle',width:25,editable:true,edittype:'',editoptions:{}, hidden : true, editrules:{edithidden:true,required:false}},
					 {name:'joBidderId',index:'joBidderId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					  {name:'joMasterId',index:'joMasterId',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'contactID',index:'contactID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'rxMasterID',index:'rxMasterID',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'jobStatus',index:'jobStatus',align:'center',width:40,editable:true, hidden : true,  editoptions:{size:20,readonly:false,alignText:'right'},editrules:{edithidden:true,required:true}},
					 {name:'edit',index:'edit',align:'center',width:25, hidden: false, editrules:{edithidden:false},  formatter:imgFmatter}],
				rowList:[],
				rowNum: 0, viewrecords:false, sortorder:"asc", imgpath: 'themes/basic/images', caption: 'Quick Quotes',
				pgbuttons: false,recordtext:'',rowList:[],pgtext: null, height:180, width:1100, rownumbers:true,
				altRows:true, altclass:'myAltRowClass',
				loadComplete:function(data) {
					if (jQuery("#customerQuotesGrid").getGridParam("records") == 0) {
						emptyMsgDiv.show();
						emptyMsgDiv.insertAfter(grid.parent());
						$("#customerQuotesGridPager").hide();
					} else {
						$("#customerQuotesGridPager").show();
						emptyMsgDiv.hide();
					}
				},
				gridComplete: function () {
					$(this).mouseover(function() {
				        var valId = $(".ui-state-hover").attr("id");
				        $(this).setSelection(valId, false);
				    });
				},
				jsonReader:{
					root:"rows", page:"page", total:"total", records:"records", repeatitems:false,
		            cell:"cell", id:"id", userdata:"userdata"
		    	},
		    	ondblClickRow: function(rowId) {
		    		var rowData = jQuery(this).getRowData(rowId); 
		    		var invoiceDate= rowData['quoteDate'];
		    		var jobNumber = rowData['jobNo'];
		    		var jobName = rowData['jobName'];
		    		var customerCon= rowData['customerContact'];
		    		var Quoteamt=rowData['quoteAmount'];
		    		var cost = Quoteamt.replace(/[^0-9\.]+/g,"");
		    		var cost1=cost.replace(".00","");
		    		var contractAmt= rowData['contractAmount'];
		    		var price = contractAmt.replace(/[^0-9\.]+/g,"");
		    		var price1=price.replace(".00","");
		    		var rev=rowData['quoteRev'];
		    		var joBidder=rowData['joBidderId'];
		    		var joMast=rowData['joMasterId'];
		    		$("#editQuoteJobNumberID").val(jobNumber);
		    		$("#editQuoteProductListID").val(jobName);
		    		$("#editQuoteDateID").val(invoiceDate);
		    		$("#editQuoteCustomerHiddenID").val(customerCon);
		    		$("#editQuoteCostID").val(cost1);
		    		$("#editQuoteSellPriceID").val(price1);
		    		$("#editQuoteRevisionID").val(rev);
		    		$("#editQuoteJobStatusID").val(1);
		    		$("#editQuoteJoBidderID").val(joBidder);
		    		$("#editQuoteJobMasterID").val(joMast);
		    		openAddnewProduct();
				},
				loadError: function(jqXHR, textStatus, errorThrown) {	}
			});
			return true;
		}

		function imgFmatter(cellValue, options, rowObject){
			var element = "<div><a onclick='editQuickQuote()'><img src='./../resources/images/edit_quote.png' title='Edit Quote' align='middle' style='padding: 2px 5px;'></a>"+
													"<a onclick='viewJobPage()'><img src='./../resources/images/view_quote.png' title='View Job' align='middle' style='padding: 2px 5px;'></a></div>";
		  	 return element;
		}

		function viewJobPage(){
			var rowIdQuote;
			if ($('#lostQuote').is(':checked')){
				rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			}else if($('#Quote').is(':checked')){
				rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
			}else{
				rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
			}
			var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
			var newDialogDiv = jQuery(document.createElement('div'));
			if(rowId === null){
				var errorText = "Please add a record in contact grid.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				var arxContactId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
				$("#customerHiddenID1").val(arxContactId);
			}
			if(rowIdQuote === null){
				var errorText = "Please Select a record";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				var rowId;
				var aJobNumber = '';
				var aJobName = '';
				var aJobCustomer='';
				var aJobStatus = '';
				var joMasterId=0;
				
				
				if ($('#lostQuote').is(':checked')){
					rowId = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
					aJobName = $("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'jobName');
					joMasterId=$("#customerLostQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
	    			aJobCustomer='';
	    			aJobStatus = "Lost";
				}else if($('#Quote').is(':checked')){
					rowId = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobNo');
					aJobName = $("#customerQuotesGrid").jqGrid('getCell', rowId, 'jobName');
					joMasterId=$("#customerQuotesGrid").jqGrid('getCell', rowId, 'joMasterId');
	    			aJobCustomer='';
	    			aJobStatus = "Quote";
				}else{
					rowId = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobNo');
					aJobName = $("#OpenGridQuotes").jqGrid('getCell', rowId, 'jobName');
					joMasterId=$("#OpenGridQuotes").jqGrid('getCell', rowId, 'joMasterId');
	    			aJobCustomer='';
	    			aJobStatus = "Booked";
				}
				var urijobname=encodeBigurl(aJobName);
				var uricusname=encodeBigurl(aJobCustomer);
				var aQryStr = "jobNumber="+aJobNumber+"&jobName="+urijobname+"&jobCustomer="+uricusname+"&jobStatus="+aJobStatus+"&joMasterID="+joMasterId;
				document.location.href = "./jobflow?token=view&" + aQryStr;
			}
		}
		
		function editQuickQuote(){
			var rowIdQuote;
			if ($('#lostQuote').is(':checked')){
				rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
			}else if($('#Quote').is(':checked')){
				rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
			}else{
				rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
			}
			var rowId = $("#EmployeeDetailsGrid").jqGrid('getGridParam', 'selrow');
			var newDialogDiv = jQuery(document.createElement('div'));
			if(rowId === null){
				var errorText = "Please add a record in contact grid.";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				var arxContactId = $("#EmployeeDetailsGrid").jqGrid('getCell', rowId, 'rxContactId');
				$("#customerHiddenID1").val(arxContactId);
			}
			if(rowIdQuote === null){
				var errorText = "Please Select a record";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Warning", 
					buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}else{
				var aJobNumber = '';
				var aJobName = '';
				var aContact = '';
				var aQuoteAmount = '';
				var aContractAmount = '';
				var aRevision = '';
				var aJoBidderID = '';
				var aJoMasterID = '';
				var aDate = '';
				var aRxContactID = '';
				var aRxMasterID = '';
				var Status;
				if ($('#lostQuote').is(':checked')){
					rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
					rowIdQuote = $("#customerLostQuotesGrid").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobNo');
					aJobName = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobName');
					aContact = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'customerContact');
					aQuoteAmount = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteAmount');
					aContractAmount = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'contractAmount');
					aRevision = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteRev');
					aJoBidderID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'joBidderId');
					aJoMasterID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'joMasterId');
					aDate = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteDate');
					aRxContactID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'contactID');
					aRxMasterID = $("#customerLostQuotesGrid").jqGrid('getCell', rowIdQuote, 'rxMasterID');
					// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
					aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
					aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
					Status = 2;
				}else if($('#Quote').is(':checked')){
					rowIdQuote = $("#customerQuotesGrid").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobNo');
					aJobName = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobName');
					aContact = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'customerContact');
					aQuoteAmount = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteAmount');
					aContractAmount = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'contractAmount');
					aRevision = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteRev');
					aJoBidderID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'joBidderId');
					aJoMasterID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'joMasterId');
					aDate = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'quoteDate');
					aRxContactID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'contactID');
					aRxMasterID = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'rxMasterID');
					// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
					aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
					aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
					Status = 1;
				}else{
					rowIdQuote = $("#OpenGridQuotes").jqGrid('getGridParam', 'selrow');
					aJobNumber = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'jobNo');
					aJobName = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'jobName');
					aContact = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'customerContact');
					aQuoteAmount = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteAmount');
					aContractAmount = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'contractAmount');
					aRevision = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteRev');
					aJoBidderID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'joBidderId');
					aJoMasterID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'joMasterId');
					aDate = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'quoteDate');
					aRxContactID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'contactID');
					aRxMasterID = $("#OpenGridQuotes").jqGrid('getCell', rowIdQuote, 'rxMasterID');
					// aJobStatus = $("#customerQuotesGrid").jqGrid('getCell', rowIdQuote, 'jobStatus');
					aTotalCost = aQuoteAmount.replace(/[^0-9\.]+/g,"").replace(".00", ""); 
					aTotalPrice = aContractAmount.replace(/[^0-9\.]+/g,"").replace(".00", "");
					Status = 3;
				}
				$("#editQuoteRxContactID").val(aRxContactID); $("#editQuoteRxMasterID").val(aRxMasterID);
				$("#customerHiddenID").val(""); $("#editQuoteCustomerID").val(aContact);
				$("#editQuoteProductListID").val(aJobName); $("#editQuoteCostID").val(aTotalCost); $("#editQuoteSellPriceID").val(aTotalPrice);
				$("#editQuoteRevisionID").val(aRevision); $("#editQuoteDateID").val(aDate);
				$("#editQuoteJobNumberID").val(aJobNumber); $("#editQuoteJoBidderID").val(aJoBidderID); $("#editQuoteJobMasterID").val(aJoMasterID);
				$("#editQuoteJobStatusID option[value="+Status+"]").attr("selected", true);
				jQuery("#editQuickQuoteDialog").dialog("open");
			}
		}
		
		function updateQuickQuote(){
			if(!$('#editQuickQuoteFormID').validationEngine('validate')) {
				return false;
			}
			var quickQuoteValues = $("#editQuickQuoteFormID").serialize();
			$.ajax({
				url: "./customerList/updateQuickQuote",
				type: "POST",
				data: quickQuoteValues,
				success: function(data) {
					$("#OpenGridQuotes").trigger("reloadGrid"); 
					$("#customerLostQuotesGrid").trigger("reloadGrid"); 
					$("#customerQuotesGrid").trigger("reloadGrid"); 
					jQuery("#editQuickQuoteDialog").dialog("close");
				}
			});
			return true;
		}

		function cancelUpdateQuickQuote(){
			jQuery("#editQuickQuoteDialog").dialog("close");
			$('#editQuickQuoteDialog').validationEngine('hideAll');
		}

		jQuery(function(){
			jQuery("#editQuickQuoteDialog").dialog({
					autoOpen : false,
					modal : true,
					title:"Edit Quote",
					height: 380,
					width: 520,
					buttons : {  },
					close:function(){
						$('#editQuickQuoteDialog').validationEngine('hideAll');
						return true;
					}
			});
		});
		
		function QuotesShow(){
			if($('#Quote').is(':checked')) {
				$("#lostQuote").prop("checked", false);
				$("#customerGridQuotes").hide();
				$("#lostQuotes").hide();
				$("#Quotes").show();
			}else if($('#lostQuote').is(':checked')){
				$("#Quote").prop("checked", false);
				$("#lostQuotes").show();
				$("#customerGridQuotes").hide();
				$("#Quotes").hide();
			}else{
				$("#lostQuote").prop("checked", false);
				$("#Quote").prop("checked", false);
				$("#customerGridQuotes").show();
				$("#lostQuotes").hide();
				$("#Quotes").hide();
			}
		}

		function openAddnewProduct(){
			var joMasterid= $("#editQuoteJobMasterID").val();
			var theQuoteRev = $("#editQuoteRevisionID").val();
			var typeID = $("#TypeDetailID").val();
			var revision;
			$.ajax({
				url: "./customerList/QuoteHeaderID",
				type: "GET",
				data: {'joMasterId' : joMasterid,'quoteRev' : theQuoteRev,'quoteTypeId' :  typeID},
				success: function(data) {
					joQuoteHeaderID = data.joQuoteHeaderId;
					typeID = data.cuMasterTypeID;
					revision = data.quoteRev;
					$("#revisionId").val(revision);
					$("#joQuoteHeaderID").val(joQuoteHeaderID);
					if(joQuoteHeaderID === null){
						var errorText = "Please add a Quote in Quotes Tab.";
						var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).attr("id","msgDlg");
						jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
						jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
							buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
						return false;
					}
					else{
					$.ajax({
						url: "./customerList/typeID",
						type: "GET",
						data: {'quoteTypeId' :  typeID},
						success: function(data)	{
							$("#TypeDetail").val(data);
						}
					});
					$("#addquotesList").jqGrid('GridUnload');
					loadQuotesListDetails(joQuoteHeaderID);
					$("#addquotesList").trigger("reload");
					jQuery("#addProductDialog").dialog("open");	
					}	
				}
			});
		}

		jQuery(function(){
			jQuery("#addProductDialog").dialog({
					autoOpen : false,
					modal : true,
					title:"Add Product",
					height: 700,
					width: 850,
					buttons : {
					 "Close":function(){
						jQuery(this).dialog("close");
					} },
					close:function(){
						$('#addProductDialog').validationEngine('hideAll');
						return true;
					}
			});
		});

		var anOldPrice;
		var anOldCost;

		function loadQuotesListDetails(joQuoteHeaderID){
			var joQuoteheaderID = joQuoteHeaderID;
			$("#addquotesList").jqGrid({
				datatype: 'JSON',
				url:'./jobtabs2/getQuoteListDetails',
				mtype: 'GET',
				postData: {'joQuoteHeaderID':joQuoteheaderID},
				pager: jQuery('#addquotespager'),
				colNames:['Notes','Description', 'Qty.', 'Paragraph', 'Vendors','Cost', 'Sell Price', 'QuoteDetailID', 'ManufacturerID', 'QuoteHeaderID', 'FactoryID','Inline Note','Posiion','Foot Note'],
				colModel:[
					{name:'inlineItem',index:'inlineItem',align:'right',width:30,editable: false,hidden: true, formatter: inlineFormatter},
					{name:'product',index:'product',align:'left',width:60,editable:true,hidden:false, edittype:'text', cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}, editoptions:{size:30,readonly:false},editrules:{edithidden:false,required: true}},
					{name:'itemQuantity',index:'itemQuantity',align:'center',width:20,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'paragraph',index:'paragraph',align:'left',width:60, cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';},editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'manufacturer',index:'manufacturer',align:'left',width:60, cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';},editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required: true}},
					{name:'cost',index:'cost',align:'right',width:25,editable:true,hidden:false,formatter:customCurrencyFormatter, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'price',index:'price',align:'right',width:25,editable:true,hidden:false,formatter:customCurrencyFormatter, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'joQuoteDetailID',index:'joQuoteDetailID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'rxManufacturerID',index:'rxManufacturerID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'joQuoteHeaderID',index:'joQuoteHeaderID',align:'left',width:30,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'veFactoryId',index:'veFactoryId',align:'left',width:3090,editable:true,hidden: true, edittype:'text', editoptions:{size:30,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'inlineNote',index:'inlineNote',align:'left',width:60,editable:true,hidden: true, edittype:'textarea', editoptions:{cols:30, rows:2, size:40,readonly:false},editrules:{edithidden:false,required:false}},
					{name:'position',index:'position',align:'left',width:90,editable:true,hidden: true, edittype:'text', editoptions:{size:30},editrules:{edithidden:false,required:false}},
					{name:'productNote',index:'productNote',align:'left',width:60,editable:true,hidden: true, edittype:'textarea', editoptions:{ cols:30, rows:2, size:40,readonly:false},editrules:{edithidden:false,required:false}}
				],
				rowNum: 1000,pgbuttons: false,recordtext: '',rowList: [],pgtext: null,viewrecords: true,
				sortname: 'Product', sortorder: "asc", imgpath: 'themes/basic/images',caption: 'Line Items',
				height:450,width: 800,
				//scrollOffset: 0,
				loadComplete: function(data) {
					$("#addquotesList").setSelection(1, true);
					var gridData = jQuery("#addquotesList").getRowData();
					var totalcost = 0;
					var totalPrice = 0;
					for(var index = 0; index < gridData.length; index++){
						var rowData = gridData[index];
						var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
						totalcost = totalcost + Number(cost);
						var price = rowData["price"].replace(/[^0-9\.]+/g,"");
						totalPrice = totalPrice + Number(price);
					}
					$("#totalPriceID").val(formatCurrency(totalPrice));
					$("#totalCostID").val(formatCurrency(totalcost));
					var joQuoteHeader = $("#joQuoteHeaderID").val();
					$.ajax({
						url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
						type: "GET",
						data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
						success: function(data) { }
					}); 
				},
				afterInsertRow: function(rowid, aData) {
					var aPositionID = $("#addquotesList").jqGrid('getCell', rowid, 'position');
					if(aPositionID === '0'){
						var aQuoteDetailID = $("#addquotesList").jqGrid('getCell', rowid, 'joQuoteDetailID');
						$.ajax({
							url: "./jobtabs2/updateInlineItemPosition",
							type: "GET",
							data : { 'joQuoteDetailID' : aQuoteDetailID },
							success: function(data) {
								$("#addquotesList").trigger("reloadGrid");
							}
					 	});
					}
				},
				loadError : function (jqXHR, textStatus, errorThrown){	},
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
				onSelectRow: function(id){
					
				},
				editurl:'./jobtabs2/manpulaterProductQuotes'
			}).navGrid('#addquotespager',{alertzIndex: 3234,search: false,pager:false, alertcap: "Warning", alerttext: 'Please select a Product'},
					//-----------------------edit options----------------------//
					{
						closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,zIndex:1234,
						modal:true,jqModal:true,viewPagerButtons: false,editCaption: "Edit Product",width: 430, top: 235, left: 320,
						beforeShowForm: function (form) 
						{	
							var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
							var rowData = $("#addquotesList").getRowData(rowId);
					      	var Cost = rowData['cost'];
					        anOldCost = Cost;
					        var rowId = $("#addquotesList").jqGrid('getGridParam', 'selrow');
							var rowData = $("#addquotesList").getRowData(rowId);
					      	var Price = rowData['price'];
					      	anOldPrice = Price;
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('Description: ');
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').append('Qty.: ');
							jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').append('Paragraph: ');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('Vendors: ');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').append('Cost: ');
							jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').append('Sell Price: ');
							jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').append('Inline Note: ');
							jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').append('Foot Note: ');
							aGlobalVariable = "edit";
							var cost = $("#cost").val().replace(/[^0-9\.]+/g,"");
							var price = $("#price").val().replace(/[^0-9\.]+/g,"");
							$("#cost").val(Number(cost)); $("#price").val(Number(price));
							$(function() {var cache = {}; var lastXhr=''; $("#product").autocomplete({ minLength: 2,select: function( event, ui ) 
								{ 
									var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); $.ajax({
										url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
										success: function(data) {
											$.each(data, function(index,value){
												manufacture = value.inlineNote;
												var detail = value.paragraph;
												var quantity = value.itemQuantity;
												var manufactureID = value.rxManufacturerID;
												var factoryID = value.detailSequenceId;
												$("#paragraph").val(detail); $("#manufacturer").val(quantity); //$("#itemQuantity").val(quantity); 
												$("#rxManufacturerID").val(manufactureID);	$("#veFactoryId").val(factoryID);						
											});
										} }); },
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
									lastXhr = $.getJSON( "jobtabs2/productList", request, 
											function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
											error: function (result) {
											     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
											}  }); });
		
							$(function() { var cache = {}; var lastXhr='';
							$( "#manufacturer" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value;  
							 	$("#rxManufacturerID").val(id);
							 	$.ajax({
									url: "./jobtabs2/getFactoryID",
									type: "GET",
									data : { 'rxMasterID' : id,'descripition' : name },
									success: function(data) {
										$("#veFactoryId").val(data);
									}
							 	});
							  },
								source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
									lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
										cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
										error: function (result) {
										     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
							}  }); });
						},
						'onInitializeForm' : function(formid){
							jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_inlineNote').show();
							jQuery('#TblGrid_addquotesList #tr_productNote').show();
						},
						beforeSubmit: function(postdata, formid){
							
							if($("#quoteTypeDetail").val() === ''){
								return [false, "Please provide 'Type' and 'Submitted By'"];
							}else if($("#jobQuoteSubmittedBYFullName").val() === ''){
								return [false, "Please provide 'Type' and 'Submitted By'"];
							}
							return [true,""];
						},
						afterSubmit: function(){
							$('#cData').trigger('click');
							$("#addquotesList").trigger("reloadGrid");
							var gridData = jQuery("#addquotesList").getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for(var index = 0; index < gridData.length; index++){
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(/[^0-9\.]+/g,"");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = $("#cost").val().replace(/[^0-9\.]+/g,"");
							var costValue = aCurrentCost-anOldCost;
							var aCurrentPrice = $("#price").val().replace(/[^0-9\.]+/g,"");
							var priceValue = aCurrentPrice-anOldPrice;
							totalcost = totalcost + costValue;
							totalPrice = totalPrice + priceValue;
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$.ajax({
								url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
								type: "GET",
								data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
								success: function(data) {
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
															buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
								jQuery(this).dialog("close");
								return true;
								}
							});
						},
						onclickSubmit: function(params){
							
						}
					},
					//-----------------------add options----------------------//
					{
						closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
						modal:true,
						jqModal:true,
						viewPagerButtons: false,
						addCaption: "Add Product",
						width: 430, top: 235, left: 320,zIndex:1234,
						beforeShowForm: function (form) 
						{
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('Description: ');
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_itemQuantity .CaptionTD').append('Qty.: ');
							jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_paragraph .CaptionTD').append('Paragraph: ');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('Vendors: ');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_cost .CaptionTD').append('Cost: ');
							jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_price .CaptionTD').append('Sell Price: ');
							jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_inlineNote .CaptionTD').append('Inline Note: ');
							jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').empty();
							jQuery('#TblGrid_addquotesList #tr_productNote .CaptionTD').append('Foot Note: ');
							$(function() {var cache = {}; var lastXhr=''; $("#product").autocomplete({ minLength: 2,select: function( event, ui ) 
								{ 
									var quoteDetailID = ui.item.id; var rxMasterID = ui.item.manufactureID; $("#quoteDetailID").val(quoteDetailID); $.ajax({
										url: "./jobtabs2/quoteDetails", mType: "GET", data: { 'quoteDetailID' : quoteDetailID, 'rxMasterID' : rxMasterID },
										success: function(data) {
											$.each(data, function(index,value){
												manufacture = value.inlineNote;
												var detail = value.paragraph;
												var quantity = value.itemQuantity;
												var manufactureID = value.rxManufacturerID;
												var factoryID = value.detailSequenceId;
												$("#paragraph").val(detail); $("#manufacturer").val(quantity); //$("#itemQuantity").val(quantity); 
												$("#rxManufacturerID").val(manufactureID);	$("#veFactoryId").val(factoryID);						
											});
										} }); },
								source: function( request, response ) { var term = request.term; if ( term in cache ) { response( cache[ term ] ); 	return; 	} 
									lastXhr = $.getJSON( "jobtabs2/productList", request, 
											function( data, status, xhr ) { cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); 	} }); },
											error: function (result) {
											     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
											}  }); });
		
							$(function() { var cache = {}; var lastXhr='';
							$( "#manufacturer" ).autocomplete({ minLength: 1, select: function( event, ui ) { var id = ui.item.id; var name = ui.item.value;  
							 	$("#rxManufacturerID").val(id);
							 	$.ajax({
									url: "./jobtabs2/getFactoryID",
									type: "GET",
									data : { 'rxMasterID' : id,'descripition' : name },
									success: function(data) {
										$("#veFactoryId").val(data);
									}
							 	});
							  },
								source: function( request, response ) {var term = request.term; if ( term in cache ) {response(cache[term]); 	return;}
									lastXhr = $.getJSON( "jobtabs2/vendorsList", request, function( data, status, xhr ) {
										cache[ term ] = data; if ( xhr === lastXhr ) { response( data ); } }); },
										error: function (result) {
										     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
							}  }); });
						},
						'onInitializeForm' : function(formid){
							jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 5 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" title="Must be atleast 1 characters long" style="padding-left: 10px;"><br>');
							jQuery('#TblGrid_addquotesList #tr_product .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .DataTD').append('<label>(Must be atleast 2 characters long)</label>');
							jQuery('#TblGrid_addquotesList #tr_manufacturer .CaptionTD').append('<span style="color:red;"> *</span>');
							jQuery('#TblGrid_addquotesList #tr_inlineNote').show();
							jQuery('#TblGrid_addquotesList #tr_productNote').show();
						},
						onclickSubmit: function(params){
							aGlobalVariable = "add";
							var joQuoteHeaderIdentity= joQuoteheaderID;
							if(aGlobalVariable !== "edit"){
								$("#joQuoteHeaderID").val(joQuoteHeaderIdentity);
								$('#addquotesList').jqGrid('setGridParam',{postData: {'joQuoteHeaderID': joQuoteHeaderIdentity}});
								jQuery("#joQuoteheader").text(joQuoteHeaderIdentity);
								joQuoteheaderID = joQuoteHeaderIdentity;
								$("#joHeaderID").val(joQuoteheaderID);
							}
							if(Number(joQuoteHeaderIdentity) === 0){
								var name = $("#joHeaderID").val();
								$('#addquotesList').jqGrid('setGridParam',{postData:{'joQuoteHeaderID': name}});
								jQuery("#joQuoteheader").text(name);
								joQuoteheaderID = name;
								$("#joHeaderID").val(joQuoteheaderID);
							} 
							if(joQuoteHeaderIdentity > joQuoteheaderID){
								joQuoteheaderID = joQuoteHeaderIdentity;
							}
							return { 'joQuoteHeaderID' : joQuoteheaderID, 'quoteheaderid' : joQuoteHeaderIdentity};
						},
						beforeSubmit: function(postdata, formid){
							aGlobalVariable = "edit";
							if($("#quoteTypeDetail").val() === ''){
								return [false, "Please provide 'Type' and 'Submitted By'"];
							}else if($("#jobQuoteSubmittedBYFullName").val() === ''){
								return [false, "Please provide 'Type' and 'Submitted By'"];
							}
							return [true,""];
						},
						afterSubmit: function(){
							$('#cData').trigger('click');
							$("#addquotesList").trigger("reloadGrid"); 
							var gridData = jQuery("#addquotesList").getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for(var index = 0; index < gridData.length; index++){
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(/[^0-9\.]+/g,"");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = $("#cost").val().replace(/[^0-9\.]+/g,"");
							var aCurrentPrice = $("#price").val().replace(/[^0-9\.]+/g,"");
							totalcost = totalcost + Number(aCurrentCost);
							totalPrice = totalPrice + Number(aCurrentPrice);
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$.ajax({
								url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
								type: "GET",
								data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
								success: function(data) {
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
															buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
								jQuery(this).dialog("close");
								return true;
								}
							});
						}
					},
					//-----------------------Delete options----------------------//
					{
						closeOnEscape: true, reloadAfterSubmit: true, modal:true, width:270, jqModal:true, top: 296, left: 450,zIndex:1234,
						caption: "Delete Product",
						msg: 'Delete the Product?',
						onclickSubmit: function(params){
							aGlobalVariable = "del";
							var id = jQuery("#addquotesList").jqGrid('getGridParam','selrow');
							var key = jQuery("#addquotesList").getCell(id, 7);
							var costValue = jQuery("#addquotesList").getCell(id, 5);
							var priceValue = jQuery("#addquotesList").getCell(id, 6);
							var gridData = jQuery("#addquotesList").getRowData();
							var totalcost = 0;
							var totalPrice = 0;
							for(var index = 0; index < gridData.length; index++){
								var rowData = gridData[index];
								var cost = rowData["cost"].replace(/[^0-9\.]+/g,"");
								totalcost = totalcost + Number(cost);
								var price = rowData["price"].replace(/[^0-9\.]+/g,"");
								totalPrice = totalPrice + Number(price);
							}
							var aCurrentCost = costValue.replace(/[^0-9\.]+/g,"");
							var aCurrentPrice = priceValue.replace(/[^0-9\.]+/g,"");
							totalcost = totalcost - Number(aCurrentCost);
							totalPrice = totalPrice - Number(aCurrentPrice);
							var joQuoteHeader = $("#joQuoteHeaderID").val();
							$.ajax({
								url: "./jobtabs2/SaveQuoteCustomerDetailInfo",
								type: "GET",
								data: {'totalCost' : totalcost, 'totalPrice' : totalPrice, 'joQuoteHeaderID' :  joQuoteHeader},
								success: function(data) {
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span><b style="color:Green;">Quote details updated successfully.</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
															buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
								jQuery(this).dialog("close");
								return true;
								}
							});
							return { 'joQuoteDetailID' : key};
						}
					}
				);
			}

		function inlineFormatter(cellValue, options, rowObject)
		{
			var element = "<div style='width: 52px;'><a onclick='openLineItemDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/lineItem.png' title='Inline Item' align='middle' style='vertical-align: middle;'></a>"+
									"<a onclick='openFooterLineDialog()' style='float: left;padding: 0px 5px;'><img src='./../resources/images/footNote.png' title='Foot Note' align='middle' style='vertical-align: middle;'></a></div>";
		  	 return element;
		} 

		function customCurrencyFormatter(cellValue, options, rowObject) {
			return formatCurrency(cellValue);
		}

		$(function() { var cache = {}; var lastXhr='';
		$("#TypeDetail").autocomplete({
			minLength: 1, timeout: 1000,
			select: function(event, ui) {
				var id = ui.item.id; 
				$("#TypeDetailID").val(id);
			},
			source: function(request, response) {var term = request.term; if (term in cache) {response(cache[term]); return;}
				lastXhr = $.getJSON("employeeCrud/customerType", request, function(data, status, xhr) {
					cache[term] = data; if (xhr === lastXhr) {response(data); } 
				});
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} 
		});
	});
		
		function currencyFormatter(cellValue, options, rowObject) {
			return formatCurrency(cellValue);
		}

		function updateRolodexName()
		{
			if(!$('#rolodexdfromID').validationEngine('validate')) {
				return false;
			}
			var rolodexName = $('#customerNameHeader').val();
			var rolodexId = getUrlVars()["rolodexNumber"];
			$.ajax({
				url: "./customerList/updateCustomerName",
				mType: "GET",
				data : { 'customer' : rolodexName, 'customerId' :rolodexId },
				success: function(data) {
					document.location.href = "./rolodexdetails?rolodexNumber="+rolodexId+"&name="+rolodexName+"";
					//window.location.reload();
				}
			});
		}
		
	</script>
