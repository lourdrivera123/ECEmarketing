package com.example.zem.patientcareapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.zem.patientcareapp.adapter.LazyAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zem on 10/2/2015.
 */
public class OrdersActivity extends Activity {

    ListView lv_items;
    LazyAdapter adapter;
    ArrayList<HashMap<String, String>> items;
    DbHelper dbHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.orders_layout);

        ActionBar actionbar = getActionBar();
        MainActivity.setCustomActionBarWithTitle(actionbar);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeButtonEnabled(true);
        actionbar.setTitle("Recent Orders");

        context = getBaseContext();
        dbHelper = new DbHelper(OrdersActivity.this);

        lv_items = (ListView) findViewById(R.id.lv_items);

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
