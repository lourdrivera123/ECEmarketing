package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.GetterSetter.Patient;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.GetRequest;
import com.example.zem.patientcareapp.Network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText username_txtfield, password_txtfield;
    TextView signup, forgotpw;
    Button login_btn;

    public static final String MyPREFERENCES = "MyPrefs", name = "nameKey", pass = "passwordKey";
    public static SharedPreferences sharedpreferences;
    String uname, password;
    String url = Constants.POST_URL;

    ProgressDialog pDialog;
    RequestQueue queue;

    static Patient patient;
    DbHelper dbHelper;
    static Helpers helpers;
    public static Activity main;
    Sync sync;

    JSONArray patient_json_array_mysql = null;
    JSONObject patient_json_object_mysql = null;
    AlarmService alarmService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_login);

        ActionBar actionbar = getActionBar();
        setCustomActionBar(actionbar);

        dbHelper = new DbHelper(this);
        helpers = new Helpers();
        alarmService = new AlarmService(this);
        main = this;
        queue = VolleySingleton.getInstance().getRequestQueue();
        sync = new Sync();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        signup = (TextView) findViewById(R.id.signup);
        forgotpw = (TextView) findViewById(R.id.forgot_password);
        login_btn = (Button) findViewById(R.id.login_btn);
        username_txtfield = (EditText) findViewById(R.id.username_txtfield);
        password_txtfield = (EditText) findViewById(R.id.password_txtfield);

        signup.setOnClickListener(this);
        forgotpw.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(name)) {
            if (sharedpreferences.contains(pass)) {
                Intent i = new Intent(this, SidebarActivity.class);
                startActivity(i);
            }
        } else {
            username_txtfield.setText("");
            password_txtfield.setText("");
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                uname = username_txtfield.getText().toString();
                password = password_txtfield.getText().toString();

                if (uname.equals("")) {
                    username_txtfield.setError("Field Required");
                } else if (password.equals("")) {
                    password_txtfield.setError("Field Required");
                } else {
                    pDialog.show();
                    patient = new Patient();
                    patient.setUsername(uname);
                    patient.setPassword(helpers.md5(password));

                    final Map<String, String> params = setParams();

                    CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int success = response.getInt("success");

                                if (success == 1) {
                                    patient_json_array_mysql = response.getJSONArray("patient");
                                    JSONArray checked_json_array = sync.checkWhatToInsert(patient_json_array_mysql, dbHelper.getAllJSONArrayFrom("patients"), "patient_id");
                                    Patient syncedPatient;

                                    if (checked_json_array.length() > 0) {
                                        patient_json_object_mysql = checked_json_array.getJSONObject(0);

                                        syncedPatient = sync.setPatient(patient_json_object_mysql); //sync.setPatient here.
                                        dbHelper.savePatient(patient_json_object_mysql, syncedPatient, "insert"); //then save on db
                                    } else {
                                        JSONObject obj = patient_json_array_mysql.getJSONObject(0);
                                        syncedPatient = sync.setPatient(obj);
                                    }

                                    if (dbHelper.LoginUser(uname, password)) {
                                        //request for patient_prescriptions
                                        GetRequest.getJSONobj(getBaseContext(), "get_prescriptions&patient_id=" + syncedPatient.getServerID(), "patient_prescriptions", "prescriptions_id", new RespondListener<JSONObject>() {
                                            @Override
                                            public void getResult(JSONObject response) {
                                                Log.d("response <MainActivity - prescriptions request >", response + "");
                                            }
                                        }, new ErrorListener<VolleyError>() {
                                            public void getError(VolleyError error) {
                                                Log.d("Error", error + "");
                                                Toast.makeText(getBaseContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        //request for orders request
                                        GetRequest.getJSONobj(getBaseContext(), "get_orders&patient_id=" + syncedPatient.getServerID(), "orders", "orders_id", new RespondListener<JSONObject>() {
                                            @Override
                                            public void getResult(JSONObject response) {
                                                Log.d("response using interface <SplashActivity.java - orders request >", response + "");
                                            }
                                        }, new ErrorListener<VolleyError>() {
                                            public void getError(VolleyError error) {
                                                Log.d("Error", error + "");
                                                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        //request for order_details request
                                        GetRequest.getJSONobj(getBaseContext(), "get_order_details&patient_id=" + syncedPatient.getServerID(), "order_details", "order_details_id", new RespondListener<JSONObject>() {
                                            @Override
                                            public void getResult(JSONObject response) {
                                                Log.d("response using interface <SplashActivity.java - order_details request >", response + "");
                                            }
                                        }, new ErrorListener<VolleyError>() {
                                            public void getError(VolleyError error) {
                                                Log.d("Error", error + "");
                                                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(name, uname);
                                        editor.putString(pass, helpers.md5(password));
                                        editor.commit();

                                        String patient_image_name = patient_json_array_mysql.getJSONObject(0).getString("photo");
                                        if (!patient_image_name.equals(""))
                                            helpers.cacheImageOnly(patient_image_name, patient_json_array_mysql.getJSONObject(0).getInt("id"));

                                        startActivity(new Intent(getBaseContext(), SidebarActivity.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                        System.out.print("error on dbHelper.loginUser <source: MainActivity>");
                                    }
                                } else
                                    Toast.makeText(MainActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Log.d("try catch error", e + "");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.print("src: <MainActivity>: " + error.toString());
                            Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                    pDialog.dismiss();
                    queue.add(jsObjRequest);
                }
                break;

            case R.id.signup:
                startActivity(new Intent(this, ReferralActivity.class));
                break;
        }
    }

    public static Map<String, String> setParams() {
        Map<String, String> params = new HashMap();

        params.put("request", "login");
        params.put("username", patient.getUsername());
        params.put("password", patient.getPassword());
        return params;
    }

    public static void setCustomActionBar(ActionBar actionbar) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5B9A68"));
        actionbar.setBackgroundDrawable(colorDrawable);
        actionbar.setDisplayShowTitleEnabled(false);
    }

    public static void setCustomActionBarWithTitle(ActionBar actionbar) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5B9A68"));
        actionbar.setBackgroundDrawable(colorDrawable);
    }
}
