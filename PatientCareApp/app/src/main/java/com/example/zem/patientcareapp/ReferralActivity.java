package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
    final int signup = 23;
    int check;

    RadioButton other, radioReferredBy;
    EditText referredBy, specifyOthers;
    ListView listOfNames;
    Button continueBtn;

    ProgressDialog dialog;

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

    public static void setCustomActionBar(ActionBar actionbar) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5B9A68"));
        actionbar.setBackgroundDrawable(colorDrawable);
        actionbar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
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
            Intent intent = new Intent(getBaseContext(), EditTabsActivity.class);
            intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
            intent.putExtra("referred_by_User", getReferredByUser);
            intent.putExtra("referred_by_Doctor", getReferredByDoctor);
            startActivity(intent);
        }
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
                        map.put("typeOfUser", "patient");
                        hashOfUsers.add(map);
                    }

                    for (int s = 0; s < hashOfUsers.size(); s++) {
                        String name;

                        if (hashOfUsers.get(s).get("typeOfUser").equals("doctor"))
                            name = "Dr. " + hashOfUsers.get(s).get("fullname") + " (" + hashOfUsers.get(s).get("referral_id") + ")";
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
                System.out.print("Error in ReferralActivity" + error);
                Toast.makeText(getBaseContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
//
//    private void populateTable() {
//        runOnUiThread(new Runnable(){
//            public void run() {
//                //If there are stories, add them to the table
//                for (Parcelable currentHeadline : allHeadlines) {
//                    addHeadlineToTable(currentHeadline);
//                }
//                try {
//                    dialog.dismiss();
//                } catch (final Exception ex) {
//                    Log.i("---","Exception in thread");
//                }
//            }
//        });
//    }
}