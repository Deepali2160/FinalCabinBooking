<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.yash.cabinbooking.model.User" %>
<%
    int cabinId = Integer.parseInt(request.getParameter("cabinId"));
    String cabinName = request.getParameter("cabinName");
    double pricePerNight = Double.parseDouble(request.getParameter("pricePerNight"));

    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    int userId = user.getId();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Book Cabin</title>
    <script>
        function calculateAmount() {
            const start = new Date(document.getElementById("startDate").value);
            const end = new Date(document.getElementById("endDate").value);
            const pricePerNight = <%= pricePerNight %>;

            if (start && end && !isNaN(start.getTime()) && !isNaN(end.getTime())) {
                const timeDiff = end - start;
                const days = Math.ceil(timeDiff / (1000 * 60 * 60 * 24));

                if (days > 0) {
                    const amount = days * pricePerNight;
                    document.getElementById("amountDisplay").value = amount; // visible
                    document.getElementById("amount").value = amount;         // hidden
                } else {
                    document.getElementById("amountDisplay").value = 0;
                    document.getElementById("amount").value = 0;
                }
            }
        }
    </script>
</head>
<body>
    <h2>Book: <%= cabinName %></h2>
    <form action="payment.jsp" method="post">
        <!-- Hidden fields to pass data -->
        <input type="hidden" name="cabinId" value="<%= cabinId %>"/>
        <input type="hidden" name="cabinName" value="<%= cabinName %>"/>
        <input type="hidden" name="userId" value="<%= userId %>"/>
        <input type="hidden" name="pricePerNight" value="<%= pricePerNight %>"/>
        <input type="hidden" id="amount" name="amount"/>

        Start Date: <input type="date" id="startDate" name="startDate" onchange="calculateAmount()" required><br><br>
        End Date: <input type="date" id="endDate" name="endDate" onchange="calculateAmount()" required><br><br>
        Guests: <input type="number" name="guests" required><br><br>

        Amount: <input type="number" id="amountDisplay" readonly><br><br>

        <button type="submit">Confirm Booking</button>
    </form>
</body>
</html>