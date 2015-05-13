package com.example.zem.patientcareapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 5/6/2015.
 */
public class StandStrong extends Activity {

    TextView mTextView;
    ArrayList<HashMap<String, String>> doctors_list;
    DbHelper dbHelper;
    JSONArray doctors_json_array = null;
    String fname = "", lname = "", mname = "", address_house_no = "", address_street = "",
            address_barangay = "", address_city_municipality = "", address_province = "", address_region = "",
            address_country = "", address_zip = "", specialty = "", sub_specialty = "", cell_no = "",
            tel_no = "", photo = "", clinic_sched = "", affiliation = "", email = "";
    int doctor_id = 0, prc_no = 0, clinic_id = 0, secretary_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_for_volley_layout);
        mTextView = (TextView) findViewById(R.id.tangina);
        doctors_list = new ArrayList<HashMap<String, String>>();
        dbHelper = new DbHelper(this);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.10.1/db/get_all_doctors.php";

// Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // Display the first 500 characters of the response string.
//                mTextView.setText("Response is: "+ response.substring(0,500));
//                mTextView.setText("ambot basta: "+response.toString());
//                Log.d("All Doctors: ", response.toString());
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        doctors_json_array = response.getJSONArray("doctors");

//                       JSONObject c = doctors_json_array.getJSONObject(1);
//
//                        mTextView.setText(""+c);

                        // looping through All Products
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject doctor_json_object = doctors_json_array.getJSONObject(i);

                            // Storing each json item in variable
                            String id = doctor_json_object.getString("id");
                            String lname = doctor_json_object.getString("lname");


                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
//                            map.put("id", id);
//                            map.put("lname", lname);

//                            if(doctor_json_object.has("fname")){
//                                fname = doctor_json_object.getString("fname");
//                            }



//              Check if there are null values and put it into variable

                            fname = doctor_json_object.getString("fname");
                            lname = doctor_json_object.getString("lname");
                            mname = doctor_json_object.getString("mname");
                            clinic_id = doctor_json_object.getInt("clinic_id");
                            secretary_id = doctor_json_object.getInt("secretary_id");
                            prc_no = doctor_json_object.getInt("prc_no");
                            address_house_no = doctor_json_object.getString("address_house_no");
                            address_street = doctor_json_object.getString("address_street");
                            address_barangay = doctor_json_object.getString("address_barangay");
                            address_city_municipality = doctor_json_object.getString("address_city_municipality");
                            address_province = doctor_json_object.getString("address_province");
                            address_region = doctor_json_object.getString("address_region");
                            address_country = doctor_json_object.getString("address_country");
                            address_zip = doctor_json_object.getString("address_zip");
                            specialty = doctor_json_object.getString("specialty");
                            sub_specialty = doctor_json_object.getString("sub_specialty");
                            cell_no = doctor_json_object.getString("cell_no");
                            tel_no = doctor_json_object.getString("tel_no");
                            photo = doctor_json_object.getString("photo");
                            clinic_sched = doctor_json_object.getString("clinic_sched");
                            email = doctor_json_object.getString("email");
                            affiliation = doctor_json_object.getString("affiliation");


//                            fname = (doctor_json_object.has("fname")) ? doctor_json_object.getString("fname") : "";
//                            lname = (doctor_json_object.has("lname")) ? doctor_json_object.getString("lname") : "";
//                            mname = (doctor_json_object.has("mname")) ? doctor_json_object.getString("mname") : "";
//                            clinic_id = (doctor_json_object.has("clinic_id")) ? doctor_json_object.getInt("clinic_id") : 0;
//                            secretary_id = (doctor_json_object.has("secretary_id")) ? doctor_json_object.getInt("secretary_id") : 0;
//                            prc_no = (doctor_json_object.has("prc_no")) ? doctor_json_object.getInt("prc_no") : 0;
//                            address_house_no = (doctor_json_object.has("address_house_no")) ? doctor_json_object.getString("address_house_no") : "";
//                            address_street = (doctor_json_object.has("address_street")) ? doctor_json_object.getString("address_street") : "";
//                            address_barangay = (doctor_json_object.has("address_barangay")) ? doctor_json_object.getString("address_barangay") : "";
//                            address_city_municipality = (doctor_json_object.has("address_city_municipality")) ? doctor_json_object.getString("address_city_municipality") : "";
//                            address_province = (doctor_json_object.has("address_province")) ? doctor_json_object.getString("address_province") : "";
//                            address_region = (doctor_json_object.has("address_region")) ? doctor_json_object.getString("address_region") : "";
//                            address_country = (doctor_json_object.has("address_country")) ? doctor_json_object.getString("address_country") : "";
//                            address_zip = (doctor_json_object.has("address_zip")) ? doctor_json_object.getString("address_zip") : "";
//                            specialty = (doctor_json_object.has("specialty")) ? doctor_json_object.getString("specialty") : "";
//                            sub_specialty = (doctor_json_object.has("sub_specialty")) ? doctor_json_object.getString("sub_specialty") : "";
//                            cell_no = (doctor_json_object.has("cell_no")) ? doctor_json_object.getString("cell_no") : "";
//                            tel_no = (doctor_json_object.has("tel_no")) ? doctor_json_object.getString("tel_no") : "";
//                            photo = (doctor_json_object.has("photo")) ? doctor_json_object.getString("photo") : "";
//                            clinic_sched = (doctor_json_object.has("cell_no")) ? doctor_json_object.getString("clinic_sched") : "";
//                            email = (doctor_json_object.has("email")) ? doctor_json_object.getString("email") : "";
//                            affiliation = (doctor_json_object.has("affiliation")) ? doctor_json_object.getString("affiliation") : "";

//          Initiate Doctor Object and set the attributes

                            Doctor doctor_object = new Doctor();

//                            doctor_object.setFname(fname);
//                            doctor_object.setMname(mname);
//                            doctor_object.setLname(lname);
//                            doctor_object.setID(c.getInt("doctor_id"));
                            doctor_object.setClinic_id(clinic_id);
                            doctor_object.setSecretary_id(secretary_id);
                            doctor_object.setFullname(fname, mname, lname);
                            doctor_object.setFullAddress(address_house_no, address_street, address_barangay, address_city_municipality,
                                    address_province, address_region, address_country, address_zip);
                            doctor_object.setPrc_no(prc_no);
                            doctor_object.setSpecialty(specialty);
                            doctor_object.setSub_specialty(sub_specialty);
                            doctor_object.setCell_no(cell_no);
                            doctor_object.setTel_no(tel_no);
                            doctor_object.setPhoto(photo);
                            doctor_object.setClinic_sched(clinic_sched);
                            doctor_object.setEmail(email);
                            doctor_object.setAffiliation(affiliation);
//
                            if (dbHelper.insertDoctor(doctor_object)) {
                                Toast.makeText(getBaseContext(), "successfully saved " + fname, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getBaseContext(), "failed to save "+ fname, Toast.LENGTH_SHORT).show();
                            }


                            // adding HashList to ArrayList
                            doctors_list.add(map);
                        }
                    }

                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        Toast.makeText(this, "String is: "+dbHelper.getDoctorsStringXml(), Toast.LENGTH_LONG).show();
    }
}
