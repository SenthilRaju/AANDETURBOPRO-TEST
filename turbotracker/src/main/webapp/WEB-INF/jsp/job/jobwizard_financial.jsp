<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<table>
		<tr><td><jsp:include page="jobwizardheader.jsp"></jsp:include></td></tr>
		<tr><td colspan="2"><hr style="width:1100px; color:#C2A472;"></td></tr>
	</table>
	<table>
 		<tr>
 		<td style="vertical-align: top;">
 			<table>
 				<tr><td>
 				<fieldset class= "custom_fieldset" style="width:250px">
 				<legend class="custom_legend"><label><b>Profit</b></label></legend>
 					<table>
 						<tr><td></td><td style="width: 75px;"><label>Estimated</label>&nbsp;</td><td style="width:10px"></td><td><label>Actual To Date</label></td></tr>
						<tr><td><label>Cost:</label></td><td><label id="estimatedCostlabel"></label></td><td style="width:10px"><td id="actualCost"></td></tr>
						<tr><td><label>Profit:</label></td><td><label id="estimattedProfit"></label></td><td style="width:10px"><td ><label id="actualProfit"></label></td></tr>
						<tr><td><label>Ratio:</label></td><td><label id = "estimatedRatio"></label></td><td style="width:10px"><td><label id="actualRatio"></label></td></tr>
 					</table>
 				</fieldset>
 		<br>
 				<fieldset class= " custom_fieldset" style="width:250px">
 				<legend class="custom_legend"><label><b>Financial Summary</b></label></legend>
 				<table>
 					<tr><td style="width: 130px;"><label>Our Contract Amt:</label></td><td></td><td><label id="contractAmt">$0.00</label></td></tr>
 					<tr><td><label>Change Orders:</label></td><td></td><td><label id="changeOrder"></label></td></tr>
 					<tr><td><label>Revised Contract:</label></td><td></td><td><label id="revisedContract"></label></td></tr>
 					<tr><td></td></tr>
 					<tr><td></td></tr>
 					<tr><td></td></tr>
	 				<tr><td><label>Invoiced To Date:</label></td><td></td><td><label id="invoicedToDate">$0.00</label></td></tr>
 					<tr><td><label>Incl Fri&Tax:</label></td><td></td><td><label id="invoicedWithTax">$0.00</label></td></tr>
 					<tr><td><label>Paid To Date:</label></td><td></td><td><label id="paidToDate">$0.00</label></td></tr>
 					<tr><td></td></tr>
 					<tr><td></td></tr>
 					<tr><td></td></tr>
	 				<tr><td><label>A/R:</label></td><td></td><td><label id="accntRecievable">
	 				 $0.00
	 				<%-- <fmt:setLocale value="en_US"/>
					<fmt:formatNumber value="${requestScope.financeTabFields.AR}" type="currency"/> --%>
	 				</label></td></tr>
 					<tr><td><label>Billing Remainder:</label></td><td></td><td><label id="billingReminder">$0.00
 					<%-- <fmt:setLocale value="en_US"/>			/*to be uncommented once cusotmer Invoice is completed*/
					<fmt:formatNumber value="${requestScope.financeTabFields.billingReminder}" type="currency"/> --%>
					</label></td></tr>
 					<tr><td><label>Estimated Tax:</label></td><td></td><td><label id="estimatedTax">$0.00
 					<%-- <fmt:setLocale value="en_US"/>
					<fmt:formatNumber value="${requestScope.financeTabFields.estimatedTax}" type="currency"/> --%>
					</label></td></tr>
 					<tr><td><label>Closeout Amount:</label></td><td></td><td><label id="closeout">$0.00
 					<%-- <fmt:setLocale value="en_US"/>
					<fmt:formatNumber value="${requestScope.financeTabFields.closeout}" type="currency"/> --%>
					</label></td></tr>
 				</table>
 				</fieldset>
 		<br>
 				<fieldset class= " custom_fieldset">
 					<legend class="custom_legend"><label><b>Customer Overall A/R</b></label></legend>
 					<table>
 						<tr><td style="width:140px"><label>Current due:</label></td><td><label id="amtCur">
							<fmt:setLocale value="en_US"/>
							<fmt:formatNumber value="${requestScope.financeTabFields.amtCur}" type="currency"/>
						</label></td></tr>
 						<tr><td><label>30 Days:</label></td><td><label id="days30">
	 						<fmt:setLocale value="en_US"/>
							<fmt:formatNumber value="${requestScope.financeTabFields.trtyDays}" type="currency"/>
						</label></td></tr>
 						<tr><td><label>60 Days:</label></td><td><label id="days60">
							<fmt:setLocale value="en_US"/>
							<fmt:formatNumber value="${requestScope.financeTabFields.sixtyDays}" type="currency"/></label></td></tr>
 	    				<tr><td><label>90 Days:</label></td><td><label id="days90">
							<fmt:setLocale value="en_US"/>
							<fmt:formatNumber value="${requestScope.financeTabFields.nintyDays}" type="currency"/>
						</label></td></tr>
 	 				</table>
 	    		</fieldset>
 			</td></tr>
 		</table>
 	</td>	
 	<td style="vertical-align: top;">
 	   <table align="center">
 	    		
 	    		<tr><td><a onclick="printFinancialReport()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Financial Report</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    		<td><a onclick="invoicefinancial()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Invoice</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    			<td><a onclick="outstandingstmtReport()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Oustanding Invoices Statement</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    			<td><a onclick="invoicesummaryStatement()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Invoice Summary Statement</b></a></td></tr>
 	   </table>
 	   <table>
 	   		<tr><td> <table id="financial"></table>
 	    	<div id="financialpager"></div></td></tr>
 	   </table>
 	  </td></tr>
<!--  Enable for ID#323	-->
  <tr>
 	  	<td colspan="2">
 	  	<table width="25%" align="right">
 	  	<tr><td>
 	  	<div>
 	  		<input type="checkbox" class="includeTaxCls" value="includeTax" onchange="includeTaxAmount()" style="vertical-align: middle;" id="includeTaxID">Include Tax
 	  	</div>
 	  	</td>
 	  	</tr>
 	  	</table>
 	  	</td>
 	  </tr> 
	</table>
 <br>
     <div style="height: 10px;"></div>
    <script type="text/javascript">
		jQuery(document).ready(function(){


			//$("#estimatedCostlabel").text(formatCurrency("${requestScope.joMasterDetails.estimatedCost}"));
			//alert($("#estimatedCostlabel").text());
			var requestScope = "${requestScope.financeTabFields}";
			var jobStatus = getUrlVars()["jobStatus"];
			$('.customerNameField').val($('#cusotmerName').val());	// Customer Name disappear due to ajax cal for fields, FIX IT LATER
			if(jobStatus === "Booked"){
				$(".customerNameField").attr("disabled", true);
			}
			profitGetValues();
			$("#estimatedCostlabel").text(formatCurrency("${requestScope.financeTabFields.estimatedCost}"));
			$("#actualCost").text(formatCurrency("${requestScope.financeTabFields.actualCost}"));
			$("#estimattedProfit").text(formatCurrency("${requestScope.financeTabFields.estimatedProfit}"));
			$("#actualProfit").text(formatCurrency("${requestScope.financeTabFields.actualProfit}"));
			$("#contractAmt").text(formatCurrency("${requestScope.financeTabFields.contractAmount}"));
			$("#changeOrder").text(formatCurrency("${requestScope.financeTabFields.changeOrder}"));
			$("#revisedContract").text(formatCurrency("${requestScope.financeTabFields.revisedContract}"));
			$('#closeout').text(formatCurrency("${requestScope.financeTabFields.closeOut}"));
			$('#billingReminder').text(formatCurrency("${requestScope.financeTabFields.billingReminder}"));
			$('#estimatedTax').text(formatCurrency("${requestScope.financeTabFields.estimatedTax}"));
			
			/*uncomment the following code to get the correct values, Once customer invoice is completed */
			// $("#invoicedToDate").text(formatCurrency("${requestScope.financeTabFields.invoiced}"));
			// $("#invoicedWithTax").text(formatCurrency("${requestScope.financeTabFields.invoicedWithTax}"));
			// $("#paidToDate").text(formatCurrency("${requestScope.financeTabFields.paidToDate}"));
			$("#OriJobNumber").css("display", "none");
			var aQuoteNumber = "${requestScope.financeTabFields.quoteNo}";
			var estimatedRatio = "${requestScope.financeTabFields.estimatedProfit}";
			var ContractAmt ="${requestScope.financeTabFields.contractAmount}";
			var actualProfit = "${requestScope.financeTabFields.actualProfit}";
			if("${requestScope.financeTabFields.revisedContract}" != 0){
				if(ContractAmt != 0.00 && estimatedRatio !=null && estimatedRatio != 0){
					estimatedRatio = estimatedRatio/ContractAmt;
					$('#estimatedRatio').text(((estimatedRatio.toFixed(2))*100).toFixed(2));
					$('#estimatedRatio').append("<span> 	%</span>");
				} 
				if(actualProfit != null && ContractAmt != 0 && ContractAmt != null ) {
					ContractAmt = "${requestScope.financeTabFields.revisedContract}";
		 			actualProfit = actualProfit / ContractAmt;
					$('#actualRatio').text(((actualProfit.toFixed(2))*100).toFixed(2));
					$('#actualRatio').append("<span> %</span>");
				}
			}
					
			if(aQuoteNumber !== ''){
				$("#OriJobNumber").show();
			}
			var joMasterID =	$("#joMaster_ID").text();
			$("#financial").jqGrid({
				url:'./jobtabs6/financial',
				datatype: 'JSON',
				mtype: 'GET',
				//pager: jQuery('#financialpager'),
				postData:{'joMasterID':joMasterID },
				colNames: ['Inv No','Invoice date','Date Paid','Invoice Total ($)','Invoice Total ($)','Current Due','Current Due','30 Days','30 Days','60 Days','60 Days','90 Days','90 Days','AppliedAmount','Freigth',"PaidAmount"],
				colModel :[
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'center', width:80, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'invoiceDate', index:'invoiceDate', align:'center', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'datePaid', index:'datePaid', align:'center', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'invoiceTotal', index:'invoiceTotal', align:'right', width:95, formatter:customCurrencyFormatter, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},	
		          	// in abouve column index and name is "invoiceTotal" to be used once customer invoice is enabled in TP
		           	{name:'subTotal', index:'subTotal', align:'right', width:95,formatter:customCurrencyFormatter,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'currentDue1', index:'currentDue1', align:'right', width:90, formatter:DayscustomCurrencyFormatter,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'currentDue1withtax', index:'currentDue1withtax', align:'right', width:90, formatter:DayscustomCurrencyFormatter,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'thirtyDays', index:'thirtyDays', align:'right', width:50, formatter:DayscustomCurrencyFormatter,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'thirtyDayswithtax', index:'thirtyDayswithtax', align:'right', width:50, formatter:DayscustomCurrencyFormatter,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'sixtyDays', index:'sixtyDays', align:'right', width:50,formatter:DayscustomCurrencyFormatter, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'sixtyDayswithtax', index:'sixtyDayswithtax', align:'right', width:50,formatter:DayscustomCurrencyFormatter, editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'ninetyDays', index:'ninetyDays', align:'right', width:50,formatter:DayscustomCurrencyFormatter,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'ninetyDayswithtax', index:'ninetyDayswithtax', align:'right', width:50,formatter:DayscustomCurrencyFormatter,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'appliedAmount', index:'appliedAmount', align:'right', width:50,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		        	{name:'freight', index:'freight', align:'right', width:50,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		        	{name:'paidAmt', index:'paidAmt', align:'right', width:50,editable:true,hidden:true, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}}
		           	],
		           	
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Aged Invoices for this job',
				height:540,	width: 820,
				loadComplete: function(data) {
					var invoicedToDate = 0;
					var inclTaxnFre = 0;
					var appliedAmount = 0;
					var frieghtTotal = 0;
					var paid_to_Date=0;
					var rows = jQuery("#financial").getDataIDs();
					for (a = 0; a < rows.length; a++) {
						row = jQuery("#financial").getRowData(rows[a]);
						appliedAmount  = appliedAmount +parseFloat(row['appliedAmount'].replace(/[^0-9\.-]+/g,"") );

						
						
						inclTaxnFre =inclTaxnFre +parseFloat(row['invoiceTotal'].replace(/[^0-9\.-]+/g,"")) ;
						invoicedToDate =invoicedToDate +parseFloat(row['subTotal'].replace(/[^0-9\.-]+/g,""));
						frieghtTotal = frieghtTotal+parseFloat(row['freight'].replace(/[^0-9\.-]+/g,""));
						var date=row['datePaid'];
						if(date!=null && date!=''){
							paid_to_Date =paid_to_Date+parseFloat(row['paidAmt'].replace(/[^0-9\.-]+/g,""));
						}
						
						}

				
					
					//appliedAmount = inclTaxnFre - appliedAmount;  
					//alert(inclTaxnFre);
					/*
					Calculating Invoiced= total (Invoiced amount without tax and freight) 
					*/
					if(!isNaN(invoicedToDate)){
						$('#invoicedToDate').text(formatCurrency(invoicedToDate-frieghtTotal));	
					}
					/*
					Calculating Invoiced= total (Invoiced amount with tax and freight) 
					*/
					if(!isNaN(inclTaxnFre)){
						$('#invoicedWithTax').text(formatCurrency(inclTaxnFre ));
					}
					/*
					Calculating invoicedToDate= total (paid invoice up to date) 
					*/
					if(!isNaN(invoicedToDate)){
						$('#paidToDate').text(formatCurrency(paid_to_Date ));	
					}
					/*
					Calculating AR(Account receivable) = total (Invoice applied amount) 
					*/
					if(!isNaN(appliedAmount)){
						$('#accntRecievable').text(formatCurrency(appliedAmount ));	
					}
					/*
					Calculating billing reminder value =Revised contract amount -(InvoicedAmount without freight and tax+ Freight total) 
					*/
						var revised = $('#revisedContract').text().replace(/[^0-9\.-]+/g,"");
					    var invtodate=$('#invoicedToDate').text().replace(/[^0-9\.-]+/g,"");
						var billingReminder = parseFloat(revised)  - parseFloat(invtodate);
						$('#billingReminder').text(formatCurrency(billingReminder));
						/*
						Calculating estimated tax value  = Billing reminder * tax territory%
						*/
						var taxValue = parseFloat($('#TaxValue').text().replace(/[^0-9\.-]+/g,"")/100);
						var estTax =parseFloat(taxValue)*parseFloat(billingReminder);
						$('#estimatedTax').text(formatCurrency(estTax));
						
						/*
						
						 */
						var closeOutAmount = (parseFloat(estTax)+parseFloat(billingReminder)+parseFloat(appliedAmount));
						$('#closeout').text(formatCurrency(closeOutAmount));
						getFinancialAmountDetails();
						
			    },
				loadError : function (jqXHR, textStatus, errorThrown){
				},
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
					if(id && id!==lastsel){
						jQuery('#quotesBidlist').jqGrid('restoreRow',lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow',id,true);
						lastsel=id;
					}
				}
				//editurl:'/employeelistcontroller?type=manipulate'
			}).navGrid('#financialpager', {add:false, edit:false,del:false,refresh:false,search:false}, //options
				//-----------------------edit options----------------------//
				{},
				//-----------------------add options----------------------//
				{},
				//-----------------------Delete options----------------------//
				{},
				//-----------------------search options----------------------//
				{});
		});


		function includeTaxAmount(){
			$('#financial').setGridWidth(720, true);
			if (document.getElementById('includeTaxID').checked){
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[3].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[4].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[5].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[6].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[7].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[8].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[9].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[10].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[11].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[12].name);
				

				 $("#financial").jqGrid('setColProp','invoiceNumber',{width:50});
			     $("#financial").jqGrid('setColProp','invoiceDate',{width:50});
			     $("#financial").jqGrid('setColProp','datePaid',{width:50});
				 $("#financial").jqGrid('setColProp','invoiceTotal',{width:50});
			     $("#financial").jqGrid('setColProp','subTotal',{width:50});
			     $("#financial").jqGrid('setColProp','currentDue1',{width:50});
			     $("#financial").jqGrid('setColProp','thirtyDays',{width:50});
			     $("#financial").jqGrid('setColProp','sixtyDays',{width:50});
			     $("#financial").jqGrid('setColProp','ninetyDays',{width:50});
			     $("#financial").jqGrid('setColProp','currentDue1withtax',{width:50});
			     $("#financial").jqGrid('setColProp','thirtyDayswithtax',{width:50});
			     $("#financial").jqGrid('setColProp','sixtyDayswithtax',{width:50});
			     $("#financial").jqGrid('setColProp','ninetyDayswithtax',{width:50});
				     var gw = $("#financial").jqGrid('getGridParam','width');
				     $("#financial").jqGrid('setGridWidth',820);
				
			}else{
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[3].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[4].name);
		
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[5].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[6].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[7].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[8].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[9].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[10].name);
				jQuery("#financial").jqGrid('showCol', jQuery("#financial").getGridParam("colModel")[11].name);
				jQuery("#financial").jqGrid('hideCol', jQuery("#financial").getGridParam("colModel")[12].name);

				 $("#financial").jqGrid('setColProp','invoiceNumber',{width:50});
			     $("#financial").jqGrid('setColProp','invoiceDate',{width:50});
			     $("#financial").jqGrid('setColProp','datePaid',{width:50});
				 $("#financial").jqGrid('setColProp','invoiceTotal',{width:50});
			     $("#financial").jqGrid('setColProp','subTotal',{width:50});
			     $("#financial").jqGrid('setColProp','currentDue1',{width:50});
			     $("#financial").jqGrid('setColProp','thirtyDays',{width:50});
			     $("#financial").jqGrid('setColProp','sixtyDays',{width:50});
			     $("#financial").jqGrid('setColProp','ninetyDays',{width:50});
			     $("#financial").jqGrid('setColProp','currentDue1withtax',{width:50});
			     $("#financial").jqGrid('setColProp','thirtyDayswithtax',{width:50});
			     $("#financial").jqGrid('setColProp','sixtyDayswithtax',{width:50});
			     $("#financial").jqGrid('setColProp','ninetyDayswithtax',{width:50});
			     var gw = $("#financial").jqGrid('getGridParam','width');
			     $("#financial").jqGrid('setGridWidth',820);
			}
		}
		function DayscustomCurrencyFormatter(cellValue, options, rowObject) {
			if(cellValue==null||cellValue==""||cellValue==undefined ||cellValue=='0'){
				return "";
			}else{
				return formatCurrency(cellValue);
			}
			
		}
		function customCurrencyFormatter(cellValue, options, rowObject) {
			return formatCurrency(cellValue);
		}
		function financial_previous(){
			var $tabs = $('#tabs_main_job').tabs();
		    $tabs.tabs('select', 4); // switch to third tab
		}
		function financial_next(){
			var $tabs = $('#tabs_main_job').tabs();
		    $tabs.tabs('select', 6); // switch to third tab
		}
		function profitGetValues() {
		}
		function invoicefinancial() {
			createtpusage('job-Financials Tab','Invoice','Info','job-Financial Tab,Invoice Report,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
			jQuery("#cusinvoicetab").dialog("open");
			$("#lineshipTo1").hide();
		}
		function printFinancialReport(){
			var grid = $("#financial");
			var rowcount = grid.getGridParam("reccount")
			var jobNumber = $("#jobNumber_ID").text();
			window.open("./jobtabs6/printFinancialReport?jobNumber="+jobNumber+"&noOfRows="+rowcount+"&joMasterID="+$("#joMaster_ID").text());
			; createtpusage('job-Financials Tab','View Report','Info','job-Financial Tab,View Report,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
		}

		function outstandingstmtReport(){
			 createtpusage('job-Financials Tab','Oustanding Invoices Statement','Info','job-Financial Tab,Oustanding Invoices Statement,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
			/* var grid = $("#financial");
			var rowcount = grid.getGridParam("reccount") */
			var jobNumber = $("#jobNumber_ID").text();
			window.open("./jobtabs6/outstandingstmtReport?noOfRows=0&jobNumber="+jobNumber+"&joMasterID="+$("#joMaster_ID").text());
		}

		function invoicesummaryStatement(){
			createtpusage('job-Financials Tab','Invoice Summary Statement','Info','job-Financial Tab,Invoice Summary Statement,Job Number:'+ $('input:text[name=jobHeader_JobNumber_name]').val());
			/* var grid = $("#financial");
			var rowcount = grid.getGridParam("reccount") */
			var jobNumber = $("#jobNumber_ID").text();
			window.open("./jobtabs6/outstandingstmtReport?noOfRows=1&jobNumber="+jobNumber+"&joMasterID="+$("#joMaster_ID").text());
		}
		
		/*
		*Created by : Praveenkumar
		Date : 2014-09-05
		Purpose: Need to fetch latest data for Cost details
		*/
		function getFinancialAmountDetails(){
			
			$.ajax({
					url: "./jobtabs6/financialAmountDetails",
					type: "GET",
					data : {"jobNumber" : $("#jobNumber_ID").text(),"joMasterID": $("#joMaster_ID").text()},
					success: function(data) {
					
				$("#estimatedCostlabel").text(formatCurrency(data.financeTabFields.estimatedCost));
				$("#actualCost").text(formatCurrency(data.financeTabFields.actualCost));
				$("#estimattedProfit").text(formatCurrency(data.financeTabFields.estimatedProfit));
				$("#actualProfit").text(formatCurrency(data.financeTabFields.actualProfit));
				$("#contractAmt").text(formatCurrency(data.financeTabFields.contractAmount));
				$("#changeOrder").text(formatCurrency(data.financeTabFields.changeOrder));
				$("#revisedContract").text(formatCurrency(data.financeTabFields.revisedContract));
				$("#amtCur").text(formatCurrency(data.financeTabFields.amtCur));
				
						}
					}); 
		}
		/********************************************************************/
	</script>