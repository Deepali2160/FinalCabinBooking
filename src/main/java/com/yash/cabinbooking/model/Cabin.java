package com.yash.cabinbooking.model;

import java.time.LocalDateTime;

public class Cabin {
    private int id;
    private String name;
    private String description;
    private String location;
    private double pricePerNight;
    private int maxGuests;
    private int bedrooms;
    private int bathrooms;
    private String amenities;
    private String imageUrl;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors, getters, and setters
    public Cabin() {}

    public Cabin(int id, String name, String description, String location, double pricePerNight,
                 int maxGuests, int bedrooms, int bathrooms, String amenities,
                 String imageUrl, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.maxGuests = maxGuests;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.updatedAt = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.updatedAt = LocalDateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
        this.updatedAt = LocalDateTime.now();
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
        this.updatedAt = LocalDateTime.now();
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
        this.updatedAt = LocalDateTime.now();
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
        this.updatedAt = LocalDateTime.now();
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
        this.updatedAt = LocalDateTime.now();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Cabin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", pricePerNight=" + pricePerNight +
                ", maxGuests=" + maxGuests +
                ", bedrooms=" + bedrooms +
                ", bathrooms=" + bathrooms +
                ", amenities='" + amenities + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

