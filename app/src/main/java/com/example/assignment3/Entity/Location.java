package com.example.assignment3.Entity;

public class Location {
    private int id;
    private String address;
    private String city;
    private String country;
    private String description;
    private double prices;
    private int hostId;

    public Location(){}
    public Location(int id, String address, String city, String country, String description, double prices, int hostId) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.country = country;
        this.description = description;
        this.prices = prices;
        this.hostId = hostId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrices() {
        return prices;
    }

    public void setPrices(double prices) {
        this.prices = prices;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }
}
