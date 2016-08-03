<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link type="text/css"href="./../resources/web-plugins/Validation_Engine/validationEngine.jquery.css" rel="stylesheet" media="screen">
<style type="text/css">
.ui-menu-item a:hover {background-color: #637C92;color: #DCEDF9;border-color: #637C92}
.ui-autocomplete-w1 {background: url(./../resources/scripts/jquery-autocomplete/shadow.png)no-repeat bottom right;position: absolute;top: 0px;left: 0px;margin: 6px 0 0 6px;_background: none;_margin: 1px 0 0 0;}
.ui-autocomplete {border: 1px solid #999;background: #EEEEEE;cursor: default;height: auto;text-align: left;max-height: 100px;overflow-y: scroll;overflow: auto;margin: -6px 6px 6px -6px;_height: 350px;_margin: 0;_overflow-x: hidden;}
.ui-autocomplete .selected {background: #F0F0F0;}
.ui-autocomplete div {padding: 2px 5px;white-space: nowrap;overflow: hidden;}
.ui-autocomplete strong {font-weight: normal;color: #3399FF;}
.ui-autocomplete-loading {background: white url('./../resources/scripts/jquery-autocomplete/loading3.gif') right center no-repeat;}
.formError .formErrorContent {background: none repeat scroll 0 0 #A90F16}
.formError .formErrorArrow div {background: none repeat scroll 0 0 #A90F16}
</style>
<div >
<form id="employeeAssigneeForm">
	<table>
		<tr height="10px;"><td></td></tr>
		<tr>
			<td><jsp:include page="vendorheader.jsp"></jsp:include></td>
		</tr>
		<tr height="10px;"><td></td></tr>
		<tr>
			<td colspan="2"><hr style="width:1100px; color:#C2A472;"></td>
		</tr>
		<tr height="10px;"><td></td></tr>
	</table>
		<table>
			<tr>
				<td>
				<fieldset class=" ui-widget-content ui-corner-all" style="width: 480px">
						<legend>
							<b><label>Employee's Assigned</label></b>
						</legend>
						<table>
							<tr>
								<td><label id="EmployeeCategory1label" style="display: none;">Salesman:</label></td>
								<td>&nbsp; <input type="text" id="employee_salesRepsList" style="width: 290px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName0}"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="EmployeeCategory1image" style="display: none;"></td>
								<td><input type="hidden" id="salesRepId" name="empSalesMan" value="${requestScope.customerRecord.cuAssignmentId0}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label id="EmployeeCategory2label" style="display: none;">CSR:</label></td>
								<td>&nbsp; <input type="text" id="employee_CSRList" style="width: 290px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName1}"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="EmployeeCategory2image" style="display: none;"></td>
								<td><input type="hidden" id="CSRId" value="${requestScope.customerRecord.cuAssignmentId1}" name="empCSR" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label id="EmployeeCategory3label" style="display: none;">Sales Mgr:</label></td>
								<td>&nbsp; <input type="text" id="employee_SalesMgrList" style="width: 290px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName2}"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="EmployeeCategory3image" style="display: none;"></td>
								<td><input type="hidden" id="salesMgrId" name="empSalesMgr" value="${requestScope.customerRecord.cuAssignmentId2}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label id="EmployeeCategory4label" style="display: none;">Engineer:</label></td>
								<td>&nbsp; <input type="text" id="employee_EngineersList" style="width: 290px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName3}"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="EmployeeCategory4image" style="display: none;"></td>
								<td><input type="hidden" id="engineerId" name="empEngineer" value="${requestScope.customerRecord.cuAssignmentId3}" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label id="EmployeeCategory5label" style="display: none;">prj. Mgr:</label></td>
								<td>&nbsp; <input type="text" id="employee_PrjMgrList" style="width: 290px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName4}"></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="EmployeeCategory5image" style="display: none;"></td>
								<td><input type="hidden" id="prjMgrId" name="empPrjMgr" value="${requestScope.customerRecord.cuAssignmentId4}" style="width: 30px;"></td>
							</tr>
							<!-- 	<tr>
								<td><label>Take off:</label></td>
								<td>&nbsp; <input type="text" id="jobMain_TakeOffList" style="width: 290px;" value=""></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="takeOffId" name="empTakeOff" value="" style="width: 30px;"></td>
							</tr>
							<tr>
								<td><label>Quote by:</label></td>
								<td>&nbsp; <input type="text" id="jobMain_QuoteByList" style="width: 290px;" value=""></td>
								<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
								<td><input type="hidden" id="quoteId" name="empQuoteBy" value="" style="width: 30px;"></td>
							</tr>
							 <tr hidden="hidden" class="createdChangedBy">
								<td><label>Created EHB:</label></td>
								<td>&nbsp;&nbsp;<label id="created"></label> <input type="text" disabled="disabled" style="width:200px" name="createdDate" class="datepicker" id="created">
								</td>
							</tr> 
							<tr hidden="hidden" class="createdChangedBy">
								<td><label>Chg. EHB:</label></td>
								<td>&nbsp;&nbsp;<label id="created"></label> <input type="text" disabled="disabled" style="width:200px"  name="changedDate" class="datepicker" id="changed"></td>
							</tr> -->
							<tr height="20px"></tr>
						</table>
					</fieldset>
					</td>
					</tr>
					<tr height="300px;">
				<td></td>
			</tr>
			</table>
					<div style="height: 70px;">
			<table style="width: 1100px">
				<tr>
					<td><hr width=""></td>
				</tr>
				<tr>
					<td align="right"><input type="button" class="savehoverbutton turbo-blue" value="Save"id="mainsave" onclick="saveViewTab()" style="width: 80px"></td>
				</tr>
			</table>
			</div>
		</form>
	</div>
	<script type="text/javascript"
	src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
	<script type="text/javascript"
	src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
	<script type="text/javascript">
	
	$(document).ready(function(){
		$("#employeeAssigneeForm").validationEngine('attach');
		loademployeeviewcategories();
	});

	/*
	Updated by:Velmurugan
	Updated on:9-1-2014
	Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
	*/
	function loademployeeviewcategories(){
		var Category1labDesc=$("#CustomerCategory1hidden").val();
		var Category2labDesc=$("#CustomerCategory2hidden").val();
		var Category3labDesc=$("#CustomerCategory3hidden").val();
		var Category4labDesc=$("#CustomerCategory4hidden").val();
		var Category5labDesc=$("#CustomerCategory5hidden").val();
		
		//CustomerCategory1label
		$("#EmployeeCategory1label").text(Category1labDesc);
		$("#EmployeeCategory2label").text(Category2labDesc);
		$("#EmployeeCategory3label").text(Category3labDesc);
		$("#EmployeeCategory4label").text(Category4labDesc);
		$("#EmployeeCategory5label").text(Category5labDesc);
		
		//employee_salesRepsList employee_CSRList employee_SalesMgrList employee_EngineersList employee_PrjMgrList
		//CustomerCategory1image
		if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
			$("#EmployeeCategory1label").css({ display: "none" });
			$("#employee_salesRepsList").css({ display: "none" });
			$("#EmployeeCategory1image").css({ display: "none" });
		}else{
			$("#EmployeeCategory1label").css({ display: "inline-block" });
			$("#employee_salesRepsList").css({ display: "inline-block" });
			$("#EmployeeCategory1image").css({ display: "inline-block" });
		}
		if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
			$("#EmployeeCategory2label").css({ display: "none" });
			$("#employee_CSRList").css({ display: "none" });
			$("#EmployeeCategory2image").css({ display: "none" });
		}else{
			$("#EmployeeCategory2label").css({ display: "inline-block" });
			$("#employee_CSRList").css({ display: "inline-block" });
			$("#EmployeeCategory2image").css({ display: "inline-block" });
		}
		if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
			$("#EmployeeCategory3label").css({ display: "none" });
			$("#employee_SalesMgrList").css({ display: "none" });
			$("#EmployeeCategory3image").css({ display: "none" });
		}else{
			$("#EmployeeCategory3label").css({ display: "inline-block" });
			$("#employee_SalesMgrList").css({ display: "inline-block" });
			$("#EmployeeCategory3image").css({ display: "inline-block" });
		}
		if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
			$("#EmployeeCategory4label").css({ display: "none" });
			$("#employee_EngineersList").css({ display: "none" });
			$("#EmployeeCategory4image").css({ display: "none" });
		}else{
			$("#EmployeeCategory4label").css({ display: "inline-block" });
			$("#employee_EngineersList").css({ display: "inline-block" });
			$("#EmployeeCategory4image").css({ display: "inline-block" });
		}
		if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
			$("#EmployeeCategory5label").css({ display: "none" });
			$("#employee_PrjMgrList").css({ display: "none" });
			$("#EmployeeCategory5image").css({ display: "none" });
		}else{
			$("#EmployeeCategory5label").css({ display: "inline-block" });
			$("#employee_PrjMgrList").css({ display: "inline-block" });
			$("#EmployeeCategory5image").css({ display: "inline-block" });
		}
		
		
	}
	function saveViewTab(){
		if(!$('#employeeAssigneeForm').validationEngine('validate')) {
			return false;
		}if($("#employee_salesRepsList").val() === ''){
			$("#salesRepId").val("");
		}if($("#employee_CSRList").val() === ''){
			$("#CSRId").val("");
		}if($("#employee_SalesMgrList").val() === ''){
			$("#salesMgrId").val("");
		}if($("#employee_EngineersList").val() === ''){
			$("#cus_engineerId").val("");
		}if($("#employee_PrjMgrList").val() === ''){
			$("#prjMgrId").val("");
		}
		
		var cuMasterid="${requestScope.customerRecord.rxMasterCategoryViewid}";
		if(cuMasterid === ''){
			var engineerForm=$("#employeeAssigneeForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
			url: "rolodex/NewViewAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					window.location.reload();
				} });
		}else{
			var engineerForm=$("#employeeAssigneeForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
				url: "rolodex/updateViewAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					window.location.reload();
				} });
		}
	}
	
	$(function() { var cache = {}, lastXhr;
	$( "#employee_salesRepsList" ).autocomplete({ minLength: 2,timeout :1000,
		select: function( event, ui ) { var id = ui.item.id; $("#salesRepId").val(id); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "salescontroller/salesrep", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });

	  $(function() { var cache = {}, lastXhr;
		$( "#employee_CSRList" ).autocomplete({ minLength: 2, timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#CSRId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/CSRList", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	  $(function() { var cache = {}, lastXhr;
		$( "#employee_SalesMgrList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#salesMgrId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/salesMGR", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	  $(function() { var cache = {}, lastXhr;
		$( "#employee_EngineersList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#engineerId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
		
	  $(function() { var cache = {}, lastXhr;
		$( "#employee_PrjMgrList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#prjMgrId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/projectManager", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
	
	</script>