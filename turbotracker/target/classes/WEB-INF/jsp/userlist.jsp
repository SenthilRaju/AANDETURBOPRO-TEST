<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=100" >
 <meta http-equiv="refresh" content="900" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Turbopro - User List</title>
		<style type="text/css">
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#mainMenuToolsPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
            #mainMenuToolsPage a{background:url('./../resources/styles/turbo-css/images/user_32_white.png') no-repeat 0px 4px;color:#FFF}
		</style>
</head>
<body>
<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		<table style="width:600px;margin:0 auto;">
			<tr><td align="right">
			<table>
		    <tr>
		    	<td><input type="button" value="  Add" class="add" id="adduserlist" onclick="openAddNewUserDialog()" ></td>
		    	<td style="padding-left: 20px;"><input type="checkbox" id="activeUsersList" style="vertical-align: middle;" onchange="activeUsersList()" ><label style="vertical-align: middle;">Active</label></td>
		    </tr>
		    </table>
		    </td></tr>
		    <tr id="allUserListID"><td><table id="userlist"></table><div id="userGridpager"></div></td></tr>
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
<jsp:include page="addNewUser.jsp"></jsp:include>
	<script type="text/javascript">
	/** Document Ready Function **/
	jQuery(document).ready(function(){
		var aActiveUser = '';
		var aUrPage = "${requestScope.userDetails.activeUserList}";
		var aUserpage = "${requestScope.userDetails.activeEmployeeList}";
		if(aUrPage === '0'){
			$("#activeUsersList").prop("checked", true);
			aActiveUser = 0;
			loadUserList(aActiveUser);
			updateUserDetails(aActiveUser,aUserpage);
		}else{
			aActiveUser = 1;
		    loadUserList(aActiveUser);
		    updateUserDetails(aActiveUser,aUserpage);
		}
	});

	function activeUsersList(){
		var aActiveUser = 1;
		if ($('#activeUsersList').is(':checked')){
			aActiveUser = 0;
			loadUserList(aActiveUser);
			$('#userlist').jqGrid('setGridParam',{postData:{'userActiveID' : aActiveUser }});
			$("#userlist").trigger("reloadGrid");
			updateUserDetails(aActiveUser,aUserpage);
		}else if(aActiveUser === 1){
		    loadUserList(aActiveUser);
		    $('#userlist').jqGrid('setGridParam',{postData:{'userActiveID' : aActiveUser }});
			$("#userlist").trigger("reloadGrid");
			updateUserDetails(aActiveUser,aUserpage);
		}
	}
	
	/** Load User List **/
	function loadUserList(aActiveUser){
		var aUserPage = "${requestScope.userDetails.userperpage}";
		$("#userlist").jqGrid({
			datatype: 'JSON',
			mtype: 'POST',
			url:'./userlistcontroller/userlist',
			postData: { 'userActiveID' : aActiveUser },
			pager: jQuery('#userGridpager'),
			colNames:['Login Status','User Id', 'Login Name', 'Full Name', 'Initials', 'Email Address', 'Active', 'Admin'],
		   	colModel:[
				{name:'loginStatus',index:'loginStatus',align:'center', width:30,editable:false, hidden:true, formatter:loginStatusFormatter, editrules:{required:false}, editoptions:{size:10}},
				{name:'userLoginId',index:'userLoginId', width:50,editable:true, hidden: true, editrules:{required:true}, editoptions:{size:10}},
				{name:'loginName',index:'loginName', width:170,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
				{name:'fullName',index:'fullName',align:'left', width:250,editable:true, editrules:{required:true}, editoptions:{size:10},formatter : nameFormatter},
				{name:'initials',index:'initials',  align:'center', width:50,editable:true, editrules:{required:true}, editoptions:{size:10}},
				{name:'emailAddr',index:'emailAddr',align:'left', width:200,editable:true, editrules:{required:true}, cellattr: function (rowId, tv, rawObject, cm, rdata) {return 'style="white-space: normal" ';}, editoptions:{size:10}},
				{name:'inactive',index:'inactive', align:'center', width:30,editable:true, editrules:{required:true}, formatter : activeFormatter, editoptions:{size:10}},
				{name:'systemAdministrator',index:'systemAdministrator', align:'center', hidden: true, width:20,editable:true, editrules:{}, editoptions:{size:10}}],
		   	rowNum: aUserPage,
			pgbuttons: true,
			altRows: true,
			altclass:'myAltRowClass',	
			recordtext: '',
			rowList: [ 25, 50, 75, 100, 125, 150],	viewrecords: true,
			pager: '#userGridpager',
			sortname: 'employeeId', sortorder: "asc",	imgpath: 'themes/basic/images',	caption: 'Users',
			height:550,	width: 1150,/*scrollOffset:0,*/ rownumbers:true,
			loadComplete: function(data) {
				$(".ui-pg-selbox").attr("selected", aUserPage);
			},
			loadError : function (jqXHR, textStatus, errorThrown) {
				var errorText = $(jqXHR.responseText).find('u').html();
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:RED;">Error: ' + errorText + '</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() {$(this).dialog("close");}}]}).dialog("open");
				return false;
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
	    	ondblClickRow: function(rowid) {
	    		var rowData = jQuery(this).getRowData(rowid); 
	    		var aUserLoginID = "${requestScope.userSysAdmin}";
	    		if(aUserLoginID !== '1'){
	    			var aInfo = true;
	    			if(aInfo){
	    				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
	    				var newDialogDiv = jQuery(document.createElement('div'));
	    				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
	    				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
	    										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	    				return true;
	    			}
		    	}else{
					var userID = rowData['userLoginId'];
					var loginName = rowData['loginName'];
					createtpusage('Company-Users','Users Grid View','Info','Company-Users,Viewing User Info,userID:'+userID+',loginName:'+loginName);
					document.location.href = "./userDetails?userLoginId="+userID+"&loginName="+loginName;
		    	}
		    	return true;
			}
		}).navGrid('#userGridpager',
				{add:false,edit:false,del:false,refresh:false,search:false});
	}
	function loginStatusFormatter(cellValue, options, rowObject){
		var loginStatus=rowObject['loginStatus'];
		var id="userLoginStatus_"+options.rowId;
		var element = '';
		console.log(cellValue);
		if(cellValue==1)
			{
		element = "<input type='radio' id='"+id+"' checked>";
			}
		else
			{
			element = "<input type='radio' id='"+id+"'>";
			}
	   
	return element;
	}
	function openAddNewUserDialog(){
		var aUserLoginID = "${requestScope.userSysAdmin}";
		if(aUserLoginID !== '1'){
			var aInfo = true;
			if(aInfo){
				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
				var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return true;
			}
    	}else{
    		document.getElementById("userFormId").reset();
    		jQuery("#addNewUserDialog").dialog("open");	
    	}
    	return true;
	}

	/** Convert Integer to String For 'Active' and 'InActive' in User List Gird **/
	function activeFormatter(value, options){
		if(value === 0){
			return "Yes";
		}else{
			return "No";
		}
	}
	function nameFormatter(value, options){
		/* var re = new RegExp('@@', 'g');
		value = value.replaceAll(,' '); */
		return value ;
	}
	
	function updateUserDetails(aActiveUser,aUserPage){
		$.ajax({
			url: "userlistcontroller/updateUserList",
			type: "POST",
			async:false,
			data :"&activeUserList=" +aActiveUser+"&activeEmployeeList=" +aUserPage,
			success: function(data) {
			}
		});
	}

	/** Add User Dialog Function **/
	jQuery( function(){
		jQuery("#addNewUserDialog").dialog({
			autoOpen:false,
			height:300,
			width:730,
			title:"Add New User",
			modal:true,
			close:function(){
				$('#userFormId').validationEngine('hideAll');
				return true;
			}
		});
	});

	/** User Search List **/
	$(function() { var cache = {}; var lastXhr='';
	$( "#searchJob" ).autocomplete({ minLength: 3,timeout :1000,
		select: function (event, ui) {
			var aUserLoginID = "${requestScope.userSysAdmin}";
			if(aUserLoginID !== '1'){
    			var aInfo = true;
    			if(aInfo){
    				var information = "You are not authorized to enter this area.  Please contact your System Administrator.";
    				var newDialogDiv = jQuery(document.createElement('div'));
    				jQuery(newDialogDiv).html('<span><b style="color:green;">'+information+'</b></span>');
    				jQuery(newDialogDiv).dialog({modal: true, width:340, height:170, title:"Information", 
    										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
    				return true;
    			}
	    	}else{
				var userID = ui.item.id;
				var name = ui.item.value;
				var splitName = name.split("[");
				var loginName = splitName[0];
				document.location.href = "./userDetails?userLoginId="+userID+"&loginName="+loginName;
			}
		},
		open: function(){ 
			$(".ui-autocomplete").prepend('<div style="font-size: 15px;"><b><a href="./userlist?oper=add" style="color:#3E8DC6;font-family: Verdana,Arial,sans-serif;font-size: 0.8em;">+ Add New User</a></b></div>');
			$('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
			 },
		source: function( request, response ) { var term = request.term;
			if ( term in cache ) { response( cache[ term ] ); 	return; 	}
			lastXhr = $.getJSON( "search/searchUserList", request, function( data, status, xhr ) { cache[ term ] = data; 
				if ( xhr === lastXhr ) { response( data ); 	} });
		},
		error: function (result) {
		     $('.ui-autocomplete-loading').removeClass("ui-autocomplete-loading");
		}  }); });
</script>
</body>
</html>