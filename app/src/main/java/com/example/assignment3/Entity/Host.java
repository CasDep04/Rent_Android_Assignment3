package com.example.assignment3.Entity;

import java.util.Date;

public class Host extends User {
    private int totalProperties; // Number of properties listed

    public Host(int id, String email, String role, double balance, String name, Date dateOfBirth, int totalProperties) {
        super(id, email, role, balance, name, dateOfBirth);
        this.totalProperties = totalProperties;
    }

    public int getTotalProperties() {
        return totalProperties;
    }

    public void setTotalProperties(int totalProperties) {
        this.totalProperties = totalProperties;
    }
}
