package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //List of stocks in the market
    //private List<Stock> market = new ArrayList<>();

    Stock allTheStocks = new Stock(77, "COOL", "CoolCorps");
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

    //Whatever stock was last searched for
    private String searchedStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //put Netflix inside the portfolio act as example for UI
        portfolio.put(new Stock(314.87, "NFLX", "Netflix"), 11);




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

        updateMarket();

        updatePortfolio();
    }


    //Makes the sell view available, which in turn will become invisible when you hit enter on the key
    private void sellButtonPressed (Stock stock, EditText sellNum) {
        sellNum.setVisibility(View.VISIBLE);

        //Set search to nothing, so it doesn't stay the same between sell actions
        sellNum.setText("");

        //Tells the program what to do when the enter key is pressed on the keyboard
        TextView.OnEditorActionListener sellUsed = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //////////////////////////////////////////////////////////////////////////////////
                //Add some kind of function to deal with the sell action internally

                sellNum.setVisibility(View.GONE);
                return true;
            }
        };

        sellNum.setOnEditorActionListener(sellUsed);
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
            TextView stockCost = stockChunk.findViewById(R.id.pricePerShare);
            TextView stockNum = stockChunk.findViewById(R.id.number);
            Button stockSell = stockChunk.findViewById(R.id.sellButton);

            EditText sellNum = stockChunk.findViewById(R.id.sellNum);

            sellNum.setVisibility(View.GONE);

            //Set three text elements to appropriate stock-related thingies
            stockName.setText(currentStock.getName());
            stockCo.setText(currentStock.getCompany());
            //*It won't let me cast a double to a string so this is a work around
            stockCost.setText("" + currentStock.getCost());

            //Sets the number paramter to the number of the stock the user owns (the value of the list)
            stockNum.setText("" + entry.getValue());

            //Makes it so clicking the buy button triggers the addShares method
            stockSell.setOnClickListener(unused -> sellButtonPressed(currentStock, sellNum));

            portStocks.addView(stockChunk);
        }

    }

    private void updateMarket() {
        EditText search = findViewById(R.id.search);
        TextView cost = findViewById(R.id.currentValue);
        TextView currentValue = findViewById(R.id.marketStockCost);
        EditText stockNumber = findViewById(R.id.stockNum);
        TextView stockName = findViewById(R.id.stockName);
        TextView stockCompany = findViewById(R.id.stockCo);

        LinearLayout afterSearch = findViewById(R.id.afterSearch);

        cost.setText("0");

        afterSearch.setVisibility(View.GONE);


        //Tells the program what to do when the enter key is pressed on the keyboard
        TextView.OnEditorActionListener searchUsed = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                searchedStock = textView.getText().toString();
                //If the searched stock is in the API, then:
                if (textView.getText().toString().equals(allTheStocks.getName())) {
                    //Make the afterSearch view visible
                    //Set the afterSearch parameters to the stock's parameters
                    afterSearch.setVisibility(View.VISIBLE);
                    //Current value needs to be set to the stock's current value
                    currentValue.setText("" + allTheStocks.getCost());


                    ////////////////////////////////////////////////////////////////////////////////////////////////
                    //REFERENCES allTheStocks explicitly -- fix that

                    //Name set to stock's ticker
                    stockName.setText(allTheStocks.getName());
                    //Company set to stock's company
                    stockCompany.setText((allTheStocks.getCompany()));
                    //Set cost to nothing, so if you bought 111 stocks last time 111 isn't still in the search bar
                    stockNumber.setText("");
                } else {
                    //If the stock is not part of the API, make afterSearch dissappear
                    afterSearch.setVisibility(View.GONE);
                }
                return true;
            }
        };

        //When you hit enter on the search, the above action ensues
        search.setOnEditorActionListener(searchUsed);

        //Tells program what to do when text changes in shareNum
        TextWatcher costChange = new TextWatcher() {
            boolean lengthZero;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int before, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                lengthZero = charSequence.length() == 0;
            }


            @Override
            public void afterTextChanged(Editable editable) {
                int newAmount;
                String negative = "";
                if (lengthZero) {
                    newAmount = 0;
                } else {
                    newAmount = Integer.parseInt(editable.toString());
                }

                //Makes it so there's a negative sign if you are BUYING stocks and your cost is not zero
                if (newAmount > 0) {
                    negative = "-";
                }

                ///////////////////////////////////////////////////////////////////////////////////////////////////
                //REFERENCES allTheStocks explicitly -- change that
                cost.setText(negative + (newAmount * allTheStocks.getCost()));
            }
        };

        stockNumber.addTextChangedListener(costChange);

        TextView.OnEditorActionListener endTransaction = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                //ADD A HELPER FUNCTION HERE TO ENACT CHANGES TO LOCAL VARIABLES AFTER TRANSACTION


                afterSearch.setVisibility(View.GONE);
                return true;
            }
        };

        stockNumber.setOnEditorActionListener(endTransaction);
    }


    private void onBuy() {
        ;
    }
}
