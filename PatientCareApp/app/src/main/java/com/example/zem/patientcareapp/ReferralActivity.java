package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    ArrayList<HashMap<String, String>> hashOfReferrals;
    ArrayList<String> tempReferrals;
    ArrayAdapter referredAdapter;
    ArrayList<String> listOfReferrals;

    String getText;
    final int signup = 23;
    int check;

    RadioButton other, radioReferredBy;
    EditText referredBy;
    ListView listOfNames;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.referral_activity_layout);
        super.onCreate(savedInstanceState);

        ActionBar actionbar = getActionBar();
        setCustomActionBar(actionbar);

        hashOfReferrals = new ArrayList();
        listOfReferrals = new ArrayList();
        tempReferrals = new ArrayList();

        other = (RadioButton) findViewById(R.id.other);
        radioReferredBy = (RadioButton) findViewById(R.id.radioReferredBy);
        referredBy = (EditText) findViewById(R.id.referredBy);
        listOfNames = (ListView) findViewById(R.id.listOfNames);
        continueBtn = (Button) findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener(this);
        radioReferredBy.setOnCheckedChangeListener(this);
        other.setOnCheckedChangeListener(this);
        referredBy.addTextChangedListener(this);
        listOfNames.setOnItemClickListener(this);

        ListOfPatientsRequest.getJSONobj(getBaseContext(), "get_patients", new RespondListener<JSONObject>() {
            @Override
            public void getResult(JSONObject response) {
                Log.d("response using interface <MainActivity.java >", response + "");

                try {
                    JSONArray json_array_mysql = response.getJSONArray("patients");
                    for (int x = 0; x < json_array_mysql.length(); x++) {
                        try {
                            JSONObject json_obj = json_array_mysql.getJSONObject(x);
                            HashMap<String, String> map = new HashMap();
                            map.put("fullname", json_obj.getString("fname") + " " + json_obj.getString("lname"));
                            map.put("referral_id", json_obj.getString("referral_id"));
                            hashOfReferrals.add(map);

                            String name = hashOfReferrals.get(x).get("fullname") + " (" + hashOfReferrals.get(x).get("referral_id") + ")";
                            listOfReferrals.add(name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    tempReferrals.addAll(listOfReferrals);

                    referredAdapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_dropdown_item_1line, listOfReferrals);
                    listOfNames.setAdapter(referredAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener<VolleyError>() {
            public void getError(VolleyError error) {
                Log.d("Error in ReferralActivity", error + "");
                Toast.makeText(getBaseContext(), "Couldn't refresh list. Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void setCustomActionBar(ActionBar actionbar) {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5B9A68"));
        actionbar.setBackgroundDrawable(colorDrawable);
        actionbar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onClick(View v) {
        String getReferred = null;
        int pos;
        check = 0;

        if (radioReferredBy.isChecked()) {
            try {
                if (referredBy.getText().toString().equals("")) {
                    referredBy.setError("Field required");
                    check += 1;
                } else {
                    pos = listOfReferrals.indexOf(referredBy.getText().toString());
                    getReferred = hashOfReferrals.get(pos).get("referral_id");
                }
            } catch (Exception e) {
                check += 1;
                Toast.makeText(getBaseContext(), "name is not on the list", Toast.LENGTH_SHORT).show();
            }
        } else if (other.isChecked()) {
            getReferred = null;
        }

        if (check == 0) {
            Intent intent = new Intent(getBaseContext(), EditTabsActivity.class);
            intent.putExtra(EditTabsActivity.SIGNUP_REQUEST, signup);
            intent.putExtra("referred_by", getReferred);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (radioReferredBy.isChecked()) {
            referredBy.setVisibility(View.VISIBLE);
            listOfNames.setVisibility(View.VISIBLE);
        } else if (other.isChecked()) {
            referredBy.setVisibility(View.INVISIBLE);
            listOfNames.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        getText = referredBy.getText().toString();
        listOfReferrals.clear();

        for (String name : tempReferrals) {
            if (name.toLowerCase().contains(getText.toLowerCase())) {
                int index = tempReferrals.indexOf(name);

                listOfReferrals.add(tempReferrals.get(index));
                referredAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String itemClicked = String.valueOf(parent.getItemAtPosition(position));
        referredBy.setText(itemClicked);
    }
}