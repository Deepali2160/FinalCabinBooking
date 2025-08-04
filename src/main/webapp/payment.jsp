<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    int cabinId = Integer.parseInt(request.getParameter("cabinId"));
    String cabinName = request.getParameter("cabinName");
    int userId = Integer.parseInt(request.getParameter("userId"));
    double amount = Double.parseDouble(request.getParameter("amount"));
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    int guests = Integer.parseInt(request.getParameter("guests"));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Payment Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            padding: 30px;
            background-color: #f5f5f5;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        form {
            max-width: 450px;
            margin: 30px auto;
            background: white;
            padding: 25px 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px #ccc;
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
            margin-top: 15px;
        }
        input {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #aaa;
            border-radius: 5px;
        }
        button {
            background-color: #28a745;
            color: white;
            padding: 12px;
            border: none;
            width: 100%;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background-color: #218838;
        }
        .amount-box {
            text-align: center;
            margin-bottom: 20px;
            font-size: 18px;
            color: #444;
        }
    </style>
</head>
<body>

    <h2>Payment for <%= cabinName %></h2>

    <div class="amount-box">
        <strong>Total Amount:</strong> ₹<%= amount %>
    </div>

    <form action="payment" method="post">
        <label>Card Number:</label>
        <input type="text" name="cardNumber" pattern="[0-9]{16}" maxlength="16" required placeholder="Enter 16-digit card number">

        <label>Expiry Date:</label>
        <input type="month" name="expiry" required>

        <label>CVV:</label>
        <input type="text" name="cvv" pattern="[0-9]{3}" maxlength="3" required placeholder="Enter 3-digit CVV">

        <!-- Hidden booking info -->
        <input type="hidden" name="cabinId" value="<%= cabinId %>">
        <input type="hidden" name="cabinName" value="<%= cabinName %>">
        <input type="hidden" name="userId" value="<%= userId %>">
        <input type="hidden" name="amount" value="<%= amount %>">
        <input type="hidden" name="startDate" value="<%= startDate %>">
        <input type="hidden" name="endDate" value="<%= endDate %>">
        <input type="hidden" name="guests" value="<%= guests %>">

        <button type="submit">Pay Now</button>
    </form>
</body>
</html>
