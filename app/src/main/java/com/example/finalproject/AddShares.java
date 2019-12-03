package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class AddShares extends AppCompatActivity {

    //The total cost will be displayed and change as the input of number changes
    //private TextView totalCost;

    //The cost of each share, to be multiplied by amount when setting totalCost's text
    private double shareCost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shares);

        Intent intent = getIntent();

        //Gets activity's textViews and buttons
        TextView shareName = findViewById(R.id.shareName);
        TextView totalCost = findViewById(R.id.totalCost0);
        TextView shareCompany = findViewById(R.id.shareCo);
        EditText shareNum = findViewById(R.id.shareNum);

        shareCost = intent.getDoubleExtra("cost", 0);

        //Set shareName to stock's name
        shareName.setText(intent.getStringExtra("name"));
        shareCompany.setText(intent.getStringExtra("company"));
        totalCost.setText("0");



        //Tells program what to do when text changes in shareNum
        TextWatcher costChange = new TextWatcher() {
            boolean lengthZero;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int before, int count, int after) {}

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
                if (intent.getStringExtra("type").equals("add") && newAmount > 0) {
                    negative = "-";
                }
                totalCost.setText(negative + (newAmount * shareCost));
            }
        };

        shareNum.addTextChangedListener(costChange);

        //Tells the program what to do when the enter key is pressed on the keyboard
        TextView.OnEditorActionListener endActivity = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                finish();
                return true;
            }
        };

        shareNum.setOnEditorActionListener(endActivity);

    }


}
