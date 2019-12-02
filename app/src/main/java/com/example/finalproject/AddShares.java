package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
        TextView totalCost = findViewById(R.id.totalCost);
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
            public void beforeTextChanged(CharSequence charSequence, int before, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                lengthZero = charSequence.length() == 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int newAmount;
                if (lengthZero) {
                    newAmount = 0;
                } else {
                    newAmount = Integer.parseInt(editable.toString());
                }
                totalCost.setText("" + (newAmount * shareCost));
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
