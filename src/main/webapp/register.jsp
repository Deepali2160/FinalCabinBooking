<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Cabinest</title>
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

                <h2>Create Your Account</h2>
                <p>Join us to book your dream cabin</p>

                 <form class="register-form" action="${pageContext.request.contextPath}/register" method="POST">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" placeholder="Enter your full name" required>
                    </div>

                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" placeholder="Enter your email" required>
                    </div>

                    <div class="form-group">
                       <label for="password">Password</label>
                       <input type="password" id="password" name="password" required />

                       <small id="passwordHelp" style="color: #555; font-size: 0.85rem;">
                         Password must be at least 8 characters, include a capital letter, number, and special character.
                       </small>

                       <ul id="passwordChecklist" style="list-style: none; padding-left: 0; margin-top: 8px; font-size: 0.85rem;">
                       </ul>

                       <div id="passwordStrengthBarContainer" style="margin-top: 8px;">
                         <div id="passwordStrengthBar" style="height: 6px; width: 100%; background: #ddd; border-radius: 5px;">
                           <div id="strengthFill" style="height: 100%; width: 0%; background-color: red; border-radius: 5px; transition: width 0.3s;"></div>
                         </div>
                         <small id="strengthLabel" style="font-size: 0.85rem; display: block; margin-top: 4px; color: #555;"></small>
                       </div>

                       <div id="passwordError" style="color: red; font-size: 0.9rem; display: none;"></div>
</div>

                     <div class="form-group">
                       <label for="confirm-password">Confirm Password</label>
                       <input type="password" id="confirm-password" name="confirm-password" required />
                     </div>
                    <button type="submit" class="btn primary">Register</button>

                    <%-- Display error message if exists --%>
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="error-message" style="color: #e74c3c; margin: 15px 0; font-weight: 500;">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>
                </form>

                <div class="divider">
                    <span>or sign up with</span>
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
                    Already have an account? <a href="login.jsp">Login</a>
                </div>
            </div>
        </div>
    </div>

     <script src="assets/js/register.js"></script>
</body>
</html>