

var vePOID;
var aContactID;
var POtransactionStatus;
var ourPoId;
var aShipToAddrID;
var aManufacturerId;
var cusotmerPONumber;


var SOnumberGeneral;
var cuSOid;


function getVepoorCusoDetails(id,oper,contactid)
{
	
			$.ajax({
				type : "POST",
				url : "./jobtabs5/getVepoorCusoDetails",
				data : { 'Id' : id, 'oper' : oper},
				success : function (data) {
					
					if(oper=="vepo")
						{
						//alert("vepo" + data.Vepo.transactionStatus);
						vePOID = data.Vepo.vePoid;
						aContactID = contactid;
						POtransactionStatus = data.Vepo.transactionStatus;
						aShipToAddrID = data.Vepo.rxShipToId;	
						cusotmerPONumber = data.Vepo.ponumber;
						aManufacturerId = $('#rxCustomer_ID').text();
						insidejobjoreleaseDropShip();
						
						//alert(data.Vepo.ponumber);
						}
					else
						{
						
						cuSOid = data.Cuso.cuSoid;
						SOnumberGeneral = data.Cuso.sonumber;
						cusotmerPONumber =  data.Cuso.sonumber;
						sendPOEmailrelease('SOJ');
						}
					
				},
				error : function (data) {}
			});
			
}



/***********************************************************************Purchase Order************************************************************************************/
function insidejobjoreleaseDropShip(){
	
	
/*	var vePOID = $("#vePOID").val();
	var aContactID = $("#contactId").val();*/
	
	var errorText = '';
	
	if(POtransactionStatus == '-1'){
		errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	return false;
	}
	else if(POtransactionStatus == '0'){
			 errorText = "You can not Send E-Mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() {
											$(this).dialog("close");
											$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	 else if(POtransactionStatus == '2'){
		 errorText = "You can not Send E-Mail, \nTransaction Status is 'Close' \nChange Status to Open.";
			jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
									buttons: [{height:35,text: "OK",click: function() {
										$(this).dialog("close");
										$("#cData").tigger('click');}}]}).dialog("open");
		return false;
	 }
	else{
	//var cusotmerPONumber = $("#ourPoId").val();
	if(vePOID === null || vePOID.length<= 0){
		errorText = "Please Save Purchase Order to Email Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	if(aContactID === null && aContactID === '' && aContactID === '-1'){
		errorText = "Please Add Contact for Purchase Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}else{

		purchaseorderpdfwrite1();
		
		}
} 
}

function purchaseorderpdfwrite1(){
	try{
		// var vePOID = $("#vePOID").val();
			if(vePOID === null || vePOID.length<= 0){
				errorText = "Please Save Purchase Order to View PDF.";
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
				return false;
			}
			//var aShipToAddrID = $('#rxShipToId').val();//"0";//"${theVepo.rxShipToID}";
		//	var aManufacturerId = $('#vendorsearchRXMasterID').val();//"615";//"${theVepo.rxVendorId}"; 
			var joReleaseId = "";//"${theVepo.joReleaseId}";
			var jobNumber = "";
			var rxCustomerId = "";
			var joMasterID=0;
		$.ajax({
			type : "GET",
			url : "./purchasePDFController/viewPDFLineItemForm",
			data : { 'joMasterID':joMasterID,'vePOID' : vePOID, 'puchaseOrder' : '', 'jobNumber' :  jobNumber, 'rxMasterID' : rxCustomerId, 'manufacturerID' : aManufacturerId, 'shipToAddrID' :0 ,'WriteorView':'write'},
			documenttype: "application\pdf",
			async: false,
			cache: false,
			success : function (msg) {
				setemailpopupdetails1();
				
			},
			error : function (msg) {}
		});
	 }catch(err){
		 alert(err.message);
	 }
	
	
}
function setemailpopupdetails1(){
	//var cusotmerPONumber = $("#ourPoId").val();
//	var vePOID = $("#vePOID").val();
//	var aContactID = $("#contactId").val();
	var toemailaddress="";
	var fullname="";
	$.ajax({ 
		url: "./vendorscontroller/GetContactDetails",
		mType: "GET",
		async:false,
		data : { 'rxContactID' : aContactID},
		success: function(data){
			var aFirstname = data.firstName;
			var aLastname = data.lastName;
			toemailaddress = data.email;
			$("#etoaddr").val(toemailaddress);
		    fullname = aFirstname + ' '+aLastname;
		   // $('#loadingPODiv').css({"visibility": "visible"});
		}
	});
	$.ajax({ 
		url: "./vendorscontroller/GetFromAddressContactDetails",
		mType: "GET",
		async:false,
		data : { },
		success: function(data){
				$("#efromaddr").val(data.emailAddr);
				var ccaddr1=data.ccaddr1;
				var ccaddr2=data.ccaddr2;
				var ccaddr3=data.ccaddr3;
				var ccaddr4=data.ccaddr4;
		var ccaddress="";
		if(ccaddr1!=null && ccaddr1!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr1;
			}else{
				 ccaddress=ccaddress+","+ccaddr1;
			}
		}
		if(ccaddr2!=null && ccaddr2!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr2;
			}else{
				 ccaddress=ccaddress+","+ccaddr2;
			}
		}
		if(ccaddr3!=null && ccaddr3!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr3;
			}else{
				 ccaddress=ccaddress+","+ccaddr3;
			}
		}
		if(ccaddr4!=null && ccaddr4!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr4;
			}else{
				 ccaddress=ccaddress+","+ccaddr4;
			}
		}
		$("#eccaddr").val(ccaddress);
		$("#esubj").val("Purchase Order # "+cusotmerPONumber);
		$("#filelabelname").text("PurchaseOrder_"+cusotmerPONumber+".pdf");
		
		$('#emailpopup').data('type', "GridPO");
		$("#emailpopup" ).dialog("open");
		}
	});
}

function sendsubmitMailFunction2(){
//	var vePOID = $("#vePOID").val();
//	var cusotmerPONumber = $("#ourPoId").val();
//	var aContactID = $("#contactId").val();
	
	
	var toemailAddress=$("#etoaddr").val();
	var ccaddress=$("#eccaddr").val();
	var subject=$("#esubj").val();
	var filename="PurchaseOrder_"+cusotmerPONumber+".pdf";
	//var content=$("#econt").val();
	//content=$('#econt').find('.nicEdit-main').html();
	//content=$(".nicEdit-main").html();
	var content=$('#emailform').find('.nicEdit-main').html();
	$.ajax({ 
		url: "./sendMailServer/sendOutsitePurchaseOrderMail",
		mType: "POST",
		data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
				'toAddress':toemailAddress, 'subject':subject,
				'filename':filename,'ccaddress':ccaddress,
				'content':content
			},
		success: function(data){
			//$('#loadingPOGenDiv').css({"visibility": "hidden"});
			//$('#loadingPODiv').css({"visibility": "hidden"});
			var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
			if(data){
				errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
				}
			jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
				buttons: [{height:35,text: "OK",click: function() {
					var today = new Date();
					var dd = today.getDate();
					var mm = today.getMonth()+1; 
					var yyyy = today.getFullYear().toString().substr(2,2);
					var hours = today.getHours();
					var minutes = today.getMinutes();
					var ampm = hours >= 12 ? 'PM' : 'AM';
					hours = hours % 12;
					hours = hours ? hours : 12;
					if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
					if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
					$.ajax({ 
						url: "./jobtabs3/updateEmailStampValue",
						mType: "GET",
						data : { 'vePOID' : vePOID, 'purcheaseDate' : today},
						success: function(data){ 
							$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
							$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); 
							},error:function(e){
								 $('#loadingDivForPO').css({
										"visibility" : "hidden","display": "none"
									}); 
								}
					});
					$('#loadingDivForPO').css({
						"visibility" : "hidden","display": "none"
					}); 
					$(this).dialog("close"); 
					$("#emailpopup" ).dialog("close");
				}}] }).dialog("open");
		},error:function(e){
			 $('#loadingDivForPO').css({
					"visibility" : "hidden","display": "none"
				}); 
			}
	});	
	
}


/***********************************************************************SalesOrder************************************************************************************/

function sendPOEmailrelease(poGeneralKey){
	
	//alert("1");
	
	console.log('Email form---- SO_General.js--->'+$('#emailListCU').text()+'  :::  poGeneralKey : '+poGeneralKey);
	if(POtransactionStatus === '-1'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Void'\nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
	return false;
	}
	else if(POtransactionStatus === '0'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Hold' \nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	else if(POtransactionStatus === '2'){
		var newDialogDiv = jQuery(document.createElement('div'));
		errorText = "You can not send E-mail, \nTransaction Status is 'Close' \nChange Status to Open.";
		jQuery(newDialogDiv).attr("id","msgDlg");
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() {
									$(this).dialog("close");
									$("#cData").tigger('click');}}]}).dialog("open");
			return false;
	}
	else{
	try{
	var rxMasterID = $('#rxCustomer_ID').text();
	var newDialogDiv = jQuery(document.createElement('div'));
	var bidderGrid = $("#salesrelease");
	var aQuotePDF = "purchase";
	if(rxMasterID === null || rxMasterID.length<= 0){
		errorText = "Please Save Sales Order to Email Sales Order.";
		jQuery(newDialogDiv).html('<span><b style="color:red;">'+errorText+'</b></span>');
		jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Warning", 
								buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
		return false;
	}
	
	
		$.ajax({ 
			url: "./vendorscontroller/GetContactDetailsFromCuso",
			mType: "GET",
			data : { 'rxMasterID' : rxMasterID},
			success: function(data){
				var aFirstname = data.firstName; 
				var aLastname = data.lastName;
				var aEmail;
				if('CuInvoice' === poGeneralKey)
					{
					if($('#emailListCU').text() != null)
						{
						aEmail=$("#emailListCU option:selected").text();
						arxContactid = $("#emailListCU option:selected").val();
						}
					else
						{
						aEmail= data.email;
						arxContactid = data.rxContactId;
						}
					}
				else
					{
					if($('#emailList').text() != null)
						{
						aEmail=$("#emailList option:selected").text();
						arxContactid = $("#emailList option:selected").val();
						}
					else
					{
						aEmail= data.email;
						arxContactid = data.rxContactId;
					}
					}
				
				callEmailPopuprelease(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, cuSOid);
			}
		});
		
	}catch(err){
		}
	return true;  
	}
}

function clearemailattachmentForm(){
	$("#efromaddr").val("");
	$("#etoaddr").val("");
	$("#eccaddr").val("");
	$("#esubj").val("");
	$("#econt").val("");
	$(".nicEdit-main").html("");
	$("#filelabelname").text("");
	$("#attachmentfilename").val("");

}

function callEmailPopuprelease(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, cuSOid){
clearemailattachmentForm();
	

	/*var cusotmerPONumber = cusotmerPONumber1;
	var vePOID = cuSOid; 
	var aContactID = arxContactid;*/
	var toemailaddress="";
	var fullname="";
	
	
	$.ajax({ 
		url: "./vendorscontroller/GetContactDetails",
		mType: "GET",
		async:false,
		data : { 'rxContactID' : aContactID},
		success: function(data){
			var aFirstname = data.firstName;
			var aLastname = data.lastName;
			toemailaddress = data.email;
			$("#etoaddr").val(toemailaddress);
		    fullname = aFirstname + ' '+aLastname;
		   // $('#loadingPODiv').css({"visibility": "visible"});
	
		}
	});
	
	if(poGeneralKey=="SOJ"){
		var WareHouseID=$("#whrhouseID").val();
		if(WareHouseID==undefined ||WareHouseID==null || WareHouseID=="" ){
			WareHouseID=-1;
		}
		$.ajax({ 
			url: "./vendorscontroller/GetTOEmailWarehouseAdd",
			mType: "GET",
			async:false,
			data : { 'WareHouseID' : WareHouseID,'cuSOID':cuSOid},
			success: function(data){
				toemailaddress = data.email;
				$("#etoaddr").val(toemailaddress);
			}
		});
	}
	
	$.ajax({ 
		url: "./vendorscontroller/GetFromAddressContactDetails",
		mType: "GET",
		async:false,
		data : { },
		success: function(data){
				$("#efromaddr").val(data.emailAddr);
				var ccaddr1=data.ccaddr1;
				var ccaddr2=data.ccaddr2;
				var ccaddr3=data.ccaddr3;
				var ccaddr4=data.ccaddr4;
		var ccaddress="";
		if(ccaddr1!=null && ccaddr1!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr1;
			}else{
				 ccaddress=ccaddress+","+ccaddr1;
			}
		}
		if(ccaddr2!=null && ccaddr2!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr2;
			}else{
				 ccaddress=ccaddress+","+ccaddr2;
			}
		}
		if(ccaddr3!=null && ccaddr3!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr3;
			}else{
				 ccaddress=ccaddress+","+ccaddr3;
			}
		}
		if(ccaddr4!=null && ccaddr4!=""){
			if(ccaddress==""){
				 ccaddress=ccaddr4;
			}else{
				 ccaddress=ccaddress+","+ccaddr4;
			}
		}
		$("#eccaddr").val(ccaddress);
		$("#esubj").val("Sales Order # "+cusotmerPONumber);
		$("#filelabelname").text("SalesOrder_"+cusotmerPONumber+".pdf");
		
		//alert(withPrice);
		
		$('#emailpopup').data('withprice', withPrice);
		$('#emailpopup').data('arxContactid', arxContactid);
		$('#emailpopup').data('aEmail', aEmail);
		$('#emailpopup').data('poGeneralKey', poGeneralKey);
		$('#emailpopup').data('cusotmerPONumber', cusotmerPONumber1);
		$('#emailpopup').data('cuSOid', cuSOid);
		$('#emailpopup').data('type', "GridSO");
		$("#emailpopup" ).dialog("open");
		}
	});
}


function sendsubmitMailFunction5(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, numberof){
	
		var cuSOId = numberof;
		var cusotmerPONumber = cusotmerPONumber1;
		var aContactID = arxContactid;
		var toemailAddress=$("#etoaddr").val();
		var ccaddress=$("#eccaddr").val();
		var subject=$("#esubj").val();
		var filename="SalesOrder.pdf";
		//var content=$("#econt").val();
		//content=$('.nicEdit-main').html();
		//content=nicEditors.findEditor('econt').getContent();
		var content=$('#emailform').find('.nicEdit-main').html();
		var newDialogDiv = jQuery(document.createElement('div'));
		$.ajax({ 
			url: "./sendMailServer/sendOutsitePurchaseOrderMail",
			mType: "POST",
			data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
					'toAddress':toemailAddress, 'subject':subject,
					'filename':filename,'ccaddress':ccaddress,
					'content':content
				},
			success: function(data){
				//$('#loadingPOGenDiv').css({"visibility": "hidden"});
				//$('#loadingPODiv').css({"visibility": "hidden"});
				var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
				if(data){
					errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
					}
				jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
					buttons: [{height:35,text: "OK",click: function() {
						var today = new Date();
						var dd = today.getDate();
						var mm = today.getMonth()+1; 
						var yyyy = today.getFullYear().toString().substr(2,2);
						var hours = today.getHours();
						var minutes = today.getMinutes();
						var ampm = hours >= 12 ? 'PM' : 'AM';
						hours = hours % 12;
						hours = hours ? hours : 12;
						if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
						if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
						$.ajax({ 
							url: "./jobtabs3/updateSOEmailTimeStamp",
							mType: "GET",
							data : { 'coSoID' : cuSOId, 'purcheaseDate' : today},
							success: function(data){ 
								$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
								$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); }
						});
						$(this).dialog("close"); 
						$("#emailpopup" ).dialog("close");
						 $('#loadingDiv').css({	"visibility" : "hidden"}); 
					}}] }).dialog("open");
			}
		});	
		
	}

function submitemailattachment(){
	 var $led = $("#emailpopup");
	 
	 if($led.data('type') === "Quotes")
	 {
		// $('#loadingDiv').css({"visibility": "visible","z-Index":"1234","top": "-294px"});
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 sendQuotesubmitMailFunction(); 
	 }
	 else if($led.data('type') === "DialogPO")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 sendsubmitMailFunction();
	 }
	 else if($led.data('type') === "DialogSO")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 viewPOPDFSave($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuSOid'),$led.data('withprice'));
	 }
	 else if($led.data('type') === "GridPO")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 sendsubmitMailFunction2();
	 }
	 else if($led.data('type') === "GridSO")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 viewPOPDFSave($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuSOid'),$led.data('withprice'));
	 }
	 else if($led.data('type') === "DialogCIOutSide")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 saveCuInvoicePDF($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuInvoiceId'));
		
	 }
	 else if($led.data('type') === "DialogCIInside")
	 {
		 $('#loadingDivForPO').css({"visibility": "visible","z-Index":"1234","display":"block"});
		 saveCuInvoicePDF1($led.data('arxContactid'),$led.data('aEmail'), $led.data('poGeneralKey'),$led.data('cusotmerPONumber') ,$led.data('cuInvoiceId'));
	 }
}

$(function(){
	$("#emailpopup" ).dialog({
		autoOpen: false,
		height: 540,
		width: 600,
		modal: true
		//buttons: {
			/*		
			Send:function(){  
				
	   		 },	
		
			Cancel: function() {
				$( this ).dialog( "close" );
			}*/
		//},	
	});
	})
	
	
function updateEmailTimestamp(type,processID){
	var today = new Date(); 
	var dd = today.getDate();
	var mm = today.getMonth()+1; 
	var yyyy = today.getFullYear().toString().substr(2,2);
	var hours = today.getHours();
	var minutes = today.getMinutes();
	var ampm = hours >= 12 ? 'PM' : 'AM';
	hours = hours % 12;
	hours = hours ? hours : 12;
	if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
	if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
	$.ajax({ 
		url: "./updateEmailStampValue",
		mType: "GET",
		data : { 'type' : type, 'processID' : processID},
		success: function(data){
			return data;
			}
	});
	
}

/********************************************************************************Customer Invoice****************************************************************************************/
	
	function saveCuInvoicePDF1(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof){	
	
	//alert("hi Da");
	var newDialogDiv = jQuery(document.createElement('div'));
	var CuInvoice = numberof;
	if(CuInvoice != '' && CuInvoice != undefined)
		{
			$.ajax({ 
				url: "./salesOrderController/saveCustomerInvoiceReport",
				mType: "GET",
				data : { 'CuInvoice' : CuInvoice},
				success: function(data){ 
					jQuery(newDialogDiv).dialog("close");
					
					sendsubmitMailFunction9(arxContactid,aEmail, poGeneralKey, cusotmerPONumber, numberof); 
					}
			});
		}
		
	return true;
}



function sendsubmitMailFunction9(arxContactid,aEmail, poGeneralKey, cusotmerPONumber1, numberof){
	
	var vePoId = numberof;
	var cusotmerPONumber = cusotmerPONumber1;
	var aContactID = arxContactid;
	var toemailAddress=$("#etoaddr").val();
	var ccaddress=$("#eccaddr").val();
	var subject=$("#esubj").val();
	var filename="CustomerInvoice.pdf";
	//var content=$("#econt").val();
	//content=$('.nicEdit-main').html();
	//content=nicEditors.findEditor('econt').getContent();
	var content=$('#emailform').find('.nicEdit-main').html();
	var newDialogDiv = jQuery(document.createElement('div'));
	var status ="";
	$.ajax({ 
		//url: "./sendMailServer/sendOutsitePurchaseOrderMail",
		url: "./sendMailServer/sendCustomerInvoiceMail",
		mType: "POST",
		data : {'contactID' : aContactID, 'PONumber' : cusotmerPONumber,
				'toAddress':toemailAddress, 'subject':subject,
				'filename':filename,'ccaddress':ccaddress,
				'content':content
			},
		success: function(data){
			//$('#loadingPOGenDiv').css({"visibility": "hidden"});
			//$('#loadingPODiv').css({"visibility": "hidden"});
			var errorText = "<b style='color:Red; align:right;'>Mail Sending Failed.</b>";
			var actionStatus = 4;
			if(data){
				$.ajax({ 
					url: "./updateEmailStampValue",
					mType: "GET",
					data : { 'type' : 'cuInvoice', 'processID' : numberof},
					success: function(data){
					 $("#mailTimestampLines").show();
						$("#mailTimestampGeneral").show();
						$("#mailTimestampLines").empty(); 
						$("#mailTimestampLines").append(data);
						$("#mailTimestampGeneral").empty(); 
						$("#mailTimestampGeneral").append(data);
					}
				});
				errorText = "<b style='color:Green; align:right;'>Mail sent successfully.</b>";
				actionStatus = 3;
				}
			
			 $.ajax({ 
					url: "./jobtabs5/addInvoiceLog",
					type: "POST",
					data : {'cuInvoiceID' : numberof, 'action' : actionStatus},
					success: function(data){
						// action = 1 view action=2 pdf view action=3 mail sent action=4 mail sent Failed
					}
				});
			
			jQuery(newDialogDiv).html('<span>'+errorText+'</span>');
			jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Message", 
				buttons: [{height:35,text: "OK",click: function() {
					
//					var today = new Date();
//					var dd = today.getDate();
//					var mm = today.getMonth()+1; 
//					var yyyy = today.getFullYear().toString().substr(2,2);
//					var hours = today.getHours();
//					var minutes = today.getMinutes();
//					var ampm = hours >= 12 ? 'PM' : 'AM';
//					hours = hours % 12;
//					hours = hours ? hours : 12;
//					if(dd<10){dd='0'+dd;} if(mm<10){mm='0'+mm;} 
//					if(hours<10){hours='0'+hours;} 	if(minutes<10){minutes='0'+minutes;} today = mm+'/'+dd+'/'+yyyy+ " "+hours+":"+minutes+" "+ampm;
//					$.ajax({ 
//						url: "./jobtabs3/updateEmailStampValue",
//						mType: "GET",
//						data : { 'vePOID' : vePoId, 'purcheaseDate' : today},
//						success: function(data){ 
//							$("#emailTimeStamp").empty(); $("#emailTimeStamp").append(today);
//							$("#emailTimeStampLines").empty(); $("#emailTimeStampLines").append(today); 
//							},error:function(e){
//								 $('#loadingDivForPO').css({
//										"visibility" : "hidden","display": "none"
//									}); 
//								}
//					});
					$(this).dialog("close"); 
					$("#emailpopup" ).dialog("close");
					 $('#loadingDiv').css({	"visibility" : "hidden"}); 
					 $('#loadingDivForPO').css({
							"visibility" : "hidden","display": "none"
						}); 
				}}] }).dialog("open");
		},error:function(e){
			 $('#loadingDivForPO').css({
					"visibility" : "hidden","display": "none"
				}); 
			}
	});	
	
}
