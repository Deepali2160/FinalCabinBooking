package com.yash.cabinbooking.model;

import java.time.LocalDateTime;

public class Booking {
    // Core booking fields
    private int id;
    private int userId;
    private int cabinId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int guests;
    private double amount;
    private String status;         // e.g., 'pending', 'confirmed', 'cancelled', 'completed'
    private String paymentStatus;  // e.g., 'unpaid', 'paid', 'refunded'
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Approval-related fields
    private String approvalStatus;      // e.g., 'pending_approval', 'approved', 'rejected'
    private Integer approvedBy;         // admin user id who approved/rejected (nullable)
    private LocalDateTime approvedAt;   // timestamp of approval/rejection
    private String adminRemarks;        // admin's notes or remarks
    private String rejectionReason;     // reason for rejection if any

    // Display-only fields (populate from joined queries)
    private String userName;
    private String userEmail;
    private String cabinName;
    private String cabinLocation;
    private String approvedByName;

    // Getters and Setters

    // id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // userId
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    // cabinId
    public int getCabinId() {
        return cabinId;
    }
    public void setCabinId(int cabinId) {
        this.cabinId = cabinId;
    }

    // startDate
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    // endDate
    public LocalDateTime getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    // guests
    public int getGuests() {
        return guests;
    }
    public void setGuests(int guests) {
        this.guests = guests;
    }

    // amount
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // status
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // paymentStatus
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // approvalStatus
    public String getApprovalStatus() {
        return approvalStatus;
    }
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    // approvedBy
    public Integer getApprovedBy() {
        return approvedBy;
    }
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    // approvedAt
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    // adminRemarks
    public String getAdminRemarks() {
        return adminRemarks;
    }
    public void setAdminRemarks(String adminRemarks) {
        this.adminRemarks = adminRemarks;
    }

    // rejectionReason
    public String getRejectionReason() {
        return rejectionReason;
    }
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    // userName
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    // userEmail
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    // cabinName
    public String getCabinName() {
        return cabinName;
    }
    public void setCabinName(String cabinName) {
        this.cabinName = cabinName;
    }

    // cabinLocation
    public String getCabinLocation() {
        return cabinLocation;
    }
    public void setCabinLocation(String cabinLocation) {
        this.cabinLocation = cabinLocation;
    }

    // approvedByName
    public String getApprovedByName() {
        return approvedByName;
    }
    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }
}
