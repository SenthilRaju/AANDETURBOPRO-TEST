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
	<form id="architecttabForm" name="architecttabForm">
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
							<td><label id="ArchitectCategory1label" style="display: none;">Salesrep</label></td>
							<td>&nbsp;
								<input type="text" id="architect_salesRepsList" name="salesReps" style="width: 200px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName0}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="ArchitectCategory1image" style="display: none;"></td>
							<td><input type="hidden" id="salesRepId" name="empSalesMan" value="${requestScope.customerRecord.cuAssignmentId0}" style="width: 30px;"></td>
						</tr>
						<tr>
							<td><label id="ArchitectCategory2label" style="display: none;">CSR</label></td>
							<td>&nbsp; 
								<input type="text" id="architect_CSRList" name="CSRListRep" style="width: 200px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName1}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="ArchitectCategory2image" style="display: none;"></td>
							<td><input type="hidden" id="CSRId" value="${requestScope.customerRecord.cuAssignmentId1}" name="empCSR" style="width: 30px;">
						</tr>
						<tr>
							<td><label id="ArchitectCategory3label" style="display: none;">Sales Manager</label></td>
							<td>&nbsp; 
								<input type="text" id="architect_SalesMgrList" name="SalesMgrList" style="width: 200px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName2}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="ArchitectCategory3image" style="display: none;"></td>
							<td><input type="hidden" id="salesMgrId" name="empSalesMgr" value="${requestScope.customerRecord.cuAssignmentId2}" style="width: 30px;">
						</tr>
						<tr>
							<td><label id="ArchitectCategory4label" style="display: none;">Engineer</label></td>
							<td>&nbsp; 
									<input type="text" id="architect_EngineersList" name="EngineersList" style="width: 200px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName3}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="ArchitectCategory4image" style="display: none;"></td>
							<td><input type="hidden" id="cus_engineerId" name="empEngineer" value="${requestScope.customerRecord.cuAssignmentId3}" style="width: 30px;">
						</tr>
						<tr>
							<td><label id="ArchitectCategory5label" style="display: none;">Project Manager</label></td>
							<td>&nbsp; 
									<input type="text" id="architect_PrjMgrList" name="PrjMgrList" style="width: 200px;display: none;" placeholder="Minimum 2 characters required" value="${requestScope.customerRecord.cuAssignmentName4}">
							</td>
							<td><img alt="search" src="./../resources/scripts/jquery-autocomplete/search.png" id="ArchitectCategory5image" style="display: none;"></td>
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
		<tr><td align="right"><label id="architecterrordiv" style="color: green;"></label></td></tr>
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
		loadarchitectcategories();
	});
	/*
	Updated by:Velmurugan
	Updated on:9-1-2014
	Description:while loading the page label,textbox and search image enable hide and show based upon sysassignment table
	*/
	function loadarchitectcategories(){

		var Category1labDesc=$("#CustomerCategory1hidden").val();
		var Category2labDesc=$("#CustomerCategory2hidden").val();
		var Category3labDesc=$("#CustomerCategory3hidden").val();
		var Category4labDesc=$("#CustomerCategory4hidden").val();
		var Category5labDesc=$("#CustomerCategory5hidden").val();
		
		//CustomerCategory1label
		$("#ArchitectCategory1label").text(Category1labDesc);
		$("#ArchitectCategory2label").text(Category2labDesc);
		$("#ArchitectCategory3label").text(Category3labDesc);
		$("#ArchitectCategory4label").text(Category4labDesc);
		$("#ArchitectCategory5label").text(Category5labDesc);
		
		//architect__salesRepsList architect__CSRList architect__SalesMgrList architect__ArchitectsList architect_PrjMgrList
		//CustomerCategory1image
		if(Category1labDesc==undefined ||Category1labDesc==null ||Category1labDesc==""||Category1labDesc.length==0){
			$("#ArchitectCategory1label").css({ display: "none" });
			$("#architect_salesRepsList").css({ display: "none" });
			$("#ArchitectCategory1image").css({ display: "none" });
		}else{
			$("#ArchitectCategory1label").css({ display: "inline-block" });
			$("#architect_salesRepsList").css({ display: "inline-block" });
			$("#ArchitectCategory1image").css({ display: "inline-block" });
		}
		if(Category2labDesc==undefined ||Category2labDesc==null ||Category2labDesc==""||Category2labDesc.length==0){
			$("#ArchitectCategory2label").css({ display: "none" });
			$("#architect_CSRList").css({ display: "none" });
			$("#ArchitectCategory2image").css({ display: "none" });
		}else{
			$("#ArchitectCategory2label").css({ display: "inline-block" });
			$("#architect_CSRList").css({ display: "inline-block" });
			$("#ArchitectCategory2image").css({ display: "inline-block" });
		}
		if(Category3labDesc==undefined ||Category3labDesc==null ||Category3labDesc==""||Category3labDesc.length==0){
			$("#ArchitectCategory3label").css({ display: "none" });
			$("#architect_SalesMgrList").css({ display: "none" });
			$("#ArchitectCategory3image").css({ display: "none" });
		}else{
			$("#ArchitectCategory3label").css({ display: "inline-block" });
			$("#architect_SalesMgrList").css({ display: "inline-block" });
			$("#ArchitectCategory3image").css({ display: "inline-block" });
		}
		if(Category4labDesc==undefined ||Category4labDesc==null ||Category4labDesc==""||Category4labDesc.length==0){
			$("#ArchitectCategory4label").css({ display: "none" });
			$("#architect_EngineersList").css({ display: "none" });
			$("#ArchitectCategory4image").css({ display: "none" });
		}else{
			$("#ArchitectCategory4label").css({ display: "inline-block" });
			$("#architect_EngineersList").css({ display: "inline-block" });
			$("#ArchitectCategory4image").css({ display: "inline-block" });
		}
		if(Category5labDesc==undefined ||Category5labDesc==null ||Category5labDesc==""||Category5labDesc.length==0){
			$("#ArchitectCategory5label").css({ display: "none" });
			$("#architect_PrjMgrList").css({ display: "none" });
			$("#ArchitectCategory5image").css({ display: "none" });
		}else{
			$("#ArchitectCategory5label").css({ display: "inline-block" });
			$("#architect_PrjMgrList").css({ display: "inline-block" });
			$("#ArchitectCategory5image").css({ display: "inline-block" });
		}
		
	}
	function saveArchitectTab(){
		//empSalesMan empCSR empSalesMgr empEngineer empPrjMgr
		if(!$('#architecttabForm').validationEngine('validate')) {
			return false;
		}if($("#architect_salesRepsList").val() === ''){
			document.architecttabForm.empSalesMan.value="";
			//$("#salesRepId").val("");
		}if($("#architect_CSRList").val() === ''){
			//$("#CSRId").val("");
			document.architecttabForm.empCSR.value="";
		}if($("#architect_SalesMgrList").val() === ''){
			document.architecttabForm.empSalesMgr.value="";
			//$("#salesMgrId").val("");
		}if($("#architect_EngineersList").val() === ''){
			document.architecttabForm.empEngineer.value="";
			//$("#cus_engineerId").val("");
		}if($("#architect_PrjMgrList").val() === ''){
			document.architecttabForm.empPrjMgr.value="";
			//$("#prjMgrId").val("");
		}
		
		var cuMasterid="${requestScope.customerRecord.rxMasterCategory1id}";
		if(cuMasterid === ''){
			var engineerForm=$("#architecttabForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
			url: "rolodex/NewArchitectAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					//window.location.reload();
					 setTimeout(function(){
		 				$("#architecterrordiv").text("");
		                 },3000);
				var errorText=$("#Category2Descset").val()+" details updated."
				$("#architecterrordiv").text(errorText);
				} });
		}else{
			var engineerForm=$("#architecttabForm").serialize();
			var rolodexID= getUrlVars()["rolodexNumber"];
			$.ajax({
				url: "rolodex/updateArchitectAssignee",
				mType: "GET",
				data : engineerForm+ "&rolodexId=" +rolodexID,
				success: function(data) {
					//window.location.reload();
					 setTimeout(function(){
			 				$("#architecterrordiv").text("");
			                 },3000);
					var errorText=$("#Category2Descset").val()+" details updated."
					$("#architecterrordiv").text(errorText);
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
		open: function(){ 
			 var len = $('.ui-autocomplete > li').length;
			// var value=$(this).val();
			 var value=$('.ui-autocomplete > li').text();
			 if(value.trim().length==0){
				 $("#architect_salesRepsList").val("");
				 $(".ui-autocomplete").css("display", "none");
				 }
			
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
			},open: function(){ 
				 var len = $('.ui-autocomplete > li').length;
					// var value=$(this).val();
					 var value=$('.ui-autocomplete > li').text();
					 if(value.trim().length==0){
						 $("#architect_CSRList").val("");
						 $(".ui-autocomplete").css("display", "none");
						 }
					
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
			},open: function(){ 
				 var len = $('.ui-autocomplete > li').length;
					// var value=$(this).val();
					 var value=$('.ui-autocomplete > li').text();
					 if(value.trim().length==0){
						 $("#architect_SalesMgrList").val("");
						 $(".ui-autocomplete").css("display", "none");
						 }
					
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
			},open: function(){ 
				 var len = $('.ui-autocomplete > li').length;
					// var value=$(this).val();
					 var value=$('.ui-autocomplete > li').text();
					 if(value.trim().length==0){
						 $("#architect_EngineersList").val("");
						 $(".ui-autocomplete").css("display", "none");
						 }
					
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
			open: function(){ 
				 var len = $('.ui-autocomplete > li').length;
					// var value=$(this).val();
					 var value=$('.ui-autocomplete > li').text();
					 if(value.trim().length==0){
						 $("#architect_PrjMgrList").val("");
						 $(".ui-autocomplete").css("display", "none");
						 }
					
				}, 
			error: function (result) {
			     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			}  }); });

	
</script>
