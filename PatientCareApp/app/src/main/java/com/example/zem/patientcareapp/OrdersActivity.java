package com.example.zem.patientcareapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.zem.patientcareapp.adapter.LazyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 10/2/2015.
 */
public class OrdersActivity extends AppCompatActivity {

    ListView lv_items;
    Toolbar ordersToolbar;

    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> items;
    DbHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_layout);

        lv_items = (ListView) findViewById(R.id.lv_items);
        ordersToolbar = (Toolbar) findViewById(R.id.ordersToolbar);

        setSupportActionBar(ordersToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Recent Orders");
        ordersToolbar.setNavigationIcon(R.drawable.ic_back);

        context = getBaseContext();
        dbHelper = new DbHelper(OrdersActivity.this);

        items = dbHelper.getAllOrderItems(); // returns all basket items for the currently loggedin patient

        adapter = new LazyAdapter(OrdersActivity.this, items, "order_items");
        lv_items.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SidebarActivity.class));
        this.finish();

        return super.onOptionsItemSelected(item);
    }
}
