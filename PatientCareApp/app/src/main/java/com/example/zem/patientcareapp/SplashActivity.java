package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Zem on 6/1/2015.
 */
public class SplashActivity extends Activity {

    ProgressDialog pDialog;
    Helpers helpers;
    Sync sync;
    RequestQueue queue;
    DbHelper dbHelper;
    public static final String PREFS_NAME = "firstTimeUsePref";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        dbHelper = new DbHelper(this);
        helpers = new Helpers();

        queue = Volley.newRequestQueue(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task

            if (helpers.isNetworkAvailable(this)) {

                // Request a string response from the provided URL.
                JsonObjectRequest clinic_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_clinics"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_clinics", "clinics", "clinics_id", response);
                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("clinics", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest clinic_doctor_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_clinic_doctor"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_clinic_doctor", "clinic_doctor", "clinic_doctor_id", response);
                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("clinic_doctor", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest doctor_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctors"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_doctors", "doctors", "doc_id", response);
                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("doctors", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                        System.out.println("GWAPO DAW KO: " + error);
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest doctor_specialty_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctor_specialties"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_doctor_specialties", "specialties", "specialty_id", response);

                        System.out.println("response in specialty: " + response.toString());

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("specialties", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                        System.out.println("GWAPO DAW KO: " + error);
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest doctor_sub_specialty_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_doctor_sub_specialties"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_doctor_sub_specialties", "sub_specialties", "sub_specialty_id", response);

                        System.out.println("response in sub specialty: " + response.toString());
                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("sub_specialties", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                        System.out.println("GWAPO DAW KO: " + error);
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest prod_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_products"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("fucking response on SplashActivity@get_products: " + response);
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_products", "products", "product_id", response);

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("products", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error on SplashActivity@get_products: " + e);
                        }

                        Intent mainactivity = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(mainactivity);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                        System.out.println("error on SplashActivity@get_products: " + error);
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest prod_category_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_product_categories"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_product_categories", "product_categories", "product_category_id", response);
                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("product_categories", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest prod_sub_cat_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_product_subcategories&cat=all"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_product_subcategories&cat=all", "product_subcategories", "product_subcategory_id", response);

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("product_subcategories", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest dosage_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_dosages"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        sync = new Sync();
                        sync.init(getBaseContext(), "get_dosages", "dosage_format_and_strength", "dosage_id", response);

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("dosage_format_and_strength", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest patient_record_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_patient_records"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.print("patient records: I am in splash activity");
                        Log.d("splash patient record response: ", "" + response.toString());
                        sync = new Sync();
                        sync.init(getBaseContext(), "get_patient_records", "patient_records", "record_id", response);

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("patient_records", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest treatments_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_treatments"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("splash treatment response: ", "" + response.toString());

                        System.out.print("treatments: I am in splash activity");

                        sync = new Sync();
                        sync.init(getBaseContext(), "get_treatments", "treatments", "treatments_id", response);

                        try {
                            System.out.println("timestamp from server: " + response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("treatments", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest promo_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_promo"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("splash promo discounts response: ", "" + response.toString());

                        System.out.print("promo discounts: I am in splash activity");

                        sync = new Sync();
                        sync.init(getBaseContext(), "get_promo", "promo", "promo_id", response);

                        try {
                            System.out.println("timestamp from server: "+response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("promo", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });



                // Request a string response from the provided URL.
                JsonObjectRequest discounts_free_products_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_discounts_free_products"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("splash get_discounts_free_products response: ", ""+response.toString());

                        System.out.print("promo free_products: I am in splash activity");

                        sync = new Sync();
                        sync.init(getBaseContext(), "get_discounts_free_products", "discounts_free_products", "dfp_id", response);

                        try {
                            System.out.println("timestamp from server: "+response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("discounts_free_products", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: "+ e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                // Request a string response from the provided URL.
                JsonObjectRequest free_products_request = new JsonObjectRequest(Request.Method.GET, helpers.get_url("get_free_products"), null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("splash get_free_products response: ", ""+response.toString());

                        System.out.println("free_products: I am in splash activity");

                        sync = new Sync();
                        sync.init(getBaseContext(), "get_free_products", "free_products", "free_products_id", response);

                        try {
                            System.out.println("timestamp from server: "+response.getString("server_timestamp"));
                            dbHelper.updateLastUpdatedTable("promo_discounts", response.getString("server_timestamp"));
                        } catch (Exception e) {
                            System.out.println("error fetching server timestamp: "+ e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getBaseContext(), "Error on request", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(doctor_request);
                queue.add(clinic_request);
                queue.add(doctor_specialty_request);
                queue.add(doctor_sub_specialty_request);
                queue.add(prod_category_request);
                queue.add(prod_sub_cat_request);
                queue.add(patient_record_request);
                queue.add(treatments_request);
                queue.add(dosage_request);
                queue.add(prod_request);
                queue.add(promo_request);
                queue.add(discounts_free_products_request);
                queue.add(free_products_request);
                queue.add(clinic_doctor_request);

                settings.edit().putBoolean("my_first_time", false).commit();


            } else {
                Log.d("Connected to internet", "no");

                Toast.makeText(this, "You must have Internet to be able to use the App properly", Toast.LENGTH_LONG).show();
            }

            // record the fact that the app has been started at least once
        } else {
            Intent mainactivity = new Intent(getBaseContext(), MainActivity.class);
            startActivity(mainactivity);
            finish();
        }
    }
}
