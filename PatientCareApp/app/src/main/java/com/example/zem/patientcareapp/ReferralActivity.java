package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zem.patientcareapp.Interface.ErrorListener;
import com.example.zem.patientcareapp.Interface.RespondListener;
import com.example.zem.patientcareapp.Network.ListOfPatientsRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User PC on 8/26/2015.
 */

public class ReferralActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, TextWatcher, AdapterView.OnItemClickListener {
    ArrayList<HashMap<String, String>> hashOfUsers, hashOfDoctors;
    ArrayList<String> tempReferrals;
    ArrayAdapter referredAdapter;
    ArrayList<String> listOfReferrals;

    String getText;
    static String getDoctor_referral_id = "", getDoctor_id = "";
    final int signup = 23;
    public static int cpd_id;
    int check;

    RadioButton other, radioReferredBy, ignoreInfo, useInfo;
    LinearLayout linearUsernamePassword;
    EditText referredBy, specifyOthers, autoUname, autoPassword;
    ListView listOfNames;
    Button continueBtn, continueBtn2;

    Dialog dialog1;
    ProgressDialog dialog, dialog_1;

    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.referral_activity_layout);
        super.onCreate(savedInstanceState);

        ActionBar actionbar = getActionBar();
        setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        hashOfUsers = new ArrayList();
        hashOfDoctors = new ArrayList();
        listOfReferrals = new ArrayList();
        tempReferrals = new ArrayList();
        cpd_id = 0;

        other = (RadioButton) findViewById(R.id.other);
        radioReferredBy = (RadioButton) findViewById(R.id.radioReferredBy);
        referredBy = (EditText) findViewById(R.id.referredBy);
        specifyOthers = (EditText) findViewById(R.id.specifyOthers);
        listOfNames = (ListView) findViewById(R.id.listOfNames);
        continueBtn = (Button) findViewById(R.id.continueBtn);

        listOfReferrals.clear();
        referredAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfReferrals);
        listOfNames.setAdapter(referredAdapter);

        getAllReferrals();

        continueBtn.setOnClickListener(this);
        radioReferredBy.setOnCheckedChangeListener(this);
        other.setOnCheckedChangeListener(this);
        referredBy.addTextChangedListener(this);
        listOfNames.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void setCustomActionBar(ActionBar actionbar) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5B9A68"));
        actionbar.setBackgroundDrawable(colorDrawable);
        actionbar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continueBtn:
                check = 0;
                String getReferredByUser = null, getReferredByDoctor = null;

                int pos;

                if (radioReferredBy.isChecked()) {
                    if (referredBy.getText().toString().equals("")) {
                        referredBy.setError("Field required");
                        check += 1;
                    } else {
                        try {
                            if (tempReferrals.contains(referredBy.getText().toString())) {
                                pos = tempReferrals.indexOf(referredBy.getText().toString());

                                if (hashOfUsers.get(pos).get("typeOfUser").equals("doctor")) {
                                    getReferredByDoctor = hashOfUsers.get(pos).get("referral_id");
                                    getDoctor_id = hashOfUsers.get(pos).get("user_id");
                                    getDoctor_referral_id = getReferredByDoctor;
                                    getReferredByUser = "";
                                } else {
                                    getReferredByUser = hashOfUsers.get(pos).get("referral_id");
                                    getReferredByDoctor = "";
                                }
                            } else {
                                check += 1;
                                referredBy.setError("User not found");
                            }
                        } catch (Exception e) {
                            check += 1;
                            Log.d("ReferralActivity", e + "");
                            Toast.makeText(getBaseContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (other.isChecked()) {
                    getReferredByDoctor = "";
                    getReferredByUser = "";
                }

                if (check == 0) {
                    if (!getReferredByDoctor.equals("")) {
                        checkDoctor(getDoctor_id);
                    } else {
                        Intent intent = new Intent(getBaseContext(), EditTabsActivity.class);
                        intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
                        intent.putExtra("referred_by_User", getReferredByUser);
                        intent.putExtra("referred_by_Doctor", getReferredByDoctor);
                        startActivity(intent);
                        this.finish();
                    }
                }
                break;
        }
    }

    public void checkDoctor(final String doctor_id) {
        dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_get_info_from_doctor);
        dialog1.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog1.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        ignoreInfo = (RadioButton) dialog1.findViewById(R.id.ignoreInfo);
        useInfo = (RadioButton) dialog1.findViewById(R.id.useInfo);
        linearUsernamePassword = (LinearLayout) dialog1.findViewById(R.id.linearUsernamePassword);
        autoUname = (EditText) dialog1.findViewById(R.id.autoUname);
        autoPassword = (EditText) dialog1.findViewById(R.id.autoPassword);
        continueBtn2 = (Button) dialog1.findViewById(R.id.continueBtn2);

        ignoreInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ignoreInfo.isChecked())
                    linearUsernamePassword.setVisibility(View.GONE);
                else
                    linearUsernamePassword.setVisibility(View.VISIBLE);
            }
        });
        continueBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ignoreInfo.isChecked()) {
                    Intent intent = new Intent(getBaseContext(), EditTabsActivity.class);
                    intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
                    intent.putExtra("referred_by_User", "");
                    intent.putExtra("referred_by_Doctor", getDoctor_referral_id);
                    startActivity(intent);
                    ReferralActivity.this.finish();
                } else if (useInfo.isChecked()) {
                    if (autoUname.getText().toString().equals(""))
                        autoUname.setError("Field required");
                    else if (autoPassword.getText().toString().equals(""))
                        autoPassword.setError("Field required");
                    else {
                        dialog_1 = new ProgressDialog(ReferralActivity.this);
                        dialog_1.setMessage("Please wait...");
                        dialog_1.show();

                        String name = autoUname.getText().toString();
                        String pass = autoPassword.getText().toString();

                        ListOfPatientsRequest.getJSONobj(ReferralActivity.this, "get_clinic_patients&username=" + name + "&password=" + pass, new RespondListener<JSONObject>() {
                            @Override
                            public void getResult(JSONObject response) {
                                try {
                                    int success = response.getInt("success");

                                    if (success == 1) {
                                        JSONArray json_mysql = response.getJSONArray("clinic_patients");
                                        JSONObject obj = json_mysql.getJSONObject(0);

                                        if (doctor_id.equals(obj.getString("doctor_id"))) {
                                            if (obj.getInt("patient_id") == 0) {
                                                Bundle ptnt = new Bundle();
                                                ptnt.putString("fname", obj.getString("fname"));
                                                ptnt.putString("mname", obj.getString("mname"));
                                                ptnt.putString("lname", obj.getString("lname"));
                                                ptnt.putString("mobile_no", obj.getString("mobile_no"));
                                                ptnt.putString("tel_no", obj.getString("tel_no"));
                                                ptnt.putString("occupation", obj.getString("occupation"));
                                                ptnt.putString("birthdate", obj.getString("birthdate"));
                                                ptnt.putString("sex", obj.getString("sex"));
                                                ptnt.putString("civil_status", obj.getString("civil_status"));
                                                ptnt.putString("height", obj.getString("height"));
                                                ptnt.putString("weight", obj.getString("weight"));
                                                ptnt.putString("optional_address", obj.getString("optional_address"));
                                                ptnt.putString("address_street", obj.getString("address_street"));
                                                ptnt.putString("barangay_id", obj.getString("address_barangay_id"));
                                                ptnt.putString("municipality_id", obj.getString("municipality_id"));
                                                ptnt.putString("province_id", obj.getString("province_id"));
                                                ptnt.putString("region_id", obj.getString("region_id"));

                                                cpd_id = obj.getInt("cpd_id");

                                                Intent intent = new Intent(getBaseContext(), EditTabsActivity.class);
                                                intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
                                                intent.putExtra("referred_by_User", "");
                                                intent.putExtra("referred_by_Doctor", getDoctor_referral_id);
                                                intent.putExtras(ptnt);
                                                startActivity(intent);
                                                ReferralActivity.this.finish();
                                            } else
                                                Toast.makeText(getBaseContext(), "The credentials provided have already been used", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(getBaseContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getBaseContext(), "Incorrect Username/Password", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(ReferralActivity.this, e + "", Toast.LENGTH_SHORT).show();
                                    Log.d("ReferralAct0", e + "");
                                }
                                dialog_1.dismiss();
                            }
                        }, new ErrorListener<VolleyError>() {
                            public void getError(VolleyError error) {
                                dialog_1.dismiss();
                                Log.d("ReferralAct1", error + "");
                                Toast.makeText(ReferralActivity.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (radioReferredBy.isChecked()) {
            specifyOthers.setVisibility(View.GONE);
            referredBy.setVisibility(View.VISIBLE);
            listOfNames.setVisibility(View.INVISIBLE);
        } else if (other.isChecked()) {
            specifyOthers.setVisibility(View.VISIBLE);
            referredBy.setVisibility(View.INVISIBLE);
            listOfNames.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        getText = referredBy.getText().toString();

        if (getText.equals(""))
            listOfNames.setVisibility(View.INVISIBLE);
        else
            listOfNames.setVisibility(View.VISIBLE);

        listOfReferrals.clear();

        for (final String name : tempReferrals) {
            if (name.toLowerCase().contains(getText.toLowerCase())) {
                int index = tempReferrals.indexOf(name);

                listOfReferrals.add(tempReferrals.get(index));
            }
            referredAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemClicked = String.valueOf(parent.getItemAtPosition(position));
        referredBy.setText(itemClicked);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void getAllReferrals() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();

        hashOfDoctors = db.getAllDoctors();
        for (int d = 0; d < hashOfDoctors.size(); d++) {
            HashMap<String, String> doc = new HashMap();
            doc.put("fullname", hashOfDoctors.get(d).get("fullname"));
            doc.put("referral_id", hashOfDoctors.get(d).get("referral_id"));
            doc.put("user_id", hashOfDoctors.get(d).get("doc_id"));
            doc.put("typeOfUser", "doctor");
            hashOfUsers.add(doc);
        }

        //for patients
        ListOfPatientsRequest.getJSONobj(getBaseContext(), "get_patients", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                try {
                    JSONArray json_array_mysql = response.getJSONArray("patients");
                    for (int x = 0; x < json_array_mysql.length(); x++) {
                        JSONObject json_obj = json_array_mysql.getJSONObject(x);
                        HashMap<String, String> map = new HashMap();
                        map.put("fullname", json_obj.getString("fname") + " " + json_obj.getString("lname"));
                        map.put("referral_id", json_obj.getString("referral_id"));
                        map.put("user_id", json_obj.getString("id"));
                        map.put("typeOfUser", "patient");
                        hashOfUsers.add(map);
                    }

                    for (int s = 0; s < hashOfUsers.size(); s++) {
                        String name;

                        if (hashOfUsers.get(s).get("typeOfUser").equals("doctor"))
                            name = hashOfUsers.get(s).get("fullname") + " (" + hashOfUsers.get(s).get("referral_id") + ")";
                        else
                            name = hashOfUsers.get(s).get("fullname") + " (" + hashOfUsers.get(s).get("referral_id") + ")";

                        listOfReferrals.add(name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tempReferrals.addAll(listOfReferrals);

                dialog.dismiss();
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("ReferralAct2", error + "");
                Toast.makeText(getBaseContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}