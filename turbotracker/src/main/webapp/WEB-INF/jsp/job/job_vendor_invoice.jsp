<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
		<table>
		<tr>
		<td>
		<table style="padding-left:15px">
		<tr><td><label><b>Payable To</b></label></td></tr>
		<tr><td><select style="width:180px"><option></option></select></td></tr>
		<tr><td><textarea rows="4" cols="25"></textarea>
		</table>
		</td>
		<td style="vertical-align:top; padding-left:100px">
				<table>
				<tr><td><label><b>Invoice/Bill</b></label></td></tr>
				<tr><td><label>Received</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
					<td><label>Vendor Inv.#</label></td><td><input type="text" style="width:75px"></td>
				</tr>
				<tr><td><label>Dated</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
					<td><label>A/p Acct</label></td><td><select style="width:75px"><option></option></select></td>
				</tr>
				<tr><td><label>Due</label></td><td><input type="text" style="width:75px" id="datepicker"></td>
					<td><label>Post Date</label></td><td><input type="checkbox"></td>
				</tr>
				
				<tr><td colspan="2"><label>PO #</label></td><td><label>Ship:</label></td><td><input type="text" style="width:75px" id="datepicker"></td></tr>
				<tr><td><label>Ship Via</label></td><td><select style="width:75px"><option></option></select></td>
					<td><label>Pro #</label></td><td><input type="text" style="width:75px"></td>
				</tr>
				</table>
		</td>
			</tr>
			</table>
			<br>
			<table style="padding-left:5px">
			<tr><td><table id="vendorinvoice1" ></table><div id="vendorinvoicepager"></div></td></tr>
			</table>
			<br>
			<table style="padding-left:20px">
			<tr><td><label><b>Totals</b></label></td></tr>
			<tr><td><label>Subtotal</label><td><input type="text" style="width:75px"></td><td><label>Freight</label></td><td><input type="text" style="width:75px"></td>
			    <td><label>Tax</label></td><td><input type="text" style="width:75px"></td><td><label>Bal Due</label></td><td><input type="text" style="width:75px"></td></tr>
			</table>
			<br>
	<script type="text/javascript">
		 $(document).ready(function(){
			$(".datepicker").datepicker();
			$("input:text").addClass("ui-state-default ui-corner-all");
			$("#vendorinvoice1").jqGrid({
				datatype:'JSON',
				mtype:'GET',
				colNames:['Product No','Description','Qty','Cost Ea','Mult','Amount'],
				colModel :[
				           {name:'ProductNo',index:'ProductNo',align:'center',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				           {name:'Description',index:'Description',align:'center',width:200,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				           {name:'Qty',index:'Qty',align:'center',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				           {name:'Cost Ea',index:'Cost Ea',align:'center',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				           {name:'Mult',index:'Mult',align:'center',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:30,readonly:true},editrules:{edithidden:false,required:false}},
				           {name:'Amount', index:'Amount', align:'center', width:90,editable:true,hidden:false, edittype:'text',editoptions:{size:20,readonly:true},editrules:{edithidden:false,required:false}}],
				           rowNum: 1000,	pgbuttons: false,	recordtext: '',	rowList: [],	pgtext: null,	viewrecords: true, altRows: true, altclass:'myAltRowClass',
						   sortname: 'Product No', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Vendor Invoice',
				height:200,	width: 800,
				scrollOffset: 0
			}).navGrid('#vendorinvoicepager',
				{add:true, edit:false,del:true,refresh:false,search:true}, //options
				//-----------------------edit options----------------------//
				{},
				//-----------------------add options----------------------//
				{},
				//-----------------------Delete options----------------------//
				{},
				//-----------------------search options----------------------//
				{});
			var mydata = [
				      		{ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'},
				      		{ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'},
				      		{ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'}
				      		/* {ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'},
				      		{ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'},
				      		{ProductNo:"*",Description:"EGC5*06*06*F22*NONE*00*00..",Qty:"10",CostEa:'50.000',Mult:'.2160',Amount:'$108.00'}, */
				      	 ];
		     	for(var i=0;i<=mydata.length;i++) {
					jQuery("#vendorinvoice1").jqGrid('addRowData', i+1, mydata[i]);
		     	}
		 });
	</script>
</body>
</html>