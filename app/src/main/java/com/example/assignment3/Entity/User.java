package com.example.assignment3.Entity;

import java.util.Date;

public class User {
    private int id;
    private String email;
    private String role;
    private double balance;
    private String name;
    private Date dateOfBirth;

    // Constructor
    public User(int id, String email, String role, double balance, String name, Date dateOfBirth) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    // Getter and Setter for id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password


    // Getter and Setter for role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for balance
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for dateOfBirth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
