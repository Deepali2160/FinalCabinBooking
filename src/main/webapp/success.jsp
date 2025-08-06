<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yash.cabinbooking.model.Booking" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Booking Confirmed - Cabin Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <meta http-equiv="refresh" content="10;url=booking?action=list">
    <style>
        .success-animation {
            animation: bounceIn 1s;
        }

        @keyframes bounceIn {
            0%, 20%, 40%, 60%, 80% {
                transform: translateY(0);
            }
            10% {
                transform: translateY(-10px);
            }
            30% {
                transform: translateY(-5px);
            }
        }

        .success-card {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 15px;
            border: 3px solid #28a745;
        }

        .success-icon {
            background: linear-gradient(45deg, #28a745, #20c997);
            width: 80px;
            height: 80px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
        }

        .booking-details {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
        }

        .action-buttons .btn {
            margin: 5px;
            min-width: 200px;
        }

        .timeline {
            position: relative;
            padding-left: 30px;
        }

        .timeline::before {
            content: '';
            position: absolute;
            left: 15px;
            top: 0;
            bottom: 0;
            width: 2px;
            background: #28a745;
        }

        .timeline-item {
            position: relative;
            margin-bottom: 20px;
        }

        .timeline-item::before {
            content: '';
            position: absolute;
            left: -22px;
            top: 5px;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #28a745;
        }

        .confetti {
            position: fixed;
            width: 10px;
            height: 10px;
            background: #ff6b6b;
            animation: confetti-fall 3s linear infinite;
        }

        @keyframes confetti-fall {
            to {
                transform: translateY(100vh) rotate(360deg);
            }
        }

        .countdown-container {
            background: linear-gradient(45deg, #007bff, #0056b3);
            color: white;
            padding: 15px;
            border-radius: 10px;
            margin: 20px 0;
            text-align: center;
        }

        .countdown {
            font-size: 2rem;
            font-weight: bold;
        }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="cabins">
                <i class="fas fa-home"></i> Cabin Booking
            </a>
        </div>
    </nav>

    <%
        String bookingId = request.getParameter("bookingId");
        String transactionId = request.getParameter("transactionId");
        Booking paymentBooking = (Booking) session.getAttribute("paymentBooking");

        // Get current date/time for display
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    %>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <!-- Success Message -->
                <div class="card success-card success-animation">
                    <div class="card-body text-center p-5">
                        <div class="success-icon">
                            <i class="fas fa-check fa-2x text-white"></i>
                        </div>

                        <h1 class="text-success mb-3">Payment Successful!</h1>
                        <h4 class="text-muted mb-4">Your cabin booking is confirmed</h4>

                        <div class="alert alert-success" role="alert">
                            <i class="fas fa-info-circle"></i>
                            <strong>Transaction ID:</strong> <%= transactionId != null ? transactionId : "N/A" %>
                        </div>
                    </div>
                </div>

                <!-- Auto-redirect Countdown -->
                <div class="countdown-container">
                    <h5><i class="fas fa-clock"></i> Auto-redirect to My Bookings</h5>
                    <div class="countdown" id="countdown">10</div>
                    <p class="mb-0">You will be automatically redirected to your bookings page</p>
                    <button class="btn btn-light btn-sm mt-2" onclick="cancelRedirect()">
                        <i class="fas fa-stop"></i> Cancel Auto-redirect
                    </button>
                </div>

                <!-- Booking Details -->
                <div class="card mt-4">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0"><i class="fas fa-receipt"></i> Booking Details</h4>
                    </div>
                    <div class="card-body">
                        <div class="booking-details">
                            <div class="row">
                                <div class="col-md-6">
                                    <h6><i class="fas fa-hashtag text-primary"></i> Booking ID</h6>
                                    <p class="fw-bold">#<%= bookingId != null ? bookingId : "N/A" %></p>

                                    <h6><i class="fas fa-home text-info"></i> Cabin</h6>
                                    <p class="text-muted">
                                        <% if (paymentBooking != null) { %>
                                            Cabin ID: <%= paymentBooking.getCabinId() %>
                                        <% } else { %>
                                            Luxury Cabin
                                        <% } %>
                                    </p>

                                    <h6><i class="fas fa-calendar text-success"></i> Check-in</h6>
                                    <p class="text-muted">
                                        <% if (paymentBooking != null && paymentBooking.getStartDate() != null) { %>
                                            <%= paymentBooking.getStartDate().format(formatter) %>
                                        <% } else { %>
                                            <%= now.format(dateFormatter) %>
                                        <% } %>
                                    </p>

                                    <h6><i class="fas fa-calendar text-warning"></i> Check-out</h6>
                                    <p class="text-muted">
                                        <% if (paymentBooking != null && paymentBooking.getEndDate() != null) { %>
                                            <%= paymentBooking.getEndDate().format(formatter) %>
                                        <% } else { %>
                                            <%= tomorrow.format(dateFormatter) %>
                                        <% } %>
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <h6><i class="fas fa-users text-primary"></i> Guests</h6>
                                    <p class="text-muted">
                                        <%= paymentBooking != null ? paymentBooking.getGuests() : "2" %> guest(s)
                                    </p>

                                    <h6><i class="fas fa-dollar-sign text-success"></i> Amount Paid</h6>
                                    <p class="fw-bold text-success fs-4">
                                        ₹<%= paymentBooking != null ? String.format("%.2f", paymentBooking.getAmount()) : "1500.00" %>
                                    </p>

                                    <h6><i class="fas fa-credit-card text-info"></i> Payment Status</h6>
                                    <span class="badge bg-success fs-6">Paid</span>

                                    <h6 class="mt-3"><i class="fas fa-clipboard-check text-warning"></i> Booking Status</h6>
                                    <span class="badge bg-primary fs-6">Confirmed</span>

                                    <h6 class="mt-3"><i class="fas fa-clock text-secondary"></i> Payment Date</h6>
                                    <p class="text-muted">
                                        <%= now.format(formatter) %>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Next Steps -->
                <div class="card mt-4">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-tasks"></i> What's Next?</h5>
                    </div>
                    <div class="card-body">
                        <div class="timeline">
                            <div class="timeline-item">
                                <h6><i class="fas fa-envelope"></i> Confirmation Email</h6>
                                <p class="text-muted">You will receive a confirmation email with your booking details and e-ticket.</p>
                            </div>
                            <div class="timeline-item">
                                <h6><i class="fas fa-mobile-alt"></i> SMS Updates</h6>
                                <p class="text-muted">We'll send you SMS updates about your booking and check-in details.</p>
                            </div>
                            <div class="timeline-item">
                                <h6><i class="fas fa-key"></i> Check-in Process</h6>
                                <p class="text-muted">Arrive at the cabin 15 minutes before your check-in time with a valid ID.</p>
                            </div>
                            <div class="timeline-item">
                                <h6><i class="fas fa-headset"></i> 24/7 Support</h6>
                                <p class="text-muted">Our customer support is available round the clock for any assistance.</p>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Buttons -->
                <div class="text-center action-buttons mt-4 mb-5">
                    <a href="booking?action=list" class="btn btn-primary btn-lg">
                        <i class="fas fa-list"></i> View My Bookings
                    </a>
                    <a href="booking?action=view&id=<%= bookingId != null ? bookingId : "" %>" class="btn btn-info btn-lg">
                        <i class="fas fa-eye"></i> View Booking Details
                    </a>
                    <a href="cabins" class="btn btn-success btn-lg">
                        <i class="fas fa-plus"></i> Book Another Cabin
                    </a>
                    <button class="btn btn-secondary btn-lg" onclick="printReceipt()">
                        <i class="fas fa-print"></i> Print Receipt
                    </button>
                </div>

                <!-- Contact Information -->
                <div class="card mt-4">
                    <div class="card-body text-center">
                        <h5><i class="fas fa-phone"></i> Need Help?</h5>
                        <p class="text-muted">Contact our customer support team</p>
                        <div class="row">
                            <div class="col-md-4">
                                <i class="fas fa-phone-alt text-primary"></i>
                                <p><strong>Phone</strong><br>+91 12345 67890</p>
                            </div>
                            <div class="col-md-4">
                                <i class="fas fa-envelope text-success"></i>
                                <p><strong>Email</strong><br>support@cabinbooking.com</p>
                            </div>
                            <div class="col-md-4">
                                <i class="fas fa-comments text-info"></i>
                                <p><strong>Live Chat</strong><br>24/7 Available</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let redirectTimer;
        let countdownInterval;
        let timeLeft = 10;

        // Create confetti effect
        function createConfetti() {
            const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#f9ca24', '#f0932b', '#eb4d4b', '#6ab04c'];

            for (let i = 0; i < 30; i++) {
                setTimeout(function() {
                    const confetti = document.createElement('div');
                    confetti.className = 'confetti';
                    confetti.style.left = Math.random() * 100 + 'vw';
                    confetti.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                    confetti.style.animationDelay = Math.random() * 3 + 's';
                    confetti.style.animationDuration = (Math.random() * 3 + 2) + 's';
                    document.body.appendChild(confetti);

                    setTimeout(function() {
                        if (confetti.parentNode) {
                            confetti.remove();
                        }
                    }, 5000);
                }, i * 100);
            }
        }

        // Countdown and auto-redirect functionality
        function startCountdown() {
            const countdownElement = document.getElementById('countdown');

            countdownInterval = setInterval(function() {
                timeLeft--;
                if (countdownElement) {
                    countdownElement.textContent = timeLeft;
                }

                if (timeLeft <= 0) {
                    clearInterval(countdownInterval);
                    window.location.href = 'booking?action=list';
                }
            }, 1000);

            redirectTimer = setTimeout(function() {
                window.location.href = 'booking?action=list';
            }, 10000);
        }

        // Cancel auto-redirect
        function cancelRedirect() {
            clearTimeout(redirectTimer);
            clearInterval(countdownInterval);
            const container = document.querySelector('.countdown-container');
            if (container) {
                container.innerHTML = '<h5><i class="fas fa-check"></i> Auto-redirect Cancelled</h5><p class="mb-0">You can now browse at your own pace</p>';
            }
        }

        // Print function
        function printReceipt() {
            const printWindow = window.open('', '_blank');
            const bookingId = '<%= bookingId != null ? bookingId : "N/A" %>';
            const transactionId = '<%= transactionId != null ? transactionId : "N/A" %>';
            const amount = '<%= paymentBooking != null ? String.format("%.2f", paymentBooking.getAmount()) : "0.00" %>';
            const currentDate = new Date();

            printWindow.document.write([
                '<html>',
                '<head>',
                '    <title>Booking Receipt</title>',
                '    <style>',
                '        body {',
                '            font-family: Arial, sans-serif;',
                '            margin: 20px;',
                '            line-height: 1.6;',
                '        }',
                '        .header {',
                '            text-align: center;',
                '            border-bottom: 2px solid #000;',
                '            padding-bottom: 10px;',
                '            margin-bottom: 20px;',
                '        }',
                '        .details { margin: 20px 0; }',
                '        .row {',
                '            display: flex;',
                '            justify-content: space-between;',
                '            margin: 10px 0;',
                '            padding: 5px 0;',
                '            border-bottom: 1px dotted #ccc;',
                '        }',
                '        .amount {',
                '            font-size: 1.5em;',
                '            font-weight: bold;',
                '            color: #28a745;',
                '        }',
                '        .footer {',
                '            text-align: center;',
                '            margin-top: 30px;',
                '            border-top: 1px solid #ccc;',
                '            padding-top: 15px;',
                '        }',
                '    </style>',
                '</head>',
                '<body>',
                '    <div class="header">',
                '        <h1>Cabin Booking Receipt</h1>',
                '        <p><strong>Payment Confirmation</strong></p>',
                '    </div>',
                '    <div class="details">',
                '        <div class="row">',
                '            <span><strong>Booking ID:</strong></span>',
                '            <span>#' + bookingId + '</span>',
                '        </div>',
                '        <div class="row">',
                '            <span><strong>Transaction ID:</strong></span>',
                '            <span>' + transactionId + '</span>',
                '        </div>',
                '        <div class="row">',
                '            <span><strong>Payment Date:</strong></span>',
                '            <span>' + currentDate.toLocaleDateString() + ' ' + currentDate.toLocaleTimeString() + '</span>',
                '        </div>',
                '        <div class="row">',
                '            <span><strong>Amount Paid:</strong></span>',
                '            <span class="amount">₹' + amount + '</span>',
                '        </div>',
                '        <div class="row">',
                '            <span><strong>Payment Status:</strong></span>',
                '            <span style="color: #28a745; font-weight: bold;">PAID</span>',
                '        </div>',
                '        <div class="row">',
                '            <span><strong>Booking Status:</strong></span>',
                '            <span style="color: #007bff; font-weight: bold;">CONFIRMED</span>',
                '        </div>',
                '    </div>',
                '    <div class="footer">',
                '        <p><strong>Thank you for choosing our cabin booking service!</strong></p>',
                '        <p>For support: +91 12345 67890 | support@cabinbooking.com</p>',
                '    </div>',
                '</body>',
                '</html>'
            ].join(''));

            printWindow.document.close();
            printWindow.print();
        }

        // Initialize on page load
        window.addEventListener('load', function() {
            createConfetti();
            startCountdown();

            // Show success notification
            const successAlert = document.createElement('div');
            successAlert.className = 'alert alert-success alert-dismissible fade show position-fixed';
            successAlert.style.top = '20px';
            successAlert.style.right = '20px';
            successAlert.style.zIndex = '9999';
            successAlert.style.maxWidth = '350px';
            successAlert.innerHTML = [
                '<i class="fas fa-check-circle"></i>',
                '<strong>Success!</strong> Your payment has been processed and booking is confirmed.',
                '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>'
            ].join(' ');
            document.body.appendChild(successAlert);

            setTimeout(function() {
                if (successAlert.parentNode) {
                    successAlert.remove();
                }
            }, 8000);
        });

        // Clean up session data
        <%
            // Clean up session attributes
            session.removeAttribute("paymentBooking");
            session.removeAttribute("paymentResult");
            session.removeAttribute("currentBooking");
            session.removeAttribute("currentCabin");
            session.removeAttribute("duration");
        %>
    </script>
</body>
</html>
