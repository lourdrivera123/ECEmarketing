package com.example.zem.patientcareapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Zem on 4/28/2015.
 */
public class ContactActivity extends ActionBarActivity implements View.OnClickListener {

    Button next_to_account_btn;
    Spinner address_region;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_contact_info);

        next_to_account_btn = (Button) findViewById(R.id.next_to_account_btn);
        next_to_account_btn.setOnClickListener(this);

        address_region = (Spinner) findViewById(R.id.address_region);
        ArrayAdapter<String> regions_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.regions));
        address_region.setAdapter(regions_adapter);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, AccountActivity.class));
    }
}
