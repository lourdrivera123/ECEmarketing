package com.example.zem.patientcareapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * Created by Zem on 4/29/2015.
 */
public class DoctorActivity extends ActionBarActivity implements View.OnClickListener {
    ImageButton call_doctor;
    TextView cp_no, fullname, specialty, sub_spcialty, address_first_line, address_second_line,
        email, clinic_name, clinic_address_first_line, clinic_address_second_line;
    Intent intent;
    int id = 0;
    DbHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        intent = getIntent();
        dbHelper = new DbHelper(getBaseContext());
        id = Integer.parseInt(intent.getStringExtra("id"));

        fullname = (TextView) findViewById(R.id.doctor_full);
        specialty = (TextView) findViewById(R.id.specialty);
        sub_spcialty = (TextView) findViewById(R.id.sub_specialty);
        address_first_line = (TextView) findViewById(R.id.address_first_line);
        address_second_line = (TextView) findViewById(R.id.address_second_line);
        email = (TextView) findViewById(R.id.email);
        clinic_name = (TextView) findViewById(R.id.clinic_name);
        clinic_address_first_line = (TextView) findViewById(R.id.clinic_address_first_line);
        clinic_address_second_line = (TextView) findViewById(R.id.clinic_address_second_line);

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
