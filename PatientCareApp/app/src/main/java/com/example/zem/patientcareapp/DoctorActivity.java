package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Zem on 4/29/2015.
 */

public class DoctorActivity extends Activity implements View.OnClickListener {
    ImageButton call_doctor;
    TextView doctor_name, specialty, sub_specialty, address_first_line, address_second_line, email, cp_no, clinic_name, clinic_address_first_line, clinic_address_second_line;
    TableLayout tbl_doctors_clinic;
    Intent intent;
    int id = 0;
    int doctorID;

    DbHelper dbHelper;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);
//        tbl_doctors_clinic = (TableLayout) findViewById(R.id.tbl_doctors_clinic);

        intent = getIntent();
        dbHelper = new DbHelper(getBaseContext());
        id = Integer.parseInt(intent.getStringExtra("id"));


        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);

        Intent intent = getIntent();
        doctorID = intent.getIntExtra("doctor_ID", 0);

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

        if (doctorID == 0) {

        } else {
            dbHelper = new DbHelper(this);
            doctor = dbHelper.getDoctorByID(doctorID);
        }
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
