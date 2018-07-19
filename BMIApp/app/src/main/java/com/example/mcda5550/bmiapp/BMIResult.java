package com.example.mcda5550.bmiapp;

public class BMIResult {
    private double height = 1;
    private double weight = 1;
    private int date = 1;

    public BMIResult(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
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
}
