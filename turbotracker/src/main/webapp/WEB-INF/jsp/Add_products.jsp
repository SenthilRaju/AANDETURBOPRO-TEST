<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div id="addproducts" align="left">
		<table>
		<tr>
		<td style="padding-left: 0px;vertical-align: top;"> 
				<table>
				<tr><td><label><b>Product/Services</b></label></td></tr>
				<tr><td></td></tr>
				<tr><td><label>Code</label></td><td><input type="text" style="width:95px"></td><td><label>Active</label></td><td><input type="checkbox"></td></tr>
				<tr><td ><label>Description</label></td><td ><input type="text" style="width:95px"></td><td><label>Inventory</label></td><td><input type="checkbox"></tr>
				<tr><td><label>Department</label></td><td colspan="1"><select style="width: 95px;"><option></option></select></td><td><label>Consignment</label></td><td><input type="checkbox"></td></tr>
				<tr><td><label>Category</label></td><td><select style="width: 95px;"><option></option></select></td>
				<td><label>Box Qty:</label></td><td><input type="text" style="width:95px"></td></tr>
				<tr><td><label>Bin # per Whse:</label></td><td><select><option>Forest Park</option></select></td><td><label>Weight:</label></td><td><input type="text" style="width:95px"></td></tr>
				</table>
				
				<table>
				<tr><td><label>Printed on forms:</label></td><td></td>
				<td><label>PO</label></td><td ><input type="checkbox"></td>
				<td ><label>SO</label></td><td ><input type="checkbox"></td>
			    <td><label>Invoice</label></td><td><input type="checkbox"></td>
			    <td><label>Pick Ticket</label></td><td><input type="checkbox"></td></tr>
			    
				</table>		
				</td>
				
						
				 <td style="padding-left: 0px;vertical-align: top;">
				      <table>
						<tr>
						<td><label><b>Inventory</b></label>
						<br>
						
						<div>
					   <table id="inventory" style="width:50px"></table><div id="inventorypager"></div>
				       </div></td></tr></table>
				       
				       </td></tr>
				       
		<tr><td style="padding-left: 0px;vertical-align: top;">
				<table width="60%">
				<tr><td><label><b>Sales</b></label></td></tr>
				<tr><td><label>Base Selling Price:</label></td><td><input type="text" style="width:95px"></td></tr>
				<tr><td><label>Taxable?</label></td><td><input type="checkbox"></td></tr>
				<tr><td><label>Single Item Tax?</label></td><td><input type="checkbox"></td></tr>
				<tr><td><label>Profit Margin:</label></td></tr>
				<tr><td><label>Pct:</label></td></tr>
				  
				</table></td>
				
		<td style="padding-left: 0px;vertical-align: top;">
		          <table>
		          <tr><td><label><b>Purchasing</b></label></td></tr>
		          <tr><td><label>Primary Vendor:</label></td><td><select style="width:100px"><option></option></select></td></tr>
		          <tr><td><label>Vendor Item code</label></td><td><input type="text" style="width:100px"></td></tr>
		          <tr><td><input type="button" value="Set Average Cost" style="width:150px"></td><td><input type="button" value="Secondary Vendor" style="width:150px"></td></tr>
		          </table>
		          <table>
		          <tr><td><label>Avg.Cost:</label></td><td>$0.000</td>
		              <td><label>Whse Cost:</label></td><td>$0.000</td></tr>
		          <tr><td><label>Multiplier:</label></td><td><input type="text" style="width:120px"></td></tr>
		          <tr><td><label>Factory Cost:</label><td><input type="text" style="width:120px"></td></tr>
		          </table></td></tr>
		    </table>
		
		          
		          
	
				<table width="80%">
				<tr><td><label><b>Product Attributes</b></label></td></tr>
				<tr><td><label>Labour?</label>&nbsp;<input type="checkbox"></td>
				    <td><label>Auto Build sub Assemblies?</label>&nbsp;<input type="checkbox"></td>
				    <td><label>Serialized Inventory?</label>&nbsp;<input type="checkbox"></td></tr>				    
				</table>
				<br>
		
		<table width="100%">
		<tr><td><label><b><center>Message when selected on sales Order</center></b></label></td>&nbsp;
		<td><label><b><center>Message when Selected on Invoice</center></b></label></td>&nbsp;
		<td><label><b><center>Message when Selected on Purchase Order</center></b></label></td></tr>
		<tr><td><center><textarea rows="2" cols="20"></textarea></center></td>
		<td><center><textarea rows="2" cols="20"></textarea></center></td>
		<td><center><textarea rows="2" cols="20"></textarea></center></td></tr>
		</table>
		
				  
			  <table width="" > 
			  <tr><td><label><b>Tier Pricing</b></label></td></tr> 
			  <tr><td><label>Quantity Breaks:</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<label>Over</label>&nbsp;&nbsp;<input type="text" style="width:95px"></td>
			  <tr><td colspan="2"><hr width="800px"></td></tr> 
			  </table>
			  
		 <table>
		 <tr><td>
		      <table >
		      <tr><td><label>Retail</label></td><td><input type="text"></td></tr>
		      <tr><td><label>Dealer</label></td><td><input type="text"></td></tr>
		      <tr><td><label>Distributer</label></td><td><input type="text"></td></tr>
		      
		      </table>
		   </td>
		   <td style="vertical-align:top">
		     <table>
		      <tr><td><label>Wholesale</label></td><td><input type="text"></td></tr>
		      <tr><td><label>Special 1</label></td><td><input type="text"></td></tr>
		      <tr><td><label>Special 2</label></td><td><input type="text"></td></tr>
		     </table>
		   </td>
		   </tr>
		   </table>
	       </div>
	    
	    
     
		<script type="text/javascript" src="./../resources/web-plugins/jquery/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.16.custom-flick/development-bundle/ui/jquery-ui-1.8.16.custom.js"></script>
		<script type="text/javascript">
			jQuery(function () {
					jQuery( "#addproducts" ).dialog({
						autoOpen: false,
						height: 800,
						width: 850,
						title:"Add Products",
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
					
				
			});
			
			function openproductsDialog() {
				jQuery( "#addproducts" ).dialog( "open" );
			}
						
			
			jQuery(document).ready(function(){
				
				$("#inventory").jqGrid({
					
					colNames: ['Item','Quantity','$Values'],
					colModel :[
                        {name:'Item', index:'Item', align:'center', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:20,readonly:true},editrules:{edithidden:false,required:false}},
			           	{name:'Quantity', index:'Quantity', align:'center', width:30, editable:true,hidden:false, edittype:'text', editoptions:{size:20,readonly:true},editrules:{edithidden:false,required:false}},
						{name:'Values', index:'Values', align:'center', width:30,editable:true,hidden:false, edittype:'text',editoptions:{size:20,readonly:true},editrules:{edithidden:false,required:false}}],
						
					height:138,	width: 350,
					scrollOffset: 0,
					loadComplete: function(data) {
				 		
				    },
					loadError : function (jqXHR, textStatus, errorThrown){
					    
					},
					onSelectRow: function(id){
						if(id && id!==lastsel){
							jQuery('#quotesBidlist').jqGrid('restoreRow',lastsel);
							jQuery('#quotesBidlist').jqGrid('editRow',id,true);
							lastsel=id;
						}
					},
					//editurl:'/employeelistcontroller?type=manipulate'
					
				}).navGrid('#generalpager',
					{add:true, edit:false,del:true,refresh:false,search:true}, //options
					//-----------------------edit options----------------------//
					{},
					//-----------------------add options----------------------//
					{},
					//-----------------------Delete options----------------------//
					{},
					//-----------------------search options----------------------//
					{}
			
				);
				
				var mydata = [
					      		{Item:"On Hand", Quantity:"0", Values:"$0.00"},
					      		{Item:"Allocated", Quantity:"0", Values:"$0.00"},
					      		{Item:"Available", Quantity:"0", Values:"$0.00"},
					      		{Item:"On Order", Quantity:"0", Values:"$0.00"},
					      		{Item:"Submitted", Quantity:"0", Values:"$0.00"},
					      		{Item:"YTD Sold", Quantity:"0", Values:"$0.00"},
					      		
					      	 ];
			     	for(var i=0;i<=mydata.length;i++) {
						jQuery("#inventory").jqGrid('addRowData', i+1, mydata[i]);
			     	}  
			});
		</script>
</body>
</html>