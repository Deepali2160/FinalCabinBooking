<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Cabins - Admin Dashboard</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&family=Montserrat:wght@700;800&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-dashboard.css" />
  <style>
    /* Modal and common styles */
    .modal {
      position: fixed;
      top:0; left:0; right:0; bottom:0;
      background: rgba(0,0,0,0.4);
      display: none;
      justify-content: center;
      align-items: center;
      z-index: 10000;
    }
    .modal-content {
      background: var(--light);
      border-radius: var(--border-radius);
      box-shadow: var(--box-shadow);
      padding: 20px 30px;
      max-width: 600px;
      width: 100%;
      position: relative;
      max-height: 90vh;
      overflow-y: auto;
      transition: transform 0.3s ease;
    }
    body.dark-theme .modal-content {
      background: #2d2d2d;
      color: #eee;
    }
    .modal-content .close {
      position: absolute;
      right: 20px; top: 15px;
      font-size: 1.5rem;
      font-weight: 600;
      cursor: pointer;
      user-select: none;
      color: var(--primary);
    }
    .modal label {
      display: block;
      margin: 12px 0 6px;
      font-weight: 600;
      color: var(--dark);
    }
    body.dark-theme .modal label {
      color: #eee;
    }
    .modal input[type="text"],
    .modal input[type="number"],
    .modal input[type="file"],
    .modal textarea {
      width: 100%;
      padding: 8px 12px;
      border-radius: var(--border-radius);
      border: 1px solid var(--light-gray);
      font-size: 1rem;
      transition: border-color 0.2s ease;
    }
    .modal textarea {
      resize: vertical;
      min-height: 80px;
    }
    .modal input[type="checkbox"] {
      transform: scale(1.2);
      margin-right: 6px;
      vertical-align: middle;
    }
    .modal input:focus,
    .modal textarea:focus {
      outline: none;
      border-color: var(--primary);
    }
    #imagePreview {
      max-width: 150px;
      margin-top: 10px;
      border-radius: 12px;
      box-shadow: var(--box-shadow);
      display: none;
    }
    .alert {
      padding: 12px 20px;
      margin-bottom: 20px;
      border-radius: var(--border-radius);
      font-weight: 600;
    }
    .alert.success {
      background: rgba(46, 204, 113, 0.15);
      color: #27ae60;
      border: 1px solid #27ae60;
    }
    .alert.error {
      background: rgba(231, 76, 60, 0.15);
      color: #c0392b;
      border: 1px solid #c0392b;
    }

    /* Cabin item styles */
    .cabin-item {
      border: 1px solid #ddd;
      border-radius: 10px;
      padding: 16px;
      margin-bottom: 16px;
      display: flex;
      gap: 20px;
      background: var(--light);
      box-shadow: var(--box-shadow);
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s;
    }
    .cabin-item:hover {
      transform: translateY(-2px);
      box-shadow: 0 5px 15px rgba(0,0,0,0.1);
    }
    .cabin-image img {
      width: 140px;
      height: 100px;
      object-fit: cover;
      border-radius: 8px;
    }
    .cabin-details {
      flex: 1;
    }
    .cabin-meta span {
      margin-right: 20px;
      font-size: 0.9rem;
      color: #555;
    }
    .status {
      margin-top: 6px;
      padding: 4px 12px;
      border-radius: 20px;
      display: inline-block;
      font-weight: 600;
      font-size: 0.9rem;
    }
    .cabin-footer {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: flex-end;
      min-width: 100px;
    }
    .cabin-footer > div:first-child {
      font-weight: 700;
      font-size: 1.1rem;
      margin-bottom: 10px;
    }
    .action-btn {
      background: none;
      border: none;
      cursor: pointer;
      font-size: 1.1rem;
      margin-left: 8px;
      color: var(--primary);
    }
    .error-input {
      border-color: #e74c3c !important;
    }

    /* Detailed View Modal */
    #detailModal {
      z-index: 10001;
    }
    .detail-modal-content {
      max-width: 900px;
      width: 90%;
      padding: 30px;
    }
    .cabin-gallery {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
      gap: 15px;
      margin-bottom: 20px;
    }
    .cabin-gallery img {
      width: 100%;
      height: 180px;
      object-fit: cover;
      border-radius: 8px;
      cursor: pointer;
      transition: transform 0.3s;
    }
    .cabin-gallery img:hover {
      transform: scale(1.03);
    }
    .cabin-info {
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 30px;
    }
    .cabin-highlights {
      background: #f8f9fa;
      padding: 20px;
      border-radius: 8px;
    }
    .amenities-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 10px;
      margin-top: 15px;
    }
    .amenity-item {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;
    }
    #cabinMap {
      height: 300px;
      width: 100%;
      border-radius: 8px;
      margin-top: 20px;
      background: #f5f5f5;
      border: 1px solid #ddd;
    }
    .more-content {
      display: none;
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #eee;
    }
    .toggle-more {
      background: none;
      border: none;
      color: var(--primary);
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 5px;
      margin-top: 10px;
      font-size: 0.9rem;
    }
    .cabin-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>
  <nav class="navbar">
    <div class="container">
      <a href="#" class="logo">
        <i class="fas fa-tree"></i> Cabinest
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
        Admin User <i class="fas fa-user-circle"></i>
      </div>
    </div>
  </nav>

  <div class="dashboard">
    <aside class="sidebar">
      <div class="sidebar-header">
        <h3>Admin Dashboard</h3>
        <p>Manage your cabin bookings</p>
      </div>
      <ul class="sidebar-menu">
        <li><a href="${pageContext.request.contextPath}/admin/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
        <li><a href="cabin.jsp" class="active"><i class="fas fa-home"></i> Cabins</a></li>
        <li><a href="#"><i class="fas fa-calendar-check"></i> Bookings</a></li>
        <li><a href="#"><i class="fas fa-users"></i> Users</a></li>
        <li><a href="#"><i class="fas fa-chart-line"></i> Reports</a></li>
        <li><a href="${pageContext.request.contextPath}/admin/settings"><i class="fas fa-cog"></i>Settings</a></li>
        <li><a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a></li>
      </ul>
    </aside>

    <main class="main-content">
      <div class="dashboard-header">
        <h1 class="dashboard-title">Cabin Management</h1>
        <button id="addCabinBtn" class="btn primary"><i class="fas fa-plus"></i> Add New</button>
      </div>

      <c:if test="${not empty message}">
        <div class="alert success">${message}</div>
      </c:if>
      <c:if test="${not empty error}">
        <div class="alert error">${error}</div>
      </c:if>

      <div class="cabins-list">
        <c:choose>
          <c:when test="${not empty cabinsList}">
            <c:forEach var="cabin" items="${cabinsList}">
              <div class="cabin-item" onclick="showCabinDetails(${cabin.id})">
                <div class="cabin-image">
                  <img src="${pageContext.request.contextPath}/${cabin.imageUrl}" alt="${fn:escapeXml(cabin.name)}" loading="lazy"/>
                </div>
                <div class="cabin-details">
                  <h4>${fn:escapeXml(cabin.name)}</h4>
                  <div class="cabin-meta">
                    <span><i class="fas fa-user-friends"></i> ${cabin.maxGuests} Guests</span>
                    <span><i class="fas fa-bed"></i> ${cabin.bedrooms} Bedrooms</span>
                    <span><i class="fas fa-shower"></i> ${cabin.bathrooms} Baths</span>
                  </div>
                  <p>${fn:escapeXml(cabin.amenities)}</p>
                  <div class="status" style="${cabin.available ? 'background:#e2f0d9;color:#27ae60;' : 'background:#f7d6d8;color:#e74c3c;'}">
                    ${cabin.available ? 'Available' : 'Not Available'}
                    <c:if test="${cabin.featured}">
                      <span style="margin-left: 10px; color: #f1c40f;"><i class="fas fa-star"></i> Featured</span>
                    </c:if>
                  </div>
                </div>
                <div class="cabin-footer">
                  <div>₹${cabin.pricePerNight} / night</div>
                  <div>
                    <button type="button" class="action-btn edit-btn" title="Edit"
                      data-id="${cabin.id}"
                      data-name="${fn:escapeXml(cabin.name)}"
                      data-description="${fn:escapeXml(cabin.description)}"
                      data-location="${fn:escapeXml(cabin.location)}"
                      data-price="${cabin.pricePerNight}"
                      data-maxguests="${cabin.maxGuests}"
                      data-bedrooms="${cabin.bedrooms}"
                      data-bathrooms="${cabin.bathrooms}"
                      data-amenities="${fn:escapeXml(cabin.amenities)}"
                      data-imageurl="${cabin.imageUrl}"
                      data-available="${cabin.available}"
                      data-featured="${cabin.featured}"
                      onclick="event.stopPropagation()">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button type="button" class="action-btn delete-btn" title="Delete" data-id="${cabin.id}"
                      onclick="event.stopPropagation()">
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                  <div style="display:flex;gap:8px; margin-top: 10px;">
                    <form action="${pageContext.request.contextPath}/admin/cabin" method="post" style="display:inline"
                      onclick="event.stopPropagation()">
                      <input type="hidden" name="action" value="toggleAvailability"/>
                      <input type="hidden" name="id" value="${cabin.id}"/>
                      <input type="hidden" name="available" value="${!cabin.available}"/>
                      <label class="switch" title="Toggle Availability">
                        <input type="checkbox" ${cabin.available ? 'checked' : ''} onchange="this.form.submit()">
                        <span class="slider round"></span>
                      </label>
                    </form>
                    <form action="${pageContext.request.contextPath}/admin/cabin" method="post" style="display:inline"
                      onclick="event.stopPropagation()">
                      <input type="hidden" name="action" value="toggleFeatured"/>
                      <input type="hidden" name="id" value="${cabin.id}"/>
                      <input type="hidden" name="featured" value="${!cabin.featured}"/>
                      <label class="switch" title="Toggle Featured">
                        <input type="checkbox" ${cabin.featured ? 'checked' : ''} onchange="this.form.submit()">
                        <span class="slider round"></span>
                      </label>
                    </form>
                  </div>
                </div>
              </div>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <p style="text-align:center; margin-top:40px; color:#888;">No cabins available to show.</p>
          </c:otherwise>
        </c:choose>
      </div>
    </main>
  </div>

  <!-- Add/Edit Modal -->
  <div id="cabinModal" class="modal" aria-hidden="true" role="dialog" aria-modal="true">
    <div class="modal-content" role="document">
      <span id="modalCloseBtn" class="close" title="Close">&times;</span>
      <h2 id="modalTitle">Add New Cabin</h2>
      <form id="cabinForm" action="${pageContext.request.contextPath}/admin/cabin" method="post" enctype="multipart/form-data" novalidate>
        <input type="hidden" name="action" id="formAction" value="add" />
        <input type="hidden" name="id" id="cabinId" />

        <label for="cabinName">Name</label>
        <input type="text" id="cabinName" name="name" maxlength="100" required placeholder="Cabin Name" />

        <label for="cabinDescription">Description</label>
        <textarea id="cabinDescription" name="description" maxlength="1000" required placeholder="Describe the cabin"></textarea>

        <label for="cabinLocation">Location</label>
        <input type="text" id="cabinLocation" name="location" required placeholder="Location" />

        <label for="latitude">Latitude</label>
        <input type="text" id="latitude" name="latitude" placeholder="e.g., 30.123456" />

        <label for="longitude">Longitude</label>
        <input type="text" id="longitude" name="longitude" placeholder="e.g., 78.123456" />

        <div style="display:flex; gap:10px;">
          <div style="flex:1;">
            <label for="cabinPrice">Price per Night</label>
            <input type="number" id="cabinPrice" name="pricePerNight" required min="100" step="0.01" placeholder="Price" />
          </div>
          <div style="flex:1;">
            <label for="cabinMaxGuests">Guests</label>
            <input type="number" id="cabinMaxGuests" name="maxGuests" required min="1" max="20" placeholder="Guests" />
          </div>
        </div>

        <div style="display:flex; gap:10px; margin-top:10px;">
          <div style="flex:1;">
            <label for="cabinBedrooms">Bedrooms</label>
            <input type="number" id="cabinBedrooms" name="bedrooms" required min="1" max="10" placeholder="Bedrooms" />
          </div>
          <div style="flex:1;">
            <label for="cabinBathrooms">Bathrooms</label>
            <input type="number" id="cabinBathrooms" name="bathrooms" required min="1" max="10" placeholder="Bathrooms" />
          </div>
        </div>

        <label for="cabinAmenities" style="margin-top:10px;">Amenities (comma separated)</label>
        <input type="text" id="cabinAmenities" name="amenities" required placeholder="Wifi, Parking, Fireplace" />

        <label for="cabinImage" style="margin-top:10px;">Primary Image (JPG/PNG max 5MB)</label>
        <input type="file" id="cabinImage" name="image" accept="image/*" />
        <img id="imagePreview" alt="Image Preview" />

        <label for="additionalImages" style="margin-top:10px;">Additional Images (Multiple select)</label>
        <input type="file" id="additionalImages" name="additionalImages" accept="image/*" multiple />

        <div style="margin-top:15px;">
          <label><input type="checkbox" id="cabinAvailable" name="isAvailable" value="true" checked /> Available</label>
          <label style="margin-left:20px;"><input type="checkbox" id="cabinFeatured" name="isFeatured" value="true" /> Featured</label>
        </div>

        <button type="submit" class="btn primary" style="margin-top:15px;">Save</button>
      </form>
    </div>
  </div>

  <!-- Delete Confirmation Modal -->
  <div id="deleteModal" class="modal" aria-hidden="true" role="dialog" aria-modal="true">
    <div class="modal-content" role="document" style="max-width:400px; text-align:center;">
      <span id="deleteCloseBtn" class="close" title="Close">&times;</span>
      <h3>Are you sure you want to delete this cabin?</h3>
      <form id="deleteForm" action="${pageContext.request.contextPath}/admin/cabin" method="post">
        <input type="hidden" name="action" value="delete" />
        <input type="hidden" id="deleteCabinId" name="id" />
        <button type="submit" class="btn primary" style="margin:15px 0;">Yes, Delete</button>
      </form>
      <button type="button" id="deleteCancelBtn" class="btn">Cancel</button>
    </div>
  </div>

  <!-- Detailed View Modal -->
  <div id="detailModal" class="modal" aria-hidden="true" role="dialog" aria-modal="true">
    <div class="modal-content detail-modal-content" role="document">
      <span id="detailCloseBtn" class="close" title="Close">&times;</span>
      <div id="detailContent">
        <!-- Content will be loaded via AJAX -->
      </div>
    </div>
  </div>

<script src="${pageContext.request.contextPath}/assets/js/admin-dashboard.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_ACTUAL_API_KEY&callback=initMap" async defer></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const contextPath = '${pageContext.request.contextPath}';
        const cabinModal = document.getElementById('cabinModal');
        const modalCloseBtn = document.getElementById('modalCloseBtn');
        const addCabinBtn = document.getElementById('addCabinBtn');
        const cabinForm = document.getElementById('cabinForm');
        const formAction = document.getElementById('formAction');
        const idInput = document.getElementById('cabinId');
        const imageInput = document.getElementById('cabinImage');
        const imagePreview = document.getElementById('imagePreview');
        const deleteModal = document.getElementById('deleteModal');
        const deleteCloseBtn = document.getElementById('deleteCloseBtn');
        const deleteCancelBtn = document.getElementById('deleteCancelBtn');
        const deleteForm = document.getElementById('deleteForm');
        const deleteIdInput = document.getElementById('deleteCabinId');
        const detailModal = document.getElementById('detailModal');
        const detailCloseBtn = document.getElementById('detailCloseBtn');

        // Open add modal
        addCabinBtn.addEventListener('click', function(e) {
            e.preventDefault();
            cabinForm.reset();
            formAction.value = 'add';
            idInput.value = '';
            document.getElementById('modalTitle').textContent = 'Add New Cabin';
            imagePreview.src = '';
            imagePreview.style.display = 'none';

            // Reset checkboxes
            document.getElementById('cabinAvailable').checked = true;
            document.getElementById('cabinFeatured').checked = false;

            cabinModal.style.display = 'flex';
        });

        // Close modals
        modalCloseBtn.addEventListener('click', () => cabinModal.style.display = 'none');
        deleteCloseBtn.addEventListener('click', () => deleteModal.style.display = 'none');
        deleteCancelBtn.addEventListener('click', () => deleteModal.style.display = 'none');
        detailCloseBtn.addEventListener('click', () => detailModal.style.display = 'none');

        // Close modal on outside click
        window.addEventListener('click', e => {
            if (e.target === cabinModal) cabinModal.style.display = 'none';
            if (e.target === deleteModal) deleteModal.style.display = 'none';
            if (e.target === detailModal) detailModal.style.display = 'none';
        });

        // Image preview
        imageInput.addEventListener('change', function() {
            const file = this.files[0];
            if(file && file.type.startsWith('image/')) {
                imagePreview.src = URL.createObjectURL(file);
                imagePreview.style.display = 'block';
            } else {
                imagePreview.src = '';
                imagePreview.style.display = 'none';
            }
        });

        // Handle edit
        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                formAction.value = 'edit';
                idInput.value = this.dataset.id;
                document.getElementById('modalTitle').textContent = 'Edit Cabin';

                // Populate form fields
                document.getElementById('cabinName').value = this.dataset.name || '';
                document.getElementById('cabinDescription').value = this.dataset.description || '';
                document.getElementById('cabinLocation').value = this.dataset.location || '';
                document.getElementById('cabinPrice').value = this.dataset.price || '';
                document.getElementById('cabinMaxGuests').value = this.dataset.maxguests || '';
                document.getElementById('cabinBedrooms').value = this.dataset.bedrooms || '';
                document.getElementById('cabinBathrooms').value = this.dataset.bathrooms || '';
                document.getElementById('cabinAmenities').value = this.dataset.amenities || '';
                document.getElementById('cabinAvailable').checked = this.dataset.available === 'true';
                document.getElementById('cabinFeatured').checked = this.dataset.featured === 'true';

                if(this.dataset.imageurl) {
                    imagePreview.src = contextPath + '/' + this.dataset.imageurl;
                    imagePreview.style.display = 'block';
                } else {
                    imagePreview.src = '';
                    imagePreview.style.display = 'none';
                }
                cabinModal.style.display = 'flex';
            });
        });

        // Handle delete
        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                deleteIdInput.value = this.dataset.id;
                deleteModal.style.display = 'flex';
            });
        });

        // Form validation
        cabinForm.addEventListener('submit', function(e) {
            let isValid = true;
            const fields = [
                'cabinName', 'cabinDescription', 'cabinLocation',
                'cabinPrice', 'cabinMaxGuests', 'cabinBedrooms',
                'cabinBathrooms', 'cabinAmenities'
            ];

            // Reset error states
            fields.forEach(field => {
                document.getElementById(field).classList.remove('error-input');
            });

            // Validate each field
            fields.forEach(field => {
                const el = document.getElementById(field);
                if (!el.value || !el.value.trim()) {
                    el.classList.add('error-input');
                    isValid = false;
                }
            });

            // Validate price
            const priceEl = document.getElementById('cabinPrice');
            const price = parseFloat(priceEl.value);
            if (isNaN(price) || price < 100) {
                priceEl.classList.add('error-input');
                isValid = false;
            }

            if (!isValid) {
                e.preventDefault();
                alert('Please fill all required fields correctly. Price must be at least 100.');
            }
        });

        // Auto-open modal if error exists
        <c:if test="${not empty error}">
            document.getElementById('addCabinBtn').click();
        </c:if>
    });

    // Show cabin details
    function showCabinDetails(cabinId) {
        fetch('${pageContext.request.contextPath}/admin/cabin?action=viewDetails&id=' + cabinId)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                document.getElementById('detailContent').innerHTML = html;
                document.getElementById('detailModal').style.display = 'flex';

                // Initialize map if coordinates exist
                const latEl = document.getElementById('mapLatitude');
                const lngEl = document.getElementById('mapLongitude');

                if (latEl && lngEl) {
                    const lat = parseFloat(latEl.value);
                    const lng = parseFloat(lngEl.value);

                    if (!isNaN(lat) && !isNaN(lng)) {
                        const cabinLocation = { lat: lat, lng: lng };
                        const map = new google.maps.Map(document.getElementById("cabinMap"), {
                            zoom: 15,
                            center: cabinLocation,
                            mapTypeId: 'hybrid'
                        });

                        new google.maps.Marker({
                            position: cabinLocation,
                            map: map,
                            title: document.getElementById('cabinTitle')?.value || 'Cabin Location',
                            icon: {
                                url: 'https://maps.google.com/mapfiles/ms/icons/red-dot.png'
                            }
                        });
                    }
                }

                // Toggle more content
                document.querySelectorAll('.toggle-more').forEach(btn => {
                    btn.addEventListener('click', function() {
                        const moreContent = this.nextElementSibling;
                        const isHidden = moreContent.style.display === 'none';

                        moreContent.style.display = isHidden ? 'block' : 'none';
                        this.querySelector('i').className = isHidden ?
                            'fas fa-chevron-up' : 'fas fa-chevron-down';
                        this.querySelector('span').textContent = isHidden ?
                            'Show less' : 'Show more';
                    });
                });
            })
            .catch(error => {
                console.error('Error loading cabin details:', error);
                alert('Error loading cabin details. Please try again.');
            });
    }
</script>
</body>
</html>