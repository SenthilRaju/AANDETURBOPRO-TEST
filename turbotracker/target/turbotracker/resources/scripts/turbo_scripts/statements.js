var stCustomer, endCustomer,Query;
var newDialogDiv = jQuery(document.createElement('div'));
var displayName='';
var data1 ='';
var email ='';
var rxMasterID ='';
jQuery(document).ready(function(){
	$("#search").hide();
	$('#statementDate').datepicker().datepicker("setDate", new Date());
	$("#exclusionDate").datepicker().datepicker("setDate", new Date());
	$('#emailSubmit').click(function (){
		email = $('#mailId').val();
		setTimeout($.unblockUI, 100); 
		emailStatement(null);
	});
	$('#cancelSubmit').click(function(){
		setTimeout($.unblockUI, 100);
	});
});
function printStatements(){
	var statementDetails = $('#statementsForm').serialize();
	var msg ='There are no statements for this criteria.';
	var statementDate = $('#statementDate').val();
	$.ajax({
		url: "./customerList/printStatementConfirmation",
		type: "GET",
		data : statementDetails+'&startingCustomerName='+stCustomer+'&endingCustomerName='+endCustomer+'&style=inline',
		success: function(data) {
			console.log(data);
			if(data == 0){
				msg ='There are no statements for this criteria.';
				jQuery(newDialogDiv).html('<span style="color:red;">'+msg+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:120, title:"Information.", 
										buttons: [{height:35,text: "OK",click: function() { $(this).dialog("close"); }}]}).dialog("open");
			}else{
				msg ='Do you want to preview the statements.?';
				jQuery(newDialogDiv).attr("id","msgDlg");
				jQuery(newDialogDiv).html('<span><b style="color:red;">'+msg+'</b></span>');
				jQuery(newDialogDiv).dialog({modal: true, width:300, height:150, title:"Information.",
					buttons:{
						"Yes": function(){
							jQuery(this).dialog("close");
							$.blockUI({ css: { 
								message: "Please Wait for the preview.",
								border: 'none', 
								padding: '15px', 
								backgroundColor: '#000', 
								'-webkit-border-radius': '10px', 
								'-moz-border-radius': '10px', 
								opacity: .5, 
								'z-index':'1010px',
								color: 'aqua'
							} });
							if(data.length>0){
							var table = '<table style="border:1px solid;font-weight: normal !important;background-color:#FAFAFA;"><tr style="background-color:#485E70;color:#FAFAFA;font-size:.9em;"><td style="border: solid 1px;">Customer Name</td><td style="border: solid 1px;">Preview</td><td style="border: solid 1px;">Email</td ></tr>';
							for(var i=0;i<data.length;i++){
								var url ='./customerList/printStatement?filename='+data[i].rxMasterID+'_statements.pdf&rxMasterID='+data[i].rxMasterID+'&statementDate='+statementDate;
								table = table+'<tr style="height:31px;font-size:.8em;">'
												+'<td style="width: 65%; border-right: 1px solid;">'+data[i].name+'</td>'
												+'<td style=" border-right: 1px solid;"><a href='+url+' target="_blank">'
												+'<input type="button" value="Preview" class="savehoverbutton turbo-blue" style="height:26px !important;"/>'
												+'</a></td>'
												+'<td>'
												+'<input type="button" value="Email" class="savehoverbutton turbo-blue" onclick="emailStatement(this)" id="'+data[i].email+'rxMasterID='+data[i].rxMasterID+'" style="height:26px !important;"/>'
												+'</td>'
												/*+'<td style="display:hidden;">'
												+'<input type="text" value='+data[i].email+' id ="'+i+'"/>'
												+'</td>'*/
												+'</tr>';
							}
							table = table+'</table>';
							setTimeout($.unblockUI, 2000);
							$('#showStatements').empty();
							$('#showStatements').append(table);
							}
							setTimeout($.unblockUI, 2000);
						},	
						"No": function ()	{
							jQuery(this).dialog("close");
						}
					}}).dialog("open");
			}
		},
		error: function(data){
			console.log(data);
		}
	});
	
}
function openUrl(url){
		window.open(url);
		setTimeout('',30000);
	}
$('#stCustomerID').on('change', function() {
	stCustomer = $(this).find('option:selected').text();
});
$('#endCustomerID').on('change', function() {
	endCustomer = $(this).find('option:selected').text();
});
function printTrigger(elementId) {
    var getMyFrame = document.getElementById(elementId);
    getMyFrame.focus();
    getMyFrame.contentWindow.print();
}
function emailStatement(obj){
	var statementDate = $('#statementDate').val();
	if(obj!=null){
		var emailAndrxMasterID = obj.id;
		var split = emailAndrxMasterID.split("rxMasterID=");
		email =  split[0];
		rxMasterID = split[1];
	 }
	if(email=='' || email==null){
		$.blockUI({ message: $('#emailForm') }); 
	}
	if(email!=null||email!=''){
	$.ajax({
		url:'./customerList/emailStatement?filename='+rxMasterID+'_statements.pdf&rxMasterID='+rxMasterID+"&toEmailaddress="+email+"&StatementDate="+statementDate,
		type: "GET",
		success: function(data) {
			filePath = data;
			msgOnAjax("Generated the PDF", "green", 2000);
			setTimeout('msgOnAjax("email Sent successfully", "green", 2000)',3000);
			},error: function(data){
			msgOnAjax("Unable to send the mail. Please check the email server port and server host names.", "red", 4000);	
			}
	});
	}
}