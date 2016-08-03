<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="vendortab">
	<table>
		<tr>
			<td>
				<table>
				<tr><td style="height:15px"></td></tr>
					<tr>
						<td style="padding-left: 0px; vertical-align: top; ">
						<table>
						<tr><td>
							<fieldset style="width:280px;height:50px" class=" ui-widget-content ui-corner-all">
								<legend><label><b>Vendor Account # </b></label></legend>
								<table>
									<tr><td><input type="text" style="width: 240px;" value="${requestScope.veMasterRecord.accountNumber}"></td></tr>
								</table>
							</fieldset>
						</td>
						</tr>
						<tr>
						<td>
							<fieldset style="width:280px;height:107px" class= " ui-widget-content ui-corner-all" >
								<legend><label><b>Payment Terms</b></label></legend>
									<table width="280px">
										<tr>
											<td><label>Payment due: Net</label>
												<input type="text" size="3" value="${requestScope.veMasterRecord.dueDays}" />&nbsp;
												<select>
													<option>days</option>
													<option>month</option>
												</select>
											</td>
										</tr>
										<tr>
											<td><label>Discount</label>
												<input type="text" size="3" value="${requestScope.veMasterRecord.discountPercent}">% 
												<input type="text" size="3" value="${requestScope.veMasterRecord.discountDays}" />&nbsp;
												<select>
													<option>days</option>
													<option>month</option>
												</select>
											</td>
										</tr>
										<tr>
											<td>
												<c:set var="sel" value=""></c:set>
												<c:if test="${requestScope.veMasterRecord.discountIncludesFreight}">
												   	<c:set var="sel" value="checked"></c:set>
												</c:if>
											<input type="checkbox" <c:out value="${sel}"/> style="vertical-align: middle;" ><label style="vertical-align: middle;">Discount Includes Freight</label></td>
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
						</table>
						</td>
						<td style="padding-left: 0px; vertical-align: top;">
						<table>
						<tr><td>
							<fieldset style="width:225px;;height: 177px;" class= " ui-widget-content ui-corner-all" >
								<legend><label><b>Vendor Overall A/P</b></label></legend>
								<table width="220px">
									<tr><td></td></tr>
									<tr>
										<td><label>Current Due:</label></td>
										<td><label>$0.00</label></td>
									</tr>
									<tr>
										<td><label>30 Days:</label></td>
										<td><label>$0.00</label></td>
									</tr>
									<tr>
										<td><label>60 Days:</label></td>
										<td><label>$0.00</label></td>
									</tr>
									<tr>
										<td><label>90 Days:</label></td>
										<td><label>$0.00</label></td>
									</tr>
									<tr>
										<td><label>Total Due:</label></td>
										<td><label>$0.00</label></td>
									</tr>
									<tr>
										<td><label>Avg Days to Pay Invoice:</label></td>
										<td><label>0</label></td>
									</tr>
								</table>
							</fieldset>
						</td>
						</tr>
						</table>
						</td>
					<td>
						<table>
							<tr>
								<td>
									<fieldset style="width:230px;height:45px" class= " ui-widget-content ui-corner-all" >
									<legend><label><b>Default Expense Account</b></label></legend>
										<table>
											<tr><td><input type="text" id="financeAccountID" name="financialAccountName" placeholder="Enter minimum 2 character to search" style="width: 122%;"></td>
											<td><input type="hidden" id="financeAccounthiddenID" name="financeAccounthidden"></td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset style="width:230px;height:45px" class= " ui-widget-content ui-corner-all" >
										<legend><label><b>Factory Software</b></label></legend>
										<table>
											<tr>
												<td>
													<select id="factorySoftware" style="width: 200px;">
														<option value="0">(none)</option>
														<option value="1">Penn</option>
														<option value="2">Nailor</option>
														<option value="3">Price</option>
														<option value="4">Pottorff</option>
														<option value="5">Metal Air</option>
														<option value="6">Greenheck</option>
														<option value="7">Perfect Air</option>
													</select>
												</td>
											</tr>
										</table>
									</fieldset>
								</td>
							</tr>
							<tr>
								<td>
									<fieldset style="width:230px;height:53px" class= " ui-widget-content ui-corner-all" >
									<legend><label><b>Alternate Manufacture</b></label></legend>
										<table width="230px;">
											<tr><td><input type="checkbox" style="vertical-align: middle;"><label style="vertical-align: middle;"> Force Alternate Manufacture?</label></td></tr>
										</table>
									</fieldset>
								</td>
							</tr>
						</table>
					</td>
			<td>
				<div>
					<table id="vendorGrid" style="width: 20px"></table>
					<div id="vendorpager1"></div>
				</div>
			</td>
		</tr>
	</table>
	</td>
	</tr>
	<tr height="340px;">
	</tr>
	</table>

</div>
	<script type="text/javascript">
		jQuery(document).ready(function() {
			var customerName = $('#customerName').text();
			$("#customerNameHeader").val($.trim(customerName));
			vendorGrid();
		});

		function parseModelMap(){
			var discountIncludesFreight = "${requestScope.veMasterRecord.discountIncludesFreight}";
			
		}
		function vendorGrid() {
			var rolodexNumber = getUrlVars()["rolodexNumber"];
			jQuery("#vendorGrid").jqGrid({
				datatype: 'JSON',
				url: 'rolodexforms/manufacturers',
				mtype: 'POST',
				postData : {'rxMasterId' : rolodexNumber },
				pager: jQuery('#vendorpager1'),
				colNames: [ 'Description', 'InActive', 'FactoryID' ],
				colModel: 
					[{name: 'description', index: 'description', width: 25, editable: true, hidden: false, edittype: 'text', editoptions: {size : 30},cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';},	editrules: {edithidden: false,required: false}},
					 {name: 'inActive', index: 'inActive', align: 'center', width: 8, editable:true, formatter:'checkbox', edittype:'checkbox' },
					 {name: 'veFactoryID', index: 'veFactoryID', align: 'center', width: 8, hidden: true, editable: true}],
				recordtext: '',
				rowList: [],
				rowNum: 0,
				pgtext: null,
				viewrecords: true,
				sortname: 'Product No',
				sortorder: "asc",
				imgpath: 'themes/basic/images',
				caption: 'Manufacturers',
				height: 120,
				width: 280,
				altRows: true,
				pgbuttons: false,
				altclass:'myAltRowClass',
				rownumbers: false,
				loadComplete: function(data) {	},
				loadError: function(jqXHR, textStatus, errorThrown) {	},
				onSelectRow: function(id) {
					
				},
				ondblClickRow: function(rowid) {
				    jQuery(this).jqGrid('editGridRow', rowid,
				                        {recreateForm:true,closeAfterEdit:true,
				                         closeOnEscape:true,reloadAfterSubmit:false,editCaption: "Edit Manufacture",	width: 390, top: 256, left: 450});
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
		    	editurl:"rolodexforms/updateManufacturers"
		}).navGrid("#vendorpager1",
				{add: false,del: false,search: false,edit: true,pager:false},
				//-----------------------edit options----------------------//
				{
					closeAfterEdit:true, reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
					modal:true,jqModal:true,viewPagerButtons: false,editCaption: "Edit Manufacture",	width: 390, top: 256, left: 450,
					beforeShowForm: function (form) 
					{
						jQuery("#tr_veFactoryID", form).hide();
					}
				},
				{
					closeAfterAdd:true,	reloadAfterSubmit:true, reloadGridAfterSubmit: true, closeOnEscape: true,
					modal:true,
					jqModal:true,
					viewPagerButtons: false,
					addCaption: "Add Manufacture",
					width: 390, top: 356, left: 207
				}
			);
		}
		$(function() { var cache = {}; var lastXhr='';
		$( "#financeAccountID" ).autocomplete({ minLength: 2,timeout :1000, clearcache : true,
		select: function( event, ui ) {console.log(ui.item); var id = ui.item.id; var number=ui.item.label;$("#financeAccounthiddenID").val(id); $("#financeAccountID").val(ui.item.label); },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "inventoryList/AccountListAuto", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			} 
			});
		});
	</script>