<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
<script src="./../resources/scripts/turbo_scripts/ckeditor/ckeditor.js"></script>

<title>Insert title here</title>
</head>
<body>
<textarea name="editor1" class="editor1" id="editor1"></textarea>
        <script>
           var editor=CKEDITOR.replace( 'editor1' );
        </script>
</body>
</html>