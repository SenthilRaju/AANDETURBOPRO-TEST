<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	.ui-menu-item a:hover { background-color: #637C92; color: #DCEDF9; border-color: #637C92 }
	.ui-autocomplete-w1 { background:url(./../resources/scripts/jquery-autocomplete/shadow.png) no-repeat bottom right; position:absolute; top:0px; left:0px; margin:6px 0 0 6px;  _background:none; _margin:1px 0 0 0; }
	.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:100px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:350px;  _margin:0; _overflow-x:hidden; }
	.ui-autocomplete .selected { background:#F0F0F0; }
	.ui-autocomplete div { padding:2px 5px; white-space:nowrap; overflow:hidden; }
	.ui-autocomplete strong { font-weight:normal; color:#3399FF; }
	.ui-autocomplete-loading { background: white url('./../resources/scripts/jquery-autocomplete/loading3.gif') right center no-repeat; }
</style>
<div id="architecttab">
	<form id="architecttabForm">
	<table>
		<tr height="10px;"><td></td></tr>
			<tr>
			<tr>
				<td style="width:70px"><b><label>Name</label></b><span style="color:red;"> *</span></td>
					<td><input type="text" name="custName" style="width:300px" id="cusNameIDName" class="validate[required] validate[maxSize[40]]" value=" ">
				</td>
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
				<fieldset class=" ui-widget-content ui-corner-all">
					<legend>
						<label><b>Employee's Assigned</b></label>
					</legend>
					<table>
						<tr>
							<td><label>Salesrep</label></td>
							<td>&nbsp;
								<input type="text" id="architect_salesRepsList" name="salesReps" style="width: 200px;" placeholder="Minimum 2 characters required" value="${requestScope.AssignedSalesRep}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
							<td><input type="hidden" id="salesRepId" name="empSalesMan" value="${requestScope.customerRecord.cuAssignmentId0}" style="width: 30px;"></td>
						</tr>
						<tr>
							<td><label>CSR</label></td>
							<td>&nbsp; 
								<input type="text" id="architect_CSRList" name="CSRListRep" style="width: 200px;" placeholder="Minimum 2 characters required" value="${requestScope.AssignedCSRs}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
							<td><input type="hidden" id="CSRId" value="${requestScope.customerRecord.cuAssignmentId1}" name="empCSR" style="width: 30px;">
						</tr>
						<tr>
							<td><label>Sales Manager</label></td>
							<td>&nbsp; 
								<input type="text" id="architect_SalesMgrList" name="SalesMgrList" style="width: 200px;" placeholder="Minimum 2 characters required" value="${requestScope.AssignedSalesMGRs}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
							<td><input type="hidden" id="salesMgrId" name="empSalesMgr" value="${requestScope.customerRecord.cuAssignmentId2}" style="width: 30px;">
						</tr>
						<tr>
							<td><label>Engineer</label></td>
							<td>&nbsp; 
									<input type="text" id="architect_EngineersList" name="EngineersList" style="width: 200px;" placeholder="Minimum 2 characters required" value="${requestScope.AssignedEngineers}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
							<td><input type="hidden" id="cus_engineerId" name="empEngineer" value="${requestScope.customerRecord.cuAssignmentId3}" style="width: 30px;">
						</tr>
						<tr>
							<td><label>Project Manager</label></td>
							<td>&nbsp; 
									<input type="text" id="architect_PrjMgrList" name="PrjMgrList" style="width: 200px;" placeholder="Minimum 2 characters required" value="${requestScope.AssignedProjManagers}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png"></td>
							<td><input type="hidden" id="prjMgrId" name="empPrjMgr" value="${requestScope.customerRecord.cuAssignmentId4}" style="width: 30px;">
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
		<tr height="300px;"><td></td></tr>
	</table>
	<div style="height: 70px;">
	<table style="width:1100px">
		<tr><td><hr width=""></td></tr>
		<tr>
			<td align="right"><input type="button"  class="savehoverbutton turbo-blue" value="Save"id="mainsave" onclick="saveArchitectTab()" style="width:80px"></td>
		</tr>
	</table>
</div>
</form>
</div>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		$("#architecttabForm").validationEngine('attach');
		var name =$("#customerName").text();
		name = name.replace('(', ' ');
		name = name.replace(')', ' ');
		$("#cusNameIDName").val('');
		$("#cusNameIDName").val(name);
	});

	function saveArchitectTab(){
		if(!$('#architecttabForm').validationEngine('validate')) {
			return false;
		}if($("#architect_salesRepsList").val() === ''){
			$("#salesRepId").val("");
		}if($("#architect_CSRList").val() === ''){
			$("#CSRId").val("");
		}if($("#architect_SalesMgrList").val() === ''){
			$("#salesMgrId").val("");
		}if($("#architect_EngineersList").val() === ''){
			$("#cus_engineerId").val("");
		}if($("#architect_PrjMgrList").val() === ''){
			$("#prjMgrId").val("");
		}
		
		var cuMasterid="${requestScope.customerRecord.cuMasterId}";
		if(cuMasterid === ''){
			var engineerForm=$("#architecttabForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
			url: "rolodex/NewEngineerAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					window.location.reload();
				} });
		}else{
			var engineerForm=$("#architecttabForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
				url: "rolodex/updateEngineerAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					window.location.reload();
				} });
		}
	}

	$(function() { var cache = {}, lastXhr;
	$( "#architect_salesRepsList" ).autocomplete({ minLength: 2,timeout :1000,
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
		$( "#architect_CSRList" ).autocomplete({ minLength: 2, timeout :1000,
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
		$( "#architect_SalesMgrList" ).autocomplete({ minLength: 2,timeout :1000,
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
		$( "#architect_EngineersList" ).autocomplete({ minLength: 2,timeout :1000,
			select: function( event, ui ) { var id = ui.item.id; $("#cus_engineerId").val(id); },
			source: function( request, response ) { var term = request.term;
				if ( term in cache ) { response( cache[ term ] ); 	return; 	}
				lastXhr = $.getJSON( "employeeCrud/engineer", request, function( data, status, xhr ) { cache[ term ] = data; 
					if ( xhr === lastXhr ) { response( data ); 	} });
			},
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });
		
	  $(function() { var cache = {}, lastXhr;
		$( "#architect_PrjMgrList" ).autocomplete({ minLength: 2,timeout :1000,
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
