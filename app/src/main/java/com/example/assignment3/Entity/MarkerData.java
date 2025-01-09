package com.example.assignment3.Entity;

public class MarkerData {
    private String name;
    private String details;
    private String imageUrl;
    private int pricePerNight;

    // Constructor
    public MarkerData(String name, String details, String imageUrl, int pricePerNight) {
        this.name = name;
        this.details = details;
        this.imageUrl = imageUrl;
        this.pricePerNight = pricePerNight;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }
}
