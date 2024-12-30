package com.example.assignment3.Entity;

import java.util.Date;

public class Host extends User {
    private int totalProperties; // Number of properties listed

    public Host(){}
    public Host(int id, String email, double balance, String name, String dateOfBirth, int totalProperties) {
        super(id, email, "host", balance, name, dateOfBirth);
        this.totalProperties = totalProperties;
    }

    public int getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(int totalProperties) {
        this.totalProperties = totalProperties;
    }
}
