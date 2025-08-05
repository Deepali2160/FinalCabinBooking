<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="cabin-details-container">
    <!-- Header Section -->
    <div class="cabin-header">
        <div>
            <h2>${cabin.name}</h2>
            <p class="location">
                <i class="fas fa-map-marker-alt"></i> ${cabin.location}
            </p>
        </div>
        <div class="status-badges">
            <span class="status ${cabin.available ? 'available' : 'unavailable'}">
                ${cabin.available ? 'Available' : 'Not Available'}
            </span>
        </div>
    </div>

    <!-- Image Section -->
    <div class="image-section">
        <div class="main-image">
            <c:choose>
                <c:when test="${not empty cabin.imageUrl}">
                    <<img src="${pageContext.request.contextPath}/${cabin.imageUrl}" alt="Cabin Image"
                         id="mainCabinImage">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/default-cabin.jpg"
                         alt="Default cabin image"
                         id="mainCabinImage">
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Details Section -->
    <div class="details-section">
        <div class="left-column">
            <div class="description-box">
                <h3><i class="fas fa-info-circle"></i> Description</h3>
                <p>${not empty cabin.description ? cabin.description : 'No description available.'}</p>
            </div>

            <div class="amenities-box">
                <h3><i class="fas fa-umbrella-beach"></i> Amenities</h3>
                <div class="amenities-grid">
                    <c:choose>
                        <c:when test="${not empty cabin.amenities}">
                            <c:forTokens items="${cabin.amenities}" delims="," var="amenity">
                                <div class="amenity-item">
                                    <i class="fas fa-check"></i>
                                    <span>${amenity}</span>
                                </div>
                            </c:forTokens>
                        </c:when>
                        <c:otherwise>
                            <p>No amenities listed.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="right-column">
            <div class="specs-box">
                <h3><i class="fas fa-home"></i> Specifications</h3>
                <div class="specs-grid">
                    <div class="spec-item">
                        <i class="fas fa-rupee-sign"></i>
                        <div>
                            <span class="spec-label">Price</span>
                            <span class="spec-value">₹${cabin.hourlyRate}/hour</span>
                        </div>
                    </div>
                    <div class="spec-item">
                        <i class="fas fa-users"></i>
                        <div>
                            <span class="spec-label">Capacity</span>
                            <span class="spec-value">${cabin.capacity}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    .cabin-details-container {
        font-family: 'Poppins', sans-serif;
        color: #333;
        max-width: 1200px;
        margin: 0 auto;
        padding: 20px;
    }

    .cabin-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 25px;
        flex-wrap: wrap;
        gap: 15px;
    }

    .cabin-header h2 {
        margin: 0;
        font-size: 28px;
        color: #2c3e50;
        font-weight: 600;
    }

    .location {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-top: 5px;
        color: #7f8c8d;
        font-size: 16px;
    }

    .status-badges {
        display: flex;
        gap: 10px;
    }

    .status {
        padding: 5px 15px;
        border-radius: 20px;
        font-size: 14px;
        font-weight: 600;
    }

    .status.available {
        background: #e8f5e9;
        color: #27ae60;
    }

    .status.unavailable {
        background: #ffebee;
        color: #e74c3c;
    }

    /* Image Section Styles */
    .image-section {
        margin-bottom: 30px;
    }

    .main-image {
        height: 400px;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .main-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    /* Details Section Styles */
    .details-section {
        display: grid;
        grid-template-columns: 2fr 1fr;
        gap: 30px;
    }

    .description-box {
        background: #fff;
        border-radius: 10px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        margin-bottom: 20px;
    }

    .description-box h3 {
        margin-top: 0;
        margin-bottom: 15px;
        font-size: 20px;
        color: #2c3e50;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .amenities-box {
        background: #fff;
        border-radius: 10px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    .amenities-box h3 {
        margin-top: 0;
        margin-bottom: 15px;
        font-size: 20px;
        color: #2c3e50;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .amenities-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;
    }

    .amenity-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 15px;
    }

    .amenity-item i {
        color: #27ae60;
    }

    .specs-box {
        background: #fff;
        border-radius: 10px;
        padding: 20px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
        margin-bottom: 20px;
    }

    .specs-box h3 {
        margin-top: 0;
        margin-bottom: 20px;
        font-size: 20px;
        color: #2c3e50;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .specs-grid {
        display: grid;
        grid-template-columns: 1fr;
        gap: 15px;
    }

    .spec-item {
        display: flex;
        align-items: center;
        gap: 15px;
    }

    .spec-item i {
        font-size: 20px;
        color: #3498db;
        width: 30px;
        text-align: center;
    }

    .spec-label {
        display: block;
        font-size: 13px;
        color: #7f8c8d;
    }

    .spec-value {
        display: block;
        font-size: 16px;
        font-weight: 500;
        color: #2c3e50;
    }

    @media (max-width: 992px) {
        .details-section {
            grid-template-columns: 1fr;
        }

        .main-image {
            height: 350px;
        }
    }

    @media (max-width: 768px) {
        .amenities-grid {
            grid-template-columns: 1fr;
        }

        .main-image {
            height: 300px;
        }
    }
</style>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Main image error handling
        const mainImage = document.getElementById('mainCabinImage');
        if (mainImage) {
            mainImage.addEventListener('error', function() {
                console.error('Failed to load main image:', this.src);
                this.src = '${pageContext.request.contextPath}/images/default-cabin.jpg';
            });
        }
    });
</script>