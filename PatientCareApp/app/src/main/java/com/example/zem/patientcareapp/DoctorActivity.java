package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class DoctorActivity extends Activity implements View.OnClickListener {
    TextView doctor_name, specialty, clinic_name, clinic_address_first_line, clinic_address_second_line;
    ImageButton back_btn;
    Intent intent;
    int id = 0;
    int doctorID;

    DbHelper dbHelper;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        Intent intent = getIntent();
        doctorID = intent.getIntExtra("doctor_ID", 0);

        doctor_name = (TextView) findViewById(R.id.doctor_name);
        specialty = (TextView) findViewById(R.id.specialty);
        clinic_name = (TextView) findViewById(R.id.clinic_name);
        clinic_address_first_line = (TextView) findViewById(R.id.clinic_address_first_line);
        clinic_address_second_line = (TextView) findViewById(R.id.clinic_address_second_line);

        if (doctorID == 0) {

        } else {
            dbHelper = new DbHelper(this);
            doctor = dbHelper.getDoctorByID(doctorID);

            doctor_name.setText(doctor.getLname() + ", " + doctor.getFname() + " " + doctor.getMname().charAt(0) + ".");
            specialty.setText("(" + doctor.getSpecialty() + ", " + doctor.getSub_specialty() + ")");
        }
        showActionBar();
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
        switch (v.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
        }
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.back_menu_layout, null);
        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        back_btn = (ImageButton) v.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
    }
}
