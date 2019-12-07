package com.example.finalproject;
import android.os.AsyncTask;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class APIFuncs {
    private static double initialCost;
    private static double currentValue;
    public static Stock getStock(String ticker) throws IOException {

        try {
            return YahooFinance.get(ticker);

        } catch (IOException e) {
            return null;
        }
    }
    public static double getCost(Stock stock) {


        initialCost = stock.getQuote().getPrice().doubleValue();
        return initialCost;
    }

    public static double getCurrentValue(Stock stock) throws IOException {
        currentValue = stock.getQuote(true).getPrice().doubleValue();
        return currentValue;
    }

    public static String getSymbol(Stock stock) {
        return stock.getSymbol();
    }

    public static String getName(Stock stock) {


        return stock.getName();
    }
    /// stock.getSymbol() returns the ticker (ie 4 character thing )
    /// stock.getName() returns the company's full name
}
