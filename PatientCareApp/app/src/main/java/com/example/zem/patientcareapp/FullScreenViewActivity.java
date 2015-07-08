package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.zem.patientcareapp.adapter.FullScreenImageAdapter;

import android.app.Activity;
import android.view.MenuItem;

import java.util.ArrayList;

public class FullScreenViewActivity extends Activity {
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    ArrayList<Bitmap> getAllBitmap;

    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_full_screen);

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionbar = getActionBar();
        MainActivity.setCustomActionBar(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);

        getAllBitmap = PrescriptionFragment.allBitmap;
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, getAllBitmap);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
