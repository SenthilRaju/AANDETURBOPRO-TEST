<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro-Vendor Invoices</title>
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
		</style> 
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td align="right">
						<div>
							<input  type="button"  class="add" value="   Add" alt="Add new Job" onclick="addNewInvoice()"/>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<table id="invoicesGrid"></table>
						<div id="invoicesGridPager"></div>
					</td>
				</tr>
			</table>
			<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				$("#search").css("display", "none");
				loadInvoicesList();
			});

			function loadInvoicesList(){
				$("#invoicesGrid").jqGrid({
					url:'./veInvoiceBillController/getVeBillList',
					datatype: 'JSON',
					mtype: 'POST',
					pager: jQuery('#invoicesGridPager'),
					colNames:['Bill Date','PO Number','Invoice #', 'Payble To', 'Amount', 'Paid', 'veBillId', 'rxCustomerId'],
					colModel :[
			           	{name:'billDate', index:'billDate', align:'center', width:30, editable:true,hidden:false,formatter:dateformatter},
						{name:'ponumber', index:'ponumber', align:'left', width:40,hidden:false},
						{name:'veInvoiceNumber', index:'veInvoiceNumber', align:'', width:40,hidden:false, editable:true},
						{name:'payableTo', index:'payableTo', align:'', width:100,hidden:false, editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal; padding-left:4px;"';}},
						{name:'billAmount', index:'billAmount', align:'left', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
						{name:'appliedAmount', index:'appliedAmount', align:'left', width:30,hidden:false, editable:true, formatter:customCurrencyFormatter},
						{name:'veBillId', index:'veBillId', align:'center', width:80, hidden:true, editable:true},
						{name:'rxCustomerId', index:'rxCustomerId', align:'center', width:40, hidden:true, editable:true}
					],
					rowNum: 50,
					pgbuttons: true,	
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],
					viewrecords: true,
					pager: '#invoicesGridPager',
					sortname: 'billDate', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Jobs',
					//autowidth: true,
					height:547,	width: 1140,/*scrollOffset:0,*/ rownumbers:true, altRows: true, altclass:'myAltRowClass', rownumWidth: 45,
					loadComplete: function(data) {
						
				    },
					loadError : function (jqXHR, textStatus, errorThrown) {
					    
					},
					ondblClickRow: function(rowId) {
						var rowData = jQuery(this).getRowData(rowId); 
						var jobNumber = rowData['jobNumber'];
						var jobName = "" + rowData['description'];
						var jobCustomer = rowData['customerName'];
						var jobStatus = rowData['jobStatus'];
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
			    	}
				}).navGrid('#invoicesGridPager',//{cloneToTop:true},
					{add:false,edit:false,del:false,refresh:true,search:false}
				);
			}
			function dateformatter(cellValue, options, rowObject){
				if(cellValue === null){
					return "";
				}
				var date = new Date(cellValue);
				var finaldate = ("0" + (date.getMonth() + 1)).slice(-2)  + "/" +("0" + date.getDate()).slice(-2) + "/" + date.getFullYear();
				return finaldate;
			}
			
			/** Currency formatter **/
			function customCurrencyFormatter(cellValue, options, rowObject) {
				return formatCurrency(cellValue);
			}
		</script>
	</body>
</html>