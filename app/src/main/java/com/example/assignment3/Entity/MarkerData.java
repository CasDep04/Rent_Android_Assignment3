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

    // Constructor
    public MarkerData(String name, String details, String description, String imageUrl,
                      String propertyType, int pricePerNight, List<String> facilities) {
        this.name = name;
        this.details = details;
        this.description = description;
        this.imageUrl = imageUrl;
        this.propertyType = propertyType;
        this.pricePerNight = pricePerNight;
        this.facilities = facilities;
    }

    // Getters
    public String getName() { return name; }
    public String getDetails() { return details; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getPropertyType() { return propertyType; }
    public int getPricePerNight() { return pricePerNight; }
    public List<String> getFacilities() { return facilities; }
}

