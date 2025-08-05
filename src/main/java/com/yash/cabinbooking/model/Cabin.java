package com.yash.cabinbooking.model;

import java.time.LocalDateTime;

public class Cabin {
    private int id;
    private String name;
    private String description;
    private String location;
    private double hourlyRate;
    private int capacity;
    private String amenities;
    private String imageUrl;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public Cabin() {}

    public Cabin(int id, String name, String description, String location, double hourlyRate,
                 int capacity, String amenities, String imageUrl, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.hourlyRate = hourlyRate;
        this.capacity = capacity;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
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

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
        this.updatedAt = LocalDateTime.now();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
}
