package com.example.zem.patientcareapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by Zem on 4/30/2015.
 */
public class MasterTabActivity extends TabActivity implements View.OnClickListener {
    Button edit_patient_profile_btn;
    public static final String TAB_REQUEST = "request";
    public static final int TAB = -1;

    int current_tab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_tab_layout);

        edit_patient_profile_btn = (Button) findViewById(R.id.edit_patient_profile_btn);
        edit_patient_profile_btn.setOnClickListener(this);

        Intent intent = getIntent();
        current_tab = intent.getIntExtra(TAB_REQUEST, -1);

        TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);

        mTabHost.addTab(mTabHost.newTabSpec("first").setIndicator("Profile").setContent(new Intent(this , PatientProfileActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("second").setIndicator("Doctors").setContent(new Intent(this  ,PatientHomeActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("third").setIndicator("My Records").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("fourth").setIndicator("News").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("fifth").setIndicator("Promos").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("sixth").setIndicator("Test Results").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("seventh").setIndicator("Products").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("eight").setIndicator("Cart").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.addTab(mTabHost.newTabSpec("ninth").setIndicator("Consultation Schedule").setContent(new Intent(this , PatientHistoryActivity.class )));
        mTabHost.setCurrentTab(current_tab);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
