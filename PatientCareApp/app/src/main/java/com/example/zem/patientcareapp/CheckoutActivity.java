package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by User PC on 9/21/2015.
 */

public class CheckoutActivity extends Activity implements View.OnClickListener {
    ActionBar actionBar;
    EditText customerName, customerAddress, customerContactNumber, customerEmail, recipientName, recipientAddress, recipientContactNumber;
    Spinner listOfECEBranches;
    Button proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutform);

        actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);

        customerName = (EditText) findViewById(R.id.customerName);
        customerAddress = (EditText) findViewById(R.id.customerAddress);
        customerContactNumber = (EditText) findViewById(R.id.customerContactNumber);
        customerEmail = (EditText) findViewById(R.id.customerEmail);
        recipientName = (EditText) findViewById(R.id.recipientName);
        recipientAddress = (EditText) findViewById(R.id.recipientAddress);
        recipientContactNumber = (EditText) findViewById(R.id.recipientContactNumber);
        listOfECEBranches = (Spinner) findViewById(R.id.listOfECEBranches);
        proceed = (Button) findViewById(R.id.proceed);

        proceed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
