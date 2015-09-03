package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.paypal.android.sdk.payments.PayPalPayment;

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

                //request for clinic
                GetRequest.getJSONobj(SplashActivity.this, "get_clinics", "clinics", "clinics_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - clinic request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for clinic doctor request
                GetRequest.getJSONobj(SplashActivity.this, "get_clinic_doctor", "clinic_doctor", "clinic_doctor_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - clinic_doctor request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for doctor request
                GetRequest.getJSONobj(SplashActivity.this, "get_doctors", "doctors", "doc_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - doctor request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for doctor specialties request
                GetRequest.getJSONobj(SplashActivity.this, "get_doctor_specialties", "specialties", "specialty_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - specialties request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for doctor subspecialties request
                GetRequest.getJSONobj(SplashActivity.this, "get_doctor_sub_specialties", "sub_specialties", "sub_specialty_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - subspecialties request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for product categories request
                GetRequest.getJSONobj(SplashActivity.this, "get_product_categories", "product_categories", "product_category_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - product categories request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for product subcategories request
                GetRequest.getJSONobj(SplashActivity.this, "get_product_subcategories&cat=all", "product_subcategories", "product_subcategory_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - product subcategories request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for products
                GetRequest.getJSONobj(SplashActivity.this, "get_products", "products", "product_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - products request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for dosages request
                GetRequest.getJSONobj(SplashActivity.this, "get_dosages", "dosage_format_and_strength", "dosage_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - dosages request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for patient records request
                GetRequest.getJSONobj(SplashActivity.this, "get_patient_records", "patient_records", "record_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - patient records request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for treatments request
                GetRequest.getJSONobj(SplashActivity.this, "get_treatments", "treatments", "treatments_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - treatments request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for promos request
                GetRequest.getJSONobj(SplashActivity.this, "get_promo", "promo", "promo_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - promos request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for discounts_free_products request
                GetRequest.getJSONobj(SplashActivity.this, "get_discounts_free_products", "discounts_free_products", "dfp_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - discounts_free_products request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                //request for free_products request
                GetRequest.getJSONobj(SplashActivity.this, "get_free_products", "free_products", "free_products_id", new RespondListener<JSONObject>() {
                    @Override
                    public void getResult(JSONObject response) {
                        Log.d("response using interface <SplashActivity.java - free_products request >", response + "");
                    }
                }, new ErrorListener<VolleyError>() {
                    public void getError(VolleyError error) {
                        Log.d("Error", error + "");
                        Toast.makeText(SplashActivity.this, "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                });

                Intent mainactivity = new Intent(getBaseContext(), MainActivity.class);
                startActivity(mainactivity);
                finish();

                settings.edit().putBoolean("my_first_time", false).commit();
            } else {
                Log.d("<SplashActivity>", "no internet");
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
