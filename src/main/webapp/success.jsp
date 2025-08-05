<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    String cabinName = (String) session.getAttribute("cabinName");
    Double amount = (Double) session.getAttribute("amount");

    // Optional: clear session values after use
    session.removeAttribute("cabinName");
    session.removeAttribute("amount");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Booking Successful</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f3f3f3;
            padding: 30px;
            text-align: center;
        }
        .success-box {
            background-color: #fff;
            padding: 25px;
            border-radius: 8px;
            display: inline-block;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .success-box h2 {
            color: #28a745;
        }
        .success-box p {
            font-size: 18px;
            margin: 10px 0;
        }
    </style>
</head>
<body>
<div class="success-box">
    <h2>🎉 Booking Confirmed!</h2>
    <p>Cabin: <strong><%= cabinName %></strong></p>
    <p>Amount Paid: ₹<strong><%= amount %></strong></p>
    <p>Booking Reference: #<%= (int)(Math.random() * 1000000) %></p>
</div>
</body>
</html>