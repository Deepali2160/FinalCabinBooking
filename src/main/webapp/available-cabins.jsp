<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.cabinbooking.model.Cabin" %>
<html>
<head>
    <title>Available Cabins</title>
    <style>
        .cabin-card {
            border: 1px solid #ccc;
            padding: 16px;
            margin: 12px;
            border-radius: 8px;
            background-color: #f9f9f9;
        }

        .book-btn {
            display: inline-block;
            margin-top: 10px;
            padding: 8px 12px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .book-btn:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h2>Available Cabins</h2>
    <%
        List<Cabin> cabins = (List<Cabin>) request.getAttribute("cabins");
        if (cabins != null && !cabins.isEmpty()) {
            for (Cabin cabin : cabins) {
    %>
        <div class="cabin-card">
            <h3><%= cabin.getName() %></h3>
            <p><b>Location:</b> <%= cabin.getLocation() %></p>
            <p><b>Price/Hour:</b> ₹<%= cabin.getHourlyRate() %></p>
            <p><b>Capacity:</b> <%= cabin.getCapacity() %></p>
            <p><b>Amenities:</b> <%= cabin.getAmenities() %></p>
            <img src="<%= cabin.getImageUrl() %>" alt="Cabin Image" width="200px" />

            <!-- ✅ Book Now button -->
            <br/>
           <a href="cabins?action=book&id=<%= cabin.getId() %>" class="book-btn">Book Now</a>

        </div>
    <%
            }
        } else {
    %>
        <p>No available cabins found.</p>
    <%
        }
    %>
</body>
</html>