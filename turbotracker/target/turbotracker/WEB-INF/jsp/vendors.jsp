<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>TurboPro - VendorDetails</title>
</head>
<body>
<table id="header">
		<tr>
			<td colspan="2">
				<div><jsp:include page="./headermenu.jsp" /></div>
			</td>
		</tr>
	</table>
	<div id="addvendor">
		<div class="tabs_main" style="padding-left: 0px;width:1150px;margin:0 auto;">
			<ul>
				<li><a href="#vendorGeneral">General</a></li>
				<li><a href="#vendorJournal">Journal</a></li>
				<li id="vendor"><a href="#vendortab">Vendor</a></li>
				<li id="employee"><a href="#employeetab">Employee</a></li>
				<li id="customer"><a href="#customertab">Customer</a></li>
				<li id="engineer"><a href="#engineertab">Engineer</a></li>
				<li id="architect"><a href="#architecttab">Architect</a></li>
				<li style="float: right; padding-right: 10px;">
					<label id="vendorName" style="color: white;">${requestScope.name}</label>
					<label id="vendorId" style="display: none;">${requestScope.rolodexNumber}</label>
			</li>
			</ul>
			<div id="vendorGeneral">
				<table>
					<tr>
						<td colspan="3">
							<div id="EmployeeDetails" style="padding-left: 0px;">
								<table style="padding-left: 0px" id="EmployeeDetailsGrid"
									class="scroll"></table>
								<div id="EmployeeDetailsGridpager" class="scroll"
									style="text-align: right;"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td align="right"><input type="button" class="add" id="add"
							value="Add"></td>
					</tr>
					<tr height="10px;"></tr>
				</table>
				<br>
			<!-- 	<table>
					<tr>
						<td align="right"><label>Categories : &nbsp;</label> <input
							type="checkbox" id="vendorcheck" style="vertical-align: middle;"
							checked="checked" disabled="disabled"><label>&nbsp;Vendor</label>
							<input type="checkbox" id="customerchek"
							style="vertical-align: middle;" onclick="customer()"><label>&nbsp;Customer</label>
							<input type="checkbox" id="employeeche"
							style="vertical-align: middle;" onclick="employee()"><label>&nbsp;Employee</label>
							<input type="checkbox" id="engineerche"
							style="vertical-align: middle;" onclick="engineer()"><label>&nbsp;Engineer</label>
							<input type="checkbox" style="vertical-align: middle;"
							id="architectche" onclick="architect()"><label>&nbsp;Architect</label>
							<input type="checkbox" id="caustMGR" name="caustMGR"
							style="vertical-align: middle;" ><label>&nbsp;G/Caust
								MGR</label> <input type="checkbox" id="owner" name="owner"
							style="vertical-align: middle;" ><label>&nbsp;Owner</label>
							<input type="checkbox" id="bondAgent" name="bondAgent"
							style="vertical-align: middle;" ><label>&nbsp;Bond
								Agent</label></td>
					</tr>
				</table> -->
				<table>
					<tr>
						<td colspan="3">
							<div id="EmployeeDetailCategories" style="padding-left: 0px;">
								<table style="padding-left: 0px"
									id="EmployeeDetailCategoriesGrid" class="scroll"></table>
								<div id="EmployeeDetailCategoriesGridpager" class="scroll"
									style="text-align: right;"></div>
							</div>
						</td>
					</tr>
					<tr>
						<td align="left"><input type="checkbox" id="multimail"
							style="vertical-align: middle;"><label>&nbsp;Mailing
								Address</label></td>
						<td align="right"><input type="button" class="add" id="add"
							value="Add"></td>
					</tr>
					<tr height="10px;"></tr>
				</table>
			</div>

			<div id="vendorJournal">
				<table>
					<tr>
						<td width="100" style="padding-bottom: 10px;"><label>Vendor
								name: </label></td>
						<td width="260" style="padding-bottom: 10px;"><input
							type="text" name="EmployeeName" size="32" tabindex="1" value="${requestScope.vendorName}" /></td>
					</tr>
					<tr>
						<td colspan="2"><hr width="800px"></td>
					</tr>
				</table>
				<table id="journal"></table>
				<div id="vendorjournalpager"></div>
			</div>
			<jsp:include page="vendor.jsp"></jsp:include>
			<jsp:include page="rolodexCustomer.jsp"></jsp:include>
			<jsp:include page="architect.jsp"></jsp:include>
			<jsp:include page="engineer.jsp"></jsp:include>
			<jsp:include page="employe.jsp"></jsp:include>
		</div>
	</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			$("#architect").css("display", "none");
			$("#engineer").css("display", "none");
			$("#customer").css("display", "none");
			$("#employee").css("display", "none");
			$(".tabs_main").tabs();
			$("#tabs_sub").tabs();
			$(".datepicker").datepicker();
			vendorgeneral();
			loadJournalsGrid();
			vendorGrid();
			loadDetailsGrid();
			loadDetailCategoriesGrid();
			loadLeaveGrid();
			loadpayrollGrid();
			loadpayrollDeductionGrid();
			var name = "${requestScope.name}";
			var sting = name.replace('`', ' ');
			var sting1 = sting.replace('`', ' ');
			var vendorName = sting1.replace('and', '&');
			var vendorName1 = vendorName.replace('and', '&');
			$('#vendorName').empty();	 
			$('#vendorName').append(vendorName1);
		});
		function vendorgeneral() {
			jQuery("#general").jqGrid({

				datatype : 'JSON',
				mtype : 'GET',
				colNames : [ 'Name', 'Phone(s)' ],
				colModel : 
					[{	name : 'Name',	index : 'Name',	align : 'right',	width : 60,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}},
					 {	name : 'Phone(s)',	index : 'Phone(s)',	align : 'center',	width : 90,	hidden : false,	editable : true,editoptions : {size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true}
				} ],
				recordtext : '',
				rowList : [],
				pgtext : null,
				viewrecords : false,
				sortname : 'Name',
				sortorder : "asc",
				imgpath : 'themes/basic/images',
				caption : 'Contact',
				height : 150,
				width : 800,
				scrollOffset : 0,
				altRows: true,
				altclass:'myAltRowClass',
				loadComplete : function(data) {				},
				loadError : function(jqXHR, textStatus, errorThrown) { },
				onSelectRow : function(id) {
					if (id && id !== lastsel) {
						jQuery('#quotesBidlist').jqGrid('restoreRow', lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow', id, true);
						lastsel = id;
					}
				}
			});
		}
		function loadJournalsGrid() {
			var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
			var grid = $('#journal');
			$("#journal").jqGrid({
				url : 'vendorscontroller/vendorjournal',
				datatype : 'JSON',
				mtype : 'GET',
				postData : {'rolodexNumber' : "${requestScope.rolodexNumber}" },
				colNames : [ 'Date', 'User', 'Entry' ],
				colModel :
					[{name : 'entryDate', index : 'entryDate', align : 'center',	width : 30,	editable : true,hidden : false, formatter:'date', editrules : {edithidden : false,required : false}}, 
					{name : 'name',	index : 'name',	align : 'left',width : 40,hidden : false,editable : true,editoptions : {size : 20,	readonly : false,	alignText : 'right'	},	editrules : {edithidden : true,required : true}}, 
					{name : 'entryMemo',index : 'entryMemo',align : 'left',width : 90,hidden : false,editable : true,	editoptions : {},	cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" '; }, editrules : {edithidden : true,required : false} } ],
				recordtext : '',
				rowList : [],
				pgtext : null,
				viewrecords : false,
				sortname : 'date',
				sortorder : "asc",
				imgpath : 'themes/basic/images',
				caption : 'Journals',
				height : 400,
				width : 930,
				altRows: true,
				altclass:'myAltRowClass',
				loadComplete : function(data) {	},
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
				loadError : function(jqXHR, textStatus, errorThrown) {	},
			});
			emptyMsgDiv.insertAfter(grid.parent());
			return true;
		}

		function vendorGrid() {

			jQuery("#vendorGrid").jqGrid({

				datatype : 'JSON',
				mtype : 'GET',
				//pager: jQuery('#vendorpager1'),
				colNames : [ 'Description', 'Active' ],
				colModel : 
					[{	name : 'Description',	index : 'Description',	align : 'right',	width : 60,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {		size : 30,		readonly : true	},	editrules : {		edithidden : false,		required : false	}},
					 {	name : 'Active',	index : 'Active',	align : 'center',	width : 90,	hidden : false,	editable : true,	editoptions : {		size : 20,		readonly : false,		alignText : 'right'	},	editrules : {		edithidden : true,		required : true	}				
					} ],
				recordtext : '',
				rowList : [],
				pgtext : null,
				viewrecords : true,
				sortname : 'Product No',
				sortorder : "asc",
				imgpath : 'themes/basic/images',
				caption : 'Manufactures',
				height : 100,
				width : 930,
				altRows: true,
				altclass:'myAltRowClass',
				scrollOffset : 0,
				loadComplete : function(data) {	},
				loadError : function(jqXHR, textStatus, errorThrown) {	},
				onSelectRow : function(id) {
					if (id && id !== lastsel) {
						jQuery('#quotesBidlist').jqGrid('restoreRow', lastsel);
						jQuery('#quotesBidlist').jqGrid('editRow', id, true);
						lastsel = id;
					}
				}
			});
		}

		function loadDetailsGrid() {
			var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
			var grid = $('#EmployeeDetailsGrid');
			$("#EmployeeDetailsGrid").jqGrid(
					{
						url : 'vendorscontroller/vendorcontact',
						datatype : 'JSON',
						postData : {'rolodexNumber' : "${requestScope.rolodexNumber}" },
						mtype : 'GET',
						colNames : [ 'Last', 'First', 'Role', 'Direct Line','Email', 'CellPhone', 'Division' ],
						colModel : 
							[{name : 'lastName',index : 'lastName',align : 'left',width : 70, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},	editable : true,hidden : false,editoptions : {size : 30,	readonly : true},	editrules : {edithidden : false,required : false}},
							 {name : 'firstName',index : 'firstName',	align : 'left',width : 60,hidden : false,editable : true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},	editoptions : {size : 20,	readonly : false,	alignText : 'right'	},editrules : {edithidden : true,required : true}},
							 {name : 'jobPosition',index : 'jobPosition',align : 'left',	width : 50,	hidden : false,editable : true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},	editoptions : {},	editrules : {edithidden : true,required : false	}},
							 {name : 'directLine',index : 'directLine',	align : 'left',width : 50,hidden : false,editable : true,editoptions : {},editrules : {edithidden : true,required : false}},
							 {name : 'email',	index : 'email',	align : 'left',width : 90,hidden : false,editable : true,editoptions : {},editrules : {	edithidden : true,	required : false}},
							 {name : 'cell',index : 'cell',align : 'left',width : 50,hidden : false,	editable : true,	editoptions : {},	editrules : {		edithidden : true,		required : false	}},
							 {name : 'pager',index : 'pager',align : 'left',width : 50,hidden : false,editable : true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},	editoptions : {},editrules : {	edithidden : true,	required : false}}, ],
						recordtext : '',
						rowList : [],
						pgtext : null,
						viewrecords : false,
						sortname : ' ',
						sortorder : "asc",
						imgpath : 'themes/basic/images',
						caption : 'Contacts',
						height : 250,
						altRows: true,
						altclass:'myAltRowClass',
						rownumbers:true,
						width : 930,
						loadComplete : function(data) {	},
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
						loadError : function(jqXHR, textStatus, errorThrown) {	}
					});
				emptyMsgDiv.insertAfter(grid.parent());
				return true;
		}

		function loadDetailCategoriesGrid() {
			var emptyMsgDiv = $('<div align="center"><b class="field_label"> </b></div>');
			var grid = $('#EmployeeDetailCategoriesGrid');
			$("#EmployeeDetailCategoriesGrid").jqGrid(
					{
						url : 'vendorscontroller/vendoraddress',
						datatype : 'JSON',
						postData : {'rolodexNumber' : "${requestScope.rolodexNumber}" },
						mtype : 'GET',
						colNames : [ 'Address', 'City', 'State', 'Country',	'ZipCode', 'Phone', 'Fax' ],
						colModel : 
						[{	name : 'address1',	index : 'address1',	align : 'left',	width : 110,	editable : true,	hidden : false,	edittype : 'text',	editoptions : {size : 30,readonly : true},editrules : {edithidden : false,required : false}},
						 {name : 'city',index : 'city',align : 'left',width : 40,hidden : false,editable : true,	editoptions : {size : 20,readonly : false,alignText : 'right'	},editrules : {	edithidden : true,required : true}},
						 {name : 'state',index : 'state',align : 'left',width : 40,hidden : false,editable : true,edittype : 'textarea',editoptions : {},editrules : {	edithidden : true,	required : false}},
						 {name : 'country',index : 'country',align : 'left',width : 30,hidden : false,editable : true,edittype : 'textarea',editoptions : {},editrules : {edithidden : true,required : false}},
						 {name : 'zip',index : 'zip',align : 'left',width : 30,hidden : false,editable : true,edittype : 'textarea',	editoptions : {},	editrules : {edithidden : true,required : false}},
						 {name : 'address2',index : 'address2',	align : 'left',	width : 50,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {		edithidden : true,		required : false	}},
						 {name : 'name',	index : 'name',	align : 'left',	width : 40,	hidden : false,	editable : true,	edittype : 'textarea',	editoptions : {},	editrules : {		edithidden : true,		required : false	}}, ],
						recordtext : '',
						rowList : [],
						pgtext : null,
						viewrecords : false,
						sortname : ' ',
						sortorder : "asc",
						imgpath : 'themes/basic/images',
						caption : 'Adderss',
						height : 200,
						rownumbers:true,
						altRows: true,
						altclass:'myAltRowClass',
						width : 930,
						loadComplete : function(data) {		},
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
						loadError : function(jqXHR, textStatus, errorThrown) {	},
					});
				emptyMsgDiv.insertAfter(grid.parent());
				return true;
		}

		function architect() {
			if ($('#architectche').is(':checked')) {
				$("#architect").show();
			} else {
				$("#architect").hide();
			}
		}

		function engineer() {
			if ($('#engineerche').is(':checked')) {
				$("#engineer").show();
			} else {
				$("#engineer").hide();
			}
		}

		function customer() {
			if ($('#customerchek').is(':checked')) {
				$("#customer").show();
			} else {
				$("#customer").hide();
			}
		}

		function employee() {
			if ($('#employeeche').is(':checked')) {
				$("#employee").show();
			} else {
				$("#employee").hide();
			}
		}

		/*jQuery(function() {
			jQuery("#addvendor").dialog({
				autoOpen : false,
				height : 730,
				width : 900,
				title : "Vendor Details",
				modal : true,
				buttons : {
					"Submit" : function() {

					},
					Cancel : function() {
						jQuery(this).dialog("close");
						return true;
					}
				},
				close : function() {
					return true;
				}
			});*/

		//});
	</script>
</body>
</html>