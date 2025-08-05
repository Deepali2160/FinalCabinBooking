<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="cabin-details-container">
    <!-- Hidden fields for map initialization -->
    <input type="hidden" id="mapLatitude" value="${cabin.latitude}">
    <input type="hidden" id="mapLongitude" value="${cabin.longitude}">
    <input type="hidden" id="cabinTitle" value="${cabin.name}">

    <!-- Header Section -->
    <div class="cabin-header">
        <div>
            <h2>${cabin.name}</h2>
            <p class="location">
                <i class="fas fa-map-marker-alt"></i> ${cabin.location}
                <c:if test="${not empty cabin.latitude && not empty cabin.longitude}">
                    <button id="viewMapBtn" class="map-btn">
                        <i class="fas fa-map"></i> View on Map
                    </button>
                </c:if>
            </p>
        </div>
        <div class="status-badges">
            <span class="status ${cabin.available ? 'available' : 'unavailable'}">
                ${cabin.available ? 'Available' : 'Not Available'}
            </span>
            <c:if test="${cabin.featured}">
                <span class="featured-badge">
                    <i class="fas fa-star"></i> Featured
                </span>
            </c:if>
        </div>
    </div>

    <!-- Image Gallery -->
    <div class="image-gallery">
        <div class="main-image">
            <c:choose>
                <c:when test="${not empty cabin.imageUrls}">
                    <img src="${pageContext.request.contextPath}${cabin.imageUrls[0].startsWith('/') ? '' : '/'}${cabin.imageUrls[0]}"
                         alt="Main image of ${cabin.name}"
                         id="mainCabinImage">
                </c:when>
                <c:when test="${not empty cabin.imageUrl}">
                    <img src="${pageContext.request.contextPath}${cabin.imageUrl.startsWith('/') ? '' : '/'}${cabin.imageUrl}"
                         alt="Main image of ${cabin.name}"
                         id="mainCabinImage">
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/images/default-cabin.jpg"
                         alt="Default cabin image"
                         id="mainCabinImage">
                </c:otherwise>
            </c:choose>
        </div>

        <div class="thumbnail-container">
            <c:choose>
                <c:when test="${not empty cabin.imageUrls}">
                    <c:forEach items="${cabin.imageUrls}" var="image" varStatus="loop">
                        <div class="thumbnail ${loop.index == 0 ? 'active' : ''}">
                            <img src="${pageContext.request.contextPath}${image.startsWith('/') ? '' : '/'}${image}"
                                 alt="Cabin image ${loop.index + 1}"
                                 data-index="${loop.index}">
                        </div>
                    </c:forEach>
                </c:when>
                <c:when test="${not empty cabin.imageUrl}">
                    <div class="thumbnail active">
                        <img src="${pageContext.request.contextPath}${cabin.imageUrl.startsWith('/') ? '' : '/'}${cabin.imageUrl}"
                             alt="Single image">
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="thumbnail active">
                        <img src="${pageContext.request.contextPath}/images/default-cabin.jpg"
                             alt="Default thumbnail">
                    </div>
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
                            <span class="spec-value">₹${cabin.pricePerNight}/night</span>
                        </div>
                    </div>
                    <div class="spec-item">
                        <i class="fas fa-users"></i>
                        <div>
                            <span class="spec-label">Guests</span>
                            <span class="spec-value">${cabin.maxGuests}</span>
                        </div>
                    </div>
                    <div class="spec-item">
                        <i class="fas fa-bed"></i>
                        <div>
                            <span class="spec-label">Bedrooms</span>
                            <span class="spec-value">${cabin.bedrooms}</span>
                        </div>
                    </div>
                    <div class="spec-item">
                        <i class="fas fa-shower"></i>
                        <div>
                            <span class="spec-label">Bathrooms</span>
                            <span class="spec-value">${cabin.bathrooms}</span>
                        </div>
                    </div>
                </div>
            </div>

            <div id="cabinMapContainer" class="map-container">
                <div id="cabinMap"></div>
                <div class="map-overlay" id="mapOverlay">
                    <button class="btn primary" id="showMapBtn">
                        <i class="fas fa-map-marked-alt"></i> Show Map
                    </button>
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

    .map-btn {
        background: none;
        border: none;
        color: #3498db;
        cursor: pointer;
        font-size: 14px;
        margin-left: 10px;
        display: inline-flex;
        align-items: center;
        gap: 5px;
    }

    .map-btn:hover {
        text-decoration: underline;
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

    .featured-badge {
        background: #fff3e0;
        color: #f39c12;
        padding: 5px 15px;
        border-radius: 20px;
        font-size: 14px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 5px;
    }

    /* Image Gallery Styles */
    .image-gallery {
        margin-bottom: 30px;
    }

    .main-image {
        height: 400px;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        margin-bottom: 15px;
    }

    .main-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        transition: opacity 0.3s ease;
    }

    .thumbnail-container {
        display: flex;
        gap: 10px;
        overflow-x: auto;
        padding-bottom: 10px;
    }

    .thumbnail {
        width: 80px;
        height: 60px;
        border-radius: 5px;
        overflow: hidden;
        cursor: pointer;
        opacity: 0.6;
        transition: all 0.3s ease;
        flex-shrink: 0;
        border: 2px solid transparent;
    }

    .thumbnail:hover {
        opacity: 0.8;
        transform: scale(1.05);
    }

    .thumbnail.active {
        opacity: 1;
        border-color: #3498db;
    }

    .thumbnail img {
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

    .map-container {
        position: relative;
        background: #f5f5f5;
        border-radius: 10px;
        overflow: hidden;
        height: 300px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
    }

    #cabinMap {
        height: 100%;
        width: 100%;
    }

    .map-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.2);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10;
    }

    .btn.primary {
        background-color: #3498db;
        color: white;
        border: none;
        padding: 10px 20px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 14px;
        display: flex;
        align-items: center;
        gap: 8px;
        transition: background-color 0.3s;
    }

    .btn.primary:hover {
        background-color: #2980b9;
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

        .thumbnail-container {
            gap: 8px;
        }

        .thumbnail {
            width: 70px;
            height: 50px;
        }
    }
</style>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Enhanced Image Gallery Functionality
        const thumbnails = document.querySelectorAll('.thumbnail img');
        const mainImage = document.getElementById('mainCabinImage');

        // Function to change main image
        function setMainImage(src) {
            if (!mainImage) return;

            // Add fade effect
            mainImage.style.opacity = '0';

            setTimeout(() => {
                mainImage.src = src;
                mainImage.style.opacity = '1';
            }, 200);

            // Update active thumbnail
            thumbnails.forEach(thumb => {
                const thumbnailContainer = thumb.closest('.thumbnail');
                if (thumbnailContainer) {
                    thumbnailContainer.classList.remove('active');
                    if (thumb.src === src) {
                        thumbnailContainer.classList.add('active');
                    }
                }
            });
        }

        // Add click event to thumbnails
        thumbnails.forEach(thumb => {
            thumb.addEventListener('click', function() {
                setMainImage(this.src);
            });

            // Error handling
            thumb.addEventListener('error', function() {
                console.error('Failed to load thumbnail:', this.src);
                const defaultImg = '${pageContext.request.contextPath}/images/default-cabin.jpg';
                if (!this.src.includes('default-cabin.jpg')) {
                    this.src = defaultImg;
                }
                this.closest('.thumbnail').style.display = 'none';
            });
        });

        // Main image error handling
        if (mainImage) {
            mainImage.addEventListener('error', function() {
                console.error('Failed to load main image:', this.src);
                this.src = '${pageContext.request.contextPath}/images/default-cabin.jpg';
            });
        }

        // Google Maps Initialization
        function initMap(forceShow = false) {
            const latEl = document.getElementById('mapLatitude');
            const lngEl = document.getElementById('mapLongitude');
            const overlay = document.getElementById('mapOverlay');

            if (!latEl || !lngEl) return;

            const lat = parseFloat(latEl.value);
            const lng = parseFloat(lngEl.value);

            if (isNaN(lat) || isNaN(lng)) {
                console.error("Invalid coordinates:", lat, lng);
                if (overlay) overlay.innerHTML = '<p>Map not available</p>';
                return;
            }

            if (forceShow && overlay) {
                overlay.style.display = 'none';
            }

            const cabinLocation = { lat: lat, lng: lng };
            const map = new google.maps.Map(document.getElementById("cabinMap"), {
                zoom: 15,
                center: cabinLocation,
                mapTypeId: 'hybrid',
                styles: [{
                    featureType: "poi",
                    stylers: [{ visibility: "off" }]
                }]
            });

            new google.maps.marker.AdvancedMarkerElement({
                position: cabinLocation,
                map: map,
                title: document.getElementById('cabinTitle')?.value || 'Cabin Location'
            });
        }

        // Map button handlers
        document.getElementById('viewMapBtn')?.addEventListener('click', function() {
            const overlay = document.getElementById('mapOverlay');
            if (overlay) overlay.style.display = 'none';
            initMap();
        });

        document.getElementById('showMapBtn')?.addEventListener('click', function() {
            const overlay = document.getElementById('mapOverlay');
            if (overlay) overlay.style.display = 'none';
            initMap();
        });

        // Initialize map if coordinates exist and no overlay
        if (!document.getElementById('mapOverlay') &&
            document.getElementById('mapLatitude') &&
            document.getElementById('mapLongitude')) {
            initMap(true);
        }
    });
</script>

<!-- Load Google Maps API with your key -->
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&loading=async&callback=initMap" async defer></script>
