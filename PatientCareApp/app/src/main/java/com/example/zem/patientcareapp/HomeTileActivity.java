package com.example.zem.patientcareapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Zem on 5/2/2015.
 */
public class HomeTileActivity extends ActionBarActivity implements View.OnClickListener {
    Button profile_btn, news_btn, promos_btn, doctors_btn, history_btn, test_results_btn, cart_btn, products_btn, consultation_btn;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tile_layout);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        profile_btn = (Button) findViewById(R.id.profile_btn);
        news_btn = (Button) findViewById(R.id.news_btn);
        promos_btn = (Button) findViewById(R.id.promos_btn);
        doctors_btn = (Button) findViewById(R.id.doctors_btn);
        history_btn = (Button) findViewById(R.id.history_btn);
        test_results_btn = (Button) findViewById(R.id.test_results_btn);
        cart_btn = (Button) findViewById(R.id.cart_btn);
        products_btn = (Button) findViewById(R.id.products_btn);
        consultation_btn = (Button) findViewById(R.id.consultation_btn);

        consultation_btn.setOnClickListener(this);
        profile_btn.setOnClickListener(this);
        news_btn.setOnClickListener(this);
        promos_btn.setOnClickListener(this);
        doctors_btn.setOnClickListener(this);
        history_btn.setOnClickListener(this);
        test_results_btn.setOnClickListener(this);
        cart_btn.setOnClickListener(this);
        products_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MasterTabActivity.class);
        switch(v.getId()) {

            case R.id.profile_btn:
                intent.putExtra("selected", 0);
                startActivity(intent);
                break;

            case R.id.history_btn:
                intent.putExtra("selected", 1);
                startActivity(intent);
                break;

            case R.id.test_results_btn:
                intent.putExtra("selected", 2);
                startActivity(intent);
                break;

            case R.id.doctors_btn:
                intent.putExtra("selected", 3);
                startActivity(intent);
                break;

            case R.id.consultation_btn:
                intent.putExtra("selected", 4);
                startActivity(intent);
                break;

            case R.id.products_btn:
                intent.putExtra("selected", 5);
                startActivity(intent);
                break;

            case R.id.cart_btn:
                intent.putExtra("selected", 6);
                startActivity(intent);
                break;

            case R.id.promos_btn:
                intent.putExtra("selected", 7);
                startActivity(intent);
                break;

            case R.id.news_btn:
                intent.putExtra("selected", 8);
                startActivity(intent);
                break;
        }
    }
}
