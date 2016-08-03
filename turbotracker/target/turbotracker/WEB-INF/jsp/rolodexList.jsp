<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
					<td align="right">
						<table>
					    <tr><td> 
					    <input type="button" value="  Add" class="add" id="addvendorlist" onclick="openvendorlistDialog()">
					    </td></tr>
					    </table>
			    	</td>
			    </tr>
			    <tr>
			    	<td>
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
				$("#address1ID").val("");$("#address2ID").val("");$("#cityNameListID").val("");$("#stateCodeID").val("");$("#pinCodeID").val("");
				$("#areaCode").val("");$("#exchangeCode").val("");$("#subscriberNumber").val("");$("#areaCode1").val("");$("#exchangeCode1").val("");
				$("#areaCode2").val("");$("#exchangeCode2").val("");$("#subscriberNumber2").val("");$("#subscriberNumber1").val("");
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
					lastXhr = $.getJSON( "search/searchrolodexlist", request, function( data, status, xhr ) { cache[ term ] = data; 
						if ( xhr === lastXhr ) { response( data ); 	} });
				},
				error: function (result) {
				     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
				}  }); });
			
		</script>
		</body>
	</html>
		 