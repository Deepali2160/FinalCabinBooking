<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cabinest - Admin Dashboard</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css" />
    <style>
        /* Your existing styles exactly as before */
        .recent-cabins {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        .cabin-item {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            overflow: hidden;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            background: white;
        }
        .cabin-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 20px rgba(0,0,0,0.1);
        }
        .cabin-image img {
            width: 100%;
            height: 180px;
            object-fit: cover;
        }
        .cabin-details {
            padding: 15px;
        }
        .cabin-details h4 {
            margin: 0 0 10px 0;
            font-size: 1.1rem;
            color: #333;
        }
        .cabin-meta {
            display: flex;
            gap: 15px;
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 10px;
            flex-wrap: wrap;
        }
        .cabin-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            border-top: 1px solid #f0f0f0;
        }
        .cabin-footer > div:first-child {
            font-weight: 600;
            color: var(--primary);
        }
        .action-btn {
            background: none;
            border: none;
            cursor: pointer;
            color: var(--primary);
            font-size: 1rem;
            margin-left: 10px;
        }
        .empty-state {
            text-align: center;
            padding: 40px;
            color: #888;
            background: #f9f9f9;
            border-radius: 8px;
        }
        .status {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 0.8rem;
            font-weight: 500;
        }
        .status.available {
            background-color: #e6f7ee;
            color: #00a854;
        }
        .status.unavailable {
            background-color: #fff2f0;
            color: #f5222d;
        }
        .status.confirmed {
            background-color: #e6f7ee;
            color: #00a854;
        }
        .status.pending {
            background-color: #fff7e6;
            color: #fa8c16;
        }
    </style>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <div class="container">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="logo">
                <i class="fas fa-tree"></i>
                <span>Cabinest</span>
            </a>

            <div class="theme-toggle">
                <input type="checkbox" id="theme-switch" hidden />
                <label for="theme-switch" class="switch">
                    <span class="sun"><i class="fas fa-sun"></i></span>
                    <span class="moon"><i class="fas fa-moon"></i></span>
                    <span class="ball"></span>
                </label>
            </div>

            <div class="user-menu">
                <span>Admin User</span>
                <i class="fas fa-user-circle" style="font-size: 1.5rem; margin-left: 10px"></i>
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
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="active"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/cabin"><i class="fas fa-home"></i> Cabins</a></li>
                <li><a href="#"><i class="fas fa-calendar-check"></i> Bookings</a></li>
                <li><a href="#"><i class="fas fa-users"></i> Users</a></li>
                <li><a href="#"><i class="fas fa-chart-line"></i> Reports</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/settings"><i class="fas fa-cog"></i> Settings</a></li>
                <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
            </ul>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <div class="dashboard-header">
                <h1 class="dashboard-title">Admin Dashboard</h1>
                <a href="${pageContext.request.contextPath}/admin/cabin" class="btn primary">Add New Cabin</a>
            </div>

            <!-- Stats Cards -->
            <div class="dashboard-stats">

                <div class="stat-card">
                    <i class="fas fa-home"></i>
                    <h3>Total Cabins</h3>
                    <div class="value">${not empty totalCabins ? totalCabins : '0'}</div>
                    <div class="change up"><i class="fas fa-arrow-up"></i> 12% from last month</div>
                </div>

                <!-- Pending Approvals Stat Card -->
                <div class="stat-card">
                    <i class="fas fa-hourglass-half"></i>
                    <h3>Pending Approvals</h3>
                    <div class="value">
                        <a href="${pageContext.request.contextPath}/admin/booking-approval?action=list"
                           style="text-decoration:none; color:inherit;">
                            <c:out value="${not empty pendingApprovals ? pendingApprovals : '0'}" />
                        </a>
                    </div>
                    <div class="change">
                        <i class="fas fa-exclamation-circle"></i> Approval Required
                    </div>
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
                    </tbody>
                </table>
            </div>

            <!-- Recent Cabins -->
            <div class="recent-bookings">
                <div class="section-header">
                    <h2 class="section-title">Recently Added Cabins</h2>
                    <a href="${pageContext.request.contextPath}/admin/cabin" class="btn primary">View All</a>
                </div>
                <div class="recent-cabins">
                    <c:choose>
                        <c:when test="${not empty recentCabins && fn:length(recentCabins) > 0}">
                            <c:forEach var="cabin" items="${recentCabins}">
                                <div class="cabin-item">
                                    <div class="cabin-image">
                                        <img src="${not empty cabin.imageUrl ? pageContext.request.contextPath.concat('/images/').concat(cabin.imageUrl) : 'https://via.placeholder.com/300x180?text=No+Image'}"
                                             alt="${cabin.name}" />
                                    </div>
                                    <div class="cabin-details">
                                        <h4>${cabin.name}</h4>
                                        <div class="cabin-meta">
                                            <span><i class="fas fa-user-friends"></i> ${cabin.capacity} Guests</span>
                                            <span><i class="fas fa-map-marker-alt"></i> ${cabin.location}</span>
                                            <span><i class="fas fa-dollar-sign"></i> $${cabin.hourlyRate}/hr</span>
                                        </div>
                                        <div class="status ${cabin.isAvailable ? 'available' : 'unavailable'}">
                                            ${cabin.isAvailable ? 'Available' : 'Not Available'}
                                        </div>
                                    </div>
                                    <div class="cabin-footer">
                                        <div>$${cabin.hourlyRate}/hour</div>
                                        <div>
                                            <a href="${pageContext.request.contextPath}/admin/cabin?action=edit&id=${cabin.id}"
                                               class="action-btn" title="Edit">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="${pageContext.request.contextPath}/admin/cabin?action=delete&id=${cabin.id}"
                                               class="action-btn" title="Delete"
                                               onclick="return confirm('Are you sure you want to delete this cabin?')">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-state">
                                <i class="fas fa-home" style="font-size: 2rem; margin-bottom: 10px;"></i>
                                <p>No cabins available</p>
                                <a href="${pageContext.request.contextPath}/admin/cabin" class="btn primary">Add Your First Cabin</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/admin-dashboard.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const deleteButtons = document.querySelectorAll('.action-btn .fa-trash');
            deleteButtons.forEach((btn) => {
                btn.addEventListener('click', function (e) {
                    if (!confirm('Are you sure you want to delete this cabin?')) {
                        e.preventDefault();
                    }
                });
            });
        });
    </script>
</body>
</html>
