package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by User PC on 9/21/2015.
 */

public class CheckoutActivity extends Activity {
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkoutform);

        actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
    }
}
