<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Cabin - Cabin Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .booking-card {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
        }
        .cabin-image {
            height: 300px;
            object-fit: cover;
            border-radius: 10px;
        }
        .price-display {
            font-size: 1.5rem;
            font-weight: bold;
            color: #28a745;
        }
        .form-control:focus, .form-select:focus {
            border-color: #28a745;
            box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25);
        }
        .error-message {
            animation: shake 0.5s;
        }
        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            75% { transform: translateX(5px); }
        }
        .debug-info {
            font-size: 0.8rem;
            background: #e3f2fd;
            border: 1px solid #2196f3;
            border-radius: 5px;
        }
    </style>
</head>
<body class="bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="cabins">
                <i class="fas fa-home"></i> Cabin Booking
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="user/dashboard">
                    <i class="fas fa-user"></i> Dashboard
                </a>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row">
            <!-- Cabin Details -->
            <div class="col-md-6">
                <div class="card booking-card">
                    <div class="card-body">
                        <h3 class="card-title">${cabin.name}</h3>
                        <!-- Add fallback for missing image -->
                        <c:choose>
                            <c:when test="${not empty cabin.imageUrl}">
                                <img src="${cabin.imageUrl}" alt="${cabin.name}" class="img-fluid cabin-image mb-3">
                            </c:when>
                            <c:otherwise>
                                <div class="cabin-image mb-3 bg-light d-flex align-items-center justify-content-center">
                                    <i class="fas fa-home fa-3x text-muted"></i>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div class="mb-3">
                            <h5><i class="fas fa-map-marker-alt text-danger"></i> Location</h5>
                            <p class="text-muted">${cabin.location}</p>
                        </div>

                        <div class="mb-3">
                            <h5><i class="fas fa-info-circle text-info"></i> Description</h5>
                            <p class="text-muted">${cabin.description}</p>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <h6><i class="fas fa-users text-primary"></i> Capacity</h6>
                                <p class="text-muted">${cabin.capacity} guests</p>
                            </div>
                            <div class="col-md-6">
                                <h6><i class="fas fa-dollar-sign text-success"></i> Hourly Rate</h6>
                                <p class="price-display">₹<fmt:formatNumber value="${cabin.hourlyRate}" type="number" maxFractionDigits="2"/>/hour</p>
                            </div>
                        </div>

                        <div class="mb-3">
                            <h5><i class="fas fa-star text-warning"></i> Amenities</h5>
                            <p class="text-muted">${cabin.amenities}</p>
                        </div>

                        <!-- Add availability status -->
                        <div class="mb-3">
                            <h6><i class="fas fa-check-circle text-success"></i> Status</h6>
                            <span class="badge bg-success">Available</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Booking Form -->
            <div class="col-md-6">
                <div class="card booking-card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0"><i class="fas fa-calendar-check"></i> Book This Cabin</h4>
                    </div>
                    <div class="card-body">
                        <!-- Debug Information Section (Remove after testing) -->
                        <div class="alert alert-info debug-info">
                            <strong>DEBUG INFO:</strong><br>
                            Cabin Object: ${cabin != null ? 'Present' : 'NULL'}<br>
                            <c:if test="${cabin != null}">
                                Cabin ID: ${cabin.id}<br>
                                Cabin Name: ${cabin.name}<br>
                                Cabin Capacity: ${cabin.capacity}<br>
                                Cabin Rate: ${cabin.hourlyRate}<br>
                                Available: ${cabin.available}<br>
                            </c:if>
                        </div>

                        <!-- Enhanced error handling -->
                        <c:if test="${not empty param.error}">
                            <div class="alert alert-danger alert-dismissible fade show error-message" role="alert">
                                <i class="fas fa-exclamation-triangle"></i>
                                <c:choose>
                                    <c:when test="${param.error == 'invalid_dates'}">Invalid dates selected. Please check your booking dates.</c:when>
                                    <c:when test="${param.error == 'cabin_not_available'}">This cabin is currently not available.</c:when>
                                    <c:when test="${param.error == 'time_conflict'}">Selected time conflicts with existing booking.</c:when>
                                    <c:when test="${param.error == 'booking_failed'}">Booking creation failed. Please try again.</c:when>
                                    <c:when test="${param.error == 'past_date'}">Cannot book dates in the past.</c:when>
                                    <c:when test="${param.error == 'missing_parameters'}">Missing required booking information.</c:when>
                                    <c:when test="${param.error == 'invalid_data'}">Invalid data provided. Please check your inputs.</c:when>
                                    <c:otherwise>${param.error}</c:otherwise>
                                </c:choose>
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show error-message" role="alert">
                                <i class="fas fa-exclamation-triangle"></i> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <form action="booking" method="post" id="bookingForm">
                            <input type="hidden" name="action" value="create">
                            <input type="hidden" name="cabinId" value="${cabin.id}">
                            <!-- Enhanced user ID handling -->
                            <input type="hidden" name="userId" value="${sessionScope.userId != null ? sessionScope.userId : 1}">
                            <input type="hidden" name="hourlyRate" value="${cabin.hourlyRate}">

                            <div class="mb-3">
                                <label for="startDate" class="form-label">
                                    <i class="fas fa-calendar-alt"></i> Check-in Date & Time
                                </label>
                                <input type="datetime-local" class="form-control" id="startDate" name="startDate" required>
                                <div class="form-text">Select your arrival date and time</div>
                            </div>

                            <div class="mb-3">
                                <label for="endDate" class="form-label">
                                    <i class="fas fa-calendar-alt"></i> Check-out Date & Time
                                </label>
                                <input type="datetime-local" class="form-control" id="endDate" name="endDate" required>
                                <div class="form-text">Select your departure date and time</div>
                            </div>

                            <!-- Enhanced Guest Selection with Fallback -->
                            <div class="mb-3">
                                <label for="guests" class="form-label">
                                    <i class="fas fa-users"></i> Number of Guests
                                </label>
                                <select class="form-select" id="guests" name="guests" required>
                                    <option value="">Select guests</option>
                                    <c:choose>
                                        <c:when test="${cabin.capacity > 0}">
                                            <c:forEach var="i" begin="1" end="${cabin.capacity}">
                                                <option value="${i}">${i} guest${i > 1 ? 's' : ''}</option>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <!-- Fallback options if capacity is not set -->
                                            <option value="1">1 guest</option>
                                            <option value="2">2 guests</option>
                                            <option value="3">3 guests</option>
                                            <option value="4">4 guests</option>
                                            <option value="5">5 guests</option>
                                            <option value="6">6 guests</option>
                                            <option value="7">7 guests</option>
                                            <option value="8">8 guests</option>
                                        </c:otherwise>
                                    </c:choose>
                                </select>
                                <div class="form-text">
                                    <c:choose>
                                        <c:when test="${cabin.capacity > 0}">
                                            Maximum ${cabin.capacity} guests allowed
                                        </c:when>
                                        <c:otherwise>
                                            Please select number of guests
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">
                                    <i class="fas fa-calculator"></i> Booking Summary
                                </label>
                                <div class="bg-light p-3 rounded">
                                    <div class="d-flex justify-content-between">
                                        <span>Duration:</span>
                                        <span id="duration">-- hours</span>
                                    </div>
                                    <div class="d-flex justify-content-between">
                                        <span>Rate:</span>
                                        <span>₹<fmt:formatNumber value="${cabin.hourlyRate}" type="number" maxFractionDigits="2"/>/hour</span>
                                    </div>
                                    <hr>
                                    <div class="d-flex justify-content-between">
                                        <strong>Total Amount:</strong>
                                        <strong class="text-success" id="totalAmount">₹0</strong>
                                    </div>
                                </div>
                                <input type="hidden" name="amount" id="amount" value="0">
                            </div>

                            <!-- Terms and conditions -->
                            <div class="form-check mb-3">
                                <input class="form-check-input" type="checkbox" id="agreeTerms" required>
                                <label class="form-check-label" for="agreeTerms">
                                    I agree to the <a href="#" data-bs-toggle="modal" data-bs-target="#termsModal">Terms and Conditions</a>
                                </label>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary btn-lg" id="submitBtn">
                                    <i class="fas fa-credit-card"></i> Proceed to Payment
                                </button>
                                <a href="cabins" class="btn btn-outline-secondary">
                                    <i class="fas fa-arrow-left"></i> Back to Cabins
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Terms Modal -->
    <div class="modal fade" id="termsModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Terms and Conditions</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>By booking this cabin, you agree to:</p>
                    <ul>
                        <li>Arrive on time for your booking</li>
                        <li>Respect the property and other guests</li>
                        <li>Pay the full amount as calculated</li>
                        <li>Follow all cabin rules and regulations</li>
                        <li>Cancellation must be done 24 hours in advance</li>
                        <li>No smoking or pets allowed without prior permission</li>
                        <li>Maximum occupancy as specified cannot be exceeded</li>
                        <li>Any damages will be charged separately</li>
                    </ul>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const startDateInput = document.getElementById('startDate');
            const endDateInput = document.getElementById('endDate');
            const durationSpan = document.getElementById('duration');
            const totalAmountSpan = document.getElementById('totalAmount');
            const amountInput = document.getElementById('amount');
            const submitBtn = document.getElementById('submitBtn');
            const guestSelect = document.getElementById('guests');

            // Get hourly rate with fallback
            const hourlyRateInput = document.querySelector('input[name="hourlyRate"]');
            const hourlyRate = hourlyRateInput ? parseFloat(hourlyRateInput.value) : 0;

            // Debug logging
            console.log('Booking form initialized');
            console.log('Hourly rate:', hourlyRate);
            console.log('Guest dropdown found:', guestSelect);
            console.log('Number of guest options:', guestSelect ? guestSelect.options.length : 'No select found');

            // Set minimum date to current date
            const now = new Date();
            const currentDateTime = now.toISOString().slice(0, 16);
            startDateInput.min = currentDateTime;
            endDateInput.min = currentDateTime;

            // Add some default times (next hour and 2 hours later)
            const nextHour = new Date(now.getTime() + 60 * 60 * 1000);
            const twoHoursLater = new Date(now.getTime() + 2 * 60 * 60 * 1000);

            if (!startDateInput.value) {
                startDateInput.value = nextHour.toISOString().slice(0, 16);
            }
            if (!endDateInput.value) {
                endDateInput.value = twoHoursLater.toISOString().slice(0, 16);
            }

            function calculateTotal() {
                const startDate = new Date(startDateInput.value);
                const endDate = new Date(endDateInput.value);

                if (startDate && endDate && endDate > startDate && hourlyRate > 0) {
                    const diffInMs = endDate - startDate;
                    const diffInHours = Math.max(1, Math.ceil(diffInMs / (1000 * 60 * 60))); // Minimum 1 hour
                    const totalAmount = diffInHours * hourlyRate;

                    durationSpan.textContent = diffInHours + ' hour' + (diffInHours > 1 ? 's' : '');
                    totalAmountSpan.textContent = '₹' + totalAmount.toFixed(2);
                    amountInput.value = totalAmount.toFixed(2);

                    // Enable submit button
                    submitBtn.disabled = false;
                } else {
                    durationSpan.textContent = '-- hours';
                    totalAmountSpan.textContent = '₹0';
                    amountInput.value = '0';

                    // Disable submit button if invalid
                    submitBtn.disabled = true;
                }
            }

            // Event listeners
            startDateInput.addEventListener('change', function() {
                endDateInput.min = this.value;
                calculateTotal();
                console.log('Start date changed to:', this.value);
            });

            endDateInput.addEventListener('change', function() {
                calculateTotal();
                console.log('End date changed to:', this.value);
            });

            // Guest dropdown debugging
            if (guestSelect) {
                guestSelect.addEventListener('change', function() {
                    console.log('Guest selection changed to:', this.value);
                });

                guestSelect.addEventListener('click', function() {
                    console.log('Guest dropdown clicked - Options available:', this.options.length);
                });

                guestSelect.addEventListener('focus', function() {
                    console.log('Guest dropdown focused');
                });
            }

            // Form validation
            document.getElementById('bookingForm').addEventListener('submit', function(e) {
                const startDate = new Date(startDateInput.value);
                const endDate = new Date(endDateInput.value);
                const amount = parseFloat(amountInput.value);
                const guests = guestSelect.value;

                console.log('Form submission attempt - Guests:', guests, 'Amount:', amount);

                if (endDate <= startDate) {
                    e.preventDefault();
                    alert('Check-out time must be after check-in time.');
                    return;
                }

                if (amount <= 0) {
                    e.preventDefault();
                    alert('Please select valid dates.');
                    return;
                }

                if (!guests) {
                    e.preventDefault();
                    alert('Please select number of guests.');
                    return;
                }

                if (startDate < now.getTime() - 30 * 60 * 1000) { // Allow 30 min buffer
                    e.preventDefault();
                    alert('Booking cannot be made for past dates.');
                    return;
                }

                // Show loading state
                submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';
                submitBtn.disabled = true;

                console.log('Form validation passed - submitting booking');
            });

            // Initial calculation
            calculateTotal();

            // Auto-dismiss alerts after 8 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert:not(.debug-info)');
                alerts.forEach(function(alert) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                });
            }, 8000);

            // Debug info auto-hide after 10 seconds
            setTimeout(function() {
                const debugAlert = document.querySelector('.debug-info');
                if (debugAlert) {
                    debugAlert.style.display = 'none';
                }
            }, 10000);

            console.log('Booking form setup complete');
        });
    </script>
</body>
</html>
