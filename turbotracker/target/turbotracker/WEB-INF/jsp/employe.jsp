<div id="employeetab">
	<table>
		<tr>
			<td style="padding-bottom: 10px;"><label>Employee name: </label>
			<input type="text" name="EmployeeName" size="32" style="width:240px" id="custName"/></td>
		</tr>
		<tr>
			<td colspan="3">
				<div id="tabs_sub" style="width: 1100px; height: 580px;">
					<ul>
						<li><a href="#tabsindex3">General</a></li>
						<li><a href="#tabsindex4">Directory</a></li>
						<li><a href="#tabsindex5">Commission</a></li>
						<li><a href="#tabsindex6">Payroll</a></li>
					</ul>
					<div id="tabsindex3">
						<form action="">
							<table>
								<tr>
									<td width=""><label>Status</label></td>
									<td width="160"><select><option value="Active">Active</option>
											<option value="Inactive">Inactive</option></select></td>
								</tr>
								<tr>
									<td width=""><label>Type</label></td>
									<td width="160"><input type="text" name="First_Name"
										size="" value="" /></td>
								</tr>
								<tr>
									<td width=""><label>Marital Status</label></td>
									<td width="160"><input type="text" name="First_Name"
										size="" value="" /></td>
								</tr>
								<tr>
									<td width=""><label>Gender</label></td>
									<td width="160"><select><option value="Male">Male</option>
											<option value="Female">Female</option></select></td>
								</tr>
								<tr>
									<td width=""><label>Birth Date</label></td>
									<td width="160"><input type="text" class="datepicker"></td>
								</tr>
								<tr>
									<td width=""><label>Hire Date</label></td>
									<td width="160"><input type="text" class="datepicker"></td>
								</tr>
							</table>
							<table id="EmployeeGeneralLeavesGrid"></table>
							<div id="EmployeeGeneralLeavesGridpager"></div>
						</form>
					</div>
					<div id="tabsindex4">
						<form action="">
							<table>
								<tr>
									<td><label class="formLabel">Location: </label></td>
									<td><input type="text" id="employeeDirectoryLocation"></td>
								</tr>
								<tr>
									<td><label class="formLabel">Position: </label></td>
									<td><input type="text" id="employeeDirectoryPosition"></td>
								</tr>
								<tr>
									<td><label class="formLabel">Photo File: </label></td>
									<td><input type="text" id="employeeDirectoryPhotoFile"></td>
								</tr>
								<tr>
									<td><label class="formLabel">Phone: </label></td>
									<td><input type="text" id="employeeDirectoryPhone"></td>
								</tr>
								<tr>
									<td><label class="formLabel">Email: </label></td>
									<td><input type="text" id="employeeDirectoryEmail"></td>
								</tr>
								<tr>
									<td><label class="formLabel">Comment: </label></td>
									<td><input type="text" id="employeeDirectoryComment"></td>
								</tr>
							</table>
						</form>
					</div>
					<div id="tabsindex5">
						<form id="commissionFormID">
							<table>
								<tr>
									<td>
										<fieldset style="width: 300px;padding-bottom: 100px;" class= " ui-widget-content ui-corner-all">
											<legend><label><b>Commission</b></label></legend>
											<table>
												<tr>
													<td><label class="formLabel">Gets Commission?
													</label></td>
													<td><input type="checkbox"
														id="employeeDirectoryCommission"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Job Profit </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" class="validate[custom[number]]"id="commJobProfitID" name="commJobProfitName" value="${requestScope.enMasterDetails.commissionJobProfit}"><label> %</label></td>
												</tr>
												<tr>
													<td><label class="formLabel">Buy/Resell </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" class="validate[custom[number]]" id="commBuySellProfitID" name="commBuySellProfitName" value="${requestScope.enMasterDetails.commissionBuySellProfit}"><label> %</label></td>
												</tr>
												<tr>
													<td><label class="formLabel">Stock Order </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" class="validate[custom[number]]" id="commStockOrderID"  name="commStockOrderName"><label> %</label></td>
												</tr>
												<tr>
													<td><label class="formLabel">Factory Commission </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" class="validate[custom[number]]" id="factoryCommissionID" name="factoryCommissionName" value="${requestScope.enMasterDetails.commissionFactory}"><label> %</label></td>
												</tr>
												<tr>
													<td><label class="formLabel">Quota </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" class="validate[custom[number]]" id="quotaID" name="quotaNAme" value="${requestScope.enMasterDetails.quota}"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Total Profit Bonus Only? </label></td>
													<td><input type="checkbox" id="totalProfitBounsOnly" name="totalProfitBounsOnlyName"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Engineering Comm.</label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="engCommission" class="validate[custom[number]]" name="engCommissionName" value="${requestScope.enMasterDetails.enginComm}"><label> %</label></td>
												</tr>
											</table>
										</fieldset>
										<fieldset style="width: 300px" class= " ui-widget-content ui-corner-all">
											<legend><label><b>Book Jobs</b></label></legend>
											<table>
												<tr>
													<td><label class="formLabel">Use Special Job Number? </label></td>
													<td><input type="checkbox" id="specialJobNumberID" name="specialJobNumberName"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Prefix Job # with</label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="prefixJobNumber" name="prefixJobNumberNAme" value="${requestScope.enMasterDetails.jobNumberPrefix}"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Sequence follows
													</label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="sequenceFollowsID" name="sequenceFollowsName" class="validate[custom[number]]" value="${requestScope.enMasterDetails.jobNumberSequence}"></td>
												</tr>
											</table>
										</fieldset>
									</td>
									<td>
										<fieldset style="width: 300px" class= " ui-widget-content ui-corner-all">
											<legend><label><b>Commission Statement</b></label></legend>
											<table>
												<tr>
													<td></td>
												</tr>
												<tr>
													<td><label class="formLabel"><u><b>Deductions</b></u></label></td>
													<td></td>
												</tr>
												<tr>
													<td><label class="formLabel">Salary </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="salaryDeductions" value="${requestScope.enMasterDetails.repDeduct1}" name="salaryDeductionsName" class="validate[custom[number]]"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Insurance </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="insurenceDeductions" name="insurenceDeductionsName" value="${requestScope.enMasterDetails.repDeduct2}" class="validate[custom[number]]"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Other </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="otherDeductions" name="otherDeductionsName" value="${requestScope.enMasterDetails.repDeduct3}" class="validate[custom[number]]"></td>
												</tr>
												<tr>
													<td><label class="formLabel">Payment Period Default: </label></td>
													<td><input type="text" style="width: 100px;text-align: right;" id="paymentPeriod" name="paymentPeriodName" value="${requestScope.enMasterDetails.paymentPeriod}" class="validate[custom[number]]"></td>
												</tr>
											</table>
										</fieldset>
										<fieldset style="width: 300px" class= " ui-widget-content ui-corner-all">
											<legend><label><b>Bonus Commission</b></label></legend>
											<table>
												<tr>
													<td><label class="formLabel">Get Bonus Commission? </label></td>
													<td><input type="checkbox" id="getBounsCommissions" name="getBounsCommissionsName"></td>
												</tr>
												<tr><td><label><u>YTD Profit</u></label></td><td><label><u>Rate</u></label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD0" name="YTD0Name" value="${requestScope.enMasterDetails.bonusComm1}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate0" name="rate0Name" value="${requestScope.enMasterDetails.bonusLevel1}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD1" name="YTD1Name" value="${requestScope.enMasterDetails.bonusComm2}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate1" name="rate1Name" value="${requestScope.enMasterDetails.bonusLevel2}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD2" name="YTD2Name" value="${requestScope.enMasterDetails.bonusComm3}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate2" name="rate2Name" value="${requestScope.enMasterDetails.bonusLevel3}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD3" name="YTD3Name" value="${requestScope.enMasterDetails.bonusComm4}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate3" name="rate3Name" value="${requestScope.enMasterDetails.bonusLevel4}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD4" name="YTD4Name" value="${requestScope.enMasterDetails.bonusComm5}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate4" name="rate4Name" value="${requestScope.enMasterDetails.bonusLevel5}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD5" name="YTD5Name" value="${requestScope.enMasterDetails.bonusComm6}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate5" name="rate5Name" value="${requestScope.enMasterDetails.bonusLevel6}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD6" name="YTD6Name" value="${requestScope.enMasterDetails.bonusComm7}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate6" name="rate6Name" value="${requestScope.enMasterDetails.bonusLevel7}" class="validate[custom[number]]"><label> %</label></td></tr>
												<tr><td><input type="text" style="width: 100px;text-align: right;" id="YTD7" name="YTD7Name" value="${requestScope.enMasterDetails.bonusComm8}" class="validate[custom[number]]"><label> %</label></td>
												<td><input type="text" style="width: 100px;text-align: right;" id="rate7" name="rate7Name" value="${requestScope.enMasterDetails.bonusLevel8}" class="validate[custom[number]]"><label> %</label></td></tr>
											</table>
										</fieldset>
									</td>
								</tr>
							</table>
							<table align="right">
								<tr  align="right" style="padding-left: 630px;">
										<td>
											<input type="button" class="cancelhoverbutton turbo-tan"  value="Save" onclick="submitCommisions()" style="width:80px;">
										</td>
									</tr>
							</table>
						</form>
					</div>
					<div id="tabsindex6">
						<form action="">
							<table>
								<tr>
									<td><p>
											<b><label>Withholding Info:</label></b>
										</p></td>
								</tr>
								<tr>
									<td width="" style="width: 150px;"><label>State</label></td>
									<td width="140"><select><option value=""></option>
											<option value=""></option></select></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td width=""><label>Pay Frequency</label></td>
									<td width="140"><select><option value=""></option>
											<option value=""></option></select></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td width=""><label>Dept.</label></td>
									<td width="140"><select><option value=""></option>
											<option value=""></option></select></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td width=""><label>Retirement Plan</label></td>
									<td width="140"><input type="checkbox" name="First_Name"
										size="32" value="" /></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td width=""><label>Statutory Employee</label></td>
									<td width="140"><input type="checkbox" name="First_Name"
										size="32" value="" /></td>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td colspan="4"><hr style="height: 1px"></td>
								</tr>
								<tr>
									<td width=""></td>
									<td width=""><b><label>Federal</label></b></td>
									<td width=""><b><label>State</label></b></td>
									<td><b><label>Local</label></b></td>
								</tr>
								<tr>
									<td width=""><label>Marital Status</label></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
								</tr>
								<tr>
									<td width=""><label>Excemptions</label></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
								</tr>
								<tr>
									<td width=""><label>Sup. WH</label></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
								</tr>
								<tr>
									<td width=""><label>Special</label></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td width="140"><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
									<td><input type="text" name="First_Name"
										style="width: 80px" value="" /></td>
								</tr>
								<tr>
									<td colspan="2">
										<div id="Employeepayroll" style="padding-left: 0px;">
											<table style="padding-left: 0px" id="EmployeepayrollGrid"
												class="scroll"></table>
											<div id="EmployeepayrollGridpager" class="scroll"
													style="text-align: right;"></div>
										</div>
									</td>
									<td colspan="2">
										<div id="EmployeepayrollDeduct" style="padding-left: 0px;">
											<table style="padding-left: 0px;"
												id="EmployeepayrollDeductGrid" class="scroll"></table>
											<div id="EmployeepayrollDeductGridpager" class="scroll"
												style="text-align: right;"></div>
										</div>
									</td>
								</tr>
							</table>
						</form>
					</div>
				</div>
			</td>
		</tr>
	</table>
</div>

<script type="text/javascript">
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
	$("#custName").val('');
	$("#custName").val(name);
	$("#tabs_sub").tabs();
	loadLeaveGrid();
	loadpayrollGrid();
	loadpayrollDeductionGrid();
	$(".datepicker").datepicker();
	var aGetCommisions = "${requestScope.enMasterDetails.getsCommission}";
	var aGetBouns = "${requestScope.enMasterDetails.getsBonus}";
	var aJobNumberGenerate = "${requestScope.enMasterDetails.jobNumberGenerate}";
	var aTotalBouns = "${requestScope.enMasterDetails.profitBonus}";
	if(aGetBouns === '1'){
		$("#getBounsCommissions").prop("checked", true);
	}if(aJobNumberGenerate === 'true'){
		$("#specialJobNumber").prop("checked", true);
	}if(aGetCommisions === 'true'){
		$("#employeeDirectoryCommission").prop("checked", true);
	}if(aTotalBouns === '1'){
		$("#totalProfitBounsOnly").prop("checked", true);
	}
	if(!$('#commissionFormID').validationEngine('validate')) {
		return false;
	}
});
	function loadLeaveGrid() {
		$("#EmployeeGeneralLeavesGrid").jqGrid({
				url : '',
				datatype : 'JSON',
				mtype : 'GET',
				pager : jQuery(''),
				colNames : [ 'Type', 'Accrues', 'Starting', 'Hrs/Year',	'Maximum', 'Available', 'Carry Fwd.' ],
				colModel : 
					[
					{name : 'type',	index : 'type',	align : 'right',	width : 40,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{	name : 'Accrues',	index : 'Accrues',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{	name : 'Starting',	index : 'Starting',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{name : 'Hrs/Year',	index : 'Hrs/Year',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{	name : 'Maximum',	index : 'Maximum',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{	name : 'Available',	index : 'Available',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
					{name : 'CarryFwd.',index : 'CarryFwd.',	align : 'center',	width : 80,	hidden : false,	editable : true,	editoptions : {		size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true	}} ],
				rowNum : 1000,
				pgbuttons : false,
				recordtext : '',
				rowList : [],
				pgtext : null,
				viewrecords : false,
				altRows: true,
				altclass:'myAltRowClass',
				sortname : 'date',
				sortorder : "asc",
				imgpath : 'themes/basic/images',
				caption : 'Vacation&Sick Days',
				height : 250,
				width : 1050,
				loadComplete : function(data) {	},
				loadError : function(jqXHR, textStatus, errorThrown) {	},
			});
		}

	function loadpayrollGrid() {
		$("#EmployeepayrollGrid").jqGrid({
			url : '',
			datatype : 'JSON',
			mtype : 'GET',
			pager : jQuery(''),
			colNames : [ 'Pay Type', 'Amt.' ],
			colModel : [ 
						{	name : 'paytype',	index : 'paytype',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
						{	name : 'amount',	index : 'amount',	align : 'center',	width : 50,	hidden : false,	editable : true,	editoptions : {		size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true	}} ],
			rowNum : 1000,
			pgbuttons : false,
			recordtext : '',
			rowList : [],
			pgtext : null,
			viewrecords : false,
			altRows: true,
			altclass:'myAltRowClass',
			sortname : 'date',
			sortorder : "asc",
			imgpath : 'themes/basic/images',
			caption : 'Payroll',
			height : 150,
			width : 525,
			loadComplete : function(data) {	},
			loadError : function(jqXHR, textStatus, errorThrown) {	},
		});

	}

	function loadpayrollDeductionGrid() {
		$("#EmployeepayrollDeductGrid").jqGrid({
			url : '',
			datatype : 'JSON',
			mtype : 'GET',
			pager : jQuery(''),
			colNames : [ 'Deduction', 'Amt.', 'Yearly Max.' ],
			colModel : [ 
						{	name : 'deduction',	index : 'deduction',	align : 'right',	width : 80,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}}, 
						{	name : 'amount',	index : 'amount',	align : 'center',	width : 50,	hidden : false,	editable : true,	editoptions : {		size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true	}}, 
						{	name : 'yearlyMax',	index : 'yearlyMax',	align : 'center',	width : 90,	hidden : false,	editable : true,	editoptions : {		size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true	}} ],
			rowNum : 1000,
			pgbuttons : false,
			recordtext : '',
			rowList : [],
			pgtext : null,
			viewrecords : false,
			altRows: true,
			altclass:'myAltRowClass',
			sortname : 'date',
			sortorder : "asc",
			imgpath : 'themes/basic/images',
			caption : 'Deductions',
			height : 150,
			width : 525,
			loadComplete : function(data) {	},
			loadError : function(jqXHR, textStatus, errorThrown) {	},
		});
	}

	function submitCommisions(){
		if(!$('#commissionFormID').validationEngine('validate')) {
			return false;
		}
		var aCommissionID = "${requestScope.enMasterDetails.emMasterId}";
		var aCommisionDetails = $('#commissionFormID').serialize()+"&commissionID="+aCommissionID;
		$.ajax({
			url: "./employeeCrud/updateCommissions",
			type: "POST",
			data : aCommisionDetails,
			success: function(data) {
				
			}
	   });
		return true; 
	}
</script>

