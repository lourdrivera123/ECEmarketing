package com.example.zem.patientcareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Zem on 4/29/2015.
 */
public class DoctorActivity extends ActionBarActivity implements View.OnClickListener {
    ImageButton call_doctor;
    TextView cp_no;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        call_doctor = (ImageButton) findViewById(R.id.call_doctor);
        cp_no = (TextView) findViewById(R.id.cp_no);

        call_doctor.setOnClickListener(this);
        cp_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        String phoneNumber = contact.getNumber().toString();
        String phoneNumber = "09095331440";
        String uriTel = "tel:"+phoneNumber;

        //Present you with the dialler
        Uri telUri  = Uri.parse(uriTel);
        Intent returnIt = new Intent(Intent.ACTION_DIAL, telUri);
        startActivity(returnIt);
    }
}
