<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=100" >
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Turbopro - Home</title>
     <link href="./../resources/scripts/Nettuts-widget-source/inettuts.css" rel="stylesheet" type="text/css" />
     <style type="text/css">
     	/* Columns section */
			.widget { width: 650px; }
			#columns .column { width: 645px;}
			#header #menu{ margin:0 auto; padding-top:1px; }
			#columns #column1 .widget { margin: 20px 5px 0 0px;}
			#columns #columnLast .widget { margin: 20px 15px 0 5px; }
		/* Columns section */
			#columns .column {float: none;width: 645px;	/* Min-height: */min-height: 50px;height: auto !important;height: 50px;}
		/* Select the header Icon */
			#mainMenuHomePage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuHomePage a{background:url('./../resources/styles/turbo-css/images/turbo_app_home_hover_icon.png') no-repeat 0px 4px;color:#FFF;}
			.weatherWidget{height:215px; overflow-y: hidden; color: #FFF; padding-left: 10px;}
			.weatherWidget table tr td label{color: #FFF; font-family: Verdana, Arial, sans-serif; font-size: 12px;}
			.widgetBorders{border-radius : 5px;}
			.widgetHeader{background: -moz-linear-gradient(-90deg, #8C9BA9, #4A5F71); background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#8C9BA9), to(#4A5F71));}
			.ui-autocomplete {
			    max-height: 200px;
			    overflow-y: auto;
			}
     </style>
    
</head>
<body>
	<form id="homeFormId" name="fname" method="post" >
<div class="bodyDiv">
<div><jsp:include page="./headermenu.jsp"></jsp:include></div>
<table align="center" class="generalSearchTable">
	<tr align="left">
		<td class="generalSearchTd">
			<div id="columns" align="center" class="generalSearchColumns">
		        <ul id="column1" class="column">
		            <li class="widgetForFullNameLable widget widgetBorders" id="intro">
		                <div class="widget-head widgetHeader">
		                    <h3 class="widget-headForNameDiv">Welcome ${sessionScope.user.fullName}</h3>
		                </div>
		                <div class="widget-content contentForSearchBoxLable" align="left">
		                    <p class="contentForSearchBoxpara" style="padding: 5px;">Please use this widget to search for Jobs, Employees, Vendors and Customers.</p>
		                </div>
		                <div class="menu">
							<div class="ui-widget">
								<div class="demo">
									<table class="font" align="center">
										<tr><td></td><td style="height:20px;"><div id="errormsg" style="color:red;padding-left:20px;display:none" class="rolodexLabel" ><p>Please use this widget to search for Jobs, Em</p></div></td></tr>
										<tr>
											<td  colspan="1" align="right"><label><b class="rolodexLabel">Rolodex:</b></label></td>
											<td class="rolodexLabelTD">
												<div>
													<input  id="rolodex" name="rolodex" type="text" class="rolodexSearchInput" style="border-width: 3px;" onKeydown='Javascript: if (event.keyCode==13) getRolodex();'  placeholder="Minimum 3 characters required to get Rolodex List">
												</div>
											</td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getRolodex()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
										<tr height="20px;">
											<td></td>
										</tr>
										<!-- <tr>
											<td align="right"><label style="padding-left: 10px"> Type Minimum Three Letters in this Job search box </label></td>
										</tr>  -->
										<tr>
											<td align="right"><label><b class="rolodexLabel">Job:</b></label></td>
											<td class="jobSearchTD"><input type="text" class="rolodexSearchInput" style="border-width: 3px;" title="" onKeydown='Javascript: if (event.keyCode==13) getJobs();'id="jobsearch"  placeholder="Minimum 3 characters required to get Job List"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getJobs()" > </td> 
											<td><input type="button" value="Advanced" class="advancedSearchbutton" onclick="openAdvancedSearchPopup()"> </td>
										</tr>
										<tr>
											<td align="right"></td>
											<td>
												<label style="padding-left:75px;"><a href="./jobflow?token=new" class="alink">+ New Job</a> </label>
												<label class="newlinkLabel" style="display:none;"><a onclick="openAdvancedSearchPopup()" class="alink">+ Advanced Search</a> </label>
												<label style="padding-left:105px; font-family:Verdana,Arial,sans-serif; font-weight:bold;"><a style="color:#4A5F71;">+ Quick Book</a></label>
											</td>
										</tr>
										<tr>
											<td align="right"><label><b class="rolodexLabel">Inventory:</b></label></td>
											<td class="rolodexLabelTD"><input type="text" class="rolodexSearchInput" style="border-width: 3px;" onKeydown='Javascript: if (event.keyCode==13) search();'id="inventorysearch" placeholder="Minimum 3 characters required to get Inventory list"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getInventory()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
										<tr height  = "20px">
											<td></td>
										</tr>
										<tr>
											<td align="right"><label><b class="rolodexLabel">PO:</b></label></td>
											<td class="rolodexLabelTD"><input type="text" class="rolodexSearchInput"  onKeydown='Javascript: if (event.keyCode==13) search();' id="posearch" placeholder="Minimum 3 characters required to get Inventory list"></td>
											<td><input type="button" value="   Search" class="searchbutton" onclick="getPO()"> </td>
											<td><input type="button" value="Advanced" class="advancedSearchInactive" onclick=""> </td>
										</tr>
								</table>
								</div>
							</div>
						</div>
		            </li>
				</ul>
			</div>
		</td>
		<td class="rolodexSearchInput">
			<div>
				<table id="lastOpenedJobs"></table>
			</div>
		</td>
	</tr>
</table>
<div align="center" class="realseNotesDiv">
	<table class="home_down">
		<tr>
			<td style="width: 300px;">
				<h3 class="versionLableH3">Version 2.0.3:</h3>
				<div id="home_down_image" class="down_home" style="width: 320px;">
					<ul class="realseNotesUl">
					<li class="imglist" >1. Sales order not pulling correct details from release </li>
						<li class="hightimglist"></li>
						<li class="imglist" >1. Quote Template property implemented.</li>
						<li class="hightimglist"></li>
						<li class="imglist" >2. Quotes Template adding shows undefined message. </li>
						<li class="hightimglist"></li>
						<!--<li class="imglist" >3. Issues while fixing changing job status to be Bokked.</li>
						<li class="hightimglist"></li>	
						<li class="imglist" >4. Release letter without Salesrep Code.</li> -->
					</ul>
				</div>
			</td>
			<td>
				<div class="weatherWidget"></div>
			</td>
			<td>
			</td>
		</tr>
	</table>
</div>
</div>
		</form>
		<div class="divHeight"></div>
	</body>
	<div class="bodyDiv">
	<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<div style="display: none;">
		<table>
			<tr>
				<td>
					<input type="text" id="homeHiddenUseNameID" value="${sessionScope.user.userName}">
				</td>
				<td>
					<input type="text" id="homeHiddenUserID" value="${sessionScope.user.userId}">
				</td>
			</tr>
		</table>
	</div>
	<div id="weather"></div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/jquery.vticker.js"></script>
	 <script type="'text/javascript">
	 $(window).load(function(){ $('#scroller').vTicker(); });
     </script>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/home.js"></script> 
<!--  	  <script type="text/javascript" src="./../resources/scripts/turbo_scripts/minscripts/home.min.js"></script>  -->
</html>	