<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Employee List</title>
		<link type="text/css" href="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/themes/custom-theme/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
		<link type="text/css" href="./../resources/styles/turbo-css/turbo.css" rel="stylesheet" />
		<link type="text/css" href="./../resources/web-plugins/Validation_Engine/validationEngine.jquery.css" rel="stylesheet" media="screen">
		<style type="text/css">
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
		</style>
	</head>
	<body>
	 <div  style="background-color: #FAFAFA">
		<div>
			<jsp:include page="./headermenu.jsp"></jsp:include> 
		</div>
		<jsp:include page="employee/addNewEmployee.jsp"></jsp:include>
		<table style="width:600px;margin:0 auto;">
				<tr>
					<td align="right">
						<table>
					    	<tr><td> 
									<input type="button" value="  Add" class="add" id="addEmployeeButton" onclick="openEmployeeDialog()" ><!--  style=" width:70px; color:#FFFFFF;background:#87CEEB url('./../resources/Icons/Add.png') no-repeat 5px 6px ">-->
							</td><td style="padding-left: 20px;"><input type="checkbox" id="activeEmployeesList" style="vertical-align: middle;" onchange="activeEmployeesList()" ><label style="vertical-align: middle;">Active</label></td></tr>
					    </table>
					 </td>
				</tr>
				<tr>
				<td colspan="2">
					<div id="Employees" style="padding-left: 0px;">
						<table style="padding-left:0px" id="EmployeesListGrid" class="scroll"></table>
						<div id="EmployeesListGridpager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				</td>
			</tr>
		</table>
	<!-- 	<div id="searchEmployee">
			<form action="" id="searchForm">
				<table>
					<tr><td><label class="formLabel">Employee Id: </label></td><td><input type="text" id="employeeIdField"></td></tr>
					<tr><td><label class="formLabel">Employee Name: &nbsp;</label></td><td><input type="text" id="employeeNameField"></td></tr>
					<tr><td><label class="formLabel">Employee Role: </label></td><td><input type="text" id="employeeRoleField"></td></tr>
				</table>
			</form>
		</div> -->
		<div style="height: 20px;"></div>
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				//$("input:button").button();
				//$("input:text").addClass("ui-state-default ui-corner-all");
				var aActiveUser = '';
				var aUrPage = "${requestScope.userDetails.activeEmployeeList}";
				var aUserpage = "${requestScope.userDetails.activeUserList}";
				if(aUrPage === '0'){
					$("#activeEmployeesList").prop("checked", true);
					aActiveUser = 0;
					loadChannelsGrid(aActiveUser);
					updateUserDetails(aActiveUser,aUserpage);
				}else{
					aActiveUser = 1;
					loadChannelsGrid(aActiveUser);
					updateUserDetails(aActiveUser,aUserpage);
				}
				var addCustomer = getUrlVars()["oper"];
				if(addCustomer === "add"){
					openEmployeeDialog();
				}else{
					$("#addNewEmployee").css("display", "none");
				}
			});

			function activeEmployeesList(){
				var aUserpage = "${requestScope.userDetails.activeUserList}";
				var aActiveUser = 1;
				if ($('#activeEmployeesList').is(':checked')){
					aActiveUser = 0;
					loadChannelsGrid(aActiveUser);
					$('#EmployeesListGrid').jqGrid('setGridParam',{postData:{'employeeActive' : aActiveUser }});
					$("#EmployeesListGrid").trigger("reloadGrid");
					updateUserDetails(aActiveUser,aUserpage);
				}else if(aActiveUser === 1){
					loadChannelsGrid(aActiveUser);
				    $('#EmployeesListGrid').jqGrid('setGridParam',{postData:{'employeeActive' : aActiveUser }});
					$("#EmployeesListGrid").trigger("reloadGrid");
					updateUserDetails(aActiveUser,aUserpage);
				}
			}
			
			
			function loadChannelsGrid(aActiveUser){
				var aEmployeePage = "${requestScope.userDetails.employeeperpage}";
				$("#EmployeesListGrid").jqGrid({
					url:'./employeeCrud',
					datatype: 'json',
					mtype: 'POST',
					postData: { 'employeeActive' : aActiveUser },
					pager: jQuery('#EmployeesListGridpager'),
				   	colNames:['rxId','Name','First Name','Office Ext.','Phone', 'Phone1', 'Fax', 'Address','Address1','City', 'State','Zip','Active'],
				   	colModel:[
				   		{name:'rxMasterId',index:'rxMasterId', width:100,  hidden:true, editable:false,  editrules:{required:true}, editoptions:{size:10}},
				   		{name:'name',index:'name', width:120,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
						{name:'firstName',index:'firstName', width:120,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
						{name:'phone',index:'phone',align:'center', width:80,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'phone1',index:'phone1',align:'center', width:80,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'phone2',index:'phone2',align:'center', width:80, hidden:true,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'fax',index:'fax',align:'center', width:80, hidden:true,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'address1',index:'address1', width:150,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
						{name:'address2',index:'address2', width:150,hidden:true,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}, editoptions:{size:10}},
						{name:'city',index:'city', width:100,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'state',index:'state',align:'center', width:50,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'zip',index:'zip',align:'center', width:50, hidden:true,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'inactive',index:'inactive', align:'center', width:30,editable:true, editrules:{required:true}, formatter : activeFormatter, editoptions:{size:10}}],
				   	rowNum: aEmployeePage,	
					pgbuttons: true,	
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],
					viewrecords: true,
					pager: '#EmployeesListGridpager',
					sortname: 'rxMasterId', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Employees',
					height:540,	width: 1150,
				    emptyrecords: "No Employees available",
				    rownumbers: true,
				    altRows: true,
					altclass:'myAltRowClass',
				    loadComplete: function() {
				    	$(".ui-pg-selbox").attr("selected", aEmployeePage);
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
				    ondblClickRow: function(rowid) {
				    	var rowData = jQuery(this).getRowData(rowid); 
			    		var rxNumber = rowData['rxMasterId'];
			    		var name = rowData['name'];
			    		var name1 = name.replace('&', 'and');
			    		var name2 = name1.replace('&', 'and');
						document.location.href = "./employeedetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
					}
				}).navGrid('#EmployeesListGridpager',
					{add:false,edit:false,del:false,refresh:false,search:false}, //options
					//-----------------------edit options----------------------//
					{},
					//-----------------------add options----------------------//
					{},
					//-----------------------Delete options----------------------//
					{},
					//-----------------------search options----------------------//
					{}
				);
				//emptyMsgDiv.insertAfter(grid.parent());
				return true;
			}

			$(function() { var cache = {}, lastXhr;
			$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
				open: function(){ 
					$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./employeelist?oper=add" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Employee</a></b></div>');
					$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				},
				select: function (event, ui) {
					var name = ui.item.value;
					$.ajax({
						url: "./search/searchrolodex",
						mType: "GET",
						data : {'rolodex': name},
						success: function(data){
							 var entityValue="";
							 var rxId="";
							$.each(data, function(index, value){
								entityValue = value.entity;
								rxId =value.pk_fields; 
							});
							var value = name.split(": ");
							var entity = value[0];
							var text = value[1];
							var text1 = text.split(",  ");
							var searchText = text1[0];
							var search = searchText.replace('&','and');
							var search1= search.replace('&','and');
							var searchlist = "";
							if(entity == "EMP")	{
								searchlist = entity.replace("EMP","employeedetails");
							}if(entity == "VEND") {
								searchlist = entity.replace("VEND","vendordetails");
							}if(entity == "CUST") {
								searchlist = entity.replace("CUST","customerdetails");
							}if(entity == "ARCH") {
								searchlist = entity.replace("ARCH","architectDetails");
							}if(entity == "ENGR") {
								searchlist = entity.replace("ENGR","engineerDetails");
							}if(entity == "ARCH/ENGR"){
								searchlist = entity.replace("ARCH/ENGR","architectDetails");
							}if(entity == "G.C") {
								searchlist = entity.replace("G.C","rolodexdetails");
							}
							location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+'`'+search1+'`'+"";
						},
						error: function(Xhr) {
						}
					});
				},
				source: function( request, response ) { var term = request.term;
					if ( term in cache ) { response( cache[ term ] ); 	return; 	}
					lastXhr = $.getJSON( "search/searchEmployeeList", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				} }); });

			function openEmployeeDialog(){
				$("#employeeID").val(""); $("#employeeID1").val("");
				$("#address1ID").val("");$("#address2ID").val("");$("#cityNameListID").val("");$("#stateCodeID").val("");$("#pinCodeID").val("");
				$("#areaCode").val("");$("#exchangeCode").val("");$("#subscriberNumber").val("");$("#areaCode1").val("");$("#exchangeCode1").val("");
				$("#areaCode2").val("");$("#exchangeCode2").val("");$("#subscriberNumber2").val("");$("#subscriberNumber1").val("");$("InactiveCheckID").val("");
				$("#addNewEmployee").dialog("open");
			}


			function activeFormatter(value, options){
				if(value === 0){
					return "Yes";
				}else{
					return "No";
				}
			}
			function updateUserDetails(aActiveUser,aUserPage){
				$.ajax({
					url: "userlistcontroller/updateUserList",
					type: "POST",
					async:false,
					data :"&activeEmployeeList=" +aActiveUser+"&activeUserList=" +aUserPage,
					success: function(data) {
					}
				});
			}
			
		</script>
	</body>
</html>