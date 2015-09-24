package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zem.patientcareapp.GetterSetter.Patient;

import java.util.ArrayList;

/**
 * Created by User PC on 9/21/2015.
 */

public class CheckoutActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    ActionBar actionBar;
    LinearLayout hideLayout;
    TextView customerName, customerContactNumber, customerAddress;
    EditText recipientName, recipientAddress, recipientContactNumber;
    RadioButton pickup, deliver;
    Spinner listOfECEBranches;
    CheckBox check;
    Button proceed;

    String senderName, senderAddress, senderContactNumber, receiverName, receiverAddress, receiverContactNumber,
            modeOfDelivery, eceBranch;
    String ptnt_completeAddress, ptnt_fullname, ptnt_contactNumber;
    String[] arrayOfECEBranches;
    ArrayAdapter adapter;

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

        customerName = (TextView) findViewById(R.id.customerName);
        customerAddress = (TextView) findViewById(R.id.customerAddress);
        customerContactNumber = (TextView) findViewById(R.id.customerContactNumber);
        recipientName = (EditText) findViewById(R.id.recipientName);
        recipientAddress = (EditText) findViewById(R.id.recipientAddress);
        recipientContactNumber = (EditText) findViewById(R.id.recipientContactNumber);
        pickup = (RadioButton) findViewById(R.id.pickup);
        deliver = (RadioButton) findViewById(R.id.deliver);
        listOfECEBranches = (Spinner) findViewById(R.id.listOfECEBranches);
        proceed = (Button) findViewById(R.id.proceed);
        check = (CheckBox) findViewById(R.id.check);
        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);

        arrayOfECEBranches = getResources().getStringArray(R.array.ECEbranches);
        adapter = new ArrayAdapter(this, R.layout.spinner_item, arrayOfECEBranches);
        listOfECEBranches.setAdapter(adapter);

        proceed.setOnClickListener(this);
        check.setOnCheckedChangeListener(this);

        ptnt_completeAddress = patient.getAddress_street() + " " + patient.getAddress_city_municipality() + ", " + patient.getAddress_province();
        ptnt_fullname = patient.getFname() + " " + patient.getLname();
        ptnt_contactNumber = patient.getMobile_no();

        customerName.setText("Name: " + ptnt_fullname);
        customerAddress.setText("Address: " + ptnt_completeAddress);
        customerContactNumber.setText("Contact No.: " + ptnt_contactNumber);
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
        senderName = ptnt_fullname;
        senderAddress = ptnt_completeAddress;
        senderContactNumber = ptnt_contactNumber;
        eceBranch = listOfECEBranches.getSelectedItem().toString();

        if (check.isChecked()) {
            receiverName = senderName;
            receiverAddress = senderAddress;
            receiverContactNumber = senderContactNumber;
        } else {
            receiverName = recipientName.getText().toString();
            receiverAddress = recipientAddress.getText().toString();
            receiverContactNumber = recipientContactNumber.getText().toString();
        }

        if (pickup.isChecked())
            modeOfDelivery = "pick-up";
        else if (deliver.isChecked())
            modeOfDelivery = "delivery";
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (check.isChecked())
            hideLayout.setVisibility(View.GONE);
        else
            hideLayout.setVisibility(View.VISIBLE);
    }
}
