package com.example.assignment3.Entity;

import java.util.Date;

public class Guest extends User {
    private String preferences;  // Preferences for rental properties

    public Guest(){}
    public Guest(int id, String email, double balance, String name, String dateOfBirth, String preferences) {
        super(id, email, "guest", balance, name, dateOfBirth);
        this.preferences = preferences;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}
