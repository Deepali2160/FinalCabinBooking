<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Pending Bookings - Admin Approval</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css" />
</head>
<body>
    <h2>Pending Booking Approvals</h2>

    <c:if test="${not empty message}">
        <p style="color:green;">${message}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p style="color:red;">${error}</p>
    </c:if>

    <c:choose>
        <c:when test="${empty pendingBookings}">
            <p>No bookings pending approval.</p>
        </c:when>
        <c:otherwise>
            <table border="1" cellpadding="8" cellspacing="0">
                <thead>
                    <tr>
                        <th>Booking ID</th>
                        <th>User</th>
                        <th>Cabin</th>
                        <th>Booking Dates</th>
                        <th>Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="booking" items="${pendingBookings}">
                        <tr>
                            <td>${booking.id}</td>
                            <td>${booking.userName} (${booking.userEmail})</td>
                            <td>${booking.cabinName}</td>
                            <td>${booking.startDate} to ${booking.endDate}</td>
                            <td>${booking.amount}</td>
                            <td>
                                <a href="booking-approval?action=view&id=${booking.id}">View & Approve/Reject</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <p><a href="${pageContext.request.contextPath}/admin/dashboard">Back to Dashboard</a></p>
</body>
</html>
