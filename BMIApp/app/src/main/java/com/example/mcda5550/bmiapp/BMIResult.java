package com.example.mcda5550.bmiapp;

public class BMIResult {
    private double height = 1;
    private double weight = 1;
    private double bmi = 1;
    private long date = 1;

    public BMIResult(double height, double weight, double bmi) {
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public long getDate() {
        return date;
    }

    public double getBmi() {
        return bmi;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getResult() {
        return weight / (height * height);
    }

    public String toString() {
        return String.valueOf(getResult());
    }

    public BMIResult() {

    }

    public void setBMI(double bmi) {
        this.bmi = bmi;
    }
}
