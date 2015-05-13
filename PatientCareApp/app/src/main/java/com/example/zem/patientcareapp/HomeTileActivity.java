package com.example.zem.patientcareapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Zem on 5/2/2015.
 */
public class HomeTileActivity extends ActionBarActivity implements View.OnClickListener {
    Button profile_btn, news_btn, promos_btn, doctors_btn, history_btn, test_results_btn, cart_btn, products_btn, consultation_btn;
    FragmentTransaction fragmentTransaction;
    DbHelper dbHelper;

//    JSONArray doctors_json_array_mysql = null;
//    JSONArray doctors_json_array_sqlite = null;
//    JSONArray doctors_json_array_final = null;
//
//    RequestQueue queue;
//    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tile_layout);

//        queue = Volley.newRequestQueue(this);
        dbHelper = new DbHelper(this);
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        url = "http://192.168.10.1/db/get_all_doctors.php";

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        profile_btn = (Button) findViewById(R.id.profile_btn);
        news_btn = (Button) findViewById(R.id.news_btn);
        promos_btn = (Button) findViewById(R.id.promos_btn);
        doctors_btn = (Button) findViewById(R.id.doctors_btn);
        history_btn = (Button) findViewById(R.id.history_btn);
        test_results_btn = (Button) findViewById(R.id.test_results_btn);
        cart_btn = (Button) findViewById(R.id.cart_btn);
        products_btn = (Button) findViewById(R.id.products_btn);
        consultation_btn = (Button) findViewById(R.id.consultation_btn);

        consultation_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        news_btn.setOnClickListener(this);
        promos_btn.setOnClickListener(this);
        doctors_btn.setOnClickListener(this);
        history_btn.setOnClickListener(this);
        test_results_btn.setOnClickListener(this);
        cart_btn.setOnClickListener(this);
        products_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MasterTabActivity.class);
        switch(v.getId()) {

            case R.id.profile_btn:
                intent.putExtra("selected", 0);
                startActivity(intent);
                break;

            case R.id.history_btn:
                intent.putExtra("selected", 1);
                startActivity(intent);
                break;

            case R.id.test_results_btn:
                intent.putExtra("selected", 2);
                startActivity(intent);
                break;

            case R.id.doctors_btn:
//                // Request a string response from the provided URL.
//                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            int success = response.getInt("success");
//                            if (success == 1) {
//                                doctors_json_array_mysql = response.getJSONArray("doctors");
//                                doctors_json_array_sqlite = dbHelper.getAllDoctorsJSONArray();
//
//                                doctors_json_array_final = checkWhatToInsert(doctors_json_array_mysql, doctors_json_array_sqlite);
//
//                                Log.d("doctor json array final", "" + doctors_json_array_final);
//
//                                if( doctors_json_array_final != null){
//                                    for (int i = 0; i < doctors_json_array_final.length(); i++) {
//                                        JSONObject doctor_json_object = doctors_json_array_final.getJSONObject(i);
//
//                                        Log.d("esel", ""+doctor_json_object);
//
//                                        if(!doctor_json_object.equals("null")){
//                                            if (dbHelper.insertDoctor(setDoctorObject(doctor_json_object))) {
//                                                Toast.makeText(getBaseContext(), "successfully saved " , Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                Toast.makeText(getBaseContext(), "failed to save " , Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    }
//                                    doctors_json_array_final = null;
//                                } else {
//                                    Toast.makeText(getBaseContext(), "the final list is empty", Toast.LENGTH_SHORT).show();
//                                }
//
//                            }
//
//                        } catch (JSONException e) {
//                            Toast.makeText(getBaseContext(), "" + e, Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                queue.add(stringRequest);
                intent.putExtra("selected", 3);
                startActivity(intent);
                break;

            case R.id.consultation_btn:
                intent.putExtra("selected", 4);
                startActivity(intent);
                break;

            case R.id.products_btn:
                intent.putExtra("selected", 5);
                startActivity(intent);
                break;

            case R.id.cart_btn:
                intent.putExtra("selected", 6);
                startActivity(intent);
                break;

            case R.id.promos_btn:
                intent.putExtra("selected", 7);
                startActivity(intent);
                break;

            case R.id.news_btn:
                intent.putExtra("selected", 8);
                Toast.makeText(this, ""+dbHelper.getAllDoctorsJSONArray(), Toast.LENGTH_LONG).show();
//                startActivity(intent);
                break;
        }
    }
//
//    public JSONArray checkWhatToInsert(JSONArray doctors_json_array_mysql, JSONArray doctors_json_array_sqlite) throws JSONException {
//        JSONArray doctors_json_array_final_storage = new JSONArray();
//        try {
//            for (int i = 0; i < doctors_json_array_mysql.length(); i++) {
//                JSONObject doctor_json_object_mysql = doctors_json_array_mysql.getJSONObject(i);
//                Boolean flag = false;
//
//                if(doctors_json_array_sqlite == null){
//                    doctors_json_array_final_storage.put(doctor_json_object_mysql);
//                } else {
//
//                    for (int x = 0; x < doctors_json_array_sqlite.length(); x++) {
//                        JSONObject doctor_json_object_sqlite = doctors_json_array_sqlite.getJSONObject(x);
//
//                        if (doctor_json_object_mysql.getInt("id") == doctor_json_object_sqlite.getInt("doc_id")) {
//                            flag = true;
//                        }
//                    }
//
//                    if (!flag) {
//                        doctors_json_array_final_storage.put(doctor_json_object_mysql);
//                    }
//
//                }
//            }
//
//        } catch (JSONException e) {
//            Toast.makeText(getBaseContext(), "" + e, Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//        return doctors_json_array_final_storage;
//    }
//
//    public Doctor setDoctorObject(JSONObject doctor_json_object){
//
//        Doctor doctor_object = new Doctor();
//        try {
//
//            doctor_object.setDoc_id(doctor_json_object.getInt("id"));
//            doctor_object.setClinic_id(doctor_json_object.getInt("clinic_id"));
//            doctor_object.setSecretary_id(doctor_json_object.getInt("secretary_id"));
//            doctor_object.setFullname(doctor_json_object.getString("fname"), doctor_json_object.getString("mname"), doctor_json_object.getString("lname"));
//            doctor_object.setFullAddress(doctor_json_object.getString("address_house_no"), doctor_json_object.getString("address_street"),
//                    doctor_json_object.getString("address_barangay"), doctor_json_object.getString("address_city_municipality"),
//                    doctor_json_object.getString("address_province"), doctor_json_object.getString("address_region"),
//                    doctor_json_object.getString("address_country"), doctor_json_object.getString("address_zip"));
//            doctor_object.setPrc_no(doctor_json_object.getInt("prc_no"));
//            doctor_object.setSpecialty(doctor_json_object.getString("specialty"));
//            doctor_object.setSub_specialty(doctor_json_object.getString("sub_specialty"));
//            doctor_object.setCell_no(doctor_json_object.getString("cell_no"));
//            doctor_object.setTel_no(doctor_json_object.getString("tel_no"));
//            doctor_object.setPhoto(doctor_json_object.getString("photo"));
//            doctor_object.setClinic_sched(doctor_json_object.getString("clinic_sched"));
//            doctor_object.setEmail(doctor_json_object.getString("email"));
//            doctor_object.setAffiliation(doctor_json_object.getString("affiliation"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return doctor_object;
//    }


}
