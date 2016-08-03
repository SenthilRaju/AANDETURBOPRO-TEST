<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div id="settingsFormDetails">
   <form action="" id="companyFormId" name="companyForm">
  	  <table align="center">
		 <tr>
			<td>
				<fieldset  class= " ui-widget-content ui-corner-all" style="width:800px;height:600px;">
					<table>
						<tr style="height: 20px;"><td></td></tr>
	       				<tr align="left">
	        				<td>
                				<label>Upload Your Company Logo:</label>
			    				<input type="file" name="datasize" size="30" id="fileId" style="height:20px;">
							    <input type="submit" value="Upload" id="ucompanydetailspload" name="uploadimage"style="height:26px; background:#d3d3d3; color:#000 " onclick="imageUpload()">
	            			</td>
						</tr>
						<tr align="center">
						  <td>
							<table>
								<tr align="center">
									<td><label>Quote</label>
										<input type="checkbox" id="1quote" name="quote" style="vertical-align: middle;" >
									</td>
									<td><label>Quick Quote</label>
										<input type="checkbox" id="1quickquote" name="quickquote" style="vertical-align: middle;" >
									</td>
									<td><label>Invoices</label>
										<input type="checkbox" id="1invoices" name="invoices" style="vertical-align: middle;" >
									</td>
									<td><label>Purchase Orders</label>
										<input type="checkbox" id="1puchaseorder" name="puchaseorder" style="vertical-align: middle;" >
									</td>
								</tr>
							</table>
							<br>
						  </td>
						</tr>
						<tr align="center">
						<td>
							<table>
								<tr>
	   								<td style="width: 160px;text-align: right;"><label>Header:</label></td>
									<td>
	   									<textarea cols="70" id="headerTextId" style="height: 120px;">${requestScope.headerTextSettings}</textarea>
	   								</td>
	   							</tr>
	   						</table>
	   					</td>
	   					</tr>
	   					<tr align="center"><td>
							<table>
      							<tr align="center">
									<td><label>Quote</label>
										<input type="checkbox" id="quote11" name="quote1" style="vertical-align: middle;" >
									</td>
									<td><label>Quick Quote</label>
										<input type="checkbox" id="quickquote11" name="quickquote1" style="vertical-align: middle;" >
									</td>
									<td><label>Invoices</label>
										<input type="checkbox" id="invoices11" name="invoices1" style="vertical-align: middle;" >
									</td>
									<td><label>Purchase Orders</label>
										<input type="checkbox" id="puchaseorder11" name="puchaseorder1" style="vertical-align: middle;" >
									</td>
	   							</tr>
							</table><br>
						</td></tr>
						<tr align="center">
						<td>
							<table>
								<tr>
									<td style="width: 160px;text-align: right;"><label>Terms:</label></td>
									<td>
									<textarea cols="70" id="termId" style="height: 120px;">${requestScope.termsTextSettings}</textarea>
									</td>
								</tr>
							</table>
							</td></tr>
							<tr align="center"><td>
								<table>
									<tr align="center">
										<td><label>Quote</label>
											<input type="checkbox" id="quote21" name="quote2" style="vertical-align: middle;" >
										</td>
										<td><label>Quick Quote</label>
											<input type="checkbox" id="quickquote21" name="quickquote2" style="vertical-align: middle;" >
										</td>
										<td><label>Invoices</label>
											<input type="checkbox" id="invoices21" name="invoices2" style="vertical-align: middle;" >
										</td>
										<td><label>Purchase Orders</label>
											<input type="checkbox" id="puchaseorder21" name="puchaseorder2" style="vertical-align: middle;" >
										</td>
									</tr>
								</table><br>
								</td></tr>
					</table>
					<hr style="width: 780px;">
					<table align="center" style="width:600px;">
						<tr>
							<td></td>
							<td align="right" style="padding-left:640px;">
								<input type="button" class="savehoverbutton turbo-blue" value="Save" onclick="saveCompanySetting()" style=" width:80px;">
							</td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>
	</form>
</div>
<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
<script type="text/javascript">
	bkLib.onDomLoaded(function() { nicEditors.allTextAreas(); });
jQuery(document).ready(function() {
	//var aHeaderTxt = "${requestScope.headerTextSettings}";
	//var aTerms = "${requestScope.termsTextSettings}";
	//$("#headerTextId").val(aHeaderTxt);
	//$("#termId").val(aTerms);
	var aQuote = "${requestScope.userLoginSettings.quote}";
	var aQuickQuote = "${requestScope.userLoginSettings.quickQuote}";
	var aInvoices = "${requestScope.userLoginSettings.invoices}";
	var aPurchaseOrder = "${requestScope.userLoginSettings.purchaseOrders}";
	var aQuote1 = "${requestScope.userLoginSettings.headerQuote}";
	var aQuickQuote1 = "${requestScope.userLoginSettings.headerQuickQuote}";
	var aInvoices1 = "${requestScope.userLoginSettings.headerInvoices}";
	var aPurchaseOrder1 = "${requestScope.userLoginSettings.headerPurchaseOrders}";
	var aQuote2 = "${requestScope.userLoginSettings.termsQuote}";
	var aQuickQuote2 = "${requestScope.userLoginSettings.termsQuickQuote}";
	var aInvoices2 = "${requestScope.userLoginSettings.termsInvoices}";
	var aPurchaseOrder2 = "${requestScope.userLoginSettings.termsPurchaseOrders}";

	if(aQuote === '1'){
		$("#1quote").attr("checked", true);
	}
	if(aQuickQuote === '1'){
		$("#1quickquote").attr("checked", true);
	}
	if(aInvoices === '1'){
		$("#1invoices").attr("checked", true);
	}
	if(aPurchaseOrder === '1'){
		$("#1puchaseorder").attr("checked", true);
	}
	if(aQuote1 === '1'){
		$("#quote11").attr("checked", true);
	}
	if(aQuickQuote1 === '1'){
		$("#quickquote11").attr("checked", true);
	}
	if(aInvoices1 === '1'){
		$("#invoices11").attr("checked", true);
	}
	if(aPurchaseOrder1 === '1'){
		$("#puchaseorder11").attr("checked", true);
	}
	if(aQuote2 === '1'){
		$("#quote21").attr("checked", true);
	}
	if(aQuickQuote2 === '1'){
		$("#quickquote21").attr("checked", true);
	}
	if(aInvoices2 === '1'){
		$("#invoices21").attr("checked", true);
	}
	if(aPurchaseOrder2 === '1'){
		$("#puchaseorder21").attr("checked", true);
	}
});

function saveCompanySetting(){
	var companySeri = $("#companyFormId").serialize();
	var headerText= nicEditors.findEditor('headerTextId').getContent();
	var terms =nicEditors.findEditor('termId').getContent();
	var termsReplace = terms.replace(/&/g,"and");
	var companydetails = companySeri+"&headerText="+headerText+"&terms="+termsReplace;
	$.ajax({
		url: "./userlistcontroller/companySettings",
		mType: "GET",
		data : companydetails,
		success: function(data){
			var newDialogDiv = jQuery(document.createElement('div'));
			jQuery(newDialogDiv).html('<span><b style="color:Green;">User details updated.</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Success", 
									buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); 
									document.location.href = "./settings"; }}]}).dialog("open");
				return true;
			}
	});
}

function imageUpload(){
	var file=document.getElementById("fileId").form.id;
}

var aBillToAddress = "${requestScope.wareHouse}";

</script>
</body>
</html>