package com.example.assignment3.Entity;

public class Review {
    private int id;
    private int locationId;
    private int rating;
    private String comments;

    // Constructor
    public Review(){}
    public Review(int id, int locationId, int rating, String comments) {
        this.id = id;
        this.locationId = locationId;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters and Setters
    public int getId() {
        return id;  // ID
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

