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

    //Request codes for starting the addShare activity seeking a result
    private int addShareAction = 0;
    private int removeShareAction = 1;

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
            if (checkedId == R.id.marketButton) {
                marketView.setVisibility(android.view.View.VISIBLE);
                portView.setVisibility(android.view.View.GONE);

            } else if (checkedId == R.id.portButton) {
                portView.setVisibility(android.view.View.VISIBLE);
                marketView.setVisibility(android.view.View.GONE);

            }
        });




        //Populate marketStocks layout (contained within marketView) with the list of market stocks
        LinearLayout marketStocks = findViewById(R.id.marketStocks);
        for (int i = 0; i < market.size(); i++) {

            //We're dealing with the stock at index(i) in market
            Stock currentStock = market.get(i);

            //Get the chunk and its three text elements to be set
            View stockChunk = getLayoutInflater().inflate(R.layout.chunk_stock, marketStocks, false);
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

            marketStocks.addView(stockChunk);
        }

        updatePortfolio();
    }

    /**
     * Invoked by the Android system when a request launched by startActivityForResult completes.
     * @param requestCode the request code passed by to startActivityForResult
     * @param resultCode a value indicating how the request finished (e.g. completed or canceled)
     * @param data an Intent containing results (e.g. as a URI or in extras)
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == addShareAction) {
            // Do something that depends on the result of that request
        }

        if (requestCode == removeShareAction) {

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

        startActivityForResult(intent,addShareAction);

        //I don't think we finish so then the player can go back?
    }


    //Launches the addShares activity (but with the remove type so stocks are removed from the portfolio)
    private void removeShares (Stock stock) {
        Intent intent = new Intent(this, AddShares.class);

        //I was an idiot and called the class AddShares, but it should be able to add or subtract shares
        //The type should tell the program which one to do
        intent.putExtra("type", "remove");

        //Sends the info about the stock that will be used to set up the addShares screen
        intent.putExtra("name", stock.getName());
        intent.putExtra("company", stock.getCompany());
        intent.putExtra("cost", stock.getCost());

        startActivityForResult(intent,removeShareAction);
    }

    //Method for updating the user's portfolio
    private void updatePortfolio() {
        LinearLayout portStocks = findViewById(R.id.portStocks);
        for (Map.Entry<Stock, Integer> entry : portfolio.entrySet()) {
            // The type names in the angle brackets should match the types in the map
            // The current key is entry.getKey()
            // The current value is entry.getValue()
            // Do something with the key and value?
            //We're dealing with the stock at index(i) in market
            Stock currentStock = entry.getKey();

            //Get the chunk and its three text elements to be set
            View stockChunk = getLayoutInflater().inflate(R.layout.chunk_portfolio_stock, portStocks, false);
            TextView stockName = stockChunk.findViewById(R.id.stockName);
            TextView stockCo = stockChunk.findViewById(R.id.stockCo);
            TextView stockCost = stockChunk.findViewById(R.id.cost);
            TextView stockNum = stockChunk.findViewById(R.id.number);
            Button stockSell = stockChunk.findViewById(R.id.stockSell);

            //Set three text elements to appropriate stock-related thingies
            stockName.setText(currentStock.getName());
            stockCo.setText(currentStock.getCompany());
            //*It won't let me cast a double to a string so this is a work around
            stockCost.setText("" + currentStock.getCost());

            //Sets the number paramter to the number of the stock the user owns (the value of the list)
            stockNum.setText("" + entry.getValue());

            //Makes it so clicking the buy button triggers the addShares method
            stockSell.setOnClickListener(unused -> removeShares(currentStock));

            portStocks.addView(stockChunk);
        }

    }
}
