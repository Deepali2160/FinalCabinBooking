<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cabin Booking System - Settings</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css">
    <style>
        /* Additional settings-specific styles */
        .settings-card {
            background: white;
            border-radius: var(--border-radius);
            padding: 30px;
            box-shadow: var(--box-shadow);
            margin-bottom: 30px;
        }

        body.dark-theme .settings-card {
            background: #2d2d2d;
        }

        .settings-group {
            margin-bottom: 30px;
        }

        .settings-group:last-child {
            margin-bottom: 0;
        }

        .settings-group-title {
            font-size: 1.3rem;
            font-weight: 600;
            color: var(--dark);
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid var(--primary);
            display: flex;
            align-items: center;
        }

        body.dark-theme .settings-group-title {
            color: white;
            border-bottom-color: var(--accent);
        }

        .settings-group-title i {
            margin-right: 10px;
            color: var(--primary);
        }

        .setting-item {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
            padding: 15px;
            background: rgba(67, 97, 238, 0.05);
            border-radius: var(--border-radius);
            transition: var(--transition);
        }

        body.dark-theme .setting-item {
            background: rgba(67, 97, 238, 0.1);
        }

        .setting-item:hover {
            background: rgba(67, 97, 238, 0.1);
        }

        body.dark-theme .setting-item:hover {
            background: rgba(67, 97, 238, 0.2);
        }

        .setting-label {
            flex: 1;
            font-weight: 500;
            color: var(--dark);
        }

        body.dark-theme .setting-label {
            color: white;
        }

        .setting-control {
            flex: 2;
        }

        .form-control {
            width: 100%;
            padding: 10px 15px;
            border-radius: var(--border-radius);
            border: 1px solid var(--light-gray);
            background: white;
            color: var(--dark);
            transition: var(--transition);
        }

        body.dark-theme .form-control {
            background: #333;
            border-color: #444;
            color: white;
        }

        .form-control:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 3px rgba(67, 97, 238, 0.2);
            outline: none;
        }

        .input-group {
            display: flex;
            align-items: center;
        }

        .input-group .form-control {
            border-top-right-radius: 0;
            border-bottom-right-radius: 0;
        }

        .input-group-append {
            display: flex;
        }

        .input-group-text {
            padding: 10px 15px;
            background: var(--light-gray);
            border: 1px solid var(--light-gray);
            border-left: none;
            border-radius: 0 var(--border-radius) var(--border-radius) 0;
            color: var(--dark);
        }

        body.dark-theme .input-group-text {
            background: #444;
            border-color: #444;
            color: white;
        }

        .toggle-switch {
            position: relative;
            display: inline-block;
            width: 60px;
            height: 30px;
        }

        .toggle-switch input {
            opacity: 0;
            width: 0;
            height: 0;
        }

        .slider {
            position: absolute;
            cursor: pointer;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: var(--light-gray);
            transition: var(--transition);
            border-radius: 34px;
        }

        body.dark-theme .slider {
            background-color: #555;
        }

        .slider:before {
            position: absolute;
            content: "";
            height: 22px;
            width: 22px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            transition: var(--transition);
            border-radius: 50%;
        }

        input:checked + .slider {
            background-color: var(--primary);
        }

        input:checked + .slider:before {
            transform: translateX(30px);
        }

        .btn-save {
            background-color: var(--primary);
            color: white;
            padding: 12px 25px;
            border-radius: 50px;
            font-weight: 600;
            font-size: 1rem;
            border: none;
            cursor: pointer;
            transition: var(--transition);
            box-shadow: 0 4px 15px rgba(67, 97, 238, 0.3);
            display: inline-flex;
            align-items: center;
        }

        .btn-save:hover {
            background-color: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(67, 97, 238, 0.4);
        }

        .btn-save i {
            margin-right: 8px;
        }

        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 1100;
            background: var(--success);
            color: white;
            padding: 15px 25px;
            border-radius: var(--border-radius);
            box-shadow: var(--box-shadow);
            display: flex;
            align-items: center;
            transform: translateY(-100px);
            opacity: 0;
            transition: var(--transition);
        }

        .toast.show {
            transform: translateY(0);
            opacity: 1;
        }

        .toast i {
            margin-right: 10px;
        }
    </style>
</head>
<body class="light-theme">
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="logo">
                <i class="fas fa-cabin"></i>CabinBook

            </a>
            <div class="theme-toggle">
                <input type="checkbox" id="theme-switch" hidden>
                <label for="theme-switch" class="switch">
                    <span class="ball"></span>
                    <i class="fas fa-sun sun"></i>
                    <i class="fas fa-moon moon"></i>
                </label>
            </div>
        </div>
    </nav>

    <!-- Dashboard Layout -->
    <div class="dashboard">
        <!-- Sidebar -->
        <div class="sidebar">
            <div class="sidebar-header">
                <h3>Admin Panel</h3>
                <p>Welcome, ${sessionScope.user.name}</p>
            </div>
            <ul class="sidebar-menu">
                <li>
                    <a href="${pageContext.request.contextPath}/admin/dashboard">
                        <i class="fas fa-tachometer-alt"></i>Dashboard
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/cabin">
                        <i class="fas fa-home"></i>Cabins
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/bookings">
                        <i class="fas fa-calendar-check"></i>Bookings
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/users">
                        <i class="fas fa-users"></i>Users
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/admin/settings" class="active">
                        <i class="fas fa-cog"></i>Settings
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/logout">
                        <i class="fas fa-sign-out-alt"></i>Logout
                    </a>
                </li>
            </ul>
        </div>

        <!-- Main Content -->
        <main class="main-content">
            <div class="dashboard-header">
                <h1 class="dashboard-title">System Settings</h1>
            </div>

            <div class="settings-card">
                <form id="settingsForm" action="${pageContext.request.contextPath}/admin/settings" method="post">
                    <!-- Booking Settings -->
                    <div class="settings-group">
                        <h3 class="settings-group-title">
                            <i class="fas fa-calendar-alt"></i>Booking Settings
                        </h3>

                        <div class="setting-item">
                            <div class="setting-label">Minimum Booking Days</div>
                            <div class="setting-control">
                                <input type="number" class="form-control" name="min_booking_days"
                                       value="${settings.min_booking_days}" min="1" max="30">
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Maximum Booking Days</div>
                            <div class="setting-control">
                                <input type="number" class="form-control" name="max_booking_days"
                                       value="${settings.max_booking_days}" min="1" max="365">
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Check-in Time</div>
                            <div class="setting-control">
                                <input type="time" class="form-control" name="checkin_time"
                                       value="${settings.checkin_time}">
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Check-out Time</div>
                            <div class="setting-control">
                                <input type="time" class="form-control" name="checkout_time"
                                       value="${settings.checkout_time}">
                            </div>
                        </div>
                    </div>

                    <!-- Payment Settings -->
                    <div class="settings-group">
                        <h3 class="settings-group-title">
                            <i class="fas fa-credit-card"></i>Payment Settings
                        </h3>

                        <div class="setting-item">
                            <div class="setting-label">Deposit Percentage</div>
                            <div class="setting-control">
                                <div class="input-group">
                                    <input type="number" class="form-control" name="deposit_percentage"
                                           value="${settings.deposit_percentage}" min="0" max="100">
                                    <div class="input-group-append">
                                        <span class="input-group-text">%</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Cancellation Policy (hours)</div>
                            <div class="setting-control">
                                <input type="number" class="form-control" name="cancellation_policy"
                                       value="${settings.cancellation_policy}" min="0">
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Tax Rate</div>
                            <div class="setting-control">
                                <div class="input-group">
                                    <input type="number" class="form-control" name="tax_rate"
                                           value="${settings.tax_rate}" min="0" max="30" step="0.1">
                                    <div class="input-group-append">
                                        <span class="input-group-text">%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- System Settings -->
                    <div class="settings-group">
                        <h3 class="settings-group-title">
                            <i class="fas fa-server"></i>System Settings
                        </h3>

                        <div class="setting-item">
                            <div class="setting-label">Maintenance Mode</div>
                            <div class="setting-control">
                                <label class="toggle-switch">
                                    <input type="checkbox" name="maintenance_mode"
                                           ${settings.maintenance_mode == 'true' ? 'checked' : ''}>
                                    <span class="slider"></span>
                                </label>
                            </div>
                        </div>

                        <div class="setting-item">
                            <div class="setting-label">Email Notifications</div>
                            <div class="setting-control">
                                <label class="toggle-switch">
                                    <input type="checkbox" name="email_notifications"
                                           ${settings.email_notifications == 'true' ? 'checked' : ''}>
                                    <span class="slider"></span>
                                </label>
                            </div>
                        </div>
                    </div>

                    <div class="text-right mt-4">
                        <button type="submit" class="btn-save">
                            <i class="fas fa-save"></i>Save Settings
                        </button>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <!-- Toast Notification -->
    <div id="toast" class="toast">
        <i class="fas fa-check-circle"></i>
        <span>Settings saved successfully!</span>
    </div>

    <!-- JavaScript -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/admin-dashboard.js"></script>
    <script>
        $(document).ready(function() {
            // Theme toggle (from your dashboard.js)
            const themeSwitch = document.getElementById('theme-switch');
            const body = document.body;

            // Check for saved theme preference
            if(localStorage.getItem('theme') === 'dark') {
                body.classList.add('dark-theme');
                body.classList.remove('light-theme');
                themeSwitch.checked = true;
            }

            themeSwitch.addEventListener('change', function() {
                if(this.checked) {
                    body.classList.add('dark-theme');
                    body.classList.remove('light-theme');
                    localStorage.setItem('theme', 'dark');
                } else {
                    body.classList.add('light-theme');
                    body.classList.remove('dark-theme');
                    localStorage.setItem('theme', 'light');
                }
            });

            // Form submission with AJAX
            $('#settingsForm').on('submit', function(e) {
                e.preventDefault();

                $.ajax({
                    url: $(this).attr('action'),
                    method: $(this).attr('method'),
                    data: $(this).serialize(),
                    success: function(response) {
                        showToast('Settings saved successfully!');
                    },
                    error: function(xhr, status, error) {
                        showToast('Error saving settings: ' + error, 'error');
                    }
                });
            });

            // Show toast notification
            function showToast(message, type = 'success') {
                const toast = $('#toast');
                toast.find('span').text(message);

                // Change icon and color based on type
                const icon = toast.find('i');
                if(type === 'success') {
                    icon.removeClass('fa-times-circle').addClass('fa-check-circle');
                    toast.css('background', 'var(--success)');
                } else {
                    icon.removeClass('fa-check-circle').addClass('fa-times-circle');
                    toast.css('background', 'var(--danger-color)');
                }

                toast.addClass('show');
                setTimeout(() => {
                    toast.removeClass('show');
                }, 3000);
            }

            // Show toast if success parameter exists in URL
            const urlParams = new URLSearchParams(window.location.search);
            if(urlParams.has('success')) {
                showToast('Settings saved successfully!');
                // Remove the success parameter from URL
                history.replaceState(null, '', window.location.pathname);
            }
        });
    </script>
</body>
</html>