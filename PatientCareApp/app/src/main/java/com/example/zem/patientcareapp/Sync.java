package com.example.zem.patientcareapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Zem on 5/11/2015.
 */
public class Sync {

    JSONArray json_array_mysql = null;
    JSONArray json_array_sqlite = null;
    JSONArray json_array_final = null;

    RequestQueue queue;
    String url, tableName, tableId;
    DbHelper dbHelper;
    Context context;

    public void init(Context c, String request, String table_name, String table_id){
        tableName = table_name;
        tableId = table_id;
        context = c;

        dbHelper = new DbHelper(context);
        queue = Volley.newRequestQueue(context);
        url = "http://192.168.1.12/db/get.php?q="+request;

        // Request a string response from the provided URL.
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response){
                try {
                    int success = response.getInt("success");
                    if (success == 1) {
                        json_array_mysql = response.getJSONArray(tableName);
                        json_array_sqlite = dbHelper.getAllJSONArrayFrom(tableName);

                        Log.d("jsonarraymysql", ""+json_array_mysql);
                        Log.d("jsonarraysqlite", ""+json_array_sqlite);

                        json_array_final = checkWhatToInsert(json_array_mysql, json_array_sqlite, tableId);

                        if (json_array_final != null){
                            for (int i = 0; i < json_array_final.length(); i++) {
                                JSONObject json_object = json_array_final.getJSONObject(i);

                                Log.d("esel", ""+json_object);

                                if(!json_object.equals("null")){
                                    if( tableName == "products" ){
                                         if (dbHelper.insertProduct(setProduct(json_object))) {
                                             Toast.makeText(context, "successfully saved " , Toast.LENGTH_SHORT).show();
                                         } else {
                                             Toast.makeText(context, "failed to save " , Toast.LENGTH_SHORT).show();
                                         }
                                    }else if( tableName == "doctors" ){
                                       if (dbHelper.insertDoctor(setDoctor(json_object))) {
                                            Toast.makeText(context, "successfully saved " , Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "failed to save " , Toast.LENGTH_SHORT).show();
                                        } 
                                    } else if( tableName == "dosage_format_and_strength") {
                                        if (dbHelper.insertDosage(setDosage(json_object))) {
                                            Toast.makeText(context, "successfully saved " , Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "failed to save " , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                           json_array_final = null;
                        } else {
                            Toast.makeText(context, "the final list is empty", Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    Toast.makeText(context, "general error" + e, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error on request", Toast.LENGTH_SHORT).show();
                System.out.println("GWAPO DAW KO: " + error);
            }
        });

        queue.add(stringRequest);
    }

    public RequestQueue getQueue(){
        return this.queue;
    }

    public Dosage setDosage(JSONObject json_object){
        Dosage dosage = new Dosage();
        try {
            dosage.setDosage_id(json_object.getInt("id"));
            dosage.setProduct_id(json_object.getInt("product_id"));
            dosage.setName(json_object.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dosage;
    }

    public JSONArray checkWhatToInsert(JSONArray json_array_mysql, JSONArray json_array_sqlite, String server_id) throws JSONException {
        JSONArray json_array_final_storage = new JSONArray();
        try {
            for (int i = 0; i < json_array_mysql.length(); i++) {
                JSONObject product_json_object_mysql = json_array_mysql.getJSONObject(i);
                Boolean flag = false;

                if(json_array_sqlite == null){
                   json_array_final_storage.put(product_json_object_mysql);
                } else {

                    for (int x = 0; x < json_array_sqlite.length(); x++) {
                        JSONObject json_object_sqlite = json_array_sqlite.getJSONObject(x);

                        if (product_json_object_mysql.getInt("id") == json_object_sqlite.getInt(server_id)) {
                            flag = true;
                        }
                    }

                    if (!flag) {
                       json_array_final_storage.put(product_json_object_mysql);
                    }

                }
            }

        } catch (JSONException e) {
            Toast.makeText(context, "error in check what to insert" + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return json_array_final_storage;
    }

    public boolean checkDateTime(String str1, String str2) {
        Boolean something = false;
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//            String str1 = "12/10/2013";
            Date date1 = formatter.parse(str1);

//            String str2 = "13/10/2013";
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2) < 0) {
                something = true;
//                System.out.println("date2 is Greater than my date1");
//            Log.d("date2 is Greater than my date1", "adsasd");
            }


        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return something;
    }

    public Doctor setDoctor(JSONObject json_object) {
        Doctor doctor_object = new Doctor();
        try {

            doctor_object.setDoc_id(json_object.getInt("id"));
            doctor_object.setClinic_id(json_object.getInt("clinic_id"));
            doctor_object.setSecretary_id(json_object.getInt("secretary_id"));
            doctor_object.setFullname(json_object.getString("fname"), json_object.getString("mname"), json_object.getString("lname"));
            doctor_object.setFullAddress(json_object.getString("address_house_no"), json_object.getString("address_street"),
                    json_object.getString("address_barangay"), json_object.getString("address_city_municipality"),
                    json_object.getString("address_province"), json_object.getString("address_region"),
                    json_object.getString("address_country"), json_object.getString("address_zip"));
            doctor_object.setPrc_no(json_object.getInt("prc_no"));
            doctor_object.setSpecialty(json_object.getString("specialty"));
            doctor_object.setSub_specialty(json_object.getString("sub_specialty"));
            doctor_object.setCell_no(json_object.getString("cell_no"));
            doctor_object.setTel_no(json_object.getString("tel_no"));
            doctor_object.setPhoto(json_object.getString("photo"));
            doctor_object.setClinic_sched(json_object.getString("clinic_sched"));
            doctor_object.setEmail(json_object.getString("email"));
            doctor_object.setAffiliation(json_object.getString("affiliation"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return doctor_object;
    }

    public Patient setPatient(JSONObject json_object){
        Patient patient_object = new Patient();
        try {
            patient_object.setServerID(json_object.getInt("id"));
            patient_object.setUsername(json_object.getString("username"));
            patient_object.setPassword(json_object.getString("password"));
            patient_object.setOccupation(json_object.getString("occupation"));
            patient_object.setBirthdate(json_object.getString("birthdate"));
            patient_object.setSex(json_object.getString("sex"));
            patient_object.setCivil_status(json_object.getString("civil_status"));
            patient_object.setHeight(json_object.getString("height"));
            patient_object.setWeight(json_object.getString("weight"));
            patient_object.setFullname(json_object.getString("fname"), json_object.getString("mname"), json_object.getString("lname"));
            patient_object.setFullAddress(json_object.getInt("unit_floor_room_no"), json_object.getString("building"),
                    json_object.getInt("lot_no"), json_object.getInt("block_no"), json_object.getInt("phase_no"),
                    json_object.getInt("address_house_no"), json_object.getString("address_street"),
                    json_object.getString("address_barangay"), json_object.getString("address_city_municipality"),
                    json_object.getString("address_province"), json_object.getString("address_region"),
                    json_object.getString("address_country"), json_object.getString("address_zip"));
            patient_object.setTel_no(json_object.getString("tel_no"));
            patient_object.setCell_no(json_object.getString("cell_no"));
            patient_object.setEmail(json_object.getString("email"));
            patient_object.setPhoto(json_object.getString("photo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patient_object;
    }
    public Product setProduct(JSONObject json_object) {
        Product product_object = new Product();

        try {
            product_object.setProductId(json_object.getInt("id"));
            product_object.setName(json_object.getString("name"));
            product_object.setGenericName(json_object.getString("generic_name"));
            product_object.setDescription(json_object.getString("description"));
            product_object.setPrice(json_object.getInt("price"));
            product_object.setUnit(json_object.getString("unit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return product_object;
    }
}
