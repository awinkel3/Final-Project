package com.example.finalproject;

import android.os.AsyncTask;

import java.io.IOException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class GetStock extends AsyncTask<String, String, Stock> {

    private MainActivity main;

    GetStock(MainActivity startMain) {
        main = startMain;
    }
    @Override
    protected void onPreExecute() {
        //Setup precondition to execute some task
    }


    protected Stock doInBackground(String... tickers) {
        System.out.println("Boutta try searching");
        //Do some task
        //tickers is really just one input--it only lets you implement the function with an array

        //Sends name to publish progress
        //return means nothing
        try {
            System.out.println("In Try block");
            return YahooFinance.get(tickers[0]);

        } catch (IOException e) {
            System.out.println("IOE problems");
            return null;
        } catch(SecurityException y) {
            System.out.println("security problems");
            y.printStackTrace();
            return null;
        } finally {
            System.out.println("Out of try block");
        }
    }

    protected void onProgressUpdate(String... results) {
        //Update the progress of current task

    }

    protected void onPostExecute(Stock result) {
        //Show the result obtained from doInBackground
        main.setCurrentStock(result);
        main.setBuyStockText();
    }
}
