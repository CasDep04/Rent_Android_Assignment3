package com.example.assignment3.Entity;

import java.util.Date;

public class Guest extends User {
    private String preferences;  // Preferences for rental properties

    public Guest(int id, String email, String role, double balance, String name, Date dateOfBirth, String preferences) {
        super(id, email, role, balance, name, dateOfBirth);
        this.preferences = preferences;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
