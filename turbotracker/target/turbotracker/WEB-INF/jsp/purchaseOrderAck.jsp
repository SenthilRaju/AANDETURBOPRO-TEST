<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div id="poacknowledgement">
	<table  style="width: 900px">
	<tr>
		<td>
			<fieldset class=" ui-widget-content ui-corner-all">
				<table>
					<tr>
						<td><label>Vendor: </label>
						<td><input type="text" id="vendorAckNameId" name="vendorAckName" style="width:200px" disabled="disabled" value="${manufactureName}"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>PO Date:</label></td>
						<td><input type="text" class="datepicker" id="poDateAckId" name="poDateAckName" style="width: 90px;"value="<fmt:formatDate pattern="MM/dd/yyyy" value="${aJobPurchaseOrderBean.orderDate}" />" disabled="disabled"></td>
						<td>&nbsp;&nbsp;</td>
						<td><label>Our PO #:</label></td>
						<td><input type="text" style="width: 90px" id="ourPoAckId" name="ourPoAckName" value="${aJobPurchaseOrderBean.ponumber}" disabled="disabled" disabled="disabled"></td>
					</tr>
				</table>
			</fieldset>
		</td>
	</tr>
</table>
	<table id="Ack"></table>
	<div id="Ackpager"></div>
	<br>
	<table style="width: 900px">
		<tr>
			<td align="left">
				<input type="button" id="saveAllButton" class="savehoverbutton turbo-tan" value="All" onclick="saveAll()" style="width: 80px;">
			</td>
			<td align="right">
				<a onclick="" style="font: bold 14px MyriadProRegular, trebuchet ms, sans-serif; cursor: pointer;"><label><b>
				<u>Apply To Rest of Order</u></b></label></a>
			</td>
		</tr>
	</table><br>
	<table style="width: 900px">
		<tr>
			<td>
				<fieldset class= " ui-widget-content ui-corner-all" style="height: 50px;">
				<legend class="custom_legend">
					<label><b>Totals</b></label>
				</legend>
					<table  style="width: 900px">
						<tr>
							<td width="70px"><label>Subtotal: </label></td><td style="right: 10px;position: relative;"><input type="text" style="width: 75px; text-align:right" id="subtotalKnowledgeId" name="subtotalKnowledgeName" disabled="disabled"></td>
							<td width="60px"><label>Freight: </label></td><td><input type="text" style="width: 75px; text-align:right" id="freightKnowledgeId" value="<fmt:formatNumber type="currency" pattern="$#,##0.00" value="${theVepo.freight}" />" name="freightKnowledgeName" disabled="disabled"></td>
							<td width="40px"><label>Tax: </label></td><td><input type="text" style="width: 60px; text-align: right;" id="taxKnowledgeId" name="taxKnowledgeName" disabled="disabled"></td>
							<td><label style="right: 10px;position: relative;">% &nbsp;</label></td><td><input type="text" id="KnowledgeID" name="KnowledgeName" style="width: 60px;text-align:right;" disabled="disabled"></td>
							<td width="50px"><label>Total: </label></td><td><input type="text" style="width: 75px; text-align:right" id="totalKnowledgeId" name="totalKnowledgeName" disabled="disabled"></td>
						</tr>
					</table>
				</fieldset>
			</td>
		</tr>
	</table>

<table  style="width: 900px">
	<tr>
		<td style="width: 60px;padding: 0px 1px; display: none">
	  		<fieldset class= " ui-widget-content ui-corner-all" style="padding-bottom: 0px; ">
		    	<table>
		    		<tr>
				  		<td align="right" style="padding-right: 7px;"><input type="image" src="./../resources/Icons/PDF_new.png" title="View Purchase Order" onclick="viewPOAckPDF()"  style="background: #EEDEBC; display: none;"></td> 	
				  		<td align="right" style="padding-right: 7px;"><input id="contactEmailID" type="image" src="./../resources/Icons/mail_new.png" title="Email Purchase Order" style="background: #EEDEBC"onclick="sendPOAckEmail()"></td>
		    		</tr>
		    	</table>
	   		</fieldset>
	  	</td>
		<td align="right" style="padding-left: 490px;">
			<input type="button" class="savehoverbutton turbo-tan" value="Save" onclick="cancelPORelease()" style="width:120px" id="cancelpoRelease">
		</td>
	</tr>
</table>
</div>

<div class="loadingDiv" id="loadingPOAckDiv"> </div>

<script type="text/javascript">
	var aAckVePoId;
	var freight = "${theVepo.freight}";
	
	jQuery(document).ready(function() {
		aAckVePoId = "${aVePoID}";
		loadAck();
	});
	
	function loadAck(){
		$("#Ack").jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			pager: jQuery('#Ackpager'),
			url:'./jobtabs5/jobReleaseAck',
			postData: {vePOID : aAckVePoId},
			colNames:['Product No','Description','Qty.','Ack.','Ship','Order #','VePOID','prMasterID','VePODetailID'],
			colModel:[
			          	{name:'note',index:'note',align:'left',width:90,editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false}},
			           	{name:'description', index:'description', align:'left', width:150, editable:true,hidden:false, edittype:'text', editoptions:{size:40},editrules:{edithidden:false},  
			          																													cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
						{name:'quantityOrdered', index:'quantityOrdered', align:'center', width:20,hidden:false, editable:true, editoptions:{size:5, alignText:'left'},editrules:{edithidden:true}},
						{name:'ackDate', index:'ackDate', align:'center', width:50,hidden:false, editable:true,  editoptions:{size:15, align:'center',},editrules:{edithidden:true}},
						{name:'shipDate', index:'shipDate', align:'center', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'center'}, editrules:{edithidden:true}},
						{name:'vendorOrderNumber', index:'vendorOrderNumber', align:'right', width:50,hidden:false, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:true}},
						{name:'vePoid', index:'vePoid', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
						{name:'prMasterId', index:'prMasterId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}},
						{name:'vePodetailId', index:'vePodetailId', align:'right', width:50,hidden:true, editable:true, editoptions:{size:15, alignText:'right'},editrules:{edithidden:false}}],
					rowNum: 0, pgbuttons: false, recordtext: '', rowList: [], pgtext: null, viewrecords: false,
					sortname: 'vePodetailId', sortorder: "asc", imgpath: 'themes/basic/images', caption: false,
					height:210,	width: 920, rownumbers:true, altRows: true, altclass:'myAltRowClass', caption: 'Acknowledgment',
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
				loadComplete: function(data) { 	},
				loadError : function (jqXHR, textStatus, errorThrown){	},
				onSelectRow: function(id){ },
				editurl:"./jobtabs5/manpulaterPOReleaseLineItem"
			}).navGrid('#Ackpager', {add: false, edit:true,del: false,refresh:true,search: false},
					//-----------------------edit options----------------------//
					{
						width:300, left:700, top: 300, zIndex:1040,
						closeAfterEdit:true, reloadAfterSubmit:true,
						modal:true, jqModel:true,
						editCaption: "Edit Acknowledgment",
						beforeShowForm: function (form) 
						{
							/*$(".navButton").empty();
							$(".navButton").append('<td style="padding-left: 60px;"><input type="button" id="saveAllButton" class="savehoverbutton turbo-blue" value="All" onclick="saveAll()"></td>');*/
							var grid = $('#Ack');
							var id = jQuery("#Ack").jqGrid('getGridParam','selrow');
							var shipDate = grid.jqGrid('getCell', id, 'shipDate');
							var ackDate = grid.jqGrid('getCell', id, 'ackDate');
							jQuery("#tr_note", form).hide();
							jQuery("#tr_description", form).hide();
							jQuery("#tr_quantityOrdered", form).hide();
							jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_ackDate .CaptionTD').append('Ack.: ');
							jQuery('#TblGrid_Ack #tr_ackDate .DataTD').empty();
							jQuery('#TblGrid_Ack #tr_ackDate .DataTD').append('&nbsp;<input type="text" size="15" align="center" id="ackDate" name="ackDatename" role="textbox">&nbsp;');
							jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_shipDate .CaptionTD').append('Ship: ');
							jQuery('#TblGrid_Ack #tr_shipDate .DataTD').empty();
							jQuery('#TblGrid_Ack #tr_shipDate .DataTD').append('&nbsp;<input type="text" size="15" aligntext="center" id="shipDate" name="shipDatename" role="textbox">&nbsp;');
							jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').empty();
							jQuery('#TblGrid_Ack #tr_vendorOrderNumber .CaptionTD').append('Order #: ');
							$("#ackDate").val(ackDate);
							$("#shipDate").val(shipDate);
							$( "#ackDate" ).datepicker({
							      showOn: "button",
							      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
							      buttonImageOnly: true
							    });
							$( "#shipDate" ).datepicker({
							      showOn: "button",
							      buttonImage: "./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/demos/datepicker/images/calendar.gif",
							      buttonImageOnly: true
							    });
						},
						onclickSubmit: function(params){
							var aAckDate =  $("#ackDate").val();
							var aShipDate =  $("#shipDate").val();
							return { 'operForAck' : 'acknowlegement', 'shipDatename' : aShipDate, 'ackDatename' : aAckDate };
						},
						afterSubmit:function(response,postData){
							 return [true, loadAck()];
						 }
					},
					{
						//-----------------------Add options----------------------//
							width:450, left:400, top: 300, zIndex:1040,
							closeAfterEdit:true, reloadAfterSubmit:true,
							modal:true, jqModel:true,
							editCaption: "Add Acknowledgment"
					}
			); 
	}
</script>
