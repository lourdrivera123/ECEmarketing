package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

/**
 * Created by Zem on 4/29/2015.
 */
public class PatientHomeActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView list_of_doctors;
    String[] doctors = new String[] {
            "Dr. Zemiel M. Asma", "Dr. Rosell B. Barnes", "Dr. Dexter M. Bengil"
    };
    ArrayAdapter doctors_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_home_layout);

        list_of_doctors = (ListView) findViewById(R.id.list_of_doctors);
        doctors_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctors);
        list_of_doctors.setAdapter(doctors_adapter);
        list_of_doctors.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String doctor = (String) list_of_doctors.getItemAtPosition(position);
        Intent intent = new Intent(this, DoctorActivity.class);
        startActivity(intent);
    }
}
