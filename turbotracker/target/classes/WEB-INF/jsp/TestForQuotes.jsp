<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=100" >
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>TurboPro - Customers List</title>
		<style type="text/css">
			input#customerNameHeader {width:400px;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;border:1px solid #D3D3D3;padding:3px;}
			input#customerNameHeader:focus{border:1px solid #637C92;border-radius:5px;-moz-border-radius:5px;-webkit-border-radius:5px;outline:0px;}
			.formError .formErrorContent{ background: none repeat scroll 0 0 #A90F16 }
			.formError .formErrorArrow div { background: none repeat scroll 0 0 #A90F16 }
			#add { display: none; }
			#mainMenuCompanyPage {text-decoration:none;color:#FFFFFF; background-color: #003961;}
			#mainMenuCompanyPage a{background:url('./../resources/styles/turbo-css/images/turbo_app_company_hover_icon.png') no-repeat 0px 4px;color:#FFF}
			#mainMenuCompanyPage ul li a{background: none; }
			#search{display: none;}
			.ui-jqgrid-bdiv{height: 500px !important;}
			.mce-tinymce mce-container mce-panel{float:right;}
			
			
		</style>
	</head>
	<body>
		<div  style="background-color: #FAFAFA">
			<div>
				 <jsp:include page="./headermenu.jsp"></jsp:include> 
			</div>
		
		<table style="width:600px;margin:0 auto;">
				
			<tr>
				<td colspan="2">
					<div id="Test" style="padding-left: 0px;">
						<table style="padding-left:0px" id="rowed3" class="scroll"></table>
						<div id="prowed3" class="scroll" style="text-align:right;"></div>
					</div>
				</td>
			</tr>
			 <tr>
				<td colspan="2">
				<textarea name="content" id="content" style="width:100%" class="myTextEditor"></textarea>
				</td>
			</tr>
		</table>
		<div style="height: 20px;"></div>
		<table id="footer">
		<tr>
			<td colspan="2">
				<div class="footer-div"><jsp:include page="./footer.jsp" /></div>
			</td>
		</tr>
	</table>
	</div>
	<script type="text/javascript" src="./../resources/scripts/turbo_scripts/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
tinymce.init({
    selector: "textarea",
    theme: "modern",
    plugins: [
         "advlist autolink lists link image charmap print preview hr anchor pagebreak",
        "searchreplace wordcount visualblocks visualchars code fullscreen",
        "insertdatetime media nonbreaking save table contextmenu directionality",
        " template paste textcolor colorpicker textpattern"
    ],
    toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor fontselect fontsizeselect",
   	menubar: false,
    toolbar_items_size: 'small',
    forced_root_block : 'div'
   
});
</script>
	<script>
	
	$(function(){
	jQuery("#rowed3").jqGrid({
	   	//url:'',
		datatype: "json",
	   	colNames:['','','',''],
	   	colModel:[
	   		{name:'title',index:'title',edittype:'select', width:10,editoptions:{
		   		value:{0:'--none--',1:'Title',2:'Item2',3:'Item3',4:'Price'},
	   		dataEvents: [{
                             type: 'change',
                             fn: function (e) {
                                if(this.value==1){
                                	tinymce.remove(".titleTextEditor");
                                    $(".qty_textbox").css('display', 'none');
                                    $("#title_editor").css('display', 'block');
                                    $("#pricelabelid").css('display', 'none');
                                    $("#price_textbox").css('display', 'none');
                                	initializetinymce(650);
                                }else if(this.value==2){
                                	tinymce.remove(".titleTextEditor");
                                    //$("#item2_textbox").css('display', 'block');
                                   // $("#title_editor").css('display', 'block');
                                   // $("#pricelabelid").css('display', 'none');
                                   // $("#price_textbox").css('display', 'none');
                                	initializetinymce(410);
                                }else if(this.value==3){
                                	tinymce.remove(".titleTextEditor");
                                   // $("#item2_textbox").css('display', 'block');
                                  //  $("#title_editor").css('display', 'block');
                                  //  $("#pricelabelid").css('display', 'none');
                                   // $("#price_textbox").css('display', 'block');
                                	initializetinymce(300);
                                }else if(this.value==4){
                                	tinymce.remove(".titleTextEditor");
                                   // $("#item2_textbox").css('display', 'none');
                                  //  $("#title_editor").css('display', 'none');
                                  //  $("#pricelabelid").css('display', 'block');
                                  //  $("#price_textbox").css('display', 'block');
                                }else{
                                	tinymce.remove(".titleTextEditor");
                                    $("#item2_textbox").css('display', 'none');
                                     $("#title_editor").css('display', 'block');
                                     $("#pricelabelid").css('display', 'none');
                                     $("#price_textbox").css('display', 'none');
                                    }
                                
                                
                             }
                         }]	
	   		} ,editable:true},
	   	 { name: "note", width: 100, sortable: false, editable: true,
	   		    //edittype: "textarea"
	   		    edittype:'custom',
	   		    editoptions: {
	   		    custom_element: function (value, options) {
		   		   var elm = $("<input type='text' name='item2_textbox' id='item2_textbox' placeholder='textbox' size='4' style='display:none;' class='qty_textbox'/>"
				   		    +"<textarea id='title_editor' class='titleTextEditor' placeholder='Title' ></textarea>"
				   		  /*  +"<textarea id='item2_editor' class='item2TextEditor'  placeholder='Item2'></textarea>" */
				   		  /*  +"<input type='text' id='item3_textbox' placeholder='textbox'/>" */
				   		   /* +"<textarea id='item3_editor' class='item3TextEditor' placeholder='Item3'/></textarea>" */
				   		   /* +"<input type='text' id='item3_sellprice' placeholder='Item3SellPrice'/>" */
				   		   +"<label id='pricelabelid' style='display:none;'><b>Total Net Price:</b></label><input type='text' id='price_textbox' class='price_textbox' placeholder='textbox' style='display:none;'/>");
				   		  /*  +"<input type='text' id='price_sellprice' placeholder='PriceSellPrice'/>"); */
	   		     elm.val(value);
	   		    // give the editor time to initialize
	   		    /* setTimeout(function () {
					 initializetinymce();
	   		 
	   		    }, 50);  */
	   		    return elm;
	   		    }  ,
	   		    custom_value: function (element, oper, gridval) {
	   		    var id;
	   		    if (element.length > 0) {
	   		    id = element.attr("id");
	   		    } else if (typeof element.selector === "string") {
	   		    var sels = element.selector.split(" "),
	   		    idSel = sels[sels.length - 1];
	   		    if (idSel.charAt(0) === "#") {
	   		    id = idSel.substring(1);
	   		    } else {
	   		    return "";
	   		    }
	   		    }
	   		    if (oper === "get") {
	   		    return tinymce.get(id).getContent({format: "row"});
	   		    } else if (oper === "set") {
	   		    if (tinymce.get(id)) {
	   		    tinymce.get(id).setContent(gridval);
	   		    }
	   		    }
	   		    }}
	   		    },
	   		{name:'cost',index:'cost', width:20,editable:true},
	   		{name:'manufacturer',index:'manufacturer', width:20,editable:true}
	   		
	   		],
	   	rowNum:10,
	   	rowList:[10,20,30],
	   	pager: '#prowed3',
	   	sortname: 'id',
	    viewrecords: true,
	    sortorder: "desc",
	    height:200,	width: 1050,
		//editurl: "server.php",
		caption: "Using navigator"
	});
	jQuery("#rowed3").jqGrid('navGrid',"#prowed3",{edit:false,add:false,del:false});
	jQuery("#rowed3").jqGrid('inlineNav',"#prowed3");
	});

	function initializetinymce(editorwidth){
		 $(".titleTextEditor").css('display', 'block');
		//tinymce.init({selector: "#item2_editor", plugins: "link"});
		  /*try {
	   		    tinymce.remove(".myTextEditor");
	   		    } catch(ex) {} */
	   		    //tinymce.init({selector: "#" + options.id, plugins: "link"});
	   		 tinymce.init({
	   			 mode : "specific_textareas",
	             editor_selector : "titleTextEditor",
	             theme: "modern",
	             width:editorwidth,
	   		 plugins: [
	   		         "advlist autolink lists link image charmap print preview hr anchor pagebreak",
	   		        "searchreplace wordcount visualblocks visualchars code fullscreen",
	   		        "insertdatetime media nonbreaking save table  directionality",
	   		        " template paste textcolor colorpicker textpattern"
	   		    ],
	   		    toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor",
	   		   	menubar: false,
	   		 	statusbar: false,
	   		 	toolbar_items_size: 'small'
	   		    
	   		   
	   		});

	   		 /* tinymce.init({
	   			 mode : "specific_textareas",
	             editor_selector : "item2TextEditor",
	   		    theme: "modern",
	   		 	width:410,
	   		 	plugins: [
	   		         "advlist autolink lists link image charmap print preview hr anchor pagebreak",
	   		        "searchreplace wordcount visualblocks visualchars code fullscreen",
	   		        "insertdatetime media nonbreaking save table  directionality",
	   		        " template paste textcolor colorpicker textpattern"
	   		    ],
	   		    toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor",
	   		   	menubar: false,
	   		 	statusbar: false,
	   		    toolbar_items_size: 'small'
	   		    
	   		   
	   		});

	   		 tinymce.init({
	   			 mode : "specific_textareas",
	             editor_selector : "item3TextEditor",
	   		    theme: "modern",
	   		 plugins: [
	   		         "advlist autolink lists link image charmap print preview hr anchor pagebreak",
	   		        "searchreplace wordcount visualblocks visualchars code fullscreen",
	   		        "insertdatetime media nonbreaking save table  directionality",
	   		        " template paste textcolor colorpicker textpattern"
	   		    ],
	   		    toolbar1: "bold italic underline | alignleft aligncenter alignright alignjustify | numlist bullist | forecolor",
	   		   	menubar: false,
	   		 	statusbar: false,
	   		    toolbar_items_size: 'small'
	   		    
	   		   
	   		});  */
		}
	</script>
	
	</body>
</html>