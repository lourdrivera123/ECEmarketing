package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.zem.patientcareapp.Fragment.ShoppingCartFragment;
import com.example.zem.patientcareapp.adapter.MasterTabsAdapter;


public class MasterTabActivity extends FragmentActivity implements ActionBar.TabListener {
    private MasterTabsAdapter mAdapter;
    private String[] tabs = {"Profile", "My Records", "Prescription", "Doctors", "Consultation", "Products", "Cart", "Promos", "News"};
    private ViewPager viewPager;
    private ActionBar actionBar;

    DbHelper dbHelper;
    Intent intent;
    int unselected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.master_tab_layout);
        dbHelper = new DbHelper(this);

        actionBar = getActionBar();
        MainActivity.setCustomActionBar(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mastertabs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_menu) {
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            MasterTabActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));

        } else if (item.getItemId() == R.id.edit_profile) {
            int edit = 7;

            Intent intent = new Intent(this, EditTabsActivity.class);
            intent.putExtra(EditTabsActivity.EDIT_REQUEST, edit);
            startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (tab.getPosition() == 6) {
            new ShoppingCartFragment();
        }
        mAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        unselected = tab.getPosition();
        new ShoppingCartFragment();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (tab.getPosition() == 6) {
            new ShoppingCartFragment();
        }
    }
}
