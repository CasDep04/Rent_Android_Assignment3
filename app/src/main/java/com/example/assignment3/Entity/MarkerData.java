package com.example.assignment3.Entity;
import java.io.Serializable;
import java.util.List;

public class MarkerData implements Serializable {
    private String name;
    private String details;
    private String description;
    private String imageUrl;
    private String propertyType;
    private int pricePerNight;
    private List<String> facilities;

    private int hostId;

    private String locationId;

    private int guestId;

    // Constructor
    public MarkerData(String name, String details, String description, String imageUrl,
                      String propertyType, int pricePerNight, List<String> facilities, int hostId, String locationId, int guestId) {
        this.name = name;
        this.details = details;
        this.description = description;
        this.imageUrl = imageUrl;
        this.propertyType = propertyType;
        this.pricePerNight = pricePerNight;
        this.facilities = facilities;
        this.hostId = hostId;
        this.locationId = locationId;
        this.guestId = guestId;
    }

    // Getters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(int pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }
}

