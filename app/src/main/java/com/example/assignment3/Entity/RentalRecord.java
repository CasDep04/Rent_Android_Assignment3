package com.example.assignment3.Entity;

import android.util.Base64DataException;

public class RentalRecord {
    private String id;
    private int guestId;
    private int hostId;
    private String locationId;
    private String name;
    private String startDate;
    private String endDate;
    private double totalPrice;
    private String status;  // (pending, accepted, rejected)

    public RentalRecord() {
    }

    // Constructor with the new status parameter
    public RentalRecord(String id, int guestId, int hostId, String locationId, String name, String startDate, String endDate, double price, String status) {
        this.id = id;
        this.guestId = guestId;
        this.hostId = hostId;
        this.locationId = locationId;
        this.name = name;
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

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
