package com.example.assignment3.Entity;

public class RentalRecord {
    private String id;
    private String guestId;
    private String hostId;
    private String locationId;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private String status;  // (pending, accepted, rejected)

    public RentalRecord() {
    }

    // Constructor with the new status parameter
    public RentalRecord(String id, String guestId, String hostId, String locationId, String startDate, String endDate, double price, String status) {
        this.id = id;
        this.guestId = guestId;
        this.hostId = hostId;
        this.locationId = locationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = price;
        this.status = status;  // Initialize status
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGuestId() {  // Corrected to return String
        return guestId;
    }

    public void setGuestId(String guestId) {  // Corrected to accept String
        this.guestId = guestId;
    }

    public String getHostId() {  // Corrected to return String
        return hostId;
    }

    public void setHostId(String hostId) {  // Corrected to accept String
        this.hostId = hostId;
    }

    public String getLocationId() {  // Corrected to return String
        return locationId;
    }

    public void setLocationId(String locationId) {  // Corrected to accept String
        this.locationId = locationId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;  // Getter for status
    }

    public void setStatus(String status) {
        this.status = status;  // Setter for status
    }
}
