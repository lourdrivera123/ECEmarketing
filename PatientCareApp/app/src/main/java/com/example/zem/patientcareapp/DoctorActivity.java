package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zem.patientcareapp.GetterSetter.Doctor;
import com.example.zem.patientcareapp.GetterSetter.Patient;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorActivity extends Activity implements View.OnClickListener {
    TextView doctor_name, specialty, clinic_name, clinic_address_first_line, clinic_address_second_line, contact_number;
    ListView listOfDoctors;
    Button scheduleConsultation;

    ArrayList<HashMap<String, String>> hashClinicsByDoctorID;

    private CustomAdapterForDoctor customAdapter;
    DbHelper dbHelper;
    Doctor doctor;

    int id = 0;
    int doctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_layout);

        doctor_name = (TextView) findViewById(R.id.doctor_name);
        specialty = (TextView) findViewById(R.id.specialty);
        listOfDoctors = (ListView) findViewById(R.id.listOfDoctors);
        scheduleConsultation = (Button) findViewById(R.id.scheduleConsultation);

        ActionBar actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DbHelper(this);
        Intent intent = getIntent();
        doctorID = intent.getIntExtra(dbHelper.DOC_DOC_ID, 0);
        hashClinicsByDoctorID = dbHelper.getClinicByDoctorID(doctorID);

        customAdapter = new CustomAdapterForDoctor(this, R.layout.list_item_of_doctors_layout, R.id.clinic_name, hashClinicsByDoctorID);
        listOfDoctors.setAdapter(customAdapter);

        if (doctorID > 0) {
            doctor = dbHelper.getDoctorByID(doctorID);

            doctor_name.setText(doctor.getLname() + ", " + doctor.getFname() + " " + doctor.getMname().charAt(0) + ".");
            specialty.setText("(" + doctor.getSpecialty() + ", " + doctor.getSub_specialty() + ")");
        }

        scheduleConsultation.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PatientConsultationActivity.class);
        intent.putExtra("request", "add");
        intent.putExtra("doctorID", doctorID);
        startActivity(intent);
    }

    public class CustomAdapterForDoctor extends ArrayAdapter {

        public CustomAdapterForDoctor(Context context, int resource, int textViewResourceId, ArrayList<HashMap<String, String>> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views

            clinic_name = (TextView) v.findViewById(R.id.clinic_name);
            clinic_address_first_line = (TextView) v.findViewById(R.id.clinic_address_first_line);
            clinic_address_second_line = (TextView) v.findViewById(R.id.clinic_address_second_line);
            contact_number = (TextView) v.findViewById(R.id.contact_number);
            String firstLine, secondLine, building = "", barangay = "";

            if (!hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESSS_UNIT_BUILDING_NO).equals(""))
                building = "#" + hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESSS_UNIT_BUILDING_NO);
            if (!hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_BARANGAY).equals(""))
                barangay = ", " + hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_BARANGAY);

            firstLine = (building + hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_STREET) + barangay).trim();
            secondLine = hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_CITY_MUNICIPALITY) +
                    ", " + hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_REGION) +
                    ", Philippines, " + hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_ADDRESS_ZIP);

            clinic_name.setText(hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_NAME));
            clinic_address_first_line.setText(firstLine);
            clinic_address_second_line.setText(secondLine);
            contact_number.setText(hashClinicsByDoctorID.get(position).get(dbHelper.CLINIC_CONTACT_NO));

            return v;
        }
    }
}
