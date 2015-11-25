package com.example.zem.patientcareapp.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.zem.patientcareapp.Controllers.DoctorController;
import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Model.Doctor;

/**
 * Created by Zem on 11/23/2015.
 */
public class Extravaganza extends AppCompatActivity {
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dbHelper = new DbHelper(this);
//        OrderPreferenceController order_preference_controller = new OrderPreferenceController(this);
//        order_preference_controller.getCustomerOrderPreference(1);
        DoctorController doctor_controller = new DoctorController(this);
        Doctor doctor = new Doctor();

        doctor.setServer_doc_id(1);
        doctor.setFullname("Zemiel", "Mamonong", "Asma");
        doctor.setPrc_no(12345);
        doctor.setSub_specialty_id(1);
        doctor.setAffiliation("affiliation");
        doctor.setEmail("lourdrivera123@gmail.com");
        doctor.setReferral_id("abc123");
        doctor.setCreated_at("00-00-00 00:00:00");
        doctor.setUpdated_at("00-00-00 00:00:00");
        doctor.setDeleted_at("00-00-00 00:00:00");

        doctor_controller.saveDoctor(doctor, "insert");

        Toast.makeText(this, "putang ina mo", Toast.LENGTH_LONG).show();
    }
}
