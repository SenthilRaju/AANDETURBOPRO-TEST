var _globalPeriodID=0;
var _globalPeriodStatus;

jQuery(document).ready(
	function() {
		$("#clPeriodEnd").datepicker();
		$("#dtp_startDate").datepicker();
		$("#dtp_endDate").datepicker();
		$("#txt_periodstart1").datepicker();
		$("#txt_periodstart2").datepicker();
		$("#txt_periodstart3").datepicker();
		$("#txt_periodstart4").datepicker();
		$("#txt_periodstart5").datepicker();
		$("#txt_periodstart6").datepicker();
		$("#txt_periodstart7").datepicker();
		$("#txt_periodstart8").datepicker();
		$("#txt_periodstart9").datepicker();
		$("#txt_periodstart10").datepicker();
		$("#txt_periodstart11").datepicker();
		$("#txt_periodstart12").datepicker();
		$("#txt_periodstart13").datepicker();
		$("#txt_periodend1").datepicker();
		$("#txt_periodend2").datepicker();
		$("#txt_periodend3").datepicker();
		$("#txt_periodend4").datepicker();
		$("#txt_periodend5").datepicker();
		$("#txt_periodend6").datepicker();
		$("#txt_periodend7").datepicker();
		$("#txt_periodend8").datepicker();
		$("#txt_periodend9").datepicker();
		$("#txt_periodend10").datepicker();
		$("#txt_periodend11").datepicker();
		$("#txt_periodend12").datepicker();
		$("#txt_periodend13").datepicker();
		var curDatefordpcheck = new Date();		
	
		$("#search").hide();
		
		$("#txt_name").val($("#drp_yearList option:selected").data('yearvalue'));
		$("#txa_Description").val($("#drp_yearList option:selected").data('description'));
		
		/** UI Designs */
		
		$('#date').datepicker();
		$(".charts_tabs_main").tabs({
				cache : true,
				ajaxOptions: {
					data: {},
					error: function(xhr, status, index, anchor) {
						$(anchor.hash).html("<div align='center' style='height: 600px;padding-top: 200px;'>"
												+ "<label style='font-size : 17px;margin-left: 30px;vertical-align: middle;'>Couldn't load this tab. Please try again later."
												+ "</label></div>");
					}
				},
				load: function(e, ui) {
					$(ui.panel).find(".tab-loading").remove();
				},
				select : function(e, ui) {
					// window.location.hash =
					// ui.tab.hash;
					var $panel = $(ui.panel);
					if ($panel.is(":empty")) {
						$panel.append("<div class='tab-loading' align='center' style='height: 350px;padding-top: 200px;background-color: #FAFAFA'>"
									+ "<img src='./../resources/scripts/jquery-autocomplete/loading.gif'></div>");
					}
				}
			});
		
		
		
		
		/**
		 * Created by: Leo  Date: Mar 07 2015
		 * Description: Auto Populate All Periods Date Fields
		 * 
		 * */
		
	
		$("#dtp_endDate").change(function(){
			
			
		var d = new Date();
		if($("#dtp_endDate").val()!="")
			{
		var getCurrentMonth =$("#dtp_endDate").val().split("/");
		var currentDate = new Date($("#dtp_endDate").val());
		var currentstDate = new Date($("#dtp_startDate").val());
		var firstDay = "";
		var lastDay = "";
		
		
		
		
		for(var i=1 ;i<=monthDiff(currentstDate,currentDate)+1;i++)
			{
			var j = (currentstDate.getMonth())+(i-1);
			d.setFullYear(currentstDate.getFullYear());
			d.setMonth(1);
			d.setMonth(j);
			
			firstDay = new Date(d.getFullYear(), d.getMonth(), 1);
			lastDay = new Date(d.getFullYear(), d.getMonth() + 1, 0);
			
			
			if(i==1)
				{
				$("#txt_periodstart1").datepicker('setDate', new Date($("#dtp_startDate").val()));
				$("#txt_periodend1").datepicker('setDate', lastDay);
				
				var date = new Date( Date.parse( lastDay ) ); 
				date.setDate( date.getDate() + 1 );
				$("#txt_periodstart2").datepicker('option', 'minDate', date);
				
				}
			else if (i==monthDiff(currentstDate,currentDate)+1)
				{
				
				$("#txt_periodstart"+i).datepicker('setDate', firstDay);
				$("#txt_periodend"+i).datepicker('setDate', lastDay);
				
				$("#txt_periodstart13").val("");
				$("#txt_periodend13").val("");
			//	$("#txt_periodstart13").datepicker('setDate', lastDay);
			//	$("#txt_periodend13").datepicker('setDate',lastDay);
				
				$("#txt_periodstart13").datepicker('setDate', new Date($("#dtp_endDate").val()));
				$("#txt_periodstart13").datepicker('setDate', new Date($("#dtp_endDate").val()));
				$("#txt_periodend13").datepicker('setDate',new Date($("#dtp_endDate").val()));
				
				
				}
			else
				{
			//	console.log(i+"=hi="+firstDay+"=="+j);
				$("#txt_periodstart"+i).datepicker('setDate', firstDay);
				$("#txt_periodend"+i).datepicker('setDate', lastDay);
				
				
				var date = new Date( Date.parse( lastDay ) ); 
				date.setDate( date.getDate() + 1 );
				$("#txt_periodstart"+(parseInt(i)+1)).datepicker('option', 'minDate', date);
				}
			
			
			}
			}
		else
			{
			for(var i=1 ;i<=13;i++)
			{
				$("#txt_periodstart"+i).val("");
				$("#txt_periodend"+i).val("");
			}
			$('#frm input[type="radio":checked]').each(function(){
			      $(this).checked = false;  
			  });
			}
		
		});
		
		
		function monthDiff(d1, d2) {
		    var months;
		    months = (d2.getFullYear() - d1.getFullYear()) * 12;
		    months -= d1.getMonth() + 1;
		    months += d2.getMonth();
		    // edit: increment months if d2 comes later in its month than d1 in its month
		    if (d2.getDate() >= d1.getDate())
		        months++
		    // end edit
		    return months <= 0 ? 0 : months;
		}
		
		/**
		 * Created by: Leo  Date: Mar 06 2015
		 * Description: Digits Only validation
		 * */
		$('.number').keypress(function (event) {
		    if ((event.which != 46 || $(this).val().indexOf('.') != -1) && (event.which < 48 || event.which > 57)) {
		        event.preventDefault();
		    }
		    var text = $(this).val();
		    if ((text.indexOf('.') != -1) && (text.substring(text.indexOf('.')).length > 2)) {
		        event.preventDefault();
		    }
		});
		
		/**
		 * Created by: Leo  Date: Mar 06 2015
		 * Description: Clear all fields
		 * */
		$("#txt_name").change(function(){
			
			$("#dtp_startDate").val("");
			$("#dtp_endDate").val("");
			$("#dtp_endDate").trigger("change");
		});
		
		/**
		 * Created by: Leo  Date: Mar 06 2015
		 * Description: Get All Periods Data
		 * */

        $.ajax({
		url : "./getAllcoFiscalPeriodBasedonyear",
		type : "POST",
		data : {
			'closesysInfoId' : jQuery("#accountingBasicsId").val(),			
		},
		success : function(data) {
			
			var count =1;
			var curPeriod = $("#currentPeriodId").val();
			
			
				$.each(data, function(key, valueMap) {								
					
					if(key=="currentPeriodList")
						{
						$.each(valueMap, function(index, value){
							//alert(value.period);
							
								if(value.period == 13)
								{
								$("#txt_periodstart13").val(getFormattedDate(new Date(value.startDate)));
								$("#txt_periodend13").val(getFormattedDate(new Date(value.endDate)));
								$("#txt_periodstart13").datepicker('option', 'minDate', new Date(value.endDate));
								if(value.openStatus===null)
								$("#rad_close13").attr("checked",false);
								else
								$("#rad_close13").attr("checked",true);	
								}
								else
								{
								//$("#txt_periodstart"+value.period).val(getFormattedDate(new Date(value.startDate)));
								
								$("#txt_periodstart"+value.period).datepicker("setDate", new Date(value.startDate) );
								$("#txt_periodend"+value.period).datepicker("setDate", new Date(value.endDate) );
								//$("#rad_close13").attr("checked",true);
								//$("#txt_periodend"+value.period).val(getFormattedDate(new Date(value.endDate)));
							//	$("#txt_periodstart"+(parseInt(value.period)+1)).datepicker('option', 'minDate', new Date(value.endDate));
								}
							count++;
							
							if(parseInt($("#drp_yearList option:selected").val()) == parseInt($("#currentfiscalYearId").val()))
							{
							
								if(value.coFiscalPeriodId == parseInt($("#currentPeriodId").val()))
								{
								$("#green_arrow"+value.period).css("display","inline");
							
								}
								if(value.openStatus)
								{
								$("#rad_open"+value.period).attr("checked",true);	
								}
								else 
								{
								if(value.coFiscalPeriodId <= parseInt($("#currentPeriodId").val()))	
								$("#rad_close"+value.period).attr("checked",true);
								}
							}
							else if(parseInt($("#drp_yearList option:selected").text()) < parseInt(curDatefordpcheck.getFullYear()))
							{
								if(value.openStatus)
								{
								$("#rad_open"+value.period).attr("checked",true);	
								}
								else 
								{
								$("#rad_close"+value.period).attr("checked",true);
								}
							
							}
						}); 
						}
					else if(key=="ActivityList")
						{
						$.each(valueMap, function(index, value){
							$("#activity"+value.period).text(value.activityCount);
						}); 
						}
				});
				
		}
        });
		
        /**
         * Created by: Leo  Date: Mar 06 2015
         * Description: Dropdown onchange Event
         * */
        
       $("#drp_yearList").live("change",function(){
    	   
    	   $.ajax({
    			url : "./cofiscalYeardropdownchanged",
    			type : "POST",
    			data : {
    				'yearId' : $("#drp_yearList option:selected").val(),	
    				'closesysInfoId' : jQuery("#accountingBasicsId").val(),	
    			},
    			success : function(data)	
    			{
    			var prevYearchosen =	$("#drp_yearList option:selected").val();
    			document.getElementById("chartsDetailsFromID").reset();
    			
    			for(var b=1;b<=13;b++)
    				{
    				$("#activity"+b).text("0");
    				$("#rad_open"+b).removeAttr("checked");
    				$("#green_arrow"+b).css("display","none");
    				}
    			
    			$("#drp_yearList option:selected").removeAttr("Selected");
    			$("#drp_yearList option[value='"+prevYearchosen+"']").attr("Selected", true);
    			
    				
    				$.each(data, function(key, valueMap) {		
    					
    						if(key=="dpchange_getYearList")
    						{
    							$.each(valueMap, function(index, value){
    								
    								$("#txt_name").val(value.fiscalYear);
    								$("#dtp_startDate").val(getFormattedDate(new Date(value.startDate)));
    								$("#dtp_endDate").val(getFormattedDate(new Date(value.endDate)));
    								$("#txa_Description").val(value.description);
    							});
    						}
    						else if(key=="dpchange_getPeriodList")
    						{
    							$.each(valueMap, function(index, value){
    								
    								if(value.period == 13)
    								{
    								$("#txt_periodstart13").val(getFormattedDate(new Date(value.startDate)));
    								$("#txt_periodend13").val(getFormattedDate(new Date(value.endDate)));
    								if(value.openStatus===null)
    									$("#rad_close13").attr("checked",false);
    									else
    									$("#rad_close13").attr("checked",true);	
    								}
    								else
    								{
    								$("#txt_periodstart"+value.period).val(getFormattedDate(new Date(value.startDate)));
    								$("#txt_periodend"+value.period).val(getFormattedDate(new Date(value.endDate)));
    								}
    								
    								if(parseInt($("#drp_yearList option:selected").text()) == parseInt(curDatefordpcheck.getFullYear()))
    								{
    								
    									if(value.coFiscalPeriodId == parseInt($("#currentPeriodId").val()))
    									{
    									$("#green_arrow"+value.period).css("display","inline");
    									}
    									
    									if(value.openStatus)
    									{
    									$("#rad_open"+value.period).attr("checked",true);	
    									}
    									else 
    									{
    									if(value.coFiscalPeriodId <= parseInt($("#currentPeriodId").val()))	
    									$("#rad_close"+value.period).attr("checked",true);
    									}
    								
    								}
    								else if(parseInt($("#drp_yearList option:selected").text()) < parseInt(curDatefordpcheck.getFullYear()))
    								{
    									if(value.openStatus)
    									{
    									$("#rad_open"+value.period).attr("checked",true);	
    									}
    									else 
    									{
    									$("#rad_close"+value.period).attr("checked",true);
    									}
    								}
    								
    								
    							});
    						}
    						else if(key=="dpchange_getActivityList")
    						{
    							
    							$.each(valueMap, function(index, value){
    								$("#activity"+value.period).text(value.activityCount);
    							}); 
    						}
    					});
    			
    			}
    			});
    	   call13periodbuttonval()
       });
       
       
       $('input[type=radio]').live("click",function(){
    	  
    	   
    	   var withNoDigits = $(this).attr("id").replace(/[0-9]/g, '');
    	   var cPeriod = $(this).val();
    	   var cYearid = $("#drp_yearList option:selected").val();
    	   _globalPeriodID = $(this).val();
    	   var closedyearflag = closedyearstatus();
    	   
    	   if(closedyearflag)
    	   {
    		   if(withNoDigits=="rad_open")
    		   {
    				var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Are you sure you want to Open this period?</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Open/Close",
						buttons : [ {
							text : "OK",
							click : function() {
								$(this).dialog("close");
								closeperiod(cYearid,cPeriod,'open');
								
							}},
							 {
								text : "Close",
								click : function() {
									$(this).dialog("close");
							//		document.location.reload();
								}
						}]
					}).dialog("open");
			
    		   }
    		   else
		       {
    				var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Are you sure you want to Close this period?</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Open/Close",
						buttons : [ {
							text : "OK",
							click : function() {
								$(this).dialog("close");
								closeperiod(cYearid, cPeriod,'close');
								
							}},
							 {
								text : "Close",
								click : function() {
									$(this).dialog("close");
									document.location.reload();
								}
						}]
					}).dialog("open");
		    	   }
    	   }
    	   else
    		   {
    		   var newDialogDiv = jQuery(document.createElement('div'));
				jQuery(newDialogDiv).html('<span><b style="color:Red;">By Opening this Period you will reopen the Fiscal Year. Are you Sure?</b></span>');
				jQuery(newDialogDiv).dialog({
					modal : true,
					width : 400,
					height : 200,
					title : "Open/Close",
					buttons : [ {
						text : "OK",
						click : function() {
							$(this).dialog("close");
							updateyearopen();
							closeperiod(cYearid,cPeriod,'open');
							
						}},
						 {
							text : "Close",
							click : function() {
								$(this).dialog("close");
						//		document.location.reload();
							}
					}]
				}).dialog("open");
    		   
    		   }
       }); 	  
      
});


function closedyearstatus()
{
	
	var clyearstatus;
	$.ajax({
		url : "./checkClosedyearstatus",
		type : "POST",
		data : {'currentyear' :$("#drp_yearList option:selected").val()},
		async:false,
		success : function(data) {
			clyearstatus = data;
		}
		
	});
	return clyearstatus;
}

function updateyearopen()
{
	$.ajax({
		url : "./updateyeartoopen",
		type : "POST",
		data : {'currentyear' :$("#drp_yearList option:selected").val()},
		success : function(data) {
		}
		
	});
}


/**
 * Created by: Leo  Date: Mar 06 2015
 * Description: Date Format
 * */

function getFormattedDate(date) {
	  var year = date.getFullYear();
	  var month = (1 + date.getMonth()).toString();
	  month = month.length > 1 ? month : '0' + month;
	  var day = date.getDate().toString();
	  day = day.length > 1 ? day : '0' + day;
	  return  month+ '/' + day + '/' + year;
	}

function closeperiod(cYearid,curentPeriod,oper)
{
	
	$.ajax({
		url : "./closecurrentPeriod",
		type : "POST",
		data : {
			'closeCurrentPeriodId' : curentPeriod,
			'closeCurrentYearId' : cYearid,
			'oper':oper
		},
		success : function(data) {
		//	document.location.reload();
		}
		
	});
}

function period_change(periodendid)
{
	 $("#"+periodendid).change(function(){
		 var date1 = $("#"+periodendid).datepicker('getDate');
		    var date = new Date( Date.parse( date1 ) ); 
		    date.setDate( date.getDate() + 1 );
		    var newDate = date.toDateString(); 
		    newDate = new Date( Date.parse( newDate ) );
		    var thenum = periodendid.match(/\d+$/)[0];
		    $("#txt_periodstart"+(parseInt(thenum)+1)).datepicker('setDate', newDate );
		   $("#txt_periodstart"+(parseInt(thenum)+1)).datepicker('option', 'minDate', newDate);
	 });
	
}




function cofiscalyearadd()
{
	var jsonList1='';
	var jsonSt="[";
	var jsonEnd="]";
	if($("#dtp_endDate").val()!="" && $("#dtp_startDate").val() !="" && $("#txt_name").val()!="")
		{
            for(a=1;a<=13;a++)
            {
            	if($("#txt_periodstart"+a).val()!="")
            		{
                    if(a == 1)
                             jsonList1 =jsonSt+"{\"period\":\""+a+"\",\"periodstartdate\":\""+$("#txt_periodstart"+a).val()+"\",\"periodenddate\":\""+$("#txt_periodend"+a).val()+"\"}";
                    else
                             jsonList1 = jsonList1+','+"{\"period\":\""+a+"\",\"periodstartdate\":\""+$("#txt_periodstart"+a).val()+"\",\"periodenddate\":\""+$("#txt_periodend"+a).val()+"\"}";
            		}
            }
            
            console.log(jsonList1+jsonEnd);
            jsonList1=jsonList1+jsonEnd;
	
        $.ajax({
		url : "./savecoFiscalYear",
		type : "POST",
		data : {
			
			'coFiscalYear' :jQuery("#txt_name").val(),
			'closesysInfoId' : jQuery("#accountingBasicsId").val(),			
			'curyearstartdate' : jQuery("#dtp_startDate").val(),
			'curyearenddate' : jQuery("#dtp_endDate").val(),
			'jsonList' : jsonList1,
			'description':jQuery("#txa_Description").val(),
		},
		success : function(data) {
			
			if(data=="Success")
				{
				createtpusage('Company-Accounting Cycles','Add Fiscal Year','Info','Company-Accounting Cycles,Adding Fiscal Year,coFiscalYear:'+jQuery("#txt_name").val()+',description:'+jQuery("#txa_Description").val());
				var newDialogDiv = jQuery(document.createElement('div'));
					jQuery(newDialogDiv).html('<span><b style="color:Green;">Account Cycle Closed successfully.</b></span>');
					jQuery(newDialogDiv).dialog({
						modal : true,
						width : 300,
						height : 150,
						title : "Success.",
						buttons : [ {
							text : "OK",
							click : function() {
								$(this).dialog("close");
								jQuery("#closePeriodDialog").dialog("close");
								document.location.reload(true);
							}
						} ]
					}).dialog("open");
				}
			else
				{
				var newDialogDiv = jQuery(document.createElement('div'));
						jQuery(newDialogDiv).html('<span><b style="color:red;">Fiscal Year Already Exist.</b></span>');
						jQuery(newDialogDiv).dialog({
							modal : true,
							width : 300,
							height : 150,
							title : "Information.",
							buttons : [ {
								text : "OK",
								click : function() {
									$(this).dialog("close");
									jQuery("#closePeriodDialog").dialog("close");
									document.location.reload(true);
								}
							} ]
						}).dialog("open");
					
				}
		}
	});
	
		}
	else
		{
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:Red;">Mandatory Fields are Required.</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 300,
			height : 150,
			title : "Error.",
			buttons : [ {
				text : "OK",
				click : function() {
					$(this).dialog("close");
					jQuery("#closePeriodDialog").dialog("close");
				}
			} ]
		}).dialog("open");
		
		}

}


function savecoFiscalPeriods()
{
	
	var jsonList1='';
	var jsonSt="[";
	var jsonEnd="]";
	
            for(a=1;a<=13;a++)
            {
            	
                    if(a == 1)
                             jsonList1 =jsonSt+"{\"period\":\""+a+"\",\"periodstartdate\":\""+$("#txt_periodstart"+a).val()+"\",\"periodenddate\":\""+$("#txt_periodend"+a).val()+"\"}";
                    else
                             jsonList1 = jsonList1+','+"{\"period\":\""+a+"\",\"periodstartdate\":\""+$("#txt_periodstart"+a).val()+"\",\"periodenddate\":\""+$("#txt_periodend"+a).val()+"\"}";
            
            }
            
            console.log(jsonList1+jsonEnd);
            jsonList1=jsonList1+jsonEnd;
            

	$.ajax({
		url: "./updatePeriods",
		type: "POST",
		data : {"jsonList" : jsonList1,"description":$("#txa_Description").val(),"yearID":$("#drp_yearList option:selected").val(),"periodID":_globalPeriodID,'closesysInfoId' : jQuery("#accountingBasicsId").val()},
		success: function(data) {
			createtpusage('Company-Accounting Cycles','Save Accounting Cycles','Info','Company-Accounting Cycles,Saving Accounting Cycles,yearID:'+$("#drp_yearList option:selected").val()+',periodID:'+_globalPeriodID);
				$("#statusMsg").text("Periods Updated Successfully.")
				
				setTimeout(function(){
				$('#statusMsg').html("");	
				},2000);
		}
	});

}


jQuery(document).ready(function(){
	
	$.ajax({
		url: "./closeFiscalyearValidation",
		type: "POST",
		data : {"currentperiod":$("#currentPeriodId").val(),"currentyear":$("#drp_yearList option:selected").val()},
		success: function(data) {
			$.each(data, function(key, valueMap) {								
				
				if(key=="periodflag")
					{
					_globalPeriodStatus=valueMap;
					}
				else if(key=="yearflag")
					{
					//alert(valueMap);
					if(valueMap=="Disable")
						$("#closefiscalyearid").attr("disabled",true);//alert(valueMap)
					else
						$("#closefiscalyearid").attr("disabled",false);//alert(valueMap)
					}
		});
		}
		});
});


function call13periodbuttonval()
{
	$.ajax({
		url: "./closeFiscalyearValidation",
		type: "POST",
		data : {"currentperiod":$("#currentPeriodId").val(),"currentyear":$("#drp_yearList option:selected").val()},
		success: function(data) {
			$.each(data, function(key, valueMap) {								
				
				if(key=="periodflag")
					{
					_globalPeriodStatus=valueMap;
					}
				else if(key=="yearflag")
					{
					//alert(valueMap);
					if(valueMap=="Disable")
						$("#closefiscalyearid").attr("disabled",true);//alert(valueMap)
					else
						$("#closefiscalyearid").attr("disabled",false);//alert(valueMap)
					}
		});
		}
		});
}


function closefiscalYear()
{
	if(_globalPeriodStatus=="Disable")
		{
		var newDialogDiv = jQuery(document.createElement('div'));
		jQuery(newDialogDiv).html('<span><b style="color:Red;">All Periods must be Closed. Would you like to Close all Open Periods?</b></span>');
		jQuery(newDialogDiv).dialog({
			modal : true,
			width : 400,
			height : 200,
			title : "Error.",
			buttons : [ {
					text : "Yes",
					click : function() {
					$(this).dialog("close");
					$("#LoadingDialog").css({"display":"inline"});
					close13periods();
				}},
				 {
					text : "No",
					click : function() {
					$(this).dialog("close");
				}
			}]
		}).dialog("open");
		}
	else
		{
		close13periods();
		}
}

function close13periods()
{
	
	$.ajax({
		url: "./closeAllopenperiods",
		type: "POST",
		data : {"currentyear":$("#drp_yearList option:selected").val()},
		success: function(data) {
			$("#LoadingDialog").css({"display":"none"});
			document.location.reload(true);
		},
		completed:function(data)
		{
			
		}
	});

}

