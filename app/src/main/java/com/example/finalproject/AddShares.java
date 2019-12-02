package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddShares extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shares);

        Intent intent = getIntent();

        //Gets activity's textViews and buttons
        TextView shareName = findViewById(R.id.shareName);
        TextView totalCost = findViewById(R.id.totalCost);
        TextView shareCo = findViewById(R.id.shareCo);
        EditText shareNum = findViewById(R.id.shareNum);
        Button complete = findViewById(R.id.completeButton);

        //Set shareName to stock's name
        shareName.setText(intent.getStringExtra("name"));
        shareCo.setText(intent.getStringExtra("company"));
        totalCost.setText("" + intent.getDoubleExtra("cost", 0.0));

    }

}
