<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cabinest - Admin Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../assets/css/admin-dashboard.css">
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
                <span>Admin User</span>
                <i class="fas fa-user-circle" style="font-size: 1.5rem; margin-left: 10px;"></i>
            </div>
        </div>
    </nav>

    <!-- Dashboard Layout -->
    <div class="dashboard">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <h3>Admin Dashboard</h3>
                <p>Manage your cabin bookings</p>
            </div>
            <ul class="sidebar-menu">
                <li><a href="#" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="#"><i class="fas fa-home"></i> Cabins</a></li>
                <li><a href="#"><i class="fas fa-calendar-check"></i> Bookings</a></li>
                <li><a href="#"><i class="fas fa-users"></i> Users</a></li>
                <li><a href="#"><i class="fas fa-chart-line"></i> Reports</a></li>
                <li><a href="#"><i class="fas fa-cog"></i> Settings</a></li>
                <li><a href="#"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <div class="dashboard-header">
                <h1 class="dashboard-title">Admin Dashboard</h1>
                <button class="btn primary">Add New Cabin</button>
            </div>

            <!-- Stats Cards -->
            <div class="dashboard-stats">
                <div class="stat-card">
                    <i class="fas fa-home"></i>
                    <h3>Total Cabins</h3>
                    <div class="value">42</div>
                    <div class="change up"><i class="fas fa-arrow-up"></i> 12% from last month</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-calendar-check"></i>
                    <h3>Total Bookings</h3>
                    <div class="value">156</div>
                    <div class="change up"><i class="fas fa-arrow-up"></i> 8% from last month</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-users"></i>
                    <h3>Total Users</h3>
                    <div class="value">89</div>
                    <div class="change up"><i class="fas fa-arrow-up"></i> 5% from last month</div>
                </div>
                <div class="stat-card">
                    <i class="fas fa-dollar-sign"></i>
                    <h3>Total Revenue</h3>
                    <div class="value">$24,589</div>
                    <div class="change down"><i class="fas fa-arrow-down"></i> 3% from last month</div>
                </div>
            </div>

            <!-- Recent Bookings -->
            <div class="recent-bookings">
                <div class="section-header">
                    <h2 class="section-title">Recent Bookings</h2>
                    <a href="#" class="btn primary">View All</a>
                </div>
                <table>
                    <thead>
                        <tr>
                            <th>Booking ID</th>
                            <th>Cabin</th>
                            <th>User</th>
                            <th>Dates</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>#CB-1256</td>
                            <td>Mountain View Lodge</td>
                            <td>john.doe@example.com</td>
                            <td>Jul 15 - Jul 20, 2023</td>
                            <td>$1,445</td>
                            <td><span class="status confirmed">Confirmed</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>#CB-1255</td>
                            <td>Lakeside Retreat</td>
                            <td>sarah.smith@example.com</td>
                            <td>Jul 18 - Jul 22, 2023</td>
                            <td>$1,316</td>
                            <td><span class="status pending">Pending</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>#CB-1254</td>
                            <td>Forest Haven</td>
                            <td>mike.johnson@example.com</td>
                            <td>Jul 20 - Jul 25, 2023</td>
                            <td>$1,995</td>
                            <td><span class="status confirmed">Confirmed</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                            </td>
                        </tr>
                        <tr>
                            <td>#CB-1253</td>
                            <td>Mountain View Lodge</td>
                            <td>emily.wilson@example.com</td>
                            <td>Jul 22 - Jul 27, 2023</td>
                            <td>$1,445</td>
                            <td><span class="status cancelled">Cancelled</span></td>
                            <td>
                                <button class="action-btn" title="View"><i class="fas fa-eye"></i></button>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Recent Cabins -->
            <div class="recent-bookings">
                <div class="section-header">
                    <h2 class="section-title">Recently Added Cabins</h2>
                    <a href="#" class="btn primary">View All</a>
                </div>
                <div class="recent-cabins">
                    <div class="cabin-item">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1580048915913-4f8f5cb481c2?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Mountain View Cabin">
                        </div>
                        <div class="cabin-details">
                            <h4>Mountain View Lodge</h4>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 6 Guests</span>
                                <span><i class="fas fa-bed"></i> 3 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div>$289 /night</div>
                            <div>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                                <button class="action-btn" title="Delete"><i class="fas fa-trash"></i></button>
                            </div>
                        </div>
                    </div>
                    <div class="cabin-item">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1591825729269-caeb344f6df2?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Lakeside Retreat">
                        </div>
                        <div class="cabin-details">
                            <h4>Lakeside Retreat</h4>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 4 Guests</span>
                                <span><i class="fas fa-bed"></i> 2 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div>$329 /night</div>
                            <div>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                                <button class="action-btn" title="Delete"><i class="fas fa-trash"></i></button>
                            </div>
                        </div>
                    </div>
                    <div class="cabin-item">
                        <div class="cabin-image">
                            <img src="https://images.unsplash.com/photo-1613490493576-7fde63acd811?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&w=800" alt="Forest Haven">
                        </div>
                        <div class="cabin-details">
                            <h4>Forest Haven</h4>
                            <div class="cabin-meta">
                                <span><i class="fas fa-user-friends"></i> 8 Guests</span>
                                <span><i class="fas fa-bed"></i> 4 Bedrooms</span>
                            </div>
                        </div>
                        <div class="cabin-footer">
                            <div>$399 /night</div>
                            <div>
                                <button class="action-btn" title="Edit"><i class="fas fa-edit"></i></button>
                                <button class="action-btn" title="Delete"><i class="fas fa-trash"></i></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <script src="../assets/js/admin-dashboard.js"></script>

</body>
</html>