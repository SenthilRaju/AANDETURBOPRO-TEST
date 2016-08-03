var newDialogDiv = jQuery(document.createElement('div'));
jQuery(document).ready(function() {
	$("#search").hide();
		loadShipviaGrid();
		$(".charts_tabs_main").tabs({
			cache: true,
			ajaxOptions: {
				data: {  },
				error: function(xhr, status, index, anchor) {
					$(anchor.hash).html("<div align='center' style='height: 386px;padding-top: 200px;'>"+
							"<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."+
							"</label></div>");
				}
			},
			load: function (e, ui) {$(ui.panel).find(".tab-loading").remove();},
			select: function (e, ui) {
				//window.location.hash = ui.tab.hash;
				var $panel = $(ui.panel);
				if ($panel.is(":empty")) {
					$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"+
							"<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
				}
			}
		});
		document.getElementById("chartsDetailsFromID").reset();
	});
	function loadShipviaGrid(){
		$("#shipViaListGrid").jqGrid({
			datatype: 'json',
			mtype: 'POST',
			url:'shipviaController/getShipDetails',
		   	colNames:['veShipViaId','Description','InActive', 'Track URL', 'Track Prefix', 'TrackSuffix'], 
		   	colModel:[{name:'veShipViaId',index:'veShipViaId', width:150,editable:true,hidden: true},
		  		   	{name:'description',index:'description', width:150,editable:true },
		  		 	{name:'inActive',index:'inActive', width:150,editable:true,hidden: true},
		   	       {name:'trackUrl',index:'trackUrl', width:350, editable:true,  cellattr: function (rowId, tv, rawObject, cm, rdata){return 'style="white-space: normal" ';}},
		   	       {name:'trackPrefix',index:'trackPrefix', width:350,editable:true, hidden: true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}},
		   	       {name:'trackSuffix',index:'trackSuffix', width:350,editable:true, hidden: true, cellattr: function (rowId, tv, rawObject, cm, rdata)	 {return 'style="white-space: normal" ';}}],
			rowNum: 500,	
			pgbuttons: false,	
			recordtext: '',
			sortorder: "asc",
			sortname: 'Description',
			altRows: true,
			altclass:'myAltRowClass',
			imgpath: 'themes/basic/images',
			caption: 'Ship Via',
			height:320,	width: 500,/*scrollOffset:0,*/rownumbers:true,rownumWidth:34,
			loadComplete:function(data) {
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
	    	onSelectRow: function(rowId){
	    		shipViaDetails(rowId);
	    	},
	    	ondblClickRow: function(rowId) {
	    		shipViaDetails(rowId);
			}
		});
    	$("#shipViaListGrid td").css('white-space','normal');
		return true;
	}
	
	function shipViaDetails(rowId){
		var rowData = jQuery("#shipViaListGrid").getRowData(rowId); 
		var veshipviaid = rowData['veShipViaId'];
		var description = rowData['description'];
		var inActive = rowData['inActive'];
		var trackUrl = rowData['trackUrl'];
		var trackPrefix = rowData['trackPrefix'];
		var trackSuffix = rowData['trackSuffix'];
		$("#shipViaID").val(veshipviaid);
		$("#DescriptionID").val(description);
		$("#trackingUrlID").val(trackUrl);
		$("#trackingPrefixID").val(trackPrefix);
		$("#trackingSuffixID").val(trackSuffix);
		return true;
	 }
	
	function saveshipviaDetails(){
		var shipviaDetails = $("#shipViaFormID").serialize();
		console.log("I am here ship via")
		$.ajax({
			url: "shipviaController/saveShipViaDetails",
			type: "POST",
			data : shipviaDetails,
			success: function(data){
				createtpusage('Company-Settings','Save Shipvia Details','Info','Company-Settings,Saving Shipvia Details,description:'+ $("#DescriptionID").val());
				if(data.oper === 'add'){
					$('#messsagetouser').text('Added');
					setTimeout(function() {
						removetext();
		                }, 2000);
					$("#shipViaListGrid").trigger("reloadGrid");
//					var errorText = "shipvia detail is added successfully";
//					jQuery(newDialogDiv).attr("id","msgDlg");
//					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
//					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
//						buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); 
//						
//						//$("#shipViaListGrid").trigger('reloadGrid');
//						//window.location.reload();
//						
//						}}]}).dialog("open");
					return true;
				}else if(data.oper === 'edit'){
					$('#messsagetouser').text('Updated');
					setTimeout(function() {
						removetext();
		                }, 2000);
//					var errorText = "shipvia detail is updated successfully";
//					$("#shipViaListGrid").trigger("reloadGrid");
//					jQuery(newDialogDiv).attr("id","msgDlg");
//					jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
//					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
//						buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close"); 
//						//window.location.reload();
//						//$("#shipViaListGrid").trigger('reloadGrid');
//						}}]}).dialog("open");
					return true;
				}
			}
	   	});
		removetext();
		
	 }
	function removetext(){
		document.getElementById("shipViaFormID").reset();
		$('#messsagetouser').text('');
	}
	function deleteshipviaDetails(){
		var grid = $("#shipViaListGrid");
		var rowId = grid.jqGrid('getGridParam', 'selrow');
		if(rowId !== null){
			jQuery(newDialogDiv).html('<span><b style="color: red;">Delete the Account Record?</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Confirm Delete", 
				buttons:{
					"Submit": function(){
						var aveShipViaId = grid.jqGrid('getCell', rowId, 'veShipViaId');
						createtpusage('Company-Settings','Delete Shipvia Details','Info','Company-Settings,Deleting Shipvia Details,description:'+ $("#DescriptionID").val());
						deleteshipvia(aveShipViaId); 
						jQuery(this).dialog("close");
						$("#shipViaListGrid").trigger("reloadGrid");
						},
					Cancel: function(){jQuery(this).dialog("close");} }}).dialog("open");
						return true;
						}
				else{
					var errorText = "Please click one of the shipvia detail to Delete";
					jQuery(newDialogDiv).attr("id","msgDlg");
					jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
					jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
						buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close");  window.location.reload(); }}]}).dialog("open");
					return false;
					}	
    	   }
	
	function deleteshipvia(aveShipViaId){
		$.ajax({
			url: "shipviaController/deleteveshipdetail",
			type: "POST",
			data : {"veShipViaId": aveShipViaId},
			success: function(data) {
				
				$('#messsagetouser').text("Record Deleted successfully");
				setTimeout(function() {
					removetext();
	                }, 2000);
				
				
				/*var errorText = "Shipvia detail is Delete successfully";
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:green;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning",
					buttons: [{height:30,text: "OK",click: function() { $(this).dialog("close");  window.location.reload(); }}]}).dialog("open");
				return true;*/
			}
		});
	}
			
	function addShipViaDetails(){
		document.getElementById("shipViaFormID").reset();
		return true;
	}
		