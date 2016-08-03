<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Products</title>
		
		<style type="text/css">
			
		</style>
	</head>
	<body>
		<table id="header">
			<tr>
				<td colspan="2">
					<div><jsp:include page="./headermenu.jsp" /></div>
				</td>
			</tr>
		</table>
		<table style="width:979px;margin:0 auto;">
			<tr align="right">
				<td style="padding-left: 50px; padding-right: 0px;">
					<input type="button" value="   Search" class="" id="searchproductsButton" onclick="" style="color:#FFF;background:#98b34a url('./../resources/styles/turbo-css/images/btn_search.png') no-repeat 5px 5px"> &nbsp;
					<input type="button" value="   Add New" class="" id="addproductsButton" onclick="openproductsDialog()" style="color:#FFF;background:#e0842a url('./../resources/styles/turbo-css/images/btn_addnew_orange.png') no-repeat 5px 5px">
				</td><td></td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="Employees" style="padding-left: 0px;">
						<table style="padding-left:0px" id="ProductsListGrid" class="scroll"></table>
						<div id="ProductsListGridpager" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				</td>
			</tr>
		</table>
		<div id="searchproducts">
			<form action="" id="searchForm">
				<table>
					<tr><td><label class="formLabel">Prod. Code: </label></td><td><input type="text" id=""></td></tr>
					
				</table>
			</form>
		</div>
	  <jsp:include page="Add_products.jsp"></jsp:include> 
		<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
		<script type="text/javascript">
			jQuery(document).ready(function(){
				$("#searchproducts").hide();
				$("input:button").button();
				$("input:text").addClass("ui-state-default ui-corner-all");
				loadChannelsGrid();
				
			});
			function loadChannelsGrid(){
				$("#ProductsListGrid").jqGrid({
					//url:'./employeeCrud',
					datatype: 'json',
					mtype: 'GET',
					pager: jQuery('#ProductsListGridpager'),
				   	colNames:['Prod. Code', 'Description', 'On Hand', 'Allocated', 'Available', 'On Order','Submitted'],
				   	colModel:[
				   		{name:'prodCode',index:'prodCode', width:55,editable:false,editoptions:{readonly:true,size:10},editoptions:{size:10}},
				   		{name:'Description',index:'Description', width:100,editable:true, editrules:{required:true}, editoptions:{size:10}},
				   		{name:'OnHand',index:'OnHand', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}},
				   		{name:'Allocated',index:'Allocated', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}},
				   		{name:'Available',index:'Available', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}},
				   		{name:'OnOrder',index:'OnOrder', width:30,editable:true, editrules:{required:true}, editoptions:{size:10}},
				   		{name:'Submitted',index:'Submitted', width:50,editable:true, editrules:{required:true}, editoptions:{size:10}}
				   	],
				   	postData: {
					},
					rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: false,
					sortname: 'rxMasterId', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Products',
					height:320,	width: 979, altRows: true,
					altclass:'myAltRowClass',
				    emptyrecords: "",
				    loadonce: true,
				    loadComplete: function() {
					},
				}).navGrid('#ProductsListGridpager',
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
			
			jQuery(function () {
					jQuery( "#searchproducts" ).dialog({
						autoOpen: false,
						height: 150,
						width: 400,
						title:"Search Products",
						modal: true,
						buttons:{
							"Submit": function(){
								
							},
							Cancel: function ()
							{
								jQuery( this ).dialog( "close" );
								return true;
							}
						},
						close: function () {
							return true;
						}
					});
					jQuery( "#searchproductsButton" ).button().click (function () {jQuery( "#searchproducts" ).dialog( "open" );});
					//jQuery( "#addEmployeeButton" ).button().click ();
					//jQuery( "#viewEmployeeButton" ).button().click ();
				return true;
			});
			
		</script>
			
	</body>
</html>