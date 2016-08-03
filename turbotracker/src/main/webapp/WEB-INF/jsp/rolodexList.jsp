<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbo Pro - Rolodex</title>
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
			<jsp:include page="rolodex/addnewRolodexList.jsp"></jsp:include>
			<table style="width:600px;margin:0 auto;">
				<tr>
				<td align="left">
				
				<table>
					    <tr> 
				<td><label>Categories:</label></td>
				<!-- <td><span  id="category1Td1" style="display: none;"><input type="checkbox" id="customercheckbox" onclick="rolodexfilter(this.id)" ><label >Customer</label></span></td>
				<td><span  id="category2Td1" style="display: none;"><input type="checkbox" id="vendorcheckbox" onclick="rolodexfilter(this.id)"/><label >Vendor</label></span></td>
				<td><span  id="category3Td1" style="display: none;"><input type="checkbox" id="employeecheckbox" onclick="rolodexfilter(this.id)"/><label >Employee</label></span></td>
				<td><span  id="category4Td1" style="display: none;"><input type="checkbox" id="architectcheckbox" onclick="rolodexfilter(this.id)" /><label >Architect</label></span></td>
				<td><span  id="category5Td1" style="display: none;"><input type="checkbox" id="engineercheckbox" onclick="rolodexfilter(this.id)"/><label >Engineer</label></span></td>
				<td><span  id="category6Td1" style="display: none;"><input type="checkbox" id="gccheckbox" onclick="rolodexfilter(this.id)" /><label >G.C/Constr.MGR</label></span></td>
				<td><span  id="category7Td1" style="display: none;"><input type="checkbox" id="ownercheckbox" onclick="rolodexfilter(this.id)" /><label >Owner</label></span></td>
				<td><span  id="category8Td1" style="display: none;"><input type="checkbox" id="bondcheckbox" onclick="rolodexfilter(this.id)" /><label >Bond Agent</label></span></td>
				 -->
				  
				<td><input type="checkbox" id="customercheckbox" onclick="rolodexfilter(this.id,9)" ><label >Customer</label></td>
				<td><input type="checkbox" id="vendorcheckbox" onclick="rolodexfilter(this.id,10)"/><label >Vendor</label></td>
				<td><input type="checkbox" id="employeecheckbox" onclick="rolodexfilter(this.id,11)"/><label >Employee</label></td>
				<td><input type="checkbox" id="architectcheckbox" onclick="rolodexfilter(this.id,1)" style="display: none;"/><label id="category1label" style="display: none;"><c:out value='${requestScope.Category1Desc}'/><!-- Architect --></label></td>
				<td><input type="checkbox" id="engineercheckbox" onclick="rolodexfilter(this.id,2)" style="display: none;"/><label id="category2label" style="display: none;"><c:out value='${requestScope.Category2Desc}'/><!-- Engineer --></label></td>
				<td><input type="checkbox" id="gccheckbox" onclick="rolodexfilter(this.id,3)" style="display: none;"/><label id="category3label" style="display: none;"><c:out value='${requestScope.Category3Desc}'/><!-- G.C/Constr.MGR --></label></td>
				<td><input type="checkbox" id="ownercheckbox" onclick="rolodexfilter(this.id,4)" style="display: none;"/><label id="category4label" style="display: none;"><c:out value='${requestScope.Category4Desc}'/><!-- Owner --></label></td>
				<td><input type="checkbox" id="ListCategory5Desc" onclick="rolodexfilter(this.id,5)" style="display: none;"/><label id="category5label" style="display: none;"><c:out value='${requestScope.Category5Desc}'/></label></td>
				<td><input type="checkbox" id="ListCategory6Desc" onclick="rolodexfilter(this.id,6)" style="display: none;"/><label id="category6label" style="display: none;"><c:out value='${requestScope.Category6Desc}'/></label></td>
				<td><input type="checkbox" id="ListCategory7Desc" onclick="rolodexfilter(this.id,7)" style="display: none;"/><label id="category7label" style="display: none;"><c:out value='${requestScope.Category7Desc}'/></label></td>
				<td><input type="checkbox" id="ListCategory8Desc" onclick="rolodexfilter(this.id,8)" style="display: none;"/><label id="category8label" style="display: none;"><c:out value='${requestScope.Category8Desc}'/></label></td>
				
				<!-- <td><span  id="category5Td" style="display: none;"><input type="checkbox" id="gccheckbox" onclick="rolodexfilter(this.id)" /><label >G.C/Constr.MGR</label></span></td>
				<td><span  id="category5Td" style="display: none;"><input type="checkbox" id="gccheckbox" onclick="rolodexfilter(this.id)" /><label >G.C/Constr.MGR</label></span></td> -->
			
				
				</td></tr>
					    </table>
				</td>
					<td align="right">
						<table>
					    <tr><td> 
					    <input type="button" value="  Add" class="add" id="addvendorlist" onclick="openvendorlistDialog()">
					    </td></tr>
					    </table>
			    	</td>
			    </tr>
			    <tr>
			    	<td colspan="2">
						<table id="rolodexList"></table>
						<div id="rolodexListPager"></div>
	 	        	</td>
	 	        </tr>
 	        </table>
 	        <div style="padding-top: 25px;">
	 	        <table id="footer">
					<tr>
						<td colspan="2">
							<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/jquery.validationEngine.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/Validation_Engine/languages/jquery.validationEngine-en.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
		<script type="text/javascript">

		jQuery(document).ready(function(){
			$("#addNewRolodex").tabs();

			$.ajax({
				url: "./rolodex/getRolodexCategory",
				mType: "GET",
				data : {'userInfoID' : 1},
				success: function(data) {
					
				console.log('RxCategories category1:'+data.rxMasterCategory1desc);
				 
				if(data.rxMasterCategory1desc!=null && data.rxMasterCategory1desc!=''){
					$("#architectcheckbox").css('display','inline-block');
					$("#category1label").css('display','inline-block');
					
					$("#category1Td1").css('display','block');
					$("#category1").text(data.rxMasterCategory1desc);
				}
				if(data.rxMasterCategory2desc!=null && data.rxMasterCategory2desc!=''){
					$("#engineercheckbox").css('display','inline-block');
					$("#category2label").css('display','inline-block');
					
					$("#category2Td1").css('display','block');
					$("#category2").text(data.rxMasterCategory2desc);
				}
				if(data.rxMasterCategory3desc!=null && data.rxMasterCategory3desc!=''){
					$("#gccheckbox").css('display','inline-block');
					$("#category3label").css('display','inline-block');
					
					$("#category3Td1").css('display','block');
					$("#category3").text(data.rxMasterCategory3desc);
				}
				if(data.rxMasterCategory4desc!=null && data.rxMasterCategory4desc!=''){
					$("#ownercheckbox").css('display','inline-block');
					$("#category4label").css('display','inline-block');
					
					$("#category4Td1").css('display','block');
					$("#category4").text(data.rxMasterCategory4desc);
				}
				if(data.rxMasterCategory5desc!=null && data.rxMasterCategory5desc!=''){
					$("#ListCategory5Desc").css('display','inline-block');
					$("#category5label").css('display','inline-block');
					
					$("#category5Td1").css('display','block');
					$("#category5").text(data.rxMasterCategory5desc);
				}
				if(data.rxMasterCategory6desc!=null && data.rxMasterCategory6desc!=''){
					$("#ListCategory6Desc").css('display','inline-block');
					$("#category6label").css('display','inline-block');
					
					$("#category6Td1").css('display','block');
					$("#category6").text(data.rxMasterCategory6desc);
				}
				if(data.rxMasterCategory7desc!=null && data.rxMasterCategory7desc!=''){
					$("#ListCategory7Desc").css('display','inline-block');
					$("#category7label").css('display','inline-block');
					
					$("#category7Td1").css('display','block');
					$("#category7").text(data.rxMasterCategory7desc);
				}
				if(data.rxMasterCategory8desc!=null && data.rxMasterCategory8desc!=''){
					$("#ListCategory8Desc").css('display','inline-block');
					$("#category8label").css('display','inline-block');
					
					$("#category8Td1").css('display','block');
					$("#category8").text(data.rxMasterCategory8desc);
				}
				}
		   });
			
		});


		
		var category = '';
			jQuery(document).ready(function() {
				var addCustomer = getUrlVars()["oper"];
				if(addCustomer === "add"){
					 openvendorlistDialog();
				}else{
					$("#addNewRolodex").css("display", "none");
				}
				loadRolodexList();
			});

			function loadRolodexList(){
				var aRolodexPage = "${requestScope.userDetails.rolodexperpage}";
				$("#rolodexList").jqGrid({
					datatype: 'JSON',
					mtype: 'GET',
					url:'./rolodex',
					colNames:['rxId', 'Name', 'Phone','Address', 'City', 'State'],
					colModel:[
				   		{name:'rxMasterId',index:'rxMasterId', width:170,editable:false, hidden:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'name',index:'name', width:120,editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" '; }, editrules:{required:true}, editoptions:{size:10}},
						{name:'phone1',index:'phone1',align:'center', width:80,editable:true, formatter:phoneFormatter, editrules:{required:true}, editoptions:{size:10}},
						{name:'address1',index:'address1', width:100,editable:true, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" '; }, editrules:{required:true}, editoptions:{size:10}},
						{name:'city',index:'city', width:60,editable:true, editrules:{required:true}, editoptions:{size:10}},
						{name:'state',index:'state',align:'center', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}}],
		          	rowNum: aRolodexPage,
					pgbuttons: true,
					recordtext: '',
					rowList: [50, 100, 200, 500, 1000],	viewrecords: true,
					pager: '#rolodexListPager',
					sortname: 'Name', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Rolodex',
					height:538,	width: 1150, rownumbers:true, altRows: true,
					altclass:'myAltRowClass',
					//scrollOffset: 0,
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
			    		var phone = rowData['phone1'];
			    		var name1 = name.replace('&', 'and');
			    		var name2 = name1.replace('&', 'and');
			    		createtpusage('Company-Rolodex','Rolodex Grid View','Info','Company-Employee,Viewing Rolodex,rxMasterId:'+rxNumber);
						document.location.href = "./rolodexdetails?rolodexNumber="+rxNumber+"&name="+'`'+name2+'`';
					}
				});
			}
			
			function phoneFormatter(cellvalue, options, rowObject) {
				return formatPhone(cellvalue);
			}
			
			/**  *  Format phone numbers */ 
			function formatPhone(phonenum) {
				phonenum = $.trim(phonenum);
				if(contains(phonenum, "Ext")) {
					var phNoArray = new Array();
					phNoArray = phonenum.split("Ext");
					phonenum = $.trim(phNoArray[0]);
				}
				var regexObj = /^(?:\+?1[-. ]?)?(?:\(?([0-9]{3})\)?[-. ]?)?([0-9]{3})[-. ]?([0-9]{4})$/;    
		    	if (regexObj.test(phonenum)) {
					var parts = phonenum.match(regexObj);
					var phone = "";
					if (parts[1]) { phone += "(" + parts[1] + ") "; }
					phone += parts[2] + "-" + parts[3];
					return phone;
				} else {
					//invalid phone number
					return '';
				}
			}

			function contains(str, text) {
				return str.indexOf(text) >= 0;
			}
			
			function openvendorlistDialog() {
				$("#rolodexID").val("");
				$("#rolodexAddress1").val(''); $("#rolodexAddress2").val(''); $("#rolodexCity").val(''); $("#rolodexState").val('');$("#rolodexZip").val('');$("#officeextension").val('');
				$("#exchangeId").val('');$("#subscriberId").val(''); $("#areaId").val(''); 
				$("#exchangeId1").val('');$("#subscriberId1").val('');$("#areaId1").val('');
				$("#exchangeId2").val('');$("#subscriberId2").val('');$("#areaId2").val(''); 
				$("#shipAddress").attr("checked", false); $("#mailAddress").attr("checked", false);  $("#defaultAddress").attr("checked", false);
				document.getElementById("customerCheckID").checked = false;
				document.getElementById("vendorCheckID").checked = false;
				document.getElementById("employeeCheckID").checked = false;
				document.getElementById("architectCheckID").checked = false;
				document.getElementById("architectCheckID").checked = false;
				document.getElementById("gcCheckID").checked = false;
				document.getElementById("engineerCheckID").checked = false;
				jQuery("#addNewRolodex").dialog("open");
			}

			$(function() { var cache = {}, lastXhr;
			$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
				open: function(){ 
					$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./rolodexList" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New Rolodex</a></b></div>');
					$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				},
				select: function (event, ui) {
					var name = ui.item.value;
					$.ajax({
						url: "./search/searchrolodex",
						mType: "GET",
						data : {'rolodex': name},
						success: function(data){
							var name = ui.item.value;
							var rxId=ui.item.pk_field;
							/*$.ajax({
								url: "./search/searchrolodex",
								mType: "GET",
								data : {'rolodex': name},
								success: function(data){
									 var rxId="";
									$.each(data, function(index, value){
										entityValue = value.entity;
										rxId =value.pk_fields; 
									});*/
									var value = name.split(": ");
									var entity = value[0];
									var text = value[1];
									var text1 = text.split(",  ");
									var searchText = text1[0];
									var search = searchText.replace('&','and');
									var search1= search.replace('&','and');
									var searchlist = "";
									var checkpermission=false;
									if(entity == "EMP")	{
										searchlist = entity.replace("EMP","employeedetails");
										checkpermission=getGrantpermissionprivilage('Employees',0);
									}if(entity == "VEND") {
										searchlist = entity.replace("VEND","vendordetails");
										checkpermission=getGrantpermissionprivilage('Vendors',0);
									}if(entity == "CUST") {
										searchlist = entity.replace("CUST","customerdetails");
										checkpermission=getGrantpermissionprivilage('Customers',0);
									}else{
										searchlist = "rolodexdetails";
										checkpermission=getGrantpermissionprivilage('Rolodex',0);
									}/*if(entity == "ARCH") {
										searchlist = entity.replace("ARCH","architectDetails");
										checkpermission=getGrantpermissionprivilage('Rolodex',0);
									}if(entity == "ENGR") {
										searchlist = entity.replace("ENGR","engineerDetails");
										checkpermission=getGrantpermissionprivilage('Rolodex',0);
									}if(entity == "ARCH/ENGR"){
										searchlist = entity.replace("ARCH/ENGR","architectDetails");
										checkpermission=getGrantpermissionprivilage('Rolodex',0);
									}if(entity == "G.C") {
										searchlist = entity.replace("G.C","rolodexdetails");
										checkpermission=getGrantpermissionprivilage('Rolodex',0);
									}*/
									
									
									
									
						    		if(checkpermission){
									location.href="./"+searchlist+"?rolodexNumber="+rxId+"&name="+'`'+search1+'`'+"";
						    		}else{
						    			$("#rolodex").val("");
						    		}
								/*},
								error: function(Xhr) {
									var errorText = $(Xhr.responseText).find('u').html();
									var newDialogDiv = jQuery(document.createElement('div'));
									jQuery(newDialogDiv).html('<span style="color:red;"><b>Error: '+errorText+'</b></span>');
									jQuery(newDialogDiv).dialog({modal: true, width:500, height:250, title:"Fatal Error.", 
															buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
								}
							});*/
						},
						error: function(Xhr) {
						}
					});
				},
				source: function( request, response ) { var term = request.term;
					if ( term in cache ) { response( cache[ term ] ); 	return; 	}
					lastXhr = $.getJSON( "search/searchrolodexlist", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
			
			/*
			*Added by: Praveenkumar
			*Created : 08/18/2014
			*Purpose: Enable catergory search option in rolodex
			*/
			function rolodexfilter(id,checkboxnumber){
				
					var checkboxes = document.getElementsByTagName('input');
			 		for (var i = 0; i < checkboxes.length; i++) {
	            	 	if (checkboxes[i].type == 'checkbox' && checkboxes[i].id!=id) {
	                	 checkboxes[i].checked = false;
	            		 }
	         		}
			 		if($('#'+id).is(":checked")){
					 		id = id.replace("checkbox","");	 		
					 		jQuery("#rolodexList").jqGrid().setGridParam({
			 						url:'./rolodex?category='+checkboxnumber 
				    		}) .trigger("reloadGrid");
			 		
			 		}else{
			 			$("#rolodexList").trigger("reload");
			 			
			 		}
			}


			
		</script>
		</body>
	</html>
		 