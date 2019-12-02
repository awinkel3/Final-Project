package com.example.finalproject;

//Super simple class that packages all the info we need on stocks
public class Stock {
    //Basically just getters for all the infos and a setter for cost

    //Cost per share
    private double cost;
    //Name of stock, i.e. NFLX, AAPL, or TWTR
    private String name;
    //Name of stock's company, i.e. Netflix, Apple, or Twitter
    private String company;
    Stock(double startCost, String startName, String startCo) {
        cost = startCost;
        name = startName;
        company = startCo;
    }

    public double getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    //Theoretically the cost will be the only thing changing in a specific stock
    public void setCost(double newCost) {
        cost = newCost;
    }
}
