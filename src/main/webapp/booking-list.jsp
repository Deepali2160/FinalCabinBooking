<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.yash.cabinbooking.model.Booking" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Bookings - Cabin Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .booking-card {
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s;
        }
        .booking-card:hover {
            transform: translateY(-2px);
        }
        .status-badge {
            font-size: 0.8em;
            padding: 4px 8px;
        }
        .paid { color: #28a745; }
        .unpaid { color: #dc3545; }
        .pending { color: #ffc107; }
        .confirmed { color: #28a745; }
        .cancelled { color: #6c757d; }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="cabins">
                <i class="fas fa-home"></i> Cabin Booking
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="booking?action=list">
                    <i class="fas fa-list"></i> My Bookings
                </a>
                <a class="nav-link" href="cabins">
                    <i class="fas fa-search"></i> Book New Cabin
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h2><i class="fas fa-calendar-alt"></i> My Bookings</h2>
                    <a href="cabins" class="btn btn-primary">
                        <i class="fas fa-plus"></i> New Booking
                    </a>
                </div>

                <%
                    List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");

                    if (bookings != null && !bookings.isEmpty()) {
                        for (Booking booking : bookings) {
                            String paymentStatusClass = "unpaid".equals(booking.getPaymentStatus()) ? "unpaid" : "paid";
                            String bookingStatusClass = "confirmed".equals(booking.getStatus()) ? "confirmed" :
                                                      "cancelled".equals(booking.getStatus()) ? "cancelled" : "pending";
                %>
                <!-- Booking Card -->
                <div class="card booking-card mb-3">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            <i class="fas fa-bookmark"></i> Booking #<%= booking.getId() %>
                        </h5>
                        <div>
                            <span class="badge bg-secondary status-badge">
                                <%= booking.getStatus().toUpperCase() %>
                            </span>
                            <span class="badge status-badge <%= "unpaid".equals(booking.getPaymentStatus()) ? "bg-danger" : "bg-success" %>">
                                <%= booking.getPaymentStatus().toUpperCase() %>
                            </span>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6><i class="fas fa-home text-primary"></i> Cabin Details</h6>
                                <p class="text-muted mb-2">
                                    <strong>Cabin ID:</strong> <%= booking.getCabinId() %><br>
                                    <strong>Guests:</strong> <%= booking.getGuests() %> guest<%= booking.getGuests() > 1 ? "s" : "" %>
                                </p>

                                <h6><i class="fas fa-calendar text-info"></i> Duration</h6>
                                <p class="text-muted mb-2">
                                    <strong>Check-in:</strong> <%= booking.getStartDate().format(formatter) %><br>
                                    <strong>Check-out:</strong> <%= booking.getEndDate().format(formatter) %>
                                </p>
                            </div>
                            <div class="col-md-6">
                                <h6><i class="fas fa-rupee-sign text-success"></i> Payment Information</h6>
                                <p class="text-muted mb-2">
                                    <strong>Amount:</strong> <span class="h5 text-success">₹<%= String.format("%.2f", booking.getAmount()) %></span><br>
                                    <strong>Payment Status:</strong>
                                    <span class="<%= paymentStatusClass %>">
                                        <i class="fas fa-<%= "unpaid".equals(booking.getPaymentStatus()) ? "times-circle" : "check-circle" %>"></i>
                                        <%= booking.getPaymentStatus().toUpperCase() %>
                                    </span>
                                </p>

                                <h6><i class="fas fa-info-circle text-secondary"></i> Booking Status</h6>
                                <p class="<%= bookingStatusClass %>">
                                    <i class="fas fa-<%= "confirmed".equals(booking.getStatus()) ? "check-circle" :
                                                      "cancelled".equals(booking.getStatus()) ? "times-circle" : "clock" %>"></i>
                                    <%= booking.getStatus().toUpperCase() %>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex justify-content-between">
                        <small class="text-muted">
                            <i class="fas fa-clock"></i>
                            Booked on: <%= booking.getCreatedAt() != null ? booking.getCreatedAt().format(formatter) : "N/A" %>
                        </small>
                        <div>
                            <!-- View Details Button -->
                            <a href="booking?action=view&id=<%= booking.getId() %>" class="btn btn-outline-info btn-sm">
                                <i class="fas fa-eye"></i> View Details
                            </a>

                            <!-- Pay Now Button for Unpaid Bookings -->
                            <% if ("unpaid".equals(booking.getPaymentStatus()) &&
                                   ("pending".equals(booking.getStatus()) || "confirmed".equals(booking.getStatus()))) { %>
                                <a href="payment?action=show&bookingId=<%= booking.getId() %>" class="btn btn-success btn-sm">
                                    <i class="fas fa-credit-card"></i> Pay Now
                                </a>
                            <% } %>

                            <!-- Cancel Button for Pending/Confirmed Bookings -->
                            <% if (("pending".equals(booking.getStatus()) || "confirmed".equals(booking.getStatus())) &&
                                   !"cancelled".equals(booking.getStatus())) { %>
                                <button class="btn btn-outline-danger btn-sm" onclick="cancelBooking(<%= booking.getId() %>)">
                                    <i class="fas fa-times"></i> Cancel
                                </button>
                            <% } %>
                        </div>
                    </div>
                </div>
                <%
                        }
                    } else {
                %>
                <!-- No Bookings Found -->
                <div class="card booking-card text-center">
                    <div class="card-body py-5">
                        <i class="fas fa-calendar-times fa-4x text-muted mb-3"></i>
                        <h4 class="text-muted">No Bookings Found</h4>
                        <p class="text-muted">You haven't made any bookings yet. Start by booking your first cabin!</p>
                        <a href="cabins" class="btn btn-primary btn-lg">
                            <i class="fas fa-search"></i> Browse Cabins
                        </a>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function cancelBooking(bookingId) {
            if (confirm('Are you sure you want to cancel this booking?')) {
                window.location.href = 'booking?action=cancel&id=' + bookingId;
            }
        }

        // Auto-refresh every 30 seconds to update payment status
        setTimeout(function() {
            location.reload();
        }, 30000);

        // Show success message if redirected from payment
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get('status');
        if (status === 'payment_success') {
            const alert = document.createElement('div');
            alert.className = 'alert alert-success alert-dismissible fade show';
            alert.innerHTML = `
                <i class="fas fa-check-circle"></i>
                <strong>Payment Successful!</strong> Your booking has been confirmed.
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            `;
            document.querySelector('.container').insertBefore(alert, document.querySelector('.row'));
        }
    </script>
</body>
</html>
