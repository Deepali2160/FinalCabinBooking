<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cabinest - Luxury Cabin Bookings</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vanta@latest/dist/vanta.globe.min.js"></script>
    <link rel="stylesheet" href="assets/css/landingpage.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="container">
            <a href="#" class="logo">
                <i class="fas fa-tree"></i>
                <span>Cabinest</span>
            </a>

            <div class="nav-links">
                <a href="#" class="active">Home</a>
                <a href="login.jsp">Book Cabin</a>
                <a href="#">View Cabins</a>
                <a href="#">About Us</a>
                <a href="#">Contact</a>
            </div>

            <div class="theme-toggle">
                <input type="checkbox" id="theme-switch" hidden>
                <label for="theme-switch" class="switch">
                    <span class="sun"><i class="fas fa-sun"></i></span>
                    <span class="moon"><i class="fas fa-moon"></i></span>
                    <span class="ball"></span>
                </label>
            </div>

            <button class="hamburger">
                <i class="fas fa-bars"></i>
            </button>
        </div>
    </nav>

    <!-- Home Page -->
    <div id="home-page">
        <!-- Hero Section -->
        <section class="hero">
            <div class="container">
                <div class="hero-content">
                    <h1 class="hero-title">Find Your Perfect Escape</h1>
                    <p class="hero-subtitle">Book luxury cabins easily with Cabinest</p>
                    <div class="hero-buttons">
                        <a href="login.jsp" class="btn primary">Book Now</a>
                        <a href="#" class="btn secondary">View Cabins</a>
                    </div>
                </div>
            </div>

            <div class="cabin-3d-container" id="cabin-container">
                <!-- 3D Cabin will be rendered here -->
            </div>

            <div class="mountain-range"></div>

            <div class="clouds">
                <div class="cloud cloud-1"></div>
                <div class="cloud cloud-2"></div>
                <div class="cloud cloud-3"></div>
            </div>
        </section>

        <!-- Features Section -->
        <section class="features">
            <div class="container">
                <h2 class="section-title">Why Choose Cabinest</h2>
                <p class="section-subtitle">Experience the best in luxury cabin rentals</p>

                <div class="features-grid">
                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-search"></i>
                        </div>
                        <h3>Easy Cabin Search</h3>
                        <p>Find your dream cabin with our intuitive search and filtering tools.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-calendar-check"></i>
                        </div>
                        <h3>Real-Time Availability</h3>
                        <p>See up-to-date availability and instant booking confirmation.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-shield-alt"></i>
                        </div>
                        <h3>Secure Payments</h3>
                        <p>Bank-level encryption ensures your payment details are always safe.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-star"></i>
                        </div>
                        <h3>Customer Reviews</h3>
                        <p>Read genuine reviews from our community of travelers.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-chart-line"></i>
                        </div>
                        <h3>Admin Dashboard</h3>
                        <p>Property owners get powerful tools to manage their listings.</p>
                    </div>

                    <div class="feature-card">
                        <div class="feature-icon">
                            <i class="fas fa-mobile-alt"></i>
                        </div>
                        <h3>Mobile App</h3>
                        <p>Manage your bookings on the go with our mobile application.</p>
                    </div>
                </div>
            </div>
        </section>

        <!-- Featured Cabins -->
        <section class="featured-cabins">
            <div class="container">
                <h2 class="section-title">Featured Luxury Cabins</h2>
                <p class="section-subtitle">Experience nature in ultimate comfort</p>

                <div class="cabins-grid">
                    <div class="cabin-card">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1580048915913-4f8f5cb481c2?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Mountain View Cabin">
                        </div>
                        <div class="cabin-info">
                            <h3>Mountain View Lodge</h3>
                            <div class="cabin-rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star-half-alt"></i>
                                <span>4.7 (128 reviews)</span>
                            </div>
                            <p>Panoramic mountain views, hot tub, and modern amenities in a secluded forest setting.</p>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 6 Guests</span>
                                <span><i class="fas fa-bed"></i> 3 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div class="price">$289 <span>/night</span></div>
                            <a href="login.jsp" class="btn primary">Book Now</a>
                        </div>
                    </div>

                    <div class="cabin-card">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1591825729269-caeb344f6df2?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Lakeside Retreat">
                        </div>
                        <div class="cabin-info">
                            <h3>Lakeside Retreat</h3>
                            <div class="cabin-rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <span>4.9 (97 reviews)</span>
                            </div>
                            <p>Private dock, floor-to-ceiling windows, and direct lake access in this waterfront paradise.</p>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 4 Guests</span>
                                <span><i class="fas fa-bed"></i> 2 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div class="price">$329 <span>/night</span></div>
                            <a href="login.jsp" class="btn primary">Book Now</a>
                        </div>
                    </div>

                    <div class="cabin-card">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1613490493576-7fde63acd811?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Forest Haven">
                        </div>
                        <div class="cabin-info">
                            <h3>Forest Haven</h3>
                            <div class="cabin-rating">
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="fas fa-star"></i>
                                <i class="far fa-star"></i>
                                <span>4.5 (86 reviews)</span>
                            </div>
                            <p>Secluded woodland escape with sauna, fireplace, and hiking trails right outside your door.</p>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 8 Guests</span>
                                <span><i class="fas fa-bed"></i> 4 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div class="price">$399 <span>/night</span></div>
                            <a href="login.jsp" class="btn primary">Book Now</a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Testimonials -->
        <section class="testimonials">
            <div class="container">
                <h2 class="section-title">What Our Guests Say</h2>
                <p class="section-subtitle">Real experiences from real travelers</p>

                <div class="testimonial-carousel">
                    <div class="testimonial active">
                        <div class="testimonial-content">
                            <div class="quote-icon"><i class="fas fa-quote-left"></i></div>
                            <p>"Our stay at the Lakeside Retreat was absolutely magical. Waking up to the mist over the water each morning was an experience we'll never forget. The cabin had everything we needed and more!"</p>
                            <div class="testimonial-author">
                                <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="Sarah Johnson">
                                <div>
                                    <h4>Sarah Johnson</h4>
                                    <span>Stayed at Lakeside Retreat</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="testimonial">
                        <div class="testimonial-content">
                            <div class="quote-icon"><i class="fas fa-quote-left"></i></div>
                            <p>"Booking through Cabinest was seamless. The real-time availability feature saved us so much time, and the cabin exceeded our expectations. We'll definitely be using this service for all our future mountain getaways."</p>
                            <div class="testimonial-author">
                                <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="Michael Chen">
                                <div>
                                    <h4>Michael Chen</h4>
                                    <span>Stayed at Mountain View Lodge</span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="testimonial">
                        <div class="testimonial-content">
                            <div class="quote-icon"><i class="fas fa-quote-left"></i></div>
                            <p>"As a family of six, finding the perfect cabin can be challenging. Forest Haven was spacious, beautifully designed, and had amenities that kept both kids and adults happy. We made memories that will last a lifetime."</p>
                            <div class="testimonial-author">
                                <img src="https://randomuser.me/api/portraits/women/68.jpg" alt="Jennifer Martinez">
                                <div>
                                    <h4>Jennifer Martinez</h4>
                                    <span>Stayed at Forest Haven</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="testimonial-controls">
                    <button class="prev"><i class="fas fa-chevron-left"></i></button>
                    <div class="dots">
                        <span class="dot active"></span>
                        <span class="dot"></span>
                        <span class="dot"></span>
                    </div>
                    <button class="next"><i class="fas fa-chevron-right"></i></button>
                </div>
            </div>
        </section>
    </div>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <div class="footer-grid">
                <div class="footer-col footer-about">
                    <div class="logo">
                        <i class="fas fa-tree"></i>
                        <span>Cabinest</span>
                    </div>
                    <p>Connecting travelers with exceptional cabin experiences in nature's most beautiful locations since 2018.</p>
                    <div class="social-links">
                        <a href="#"><i class="fab fa-facebook-f"></i></a>
                        <a href="#"><i class="fab fa-instagram"></i></a>
                        <a href="#"><i class="fab fa-twitter"></i></a>
                        <a href="#"><i class="fab fa-pinterest"></i></a>
                    </div>
                </div>

                <div class="footer-col">
                    <h4>Explore</h4>
                    <ul>
                        <li><a href="#">Home</a></li>
                        <li><a href="#">Cabins</a></li>
                        <li><a href="#">Destinations</a></li>
                        <li><a href="#">Special Offers</a></li>
                        <li><a href="#">Gift Cards</a></li>
                    </ul>
                </div>

                <div class="footer-col">
                    <h4>Support</h4>
                    <ul>
                        <li><a href="#">Help Center</a></li>
                        <li><a href="#">FAQ</a></li>
                        <li><a href="#">Cancellation Policy</a></li>
                        <li><a href="#">Privacy Policy</a></li>
                        <li><a href="#">Contact Us</a></li>
                    </ul>
                </div>

                <div class="footer-col">
                    <h4>Newsletter</h4>
                    <p>Subscribe for exclusive deals and cabin inspiration</p>
                    <form class="newsletter-form">
                        <input type="email" placeholder="Your email address" required>
                        <button type="submit"><i class="fas fa-paper-plane"></i></button>
                    </form>
                </div>
            </div>

            <div class="footer-bottom">
                <p>&copy; 2023 Cabinest. All rights reserved.</p>
                <div class="payment-methods">
                    <i class="fab fa-cc-visa"></i>
                    <i class="fab fa-cc-mastercard"></i>
                    <i class="fab fa-cc-amex"></i>
                    <i class="fab fa-cc-paypal"></i>
                    <i class="fab fa-cc-apple-pay"></i>
                </div>
            </div>
        </div>
    </footer>
   <script src="assets/js/landingpage.js"></script>
</body>
</html>