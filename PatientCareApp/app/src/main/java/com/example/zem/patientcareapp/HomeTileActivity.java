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
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeTileActivity extends Activity implements View.OnClickListener {
    Button profile_btn, news_btn, promos_btn, doctors_btn, history_btn, test_results_btn, cart_btn, products_btn, consultation_btn;
    FragmentTransaction fragmentTransaction;
    DbHelper dbHelper;

//    JSONArray doctors_json_array_mysql = null;
//    JSONArray doctors_json_array_sqlite = null;
//    JSONArray doctors_json_array_final = null;
//
//    RequestQueue queue;
//    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_tile_layout);

//        queue = Volley.newRequestQueue(this);
        dbHelper = new DbHelper(this);
        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        url = "http://192.168.10.1/db/get_all_doctors.php";

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
                Toast.makeText(this, ""+dbHelper.getAllDoctorsJSONArray(), Toast.LENGTH_LONG).show();
//                startActivity(intent);
                break;
        }
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
            SharedPreferences sharedpreferences = getSharedPreferences
                    (MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            moveTaskToBack(true);
            HomeTileActivity.this.finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
