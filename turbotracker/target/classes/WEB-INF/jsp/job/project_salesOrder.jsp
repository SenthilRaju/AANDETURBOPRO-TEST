<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Turbopro - Jobs</title>
		<style type="text/css">
			#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
			#mainMenuJobsPage a span{color:#FFF}
		</style> 
		<style type="text/css">
			.textSpecial{ vertical-align: top; }
			#ui-datepicker-div{border: 1px solid #FF864E; }
			.ui-autocomplete { border:1px solid #999; background:#EEEEEE; cursor:default; height: auto; text-align:left; max-height:150px; overflow-y: scroll; overflow:auto; margin:-6px 6px 6px -6px; _height:145px;  _margin:0; _overflow-x:hidden; width: 100px; }
			.ui-tabs-nav {height: 32px;}
				#mainMenuJobsPage {text-decoration:none;color:#FFFFFF;background-color: #54A4DE;}
				#mainMenuJobsPage a span{color:#FFF}
		</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include> 
			</div>
			<table style="width:979px;margin:0 auto;">
				<tr>
					<td colspan="2">
						<div id="salesrelease" class="jobListWizardDivColor">
							<div id="salesreleasetab" style="width: 800px;">
								<ul>
									<li id="salesgeneral"><a href="SO_General">General</a></li>
									<li id="saleslineitems"><a href="SO_Lines">Line Items</a></li>
									<li id="salesacknowledgement"><a href="SO_Kit">Kit</a></li>
									
									<li style="float: right; display: none;">
							           <label id="jobNumber_ID"></label>
										<label id="jobName_ID"></label>
										<label id="jobCustomerName_ID"></label>
										<label id="Subtotal_ID"></label>
										<label id="Freight_ID"></label>
										<label id="cuInvoiceID"></label>
										<label id="Percentage_ID"></label>
										<label id="Total_ID"></label>
										<label id="prWareHouse_ID"></label>
										<label id="poDate_ID"></label>
										<label id="order_ID"></label>
										<label id="Jomasterid"></label>
										<label id="vePO_ID"></label>
										<label id = "Cuso_ID"></label>
										<label id ="rxmasterId"></label>
							          	</li>
							          	<li id="salesacknowledgement" style="position: absolute;right: 1%;top: 1%; background:white;"><input type="button" id="soStatusButton" value="Open" style="width: 90px;height: 25px;" onClick="callSalesOrderStatus();" /></li>
								</ul>
							</div>
							<input type="hidden" id="operation" name="operation"/>
						  <jsp:include page="./../Email_Attachment.jsp"></jsp:include>
						</div>
					</td>
				</tr>
			</table>
			<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
			</div>
		</div>
		<input type="hidden" id="project_acuSoid" value="${requestScope.project_acuSoid}"/>
		<input type="hidden" id="project_aMasterID" value="${requestScope.project_aMasterID}"/>
        <script type="text/javascript" src="./../resources/scripts/turbo_scripts/project_salesOrder.js"></script>
<!-- 		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/SalesRelease.js"></script> -->
	</body>
	
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/nicEdit.js"></script>
	<script type="text/javascript">
		bkLib.onDomLoaded(function() { 
			//nicEditors.allTextAreas(); 
			var myNicEditor = new nicEditor();
			myNicEditor.panelInstance(
		            document.getElementById('econt')
		        );
			/* myNicEditor.panelInstance(
		            document.getElementById('specialInstructionID')
		        ); */
			 });
	</script>
	<script type="text/javascript" src="./../resources/web-plugins/jqueryui/jquery-ui-1.8.23.custom/development-bundle/ui/jquery-ui-1.8.23.custom.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="./../resources/web-plugins/jquery.jqGrid-4.4.0/js/jquery.jqGrid.min.js" ></script>
</html>
