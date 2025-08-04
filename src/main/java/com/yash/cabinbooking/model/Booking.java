package com.yash.cabinbooking.model;



import java.util.Date;

public class Booking {
    private int id;
    private int userId;
    private String cabinName;
    private Date startDate;
    private Date endDate;
    private int guests;
    private double amount;
    private String status;

    // Getters and Setters
    // Example:
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCabinName() { return cabinName; }
    public void setCabinName(String cabinName) { this.cabinName = cabinName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
