<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Turbo Pro</title>		
	</head>
	<body style = "width: 1197px;">
		
		<table id="pagecenter">
			<tr>
				<td style="padding-left: 20px">
				<div id="tabs_main" style="width: 1170px;">
					<ul>
						<li><a href="#storyBuilderDiv">Stories</a></li>
						<li><a href="#tabs-1">Campaigns</a></li>
						<li><a href="#ChannelsTab">Channels</a></li>
						<li><a href="#CategoriesTab">Categories</a></li>
						<li><a href="#SettingsTab">Settings</a></li>
					</ul>
					<div id="tabs-1">
					</div>
					<div id="storyBuilderDiv">
					<form id="storyForm">
						<table style="height:35px; width:100%;">
							<tr style="padding: 0px; height: 35px;">
								<td style="padding: 0px; height: 35px; width: 5%">
									<label class="field_label">
										<b>Story: </b>&nbsp;
									</label>
								</td>
								<td style="padding: 0px; height: 35px; width: 20%">
									<select style="padding-left:0px;z-index:-24;height:32px;padding-top: 3px;width: 95%;" 
														class="ui-widget ui-state-default ui-corner-all" id="stories" onchange="changeStory();">
									</select>
								</td>
								<td align="left" width="120px">
									<input type="button" value = "&nbsp;&nbsp;&nbsp;Copy" style="background:url('Images/copy_story.png') no-repeat 3px 2px #DFEFFC;width:100px;height:31px;" id="copystoryButton">
								</td>
								<td align="left" width="120px">
									<input type="button" value = "&nbsp;&nbsp;&nbsp;Delete" onclick = "deleteStory();" style="background:url('Images/delete_story_trash.png') no-repeat 3px 2px #DFEFFC;width:100px;height:31px;">
								</td>
								<td style="width:20px;">
								</td>
								<td align="right">
									<input type="button" id="testOrSubmit" value = "&nbsp;&nbsp;&nbsp;Submit" onclick = "" style="background:url('Images/new_story_submit.png') no-repeat 3px 2px #DFEFFC;width:120px;height:31px;">
								</td>
								<td align="center" width="110px">
									<div id="users-contain" class="ui-widget" style="margin-bottom: 5px; margin-top: 5px;" align="right" >
										<input type="button" style="vertical-align: middle; background:url('Images/new_story.png') no-repeat 3px 2px #DFEFFC;width:100px;height:31px;" id="newStoryButton" value="&nbsp;&nbsp;New" >
									</div>
								</td>
							</tr>
						</table>
						</form>
						<div id="submitStoryFormDiv" title="Submiit Story">
						</div>
						<div id="submitCampaignFormDiv" title="Submiit Campaign">
						</div>
						<table>
							<tr>
								<td style="padding-left: 0px">
								<div id="tabs_sub" style="width: 1120px;">
									<ul>
										<li><a href="#tabs-3">Events</a></li>
										<li><a href="#tabs-4">Actors</a></li>
									</ul>
									<div id="tabs-3">
										<div id="DemoEventGrid">
											<table style="padding-left:0px" id="DemoEvents" class="scroll"></table>
											<div id="DemoEventspager" class="scroll" style="text-align:right;"></div>
										</div>
									</div>
									<div id="tabs-4">
										<div id="VehicleGrid">
											<table style="padding-left:0px" id="Vehicles" class="scroll"></table>
											<div id="VehiclePager" class="scroll" style="text-align:right;"></div>
										</div>
									</div>
								</div>
								</td>
							</tr>
						</table>
						<br/>
						<table>
							<tr>
								<td>
									<textarea name="testStoryOutput" id="source" cols="115" rows="15"></textarea>
									<div  id="testStoryOutputDiv"></div>
								</td>
							</tr>
						</table>
					</div>
					<div id="ChannelsTab">
						<div id="Channels">
							<table style="padding-left:0px" id="ChannelsGrid" class="scroll"></table>
							<div id="Channelspager" class="scroll" style="text-align:right;"></div>
						</div>
					</div>
					<div id="CategoriesTab">
						<div id="Categories">
							<table style="padding-left:0px" id="CategoriesGrid" class="scroll"></table>
							<div id="Categoriespager" class="scroll" style="text-align:right;"></div>
						</div>
					</div>
					<div id="SettingsTab">
					</div>
				</div>
				</td>
			</tr>
		</table>
		<br/>
		<div id="campaignStoryDialogDiv" title="Add New Campaign">
			<form id="campaignStoryForm">
				<table>
					<tr style="height:10px;"></tr>
					<tr>
						<td>
							<label for="campaignName" class="field_label">Campaign Name:</label><span style="color: red;">*</span>
						</td>
						<td>
							<input type="text" id="campaignName" class="validate[required] text ui-widget-content ui-corner-all" name="req" size="45"/>
						</td>
					</tr>
					<tr style="height:10px;"></tr>
					<tr>
						<td style="vertical-align: top;" >
							<label for="campaignDescription" class="field_label" style="padding-right:10px;">Campaign Description:</label>
						</td>
						<td>
							<textarea name="campaignDescription" id="campaignDescription" class="text ui-widget-content ui-corner-all" rows="4" cols="36"></textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
	
</html>