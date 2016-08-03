<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<div class="jobListWizardDivColor">
		<ul>
			<li><a href="jobtabs1/jobwizardmain">Main</a></li>
			<li><a href="jobtabs2/jobwizardquotes" class="requiredSave">Quotes</a></li>
			<li><a href="jobtabs3/jobwizard_submittal" class="requiredSave">Submittal</a></li>
			<li><a href="jobtabs4/jobwizard_credit" class="requiredSave">Credit</a></li>
			<li><a href="jobtabs5/jobwizard_release" class="requiredSave">Releases</a></li>
			<li><a href="jobtabs6/jobwizard_financial" class="requiredSave">Financials</a></li>
			<li><a href="jobtabs7/jobwizard_journal" class="requiredSave">Journal</a></li>
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
	<!--	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/jobListWizard.min.js"></script>   -->
	 <script type="text/javascript" src="./../resources/scripts/turbo_scripts/joblistwizard.js"></script>