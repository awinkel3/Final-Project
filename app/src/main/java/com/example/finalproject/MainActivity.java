package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public MainActivity() throws IOException {
    }

    //List of stocks in the market
    //private List<Stock> market = new ArrayList<>();

    MainActivity main = this;

    Stock currentStock;
    /**
     * Map of stocks in the portfolio.
     * The key is the stock.
     * The value is the number of that stock the user owns.
     */
    private Map<String, Integer> portfolio = new HashMap<>();

    //This is the currently selected stock
    private Stock selectedStock = null;

    //Request codes for starting the addShare activity seeking a result
    private int addShareAction = 0;
    private int removeShareAction = 1;

    //Whatever stock was last searched for
    private String searchedStock;

    //CODES AND GETTERS FOR USE IN GETSTOCK
    private int sellCode = 0;
        int getSellCode() {return sellCode;}


    private int buyCode = 1;
        int getBuyCode() {return buyCode;}
    //////////////

    private boolean hasInternetPermission;

    private double money = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //put Netflix inside the portfolio act as example for UI
        //portfolio.put(new Stock(314.87, "NFLX", "Netflix"), 11);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission isn't already granted, start a request
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, 0);
            // The result will be delivered to the onRequestPermissionsResult function
        }

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


    }

    //Method for updating the user's portfolio
    private void updatePortfolio() {
        LinearLayout portStocks = findViewById(R.id.portStocks);
        TextView moneyView = findViewById(R.id.money);
        portStocks.removeAllViews();
        for (Map.Entry<String, Integer> entry : portfolio.entrySet()) {
            //This is the portfolio version of GetStock, (uses sellCode and includes the number bought in constructor)
            //This function is responsible for inflating the portfolio layout at this point
            new GetStock(this, sellCode, entry.getValue()).execute(entry.getKey());
            //Set three text elements to appropriate stock-related thingies
        }

        moneyView.setText("$" + money);
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

                //Performs a background method that will eventually change the current stock
                //And reveal the buy chunk thing using MainActivity's setCurrentStock()
                //and setBuyStockText() functions


                //A:ALSKDJALSKDJLASKJDLAKSJDLAKSJDLAKSJDLAKSJDLKAJDLKAJSLDKJ
                //        IMPLEMENT BUY/Sell codes in GetStock
                //        Make it so portfolio iterates through map of strings and gets each one's prive
                //        individually'
                new GetStock(main, buyCode).execute(searchedStock);

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
                //REFERENCES currentStock explicitly -- change that
                cost.setText(negative + (newAmount * APIFuncs.getCost(currentStock)));
            }
        };

        stockNumber.addTextChangedListener(costChange);

        TextView.OnEditorActionListener endTransaction = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                //This function "buyAction" handles all of the game-type logic
                //Then returns based on whether you have enough money to perform the action
                //If you do then after search dissapears
                //Otherwise it just sticks around awkwardly and waits for you to enter a valid number
                if (buyAction(Integer.parseInt(textView.getText().toString()))) {
                    afterSearch.setVisibility(View.GONE);
                }
                return true;
            }
        };

        stockNumber.setOnEditorActionListener(endTransaction);
    }


    public void setBuyStockText() {
        TextView currentValue = findViewById(R.id.marketStockCost);
        EditText stockNumber = findViewById(R.id.stockNum);
        TextView stockName = findViewById(R.id.stockName);
        TextView stockCompany = findViewById(R.id.stockCo);

        LinearLayout afterSearch = findViewById(R.id.afterSearch);

        if (currentStock == null || currentStock.getQuote().getPrice() == null) {
            System.out.println("Stock is null");
            afterSearch.setVisibility(View.GONE);
            return;
        }

        afterSearch.setVisibility(View.VISIBLE);
        //Current value needs to be set to the stock's current value
        //try {
        currentValue.setText("" + APIFuncs.getCost(currentStock));
        //} catch (IOException e) {
        //e.printStackTrace();
        //}


        //Name set to stock's ticker
        stockName.setText(APIFuncs.getSymbol(currentStock));
        //Company set to stock's company
        stockCompany.setText((APIFuncs.getName(currentStock)));
        //Set cost to nothing, so if you bought 111 stocks last time 111 isn't still in the search bar
        stockNumber.setText("");

    }

    //Used by GetStock class to update current stock
    void setCurrentStock(Stock stock) {
        currentStock = stock;
    }


    //The effect to the game's internal logic after a sell action is used
    private boolean buyAction(int numberPurchased) {
        double cost = APIFuncs.getCost(currentStock) * numberPurchased;
        //Only perform if you can afford it
        if (money - cost >= 0) {
            money = money - cost;
            //If the stock is already in the portfoliod


            if (portfolio.containsKey(currentStock.getSymbol())) {
                //Add number purchased to the number you already have
                portfolio.put(currentStock.getSymbol(), portfolio.get(currentStock.getSymbol()) + numberPurchased);
            } else {
                //Otherwise just put the stock in with the number purchased
                portfolio.put(currentStock.getSymbol(), numberPurchased);
            }

            updatePortfolio();
            return true;
        }

        return false;
    }

    private boolean sellAction(Stock toBeSold, int numberSold) {
        String symbol = toBeSold.getSymbol();
        if (portfolio.containsKey(symbol) && numberSold <= portfolio.get(symbol)) {
            double gain = APIFuncs.getCost(toBeSold) * numberSold;
            money += gain;

            if (portfolio.get(symbol) - numberSold == 0) {
                portfolio.remove(symbol);
            } else {
                portfolio.put(symbol, portfolio.get(symbol) - numberSold);
            }

            updatePortfolio();
            return true;
        }

        return false;
    }

    //Function responsible for inflating portStocks, used by GetStock
    void inflatePortStocks(Stock currentPortStock, int number) {
        LinearLayout portStocks = findViewById(R.id.portStocks);
        View stockChunk = getLayoutInflater().inflate(R.layout.chunk_portfolio_stock, portStocks, false);
        TextView stockName = stockChunk.findViewById(R.id.stockName);
        TextView stockCo = stockChunk.findViewById(R.id.stockCo);
        TextView stockTotal = stockChunk.findViewById(R.id.currentValue);
        TextView stockNum = stockChunk.findViewById(R.id.number);
        TextView stockPPS = stockChunk.findViewById(R.id.pricePerShare);
        Button stockSell = stockChunk.findViewById(R.id.sellButton);

        EditText sellNum = stockChunk.findViewById(R.id.sellNum);

        sellNum.setVisibility(View.GONE);
        //Makes it so clicking the buy button triggers the addShares method
        stockSell.setOnClickListener(unused -> sellButtonPressed(currentPortStock, sellNum));


        stockName.setText(APIFuncs.getSymbol(currentPortStock));
        stockCo.setText(APIFuncs.getName(currentPortStock));
        //*It won't let me cast a double to a string so this is a work around
        final double currentUpdatedStockPrice = APIFuncs.getCost(currentPortStock);
        stockTotal.setText(String.format("%.2f", currentUpdatedStockPrice * number));
        stockPPS.setText("" + currentUpdatedStockPrice);

        //Sets the number paramter to the number of the stock the user owns (the value of the list)
        stockNum.setText("" + number);

        portStocks.addView(stockChunk);

        //Tells the program what to do when the enter key is pressed on the keyboard
        TextView.OnEditorActionListener sellUsed = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                //////////////////////////////////////////////////////////////////////////////////
                //Add some kind of function to deal with the sell action internally
                if (sellNum.getText().toString().equals("") || sellNum.getText().toString() == null) {
                    return false;
                }
                if (sellAction(currentPortStock, Integer.parseInt(sellNum.getText().toString()))) {
                    sellNum.setVisibility(View.GONE);
                }
                return true;
            }
        };

        sellNum.setOnEditorActionListener(sellUsed);
    }

}
