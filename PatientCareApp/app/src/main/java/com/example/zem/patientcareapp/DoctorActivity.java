package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.zem.patientcareapp.GetterSetter.Doctor;

public class DoctorActivity extends Activity implements View.OnClickListener {
    TextView doctor_name, specialty, clinic_name, clinic_address_first_line, clinic_address_second_line;

    DbHelper dbHelper;
    Doctor doctor;

    public static final String PARENT_ACTIVITY = "parent_acitivity";
    String get_parent_activity = "";
    int id = 0;
    int doctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        doctorID = intent.getIntExtra(dbHelper.RECORDS_DOCTOR_ID, 0);
        get_parent_activity = intent.getStringExtra(PARENT_ACTIVITY);

        doctor_name = (TextView) findViewById(R.id.doctor_name);
        specialty = (TextView) findViewById(R.id.specialty);
        clinic_name = (TextView) findViewById(R.id.clinic_name);
        clinic_address_first_line = (TextView) findViewById(R.id.clinic_address_first_line);
        clinic_address_second_line = (TextView) findViewById(R.id.clinic_address_second_line);

        if (doctorID == 0) {

        } else {
            doctor = dbHelper.getDoctorByID(doctorID);

            doctor_name.setText(doctor.getLname() + ", " + doctor.getFname() + " " + doctor.getMname().charAt(0) + ".");
            specialty.setText("(" + doctor.getSpecialty() + ", " + doctor.getSub_specialty() + ")");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
//        String phoneNumber = "09095331440";
//        String uriTel = "tel:" + phoneNumber;
//
//        //Present you with the dialler
//        Uri telUri = Uri.parse(uriTel);
//        Intent returnIt = new Intent(Intent.ACTION_DIAL, telUri);
//        startActivity(returnIt);
    }
}
