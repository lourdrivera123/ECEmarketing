package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import org.json.JSONArray;

/**
 * Created by Zem on 5/25/2015.
 */
public class PreloadNonUserRelatedData extends Activity {
    ProgressDialog pDialog;
    Helpers helpers;
    Sync sync;
    RequestQueue queue;
    DbHelper dbHelper;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");

        dbHelper = new DbHelper(this);
        helpers = new Helpers();

        pDialog.show();
        //process here.

        if (helpers.isNetworkAvailable(this)){

            pDialog.setMessage("Preparing Products...");

//            sync = new Sync();
//            sync.init(this, "get_doctors", "doctors", "doc_id");
//            queue = sync.getQueue();
//
//            pDialog.setMessage("Gathering Doctors...");
//
//            sync = new Sync();
//            sync.init(this, "get_products", "products", "product_id");
//            queue = sync.getQueue();
//
//            sync = new Sync();
//            sync.init(this, "get_dosages", "dosage_format_and_strength", "dosage_id");
//            queue = sync.getQueue();

            pDialog.hide();

            Intent mainactivity = new Intent(getBaseContext(), MainActivity.class);
            startActivity(mainactivity);


        } else {
            Log.d("Connected to internet", "no");
            dbHelper.getAllDoctors();
            String xml = dbHelper.getDoctorsStringXml();
            pDialog.setMessage("No Internet");
            pDialog.hide();
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
//            populateDoctorListView(rootView, xml);
//            pDialog.hide();
        }

    }
}
