<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<body>
<h1>Upload a file for OCR:</h1>
<form method="POST" action="/upload" enctype="multipart/form-data">
    <input type="file" name="file"/><br/><br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
