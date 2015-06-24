package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.zem.patientcareapp.adapter.FullScreenImageAdapter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by User PC on 6/23/2015.
 */
public class FullScreenViewActivity extends Activity {
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    ArrayList<String> getUriItems;

    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_full_screen);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);

        getUriItems = PrescriptionFragment.uriItems;
        Log.i("getUriItems", "" + getUriItems);

        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, getUriItems);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}
