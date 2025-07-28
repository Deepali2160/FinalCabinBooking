<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Cabinest</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/login.css">
</head>
<body>
    <div class="login-background"></div>

    <div class="theme-toggle-container">
        <div class="theme-toggle">
            <input type="checkbox" id="theme-switch" hidden>
            <label for="theme-switch" class="switch">
                <span class="sun"><i class="fas fa-sun"></i></span>
                <span class="moon"><i class="fas fa-moon"></i></span>
                <span class="ball"></span>
            </label>
        </div>
    </div>

    <div class="container">
        <div class="login-container">
            <div class="login-card">
                <div class="logo" style="margin-bottom: 30px;">
                    <i class="fas fa-tree" style="font-size: 2.5rem; color: #4361ee;"></i>
                    <span style="font-family: 'Montserrat', sans-serif; font-weight: 800; font-size: 2rem; color: #4361ee;">Cabinest</span>
                </div>

                <h2>Login to Your Account</h2>
                <p>Access your bookings and manage your account</p>

                <form class="login-form" action="login" method="POST">
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" placeholder="Enter your email" required>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password" required>
                    </div>

                    <div class="form-options">
                        <div class="remember-me">
                            <input type="checkbox" id="remember" name="remember">
                            <label for="remember">Remember me</label>
                        </div>
                        <a href="#" class="forgot-password">Forgot password?</a>
                    </div>

                    <button type="submit" class="btn primary">Login</button>

                    <%-- Display error message if exists --%>
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="error-message" style="color: #e74c3c; margin: 15px 0; font-weight: 500;">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>
                </form>

                <div class="divider">
                    <span>or continue with</span>
                </div>

                <div class="social-login">
                    <button type="button" class="social-btn google">
                        <i class="fab fa-google"></i> Google
                    </button>
                    <button type="button" class="social-btn facebook">
                        <i class="fab fa-facebook-f"></i> Facebook
                    </button>
                </div>

                <div class="signup-link">
                    Don't have an account? <a href="register.jsp">Sign up</a>
                </div>
            </div>
        </div>
    </div>
    <script src="assets/js/login.js"></script>
</body>
</html>