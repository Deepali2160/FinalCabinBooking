<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.yash.cabinbooking.model.Cabin" %>

<%
    Cabin cabin = (Cabin) request.getAttribute("cabin");
    boolean isEdit = (cabin != null);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= isEdit ? "Edit Cabin" : "Add New Cabin" %> | Admin Dashboard</title>
    <link rel="stylesheet" href="../css/admin-dashboard.css">
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            padding: 30px;
            background-color: var(--background, #f4f4f4);
            color: #333;
        }

        h1 {
            text-align: center;
            margin-bottom: 30px;
        }

        form {
            max-width: 700px;
            margin: auto;
            background: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input[type="text"],
        input[type="number"],
        textarea {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        .form-check {
            margin-top: 10px;
        }

        .form-check label {
            font-weight: normal;
        }

        button {
            margin-top: 20px;
            background-color: #4361ee;
            color: #fff;
            border: none;
            padding: 12px 20px;
            border-radius: 6px;
            cursor: pointer;
        }

        button:hover {
            background-color: #3a56d4;
        }

        .error {
            color: red;
            text-align: center;
        }
    </style>
</head>
<body>

<h1><%= isEdit ? "Update Cabin" : "Add New Cabin" %></h1>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<form method="post" action="../CabinServlet">
    <input type="hidden" name="id" value="<%= isEdit ? cabin.getId() : "" %>">

    <label>Cabin Name</label>
    <input type="text" name="name" required value="<%= isEdit ? cabin.getName() : "" %>">

    <label>Description</label>
    <textarea name="description" rows="3"><%= isEdit ? cabin.getDescription() : "" %></textarea>

    <label>Location</label>
    <input type="text" name="location" required value="<%= isEdit ? cabin.getLocation() : "" %>">

    <label>Price per Night</label>
    <input type="number" step="0.01" name="pricePerNight" required value="<%= isEdit ? cabin.getPricePerNight() : "" %>">

    <label>Max Guests</label>
    <input type="number" name="maxGuests" required value="<%= isEdit ? cabin.getMaxGuests() : "" %>">

    <label>Bedrooms</label>
    <input type="number" name="bedrooms" required value="<%= isEdit ? cabin.getBedrooms() : "" %>">

    <label>Bathrooms</label>
    <input type="number" name="bathrooms" required value="<%= isEdit ? cabin.getBathrooms() : "" %>">

    <label>Amenities</label>
    <input type="text" name="amenities" value="<%= isEdit ? cabin.getAmenities() : "" %>">

    <label>Image URL</label>
    <input type="text" name="imageUrl" value="<%= isEdit ? cabin.getImageUrl() : "" %>">

    <div class="form-check">
        <input type="checkbox" name="isAvailable" id="isAvailable" <%= isEdit && cabin.isAvailable() ? "checked" : "" %>>
        <label for="isAvailable">Available</label>
    </div>

    <div class="form-check">
        <input type="checkbox" name="isFeatured" id="isFeatured" <%= isEdit && cabin.isFeatured() ? "checked" : "" %>>
        <label for="isFeatured">Featured</label>
    </div>

    <button type="submit"><%= isEdit ? "Update" : "Save" %> Cabin</button>
</form>

</body>
</html>
