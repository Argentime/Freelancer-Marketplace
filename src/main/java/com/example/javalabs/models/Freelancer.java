package com.example.javalabs.models;

public class Freelancer {
    private String name;
    private String category;
    private double rating;
    private int completedOrders;

    public Freelancer(String name, String category, double rating, int completedOrders) {
        this.name = name;
        this.category = category;
        this.rating = rating;
        this.completedOrders = completedOrders;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getRating() {
        return rating;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }
}
