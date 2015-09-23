package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.zem.patientcareapp.GetterSetter.Patient;

/**
 * Created by User PC on 9/21/2015.
 */

public class CheckoutActivity extends Activity implements View.OnClickListener {
    ActionBar actionBar;
    EditText customerName, customerAddress, customerContactNumber, customerEmail, recipientName, recipientAddress, recipientContactNumber;
    Spinner listOfECEBranches;
    Button proceed;

    DbHelper db;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutform);

        actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        patient = db.getCurrentLoggedInPatient();

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

        String completeAddress = patient.getAddress_street() + " " + patient.getAddress_city_municipality() + ", " + patient.getAddress_province();

        customerName.setText(patient.getFname() + " " + patient.getLname());
        customerAddress.setText(completeAddress);
        customerContactNumber.setText(patient.getMobile_no());
        customerEmail.setText(patient.getEmail());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
