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

    DbHelper dbHelper;
    static Helpers helpers;
    public static Activity main;

    public static final String MyPREFERENCES = "MyPrefs";
    public static SharedPreferences sharedpreferences;
    public static final String name = "nameKey", pass = "passwordKey";
    String uname, password;
    String url = "http://192.168.177.1/db/post.php";

    ProgressDialog pDialog;
    RequestQueue queue;

    static Patient patient;
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
                            Log.d("ifsuccess", response + "");
                            Log.d("response on jrequest", response + "");

                            try {
                                int success = response.getInt("success");

                                if (success == 1) {
                                    patient_json_array_mysql = response.getJSONArray("patient");

                                    JSONArray checked_json_array = sync.checkWhatToInsert(patient_json_array_mysql, dbHelper.getAllJSONArrayFrom("patients"), "patient_id");

                                    if (checked_json_array.length() > 0) {
                                        patient_json_object_mysql = checked_json_array.getJSONObject(0);

                                        //sync.setPatient here.
                                        Patient syncedPatient = sync.setPatient(patient_json_object_mysql);

                                        //then save on db
                                        dbHelper.savePatient(patient_json_object_mysql, syncedPatient, "insert");
                                    }
                                    if (dbHelper.LoginUser(uname, password)) {
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(name, uname);
                                        editor.putString(pass, helpers.md5(password));
                                        editor.commit();

                                        startActivity(new Intent(getBaseContext(), SidebarActivity.class));

                                    } else {
                                        Toast.makeText(MainActivity.this, "Invalid Username of Password", Toast.LENGTH_SHORT).show();
                                        System.out.print("error on dbHelper.loginUser <source: MainActivity.java>");
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_SHORT).show();
                                }
                                pDialog.dismiss();

                            } catch (JSONException e) {
                                Log.d("try catch error", e + "");
                                Toast.makeText(MainActivity.this, "error: " + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                            Log.d("custom request error", error + "");
                            Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_SHORT).show();

                        }
                    });

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
}
