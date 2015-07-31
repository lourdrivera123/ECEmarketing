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
import com.android.volley.toolbox.Volley;
import com.example.zem.patientcareapp.GetterSetter.Patient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener {
    TextView signup, forgotpw;
    Button login_btn;

    EditText username_txtfield, password_txtfield;

    DbHelper dbHelper;
    Helpers helpers;
    public static Activity main;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    String uname, password;
    String url = "http://vinzry.0fees.us/db/post.php";

    ProgressDialog pDialog;
    RequestQueue queue;

    Patient patient;
    Sync sync;

    JSONArray patient_json_array_mysql = null;
    JSONObject patient_json_object_mysql = null;
    ArrayList<HashMap<String, String>> listOfAllConsultations;
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
        queue = Volley.newRequestQueue(this);
        sync = new Sync();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        signup = (TextView) findViewById(R.id.signup);
        forgotpw = (TextView) findViewById(R.id.forgot_password);
        login_btn = (Button) findViewById(R.id.login_btn);
        username_txtfield = (EditText) findViewById(R.id.username_txtfield);
        password_txtfield = (EditText) findViewById(R.id.password_txtfield);

        login_btn.setBackgroundColor(0xFF5B9A68);
        signup.setOnClickListener(this);
        forgotpw.setOnClickListener(this);
        login_btn.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (sharedpreferences.contains(name)) {
            if (sharedpreferences.contains(pass)) {
//                helpers.showNotification(this, "Title ni diri", "Diri dapita ang mensahe");
                Intent i = new Intent(this, HomeTileActivity.class);
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
                    patient.setPassword(password);
                    if (helpers.isNetworkAvailable(getBaseContext())) {

                        final Map<String, String> params = setParams();

                        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, params,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {

                                            int success = response.getInt("success");
                                            if (success == 1) {
                                                patient_json_array_mysql = response.getJSONArray("patient");


                                                JSONArray checked_json_array = sync.checkWhatToInsert(patient_json_array_mysql, dbHelper.getAllJSONArrayFrom("patients"), "patient_id");
                                                Log.d("checked json array", "" + checked_json_array);

                                                if (checked_json_array.length() > 0) {
                                                    //json object from server
                                                    patient_json_object_mysql = checked_json_array.getJSONObject(0);

                                                    //sync.setPatient here.
                                                    Patient syncedPatient = sync.setPatient(patient_json_object_mysql);

                                                    //then save on db
                                                    dbHelper.insertPatient(patient_json_object_mysql, syncedPatient);

                                                }
                                                if (dbHelper.LoginUser(uname, password)) {
                                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                                    editor.putString(name, uname);
                                                    editor.putString(pass, password);
                                                    editor.commit();
//                                                    helpers.showNotification(getBaseContext(), "Title ni diri", "Mao ni diri ang Mensahe");
                                                    Intent i = new Intent(getBaseContext(), HomeTileActivity.class);
                                                    startActivity(i);
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                                }

                                                pDialog.hide();
                                            } else {
                                                Toast.makeText(MainActivity.this, "Invalid Username or Password ", Toast.LENGTH_SHORT).show();
                                            }

                                        } catch (JSONException e) {
                                            Toast.makeText(MainActivity.this, "error: " + e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pDialog.hide();
                                Toast.makeText(getBaseContext(), "error" + error.toString(), Toast.LENGTH_SHORT).show();

                                Log.d("volley error", "" + error.toString());
                            }
                        });

                        queue.add(jsObjRequest);

                    } else {
                        Toast.makeText(getBaseContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.signup:
                int signup = 23;

                Intent intent = new Intent(this, EditTabsActivity.class);
                intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
                startActivity(intent);
                break;
        }
    }

    public Map<String, String> setParams() {
        Map<String, String> params = new HashMap<String, String>();

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
