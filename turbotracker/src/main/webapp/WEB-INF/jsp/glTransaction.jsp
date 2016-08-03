<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Customers List</title>
		<style type="text/css">
			input#customerNameHeader {width:400px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
			input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#add { display: none; }
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			#search{display: none;}
			.ui-jqgrid-bdiv{height: 500px !important;}
			
		</style>
	</head>
	<body>
		<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		
		<table style="width:600px;margin:0 auto;">
				
			<tr>
				<td colspan="2">
					<div id="Employees" style="padding-left: 0px;">
					
						<table style="padding-left:0px" id="drillDownGrid1" class="scroll"></table>
						<div id="drillDownPager1" class="scroll" style="text-align:right;"></div>
						
					
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				</td>
			</tr>
		</table>
		<div style="height: 20px;"></div>
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	
	<script>

	$(function(){
		
			var accountsID ="";
			var periodsId ="";
			var periodsToID ="";
			var yearID ="";
			var mostRecentPeriodID ="";
			//alert("AccountID: "+accountsID+" periodsId: "+periodsId+" periodsToID: "+periodsToID+" yearID: "+yearID+" mostRecentPeriodID: "+mostRecentPeriodID);
			
			try{
				$("#Employees").empty();
				$("#Employees").append("<table id='drillDownGrid1'></table><div id='drillDownPager1'></div>");
				$("#drillDownGrid1").jqGrid({
				datatype: "json",
				mtype: "POST",
				url:'./drillDown/accountDtails1',
				pager: jQuery('#drillDownPager1'),
				postData : {
					'accountNumber' : accountsID,
					'periodsId' : periodsId,
					'periodsToID' : periodsToID,
					'yearID' : yearID,
					'mostRecentPeriodID' : mostRecentPeriodID
					},
				
				colNames:['TransactionID','Account ','Account Description',
				        'Transaction Description','Reference','Debit',
				         'Credit'
				          ],
				         
				          colModel:[
				        			{name:'glTransactionId',index:'glTransactionId',hidden:false, align:'left', width:54,editable:true, editrules:{required:false}, editoptions:{size:10}},
				        			{name:'coAccountNumber',index:'coAccountNumber',hidden:false, width:92,editable:true, editrules:{required:false}, editoptions:{size:10}},
				        			{name:'coAccountDesc',index:'coAccountDesc',hidden:false, width:237,editable:true, editrules:{required:false}, editoptions:{size:10}},
				        			{name:'transactionDesc',index:'transactionDesc',hidden:false, width:188,editable:true, editrules:{required:false}, editoptions:{size:10}},
				          			{name:'reference',index:'reference',hidden:false, width:125,editable:true, editrules:{required:false}, editoptions:{size:10}},
				        			{name:'debit',index:'debit',hidden:false, align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter},
				         			{name:'credit',hidden:false,index:'credit', align:'right', width:125,editable:true, editrules:{required:false}, editoptions:{size:10},formatter:customCurrencyFormatter}
				        		],
				rowNum: 500,	
				pgbuttons: true,	
				recordtext: '',
				rowList: [500, 1000],
				viewrecords: true,
				pager: '#drillDownPager1',
				sortorder: "asc",
				altRows: true,
				altclass:'myAltRowClass',
				imgpath: 'themes/basic/images',
				caption: 'Accounts',
				height:200,	width: 1050,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
				loadonce: false,
				loadComplete:function(data) {
				},
			    jsonReader : {
		            root: "rows",
		            page: "page",
		            total: "total",
		            records: "records",
		            repeatitems: false,
		            cell: "cell",
		            id: "ecSplitCodeID",
		            userdata: "userdata"
		    	},
		    	onSelectRow: function(rowId){
		    		 var rowData = jQuery(this).getRowData(rowId); 
		    		 $("#ecSplitCodeHiddenID").val(rowData["ecSplitCodeID"]);
		    		 $("#splittypedescription").val(rowData["codeName"]);
		    		 $("#spltypdefaultper").val(rowData["defaultPct"]);
		    	},
		    	ondblClickRow: function(rowId) {
		    		var rowData = jQuery(this).getRowData(rowId); 
		   		 	var accID = rowData["coAccountID"];
		   		 	getJournalBalance(accID,periodsId,periodsToID,yearID,mostRecentPeriodID);
		   		
		   		$('#drillDown_tabs').tabs({ selected: 1 });
				$('#drillDown_tabs').tabs({ active: 1 });
				}
			})/*.navGrid('#drillDownPager',
				{add:false,edit:false,del:false,refresh:false,search:false});*/
			}
			catch(err){
				console.log(err.message+"message about error");
		}
			//return true;


		});
	
	</script>
	
	<!-- <script type="text/javascript" src="./../resources/scripts/turbo_scripts/customers.js"></script> -->
	<!--  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/customers.min..js"></script> -->
	</body>
</html>