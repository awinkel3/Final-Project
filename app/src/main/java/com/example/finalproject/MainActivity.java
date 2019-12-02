package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //List of stocks in the market
    private List<Stock> market = new ArrayList<>();

    /**
     * Map of stocks in the portfolio.
     * The key is the stock.
     * The value is the number of that stock the user owns.
     */
    private Map<Stock, Integer> portfolio = new HashMap<>();

    //This is the currently selected stock
    private Stock selectedStock = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //put Netflix inside the portfolio act as example for UI
        portfolio.put(new Stock(314.87, "NFLX", "Netflix"), 11);

        //Put Twitter and Tesla in the market to act as an example for UI
        market.add(new Stock(30.9, "TWTR", "Twitter"));
        market.add(new Stock(329.83, "TSLA", "Tesla"));




        //Allow for the switching between portfolio view and market view
        RadioGroup modeGroup;
        modeGroup = findViewById(R.id.viewSelect);

        final LinearLayout marketView = findViewById(R.id.market);
        final LinearLayout portView = findViewById(R.id.portfolio);

        modeGroup.setOnCheckedChangeListener((unused, checkedId) -> {
            // checkedId is the R.id constant of the currently checked RadioButton
            // Your code here: make only the selected mode's settings group visible
            if (checkedId == R.id.marketView) {
                marketView.setVisibility(android.view.View.VISIBLE);
                portView.setVisibility(android.view.View.GONE);

            } else if (checkedId == R.id.portView) {
                portView.setVisibility(android.view.View.VISIBLE);
                marketView.setVisibility(android.view.View.GONE);

            }
        });




        //Populate marketView layout with the list of market stocks
        for (int i = 0; i < market.size(); i++) {

            //We're dealing with the stock at index(i) in market
            Stock currentStock = market.get(i);

            //Get the chunk and its three text elements to be set
            View stockChunk = getLayoutInflater().inflate(R.layout.chunk_stock, marketView, false);
            TextView stockName = stockChunk.findViewById(R.id.stockName);
            TextView stockCo = stockChunk.findViewById(R.id.stockCo);
            TextView stockCost = stockChunk.findViewById(R.id.cost);
            Button stockBuy = stockChunk.findViewById(R.id.stockBuy);

            //Set three text elements to appropriate stock-related thingies
            stockName.setText(currentStock.getName());
            stockCo.setText(currentStock.getCompany());
            //*It won't let me cast a double to a string so this is a work around
            stockCost.setText("" + currentStock.getCost());

            //Makes it so clicking the buy button triggers the addShares method
            stockBuy.setOnClickListener(unused -> addShares(currentStock));

            marketView.addView(stockChunk);
        }
    }

    //Launches the addShares activity
    private void addShares (Stock stock) {
        Intent intent = new Intent(this, AddShares.class);

        //I was an idiot and called the class AddShares, but it should be able to add or subtract shares
        //The type should tell the program which one to do
        intent.putExtra("type", "add");

        //Sends the info about the stock that will be used to set up the addShares screen
        intent.putExtra("name", stock.getName());
        intent.putExtra("company", stock.getCompany());
        intent.putExtra("cost", stock.getCost());

        startActivity(intent);

        //I don't think we finish so then the player can go back?
    }
}
