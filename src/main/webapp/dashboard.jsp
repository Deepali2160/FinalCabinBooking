<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cabinest - My Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/user-dashboard.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="container">
            <a href="#" class="logo">
                <i class="fas fa-tree"></i>
                <span>Cabinest</span>
            </a>

            <div class="theme-toggle">
                <input type="checkbox" id="theme-switch" hidden>
                <label for="theme-switch" class="switch">
                    <span class="sun"><i class="fas fa-sun"></i></span>
                    <span class="moon"><i class="fas fa-moon"></i></span>
                    <span class="ball"></span>
                </label>
            </div>

            <div class="user-menu">
                <span>John Doe</span>
                <i class="fas fa-user-circle" style="font-size: 1.5rem; margin-left: 10px;"></i>
            </div>
        </div>
    </nav>

    <!-- Dashboard Layout -->
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="User Avatar" class="user-avatar">
                <div class="user-info">
                    <h3>John Doe</h3>
                    <p>Member since 2022</p>
                </div>
            </div>
            <ul class="sidebar-menu">
                <li><a href="#" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                 <li><a href="available-cabins"><i class="fas fa-cabin"></i> View Cabins</a></li>
                <li><a href="#"><i class="fas fa-calendar-check"></i> My Bookings</a></li>
                <li><a href="#"><i class="fas fa-heart"></i> Wishlist</a></li>
                <li><a href="#"><i class="fas fa-cog"></i> Account Settings</a></li>
                <li><a href="#"><i class="fas fa-sign-out-alt"></i> Logout</a></li>


            </ul>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <div class="dashboard-header">
                <h1 class="dashboard-title">My Dashboard</h1>
                <button class="btn primary">Book a Cabin</button>
            </div>

            <!-- Welcome Banner -->
            <div class="welcome-banner">
                <h2>Welcome back, John!</h2>
                <p>Ready for your next cabin adventure? Explore our newest additions or check out your upcoming bookings.</p>
                <button class="btn secondary">Explore Cabins</button>
            </div>

            <!-- Stats Cards -->
            <div class="dashboard-stats">
                <div class="stat-card">
                    <i class="fas fa-calendar-check"></i>
                    <h3>Upcoming Bookings</h3>
                    <div class="value">3</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-history"></i>
                    <h3>Past Bookings</h3>
                    <div class="value">7</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-heart"></i>
                    <h3>Wishlist</h3>
                    <div class="value">5</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-star"></i>
                    <h3>Your Reviews</h3>
                    <div class="value">4</div>
                </div>
            </div>

            <!-- Upcoming Bookings -->
            <div class="recent-bookings">
                <div class="section-header">
                    <h2 class="section-title">Upcoming Bookings</h2>
                    <a href="#" class="btn primary">View All</a>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Cabin</th>
                            <th>Dates</th>
                            <th>Guests</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>#CB-1256</td>
                            <td>Mountain View Lodge</td>
                            <td>Jul 15 - Jul 20, 2023</td>
                            <td>4</td>
                            <td>$1,445</td>
                            <td><span class="status confirmed">Confirmed</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Cancel"><i class="fas fa-times"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>#CB-1255</td>
                            <td>Lakeside Retreat</td>
                            <td>Aug 5 - Aug 10, 2023</td>
                            <td>2</td>
                            <td>$1,645</td>
                            <td><span class="status confirmed">Confirmed</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Cancel"><i class="fas fa-times"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>#CB-1254</td>
                            <td>Forest Haven</td>
                            <td>Sep 12 - Sep 18, 2023</td>
                            <td>6</td>
                            <td>$2,394</td>
                            <td><span class="status pending">Payment Pending</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Pay Now"><i class="fas fa-credit-card"></i></button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Wishlist -->

            <!-- Wishlist -->
            <div class="recent-bookings">
                <div class="section-header">
                    <h2 class="section-title">Available Cabins</h2>
                    <a href="#" class="btn primary">View All</a>
                </div>
                <div class="wishlist">
                    <c:forEach var="cabin" items="${cabinList}">
                        <div class="cabin-card">
                            <div class="cabin-image">
                                <img src="<c:out value='${cabin.imageUrls != null && !cabin.imageUrls.isEmpty() ? cabin.imageUrls[0] : cabin.imageUrl}'/>"
                                     alt="${cabin.name}" style="width:100%; height:200px; object-fit:cover;">
                                <button class="wishlist-btn"><i class="fas fa-heart"></i></button>
                            </div>
                            <div class="cabin-details">
                                <h4>${cabin.name}</h4>
                                <p>${cabin.location}</p>
                                <div class="cabin-meta">
                                    <span><i class="fas fa-user-friends"></i> ${cabin.maxGuests} Guests</span>
                                    <span><i class="fas fa-bed"></i> ${cabin.bedrooms} Bedrooms</span>
                                    <span><i class="fas fa-bath"></i> ${cabin.bathrooms} Bathrooms</span>
                                </div>
                                <p style="font-size: 0.9rem; margin-top: 5px;">${cabin.description}</p>
                            </div>
                            <div class="cabin-footer">
                                <div class="price">₹${cabin.pricePerNight} <span>/night</span></div>
                                <button class="btn primary">Book Now</button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

 <script src="assets/js/user-dashboard.js"></script>
</body>
</html>