package com.example.mcda5550.bmiapp;

public class User {

    public int id;
    public String name;
    public String healthCardNumber;
    public String password;
    public long dateOfBirth;

    public int getId() {
        return id;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(int id, String name, String healthCardNumber, String password, int dateOfBirth) {
        this.id = id;
        this.name = name;
        this.healthCardNumber = healthCardNumber;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {

    }
}
