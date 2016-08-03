<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Inventory Categories</title>
		<style type="text/css">
			#search {display: none;}
			input[type='checkbox'] {
				margin-left: 0px;
				margin-right: 11px;
			}
			.accountRangeInput {width: 80px;}
			.accountRangeInputID {display: none;}
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
		</style>
	</head>
	<body>
		<div style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./../headermenu.jsp"></jsp:include>
			</div>
			<table style="width:979px;margin:0 auto;padding-bottom: 30px; padding-top: 50px;">
				<tr>
					<td style="padding-right: 20px;">
						<table>
							<!-- <tr>
								<td style="padding-left: 280px;">
									<table><tr><td><input type="button" value="  Add" class="add" id="addInvCat" onclick="showAddDialog()"></td></tr></table>
						    	</td>
						    </tr> -->
						    <tr>
						    	<td>
									<table id="inventoryCategoriesGrid"></table>
								</td>
						    </tr>
						</table>
					</td>
					<td style="vertical-align: top;">
						<div id="inventoryCategoriesDetails" style="margin-bottom: 15px;">
							<fieldset class= " ui-widget-content ui-corner-all">
								<legend><label><b>Category</b></label></legend>
								<form action="" id="categoryDetailsForm">
								<table>
									<tr>
										<td>
											<input type="text" id="invCategoryId" name="invCategoryId" style="display: none;">
										</td>
									</tr>
									<tr>
										<td><label>Description: </label><input type="text" id="categoryDescription" name="description"></td>
										<td><label>In Active: </label><input type="checkbox" value='1' id="categoryInactive" name="categoryInactive"></td>
									</tr>
									<tr>
										<td colspan="2"><label>Warehousing Cost Markup: </label><label id="markupAmount"></label></td>
									</tr>
									<tr>
										<td colspan="2">
											<label>Override: </label><input type="checkbox" value='1' id="overrideMarkup" name="overrideMarkup" onclick="overrideMarkupAmount()">
											<input type="text" id="overrideInput" name="overrideMarkupInput" style="" disabled="disabled">
										</td>
									</tr>
								</table>
								</form>
								<div>
									<input type="button" onclick="clearCategoryDetails()" value="Clear" class="savehoverbutton turbo-blue">
									<input type="button" onclick="deleteCategoryDetails()" value="Delete" class="savehoverbutton turbo-blue">
									<input type="button" onclick="SaveCategoryDetails()" value="Save" class="savehoverbutton turbo-blue" style="margin-left: 300px;width: 80px;">
								</div>
							</fieldset>
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div style="padding-top: 22px;">
			<table id="footer">
				<tr>
					<td colspan="2">
						<div class="footer-div"><jsp:include page="./../footer.jsp" /></div>
					</td>
				</tr>
			</table>
		</div>
		<script type="text/javascript" src="./../resources/scripts/turbo_scripts/inv_categories.js"></script>
	</body>
</html>