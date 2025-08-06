<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yash.cabinbooking.model.Booking" %>
<%@ page import="com.yash.cabinbooking.model.Cabin" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment - Cabin Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .payment-card {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        .payment-method {
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            cursor: pointer;
            transition: all 0.3s;
        }
        .payment-method:hover {
            border-color: #007bff;
            background-color: #f8f9fa;
        }
        .payment-method.selected {
            border-color: #007bff;
            background-color: #e3f2fd;
        }
        .security-badge {
            background: linear-gradient(45deg, #28a745, #20c997);
            color: white;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .amount-display {
            font-size: 2rem;
            font-weight: bold;
            color: #28a745;
        }
        .payment-details {
            margin-top: 20px;
        }
        .debug-info {
            background: #f8f9fa;
            border: 1px solid #dee2e6;
            border-radius: 5px;
            padding: 10px;
            margin: 10px 0;
            font-size: 12px;
            color: #6c757d;
        }
        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .btn-success:disabled {
            background-color: #6c757d;
            border-color: #6c757d;
        }
        .spinner-border-sm {
            width: 1rem;
            height: 1rem;
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
        Booking booking = (Booking) request.getAttribute("booking");
        Cabin cabin = (Cabin) request.getAttribute("cabin");
        Long duration = (Long) request.getAttribute("duration");
        String error = request.getParameter("error");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm");
    %>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <!-- Debug Information (Remove in production) -->
                <div class="debug-info">
                    <strong>Debug Info:</strong>
                    Booking ID: <%= booking != null ? booking.getId() : "NULL" %> |
                    Amount: <%= booking != null ? booking.getAmount() : "NULL" %> |
                    Status: <%= booking != null ? booking.getPaymentStatus() : "NULL" %>
                </div>

                <!-- Security Badge -->
                <div class="security-badge text-center">
                    <i class="fas fa-shield-alt fa-2x mb-2"></i>
                    <h5>Secure Payment Gateway</h5>
                    <p class="mb-0">Your payment information is encrypted and secure</p>
                </div>

                <%-- Error Messages --%>
                <% if (error != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-triangle"></i>
                        <%
                            switch(error) {
                                case "amount_mismatch":
                                    out.print("Amount mismatch detected! Please try again.");
                                    break;
                                case "payment_failed":
                                    out.print("Payment processing failed. Please try again.");
                                    break;
                                case "invalid_parameters":
                                    out.print("Invalid payment parameters. Please check your details.");
                                    break;
                                case "card_payment_declined":
                                    out.print("Your card payment was declined. Please try another payment method.");
                                    break;
                                case "invalid_card_details":
                                    out.print("Invalid card details. Please check and try again.");
                                    break;
                                case "upi_payment_failed":
                                    out.print("UPI payment failed. Please check your UPI ID and try again.");
                                    break;
                                case "invalid_upi_id":
                                    out.print("Invalid UPI ID format. Please enter a valid UPI ID.");
                                    break;
                                case "net_banking_payment_failed":
                                    out.print("Net banking payment failed. Please try again.");
                                    break;
                                case "wallet_payment_failed":
                                    out.print("Wallet payment failed. Please try again.");
                                    break;
                                case "status_update_failed":
                                    out.print("Payment was processed but status update failed. Please contact support.");
                                    break;
                                default:
                                    out.print("An error occurred: " + error);
                            }
                        %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <% if (booking != null) { %>
                    <!-- Booking Summary -->
                    <div class="card payment-card mb-4">
                        <div class="card-header bg-info text-white">
                            <h4 class="mb-0"><i class="fas fa-receipt"></i> Booking Summary</h4>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <h6><i class="fas fa-home"></i> Cabin</h6>
                                    <p class="text-muted">
                                        <% if (cabin != null) { %>
                                            <%= cabin.getName() %>
                                            <small class="text-secondary d-block">ID: <%= booking.getCabinId() %></small>
                                        <% } else { %>
                                            Cabin ID: <%= booking.getCabinId() %>
                                        <% } %>
                                    </p>

                                    <h6><i class="fas fa-calendar"></i> Check-in</h6>
                                    <p class="text-muted">
                                        <%= booking.getStartDate().format(formatter) %>
                                    </p>

                                    <h6><i class="fas fa-calendar"></i> Check-out</h6>
                                    <p class="text-muted">
                                        <%= booking.getEndDate().format(formatter) %>
                                    </p>
                                </div>
                                <div class="col-md-6">
                                    <h6><i class="fas fa-users"></i> Guests</h6>
                                    <p class="text-muted">
                                        <%= booking.getGuests() %> guest<%= booking.getGuests() > 1 ? "s" : "" %>
                                    </p>

                                    <h6><i class="fas fa-clock"></i> Duration</h6>
                                    <p class="text-muted">
                                        <%= duration != null ? duration : 0 %> hours
                                    </p>

                                    <% if (cabin != null) { %>
                                        <h6><i class="fas fa-tag"></i> Rate</h6>
                                        <p class="text-muted">₹<%= String.format("%.2f", cabin.getHourlyRate()) %> per hour</p>
                                    <% } %>

                                    <h6><i class="fas fa-dollar-sign"></i> Total Amount</h6>
                                    <p class="amount-display">₹<%= String.format("%.2f", booking.getAmount()) %></p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Payment Form -->
                    <div class="card payment-card">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0"><i class="fas fa-credit-card"></i> Payment Details</h4>
                        </div>
                        <div class="card-body">
                            <form action="payment" method="post" id="paymentForm" novalidate>
                                <input type="hidden" name="action" value="process">
                                <input type="hidden" name="bookingId" value="<%= booking.getId() %>">
                                <input type="hidden" name="amount" value="<%= booking.getAmount() %>">

                                <!-- Payment Method Selection -->
                                <div class="mb-4">
                                    <h5>Select Payment Method</h5>

                                    <div class="payment-method selected" data-method="card">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="card" value="card" checked>
                                            <label class="form-check-label w-100" for="card">
                                                <i class="fas fa-credit-card fa-lg text-primary me-2"></i>
                                                <strong>Credit/Debit Card</strong>
                                                <small class="text-muted d-block">Visa, Mastercard, American Express</small>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="payment-method" data-method="upi">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="upi" value="upi">
                                            <label class="form-check-label w-100" for="upi">
                                                <i class="fas fa-mobile-alt fa-lg text-success me-2"></i>
                                                <strong>UPI Payment</strong>
                                                <small class="text-muted d-block">Pay using UPI ID or QR Code</small>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="payment-method" data-method="netbanking">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="netbanking" value="netbanking">
                                            <label class="form-check-label w-100" for="netbanking">
                                                <i class="fas fa-university fa-lg text-info me-2"></i>
                                                <strong>Net Banking</strong>
                                                <small class="text-muted d-block">All major banks supported</small>
                                            </label>
                                        </div>
                                    </div>

                                    <div class="payment-method" data-method="wallet">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="paymentMethod" id="wallet" value="wallet">
                                            <label class="form-check-label w-100" for="wallet">
                                                <i class="fas fa-wallet fa-lg text-warning me-2"></i>
                                                <strong>Digital Wallet</strong>
                                                <small class="text-muted d-block">Paytm, PhonePe, Google Pay</small>
                                            </label>
                                        </div>
                                    </div>
                                </div>

                                <!-- Card Details (shown by default) -->
                                <div id="cardDetails" class="payment-details">
                                    <h6><i class="fas fa-credit-card"></i> Card Information</h6>
                                    <div class="row mb-3">
                                        <div class="col-md-12">
                                            <label for="cardNumber" class="form-label">Card Number</label>
                                            <input type="text" class="form-control" id="cardNumber" name="cardNumber"
                                                   placeholder="1234 5678 9012 3456" maxlength="19" autocomplete="cc-number">
                                            <div class="invalid-feedback" id="cardNumberError"></div>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <div class="col-md-8">
                                            <label for="expiryDate" class="form-label">Expiry Date</label>
                                            <input type="text" class="form-control" id="expiryDate" name="expiryDate"
                                                   placeholder="MM/YY" maxlength="5" autocomplete="cc-exp">
                                            <div class="invalid-feedback" id="expiryDateError"></div>
                                        </div>
                                        <div class="col-md-4">
                                            <label for="cvv" class="form-label">CVV</label>
                                            <input type="password" class="form-control" id="cvv" name="cvv"
                                                   placeholder="123" maxlength="4" autocomplete="cc-csc">
                                            <div class="invalid-feedback" id="cvvError"></div>
                                        </div>
                                    </div>

                                    <div class="mb-3">
                                        <label for="cardHolder" class="form-label">Cardholder Name</label>
                                        <input type="text" class="form-control" id="cardHolder" name="cardHolder"
                                               placeholder="Enter name as on card" autocomplete="cc-name">
                                        <div class="invalid-feedback" id="cardHolderError"></div>
                                    </div>
                                </div>

                                <!-- UPI Details (hidden by default) -->
                                <div id="upiDetails" class="payment-details" style="display: none;">
                                    <h6><i class="fas fa-mobile-alt"></i> UPI Information</h6>
                                    <div class="mb-3">
                                        <label for="upiId" class="form-label">UPI ID</label>
                                        <input type="text" class="form-control" id="upiId" name="upiId"
                                               placeholder="yourname@upi (e.g., john@paytm)">
                                        <div class="invalid-feedback" id="upiIdError"></div>
                                    </div>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle"></i>
                                        Enter your UPI ID in the format: username@bankname
                                    </div>
                                </div>

                                <!-- Net Banking Details (hidden by default) -->
                                <div id="netbankingDetails" class="payment-details" style="display: none;">
                                    <h6><i class="fas fa-university"></i> Bank Information</h6>
                                    <div class="mb-3">
                                        <label for="bank" class="form-label">Select Your Bank</label>
                                        <select class="form-select" id="bank" name="bank">
                                            <option value="">Choose Bank...</option>
                                            <option value="sbi">State Bank of India</option>
                                            <option value="hdfc">HDFC Bank</option>
                                            <option value="icici">ICICI Bank</option>
                                            <option value="axis">Axis Bank</option>
                                            <option value="kotak">Kotak Mahindra Bank</option>
                                            <option value="pnb">Punjab National Bank</option>
                                            <option value="other">Other Bank</option>
                                        </select>
                                        <div class="invalid-feedback" id="bankError"></div>
                                    </div>
                                </div>

                                <!-- Wallet Details (hidden by default) -->
                                <div id="walletDetails" class="payment-details" style="display: none;">
                                    <h6><i class="fas fa-wallet"></i> Wallet Information</h6>
                                    <div class="mb-3">
                                        <label for="walletType" class="form-label">Select Wallet</label>
                                        <select class="form-select" id="walletType" name="walletType">
                                            <option value="">Choose Wallet...</option>
                                            <option value="paytm">Paytm</option>
                                            <option value="phonepe">PhonePe</option>
                                            <option value="googlepay">Google Pay</option>
                                            <option value="amazonpay">Amazon Pay</option>
                                            <option value="mobikwik">MobiKwik</option>
                                            <option value="freecharge">FreeCharge</option>
                                        </select>
                                        <div class="invalid-feedback" id="walletTypeError"></div>
                                    </div>
                                </div>

                                <!-- Terms and Conditions -->
                                <div class="form-check mb-4">
                                    <input class="form-check-input" type="checkbox" id="terms" name="terms">
                                    <label class="form-check-label" for="terms">
                                        I agree to the <a href="#" class="text-primary" target="_blank">Terms and Conditions</a> and
                                        <a href="#" class="text-primary" target="_blank">Privacy Policy</a>
                                    </label>
                                    <div class="invalid-feedback" id="termsError"></div>
                                </div>

                                <!-- Payment Buttons -->
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success btn-lg" id="payButton">
                                        <i class="fas fa-lock"></i> Pay ₹<%= String.format("%.2f", booking.getAmount()) %>
                                    </button>
                                    <a href="booking?action=view&id=<%= booking.getId() %>" class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left"></i> Back to Booking
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>

                <% } else { %>
                    <!-- No Booking Found -->
                    <div class="card payment-card">
                        <div class="card-body text-center">
                            <i class="fas fa-exclamation-triangle fa-3x text-warning mb-3"></i>
                            <h4>Booking Not Found</h4>
                            <p class="text-muted">The booking details could not be loaded. Please try again.</p>
                            <a href="booking?action=list" class="btn btn-primary">
                                <i class="fas fa-list"></i> View My Bookings
                            </a>
                        </div>
                    </div>
                <% } %>

                <!-- Security Information -->
                <div class="text-center mt-4">
                    <small class="text-muted">
                        <i class="fas fa-shield-alt"></i> 256-bit SSL encryption •
                        <i class="fas fa-lock"></i> PCI DSS compliant •
                        <i class="fas fa-check-circle"></i> 100% secure
                    </small>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Initialize payment form functionality
        (function() {
            'use strict';

            // Wait for DOM to be ready
            document.addEventListener('DOMContentLoaded', function() {
                console.log('🚀 Payment form initialized');

                // Get all necessary elements
                const paymentForm = document.getElementById('paymentForm');
                const paymentMethods = document.querySelectorAll('.payment-method');
                const payButton = document.getElementById('payButton');

                // Payment details containers
                const cardDetails = document.getElementById('cardDetails');
                const upiDetails = document.getElementById('upiDetails');
                const netbankingDetails = document.getElementById('netbankingDetails');
                const walletDetails = document.getElementById('walletDetails');

                // Form elements
                const cardNumber = document.getElementById('cardNumber');
                const expiryDate = document.getElementById('expiryDate');
                const cvv = document.getElementById('cvv');
                const terms = document.getElementById('terms');

                // Utility functions
                function showError(element, message) {
                    if (element) {
                        element.classList.add('is-invalid');
                        const errorDiv = document.getElementById(element.id + 'Error');
                        if (errorDiv) {
                            errorDiv.textContent = message;
                        }
                    }
                }

                function clearErrors() {
                    document.querySelectorAll('.is-invalid').forEach(el => {
                        el.classList.remove('is-invalid');
                    });
                    document.querySelectorAll('.invalid-feedback').forEach(el => {
                        el.textContent = '';
                    });
                }

                function formatCardNumber(value) {
                    const cleanValue = value.replace(/\s/g, '').replace(/[^0-9]/g, '');
                    const formattedValue = cleanValue.match(/.{1,4}/g)?.join(' ') || cleanValue;
                    return formattedValue.substring(0, 19); // Max 19 characters including spaces
                }

                function formatExpiryDate(value) {
                    const cleanValue = value.replace(/\D/g, '');
                    if (cleanValue.length >= 2) {
                        return cleanValue.substring(0, 2) + '/' + cleanValue.substring(2, 4);
                    }
                    return cleanValue;
                }

                // Payment method selection handler
                paymentMethods.forEach(method => {
                    method.addEventListener('click', function() {
                        const methodType = this.dataset.method;
                        console.log('💳 Payment method selected:', methodType);

                        // Update radio button
                        const radio = this.querySelector('input[type="radio"]');
                        if (radio) {
                            radio.checked = true;
                        }

                        // Update visual selection
                        paymentMethods.forEach(m => m.classList.remove('selected'));
                        this.classList.add('selected');

                        // Show/hide payment details
                        if (cardDetails) cardDetails.style.display = methodType === 'card' ? 'block' : 'none';
                        if (upiDetails) upiDetails.style.display = methodType === 'upi' ? 'block' : 'none';
                        if (netbankingDetails) netbankingDetails.style.display = methodType === 'netbanking' ? 'block' : 'none';
                        if (walletDetails) walletDetails.style.display = methodType === 'wallet' ? 'block' : 'none';

                        clearErrors();
                    });
                });

                // Input formatters and validators
                if (cardNumber) {
                    cardNumber.addEventListener('input', function() {
                        this.value = formatCardNumber(this.value);
                    });
                }

                if (expiryDate) {
                    expiryDate.addEventListener('input', function() {
                        this.value = formatExpiryDate(this.value);
                    });
                }

                if (cvv) {
                    cvv.addEventListener('input', function() {
                        this.value = this.value.replace(/[^0-9]/g, '').substring(0, 4);
                    });
                }

                // Main form submission handler - FIXED VERSION
                if (paymentForm) {
                    paymentForm.addEventListener('submit', function(e) {
                        console.log('📝 Form submission started');

                        clearErrors();

                        // Get selected payment method
                        const selectedMethodRadio = document.querySelector('input[name="paymentMethod"]:checked');
                        if (!selectedMethodRadio) {
                            e.preventDefault();
                            alert('Please select a payment method.');
                            return false;
                        }

                        const paymentMethod = selectedMethodRadio.value;
                        console.log('💳 Selected payment method:', paymentMethod);

                        // Validation based on payment method
                        let isValid = true;

                        if (paymentMethod === 'card') {
                            const cardNum = document.getElementById('cardNumber');
                            const expiry = document.getElementById('expiryDate');
                            const cvvField = document.getElementById('cvv');
                            const cardHolder = document.getElementById('cardHolder');

                            if (!cardNum?.value?.trim()) {
                                showError(cardNum, 'Card number is required');
                                isValid = false;
                            } else if (cardNum.value.replace(/\s/g, '').length !== 16) {
                                showError(cardNum, 'Please enter a valid 16-digit card number');
                                isValid = false;
                            }

                            if (!expiry?.value?.trim()) {
                                showError(expiry, 'Expiry date is required');
                                isValid = false;
                            } else if (expiry.value.length !== 5 || !expiry.value.includes('/')) {
                                showError(expiry, 'Please enter expiry date in MM/YY format');
                                isValid = false;
                            }

                            if (!cvvField?.value?.trim()) {
                                showError(cvvField, 'CVV is required');
                                isValid = false;
                            } else if (cvvField.value.length < 3) {
                                showError(cvvField, 'Please enter a valid CVV (3-4 digits)');
                                isValid = false;
                            }

                            if (!cardHolder?.value?.trim()) {
                                showError(cardHolder, 'Cardholder name is required');
                                isValid = false;
                            }

                        } else if (paymentMethod === 'upi') {
                            const upiField = document.getElementById('upiId');
                            if (!upiField?.value?.trim()) {
                                showError(upiField, 'UPI ID is required');
                                isValid = false;
                            } else if (!upiField.value.includes('@')) {
                                showError(upiField, 'Please enter a valid UPI ID (e.g., name@paytm)');
                                isValid = false;
                            }

                        } else if (paymentMethod === 'netbanking') {
                            const bankField = document.getElementById('bank');
                            if (!bankField?.value) {
                                showError(bankField, 'Please select your bank');
                                isValid = false;
                            }

                        } else if (paymentMethod === 'wallet') {
                            const walletField = document.getElementById('walletType');
                            if (!walletField?.value) {
                                showError(walletField, 'Please select your wallet');
                                isValid = false;
                            }
                        }

                        // Check terms and conditions
                        if (!terms?.checked) {
                            showError(terms, 'Please accept the Terms and Conditions');
                            isValid = false;
                        }

                        if (!isValid) {
                            console.log('❌ Form validation failed');
                            e.preventDefault();
                            // Scroll to first error
                            const firstError = document.querySelector('.is-invalid');
                            if (firstError) {
                                firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                                firstError.focus();
                            }
                            return false;
                        }

                        console.log('✅ Form validation passed - submitting...');

                        // Show loading state
                        if (payButton) {
                            payButton.innerHTML = '<span class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>Processing Payment...';
                            payButton.disabled = true;
                        }

                        // Form will submit naturally
                        return true;
                    });
                }

                console.log('✅ Payment form setup completed');
            });
        })();
    </script>
</body>
</html>
