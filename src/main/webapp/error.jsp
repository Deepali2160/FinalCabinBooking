<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8d7da;
            color: #721c24;
            padding: 30px;
        }
        .error-box {
            background: #f5c6cb;
            border: 1px solid #f5c2c7;
            padding: 20px;
            border-radius: 6px;
        }
    </style>
</head>
<body>
    <div class="error-box">
        <h2>Oops! Something went wrong.</h2>
        <p><%= request.getAttribute("errorMessage") %></p>
        <p><a href="index.jsp">Return to Home</a></p>
    </div>
</body>
</html>
