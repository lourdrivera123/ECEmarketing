package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Zem on 4/29/2015.
 */
public class PatientHistoryActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView list_of_history;
    Button view_doctor_btn;

    ArrayAdapter history_adapter;
    String[] history = new String[]{
            "Dr. Zemiel Asma, January 9, 1995", "Dr. Dexter Bengil, March 5, 2000"
    };
    int check = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_records_layout);

        list_of_history = (ListView) findViewById(R.id.list_of_history);
        history_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, history);
        list_of_history.setAdapter(history_adapter);
        list_of_history.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Dialog dialog = new Dialog(this);
        dialog.setTitle("Medical Records");
        dialog.setContentView(R.layout.patient_diagnosis_layout);
        dialog.show();

//        view_doctor_btn = (Button) findViewById(R.id.view_doctor_btn);
//        check = 23;
//        view_doctor_btn.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        if (check > 0) {
//            switch (v.getId()) {
//                case R.id.view_doctor_btn:
//                    startActivity(new Intent(this, DoctorActivity.class));
//                    break;
//            }
//        } else {
//
//        }
//    }
}
