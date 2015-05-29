package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DoctorActivity extends Activity implements View.OnClickListener {
    ImageButton call_doctor;
    TextView doctor_name, specialty, sub_specialty, address_first_line, address_second_line, email, cp_no, clinic_name, clinic_address_first_line, clinic_address_second_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);

        call_doctor = (ImageButton) findViewById(R.id.call_doctor);
        doctor_name = (TextView) findViewById(R.id.doctor_name);
        specialty = (TextView) findViewById(R.id.specialty);
        sub_specialty = (TextView) findViewById(R.id.sub_specialty);
        address_first_line = (TextView) findViewById(R.id.address_first_line);
        address_second_line = (TextView) findViewById(R.id.address_second_line);
        email = (TextView) findViewById(R.id.email);
        cp_no = (TextView) findViewById(R.id.cp_no);
        clinic_name = (TextView) findViewById(R.id.clinic_name);
        clinic_address_first_line = (TextView) findViewById(R.id.clinic_address_first_line);
        clinic_address_second_line = (TextView) findViewById(R.id.clinic_address_second_line);

        call_doctor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNumber = "09095331440";
        String uriTel = "tel:" + phoneNumber;

        //Present you with the dialler
        Uri telUri = Uri.parse(uriTel);
        Intent returnIt = new Intent(Intent.ACTION_DIAL, telUri);
        startActivity(returnIt);
    }
}
