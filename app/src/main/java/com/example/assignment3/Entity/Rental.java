package com.example.assignment3.Entity;

import java.util.List;

public class Rental {
    // attributes
    private String id;
    private String name;
    private String address;
    private double pricePerNight;
    private String propertyType;
    private List<String> facilities;
    private String imageUrl;
    private double latitude;
    private double longitude;

    public Rental() {
        // Default constructor required for calls to DataSnapshot.getValue(Rental.class)
    }

    public Rental(String id, String name, String address, double pricePerNight, String propertyType, List<String> facilities, String imageUrl, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.pricePerNight = pricePerNight;
        this.propertyType = propertyType;
        this.facilities = facilities;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and setters for all fields
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

    public List<String> getFacilities() { return facilities; }
    public void setFacilities(List<String> facilities) { this.facilities = facilities; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}