<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Booking Approval - Booking ID ${booking.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css" />
</head>
<body>
    <h2>Booking Approval - Booking ID ${booking.id}</h2>

    <table border="1" cellpadding="6">
        <tr><th>User</th><td>${booking.userName} (${booking.userEmail})</td></tr>
        <tr><th>Cabin</th><td>${booking.cabinName}</td></tr>
        <tr><th>Start Date</th><td>${booking.startDate}</td></tr>
        <tr><th>End Date</th><td>${booking.endDate}</td></tr>
        <tr><th>Guests</th><td>${booking.guests}</td></tr>
        <tr><th>Amount</th><td>${booking.amount}</td></tr>
        <tr><th>Status</th><td>${booking.status}</td></tr>
        <tr><th>Payment Status</th><td>${booking.paymentStatus}</td></tr>
        <tr><th>Approval Status</th><td>${booking.approvalStatus}</td></tr>
    </table>

    <h3>Admin Action</h3>
    <form method="POST" action="booking-approval">
        <input type="hidden" name="bookingId" value="${booking.id}" />

        <label for="adminRemarks">Remarks (optional):</label><br/>
        <textarea name="adminRemarks" id="adminRemarks" rows="3" cols="50"></textarea><br/><br/>

        <label for="rejectionReason">If Rejecting, reason:</label><br/>
        <textarea name="rejectionReason" id="rejectionReason" rows="3" cols="50"></textarea><br/><br/>

        <button type="submit" name="action" value="approve">Approve</button>
        <button type="submit" name="action" value="reject">Reject</button>
    </form>

    <p><a href="booking-approval?action=list">Back to Pending Bookings</a></p>
</body>
</html>

