package com.example.zem.patientcareapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeTileActivity extends Activity implements View.OnClickListener {
    Button profile_btn, news_btn, promos_btn, doctors_btn, history_btn, test_results_btn, cart_btn, products_btn, consultation_btn;
    FragmentTransaction fragmentTransaction;

    public static SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static String uname;
    public static int userID;
    public static Activity hometile;

    static Patient patient;
    static DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tile_layout);

        hometile = this;

        sharedpreferences = getSharedPreferences
                (MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        dbHelper = new DbHelper(this);

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

        if (dbHelper.checkUserIfRegistered(getUname()) > 0) {

        } else {
            editor.clear();
            editor.commit();
            moveTaskToBack(true);
            HomeTileActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MasterTabActivity.class);
        switch (v.getId()) {

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
                break;
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.main.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            editor.clear();
            editor.commit();
            moveTaskToBack(true);
            HomeTileActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public static String getUname() {
        uname = sharedpreferences.getString("nameKey", "DEFAULT");
        return uname;
    }

    public static int getUserID() {
        patient = dbHelper.getloginPatient(getUname());
        userID = patient.getServerID();

        return userID;
    }
}
