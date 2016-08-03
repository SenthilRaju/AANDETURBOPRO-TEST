<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="addUserDefaultsDialog">
	<form action="" id="userDefaultFormId">
		<div id="userDefaultPage">
			<table>
				<tr>
					<td>
						<fieldset  class= " ui-widget-content ui-corner-all" style="width: 355px;height: 100px;">
							<legend><label><b></b></label></legend>
							<table style="width: 350px;">
								<tr>
									<td><label>Warehouse: </label></td>
									<td align="left">
											<select id="whrhouseheaderID" name="warehouseName" style="width: 245px;">
													<option value="0"> -Select- </option>														
											</select>
									</td>
								</tr>
								<tr>			
									<td><label>Division:</label></td>
									<td>
											<select id="cODivisionID" name="divisionName" style="width: 245px;">
													<option value="0"> -Select- </option>														
											</select>
									</td>
								</tr>
								 <input type="hidden" name="loginuserID" id="loginuserID"/>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td align="right" style="padding-right:25px;"><input type="button" id="saveUserButton" name="saveUserButtonName" value="Save & Close" class="savehoverbutton turbo-blue" onclick="saveUserDefault()" style=" width:105px;">
					<input type="button" id="cancelUserButton" name="cancelUserButtonname" value="Cancel" class="cancelhoverbutton turbo-blue" style="width: 65px;" onclick="cancelUserDefault()"></td>
				</tr>
			</table>
		</div>
	</form>
</div>
	<div class="jobListWizardDivColor">
		<ul>
			<li><a href="jobtabs1/jobwizardmain" onclick="createtpusage('job-Main Tab','view','Info','Job,View,Main Tab');setHeaderDetails();">Main</a></li>
			<c:if test="${QuoteGroupPermission==true}"> 
			<li><a href="jobtabs2/jobwizardquotes" id="jobquotesid" class="requiredSave" onclick="createtpusage('job-Quotes Tab','view','Info','Job,View,Quote Tab');">Quotes</a></li>
			</c:if>
			<c:if test="${SubmittalGroupPermission==true}">
			<li><a href="jobtabs3/jobwizard_submittal" class="requiredSave" onclick="createtpusage('job-Submittal Tab','view','Info','Job,View,Submittal Tab');">Submittal</a></li>
			</c:if>
			<c:if test="${CreditGroupPermission==true}">
			<li><a href="jobtabs4/jobwizard_credit" class="requiredSave" onclick="createtpusage('job-Credit Tab','view','Info','Job,View,Credit Tab');">Credit</a></li>
			</c:if>
			<c:if test="${ReleaseGroupPermission==true}">
			<li><a href="jobtabs5/jobwizard_release" class="requiredSave" id="jobreleasetab" onclick="createtpusage('job-Releases Tab','view','Info','Job,View,Releases Tab');">Releases</a></li>
			</c:if>
			<c:if test="${FinancialGroupPermission==true}">
			<li><a href="jobtabs6/jobwizard_financial" id="financialtab" class="requiredSave" onclick="createtpusage('job-Financials Tab','view','Info','Job,View,Financials Tab');">Financials</a></li>
			</c:if>
			<c:if test="${JournalGroupPermission==true}">
			<li><a href="jobtabs7/jobwizard_journal" class="requiredSave" id="jobTabJournalHeader" onclick="createtpusage('job-Journal Tab','view','Info','Job,View,Journal Tab');">Journal</a>
			</c:if>
			<input id="joMasterID" type="hidden" value="${requestScope.joMasterID}"/></li>
			<!--
			<li><a onclick="showTab('Main')">Main</a></li>
			<li><a onclick="showTab('Quotes')" id="jobquotesid" class="requiredSave">Quotes</a></li>
			<li><a onclick="showTab('Submittal')" class="requiredSave">Submittal</a></li>
			<li><a onclick="showTab('Credit')" class="requiredSave">Credit</a></li>
			<li><a onclick="showTab('Release')" class="requiredSave" id="jobreleasetab">Releases</a></li>
			<li><a onclick="showTab('Financial')" id="financialtab" class="requiredSave">Financials</a></li>
			<li><a onclick="showTab('Journal')" class="requiredSave" id="jobTabJournalHeader">Journal</a><input id="joMasterID" type="hidden" value="${requestScope.joMasterID}"/></li> 
			 -->
			
			<li id="viewwebpage"><input type="text" id="webpageurltoopen" placeholder="[Click to set WebPage]" value="${requestScope.appletUrl }"/></li>
<!-- 	   <li><input type="submit"  value="Go" onClick="accessAppletMethod()" /></li> -->
<!-- 	   <APPLET CODE="TurboApplet.class" archive="./../turboappletswing1.jar"    style="width: 30px; height: 25px; float: left;" id="AppletABC"></APPLET> -->
			
			<!-- Commented By:Naveed 15Oct2015, BID#889 -->
			<!-- <APPLET CODE="TurboApplet.class" archive="../turboapplet.jar"    style="width: 30px; height: 25px; float: left;" id="AppletABC"></APPLET> -->
			
				<li style="float: right;display: none;"><a id="bidDateReportID" onclick="reportCriteriaPage()" style="cursor: hand; background: url(./../resources/Icons/bid_report.png) no-repeat; height: 30px;padding-left: 19px;" title="Bid Date Report"></a></li>
			<li style="float: right">
				<label id="joMaster_ID"  style="display: none;">${requestScope.joMasterID}</label>
				<label id="jobNumber_ID"  style="display: none;">${requestScope.jobNumber}</label>
				<label id="jobName_ID"  style="display: none;">${requestScope.jobName}</label>
				<label id="jobCustomerName_ID" style="display: none;">${requestScope.CustomerName}</label>
				<label id="jobStatus_ID"  style="display: none;">${requestScope.jobStatusID}</label>
				<label id="jobQuoteNumber_ID"  style="display: none;">${requestScope.jobQuoteNumber}</label>
				<label id="jobCreditHold_ID" style="display: none;">${requestScope.cuMasterDetails.creditHold}</label>
				<label id="rxCustomer_ID" style="display: none;">${requestScope.rxCustomerID}</label>
				<label id="systemAdmin_ID" style="display: none;">${sessionScope.user.systemAdministrator}</label>
				<input type="hidden" id="userLogin_Id" value="${sessionScope.user.userId}">
				<label id="jobType_ID" style="display: none;">${requestScope.jobtype}</label>
				<label id="manufacturerSubmital_ID" style="display: none;"></label>
				<label id="vePOVendorInvoice_ID"></label>
				<label style="color: #3F3731;" >Status: </label>
				<label id="isAdmin" style="display: none;">${requestScope.isAdmin}</label>
				<select id="jobStatusList" onchange="ChangeJobStatus()">
					<option value="0">Bid</option>
					<option value="-4">Planning</option>
					<option value="6">Budget</option>
					<option value="1">Quote</option>
					<option value="5">Submitted</option>
					<option value="3">Booked</option>
					<option value="4">Closed</option>
					<option value="2">Lost</option>
					<option value="-1">Abandoned</option>
					<option value="-2">Rejected</option>
					<option value="-3">Over Budget</option>
				</select>
		</ul>
	</div>
	<div style="display: none;">
	<input type="text" id="CustomerCategory1hidden" name="CustomerCategory1hidden" value="${requestScope.CustomerCategory1}">
	<input type="text" id="CustomerCategory2hidden" name="CustomerCategory2hidden" value="${requestScope.CustomerCategory2}">
	<input type="text" id="CustomerCategory3hidden" name="CustomerCategory3hidden" value="${requestScope.CustomerCategory3}">
	<input type="text" id="CustomerCategory4hidden" name="CustomerCategory4hidden" value="${requestScope.CustomerCategory4}">
	<input type="text" id="CustomerCategory5hidden" name="CustomerCategory5hidden" value="${requestScope.CustomerCategory5}">
	</div>
	<div style="display: none;">
	<input type="text" id="Category1hidden" name="Category1hidden" value="${requestScope.Category1Desc}">
	<input type="text" id="Category2hidden" name="Category2hidden" value="${requestScope.Category2Desc}">
	</div>
			
		<script language="Javascript">
		jQuery(document).ready(function(){
			//showTab('Main');
			try{
				if(getURLParameter('from')=='projects'){			
				 	 $('#financialtab').trigger('click');
			 }	
			}catch(err){
				alert(err.message);
			}
			 
		});
		
		function showTab(tabName)
		{
			var hrefVal = "";
			if(tabName === 'Main')
				hrefVal = "jobtabs1/jobwizardmain";
			if(tabName === 'Quotes')
				hrefVal = "jobtabs2/jobwizardquotes";
			if(tabName === 'Submittal')
				 hrefVal ="jobtabs3/jobwizard_submittal"; 
			if(tabName === 'Credit')
				hrefVal = "jobtabs4/jobwizard_credit";
			if(tabName === 'Release')
				hrefVal = "jobtabs5/jobwizard_release";
			if(tabName === 'Financial')
				hrefVal = "jobtabs6/jobwizard_financial"
			if(tabName === 'Journal')
				hrefVal = "jobtabs7/jobwizard_journal";
			
			var UserLoginID=$("#userLogin_Id").val();
			//alert('accessPage = Project , userGroupID = 0 , UserLoginID = '+UserLoginID);
			$.ajax({
			    url: "./getSysPrivilage",
			    data: {'accessPage': tabName,'userGroupID':0, 'UserLoginID':UserLoginID},
			    type: 'POST',
			    success: function(data){
			    	//alert(data.Value);
			    	if(data.Value == "granted")
			    	{
			    		document.location.href = document.location.href+hrefVal;		
			    	}
			    	else
			    		showDeniedPopup();
			    }, error: function(error) {
			    	showDeniedPopup();
				}
			});	
		}
 function accessAppletMethod()
 {
 try{
	
 var url = $("#webpageurltoopen").val();
 console.log('webpageurltoopen..'+url);
 document.getElementById("AppletABC").invokeURL('test');
 // 	$("#AppletABC").invokeURL(url);
 	}catch(e){
 	console.log('niaz'+e);
 	}
 }

function getURL(){
	var url = $("#webpageurltoopen").val();
	var href = location.href;
	var jobNumber = getParameterByName('jobNumber');
	console.log('url == '+url+' >>>  '+href+" || jobNumber = "+jobNumber);
	
	
	// This script sets OSName variable as follows:
	// "Windows"    for all versions of Windows
	// "MacOS"      for all versions of Macintosh OS
	// "Linux"      for all versions of Linux
	// "UNIX"       for all other UNIX flavors 
	// "Unknown OS" indicates failure to detect the OS

	var OSName="Unknown OS";
	if (navigator.appVersion.indexOf("Win")!=-1) OSName="Windows";
	if (navigator.appVersion.indexOf("Mac")!=-1) OSName="MacOS";
	if (navigator.appVersion.indexOf("X11")!=-1) OSName="UNIX";
	if (navigator.appVersion.indexOf("Linux")!=-1) OSName="Linux";

// 	alert('Your OS: '+OSName);
	var url1 = "";
		
		url1 = ("file:///"+url.replace("file:///",""));
		url1 =url1.replaceAll("%","%25");
		url1=url1.replaceAll(/ /g, '%20');
		url1 =url1.replaceAll("#","%23");
		url1=url1.replaceAll("^","%5E");
		url1=url1.replaceAll("{","%7B");
		url1=url1.replaceAll("}","%7D");
		url1=url1.replaceAll("[","%5B");
		url1=url1.replaceAll("]","%5D");
		url1=url1.replaceAll("`","%60");
		console.log("url1"+url1);

	Updateintotable(jobNumber,url);
	//url1="file:///C:\Users\velmurugan\Desktop\local";
	return url1;
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.href);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function callquotestab(){
	
}
function alertURL(msg){
	alert('Entered URL has error'+msg);
}

function getURLParameter(name) {
	  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null
}
function Updateintotable(jobnumber,url){

	 $.ajax({
		url: "./jobtabs7/saveAppletLocalFileURL",
		type: "POST",
		data : {"jobNumber" : jobnumber, "urlValue" : url},
		success: function(data) {
			
		}
	});
}
String.prototype.replaceAll = function(target, replacement) {
	  return this.split(target).join(replacement);
	};

</script>
		
	<!--	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobListWizard.min.js"></script>   -->
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/joblistwizard.js"></script>
	 
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<!--  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/tinymce/tinymce.min.js"></script> -->
	  <script>
	 
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

		
		/* function myCustomInitInstance(ed) {  
			alert("inis");
			  try{
			    ed.execCommand("fontName", false, "Helvetica");    
			    ed.execCommand("fontSize", false, "10pt");    
			  }
			  catch(err){      
			  }
			} */
	 </script> 
	 
	 