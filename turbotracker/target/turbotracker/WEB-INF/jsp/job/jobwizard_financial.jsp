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
						<tr><td><label>Cost:</label></td><td><label id="estimatedCost"></label></td><td style="width:10px"><td id="actualCost"></td></tr>
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
 	    		
 	    		<tr><td><a onclick="printFinancialReport()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">View Report</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    		<td><a onclick="invoicefinancial()" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Invoice</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    			<td><a onclick="" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Oustanding Invoices Statement</b></a></td><td>&nbsp;&nbsp;&nbsp;</td>
 	    			<td><a onclick="" style="font: bold 14px MyriadProRegular,trebuchet ms,sans-serif;cursor:pointer;"><b style="text-decoration: underline;">Invoice Summary Statement</b></a></td></tr>
 	   </table>
 	   <table>
 	   		<tr><td> <table id="financial"></table>
 	    	<div id="financialpager"></div></td></tr>
 	   </table>
 	  </td></tr>
	</table>
 <br>
     <div style="height: 10px;"></div>
    <script type="text/javascript">
		jQuery(document).ready(function(){
			var requestScope = "${requestScope.financeTabFields}";
			var jobStatus = getUrlVars()["jobStatus"];
			$('.customerNameField').val($('#cusotmerName').val());	// Customer Name disappear due to ajax cal for fields, FIX IT LATER
			if(jobStatus === "Booked"){
				$(".customerNameField").attr("disabled", true);
			}
			profitGetValues();
			$("#estimatedCost").text(formatCurrency("${requestScope.financeTabFields.estimatedCost}"));
			$("#actualCost").text(formatCurrency("${requestScope.financeTabFields.actualCost}"));
			$("#estimattedProfit").text(formatCurrency("${requestScope.financeTabFields.estimatedProfit}"));
			$("#actualProfit").text(formatCurrency("${requestScope.financeTabFields.actualProfit}"));
			$("#actualCost").text(formatCurrency("${requestScope.financeTabFields.actualCost}"));
			$("#contractAmt").text(formatCurrency("${requestScope.financeTabFields.contractAmount}"));
			$("#changeOrder").text(formatCurrency("${requestScope.financeTabFields.changeOrder}"));
			$("#revisedContract").text(formatCurrency("${requestScope.financeTabFields.revisedContract}"));
			
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
			var jobNumber = $("#jobNumber_ID").text();
			$("#financial").jqGrid({
				url:'./jobtabs6/financial',
				datatype: 'JSON',
				mtype: 'GET',
				//pager: jQuery('#financialpager'),
				postData:{'jobNumber':jobNumber },
				colNames: ['Inv No','Invoice date','Date Paid','Invoice Total ($)','Current Due','30 Days','60 Days','90 Days'],
				colModel :[
		           	{name:'invoiceNumber', index:'invoiceNumber', align:'center', width:80, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'invoiceDate', index:'invoiceDate', align:'center', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'datePaid', index:'datePaid', align:'center', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:0.00, index:0.00, align:'right', width:95, formatter:customCurrencyFormatter, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},	
		          	// in abouve column index and name is "invoiceTotal" to be used once customer invoice is enabled in TP
		           	{name:'currentDue1', index:'currentDue1', align:'center', width:90, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'thirtyDays2', index:'thirtyDays2', align:'center', width:50, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'sixtyDays3', index:'sixtyDays3', align:'center', width:50, editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
		           	{name:'ninetyDays4', index:'ninetyDays4', align:'center', width:50,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}}],
				rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false, altRows: true, altclass:'myAltRowClass',
				sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Aged Invoices for this job',
				height:540,	width: 820,
				loadComplete: function(data) {
					
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
			jQuery("#cusinvoicetab").dialog("open");
			$("#lineshipTo1").hide();
		}
		function printFinancialReport(){
			var grid = $("#financial");
			var rowcount = grid.getGridParam("reccount")
			var jobNumber = $("#jobNumber_ID").text();
			window.open("./jobtabs6/printFinancialReport?jobNumber="+jobNumber+"&noOfRows="+rowcount);
		}
		/********************************************************************/
	</script>