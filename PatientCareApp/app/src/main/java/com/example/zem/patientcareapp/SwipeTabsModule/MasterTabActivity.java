package com.example.zem.patientcareapp.SwipeTabsModule;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zem.patientcareapp.Controllers.DbHelper;
import com.example.zem.patientcareapp.Activities.MainActivity;
import com.example.zem.patientcareapp.Controllers.OverlayController;
import com.example.zem.patientcareapp.R;
import com.example.zem.patientcareapp.adapter.MasterTabsAdapter;


public class MasterTabActivity extends FragmentActivity implements ActionBar.TabListener {
    private MasterTabsAdapter mAdapter;
    private String[] tabs = {"Refills & Renewals", "Referral Points", "Prescription", "Consultation"};
    private ViewPager viewPager;
    private ActionBar actionBar;

    ImageView swipeLeftRight;

    DbHelper dbHelper;
    OverlayController oc;
    Intent intent;
    int unselected = 0;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_tab_layout);
        dbHelper = new DbHelper(this);
        oc = new OverlayController(this);

        actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
        showOverLay();

        viewPager = (ViewPager) findViewById(R.id.pager);
        mAdapter = new MasterTabsAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        intent = getIntent();
        actionBar.setSelectedNavigationItem(intent.getIntExtra("selected", 0));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(final ActionBar.Tab tab, FragmentTransaction ft) {
        mAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        unselected = tab.getPosition();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    private void showOverLay() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.mastertabs_overlay);

        if (oc.checkOverlay("MasterTabs", "check") == false) {
            swipeLeftRight = (ImageView) dialog.findViewById(R.id.swipeLeftRight);
            LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.masterTabsLayout);
            layout.setAlpha((float) 0.8);

            swipeLeftRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (oc.checkOverlay("MasterTabs", "insert"))
                        dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
